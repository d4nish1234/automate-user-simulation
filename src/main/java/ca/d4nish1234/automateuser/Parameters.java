/**
 * this class is for storing arguments that are sent from command line
 */
package ca.d4nish1234.automateuser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Danish Mahboob
 * @since July 13 2018
 */
class Parameters {
	private Map<String,String> parameters;

/**
 * insert a parameter
 * @param param
 * @param value
 */
	public void addParameters(String param, String value) {
		
		if (parameters ==null) {
			// lazy instantiation of parameters object
			parameters = new HashMap<String,String>();
		}
		parameters.put(param,value);
	}
	public Map<String, String> getParameters() {
		return parameters;
	}
	public String getParameter(String param) {
		return parameters.get(param);
	}

}
