package mx.com.gseguros.ws.recibossigs.client.axis2.callback.impl;

import java.util.HashMap;

import mx.com.aon.kernel.service.KernelManagerSustituto;
import mx.com.gseguros.ws.ice2sigs.service.Ice2sigsService.Estatus;
import mx.com.gseguros.ws.recibossigs.client.axis2.GeneradorReciboDxnWsServiceCallbackHandler;
import mx.com.gseguros.ws.recibossigs.client.axis2.GeneradorReciboDxnWsServiceStub;
import mx.com.gseguros.ws.recibossigs.client.axis2.GeneradorReciboDxnWsServiceStub.CalendarioEntidad;
import mx.com.gseguros.ws.recibossigs.client.axis2.GeneradorReciboDxnWsServiceStub.GeneraRecDxnResponseE;
import mx.com.gseguros.ws.recibossigs.client.axis2.GeneradorReciboDxnWsServiceStub.GeneradorRecibosDxnRespuesta;
import mx.com.gseguros.ws.recibossigs.service.RecibosSigsService;

import org.apache.axis2.AxisFault;

public class GeneradorReciboDxnWsServiceCallbackHandlerImpl extends GeneradorReciboDxnWsServiceCallbackHandler {

	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(GeneradorReciboDxnWsServiceCallbackHandlerImpl.class);
	
	private KernelManagerSustituto kernelManager;

	public GeneradorReciboDxnWsServiceCallbackHandlerImpl() {
		super();
	}

	public GeneradorReciboDxnWsServiceCallbackHandlerImpl(Object obj) {
		super(obj);
	}
	
	/**
	 * Agregamos el setter de clientData 
	 * @param obj
	 */
	public void setClientData(Object obj) {
		super.clientData = obj;
	}

	
	@Override
	public void receiveErrorgeneraRecDxn(Exception e) {
		
		logger.error("Error en WS clienteSalud: " + e.getMessage()
				+ " Guardando en bitacora el error, getCause: " + e.getCause(),e);

		HashMap<String, Object> params = (HashMap<String, Object>) this.clientData;

		GeneradorReciboDxnWsServiceStub stubGS = (GeneradorReciboDxnWsServiceStub) params.get("STUB");
		logger.debug("Imprimpriendo el xml enviado al WS: ");
		try {
			logger.debug(stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
		} catch (AxisFault ex) {
			logger.error(ex);
		}
		
		String usuario = null;
		if(params.containsKey("USUARIO")) usuario = (String) params.get("USUARIO");
		
		try {
			kernelManager.movBitacobro((String) params.get("pv_cdunieco_i"),
					(String) params.get("pv_cdramo_i"), (String) params.get("pv_estado_i"),
					(String) params.get("pv_nmpoliza_i"), (String) params.get("pv_nmsuplem_i"),
					RecibosSigsService.TipoError.ErrWsDXNCx.getCodigo(), "Msg: " + e.getMessage() + " ***Cause: " + e.getCause(),
					 usuario, (String) params.get("pv_ntramite_i"), "ws.recibossigs.url", "generaRecDxn",
					 stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString(), null);
		} catch (Exception e1) {
			logger.error("Error en guardado a Bitacora a PL", e1);
		}
	}

	
	@Override
	public void receiveResultgeneraRecDxn(GeneraRecDxnResponseE result) {
		
		logger.debug("Comunicacion exitosa WS generaRecDxn");
		GeneradorRecibosDxnRespuesta respuesta = result.getGeneraRecDxnResponse().get_return();
		logger.debug("Resultado al ejecutar el WS generaRecDxn: " + respuesta.getCodigo() + " - " + respuesta.getMensaje());
		
		HashMap<String, Object> params = (HashMap<String, Object>) this.clientData;
		
		GeneradorReciboDxnWsServiceStub stubGS = (GeneradorReciboDxnWsServiceStub) params.get("STUB");
		logger.debug("Imprimpriendo el xml enviado al WS: ");
		try {
			logger.debug(stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
		} catch (AxisFault e) {
			logger.error(e);
		}

		if (Estatus.EXITO.getCodigo() != respuesta.getCodigo()) {
			
			logger.error("Guardando en bitacora el estatus");

			String usuario = null;
			if(params.containsKey("USUARIO")) usuario = (String) params.get("USUARIO");
			
			try {
				kernelManager.movBitacobro((String) params.get("pv_cdunieco_i"), (String) params.get("pv_cdramo_i"),
						(String) params.get("pv_estado_i"), (String) params.get("pv_nmpoliza_i"), (String) params.get("pv_nmsuplem_i"),
						RecibosSigsService.TipoError.ErrWsDXN.getCodigo(), respuesta.getCodigo() + " - " + respuesta.getMensaje(), usuario, (String) params.get("pv_ntramite_i"), "ws.recibossigs.url", "generaRecDxn",
						stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString(), Integer.toString(respuesta.getCodigo()));
			} catch (Exception e1) {
				logger.error("Error en llamado a PL", e1);
			}
		}else {
			
			for(CalendarioEntidad cal : respuesta.getCalendariosEntidad()){
				logger.debug(">>>Calendario: "+cal.getPeriodo());	
				logger.debug("Fecha inicio: "+cal.getFechaIncio());	
			}
		}

	}

	
	/**
	 * kernelManager setter
	 * @param kernelManager
	 */
	public void setKernelManager(KernelManagerSustituto kernelManager) {
		this.kernelManager = kernelManager;
	}

}