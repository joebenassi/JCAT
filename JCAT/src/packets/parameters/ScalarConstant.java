package packets.parameters;

import java.util.ArrayList;

public class ScalarConstant {
	private static ArrayList<String> names = new ArrayList<String>();
	private static ArrayList<String> values = new ArrayList<String>();

	public static void addConstant(String name, String value) {
		names.add(name);
		values.add(value);
	}

	public static String getValue(String name) {
		for (int i = 0; i < names.size(); i++) {
			if (name.equalsIgnoreCase(names.get(i)))
				return values.get(i);
		}
		return "1";
	}

	public static final boolean hasConstant(String name) {
		for (int i = 0; i < names.size(); i++) {
			if (names.get(i).equalsIgnoreCase(name))
				return true;
		}
		return false;
	}
}
