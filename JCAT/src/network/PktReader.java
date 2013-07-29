package network;

/*TODO read in cFE headers like osconfig.h to get #defines*/
import java.io.IOException;
import java.net.*;

import utilities.EndianCorrector;

import main.Launcher;

public class PktReader {
	private static DatagramSocket MsgSock;
	private static final int port = 1235;

	public static void start() {
		try {
			MsgSock = new DatagramSocket(port);
			MsgSock.setReuseAddress(true);
			//MsgSock.connect(InetAddress.getLocalHost(), port);
		} catch (IOException ex) {
			System.err.println("Error creating DatagramSocket on port " + port);
			ex.printStackTrace();
		}
		startThread();
	}

	private static void startThread() {
		final DatagramPacket DataPacket = new DatagramPacket(new byte[1024],
				1024);

		final Thread t = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						/*
						 * TODO Thread.yield();? // Platform independent
						 * friendly
						 */
						Thread.sleep(40);
					} catch (InterruptedException e) {
					}

					try {
						MsgSock.receive(DataPacket);

						byte[] data = DataPacket.getData();
						EndianCorrector.fixHeaderIn(data);
						FswTlmNetwork.addTlmPkt(data);
					} catch (Exception ex) {
						Launcher.addUserActivity("PKTREADER:  Error reading text from the server");
						ex.printStackTrace();
					}
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

	public static String getIP() {
		String s = "Cannot Access";

		try {
			s = InetAddress.getLocalHost().getHostAddress();
			s = addZeroes(s);
		} catch (Throwable e) {
		}
		return s;
	}

	public static final String getPort() {
		return port + "";
	}
}
