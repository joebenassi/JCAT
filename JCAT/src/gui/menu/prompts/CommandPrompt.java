package gui.menu.prompts;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import fsw.CmdPkt;

/** 
 * IN DEVELOPMENT.
 * @author Joe
 *
 */
public class CommandPrompt {
	public static void launchShell(final CmdPkt cmdPkt) {
		final Shell dialog = GenericPrompt.getDialogShell();

		GenericPrompt.addFirstLine(dialog, cmdPkt.getName());

		final Scrollable[] texts = getTexts(dialog, cmdPkt);
		final SelectionListener selectionListener = getSelectionListener(
				dialog, cmdPkt, texts);

		final String shellTitle = "JCAT";

		try {
			GenericPrompt.launchShell(dialog, shellTitle,
					selectionListener);
		} catch (Throwable e) {
		}
	}


	private static final SelectionListener getSelectionListener(
			final Shell dialog, final CmdPkt cmdPkt, final Scrollable[] texts) {
		return new SelectionAdapter() {
			@Override
			public final void widgetSelected(SelectionEvent e) {
				String[] values = new String[texts.length];

				for (int i = 0; i < values.length; i++) {
					try {
						values[i] = ((Combo) texts[i]).getText();
					} catch (Throwable e1) {
						try {
							values[i] = ((Text) texts[i]).getText();
						} catch (Throwable e2) {
							e2.printStackTrace();
						}
					}
				}

				try {
					cmdPkt.execute(values);
					dialog.close();
				}
				catch (Throwable e1) {System.out.println("ERROR WITH EXECUTION");};
			}
		};
	}

	private static final Scrollable[] getTexts(Shell dialog, CmdPkt cmdPkt) {
		Scrollable[] texts = new Scrollable[cmdPkt.getParameterNames().length];

		for (int i = 0; i < cmdPkt.getParameterNames().length; i++) {
			ParameterDetails parameterDetails = cmdPkt.getParamList().get(i)
					.getParameterDetails();
			
			GenericPrompt.addLabel(dialog, cmdPkt.getParameterNames()[i]);
			
			if (parameterDetails.isInputParameter())
				texts[i] = GenericPrompt.getText(dialog);
			
			else 
				texts[i] = GenericPrompt.getCombo(dialog, parameterDetails.getOptions());
		}
		return texts;	
	}
}
