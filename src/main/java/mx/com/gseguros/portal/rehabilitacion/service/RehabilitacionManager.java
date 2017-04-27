package mx.com.gseguros.portal.rehabilitacion.service;

import java.util.List;
import java.util.Map;

public interface RehabilitacionManager
{
	public List<Map<String,String>> buscarPolizas    (Map<String,String> params)  throws Exception;
	public Map<String,Object>       rehabilitarPoliza(Map<String, String> params, String cdusuari, String cdsisrol) throws Exception;
	
	/**
	 * pkg_satelites.p_valida_antiguedad
	 */
	public boolean validaAntiguedad(String cdunieco,String cdramo,String estado,String nmpoliza) throws Exception;
	/**
	 * pkg_satelites.p_borra_antiguedad
	 */
	public void    borraAntiguedad(String cdunieco,String cdramo,String estado,String nmpoliza,String nmsuplem,String fereinste) throws Exception;
}