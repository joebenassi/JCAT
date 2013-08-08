package packets.ccsds;

import packets.parameters.DataType;

/**
 * FULLY DOCUMENTED. An instance of this class is created when a packet is
 * received by PktReader.
 * 
 * @author Joe Benassi
 * @author David McComas
 */
public class CcsdsTlmPkt extends CcsdsPkt {
	private static final int CCSDS_IDX_TLM_HDR = 6;
	private static final int CCSDS_IDX_TLM_DATA = 12;
	private static final int CCSDS_TLM_HDR_LEN = 12;

	/**
	 * Constructs a CcsdsPkt from a raw data stream. Formatted to have
	 * attributes of a telemetry packet.
	 * 
	 * @param TlmMsg
	 *            The data comprising the packet.
	 */
	public CcsdsTlmPkt(byte[] TlmMsg) {
		super(TlmMsg);
	}

	/**
	 * Parses the packet and returns the time of the packet.
	 * 
	 * @return The time that the CFS encoded the packet with, in seconds.
	 */
	public final int getTime() {
		int sec = 0;

		int a = Packet[CCSDS_IDX_TLM_HDR];
		int b = Packet[CCSDS_IDX_TLM_HDR + 1];
		int c = Packet[CCSDS_IDX_TLM_HDR + 2];
		int d = Packet[CCSDS_IDX_TLM_HDR + 3];

		if (a < 0)
			a += 256;

		sec += d * 256 * 256 * 256;
		sec += c * 256 * 256;
		sec += b * 256;
		sec += a;

		return sec;
	}
}
