package mx.com.gseguros.portal.general.util;

public enum TipoFecha {
	
	ddMMyyyy_Diagonal("1"), 
	ddMMyyyy_Gion("2"),
	yyyyMMdd_Diagonal("3"),
	yyyyMMdd_Gion("4"),
	ddMMyyyyhhmmss_Diagonal("5");
	
	private String codigo;

	private TipoFecha(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}
	
}