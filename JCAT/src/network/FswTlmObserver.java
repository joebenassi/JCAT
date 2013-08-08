package network;

import java.util.ArrayList;

import packets.ccsds.CcsdsTlmPkt;

/**
 * FULLY DOCUMENTED. This class is to be used for any entity that wants to
 * receive telemetry packets. Currently there is only one such entity, but this
 * may be changed in the future.
 * 
 * @author Joe Benassi
 * @author David McComas
 */
public class FswTlmObserver {

	/**
	 * The telemetry packets that have yet to be parsed by Networker.
	 */
	private volatile ArrayList<CcsdsTlmPkt> pkts = new ArrayList<CcsdsTlmPkt>();

	/**
	 * An arbitrary ID used to differentiate different FswTlmObservers.
	 */
	private final String id;

	/**
	 * Creates a new FswTlmObserver with the input ID.
	 * 
	 * @param id
	 *            The ID to set as the ID of this new FswTlmObserver Object.
	 */
	public FswTlmObserver(String id) {
		this.id = id;
	}

	/**
	 * Returns if there are no remaining CcsdsTlmPkts.
	 * 
	 * @return If all CcsdsTlmPkts have been parsed by Networker.
	 */
	public boolean isEmpty() {
		return pkts.isEmpty();
	}

	/**
	 * Adds the input packet, to later be parsed by Networker.
	 * 
	 * @param pkt
	 *            The CcsdsTlmPkt to be parsed by Networker.
	 */
	public void addPkt(CcsdsTlmPkt pkt) {
		pkts.add(pkt);
	}

	/**
	 * Returns the oldest remaining CcsdsTlmPkt and removes it.
	 * 
	 * @return The oldest remaining CcsdsTlmPkt.
	 */
	public final CcsdsTlmPkt getTlmPkt() {
		return pkts.remove(0);
	}

	/**
	 * Returns the arbitrary ID given to this FswTlmObserver.
	 * 
	 * @return The ID of this FswTlmObserver.
	 */
	public final String getID() {
		return id;
	}
}