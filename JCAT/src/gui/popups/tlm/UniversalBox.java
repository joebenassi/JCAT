package gui.popups.tlm;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import utilities.Updater;

/**
 * This is to be displayed in every App's popup window.
 * @author Joe Benassi
 *
 */
final class UniversalBox extends Composite {
	/**
	 * The various entities within this. Each instance defined displays a title
	 * and current value. Values are updated through the <code>Updater</code>
	 * class.
	 * 
	 * @author Joe Benassi
	 * 
	 */
	private static enum Child {
		GMT("GMT"), SC("SC"), UTC("UTC"), SequenceCount("Sequence Count");

		private final String name;

		private Child(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	/**
	 * The width of the GMT, SC, and UTC Texts
	 */
	private static final int fieldMinimumWidth1 = 180;
	/**
	 * The width of the SequenceCount Text
	 */
	private static final int fieldMinimumWidth2 = fieldMinimumWidth1 - 61;
	private static final int verticalSpacing = -5;

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
	 */
	public static final void addUniversalBox(Composite parent,
			Color backgroundColor, Color textColor, Color textBoxColor,
			Color borderColor, Object layoutData) {
		new UniversalBox(parent, backgroundColor, textColor, textBoxColor,
				borderColor, layoutData);
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
	private UniversalBox(Composite parent, Color backgroundColor,
			Color textColor, Color textBoxColor, Color borderColor,
			Object layoutData) {
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

		addTopContent(internal, backgroundColor, textColor, textBoxColor);
		addBottomContent(internal, backgroundColor, textColor, textBoxColor);
	}

	/**
	 * Adds Sequence Count content.
	 * 
	 * 
	 * @param internal
	 *            The composite to contain the bottom content.
	 * @param backgroundColor
	 *            The background of the various contained Labels, Texts, and
	 *            Composites.
	 * @param textColor
	 *            The color of the texts within the various contained Labels,
	 *            Texts, and Composites.
	 * @param textBoxColor
	 *            The color of the background of the various Texts (textboxes).
	 */
	private static final void addBottomContent(Composite internal,
			Color backgroundColor, Color textColor, Color textBoxColor) {
		Composite bottomComposite = new Composite(internal, SWT.NONE);
		bottomComposite.setBackground(backgroundColor);
		bottomComposite.setLayout(new GridLayout(2, false));

		Child[] bottomChildren = new Child[] { Child.SequenceCount };

		for (int i = 0; i < bottomChildren.length; i++) {
			addLabel(bottomComposite, bottomChildren[i].getName(),
					backgroundColor, textColor);
			addText(false, bottomComposite, textColor, textBoxColor,
					bottomChildren[i].getName());
		}
	}

	/**
	 * Adds GMT, SC, and UTC content.
	 * 
	 * @param internal
	 *            The composite to contain the top content.
	 * @param backgroundColor
	 *            The background of the various contained Labels, Texts, and
	 *            Composites.
	 * @param textColor
	 *            The color of the texts within the various contained Labels,
	 *            Texts, and Composites.
	 * @param textBoxColor
	 *            The color of the background of the various Texts (textboxes).
	 */
	private static final void addTopContent(Composite internal,
			Color backgroundColor, Color textColor, Color textBoxColor) {
		Composite topComposite = new Composite(internal, SWT.NONE);
		topComposite.setBackground(backgroundColor);
		topComposite.setLayout(new GridLayout(2, false));

		Child[] topChildren = new Child[] { Child.GMT, Child.SC, Child.UTC };

		for (int i = 0; i < topChildren.length; i++) {
			addLabel(topComposite, topChildren[i].getName(), backgroundColor,
					textColor);
			addText(true, topComposite, textColor, textBoxColor,
					topChildren[i].getName());
		}
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
			Color backgroundColor, Color textColor) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(name + ": ");
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
	 */
	private static final void addText(final boolean isTopContent,
			Composite parent, Color textColor, Color textBoxColor, String name) {
		GridData fieldGridData = new GridData(GridData.FILL_HORIZONTAL);
		if (isTopContent)
			fieldGridData.minimumWidth = fieldMinimumWidth1;
		else
			fieldGridData.minimumWidth = fieldMinimumWidth2;

		Text temp = new Text(parent, SWT.NONE);
		temp.setBackground(textBoxColor);
		temp.setEditable(false);
		temp.setText("        ");
		Updater.addUpdater(temp, name);
		temp.setLayoutData(fieldGridData);
		temp.setForeground(textColor);
	}
}