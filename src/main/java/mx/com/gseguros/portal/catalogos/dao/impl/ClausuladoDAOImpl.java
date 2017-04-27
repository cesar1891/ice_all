package mx.com.gseguros.portal.catalogos.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.gseguros.portal.catalogos.dao.ClausuladoDAO;
import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.general.model.BaseVO;
import oracle.jdbc.OracleTypes;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class ClausuladoDAOImpl extends AbstractManagerDAO implements ClausuladoDAO {

	@Override
	public List<BaseVO> consultaClausulas(String cdclause, String dsclausu) throws Exception {
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdcla_i", cdclause);
		params.put("pv_descrip_i", dsclausu);
		
		Map<String, Object> resultado = ejecutaSP(new ObtieneClausulas(getDataSource()), params);
		
		return (List<BaseVO>) resultado.get("pv_registro_o");
	}

	@Override
	public BaseVO insertaClausula(String dsclausu, String contenido) throws Exception {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("pv_descrip_i", dsclausu);
		params.put("pv_conten_i", contenido);
		
		Map<String, Object> result = ejecutaSP(new InsertaClausula(getDataSource()), params);
		
		BaseVO baseVO = new BaseVO();
		baseVO.setKey(result.get("msg_id").toString());
		baseVO.setValue(result.get("msg_title").toString());
    	return baseVO;
	}

	@Override
	public BaseVO actualizaClausula(String cdclausu, String dsclausu, String contenido) throws Exception {
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdtipcla_i", cdclausu);
		params.put("pv_descrip_i", dsclausu);
		params.put("pv_conten_i", contenido);
		
		Map<String, Object> result = ejecutaSP(new ActualizaClausula(getDataSource()), params);
		
		BaseVO baseVO = new BaseVO();
		baseVO.setKey(result.get("msg_id").toString());
		baseVO.setValue(result.get("msg_title").toString());
    	return baseVO;
	}

	@Override
	public String consultaClausulaDetalle(String cdclausu) throws Exception {
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdcla_i", cdclausu);
		
		Map<String, Object> result = ejecutaSP(new ConsultaDetalleClausula(getDataSource()), params);
		
		return (String) result.get("vDslinea");
	}
	
	
    protected class ObtieneClausulas extends StoredProcedure {
    	
    	protected ObtieneClausulas(DataSource dataSource) {
    		super(dataSource, "PKG_CONSULTA.P_obtiene_clausulas");
    		
    		declareParameter(new SqlParameter("pv_cdcla_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_descrip_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ClausulaMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    protected class ClausulaMapper implements RowMapper {
    	
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		
    		BaseVO consulta = new BaseVO();
    		consulta.setKey(rs.getString("cdclausu"));
    		consulta.setValue(rs.getString("dsclausu"));
    		
    		return consulta;
    	}
    }

    protected class InsertaClausula extends StoredProcedure {
    	
    	protected InsertaClausula(DataSource dataSource) {
    		super(dataSource, "PKG_CONSULTA.P_INSERTA_CLAUSU");
    		
    		declareParameter(new SqlParameter("pv_descrip_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_conten_i", OracleTypes.CLOB));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }

    protected class ActualizaClausula extends StoredProcedure {
    	
    	protected ActualizaClausula(DataSource dataSource) {
    		super(dataSource, "PKG_CONSULTA.P_ACTUALIZA_CLAUSU");
    		
    		declareParameter(new SqlParameter("pv_cdtipcla_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_descrip_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_conten_i", OracleTypes.CLOB));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    protected class ConsultaDetalleClausula extends StoredProcedure {
    	
    	protected ConsultaDetalleClausula(DataSource dataSource) {
    		super(dataSource, "PKG_CONSULTA.P_obtiene_detalle_clausula");
    		
    		declareParameter(new SqlParameter("pv_cdcla_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("vDslinea", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }

}