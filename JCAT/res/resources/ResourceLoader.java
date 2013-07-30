package resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ResourceLoader {	
	private static final ResourceLoader rl = new ResourceLoader();

	public static final Image medJCATLogo = getImage("JCATLogoMed.png");
	public static final Image smallJCATLogo = getImage("JCATLogoSmall.png");
	public static final Image bigJCATLogo = getImage("JCATLogoBig.png");
	public static final Image largeJCATLogo = getImage("JCATLogoLarge.png");

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

	public static final Document getSettingsDocument() throws SAXException, IOException, ParserConfigurationException
	{
		InputStream stream = ClassLoader.class.getResourceAsStream("/XMLs/Settings.xml");
		return readInputStream(stream);
	}
	
	private static Document readInputStream(InputStream response) throws SAXException, IOException, ParserConfigurationException {

		  DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
		  DocumentBuilder docBuilder = (DocumentBuilder) docBF.newDocumentBuilder();
		  InputSource inSource = new InputSource(new InputStreamReader(response, "UTF-8"));
		  Document respDoc = docBuilder.parse(inSource);
		  return respDoc;
	}
	
	public static final void disposeImages() {
		medJCATLogo.dispose();
		smallJCATLogo.dispose();
		bigJCATLogo.dispose();
		largeJCATLogo.dispose();
	}
}