package applications;

import java.util.ArrayList;

import gui.popups.menu.CommandPrompt;
import gui.popups.tlm.PopupFiller;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import packets.cmd.CmdPkt;
import packets.tlm.Telemetry;

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
	private final Telemetry[] telemetry;
	private final String name;
	private final PopupFiller popupFiller;
	private final int appID;
	private static final int TLM_MID_HK = 0x0800; // what does this do? (Joe)
	private ArrayList<Integer> TlmList; // what does this do? (Joe) // TODO -
										// Couple with CCSDS stream ID
										// type

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
	public App(final String name, final String prefix,
			final ArrayList<CmdPkt> commands, final Telemetry[] telemetry,
			final int appID) {
		final String[] entryNames = new String[telemetry.length];

		for (int i = 0; i < entryNames.length; i++) {
			entryNames[i] = telemetry[i].getName();
		}
		TlmList = new ArrayList<Integer>();
		TlmList.add(TLM_MID_HK);
		popupFiller = new PopupFiller(name, entryNames);

		this.appID = appID;
		this.name = name;
		this.commands = commands;
		this.telemetry = telemetry;
	}

	/**
	 * Returns the ID of this particular App. Used to differentiate Apps
	 * behind-the-scenes.
	 * 
	 * @return the ID of this particular App.
	 */
	public final int getAppID() {
		return appID;
	}

	/**
	 * Updates the GUI's displayed value of Telemetry[index]. Telemetry[index]
	 * contains the real-time correct value, and the GUI updates its current
	 * value to this when this method is executed.
	 * 
	 * @param index
	 *            The index of the GUI to update to its correct, current value
	 *            is defined in Telemetry[index].
	 */
	public final void updateTelemetry(final int index) {
		popupFiller.getText(index).setText(telemetry[index].getValue());
	}

	/**
	 * Updates the GUI to display the current telemetry data. This occurs
	 * immediately, and will update each telemetry quality once.
	 */
	public final void updateTelemetry() {
		for (int i = 0; i < telemetry.length; i++)
			updateTelemetry(i);
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
					CommandPrompt.launchShell(commands.get(index));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
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
}