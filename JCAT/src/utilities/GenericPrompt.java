package utilities;

import gui.popups.menu.IOPrompt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import resources.ResourceLoader;

/**
 * NOT DOCUMENTED.
 * 
 * @author Joe Benassi
 */
public class GenericPrompt {
	public static final void addLabel(Shell dialog, String text) {
		Label label = new Label(dialog, SWT.NONE);
		label.setText(text);
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
		gridData.horizontalIndent = 5;
		label.setLayoutData(gridData);
	}

	public static final void addFirstLine(Shell dialog, String text) {
		Label label = new Label(dialog, SWT.NONE);

		label.setText(text);
		label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 2,
				1));

		final Font titleFont = new Font(Display.getCurrent(), new FontData(
				label.getFont().getFontData()[0].getName(), 10, SWT.NONE));
		label.setFont(titleFont);

		dialog.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				titleFont.dispose();
			}
		});

	}

	public static final Text getText(Shell dialog) {
		Text text = new Text(dialog, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		return text;
	}

	public static final Combo getCombo(Shell dialog, String[] options) {
		Combo combo = new Combo(dialog, SWT.READ_ONLY);
		combo.setItems(options);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		return combo;
	}

	public static final Shell getMainShell() {
		Shell mainShell = new Shell(Display.getCurrent(), SWT.RESIZE | SWT.DIALOG_TRIM | SWT.BORDER_DASH);
		mainShell.setImages(ResourceLoader.getImages());
		
		return mainShell;
	}
	
	public static final Shell getGenericShell() {
		Shell parent = new Shell(Display.getCurrent(), SWT.ON_TOP);
		ShellDisposer.queueForDisposal(parent);
		
		Shell child = new Shell(parent, SWT.RESIZE | SWT.DIALOG_TRIM | SWT.BORDER_DASH);
		child.setImages(ResourceLoader.getImages());
		return child;
	}

	public static final void launchShell(final Shell dialog, String shellTitle,
			SelectionListener selectionListener) {
		dialog.setText(shellTitle);
		dialog.setLayout(new GridLayout(2, false));

		dialog.pack();
		Composite composite = new Composite(dialog, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2,
				1));

		FormLayout formLayout = new FormLayout();
		formLayout.marginWidth = 6;
		formLayout.marginHeight = 5;
		formLayout.spacing = 10;
		composite.setLayout(formLayout);

		Button cancel = new Button(composite, SWT.PUSH);
		cancel.setText("Cancel");
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dialog.close();
			}
		});
		FormData formData = new FormData();
		formData.width = dialog.getSize().x / 3;
		cancel.setLayoutData(formData);

		Button ok = new Button(composite, SWT.PUSH);
		ok.setText("Send");
		ok.addSelectionListener(selectionListener);

		formData = new FormData();
		formData.right = new FormAttachment(100, 0);
		formData.width = dialog.getSize().x / 3;
		ok.setLayoutData(formData);
		ok.setFocus();

		dialog.pack();
		dialog.open();
		while (true)
			if (!Display.getCurrent().readAndDispatch())
				Display.getCurrent().sleep();
	}
}
