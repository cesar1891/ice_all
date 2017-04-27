package mx.com.gseguros.wizard.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.dao.impl.GenericMapper;
import mx.com.gseguros.wizard.configuracion.producto.conceptosCobertura.model.ConceptosCoberturaVO;
import mx.com.gseguros.wizard.configuracion.producto.datosFijos.model.DatoFijoVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.reglaValidacion.model.ReglaValidacionVO;
import mx.com.gseguros.wizard.configuracion.producto.reglanegocio.model.ReglaNegocioVO;
import mx.com.gseguros.wizard.configuracion.producto.rol.model.RolAtributoVariableVO;
import mx.com.gseguros.wizard.configuracion.producto.rol.model.RolVO;
import mx.com.gseguros.wizard.configuracion.producto.tipoObjeto.model.DatoVariableObjetoVO;
import mx.com.gseguros.wizard.dao.WizardDAO;
import mx.com.gseguros.wizard.model.MensajesVO;
import oracle.jdbc.OracleTypes;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class WizardDAOImpl extends AbstractManagerDAO implements WizardDAO {

	public List<ReglaNegocioVO> obtenerValidaciones()throws Exception{
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		Map<String, Object> resultado = ejecutaSP(new ObtieneValidaciones(getDataSource()), params);
		
		return (List<ReglaNegocioVO>) resultado.get("pv_registro_o");
	}

	public List<LlaveValorVO> obtenerValidacionesLlave()throws Exception{
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		Map<String, Object> resultado = ejecutaSP(new ObtieneValidacionesLlave(getDataSource()), params);
		
		return (List<LlaveValorVO>) resultado.get("pv_registro_o");
	}
	
	public List<ReglaNegocioVO> obtenerConceptoTarificacion()throws Exception{
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		Map<String, Object> resultado = ejecutaSP(new ObtieneConceptoTarif(getDataSource()), params);
		
		return (List<ReglaNegocioVO>) resultado.get("pv_registro_o");
	}

	public List<ReglaNegocioVO> obtenerCondicion()throws Exception{
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		Map<String, Object> resultado = ejecutaSP(new ObtenerCondicion(getDataSource()), params);
		
		return (List<ReglaNegocioVO>) resultado.get("pv_registro_o");
	}

	public List<ReglaNegocioVO> obtenerAutorizacion()throws Exception{
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		Map<String, Object> resultado = ejecutaSP(new ObtenerAutorizacion(getDataSource()), params);
		
		return (List<ReglaNegocioVO>) resultado.get("pv_registro_o");
	}

	public List<ReglaNegocioVO> obtenerVarTemp()throws Exception{
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		Map<String, Object> resultado = ejecutaSP(new ObtenerVarTemp(getDataSource()), params);
		
		return (List<ReglaNegocioVO>) resultado.get("pv_registro_o");
	}
	
	
	public List<ConceptosCoberturaVO> obtieneConceptosPorCobertura(Map<String, String> params) throws Exception{
    	
		Map<String, Object> resultado = ejecutaSP(new ObtieneConceptosPorCobertura(getDataSource()), params);
		
		return (List<ConceptosCoberturaVO>) resultado.get("pv_registro_o");
		
    }
	
	public List<LlaveValorVO> obtieneListaPeriodos(Map<String, String> params) throws Exception{
    	
		Map<String, Object> resultado = ejecutaSP(new ObtieneListaPeriodos(getDataSource()), params);
		
		return (List<LlaveValorVO>) resultado.get("pv_registro_o");
		
    }
	
	public List<LlaveValorVO> obtieneListaCoberturas(Map<String, String> params)throws Exception{
    	
		Map<String, Object> resultado = ejecutaSP(new ObtieneListaCoberturas(getDataSource()), params);
		
		return (List<LlaveValorVO>) resultado.get("pv_registro_o");
		
    }
	
	public List<LlaveValorVO> obtieneListaConceptos(Map<String, String> params)throws Exception{
    	
		Map<String, Object> resultado = ejecutaSP(new ObtieneListaConceptos(getDataSource()), params);
		
		return (List<LlaveValorVO>) resultado.get("pv_registro_o");
		
    }

	public List<LlaveValorVO> obtieneTipoConceptos(Map<String, Object> params)throws Exception{
		
		Map<String, Object> resultado = ejecutaSP(new ObtieneTipoConceptos(getDataSource()), params);
		
		return (List<LlaveValorVO>) resultado.get("pv_registro_o");
		
	}
	
	public List<LlaveValorVO> obtieneListaComportamientos(Map<String, String> params)throws Exception{
		
		Map<String, Object> resultado = ejecutaSP(new ObtieneListaComportamientos(getDataSource()), params);
		
		return (List<LlaveValorVO>) resultado.get("pv_registro_o");
		
	}
	
	public List<LlaveValorVO> obtieneListaCondiciones(Map<String, String> params)throws Exception{
		
		Map<String, Object> resultado = ejecutaSP(new ObtieneListaCondiciones(getDataSource()), params);
		
		return (List<LlaveValorVO>) resultado.get("pv_registro_o");
		
	}

	public List<LlaveValorVO> obtieneListaCondicionesLlave(Map<String, String> params)throws Exception{
		
		Map<String, Object> resultado = ejecutaSP(new ObtieneListaCondicionesLlave(getDataSource()), params);
		
		return (List<LlaveValorVO>) resultado.get("pv_registro_o");
		
	}
	
	public void agregarConceptoPorCobertura(Map<String, String> params)throws Exception{
		
		ejecutaSP(new AgregarConceptoPorCobertura(getDataSource()), params);
		
		return;
		
	}

	public void insertarDatoFijo(Map<String, Object> params) throws Exception{
		
		ejecutaSP(new InsertarDatoFijo(getDataSource()), params);
		
		return;
		
	}

	public void eliminaConceptosPorCobertura(Map<String, String> params)throws Exception{
		
		ejecutaSP(new EliminaConceptosPorCobertura(getDataSource()), params);
		
		return;
		
	}

	public void borraTatriObjeto(Map<String, Object> params) throws Exception{
		
		ejecutaSP(new BorraTatriObjeto(getDataSource()), params);
		
		return;
		
	}

	public List<LlaveValorVO> catalogoAtributosVariablesJson(Map<String, Object> params)throws Exception{
		
		Map<String, Object> resultado = ejecutaSP(new CatalogoAtributosVariables(getDataSource()), params);
		
		return (List<LlaveValorVO>) resultado.get("pv_registro_o");
		
	}
	
	public List<LlaveValorVO> obtieneCatalogoRoles(Map<String, String> params)throws Exception{
		
		Map<String, Object> resultado = ejecutaSP(new ObtieneCatalogoRoles(getDataSource()), params);
		
		return (List<LlaveValorVO>) resultado.get("pv_registro_o");
		
	}

	public List<RolAtributoVariableVO> atributosVariablesRol(Map<String, String> params) throws Exception{
		
		Map<String, Object> resultado = ejecutaSP(new AtributosVariablesRol(getDataSource()), params);
		
		return (List<RolAtributoVariableVO>) resultado.get("pv_registro_o");
		
	}

	public RolVO obtieneRolAsociado(Map<String, String> params) throws Exception{
		RolVO rol =  null;
		Map<String, Object> resultado = ejecutaSP(new ObtieneRolAsociado(getDataSource()), params);
		List<RolVO> roles = (List<RolVO>) resultado.get("pv_registro_o");
		if(roles!=null) rol =  roles.get(0);
		return rol;
	}

	public void agregaRolCatalogo(Map<String, String> params) throws Exception{
		ejecutaSP(new AgregaRolCatalogo(getDataSource()), params);
		return;
	}

	public void agregaAtributoVariableRol(Map<String, Object> params) throws Exception{
		ejecutaSP(new AgregaAtributoVariableRol(getDataSource()), params);
		return;
	}

	public List<LlaveValorVO> catalogoTipoObjetoJson(Map<String, String> params) throws Exception{
		
		Map<String, Object> resultado = ejecutaSP(new CatalogoTipoObjeto(getDataSource()), params);
		
		return (List<LlaveValorVO>) resultado.get("pv_registro_o");
		
	}

	public List<DatoVariableObjetoVO> listaDatosVariablesObjetos(Map<String, String> params) throws Exception{
		
		Map<String, Object> resultado = ejecutaSP(new DatosVarObjetos(getDataSource()), params);
		
		return (List<DatoVariableObjetoVO>) resultado.get("pv_registro_o");
		
	}

	public List<DatoFijoVO> listaDatosFijos(Map<String, String> params) throws Exception{
		
		Map<String, Object> resultado = ejecutaSP(new ListaDatosFijos(getDataSource()), params);
		
		return (List<DatoFijoVO>) resultado.get("pv_registro_o");
		
	}

	public List<ReglaNegocioVO> obtenerVariablesTemporalesAsociadasAlProducto(Map<String, String> params) throws Exception{
		
		Map<String, Object> resultado = ejecutaSP(new ObtenerVarTemProd(getDataSource()), params);
		
		return (List<ReglaNegocioVO>) resultado.get("pv_registro_o");
		
	}

	public List<ReglaValidacionVO> obtieneReglasDeValidacion(Map<String, String> params) throws Exception{
		
		Map<String, Object> resultado = ejecutaSP(new ObtenerReglaValidacion(getDataSource()), params);
		
		return (List<ReglaValidacionVO>) resultado.get("pv_registro_o");
		
	}

	public List<LlaveValorVO> obtieneListaBloques(Map<String, String> params) throws Exception{
		
		Map<String, Object> resultado = ejecutaSP(new ObtieneListaBloques(getDataSource()), params);
		
		return (List<LlaveValorVO>) resultado.get("pv_registro_o");
		
	}

	public List<LlaveValorVO> catalogoBloque(Map<String, Object> params) throws Exception{
		
		Map<String, Object> resultado = ejecutaSP(new CatalogoBloque(getDataSource()), params);
		
		return (List<LlaveValorVO>) resultado.get("pv_registro_o");
		
	}

	public List<LlaveValorVO> catalogoCampo(Map<String, Object> params) throws Exception{
		
		Map<String, Object> resultado = ejecutaSP(new CatalogoCampo(getDataSource()), params);
		
		return (List<LlaveValorVO>) resultado.get("pv_registro_o");
		
	}

	public List<LlaveValorVO> obtenerListaTablasApoyo(Map<String, String> params) throws Exception{
		
		Map<String, Object> resultado = ejecutaSP(new ObtenerListaTablasApoyo(getDataSource()), params);
		
		return (List<LlaveValorVO>) resultado.get("pv_registro_o");
		
	}
	
	public MensajesVO eliminaAtributoVariableRol(Map<String, Object> params) throws Exception{
		MensajesVO mensaje =  new MensajesVO();
		Map<String, Object> resultado = ejecutaSP(new EliminaAtributoVariableRol(getDataSource()), params);
		
		mensaje.setMsgId((String)resultado.get("pv_msg_id_o"));
		mensaje.setTitle((String)resultado.get("pv_title_o"));
		return mensaje;
		
	}
	public MensajesVO borraTipoObjInciso(Map<String, Object> params) throws Exception{
		MensajesVO mensaje =  new MensajesVO();
		Map<String, Object> resultado = ejecutaSP(new BorraTipoObjInciso(getDataSource()), params);
		
		mensaje.setMsgId((String)resultado.get("pv_msg_id_o"));
		mensaje.setTitle((String)resultado.get("pv_title_o"));
		return mensaje;
		
	}

	public WrapperResultados eliminaRol(Map<String, Object> params) throws Exception{
		WrapperResultados mensaje =  new WrapperResultados();
		Map<String, Object> resultado = ejecutaSP(new EliminaRol(getDataSource()), params);
		
		mensaje.setMsgId((String)resultado.get("pv_msg_id_o"));
		mensaje.setMsgTitle((String)resultado.get("pv_title_o"));
		return mensaje;
		
	}
	
	public void agregarRolInciso(Map<String, Object> params) throws Exception{
		ejecutaSP(new AgregarRolInciso(getDataSource()), params);
		return ;
	}

	public void agregarAtributoVariableCatalogo(Map<String, Object> params) throws Exception{
		ejecutaSP(new AgregarAtributoVariableCatalogo(getDataSource()), params);
		return ;
	}

	public void asociarReglasDeValidacion(Map<String, String> params) throws Exception{
		ejecutaSP(new AsociarReglasDeValidacion(getDataSource()), params);
		return ;
	}

	public void insertarValidacion(Map<String, Object> params) throws Exception{
		ejecutaSP(new InsertarValidacion(getDataSource()), params);
		return ;
	}

	public void insertarConceptoTarif(Map<String, Object> params) throws Exception{
		ejecutaSP(new InsertarConceptoTarif(getDataSource()), params);
		return ;
	}

	public void insertarVariableTemp(Map<String, Object> params) throws Exception{
		ejecutaSP(new InsertarVariableTemp(getDataSource()), params);
		return ;
	}

	public void insertarAutorizacion(Map<String, Object> params) throws Exception{
		ejecutaSP(new InsertarAutorizacion(getDataSource()), params);
		return ;
	}

	public void insertarCondicion(Map<String, Object> params) throws Exception{
		ejecutaSP(new InsertarCondicion(getDataSource()), params);
		return ;
	}

	public void agregarObjCatalogo(Map<String, Object> params) throws Exception{
		ejecutaSP(new AgregarObjCatalogo(getDataSource()), params);
		return ;
	}

	public void agregaDatosVarObj(Map<String, Object> params) throws Exception{
		ejecutaSP(new AgregaDatosVarObj(getDataSource()), params);
		return ;
	}

	public void agregaTipoObjInciso(Map<String, Object> params) throws Exception{
		ejecutaSP(new AgregaTipoObjInciso(getDataSource()), params);
		return ;
	}

	public void eliminaReglasDeValidacion(Map<String, String> params) throws Exception{
		ejecutaSP(new EliminaReglasDeValidacion(getDataSource()), params);
		return ;
	}

	public void asociarVariablesDelProducto(Map<String, Object> params) throws Exception{
		ejecutaSP(new AsociarVariablesDelProducto(getDataSource()), params);
		return ;
	}
	public void desasociarVariablesDelProducto(Map<String, Object> params) throws Exception{
		ejecutaSP(new DesasociarVariablesDelProducto(getDataSource()), params);
		return ;
	}
	public void borraVarTmp(Map<String, Object> params) throws Exception{
		ejecutaSP(new BorraVarTmp(getDataSource()), params);
		return ;
	}
	
	public MensajesVO tieneHijosAtributoVariableRol(Map<String, Object> params) throws Exception{
		MensajesVO mensaje =  new MensajesVO();
		Map<String, Object> resultado = ejecutaSP(new TieneHijosAtributoVariableRol(getDataSource()), params);
		
		mensaje.setMsgText((String)resultado.get("pv_existe_o"));
		mensaje.setMsgId((String)resultado.get("pv_msg_id_o"));
		mensaje.setTitle((String)resultado.get("pv_title_o"));
		return mensaje;
		
	}

	public MensajesVO tieneHijosAtributoVariableObjeto(Map<String, Object> params) throws Exception{
		MensajesVO mensaje =  new MensajesVO();
		Map<String, Object> resultado = ejecutaSP(new TieneHijosAtributoVariableObjeto(getDataSource()), params);
		
		mensaje.setMsgText((String)resultado.get("pv_existe_o"));
		mensaje.setMsgId((String)resultado.get("pv_msg_id_o"));
		mensaje.setTitle((String)resultado.get("pv_title_o"));
		return mensaje;
		
	}

		
/*********************************************************************************************************
 * *******************************************************************************************************
 * Clases internas	
 * @author HectorLT
 *
 */
	
    protected class ObtieneValidaciones extends StoredProcedure {
    	
    	protected ObtieneValidaciones(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_OBTIENE_TVALIDAC");
    		
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ValidacionMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    protected class ValidacionMapper implements RowMapper {
    	
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		
    		ReglaNegocioVO regla = new ReglaNegocioVO();
    		regla.setNombre(rs.getString("CDVALIDA"));
    		regla.setMensaje(rs.getString("DSMSGERR"));
    		regla.setCodigoExpresion(rs.getString("CDEXPRES"));
    		regla.setDescripcion(rs.getString("DSVALIDA"));
    		regla.setDescripcionTipo(rs.getString("CDTIPO"));
    		
    		return regla;
    	}
    }
    
    protected class ObtieneValidacionesLlave extends StoredProcedure {
    	
    	protected ObtieneValidacionesLlave(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_OBTIENE_TVALIDAC");
    		
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ValidacionLlaveMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    protected class ValidacionLlaveMapper implements RowMapper {
    	
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		
    		LlaveValorVO regla = new LlaveValorVO();
    		regla.setKey(rs.getString("CDVALIDA"));
    		regla.setValue(rs.getString("DSVALIDA"));
    		
    		return regla;
    	}
    }

    protected class CatalogoAtributosVariables extends StoredProcedure {
    	
    	protected CatalogoAtributosVariables(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_ATRIBVARROLINC");
    		
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new CatAtrVarMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    protected class CatAtrVarMapper implements RowMapper {
    	
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		
    		LlaveValorVO regla = new LlaveValorVO();
    		regla.setKey(rs.getString(1));
    		regla.setValue(rs.getString(2));
    		
    		return regla;
    	}
    }

    protected class ObtieneConceptoTarif extends StoredProcedure {
    	
    	protected ObtieneConceptoTarif(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_OBTCONCTARI");
    		
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ConTarifMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    protected class ConTarifMapper implements RowMapper {
    	
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		
    		ReglaNegocioVO regla = new ReglaNegocioVO();
    		regla.setNombre(rs.getString("CDCONTAR"));
    		regla.setDescripcion(rs.getString("DSCONTAR"));
    		regla.setTipo(rs.getString("CDTIPCON"));
    		regla.setDescripcionTipo(rs.getString("DSTIPCON"));
    		regla.setCodigoExpresion(rs.getString("CDEXPRES"));
    		
    		return regla;
    	}
    }

    protected class ObtenerCondicion extends StoredProcedure {
    	
    	protected ObtenerCondicion(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_OBTIENECONDIC");
    		
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new CondicionMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    protected class CondicionMapper implements RowMapper {
    	
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		
    		ReglaNegocioVO regla = new ReglaNegocioVO();
    		regla.setNombre(rs.getString("CDCONDIC"));
    		regla.setDescripcion(rs.getString("DSCONDIC"));
    		regla.setCodigoExpresion(rs.getString("CDEXPRES"));
    		
    		return regla;
    	}
    }

    protected class ObtenerAutorizacion extends StoredProcedure {
    	
    	protected ObtenerAutorizacion(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_OBTIENEAUTO");
    		
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new AutorizacionMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    protected class AutorizacionMapper implements RowMapper {
    	
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		
    		ReglaNegocioVO regla = new ReglaNegocioVO();
    		regla.setNombre(rs.getString("CDMOTIVO"));
    		regla.setDescripcion(rs.getString("DSMOTIVO"));
    		regla.setNivel(rs.getString("OTNIVEL"));
    		regla.setCodigoExpresion(rs.getString("CDEXPRES"));
    		
    		return regla;
    	}
    }

    protected class ObtenerVarTemp extends StoredProcedure {
    	
    	protected ObtenerVarTemp(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_OBTIENE_TVARIATM");
    		
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new VarTempMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    protected class VarTempMapper implements RowMapper {
    	
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		
    		ReglaNegocioVO regla = new ReglaNegocioVO();
    		regla.setNombre(rs.getString("CDVARIAT"));
    		regla.setDescripcion(rs.getString("DSVARIAT"));
    		regla.setCodigoExpresion(rs.getString("CDEXPRES"));
    		
    		return regla;
    	}
    }

    protected class ObtieneConceptosPorCobertura extends StoredProcedure {
    	
    	protected ObtieneConceptosPorCobertura(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_LOADCONCEPGLOB");
    		
    		declareParameter(new SqlParameter("PV_CDRAMO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDTIPSIT", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDGARANT", OracleTypes.VARCHAR));

    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ConcpetoPorCoberturaMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    protected class ConcpetoPorCoberturaMapper implements RowMapper {
    	
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		
    		ConceptosCoberturaVO concepto = new ConceptosCoberturaVO();
    		concepto.setCodigoPeriodo(rs.getString("CDPERIOD"));
    		concepto.setDescripcionPeriodo(rs.getString("DSFECHAS"));
    		concepto.setOrden(rs.getString("ORDEN"));
    		concepto.setCodigoCobertura(rs.getString("CDGARANT"));
    		concepto.setDescripcionCobertura(rs.getString("DSGARANT"));
    		concepto.setCodigoConcepto(rs.getString("CDCONTAR"));
    		concepto.setDescripcionConcepto(rs.getString("DSCONTAR"));
    		concepto.setCodigoComportamiento(rs.getString("OTTIPO"));
    		concepto.setDescripcionComportamiento(rs.getString("DSCOMPOR"));
    		concepto.setCodigoCondicion(rs.getString("CDCONDIC"));
    		concepto.setDescripcionCondicion(rs.getString("DSCONDIC"));
    		concepto.setCdtipcon(rs.getString("CDTIPCON"));
    		concepto.setDstipcon(rs.getString("DSTIPCON"));
    		concepto.setCdexpres(rs.getString("CDEXPRES"));
    		
    		return concepto;
    	}
    }

    protected class ObtieneListaPeriodos extends StoredProcedure {
    	
    	protected ObtieneListaPeriodos(DataSource dataSource) {
    		super(dataSource, "PKG_LISTAS.P_OBTIENE_PERIODO");
    		
    		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ListasPeriodosMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    protected class ListasPeriodosMapper implements RowMapper {
    	
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		
    		LlaveValorVO llave =  new LlaveValorVO();
    		llave.setKey(rs.getString(1));
    		llave.setValue(rs.getString(2));
    		
    		return llave;
    	}
    }

    protected class ObtieneListaCoberturas extends StoredProcedure {
    	
    	protected ObtieneListaCoberturas(DataSource dataSource) {
    		super(dataSource, "PKG_LISTAS.P_COBERTURA_RAMO");
    		
    		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ListasPeriodosMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }

    protected class ObtieneListaConceptos extends StoredProcedure {
    	
    	protected ObtieneListaConceptos(DataSource dataSource) {
    		super(dataSource, "PKG_LISTAS.P_CONCEPTO_TARIFICA");
    		
    		declareParameter(new SqlParameter("pv_tipcon", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ListasPeriodosMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }

    protected class ObtieneTipoConceptos extends StoredProcedure {
    	
    	protected ObtieneTipoConceptos(DataSource dataSource) {
    		super(dataSource, "PKG_LISTAS.P_CONCEPTO_PRODUCTO");
    		
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ListasPeriodosMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    
protected class ObtieneListaComportamientos extends StoredProcedure {
    	
    	protected ObtieneListaComportamientos(DataSource dataSource) {
    		super(dataSource, "PKG_TABAPOYO.P_OBTENER_VALORATT_TODO");
    		
    		declareParameter(new SqlParameter("PI_TABLA", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_ATRIB_DESC", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_NOM_CLAVE01", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_NOM_CLAVE02", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_VAL_CLAVE02", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_NOM_CLAVE03", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_VAL_CLAVE03", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_NOM_CLAVE04", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_VAL_CLAVE04", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_NOM_CLAVE05", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_VAL_CLAVE05", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ListasPeriodosMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }

protected class ObtieneListaCondiciones extends StoredProcedure {
	
	protected ObtieneListaCondiciones(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_CONDICCOBERT");
		
		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ListasPeriodosMapper()));
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}
protected class ObtieneListaCondicionesLlave extends StoredProcedure {
	
	protected ObtieneListaCondicionesLlave(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_CONDICCOBERT");
		
		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new CondicionesLlaveMapper()));
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class CondicionesLlaveMapper implements RowMapper {
	
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		LlaveValorVO regla = new LlaveValorVO();
		regla.setKey(rs.getString("CDCONDIC"));
		regla.setValue(rs.getString("DSCONDIC"));
		return regla;
	}
}

protected class ObtieneCatalogoRoles extends StoredProcedure {
	
	protected ObtieneCatalogoRoles(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.p_getrolesinciso");
		
		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ListasPeriodosMapper()));
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class AtributosVariablesRol extends StoredProcedure {
	
	protected AtributosVariablesRol(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_GETATRIBVARROLINC");
		
		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDTIPSIT_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDNIVEL_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDROL_I", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new AtrVarRolMapper()));
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class AtrVarRolMapper implements RowMapper {
	
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		RolAtributoVariableVO atr =  new RolAtributoVariableVO();
		atr.setCodigoAtributoVariable(rs.getString("CDATRIBU"));
		atr.setDescripcionAtributoVariable(rs.getString("DSATRIBU"));
		atr.setSwitchObligatorio(rs.getString("SWOBLIGA"));
		atr.setOttabval(rs.getString("OTTABVAL"));
		atr.setDescripcionListaDeValores(rs.getString("DSTABLA"));
		atr.setApareceCotizador(rs.getString("SWCOTIZA"));
		atr.setModificaCotizador(rs.getString("SWCOTUPD"));
		atr.setDatoComplementario(rs.getString("SWDATCOM"));
		atr.setObligatorioComplementario(rs.getString("SWCOMOBL"));
		atr.setModificableComplementario(rs.getString("SWCOMUPD"));
		atr.setApareceEndoso(rs.getString("SWENDOSO"));
		atr.setObligatorioEndoso(rs.getString("SWENDOBL"));
		atr.setModificaEndoso(rs.getString("SWENDUPD"));
		atr.setCodigoPadre(rs.getString("CDATRIBU_PADRE"));
		atr.setDescripcionPadre(rs.getString("DSATRIBU_PADRE"));
		atr.setOrden(rs.getString("NMORDEN"));
		atr.setAgrupador(rs.getString("NMAGRUPA"));
		atr.setCodigoCondicion(rs.getString("CDCONDICVIS"));
		atr.setDescripcionCondicion(rs.getString("DSCONDICVIS"));
		atr.setRetarificacion(rs.getString("SWTARIFI"));
		
		return atr;
	}
}

protected class AgregaRolCatalogo extends StoredProcedure {
	
	protected AgregaRolCatalogo(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_INSERTTROL");
		
		declareParameter(new SqlParameter("PV_CDROL_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_DSROL_I", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class AgregaAtributoVariableRol extends StoredProcedure {
	
	protected AgregaAtributoVariableRol(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_INSATRIBROLINC");
		
		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDNIVEL_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDROL_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDATRIBU_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_SWOBLIGA_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDTIPSIT_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_OTTABVAL_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_SWCOTIZA_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_SWCOTUPD_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_SWDATCOM_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_SWCOMOBL_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_SWCOMUPD_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_SWENDOSO_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_SWENDOBL_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_SWENDUPD_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDATRIBU_PADRE_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_NMORDEN_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_NMAGRUPA_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDCONDICVIS_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDEXPRES_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_SWTARIFI_i", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class ObtieneRolAsociado extends StoredProcedure {
	
	protected ObtieneRolAsociado(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_LOADROLINCISO");
		
		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDTIPSIT_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDROL_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDNIVEL_I", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new RolAsociadoMapper()));
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class RolAsociadoMapper implements RowMapper {
	
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		RolVO rol =  new RolVO();
		rol.setCodigoRol(rs.getString("CDROL"));
		rol.setDescripcionRol(rs.getString("DSROL"));
		rol.setCodigoRamo(rs.getString("CDRAMO"));
		rol.setCodigoComposicion(rs.getString("CDCOMPO"));
		rol.setDescripcionComposicion(rs.getString("DSCOMPO"));
		rol.setNumeroMaximo(rs.getString("NMAXIMO"));
		rol.setSwitchDomicilio(rs.getString("SWDOMICI"));
		
		return rol;
	}
}

protected class CatalogoTipoObjeto extends StoredProcedure {
	
	protected CatalogoTipoObjeto(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_OBTIENE_TIPO_OBJETO");
		
		declareParameter(new SqlParameter("PV_CDTIPOBJ_I", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ListasPeriodosMapper()));
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class DatosVarObjetos extends StoredProcedure {
	
	protected DatosVarObjetos(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_OBTIENE_TIPO_OBJ_ATRIBUTOS");
		
		declareParameter(new SqlParameter("PV_CDTIPOBJ_I", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatVarObjMapper()));
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class DatVarObjMapper implements RowMapper {
	
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		DatoVariableObjetoVO dat =  new DatoVariableObjetoVO();
		dat.setCodigoAtributoVariable(rs.getString("CDATRIBU"));
		dat.setDescripcionAtributoVariable(rs.getString("DSATRIBU"));
		dat.setSwitchObligatorio(rs.getString("SWOBLIGA"));
		dat.setCodigoTabla(rs.getString("OTTABVAL"));
		dat.setDescripcionTabla(rs.getString("DSTABLA"));
		dat.setCodigoFormato(rs.getString("SWFORMAT"));
		dat.setMaximo(rs.getString("NMLMAX"));
		dat.setMinimo(rs.getString("NMLMIN"));
		dat.setEmision(rs.getString("SWPRODUC"));
		dat.setEndoso(rs.getString("SWSUPLEM"));
		dat.setRetarificacion(rs.getString("SWACTUAL"));
		dat.setCodigoObjeto(rs.getString("CDTIPOBJ"));
		dat.setCodigoPadre(rs.getString("CDATRIBU_PADRE"));
		dat.setOrden(rs.getString("NMORDEN"));
		dat.setAgrupador(rs.getString("NMAGRUPA"));
		dat.setCodigoCondicion(rs.getString("CDCONDICVIS"));
		dat.setDsAtributoPadre(rs.getString("DSATRIBU_PADRE"));
		dat.setDsCondicion(rs.getString("DSCONDICVIS"));
		dat.setApareceCotizador(rs.getString("SWCOTIZA"));
		dat.setDatoComplementario(rs.getString("SWDATCOM"));
		dat.setObligatorioComplementario(rs.getString("SWCOMOBL"));
		dat.setModificableComplementario(rs.getString("SWCOMUPD"));
		dat.setApareceEndoso(rs.getString("SWENDOSO"));
		dat.setObligatorioEndoso(rs.getString("SWENDOBL"));
		
		return dat;
	}
}

	protected class ListaDatosFijos extends StoredProcedure {
	
	protected ListaDatosFijos(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_OBTTCMPFFLD");
		
		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatFijoMapper()));
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class DatFijoMapper implements RowMapper {
	
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		DatoFijoVO dat =  new DatoFijoVO();
		dat.setCodigoRamo(rs.getString("CDRAMO"));
		dat.setCodigoFuncio(rs.getString("CDFUNCIO"));
		dat.setDescripcionBloque(rs.getString("CDBLOQUE"));
		dat.setDescripcionCampo(rs.getString("CDCAMPO"));
		dat.setCodigoExpresion(rs.getString("CDEXPRES"));
		
		return dat;
	}
}

protected class ObtenerVarTemProd extends StoredProcedure {
	
	protected ObtenerVarTemProd(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_OBTIENE_RAMO_VARIABLE");
		
		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new VarTemProMapper()));
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class VarTemProMapper implements RowMapper {
	
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		ReglaNegocioVO regla = new ReglaNegocioVO();
		regla.setNombre(rs.getString("CDVARIAT"));
		regla.setDescripcion(rs.getString("DSVARIAT"));
		regla.setCodigoExpresion(rs.getString("CDEXPRES"));
		
		return regla;
	}
}


protected class ObtieneListaBloques extends StoredProcedure {
	
	protected ObtieneListaBloques(DataSource dataSource) {
		super(dataSource, "PKG_LISTAS.P_BLOQUES");
		
		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new BloqueMapper()));
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class BloqueMapper implements RowMapper {
	
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		LlaveValorVO bloque = new LlaveValorVO();
		bloque.setKey(rs.getString("CDBLOQUE"));
		bloque.setValue(rs.getString("DSBLOQUE"));
		
		return bloque;
	}
}

protected class CatalogoBloque extends StoredProcedure {
	
	protected CatalogoBloque(DataSource dataSource) {
		super(dataSource, "PKG_LISTAS.P_BLOQUES_FIJOS");
		
		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new BloqueFijoMapper()));
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class BloqueFijoMapper implements RowMapper {
	
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		LlaveValorVO bloque = new LlaveValorVO();
		bloque.setNick(rs.getString(1));
		bloque.setKey(rs.getString(2));
		bloque.setValue(rs.getString(3));
		
		return bloque;
	}
}

protected class CatalogoCampo extends StoredProcedure {
	
	protected CatalogoCampo(DataSource dataSource) {
		super(dataSource, "PKG_LISTAS.P_CAMPO_BLOQUE");
		
		declareParameter(new SqlParameter("PV_OTTIPO_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDBLOQUE_I", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new CampoBloqueMapper()));
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class CampoBloqueMapper implements RowMapper {
	
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		LlaveValorVO bloque = new LlaveValorVO();
		bloque.setKey(rs.getString(1));
		bloque.setValue(rs.getString(2));
		
		return bloque;
	}
}

protected class ObtenerReglaValidacion extends StoredProcedure {
	
	protected ObtenerReglaValidacion(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_OBTIENE_RAMO_REGLAS");
		
		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ReglaValidMapper()));
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class ReglaValidMapper implements RowMapper {
	
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		ReglaValidacionVO regla = new ReglaValidacionVO();
		regla.setCodigoBloque(rs.getString("CDBLOQUE"));
		regla.setDescripcionBloque(rs.getString("DSBLOQUE"));
		regla.setSecuencia(rs.getString("OTSECUEN"));
		regla.setCodigoValidacion(rs.getString("CDVALIDA"));
		regla.setDescripcionValidacion(rs.getString("DSVALIDA"));
		regla.setCodigoCondicion(rs.getString("CDCONDIC"));
		regla.setDescripcionCondicion(rs.getString("DSCONDIC"));
		
		return regla;
	}
}

protected class ObtenerListaTablasApoyo extends StoredProcedure {
	
	protected ObtenerListaTablasApoyo(DataSource dataSource) {
		super(dataSource, "PKG_LISTAS.P_LISTA_TCATALOG");
		
		declareParameter(new SqlParameter("PV_CDTABLA_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDIDIOMA_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDREGION_I", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new TablaApoyoMapper()));
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class TablaApoyoMapper implements RowMapper {
	
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		LlaveValorVO llave = new LlaveValorVO();
		llave.setKey(rs.getString(1));
		llave.setValue(rs.getString(3));
		
		return llave;
	}
}

protected class AgregarConceptoPorCobertura extends StoredProcedure {
	
	protected AgregarConceptoPorCobertura(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_GUARDAR_COBERTURA_CONCEPTO");
		
		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDPERIOD_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_ORDEN_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDGARANT_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDCONTAR_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_OTTIPO_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDCONDIC_I", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class EliminaConceptosPorCobertura extends StoredProcedure {
	
	protected EliminaConceptosPorCobertura(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_BORRA_COBERTURA_CONCEPTO");
		
		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDPERIOD_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_ORDEN_I", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class EliminaAtributoVariableRol extends StoredProcedure {
	
	protected EliminaAtributoVariableRol(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_BORRA_TATRIROL");
		
		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDROL_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDATRIBU_I", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class AgregarRolInciso extends StoredProcedure {
	
	protected AgregarRolInciso(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_INSERTROLINCISO");
		
		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDROL_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDNIVEL_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDCOMPO_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_NMAXIMO_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDTIPSIT_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_SWDOMICI_I", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class AgregarAtributoVariableCatalogo extends StoredProcedure {
	
	protected AgregarAtributoVariableCatalogo(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_INSERT_TATRIPER");
		
		declareParameter(new SqlParameter("PV_CDATRIBU_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_DSATRIBU_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_SWFORMAT_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_NMLMIN_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_NMLMAX_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_OTTABVAL_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_GB_SWFORMAT_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDFISJUR_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDCATEGO_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_SWOBLIGA_I", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class EliminaRol extends StoredProcedure {
	
	protected EliminaRol(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_DELROLRAMO");
		
		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDROL_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDNIVEL_I", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class TieneHijosAtributoVariableRol extends StoredProcedure {
	
	protected TieneHijosAtributoVariableRol(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_VALIDA_HIJOS_TATRIROL");
		
		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("pv_cdrol_i", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("pv_cdatribu_i", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_existe_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class TieneHijosAtributoVariableObjeto extends StoredProcedure {
	
	protected TieneHijosAtributoVariableObjeto(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_VALIDA_HIJOS_TATRIOBJ");
		
		declareParameter(new SqlParameter("pv_cdtipobj_i", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("pv_cdatribu_i", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_existe_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class BorraTatriObjeto extends StoredProcedure {
	
	protected BorraTatriObjeto(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_BORRA_TATRIOBJ");
		
		declareParameter(new SqlParameter("pv_cdtipobj_i", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("pv_cdatribu_i", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class InsertarDatoFijo extends StoredProcedure {
	
	protected InsertarDatoFijo(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_INSERTTCMPFFLD");
		
		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDFUNCIO_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDBLOQUE_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDCAMPO_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDEXPRES_I", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class AsociarReglasDeValidacion extends StoredProcedure {
	
	protected AsociarReglasDeValidacion(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_GUARDA_RAMO_REGLAS");
		
		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDBLOQUE_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_OTSECUEN_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDVALIDA_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDCONDIC_I", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class EliminaReglasDeValidacion extends StoredProcedure {
	
	protected EliminaReglasDeValidacion(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_BORRA_RAMO_REGLAS");
		
		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDBLOQUE_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_OTSECUEN_I", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class InsertarValidacion extends StoredProcedure {
	
	protected InsertarValidacion(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_INSERT_TVALIDAC");
		
		declareParameter(new SqlParameter("pv_cdvalida_i", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("pv_dsmsgerr_i", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("pv_cdexpres_i", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("pv_dsvalida_i", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("pv_cdtipo_i", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class InsertarConceptoTarif extends StoredProcedure {
	
	protected InsertarConceptoTarif(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_INSERTCONCTARI");
		
		declareParameter(new SqlParameter("pv_cdcontar_i", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("pv_dscontar_i", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("pv_cdtipcon_i", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("pv_cdexpres_i", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class InsertarVariableTemp extends StoredProcedure {
	
	protected InsertarVariableTemp(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_INSERT_TVARIATM");
		
		declareParameter(new SqlParameter("pv_cdexpres_i", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("pv_cdvariant_i", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("pv_dsvariat_i", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class InsertarAutorizacion extends StoredProcedure {
	
	protected InsertarAutorizacion(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_INSERTAUTO");
		
		declareParameter(new SqlParameter("pv_cdmotivo_i", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("pv_dsmotivo_i", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("pv_otnivel_i", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("pv_cdexpres_i", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class InsertarCondicion extends StoredProcedure {
	
	protected InsertarCondicion(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_INSERTCONDIC");
		
		declareParameter(new SqlParameter("pv_cdcondic_i", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("pv_dscondic_i", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("pv_cdexpres_i", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class AgregarObjCatalogo extends StoredProcedure {
	
	protected AgregarObjCatalogo(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_GUARDA_TIPO_OBJETO");
		
		declareParameter(new SqlParameter("PV_DSTIPOBJ_I", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class AgregaTipoObjInciso extends StoredProcedure {
	
	protected AgregaTipoObjInciso(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_GUARDA_TIPO_INCISO");
		
		declareParameter(new SqlParameter("PV_CDTIPSIT_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDTIPOBJ_I", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class BorraTipoObjInciso extends StoredProcedure {
	
	protected BorraTipoObjInciso(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_BORRA_TIPO_INCISO");
		
		declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("pv_cdtipobj_i", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class AgregaDatosVarObj extends StoredProcedure {
	
	protected AgregaDatosVarObj(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_INSERTA_TATRIOBJ");
		
		declareParameter(new SqlParameter("PV_CDTIPOBJ_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDATRIBU_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_SWFORMAT_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_NMLMAX_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_NMLMIN_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_SWOBLIGA_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_DSATRIBU_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_OTTABVAL_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_SWPRODUC_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_SWSUPLEM_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_SWACTUAL_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_GB_SWFORMAT_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDATRIBU_PADRE_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_NMORDEN_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_NMAGRUPA_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDCONDICVIS_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_SWCOTIZA_I", OracleTypes.VARCHAR));		
		declareParameter(new SqlParameter("PV_SWDATCOM_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_SWCOMOBL_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_SWCOMUPD_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_SWENDOSO_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_SWENDOBL_I", OracleTypes.VARCHAR));
		
		
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}

protected class AsociarVariablesDelProducto extends StoredProcedure {
	
	protected AsociarVariablesDelProducto(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_GUARDA_RAMO_VARIABLE");
		
		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDVARIAT_I", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}
protected class DesasociarVariablesDelProducto extends StoredProcedure {
	
	protected DesasociarVariablesDelProducto(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_BORRA_RAMO_VARIABLE");
		
		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("PV_CDVARIAT_I", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}
protected class BorraVarTmp extends StoredProcedure {
	
	protected BorraVarTmp(DataSource dataSource) {
		super(dataSource, "PKG_WIZARD.P_BORRA_TVARIATM");
		
		declareParameter(new SqlParameter("pv_cdvariat_i", OracleTypes.VARCHAR));
		
		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		compile();
	}
}


	@Override
	public List<Map<String, String>> obtieneTablasApoyo(Map<String,String> params) throws Exception {
		Map<String, Object> resultado = ejecutaSP(new ObtieneTablasApoyo(getDataSource()), params);
		return (List<Map<String, String>>) resultado.get("PV_REGISTRO_O");
	}
	
	protected class ObtieneTablasApoyo extends StoredProcedure {
		protected ObtieneTablasApoyo(DataSource dataSource) {
	        super(dataSource,"Pkg_Tabapoyo.P_OBTIENE_TABLAS_APOYO");
	        declareParameter(new SqlParameter("PV_NMTABLA_I"  , OracleTypes.VARCHAR));
	        declareParameter(new SqlParameter("PV_CDTABLA_I"  , OracleTypes.VARCHAR));
	        declareParameter(new SqlParameter("PV_DSTABLA_I"  , OracleTypes.VARCHAR));
	        declareParameter(new SqlParameter("PV_OTTIPOTB_I" , OracleTypes.VARCHAR));
	        declareParameter(new SqlParameter("PV_CDSISROL_I" , OracleTypes.VARCHAR));
	        declareParameter(new SqlParameter("PV_CDUSER_I"   , OracleTypes.VARCHAR));
	        String[] cols = new String[]{
	        		"NMTABLA"
	        		,"CDTABLA"
	        		,"DSTABLA"
	        		,"OTTIPOTB"
	        		,"OTTIPOTB_DESC"
	        		,"OTTIPOAC"
	        		,"CDTABLJ1"
	        		,"CDTABLJ2"
	        		,"CDTABLJ3"
	        		,"CLNATURA"
	        };
	        declareParameter(new SqlOutParameter("PV_REGISTRO_O" , OracleTypes.CURSOR, new GenericMapper(cols)));
	        declareParameter(new SqlOutParameter("PV_MSG_ID_O"   , OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("PV_TITLE_O"    , OracleTypes.VARCHAR));
	        compile();
		}
	}

	@Override
	public String guardaTablaApoyo(Map<String,String> params) throws Exception {
		Map<String, Object> resultado = ejecutaSP(new GuardaTablaApoyo(getDataSource()), params);
		return (String) resultado.get("pi_nmtabla");
	}
	
	protected class GuardaTablaApoyo extends StoredProcedure {
		protected GuardaTablaApoyo(DataSource dataSource) {
			super(dataSource,"PKG_TABAPOYO.P_GUARDA_TABLA");
			declareParameter(new SqlParameter("pi_cdtabla" , OracleTypes.VARCHAR));
			declareParameter(new SqlInOutParameter("pi_nmtabla" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_dstabla" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_ottipoac" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_ottipotb" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_swmodifi" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_swerror" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_clnatura" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_cdtablj1" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_cdtablj2" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_cdtablj3" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public String guardaClavesTablaApoyo(Map<String,String> params) throws Exception {
		Map<String, Object> resultado = ejecutaSP(new GuardaClavesTablaApoyo(getDataSource()), params);
		return (String) resultado.get("pv_title_o");
	}
	
	protected class GuardaClavesTablaApoyo extends StoredProcedure {
		protected GuardaClavesTablaApoyo(DataSource dataSource) {
			super(dataSource,"PKG_TABAPOYO.P_GUARDA_CLAVES");
			declareParameter(new SqlParameter("pi_tip_tran" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_nmtabla" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_dsclave1" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_swforma1" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_nmlmin1" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_nmlmax1" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_dsclave2" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_swforma2" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_nmlmin2" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_nmlmax2" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_dsclave3" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_swforma3" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_nmlmin3" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_nmlmax3" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_dsclave4" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_swforma4" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_nmlmin4" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_nmlmax4" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_dsclave5" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_swforma5" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_nmlmin5" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_nmlmax5" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public String guardaAtributosTablaApoyo(Map<String,String> params) throws Exception {
		Map<String, Object> resultado = ejecutaSP(new GuardaAtributosTablaApoyo(getDataSource()), params);
		return (String) resultado.get("pv_title_o");
	}
	
	protected class GuardaAtributosTablaApoyo extends StoredProcedure {
		protected GuardaAtributosTablaApoyo(DataSource dataSource) {
			super(dataSource,"PKG_TABAPOYO.P_GUARDA_ATRIBUTOS");
			declareParameter(new SqlParameter("pi_tip_tran" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_nmtabla" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_cdatribu" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_dsatribu" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_swformat" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_nmlmin" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_nmlmax" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> obtieneClavesTablaApoyo(Map<String,String> params) throws Exception {
		Map<String, Object> resultado = ejecutaSP(new ObtieneClavesTablaApoyo(getDataSource()), params);
		return (List<Map<String, String>>) resultado.get("pv_registro_o");
	}
	
	protected class ObtieneClavesTablaApoyo extends StoredProcedure {
		protected ObtieneClavesTablaApoyo(DataSource dataSource) {
			super(dataSource,"PKG_TABAPOYO.P_OBTIENE_CLAVES");
			declareParameter(new SqlParameter("pi_nmtabla" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"DSCLAVE1"
					,"SWFORMA1"
					,"DSFORMA1"
					,"NMLMIN1"
					,"NMLMAX1"
					,"NUMCLAVE"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public List<Map<String, String>> obtieneAtributosTablaApoyo(Map<String,String> params) throws Exception {
		Map<String, Object> resultado = ejecutaSP(new ObtieneAtributosTablaApoyo(getDataSource()), params);
		return (List<Map<String, String>>) resultado.get("pv_registro_o");
	}
	
	protected class ObtieneAtributosTablaApoyo extends StoredProcedure {
		protected ObtieneAtributosTablaApoyo(DataSource dataSource) {
			super(dataSource,"PKG_TABAPOYO.P_OBTIENE_ATRIBUTOS");
			declareParameter(new SqlParameter("pi_nmtabla" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CDATRIBU"
					,"DSATRIBU"
					,"SWFORMAT"
					,"DSFORMAT"
					,"NMLMIN"
					,"NMLMAX"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

}