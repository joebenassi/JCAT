package network;

/*TODO read in cFE headers like osconfig.h to get #defines*/
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

import packets.ccsds.CcsdsTlmPkt;

import main.Launcher;
import utilities.EndianCorrector;
import gui.mainpage.TopBar;

public class PktReader {
	private static DatagramSocket MsgSock;
	private static final int port = 1235;
	private static boolean functional = true;

	public static void start() {
		try {
			MsgSock = new DatagramSocket(port);
			startThread();
		} catch (IOException ex) {
			ex.printStackTrace();
			functional = false;
		}
		/* TODO undo this? */
	}

	private static void startThread() {
		final DatagramPacket DataPacket = new DatagramPacket(new byte[1024],
				1024);
		
		final Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						/*
						 * TODO Thread.yield();? // Platform independent
						 * friendly
						 */
						Thread.sleep(40);
					} catch (Throwable e) {e.printStackTrace();
					}
				
					try {
						MsgSock.receive(DataPacket);
						byte[] data = DataPacket.getData();
						TopBar.packetReceived();
						//try {Launcher.addUserActivity("PKTREADER: STRMID = " + CcsdsTlmPkt.getID(data));
						//} catch (Throwable e){}
						EndianCorrector.fixHeaderIn(data);
						FswTlmNetwork.addTlmPkt(data);
					} catch (Exception ex) {}
			}
			}
		});
		t.setDaemon(true);
		t.start();
	}

	public static String addZeroes(String s) {
		String[] components = new String[] { "", "", "", "" };

		int index = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == ".".charAt(0))
				index++;
			else {
				components[index] += s.charAt(i);
			}
		}

		String output = "";

		for (int i = 0; i < 4; i++) {
			while (components[i].length() != 3) {
				components[i] = "0" + components[i];
			}
			output += components[i];
			if (i < 4 - 1)
				output += ".";
		}
		return output;
	}

/*	public static void main(String[] args) {
		System.out.println("Wireless IP: " + getIP());
		System.out.println("Local IP: " + getLocalIP());
	}*/

	/* only tested on windows */
	public static String getIP() {
		ArrayList<String> ips = new ArrayList<String>();

		Enumeration<NetworkInterface> interfaces = null;

		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
		} catch (Throwable e) {
		}

		while (interfaces != null && interfaces.hasMoreElements()) {
			final NetworkInterface cur = interfaces.nextElement();
			for (final InterfaceAddress addr : cur.getInterfaceAddresses()) {
				final InetAddress add = addr.getAddress();
				if (!add.isLoopbackAddress())
					if (addr.getBroadcast() != null)
						if (!addr.getBroadcast().isSiteLocalAddress())
							return add.getHostAddress();
			}
		}
		return "Cannot Access";
	}
	
	public static String getLocalIP() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "0.1.2.3";
	}

	public static final String getPort() {
		return port + "";
	}

	public static boolean isFunctional() {
		return functional;
	}
}
