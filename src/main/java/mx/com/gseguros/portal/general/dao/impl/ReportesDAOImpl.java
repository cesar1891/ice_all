package mx.com.gseguros.portal.general.dao.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.general.dao.ReportesDAO;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.model.ParamReporteVO;
import mx.com.gseguros.portal.general.model.ReporteVO;
import mx.com.gseguros.utils.Constantes;
import oracle.jdbc.OracleTypes;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.jdbc.support.lob.OracleLobHandler;

public class ReportesDAOImpl extends AbstractManagerDAO implements ReportesDAO {
	
	private static Logger logger = Logger.getLogger(ReportesDAOImpl.class);
    
	@SuppressWarnings("unchecked")
	@Override
	public List<ReporteVO> obtenerListaReportes() throws Exception {
    	Map<String, Object> result = ejecutaSP(new ObtieneListaReportesSP(this.getDataSource()), new HashMap<String, Object>());
		return (List<ReporteVO>)result.get("pv_registro_o");
    }
    
    protected class ObtieneListaReportesSP extends StoredProcedure {
    	protected ObtieneListaReportesSP(DataSource dataSource) {
            super(dataSource,"PKG_TAEXTRACCION.GET_REPORTES");
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ObtieneListaReportesMapper()));
            declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    protected class ObtieneListaReportesMapper implements RowMapper<ReporteVO> {
        public ReporteVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	ReporteVO reporte = new ReporteVO();
        	reporte.setCdReporte(rs.getString("CDREPORTE"));
        	reporte.setDsReporte(rs.getString("DSREPORTE"));
        	reporte.setCdPantalla(rs.getString("CDPANTALLA"));
        	reporte.setCdSeccion(rs.getString("CDSECCION"));
        	return reporte;
        }
    }
    
    
    @SuppressWarnings("unchecked")
	@Override
	public List<ComponenteVO> obtenerParametrosReporte(Map<String, Object> params) throws Exception {
    	Map<String, Object> result = ejecutaSP(new ObtieneParametrosReportesSP(this.getDataSource()), params);
		return (List<ComponenteVO>)result.get("pv_registro_o");
    }
    
    protected class ObtieneParametrosReportesSP extends StoredProcedure {
    	protected ObtieneParametrosReportesSP(DataSource dataSource) {
            super(dataSource,"PKG_TAEXTRACCION.GET_PARAMETROS");
            declareParameter(new SqlParameter("pv_cdreporte_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdpantalla_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdseccion_i", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ObtieneParametrosReportesMapper()));
            declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    protected class ObtieneParametrosReportesMapper implements RowMapper<ComponenteVO> {
		String llaveLabel       = "LABEL";
		String llaveTipoCampo   = "TIPOCAMPO";
		String llaveCatalogo    = "CATALOGO";
		String llaveDependiente = "SWDEPEND";
		String llaveMinLength   = "MINLENGTH";
		String llaveMaxLength   = "MAXLENGTH";
		String llaveObligatorio = "SWOBLIGA";
		String llaveColumna     = "SWCOLUMN";
		String llaveRenderer    = "RENDERER";
		String llaveName        = "NAME_CDATRIBU";
		String llaveSoloLectura = "SWLECTURA";
		String llaveQueryParam  = "QUERYPARAM";
		String llaveValue       = "VALUE";
		String llaveOculto      = "SWOCULTO";
		String llaveParam1      = "PARAM1";
		String llaveValue1      = "VALUE1";
		String llaveParam2      = "PARAM2";
		String llaveValue2      = "VALUE2";
		String llaveParam3      = "PARAM3";
		String llaveValue3      = "VALUE3";
		String llaveParam4      = "PARAM4";
		String llaveValue4      = "VALUE4";
		String llaveParam5      = "PARAM5";
		String llaveValue5      = "VALUE5";
		String llaveComboVacio  = "SWCVACIO";
		String llaveIcon        = "ICONO";
		String llaveHandler     = "HANDLER";
		
		public ComponenteVO mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			String  label        = rs.getString(llaveLabel);
			String  tipoCampo    = rs.getString(llaveTipoCampo);
			String  catalogo     = rs.getString(llaveCatalogo);
			
			String  sDependiente  = rs.getString(llaveDependiente);
			boolean isDependiente = false;
			if(StringUtils.isNotBlank(sDependiente)&&sDependiente.equalsIgnoreCase(Constantes.SI))
			{
				isDependiente = true;
			}
			
			String  sMinLength    = rs.getString(llaveMinLength);
			int     minLength     = -1;
			boolean flagMinLength = false;
			if(StringUtils.isNotBlank(sMinLength))
			{
				minLength     = (Integer)Integer.parseInt(sMinLength);
				flagMinLength = true;
			}
			
			String  sMaxLength    = rs.getString(llaveMaxLength);
			int     maxLength     = -1;
			boolean flagMaxLength = false;
			if(StringUtils.isNotBlank(sMaxLength))
			{
				maxLength     = (Integer)Integer.parseInt(sMaxLength);
				flagMaxLength = true;
			}
			
			String  sObligatorio  = rs.getString(llaveObligatorio);
			boolean isObligatorio = false;
			if(StringUtils.isNotBlank(sObligatorio)&&sObligatorio.equalsIgnoreCase(Constantes.SI))
			{
				isObligatorio = true;
			}
			
			String  columna  = rs.getString(llaveColumna);
			
			String sRenderer = rs.getString(llaveRenderer);
			String renderer  = null;
			if(StringUtils.isNotBlank(sRenderer))
			{
				if(sRenderer.equalsIgnoreCase(ComponenteVO.RENDERER_MONEY))
				{
					renderer = ComponenteVO.RENDERER_MONEY_EXT;
				}
				else if(!sRenderer.equalsIgnoreCase(Constantes.NO))
				{
					renderer = sRenderer;
				}
			}
			
			String  nameCdatribu = rs.getString(llaveName);
			boolean flagEsAtribu = false;
			if(StringUtils.isNotBlank(nameCdatribu))
			{
				flagEsAtribu = true;
				try
				{
					int aux = (Integer)Integer.parseInt(nameCdatribu);
				}
				catch(Exception ex)
				{
					flagEsAtribu = false;
				}
			}
			
			String sSoloLectura   = rs.getString(llaveSoloLectura);
			boolean isSoloLectura = false;
			if(StringUtils.isNotBlank(sSoloLectura)&&sSoloLectura.equalsIgnoreCase(Constantes.SI))
			{
				isSoloLectura = true;
			}
			
			String queryParam = rs.getString(llaveQueryParam);
			String value      = rs.getString(llaveValue);
			
			String  sOculto  = rs.getString(llaveOculto);
			boolean isOculto = false;
			if(StringUtils.isNotBlank(sOculto)&&sOculto.equalsIgnoreCase(Constantes.SI))
			{
				isOculto = true;
			}
			
			String  paramName1   = rs.getString(llaveParam1);
			String  paramValue1  = rs.getString(llaveValue1);
			String  paramName2   = rs.getString(llaveParam2);
			String  paramValue2  = rs.getString(llaveValue2);
			String  paramName3   = rs.getString(llaveParam3);
			String  paramValue3  = rs.getString(llaveValue3);
			String  paramName4   = rs.getString(llaveParam4);
			String  paramValue4  = rs.getString(llaveValue4);
			String  paramName5   = rs.getString(llaveParam5);
			String  paramValue5  = rs.getString(llaveValue5);
			
			String  sComboVacio  = rs.getString(llaveComboVacio);
			boolean isComboVacio = false;
			if(StringUtils.isNotBlank(sComboVacio)&&sComboVacio.equalsIgnoreCase(Constantes.SI))
			{
				isComboVacio = true;
			}
			
			String  icon    = rs.getString(llaveIcon);
			String  handler = rs.getString(llaveHandler);
			
			ComponenteVO comp = new ComponenteVO(
					ComponenteVO.TIPO_GENERICO,
					label         , tipoCampo     , catalogo,
					isDependiente , minLength     , flagMinLength,
					maxLength     , flagMaxLength , isObligatorio,
					columna       , renderer      , "params."+nameCdatribu,
					flagEsAtribu  , isSoloLectura , queryParam,
					value         , isOculto      , paramName1,
					paramValue1   , paramName2    , paramValue2,
					paramName3    , paramValue3   , paramName4,
					paramValue4   , paramName5    , paramValue5,
					isComboVacio  , icon          , handler,
					false
					);
			
			return comp;
		}
	}
    
    
	@Override
	public Map<String, Object> actualizarParametroReporte(String cdreporte, String username, ParamReporteVO paramReporteVO) throws Exception {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdreporte_i", cdreporte);
		params.put("pv_nomparam_i", paramReporteVO.getNombre());
		params.put("pv_valor_i", paramReporteVO.getValor());
		params.put("pv_usuario_i", username);
		
    	return ejecutaSP(new ActualizaParametroReporteSP(this.getDataSource()), params);
    }
    
    protected class ActualizaParametroReporteSP extends StoredProcedure {
    	protected ActualizaParametroReporteSP(DataSource dataSource) {
            super(dataSource,"PKG_TAEXTRACCION.PUT_PARAMETROS");
            declareParameter(new SqlParameter("pv_cdreporte_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nomparam_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_valor_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_usuario_i", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    
	@Override
	public void armarReporte(String cdreporte, String username) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdreporte_i", cdreporte);
		params.put("pv_usuario_i", username);
    	ejecutaSP(new ArmaReporteSP(this.getDataSource()), params);
    }
    
    protected class ArmaReporteSP extends StoredProcedure {
    	protected ArmaReporteSP(DataSource dataSource) {
            super(dataSource,"PKG_TAEXTRACCION.ARMA_REPORTE");
            declareParameter(new SqlParameter("pv_cdreporte_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_usuario_i", OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    
    @SuppressWarnings("unchecked")
	@Override
	public InputStream obtenerReporte(String cdreporte, String username) throws Exception {
    	
    	Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdreporte_i", cdreporte);
		params.put("pv_usuario_i", username);
		
		logger.debug("params=" + params);
		
		InputStream archivo =  null;
		try {
			Map<String, Object> resultado = ejecutaSP(new ObtieneReporteSP(getDataSource()), params);
			logger.debug("resultado:"+resultado);
			ArrayList<InputStream> inputList = (ArrayList<InputStream>) resultado.get("pv_registro_o");
			archivo = inputList.get(0);
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
		
		return archivo;
    }
        
    protected class ObtieneReporteSP extends StoredProcedure {
    	protected ObtieneReporteSP(DataSource dataSource) {
            super(dataSource,"PKG_TAEXTRACCION.GET_SALIDA");
            declareParameter(new SqlParameter("pv_cdreporte_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_usuario_i",   OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ObtieneReporteMapper()));
            declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    protected class ObtieneReporteMapper implements RowMapper<InputStream> {
    	public InputStream mapRow(ResultSet rs, int rowNum) throws SQLException {
        	OracleLobHandler lobHandler = new OracleLobHandler();
            return new ByteArrayInputStream(lobHandler.getBlobAsBytes(rs, "DATA"));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public InputStream exportaTablaApoyo(Map<String, String> params) throws Exception {
    	
    	InputStream archivo =  null;
    	try {
    		Map<String, Object> resultado = ejecutaSP(new ExportaTablaApoyo(getDataSource()), params);
    		logger.debug("resultado:"+resultado);
    		ArrayList<InputStream> inputList = (ArrayList<InputStream>) resultado.get("pv_registro_o");
    		archivo = inputList.get(0);
    	} catch (Exception e) {
    		throw new Exception(e.getMessage(), e);
    	}
    	
    	return archivo;
    }
    
    protected class ExportaTablaApoyo extends StoredProcedure {
    	protected ExportaTablaApoyo(DataSource dataSource) {
    		super(dataSource,"PKG_TABAPOYO.P_EXPORTA_TABLA_APOYO");
    		declareParameter(new SqlParameter("pv_cdreporte_i"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_usuario_i"     , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_cdtabla"       , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new ExportaTablaApoyoMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    protected class ExportaTablaApoyoMapper implements RowMapper<InputStream> {
    	public InputStream mapRow(ResultSet rs, int rowNum) throws SQLException {
    		OracleLobHandler lobHandler = new OracleLobHandler();
    		return new ByteArrayInputStream(lobHandler.getBlobAsBytes(rs, "DATA"));
    	}
    }
    
}