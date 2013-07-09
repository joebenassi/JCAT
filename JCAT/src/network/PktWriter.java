package network;
/*
** MsgWriter 
**
*/
import java.util.Date;  
import java.io.IOException;
import java.net.*;


public class PktWriter
{

   protected  int             MsgPort;
   protected  long            MsgCount = 0;
   protected  DatagramSocket  MsgSock;
   protected  boolean         CreatedSocket = false;
   protected  Date            CreationDate;
 
  
   public PktWriter(int CmdPort)
   {
       this.MsgPort = CmdPort;
       try
       {
           MsgSock = new DatagramSocket();
           CreatedSocket = true;
           CreationDate = new Date();
           System.out.println("MsgWriter: Created socket");
       } 
       catch(IOException ex) 
       {
           System.err.println("MsgWriter: Error creating DatagramSocket");
           ex.printStackTrace();
           // A real application would return and not do a stack trace
       }

   } // End MsgWriter()
   
   public void WriteCmdPkt(byte[] CmdData, int CmdLen)
   {
    
      try
      {
      
         DatagramPacket CmdDatagram = new DatagramPacket(CmdData, CmdLen,
                                                         InetAddress.getLocalHost(),MsgPort);
         MsgSock.send(CmdDatagram);
         MsgCount++;
         
      } 
      catch(Exception ex)
      {
         System.err.println("Msgwriter: Error writing datagram");
         System.err.println(ex);
         ex.printStackTrace();
      }

        
   } // End WriteCmdPkt()

   public String getStatus()
   {
   
      String Status = "MsgWriter Socket:\n";

      if (CreatedSocket)
      {
      Status += "   Created: " + CreationDate.toString() + "\n" + 
                "   Port: " + MsgSock.getLocalPort() + "\n" + 
                "   Sent: " + MsgCount + "\n\n";
      }
      else
      {
         Status += "   Socket not created\n\n";
      }
      return Status;
      
   } // End getStatus()
   
} // End class MsgWriter
