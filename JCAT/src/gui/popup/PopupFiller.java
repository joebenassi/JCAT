package gui.popup;

import helpers.ColorConstants;
import helpers.FontConstants;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * This creates a Shell containing a UniversalBox, a LeftBar, and App-specific
 * telemetry data. This is an instance variable of an App. The shell within this
 * can be displayed, hidden, and disposed as needed. This orchastrates the
 * designing and adding of contained components, such as Entries, their
 * EntryBox, a TitleBox, a UniversalBox, and a LeftBar. This is the only entity
 * that other classes in other packages need to communicate with.
 * 
 * @author Joe
 * 
 */
public class PopupFiller {
	private final Shell POPUP = new Shell(SWT.SHELL_TRIM & ~(SWT.RESIZE)
			| SWT.ON_TOP | SWT.BORDER);
	private static final Font TEXTFONT = FontConstants.timeFont;
	private static final Font POPUPTITLEFONT = FontConstants.popupTitleFont;
	private static Color backgroundColor = ColorConstants.baseGray52;
	private static Color textColor = ColorConstants.text;
	private static Color textBoxColor = ColorConstants.base;
	private static Color borderColor = ColorConstants.baseGray20;
	private static Color[] panelColors = ColorConstants.panelColors;
	private static final int offSet = -37;
	private EntryBox entryBox;

	/**
	 * This creates a Shell containing a UniversalBox, a LeftBar, and
	 * App-specific telemetry data. This is an instance variable of an App. The
	 * shell within this can be displayed, hidden, and disposed as needed. This
	 * orchastrates the designing and adding of contained components, such as
	 * Entries, their EntryBox, a TitleBox, a UniversalBox, and a LeftBar. This
	 * is the only entity that other classes in other packages need to
	 * communicate with.
	 * 
	 * @param POPUPNAME The name of the Shell.
	 * @param ENTRYNAMES The names of the various Telemetry data subjects.
	 */
	public PopupFiller(final String POPUPNAME, final String[] ENTRYNAMES) {
		developShell();

		addRightBar(POPUPNAME, ENTRYNAMES);

		FormData data = new FormData();
		data.width = 60;
		data.right = new FormAttachment(POPUP.getChildren()[0], 0, SWT.DEFAULT);
		data.height = POPUP.getSize().y
				- ((FormLayout) (POPUP.getLayout())).marginHeight + offSet;
		LeftBar.addLeftBar(POPUP, panelColors, backgroundColor, data);
		POPUP.pack();
	}

	/**
	 * Formats the look of the Shell and specifies the correct behavior on close.
	 */
	private final void developShell() {
		POPUP.addShellListener(new ShellAdapter() {
			@Override
			public final void shellClosed(ShellEvent e) {
				e.doit = false;
				POPUP.setVisible(false);
			}
		});

		FormLayout formLayout = new FormLayout();
		formLayout.marginWidth = 10;
		formLayout.marginHeight = 10;
		formLayout.spacing = 0;
		POPUP.setLayout(formLayout);
		POPUP.setBackground(backgroundColor);
	}

	/**
	 * Develops the right side of the Shell: The TitleBox, UniversalBox, and EntryBox. 
	 * 
	 * @param POPUPNAME The name of the Shell.
	 * @param ENTRYNAMES The names of the various Telemetry data subjects.
	 */
	private final void addRightBar(String POPUPNAME, String[] ENTRYNAMES) {
		Composite rightBar = new Composite(POPUP, SWT.NONE);

		FormData datax = new FormData();
		datax.right = new FormAttachment(100, 0);
		rightBar.setLayoutData(datax);
		rightBar.setBackground(backgroundColor);

		/* CREATING GRID FOR VERTICAL LINES */
		rightBar.setLayout(new GridLayout(1, false));

		GridData data;

		data = new GridData(SWT.LEFT, SWT.NONE, true, false);
		data.horizontalIndent = 15;
		TitleBox.addTitleBox(rightBar, POPUPNAME, backgroundColor, textColor,
				POPUPTITLEFONT, data);

		data = new GridData(SWT.LEFT, SWT.NONE, true, false);
		data.horizontalIndent = 15;

		UniversalBox.addUniversalBox(rightBar, backgroundColor, textColor,
				textBoxColor, borderColor, data);

		data = new GridData(SWT.END, SWT.NONE, true, false);
		data.horizontalIndent = 15;

		entryBox = new EntryBox(rightBar, data, borderColor, backgroundColor,
				backgroundColor, ENTRYNAMES, textColor, backgroundColor,
				TEXTFONT, textColor, textBoxColor, TEXTFONT);
		POPUP.pack();
	}

	/**
	 * Returns the Text containing the telemetry data at the specified index.
	 * 
	 * @param index The index of the telemetry data.
	 * @return The Text containing the telemetry data at the specified index.
	 */
	public Text getText(int index) {
		return entryBox.getEntry(index).getText();
	}

	/**
	 * Displays the popup.
	 */
	public final void launchPopup() {
		POPUP.setVisible(true);
	}

	/**
	 * Disposes of the popup. This method is required to free system resources.
	 */
	public final void dispose() {
		POPUP.dispose();
	}

	/**
	 * Returns true if the shell is disposed, false otherwise. This should hopefully return true when the program exist.
	 * 
	 * @return The status of disposal of the shell.
	 */
	public final boolean isDisposed() {
		return POPUP.isDisposed();
	}
}
