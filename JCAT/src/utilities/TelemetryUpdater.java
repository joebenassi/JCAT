package utilities;

import java.util.ArrayList;

import packets.ccsds.CcsdsTlmPkt;
import packets.tlm.TlmPkt;

import applications.App;

public class TelemetryUpdater {
	//private static final int sleepDuration = 10;
	//private static ArrayList<App> apps = new ArrayList<App>();
	//public static final boolean updateAtIntervals = false;
	
	public static void updateTelemetry(CcsdsTlmPkt TlmMsg, App app) {
		String[] TlmStrArray = new String[app.getTelemetryAmt()];
		loadTlmStrArrayHdr(TlmMsg, TlmStrArray);

		final int ID = TlmMsg.getStreamId();
		if (ID == app.getTlmAppID())
		{
			loadTlmStrArrayHk(TlmMsg.getPacket(), app.getTelemetry());
			updatePopup(app);
		}
		/*
		 * else if (ID == TLM_MID_APP) {
		 * 
		 * }
		 */
		else
			System.out.println("ERROR IN applications.TlmStrArrayLoader.class");
	}
	
	private static final void loadTlmStrArrayHk(byte[] RawData, TlmPkt[] Telemetry) {
		int RawIndex = 12;
		for (int i = 0; i < Telemetry.length; i++) {
			TlmPkt T = Telemetry[i];

			T.setValue(T.getDataType().getTlmStrArray(RawData, RawIndex));
			RawIndex += T.getDataType().getBytes();
		}
	}
	
	private static final void updatePopup(App app)
	{
		for (int i = 0; i < app.getTelemetryAmt(); i++) {
			app.getTelemetryText(i).setText(
					app.getTelemetryValue(i));
			}
	}
	
	public static void loadTlmStrArrayHdr(CcsdsTlmPkt TlmMsg,
			String[] TlmStrArray) {

		TlmStrArray[0] = String.valueOf((TlmMsg.getStreamId()));
		TlmStrArray[1] = String.valueOf((TlmMsg.getSeqCount()));
		TlmStrArray[2] = String.valueOf((TlmMsg.getLength()));
		TlmStrArray[3] = "Seconds"; // @todo - Seconds
		TlmStrArray[4] = "SubSeconds"; // @todo - SubSeconds

	} // End loadTlmStrArrayHdr()
	/*
	 * getTlmStrArray(CcsdsCmdPkt TlmMsg) -> case TlmMsg.getStreamId() ==
	 * TLM_MID_HK ->loadTlmStrArrayHk(TlmMsg.getPacket()); =>(packet is byte[].
	 * changed when parameters are loaded after user input)
	 */
	// first priority: getting functional cmd w/ parameter.
	
	/*
	public static void addApp(App app) {
		apps.add(app);
	}

	private static void updateTelemetryText(final App app) {
		new Thread(new Runnable() {
			public void run() {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						for (int i = 0; i < app.getTelemetryAmt(); i++) {
							app.getTelemetryText(i).setText(
									app.getTelemetryValue(i));
						}
					}
				});
			}
		}).start();
	}

	public static void start() {
		final Thread t = new Thread(new Runnable() {
		public void run() {
			while (true) {
				try {Thread.sleep(sleepDuration);}
				catch (Throwable e){};
				
				for (App app : apps) {
					updateTelemetryText(app);
				}}
			}
		});
		t.setDaemon(true);
		t.start();
	}*/
}
