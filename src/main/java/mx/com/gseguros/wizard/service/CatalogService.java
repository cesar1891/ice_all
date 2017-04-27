package mx.com.gseguros.wizard.service;

import java.util.List;
import java.util.Map;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.model.MensajesVO;


/**
 * 
 * @author Leopoldo Ramirez
 *
 */
public interface CatalogService {
	
	/**
	 * 
	 * @param endpointName
	 * @return
	 * @throws ApplicationException
	 */
	@SuppressWarnings("unchecked")
	List getItemList(String endpointName)throws ApplicationException;
	
	
	/**
     * 
     * @param endpointName
     * @return
     * @throws ApplicationException
     */
    @SuppressWarnings("unchecked")
    List getItemList(String endpointName, String itemId)throws ApplicationException;
    
    /**
	 * 
	 * @param endpointName
	 * @param parameters
	 * @return ItemList
	 * @throws ApplicationException
	 */
    @SuppressWarnings("unchecked")
    List getWrapperItemList (String endpointName, Map<String, Object> parameters) throws ApplicationException;
	
	/**
	 * 
	 * @param endpointName
	 * @param parameters
	 * @return
	 * @throws ApplicationException
	 */
	@SuppressWarnings("unchecked")
	List getItemList( String endpointName, Map<String,Object> parameters)throws ApplicationException;
	
	/**
	 * 
	 * @param  endpointName
	 * @param  params
	 * @return MensajesVO
	 * @throws ApplicationException
	 */
	@SuppressWarnings("unchecked")
	MensajesVO getMensajes(Map<String, String> params, String endpointName) throws ApplicationException;	
}
