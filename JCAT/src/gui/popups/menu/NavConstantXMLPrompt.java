package gui.popups.menu;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.xml.sax.SAXException;

import utilities.XMLParser;

public final class NavConstantXMLPrompt{
	public static final void launch()
			throws ParserConfigurationException,
			SAXException, IOException {
		FileDialog dialog = new FileDialog(new Shell(Display.getCurrent()), SWT.MULTI | SWT.OPEN);
		dialog.setText("Select Constant File");
		dialog.setFilterExtensions(new String[] { "*.xml" });
		dialog.setFilterPath("c:\\temp");

		String fullFilePath = dialog.open();
		String[] fileNames = dialog.getFileNames();
		addConstants(fullFilePath, fileNames);
	}

	private static final void addConstants(
			final String fullFilePath, final String[] fileNames) throws ParserConfigurationException,
			SAXException, IOException {
		String directory;
		if (fullFilePath != null) {
			directory = fullFilePath.substring(0, fullFilePath.length()
					- fileNames[0].length());
		} else
			return;

		for (int i = 0; i < fileNames.length; i++) {
			XMLParser.addConstants(new File(directory + fileNames[i]));
		}
	}
}