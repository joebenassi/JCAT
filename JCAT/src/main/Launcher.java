package main;

import gui.mainpage.MainPageFiller;
import gui.menu.MenuFiller;
import gui.popups.menu.NewUserPrompt;

import network.Networker;
import network.PktReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.graphics.Image;
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
 * @author Joe Benassi
 * @version 1.0.0 July 8, 2013
 * 
 *          The class containing the main method to execute the program
 */
public final class Launcher {
	private static boolean shouldRestart = true;
	public static volatile MainPageFiller mainPageFiller;
	private static volatile int instanceNum = 0;

	/**
	 * The main method to execute the program. Creates the main page and the
	 * initial menu, to be changed later as Apps are imported
	 * 
	 * @param args
	 *            the main method argument
	 */
	public static final void main(String[] args) {
		// DeviceData data = new DeviceData();
		// data.tracking = true;
		Display display = new Display();// data);
		// Sleak sleak = new Sleak();
		// sleak.open();
		PktReader.start();
		startup();
	}

	/**
	 * Identical to <code>main()</code>. <code>main</code> calls
	 * <code>new Launcher();</code>
	 */
	public final static void startup() {
		instanceNum++;
		ShellDisposer.disposePopups();
		final Shell s = GenericPrompt.getMainShell();
		TimeKeeper.reset();

		Networker.startNetworker();
		mainPageFiller = new MainPageFiller(s);

		addShellExitBehavior(s);
		final String version = "1.0.0";
		MenuFiller.addMenu(s, null, version);
		// PreferenceTest.showHelp();
		if (PreferenceStorage.shouldShowHelp())
			NewUserPrompt.launch();

		s.open();

		addUserActivity("JCAT startup successful");
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

	public final static void restartApplication(Shell s) {
		s.setVisible(false);
		startup();
	}

	public static final void addEvent(String time, String config, String msgStr) {
		mainPageFiller.addEventMessage(time, config, msgStr,
				ColorConstants.textColor);
	}

	public static final void addUserActivity(String userActivityMessage) {
		mainPageFiller.addUserActivity(TimeKeeper.getElapsedTime(),
				userActivityMessage, ColorConstants.textColor);
	}

	public static final int getInstanceNum() {
		return instanceNum;
	}
}