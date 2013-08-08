package packets.cmd;

import java.util.ArrayList;

import packets.ccsds.CcsdsCmdPkt;
import packets.parameters.CmdParam;
import network.Networker;
import network.PktWriter;

/**
 * NOT DOCUMENTED.
 * 
 * @author David McComas
 */
public class Cmd {
	private final String Name;
	private ArrayList<CmdParam> ParamList = new ArrayList<CmdParam>();
	private final int ParamByteLen;
	private CcsdsCmdPkt CmdPkt;

	public Cmd(String Name, int MsgId, int FuncCode, int DataLen) {
		this.Name = Name;
		ParamByteLen = DataLen;

		CmdPkt = new CcsdsCmdPkt(MsgId, DataLen, FuncCode);
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
		if (!ParamList.isEmpty())
			loadParamList();

		CmdPkt.ComputeChecksum();
		PktWriter.sendPacket(this);
	}

	public final String[] getParameterNames() {
		String[] parameterNames = new String[ParamList.size()];
		for (int i = 0; i < ParamList.size(); i++)
			parameterNames[i] = ParamList.get(i).getName();

		return parameterNames;
	}

	/** Probably the problem **/
	private final void loadParamList() {
		byte[] CmdParamBuffer;
		int CmdParamBufIndx = 0;

		CmdParamBuffer = new byte[ParamByteLen];

		for (int i = 0; i < ParamList.size(); i++) {
			byte[] ParamBuffer = ParamList.get(i).getByteArray();

			for (int j = 0; j < ParamList.get(i).getNumBytes(); j++)
				CmdParamBuffer[CmdParamBufIndx++] = ParamBuffer[j];
		}
		CmdPkt.LoadData(CmdParamBuffer);
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