package mx.com.gseguros.portal.dao.impl;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.gseguros.exception.DaoException;
import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.dao.ProcesoResultadoDAO;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class ProcesoResultadoDAOImpl extends AbstractManagerDAO implements ProcesoResultadoDAO {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ProcesoResultadoDAOImpl.class);

    protected class ObtieneMensajeSP extends StoredProcedure {
    	
    	protected ObtieneMensajeSP(DataSource dataSource) {
    		super(dataSource, "PKG_TRADUC.P_OBTIENE_MENSAJE");
			declareParameter(new SqlParameter("pv_msg_id_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_log_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdusuario_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsprograma_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_text_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
    	}
    	
    }


	@Override
	public String obtieneMensaje(String msgId, String log, String cdUsuario, String dsPrograma) throws DaoException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pv_msg_id_i", msgId);
			params.put("pv_log_i", log);
			params.put("pv_cdusuario_i", cdUsuario);
			params.put("pv_dsprograma_i", dsPrograma);
			
			Map<String, Object> mapResult = new ObtieneMensajeSP(getDataSource()).execute(params);
			
	        return mapResult.get("pv_msg_text_o").toString();
        
		} catch(Exception e) {
        	throw new DaoException(e.getMessage(), e);
        }
	}

}