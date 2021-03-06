package utilities;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import packets.ccsds.CcsdsTlmPkt;
import packets.tlm.Tlm;

import applications.App;

/**
 * NOT DOCUMENTED.
 * 
 * @author Joe Benassi
 * @author David McComas
 */
public final class TelemetryUpdater {
	public static void updateTelemetry(CcsdsTlmPkt TlmMsg, App app) {
		String[] TlmStrArray = new String[app.getTelemetryAmt()];
		loadTlmStrArrayHdr(TlmMsg, TlmStrArray);

		final int ID = TlmMsg.getStreamID();
		if (ID == app.getTlmAppID()) {
			loadTlmStrArrayHk(TlmMsg.getPacket(), app.getTelemetry());
			try {
				app.setTime(TimeKeeper.getTimeFormatted(TlmMsg.getTime()));
				app.setSC(TlmMsg.getSeqCount() + "");// "" +
														// CcsdsTlmPkt.getSeqCount(TlmMsg.getPacket()));
			} catch (Exception e) {
			}
			updatePopup(app);
		}
		/* TODO account for different AppIDs (Non-HK) */
	}

	private static final void loadTlmStrArrayHk(byte[] RawData, Tlm[] Telemetry) {
		int RawIndex = 12;
		for (int i = 0; i < Telemetry.length; i++) {
			Tlm T = Telemetry[i];

			T.setValue(T.getDataType().getTlmStrArray(RawData, RawIndex));
			RawIndex += T.getDataType().getBytes();
		}
	}

	private static final void updatePopup(final App app) {
		for (int i = 0; i < app.getTelemetryAmt(); i++) {
			final Text text = app.getTelemetryText(i);
			final String value = app.getTelemetryValue(i);
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					text.setText(value);
				}
			});
		}
	}

	public static void loadTlmStrArrayHdr(CcsdsTlmPkt TlmMsg,
			String[] TlmStrArray) {
		TlmStrArray[0] = String.valueOf((TlmMsg.getStreamID()));
		TlmStrArray[1] = String.valueOf((TlmMsg.getSeqCount()));
		TlmStrArray[2] = String.valueOf((TlmMsg.getLength()));
		TlmStrArray[3] = "Seconds"; // @todo - Seconds
		TlmStrArray[4] = "SubSeconds"; // @todo - SubSeconds
	}
}
