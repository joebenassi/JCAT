package gui.popup;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

/**
 * IN DEVELOPMENT. SUBJECT TO CHANGE/REMOVAL. NOT CURRENTLY IMPLEMENTED.
 * 
 * @author Joe Benassi
 *
 */
public class ProgressBarPopup {
  public static void main(String[] args) {
	 final int height = 600;
	 final int width = 100;
	 final int x = 100; 
	 final int y = 100;
	  
	  
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.setLayout(new FillLayout());

    // Create an indeterminate ProgressBar
    ProgressBar pb2 = new ProgressBar(shell, SWT.HORIZONTAL | SWT.INDETERMINATE);
    pb2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));


    shell.open();

    for (int i = 0; i < 100; i++)
    {
    	if (i%2 > 0)
    	shell.setBounds(x, y, height + 1, width + 1);
    	else 
    		shell.setBounds(x, y, height, width);
    }
    
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
  }
}
/*
public class ProgressBarPopup {
	private final int maximumProgress;
	private int currentLoadAmount;
	private final int maxLoadAmount;
	private final Display display;
	private final Shell shell;
	private ProgressBar bar;

	public ProgressBarPopup(int loadAmount) {
		this.maxLoadAmount = loadAmount - 1;
		this.currentLoadAmount = 0;

		display = new Display();
		shell = new Shell(display);
		bar = new ProgressBar(shell, SWT.SMOOTH);
		Rectangle clientArea = shell.getClientArea();
		bar.setBounds(clientArea.x, clientArea.y, 200, 32);
		maximumProgress = bar.getMaximum();
		shell.open();
	}

	public synchronized void doWork() {
		System.out.println("THREAD STARTED");
		new Thread(new Runnable(){
			public void run() {
				
				while (true) {
					if (currentLoadAmount == maxLoadAmount) return;
					System.out.println("PROGRESS BAR: " + currentLoadAmount);
					System.out.println("MAX LOad amount" + maxLoadAmount);
					try {
						Thread.sleep(100);
					} catch (Throwable th) {
					}
					if (display.isDisposed())
						return;
					display.asyncExec(new Runnable() {
						public void run() {
							if (bar.isDisposed())
								return;
							bar.setSelection((int) ((double) maximumProgress
									* (double) getCurrentLoadAmount() / (double) maxLoadAmount));
						}
					});}}}).start();
		
		display.asyncExec(new Runnable() {
			public void run() 
			{
				while (!shell.isDisposed()) 
				{
					shell.update();
					shell.redraw();
					shell.pack();
					if (!display.readAndDispatch ()) 
						display.sleep ();
					display.dispose();
				}
				
			}});
	}

	private synchronized int getCurrentLoadAmount() {
		return currentLoadAmount;
	}

	private final void setCurrentLoadAmount(int i) {
		currentLoadAmount = i;
	}

	public static void main(String[] args) {
		final ProgressBarPopup progressBar = new ProgressBarPopup(10);
		new Thread(new Runnable() {
			public void run() {
				System.out.println("STARTED");
				progressBar.doWork();
			}

		}).start();

		System.out.println("THREAD STARTED2");
		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(500);
			} catch (Throwable e) {
			}
			progressBar.setCurrentLoadAmount(i);
			System.out.println(i);
		}
	}
}*/
