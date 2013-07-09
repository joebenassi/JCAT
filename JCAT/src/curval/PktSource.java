package curval;

public interface PktSource
{

   public void registerObserver(PktObserver observer);

   public void removeObserver(PktObserver observer);

   public void notifyObservers(int StreamId);

} // End interface PktSource
