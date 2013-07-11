package helpers;

import fsw.CmdIntParam;
import fsw.CmdParam;
import fsw.CmdStrParam;
import gui.menu.prompts.ParameterDetails;

public class CmdParamGen {
	private enum DataType {
		uint8("uint8", 1), uint16("uint16", 2), uint32("uint32", 4), int8("int8", 1), int16("int16", 2), int32("int32", 4), Char("char", 1);
		
		private static final DataType[] dataTypes = new DataType[]{DataType.uint8, DataType.uint16, DataType.uint32, DataType.int8, DataType.int16, DataType.int32, DataType.Char};
		
		private final String name;
		private final int bytes;
		
		private DataType(String name, int bytes)
		{
			this.name = name;
			this.bytes = bytes;
		}
		
		private final String getName()
		{
			return name;
		}
		
		private final int getBytes()
		{
			return bytes;
		}
	}
	
	private enum PrimitiveType{
		String(new String[]{"str", "string"}), integer(new String[]{"int", "integer"}), TBD(new String[]{"tbd"});
		
		private static final PrimitiveType[] primitiveTypes = new PrimitiveType[]{PrimitiveType.String, PrimitiveType.integer, PrimitiveType.TBD};
		
		private final String[] nameAlterations;
		private PrimitiveType(String[] nameAlterations)
		{
			this.nameAlterations = nameAlterations;
		}
		
		private final boolean hasName(String s)
		{
			for (String name : nameAlterations)
				if (name.equalsIgnoreCase(s))
					return true;
			
			return false;
		}
		
		private CmdParam getCmdParam(String name, ParameterDetails parameterDetails, String defValue, int totalBytes)
		{
			if (hasName("str"))
				return new CmdStrParam(name, parameterDetails, defValue, totalBytes);
			else if (hasName("int"))
				return new CmdIntParam(name, parameterDetails, defValue, totalBytes);
			else if (hasName("tbd"))
				return new CmdStrParam(name, parameterDetails, defValue, totalBytes);
			
			return null;
		}
	}
	
	public static final CmdParam getCmdParam(String name, String type, String primitive, String bytes, ParameterDetails parameterDetails, String defValue)
	{
		System.out.println("NAME: " + name + ", TYPE: " + type +", PRIMITIVE: " + primitive +  ", BYTES: " + bytes + ".");

		for (PrimitiveType primitiveType : PrimitiveType.primitiveTypes)
			if (primitiveType.hasName(primitive))
				return primitiveType.getCmdParam(name, parameterDetails, defValue, getTotalBytes(bytes, type));
		
		return new CmdStrParam("ERROR", parameterDetails, "ERROR", 64);
	}
	
	private static final int getTotalBytes(String bytes, String type)
	{
		int bytesInt = 64;
		try {bytesInt = Integer.parseInt(bytes);}
		catch (Throwable e) {}
		
		for (DataType d : DataType.dataTypes)
			if (d.getName().equalsIgnoreCase(type))
				return bytesInt * d.getBytes();
		
		return 64;
	}
}
