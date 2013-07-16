package network;

/*
 ** MsgWriter 
 **
 */
import java.util.Date;
import java.net.*;

import main.Launcher;

public class PktWriter {
	private static String port = "1234";
	private static String ip = "127.000.000.001";
	private long MsgCount = 0;
	private final Date CreationDate = new Date();

	public PktWriter() {
	}

	public void WriteCmdPkt(String name, byte[] CmdData, int dataL)
   {
	   try{sendPacket(CmdData);
	   Launcher.addUserActivity("COMMAND SENT: " + name);
	   }
	   catch (Throwable e)
	   {
		   Launcher.addUserActivity("COMMAND NOT SENT: " + name + ". Invalid IP, Port, or packet length");
		   e.printStackTrace();
	   }
   }

	private final void sendPacket(byte[] buf) throws Exception {
		DatagramSocket socket = new DatagramSocket();
		
		InetAddress address = InetAddress.getByName(ip);
		int intport = Integer.parseInt(this.port);

		DatagramPacket out = new DatagramPacket(buf, buf.length, address, intport);
		socket.send(out);
		socket.close();
	}

	public static void setIP(String IP) {
		ip = IP;
	}

	public static void setPort(String Port) {
		port = Port;
	}
}
