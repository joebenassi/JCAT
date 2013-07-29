package packets.ccsds;


public class CcsdsTlmPkt extends CcsdsPkt 
{
	public static final int CCSDS_IDX_TLM_HDR   =  6;
	public  static final int CCSDS_IDX_TLM_DATA  = 12;
	public static final int CCSDS_TLM_HDR_LEN   = 12;

   /*
   ** Construct a packet from raw data stream
   */
   public CcsdsTlmPkt(byte[] TlmMsg)
   {
      super(TlmMsg);
   }
} // End class CcsdsTlmPkt
