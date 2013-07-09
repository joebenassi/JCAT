package app;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class CmdTestDialog extends Dialog
{

   private String   cmdID;
   private String   cmdParam;

   // Constructors
   
   public CmdTestDialog(Shell parent) {
     // Pass the default styles here
     this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
   }
   public CmdTestDialog(Shell parent, int style) {
     // Let users override the default styles
     super(parent, style);
     setText("Test Command...");
   }

   public int getCmdID() {
     return Integer.parseInt(cmdID);
   }

   public String getCmdParam() {
     return cmdParam;
   }

   public int open() {
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
     return getCmdID();
   }

   private void createContents(final Shell shell) {

      shell.setLayout(new GridLayout(2, false));

      // Input for project name
      Label projLabel = new Label(shell, SWT.NONE);
      projLabel.setText("Command ID:");
      GridData data = new GridData();
      projLabel.setLayoutData(data);

      // Display the input box
      final Text cmdText = new Text(shell, SWT.BORDER);
      data = new GridData(GridData.FILL_HORIZONTAL);
      cmdText.setLayoutData(data);

      // Input for version
      Label paramLabel = new Label(shell, SWT.NONE);
      paramLabel.setText("Parameter:");
      data = new GridData();
      paramLabel.setLayoutData(data);

      // Display the input box
      final Text paramText = new Text(shell, SWT.BORDER);
      data = new GridData(GridData.FILL_HORIZONTAL);
      paramText.setLayoutData(data);

     // OK button handler
     Button ok = new Button(shell, SWT.PUSH);
     ok.setText("OK");
     data = new GridData(GridData.FILL_HORIZONTAL);
     ok.setLayoutData(data);
     ok.addSelectionListener(new SelectionAdapter() {
       @Override
	public void widgetSelected(SelectionEvent event) {
         cmdID = cmdText.getText();
         cmdParam = paramText.getText();
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
         cmdID = null;
         shell.close();
       }
     });

     // Set the OK button as the default, so
     // user can type input and press Enter
     // to dismiss
     shell.setDefaultButton(ok);
   }   
   
} // End class CmdTestDialog
