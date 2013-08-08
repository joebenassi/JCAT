package gui.popups.tlm;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import utilities.ColorConstants;
import utilities.FontConstants;
import utilities.GenericPrompt;

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
	private Shell POPUP = GenericPrompt.getGenericShell();
	private ScrolledComposite SCROLL = new ScrolledComposite(POPUP,
			SWT.H_SCROLL | SWT.V_SCROLL);// | SWT.BORDER);
	private Composite SCROLLER = new Composite(SCROLL, SWT.NONE);
	private static final Font TEXTFONT = FontConstants.bodyFont;
	private static final Font SCROLLERTITLEFONT = FontConstants.titleFont;
	private static Color backgroundColor = ColorConstants.lightPageBackground;
	private static Color textColor = ColorConstants.textColor;
	private static Color textBoxColor = ColorConstants.textBoxColor;
	private static Color borderColor = ColorConstants.borderColor;
	private static Color[] panelColors = ColorConstants.panelColors;
	private static final int offSet = -37;
	private EntryBox entryBox;
	private UniversalBox universalBox;

	/**
	 * This creates a Shell containing a UniversalBox, a LeftBar, and
	 * App-specific telemetry data. This is an instance variable of an App. The
	 * shell within this can be displayed, hidden, and disposed as needed. This
	 * orchastrates the designing and adding of contained components, such as
	 * Entries, their EntryBox, a TitleBox, a UniversalBox, and a LeftBar. This
	 * is the only entity that other classes in other packages need to
	 * communicate with.
	 * 
	 * @param SCROLLERNAME
	 *            The name of the Shell.
	 * @param ENTRYNAMES
	 *            The names of the various Telemetry data subjects.
	 */
	public PopupFiller(final String PopupName, final String[] ENTRYNAMES) {
		developShell();
		addRightBar(PopupName, ENTRYNAMES);

		FormData data = new FormData();
		data.width = 60;
		data.right = new FormAttachment(SCROLLER.getChildren()[0], 0, SWT.LEFT);
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 5);
		data.height = SCROLLER.getSize().y
				- ((FormLayout) (SCROLLER.getLayout())).marginHeight + offSet;
		data.bottom = new FormAttachment(100, -5);
		LeftBar.addLeftBar(SCROLLER, panelColors, backgroundColor, data);
		SCROLLER.pack();
		SCROLL.pack();
		POPUP.pack();
	}

	/**
	 * Formats the look of the Shell and specifies the correct behavior on
	 * close.
	 */
	private final void developShell() {
		POPUP.setLayout(new FillLayout());
		POPUP.addShellListener(new ShellAdapter() {
			@Override
			public final void shellClosed(ShellEvent e) {
				e.doit = false;
				POPUP.setVisible(false);
			}
		});
		SCROLL.addListener(SWT.Activate, new Listener() {
			@Override
			public void handleEvent(Event e) {
				SCROLL.setFocus();
			}
		});
		SCROLL.setLayout(new FillLayout());
		SCROLL.setContent(SCROLLER);

		FormLayout formLayout = new FormLayout();
		formLayout.marginWidth = 0;
		formLayout.marginHeight = 0;
		formLayout.marginRight = 0;
		formLayout.marginLeft = 0;
		formLayout.spacing = 0;
		SCROLL.setBackground(backgroundColor);
		SCROLLER.setLayout(formLayout);
		SCROLLER.setBackground(backgroundColor);
		// POPUP.setBackground(backgroundColor);
	}

	/**
	 * Develops the right side of the Shell: The TitleBox, UniversalBox, and
	 * EntryBox.
	 * 
	 * @param SCROLLERNAME
	 *            The name of the Shell.
	 * @param ENTRYNAMES
	 *            The names of the various Telemetry data subjects.
	 */
	private final void addRightBar(String SCROLLERNAME, String[] ENTRYNAMES) {
		Composite rightBar = new Composite(SCROLLER, SWT.NONE);

		FormData datax = new FormData();
		datax.right = new FormAttachment(100, 0);
		rightBar.setLayoutData(datax);
		rightBar.setBackground(backgroundColor);

		/* CREATING GRID FOR VERTICAL LINES */
		rightBar.setLayout(new GridLayout(1, false));

		GridData data;

		data = new GridData(SWT.LEFT, SWT.NONE, true, false);
		data.horizontalIndent = 15;
		TitleBox.addTitleBox(rightBar, SCROLLERNAME, backgroundColor,
				textColor, SCROLLERTITLEFONT, data);

		data = new GridData(SWT.LEFT, SWT.NONE, true, false);
		data.horizontalIndent = 15;

		universalBox = new UniversalBox(rightBar, backgroundColor, textColor,
				textBoxColor, borderColor, TEXTFONT, data);

		data = new GridData(SWT.LEFT, SWT.NONE, true, false);
		data.horizontalIndent = 15;

		entryBox = new EntryBox(rightBar, data, borderColor, backgroundColor,
				backgroundColor, ENTRYNAMES, textColor, backgroundColor,
				TEXTFONT, textColor, textBoxColor, TEXTFONT);
		SCROLLER.pack();
		POPUP.pack();
	}

	/**
	 * Returns the Text containing the telemetry data at the specified index.
	 * 
	 * @param index
	 *            The index of the telemetry data.
	 * @return The Text containing the telemetry data at the specified index.
	 */
	public Text getText(int index) {
		return entryBox.getEntry(index).getText();
	}

	/**
	 * Displays the SCROLLER.
	 */
	public final void launchPopup() {
		POPUP.setVisible(true);
	}

	/**
	 * Disposes of the SCROLLER. This method is required to free system
	 * resources.
	 */
	public final void dispose() {
		POPUP.dispose();
	}

	/**
	 * Returns true if the shell is disposed, false otherwise. This should
	 * hopefully return true when the program exist.
	 * 
	 * @return The status of disposal of the shell.
	 */
	public final boolean isDisposed() {
		return POPUP.isDisposed();
	}

	public void setTime(String time) {
		universalBox.setTimeText(time);
	}

	public void setSC(String SC) {
		universalBox.setSCText(SC);

	}
}
