package mx.com.gseguros.confpantallas.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.gseguros.confpantallas.dao.GeneradorPantallasDAO;
import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.dao.impl.GenericMapper;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class GeneradorPantallasDAOImpl extends AbstractManagerDAO implements GeneradorPantallasDAO {
	
	private static Logger logger = Logger.getLogger(GeneradorPantallasDAOImpl.class);
	
	@Override
	public List<Map<String,String>> obtienePantalla(Map<String, String> params) throws Exception {
		Map<String,Object> resultadoMap = this.ejecutaSP(new ObtienePantalla(this.getDataSource()), params);
		return (List<Map<String,String>>) resultadoMap.get("PV_REGISTRO_O");
	}
	
	protected class ObtienePantalla extends StoredProcedure {
		protected ObtienePantalla(DataSource dataSource) {
			super(dataSource,"PKG_CONF_PANTALLAS.P_GET_PANTALLA_FINAL");
			declareParameter(new SqlParameter("PV_CDPANTALLA_I"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_CDRAMO_I"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_CDTIPSIT_I"    , OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"DATOS" , "COMPONENTES"
			};
			declareParameter(new SqlOutParameter("PV_REGISTRO_O" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("PV_MSG_ID_O"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("PV_TITLE_O"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	@Override
	public String insertaPantalla(String cdpantalla, String datos, String componentes) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("PV_CDPANTALLA_I", cdpantalla);
		params.put("PV_DATOS_I", datos);
		params.put("PV_COMPONENTES_I", componentes);
		Map<String,Object> resultadoMap=this.ejecutaSP(new InsertaPantalla(this.getDataSource()), params);
		logger.debug("resultadoMap=" + resultadoMap);
		logger.debug("PV_MSG_ID_O=" + resultadoMap.get("PV_MSG_ID_O"));
		logger.debug("PV_TITLE_O=" + resultadoMap.get("PV_TITLE_O"));
		return resultadoMap.get("PV_TITLE_O").toString(); 
	}
	
	protected class InsertaPantalla extends StoredProcedure {
		protected InsertaPantalla(DataSource dataSource) {
			super(dataSource,"PKG_CONF_PANTALLAS.P_MOV_TPANTALLAS");
			declareParameter(new SqlParameter("PV_CDPANTALLA_I" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_DATOS_I"      , OracleTypes.CLOB));
			declareParameter(new SqlParameter("PV_COMPONENTES_I", OracleTypes.CLOB));
			declareParameter(new SqlOutParameter("PV_MSG_ID_O"     , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("PV_TITLE_O"      , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	
	
	
}