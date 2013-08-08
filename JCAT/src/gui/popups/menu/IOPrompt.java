package gui.popups.menu;

import main.Launcher;
import network.PktReader;
import network.PktWriter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import resources.ResourceLoader;
import utilities.EndianCorrector;
import utilities.GenericPrompt;

public final class IOPrompt {
	public static final void launchOutputPrompt() {
		launchShell(true);
	}

	public static final void launchInputPrompt() {
		launchShell(false);
	}

	private static final void launchShell(boolean output) {
		final Shell dialog = GenericPrompt.getGenericShell();
		dialog.setText("JCAT");
		dialog.setLayout(new RowLayout(SWT.VERTICAL));
		final Composite composite = new Composite(dialog, SWT.NONE);
		FormLayout formLayout = new FormLayout();
		formLayout.marginLeft = 8;
		formLayout.marginRight = 8;
		formLayout.marginTop = 8;
		formLayout.marginBottom = 8;
		formLayout.spacing = 5;
		composite.setLayout(formLayout);

		final Composite title = new Composite(composite, SWT.NONE);
		FormData data = new FormData();
		data.top = new FormAttachment(0, -8);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		title.setLayoutData(data);
		title.setLayout(new GridLayout(1, true));
		final Label titleL = new Label(title, SWT.NONE);

		if (output)
			titleL.setText("Configure Output");
		else
			titleL.setText("Configure Input");

		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = SWT.CENTER;
		titleL.setLayoutData(gridData);

		final Font titleFont = new Font(Display.getCurrent(), title.getFont()
				.getFontData()[0].getName(), 8, SWT.BOLD);
		titleL.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				titleFont.dispose();
			}
		});
		titleL.setFont(titleFont);

		final Label label = new Label(composite, SWT.NONE);
		if (output)
			label.setText("Modify target's IP/Port/Endian: ");
		else
			label.setText("Your IP/Port/Endian: ");

		data = new FormData();
		data.top = new FormAttachment(title, 0, SWT.BOTTOM);
		data.left = new FormAttachment(0, 0);
		label.setLayoutData(data);

		final Group middle = new Group(composite, SWT.BORDER_SOLID);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(label, -8, SWT.BOTTOM);
		middle.setLayoutData(data);
		middle.setLayout(new GridLayout(2, false));

		Text[] texts = new Text[2];
		fillDialog(middle, texts, output);

		final SelectionListener selectionListener = getSelectionListener(
				dialog, texts, output);

		Button ok = new Button(composite, SWT.PUSH);
		ok.setText("OK");
		ok.addSelectionListener(selectionListener);
		data = new FormData();
		data.right = new FormAttachment(100, 0);
		data.width = 60;
		data.bottom = new FormAttachment(100, 0);
		ok.setLayoutData(data);
		ok.setFocus();

		Button cancel = new Button(composite, SWT.PUSH);
		cancel.setText("Cancel");
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dialog.close();
			}
		});
		data = new FormData();
		data.right = new FormAttachment(ok, -10, 0);
		data.width = 60;
		data.bottom = new FormAttachment(ok, 100, SWT.CENTER);
		cancel.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(middle, 0, SWT.BOTTOM);
		data.bottom = new FormAttachment(ok, 0, SWT.TOP);
		addRadioComposite(composite, data, output);

		dialog.pack();
		dialog.open();
		while (!dialog.isDisposed())
			if (!Display.getCurrent().readAndDispatch())
				Display.getCurrent().sleep();
	}

	public static final void addLabel(Group middle, String text) {
		Label label = new Label(middle, SWT.NONE);
		label.setText(text);
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
		gridData.horizontalIndent = 5;
		label.setLayoutData(gridData);
	}

	public static final Text getText(Group middle) {
		Text text = new Text(middle, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		return text;
	}

	private static final void fillDialog(Group middle, Text[] texts,
			boolean isOutput) {
		addLabel(middle, "IP: ");
		texts[0] = getText(middle);
		if (isOutput)
			texts[0].setText(PktWriter.getTargetIP());
		else {
			String wirelessIP = PktReader.getWirelessIP();
			if (wirelessIP != null) {
			texts[0].setText(wirelessIP);}
			else texts[0].setText("Could not be found");
			texts[0].setEditable(false);
		}

		addLabel(middle, "Port: ");
		texts[1] = getText(middle);
		if (isOutput)
			texts[1].setText(PktWriter.getPort());
		else {
			texts[1].setText(PktReader.getPort());
			texts[1].setEditable(false);
		}
	}

	private static final SelectionListener getSelectionListener(
			final Shell dialog, final Text[] texts, final boolean isOutput) {
		return new SelectionAdapter() {
			@Override
			public final void widgetSelected(SelectionEvent e) {
				if (isOutput) {
					PktWriter.setTargetIP(texts[0].getText());
					PktWriter.setPort(texts[1].getText());
					Launcher.addUserActivity("Set output IP: "
							+ texts[0].getText());
					Launcher.addUserActivity("Set output port: "
							+ texts[1].getText());
				}
				dialog.close();
			}
		};
	}

	private static final void addRadioComposite(Composite s, Object data,
			boolean output) {
		Composite intermediary = new Composite(s, SWT.NONE);
		intermediary.setLayoutData(data);
		RowLayout rowLayout = new RowLayout();
		rowLayout.spacing = 17;
		intermediary.setLayout(rowLayout);
		addListener(intermediary, new Button(intermediary, SWT.RADIO), true,
				output);
		addListener(intermediary, new Button(intermediary, SWT.RADIO), false,
				output);

	}

	private static final void addListener(final Composite c1, Button b,
			final boolean bigEndian, final boolean isOutput) {
		if (bigEndian) {
			b.setText("Big Endian");
			if (isOutput)
				b.setSelection(EndianCorrector.isBigEndianOut());
			else {
				b.setEnabled(false);
				b.setSelection(EndianCorrector.isBigEndianIn());
			}
		} else {
			b.setText("Little Endian");
			if (isOutput)
				b.setSelection(!EndianCorrector.isBigEndianOut());
			else {
				b.setEnabled(false);
				b.setSelection(!EndianCorrector.isBigEndianIn());
			}
		}

		b.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Control[] children = c1.getChildren();
				for (int j = 0; j < 2; j++) {
					Button button = (Button) children[j];
					if ((button.getStyle() & SWT.RADIO) != 0)
						button.setSelection(false);
				}
				Button button = (Button) event.widget;
				button.setSelection(true);
				if (isOutput)
					EndianCorrector.setBigEndianOut(bigEndian);
				else
					EndianCorrector.setBigEndianIn(bigEndian);
			}
		});
	}
}