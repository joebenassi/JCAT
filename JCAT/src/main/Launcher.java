package main;

import gui.mainpage.MainPageFiller;
import gui.menu.MenuFiller;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

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
	private static final Display display = new Display();
	private static final Shell shell = new Shell(display, SWT.DIALOG_TRIM | SWT.CLIP_CHILDREN);
	public static final MainPageFiller mainPageFiller = new MainPageFiller(shell);
	
	/**
	 * The main method to execute the program. Creates the main page and the
	 * initial menu, to be changed later as Apps are imported
	 * 
	 * @param args
	 *            the main method argument
	 */
	public static final void main(String[] args) {
		new Launcher();
	}

	/**
	 * Identical to <code>main()</code>. <code>main</code> calls
	 * <code>new Launcher();</code>
	 */
	private Launcher() {
		final String version = "1.0.0";
		//Display display = new Display();
		///final String version = "1.0.0";
		//Shell shell = new Shell(display, SWT.DIALOG_TRIM | SWT.CLIP_CHILDREN);
		//mainPageFiller = new MainPageFiller(shell);
		
		//new MainPageFiller(shell);
		MenuFiller.addMenu(shell, null, version);

		shell.open();
		updateTime();

		while (!shell.isDisposed())
			if (!Display.getCurrent().readAndDispatch())
				Display.getCurrent().sleep();

		ColorConstants.disposeColors();
		FontConstants.disposeFonts();

		try {Display.getCurrent().dispose();}
		catch (Throwable e) {}
	}

	public static final void addEvent(String eventMessage)
	{
		mainPageFiller.addEventMessage("SOMETIME", eventMessage, ColorConstants.text);
	}
	/**
	 * Updates the UTC, GMT, SC, and SequenceCount values to arbitrary,
	 * hardcoded values. These updates are reflected throughout the program
	 */
	private void updateTime() {
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