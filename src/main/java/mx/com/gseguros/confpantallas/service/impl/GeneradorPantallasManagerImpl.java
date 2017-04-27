package mx.com.gseguros.confpantallas.service.impl;

import java.util.List;
import java.util.Map;

import mx.com.gseguros.confpantallas.dao.GeneradorPantallasDAO;

import org.apache.log4j.Logger;

public class GeneradorPantallasManagerImpl implements GeneradorPantallasManager {
	
	private Logger logger = Logger.getLogger(GeneradorPantallasManagerImpl.class);
	
	private GeneradorPantallasDAO generadorPantallasDAO;

	public List<Map<String, String>> obtienePantalla(Map<String, String> params) throws Exception {
		return generadorPantallasDAO.obtienePantalla(params);
	}

	public String insertaPantalla(String cdpantalla, String datos, String componentes) throws Exception {
		return generadorPantallasDAO.insertaPantalla(cdpantalla, datos, componentes);
	}

	
	
	
	
	/**
	 * Setter
	 * @param generadorPantallasDAO
	 */
	public void setGeneradorPantallasDAO(GeneradorPantallasDAO generadorPantallasDAO) {
		this.generadorPantallasDAO = generadorPantallasDAO;
	}
	
}