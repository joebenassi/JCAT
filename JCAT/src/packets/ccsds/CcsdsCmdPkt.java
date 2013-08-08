package packets.ccsds;

import packets.parameters.DataType;

/**
 * FULLY DOCUMENTED. This class is a CcsdsPkt formatted to be a command packet.
 * 
 * @author David McComas
 */
public class CcsdsCmdPkt extends CcsdsPkt {
	private static final int CCSDS_IDX_CMD_HDR = 6;
	private static final int CCSDS_IDX_CMD_DATA = 8;
	private static final int CCSDS_CMD_HDR_LEN = 8;

	/**
	 * Creates a CcsdsCmdPkt.
	 * 
	 * @param StreamId The cmdmid of its command parent.
	 * @param parameterLength The byte length of all the parameters combined.
	 * @param FuncCode The command code.
	 */
	public CcsdsCmdPkt(int StreamId, int parameterLength, int FuncCode) {
		super(StreamId, CCSDS_CMD_HDR_LEN + parameterLength);
		InitSecHdr(FuncCode);
	}

	/**
	 * Sets the value of the command code in the command packet.
	 * 
	 * @param FuncCode The command code to put in the packet.
	 */
	private void InitSecHdr(int FuncCode) {
		Packet[CCSDS_IDX_CMD_HDR] = 0;
		Packet[CCSDS_IDX_CMD_HDR + 1] = 0;

		Packet[CCSDS_IDX_CMD_HDR + 1] = new Integer(FuncCode & 0xFF)
				.byteValue();
	}

	/**
	 * Computes the checksum, and adds it to the packet.
	 * 
	 * @return The checksum.
	 */
	public byte ComputeChecksum() {
		int PktLen = getLength();
		byte Checksum = new Integer(0xFF).byteValue();

		for (int i = 0; i < PktLen; i++) {
			try {
				Checksum ^= Packet[i];
			} catch (Throwable ex) {}
		}
		Packet[CCSDS_IDX_CMD_HDR] = new Integer((Checksum & 0xFF)).byteValue();

		return Checksum;
	}

	/**
	 * Loads the data portion of the command data. Computes the checksum.
	 * 
	 * @param Data The parameter data.
	 */
	public void LoadData(byte Data[]) {
		for (int i = 0; i < Data.length; i++)
			Packet[CCSDS_IDX_CMD_DATA + i] = Data[i];
		
		ComputeChecksum();
	}
}
