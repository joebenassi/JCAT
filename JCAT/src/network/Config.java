package network;

/**
 * FULLY DOCUMENTED. This class is used for determining the Config assigned to a
 * particular event message to display in the Event Window.
 * 
 * @author Joe Benassi
 */
public class Config {
	/**
	 * The name of this. For example, "CPU1".
	 */
	private final String name;

	/**
	 * The event message ID that this is assigned to.
	 */
	private final int MsgIdVal;

	public Config(String name, String MsgId) {
		this.name = name;
		MsgIdVal = Integer.parseInt(MsgId.substring(2), 16);
	}

	/**
	 * Returns the name of this. For example, "CPU1".
	 * 
	 * @return The name of this to display in event messages.
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Returns an integer that equals the message ID that this is assigned to.
	 * 
	 * @return The event message ID of this.
	 */
	public final int getMsgId() {
		return MsgIdVal;
	}
}
