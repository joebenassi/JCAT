package gui.menu.prompts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import fsw.CmdPkt;

public class CommandPrompt {

	public static void launchShell(final CmdPkt cmdPkt) {
		final String title = cmdPkt.getName();
		final String[] parameterNames = cmdPkt.getParameterNames();
		final int width = 150;

		final Text[] texts = new Text[parameterNames.length];
		final Shell dialog = new Shell(new Shell(Display.getCurrent()),
				SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

		final SelectionListener selectionListener = new SelectionAdapter() {
			@Override
			public final void widgetSelected(SelectionEvent e) {
				String[] values = new String[texts.length];

				for (int i = 0; i < values.length; i++)
					values[i] = texts[i].getText();
				boolean shouldClose = true;
				try {
					cmdPkt.execute(values);
				}

				catch (Throwable e1) {
					shouldClose = false;
				}

				if (shouldClose)
					dialog.close();
			}
		};
		try{
		GenericInputPrompt.launchShell(dialog, width, title, parameterNames,
				texts, selectionListener);
		}catch(Throwable e){};
	}
}
