package mx.com.gseguros.portal.reclamoExpress.dao.impl;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.general.model.BaseVO;
import mx.com.gseguros.portal.reclamoExpress.dao.ReclamoExpressDAO;
import mx.com.gseguros.portal.reclamoExpress.model.ReclamoExpressDetalleVO;
import mx.com.gseguros.portal.reclamoExpress.model.ReclamoExpressVO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.object.StoredProcedure;

public class ReclamoExpressDAOSIGSImpl extends AbstractManagerDAO implements ReclamoExpressDAO {

	@Override
	public List<BaseVO> obtieneReclamos() throws Exception {
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		Map<String, Object> mapResult = ejecutaSP(new ConsultaReclamosSP(getDataSource()), params);
		return (List<BaseVO>) mapResult.get("rs");
	}
	
	public class ConsultaReclamosSP extends StoredProcedure{
		protected ConsultaReclamosSP(DataSource dataSource){
			super(dataSource, "p_get_reclaexpress");
			/*Aqui se colocan los parametros conforme se requieran en el SP*/
			/*declareParameter(new SqlOutParameter("pv_msg_id_o", Types.INTEGER));
			declareParameter(new SqlOutParameter("pv_title_o", Types.VARCHAR));*/
			declareParameter(new SqlReturnResultSet("rs", new ReclamosMapper()));
			compile();
		}
	}
	
	public class ReclamosMapper implements RowMapper<BaseVO>{
		public BaseVO mapRow(ResultSet rs, int rowNum) throws SQLException{
			BaseVO reclamos = new BaseVO();			
			reclamos.setKey(rs.getString("icodreclamo"));
			reclamos.setValue(rs.getString("icodreclamo"));
			return reclamos;
		}
	}
	
	@Override
	public List<BaseVO> obtieneSecuenciales(int reclamo) throws Exception {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_reclamo_i", reclamo);
		Map<String, Object> mapResult = ejecutaSP(new ConsultaSecuencialesSP(getDataSource()), params);
		return (List<BaseVO>) mapResult.get("rs");
	}
	
	public class ConsultaSecuencialesSP extends StoredProcedure{
		protected ConsultaSecuencialesSP(DataSource dataSource){
			super(dataSource, "p_get_reclaexpress_secuencial");
			/*Aqui se colocan los parametros conforme se requieran en el SP*/
			declareParameter(new SqlParameter("pv_reclamo_i", Types.INTEGER));
			declareParameter(new SqlReturnResultSet("rs", new SecuencialesMapper()));
			compile();
		}
	}
	
	public class SecuencialesMapper implements RowMapper<BaseVO>{
		public BaseVO mapRow(ResultSet rs, int rowNum) throws SQLException{
			BaseVO secuenciales = new BaseVO();			
			secuenciales.setKey(rs.getString("tisecuencialafi"));
			secuenciales.setValue(rs.getString("tisecuencialafi"));
			return secuenciales;
		}
	}

	@Override
	public List<ReclamoExpressVO> obtieneReclamoExpress(int reclamo,
			int secuencial) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_reclamo_i", reclamo);
		params.put("pv_secuencial_i", secuencial);
		Map<String, Object> mapResult = ejecutaSP(new ConsultaReclamoExpressSP(getDataSource()), params);
		return (List<ReclamoExpressVO>) mapResult.get("rs");
	}
	
	public class ConsultaReclamoExpressSP extends StoredProcedure{
		protected ConsultaReclamoExpressSP(DataSource dataSource){
			super(dataSource, "p_get_reclamoexpress");
			/*Aqui se colocan los parametros conforme se requieran en el SP*/
			declareParameter(new SqlParameter("pv_reclamo_i", Types.INTEGER));
			declareParameter(new SqlParameter("pv_secuencial_i", Types.INTEGER));
			declareParameter(new SqlReturnResultSet("rs", new ReclamoExpressMapper()));
			compile();
		}
	}
	
	public class ReclamoExpressMapper implements RowMapper<ReclamoExpressVO>{
		public ReclamoExpressVO mapRow(ResultSet rs, int rowNum) throws SQLException{
			ReclamoExpressVO reclamoExpress = new ReclamoExpressVO();
			reclamoExpress.setReclamo(rs.getString("RECLAMO"));
			reclamoExpress.setSecuencial(rs.getString("SECUENCIAL"));
			reclamoExpress.setFechaCaptura(rs.getString("FECHA_CAPTURA"));			
			reclamoExpress.setSucursal(rs.getString("SUCURSAL"));			
			reclamoExpress.setSucursalNombre(rs.getString("SUCURSAL_NOMBRE"));
			reclamoExpress.setRamo(rs.getString("RAMO"));
			reclamoExpress.setPoliza(rs.getString("POLIZA"));
			reclamoExpress.setCliente(rs.getString("CLIENTE").trim());
			reclamoExpress.setIdAsegurado(rs.getString("ID_ASEGURADO"));			
			reclamoExpress.setAsegurado(rs.getString("ASEGURADO").trim());
			reclamoExpress.setFactura(rs.getString("FACTURA").trim());
			reclamoExpress.setFechaFactura(rs.getString("FECHA_FACTURA"));
			reclamoExpress.setImporte(rs.getString("IMPORTE"));
			reclamoExpress.setIva(rs.getString("IVA"));
			reclamoExpress.setIvaRetenido(rs.getString("IVA_RETENIDO"));
			reclamoExpress.setIsr(rs.getString("ISR"));
			reclamoExpress.setIdProveedor(rs.getString("ID_PROVEEDOR"));
			reclamoExpress.setProveedor(rs.getString("PROVEEDOR").trim());
			reclamoExpress.setProveedorRfc(rs.getString("PROVEEDOR_RFC").trim());
			reclamoExpress.setFechaProcesamiento(rs.getString("FECHA_PROCESAMIENTO"));
			reclamoExpress.setTipoReclamo(rs.getString("TIPO_RECLAMO").trim());
			reclamoExpress.setSiniestro(rs.getString("SINIESTRO"));
			reclamoExpress.setSiniestroSerie(rs.getString("SINIESTRO_SERIE"));
			reclamoExpress.setFechaPago(rs.getString("FECHA_PAGO"));
			reclamoExpress.setDestino(rs.getString("DESTINO"));
			reclamoExpress.setDestinoNombre(rs.getString("DESTINO_NOMBRE").trim());
			reclamoExpress.setFechaAplicacion(rs.getString("FECHA_APLICACION"));
			reclamoExpress.setIdTipoServicio(rs.getString("ID_TIPO_SERVICIO"));
			reclamoExpress.setTipoServicio(rs.getString("TIPO_SERVICIO").trim());
			reclamoExpress.setSolicitudCxp(rs.getString("SOLICITUD_CXP").trim());
			reclamoExpress.setTipoPago(rs.getString("TIPO_PAGO").trim());
			reclamoExpress.setReferencia(rs.getString("REFERENCIA").trim());
			reclamoExpress.setModo(rs.getString("MODO"));
			
			this.generaClavePoliza(reclamoExpress);
			this.generaClaveReclamo(reclamoExpress);

			return reclamoExpress;
		}
		
		//M�todo para generar la clave de la p�liza
		public void generaClavePoliza(ReclamoExpressVO recla){
			String clavePoliza = "";
			//Sucursal
			if(recla.getSucursal() != null){
				clavePoliza += StringUtils.leftPad(recla.getSucursal(), 4, "0");
			} else {
				clavePoliza += "0000";				
			}		
			//Reclamo
			if(recla.getRamo() != null){
				clavePoliza += StringUtils.leftPad(recla.getRamo(), 3, "0");
			} else {
				clavePoliza += "000";
			}
			//Poliza
			if(recla.getPoliza() != null){
				clavePoliza += StringUtils.leftPad(recla.getPoliza(), 6, "0");
			} else {
				clavePoliza += "000000";
			}
			//Renovaci�n
			clavePoliza += "000";
			
			recla.setClavePoliza(clavePoliza);
		}
		
		//M�todo para generar la clave del reclamo
		public void generaClaveReclamo(ReclamoExpressVO recla){
			String claveReclamo = "";
			//Reclamo
			if(recla.getReclamo() != null){
				claveReclamo += StringUtils.leftPad(recla.getReclamo(), 9, "0");
			} else {
				claveReclamo += "000000000";
			}
			//Secuencial
			if(recla.getSecuencial() != null){
				claveReclamo += StringUtils.leftPad(recla.getSecuencial(), 5, "0");
			} else {
				claveReclamo += "00000";
			}
				
			recla.setClaveReclamo(claveReclamo);
		}
	}

	@Override
	public boolean guardaReclamoExpress(ReclamoExpressVO reclamoExpress)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>(); 
		
		//Objetos requeridos para el formateo de fechas. NO ELIMINAR.
		SimpleDateFormat sdfAux = new SimpleDateFormat("dd/MM/yyyy");		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
		params.put("pv_fecpro_i", 
					sdf.format(
							sdfAux.parse(reclamoExpress.getFechaProcesamiento())
					)
				);
		params.put("pv_cvepol_i", reclamoExpress.getClavePoliza());
		params.put("pv_cverec_i", reclamoExpress.getClaveReclamo());
		params.put("pv_sucemi_i", reclamoExpress.getSucursal());
		params.put("pv_ramemi_i", reclamoExpress.getRamo());
		params.put("pv_numpol_i", reclamoExpress.getPoliza());
		params.put("pv_cvecli_i", reclamoExpress.getIdCliente());
		params.put("pv_nomcli_i", reclamoExpress.getCliente());
		params.put("pv_ideafi_i", reclamoExpress.getIdSESAS());
		params.put("pv_cveafi_i", reclamoExpress.getIdAsegurado());
		params.put("pv_nomafi_i", reclamoExpress.getAsegurado());
		params.put("pv_tipadm_i", reclamoExpress.getConducto());
		params.put("pv_cvedoc_i", reclamoExpress.getIdTipoServicio());
		params.put("pv_tipdoc_i", reclamoExpress.getTipoServicio());
		params.put("pv_atehos_i", reclamoExpress.getAtencionHosp());
		params.put("pv_caurec_i", reclamoExpress.getCausaReclamo());
		params.put("pv_cvedia_i", reclamoExpress.getIdIcd());
		params.put("pv_nomdia_i", reclamoExpress.getIcdNombre());
		params.put("pv_prouno_i", reclamoExpress.getProcedimiento1());
		params.put("pv_prodos_i", reclamoExpress.getProcedimiento2());
		params.put("pv_protre_i", reclamoExpress.getProcedimiento3());
		params.put("pv_fecing_i", 
					sdf.format(
							sdfAux.parse(reclamoExpress.getFechaIngreso())
					)
				);
		params.put("pv_fecalt_i", 
					sdf.format(
							sdfAux.parse(reclamoExpress.getFechaAlta())
					)
				);
		params.put("pv_motegr_i", reclamoExpress.getMotivoEgreso());
		params.put("pv_feccap_i", 
					sdf.format(
							sdfAux.parse(reclamoExpress.getFechaCaptura())
					)
				
				);		
		params.put("pv_tiprec_i", reclamoExpress.getTipoReclamo());
		params.put("pv_cveprv_i", reclamoExpress.getIdProveedor());
		params.put("pv_rfcprv_i", reclamoExpress.getProveedorRfc());
		params.put("pv_nomprv_i", reclamoExpress.getProveedor());
		params.put("pv_numfac_i", reclamoExpress.getFactura());
		params.put("pv_fecfac_i", 
					sdf.format(
							sdfAux.parse(reclamoExpress.getFechaFactura())
					)
				);
		params.put("pv_imppag_i", reclamoExpress.getImporte());
		params.put("pv_fecpag_i", 
					sdf.format(
							sdfAux.parse(reclamoExpress.getFechaPago())
					)
				);
		
		params.put("pv_sucpag_i", reclamoExpress.getDestino());
		params.put("pv_tippag_i", reclamoExpress.getTipoPago());
		params.put("pv_refpag_i", reclamoExpress.getReferencia());
		params.put("pv_fecapl_i", 
				sdf.format(
						sdfAux.parse(reclamoExpress.getFechaAplicacion())
				)
			);
		params.put("pv_numsin_i", reclamoExpress.getSiniestro());
		params.put("pv_numsol_i", reclamoExpress.getSolicitudCxp());
		
		Map<String, Object> mapResult = ejecutaSP(new GuardaReclamoExpressSP(getDataSource()), params);
		
		//return (boolean) Boolean.parseBoolean(mapResult.get("EXITO").toString());
		return true;
	}
	
		
	public class GuardaReclamoExpressSP extends StoredProcedure{
		protected GuardaReclamoExpressSP(DataSource dataSource){
			super(dataSource, "p_insert_reclamoexpress");
			/*Aqui se colocan los parametros conforme se requieran en el SP*/
			declareParameter(new SqlParameter("pv_fecpro_i", Types.DATE));
			declareParameter(new SqlParameter("pv_cvepol_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_cverec_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_sucemi_i", Types.SMALLINT));
			declareParameter(new SqlParameter("pv_ramemi_i", Types.SMALLINT));
			declareParameter(new SqlParameter("pv_numpol_i", Types.INTEGER));
			declareParameter(new SqlParameter("pv_cvecli_i", Types.INTEGER));
			declareParameter(new SqlParameter("pv_nomcli_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_ideafi_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_cveafi_i", Types.INTEGER));
			declareParameter(new SqlParameter("pv_nomafi_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_tipadm_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_cvedoc_i", Types.SMALLINT));
			declareParameter(new SqlParameter("pv_tipdoc_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_atehos_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_caurec_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_cvedia_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_nomdia_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_prouno_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_prodos_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_protre_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_fecing_i", Types.DATE));
			declareParameter(new SqlParameter("pv_fecalt_i", Types.DATE));
			declareParameter(new SqlParameter("pv_motegr_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_feccap_i", Types.DATE));
			declareParameter(new SqlParameter("pv_tiprec_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_cveprv_i", Types.INTEGER));
			declareParameter(new SqlParameter("pv_rfcprv_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_nomprv_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_numfac_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_fecfac_i", Types.DATE));
			declareParameter(new SqlParameter("pv_imppag_i", Types.DECIMAL));
			declareParameter(new SqlParameter("pv_fecpag_i", Types.DATE));
			declareParameter(new SqlParameter("pv_sucpag_i", Types.INTEGER));
			declareParameter(new SqlParameter("pv_tippag_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_refpag_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_fecapl_i", Types.DATE));
			declareParameter(new SqlParameter("pv_numsin_i", Types.INTEGER));
			declareParameter(new SqlParameter("pv_numsol_i", Types.INTEGER));
			
			compile();
		}
	}

	@Override
	public boolean guardaDetalleExpress(
			ReclamoExpressDetalleVO reclamoExpressDetalle) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		
		//Objetos requeridos para el formateo de fechas. NO ELIMINAR.
		SimpleDateFormat sdfAux = new SimpleDateFormat("dd/MM/yyyy");		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		params.put("pv_fecpro_i", 
				sdf.format(
						sdfAux.parse(reclamoExpressDetalle.getFechaProcesamiento())
				)
			);
		params.put("pv_cvepol_i", reclamoExpressDetalle.getClavePoliza());
		params.put("pv_cverec_i", reclamoExpressDetalle.getClaveReclamo());
		params.put("pv_cvedet_i", reclamoExpressDetalle.getClaveDetalle());
		params.put("pv_ideafi_i", reclamoExpressDetalle.getIdSESAS());
		params.put("pv_cveafi_i", reclamoExpressDetalle.getIdAsegurado());
		params.put("pv_tipprc_i", reclamoExpressDetalle.getTipoProcedimiento());
		params.put("pv_cveprc_i", reclamoExpressDetalle.getProcedimiento());
		params.put("pv_nomprc_i", reclamoExpressDetalle.getProcedimientoNombre().toUpperCase());
		params.put("pv_imppag_i", reclamoExpressDetalle.getImporte());
		params.put("pv_cvesub_i", reclamoExpressDetalle.getSubcobertura());
		//params.put("pv_cvesub_i", 0);
		params.put("pv_nomsub_i", reclamoExpressDetalle.getSubcoberturaNombre().toUpperCase());
		params.put("pv_cvecob_i", reclamoExpressDetalle.getCobertura());
		//params.put("pv_cvecob_i", 0);
		params.put("pv_nomcob_i", reclamoExpressDetalle.getCoberturaNombre().toUpperCase());		
		params.put("pv_nomcla_i", reclamoExpressDetalle.getClasificacion());
		params.put("pv_nomprg_i", reclamoExpressDetalle.getPrograma().toUpperCase());
		
		Map<String, Object> mapResult = ejecutaSP(new GuardaDetalleExpressSP(getDataSource()), params);
		
		return true;
	}
	
	public class GuardaDetalleExpressSP extends StoredProcedure{
		protected GuardaDetalleExpressSP(DataSource dataSource){
			super(dataSource, "p_insert_detalleexpress");
			
			declareParameter(new SqlParameter("pv_fecpro_i", Types.DATE));
			declareParameter(new SqlParameter("pv_cvepol_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_cverec_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_cvedet_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_ideafi_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_cveafi_i", Types.INTEGER));
			declareParameter(new SqlParameter("pv_tipprc_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_cveprc_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_nomprc_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_imppag_i", Types.DECIMAL));
			declareParameter(new SqlParameter("pv_cvesub_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_nomsub_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_cvecob_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_nomcob_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_nomcla_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_nomprg_i", Types.VARCHAR));
			
			compile();
		}
	}

	@Override
	public boolean borraDetalleExpress(
			ReclamoExpressDetalleVO reclamoExpressDetalle) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>(); 
		
		//Objetos requeridos para el formateo de fechas. NO ELIMINAR.
		SimpleDateFormat sdfAux = new SimpleDateFormat("dd/MM/yyyy");		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
		params.put("pv_fecpro_i", 
					sdf.format(
							sdfAux.parse(reclamoExpressDetalle.getFechaProcesamiento())
					)
				);
		params.put("pv_cvepol_i", reclamoExpressDetalle.getClavePoliza());
		params.put("pv_cverec_i", reclamoExpressDetalle.getClaveReclamo());
		
		Map<String, Object> mapResult = ejecutaSP(new BorraDetalleExpressSP(getDataSource()), params);
		
		return true;
	}
	
	public class BorraDetalleExpressSP extends StoredProcedure{
		protected BorraDetalleExpressSP(DataSource dataSource){
			super(dataSource, "p_delete_detalleexpress");
			
			declareParameter(new SqlParameter("pv_fecpro_i", Types.DATE));
			declareParameter(new SqlParameter("pv_cvepol_i", Types.VARCHAR));
			declareParameter(new SqlParameter("pv_cverec_i", Types.VARCHAR));
						
			compile();
		}
	}
}
