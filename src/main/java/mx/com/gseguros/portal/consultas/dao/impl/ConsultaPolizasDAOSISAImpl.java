package mx.com.gseguros.portal.consultas.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

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
import mx.com.gseguros.portal.general.model.ClausulaVO;
import mx.com.gseguros.portal.general.model.DetalleReciboVO;
import mx.com.gseguros.portal.general.model.PolizaVO;
import mx.com.gseguros.portal.general.model.ReciboVO;
import oracle.jdbc.OracleTypes;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.object.StoredProcedure;

public class ConsultaPolizasDAOSISAImpl extends AbstractManagerDAO implements ConsultasPolizaDAO {
	
	// Coincidencias del asegurado seg�n criterios: RFC, c�digo de asegurado y
	// nombre.
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PolizaAseguradoVO> obtienePolizasAsegPromotor(String user, String rfc, String cdperson, String nombre) throws Exception {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_user_i",user); //Agrego parametro user: campo cdusurari de la tabla TUSUARIO
		params.put("pv_cdrfc_i", rfc);
		params.put("pv_cdperson_i", cdperson);
		params.put("pv_nombre_i", nombre);
		Map<String, Object> mapResult = ejecutaSP(new ConsultaPolizasAseguradoSP(getDataSource()), params);
		return (List<PolizaAseguradoVO>) mapResult.get("rs");
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PolizaAseguradoVO> obtienePolizasAsegurado(String user, String rfc,
			String cdperson, String nombre) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_user_i",user); //Agrego parametro user: campo cdusurari de la tabla TUSUARIO
		params.put("pv_cdrfc_i", rfc);
		params.put("pv_cdperson_i", cdperson);
		params.put("pv_nombre_i", nombre);
		Map<String, Object> mapResult = ejecutaSP(
				new ConsultaPolizasAseguradoSP(getDataSource()), params);
		return (List<PolizaAseguradoVO>) mapResult.get("rs");
	}

	protected class ConsultaPolizasAseguradoSP extends StoredProcedure {
		protected ConsultaPolizasAseguradoSP(DataSource dataSource) {
			super(dataSource, "P_Get_Polizas_Asegurado");
			declareParameter(new SqlParameter("pv_user_i", OracleTypes.VARCHAR)); //Agrego parametro user: campo cdusurari de la tabla TUSUARIO
			declareParameter(new SqlParameter("pv_cdrfc_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_nombre_i", Types.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_title_o", Types.VARCHAR));
			declareParameter(new SqlReturnResultSet("rs",
					new PolizaAseguradoMapper()));
			compile();
		}
	}

	protected class PolizaAseguradoMapper implements
			RowMapper<PolizaAseguradoVO> {
		public PolizaAseguradoVO mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			PolizaAseguradoVO polizaAsegurado = new PolizaAseguradoVO();
			polizaAsegurado.setCdramo(rs.getString("codigo_ramo"));
			polizaAsegurado.setCdunieco(rs.getString("compania"));
			polizaAsegurado.setDsramo(rs.getString("descripcion_ramo"));
			polizaAsegurado.setDsunieco(rs.getString("descripcion"));
			polizaAsegurado.setEstado(rs.getString("estado"));
			polizaAsegurado.setNmpoliex(rs.getString("nmpoliex"));
			polizaAsegurado.setNmpoliza(rs.getString("nmpoliza"));
			polizaAsegurado.setNombreAsegurado(rs.getString("nombre"));
			// Recupera el iCodPoliza de SISA
			polizaAsegurado.setIcodpoliza(rs.getString("icodpoliza"));
			//Recupera el Origen
			polizaAsegurado.setOrigen(rs.getString("origen"));
			polizaAsegurado.setFeinivigencia(rs.getString("feinivigencia"));
			polizaAsegurado.setFefinvigencia(rs.getString("fefinvigencia"));
			return polizaAsegurado;
		}
	}

	// Hist�rico de la p�liza seleccionada.
	@Override
	public List<SuplementoVO> obtieneHistoricoPoliza(
			PolizaAseguradoVO polizaAsegurado) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmpoliex_i", polizaAsegurado.getIcodpoliza());
		Map<String, Object> mapResult = ejecutaSP(
				new ConsultaHistoricoPolizaSP(getDataSource()), params);
		return (List<SuplementoVO>) mapResult.get("rs");
	}
	
	
	protected class ConsultaHistoricoPolizaSP extends StoredProcedure {
		protected ConsultaHistoricoPolizaSP(DataSource dataSource) {
			super(dataSource, "P_Get_Datos_Suplem");
			declareParameter(new SqlParameter("pv_nmpoliex_i", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_msg_id_o", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_title_o", Types.VARCHAR));
			declareParameter(new SqlReturnResultSet("rs",
					new DatosSuplementoMapper()));
			compile();
		}
	}

	protected class DatosSuplementoMapper implements
			RowMapper<SuplementoVO> {
		public SuplementoVO mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			SuplementoVO datosSuplemento = new SuplementoVO();
			datosSuplemento.setCdunieco(rs.getString("cdunieco"));
			datosSuplemento.setCdramo(rs.getString("cdramo"));
			datosSuplemento.setEstado(rs.getString("estado"));
			datosSuplemento.setNmpoliza(rs.getString("nmpoliza"));
			datosSuplemento.setNmsuplem(rs.getString("nmsuplem"));
			datosSuplemento.setFeinival(rs.getString("feinival"));
			datosSuplemento.setNsuplogi(rs.getString("nsuplogi"));
			datosSuplemento.setFeemisio(rs.getString("feemisio"));
			datosSuplemento.setNlogisus(rs.getString("nlogisus"));
			datosSuplemento.setDstipsup(rs.getString("dstipsup"));
			datosSuplemento.setPtpritot(rs.getString("ptpritot"));
			datosSuplemento.setIcodpoliza(rs.getString("icodpoliza"));
			datosSuplemento.setOrigen(rs.getString("origen"));
			return datosSuplemento;
		}
	}
		
	// Hist�rico de la p�liza seleccionada para SISA.
	@Override
	public List<HistoricoVO> obtieneHistoricoPolizaSISA(
			PolizaAseguradoVO polizaAsegurado) throws Exception {
		System.out.println("Entro aca ***");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmpoliex_i", polizaAsegurado.getIcodpoliza());
		Map<String, Object> mapResult = ejecutaSP(
				new ConsultaHistoricoPolizaSISASP(getDataSource()), params);
		return (List<HistoricoVO>) mapResult.get("rs");
	}

	
	protected class ConsultaHistoricoPolizaSISASP extends StoredProcedure {
		protected ConsultaHistoricoPolizaSISASP(DataSource dataSource) {
			super(dataSource, "P_Get_Datos_Historico");
			declareParameter(new SqlParameter("pv_nmpoliex_i", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_msg_id_o", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_title_o", Types.VARCHAR));
			declareParameter(new SqlReturnResultSet("rs",
					new DatosHistoricoMapper()));
			compile();
		}
	}
	
	protected class DatosHistoricoMapper implements
	RowMapper<HistoricoVO> {
public HistoricoVO mapRow(ResultSet rs, int rowNum)
		throws SQLException {
	HistoricoVO datosHistorico = new HistoricoVO();
	datosHistorico.setCdunieco(rs.getString("cdunieco"));
	datosHistorico.setCdramo(rs.getString("cdramo"));
	datosHistorico.setEstado(rs.getString("estado"));
	datosHistorico.setNmpoliza(rs.getString("nmpoliza"));
	datosHistorico.setNmsuplem(rs.getString("nmsuplem"));
	datosHistorico.setFeinival(rs.getString("feinival"));
	datosHistorico.setFefinval(rs.getString("fefinval"));
	datosHistorico.setNsuplogi(rs.getString("nsuplogi"));
	datosHistorico.setFeemisio(rs.getString("feemisio"));
	datosHistorico.setNlogisus(rs.getString("nlogisus"));
	datosHistorico.setDstipsup(rs.getString("dstipsup"));
	datosHistorico.setPtpritot(rs.getString("ptpritot"));
	datosHistorico.setObserva(rs.getString("observa"));
	datosHistorico.setDsplan(rs.getString("dsplan"));
	datosHistorico.setIcodpoliza(rs.getString("icodpoliza"));	
	datosHistorico.setOrigen(rs.getString("origen"));
	return datosHistorico;
}
}
	
			
	// Datos generales de la p�liza.
	@Override
	public List<PolizaDTO> obtieneDatosPoliza(
			PolizaAseguradoVO polizaAsegurado) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmpoliza_i", polizaAsegurado.getIcodpoliza());
		Map<String, Object> mapResult = ejecutaSP(new ConsultaDatosPolizaSP(
				getDataSource()), params);
		return (List<PolizaDTO>) mapResult.get("rs");
	}

	public class ConsultaDatosPolizaSP extends StoredProcedure {
		protected ConsultaDatosPolizaSP(DataSource dataSource) {
			super(dataSource, "P_Get_Datos_Poliza");
			declareParameter(new SqlParameter("pv_nmpoliza_i", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_msg_id_o", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_title_o", Types.VARCHAR));
			declareParameter(new SqlReturnResultSet("rs",
					new DatosPolizaMapper()));
			compile();
		}
	}

	protected class DatosPolizaMapper implements
			RowMapper<PolizaDTO> {
		public PolizaDTO mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			PolizaDTO datosPoliza = new PolizaDTO();
			datosPoliza.setNmpoliex(rs.getString("nmpoliex"));
			datosPoliza.setNmsolici(rs.getString("nmsolici"));
			datosPoliza.setFeemisio(rs.getString("feemisio"));
			datosPoliza.setFeefecto(rs.getString("feefecto"));
			datosPoliza.setCdmoneda(rs.getString("cdmoneda"));
			datosPoliza.setDsmoneda(rs.getString("dsmoneda"));
			datosPoliza.setOttempot(rs.getString("ottempot"));
			datosPoliza.setDstempot(rs.getString("dstempot"));
			datosPoliza.setFeproren(rs.getString("feproren"));
			datosPoliza.setFevencim(rs.getString("fevencim"));
			datosPoliza.setNmrenova(rs.getString("nmrenova"));
			datosPoliza.setCdperpag(rs.getString("cdperpag"));
			datosPoliza.setDsperpag(rs.getString("dsperpag"));
			datosPoliza.setSwtarifi(rs.getString("swtarifi"));
			datosPoliza.setDstarifi(rs.getString("dstarifi"));
			datosPoliza.setCdtipcoa(rs.getString("cdtipcoa"));
			datosPoliza.setDstipcoa(rs.getString("dstipcoa"));
			datosPoliza.setNmcuadro(rs.getString("nmcuadro"));
			datosPoliza.setDscuadro(rs.getString("dscuadro"));
			datosPoliza.setPorredau(rs.getString("porredau"));
			datosPoliza.setPtpritot(rs.getString("ptpritot"));
			datosPoliza.setCdmotanu(rs.getString("cdmotanu"));
			datosPoliza.setDsmotanu(rs.getString("dsmotanu"));
			datosPoliza.setCdperson(rs.getString("cdperson"));
			datosPoliza.setTitular(rs.getString("titular"));
			datosPoliza.setCdrfc(rs.getString("cdrfc"));
			datosPoliza.setCdagente(rs.getString("cdagente"));
			datosPoliza.setStatuspoliza(rs.getString("status_poliza"));
			datosPoliza.setCdplan(rs.getString("cdplan"));
			datosPoliza.setDsplan(rs.getString("dsplan"));
			datosPoliza.setCdramo(rs.getString("cdramo"));
			datosPoliza.setDsramo(rs.getString("dsramo"));
			datosPoliza.setCdtipsit(rs.getString("cdtipsit"));
			datosPoliza.setCdunieco(rs.getString("cdunieco"));
			datosPoliza.setDsunieco(rs.getString("dsunieco"));
			datosPoliza.setNmpolant(rs.getString("nmpolant"));
			datosPoliza.setDstipsit(rs.getString("dstipsit"));
			datosPoliza.setIcodpoliza(rs.getString("icodpoliza"));
			datosPoliza.setFepag(rs.getString("fepag"));
			datosPoliza.setStatuspago(rs.getString("status_pago"));
			return datosPoliza;
		}
	}
	
	//Datos complementarios de la p�liza
	@Override
	public List<DatosComplementariosVO> obtieneDatosComplementarios(PolizaVO poliza,
			AseguradoVO asegurado) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmpoliza_i", poliza.getIcodpoliza());
		params.put("pv_cdperson_i", asegurado.getCdperson());
		Map<String, Object> mapResult = ejecutaSP(new ConsultaDatosComplementariosSP(getDataSource()), params);
		return (List<DatosComplementariosVO>) mapResult.get("rs");
		
	}
	
	public class ConsultaDatosComplementariosSP extends StoredProcedure{
		protected ConsultaDatosComplementariosSP(DataSource dataSource){
			super(dataSource, "P_Get_Datos_Complementarios");
			declareParameter(new SqlParameter("pv_nmpoliza_i", Types.INTEGER));
			declareParameter(new SqlParameter("pv_cdperson_i", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_msg_id_o", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_title_o", Types.VARCHAR));
			declareParameter(new SqlReturnResultSet("rs", new DatosComplementariosMapper()));
			compile();
		}
	}
	
	public class DatosComplementariosMapper implements RowMapper<DatosComplementariosVO>{
		public DatosComplementariosVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			DatosComplementariosVO datosComplementarios = new DatosComplementariosVO();
			datosComplementarios.setCdperson(rs.getString("cdperson"));
			datosComplementarios.setNombre(rs.getString("vchNombre"));
			datosComplementarios.setFenacimi(rs.getString("fenacimi"));
			datosComplementarios.setEdad(rs.getString("iedad"));
			datosComplementarios.setDsplan(rs.getString("dsplan"));
			datosComplementarios.setAgente(rs.getString("agente"));
			return datosComplementarios;
		}
	}

	// Copagos de la p�liza.
	@Override
	public List<CopagoVO> obtieneCopagosPoliza(PolizaVO poliza, String nmsituac)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmpoliza_i", poliza.getIcodpoliza());
		Map<String, Object> mapResult = ejecutaSP(new ConsultaCopagosPolizaSP(
				getDataSource()), params);
		return (List<CopagoVO>) mapResult.get("rs");
	}

	public class ConsultaCopagosPolizaSP extends StoredProcedure {
		protected ConsultaCopagosPolizaSP(DataSource dataSource) {
			super(dataSource, "P_Get_Copagos");
			declareParameter(new SqlParameter("pv_nmpoliza_i", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_msg_id_o", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_title_o", Types.VARCHAR));
			declareParameter(new SqlReturnResultSet("rs",
					new CopagosPolizaMapper()));
			compile();
		}
	}

	public class CopagosPolizaMapper implements RowMapper<CopagoVO> {
		public CopagoVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			CopagoVO copago = new CopagoVO();
			copago.setOrden(rs.getString("ORDEN"));
			copago.setDescripcion(rs.getString("descripcion"));
			copago.setValor(rs.getString("valor"));
			copago.setAgrupador("");
			copago.setNivel(rs.getInt("NIVEL"));
			return copago;
		}
	}
	
	// Coberturas p�liza.
	@Override
	public List<CoberturaBasicaVO> obtieneCoberturasPoliza(PolizaVO poliza)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmpoliza_i", poliza.getIcodpoliza());
		Map<String, Object> mapResult = ejecutaSP(new ConsultaCoberturasPolizaSP(
				getDataSource()), params);
		return (List<CoberturaBasicaVO>) mapResult.get("rs");
	}

	public class ConsultaCoberturasPolizaSP extends StoredProcedure {
		protected ConsultaCoberturasPolizaSP(DataSource dataSource) {
			super(dataSource, "P_Get_Coberturas");
			declareParameter(new SqlParameter("pv_nmpoliza_i", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_msg_id_o", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_title_o", Types.VARCHAR));
			declareParameter(new SqlReturnResultSet("rs",
					new CoberturasPolizaMapper()));
			compile();
		}
	}

	public class CoberturasPolizaMapper implements RowMapper<CoberturaBasicaVO> {
		public CoberturaBasicaVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			CoberturaBasicaVO coberturas = new CoberturaBasicaVO();
			coberturas.setDescripcion(rs.getString("vchDescripcion"));			
			return coberturas;
		}
	}
	
	// Coberturas b�sicas.
		@Override
		public List<CoberturaBasicaVO> obtieneCoberturasBasicas(PolizaVO poliza)
				throws Exception {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pv_nmpoliza_i", poliza.getIcodpoliza());
			Map<String, Object> mapResult = ejecutaSP(new ConsultaCoberturasBasicasSP(
					getDataSource()), params);
			return (List<CoberturaBasicaVO>) mapResult.get("rs");
		}

		public class ConsultaCoberturasBasicasSP extends StoredProcedure {
			protected ConsultaCoberturasBasicasSP(DataSource dataSource) {
				super(dataSource, "P_Get_Coberturas_Basicas");
				declareParameter(new SqlParameter("pv_nmpoliza_i", Types.INTEGER));
				declareParameter(new SqlOutParameter("pv_msg_id_o", Types.INTEGER));
				declareParameter(new SqlOutParameter("pv_title_o", Types.VARCHAR));
				declareParameter(new SqlReturnResultSet("rs",
						new CoberturasBasicasMapper()));
				compile();
			}
		}

		public class CoberturasBasicasMapper implements RowMapper<CoberturaBasicaVO> {
			public CoberturaBasicaVO mapRow(ResultSet rs, int rowNum) throws SQLException {
				CoberturaBasicaVO coberturasBasicas = new CoberturaBasicaVO();
				coberturasBasicas.setDescripcion(rs.getString("vchDescripcion"));
				coberturasBasicas.setCopagoporcentaje(rs.getString("fltCopago"));
				coberturasBasicas.setCopagomonto(rs.getString("mCopago"));
				coberturasBasicas.setIncluido(rs.getString("tiactivo"));
				coberturasBasicas.setBeneficiomaximo(rs.getString("mBeneficioMax"));
				coberturasBasicas.setBeneficiomaximovida(rs.getString("mBenefMaxVida"));
				return coberturasBasicas;
			}
		}
		
		//Datos del plan
		@Override
		public List<PlanVO> obtieneDatosPlan(PolizaVO poliza) throws Exception {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pv_nmpoliza_i", poliza.getIcodpoliza());			
			Map<String, Object> mapResult = ejecutaSP(new ConsultaDatosPlanSP(getDataSource()), params);
			return (List<PlanVO>) mapResult.get("rs");
			
		}
		
		public class ConsultaDatosPlanSP extends StoredProcedure{
			protected ConsultaDatosPlanSP(DataSource dataSource){
				super(dataSource, "P_Get_Datos_Plan");
				declareParameter(new SqlParameter("pv_nmpoliza_i", Types.INTEGER));				
				declareParameter(new SqlOutParameter("pv_msg_id_o", Types.INTEGER));
				declareParameter(new SqlOutParameter("pv_title_o", Types.VARCHAR));
				declareParameter(new SqlReturnResultSet("rs", new DatosPlanMapper()));
				compile();
			}
		}
		
		public class DatosPlanMapper implements RowMapper<PlanVO>{
			public PlanVO mapRow(ResultSet rs, int rowNum) throws SQLException {				
				PlanVO datosPlan = new PlanVO();
				datosPlan.setPlan(rs.getString("vchCodPlan"));
				datosPlan.setFecha(rs.getString("dtFecha"));
				datosPlan.setDescripcion(rs.getString("vchDescripcion"));
				datosPlan.setTipoprograma(rs.getString("vchDescTipoPlan"));
				datosPlan.setCalculopor(rs.getString("chClasifica"));
				datosPlan.setBeneficiomaximoanual(rs.getString("mBenefMaxAnual"));
				datosPlan.setBeneficiomaximovida(rs.getString("mBenefMaxVida"));
				datosPlan.setIdentificadortarifa(rs.getString("vchCodCosto"));
				datosPlan.setZona(rs.getString("vchDescripcionZona"));
				return datosPlan;
			}
		}
	
		//Datos del contratante
		@Override
		public List<ContratanteVO> obtieneDatosContratante(PolizaVO poliza) throws Exception {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pv_nmpoliza_i", poliza.getIcodpoliza());			
			Map<String, Object> mapResult = ejecutaSP(new ConsultaDatosContratanteSP(getDataSource()), params);
			return (List<ContratanteVO>) mapResult.get("rs");
			
		}
		
		public class ConsultaDatosContratanteSP extends StoredProcedure{
			protected ConsultaDatosContratanteSP(DataSource dataSource){
				super(dataSource, "P_Get_Datos_Contratante");
				declareParameter(new SqlParameter("pv_nmpoliza_i", Types.INTEGER));				
				declareParameter(new SqlOutParameter("pv_msg_id_o", Types.INTEGER));
				declareParameter(new SqlOutParameter("pv_title_o", Types.VARCHAR));
				declareParameter(new SqlReturnResultSet("rs", new DatosContratanteMapper()));
				compile();
			}
		}
		
		public class DatosContratanteMapper implements RowMapper<ContratanteVO>{
			public ContratanteVO mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				ContratanteVO datosContratante = new ContratanteVO();
				datosContratante.setRazonsocial(rs.getString("vchRazonSocial"));
				datosContratante.setZonacosto(rs.getString("vchZonaCosto"));
				datosContratante.setDomicilio(rs.getString("vchDomicilio"));
				datosContratante.setCiudad(rs.getString("C"));
				datosContratante.setEstado(rs.getString("E"));
				datosContratante.setRepresentante(rs.getString("vchRepresen"));
				datosContratante.setTelefono1(rs.getString("vchTelef1"));
				datosContratante.setArea1(rs.getString("vchAreaT1"));
				datosContratante.setPuesto(rs.getString("P"));
				datosContratante.setTelefono2(rs.getString("vchTelef2"));
				datosContratante.setArea2(rs.getString("vchAreaT2"));
				datosContratante.setGiro(rs.getString("G"));
				datosContratante.setFax(rs.getString("vchFax"));
				datosContratante.setAreafax(rs.getString("vchAreaF"));
				datosContratante.setCodigopostal(rs.getString("vchCP"));
				datosContratante.setTipocontratante(rs.getString("tiTipoContratante"));
				datosContratante.setRfc(rs.getString("vchRFC"));
				datosContratante.setImss(rs.getString("vchRegIMSS"));
				return datosContratante;
			}
		}

	// Asegurados de la p�liza.
	@Override
	public List<AseguradoVO> obtieneAsegurados(PolizaVO poliza)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmpoliza_i", poliza.getIcodpoliza());
		Map<String, Object> mapResult = ejecutaSP(new ConsultaAseguradosSP(
				getDataSource()), params);
		return (List<AseguradoVO>) mapResult.get("rs");
	}

	public class ConsultaAseguradosSP extends StoredProcedure {
		protected ConsultaAseguradosSP(DataSource dataSource) {
			super(dataSource, "P_Get_Datos_Aseg");
			declareParameter(new SqlParameter("pv_nmpoliza_i", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_msg_id_o", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_title_o", Types.VARCHAR));
			declareParameter(new SqlReturnResultSet("rs",
					new AseguradosMapper()));
			compile();
		}
	}

	public class AseguradosMapper implements RowMapper<AseguradoVO> {
		public AseguradoVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			AseguradoVO datosAsegurado = new AseguradoVO();
			datosAsegurado.setCdperson(rs.getString("cdperson"));
			datosAsegurado.setNmsituac(rs.getString("nmsituac"));
			datosAsegurado.setCdtipsit(rs.getString("cdtipsit"));
			datosAsegurado.setNombre(rs.getString("titular"));
			datosAsegurado.setCdrfc(rs.getString("cdrfc"));
			datosAsegurado.setCdrol(rs.getString("cdrol"));
			datosAsegurado.setSexo(rs.getString("Sexo"));
			datosAsegurado.setFenacimi(rs.getString("fenacimi"));
			datosAsegurado.setStatus(rs.getString("STATUS"));
			datosAsegurado.setParentesco(rs.getString("parentesco"));
			return datosAsegurado;
		}
	}
		
	// Endosos.
	@Override
	public List<ClausulaVO> obtieneEndososPoliza(PolizaVO poliza,
			AseguradoVO asegurado) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmpoliza_i", poliza.getIcodpoliza());
		params.put("pv_cdperson_i", asegurado.getCdperson());
		Map<String, Object> mapResult = ejecutaSP(
				new ConsultaEndososPolizaSP(getDataSource()), params);
		return (List<ClausulaVO>) mapResult.get("rs");
	}

	protected class ConsultaEndososPolizaSP extends StoredProcedure {
		protected ConsultaEndososPolizaSP(DataSource dataSource) {
			super(dataSource, "P_Obtiene_MPolicot");
			declareParameter(new SqlParameter("pv_nmpoliza_i", Types.INTEGER));
			declareParameter(new SqlParameter("pv_cdperson_i", Types.INTEGER));
			declareParameter(new SqlReturnResultSet("rs",
					new EndososPolizaMapper()));
			compile();
		}
	}

	protected class EndososPolizaMapper implements RowMapper<ClausulaVO> {
		public ClausulaVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ClausulaVO clausulaVO = new ClausulaVO();
			clausulaVO.setCdclausu(rs.getString("cdclausu"));
			clausulaVO.setDsclausu(rs.getString("dsclausu"));
			clausulaVO.setCdtipcla(rs.getString("cdtipcla"));
			clausulaVO.setStatus(rs.getString("status"));
			clausulaVO.setLinea_general(rs.getString("linea_general"));
			return clausulaVO;
		}
	}
	
	//Hist�rico de Farmacia
	@Override
	public List<HistoricoFarmaciaVO> obtieneHistoricoFarmacia(PolizaVO poliza,
			AseguradoVO asegurado) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmpoliza_i", poliza.getIcodpoliza());
		params.put("pv_cdperson_i", asegurado.getCdperson());		
		Map<String, Object> mapResult = ejecutaSP(new ConsultaHistoricoFarmaciaSP(getDataSource()), params);
		return (List<HistoricoFarmaciaVO>) mapResult.get("rs");
	}
	
	protected class ConsultaHistoricoFarmaciaSP extends StoredProcedure {
		protected ConsultaHistoricoFarmaciaSP(DataSource dataSource){
			super(dataSource, "P_Get_Datos_Farmacia");
			declareParameter(new SqlParameter("pv_cdperson_i", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_msg_id_o", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_title_o", Types.VARCHAR));
			declareParameter(new SqlReturnResultSet("rs", new HistoricoFarmaciaMapper()));			
			compile();
		}
	}
	
	public class HistoricoFarmaciaMapper implements RowMapper<HistoricoFarmaciaVO>{
		public HistoricoFarmaciaVO mapRow(ResultSet rs, int rowNum) throws SQLException{
			HistoricoFarmaciaVO historicoFarmacia = new HistoricoFarmaciaVO();
			historicoFarmacia.setCdperson(rs.getString("icodafiliado"));
			historicoFarmacia.setTigrupo(rs.getString("tiGrupo"));
			historicoFarmacia.setTotal(rs.getString("Total"));
			historicoFarmacia.setPoliza(rs.getString("Poliza"));
			historicoFarmacia.setEstatus(rs.getString("Estatus"));
			historicoFarmacia.setDtfecini(rs.getString("dtFecini"));
			historicoFarmacia.setDtfecfin(rs.getString("dtFecfin"));
			historicoFarmacia.setIultimoafiliado(rs.getString("iUltimoAfiliado"));
			historicoFarmacia.setMaximo(rs.getString("MAXIMO"));
			historicoFarmacia.setOrden(rs.getString("Orden"));
			historicoFarmacia.setIultimo(rs.getString("iUltimo"));
			historicoFarmacia.setPendiente(rs.getString("Pendiente"));
			//Gasto Total y Disponible
			float gastoTotalAux = 0;
			float disponibleAux = 0;
			if(StringUtils.isBlank(historicoFarmacia.getTotal()) == false && StringUtils.isBlank(historicoFarmacia.getPendiente()) == false){
				gastoTotalAux = Float.parseFloat(historicoFarmacia.getTotal()) + Float.parseFloat(historicoFarmacia.getPendiente());
				if(StringUtils.isBlank(historicoFarmacia.getMaximo()) == false){
					disponibleAux = Float.parseFloat(historicoFarmacia.getMaximo()) - gastoTotalAux - Float.parseFloat(historicoFarmacia.getPendiente());
				}
			}			
			historicoFarmacia.setGastototal(Float.toString(gastoTotalAux));
			historicoFarmacia.setDisponible(Float.toString(disponibleAux));
			
			return historicoFarmacia;
		}
	}
	
	//Periodos de Vigencia
		@Override
		public List<PeriodoVigenciaVO> obtienePeriodosVigencia(PolizaVO poliza,
				AseguradoVO asegurado) throws Exception {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pv_nmpoliza_i", poliza.getIcodpoliza());
			params.put("pv_cdperson_i", asegurado.getCdperson());		
			Map<String, Object> mapResult = ejecutaSP(new ConsultaPeriodosVigenciaSP(getDataSource()), params);
			return (List<PeriodoVigenciaVO>) mapResult.get("rs");
		}
		
		protected class ConsultaPeriodosVigenciaSP extends StoredProcedure {
			protected ConsultaPeriodosVigenciaSP(DataSource dataSource){
				super(dataSource, "P_Get_Datos_Vigencia");
				declareParameter(new SqlParameter("pv_cdperson_i", Types.INTEGER));
				declareParameter(new SqlOutParameter("pv_msg_id_o", Types.INTEGER));
				declareParameter(new SqlOutParameter("pv_title_o", Types.VARCHAR));
				declareParameter(new SqlReturnResultSet("rs", new PeriodosVigenciaMapper()));			
				compile();
			}
		}
		
		public class PeriodosVigenciaMapper implements RowMapper<PeriodoVigenciaVO>{
			public PeriodoVigenciaVO mapRow(ResultSet rs, int rowNum) throws SQLException{
				
				PeriodoVigenciaVO periodosVigencia = new PeriodoVigenciaVO();
				periodosVigencia.setEstatus(rs.getString("vchEstado"));
				periodosVigencia.setDias(rs.getString("iDias"));
				periodosVigencia.setAnios(rs.getString("iAnios"));
				periodosVigencia.setFeinicial(rs.getString("dtFecDesde"));
				periodosVigencia.setFefinal(rs.getString("dtFecHasta"));
				return periodosVigencia;
			}
		}
	

	//Detalle de asegurado.
	@Override
	public List<AseguradoDetalleVO> obtieneAseguradoDetalle(AseguradoVO asegurado) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdperson_i", asegurado.getCdperson());
		Map<String, Object> mapResult = ejecutaSP(new ConsultaAseguradoDetalleSP(getDataSource()),params);
		return (List<AseguradoDetalleVO>) mapResult.get("rs");
	}
	
	protected class ConsultaAseguradoDetalleSP extends StoredProcedure{
		protected ConsultaAseguradoDetalleSP(DataSource dataSource){
			super(dataSource, "P_Get_Atributos_Sit");
			declareParameter(new SqlParameter("pv_cdperson_i", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_msg_id_o", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_title_o", Types.VARCHAR));
			declareParameter(new SqlReturnResultSet("rs", new AseguradoDetalleMapper()));
			compile();
		}
	}
	
	protected class AseguradoDetalleMapper implements RowMapper<AseguradoDetalleVO>{
		@Override
		public AseguradoDetalleVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			AseguradoDetalleVO aseguradoDetalleVO = new AseguradoDetalleVO();
			aseguradoDetalleVO.setCdperson(rs.getString("icodafiliado"));
			aseguradoDetalleVO.setEdad(rs.getString("iedad"));
			aseguradoDetalleVO.setIdentidad(rs.getString("vchIDAfiliado"));
			aseguradoDetalleVO.setNombre(rs.getString("vchNombre"));
			aseguradoDetalleVO.setTiposangre(rs.getString("vchTipoSangre"));
			aseguradoDetalleVO.setAntecedentes(rs.getString("vchAntecedentes"));
			aseguradoDetalleVO.setOii(rs.getString("vchOII"));
			aseguradoDetalleVO.setTelefono(rs.getString("vchTelefono"));
			aseguradoDetalleVO.setDireccion(rs.getString("vchDomicilio"));
			aseguradoDetalleVO.setCorreo(rs.getString("vchemailT"));
			aseguradoDetalleVO.setMcp(rs.getString("vchMCP"));
			aseguradoDetalleVO.setMcpespecialidad(rs.getString("vchEspMCP"));
			aseguradoDetalleVO.setOcp(rs.getString("vchOCP"));
			aseguradoDetalleVO.setOcpespecialidad(rs.getString("vchEspOCP"));			
			return aseguradoDetalleVO;
		}
		
	}
	
	// Recibos de la p�liza.
	@Override
	public List<ReciboVO> obtieneRecibosPoliza(PolizaVO poliza)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmpoliza_i", poliza.getIcodpoliza());
		Map<String, Object> mapResult = ejecutaSP(new ConsultaRecibosPolizaSP(
				getDataSource()), params);
		return (List<ReciboVO>) mapResult.get("rs");
	}

	protected class ConsultaRecibosPolizaSP extends StoredProcedure {
		protected ConsultaRecibosPolizaSP(DataSource dataSource) {
			super(dataSource, "P_Obtiene_Recibos");
			declareParameter(new SqlParameter("pv_nmpoliza_i", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_msg_id_o", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_title_o", Types.VARCHAR));
			declareParameter(new SqlReturnResultSet("rs",
					new RecibosPolizaMapper()));
			compile();
		}
	}

	protected class RecibosPolizaMapper implements RowMapper<ReciboVO> {
		public ReciboVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ReciboVO reciboVO = new ReciboVO();
			reciboVO.setNmrecibo(rs.getString("nmrecibo"));
			reciboVO.setFeinicio(rs.getString("FEINICIO"));
			reciboVO.setFefinal(rs.getString("FEFINAL"));
			reciboVO.setFeemisio(rs.getString("FEEMISIO"));
			reciboVO.setCdestado(rs.getString("cdestado"));
			reciboVO.setDsestado(rs.getString("dsestado"));
			reciboVO.setPtimport(rs.getString("ptimport"));
			reciboVO.setTiporeci(rs.getString("TIPORECI"));
			reciboVO.setDstipore(rs.getString("DSTIPORE"));
			return reciboVO;
		}
	}

	// Detalle de los recibos de la p�liza
	@Override
	public List<DetalleReciboVO> obtieneDetalleRecibo(PolizaVO poliza,
			ReciboVO recibo) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_NmPoliza_i", poliza.getIcodpoliza());
		params.put("pv_cdRecibo_i", recibo.getNmrecibo()); // Deber�a ser
															// icodrecibo
		Map<String, Object> mapResult = ejecutaSP(new ConsultaDetalleReciboSP(
				getDataSource()), params);
		return (List<DetalleReciboVO>) mapResult.get("rs");
	}

	protected class ConsultaDetalleReciboSP extends StoredProcedure {
		protected ConsultaDetalleReciboSP(DataSource dataSource) {
			super(dataSource, "P_Obtiene_MRECIDET");
			declareParameter(new SqlParameter("pv_NmPoliza_i", Types.INTEGER));
			declareParameter(new SqlParameter("pv_cdRecibo_i", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_msg_id_o", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_title_o", Types.VARCHAR));
			declareParameter(new SqlReturnResultSet("rs",
					new DetalleRecibosMapper()));
			compile();
		}
	}

	protected class DetalleRecibosMapper implements RowMapper<DetalleReciboVO> {
		public DetalleReciboVO mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			DetalleReciboVO detalleReciboVO = new DetalleReciboVO();
			detalleReciboVO.setCdtipcon(rs.getString("CdTipCon"));
			detalleReciboVO.setDstipcon(rs.getString("DsTipCon"));
			detalleReciboVO.setPtimport(rs.getString("ptimport"));
			return detalleReciboVO;

		}
	}

	// Agentes de la p�liza.
	@Override
	public List<AgentePolizaVO> obtieneAgentesPoliza(PolizaVO poliza)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmpoliza_i", poliza.getIcodpoliza());
		Map<String, Object> mapResult = ejecutaSP(new ConsultaAgentesPolizaSP(
				getDataSource()), params);
		return (List<AgentePolizaVO>) mapResult.get("rs");
	}

	protected class ConsultaAgentesPolizaSP extends StoredProcedure {
		protected ConsultaAgentesPolizaSP(DataSource dataSource) {
			super(dataSource, "P_Get_Agente_Poliza");
			declareParameter(new SqlParameter("pv_nmpoliza_i", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_msg_id_o", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_title_o", Types.VARCHAR));
			declareParameter(new SqlReturnResultSet("rs",
					new AgentesPolizaMapper()));
			compile();
		}
	}

	protected class AgentesPolizaMapper implements RowMapper<AgentePolizaVO> {
		public AgentePolizaVO mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			AgentePolizaVO agentePolizaVO = new AgentePolizaVO();
			agentePolizaVO.setCdagente(rs.getString("CDAGENTE"));
			agentePolizaVO.setNombre(rs.getString("nombre"));
			agentePolizaVO.setCdtipoAg(rs.getString("CDTIPOAG"));
			agentePolizaVO.setDescripl(rs.getString("DESCRIPL"));
			agentePolizaVO.setPorparti(rs.getString("PORPARTI"));
			agentePolizaVO.setPorredau(rs.getString("PORREDAU"));
			agentePolizaVO.setNmsuplem(rs.getString("NMSUPLEM"));
			agentePolizaVO.setNmcuadro(rs.getString("NMCUADRO"));
			agentePolizaVO.setCdsucurs(rs.getString("CDSUCURS"));
			return agentePolizaVO;
		}
	}

	// Recibos del Agente
	@Override
	public List<ReciboAgenteVO> obtieneRecibosAgente(PolizaVO poliza)	throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmpoliza_i", poliza.getIcodpoliza());
		Map<String, Object> mapResult = ejecutaSP(new ConsultaRecibosAgenteSP(getDataSource()), params);
		return (List<ReciboAgenteVO>) mapResult.get("rs");
	}

	protected class ConsultaRecibosAgenteSP extends StoredProcedure {
		protected ConsultaRecibosAgenteSP(DataSource dataSource) {
			super(dataSource, "P_Get_Recibos_Agente");
			declareParameter(new SqlParameter("pv_nmpoliza_i", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_msg_id_o", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_title_o", Types.VARCHAR));
			declareParameter(new SqlReturnResultSet("rs",
					new RecibosAgenteMapper()));
			compile();
		}
	}

	protected class RecibosAgenteMapper implements
			RowMapper<ReciboAgenteVO> {
		public ReciboAgenteVO mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			ReciboAgenteVO reciboAgenteVO = new ReciboAgenteVO();
			reciboAgenteVO.setNmrecibo(rs.getString("NMRECIBO"));
			reciboAgenteVO.setFeinicio(rs.getString("Fecha_inicio"));
			reciboAgenteVO.setFefin(rs.getString("Fecha_fin"));
			reciboAgenteVO.setDsgarant(rs.getString("DSGARANT"));
			reciboAgenteVO.setPtimport(rs.getString("PTIMPORT"));
			return reciboAgenteVO;
		}
	}

	@Override
	public String obtieneMensajeAgente(PolizaVO poliza) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TarifaVO> obtieneTarifasPoliza(PolizaVO poliza)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SuplementoVO> obtieneHistoricoPolizaCorto(String sucursal, String producto, String polizacorto,String cdsisrol)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, String>> obtieneDatosPolizaTvalopol(PolizaAseguradoVO polizaAsegurado) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, String>> obtieneRecibosPolizaAuto(String cdunieco, String cdramo, String cdestado,
			String nmpoliza, String nmsuplem) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int obtieneNumeroDeIncisosPoliza(String cdunieco, String cdramo, String cdestado,
			String nmpoliza, String nmsuplem) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String obtieneNmsituacContratantePoliza(String cdunieco, String cdramo, String cdestado,
			String nmpoliza) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AseguradoVO> obtieneAsegurados(PolizaVO poliza, long start,
			long limit) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SuplementoVO> obtieneHistoricoPolizaAsegurado(PolizaAseguradoVO polizaAsegurado) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Map<String, String>> getQueryResult(String query, String usuario) throws Exception{
		// TODO Auto-generated method stub
				return null;
	}
	
	@Override
	public List<Map<String, String>> executePLSQL(String archivo,String usuario) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> obtieneDatosLigasRecibosPoliza(String cdunieco, String cdramo, String cdestado,
			String nmpoliza, String nmsuplem) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, String>> consultaIncisosPoliza(String cdunieco, String cdramo, String estado, String nmpoliza)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String insertaBitacora(Date fecha, String nombre, int polizas, String rango, String usuario)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}