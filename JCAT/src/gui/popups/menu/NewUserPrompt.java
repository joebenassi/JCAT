package gui.popups.menu;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import resources.ResourceLoader;
import utilities.PreferenceStorage;

public class NewUserPrompt {
	public static boolean showAgain = true;

	public static void main(String[] args) {
		Display display = new Display();
		launch();
	}

	public static final void launch() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				final Shell shell = new Shell(Display.getCurrent(),
						SWT.DIALOG_TRIM | SWT.ON_TOP);
				shell.setImage(ResourceLoader.getSmallJCATLogo());
				shell.setText("Welcome");
				FormLayout formLayout = new FormLayout();
				formLayout.marginWidth = 10;
				formLayout.marginHeight = 10;
				shell.setLayout(formLayout);

				FormData data;

				Label infoLabel = new Label(shell, SWT.NONE);
				infoLabel.setImage(Display.getDefault().getSystemImage(
						SWT.ICON_INFORMATION));
				data = new FormData();
				data.left = new FormAttachment(0, 20);
				data.top = new FormAttachment(0, 0);
				infoLabel.setLayoutData(data);

				Composite titleBox = new Composite(shell, SWT.NONE);
				titleBox.setLayout(new GridLayout(1, true));
				data = new FormData();
				data.left = new FormAttachment(infoLabel, 15, SWT.RIGHT);
				data.right = new FormAttachment(100, -30);
				data.top = new FormAttachment(0, 0);
				data.width = 250;
				// data.height = 50;
				titleBox.setLayoutData(data);

				// StyledText title = new StyledText(titleBox, SWT.WRAP);
				Label title = new Label(titleBox, SWT.WRAP);
				title.setText("If you're new to JCAT, you should follow the setup procedure at Help > Getting Started");
				GridData gData = new GridData();
				gData.grabExcessHorizontalSpace = true;
				gData.horizontalAlignment = SWT.CENTER;
				title.setLayoutData(gData);

				final Button neverShowAgain = new Button(shell, SWT.CHECK);
				neverShowAgain.setText("Don't show this again");
				data = new FormData();
				data.left = new FormAttachment(infoLabel, 8, SWT.RIGHT);
				data.top = new FormAttachment(titleBox, 20, 0);
				data.width = 150;
				neverShowAgain.setLayoutData(data);
				neverShowAgain.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(final SelectionEvent e) {
						showAgain = !showAgain;
					}
				});

				Button okayB = new Button(shell, SWT.NONE);
				okayB.setText("OK");
				data = new FormData();
				data.top = new FormAttachment(neverShowAgain, 0, 0);
				data.bottom = new FormAttachment(100, 0);
				data.right = new FormAttachment(100, 0);
				data.width = 120;
				okayB.setLayoutData(data);

				okayB.addListener(SWT.Selection, new Listener() {
					@Override
					public void handleEvent(Event event) {
						if (!showAgain) {
							PreferenceStorage.dontShowHelp();
						}
						shell.close();
					}
				});

				shell.pack();
				shell.open();
			}
		});
	}
}
