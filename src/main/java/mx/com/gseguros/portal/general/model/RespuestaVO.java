package mx.com.gseguros.portal.general.model;

import java.io.Serializable;

public class RespuestaVO implements Serializable {

	private static final long serialVersionUID = -5174703012933088228L;

	/**
	 * Resultado de la operacion
	 */
	private boolean success;
	
	/**
	 * Mensaje de respuesta de la operacion
	 */
	private String mensaje;
	
	
	public RespuestaVO() {
		super();
	}
	
	public RespuestaVO(boolean success, String mensaje) {
		super();
		this.success = success;
		this.mensaje = mensaje;
	}
	
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
}
