package packets.parameters;

import utilities.EndianCorrector;

/**
 * FULLY DOCUMENTED. This is used to represent the various types of cmd and tlm
 * parameters. This is used behind-the-scenes to read and write CCSDS packets.
 * 
 * @author Joe Benassi
 */
public enum DataType {
	uint8Integer("uint8", 1, false), uint8String("uint8", 1, true), uint16Integer(
			"uint16", 2, false), uint16String("uint16", 2, true), uint32Integer(
			"uint32", 4, false), uint32String("uint32", 4, true), int8Integer(
			"int8", 1, false), int8String("int8", 1, true), int16Integer(
			"int16", 2, false), int16String("int16", 2, true), int32Integer(
			"int32", 4, false), int32String("int32", 4, true), Char("char", 1,
			true), undef("UNDEFINED", 1, true);

	/**
	 * All data types supported for a parameter. Each is either an integer,
	 * unsigned integer, or char. Each is either 8, 16, or 32 bit. Each is
	 * either a string or integer. This is used for byte packing or parsing.
	 */
	private static final DataType[] dataTypes = new DataType[] {
			DataType.uint8Integer, DataType.uint8String,
			DataType.uint16Integer, DataType.uint16String,
			DataType.uint32Integer, DataType.uint32String,
			DataType.int8Integer, DataType.int8String, DataType.int16Integer,
			DataType.int16String, DataType.int32Integer, DataType.int32String,
			DataType.Char };

	private final String type;
	private final int typeBytes;
	private final boolean isString;

	/**
	 * This is used when a parameter is a string of length 'stringScalar'. This
	 * stringScalar determines the amount of bytes allocated to a paramter, and
	 * is defined in an App Profile.
	 */
	private int stringScalar = 1;

	/**
	 * Constructs a DataType with a name, an initial amount of bytes, and if it
	 * is a string or not.
	 * 
	 * @param type
	 *            The name to represent this. For example "uint8".
	 * @param bytes
	 *            The size of this DataType for an integer. For a String, this
	 *            value is multiplied by its StringScalar defined in its App
	 *            Profile.
	 * @param isString
	 *            If this is a String.
	 */
	private DataType(String type, int bytes, boolean isString) {
		this.type = type;
		this.typeBytes = bytes;
		this.isString = isString;
	}

	/**
	 * Returns the name of this DataType.
	 * 
	 * @return the name of this.
	 */
	private final String getName() {
		return type;
	}

	/**
	 * Returns the amount of bytes allocated to this DataType.
	 * 
	 * @return The size of this DataType.
	 */
	public final int getBytes() {
		if (isString)
			return typeBytes * stringScalar;

		return typeBytes;
	}

	/**
	 * Sets the string scalar. This is used to allocate extra bytes for a String
	 * that represents an array of this DataType. For example, the input value
	 * could be 20 (OS_MAX_PATH_LEN) if appropriate.
	 * 
	 * @param stringScalar
	 *            The string scalar.
	 */
	private final void setStringScalar(int stringScalar) {
		this.stringScalar = stringScalar;
	}

	/**
	 * Returns a CmdParam with the input properties of name, choice options, and
	 * if it is an inputparam.
	 * 
	 * @param name
	 *            The name of the parameter.
	 * @param isInputParam
	 *            If the desired CmdParam is an inputparameter.
	 * @param choiceArray
	 *            An array of ChoiceOptions. Can be size zero if this is an
	 *            inputparam.
	 * @return A CmdParam with the input properties.
	 */
	public final CmdParam getCmdParam(String name, boolean isInputParam,
			ChoiceOption[] choiceArray) {
		if (isString)
			return new CmdParam(CmdParam.ParamType.STR, name, isInputParam,
					choiceArray, getBytes());

		return new CmdParam(CmdParam.ParamType.INT, name, isInputParam,
				choiceArray, getBytes());
	}

	/**
	 * Returns a DataType who has the desired type, primitive, and bytes.
	 * 
	 * @param type
	 *            The type of this DataType. For example, "uint8".
	 * @param primitive
	 *            The primitive of this DataType. If this starts with an "s", it
	 *            is considered a string.
	 * @param bytes
	 *            The amount of bytes to allocate to this DataType if this is an
	 *            integer (if this is DataType represents a String, this value
	 *            will be multiplied by its StringScalar to determine its byte
	 *            length).
	 * @return The DataType with the input properties.
	 */
	public static final DataType getDataType(String type, String primitive,
			String bytes) {
		boolean isString = false;
		/* TODO returns true if first letter is 's'. More elegant solution? */
		if (primitive.length() > 0)
			if (primitive.substring(0, 1).equalsIgnoreCase("s"))
				isString = true;

		DataType dataType = DataType.undef;
		for (DataType d : dataTypes)
			if (d.getName().equalsIgnoreCase(type))
				if ((d.isString && isString) || (!d.isString && !isString)) {
					dataType = d;
				}

		if (dataType == DataType.undef)
			System.out.println("DATATYPE: THE PRIMITIVE " + primitive
					+ " AND TYPE " + type + " IS NOT A REGISTERED DATATYPE");

		if (bytes.equalsIgnoreCase("TBD") || bytes.equalsIgnoreCase("DNE"))
			bytes = "1";
		if (dataType.isString)
			dataType.setStringScalar(Integer.parseInt(bytes));

		return dataType;
	}

	/**
	 * MAY NOT WORK WITH DATATYPES OF TYPE STRING. Used to parse telemetry CCSDS
	 * packets. Parses the data array between the indices (RawIndex) and
	 * (RawIndex + getBytes()) and converts it to a meaningful String to be
	 * displayed in the GUI.
	 * 
	 * @param RawData
	 * @param RawIndex
	 * @return A String representing the value of the telemetry.
	 */
	public final String getTlmStrArray(byte[] RawData, int RawIndex) {

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
		case (0): {
			return String.valueOf((0x00FF & RawData[RawIndex]));
		}
		default:
			return "Invalid datatype";
		}
	}
}