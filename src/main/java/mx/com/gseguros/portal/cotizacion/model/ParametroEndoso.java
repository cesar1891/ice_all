package mx.com.gseguros.portal.cotizacion.model;

public enum ParametroEndoso
{
	ENDOSO_PERMITIDO("ENDOSO_PERMITIDO")              //ENDOSOS PERMITIDOS
	,RELACION_ENDOSO_ATRIBUTO_SITUACION("RELSUPLATRI")//RELACION ENDOSO CON ATRIBUTO DE SITUACION
	;
	
	private String parametro;
	
	private ParametroEndoso(String parametro)
	{
		this.parametro=parametro;
	}
	
	public String getParametro()
	{
		return this.parametro;
	}
}