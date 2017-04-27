package mx.com.gseguros.portal.consultas.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.gseguros.portal.consultas.dao.IConsultasAseguradoDAO;
import mx.com.gseguros.portal.consultas.model.AseguradoDetalleVO;
import mx.com.gseguros.portal.consultas.model.AseguradoVO;
import mx.com.gseguros.portal.consultas.model.AvisoHospitalizacionVO;
import mx.com.gseguros.portal.consultas.model.CoberturaBasicaVO;
import mx.com.gseguros.portal.consultas.model.ConsultaDatosComplementariosVO;
import mx.com.gseguros.portal.consultas.model.ConsultaDatosGeneralesPolizaVO;
import mx.com.gseguros.portal.consultas.model.ConsultaDatosTitularVO;
import mx.com.gseguros.portal.consultas.model.ConsultaPolizaActualVO;
import mx.com.gseguros.portal.consultas.model.ConsultaResultadosAseguradoVO;
import mx.com.gseguros.portal.consultas.model.ContratanteVO;
import mx.com.gseguros.portal.consultas.model.CopagoVO;
import mx.com.gseguros.portal.consultas.model.EndosoVO;
import mx.com.gseguros.portal.consultas.model.EnfermedadVO;
import mx.com.gseguros.portal.consultas.model.HistoricoFarmaciaVO;
import mx.com.gseguros.portal.consultas.model.HistoricoVO;
import mx.com.gseguros.portal.consultas.model.PeriodoVigenciaVO;
import mx.com.gseguros.portal.consultas.model.PlanVO;
import mx.com.gseguros.portal.consultas.model.PolizaAseguradoVO;
import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.general.model.BaseVO;
import mx.com.gseguros.portal.general.model.PolizaVO;
import mx.com.gseguros.portal.general.model.SolicitudCxPVO;
import mx.com.gseguros.utils.Utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.object.StoredProcedure;

public class ConsultasAseguradoDAOSISAImpl extends AbstractManagerDAO implements
		IConsultasAseguradoDAO {
	
	private static SimpleDateFormat renderFechas = new SimpleDateFormat("dd/MM/yyyy");
	
	/* Coincidencias del asegurado segun criterios: RFC, codigo de asegurado y
	nombre.*/
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaResultadosAseguradoVO> obtieneResultadosAsegurado(
			String rfc, String cdperson, String nombre) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdrfc_i", rfc);
		params.put("pv_cdperson_i", cdperson);
		params.put("pv_nombre_i", nombre);
		Map<String, Object> mapResult = ejecutaSP(new ConsultaResultadosAseguradoSP(getDataSource()), params);
		return (List<ConsultaResultadosAseguradoVO>) mapResult.get("rs");
	}
	
	protected class ConsultaResultadosAseguradoSP extends StoredProcedure {
		protected ConsultaResultadosAseguradoSP(DataSource dataSource) {
			super(dataSource, "P_Get_Resultados_Asegurado");
			declareParameter(new SqlParameter("pv_cdrfc_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_nombre_i", Types.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_title_o", Types.VARCHAR));
			declareParameter(new SqlReturnResultSet("rs", new ResultadosAseguradoMapper()));
			compile();
		}
	}
	
	protected class ResultadosAseguradoMapper implements
		RowMapper<ConsultaResultadosAseguradoVO> {
		public ConsultaResultadosAseguradoVO mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			ConsultaResultadosAseguradoVO resultadosAsegurado = new ConsultaResultadosAseguradoVO();
			resultadosAsegurado.setCdramo(rs.getString("codigo_ramo"));
			resultadosAsegurado.setCdunieco(rs.getString("compania"));
			resultadosAsegurado.setDsramo(rs.getString("descripcion_ramo"));
			resultadosAsegurado.setDsunieco(rs.getString("descripcion"));
			resultadosAsegurado.setEstado(rs.getString("estado"));
			resultadosAsegurado.setNmpoliex(rs.getString("nmpoliex"));
			resultadosAsegurado.setNmpoliza(rs.getString("nmpoliza"));
			resultadosAsegurado.setCdperson(rs.getString("cdperson"));
			resultadosAsegurado.setNombreAsegurado(rs.getString("nombre"));
			resultadosAsegurado.setIdentificacion(rs.getString("identificacion"));
			// Recupera el iCodPoliza de SISA
			resultadosAsegurado.setIcodpoliza(rs.getString("icodpoliza"));
			//Recupera el Origen
			resultadosAsegurado.setOrigen(rs.getString("origen"));
			resultadosAsegurado.setFeinivigencia(rs.getString("feinivigencia"));
			resultadosAsegurado.setFefinvigencia(rs.getString("fefinvigencia"));
			return resultadosAsegurado;
		}
	}
	
	// poliza actual del asegurado.
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaPolizaActualVO> obtienePolizaActual(
			PolizaAseguradoVO polizaAsegurado) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmpoliza_i", polizaAsegurado.getIcodpoliza());
		Map<String, Object> mapResult = ejecutaSP(
				new ConsultaPolizaActualSP(getDataSource()), params);
		return (List<ConsultaPolizaActualVO>) mapResult.get("rs");
	}
	
	
	protected class ConsultaPolizaActualSP extends StoredProcedure {
		protected ConsultaPolizaActualSP(DataSource dataSource) {
			super(dataSource, "P_Get_Poliza_Asegurado");
			declareParameter(new SqlParameter("pv_nmpoliza_i", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_msg_id_o", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_title_o", Types.VARCHAR));
			declareParameter(new SqlReturnResultSet("rs",
					new DatosSuplementoMapper()));
			compile();
		}
	}

	protected class DatosSuplementoMapper implements
			RowMapper<ConsultaPolizaActualVO> {
		public ConsultaPolizaActualVO mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			ConsultaPolizaActualVO polizaActual = new ConsultaPolizaActualVO();
			polizaActual.setCdunieco(rs.getString("cdunieco"));
			polizaActual.setCdramo(rs.getString("cdramo"));
			polizaActual.setEstado(rs.getString("estado"));
			polizaActual.setNmpoliza(rs.getString("nmpoliza"));
			polizaActual.setNmsuplem(rs.getString("nmsuplem"));
			polizaActual.setFeinival(rs.getString("feinival"));
			polizaActual.setFefinval(rs.getString("fefinval"));
			polizaActual.setNsuplogi(rs.getString("nsuplogi"));
			polizaActual.setFeemisio(rs.getString("feemisio"));
			polizaActual.setNlogisus(rs.getString("nlogisus"));
			polizaActual.setDstipsup(rs.getString("dstipsup"));
			polizaActual.setPtpritot(rs.getString("ptpritot"));
			polizaActual.setIcodpoliza(rs.getString("icodpoliza"));
			polizaActual.setOrigen(rs.getString("origen"));
			return polizaActual;
		}
	}
	
	//Datos complementarios para Datos Generales.
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaDatosComplementariosVO> obtieneDatosComplementarios(PolizaVO poliza,
			AseguradoVO asegurado) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmpoliza_i", poliza.getIcodpoliza());
		params.put("pv_cdperson_i", asegurado.getCdperson());
		Map<String, Object> mapResult = ejecutaSP(new ConsultaDatosComplementariosSP(getDataSource()), params);
		return (List<ConsultaDatosComplementariosVO>) mapResult.get("rs");
		
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
	
	public class DatosComplementariosMapper implements RowMapper<ConsultaDatosComplementariosVO>{
		public ConsultaDatosComplementariosVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ConsultaDatosComplementariosVO datosComplementarios = new ConsultaDatosComplementariosVO();
			datosComplementarios.setCdperson(rs.getString("cdperson"));
			datosComplementarios.setNombre(rs.getString("vchNombre"));
			datosComplementarios.setFenacimi(rs.getString("fenacimi"));
			datosComplementarios.setEdad(rs.getString("iedad"));
			datosComplementarios.setDsplan(rs.getString("dsplan"));
			datosComplementarios.setAgente(rs.getString("agente"));
			datosComplementarios.setStatusaseg(rs.getString("statusaseg"));
			datosComplementarios.setSexo(rs.getString("sexo"));
			return datosComplementarios;
		}
	}
	
	//Consulta datos generales de la poliza
	// Datos generales de la poliza.
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaDatosGeneralesPolizaVO> obtieneDatosPoliza(
			PolizaAseguradoVO polizaAsegurado) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmpoliza_i", polizaAsegurado.getIcodpoliza());
		Map<String, Object> mapResult = ejecutaSP(new ConsultaDatosPolizaSP(
				getDataSource()), params);
		return (List<ConsultaDatosGeneralesPolizaVO>) mapResult.get("rs");
	}

	public class ConsultaDatosPolizaSP extends StoredProcedure {
		protected ConsultaDatosPolizaSP(DataSource dataSource) {
			super(dataSource, "P_Get_Datos_Generales_Poliza");
			declareParameter(new SqlParameter("pv_nmpoliza_i", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_msg_id_o", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_title_o", Types.VARCHAR));
			declareParameter(new SqlReturnResultSet("rs",
					new DatosPolizaMapper()));
			compile();
		}
	}

	protected class DatosPolizaMapper implements
			RowMapper<ConsultaDatosGeneralesPolizaVO> {
		public ConsultaDatosGeneralesPolizaVO mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			ConsultaDatosGeneralesPolizaVO datosPoliza = new ConsultaDatosGeneralesPolizaVO();
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
			datosPoliza.setAgente(rs.getString("dsagente"));
			return datosPoliza;
		}
	}
	
	//Datos del Asegurado (Detalle)
	@SuppressWarnings("unchecked")
	@Override
	public List<AseguradoDetalleVO> obtieneAseguradoDetalle(PolizaVO poliza, AseguradoVO asegurado) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdperson_i", asegurado.getCdperson());
		Map<String, Object> mapResult = ejecutaSP(new ConsultaAseguradoDetalleSP(getDataSource()),params);
		return (List<AseguradoDetalleVO>) mapResult.get("rs");
	}
	
	protected class ConsultaAseguradoDetalleSP extends StoredProcedure{
		protected ConsultaAseguradoDetalleSP(DataSource dataSource){
			super(dataSource, "P_Get_Asegurado_Detalle");
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
	
	//Datos del titular
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaDatosTitularVO> obtieneDatosTitular(PolizaVO poliza, AseguradoVO asegurado) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmpoliza_i", poliza.getIcodpoliza());
		params.put("pv_cdperson_i", asegurado.getCdperson());
		Map<String, Object> mapResult = ejecutaSP(new ConsultaDatosTitularSP(getDataSource()), params);
		return (List<ConsultaDatosTitularVO>) mapResult.get("rs");
		
	}
	
	public class ConsultaDatosTitularSP extends StoredProcedure{
		protected ConsultaDatosTitularSP(DataSource dataSource){
			super(dataSource, "P_Get_Datos_Titular");
			declareParameter(new SqlParameter("pv_nmpoliza_i", Types.INTEGER));
			declareParameter(new SqlParameter("pv_cdperson_i", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_msg_id_o", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_title_o", Types.VARCHAR));
			declareParameter(new SqlReturnResultSet("rs", new DatosTitularMapper()));
			compile();
		}
	}
	
	public class DatosTitularMapper implements RowMapper<ConsultaDatosTitularVO>{
		public ConsultaDatosTitularVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			ConsultaDatosTitularVO datosTitular = new ConsultaDatosTitularVO();
			datosTitular.setIdentificacion(rs.getString("vchIDTitular"));
			datosTitular.setNombre(rs.getString("vchNombre"));
			datosTitular.setFenacimiento(rs.getString("dtFecNacimiento"));
			datosTitular.setTelefono(rs.getString("vchTelefono"));
			datosTitular.setSexo(rs.getString("vchSexo"));
			datosTitular.setEdad(rs.getString("edad"));
			datosTitular.setDireccion(rs.getString("vchDomicilio"));
			datosTitular.setColonia(rs.getString("vchColonia"));
			datosTitular.setCodigopostal(rs.getString("vchCP"));
			datosTitular.setEdocivil(rs.getString("vchEstadoCivil"));
			datosTitular.setFeingreso(rs.getString("dtFecingreso"));
			datosTitular.setCelular(rs.getString("vchCelular"));
			datosTitular.setEmail(rs.getString("vchEmailT"));
			return datosTitular;
		}
	}
	
	//Datos del contratante
	@SuppressWarnings("unchecked")
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
	
	// Asegurados de la poliza.
	@SuppressWarnings("unchecked")
	@Override
	public List<AseguradoVO> obtieneAsegurados(PolizaVO poliza, AseguradoVO asegurado)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmpoliza_i", poliza.getIcodpoliza());
		params.put("pv_cdperson_i", asegurado.getCdperson());
		Map<String, Object> mapResult = ejecutaSP(new ConsultaAseguradosSP(
				getDataSource()), params);
		return (List<AseguradoVO>) mapResult.get("rs");
	}

	public class ConsultaAseguradosSP extends StoredProcedure {
		protected ConsultaAseguradosSP(DataSource dataSource) {
			super(dataSource, "P_Get_Datos_Familia");
			declareParameter(new SqlParameter("pv_nmpoliza_i", Types.INTEGER));
			declareParameter(new SqlParameter("pv_cdperson_i", Types.INTEGER));
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
			datosAsegurado.setNombre(rs.getString("nombre"));
			datosAsegurado.setCdrfc(rs.getString("cdrfc"));
			datosAsegurado.setCdrol(rs.getString("cdrol"));
			datosAsegurado.setSexo(rs.getString("sexo"));
			datosAsegurado.setFenacimi(rs.getString("fenacimi"));
			datosAsegurado.setStatus(rs.getString("status"));
			datosAsegurado.setParentesco(rs.getString("parentesco"));
			return datosAsegurado;
		}
	}
	
	//Datos de los endosos de exclusion
	@SuppressWarnings("unchecked")
	@Override
	public List<EndosoVO> obtieneEndososPoliza(PolizaVO poliza,
			AseguradoVO asegurado) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmpoliza_i", poliza.getIcodpoliza());
		params.put("pv_cdperson_i", asegurado.getCdperson());
		Map<String, Object> mapResult = ejecutaSP(new ConsultaEndososSP(
				getDataSource()), params);
		return (List<EndosoVO>) mapResult.get("rs");
	}
	
	public class ConsultaEndososSP extends StoredProcedure {
		protected ConsultaEndososSP(DataSource dataSource) {
			super(dataSource, "P_Get_Endosos_Asegurado");
			declareParameter(new SqlParameter("pv_nmpoliza_i", Types.INTEGER));
			declareParameter(new SqlParameter("pv_cdperson_i", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_msg_id_o", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_title_o", Types.VARCHAR));
			declareParameter(new SqlReturnResultSet("rs",
					new EndososMapper()));
			compile();
		}
	}
	
	public class EndososMapper implements RowMapper<EndosoVO> {
		public EndosoVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			EndosoVO endoso = new EndosoVO();
			endoso.setTipoendoso(rs.getString("TipoEndoso"));
			endoso.setTipoextraprima(rs.getString("TipoExtraPrima"));
			endoso.setDescripcion(rs.getString("Descripcion"));
			endoso.setResumen(rs.getString("Resumen"));			
			return endoso;
		}
	}
	
	//Datos de las enfermedades cronicas
	@SuppressWarnings("unchecked")
	@Override
	public List<EnfermedadVO> obtieneEnfermedades(PolizaVO poliza,
			AseguradoVO asegurado) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();		
		params.put("pv_cdperson_i", asegurado.getCdperson());
		Map<String, Object> mapResult = ejecutaSP(new ConsultaEnfermedadesSP(
				getDataSource()), params);
		return (List<EnfermedadVO>) mapResult.get("rs");
	}
	
	public class ConsultaEnfermedadesSP extends StoredProcedure {
		protected ConsultaEnfermedadesSP(DataSource dataSource) {
			super(dataSource, "P_Get_Datos_Enfermedades");			
			declareParameter(new SqlParameter("pv_cdperson_i", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_msg_id_o", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_title_o", Types.VARCHAR));
			declareParameter(new SqlReturnResultSet("rs",
					new EnfermedadesMapper()));
			compile();
		}
	}
	
	public class EnfermedadesMapper implements RowMapper<EnfermedadVO> {
		public EnfermedadVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			EnfermedadVO enfermedad = new EnfermedadVO();
			enfermedad.setIcd(rs.getString("vchCodICD9"));
			enfermedad.setEnfermedad(rs.getString("vchDescripcion"));
			return enfermedad;
		}
	}
	
	
	//Datos del plan
	@SuppressWarnings("unchecked")
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
	
	// Copagos de la poliza.
	@SuppressWarnings("unchecked")
	@Override
	public List<CopagoVO> obtieneCopagosPoliza(PolizaVO poliza)
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
	
	// Coberturas poliza.
	@SuppressWarnings("unchecked")
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
	
	// Coberturas basicas.
	@SuppressWarnings("unchecked")
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
	
	// Historico del asegurado 
	@SuppressWarnings("unchecked")
	@Override
	public List<HistoricoVO> obtieneHistoricoAsegurado(
			PolizaAseguradoVO polizaAsegurado, AseguradoVO asegurado) throws Exception {		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmpoliex_i", polizaAsegurado.getIcodpoliza());
		params.put("pv_cdperson_i", asegurado.getCdperson());
		Map<String, Object> mapResult = ejecutaSP(
				new ConsultaHistoricoAseguradoSP(getDataSource()), params);
		return (List<HistoricoVO>) mapResult.get("rs");
	}

	
	protected class ConsultaHistoricoAseguradoSP extends StoredProcedure {
		protected ConsultaHistoricoAseguradoSP(DataSource dataSource) {
			super(dataSource, "P_Get_Datos_Historico");
			declareParameter(new SqlParameter("pv_nmpoliex_i", Types.INTEGER));
			declareParameter(new SqlParameter("pv_cdperson_i", Types.INTEGER));
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
	
	//Historico de Farmacia
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
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
	
	/***Seccion Aviso de Hospitalizacion***/
	@SuppressWarnings("unchecked")
	//Lista de hospitales
	@Override
	public List<BaseVO> obtieneHospitales(String filtro) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iRowsAffected", 0);
		params.put("vchOpcional", "''");
		params.put("iOperacion", 2);
		params.put("iCodAfiliado", 0);
		params.put("iCodProveedor", 0);
		params.put("dtFecRegistro", null);
		params.put("dtFecIngreso", null);
		params.put("siUsuario", 0);
		params.put("vchCadena", filtro);
		Map<String, Object> mapResult = ejecutaSP(new ConsultaHospitalesSP(getDataSource()), params);
		return (List<BaseVO>) mapResult.get("rs");
	}
	
	public class ConsultaHospitalesSP extends StoredProcedure{
		protected ConsultaHospitalesSP(DataSource dataSource){
			super(dataSource, "paAvisoHospitalizacion");
			declareParameter(new SqlOutParameter("iRowsAffected", Types.INTEGER));
			declareParameter(new SqlOutParameter("vchOpcional", Types.VARCHAR));
			declareParameter(new SqlParameter("iOperacion", Types.INTEGER));
			declareParameter(new SqlParameter("iCodAfiliado", Types.INTEGER));
			declareParameter(new SqlParameter("iCodProveedor", Types.INTEGER));
			declareParameter(new SqlParameter("dtFecRegistro", Types.DATE));
			declareParameter(new SqlParameter("dtFecIngreso", Types.DATE));
			declareParameter(new SqlParameter("siUsuario", Types.SMALLINT));
			declareParameter(new SqlParameter("vchCadena", Types.VARCHAR));
			declareParameter(new SqlReturnResultSet("rs", new HospitalesMapper()));
			compile();
		}
	}
	
	public class HospitalesMapper implements RowMapper<BaseVO>{
		public BaseVO mapRow(ResultSet rs, int rowNum) throws SQLException{
			BaseVO hospitales = new BaseVO();
			hospitales.setKey(rs.getString("iCodProveedor"));
			hospitales.setValue(rs.getString("vchRazonSocial"));
			return hospitales;
		}
	}
	
	//Avisos Anteriores de hospitalizacion
		@SuppressWarnings("unchecked")
		@Override
		public List<AvisoHospitalizacionVO> obtieneAvisosAnteriores(
				AseguradoVO asegurado) throws Exception {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("iRowsAffected", 0);
			params.put("vchOpcional", "''");
			params.put("iOperacion", 3);
			params.put("iCodAfiliado", asegurado.getCdperson());	
			Map<String, Object> mapResult = ejecutaSP(new AvisosAnterioresSP(getDataSource()), params);
			return (List<AvisoHospitalizacionVO>) mapResult.get("rs");
		}
		
		public class AvisosAnterioresSP extends StoredProcedure{
			protected AvisosAnterioresSP(DataSource dataSource){
				super(dataSource, "paAvisoHospitalizacion");
				declareParameter(new SqlOutParameter("iRowsAffected", Types.INTEGER));
				declareParameter(new SqlOutParameter("vchOpcional", Types.VARCHAR));
				declareParameter(new SqlParameter("iOperacion", Types.INTEGER));
				declareParameter(new SqlParameter("iCodAfiliado", Types.INTEGER));
				declareParameter(new SqlReturnResultSet("rs", new AvisosAnterioresMapper()));
				compile();
			}
		}
		
		public class AvisosAnterioresMapper implements RowMapper<AvisoHospitalizacionVO>{
			public AvisoHospitalizacionVO mapRow(ResultSet rs, int rowNum) throws SQLException{
				AvisoHospitalizacionVO avisoHospitalizacion = new AvisoHospitalizacionVO();
				avisoHospitalizacion.setFeregistro(Utils.formateaFecha(rs.getString("dtFecRegistro")));
				avisoHospitalizacion.setDspresta(rs.getString("vchRazonSocial"));
				avisoHospitalizacion.setFeingreso(Utils.formateaFecha(rs.getString("dtFecIngreso")));
				avisoHospitalizacion.setComentario(rs.getString("vchObservacion"));
				return avisoHospitalizacion;
			}
		}

		@Override
		public String enviarAvisoHospitalizacion(AvisoHospitalizacionVO aviso)
				throws Exception {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("iRowsAffected", 0);
			params.put("vchOpcional", "''");
			params.put("iOperacion", 1);
			params.put("iCodAfiliado", aviso.getCdperson());
			params.put("iCodProveedor", aviso.getCdpresta());
			params.put("dtFecRegistro", null);
			params.put("dtFecIngreso", StringUtils.isBlank(aviso.getFeingreso()) ? null : renderFechas.parse(aviso.getFeingreso()));
			params.put("siUsuario", 515);
			params.put("vchCadena", aviso.getComentario());
			Map<String, Object> mapResult = ejecutaSP(new enviarAvisoHospitalizacionSP(getDataSource()), params);
			return (String) mapResult.get("vchOpcional");
		}
		
		public class enviarAvisoHospitalizacionSP extends StoredProcedure{
			protected enviarAvisoHospitalizacionSP(DataSource dataSource){
				super(dataSource, "paAvisoHospitalizacion");
				declareParameter(new SqlOutParameter("iRowsAffected", Types.INTEGER));
				declareParameter(new SqlOutParameter("vchOpcional", Types.VARCHAR));
				declareParameter(new SqlParameter("iOperacion", Types.INTEGER));
				declareParameter(new SqlParameter("iCodAfiliado", Types.INTEGER));
				declareParameter(new SqlParameter("iCodProveedor", Types.INTEGER));
				declareParameter(new SqlParameter("dtFecRegistro", Types.DATE));
				declareParameter(new SqlParameter("dtFecIngreso", Types.DATE));
				declareParameter(new SqlParameter("siUsuario", Types.SMALLINT));
				declareParameter(new SqlParameter("vchCadena", Types.VARCHAR));
				compile();
			}
		}
		
		@Override
		public String consultaTelefonoAgente(String cdagente) throws Exception {
			return null;
		}

		@Override
		public void actualizaEstatusEnvio(String iCodAviso) throws Exception {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("iRowsAffected", 0);
			params.put("vchOpcional", "''");
			params.put("iOperacion", 5);
			params.put("iCodAfiliado", 0);
			params.put("iCodProveedor", 0);
			params.put("dtFecRegistro", null);
			params.put("dtFecIngreso", null);
			params.put("siUsuario", 0);
			params.put("vchCadena", "''");
			params.put("iCodAviso", iCodAviso);
			ejecutaSP(new actualizaEstatusEnvioSP(getDataSource()), params);
		}
		
		public class actualizaEstatusEnvioSP extends StoredProcedure{
			protected actualizaEstatusEnvioSP(DataSource dataSource){
				super(dataSource, "paAvisoHospitalizacion");
				declareParameter(new SqlOutParameter("iRowsAffected", Types.INTEGER));
				declareParameter(new SqlOutParameter("vchOpcional", Types.VARCHAR));
				declareParameter(new SqlParameter("iOperacion", Types.INTEGER));
				declareParameter(new SqlParameter("iCodAfiliado", Types.INTEGER));
				declareParameter(new SqlParameter("iCodProveedor", Types.INTEGER));
				declareParameter(new SqlParameter("dtFecRegistro", Types.DATE));
				declareParameter(new SqlParameter("dtFecIngreso", Types.DATE));
				declareParameter(new SqlParameter("siUsuario", Types.SMALLINT));
				declareParameter(new SqlParameter("vchCadena", Types.VARCHAR));
				declareParameter(new SqlParameter("iCodAviso", Types.INTEGER));
				compile();
			}
		}

		@Override
		public List<SolicitudCxPVO> obtieneListadoSolicitudesCxp() throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
}