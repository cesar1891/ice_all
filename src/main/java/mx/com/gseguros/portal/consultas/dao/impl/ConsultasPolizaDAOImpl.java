package mx.com.gseguros.portal.consultas.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.jfree.util.Log;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import mx.com.gseguros.portal.consultas.dao.ConsultasPolizaDAO;
import mx.com.gseguros.portal.consultas.model.AseguradoDetalleVO;
import mx.com.gseguros.portal.consultas.model.AseguradoVO;
import mx.com.gseguros.portal.consultas.model.CoberturaBasicaVO;
import mx.com.gseguros.portal.consultas.model.ContratanteVO;
import mx.com.gseguros.portal.consultas.model.CopagoVO;
import mx.com.gseguros.portal.consultas.model.DatosComplementariosVO;
import mx.com.gseguros.portal.consultas.model.HistoricoFarmaciaVO;
import mx.com.gseguros.portal.consultas.model.HistoricoVO;
import mx.com.gseguros.portal.consultas.model.PeriodoVigenciaVO;
import mx.com.gseguros.portal.consultas.model.PlanVO;
import mx.com.gseguros.portal.consultas.model.PolizaAseguradoVO;
import mx.com.gseguros.portal.consultas.model.PolizaDTO;
import mx.com.gseguros.portal.consultas.model.ReciboAgenteVO;
import mx.com.gseguros.portal.consultas.model.SuplementoVO;
import mx.com.gseguros.portal.consultas.model.TarifaVO;
import mx.com.gseguros.portal.cotizacion.model.AgentePolizaVO;
import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.dao.impl.DynamicMapper;
import mx.com.gseguros.portal.dao.impl.GenericMapper;
import mx.com.gseguros.portal.general.model.ClausulaVO;
import mx.com.gseguros.portal.general.model.DetalleReciboVO;
import mx.com.gseguros.portal.general.model.PolizaVO;
import mx.com.gseguros.portal.general.model.ReciboVO;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.Utils;
import oracle.jdbc.OracleTypes;

public class ConsultasPolizaDAOImpl extends AbstractManagerDAO implements ConsultasPolizaDAO {

	//private final static Logger logger = Logger.getLogger(ConsultasPolizaDAOImpl.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PolizaDTO> obtieneDatosPoliza(PolizaAseguradoVO polizaAsegurado) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", polizaAsegurado.getCdunieco());
		params.put("pv_cdramo_i",   polizaAsegurado.getCdramo());
		params.put("pv_estado_i",   polizaAsegurado.getEstado());
		params.put("pv_nmpoliza_i", polizaAsegurado.getNmpoliza());
		Map<String, Object> mapResult = ejecutaSP(new ObtieneDatosPolizaSP(getDataSource()), params);
		return (List<PolizaDTO>) mapResult.get("pv_registro_o");
	}
	
	protected class ObtieneDatosPolizaSP extends StoredProcedure {

		protected ObtieneDatosPolizaSP(DataSource dataSource) {
			
			super(dataSource, "PKG_CONSULTA.P_Get_Datos_Poliza");
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatosPolizaMapper()));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
    protected class DatosPolizaMapper  implements RowMapper<PolizaDTO> {
    	
        public PolizaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	
        	PolizaDTO consulta = new PolizaDTO();
        	consulta.setNmsolici(rs.getString("nmsolici"));
        	consulta.setFeefecto(Utils.formateaFecha(rs.getString("feefecto")));
        	consulta.setNmpoliex(rs.getString("nmpoliex"));
        	consulta.setCduniext(rs.getString("cduniext"));
        	consulta.setCdramoext(rs.getString("ramo"));
        	consulta.setFeemisio(Utils.formateaFecha(rs.getString("feemisio")));
        	consulta.setCdmoneda(rs.getString("cdmoneda"));
        	consulta.setDsmoneda(rs.getString("dsmoneda"));
        	consulta.setOttempot(rs.getString("ottempot"));
        	consulta.setDstempot(rs.getString("dstempot"));
        	consulta.setFeproren(Utils.formateaFecha(rs.getString("feproren")));
        	consulta.setFevencim(Utils.formateaFecha(rs.getString("fevencim")));
        	consulta.setNmrenova(rs.getString("nmrenova"));
        	consulta.setCdperpag(rs.getString("cdperpag"));
        	consulta.setDsperpag(rs.getString("dsperpag"));
        	consulta.setSwtarifi(rs.getString("swtarifi"));
        	consulta.setDstarifi(rs.getString("dstarifi"));
        	consulta.setCdtipcoa(rs.getString("cdtipcoa"));
        	consulta.setDstipcoa(rs.getString("dstipcoa"));
        	consulta.setNmcuadro(rs.getString("nmcuadro"));
        	consulta.setDscuadro(rs.getString("dscuadro"));
        	consulta.setPorredau(rs.getString("porredau"));
        	consulta.setPtpritot(rs.getString("ptpritot"));
        	consulta.setCdmotanu(rs.getString("cdmotanu"));
        	consulta.setDsmotanu(rs.getString("dsmotanu"));
        	consulta.setCdperson(rs.getString("cdperson"));
        	consulta.setTitular(rs.getString("titular"));
        	consulta.setCdrfc(rs.getString("cdrfc"));
        	consulta.setCdagente(rs.getString("cdagente"));
        	consulta.setStatuspoliza(rs.getString("status_poliza"));
        	consulta.setCdplan(rs.getString("cdplan"));
        	consulta.setCdtipsit(rs.getString("cdtipsit"));
        	consulta.setCdramo(rs.getString("cdramo"));
        	consulta.setCdunieco(rs.getString("cdunieco"));
        	consulta.setNmpolant(rs.getString("nmpolant"));
        	consulta.setDsunieco(rs.getString("dsunieco"));
        	consulta.setDsramo(rs.getString("dsramo"));
        	consulta.setDsplan(rs.getString("dsplan"));
        	consulta.setDstipsit(rs.getString("dstipsit"));

        	consulta.setReduceGS(rs.getString("reducegs"));
        	consulta.setGestoria(rs.getString("gestoria"));
        	consulta.setCobvida (rs.getString("cobvida"));

        	consulta.setTipopol(rs.getString("TIPOPOLIZA"));
        	
            return consulta;
        }
    }
    
    @SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> obtieneDatosPolizaTvalopol(PolizaAseguradoVO polizaAsegurado) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", polizaAsegurado.getCdunieco());
		params.put("pv_cdramo_i",   polizaAsegurado.getCdramo());
		params.put("pv_estado_i",   polizaAsegurado.getEstado());
		params.put("pv_nmpoliza_i", polizaAsegurado.getNmpoliza());
		params.put("p_nmsuplem_i", polizaAsegurado.getNmsuplem());
		Map<String, Object> mapResult = ejecutaSP(new ObtieneDatosPolizaTvalopolSP(getDataSource()), params);
		
		List<Map<String,String>>listaAtributos=(List<Map<String,String>>)mapResult.get("pv_registro_o");
		if(listaAtributos==null)
		{
			listaAtributos=new ArrayList<Map<String,String>>();
		}
		
		return listaAtributos;
	}
	
	protected class ObtieneDatosPolizaTvalopolSP extends StoredProcedure {

		protected ObtieneDatosPolizaTvalopolSP(DataSource dataSource) {
			
			super(dataSource, "PKG_CONSULTA.P_GET_INF_TVALOPOL");
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("p_nmsuplem_i", OracleTypes.VARCHAR));
			String[] cols=new String[]{
            		"OTVALOR"
            		,"DSATRIBU"
            		,"CDATRIBU"
            };
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
    @SuppressWarnings("unchecked")
	@Override
	public List<PolizaAseguradoVO> obtienePolizasAsegPromotor(String user, String rfc, String cdperson, String nombre) throws Exception {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_user_i",user); //Agrego parametro user: campo cdusurari de la tabla TUSUARIO
		params.put("pv_cdrfc_i", rfc);
		params.put("pv_cdperson_i", cdperson);
		params.put("pv_nombre_i", nombre);
		Map<String, Object> mapResult = ejecutaSP(new ConsultaPolizasAsegPromotorSP(getDataSource()), params);
		return (List<PolizaAseguradoVO>) mapResult.get("pv_registro_o");
		
	}
    
    protected class ConsultaPolizasAsegPromotorSP extends StoredProcedure {
		protected ConsultaPolizasAsegPromotorSP(DataSource dataSource) {
			super(dataSource, "PKG_CONSULTA.P_Get_Polizas_Aseg_Promotor");
			declareParameter(new SqlParameter("pv_user_i", OracleTypes.VARCHAR)); //Agrego parametro user: campo cdusurari de la tabla TUSUARIO
    		declareParameter(new SqlParameter("pv_cdrfc_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nombre_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new PolizaAsegPromotorMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
		}
	}
    
    protected class PolizaAsegPromotorMapper  implements RowMapper<PolizaAseguradoVO> {
    	
    	public PolizaAseguradoVO mapRow(ResultSet rs, int rowNum) throws SQLException {
    		PolizaAseguradoVO polizaAsegurado = new PolizaAseguradoVO();
    		polizaAsegurado.setCdramo(rs.getString("codigo_ramo"));
    		polizaAsegurado.setCdunieco(rs.getString("compania"));
    		polizaAsegurado.setDsramo(rs.getString("descripcion_ramo"));
    		polizaAsegurado.setDstipsit(rs.getString("dstipsit"));
    		polizaAsegurado.setCdsubram(rs.getString("cdsubram"));
    		polizaAsegurado.setDsunieco(rs.getString("descripcion"));
    		polizaAsegurado.setNombreAgente(rs.getString("cdagente"));
    		polizaAsegurado.setEstado(rs.getString("estado"));
    		polizaAsegurado.setNmpoliex(rs.getString("nmpoliex"));
    		polizaAsegurado.setNmpoliza(rs.getString("nmpoliza"));
    		polizaAsegurado.setNombreAsegurado(rs.getString("nombre"));
    		polizaAsegurado.setIcodpoliza(null); // No utilizado para ICE		
    		polizaAsegurado.setOrigen(rs.getString("origen"));
    		polizaAsegurado.setFeinivigencia(Utils.formateaFecha(rs.getString("feefecto")));// Sera el valor que posea la columna
    		polizaAsegurado.setFefinvigencia(Utils.formateaFecha(rs.getString("feproren")));// Sera el valor que posea la columna
    		return polizaAsegurado;
    	}
    }
    
	@SuppressWarnings("unchecked")
	@Override
	public List<PolizaAseguradoVO> obtienePolizasAsegurado(String user, String rfc, String cdperson, String nombre) throws Exception {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_user_i",user); //Agrego parametro user: campo cdusurari de la tabla TUSUARIO
		params.put("pv_cdrfc_i", rfc);
		params.put("pv_cdperson_i", cdperson);
		params.put("pv_nombre_i", nombre);
		Map<String, Object> mapResult = ejecutaSP(new ConsultaPolizasAseguradoSP(getDataSource()), params);
		return (List<PolizaAseguradoVO>) mapResult.get("pv_registro_o");
		
	}
	
	protected class ConsultaPolizasAseguradoSP extends StoredProcedure {
		protected ConsultaPolizasAseguradoSP(DataSource dataSource) {
			super(dataSource, "PKG_CONSULTA_PRUEBA.P_Get_Polizas_Asegurado");
			declareParameter(new SqlParameter("pv_user_i", OracleTypes.VARCHAR)); //Agrego parametro user: campo cdusurari de la tabla TUSUARIO
    		declareParameter(new SqlParameter("pv_cdrfc_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nombre_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new PolizaAseguradoMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
		}
	}
	
    protected class PolizaAseguradoMapper  implements RowMapper<PolizaAseguradoVO> {
    	
    	public PolizaAseguradoVO mapRow(ResultSet rs, int rowNum) throws SQLException {
    		PolizaAseguradoVO polizaAsegurado = new PolizaAseguradoVO();
    		polizaAsegurado.setCdramo(rs.getString("codigo_ramo"));
    		polizaAsegurado.setCdunieco(rs.getString("compania"));
    		polizaAsegurado.setDsramo(rs.getString("descripcion_ramo"));
    		polizaAsegurado.setDstipsit(rs.getString("dstipsit"));
    		polizaAsegurado.setCdsubram(rs.getString("cdsubram"));
    		polizaAsegurado.setDsunieco(rs.getString("descripcion"));
    		polizaAsegurado.setEstado(rs.getString("estado"));
    		polizaAsegurado.setNmpoliex(rs.getString("nmpoliex"));
    		polizaAsegurado.setNmpoliza(rs.getString("nmpoliza"));
    		polizaAsegurado.setNombreAsegurado(rs.getString("nombre"));
    		polizaAsegurado.setIcodpoliza(null); // No utilizado para ICE		
    		polizaAsegurado.setOrigen(rs.getString("origen"));
    		polizaAsegurado.setFeinivigencia(Utils.formateaFecha(rs.getString("feefecto")));// Sera el valor que posea la columna
    		polizaAsegurado.setFefinvigencia(Utils.formateaFecha(rs.getString("feproren")));// Sera el valor que posea la columna
    		return polizaAsegurado;
    	}
    }


	@SuppressWarnings("unchecked")
	@Override
	public List<SuplementoVO> obtieneHistoricoPoliza(PolizaAseguradoVO polizaAsegurado) throws Exception {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmpoliex_i", polizaAsegurado.getNmpoliex());
		params.put("pv_cdunieco_i", null);
		params.put("pv_cdramo_i"  , null);
		params.put("pv_nmpoliza_i", null);
		params.put("pv_ramo_i"    , null);
		params.put("pv_cdsisrol_i", polizaAsegurado.getCdsisrol() );
		
		Map<String, Object> mapResult = ejecutaSP(new ConsultaSuplementosSP(getDataSource()), params);
		return (List<SuplementoVO>) mapResult.get("pv_registro_o");
	}
	
	protected class ConsultaSuplementosSP extends StoredProcedure {
		protected ConsultaSuplementosSP(DataSource dataSource) {
			super(dataSource, "PKG_CONSULTA.p_get_datos_suplem");
			declareParameter(new SqlParameter("pv_nmpoliex_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdsisrol_i",OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new SuplementoMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SuplementoVO> obtieneHistoricoPolizaAsegurado(PolizaAseguradoVO polizaAsegurado) throws Exception {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmpoliex_i", polizaAsegurado.getNmpoliex());
		params.put("pv_cdunieco_i", polizaAsegurado.getCdunieco());
		params.put("pv_cdramo_i"  , polizaAsegurado.getCdramo());
		params.put("pv_nmpoliza_i", polizaAsegurado.getNmpoliza());
		params.put("pv_ramo_i"    , polizaAsegurado.getDsramo());
		params.put("pv_cdsisrol_i", polizaAsegurado.getCdsisrol() );
		params.put("pv_dsperson"  , polizaAsegurado.getNombreAsegurado());
		Map<String, Object> mapResult = ejecutaSP(new ConsultaSuplementosAseguradoSP(getDataSource()), params);
		return (List<SuplementoVO>) mapResult.get("pv_registro_o");
	}
	
	protected class ConsultaSuplementosAseguradoSP extends StoredProcedure {
		protected ConsultaSuplementosAseguradoSP(DataSource dataSource) {
			super(dataSource, "PKG_CONSULTA.p_get_datos_suplem");
			declareParameter(new SqlParameter("pv_nmpoliex_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdunieco_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ramo_i",        OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdsisrol_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsperson",      OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new SuplementoMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
    		compile();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SuplementoVO> obtieneHistoricoPolizaCorto(String sucursal, String producto, String polizacorto, String cdsisrol ) 
			throws Exception{
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("pv_nmpoliex_i", null);
		params.put("pv_cdunieco_i", sucursal);
		params.put("pv_cdramo_i"  , producto);
		params.put("pv_nmpoliza_i", polizacorto);
		params.put("pv_cdsisrol_i", cdsisrol);
		params.put("pv_ramo_i"    , null);
		
		Map<String, Object> mapResult = ejecutaSP(new ObtieneHistoricoPolizaCorto(getDataSource()), params);
		return (List<SuplementoVO>) mapResult.get("pv_registro_o");
	}
	
	protected class ObtieneHistoricoPolizaCorto extends StoredProcedure {
		protected ObtieneHistoricoPolizaCorto(DataSource dataSource) {
			super(dataSource, "PKG_CONSULTA.p_get_datos_suplem");
			declareParameter(new SqlParameter("pv_nmpoliex_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdsisrol_i",OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new SuplementoMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
    protected class SuplementoMapper  implements RowMapper<SuplementoVO> {
    	
    	public SuplementoVO mapRow(ResultSet rs, int rowNum) throws SQLException {
    		SuplementoVO suplemento = new SuplementoVO();
    		suplemento.setCdunieco(rs.getString("cdunieco"));
    		suplemento.setCdramo(rs.getString("cdramo"));
    		suplemento.setEstado(rs.getString("estado"));
    		suplemento.setNmpoliza(rs.getString("nmpoliza"));
    		suplemento.setFeinival(Utils.formateaFecha(rs.getString("feinival")));
    		suplemento.setNsuplogi(rs.getString("nsuplogi"));
    		suplemento.setFeemisio(Utils.formateaFecha(rs.getString("feemisio")));
    		suplemento.setNlogisus(rs.getString("nlogisus"));
    		suplemento.setDstipsup(rs.getString("dstipsup"));
    		suplemento.setPtpritot(rs.getString("ptpritot"));
    		suplemento.setNmsuplem(rs.getString("nmsuplem"));
    		suplemento.setOrigen(rs.getString("origen"));
    		suplemento.setSwitchConvenios(rs.getString("swconvenios"));
    		
    		return suplemento;
    	}
    }
    
    
	@SuppressWarnings("unchecked")
	@Override
	public List<CopagoVO> obtieneCopagosPoliza(PolizaVO poliza, String nmsituac) throws Exception {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", poliza.getCdunieco());
		params.put("pv_cdramo_i", poliza.getCdramo());
		params.put("pv_estado_i", poliza.getEstado());
		params.put("pv_nmpoliza_i", poliza.getNmpoliza());
		params.put("pv_nmsuplem_i", poliza.getNmsuplem());
		params.put("pv_nmsituac_i", nmsituac);
		Map<String, Object> mapResult = ejecutaSP(new ObtieneCopagosSP(getDataSource()), params);
		return (List<CopagoVO>) mapResult.get("pv_registro_o");
	}
	
    protected class ObtieneCopagosSP extends StoredProcedure {
    	
    	protected ObtieneCopagosSP(DataSource dataSource) {
    		super(dataSource, "PKG_CONSULTA.P_GET_COPAGOS");
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new CopagosMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    protected class CopagosMapper implements RowMapper<CopagoVO> {
    	
    	public CopagoVO mapRow(ResultSet rs, int rowNum) throws SQLException {
    		CopagoVO copago = new CopagoVO();
    		copago.setOrden(rs.getString("ORDEN"));
    		copago.setDescripcion(rs.getString("DESCRIPCION"));
    		copago.setValor(rs.getString("VALOR"));
    		copago.setNivel(rs.getInt("NIVEL"));
			copago.setVisible(
					(rs.getString("SWVISIBLE") != null && rs.getString("SWVISIBLE").equals(Constantes.SI)) ? 
					true: false);
    		return copago;
    	}
    }
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AseguradoVO> obtieneAsegurados(PolizaVO poliza) throws Exception {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", poliza.getCdunieco());
		params.put("pv_cdramo_i",   poliza.getCdramo());
		params.put("pv_nmpoliza_i", poliza.getNmpoliza());
		params.put("pv_nmsuplem_i", poliza.getNmsuplem());
		params.put("pv_estado_i",   poliza.getEstado());
		Map<String, Object> mapResult = ejecutaSP(new ObtieneAseguradosSP(getDataSource()), params);
		
		return (List<AseguradoVO>) mapResult.get("pv_registro_o");
	}
	
    protected class ObtieneAseguradosSP extends StoredProcedure {
    	
    	protected ObtieneAseguradosSP(DataSource dataSource) {
    		
    		super(dataSource, "PKG_CONSULTA.P_Get_Datos_Aseg");
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new AseguradoMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    protected class AseguradoMapper  implements RowMapper<AseguradoVO> {
    	
    	public AseguradoVO mapRow(ResultSet rs, int rowNum) throws SQLException {
    		AseguradoVO consulta = new AseguradoVO();
    		consulta.setCdperson(rs.getString("cdperson"));
    		consulta.setCdrfc(rs.getString("cdrfc"));
    		consulta.setCdrol(rs.getString("cdrol"));
    		consulta.setDsrol(rs.getString("dsrol"));
    		consulta.setNmsituac(rs.getString("nmsituac"));
    		consulta.setCdtipsit(rs.getString("cdtipsit"));
    		consulta.setNombre(rs.getString("titular"));
    		consulta.setFenacimi(Utils.formateaFecha(rs.getString("fenacimi")));
    		consulta.setSexo(rs.getString("Sexo"));
    		consulta.setStatus(rs.getString("status"));
    		consulta.setParentesco(rs.getString("parentesco"));
    		consulta.setGrupo(rs.getString("desgrupo"));
    		consulta.setCdgrupo(rs.getString("cvegrupo"));
    		consulta.setFamilia(rs.getString("desfamilia"));
    		consulta.setCdfamilia(rs.getString("cvefamilia"));
    		
    		consulta.setCdplan(rs.getString("CDPLAN"));
    		consulta.setDsplan(rs.getString("DSPLAN"));
    		
    		return consulta;
    	}
    }
    
public List<AseguradoVO> obtieneAsegurados(PolizaVO poliza,long start,long limit) throws Exception {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", poliza.getCdunieco());
		params.put("pv_cdramo_i",   poliza.getCdramo());
		params.put("pv_estado_i",   poliza.getEstado());
		params.put("pv_nmpoliza_i", poliza.getNmpoliza());
		params.put("pv_nmsuplem_i", poliza.getNmsuplem());
		params.put("pv_cdperson_i", poliza.getCdperson());
		params.put("pv_nmsitaux_i", poliza.getNmsitaux());
		params.put("pv_nombre_i", poliza.getNombre());
		params.put("pv_start_i", start);
		params.put("pv_limit_i",limit);
		params.put("pv_familia_i", poliza.getFamilia());
		Map<String, Object> mapResult = ejecutaSP(new ObtieneAseguradosSP2(getDataSource()), params);
		
		return (List<AseguradoVO>) mapResult.get("pv_registro_o");
	}
	
    protected class ObtieneAseguradosSP2 extends StoredProcedure {
    	
    	protected ObtieneAseguradosSP2(DataSource dataSource) {
    		
    		super(dataSource, "PKG_CONSULTA_PRUEBA.P_Get_Datos_Aseg_f");
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdperson_i",OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmsitaux_i",OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nombre_i",OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_start_i",OracleTypes.NUMBER));
    		declareParameter(new SqlParameter("pv_limit_i",OracleTypes.NUMBER));
    		declareParameter(new SqlParameter("pv_familia_i",OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new AseguradoMapper2()));
    		declareParameter(new SqlOutParameter("pv_num_rec_o", OracleTypes.NUMBER));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    protected class AseguradoMapper2  implements RowMapper<AseguradoVO> {
    	
    	public AseguradoVO mapRow(ResultSet rs, int rowNum) throws SQLException {
    		AseguradoVO consulta = new AseguradoVO();
    		consulta.setCdperson(rs.getString("cdperson"));
    		consulta.setCdrfc(rs.getString("cdrfc"));
    		consulta.setCdrol(rs.getString("cdrol"));
    		consulta.setDsrol(rs.getString("dsrol"));
    		consulta.setNmsituac(rs.getString("nmsituac"));
    		consulta.setCdtipsit(rs.getString("cdtipsit"));
    		consulta.setNombre(rs.getString("titular"));
    		consulta.setFenacimi(Utils.formateaFecha(rs.getString("fenacimi")));
    		consulta.setSexo(rs.getString("Sexo"));
    		consulta.setStatus(rs.getString("status"));
    		consulta.setParentesco(rs.getString("parentesco"));
    		consulta.setGrupo(rs.getString("desgrupo"));
    		consulta.setCdgrupo(rs.getString("cvegrupo"));
    		consulta.setFamilia(rs.getString("desfamilia"));
    		consulta.setCdfamilia(rs.getString("cvefamilia"));
    		
    		consulta.setCdplan(rs.getString("CDPLAN"));
    		consulta.setDsplan(rs.getString("DSPLAN"));
    		
    		consulta.setTotal(rs.getLong("total")); //total - pv_num_rec_o
    		
    		
    		return consulta;
    	}
    }

	
	@SuppressWarnings("unchecked")
	@Override
	public List<ClausulaVO> obtieneEndososPoliza(PolizaVO poliza, AseguradoVO asegurado) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco", poliza.getCdunieco());
		params.put("pv_cdramo", poliza.getCdramo());
		params.put("pv_estado", poliza.getEstado());
		params.put("pv_nmpoliza", poliza.getNmpoliza());
		params.put("pv_nmsituac", poliza.getNmsituac());
		Map<String, Object> mapResult = ejecutaSP(new ObtenerMPolicotSP(getDataSource()), params);
		return (List<ClausulaVO>) mapResult.get("pv_registro_o");
	}
	
	protected class ObtenerMPolicotSP extends StoredProcedure {

		protected ObtenerMPolicotSP(DataSource dataSource) {
			super(dataSource, "PKG_SATELITES.P_OBTIENE_MPOLICOT");
			declareParameter(new SqlParameter("pv_cdunieco", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o",   OracleTypes.CURSOR, new ObtenerPolicotMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
			compile();
		}
	}
	
	protected class ObtenerPolicotMapper implements RowMapper<ClausulaVO> {
		public ClausulaVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			ClausulaVO claus = new ClausulaVO();
			claus.setCdclausu(rs.getString("cdclausu"));
			claus.setDsclausu(rs.getString("dsclausu"));
			claus.setCdtipcla(rs.getString("cdtipcla"));
			claus.setStatus(rs.getString("status"));
			claus.setLinea_usuario(rs.getString("linea_usuario"));
			claus.setLinea_general(rs.getString("linea_general"));
			claus.setSwmodi(rs.getString("swmodi"));
			claus.setCdunieco(rs.getString("cdunieco"));
			claus.setCdramo(rs.getString("cdramo"));
			claus.setEstado(rs.getString("estado"));
			claus.setNmpoliza(rs.getString("nmpoliza"));
			claus.setNmsituac(rs.getString("nmsituac"));
			claus.setNmsuplem(rs.getString("nmsuplem"));
			return claus;
		}
	}

	
	@Override
	public String obtieneMensajeAgente (PolizaVO poliza) throws Exception {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", poliza.getCdunieco());
		params.put("pv_cdramo_i",   poliza.getCdramo());
		params.put("pv_estado_i",   poliza.getEstado());
		params.put("pv_nmpoliza_i", poliza.getNmpoliza());
		Map<String, Object> result = ejecutaSP(new ObtieneMensajeAgenteSP(getDataSource()), params);
		return (String) result.get("pv_mensaje_o");
	}
	
	protected class ObtieneMensajeAgenteSP extends StoredProcedure {
		protected ObtieneMensajeAgenteSP(DataSource dataSource) {
			super(dataSource, "PKG_CONSULTA.P_Get_Datos_prevex");
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_mensaje_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TarifaVO> obtieneTarifasPoliza(PolizaVO poliza) throws Exception {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", poliza.getCdunieco());
		params.put("pv_cdramo_i", poliza.getCdramo());
		params.put("pv_estado_i", poliza.getEstado());
		params.put("pv_nmpoliza_i", poliza.getNmpoliza());
		params.put("pv_nmsuplem_i", poliza.getNmsuplem());
		Map<String, Object> mapResult = ejecutaSP(new ObtieneDatosTarifaSP(getDataSource()), params);
		return (List<TarifaVO>) mapResult.get("pv_registro_o");
	}
	
	
    protected class ObtieneDatosTarifaSP extends StoredProcedure {
    	protected ObtieneDatosTarifaSP(DataSource dataSource) {
    		super(dataSource, "PKG_CONSULTA.P_Get_Datos_Tarifa_Pol");
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new TarifaMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    protected class TarifaMapper  implements RowMapper<TarifaVO> {
    	public TarifaVO mapRow(ResultSet rs, int rowNum) throws SQLException {
    		TarifaVO consulta = new TarifaVO();
    		consulta.setCdgarant(rs.getString("GARANTIA"));
    		consulta.setDsgarant(rs.getString("NOMBRE_GARANTIA"));
    		consulta.setSumaAsegurada(rs.getString("SUMA_ASEGURADA"));
    		consulta.setMontoPrima(rs.getString("MONTO_PRIMA"));
    		consulta.setMontoComision(rs.getString("MONTO_COMISION"));
    		return consulta;
    	}
    }
    
    
	@Override
	public List<ReciboVO> obtieneRecibosPoliza(PolizaVO poliza)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DetalleReciboVO> obtieneDetalleRecibo(PolizaVO poliza,
			ReciboVO recibo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AgentePolizaVO> obtieneAgentesPoliza(PolizaVO poliza) throws Exception {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("PV_CDUNIECO_I", poliza.getCdunieco());
		params.put("PV_CDRAMO_I", poliza.getCdramo());
		params.put("PV_ESTADO_I", poliza.getEstado());
		params.put("PV_NMPOLIZA_I", poliza.getNmpoliza());
		params.put("PV_NMSUPLEM_I", poliza.getNmsuplem());
		Map<String, Object> mapResult = ejecutaSP(new ObtieneAgentesPolizaSP(getDataSource()), params);
		return (List<AgentePolizaVO>) mapResult.get("PV_REGISTRO_O");
	}
	
    protected class ObtieneAgentesPolizaSP extends StoredProcedure {
		protected ObtieneAgentesPolizaSP(DataSource dataSource) {
			super(dataSource, "PKG_CONSULTA.P_GET_AGENTE_POLIZA");
			declareParameter(new SqlParameter("PV_CDUNIECO_I" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_CDRAMO_I"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_ESTADO_I"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_NMPOLIZA_I" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_NMSUPLEM_I" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("PV_REGISTRO_O" , OracleTypes.CURSOR, new AgentePolizaMapper()));
			declareParameter(new SqlOutParameter("PV_MSG_ID_O"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("PV_TITLE_O"    , OracleTypes.VARCHAR));
			compile();
		}
	}
    
    protected class AgentePolizaMapper  implements RowMapper<AgentePolizaVO> {
    	public AgentePolizaVO mapRow(ResultSet rs, int rowNum) throws SQLException {
    		AgentePolizaVO agtePoliza = new AgentePolizaVO();
    		agtePoliza.setCdagente(rs.getString("CDAGENTE"));
    		agtePoliza.setCdsucurs(rs.getString("CDSUCURS"));
    		agtePoliza.setCdtipoAg(rs.getString("CDTIPOAG"));
    		agtePoliza.setDescripl(rs.getString("DESCRIPL"));
    		agtePoliza.setNmcuadro(rs.getString("NMCUADRO"));
    		agtePoliza.setNmsuplem(rs.getString("NMSUPLEM"));
    		agtePoliza.setNombre(rs.getString("NOMBRE"));
    		agtePoliza.setPorparti(rs.getString("PORPARTI"));
    		agtePoliza.setPorredau(rs.getString("PORREDAU"));
    		return agtePoliza;
    	}
    }
    
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ReciboAgenteVO> obtieneRecibosAgente(PolizaVO poliza) throws Exception {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", poliza.getCdunieco());
		params.put("pv_cdramo_i", poliza.getCdramo());
		params.put("pv_estado_i", poliza.getEstado());
		params.put("pv_nmpoliza_i", poliza.getNmpoliza());
		Map<String, Object> mapResult = ejecutaSP(new ObtieneRecibosAgenteSP(getDataSource()), params);
		return (List<ReciboAgenteVO>) mapResult.get("pv_registro_o");
	}
	
    protected class ObtieneRecibosAgenteSP extends StoredProcedure {
    	protected ObtieneRecibosAgenteSP(DataSource dataSource) {
    		super(dataSource, "PKG_CONSULTA.P_Get_recibos_Agente");
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ReciboAgenteMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    protected class ReciboAgenteMapper  implements RowMapper<ReciboAgenteVO> {
    	public ReciboAgenteVO mapRow(ResultSet rs, int rowNum) throws SQLException {
    		ReciboAgenteVO consulta = new ReciboAgenteVO();
    		consulta.setNmrecibo(rs.getString("NMRECIBO"));
    		consulta.setFeinicio(Utils.formateaFecha(rs.getString("Fecha_inicio")));
    		consulta.setFefin(Utils.formateaFecha(rs.getString("Fecha_fin")));
    		consulta.setDsgarant(rs.getString("DSGARANT"));
    		consulta.setPtimport(rs.getString("PTIMPORT"));
    		return consulta;
    	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String,String>> obtieneRecibosPolizaAuto(String cdunieco,String cdramo,String cdestado,String nmpoliza,String nmsuplem) throws Exception {
    	HashMap<String, Object> params = new HashMap<String, Object>();
    	params.put("pv_cdunieco_i", cdunieco);
    	params.put("pv_cdramo_i"  , cdramo);
    	params.put("pv_estado_i"  , cdestado);
    	params.put("pv_nmpoliza_i", nmpoliza);
    	params.put("pv_nmsuplem_i", nmsuplem);
    	Map<String, Object> mapResult = ejecutaSP(new ObtieneRecibosPolizaAuto(getDataSource()), params);
    	return (List<Map<String,String>>) mapResult.get("pv_registro_o");
    	
    }
    
    protected class ObtieneRecibosPolizaAuto extends StoredProcedure {
    	protected ObtieneRecibosPolizaAuto(DataSource dataSource) {
    		super(dataSource, "PKG_CONSULTA.P_GET_RECIBOS_AUTOS");
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));

    		String[] cols=new String[]{
            		"FECINI"
            		,"FECTER"
            		,"TIPEND"
            		,"NUMEND"
            		,"NUMREC"
            		,"TOTALREC"
            		,"PRIMA"
            		,"IVA"
            		,"RECARGOS"
            		,"DERECHOS"
            		,"CESIONCOM"
            		,"COMISIONPRIMA"
            		,"COMISIONRECARGO"
            		,"MODO"
            		,"ESTATUS"
            		,"CDESTADO"
            };
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public int obtieneNumeroDeIncisosPoliza(String cdunieco,String cdramo,String cdestado,String nmpoliza,String nmsuplem) throws Exception{
    	
    	int incisos = 0;
    	 
    	HashMap<String, Object> params = new HashMap<String, Object>();
    	params.put("pv_cdunieco_i", cdunieco);
    	params.put("pv_cdramo_i"  , cdramo);
    	params.put("pv_estado_i"  , cdestado);
    	params.put("pv_nmpoliza_i", nmpoliza);
    	params.put("pv_nmsuplem_i", nmsuplem);
    	Map<String, Object> mapResult = ejecutaSP(new ObtieneNumeroDeIncisosPoliza(getDataSource()), params);
    	
    	List<Map<String,String>>lista=(List<Map<String,String>>)mapResult.get("pv_registro_o");
    	
    	if(lista!= null && !lista.isEmpty()){
    		Map<String, String> fila = lista.get(0);
    		if(fila!= null && fila.containsKey("NUMINCISOS") && !StringUtils.isEmpty(fila.get("NUMINCISOS"))){
    			incisos = Integer.parseInt(fila.get("NUMINCISOS"));
    		}
    	}
    	
    	Log.debug("Resultado:::: PKG_CONSULTA.P_GET_TOTAL_INCISOS_X_POLIZA, Se obtuvieron "+incisos+" Incisos para la Poliza " + nmpoliza);
    	
    	return incisos;
    	
    }
    
    protected class ObtieneNumeroDeIncisosPoliza extends StoredProcedure {
    	protected ObtieneNumeroDeIncisosPoliza(DataSource dataSource) {
    		super(dataSource, "PKG_CONSULTA.P_GET_TOTAL_INCISOS_X_POLIZA");
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
    		
    		String[] cols=new String[]{
    				"NUMINCISOS"
    		};
    		declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public String obtieneNmsituacContratantePoliza(String cdunieco,String cdramo,String cdestado,String nmpoliza) throws Exception{
    	
    	String nmsituac = null; // valor default 
    	
    	HashMap<String, Object> params = new HashMap<String, Object>();
    	params.put("pv_cdunieco_i", cdunieco);
    	params.put("pv_cdramo_i"  , cdramo);
    	params.put("pv_estado_i"  , cdestado);
    	params.put("pv_nmpoliza_i", nmpoliza);
    	Map<String, Object> mapResult = ejecutaSP(new ObtieneNmsituacContratantePoliza(getDataSource()), params);
    	
    	List<Map<String,String>>lista=(List<Map<String,String>>)mapResult.get("pv_registro_o");
    	
    	if(lista!= null && !lista.isEmpty()){
    		Map<String, String> fila = lista.get(0);
    		if(fila!= null && fila.containsKey("NMSITUAC") && !StringUtils.isEmpty(fila.get("NMSITUAC"))){
    			nmsituac = fila.get("NMSITUAC");
    		}
    	}
    	
    	Log.debug("Resultado:::: PKG_CONSULTA.P_GET_NMSITUAC_CONTRATANTE, Se obtuvo nmsituac:"+nmsituac+" para el contratante de la Poliza " + nmpoliza);
    	
    	return nmsituac;
    	
    }
    
    protected class ObtieneNmsituacContratantePoliza extends StoredProcedure {
    	protected ObtieneNmsituacContratantePoliza(DataSource dataSource) {
    		super(dataSource, "PKG_CONSULTA.P_GET_NMSITUAC_CONTRATANTE");
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
    		
    		String[] cols=new String[]{
    				"NMSITUAC"
    		};
    		declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
	@Override
	public List<AseguradoDetalleVO> obtieneAseguradoDetalle(
			AseguradoVO asegurado) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HistoricoVO> obtieneHistoricoPolizaSISA(
			PolizaAseguradoVO polizaAsegurado) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DatosComplementariosVO> obtieneDatosComplementarios(
			PolizaVO poliza, AseguradoVO asegurado) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HistoricoFarmaciaVO> obtieneHistoricoFarmacia(PolizaVO poliza,
			AseguradoVO asegurado) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CoberturaBasicaVO> obtieneCoberturasPoliza(PolizaVO poliza)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CoberturaBasicaVO> obtieneCoberturasBasicas(PolizaVO poliza)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PlanVO> obtieneDatosPlan(PolizaVO poliza)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContratanteVO> obtieneDatosContratante(
			PolizaVO poliza) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PeriodoVigenciaVO> obtienePeriodosVigencia(
			PolizaVO poliza, AseguradoVO asegurado) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, String>> getQueryResult(
			String query,
			String usuario
			) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("query"  , query);
		params.put("usuario", usuario);
		Map<String, Object> procRes = ejecutaSP(new GetQueryResult(getDataSource()), params);
		List<Map<String, String>> lista = (List<Map<String, String>>) procRes.get("pv_registro_o");
//		if (lista == null || lista.size() == 0) {
//			throw new ApplicationException("No se encuentra el tipo de endoso");
//		} else if (lista.size() > 1) {
//			throw new ApplicationException("No se pudo ejecutar query");
//		}
		return lista;
	}

	protected class GetQueryResult extends StoredProcedure {
    	protected GetQueryResult(DataSource dataSource) {
    		super(dataSource, "PKG_EXEC_SQL.GET_SALIDA");
            declareParameter(new SqlParameter("query"  , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o"  , OracleTypes.CURSOR,  new DynamicMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o"     , OracleTypes.VARCHAR));
    		compile();
    	}
    }
	
	@Override
	public List<Map<String, String>> executePLSQL(
			String archivo,
			String usuario
			) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("usuario", usuario);
		params.put("blob"   , archivo);		
		Map<String, Object> procRes = ejecutaSP(new ExecutePLSQL(getDataSource()), params);
		List<Map<String, String>> lista = null;//(List<Map<String, String>>) procRes.get("pv_registro_o");
		return lista;
	}

	protected class ExecutePLSQL extends StoredProcedure {
    	protected ExecutePLSQL(DataSource dataSource) {
    		super(dataSource, "PKG_EXEC_SQL.EJECUTAR");
    		declareParameter(new SqlParameter("usuario"  , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("blob"  , OracleTypes.CLOB));
//            declareParameter(new SqlOutParameter("pv_registro_o"  , OracleTypes.CURSOR,  new DinamicMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o"     , OracleTypes.VARCHAR));
    		compile();
    	}
    }
	
	@Override
    public Map<String,String> obtieneDatosLigasRecibosPoliza(String cdunieco,String cdramo,String cdestado,String nmpoliza, String nmsuplem) throws Exception{
    	
		Map<String,String> result = null; // valor default 
    	
    	HashMap<String, Object> params = new HashMap<String, Object>();
    	params.put("pv_cdunieco_i", cdunieco);
    	params.put("pv_cdramo_i"  , cdramo);
    	params.put("pv_estado_i"  , cdestado);
    	params.put("pv_nmpoliza_i", nmpoliza);
    	params.put("pv_nmsuplem_i", nmsuplem);
    	Map<String, Object> mapResult = ejecutaSP(new ObtieneDatosLigasRecibosPoliza(getDataSource()), params);
    	List<Map<String,String>>lista=(List<Map<String,String>>)mapResult.get("pv_registro_o");
    	
    	if(lista!= null && !lista.isEmpty()){
    		result = lista.get(0);
    	}
    	
    	Log.debug("Parametros obtenidos de  P_GET_DATOS_PARA_ENVIO_URL ::: " + result);
    	return result;
    	
    }
    
    protected class ObtieneDatosLigasRecibosPoliza extends StoredProcedure {
    	protected ObtieneDatosLigasRecibosPoliza(DataSource dataSource) {
    		super(dataSource, "P_GET_DATOS_PARA_ENVIO_URL");
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
    		
    		String[] cols=new String[]{
    				"NTRAMITE",
    				"NMSOLICI",
    				"CDTIPSUP"
    		};
    		declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> consultaIncisosPoliza(String cdunieco, String cdramo, String estado, String nmpoliza)
			throws Exception {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i", cdramo);
		params.put("pv_estado_i", estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		Map<String, Object> mapResult = ejecutaSP(new GetIncisosPolizaSP(getDataSource()), params);
		return (List<Map<String, String>>) mapResult.get("pv_registro_o");
	}
	
	protected class GetIncisosPolizaSP extends StoredProcedure {

		protected GetIncisosPolizaSP(DataSource dataSource) {
			super(dataSource, "PKG_CONSULTA2.P_GET_INCISOS_POLIZA");
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DynamicMapper()));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	 public String insertaBitacora(Date fecha,String nombre,int polizas,String rango,String usuario)throws Exception{
		 String resultado = null;
		 
		    Map<String, Object> params = new HashMap<String, Object>();
			params.put("pv_feproceso_i", fecha);
			params.put("pv_nomarchivo_i",   nombre);
			params.put("pv_nmpolreg_i",   polizas);
			params.put("pv_rangopolproc_i", rango);
			params.put("pv_nomusuario_i", usuario);
			Map<String, Object> mapResult = ejecutaSP(new InsertaBitacoraSP(getDataSource()), params);
			
		 return resultado;
	 }
	
	 protected class InsertaBitacoraSP extends StoredProcedure {

			protected InsertaBitacoraSP(DataSource dataSource) {
				super(dataSource, "P_INS_BITA_EMI_REMOTA_RECUPERA");
				declareParameter(new SqlParameter("pv_feproceso_i", OracleTypes.DATE));
				declareParameter(new SqlParameter("pv_nomarchivo_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_nmpolreg_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_rangopolproc_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_nomusuario_i", OracleTypes.VARCHAR));
		        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
		        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
				compile();
			}
		}
}