package main;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.widgets.Display;

import app.FswCmdNetwork;
import app.FswTlmNetwork;
import ccsds.CcsdsTlmPkt;
import curval.PktObserver;
import curval.TlmPktDatabase;
import fsw.CmdPkt;
import fsw.ToApp;


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
	private final int DisplayStreamId;
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

		createTlmMonitorThread();
		enableToLabTelemetry();
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

	/**
	 * ~MY ATTEMPT TO LET OTHER APPS SEND NOOPS~ 
	 * Uses the input CmdPkt to tell CFE that the App described in the CmdPkt
	 * should be listened to. (i.e. NoOps show up in Cygwin).
	 * 
	 * @param addPktCmdPkt
	 *            The CmdPkt containing details to allow an App to send NoOps.
	 */
	/** assumed functional, if parameter is valid **/
	public final void addAppFunctionality(CmdPkt addPktCmdPkt) {
		/** NOT functional **/
		// dPkt AnAppsEnableTlmPkt = new CmdPkt(prefix, "Add Pkt", appID, 2, 7);
		// AnAppsEnableTlmPkt.addParam(new CmdIntParam("Message ID", new ParameterDetails(true), TLM_MID_HK + "", 2));
		// AnAppsEnableTlmPkt.addParam(new CmdIntParam("Pkt Size", new ParameterDetails(true), "50", 2));
		// AnAppsEnableTlmPkt.addParam(new CmdIntParam("SB QoS", new ParameterDetails(true), "0", 2));
		// AnAppsEnableTlmPkt.addParam(new CmdIntParam("Buffer Cnt",new ParameterDetails(true), "1", 1));
		// AnAppsEnableTlmPkt.loadParamList();
		// NetWorker.getNetwork().addAppFunctionality(enableTlmPkt);

		sendPkt(addPktCmdPkt);
	}

	/** functional **/
	private final static class TlmListener implements PktObserver {
		public final void update(int StreamId) {
			System.out.println("TlmListener called for packet " + StreamId);
		}
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
									// logFswEventMessage(EVS.getTlmStr(TlmPkt));
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