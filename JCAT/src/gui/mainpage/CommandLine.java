package gui.mainpage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

final class CommandLine extends Composite {
	final static void addCommandLine(final Composite parent, Color textColor,
			Color backgroundColor, final Font textFont) {
		new CommandLine(parent, textColor, backgroundColor, textFont);
	}

	private CommandLine(final Composite parent, Color textColor,
			Color backgroundColor, final Font textFont) {
		/* FORMATTING */
		super(parent, SWT.NONE);
		final GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = SWT.FILL;
		setLayoutData(gridData);

		setLayout(new FillLayout());

		/* ADDING TEXTBOX */

		final Text text = new Text(this, SWT.NONE);
		text.setFont(textFont);
		text.setForeground(textColor);

		text.setBackground(backgroundColor);
		text.setEditable(true);

		text.addListener(SWT.DefaultSelection, new Listener() {
			@Override
			public void handleEvent(final Event e) {
				// String commandx = text.getText();
				text.setText("");
				// Execute Commmand
			}
		});
	}
}