/*******************************************************************************
 * 
 * This class provides a bridge between the network package (has no knowledge of CCSDS)
 * and the application/GUI.
 * 
 *******************************************************************************/
package network;

import packets.ccsds.*;

public class FswCmdNetwork {

	private static PktWriter PktOutput;

	public FswCmdNetwork() {
		PktOutput = new PktWriter();
	}

	public void sendCmd(String name, CcsdsCmdPkt CmdPkt) {
		PktOutput
				.WriteCmdPkt(name, CmdPkt.GetPacket(), CmdPkt.getTotalLength());

	} // End getPktWriter()

	public PktWriter getPktWriter() {
		return PktOutput;

	} // End getPktWriter()

} // End class FswCmdNetwork
