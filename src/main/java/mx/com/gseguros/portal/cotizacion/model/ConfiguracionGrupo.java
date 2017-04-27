package mx.com.gseguros.portal.cotizacion.model;

public class ConfiguracionGrupo
{

	//Cuando el registro es un atributo de TATRISIT
	public static final String TIPO_ATRIBUTO  = "ATRIBUTO";
	
	//Cuando el registro es un atributo de TATRISIT que se llena desde un valor de TVALOGAR
	public static final String TIPO_DOBLE     = "DOBLE";
	
	//Cuando el registro es una columna que representa una cobertura pero no tiene relacion a TATRISIT
	public static final String TIPO_COBERTURA = "COBERTURA";
	
	//Cuando el registro inserta OTVALOR de TVALOGAR
	public static final String TIPOGAR_VALOR     = "VALOR";
	
	//Cuando el registro inserta o borra el registro de MPOLIGAR
	public static final String TIPOGAR_INSERCION = "INSERCION";
	
}