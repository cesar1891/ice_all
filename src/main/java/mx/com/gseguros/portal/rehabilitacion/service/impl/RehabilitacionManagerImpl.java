package mx.com.gseguros.portal.rehabilitacion.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.portal.rehabilitacion.dao.RehabilitacionDAO;
import mx.com.gseguros.portal.rehabilitacion.service.RehabilitacionManager;

import org.apache.log4j.Logger;

public class RehabilitacionManagerImpl implements RehabilitacionManager
{
	private static Logger     log               = Logger.getLogger(RehabilitacionManagerImpl.class);
	private RehabilitacionDAO rehabilitacionDAO;

	@Override
	public List<Map<String, String>> buscarPolizas(Map<String, String> params) throws Exception {
		log.debug("RehabilitacionManager buscarPolizas params: "+params);
		List<Map<String,String>> lista=rehabilitacionDAO.buscarPolizas(params);
		lista=lista!=null?lista:new ArrayList<Map<String,String>>(0);
		log.debug("RehabilitacionManager buscarPolizas lista size: "+lista.size());
		return lista;
	}

	@Override
	public Map<String,Object> rehabilitarPoliza(Map<String, String> params,String cdusuari, String cdsisrol) throws Exception
	{
		log.debug("RehabilitacionManager rehabilitarPoliza params: "+params);
		Map<String,Object> mapa=rehabilitacionDAO.rehabilitarPoliza(params,cdusuari,cdsisrol);
		log.debug("RehabilitacionManager rehabilitarPoliza respuesta: "+mapa);
		return mapa;
	}
	
	/**
	 * pkg_satelites.p_valida_antiguedad
	 */
	@Override
	public boolean validaAntiguedad(String cdunieco,String cdramo,String estado,String nmpoliza) throws Exception
	{
		Map<String,String>params = new HashMap<String,String>();
		params.put("pv_cdunieco_i" , cdunieco);
		params.put("pv_cdramo_i"   , cdramo);
		params.put("pv_estado_i"   , estado);
		params.put("pv_nmpoliza_i" , nmpoliza);
		log.debug("RehabilitacionManager validaAntiguedad params: "+params);
		boolean validacion = rehabilitacionDAO.validaAntiguedad(params);
		log.debug("RehabilitacionManager validaAntiguedad alguno tieneantiguedad: "+(validacion?"si":"no"));
		return validacion;
	}
	
	/**
	 * pkg_satelites.p_borra_antiguedad
	 */
	@Override
	public void borraAntiguedad(String cdunieco,String cdramo,String estado,String nmpoliza,String nmsuplem,String fereinst) throws Exception
	{
		Map<String,String>params = new HashMap<String,String>();
		params.put("pv_cdunieco_i" , cdunieco);
		params.put("pv_cdramo_i"   , cdramo);
		params.put("pv_estado_i"   , estado);
		params.put("pv_nmpoliza_i" , nmpoliza);
		params.put("pv_nmsuplem_i" , nmsuplem);
		params.put("pv_fereinst_i" , fereinst);
		log.debug("RehabilitacionManager borraAntiguedad params: "+params);
		rehabilitacionDAO.borraAntiguedad(params);
		log.debug("RehabilitacionManager borraAntiguedad end");
	}
	
	/////////////////////////////////
	////// getters and setters //////
	/*/////////////////////////////*/
	public void setRehabilitacionDAO(RehabilitacionDAO rehabilitacionDAO) {
		this.rehabilitacionDAO = rehabilitacionDAO;
	}
	
}