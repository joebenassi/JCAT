package main;

import gui.mainpage.MainPageFiller;
import gui.menu.MenuFiller;
import gui.popups.menu.NewUserPrompt;

import network.Networker;
import network.PktReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import resources.ResourceLoader;

import utilities.ColorConstants;
import utilities.FontConstants;
import utilities.GenericPrompt;
import utilities.PreferenceStorage;
import utilities.ShellDisposer;
import utilities.TimeKeeper;

/**
 * FULLY DOCUMENTED. The class containing the main method to execute the
 * program.
 * 
 * @author Joe Benassi
 */
public final class Launcher {
	private static boolean shouldRestart = true;
	private static final String version = "1.0.0";
	private static final String successMessage = "JCAT startup successful";
	private static final String[] failureMessage = new String[] {
			"JCAT startup unsuccessful",
			"Existing JCAT instance. No event messages or telemetry will display" };

	/**
	 * This is the MainPageFiller that occupies the current primary Shell. When
	 * File > Restart is executed, this does not get destroyed.
	 */
	public static volatile MainPageFiller mainPageFiller;

	/**
	 * This value equals the instance of JCAT. It increments every time JCAT
	 * restarts. This is so that certain threads end after a restart. This is
	 * safe because those same threads are created on JCAT's restart.
	 */
	private static volatile int instanceNum = 0;

	/**
	 * The main method to execute the program. Starts PktReader, and calls
	 * 'startup()'.
	 * 
	 * @param args
	 *            the main method argument
	 */
	public static final void main(String[] args) {
		Display display = new Display();
		PktReader.start();
		startup();
	}

	/**
	 * Increments the InstanceNum, disposes of old popups, resets the time,
	 * resets Networker, adds the main page, adds the 'Continue?' prompt on
	 * exit, adds the menu, launches the 'new user' window if appropriate,
	 * displays the main page, and displays in the Event Window if JCAT's
	 * startup was successful.
	 */
	public final static void startup() {
		instanceNum++;
		ShellDisposer.disposePopups();
		final Shell s = GenericPrompt.getMainShell();
		TimeKeeper.reset();

		Networker.restart();
		mainPageFiller = new MainPageFiller(s);

		addShellExitBehavior(s);
		MenuFiller.addMenu(s, null, version);

		if (PreferenceStorage.shouldShowHelp())
			NewUserPrompt.launch();

		s.open();

		if (!PktReader.isFunctional()) {
			addUserActivity(failureMessage[0]);
			addUserActivity(failureMessage[1]);
		} else
			addUserActivity(successMessage);
		try {
			while (!Display.getCurrent().isDisposed()) {
				try {
					boolean bool1 = !Display.getCurrent().readAndDispatch();
					if (bool1)
						shutdown();
				} catch (Throwable e) {
				}
			}
		} catch (Throwable e) {
		}
	}

	/**
	 * If shouldRestart is false, then this disposes of all resources, popups,
	 * shells, and the Display.
	 */
	public static final void shutdown() {
		if (!shouldRestart) {
			ResourceLoader.disposeImages();
			ColorConstants.disposeColors();
			FontConstants.disposeFonts();
			ShellDisposer.disposePopups();
			Shell[] shells = Display.getCurrent().getShells();
			for (Shell s : shells)
				s.dispose();
			try {
				Display.getCurrent().sleep();
				Display.getCurrent().dispose();
			} catch (Throwable e1) {
			}
		}
	}

	/**
	 * Adds a "Confirm Quit" prompt when the user attempts to exit.
	 * 
	 * @param shell
	 *            The shell to add the listener to.
	 */
	private final static void addShellExitBehavior(final Shell shell) {
		shell.addShellListener(new ShellAdapter() {
			@Override
			public final void shellClosed(ShellEvent e) {
				e.doit = false;

				int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO
						| SWT.ON_TOP | SWT.ICON_WARNING;
				MessageBox messageBox = new MessageBox(shell, style);
				messageBox.setText("Confirm Quit");
				messageBox.setMessage("Are you sure you want to exit JCAT?");
				e.doit = messageBox.open() == SWT.YES;
				if (e.doit) {
					shouldRestart = false;
					shutdown();
				}
			}
		});
	}

	/**
	 * Sets the current main page invisible, and calls startup().
	 * 
	 * @param s
	 *            The current primary shell; the Shell the main page is
	 *            currently occupying.
	 */
	public final static void restartApplication(Shell s) {
		s.setVisible(false);
		startup();
	}

	/**
	 * Adds an event to the Event Window.
	 * 
	 * @param time
	 *            The time for this event. First column.
	 * @param config
	 *            The config for this event. Second column.
	 * @param msgStr
	 *            The event message. Third column.
	 */
	public static final void addEvent(int time, String config, String msgStr) {
		mainPageFiller.addEventMessage(TimeKeeper.getTimeFormatted(time),
				config, msgStr, ColorConstants.textColor);
	}

	/**
	 * Adds a mesage to the User Activity window.
	 * 
	 * @param userActivityMessage
	 *            The message to display.
	 */
	public static final void addUserActivity(String userActivityMessage) {
		mainPageFiller.addUserActivity(TimeKeeper.getElapsedTime(),
				userActivityMessage, ColorConstants.textColor);
	}

	/**
	 * Returns the instanceNum; the number denoting the particular instance of
	 * JCAT. This instanceNum changes whenever JCAT is restarted.
	 * 
	 * @return The instance of JCAT.
	 */
	public static final int getInstanceNum() {
		return instanceNum;
	}
}