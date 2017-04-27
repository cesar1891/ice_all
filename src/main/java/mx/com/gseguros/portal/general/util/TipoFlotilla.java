package mx.com.gseguros.portal.general.util;

public enum TipoFlotilla {
	
	Tipo_Flotilla     ("F"),
	Tipo_PyMES        ("P")
	
	;

	private String cdtipsit;

	private TipoFlotilla(String cdtipsit) {
		this.cdtipsit = cdtipsit;
	}

	public String getCdtipsit() {
		return cdtipsit;
	}
	
}