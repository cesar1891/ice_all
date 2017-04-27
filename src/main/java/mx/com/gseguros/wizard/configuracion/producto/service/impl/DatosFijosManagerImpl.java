package mx.com.gseguros.wizard.configuracion.producto.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.datosFijos.model.DatoFijoVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.service.DatosFijosManager;
import mx.com.gseguros.wizard.dao.WizardDAO;

import org.apache.log4j.Logger;

public class DatosFijosManagerImpl implements DatosFijosManager {
	/**
	 * Logger de la clase.
	 * 
	 */
	
	
	private static Logger logger = Logger.getLogger(DatosFijosManagerImpl.class);

	private WizardDAO wizardDAO;

	public void setWizardDAO(WizardDAO wizardDAO) {
		this.wizardDAO = wizardDAO;
	}

	
	public List<LlaveValorVO> catalogoBloque() throws ApplicationException {
		
		List<LlaveValorVO> resultado = null;
		try{
			HashMap<String, Object> params = new HashMap<String, Object>();
			resultado = wizardDAO.catalogoBloque(params);
		}catch(Exception bae){
			
		}
		return resultado;
	}

	public List<LlaveValorVO> catalogoCampo(String claveBloque)
			throws ApplicationException {
		List<LlaveValorVO> resultado = null;
		try{
			HashMap<String, Object> params =  new HashMap<String, Object>();
			params.put("PV_OTTIPO_I", "P");
			params.put("PV_CDBLOQUE_I", claveBloque);
			
			resultado = wizardDAO.catalogoCampo(params);
		}catch(Exception bae){
			
		}
		return resultado;
	}

	public List<DatoFijoVO> listaDatosFijos(String codigoRamo) throws ApplicationException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("pv_cdramo_i", codigoRamo);
		List<DatoFijoVO> resultado = null;
		try{
			resultado = wizardDAO.listaDatosFijos(params);
			
		}catch(Exception bae){
			
		}
		return resultado;
	}

	public void insertarDatoFijo(DatoFijoVO dfvo) throws ApplicationException{	
		
		try{
			HashMap<String, Object> params  =  new HashMap<String, Object>();
			params.put("PV_CDRAMO_I", dfvo.getCodigoRamo());
			params.put("PV_CDFUNCIO_I", "SIGS2035");
			params.put("PV_CDBLOQUE_I", dfvo.getCodigoBloque());
			params.put("PV_CDCAMPO_I", dfvo.getCodigoCampo());
			params.put("PV_CDEXPRES_I", dfvo.getCodigoExpresion());
			
			wizardDAO.insertarDatoFijo(params);
		}catch(Exception bae){
			
		}
	}
	
}
