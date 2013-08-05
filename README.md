  <p align="center" >
	  <img src="https://raw.github.com/joebenassi/JCAT/master/JCAT/res/Images/JCATLogoHuge.png">
	</p>
#Java Command And Telemetry (JCAT)
JCAT is a Java program created at the NASA Goddard Space Flight Center to aid in CFS App development. The CFS (Core Flight System) is a flight software product line that is being used by multiple NASA centers. The CFS provides mission-independent, reusable, core flight software services and applications. The cFE (Core Flight Executive) is a set of applications for the CFS developed by NASA. 
	
The CFS, being so reusable, can run on various platforms, and hopes to create a community of developers to create Apps to incorperate into the CFS for mutual benefit. As a result, NASA has released versions of the CFS that are open-source, and has released JCAT as a tool for independant App development.
	
##How does JCAT help?
JCAT is the first open-source ground system for CFS App development. It is designed to be a lightwieght, rapid-application prototype development program. JCAT accompishes three tasks to aid in CFS App development: command-sending, telemetry-displaying, and event message-displaying. To test an App, developers simply need to load its 'App Profile' into JCAT at runtime. 

##What is an App Profile?
An App Profile is an XML file that describes all the App information that JCAT needs to send commands and display telemetry from it. You must create or obtain a correctly-defined App Profile in order to use an App. Examples of App Profiles are available in "App Profiles (CFE 6.1)" within "App Profiles", and apply to CFE 6.1, which is in development.
To create an App Profile, you must first know all its commands and its telemetry housekeeping packet. Next, you should navigate to "App Profile Creation Guide.docx" in "User Help".
	
##What platforms/JRE's does JCAT support?
JCAT can run on Macintosh, Windows, and Linux on 32 or 64-bit JRE's of Java 6 or later, though it has been designed for Windows. JCAT can test Apps in a CFS running on a virtual machine, over LAN, or over a router.

##How do I get started using JCAT?
1. Determine your Java JRE, and click the corresponding JCAT version in the "Executables" folder. Click 'View Raw' and save it to your hard drive. 
2. Navigate to "App Profiles", open the folder of your cFE version, and download the files. 
3. Navigate to "Constant Definition Files", and open the file of your cFE version. 
4. Double-click JCAT_XXXXX.jar, and follow the instructions in Help > Getting Started. (for Mac users, you must open Terminal, navigate to the location of JCAT, and type "java -XstartOnFirstThread -jar " + the name of your JCAT jarfile.

##How do I get started developing for JCAT?
1. Email <thisemail> for permission to be added as a collaborator on GitHub. This will allow you to push changes.
2. Download Eclipse Java EE IDE.
3. Download EGit from the Marketplace, and set it up correctly.
4. Open the Repositories view, and pull JCAT.

For further instructions, navigate to "Developer Documentation" and open "Configuring Eclipse".
