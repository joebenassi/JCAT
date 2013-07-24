package resources;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ResourceLoader {
	static ResourceLoader rl = new ResourceLoader();

	public static final Image getMedJCATLogo() {
		return getImage("JCATLogoMed.png");
	}
	
	public static final Image getSmallJCATLogo() {
		return getImage("JCATLogoSmall.png");
	}
	
	public static final Image getBigJCATLogo() {
		return getImage("JCATLogoBig.png");
	}

	public static final Image getNASALogo() {
		return getImage("NasaLogo.png");
	}

	public static final Image getLargeJCATLogo() {
		return getImage("JCATLogoLarge.png");
	}
	/**
	 * Returns the image that has the file name fileName, fileName.png, or
	 * fileName.jpg
	 * 
	 * @param fileName
	 *            the name of the file
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

	public static final File[] getCFEFiles()
	{
		return getXMLsFromFolder();
	}
	
	public static final File[] getXMLsFromFolder()
	{
		String folderPath = rl.getClass().getResource("/XMLs/").getFile();
		File folder = new File(folderPath);
		File[] files = folder.listFiles();
		ArrayList<File> fileArray = new ArrayList<File>();
		for (int i = 0; i < files.length; i++)
			if (files[i].isFile())
				fileArray.add(files[i]);
		
		return fileArray.toArray(new File[fileArray.size()]);
	}
}
