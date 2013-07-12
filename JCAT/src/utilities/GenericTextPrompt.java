package utilities;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class GenericTextPrompt {
	private static final ArrayList<Label> labels = new ArrayList<Label>();
	private static Shell shell;
	private static Shell dialog;

	public static final void launchShell(String title, String[] textLines) {
		stepOne(title, textLines[0]);
		for (int i = 1; i < textLines.length; i++)
			addLine(textLines[i]);

		stepThree(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
	}

	private static final void stepThree(SelectionAdapter selectionAdapter) {
		addOkayButton(selectionAdapter);
		dialog.pack();
		dialog.open();

		while (true)
			try{
			if (!Display.getCurrent().readAndDispatch())
				Display.getCurrent().sleep();}
		catch(Throwable e){}
	}

	private static final void stepOne(String title, String lineOne) {
		shell = new Shell(Display.getCurrent());
		dialog = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		dialog.setText(title);
		FormLayout formLayout = new FormLayout();
		formLayout.marginWidth = 10;
		formLayout.marginHeight = 10;
		formLayout.spacing = 3;
		dialog.setLayout(formLayout);

		Label line1 = new Label(dialog, SWT.NONE);
		line1.setText(lineOne);
		FormData data = new FormData();
		line1.setLayoutData(data);
		labels.add(line1);
	}

	private static final void addOkayButton(SelectionAdapter selectionAdapter) {
		Button okayButton = new Button(dialog, SWT.PUSH);

		okayButton.addSelectionListener(selectionAdapter);

		okayButton.setText("OK");
		FormData data = new FormData();
		data.width = 75;
		data.top = new FormAttachment(labels.get(labels.size() - 1), 0,
				SWT.DEFAULT);
		data.bottom = new FormAttachment(100, 0);
		data.right = new FormAttachment(100, 0);
		okayButton.setLayoutData(data);
	}

	private static final void addLine(String text) {
		labels.add(getUntabbedLabel(dialog, labels.get(labels.size() - 1), text));
	}

	private static final Label getUntabbedLabel(Shell shell, Label labelAbove,
			String text) {
		Label temp = new Label(shell, SWT.NONE);
		temp.setText(text);
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(labelAbove, 0, SWT.DEFAULT);
		temp.setLayoutData(data);

		return temp;
	}
}
