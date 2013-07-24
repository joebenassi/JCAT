package packets.cmd;

import java.util.ArrayList;

import packets.ccsds.CcsdsCmdPkt;
import packets.parameters.CmdParam;
import network.Networker;

/*
 ** 
 ** @author dmccomas
 **
 */
public class CmdPkt {

	//private String AppPrefix;
	private String Name;
	private ArrayList<CmdParam> ParamList = new ArrayList<CmdParam>();
	private int ParamByteLen; // Total number of bytes in ParamList
	
	private CcsdsCmdPkt CmdPkt;

	/*
	 * * Constructor - No command parameters
	 */
	public CmdPkt(String Name, int MsgId, int FuncCode,
			int DataLen) {

		//this.AppPrefix = AppPrefix;
		this.Name = Name;
		ParamByteLen = 0; // Will be computed as parameters added

		CmdPkt = new CcsdsCmdPkt(MsgId,
				CcsdsCmdPkt.CCSDS_CMD_HDR_LEN + DataLen, FuncCode);
		CmdPkt.ComputeChecksum();
	} // End CmdPkt()

	/*
	 * * Constructor - With command parameters in a byte array
	 */
	public CmdPkt(String Name, Integer MsgId,
			Integer FuncCode, byte[] DataBuf, int DataLen) {

		//this.AppPrefix = AppPrefix;
		this.Name = Name;
		ParamByteLen = DataLen;

		CmdPkt = new CcsdsCmdPkt(MsgId,
				CcsdsCmdPkt.CCSDS_CMD_HDR_LEN + DataLen, FuncCode);
		CmdPkt.LoadData(DataBuf, DataLen);

	} // End CmdPkt()

	public void execute(String[] paramValues) 
	{
		for (int i = 0; i < paramValues.length; i++)
		{
			System.out.println(paramValues[i] + "<-- Param Value CMDPKT");
			if (ParamList.get(i).isInputParam())
				ParamList.get(i).setValue(paramValues[i]);
			else 
			{
				CmdParam relevant = ParamList.get(i);
				String input = paramValues[i];
				
				for (int j = 0; j < relevant.getChoiceOptions().length; j++)
				{
					if (input.equals(relevant.getChoiceOptions()[j].getName()))
					{
						relevant.setValue(relevant.getChoiceOptions()[j].getValue());
						ParamList.set(i, relevant);
					}
				}
				
				//String value = ParamList.get(i).getChoiceOptions()[i].getValue();

			}
			//if (!ParamList.get(i).isInputParam())
			//{
				
			//}
				//String value = paramValues[i].substring(paramValues[i].length() - 1, paramValues[i].length());
				//ParamList.get(i).setValue(value);
				//System.out.println("INPUT PARAM: " + value);
		}
		System.out.println("CmdPkt: Command Executed!");
		loadParamList();
		
		//Networker.getNetworker();
		Networker.sendPkt(this);
	}

	public final String[] getParameterNames() {
		String[] parameterNames = new String[ParamList.size()];
		for (int i = 0; i < ParamList.size(); i++) {
			parameterNames[i] = ParamList.get(i).getName();
		}

		return parameterNames;
	}

	/*
	 * Load command parameters from a byte array
	 */
	//public CcsdsCmdPkt LoadParams(byte[] DataBuffer, int DataLen) {
//
	//	if (DataLen > 0) {
	////		CmdPkt.LoadData(DataBuffer, DataLen);
	//	}

	//	CmdPkt.ComputeChecksum();

	//	return CmdPkt;

	//} // LoadParams()

	/*
	 * Replace CmdParam array list and load the data
	 */

	/*public CcsdsCmdPkt setParamList(ArrayList<CmdParam> paramList) {

		ParamList = paramList;

		return CmdPkt = loadParamList();

	} // setParamList()

	/*
	 * Load parameter data from the current parameter list
	 */

	public void loadParamList() {

		byte[] CmdParamBuffer;
		int CmdParamBufIndx = 0;
		
		if (!ParamList.isEmpty()) {
		
			ParamByteLen = 0;
			for (int i = 0; i < ParamList.size(); i++) {
				ParamByteLen += ParamList.get(i).getNumBytes();
			}
			
			System.out.println("CmdPkt: " + Name + " has " + ParamByteLen + " Bytes of Parameters");
			CmdParamBuffer = new byte[ParamByteLen];
			System.out
					.println("CmdPkt::loadParamList() - Creating parameter list with byte length "
							+ ParamByteLen);

			for (int i = 0; i < ParamList.size(); i++) {
				byte[] ParamBuffer = ParamList.get(i).getByteArray();

				for (int j = 0; j < ParamList.get(i).getNumBytes(); j++) {

					CmdParamBuffer[CmdParamBufIndx++] = ParamBuffer[j];
					System.out.println("CmdParamBuffer["
							+ (CmdParamBufIndx - 1) + "] = "
							+ CmdParamBuffer[CmdParamBufIndx - 1]);

				} // End by

			} // End parameter list loop

			CmdPkt.LoadData(CmdParamBuffer, ParamByteLen);

		} // ParamList not empty
		else {
			// Currently default to a null buffer
		}

		CmdPkt.ComputeChecksum();

	} // loadParamList()

	public CcsdsCmdPkt getCcsdsPkt() {
		return CmdPkt;
	} // getCcsdsPkt()

	//public String getAppPrefix() {
	//	return AppPrefix;


	public String getName() {
		return Name;

	} // getName()

	public void addParam(CmdParam Param) {
		ParamList.add(Param);

	} // addParam()

	public ArrayList<CmdParam> getParamList() {
		return ParamList;

	} // getParamList()

	//public boolean hasParam() {
	//	return !ParamList.isEmpty();

	//} // hasParam()

	//public void setParam(int paramNum, String paramValue) {
	//	ParamList.get(paramNum).setValue(paramValue);
//
	//} // addParam()

	/*
	 * @todo - Can this be deleted? No references.
	 * 
	 * * Create a command parameter byte array that can easily be used with
	 * CCSDS Packet* methods.** CmdParam - String of comma separated* ParamBytes
	 * - Number of bytes in each parameter* ParamCnt - Number of parameters
	 */
	byte[] createCmdByteArray(String CmdParam, int ParamBytes[], int ParamCnt) {
		System.out.println("CmdPkt: Param Amt: " + ParamCnt);
		System.out.println("CmdPkt: Param Bytes: " + ParamBytes[0]);
		
		byte[] DataBuffer = null;

		String[] Param = CmdParam.split(",");

		if (Param.length == ParamCnt) {
			int i, DataLen = 0, DataIndex = 0;

			for (i = 0; i < ParamCnt; i++) {
				DataLen += ParamBytes[i];
				System.out.println("Param[" + i + "]=" + Param[i]);
			}

			DataBuffer = new byte[DataLen];

			for (i = 0; i < ParamCnt; i++) {
				if (ParamBytes[i] == 1) {
					DataBuffer[DataIndex++] = new Integer(
							Integer.parseInt(Param[i])).byteValue();
					System.out.println("DataBuffer" + (DataIndex - 1) + "]="
							+ DataBuffer[DataIndex - 1]);
				} else if (ParamBytes[i] == 2) {
					int Temp = Integer.parseInt(Param[i]);
					DataBuffer[DataIndex++] = new Integer(Temp & 0xFF)
							.byteValue();
					DataBuffer[DataIndex++] = new Integer((Temp & 0xFF00) >> 8)
							.byteValue();
					System.out.println("DataBuffer" + (DataIndex - 2) + "]="
							+ DataBuffer[DataIndex - 2]);
					System.out.println("DataBuffer" + (DataIndex - 1) + "]="
							+ DataBuffer[DataIndex - 1]);
				} else {

					// @todo - Resolve illegal parameter bytes definition

				}

			} // End parameter loop

		} else {
			// Currently default to a null buffer
		}

		return DataBuffer;

	} // LoadDataBuf()

} // End class CmdPkt