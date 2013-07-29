package packets.parameters;

/*
 ** @author dmccomas
 **
 ** Design Notes:
 **   1. This class is intended to be used as an interface to a class providing a user interface such as
 **      a GUI or a script. Therefore the user interface will be a string. On the 'back end' is a byte
 **      array to the communications to the CFS. Since these interface types are fixed an abstract base
 **      class can provide methods for the interfaces and the concrete subclasses would provide the conversion
 **      implementation for each data type.
 **   2. When I hard coded some defaults the string constructor seemed odd but if this project expands to use 
 **      a text based database then it would also use text for the constructor so the design may be okay.
 **   3. Generics didn't fit the problem being solved because they are typically used in containers when the
 **      different types are being passed across a class interface.
 */
public abstract class CmdParam {

	public enum ParamType {
		UNDEF, UINT, INT, STR, SPARE
	};

	private final boolean isInputParam;
	private final ChoiceOption[] choiceOptions;
	private final String Name;
	private final ParamType Type;
	protected String Value;
	private final int NumBytes;
	protected byte[] ByteArray;

	public CmdParam(String Name, boolean isInputParam,
			ChoiceOption[] choiceOptions, ParamType Type, int NumBytes) {
		this.isInputParam = isInputParam;
		this.choiceOptions = choiceOptions;
		this.Name = Name;
		this.Type = Type;
		this.NumBytes = NumBytes;
		ByteArray = new byte[NumBytes];
	}

	public String getName() {
		return Name;
	}

	public ParamType getType() {
		return Type;
	}

	/*
	 * Each subclass type must provide the conversions for the particular
	 * parameter type
	 */
	protected abstract void loadByteArray(); // Load byte array using current
												// Value. Intent is an internal
												// helper function

	public byte[] getByteArray() {
		return ByteArray;
	}

	public void setValue(String value) {
		Value = value;
		loadByteArray();
	}

	public int getNumBytes() {
		return NumBytes;
	}

	public boolean isInputParam() {
		return isInputParam;
	}

	public ChoiceOption[] getChoiceOptions() {
		return choiceOptions;
	}
}
