package gui.menu.prompts;

public final class GettingStartedPrompt {
	public static final void launchShell() {
		String title = "JCAT";
		String[] lines = new String[8];

		lines[0] = "Getting Started";
		lines[1] = "";
		lines[2] = "     Go to File > Populate Apps, and navigate to the desired Apps' XML files.";
		lines[3] = "     Select all desired Apps using CTRL + 'click'. Press 'Open'";
		lines[4] = "";
		lines[5] = "To command an App, go to the Command menu, hover over the desired App,";
		lines[6] = "and select the desired command. To view telemetry for an App, go to the";
		lines[7] = "Telemetry menu, and select the app.";
		try{GenericTextPrompt.launchShell(title, lines);}
		catch(Throwable e){};
	}
}