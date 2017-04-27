package mx.com.gseguros.portal.general.util;

public enum Validacion {
	
	/**
	 * Edad m&aacute;xima de cotizaci&oacute;n
	 */
	EDAD_MAX_COTIZACION("1"),
	/**
	 * Dias maximos permitidos entre la fecha de ocurrencia y fecha de autorizacion
	 */
	DIAS_MAX_AUTORIZACION_SERVICIOS("2"),
	/**
	 * Monto maximo de una autorizacion sin que requiera autorizacion especial
	 */
	MONTO_MAXIMO_AUTORIZACION_SERVICIOS("3"),
	/**
	 * Longitud m&aacute;xima del nombre del contratante
	 */
	LONGITUD_MAX_CONTRATANTE("4"),
	/**
	 * Numero de meses para validacion de Maternidad*/
	MESES_MAX_MATERNIDAD("5");
	
	private String clave;
	
	private Validacion(String clave) {
		this.clave = clave;
	}

	public String getClave() {
		return clave;
	}
	
}