package mx.com.aon.portal.dao;

import java.sql.SQLException;
import java.util.Map;

import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.portal.dao.AbstractManagerDAO;

import org.apache.log4j.Logger;
@Deprecated
public class WrapperResultadosGeneric implements WrapperResultadosBuilder {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(AbstractManagerDAO.class);
	
    @SuppressWarnings("rawtypes")
	public WrapperResultados build(Map map) throws SQLException {
    	//logger.debug("### map en wrapperresultadosgeneric: "+map+"\n###");
        WrapperResultados wrapperResultados = new WrapperResultados();
        String msgId    = "";
        String msgTitle = "";
        String msgText  = "";
        if(map.get("pv_msg_id_o")!=null)
        {
        	msgId = map.get("pv_msg_id_o").toString();
        }
        if(map.get("pv_title_o")!=null)
        {
        	msgTitle = map.get("pv_title_o").toString();
        }
        if(map.get("pv_msg_text_o")!=null)
        {
        	msgText = map.get("pv_msg_text_o").toString();
        }
        wrapperResultados.setMsgId(msgId);
        wrapperResultados.setMsgTitle(msgTitle);
        wrapperResultados.setMsgText(msgText);
        return wrapperResultados;
     }

}