package mx.com.gseguros.portal.general.util;

public enum TipoRamo
{
	
	AUTOS("2")
	,SALUD("10")
	;
	
	private String cdtipram;

	private TipoRamo(String cdtipram) {
		this.cdtipram = cdtipram;
	}

	public String getCdtipram() {
		return cdtipram;
	}
}