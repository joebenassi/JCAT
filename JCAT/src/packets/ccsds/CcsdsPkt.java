package packets.ccsds;

import packets.parameters.DataType;
import utilities.EndianCorrector;

/**
 * FULLY DOCUMENTED. This class contains a byte array that is the packet. This
 * class is used to perform operations on data that is in all Ccsds packets,
 * such as determining the stream ID, sequence count, and length. Each CmdPkt
 * and TlmPkt has an instance of CcsdsCmdPkt or CcsdsTlmPkt respectively.
 * 
 * @author David McComas
 */
public class CcsdsPkt {
	private static final int CCSDS_IDX_STREAM_ID = 0;
	private static final int CCSDS_IDX_SEQ_COUNT = 2;
	private static final int CCSDS_IDX_LENGTH = 4;
	private static final int CCSDS_MSK_MSG_ID = 0x0000FFFF;
	private static final int CCSDS_MSK_SEQ_CNT = 0x00003FFF;
	private static final int CCSDS_LENGTH_ADJUST = 7;

	byte[] Packet;

	/**
	 * Creates a CcsdsPkt from a raw byte array. Used for telemetry packets.
	 * 
	 * @param MsgData
	 *            The data comprising the packet.
	 */
	CcsdsPkt(byte[] MsgData) {
		Packet = MsgData;
	}

	/**
	 * Creates a CcsdsPkt from a stream ID/Cmdmid and length. Used for command
	 * packets. Creates the byte array, and initializes everything to zero.
	 * Encodes in the byte array the stream ID, length of the command, and
	 * sequence count.
	 * 
	 * @param StreamId
	 *            The streamID of the command.
	 * @param Length
	 *            The entire length of the byte array.
	 */
	CcsdsPkt(int StreamId, int Length) {
		Packet = new byte[Length];
		InitPkt(StreamId, Length);
	}

	/**
	 * Used for command packets. Sets the byte array representing the packet
	 * equal to zero, and encodes in it the stream ID, length of the command,
	 * and sequence count.
	 * 
	 * @param StreamId
	 *            The stream ID of the command.
	 * @param Length
	 *            The entire length of the byte array.
	 */
	public final void InitPkt(int StreamId, int Length) {
		for (int i = 0; i < Length; i++)
			Packet[i] = 0;

		Packet[CCSDS_IDX_STREAM_ID] = new Integer(StreamId & 0xFF).byteValue();
		Packet[CCSDS_IDX_STREAM_ID + 1] = new Integer((StreamId & 0xFF00) >> 8)
				.byteValue();
		Packet[CCSDS_IDX_SEQ_COUNT + 1] = new Integer(0xC0).byteValue();
		Packet[CCSDS_IDX_LENGTH] = new Integer(
				(Length - CCSDS_LENGTH_ADJUST) & 0xFF).byteValue();
		Packet[CCSDS_IDX_LENGTH + 1] = new Integer(
				((Length - CCSDS_LENGTH_ADJUST) & 0xFF00) >> 8).byteValue();

	}

	/**
	 * Returns the stream ID embedded in this packet.
	 * 
	 * @return The cmdmid of this packet.
	 */
	public final int getStreamID() {
		return ((((Packet[CCSDS_IDX_STREAM_ID] & 0x00FF) | (Packet[CCSDS_IDX_STREAM_ID + 1] << 8)) & CCSDS_MSK_MSG_ID));
	}

	/**
	 * Returns the sequence count encoded in this packet.
	 * 
	 * @return The SC of this packet.
	 */
	public final int getSeqCount() {
		return ((((Packet[CCSDS_IDX_SEQ_COUNT] & 0x00FF) | (Packet[CCSDS_IDX_SEQ_COUNT + 1] << 8)) & CCSDS_MSK_SEQ_CNT));
	}

	/**
	 * Returns the length of this packet, as embedded in this packet.
	 * 
	 * @return The length of this packet.
	 */
	public final int getLength() {
		return (((Packet[CCSDS_IDX_LENGTH] & 0x00FF) | (Packet[CCSDS_IDX_LENGTH + 1] << 8)) + CCSDS_LENGTH_ADJUST);
	}

	/**
	 * Returns the byte array containing the values of this packet.
	 * 
	 * @return The packet.
	 */
	public final byte[] getPacket() {
		return Packet;
	}
}