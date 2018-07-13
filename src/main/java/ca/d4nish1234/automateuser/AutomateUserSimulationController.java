
package ca.d4nish1234.automateuser;

import static java.awt.event.KeyEvent.VK_ALT;
import static java.awt.event.KeyEvent.VK_BACK_SPACE;
import static java.awt.event.KeyEvent.VK_CONTROL;
import static java.awt.event.KeyEvent.VK_DELETE;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_SPACE;
import static java.awt.event.KeyEvent.VK_TAB;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ca.d4nish1234.robothelper.RobotHelper;

enum Action{
	ROBOT_KEY_PRESS("value"), WAIT_DELAY("delay");
	private final String propertyValue;
	Action(String propertyValue){
		this.propertyValue=propertyValue;
	}
	String getPropertyValue(){
		return propertyValue;
	}
}
/**
 * @author Danish Mahboob
 * @since April 10 2017
 */
public class AutomateUserSimulationController {
	public AutomateUserSimulationController() throws AWTException {
		robotHelper= new RobotHelper();
	}
	private RobotHelper robotHelper;

	private final String FN_ENTER = "fn:enter";
	private final String FN_TAB = "fn:tab";
	private final String FN_BACKSPACE = "fn:backspace";
	private final String FN_DELETE = "fn:delete";
	private final String FN_SPACEBAR = "fn:spacebar";
	private final String FN_PASTE = "fn:paste-";
	private final String FN_COMPUTE = "fn:compute-";
	private final String FN_CTRL= "fn:ctrl+";
	private final String FN_ALT= "fn:alt+";
	private final String FN_ALT_TAB= "fn:alt+tab";
	private final String FN_DELAY= "fn:delay:";
	private final String FN_UP_KEY = "fn:upkey";
	private final String FN_DOWN_KEY = "fn:downkey";
	private final String FN_RIGHT_KEY = "fn:rightkey";
	private final String FN_LEFT_KEY = "fn:leftkey";
	private final String FN_COPY_TO_TEMP = "fn:copytotemp-";
	private final String FN_PASTE_FROM_TEMP = "fn:pastefromtemp-";
	private final String FN_WIN_KEY="fn:windowskey";
	private final String FN_PASTE_FROM_TEXT_FILE="fn:paste.from.file:";
	private final String FN_PARAM="fn:param-";
	private int clipboardSteps=0;
	
	private final String FILE_READ_TYPE_TEXT = "text";
	
	private ConfigSettings globalConfigSettings = new ConfigSettings();
	@SuppressWarnings("rawtypes")
	private List fileData= new ArrayList();
	
	private List<StepObject> userStepsSimple = new ArrayList<StepObject>();
	
	
	private void validateAndAddFilePath(String[] args) {
		for (int i=0;i<args.length;i++) {
			if (args[i].equalsIgnoreCase("-usage") || args[i].equalsIgnoreCase("-help")){ 
				System.out.println("usage: -p1 p1value -p2 p2value -rep <number of repetition> -setup \"filepath\"");
				System.exit(1);
			}else if (args[i].equalsIgnoreCase("-setup")) { // this conditional statement is to check for setup properties file value
				
				if (i+1==args.length) { // this is to ensure that the repetition value is passed in
					throw new IllegalArgumentException("-setup value not passed in");
				}
				globalConfigSettings.setSetUpFileReadPath((args[i+1]));
			}
		}
	}
	
	private void validateAndAddCmdArgs(String [] args) throws IllegalArgumentException {
		for (int i=0;i<args.length;i++) {
			if (args[i].startsWith("-p") && // this conditional statement is to check for parameter value 
					args[i].length()>2) { // making sure that the length of the current argument is more than three characters i.e. -p1, -p34 etc
				if (i+1==args.length) { // making sure that next argument is not empty or the parameter value
					throw new IllegalArgumentException(args[i] + "value not passed in");
				}
				globalConfigSettings.addParameters(args[i].substring(2), args[i+1]);
				i++; // moving argument pointer to next pointer
			}else if (args[i].equalsIgnoreCase("-rep")) { // this conditional statement is to check for repetition value
					
				if (i+1==args.length) { // this is to ensure that the repetition value is passed in
					throw new IllegalArgumentException("-rep value not passed in");
				}
				try {
					globalConfigSettings.setUserAutomateRepetition(Integer.parseInt(args[i+1]));
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException("could not parse repetition value for -rep");
				}
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void start(String[] args) throws AWTException, AutomateUserSimulationException {
		
		// get setup file location and usage
		validateAndAddFilePath(args);
		
		// load initial values from properties files if present
		globalConfigSettings.loadValuesFromProperties();
		
		// override all properties from command prompt parameters
		validateAndAddCmdArgs(args);
		
		// at this point all values should be complete in globalConfigSettings
		
		// set up robot helper variables
		robotHelper.setDelayBetweenActions(globalConfigSettings.getUserAutomateDelayBetweenActions());
		
		// print all globalConfigSettings
		Util.logInfo(globalConfigSettings.toString());
		/**
		 * Perform basic checks
		 * 1) if pasting from a file does the value exist or null pointer?
		 * 2) if computing from a file does the value exist?		
		 */
		validatePropertiesFile();

		if (globalConfigSettings.getClipBoardFileReadType() != null && globalConfigSettings.getClipBoardFileReadType().trim().equalsIgnoreCase(FILE_READ_TYPE_TEXT)) {
			
			try {
				//getting file data from clipboard file
				BufferedReader in = new BufferedReader(new FileReader(globalConfigSettings.getClipBoardFileReadPath()));
				String str;
				Util.logInfo("		***FILE DATA***		");
				while ((str = in.readLine()) != null) {
					if (str.trim().length()==0 || str.trim().charAt(0)=='#'){
						continue;
					}
					fileData.add(str.split(globalConfigSettings.getClipBoardFileDelim()));
					clipboardSteps++;
					Util.logInfo("File data: " + str);
				}
				Util.logInfo("");
				Util.logInfo("");
				Util.logInfo("");
				in.close();
			} catch (IOException e) {
				Util.logWarn("Unable to read clipboard File location please check if file exists: " + globalConfigSettings.getClipBoardFileReadPath() + "... still continuing.");
				//System.exit(0);
			}
//			String test [] = (String [])fileData.get(0);
//			Util.log("test: " + test[0]);
			automateUserRounds();
			Util.logInfo("		***ROBOT OPERATIONS COMPLETE***		");
		}
	}


	private void automateUserRounds() throws AutomateUserSimulationException {

		if (globalConfigSettings.isUserAutomateRepetitionUseClipBoardFileCount()){
			globalConfigSettings.setUserAutomateRepetition(clipboardSteps);
		}
		//get all user automate steps from properties files
		computeUserStepsSimple();
		
		int currentRepetition = 0;
		int delay=0;
		// Creates the initial delay
		robotHelper.delay(globalConfigSettings.getUserAutomateInitialDelay());
		Util.logInfo("		***STARTING ROBOT OPERATIONS***		");
		while (currentRepetition<globalConfigSettings.getUserAutomateRepetition()){
			Util.logInfo("		***ITERATION: " + (currentRepetition+1) + " out of " + (globalConfigSettings.getUserAutomateRepetition()) + "***		");	
			for (StepObject stepObjects :userStepsSimple){
				if (stepObjects.getAction().equals(Action.WAIT_DELAY)){
					delay = Integer.parseInt(stepObjects.getStepvalue());
					Util.logInfo("***Sleeping for: " +delay + " ms");
					robotHelper.delay(delay);
					continue;
				}else if (stepObjects.getAction().equals(Action.ROBOT_KEY_PRESS)){
					try {
						Util.logInfo("***Running step: " + stepObjects.getStepvalue());
						performRobotFunctionSimple(stepObjects.getStepvalue(),currentRepetition);
					} catch (AutomateUserSimulationException | IllegalArgumentException e) {
						Util.logSevere("AutomateUserSimulationController.automateUserRounds - following steps could not be performed: " + stepObjects.getStepvalue());
						e.printStackTrace();
						System.exit(0);
					}
				}
			}
			currentRepetition++;
			//clear the temp store location after each iteration
			robotHelper.clearTempStoreLocation();
		}
	}

	public void performRobotFunctionSimple(String inputValue, int step) throws AutomateUserSimulationException {
		
		CharSequence characters= inputValue;
		int length = characters.length();
		for (int i = 0; i < length; i++) {
			char character = characters.charAt(i);
			if (inputValue.length()>i+3 && inputValue.substring(i,i+3).equals("fn:")){
				if (i+FN_UP_KEY.length() <=inputValue.length() &&
						inputValue.substring(i,i+FN_UP_KEY.length()).equalsIgnoreCase(FN_UP_KEY)){
					robotHelper.doType(KeyEvent.VK_UP);
					i+=FN_UP_KEY.length()-1;
					continue;	
				}else if (i+FN_DOWN_KEY.length() <=inputValue.length() &&
						inputValue.substring(i,i+FN_DOWN_KEY.length()).equalsIgnoreCase(FN_DOWN_KEY)){
					robotHelper.doType(KeyEvent.VK_DOWN);
					i+=FN_DOWN_KEY.length()-1;
					continue;	
				}else if (i+FN_LEFT_KEY.length() <=inputValue.length() &&
						inputValue.substring(i,i+FN_LEFT_KEY.length()).equalsIgnoreCase(FN_LEFT_KEY)){
					robotHelper.doType(KeyEvent.VK_LEFT);
					i+=FN_LEFT_KEY.length()-1;
					continue;	
				}else if (i+FN_RIGHT_KEY.length() <=inputValue.length() &&
						inputValue.substring(i,i+FN_RIGHT_KEY.length()).equalsIgnoreCase(FN_RIGHT_KEY)){
					robotHelper.doType(KeyEvent.VK_RIGHT);
					i+=FN_RIGHT_KEY.length()-1;
					continue;	
				}else if (i+FN_ENTER.length() <=inputValue.length() &&
						inputValue.substring(i,i+FN_ENTER.length()).equalsIgnoreCase(FN_ENTER)){
					robotHelper.doType(VK_ENTER);
					i+=FN_ENTER.length()-1;
					continue;
				}else if (i+FN_TAB.length() <=inputValue.length() &&
				inputValue.substring(i,i+FN_TAB.length()).equalsIgnoreCase(FN_TAB)){
					robotHelper.doType(VK_TAB);
					i+=FN_TAB.length()-1;
					continue;
				}else if (i+FN_BACKSPACE.length() <=inputValue.length() &&
					inputValue.substring(i,i+FN_BACKSPACE.length()).equalsIgnoreCase(FN_BACKSPACE)){
					robotHelper.doType(VK_BACK_SPACE);
					i+=FN_BACKSPACE.length()-1;
					continue;
				}else if (i+FN_WIN_KEY.length() <=inputValue.length() &&
						inputValue.substring(i,i+FN_WIN_KEY.length()).equalsIgnoreCase(FN_WIN_KEY)){
						robotHelper.doType(KeyEvent.VK_WINDOWS);
						robotHelper.delay(100);
						i+=FN_WIN_KEY.length()-1;
						continue;
					}
				else if (i+FN_DELETE.length() <=inputValue.length() &&
						inputValue.substring(i,i+FN_DELETE.length()).equalsIgnoreCase(FN_DELETE)){
					robotHelper.doType(VK_DELETE);
					i+=FN_DELETE.length()-1;
					continue;
				}else if (i+FN_SPACEBAR.length() <=inputValue.length() &&
						inputValue.substring(i,i+FN_SPACEBAR.length()).equalsIgnoreCase(FN_SPACEBAR)){
					robotHelper.doType(VK_SPACE);
					i+=FN_SPACEBAR.length()-1;
					continue;
				}else if (i+FN_CTRL.length()+1 <=inputValue.length() &&
						inputValue.substring(i,i+FN_CTRL.length()).equalsIgnoreCase(FN_CTRL)){
					robotHelper.robotMultiKeyPress(VK_CONTROL,robotHelper.getKeyEventfromChar(inputValue.charAt(i+FN_CTRL.length())));
					i+=FN_CTRL.length();
					continue;
				}else if (i+FN_ALT_TAB.length() <=inputValue.length() &&
						inputValue.substring(i,i+FN_ALT_TAB.length()).equalsIgnoreCase(FN_ALT_TAB)){
					robotHelper.robotMultiKeyPress(VK_ALT,VK_TAB);
//					robot.delay(500);
					i+=FN_ALT_TAB.length();
					continue;
				}else if (i+FN_ALT.length()+1 <=inputValue.length() &&
						inputValue.substring(i,i+FN_ALT.length()).equalsIgnoreCase(FN_ALT)){
					robotHelper.robotMultiKeyPress(VK_ALT,robotHelper.getKeyEventfromChar(inputValue.charAt(i+FN_ALT.length())));
					i+=FN_ALT.length();
					continue;
				}
				else if (i+FN_PASTE.length()+2 <=inputValue.length() &&
						inputValue.substring(i,i+FN_PASTE.length()).equalsIgnoreCase(FN_PASTE)){
					i+=FN_PASTE.length();
					String tmpPasteDataRaw= "";
					String[] tmpPasteData;
					try{
						while (!(inputValue.charAt(i)=='|')){
							tmpPasteDataRaw +=inputValue.charAt(i);
							i++;
						}
					}catch (IndexOutOfBoundsException e){
							//do nothing if we cannot identify if fn: was found or out of bounds ex etc.
							Util.logSevere("Paste command not ended well!! ensure to close paste statement with |. Exception: " + e.toString() + "\nInput value: " + inputValue );
							throw new AutomateUserSimulationException("Paste command not ended well!! ensure to close paste statement with |. Exception: " + e.toString() + "\nInput value: " + inputValue );
					}
					int column = 0;
					int row=step;
					// split the data
					tmpPasteData=tmpPasteDataRaw.split(",");
					try{
						column=Integer.parseInt(tmpPasteData[0].trim());
						// if row found then assign row otherwise keep the step value
						if (tmpPasteData.length>1) {
							row=Integer.parseInt(tmpPasteData[1].trim()) -1;
						}
						
						robotHelper.robotPasteValue(getValueFromClipboardFile(row, column),globalConfigSettings.getUserAutomateDelayBeforePaste());
					}catch (ArrayIndexOutOfBoundsException | NumberFormatException e){
						Util.logSevere("column that you are trying to paste " + (column - 1) + "does not exist or is not a number");
						throw new AutomateUserSimulationException("column that you are trying to paste " + (column - 1) + "does not exist or is not a number");
					}
					
					continue;
				}else if (i+FN_DELAY.length()+2 <=inputValue.length() &&
						inputValue.substring(i,i+FN_DELAY.length()).equalsIgnoreCase(FN_DELAY)){
					i+=FN_DELAY.length();
					String tmpDelayColumnNumber= "";
					try{
						while (!(inputValue.charAt(i)=='|')){
							tmpDelayColumnNumber +=inputValue.charAt(i);
							i++;
						}
					}catch (IndexOutOfBoundsException e){
							Util.logSevere("Delay command not ended well!! ensure to close delay statement with |.i.e. fn:delay:6000|. Exception: " + e.toString() + "\nInput value: " + inputValue );
							throw new AutomateUserSimulationException("Delay command not ended well!! ensure to close delay statement with |.i.e. fn:delay:6000|. Exception: " + e.toString() + "\nInput value: " + inputValue );
					}
					int delayInt=0;
					try{
						delayInt = Integer.parseInt(tmpDelayColumnNumber.trim());
						robotHelper.delay(delayInt);
					}catch (NumberFormatException e){
						Util.logSevere("Delay " + delayInt + "is not a number!");
						throw new AutomateUserSimulationException("Delay " + delayInt + "is not a number!");
					}
					
					continue;
				}else if (i+FN_PASTE_FROM_TEXT_FILE.length()+2 <=inputValue.length() &&
						inputValue.substring(i,i+FN_PASTE_FROM_TEXT_FILE.length()).equalsIgnoreCase(FN_PASTE_FROM_TEXT_FILE)){
					i+=FN_PASTE_FROM_TEXT_FILE.length();
					String filePath= "";
					try{
						while (!(inputValue.charAt(i)=='|')){
							filePath +=inputValue.charAt(i);
							i++;
						}
					}catch (IndexOutOfBoundsException e){
							Util.logSevere("Paste From File command not ended well!! ensure to close statement with |. Exception: " + e.toString() + "\nInput value: " + inputValue);
							throw new AutomateUserSimulationException("Paste From File command not ended well!! ensure to close statement with |. Exception: " + e.toString() + "\nInput value: " + inputValue);
					}
					try{
						robotHelper.pasteFromFile(filePath,globalConfigSettings.getUserAutomateDelayBeforePaste());
					}catch (Exception e){
						Util.logSevere("paste from file command failed");
						throw new AutomateUserSimulationException("paste from file command failed");
					}
					
					continue;
				}else if (i+FN_COMPUTE.length()+2 <=inputValue.length() &&
						inputValue.substring(i,i+FN_COMPUTE.length()).equalsIgnoreCase(FN_COMPUTE)){
					i+=FN_COMPUTE.length();
					String tmpComputeDataRaw= "";
					String[]  tmpComputeData;
					try{
						while (!(inputValue.charAt(i)=='|')){
							tmpComputeDataRaw +=inputValue.charAt(i);
							i++;
						}
					}catch (IndexOutOfBoundsException e){
							//do nothing if we cannot identify if fn: was found or out of bounds ex etc.
							Util.logSevere("Compute command not ended well!! ensure to close compute statement with |. Exception: " + e.toString() + "\nInput value: " + inputValue );
							throw new AutomateUserSimulationException("Compute command not ended well!! ensure to close compute statement with |. Exception: " + e.toString() + "\nInput value: " + inputValue );
					}
					int column = 0;
					int row=step;
					
					// split the data:
					tmpComputeData=tmpComputeDataRaw.split(",");
					try{
						column=Integer.parseInt(tmpComputeData[0].trim());
						if (tmpComputeData.length>1) {
							row=Integer.parseInt(tmpComputeData[1].trim()) -1;
						}
						performRobotFunctionSimple(getValueFromClipboardFile(row, column),row);
					}catch (ArrayIndexOutOfBoundsException | NumberFormatException e){
						Util.logSevere("column that you are trying to compute " + (column - 1) + "does not exist or is not a number");
						throw new AutomateUserSimulationException("column that you are trying to compute " + (column - 1) + "does not exist or is not a number");
					}
					continue;
				}else if (i+FN_PARAM.length()+2 <=inputValue.length() &&
						FN_PARAM.substring(i,i+FN_PARAM.length()).equalsIgnoreCase(FN_PARAM)){
					i+=FN_PARAM.length();
					String tmpParamColumnNumber= "";
					try{
						while (!(inputValue.charAt(i)=='|')){
							tmpParamColumnNumber +=inputValue.charAt(i);
							i++;
						}
					}catch (IndexOutOfBoundsException e){
							//do nothing if we cannot identify if fn: was found or out of bounds ex etc.
							Util.logSevere("Param command not ended well!! ensure to close param statement with |. Exception: " + e.toString() + "\nInput value: " + inputValue );
							throw new AutomateUserSimulationException("Param command not ended well!! ensure to close param statement with |. Exception: " + e.toString() + "\nInput value: " + inputValue );
					}
					if (globalConfigSettings.getParameter(tmpParamColumnNumber)==null || "".equals(globalConfigSettings.getParameter(tmpParamColumnNumber))){
						// if parameter is null: 
						Util.logSevere("parameter " + tmpParamColumnNumber + "was not passed in");
						throw new AutomateUserSimulationException("parameter " + tmpParamColumnNumber + "was not passed in");
					}
						performRobotFunctionSimple(globalConfigSettings.getParameter(tmpParamColumnNumber),step);
					continue;
				}else if (i+FN_COPY_TO_TEMP.length()+2 <=inputValue.length() &&
						inputValue.substring(i,i+FN_COPY_TO_TEMP.length()).equalsIgnoreCase(FN_COPY_TO_TEMP)){
					i+=FN_COPY_TO_TEMP.length();
					String tmpStoreLocationKey= "";
					try{
						while (!(inputValue.charAt(i)=='|')){
							tmpStoreLocationKey +=inputValue.charAt(i);
							i++;
						}
					}catch (IndexOutOfBoundsException e){
							//do nothing if we cannot identify if fn: was found or out of bounds ex etc.
							Util.logSevere("Copy to temp command not ended well!! ensure to close copy to temp statement with |. Exception: " + e.toString() + "\nInput value: " + inputValue );
							throw new AutomateUserSimulationException("Copy to temp command not ended well!! ensure to close copy to temp statement with |. Exception: " + e.toString() + "\nInput value: " + inputValue );
					}
					try{
						robotHelper.copyToTempLocation(tmpStoreLocationKey.trim());
					}catch (NullPointerException | HeadlessException |UnsupportedFlavorException | IOException e){
						Util.logSevere("column that you are trying to copy to temp " + (tmpStoreLocationKey.trim()) + "does not exist or there was a problem copying to clipboard!!");
						throw new AutomateUserSimulationException("column that you are trying to copy to temp " + (tmpStoreLocationKey.trim()) + "does not exist or there was a problem copying to clipboard!!");
					}
					continue;
				}else if (i+FN_PASTE_FROM_TEMP.length()+2 <=inputValue.length() &&
						inputValue.substring(i,i+FN_PASTE_FROM_TEMP.length()).equalsIgnoreCase(FN_PASTE_FROM_TEMP)){
					i+=FN_PASTE_FROM_TEMP.length();
					String tmpRetrieveLocationValue= "";
					try{
						while (!(inputValue.charAt(i)=='|')){
							tmpRetrieveLocationValue +=inputValue.charAt(i);
							i++;
						}
					}catch (IndexOutOfBoundsException e){
							//do nothing if we cannot identify if fn: was found or out of bounds ex etc.
							Util.logSevere("Paste from temp command not ended well!! ensure to close paste from temp statement with |. Exception: " + e.toString() + "\nInput value: " + inputValue );
							throw new AutomateUserSimulationException("Paste from temp command not ended well!! ensure to close paste from temp statement with |. Exception: " + e.toString() + "\nInput value: " + inputValue );
					}
					try{
						robotHelper.pasteFromTempLocation(tmpRetrieveLocationValue.trim(),globalConfigSettings.getUserAutomateDelayBeforePaste());
					}catch (NullPointerException | HeadlessException e){
						Util.logSevere("column that you are trying to paste from temp " + (tmpRetrieveLocationValue.trim()) + "does not exist or there was a problem pasting from clipboard!!");
						throw new AutomateUserSimulationException("column that you are trying to paste from temp " + (tmpRetrieveLocationValue.trim()) + "does not exist or there was a problem pasting from clipboard!!");
					}
					continue;
				}
			}
			robotHelper.type(character);
		}
	}

	private String getValueFromClipboardFile(int step, int column) {
		return ((String [])fileData.get(step))[column-1];
	}

	private void computeUserStepsSimple() throws AutomateUserSimulationException {
			Scanner scan= null;
		try{
			//actual
			scan = new Scanner(new File(globalConfigSettings.getSetUpFileReadPath()));
//			testing
//			scan = new Scanner(new File("C:\\Users\\Danish\\Dropbox\\Danish\\JavaWorkspace\\automate-user-simulation\\src\\main\\resources\\setup.properties"));
			
			Util.logInfo("		***SETUP DATA***		");
			while(scan.hasNextLine()) {
				String split[] = scan.nextLine().trim().split("=",2);
				String prop ="",value="";
				prop=split[0];
				if (split.length==2){
					value=split[1];
				}
				// now you have name and value
				if (("user.automate.step." + Action.ROBOT_KEY_PRESS.getPropertyValue()).equals(prop)) {
					userStepsSimple.add(new StepObject(value, Action.ROBOT_KEY_PRESS));
					Util.logInfo("value added - " + prop + " = " + value);
				}else if (("user.automate.step." + Action.WAIT_DELAY.getPropertyValue()).equals(prop)) {
					userStepsSimple.add((new StepObject(value, Action.WAIT_DELAY)));
					Util.logInfo("Delay added - " + prop + " = " + value);
				}
			}
			Util.logInfo("");
			Util.logInfo("");
			Util.logInfo("");
		}catch (FileNotFoundException e){
			throw new AutomateUserSimulationException("Could not read properties file: " + globalConfigSettings.getClipBoardFileReadPath());
		}finally{
			scan.close();
		}
	}
	private void validatePropertiesFile() {
		// TODO: sanity checks here
	}
}
