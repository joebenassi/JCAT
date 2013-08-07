package utilities;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

/**
 * FULLY DOCUMENTED. This class provides the Fonts specifically referenced and
 * copied by various GUI classes.
 * 
 * @author Joe Benassi
 */
public final class FontConstants {

	public static final Font headerFont = new Font(Display.getCurrent(),
			new FontData("arial", 11, SWT.BOLD));
	public static final Font titleFont = new Font(Display.getCurrent(),
			"arial", 21, SWT.NONE);
	public static final Font monospacedFont = new Font(Display.getCurrent(),
			"courier", 13, SWT.BOLD);
	public static final Font bodyFont = new Font(Display.getCurrent(), "arial",
			11, SWT.NONE);

	/**
	 * Disposes the Fonts to clear memory. This is necessary.
	 */
	public static final void disposeFonts() {
		headerFont.dispose();
		titleFont.dispose();
		monospacedFont.dispose();
		bodyFont.dispose();
	}
}
