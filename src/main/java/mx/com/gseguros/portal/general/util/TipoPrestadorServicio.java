package mx.com.gseguros.portal.general.util;

public enum TipoPrestadorServicio {
	
	MEDICO("15"),
	CLINICA("16");

	private String cdtipo;

	private TipoPrestadorServicio(String cdtipo) {
		this.cdtipo = cdtipo;
	}

	public String getCdtipo() {
		return cdtipo;
	}
	
}