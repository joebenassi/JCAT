package gui.menu.prompts;

public final class AboutPrompt {
	public static final void launchShell(final String version) {
		String title = "JCAT";
		String[] lines = new String[6];

		lines[0] = "Java Command and Telemetry (JCAT)";
		lines[1] = "Version " + version;
		lines[2] = "";// Select all desired Apps using CTRL + 'click'. Press
						// 'Open'";
		lines[3] = "";
		lines[4] = "Property of the National Aeronautics";
		lines[5] = "and Space Administration (NASA)";

		try{GenericTextPrompt.launchShell(title, lines);}
		catch(Throwable e){};
	}
}
