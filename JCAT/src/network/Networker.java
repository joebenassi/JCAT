package network;

import java.util.ArrayList;

import main.Launcher;

import org.eclipse.swt.widgets.Display;

import applications.App;

import packets.ccsds.CcsdsPkt;
import packets.ccsds.CcsdsTlmPkt;
import packets.cmd.Cmd;
import utilities.TelemetryUpdater;
import utilities.TimeKeeper;

/**
 * FULLY DOCUMENTED. UNSTABLE. This class is primarily responsible for
 * interpreting all received telemetry/event message packets and handling each
 * accordingly. This class has methods that are at high-risk of failure in
 * past/future cFE Apps.
 * 
 * @author Joe Benassi
 * @author David McComas
 */
public class Networker {
	private static ArrayList<App> apps;
	private static ArrayList<Config> configs;
	private static final String observerID = "arbitrary";
	private static FswTlmObserver observer = new FswTlmObserver(observerID);

	/**
	 * If this App has a telemetry msgid unique from those of all other Apps in
	 * "apps", it is added to "apps".
	 * 
	 * @param app
	 *            The App to attempt to add.
	 */
	public static void addApp(App app) {
		for (int i = 0; i < apps.size(); i++)
			if (apps.get(i).getTlmAppID() == app.getTlmAppID())
				return;

		apps.add(app);
	}

	/**
	 * Adds a Config to "configs" based on the input name and msgid. When this
	 * msgid is parsed from a telemetry packet, the telemetry packet will be
	 * parsed as an event message and the name of this config will display in
	 * the Event Window.
	 * 
	 * @param name
	 *            The name to display in the Event Window.
	 * @param msgid
	 *            The msgid, in String form, of this config.
	 */
	public static void addConfig(String name, String msgid) {
		configs.add(new Config(name, msgid));
	}

	/**
	 * Returns the menuname of the App whose commandID matches that of the input
	 * cmdPkt. If no App has this, this returns "".
	 * 
	 * @param data
	 *            The data within a App's command.
	 * @return The String representing the menuname of the App.
	 */
	public static final String getAppName(Cmd cmdPkt) {
		for (App app : apps)
			if (app.getCmdId() == cmdPkt.getCcsdsPkt().getStreamID())
				return app.getMenuName();
		return "";
	}

	/**
	 * Completes all the necessary steps for Networker to restart after File >
	 * Restart.
	 */
	public static void restart() {
		apps = new ArrayList<App>();
		configs = new ArrayList<Config>();
		FswTlmNetwork.removeObserver(observer.getID());
		FswTlmNetwork.addObserver(observer);
	}

	/**
	 * UNSTABLE. Attempts to enable telemetry. For all apps whose names start
	 * with "TO", their 6th command is sent with a String parameter equaling the
	 * either local or wireless ip address of this machine. Note: You cannot
	 * enable both local and wireless telemetry at the same time, because the
	 * CFS can only send messages to one IP address.
	 * 
	 * @param wireless
	 *            If the wireless IP for this machine should receive telemetry
	 *            packets.
	 * 
	 *            TODO find a better, less dirty method to accomplish this.
	 */
	public final static void enableTelemetry(boolean wireless) {
		for (App app : apps) {
			if (app.getName().substring(0, 2).equalsIgnoreCase("to")) {
				String ip = PktReader.getLocalIP();
				if (wireless)
					ip = PktReader.getWirelessIP();

				if (ip != null) {
					app.executeCommand(6, new String[] { ip });
					Launcher.addUserActivity("Enabled Telemetry for IP: " + ip);
				}
			}
		}
	}

	/**
	 * UNSTABLE. Attempts to enable event messages. For all apps whose names
	 * start with "TO", their 7th command is sent with the String parameter "".
	 * 
	 * TODO find a better, less dirty method to accomplish this.
	 */
	public static void enableEventMessages() {
		for (App app : apps) {
			if (app.getName().substring(0, 2).equalsIgnoreCase("to"))
				app.executeCommand(7, new String[] { "" });
		}
	}

	/**
	 * UNSTABLE. Prints an event message in the main page based on the input
	 * config and CcsdsTlmPkt. This algorithm is hardcoded and likely to fail in
	 * other CFS/cFE releases.
	 * 
	 * @param config
	 *            The config to display in the Event Window.
	 * @param pkt
	 *            The CcsdsTlmPkt to parse for event information.
	 * 
	 *            TODO FIXME Hardcoded parsing!
	 */
	private final static void printEvent(String config, CcsdsTlmPkt pkt) {
		byte[] TlmPkt = pkt.getPacket();
		int time = pkt.getTime();

		String MsgA = new String(TlmPkt, 12, 122);
		String MsgB = new String(TlmPkt, 44, 122);

		String MsgStr = MsgA.substring(0, MsgA.indexOf('\0')) + ": "
				+ MsgB.substring(0, MsgB.indexOf('\0')) + "\n";
		Launcher.addEvent(time, config, MsgStr);
	}

	/**
	 * Monitors telemetry and event messages every 40ms. Determines if packets
	 * are telemetry or event messages, and handles them accordingly. Always
	 * receives all packets received by PktReader.
	 */
	public static final void monitorTelemetry() {
		final int myInstance = Launcher.getInstanceNum();
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
									if (c.getMsgId() == TlmPkt.getStreamID())
										printEvent(c.getName(), TlmPkt);
								}

								for (App app : apps)
									if (TlmPkt.getStreamID() == app
											.getTlmAppID()) {
										TelemetryUpdater.updateTelemetry(
												TlmPkt, app);
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