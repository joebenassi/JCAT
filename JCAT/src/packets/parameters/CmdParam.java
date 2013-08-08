package packets.parameters;

import utilities.EndianCorrector;

/**
 * NOT DOCUMENTED. Design Notes: 1. This class is intended to be used as an
 * interface to a class providing a user interface such as a GUI or a script.
 * Therefore the user interface will be a string. On the 'back end' is a byte
 * array to the communications to the CFS. Since these interface types are fixed
 * an abstract base class can provide methods for the interfaces and the
 * concrete subclasses would provide the conversion implementation for each data
 * type. 2. When I hard coded some defaults the string constructor seemed odd
 * but if this project expands to use a text based database then it would also
 * use text for the constructor so the design may be okay. 3. Generics didn't
 * fit the problem being solved because they are typically used in containers
 * when the different types are being passed across a class interface.
 * 
 * @author Joe Benassi
 * @author David McComas
 */
public class CmdParam {
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

	public CmdParam(ParamType Type, String Name, boolean isInputParam,
			ChoiceOption[] choiceOptions, int NumBytes) {
		this.isInputParam = isInputParam;
		this.choiceOptions = choiceOptions;
		this.Name = Name;
		this.Type = Type;
		this.NumBytes = NumBytes;
		ByteArray = new byte[NumBytes];
	}

	public static final CmdParam getSpare(String type) {
		return new CmdParam(ParamType.SPARE, "", true, new ChoiceOption[0], DataType.getDataType(type, "int", "1").getBytes());
	}
	
	public final String getName() {
		return Name;
	}

	public final ParamType getType() {
		return Type;
	}

	/**
	 * TODO Think about adding Value also stored as an integer with a non string
	 * constructor TODO More robust NumByte error handling TODO Add radix
	 * support
	 */
	private final void loadIntByteArray() {
		switch (ByteArray.length) {

		case 1:
			ByteArray[0] = Integer.valueOf(Value).byteValue();
			System.out.println("ByteArray[0] = " + ByteArray[0]);
			break;

		case 2:
			int Temp2 = Integer.valueOf(Value);
			ByteArray[0] = (byte) (Temp2 & 0xFF);
			ByteArray[1] = (byte) ((Temp2 & 0xFF00) >> 8);
			System.out.println("ByteArray[0] = " + ByteArray[0]);
			System.out.println("ByteArray[1] = " + ByteArray[1]);
			break;

		case 4:
			int Temp4 = Integer.valueOf(Value);
			ByteArray[0] = (byte) (Temp4 & 0x00FF);
			ByteArray[1] = (byte) ((Temp4 & 0x0000FF00) >> 8);
			ByteArray[2] = (byte) ((Temp4 & 0x00FF0000) >> 16);
			ByteArray[3] = (byte) ((Temp4 & 0xFF000000) >> 24);
			System.out.println("ByteArray[0] = " + ByteArray[0]);
			System.out.println("ByteArray[1] = " + ByteArray[1]);
			System.out.println("ByteArray[2] = " + ByteArray[2]);
			System.out.println("ByteArray[3] = " + ByteArray[3]);
			break;
		}
		EndianCorrector.fixParameterOut(ByteArray);
	}

	/**
	 * TODO Add error protection (null & invalid length)
	 */
	private final void loadStringByteArray() {
		/* TODO add error protection (null & invalid len. */
		for (int i = 0; i < ByteArray.length; i++) {
			if (i < Value.length()) {
				ByteArray[i] = (byte) (Value.codePointAt(i) & 0x0FF);
				/* Unicode equals ASCII */
			}

			else
				ByteArray[i] = 0;
		}
	}

	/**
	 * nothing
	 */
	private final void loadSpareByteArray() {
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

	/*
	 * Each subclass type must provide the conversions for the particular
	 * parameter type
	 */
	public void loadByteArray() {
		if (Type == ParamType.INT)
			loadIntByteArray();

		else if (Type == ParamType.STR)
			loadStringByteArray();

		else if (Type == ParamType.SPARE)
			loadSpareByteArray();

		; // Load byte array using current
	}

	// Value. Intent is an internal
	// helper function

	public final byte[] getByteArray() {
		return ByteArray;
	}

	public final void setValue(String value) {
		Value = value;
		loadByteArray();
	}

	public final int getNumBytes() {
		return NumBytes;
	}

	public final boolean isInputParam() {
		return isInputParam;
	}

	public final ChoiceOption[] getChoiceOptions() {
		return choiceOptions;
	}
}
