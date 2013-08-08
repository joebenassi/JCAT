package gui.popups.tlm;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * This is to be displayed in every App's popup window.
 * 
 * @author Joe Benassi
 * 
 */
final class UniversalBox extends Composite {
	private static final int fieldMinimumWidth = 210;
	private static final int verticalSpacing = -5;
	private Text timeText;
	private Text SCText;

	/**
	 * (Same as new UniversalBox(..)) Creates a Composite that displays the GMT,
	 * SC, and UTC within the input Composite.
	 * 
	 * @param parent
	 *            The Composite to contain this.
	 * @param backgroundColor
	 *            The background of the various contained Labels, Texts, and
	 *            Composites.
	 * @param textColor
	 *            The color of the texts within the various contained Labels,
	 *            Texts, and Composites.
	 * @param textBoxColor
	 *            The color of the background of the various Texts (textboxes).
	 * @param borderColor
	 *            The color of the border of this.
	 * @param layoutData
	 *            The layout data to assign to this.
	 * @param data
	 */
	public static final void addUniversalBox(Composite parent,
			Color backgroundColor, Color textColor, Color textBoxColor,
			Color borderColor, Font font, Object layoutData) {
		new UniversalBox(parent, backgroundColor, textColor, textBoxColor,
				borderColor, font, layoutData);
	}

	/**
	 * (Same as addUniversalBox(..)). Creates a Composite that displays the GMT,
	 * SC, and UTC within the input Composite.
	 * 
	 * @param parent
	 *            The Composite to contain this.
	 * @param backgroundColor
	 *            The background of the various contained Labels, Texts, and
	 *            Composites.
	 * @param textColor
	 *            The color of the texts within the various contained Labels,
	 *            Texts, and Composites.
	 * @param textBoxColor
	 *            The color of the background of the various Texts (textboxes).
	 * @param borderColor
	 *            The color of the border of this.
	 * @param layoutData
	 *            The layout data to assign to this.
	 */
	UniversalBox(Composite parent, Color backgroundColor, Color textColor,
			Color textBoxColor, Color borderColor, Font font, Object layoutData) {
		/* ADDING BORDER */
		super(parent, SWT.NONE);

		setLayoutData(layoutData);
		setBackground(borderColor);
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = 2;
		fillLayout.marginWidth = 2;
		setLayout(fillLayout);

		/* ADDING PRIMARY CONTENT */
		Composite internal = new Composite(this, SWT.NONE);
		internal.setBackground(backgroundColor);

		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = verticalSpacing;
		internal.setLayout(gridLayout);

		Composite topComposite = new Composite(internal, SWT.NONE);
		topComposite.setBackground(backgroundColor);
		topComposite.setLayout(new GridLayout(2, false));

		addLabel(topComposite, "GMT: ", backgroundColor, textColor, font);
		timeText = addText(topComposite, textColor, textBoxColor, font);

		Composite bottomComposite = new Composite(internal, SWT.NONE);
		bottomComposite.setBackground(backgroundColor);
		bottomComposite.setLayout(new GridLayout(2, false));

		addLabel(bottomComposite, "SC:  ", backgroundColor, textColor, font);
		SCText = addText(bottomComposite, textColor, textBoxColor, font);
	}

	/**
	 * The helper method to add Labels to this. The Labels display the names
	 * "GMT", "SC", "UTC", and "Sequence Count".
	 * 
	 * @param parent
	 *            The Composite to contain the Label.
	 * @param name
	 *            The text to display within the Label.
	 * @param backgroundColor
	 *            The background of the various contained Labels, Texts, and
	 *            Composites.
	 * @param textColor
	 *            The color of the texts within the various contained Labels,
	 *            Texts, and Composites.
	 */
	private static final void addLabel(Composite parent, String name,
			Color backgroundColor, Color textColor, Font font) {
		Label label = new Label(parent, SWT.NONE);
		label.setFont(font);
		label.setText(name);
		label.setBackground(backgroundColor);
		label.setForeground(textColor);
	}

	/**
	 * 
	 * @param isTopContent
	 *            True if the text is for GMT, SC, or UTC
	 * @param parent
	 *            The Composite to contain the Text.
	 * @param textColor
	 *            The color of the texts within the various contained Labels,
	 *            Texts, and Composites.
	 * @param textBoxColor
	 *            The color of the background of the various Texts (textboxes).
	 * @param name
	 *            The name representing the content. Should be "GMT", "SC",
	 *            "UTC", or "Sequence Count".
	 * @param font
	 */
	private static final Text addText(Composite parent, Color textColor,
			Color textBoxColor, Font font) {
		Text temp = new Text(parent, SWT.NONE);
		temp.setText("No Telemetry               ");
		temp.setFont(font);
		temp.setBackground(textBoxColor);
		temp.setForeground(textColor);
		temp.setEditable(false);

		GridData fieldGridData = new GridData(GridData.FILL_HORIZONTAL);
		fieldGridData.minimumWidth = fieldMinimumWidth;
		temp.setLayoutData(fieldGridData);

		return temp;
	}

	public final void setTimeText(String time) {
		timeText.setText(time);
	}

	public void setSCText(String SC) {
		SCText.setText(SC);
	}
}