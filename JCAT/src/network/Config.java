package network;

public class Config {
	private final String name;
	private final String MsgId;
	private int MsgIdVal;

	public Config(String name, String MsgId) {
		this.name = name;
		this.MsgId = MsgId;
		adjValue();
	}

	public final String getName() {
		return name;
	}

	public final int getMsgId() {
		return MsgIdVal;
	}

	private final void adjValue() {
		try {
			MsgIdVal = Integer.parseInt(MsgId.substring(2), 16);
		} catch (Throwable e) {
			MsgIdVal = 0;
		}
	}
}
