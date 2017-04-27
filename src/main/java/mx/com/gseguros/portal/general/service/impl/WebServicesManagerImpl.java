package mx.com.gseguros.portal.general.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.exception.DaoException;
import mx.com.gseguros.portal.general.dao.WebServicesDAO;
import mx.com.gseguros.portal.general.service.WebServicesManager;
import mx.com.gseguros.utils.Constantes;

import org.apache.log4j.Logger;

public class WebServicesManagerImpl implements WebServicesManager {
	
	private Logger logger = Logger.getLogger(WebServicesManagerImpl.class);
	
	
	private WebServicesDAO webServicesDAO;

	@Override
	public List<Map<String, String>> obtienePeticionesFallidasWS(Map<String, String> params) throws Exception{
		try {
			return webServicesDAO.obtienePeticionesFallidasWS(params);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public List<Map<String, String>> obtieneDetallePeticionWS(Map<String, String> params) throws Exception{
		try {
			return webServicesDAO.obtieneDetallePeticionWS(params);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public boolean eliminaPeticionWS(List<Map<String, String>> listaEliminar) throws Exception{
		boolean allDeteted = true;
		try {
			Map<String, String> params = new HashMap<String, String>();
			for(Map<String,String> record : listaEliminar){
				params.put("pv_seqidws_i", record.get("SEQIDWS"));
				if(Constantes.MSG_TITLE_ERROR.equals(webServicesDAO.eliminaPeticionWS(params))){
					allDeteted = false;
				}
			}
			
			return allDeteted;
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	public void setWebServicesDAO(WebServicesDAO webServicesDAO) {
		this.webServicesDAO = webServicesDAO;
	}

}