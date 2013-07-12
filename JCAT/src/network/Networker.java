package network;

import java.util.Timer;
import java.util.TimerTask;

import main.Launcher;

import org.eclipse.swt.widgets.Display;

import applications.ToApp;

import packets.ccsds.CcsdsTlmPkt;
import packets.cmd.CmdPkt;

import curval.PktObserver;
import curval.TlmPktDatabase;

/**
 * This is what I got after I removed the GUI from JcatApp and all of the
 * logging. Currently, when it runs, Cygwin registers TO's NOOP cmds, but not
 * NOOP cmds from other Apps (I'll work on commands with parameters later). I
 * don't know how to get Cygwin to display NOOPs from other Apps.
 * 
 * Another detail: The terminal is automatically filled with "Notifying Observers \n
 * TlmListener called for packet 2### \n addTlmMsg() - 8## \n Notifying Observers \n"
 * until JCAT is exited.
 * 
 * This class is currently only used within an App instance, in a way similar to the 
 * failed implementation within addAppFunctionality(addPktCmdPkt).
 */
public class Networker {
	private final ToApp TO;
	private final FswCmdNetwork CmdWriter;
	private final TlmPktDatabase TlmDatabase;
	private final FswTlmNetwork TlmReader;
	private final TlmListener TlmDisplay;
	private int DisplayStreamId;
	private static Networker networker = new Networker();

	/** functional **/
	public static Networker getNetworker() //Allows every App/CmdPkt to call the same instance of Networker
	{
		return networker;
	}
	
	/** functional **/
	public Networker() {
		TO = new ToApp(ToApp.PREFIX_STR, "Telemetry Output");
		CmdWriter = new FswCmdNetwork();
		TlmDatabase = new TlmPktDatabase();
		TlmReader = new FswTlmNetwork(TlmDatabase);
		TlmDisplay = new TlmListener();
		DisplayStreamId = 0;

		TlmDatabase.registerObserver(TlmDisplay);
        //DisplayStreamId = 0x0F00; /*ExApp.TLM_MID_PKT1;*/
        //DisplayStreamId = 0x0800; /*EsApp.TLM_MID_HK;*/
		createTlmMonitorThread();
		enableToLabTelemetry();
        DisplayStreamId = 0x0F00; /*ExApp.TLM_MID_PKT1;*/
        DisplayStreamId = 0x0800; /*EsApp.TLM_MID_HK;*/
	}

	/** functional **/
	private final void enableToLabTelemetry() {
		CmdPkt Cmd = TO.getEnaTlmCmd();
		CmdWriter.sendCmd(Cmd.getCcsdsPkt());
	}

	/** functional **/
	public final void sendPkt(CmdPkt cmdPkt) {
		CmdWriter.sendCmd(cmdPkt.getCcsdsPkt());
	}

	/** functional **/
	private final static class TlmListener implements PktObserver {
		public final void update(int StreamId) {
			System.out.println("TlmListener called for packet " + StreamId);
		}
	}

	public final static class EVS
	{
	/*new EvsApp(EvsApp.PREFIX_STR,"cFE Event Service");*/

	   public static String getTlmStr(CcsdsTlmPkt TlmMsg) 
	   {

	      byte[] TlmPkt = TlmMsg.getPacket();
	      
	      // @todo - Having trouble with format and no time to debug
	      String MsgA = new String(TlmPkt,12,122); // OS_MAX_API_NAME = 20, CFE_EVS_MAX_MESSAGE_LENGTH = 122
	      String MsgB = new String(TlmPkt,44,122); // OS_MAX_API_NAME = 20, CFE_EVS_MAX_MESSAGE_LENGTH = 122
	      String MsgStr  = MsgA.substring(0,MsgA.indexOf('\0')) + ": " + MsgB.substring(0,MsgB.indexOf('\0')) + "\n";
	    
	      return MsgStr; 
	      
	   } // getTlmStr
	}
	
	/** assumed functional **/
	private final void createTlmMonitorThread() {
		Timer timer = new Timer();
		TimerTask TlmTask = new TimerTask() {
			public void run() {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						synchronized (TlmReader) {
							while (!TlmReader.getTlmPktQ().isEmpty()) {
								CcsdsTlmPkt TlmPkt = TlmReader.getTlmPktQ()
										.remove();

								System.out
										.println("TlmMonitor dequeued packet "
												+ TlmPkt.getStreamId());

								// @todo - Create efficient StreamId to app
								// lookup scheme
								if (TlmPkt.getStreamId() == 0x0808) {
									Launcher.addEvent(EVS.getTlmStr(TlmPkt));
								}
								if (TlmPkt.getStreamId() == DisplayStreamId) {
									String[] OutputStr = new String[] { "ES" };
									// .getTlmStrArray(TlmPkt);
									// Logger.logTlmPacket(OutputStr, 40);
									// ExApp EX = new
									// ExApp(ExApp.PREFIX_STR,"Example Application");
									// logTlmPacket(EX.getTlmStr(TlmPkt));
								}

							} // End while packets in queue
						}

					} // End run()
				}); // End Runnable()
			} // End run()
		}; // End TimerTask()
		timer.scheduleAtFixedRate(TlmTask, 0, 10);
	}
}