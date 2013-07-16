package network;

import java.util.ArrayList;

import main.Launcher;

import org.eclipse.swt.widgets.Display;

import applications.App;
import applications.ToApp;

import packets.ccsds.CcsdsTlmPkt;
import packets.cmd.CmdPkt;

import curval.PktObserver;
import curval.TlmPktDatabase;

public class Networker {
	private FswCmdNetwork CmdWriter;
	private TlmPktDatabase TlmDatabase;
	private FswTlmNetwork TlmReader;
	private TlmListener TlmDisplay;
	private static Networker networker = new Networker();
	private static ArrayList<App> apps = new ArrayList<App>();

	public static void addApp(App app) {
		for (int i = 0; i < apps.size(); i++)
			if (apps.get(i).getTlmAppID() == app.getTlmAppID())
				return;

		/* only ads app if it doesn't share TlmAppID with any other app */
		apps.add(app);
	}

	/** functional **/
	public static Networker getNetworker() // Allows every App/CmdPkt to call
											// the same instance of Networker
	{
		return networker;
	}

	/** functional **/
	public Networker() {
	}

	public void launch() {
		System.out.println("APPS LOADED: " + apps.size());
		CmdWriter = new FswCmdNetwork();
		TlmDatabase = new TlmPktDatabase();
		TlmReader = new FswTlmNetwork(TlmDatabase);
		TlmDisplay = new TlmListener();

		TlmDatabase.registerObserver(TlmDisplay);
		createTlmMonitorThread();
		enableToLabTelemetry();
		configTOLab();
	}

	/** functional **/
	private final void enableToLabTelemetry() {
		CmdPkt Cmd = ToApp.getEnaTlmCmd();
		CmdWriter.sendCmd(Cmd.getName(), Cmd.getCcsdsPkt());
	}

	private final void configTOLab() {
		int[] addresses = new int[apps.size()];

		for (int i = 0; i < addresses.length; i++)
			addresses[i] = apps.get(i).getTlmAppID();

		CmdPkt[] cmdPkts = ToApp.getConfigTlm(addresses);
		for (CmdPkt c : cmdPkts)
			sendPkt(c);
	}

	/** functional **/
	public final void sendPkt(CmdPkt cmdPkt) {
		CmdWriter.sendCmd(cmdPkt.getName(), cmdPkt.getCcsdsPkt());
		// Launcher.addUserActivity("Command Sent: " + cmdPkt.getName());
	}

	/** functional **/
	private final static class TlmListener implements PktObserver {
		public final void update(int StreamId) {
			// System.out.println("TlmListener called for packet " + StreamId);
		}
	}

	private final static void printEvent(CcsdsTlmPkt pkt) {
		byte[] TlmPkt = pkt.getPacket();
		String MsgA = new String(TlmPkt, 12, 122); // OS_MAX_API_NAME = 20,
													// CFE_EVS_MAX_MESSAGE_LENGTH
													// = 122
		String MsgB = new String(TlmPkt, 44, 122); // OS_MAX_API_NAME = 20,
													// CFE_EVS_MAX_MESSAGE_LENGTH
													// = 122
		String MsgStr = MsgA.substring(0, MsgA.indexOf('\0')) + ": "
				+ MsgB.substring(0, MsgB.indexOf('\0')) + "\n";
		Launcher.addEvent(MsgStr);
	}

	/** assumed functional **/
	private final void createTlmMonitorThread() {
		final Thread t = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(10);
					} catch (Throwable e) {
					}
					;
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							synchronized (TlmReader) {
								while (!TlmReader.getTlmPktQ().isEmpty()) {
									CcsdsTlmPkt TlmPkt = TlmReader.getTlmPktQ()
											.remove();

									if (TlmPkt.getStreamId() == 0x0808)
										printEvent(TlmPkt);
									else
										for (App app : apps)
											if (TlmPkt.getStreamId() == app
													.getTlmAppID())
												app.ingest(TlmPkt);
								}
							}
						}
					});
				}
			}
		});
		t.setDaemon(true);
		t.start();
	}
}