package packets.cmd;

import java.util.ArrayList;

import packets.ccsds.CcsdsCmdPkt;
import packets.parameters.CmdParam;
import network.Networker;

public class CmdPkt {
	private final String Name;
	private ArrayList<CmdParam> ParamList = new ArrayList<CmdParam>();
	private int ParamByteLen;
	private CcsdsCmdPkt CmdPkt;

	public CmdPkt(String Name, int MsgId, int FuncCode, int DataLen) {
		this.Name = Name;
		ParamByteLen = 0;

		CmdPkt = new CcsdsCmdPkt(MsgId,
				CcsdsCmdPkt.CCSDS_CMD_HDR_LEN + DataLen, FuncCode);
		CmdPkt.ComputeChecksum();
	}

	public void execute(String[] paramValues) {
		for (int i = 0; i < paramValues.length; i++) {		
			if (ParamList.get(i).isInputParam())
				ParamList.get(i).setValue(paramValues[i]);
			else {
				CmdParam relevant = ParamList.get(i);
				String input = paramValues[i];

				for (int j = 0; j < relevant.getChoiceOptions().length; j++) {
					if (input.equals(relevant.getChoiceOptions()[j].getName())) {
						relevant.setValue(relevant.getChoiceOptions()[j]
								.getValue());
						ParamList.set(i, relevant);
					}
				}
			}
		}
		loadParamList();
		Networker.sendPkt(this);
	}

	public final String[] getParameterNames() {
		String[] parameterNames = new String[ParamList.size()];
		for (int i = 0; i < ParamList.size(); i++)
			parameterNames[i] = ParamList.get(i).getName();

		return parameterNames;
	}

	/**Probably the problem **/
	private final void loadParamList() {
		byte[] CmdParamBuffer;
		int CmdParamBufIndx = 0;

		if (!ParamList.isEmpty()) {
			ParamByteLen = 0;
			for (int i = 0; i < ParamList.size(); i++) {
				ParamByteLen += ParamList.get(i).getNumBytes();
			}
			CmdParamBuffer = new byte[ParamByteLen];

			for (int i = 0; i < ParamList.size(); i++) {
				byte[] ParamBuffer = ParamList.get(i).getByteArray();

				for (int j = 0; j < ParamList.get(i).getNumBytes(); j++) 
					CmdParamBuffer[CmdParamBufIndx++] = ParamBuffer[j];
			}
			CmdPkt.LoadData(CmdParamBuffer, ParamByteLen);
		} else {
		}
		CmdPkt.ComputeChecksum();
	}

	public final CcsdsCmdPkt getCcsdsPkt() {
		return CmdPkt;
	}

	public final String getName() {
		return Name;
	}

	public final void addParam(CmdParam Param) {
		ParamList.add(Param);
	}

	public final ArrayList<CmdParam> getParamList() {
		return ParamList;
	}
}