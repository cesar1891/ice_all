package mx.com.gseguros.portal.cancelacion.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.gseguros.exception.DaoException;
import mx.com.gseguros.portal.cancelacion.dao.CancelacionDAO;
import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.dao.impl.GenericMapper;
import mx.com.gseguros.portal.general.model.PolizaVO;
import mx.com.gseguros.utils.Utils;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class CancelacionDAOImpl extends AbstractManagerDAO implements CancelacionDAO
{
	
	private static Logger logger = Logger.getLogger(CancelacionDAOImpl.class);

	////////////////////////////
	////// buscar polizas //////
	/*////////////////////////*/
	@Override
	@Deprecated
	public List<Map<String, String>> buscarPolizas(Map<String,String> params) throws Exception
	{
		Map<String,Object> resultadoMap=this.ejecutaSP(new BuscarPolizas(this.getDataSource()), params);
		return (List<Map<String, String>>) resultadoMap.get("pv_registro_o");
	}
	
	@Deprecated
	protected class BuscarPolizas extends StoredProcedure
	{
		String[] columnas=new String[]{"NOMBRE", "FEMISION", "FEINICOV", "FEFINIV", "PRITOTAL","DSRAMO"
	            ,"CDRAMO"
	            ,"DSTIPSIT"
	            ,"CDTIPSIT"
	            ,"DSUNIECO"
	            ,"CDUNIECO"
	            ,"NMPOLIZA"
	            ,"NMPOLIEX"
	            ,"NMSOLICI"
	            ,"ESTADO"
	            ,"FERECIBO"};

		protected BuscarPolizas(DataSource dataSource)
		{
			super(dataSource, "PKG_CONSULTA.P_CONSUL_POLIZA");
			declareParameter(new SqlParameter("pv_cdunieco_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliex_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdagente_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fereferen_i"  , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(columnas)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	/*////////////////////////*/
	////// buscar polizas //////
	////////////////////////////

	////////////////////////////////////////////
	////// obtener detalle de cancelacion //////
	/*////////////////////////////////////////*/
	@Override
	public Map<String, String> obtenerDetalleCancelacion(Map<String, String> params) throws Exception
	{
		Map<String,Object> resultadoMap=this.ejecutaSP(new ObtenerDetalleCancelacion(this.getDataSource()), params);
		List<Map<String, String>>listaTemp=(List<Map<String, String>>) resultadoMap.get("pv_registro_o");
		Map<String,String> respuesta=null;
		if(!listaTemp.isEmpty())
			respuesta=listaTemp.get(0);
		return respuesta;
	}

	protected class ObtenerDetalleCancelacion extends StoredProcedure
	{
		String[] columnas=new String[]{"CDMOTANU", "DSMOTANU", "FEANULAC", "DSCANCEL"};

		protected ObtenerDetalleCancelacion(DataSource dataSource)
		{
			super(dataSource, "PKG_CONSULTA.P_GET_DETALLE_CANC");
			declareParameter(new SqlParameter("pv_cdunieco_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i"    , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(columnas)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	/*////////////////////////////////////////*/
	////// obtener detalle de cancelacion //////
	////////////////////////////////////////////
	
	////////////////////////////////////////
	////// obtener polizas candidatas //////
	/*////////////////////////////////////*/
	@Override
	@Deprecated
	public List<Map<String,String>> obtenerPolizasCandidatas(Map<String,String> params) throws Exception
	{
		
		Map<String,Object> resultadoMap=this.ejecutaSP(new ObtenerPolizasCandidatas(this.getDataSource()), params);
		return (List<Map<String, String>>) resultadoMap.get("pv_registro_o");
	}
	
	@Override
	public List<Map<String,String>> obtenerPolizasCandidatas(
			String asegurado
			,String cdunieco
			,String cdramo
			,String nmpoliza
			,String nmsituac
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("pv_asegurado_i" , asegurado);
		params.put("pv_dsuniage_i"  , cdunieco);
		params.put("pv_dsramo_i"    , cdramo);
		params.put("pv_nmpoliza_i"  , nmpoliza);
		params.put("pv_nmsituac_i"  , nmsituac);
		Utils.debugProcedure(logger, "pkg_cancela.p_obtiene_poliza_a_cancelar", params);
		Map<String,Object> procResult  = ejecutaSP(new ObtenerPolizasCandidatas(this.getDataSource()), params);
		List<Map<String,String>> lista = (List<Map<String, String>>)procResult.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		Utils.debugProcedure(logger, "pkg_cancela.p_obtiene_poliza_a_cancelar", params, lista);
		return lista;
	}
	
	protected class ObtenerPolizasCandidatas extends StoredProcedure
	{

		protected ObtenerPolizasCandidatas(DataSource dataSource)
		{
			super(dataSource, "pkg_cancela.p_obtiene_poliza_a_cancelar");
			declareParameter(new SqlParameter("pv_asegurado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsuniage_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsramo_i"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i"    , OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"ASEGURADO"  , "CDUNIAGE" , "DSUNIECO"    , "CDRAMO"   , "DSRAMO"
					,"NMPOLIZA"  , "NMSITUAC" , "TIPO_CANCEL" , "FECANCEL" , "CDRAZON"
					,"SWCANCELA" , "ESTADO"   , "FEEFECTO"    , "FEVENCIM"
			};
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	/*////////////////////////////////////*/
	////// obtener polizas candidatas //////
	////////////////////////////////////////
	
	////////////////////////////////////////
	////// grabar polizas en tagrucan //////
	/*////////////////////////////////////*/
	@Override
	public void seleccionaPolizas(Map<String,Object> params) throws Exception
	{
		ejecutaSP(new SeleccionaPolizas(this.getDataSource()), params);
	}
	
	protected class SeleccionaPolizas extends StoredProcedure
	{
		public SeleccionaPolizas(DataSource dataSource)
		{
			super(dataSource, "pkg_cancela.p_selecciona_polizas");
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_agencia_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fechapro_i" , OracleTypes.DATE));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	/*////////////////////////////////////*/
	////// grabar polizas en tagrucan //////
	////////////////////////////////////////
	
	/////////////////////////////
	////// cancelar poliza //////
	/*/////////////////////////*/
	@Override
	@Deprecated
	public String cancelaPoliza (Map<String,String> params) throws Exception
	{
		Map<String,Object> resultadoMap = ejecutaSP(new CancelaPoliza(getDataSource()),Utils.ponFechas(params));
		return (String) resultadoMap.get("pv_nmsuplem_o");
	}
	
	@Override
	public Map<String,Object> cancelaPoliza(
			String cdunieco
			,String cdramo
			,String cduniage
			,String estado
			,String nmpoliza
			,String nmsituac
			,String cdrazon
			,String comenta
			,Date feefecto
			,Date fevencim
			,Date fecancel
			,String cdusuari
			,String cdtipsup
			,String cdsisrol
			)throws Exception
	{
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("pv_cdunieco_i" , cdunieco);
		params.put("pv_cdramo_i"   , cdramo);
		params.put("pv_cduniage_i" , cduniage);
		params.put("pv_estado_i"   , estado);
		params.put("pv_nmpoliza_i" , nmpoliza);
		params.put("pv_nmsituac_i" , nmsituac);
		params.put("pv_cdrazon_i"  , cdrazon);
		params.put("pv_comenta_i"  , comenta);
		params.put("pv_feefecto_i" , feefecto);
		params.put("pv_fevencim_i" , fevencim);
		params.put("pv_fecancel_i" , fecancel);
		params.put("pv_usuario_i"  , cdusuari);
		params.put("pv_cdtipsup_i" , cdtipsup);
		params.put("pv_cdsisrol_i" , cdsisrol);
		Utils.debugProcedure(logger, "pkg_cancela.p_cancela_poliza", params);
		Map<String,Object> resParams = ejecutaSP(new CancelaPoliza(getDataSource()),params);
		
		return resParams;
	}
	
	protected class CancelaPoliza extends StoredProcedure
	{
		public CancelaPoliza(DataSource dataSource)
		{
			super(dataSource, "pkg_cancela.p_cancela_poliza");
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cduniage_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdrazon_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_comenta_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_feefecto_i" , OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_fevencim_i" , OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_fecancel_i" , OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_usuario_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsup_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdsisrol_i" , OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_nmsuplem_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_ntramite_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_tipoflot_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_nsuplogi_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_texto_o"  , OracleTypes.VARCHAR));
					
		}
	}
	/*/////////////////////////*/
	////// cancelar poliza //////
	/////////////////////////////
	
	/////////////////////////////////////////////
	////// grabar poliza unica en tagrucan //////
	/*/////////////////////////////////////////*/
	@Override
	@Deprecated
	public void seleccionaPolizaUnica(Map<String,Object> params) throws Exception
	{
		ejecutaSP(new SeleccionaPolizaUnica(this.getDataSource()), params);
	}
	
	@Override
	public void seleccionaPolizaUnica(
			String cdunieco
			,String cdramo
			,String nmpoliza
			,String agencia
			,Date   fechapro
			)throws Exception
	{
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("pv_cdunieco_i" , cdunieco);
		params.put("pv_cdramo_i"   , cdramo);
		params.put("pv_nmpoliza_i" , nmpoliza);
		params.put("pv_agencia_i"  , agencia);
		params.put("pv_fechapro_i" , fechapro);
		Utils.debugProcedure(logger, "pkg_cancela.p_selecciona_poliza_unica", params);
		ejecutaSP(new SeleccionaPolizaUnica(this.getDataSource()), params);
	}
	
	protected class SeleccionaPolizaUnica extends StoredProcedure
	{
		public SeleccionaPolizaUnica(DataSource dataSource)
		{
			super(dataSource, "pkg_cancela.p_selecciona_poliza_unica");
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_agencia_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fechapro_i" , OracleTypes.DATE));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	/*/////////////////////////////////////////*/
	////// grabar poliza unica en tagrucan //////
	/////////////////////////////////////////////
	
	////////////////////////////////////////////
	////// actualizar polizas en tagrucan //////
	/*////////////////////////////////////////*/
	@Override
	public void actualizarTagrucan(Map<String,String> params) throws Exception
	{
		ejecutaSP(new ActualizarTagrucan(this.getDataSource()), params);
	}
	
	protected class ActualizarTagrucan extends StoredProcedure
	{
		public ActualizarTagrucan(DataSource dataSource)
		{
			super(dataSource, "pkg_cancela.p_actualiza_tagrucan");
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swcancel_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_texto_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	/*////////////////////////////////////////*/
	////// actualizar polizas en tagrucan //////
	////////////////////////////////////////////
	
	//////////////////////////////////////////
	////// cancelar polizas en tagrucan //////
	/*//////////////////////////////////////*/
	@Override
	public void cancelacionMasiva(Map<String,String> params) throws Exception
	{
		ejecutaSP(new CancelacionMasiva(this.getDataSource()), params);
	}
	
	protected class CancelacionMasiva extends StoredProcedure
	{
		public CancelacionMasiva(DataSource dataSource)
		{
			super(dataSource, "pkg_cancela.p_cancelacion_masiva");
			declareParameter(new SqlParameter("pv_id_proceso_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fecha_carga_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_usuario_i"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsup_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdsisrol_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_texto_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	/*//////////////////////////////////////*/
	////// cancelar polizas en tagrucan //////
	//////////////////////////////////////////
	
	/**
	 * PKG_CONSULTA.P_IMP_DOC_CANCELACION
	 * @return nmsolici,nmsituac,descripc,descripl,ntramite,nmsuplem
	 */
	@Override
	public List<Map<String,String>> reimprimeDocumentos(Map<String,String>params) throws Exception
	{
		Map<String,Object> resultadoMap=this.ejecutaSP(new ReimprimeDocumentos(this.getDataSource()), params);
		return (List<Map<String, String>>) resultadoMap.get("PV_REGISTRO_O");
	}

	protected class ReimprimeDocumentos extends StoredProcedure
	{
		String columnas[]=new String[]{
				"nmsolici"
				,"nmsituac"
				,"descripc"
				,"descripl"
				,"ntramite"
				,"nmsuplem"
		};
		protected ReimprimeDocumentos(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_IMP_DOC_CANCELACION");
			declareParameter(new SqlParameter("PV_CDUNIECO_I" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_CDRAMO_I"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_ESTADO_I"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_NMPOLIZA_I" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_TIPMOV_I"   , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("PV_REGISTRO_O" , OracleTypes.CURSOR, new GenericMapper(columnas)));
			declareParameter(new SqlOutParameter("PV_MSG_ID_O"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("PV_TITLE_O"    , OracleTypes.VARCHAR));
		}
	}
	
	
	@Override
	public ArrayList<PolizaVO> obtienePolizasCancelacionMasiva(Map<String,String> params) throws Exception{
		
		try {

			Map<String, Object> resultado = ejecutaSP(new PolizasCanceladas(getDataSource()), params);
			return (ArrayList<PolizaVO>) resultado.get("pv_registro_o");
			
		} catch (Exception e) {
			throw new DaoException(e.getMessage(), e);
		}
	}
	
	protected class PolizasCanceladas extends StoredProcedure {
    	protected PolizasCanceladas(DataSource dataSource) {
            super(dataSource,"PKG_CONSULTA.P_CONS_POL_CANCELADAS");
            declareParameter(new SqlParameter("pv_feproces_i",       OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new PolizasCanceladasMapper()));
            declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    protected class PolizasCanceladasMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            PolizaVO pol=new PolizaVO();
            pol.setCdunieco(rs.getString("CDUNIECO"));
            pol.setCdramo(rs.getString("CDRAMO"));
            pol.setEstado(rs.getString("ESTADO"));
            pol.setNmpoliza(rs.getString("NMPOLIZA"));
            pol.setNmsuplem(rs.getString("NMSUPLEM"));
            return pol;
        }
    }

	@Override
	public void validaCancelacionAProrrata(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		Utils.debugProcedure(logger, "PKG_SATELITES.P_VALIDA_CANC_A_PRORRATA", params);
		ejecutaSP(new ValidaCancelacionAProrrata(getDataSource()),params);
	}

	protected class ValidaCancelacionAProrrata extends StoredProcedure
	{
    	protected ValidaCancelacionAProrrata(DataSource dataSource)
    	{
            super(dataSource , "PKG_SATELITES.P_VALIDA_CANC_A_PRORRATA");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(new String[]{})));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }

	@Override
	public boolean validaRazoCancelacion(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdrazon
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("pv_cdunieco_i" , cdunieco);
		params.put("pv_cdramo_i"   , cdramo);
		params.put("pv_estado_i"   , estado);
		params.put("pv_nmpoliza_i" , nmpoliza);
		params.put("pv_cdrazon_i"  , cdrazon);
		Utils.debugProcedure(logger, "PKG_SATELITES2.P_VALIDA_RAZON_CANCELACION", params);
		ejecutaSP(new ValidaRazonCancelacion(getDataSource()),params);
		
		return true;
	}
	
	protected class ValidaRazonCancelacion extends StoredProcedure
	{
		protected ValidaRazonCancelacion(DataSource dataSource)
		{
			super(dataSource , "PKG_SATELITES2.P_VALIDA_RAZON_CANCELACION");
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdrazon_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(new String[]{})));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
}