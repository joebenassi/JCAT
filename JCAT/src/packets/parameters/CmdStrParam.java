package packets.parameters;

public class CmdStrParam extends CmdParam {
	public CmdStrParam(String Name, boolean isInputParam,
			ChoiceOption[] choiceOptions, int NumBytes) {
		super(Name, isInputParam, choiceOptions, ParamType.STR, NumBytes);
	}

	@Override
	protected void loadByteArray() {
		/* TODO add error protection (null & invalid len. */
		for (int i = 0; i < ByteArray.length; i++) {
			if (i < Value.length()) {
				ByteArray[i] = (byte) (Value.codePointAt(i) & 0x0FF); 
				/* Unicode equals ASCII */
			}
		
			else ByteArray[i] = 0;
		}
	}
	
}
