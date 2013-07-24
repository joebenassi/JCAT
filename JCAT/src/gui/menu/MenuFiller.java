package gui.menu;

import gui.popups.menu.ShowAboutPopup;
import gui.popups.menu.IOPrompt;
import gui.popups.menu.HelpPopup;
import gui.popups.menu.NavAppXMLPrompt;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import main.Launcher;
import network.Networker;

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
	 * @param shell
	 *            The Shell to add the menu to.
	 * @param Apps
	 *            The Apps to add to the menu.
	 * @param version
	 *            The version of this program.
	 */
	public static final void addMenu(final Shell shell, App[] Apps,
			final String version) {
		if (shell.getMenuBar()!= null){
			//shell.
		}
		shell.setMenuBar(MenuHelper.createMenu(shell, SWT.BAR
				| SWT.LEFT_TO_RIGHT));
		shell.getMenuBar().setVisible(true);
		addFileMenu(shell, Apps, version);
		addCommandMenu(shell, Apps);
		addTelemetryMenu(shell, Apps);
		addConfigureMenu(shell);
		addHelpMenu(shell, version);
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
		if (apps == null) {
			final MenuItem importAppsMenuItem = MenuHelper.createMenuItem(
					fileMenu, SWT.PUSH, "I&mport App Profiles\tCtrl+I", null,
					SWT.CTRL + 'I');

			importAppsMenuItem.addListener(SWT.Selection, new Listener() {
				@Override
				public void handleEvent(Event e) {
					try {
						NavAppXMLPrompt.launch(shell, version);

					} catch (ParserConfigurationException | SAXException
							| IOException e1) {
						e1.printStackTrace();
					}
				}
			});
		}

		final MenuItem enableTelemetryMenuItem = MenuHelper.createMenuItem(fileMenu,
				SWT.PUSH, "E&nable Telemetry", null, SWT.NONE);

		if (apps == null) enableTelemetryMenuItem.setEnabled(false);
		enableTelemetryMenuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				Networker.enableToLabTelemetry();
			}
		});
		/*
		final MenuItem enableAppTelemetry = MenuHelper.createMenuItem(fileMenu,
				SWT.PUSH, "E&nable App Telemetry", null, SWT.NONE);

		if (apps == null) enableAppTelemetry.setEnabled(false);
		enableAppTelemetry.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				Networker.configTOLab();
			}
		});
		*/
		final MenuItem restartMenuItem = MenuHelper.createMenuItem(fileMenu,
				SWT.PUSH, "R&estart\tCtrl+R", null, SWT.CTRL + 'R');

		restartMenuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				Launcher.restartApplication(shell);
			}
		});

		final MenuItem exitMenuItem = MenuHelper.createMenuItem(fileMenu,
				SWT.PUSH, "E&xit\tCtrl+X", null, SWT.CTRL + 'X');

		exitMenuItem.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				shell.close();
			}
		});
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
		if (apps == null)
			commandMenuItem.setEnabled(false);
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
		if (apps == null)
			telemetryMenuItem.setEnabled(false);
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
			String displayedAccel = "";
			if (i < 9) {
				accel = SWT.CTRL + hotkeys[i];
				displayedAccel = "\tCtrl+" + (i + 1);
			} else
				accel = SWT.NONE;

			String appMenuName = apps[i].getName().substring(0, 1)
					+ "&"
					+ apps[i].getName()
							.substring(1, apps[i].getName().length())
					+ displayedAccel;

			final MenuItem menuItem = MenuHelper.createMenuItem(telemetryMenu,
					SWT.CASCADE, appMenuName, null, accel);

			menuItem.addListener(SWT.Selection,
					apps[i].getTelemetryListener(shell));
		}
	}

	public static final void addConfigureMenu(final Shell shell) {
		MenuItem configureMenuItem = MenuHelper.createMenuItem(
				shell.getMenuBar(), SWT.CASCADE, "&Configure", null, -1);

		Menu configureMenu = MenuHelper.createMenu(shell, SWT.DROP_DOWN,
				configureMenuItem, true);

		fillConfigureMenu(shell, configureMenu);
	}

	public static final void fillConfigureMenu(final Shell shell,
			final Menu configureMenu) {
		final MenuItem outputMenuItem = MenuHelper.createMenuItem(
				configureMenu, SWT.PUSH, "O&utput\tCtrl+O", null,
				SWT.CTRL + 'O');

		outputMenuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				IOPrompt.launchOutputPrompt();
			}
		});

		final MenuItem inputMenuItem = MenuHelper.createMenuItem(configureMenu,
				SWT.PUSH, "I&nput\tCtrl+N", null, SWT.CTRL + 'N');

		inputMenuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				IOPrompt.launchInputPrompt();
			}
		});
		/*
		final MenuItem colorSchemeMenuItem = MenuHelper.createMenuItem(
				configureMenu, SWT.PUSH, "C&olor Scheme", null, SWT.NONE);

		colorSchemeMenuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				// TODO
			}
		});*/
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
				HelpPopup.GettingStarted.launch();
			}
		});

		final MenuItem importingNewAppsMenuItem = MenuHelper.createMenuItem(
				helpMenu, SWT.PUSH, "I&mporting New Apps", null, SWT.NONE);

		importingNewAppsMenuItem.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				HelpPopup.ImportingNewApps.launch();
			}
		});

		final MenuItem commonProblemsMenuItem = MenuHelper.createMenuItem(
				helpMenu, SWT.CASCADE, "C&ommon Problems", null, SWT.NONE);

		Menu commonProblemsMenu = MenuHelper.createMenu(shell, SWT.DROP_DOWN,
				commonProblemsMenuItem, true);

		fillCommonProblemsMenu(shell, commonProblemsMenu);

		final MenuItem aboutMenuItem = MenuHelper.createMenuItem(helpMenu,
				SWT.PUSH, "A&bout JCAT", null, SWT.NONE);

		aboutMenuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				ShowAboutPopup.launchShell(version);
			}
		});
	}

	private static final void fillCommonProblemsMenu(final Shell shell,
			final Menu commonProblemsMenu) {
		final MenuItem importAppsMenuItem = MenuHelper.createMenuItem(
				commonProblemsMenu, SWT.PUSH, "A&pps Not Importing", null,
				SWT.NONE);

		importAppsMenuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				HelpPopup.AppsNotImporting.launch();
			}
		});

		final MenuItem eventWindowMenuItem = MenuHelper.createMenuItem(
				commonProblemsMenu, SWT.PUSH, "E&mpty Event Window", null,
				SWT.NONE);

		eventWindowMenuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				HelpPopup.EmptyEventWindow.launch();
			}
		});

		final MenuItem commandMenuItem = MenuHelper
				.createMenuItem(commonProblemsMenu, SWT.PUSH,
						"C&ommand Errors", null, SWT.NONE);

		commandMenuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				HelpPopup.CommandErrors.launch();
			}
		});

		final MenuItem noTelemetryMenuItem = MenuHelper.createMenuItem(
				commonProblemsMenu, SWT.PUSH, "N&o Telemetry", null, SWT.NONE);

		noTelemetryMenuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				HelpPopup.NoTelemetry.launch();
			}
		});

		final MenuItem freezesMenuItem = MenuHelper.createMenuItem(
				commonProblemsMenu, SWT.PUSH, "P&rogram Freezes", null,
				SWT.NONE);

		freezesMenuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				HelpPopup.ProgramFreezes.launch();
			}
		});
	}
}