package mx.com.gseguros.portal.estadisticas.service;

import java.util.Date;
import java.util.Map;

import mx.com.gseguros.portal.cotizacion.model.Item;

public interface EstadisticasManager 
{
	public Map<String,Item> cotizacionEmision(StringBuilder sb, String cdsisrol) throws Exception;
	
	public Map<String,Object> recuperarCotizacionesEmisiones(
			StringBuilder sb
			,Date fedesde
			,Date fehasta
			,String cdunieco
			,String cdramo
			,String cdusuari
			,String cdagente
			)throws Exception;
	
	public Map<String,Item> tareas(StringBuilder sb, String cdsisrol) throws Exception;
	
	public Map<String,Object> recuperarTareas(
			StringBuilder sb
			,Date fedesde
			,Date fehasta
			,String cdmodulo
			,String cdtarea
			,String cdunieco
			,String cdramo
			,String cdusuari
			,String cdsisrol
			)throws Exception;
}