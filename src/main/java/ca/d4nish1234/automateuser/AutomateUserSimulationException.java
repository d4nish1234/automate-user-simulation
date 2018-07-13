/**
 * @auther Danish Mahboob
 * Date: April 10 2017
 */
package ca.d4nish1234.automateuser;

public class AutomateUserSimulationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public AutomateUserSimulationException(String msg) {
		Util.logInfo("Exception: " + msg);
	}
	public AutomateUserSimulationException() {
	}
	
}
