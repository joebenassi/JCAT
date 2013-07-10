/*******************************************************************************
 * 
 * This application provides a simple command and telemetry interface to the 
 * cFE.
 * 
 * @todo - Replace GUI  
 *
 *******************************************************************************/

package main;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.widgets.Display;

import app.FswCmdNetwork;
import app.FswTlmNetwork;
import ccsds.CcsdsTlmPkt;
import curval.PktObserver;
import curval.TlmPktDatabase;
import fsw.CmdParam;
import fsw.CmdPkt;
import fsw.EsApp;
import fsw.ToApp;

/**
 *  
 */
public class JcatAppCopy {
	private ToApp TO = new ToApp(ToApp.PREFIX_STR, "Telemetry Output");

	FswCmdNetwork CmdWriter = null;
	FswTlmNetwork TlmReader = null;

	private TlmListener TlmDisplay;
	private TlmPktDatabase TlmDatabase = null;

	private int DisplayStreamId = 0;

	public JcatAppCopy() {
		TlmDatabase = new TlmPktDatabase();
		TlmDisplay = new TlmListener();
		TlmDatabase.registerObserver(TlmDisplay);
	}

	public void initNetwork() {
		Logger.logUserActivity("Initialize Network", true);
		CmdWriter = new FswCmdNetwork();
		TlmReader = new FswTlmNetwork(TlmDatabase);
		Logger.logNetworkStatus();

		createTlmMonitorThread();
	}

	public void enableToLabTelemetry() {
		Logger.logUserActivity("Enable TO Lab Telemetry", true);
		CmdPkt Cmd = TO.getCmdList().get(ToApp.CMD_FC_ENA_TLM);
		CmdWriter.sendCmd(Cmd.getCcsdsPkt());
	}

	/*
	 * Configure (enable/disable) which packets are output by TO Lab
	 */
	public void configureToLabPacketFilter() {
		Logger.logUserActivity("Configure TO Lab Packets", true);
		CmdPkt Cmd = TO.getCmdList().get(ToApp.CMD_FC_ADD_PKT); // Use default
		CmdWriter.sendCmd(Cmd.getCcsdsPkt());
		// @todo - Fix CmdPkt bug. The default CmdParam are not actually loaded
		// into the parameter list
	}

	public void sendUserSelectedCommand() {
		int cmdID;
		String cmdParam = null;
		CmdPkt cmdPkt = null;
		ArrayList<CmdParam> cmdParamList;

		cmdID = 0;// cmdDialog.open();
		// cmdParam = cmdDialog.getCmdParam();

		switch (cmdID) {
		case 0:
			Logger.logUserActivity("EVS Cmd: Noop", true);
			// cmdPkt = EVS.getCmdList().get(EvsApp.CMD_FC_NOOP);
			CmdWriter.sendCmd(cmdPkt.getCcsdsPkt());
			break;
		case 1:
			Logger.logUserActivity("ES Cmd: Noop", true);
			// cmdPkt = ES.getCmdList().get(EsApp.CMD_FC_NOOP);
			CmdWriter.sendCmd(cmdPkt.getCcsdsPkt());
			break;

		case 2:
			Logger.logUserActivity("ES Cmd: Set Sys Log Mode", true);
			// cmdPkt = ES.getCmdList().get(EsApp.CMD_FC_OVERWRITE_SYSLOG);
			cmdPkt.setParam(0, cmdParam);
			CmdWriter.sendCmd(cmdPkt.getCcsdsPkt());
			break;

		case 3:
			Logger.logUserActivity("ES Cmd: Write Sys Log to a File", true);
			// cmdPkt = ES.getCmdList().get(EsApp.CMD_FC_WRITE_SYSLOG);
			// Use default cmdPkt.setParam(0, cmdParam);
			CmdWriter.sendCmd(cmdPkt.getCcsdsPkt());
			break;

		case 4:
			Logger.logUserActivity("ES Cmd: Write Err Log to a File", true);
			// cmdPkt = ES.getCmdList().get(EsApp.CMD_FC_WRITE_ERLOG);
			// Use default cmdPkt.setParam(0, cmdParam);
			CmdWriter.sendCmd(cmdPkt.getCcsdsPkt());
			break;

		case 5:
			Logger.logUserActivity("ES Cmd: Restart Application", true);
			// cmdPkt = ES.getCmdList().get(EsApp.CMD_FC_RESTART_APP);
			// Use default cmdPkt.setParam(0, cmdParam);
			CmdWriter.sendCmd(cmdPkt.getCcsdsPkt());
			break;

		default:
			Logger.logUserActivity("Invalid command ID " + cmdID, true);
			System.out.println("Invalid command ID " + cmdID);
		}
	}

	public void sendPacketDisplayedInTestField() {
		// DisplayStreamId = ExApp.TLM_MID_PKT1; // 0x0F00
		DisplayStreamId = EsApp.TLM_MID_HK; // 0x0800
		Logger.logUserActivity("Display packet " + DisplayStreamId, true);
	}

	// @todo - This callback method didn't work because function does not
	// execute within the context of the display and an exception is generated
	// when window is updated.
	private class TlmListener implements PktObserver {
		public void update(int StreamId) {
			System.out.println("TlmListener called for packet " + StreamId);
			// CcsdsTlmPkt TlmPkt = TlmReader.getTlmPktQ().remove();
			// System.out.println("TlmListener 2");
			// logFswEventMessage("Received Telemetry Packet " +
			// TlmPkt.getStreamId());
			// logTlmPacket("PktObserver);
			// System.out.println("TlmListener 3");
		}

	}

	private void createTlmMonitorThread() {

		Timer timer = new Timer();
		TimerTask TlmTask = new TimerTask() {
			@Override
			public void run() {

				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {

						// String TlmStr;

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
									Logger.logTlmPacket(OutputStr, 40);
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

	} // End createTlmMonitorThread()

	public void displayNetworkInfo() {
		logNetworkStatus();
	}

	void logNetworkStatus() {
		String LogString;

		if (CmdWriter != null && TlmReader != null) {
			LogString = new String("Network Status: " + CmdWriter.getStatus()
					+ TlmReader.getStatus());
		} else
			LogString = new String("Network not initialized");

		Logger.logUserActivity(LogString, true);
	}

	

	public static void main(String[] args) {
		new JcatAppCopy();
	}
}
