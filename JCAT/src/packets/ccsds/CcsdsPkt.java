package packets.ccsds;

import packets.parameters.DataType;
import utilities.EndianCorrector;

/**
 * NOT DOCUMENTED.
 * 
 * @author David McComas
 * 
 *         TODO Implement packet type
 */
public class CcsdsPkt {
	private static final int CCSDS_IDX_STREAM_ID = 0;
	private static final int CCSDS_IDX_SEQ_COUNT = 2;
	private static final int CCSDS_IDX_LENGTH = 4;
	private static final int CCSDS_MSK_MSG_ID = 0x0000FFFF;
	private static final int CCSDS_MSK_SEQ_CNT = 0x00003FFF;
	private static final int CCSDS_LENGTH_ADJUST = 7;
	// private static final int CCSDS_PRI_HDR_LENGTH = 6;

	byte[] Packet;

	public CcsdsPkt(int Length) {
		Packet = new byte[Length];

	} // End CcsdsPkt()

	public CcsdsPkt(int StreamId, int Length) {
		Packet = new byte[Length];
		InitPkt(StreamId, Length);

	} // End CcsdsPkt()

	/*
	 * Length - Is the total packet length in bytes
	 */
	public void InitPkt(int StreamId, int Length) {

		for (int i = 0; i < Length; i++) {
			Packet[i] = 0;
		}

		Packet[CCSDS_IDX_STREAM_ID] = new Integer(StreamId & 0xFF).byteValue();
		Packet[CCSDS_IDX_STREAM_ID + 1] = new Integer((StreamId & 0xFF00) >> 8)
				.byteValue();
		Packet[CCSDS_IDX_SEQ_COUNT + 1] = new Integer(0xC0).byteValue();
		Packet[CCSDS_IDX_LENGTH] = new Integer(
				(Length - CCSDS_LENGTH_ADJUST) & 0xFF).byteValue();
		Packet[CCSDS_IDX_LENGTH + 1] = new Integer(
				((Length - CCSDS_LENGTH_ADJUST) & 0xFF00) >> 8).byteValue();

	} // End InitPkt()

	/*
	 * * Construct a packet from raw data stream
	 */
	public CcsdsPkt(byte[] MsgData) {

		Packet = MsgData;

	} // End CcsdsPkt()

	public int getTotalLength() {
		byte first = EndianCorrector.getValueOut(Packet, CCSDS_IDX_LENGTH);
		byte second = EndianCorrector.getValueOut(Packet, CCSDS_IDX_LENGTH + 1);
		byte[] init = new byte[] { first, second };
		EndianCorrector.fixParameterOut(init);

		return (init[0] | init[1] << 8) + CCSDS_LENGTH_ADJUST;
		// return (Packet[CCSDS_IDX_LENGTH] | (Packet[CCSDS_IDX_LENGTH + 1] <<
		// 8))
		// + CCSDS_LENGTH_ADJUST;

	}// End getTotalLength()

	public static int getID(byte[] data) {
		return ((((data[CCSDS_IDX_STREAM_ID] & 0x00FF) | (data[CCSDS_IDX_STREAM_ID + 1] << 8)) & CCSDS_MSK_MSG_ID));
	}

	public int getStreamId() {
		return getID(Packet);

	}// End getStreamId()

	public static int getSeqCount(byte[] data) {
		return ((((data[CCSDS_IDX_SEQ_COUNT] & 0x00FF) | (data[CCSDS_IDX_SEQ_COUNT + 1] << 8)) & CCSDS_MSK_SEQ_CNT));
	}

	public int getSeqCount() {
		return getSeqCount(Packet);
	}// End getSeqCount()

	public int getLength() {
		return (((Packet[CCSDS_IDX_LENGTH] & 0x00FF) | (Packet[CCSDS_IDX_LENGTH + 1] << 8)) + CCSDS_LENGTH_ADJUST);

	}// End getLength()

	public byte[] getPacket() {
		return Packet;
	}// End getPacket()

	public void setPacket(byte[] newPacket) {
		Packet = newPacket;
	}

} // End class CcsdsPkt
