package mx.com.gseguros.portal.general.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.aon.portal.model.IsoVO;
import mx.com.aon.portal.model.UsuarioRolEmpresaVO;
import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.dao.impl.GenericMapper;
import mx.com.gseguros.portal.general.dao.UsuarioDAO;
import mx.com.gseguros.portal.general.model.UsuarioVO;
import oracle.jdbc.OracleTypes;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class UsuarioDAOImpl extends AbstractManagerDAO implements UsuarioDAO {

	private Logger logger = Logger.getLogger(UsuarioDAOImpl.class);
	private static SimpleDateFormat renderFechas = new SimpleDateFormat("dd/MM/yyyy");
	
	
	@Override
	public String creaEditaRolSistema(Map params) throws Exception {
		try {
			Map<String, Object> result = ejecutaSP(new CreaEditaRolSistema(getDataSource()), params);
			return (String) result.get("pv_title_o");
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
	}
	
	protected class CreaEditaRolSistema extends StoredProcedure {
		
		protected CreaEditaRolSistema(DataSource dataSource){
			super(dataSource, "PKG_GENERA_USUARIO.P_MOV_TSISROL");
			declareParameter(new SqlParameter("pv_accion_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdsisrol_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dssisrol_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public GenericVO guardaUsuario(Map<String, String> params) throws Exception {
		try {
			HashMap<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("PV_CDUSUARI_I", params.get("cdusuari"));
			parametros.put("PV_DSNOMBRE_I", params.get("dsnombre"));
			parametros.put("PV_DSNOMBRE1_I", params.get("dsnombre1"));
			parametros.put("PV_DSAPELLIDO_I", params.get("dsapellido"));
			parametros.put("PV_DSAPELLIDO1_I", params.get("dsapellido1"));
			parametros.put("PV_CDSISROL_I", params.get("cdsisrol"));
			parametros.put("PV_OTSEXO_I", params.get("otsexo"));
			parametros.put("PV_FENACIMI_I", StringUtils.isBlank(params.get("fenacimi")) ? null : renderFechas.parse(params.get("fenacimi")));
			parametros.put("PV_CDRFC_I", params.get("cdrfc"));
			parametros.put("PV_DSEMAIL_I", params.get("dsemail"));
			parametros.put("PV_CURP_I", params.get("curp"));
			parametros.put("PV_CDMODGRA_I", params.get("cdmodgra"));
			parametros.put("PV_CDUNIECO_I", params.get("cdunieco"));
			parametros.put("PV_CDRAMO_I", params.get("cdramo"));
			parametros.put("PV_FEDESDE_I", StringUtils.isBlank(params.get("feini")) ? null : renderFechas.parse(params.get("feini")));
			parametros.put("PV_FEVENLICV_I", StringUtils.isBlank(params.get("fefin")) ? null : renderFechas.parse(params.get("fefin")));
			parametros.put("PV_CLASEAG_I", params.get("claseag"));
			parametros.put("PV_STATUSAG_I", params.get("statusag"));
			parametros.put("PV_CDOFICIN_I", params.get("cdoficin"));
			parametros.put("PV_CDBROKER_I", params.get("cdbroker"));
			parametros.put("PV_CDEMPRESA_I", params.get("cdempresa"));
			parametros.put("PV_ACCION_I", params.get("accion"));
			parametros.put("PV_CDUNISLD_I", params.get("cdunisld"));
            parametros.put("PV_SWSUSMAT_I", "S".equalsIgnoreCase(params.get("swsusmat")) ? "S" : "N");
			
			Map<String, Object> result = ejecutaSP(new GuardaUsuario(getDataSource()), parametros);

			GenericVO respuestaVO = new GenericVO();
			respuestaVO.setKey(result.get("msg_id").toString());
			respuestaVO.setValue(result.get("msg_title").toString());
	    	return respuestaVO;
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
	}

	protected class GuardaUsuario extends StoredProcedure {
		
		protected GuardaUsuario(DataSource dataSource){
			super(dataSource, "PKG_GENERA_USUARIO.P_MOV_USUARIO");
			// Respetar el orden de declaracion de los parametros:
			declareParameter(new SqlParameter("PV_CDUSUARI_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_DSNOMBRE_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_DSNOMBRE1_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_DSAPELLIDO_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_DSAPELLIDO1_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_CDSISROL_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_OTSEXO_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_FENACIMI_I", OracleTypes.DATE));// DEBE SER TIPO DATE ( PV_FENACIMI_I IN  MPERSONA.FENACIMI%TYPE es tipo DATE)
			declareParameter(new SqlParameter("PV_CDRFC_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_DSEMAIL_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_CURP_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_CDMODGRA_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_CDUNIECO_I", OracleTypes.VARCHAR));//DEBE SER NUMBER(4) (PV_CDUNIECO_I IN  TUSUARIO.CDUNIECO%TYPE es tipo NUMBER (4))
			declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));//DEBE SER NUMBER(3) pero se queda como VARCHAR
			declareParameter(new SqlParameter("PV_FEDESDE_I", OracleTypes.DATE));// DEBE SER TIPO DATE
			declareParameter(new SqlParameter("PV_FEVENLICV_I", OracleTypes.DATE));//DEBE SER DE TIPO DATE
			declareParameter(new SqlParameter("PV_CLASEAG_I", OracleTypes.VARCHAR));//DEBE SER VARCHAR
			declareParameter(new SqlParameter("PV_STATUSAG_I", OracleTypes.VARCHAR));//DEBE SER VARCHAR
			declareParameter(new SqlParameter("PV_CDOFICIN_I", OracleTypes.VARCHAR));//DEBE SER NUMBER (6)
			declareParameter(new SqlParameter("PV_CDBROKER_I", OracleTypes.VARCHAR));//DEBE SER NUMBER (6)
			declareParameter(new SqlParameter("PV_CDEMPRESA_I", OracleTypes.VARCHAR));//DEBE SER TIPO , ASI SE DEFINE EN EL PROCEDURE
			declareParameter(new SqlParameter("PV_ACCION_I", OracleTypes.VARCHAR));//DEBE SER TIPO VARCHAR, ASI SE DEFINE EN EL PROCEDURE
			declareParameter(new SqlParameter("PV_CDUNISLD_I", OracleTypes.VARCHAR));//DEBE SER TIPO VARCHAR, ASI SE DEFINE EN EL PROCEDURE
            declareParameter(new SqlParameter("PV_SWSUSMAT_I", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));//DEBE SER TIPO NUMBER
			declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public void cambiaEstatusUsuario(Map params) throws Exception {
		try {
			ejecutaSP(new CambiaEstatusUsuario(getDataSource()), params);
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
	}
	
	protected class CambiaEstatusUsuario extends StoredProcedure {
		
		protected CambiaEstatusUsuario(DataSource dataSource){
			super(dataSource, "PKG_GENERA_USUARIO.P_CAMBIA_ESTATUS");
			declareParameter(new SqlParameter("PV_CDUSUARI_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_SWACTIVO_I", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	@Override
	public List<UsuarioRolEmpresaVO> obtieneRolesCliente(String user) throws Exception {
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("PV_CDUSUARIO_I", user);
		
		Map<String, Object> resultado = ejecutaSP(new CargaRolesClientes(getDataSource()), params);
		return (List<UsuarioRolEmpresaVO>) resultado.get("PV_REGISTRO_O");
	}
	
	protected class CargaRolesClientes extends StoredProcedure {

		protected CargaRolesClientes(DataSource dataSource) {
			super(dataSource, "PKG_CONFPAGINA.P_OBTIENE_ROLES_CLIENTE");
			declareParameter(new SqlParameter("PV_CDUSUARIO_I", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new UsuarioRolEmpresaMapper()));
			declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
			compile();
		}
	}

    protected class UsuarioRolEmpresaMapper implements RowMapper<UsuarioRolEmpresaVO> {
		public UsuarioRolEmpresaVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			UsuarioRolEmpresaVO usuarioRolEmpresaVO = new UsuarioRolEmpresaVO();
			usuarioRolEmpresaVO.setCdUsuario(rs.getString("CDUSUARIO"));
			usuarioRolEmpresaVO.setCdPerson(rs.getString("CDPERSON"));
			usuarioRolEmpresaVO.setDsUsuario(rs.getString("DSUSUARI"));
			usuarioRolEmpresaVO.setCdElemento(rs.getString("CDELEMENTO"));
			usuarioRolEmpresaVO.setDsElemen(rs.getString("DSELEMEN"));
			usuarioRolEmpresaVO.setCdSisRol(rs.getString("CDSISROL"));
			usuarioRolEmpresaVO.setDsSisRol(rs.getString("DSSISROL"));
			return usuarioRolEmpresaVO;
        }
    }
    
    /*
    protected class UsuarioRolEmpresaMapper2 implements RowMapper<RamaVO> {
		public RamaVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			RamaVO rama = new RamaVO();
			
			rama.setText(rs.getString("DSELEMEN"));
			rama.setCodigoObjeto(rs.getString("CDELEMENTO"));
			rama.setCdElemento(rs.getString("CDELEMENTO"));
			rama.setNick(rs.getString("CDUSUARIO"));
			rama.setName(rs.getString("DSUSUARI"));
			rama.setDsRol(rs.getString("DSSISROL"));
			rama.setClaveRol(rs.getString("CDSISROL"));
			//rama.setAllowDelete(allowDelete);
			//rama.setChildren(children);
			//rama.setExpanded(expanded);
			//rama.setId(id);
			//rama.setLeaf(leaf);
			return rama;
        }
    }
    */
    
    
    @Override
    public IsoVO obtieneVariablesIso(String user) throws Exception {
    	
    	HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("P_USUARIO", user);
		
		Map<String, Object> resultado = ejecutaSP(new ObtieneVariablesIso(getDataSource()), params);
		
		IsoVO isoVO = new IsoVO();
		isoVO.setCdIdioma((String)resultado.get("LANGCODE"));
		isoVO.setCdRegion((String)resultado.get("REGIONID"));
		isoVO.setClientDateFormat((String)resultado.get("P_DSFORMATOFECHAC"));
		isoVO.setFormatoFecha((String)resultado.get("P_DSFORMATOFECHA"));
		isoVO.setFormatoNumerico((String)resultado.get("P_DSFORMATONUMERICO"));
		isoVO.setLanguague((String)resultado.get("P_LANGUAGUE_ISO"));
		isoVO.setPais((String)resultado.get("P_PAIS"));
		
		return isoVO;
    }
    
    protected class ObtieneVariablesIso extends StoredProcedure {
    	
        protected ObtieneVariablesIso(DataSource dataSource) {
            super(dataSource, "PKG_VAR_GLOBAL.P_VARIABLES_OBTIENE_ISO");
            declareParameter(new SqlParameter("P_USUARIO", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("P_PAIS", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("P_LANGUAGUE_ISO", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("LANGCODE", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("REGIONID", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("P_DSFORMATONUMERICO", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("P_DSFORMATOFECHA", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("P_DSFORMATOFECHAC", OracleTypes.VARCHAR));
            compile();
        }
    }
 
    @Override
	public List<UsuarioVO> obtieneUsuarios(Map params) throws Exception {
		Map<String, Object> resultado = ejecutaSP(new ObtieneUsuarios(getDataSource()), params);
		return (List<UsuarioVO>) resultado.get("PV_REGISTRO_O");
	}
	
	protected class ObtieneUsuarios extends StoredProcedure {

		protected ObtieneUsuarios(DataSource dataSource) {
			super(dataSource, "PKG_GENERA_USUARIO.P_GET_USUARIOS_POR_PRIVILEGIOS");
			declareParameter(new SqlParameter("pv_cdsisrol_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_CDUSUARI_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_NOMBRE_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_CDRFC_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_DSEMAIL_I", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new ObtieneUsuariosMapper()));
			declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
			compile();
		}
	}

    protected class ObtieneUsuariosMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			UsuarioVO usuarioVO = new UsuarioVO();
			usuarioVO.setCdUsuario(rs.getString("CDUSUARI"));
			usuarioVO.setDsNombre(rs.getString("DSNOMBRE"));
			usuarioVO.setDsNombre1(rs.getString("DSNOMBRE1"));
			usuarioVO.setDsApellido(rs.getString("DSAPELLIDO"));
			usuarioVO.setDsApellido1(rs.getString("DSAPELLIDO1"));
			usuarioVO.setOtSexo(rs.getString("OTSEXO"));
			usuarioVO.setdSexo(rs.getString("DSEXO"));
			usuarioVO.setFeNacimi(rs.getString("FENACIMI"));
			usuarioVO.setCdrfc(rs.getString("CDRFC"));
			usuarioVO.setDsEmail(rs.getString("DSEMAIL"));
			usuarioVO.setCurp(rs.getString("CURP"));
			usuarioVO.setCdunisld(rs.getString("CDUNISLD"));

			usuarioVO.setCdrol(rs.getString("CDSISROL"));
			usuarioVO.setEsAdmin(rs.getString("CDMODGRA"));
			usuarioVO.setCdunieco(rs.getString("CDUNIECO"));
			usuarioVO.setFeini(rs.getString("FEDESDE"));
			usuarioVO.setFefinlic(rs.getString("FEVENLICV"));
			usuarioVO.setSwActivo(rs.getString("SWACTIVO"));
			/*
			Se agregan los campos de las modificaciones
			 * statusag
			 * claseag
			 * cdoficin
			 * cdbroker
			  *
			*/
			usuarioVO.setClaseag(rs.getString("CLASEAG"));			
			usuarioVO.setStatusag(rs.getString("STATUSAG"));
			usuarioVO.setCdoficin(rs.getString("CDOFICIN"));
			usuarioVO.setCdbroker(rs.getString("CDBROKER"));
			/**
			 * Se agrega el campo CDEMPRESA
			 */
			usuarioVO.setCdempresa(rs.getString("CDEMPRESA"));
			usuarioVO.setSwsusmat(rs.getString("SWSUSMAT"));
			return usuarioVO;
        }
    }

    @Override
	public List<GenericVO> obtienerRolesPorPrivilegio(Map params) throws Exception {
		Map<String, Object> resultado = ejecutaSP(new ObtienerRolesPorPrivilegio(getDataSource()), params);
		return (List<GenericVO>) resultado.get("PV_REGISTRO_O");
	}
	
	protected class ObtienerRolesPorPrivilegio extends StoredProcedure {
		protected ObtienerRolesPorPrivilegio(DataSource dataSource) {
			super(dataSource, "PKG_GENERA_USUARIO.P_BUSCA_ROL_POR_PRIVILEGIOS");
			declareParameter(new SqlParameter("pv_cdsisrol_i", OracleTypes.VARCHAR));
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
    public List<Map<String, String>> obtieneRolesUsuario(Map params) throws Exception {
    	Map<String, Object> resultado = ejecutaSP(new ObtieneRolesUsuario(getDataSource()), params);
    	return (List<Map<String, String>>) resultado.get("PV_REGISTRO_O");
    }
    
    protected class ObtieneRolesUsuario extends StoredProcedure {
    	
    	protected ObtieneRolesUsuario(DataSource dataSource) {
    		super(dataSource, "PKG_GENERA_USUARIO.P_GET_ROLES_SIST_USUARIO");
    		declareParameter(new SqlParameter("PV_CDUSUARIO_I", OracleTypes.VARCHAR));
    		String[] cols = new String[]{
    				"CDSISROL" , "DSSISROL" , "EXISTE_ROL"
    		};
    		declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    @Override
	public String guardaRolUsuario(Map params)
			throws Exception {
		logger.debug("Params guardaRolUsuario: "+ params);
		Map<String, Object> mapResult = ejecutaSP(new GuardaRolUsuario(getDataSource()), params);
		
		return (String) mapResult.get("PV_TITLE_O");
	}
	
	protected class GuardaRolUsuario extends StoredProcedure {

		protected GuardaRolUsuario(DataSource dataSource) {
			super(dataSource, "PKG_GENERA_USUARIO.P_MOV_SISROL_USUARIO");
			
			declareParameter(new SqlParameter("PV_ACCION_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_CDUSUARI_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_CDSISROL_I", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String guardaImpresorasUsuario(String cdusuario,
										  String ip,
										  String tipo, 
										  String descripcion,
										  String swactivo,
										  String impAsig,
										  String nombre)
			throws Exception {
		HashMap<String, String> params=new HashMap<String, String>();
		
		params.put("PV_CDUSUARIO_I", cdusuario);
		params.put("PV_IP_I", ip);
		params.put("PV_TIPO_I", tipo);
		params.put("PV_DESCRIPCION_I", descripcion);
		params.put("PV_SWACTIVO_I", swactivo);
		params.put("PV_IMP_ASIG_I", impAsig);
		params.put("PV_NOMBRE_I", nombre);
		logger.debug("Params guardaImpresorasUsuario: "+ params);
		Map<String, Object> mapResult = ejecutaSP(new GuardaImpresorasUsuario(getDataSource()), params);
		logger.debug("## termino");
		return (String) mapResult.get("PV_TITLE_O");
	}
	
	protected class GuardaImpresorasUsuario extends StoredProcedure {

		protected GuardaImpresorasUsuario(DataSource dataSource) {
			super(dataSource, "Pkg_Tabapoyo.P_INSERTA_IMPRESORA_USUARIO");
			
			declareParameter(new SqlParameter("PV_CDUSUARIO_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_IP_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_TIPO_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_NOMBRE_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_DESCRIPCION_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_SWACTIVO_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_IMP_ASIG_I", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public String habilitaDeshabilitaImpresora(String pv_habilita,
										  String pv_impresora_i,
										  String pv_CdUsuari_i)
			throws Exception {
		HashMap<String, String> params=new HashMap<String, String>();
		
		params.put("pv_habilita",pv_habilita );
		params.put("pv_impresora_i", pv_impresora_i);
		params.put("pv_CdUsuari_i", pv_CdUsuari_i);
		logger.debug("Params habilitaDeshabilitaImpresora: "+ params);
		Map<String, Object> mapResult = ejecutaSP(new HabilitaDeshabilitaImpresora(getDataSource()), params);
		
		return (String) mapResult.get("PV_TITLE_O");
	}
	
	protected class HabilitaDeshabilitaImpresora extends StoredProcedure {

		protected HabilitaDeshabilitaImpresora(DataSource dataSource) {
			super(dataSource, "Pkg_Tabapoyo.P_HABILITA_IMPRESORA");
			
			declareParameter(new SqlParameter("pv_habilita", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_impresora_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_CdUsuari_i", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String insertaActualizaImpresora(String pv_nombre_i,
										  String pv_ip_i,
										  String pv_tipo_i,
										  String pv_descripcion_i,
										  String pv_swactivo_i)
			throws Exception {
		HashMap<String, String> params=new HashMap<String, String>();
		
		params.put("pv_nombre_i",pv_nombre_i );
		params.put("pv_ip_i", pv_ip_i);
		params.put("pv_tipo_i", pv_tipo_i);
		params.put("pv_descripcion_i", pv_descripcion_i);
		params.put("pv_swactivo_i", pv_swactivo_i);
		logger.debug("Params habilitaDeshabilitaImpresora: "+ params);
		Map<String, Object> mapResult = ejecutaSP(new InsertaActualizaImpresora(getDataSource()), params);
		
		return (String) mapResult.get("PV_TITLE_O");
	}
	
	protected class InsertaActualizaImpresora extends StoredProcedure {

		protected InsertaActualizaImpresora(DataSource dataSource) {
			super(dataSource, "Pkg_Tabapoyo.P_INSERTA_ACTUALIZA_IMPRESORA");
			
			declareParameter(new SqlParameter("pv_nombre_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ip_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_tipo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_descripcion_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swactivo_i", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
			compile();
		}
	}

	
	@Override
	public List<Map<String, String>> obtieneProductosAgente(Map params) throws Exception {
		Map<String, Object> resultado = ejecutaSP(new ObtieneProductosAgente(getDataSource()), params);
		return (List<Map<String, String>>) resultado.get("PV_REGISTRO_O");
	}
	
	protected class ObtieneProductosAgente extends StoredProcedure {
		
		protected ObtieneProductosAgente(DataSource dataSource) {
			super(dataSource, "PKG_GENERA_USUARIO.P_GET_RAMOS_AGENTE");
			declareParameter(new SqlParameter("PV_CDAGENTE_I", OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CDRAMO" , "DSRAMO" , "TIENE_CDRAMO"
			};
			declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> obtieneImpresorasUsuario(String cdusuario) throws Exception{
		;
		Map<String, String> params=new HashMap<String, String>();
		params.put("pv_cdusuario_i", cdusuario);
		
		Map<String,Object> resultado=ejecutaSP(new ObtieneImpresorasUsuario(getDataSource()),params);
		return (List<Map<String, String>>) resultado.get("PV_REGISTRO_O");
	}
	
	protected class ObtieneImpresorasUsuario extends StoredProcedure{
		protected ObtieneImpresorasUsuario(DataSource dataSource){
			super(dataSource, "Pkg_Tabapoyo.P_GET_IMPRESORAS_USR");
			declareParameter(new SqlParameter("pv_cdusuario_i", OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"IMPRESORA" ,"IP","TIPO", "DESCRIPCION" , "DISPONIBLE","ALTA"
			};
			declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	@Override
	public String guardaProductoAgente(Map params)
			throws Exception {
		logger.debug("Params guardaProductoAgente: "+ params);
		Map<String, Object> mapResult = ejecutaSP(new GuardaProductoAgente(getDataSource()), params);
		
		return (String) mapResult.get("PV_TITLE_O");
	}
	
	protected class GuardaProductoAgente extends StoredProcedure {
		
		protected GuardaProductoAgente(DataSource dataSource) {
			super(dataSource, "PKG_GENERA_USUARIO.P_MOV_MAGECOM");
			
			declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_CDAGENTE_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_ACCION_I", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void guardarSesion(String idSesion,String cdusuari,String cdsisrol,String userAgent,boolean esMovil,Date fecha) throws Exception
	{
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("idSesion"  , idSesion);
		params.put("cdusuari"  , cdusuari);
		params.put("cdsisrol"  , cdsisrol);
		params.put("userAgent" , userAgent);
		params.put("esMovil" , esMovil?"S":"N");
		params.put("fecha"     , fecha);
		ejecutaSP(new GuardarSesion(getDataSource()),params);
	}
	
	protected class GuardarSesion extends StoredProcedure {

		protected GuardarSesion(DataSource dataSource)
		{
			super(dataSource, "PKG_SATELITES.P_INSERTA_SESION");
			
			declareParameter(new SqlParameter("idSesion"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuari"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("userAgent" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("esMovil"   , OracleTypes.CHAR));
			declareParameter(new SqlParameter("fecha"     , OracleTypes.TIMESTAMP));
	        declareParameter(new SqlOutParameter("PV_MSG_ID_O" , OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("PV_TITLE_O"  , OracleTypes.VARCHAR));
			compile();
		}
	}
    
	/*
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
	*/
	
}