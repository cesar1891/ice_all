package mx.com.gseguros.ws.recibossigs.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import mx.com.aon.kernel.service.KernelManagerSustituto;
import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.exception.WSException;
import mx.com.gseguros.portal.documentos.model.Documento;
import mx.com.gseguros.portal.documentos.service.DocumentosManager;
import mx.com.gseguros.portal.emision.model.DatosRecibosDxNVO;
import mx.com.gseguros.portal.general.util.TipoSituacion;
import mx.com.gseguros.portal.general.util.TipoTramite;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.ws.model.WrapperResultadosWS;
import mx.com.gseguros.ws.recibossigs.client.axis2.GeneradorReciboDxnWsServiceStub;
import mx.com.gseguros.ws.recibossigs.client.axis2.GeneradorReciboDxnWsServiceStub.CalendarioEntidad;
import mx.com.gseguros.ws.recibossigs.client.axis2.GeneradorReciboDxnWsServiceStub.Empleado;
import mx.com.gseguros.ws.recibossigs.client.axis2.GeneradorReciboDxnWsServiceStub.GeneraRecDxn;
import mx.com.gseguros.ws.recibossigs.client.axis2.GeneradorReciboDxnWsServiceStub.GeneraRecDxnE;
import mx.com.gseguros.ws.recibossigs.client.axis2.GeneradorReciboDxnWsServiceStub.GeneraRecDxnResponseE;
import mx.com.gseguros.ws.recibossigs.client.axis2.GeneradorReciboDxnWsServiceStub.GeneradorRecibosDxnRespuesta;
import mx.com.gseguros.ws.recibossigs.client.axis2.GeneradorReciboDxnWsServiceStub.PolizaEntidad;
import mx.com.gseguros.ws.recibossigs.client.axis2.callback.impl.GeneradorReciboDxnWsServiceCallbackHandlerImpl;
import mx.com.gseguros.ws.recibossigs.service.RecibosSigsService;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Implementaci�n de los m�todos para invocar al WS recibossigs
 * @author Ricardo
 *
 */
public class RecibosSigsServiceImpl implements RecibosSigsService {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(RecibosSigsServiceImpl.class);
	
	private static final long WS_TIMEOUT =  20000;
	
	private String endpoint;
	
	private String urlImpresionRecibos;
	
	private KernelManagerSustituto kernelManager;
	
	@Autowired
	private DocumentosManager documentosManager;

	/**
	 * Recibos DXN solo se utiliza en emision para obtener los calendarios en la emision, luego de esto en endosos se utiliza el WS de Recibos normal 
	 */
	public boolean generaRecibosDxN(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem, String sucursal, String nmsolici, String ntramite, UserVO userVO){
		
		logger.debug("*** Entrando a metodo Genera Recibos DxN, para la poliza: " + nmpoliza + " cdunieco: " + cdunieco + "***");
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i", cdramo);
		params.put("pv_estado_i", estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_nmsuplem_i", nmsuplem);
		params.put("pv_ntramite_i", ntramite);
		
		WrapperResultados result = null;
		DatosRecibosDxNVO datosRecDxN = null;
		WrapperResultadosWS resultWS = null;
		
		try {
			result = kernelManager.obtenDatosRecibosDxN(params);
			ArrayList<DatosRecibosDxNVO> listDatos = (ArrayList<DatosRecibosDxNVO>) result.getItemList();
			datosRecDxN = listDatos.get(0);
		} catch (Exception e1) {
			logger.error("Error en llamar al PL de obtencion de Datos para recibos DxN",e1);
			return false;
		}
		
		GeneradorRecibosDxnRespuesta calendarios = null;
		
		try{
			Empleado empleado =  new Empleado(); 
			empleado.setAdministradoraEmp(Integer.parseInt(datosRecDxN.getAdministradoraEmp()));
			empleado.setClaveEmp(datosRecDxN.getClaveEmp());
			empleado.setCurpEmp(datosRecDxN.getCurpEmp());
			empleado.setDepartamentoEmp(Integer.parseInt(datosRecDxN.getDepartamentoEmp()));
			empleado.setMaternoEmp(datosRecDxN.getMaternoEmp());
			empleado.setNombreEmp(datosRecDxN.getNombreEmp());
			empleado.setPaternoEmp(datosRecDxN.getPaternoEmp());
			empleado.setRetenedoraEmp(Integer.parseInt(datosRecDxN.getRetenedoraEmp()));
			empleado.setRfcEmp(datosRecDxN.getRfcEmp());
			
			PolizaEntidad polizaEnt = new PolizaEntidad();
			polizaEnt.setAdministradoraEmp(Integer.parseInt(datosRecDxN.getAdministradoraEmp()));
			polizaEnt.setClaveDescuento(datosRecDxN.getClaveDescuento());
			polizaEnt.setClaveEmp(datosRecDxN.getClaveEmp());
			polizaEnt.setImpCob(Double.parseDouble(datosRecDxN.getImpCob()));
			polizaEnt.setNumeroAgente(Integer.parseInt(datosRecDxN.getNumeroAgente()));
			polizaEnt.setNumeroApoderado(Integer.parseInt(datosRecDxN.getNumeroApoderado()));
			polizaEnt.setNumPag(datosRecDxN.getNumPag());
			polizaEnt.setNumRel(Integer.parseInt(datosRecDxN.getNumRel()));
			polizaEnt.setPolizaEmi(Integer.parseInt(datosRecDxN.getPolizaEmi()));
			polizaEnt.setRamoEmi(Integer.parseInt(datosRecDxN.getRamoEmi()));
			polizaEnt.setRenovacionAutomatica(datosRecDxN.getRenovacionAutomatica());
			polizaEnt.setRetenedoraEmp(Integer.parseInt(datosRecDxN.getRetenedoraEmp()));
			polizaEnt.setSucursalEmi(Integer.parseInt(datosRecDxN.getSucursalEmi()));
			
			resultWS = generarRecibosDxNGS(empleado, polizaEnt, endpoint, null, false);
			calendarios = (GeneradorRecibosDxnRespuesta) resultWS.getResultadoWS();
			logger.debug("XML de entrada: " + resultWS.getXmlIn());
			
		}catch(WSException e){
			logger.error("Error al generar los datos de Recibos DxN: " + e.getMessage()
					+ " Guardando en bitacora el error, getCause: " + e.getCause(),e);
			logger.error("Imprimpriendo el xml enviado al WS: Payload: " + e.getPayload());
			
			try {
				
				kernelManager.movBitacobro((String) params.get("pv_cdunieco_i"), (String) params.get("pv_cdramo_i"),
						(String) params.get("pv_estado_i"), (String) params.get("pv_nmpoliza_i"), (String) params.get("pv_nmsuplem_i"), 
						RecibosSigsService.TipoError.ErrWsDXNCx.getCodigo(), "Msg: " + e.getMessage() + " ***Cause: " + e.getCause(), userVO.getUser(), (String) params.get("pv_ntramite_i"),
						"ws.recibossigs.url", "generaRecDxn",  e.getPayload(), null);
			} catch (Exception e1) {
				logger.error("Error al insertar en Bitacora", e1);
			}
			
			return false;
		}catch (Exception e){
			logger.error("Error al generar los datos de Recibos DxN: " + e.getMessage()
				+ " Guardando en bitacora el error, getCause: " + e.getCause(),e);
		}
		
		
		if (Estatus.EXITO.getCodigo() != calendarios.getCodigo()) {
			logger.error("Guardando en bitacora el estatus");

			try {
				kernelManager.movBitacobro((String) params.get("pv_cdunieco_i"), (String) params.get("pv_cdramo_i"),
						(String) params.get("pv_estado_i"), (String) params.get("pv_nmpoliza_i"), (String) params.get("pv_nmsuplem_i"),
						RecibosSigsService.TipoError.ErrWsDXN.getCodigo(), calendarios.getCodigo() + " - " + calendarios.getMensaje(), userVO.getUser(), (String) params.get("pv_ntramite_i"),
						"ws.recibossigs.url", "generaRecDxn", resultWS.getXmlIn(), Integer.toString(calendarios.getCodigo()));
			} catch (Exception e1) {
				logger.error("Error al insertar en Bitacora", e1);
			}
			return false;
		}else{
			guardaCalendariosDxnFinaliza(cdunieco, cdramo, estado, nmpoliza, nmsuplem, sucursal, nmsolici, ntramite, calendarios);
		}
		
		return true;
	}
	
	public boolean guardaCalendariosDxnFinaliza(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem, String sucursal, String nmsolici, String ntramite, GeneradorRecibosDxnRespuesta calendarios){
		
		if(calendarios == null) return false;
		
		String cdtipsit = null;
		String cdtipsitGS = null;
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i", cdramo);
		params.put("pv_estado_i", estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_nmsuplem_i", nmsuplem);
		
		logger.debug("********* Total de calendarios *********** : "+calendarios.getCalendariosEntidad().length);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		int cont = 0;
		
		for(CalendarioEntidad cal : calendarios.getCalendariosEntidad()){
			cont++;
			
			logger.debug(">>>Calendario: "+cal.getPeriodo());
			logger.debug(">>>Dia Inicio: "+cal.getFechaIncio().get(Calendar.DAY_OF_MONTH));
			
			String fechaCorte = null;
			String fechaEmision = null;
			String fechaStatus = null;
			String fechaInicio = null;
			String fechaTermino = null;
			
			
			/**
			 * COMMENT: Se ha obtenido el valor del calendario original pasandolo a otro calendario ya que el original venia incompleto y al hacer un getTime
			 * 			Regresaba un Date Erroneo
			 */
			
			Calendar calendar =  Calendar.getInstance();
			
			if(cal.getFechaCorte() != null){
				calendar.set(cal.getFechaCorte().get(Calendar.YEAR), cal.getFechaCorte().get(Calendar.MONTH), cal.getFechaCorte().get(Calendar.DAY_OF_MONTH));
				fechaCorte = sdf.format(calendar.getTime());
			}
			if(cal.getFechaEmision() != null){
				calendar.set(cal.getFechaEmision().get(Calendar.YEAR), cal.getFechaEmision().get(Calendar.MONTH), cal.getFechaEmision().get(Calendar.DAY_OF_MONTH));
				fechaEmision = sdf.format(calendar.getTime());
			}
			if(cal.getFechaEstatus() != null){
				calendar.set(cal.getFechaEstatus().get(Calendar.YEAR), cal.getFechaEstatus().get(Calendar.MONTH), cal.getFechaEstatus().get(Calendar.DAY_OF_MONTH));
				fechaStatus = sdf.format(calendar.getTime());
			}
			if(cal.getFechaIncio() != null){
				calendar.set(cal.getFechaIncio().get(Calendar.YEAR), cal.getFechaIncio().get(Calendar.MONTH), cal.getFechaIncio().get(Calendar.DAY_OF_MONTH));
				fechaInicio = sdf.format(calendar.getTime());
			}
			if(cal.getFechaTermino() != null){
				calendar.set(cal.getFechaTermino().get(Calendar.YEAR), cal.getFechaTermino().get(Calendar.MONTH), cal.getFechaTermino().get(Calendar.DAY_OF_MONTH));
				fechaTermino = sdf.format(calendar.getTime());
			}
			
			params.put("pi_ADMINISTRADORA", cal.getAdministradora());
			params.put("pi_ANIO", cal.getAnho());
			params.put("pi_ESTATUS", cal.getEstatus());
			params.put("Pi_FECHACORTE", fechaCorte);
			params.put("pi_FECHAEMISION", fechaEmision);
			params.put("pi_FECHASTATUS", fechaStatus);
			params.put("pi_FECHAINICIO", fechaInicio);
			params.put("pi_FECHATERMINO", fechaTermino);
			params.put("pi_HORAEMISION", cal.getHoraEmision());
			params.put("pi_PERIODO", cal.getPeriodo());
			params.put("pi_RETENEDORA", cal.getRetenedora());
			
			try {
				kernelManager.guardaPeriodosDxN(params);
				cdtipsit = kernelManager.obtenCdtipsit(params);
				cdtipsitGS = kernelManager.obtenCdtipsitGS(params);
			} catch (Exception e) {
				logger.error("Error en llamado a PL", e);
			}
			
			/**
			 * Se salta el primer calendario porque ya se grabo el primer recibo en el WS de Recibos normal
			 */
			if(cont == 1)continue;
			
			int modalidad = 0;
			if(StringUtils.isNotBlank(cdtipsit)){
				if(TipoSituacion.SALUD_VITAL.getCdtipsit().equalsIgnoreCase(cdtipsit)){
					modalidad = 1;
				}else if(TipoSituacion.SALUD_NOMINA.getCdtipsit().equalsIgnoreCase(cdtipsit)){
					modalidad = 2;
				}
			}
			
			/**
			 * La url no contempla endosos pues es solo para la emision.
			 */
			//String parametros = "?9999,"+modalidad+","+sucursal+","+cdtipsitGS+","+nmpoliza+",0,0,,"+cont; // PARAMS RECIBO ANTERIORES
			String parametros = "?"+sucursal+","+cdtipsitGS+","+nmpoliza+",,0,"+cont;
			
			logger.debug("URL Generada para Recibo: "+ urlImpresionRecibos + parametros);
			
			//HashMap<String, Object> paramsR =  new HashMap<String, Object>();
			//paramsR.put("pv_cdunieco_i", cdunieco);
			//paramsR.put("pv_cdramo_i", cdramo);
			//paramsR.put("pv_estado_i", estado);
			//paramsR.put("pv_nmpoliza_i", nmpoliza);
			//paramsR.put("pv_nmsuplem_i", nmsuplem);
			//paramsR.put("pv_feinici_i", new Date());
			//paramsR.put("pv_cddocume_i", urlImpresionRecibos + parametros);
			//paramsR.put("pv_dsdocume_i", "Recibo "+cont);
			//paramsR.put("pv_nmsolici_i", nmsolici);
			//paramsR.put("pv_ntramite_i", ntramite);
			//paramsR.put("pv_tipmov_i", "1");
			//paramsR.put("pv_swvisible_i", Constantes.NO);
			
			try{
				//kernelManager.guardarArchivo(paramsR);
				
				documentosManager.guardarDocumento(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,nmsuplem
						,new Date()
						,urlImpresionRecibos + parametros
						,"Recibo "+cont
						,nmsolici
						,ntramite
						,"1"
						,Constantes.NO
						,null
						,TipoTramite.POLIZA_NUEVA.getCdtiptra()
						,"0"
						,Documento.RECIBO
						,null
						,null, false
						);
				
			} catch (Exception e) {
				logger.error("Error en llamado a PL", e);
			}
		}
		
		try {
			kernelManager.lanzaProcesoDxN(params);
		} catch (Exception e) {
			logger.error("Error en llamado a PL", e);
			return false;
		}
		
		return true;
	}

	
	/**
	 * Genera cliente WS e invoca el metodo generaRecDxn
	 * @param empleado
	 * @param polizaEntidad
	 * @param endpoint
	 * @param params
	 * @param async
	 * @return
	 * @throws Exception
	 */
	private WrapperResultadosWS generarRecibosDxNGS(Empleado empleado,
			PolizaEntidad polizaEntidad, String endpoint,
			HashMap<String, Object> params, boolean async) throws Exception {
		
		GeneradorRecibosDxnRespuesta resultado = null;
		WrapperResultadosWS resWS = new WrapperResultadosWS();
		GeneradorReciboDxnWsServiceStub stubGS = null;
		
		try {
			logger.info(new StringBuffer("endpoint a invocar=").append(endpoint));
			stubGS = new GeneradorReciboDxnWsServiceStub(endpoint);
		} catch (AxisFault e) {
			logger.error(e);
			throw new Exception("Error de preparacion de Axis2: " + e.getMessage());
		}
		stubGS._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
		
		GeneraRecDxnResponseE RespuestaGS = null;
		
		GeneraRecDxn generaRecDxn =  new GeneraRecDxn();
		generaRecDxn.setArg0(empleado);
		generaRecDxn.setArg1(polizaEntidad);
		
		GeneraRecDxnE generaRecDxnE = new GeneraRecDxnE(); 
		generaRecDxnE.setGeneraRecDxn(generaRecDxn);
		
		try {
			if(async){
				
				//TODO: RBS Cambiar params por PolizaVO
				//Se genera una nueva instancia en cada llamado, para evitar corrupcion de datos en el handler:
				WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext());
				GeneradorReciboDxnWsServiceCallbackHandlerImpl callback = (GeneradorReciboDxnWsServiceCallbackHandlerImpl)context.getBean("generadorReciboDxnWsServiceCallbackHandlerImpl");
				// Se setean los parametros al callback handler:
				params.put("STUB", stubGS);
				callback.setClientData(params);
				
				stubGS.startgeneraRecDxn(generaRecDxnE, callback);
			} else {
				RespuestaGS = stubGS.generaRecDxn(generaRecDxnE);
				resultado = RespuestaGS.getGeneraRecDxnResponse().get_return();
				resWS.setResultadoWS(resultado);
				resWS.setXmlIn(stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
				logger.debug("Resultado para ejecucion de WS generarRecibosDxNGS: "+resultado.getCodigo()+" - "+resultado.getMensaje());
			}
		} catch (Exception re) {
			throw new WSException("Error de conexion: " + re.getMessage(), re, stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
		}
		
		return resWS;
	}
	

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}


	public void setUrlImpresionRecibos(String urlImpresionRecibos) {
		this.urlImpresionRecibos = urlImpresionRecibos;
	}


	public void setKernelManager(KernelManagerSustituto kernelManager) {
		this.kernelManager = kernelManager;
	}
	
	
	
}