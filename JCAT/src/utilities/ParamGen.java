package utilities;

import packets.cmd.CmdParam;

public class ParamGen {
	public static final CmdParam getCmdParam(String name, String type,
			String primitive, String bytes, boolean isInputParam, String[] options,
			String defValue) {
		

		return PrimitiveType.getCmdParam(type, name, isInputParam, options,
				defValue, getTotalBytes(bytes, type));
	}
	
	private static final int getTotalBytes(String bytes, String type) {
		int bytesInt = 21;// 24;//32
		try {
			bytesInt = Integer.parseInt(bytes);
			System.out.println("BYTES: " + bytes);
		} catch (Throwable e) {
		}

		return DataType.getDataType(type).getBytes() * bytesInt;
	}
}
