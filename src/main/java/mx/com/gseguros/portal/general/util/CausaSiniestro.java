package mx.com.gseguros.portal.general.util;
/**
 * Causas de un siniestro
 */
public enum CausaSiniestro {
	
	ENFERMEDAD("1", "Enfermedad", "TCAUSASSV"),
	ACCIDENTE ("2", "Accidente" , "TCAUSASSV"),
	MATERNIDAD("3", "Maternidad", "TCAUSASSV");

	/**
	 * C&oacute;digo de la causa del siniestro
	 */
	private String codigo;
	
	/**
	 * Descripci&oacute;n de la causa del siniestro
	 */
	private String descripcion;
	
	/**
	 * C&oacute;digo de tabla (fuente de datos) de las causas del siniestro
	 */
	private String cdTabla;
	
	private CausaSiniestro(String codigo, String descripcion, String cdTabla) {
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.cdTabla = cdTabla;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getCdTabla() {
		return cdTabla;
	}
	
}