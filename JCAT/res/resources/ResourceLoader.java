package resources;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ResourceLoader {
	private static final ResourceLoader rl = new ResourceLoader();

	private static final Image medJCATLogo = getImage("JCATLogoMed.png");
	private static final Image smallJCATLogo = getImage("JCATLogoSmall.png");
	public static final Image bigJCATLogo = getImage("JCATLogoBig.png");
	public static final Image largeJCATLogo = getImage("JCATLogoLarge.png");
	private static final Image veryBigJCATLogo = getImage("JCATLogoVeryBig");
	private static final Image[] images = new Image[] { smallJCATLogo,
			medJCATLogo, bigJCATLogo, veryBigJCATLogo };

	/**
	 * Returns the image that has the file name fileName, fileName.png, or
	 * fileName.jpg
	 * 
	 * @param fileName
	 *            the name of the file
	 * @return the image with that name
	 */
	private static final Image getImage(String fileName) {
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

	/*
	 * private static Document readInputStream(InputStream response) throws
	 * SAXException, IOException, ParserConfigurationException {
	 * 
	 * DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
	 * DocumentBuilder docBuilder = (DocumentBuilder)
	 * docBF.newDocumentBuilder(); InputSource inSource = new InputSource(new
	 * InputStreamReader(response, "UTF-8")); Document respDoc =
	 * docBuilder.parse(inSource); return respDoc; }
	 */
	public static final void disposeImages() {
		medJCATLogo.dispose();
		smallJCATLogo.dispose();
		bigJCATLogo.dispose();
		largeJCATLogo.dispose();
		for (Image i : images) {
			i.dispose();
		}
	}

	public static final Image[] getImages() {
		return images;
	}
}