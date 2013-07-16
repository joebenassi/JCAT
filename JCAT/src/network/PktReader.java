package network;

/*
 ** MsgReader
 **
 ** @todo
 **   -# Add argument and/or config file
 **   -# Add XML processing for command definitions
 **   -# Would be best to read in cFE headers like osconfig.h to get #defines
 */
//import java.util.Date;
import java.io.IOException;
import java.net.*;

import main.Launcher;

public class PktReader {
	private PktEventInterface MsgEvent;
	private int MsgPort = 1235;
	private static DatagramSocket MsgSock;
	private static boolean shouldEnd = false;

	public PktReader(PktEventInterface MsgEvent) {
		this.MsgEvent = MsgEvent;
		shouldEnd = false;
		try {
			if (MsgSock == null)
			MsgSock = new DatagramSocket(MsgPort);
		} catch (IOException ex) {
			System.err.println("Error creating DatagramSocket on port "
					+ MsgPort);
			ex.printStackTrace();
		}

		startThread();
	}

	public static void end()
	{
		shouldEnd = true;
	}
	
	private void startThread()
	{
		shouldEnd = false;
		System.out.println("THREAD STARTED PKTREADER");
		byte[] DataBuffer = new byte[1024];
		final DatagramPacket DataPacket = new DatagramPacket(DataBuffer,1024);
	 
		final Thread t = new Thread(
			  new Runnable(){
				  public void run(){
					  while(!shouldEnd){
						 try {
				               //Thread.yield(); // Platform independent friendly threading - Doesn't throw interrupt exception
				               Thread.sleep(10);
				           } catch (InterruptedException e) {}
						 
				            try 
				            {
				               MsgSock.receive(DataPacket);
				               //MsgCount++;
				               MsgEvent.addTlmPkt(DataPacket.getData()); // @todo - Do I need length?
				            }
				            catch(Exception ex) 
				            {
				               Launcher.addUserActivity("Class PktReader:  Error reading text from the server");
				               ex.printStackTrace();
				            }
					  }
					 // MsgSock = null;
				  }
			  });
		t.setDaemon(true);
		t.start();
   }
}
