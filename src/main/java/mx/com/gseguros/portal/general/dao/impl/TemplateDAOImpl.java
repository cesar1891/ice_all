package mx.com.gseguros.portal.general.dao.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.dao.impl.GenericMapper;
import mx.com.gseguros.portal.general.dao.TemplateDAO;
import oracle.jdbc.OracleTypes;

public class TemplateDAOImpl extends AbstractManagerDAO implements TemplateDAO {

    @Override
    public void guardarDatosPoliza(String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("pv_cdunieco_i" , cdunieco);
        params.put("pv_cdramo_i"   , cdramo);
        params.put("pv_estado_i"   , estado);
        params.put("pv_nmpoliza_i" , nmpoliza);
        ejecutaSP(new GuardarDatosPolizaSP(getDataSource()), params);
    }
    
    protected class GuardarDatosPolizaSP extends StoredProcedure {
        protected GuardarDatosPolizaSP (DataSource dataSource) {
            super(dataSource, "PKG_TEMPLATE.P_GUARDA_DATOS_POLIZA");
            declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.NUMBER));
            declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.NUMBER));
            declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.NUMBER));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
        }
    }

    @Override
    public List<Map<String, String>> recuperarAsegurados(String cdunieco, String cdramo, String estado, String nmpoliza,
            String nmsuplem) throws Exception {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("pv_cdunieco_i" , cdunieco);
        params.put("pv_cdramo_i"   , cdramo);
        params.put("pv_estado_i"   , estado);
        params.put("pv_nmpoliza_i" , nmpoliza);
        params.put("pv_nmsuplem_i" , nmsuplem);
        Map<String, Object> respuesta = ejecutaSP(new RecuperarAseguradosSP(getDataSource()), params);
        return (List<Map<String, String>>)respuesta.get("pv_registro_o");
    }
    
    protected class RecuperarAseguradosSP extends StoredProcedure {
        protected RecuperarAseguradosSP (DataSource dataSource) {
            super(dataSource, "PKG_TEMPLATE.P_GET_ASEGURADOS_POLIZA");
            declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.NUMBER));
            declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.NUMBER));
            declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.NUMBER));
            declareParameter(new SqlParameter("pv_nmsuplem_i" , OracleTypes.NUMBER));
            String[] columnas = new String[] {
                    "CDUNIECO", "CDRAMO", "ESTADO", "NMPOLIZA", "NMSITUAC", "NMSUPLEM", "NOMBRE", "SEXO", "FENACIMI"
            };
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(columnas)));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
        }
    }
}