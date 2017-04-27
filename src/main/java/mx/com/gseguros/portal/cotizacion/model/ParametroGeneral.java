package mx.com.gseguros.portal.cotizacion.model;

public enum ParametroGeneral
{
	DIRECTORIO_REPORTES("DIR-REPORTS"),
	VALIDACION_SIGS_TRAMITE("VALITRASIGS"), // Que tramites se validan con sigs? |EMISION|ENDOSO|RENOVACION|
	MINUTOS_FLAG_AMARILLA("FLAG_AMAR_MIN"), // minutos defecto para flag amarilla
	MINUTOS_FLAG_ROJA("FLAG_ROJA_MIN"),     // minutos defecto para flag roja
	MINUTOS_FLAG_MAXIMA("FLAG_MAXI_MIN");   // minutos defecto para flag maxima
	
	private String nomparam;
	
	private ParametroGeneral(String nomparam)
	{
		this.nomparam = nomparam;
	}
	
	public String getNomparam()
	{
		return this.nomparam;
	}
}