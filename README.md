JCAT (Java Command and Telemetry)
====
	The CFS (Core Flight System) is a flight software product line that is being used by multiple NASA centers. The CFS provides mission-independent, reusable, core flight software services and applications.
	Being so reusable, the CFS can run on various platforms, and hopes to create a community of developers to create Apps to incorperate into the CFS for mutual benefit. As a result, NASA has released versions of the CFS that are open-source, and has released JCAT as a tool for independant App development.

	JCAT is a project started at the NASA Goddard Space Flight Center. It is a lightwieght, open-source, rapid-application prototype development ground system. It allows App developers to create App Profiles that describe attributes of their CFS App, load them in JCAT at runtime, and test their App.
	JCAT accompishes three tasks to aid in development: command-sending, telemetry-displaying, and event message-displaying. For each App intended to be commanded by JCAT, you must create or obtain an App Profile that matches the exact version of the App. On GitHub, there are App Profiles in the folder "Runtime Files" that match a developing version of the CFS. A document on GitHub describes the procedure to create an App Profile.
	JCAT can be used on Macintosh, Windows, and Linux on 32 or 64-bit JRE's of Java 6 or later. JCAT is usable to test Apps in a CFS running on a virtual machine, over LAN, or over a router.
