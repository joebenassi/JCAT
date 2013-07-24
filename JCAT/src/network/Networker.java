package network;

import java.util.ArrayList;

import main.Launcher;

import org.eclipse.swt.widgets.Display;

import applications.App;

import packets.ccsds.CcsdsTlmPkt;
import packets.cmd.CmdPkt;

import curval.PktObserver;
import curval.TlmPktDatabase;

public class Networker {
	private static FswCmdNetwork CmdWriter;
	private TlmPktDatabase TlmDatabase;
	private static FswTlmNetwork TlmReader;
	private TlmListener TlmDisplay;
	private static Networker networker;
	private static ArrayList<App> apps;
	private static ArrayList<Config> configs;

	public static void addApp(App app) {
		for (int i = 0; i < apps.size(); i++)
			if (apps.get(i).getTlmAppID() == app.getTlmAppID())
				return;

		apps.add(app);
	}

	public static void addConfig(String name, String msgid) {
		configs.add(new Config(name, msgid));
	}

	public static void startNetworker() {
		apps = new ArrayList<App>();
		configs = new ArrayList<Config>();
		networker = new Networker();
	}

	/** functional **/
	public static Networker getNetworker() {
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
	}

	/** functional **/
	public final static void enableToLabTelemetry() {
		for (App app : apps) {
			if (app.getName().substring(0, 2).equalsIgnoreCase("to")) {
				app.executeCommand(6, new String[] { PktReader.getIP() });
				/*
				 * TODO find a more elegant solution. Don't hardcode based on
				 * current TO_LAB
				 */
			}
		}
	}

	/** functional **/
	public final static void sendPkt(CmdPkt cmdPkt) {
		CmdWriter.sendCmd(cmdPkt.getName(), cmdPkt.getCcsdsPkt());
		// Launcher.addUserActivity("Command Sent: " + cmdPkt.getName());
	}

	/** functional **/
	private final static class TlmListener implements PktObserver {
		public final void update(int StreamId) {
			// System.out.println("TlmListener called for packet " + StreamId);
		}
	}

	private final static void printEvent(String config, CcsdsTlmPkt pkt) {
		byte[] TlmPkt = pkt.getPacket();
		final String time = "SOMETIME2";
		String MsgA = new String(TlmPkt, 12, 122);
		/*
		 * TODO get magic numbers from OS_MAX_API_NAME = 20 or
		 * CFE_EVS_MAX_MESSAGE_LENGTH = 122?
		 */
		String MsgB = new String(TlmPkt, 44, 122);

		/*
		 * TODO get magic numbers from OS_MAX_API_NAME = 20 or
		 * CFE_EVS_MAX_MESSAGE_LENGTH = 122?
		 */
		String MsgStr = MsgA.substring(0, MsgA.indexOf('\0')) + ": "
				+ MsgB.substring(0, MsgB.indexOf('\0')) + "\n";
		Launcher.addEvent(time, config, MsgStr);
	}

	/** assumed functional **/
	private final static void createTlmMonitorThread() {
		final int myInstance = Launcher.getInstanceNum();

		final Thread t = new Thread(new Runnable() {
			public void run() {
				while (Launcher.getInstanceNum() == myInstance) {
					try {
						Thread.sleep(40);
					} catch (Throwable e) {
					}
					;
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							synchronized (TlmReader) {
								while (!TlmReader.getTlmPktQ().isEmpty()) {
									CcsdsTlmPkt TlmPkt = TlmReader.getTlmPktQ()
											.remove();

									for (Config c : configs) {
										if (c.getMsgId() == TlmPkt
												.getStreamId())
											printEvent(c.getName(), TlmPkt);
									}

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