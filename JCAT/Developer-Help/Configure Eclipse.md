 <p align="center" >
    <img src="https://raw.github.com/joebenassi/JCAT/master/JCAT/res/Images/JCATLogoHuge.png">
	</p>
#Java Command And Telemetry (JCAT)
JCAT is a Java program created at the NASA Goddard Space Flight Center to aid in CFS App development. The CFS (Core Flight System) is a flight software product line that is being used by multiple NASA centers. The CFS provides mission-independent, reusable, core flight software services and applications. The cFE (Core Flight Executive) is a set of applications for the CFS also developed by NASA. 

##Procedure:
1. Download Eclipse Standard 4.3.
2.	Open Eclipse, navigate to Help > Marketplace and download “Egit – Git Team Provider”. Check “Eclipse Git Team Provider” and “Java Implementation of Git”. Click “Confirm”.
3.	Correctly setup EGit. Use (http://www.eclipse.org/resources/resource.php?id=528).
4.	Navigate to Window > Show View > Other > Git > Git Repositories. Copy “https://github.com/joebenassi/JCAT.git", right click in the Git Repositories window, and click "Paste Repository Path or URI".
5.	Check “master” and “gh-pages”. Click “Next”.
6.	In “Directory”, type the desired location of your local JCAT Git Repository. Click “Finish”.
7.	In the Git Repositories window, Open the JCAT tree and right click “Working Directory”. Select “Import Projects”.
8.	Click “Import existing projects”, and click “Next”.
9.	Check “JCAT”, and “Search for nested projects”, and click “Finish”.
10.	 To run JCAT, in Package Explorer, go to JCAT > src > main > Launcher, right click “Launcher”, select Run As > Java Application.
