package gui.popups.menu;

import java.util.ArrayList;

enum Status {
	none("No"), some("Some"), all("All"), undef("");

	private final String name;

	Status(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static final Status getStatus(int i) {
		if (i == 0)
			return none;
		else if (i == 1)
			return some;
		else if (i == 2)
			return all;
		return undef;
	}
}

enum Symptoms {
	AAS(Status.all, Status.all, Status.some), ASA(Status.all, Status.some,
			Status.all), SSS(Status.some, Status.some, Status.some), ANN(
			Status.all, Status.none, Status.none), NNN(Status.none,
			Status.none, Status.none);

	private final Status minicom;
	private final Status telemetry;
	private final Status event;

	Symptoms(Status minicom, Status telemetry, Status event) {
		this.minicom = minicom;
		this.telemetry = telemetry;
		this.event = event;
	}

	public final Status[] getStatuses() {
		return new Status[] { minicom, telemetry, event };
	}
}

public enum HelpPopup {

	GettingStarted("Getting Started", Text.GETTINGSTARTED, null), ImportingNewApps(
			"Importing New Apps", Text.IMPORTINGNEWAPPS, null), AppsNotImporting(
			"Apps Not Importing", Text.APPSNOTIMPORTING, null), ProgramFreezes(
			"Freezes/Crashes", Text.PROGRAMFREEZES, null), AAS("AAS", Text.AAS,
			Symptoms.AAS), ASA("ASA", Text.ASA, Symptoms.ASA), SSS("SSS",
			Text.SSS, Symptoms.SSS), ANN("ANN", Text.ANN, Symptoms.ANN), NNN(
			"NNN", Text.NNN, Symptoms.NNN);

	public static final HelpPopup[] commonProblemPopups = new HelpPopup[] {
			AppsNotImporting, AAS, ASA, SSS, ANN, NNN, ProgramFreezes };

	private final String[] paragraphs;
	private final String header;
	private final Symptoms symptoms;

	private HelpPopup(String header, String[] paragraphs, Symptoms symptoms) {
		this.header = header;
		this.paragraphs = paragraphs;
		this.symptoms = symptoms;
	}

	public String getName() {
		if (symptoms == null)
			return header;
		Status[] s = symptoms.getStatuses();
		String[] names = new String[] { s[0].getName(), s[1].getName(),
				s[2].getName() };

		ArrayList<String> names2 = new ArrayList<String>();
		for (String n : names) {
			if (n.length() > 1)
				names2.add(n);
		}
		String output = "";

		for (int i = 0; i < names2.size(); i++) {
			output += names2.get(i);
			if (i == 0)
				output += " Minicom Messages";
			else if (i == 1)
				output += " Telemetry";
			else if (i == 2)
				output += " Event Messages";
			if (i < names2.size() - 1)
				output += ", ";
		}
		return output;
	}

	public final void launch() {
		ShowHelpPopup.launch(header, paragraphs);
	}

	private static class Text {
		private static final String[] GETTINGSTARTED = new String[] {
				"This page is designed to explain the procedure to startup JCAT.",
				"First, you must load the App Profiles. Navigate to File > Import App Profiles. Select the App Profile XML files on your local hard drive, and click 'Open'. Next, a window will prompt for the desired configurations to run each App. Check the desired boxes, and click 'Open'. Lastly, a window will prompt for the Constant Definition file. Select the desired Constant Definition XML file, and click 'Open'",
				"To begin sending commands, you must first configure your Output settings. Navigate to Configure > Output. Input the IP, port, and endian of the recipient, and click 'OK'. To send a command, navigate to Command > (App name) > (config name) > (command name). A window will prompt for parameters. Fill out the parameters and click 'Send'.",
				"To begin displaying telemetry, you must first configure your Output settings (see the previous paragraph). Additionally, you must enable telemetry. Depending on your setup, navigate to File > Enable Wireless Telemetry or File > Enable Local Telemetry. To view an App's telemetry, navigate to Telemetry > (App name) > (config name). If any field is labeled 'No Telemetry', no telemetry was received for the App.",
				"To begin displaying event messages, you must first configure your Output settings and enable telemetry (see the previous paragraphs). Additionally, you must navigate to File > Enable Event Messages. Event messages are displayed on the top half of the main page.",
				"If JCAT does not successfully send commands, display event messages, or display telemetry at this point, navigate to Help > Common Problems for possible solutions." };

		private static final String[] AAS = new String[] {
				"Note: many commands do not display event messages.",
				"Check the Constant Definition file and ensure that all event message IDs are included.",
				"If the problem exists with particular configs, you should try to disable and re-enable event messages for them. For each config, navigate to Command > TO_LAB > CFE_TO_REMOVE_PKT_CC. A window will prompt for the config’s msgid. Input the msgid and click ‘Send’. Then, for each config, navigate to Command > TO_LAB > CFE_TO_ADD_PKT_CC. A window will prompt for the config’s msgid, the msgid for the config containing TO_LAB, and two other parameters, for which “0” and “100” are generally appropriate arguments. Input the values and press 'Send'." };

		private static final String[] ASA = new String[] { "If some Apps display telemetry, and others don’t, ensure that the tlmmid defined in the App Profile of the problematic App is correct for the CFS version.", 
				"If an App has telemetry values that are incorrect, ensure that the App Profile is absolutely accurate to its App running in the CFS."
				};

		private static final String[] SSS = new String[] {
				"If some Apps send commands, but others don’t, ensure that the cmdmid defined in the App Profile of the problematic App is correct for the CFS version.",
				"If all Apps within a config do not send commands, those configs are not running CFS Apps. If you are using a virtual machine, it is likely only one config would run, because it is likely that only one instance of the CFS would run.",
				"If commands display error messages in the Event Window, navigate to Configure > Output and ensure that your output Endian is correct. Also ensure that the App Profile perfectly describes the version of the App running on the CFS." };

		private static final String[] ANN = new String[] {
				"Ensure you imported TO_LAB's App Profile and that no-ops to TO_LAB appear on minicom. Then, navigate to Command > TO_LAB > (config where TO_LAB is running) > Output Enable, and input your IP address. Try multiple IP's. You may have to do this more than once in a session, as CFS can only output telemetry and event messages to one IP address. Complications include other ground systems enabling telemetry, having a dynamic IP address, and determining your correct IP address.",
				"Ensure that this is the only JCAT program currently running on your machine, and that the User Activity window displays “JCAT startup successful”. To be certain, check your system processes and confirm that there is only one Java Virtual Machine running. ",
				"Ensure that your firewall accepts all packets from port 1235." };

		private static final String[] NNN = new String[] { "Ensure you are connected to the CFS, wirelessly, wired, or otherwise. If the command parameter window stalls for a second after you click ‘Send’, it is likely due to this. Ensure that your firewall and permissions allow for networking." };

		private static final String[] IMPORTINGNEWAPPS = new String[] { "INPROG: To import" };

		private static final String[] APPSNOTIMPORTING = new String[] {
				"INPROG: If an App is unchoosable in the 'Select Apps' window after importing its App Profile XML: ",
				"The App Profile XML is incorrectly formatted. See Menu > Help > Importing New Apps" };

		private static final String[] PROGRAMFREEZES = new String[] {
				"If JCAT freezes after sending a command, ensure that the command is correctly defined defined in its App Profile, specifically with respect to it's primitive. If you define a parameter as an integer, and input something else, this may occur.", 
				"If JCAT's menu has missing text or is acting differently, or if JCAT slows down, becomes unresponsive, or has the main page window change appearance, it is the result of processing an App Profile. The App Profile may even be perfectly formatted. This malfunction has yet to be resolved."
		 };
	}
}
