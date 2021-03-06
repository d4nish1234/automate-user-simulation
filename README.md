# automate-user-simulation

This program uses java robot package to imitate user actions. Action sequences and delays can be used to complete a whole action or multiple actions

## 1.0 ABOUT:

Use steps to determine what input needs to be determined by the program. You can use these three parameters to determine data load from file:

## 2.0 COMMAND LIST

Actions that is allowed to be entered for simulating user key press and commands allowed to be executed in the program:

| Actions/Command | Description |
| --- | --- |
| keyboard keys | mostly used keys such as a,b,c,A,B,C,1,2,3,?,>,\<,%,$,=,-,+ etc are supported and these can be combined in one step to simulate key presses |
| fn:param-#\| | parameter entered from commandline (see commandline section) |
| fn:windowskey | simulate windows key |
| fn:tab | simulate tab key |
| fn:enter | simulate enter key |
| fn:upkey | simulate up arrow key |
| fn:downkey | simulate down arrow key |
| fn:rightkey | simulate right arrow key |
| fn:leftkey | simulate left arrow key |
| fn:copytotemp-\#\| | automates control+c command and saves it in a key. A key could be any String identifier such as numbers or letters or a combination of both. i.e. fn:copytotemp-2\| would save (automate control+c) values into key 2 |
| fn:pastefromtemp-\# | whatever was copied to fn:copytotemp- command fn:pastefromtemp- will try to paste the saved key using control+V keys. i.e. fn:pastefromtemp-2\| will try to retrieve saved key 2 and paste it using control+V |
| fn:backspace | simulate backspace button |
| fn:paste-\#\| | if reading from file use this to paste the column from the number i.e. fn:paste-32\| will paste from 32nd column|
| fn:paste-\#,\#\| | if reading from file use this to a specific column and row. i.e. fn:paste-22,5\| will paste an item from 22nd column, 5th row |
| fn:compute-\#\| | if reading from file use this to compute from nth row. i.e. fn:compute-21\| will compute item from 21st column - item retrieved from file as a result will perform additional actions recognized by the program. I.e if fn:tab is found then a tab will be simulated. commands can be chained.
| fn:compute-\#,\#\| | if reading from file use this to compute specific column and row. i.e. fn:compute-21,5 will compuate an item from   21st column, 5th row. Item retrieved from file as a result will perform additional actions recognized by the program. I.e if fn:tab is found then a tab will be simulated. commands can be chained.
| fn:delete | simulate delete button |
| fn:ctrl+\<a-z\> | simulate control + \<a-z\> button |
| fn:alt+\<a-z\> | simulate alt + \<a-z\> button|
| fn:spacebar | simulate space bar |
| fn:delay:\#\#\#\#\|	simulate delay function in ms. i.e. fn:delay:6000\| will simulate 6 seconds |
| fn:paste.from.file:C:\\test2.txt\|	|simulate paste from file. It would open up a file, read it and paste the contents of it. |
| fn:exec:\<command\>\| | executes a command to invoke another program. i.e. open a AutoHotKey script using fn:exec:cmd /c c:\autoHotKey\script.ahk param1 param2\| |

## 3.0 GETTING STARTED:

1. Download latest automateUserSimulation-x.x.x.jar: [Download for windows](https://github.com/d4nish1234/automate-user-simulation/blob/master/download/automate-user-simulation-0.3.0-SNAPSHOT.jar).
2. Extract setup.properties and demo_setup.properties from automateUserSimulation-x.x.x.jar and place it in the location of the placed jar
3. Open command prompt (ensure you have java 8 jre) and run java -jar automateUserSimulation.jar -setup demo_setup.properties to see a demo
4. Modify steps in setup.properties to start scripting! 
5. Keep command prompt near you and in order to terminate the program you will need to select the command prompt and press ctrl+c to kill. There is no kill command at the moment 

### 3.1 COMMAND LINE PARAMETERS:

| Parameter | Description |
| --- | --- |
| -p1\| \<command\> -p2\| \<command\> ... | this is to pass in parameters into the program i.e -p1\| fn:tab can be invoked in the program by calling fn:param-1\| |
| -setup \<setup.properties\> | this is to pass in setup.properties file path |
| -rep \<number of repetitions\> | this is to pass in how many repetitions to do (equavalent of user.automate.repetition property) |

#### NOTE: Command line parameters take higher preceidence over properties files variables.

### 3.2 COMPILING FROM SOURCE

1. Clone this project locally
2. Invoke "mvn clean install"
3. Inside target/classes Extract setup.properties and demo_setup.properties from automateUserSimulation-x.x.x.jar and place it in the location of the placed jar
4. Open command prompt (ensure you have java 8 jre) and run java -jar automateUserSimulation.jar -setup demo_setup.properties to see a demo
5. Modify steps in setup.properties to start scripting! 
6. Keep command prompt near you and in order to terminate the program you will need to select the command prompt and press ctrl+c to kill. There is no kill command at the moment 

## 4.0 SETUP.PROPERTIES

Setup.properties is where all the magic happens that makes the automate user simulator work. In setup.properties the following one time configuration properties are supported:

| Property | Mandatory | Description | Example |
| --- | --- |  --- | --- |
| file.read.path | no? | Path to file to be read for clipboard use. The path here could be relative or absolute. Forward slashes needs to be escaped. | C:\\\\temp\\\\test.txt |
| file.read.delim | yes, if file.read.path is used ?(to be confirmed) | delimiter for reading file | \\t |
| file.read.type | yes | The idea is that in the future multiple file types can be supported, currently only "text" is support (for text files) | text |
| user.automate.initialdelay | no? | start with initial delay (in miliseconds) | 5000 | 
| user.automate.delay-between-actions-in-ms | yes? | delay in between functions and input | 0 | 
| user.automate.delay-before-paste-in-ms | no? | add delay before poasting. some machines are slow so this value maybe set to a higher number i.e. 300 (.3s) instead of 50 (.05s) | 50 |
| user.automate.repetition | yes | how many times to repeat all of the steps | 1 |
| user.automate.repetition.useclipboardfilecount | yes | The program is going to repeat n times. where n is number of lines in the clipboard file. (non commented values). <br /> Note: when this field is set to true, the user.automate.repetition field is overridden. | false |

Moreover, in setup.properties the following commands like properties are supported. These properties act like steps that the program will take. These can be added as many times as the user likes

| Property | Mandatory | Description | Example | Example explaination |
| --- | --- |  --- | --- | --- |
| user.automate.step.value | yes (at least once) | add user steps that the program needs to take. **(more details required on the features)**  | fn:windowskeyfn:delay:500|notepadfn:delay:500|fn:enter | press windows key -> delay for .5s -> type in "notepad" -> delay for .5s -> press enter key |
| user.automate.step.delay | no | adds delays command | 5000 | adds delay of .5s |

## 5.0 VERSION HISTORY

| Version | Development Date | Description |
| --- | --- |  --- |
| **0.3.1** | Nov 25 2019	| added a demo setup file |
| **0.3.0** | Sept 14 2018	| add fn:exec:\<command\>\| where command could be any command i.e. cmd /c test.exe or c:\test.vb etc|
| **0.2.9** | July 12 2018	| modify fn:param and fn:paste to now include paste from specific rows |
| **0.2.8** | July 9 2018 	| Allow settings to be entered via command line |
| **0.2.7** | Apr 24 2018 	| better reading of setup properties files |
| **0.2.6** | Nov 24 2017 	| Allow user to use arrow keys using fn:upkey, fn:downkey, fn:rightkey, fn:leftkey.<br /> Allow user to use copy to and paste from temp locations fn:copytotemp-1\| or fn:pastefromtemp-1\| <br /> Allow user to press windows key via fn:windowskey |
| **0.2.5** | Nov 15 2017 |	Better Logging and fix property load issue when starting the program with custom setup.properties |
| **0.2.4** | Nov 04 2017 	|	Allow comments in clipboard file starting with '\#' or with empty line| <br /> Allow equal (=) in user commands.<br /> Allow delay command via fn:delay:23423| (add delay). <br /> Allow paste from text file via command fn:paste.from.file:C:\\test2.txt\| <br />Use user.automate.repetition.useclipboardfilecount in setup.properties. This will allow user to perform same number of repetitions as clipboard file.|
| **0.2.3** | Nov 01 2017 | Allow user to specify command in one line mixed with robot type characters. <br /> Also allowed users to user compute-#! compute # would read column # and allows functions <br /> Also added Java Util Logging. Just logs to console at the moment |
| **0.2.1** | Oct 30 2017 | bugfix for 0.1.6 - due to limitation enter tab buttons dont work with \\t and \\n. for this added fn:enter and fn:tab |
|**0.2.0** | Oct 28 2017 | removed numbering steps - now they are dynamically called. Either delay or value is accepted |
| **0.1.5** | Oct 27 2017 | added ctrl+(anykey) and alt+(anykey) |
|**0.1.4** | Apr 13 2017 | add multi column paste, add before paste in ms property |
|**0.1.3** | Apr 12 2017 | add setup properties parameter|
|**0.1.2** | Apr 11 2017 | add more functions, bug fixes, improving input readability |
|**0.1.1** | Apr 10 2017 | add delay in between key events |
|**0.1.0** | Apr 10 2017 | initial release |
