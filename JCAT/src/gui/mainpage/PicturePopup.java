package gui.mainpage;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import resources.ResourceLoader;

/**
 * IMPROPERLY JAVADOC'D. CURRENTLY UNUSED.
 * @author Joe Benassi
 *
 */
public class PicturePopup
{
	private static final int minimumWidth = 250;
	
	public static void showImage(String title, Image image)
	{
		Display display = Display.getCurrent();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setText(title);
		final Label label = new Label(shell, SWT.NONE);
		label.setImage(image);

		shell.pack();
		if(shell.getSize().x < minimumWidth)
			shell.setSize(new Point(minimumWidth, shell.getSize().y));
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		image.dispose();
		display.dispose();
	}
	
	public static void showImage(String title, String fileName) {
		showImage(title, ResourceLoader.getImage(fileName));
	}

	public static void main(String[] args) {
		showImage("TITLEHERE", "SHANGHAI");
	}
}