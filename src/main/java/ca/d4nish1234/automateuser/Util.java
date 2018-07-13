/**
 * @auther Danish Mahboob
 * Date: April 10 2017
 */
package ca.d4nish1234.automateuser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Danish Mahboob
 * @since July 13 2018
 */
public class Util {
	Util(){
		logger.setLevel(Level.ALL);
	}
	private static final Logger logger = Logger.getLogger(AutomateUserSimulationController.class.getName());
	public static Properties loadFromFile(String fileName) throws IOException{
		Properties properties = null;
			try(InputStream is = new FileInputStream(fileName);){
		//		try(InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);){
			properties = new Properties();
			properties.load(is);
			
		} //catch (Exception e){
			//Util.logWarn("Could not load properties file: " + fileName);
			//System.exit(0);
		//}
			return properties;
	}

	public static void logInfo(String msg) {
		logger.info(msg);
	}
	public static void logWarn(String msg) {
		logger.warning(msg);
	}
	public static void logSevere(String msg) {
		logger.severe(msg);
	}
}
