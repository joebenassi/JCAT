package applications;

import java.util.ArrayList;

import gui.popups.menu.InputCmdParamPrompt;
import gui.popups.tlm.PopupFiller;

import main.Launcher;
import network.Networker;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import packets.ccsds.CcsdsTlmPkt;
import packets.cmd.CmdPkt;
import packets.tlm.TlmPkt;
import utilities.TelemetryUpdater;

/**
 * IN DEVELOPMENT. SUBJECT TO CHANGE.
 * 
 * A dynamic and versatile class that can represent any Command and Telemetry
 * App. Every App is an instance of this class with App-specific parameters. The
 * parameters are configured from an XML document through the
 * packets.housekeeping.XMLParser class. This is done through File -> Populate
 * Apps and selecting appropriately-formatted XML files on your computer.
 * 
 * @author Joe Benassi
 * 
 */
public final class App {
	private final ArrayList<CmdPkt> commands;
	private final TlmPkt[] telemetry;
	private final String name;
	private final PopupFiller popupFiller;
	private final int TlmAppID;

	/**
	 * This is a Command and Telemetry app configured with app-specific
	 * parameters, and can represent any Command and Telemetry app. Every app is
	 * an instance of this class with app-specific parameters. The parameters
	 * are configured from an XML document through the
	 * packets.housekeeping.XMLParser class. This is done through File ->
	 * Populate Apps and selecting appropriately-formatted XML files on your
	 * computer.
	 * 
	 * @param displayName
	 *            The name that appears in the menu. Specific to an App.
	 * @param popupName
	 *            The name that appears in the popup telemetry page. Specific to
	 *            an App.
	 * @param prefix
	 *            The prefix name specific to an App.
	 * @param commands
	 *            The command information specific to an App. This input
	 *            ArrayList should contain every CmdPkt for this App, with each
	 *            fully defined in all ways, including its parameters.
	 * @param telemetry
	 *            The telemetry information specific to an App. This input array
	 *            should contain all of the telemetry qualities possible to
	 *            display for the App. For example, if this were the Executive
	 *            Services app, telemetry.length = 35.
	 * @param appID
	 *            The ID specific to an App.
	 */
	public App(final String name, final ArrayList<CmdPkt> commands,
			final TlmPkt[] telemetry, final int TlmAppID) {
		final String[] entryNames = new String[telemetry.length];

		for (int i = 0; i < entryNames.length; i++) {
			entryNames[i] = telemetry[i].getName();
		}

		popupFiller = new PopupFiller(name, entryNames);

		this.TlmAppID = TlmAppID;
		this.name = name;
		this.commands = commands;
		this.telemetry = telemetry;

		//if (!name.equals("FT_APP"))
		Networker.addApp(this);
		Launcher.addUserActivity("IMPORTED APP PROFILE: " + name);
	}

	/**
	 * Returns the ID of this particular App. Used to differentiate Apps
	 * behind-the-scenes.
	 * 
	 * @return the ID of this particular App.
	 */
	public final int getTlmAppID() {
		return TlmAppID;
	}

	public final void setTime(final String time) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				popupFiller.setTime(time);
			}
		});
	}

	public final String getTelemetryValue(int index) {
		return telemetry[index].getValue();
	}

	public final Text getTelemetryText(int index) {
		return popupFiller.getText(index);
	}

	public final int getTelemetryAmt() {
		return telemetry.length;
	}

	public final void executeCommand(int index, String[] parameters) {
		commands.get(index).execute(parameters);
	}

	/**
	 * Returns the name of this particular App.
	 * 
	 * @return The name of this particular App.
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Returns the names of the commands, as displayed in the Menu.
	 * 
	 * @return the names of the commands, as displayed in the Menu.
	 */
	public final String[] getCommandNames() {
		String[] names = new String[commands.size()];
		for (int i = 0; i < commands.size(); i++)
			names[i] = commands.get(i).getName();

		return names;
	}

	/**
	 * Returns a Listener for a Command that, when executed, launches the
	 * parameter-input window for that Command.
	 * 
	 * @param index
	 *            the index of the desired Command within this App.
	 * @return A Listener for a Command that, when executed, launches the
	 *         parameter-input window for that Command.
	 */
	public final Listener getCommandListener(final int index) {
		return new Listener() {
			@Override
			public void handleEvent(Event e) {
				try {
					InputCmdParamPrompt.launchShell(commands.get(index));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		};
	}

	/**
	 * Returns a Listener that, when executed, launches the popup telemetry
	 * window for this App. After it is launched, the input Shell is given
	 * focus.
	 * 
	 * @param shell
	 *            The shell containing the main Shell of the app.
	 * @return A Listener that launches the popup telemetry window when executed
	 */
	public final Listener getTelemetryListener(final Shell shell) {
		return new Listener() {
			@Override
			public void handleEvent(Event e) {
				popupFiller.launchPopup();
				shell.setActive();
			}
		};
	}

	public final TlmPkt[] getTelemetry() {
		return telemetry;
	}

	public final void ingest(CcsdsTlmPkt TlmPkt) {
		TelemetryUpdater.updateTelemetry(TlmPkt, this);
	}
}