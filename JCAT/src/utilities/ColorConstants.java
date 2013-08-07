package utilities;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * FULLY DOCUMENTED. This class provides the Colors specifically referenced and copied by various
 * GUI classes.
 * 
 * @author Joe Benassi
 */
public final class ColorConstants {

	public static Color textColor = new Color(Display.getCurrent(), 50, 170,
			255);
	public static Color newBase = new Color(Display.getCurrent(), 0, 0, 0);
	public static final Color darkAccent = newBase;
	public static final Color borderColor = darkAccent;
	public static final Color textBoxColor = darkAccent;

	public static final Color eventWindowColor = new Color(
			Display.getCurrent(), newBase.getRed() + 30,
			newBase.getGreen() + 30, newBase.getBlue() + 30);

	public static final Color lightPageBackground = new Color(
			Display.getCurrent(), newBase.getRed() + 48,
			newBase.getGreen() + 48, newBase.getBlue() + 48);

	public static final Color[] panelColors = new Color[] { textColor, newBase,
			borderColor, newBase, lightPageBackground, textColor, newBase,
			lightPageBackground };

	public static Color[] panelColors2 = new Color[] { textColor, newBase,
			borderColor, newBase, lightPageBackground, textColor, newBase,
			lightPageBackground, lightPageBackground, lightPageBackground,
			lightPageBackground, lightPageBackground, lightPageBackground };

	/**
	 * Used to change the text color for various texts. Currently not used.
	 * 
	 * @param color
	 *            The color to set as the text color.
	 */
	public static final void changeText(Color color) {
		textColor = color;
	}

	/**
	 * Used to change the base color for various backgrounds. Currently not
	 * used.
	 * 
	 * @param color
	 *            The color to set as the base.
	 */
	public static final void changeBase(Color color) {
		newBase = color;
	}

	/**
	 * Disposes all the Color objects to clear memory. This is necessary.
	 */
	public static final void disposeColors() {
		textColor.dispose();
		newBase.dispose();
		textBoxColor.dispose();
		borderColor.dispose();
		lightPageBackground.dispose();
		eventWindowColor.dispose();
		darkAccent.dispose();

		for (Color c : panelColors)
			c.dispose();
		for (Color c : panelColors2)
			c.dispose();
	}
}