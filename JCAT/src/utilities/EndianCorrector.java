package utilities;

import main.Launcher;

/**
 * corrects commands to correct Endian (big or little)
 * 
 * @author Joe
 * 
 */
public class EndianCorrector {
	/* Assumes JCAT is running on little endian*/
	private static boolean isBigEndianOut = true; // is ColdFire
	private static boolean isBigEndianIn = false; // is Coldfire

	public static final boolean isBigEndianOut()
	{
		return isBigEndianOut;
	}
	
	public static final boolean isBigEndianIn()
	{
		return isBigEndianIn;
	}
	
	public static final void setBigEndianOut(boolean isBigE) {
		final boolean oldEndian = isBigEndianOut;
		isBigEndianOut = isBigE;
		
		if (oldEndian != isBigEndianOut)
			if (isBigEndianOut)
				Launcher.addUserActivity("ENDIANCORRECTOR: OUTPUT SET TO BIG ENDIAN");
			else Launcher.addUserActivity("ENDIANCORRECTOR: OUTPUT SET TO LITTLE ENDIAN");
	}

	private static final void flipParameterBytesOut(byte[] init, int firstIndex,
			int length) {
		firstIndex = firstIndex - 1;
		byte[] temp = new byte[length];
		for (int i = firstIndex; i < firstIndex + length; i++) {
			temp[length - 1 + firstIndex - i] = init[i];
		}
		System.out.println("EXPECTED: 5, 4, 3, 2");
		for (int i = 0; i < temp.length; i++) {
			System.out.println(temp[i]);
		}

		for (int i = firstIndex; i < firstIndex + length; i++) {
			init[i] = temp[i - firstIndex];
		}
	}

	public static final void fixParameterOut(byte[] buf) {
		if (isBigEndianOut) {
			byte[] tempBytes = new byte[buf.length];

			for (int i = 0; i < buf.length; i++) 
				tempBytes[i] = buf[buf.length - 1 - i];
			
			for (int i = 0; i < buf.length; i++) 
				buf[i] = tempBytes[i];
		}
	}

	private final static void flipHeaderBytesOut(byte[] init) {
		for (int i = 0; i < 8; i = i + 2) {
			byte temp = init[i];
			init[i] = init[i + 1];
			init[i + 1] = temp;
		}
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

	/**
	 * Iff this class is set to be BigEndian, starting at the first byte, and
	 * lasting for length more bytes, this flips them about its middle axis
	 */
	public static final void fixParameterOut(byte[] buf, int firstIndex, int length) {
		if (isBigEndianOut)
			flipParameterBytesOut(buf, firstIndex, length);
	}

	/**
	 * Iff this class is set to be BigEndian, this flips the header to be used
	 * for Big-Endian commands
	 */
	public static final void fixHeaderOut(byte[] buf) {
		if (isBigEndianOut)
			flipHeaderBytesOut(buf);
	}

	public static void main(String[] args) {
		byte[] first = new byte[] { 18, 17, 10, 00, 00, 10, 06, 00, 31, 39 };
		System.out.println("EXPECTED: 17, 18, 00, 10, 10, 00, 06, 00, 31, 39");
		flipHeaderBytesOut(first);
		System.out.println("RESULT : ");
		for (int i = 0; i < first.length; i++) {
			System.out.print(first[i] + ", ");
		}
	}
	
	public static void setBigEndianIn(boolean bigEndian) {
		isBigEndianIn = bigEndian;
	}
}
