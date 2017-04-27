package mx.com.gseguros.exception;

public class WSEmisionAutoException extends WSException {
	
	private static final long serialVersionUID = 6955976226751247116L;
	
	public WSEmisionAutoException() {
		super();
	}
	
	/**
	 * 
	 * @param message
	 */
	public WSEmisionAutoException(String message) {
		super(message);
	}
	
	/**
	 * 
	 * @param cause
	 */
	public WSEmisionAutoException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public WSEmisionAutoException(String message, Throwable cause) {
		super(message, cause);
	}

}