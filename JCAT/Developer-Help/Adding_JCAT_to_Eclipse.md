#Adding JCAT to Eclipse for JCAT Development
<b>Note: </b>This tutorial will use Eclipse 4.3 to demonstrate..

##Procedure:
1. 	Download Eclipse Standard 4.3.
2. 	Open Eclipse, navigate to Help > Marketplace and download “Egit – Git Team Provider”. Check “Eclipse Git Team Provider” and “Java Implementation of Git”. Click “Confirm”.
3. 	Correctly setup EGit. (See http://www.eclipse.org/resources/resource.php?id=528).
4.	Navigate to Window > Show View > Other > Git > Git Repositories. Copy “https://github.com/joebenassi/JCAT.git", right click in the Git Repositories window, and click "Paste Repository Path or URI". Click "Next".
<p align="center" >
    <img src="https://raw.github.com/joebenassi/JCAT/master/JCAT/Git-Resources/Eclipse_Setup_1.png">
  </p>
5.	Check “master” and “gh-pages”. Click “Next”.
<p align="center" >
    <img src="https://raw.github.com/joebenassi/JCAT/master/JCAT/Git-Resources/Eclipse_Setup_2.png">
  </p>
6.	In “Directory”, type the desired location of your local JCAT Git Repository. Click “Finish”.
<p align="center" >
    <img src="https://raw.github.com/joebenassi/JCAT/master/JCAT/Git-Resources/Eclipse_Setup_3.png">
  </p>
7.	In the Git Repositories window, Open the JCAT tree and right click “Working Directory”. Select “Import Projects”.
8.	Click “Import existing projects”, and click “Next”.
<p align="center" >
    <img src="https://raw.github.com/joebenassi/JCAT/master/JCAT/Git-Resources/Eclipse_Setup_4.png">
  </p>
9.	Check “JCAT”, and “Search for nested projects”, and click “Finish”.
<p align="center" >
    <img src="https://raw.github.com/joebenassi/JCAT/master/JCAT/Git-Resources/Eclipse_Setup_5.png">
  </p>
10.	To run JCAT, in Package Explorer, go to JCAT > src > main, right click “Launcher”, and select Run As > Java Application.
<p align="center" >
    <img src="https://raw.github.com/joebenassi/JCAT/master/JCAT/Git-Resources/Eclipse_Setup_6.png">
  </p>
<br>
<p>If you are unable to open JCAT as described in Step 10 due to an error, try the following solutions:
<ul>
    <li>In the list of SWT libraries (swt_win64, swt_win32, swt_linux64, swt_linux32, swt_osx64, and swt_osx32), ensure that your Eclipse JDK's applicable SWT library is the topmost entry in the list. This is because when Eclipse compiles, it uses the first listed library instead of the one intended for your machine.</li>
    <li>Ensure that you are running a JDK of 1.6 or greater.</li>
    <li>If these solutions do not work, email joebenassi@gmail.com for help.</li>
</ul>
