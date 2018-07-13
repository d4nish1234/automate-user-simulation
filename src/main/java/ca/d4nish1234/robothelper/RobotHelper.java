package ca.d4nish1234.robothelper;

import static java.awt.event.KeyEvent.VK_0;
import static java.awt.event.KeyEvent.VK_1;
import static java.awt.event.KeyEvent.VK_2;
import static java.awt.event.KeyEvent.VK_3;
import static java.awt.event.KeyEvent.VK_4;
import static java.awt.event.KeyEvent.VK_5;
import static java.awt.event.KeyEvent.VK_6;
import static java.awt.event.KeyEvent.VK_7;
import static java.awt.event.KeyEvent.VK_8;
import static java.awt.event.KeyEvent.VK_9;
import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_B;
import static java.awt.event.KeyEvent.VK_BACK_QUOTE;
import static java.awt.event.KeyEvent.VK_BACK_SLASH;
import static java.awt.event.KeyEvent.VK_C;
import static java.awt.event.KeyEvent.VK_CLOSE_BRACKET;
import static java.awt.event.KeyEvent.VK_COMMA;
import static java.awt.event.KeyEvent.VK_CONTROL;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_E;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_EQUALS;
import static java.awt.event.KeyEvent.VK_F;
import static java.awt.event.KeyEvent.VK_G;
import static java.awt.event.KeyEvent.VK_H;
import static java.awt.event.KeyEvent.VK_I;
import static java.awt.event.KeyEvent.VK_J;
import static java.awt.event.KeyEvent.VK_K;
import static java.awt.event.KeyEvent.VK_L;
import static java.awt.event.KeyEvent.VK_M;
import static java.awt.event.KeyEvent.VK_MINUS;
import static java.awt.event.KeyEvent.VK_N;
import static java.awt.event.KeyEvent.VK_O;
import static java.awt.event.KeyEvent.VK_OPEN_BRACKET;
import static java.awt.event.KeyEvent.VK_P;
import static java.awt.event.KeyEvent.VK_PERIOD;
import static java.awt.event.KeyEvent.VK_Q;
import static java.awt.event.KeyEvent.VK_QUOTE;
import static java.awt.event.KeyEvent.VK_R;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_SEMICOLON;
import static java.awt.event.KeyEvent.VK_SHIFT;
import static java.awt.event.KeyEvent.VK_SLASH;
import static java.awt.event.KeyEvent.VK_SPACE;
import static java.awt.event.KeyEvent.VK_T;
import static java.awt.event.KeyEvent.VK_TAB;
import static java.awt.event.KeyEvent.VK_U;
import static java.awt.event.KeyEvent.VK_V;
import static java.awt.event.KeyEvent.VK_W;
import static java.awt.event.KeyEvent.VK_X;
import static java.awt.event.KeyEvent.VK_Y;
import static java.awt.event.KeyEvent.VK_Z;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import ca.d4nish1234.automateuser.AutomateUserSimulationException;
import ca.d4nish1234.automateuser.Util;

/**
 * @author Danish Mahboob
 * @since July 13 2018
 */
public class RobotHelper {

	private Map<String, String> tempStoreLocation = new HashMap<String,String>();
	private int delayBetweenActions;
	//Create instance of Clipboard class
	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	private StringSelection stringSelection = null;
		
	private Robot robot;
	public RobotHelper() throws AWTException {
		this.robot = new Robot();
	}
	public void delay (int delay) {
		robot.delay(delay);
	}
	public void robotMultiKeyPress(int ... keyEvents) {
		for (int keyEvent: keyEvents){
			robot.keyPress(keyEvent);
		}
		
		for (int i=keyEvents.length-1;i>=0;i--){
			robot.keyRelease(keyEvents[i]);
		}
	}
	public void robotPasteValue(String value, int delayBeforePaste) {
		//without delay the copy does not happen properly
		
		if (delayBeforePaste >=50){
			robot.delay(delayBeforePaste);
		}else{
			Util.logWarn("Invalid before paste value: " + delayBeforePaste + ". Value was defaulted to 50 ms");
			robot.delay(50);
		}
		stringSelection = new StringSelection(value);
		//Copy the String to Clipboard

		clipboard.setContents(stringSelection, null);
		//Use Robot class instance to simulate CTRL+C and CTRL+V key events :
		robot.delay(100);
		robotMultiKeyPress(VK_CONTROL,VK_V);
//		robot.keyPress(KeyEvent.VK_CONTROL);
//		robot.keyPress(KeyEvent.VK_V);
//		robot.keyRelease(KeyEvent.VK_V);
//		robot.keyRelease(KeyEvent.VK_CONTROL);
	}
	public void pasteFromTempLocation(String key, int delayBeforePaste) {
		String keyValue = tempStoreLocation.get(key);
		Util.logInfo("about to paste: " + keyValue);;
		robotPasteValue(keyValue, delayBeforePaste);
	}
	public void copyToTempLocation(String key) throws HeadlessException, UnsupportedFlavorException, IOException {
			
			// copy to clipboard
			robotMultiKeyPress(VK_CONTROL,VK_C);
	//		robot.keyPress(KeyEvent.VK_CONTROL);
	//		robot.keyPress(KeyEvent.VK_C);
	//		robot.keyRelease(KeyEvent.VK_C);
	//		robot.keyRelease(KeyEvent.VK_CONTROL);
			
			// add delay so that programs like excel file wont crash right after copying something
			robot.delay(200);
			String tmpClipboard = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		
			tempStoreLocation.put(key, tmpClipboard);
	}
	private void doType(int[] keyCodes, int offset, int length) {
		if (length == 0) {
			return;
		}

		robot.keyPress(keyCodes[offset]);
		doType(keyCodes, offset + 1, length - 1);
		robot.keyRelease(keyCodes[offset]);

			robot.delay(getDelayBetweenActions());
	}
	public void doType(int... keyCodes) {
		doType(keyCodes, 0, keyCodes.length);
	}
	public int getKeyEventfromChar(char character){
		switch (character) {
		case 'a': return VK_A;
		case 'b': return VK_B;
		case 'c': return VK_C;
		case 'd': return VK_D;
		case 'e': return VK_E;
		case 'f': return VK_F;
		case 'g': return VK_G;
		case 'h': return VK_H;
		case 'i': return VK_I;
		case 'j': return VK_J;
		case 'k': return VK_K;
		case 'l': return VK_L;
		case 'm': return VK_M;
		case 'n': return VK_N;
		case 'o': return VK_O;
		case 'p': return VK_P;
		case 'q': return VK_Q;
		case 'r': return VK_R;
		case 's': return VK_S;
		case 't': return VK_T;
		case 'u': return VK_U;
		case 'v': return VK_V;
		case 'w': return VK_W;
		case 'x': return VK_X;
		case 'y': return VK_Y;
		case 'z': return VK_Z;
		case 'A': return VK_A;
		case 'B': return VK_B;
		case 'C': return VK_C;
		case 'D': return VK_D;
		case 'E': return VK_E;
		case 'F': return VK_F;
		case 'G': return VK_G;
		case 'H': return VK_H;
		case 'I': return VK_I;
		case 'J': return VK_J;
		case 'K': return VK_K;
		case 'L': return VK_L;
		case 'M': return VK_M;
		case 'N': return VK_N;
		case 'O': return VK_O;
		case 'P': return VK_P;
		case 'Q': return VK_Q;
		case 'R': return VK_R;
		case 'S': return VK_S;
		case 'T': return VK_T;
		case 'U': return VK_U;
		case 'V': return VK_V;
		case 'W': return VK_W;
		case 'X': return VK_X;
		case 'Y': return VK_Y;
		case 'Z': return VK_Z;
		case '`': return VK_BACK_QUOTE;
		case '0': return VK_0;
		case '1': return VK_1;
		case '2': return VK_2;
		case '3': return VK_3;
		case '4': return VK_4;
		case '5': return VK_5;
		case '6': return VK_6;
		case '7': return VK_7;
		case '8': return VK_8;
		case '9': return VK_9;
		case '-': return VK_MINUS;
		case '=': return VK_EQUALS;
//		case '!': return VK_EXCLAMATION_MARK;
//		case '@': return VK_AT;
//		case '#': return VK_NUMBER_SIGN;
//		case '$': return VK_DOLLAR;
//		case '^': return VK_CIRCUMFLEX;
//		case '&': return VK_AMPERSAND;
//		case '*': return VK_ASTERISK;
//		case '(': return VK_LEFT_PARENTHESIS;
//		case ')': return VK_RIGHT_PARENTHESIS;
//		case '_': return VK_UNDERSCORE;
//		case '+': return VK_PLUS;
		case '\t': return VK_TAB;
		case '\n': return VK_ENTER;
		case '[': return VK_OPEN_BRACKET;
		case ']': return VK_CLOSE_BRACKET;
		case '\\': return VK_BACK_SLASH;
		case ';': return VK_SEMICOLON;
//		case ':': return VK_COLON;
		case '\'': return VK_QUOTE;
//		case '"': return VK_QUOTEDBL;
		case ',': return VK_COMMA;
		case '.': return VK_PERIOD;
		case '/': return VK_SLASH;
		case ' ': return VK_SPACE;
		default:
			throw new IllegalArgumentException("Cannot type character " + character);
		}

	}
	
	public void type(char character) {
		switch (character) {
		case 'a': doType(getKeyEventfromChar('a')); break;
		case 'b': doType(getKeyEventfromChar('b')); break;
		case 'c': doType(getKeyEventfromChar('c')); break;
		case 'd': doType(getKeyEventfromChar('d')); break;
		case 'e': doType(getKeyEventfromChar('e')); break;
		case 'f': doType(getKeyEventfromChar('f')); break;
		case 'g': doType(getKeyEventfromChar('g')); break;
		case 'h': doType(getKeyEventfromChar('h')); break;
		case 'i': doType(getKeyEventfromChar('i')); break;
		case 'j': doType(getKeyEventfromChar('j')); break;
		case 'k': doType(getKeyEventfromChar('k')); break;
		case 'l': doType(getKeyEventfromChar('l')); break;
		case 'm': doType(VK_M); break;
		case 'n': doType(VK_N); break;
		case 'o': doType(VK_O); break;
		case 'p': doType(VK_P); break;
		case 'q': doType(VK_Q); break;
		case 'r': doType(VK_R); break;
		case 's': doType(VK_S); break;
		case 't': doType(VK_T); break;
		case 'u': doType(VK_U); break;
		case 'v': doType(VK_V); break;
		case 'w': doType(VK_W); break;
		case 'x': doType(VK_X); break;
		case 'y': doType(VK_Y); break;
		case 'z': doType(VK_Z); break;
		case 'A': doType(VK_SHIFT, VK_A); break;
		case 'B': doType(VK_SHIFT, VK_B); break;
		case 'C': doType(VK_SHIFT, VK_C); break;
		case 'D': doType(VK_SHIFT, VK_D); break;
		case 'E': doType(VK_SHIFT, VK_E); break;
		case 'F': doType(VK_SHIFT, VK_F); break;
		case 'G': doType(VK_SHIFT, VK_G); break;
		case 'H': doType(VK_SHIFT, VK_H); break;
		case 'I': doType(VK_SHIFT, VK_I); break;
		case 'J': doType(VK_SHIFT, VK_J); break;
		case 'K': doType(VK_SHIFT, VK_K); break;
		case 'L': doType(VK_SHIFT, VK_L); break;
		case 'M': doType(VK_SHIFT, VK_M); break;
		case 'N': doType(VK_SHIFT, VK_N); break;
		case 'O': doType(VK_SHIFT, VK_O); break;
		case 'P': doType(VK_SHIFT, VK_P); break;
		case 'Q': doType(VK_SHIFT, VK_Q); break;
		case 'R': doType(VK_SHIFT, VK_R); break;
		case 'S': doType(VK_SHIFT, VK_S); break;
		case 'T': doType(VK_SHIFT, VK_T); break;
		case 'U': doType(VK_SHIFT, VK_U); break;
		case 'V': doType(VK_SHIFT, VK_V); break;
		case 'W': doType(VK_SHIFT, VK_W); break;
		case 'X': doType(VK_SHIFT, VK_X); break;
		case 'Y': doType(VK_SHIFT, VK_Y); break;
		case 'Z': doType(VK_SHIFT, VK_Z); break;
		case '`': doType(VK_BACK_QUOTE); break;
		case '0': doType(VK_0); break;
		case '1': doType(VK_1); break;
		case '2': doType(VK_2); break;
		case '3': doType(VK_3); break;
		case '4': doType(VK_4); break;
		case '5': doType(VK_5); break;
		case '6': doType(VK_6); break;
		case '7': doType(VK_7); break;
		case '8': doType(VK_8); break;
		case '9': doType(VK_9); break;
		case '-': doType(VK_MINUS); break;
		case '=': doType(VK_EQUALS); break;
		case '~': doType(VK_SHIFT, VK_BACK_QUOTE); break;
		case '!': doType(VK_SHIFT,VK_1); break;
		case '@': doType(VK_SHIFT,VK_2); break;
		case '#': doType(VK_SHIFT,VK_3); break;
		case '$': doType(VK_SHIFT,VK_4); break;
		case '%': doType(VK_SHIFT, VK_5); break;
		case '^': doType(VK_SHIFT,VK_6); break;
		case '&': doType(VK_SHIFT,VK_7); break;
		case '*': doType(VK_SHIFT,VK_8); break;
		case '(': doType(VK_SHIFT,VK_9); break;
		case ')': doType(VK_SHIFT,VK_0); break;
		case '_': doType(VK_SHIFT,VK_MINUS); break;
		case '+': doType(VK_SHIFT,VK_EQUALS); break;
		case '\t': doType(VK_TAB); break;
		case '\n': doType(VK_ENTER); break;
		case '[': doType(VK_OPEN_BRACKET); break;
		case ']': doType(VK_CLOSE_BRACKET); break;
		case '\\': doType(VK_BACK_SLASH); break;
		case '{': doType(VK_SHIFT, VK_OPEN_BRACKET); break;
		case '}': doType(VK_SHIFT, VK_CLOSE_BRACKET); break;
		case '|': doType(VK_SHIFT, VK_BACK_SLASH); break;
		case ';': doType(VK_SEMICOLON); break;
		case ':': doType(VK_SHIFT,VK_SEMICOLON); break;
		case '\'': doType(VK_QUOTE); break;
		case '"': doType(VK_SHIFT,VK_QUOTE); break;
		case ',': doType(VK_COMMA); break;
		case '<': doType(VK_SHIFT, VK_COMMA); break;
		case '.': doType(VK_PERIOD); break;
		case '>': doType(VK_SHIFT, VK_PERIOD); break;
		case '/': doType(VK_SLASH); break;
		case '?': doType(VK_SHIFT, VK_SLASH); break;
		case ' ': doType(VK_SPACE); break;
		default:
			throw new IllegalArgumentException("Cannot type character " + character);
		}
	}
	public void pasteFromFile(String filePath, int delayBeforePaste) throws FileNotFoundException, AutomateUserSimulationException{
		Scanner scan=null;
		try{
			scan = new Scanner(new File(filePath));
			
			while(scan.hasNextLine()) {
				String nextLine= scan.nextLine();
				// now you have name and value
				robotPasteValue(nextLine,delayBeforePaste);
				robot.delay(150);
				doType(VK_ENTER);
			}
		}finally{
			scan.close();
		}
	}
	public void clearTempStoreLocation() {
		tempStoreLocation.clear();
	}
	public int getDelayBetweenActions() {
		return delayBetweenActions;
	}
	public void setDelayBetweenActions(int delayBetweenActions) {
		this.delayBetweenActions = delayBetweenActions;
	}
}
