package utilities;

import main.Launcher;

/**
 * corrects commands to correct Endian (big or little)
 * 
 * @author Joe
 * 
 */
public class EndianCorrector {
	/* Assumes JCAT is running on little endian */
	private static boolean isBigEndianOut = true; // is ColdFire
	private static boolean isBigEndianIn = false; // is Coldfire

	public static final boolean isBigEndianOut() {
		return isBigEndianOut;
	}

	public static final boolean isBigEndianIn() {
		return isBigEndianIn;
	}

	public static final void setBigEndianIn(boolean bigEndian) {
		isBigEndianIn = bigEndian;
	}

	public static final void setBigEndianOut(boolean isBigE) {
		final boolean oldEndian = isBigEndianOut;
		isBigEndianOut = isBigE;

		if (oldEndian != isBigEndianOut)
			if (isBigEndianOut)
				Launcher.addUserActivity("Set output endian: big endian");
			else
				Launcher.addUserActivity("Set output endian: little endian");
	}

	private final static void flipParameterBytes(byte[] buf) {
		byte[] tempBytes = new byte[buf.length];

		for (int i = 0; i < buf.length; i++)
			tempBytes[i] = buf[buf.length - 1 - i];

		for (int i = 0; i < buf.length; i++)
			buf[i] = tempBytes[i];
	}

	public static final void fixParameterOut(byte[] buf) {
		if (isBigEndianOut)
			flipParameterBytes(buf);
	}

	public static final void fixParameterIn(byte[] buf) {
		fixParameterOut(buf);

		/* TODO fix this */
	}

	public static final byte getValueOut(byte[] buf, int index) {
		if (isBigEndianOut) {
			if (index % 2 == 0)
				index++;
			else
				index--;
		}
		return buf[index];
	}

	private final static void flipHeaderBytes(byte[] init) {
		for (int i = 0; i < 8; i = i + 2) {
			byte temp = init[i];
			init[i] = init[i + 1];
			init[i + 1] = temp;
		}
	}

	/**
	 * Iff this class is set to be BigEndian, this flips the header to be used
	 * for Big-Endian commands
	 */
	public static final void fixHeaderOut(byte[] buf) {
		if (isBigEndianOut)
			flipHeaderBytes(buf);
	}

	public static final void fixHeaderIn(byte[] buf) {
		fixHeaderOut(buf);
		/* TODO fix this */
	}

}
