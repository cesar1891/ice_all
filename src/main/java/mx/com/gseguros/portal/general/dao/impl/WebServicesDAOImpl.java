package mx.com.gseguros.portal.general.dao.impl;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.dao.impl.GenericMapper;
import mx.com.gseguros.portal.general.dao.WebServicesDAO;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class WebServicesDAOImpl extends AbstractManagerDAO implements WebServicesDAO {

	private Logger logger = Logger.getLogger(WebServicesDAOImpl.class);
	
    @Override
    public List<Map<String, String>> obtienePeticionesFallidasWS(Map params) throws Exception {
    	Map<String, Object> resultado = ejecutaSP(new ObtieneOpcionesLiga(getDataSource()), params);
    	return (List<Map<String, String>>) resultado.get("pv_registro_o");
    }
    
    protected class ObtieneOpcionesLiga extends StoredProcedure {
    	
    	protected ObtieneOpcionesLiga(DataSource dataSource) {
    		super(dataSource, "PKG_CONSULTA.P_WS_GET_TBITACOBROS_ALL");
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdcodigo_i", OracleTypes.VARCHAR));
    		String[] cols = new String[]{
    				"CDUNIECO"  , "CDRAMO"  , "NMPOLIZA" , "NMSUPLEM"
    				,"DESCRIPL" , "MENSAJE" , "USUARIO"  , "FECHAHR"
    				,"NTRAMITE" , "CDERRWS" , "SEQIDWS"
    		};
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }

    @Override
    public List<Map<String, String>> obtieneDetallePeticionWS(Map params) throws Exception {
    	Map<String, Object> resultado = ejecutaSP(new ObtieneDetallePeticionWS(getDataSource()), params);
    	return (List<Map<String, String>>) resultado.get("pv_registro_o");
    }
    
    protected class ObtieneDetallePeticionWS extends StoredProcedure {
    	
    	protected ObtieneDetallePeticionWS(DataSource dataSource) {
    		super(dataSource, "PKG_CONSULTA.P_WS_GET_TBITACOBROS_X_SEQ");
    		declareParameter(new SqlParameter("pv_seqidws_i", OracleTypes.VARCHAR));
    		String[] cols = new String[]{
    				"CDUNIECO" , "CDRAMO" , "ESTADO" , "NMPOLIZA" , "NMSUPLEM" , "NTRAMITE" , "CDURLWS" , "METODOWS" , "XMLIN"
    		};
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }

    
    @Override
	public String eliminaPeticionWS(Map params)
			throws Exception {
		
		Map<String, Object> mapResult = ejecutaSP(new EliminaPeticionWS(getDataSource()), params);
		
		return (String) mapResult.get("pv_title_o");
	}
	
	protected class EliminaPeticionWS extends StoredProcedure {

		protected EliminaPeticionWS(DataSource dataSource) {
			super(dataSource, "PKG_SATELITES.P_WS_ELIMINA_TBITACOBROS");
			
			declareParameter(new SqlParameter("pv_seqidws_i", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}

}