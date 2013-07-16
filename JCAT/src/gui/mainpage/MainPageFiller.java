package gui.mainpage;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;

import resources.ResourceLoader;
import utilities.ColorConstants;
import utilities.FontConstants;

public class MainPageFiller {
	private static StatusWindowSash statusWindowSash;
	private final Font titleFont = FontConstants.titleFont;
	private final Font timeFont = FontConstants.timeFont;
	private final Font monospacedFont = FontConstants.monospacedFont;
	private final Image nasaLogo = ResourceLoader.getNASALogo();
	private final String[] firstEventMessage = new String[] {
			"123:14:12-12-12", "EVENT MESSAGES STREAMING...", "" };
	private final String[] firstUserActivity = new String[] {
			"123:14:12-12-12", "USER ACTIVITY STREAMING..." };

	public MainPageFiller(Shell shell) {
		// this.colorConstants = colorConstants;

		final Color sashBackgroundColor = ColorConstants.baseGray30;
		final Color titleBackgroundColor = ColorConstants.base;
		Color titleForegroundColor = ColorConstants.textGray10;

		shell.setText("JCAT: JAVA COMMAND AND TELEMETRY");
		shell.setLayout(new GridLayout(1, false));

		TopBar.addTopBar(shell, "Java Command and Telemetry",
				titleBackgroundColor, titleForegroundColor, titleFont,
				timeFont, nasaLogo);

		statusWindowSash = new StatusWindowSash(shell, sashBackgroundColor,
				monospacedFont);

		addEventMessage(firstEventMessage[0], firstEventMessage[1],
				titleForegroundColor);
		addUserActivity(firstUserActivity[0], firstUserActivity[1],
				titleForegroundColor);

		CommandLine.addCommandLine(shell, titleForegroundColor,
				titleBackgroundColor, monospacedFont);
	}

	public final void addUserActivity(String time, String activity, Color color) {
		statusWindowSash.addUserActivity(time, activity, color);
	}

	public final void addEventMessage(String time, String detail1, Color color) {
		statusWindowSash.addEventMessage(time, detail1, color);
	}
}