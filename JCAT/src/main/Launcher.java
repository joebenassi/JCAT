package main;

import gui.mainpage.MainPageFiller;
import gui.menu.MenuFiller;

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
import utilities.Updater;

/**
 * @author Joe Benassi
 * @version 1.0.0 July 8, 2013
 * 
 *          The class containing the main method to execute the program
 */
public final class Launcher {
	private static Display display = new Display();
	private static Shell shell = new Shell(display, SWT.DIALOG_TRIM
			| SWT.CLIP_CHILDREN);
	private static boolean shouldRestart = true;
	public static MainPageFiller mainPageFiller = new MainPageFiller(shell);

	/**
	 * The main method to execute the program. Creates the main page and the
	 * initial menu, to be changed later as Apps are imported
	 * 
	 * @param args
	 *            the main method argument
	 */
	public static final void main(String[] args) {
		addShellExitBehavior(shell);
		new Launcher();
	}

	/**
	 * Identical to <code>main()</code>. <code>main</code> calls
	 * <code>new Launcher();</code>
	 */
	private Launcher() {
		startup(shell);
		awaitShutdown(shell);
	}

	public final static void startup(Shell s) {
		final String version = "1.0.0";
		MenuFiller.addMenu(s, null, version);

		s.setImage(ResourceLoader.getJCATLogo());
		s.open();
		updateTime();

		addUserActivity("JCAT startup successful");
	}

	public static final void awaitShutdown(Shell s) {
		while (!s.isDisposed())
			if (!Display.getCurrent().readAndDispatch())
				shutdown();
	}

	public static final void shutdown() {
		if (!shouldRestart)
		{
			Display.getCurrent().sleep();

			ColorConstants.disposeColors();
			FontConstants.disposeFonts();

		try {
			Display.getCurrent().dispose();
			} catch (Throwable e) {
		}
			System.out.println("MAIN THREAD ENDED!");
		}
	}

	private final static void addShellExitBehavior(final Shell shell) {
		shell.addShellListener(new ShellAdapter() {
			@Override
			public final void shellClosed(ShellEvent e) {
				e.doit = false;

				int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
				MessageBox messageBox = new MessageBox(shell, style);
				messageBox.setText("Confirmation of Exit");
				messageBox.setMessage("End your current session?");
				e.doit = messageBox.open() == SWT.YES;
				if (e.doit) {
					ColorConstants.disposeColors();
					FontConstants.disposeFonts();

					try {
						Display.getCurrent().sleep();
						Display.getCurrent().dispose();
					} catch (Throwable e1) {
					}
					System.out.println("MAIN THREAD ENDED!");
					if (ColorConstants.base.isDisposed())
						System.out.println("COLORS DISPOSED!");
					if (FontConstants.dialogFont.isDisposed())
						System.out.println("FONTS DISPOSED!");
				}
			}
		});
	}
	
	public final static void restartApplication(Shell s) {
		s.setVisible(false);
		PktReader.end();
		Shell shell2 = new Shell(display, SWT.DIALOG_TRIM
				| SWT.CLIP_CHILDREN);
		addShellExitBehavior(shell2);
		mainPageFiller = new MainPageFiller(shell2);
		startup(shell2);
		awaitShutdown(shell2);
	}

	public static final void addEvent(String eventMessage) {
		mainPageFiller.addEventMessage("SOMETIME", eventMessage,
				ColorConstants.text);
	}

	public static final void addUserActivity(String userActivityMessage) {
		mainPageFiller.addUserActivity("SOMETIME", userActivityMessage,
				ColorConstants.text);
	}

	/**
	 * Updates the UTC, GMT, SC, and SequenceCount values to arbitrary,
	 * hardcoded values. These updates are reflected throughout the program
	 */
	private static void updateTime() {
		Thread updater = new Thread(new Runnable() {
			private int currentVal = 0;

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (Throwable e) {
					}

					currentVal++;
					Updater.setUTCValue(currentVal + "");
					Updater.setGMTValue(2 * currentVal + "");
					Updater.setSCValue(3 * currentVal + "");
					Updater.setSequenceCountValue(4 * currentVal + "");
				}
			}
		});

		updater.setDaemon(true);
		updater.start();
	}
}