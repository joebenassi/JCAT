package gui.popups.menu;

import java.util.ArrayList;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import packets.cmd.CmdPkt;
import packets.parameters.ChoiceOption;
import packets.parameters.CmdParam;
import packets.parameters.CmdParam.ParamType;
import utilities.GenericPrompt;

public class InputCmdParamPrompt {
	public static void launchShell(final CmdPkt cmdPkt) {
		final Shell dialog = GenericPrompt.getDialogShell();

		GenericPrompt.addFirstLine(dialog, cmdPkt.getName());

		final Scrollable[] texts = getTexts(dialog, cmdPkt);
		final SelectionListener selectionListener = getSelectionListener(
				dialog, cmdPkt, texts);

		final String shellTitle = "JCAT";

		try {
			GenericPrompt.launchShell(dialog, shellTitle, selectionListener);
		} catch (Throwable e) {
		}
	}

	private static final SelectionListener getSelectionListener(
			final Shell dialog, final CmdPkt cmdPkt, final Scrollable[] texts) {
		return new SelectionAdapter() {
			@Override
			public final void widgetSelected(SelectionEvent e) {
				String[] values = new String[texts.length];
				//String[] options = cmdPkt.getParamList().get(i).getOptions();
				for (int i = 0; i < values.length; i++) {
						String[] options = ChoiceOption.getNames(cmdPkt.getParamList().get(i).getChoiceOptions());
						if (options != null && options.length > 0) {
							values[i] = ((Combo)texts[i]).getText();
							/*
							for (int j = 0; j < options.length; j++){
								try{
									if (options[j].equals(((Combo) texts[i]).getText()))
									{
										values[i] = j + "";
										System.out.println(j + "");
									}
								} catch(Throwable ex){ex.printStackTrace();}
							
							}*/
						}
						else values[i] = ((Text) texts[i]).getText();
					/*
						try {
							values[i] = ((Text) texts[i]).getText();
						} catch (Throwable e2) {
							e2.printStackTrace();
						}*/
				}
							//values[i] = ((Combo) texts[i].get
				try {
					cmdPkt.execute(values);
					dialog.close();
				}
				catch (Throwable e1) {e1.printStackTrace(); System.out.println("ERROR WITH EXECUTION");};
			}
		};
	}
	

	private static final Scrollable[] getTexts(Shell dialog, CmdPkt cmdPkt) {
		ArrayList<Scrollable> texts = new ArrayList<Scrollable>();
		
		final CmdParam[] params = cmdPkt.getParamList().toArray(new CmdParam[cmdPkt.getParamList().size()]);
		
		for (int i = 0; i < params.length; i++) {
			if (!(params[i].getType() == ParamType.SPARE))	{
				GenericPrompt.addLabel(dialog, cmdPkt.getParameterNames()[i]);
				if (params[i].isInputParam())
					texts.add(GenericPrompt.getText(dialog));
				else {
					String[] names = ChoiceOption.getNames(params[i].getChoiceOptions());
					texts.add(GenericPrompt.getCombo(dialog, names));
				}
			}
		}
		
		return texts.toArray(new Scrollable[texts.size()]);
	}
}
