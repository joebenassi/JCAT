package applications;

import packets.cmd.CmdIntParam;
import packets.cmd.CmdPkt;

public class ToApp {
	// These must match the cFE's test lab TO (UDP based) definitions
	// private static final String IP_ADDR = "192.168.1.81";
	// static final public String IP_ADDR = "127.0.0.1";
	private static final int CMD_MID = 0x1880;
	private static final int CMD_FC_ADD_PKT = 2;
	private static final int CMD_FC_ENA_TLM = 6;
	private static final String PREFIX_STR = "TO";

	public static final CmdPkt getEnaTlmCmd() {
		int cmdDataLen = 15;
		byte[] cmdDataBuf = null;

		cmdDataBuf = new byte[cmdDataLen];
		cmdDataBuf[0] = 0x31; // 127.
		cmdDataBuf[1] = 0x32;
		cmdDataBuf[2] = 0x37;
		cmdDataBuf[3] = 0x2E;
		cmdDataBuf[4] = 0x30; // 000.
		cmdDataBuf[5] = 0x30;
		cmdDataBuf[6] = 0x30;
		cmdDataBuf[7] = 0x2E;
		cmdDataBuf[8] = 0x30; // 000.
		cmdDataBuf[9] = 0x30;
		cmdDataBuf[10] = 0x30;
		cmdDataBuf[11] = 0x2E;
		cmdDataBuf[12] = 0x30; // 001
		cmdDataBuf[13] = 0x30;
		cmdDataBuf[14] = 0x31;
		return new CmdPkt(PREFIX_STR, "TO_LAB_APP_ENA_TLM", CMD_MID, CMD_FC_ENA_TLM,
				cmdDataBuf, cmdDataLen);
	}

	public static final CmdPkt[] getConfigTlm(int[] Add) {
		final CmdPkt[] cmdPkts = new CmdPkt[Add.length];
		CmdPkt cmdPkt;
		for (int i = 0; i < Add.length; i++) {
			cmdPkt = new CmdPkt(PREFIX_STR, "TO_LAB_APP_ADD_PKT", CMD_MID, CMD_FC_ADD_PKT,
					7);
			cmdPkt.addParam(new CmdIntParam("Message ID", true, new String[0], Add[i] + "", 2)); // // 3840 = 0xF00
												// (ExApp), 2048 =
												// 0x800 (ES HK)
			cmdPkt.addParam(new CmdIntParam("Pkt Size", true, new String[0], "50", 2));
			cmdPkt.addParam(new CmdIntParam("SB QoS",
					true, new String[0], "0", 2));
			cmdPkt.addParam(new CmdIntParam("Buffer Cnt", true, new String[0], "1", 1));
			cmdPkt.loadParamList();
			cmdPkts[i] = cmdPkt;
		}
		return cmdPkts;
	}
}
