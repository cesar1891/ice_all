package mx.com.aon.tmp;

import java.util.List;
import java.util.Map;

import mx.com.gseguros.exception.ApplicationException;


/**
 * 
 * @author ricardo.bautista
 *
 */
public interface CatalogServiceJdbcTemplate {
	
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
	 * @return
	 * @throws ApplicationException
	 */
	@SuppressWarnings("unchecked")
	List getItemList( String endpointName, Map<String,Object> parameters)throws ApplicationException;
	
}
