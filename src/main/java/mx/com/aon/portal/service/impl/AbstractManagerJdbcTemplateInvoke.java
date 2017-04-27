package mx.com.aon.portal.service.impl;

import java.util.List;

import mx.com.aon.portal.dao.AbstractDAO;
import mx.com.aon.portal.service.PagedList;
import mx.com.aon.portal.service.ProcessResultManagerJdbcTemplate;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.exception.DaoException;

import org.apache.log4j.Logger;
@Deprecated
public abstract class AbstractManagerJdbcTemplateInvoke {

    /**
	 * Logger de la clase para monitoreo y registro de comportamiento
	 */
	protected static Logger logger = Logger.getLogger(AbstractManagerJdbcTemplateInvoke.class);
	/**
	 * Mapa en el cual se introducen los Manager's para ser extraidos y utilizados como servicios
	 */

    private AbstractDAO abstractDAO;

    private ProcessResultManagerJdbcTemplate processResultManager;


    public void setProcessResultManager(ProcessResultManagerJdbcTemplate processResultManager) {
        this.processResultManager = processResultManager;
    }

    public void setAbstractDAO(AbstractDAO abstractDAO) {
        this.abstractDAO = abstractDAO;
    }
	
    /**
     * Metodo que ejecuta backbone y obtiene un registro dado un conjunto de argumentos
     *
     *
     * @param arg Object a pasar a metodo invoke de backbone
     * @param endpointName nombre del endpoint a invocar
     * @return instancia de WrapperResultados cargado con los id de msgs de respuesta y resultados
     * @throws mx.com.gseguros.exception.ApplicationException
     */
    public Object getBackBoneInvoke(Object arg,String endpointName ) throws ApplicationException {
        WrapperResultados res;

        try {

            res = (WrapperResultados) abstractDAO.invoke(endpointName,arg);

        } catch (DaoException e) {
        	StringBuilder msgExc = new StringBuilder("No se pudo ejecutar el endpoint ").append(endpointName);
			logger.error(msgExc, e);
			throw new ApplicationException(msgExc.toString(), e);
        }

        res = processResultManager.processResultMessageId(res);

        if (res.getItemList()==null) {
            logger.debug("No se encontraron datos");
            throw new ApplicationException("No se encontraron datos");
        }
        if (res.getItemList().size()==0) {
            logger.debug("No se encontraron datos");
            throw new ApplicationException("No se encontraron datos");
        }

        // Chequeamos que solo se retorne un solo rgistro
        if (res.getItemList() != null && res.getItemList().size()>0 )  {
            return res.getItemList().get(0);
        } else {
            throw new ApplicationException("Registro no encontrado");
        }
    }

    /**
     * Metodo que ejecuta backbone y obtiene un registro dado un conjunto de argumentos
     *
     *
     * @param arg Object a pasar a metodo invoke de backbone
     * @param endpointName nombre del endpoint a invocar
     * @return instancia de WrapperResultados cargado con los id de msgs de respuesta y resultados
     * @throws mx.com.gseguros.exception.ApplicationException
     */
    public WrapperResultados returnBackBoneInvoke(Object arg,String endpointName ) throws ApplicationException {
        WrapperResultados res;

        try {

            res = (WrapperResultados) abstractDAO.invoke(endpointName,arg);

        } catch (DaoException e) {
        	StringBuilder msgExc = new StringBuilder("No se pudo ejecutar el endpoint ").append(endpointName);
			logger.error(msgExc, e);
			throw new ApplicationException(msgExc.toString(), e);
        }

        res = processResultManager.processResultMessageId(res);
        return res;

    }

    /**
     * Metodo que ejecuta backbone y obtiene un registro dado un conjunto de argumentos
     *
     *
     * @param arg Object a pasar a metodo invoke de backbone
     * @param endpointName nombre del endpoint a invocar
     * @return instancia de WrapperResultados cargado con los id de msgs de respuesta y resultados
     * @throws mx.com.gseguros.exception.ApplicationException
     */
    public WrapperResultados returnResult(Object arg,String endpointName ) throws ApplicationException {
        WrapperResultados res;

        try {

            res = (WrapperResultados) abstractDAO.invoke(endpointName,arg);

        } catch (DaoException e) {
        	StringBuilder msgExc = new StringBuilder("No se pudo ejecutar el endpoint ").append(endpointName);
			logger.error(msgExc, e);
			throw new ApplicationException(msgExc.toString(), e);
        }

        //res = processResultManager.processResultMessageId(res);
        return res;

    }
    
    
    @SuppressWarnings("rawtypes")
	public List getAllBackBoneInvoke(Object arg,String endpointName) throws ApplicationException {
        WrapperResultados res;

        try {

            res = (WrapperResultados) abstractDAO.invoke(endpointName,arg);

        } catch (DaoException e) {
        	StringBuilder msgExc = new StringBuilder("No se pudo ejecutar el endpoint ").append(endpointName);
			logger.error(msgExc, e);
			throw new ApplicationException(msgExc.toString(), e);
        }

        if (res.getItemList()==null) {
            logger.debug("No se encontraron datos");
            throw new ApplicationException("No se encontraron datos");
        }


        res = processResultManager.processResultMessageId(res);


        return res.getItemList();

    }
    
    /**
	 * Metodo que ejecuta backbone y obtiene una lista filtrada de el cursor de salida obtenido
	 *
	 * Nota: Este metodo existe como alternativa a la carencia de paginacion por parte de backbone (o para acceder a los resultset)
	 * y porque en el proyecto actual solo se invocan stored procedures q no reciben ningun parametro de paginacion.
	 *
	 * @param arg Object a pasar a metodo invoke de backbone
	 * @param endpointName nombre del endpoint a invocar
	 * @param start  desde , paginacion
	 * @param limit  hasta , paginacion
	 * @return instancia de WrapperResultados cargado con los id de msgs de respuesta y resultados
	 * @throws mx.com.aon.core.ApplicationException
     */
	public PagedList pagedBackBoneInvoke(Object arg,String endpointName ,int start,int limit) throws ApplicationException {
		WrapperResultados res;

		try {

            res = (WrapperResultados) abstractDAO.invoke(endpointName,arg);

        } catch (Exception e) {
        	StringBuilder msgExc = new StringBuilder("No se pudo ejecutar el endpoint ").append(endpointName);
			logger.error(msgExc, e);
			throw new ApplicationException(msgExc.toString(), e);
		}
        
        PagedList pagedList = new PagedList();
        pagedList.setItemMap(res.getItemMap());
        
        if (res.getItemList()==null) {
            logger.debug("No se encontraron datos");
            throw new ApplicationException("No se encontraron datos");
        }
        if (res.getItemList().size()==0) {
            logger.debug("No se encontraron datos");
            throw new ApplicationException("No se encontraron datos");
        }
        //carga el tamanio antes de filtrar la lista
		res.setNotPagedTotalItems( res.getItemList().size() );
		
		
		//Si limit es -1 regresa todos los elementos (esto para el caso en el que se reutilizan los metodos en combos y no en pagedGrids 
		if(limit != -1){
	        if (res.getItemList().size() < (start + limit)) {
	          limit =  res.getItemList().size();
	        }  else {
	          limit = start + limit;
	        }
		
	        //sobrescribe la lista con una sublista
			res.setItemList( res.getItemList().subList(start, limit) );
		}
        
        
        res = processResultManager.processResultMessageId(res);

        pagedList.setItemsRangeList(res.getItemList());
        pagedList.setTotalItems(res.getNotPagedTotalItems());
        pagedList.setMessageResult(res.getMsgText());
        

        return pagedList;

	}

}
