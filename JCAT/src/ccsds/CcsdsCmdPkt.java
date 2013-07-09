package ccsds;


public class CcsdsCmdPkt extends CcsdsPkt 
{
   static final public int CCSDS_IDX_CMD_HDR   = 6;
   static final public int CCSDS_IDX_CMD_DATA  = 8;
   static final public int CCSDS_CMD_HDR_LEN   = 8;

   public CcsdsCmdPkt(int Length)
   {
      super(Length);
      
   } // End CcsdsCmdPkt()
   
   public CcsdsCmdPkt(int StreamId, int Length)
   {
        super(StreamId, Length);
      
   } // End CcsdsCmdPkt()

   public CcsdsCmdPkt(int StreamId, int Length, int FuncCode)
   {
        super(StreamId, Length);
        InitSecHdr(FuncCode);
        
   } // End CcsdsCmdPkt()

   public void InitSecHdr(int FuncCode)
   {

        Packet[CCSDS_IDX_CMD_HDR]   = 0;
        Packet[CCSDS_IDX_CMD_HDR+1] = 0;
        
        Packet[CCSDS_IDX_CMD_HDR+1] = new Integer(FuncCode&0xFF).byteValue();

   } // End InitSecHdr()
   
   public byte ComputeChecksum()
   {

      int  PktLen   = getTotalLength();
      byte Checksum = new Integer(0xFF).byteValue();

      for (int i=0; i < PktLen; i++)
      {
         Checksum ^= Packet[i];
      }
      Packet[CCSDS_IDX_CMD_HDR] = new Integer((Checksum & 0xFF)).byteValue();
      
      //System.out.println("Checksum = " + Checksum);
      
       return Checksum;

   } // End ComputeChecksum()

   /*
    * Load data portion of command data and compute the packet checksum
    */
   public void LoadData(byte Data[], int DataLength)
   {
   
      
      for (int i=0; i < DataLength; i++)
         Packet[CCSDS_IDX_CMD_DATA+i] = Data[i];
      
      ComputeChecksum();
      
   }
   
   public byte[] GetPacket()
   {
   
      return Packet;
   
   } // End GetPacket()
   
    public static void main(String [] args) throws Exception
    {
    
      boolean BuffersMatch = true;
      Integer Temp, i;
      byte[] DataBuffer = new byte[16];
      byte[] ByteCmdPkt1 = new byte[24]; 
      byte[] ByteCmdPkt2; 
      CcsdsCmdPkt CmdPkt = new CcsdsCmdPkt(0x1880, 24, 6);
      CcsdsCmdPkt ResetCmdPkt = new CcsdsCmdPkt(0x6318, 8, 0);
      CcsdsCmdPkt NoopCmdPkt = new CcsdsCmdPkt(0x6318, 8, 1);
      
      Temp = 0x80;
      ByteCmdPkt1[0] = Temp.byteValue();
      ByteCmdPkt1[1] = 0x18;
     ByteCmdPkt1[2] = 0x00;
     Temp = 0xC0;
     ByteCmdPkt1[3] = Temp.byteValue();
     ByteCmdPkt1[4] = 0x11;
     ByteCmdPkt1[5] = 0x00;
     Temp = 0xE3;  // This is not correct!!
     ByteCmdPkt1[6] = Temp.byteValue();
     ByteCmdPkt1[7] = 0x06;

     ByteCmdPkt1[8] = 0x31;
     ByteCmdPkt1[9] = 0x32;
     ByteCmdPkt1[10] = 0x37;
     ByteCmdPkt1[11] = 0x2E;
     ByteCmdPkt1[12] = 0x30;
     ByteCmdPkt1[13] = 0x30;
     ByteCmdPkt1[14] = 0x30;
     ByteCmdPkt1[15] = 0x2E;
     ByteCmdPkt1[16] = 0x30;
     ByteCmdPkt1[17] = 0x2E;
     ByteCmdPkt1[18] = 0x30;
     ByteCmdPkt1[19] = 0x31;
     ByteCmdPkt1[20] = 0x00;
     ByteCmdPkt1[21] = 0x00;
     ByteCmdPkt1[22] = 0x00;
     ByteCmdPkt1[23] = 0x00;
      
     for (i=0; i < 16; i++)
        DataBuffer[i] = ByteCmdPkt1[CCSDS_IDX_CMD_DATA+i];
     
     CmdPkt.LoadData(DataBuffer, 16);
     
      ByteCmdPkt2 = CmdPkt.GetPacket();
      
      for (i=0; i < 24; i++)
      {
        if (ByteCmdPkt1[i] != ByteCmdPkt2[i])
        {
           BuffersMatch = false;
           System.out.println("Buffers miscompare at index " + i +
                 " DataBuffer1 = " + Integer.toHexString(ByteCmdPkt1[i]) +
                 " DataBuffer2 = " + Integer.toHexString(ByteCmdPkt2[i]) );
        }
      }
      
      if (BuffersMatch)
         System.out.println("Buffers Match");

     NoopCmdPkt.ComputeChecksum();
      ByteCmdPkt2 = NoopCmdPkt.GetPacket();
     for (i=0; i <8; i++)
        System.out.println("i: " + i + " " + Integer.toHexString(ByteCmdPkt2[i]));
        
     ResetCmdPkt.ComputeChecksum();
      ByteCmdPkt2 = ResetCmdPkt.GetPacket();
     for (i=0; i < 8; i++)
        System.out.println("i: " + i + " " + Integer.toHexString(ByteCmdPkt2[i]));

    } // End main()

   
} // End class CcsdsCmdPkt
