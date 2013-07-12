package gui.mainpage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

final class StatusWindowSash extends SashForm {
	private final StatusWindow eventWindow;
	private final StatusWindow userWindow;

	final static void addWindowSash(Composite comp, Color backgroundColor,
			Font monospacedFont) {
		new StatusWindowSash(comp, backgroundColor, monospacedFont);
	}

	StatusWindowSash(Composite comp, Color backgroundColor, Font monospacedFont) {
		super(comp, SWT.VERTICAL);

		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		this.setLayoutData(gridData);
		setLayout(new FillLayout());

		eventWindow = new StatusWindow(this, backgroundColor, new int[] { 170,
				1000}, new String[] { "TIME", "EVENT" },
				monospacedFont);
		// eventWindow.addTextEntry("126:12:12-12:41",
		// "EVENT STREAMING ENABLED...", )

		userWindow = new StatusWindow(this, backgroundColor, new int[] { 170,
				1000 }, new String[] { "Time", "Entry" }, monospacedFont);
	}

	final void addUserActivity(String time, String activity, Color color) {
		userWindow.addTextEntry(new String[] { time, activity }, color);
	}

	final void addEventMessage(String time, String detail1, Color color) {
		eventWindow
				.addTextEntry(new String[] { time, detail1}, color);
	}
}
