package utilities;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

/**
 * This class provides the Fonts specifically referenced and copied by
 * gui.mainpage.MainPageFiller and gui.popup.PopupFiller, though they are used
 * in most of the GUI classes through parameter-passing by
 * gui.mainpage.MainPageFiller and gui.popup.PopupFiller.
 * 
 * @author Joe Benassi
 * 
 */
public final class FontConstants {

	public static final Font dialogFont = new Font(Display.getCurrent(),
			new FontData("Fixedsys Regular", 12, SWT.BOLD));
	public static final Font titleFont = new Font(Display.getCurrent(),
			"arial", 21, SWT.NONE);
	public static final Font popupTitleFont = new Font(Display.getCurrent(),
			"Arial", 20, SWT.NONE);
	public static final Font timeFont = new Font(Display.getCurrent(), "Arial",
			10, SWT.NONE);
	public static final Font monospacedFont = new Font(Display.getCurrent(),
			"courier", 13, SWT.BOLD);
	public static final Font groupFont = new Font(Display.getCurrent(),
			"arial", 16, SWT.BOLD);

/**
 * Disposes the Fonts to clear memory. This is necessary.
 */
	public static final void disposeFonts() {
		dialogFont.dispose();
		titleFont.dispose();
		popupTitleFont.dispose();
		timeFont.dispose();
		monospacedFont.dispose();
		groupFont.dispose();
	}
}