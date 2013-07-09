package network;
/*
** MsgReader
**
** @todo
**   -# Add argument and/or config file
**   -# Add XML processing for command definitions
**   -# Would be best to read in cFE headers like osconfig.h to get #defines
*/
import java.util.Date;  
import java.io.IOException;
import java.net.*;

public class PktReader implements Runnable
{

   private    PktEventInterface MsgEvent;
   
   protected  int               MsgPort  = 0;
   protected  long              MsgCount = 0;
   protected  DatagramSocket    MsgSock;
   protected  boolean           CreatedSocket = false;
   protected  Date              CreationDate;
   
   public PktReader(int MsgPort, PktEventInterface MsgEvent)
   {
       this.MsgEvent = MsgEvent; 
       try
       {
           this.MsgPort = MsgPort;
           this.MsgCount = 0;
           MsgSock = new DatagramSocket(MsgPort);
           CreatedSocket = true;
           CreationDate = new Date();
           System.out.println("MsgReader: Created socket on port " + MsgPort);
       } 
       catch(IOException ex) 
       {
           System.err.println("Error creating DatagramSocket on port " + MsgPort);
           ex.printStackTrace();
           // A real application would return and not do a stack trace
       }

   } // End MsgReader()
   
   @Override
public void run()
   {
        
      byte[] DataBuffer = new byte[1024];
      DatagramPacket DataPacket = new DatagramPacket(DataBuffer,1024);
        
      if (CreatedSocket)
      {
         System.out.println("MsgReader: Start while loop");
         while (true)
         {
            try 
            {
         
               MsgSock.receive(DataPacket);
               MsgCount++;
               MsgEvent.addTlmPkt(DataPacket.getData()); // @todo - Do I need length?
            }
            catch(Exception ex) 
            {
               System.err.println("MsgRead: Error reading text from the server");
               ex.printStackTrace();
            }
            try {
               //Thread.yield(); // Platform independent friendly threading - Doesn't throw interrupt exception
               Thread.sleep(10);
           } catch (InterruptedException e) {
               //We've been interrupted: no more messages.
               System.err.println("MsgRead: Terminating thread");
               return;
           }
         
              
         } // End while
      } // End if SocketCreated
      else
      {
         System.out.println("MsgReader: Socket creation failure prevented while loop from starting");
      }
      
   } // End run()
       
   public String getStatus()
   {
   
      String Status = "MsgReader Socket:\n";

      if (CreatedSocket)
      {
      Status += "   Created: " + CreationDate.toString() + "\n" + 
                "   Port: " + MsgSock.getLocalPort() + "\n" + 
                "   Received: " + MsgCount + "\n\n";
      }
      else
      {
         Status += "   Socket not created\n\n";
      }
      return Status;
      
   } // End getStatus()
   
} // End class MsgReader
