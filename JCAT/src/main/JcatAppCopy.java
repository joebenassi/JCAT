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

public class JcatAppCopy {
	private final ToApp TO;
	private final FswCmdNetwork CmdWriter;
	private final TlmPktDatabase TlmDatabase;
	private final FswTlmNetwork TlmReader;
	private final TlmListener TlmDisplay;
	private final int DisplayStreamId;

	public JcatAppCopy() {
		TO = new ToApp(ToApp.PREFIX_STR, "Telemetry Output");
		CmdWriter = new FswCmdNetwork();
		TlmDatabase = new TlmPktDatabase();
		TlmReader = new FswTlmNetwork(TlmDatabase);
		TlmDisplay = new TlmListener();
		DisplayStreamId = 0;

		TlmDatabase.registerObserver(TlmDisplay);
		
		createTlmMonitorThread();
		enableToLabTelemetry();
		configureToLabPacketFilter();

		CmdPkt Cmd = TO.getNoop();
		CmdWriter.sendCmd(Cmd.getCcsdsPkt());
	}

	private final void enableToLabTelemetry() {
		CmdPkt Cmd = TO.getEnaTlmCmd();
		CmdWriter.sendCmd(Cmd.getCcsdsPkt());
	}

	private final void configureToLabPacketFilter() {
		CmdPkt Cmd = TO.getAddPktCmdPkt();
		CmdWriter.sendCmd(Cmd.getCcsdsPkt());
	}

	private final static class TlmListener implements PktObserver {
		public final void update(int StreamId) {
			System.out.println("TlmListener called for packet " + StreamId);
		}
	}

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

	public static void main(String[] args) {
		new JcatAppCopy();
	}
}
