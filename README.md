  <p align="center" >
	  <img src="https://raw.github.com/joebenassi/JCAT/master/JCAT/res/Images/JCATLogoHuge.png">
	</p>
#Java Command And Telemetry (JCAT)
JCAT is a Java program created at the NASA Goddard Space Flight Center to aid in CFS App development. The CFS (Core Flight System) is a flight software product line that is being used by multiple NASA centers. The CFS provides mission-independent, reusable, core flight software services and applications.
	
Being so reusable, the CFS can run on various platforms, and hopes to create a community of developers to create Apps to incorperate into the CFS for mutual benefit. As a result, NASA has released versions of the CFS that are open-source, and has released JCAT as a tool for independant App development.
	
##How does JCAT help?
JCAT is the first open-source ground system for CFS App development. It is designed to be a lightwieght, rapid-application prototype development program. JCAT accompishes three tasks to aid in CFS App development: command-sending, telemetry-displaying, and event message-displaying. To test an App, developers simply need to load its 'App Profile' into JCAT at runtime. 
	
##What is an App Profile?
An App Profile is an XML file that describes all the information about an App that JCAT needs to send commands to it and display telemetry from it. You must create or obtain a correctly-defined App Profile in order to use an App. Examples of App Profiles are in the GitHub repository folder "Runtime Files", and apply to the most recent, developing version of the CFS.
To create an App Profile, you must first know exactly how each command for the App is defined and exactly how its telemetry housekeeping packet is defined. Then, you should navigate to the GitHub repository file "User Help/CreatingAppProfiles" for an in-depth explanation.
	
##What platforms/JRE's does JCAT support?
JCAT can ran on Macintosh, Windows, and Linux on 32 or 64-bit JRE's of Java 6 or later, though it has been designed for Windows. JCAT can test Apps in a CFS running on a virtual machine, over LAN, or over a router.
	

