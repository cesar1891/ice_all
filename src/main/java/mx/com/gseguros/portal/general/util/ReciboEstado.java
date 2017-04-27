package mx.com.gseguros.portal.general.util;

public enum ReciboEstado {
	
	PENDIENTE("PENDIENTE"),
	CANCELADO("CANCELADO"),
	PAGADO("PAGADO"),
	DEVUELTO("DEVUELTO");

	private String dsestado;

	private ReciboEstado(String dsestado) {
		this.dsestado = dsestado;
	}

	public String getDsestado() {
		return dsestado;
	}
	
}