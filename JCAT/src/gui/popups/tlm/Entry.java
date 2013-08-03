package gui.popups.tlm;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * This displays the information of one packets.housekeeping.Telemetry.class
 * object.
 * 
 * @author Joe Benassi
 * 
 */
final class Entry extends Composite {
	private final Text text;

	/**
	 * Adds an Entry within the input Composite. Has a Label on the left and a
	 * Text (textfield) on the right. The Label is left-aligned with initial
	 * text "init". The Text is left-aligned, uneditable, and has a predefined
	 * minimum width.
	 * 
	 * @param entryParent
	 *            The Composite to contain this.
	 * @param entryLayoutData
	 *            The desired LayoutData of this.
	 * @param entryBackgroundColor
	 *            the desired background of this
	 * @param labelText
	 *            The desired text of the Label.
	 * @param labelForegroundColor
	 *            The desired foreground of the Label.
	 * @param labelBackgroundColor
	 *            The desired background of the Label.
	 * @param labelFont
	 *            The desired Font of the Label.
	 * @param textForegroundColor
	 *            The desired foreground of the Text.
	 * @param textBackgroundColor
	 *            The desired background of the Text.
	 * @param textFont
	 *            The desired Font of the Text.
	 */
	Entry(Composite entryParent, Object entryLayoutData,
			Color entryBackgroundColor, String labelText,
			Color labelForegroundColor, Color labelBackgroundColor,
			Font labelFont, Color textForegroundColor,
			Color textBackgroundColor, Font textFont) {
		super(entryParent, SWT.RIGHT);
		setLayoutData(entryLayoutData);

		final String initialLabelText = "No Telemetry      ";
		setBackground(entryBackgroundColor);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.makeColumnsEqualWidth = false;
		setLayout(gridLayout);

		GridData gridData = new GridData();
		gridData.horizontalIndent = 5;
		gridData.grabExcessHorizontalSpace = true;
		Label nameL = new Label(this, SWT.NONE);
		nameL.setLayoutData(gridData);
		nameL.setText(labelText + ": ");
		nameL.setBackground(labelBackgroundColor);
		nameL.setForeground(labelForegroundColor);
		nameL.setFont(labelFont);
		nameL.pack();

		text = new Text(this, SWT.NONE);
		text.setText(initialLabelText);
		text.setBackground(textBackgroundColor);
		text.setForeground(textForegroundColor);
		text.setFont(textFont);
		text.setEditable(false);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.HORIZONTAL_ALIGN_END;
		text.setLayoutData(gridData);
	}

	/**
	 * Returns the Text object of this Entry.
	 * 
	 * @return the Text object of this entry.
	 */
	public final Text getText() {
		return text;
	}
}
