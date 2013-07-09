package gui.menu.prompts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class GenericInputPrompt {
	static final void launchShell(final Shell dialog, int width, String title,
			String[] names, Text[] textBoxes,
			SelectionListener selectionListener) {
		dialog.setText(title);
		FormLayout formLayout = new FormLayout();
		formLayout.marginWidth = 10;
		formLayout.marginHeight = 10;
		formLayout.spacing = 10;
		dialog.setLayout(formLayout);
		
		GridLayout gridLayout = new GridLayout(2, true);
		gridLayout.horizontalSpacing = 14;
		dialog.setLayout(gridLayout);
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = SWT.FILL;
		dialog.setData(gridData);

		Label[] labels = new Label[names.length];
		for (int i = 0; i < names.length; i++) {
			labels[i] = new Label(dialog, SWT.NONE);
			labels[i].setText(names[i]);
			gridData = new GridData();
			gridData.grabExcessHorizontalSpace = true;
			gridData.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
			gridData.horizontalIndent = 5;
			labels[i].setLayoutData(gridData);
			
			textBoxes[i] = new Text(dialog, SWT.BORDER);
			gridData = new GridData();
			gridData.horizontalAlignment = GridData.HORIZONTAL_ALIGN_END;
			textBoxes[i].setLayoutData(gridData);
		}

		Button cancel = new Button(dialog, SWT.PUSH);
		cancel.setText("Cancel");
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dialog.close();
			}
		});

		Button ok = new Button(dialog, SWT.PUSH);
		ok.setText("OK");
		//ok.
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.HORIZONTAL_ALIGN_CENTER;
		ok.setLayoutData(gridData);
		
		ok.addSelectionListener(selectionListener);
		
		dialog.pack();
		dialog.open();
		while (true)
			if (!Display.getCurrent().readAndDispatch())
				Display.getCurrent().sleep();
	}
}
