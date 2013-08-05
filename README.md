  <p align="center" >
	  <img src="https://raw.github.com/joebenassi/JCAT/master/JCAT/Git-Resources/JCATLogoHuge.png">
	</p>
#Java Command And Telemetry (JCAT)
JCAT is a Java program created at the NASA Goddard Space Flight Center to aid in CFS App development. The CFS (Core Flight System) is a flight software product line that is being used by multiple NASA centers. The CFS provides mission-independent, reusable, core flight software services and applications. The cFE (Core Flight Executive) is a set of applications for the CFS also developed by NASA. 
	
Versions of the CFS are open-source, and can run on various platforms, including RTEMS virtual machines, embedded ColdFire processors, and, in the future, Rasperry Pi processors. A ground system, such as JCAT, was the missing link to allow a community of CFS developers to form and create Apps for their own and each others' use.
	
##How does JCAT help?
JCAT is the first open-source ground system for CFS App development. It is designed to be a lightwieght, rapid-application prototype development program. JCAT accompishes three tasks to aid in CFS App development: command-sending, telemetry-displaying, and event message-displaying. To test an App, developers simply need to connect JCAT to the CFS containing their App via ethernet cable, router, or virtual machine, and load its 'App Profile' into JCAT at runtime. 

##What is an App Profile?
An App Profile is an XML file that describes all the information JCAT needs to send commands to and display telemetry from an App. You must create or obtain a correctly-defined App Profile in order to use an App. Examples of App Profiles are available in "JCAT/App-Profiles/CFE-6.1/".
To create an App Profile, you must first know all its commands and the form of its telemetry housekeeping. Then, navigate to "JCAT/User-Help/App_Profile_Creation_Guide.docx".
	
##What platforms/JRE's does JCAT support?
JCAT can run on Macintosh, Windows, and Linux on 32 or 64-bit JRE's of Java 6 or later, though it has been designed for Windows. JCAT can test Apps from the CFS running on a virtual machine, over LAN, or through a router.

##How do I get started using JCAT?
1. Determine your Java JRE, and click the corresponding JCAT version in "JCAT/Executables/". Click 'View Raw', and save JCAT to your hard drive. 
2. Navigate to "JCAT/App-Profiles/", open the folder of your cFE version, and download the files. 
3. Navigate to "JCAT/Constant-Definition-Files", and download the file of your cFE version. 
4. Double-click JCAT_XXXXX.jar, and follow the instructions in Help > Getting Started. (for Mac users, you must open Terminal, navigate to the location of JCAT, and type "java -XstartOnFirstThread -jar " and the name of your JCAT jarfile.

##How do I get started developing for JCAT?
1. Email <thisemail> for permission to be added as a collaborator on GitHub. This will allow you to push changes.
2. Download Eclipse Java EE IDE.
3. Download EGit from the Marketplace, and set it up correctly.
4. Open the Repositories view, and pull JCAT.

For further instructions, navigate to "Developer Documentation" and open "Configuring Eclipse".
