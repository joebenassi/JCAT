package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

import utilities.EndianCorrector;
import gui.mainpage.TopBar;

/**
 * FULLY DOCUMENTED. UNSTABLE. This class is the first to receive telemetry
 * packets. When it receives them, it adjusts their Endian, and sends them to
 * all FswTlmObservers. A common error that can occur is if there is an existing
 * JCAT instance listening to port 1235, the port where the CFS sends packets.
 * If this happens, the new JCAT instance will not receive packets.
 * 
 * @author Joe Benassi
 * @author David McComas
 * 
 *         TODO Read in cFE headers like osconfig.h to get #defines. TODO Allow
 *         multiple instances of JCAT share the same socket. TODO Ensure that
 *         EnableWirelessTelemetry works cross-platform. TODO Ensure that
 *         EnableLocalTelemetry works cross-platform.
 */
public final class PktReader {
	private static DatagramSocket MsgSock;
	private static final int port = 1235;
	private static boolean functional = true;
	private static final int pause = 40;

	/**
	 * If port 1235 is not occupied, it launches a thread to receive packets on
	 * that port every 40ms. If it IS occupied, this prints an exception and
	 * sets the variable 'functional' false. This method is called only once,
	 * and in JCAT's main method.
	 */
	public static void start() {
		try {
			MsgSock = new DatagramSocket(port);
			startThread();
		} catch (IOException ex) {
			ex.printStackTrace();
			functional = false;
		}
	}

	/**
	 * Starts a thread that receives all packets on port 1235. Updates the
	 * TopBar's 'last received packet' value, adjusts the header of packets
	 * based on the Endian, and sends all packets to each observer.
	 */
	private static void startThread() {
		final DatagramPacket DataPacket = new DatagramPacket(new byte[1024],
				1024);

		final Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(pause);
					} catch (Throwable e) {
						e.printStackTrace();
					}

					try {
						MsgSock.receive(DataPacket);
						byte[] data = DataPacket.getData();
						TopBar.packetReceived();
						EndianCorrector.fixHeaderIn(data);
						FswTlmNetwork.addTlmPkt(data);
					} catch (Exception ex) {
					}
				}
			}
		});
		t.setDaemon(true);
		t.start();
	}

	/**
	 * Returns a String that equals either null or the wireless host address. Is
	 * fully-functional on Windows, but has not been tested on Linux or Mac
	 * platforms.
	 * 
	 * @return A String equaling the host address of this machine. If this could
	 *         not be determined, this will return null.
	 * 
	 *         TODO Ensure this works cross-platform.
	 * 
	 */
	public static String getWirelessIP() {
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
		return null;
	}

	/**
	 * Returns a String that equals either null or the local host address. It is
	 * fully-functional on Windows, ut has not been tested on Linux or Mac
	 * platforms.
	 * 
	 * @return A String equaling the host address of this machine. If this could
	 *         not be determined, this returns null.
	 * 
	 *         TODO Ensure this works cross-platform.
	 */
	public static String getLocalIP() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns the port this listens for packets from. Currently 1235.
	 * 
	 * @return The String "1235".
	 */
	public static final String getPort() {
		return port + "";
	}

	/**
	 * Returns if this is listening for telemetry packets.
	 * 
	 * @return If the port '1235' on this machine was not occupied when JCAT
	 *         launched.
	 */
	public static boolean isFunctional() {
		return functional;
	}
}
