package mx.com.gseguros.portal.emision.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.consultas.model.DocumentoReciboParaMostrarDTO;
import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.dao.impl.GenericMapper;
import mx.com.gseguros.portal.emision.dao.EmisionDAO;
import mx.com.gseguros.portal.emision.dao.impl.EmisionDAOImpl.InsertarMpoliimp;
import mx.com.gseguros.portal.emision.dao.impl.EmisionDAOImpl.ObtieneDatosWsCotizacionServPublico;
import mx.com.gseguros.portal.emision.model.EmisionVO;
import mx.com.gseguros.utils.Utils;
import oracle.jdbc.OracleTypes;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jdbc.support.oracle.SqlArrayValue;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class EmisionDAOImpl extends AbstractManagerDAO implements EmisionDAO
{
	private final static Logger logger = LoggerFactory.getLogger(EmisionDAOImpl.class);
	
	@Override
	public EmisionVO emitir(String cdusuari, String cdunieco, String cdramo, String estado, String nmpoliza, 
			String nmsituac, String nmsuplem, String cdelemento, String cdcia, String cdplan, String cdperpag, 
			String cdperson, Date fecha, String ntramite) throws Exception {
	    
		EmisionVO emision = new EmisionVO();
		
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("pv_cdusuari" , cdusuari);
		params.put("pv_cdunieco" , cdunieco);
		params.put("pv_cdramo"   , cdramo);
		params.put("pv_estado"   , estado);
		params.put("pv_nmpoliza" , nmpoliza);
		params.put("pv_nmsituac" , nmsituac);
		params.put("pv_nmsuplem" , nmsuplem);
		params.put("pv_cdelement", cdelemento);
		params.put("pv_cdcia"    , cdcia);
		params.put("pv_cdplan"   , cdplan);
		params.put("pv_cdperpag" , cdperpag);
		params.put("pv_cdperson" , cdperson);
		params.put("pv_fecha"    , fecha);
		params.put("pv_ntramite" , ntramite);
		
		Map<String,Object> resultado = ejecutaSP(new ProcesoEmisionGeneralSP(getDataSource()), params);
		
		emision.setNmpoliza((String)resultado.get("pv_nmpoliza_o"));
		emision.setNmpoliex((String)resultado.get("pv_nmpoliex_o"));
		emision.setNmsuplem((String)resultado.get("pv_nmsuplem_o"));
		emision.setEsDxN((String)resultado.get("pv_esdxn_o"));
		emision.setMessage((String)resultado.get("pv_message"));
		emision.setCdideper((String)resultado.get("pv_cdideper_o"));
		return emision;
	}
	
	protected class ProcesoEmisionGeneralSP extends StoredProcedure {
		
		protected ProcesoEmisionGeneralSP(DataSource dataSource) {
			
			super(dataSource,"PKG_EMISION.P_PROCESO_EMISION_GENERAL");
			declareParameter(new SqlParameter("pv_cdusuari",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdunieco",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo",        OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado",        OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdelement",     OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdcia",         OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdplan",        OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperpag",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fecha",         OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_ntramite",      OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_nmpoliza_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_nmpoliex_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_nmsuplem_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_esdxn_o",    OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_message",    OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_cdideper_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
		}
	}
	
	
    @Override
    public EmisionVO emitirPolizaRemotaRecupera(String cdusuari, String cdunieco, String cdramo, String estado, String nmpoliza, 
            String nmsituac, String nmsuplem, String cdelemento, String cdcia, String cdplan, String cdperpag, 
            String cdperson, Date fecha, String ntramite, String polizaremota) throws Exception {
        
        EmisionVO emision = new EmisionVO();
        
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("pv_cdusuari" , cdusuari);
        params.put("pv_cdunieco" , cdunieco);
        params.put("pv_cdramo"   , cdramo);
        params.put("pv_estado"   , estado);
        params.put("pv_nmpoliza" , nmpoliza);
        params.put("pv_nmsituac" , nmsituac);
        params.put("pv_nmsuplem" , nmsuplem);
        params.put("pv_cdelement", cdelemento);
        params.put("pv_cdcia"    , cdcia);
        params.put("pv_cdplan"   , cdplan);
        params.put("pv_cdperpag" , cdperpag);
        params.put("pv_cdperson" , cdperson);
        params.put("pv_fecha"    , fecha);
        params.put("pv_ntramite" , ntramite);
        params.put("pv_poliza_remota_i" , polizaremota);
        
        Map<String,Object> resultado = ejecutaSP(new EmisionRemotaRecuperaSP(getDataSource()), params);
        
        emision.setNmpoliza((String)resultado.get("pv_nmpoliza_o"));
        emision.setNmpoliex((String)resultado.get("pv_nmpoliex_o"));
        emision.setNmsuplem((String)resultado.get("pv_nmsuplem_o"));
        emision.setEsDxN((String)resultado.get("pv_esdxn_o"));
        emision.setMessage((String)resultado.get("pv_message"));
        emision.setCdideper((String)resultado.get("pv_cdideper_o"));
        return emision;
    }
	
	protected class EmisionRemotaRecuperaSP extends StoredProcedure {
	    
	    protected EmisionRemotaRecuperaSP(DataSource dataSource) {
	        super(dataSource, "PKG_EMISION.P_EMISION_REMOTA_RECUPERA");
            declareParameter(new SqlParameter("pv_cdusuari",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdunieco",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo",        OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado",        OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsituac",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsuplem",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdelement",     OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdcia",         OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdplan",        OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdperpag",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdperson",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_fecha",         OracleTypes.DATE));
            declareParameter(new SqlParameter("pv_ntramite",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_poliza_remota_i",OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_nmpoliza_o", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_nmpoliex_o", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_nmsuplem_o", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_esdxn_o",    OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_message",    OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_cdideper_o", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
	    }
	}
	
	
	public void actualizaNmpoliexAutos(String cdunieco, String cdramo, String estado, String nmpoliza, 
			String nmsuplem, String nmpoliex, String cduniext, String ramoGS) throws Exception {
		
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i"  , cdramo);
		params.put("pv_estado_i"  , estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_nmsuplem_i", nmsuplem);
		params.put("pv_nmpoliex_i", nmpoliex);
		params.put("pv_cduniext_i", cduniext);
		params.put("pv_ramo_i"    , ramoGS);
		ejecutaSP(new ActualizaPolizaExternaSP(getDataSource()), params);
	}
	
	protected class ActualizaPolizaExternaSP extends StoredProcedure {
		
		protected ActualizaPolizaExternaSP(DataSource dataSource) {
			
			super(dataSource, "PKG_SATELITES.P_ACTUALIZA_NMPOLIEX_AUTOS");
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliex_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cduniext_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ramo_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o",OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o" ,OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void insertarMpoliimp(
			String ntramite
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String tipo
			,String nmtraope
			,String nmrecibo
			,String orden
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("ntramite" , ntramite);
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("tipo"     , tipo);
		params.put("nmtraope" , nmtraope);
		params.put("nmrecibo" , nmrecibo);
		params.put("orden"    , orden);
		ejecutaSP(new InsertarMpoliimp(getDataSource()),params);
	}
	
	protected class InsertarMpoliimp extends StoredProcedure
	{
		protected InsertarMpoliimp(DataSource dataSource)
		{
			super(dataSource, "PKG_SATELITES2.P_INSERTA_MPOLIIMP");
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipo"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmtraope" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmrecibo" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("orden"    , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void marcarTramiteImpreso(String ntramite, String swimpres) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("ntramite" , ntramite);
		params.put("swimpres" , swimpres);
		ejecutaSP(new MarcarTramiteImpreso(getDataSource()),params);
	}
	
	protected class MarcarTramiteImpreso extends StoredProcedure
	{
		protected MarcarTramiteImpreso(DataSource dataSource)
		{
			super(dataSource, "PKG_SATELITES2.P_IMPRIMIR_TRAMITE");
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swimpres" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	/**
	 * SUMA A LAS REMESAS LA IMPRESION ACTUAL
	 * SI LA SUMA RESULTA ESTAR COMPLETA: ACTUALIZA LAS REMESAS, Y LOS HIJOS EMISION/ENDOSOS SI HAY
	 */
	@Override
	public boolean sumarImpresiones(String lote, String tipolote, String peso) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("lote"     , lote);
		params.put("tipolote" , tipolote);
		params.put("peso"     , peso);
		Map<String,Object> procRes = ejecutaSP(new SumarImpresiones(getDataSource()),params);
		boolean            impreso = "S".equals((String)procRes.get("pv_impreso_o"));
		logger.debug(Utils.log("PKG_SATELITES2.P_SUMA_IMPRESIONES impreso:",impreso));
		return impreso;
	}
	
	protected class SumarImpresiones extends StoredProcedure
	{
		protected SumarImpresiones(DataSource dataSource)
		{
			super(dataSource, "PKG_SATELITES2.P_SUMA_IMPRESIONES");
			declareParameter(new SqlParameter("lote"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipolote" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("peso"     , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_impreso_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void mostrarRecibosImpresosListaDeListas(List<DocumentoReciboParaMostrarDTO> lista)throws Exception
	{
		String[][] array = new String[lista.size()][];
		int        i     = 0;
		for(DocumentoReciboParaMostrarDTO pMovIte : lista)
		{
			array[i++] = pMovIte.indexar();
		}
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("array" , new SqlArrayValue(array));
		Utils.debugProcedure(logger, "PKG_SATELITES2.P_MUESTRA_RECIBOS_IMPRESOS", params);
		ejecutaSP(new MostrarRecibosImpresosListaDeListas(getDataSource()),params);
	}
	
	protected class MostrarRecibosImpresosListaDeListas extends StoredProcedure
	{
		protected MostrarRecibosImpresosListaDeListas(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_MUESTRA_RECIBOS_IMPRESOS");
			declareParameter(new SqlParameter("array" , OracleTypes.ARRAY , "LISTA_LISTAS_VARCHAR2"));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void movimientoClienteTramite(String ntramite, String cdperson, String accion) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("ntramite" , ntramite);
		params.put("cdperson" , cdperson);
		params.put("accion"   , accion);
		Map<String,Object> procRes = ejecutaSP(new MovimientoClienteTramiteSP(getDataSource()),params);
		String error = (String)procRes.get("pv_error_o");
		if(StringUtils.isNotBlank(error))
		{
			throw new ApplicationException(error);
		}
	}
	
	protected class MovimientoClienteTramiteSP extends StoredProcedure
	{
		protected MovimientoClienteTramiteSP(DataSource dataSource)
		{
			super(dataSource,"P_MOV_CLIENTE_TRAMITE");
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdperson" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"   , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_error_o"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void validarDocumentoTramite (String ntramite, String cddocume) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("ntramite", ntramite);
		params.put("cddocume", cddocume);
		ejecutaSP(new ValidarDocumentoTramiteSP(getDataSource()), params);
	}
	
	protected class ValidarDocumentoTramiteSP extends StoredProcedure
	{
		protected ValidarDocumentoTramiteSP(DataSource dataSource)
		{
			super(dataSource,"P_VALIDA_CDDOCUME_TRAMITE");
			declareParameter(new SqlParameter("ntramite", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cddocume", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o" , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String recuperarTramiteCotizacion (String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("cdunieco", cdunieco);
		params.put("cdramo",   cdramo);
		params.put("estado",   estado);
		params.put("nmpoliza", nmpoliza);
		Map<String, Object> procRed = ejecutaSP(new RecuperarTramiteCotizacionSP(getDataSource()), params);
		String ntramite = (String) procRed.get("pv_ntramite_o");
		if (StringUtils.isBlank(ntramite)) {
			throw new ApplicationException("No se encuentra el tr\u00e1mite de cotizaci\u00f3n");
		}
		logger.debug("tramite de cotizacion = {}", ntramite);
		return ntramite;
	}
	
	protected class RecuperarTramiteCotizacionSP extends StoredProcedure
	{
		protected RecuperarTramiteCotizacionSP(DataSource dataSource)
		{
			super(dataSource,"P_GET_TRAMITE_COTIZACION");
			declareParameter(new SqlParameter("cdunieco", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_ntramite_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void bloquearProceso (String cdproceso, boolean bloquear, String cdusuari, String cdsisrol) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("cdproceso" , cdproceso);
		params.put("bloquear"  , bloquear ? "S" : "N");
		params.put("cdusuari"  , cdusuari);
		params.put("cdsisrol"  , cdsisrol);
		ejecutaSP(new BloquearProcesoSP(getDataSource()), params);
	}
	
	protected class BloquearProcesoSP extends StoredProcedure
	{
		protected BloquearProcesoSP(DataSource dataSource)
		{
			super(dataSource,"P_BLOQUEAR_PROCESO");
			declareParameter(new SqlParameter("cdproceso" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("bloquear"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuari"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void revierteEmision(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem) throws Exception{
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i"  , cdramo);
		params.put("pv_estado_i"  , estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_nmsuplem_i", nmsuplem);
		
		ejecutaSP(new RevierteEmision(getDataSource()), params);
	}
	
	protected class RevierteEmision extends StoredProcedure
	{
		protected RevierteEmision(DataSource dataSource)
		{
			super(dataSource,"P_REVIERTE_EMISION");
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> recuperarDocumentosGeneradosPorParametrizacion (String ntramite) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("ntramite", ntramite);
		Map<String, Object> procRes = ejecutaSP(new RecuperarDocumentosGeneradosPorParametrizacionSP(getDataSource()), params);
		List<Map<String, String>> lista = (List<Map<String, String>>) procRes.get("pv_registro_o");
		if (lista == null) {
			lista = new ArrayList<Map<String, String>>();
		}
		logger.debug(Utils.log("recuperarDocumentosGeneradosPorParametrizacion lista: ", lista));
		return lista;
	}
	
	protected class RecuperarDocumentosGeneradosPorParametrizacionSP extends StoredProcedure
	{
		protected RecuperarDocumentosGeneradosPorParametrizacionSP(DataSource dataSource)
		{
			super(dataSource, "PKG_EMISION.P_GET_DOCS_GENERADOS_X_PARAM");
			declareParameter(new SqlParameter("ntramite", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new GenericMapper(new String[]{
					"CDDOCUME", "DSDOCUME", "CDORDDOC", "CDMODDOC"
			})));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void actualizarCdplanGrupo(String cdunieco, String cdramo, String estado, String nmpoliza,
			String nmsuplem, String cdgrupo, String cdplan) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("cdgrupo"  , cdgrupo);
		params.put("cdplan"   , cdplan);
		ejecutaSP(new ActualizarCdplanGrupoSP(getDataSource()), params);
	}
	
	protected class ActualizarCdplanGrupoSP extends StoredProcedure {
		protected ActualizarCdplanGrupoSP (DataSource dataSource) {
			super(dataSource, "P_EMI_ACTUALIZA_CDPLAN_GRUPO");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdgrupo"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdplan"   , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
    public Map<String, String> redireccionaReporteVidaAuto(Integer cdunieco, Integer cdramo,Integer nmpoliza, String tipoEndoso, String endoso) throws Exception {
        Map<String, String> params= new LinkedHashMap<String, String>();
        params.put("PV_CDUNIECO_I"   , cdunieco+"");
        params.put("PV_CDRAMO_I"     , cdramo+"");
        params.put("PV_NMPOLIZA_I"   , nmpoliza+"");
        params.put("PV_CDDEVCIA_I"   , tipoEndoso);
        params.put("PV_CDGESTOR_I"   , endoso);
        Map<String,Object>procResult  = ejecutaSP(new redireccionaReporteVidaAuto(getDataSource()), params);
        List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
        if(lista==null)
        {
            lista=new ArrayList<Map<String,String>>();
        }
        Utils.debugProcedure(logger, "redireccionaReporteVidaAuto", params,lista);
        return lista.get(0);
    }
    
    protected class redireccionaReporteVidaAuto extends StoredProcedure {
        
        String columnas[]=new String[]{
                "p_unieco","p_ramo","p_estado","p_poliza","p_suplem","p_situac"
                };
        
        protected redireccionaReporteVidaAuto (DataSource dataSource) {
            super(dataSource, "P_REPORTE_VIDA_AUTO");
            declareParameter(new SqlParameter("PV_CDUNIECO_I" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("PV_CDRAMO_I"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("PV_NMPOLIZA_I" , OracleTypes.VARCHAR)); 
            declareParameter(new SqlParameter("PV_CDDEVCIA_I" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("PV_CDGESTOR_I" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o",   OracleTypes.CURSOR, new GenericMapper(columnas)));
            declareParameter(new SqlOutParameter("PV_MSG_ID_O" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("PV_TITLE_O"  , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public List<Map<String,String>> obtieneDatosWsCotizacionServPublico( String pv_cdunieco_i
                                                    ,String pv_cdramo_i
                                                    ,String pv_estado_i
                                                    ,String pv_nmpoliza_i
                                                    ,String pv_nmsuplem_i
                                                    ,String pv_tipoend_i
                                                    ,String pv_numend_i
                                                    )throws Exception
    {
        Map<String,String> params = new LinkedHashMap<String,String>();
        
        params.put("pv_cdunieco_i" , pv_cdunieco_i);
        params.put("pv_cdramo_i"   , pv_cdramo_i);
        params.put("pv_estado_i" , pv_estado_i);
        params.put("pv_nmpoliza_i"   , pv_nmpoliza_i);
        params.put("pv_nmsuplem_i" , pv_nmsuplem_i);
        params.put("pv_tipoend_i"     , pv_tipoend_i);
        params.put("pv_numend_i" , pv_numend_i);
        
       
        Map<String,Object>procResult     = ejecutaSP(new ObtieneDatosWsCotizacionServPublico(getDataSource()),params);
        List<Map<String,String>>registro = (List<Map<String,String>>)procResult.get("pv_registro_o");
        return registro;
    }
    
    protected class ObtieneDatosWsCotizacionServPublico extends StoredProcedure
    {
        protected ObtieneDatosWsCotizacionServPublico(DataSource dataSource)
        {
            super(dataSource, "PKG_CONSULTA.P_WS_COTIZACION_SERV_PUBLICO");
            declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsuplem_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_tipoend_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_numend_i"     , OracleTypes.VARCHAR));
            String[] cols = new String[]{
                    "idNegocio"
                    ,"vigencia"
                    ,"idAgente"
                    ,"sucursal"
                    ,"numPoliza"
                    ,"ramo"
                    ,"sucursalEmisora"
                    ,"numEndoso"
                    ,"tipoEndoso"
                    ,"idMotivoEndoso"
                    ,"cve_cli"
                    ,"codigo"
                    ,"idDireccion"
                    ,"cveUsuarioCaptura"
                    ,"descuentoAgente"
                    ,"descuentoCliente"
                    ,"descuentoFacultamiento"
                    ,"porcentajeCesionComision"
                    ,"idAgenteCompartido"
                    ,"porcenComisionAgente2"
                    ,"idEstatusCotizacion"
                    ,"tipoProducto"
                    ,"tipoProductoSpecified"
                    ,"tipoServicio"
                    ,"versionTarifa"
                    ,"inicioVigencia"
                    ,"inicioVigenciaSpecified"
                    ,"finVigencia"
                    ,"moneda"
                    ,"renueva"
                    ,"formaPago"
                    ,"polizaTracto"
                    ,"derechoPoliza"
                    ,"iva"
                    ,"pagoSubsecuente"
                    ,"primaNeta"
                    ,"primerPago"
                    ,"recargoPagoFraccionado"
                    ,"contarPAI"
                    ,"nombreAlterno"
                    ,"idTarifa"
                    ,"udi"
                    ,"multianual"
                    ,"periodoGracia"
                    ,"fondoEspecial"
                    ,"nmsolici"
                    ,"tipoCotizacion"
                    ,"f1"
                    ,"f2"
                    ,"f3"
                    ,"porcentajeBono"
                    ,"idProveedorUdi"
                    ,"tipoProveedorUdi"
                    ,"idTipoValor"
                    ,"modelo"
                    ,"numMotor"
                    ,"numSerie"
                    ,"numPlacas"
                    ,"numEconomico"
                    ,"numOcupantes"
                    ,"sarcvdias"
                    ,"beneficiarioPref"
                    ,"tipVehiCA"
                    ,"tipoVehiculo"
                    ,"tipoVehiculoSpecified"
                    ,"valor"
                    ,"conductor"
                    ,"primaNetaInc"
                    ,"aseguradoAlterno"
                    ,"idTipoCarga"
                    ,"adaptaciones"
                    ,"equipoEspecial"
                    ,"numSerieValido"
                    ,"situacionRiesgo"
                    ,"descripcion"
                    ,"versionTarifaInc"
                    ,"idConfiguracionPaquete"
                    ,"seleccionado"
                    ,"fechaFactura"
                    ,"idCobertura"
                    ,"idCoberturaSpecified"
                    ,"seleccionadoCob"
                    ,"deducible"
                    ,"suma_asegurada"
                    ,"prima_bruta"
                    ,"prima_netaCob"
                    ,"comision"
                    ,"nmsituac"
                    ,"numFolio"
                    ,"vigencia"
                    ,"idBanco"
                    ,"mesesSinIntereses"
                    ,"idOrigenSolicitud"
                    ,"tipoUso"
                    ,"clavegs"
                    ,"amis"
                    ,"derechoAgente"
                    ,"derechoPromotor"
                    ,"montoCedido"
                    ,"cilindraje"
                    ,"descuentoInciso"
                    
            };
            
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
        }
    }
	
}