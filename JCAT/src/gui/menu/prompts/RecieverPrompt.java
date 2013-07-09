package gui.menu.prompts;

import java.net.UnknownHostException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import udp.PacketSender;

public final class RecieverPrompt {
	public static final void launchShell() {
		final String title = "Configure Receiver";
		final String[] names = new String[] { "IP:", "Port:" };
		final int width = 150;

		final Text[] texts = new Text[names.length];
		final Shell dialog = new Shell(new Shell(Display.getCurrent()),
				SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

		final SelectionListener selectionListener = new SelectionAdapter() {
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

				try {
					PacketSender.set(ip, port);

				} catch (UnknownHostException e1) {
					System.out.println("IP DID NOT GO THROUGH");

					shouldClose = false;
				}

				if (shouldClose) {
					dialog.close();
				}
			}
		};
		try{
		GenericInputPrompt.launchShell(dialog, width, title, names, texts,
				selectionListener);}
		catch(Throwable e){};
	}
}