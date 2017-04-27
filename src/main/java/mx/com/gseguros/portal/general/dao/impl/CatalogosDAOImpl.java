package mx.com.gseguros.portal.general.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.dao.impl.GenericMapper;
import mx.com.gseguros.portal.general.dao.CatalogosDAO;
import mx.com.gseguros.portal.general.util.Rango;
import mx.com.gseguros.portal.general.util.TipoTramite;
import mx.com.gseguros.portal.general.util.Validacion;
import mx.com.gseguros.utils.Utils;
import oracle.jdbc.OracleTypes;

public class CatalogosDAOImpl extends AbstractManagerDAO implements CatalogosDAO {

	protected static final transient Logger logger = LoggerFactory.getLogger(CatalogosDAOImpl.class);
	
	@Override
	public List<GenericVO> obtieneTmanteni(String cdTabla) throws Exception{
		
		try {
			Map<String,Object> params=new LinkedHashMap<String,Object>(0);
			params.put("pv_cdtabla", cdTabla);
			Map<String, Object> resultado = ejecutaSP(new ObtenerTmanteni(getDataSource()), params);
			return (List<GenericVO>) resultado.get("pv_registro_o");
			
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
	}
	
	protected class ObtenerTmanteni extends StoredProcedure {
    	protected ObtenerTmanteni(DataSource dataSource) {
            super(dataSource,"PKG_LISTAS.P_GET_TMANTENI");
            declareParameter(new SqlParameter("pv_cdtabla",       OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ObtenerTmanteniMapper()));
            declareParameter(new SqlOutParameter("pv_messages_o", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    protected class ObtenerTmanteniMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            GenericVO generic=new GenericVO();
            generic.setKey(rs.getString("CODIGO"));
            generic.setValue(rs.getString("DESCRIPC"));
            return generic;
        }
    }
    
    
    public List<GenericVO> obtieneColonias(String codigoPostal) throws Exception {
    	try {
    		HashMap<String,Object> params =  new LinkedHashMap<String, Object>();
    		params.put("pv_codpostal_i", codigoPostal);
    		
    		Map<String, Object> resultado = ejecutaSP(new ObtenCatalogoColonias(getDataSource()), params);
    		return (List<GenericVO>) resultado.get("pv_registro_o");
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
    }
    
    protected class ObtenCatalogoColonias extends StoredProcedure {
    	
    	protected ObtenCatalogoColonias(DataSource dataSource) {
    		super(dataSource, "PKG_LISTAS.P_GET_COLONIAS");
    		
    		declareParameter(new SqlParameter("pv_codpostal_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ColoniasMapper()));
    		declareParameter(new SqlOutParameter("pv_messages_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    protected class ColoniasMapper  implements RowMapper {
    	
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		GenericVO base = new GenericVO();	    
    		base.setKey(rs.getString("CODIGO"));
    		base.setValue(rs.getString("NOMBRE"));
    		return base;
    	}
    }

    public List<GenericVO> obtieneMunicipios(String cdEstado) throws Exception {
    	try {
    		HashMap<String,Object> params =  new LinkedHashMap<String, Object>();
    		params.put("pv_estado_i", cdEstado);
    		
    		Map<String, Object> resultado = ejecutaSP(new ObtieneMunicipios(getDataSource()), params);
    		return (List<GenericVO>) resultado.get("pv_registro_o");
    	} catch (Exception e) {
    		throw new Exception(e.getMessage(), e);
    	}
    }
    
    protected class ObtieneMunicipios extends StoredProcedure {
    	
    	protected ObtieneMunicipios(DataSource dataSource) {
    		super(dataSource, "PKG_LISTAS.P_GET_MUNICIPIOS_X_EDO");
    		
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new MunicipiosMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    protected class MunicipiosMapper  implements RowMapper {
    	
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		GenericVO base = new GenericVO();	    
    		base.setKey(rs.getString("CODIGO"));
    		base.setValue(rs.getString("DESCRIPCION"));
    		return base;
    	}
    }

    public List<GenericVO> obtieneZonasPorModalidad(String cdtipsit) throws Exception {
    	try {
    		HashMap<String,Object> params =  new LinkedHashMap<String, Object>();
    		params.put("pv_cdtipsit_i", cdtipsit);
    		
    		Map<String, Object> resultado = ejecutaSP(new ObtieneZonasPorModalidad(getDataSource()), params);
    		return (List<GenericVO>) resultado.get("pv_registro_o");
    	} catch (Exception e) {
    		throw new Exception(e.getMessage(), e);
    	}
    }
    
    protected class ObtieneZonasPorModalidad extends StoredProcedure {
    	
    	protected ObtieneZonasPorModalidad(DataSource dataSource) {
    		super(dataSource, "PKG_LISTAS.___");
    		
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ZonasMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    protected class ZonasMapper  implements RowMapper {
    	
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		GenericVO base = new GenericVO();	    
    		base.setKey(rs.getString("CODIGO"));
    		base.setValue(rs.getString("DESCRIPCION"));
    		return base;
    	}
    }

    
	@Override
    public List<GenericVO> obtieneAtributosSituacion(String cdAtribu, String cdTipSit, String otValor, String cdSisRol) throws Exception{
		try{
			HashMap<String,Object> params = new LinkedHashMap<String,Object>();
			params.put("pv_cdtipsit_i", cdTipSit);
			params.put("pv_cdatribu_i", cdAtribu);
			params.put("pv_otvalor_i", otValor);
			params.put("pv_cdsisrol_i", cdSisRol);
			Map<String, Object> resultado = ejecutaSP(new ObtieneAtributosSitSP(getDataSource()), params);
			return (List<GenericVO>) resultado.get("pv_registro_o");
		}catch (Exception e){
			throw new Exception(e.getMessage(),e);
		}
	}
	
	protected class ObtieneAtributosSitSP extends StoredProcedure {

        protected ObtieneAtributosSitSP(DataSource dataSource) {
            super(dataSource, "PKG_LISTAS.P_GET_ATRIBUTOS_SIT");
            declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdatribu_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_otvalor_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdsisrol_i", OracleTypes.VARCHAR));
            
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ObtieneAtributosSitMapper()));
            declareParameter(new SqlOutParameter("pv_messages_o", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
            compile();
        }
    }
	
    protected class ObtieneAtributosSitMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new GenericVO(rs.getString("CODIGO"),rs.getString("DESCRIPCION"));
        }
    }
	
	
	@Override
	public List<GenericVO> obtieneAtributosSiniestro(String cdAtribu, String cdTipSit, String otValor) throws Exception {
		try {
    		HashMap<String,Object> params = new LinkedHashMap<String,Object>();
            params.put("pv_cdatribu_i",cdAtribu);
            params.put("pv_cdtipsit_i",cdTipSit);
            params.put("pv_otvalor_i",otValor);
    		
    		Map<String, Object> resultado = ejecutaSP(new ObtieneAtributosSin(getDataSource()), params);
    		return (List<GenericVO>) resultado.get("pv_registro_o");
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
	}
    
    protected class ObtieneAtributosSin extends StoredProcedure {

        protected ObtieneAtributosSin(DataSource dataSource) {
            super(dataSource, "PKG_LISTAS.P_GET_ATRIBUTOS_SIN");
            declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdatribu_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_otvalor_i", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ObtieneAtributosSinMapper()));
            declareParameter(new SqlOutParameter("pv_messages_o", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
            compile();
        }
    }

    protected class ObtieneAtributosSinMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new GenericVO(rs.getString("CODIGO"),rs.getString("DESCRIPCION"));
        }
    }

	@Override
	public List<GenericVO> obtieneAtributosPoliza(String cdAtribu, String cdRamo, String otValor) throws Exception {
		
		try {
			HashMap<String,Object> params = new LinkedHashMap<String,Object>();
			params.put("pv_cdatribu_i", cdAtribu);
			params.put("pv_cdramo_i", cdRamo);
			params.put("pv_otvalor_i", otValor);
    		
    		Map<String, Object> resultado = ejecutaSP(new ObtieneAtributosPol(getDataSource()), params);
    		return (List<GenericVO>) resultado.get("pv_registro_o");
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
	}
	
	protected class ObtieneAtributosPol extends StoredProcedure {
	
		protected ObtieneAtributosPol(DataSource dataSource) {
			super(dataSource, "PKG_LISTAS.P_GET_ATRIBUTOS_POL");
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdatribu_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ObtieneAtributosPolMapper()));
			declareParameter(new SqlOutParameter("pv_messages_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	protected class ObtieneAtributosPolMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new GenericVO(rs.getString("CODIGO"),rs.getString("DESCRIPCION"));
		}
	}
	

	@Override
	public List<GenericVO> obtieneAtributosGarantia(String cdAtribu, String cdTipSit, String cdRamo, String valAnt, String cdGarant, String cdSisrol)
			throws Exception {
		try {
			HashMap<String,Object> params = new LinkedHashMap<String,Object>();
			params.put("pv_cdramo_i"  ,cdRamo);
			params.put("pv_cdtipsit_i",cdTipSit);
			params.put("pv_cdgarant_i",cdGarant);
			params.put("pv_cdatribu_i",cdAtribu);
			params.put("pv_otvalor_i" ,valAnt);
			params.put("pv_cdsisrol_i", cdSisrol); // se agrega par�metro para considerar restricciones por rol (EGS)
    		
    		Map<String, Object> resultado = ejecutaSP(new ObtieneAtributosGar(getDataSource()), params);
    		return (List<GenericVO>) resultado.get("pv_registro_o");
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
	}
	
	protected class ObtieneAtributosGar extends StoredProcedure {
		
		protected ObtieneAtributosGar(DataSource dataSource) {
			super(dataSource, "PKG_LISTAS.P_GET_ATRIBUTOS_GAR");
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdgarant_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdatribu_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdsisrol_i", OracleTypes.VARCHAR)); // se agrega par�metro para considerar restricciones por rol (ESG)
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ObtieneAtributosGarMapper()));
			declareParameter(new SqlOutParameter("pv_messages_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}

	protected class ObtieneAtributosGarMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new GenericVO(rs.getString("CODIGO"),rs.getString("DESCRIPCION"));
		}
	}

	
	@Override
	public List<GenericVO> obtieneAtributosRol(String cdAtribu,
			String cdTipSit, String cdRamo, String valAnt, String cdRol)
			throws Exception {
		try {
			HashMap<String,Object> params = new LinkedHashMap<String,Object>();
			params.put("pv_cdramo_i", cdRamo);
			params.put("pv_cdrol_i", cdRol);
			params.put("pv_cdatribu_i", cdAtribu);
			params.put("pv_cdtipsit_i", cdTipSit);
			params.put("pv_otvalor_i", valAnt);
    		
    		Map<String, Object> resultado = ejecutaSP(new ObtieneAtributosRol(getDataSource()), params);
    		return (List<GenericVO>) resultado.get("pv_registro_o");
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
	}
    

	protected class ObtieneAtributosRol extends StoredProcedure {
	
		protected ObtieneAtributosRol(DataSource dataSource) {
			super(dataSource, "PKG_LISTAS.P_GET_ATRIBUTOS_ROL");
			declareParameter(new SqlParameter("pv_cdramo_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdrol_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdatribu_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor_i",  OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ObtieneAtributosRolMapper()));
			declareParameter(new SqlOutParameter("pv_messages_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	protected class ObtieneAtributosRolMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new GenericVO(rs.getString("CODIGO"),rs.getString("DESCRIPCION"));
		}
	}


	@Override
	public List<GenericVO> obtieneRolesSistema(String dsRol) throws Exception {
		HashMap<String,Object> params =  new LinkedHashMap<String, Object>();
		params.put("pv_dssysrol_i", dsRol);
		Map<String, Object> resultado = ejecutaSP(new ObtieneRolesSistema(getDataSource()), params);
		return (List<GenericVO>) resultado.get("PV_REGISTRO_O");
	}
	
	protected class ObtieneRolesSistema extends StoredProcedure {
		protected ObtieneRolesSistema(DataSource dataSource) {
			super(dataSource, "PKG_GENERA_USUARIO.P_BUSCA_ROL");
			declareParameter(new SqlParameter("pv_dssysrol_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new ObtieneRolesSistemaMapper()));
			declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	protected class ObtieneRolesSistemaMapper implements RowMapper {
		
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		return new GenericVO(rs.getString("CDSISROL"), rs.getString("DSSISROL"));
    	}
    }


	@Override
	public List<GenericVO> obtieneAgentes(String claveONombre) throws Exception {
		try {
			HashMap<String,Object> params =  new LinkedHashMap<String, Object>();
			params.put("pv_nombre_i", claveONombre);
    		
    		Map<String, Object> resultado = ejecutaSP(new ObtieneAgentes(getDataSource()), params);
    		return (List<GenericVO>) resultado.get("pv_registro_o");
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
	}
	
	protected class ObtieneAgentes extends StoredProcedure
	{
		protected ObtieneAgentes(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_OBTIENE_AGENTES");
			declareParameter(new SqlParameter("pv_nombre_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o",   OracleTypes.CURSOR, new ObtenerAgentesMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
			compile();
		}
	}
	
	protected class ObtenerAgentesMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException{
			GenericVO generic=new GenericVO();
            generic.setKey(rs.getString("CDAGENTE"));
            generic.setValue(rs.getString("Nombre_agente"));
            return generic;
		}
	}
	
	@Override
	public List<GenericVO> obtieneStatusTramite(Map<String,String> params) throws Exception
	{
		Map<String, Object> resultado = ejecutaSP(new ObtieneStatusTramite(getDataSource()), params);
		return (List<GenericVO>) resultado.get("pv_registro_o");
	}
	
	protected class ObtieneStatusTramite extends StoredProcedure
	{
    	protected ObtieneStatusTramite(DataSource dataSource)
    	{
            super(dataSource,"PKG_CONSULTA.P_GET_STATUS_TRAMITE");
            declareParameter(new SqlParameter(   "pv_cdtiptra_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new ObtenerTmanteniMapper()));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	@Override
	public List<GenericVO> obtieneSucursales(String cdunieco,String cdusuari) throws Exception {
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("pv_suc_admon_i" , cdunieco);
		params.put("pv_cdusuari_i"  , cdusuari);
		Map<String, Object> resultado = ejecutaSP(new ObtieneSucursales(getDataSource()), params);
		return (List<GenericVO>) resultado.get("pv_registro_o");
	}
	
	protected class ObtieneSucursales extends StoredProcedure {
    	protected ObtieneSucursales(DataSource dataSource) {
            super(dataSource,"PKG_LISTAS.P_GET_SUCURSALES");
            declareParameter(new SqlParameter("pv_suc_admon_i" , OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_cdusuari_i"  , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new ObtieneSucursalesMapper()));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	protected class ObtieneSucursalesMapper implements RowMapper<GenericVO> {
		public GenericVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new GenericVO(rs.getString("CODIGO"),rs.getString("DESCRIPC"));
		}
	}

	
	@Override
	public String obtieneCantidadMaxima(String cdramo, String cdtipsit, TipoTramite tipoTramite, Rango rango, Validacion validacion) throws Exception {
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("pv_cdramo_i"   , cdramo);
		params.put("pv_cdtipsit_i" , cdtipsit);
		params.put("pv_cdtiptra_i" , tipoTramite.getCdtiptra());
		params.put("pv_tipocant_i" , rango.getClave());
		params.put("pv_numval_i"   , validacion.getClave());
		Map<String, Object> resultado = ejecutaSP(new ObtieneCantidadMaxima(getDataSource()), params);
		return (String) resultado.get("pv_cantidad_o");
	}
	
    protected class ObtieneCantidadMaxima extends StoredProcedure {
    	
    	protected ObtieneCantidadMaxima(DataSource dataSource) {
    		
    		super(dataSource, "PKG_CONSULTA.P_GET_CANTIDAD_MAXIMA");
    		declareParameter(new SqlParameter("pv_cdramo_i",   OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtiptra_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_tipocant_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_numval_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_cantidad_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    @Override
    public List<GenericVO> cargarAgentesPorPromotor(Map<String,String> params) throws Exception
    {
    	Map<String,Object>respuestaProcedure=ejecutaSP(new CargarAgentesPorPromotor(getDataSource()), params);
    	List<GenericVO>listaResp=new ArrayList<GenericVO>();
    	List<Map<String,String>>lista=(List<Map<String,String>>)respuestaProcedure.get("pv_registro_o");
    	if(lista!=null&&lista.size()>0)
    	{
    		for(Map<String,String>iAgente:lista)
    		{
    			listaResp.add(new GenericVO(iAgente.get("CDAGENTE"),iAgente.get("DSAGENTE")));
    		}
    	}
    	return listaResp;
    }
	
    protected class CargarAgentesPorPromotor extends StoredProcedure {
    	
    	protected CargarAgentesPorPromotor(DataSource dataSource) {
    		
    		super(dataSource, "PKG_CONSULTA.P_GET_AGENTES_X_PROMOTOR");
			declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CDAGENTE" , "DSAGENTE"
			};
    		declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    @Override
    public List<GenericVO> cargarServicioPublicoAutos(Map<String,String> params) throws Exception
    {
    	Map<String,Object>respuestaProcedure=ejecutaSP(new CargarServicioPublicoAutos(getDataSource()), params);
    	List<Map<String,String>>lista=(List<Map<String,String>>)respuestaProcedure.get("pv_registro_o");
    	List<GenericVO>listaGen=new ArrayList<GenericVO>();
    	if(lista==null)
    	{
    		lista=new ArrayList<Map<String,String>>();
    	}
    	for(Map<String,String>iAuto:lista)
    	{
    		listaGen.add(new GenericVO(iAuto.get("CLAVEGS"),
    				new StringBuilder()
    		        .append(iAuto.get("CLAVEGS")).append(" - ")
    		        .append(iAuto.get("TIPUNI")).append(" - ")
    		        .append(iAuto.get("MARCA")).append(" - ")
    		        .append(iAuto.get("SUBMARCA")).append(" - ")
    		        .append(iAuto.get("MODELO")).append(" - ")
    		        .append(iAuto.get("VERSION"))
    		        .toString()
    		        ));
    	}
    	return listaGen;
    }
	
    protected class CargarServicioPublicoAutos extends StoredProcedure {
    	
    	protected CargarServicioPublicoAutos(DataSource dataSource) {
    		
    		super(dataSource, "PKG_CONSULTA.P_GET_DESC_VEHICULOS");
			declareParameter(new SqlParameter("cdramo"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("substring" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit"  , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"TIPUNI"
					,"MARCA"
					,"SUBMARCA"
					,"MODELO"
					,"VERSION"
					,"CLAVEGS"
			};
    		declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    @Override
	public String agregaCodigoPostal(Map<String,String>params)throws Exception
	{
		Map<String,Object>resultado=ejecutaSP(new AgregaCodigoPostal(getDataSource()), params);
		return (String)resultado.get("pv_msg_id_o");
	}
	
	protected class AgregaCodigoPostal extends StoredProcedure
	{
		protected AgregaCodigoPostal(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES.P_MOV_CODIGO_POSTAL");
			declareParameter(new SqlParameter("pv_cdpostal_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdmunici_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public String asociaZonaCodigoPostal(Map<String,String>params)throws Exception
	{
		Map<String,Object>resultado=ejecutaSP(new AsociaZonaCodigoPostal(getDataSource()), params);
		return (String)resultado.get("pv_msg_id_o");
	}
	
	protected class AsociaZonaCodigoPostal extends StoredProcedure
	{
		protected AsociaZonaCodigoPostal(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES.P_ASOCIA_ZONA_A_CODIPOS");
			declareParameter(new SqlParameter("pv_cdpostal_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cvezona_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO> cargarDescuentosPorAgente(
    		String tipoUnidad
    		,String uso
    		,String zona
    		,String promotoria
    		,String cdagente
    		,String cdtipsit
    		,String cdatribu)throws Exception
    {
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("tipoUnidad" , tipoUnidad);
		params.put("uso"        , uso);
		params.put("zona"       , zona);
		params.put("promotoria" , promotoria);
		params.put("cdagente"   , cdagente);
		params.put("cdtipsit"   , cdtipsit);
		params.put("cdatribu"   , cdatribu);
		Map<String,Object>procedureResult = ejecutaSP(new CargarDescuentosPorAgente(getDataSource()),params);
		List<Map<String,String>>lista     = (List<Map<String,String>>)procedureResult.get("pv_registro_o");
		List<GenericVO>listaGen           = new ArrayList<GenericVO>();
		if(lista!=null)
		{
			for(Map<String,String>it:lista)
			{
				listaGen.add(new GenericVO(it.get("codigo"),it.get("descripcion")));
			}
		}
		return listaGen;
    }
	
	protected class CargarDescuentosPorAgente extends StoredProcedure
	{
		protected CargarDescuentosPorAgente(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES.P_VALIDA_DESCUENTO_COMERCIAL");
			declareParameter(new SqlParameter("tipoUnidad" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("uso"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("zona"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("promotoria" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdagente"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdatribu"   , OracleTypes.VARCHAR));
			String[] cols=new String[]
					{
					"codigo"
					,"descripcion"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO>cargarListaNegocioServicioPublico(
			String cdtipsit
			,String cdatribu
			,String tipoUnidad
			,String cdagente)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdtipsit"   , cdtipsit);
		params.put("cdatribu"   , cdatribu);
		params.put("tipoUnidad" , tipoUnidad);
		params.put("cdagente"   , cdagente);
		Map<String,Object>procResult     = ejecutaSP(new CargarListaNegocioServicioPublico(getDataSource()),params);
		List<GenericVO>lista             = new ArrayList<GenericVO>();
		List<Map<String,String>>listaAux = (List<Map<String,String>>)procResult.get("pv_registro_o");
		logger.debug(
				new StringBuilder()
				.append("\n********************************************")
				.append("\n****** ...P_GET_ATRIBUTOS_NEG ******")
				.append("\n****** params=").append(params)
				.append("\n****** registro=").append(listaAux)
				.append("\n********************************************")
				.toString()
				);
		if(listaAux==null)
		{
			//lista.add(new GenericVO("0","TRADICIONAL"));
		}
		else
		{
			for(Map<String,String>elem:listaAux)
			{
				lista.add(new GenericVO(elem.get("codigo"),elem.get("descripcion")));
			}
		}
		return lista;
	}
	
	protected class CargarListaNegocioServicioPublico extends StoredProcedure
	{
		protected CargarListaNegocioServicioPublico(DataSource dataSource)
		{
			super(dataSource,"PKG_LISTAS.P_GET_ATRIBUTOS_NEG");
			declareParameter(new SqlParameter("cdtipsit"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdatribu"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipoUnidad" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdagente"   , OracleTypes.VARCHAR));
			String[] cols=new String[]
					{
					"codigo"
					,"descripcion"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO>cargarModelosPorSubmarcaRamo5(String submarca,String cdtipsit)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("submarca" , submarca);
		params.put("cdtipsit" , cdtipsit);
		Map<String,Object>procResult=ejecutaSP(new CargarModelosPorSubmarcaRamo5(getDataSource()),params);
		List<Map<String,String>>lista=(List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		List<GenericVO>listaGeneric=new ArrayList<GenericVO>();
		for(Map<String,String>modelo:lista)
		{
			listaGeneric.add(new GenericVO(modelo.get("anno"),modelo.get("anno")));
		}
		logger.debug(
				new StringBuilder()
				.append("\n*******************************************")
				.append("\n****** registro=").append(listaGeneric)
				.append("\n****** ...P_RECUPERA_MODELOS ******")
				.append("\n*******************************************")
				.toString()
				);
		return listaGeneric;
	}
	
	protected class CargarModelosPorSubmarcaRamo5 extends StoredProcedure
	{
		protected CargarModelosPorSubmarcaRamo5(DataSource dataSource)
		{
			super(dataSource,"PKG_LISTAS.P_RECUPERA_MODELOS");
			declareParameter(new SqlParameter("submarca" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			String[] cols=new String[]
					{
					"anno"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO>cargarVersionesPorModeloSubmarcaRamo5(String submarca,String modelo,String cdtipsit)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("submarca" , submarca);
		params.put("modelo"   , modelo);
		params.put("cdtipsit" , cdtipsit);
		Map<String,Object>procResult=ejecutaSP(new CargarVersionesPorModeloSubmarcaRamo5(getDataSource()),params);
		List<Map<String,String>>lista=(List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		List<GenericVO>listaGeneric=new ArrayList<GenericVO>();
		for(Map<String,String>descripcion:lista)
		{
			listaGeneric.add(new GenericVO(descripcion.get("codigo"),descripcion.get("descripcion")));
		}
		logger.debug(
				new StringBuilder()
				.append("\n*************************************************")
				.append("\n****** registro=").append(listaGeneric)
				.append("\n****** ...P_RECUPERA_DESCRIPCIONES ******")
				.append("\n*************************************************")
				.toString()
				);
		return listaGeneric;
	}
	
	protected class CargarVersionesPorModeloSubmarcaRamo5 extends StoredProcedure
	{
		protected CargarVersionesPorModeloSubmarcaRamo5(DataSource dataSource)
		{
			super(dataSource,"PKG_LISTAS.P_RECUPERA_DESCRIPCIONES");
			declareParameter(new SqlParameter("submarca" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("modelo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			String[] cols=new String[]
					{
					"codigo"
					,"descripcion"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO>cargarAutosPorCadenaRamo5(
			String cadena
			,String cdtipsit
			,String servicio
			,String uso
			)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cadena"   , cadena);
		params.put("cdtipsit" , cdtipsit);
		params.put("servicio" , servicio);
		params.put("uso"      , uso);
		Map<String,Object>procResult=ejecutaSP(new CargarAutosPorCadenaRamo5(getDataSource()),params);
		List<Map<String,String>>lista=(List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		logger.debug(Utils.log("****** ...P_GET_VEHICULOS_RAMO_5",lista));
		List<GenericVO>listaGeneric=new ArrayList<GenericVO>();
		for(Map<String,String>descripcion:lista)
		{
			listaGeneric.add(
					new GenericVO(
							descripcion.get("clave")
							,new StringBuilder(descripcion.get("clave"))
							.append(" - ")
							.append(descripcion.get("marca"))
							.append(" - ")
							.append(descripcion.get("submarca"))
							.append(" - ")
							.append(descripcion.get("modelo"))
							.append(" - ")
							.append(descripcion.get("descripcion"))
							.toString()
							,descripcion.get("tipovehi")
					));
		}
		logger.debug(
				new StringBuilder()
				.append("\n*************************************************")
				.append("\n****** registro=").append(listaGeneric)
				.append("\n****** ...P_GET_VEHICULOS_RAMO_5 ******")
				.append("\n*************************************************")
				.toString()
				);
		return listaGeneric;
	}
	
	protected class CargarAutosPorCadenaRamo5 extends StoredProcedure
	{
		protected CargarAutosPorCadenaRamo5(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_VEHICULOS_RAMO_5");
			declareParameter(new SqlParameter("cadena"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("servicio" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("uso"      , OracleTypes.VARCHAR));
			String[] cols=new String[]
					{
					"clave"
					,"marca"
					,"submarca"
					,"descripcion"
					,"modelo"
					,"clave_marca"
					,"clave_submarca"
					,"tipovehi"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO>cargarTtapvat1(String cdtabla)throws ApplicationException,Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdtabla" , cdtabla);
		Map<String,Object>procResult  = ejecutaSP(new CargarTtapvat1(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new ApplicationException("No hay datos en la tabla de apoyo");
		}
		List<GenericVO>listaGen = new ArrayList<GenericVO>();
		for(Map<String,String>item:lista)
		{
			listaGen.add(new GenericVO(item.get("OTCLAVE"),item.get("OTVALOR"),item.get("RELACION")));
		}
		return listaGen;
	}
	
	protected class CargarTtapvat1 extends StoredProcedure
	{
		protected CargarTtapvat1(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_TTAPVAT1");
			declareParameter(new SqlParameter("cdtabla" , OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"CDRELACION"
					,"RELACION"
					,"NMTABLA_EQUAL"
					,"CDPADRE"
					,"DSPADRE"
					,"CDHIJO"
					,"NMTABLA"
					,"OTCLAVE"
					,"OTVALOR"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO>cargarNegocioPorCdtipsitRamo5(String cdtipsit)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdtipsit",cdtipsit);
		Map<String,Object>procResult  = ejecutaSP(new CargarNegocioPorCdtipsitRamo5(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new ApplicationException("No hay datos de negocio por situacion de riesgo");
		}
		List<GenericVO>listaGen = new ArrayList<GenericVO>();
		for(Map<String,String>item:lista)
		{
			listaGen.add(new GenericVO(item.get("clave"),item.get("valor")));
		}
		return listaGen;
	}
	
	protected class CargarNegocioPorCdtipsitRamo5 extends StoredProcedure
	{
		protected CargarNegocioPorCdtipsitRamo5(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_NEGOCIO_X_CDTIPSIT");
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			String[] cols=new String[]{ "clave" , "valor" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO>cargarUsosPorNegocioRamo5(String cdnegocio,String cdtipsit,String servicio,String tipocot)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdnegocio" , cdnegocio);
		params.put("cdtipsit"  , cdtipsit);
		params.put("servicio"  , servicio);
		params.put("tipocot"   , tipocot);
		Map<String,Object>procResult  = ejecutaSP(new CargarUsosPorNegocioRamo5(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new ApplicationException("No hay datos de uso por negocio");
		}
		List<GenericVO>listaGen = new ArrayList<GenericVO>();
		for(Map<String,String>item:lista)
		{
			listaGen.add(new GenericVO(item.get("clave"),item.get("valor")));
		}
		return listaGen;
	}
	
	protected class CargarUsosPorNegocioRamo5 extends StoredProcedure
	{
		protected CargarUsosPorNegocioRamo5(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_USOS_X_NEGOCIO");
			declareParameter(new SqlParameter("cdnegocio" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("servicio"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipocot"   , OracleTypes.VARCHAR));
			String[] cols=new String[]{ "clave" , "valor" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO>cargarMarcasPorNegocioRamo5(String cdnegocio,String cdtipsit)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdnegocio" , cdnegocio);
		params.put("cdtipsit"  , cdtipsit);
		Map<String,Object>procResult  = ejecutaSP(new CargarMarcasPorNegocioRamo5(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new ApplicationException("No hay datos de uso por negocio");
		}
		List<GenericVO>listaGen = new ArrayList<GenericVO>();
		for(Map<String,String>item:lista)
		{
			listaGen.add(new GenericVO(item.get("clave"),item.get("valor")));
		}
		return listaGen;
	}
	
	protected class CargarMarcasPorNegocioRamo5 extends StoredProcedure
	{
		protected CargarMarcasPorNegocioRamo5(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_MARCAS_X_NEGOCIO");
			declareParameter(new SqlParameter("cdnegocio" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit"  , OracleTypes.VARCHAR));
			String[] cols=new String[]{ "clave" , "valor" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO>cargarNegociosPorAgenteRamo5(
			String cdagente
			,String cdsisrol
			,String tipoflot
			)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdagente" , cdagente);
		params.put("cdsisrol" , cdsisrol);
		params.put("tipoflot" , tipoflot);
		Map<String,Object>procResult  = ejecutaSP(new CargarNegociosPorAgenteRamo5(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		Utils.debugProcedure(logger, "...P_GET_NEGOCIO_X_AGENTE_RAMO5", params,lista);
		List<GenericVO>listaGen       = new ArrayList<GenericVO>();
		if(lista!=null&&lista.size()>0)
		{
			for(Map<String,String>negocio:lista)
			{
				listaGen.add(new GenericVO(negocio.get("OTCLAVE"),negocio.get("OTVALOR")));
			}
		}
		return listaGen;
	}
	
	protected class CargarNegociosPorAgenteRamo5 extends StoredProcedure
	{
		protected CargarNegociosPorAgenteRamo5(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_GET_NEGOCIO_X_AGENTE_RAMO5");
			declareParameter(new SqlParameter("cdagente" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipoflot" , OracleTypes.VARCHAR));
			String[] cols=new String[]{ "OTCLAVE" , "OTVALOR" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO>cargarCargasPorNegocioRamo5(String cdsisrol,String negocio)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdsisrol" , cdsisrol);
		params.put("negocio"  , negocio);
		Map<String,Object>procResult  = ejecutaSP(new CargarCargasPorNegocioRamo5(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		Utils.debugProcedure(logger, "...P_GET_CAT_CARGA_RAMO5", params, lista);
		List<GenericVO>listaGen = new ArrayList<GenericVO>();
		if(lista!=null)
		{
			for(Map<String,String>elem:lista)
			{
				listaGen.add(new GenericVO(elem.get("ID"),elem.get("NOMBRE")));
			}
		}
		return listaGen;
	}
	
	protected class CargarCargasPorNegocioRamo5 extends StoredProcedure
	{
		protected CargarCargasPorNegocioRamo5(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_GET_CAT_CARGA_RAMO5");
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("negocio"  , OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"ID"
					,"NOMBRE"
					,"TIPO"
					,"ROL"
					,"PERMITIDO"
					,"NEGOCIO"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO>cargarPlanesPorNegocioModeloClavegsRamo5(
			String cdtipsit
			,String modelo
			,String negocio
			,String clavegs
			,String servicio
			,String tipoflot
			)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdramo"   , "5");
		params.put("cdtipsit" , cdtipsit);
		params.put("modelo"   , modelo);
		params.put("negocio"  , negocio);
		params.put("clavegs"  , clavegs);
		params.put("servicio" , servicio);
		params.put("tipoflot" , tipoflot);
		Map<String,Object>procResult  = ejecutaSP(new CargarPlanesPorNegocioModeloClavegsRamo5(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		Utils.debugProcedure(logger, "...P_GET_PLANES_CUSTOM_RAMO5", params, lista);
		List<GenericVO>listaGen = new ArrayList<GenericVO>();
		for(Map<String,String>plan:lista)
		{
			listaGen.add(new GenericVO(plan.get("CDPLAN"),plan.get("DSPLAN")));
		}
		return listaGen;
	}
	
	protected class CargarPlanesPorNegocioModeloClavegsRamo5 extends StoredProcedure
	{
		protected CargarPlanesPorNegocioModeloClavegsRamo5(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_GET_PLANES_CUSTOM_RAMO5");
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("modelo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("negocio"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("clavegs"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("servicio" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipoflot" , OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"CDPLAN"
					,"DSPLAN"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO>cargarNegociosPorTipoSituacionAgenteRamo5(
			String cdtipsit
			,String cdagente
			,String producto
			,String cdsisrol
			)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdtipsit" , cdtipsit);
		params.put("cdagente" , cdagente);
		params.put("producto" , producto);
		params.put("cdsisrol" , cdsisrol);
		Map<String,Object>procResult  = ejecutaSP(new CargarNegociosPorTipoSituacionAgenteRamo5(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		List<GenericVO>listaGen       = new ArrayList<GenericVO>();
		if(lista!=null)
		{
			for(Map<String,String>negocio:lista)
			{
				listaGen.add(new GenericVO(negocio.get("OTCLAVE"),negocio.get("OTVALOR")));
			}
		}
		Utils.debugProcedure(logger, "...P_GET_NEGOCIO_X_AGENTE_TIPSIT", params, listaGen);
		return listaGen;
	}
	
	protected class CargarNegociosPorTipoSituacionAgenteRamo5 extends StoredProcedure
	{
		protected CargarNegociosPorTipoSituacionAgenteRamo5(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_GET_NEGOCIO_X_AGENTE_TIPSIT");
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdagente" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("producto" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"OTCLAVE"
					,"OTVALOR"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO>cargarTiposSituacionPorNegocioRamo5(String negocio,String producto)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("negocio"  , negocio);
		params.put("producto" , producto);
		Map<String,Object>procResult  = ejecutaSP(new CargarTiposSituacionPorNegocioRamo5(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		List<GenericVO>listaGen       = new ArrayList<GenericVO>();
		if(lista!=null)
		{
			for(Map<String,String>tiposit:lista)
			{
				listaGen.add(new GenericVO(tiposit.get("CDTIPSIT"),tiposit.get("DSTIPSIT")));
			}
		}
		Utils.debugProcedure(logger, "...P_GET_TIPOSIT_X_NEGOCIO_RAMO_5", params, listaGen);
		return listaGen;
	}
	
	protected class CargarTiposSituacionPorNegocioRamo5 extends StoredProcedure
	{
		protected CargarTiposSituacionPorNegocioRamo5(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_GET_TIPOSIT_X_NEGOCIO_RAMO_5");
			declareParameter(new SqlParameter("negocio"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("producto" , OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"CDTIPSIT"
					,"DSTIPSIT"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO>cargarCuadrosPorSituacion(String cdtipsit)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdtipsit" , cdtipsit);
		Map<String,Object>procResult  = ejecutaSP(new CargarCuadrosPorSituacion(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		List<GenericVO>listaGen       = new ArrayList<GenericVO>();
		if(lista!=null)
		{
			for(Map<String,String>tiposit:lista)
			{
				listaGen.add(new GenericVO(tiposit.get("NMCUADRO"),tiposit.get("PORC_COMISION")));
			}
		}
		Utils.debugProcedure(logger, "...P_RECUPERA_CUADROS_COM_X_PROD", params, listaGen);
		return listaGen;
	}
	
	protected class CargarCuadrosPorSituacion extends StoredProcedure
	{
		protected CargarCuadrosPorSituacion(DataSource dataSource)
		{
			super(dataSource,"PKG_LISTAS.P_RECUPERA_CUADROS_COM_X_PROD");
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"NMCUADRO"
					,"PORC_COMISION"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO>recuperarSumaAseguradaRamo4(String cdsisrol,String cdplan)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdsisrol" , cdsisrol);
		params.put("cdplan"   , cdplan);
		Map<String,Object>procResult  = ejecutaSP(new RecuperarSumaAseguradaRamo4(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		List<GenericVO>listaGen       = new ArrayList<GenericVO>();
		if(lista!=null)
		{
			for(Map<String,String>tiposit:lista)
			{
				listaGen.add(new GenericVO(tiposit.get("OTCLAVE"),tiposit.get("OTVALOR")));
			}
		}
		Utils.debugProcedure(logger, "...P_GET_SUMA_ASEG_MSC", params, listaGen);
		return listaGen;
	}
	
	protected class RecuperarSumaAseguradaRamo4 extends StoredProcedure
	{
		protected RecuperarSumaAseguradaRamo4(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_GET_SUMA_ASEG_MSC");
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdplan"   , OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"OTCLAVE"
					,"OTVALOR"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO>recuperarTiposServicioPorAuto(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsituac" , nmsituac);
		params.put("nmsuplem" , nmsuplem);
		Map<String,Object>procResult  = ejecutaSP(new RecuperarTiposServicioPorAuto(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		List<GenericVO>listaGen       = new ArrayList<GenericVO>();
		if(lista!=null)
		{
			for(Map<String,String>tiposit:lista)
			{
				listaGen.add(new GenericVO(tiposit.get("OTCLAVE"),tiposit.get("OTVALOR")));
			}
		}
		Utils.debugProcedure(logger, "...P_GET_TIPO_SERVICIO_AUTO", params, listaGen);
		return listaGen;
	}
	
	protected class RecuperarTiposServicioPorAuto extends StoredProcedure
	{
		protected RecuperarTiposServicioPorAuto(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_TIPO_SERVICIO_AUTO");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"OTCLAVE"
					,"OTVALOR"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO> recuperarListaTiposValorRamo5PorRol(String cdtipsit,String cdsisrol,String cdusuari) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipsit" , cdtipsit);
		params.put("cdsisrol" , cdsisrol);
		params.put("cdusuari" , cdusuari);
		Map<String,Object>procResult  = ejecutaSP(new RecuperarListaTiposValorRamo5PorRol(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		List<GenericVO>listaGen       = new ArrayList<GenericVO>();
		if(lista!=null)
		{
			for(Map<String,String>tiposit:lista)
			{
				listaGen.add(new GenericVO(tiposit.get("OTCLAVE"),tiposit.get("OTVALOR")));
			}
		}
		Utils.debugProcedure(logger, "...P_GET_TIPOVALOR_ROL_RAMO5", params, listaGen);
		return listaGen;
	}
	
	protected class RecuperarListaTiposValorRamo5PorRol extends StoredProcedure
	{
		protected RecuperarListaTiposValorRamo5PorRol(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_TIPOVALOR_ROL_RAMO5");
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"OTCLAVE"
					,"OTVALOR"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	public List<GenericVO> recuperarModulosEstadisticas() throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		Map<String,Object>procResult  = ejecutaSP(new RecuperarModulosEstadisticas(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		List<GenericVO>listaGen       = new ArrayList<GenericVO>();
		if(lista!=null)
		{
			for(Map<String,String>tiposit:lista)
			{
				listaGen.add(new GenericVO(tiposit.get("OTCLAVE"),tiposit.get("OTVALOR")));
			}
		}
		Utils.debugProcedure(logger, "...P_GET_MODULOS", params, listaGen);
		return listaGen;
	}
	
	protected class RecuperarModulosEstadisticas extends StoredProcedure
	{
		protected RecuperarModulosEstadisticas(DataSource dataSource)
		{
			super(dataSource,"PKG_ESTADISTICA.P_GET_MODULOS");
			String[] cols=new String[]{
					"OTCLAVE"
					,"OTVALOR"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO> recuperarTareasEstadisticas(String cdmodulo) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdmodulo" , cdmodulo);
		Map<String,Object>procResult  = ejecutaSP(new RecuperarTareasEstadisticas(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		List<GenericVO>listaGen       = new ArrayList<GenericVO>();
		if(lista!=null)
		{
			for(Map<String,String>tiposit:lista)
			{
				listaGen.add(new GenericVO(tiposit.get("CDTAREA"),tiposit.get("DSTAREA")));
			}
		}
		Utils.debugProcedure(logger, "...P_GET_TAREAS", params, listaGen);
		return listaGen;
	}
	
	protected class RecuperarTareasEstadisticas extends StoredProcedure
	{
		protected RecuperarTareasEstadisticas(DataSource dataSource)
		{
			super(dataSource,"PKG_ESTADISTICA.P_GET_TAREAS");
			declareParameter(new SqlParameter("cdmodulo" , OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"CDTAREA"
					,"DSTAREA"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO> obtieneAgenteEspecifico(String cdagente) throws Exception {
		try {
			HashMap<String,Object> params =  new LinkedHashMap<String, Object>();
			params.put("pv_cdagente_i", cdagente);
    		
    		Map<String, Object> resultado = ejecutaSP(new ObtieneAgenteEspecifico(getDataSource()), params);
    		return (List<GenericVO>) resultado.get("pv_registro_o");
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
	}
	
	protected class ObtieneAgenteEspecifico extends StoredProcedure
	{
		protected ObtieneAgenteEspecifico(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_OBTIENE_AGENTES_X_CDAGENTE");
			declareParameter(new SqlParameter("pv_cdagente_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o",   OracleTypes.CURSOR, new ObtenerAgentesMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO> recuperarListaPools() throws Exception
	{
		Map<String,Object>       procRes    = ejecutaSP(new RecuperarListaPools(getDataSource()),new LinkedHashMap<String,String>());
		List<Map<String,String>> listaMapas = (List<Map<String,String>>)procRes.get("pv_registro_o");
		List<GenericVO>          lista      = new ArrayList<GenericVO>();
		if(listaMapas!=null)
		{
			for(Map<String,String> mapa : listaMapas)
			{
				lista.add(new GenericVO(mapa.get("OTCLAVE"),mapa.get("OTVALOR")));
			}
		}
		return lista;
	}
	
	protected class RecuperarListaPools extends StoredProcedure
	{
		protected RecuperarListaPools(DataSource dataSource)
		{
			super(dataSource,"Pkg_Consulta.P_GET_POOL");
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(new String[]{"OTCLAVE","OTVALOR"})));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO> obtieneAtributosExcel(String cdTabla) throws Exception{
		
		try {
			Map<String,Object> params=new LinkedHashMap<String,Object>(0);
			params.put("pv_cdtabla", cdTabla);
			Map<String, Object> resultado = ejecutaSP(new ObtieneAtributosExcel(getDataSource()), params);
			return (List<GenericVO>) resultado.get("pv_registro_o");
			
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
	}
	
	protected class ObtieneAtributosExcel extends StoredProcedure {
    	protected ObtieneAtributosExcel(DataSource dataSource) {
            super(dataSource,"PKG_SINIESTRO.P_GET_ATRIBUTO_EXCEL");
            declareParameter(new SqlParameter("pv_cdtabla",       OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ObtenerTmanteniMapper()));
            declareParameter(new SqlOutParameter("pv_messages_o", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	@Override
	public List<GenericVO> obtieneCatalogoParentescoAutos() throws Exception {
		try {
			HashMap<String,Object> params = new LinkedHashMap<String,Object>();    		
    		Map<String, Object> resultado = ejecutaSP(new ObtieneCatalogoParentescoAutos(getDataSource()), params);
    		return (List<GenericVO>) resultado.get("pv_registro_o");
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
	}
    
	protected class ObtieneCatalogoParentescoAutos extends StoredProcedure {
	
		protected ObtieneCatalogoParentescoAutos(DataSource dataSource) {
			super(dataSource, "PKG_CONSULTA.P_OBTIENE_PARENTESCO_AUTO");
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ObtieneParentescoMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	protected class ObtieneParentescoMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new GenericVO(rs.getString("CODIGO"),rs.getString("DESCRIPCION"));
		}
	}
	
	@Override
	public List<GenericVO> recuperarListaConvenios() throws Exception
	{
		Map<String,Object>       procRes    = ejecutaSP(new RecuperarListaConvenios(getDataSource()),new LinkedHashMap<String,String>());
		List<Map<String,String>> listaMapas = (List<Map<String,String>>)procRes.get("pv_registro_o");
		logger.debug(Utils.log("ListaConvenios:", listaMapas));
		List<GenericVO>          lista      = new ArrayList<GenericVO>();
		if(listaMapas!=null)
		{
			for(Map<String,String> mapa : listaMapas)
			{
				lista.add(new GenericVO(mapa.get("CDCONVEN"),mapa.get("DSCONVEN")));
			}
		}
		return lista;
	}
	
	protected class RecuperarListaConvenios extends StoredProcedure
	{
		protected RecuperarListaConvenios(DataSource dataSource)
		{
			super(dataSource,"Pkg_Consulta.P_GET_CAT_TCONVENIOS");
			declareParameter(new SqlOutParameter("pv_cdconven_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(new String[]{"CDCONVEN","DSCONVEN"})));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	public List<GenericVO> recuperarContratantesSalud(String nombre) throws Exception
	{
		HashMap<String,Object> params =  new LinkedHashMap<String, Object>();
		params.put("pv_cadena_i", nombre);
		Map<String,Object>       procRes    = ejecutaSP(new RecuperarContratantesSalud(getDataSource()), params);
		List<Map<String,String>> listaMapas = (List<Map<String,String>>)procRes.get("pv_registro_o");
		logger.debug(Utils.log("ContratantesSalud:", listaMapas));
		List<GenericVO>          lista      = new ArrayList<GenericVO>();
		if(listaMapas!=null)
		{
			for(Map<String,String> mapa : listaMapas)
			{
				lista.add(new GenericVO(mapa.get("CDPERSON"),mapa.get("NOMBRE")));
			}
		}
		return lista;
	}
	
	protected class RecuperarContratantesSalud extends StoredProcedure
	{
		protected RecuperarContratantesSalud(DataSource dataSource)
		{
			super(dataSource,"Pkg_Consulta.P_GET_CONTRATANTES_SALUD");
			declareParameter(new SqlParameter("pv_cadena_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(new String[]{"CDPERSON","NOMBRE"})));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	public List<GenericVO> recuperaRamosColectivoTipoRamo(String tipoRamo) throws Exception
	{
		HashMap<String,Object> params =  new LinkedHashMap<String, Object>();
		params.put("pv_tiporamo_i", tipoRamo);
		Map<String,Object>       procRes    = ejecutaSP(new RecuperaRamosColectivoTipoRamo(getDataSource()), params);
		List<Map<String,String>> listaMapas = (List<Map<String,String>>)procRes.get("pv_registro_o");
		logger.debug(Utils.log("RamosColectivoTipoRamo:", listaMapas));
		List<GenericVO>          lista      = new ArrayList<GenericVO>();
		if(listaMapas!=null)
		{
			for(Map<String,String> mapa : listaMapas)
			{
				lista.add(new GenericVO(mapa.get("ramo"),mapa.get("descripcion")));
			}
		}
		return lista;
	}
	
	protected class RecuperaRamosColectivoTipoRamo extends StoredProcedure
	{
		protected RecuperaRamosColectivoTipoRamo(DataSource dataSource)
		{
			super(dataSource,"Pkg_Consulta.P_GET_PRODUCTOS_X_TIPRAM");
			declareParameter(new SqlParameter("pv_tiporamo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(new String[]{"ramo","descripcion"})));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	public List<GenericVO> recuperarTiposRamoSituacionColectivo(String tipoRamo, String cdramo) throws Exception
	{
		HashMap<String,Object> params =  new LinkedHashMap<String, Object>();
		params.put("pv_tiporamo_i", tipoRamo);
		params.put("pv_cdramo_i", cdramo);
		Map<String,Object>       procRes    = ejecutaSP(new RecuperarTiposRamoSituacionColectivo(getDataSource()), params);
		List<Map<String,String>> listaMapas = (List<Map<String,String>>)procRes.get("pv_registro_o");
		logger.debug(Utils.log("TiposRamoSituacionColectivo=", listaMapas));
		List<GenericVO>          lista      = new ArrayList<GenericVO>();
		if(listaMapas!=null)
		{
			for(Map<String,String> mapa : listaMapas)
			{
				lista.add(new GenericVO(mapa.get("tipo_inciso"),mapa.get("descripcion")));
			}
		}
		return lista;
	}
	
	protected class RecuperarTiposRamoSituacionColectivo extends StoredProcedure
	{
		protected RecuperarTiposRamoSituacionColectivo(DataSource dataSource)
		{
			super(dataSource,"Pkg_Consulta.P_GET_TIPOS_SIT_X_CDRAMO");
			declareParameter(new SqlParameter("pv_tiporamo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(new String[]{"tipo_inciso","descripcion"})));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	public List<GenericVO> recuperarListaFiltroPropiedadesInciso(String cdunieco, String cdramo,String estado,String nmpoliza) throws Exception
	{
		
		HashMap<String,Object> params =  new LinkedHashMap<String, Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i", cdramo);
		params.put("pv_estado_i", estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		Map<String,Object>       procRes    = ejecutaSP(new RecuperarListaFiltroPropiedadesInciso(getDataSource()), params);
		List<Map<String,String>> listaMapas = (List<Map<String,String>>)procRes.get("pv_registro_o");
		logger.debug(Utils.log("ListaFiltroPropiedadesInciso:", listaMapas));
		List<GenericVO>          lista      = new ArrayList<GenericVO>();
		if(listaMapas!=null)
		{
			for(Map<String,String> mapa : listaMapas)
			{
				lista.add(new GenericVO(mapa.get("dsatribu"),mapa.get("dsatribu")));
			}
		}
		return lista;
	}
	
	protected class RecuperarListaFiltroPropiedadesInciso extends StoredProcedure
	{
		protected RecuperarListaFiltroPropiedadesInciso(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA_PRUEBA.P_Lista_Att_Inc");
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(new String[]{"dsatribu"})));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO> recuperarRamosPorSucursalPorTipogrupo(String cdunieco, String tipogrupo) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		
		params.put("cdunieco"  , cdunieco);
		params.put("tipogrupo" , tipogrupo);
		
		Map<String,Object> procRes = ejecutaSP(new RecuperarRamosPorSucursalPorTipogrupoSP(getDataSource()),params);
		
		List<Map<String,String>> lista = (List<Map<String,String>>) procRes.get("pv_registro_o");
		
		List<GenericVO> lista2 = new ArrayList<GenericVO>();
		
		if(lista!=null)
		{
			for(Map<String,String> ramo : lista)
			{
				lista2.add(new GenericVO(ramo.get("CDRAMO"),ramo.get("DSRAMO")));
			}
		}
		
		return lista2;
	}
	
	protected class RecuperarRamosPorSucursalPorTipogrupoSP extends StoredProcedure
	{
		protected RecuperarRamosPorSucursalPorTipogrupoSP(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_OBT_RAMO_X_CDUNIECO");
			declareParameter(new SqlParameter("cdunieco"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipogrupo" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(new String[]{"CDRAMO","DSRAMO"})));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO> recuperarTipsitPorRamoPorTipogrupo(String cdramo, String tipogrupo) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		
		params.put("cdramo"    , cdramo);
		params.put("tipogrupo" , tipogrupo);
		
		Map<String,Object> procRes = ejecutaSP(new RecuperarTipsitPorRamoPorTipogrupoSP(getDataSource()),params);
		
		List<Map<String,String>> lista = (List<Map<String,String>>) procRes.get("pv_registro_o");
		
		List<GenericVO> lista2 = new ArrayList<GenericVO>();
		
		if(lista!=null)
		{
			for(Map<String,String> ramo : lista)
			{
				lista2.add(new GenericVO(ramo.get("CDTIPSIT"),ramo.get("DSTIPSIT")));
			}
		}
		
		return lista2;
	}
	
	protected class RecuperarTipsitPorRamoPorTipogrupoSP extends StoredProcedure
	{
		protected RecuperarTipsitPorRamoPorTipogrupoSP(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_OBTIENE_SITUACION");
			declareParameter(new SqlParameter("cdramo"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipogrupo" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(new String[]{"CDTIPSIT","DSTIPSIT"})));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO> recuperarTiposDeEndosoPorCdramoPorCdtipsit(String cdramo, String cdtipsit, String vigente) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdramo"   , cdramo);
		params.put("cdtipsit" , cdtipsit);
		params.put("vigente"  , StringUtils.isNotBlank(vigente) ? vigente : "N");
		
		Map<String,Object> procRes = ejecutaSP(new RecuperarTiposDeEndosoPorCdramoPorCdtipsitSP(getDataSource()),params);
		
		List<Map<String,String>> lista = (List<Map<String,String>>) procRes.get("pv_registro_o");
		
		List<GenericVO> lista2 = new ArrayList<GenericVO>();
		
		if(lista!=null)
		{
			for(Map<String,String> ramo : lista)
			{
				lista2.add(new GenericVO(ramo.get("CDTIPSUP"),ramo.get("DSTIPSUP")));
			}
		}
		
		return lista2;
	}
	
	protected class RecuperarTiposDeEndosoPorCdramoPorCdtipsitSP extends StoredProcedure
	{
		protected RecuperarTiposDeEndosoPorCdramoPorCdtipsitSP(DataSource dataSource)
		{
			super(dataSource,"P_GET_ENDOSOS_X_RAMO_X_TIPSIT");
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("vigente"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(new String[]{"CDTIPSUP","DSTIPSUP"})));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO> recuperarMotivosRechazo (String ntramite) throws Exception {
		Map<String, String> params = new LinkedHashMap<String,String>();
		params.put("ntramite", ntramite);
		Map<String,Object> procRes = ejecutaSP(new RecuperarMotivosRechazoSP(getDataSource()),params);
		List<Map<String,String>> lista = (List<Map<String,String>>) procRes.get("pv_registro_o");
		List<GenericVO> lista2 = new ArrayList<GenericVO>();
		if(lista!=null)
		{
			for(Map<String,String> ramo : lista)
			{
				lista2.add(new GenericVO(ramo.get("CDRAZRECHA"),ramo.get("DSRAZRECHA"),ramo.get("TEXTORECHA")));
			}
		}
		return lista2;
	}
	
	protected class RecuperarMotivosRechazoSP extends StoredProcedure
	{
		protected RecuperarMotivosRechazoSP(DataSource dataSource)
		{
			super(dataSource,"P_GET_MOTIVOS_RECHAZO_TRA");
			declareParameter(new SqlParameter("ntramite", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(new String[]{
					"CDRAZRECHA", "DSRAZRECHA", "TEXTORECHA"})));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> obtenerTablaApoyo1(String cdtabla)throws Exception{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("pv_cdtabla_i"  , cdtabla);
		Map<String,Object>procResult  = ejecutaSP(new ObtenerTablaApoyo1(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		Utils.debugProcedure(logger, "P_GET_VAL_TTAPVAT1", params,lista);
		return lista;
	}
	
	protected class ObtenerTablaApoyo1 extends StoredProcedure
    {
    	protected ObtenerTablaApoyo1(DataSource dataSource)
        {
            super(dataSource,"P_GET_VAL_TTAPVAT1");
            declareParameter(new SqlParameter("pv_cdtabla_i"  , OracleTypes.VARCHAR));
            String[] cols = new String[]{
            		"otclave",
            		"otvalor"
            };
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	@Override
	public List<Map<String,String>> obtenerCamposExclusionRenovacion()throws Exception{
		Map<String,String>params=new LinkedHashMap<String,String>();
		Map<String,Object>procResult  = ejecutaSP(new ObtenerCamposExclusionRenovacion(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		Utils.debugProcedure(logger, "P_GET_CAMPEXTREN", params,lista);
		return lista;
	}
	
	protected class ObtenerCamposExclusionRenovacion extends StoredProcedure
    {
    	protected ObtenerCamposExclusionRenovacion(DataSource dataSource)
        {
            super(dataSource,"P_GET_CAMPEXTREN");
            String[] cols = new String[]{
            		"otvalor01",
            		"otclave1"
            };
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	@Override
	public List<GenericVO> obtieneCatalogoDescAtrib(String cdRamo, String dsAtribu, String otValor) throws Exception {		
		try {
			HashMap<String,Object> params = new LinkedHashMap<String,Object>();
			params.put("pv_cdramo_i", cdRamo);
			params.put("pv_dsatribu_i", dsAtribu);
			params.put("pv_otvalor_i", otValor);    		
    		Map<String, Object> resultado = ejecutaSP(new ObtieneCatalogoDescAtrib(getDataSource()), params);
    		return (List<GenericVO>) resultado.get("pv_registro_o");
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
	}
	
	protected class ObtieneCatalogoDescAtrib extends StoredProcedure {
	
		protected ObtieneCatalogoDescAtrib(DataSource dataSource) {
			super(dataSource, "P_GET_CAT_X_DSATRI_POL");
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsatribu_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ObtieneAtributosPolMapper()));
			declareParameter(new SqlOutParameter("pv_messages_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	@Override
    public List<GenericVO> obtieneCatalogoRetAdminAgente(String pv_numsuc_i, String pv_cdagente_i) throws Exception {      
        try {
            HashMap<String,Object> params = new LinkedHashMap<String,Object>();
            params.put("pv_numsuc_i", pv_numsuc_i);
            params.put("pv_cdagente_i", pv_cdagente_i);
                      
            Map<String, Object> resultado = ejecutaSP(new ObtieneCatalogoRetAdminAgente(getDataSource()), params);
            return (List<GenericVO>) resultado.get("pv_registro_o");
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }
    
    protected class ObtieneCatalogoRetAdminAgente extends StoredProcedure {
    
        protected ObtieneCatalogoRetAdminAgente(DataSource dataSource) {
            super(dataSource, "PKG_RETENEDORAS.P_GET_RETENEDORAS_AGENTE");
            declareParameter(new SqlParameter("pv_numsuc_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdagente_i", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ObtieneAtributosPolMapper()));
            declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public List<GenericVO> obtieneAdminXAgente(String pv_cdagente_i) throws Exception
    {
        Map<String,String> params=new HashMap<String, String>();
        params.put("pv_cdagente_i", pv_cdagente_i);
        
        
        
        
        Map<String, Object> resultado = ejecutaSP(new ObtieneAdminXAgente(getDataSource()), params);
        return (List<GenericVO>) resultado.get("pv_registro_o");
    }
    
    protected class ObtieneAdminXAgente extends StoredProcedure {
        
        protected ObtieneAdminXAgente(DataSource dataSource) {
            
            super(dataSource, "PKG_RETENEDORAS.P_GET_ADMINISTRADORAS_AGENTE");
            declareParameter(new SqlParameter("pv_cdagente_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ObtieneAtributosPolMapper()));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
        }
    }

	@Override
	public boolean guardaDescripcionCortaCobertura(String cdgarant, String descCorta) throws Exception{		
	    try {
	        HashMap<String,Object> params = new LinkedHashMap<String,Object>();
	        params.put("pv_cdgarant_i", cdgarant);
	        params.put("pv_dsgarant_corta_i", descCorta);
	        ejecutaSP(new GuardaDescripcionCortaCobertura(getDataSource()), params);
	    } catch (Exception e) {
	        throw new Exception(e.getMessage(), e);
	    }
	    return true;
	}
	
	protected class GuardaDescripcionCortaCobertura extends StoredProcedure {
	    
	    protected GuardaDescripcionCortaCobertura(DataSource dataSource) {
	        super(dataSource, "PKG_COTIZA.P_ACTUALIZA_TGARANTI");
	        declareParameter(new SqlParameter("pv_cdgarant_i", OracleTypes.VARCHAR));
	        declareParameter(new SqlParameter("pv_dsgarant_corta_i", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
	        compile();
	    }
	}
	
	@Override
    public List<GenericVO> obtieneClaveDescuentoSubRamo(String pv_numsuc_i, 
                                                        String pv_cveent_i,
                                                        String pv_cdramo_i,
                                                        String pv_cdtipsit_i) throws Exception {      
        try {
            
            HashMap<String,Object> params = new LinkedHashMap<String,Object>();
            params.put("pv_numsuc_i", pv_numsuc_i);
            params.put("pv_cveent_i", pv_cveent_i);
            params.put("pv_cdramo_i", pv_cdramo_i);
            params.put("pv_cdtipsit_i", pv_cdtipsit_i);   
            Map<String, Object> resultado = ejecutaSP(new ObtieneClaveDescuentoSubRamo(getDataSource()), params);
            return (List<GenericVO>) resultado.get("pv_registro_o");
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }
    
    protected class ObtieneClaveDescuentoSubRamo extends StoredProcedure {
    
        protected ObtieneClaveDescuentoSubRamo(DataSource dataSource) {
            super(dataSource, "PKG_RETENEDORAS.P_GET_CLA_DES_X_SUBRAMO");
            declareParameter(new SqlParameter("pv_numsuc_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cveent_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ObtieneAtributosPolMapper()));
            declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
            compile();
        }
    }
	
	@Override
    public List<GenericVO> obtieneIdsCierres() throws Exception {      
        try {
            HashMap<String,Object> params = new LinkedHashMap<String,Object>();
                      
            Map<String, Object> resultado = ejecutaSP(new ObtieneIdsCierres(getDataSource()), params);
            return (List<GenericVO>) resultado.get("pv_registro_o");
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }
    
    protected class ObtieneIdsCierres extends StoredProcedure {
    
        protected ObtieneIdsCierres(DataSource dataSource) {
            super(dataSource, "PKG_ESTADISTICAS_MESA.p_obtiene_cierres");
            String[] cols = new String[]{
                    "idcierre",
                    "fecha_cierre"
            };
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    public List<GenericVO> recuperarListaFiltroPropiedadInciso(String cdramo,String cdtipsit,String nivel) throws Exception
    {
        HashMap<String,Object> params =  new LinkedHashMap<String, Object>();
        params.put("pv_cdramo_i", cdramo);
        params.put("pv_cdtipsit_i", cdtipsit);
        params.put("pv_nivel_i", nivel);
        Map<String,Object>       procRes    = ejecutaSP(new RecuperarListaFiltroPropiedadInciso(getDataSource()), params);
        List<Map<String,String>> listaMapas = (List<Map<String,String>>)procRes.get("pv_registro_o");
        logger.debug(Utils.log("ListaFiltroPropiedadInciso:", listaMapas));
        List<GenericVO>          lista      = new ArrayList<GenericVO>();
        if(listaMapas!=null)
        {
            for(Map<String,String> mapa : listaMapas)
            {
                lista.add(new GenericVO(mapa.get("dsatribu"),mapa.get("dsatribu")));
            }
        }
        return lista;
    }
    protected class RecuperarListaFiltroPropiedadInciso extends StoredProcedure
    {
        protected RecuperarListaFiltroPropiedadInciso(DataSource dataSource)
        {
            super(dataSource,"PKG_CONSULTA_PRUEBA.P_GET_LISTA_FILTROS");
            declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nivel_i", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(new String[]{"dsatribu"})));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public List<Map<String, String>> recuperarTiposEndosoPorTramite (String ntramite) throws Exception {
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("pv_ntramite_i", ntramite);
        Map<String, Object> procRes = ejecutaSP(new RecuperarTiposEndosoPorTramiteSP(getDataSource()), params);
        List<Map<String, String>> lista = (List<Map<String, String>>) procRes.get("pv_registro_o");
        if (lista == null) {
            lista = new ArrayList<Map<String, String>>();
        }
        logger.debug("recuperarTiposEndosoPorTramite lista {}", Utils.log(lista));
        return lista;
    }
    
    protected class RecuperarTiposEndosoPorTramiteSP extends StoredProcedure {
        protected RecuperarTiposEndosoPorTramiteSP (DataSource dataSource) {
            super(dataSource, "P_END_GET_TIPOS_END_X_TRAMITE");
            declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(new String[]{
                    "CDTIPSUP", "DSTIPSUP"
            })));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
        }
    }
}
