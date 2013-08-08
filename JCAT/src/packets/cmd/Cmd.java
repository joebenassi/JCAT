package packets.cmd;

import java.util.ArrayList;

import packets.ccsds.CcsdsCmdPkt;
import packets.parameters.CmdParam;
import network.PktWriter;

/**
 * FULLY DOCUMENTED. There is an instance of this class for each command that
 * any App can execute, as defined in each's App Profile. An instance of this
 * class has a CcsdsCmdPkt, which it updates and sends when parameters are input
 * by the user.
 * 
 * @author David McComas
 */
public final class Cmd {
	private final String Name;
	private ArrayList<CmdParam> ParamList = new ArrayList<CmdParam>();
	private final int ParamByteLen;
	private CcsdsCmdPkt CmdPkt;

	/**
	 * Creates a Cmd with the input name, cmdmid, command code, and parameter
	 * byte length. Creates a CcsdsPkt for it.
	 * 
	 * @param Name
	 *            The name of this Cmd, to appear in the GUI.
	 * @param MsgId
	 *            The command message ID of it's parent App.
	 * @param FuncCode
	 *            The command code of this command. The index of the command in
	 *            the App's list of commands.
	 * @param DataLen
	 *            The amount of bytes the parameters occupy.
	 */
	public Cmd(String Name, int MsgId, int FuncCode, int DataLen) {
		this.Name = Name;
		ParamByteLen = DataLen;

		CmdPkt = new CcsdsCmdPkt(MsgId, DataLen, FuncCode);
		CmdPkt.ComputeChecksum();
	}

	/**
	 * Adds information from the input parameter value Strings to the CcsdsPkt,
	 * and sends it.
	 * 
	 * @param paramValues
	 *            The values that are input by the user for each parameter.
	 */
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

	/**
	 * Returns a String array containing all the parameter names for this
	 * command.
	 * 
	 * @return The parameter names in correct order.
	 */
	public final String[] getParameterNames() {
		String[] parameterNames = new String[ParamList.size()];
		for (int i = 0; i < ParamList.size(); i++)
			parameterNames[i] = ParamList.get(i).getName();

		return parameterNames;
	}

	/**
	 * Uses the values of each parameter to pack them and load them into the
	 * CcsdsCmdPkt.
	 */
	private final void loadParamList() {
		byte[] CmdParamBuffer = new byte[ParamByteLen];
		int CmdParamBufIndx = 0;

		for (int i = 0; i < ParamList.size(); i++) {
			byte[] ParamBuffer = ParamList.get(i).getByteArray();

			for (int j = 0; j < ParamList.get(i).getNumBytes(); j++)
				CmdParamBuffer[CmdParamBufIndx++] = ParamBuffer[j];
		}
		CmdPkt.LoadData(CmdParamBuffer);
	}

	/**
	 * Returns the CcsdsCmdPkt for this Cmd.
	 * 
	 * @return The CcsdsPkt containing the current packet information for this
	 *         Cmd.
	 */
	public final CcsdsCmdPkt getCcsdsPkt() {
		return CmdPkt;
	}

	/**
	 * Returns the name of this Cmd, to display in the GUI.
	 * 
	 * @return The name of this Cmd.
	 */
	public final String getName() {
		return Name;
	}

	/**
	 * Tells the Cmd to expect a parameter later with particular attributes,
	 * which are enclosed in the input CmdParam. This is NOT called every time a
	 * command is sent.
	 * 
	 * @param Param
	 *            The parameter that is required for this Cmd to be sent.
	 */
	public final void addParam(CmdParam Param) {
		ParamList.add(Param);
	}

	/**
	 * Returns an arraylist of parameters for this command.
	 * 
	 * @return All the parameters that this Cmd requires.
	 */
	public final ArrayList<CmdParam> getParamList() {
		return ParamList;
	}
}