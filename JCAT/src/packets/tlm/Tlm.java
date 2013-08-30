package packets.tlm;

import packets.parameters.DataType;

/**
 * FULLY DOCUMENTED. This class contains the name and value of a telemetry
 * parameter to display in the GUI. It also contains the DataType of this
 * parameter, to be used for parsing of telemetry CCSDS packets.
 * 
 * @author Joe Benassi
 */
public final class Tlm {
	private final String name;
	private String value;
	private final DataType dataType;

	/**
	 * For
	 * example, the Executive Services app has 35 potential Telemetry objects,
	 * with <code>name</code> instance variables such as "CmdCounter",
	 * "ErrCounter", "CFECoreChecksum".
	 * 
	 * @param name
	 *            What quality this represents
	 */

	/**
	 * This constructs a Tlm object with the given attributes. Contains a name
	 * identifying the value, and the value itself. There are as many Tlm
	 * objects for an App as there are qualities that can be displayed. 
	 * 
	 * @param name
	 *            The name to display as what it represents.
	 * @param type
	 *            The data type for this parameter. For example, uint8, int15,
	 *            char.
	 * @param primitive
	 *            Whether or not this should display a string. If this value
	 *            starts with 's', this is deemed a String.
	 * @param constant
	 *            The name of the constant used if this is a String parameter.
	 *            If this is a String parameter, the value tied to this String
	 *            as defined in the Constant Definition file is used to increase
	 *            the amount of bytes allocated to this. The value tied to this
	 *            name represents the numerical amount of "type"s in the App's
	 *            Housekeeping packet.
	 */
	public Tlm(String name, String type, String primitive, String constant) {
		this.name = name;
		dataType = DataType.getDataType(type, primitive, constant);
	}

	/**
	 * Returns a String equal to the name of this Tlm object.
	 * 
	 * @return The name of this Tlm object.
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
	 * Returns The value assigned to this Tlm attribute.
	 * 
	 * @return The String representing the status of this Tlm attribute.
	 */
	public final String getValue() {
		return value;
	}

	/**
	 * The DataType of this Tlm object.
	 * 
	 * @return The DataType of this Parameter.
	 */
	public DataType getDataType() {
		return dataType;
	}
}