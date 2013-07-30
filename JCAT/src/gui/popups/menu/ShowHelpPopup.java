package gui.popups.menu;

import gui.popups.tlm.LeftBar;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.custom.*;

import resources.ResourceLoader;

import utilities.ColorConstants;
import utilities.FontConstants;
import utilities.GenericPrompt;
import utilities.ShellDisposer;

public class ShowHelpPopup {
	private static final Font titleFont = FontConstants.titleFont;
	private static final Font dialogFont = FontConstants.bodyFont;
	private static final Color fgC = ColorConstants.textColor;
	private static final Color oC = ColorConstants.lightPageBackground;
	private static final Color borderC = ColorConstants.borderColor;
	private static final Color[] panelColors2 = ColorConstants.panelColors2;
	private static final Color darkAccent = ColorConstants.darkAccent;
	private static final int width = 350;

	public static final void launch(String header, String[] paragraphs) {
		/* Configure shell */
		final Shell shell = GenericPrompt.getDialogShell();
		shell.setText("JCAT");
		shell.setBackground(oC);
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = 2;
		fillLayout.marginWidth = 2;
		shell.setLayout(fillLayout);

		/* Configure internal */
		Composite internal = new Composite(shell, SWT.NONE);
		FormLayout f = new FormLayout();
		f.marginHeight = 5;
		f.marginWidth = 5;
		internal.setLayout(f);
		internal.setBackground(darkAccent);

		/* Configure inner composites */
		Composite c0 = new Composite(internal, SWT.NONE);
		Composite c1 = new Composite(c0, SWT.NONE);
		Composite c2 = new Composite(c0, SWT.NONE);
		Composite c3 = new Composite(c0, SWT.NONE);

		/* Configure C0 */
		FormData data = new FormData();
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		c0.setLayoutData(data);
		c0.setBackground(oC);
		FormLayout formLayout = new FormLayout();
		formLayout.marginHeight = 5;
		formLayout.marginRight = 5;
		c0.setLayout(formLayout);

		/* Configure Leftbar */
		data = new FormData();
		data.right = new FormAttachment(c0, 15, SWT.LEFT);
		data.top = new FormAttachment(0, -5);
		data.bottom = new FormAttachment(100, 5);
		data.left = new FormAttachment(0, 0);
		data.width = 70;
		data.height = 100;
		LeftBar.addLeftBar(internal, panelColors2, borderC, data);

		/* Configure c1 */
		data = new FormData();
		data.right = new FormAttachment(100, 0);
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, -10);
		c1.setLayoutData(data);
		c1.setLayout(new GridLayout(1, true));
		c1.setBackground(oC);

		/* Configure c2 */
		data = new FormData();
		data.top = new FormAttachment(c1, -6);
		data.left = new FormAttachment(0, -30);
		data.right = new FormAttachment(100, 0);
		data.height = 10;
		c2.setLayoutData(data);
		c2.setLayout(new GridLayout(1, true));
		c2.setBackground(darkAccent);

		/* Configure c3 */
		data = new FormData();
		data.top = new FormAttachment(c2, 8);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.width = width;
		c3.setLayoutData(data);
		c3.setLayout(new FillLayout());

		/* Configure S1 */
		StyledText s1 = new StyledText(c1, SWT.WRAP);
		s1.setForeground(fgC);
		String totalTitle = "HELP: " + header;
		s1.setText(totalTitle);
		StyleRange[] styles = new StyleRange[1];
		styles[0] = new StyleRange();
		styles[0].font = titleFont;
		s1.setBackground(oC);
		int[] range = new int[] { 0, totalTitle.length() };
		s1.setStyleRanges(range, styles);
		s1.setEditable(false);

		/* Configure S3 */
		StyledText s3 = new StyledText(c3, SWT.WRAP);
		s3.setForeground(fgC);
		String body = getContent(paragraphs);
		s3.setText(body);
		styles = new StyleRange[1];
		styles[0] = new StyleRange();
		styles[0].font = dialogFont;
		s3.setBackground(oC);
		range = new int[] { 0, body.length() };
		s3.setStyleRanges(range, styles);
		s3.setEditable(false);

		shell.pack();
		ShellDisposer.queueForDisposal(shell);
		shell.open();
	}

	private static final String getContent(String[] paragraphs) {
		String master = "";
		for (int i = 0; i < paragraphs.length; i++) {
			master += "   " + paragraphs[i];
			if (i < paragraphs.length - 1)
				master += "\n\n";
		}
		return master;
	}
}