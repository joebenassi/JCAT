package packets.parameters;

import java.util.ArrayList;

/**
 * FULLY DOCUMENTED. This stores the values parsed from a Constant Definition
 * file so that App Profiles' parameters can use a defined constant to allot
 * more storage for the parameter.
 * 
 * @author Joe Benassi
 */
public final class ScalarConstant {
	private static ArrayList<String> names = new ArrayList<String>();
	private static ArrayList<String> values = new ArrayList<String>();

	/**
	 * Adds a constant. This is called every time a CONST is defined in a
	 * Constant Definition XML. After the CONST with the input name and value is
	 * entered, it stores it for later use if it is mentioned in an App Profile.
	 * 
	 * @param name
	 *            The name of this CONST.
	 * @param value
	 *            The value of this CONST. This is multiplied by the byte value
	 *            of a parameter's type (for example, "uint8" = 1 and "int16" =
	 *            2) to determine the total amount of bytes allocated for it in
	 *            its CCSDS packet.
	 */
	public static void addConstant(String name, String value) {
		names.add(name);
		values.add(value);
	}

	/**
	 * Returns the value of the constant with the input name.
	 * 
	 * @param name
	 *            The name of the constant.
	 * @return The value tied to the input constant name.
	 */
	public static String getValue(String name) {
		for (int i = 0; i < names.size(); i++) {
			if (name.equalsIgnoreCase(names.get(i)))
				return values.get(i);
		}
		return "1";
	}

	/**
	 * Returns whether or not there exists a constant with the given name.
	 * 
	 * @param name
	 *            The name of the constant in question.
	 * @return If it has been defined.
	 */
	public static final boolean hasConstant(String name) {
		for (int i = 0; i < names.size(); i++) {
			if (names.get(i).equalsIgnoreCase(name))
				return true;
		}
		return false;
	}
}
