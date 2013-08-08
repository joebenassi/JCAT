package network;

import java.net.*;

import packets.cmd.Cmd;

import utilities.EndianCorrector;

import main.Launcher;

/**
 * FULLY DOCUMENTED. This class is used to send CmdPkt data to a particular IP
 * address and port.
 * 
 * @author Joe Benassi
 * @author David McComas
 */
public final class PktWriter {

	/**
	 * The standard CI_LAB port for the CFS.
	 */
	private static String targetPort = "1234";

	/**
	 * An arbitrarily-defined IP address that is set by default.
	 */
	private static String targetIP = "192.168.1.11";

	/**
	 * Attempts to send the input CmdPkt to the CFS. If this succeeds a packet
	 * will be sent, this will put a "COMMAND SENT" message in the User Activity
	 * window. If this fails, a packet will NOT be sent, and this will put a
	 * "COMMAND NOT SENT" message in the User Activity window, and print the
	 * stack trace.
	 * 
	 * @param cmdPkt
	 *            The command packet to send.
	 */
	public static final void sendPacket(Cmd cmdPkt) {
		final String name = cmdPkt.getName();
		final byte[] CmdData = cmdPkt.getCcsdsPkt().getPacket();
		final int dataLength = cmdPkt.getCcsdsPkt().getLength();

		try {
			sendPacket(CmdData);
			Launcher.addUserActivity("COMMAND SENT: " + name + " to "
					+ Networker.getAppName(cmdPkt));
		} catch (Throwable e) {
			Launcher.addUserActivity("COMMAND NOT SENT: " + name + " to "
					+ Networker.getAppName(cmdPkt));
			e.printStackTrace();
		}
	}

	/**
	 * Sends the input byte[] to this class's static 'targetPort' and 'targetIP'
	 * after adjusting it for Endianness.
	 * 
	 * @param buf
	 *            The command byte[] with unadjusted endianness.
	 * @throws Exception
	 *             Can be thrown for a variety of reasons.
	 */
	private static final void sendPacket(byte[] buf) throws Exception {
		Exception e = null;
		DatagramSocket socket = new DatagramSocket();
		try {
			InetAddress address = InetAddress.getByName(targetIP);
			int intport = Integer.parseInt(targetPort);

			byte[] buf2 = new byte[buf.length];
			for (int i = 0; i < buf.length; i++)
				buf2[i] = buf[i];

			EndianCorrector.fixHeaderOut(buf2);

			DatagramPacket out = new DatagramPacket(buf2, buf2.length, address,
					intport);

			socket.send(out);
		} catch (Exception ex) {
			e = ex;
		}
		socket.close();
		if (e != null)
			throw e;
	}

	/**
	 * Returns the targetIP.
	 * 
	 * @return The IP that command packets are sent to.
	 */
	public static final String getTargetIP() {
		return targetIP;
	}

	/**
	 * Returns The targetPort.
	 * 
	 * @return The port that command packets are sent to.
	 */
	public static final String getPort() {
		return targetPort;
	}

	/**
	 * Sets the targetIP to the input String.
	 * 
	 * @param IP
	 *            The IP that future command packets will be sent to.
	 */
	public static final void setTargetIP(String IP) {
		targetIP = IP;
	}

	/**
	 * Sets the targetPort to the input String.
	 * 
	 * @param Port
	 *            The port that future command packets will be sent to.
	 */
	public static final void setPort(String Port) {
		targetPort = Port;
	}
}
