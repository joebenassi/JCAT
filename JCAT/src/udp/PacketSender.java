package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * CURRENTLY UNUSED. SUBJECT TO CHANGE/REMOVAL. IMPROPERLY JAVADOC'D.
 * @author Joe Benassi
 *
 */
public final class PacketSender {
	private static InetAddress RECEIVER;
	private static int PORT;

	//public PacketSender(String ip, int port) throws UnknownHostException {
	//	set(ip, port);
	//}

	/*
	 * Be careful using this. Will throw exception if you do not instantiate
	 * RECEIVER, PORT
	 */

	public final static void sendPacket(byte[] buf) throws Exception {
		sendPacket(buf, RECEIVER, PORT);
	}

	public final static void set(String ip, int port) throws UnknownHostException {
		PORT = port;
		RECEIVER = InetAddress.getByName(ip);
	}

	/**
	 * @see http ://www.ehow.com/how_12194784_java-udp-send-file .html
	 * 
	 * @param buf
	 *            the byte array of data
	 * @param reciever
	 *            the IP address of the receiver
	 * @param port
	 *            the port open to UDP connections on receiver's end
	 * @throws Exception
	 */
	public static final void sendPacket(byte[] buf, InetAddress reciever,
			int port) throws Exception {
		DatagramSocket s = new DatagramSocket();

		DatagramPacket out = null;

		try {
			out = new DatagramPacket(buf, buf.length, reciever, port);
			System.out.println("SUCCESS 1/3");
		} catch (Throwable e) {
			System.out.println("ERROR 1/3");
		}

		try {
			s.send(out);
			System.out.println("SUCCESS 2/3");
		} catch (Throwable e) {
			System.out.println("ERROR 2/3");
		}

		try {
			s.close();
			System.out.println("SUCCESS 3/3");
		} catch (Throwable e) {
			System.out.println("ERROR 3/3");
		}
	}
}
