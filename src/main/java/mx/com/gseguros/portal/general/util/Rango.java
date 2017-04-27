package mx.com.gseguros.portal.general.util;

public enum Rango {
	
	ANIOS("1"),
	DIAS("2"),
	PESOS("3"),
	LONGITUD("4"),
	MESES("5");

	private String clave;

	private Rango(String clave) {
		this.clave = clave;
	}

	public String getClave() {
		return clave;
	}
	
}