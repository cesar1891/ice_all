package mx.com.gseguros.portal.siniestros.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.dao.impl.GenericMapper;
import mx.com.gseguros.portal.siniestros.dao.SiniestrosDAO;
import mx.com.gseguros.portal.siniestros.model.AltaTramiteVO;
import mx.com.gseguros.portal.siniestros.model.AutorizaServiciosVO;
import mx.com.gseguros.portal.siniestros.model.AutorizacionServicioVO;
import mx.com.gseguros.portal.siniestros.model.CoberturaPolizaVO;
import mx.com.gseguros.portal.siniestros.model.ConsultaManteniVO;
import mx.com.gseguros.portal.siniestros.model.ConsultaPorcentajeVO;
import mx.com.gseguros.portal.siniestros.model.ConsultaProveedorVO;
import mx.com.gseguros.portal.siniestros.model.ConsultaTDETAUTSVO;
import mx.com.gseguros.portal.siniestros.model.ConsultaTTAPVAATVO;
import mx.com.gseguros.portal.siniestros.model.DatosSiniestroVO;
import mx.com.gseguros.portal.siniestros.model.ListaFacturasVO;
import mx.com.gseguros.portal.siniestros.model.MesaControlVO;
import mx.com.gseguros.portal.siniestros.model.PolizaVigenteVO;
import mx.com.gseguros.portal.siniestros.model.SiniestroVO;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.Utils;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.Reclamo;
import oracle.jdbc.OracleTypes;

public class SiniestrosDAOImpl extends AbstractManagerDAO implements SiniestrosDAO {

	private static org.apache.log4j.Logger logger  = org.apache.log4j.Logger.getLogger(SiniestrosDAOImpl.class);
	private static org.slf4j.Logger        logger2 = LoggerFactory.getLogger(SiniestrosDAOImpl.class);
	
	@Override
	public List<AutorizacionServicioVO> obtieneDatosAutorizacionEsp(String nmautser) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmautser_i", nmautser);
		
		Map<String, Object> mapResult = ejecutaSP(new ObtieneAutorizacionServicioSP(getDataSource()), params);
		
		@SuppressWarnings("unchecked")
		List<AutorizacionServicioVO> listaDatosPoliza = (List<AutorizacionServicioVO>)mapResult.get("pv_registro_o");
		return listaDatosPoliza;
	}
	
	protected class ObtieneAutorizacionServicioSP extends StoredProcedure {

		protected ObtieneAutorizacionServicioSP(DataSource dataSource) {
			super(dataSource, "PKG_PRESINIESTRO.P_CONSULTA_MAUTSERV");
			declareParameter(new SqlParameter("pv_nmautser_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatosAutorizacionServicioMapper()));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
    protected class DatosAutorizacionServicioMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	AutorizacionServicioVO consulta = new AutorizacionServicioVO();
        	consulta.setNmautser(rs.getString("NMAUTSER"));
        	consulta.setNmautant(rs.getString("NMAUTANT"));
        	consulta.setCdperson(rs.getString("CDPERSON"));
        	consulta.setNombreCliente(rs.getString("NOMBRECLIENTE"));
        	consulta.setFesolici(Utils.formateaFecha(rs.getString("FESOLICI")));
        	consulta.setFeautori(Utils.formateaFecha(rs.getString("FEAUTORI")));
        	consulta.setFevencim(Utils.formateaFecha(rs.getString("FEVENCIM")));
        	consulta.setFeingres(Utils.formateaFecha(rs.getString("FEINGRES")));
        	consulta.setCdunieco(rs.getString("CDUNIECO"));
        	consulta.setEstado(rs.getString("ESTADO"));
        	consulta.setCdramo(rs.getString("CDRAMO"));
        	consulta.setNmpoliza(rs.getString("NMPOLIZA"));
        	consulta.setNmsituac(rs.getString("NMSITUAC"));
        	consulta.setCduniecs(rs.getString("CDUNIECS"));
        	consulta.setCdgarant(rs.getString("CDGARANT"));
        	consulta.setDescGarantia(rs.getString("DESCGARANTIA"));
        	consulta.setCdconval(rs.getString("CDCONVAL"));
        	consulta.setDescSubGarantia(rs.getString("DESCSUBGARANTIA"));
        	consulta.setCdprovee(rs.getString("CDPROVEE"));
        	consulta.setNombreProveedor(rs.getString("NOMBREPROVEEDOR"));
        	consulta.setCdmedico(rs.getString("CDMEDICO"));
        	consulta.setNombreMedico(rs.getString("NOMBREMEDICO"));
        	consulta.setMtsumadp(rs.getString("MTSUMADP"));
        	consulta.setPorpenal(rs.getString("PORPENAL"));
        	consulta.setCopagofi(rs.getString("COPAGOFI"));
        	consulta.setCdicd(rs.getString("CDICD"));
        	consulta.setDescICD(rs.getString("DESCICD"));
        	consulta.setCdcausa(rs.getString("CDCAUSA"));
        	consulta.setDescCausa(rs.getString("DESCCAUSA"));
        	consulta.setAaapertu(rs.getString("AAAPERTU"));
        	consulta.setStatus(rs.getString("STATUS"));
        	consulta.setDstratam(rs.getString("DSTRATAM"));
        	consulta.setDsobserv(rs.getString("DSOBSERV"));
        	consulta.setDsnotas(rs.getString("DSNOTAS"));
        	consulta.setFesistem(Utils.formateaFecha(rs.getString("FESISTEM")));
        	consulta.setCduser(rs.getString("CDUSER"));
        	consulta.setEspecialidadMedico(rs.getString("ESPECMED"));
        	consulta.setCveTipoAutorizaG(rs.getString("TPAUTORI"));
        	consulta.setCdtipsit(rs.getString("CDTIPSIT"));
        	consulta.setAplicaCirHos(rs.getString("SWPECIHO"));
        	consulta.setAplicaZonaHosp(rs.getString("SWPEZOHO"));
        	consulta.setDescTipsit(rs.getString("DESCTIPSIT"));
        	consulta.setFenacimi(Utils.formateaFecha(rs.getString("FENACIMI"))); // (EGS)
        	consulta.setGenero(rs.getString("GENERO")); //(EGS)
        	consulta.setIdTipoEvento(rs.getString("TIPOEVENTO"));
        	consulta.setAplicaNeg(rs.getString("SWNEGOCI"));
        	consulta.setTipoServ(rs.getString("TIPOEVEN"));
        	consulta.setNumrecla(rs.getString("NUMRECLA"));
            return consulta;
        }
    }

	
    /******************************		ARCHIVO QUE SE TIENE QUE MODIFICAR 		***************************************/    
    @SuppressWarnings("unchecked")
	public List<GenericVO> obtieneListadoAsegurado(String cdperson) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdperson_i", cdperson);
		
		Map<String,Object> resultadoMap=this.ejecutaSP(new ObtenerListadoAsegurado(this.getDataSource()), params);
		return (List<GenericVO>) resultadoMap.get("pv_registro_o");
	}
	protected class ObtenerListadoAsegurado extends StoredProcedure
	{
		protected ObtenerListadoAsegurado(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.P_LISTA_ASEGURADO");
			declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosListaAsegurado()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	protected class DatosListaAsegurado  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	GenericVO consulta = new GenericVO();
        	consulta.setKey(rs.getString("CODIGO"));
        	consulta.setValue(rs.getString("ASEGURADO"));
            return consulta;
        }
    }
    
	@Override
	public List<AutorizaServiciosVO> obtieneListadoAutorizaciones(String tipoAut,String cdperson) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_tipo_aut_i", tipoAut);
		params.put("pv_cdperson_i", cdperson);
		
		Map<String, Object> mapResult = ejecutaSP(new ObtieneListadoAutorizacionServicioSP(getDataSource()), params);
		
		@SuppressWarnings("unchecked")
		List<AutorizaServiciosVO> listaDatosPoliza = (List<AutorizaServiciosVO>)mapResult.get("pv_registro_o");
		return listaDatosPoliza;
	}
	
	protected class ObtieneListadoAutorizacionServicioSP extends StoredProcedure {

		protected ObtieneListadoAutorizacionServicioSP(DataSource dataSource) {
			super(dataSource, "PKG_PRESINIESTRO.P_LISTA_AUTORIZA");
			declareParameter(new SqlParameter("pv_tipo_aut_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatosListaAutorizacionServicioMapper()));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
    protected class DatosListaAutorizacionServicioMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	AutorizaServiciosVO consulta = new AutorizaServiciosVO();
        	consulta.setNmautser(rs.getString("NMAUTSER"));
        	consulta.setNmautant(rs.getString("NMAUTANT"));
        	consulta.setFesolici(Utils.formateaFecha(rs.getString("FESOLICI")));
        	consulta.setPolizaafectada(rs.getString("POLIZAAFECTADA"));
        	consulta.setCdprovee(rs.getString("CDPROVEE"));
        	consulta.setNombreProveedor(rs.getString("NOMBREPROVEEDOR"));
        	consulta.setStatusTramite(rs.getString("STATUS"));
        	consulta.setDescICD(rs.getString("DESICD"));
        	consulta.setCobertura(rs.getString("COBERTURA"));
        	consulta.setSubcobertura(rs.getString("SUBCOBERTURA"));
            return consulta;
        }
    }
    
    
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaProveedorVO> obtieneListadoProvMedico(String tipoprov,String cdpresta) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_tipoprov_i", tipoprov);
		params.put("pv_cdpresta_i", cdpresta);
		
		Map<String, Object> mapResult = ejecutaSP(new ObtieneListadoProvMedicoSP(getDataSource()), params);
		return (List<ConsultaProveedorVO>) mapResult.get("pv_registro_o");
	}
	
	protected class ObtieneListadoProvMedicoSP extends StoredProcedure
	{
		protected ObtieneListadoProvMedicoSP(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.P_LISTA_PROVEEDOR");
			declareParameter(new SqlParameter("pv_tipoprov_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdpresta_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosListaProvMedico()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
    protected class DatosListaProvMedico  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	ConsultaProveedorVO consulta = new ConsultaProveedorVO();
        	consulta.setCdpresta(rs.getString("CDPRESTA"));
        	consulta.setNombre(rs.getString("NOMBRE"));
        	consulta.setCdespeci(rs.getString("CDESPECI"));
        	consulta.setDescesp(rs.getString("DESCESP"));
        	consulta.setCirculo(rs.getString("CIRCULO"));
        	consulta.setCodpos(rs.getString("CODPOS"));
        	consulta.setZonaHospitalaria(rs.getString("ZONA"));
            return consulta;
        }
    }
    
    
	@Override
	public List<CoberturaPolizaVO> obtieneListadoCoberturaPoliza(
			HashMap<String, Object> paramCobertura) throws Exception {
		Map<String, Object> mapResult = ejecutaSP(new ObtieneListadoCoberturaSP(getDataSource()), paramCobertura);
		
		@SuppressWarnings("unchecked")
		List<CoberturaPolizaVO> listaDatosPoliza = (List<CoberturaPolizaVO>)mapResult.get("pv_registro_o");
		return listaDatosPoliza;
	}
	protected class ObtieneListadoCoberturaSP extends StoredProcedure {

		protected ObtieneListadoCoberturaSP(DataSource dataSource) {
			super(dataSource, "PKG_PRESINIESTRO.P_LISTA_COBERT_POL");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_tipopago_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdgarant_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatosListaCoberturasMapper()));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<CoberturaPolizaVO> obtieneListadoCoberturaAsegurado(
			HashMap<String, Object> paramCobertura) throws Exception {
		Map<String, Object> mapResult = ejecutaSP(new ObtieneListadoCoberturaAsegurado(getDataSource()), paramCobertura);
		
		@SuppressWarnings("unchecked")
		List<CoberturaPolizaVO> listaDatosPoliza = (List<CoberturaPolizaVO>)mapResult.get("pv_registro_o");
		return listaDatosPoliza;
	}
	protected class ObtieneListadoCoberturaAsegurado extends StoredProcedure {

		protected ObtieneListadoCoberturaAsegurado(DataSource dataSource) {
			super(dataSource, "PKG_PRESINIESTRO.P_LISTA_COBERT_ASEG");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_tipopago_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdgarant_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatosListaCoberturasMapper()));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
    protected class DatosListaCoberturasMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	CoberturaPolizaVO consulta = new CoberturaPolizaVO();
        	consulta.setCdgarant(rs.getString("CDGARANT"));
        	consulta.setDsgarant(rs.getString("DSGARANT"));
        	consulta.setPtcapita(rs.getString("PTCAPITA"));
            return consulta;
        }
    }
    
	@Override
	public List<DatosSiniestroVO> obtieneListadoDatSubGeneral(
			HashMap<String, Object> paramDatSubGral) throws Exception {
		Map<String, Object> mapResult = ejecutaSP(new ObtieneListadoDatSubGeneralSP(getDataSource()), paramDatSubGral);
		
		@SuppressWarnings("unchecked")
		List<DatosSiniestroVO> listaDatosPoliza = (List<DatosSiniestroVO>)mapResult.get("pv_registro_o");
		return listaDatosPoliza;
	}
    
	protected class ObtieneListadoDatSubGeneralSP extends StoredProcedure {

		protected ObtieneListadoDatSubGeneralSP(DataSource dataSource) {
			super(dataSource, "PKG_PRESINIESTRO.P_LISTA_DAT_SUBG");

			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdgarant_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_subcober_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdpresta_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatosListaDatSubGeneralMapper()));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
    protected class DatosListaDatSubGeneralMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	DatosSiniestroVO consulta = new DatosSiniestroVO();
        	consulta.setCdgarant(rs.getString("CDGARANT"));
        	consulta.setSubcobertura(rs.getString("OTCLAVE2"));
        	consulta.setCdcapita(rs.getString("CDCAPITA"));
        	consulta.setLuc(rs.getString("LUC"));
        	consulta.setDeducible(rs.getString("DEDUCIBLE"));
        	consulta.setCopago(rs.getString("COPAGO"));
        	consulta.setBenefmax(rs.getString("BENEFMAX"));
        	consulta.setIcd(rs.getString("ICD"));
        	consulta.setCpt(rs.getString("CPT"));
        	consulta.setLimites(rs.getString("LIMITES"));
        	consulta.setTipoCopago(rs.getString("TIPOCOPAGO"));
            return consulta;
        }
    }
    
    @Override
	public List<GenericVO> obtieneListadoSubcobertura(String cdunieco, String cdramo, String estado, String nmpoliza,
					String nmsituac, String cdtipsit, String cdgarant, String cdsubcob,String cdrol) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i", 	cdramo); 
		params.put("pv_estado_i", 	estado); 
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_nmsituac_i", nmsituac);
		params.put("pv_cdtipsit_i", cdtipsit);
		params.put("pv_cdgarant_i", cdgarant);
		params.put("pv_cdsubcob_i", cdsubcob);
		params.put("pv_cdrol_i", 	cdrol);
		
		Map<String, Object> mapResult = ejecutaSP(new ObtieneListadoSubcoberturaSP(getDataSource()), params);
		return (List<GenericVO>) mapResult.get("pv_registro_o");
	}
	
	protected class ObtieneListadoSubcoberturaSP extends StoredProcedure
	{
		protected ObtieneListadoSubcoberturaSP(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.P_LISTA_SUB_COBERT");
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdgarant_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdsubcob_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdrol_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosListaSubcobertura()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO> obtieneListadoSubcoberturaTotales() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> mapResult = ejecutaSP(new ObtieneListadoSubcoberturaTotales(getDataSource()), params);
		return (List<GenericVO>) mapResult.get("pv_registro_o");
	}
	
	protected class ObtieneListadoSubcoberturaTotales extends StoredProcedure
	{
		protected ObtieneListadoSubcoberturaTotales(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.P_GET_SUBCOBERTURA_TOTALES");
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosListaSubcobertura()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO> obtieneListadoSubcoberturaTotalesMultisalud(String cdtipsit) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdtipsit_i", cdtipsit);
		Map<String, Object> mapResult = ejecutaSP(new ObtieneListadoSubcoberturaTotalesMultisalud(getDataSource()), params);
		return (List<GenericVO>) mapResult.get("pv_registro_o");
	}
	
	protected class ObtieneListadoSubcoberturaTotalesMultisalud extends StoredProcedure
	{
		protected ObtieneListadoSubcoberturaTotalesMultisalud(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.P_GET_CDCONVAL_MULTISALUD");
			declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosListaSubcobertura()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
    protected class DatosListaSubcobertura  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	GenericVO consulta = new GenericVO();
        	consulta.setKey(rs.getString("OTCLAVE"));
        	consulta.setValue(rs.getString("OTVALOR"));
            return consulta;
        }
    }
    
	@Override
	public List<GenericVO> obtieneListadoSubcoberturaRecupera() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> mapResult = ejecutaSP(new ObtieneListadoSubcoberturaRecupera(getDataSource()), params);
		return (List<GenericVO>) mapResult.get("pv_registro_o");
	}
	
	protected class ObtieneListadoSubcoberturaRecupera extends StoredProcedure
	{
		protected ObtieneListadoSubcoberturaRecupera(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.P_GET_SUBCOBERTURA_RECUPERA");
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosListaSubcobertura()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	public List<GenericVO> obtieneListadoCPTICD(String cdtabla, String otclave)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdtabla_i", cdtabla); // (EGS)
		params.put("pv_otclave_i", otclave); // (EGS)
		
		Map<String, Object> mapResult = ejecutaSP(new ObtieneListadoCPTICDSP(getDataSource()), params);
		return (List<GenericVO>) mapResult.get("pv_registro_o");
	}
	
	protected class ObtieneListadoCPTICDSP extends StoredProcedure
	{
		protected ObtieneListadoCPTICDSP(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.P_LISTA_TTAPVAT1");
			declareParameter(new SqlParameter("pv_cdtabla_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otclave_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosListaCPTICD()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
    protected class DatosListaCPTICD  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	GenericVO consulta = new GenericVO();
        	consulta.setKey(rs.getString("OTCLAVE"));
        	consulta.setValue(rs.getString("OTVALOR"));
            return consulta;
        }
    }
    
    
	/**
	 * (EGS)
	 */
    public List<GenericVO> obtieneListadoCPTICD(String cdicd, String cdramo, String cdtipsit, String edad, String genero)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdicd_i", cdicd);
		params.put("pv_cdramo_i", cdramo);
		params.put("pv_cdtipsit_i", cdtipsit);
		params.put("pv_edad_i", edad);
		params.put("pv_genero_i", genero);
		
		Map<String, Object> mapResult = ejecutaSP(new ObtieneListadoICDSP(getDataSource()), params);
		return (List<GenericVO>) mapResult.get("pv_registro_o");
	}
	
	protected class ObtieneListadoICDSP extends StoredProcedure
	{
		protected ObtieneListadoICDSP(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.P_CATALOGO_ICDXPROD"); // Nuevo PL a invocar (EGS)
			declareParameter(new SqlParameter("pv_cdicd_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_edad_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_genero_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosListaICD()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
    /**
     * Mapper para catalogo de ICD's restringidos
     * @author (EGS)
     */
	protected class DatosListaICD  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	GenericVO consulta = new GenericVO();
        	consulta.setKey(rs.getString("OTCLAVE1"));
        	consulta.setValue(rs.getString("OTVALOR01"));
            return consulta;
        }
    }
    
    
    public List<GenericVO> obtieneListadoTipoPago(String cdramo)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdramo_i", cdramo);
		Map<String, Object> mapResult = ejecutaSP(new ObtieneListadoTipoPago(getDataSource()), params);
		return (List<GenericVO>) mapResult.get("pv_registro_o");
	}
	
	protected class ObtieneListadoTipoPago extends StoredProcedure
	{
		protected ObtieneListadoTipoPago(DataSource dataSource)
		{
			super(dataSource, "PKG_CONSULTA.P_LISTA_TIPOPAGO");
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosListadoTipoPago()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
    protected class DatosListadoTipoPago  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	GenericVO consulta = new GenericVO();
        	consulta.setKey(rs.getString("OTVALOR01"));
        	consulta.setValue(rs.getString("OTVALOR02"));
            return consulta;
        }
    }
    
    public static void setLogger(org.apache.log4j.Logger logger) {
		SiniestrosDAOImpl.logger = logger;
	}
    
	@Override
	public List<ConsultaTDETAUTSVO> obtieneListadoTDeTauts(String nmautser)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmautser_i", nmautser);
		
		Map<String, Object> mapResult = ejecutaSP(new ObtieneListadoTDeTauts(getDataSource()), params);
		
		@SuppressWarnings("unchecked")
		List<ConsultaTDETAUTSVO> listadoTDeTauts = (List<ConsultaTDETAUTSVO>)mapResult.get("pv_registro_o");
		return listadoTDeTauts;
	}
	
	protected class ObtieneListadoTDeTauts extends StoredProcedure {

		protected ObtieneListadoTDeTauts(DataSource dataSource) {
			super(dataSource, "PKG_PRESINIESTRO.P_CONSULTA_TDETAUTS");
			declareParameter(new SqlParameter("pv_nmautser_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatosListadoTDeTauts()));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
    protected class DatosListadoTDeTauts  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	ConsultaTDETAUTSVO consulta = new ConsultaTDETAUTSVO();
        	consulta.setNmautser(rs.getString("NMAUTSER"));
        	consulta.setCdtipaut(rs.getString("CDTIPAUT"));
        	consulta.setCdmedico(rs.getString("CDMEDICO"));
        	consulta.setNombreMedico(rs.getString("NOMBREMEDICO"));
        	consulta.setCdtipmed(rs.getString("CDTIPMED"));
        	consulta.setCdcpt(rs.getString("CDCPT"));
        	consulta.setDesccpt(rs.getString("DESCCPT"));
        	consulta.setCantporc(rs.getString("CANTPORC"));
        	consulta.setPrecio(rs.getString("PRECIO"));
        	consulta.setPtimport(rs.getString("PTIMPORT"));
        	consulta.setDescTipMed(rs.getString("DESCTIPMED"));
            return consulta;
        }
    }
    
    
    
    
    /* ############################################################################## 
	 * ##################################### VERIFICAR ##############################*/

	@Override
	public List<AutorizacionServicioVO>  guardarAutorizacionServicio(Map<String, Object> paramsR)
			throws Exception {
		
		Map<String, Object> mapResult = ejecutaSP(new GuardaAutorizacionServicioSP(getDataSource()), paramsR);
		
		@SuppressWarnings("unchecked")
		List<AutorizacionServicioVO> listaDatosPoliza = (List<AutorizacionServicioVO>)mapResult.get("pv_registro_o");
		return listaDatosPoliza;
	}
	
	protected class GuardaAutorizacionServicioSP extends StoredProcedure {

		protected GuardaAutorizacionServicioSP(DataSource dataSource) {
			super(dataSource, "PKG_PRESINIESTRO.P_GUARDA_MAUTSERV");
			declareParameter(new SqlParameter("pv_nmautser_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmautant_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fesolici_i", OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_feautori_i", OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_fevencim_i", OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_feingres_i", OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cduniecs_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdgarant_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdconval_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdprovee_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdmedico_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_mtsumadp_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_porpenal_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_copagofi_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdicd_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdcausa_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_aaapertu_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dstratam_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsobserv_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsnotas_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fesistem_i", OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_cduser_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nombmedi_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_especmed_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_tpautori_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_idaplicaCirHosp_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_idaplicaZona_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatosGuardardoAutorizacionServicioMapper()));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
    protected class DatosGuardardoAutorizacionServicioMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	AutorizacionServicioVO consulta = new AutorizacionServicioVO();
        	consulta.setNmautser(rs.getString("nmautser"));
        	consulta.setNtramite(rs.getString("ntramite"));
            return consulta;
        }
    }

	@Override
	public String guardarListaTDeTauts(HashMap<String, Object> paramsTDeTauts)
			throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> mapResult = ejecutaSP(new GuardaListaTDeTautsSP(getDataSource()), paramsTDeTauts);
		return (String) mapResult.get("pv_registro_o");
	}
	
	protected class GuardaListaTDeTautsSP extends StoredProcedure {

		protected GuardaListaTDeTautsSP(DataSource dataSource) {
			super(dataSource, "PKG_PRESINIESTRO.P_GUARDA_TDETAUTS");
			declareParameter(new SqlParameter("pv_nmautser_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipaut_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdmedico_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipmed_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdctp_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_precio_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cantporc_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ptimport_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nombprov_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
    /*protected class DatosGuardardoListaTDeTautsMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	logger.debug("ENTRA A DatosGuardardoListaTDeTautsMapper");
        	logger.debug(rs);
        	logger.debug(rowNum);
        	logger.debug("SALE A DatosGuardardoListaTDeTautsMapper");
            return rs;
        }
    }*/


	@Override
	public List<ConsultaTTAPVAATVO> obtieneListadoTTAPVAAT(
			HashMap<String, Object> paramTTAPVAAT) throws Exception {
Map<String, Object> mapResult = ejecutaSP(new ObtieneListadoTTAPVAATSP(getDataSource()), paramTTAPVAAT);
		
		@SuppressWarnings("unchecked")
		List<ConsultaTTAPVAATVO> listaDatosPoliza = (List<ConsultaTTAPVAATVO>)mapResult.get("pv_registro_o");
		return listaDatosPoliza;
	}
	protected class ObtieneListadoTTAPVAATSP extends StoredProcedure {

		protected ObtieneListadoTTAPVAATSP(DataSource dataSource) {
			super(dataSource, "PKG_PRESINIESTRO.P_LISTA_TTAPVAAT");
			declareParameter(new SqlParameter("pv_cdtabla_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otclave1_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otclave2_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otclave3_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otclave4_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otclave5_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatosListaTTAPVAATMapper()));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
    protected class DatosListaTTAPVAATMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	logger.debug("VALOR DE RESPUESTA");
        	logger.debug(rs);
        	
        	logger.debug("VALOR DE colunma");
        	logger.debug(rowNum);
        	
        	ConsultaTTAPVAATVO consulta = new ConsultaTTAPVAATVO();
        	consulta.setNmtabla(rs.getString("NMTABLA"));
        	consulta.setOtclave1(rs.getString("OTCLAVE1"));
        	consulta.setOtclave2(rs.getString("OTCLAVE2"));
        	consulta.setOtclave3(rs.getString("OTCLAVE3"));
        	consulta.setOtclave4(rs.getString("OTCLAVE4"));
        	consulta.setOtclave5(rs.getString("OTCLAVE5"));
        	consulta.setFedesde(rs.getString("FEDESDE"));
        	consulta.setFehasta(rs.getString("FEHASTA"));
        	consulta.setOtvalor01(rs.getString("OTVALOR01"));
            return consulta;
        }
    }


	@Override
	public List<ConsultaManteniVO> obtieneListadoTipoMedico(String codigo) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_codigo_i", codigo);
		
		Map<String, Object> mapResult = ejecutaSP(new ObtieneListadoTipoMedico(getDataSource()), params);
		
		@SuppressWarnings("unchecked")
		List<ConsultaManteniVO> listaDatosPoliza = (List<ConsultaManteniVO>)mapResult.get("pv_registro_o");
		return listaDatosPoliza;
	}
	
	protected class ObtieneListadoTipoMedico extends StoredProcedure {

		protected ObtieneListadoTipoMedico(DataSource dataSource) {
			super(dataSource, "PKG_PRESINIESTRO.P_LISTA_TIPOMEDICO");
			declareParameter(new SqlParameter("pv_codigo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatosListadoMantenimientoMapper()));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
    protected class DatosListadoMantenimientoMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	ConsultaManteniVO consulta = new ConsultaManteniVO();
        	consulta.setCdtabla(rs.getString("CDTABLA"));
        	consulta.setCodigo(rs.getString("CODIGO"));
        	consulta.setDescripc(rs.getString("DESCRIPC"));
        	consulta.setDescripl(rs.getString("DESCRIPL"));
            return consulta;
        }
    }


	@Override
	public List<ConsultaPorcentajeVO> obtieneListadoPorcentaje(String cdcpt,
			String cdtipmed,String mtobase) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdcpt_i", cdcpt);
		params.put("pv_cdtipmed_i", cdtipmed);
		params.put("pv_mtobase_i", mtobase);
		
		
		Map<String, Object> mapResult = ejecutaSP(new ObtieneListadoPorcentajeSP(getDataSource()), params);
		
		@SuppressWarnings("unchecked")
		List<ConsultaPorcentajeVO> listaDatosPoliza = (List<ConsultaPorcentajeVO>)mapResult.get("pv_registro_o");
		return listaDatosPoliza;
	}
	
	protected class ObtieneListadoPorcentajeSP extends StoredProcedure {

		protected ObtieneListadoPorcentajeSP(DataSource dataSource) {
			super(dataSource, "PKG_PRESINIESTRO.P_LISTA_TABULADOR");
			declareParameter(new SqlParameter("pv_cdcpt_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipmed_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_mtobase_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatosListadoPorcentajeMapper()));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
    protected class DatosListadoPorcentajeMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	ConsultaPorcentajeVO consulta = new ConsultaPorcentajeVO();
        	consulta.setPorcentaje(rs.getString("PORCENTAJE"));
        	consulta.setMtomedico(rs.getString("MTOMEDICO"));
            return consulta;
        }
    }


	@Override
	public List<PolizaVigenteVO> obtieneListadoPoliza(String cdperson,String cdramo,String rolUsuario)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdperson_i", cdperson);
		params.put("pv_cdramo_i", cdramo);
		params.put("pv_cdsisrol_i", rolUsuario);
		
		Map<String,Object> resultadoMap=this.ejecutaSP(new ObtenerListadoPoliza(this.getDataSource()), params);
		return (List<PolizaVigenteVO>) resultadoMap.get("pv_registro_o");
	}
	protected class ObtenerListadoPoliza extends StoredProcedure
	{
		protected ObtenerListadoPoliza(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.P_LISTA_POLIZAS");
			declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdsisrol_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosListaPoliza()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	protected class DatosListaPoliza  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	logger.debug("Valor de RS "+rs);
        	PolizaVigenteVO consulta = new PolizaVigenteVO();
        	consulta.setCdunieco(rs.getString("CDUNIECO"));
        	consulta.setCdramo(rs.getString("CDRAMO"));
        	consulta.setEstado(rs.getString("ESTADO"));
        	consulta.setNmpoliza(rs.getString("NMPOLIZA"));
        	consulta.setNmsituac(rs.getString("NMSITUAC"));
        	consulta.setMtoBase(rs.getString("MTOBASE"));
        	consulta.setFeinicio(Utils.formateaFecha(rs.getString("FEINICIO")));
        	consulta.setFefinal(Utils.formateaFecha(rs.getString("FEFINAL")));
        	consulta.setDssucursal(rs.getString("DSUNIECO"));
        	consulta.setDsramo(rs.getString("DSRAMO"));
        	consulta.setEstatus(rs.getString("STATUS"));
        	consulta.setDsestatus(rs.getString("DSTATUS"));
        	consulta.setNmsolici(rs.getString("NMSOLICI"));
        	consulta.setNmsuplem(rs.getString("NMSUPLEM"));
        	consulta.setCdtipsit(rs.getString("CDTIPSIT"));
        	consulta.setEstatusCliente(rs.getString("STATUS_CLIENTE"));
        	if(rs.getString("STATUS_CLIENTE").equalsIgnoreCase("v"))
        	{
        		consulta.setDesEstatusCliente("Vigente");
        	}else{
        		consulta.setDesEstatusCliente("Cancelado");
        	}
        	
        	consulta.setFcancelacionAfiliado(Utils.formateaFecha(rs.getString("FCANCEL_AFILIADO")));
        	consulta.setFaltaAsegurado(Utils.formateaFecha(rs.getString("FALTA_ASEGURADO")));
        	consulta.setMtoBeneficioMax(rs.getString("BENEFICIO_MAXIMO"));
        	consulta.setZonaContratada(rs.getString("ZONA_CONTRATADA"));
        	consulta.setVigenciaPoliza(Utils.formateaFecha(rs.getString("FEINICIO"))+"\t\t|\t\t"+Utils.formateaFecha(rs.getString("FEFINAL")));
        	consulta.setNumPoliza(rs.getString("NUMPOLIZA"));
        	consulta.setDsplan(rs.getString("DSPLAN"));
        	consulta.setMesesAsegurado(rs.getString("MESESASEGURADO"));
        	consulta.setDiasAsegurado(rs.getString("DIASASEGURADO"));
        	consulta.setTelefono(rs.getString("TELEFONO"));
        	consulta.setEmail(rs.getString("EMAIL"));
        	consulta.setNombAsegurado(rs.getString("NOMBASEGURADO"));
        	consulta.setDsTipsit(rs.getString("DESCTIPSIT"));
        	consulta.setGenero(rs.getString("GENERO")); // (EGS)
        	consulta.setFenacimi(Utils.formateaFecha(rs.getString("FENACIMI"))); // (EGS)
        	consulta.setCirHosp(rs.getString("CIRHOSP"));
            return consulta;
        }
    }


	@Override
	public void eliminacionRegistrosTabla(String nmautser)
			throws Exception {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pv_nmautser_i", nmautser);
			Map<String,Object> resultadoMap=this.ejecutaSP(new EliminacionRegistros(this.getDataSource()), params);
		}
		protected class EliminacionRegistros extends StoredProcedure
		{
			protected EliminacionRegistros(DataSource dataSource)
			{
				super(dataSource, "PKG_PRESINIESTRO.P_BORRA_TDETAUTS");
				declareParameter(new SqlParameter("pv_nmautser_i", OracleTypes.VARCHAR));
				declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
				declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
				compile();
			}
		}
		
	    @Override
		public List<HashMap<String, String>> loadListaDocumentos(
				Map<String, String> params) throws Exception {
			logger.debug(
					new StringBuilder()
					.append("\n****************************************************")
					.append("\n****** PKG_LISTAS.P_GET_DOCUMENTOS_SINIESTROS ******")
					.append("\n****** params=").append(params)
					.append("\n****************************************************")
					.toString()
					);
			Map<String, Object> mapResult = ejecutaSP(new LoadListaDocumentos(getDataSource()), params);
			
			return (List<HashMap<String, String>>) mapResult.get("pv_registro_o");
		}
	    
	    protected class LoadListaDocumentos extends StoredProcedure {

			protected LoadListaDocumentos(DataSource dataSource) {
				super(dataSource, "PKG_LISTAS.P_GET_DOCUMENTOS_SINIESTROS");
				
				declareParameter(new SqlParameter("pv_cdtippag_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_cdtipate_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_nmtramite_i", OracleTypes.VARCHAR));
				declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new LoadListaDocumentosMapper()));
				declareParameter(new SqlOutParameter("pv_messages_o", OracleTypes.VARCHAR));
		        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
				compile();
			}
		}
	    
	    protected class LoadListaDocumentosMapper  implements RowMapper {
	        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	        	HashMap<String, String> map =  new HashMap<String, String>();
	        	String obl = Constantes.SI.equalsIgnoreCase(rs.getString("SWOBLIGA"))? "Si" : "No";
	        	map.put("id", rs.getString("CODIGO"));
	        	map.put("listo", rs.getString("VALOR"));
	        	map.put("nombre", rs.getString("DESCRIP"));
	        	map.put("obligatorio", obl);
	            return map;
	        }
	    }


	    @Override
	    public List<Map<String, String>> loadListaIncisosRechazos(
	    		Map<String, String> params) throws Exception {
	    	
	    	Map<String, Object> mapResult = ejecutaSP(new LoadListaIncisosRechazos(getDataSource()), params);
	    	
	    	return (List<Map<String, String>>) mapResult.get("pv_registro_o");
	    }
	    
	    protected class LoadListaIncisosRechazos extends StoredProcedure {
	    	
	    	protected LoadListaIncisosRechazos(DataSource dataSource) {
	    		super(dataSource, "PKG_LISTAS.P_GET_CAUSAS_MOT_RECHAZO");
	    		
	    		declareParameter(new SqlParameter("pv_cdmotivo_i", OracleTypes.VARCHAR));
	    		declareParameter(new SqlParameter("pv_cdramo_i"  , OracleTypes.VARCHAR));
	    		declareParameter(new SqlParameter("pv_cdtipsit_i"  , OracleTypes.VARCHAR));
	    		String[] cols = new String[]{
	    				"CDCAUMOT","DSCAUMOT"
	    		};
	    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new GenericMapper(cols)));
	    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
	    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
	    		compile();
	    	}
	    }

	    @Override
	    public List<Map<String, String>> loadListaRechazos() throws Exception {
	    	Map<String, Object> params = new HashMap<String, Object>();
	    	Map<String, Object> mapResult = ejecutaSP(new LoadListaRechazos(getDataSource()), params);
	    	
	    	return (List<Map<String, String>>) mapResult.get("pv_registro_o");
	    }
	    
	    protected class LoadListaRechazos extends StoredProcedure {
	    	
	    	protected LoadListaRechazos(DataSource dataSource) {
	    		super(dataSource, "PKG_LISTAS.P_GET_MOTIVOS_RECHAZO");
	    		String[] cols = new String[]{
	    				"CDMOTIVO","DSMOTIVO"
	    		};
	    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new GenericMapper(cols)));
	    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
	    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
	    		compile();
	    	}
	    }

		@Override
		public String guardaEstatusDocumento(HashMap<String, String> params)
				throws Exception {
			
			logger.debug("parms: "+params);
			Map<String, Object> mapResult = ejecutaSP(new GuardaEstatusDocumento(getDataSource()), params);
			
			return (String) mapResult.get("pv_msg_id_o");
		}
		
		protected class GuardaEstatusDocumento extends StoredProcedure {

			protected GuardaEstatusDocumento(DataSource dataSource) {
				super(dataSource, "PKG_SATELITES.P_INSERTA_MDOCUTRA");
				
				declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_cdtippag_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_cdtipate_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_cddocume_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_accion_i", OracleTypes.VARCHAR));
		        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
		        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
				compile();
			}
		}

		@Override
		public List<GenericVO> obtieneListadoPlaza() throws Exception {
			Map<String,Object> resultadoMap=this.ejecutaSP(new ObtenerListaPlazas(this.getDataSource()), new HashMap<String,String>());
			return (List<GenericVO>) resultadoMap.get("pv_registro_o");
		}
		
		protected class ObtenerListaPlazas extends StoredProcedure
		{
			protected ObtenerListaPlazas(DataSource dataSource)
			{
				super(dataSource, "PKG_LISTAS.P_GET_PLAZAS");
				
				declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosListaPlazas()));
				declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
				declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
				compile();
			}
		}
		
		protected class DatosListaPlazas  implements RowMapper {
	        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	        	GenericVO consulta = new GenericVO();
	        	consulta.setKey(rs.getString("CDUNIECO"));
	        	consulta.setValue(rs.getString("DSUNIECO"));
	            return consulta;
	        }
	    }
		
		@Override
		public String rechazarTramite(HashMap<String, String> params)
				throws Exception {
			
			logger.debug("parms: "+params);
			Map<String, Object> mapResult = ejecutaSP(new RechazarTramite(getDataSource()), params);
			
			return (String) mapResult.get("pv_msg_id_o");
		}
		
		protected class RechazarTramite extends StoredProcedure {
			
			protected RechazarTramite(DataSource dataSource) {
				super(dataSource, "");
				
				declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_cdtippag_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_cdtipate_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_cddocume_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_accion_i", OracleTypes.VARCHAR));
				declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
				declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
				compile();
			}
		}
		
		@Override
		public String guardaListaFacturaSiniestro(HashMap<String, Object> paramsFacMesaCtrl) throws Exception {
				Map<String, Object> mapResult = ejecutaSP(new guardaListaFacturaSiniestro(getDataSource()), paramsFacMesaCtrl);
				return (String) mapResult.get("pv_msg_id_o");
		}
		
		protected class guardaListaFacturaSiniestro extends StoredProcedure {
			protected guardaListaFacturaSiniestro(DataSource dataSource) {
				super(dataSource, "PKG_SATELITES2.P_MOV_TFACMESCTRL");
				declareParameter(new SqlParameter("pv_accion_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_ffactura_i", OracleTypes.DATE));
				declareParameter(new SqlParameter("pv_cdtipser_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_cdpresta_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_ptimport_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_cdgarant_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_cdconval_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_descporc_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_descnume_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_cdmoneda_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_tasacamb_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_ptimporta_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_dctonuex_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_feegreso_i", OracleTypes.DATE));
				declareParameter(new SqlParameter("pv_diasdedu_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_nombprov_i", OracleTypes.VARCHAR));
				declareParameter(new SqlParameter("pv_factInicial_i", OracleTypes.VARCHAR));
				declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
				declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
				compile();
			}
		}


	@Override
	public String guardaListaTworkSin(HashMap<String, Object> paramsTworkSin) throws Exception {
		Map<String, Object> mapResult = ejecutaSP(new guardaListaTworkSinSP(getDataSource()), paramsTworkSin);
		return (String) mapResult.get("pv_msg_id_o");
	}
	
	protected class guardaListaTworkSinSP extends StoredProcedure {
		protected guardaListaTworkSinSP(DataSource dataSource) {
			super(dataSource, "PKG_PRESINIESTRO.P_GUARDA_TWORKSIN2");
			declareParameter(new SqlParameter("pv_nmtramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsolici_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_feocurre_i", OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_nmautser_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_reqautes_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmordina_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}


	@Override
	public String guardaAltaSiniestroAutServicio(String nmautser,String nfactura) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmautser_i", nmautser);
		params.put("pv_nfactura_i", nfactura);
		Map<String, Object> mapResult = ejecutaSP(new GuardAltaSiniestroAutServicio(this.getDataSource()), params);
		java.math.BigDecimal msgId = (java.math.BigDecimal)mapResult.get("pv_msg_id_o"); 
		return msgId.toPlainString();
	}
	
	protected class GuardAltaSiniestroAutServicio extends StoredProcedure
	{
		protected GuardAltaSiniestroAutServicio(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_GEN_SINIEST_AUT");
			declareParameter(new SqlParameter("pv_nmautser_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String guardaAltaSiniestroAltaTramite(String ntramite) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_ntramite_i", ntramite);
		Map<String, Object> mapResult = ejecutaSP(new GuardaSiniestroAltaTramite(this.getDataSource()), params);
		java.math.BigDecimal msgId = (java.math.BigDecimal)mapResult.get("pv_msg_id_o"); 
		return msgId.toPlainString();
	}
	
	protected class GuardaSiniestroAltaTramite extends StoredProcedure
	{
		protected GuardaSiniestroAltaTramite(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_GEN_SINIEST_TRA");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public List<ListaFacturasVO> obtieneListadoFacturas(HashMap<String, Object> paramFact) throws Exception {
		Map<String, Object> mapResult = ejecutaSP(new ObtieneListadoFacturasSP(getDataSource()), paramFact);
		List<ListaFacturasVO> listaDatosPoliza = (List<ListaFacturasVO>)mapResult.get("pv_registro_o");
		return listaDatosPoliza;
	}
	
	protected class ObtieneListadoFacturasSP extends StoredProcedure {

		protected ObtieneListadoFacturasSP(DataSource dataSource) {
			super(dataSource, "PKG_SINIESTRO.P_LISTA_FACTURA");
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_aaapertu_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsinies_i", OracleTypes.VARCHAR));			
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ListadoFacturasMapper()));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}

	protected class ListadoFacturasMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	ListaFacturasVO consulta = new ListaFacturasVO();
        	consulta.setCdfactur(rs.getString("CDFACTUR"));
        	consulta.setFefactur(rs.getString("FEFACTUR"));
        	consulta.setCdprovee(rs.getString("CDPROVEE"));
        	consulta.setCdrol(rs.getString("CDROL"));
        	consulta.setCdperson(rs.getString("CDPERSON"));
        	consulta.setMtolocal(rs.getString("MTOLOCAL"));
        	consulta.setDesProvee(rs.getString("PROVEEDOR"));
            return consulta;
        }
    }


	@Override
	public String bajaMsinival(HashMap<String, Object> paramBajasinival) throws Exception {
		Map<String, Object> mapResult = ejecutaSP(new BajaMsinivalSP(this.getDataSource()), paramBajasinival);
		return (String) mapResult.get("pv_msg_id_o");
	}
	
	protected class BajaMsinivalSP extends StoredProcedure
	{
		protected BajaMsinivalSP(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_BORRA_MSINIVAL");
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_aaapertu_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsinies_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public String generaContraRecibo(HashMap<String, Object> params) throws Exception {
		Map<String, Object> mapResult = ejecutaSP(new GeneraContraRecibo(this.getDataSource()), params);
		return (String) mapResult.get("pv_title_o");
	}
	
	protected class GeneraContraRecibo extends StoredProcedure
	{
		protected GeneraContraRecibo(DataSource dataSource)
		{
			super(dataSource, "Pkg_Presiniestro.p_update_contrarecibo");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO> obtieneListadoCobertura(String cdramo,String cdtipsit) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdramo_i", cdramo);
		params.put("pv_cdtipsit_i", cdtipsit);
		Map<String, Object> mapResult = ejecutaSP(new ObtenerListaCoberturas(this.getDataSource()), params);
		List<GenericVO> listaCoberturas = (List<GenericVO>)mapResult.get("pv_registro_o");
		return listaCoberturas;
	}
	
	protected class ObtenerListaCoberturas extends StoredProcedure
	{
		protected ObtenerListaCoberturas(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.P_GET_COBERTURASXRAMO");
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosListaCobertura()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public List<GenericVO> obtieneListadoCoberturaTotales() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> mapResult = ejecutaSP(new ObtenerListaCoberturasTotales(this.getDataSource()), params);
		List<GenericVO> listaCoberturas = (List<GenericVO>)mapResult.get("pv_registro_o");
		return listaCoberturas;
	}
	
	protected class ObtenerListaCoberturasTotales extends StoredProcedure
	{
		protected ObtenerListaCoberturasTotales(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.P_GET_COBERTURAS_TOTALES");
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosListaCobertura()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	protected class DatosListaCobertura  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	GenericVO consulta = new GenericVO();
        	consulta.setKey(rs.getString("CDGARANT"));
        	consulta.setValue(rs.getString("DSGARANT"));
            return consulta;
        }
    }
	
	@Override
	@Deprecated
	public String actualizaOTValorMesaControl(Map<String, Object> params) throws Exception {
		Map<String, Object> mapResult = ejecutaSP(new ActualizaOTValorMesaControl(this.getDataSource()), params);
		return (String) mapResult.get("pv_title_o");
	}
	
	protected class ActualizaOTValorMesaControl extends StoredProcedure
	{
		protected ActualizaOTValorMesaControl(DataSource dataSource)
		{
			super(dataSource, "PKG_SATELITES.p_upd_tmesacontrol");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdsucadm_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdsucdoc_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_comments_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor01_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor02_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor03_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor04_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor05_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor06_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor07_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor08_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor09_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor10_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor11_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor12_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor13_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor14_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor15_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor16_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor17_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor18_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor19_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor20_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor21_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor22_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor23_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor24_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor25_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor26_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor27_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor28_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor29_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor30_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor31_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor32_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor33_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor34_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor35_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor36_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor37_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor38_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor39_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor40_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor41_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor42_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor43_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor44_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor45_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor46_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor47_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor48_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor49_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor50_i", OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	/**
	 * PKG_SINIESTRO.P_LISTA_SINIESTROSXTRAMITE
	 * 6969 NMSINIES,
	 * 500 NMAUTSER,
	 * 510918 CDPERSON,
	 * 'JUAN PEREZ' NOMBRE,
	 * SYSDATE FEOCURRE,
	 * 1009 CDUNIECO,
	 * 'SALUD CAMPECHE' DSUNIECO,
	 * 2 CDRAMO,
	 * 'SALUD VITAL' DSRAMO,
	 * 'SL' CDTIPSIT,
	 * 'SALUD VITAL' DSTIPSIT,
	 * status,
	 * 'M' ESTADO,
	 * 500 NMPOLIZA,
	 * 'S' VOBOAUTO,
	 * '65' CDICD,
	 * 'GRIPE' DSICD,
	 * '66' ICD2,
	 * 'FIEBRE' DSICD2,
	 * 12.5 DESCPORC,
	 * 300 DESCNUME,
	 * 15 COPAGO,
	 * 3500 PTIMPORT,
	 * 'S' AUTRECLA,
	 * 54647 NMRECLAM,
	 * aaapertu
	 */
	public List<Map<String,String>> listaSiniestrosMsiniesTramite(Map<String, String> params) throws Exception
	{
		Map<String, Object> mapResult = ejecutaSP(new ListaSiniestrosMsiniesTramite(this.getDataSource()), params);
		return (List<Map<String,String>>) mapResult.get("pv_registro_o");
	}
	
	protected class ListaSiniestrosMsiniesTramite extends StoredProcedure
	{
		protected ListaSiniestrosMsiniesTramite(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_LISTA_MSINIESTTRAMITE");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_autoServ_i", OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"NMSINIES",				"NMAUTSER",				"CDPERSON"
					,"NOMBRE",				"FEOCURRE",				"CDUNIECO"
					,"DSUNIECO",			"AAAPERTU",				"ESTADO"
					,"NMSITUAC",			"NMSUPLEM",				"CDRAMO"
					,"DSRAMO",				"CDTIPSIT",				"DSTIPSIT"
					,"STATUS",				"ESTADO",				"NMPOLIZA"
					,"VOBOAUTO",			"CDICD",				"DSICD"
					,"CDICD2",				"DSICD2",				"DESCPORC"
					,"DESCNUME",			"PTIMPORT",				"AUTRECLA"
					,"NMRECLAMO",			"COMMENAR",				"COMMENME"
					,"AUTMEDIC",			"CDCAUSA",				"CDGARANT"
					,"CDCONVAL",			"NMSINREF",				"SECTWORKSIN"
					,"FEINGRESO",           "FEEGRESO",             "CDTIPEVE"
					,"CDTIPALT",            "SWFONSIN"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> listaSiniestrosTramite2(Map<String, String> params) throws Exception
	{
		Map<String, Object> mapResult = ejecutaSP(new ListaSiniestrosTramite2(this.getDataSource()), params);
		return (List<Map<String,String>>) mapResult.get("pv_registro_o");
	}
	
	protected class ListaSiniestrosTramite2 extends StoredProcedure
	{
		protected ListaSiniestrosTramite2(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_LISTA_SINIESTROSXTRAMITE2");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"NMSINIES",			"NMAUTSER",			"CDPERSON",			"NOMBRE"
					,"FEOCURRE",		"CDUNIECO",			"DSUNIECO",			"AAAPERTU"
					,"ESTADO",			"NMSITUAC",			"NMSUPLEM",			"CDRAMO"
					,"DSRAMO",			"CDTIPSIT",			"DSTIPSIT",			"STATUS"
					,"ESTADO",			"NMPOLIZA",			"VOBOAUTO",			"CDICD"
					,"DSICD",			"CDICD2",			"DSICD2",			"DESCPORC"
					,"DESCNUME",		"COPAGO",			"PTIMPORT",			"AUTRECLA"
					,"NMRECLAMO",		"COMMENAR",			"COMMENME",			"AUTMEDIC"
					,"CDCAUSA",			"CDGARANT",			"CDCONVAL",			"NMSINREF"
					,"IMPORTEASEG",		"PTIVAASEG",		"PTIVARETASEG",		"PTISRASEG"
					,"PTIMPCEDASEG",	"DEDUCIBLE",		"IMPORTETOTALPAGO",	"COMPLEMENTO"
					,"REQAUTES",		"NMAUTESP", 		"REQAUTESPECIAL",	"VALTOTALCOB"
					,"LIMITE",			"IMPPAGCOB",		"NMCALLCENTER",		"SECTWORKSIN"
					,"GENERO",			"FENACIMI"	// (EGS)
					,"FEINGRESO",       "FEEGRESO",         "CDTIPEVE",         "CDTIPALT"
					,"FLAGTIPEVE",      "FLAGTIPALT",       "SWFONSIN",			"NUMRECLA"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> listaAseguradosTramite(Map<String, String> params) throws Exception
	{
		Map<String, Object> mapResult = ejecutaSP(new ListaAseguradosTramite(this.getDataSource()), params);
		return (List<Map<String,String>>) mapResult.get("pv_registro_o");
	}
	
	protected class ListaAseguradosTramite extends StoredProcedure
	{
		protected ListaAseguradosTramite(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_LISTA_OBTIENE_ASEGURADOS");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_tipoProceso_i", OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"USUARIO",			"NFACTURA",			"NMSINIES",			"NMAUTSER"
					,"CDPERSON",		"NOMBRE",			"FEOCURRE",			"CDUNIECO"
					,"DSUNIECO",		"AAAPERTU",			"ESTADO",			"NMSITUAC"
					,"NMSUPLEM",		"CDRAMO",			"DSRAMO",			"CDTIPSIT"
					,"DSTIPSIT",		"STATUS",			"NMPOLIZA",			"VOBOAUTO"
					,"CDICD",			"DSICD",			"CDICD2",			"DSICD2"
					,"AUTRECLA",		"NMRECLAMO",		"COMMENAR",			"COMMENME"
					,"AUTMEDIC",		"CDCAUSA",			"CDGARANT",			"CDCONVAL"
					,"NMSINREF",		"IMPORTEASEG",		"PTIVAASEG",		"PTIVARETASEG"
					,"PTISRASEG",		"PTIMPCEDASEG",		"IMPORTETOTALPAGO",	"NTRAMITE"
					,"DESCPORC",		"DESCNUME"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String> obtenerTramiteCompleto(String ntramite) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("pv_ntramite_i", ntramite);
		return this.obtenerTramiteCompleto(params);
	}
	
	/**
	 * PKG_PRESINIESTRO.P_GET_TRAMITE_COMPLETO
	 */
	@Override
	@Deprecated
	public Map<String,String> obtenerTramiteCompleto(Map<String, String> params) throws Exception
	{
		Map<String, Object> mapResult = ejecutaSP(new ObtenerTramiteCompleto(this.getDataSource()), params);
		List<Map<String,String>> listaTramites = (List<Map<String,String>>) mapResult.get("pv_registro_o");
		if(listaTramites==null||listaTramites.size()==0)
		{
			throw new Exception("No se encuentra el tramite "+params.get("pv_ntramite_i"));
		}
		return listaTramites.get(0);
	}
	
	protected class ObtenerTramiteCompleto extends StoredProcedure
	{
		protected ObtenerTramiteCompleto(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.P_GET_TRAMITE_COMPLETO");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"NTRAMITE"
					,"CDUNIECO"
					,"CDRAMO"
					,"ESTADO"
					,"NMPOLIZA"
					,"NMSUPLEM"
					,"NMSOLICI"
					,"CDSUCADM"
					,"CDSUCDOC"
					,"CDSUBRAM"
					,"CDTIPTRA"
					,"FERECEPC"
					,"CDAGENTE"
					,"REFERENCIA"
					,"NOMBRE"
					,"FECSTATU"
					,"STATUS"
					,"COMMENTS"
					,"CDTIPSIT"
					,"OTVALOR01","OTVALOR02","OTVALOR03","OTVALOR04","OTVALOR05","OTVALOR06","OTVALOR07","OTVALOR08","OTVALOR09","OTVALOR10"
					,"OTVALOR11","OTVALOR12","OTVALOR13","OTVALOR14","OTVALOR15","OTVALOR16","OTVALOR17","OTVALOR18","OTVALOR19","OTVALOR20"
					,"OTVALOR21","OTVALOR22","OTVALOR23","OTVALOR24","OTVALOR25","OTVALOR26","OTVALOR27","OTVALOR28","OTVALOR29","OTVALOR30"
					,"OTVALOR31","OTVALOR32","OTVALOR33","OTVALOR34","OTVALOR35","OTVALOR36","OTVALOR37","OTVALOR38","OTVALOR39","OTVALOR40"
					,"OTVALOR41","OTVALOR42","OTVALOR43","OTVALOR44","OTVALOR45","OTVALOR46","OTVALOR47","OTVALOR48","OTVALOR49","OTVALOR50"
					,"RENUNIEXT", "RENRAMO" ,"RENPOLIEX","CDTIPFLU" ,"CDFLUJOMC","CDUNIDSPCH"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String> obtenerTramiteCompletoXNmsolici(Map<String, String> params) throws Exception
	{
		Map<String, Object> mapResult = ejecutaSP(new obtenerTramiteCompletoXNmsolici(this.getDataSource()), params);
		
		List<Map<String,String>> listaTramites = (List<Map<String,String>>) mapResult.get("pv_registro_o");
		if(listaTramites==null||listaTramites.size()==0)
		{
			params.put("Mensaje","La poliza no corresponde a un tramite de renovacion:  "+params.get("pv_nmpoliza_i"));
			return params;
		}
		else
		return listaTramites.get(0);
	}
	
	protected class obtenerTramiteCompletoXNmsolici extends StoredProcedure
	{
		protected obtenerTramiteCompletoXNmsolici(DataSource dataSource)
		{
			super(dataSource, "PKG_SATELITES2.P_GET_TRAMITE_X_NMPOLIZA");
			declareParameter(new SqlParameter("pv_nmsolici_i" , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtiptra_i" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"NTRAMITE"
					,"CDUNIECO"
					,"CDRAMO"
					,"ESTADO"
					,"NMPOLIZA"
					,"NMSUPLEM"
					,"NMSOLICI"
					,"CDSUCADM"
					,"CDSUCDOC"
					,"CDSUBRAM"
					,"CDTIPTRA"
					,"FERECEPC"
					,"CDAGENTE"
					,"REFERENCIA"
					,"NOMBRE"
					,"FECSTATU"
					,"STATUS"
					,"COMMENTS"
					,"CDTIPSIT"
					,"OTVALOR01","OTVALOR02","OTVALOR03","OTVALOR04","OTVALOR05","OTVALOR06","OTVALOR07","OTVALOR08","OTVALOR09","OTVALOR10"
					,"OTVALOR11","OTVALOR12","OTVALOR13","OTVALOR14","OTVALOR15","OTVALOR16","OTVALOR17","OTVALOR18","OTVALOR19","OTVALOR20"
					,"OTVALOR21","OTVALOR22","OTVALOR23","OTVALOR24","OTVALOR25","OTVALOR26","OTVALOR27","OTVALOR28","OTVALOR29","OTVALOR30"
					,"OTVALOR31","OTVALOR32","OTVALOR33","OTVALOR34","OTVALOR35","OTVALOR36","OTVALOR37","OTVALOR38","OTVALOR39","OTVALOR40"
					,"OTVALOR41","OTVALOR42","OTVALOR43","OTVALOR44","OTVALOR45","OTVALOR46","OTVALOR47","OTVALOR48","OTVALOR49","OTVALOR50"
					,"RENUNIEXT", "RENRAMO" ,"RENPOLIEX"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	/**
	 * PKG_SATELITES.P_OBT_TFACMESCTRL
	 * ntramite,
		nfactura,
		ffactura,
		cdtipser,
		DescServicio,
		cdpresta,
		NombreProveedor,
		ptimport,
		cdgarant,
		DSGARANT,
		DESCPORC,
		DESCNUME
	 */
	@Override
	public List<Map<String,String>> obtenerFacturasTramite(Map<String, String> params) throws Exception
	{
		Map<String, Object> mapResult = ejecutaSP(new ObtenerFacturasTramite(this.getDataSource()), params);
		return (List<Map<String,String>>) mapResult.get("pv_registro_o");
	}
	
	protected class ObtenerFacturasTramite extends StoredProcedure
	{
		protected ObtenerFacturasTramite(DataSource dataSource)
		{
			super(dataSource, "PKG_SATELITES.P_OBT_TFACMESCTRL");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"NTRAMITE"       , "NFACTURA"        , "FFACTURA"  , "CDTIPSER" , "DESCSERVICIO"
					,"CDPRESTA"      , "NOMBREPROVEEDOR" , "PTIMPORT"  , "CDGARANT" , "DSGARANT"
					,"DESCPORC"      , "DESCNUME"        , "CDCONVAL"  , "DSSUBGAR" , "CDMONEDA"
					,"DESTIPOMONEDA" , "TASACAMB"        , "PTIMPORTA" , "DCTONUEX" , "CODRECLAM"
					,"FEEGRESO" 	 , "DIASDEDU"        , "NOMBPROV"  , "CONTRARECIBO", "TOTALPAGAR"
					,"SWISR"		 , "SWICE"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	

	@Override
	public List<HashMap<String,String>> obtenerFacturasTramiteSiniestro(HashMap<String, String> params) throws Exception
	{
		Map<String, Object> mapResult = ejecutaSP(new ObtenerFacturasTramiteSiniestro(this.getDataSource()), params);
		return (List<HashMap<String,String>>) mapResult.get("pv_registro_o");
	}
	
	protected class ObtenerFacturasTramiteSiniestro extends StoredProcedure
	{
		protected ObtenerFacturasTramiteSiniestro(DataSource dataSource)
		{
			super(dataSource, "");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new FacturasTramiteMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	protected class FacturasTramiteMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	AutorizacionServicioVO consulta = new AutorizacionServicioVO();
        	consulta.setNmautser(rs.getString("NMAUTSER"));
        	consulta.setNmautant(rs.getString("NMAUTANT"));
            return consulta;
        }
    }
	
	@Override
	public void actualizarAutorizacionTworksin(Map<String, Object> params) throws Exception
	{
		ejecutaSP(new ActualizarAutorizacionTworksin(this.getDataSource()), params);
	}
	
	protected class ActualizarAutorizacionTworksin extends StoredProcedure
	{
		protected ActualizarAutorizacionTworksin(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.P_UPD_NMAUTSER_TWORKSIN");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmautser_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_feocurrencia_i", OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_secAsegurado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}


	@Override
	public List<PolizaVigenteVO> consultaPolizaUnica(HashMap<String, Object> paramPolUnica) throws Exception {
		logger.debug("ANTES DE CONSULTAR POLIZA UNICA" + paramPolUnica);
		Map<String, Object> mapResult = ejecutaSP(new ObtienePolizaUnicaSP(getDataSource()), paramPolUnica);
		logger.debug("LUEGO DE CONSULTAR POLIZA UNICA");
		List<PolizaVigenteVO> listaDatosPoliza = (List<PolizaVigenteVO>)mapResult.get("pv_registro_o");
		logger.debug("DATOS ASIGNADOS");
		return listaDatosPoliza;
	}
	
	protected class ObtienePolizaUnicaSP extends StoredProcedure {

		protected ObtienePolizaUnicaSP(DataSource dataSource) {
			super(dataSource, "PKG_PRESINIESTRO.P_OBTIENE_POLIZAUNICA");
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatosListaPoliza()));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}


	@Override
	public String validaExclusionPenalizacion(HashMap<String, Object> paramExclusion) throws Exception {
		Map<String, Object> resultado = ejecutaSP(new ExisteEsclusionPenalizacion(getDataSource()), paramExclusion);
		logger.debug( resultado.get("pv_existe_o"));
		return (String) resultado.get("pv_existe_o");
	}
	
    protected class ExisteEsclusionPenalizacion extends StoredProcedure {
    	
    	protected ExisteEsclusionPenalizacion(DataSource dataSource) {
    		
    		super(dataSource, "PKG_PRESINIESTRO.P_ELIMACION_PENALIZACION");
    		declareParameter(new SqlParameter("pv_cdunieco_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_existe_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
	
	@Override
	public void actualizaMsinies(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsituac,
			String nmsuplem,
			String status,
			String aaapertu,
			String nmsinies,
			Date feocurre,
			String cdicd,
			String cdicd2,
			String nreclamo) throws Exception
	{
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("pv_cdunieco_i" , cdunieco);
		params.put("pv_cdramo_i"   , cdramo);
		params.put("pv_estado_i"   , estado);
		params.put("pv_nmpoliza_i" , nmpoliza);
		params.put("pv_nmsituac_i" , nmsituac);
		params.put("pv_nmsuplem_i" , nmsuplem);
		params.put("pv_status_i"   , status);
		params.put("pv_aaapertu_i" , aaapertu);
		params.put("pv_nmsinies_i" , nmsinies);
		params.put("pv_feocurre_i" , feocurre);
		params.put("pv_cdicd_i"    , cdicd);
		params.put("pv_cdicd2_i"   , cdicd2);
		params.put("pv_nreclamo_i" , nreclamo);
		logger.debug("actualizaMsinies params: "+params);
		ejecutaSP(new ActualizaMultisiniestro(this.getDataSource()), params);
		logger.debug("actualizaMsinies end");
	}
	
	protected class ActualizaMultisiniestro extends StoredProcedure
	{
		protected ActualizaMultisiniestro(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.P_ACTUALIZA_MULTISINIESTRO");
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_aaapertu_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsinies_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_feocurre_i" , OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_cdicd_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdicd2_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nreclamo_i" , OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void P_MOV_MAUTSINI(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			String nmsituac,
			String aaapertu,
			String status,
			String nmsinies,
			String nfactura,
			String cdgarant,
			String cdconval,
			String cdconcep,
			String idconcep,
			String nmordina,
			String areaauto,
			String swautori,
			String tipautor,
			String comments,
			String accion) throws Exception
	{
		Map<String,String> p = new HashMap<String,String>();
		p.put("pv_cdunieco_i" , cdunieco);
		p.put("pv_cdramo_i"   , cdramo);
		p.put("pv_estado_i"   , estado);
		p.put("pv_nmpoliza_i" , nmpoliza);
		p.put("pv_nmsuplem_i" , nmsuplem);
		p.put("pv_nmsituac_i" , nmsituac);
		p.put("pv_aaapertu_i" , aaapertu);
		p.put("pv_status_i"   , status);
		p.put("pv_nmsinies_i" , nmsinies);
		p.put("pv_nfactura_i" , nfactura);
		p.put("pv_cdgarant_i" , cdgarant);
		p.put("pv_cdconval_i" , cdconval);
		p.put("pv_cdconcep_i" , cdconcep);
		p.put("pv_idconcep_i" , idconcep);
		p.put("pv_nmordina_i" , nmordina);
		p.put("pv_areaauto_i" , areaauto);
		p.put("pv_swautori_i" , swautori);
		p.put("pv_tipautor_i" , tipautor);
		p.put("pv_comments_i" , comments);
		p.put("pv_accion_i"   , accion);
		logger.debug("actualizaMsinies params: "+p);
		ejecutaSP(new PMOVMAUTSINI(this.getDataSource()), p);
		logger.debug("actualizaMsinies end");
	}
	
	protected class PMOVMAUTSINI extends StoredProcedure
	{
		protected PMOVMAUTSINI(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_MOV_MAUTSINI");
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_aaapertu_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsinies_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdgarant_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdconval_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdconcep_i" , OracleTypes.VARCHAR));
			//declareParameter(new SqlParameter("pv_idconcep_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmordina_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_areaauto_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swautori_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_tipautor_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_comments_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_accion_i"   , OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	public void P_MOV_MSINIVAL(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			String nmsituac,
			String aaapertu,
			String status,
			String nmsinies,
			String nfactura,
			String cdgarant,
			String cdconval,
			String cdconcep,
			String idconcep,
			String cdcapita,
			String nmordina,
			Date   femovimi,
			String cdmoneda,
			String ptprecio,
			String cantidad,
			String destopor,
			String destoimp,
			String ptimport,
			String ptrecobr,
			String nmanno,
			String nmapunte,
			String userregi,
			Date   feregist,
			String accion,
			String ptpcioex,
			String dctoimex,
			String ptimpoex,
			String mtoArancel,
			String aplicIVA) throws Exception
	{
		Map<String,Object>p=new HashMap<String,Object>();
		p.put("pv_cdunieco_i" , cdunieco);
		p.put("pv_cdramo_i"   , cdramo);
		p.put("pv_estado_i"   , estado);
		p.put("pv_nmpoliza_i" , nmpoliza);
		p.put("pv_nmsuplem_i" , nmsuplem);
		p.put("pv_nmsituac_i" , nmsituac);
		p.put("pv_aaapertu_i" , aaapertu);
		p.put("pv_status_i"   , status);
		p.put("pv_nmsinies_i" , nmsinies);
		p.put("pv_nfactura_i" , nfactura);
		p.put("pv_cdgarant_i" , cdgarant);
		p.put("pv_cdconval_i" , cdconval);
		p.put("pv_cdconcep_i" , cdconcep);
		p.put("pv_idconcep_i" , idconcep);
		p.put("pv_cdcapita_i" , cdcapita);
		p.put("pv_nmordina_i" , nmordina);
		p.put("pv_femovimi_i" , femovimi);
		p.put("pv_cdmoneda_i" , cdmoneda);
		p.put("pv_ptprecio_i" , ptprecio);
		p.put("pv_cantidad_i" , cantidad);
		p.put("pv_destopor_i" , destopor);
		p.put("pv_destoimp_i" , destoimp);
		p.put("pv_ptimport_i" , ptimport);
		p.put("pv_ptrecobr_i" , ptrecobr);
		p.put("pv_nmanno_i"   , nmanno);
		p.put("pv_nmapunte_i" , nmapunte);
		p.put("pv_userregi_i" , userregi);
		p.put("pv_feregist_i" , feregist);
		p.put("pv_accion_i"   , accion);
		p.put("pv_ptpcioex_i"   , ptpcioex);
		p.put("pv_dctoimex_i"   , dctoimex);
		p.put("pv_ptimpoex_i"   , ptimpoex);
		p.put("pv_ptmtoara_i"   , mtoArancel);
		p.put("pv_aplicIVA_i"   , aplicIVA);
		logger.debug("P_MOV_MSINIVAL params: "+p);
		ejecutaSP(new PMOVMSINIVAL(this.getDataSource()), p);
		logger.debug("P_MOV_MSINIVAL end");
	}
	
	protected class PMOVMSINIVAL extends StoredProcedure
	{
		protected PMOVMSINIVAL(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_MOV_MSINIVAL1");
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_aaapertu_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsinies_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdgarant_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdconval_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdconcep_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_idconcep_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdcapita_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmordina_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_femovimi_i" , OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_cdmoneda_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ptprecio_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cantidad_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_destopor_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_destoimp_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ptimport_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ptrecobr_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmanno_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmapunte_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_userregi_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_feregist_i" , OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_accion_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ptpcioex_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dctoimex_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ptimpoex_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ptmtoara_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_aplicIVA_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>>P_GET_MSINIVAL(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			String nmsituac,
			String aaapertu,
			String status,
			String nmsinies,
			String nfactura) throws Exception
	{
		Map<String,String>p=new HashMap<String,String>();
		p.put("pv_cdunieco_i" , cdunieco);
		p.put("pv_cdramo_i"   , cdramo);
		p.put("pv_estado_i"   , estado);
		p.put("pv_nmpoliza_i" , nmpoliza);
		p.put("pv_nmsuplem_i" , nmsuplem);
		p.put("pv_nmsituac_i" , nmsituac);
		p.put("pv_aaapertu_i" , aaapertu);
		p.put("pv_status_i"   , status);
		p.put("pv_nmsinies_i" , nmsinies);
		p.put("pv_nfactura_i" , nfactura);
		logger.debug("P_GET_MSINIVAL params: "+p);
		Map<String, Object> mapResult = ejecutaSP(new PGETMSINIVAL(this.getDataSource()), p);
		return (List<Map<String,String>>) mapResult.get("pv_registro_o");
	}
	
	protected class PGETMSINIVAL extends StoredProcedure
	{
		protected PGETMSINIVAL(DataSource dataSource)
		{
			super(dataSource, "PKG_CONSULTA.P_GET_MSINIVAL");
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_aaapertu_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsinies_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i" , OracleTypes.VARCHAR));
			String cols[] = new String[]{
					"CDUNIECO"    , "CDRAMO"       , "ESTADO"   , "NMPOLIZA"
					,"NMSUPLEM"   , "NMSITUAC"     , "AAAPERTU" , "STATUS"
					,"NMSINIES"   , "NFACTURA"     , "CDGARANT" , "DSGARANT"
					,"CDCONVAL"   , "DSSUBGAR"     , "CDCONCEP" , "DESCONCEP"
					,"IDCONCEP"   , "DESIDCONCEP"  , "CDCAPITA" , "NMORDINA"
					,"FEMOVIMI"   , "CDMONEDA"     , "PTPRECIO" , "CANTIDAD"
					,"DESTOPOR"   , "DESTOIMP"     , "PTIMPORT" , "PTRECOBR"
					,"NMANNO"     , "NMAPUNTE"     , "USERREGI" , "FEREGIST"
					,"PTPCIOEX"   , "DCTOIMEX"     , "PTIMPOEX" , "PTMTOARA"
					,"TOTAJUSMED" , "SUBTAJUSTADO" , "APLICIVA"	, "PTIVA"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void P_MOV_TDSINIVAL(
			String cdunieco,String cdramo,String estado,String nmpoliza,String nmsuplem,
			String nmsituac,String aaapertu,String status,String nmsinies,String nfactura,
			String cdgarant,String cdconval,String cdconcep,String idconcep,String nmordina,
			String nmordmov,String ptimport,String comments,String userregi,Date feregist,String accion) throws Exception
	{
		Map<String,Object>p=new HashMap<String,Object>();
		p.put("pv_cdunieco_i" , cdunieco);
		p.put("pv_cdramo_i"   , cdramo);
		p.put("pv_estado_i"   , estado);
		p.put("pv_nmpoliza_i" , nmpoliza);
		p.put("pv_nmsuplem_i" , nmsuplem);
		p.put("pv_nmsituac_i" , nmsituac);
		p.put("pv_aaapertu_i" , aaapertu);
		p.put("pv_status_i"   , status);
		p.put("pv_nmsinies_i" , nmsinies);
		p.put("pv_nfactura_i" , nfactura);
		p.put("pv_cdgarant_i" , cdgarant);
		p.put("pv_cdconval_i" , cdconval);
		p.put("pv_cdconcep_i" , cdconcep);
		p.put("pv_idconcep_i" , idconcep);
		p.put("pv_nmordina_i" , nmordina);
		p.put("pv_nmordmov_i" , nmordmov);
		p.put("pv_ptimport_i" , ptimport);
		p.put("pv_comments_i" , comments);
		p.put("pv_userregi_i" , userregi);
		p.put("pv_feregist_i" , feregist);
		p.put("pv_accion_i"   , accion);
		logger.debug("P_MOV_TDSINIVAL paras: "+p);
		ejecutaSP(new PMOVTDSINIVAL(this.getDataSource()), p);
		logger.debug("P_MOV_TDSINIVAL end");
	}
	
	protected class PMOVTDSINIVAL extends StoredProcedure
	{
		protected PMOVTDSINIVAL(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_MOV_TDSINIVAL");
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_aaapertu_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsinies_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdgarant_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdconval_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdconcep_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_idconcep_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmordina_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmordmov_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ptimport_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_comments_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_userregi_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_feregist_i" , OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_accion_i"   , OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>>P_GET_TDSINIVAL(
			String cdunieco,String cdramo,String estado,String nmpoliza,String nmsuplem,
			String nmsituac,String aaapertu,String status,String nmsinies,String nfactura,
			String cdgarant,String cdconval,String cdconcep,String idconcep,String nmordina) throws Exception
	{
		Map<String,Object>p=new HashMap<String,Object>();
		p.put("pv_cdunieco_i" , cdunieco);
		p.put("pv_cdramo_i"   , cdramo);
		p.put("pv_estado_i"   , estado);
		p.put("pv_nmpoliza_i" , nmpoliza);
		p.put("pv_nmsuplem_i" , nmsuplem);
		p.put("pv_nmsituac_i" , nmsituac);
		p.put("pv_aaapertu_i" , aaapertu);
		p.put("pv_status_i"   , status);
		p.put("pv_nmsinies_i" , nmsinies);
		p.put("pv_nfactura_i" , nfactura);
		p.put("pv_cdgarant_i" , cdgarant);
		p.put("pv_cdconval_i" , cdconval);
		p.put("pv_cdconcep_i" , cdconcep);
		p.put("pv_idconcep_i" , idconcep);
		p.put("pv_nmordina_i" , nmordina);
		logger.debug("P_GET_TDSINIVAL params: "+p);
		Map<String, Object> mapResult = ejecutaSP(new PGETTDSINIVAL(this.getDataSource()), p);
		return (List<Map<String,String>>) mapResult.get("pv_registro_o");
	}
	
	protected class PGETTDSINIVAL extends StoredProcedure
	{
		protected PGETTDSINIVAL(DataSource dataSource)
		{
			super(dataSource, "PKG_CONSULTA.P_GET_TDSINIVAL");
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_aaapertu_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsinies_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdgarant_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdconval_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdconcep_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_idconcep_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmordina_i" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CDUNIECO"  , "CDRAMO"   , "ESTADO"   , "NMPOLIZA" , "NMSUPLEM"
					,"NMSITUAC" , "AAAPERTU" , "STATUS"   , "NMSINIES" , "NFACTURA"
					,"CDGARANT" , "CDCONVAL" , "CDCONCEP" , "IDCONCEP" , "NMORDINA"
					,"NMORDMOV" , "PTIMPORT" , "COMMENTS" , "USERREGI" , "FEREGIST"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	public List<Map<String,String>>P_GET_FACTURAS_SINIESTRO(
			String cdunieco,String cdramo,String estado,String nmpoliza,String nmsuplem,
			String nmsituac,String aaapertu,String status,String nmsinies,String cdtipsit) throws Exception
	{
		Map<String,Object>p=new HashMap<String,Object>();
		p.put("pv_cdunieco_i" , cdunieco);
		p.put("pv_cdramo_i"   , cdramo);
		p.put("pv_estado_i"   , estado);
		p.put("pv_nmpoliza_i" , nmpoliza);
		p.put("pv_nmsuplem_i" , nmsuplem);
		p.put("pv_nmsituac_i" , nmsituac);
		p.put("pv_aaapertu_i" , aaapertu);
		p.put("pv_status_i"   , status);
		p.put("pv_nmsinies_i" , nmsinies);
		p.put("pv_cdtipsit_i" , cdtipsit);
		logger.debug("P_GET_FACTURAS_SINIESTRO params: "+p);
		Map<String, Object> mapResult = ejecutaSP(new PGETFACTURASSINIESTRO(this.getDataSource()), p);
		return (List<Map<String,String>>) mapResult.get("pv_registro_o");
	}
	
	protected class PGETFACTURASSINIESTRO extends StoredProcedure
	{
		protected PGETFACTURASSINIESTRO(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_GET_FACTURAS_SINIESTRO");
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_aaapertu_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsinies_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"NFACTURA"
					,"FFACTURA"
					,"CDGARANT"
					,"DSGARANT"
					,"CDCONVAL"
					,"DSSUBGAR"
					,"PTIMPORT"
					,"CDMONEDA"
					,"DESTIPOMONEDA"
					,"TASACAMB"
					,"PTIMPORTA"
					,"DCTONUEX"
					,"CODRECLAM"
					,"DEDUCIBLE"
					,"COPAGO"
					,"AUTRECLA"
					,"COMMENAR"
					,"AUTMEDIC"
					,"COMMENME"
					,"CDTIPSER"
					//,"DESCRIPC"
					,"CDPRESTA"
					,"NOM_PRESTA"
					,"DESCPORC"
					,"DESCNUME"
					,"APLICA_IVA"
					,"ANTES_DESPUES"
					,"IVARETENIDO"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	@Override
	public List<Map<String,String>>cargaHistorialSiniestros(Map<String,String> params) throws Exception
	{
		Map<String, Object> mapResult = ejecutaSP(new CargaHistorialHiniestros(this.getDataSource()), params);
		return (List<Map<String,String>>) mapResult.get("pv_registro_o");
	}
	
	protected class CargaHistorialHiniestros extends StoredProcedure
	{
		protected CargaHistorialHiniestros(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_GET_HIST_STROS");
			declareParameter(new SqlParameter("pv_cdperson_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"NTRAMITE",			"CONTRARECIBO",		"ASEGSINIESTRO",	"ASEGURADO",	"EDADASEG",
					"ANTIGUEDAD",		"CONTRATANTE",		"TIPOPAGO",			"ESTATUS",		"SUCURSAL",
					"POLIZA",			"FACTURA",			"FECHAFACT",		"PROVEEDOR",	"SINIESTRO",
					"DIAGNOSTICO",		"CAUSASIN",			"FECHAOCURRE",		"SUBTOTAL",		"IVA",
					"IVARETENIDO",		"ISR",				"IMPCEDULAR",		"PAGADO",		"NMPOLIEX"};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO>obtenerCodigosMedicos(String idconcep, String subcaden) throws Exception
	{
		Map<String,String>p=new HashMap<String,String>();
		p.put("pv_idconcep_i" , idconcep);
		p.put("pv_descripc_i" , subcaden);
		logger.debug("obtenerCodigosMedicos params: "+p);
		Map<String, Object> mapResult = ejecutaSP(new ObtenerCodigosMedicos(this.getDataSource()), p);
		List<Map<String,String>>lista=(List<Map<String,String>>) mapResult.get("pv_registro_o");
		List<GenericVO>listaG=new ArrayList<GenericVO>();
		if(lista!=null)
		{
			for(Map<String,String>cpt:lista)
			{
				listaG.add(new GenericVO(cpt.get("CLAVE"),cpt.get("VALOR")));
			}
		}
		return listaG;
	}
	
	protected class ObtenerCodigosMedicos extends StoredProcedure
	{
		protected ObtenerCodigosMedicos(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_LISTA_CODIGOS_MEDICOS");
			declareParameter(new SqlParameter("pv_idconcep_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_descripc_i" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CLAVE" , "VALOR"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<GenericVO>obtenerCodigosMedicosTotales() throws Exception
	{
		Map<String,String>p=new HashMap<String,String>();
		Map<String, Object> mapResult = ejecutaSP(new ObtenerCodigosMedicosTotales(this.getDataSource()), p);
		List<Map<String,String>>lista=(List<Map<String,String>>) mapResult.get("pv_registro_o");
		List<GenericVO>listaG=new ArrayList<GenericVO>();
		if(lista!=null)
		{
			for(Map<String,String>cpt:lista)
			{
				listaG.add(new GenericVO(cpt.get("CLAVE"),cpt.get("VALOR")));
			}
		}
		return listaG;
	}
	
	protected class ObtenerCodigosMedicosTotales extends StoredProcedure
	{
		protected ObtenerCodigosMedicosTotales(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_LISTA_CODIGOS_MED_TOTALES");
			String[] cols = new String[]{
					"CLAVE" , "VALOR"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String>obtenerLlaveSiniestroReembolso(String ntramite) throws Exception
	{
		Map<String,String>p=new HashMap<String,String>();
		p.put("pv_ntramite_i" , ntramite);
		logger.debug("obtenerLlaveSiniestroReembolso params: "+p);
		Map<String, Object> mapResult = ejecutaSP(new ObtenerLlaveSiniestroReembolso(this.getDataSource()), p);
		List<Map<String,String>>lista=(List<Map<String,String>>) mapResult.get("pv_registro_o");
		Map<String,String>llave=null;
		if(lista!=null)
		{
			llave=lista.get(0);
		}
		logger.debug("obtenerLlaveSiniestroReembolso llave: "+llave);
		return llave;
	}
	
	protected class ObtenerLlaveSiniestroReembolso extends StoredProcedure
	{
		protected ObtenerLlaveSiniestroReembolso(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_GET_LLAVE_SINIES_REEMBOLSO");
			declareParameter(new SqlParameter("pv_ntramite_i" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CDUNIECO","CDRAMO","ESTADO","NMPOLIZA","NMSUPLEM","NMSITUAC","AAAPERTU","STATUS","NMSINIES","CDTIPSIT"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	@Override
	public List<Map<String,String>> obtieneDatosGeneralesSiniestro(Map<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ObtieneDatosGeneralesSiniestro(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneDatosGeneralesSiniestro extends StoredProcedure {
		protected ObtieneDatosGeneralesSiniestro(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_SINIESTRO.P_LISTA_DG_STROS");
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i" , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_estado_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_aaapertu_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsinies_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ntramite_i" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CONTREC"
					,"STATUS"
					,"DESSTATUS"
					,"CDSUCADM"
					,"DSSUCADM"
					,"CDSUCDOC"
					,"DSSUCDOC"
					,"FEOCURRE"
					,"FERECEP"
					,"FEOCURR"
					,"CDCAUSA"
					,"DSCAUSA"
					,"PLANTARI"
					,"CIRHOSPI"
					,"ZONTARIF"
					,"DSZONAT"
					,"SUMAASEG"
					,"TIPPAG"
					,"DSTIPPAG"
					,"NMPOLIEX"
					,"IMPORTE"
					,"FEINIVAL"
					,"FEFINVAL"
					,"STAPOLIZA"
					,"FEANTIG"
					,"FEALTA"
					,"CONTEO"
					,"CDPERPAG"
					,"DSPERPAG"
					,"CDPERSON"
					,"ASEGURADO"
					,"CDPROVEE"
					,"DSPROVEED"
					,"CIRHOPROV"
					,"CDICD"
					,"DSICD"
					,"CDICD2"
					,"DSICD2"
					,"NMRECLAMO"
					,"CDBENEFICIARIO"
					,"BENEFICIARIO"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String, Object> actualizaDatosGeneralesSiniestro(Map<String, Object> params) throws Exception {
		return ejecutaSP(new ActualizaDatosGeneralesSiniestro(this.getDataSource()), params);
	}
	
	protected class ActualizaDatosGeneralesSiniestro extends StoredProcedure {
		protected ActualizaDatosGeneralesSiniestro(DataSource dataSource) {
			super(dataSource, "PKG_SINIESTRO.P_MOD_MSINIEST");
            declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_aaapertu_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsinies_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsuplem_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_feocurre_i" , OracleTypes.DATE));
            declareParameter(new SqlParameter("pv_nmreclamo_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdicd_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdicd2_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdcausa_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdgarant_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdconval_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmautser_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdperson_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_tipoProceso_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_complemento_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_aplicFondo_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String, Object> actualizaMsiniestroReferenciado(Map<String, Object> params) throws Exception {
		return ejecutaSP(new ActualizaMsiniestroReferenciado(this.getDataSource()), params);
	}
	
	protected class ActualizaMsiniestroReferenciado extends StoredProcedure {
		protected ActualizaMsiniestroReferenciado(DataSource dataSource) {
			super(dataSource, "PKG_SINIESTRO.P_ACTUALIZA_SINI_REFERENCIADO");
            declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsuplem_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsituac_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_aaapertu_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_status_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsinies_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsinref_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	
	@Override
	public List<Map<String,String>>P_GET_CONCEPTOS_FACTURA(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			String nmsituac,
			String aaapertu,
			String status,
			String nmsinies,
			String nfactura,
			String cdtipsit,
			String ntramite) throws Exception
	{
		Map<String,Object>p=new HashMap<String,Object>();
		p.put("pv_cdunieco_i" , cdunieco);
		p.put("pv_cdramo_i"   , cdramo);
		p.put("pv_estado_i"   , estado);
		p.put("pv_nmpoliza_i" , nmpoliza);
		p.put("pv_nmsuplem_i" , nmsuplem);
		p.put("pv_nmsituac_i" , nmsituac);
		p.put("pv_aaapertu_i" , aaapertu);
		p.put("pv_status_i"   , status);
		p.put("pv_nmsinies_i" , nmsinies);
		p.put("pv_nfactura_i" , nfactura);
		p.put("pv_cdtipsit_i" , cdtipsit);
		p.put("pv_ntramite_i" , ntramite);
		logger.debug("P_GET_CONCEPTOS_FACTURA params: "+p);
		Map<String, Object> mapResult = ejecutaSP(new PGETCONCEPTOSFACTURA(this.getDataSource()), p);
		return (List<Map<String,String>>) mapResult.get("pv_registro_o");
	}
	
	protected class PGETCONCEPTOSFACTURA extends StoredProcedure
	{
		protected PGETCONCEPTOSFACTURA(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_GET_CONCEPTOS_FACTURA");
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_aaapertu_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsinies_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ntramite_i" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CDUNIECO"
					,"CDRAMO"
					,"ESTADO"
					,"NMPOLIZA"
					,"NMSUPLEM"
					,"NMSITUAC"
					,"AAAPERTU"
					,"STATUS"
					,"NMSINIES"
					,"CDCONCEP"
					,"OTVALOR"
					,"IDCONCEP"
					,"DESCRIPC"
					,"PTPRECIO"
					,"CANTIDAD"
					,"PTIMPORT"
					,"DESTOPOR"
					,"DESTOIMP"
					,"PTPCIOEX"
					,"DCTOIMEX"
					,"PTIMPOEX"
					,"DEDUCIBLE"
					,"COPAGO"
					/*,"AUTMEDIC"
					,"COMMENME"
					,"AUTRECLA"
					,"COMMENRE"*/
					,"PTIMPORT_AJUSTADO"
					,"IMP_ARANCEL"
					,"NFACTURA"
					,"CDGARANT"
					,"CDCONVAL"
					,"NMORDINA"
					,"APLICIVA"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String>obtenerCopagoDeducible(
			String ntramite,
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			String nmsituac,
			String aaapertu,
			String status,
			String nmsinies,
			String nfactura,
			String tipopago,
			String cdtipsit) throws Exception
	{
		Map<String,Object>p=new HashMap<String,Object>();
		p.put("ntramite" , ntramite);
		p.put("cdunieco" , cdunieco);
		p.put("cdramo"   , cdramo);
		p.put("estado"   , estado);
		p.put("nmpoliza" , nmpoliza);
		p.put("nmsuplem" , nmsuplem);
		p.put("nmsituac" , nmsituac);
		p.put("aaapertu" , aaapertu);
		p.put("status"   , status);
		p.put("nmsinies" , nmsinies);
		p.put("nfactura" , nfactura);
		p.put("tipopago" , tipopago);
		p.put("cdtipsit" , cdtipsit);
		logger.debug("obtenerCopagoDeducible params: "+p);
		Map<String, Object> mapResult = ejecutaSP(new ObtenerCopagoDeducible(this.getDataSource()), p);
		List<Map<String,String>> lista = (List<Map<String,String>>) mapResult.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new Exception("No se encuentra el copago deducible");
		}
		if(lista.size()>1)
		{
			throw new Exception("Copago y deducible duplicado");
		}
		Map<String,String>copagoDeducible = lista.get(0);
		logger.debug("Copago deducible: "+copagoDeducible);
		return copagoDeducible;
	}
	
	protected class ObtenerCopagoDeducible extends StoredProcedure
	{
		protected ObtenerCopagoDeducible(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.P_GET_DATOS_SUBG");
			declareParameter(new SqlParameter("ntramite", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("aaapertu" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("status"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsinies" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nfactura" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipopago" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CDGARANT"
					,"OTCLAVE2"
					,"CDCAPITA"
					,"LUC"
						,"DEDUCIBLE"
						,"COPAGO"
					,"BENEFMAX"
					,"ICD"
					,"CPT"
					,"LIMITES"
					,"TIPOCOPAGO"
					,"UNIDAD"
					//,"LV_PENALIZA"
					,"FORMATOCALCULO"
					,"PENALIZACIONES"
					,"VALSESIONES"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String>obtenerRentaDiariaxHospitalizacion(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsituac,
			String nmsuplem
			) throws Exception
	{
		Map<String,Object>p=new HashMap<String,Object>();
		p.put("cdunieco" , cdunieco);
		p.put("cdramo"   , cdramo);
		p.put("estado"   , estado);
		p.put("nmpoliza" , nmpoliza);
		p.put("nmsituac" , nmsituac);
		p.put("nmsuplem" , nmsuplem);
		logger.debug("obtenerRentaDiariaxHospitalizacion params: "+p);
		Map<String, Object> mapResult = ejecutaSP(new ObtenerRentaDiariaxHospitalizacion(this.getDataSource()), p);
		List<Map<String,String>> lista = (List<Map<String,String>>) mapResult.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new Exception("No se encuentra la Renta Diaria x hospitalizaci�n ");
		}
		if(lista.size()>1)
		{
			throw new Exception("Renta Diaria x hospitalizaci�n duplicado");
		}
		Map<String,String>rentaDiaria = lista.get(0);
		logger.debug("Renta Diaria x hospitalizacion: "+rentaDiaria);
		return rentaDiaria;
	}
	
	protected class ObtenerRentaDiariaxHospitalizacion extends StoredProcedure
	{
		protected ObtenerRentaDiariaxHospitalizacion(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_OBTINE_RENTA_DIARIA_X_HOSP");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"OTVALOR"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String validaPorcentajePenalizacion(String zonaContratada,String zonaAtencion,String cdRamo) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_zcontratada_i", zonaContratada);
		params.put("pv_zatencion_i", zonaAtencion);
		params.put("pv_cdRamo_i", cdRamo);
		Map<String, Object> mapResult = ejecutaSP(new ValidaPorcentajePenalizacionSP(getDataSource()), params);
		return (String) mapResult.get("pv_penalizacion_o");
	}
	
	protected class ValidaPorcentajePenalizacionSP extends StoredProcedure {

		protected ValidaPorcentajePenalizacionSP(DataSource dataSource) {
			super(dataSource, "PKG_PRESINIESTRO.P_GET_PENALIZACION");
			declareParameter(new SqlParameter("pv_zcontratada_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_zatencion_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdRamo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_penalizacion_o", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}


	@Override
	public String obtieneAutorizacionProceso(String nmAutSer) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("PV_NMAUTSER_I", nmAutSer);
		
		Map<String, Object> resultado = ejecutaSP(new ExisteAutorizacionProceso(getDataSource()), params);
		return (String) resultado.get("PV_VALOR_I");
	}
	
    protected class ExisteAutorizacionProceso extends StoredProcedure {
    	protected ExisteAutorizacionProceso(DataSource dataSource) {
    		super(dataSource, "PKG_PRESINIESTRO.P_VALIDA_TDETAUTS");
    		declareParameter(new SqlParameter("PV_NMAUTSER_I",   OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("PV_VALOR_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
	
	@Override
	public String validaDocumentosCargados(HashMap<String, String> params) throws Exception {
		Map<String, Object> mapResult = ejecutaSP(new ValidaDocumentosCargados(getDataSource()), params);
		return (String) mapResult.get("PV_VALOR_O");
	}
	
	protected class ValidaDocumentosCargados extends StoredProcedure {

		protected ValidaDocumentosCargados(DataSource dataSource) {
			super(dataSource, "PKG_PRESINIESTRO.P_VALIDA_DOCUMENTOS");
			declareParameter(new SqlParameter("PV_NTRAMITE_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_cdtippag_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_CDTIPATE_I", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("PV_VALOR_O", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Reclamo> obtieneDatosReclamoWS(Map<String, Object> params) throws Exception{
		Map<String, Object> result = ejecutaSP(new ObtieneDatosReclamoWS(this.getDataSource()), params);
		return (List<Reclamo>)result.get("pv_registro_o");
	}
	
	protected class ObtieneDatosReclamoWS extends StoredProcedure {
		protected ObtieneDatosReclamoWS(DataSource dataSource) {
			super(dataSource, "PKG_CONSULTA.P_WS_RECLAMACIONES");
			declareParameter(new SqlParameter("pv_ntramite_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new ReclamoWSMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	protected class ReclamoWSMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	Reclamo reclamo =  new Reclamo();
        	Calendar cal;
        	
        	reclamo.setCanIce(rs.getDouble("CAN_ICE"));
        	reclamo.setConPag(rs.getInt("CON_PAG"));
        	reclamo.setConReg(0);
        	reclamo.setCveAfe(rs.getInt("CVE_AFE"));
        	reclamo.setCveAge(rs.getInt("CVE_AGE"));
        	reclamo.setCveCap(rs.getString("CVE_CAP"));
        	reclamo.setCveCob(rs.getInt("CVE_COB"));
        	reclamo.setCveDes(rs.getInt("CVE_DES"));
        	reclamo.setCveEdo(rs.getInt("CVE_EDO"));
        	reclamo.setCveMun(rs.getInt("CVE_MUN"));
        	reclamo.setEdoImp(rs.getInt("EDO_IMP"));
        	reclamo.setEdoOcu(rs.getInt("EDO_OCU"));
        	reclamo.setEstReg(rs.getString("EST_REG"));
        	
        	cal = Utils.getCalendarTimeZone0(rs.getString("FEC_FAC"), Constantes.FORMATO_FECHA);
        	if(cal != null){
        		reclamo.setFecFac(cal);
        	}else{
        		logger.error("NO SE PUDO PARSEAR LA FECHA FEC_FAC !!! " + rs.getString("FEC_FAC"));
        	}
        	
        	cal = Utils.getCalendarTimeZone0(rs.getString("FEC_OCU"), Constantes.FORMATO_FECHA);
        	if(cal != null){
        		reclamo.setFecOcu(cal);
        	}else{
        		logger.error("NO SE PUDO PARSEAR LA FECHA FEC_OCU !!! " + rs.getString("FEC_OCU"));
        	}

        	cal = Utils.getCalendarTimeZone0(rs.getString("FEC_PRO"), Constantes.FORMATO_FECHA);
        	if(cal != null){
        		reclamo.setFecPro(cal);
        	}else{
        		logger.error("NO SE PUDO PARSEAR LA FECHA FEC_PRO !!! " + rs.getString("FEC_PRO"));
        	}

        	cal = Utils.getCalendarTimeZone0(rs.getString("FEC_REG"), Constantes.FORMATO_FECHA);
        	if(cal != null){
        		reclamo.setFecReg(cal);
        	}else{
        		logger.error("NO SE PUDO PARSEAR LA FECHA FEC_REG !!! " + rs.getString("FEC_REG"));
        	}

        	cal = Utils.getCalendarTimeZone0(rs.getString("FIN_VIG"), Constantes.FORMATO_FECHA);
        	if(cal != null){
        		reclamo.setFinVig(cal);
        	}else{
        		logger.error("NO SE PUDO PARSEAR LA FECHA FIN_VIG !!! " + rs.getString("FIN_VIG"));
        	}


        	try {
        		logger.debug("--> Parseando hora rs.getString(HOR_OCU) -->> "+ rs.getString("HOR_OCU"));
        		cal = Calendar.getInstance();
        		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), rs.getInt("HOR_OCU"), 0);
        		logger.debug("--> Calendario obtenido -->> "+ cal);
        		reclamo.setHorOcu(cal);
        	} catch (Exception e) {
        		logger.error("NO SE PUDO PARSEAR LA hora HOR_OCU !!! ");
        		reclamo.setHorOcu(null);
        	}
        	try {
        		logger.debug("--> Parseando hora rs.getString(HOR_PRO) -->> "+ rs.getString("HOR_PRO"));
        		cal = Calendar.getInstance();
        		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), rs.getInt("HOR_PRO"), 0);
        		logger.debug("--> Calendario obtenido -->> "+ cal);
        		reclamo.setHorPro(cal);
        	} catch (Exception e) {
        		logger.error("NO SE PUDO PARSEAR LA hora HOR_PRO !!! ");
        		reclamo.setHorPro(null);
        	}
        	
        	reclamo.setIcodreclamo(rs.getInt("ICODRECLAMO"));
        	reclamo.setIdBen(rs.getInt("ID_BEN"));
        	reclamo.setIdBen1(rs.getInt("ID_BEN1"));
        	reclamo.setImpMov(rs.getDouble("IMP_MOV"));
        	reclamo.setIncPol(rs.getInt("INC_POL"));
        	
        	
        	cal = Utils.getCalendarTimeZone0(rs.getString("INI_VIG"), Constantes.FORMATO_FECHA);
        	if(cal != null){
        		reclamo.setIniVig(cal);
        	}else{
        		logger.error("NO SE PUDO PARSEAR LA FECHA INI_VIG !!! " + rs.getString("INI_VIG"));
        	}
        	
        	
        	reclamo.setIsrMov(rs.getDouble("ISR_MOV"));
        	reclamo.setIvaMov(rs.getDouble("IVA_MOV"));
        	reclamo.setIvrMov(rs.getDouble("IVR_MOV"));
        	reclamo.setMatAfe(rs.getString("MAT_AFE"));
        	reclamo.setMatCli(rs.getString("MAT_CLI"));
        	reclamo.setMunImp(rs.getInt("MUN_IMP"));
        	reclamo.setMunOcu(rs.getInt("MUN_OCU"));
        	reclamo.setNomAfe(rs.getString("NOM_AFE"));
        	reclamo.setNomCli(rs.getString("NOM_CLI"));
        	reclamo.setNumDoc(rs.getString("NUM_DOC"));
        	reclamo.setNumMov(rs.getInt("NUM_MOV"));
        	reclamo.setNumPol(rs.getInt("NUM_POL"));
        	reclamo.setNumSin(rs.getInt("NUM_SIN"));
        	reclamo.setNumSol(rs.getInt("NUM_SOL"));
        	reclamo.setObsSin(rs.getString("OBS_SIN"));
        	reclamo.setPatAfe(rs.getString("PAT_AFE"));
        	reclamo.setPatCli(rs.getString("PAT_CLI"));
        	reclamo.setRamPol(rs.getInt("RAM_POL"));
        	reclamo.setRegRec(rs.getString("REG_REC"));
        	reclamo.setSucAdm(rs.getInt("SUC_ADM"));
        	reclamo.setSucPol(rs.getInt("SUC_POL"));
        	reclamo.setTipBen(rs.getString("TIP_BEN"));
        	reclamo.setTipBen1(rs.getString("TIP_BEN1"));
        	reclamo.setTipImp(rs.getString("TIP_IMP"));
        	reclamo.setTipMov(rs.getString("TIP_MOV"));
        	reclamo.setTisecuencialafi(rs.getInt("TISECUENCIALAFI"));
            return reclamo;
        }
    }


	@Override
	public void cambiarEstatusMAUTSERV(String nmautser, String status) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmautser_i", nmautser);
		params.put("pv_status_i", status);
		Map<String, Object> mapResult = ejecutaSP(new CambioEstatusMAUTSERV(getDataSource()), params);
	}
	
	protected class CambioEstatusMAUTSERV extends StoredProcedure
	{
		protected CambioEstatusMAUTSERV(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.p_upd_status_mautserv");
			declareParameter(new SqlParameter("pv_nmautser_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}


	@Override
	public List<AltaTramiteVO> consultaListaAltaTramite(String ntramite) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_ntramite_i", ntramite);
		Map<String, Object> mapResult = ejecutaSP(new ConsultaListaAltaTramite(getDataSource()), params);
		return (List<AltaTramiteVO>) mapResult.get("pv_registro_o");
	}
	
	protected class ConsultaListaAltaTramite extends StoredProcedure
	{
		protected ConsultaListaAltaTramite(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.p_get_tfacmesctrl_tworksin");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosListaAltaTramite()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	protected class DatosListaAltaTramite  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	AltaTramiteVO consulta = new AltaTramiteVO();
        	//rs.getString("")
        	consulta.setNtramite(rs.getString("NTRAMITE"));
        	consulta.setNfactura(rs.getString("NFACTURA"));
        	consulta.setFfactura(Utils.formateaFecha(rs.getString("FFACTURA")));
        	consulta.setCdtipser(rs.getString("CDTIPSER"));
        	consulta.setDstipser(rs.getString("DSTIPSER"));
        	consulta.setDspresta(rs.getString("DSPRESTA"));
        	consulta.setCdpresta(rs.getString("CDPRESTA"));
        	consulta.setPtimport(rs.getString("PTIMPORT"));
        	consulta.setCdgarant(rs.getString("CDGARANT"));
        	consulta.setCdconval(rs.getString("CDCONVAL"));
        	consulta.setDescporc(rs.getString("DESCPORC"));
        	consulta.setDescnume(rs.getString("DESCNUME"));
        	consulta.setCdunieco(rs.getString("CDUNIECO"));
        	consulta.setCdramo(rs.getString("CDRAMO"));
        	consulta.setEstado(rs.getString("ESTADO"));
        	consulta.setNmpoliza(rs.getString("NMPOLIZA"));
        	consulta.setNmsolici(rs.getString("NMSOLICI"));
        	consulta.setNmsuplem(rs.getString("NMSUPLEM"));
        	consulta.setNmsituac(rs.getString("NMSITUAC"));
        	consulta.setCdtipsit(rs.getString("CDTIPSIT"));
        	consulta.setNombre(rs.getString("NOMBRE"));
        	consulta.setCdperson(rs.getString("CDPERSON"));
        	consulta.setFeocurre(Utils.formateaFecha(rs.getString("FEOCURRE")));
        	consulta.setNmautser(rs.getString("NMAUTSER"));
        	consulta.setNmpoliex(rs.getString("NMPOLIEX"));
        	consulta.setNombreAsegurado(rs.getString("CDPERSON")+" "+ rs.getString("NOMBRE"));
        	consulta.setCdmoneda(rs.getString("CDMONEDA"));
        	consulta.setDesTipomoneda(rs.getString("DESTIPOMONEDA"));
        	consulta.setTasacamb(rs.getString("TASACAMB"));
        	consulta.setPtimporta(rs.getString("PTIMPORTA"));
        	consulta.setNombProv(rs.getString("NOMBPROV"));
        	consulta.setEmail(rs.getString("EMAIL"));
        	consulta.setTelefono(rs.getString("TELEFONO"));
            return consulta;
        }
    }


	@Override
	public List<MesaControlVO> consultaListaMesaControl(String ntramite) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_ntramite_i", ntramite);
		Map<String, Object> mapResult = ejecutaSP(new ConsultaListaMesaControl(getDataSource()), params);
		return (List<MesaControlVO>) mapResult.get("pv_registro_o");
	}
	
	protected class ConsultaListaMesaControl extends StoredProcedure
	{
		protected ConsultaListaMesaControl(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.P_GET_TRAMITE_COMPLETO");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosListaMesaControl()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	protected class DatosListaMesaControl  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	MesaControlVO consulta = new MesaControlVO();
        	//rs.getString("")
        	consulta.setNtramitemc(rs.getString("NTRAMITE"));
        	consulta.setCduniecomc(rs.getString("CDUNIECO"));
        	consulta.setCdramomc(rs.getString("CDRAMO"));
        	consulta.setEstadomc(rs.getString("ESTADO"));
        	consulta.setNmpolizamc(rs.getString("NMPOLIZA"));
        	consulta.setNmsuplemmc(rs.getString("NMSUPLEM"));
        	consulta.setNmsolicimc(rs.getString("NMSOLICI"));
        	consulta.setCdsucadmmc(rs.getString("CDSUCADM"));
        	consulta.setCdsucdocmc(rs.getString("CDSUCDOC"));
        	consulta.setCdsubrammc(rs.getString("CDSUBRAM"));
        	consulta.setCdtiptramc(rs.getString("CDTIPTRA"));
        	consulta.setFerecepcmc(Utils.formateaFecha(rs.getString("FERECEPC")));
        	consulta.setCdagentemc(rs.getString("CDAGENTE"));
        	consulta.setReferenciamc(rs.getString("REFERENCIA"));
        	consulta.setNombremc(rs.getString("NOMBRE"));
        	consulta.setFecstatumc(Utils.formateaFecha(rs.getString("FECSTATU")));
        	consulta.setStatusmc(rs.getString("STATUS"));
        	consulta.setCommentsmc(rs.getString("COMMENTS"));
        	consulta.setCdtipsitmc(rs.getString("CDTIPSIT"));
        	consulta.setOtvalor01mc(rs.getString("OTVALOR01"));
        	consulta.setOtvalor02mc(rs.getString("OTVALOR02"));
        	consulta.setOtvalor03mc(rs.getString("OTVALOR03"));
        	consulta.setOtvalor04mc(rs.getString("OTVALOR04"));
        	consulta.setOtvalor05mc(rs.getString("OTVALOR05"));
        	consulta.setOtvalor06mc(Utils.formateaFecha(rs.getString("OTVALOR06")));
        	consulta.setOtvalor07mc(rs.getString("OTVALOR07"));
        	consulta.setOtvalor08mc(rs.getString("OTVALOR08"));
        	consulta.setOtvalor09mc(rs.getString("OTVALOR09"));
        	consulta.setOtvalor10mc(Utils.formateaFecha(rs.getString("OTVALOR10")));
        	consulta.setOtvalor11mc(rs.getString("OTVALOR11"));
        	consulta.setOtvalor12mc(rs.getString("OTVALOR12"));
        	consulta.setOtvalor13mc(rs.getString("OTVALOR13"));
        	consulta.setOtvalor14mc(rs.getString("OTVALOR14"));
        	consulta.setOtvalor15mc(rs.getString("OTVALOR15"));
        	consulta.setOtvalor16mc(rs.getString("OTVALOR16"));
        	consulta.setOtvalor17mc(rs.getString("OTVALOR17"));
        	consulta.setOtvalor18mc(rs.getString("OTVALOR18"));
        	consulta.setOtvalor19mc(rs.getString("OTVALOR19"));
        	consulta.setOtvalor20mc(rs.getString("OTVALOR20"));
        	consulta.setOtvalor21mc(rs.getString("OTVALOR21"));
        	consulta.setOtvalor22mc(rs.getString("OTVALOR22"));
        	consulta.setOtvalor23mc(rs.getString("OTVALOR23"));
        	consulta.setOtvalor24mc(rs.getString("OTVALOR24"));
        	consulta.setOtvalor25mc(rs.getString("OTVALOR25"));
        	consulta.setOtvalor26mc(rs.getString("OTVALOR26"));
        	consulta.setOtvalor27mc(rs.getString("OTVALOR27"));
        	consulta.setOtvalor28mc(rs.getString("OTVALOR28"));
        	consulta.setOtvalor29mc(rs.getString("OTVALOR29"));
        	consulta.setOtvalor30mc(rs.getString("OTVALOR30"));
        	consulta.setOtvalor31mc(rs.getString("OTVALOR31"));
        	consulta.setOtvalor32mc(rs.getString("OTVALOR32"));
        	consulta.setOtvalor33mc(rs.getString("OTVALOR33"));
        	consulta.setOtvalor34mc(rs.getString("OTVALOR34"));
        	consulta.setOtvalor35mc(rs.getString("OTVALOR35"));
        	consulta.setOtvalor36mc(rs.getString("OTVALOR36"));
        	consulta.setOtvalor37mc(rs.getString("OTVALOR37"));
        	consulta.setOtvalor38mc(rs.getString("OTVALOR38"));
        	consulta.setOtvalor39mc(rs.getString("OTVALOR39"));
        	consulta.setOtvalor40mc(rs.getString("OTVALOR40"));
        	consulta.setOtvalor41mc(rs.getString("OTVALOR41"));
        	consulta.setOtvalor42mc(rs.getString("OTVALOR42"));
        	consulta.setOtvalor43mc(rs.getString("OTVALOR43"));
        	consulta.setOtvalor44mc(rs.getString("OTVALOR44"));
        	consulta.setOtvalor45mc(rs.getString("OTVALOR45"));
        	consulta.setOtvalor46mc(rs.getString("OTVALOR46"));
        	consulta.setOtvalor47mc(rs.getString("OTVALOR47"));
        	consulta.setOtvalor48mc(rs.getString("OTVALOR48"));
        	consulta.setOtvalor49mc(rs.getString("OTVALOR49"));
        	consulta.setOtvalor50mc(rs.getString("OTVALOR50"));
        	consulta.setCdtipsup(rs.getString("CDTIPSUP"));
        	return consulta;
        }
    }
	
	@Override
	public void eliminacionAsegurado(String ntramite,String factura, String valorAccion) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmtramite_i", ntramite);
		params.put("pv_nfactura_i", factura);
		params.put("pv_nmautser_i", null);
		params.put("pv_valorAccion_i", valorAccion);
		
		logger.debug("VALOR DEL PARAMS");
		logger.debug(params);
		Map<String,Object> resultadoMap=this.ejecutaSP(new EliminacionAsegurado(this.getDataSource()), params);
	}
	protected class EliminacionAsegurado extends StoredProcedure
	{
		protected EliminacionAsegurado(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.P_BORRA_ASEGURADO");
			declareParameter(new SqlParameter("pv_nmtramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmautser_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_valorAccion_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}


	@Override
	//public void eliminacionFacturaTramite(String ntramite, String nfactura, String valorAccion) throws Exception { (EGS)
	public void eliminacionFacturaTramite(String ntramite, String nfactura, String valorAccion, String cdramo) throws Exception { // (EGS)
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_ntramite_i", ntramite);
		params.put("pv_nfactura_i", nfactura);
		params.put("pv_valorAccion_i", valorAccion);
		params.put("PV_CDRAMO_I", cdramo);		// (EGS)
		 
		Map<String,Object> resultadoMap=this.ejecutaSP(new EliminacionFacturaTramite(this.getDataSource()), params);
	}
	protected class EliminacionFacturaTramite extends StoredProcedure
	{
		protected EliminacionFacturaTramite(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.P_BORRA_FACTURATRAMITE");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_valorAccion_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_CDRAMO_I",OracleTypes.VARCHAR)); //(EGS)
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public List<SiniestroVO>  solicitudPagoEnviada(Map params)throws Exception {
		Map<String,Object> resultadoMap=this.ejecutaSP(new SolicitudPagoEnviada(this.getDataSource()), params);
		return (List<SiniestroVO>) resultadoMap.get("pv_registro_o");	
	}
	protected class SolicitudPagoEnviada extends StoredProcedure
	{
		protected SolicitudPagoEnviada(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_ACT_TRAMITE_SINI_A_ENVIADO");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new SiniestrosMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	protected class SiniestrosMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	SiniestroVO siniestro = new SiniestroVO();
        	siniestro.setCdunieco(rs.getString("CDUNIECO"));
        	siniestro.setCdramo(rs.getString("CDRAMO"));
        	siniestro.setEstado(rs.getString("ESTADO"));
        	siniestro.setNmpoliza(rs.getString("NMPOLIZA"));
        	siniestro.setNmsuplem(rs.getString("NMSUPLEM"));
        	siniestro.setNmsituac(rs.getString("NMSITUAC"));
        	siniestro.setAapertu(rs.getString("AAAPERTU"));
        	siniestro.setStatusSinies(rs.getString("STATUS"));
        	siniestro.setNmsinies(rs.getString("NMSINIES"));
        	
        	return siniestro;
        }
    }
	
	
	/*	public void eliminacionRegistrosTabla(String nmautser)
			throws Exception {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pv_nmautser_i", nmautser);
			Map<String,Object> resultadoMap=this.ejecutaSP(new EliminacionRegistros(this.getDataSource()), params);
		}
		protected class EliminacionRegistros extends StoredProcedure
		{
			protected EliminacionRegistros(DataSource dataSource)
			{
				super(dataSource, "PKG_PRESINIESTRO.P_BORRA_TDETAUTS");
				declareParameter(new SqlParameter("pv_nmautser_i", OracleTypes.VARCHAR));
				declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
				declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
				compile();
			}
		}
		*/
	
	@Override
	public Map<String,String> obtenerDatosProveedor(String cdpresta) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("cdpresta",cdpresta);
		Map<String,Object>respuesta = this.ejecutaSP(new ObtenerDatosProveedor(this.getDataSource()), params);
		List<Map<String,String>>registro = (List<Map<String, String>>) respuesta.get("pv_registro_o");
		if(registro==null||registro.isEmpty())
		{
			throw new Exception("No se encuentra el proveedor con clave '"+cdpresta+"'");
		}
		if(registro.size()>1)
		{
			throw new Exception("Se encontro mas de un proveedor con clave '"+cdpresta+"'");
		}
		return registro.get(0);
	}

	protected class ObtenerDatosProveedor extends StoredProcedure
	{
		protected ObtenerDatosProveedor(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_GET_MPRESTAD");
			declareParameter(new SqlParameter("cdpresta", OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CDPRESTA"
					,"NOMBRE"
					,"ISR"
					,"CEDULAR"
					,"IVA"
					,"IDPROVEEDOR"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void movTimpsini(String accion
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String aaapertu
			,String status
			,String nmsinies
			,String ntramite
			,String ptimport
			,String iva
			,String ivr
			,String isr
			,String cedular
			,boolean enviado
			,String nmsecsin) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("accion"   , accion);
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("nmsituac" , nmsituac);
		params.put("aaapertu" , aaapertu);
		params.put("status"   , status);
		params.put("nmsinies" , nmsinies);
		params.put("ntramite" , ntramite);
		params.put("ptimport" , ptimport);
		params.put("iva"      , iva);
		params.put("ivr"      , ivr);
		params.put("isr"      , isr);
		params.put("cedular"  , cedular);
		params.put("enviado",enviado?Constantes.SI:Constantes.NO);
		params.put("nmsecsin"  , nmsecsin);
		
		logger.debug("params: "+params);
		ejecutaSP(new MovTimpsini(this.getDataSource()), params);
	}
	
	protected class MovTimpsini extends StoredProcedure
	{
		protected MovTimpsini(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_MOV_TIMPSINI");
			declareParameter(new SqlParameter("accion"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("aaapertu" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("status"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsinies" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ptimport" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("iva"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ivr"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("isr"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cedular"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("enviado"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsecsin"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String>obtenerAutorizacionesFactura(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String aaapertu
			,String status
			,String nmsinies
			,String nfactura) throws Exception
	{
		Map<String,String>params = new HashMap<String,String>();
		params.put("cdunieco",cdunieco);
		params.put("cdramo",cdramo);
		params.put("estado",estado);
		params.put("nmpoliza",nmpoliza);
		params.put("nmsuplem",nmsuplem);
		params.put("nmsituac",nmsituac);
		params.put("aaapertu",aaapertu);
		params.put("status",status);
		params.put("nmsinies",nmsinies);
		params.put("nfactura",nfactura);
		logger.info("params: "+params);
		Map<String,Object> result=ejecutaSP(new ObtenerAutorizacionesFactura(getDataSource()), params);
		List<Map<String,String>>autorizacionesFacturaLista = (List<Map<String,String>>)result.get("pv_registro_o");
		if(autorizacionesFacturaLista==null || autorizacionesFacturaLista.size()==0)
		{
			throw new Exception("No se encuentran las autorizaciones de factura");
		}
		if(autorizacionesFacturaLista.size()>1)
		{
			throw new Exception("Se encontraron autorizaciones de factura duplicada");
		}
		return autorizacionesFacturaLista.get(0);
	}
	
	protected class ObtenerAutorizacionesFactura extends StoredProcedure
	{
		protected ObtenerAutorizacionesFactura(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_GET_MAUTSINI_FACTURA");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("aaapertu" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("status"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsinies" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nfactura" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"AUTRECLA"
					,"COMMENAR"
					,"AUTMEDIC"
					,"COMMENME"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public void guardarTotalProcedenteFactura(String ntramite,String nfactura,String importe,String nmsecsin)throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("ntramite" , ntramite);
		params.put("nfactura" , nfactura);
		params.put("importe"  , importe);
		params.put("nmsecsin"  , nmsecsin);
		logger.debug("params: "+params);
		ejecutaSP(new GuardarTotalProcedenteFactura(this.getDataSource()), params);
	}
	
	protected class GuardarTotalProcedenteFactura extends StoredProcedure
	{
		protected GuardarTotalProcedenteFactura(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_UPD_TFACTURA");
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nfactura" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("importe"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsecsin"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String validaDocumentosAutServicio(String ntramite) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_ntramite_i", ntramite);
		
		Map<String, Object> resultado = ejecutaSP(new ExisteDocumentosAutServ(getDataSource()), params);
		return (String) resultado.get("PV_VALOR_O");
	}
	
    protected class ExisteDocumentosAutServ extends StoredProcedure {
    	
    	protected ExisteDocumentosAutServ(DataSource dataSource) {
    		
    		super(dataSource, "PKG_PRESINIESTRO.p_valida_doc_autser");
    		declareParameter(new SqlParameter("pv_ntramite_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("PV_VALOR_O", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    @Override
    public void turnarTramite(String ntramite,String cdsisrol,String cdusuari) throws Exception
    {
    	Map<String,String>params=new HashMap<String,String>();
    	params.put("ntramite",ntramite);
    	params.put("cdsisrol",cdsisrol);
    	params.put("cdusuari",cdusuari);
    	logger.debug("params: "+params);
		ejecutaSP(new TurnarTramite(this.getDataSource()), params);
	}
	
	protected class TurnarTramite extends StoredProcedure
	{
		protected TurnarTramite(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.P_TURNAR_TRAMITE");
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> obtenerUsuariosPorRol(String cdsisrol)throws Exception
	{
		Map<String,String>params = new HashMap<String,String>();
		params.put("cdsisrol",cdsisrol);
		Map<String,Object>result = this.ejecutaSP(new ObtenerUsuariosPorRol(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtenerUsuariosPorRol extends StoredProcedure
	{
		protected ObtenerUsuariosPorRol(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.P_GET_USUARIOS_ROL");
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CDUSUARIO" , "DSUSUARIO"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR  , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,Object> moverTramite(
			String ntramite
			,String nuevoStatus
			,String comments
			,String cdusuariSesion
			,String cdsisrolSesion
			,String cdusuariDestino
			,String cdsisrolDestino
			,String cdmotivo
			,String cdclausu
			,String swagente
			) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("ntramite"        , ntramite);
		params.put("nuevoStatus"     , nuevoStatus);
		params.put("comments"        , comments);
		params.put("cdusuariSesion"  , cdusuariSesion);
		params.put("cdsisrolSesion"  , cdsisrolSesion);
		params.put("cdusuariDestino" , cdusuariDestino);
		params.put("cdsisrolDestino" , cdsisrolDestino);
		params.put("cdmotivo"        , cdmotivo);
		params.put("cdclausu"        , cdclausu);
		if(StringUtils.isBlank(swagente))
		{
			swagente = "N";
		}
		params.put("swagente"        , swagente);
		logger.info("params: "+params);
		Map<String,Object> procRes  = ejecutaSP(new MoverTramite(getDataSource()), params);
		boolean            escalado = "S".equals((String)procRes.get("pv_escalado_o"));
		String             status   = (String)procRes.get("pv_status_esc_o");
		String             nombre   = (String)procRes.get("pv_nombre_o");
		Map<String,Object> result   = new HashMap<String,Object>();
		result.put("ESCALADO" , escalado);
		result.put("STATUS"   , status);
		result.put("NOMBRE"   , nombre);
		logger2.debug("\nMover tramite, escalado: {}, status: {}, result: {}",escalado,status,result);
		return result;
	}
	
	protected class MoverTramite extends StoredProcedure
	{
		protected MoverTramite(DataSource dataSource)
		{
			super(dataSource, "PKG_SATELITES2.P_MOV_TRAMITE");
			declareParameter(new SqlParameter("ntramite"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nuevoStatus"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("comments"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuariSesion"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrolSesion"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuariDestino" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrolDestino" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdmotivo"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdclausu"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swagente"        , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_escalado_o"   , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_status_esc_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_nombre_o"     , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"     , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"      , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public void turnarAutServicio(
			String ntramite
			,String nuevoStatus
			,String comments
			,String cdusuariSesion
			,String cdsisrolSesion
			,String cdusuariDestino
			,String cdsisrolDestino
			,String cdmotivo
			,String cdclausu
			) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("ntramite"        , ntramite);
		params.put("nuevoStatus"     , nuevoStatus);
		params.put("comments"        , comments);
		params.put("cdusuariSesion"  , cdusuariSesion);
		params.put("cdsisrolSesion"  , cdsisrolSesion);
		params.put("cdusuariDestino" , cdusuariDestino);
		params.put("cdsisrolDestino" , cdsisrolDestino);
		params.put("cdmotivo"        , cdmotivo);
		params.put("cdclausu"        , cdclausu);
		logger.info("params: "+params);
		ejecutaSP(new TurnarAutServicio(getDataSource()), params);
	}
	
	protected class TurnarAutServicio extends StoredProcedure
	{
		protected TurnarAutServicio(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_MOV_AUTSERVICIO");
			declareParameter(new SqlParameter("ntramite"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nuevoStatus"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("comments"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuariSesion"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrolSesion"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuariDestino" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrolDestino" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdmotivo"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdclausu"        , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}	

	@Override
	public String guardaAltaSiniestroSinAutorizacion(String ntramite,String cdunieco,String cdramo, String estado,String nmpoliza,
			  String nmsuplem,String nmsituac, String cdtipsit, Date fechaOcurrencia,String nfactura, String secAsegurado)throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_ntramite_i", ntramite);
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i", cdramo);
		params.put("pv_estado_i", estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_nmsuplem_i", nmsuplem);
		params.put("pv_nmsituac_i", nmsituac);
		params.put("pv_cdtipsit_i", cdtipsit);
		params.put("pv_fechaOcurrencia_i", fechaOcurrencia);
		params.put("pv_nfactura_i", nfactura);
		params.put("pv_secAsegurado_i", secAsegurado);
		
		Map<String, Object> mapResult = ejecutaSP(new GuardaAltaSiniestroSinAutorizacion(this.getDataSource()), params);
		java.math.BigDecimal msgId = (java.math.BigDecimal)mapResult.get("pv_msg_id_o"); 
		return msgId.toPlainString();
	}
	
	protected class GuardaAltaSiniestroSinAutorizacion extends StoredProcedure
	{
		protected GuardaAltaSiniestroSinAutorizacion(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_GEN_SINIESTRO");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fechaOcurrencia_i", OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_secAsegurado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}


	@Override
	public String obtieneMontoArancelCPT(String tipoConcepto, String idProveedor, String idConceptoTipo) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		//params.put("pv_tipoConcepto_i", tipoConcepto);
		params.put("pv_cdpresta_i", idProveedor);
		params.put("pv_cdcpt_i", idConceptoTipo);
		
		Map<String, Object> resultado = ejecutaSP(new ObtieneMontoArancel(getDataSource()), params);
		logger.debug( resultado.get("pv_registro_o"));
		return (String) resultado.get("pv_registro_o");
	}
	
    protected class ObtieneMontoArancel extends StoredProcedure {
    	
    	protected ObtieneMontoArancel(DataSource dataSource) {
    		
    		super(dataSource, "PKG_SINIESTRO.P_GET_ARANCEL");
    		declareParameter(new SqlParameter("pv_cdpresta_i",   OracleTypes.VARCHAR));		// Id. del proveedor
    		declareParameter(new SqlParameter("pv_cdcpt_i", OracleTypes.VARCHAR));		// Id. del concepto
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }


	@Override
	public void eliminacionDocumentosxTramite(String ntramite) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_ntramite_i", ntramite);
		Map<String,Object> resultadoMap=this.ejecutaSP(new EliminacionDocumentosxTramite(this.getDataSource()), params);
	}
	
	protected class EliminacionDocumentosxTramite extends StoredProcedure
	{
		protected EliminacionDocumentosxTramite(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.P_ELIMINA_DOCTOS_X_TRAMITE");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}


	@Override
	public String obtieneMesesTiempoEspera(String valorICDCPT, String nomTabla)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdtabla_i", nomTabla);
		params.put("pv_otclave_i", valorICDCPT);
		Map<String, Object> resultado = ejecutaSP(new ObtieneMesesTiempoEspera(getDataSource()), params);
		return (String) resultado.get("pv_registro_o");
	}
	
    protected class ObtieneMesesTiempoEspera extends StoredProcedure {
    	
    	protected ObtieneMesesTiempoEspera(DataSource dataSource) {
    		super(dataSource, "PKG_PRESINIESTRO.P_OBTIENE_MESES_TESPERA");
    		declareParameter(new SqlParameter("pv_cdtabla_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otclave_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    @Override
	public String obtieneUsuarioTurnadoSiniestro(String ntramite, String rolDestino)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_ntramite_i", ntramite);
		params.put("pv_roldestino_i", rolDestino);
		Map<String, Object> resultado = ejecutaSP(new ObtieneUsuarioTurnadoSiniestro(getDataSource()), params);
		logger.debug("VALOR DE RESPUESTA -->"+(String) resultado.get("pv_registro_o"));
		return (String) resultado.get("pv_registro_o");
	}
	
    protected class ObtieneUsuarioTurnadoSiniestro extends StoredProcedure {
    	
    	protected ObtieneUsuarioTurnadoSiniestro(DataSource dataSource) {
    		super(dataSource, "PKG_PRESINIESTRO.P_OBTIENE_USUARIO_TURNADO");
    		declareParameter(new SqlParameter("pv_ntramite_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_roldestino_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }

	@Override
	public List<GenericVO> obtieneListadoRamoSalud() throws Exception {
		Map<String,Object> resultadoMap=this.ejecutaSP(new ObtenerListadoRamoSalud(this.getDataSource()), new HashMap<String,String>());
		return (List<GenericVO>) resultadoMap.get("pv_registro_o");
	}
	
	protected class ObtenerListadoRamoSalud extends StoredProcedure
	{
		protected ObtenerListadoRamoSalud(DataSource dataSource)
		{
			super(dataSource, "PKG_CONSULTA.P_LISTA_RAMOS_SALUD");
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosListadoRamoSalud()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	protected class DatosListadoRamoSalud  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	GenericVO consulta = new GenericVO();
        	consulta.setKey(rs.getString("CDRAMO"));
        	consulta.setValue(rs.getString("DSRAMO"));
            return consulta;
        }
    }

	@Override
	public List<Map<String, String>> obtieneDatosAdicionales(Map<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ObtieneDatosAdicionales(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneDatosAdicionales extends StoredProcedure {
		protected ObtieneDatosAdicionales(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_SINIESTRO.P_GET_REQAUTSERV");
			declareParameter(new SqlParameter("pv_cobertura_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_subcobertura_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdramo_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
    		String[] cols = new String[]{
    				"REQAUTSERV"
    				,"SUMADISP"
    				,"REQPENALIZACION"
    				,"VALMATERNIDAD"
    				,"VALSESIONES"
    				,"REQVALSUMASEGURADA"
    				,"REQTIPOATENCION"
    		};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public List<Map<String, String>> obtieneDatosCirculoHospitalarioMultisalud(Map<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ObtieneDatosCirculoHospitalarioMultisalud(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneDatosCirculoHospitalarioMultisalud extends StoredProcedure {
		protected ObtieneDatosCirculoHospitalarioMultisalud(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_PRESINIESTRO.P_OBTIENE_CIRCULO_HOSPITALARIO");
			declareParameter(new SqlParameter("pv_cdpresta_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_feautori_i", OracleTypes.DATE));
    		String[] cols = new String[]{
    				"HOSPITALPLUS"
    				,"PORCINCREMENTO"
    				,"MULTINCREMENTO"
    		};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String eliminarAsegurado(HashMap<String, Object> paramsTworkSin) throws Exception {
		Map<String, Object> mapResult = ejecutaSP(new eliminaAseguradoRegistrado(getDataSource()), paramsTworkSin);
		return (String) mapResult.get("pv_msg_id_o");
	}
	
	protected class eliminaAseguradoRegistrado extends StoredProcedure {
		protected eliminaAseguradoRegistrado(DataSource dataSource) {
			super(dataSource, "PKG_PRESINIESTRO.P_ELIMINA_ASEGURADO");
			declareParameter(new SqlParameter("pv_nmtramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_feocurre_i", OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_nmsinies_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_accion_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}


	@Override
	public List<Map<String, String>> obtieneDatosAdicionalesCobertura(Map<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ObtieneDatosAdicionalesCobertura(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneDatosAdicionalesCobertura extends StoredProcedure {
		protected ObtieneDatosAdicionalesCobertura(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_SINIESTRO.P_LISTA_DATOS_COBERTURA");
			declareParameter(new SqlParameter("pv_ntramite_i",   OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CDRAMO","NTRAMITE","ESTADO","CDUNIECO","NMPOLIZA","NMSITUAC"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
 	@Override
	public String obtieneTramiteEnProceso(String nfactura, String cdpresta, String ptimport) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdpresta_i"   , cdpresta);
		params.put("pv_nfactura_i" , nfactura);
		params.put("pv_ptimport_i" , ptimport);
		logger.debug("obtiene ntramite : "+params);
		Map<String, Object> resultado = ejecutaSP(new ObtieneTramiteEnProceso(getDataSource()), params);
		return (String) resultado.get("pv_ntramite_o");
	}

    protected class ObtieneTramiteEnProceso extends StoredProcedure {
    	
    	protected ObtieneTramiteEnProceso(DataSource dataSource) {
    		super(dataSource, "PKG_PRESINIESTRO.P_GET_TRAMITE_FACT_PAG");
    		declareParameter(new SqlParameter("pv_cdpresta_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ptimport_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_ntramite_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }

	@Override
	public List<Map<String,String>> obtenerAseguradosTramite(Map<String, String> params) throws Exception
	{
		Map<String, Object> mapResult = ejecutaSP(new ObtenerAseguradosTramite(this.getDataSource()), params);
		return (List<Map<String,String>>) mapResult.get("pv_registro_o");
	}
	
	protected class ObtenerAseguradosTramite extends StoredProcedure
	{
		protected ObtenerAseguradosTramite(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.P_OBTIENE_TWORKSIN");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"MODUNIECO"
					,"MODRAMO"
					,"MODESTADO"
					,"MODPOLIZAAFECTADA"
					,"MODNMSOLICI"
					,"MODNMSUPLEM"
					,"MODNMSITUAC"
					,"MODCDTIPSIT"
					,"MODCDPERSON"
					,"MODCDPERSONDESC"
					,"MODFECHAOCURRENCIA"
					,"MODNMAUTSERV"
					,"NOFACTURAINT"
					,"NMPOLIEX"
					,"MODTELEFONO"
					,"MODEMAIL"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> obtenerInfAseguradosTramite(Map<String, String> params) throws Exception
	{
		Map<String, Object> mapResult = ejecutaSP(new ObtenerInfAseguradosTramite(this.getDataSource()), params);
		return (List<Map<String,String>>) mapResult.get("pv_registro_o");
	}
	
	protected class ObtenerInfAseguradosTramite extends StoredProcedure
	{
		protected ObtenerInfAseguradosTramite(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_LISTA_INF_ASEGURADO");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"NTRAMITE"
					,"NFACTURA"
					,"CDSUCDOC"
					,"OTVALOR02"
					,"OTVALOR12"
					,"CDUNIECO"
					,"CDRAMO"
					,"ESTADO"
					,"NMPOLIZA"
					,"NMSUPLEM"
					,"NMSITUAC"
					,"CDTIPSIT"
					,"CDPERSON"
					,"FEOCURRE"
					,"REQAUTSER"
					,"NMSINIES"
					,"NMAUTESP"
					,"OTVALOR22"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}


	@Override
	public String actualizaValorMC(HashMap<String, Object> modMesaControl) throws Exception {
		Map<String, Object> mapResult = ejecutaSP(new ActualizaValorMC(this.getDataSource()), modMesaControl);
		return (String) mapResult.get("pv_title_o");
	}
	
	protected class ActualizaValorMC extends StoredProcedure
	{
		protected ActualizaValorMC(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.P_UPDATE_VALORES_MC");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsolici_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdsucadm_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdsucdoc_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtiptra_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ferecepc_i", OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_nombre_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_festatus_i", OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_status_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor01_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor02_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor03_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor04_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor05_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor06_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor07_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor08_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor09_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor10_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor11_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor15_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor20_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor22_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}


	@Override
	public List<GenericVO> obtieneListaTipoAtencion(String cdramo, String modalidad, String tipoPago) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_producto_i"   , cdramo);
		params.put("pv_cdtipsit_i" , modalidad);
		params.put("pv_tipoPago_i" , tipoPago);
		Map<String,Object> resultadoMap=this.ejecutaSP(new ObtenerListaTipoAtencion(this.getDataSource()), params);
		return (List<GenericVO>) resultadoMap.get("pv_registro_o");
	}
	
	protected class ObtenerListaTipoAtencion extends StoredProcedure
	{
		protected ObtenerListaTipoAtencion(DataSource dataSource)
		{
			super(dataSource, "PKG_LISTAS.P_GET_TIPOATENCION");
			declareParameter(new SqlParameter("pv_producto_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_tipoPago_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosListaTipoAtencion()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	protected class DatosListaTipoAtencion  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	GenericVO consulta = new GenericVO();
        	consulta.setKey(rs.getString("OTCLAVE3"));
        	consulta.setValue(rs.getString("OTVALOR01"));
            return consulta;
        }
    }


	@Override
	public List<Map<String, String>> obtieneListaAutirizacionServicio(Map<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ObtieneListaAutirizacionServicio(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneListaAutirizacionServicio extends StoredProcedure {
		protected ObtieneListaAutirizacionServicio(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_SINIESTRO.P_LISTA_AUT_SINIESTRO");
			declareParameter(new SqlParameter("pv_cdperson_i",   OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"NMAUTSER","FESOLICI","CDPROVEE","NOMPROV",
					"CDCAUSA","DSCAUSA","CDICD","DSICD"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> obtieneListaMsiniestMaestro(Map<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ObtieneListaMsiniestMaestro(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneListaMsiniestMaestro extends StoredProcedure {
		protected ObtieneListaMsiniestMaestro(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_SINIESTRO.P_LISTA_SINIESTROS_REFERENCIA");
			declareParameter(new SqlParameter("pv_cdunieco_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i",   OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"NMSINIES","CDCAUSA","DSCAUSA","CDICD","DSICD","FEOCURRE","FEAPERTU"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public List<Map<String, String>> obtieneListaDatosValidacionSiniestro(HashMap<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ObtieneListaDatosValidacionSiniestro(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneListaDatosValidacionSiniestro extends StoredProcedure {
		protected ObtieneListaDatosValidacionSiniestro(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_SINIESTRO.P_GET_TVALOSIN_Y_MAUTSINI");
			declareParameter(new SqlParameter("pv_ntramite_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_tipoPago_i",   OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"OTVALOR01","OTVALOR02","OTVALOR03","AREAAUTO","SWAUTORI","COMENTARIOS" 
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> obtieneListaDatosSumaAsegurada(HashMap<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ObtieneListaDatosSumaAsegurada(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneListaDatosSumaAsegurada extends StoredProcedure {
		protected ObtieneListaDatosSumaAsegurada(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_SINIESTRO.P_OBTINE_IMP_PAGADOXPERSONA");
			declareParameter(new SqlParameter("pv_cdunieco_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsinref_i",   OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"SUMA_ASEGURADA","RESERVA_DISPONIBLE"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	
	@Override
	public List<Map<String, String>> obtieneListaDatosValidacionAjustadorMed(HashMap<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ObtieneListaDatosValidacionAjustadorMed(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneListaDatosValidacionAjustadorMed extends StoredProcedure {
		protected ObtieneListaDatosValidacionAjustadorMed(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_SINIESTRO.P_GET_OBTIENE_VAL_AJUSTADORMED");
			declareParameter(new SqlParameter("pv_ntramite_i",   OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"NFACTURA","AREAAUTO","SWAUTORI" 
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	@Override
	public String validaCdTipsitAltaTramite(HashMap<String, Object> paramTramite) throws Exception {
		Map<String, Object> resultado = ejecutaSP(new ObtieneCdTipsitAltaTramite(getDataSource()), paramTramite);
		logger.debug( resultado.get("pv_existe_o"));
		return (String) resultado.get("pv_existe_o");
	}
	
	protected class ObtieneCdTipsitAltaTramite extends StoredProcedure {
		
		protected ObtieneCdTipsitAltaTramite(DataSource dataSource) {
			
			super(dataSource, "PKG_PRESINIESTRO.P_OBTIENE_CDTIPSIT");
			declareParameter(new SqlParameter("pv_ntramite_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_existe_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}


	@Override
	public String obtieneDatosCirculoHospitalario(HashMap<String, Object> paramPenalizacion) throws Exception {
		Map<String, Object> resultado = ejecutaSP(new ObtieneDatosCirculoHospitalario(getDataSource()), paramPenalizacion);
		logger.debug( resultado.get("pv_registro_o"));
		return (String) resultado.get("pv_registro_o");
	}
	
	protected class ObtieneDatosCirculoHospitalario extends StoredProcedure {
    	
    	protected ObtieneDatosCirculoHospitalario(DataSource dataSource) {
    		
    		super(dataSource, "PKG_PRESINIESTRO.P_OBTIENE_PENALIZACION_CIRHOSP");
    		declareParameter(new SqlParameter("pv_circuloHosPoliza_i",   OracleTypes.VARCHAR)); 
			declareParameter(new SqlParameter("pv_circuloHosProv_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_feautori_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
	
	@Override
	public String obtienePorcentajeQuirurgico(String tipoMedico, String feAutorizacion) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_tipoMedico_i", tipoMedico);
		params.put("pv_feAutorizacion_i", feAutorizacion);
		
		Map<String, Object> resultado = ejecutaSP(new ObtienePorcentajeEquipoQuirurgico(getDataSource()), params);
		return (String) resultado.get("pv_registro_o");
	}

	protected class ObtienePorcentajeEquipoQuirurgico extends StoredProcedure {
		
		protected ObtienePorcentajeEquipoQuirurgico(DataSource dataSource) {
			
			super(dataSource, "PKG_PRESINIESTRO.P_GET_PORCENTAJE_QUIRURGICO");
			declareParameter(new SqlParameter("pv_tipoMedico_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_feAutorizacion_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}


	@Override
	public List<Map<String, String>> obtieneSumaAseguradaPeriodoEsperaRec(
			HashMap<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ObtieneSumaAseguradaPeriodoEsperaRec(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	protected class ObtieneSumaAseguradaPeriodoEsperaRec extends StoredProcedure {
		protected ObtieneSumaAseguradaPeriodoEsperaRec(DataSource dataSource) {
			super(dataSource, "PKG_SINIESTRO.P_GET_DATOS_RECUPERA");
			declareParameter(new SqlParameter("pv_cdramo_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cobertura_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_subcobertura_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_feefecto_i",   OracleTypes.DATE));
			String[] cols = new String[]{
					"PLAZOESPERA"
					,"SUMAASEG"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> obtieneEsquemaSumaAseguradaRec(
			HashMap<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ObtieneEsquemaSumaAseguradaRec(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	protected class ObtieneEsquemaSumaAseguradaRec extends StoredProcedure {
		protected ObtieneEsquemaSumaAseguradaRec(DataSource dataSource) {
			super(dataSource, "PKG_SINIESTRO.P_GET_ESQUEMASUMA_RECUPERA");
			declareParameter(new SqlParameter("pv_cdunieco_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i",   OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"ESQUEMAASEG"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}


	@Override
	public List<Map<String, String>> obtienePeriodoEsperaAsegurado(
			HashMap<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> result = ejecutaSP(new ObtienePeriodoEsperaAsegurado(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtienePeriodoEsperaAsegurado extends StoredProcedure {
		protected ObtienePeriodoEsperaAsegurado(DataSource dataSource) {
			super(dataSource, "PKG_CONSULTA.P_GET_PERIODO_ESPERA");
			declareParameter(new SqlParameter("pv_cdunieco_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_feocurre_i",   OracleTypes.DATE));
			String[] cols = new String[]{
					"DIAS"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public List<Map<String, String>> obtieneMontoPagoSiniestro(
			HashMap<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> result = ejecutaSP(new ObtieneMontoPagoSiniestro(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneMontoPagoSiniestro extends StoredProcedure {
		protected ObtieneMontoPagoSiniestro(DataSource dataSource) {
			super(dataSource, "PKG_SINIESTRO.P_GET_MONTO_SINIESTROS");
			declareParameter(new SqlParameter("pv_ntramite_i",   OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"IMPORTEFACTURA",
					"MONTOXPAGAR"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	public List<GenericVO> obtieneListadoConceptoPago(String cdramo) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdramo_i", cdramo);
		Map<String, Object> mapResult = ejecutaSP(new ObtieneListadoConceptoPago(getDataSource()), params);
		return (List<GenericVO>) mapResult.get("pv_registro_o");
	}
	
	protected class ObtieneListadoConceptoPago extends StoredProcedure
	{
		protected ObtieneListadoConceptoPago(DataSource dataSource)
		{
			super(dataSource, "PKG_CONSULTA.P_LISTA_CONCEPTOPAGO");
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosListadoConceptoPago()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
    protected class DatosListadoConceptoPago  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	GenericVO consulta = new GenericVO();
        	consulta.setKey(rs.getString("OTVALOR01"));
        	consulta.setValue(rs.getString("OTVALOR02"));
            return consulta;
        }
    }
    
    @SuppressWarnings("unchecked")
	public List<GenericVO> obtieneListadoAseguradoPoliza(String cdunieco, String cdramo, String estado, String nmpoliza, String cdperson) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i", cdramo);
		params.put("pv_estado_i", estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_cdperson_i", cdperson);
		
		Map<String,Object> resultadoMap=this.ejecutaSP(new ObtieneListadoAseguradoPoliza(this.getDataSource()), params);
		return (List<GenericVO>) resultadoMap.get("pv_registro_o");
	}
	protected class ObtieneListadoAseguradoPoliza extends StoredProcedure
	{
		protected ObtieneListadoAseguradoPoliza(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_LISTA_FILTASEG_POLIZA");
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosListaAsegurado()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public List<Map<String, String>> obtieneDatosBeneficiario(
			HashMap<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> result = ejecutaSP(new ObtieneDatosBeneficiario(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneDatosBeneficiario extends StoredProcedure {
		protected ObtieneDatosBeneficiario(DataSource dataSource) {
			super(dataSource, "PKG_CONSULTA.P_RECUPERA_EDAD_ASEGURADO");
			declareParameter(new SqlParameter("pv_cdunieco_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i",   OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"EDAD"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> obtieneDatoMsiniper(
			HashMap<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> result = ejecutaSP(new ObtieneDatoMsiniper(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneDatoMsiniper extends StoredProcedure {
		protected ObtieneDatoMsiniper(DataSource dataSource) {
			super(dataSource, "PKG_SINIESTRO.P_GET_DATOS_MSINIPER");
			declareParameter(new SqlParameter("pv_ntramite_i",   OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"NTRAMITE",			"CDUNIECO",			"CDRAMO",		"ESTADO",
					"NMPOLIZA",			"NMSOLICI",			"NMSUPLEM",		"NMSITUAC",
					"CDTIPSIT",			"CDPERSON",			"FEOCURRE",		"NMAUTSER",
					"NFACTURA", 		"REQAUTES",         "NMORDINA"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> obtieneListaConfiguracionProveedor(HashMap<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ObtieneListaConfiguracionProveedor(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneListaConfiguracionProveedor extends StoredProcedure {
		protected ObtieneListaConfiguracionProveedor(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_SINIESTRO.P_LISTA_CONFIGPROV");
			declareParameter(new SqlParameter("pv_cdpresta_i",   OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CLAVEPROVEEDOR",	"NOMBPROVEEDOR",	"APLICAIVA",
					"APLICAIVADESC",	"SECUENCIAIVA",		"SECIVADESC",
					"APLICAIVARET",		"IVARETDESC", 		"CVECONFI",
					"DESCRIPC"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> obtieneListaLayoutConfigurados(HashMap<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ObtieneListaLayoutConfigurados(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneListaLayoutConfigurados extends StoredProcedure {
		protected ObtieneListaLayoutConfigurados(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_SINIESTRO.P_GET_CAT_LAYOUT");
			declareParameter(new SqlParameter("pv_cadena_i",   OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CDLAYOUT",	"DSLAYOUT"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String obtieneAplicaConceptoIVA(String idConcepto) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_idConcepto_i", idConcepto);
		Map<String, Object> resultado = ejecutaSP(new ObtieneAplicaConceptoIVA(getDataSource()), params);
		logger.debug( resultado.get("pv_registro_o"));
		return (String) resultado.get("pv_registro_o");
	}
	
    protected class ObtieneAplicaConceptoIVA extends StoredProcedure {
    	
    	protected ObtieneAplicaConceptoIVA(DataSource dataSource) {
    		
    		super(dataSource, "PKG_SINIESTRO.P_GET_CONCEPTOIVA");
    		declareParameter(new SqlParameter("pv_idConcepto_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
	@Override
	public String guardaConfiguracionProveedor(String cdpresta, String tipoLayout, String aplicaIVA,String secuenciaIVA,
			 String aplicaIVARET,String cduser, Date fechaProcesamiento, String proceso) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdpresta_i", cdpresta);
		params.put("pv_cveconfi_i", tipoLayout);
		params.put("pv_aplicaIVA_i", aplicaIVA);
		params.put("pv_secuenciaIVA_i", secuenciaIVA);
		params.put("pv_aplicaIVARet_i", aplicaIVARET);
		params.put("pv_cduser_i", cduser);
		params.put("pv_fefecha_i", fechaProcesamiento);
		params.put("pv_accion_i", proceso);
		Map<String, Object> resultado = ejecutaSP(new GuardaConfiguracionProveedor(getDataSource()), params);
		//logger.debug( resultado.get("pv_registro_o"));
		return "0";
	}
	
    protected class GuardaConfiguracionProveedor extends StoredProcedure {
    	
    	protected GuardaConfiguracionProveedor(DataSource dataSource) {
    		
    		super(dataSource,"PKG_SINIESTRO.P_MOV_CONFPROV");
    		declareParameter(new SqlParameter("pv_cdpresta_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cveconfi_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_aplicaIVA_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_secuenciaIVA_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_aplicaIVARet_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cduser_i",  OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_fefecha_i",   OracleTypes.DATE));
    		declareParameter(new SqlParameter("pv_accion_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
	@Override
	public List<GenericVO>obtenerAtributosLayout(String descripcion) throws Exception
	{
		Map<String,String>p=new HashMap<String,String>();
		p.put("pv_descripc_i" , descripcion);
		logger.debug("obtenerAtributosLayout params: "+p);
		Map<String, Object> mapResult = ejecutaSP(new ObtenerAtributosLayout(this.getDataSource()), p);
		List<Map<String,String>>lista=(List<Map<String,String>>) mapResult.get("pv_registro_o");
		List<GenericVO>listaG=new ArrayList<GenericVO>();
		if(lista!=null)
		{
			for(Map<String,String>cpt:lista)
			{
				listaG.add(new GenericVO(cpt.get("CLAVE"),cpt.get("VALOR")));
			}
		}
		return listaG;
	}
	
	protected class ObtenerAtributosLayout extends StoredProcedure
	{
		protected ObtenerAtributosLayout(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_LISTA_ATRIBUTOS_PROV");
			declareParameter(new SqlParameter("pv_descripc_i" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CLAVE" , "VALOR"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String guardaLayoutProveedor(HashMap<String, Object> paramsFacMesaCtrl) throws Exception {
			Map<String, Object> mapResult = ejecutaSP(new GuardaLayoutProveedor(getDataSource()), paramsFacMesaCtrl);
			return (String) mapResult.get("pv_msg_id_o");
	}
	
	protected class GuardaLayoutProveedor extends StoredProcedure {
		protected GuardaLayoutProveedor(DataSource dataSource) {
			super(dataSource, "PKG_SINIESTRO.P_MOV_CONFLAYOUT");
			declareParameter(new SqlParameter("pv_cdpresta_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cveconfi_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cveatri_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmordina_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cveformato_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_valormax_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_valormin_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cveexcel_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_formatfech_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swobliga_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_accion_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> obtieneConfiguracionLayout(Map<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ObtieneConfiguracionLayout(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneConfiguracionLayout extends StoredProcedure {
		protected ObtieneConfiguracionLayout(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_SINIESTRO.P_GET_CONFLAYOUT");
			declareParameter(new SqlParameter("pv_cdpresta_i",   OracleTypes.VARCHAR));
    		String[] cols = new String[]{
    				"CDPRESTA",
    				"CVEATRI",
    				"NMORDINA",
    				"CVEFORMATO",
    				"VALORMAX",
    				"VALORMIN",
    				"CVEEXCEL",
    				"FORMATFECH",
    				"SWOBLIGA"
    				
    		};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> guardaHistorialSiniestro(Map<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new GuardaHistorialSiniestro(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class GuardaHistorialSiniestro extends StoredProcedure {
		protected GuardaHistorialSiniestro(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_SINIESTRO.P_OBTIENE_SECPAGO");
			declareParameter(new SqlParameter("pv_ntramite_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i",   OracleTypes.VARCHAR));
    		String[] cols = new String[]{
    				"RESPUESTA"
    		};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String guardaInfoRecupera(HashMap<String, Object> paramsMRecupera) throws Exception {
			Map<String, Object> mapResult = ejecutaSP(new guardaInfoRecupera(getDataSource()), paramsMRecupera);
			return (String) mapResult.get("pv_msg_id_o");
	}
	
	
	protected class guardaInfoRecupera extends StoredProcedure {
		protected guardaInfoRecupera(DataSource dataSource) {
			super(dataSource, "PKG_SINIESTRO.P_MOV_MRECUPERA");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdgarant_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdconval_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cantporc_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ptimport_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_accion_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>>obtieneInformacionRecupera(String cdunieco,String cdramo, String estado, String nmpoliza, String nmsuplem,
			String nmsituac, Date feEfecto, String ntramite, String nfactura) throws Exception
	{
		Map<String,Object>p=new HashMap<String,Object>();
		p.put("pv_cdunieco_i" , cdunieco);
		p.put("pv_cdramo_i" , cdramo);
		p.put("pv_estado_i" , estado);
		p.put("pv_nmpoliza_i" , nmpoliza);
		p.put("pv_nmsuplem_i" , nmsuplem);
		p.put("pv_nmsituac_i" , nmsituac);
		p.put("pv_feefecto_i" , feEfecto);
		p.put("pv_ntramite_i" , ntramite);
		p.put("pv_nfactura_i" , nfactura);
		logger.debug("P_GET_MSINIVAL params: "+p);
		Map<String, Object> mapResult = ejecutaSP(new obtieneInformacionRecupera(this.getDataSource()), p);
		return (List<Map<String,String>>) mapResult.get("pv_registro_o");
	}
	
	protected class obtieneInformacionRecupera extends StoredProcedure
	{
		protected obtieneInformacionRecupera(DataSource dataSource)
		{
			super(dataSource, "PKG_CONSULTA.P_OBTIENE_INF_RECUPERA");
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i" ,   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i" ,   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_feefecto_i" , OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_ntramite_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i" , OracleTypes.VARCHAR));
			String cols[] = new String[]{
					"NTRAMITE","NFACTURA","CDGARANT","CDCONVAL","CANTPORC","ESQUEMAASEG", "SUMAASEG", "PTIMPORT" 
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>>obtieneEsquemaSumAseguradaRecupera(String cdunieco,String cdramo, String estado, String nmpoliza, String nmsuplem,
			String nmsituac, Date feEfecto, String cdgarant, String cdconval) throws Exception
	{
		Map<String,Object>p=new HashMap<String,Object>();
		p.put("pv_cdunieco_i" , cdunieco);
		p.put("pv_cdramo_i" , cdramo);
		p.put("pv_estado_i" , estado);
		p.put("pv_nmpoliza_i" , nmpoliza);
		p.put("pv_nmsuplem_i" , nmsuplem);
		p.put("pv_nmsituac_i" , nmsituac);
		p.put("pv_cdgarant_i" , cdgarant);
		p.put("pv_cdconval_i" , cdconval);
		p.put("pv_feefecto_i" , feEfecto);
		logger.debug("obtieneEsquemaSumAseguradaRecupera params: "+p);
		Map<String, Object> mapResult = ejecutaSP(new obtieneEsquemaSumAseguradaRecupera(this.getDataSource()), p);
		return (List<Map<String,String>>) mapResult.get("pv_registro_o");
	}
	
	protected class obtieneEsquemaSumAseguradaRecupera extends StoredProcedure
	{
		protected obtieneEsquemaSumAseguradaRecupera(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_GET_ESQSUMA_RECUPERA");
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i" ,   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i" ,   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdgarant_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdconval_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_feefecto_i" , OracleTypes.DATE));
			String cols[] = new String[]{
					"SUMAASEG", "ESQUEMAASEG" 
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}


	@Override
	public void P_MOV_MRECUPERA(String ntramite, String nfactura,
			String cdgarant, String cdconval, String cantporc, String ptimport,
			String accion) throws Exception {
		Map<String,Object>p=new HashMap<String,Object>();
		p.put("pv_ntramite_i" , ntramite);
		p.put("pv_nfactura_i" , nfactura);
		p.put("pv_cdgarant_i" , cdgarant);
		p.put("pv_cdconval_i" , cdconval);
		p.put("pv_cantporc_i" , cantporc);
		p.put("pv_ptimport_i" , ptimport);
		p.put("pv_accion_i"   , accion);
		logger.debug("P_MOV_MRECUPERA params: "+p);
		ejecutaSP(new PMOVMRECUPERA(this.getDataSource()), p);
		logger.debug("PMOVMRECUPERA end");
	}
	
	protected class PMOVMRECUPERA extends StoredProcedure
	{
		protected PMOVMRECUPERA(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_MOV_MRECUPERA");
			declareParameter(new SqlParameter("pv_ntramite_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdgarant_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdconval_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cantporc_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ptimport_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_accion_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String actualizaTelefonoEmailAsegurado(HashMap<String, Object> paramsAsegurado) throws Exception {
		Map<String, Object> mapResult = ejecutaSP(new ActualizaTelefonoEmailAsegurado(getDataSource()), paramsAsegurado);
		return (String) mapResult.get("pv_msg_id_o");
	}
	
	protected class ActualizaTelefonoEmailAsegurado extends StoredProcedure {
		protected ActualizaTelefonoEmailAsegurado(DataSource dataSource) {
			super(dataSource, "PKG_CONSULTA.P_ACTUALIZA_INFASEG");
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmtelefo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsemail_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void guardaAutorizacionConceptos(Map<String, Object> params) throws Exception
	{
		ejecutaSP(new GuardaAutorizacionConceptos(this.getDataSource()), params);
	}
	
	protected class GuardaAutorizacionConceptos extends StoredProcedure
	{
		protected GuardaAutorizacionConceptos(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_INSERTA_AUTMSINIVAL");
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmautser_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdpresta_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>>cargaHistorialCPTPagados(Map<String,String> params) throws Exception
	{
		Map<String, Object> mapResult = ejecutaSP(new CargaHistorialCPTPagados(this.getDataSource()), params);
		return (List<Map<String,String>>) mapResult.get("pv_registro_o");
	}
	
	protected class CargaHistorialCPTPagados extends StoredProcedure
	{
		protected CargaHistorialCPTPagados(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_OBTIENE_CPTPAGADO");
			declareParameter(new SqlParameter("pv_nmautser_i" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"NTRAMITE",			"CONTRARECIBO",			"NFACTURA",			"NMSINIES",
					"DSGARANT",			"DSCONVAL",				"SUBTOTAL",			"IVA",
					"IVARETENIDO",		"ISR",					"IMPCEDULAR",		"PAGADO",
					"DSESTATUS",		"NOMBREASEG",			"POLIZA"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> listaSiniestrosInfAsegurados(Map<String, String> params) throws Exception
	{
		Map<String, Object> mapResult = ejecutaSP(new ListaSiniestrosInfAsegurados(this.getDataSource()), params);
		return (List<Map<String,String>>) mapResult.get("pv_registro_o");
	}
	
	protected class ListaSiniestrosInfAsegurados extends StoredProcedure
	{
		protected ListaSiniestrosInfAsegurados(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_OBTIENE_INFSINIESTRO_ASEG");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CDUNIECO",			"CDRAMO",			"ESTADO",			"NMPOLIZA"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
    @SuppressWarnings("unchecked")
	public List<GenericVO> obtieneListaAseguraAutEspecial(String ntramite, String nfactura) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_ntramite_i", ntramite);
		params.put("pv_nfactura_i", nfactura);
		
		Map<String,Object> resultadoMap=this.ejecutaSP(new ObtieneListaAseguraAutEspecial(this.getDataSource()), params);
		return (List<GenericVO>) resultadoMap.get("pv_registro_o");
	}
	protected class ObtieneListaAseguraAutEspecial extends StoredProcedure
	{
		protected ObtieneListaAseguraAutEspecial(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_OBTIENE_ASEG_AUTESPECIAL");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosListaAsegurado()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<CoberturaPolizaVO> obtieneListadoCoberturaProducto(
			HashMap<String, Object> paramCobertura) throws Exception {
		Map<String, Object> mapResult = ejecutaSP(new ObtieneListadoCoberturaProducto(getDataSource()), paramCobertura);
		
		@SuppressWarnings("unchecked")
		List<CoberturaPolizaVO> listaDatosPoliza = (List<CoberturaPolizaVO>)mapResult.get("pv_registro_o");
		return listaDatosPoliza;
	}
	protected class ObtieneListadoCoberturaProducto extends StoredProcedure {

		protected ObtieneListadoCoberturaProducto(DataSource dataSource) {
			super(dataSource, "PKG_SINIESTRO.P_LISTA_COBER_PRODUCTO");
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatosListaCoberturasMapper()));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String guardaListaAutorizacionEspecial(HashMap<String, Object> paramsAutoriEspecial) throws Exception {
		Map<String, Object> mapResult = ejecutaSP(new guardaListaAutorizacionEspecial(getDataSource()), paramsAutoriEspecial);
		return (String) mapResult.get("pv_nmautesp_o");
	}
	
	protected class guardaListaAutorizacionEspecial extends StoredProcedure {
		protected guardaListaAutorizacionEspecial(DataSource dataSource) {
			super(dataSource, "PKG_SINIESTRO.P_VERIFICA_INF_AUTESP");
			declareParameter(new SqlParameter("pv_nmautesp_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsinies_i", OracleTypes.VARCHAR));
			//declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
			//declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_valrango_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_valcober_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdgarant_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_comments_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cduser_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ferecepc_i", OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_val_lim_i", OracleTypes.VARCHAR)); //(EGS)
			declareParameter(new SqlOutParameter("pv_nmautesp_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}


	@Override
	public List<Map<String, String>> obtenerConfiguracionAutEspecial(HashMap<String, Object> paramsAutoriEspecial) throws Exception {
		Map<String, Object> mapResult = ejecutaSP(new ObtenerConfiguracionAutEspecial(this.getDataSource()), paramsAutoriEspecial);
		return (List<Map<String,String>>) mapResult.get("pv_registro_o");
	}
	
	protected class ObtenerConfiguracionAutEspecial extends StoredProcedure
	{
		protected ObtenerConfiguracionAutEspecial(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_OBTIENE_DATOS_TSINAUSP");
			declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			//declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsinies_i", OracleTypes.VARCHAR));
			//declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"VALRANGO", "VALCOBER", "CDGARANT", "COMMENTS", "NMAUTESP"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String asociarAutorizacionEspecial(HashMap<String, Object> paramAutEspecial) throws Exception {
		Map<String, Object> resultado = ejecutaSP(new AsociarAutorizacionEspecial(getDataSource()), paramAutEspecial);
		logger.debug( resultado.get("pv_mensaje_o"));
		return (String) resultado.get("pv_mensaje_o");
	}
	
    protected class AsociarAutorizacionEspecial extends StoredProcedure {
    	
    	protected AsociarAutorizacionEspecial(DataSource dataSource) {
    		super(dataSource, "PKG_SINIESTRO.P_ACTUALIZA_NMAUTESP");
    		declareParameter(new SqlParameter("pv_ntramite_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_tipoPago_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nfactura_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdunieco_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdramo_i",   	 OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_estado_i",   	 OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmsuplem_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmsituac_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmautesp_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmsinies_i",   OracleTypes.VARCHAR));
    		//declareParameter(new SqlParameter("pv_cdperson_i",   OracleTypes.VARCHAR));
    		//declareParameter(new SqlParameter("pv_cdtipsit_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_mensaje_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
	
	@Override
	public List<Map<String,String>> obtenerDatosAutorizacionEspecial(Map<String, String> params) throws Exception
	{
		Map<String, Object> mapResult = ejecutaSP(new ObtenerDatosAutorizacionEspecial(this.getDataSource()), params);
		return (List<Map<String,String>>) mapResult.get("pv_registro_o");
	}
	
	protected class ObtenerDatosAutorizacionEspecial extends StoredProcedure
	{
		protected ObtenerDatosAutorizacionEspecial(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_VALIDA_AUTESP_USUARIO");
			declareParameter(new SqlParameter("pv_nmautesp_i", OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"NMAUTESP","VALRANGO","VALCOBER","CDGARANT","DSGARANT"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> obtieneListaExisteCoberturaTramite(HashMap<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ObtieneListaExisteCoberturaTramite(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneListaExisteCoberturaTramite extends StoredProcedure {
		protected ObtieneListaExisteCoberturaTramite(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_SINIESTRO.P_VALIDA_EXISTE_COBERTURA");
			declareParameter(new SqlParameter("pv_ntramite_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_formapago_i",  OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"NTRAMITE","NFACTURA","NMSINIES" 
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String validaExisteConfiguracionProv(String cdpresta, String tipoLayout) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdpresta_i", cdpresta);
		params.put("pv_tiplayout_i", tipoLayout);
		Map<String, Object> resultado = ejecutaSP(new ValidaExisteConfiguracionProv(getDataSource()), params);
		logger.debug( resultado.get("pv_existe_o"));
		return (String) resultado.get("pv_existe_o");
	}
	
    protected class ValidaExisteConfiguracionProv extends StoredProcedure {
    	
    	protected ValidaExisteConfiguracionProv(DataSource dataSource) {
    		super(dataSource, "PKG_SINIESTRO.P_VALIDA_CONF_PROV");
    		declareParameter(new SqlParameter("pv_cdpresta_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_tiplayout_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_existe_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    @Override
	public List<Map<String, String>> obtieneConfiguracionLayoutProveedor(HashMap<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ObtieneConfiguracionLayoutProveedor(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneConfiguracionLayoutProveedor extends StoredProcedure {
		protected ObtieneConfiguracionLayoutProveedor(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_SINIESTRO.P_OBTIENE_LAYOUT_INFOPROV");
			declareParameter(new SqlParameter("pv_cdpresta_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cveconfi_i",   OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"MAXREGISTRO","NMORDINA","CVEEXCEL","CVEATRI",
					"DESCEXCEL", "CVEFORMATO", "DESCFECHA", "FORMATFECH",
					"DESCRIPC",	 "SWOBLIGA"
					
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
    @SuppressWarnings("unchecked")
	public List<GenericVO> obtieneListaContrareciboAutEsp(String cdramo, String ntramite) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_ntramite_i", ntramite);
		params.put("pv_cdramo_i", cdramo);
		
		Map<String,Object> resultadoMap=this.ejecutaSP(new ObtieneListaContrareciboAutEsp(this.getDataSource()), params);
		return (List<GenericVO>) resultadoMap.get("pv_registro_o");
	}
	protected class ObtieneListaContrareciboAutEsp extends StoredProcedure
	{
		protected ObtieneListaContrareciboAutEsp(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_OBTIENE_CR_AUTESPECIAL");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosConfAutEspecial()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	protected class DatosConfAutEspecial  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	GenericVO consulta = new GenericVO();
        	consulta.setKey(rs.getString("CLAVE"));
        	consulta.setValue(rs.getString("DESCRIPCION"));
            return consulta;
        }
    }
	
    @SuppressWarnings("unchecked")
	public List<GenericVO> obtieneListaFacturaTramite(String ntramite, String nfactura) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_ntramite_i", ntramite);
		params.put("pv_nfactura_i", nfactura);
		Map<String,Object> resultadoMap=this.ejecutaSP(new ObtieneListaFacturaTramite(this.getDataSource()), params);
		logger.debug("Valor de Respuesta ====>"+resultadoMap);
		return (List<GenericVO>) resultadoMap.get("pv_registro_o");
	}
	protected class ObtieneListaFacturaTramite extends StoredProcedure
	{
		protected ObtieneListaFacturaTramite(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_OBTIENE_FACT_AUTESPECIAL");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosConfAutEspecial()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> procesaPagoAutomaticoSisco(HashMap<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ProcesaPagoAutomaticoSisco(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}

	protected class ProcesaPagoAutomaticoSisco extends StoredProcedure {
		protected ProcesaPagoAutomaticoSisco(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_SINIESTRO.P_GET_TMOVSISCO");
			String[] cols = new String[]{
					"NTRAMITE"
			};
			declareParameter(new SqlParameter("pv_cdusuari_i"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_tipoproc_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public List<Map<String, String>> obtieneValidaconAranceleTramite(HashMap<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ObtieneValidaconAranceleTramite(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneValidaconAranceleTramite extends StoredProcedure {
		protected ObtieneValidaconAranceleTramite(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_SINIESTRO.P_VALIDA_ARANCE_TRAMITE");
			declareParameter(new SqlParameter("pv_ntramite_i",   OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"NTRAMITE","NFACTURA","NMSINIES", "CDCONCEP"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public String obtieneMontoTramitePagoDirecto(HashMap<String, Object> paramsPagoDirecto) throws Exception {
		Map<String, Object> resultado = ejecutaSP(new ObtieneMontoTramitePagoDirecto(getDataSource()), paramsPagoDirecto);
		logger.debug( resultado.get("pv_monto_o"));
		return (String) resultado.get("pv_monto_o");
	}
	
    protected class ObtieneMontoTramitePagoDirecto extends StoredProcedure {
    	
    	protected ObtieneMontoTramitePagoDirecto(DataSource dataSource) {
    		super(dataSource, "PKG_SINIESTRO.P_OBTIENE_MONTOTRAMITE");
    		declareParameter(new SqlParameter("pv_ntramite_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_monto_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
	@Override
	public List<Map<String, String>> obtieneValidaFacturaMontoPagoAutomatico(HashMap<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ObtieneValidaFacturaMontoPagoAutomatico(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneValidaFacturaMontoPagoAutomatico extends StoredProcedure {
		protected ObtieneValidaFacturaMontoPagoAutomatico(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_SINIESTRO.P_VALIDA_FACTURA_MONTO");
			declareParameter(new SqlParameter("pv_ntramite_i",   OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"NTRAMITE","NFACTURA","PTIMPORT"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String guardaListaFacturaPagoAutomatico(HashMap<String, Object> paramsFacMesaCtrl) throws Exception {
			Map<String, Object> mapResult = ejecutaSP(new GuardaListaFacturaPagoAutomatico(getDataSource()), paramsFacMesaCtrl);
			return (String) mapResult.get("pv_msg_id_o");
	}
	
	protected class GuardaListaFacturaPagoAutomatico extends StoredProcedure {
		protected GuardaListaFacturaPagoAutomatico(DataSource dataSource) {
			super(dataSource, "PKG_SINIESTRO.P_ACTUALIZA_FAC_MSINIEST_PA");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfacori_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void actualizaTurnadoMesaControl(String ntramite) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_ntramite_i", ntramite);
		Map<String,Object> resultadoMap=this.ejecutaSP(new ActualizaTurnadoMesaControl(this.getDataSource()), params);
	}
	
	protected class ActualizaTurnadoMesaControl extends StoredProcedure
	{
		protected ActualizaTurnadoMesaControl(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_UPD_TURNADOTRAMITE");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public List<Map<String, String>> obtieneListadoFacturasxControntrol(HashMap<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ObtieneListadoFacturasxControntrol(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneListadoFacturasxControntrol extends StoredProcedure {
		protected ObtieneListadoFacturasxControntrol(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_SINIESTRO.P_OBTIENE_NFACTURAXCONTROL");
			declareParameter(new SqlParameter("pv_ntramite_i",   OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"NTRAMITE",		"NFACTURA",		"CDTIPSER",		"CDPRESTA", 	"PTIMPORT", 	"CDGARANT", 	"CDCONVAL",
					"DESCPORC", 	"DESCNUME", 	"CDMONEDA", 	"TASACAMB",		"PTIMPORTA", 	"DCTONUEX", 	"FEEGRESO",
					"DIASDEDU"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
    @Override
	public List<Map<String, String>> obtieneConfiguracionLayoutExcel(HashMap<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ObtieneConfiguracionLayoutExcel(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
    protected class ObtieneConfiguracionLayoutExcel extends StoredProcedure {
		protected ObtieneConfiguracionLayoutExcel(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_SINIESTRO.P_GET_CONF_EXCEL");
			declareParameter(new SqlParameter("pv_cdpresta_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cveexcel_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cveconfi_i",   OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CVEFORMATO","VALORMIN","VALORMAX","FORMATDATE","SWOBLIGA"
					
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
    
	@Override
	public List<Map<String, String>> procesaPagoAutomaticoLayout(HashMap<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ProcesaPagoAutomaticoLayout(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}

	protected class ProcesaPagoAutomaticoLayout extends StoredProcedure {
		protected ProcesaPagoAutomaticoLayout(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_SINIESTRO.P_GET_TCONLAYSIN");
			String[] cols = new String[]{
					"NTRAMITE"
			};
			declareParameter(new SqlParameter("pv_cdpresta_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmconsult_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdusuari_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_tipoproc_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
    
	@Override
	public String existeRegistrosProcesarSISCO() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> resultado = ejecutaSP(new ExisteRegistrosProcesarSISCO(getDataSource()), params);
		logger.debug("Valor de Respuesta ====>"+ resultado.get("pv_procesar_o"));
		String respuesta = resultado.get("pv_procesar_o")+"";
		return respuesta;
	}
	
    protected class ExisteRegistrosProcesarSISCO extends StoredProcedure {
    	
    	protected ExisteRegistrosProcesarSISCO(DataSource dataSource) {
    		super(dataSource, "PKG_SINIESTRO.P_GET_REGISTROSISCO");
    		declareParameter(new SqlOutParameter("pv_procesar_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }

    @Override
    public String validaPersonaSisaSicaps(HashMap<String, Object> paramPersona) throws Exception {
    	Map<String, Object> resultado = ejecutaSP(new ValidaPersonaSisaSicaps(getDataSource()), paramPersona);
    	logger.debug( resultado.get("pv_cdperson_o"));
    	return (String) resultado.get("pv_cdperson_o");
    }

    protected class ValidaPersonaSisaSicaps extends StoredProcedure {
    	protected ValidaPersonaSisaSicaps(DataSource dataSource) {    		
    		super(dataSource, "PKG_SINIESTRO.P_GET_VALIDAPERSONA");
    		declareParameter(new SqlParameter("pv_cdideext_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_cdperson_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
	@Override
	public List<Map<String, String>> obtieneInfCoberturaInfonavit(HashMap<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ObtieneInfCoberturaInfonavit(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneInfCoberturaInfonavit extends StoredProcedure {
		protected ObtieneInfCoberturaInfonavit(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_SINIESTRO.P_GET_NUM_CONSULTAS_AFI");
			declareParameter(new SqlParameter("pv_cdunieco_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdgarant_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdconval_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsinies_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swfonsin_i",   OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"NO_CONSULTAS", "MONTO", "IMPGASTADOCOB", "OTVALOR04","OTVALOR07", "OTVALOR15","OTVALOR16"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> obtieneInfCausaSiniestroProducto(HashMap<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ObtieneInfCausaSiniestroProducto(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneInfCausaSiniestroProducto extends StoredProcedure {
		protected ObtieneInfCausaSiniestroProducto(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_SINIESTRO.P_GET_SINIESTRO_GRAL");
			declareParameter(new SqlParameter("pv_cdramo_i",   	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i",  OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_causa_i",   	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_codigo_i",   	OracleTypes.VARCHAR));
			
			String[] cols = new String[]{
					"REQVALIDACION",	"REQCONSULTAS"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
    @Override
    public String validaFeocurreAsegurado(HashMap<String, Object> paramPersona) throws Exception {
    	Map<String, Object> resultado = ejecutaSP(new ValidaFeocurreAsegurado(getDataSource()), paramPersona);
    	logger.debug("Valor de respuesta ==> "+resultado.get("pv_extsinie_o"));
    	return (String) resultado.get("pv_extsinie_o");
    }

    protected class ValidaFeocurreAsegurado extends StoredProcedure {
    	protected ValidaFeocurreAsegurado(DataSource dataSource) {    		
    		super(dataSource, "PKG_SINIESTRO.P_VALIDA_FEOCURRE_ASEG");
    		declareParameter(new SqlParameter("pv_feocurre_i",   OracleTypes.DATE));
    		declareParameter(new SqlParameter("pv_cdperson_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_extsinie_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
	@Override
	public Map<String, Object> actualizaDatosGeneralesConceptos(Map<String, Object> params) throws Exception {
		return ejecutaSP(new ActualizaDatosGeneralesConceptos(this.getDataSource()), params);
	}
	
	protected class ActualizaDatosGeneralesConceptos extends StoredProcedure {
		protected ActualizaDatosGeneralesConceptos(DataSource dataSource) {
			super(dataSource, "PKG_SINIESTRO.P_UPD_CONCEPTASEGURADO");
            declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsuplem_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_aaapertu_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsinies_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdgarant_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdconval_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String validaExisteCodigoConcepto(HashMap<String, Object> paramExiste) throws Exception {
		Map<String, Object> resultado = ejecutaSP(new ValidaExisteCodigoConcepto(getDataSource()), paramExiste);
		logger.debug( resultado.get("pv_existe_o"));
		return (String) resultado.get("pv_existe_o");
	}
	
    protected class ValidaExisteCodigoConcepto extends StoredProcedure {
    	
    	protected ValidaExisteCodigoConcepto(DataSource dataSource) {
    		super(dataSource, "PKG_SINIESTRO.P_EXISTE_CPTUBHCPC");
    		declareParameter(new SqlParameter("pv_idconcep_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_descripc_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_existe_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
	@Override
	public List<Map<String, String>> obtieneInfAseguradoCobSubCoberturas(HashMap<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ObtieneInfAseguradoCobSubCoberturas(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneInfAseguradoCobSubCoberturas extends StoredProcedure {
		protected ObtieneInfAseguradoCobSubCoberturas(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_SINIESTRO.P_OBTIENE_CDRAMO_NOVA");
			declareParameter(new SqlParameter("pv_cdperson_i",   	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_feocurre_i",  	OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_cobertura_i",   	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_subcobertura_i",  OracleTypes.VARCHAR));
			
			String[] cols = new String[]{
					"CDTIPSIT",		"CDRAMO",	"COBERTURA",		"SUBCOBERTURA"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String actualizaRegistroTimpsini(HashMap<String, Object> datosActualizacion) throws Exception {
		Map<String, Object> mapResult = ejecutaSP(new ActualizaRegistroTimpsini(this.getDataSource()), datosActualizacion);
		return (String) mapResult.get("pv_msg_id_o");
	}
	
	protected class ActualizaRegistroTimpsini extends StoredProcedure
	{
		protected ActualizaRegistroTimpsini(DataSource dataSource)
		{
			
			super(dataSource, "PKG_SINIESTRO.P_UPD_TIMPSINI");
			declareParameter(new SqlParameter("pv_accion_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_aaapertu_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsinies_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ptimport_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ptiva_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String, Object> actualizaDatosGeneralesCopago(Map<String, Object> params) throws Exception {
		return ejecutaSP(new ActualizaDatosGeneralesCopago(this.getDataSource()), params);
	}
	
	protected class ActualizaDatosGeneralesCopago extends StoredProcedure {
		protected ActualizaDatosGeneralesCopago(DataSource dataSource) {
			super(dataSource, "PKG_SINIESTRO.P_MOV_TCOPASIN");
            declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsuplem_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsituac_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsinies_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_ntramite_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nfactura_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdgarant_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdconval_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_deducible_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_copago_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmautser_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_swautori_i" , OracleTypes.VARCHAR));            
            declareParameter(new SqlParameter("pv_feingreso_i" , OracleTypes.DATE));	//(EGS)
            declareParameter(new SqlParameter("pv_feegreso_i" , OracleTypes.DATE));	//(EGS)
            declareParameter(new SqlParameter("pv_cveEvento_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cveAlta_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_accion_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
    
	@Override
	public String actualizaDeducibleCopagoConceptos(HashMap<String, Object> datosActualizacion) throws Exception {
		Map<String, Object> mapResult = ejecutaSP(new ActualizaDeducibleCopagoConceptos(this.getDataSource()), datosActualizacion);
		return (String) mapResult.get("pv_msg_id_o");
	}
	
	protected class ActualizaDeducibleCopagoConceptos extends StoredProcedure
	{
		protected ActualizaDeducibleCopagoConceptos(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_UPD_COPAGODEDCONCEP");
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_aaapertu_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_msinies_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdgarant_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdconval_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdconcep_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_idconcep_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ptprecio_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cantidad_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_deducible_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_copago_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> obtieneListaDatosAutEspecial(HashMap<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ObtieneListaDatosAutEspecial(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneListaDatosAutEspecial extends StoredProcedure {
		protected ObtieneListaDatosAutEspecial(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_SINIESTRO.P_LISTA_AUTESPECIAL_TRAMITE");
			declareParameter(new SqlParameter("pv_cdramo_i",   	 OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_tipoPago_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ntramite_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i",   OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CDUNIECO",			"DESCDUNIECO",			"CDRAMO",			"DSRAMO",	
					"DSTATUS",			"ESTADO",				"NMPOLIZA",			"NMSUPLEM",
					"NMSITUAC",			"NMSINIES",				"CDPERSON",			"DESCDPERSON",
					"NTRAMITE",			"TIPOPAGO",				"DESTIPOPAGO", 		"NFACTURA",
					"CDTIPSIT",			"NUMPOLIZA", 			"VIGENCIA", 		"NMSINIES",
					"NMAUTESP",			"VALRANGO",				"VALCOBER",			"CDGARANT",
					"COMMENTS"
					,"VALEXCEDE"	//(EGS)
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	public String actualizaValImpuestoProv(HashMap<String, Object> datosActualizacion) throws Exception {
		Map<String, Object> mapResult = ejecutaSP(new ActualizaValImpuestoProv(this.getDataSource()), datosActualizacion);
		return (String) mapResult.get("pv_msg_id_o");
	}
	
	protected class ActualizaValImpuestoProv extends StoredProcedure
	{
		protected ActualizaValImpuestoProv(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_UPD_IMPUESTOPROV");
			declareParameter(new SqlParameter("pv_accion_i", 	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ntramite_i", 	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i", 	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swisr_i", 	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swice_i", 	OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",	OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String obtieneValidacionAsegurado(String cdperson, Date feocurre, String nmpoliza) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdperson_i", cdperson);
		params.put("pv_feocurre_i", feocurre);
		params.put("pv_nmpoliza_i", nmpoliza);
		
		Map<String, Object> resultado = ejecutaSP(new ObtieneValidacionAsegurado(getDataSource()), params);
		logger.debug( resultado.get("pv_status_o"));
		return (String) resultado.get("pv_status_o");
	}
	
    protected class ObtieneValidacionAsegurado extends StoredProcedure {
    	
    	protected ObtieneValidacionAsegurado(DataSource dataSource) {
    		
    		super(dataSource, "PKG_SINIESTRO.P_VALIDA_STATUS_ASEG");
    		declareParameter(new SqlParameter("pv_cdperson_i",   OracleTypes.VARCHAR));		// Id. del proveedor
    		declareParameter(new SqlParameter("pv_feocurre_i", OracleTypes.DATE));		// Id. del concepto
    		declareParameter(new SqlParameter("pv_nmpoliza_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_status_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
	@Override
	public List<Map<String,String>> obtieneInfImporteAsegTramiteAseg(Map<String, String> params) throws Exception
	{
		Map<String, Object> mapResult = ejecutaSP(new ObtieneInfImporteAsegTramiteAseg(this.getDataSource()), params);
		return (List<Map<String,String>>) mapResult.get("pv_registro_o");
	}
	
	protected class ObtieneInfImporteAsegTramiteAseg extends StoredProcedure
	{
		protected ObtieneInfImporteAsegTramiteAseg(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_VALIDA_IMP_ASEGSIN");
			declareParameter(new SqlParameter("pv_tipopago_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CONTRARECIBO",		"FACTURA",		"SINIESTRO",		"IMPORTE"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	public String actualizaValoresMCSiniestros(HashMap<String, Object> datosActualizacion) throws Exception {
		Map<String, Object> mapResult = ejecutaSP(new ActualizaValoresMCSiniestros(this.getDataSource()), datosActualizacion);
		return (String) mapResult.get("pv_msg_id_o");
	}
	
	protected class ActualizaValoresMCSiniestros extends StoredProcedure
	{
		protected ActualizaValoresMCSiniestros(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_MOV_ACTMCSINIESTROS");
			declareParameter(new SqlParameter("pv_accion_i", 	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ntramite_i", 	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdsubram_i", 	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtiptra_i", 	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i", 	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i", 	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor04_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor05_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor16_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor17_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor20_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor22_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor26_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",	OracleTypes.VARCHAR));
			compile();
		}
	}
	@Override
	public List<Map<String, String>> obtenerAseguradosxTworksin(Map<String, String> params) throws Exception
	{
		Map<String, Object> mapResult = ejecutaSP(new ObtenerAseguradosxTworksin(this.getDataSource()), params);
		return (List<Map<String,String>>) mapResult.get("pv_registro_o");
	}
	
	protected class ObtenerAseguradosxTworksin extends StoredProcedure
	{
		protected ObtenerAseguradosxTworksin(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_OBTIENE_ASEGXTWORKSIN");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"NTRAMITE"
					,"CDUNIECO"
					,"CDRAMO"
					,"ESTADO"
					,"NMPOLIZA"
					,"NMSOLICI"
					,"NMSUPLEM"
					,"NMSITUAC"
					,"CDTIPSIT"
					,"CDPERSON"
					,"DESCDPERSON"
					,"FEOCURRE"
					,"NFACTURA"
					,"REQAUTES"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}


	@Override
	public void eliminaFaltantesAsegurados() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> mapResult = ejecutaSP(new EliminaFaltantesAsegurados(getDataSource()), params);
	}
	
	protected class EliminaFaltantesAsegurados extends StoredProcedure {
		protected EliminaFaltantesAsegurados(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_ELIMINA_ASEGFALTANTES");
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void eliminacionAseguradoEspecifico(String ntramite, String nfactura, String cdperson, Date feocurre) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_ntramite_i", ntramite);
		params.put("pv_nfactura_i", nfactura);
		params.put("pv_cdperson_i",	cdperson);
		params.put("pv_feocurre_i", feocurre);
		Map<String,Object> resultadoMap=this.ejecutaSP(new EliminacionAseguradoEspecifico(this.getDataSource()), params);
	}
	protected class EliminacionAseguradoEspecifico extends StoredProcedure {
		protected EliminacionAseguradoEspecifico(DataSource dataSource)
		{
			super(dataSource, "PKG_SINIESTRO.P_BORRA_ASEGESPECIFICO");
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_feocurre_i", OracleTypes.DATE));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}


    @Override
    public List<Map<String,String>>cargaICDExcluidosAsegurados(Map<String,String> params) throws Exception
    {
        Map<String, Object> mapResult = ejecutaSP(new CargaICDExcluidosAsegurados(this.getDataSource()), params);
        return (List<Map<String,String>>) mapResult.get("pv_registro_o");
    }
    
    protected class CargaICDExcluidosAsegurados extends StoredProcedure
    {
        protected CargaICDExcluidosAsegurados(DataSource dataSource)
        {
            super(dataSource, "PKG_SINIESTRO.P_GET_ICD_EXCLUSION");
            declareParameter(new SqlParameter("pv_cdunieco_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i"     , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado_i"     , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsuplem_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsituac_i"   , OracleTypes.VARCHAR));
            String[] cols = new String[]{
                    "CLAUSULA",         "DESCLAUSULA",         "ICD",         "DESCICD"
            };
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public List<GenericVO> obtieneListadoTipoEvento(String cdramo, String cdtipsit, String cdgarant, String reporte) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pv_cdramo_i",   cdramo); 
        params.put("pv_cdtipsit_i", cdtipsit);
        params.put("pv_cdgarant_i", cdgarant);
        params.put("pv_idReporte_i", reporte);
        
        Map<String, Object> mapResult = ejecutaSP(new ObtieneListadoTipoEvento(getDataSource()), params);
        return (List<GenericVO>) mapResult.get("pv_registro_o");
    }
    
    protected class ObtieneListadoTipoEvento extends StoredProcedure
    {
        protected ObtieneListadoTipoEvento(DataSource dataSource)
        {
            super(dataSource, "PKG_PRESINIESTRO.P_LISTA_TIPO_EVENTO");
            declareParameter(new SqlParameter("pv_cdramo_i"      , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdtipsit_i"    , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdgarant_i"    , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_idReporte_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosListaSubcobertura()));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public List<Map<String,String>> obtenerDatosValTipoEventoAlta(Map<String, String> params) throws Exception
    {
        Map<String, Object> mapResult = ejecutaSP(new ObtenerDatosValTipoEventoAlta(this.getDataSource()), params);
        return (List<Map<String,String>>) mapResult.get("pv_registro_o");
    }
    
    protected class ObtenerDatosValTipoEventoAlta extends StoredProcedure
    {
        protected ObtenerDatosValTipoEventoAlta(DataSource dataSource)
        {
            super(dataSource, "PKG_SINIESTRO.P_OBT_VALTIPOEVENTOALTA");
            declareParameter(new SqlParameter("pv_cdramo_i"      , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdtipsit_i"    , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdgarant_i"    , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdconval_i"    , OracleTypes.VARCHAR));
            String[] cols = new String[]{
                    "FLAGTIPEVE","FLAGTIPALT"
            };
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public List<GenericVO> obtieneListadoValidacionesGrales() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> mapResult = ejecutaSP(new ObtieneListadoValidacionesGrales(getDataSource()), params);
        return (List<GenericVO>) mapResult.get("pv_registro_o");
    }
    
    protected class ObtieneListadoValidacionesGrales extends StoredProcedure
    {
        protected ObtieneListadoValidacionesGrales(DataSource dataSource)
        {
            super(dataSource, "PKG_SINIESTRO.P_GET_VALIDACION_TOTALES");
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosListaSubcobertura()));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
        }
    }
	
	@Override
	public String validaProveedorPD(String ntramite) throws Exception{ //(EGS) validar proveedor unico en siniestro pago directo
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("PV_NTRAMITE_I", ntramite);
		Map<String, Object> resultado = ejecutaSP(new ConsultaProveedorPagoDirecto(getDataSource()),params);
		logger.debug(resultado.get("PV_N_CDPRESTA_O"));
		return (String)resultado.get("PV_N_CDPRESTA_O");
	}
	
	protected class ConsultaProveedorPagoDirecto extends StoredProcedure{
		
		protected ConsultaProveedorPagoDirecto(DataSource dataSource){
			super(dataSource, "PKG_SINIESTRO.P_VALIDA_CDPRESTA_PD");
			declareParameter(new SqlParameter("PV_NTRAMITE_I", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("PV_N_CDPRESTA_O", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public String actualizarReqautes(String reqautes, String ntramite, String nfactura, String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, String nmsituac, String aaapertu, String status, String nmsinies) throws Exception {	// (EGS)
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("PV_REQAUTES_I", reqautes);
		params.put("PV_NTRAMITE_I", ntramite);
		params.put("PV_NFACTURA_I", nfactura);
		params.put("PV_CDUNIECO_I", cdunieco);
		params.put("PV_CDRAMO_I", cdramo);
		params.put("PV_ESTADO_I", estado);
		params.put("PV_NMPOLIZA_I", nmpoliza);
		params.put("PV_NMSUPLEM_I", nmsuplem);
		params.put("PV_NMSITUAC_I", nmsituac);
		params.put("PV_AAAPERTU_I", aaapertu);
		params.put("PV_STATUS_I", status);
		params.put("PV_NMSINIES_I", nmsinies);
		Map<String,Object> resultado = ejecutaSP(new ActualizaReqautes(getDataSource()),params);
		logger.debug(resultado.get("PV_MSG_ID_O"));
		return (String)resultado.get("PV_MSG_ID_O");
	}
	
	protected class ActualizaReqautes extends StoredProcedure{

		protected ActualizaReqautes(DataSource dataSource) {
			super(dataSource, "PKG_SINIESTRO.P_ACTUALIZA_REQAUTES");
			declareParameter(new SqlParameter("PV_REQAUTES_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_NTRAMITE_I", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("PV_NFACTURA_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_CDUNIECO_I", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("PV_ESTADO_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_NMPOLIZA_I", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("PV_NMSUPLEM_I", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("PV_NMSITUAC_I", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("PV_AAAPERTU_I", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("PV_STATUS_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_NMSINIES_I", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
			compile();
		}
		
	}

	@Override
	public String validaAutEspLimMedi(String nmautesp, String ntramite, String nfactura, String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, String nmsituac, String nmsinies) throws Exception {	//(EGS)
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("PV_NMAUTESP_I", nmautesp);
		params.put("PV_NTRAMITE_I", ntramite);
		params.put("PV_NFACTURA_I", nfactura);
		params.put("PV_CDUNIECO_I", cdunieco);
		params.put("PV_CDRAMO_I", cdramo);
		params.put("PV_ESTADO_I", estado);
		params.put("PV_NMPOLIZA_I", nmpoliza);
		params.put("PV_NMSUPLEM_I", nmsuplem);
		params.put("PV_NMSITUAC_I", nmsituac);
		params.put("PV_NMSINIES_I", nmsinies);
		Map<String,Object> resultado = ejecutaSP(new ValidaAutEspLimMed(getDataSource()),params);
		logger.debug(resultado.get("PV_AUTORIZA_O"));
		return resultado.get("PV_AUTORIZA_O").toString();
	}
	
	protected class ValidaAutEspLimMed extends StoredProcedure{

		protected ValidaAutEspLimMed(DataSource dataSource) {
			super(dataSource, "PKG_SINIESTRO.P_VALIDA_AUTESPLIMEDI");
			declareParameter(new SqlParameter("PV_NMAUTESP_I", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("PV_NTRAMITE_I", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("PV_NFACTURA_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_CDUNIECO_I", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("PV_ESTADO_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_NMPOLIZA_I", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("PV_NMSUPLEM_I", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("PV_NMSITUAC_I", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("PV_NMSINIES_I", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("PV_AUTORIZA_O", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
			compile();
		}
		
	}
	
	@Override
	public List<Map<String, String>> obtieneListaDatosRenovaSiniestralidad(String pv_CdUniEco_i 
			,String pv_CdRamo_i  
			,String pv_nmpoliza_i
			,String pv_cdperson  
			,String pv_nmsinies
			,String pv_fecdesde  
			,String pv_fechasta  
			,String pv_start_i
			,String pv_limit_i 
			,String pv_ntramite_i) throws Exception {
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		
		logger.debug("params: {} "+params);
  
		params.put("pv_CdUniEco_i" , pv_CdUniEco_i);
		params.put("pv_CdRamo_i"   , pv_CdRamo_i  );
		params.put("pv_nmpoliza_i" , pv_nmpoliza_i);
		params.put("pv_cdperson"   , pv_cdperson  );
		params.put("pv_nmsinies"   , pv_nmsinies);
		params.put("pv_fecdesde"   , Utils.parse(pv_fecdesde));
		params.put("pv_fechasta"   , Utils.parse(pv_fechasta));
		params.put("pv_start_i"    , pv_start_i);
		params.put("pv_limit_i"    , pv_limit_i); 
		params.put("pv_ntramite_i" , pv_ntramite_i);
        logger.debug("params:"+params);
        
		
		Map<String, Object> result = ejecutaSP(new ListaRenovaSiniestralidad(this.getDataSource()), params);
		logger.debug("result: {}"+result);

		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ListaRenovaSiniestralidad extends StoredProcedure {
		protected ListaRenovaSiniestralidad(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_RESERVAS_SINI.P_SHOW_DETALLE_RESERVAS");
			declareParameter(new SqlParameter("pv_CdUniEco_i",   	 OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_CdRamo_i",   	 OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsinies",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fecdesde",   OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_fechasta",   OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_start_i",   OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_limit_i",   OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_ntramite_i",   OracleTypes.VARCHAR));
			String[] cols = new String[]{
					    "TIPO_PAGO"
					   ,"CDUNIECO"
					   ,"DSUNIECO"
					   ,"CDRAMO"
					   ,"DSRAMO"
					   ,"ESTADO"
					   ,"NMPOLIZA"
					   ,"POLIZA"
					   ,"FECINIVIG"
					   ,"FECFINVIG"
					   ,"NMAUTSER"
					   ,"AAAPERTU"
					   ,"NMSINIES"
					   ,"FECHA_OCURRENCIA"
					   ,"CDICD"
					   ,"DESC_ICD"
					   ,"CDCAUSA"
					   ,"CDPERSON"
					   ,"NOMBRE_ASEGURADO"
					   ,"FENACIMI"
					   ,"EDAD"
					   ,"SEXO"
					   ,"NTRAMITE"
					   ,"MONTO_RESERVADO"
					   ,"MONTO_APROBADO"
					   ,"MONTO_PAGADO"	 
			};
			//declareParameter(new SqlOutParameter("pv_cant_regis"   , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> obtieneListaTopIcd(String pv_CdUniEco_i 
			,String pv_CdRamo_i  
			,String pv_nmpoliza_i
			,String pv_cdperson  
			,String pv_nmsinies
			,String pv_fecdesde  
			,String pv_fechasta  
			,String pv_top) throws Exception {
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		
		
		params.put("pv_CdUniEco_i" , pv_CdUniEco_i);
		params.put("pv_CdRamo_i"   , pv_CdRamo_i  );
		params.put("pv_nmpoliza_i" , pv_nmpoliza_i);
		params.put("pv_cdperson"   , pv_cdperson  );
		params.put("pv_nmsinies"   , pv_nmsinies);
		params.put("pv_fecdesde"   , Utils.parse(pv_fecdesde));
		params.put("pv_fechasta"   , Utils.parse(pv_fechasta));
		params.put("pv_top"    , "5"); 
        logger.debug("params:"+params);
        
		
		Map<String, Object> result = ejecutaSP(new ObtieneListaTopIcd(this.getDataSource()), params);
		logger.debug("result: {}"+result);

		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneListaTopIcd extends StoredProcedure {
		protected ObtieneListaTopIcd(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_RESERVAS_SINI.P_TOPS_ICDS");
			declareParameter(new SqlParameter("pv_CdUniEco_i",   	 OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_CdRamo_i",   	 OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsinies",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fecdesde",   OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_fechasta",   OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_top",   OracleTypes.VARCHAR));
			
			String[] cols = new String[]{
					"CDICD"	        	
					,"DESC_ICD"	        
					,"MONTO_RESERVADO"		    
					,"MONTO_APROBADO"	        
					,"MONTO_PAGADO"		    
					
			};
			//declareParameter(new SqlOutParameter("pv_cant_regis"   , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
    
	@Override
	public List<Map<String, String>> obtieneListaReservas(String pv_CdUniEco_i 
			,String pv_CdRamo_i  
			,String pv_nmpoliza_i
			,String pv_cdperson  
			,String pv_nmsinies
			,String pv_fecdesde  
			,String pv_fechasta) throws Exception {
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		
		
		params.put("pv_CdUniEco_i" , pv_CdUniEco_i);
		params.put("pv_CdRamo_i"   , pv_CdRamo_i  );
		params.put("pv_nmpoliza_i" , pv_nmpoliza_i);
		params.put("pv_cdperson"   , pv_cdperson  );
		params.put("pv_nmsinies"   , pv_nmsinies);
		params.put("pv_fecdesde"   , Utils.parse(pv_fecdesde));
		params.put("pv_fechasta"   , Utils.parse(pv_fechasta));
        logger.debug("params:"+params);
        
		
		Map<String, Object> result = ejecutaSP(new ObtieneListaReservas(this.getDataSource()), params);
		logger.debug("result: {}"+result);

		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneListaReservas extends StoredProcedure {
		protected ObtieneListaReservas(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_RESERVAS_SINI.P_SHOW_RESERVAS_TIPPAG");
			declareParameter(new SqlParameter("pv_CdUniEco_i",   	 OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_CdRamo_i",   	 OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsinies",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fecdesde",   OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_fechasta",   OracleTypes.DATE));
			String[] cols = new String[]{
					"TIPO_PAGO"	        
					,"MONTO_RESERVADO"		    
					,"MONTO_APROBADO"	        
					,"MONTO_PAGADO"		    
					
			};
			//declareParameter(new SqlOutParameter("pv_cant_regis"   , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> obtieneListaReservasSolo(String pv_CdUniEco_i 
			,String pv_CdRamo_i  
			,String pv_nmpoliza_i
			,String pv_cdperson  
			,String pv_nmsinies
			,String pv_fecdesde  
			,String pv_fechasta) throws Exception {
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		
		
		params.put("pv_CdUniEco_i" , pv_CdUniEco_i);
		params.put("pv_CdRamo_i"   , pv_CdRamo_i  );
		params.put("pv_nmpoliza_i" , pv_nmpoliza_i);
		params.put("pv_cdperson"   , pv_cdperson  );
		params.put("pv_nmsinies"   , pv_nmsinies);
		params.put("pv_fecdesde"   , Utils.parse(pv_fecdesde));
		params.put("pv_fechasta"   , Utils.parse(pv_fechasta));
        logger.debug("params:"+params);
        
		
		Map<String, Object> result = ejecutaSP(new ObtieneListaReservasSolo(this.getDataSource()), params);
		logger.debug("result: {}"+result);
		List<Map<String,String>> ret=(List<Map<String,String>>)result.get("pv_registro_o");
		
		if(ret.size()<=0){
			throw new ApplicationException("No hay datos");
		}
		List<Map<String,String>> dat=new ArrayList<Map<String,String>>();
		Map<String, String> map = ret.get(0);
		Map<String, String> map2= new HashMap<String, String>();
		map2.put("TITULO", "Monto Reservado");
		map2.put("DAT1", map.get("MONTO_RESERVADO"));
		
		dat.add(map2);
		map2=new HashMap<String, String>();
		
		map2.put("TITULO", "Monto Aprobado");
		map2.put("DAT1", map.get("MONTO_APROBADO"));
		dat.add(map2);
		map2=new HashMap<String, String>();
		
		map2.put("TITULO", "Monto Pagado");
		map2.put("DAT1", map.get("MONTO_PAGADO"));
		dat.add(map2);
		return dat;
	}
	
	protected class ObtieneListaReservasSolo extends StoredProcedure {
		protected ObtieneListaReservasSolo(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_RESERVAS_SINI.P_SHOW_RESERVAS");
			declareParameter(new SqlParameter("pv_CdUniEco_i",   	 OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_CdRamo_i",   	 OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsinies",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fecdesde",   OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_fechasta",   OracleTypes.DATE));
			String[] cols = new String[]{
					"MONTO_RESERVADO"		    
					,"MONTO_APROBADO"	        
					,"MONTO_PAGADO"		    
					
			};
			//declareParameter(new SqlOutParameter("pv_cant_regis"   , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
    public List<GenericVO> obtieneListadoCPTUnico(String cdicd)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdicd_i", cdicd);
		
		Map<String, Object> mapResult = ejecutaSP(new ObtieneListadoCPTUnico(getDataSource()), params);
		return (List<GenericVO>) mapResult.get("pv_registro_o");
	}
	
	protected class ObtieneListadoCPTUnico extends StoredProcedure
	{
		protected ObtieneListadoCPTUnico(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.P_CATALOGO_ICDXCPT"); // Nuevo PL a invocar (EGS)
			declareParameter(new SqlParameter("pv_cdicd_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosGenericos()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	protected class DatosGenericos  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	GenericVO consulta = new GenericVO();
        	consulta.setKey(rs.getString("OTCLAVE1"));
        	consulta.setValue(rs.getString("OTVALOR01"));
            return consulta;
        }
    }
	
	@Override
	public String obtieneMesesTiempoEsperaICDCPT(String cdramo, String cdtipsit, String cdicd, String dsplan)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdramo_i", cdramo);
		params.put("pv_cdtipsit_i", cdtipsit);
		params.put("pv_cdicd_i", cdicd);
		params.put("pv_dsplan_i", dsplan);
		Map<String, Object> resultado = ejecutaSP(new ObtieneMesesTiempoEsperaICDCPT(getDataSource()), params);
		return (String) resultado.get("pv_registro_o");
	}
	
    protected class ObtieneMesesTiempoEsperaICDCPT extends StoredProcedure {
    	protected ObtieneMesesTiempoEsperaICDCPT(DataSource dataSource) {
    		super(dataSource, "PKG_PRESINIESTRO.P_OBTIENE_MESES_TESPERAICD");
    		declareParameter(new SqlParameter("pv_cdramo_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdicd_i",    OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_dsplan_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    public List<GenericVO> obtieneListadoTipoMedicos(String cdicd, String cpts)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdicd_i", cdicd);
		params.put("pv_cpts_i", cpts);
		
		Map<String, Object> mapResult = ejecutaSP(new ObtieneListadoTipoMedicos(getDataSource()), params);
		return (List<GenericVO>) mapResult.get("pv_registro_o");
	}
	
	protected class ObtieneListadoTipoMedicos extends StoredProcedure{
		protected ObtieneListadoTipoMedicos(DataSource dataSource)
		{
			super(dataSource, "P_GET_EQUIPO_MEDICO");
			declareParameter(new SqlParameter("pv_cdicd_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cpts_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosGenericos()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String obtieneImporteArancelGNP(String cdpresta,String cpt) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdpresta_i", cdpresta);
		params.put("pv_cpt_i", cpt);
		Map<String, Object> resultado = ejecutaSP(new ObtieneImporteArancelGNP(getDataSource()), params);
		logger.debug( resultado.get("pv_registro_o"));
		return (String) resultado.get("pv_registro_o");
	}
	
    protected class ObtieneImporteArancelGNP extends StoredProcedure {
    	protected ObtieneImporteArancelGNP(DataSource dataSource) {
    		super(dataSource, "PKG_SINIESTRO.P_GET_ARANCELGNP");
    		declareParameter(new SqlParameter("pv_cdpresta_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cpt_i",        OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    @Override
	public List<Map<String, String>> obtieneListaProveedores(String pv_cdpresta_i,String pv_idespecialidad_i,String pv_tipoProveedor_i,String pv_idZonaHosp_i) throws Exception {
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("pv_cdpresta_i", StringUtils.isBlank( pv_cdpresta_i)?null:pv_cdpresta_i);
		params.put("pv_idespecialidad_i", StringUtils.isBlank( pv_idespecialidad_i)?null:pv_idespecialidad_i);
		params.put("pv_tipoProveedor_i", StringUtils.isBlank(pv_tipoProveedor_i)?null:pv_tipoProveedor_i);
		params.put("pv_idZonaHosp_i", StringUtils.isBlank(pv_idZonaHosp_i)?null:pv_idZonaHosp_i);
		logger.debug("---"+params);
		Map<String, Object> resultado = ejecutaSP(new ObtieneListaProveedores(getDataSource()), params);
		logger.debug( resultado.get("pv_registro_o"));
		return (List<Map<String, String>>) resultado.get("pv_registro_o");
	}
	
    protected class ObtieneListaProveedores extends StoredProcedure {
    	protected ObtieneListaProveedores(DataSource dataSource) {
    		super(dataSource, "PKG_SINIESTRO.P_LISTA_PROVEEDORES");
    		declareParameter(new SqlParameter("pv_cdpresta_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_idespecialidad_i",        OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_tipoProveedor_i",        OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_idZonaHosp_i",        OracleTypes.VARCHAR));
    		
    		String[] cols = new String[]{
					"CDPRESTA" , "CDPERSON","PROVEEDOR","CVEESPECIALIDAD",
					"ESPECIALIDAD","CVETIPO","TIPO","CVEZONA","ZONA","CIRCULO"
			};
    		declareParameter(new SqlOutParameter("pv_registro_o",OracleTypes.CURSOR , new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
	@Override
	public List<GenericVO> obtieneListadoTiposProveedores() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> mapResult = ejecutaSP(new ObtieneListadoTiposProveedores(getDataSource()), params);
		return (List<GenericVO>) mapResult.get("pv_registro_o");
	}
	
	protected class ObtieneListadoTiposProveedores extends StoredProcedure
	{
		protected ObtieneListadoTiposProveedores(DataSource dataSource)
		{
			super(dataSource, "PKG_PRESINIESTRO.P_GET_TIPOPROVEEDOR");
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DatosListaSubcobertura()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> obtieneListadoDetalleSiniestro(String pv_CdUniEco_i
																,String pv_CdRamo_i  
																,String pv_nmpoliza_i
																,String pv_cdperson  
																,String pv_ntramite_i
																,String pv_nmsinies  
																,String pv_fecdesde  
																,String pv_fechasta ) throws Exception {
		
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		
		params.put("pv_cdunieco_i" , pv_CdUniEco_i);
		params.put("pv_cdramo_i"   , pv_CdRamo_i);
		params.put("pv_nmpoliza_i" , pv_nmpoliza_i);
		params.put("pv_cdperson"   , pv_cdperson);
		params.put("pv_ntramite_i" , pv_ntramite_i);
		params.put("pv_nmsinies"   , pv_nmsinies);
		params.put("pv_fecdesde"   , Utils.parse(pv_fecdesde));
		params.put("pv_fechasta"   , Utils.parse(pv_fechasta));
		
		Map<String, Object> result = ejecutaSP(new obtieneListadoDetalleSiniestros(getDataSource()), params);
		logger.debug("result: {}"+(List<Map<String,String>>)result.get("pv_registro_o"));

		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	protected class obtieneListadoDetalleSiniestros extends StoredProcedure {

		protected obtieneListadoDetalleSiniestros(DataSource dataSource) {
			super(dataSource, "PKG_RESERVAS_SINI.P_SHOW_DETALLE_PAGO");
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsinies", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fecdesde", OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_fechasta", OracleTypes.DATE));
			String[] cols = new String[]{
					 "NTRAMITE"
					,"CDPERSON"
					,"ASEGURADO"
					,"CDPRESTA"
					,"PROVEEDOR"
					,"NFACTURA"
					,"CDGARANT"
					,"DSGARANT"
					,"CDCONVAL"
					,"DSCONVAL"
					,"MONTO_FACTURA"
					,"IVA"
					,"IVA_RETENIDO"
					,"ISLR"
					,"ICED"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String obtieneMedicoEquipoQuirurgico(String numAutorizacion) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmautser_i", numAutorizacion);
		Map<String, Object> resultado = ejecutaSP(new ObtieneMedicoEquipoQuirurgico(getDataSource()), params);
		logger.debug( resultado.get("pv_registro_o"));
		return (String) resultado.get("pv_registro_o");
	}
	
    protected class ObtieneMedicoEquipoQuirurgico extends StoredProcedure {
    	protected ObtieneMedicoEquipoQuirurgico(DataSource dataSource) {
    		super(dataSource, "PKG_SINIESTRO.P_GET_EQUIPOQUIRUR");
    		declareParameter(new SqlParameter("pv_nmautser_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
	@Override
	public List<AutorizacionServicioVO>  guardarAutorizacionServicioGNP(Map<String, Object> paramsR)
			throws Exception {
		
		Map<String, Object> mapResult = ejecutaSP(new GuardaAutorizacionServicioGNPSP(getDataSource()), paramsR);
		
		@SuppressWarnings("unchecked")
		List<AutorizacionServicioVO> listaDatosPoliza = (List<AutorizacionServicioVO>)mapResult.get("pv_registro_o");
		return listaDatosPoliza;
	}
	
	protected class GuardaAutorizacionServicioGNPSP extends StoredProcedure {

		protected GuardaAutorizacionServicioGNPSP(DataSource dataSource) {
			super(dataSource, "PKG_PRESINIESTRO.P_GUARDA_MAUTSERV_N");
			declareParameter(new SqlParameter("pv_nmautser_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmautant_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fesolici_i", OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_feautori_i", OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_fevencim_i", OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_feingres_i", OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cduniecs_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdgarant_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdconval_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdprovee_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdmedico_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_mtsumadp_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_porpenal_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_copagofi_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdicd_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdcausa_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_aaapertu_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dstratam_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsobserv_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsnotas_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fesistem_i", OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_cduser_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nombmedi_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_especmed_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_tpautori_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_idaplicaCirHosp_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_idaplicaZona_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swnegoci_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_tiposerv_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_numrecla_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatosGuardardoAutorizacionServicioGNPMapper()));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	protected class DatosGuardardoAutorizacionServicioGNPMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	AutorizacionServicioVO consulta = new AutorizacionServicioVO();
        	consulta.setNmautser(rs.getString("nmautser"));
        	consulta.setNtramite(rs.getString("ntramite"));
            return consulta;
        }
    }

	@Override
	public String obtieneDatosGeneralesICD(String cdunieco, String cdramo, String estado, String nmpoliza, String cdicd, String cdperson) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i"  , cdramo);
		params.put("pv_estado_i"  , estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_icd_i"     , cdicd);
		params.put("pv_cdperson_i", cdperson);
		Map<String, Object> resultado = ejecutaSP(new ObtieneDatosGeneralesICD(getDataSource()), params);
		logger.debug( resultado.get("pv_numrecla"));
		return (String) resultado.get("pv_numrecla");
	}
	
    protected class ObtieneDatosGeneralesICD extends StoredProcedure {
    	protected ObtieneDatosGeneralesICD(DataSource dataSource) {
    		super(dataSource, "p_get_num_recla_gnp");
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdramo_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_estado_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_icd_i",      OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_numrecla", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
	@Override
	public List<Map<String, String>> obtieneListaDatosSumaAseguradaGNP(HashMap<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ObtieneListaDatosSumaAseguradaGNP(this.getDataSource()), params);
		return (List<Map<String,String>>)result.get("pv_registro_o");
	}
	
	protected class ObtieneListaDatosSumaAseguradaGNP extends StoredProcedure {
		protected ObtieneListaDatosSumaAseguradaGNP(DataSource dataSource) {
			// TODO: Terminar cuando este listo el SP
			super(dataSource, "PKG_SINIESTRO.P_OBTINE_IMP_PAGADOXPERSONA2");
			declareParameter(new SqlParameter("pv_cdunieco_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsinref_i",   OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"SUMA_ASEGURADA","RESERVA_DISPONIBLE"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String obtenerValidacionExclusionICD(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem, String nmsituac, String cdicd) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i"  , cdramo);
		params.put("pv_estado_i"  , estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_nmsuplem_i", nmsuplem);
		params.put("pv_nmsituac_i", nmsituac);
		params.put("pv_cdicd_i"     , cdicd);
		Map<String, Object> resultado = ejecutaSP(new ObtenerValidacionExclusionICD(getDataSource()), params);
		logger.debug( resultado.get("pv_respuesta_o"));
		return (String) resultado.get("pv_respuesta_o");
	}
	
    protected class ObtenerValidacionExclusionICD extends StoredProcedure {
    	protected ObtenerValidacionExclusionICD(DataSource dataSource) {
    		super(dataSource, "p_get_excl_icd");
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdramo_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_estado_i",   OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdicd_i",      OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_respuesta_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
}