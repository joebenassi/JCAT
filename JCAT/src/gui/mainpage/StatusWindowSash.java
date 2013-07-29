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
				120, 800 }, new String[] { "TIME", "SOURCE", "EVENT" },
				monospacedFont);

		userWindow = new StatusWindow(this, backgroundColor, new int[] { 170,
				120, 800 }, new String[] { "TIME", "SOURCE", "ENTRY" },
				monospacedFont);
	}

	final void addUserActivity(String time, String activity, Color color) {
		userWindow.addTextEntry(new String[] { time, "JCAT", activity }, color);
	}

	final void addEventMessage(String time, String config, String msgStr,
			Color color) {
		eventWindow.addTextEntry(new String[] { time, config, msgStr }, color);
	}

	public void addUserActivity(String time, String header, String activity,
			Color color) {
		userWindow.addTextEntry(new String[] { time, header, activity }, color);
	}
}
