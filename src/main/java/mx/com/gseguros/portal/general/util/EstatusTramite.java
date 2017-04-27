package mx.com.gseguros.portal.general.util;

public enum EstatusTramite {
	
	EN_REVISION_MEDICA("1"),
	PENDIENTE("2"),
	CONFIRMADO("3"),
	RECHAZADO("4"),
	VO_BO_MEDICO("5"),
	SOLICITUD_MEDICA("6"),
	EN_CAPTURA("7"),
	ENDOSO_EN_ESPERA("8"),
	ENDOSO_CONFIRMADO("9"),
	EN_ESPERA_DE_ASIGNACION("10"),
	EN_ESPERA_DE_AUTORIZACION("11"),
	EN_CAPTURA_CMM("12"),
	EN_SUSCRIPCION("13"),
	EN_ESPERA_DE_COTIZACION("14"),
	COTIZACION_EN_REVISION_MEDICA("15"),
	COTIZACION_CON_VOBO_MEDICO("16"),
	COTIZACION_APROBADA("17"),
	TRAMITE_COMPLETO("19"),
	TRAMITE_EN_DEVOLUCION("24"),
	IMPRESION_PENDIENTE("34"),
	IMPRESO("37"),
	EN_TARIFA("98"),
	EN_DOCUMENTACION("99"),
	TRAMITE_AGENTE("100");

	private String codigo;

	private EstatusTramite(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}
	
}