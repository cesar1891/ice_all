package mx.com.aon.utils;

public class ServerConfigurationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServerConfigurationException() {
		super();
	}
	
	public ServerConfigurationException(String mensaje) {
		super(mensaje);
	}
	
	public ServerConfigurationException(String mensaje, Throwable throwable){
		super(mensaje, throwable);
	}
}
