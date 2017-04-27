package mx.com.gseguros.exception;

public class ValidationDataException extends ApplicationException {
	
	private static final long serialVersionUID = -3141050547999237875L;
	
	public ValidationDataException() {
		super();
	}
	
	/**
	 * 
	 * @param message
	 */
	public ValidationDataException(String message) {
		super(message);
	}
	
	/**
	 * 
	 * @param cause
	 */
	public ValidationDataException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public ValidationDataException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ValidationDataException(String message, String traza) {
		super(message);
		this.traza=traza;
	}
}