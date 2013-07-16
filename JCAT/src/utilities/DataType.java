package utilities;

public enum DataType {
	uint8("uint8", 1), uint16("uint16", 2), uint32("uint32", 4), int8("int8", 1), int16(
			"int16", 2), int32("int32", 4), Char("char", 1), bool("boolean", 1), undef(
			"UNDEFINED", 0);

	public static final DataType[] dataTypes = new DataType[] { DataType.uint8,
			DataType.uint16, DataType.uint32, DataType.int8, DataType.int16,
			DataType.int32, DataType.bool, DataType.Char };

	private final String name;
	private final int bytes;

	private DataType(String name, int bytes) {
		this.name = name;
		this.bytes = bytes;
	}

	final String getName() {
		return name;
	}

	public final int getBytes() {
		return bytes;
	}

	public static DataType getDataType(String name) {
		for (DataType d : dataTypes)
			if (d.getName().equalsIgnoreCase(name))
				return d;

		return DataType.undef;
	}

	/**
	 * replaces getTlmStrArrayuint32 and the like.
	 */
	public String getTlmStrArray(byte[] RawData, int RawIndex) {
		switch (bytes) {
		case (1):
			return String.valueOf((0x00FF & RawData[RawIndex]));

		case (2): {
			long longInt = 0x0000FFFF & (RawData[RawIndex] | ((RawData[RawIndex + 1] << 8)));
			return String.valueOf(longInt);
		}

		case (4): {
			long longIntA = (RawData[RawIndex] | ((RawData[RawIndex + 1] << 8)));
			long longIntB = (RawData[RawIndex + 2] | ((RawData[RawIndex + 3] << 8)));
			long longInt = (longIntA | longIntB << 16);

			return String.valueOf(longInt);
		}

		default:
			return "ERROR IN DATATYPE";
		}
	}
}