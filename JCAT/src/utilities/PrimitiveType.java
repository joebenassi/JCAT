package utilities;

import packets.cmd.CmdIntParam;
import packets.cmd.CmdParam;
import packets.cmd.CmdStrParam;

public enum PrimitiveType {
	String(new String[] { "str", "string" }), integer(new String[] { "int",
			"integer" }), TBD(new String[] { "tbd" });

	public static final PrimitiveType[] primitiveTypes = new PrimitiveType[] {
			PrimitiveType.String, PrimitiveType.integer, PrimitiveType.TBD };

	private final String[] nameAlterations;

	private PrimitiveType(String[] nameAlterations) {
		this.nameAlterations = nameAlterations;
	}

	public final boolean hasName(String s) {
		for (String name : nameAlterations)
			if (name.equalsIgnoreCase(s))
				return true;

		return false;
	}

	private final CmdParam getCmdParam(String name, boolean isInputParam,
			String[] options, String defValue, int totalBytes) {
		if (hasName("str"))
			return new CmdStrParam(name, isInputParam, options, defValue,
					totalBytes);
		else if (hasName("int"))
			return new CmdIntParam(name, isInputParam, options, defValue,
					totalBytes);
		else if (hasName("tbd"))
			return new CmdStrParam(name, isInputParam, options, defValue,
					totalBytes);

		return null;
	}

	public static final PrimitiveType getPrimitiveType(String primitive) {
		for (PrimitiveType type : primitiveTypes)
			if (type.hasName(primitive))
				return type;

		return PrimitiveType.TBD;
	}

	public final static CmdParam getCmdParam(String primitive, String name,
			boolean isInputParam, String[] options, String defValue,
			int totalBytes) {
		return getPrimitiveType(primitive).getCmdParam(name, isInputParam,
				options, defValue, totalBytes);
	}
}
