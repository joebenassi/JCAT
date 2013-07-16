/*******************************************************************************
 * 
 * This class provides a bridge between the network package (has no knowledge of CCSDS)
 * and the application/GUI.
 * 
 *******************************************************************************/
package network;

import java.util.concurrent.ConcurrentLinkedQueue; 

import packets.ccsds.CcsdsTlmPkt;

import curval.*;


public class FswTlmNetwork implements PktEventInterface
{

   private static PktReader  PktInput;
   // @todo - Determine whether a queue is needed after test with higher rates 
   private static ConcurrentLinkedQueue<CcsdsTlmPkt> TlmPktQ = new ConcurrentLinkedQueue<CcsdsTlmPkt>();
   private TlmPktDatabase TlmDatabase = null;

   public FswTlmNetwork(TlmPktDatabase Database)
   {
      PktInput = new PktReader(this);
      TlmDatabase = Database;
   }

   /*
    ** This method queues the message so it can be processed safely (WRT threads)
    ** in the GUI event loop.
    */
    @Override
	public synchronized void addTlmPkt(byte[] TlmData) 
    {
       
       CcsdsTlmPkt TlmPkt = new CcsdsTlmPkt(TlmData);
       TlmPktQ.add(TlmPkt);
       TlmDatabase.notifyObservers(TlmPkt.getStreamId());
       
    } // End addTlmPkt()
   
   
   public ConcurrentLinkedQueue<CcsdsTlmPkt> getTlmPktQ()
   {
      return TlmPktQ;
      
   } // End getTlmPktQ()
   
   public synchronized CcsdsTlmPkt getTlmPkt()
   {
      return TlmPktQ.remove();
      
   } // End getTlmPkt()

   public PktReader getPktReader()
   {
      return PktInput;
      
   } // End getMsgWriter()
   /*   
   public String getStatus()
   {
      return PktInput.getStatus();
   }*/
} // End class FswTlmNetwork
