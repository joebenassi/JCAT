package network;

import java.util.ArrayList;

import main.Launcher;

import org.eclipse.swt.widgets.Display;

import applications.App;

import packets.ccsds.CcsdsPkt;
import packets.ccsds.CcsdsTlmPkt;
import packets.cmd.CmdPkt;
import utilities.TimeKeeper;

/**
 * NOT DOCUMENTED.
 * 
 * @author Joe Benassi
 * @author David McComas
 */
public class Networker {
	private static Networker networker;
	private static ArrayList<App> apps;
	private static ArrayList<Config> configs;
	private static final String observerID = "arbitrary";
	private static PktObserver observer = new PktObserver(observerID);
	private static PktWriter PktOutput;

	public static void addApp(App app) {
		for (int i = 0; i < apps.size(); i++)
			if (apps.get(i).getTlmAppID() == app.getTlmAppID())
				return;

		apps.add(app);
	}

	public static void addConfig(String name, String msgid) {
		configs.add(new Config(name, msgid));
	}

	public static final String getAppName(byte[] data) {
		for (App app : apps)
			if (app.getCmdId() == CcsdsPkt.getID(data))
				return app.getMenuName();
		return "";
	}

	public static void startNetworker() {
		apps = new ArrayList<App>();
		configs = new ArrayList<Config>();
		networker = new Networker();
		FswTlmNetwork.removeObserver(observer.getID());
		FswTlmNetwork.addObserver(observer);
	}

	/** functional **/
	public static Networker getNetworker() {
		return networker;
	}

	public void launch() {
		createTlmMonitorThread();
	}

	/** functional **/
	public final static void enableToLabTelemetry(boolean wireless) {
		/* TODO find a more elegant solution */
		for (App app : apps) {
			if (app.getName().substring(0, 2).equalsIgnoreCase("to")) {
				String ip = PktReader.getLocalIP();
				if (wireless)
					ip = PktReader.getIP();

				app.executeCommand(6, new String[] { ip });
				Launcher.addUserActivity("Enabled Telemetry for IP: " + ip);
			}
		}
	}

	public static void enableEventMessages() {
		/* TODO find a more elegant solution */
		for (App app : apps) {
			if (app.getName().substring(0, 2).equalsIgnoreCase("to"))
				app.executeCommand(7, new String[] { "" });
		}
	}

	/** functional **/
	public final static void sendPkt(CmdPkt cmdPkt) {
		PktOutput.WriteCmdPkt(cmdPkt.getName(), cmdPkt.getCcsdsPkt()
				.GetPacket(), cmdPkt.getCcsdsPkt().getTotalLength());
	}

	private final static void printEvent(String config, CcsdsTlmPkt pkt) {
		byte[] TlmPkt = pkt.getPacket();
		String time = "";
		try {
			time = TimeKeeper.getEventTime(pkt.getPacket());
		} catch (Exception e) {
		}

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
		System.out.println("NETWORKER: MONITORING TLM");
		final Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (Launcher.getInstanceNum() == myInstance) {
					try {
						Thread.sleep(40);
					} catch (Throwable e) {
					}

					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							while (!observer.isEmpty()) {
								CcsdsTlmPkt TlmPkt = observer.getTlmPkt();
								for (Config c : configs) {
									if (c.getMsgId() == TlmPkt.getStreamId())
										printEvent(c.getName(), TlmPkt);
								}

								for (App app : apps)
									if (TlmPkt.getStreamId() == app
											.getTlmAppID()) {
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