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
import packets.cmd.Cmd;
import packets.tlm.Tlm;
import utilities.TelemetryUpdater;

/**
 * FULLY DOCUMENTED. A class that has an instance for each App Profile loaded.
 * 
 * @author Joe Benassi
 */
public final class App {
	private final ArrayList<Cmd> commands;
	private final Tlm[] telemetry;
	private final String name;
	private final String config;
	private final PopupFiller popupFiller;
	private final int TlmAppID;
	private final int CmdAppID;

	/**
	 * This is a Command and Telemetry app configured with app-specific
	 * parameters, and can represent any Command and Telemetry app. Every app is
	 * an instance of this class with app-specific parameters. The parameters
	 * are configured from an XML document through the utilities.XMLParser
	 * class. This is done through File > Populate Apps and selecting
	 * appropriately-formatted XML files on your computer.
	 * 
	 * @param name
	 *            The name to display for this App in the GUI.
	 * @param config
	 *            The name of the Config, to be displayed in the GUI.
	 * @param commands
	 *            The commands for this App.
	 * @param telemetry
	 *            The telemetry for this App.
	 * @param TlmAppID
	 *            The telemetry msgid. If an incoming packet has a msgid
	 *            matching this, the packet will be treated as a houskeeping
	 *            packet to update this App's telemetry values in the GUI.
	 * @param CmdAppID
	 *            The command msgid. If the CFS has an App whose command msgid
	 *            matches this, the CFS will treat this as a command packet for
	 *            the App.
	 */
	public App(final String name, String config,
			final ArrayList<Cmd> commands, final Tlm[] telemetry,
			final int TlmAppID, final int CmdAppID) {
		final String[] entryNames = new String[telemetry.length];

		for (int i = 0; i < entryNames.length; i++) {
			entryNames[i] = telemetry[i].getName();
		}

		this.TlmAppID = TlmAppID;
		this.CmdAppID = CmdAppID;
		this.config = config;
		this.name = name;
		this.commands = commands;
		this.telemetry = telemetry;

		popupFiller = new PopupFiller(getMenuName(), entryNames);

		Networker.addApp(this);
		Launcher.addUserActivity("IMPORTED APP PROFILE: " + getMenuName());
	}

	/**
	 * Returns the tlmmid of this particular App. Used to determine if a
	 * telemetry housekeeping packet belongs to this App.
	 * 
	 * @return The tlmmid of this particular App.
	 */
	public final int getTlmAppID() {
		return TlmAppID;
	}

	/**
	 * Sets the "SC" or "Sequence Count" value in this App's telemetry window.
	 * This is used when a telemetry packet is parsed for its SC value, and
	 * needs to be displayed in the GUI.
	 * 
	 * @param SC
	 *            The string to represent as this App's current Sequence Count.
	 */
	public final void setSC(final String SC) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				popupFiller.setSC(SC);
			}
		});
	}

	/**
	 * Sets the Time value in this App's telemetry window. This is used when a
	 * telemetry packet is parsed for its Time value, and needs to be displayed
	 * in the GUI.
	 * 
	 * @param time
	 *            The string to represent as this App's current Time, as
	 *            determined by the CFS App.
	 */
	public final void setTime(final String time) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				popupFiller.setTime(time);
			}
		});
	}

	/**
	 * Returns a string representing the value of a telemetry parameter. The
	 * input "index" determines the particular telemetry parameter.
	 * 
	 * @param index
	 *            The index of the telemetry parameter within this App's
	 *            telemetry housekeeping packet.
	 * @return The string representing the value of the telemetry parameter in
	 *         question.
	 */
	public final String getTelemetryValue(int index) {
		return telemetry[index].getValue();
	}

	/**
	 * Returns the GUI object that displays the telemetry parameter's value. The
	 * input "index" determines the particular telemetry parameter.
	 * 
	 * @param index
	 *            The index of the telemetry parameter within this App's
	 *            housekeeping packet.
	 * @return The Text object that contains the string representing the value
	 *         of the telemetry parameter in question.
	 */
	public final Text getTelemetryText(int index) {
		return popupFiller.getText(index);
	}

	/**
	 * Returns the amount of telemetry parameters in this App's housekeeping
	 * packet.
	 * 
	 * @return The integer whose value equals the amount of telemetry parameters
	 *         in this App's housekeeping packet.
	 */
	public final int getTelemetryAmt() {
		return telemetry.length;
	}

	/**
	 * Executes the command at the input index with the input parameters.
	 * 
	 * @param index
	 *            The index of the desired command within this App's commands.
	 * @param parameters
	 *            The string array containing the parameters to pack in this
	 *            command.
	 */
	public final void executeCommand(int index, String[] parameters) {
		commands.get(index).execute(parameters);
	}

	/**
	 * Returns the name of this App as it would appear in the menu. This equals:
	 * name [config].
	 * 
	 * @return A string that represents this App in the menu.
	 */
	public final String getMenuName() {
		return name + " [" + config + "] ";
	}

	/**
	 * Returns the name of this App.
	 * 
	 * @return A string representing the name of this App.
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Returns the name of this App's config.
	 * 
	 * @return A string representing the name of this App's config.
	 */
	public final String getConfig() {
		return config;
	}

	/**
	 * Returns an array of strings, where each string is the name of a command
	 * for this App.
	 * 
	 * @return The names of the commands for this App.
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
	 *            The index of the desired Command within this App.
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
	 * @return A Listener that launches the popup telemetry window when
	 *         executed.
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

	/**
	 * Returns the array of TlmPkts for this App.
	 * 
	 * @return The telemetry objects for this App.
	 */
	public final Tlm[] getTelemetry() {
		return telemetry;
	}

	/**
	 * Returns the command ID for this App.
	 * 
	 * @return the cmdmid for this App.
	 */
	public int getCmdId() {
		return CmdAppID;
	}
}