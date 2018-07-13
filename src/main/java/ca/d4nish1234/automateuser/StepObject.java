package ca.d4nish1234.automateuser;

public class StepObject {
	private String Stepvalue;
	private Action action;
	
	public StepObject(String stepvalue, Action action) {
		super();
		
		this.action = action;
		Stepvalue = stepvalue;
	}
	
	public Action getAction() {
		return action;
	}
	public String getStepvalue() {
		return Stepvalue;
	}
	public void setStepvalue(String stepvalue) {
		Stepvalue = stepvalue;
	}
	public void setAction(Action action) {
		this.action = action;
	}
}
