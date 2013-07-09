package ccsds;

/*
** 
** @author dmccomas
**
** @todo - Implement packet type
*/
public class CcsdsPkt 
{

   static final public int CCSDS_IDX_STREAM_ID  = 0;
   static final public int CCSDS_IDX_SEQ_COUNT  = 2;
   static final public int CCSDS_IDX_LENGTH     = 4;
   static final public int CCSDS_PRI_HDR_LENGTH = 6;
   
   static final public int CCSDS_MSK_MSG_ID  = 0x0000FFFF;
   static final public int CCSDS_MSK_SEQ_CNT = 0x00003FFF;

   static int CCSDS_LENGTH_ADJUST = 7;

   byte[] Packet;
   
   public CcsdsPkt(int Length)
   {
      Packet = new byte[Length];
      
   } // End CcsdsPkt()
   
   public CcsdsPkt(int StreamId, int Length)
   {
      Packet = new byte[Length];
      InitPkt(StreamId, Length);
      
   } // End CcsdsPkt()

   /*
    * Length - Is the total packet length in bytes
    */
   public void InitPkt(int StreamId, int Length)
   {

      for (int i=0; i < Length; i++)
       {
         Packet[i] = 0;
       }
       
      Packet[CCSDS_IDX_STREAM_ID]   = new Integer(StreamId&0xFF).byteValue();
      Packet[CCSDS_IDX_STREAM_ID+1] = new Integer((StreamId&0xFF00)>>8).byteValue();
      Packet[CCSDS_IDX_SEQ_COUNT+1] = new Integer(0xC0).byteValue();
      Packet[CCSDS_IDX_LENGTH]      = new Integer((Length-CCSDS_LENGTH_ADJUST)&0xFF).byteValue();
      Packet[CCSDS_IDX_LENGTH+1]    = new Integer(((Length-CCSDS_LENGTH_ADJUST)&0xFF00)>>8).byteValue();
      
   } // End InitPkt()

   /*
   ** Construct a packet from raw data stream
   */
   public CcsdsPkt(byte[] MsgData)
   {

      Packet = MsgData;
            
   } // End CcsdsPkt()

   public int getTotalLength()
   {
      return (Packet[CCSDS_IDX_LENGTH] | (Packet[CCSDS_IDX_LENGTH+1] << 8)) + CCSDS_LENGTH_ADJUST;

   }// End getTotalLength()
      
   public int getStreamId()
   {
      return ( (( (Packet[CCSDS_IDX_STREAM_ID] & 0x00FF) | (Packet[CCSDS_IDX_STREAM_ID+1] << 8)) & CCSDS_MSK_MSG_ID) );

   }// End getStreamId()

   public int getSeqCount()
   {
      return ( (( (Packet[CCSDS_IDX_SEQ_COUNT] & 0x00FF) | (Packet[CCSDS_IDX_SEQ_COUNT+1] << 8)) & CCSDS_MSK_SEQ_CNT) );

   }// End getSeqCount() 

   public int getLength()
   {
      return ( ( (Packet[CCSDS_IDX_LENGTH]    & 0x00FF) | (Packet[CCSDS_IDX_LENGTH+1] << 8)) + CCSDS_LENGTH_ADJUST);

   }// End getLength()

   public byte[] getPacket()
   {
      return Packet;

   }// End getPacket()

} // End class CcsdsPkt
