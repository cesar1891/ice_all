package mx.com.gseguros.portal.catalogos.service;

import java.util.Map;

import mx.com.gseguros.portal.cotizacion.model.Item;

public interface TvalositManager
{
	
	public Map<String,Item> pantallaActTvalosit(
			String origen
			,String cdsisrol
			,String cdtipsit
			,String contexto
			)throws Exception;
	
	public Map<String,String> cargarPantallaActTvalosit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			)throws Exception;
	
	public void guardarPantallaActTvalosit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,Map<String,String> otvalores
			)throws Exception;
	
}