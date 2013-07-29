package gui.mainpage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

final class BottomBar extends Composite {
	final static void addBottomBar(final Composite parent, Color backgroundColor) {
		new BottomBar(parent, backgroundColor);
	}

	private BottomBar(final Composite parent, Color backgroundColor) {
		super(parent, SWT.NONE);
		final GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = SWT.FILL;
		setLayoutData(gridData);

		setLayout(new FillLayout());

		final Label label = new Label(this, SWT.NONE);
		label.setBackground(backgroundColor);
		label.setText("");
	}
}