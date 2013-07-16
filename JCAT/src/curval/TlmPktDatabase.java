package curval;

import java.util.ArrayList;  

import packets.ccsds.*;


public class TlmPktDatabase implements PktSource
{
   private CcsdsTlmPkt[]  pktDatabase; 
   private ArrayList<PktObserver> observers  = new ArrayList<PktObserver>();
   
  public TlmPktDatabase()
   {
      pktDatabase = new CcsdsTlmPkt[4096];
   }
   
   public void registerPkt(int StreamId, CcsdsTlmPkt Pkt)
   {
      pktDatabase[StreamId] = Pkt;

   }

   public void updatePkt(int StreamId, byte[] ByteArray)
   {
      pktDatabase[StreamId] = new CcsdsTlmPkt(ByteArray);
      notifyObservers(StreamId);
   }
   
   @Override
  public void registerObserver(PktObserver observer)
   {
      observers.add(observer);

   }

   @Override
   public void removeObserver(PktObserver observer)
   {
      observers.remove(observer);

   }

   @Override
   public void notifyObservers(int StreamId)
   {
      for (PktObserver ob : observers)
         ob.update(StreamId);
  }
}
