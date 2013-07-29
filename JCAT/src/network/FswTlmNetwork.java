/*******************************************************************************
 * 
 * This class provides a bridge between the network package (has no knowledge of CCSDS)
 * and the application/GUI.
 * 
 *******************************************************************************/
package network;

import java.util.ArrayList;

import packets.ccsds.CcsdsTlmPkt;

public class FswTlmNetwork {
	/* TODO Determine whether a queue is needed after test with higher rates */
	private static volatile ArrayList<PktObserver> observers = new ArrayList<PktObserver>();

	public static synchronized void addTlmPkt(byte[] TlmData) {
		CcsdsTlmPkt TlmPkt = new CcsdsTlmPkt(TlmData);
		for (PktObserver o : observers)
			o.addPkt(TlmPkt);
	}
	
	public static final void removeObserver(String id) {
		for (PktObserver o : observers) {
			if (o.getID().equals(id)) {
				observers.remove(o);
				return;
			}
		}
	}
	
	public static void addObserver(PktObserver o) {
		observers.add(o);
	}
}
