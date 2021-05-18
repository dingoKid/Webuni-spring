package hu.webuni.hr.gyd.service;

public class ClaimAlreadyApprovedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3560882208765988024L;
	private String message;

	public ClaimAlreadyApprovedException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
