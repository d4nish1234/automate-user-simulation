package ca.d4nish1234.automateuser;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Danish Mahboob
 * @since July 13 2018
 */
class ConfigSettings {
	private String setUpFileReadPath;
	private String clipBoardFileReadPath;
	private String clipBoardFileReadType;
	private String clipBoardFileDelim;
	private int userAutomateRepetition =1;
	private boolean userAutomateRepetitionUseClipBoardFileCount =false;
	private int userAutomateInitialDelay =10000;
	private int userAutomateDelayBetweenActions =0;
	private int userAutomateDelayBeforePaste = 50;
	private Parameters params = new Parameters();
	
	private final String DEFAULT_SETUP_PROPERTIES = "setup.properties";
	private final String P_FILE_READ_TYPE = "file.read.type";
	private final String P_FILE_READ_DELIM = "file.read.delim";
	private final String P_FILE_READ_PATH = "file.read.path";
	private final String P_USER_AUTOMATE_REPETITION = "user.automate.repetition";
	private final String P_USER_AUTOMATE_REPETITION_USECLIPBOARDFILECOUNT = "user.automate.repetition.useclipboardfilecount";
	
	private final String P_USER_AUTOMATE_INITIAL_DELAY = "user.automate.initialdelay";
	private final String P_USER_AUTOMATE_DELAY_BETWEEN_ACTIONS ="user.automate.delay-between-actions-in-ms";
	private final String P_USER_AUTOMATE_DELAY_BEFORE_PASTE = "user.automate.delay-before-paste-in-ms";
	
	private Properties prop = null;

	void loadValuesFromProperties(){
		if (this.getSetUpFileReadPath()==null || "".equals(this.getSetUpFileReadPath())){
			// use default path set up in the file if not sent as a command prompt argument
			this.setSetUpFileReadPath(DEFAULT_SETUP_PROPERTIES);
		}
		
		try {
			prop = Util.loadFromFile(this.getSetUpFileReadPath());
		} catch (IOException e) {
			Util.logSevere("properties files " + this.getSetUpFileReadPath() + " could not be loaded! Exiting...");
			e.printStackTrace();
			System.exit(0);
		}
		
		// setUpFileReadPath already set in another method earlier
		this.setClipBoardFileReadType(prop.getProperty(P_FILE_READ_TYPE)); // File read type
		this.setClipBoardFileDelim(prop.getProperty(P_FILE_READ_DELIM)); // File read delimiter
		this.setClipBoardFileReadPath(prop.getProperty(P_FILE_READ_PATH)); // Clipboard file read path

		// User Automate - Delay before paste
		try {
			this.setUserAutomateDelayBeforePaste(Integer.parseInt(prop.getProperty(P_USER_AUTOMATE_DELAY_BEFORE_PASTE)));
		} catch (NumberFormatException e){
			// could not parse this property from property file
			Util.logWarn("could not parse property" + P_USER_AUTOMATE_DELAY_BEFORE_PASTE);
		}
		
		// User Automate - Delay between actions
		try {
			this.setUserAutomateDelayBetweenActions(Integer.parseInt(prop.getProperty(P_USER_AUTOMATE_DELAY_BETWEEN_ACTIONS)));
		} catch (NumberFormatException e){
			// could not parse this property from property file
			Util.logWarn("could not parse property" + P_USER_AUTOMATE_DELAY_BETWEEN_ACTIONS);
		}
		
		// User Automate - Initial Delay
		try {
			this.setUserAutomateInitialDelay(Integer.parseInt(prop.getProperty(P_USER_AUTOMATE_INITIAL_DELAY)));
		} catch (NumberFormatException e){
			// could not parse this property from property file
			Util.logWarn("could not parse property" + P_USER_AUTOMATE_INITIAL_DELAY);
		}
		
		// User Automate - Repetition
		try {
			this.setUserAutomateRepetition(Integer.parseInt(prop.getProperty(P_USER_AUTOMATE_REPETITION)));
		} catch (NumberFormatException e){
			// could not parse this property from property file
			Util.logWarn("could not parse property" + P_USER_AUTOMATE_REPETITION);
		}
		
		// User Automate - Repetition user clipboard file count
		try {
			this.setUserAutomateRepetitionUseClipBoardFileCount(Boolean.parseBoolean(prop.getProperty(P_USER_AUTOMATE_REPETITION_USECLIPBOARDFILECOUNT)));
		} catch (NumberFormatException e){
			// could not parse this property from property file
			Util.logWarn("could not parse property" + P_USER_AUTOMATE_REPETITION_USECLIPBOARDFILECOUNT);
		}
		
	}
	public String getClipBoardFileReadPath() {
		return clipBoardFileReadPath;
	}
	public void setClipBoardFileReadPath(String clipBoardFileReadPath) {
		this.clipBoardFileReadPath = clipBoardFileReadPath;
	}
	public Parameters getParameters() {
		// delegate method
		return params;
	}
	public String getParameter(String param) {
		// delegate method
		return params.getParameter(param);
	}
	public void addParameters(String param, String value) {
		// delegate method
		params.addParameters(param, value);
	}
	public String getSetUpFileReadPath() {
		return setUpFileReadPath;
	}
	public void setSetUpFileReadPath(String setUpFileReadPath) {
		this.setUpFileReadPath = setUpFileReadPath;
	}
	public int getUserAutomateRepetition() {
		return userAutomateRepetition;
	}
	public void setUserAutomateRepetition(int userAutomateRepetition) {
		this.userAutomateRepetition = userAutomateRepetition;
	}
	public boolean isUserAutomateRepetitionUseClipBoardFileCount() {
		return userAutomateRepetitionUseClipBoardFileCount;
	}
	public void setUserAutomateRepetitionUseClipBoardFileCount(boolean userAutomateRepetitionUseClipBoardFileCount) {
		this.userAutomateRepetitionUseClipBoardFileCount = userAutomateRepetitionUseClipBoardFileCount;
	}
	public int getUserAutomateInitialDelay() {
		return userAutomateInitialDelay;
	}
	public void setUserAutomateInitialDelay(int userAutomateInitialDelay) {
		this.userAutomateInitialDelay = userAutomateInitialDelay;
	}
	public int getUserAutomateDelayBetweenActions() {
		return userAutomateDelayBetweenActions;
	}
	public void setUserAutomateDelayBetweenActions(int userAutomateDelayBetweenActions) {
		this.userAutomateDelayBetweenActions = userAutomateDelayBetweenActions;
	}
	public int getUserAutomateDelayBeforePaste() {
		return userAutomateDelayBeforePaste;
	}
	public void setUserAutomateDelayBeforePaste(int userAutomateDelayBeforePaste) {
		this.userAutomateDelayBeforePaste = userAutomateDelayBeforePaste;
	}
	public String getClipBoardFileReadType() {
		return clipBoardFileReadType;
	}
	public void setClipBoardFileReadType(String clipBoardFileReadType) {
		this.clipBoardFileReadType = clipBoardFileReadType;
	}
	public String getClipBoardFileDelim() {
		return clipBoardFileDelim;
	}
	public void setClipBoardFileDelim(String clipBoardFileDelim) {
		this.clipBoardFileDelim = clipBoardFileDelim;
	}
	@Override
	public String toString() {
		return "ConfigSettings [setUpFileReadPath=" + setUpFileReadPath + ", clipBoardFileReadPath="
				+ clipBoardFileReadPath + ", clipBoardFileReadType=" + clipBoardFileReadType + ", clipBoardFileDelim="
				+ clipBoardFileDelim + ", userAutomateRepetition=" + userAutomateRepetition
				+ ", userAutomateRepetitionUseClipBoardFileCount=" + userAutomateRepetitionUseClipBoardFileCount
				+ ", userAutomateInitialDelay=" + userAutomateInitialDelay + ", userAutomateDelayBetweenActions="
				+ userAutomateDelayBetweenActions + ", userAutomateDelayBeforePaste=" + userAutomateDelayBeforePaste
				+ ", params=" + params + "]";
	}
}
