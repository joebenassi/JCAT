package ccsds;


public class CcsdsTlmPkt extends CcsdsPkt 
{
   static final public int CCSDS_IDX_TLM_HDR   =  6;
   static final public int CCSDS_IDX_TLM_DATA  = 12;
   static final public int CCSDS_TLM_HDR_LEN   = 12;

   /*
   ** Construct a packet from raw data stream
   */
   public CcsdsTlmPkt(byte[] TlmMsg)
   {
      
      super(TlmMsg);
      
   } // End CcsdsTlmPkt()

} // End class CcsdsTlmPkt
