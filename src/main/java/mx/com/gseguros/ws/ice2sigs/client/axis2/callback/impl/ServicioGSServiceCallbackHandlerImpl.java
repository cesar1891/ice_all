package mx.com.gseguros.ws.ice2sigs.client.axis2.callback.impl;

import java.util.HashMap;

import mx.com.aon.kernel.service.KernelManagerSustituto;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceCallbackHandler;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.CcomisionRespuesta;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteGeneralGSResponseE;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteGeneralRespuesta;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteSaludGSResponseE;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteSaludRespuesta;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ReciboGSResponseE;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ReciboRespuesta;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ReclamoGSResponseE;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ReclamoRespuesta;
import mx.com.gseguros.ws.ice2sigs.service.Ice2sigsService;
import mx.com.gseguros.ws.ice2sigs.service.Ice2sigsService.Estatus;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

public class ServicioGSServiceCallbackHandlerImpl extends ServicioGSServiceCallbackHandler {

	private Logger logger = Logger.getLogger(ServicioGSServiceCallbackHandlerImpl.class);
	
	private transient KernelManagerSustituto kernelManager;

	public ServicioGSServiceCallbackHandlerImpl() {
		super();
	}

	public ServicioGSServiceCallbackHandlerImpl(Object obj) {
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
	public void receiveErrorclienteSaludGS(Exception e) {
		logger.error("Error en WS clienteSalud: " + e.getMessage() + " Guardando en bitacora el error, getCause: " + e.getCause(),e);

		HashMap<String, Object> params = (HashMap<String, Object>) this.clientData;
		
		ServicioGSServiceStub stubGS = (ServicioGSServiceStub) params.get("STUB");
		logger.debug("Imprimpriendo el xml enviado al WS: ");
		try {
			logger.debug(stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
		} catch (AxisFault ex) {
			logger.error(ex);
		}
		
		String usuario = (String) params.get("USUARIO");
		try {
			kernelManager.movBitacobro(
					(String) params.get("pv_cdunieco_i"),
					(String) params.get("pv_cdramo_i"),
					(String) params.get("pv_estado_i"),
					(String) params.get("pv_nmpoliza_i"), 
					(String) params.get("pv_nmsuplem_i"), 
					Ice2sigsService.TipoError.ErrWScliCx.getCodigo(), 
					"Msg: " + e.getMessage() + " ***Cause: " + e.getCause(),
					 usuario, (String) params.get("pv_ntramite_i"), "ws.ice2sigs.url", "clienteSaludGS",
					 stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString(), null);
		} catch (Exception e1) {
			logger.error("Error al insertar en bitacora", e1);
		}
	}

	@Override
	public void receiveResultclienteSaludGS(ClienteSaludGSResponseE result) {
		logger.debug("Comunicacion exitosa WS clienteSalud");
		ClienteSaludRespuesta respuesta = result.getClienteSaludGSResponse().get_return();
		logger.debug("Resultado al ejecutar el WS cliente: " + respuesta.getCodigo() + " - " + respuesta.getMensaje());
		
		//TODO: RBS cambiar el param por PolizaVO
		HashMap<String, Object> params = (HashMap<String, Object>) this.clientData;
		
		ServicioGSServiceStub stubGS = (ServicioGSServiceStub) params.get("STUB");
		logger.debug("Imprimpriendo el xml enviado al WS: ");
		try {
			logger.debug(stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
		} catch (AxisFault e) {
			logger.error(e);
		}

		if (Estatus.EXITO.getCodigo() != respuesta.getCodigo() && Estatus.LLAVE_DUPLICADA.getCodigo() != respuesta.getCodigo()) {
			logger.error("Guardando en bitacora el estatus");

			String usuario = (String) params.get("USUARIO");
			try {
				kernelManager.movBitacobro(
						(String) params.get("pv_cdunieco_i"),
						(String) params.get("pv_cdramo_i"),
						(String) params.get("pv_estado_i"),
						(String) params.get("pv_nmpoliza_i"),
						(String) params.get("pv_nmsuplem_i"), 
						Ice2sigsService.TipoError.ErrWScli.getCodigo(),
						respuesta.getCodigo() + " - " + respuesta.getMensaje(),
						usuario, (String) params.get("pv_ntramite_i"), "ws.ice2sigs.url", "clienteSaludGS",
						stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString(), Integer.toString(respuesta.getCodigo()));
			} catch (Exception e1) {
				logger.error("Error al insertar en bitacora", e1);
			}
		}
	}

	@Override
	public void receiveErrorclienteGeneralGS(Exception e) {
		logger.error("Error en WS clienteGeneral: " + e.getMessage() + " Guardando en bitacora el error, getCause: " + e.getCause(),e);
		
		HashMap<String, Object> params = (HashMap<String, Object>) this.clientData;
		
		ServicioGSServiceStub stubGS = (ServicioGSServiceStub) params.get("STUB");
		logger.debug("Imprimpriendo el xml enviado al WS: ");
		try {
			logger.debug(stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
		} catch (AxisFault ex) {
			logger.error(ex);
		}
		
		String usuario = (String) params.get("USUARIO");
		try {
			kernelManager.movBitacobro(
					(String) params.get("pv_cdunieco_i"),
					(String) params.get("pv_cdramo_i"),
					(String) params.get("pv_estado_i"),
					(String) params.get("pv_nmpoliza_i"), 
					(String) params.get("pv_nmsuplem_i"), 
					Ice2sigsService.TipoError.ErrWScliCx.getCodigo(), 
					"Msg: " + e.getMessage() + " ***Cause: " + e.getCause(),
					usuario, (String) params.get("pv_ntramite_i"), "ws.ice2sigs.url", "clienteGeneralGS",
					stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString(), null);
		} catch (Exception e1) {
			logger.error("Error al insertar en bitacora", e1);
		}
	}
	
	@Override
	public void receiveResultclienteGeneralGS(ClienteGeneralGSResponseE result) {
		logger.debug("Comunicacion exitosa WS clienteGeneral");
		ClienteGeneralRespuesta respuesta = result.getClienteGeneralGSResponse().get_return();
		logger.debug("Resultado al ejecutar el WS cliente: " + respuesta.getCodigo() + " - " + respuesta.getMensaje());
		
		//TODO: RBS cambiar el param por PolizaVO
		HashMap<String, Object> params = (HashMap<String, Object>) this.clientData;
		
		ServicioGSServiceStub stubGS = (ServicioGSServiceStub) params.get("STUB");
		logger.debug("Imprimpriendo el xml enviado al WS: ");
		try {
			logger.debug(stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
		} catch (AxisFault e) {
			logger.error(e);
		}
		
		if (Estatus.EXITO.getCodigo() != respuesta.getCodigo() && Estatus.LLAVE_DUPLICADA.getCodigo() != respuesta.getCodigo()) {
			logger.error("Guardando en bitacora el estatus");
			
			String usuario = (String) params.get("USUARIO");
			try {
				kernelManager.movBitacobro(
						(String) params.get("pv_cdunieco_i"),
						(String) params.get("pv_cdramo_i"),
						(String) params.get("pv_estado_i"),
						(String) params.get("pv_nmpoliza_i"),
						(String) params.get("pv_nmsuplem_i"), 
						Ice2sigsService.TipoError.ErrWScli.getCodigo(),
						respuesta.getCodigo() + " - " + respuesta.getMensaje(),
						usuario, (String) params.get("pv_ntramite_i"), "ws.ice2sigs.url", "clienteGeneralGS",
						stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString(), Integer.toString(respuesta.getCodigo()));
			} catch (Exception e1) {
				logger.error("Error al insertar en bitacora", e1);
			}
		}
	}
	

	@Override
	public void receiveErrorreciboGS(Exception e) {
		logger.error("Error en WS Recibo: " + e.getMessage() + " Guardando en bitacora el error, getCause: " + e.getCause(),e);

		HashMap<String, Object> params = (HashMap<String, Object>) this.clientData;

		ServicioGSServiceStub stubGS = (ServicioGSServiceStub) params.get("STUB");
		logger.debug("Imprimpriendo el xml enviado al WS: ");
		try {
			logger.debug(stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
		} catch (AxisFault ex) {
			logger.error(ex);
		}
		
		String usuario = (String) params.get("USUARIO");
		
		try {
			kernelManager.movBitacobro(
					(String) params.get("pv_cdunieco_i"),
					(String) params.get("pv_cdramo_i"),
					(String) params.get("pv_estado_i"),
					(String) params.get("pv_nmpoliza_i"),
					(String) params.get("pv_nmsuplem_i"),
					Ice2sigsService.TipoError.ErrWSrecCx.getCodigo(),
					"Error Recibo " + params.get("NumRec") + " TipEnd: " + params.get("TipEnd") + " NumEnd: " + params.get("NumEnd")
							+ " Msg: " + e.getMessage() + " ***Cause: "
							+ e.getCause(),
					 usuario, (String) params.get("pv_ntramite_i"), "ws.ice2sigs.url", "reciboGS",
					 stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString(), null);
		} catch (Exception e1) {
			logger.error("Error al insertar en Bitacora", e1);
		}
	}

	@Override
	public void receiveResultreciboGS(ReciboGSResponseE result) {
		logger.debug("Comunicacion exitosa WS Recibo Salud");
		ReciboRespuesta respuesta = result.getReciboGSResponse().get_return();
		HashMap<String, Object> params = (HashMap<String, Object>) this.clientData;
		
		logger.debug("Resultado al ejecutar el WS Recibo: " + params.get("NumRec") + " >>>"
				+ respuesta.getCodigo() + " - " + respuesta.getMensaje());
		
		ServicioGSServiceStub stubGS = (ServicioGSServiceStub) params.get("STUB");
		logger.debug("Imprimpriendo el xml enviado al WS: ");
		try {
			logger.debug(stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
		} catch (AxisFault e) {
			logger.error(e);
		}
		
		if (Estatus.EXITO.getCodigo() != respuesta.getCodigo()) {
			logger.error("Guardando en bitacora el estatus..");

			String usuario = (String) params.get("USUARIO");
			
			try {
				kernelManager.movBitacobro((String) params.get("pv_cdunieco_i"),
						(String) params.get("pv_cdramo_i"),
						(String) params.get("pv_estado_i"),
						(String) params.get("pv_nmpoliza_i"),
						(String) params.get("pv_nmsuplem_i"),
						Ice2sigsService.TipoError.ErrWSrec.getCodigo(),
						"Error Recibo " + params.get("NumRec") + " TipEnd: " + params.get("TipEnd") + " NumEnd: " + params.get("NumEnd")
								+ " >>> " + respuesta.getCodigo() + " - "
								+ respuesta.getMensaje(),
						 usuario,(String) params.get("pv_ntramite_i"), "ws.ice2sigs.url", "reciboGS",
						 stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString(), Integer.toString(respuesta.getCodigo()));
			} catch (Exception e1) {
				logger.error("Error al insertar en bitacora", e1);
			}
		}
	}
	
	@Override
	public void receiveErrorcomisionReciboAgenteGS(Exception e){
		logger.error("Error en WS Comision: " + e.getMessage() + " Guardando en bitacora el error, getCause: " + e.getCause(),e);

		HashMap<String, Object> params = (HashMap<String, Object>) this.clientData;

		ServicioGSServiceStub stubGS = (ServicioGSServiceStub) params.get("STUB");
		logger.debug("Imprimpriendo el xml enviado al WS: ");
		try {
			logger.debug(stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
		} catch (AxisFault ex) {
			logger.error(ex);
		}
		
		String usuario = (String) params.get("USUARIO");
		
		try {
			kernelManager.movBitacobro(
					(String) params.get("pv_cdunieco_i"),
					(String) params.get("pv_cdramo_i"),
					(String) params.get("pv_estado_i"),
					(String) params.get("pv_nmpoliza_i"),
					(String) params.get("pv_nmsuplem_i"),
					Ice2sigsService.TipoError.ErrWSrecCx.getCodigo(),
					"Error Comision " + params.get("NumRec") + " Agente: " + params.get("NumAgt") + " NumEnd: " + params.get("NumEnd")
							+ " Msg: " + e.getMessage() + " ***Cause: "
							+ e.getCause(),
					 usuario, (String) params.get("pv_ntramite_i"), "ws.ice2sigs.url", "comisionReciboAgenteGS",
					 stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString(), null);
		} catch (Exception e1) {
			logger.error("Error al insertar en Bitacora", e1);
		}
	}

	@Override
	public void receiveResultcomisionReciboAgenteGS(mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ComisionReciboAgenteGSResponseE result){
		logger.debug("Comunicacion exitosa WS Comision");
		CcomisionRespuesta respuesta = result.getComisionReciboAgenteGSResponse().get_return();
		HashMap<String, Object> params = (HashMap<String, Object>) this.clientData;
		
		logger.debug("Resultado al ejecutar el WS Comision: Recibo: " + params.get("NumRec") + " Agente: "+ params.get("NumAgt")+" >>>"
				+ respuesta.getCodigo() + " - " + respuesta.getMensaje());
		
		ServicioGSServiceStub stubGS = (ServicioGSServiceStub) params.get("STUB");
		logger.debug("Imprimpriendo el xml enviado al WS: ");
		try {
			logger.debug(stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
		} catch (AxisFault e) {
			logger.error(e);
		}
		
		if (Estatus.EXITO.getCodigo() != respuesta.getCodigo()) {
			logger.error("Guardando en bitacora el estatus..");

			String usuario = (String) params.get("USUARIO");
			
			try {
				kernelManager.movBitacobro((String) params.get("pv_cdunieco_i"),
						(String) params.get("pv_cdramo_i"),
						(String) params.get("pv_estado_i"),
						(String) params.get("pv_nmpoliza_i"),
						(String) params.get("pv_nmsuplem_i"),
						Ice2sigsService.TipoError.ErrWSrec.getCodigo(),
						"Error Comision, Recibo " + params.get("NumRec") + " Agente: " + params.get("NumAgt")
								+ " >>> " + respuesta.getCodigo() + " - "
								+ respuesta.getMensaje(),
						 usuario,(String) params.get("pv_ntramite_i"), "ws.ice2sigs.url", "comisionReciboAgenteGS",
						 stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString(), Integer.toString(respuesta.getCodigo()));
			} catch (Exception e1) {
				logger.error("Error al insertar en bitacora", e1);
			}
		}
	}
	
	@Override
	public void receiveErrorreclamoGS(Exception e) {
		logger.error("Error en WS Reclamo: " + e.getMessage() + " Guardando en bitacora el error, getCause: " + e.getCause(),e);

		HashMap<String, Object> params = (HashMap<String, Object>) this.clientData;
		ServicioGSServiceStub stubGS = (ServicioGSServiceStub) params.get("STUB");
		logger.debug("Imprimpriendo el xml enviado al WS: ");
		try {
			logger.debug(stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
		} catch (AxisFault ex) {
			logger.error(ex);
		}
		
		String usuario = (String) params.get("USUARIO");
		
		try {
			kernelManager.movBitacobro(
					(String) params.get("pv_cdunieco_i"),
					(String) params.get("pv_cdramo_i"),
					(String) params.get("pv_estado_i"),
					(String) params.get("pv_nmpoliza_i"),
					(String) params.get("pv_nmsuplem_i"),
					Ice2sigsService.TipoError.ErrWSsinCx.getCodigo(),
					"Error en ReclamoCod: " + params.get("NumSin") + " Inciso: " + params.get("NumInc") 
							+ " Msg: " + e.getMessage() + " ***Cause: "
							+ e.getCause(),
					 usuario, (String) params.get("pv_ntramite_i"), "ws.ice2sigs.url", "reclamoGS",
					 stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString(), null);
		} catch (Exception e1) {
			logger.error("Error al insertar en bitacora", e1);
		}
	}

	@Override
	public void receiveResultreclamoGS(ReclamoGSResponseE result) {
		logger.debug("Comunicacion exitosa WS Reclamo");
		ReclamoRespuesta respuesta = result.getReclamoGSResponse().get_return();
		HashMap<String, Object> params = (HashMap<String, Object>) this.clientData;
		
		logger.debug("Resultado al ejecutar el WS Reclamo: " + respuesta.getCodigo() + " - " + respuesta.getMensaje());
		ServicioGSServiceStub stubGS = (ServicioGSServiceStub) params.get("STUB");
		logger.debug("Imprimpriendo el xml enviado al WS: ");
		try {
			logger.debug(stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
		} catch (AxisFault e) {
			logger.error(e);
		}

		if (Estatus.EXITO.getCodigo() != respuesta.getCodigo()) {
			logger.error("Guardando en bitacora el estatus..");

			String usuario = (String) params.get("USUARIO");
			
			try {
				kernelManager.movBitacobro((String) params.get("pv_cdunieco_i"),
						(String) params.get("pv_cdramo_i"),
						(String) params.get("pv_estado_i"),
						(String) params.get("pv_nmpoliza_i"),
						(String) params.get("pv_nmsuplem_i"),
						Ice2sigsService.TipoError.ErrWSsin.getCodigo(),
						"Error en ReclamoCod: " + params.get("NumSin") + " Inciso: " + params.get("NumInc") 
								+ " >>> " + respuesta.getCodigo() + " - "
								+ respuesta.getMensaje(),
						 usuario, (String) params.get("pv_ntramite_i"), "ws.ice2sigs.url", "reclamoGS",
						 stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString(), Integer.toString(respuesta.getCodigo()));
			} catch (Exception e1) {
				logger.error("Error al insertar en bitacora", e1);
			}
		}
	}

	public void setKernelManager(KernelManagerSustituto kernelManager) {
		this.kernelManager = kernelManager;
	}

}