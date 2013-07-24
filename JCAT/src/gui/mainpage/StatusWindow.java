package gui.mainpage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

final class StatusWindow extends Composite {
	private final Table table;
	private final Font dialogFont;

	StatusWindow(SashForm parent, Color backgroundColor, int[] columnWidths,
			String[] columnNames, Font dialogFont) {
		super(parent, SWT.NONE);

		this.dialogFont = dialogFont;
		Composite intermediary = this;
		intermediary.setLayout(new FillLayout(SWT.BOTTOM));

		table = new Table(intermediary, SWT.MULTI | SWT.FULL_SELECTION
				| SWT.V_SCROLL | SWT.H_SCROLL);

		table.setSortDirection(SWT.DOWN);
		table.setLinesVisible(false);
		// table.setHeaderVisible(true);
		table.setFont(dialogFont);

		for (int i = 0; i < columnWidths.length; i++)
			createTableColumn(SWT.LEFT, columnNames[i], columnWidths[i]);

		table.setBackground(backgroundColor);

		setResizeListener(parent);
		scrollToBottom();
		
		parent.addMouseWheelListener(new MouseWheelListener() {
		    public void mouseScrolled(final MouseEvent e) {
		        Scale src = (Scale)e.getSource();
		        src.setSelection(src.getSelection() - e.count );
		    }
		});
	}

	private final TableColumn createTableColumn(int style, String title,
			int width) {
		TableColumn tc = new TableColumn(table, style);
		tc.setText(title);
		tc.setResizable(true);
		tc.setWidth(width);
		return tc;
	}

	private final void setResizeListener(final Composite shell) {
		shell.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event e) {
				scrollToBottom();
			}
		});
	}

	/**
	 * Adds an entry into this EventWindow instance's table. This is an
	 * instantaneous command, and automatically scrolls the table downward.
	 * Displays information in 3 different columns. To be invoked whenever an
	 * event occurs. For public use.
	 * 
	 * @param time
	 *            the time that should be included in this entry. Occupies the
	 *            first column.
	 * @param details1
	 *            some text that should be included in this entry. Occupies the
	 *            second column.
	 * @param details2
	 *            some text that should be included in this entry. Occupies the
	 *            third column.
	 * @param color
	 *            the color of all three columns' text for this entry
	 */
	public final void addTextEntry(final String[] details, final Color color) {
		Display.getDefault().syncExec(new Runnable(){
			public void run()
			{
				TableItem ti = new TableItem(table, SWT.NONE);
				ti.setForeground(color);
				ti.setText(details);
				ti.setFont(dialogFont);
				scrollToBottom();
			}
		});		
	}

	private final void scrollToBottom() {
		TableItem item;
		if (table.getItemCount() > 0)
		{
			item = table.getItem(table.getItemCount() - 1);
			table.showItem(item);
		}
	}
}