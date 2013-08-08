package packets.parameters;

import utilities.EndianCorrector;

/**
 * NOT DOCUMENTED. This contains a 'back end', which is a byte array to the
 * communications to the CFS.
 * 
 * @author Joe Benassi
 * @author David McComas
 */
public final class CmdParam {
	public enum ParamType {
		INT, STR, SPARE
	};

	private final boolean isInputParam;
	private final ChoiceOption[] choiceOptions;
	private final String Name;
	private final ParamType Type;
	protected String Value;
	private final int NumBytes;
	protected byte[] ByteArray;

	/**
	 * Constructor: creates a CmdParam with the input attributes.
	 * 
	 * @param Type
	 *            The parameter type (int, string, spare, or spare).
	 * @param Name
	 *            The name to display in the GUI.
	 * @param isInputParam
	 *            If the user has to manually type in the parameter.
	 * @param choiceOptions
	 *            If this is not an InputParameter, the different choices
	 *            available to the user.
	 * @param NumBytes
	 *            The amount of bytes that all the parameters will occupy in the
	 *            command packet.
	 */
	public CmdParam(ParamType Type, String Name, boolean isInputParam,
			ChoiceOption[] choiceOptions, int NumBytes) {
		this.isInputParam = isInputParam;
		this.choiceOptions = choiceOptions;
		this.Name = Name;
		this.Type = Type;
		this.NumBytes = NumBytes;
		ByteArray = new byte[NumBytes];
	}

	/**
	 * Returns a CmdParam that is invisible to the GUI and contributes only
	 * values of zero to the command packet.
	 * 
	 * @param type
	 *            The type. For example, uint8, int16. This is only used to
	 *            determine the size of the spare.
	 * @return A CmdPaam that is a spare.
	 */
	public static final CmdParam getSpare(String type) {
		return new CmdParam(ParamType.SPARE, "", true, new ChoiceOption[0],
				DataType.getDataType(type, "int", "1").getBytes());
	}

	/**
	 * Returns the String representing this perameter's name.
	 * 
	 * @return This parameter's name.
	 */
	public final String getName() {
		return Name;
	}

	/**
	 * Returns the type of this parameter.
	 * 
	 * @return the ParamType of this.
	 */
	public final ParamType getType() {
		return Type;
	}

	/**
	 * Updates the byte array by using this parameter's value and encoding it as
	 * an integer value.
	 */
	private final void loadIntByteArray() {
		int index = 255;
		final int temp = Integer.valueOf(Value);

		for (int i = 0; i < ByteArray.length; i++) {
			ByteArray[i] = (byte) ((temp & index) >> (8 * i));
			if (index > Integer.MAX_VALUE / 256)
				i = ByteArray.length;
			else
				index *= 256;
		}
		EndianCorrector.fixParameterOut(ByteArray);
	}

	/**
	 * Updates the byte array by using this parameter's value and encoding it as
	 * String information.
	 */
	private final void loadStringByteArray() {
		if (Value == null)
			return;
		for (int i = 0; i < ByteArray.length; i++) {
			ByteArray[i] = 0;

			if (i < Value.length())
				ByteArray[i] = (byte) (Value.codePointAt(i) & 0x0FF);
		}
	}

	/**
	 * Updates the byte array for this parameter by setting all values within
	 * ByteArray to zero.
	 */
	private final void loadSpareByteArray() {
		for (byte b : ByteArray)
			b = 0;
	}

	/**
	 * Updates the byte array for this parameter based on the value more
	 * recently assigned to it. Uses the knowledge of this being an integer,
	 * string, or spare parameter to pack and adjust for endianness accordingly.
	 */
	public final void loadByteArray() {
		if (Type == ParamType.INT)
			loadIntByteArray();

		else if (Type == ParamType.STR)
			loadStringByteArray();

		else if (Type == ParamType.SPARE)
			loadSpareByteArray();
	}

	/**
	 * Returns the byte array. Returns the array that will be pasted in the
	 * final command packet. In the packet, it will be together and in its
	 * original order.
	 * 
	 * @return The byte array representing the value of this parameter.
	 */
	public final byte[] getByteArray() {
		return ByteArray;
	}

	/**
	 * Sets the value of this parameter to the input value and updates the byte
	 * array.
	 * 
	 * @param value
	 *            The value to set as this parameter's value.
	 */
	public final void setValue(String value) {
		Value = value;
		loadByteArray();
	}

	/**
	 * Returns the number of bytes that this command parameter occupies in the
	 * final packet.
	 * 
	 * @return The byte length of this paramter.
	 */
	public final int getNumBytes() {
		return NumBytes;
	}

	/**
	 * Returns true if this parameter is an input parameter. Otherwise, it is a
	 * choiceparameter, and returns false.
	 * 
	 * @return Whether or not this is an input parameter.
	 */
	public final boolean isInputParam() {
		return isInputParam;
	}

	/**
	 * Returns the ChoiceOptions for this command parameter.
	 * 
	 * @return The ChoiceOptions available for this.
	 */
	public final ChoiceOption[] getChoiceOptions() {
		return choiceOptions;
	}
}
