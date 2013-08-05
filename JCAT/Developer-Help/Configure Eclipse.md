#Configuring Eclipse for JCAT Development
<b>Note: </b>This tutorial will use Eclipse 4.3 to demonstrate the setup.

##Procedure:
1. 	Download Eclipse Standard 4.3.
2. 	Open Eclipse, navigate to Help > Marketplace and download “Egit – Git Team Provider”. Check “Eclipse Git Team Provider” and “Java Implementation of Git”. Click “Confirm”.
3. 	Correctly setup EGit. Use (http://www.eclipse.org/resources/resource.php?id=528).
4.	Navigate to Window > Show View > Other > Git > Git Repositories. Copy “https://github.com/joebenassi/JCAT.git", right click in the Git Repositories window, and click "Paste Repository Path or URI".
5.	Check “master” and “gh-pages”. Click “Next”.
6.	In “Directory”, type the desired location of your local JCAT Git Repository. Click “Finish”.
7.	In the Git Repositories window, Open the JCAT tree and right click “Working Directory”. Select “Import Projects”.
8.	Click “Import existing projects”, and click “Next”.
9.	Check “JCAT”, and “Search for nested projects”, and click “Finish”.
10.	To run JCAT, in Package Explorer, go to JCAT > src > main > Launcher, right click “Launcher”, select Run As > Java Application.