package main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;

public class Logger {

	StyledText textTlmLog;
	StyledText textFswEventLog;
	static StyledText textUserActivityLog;

	void logTlmPacket(String message) {
		StyleRange styleRange1 = new StyleRange();
		styleRange1.start = textTlmLog.getCharCount();
		styleRange1.length = message.length();
		styleRange1.fontStyle = SWT.NORMAL;

		textTlmLog.append(message + "\r\n");
		textTlmLog.setStyleRange(styleRange1);
		textTlmLog.setSelection(textTlmLog.getCharCount());
	}

	static void logTlmPacket(String[] message, int len) {
		for (int i = 0; i < len; i++) {
			textTlmLog.append(message[i] + "\r\n");
		}
	}

	void logFswEventMessage(String message) {
		StyleRange styleRange1 = new StyleRange();
		styleRange1.start = textFswEventLog.getCharCount();
		styleRange1.length = message.length();
		styleRange1.fontStyle = SWT.NORMAL;

		textFswEventLog.append(message + "\r\n");
		textFswEventLog.setStyleRange(styleRange1);
		textFswEventLog.setSelection(textFswEventLog.getCharCount());
	}

	static void logUserActivity(String message, boolean showInStatusBar) {
		StyleRange styleRange1 = new StyleRange();
		styleRange1.start = textUserActivityLog.getCharCount();
		styleRange1.length = message.length();
		styleRange1.fontStyle = SWT.NORMAL;

		textUserActivityLog.append(message + "\r\n");
		textUserActivityLog.setStyleRange(styleRange1);
		textUserActivityLog.setSelection(textUserActivityLog.getCharCount());
	}

	void logError(String message) {

		StyleRange styleRange1 = new StyleRange();
		styleRange1.start = textUserActivityLog.getCharCount();
		styleRange1.length = message.length();
		styleRange1.fontStyle = SWT.NORMAL;

		textUserActivityLog.append(message + "\r\n");
		textUserActivityLog.setStyleRange(styleRange1);
		textUserActivityLog.setSelection(textUserActivityLog.getCharCount());
	}
}
