package app;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class FileTypeDialog extends Dialog
{
   
   boolean xmlSelected = true;
   boolean cancelled   = true;
   
   // Constructors
   
   public FileTypeDialog(Shell parent) {
     // Pass the default styles here
     this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
   }
   public FileTypeDialog(Shell parent, int style) {
     // Let users override the default styles
     super(parent, style);
     setText("Select project input file type ...");
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

      final Button radioXML = new Button(shell, SWT.RADIO);
      GridData data = new GridData();
      radioXML.setLayoutData(data);
      radioXML.setText("XML Project File");
      Button radioSLIC = new Button(shell, SWT.RADIO);
      data = new GridData(GridData.FILL_HORIZONTAL);
      radioSLIC.setLayoutData(data);
      radioSLIC.setText("SLIC Text File");
      radioXML.setSelection(true);
      radioXML.setFocus();
      
      // OK button handler
      final Button ok = new Button(shell, SWT.PUSH);
      ok.setText("OK");
      data = new GridData(GridData.FILL_HORIZONTAL);
      ok.setLayoutData(data);
      ok.addSelectionListener(new SelectionAdapter() {
      @Override
	public void widgetSelected(SelectionEvent event) {
         xmlSelected = radioXML.getSelection(); // Assumes only two buttons
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
   
   public boolean xmlSelected(){
      return xmlSelected;
   }

   public boolean cancelled(){
      return cancelled;
   }
   
} // End class ReportDialog
