package gui.popups.menu;

import main.Launcher;
import network.PktWriter;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import utilities.GenericPrompt;

public final class RecieverPrompt {
	public static final void launchShell() {
		final Shell dialog = GenericPrompt.getDialogShell();
		final String title = "JCAT";
		
		final Text[] texts = new Text[2];
		final SelectionListener selectionListener = getSelectionListener(dialog, texts);
		
		GenericPrompt.addFirstLine(dialog, "Select target's IP/PORT");
		fillDialog(dialog, texts);
		
		try{
			GenericPrompt.launchShell(dialog, title, selectionListener);}
		catch(Throwable e){};
	}
	
	private static final void fillDialog(Shell dialog, Text[] texts)
	{
		GenericPrompt.addLabel(dialog, "IP: ");
		texts[0] = GenericPrompt.getText(dialog);
		GenericPrompt.addLabel(dialog, "Port: ");
		texts[1] = GenericPrompt.getText(dialog);
	}

	private static final SelectionListener getSelectionListener(final Shell dialog, final Text[] texts)
	{
		return new SelectionAdapter() {
			@Override
			public final void widgetSelected(SelectionEvent e) {
				PktWriter.setIP(texts[0].getText());
				PktWriter.setPort(texts[1].getText());
				
				Launcher.addUserActivity("Set output IP: " + texts[0].getText());
				Launcher.addUserActivity("Set output port: " + texts[1].getText());
				dialog.close();
			}
		};
	}
}