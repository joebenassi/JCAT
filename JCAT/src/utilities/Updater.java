package utilities;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * IN DEVELOPMENT. SUBJECT TO CHANGE.
 * 
 * Updates the various instances of displayed text of GMT, UTC, SC, and Sequence
 * Count throughout the application. DisplaySubjects are GMT, UTC, SC, and
 * Sequence Count. A display instance is any single display of a
 * DisplaySubject's value in the form of a Text or Label.
 * 
 * @author Joe Benassi
 * 
 */
public final class Updater {
	private static final int sleepDuration = 1000;

	/**
	 * Represents either GMT, UTC, SC, SequenceCount, or Unknown. Contains a
	 * String <code>text</code> that holds the text representing the current
	 * status of the DisplaySubject. For example, <code>text</code> for the
	 * DisplaySubject <code>GMT</code> might hold "
	 * <code>13-165-17:57:19.322</code>".
	 * 
	 * @author Joe Benassi
	 * 
	 */
	enum DisplaySubject {
		GMT("0"), UTC("0"), SC("0"), SequenceCount("0"), Unknown(
				"ERROR");

		private ArrayList<Text> textDisplayInstances = new ArrayList<Text>();
		private ArrayList<Label> labelDisplayInstances = new ArrayList<Label>();
		
		/**
		 * The text representing the current status of this. If this were
		 * DisplaySubject.GMT, it might equal "13-165-17:57:19.322".
		 */
		private String text;
		private Thread updaterThread;

		/**
		 * Represents either GMT, UTC, SC, SequenceCount, or Unknown. Contains a
		 * String <code>text</code> that holds the text representing the current
		 * status of the DisplaySubject.
		 * 
		 */
		private DisplaySubject(String initText) {
			setDisplayText(initText);
			configureThread();
			startUpdaterThread();
		}

		private final void configureThread()
		{
			updaterThread = new Thread(new Runnable() {
				public void run() {
					while (true) {
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								updateDisplays();
							}
						});
						try {
							Thread.sleep(sleepDuration);
						} catch (Exception e) {
						}
					}
				}
			});
			updaterThread.setDaemon(true);
		}
		
		private final void startUpdaterThread()
		{
			updaterThread.start();
		}
		
		public final void addDisplayInstance(Text t)
		{
			textDisplayInstances.add(t);
		}
		
		public final void addDisplayInstance(Label l)
		{
			labelDisplayInstances.add(l);
		}
		
		private final void updateDisplays()
		{
			for (int i = 0; i < textDisplayInstances.size(); i++)
				textDisplayInstances.get(i).setText(text);
			for (int i = 0; i < labelDisplayInstances.size(); i++)
				labelDisplayInstances.get(i).setText(text);
		}
		
		/**
		 * Sets the text of its DisplaySubject. Used to update all instances of
		 * display of its DisplaySubject.
		 * 
		 * @param text
		 *            The text to output to all instances of display of its
		 *            DisplaySubject.
		 */
		private final void setDisplayText(String text) {
			this.text = text;
		}
	}

	/**
	 * Displays the input text wherever the GMT is displayed.
	 * 
	 * @param text
	 *            The text to output to wherever the GMT is displayed.
	 */
	public final static void setGMTValue(String text) {
		DisplaySubject.GMT.setDisplayText(text);
	}

	/**
	 * Displays the input text wherever the UTC is displayed.
	 * 
	 * @param text
	 *            The text to output to wherever the UTC is displayed.
	 */
	public final static void setUTCValue(String text) {
		DisplaySubject.UTC.setDisplayText(text);
	}

	/**
	 * Displays the input text wherever the Sequence Count is displayed.
	 * 
	 * @param text
	 *            The text to output to wherever the Sequence Count is
	 *            displayed.
	 */
	public final static void setSequenceCountValue(String text) {
		DisplaySubject.SequenceCount.setDisplayText(text);
	}

	/**
	 * Displays the input text wherever the SC is displayed.
	 * 
	 * @param text
	 *            The text to output to wherever the SC is displayed.
	 */
	public final static void setSCValue(String text) {
		DisplaySubject.SC.setDisplayText(text);
	}

	/**
	 * Returns the DisplaySubject whose name is equivalent to the input String.
	 * Input strings could be "GMT", "SC", "UTC", or "SequenceCount". If none of
	 * these are chosen, this returns the DisplaySubject Unknown, which displays
	 * "ERROR" wherever its value is displayed.
	 * 
	 * @param type
	 *            The string representation of the DisplaySubject desired to
	 *            return.
	 * @return The DisplaySubject whose name is equivalent to the input String
	 */
	private final static DisplaySubject getDisplaySubject(String type) {
		if (type.equalsIgnoreCase("GMT"))
			return DisplaySubject.GMT;
		else if (type.equalsIgnoreCase("SC"))
			return DisplaySubject.SC;
		else if (type.equalsIgnoreCase("UTC"))
			return DisplaySubject.UTC;
		else if (type.equalsIgnoreCase("Sequence Count"))
			return DisplaySubject.SequenceCount;

		return DisplaySubject.Unknown;
	}

	/**
	 * Automatically and continually updates the input Label to have the text
	 * representing the status of the DisplaySubject whose name is equivalent to
	 * the input String.
	 * 
	 * @param label
	 *            The Label to update.
	 * @param name
	 *            The name of the DisplaySubject it should display the status
	 *            of.
	 */
	public final static void addUpdater(final Label label, String name) {
		getDisplaySubject(name).addDisplayInstance(label);
	}

	/**
	 * Automatically and continually updates the input Text to have the text
	 * representing the status of the DisplaySubject whose name is equivalent to
	 * the input String.
	 * 
	 * @param text
	 *            The Text to update.
	 * @param name
	 *            The name of the DisplaySubject it should display the status
	 *            of.
	 */
	public final static void addUpdater(final Text text, String name) {
		getDisplaySubject(name).addDisplayInstance(text);
	}
}