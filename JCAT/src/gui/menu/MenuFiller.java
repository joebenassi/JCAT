package gui.menu;

import gui.popups.menu.AboutPrompt;
import gui.popups.menu.GettingStartedPrompt;
import gui.popups.menu.PopulateAppsPrompt;
import gui.popups.menu.RecieverPrompt;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.xml.sax.SAXException;

import applications.App;


/**
 * This class populates the menu of the program.
 * 
 * @author Joe Benassi
 * 
 */
public final class MenuFiller {
	/**
	 * Adds a menu to the input Shell. Adds a File menu, Commands menu,
	 * Telemetry menu, and Help menu. Note: This method is called twice: once
	 * upon launching of the program, and once again when Apps are populated.
	 * The first time this is called there are no Apps and the Command and
	 * Telemetry menus are consequently empty. The second time this is called,
	 * the option to Populate Apps in the File menu is absent.
	 * 
	 * @param shell The Shell to add the menu to.
	 * @param Apps The Apps to add to the menu.
	 * @param version The version of this program.
	 */
	public static final void addMenu(final Shell shell, App[] Apps,
			final String version) {
		shell.setMenuBar(MenuHelper.createMenu(shell, SWT.BAR
				| SWT.LEFT_TO_RIGHT));
		shell.getMenuBar().setVisible(false);

		addFileMenu(shell, Apps, version);
		addCommandMenu(shell, Apps);
		addTelemetryMenu(shell, Apps);
		addHelpMenu(shell, version);
		shell.getMenuBar().setVisible(true);
	}

	/**
	 * Adds the File menu to the primary MenuBar.
	 * 
	 * @param shell
	 *            The Shell to contain the File menu.
	 * @param Apps
	 *            The Apps to be contained.
	 * @param version
	 *            The version of this program.
	 */
	private static final void addFileMenu(final Shell shell, final App[] apps,
			final String version) {
		MenuItem fileMenuItem = MenuHelper.createMenuItem(shell.getMenuBar(),
				SWT.CASCADE, "&File", null, -1);

		Menu fileMenu = MenuHelper.createMenu(shell, SWT.DROP_DOWN,
				fileMenuItem, true);

		fillFileMenu(shell, fileMenu, apps, version);
	}

	/**
	 * Populates the File menu with "Exit" and "Add Receiver". Also includes
	 * "Populate Apps" if there are no Apps.
	 * 
	 * @param shell
	 *            The Shell to contain the File menu.
	 * @param fileMenu
	 *            The File menu.
	 * @param Apps
	 *            The Apps to be contained.
	 * @param version
	 *            The version of this program.
	 */
	private static final void fillFileMenu(final Shell shell,
			final Menu fileMenu, final App[] apps, final String version) {
		final MenuItem exitMenuItem = MenuHelper.createMenuItem(fileMenu,
				SWT.PUSH, "E&xit\tCtrl+x", null, SWT.CTRL + 'X');

		exitMenuItem.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				shell.close();
			}
		});

		final MenuItem receiverMenuItem = MenuHelper.createMenuItem(fileMenu,
				SWT.PUSH, "A&dd Receiver\tCtrl+r", null, SWT.CTRL + 'R');

		receiverMenuItem.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				RecieverPrompt.launchShell();
			}
		});

		if (apps == null) {
			final MenuItem populateAppsMenuItem = MenuHelper.createMenuItem(
					fileMenu, SWT.PUSH, "P&opulate Apps\tCtrl+p", null,
					SWT.CTRL + 'P');

			populateAppsMenuItem.addListener(SWT.Selection, new Listener() {
				@Override
				public void handleEvent(Event e) {
					try {
						PopulateAppsPrompt.launch(shell, version);

					} catch (ParserConfigurationException | SAXException
							| IOException e1) {
						e1.printStackTrace();
					}
				}
			});
		}
	}

	/**
	 * Adds the Telemetry menu to the primary MenuBar. This menu will be empty
	 * if there are no Apps.
	 * 
	 * @param shell
	 *            The Shell to contain the Telemetry menu.
	 * @param apps
	 *            The Apps to be contained.
	 */
	private static final void addTelemetryMenu(final Shell shell,
			final App[] apps) {
		final MenuItem telemetryMenuItem = MenuHelper.createMenuItem(
				shell.getMenuBar(), SWT.CASCADE, "&Telemetry", null, -1);
		final Menu telemetryMenu = MenuHelper.createMenu(shell, SWT.DROP_DOWN,
				telemetryMenuItem, true);

		if (apps != null)
			fillTelemetryMenu(shell, telemetryMenu, apps);
	}

	/**
	 * Populates the Telemetry menu with Apps. The Telemetry menu will be empty
	 * if there are no Apps, as is the case upon startup.
	 * 
	 * @param shell
	 *            The Shell to contain the Telemetry menu.
	 * @param telemetryMenu
	 *            The menu to add to.
	 * @param apps
	 *            The Apps to add.
	 */
	private static final void fillTelemetryMenu(final Shell shell,
			final Menu telemetryMenu, final App[] apps) {

		final char[] hotkeys = new char[] { '1', '2', '3', '4', '5', '6', '7',
				'8', '9' };
		int accel;

		for (int i = 0; i < apps.length; i++) {
			if (i < 9)
				accel = SWT.CTRL + hotkeys[i];
			else
				accel = SWT.NONE;

			String appMenuName = apps[i].getName().substring(0, 1)
					+ "&"
					+ apps[i].getName()
							.substring(1, apps[i].getName().length())
					+ "\tCtrl+" + (i + 1);

			final MenuItem menuItem = MenuHelper.createMenuItem(telemetryMenu,
					SWT.CASCADE, appMenuName, null, accel);

			menuItem.addListener(SWT.Selection,
					apps[i].getTelemetryListener(shell));
		}
	}

	/**
	 * Adds the Command menu to the primary MenuBar. This menu will be empty if
	 * there are no Apps.
	 * 
	 * @param shell
	 *            The Shell to contain the Command menu.
	 * @param apps
	 *            The Apps to be contained.
	 */
	private static final void addCommandMenu(final Shell shell, final App[] apps) {
		MenuItem commandMenuItem = MenuHelper.createMenuItem(
				shell.getMenuBar(), SWT.CASCADE, "&Command", null, -1);
		Menu commandMenu = MenuHelper.createMenu(shell, SWT.DROP_DOWN,
				commandMenuItem, true);

		if (apps != null)
			fillCommandMenu(shell, commandMenu, apps);
	}

	/**
	 * Adds the command submenus to the Command menu : one for each App. A
	 * submenu will be empty if the associated App has no commands.
	 * 
	 * @param shell
	 *            The Shell to contain the CommandMenu
	 * @param commandMenu
	 *            The menu to add to.
	 * @param apps
	 *            The Apps to add.
	 */
	private static final void fillCommandMenu(final Shell shell,
			final Menu commandMenu, final App[] apps) {
		for (App app : apps) {
			final MenuItem commandMenuItem = MenuHelper.createMenuItem(
					commandMenu, SWT.CASCADE, app.getName(), null, SWT.NONE);

			final Menu commandSubMenu = new Menu(shell, SWT.DROP_DOWN);
			commandMenuItem.setMenu(commandSubMenu);
			for (int i = 0; i < app.getCommandNames().length; i++) {
				final MenuItem commandSubMenuItem = MenuHelper.createMenuItem(
						commandSubMenu, SWT.PUSH, app.getCommandNames()[i],
						null, SWT.NONE);

				commandSubMenuItem.addListener(SWT.Selection,
						app.getCommandListener(i));
			}
		}
	}

	/**
	 * Adds the Help menu to the primary MenuBar. Includes selections for
	 * "Getting Started" and "About JCAT".
	 * 
	 * @param shell
	 *            The Shell to contain the Help menu.
	 * @param version
	 *            The version of this program.
	 */
	private static final void addHelpMenu(final Shell shell,
			final String version) {
		MenuItem helpMenuItem = MenuHelper.createMenuItem(shell.getMenuBar(),
				SWT.CASCADE, "&Help", null, -1);

		Menu helpMenu = MenuHelper.createMenu(shell, SWT.DROP_DOWN,
				helpMenuItem, true);

		fillHelpMenu(shell, helpMenu, version);
	}

	/**
	 * Populates the Help menu with selections of "Getting Started" and
	 * "About JCAT".
	 * 
	 * @param shell
	 *            The Shell containing the Help menu.
	 * @param helpMenu
	 *            The menu to add to.
	 * @param version
	 *            The version of this program.
	 */
	private static final void fillHelpMenu(final Shell shell,
			final Menu helpMenu, final String version) {
		final MenuItem gettingStartedMenuItem = MenuHelper.createMenuItem(
				helpMenu, SWT.PUSH, "G&etting Started", null, SWT.NONE);

		gettingStartedMenuItem.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				GettingStartedPrompt.launchShell();
			}
		});

		final MenuItem aboutMenuItem = MenuHelper.createMenuItem(helpMenu,
				SWT.PUSH, "A&bout JCAT", null, SWT.NONE);

		aboutMenuItem.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				AboutPrompt.launchShell(version);
			}
		});
	}
}