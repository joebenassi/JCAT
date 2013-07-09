package app;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class ReportDialog extends Dialog
{
   
   boolean groupOnly = true;
   boolean cancelled = true;
   
   // Constructors
   
   public ReportDialog(Shell parent) {
     // Pass the default styles here
     this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
   }
   public ReportDialog(Shell parent, int style) {
     // Let users override the default styles
     super(parent, style);
     setText("Generate Report...");
   }

   public void open() {
     // Create the dialog window
     Shell shell = new Shell(getParent(), getStyle());
     shell.setText(getText());
     createContents(shell);
     shell.pack();
     shell.open();
     Display display = getParent().getDisplay();
     while (!shell.isDisposed()) {
       if (!display.readAndDispatch()) {
         display.sleep();
       }
     }
     // Return the entered value, or null
   }

   private void createContents(final Shell shell) {

      shell.setLayout(new GridLayout(2, false));

      final Button radioGroupOnly = new Button(shell, SWT.RADIO);
      GridData data = new GridData();
      radioGroupOnly.setLayoutData(data);
      radioGroupOnly.setText("Groups Only");
      Button radioGroupDir = new Button(shell, SWT.RADIO);
      data = new GridData(GridData.FILL_HORIZONTAL);
      radioGroupDir.setLayoutData(data);
      radioGroupDir.setText("Groups and Directories");
      radioGroupDir.setSelection(true);
      radioGroupOnly.setFocus();
      
      // OK button handler
      final Button ok = new Button(shell, SWT.PUSH);
      ok.setText("OK");
      data = new GridData(GridData.FILL_HORIZONTAL);
      ok.setLayoutData(data);
      ok.addSelectionListener(new SelectionAdapter() {
      @Override
	public void widgetSelected(SelectionEvent event) {
         groupOnly = radioGroupOnly.getSelection(); // Assumes only two buttons
         cancelled = false;
         shell.close();
      }
      });

     // Cancel button handler
     Button cancel = new Button(shell, SWT.PUSH);
     cancel.setText("Cancel");
     data = new GridData(GridData.FILL_HORIZONTAL);
     cancel.setLayoutData(data);
     cancel.addSelectionListener(new SelectionAdapter() {
       @Override
	public void widgetSelected(SelectionEvent event) {
         shell.close();
       }
     });

     // Set the OK button as the default, so
     // user can type input and press Enter
     // to dismiss
     shell.setDefaultButton(ok);
   }   
   
   public boolean groupOnly(){
      return groupOnly;
   }

   public boolean cancelled(){
      return cancelled;
   }
   
} // End class ReportDialog
