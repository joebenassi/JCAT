package network;
/*
** MsgWriter 
**
*/
import java.util.Date;  
import java.net.*;

public class PktWriter
{
	private String port = "1234";
	private String ip = "127.000.000.001";
	private long MsgCount = 0;
	private final Date CreationDate = new Date();
 
   public PktWriter(){}
   
   public void WriteCmdPkt(byte[] CmdData, int dataL)
   {
	   try{sendPacket(CmdData);}
	   catch (Throwable e)
	   {
		   System.out.println("ABORTED!");
		   e.printStackTrace();
	   }
   }
   
   	private final void sendPacket(byte[] buf) throws Exception {
		DatagramSocket socket = new DatagramSocket();
		PE();
		DatagramPacket out = null;
		PE();
		InetAddress address = InetAddress.getByName(ip);
		PE();
		int port = Integer.parseInt(this.port);
		PE();
		out = new DatagramPacket(buf, buf.length, address, port);
		PE();
		socket.send(out);
		PE();
		socket.close();
		PE();
		System.out.println("COMPLETED!");
	}
   
   private final static void PE()
   {
	   System.out.println("SUCCESS!");
   }
}
