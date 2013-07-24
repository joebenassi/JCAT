package packets.parameters;

public class ChoiceOption {
	private final String name;
	private final String value;
	
	public ChoiceOption(String name, String value)
	{
		this.name = name;
		this.value = value;
	}
	
	public final String getName()
	{
		return name;
	}
	
	public final String getValue()
	{
		return value;
	}
	
	public static final String[] getNames(ChoiceOption[] choiceOptions)
	{
		String[] names = new String[choiceOptions.length];
		
		for (int i = 0; i < names.length; i++)
			names[i] = choiceOptions[i].getName();
		
		return names;
	}
}
