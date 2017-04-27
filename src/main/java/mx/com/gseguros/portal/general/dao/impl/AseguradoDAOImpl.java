package mx.com.gseguros.portal.general.dao.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.dao.impl.GenericMapper;
import mx.com.gseguros.portal.general.dao.AseguradoDAO;
import oracle.jdbc.OracleTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class AseguradoDAOImpl extends AbstractManagerDAO implements AseguradoDAO {

	private Logger logger = LoggerFactory.getLogger(AseguradoDAOImpl.class);
	
	private static SimpleDateFormat renderFechas = new SimpleDateFormat("dd/MM/yyyy");	
	
	@Override
	public List<Map<String,String>> validaEdadAsegurados(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem) throws Exception {
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("PV_CDUNIECO_I", cdunieco);
		params.put("PV_CDRAMO_I"  , cdramo);
		params.put("PV_ESTADO_I"  , estado);
		params.put("PV_NMPOLIZA_I", nmpoliza);
		params.put("PV_NMSUPLEM_I", nmsuplem);
		Map<String, Object> resultado = ejecutaSP(new ValidaEdadAseguradosSP(getDataSource()), params);
		return (List<Map<String, String>>) resultado.get("pv_registro_o");
	}
	
	protected class ValidaEdadAseguradosSP extends StoredProcedure {
		
		protected ValidaEdadAseguradosSP(DataSource dataSource) {
			
			super(dataSource, "PKG_CONSULTA.P_VALIDA_EDAD_ASEGURADOS");
            
			String[] cols = new String[]{"NOMBRE","PARENTESCO","EDAD","EDADMINI","EDADMAXI","SUPERAMINI","SUPERAMAXI"};
            
			declareParameter(new SqlParameter("PV_CDUNIECO_I"   , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("PV_CDRAMO_I"     , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("PV_ESTADO_I"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_NMPOLIZA_I"   , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("PV_NMSUPLEM_I"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new GenericMapper(cols)));
	        declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
    @Override
    public void agregarAsegurado(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem,
            String feefecto, String dsnombre, String dsnombre1, String paterno, String materno,
            String cdrfc, String sexo, String fenacimi, String cdestciv, String dsocupacion, String cdtipsit,
            String cdplan, String nmorddom, String cdagrupa, String otvalor01, String otvalor02, String otvalor03,
            String otvalor04, String otvalor05, String otvalor06, String otvalor07, String otvalor08, String otvalor09,
            String otvalor10) throws Exception {
        
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("pv_cdunieco_i", cdunieco);
        params.put("pv_cdramo_i"  , cdramo);
        params.put("pv_estado_i"  , estado);
        params.put("pv_nmpoliza_i", nmpoliza);
        params.put("pv_nmsuplem_i", nmsuplem);
        params.put("pv_feefecto_i", feefecto);
        params.put("pv_dsnombre_i", dsnombre);
        params.put("pv_dsnombre1_i", dsnombre1);
        params.put("pv_paterno_i" , paterno);
        params.put("pv_materno_i" , materno);
        params.put("pv_cdrfc_i"   , cdrfc);
        params.put("pv_sexo_i"    , sexo);
        params.put("pv_fenacimi_i", fenacimi);
        params.put("pv_cdestciv_i", cdestciv);
        params.put("pv_dsocupacion_i", dsocupacion);
        params.put("pv_cdtipsit_i", cdtipsit);
        params.put("pv_cdplan_i"  , cdplan);
        params.put("pv_nmorddom_i"  , nmorddom);
        params.put("pv_cdagrupa_i"  , cdagrupa);
        params.put("pv_otvalor01_i", otvalor01);
        params.put("pv_otvalor02_i", otvalor02);
        params.put("pv_otvalor03_i", otvalor03);
        params.put("pv_otvalor04_i", otvalor04);
        params.put("pv_otvalor05_i", otvalor05);
        params.put("pv_otvalor06_i", otvalor06);
        params.put("pv_otvalor07_i", otvalor07);
        params.put("pv_otvalor08_i", otvalor08);
        params.put("pv_otvalor09_i", otvalor09);
        params.put("pv_otvalor10_i", otvalor10);
        ejecutaSP(new AgregaAseguradoSP(getDataSource()), params);
    }
    
    protected class AgregaAseguradoSP extends StoredProcedure {
        
        protected AgregaAseguradoSP(DataSource dataSource) {

            super(dataSource, "P_AGREGA_ASEGURADO");
            declareParameter(new SqlParameter("pv_cdunieco_i"    , OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_cdramo_i"      , OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_estado_i"      , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i"    , OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_nmsuplem_i"    , OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_feefecto_i"    , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_dsnombre_i"    , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_dsnombre1_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_paterno_i"     , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_materno_i"     , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdrfc_i"       , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_sexo_i"        , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_fenacimi_i"    , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdestciv_i"    , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_dsocupacion_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdtipsit_i"    , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdplan_i"      , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmorddom_i"    , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdagrupa_i"    , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_otvalor01_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_otvalor02_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_otvalor03_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_otvalor04_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_otvalor05_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_otvalor06_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_otvalor07_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_otvalor08_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_otvalor09_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_otvalor10_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
        }
    }
	
}