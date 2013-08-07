package packets.tlm;

import packets.parameters.DataType;
import packets.parameters.ScalarConstant;

/**
 * NOT DOCUMENTED.
 * 
 * @author Joe Benassi
 */
public class TlmPkt {
	private final String name;
	private String value;
	private final DataType dataType;

	/**
	 * The object that represents a single telemetry value. Contains a name
	 * identifying the value, and the value itself. There are as many Telemetry
	 * objects for an App as there are qualities that can be displayed. For
	 * example, the Executive Services app has 35 potential Telemetry objects,
	 * with <code>name</code> instance variables such as "CmdCounter",
	 * "ErrCounter", "CFECoreChecksum".
	 * 
	 * @param name
	 *            What quality this represents
	 */
	public TlmPkt(String name, String type, String primitive, String constant) {
		this.name = name;
		dataType = DataType.getDataType(type, primitive, constant);
	}

	/**
	 * Returns a String equal to the name of this Telemetry object.
	 * 
	 * @return The name of this Telemetry object.
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Replaces the value of this Object with the input String
	 * 
	 * @param newValue
	 *            The status to assign to this.
	 */
	public final void setValue(String newValue) {
		value = newValue;
	}

	/**
	 * Returns The value assigned to this Telemetry quality
	 * 
	 * @return The String representing the status of this Telemetry quality
	 */
	public final String getValue() {
		return value;
	}

	public DataType getDataType() {
		return dataType;
	}
}
