package mx.com.gseguros.portal.general.util;

public enum TipoTramite {
	
	POLIZA_NUEVA("1"),
	ALTA_AFILIADO("2"),
	REHABILITACION("3"),
	BAJA_AFILIADO("4"),
	CANCELACION_PRORRATA("5"),
	CANCELACION("6"),
	CANCELACION_POR_PAGO_IMPROCEDENTE("7"),
	MODIFICATORIO_ALTA("8"),
	MODIFICATORIO_BAJA("9"),
	CAMBIO_CONTRATANTE("10"),
	CAMBIO_AGENTE("11"),
	CAMBIO_VIGENCIA("12"),
	CAMBIO_FORMA_PAGO("13"),
	AUTORIZACION_SERVICIOS("14"),
	ENDOSO("15"),
	SINIESTRO("16"),
	EMISION_EN_ESPERA("17"),
	PAGO_AUTOMATICO("19"),
	IMPRESION("20"),
	RENOVACION("21");

	private String cdtiptra;

	private TipoTramite(String cdtiptra) {
		this.cdtiptra = cdtiptra;
	}

	public String getCdtiptra() {
		return cdtiptra;
	}
}