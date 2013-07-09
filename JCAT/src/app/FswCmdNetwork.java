/*******************************************************************************
 * 
 * This class provides a bridge between the network package (has no knowledge of CCSDS)
 * and the application/GUI.
 * 
 *******************************************************************************/
package app;

import fsw.CiApp;
import network.PktWriter;

import ccsds.*;

public class FswCmdNetwork
{

   private static PktWriter  PktOutput;
   
   FswCmdNetwork()
   {
      PktOutput = new PktWriter(CiApp.IP_PORT);   
   }
   
   public void sendCmd(CcsdsCmdPkt CmdPkt)
   {
      
      PktOutput.WriteCmdPkt(CmdPkt.GetPacket(), CmdPkt.getTotalLength());
      
   } // End getPktWriter()
   
   public PktWriter getPktWriter()
   {
      return PktOutput;
      
   } // End getPktWriter()

   public String getStatus()
   {
      if (PktOutput != null)
         return PktOutput.getStatus();
      else
         return new String("FswCmdNetwork not initialized");
   }
   
} // End class FswCmdNetwork
