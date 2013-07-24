package network;

/*TODO read in cFE headers like osconfig.h to get #defines*/
import java.io.IOException;
import java.net.*;

import main.Launcher;

public class PktReader {
	private PktEventInterface MsgEvent;
	private static DatagramSocket MsgSock;
	private static int port = 1235;

	public PktReader(PktEventInterface MsgEvent) {
		this.MsgEvent = MsgEvent;
		try {
			if (MsgSock == null)
				MsgSock = new DatagramSocket(port);
		} catch (IOException ex) {
			System.err.println("Error creating DatagramSocket on port " + port);
			ex.printStackTrace();
		}

		startThread();
	}

	private void startThread() {
		System.out.println("THREAD STARTED PKTREADER");
		byte[] DataBuffer = new byte[1024];
		final DatagramPacket DataPacket = new DatagramPacket(DataBuffer, 1024);

		final int myInstanceNum = Launcher.getInstanceNum();
		final Thread t = new Thread(new Runnable() {
			public void run() {
				System.out.println("PKTREADER: Thread running");
				while (myInstanceNum == Launcher.getInstanceNum()) {
					try {
						/* TODO Thread.yield(); // Platform independent friendly */
						Thread.sleep(10);
					} catch (InterruptedException e) {}

					try {
						MsgSock.receive(DataPacket);
						MsgEvent.addTlmPkt(DataPacket.getData()); 
						
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

	public static void main(String[] args) {
		String first = "abc.d.efg.hi";
		System.out.println("EXPECTED: abc.d00.efg.hi0");
		System.out.println("RESULT : " + addZeroes(first));
	}

	public static String addZeroes(String s) {
		String[] components = new String[]{"","","",""};
		
		int index = 0;
		for (int i = 0; i < s.length(); i++){
			if (s.charAt(i) == ".".charAt(0))
				index++;
			else {
				components[index] += s.charAt(i);
			}
		}

		String output = "";
		
		for (int i = 0; i < 4; i++){
			while (components[i].length() != 3) {
				components[i] = "0" + components[i];
			}
			output += components[i];
			if (i < 4-1) output += ".";
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
