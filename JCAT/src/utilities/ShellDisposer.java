package utilities;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Shell;

public class ShellDisposer {
	private static ArrayList<Shell> popups = new ArrayList<Shell>();
	
	public static final void disposePopups()
	{
		for (int i = 0; i < popups.size(); i ++)
			popups.get(i).dispose();
		
		popups = new ArrayList<Shell>();
	}
	
	public static final void queueForDisposal(Shell s)
	{
		popups.add(s);
	}
}
