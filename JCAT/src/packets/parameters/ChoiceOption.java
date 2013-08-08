package packets.parameters;

/**
 * FULLY DOCUMENTED. This class represents one choice that the user could make
 * at the 'input parameters' screen of a 'send command' window. It has a name
 * and a value. If the name is chosen in the GUI, the value will be encoded in
 * the command packet.
 * 
 * @author Joe Benassi
 */
public final class ChoiceOption {
	private final String name;
	private final String value;

	/**
	 * Creates a ChoiceOption with the input name and value.
	 * 
	 * @param name
	 *            The name of this ChoiceOption as it would display in the GUI.
	 * 
	 * @param value
	 *            The value that this ChoiceOption would output if this
	 *            ChoiceOption was chosen by the user.
	 */
	public ChoiceOption(String name, String value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * Returns the name of this option, as it would appear in the GUI.
	 * 
	 * @return The name of this ChoiceOption.
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Returns the value of this option; the value that would be encoded in a
	 * command packet if this was chosen.
	 * 
	 * @return The value of this option.
	 */
	public final String getValue() {
		return value;
	}

	/**
	 * Returns a String containing all the names of the input ChoiceOptions.
	 * 
	 * @param choiceOptions
	 *            The ChoiceOptions desired to know the names of.
	 * 
	 * @return The names of the options.
	 */
	public static final String[] getNames(ChoiceOption[] choiceOptions) {
		String[] names = new String[choiceOptions.length];

		for (int i = 0; i < names.length; i++)
			names[i] = choiceOptions[i].getName();

		return names;
	}
}
