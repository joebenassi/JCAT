package app;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class HelpDialog extends Dialog
{

   final String helpStr = "Coming soon...\n";
   
   // Constructors
   
   public HelpDialog(Shell parent) {
     // Pass the default styles here
     this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
   }
   public HelpDialog(Shell parent, int style) {
     // Let users override the default styles
     super(parent, style);
     setText("Instructions for use ...");
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
   } // End open()

   private void createContents(final Shell shell) {

      shell.setLayout(new GridLayout(1, false));
      
      StyledText helpText = new StyledText( shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
      GridData data = new GridData(GridData.FILL_HORIZONTAL);
      helpText.setLayoutData(data);
      StyleRange styleRange1 = new StyleRange();
      styleRange1.start = helpText.getCharCount();
      styleRange1.length = helpStr.length();
      styleRange1.foreground = shell.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN);
      styleRange1.fontStyle = SWT.NORMAL;
      
      helpText.append(helpStr + "\r\n");
      helpText.setStyleRange(styleRange1);
      helpText.setSelection(helpText.getCharCount());


      // OK button handler
      Button ok = new Button(shell, SWT.PUSH);
      ok.setText("OK");
      data = new GridData(GridData.CENTER);
      ok.setLayoutData(data);
      ok.addSelectionListener(new SelectionAdapter() {
         @Override
		public void widgetSelected(SelectionEvent event) {
            shell.close();
         }
         });

     shell.setDefaultButton(ok);

   } // End createContents   
   
} // End class ProjInputDialog
