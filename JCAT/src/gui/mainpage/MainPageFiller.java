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
	private final Font timeFont = FontConstants.monospacedFont;
	private final Font monospacedFont = FontConstants.monospacedFont;
	private final Image JCATLogo = ResourceLoader.largeJCATLogo;
	private final String[] firstEventMessage = new String[] { "CFS TIME: ",
			"SOURCE: ", "CFS EVENT MESSAGE: " };
	private final String[] firstUserActivity = new String[] { "ELAPSED TIME: ",
			"SOURCE: ", "USER ACTIVITY: " };
	private final Color sashBackgroundColor = ColorConstants.eventWindowColor;
	final Color titleBackgroundColor = ColorConstants.darkAccent;
	final Color titleForegroundColor = ColorConstants.textColor;

	public MainPageFiller(Shell shell) {
		shell.setText("JCAT: JAVA COMMAND AND TELEMETRY");
		shell.setLayout(new GridLayout(1, false));

		TopBar.addTopBar(shell, "Java Command and Telemetry",
				titleBackgroundColor, titleForegroundColor, titleFont,
				timeFont, JCATLogo);

		statusWindowSash = new StatusWindowSash(shell, sashBackgroundColor,
				monospacedFont);

		addEventMessage(firstEventMessage[0], firstEventMessage[1],
				firstEventMessage[2], titleForegroundColor);

		addUserActivity(firstUserActivity[0], firstUserActivity[1],
				firstUserActivity[2], titleForegroundColor);

		BottomBar.addBottomBar(shell, titleBackgroundColor);
	}

	private void addUserActivity(String time, String header, String activity,
			Color color) {
		statusWindowSash.addUserActivity(time, header, activity, color);
	}

	public final void addUserActivity(String time, String activity, Color color) {
		statusWindowSash.addUserActivity(time, activity, color);
	}

	public final void addEventMessage(String time, String config,
			String msgStr, Color color) {
		statusWindowSash.addEventMessage(time, config, msgStr, color);
	}
}