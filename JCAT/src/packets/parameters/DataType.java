package packets.parameters;

import utilities.EndianCorrector;


public enum DataType {
	int8("int8", 1, false), uint8Integer("uint8", 1, false), uint8String("uint8", 1, true), uint16Integer(
			"uint16", 2, false), uint16String("uint16", 2, true), uint32Integer("uint32", 4, false), uint32String("uint32", 4, true), int16("int16", 2,
			false), int32("int32", 4, false), Char("char", 1, true), bool(
			"boolean", 1, false), undef("UNDEFINED", 1, true);

	public static final DataType[] dataTypes = new DataType[] {
			DataType.int8, DataType.uint8Integer, DataType.uint8String, DataType.uint16Integer,
			DataType.uint16String, DataType.uint32Integer, DataType.uint32String, DataType.int16, DataType.int32, DataType.Char,
			DataType.bool };

	private final String type;
	private final int typeBytes;
	private final boolean isString;
	private int stringScalar = 1;

	private DataType(String type, int bytes, boolean isString) {
		this.type = type;
		this.typeBytes = bytes;
		this.isString = isString;
	}

	final String getName() {
		return type;
	}

	public final int getBytes() {
		if (isString)
			return typeBytes * stringScalar;
		
		return typeBytes;
	}
	
	private final void setStringScalar(int stringScalar)
	{
		this.stringScalar = stringScalar;
	}
	
	public final CmdParam getCmdParam(String name, boolean isInputParam,
			ChoiceOption[] choiceArray) {
		if (typeBytes == 0) //if Undef.
			return new CmdStrParam(name, isInputParam, choiceArray, 1);
		
		if (isString) {
			return new CmdStrParam(name, isInputParam, choiceArray,
					getBytes());
		}

		return new CmdIntParam(name, isInputParam, choiceArray,
				getBytes());
	}

	public static DataType getDataType(String type, String primitive, String bytes) {
			boolean isString = false;
			/* TODO returns true if first letter is 's'. More elegant solution? */
			if (primitive.length() > 0)
				if (primitive.substring(0,1).equalsIgnoreCase("s"))
					isString = true;
		
		DataType dataType = DataType.undef;
		for (DataType d : dataTypes)
			if (d.getName().equalsIgnoreCase(type))
				if ((d.isString && isString) || (!d.isString && !isString))	{
					dataType = d;
		}

		if (dataType == DataType.undef)
			System.out.println("DATATYPE: THE PRIMITIVE " + primitive + " AND TYPE " + type + " IS NOT A REGISTERED DATATYPE");
		
		if (bytes.equalsIgnoreCase("TBD") || bytes.equalsIgnoreCase("DNE"))
			bytes = "1";
		if (dataType.isString) dataType.setStringScalar(Integer.parseInt(bytes));

		return dataType;
	}

	/**
	 * replaces getTlmStrArrayuint32 and the like.
	 */
	public String getTlmStrArray(byte[] RawData, int RawIndex) {
		
		byte[] data = new byte[getBytes()];
		for (int i = 0; i < data.length; i++)
			data[i] = RawData[RawIndex + i];
		
		if (data.length > 0) {
			EndianCorrector.fixParameterIn(data);
		}
		
		switch (data.length) {
		case (1): {
			return String.valueOf((0x00FF & data[0]));
		}
		
		case (2): {
			long longInt = 0x0000FFFF & (data[0] | (data[1] << 8));
			return String.valueOf(longInt);
		}

		case (4): {
			long longIntA = (data[0] | ((data[1] << 8)));
			long longIntB = (data[2] | ((data[3] << 8)));
			long longInt = (longIntA | longIntB << 16);

			return String.valueOf(longInt);
		}

		default:
			return "Invalid datatype";
		}
	}
}