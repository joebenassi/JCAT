package gui.popups.menu;

public enum HelpPopup {
	GettingStarted("Getting Started", Text.GETTINGSTARTED), ImportingNewApps(
			"Importing New Apps", Text.IMPORTINGNEWAPPS), AppsNotImporting(
			"Apps Not Importing", Text.APPSNOTIMPORTING), EmptyEventWindow(
			"Empty Event Window", Text.EMPTYEVENTWINDOW), CommandErrors(
			"Command Errors", Text.COMMANDERRORS), NoTelemetry("No Telemetry",
			Text.NOTELEMETRY), ProgramFreezes("Program Freezes",
			Text.PROGRAMFREEZES);

	private final String[] paragraphs;
	private final String header;

	private HelpPopup(String header, String[] paragraphs) {
		this.header = header;
		this.paragraphs = paragraphs;
	}

	public final void launch() {
		ShowHelpPopup.launch(header, paragraphs);
	}

	private static class Text {

		private static final String[] GETTINGSTARTED = new String[] {
				"This page is designed to explain the procedure to startup JCAT.",
				"First, you must load the App Profiles. Navigate to File > Import App Profiles. Select the App Profile XML files on your local hard drive, and click 'Open'. Next, a window will prompt for the desired configurations to run each App. Check the desired boxes, and click 'Open'. Finally, a window will prompt for the Constant Definition file. Select the desired Constant Definition XML file on your local hard drive, and click 'Open'.",
				"To begin sending commands, you must first configure your Output settings. Navigate to Configure > Output. Input the IP, port, and endian of the recipient, and click 'OK'. To send a command, navigate to Command > (App name) > (command name). A window will prompt for parameters. Fill out the parameters and click 'Send'.",
				"To begin displaying telemetry, you must first configure your Output settings. Navigate to Configure > Output. Input the IP, port, and Endian of the telemetry sender, and click 'OK'. Next, you must navigate to File > Enable Telemetry. You should now have a new confirmation entry in your Event Window. To view an App's telemetry, navigate to Telemetry > (App name). If any field is labeled 'init', no telemetry was recieved for this App.",
				"If a problem occurs in this process, navigate to Help > Common Problems for possible causes and solutions." };
		private static final String[] IMPORTINGNEWAPPS = new String[] { "To import" };

		private static final String[] APPSNOTIMPORTING = new String[] {
				"If an App is unchoosable in the 'Select Apps' window after importing its App Profile XML: ",
				"The App Profile XML is incorrectly formatted. See Menu > Help > Importing New Apps" };
		private static final String[] EMPTYEVENTWINDOW = new String[] {
				"If empty after navigating to File > Enable Telemetry, navigate to Commands > TO_LAB > TO_OUTPUT_ENABLE_CC, input your IP address in 'xxx.xxx.xxx.xxx' format, and press 'Send'. Your IP address is also displayed in Configure > Input.",
				"If that doesn't work, Incorrect output configuration, no connection between JCAT and CFS, no CFS, another active JCAT instance" };
		private static final String[] COMMANDERRORS = new String[] {
				"If errors are displayed in event wndow: ",
				"Incorrect output Endian, Command incorrectly defined in Import file for CFE version" };
		private static final String[] NOTELEMETRY = new String[] { "If the event window is working but App telemetry isnt: "
				+ "Another open instance of JCAT" };
		private static final String[] PROGRAMFREEZES = new String[] { "If the program freezes after sending a command: "
				+ "Command incorrectly defined in Import App Profile XML" };
	}
}
