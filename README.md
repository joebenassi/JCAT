  <p align="center" >
	  <img src="https://raw.github.com/joebenassi/JCAT/master/JCAT/Git-Resources/JCATLogoHuge.png">
	</p>
#Java Command And Telemetry (JCAT)
JCAT is a Java program created at the NASA Goddard Space Flight Center to aid in CFS App development. The CFS (Core Flight System) is a flight software product line used by multiple NASA centers. The CFS provides mission-independent, reusable, core flight software services and applications, also known as "Apps". The cFE (Core Flight Executive) is a set of Apps for the CFS also developed by NASA. 
	
Versions of the CFS are open-source, and can run on various platforms, including RTEMS virtual machines, embedded ColdFire processors, and, in the future, Raspberry Pi processors. A ground system, such as JCAT, was the missing link to allow a community of CFS developers to form and create Apps for their own and each others' use.
	
##How does JCAT help?
JCAT is the first open-source ground system for CFS App development. It is designed to be a lightweight, rapid-application prototype development program. JCAT accomplishes three tasks to aid in CFS App development: command-sending, telemetry-displaying, and event message-displaying. To test an App, developers simply need to connect JCAT to the CFS containing their App via Ethernet cable, router, or virtual machine, and load its App Profile into JCAT at run-time. JCAT can test any App in the CFS, whether or not it is in the cFE. 

##What is an App Profile?
An App Profile is an XML file that describes all the information JCAT needs to send commands to and display telemetry from an App running on the CFS. You must create or obtain an App Profile in order to use an App in JCAT. Examples of App Profiles are available in <a href="https://github.com/joebenassi/JCAT/tree/master/JCAT/App-Profiles/CFE-6.1/">JCAT/App-Profiles/CFE-6.1/</a>.
To create an App Profile, you must first know its telemetry housekeeping packet and all its commands. Then, open <a href="https://github.com/joebenassi/JCAT/blob/master/JCAT/User-Help/App_Profile_Creation_Guide.md">JCAT/User-Help/App_Profile_Creation_Guide.md</a> for further instructions.
	
##What JRE's does JCAT support?
JCAT can run on Windows, Linux, and Macintosh on 32 and 64-bit JRE's of Java 6 or later, though it has been designed graphically for Windows.

##How do I get started using JCAT?
1. Determine your Java JRE, and select the corresponding JCAT version in <a href="https://github.com/joebenassi/JCAT/blob/master/JCAT/Executables/">JCAT/Executables/</a>. Click 'View Raw', and save the resulting file to your hard drive. 
2. Navigate to <a href="https://github.com/joebenassi/JCAT/blob/master/JCAT/App-Profiles/">JCAT/App-Profiles/</a>, open the folder for your cFE version, and download the files. 
3. Navigate to <a href="https://github.com/joebenassi/JCAT/blob/master/JCAT/Constant-Definition-Files/">JCAT/Constant-Definition-Files/</a>, open the folder for your cFE version, and download the desired file.
4. Double-click the JCAT executable you downloaded in Step 1, and follow the in-program instructions. (for 64-bit Mac users to open JCAT, you must open Terminal, navigate to the location of JCAT, and type "java -XstartOnFirstThread -jar JCAT_osx64.jar").
5. To share your App and App Profile for community use, email joebenassi@gmail.com for instructions.

##How do I get started developing for JCAT?
1. Open and follow the directions within <a href="https://github.com/joebenassi/JCAT/blob/master/JCAT/Developer-Help/Configuring_Eclipse.md">JCAT/Developer-Help/Configuring_Eclipse.md</a>.
2. To push changes to JCAT for community use, email joebenassi@gmail.com for permission to be added as a collaborator.
