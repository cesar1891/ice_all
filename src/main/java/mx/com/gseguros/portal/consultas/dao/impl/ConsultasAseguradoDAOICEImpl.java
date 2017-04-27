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
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.Utils;
import oracle.jdbc.OracleTypes;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.object.StoredProcedure;

public class ConsultasAseguradoDAOICEImpl extends AbstractManagerDAO implements
		IConsultasAseguradoDAO {
	
	private static SimpleDateFormat renderFechas = new SimpleDateFormat("dd/MM/yyyy");
	
	/* Coincidencias del asegurado seg&uacute;n criterios: RFC, c&ocaute;digo de asegurado y
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
		return (List<ConsultaResultadosAseguradoVO>) mapResult.get("pv_registro_o");
	}
	
	protected class ConsultaResultadosAseguradoSP extends StoredProcedure {
		protected ConsultaResultadosAseguradoSP(DataSource dataSource) {
			super(dataSource, "PKG_CONSULTA_GS.P_Get_Resultados_Asegurado");
    		declareParameter(new SqlParameter("pv_cdrfc_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nombre_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ResultadosAseguradoMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
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
			resultadosAsegurado.setNombreAsegurado(rs.getString("nombre"));
			resultadosAsegurado.setIcodpoliza(null); // No utilizado para ICE
			//Recupera el Origen
			resultadosAsegurado.setOrigen(rs.getString("origen"));
			resultadosAsegurado.setCdperson(rs.getString("cdperson"));
			resultadosAsegurado.setFeinivigencia(rs.getString("feefecto"));
			resultadosAsegurado.setFefinvigencia(rs.getString("feproren"));
			resultadosAsegurado.setIdentificacion(rs.getString("identificacion"));
			resultadosAsegurado.setNmsituac(rs.getString("nmsituac"));
			return resultadosAsegurado;
		}
	}
	
	// P&ocaute;liza actual del asegurado.
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaPolizaActualVO> obtienePolizaActual(
			PolizaAseguradoVO polizaAsegurado) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_nmpoliex_i", polizaAsegurado.getNmpoliex());
		Map<String, Object> mapResult = ejecutaSP(new ConsultaPolizaActualSP(getDataSource()), params);
		return (List<ConsultaPolizaActualVO>) mapResult.get("pv_registro_o");
	}
	
	
	protected class ConsultaPolizaActualSP extends StoredProcedure {
		protected ConsultaPolizaActualSP(DataSource dataSource) {
			super(dataSource, "PKG_CONSULTA_GS.P_Get_Datos_Suplem");
			declareParameter(new SqlParameter("pv_nmpoliex_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatosSuplementoMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
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
			polizaActual.setCdsubram(rs.getString("cdsubram"));
			polizaActual.setEstado(rs.getString("estado"));
			polizaActual.setNmpoliza(rs.getString("nmpoliza"));
			polizaActual.setNmsuplem(rs.getString("nmsuplem"));
			polizaActual.setFeinival(rs.getString("feinival"));
			//polizaActual.setFefinval(rs.getString("fefinval"));
			polizaActual.setNsuplogi(rs.getString("nsuplogi"));
			polizaActual.setFeemisio(rs.getString("feemisio"));
			polizaActual.setNlogisus(rs.getString("nlogisus"));
			polizaActual.setDstipsup(rs.getString("dstipsup"));
			polizaActual.setPtpritot(rs.getString("ptpritot"));
			//polizaActual.setIcodpoliza(rs.getString("icodpoliza"));
			polizaActual.setOrigen(rs.getString("origen"));
			return polizaActual;
		}
	}
	
	//Datos Complementarios a los Datos Generales.
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaDatosComplementariosVO> obtieneDatosComplementarios(PolizaVO poliza,
			AseguradoVO asegurado) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", poliza.getCdunieco());
		params.put("pv_cdramo_i",   poliza.getCdramo());
		params.put("pv_estado_i",   poliza.getEstado());
		params.put("pv_nmpoliza_i", poliza.getNmpoliza());
		params.put("pv_nmsuplem_i", poliza.getNmsuplem());
		params.put("pv_cdperson_i", asegurado.getCdperson());		
		Map<String, Object> mapResult = ejecutaSP(new ConsultaDatosComplementariosSP(getDataSource()), params);		
		return (List<ConsultaDatosComplementariosVO>) mapResult.get("pv_registro_o");
		
	}
	
	public class ConsultaDatosComplementariosSP extends StoredProcedure{
		protected ConsultaDatosComplementariosSP(DataSource dataSource){
			super(dataSource, "PKG_CONSULTA_GS.P_Get_Datos_Aseg");
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));    		
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatosComplementariosMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
		}
	}
	
	public class DatosComplementariosMapper implements RowMapper<ConsultaDatosComplementariosVO>{
		public ConsultaDatosComplementariosVO mapRow(ResultSet rs, int rowNum) throws SQLException {			
			ConsultaDatosComplementariosVO datosComplementarios = new ConsultaDatosComplementariosVO();
			datosComplementarios.setCdperson(rs.getString("cdperson"));
			datosComplementarios.setNombre(rs.getString("titular"));
			datosComplementarios.setFenacimi(rs.getString("fenacimi"));
			datosComplementarios.setEdad(rs.getString("iedad"));			
			datosComplementarios.setStatusaseg(rs.getString("status"));
			datosComplementarios.setSexo(rs.getString("Sexo"));
			return datosComplementarios;
		}
	}
	
	//Consulta datos generales de la p&oacute;liza
	// Datos generales de la p&oacute;liza.
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaDatosGeneralesPolizaVO> obtieneDatosPoliza(
			PolizaAseguradoVO polizaAsegurado) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", polizaAsegurado.getCdunieco());
		params.put("pv_cdramo_i",   polizaAsegurado.getCdramo());
		params.put("pv_estado_i",   polizaAsegurado.getEstado());
		params.put("pv_nmpoliza_i", polizaAsegurado.getNmpoliza());
		Map<String, Object> mapResult = ejecutaSP(new ConsultaDatosPolizaSP(getDataSource()), params);
		return (List<ConsultaDatosGeneralesPolizaVO>) mapResult.get("pv_registro_o");
	}

	public class ConsultaDatosPolizaSP extends StoredProcedure {
		protected ConsultaDatosPolizaSP(DataSource dataSource) {
			super(dataSource, "PKG_CONSULTA_GS.P_Get_Datos_Poliza");
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
			datosPoliza.setAgente(rs.getString("dsagente"));
			return datosPoliza;
		}
	}
	
	//Datos del Asegurado (Detalle)
	@SuppressWarnings("unchecked")
	@Override
	public List<AseguradoDetalleVO> obtieneAseguradoDetalle(PolizaVO poliza, AseguradoVO asegurado) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", poliza.getCdunieco());
		params.put("pv_cdramo_i",   poliza.getCdramo());
		params.put("pv_estado_i",   poliza.getEstado());
		params.put("pv_nmpoliza_i", poliza.getNmpoliza());
		params.put("pv_nmsuplem_i", poliza.getNmsuplem());
		params.put("pv_cdperson_i", asegurado.getCdperson());		
		Map<String, Object> mapResult = ejecutaSP(new ConsultaAseguradoDetalleSP(getDataSource()), params);
		
		return (List<AseguradoDetalleVO>) mapResult.get("pv_registro_o");
	}
	
	protected class ConsultaAseguradoDetalleSP extends StoredProcedure{
		protected ConsultaAseguradoDetalleSP(DataSource dataSource){
			super(dataSource, "PKG_CONSULTA_GS.P_Get_Datos_Aseg");
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));    		
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new AseguradoDetalleMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
		}
	}
	
	protected class AseguradoDetalleMapper implements RowMapper<AseguradoDetalleVO>{
		@Override
		public AseguradoDetalleVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			AseguradoDetalleVO aseguradoDetalleVO = new AseguradoDetalleVO();
			aseguradoDetalleVO.setCdperson(rs.getString("cdperson"));
			aseguradoDetalleVO.setIdentidad(rs.getString("cdrfc"));
			aseguradoDetalleVO.setNombre(rs.getString("titular"));
			aseguradoDetalleVO.setEdad(rs.getString("iedad"));
			aseguradoDetalleVO.setCorreo(rs.getString("dsemail"));
			aseguradoDetalleVO.setTelefono(rs.getString("telefono"));
			aseguradoDetalleVO.setDireccion(rs.getString("direccion"));
						
			return aseguradoDetalleVO;
		}
		
	}
	
	//Datos del titular
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaDatosTitularVO> obtieneDatosTitular(PolizaVO poliza, AseguradoVO asegurado) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", poliza.getCdunieco());
		params.put("pv_cdramo_i",   poliza.getCdramo());
		params.put("pv_estado_i",   poliza.getEstado());
		params.put("pv_nmpoliza_i", poliza.getNmpoliza());
		params.put("pv_cdperson_i", asegurado.getCdperson());
		params.put("pv_nmsituac_i", poliza.getNmsituac());
		Map<String, Object> mapResult = ejecutaSP(new ConsultaDatosTitularSP(getDataSource()), params);
		return (List<ConsultaDatosTitularVO>) mapResult.get("pv_registro_o");
		
	}
	
	public class ConsultaDatosTitularSP extends StoredProcedure{
		protected ConsultaDatosTitularSP(DataSource dataSource){
			super(dataSource, "PKG_CONSULTA_GS.P_Get_Datos_Titular");
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatosTitularMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
		}
	}
	
	public class DatosTitularMapper implements RowMapper<ConsultaDatosTitularVO>{
		public ConsultaDatosTitularVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			ConsultaDatosTitularVO datosTitular = new ConsultaDatosTitularVO();
			datosTitular.setIdentificacion(rs.getString("cdrfc"));
			datosTitular.setNombre(rs.getString("nombre"));
			datosTitular.setFenacimiento(rs.getString("fenacimi"));
			datosTitular.setTelefono(rs.getString("telefono"));
			datosTitular.setSexo(rs.getString("sexo"));
			datosTitular.setEdad(rs.getString("edad"));
			datosTitular.setDireccion(rs.getString("domicilio"));
			datosTitular.setColonia(rs.getString("colonia"));
			datosTitular.setCodigopostal(rs.getString("cdpostal"));
			datosTitular.setEdocivil(rs.getString("edocivil"));
			datosTitular.setFeingreso(rs.getString("feingreso"));
			datosTitular.setCelular(rs.getString("celular"));
			datosTitular.setEmail(rs.getString("correo"));
			return datosTitular;
		}
	}
	
	//Datos del contratante
	@SuppressWarnings("unchecked")
	@Override
	public List<ContratanteVO> obtieneDatosContratante(PolizaVO poliza) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", poliza.getCdunieco());
		params.put("pv_cdramo_i",   poliza.getCdramo());
		params.put("pv_estado_i",   poliza.getEstado());
		params.put("pv_nmpoliza_i", poliza.getNmpoliza());				
		Map<String, Object> mapResult = ejecutaSP(new ConsultaDatosContratanteSP(getDataSource()), params);
		return (List<ContratanteVO>) mapResult.get("pv_registro_o");
	}
	
	public class ConsultaDatosContratanteSP extends StoredProcedure{
		protected ConsultaDatosContratanteSP(DataSource dataSource){
			super(dataSource, "PKG_CONSULTA_GS.P_Get_Datos_Contratante");
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));    		    		
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatosContratanteMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
		}
	}
	
	public class DatosContratanteMapper implements RowMapper<ContratanteVO>{
		public ContratanteVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			ContratanteVO datosContratante = new ContratanteVO();
			datosContratante.setRazonsocial(rs.getString("nombre"));
			datosContratante.setDomicilio(rs.getString("domicilio"));
			datosContratante.setCiudad(rs.getString("ciudad"));
			datosContratante.setEstado(rs.getString("estado"));
			datosContratante.setTelefono1(rs.getString("telefono"));
			datosContratante.setFax(rs.getString("fax"));
			datosContratante.setCodigopostal(rs.getString("cdpostal"));
			datosContratante.setTipocontratante(rs.getString("tipocontratante"));
			datosContratante.setRfc(rs.getString("cdrfc"));
			
			return datosContratante;
		}
	}
	
	// Asegurados de la p&oacute;liza (Familia)
	@SuppressWarnings("unchecked")
	@Override
	public List<AseguradoVO> obtieneAsegurados(PolizaVO poliza, AseguradoVO asegurado)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", poliza.getCdunieco());
		params.put("pv_cdramo_i",   poliza.getCdramo());
		params.put("pv_estado_i",   poliza.getEstado());
		params.put("pv_nmpoliza_i", poliza.getNmpoliza());
		params.put("pv_nmsuplem_i", poliza.getNmsuplem());
		params.put("pv_nmsituac_i", poliza.getNmsituac());
		params.put("pv_cdperson_i", asegurado.getCdperson());
		Map<String, Object> mapResult = ejecutaSP(new ConsultaAseguradosSP(getDataSource()), params);
		
		return (List<AseguradoVO>) mapResult.get("pv_registro_o");
	}

	public class ConsultaAseguradosSP extends StoredProcedure {
		protected ConsultaAseguradosSP(DataSource dataSource) {
			super(dataSource, "PKG_CONSULTA_GS.P_Get_Datos_Familia");
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new AseguradosMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
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
		params.put("pv_cdunieco", poliza.getCdunieco());
		params.put("pv_cdramo", poliza.getCdramo());
		params.put("pv_estado", poliza.getEstado());
		params.put("pv_nmpoliza", poliza.getNmpoliza());
		params.put("pv_nmsituac", poliza.getNmsituac());
		Map<String, Object> mapResult = ejecutaSP(new ConsultaEndososSP(getDataSource()), params);
		return (List<EndosoVO>) mapResult.get("pv_registro_o");
	}
	
	public class ConsultaEndososSP extends StoredProcedure {
		protected ConsultaEndososSP(DataSource dataSource) {
			super(dataSource, "PKG_CONSULTA_GS.P_Get_Endosos_Asegurado");
			declareParameter(new SqlParameter("pv_cdunieco", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o",   OracleTypes.CURSOR, new EndososMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
			compile();
		}
	}
	
	public class EndososMapper implements RowMapper<EndosoVO> {
		public EndosoVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			EndosoVO endoso = new EndosoVO();
			endoso.setTipoendoso(rs.getString("dsclausu"));
			//endoso.setTipoextraprima(rs.getString("status"));
			endoso.setDescripcion(rs.getString("linea_general"));
			endoso.setResumen(rs.getString("linea_usuario"));			
			return endoso;
		}
	}
	
	
	//Datos del plan
	@SuppressWarnings("unchecked")
	@Override
	public List<PlanVO> obtieneDatosPlan(PolizaVO poliza) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", poliza.getCdunieco());
		params.put("pv_cdramo_i",   poliza.getCdramo());
		params.put("pv_estado_i",   poliza.getEstado());
		params.put("pv_nmpoliza_i", poliza.getNmpoliza());
		Map<String, Object> mapResult = ejecutaSP(new ConsultaDatosPlanSP(getDataSource()), params);
		return (List<PlanVO>) mapResult.get("pv_registro_o");
	}
	public class ConsultaDatosPlanSP extends StoredProcedure{
		protected ConsultaDatosPlanSP(DataSource dataSource){
			super(dataSource, "PKG_CONSULTA_GS.P_Get_Datos_Plan");
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatosPlanMapper()));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	public class DatosPlanMapper implements RowMapper<PlanVO>{
		public PlanVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			PlanVO datosPlan = new PlanVO();
			datosPlan.setPlan(rs.getString("dsramo"));
			datosPlan.setFecha(rs.getString("fefecsit"));
			datosPlan.setDescripcion(rs.getString("dsramo"));
			datosPlan.setTipoprograma(rs.getString("dsplan"));
			datosPlan.setBeneficiomaximoanual(rs.getString("sumaasegurada"));
			datosPlan.setIdentificadortarifa(rs.getString("dstarifi"));
			
			return datosPlan;
						
			
		}
	}
	
	// Copagos de lap&oacute;liza.
	@SuppressWarnings("unchecked")
	@Override
	public List<CopagoVO> obtieneCopagosPoliza(PolizaVO poliza)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", poliza.getCdunieco());
		params.put("pv_cdramo_i", poliza.getCdramo());
		params.put("pv_estado_i", poliza.getEstado());
		params.put("pv_nmpoliza_i", poliza.getNmpoliza());
		params.put("pv_nmsuplem_i", poliza.getNmsuplem());
		params.put("pv_nmsituac_i", poliza.getNmsituac());
		Map<String, Object> mapResult = ejecutaSP(new ConsultaCopagosPolizaSP(getDataSource()), params);		
		return (List<CopagoVO>) mapResult.get("pv_registro_o");
	}

	public class ConsultaCopagosPolizaSP extends StoredProcedure {
		protected ConsultaCopagosPolizaSP(DataSource dataSource) {
			
			super(dataSource, "PKG_CONSULTA.P_GET_COPAGOS");
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new CopagosPolizaMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
		}
	}

	public class CopagosPolizaMapper implements RowMapper<CopagoVO> {
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
	
	// Coberturas p&oacute;liza. Utilizan el mismo VO que las b&aacute;sicas. NO CAMBIAR
	@SuppressWarnings("unchecked")
	@Override
	public List<CoberturaBasicaVO> obtieneCoberturasPoliza(PolizaVO poliza)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", poliza.getCdunieco());
		params.put("pv_estado_i", poliza.getEstado());
		params.put("pv_cdramo_i", poliza.getCdramo());
		params.put("pv_nmpoliza_i", poliza.getNmpoliza());
		params.put("pv_nmsituac_i", poliza.getNmsituac());
		params.put("pv_cdgarant_i", null);
		
		Map<String, Object> mapResult = ejecutaSP(new ConsultaCoberturasPolizaSP(getDataSource()), params);	
		
		return (List<CoberturaBasicaVO>) mapResult.get("pv_registro_o");
	}

	public class ConsultaCoberturasPolizaSP extends StoredProcedure {
		protected ConsultaCoberturasPolizaSP(DataSource dataSource) {
			super(dataSource, "PKG_CONSULTA_GS.P_Get_Coberturas");
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdgarant_i", OracleTypes.VARCHAR));
			
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new CoberturasPolizaMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
		}
	}

	public class CoberturasPolizaMapper implements RowMapper<CoberturaBasicaVO> {
		public CoberturaBasicaVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			CoberturaBasicaVO coberturas = new CoberturaBasicaVO();
			coberturas.setDescripcion(rs.getString("dsgarant"));			
			return coberturas;
		}
	}
	
	// Coberturas b&aacute;sicas.
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
	
	// Hist&oacute;rico del asegurado 
	@SuppressWarnings("unchecked")
	@Override
	public List<HistoricoVO> obtieneHistoricoAsegurado(
			PolizaAseguradoVO polizaAsegurado, AseguradoVO asegurado) throws Exception {		
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("pv_cdperson_i", asegurado.getCdperson());
		params.put("pv_cdramo_i", polizaAsegurado.getCdramo());
		Map<String, Object> mapResult = ejecutaSP(new ConsultaHistoricoAseguradoSP(getDataSource()), params);
		return (List<HistoricoVO>) mapResult.get("pv_registro_o");
	}

	
	protected class ConsultaHistoricoAseguradoSP extends StoredProcedure {
		protected ConsultaHistoricoAseguradoSP(DataSource dataSource) {
			
			super(dataSource, "PKG_CONSULTA_GS.P_Get_Historico_Asegurado");
			
			declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatosHistoricoMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
		}
	}
	
	protected class DatosHistoricoMapper implements
	RowMapper<HistoricoVO> {
		public HistoricoVO mapRow(ResultSet rs, int rowNum)
				throws SQLException {				
			HistoricoVO datosHistorico = new HistoricoVO();
			
			datosHistorico.setEstado(rs.getString("dstatus"));
			datosHistorico.setNmpoliza(rs.getString("nmpoliex"));
			
			datosHistorico.setFeinival(rs.getString("feinival"));
			datosHistorico.setFefinval(rs.getString("fefinval"));
			datosHistorico.setNsuplogi(rs.getString("nsuplogi"));
			datosHistorico.setFeemisio(rs.getString("feemisio"));
			
			datosHistorico.setDsplan(rs.getString("dsplan"));
				
			datosHistorico.setOrigen(rs.getString("origen"));
			return datosHistorico;
		}
	}
	
	//Hist&oacute;rico de Farmacia
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

	@Override
	public List<EnfermedadVO> obtieneEnfermedades(PolizaVO poliza,
			AseguradoVO asegurado) throws Exception {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BaseVO> obtieneHospitales(String filtro) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdpresta_i", filtro);
		Map<String, Object> mapResult = ejecutaSP(new ConsultaHospitalesSP(getDataSource()), params);
		return (List<BaseVO>) mapResult.get("pv_registro_o");
	}
	
	public class ConsultaHospitalesSP extends StoredProcedure{
		protected ConsultaHospitalesSP(DataSource dataSource){
			super(dataSource, "PKG_CONSULTA_GS.P_Get_Catalogo_Hospitales");
    		declareParameter(new SqlParameter("pv_cdpresta_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new HospitalesMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	public class HospitalesMapper implements RowMapper<BaseVO>{
		public BaseVO mapRow(ResultSet rs, int rowNum) throws SQLException{
			BaseVO hospitales = new BaseVO();
			hospitales.setKey(rs.getString("cdpresta"));
			hospitales.setValue(rs.getString("dspresta"));
			return hospitales;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AvisoHospitalizacionVO> obtieneAvisosAnteriores(
			AseguradoVO asegurado) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdperson_i", asegurado.getCdperson());
		Map<String, Object> mapResult = ejecutaSP(new AvisosAnterioresSP(getDataSource()), params);
		return (List<AvisoHospitalizacionVO>) mapResult.get("pv_registro_o");
	}
	
	public class AvisosAnterioresSP extends StoredProcedure{
		protected AvisosAnterioresSP(DataSource dataSource){
			super(dataSource, "PKG_CONSULTA_GS.P_Get_Avisos_Hosp_Anteriores");
			declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new AvisosAnterioresMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	public class AvisosAnterioresMapper implements RowMapper<AvisoHospitalizacionVO>{
		public AvisoHospitalizacionVO mapRow(ResultSet rs, int rowNum) throws SQLException{
			AvisoHospitalizacionVO avisoHospitalizacion = new AvisoHospitalizacionVO();
			avisoHospitalizacion.setFeregistro(Utils.formateaFecha(rs.getString("feregistro")));
			avisoHospitalizacion.setDspresta(rs.getString("dspresta"));
			avisoHospitalizacion.setFeingreso(Utils.formateaFecha(rs.getString("feingreso")));
			avisoHospitalizacion.setComentario(rs.getString("dsobserv"));
			return avisoHospitalizacion;
		}
	}

	@Override
	public String enviarAvisoHospitalizacion(AvisoHospitalizacionVO aviso)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdperson_i", aviso.getCdperson());
		params.put("pv_nmpoliza_i", aviso.getNmpoliza());
		params.put("pv_cdagente_i", aviso.getCdagente());
		params.put("pv_cdpresta_i", aviso.getCdpresta());
		params.put("pv_feingreso_i", StringUtils.isBlank(aviso.getFeingreso()) ? null : renderFechas.parse(aviso.getFeingreso()));
		params.put("pv_cdusuari_i", aviso.getCdusuari());
		params.put("pv_dsobserv_i", aviso.getComentario());
		Map<String, Object> mapResult = ejecutaSP(new enviarAvisoHospitalizacionSP(getDataSource()), params);
		return (String) mapResult.get("pv_cdaviso_o");
	}
	
	public class enviarAvisoHospitalizacionSP extends StoredProcedure{
		protected enviarAvisoHospitalizacionSP(DataSource dataSource){
			super(dataSource, "PKG_SATELITES2.P_Guarda_Aviso_Hosp");
			declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdagente_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdpresta_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_feingreso_i", OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_dsobserv_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdusuari_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_cdaviso_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
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
		params.put("pv_cdaviso_i", iCodAviso);
		ejecutaSP(new actualizaEstatusEnvioSP(getDataSource()), params);
	}
	
	public class actualizaEstatusEnvioSP extends StoredProcedure{
		protected actualizaEstatusEnvioSP(DataSource dataSource){
			super(dataSource, "PKG_SATELITES2.P_Actualiza_Estatus_Envio");
			declareParameter(new SqlParameter("pv_cdaviso_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public List<SolicitudCxPVO> obtieneListadoSolicitudesCxp() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}