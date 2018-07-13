/**
 * @auther Danish Mahboob
 * Date: April 10 2017
 */
package ca.d4nish1234.automateuser;

import java.awt.AWTException;

public class AutomateUserSimulationMain {

	public static void main(String[] args) throws AutomateUserSimulationException, AWTException {
			new AutomateUserSimulationController().start(args);
	}
}
