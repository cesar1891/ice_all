package mx.com.gseguros.portal.renovacion.service;

import java.util.List;
import java.util.Map;

import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.portal.consultas.model.RecuperacionSimple;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaImapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSlistVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaVoidVO;

public interface RenovacionManager
{
	public ManagerRespuestaImapVO  pantallaRenovacion(String cdsisrol);
	public ManagerRespuestaSlistVO buscarPolizasRenovables(String cdunieco,String cdramo,String anio,String mes);
	public ManagerRespuestaVoidVO  renovarPolizas(
			List<Map<String,String>>polizas
			,String cdusuari
			,String anio
			,String mes
			,String rutaDocumentosPoliza
			,String rutaServidorReports
			,String passServidorReports
			,UserVO usuario
			);
	public ManagerRespuestaImapVO pantallaRenovacionIndividual(String cdsisrol) throws Exception;
	
	public ManagerRespuestaSlistVO buscarPolizasRenovacionIndividual(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String cdusuari,
			String cdsisrol
			) throws Exception;
	public ManagerRespuestaSlistVO buscarPolizasRenovacionIndividualMasiva(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String cdtipsit,
			String fecini,
			String fecfin,
			String status,
			String cdperson,
			String retenedora,
			String cdusuari,
			String cdsisrol
			) throws Exception;
	public ManagerRespuestaSlistVO renuevaPolizaIndividual(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String usuario,
			String feefecto,
			String feproren,
			String estadoNew,
			String cdmoneda) throws Exception;
	
	public void actualizaValoresCotizacion(
			Map<String, String> valores, 
			String cdelemen, 
			String cdusuari, 
			String cdtipsup) throws Exception;
	
	public Map<String, String> confirmarCotizacion(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			String ntramite,
			String cdperpag,
			String feefecto,
			UserVO usuario,
			String rutaDocumentosPoliza) throws Exception;
	
	public void generaTcartera(
			String cdunieco,
			String cdramo,
			String nmpoliza,
			String feefecto,
			String feefecto_ant,
			String nmsuplem,
			String cdagente,
			String cdperpag,
			String cdcontra,
			String cdmoneda) throws Exception;
	
	public String obtenerItemsTatripol(
			String cdramo,
			String cdtipsit) throws Exception;
	
	public void renovarPolizasMasivasIndividuales(
			List<Map<String,String>> slist) throws Exception;
	
	public List<Map<String, String>> obtenerCondicionesRenovacionIndividual(
			String nmperiod,
			String cdunieco,
			String cdramo,
			String anio,
			String mes) throws Exception;
	
	public void movimientoCondicionesRenovacionIndividual(
			String nmperiod,
			String cdunieco,
			String cdramo,
			String anio,
			String mes,
			String criterio,
			String campo,
			String valor,
			String valor2,
			String operacion) throws Exception;
	
	public List<Map<String, String>> obtenerCalendarizacionRenovacionIndividual(
			String anio,
			String mes) throws Exception;
	
	public void movimientoCalendarizacionRenovacionIndividual(
			String nmperiod,
			String anio,
			String mes,
			String cdunieco,
			String cdramo,
			String feinicio,
			String fefinal,
			String feaplica,
			String operacion) throws Exception;
	
	public String validaValorExclusion(String criterio, String valor) throws Exception;
	/*
	 * 
	 */
	public String renovacionColectivo(
			String cdusuari,
			String cdunieco,
			String cdramo,
			String nmpoliza,
			String fecdesde,
			String fechasta,
			String procedimiento)throws Exception;
}