/*******************************************************************************
 * 
 * This application provides a simple command and telemetry interface to the 
 * cFE.
 * 
 * @todo - Replace GUI  
 *
 *******************************************************************************/

package app;

import java.util.ArrayList;   
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import fsw.CmdIntParam;
import fsw.CmdPkt;
import fsw.CmdStrParam;
import fsw.EsApp;
import network.PktEventInterface;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import curval.*;
import fsw.*;
import ccsds.*;

/**
 *  
 */
public class JcatApp extends ApplicationWindow 
{

   StyledText  textTlmLog;
   StyledText  textFswEventLog;
   StyledText  textUserActivityLog;

   Action actionInitNetwork;
   Action actionEnaToTlm;
   Action actionCfgToPkt;
   Action actionSendCmd;
   Action actionDisplayPkt;
   Action actionExit;

   Action actionDisplayNetworkInfo;
   
   Action actionDisplayAbout;
   Action actionDisplayHelp;


   FswCmdNetwork  CmdWriter = null;
   FswTlmNetwork  TlmReader = null;
   
   private TlmListener    TlmDisplay;
   private TlmPktDatabase TlmDatabase = null;

   private int DisplayStreamId = 0;
   
   private EsApp  ES  = new EsApp(EsApp.PREFIX_STR,"Executive Services Application");
   private ToApp  TO  = new ToApp(ToApp.PREFIX_STR,"Telemetry Output");
   private EvsApp EVS = new EvsApp(EvsApp.PREFIX_STR,"cFE Event Service");
   private ExApp  EX  = new ExApp(ExApp.PREFIX_STR,"Example Application");
     
    /**
    * @param parentShell
    */
   public JcatApp(Shell parentShell) 
   {
      super(parentShell);

      createActions();

      addStatusLine();
      addToolBar(SWT.FLAT);
      addMenuBar();
      
      TlmDatabase = new TlmPktDatabase();
      TlmDisplay = new TlmListener(); 
      TlmDatabase.registerObserver(TlmDisplay);
      // @todo - Possibly add default behavior based on previous application execution
 
   } // End JcatApp()
   
   
   private void createActions() 
   {

      /*
       *  ACTION: Initialize network connection to CFS
       *  
       */
      actionInitNetwork = new Action() 
      {
         public void run() 
         {
            
            logUserActivity("Initialize Network", true);
            CmdWriter = new FswCmdNetwork();
            TlmReader = new FswTlmNetwork(TlmDatabase);
            logNetworkStatus();            

            createTlmMonitorThread();
            
         } // End run()
      }; // End Action()
      actionInitNetwork.setText("Init Network");
      actionInitNetwork.setToolTipText("Initialize network connections with CI and TO");
      //actionInitNetwork.setImageDescriptor(ImageDescriptor.createFromFile(null, ""));

      /*
       *  ACTION: Enable TO Lab Telemetry
       *  
       */
      actionEnaToTlm = new Action() 
      {
         public void run() 
         {
            
            logUserActivity("Enable TO Lab Telemetry", true);
            CmdPkt Cmd = TO.getCmdList().get(ToApp.CMD_FC_ENA_TLM);
            CmdWriter.sendCmd(Cmd.getCcsdsPkt());
            
         } // End run()
      }; // End Action()
      actionEnaToTlm.setText("Enable TO TLM");
      actionEnaToTlm.setToolTipText("Enable TO Lab's Telemetry");
      //actionEnaToTlm.setImageDescriptor(ImageDescriptor.createFromFile(null, ""));

      /*
       *  ACTION: Configure (enable/disable) which packets are output by TO Lab
       *   
       */
      actionCfgToPkt = new Action() 
      {
         public void run() 
         {

            logUserActivity("Configure TO Lab Packets", true);
            CmdPkt Cmd = TO.getCmdList().get(ToApp.CMD_FC_ADD_PKT); // Use default
            CmdWriter.sendCmd(Cmd.getCcsdsPkt());

            // @todo - Fix CmdPkt bug. The default CmdParam are not actually loaded into the parameter list
/*
            ArrayList<CmdParam>  ParamList = new ArrayList<CmdParam>();
            ParamList.add(new CmdParam("Message ID",EsApp.TLM_MID_HK, 2)); // ExApp.TLM_MID_PKT1: 3840 = 0xF00, 0x0800 = ES HK
            ParamList.add(new CmdParam("Pkt Size",50, 2));
            ParamList.add(new CmdParam("SB QoS",0, 2));
            ParamList.add(new CmdParam("Buffer Cnt",1, 1));
            Cmd.LoadParams(ParamList);
*/
            
               
         } // End run()
      }; // End Action()
      actionCfgToPkt.setText("Config TO Pkt");
      actionCfgToPkt.setToolTipText("Enable/Disable packets for Telemetry Output");
      //actionCfgToPkt.setImageDescriptor(ImageDescriptor.createFromFile(null, ""));

      /*
       *  ACTION: Send a user selected command
       *   
       */
      actionSendCmd = new Action() 
      {
         public void run() 
         {

            int     cmdID;
            String  cmdParam;
            CmdPkt  cmdPkt;
            ArrayList<CmdParam> cmdParamList;
            
            CmdTestDialog cmdDialog = new CmdTestDialog(getShell());
            cmdID = cmdDialog.open();
            cmdParam = cmdDialog.getCmdParam();
            
            switch (cmdID) 
            {
            case 0:
               logUserActivity("EVS Cmd: Noop", true);
               cmdPkt = EVS.getCmdList().get(EvsApp.CMD_FC_NOOP);
               CmdWriter.sendCmd(cmdPkt.getCcsdsPkt());
               break;
            case 1:
               logUserActivity("ES Cmd: Noop", true);
               cmdPkt = ES.getCmdList().get(EsApp.CMD_FC_NOOP);
               CmdWriter.sendCmd(cmdPkt.getCcsdsPkt());
               break;

            case 2:
               logUserActivity("ES Cmd: Set Sys Log Mode", true);
               cmdPkt = ES.getCmdList().get(EsApp.CMD_FC_OVERWRITE_SYSLOG);
               cmdPkt.setParam(0, cmdParam);
               CmdWriter.sendCmd(cmdPkt.getCcsdsPkt());
               break;
               
            case 3:
               logUserActivity("ES Cmd: Write Sys Log to a File", true);
               cmdPkt = ES.getCmdList().get(EsApp.CMD_FC_WRITE_SYSLOG);
               // Use default cmdPkt.setParam(0, cmdParam);
               CmdWriter.sendCmd(cmdPkt.getCcsdsPkt());
               break;

            case 4:
               logUserActivity("ES Cmd: Write Err Log to a File", true);
               cmdPkt = ES.getCmdList().get(EsApp.CMD_FC_WRITE_ERLOG);
               // Use default cmdPkt.setParam(0, cmdParam);
               CmdWriter.sendCmd(cmdPkt.getCcsdsPkt());
               break;

            case 5:
               logUserActivity("ES Cmd: Restart Application", true);
               cmdPkt = ES.getCmdList().get(EsApp.CMD_FC_RESTART_APP);
               // Use default cmdPkt.setParam(0, cmdParam);
               CmdWriter.sendCmd(cmdPkt.getCcsdsPkt());
               break;

            case 9:
               logUserActivity("Send EX Noop", true);
               CmdPkt ExCmd = EX.getCmdList().get(ExApp.CMD_FC_NOOP);
               CmdWriter.sendCmd(ExCmd.getCcsdsPkt());
               break;

            default:
               logUserActivity("Invalid command ID " + cmdID, true);
               System.out.println("Invalid command ID " + cmdID); 
            
            } // End command switch
            
            
         } // End run()
      }; // End Action()
      actionSendCmd.setText("Send Command");
      actionSendCmd.setToolTipText("Send a command");
      //actionSendNoop.setImageDescriptor(ImageDescriptor.createFromFile(null, ""));

      /*
       *  ACTION: Select a packet that will be displayed in the test field
       *   
       */
      actionDisplayPkt = new Action() 
      {
         public void run() 
         {

            DisplayStreamId = ExApp.TLM_MID_PKT1; // 0x0F00 
            DisplayStreamId = EsApp.TLM_MID_HK;   // 0x0800
            logUserActivity("Display packet " + DisplayStreamId, true);
            
         } // End run()
      }; // End Action()
      actionDisplayPkt.setText("Display Pkt");
      actionDisplayPkt.setToolTipText("Select which packet to display in telemetry window");
      //actionDisplayPkt.setImageDescriptor(ImageDescriptor.createFromFile(null, ""));

      /*
       *  ACTION: Exit the application
       *   
       */
      actionExit = new Action() {
         public void run()
         {
            if(! MessageDialog.openConfirm(getShell(), "Confirm", "Are you sure you want to exit?")) 
               return;
            // TODO - Add dirty file checks and saving

            close();
         } // End run()
      };
      actionExit.setText("Exit");
      
      /*
       *  ACTION: Display Network Information
       *   
       */
      actionDisplayNetworkInfo = new Action() 
      {
         public void run() 
         {
            logNetworkStatus();
            
         } // End run()
      }; // End Action()
      actionDisplayNetworkInfo.setText("Traffic Stats");
      actionDisplayNetworkInfo.setToolTipText("Display ntework traffic statistics");

      actionDisplayAbout = new Action() {
         public void run() {
            MessageDialog.openInformation(getShell(), "About", "SWT CFS Command and Telemetry Prototype\nProvide basic SWT application framework with interface to UDP CI and TO");
         }
      };
      actionDisplayAbout.setText("About");
      //actionDisplayAbout.setImageDescriptor(ImageDescriptor.createFromFile(null, ""));
      
      actionDisplayHelp = new Action() {
         public void run() {
            HelpDialog help = new HelpDialog(getShell());
            help.open();
         }
      };
      actionDisplayHelp.setText("Help");
      actionDisplayHelp.setToolTipText("Instructions for use");
      
   } // End createActions()


   /*
    * (non-Javadoc)
    * 
    * @see org.eclipse.jface.window.ApplicationWindow#createMenuManager()
    */
   protected MenuManager createMenuManager()
   {
      MenuManager bar = new MenuManager();

      MenuManager menuFile = new MenuManager("&Test");
      menuFile.add(actionInitNetwork);
      menuFile.add(new Separator());
      menuFile.add(actionEnaToTlm);
      menuFile.add(actionCfgToPkt);
      menuFile.add(actionSendCmd);
      menuFile.add(new Separator());
      menuFile.add(actionDisplayPkt);
      menuFile.add(new Separator());
      menuFile.add(actionExit);
      
      MenuManager menuOptions = new MenuManager("Network");
      menuOptions.add(actionDisplayNetworkInfo);

      MenuManager menuHelp = new MenuManager("&Help");
      menuHelp.add(actionDisplayAbout);
      menuHelp.add(actionDisplayHelp);

      bar.add(menuFile);
      bar.add(menuOptions);
      bar.add(menuHelp);
      bar.updateAll(true);

      return bar;
      
   } // End MenuManager()

   public static void addAction(
      ToolBarManager manager,
      Action action,
      boolean displayText) {
      if (!displayText) {
         manager.add(action);
         return;
      } else {
         ActionContributionItem item = new ActionContributionItem(action);
         item.setMode(ActionContributionItem.MODE_FORCE_TEXT);
         manager.add(item);
      }
   }

   
   /* (non-Javadoc)
    * @see org.eclipse.jface.window.ApplicationWindow#createToolBarManager(int)
    */
   protected ToolBarManager createToolBarManager(int style) {
      ToolBarManager manager = super.createToolBarManager(style);

      // Network actions
      addAction(manager, actionInitNetwork, true);

      // Command actions
      addAction(manager, actionEnaToTlm, true);
      addAction(manager, actionCfgToPkt, true);
      addAction(manager, actionSendCmd, true);
      manager.add(new Separator());

      // Telemetry actions
      addAction(manager, actionDisplayPkt, true);
      manager.add(new Separator());

      // Command actions
      addAction(manager, actionDisplayNetworkInfo, true);
      manager.add(new Separator());

      addAction(manager, actionDisplayHelp, true);      

      manager.update(true);      
      
      return manager;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.eclipse.jface.window.Window#createContents(org.eclipse.swt.widgets.Composite)
    */
   protected Control createContents(Composite parent) 
   {
   
      Composite composite = new Composite(parent, SWT.NULL);
      composite.setLayout(new FillLayout(SWT.VERTICAL));

      // The vertical sashform (controls are set vertically and sash is horizontal)
      SashForm verticalForm = new SashForm(composite, SWT.VERTICAL);
      verticalForm.setLayout(new FillLayout());
      
      // Composite Telemetry window

      Composite compositeTextTlmWin = new Composite(verticalForm, SWT.NULL);
      compositeTextTlmWin.setLayout(new FillLayout());

      Group compositeTextTlmGroup = new Group(compositeTextTlmWin, SWT.NULL);
      compositeTextTlmGroup.setText("Telemetry");
      compositeTextTlmGroup.setLayout(new FillLayout());

      textTlmLog = new StyledText(compositeTextTlmGroup, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);

      // Composite FSW Event Window

      Composite compositeTextFswEventWin = new Composite(verticalForm, SWT.NULL);
      compositeTextFswEventWin.setLayout(new FillLayout());

      Group compositeTextFswEventGroup = new Group(compositeTextFswEventWin, SWT.NULL);
      compositeTextFswEventGroup.setText("FSW Event Messages");
      compositeTextFswEventGroup.setLayout(new FillLayout());

      textFswEventLog = new StyledText( compositeTextFswEventGroup, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
      textFswEventLog.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));


      // Composite User Activity Window

      Composite compositeTextUserActivityWin = new Composite(verticalForm, SWT.NULL);
      compositeTextUserActivityWin.setLayout(new FillLayout());

      Group compositeTextUserActivityGroup = new Group(compositeTextUserActivityWin, SWT.NULL);
      compositeTextUserActivityGroup.setLayout(new FillLayout());
      compositeTextUserActivityGroup.setText("User Activity");

      textUserActivityLog = new StyledText( compositeTextUserActivityGroup, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
     
      // resize sashform children adn give each child equal space
      verticalForm.setWeights(new int[]{1,1,1});
     
      getToolBarControl().setBackground(
         new Color(getShell().getDisplay(), 230, 230, 230));

      //getShell().setImage(new Image(getShell().getDisplay(), ""));
      getShell().setText("CFS Command and Telemetry Prototype");
      
      return composite;
      
   } // End createContent()

   
   private void logNetworkStatus() {

      String LogString;
      
      if (CmdWriter != null && TlmReader != null)
      {
         LogString = new String("Network Status: " + CmdWriter.getStatus() + TlmReader.getStatus());
      }
      else
         LogString = new String("Network not initialized");
     
      logUserActivity(LogString, true);
      
   } // End getProjectInfo()


   private void logTlmPacket(String message) {
      
      StyleRange styleRange1 = new StyleRange();
      styleRange1.start = textTlmLog.getCharCount();
      styleRange1.length = message.length();
      styleRange1.foreground = getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN);
      styleRange1.fontStyle = SWT.NORMAL;
      
      textTlmLog.append(message + "\r\n");
      textTlmLog.setStyleRange(styleRange1);
      textTlmLog.setSelection(textTlmLog.getCharCount());
      
   } // End logTlmPacket()

   private void logTlmPacket(String[] message, int len) {
      
      for (int i=0; i < len; i++)
      {
          textTlmLog.append(message[i] + "\r\n");
      }
      
   } // End logTlmPacket()

   private void logFswEventMessage(String message) {
      
      StyleRange styleRange1 = new StyleRange();
      styleRange1.start = textFswEventLog.getCharCount();
      styleRange1.length = message.length();
      styleRange1.foreground = getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN);
      styleRange1.fontStyle = SWT.NORMAL;
      
      textFswEventLog.append(message + "\r\n");
      textFswEventLog.setStyleRange(styleRange1);
      textFswEventLog.setSelection(textFswEventLog.getCharCount());
      
   } // End logFswEventMessage()

   private void logUserActivity(String message, boolean showInStatusBar) {
      
      StyleRange styleRange1 = new StyleRange();
      styleRange1.start = textUserActivityLog.getCharCount();
      styleRange1.length = message.length();
      styleRange1.foreground = getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN);
      styleRange1.fontStyle = SWT.NORMAL;
      
      textUserActivityLog.append(message + "\r\n");
      textUserActivityLog.setStyleRange(styleRange1);
      textUserActivityLog.setSelection(textUserActivityLog.getCharCount());
      
      if(showInStatusBar) {
         setStatus(message);
      }
   } // End logUserActivity()

   private void logError(String message) {
      
      StyleRange styleRange1 = new StyleRange();
      styleRange1.start = textUserActivityLog.getCharCount();
      styleRange1.length = message.length();
      styleRange1.foreground = getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_RED);
      styleRange1.fontStyle = SWT.NORMAL;    
      
      textUserActivityLog.append(message + "\r\n");
      textUserActivityLog.setStyleRange(styleRange1);
      textUserActivityLog.setSelection(textUserActivityLog.getCharCount());
   }

   // @todo - This callback method didn't work because function does not execute within
   //         the context of the display and an exception is generated when window is updated.
   private class TlmListener implements PktObserver {

      @Override
	public void update(int StreamId)
      {
         System.out.println("TlmListener called for packet " + StreamId);
         //CcsdsTlmPkt TlmPkt = TlmReader.getTlmPktQ().remove();
         //System.out.println("TlmListener 2");
         //logFswEventMessage("Received Telemetry Packet " + TlmPkt.getStreamId());
         //logTlmPacket("PktObserver);
         //System.out.println("TlmListener 3");
      }
      
   } // End class TlmListener

   
   
   private void createTlmMonitorThread() {

      Timer timer = new Timer ();
      TimerTask TlmTask = new TimerTask () {
      @Override
	public void run() {
          
         Display.getDefault().asyncExec(new Runnable(){
            @Override
			public void run() {
               
               //String TlmStr;
               
               synchronized (TlmReader) {
                  while (!TlmReader.getTlmPktQ().isEmpty())
                  {
                     CcsdsTlmPkt TlmPkt = TlmReader.getTlmPktQ().remove();

                     System.out.println("TlmMonitor dequeued packet " + TlmPkt.getStreamId());
                     
                     // @todo - Create efficient StreamId to app lookup scheme 
                     if (TlmPkt.getStreamId() == 0x0808)
                     {
                        logFswEventMessage(EVS.getTlmStr(TlmPkt));
                     }
                     if (TlmPkt.getStreamId() == DisplayStreamId)
                     {
                        String[] OutputStr = ES.getTlmStrArray(TlmPkt);
                        logTlmPacket(OutputStr, 40);
                        //ExApp EX = new ExApp(ExApp.PREFIX_STR,"Example Application");
                        //logTlmPacket(EX.getTlmStr(TlmPkt));   
                     }
                     
                  } // End while packets in queue
               }
                  
            } // End run() 
          }); // End Runnable()
        } // End run()
      }; // End TimerTask()

      timer.scheduleAtFixedRate (TlmTask, 0, 10);
   
   } // End createTlmMonitorThread() 

   public static void main(String[] args) {
      
      ApplicationWindow window = new JcatApp(null);
      window.setBlockOnOpen(true);

      window.open();
      Display.getCurrent().dispose();
   }

   
} // End class JcatApp

