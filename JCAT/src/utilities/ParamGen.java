package utilities;

import packets.parameters.ChoiceOption;
import packets.parameters.CmdParam;
import packets.parameters.CmdSpareParam;
import packets.parameters.DataType;

public class ParamGen {
	public static final CmdParam getCmdParam(String name, String type,
			String primitive, String bytes, boolean isInputParam,
			ChoiceOption[] choiceArray) {

		return DataType.getDataType(type, primitive, bytes).getCmdParam(name,
				isInputParam, choiceArray);
	}

	public static CmdParam getSpareParam(String type) {
		return new CmdSpareParam(type);
	}
}
