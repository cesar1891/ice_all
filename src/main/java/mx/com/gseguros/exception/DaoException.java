package mx.com.gseguros.exception;

public class DaoException extends Exception {
	
	private static final long serialVersionUID = -3141050547999237875L;
	
	public DaoException() {
		super();
	}
	
	/**
	 * 
	 * @param message
	 */
	public DaoException(String message) {
		super(message);
	}
	
	/**
	 * 
	 * @param cause
	 */
	public DaoException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}	
}