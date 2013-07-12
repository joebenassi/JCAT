package packets.cmd;

/**
 * IN DEVELOPMENT. SUBJECT TO CHANGE/REMOVAL.
 * @author Joe
 *
 */
public class ParameterDetails{
	
	private final String[] options;
	private final boolean isInputParameter;
	
	public ParameterDetails(boolean isInputParameter, String[] options)
	{
		this.isInputParameter = isInputParameter;
		this.options = options;
	}
	
	public ParameterDetails(boolean isInputParameter)
	{
		this.isInputParameter = isInputParameter;
		this.options = new String[]{};
	}
	
	public boolean isInputParameter()
	{
		return isInputParameter;
	}
	
	public final String[] getOptions()
	{
		return options;
	}
}
