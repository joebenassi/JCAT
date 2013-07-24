package utilities;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * This class provides the Colors specifically referenced and copied by
 * gui.mainpage.MainPageFiller and gui.popup.PopupFiller, though they are used
 * in most of the GUI classes through parameter-passing by
 * gui.mainpage.MainPageFiller and gui.popup.PopupFiller.
 * 
 * @author Joe Benassi
 * 
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
		lightPageBackground, lightPageBackground, lightPageBackground, lightPageBackground, lightPageBackground, lightPageBackground};
	// public static Color textGray10 =
	// public static Color baseGray20 =
	// public static Color baseGray30 = new Color(Display.getCurrent(),
	// base.getRed() + 30, base.getGreen() + 30, base.getBlue() + 30);
	// public static Color baseGray52 = new Color(Display.getCurrent(),
	// base.getRed() + 52, base.getGreen() + 52, base.getBlue() + 52);

	/**
	 * Used to change the text color for various texts.
	 * 
	 * @param color
	 *            the color to change the text
	 */
	public static final void changeText(Color color) {
		textColor = color;
	}

	/**
	 * Used to change the base color for various backgrounds.
	 * 
	 * @param color
	 *            the color to change the base
	 */
	public static final void changeBase(Color color) {
		newBase = color;
	}

	/**
	 * Dispenses the Colors to clear memory. This is necessary.
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