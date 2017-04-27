package mx.com.gseguros.portal.rehabilitacion.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface RehabilitacionDAO
{
	@Deprecated
	public List<Map<String,String>> buscarPolizas(Map<String,String> params) throws Exception;
	
	public List<Map<String, String>> buscarPolizas(
			String asegurado
			,String cdunieco
			,String cdramo
			,String nmpoliza
			,String nmsituac
			) throws Exception;
	
	public Map<String,Object> rehabilitarPoliza(Map<String, String> params, String cdusuari, String cdsisrol) throws Exception;
	
	/**
	 * valida Antiguedad
	 */
	public boolean validaAntiguedad(Map<String,String>params) throws Exception;
	
	/**
	 * borra antiguedad
	 */
	public void borraAntiguedad(Map<String,String>params) throws Exception;
	
	public Map<String,Object> rehabilitarPoliza(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,Date feefecto
			,Date fevencim
			,Date fecancel
			,Date fereinst
			,String cdrazon
			,String cdperson
			,String cdmoneda
			,String nmcancel
			,String comments
			,String nmsuplem
			,String cdusuari
			,String cdsisrol
			)throws Exception;
}