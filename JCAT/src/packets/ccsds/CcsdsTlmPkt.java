package packets.ccsds;

import packets.parameters.DataType;

/**
 * NOT DOCUMENTED.
 * 
 * @author Joe Benassi
 * @author David McComas
 */
public class CcsdsTlmPkt extends CcsdsPkt {
	public static final int CCSDS_IDX_TLM_HDR = 6;
	public static final int CCSDS_IDX_TLM_DATA = 12;
	public static final int CCSDS_TLM_HDR_LEN = 12;

	/*
	 * * Construct a packet from raw data stream
	 */
	public CcsdsTlmPkt(byte[] TlmMsg) {
		super(TlmMsg);
	}

	/* WORKS! */
	public static int getTime(byte[] data) {
		int sec = 0;

		int a = data[CCSDS_IDX_TLM_HDR];
		int b = data[CCSDS_IDX_TLM_HDR + 1];
		int c = data[CCSDS_IDX_TLM_HDR + 2];
		int d = data[CCSDS_IDX_TLM_HDR + 3];

		if (a < 0)
			a += 256;

		sec += d * 256 * 256 * 256;
		sec += c * 256 * 256;
		sec += b * 256;
		sec += a;

		return sec;
	}
}
