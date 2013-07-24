package gui.popups.menu;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.xml.sax.SAXException;

public final class NavAppXMLPrompt {
	public static final void launch(final Shell shell,
			final String version) throws ParserConfigurationException,
			SAXException, IOException {
		FileDialog dialog = new FileDialog(shell, SWT.MULTI | SWT.OPEN);
		dialog.setText("Select App Profiles");
		dialog.setFilterExtensions(new String[] { "*.xml" });
		dialog.setFilterPath("c:\\temp");

		String fullFilePath = dialog.open();
		String[] fileNames = dialog.getFileNames();

		populateApps(shell, fullFilePath, fileNames, version);
	}

	private static final void populateApps(final Shell shell,
			final String fullFilePath, final String[] fileNames, final String version) throws ParserConfigurationException,
			SAXException, IOException {
		
		String directory;
		if (fullFilePath != null) {
			directory = fullFilePath.substring(0, fullFilePath.length()
					- fileNames[0].length());
		} else
			return;

		File[] files = new File[fileNames.length];

		for (int i = 0; i < fileNames.length; i++) {
			files[i] = new File(directory + fileNames[i]);
		}

		ChooseConfigsPrompt.launch(files, shell);
	}
}