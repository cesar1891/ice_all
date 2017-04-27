package mx.com.gseguros.portal.rehabilitacion.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.dao.impl.GenericMapper;
import mx.com.gseguros.portal.rehabilitacion.dao.RehabilitacionDAO;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.Utils;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class RehabilitacionDAOImpl extends AbstractManagerDAO implements RehabilitacionDAO
{
	private static Logger logger = Logger.getLogger(RehabilitacionDAOImpl.class);
	
	@Deprecated
	@Override
	public List<Map<String, String>> buscarPolizas(Map<String,String> params) throws Exception
	{
		Map<String,Object> resultadoMap=this.ejecutaSP(new BuscarPolizas(this.getDataSource()), params);
		return (List<Map<String, String>>) resultadoMap.get("pv_registro_o");
	}
	
	@Override
	public List<Map<String, String>> buscarPolizas(
			String asegurado
			,String cdunieco
			,String cdramo
			,String nmpoliza
			,String nmsituac
			) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("pv_asegurado_i" , asegurado);
		params.put("pv_cdunieco_i"  , cdunieco);
		params.put("pv_cdramo_i"    , cdramo);
		params.put("pv_nmpoliza_i"  , nmpoliza);
		params.put("pv_nmsituac_i"  , nmsituac);
		Utils.debugProcedure(logger, "pkg_cancela.p_polizas_canc_a_rehabilitar", params);
		Map<String,Object> resultadoMap = this.ejecutaSP(new BuscarPolizas(this.getDataSource()), params);
		List<Map<String,String>> lista  = (List<Map<String, String>>) resultadoMap.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		Utils.debugProcedure(logger, "pkg_cancela.p_polizas_canc_a_rehabilitar", params, lista);
		return lista;
	}
	
	protected class BuscarPolizas extends StoredProcedure
	{
		protected BuscarPolizas(DataSource dataSource) {
			super(dataSource, "pkg_cancela.p_polizas_canc_a_rehabilitar");
			declareParameter(new SqlParameter("pv_asegurado_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdunieco_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i"  , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"ASEGURADO" , "CDUNIECO" , "ASEGURADORA" , "CDRAMO" 
					,"PRODUCTO" , "NMPOLIZA" , "NMSITUAC"    , "FECANCEL"
					,"CDRAZON"  , "DSRAZON"  , "COMENTARIOS" , "ESTADO"
					,"FEEFECTO" , "FEVENCIM" , "CDPERSON"    , "CDMONEDA"
					,"NMCANCEL" , "NMSUPLEM" , "CDELEMENTO"  , "DSELEMEN"
			};
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Deprecated
	@Override
	public Map<String,Object> rehabilitarPoliza(Map<String,String>params,String cdusuari, String cdsisrol) throws Exception
	{
		if(params!=null)
		{
			params.put("pv_cdusuari_i" , cdusuari);
			params.put("pv_cdsisrol_i" , cdsisrol);
		}
		return this.ejecutaSP(new RehabilitarPoliza(this.getDataSource()), Utils.ponFechas(params));
	}
	
	@Override
	public Map<String,Object> rehabilitarPoliza(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,Date feefecto
			,Date fevencim
			,Date fecancel
			,Date fereinst
			,String cdrazon
			,String cdperson
			,String cdmoneda
			,String nmcancel
			,String comments
			,String nmsuplem
			,String cdusuari
			,String cdsisrol
			)throws Exception
	{
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("pv_cdunieco_i" , cdunieco);
		params.put("pv_cdramo_i"   , cdramo);
		params.put("pv_estado_i"   , estado);
		params.put("pv_nmpoliza_i" , nmpoliza);
		params.put("pv_feefecto_i" , feefecto);
		params.put("pv_fevencim_i" , fevencim);
		params.put("pv_fecancel_i" , fecancel);
		params.put("pv_fereinst_i" , fereinst);
		params.put("pv_cdrazon_i"  , cdrazon);
		params.put("pv_cdperson_i" , cdperson);
		params.put("pv_cdmoneda_i" , cdmoneda);
		params.put("pv_nmcancel_i" , nmcancel);
		params.put("pv_comments_i" , comments);
		params.put("pv_nmsuplem_i" , nmsuplem);
		params.put("pv_cdusuari_i" , cdusuari);
		params.put("pv_cdsisrol_i" , cdsisrol);
		Utils.debugProcedure(logger, "pkg_cancela.p_rehabilita_poliza", params);
		Map<String,Object> procResult = ejecutaSP(new RehabilitarPoliza(getDataSource()),params);
		return procResult;
	}
	
	protected class RehabilitarPoliza extends StoredProcedure
	{
		protected RehabilitarPoliza(DataSource dataSource)
		{
			super(dataSource, "pkg_cancela.p_rehabilita_poliza");
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_feefecto_i" , OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_fevencim_i" , OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_fecancel_i" , OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_fereinst_i" , OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_cdrazon_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdmoneda_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmcancel_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_comments_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdusuari_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdsisrol_i" , OracleTypes.VARCHAR));
            
			declareParameter(new SqlOutParameter("pv_nmsuplem_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_ntramite_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_tipoflot_o" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	/**
	 * pkg_satelites.p_valida_antiguedad
	 */
	@Override
	public boolean validaAntiguedad(Map<String,String>params) throws Exception
	{
		boolean validacion = false; 
		Map<String,Object>mapa=this.ejecutaSP(new ValidaAntiguedad(this.getDataSource()), Utils.ponFechas(params));
		validacion = mapa!=null && mapa.containsKey("pv_valor_o") 
				&& mapa.get("pv_valor_o")!=null && ((String)mapa.get("pv_valor_o")).equalsIgnoreCase(Constantes.SI);
		return validacion;
	}
	
	protected class ValidaAntiguedad extends StoredProcedure
	{
		protected ValidaAntiguedad(DataSource dataSource)
		{
			super(dataSource, "pkg_satelites.p_valida_antiguedad");
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
            
			declareParameter(new SqlOutParameter("pv_valor_o"  , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	/**
	 * pkg_satelites.p_borra_antiguedad
	 */
	@Override
	public void borraAntiguedad(Map<String,String>params) throws Exception
	{
		this.ejecutaSP(new BorraAntiguedad(this.getDataSource()), Utils.ponFechas(params));
	}
	
	protected class BorraAntiguedad extends StoredProcedure
	{
		protected BorraAntiguedad(DataSource dataSource)
		{
			super(dataSource, "pkg_satelites.p_borra_antiguedad");
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fereinst_i" , OracleTypes.DATE));
            
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}

}
