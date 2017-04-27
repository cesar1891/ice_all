package mx.com.gseguros.wizard.service.impl;

import java.util.List;
import java.util.Map;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.model.MensajesVO;
import mx.com.gseguros.wizard.service.CatalogService;

import org.apache.log4j.Logger;

public class CatalogServiceImpl implements CatalogService {
	
	/**
	 * 
	 */
	private final transient Logger logger = Logger.getLogger(CatalogServiceImpl.class);
	
	/**
	 * 
	 */
	//private Map<String,Endpoint> endpoints;
	
	
	/**
	 * @param endpoints the endpoints to set
	 */
//	public void setEndpoints(Map<String, Endpoint> endpoints) {
//		this.endpoints = endpoints;
//	}	

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List getItemList(String endpointName) throws ApplicationException{
		
		List itemList = null;
		
		try {
			//Endpoint endpoint = endpoints.get(endpointName);
			//itemList = (ArrayList) endpoint.invoke(null);
			
		} catch (Exception bae) {
			logger.error("Exception in invoke Consulta de catalogo ",bae);
			throw new ApplicationException("Error al consultar un catalogo");
		}
		
		return itemList;
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List getItemList(String endpointName, Map<String, Object> parameters) 
		throws ApplicationException {		
		
		List itemList = null;
		
		try {
//			Endpoint endpoint = endpoints.get(endpointName);
//			itemList = (ArrayList) endpoint.invoke(parameters);
			
		} catch (Exception bae) {			
			logger.error("Exception in invoke Consulta de catalogo ",bae);
			throw new ApplicationException("Error al consultar un catalogo");
		}
		
		return itemList;
	}

    /* (non-Javadoc)
     * @see mx.com.aon.portal.service.CatalogService#getItemList(java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List getItemList(String endpointName, String itemId) throws ApplicationException {
        List itemList = null;
        
        try {
//            Endpoint endpoint = endpoints.get(endpointName);
//            itemList = (ArrayList) endpoint.invoke(itemId);
            
        } catch (Exception bae) {            
            logger.error("Exception in invoke Consulta de catalogo ",bae);
            throw new ApplicationException("Error al consultar un catalogo");
        }
        
        return itemList;
    }
    @SuppressWarnings("unchecked")
	public List getWrapperItemList (String endpointName, Map<String, Object> parameters) throws ApplicationException {
//		return getAllBackBoneInvoke(parameters, endpointName);
    	return null;
	}
    
    public MensajesVO getMensajes(Map<String, String> params, String endpointName) throws ApplicationException {
    	MensajesVO msg = null;
        
        try {
//            Endpoint endpoint = endpoints.get(endpointName);
//            msg = (MensajesVO) endpoint.invoke(params);
//            
        } catch (Exception bae) {            
            logger.error("Exception in invoke Consulta de catalogo ",bae);
            throw new ApplicationException("Error al consultar un catalogo");
        }
        
        return msg;
    }

}
