package gui.popups.tlm;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

final class EntryBox extends Composite {
	private final Entry[] entries;

	/**
	 * Creates a composite within the input shell. This contains
	 * (entrytitles.length) <code>Entry</code>s (Objects that extends
	 * Composites). Each <code>Entry</code> has a Label and a Text (textbox),
	 * and the input parameters define every <code>Entry</code>. Each
	 * <code>Entry</code>'s Label displays a String within the input String[]
	 * entryTitles. The <code>Entry</code>s are displayed vertically, in the
	 * same order as their Label's String within entryTitles, with smaller
	 * indices north of greater indices.
	 * 
	 * @param shell
	 *            The shell to contain this.
	 * @param entryBoxLayoutData
	 *            The desired LayoutData for this.
	 * @param borderColor
	 *            The desired color for the border of this.
	 * @param entryBoxBackgroundColor
	 *            The desired color for the background of this.
	 * @param entryBackgroundColor
	 *            The desired color for the backgrounds of the Entry's.
	 * @param entryTitles
	 *            The desired titles for the Entrys
	 * @param labelForegroundColor
	 *            The desired foreground of the Entrys' Labels.
	 * @param labelBackgroundColor
	 *            The desired background of the Entrys' Labels.
	 * @param labelFont
	 *            The desired Font of the Entrys' Labels.
	 * @param textForegroundColor
	 *            The desired foreground of the Entrys' Texts.
	 * @param textBackgroundColor
	 *            The desired background of the Entrys' Texts.
	 * @param textFont
	 *            The desired Font of the Entrys' Text.
	 **/
	EntryBox(Composite shell, Object entryBoxLayoutData, Color borderColor,
			Color entryBoxBackgroundColor, Color entryBackgroundColor,
			String[] entryTitles, Color labelForegroundColor,
			Color labelBackgroundColor, Font labelFont,
			Color textForegroundColor, Color textBackgroundColor, Font textFont) {

		super(shell, SWT.NONE);
		setLayoutData(entryBoxLayoutData);

		setBackground(borderColor);
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = 2;
		fillLayout.marginWidth = 2;
		setLayout(fillLayout);

		Composite innerComposite = new Composite(this, SWT.NONE);
		innerComposite.setBackground(entryBoxBackgroundColor);
		GridLayout gridLayout = new GridLayout(1, true);
		gridLayout.verticalSpacing = -6;
		innerComposite.setLayout(gridLayout);

		entries = new Entry[entryTitles.length];

		for (int i = 0; i < entryTitles.length; i++) {
			GridData entryLayoutData = new GridData();
			entryLayoutData.grabExcessHorizontalSpace = true;
			entryLayoutData.horizontalAlignment = SWT.FILL;

			entries[i] = new Entry(innerComposite, entryLayoutData,
					entryBackgroundColor, entryTitles[i], labelForegroundColor,
					labelBackgroundColor, labelFont, textForegroundColor,
					textBackgroundColor, textFont);
		}
	}

	/**
	 * Returns the <code>Entry</code> at the input index.
	 * 
	 * @param index
	 *            The location of the Entry within this.
	 * @return The <code>Entry</code> at the input index.
	 */
	public final Entry getEntry(int index) {
		return entries[index];
	}
}