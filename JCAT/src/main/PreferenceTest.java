package main;

import java.util.prefs.Preferences;

public class PreferenceTest {
	private static volatile Preferences prefs = Preferences.userNodeForPackage(main.Launcher.class);
	private static final String x = "showHelp";
	
	public static final void showHelp() {
		prefs.remove(x);
	}
	
	public static final void dontShowHelp() {
		//if (shouldShowHelp())
		System.out.println("PREFERENCETEST: DON'T SHOW HELP!");
		prefs.putBoolean(x, false);
	}
	
	public static final boolean shouldShowHelp() {
		boolean show = prefs.getBoolean(x, true);
		if (show)
			return true;
		return false;
	}
}