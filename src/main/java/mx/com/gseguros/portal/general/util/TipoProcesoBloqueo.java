package mx.com.gseguros.portal.general.util;

/**
 * Enum con tips de proceso que se pueden bloquear 
 * @author Hector LT    
 *
 */
public enum TipoProcesoBloqueo {
	
	AUTORIZAR_NOMPLAN_SUPERVISOR ("AUTORIZAR_NOMPLAN_SUPERVISOR","Se requiere validar cambio de nombre de plan por Supervisor");
	
	
	private TipoProcesoBloqueo(String claveProceso, String descripcion) {
		this.claveProceso = claveProceso;
		this.descripcion = descripcion;
	}
	
	private String claveProceso;
	
	private String descripcion;

	
	public String getClaveProceso() {
		return claveProceso;
	}

	public String getDescripcion() {
		return descripcion;
	}
	
}