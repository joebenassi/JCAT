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

public final class NavConstantXMLPrompt {
	public static final boolean launch() {
		FileDialog dialog = new FileDialog(new Shell(Display.getCurrent()),
				SWT.MULTI | SWT.OPEN | SWT.ON_TOP);
		dialog.setText("Select Constant Definition File");
		dialog.setFilterExtensions(new String[] { "*.xml" });
		dialog.setFilterPath("c:\\temp");

		String fullFilePath = dialog.open();
		String[] fileNames = dialog.getFileNames();
		if (fileNames.length < 1) return false;
		try {
		addConstants(fullFilePath, fileNames);
		} catch (Throwable e){}
		return true;
	}

	private static final void addConstants(final String fullFilePath,
			final String[] fileNames) throws ParserConfigurationException,
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