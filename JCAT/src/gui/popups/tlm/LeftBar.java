package gui.popups.tlm;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public final class LeftBar extends Composite {
	/**
	 * Adds a Composite within the input Composite, displaying colored, vertical
	 * lines. There are (colors.length) lines.
	 * 
	 * @param parent
	 *            The Composite to contain this.
	 * @param colors
	 *            The colors of the lines of this. Greater indices are further
	 *            to the Right within this.
	 * @param backgroundColor
	 *            The color of the background of this.
	 * @param layoutData
	 *            The layout data to assign to this.
	 */
	public static final void addLeftBar(Composite parent, Color[] colors,
			Color backgroundColor, Object layoutData) {
		new LeftBar(parent, colors, backgroundColor, layoutData);
	}

	private LeftBar(Composite parent, Color[] colors, Color backgroundColor,
			Object layoutData) {
		super(parent, SWT.NONE);
		setLayoutData(layoutData);

		GridLayout gridLayout = new GridLayout(colors.length, true);
		gridLayout.horizontalSpacing = 0;
		setLayout(gridLayout);

		setBackground(backgroundColor);

		/* ADDING VERTICAL LINES */
		for (int i = 0; i < colors.length; i++) {
			Label temp = new Label(this, SWT.NONE);
			temp.setLayoutData(new GridData(GridData.FILL_BOTH));
			temp.setBackground(colors[i]);
		}
		pack();
	}
}