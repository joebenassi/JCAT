package network;

import java.util.ArrayList;

import packets.ccsds.CcsdsTlmPkt;

/**
 * FULLY DOCUMENTED. This class allows telemetry packets received from JCAT to be
 * shared with multiple observers. In JCAT, there is currently only one
 * observer.
 * 
 * @author Joe Benassi
 * @author David McComas
 */
public class FswTlmNetwork {
	/**
	 * The observers to receive the telemetry packet.
	 */
	private static volatile ArrayList<PktObserver> observers = new ArrayList<PktObserver>();

	/**
	 * Creates a CcsdsTlmPkt from the input data and sends it to each observer
	 * in observers.
	 * 
	 * @param TlmData
	 *            The data to send to each observer.
	 */
	public static synchronized void addTlmPkt(byte[] TlmData) {
		CcsdsTlmPkt TlmPkt = new CcsdsTlmPkt(TlmData);
		for (PktObserver o : observers)
			o.addPkt(TlmPkt);
	}

	/**
	 * No longer sends telemetry packets to the observer with the input ID. If
	 * there is no observer with such an ID, then nothing changes. If more than
	 * one observer has this ID, only the first is removed.
	 * 
	 * @param id The id belonging to the observer to no longer send telemetry packets.
	 */
	public static final void removeObserver(String id) {
		for (PktObserver o : observers) {
			if (o.getID().equals(id)) {
				observers.remove(o);
				return;
			}
		}
	}

	/**
	 * Adds the input PktObserver to the set of observers to receive telemetry packets.
	 * 
	 * @param o The observer to now receive telemetry packets.
	 */
	public static void addObserver(PktObserver o) {
		observers.add(o);
	}
}
