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

	public static Color text = new Color(Display.getCurrent(), 50, 170, 255);
	public static Color base = new Color(Display.getCurrent(), 0, 0, 0);
	public static Color textGray10 = new Color(Display.getCurrent(),
			text.getRed() - 10, text.getGreen() - 10, text.getBlue() - 10);
	public static Color baseGray20 = new Color(Display.getCurrent(),
			base.getRed() + 20, base.getGreen() + 20, base.getBlue() + 20);
	public static Color baseGray30 = new Color(Display.getCurrent(),
			base.getRed() + 30, base.getGreen() + 30, base.getBlue() + 30);
	public static Color baseGray52 = new Color(Display.getCurrent(),
			base.getRed() + 52, base.getGreen() + 52, base.getBlue() + 52);
	public static final Color[] panelColors = new Color[] { textGray10, base,
			baseGray20, base, baseGray30, textGray10, base, textGray10 };

	/**
	 * Used to change the text color for various texts.
	 * 
	 * @param color
	 *            the color to change the text
	 */
	public static final void changeText(Color color) {
		text = color;
	}

	/**
	 * Used to change the base color for various backgrounds.
	 * 
	 * @param color
	 *            the color to change the base
	 */
	public static final void changeBase(Color color) {
		base = color;
	}

	/**
	 * Dispenses the Colors to clear memory. This is necessary.
	 */
	public static final void disposeColors() {
		textGray10.dispose();
		text.dispose();
		base.dispose();
		baseGray30.dispose();
		baseGray52.dispose();
		baseGray20.dispose();
		
		for (Color c : panelColors)
			c.dispose();
	}
}