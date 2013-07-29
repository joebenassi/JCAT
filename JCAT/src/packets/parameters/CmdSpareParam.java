package packets.parameters;

public class CmdSpareParam extends CmdParam {
	public CmdSpareParam(String type) {
		super("", true, new ChoiceOption[0], ParamType.SPARE, DataType
				.getDataType(type, "int", "1").getBytes());
	}

	@Override
	protected void loadByteArray() {
		switch (ByteArray.length) {

		case 1:
			System.out
					.println("CmdSpareParam::loadByteArray - 1 byte parameter");
			ByteArray[0] = 0;
			break;

		case 2:
			System.out
					.println("CmdSpareParam::loadByteArray - 2 byte parameter");
			ByteArray[0] = 0;
			ByteArray[1] = 0;
			break;

		case 4:
			System.out
					.println("CmdSpareParam::loadByteArray - 4 byte parameter");
			ByteArray[0] = 0;
			ByteArray[1] = 0;
			ByteArray[2] = 0;
			ByteArray[3] = 0;
			break;

		default:
			System.out.println("CMDSPAREPARAM: Unsupported datasize");
		}
	}
}
