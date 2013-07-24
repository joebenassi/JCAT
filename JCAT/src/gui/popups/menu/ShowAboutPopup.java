package gui.popups.menu;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import resources.ResourceLoader;
import utilities.ColorConstants;
import utilities.FontConstants;

public final class ShowAboutPopup {
	public static final void launchShell(final String version) {
		String title = "JCAT";
		String[] lines = new String[6];

		lines[0] = "Java Command and Telemetry (JCAT)";
		lines[1] = "Version " + version;
		lines[2] = "";
		lines[3] = "Created by NASA to aid cFE App";
		lines[4] = "development for both NASA and ";
		lines[5] = "non-NASA developers.";

		final Label[] labels = new Label[lines.length];
		
		Shell shell = new Shell(Display.getCurrent(), SWT.DIALOG_TRIM);
		FormLayout formLayout = new FormLayout();
		formLayout.marginBottom = 10;
		formLayout.marginTop = 10;
		formLayout.marginLeft = 10;
		formLayout.marginRight = 10;
		formLayout.spacing = 5;
		shell.setLayout(formLayout);
		shell.setText(title);
		shell.setImage(ResourceLoader.getSmallJCATLogo());
		FormData data = new FormData();
		
		final Image JCATLogo = ResourceLoader.getBigJCATLogo();
		Label JCATLogoLabel = new Label(shell, SWT.NONE);
		JCATLogoLabel.setImage(JCATLogo);
		data = new FormData();
		JCATLogoLabel.setLayoutData(data);

		Label spacer = new Label(shell, SWT.NONE);
		spacer.setBackground(ColorConstants.textBoxColor);
		data = new FormData();
		data.width = 4;
		data.left = new FormAttachment(JCATLogoLabel, 0);
		data.top = new FormAttachment(0, -2);
		data.bottom = new FormAttachment(100, 2);
		spacer.setLayoutData(data);
		
		for (int i = 0; i < lines.length; i++)
		{
			labels[i] = new Label(shell, SWT.NONE);
			labels[i].setText(lines[i]);
			
			data = new FormData();
			if (i == 0)
				labels[i].setFont(FontConstants.headerFont);
			else
				labels[i].setFont(FontConstants.bodyFont);
			if (i > 0)
				data.top = new FormAttachment(labels[i-1], -4);
			if (i == lines.length - 1)
				data.bottom = new FormAttachment(100, 0);
				data.left = new FormAttachment(spacer, 0);
			
			data.right = new FormAttachment(100, 0);
			labels[i].setLayoutData(data);
			shell.pack();
		}
		
		shell.pack();
		shell.open();

		shell.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				JCATLogo.dispose();
			}
		});
	}
}
