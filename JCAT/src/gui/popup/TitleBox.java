package gui.popup;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

final class TitleBox extends Composite {
	private TitleBox(Composite parent, String name, Color backgroundColor,
			Color textColor, Font popupTitleFont, Object layoutData) {
		super(parent, SWT.RIGHT);

		setLayoutData(layoutData);
		setLayout(new GridLayout(1, false));

		setBackground(backgroundColor);
		Label title = new Label(this, SWT.CENTER);
		title.setText(name);
		title.setFont(popupTitleFont);
		title.setBackground(backgroundColor);
		title.setForeground(textColor);
	}

	/**
	 * Adds a Composite displaying the title of an App.
	 * 
	 * @param parent
	 *            The Composite to contain this.
	 * @param name
	 *            The name to display.
	 * @param backgroundColor
	 *            The color of the background of this.
	 * @param textColor
	 *            The color of the text of this.
	 * @param popupTitleFont
	 *            The Font of this.
	 * @param layoutData
	 *            The layout data to apply to this.
	 */
	static final void addTitleBox(Composite parent, String name,
			Color backgroundColor, Color textColor, Font popupTitleFont,
			Object layoutData) {
		new TitleBox(parent, name, backgroundColor, textColor, popupTitleFont,
				layoutData);
	}
}