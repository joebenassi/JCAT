package gui.popups.menu;

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
				final String ip = texts[0].getText();
				int port = 0;
				boolean shouldClose = true;
				try {
					port = Integer.parseInt(texts[1].getText());
				} catch (Throwable x) {
					System.out.println("PORT DID NOT GO THROUGH");
					shouldClose = false;
				}

				System.out.println(" DIDNT SET PORT ");
					//PacketSender.set(ip, port);

				if (shouldClose) {
					dialog.close();
				}
			}
		};
	}
}