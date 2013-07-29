package network;

import java.util.ArrayList;

import packets.ccsds.CcsdsTlmPkt;

public class PktObserver {
	private volatile ArrayList<CcsdsTlmPkt> pkts = new ArrayList<CcsdsTlmPkt>();
	private final String id;

	public PktObserver(String id) {
		this.id = id;
	}

	public boolean isEmpty() {
		return pkts.isEmpty();
	}

	public void addPkt(CcsdsTlmPkt pkt) {
		pkts.add(pkt);
	}

	public final CcsdsTlmPkt getTlmPkt() {
		return pkts.remove(0);
	}

	public final String getID() {
		return id;
	}
}