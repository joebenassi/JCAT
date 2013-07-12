/*******************************************************************************
 * 
 * This class provides a bridge between the network package (has no knowledge of CCSDS)
 * and the application/GUI.
 * 
 *******************************************************************************/
package network;


import packets.ccsds.*;

public class FswCmdNetwork
{

   private static PktWriter  PktOutput;
   
   public FswCmdNetwork()
   {
      PktOutput = new PktWriter();
   }
   
   public void sendCmd(CcsdsCmdPkt CmdPkt)
   {
      
      PktOutput.WriteCmdPkt(CmdPkt.GetPacket(), CmdPkt.getTotalLength());
      
   } // End getPktWriter()
   
   public PktWriter getPktWriter()
   {
      return PktOutput;
      
   } // End getPktWriter()
   
} // End class FswCmdNetwork
