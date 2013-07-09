package gui.mainpage;

import helpers.Updater;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


final class TopBar extends Composite {
	// private final Image nasaImage;
	private static Label time1;
	private static Label time2;

	final static void addTopBar(Composite parent, String title,
			Color backgroundColor, Color foregroundColor, Font titleFont,
			Font timeFont, Image nasaLogo)// , Image nasaLogo)
	{
		new TopBar(parent, title, backgroundColor, foregroundColor, titleFont,
				timeFont, nasaLogo);
	}

	private TopBar(Composite comp, String title, Color backgroundColor,
			Color foregroundColor, Font titleFont, Font timeFont, Image nasaLogo) {
		super(comp, SWT.NONE);

		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = SWT.FILL;
		setLayoutData(gridData);

		setLayout(new GridLayout(3, false));

		setBackground(backgroundColor);
		addImage(this, nasaLogo);
		addTitle(this, title, backgroundColor, foregroundColor, titleFont);
		addTimeBox(this, backgroundColor, foregroundColor, timeFont);
	}

	private static final void addImage(Composite composite, Image nasaLogo) {
		Label temp = new Label(composite, SWT.NONE);

		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
		gridData.horizontalIndent = 5;
		temp.setLayoutData(gridData);

		temp.setImage(nasaLogo);
		temp.setBounds(nasaLogo.getBounds());
	}

	private static final void addTitle(Composite composite, String t,
			Color backgroundColor, Color foregroundColor, Font titleFont) {
		Label title = new Label(composite, SWT.BOLD);
		title.setText(t);
		title.setFont(titleFont);
		title.setForeground(foregroundColor);
		title.setBackground(backgroundColor);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.HORIZONTAL_ALIGN_CENTER;
		gridData.grabExcessHorizontalSpace = true;
		title.setLayoutData(gridData);
	}

	private static final void addTimeBox(Composite composite,
			Color backgroundColor, Color foregroundColor, Font timeFont) {
		Composite timeBox = new Composite(composite, SWT.NONE);
		timeBox.setLayout(new GridLayout(2, false));
		timeBox.setBackground(backgroundColor);

		Label timeType1 = new Label(timeBox, SWT.NONE);
		timeType1.setText("GMT: ");
		timeType1.setForeground(foregroundColor);
		timeType1.setBackground(backgroundColor);
		time1 = new Label(timeBox, SWT.NONE);
		Updater.addUpdater(time1, "GMT");
		time1.setText("               ");
		time1.setForeground(foregroundColor);
		time1.setBackground(backgroundColor);
		time1.setFont(timeFont);
		Label timeType2 = new Label(timeBox, SWT.NONE);
		timeType2.setText("S/C UTC: ");
		timeType2.setForeground(foregroundColor);
		timeType2.setBackground(backgroundColor);
		time2 = new Label(timeBox, SWT.NONE);
		Updater.addUpdater(time2, "UTC");
		time2.setText("               ");
		time2.setForeground(foregroundColor);
		time2.setBackground(backgroundColor);
		time2.setFont(timeFont);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.HORIZONTAL_ALIGN_END;
		timeBox.setLayoutData(gridData);
	}
}
