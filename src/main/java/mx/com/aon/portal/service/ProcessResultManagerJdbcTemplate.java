package mx.com.aon.portal.service;

import java.util.HashMap;

import mx.com.aon.portal.dao.AbstractDAO;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.exception.DaoException;
import mx.com.gseguros.utils.Constantes;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

@Deprecated
public class ProcessResultManagerJdbcTemplate {

    @SuppressWarnings("unused")
	private  static String ERROR_MSG_ID = "100000";
    private  static String ERROR_MSG_TITLE = "1";
    private  static String ERROR_STORE_PROCEDURE = "OBTIENE_MENSAJE";//"PKG_TRADUC.P_OBTIENE_MENSAJE";

    /**
	 * Logger de la clase para monitoreo y registro de comportamiento
	 */
	protected static Logger logger = Logger.getLogger(ProcessResultManagerJdbcTemplate.class);
    private AbstractDAO abstractDAO;


    public void setAbstractDAO(AbstractDAO abstractDAO) {
        this.abstractDAO = abstractDAO;
    }

    
    /**
     * 
     * @param res
     * @return
     * @throws ApplicationException
     */
    public WrapperResultados processResultMessageId(WrapperResultados res) throws ApplicationException {

    	String msgId    = res.getMsgId();
    	String msgTitle = res.getMsgTitle();
    	
    	if( StringUtils.isNotBlank(msgId) || StringUtils.isNotBlank(msgTitle) ) {
    		logger.info(new StringBuilder("MsgId=").append(msgId).append(" ").append("MsgTitle=").append(msgTitle));
    	}
    	
    	// Obtenemos el msgText a partir del msgId:
    	String msgText  = "";
        if(StringUtils.isNotBlank(res.getMsgText())) {
        	msgText = res.getMsgText();
        } else if (StringUtils.isNotBlank(msgId)) {
        	// Buscamos el msgText en properties, sino lo buscamos en BD:
	    	ActionSupport actionSupport = new ActionSupport();
	        if (!actionSupport.getText(msgId).equals(msgId)) {
	        	msgText = actionSupport.getText(msgId);
	        } else {
	        	msgText = getResultMessage(msgId);
	        }

            if (StringUtils.isBlank(msgText)) {
            	String msgException = "No se encontr\u00F3 el mensaje de respuesta del servicio de datos, verifique los par\u00E1metros de salida";
                logger.error(msgException);
                throw new ApplicationException(msgException);
            }
            
            logger.info(new StringBuilder("MsgText=").append(msgText));
        }
        
        // Si msgTitle es de tipo ERROR, lanzamos la excepci&oacute;n con el msgText obtenido:
        if (msgTitle.equals(Constantes.MSG_TITLE_ERROR)) {
			logger.error(new StringBuilder("Error de SP: ").append(msgText));
			throw new ApplicationException(msgText);
        }
        
        res.setMsgText(msgText);
        return res;
    }
    
    
    /**
     * Obtiene el mensaje asociado al msgId
     * @param msgId
     * @return msgText asociado al msgId
     * @throws ApplicationException
     */
    private String getResultMessage(String msgId) throws ApplicationException {
        
    	String msgText = null;
    	
        // Buscamos el mensaje en los properties, sino en BD:
    	ActionSupport actionSupport = new ActionSupport();
    	if ( !actionSupport.getText(msgId).equals(msgId) ) {
    		msgText = actionSupport.getText(msgId);
        } else {
            try {
            	HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("pv_msg_id_i", msgId);
                map.put("pv_log_i", "0");
                map.put("pv_cdusuario_i", null);
                map.put("pv_dsprograma_i", null);
            	WrapperResultados result = new WrapperResultados();
            	result = (WrapperResultados) abstractDAO.invoke(ERROR_STORE_PROCEDURE,map);
            	msgText = result.getMsgText();
            } catch (DaoException e) {
            	StringBuilder msgExc = new StringBuilder("Error al invocar el servicio de datos que obtiene el mensaje de respuesta: ").append(ERROR_STORE_PROCEDURE); 
            	logger.error(msgExc, e);
                throw new ApplicationException(msgExc.toString(), e);
            }
        }
    	return msgText;
    }

}