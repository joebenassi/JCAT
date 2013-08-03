package network;

/*
 ** MsgWriter 
 **
 */

import java.net.*;

import packets.ccsds.CcsdsPkt;

import utilities.EndianCorrector;

import main.Launcher;

public class PktWriter {
	private static String port = "1234";
	//private static String ip = "192.168.224.128"; //VMWare
	private static String ip = "192.168.1.11";  //ColdFires

	public PktWriter() {
	}

	public void WriteCmdPkt(String name, byte[] CmdData, int dataL) {
		try {
			sendPacket(CmdData);
			Launcher.addUserActivity("COMMAND SENT: " + name + " to " + Networker.getAppName(CmdData));
		} catch (Throwable e) {
			Launcher.addUserActivity("COMMAND NOT SENT: " + name + " to " + Networker.getAppName(CmdData));
			e.printStackTrace();
		}
	}

	private final void sendPacket(byte[] buf) throws Exception {
		DatagramSocket socket = new DatagramSocket();
		InetAddress address = InetAddress.getByName(ip);
		int intport = Integer.parseInt(PktWriter.port);

		byte[] buf2 = new byte[buf.length];
		for (int i = 0; i < buf.length; i++)
			buf2[i] = buf[i];

		EndianCorrector.fixHeaderOut(buf2);
		
		DatagramPacket out = new DatagramPacket(buf2, buf2.length, address,
				intport);

		socket.send(out);
		socket.close();
	}

	public static final String getIP() {
		return ip;
	}

	public static final String getPort() {
		return port;
	}

	public static final void setIP(String IP) {
		ip = IP;
	}

	public static final void setPort(String Port) {
		port = Port;
	}
}
