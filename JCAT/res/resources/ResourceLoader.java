package resources;

import java.io.File;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ResourceLoader {
	static ResourceLoader rl = new ResourceLoader();
	
	public static final Image getJCATLogo()
	{
		return getImage("JCATLogo");
	}
	
	public static final Image getNASALogo()
	{
		return getImage("NasaLogo.png");
	}
	
	/**
	 * Returns the image that has the file name fileName, fileName.png, or fileName.jpg
	 * @param fileName the name of the file
	 * @return the image with that name
	 */
	public static final Image getImage(String fileName) {
		Image image;
		try {
			image = new Image(Display.getCurrent(), rl.getClass()
					.getClassLoader().getResourceAsStream("Images/" + fileName));

		} catch (Throwable e) {

			try {
				image = new Image(Display.getCurrent(), rl.getClass()
						.getClassLoader()
						.getResourceAsStream("Images/" + fileName + ".png"));
			} catch (Throwable e1) {

				try {
					image = new Image(Display.getCurrent(), rl.getClass()
							.getClassLoader()
							.getResourceAsStream("Images/" + fileName + ".jpg"));
				} catch (Throwable e2) {
					return new Image(Display.getCurrent(), 100, 100);
				}
			}
		}
		return image;
	}

	public static File getXML(String fileName) {
		return new File("XMLs/" + fileName);
	}
}
