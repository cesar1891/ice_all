package mx.com.gseguros.exception;

public class WSException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 911555617504858834L;
	
	/**
	 * Payload del WS, xml que tiene contenida la peticion que se estaba realizando a un WS
	 */
	private String payload;

	public WSException() {
		super();
	}
	
	/**
	 * 
	 * @param message
	 */
	public WSException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param cause
	 */
	public WSException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public WSException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 * @param payload mensaje XML
	 */
	public WSException(String message, Throwable cause, String payload) {
		super(message, cause);
		this.payload = payload;
	}

	public String getPayload() {
		return payload;
	}	
}