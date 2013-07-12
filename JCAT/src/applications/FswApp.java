/*
 ** Purpose: 
 **
 ** @author dmccomas
 **
 ** @todo - DefineCmds() - Allow XML file source 
 ** @todo - For TLM I allowed GUI class to render TLM 
 */
package applications;

import java.util.ArrayList;

import packets.ccsds.*;
import packets.cmd.CmdPkt;


/*
 ** Base class for FSW application models
 **
 ** Notes:
 **  1. Name Convention: Start all command and telemetry related constants
 **     with "CMD_" and "TLM_". Use second field to ID category like
 **     function code (FC) or message ID (MID). For example "CMD_FC_" and
 **     "CMD_MID_"
 **
 ** TODO - Should error message methods be used? Allows app prefix etc. to be used.
 ** TODO - Should Tlm string helper functions be moved to TlmPkt class?
 */
public abstract class FswApp {

	public static final int CMD_MAX = 20;
	public static final int TLM_MAX = 10;

	public static final int NULL_CMD_INT = 99;
	public static final String NULL_APP_STR = "Null";
	public static final String NULL_CMD_STR = "Null Command";

	public static final String TLM_STR_TBD = "Telemetry message string method not implemented\n";
	public static final String TLM_STR_ERR = "Invalid telemetry message ID for this application";

	public ArrayList<CmdPkt> commands = new ArrayList<CmdPkt>(100);
	protected ArrayList<Integer> telemetryIntegers; // TODO - Couple with CCSDS stream
											// ID type
	protected String[] TlmStrArray;

	protected FswApp() {}

	public ArrayList<Integer> getTlmList() {

		return telemetryIntegers;

	} // getTlmList()

	/***************************************************************************
	 ** 
	 ** Helper methods that provide generic TLM-to-String parsing
	 ** 
	 ** TODO - Make length variable with better line formats
	 */

	static public String ParseRawData(byte[] RawData) {

		String Message = new String();

		Message = "\n---------------------------------\nPacket: App ID = 0x";
		Message += ByteToHexStr(RawData[1]) + ByteToHexStr(RawData[0]) + "\n";
		// return ( (( (Packet[CCSDS_IDX_STREAM_ID] & 0x00FF) |
		// (Packet[CCSDS_IDX_STREAM_ID+1] << 8)) & CCSDS_MSK_MSG_ID) );

		// Truncate to 16 bytes since it's just a warm fuzzy
		int DataLen = 16;
		if (RawData.length < 16)
			DataLen = RawData.length;

		for (int i = 0; i < DataLen; i++) {
			Message += ByteToHexStr(RawData[i]) + " ";
		}

		return Message;

	} // End ParseRawData()

	/*
	 * * There's probably an easier way but I was having problems with sign
	 * extension* and not knowing Java well I just hacked a solution.
	 */

	static public String ByteToHexStr(byte Byte) {

		String HexStr = Integer.toHexString(Byte & 0x00FF); // Need to
																	// mask to
																	// prevent
																	// long
																	// strings

		if (HexStr.length() == 1)
			HexStr = "0" + HexStr;

		else if (HexStr.length() > 2)
			HexStr = "**";

		return HexStr.toUpperCase();

	} // End ByteToHexStr()

	/***************************************************************************
	 ** 
	 ** TlmStrArray helper functions
	 ** 
	 ** 1. Use ArrayList for speed because it does not allocate a new array on
	 * the heap. 2. Java doesn't have unsigned integers. Use 'long' that should
	 * be 64 bits and avoid sign bit
	 */

	public void loadTlmStrArrayHdr(CcsdsTlmPkt TlmMsg) {

		TlmStrArray[0] = String.valueOf((TlmMsg.getStreamId()));
		TlmStrArray[1] = String.valueOf((TlmMsg.getSeqCount()));
		TlmStrArray[2] = String.valueOf((TlmMsg.getLength()));
		TlmStrArray[3] = "Seconds"; // @todo - Seconds
		TlmStrArray[4] = "SubSeconds"; // @todo - SubSeconds

	} // End loadTlmStrArrayHdr()

	public void loadTlmStrArrayUint8(int TlmStrIndex, byte[] RawData,
			int RawIndex) {

		TlmStrArray[TlmStrIndex] = String.valueOf((0x00FF & RawData[RawIndex])); // I
																					// think
																					// this
																					// solves
																					// unsigned

	} // End loadTlmStrArrayUint8()

	public void loadTlmStrArrayUint16(int TlmStrIndex, byte[] RawData,
			int RawIndex) {

		long longInt = 0x0000FFFF & (RawData[RawIndex] | ((RawData[RawIndex + 1] << 8)));
		TlmStrArray[TlmStrIndex] = String.valueOf(longInt);

	} // End loadTlmStrArrayUint16()

	public void loadTlmStrArrayUint32(int TlmStrIndex, byte[] RawData,
			int RawIndex) {

		long longIntA = (RawData[RawIndex] | ((RawData[RawIndex + 1] << 8)));
		long longIntB = (RawData[RawIndex + 2] | ((RawData[RawIndex + 3] << 8)));
		long longInt = (longIntA | longIntB << 16);

		TlmStrArray[TlmStrIndex] = String.valueOf(longInt);

	} // End loadTlmStrArrayUint32()

} // End class FswApp

/*
 * A couple of key App IDs: 0x0841: Event 0x0815: CI CLCW (don't print) 0x080B:
 * Fast HK (Don't print)
 * 
 * *
 */
