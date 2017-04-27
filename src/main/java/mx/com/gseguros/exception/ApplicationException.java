package mx.com.gseguros.exception;

public class ApplicationException extends Exception {
	
	private static final long serialVersionUID = -3141050547999237875L;
	protected String          traza;
	
	public ApplicationException() {
		super();
	}
	
	/**
	 * 
	 * @param message
	 */
	public ApplicationException(String message) {
		super(message);
	}
	
	/**
	 * 
	 * @param cause
	 */
	public ApplicationException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationException(String message, String traza) {
		super(message);
		this.traza=traza;
	}
	
	public ApplicationException(String message, Throwable cause, String traza) {
		super(message, cause);
		this.traza=traza;
	}

	public String getTraza() {
		return traza;
	}

	public void setTraza(String traza) {
		this.traza = traza;
	}	
}