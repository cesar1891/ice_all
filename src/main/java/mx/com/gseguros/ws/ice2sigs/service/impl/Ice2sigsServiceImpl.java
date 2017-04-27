package mx.com.gseguros.ws.ice2sigs.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.kernel.service.KernelManagerSustituto;
import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.exception.WSException;
import mx.com.gseguros.portal.documentos.model.Documento;
import mx.com.gseguros.portal.general.model.RespuestaVO;
import mx.com.gseguros.portal.general.util.TipoSituacion;
import mx.com.gseguros.portal.general.util.TipoTramite;
import mx.com.gseguros.portal.mesacontrol.dao.MesaControlDAO;
import mx.com.gseguros.portal.siniestros.service.SiniestrosManager;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.Ccomision;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.CcomisionRespuesta;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteGeneral;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteGeneralGS;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteGeneralGSE;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteGeneralGSResponseE;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteGeneralRespuesta;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteSalud;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteSaludGS;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteSaludGSE;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteSaludGSResponseE;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteSaludRespuesta;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ComisionReciboAgenteGS;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ComisionReciboAgenteGSE;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ComisionReciboAgenteGSResponseE;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.DirCli;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.DirCliRespuesta;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.DireccionClienteGeneralGS;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.DireccionClienteGeneralGSE;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.DireccionClienteGeneralGSResponseE;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.Recibo;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ReciboGS;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ReciboGSE;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ReciboGSResponseE;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ReciboRespuesta;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.Reclamo;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ReclamoGS;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ReclamoGSE;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ReclamoGSResponseE;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ReclamoRespuesta;
import mx.com.gseguros.ws.ice2sigs.client.axis2.callback.impl.ServicioGSServiceCallbackHandlerImpl;
import mx.com.gseguros.ws.ice2sigs.client.model.ReciboWrapper;
import mx.com.gseguros.ws.ice2sigs.service.Ice2sigsService;
import mx.com.gseguros.ws.model.WrapperResultadosWS;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.xpath.operations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Ice2sigsServiceImpl implements Ice2sigsService {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Ice2sigsServiceImpl.class);

	private static final long WS_TIMEOUT =  20000;
	private static final long WS_TIMEOUT_EXTENDED =  3*60*1000; // 3 minutos// Cambio Temporal Antes 60000;
	
	private String endpoint;
	
	private String urlImpresionRecibos;
	
	private transient KernelManagerSustituto kernelManager;
	private SiniestrosManager siniestrosManager;
	
	@Autowired
	private MesaControlDAO mesaControlDAO;

	/**
	 * METODO DEPRECADO
	 */
	@Deprecated
	private WrapperResultadosWS ejecutaClienteSaludGS(Operacion operacion,
			ClienteSalud cliente, HashMap<String, Object> params, boolean async) throws Exception {
		
		ClienteSaludRespuesta resultado = null;
		WrapperResultadosWS resWS = new WrapperResultadosWS();
		ServicioGSServiceStub stubGS = null;
		
		try {
			logger.info(new StringBuffer("endpoint a invocar=").append(endpoint));
			stubGS = new ServicioGSServiceStub(endpoint);
		} catch (AxisFault e) {
			logger.error(e);
			throw new Exception("Error de preparacion de Axis2: "
					+ e.getMessage());
		}
		stubGS._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
		
		ClienteSaludGSResponseE RespuestaGS = null;
		
		ClienteSaludGS clienteS = new ClienteSaludGS();
		clienteS.setArg0(operacion.getCodigo());
		clienteS.setArg1(cliente);
		
		ClienteSaludGSE clienteE = new ClienteSaludGSE();
		clienteE.setClienteSaludGS(clienteS);
		
		try {
			if(async){
				//TODO: RBS Cambiar params por PolizaVO
				//Se genera una nueva instancia en cada llamado, para evitar corrupcion de datos en el handler:
				WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext());
				ServicioGSServiceCallbackHandlerImpl callback = (ServicioGSServiceCallbackHandlerImpl)context.getBean("servicioGSServiceCallbackHandlerImpl");
				// Se setean los parametros al callback handler:
				params.put("STUB", stubGS);
				callback.setClientData(params);
				
				stubGS.startclienteSaludGS(clienteE, callback);
			} else {
				RespuestaGS = stubGS.clienteSaludGS(clienteE);
				resultado = RespuestaGS.getClienteSaludGSResponse().get_return();
				resWS.setResultadoWS(resultado);
				resWS.setXmlIn(stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
				logger.debug("Resultado ejecucion de WS clienteSaludGS: "+resultado.getCodigo()+" - "+resultado.getMensaje());
			}
		} catch (Exception re) {
			throw new WSException("Error de conexion: " + re.getMessage(), re, stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
		}
		
		return resWS;
	}

	private WrapperResultadosWS ejecutaClienteGeneralGS(Operacion operacion,
			ClienteGeneral cliente, HashMap<String, Object> params, boolean async) throws Exception {
		
		ClienteGeneralRespuesta resultado = null;
		WrapperResultadosWS resWS = new WrapperResultadosWS();
		ServicioGSServiceStub stubGS = null;
		
		try {
			logger.info(new StringBuffer("endpoint a invocar=").append(endpoint));
			stubGS = new ServicioGSServiceStub(endpoint);
		} catch (AxisFault e) {
			logger.error(e);
			throw new Exception("Error de preparacion de Axis2: "
					+ e.getMessage());
		}
		stubGS._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT_EXTENDED);
		
		ClienteGeneralGSResponseE RespuestaGS = null;
		
		ClienteGeneralGS clienteS = new ClienteGeneralGS();
		clienteS.setArg0(operacion.getCodigo());
		clienteS.setArg1(cliente);
		
		ClienteGeneralGSE clienteE = new ClienteGeneralGSE();
		clienteE.setClienteGeneralGS(clienteS);
		
		try {
			if(async){
				//TODO: RBS Cambiar params por PolizaVO
				//Se genera una nueva instancia en cada llamado, para evitar corrupcion de datos en el handler:
				WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext());
				ServicioGSServiceCallbackHandlerImpl callback = (ServicioGSServiceCallbackHandlerImpl)context.getBean("servicioGSServiceCallbackHandlerImpl");
				// Se setean los parametros al callback handler:
				params.put("STUB", stubGS);
				callback.setClientData(params);
				
				stubGS.startclienteGeneralGS(clienteE, callback);
			} else {
				RespuestaGS = stubGS.clienteGeneralGS(clienteE);
				resultado = RespuestaGS.getClienteGeneralGSResponse().get_return();
				resWS.setResultadoWS(resultado);
				resWS.setXmlIn(stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
				logger.debug("Resultado ejecucion de WS clienteGeneralGS: "+resultado.getCodigo()+" - "+resultado.getMensaje());
			}
		} catch (Exception re) {
			throw new WSException("Error de conexion: " + re.getMessage(), re, stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
		}
		
		return resWS;
	}
	

	private WrapperResultadosWS ejecutaDomicilioClienteGeneralGS(Operacion operacion, DirCli direccionCliente) throws Exception {
		
		DirCliRespuesta resultado = null;
		WrapperResultadosWS resWS = new WrapperResultadosWS();
		ServicioGSServiceStub stubGS = null;
		
		try {
			logger.info(new StringBuffer("endpoint a invocar=").append(endpoint));
			stubGS = new ServicioGSServiceStub(endpoint);
		} catch (AxisFault e) {
			logger.error(e);
			throw new Exception("Error de preparacion de Axis2: "
					+ e.getMessage());
		}
		stubGS._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT_EXTENDED);
		
		DireccionClienteGeneralGSResponseE RespuestaGS = null;
		
		DireccionClienteGeneralGS direccionClienteS = new DireccionClienteGeneralGS();
		direccionClienteS.setArg0(operacion.getCodigo());
		direccionClienteS.setArg1(direccionCliente);
		
		DireccionClienteGeneralGSE direccionClienteE = new DireccionClienteGeneralGSE();
		direccionClienteE.setDireccionClienteGeneralGS(direccionClienteS);
		
		try {
			RespuestaGS = stubGS.direccionClienteGeneralGS(direccionClienteE);
			resultado = RespuestaGS.getDireccionClienteGeneralGSResponse().get_return();
			resWS.setResultadoWS(resultado);
			resWS.setXmlIn(stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
			logger.debug("Resultado ejecucion de WS direccionClienteGeneralGS: "+resultado.getCodigo()+" - "+resultado.getMensaje());
		} catch (Exception re) {
			throw new WSException("Error de conexion: " + re.getMessage(), re, stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
		}
		
		return resWS;
	}
	
	private WrapperResultadosWS ejecutaReciboGS(Operacion operacion,
			Recibo recibo, HashMap<String, Object> params, boolean async) throws Exception {
		
		ReciboRespuesta resultado = null;
		WrapperResultadosWS resWS = new WrapperResultadosWS();
		ServicioGSServiceStub stubGS = null;
		
		try {
			logger.info(new StringBuffer("endpoint a invocar=").append(endpoint));
			stubGS = new ServicioGSServiceStub(endpoint);
		} catch (AxisFault e) {
			logger.error(e);
			throw new Exception("Error de preparacion de Axis2: "
					+ e.getMessage());
		}
		stubGS._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
		
		ReciboGSResponseE RespuestaGS = null;
		
		ReciboGS reciboS = new ReciboGS();
		reciboS.setArg0(operacion.getCodigo());
		reciboS.setArg1(recibo);
		
		ReciboGSE reciboE = new ReciboGSE();
		reciboE.setReciboGS(reciboS);
		
		try {
			if(async){
				//TODO: RBS Cambiar params por PolizaVO
				//Se genera una nueva instancia en cada llamado, para evitar corrupcion de datos en el handler:
				WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext());
				ServicioGSServiceCallbackHandlerImpl callback = (ServicioGSServiceCallbackHandlerImpl)context.getBean("servicioGSServiceCallbackHandlerImpl");
				// Se setean los parametros al callback handler:
				params.put("STUB", stubGS);
				callback.setClientData(params);
				
				stubGS.startreciboGS(reciboE, callback);
			} else {
				RespuestaGS = stubGS.reciboGS(reciboE);
				resultado = RespuestaGS.getReciboGSResponse().get_return();
				resWS.setResultadoWS(resultado);
				resWS.setXmlIn(stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
				logger.debug("Resultado ejecucion de WS reciboGS: "+resultado.getCodigo()+" - "+resultado.getMensaje());
			}
		} catch (Exception re) {
			throw new WSException("Error de conexion: " + re.getMessage(), re, stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
		}
		
		return resWS;
	}
	
	private WrapperResultadosWS ejecutaComisionReciboAgenteGS(Operacion operacion,
			Ccomision ccomision, HashMap<String, Object> params, boolean async) throws Exception {
		
		CcomisionRespuesta resultado = null;
		WrapperResultadosWS resWS = new WrapperResultadosWS();
		ServicioGSServiceStub stubGS = null;
		
		try {
			logger.info(new StringBuffer("endpoint a invocar=").append(endpoint));
			stubGS = new ServicioGSServiceStub(endpoint);
		} catch (AxisFault e) {
			logger.error(e);
			throw new Exception("Error de preparacion de Axis2: "
					+ e.getMessage());
		}
		stubGS._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
		
		ComisionReciboAgenteGSResponseE RespuestaGS = null;
		
		ComisionReciboAgenteGS comisionReciboAgenteGS = new ComisionReciboAgenteGS();
		comisionReciboAgenteGS.setArg0(operacion.getCodigo());
		comisionReciboAgenteGS.setArg1(ccomision);
		
		ComisionReciboAgenteGSE comisionReciboAgenteGSE = new ComisionReciboAgenteGSE();
		comisionReciboAgenteGSE.setComisionReciboAgenteGS(comisionReciboAgenteGS);
		
		try {
			if(async){
				//TODO: RBS Cambiar params por PolizaVO
				//Se genera una nueva instancia en cada llamado, para evitar corrupcion de datos en el handler:
				WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext());
				ServicioGSServiceCallbackHandlerImpl callback = (ServicioGSServiceCallbackHandlerImpl)context.getBean("servicioGSServiceCallbackHandlerImpl");
				// Se setean los parametros al callback handler:
				params.put("STUB", stubGS);
				callback.setClientData(params);
				
				stubGS.startcomisionReciboAgenteGS(comisionReciboAgenteGSE, callback);
			}else {
				RespuestaGS = stubGS.comisionReciboAgenteGS(comisionReciboAgenteGSE);
				resultado = RespuestaGS.getComisionReciboAgenteGSResponse().get_return();
				resWS.setResultadoWS(resultado);
				resWS.setXmlIn(stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
				logger.debug("Resultado ejecucion de WS comision: "+resultado.getCodigo()+" - "+resultado.getMensaje());
			}
		} catch (Exception re) {
			throw new WSException("Error de conexion: " + re.getMessage(), re, stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
		}
		
		return resWS;
	}

	
	private WrapperResultadosWS ejecutaReclamoGS(Operacion operacion,
			Reclamo reclamo, HashMap<String, Object> params, boolean async) throws Exception {
		
		ReclamoRespuesta resultado = null;
		WrapperResultadosWS resWS = new WrapperResultadosWS();
		ServicioGSServiceStub stubGS = null;
		
		try {
			logger.info(new StringBuffer("endpoint a invocar=").append(endpoint));
			stubGS = new ServicioGSServiceStub(endpoint);
		} catch (AxisFault e) {
			logger.error(e);
			throw new Exception("Error de preparacion de Axis2: "
					+ e.getMessage());
		}
		stubGS._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
		
		ReclamoGSResponseE RespuestaGS = null;
		
		ReclamoGS reclamoS = new ReclamoGS();
		reclamoS.setArg0(operacion.getCodigo());
		reclamoS.setArg1(reclamo);
		
		ReclamoGSE reclamoE = new ReclamoGSE();
		reclamoE.setReclamoGS(reclamoS);
		
		try {
			if(async){
				//TODO: RBS Cambiar params por PolizaVO
				//Se genera una nueva instancia en cada llamado, para evitar corrupcion de datos en el handler:
				WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext());
				ServicioGSServiceCallbackHandlerImpl callback = (ServicioGSServiceCallbackHandlerImpl)context.getBean("servicioGSServiceCallbackHandlerImpl");
				// Se setean los parametros al callback handler:
				params.put("STUB", stubGS);
				callback.setClientData(params);
				
				stubGS.startreclamoGS(reclamoE, callback);
			} else {
				RespuestaGS = stubGS.reclamoGS(reclamoE);
				resultado = RespuestaGS.getReclamoGSResponse().get_return();
				resWS.setResultadoWS(resultado);
				resWS.setXmlIn(stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
				logger.debug("Resultado ejecucion de WS ejecutaReclamoGS: "+resultado.getCodigo()+" - "+resultado.getMensaje());
			}
		} catch (Exception re) {
			throw new WSException("Error de conexion: " + re.getMessage(), re, stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
		}
		
		return resWS;
	}
	
	
	/**
	 * METODO DEPRECADO
	 */
	@Deprecated
	public boolean ejecutaWSclienteSalud(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, String ntramite,
			Ice2sigsService.Operacion op, UserVO userVO) {
		
		logger.debug("********************* Entrando a Ejecuta WSclienteSalud ******************************");
		
		boolean exito = true;
		
		WrapperResultados result = null;
		ClienteSalud cliente =  null;
		
		//Se invoca servicio para obtener los datos del cliente
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i", cdramo);
		params.put("pv_estado_i", estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_nmsuplem_i", nmsuplem);
		params.put("pv_ntramite_i", ntramite);
		
		try {
			result = kernelManager.obtenDatosClienteWS(params);
			if(result.getItemList() != null && result.getItemList().size() > 0){
				cliente = ((ArrayList<ClienteSalud>) result.getItemList()).get(0);
			}
		} catch (Exception e1) {
			logger.error("Error en llamar al PL de obtencion de ejecutaWSclienteSalud",e1);
			return false;
		}
		
		if(cliente != null){
			
			String usuario = "SIN USUARIO";
			if(userVO != null){
				usuario = userVO.getUser();
			}
			params.put("USUARIO", usuario);
			
			try{
				logger.debug("Ejecutando WS TEST para WS Cliente");
				ejecutaClienteSaludGS(Operacion.INSERTA, null, params, false);
			}catch(Exception e){
				logger.error("Error al ejecutar WS TEST para cliente: " + cliente.getClaveCli(), e);
			}
			try{
				logger.debug(">>>>>>> Enviando el Cliente: " + cliente.getClaveCli());
				ejecutaClienteSaludGS(op, cliente, params, true);
			}catch(Exception e){
				logger.error("Error al insertar el cliente: " + cliente.getClaveCli(), e);
				exito = false;
			}
		}

		return exito;
	}

	public ClienteGeneralRespuesta ejecutaWSclienteGeneral(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, String ntramite, String cdperson,
			Ice2sigsService.Operacion op, ClienteGeneral cliente, UserVO userVO, boolean async) {
		
		logger.debug("********************* Entrando a Ejecuta WSclienteGeneral ******************************");
		logger.debug("********************* Operacion a Realizar: " + op);
		
		ClienteGeneralRespuesta clientesRespuesta = null;
		WrapperResultadosWS resultWS = null;
		WrapperResultados result = null;
		
		//Se invoca servicio para obtener los datos del cliente
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i", cdramo);
		params.put("pv_estado_i", estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_nmsuplem_i", nmsuplem);
		params.put("pv_ntramite_i", ntramite);
		params.put("pv_cdperson_i", cdperson);

		params.put("pv_compania_i", (cliente != null)? cliente.getClaveCia():null);
		
		
		if(Ice2sigsService.Operacion.CONSULTA_GENERAL.getCodigo() != op.getCodigo()){
			try {
				
				if(StringUtils.isNotBlank(cdperson)){
					result = kernelManager.obtenDatosClienteGeneralWSporCdperson(params);
				}else{
					result = kernelManager.obtenDatosClienteGeneralWS(params);
				}
				
				if(result.getItemList() != null && result.getItemList().size() > 0){
					if(StringUtils.isNotBlank(cdperson) && cliente != null){
						String claveCia = cliente.getClaveCia();
						String numExt   = cliente.getNumeroExterno();
						cliente = ((ArrayList<ClienteGeneral>) result.getItemList()).get(0);
						
						if(StringUtils.isNotBlank(claveCia)){
							cliente.setClaveCia(claveCia);
						}
						
						if(StringUtils.isBlank(cliente.getNumeroExterno()) && StringUtils.isNotBlank(numExt)){
							cliente.setNumeroExterno(numExt);
						}
						
					}else{
						cliente = ((ArrayList<ClienteGeneral>) result.getItemList()).get(0);
					}
				}
			} catch (Exception e1) {
				logger.error("Error en llamar al PL de obtencion de ejecutaWSclienteGeneral",e1);
				return null;
			}	
		}
		
		
		if(cliente != null){
			
			String usuario = "SIN USUARIO";
			if(userVO != null){
				usuario = userVO.getUser();
			}
			
/**
 * PROBABLEMETE SE USE:
 */
//			int usuarioCap = 0;
//			String usuarioCaptura = null;
//			try{
//				if(StringUtils.isNotBlank(userVO.getClaveUsuarioCaptura())){
//					usuarioCaptura = userVO.getClaveUsuarioCaptura();
//				}else{
//					usuarioCaptura = userVO.getCodigoPersona();
//				}
//				usuarioCap =  Integer.parseInt(usuarioCaptura);
//				
//			}catch(Exception e){
//				logger.error("<<<<>>>> Error Al obtener el Usuario de Captura <<<<>>>>, se envia 0, usuarioCaptura:" +  usuarioCaptura, e);
//			}
//			
//			cliente.setUsucapCli(usuarioCap);
			
			logger.debug(">>>>>>> Ejecutando WS Cliente General Clave Compania: " + cliente.getClaveCia());
			logger.debug(">>>>>>> Ejecutando WS Cliente General Clave Externa: " + cliente.getNumeroExterno());
			logger.debug(">>>>>>> Ejecutando WS Cliente General RFC: " + cliente.getRfcCli());
			
			logger.debug("Clave de cliente cdperson: "+ cliente.getClaveCli());
			logger.debug("Clave de usuarioCaptura: "  + cliente.getUsucapCli());
			
			try{
				
				if(async){
					params.put("USUARIO", usuario);
					ejecutaClienteGeneralGS(op, cliente, params, async);
				}else{
					resultWS = ejecutaClienteGeneralGS(op, cliente, null, async);
					clientesRespuesta = (ClienteGeneralRespuesta) resultWS.getResultadoWS();
					
					if(clientesRespuesta != null && clientesRespuesta.getClientesGeneral() != null &&
							clientesRespuesta.getClientesGeneral().length > 0 ){
						for(ClienteGeneral cliItImp : clientesRespuesta.getClientesGeneral()){
							cliItImp.setRfcCli(StringUtils.trim(cliItImp.getRfcCli()));
						}
					}
					
					logger.debug("XML de entrada: " + resultWS.getXmlIn());

					if (Ice2sigsService.Operacion.CONSULTA_GENERAL.getCodigo() != op.getCodigo() && Estatus.EXITO.getCodigo() != clientesRespuesta.getCodigo() 
							&& Estatus.LLAVE_DUPLICADA.getCodigo() != clientesRespuesta.getCodigo()) {
						logger.error("Guardando en bitacora el estatus");

						try {
							kernelManager.movBitacobro(
									(String) params.get("pv_cdunieco_i"),
									(String) params.get("pv_cdramo_i"),
									(String) params.get("pv_estado_i"),
									(String) params.get("pv_nmpoliza_i"),
									(String) params.get("pv_nmsuplem_i"), 
									Ice2sigsService.TipoError.ErrWScli.getCodigo(),
									clientesRespuesta.getCodigo() + " - " + clientesRespuesta.getMensaje(),
									usuario, (String) params.get("pv_ntramite_i"), "ws.ice2sigs.url", "clienteGeneralGS",
									resultWS.getXmlIn(), Integer.toString(clientesRespuesta.getCodigo()));
						} catch (Exception e1) {
							logger.error("Error al insertar en bitacora", e1);
						}
					}
				}
			}catch(WSException e){
				logger.error("Error al ejecutar WS Cliente General Clave Externa: " + cliente.getNumeroExterno() + " RFC: " + cliente.getRfcCli(), e);
				logger.error("Imprimpriendo el xml enviado al WS: Payload: " + e.getPayload());
				if (Ice2sigsService.Operacion.CONSULTA_GENERAL.getCodigo() != op.getCodigo()){
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
								e.getPayload(), null);
					} catch (Exception e1) {
						logger.error("Error al insertar en bitacora", e1);
					}
				}
				
			}catch (Exception e){
				logger.error("Error al ejecutar WS Cliente General Clave: " + cliente.getNumeroExterno() + " RFC: " + cliente.getRfcCli(), e);
			}
		}else{
			logger.error("Error, No se tienen datos del Cliente");
		}
		
		return clientesRespuesta;
	}

	
	public boolean ejecutaWSdireccionClienteGeneral(String cdperson, String compania, List<Map<String,String>> direccionesCliSave, List<Map<String,String>> direccionesCliUpdate, boolean sinCodigoWS, UserVO userVO) {
		
		logger.debug("######### Entrado al envio de direcciones para Web Service de Domicilio #########");
		logger.debug("#### cdperson: " + cdperson);
		logger.debug("#### compania: " + compania);
		
		WrapperResultados result = null;
		DirCli direccionCliente = null;
		
		if(StringUtils.isBlank(cdperson)){
			logger.error("Error en envio de Direcciones, cdperson nulo");
			return false;
		}
		if(StringUtils.isBlank(compania)){
			logger.error("Error en envio de Direcciones, compania nula");
			return false;
		}
		
		if((direccionesCliSave == null || direccionesCliSave.isEmpty())  &&  (direccionesCliUpdate == null || direccionesCliUpdate.isEmpty())){
			logger.warn("Alerta, No hay direcciones a insertar o acutalizar para enviar al Web Service Domicilios de Cliente");
			return true;
		}
		
		List<Map<String,String>> direccionesTodas = new ArrayList<Map<String,String>>();
		
		for(Map<String,String> dir : direccionesCliSave){
			dir.put("OPERACION", Constantes.INSERT_MODE);
		}
		
		for(Map<String,String> dir : direccionesCliUpdate){
			if(sinCodigoWS){
				/**
				 * Si el cliente no se habia registrado y ya hay direcciones en la base de datos por actualizar
				 * se mandan en modo insert para el Web Service d Domicilios
				 * 
				 */
				dir.put("OPERACION", Constantes.INSERT_MODE);
			}else{
				dir.put("OPERACION", Constantes.UPDATE_MODE);
			}
			
		}
		
		direccionesTodas.addAll(direccionesCliUpdate);
		direccionesTodas.addAll(direccionesCliSave);
		
		
		Ice2sigsService.Operacion op = null;
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdperson_i", cdperson);
		params.put("pv_compania_i", compania);
		
		for(Map<String,String> direccionCliIt : direccionesTodas){
			
			try {
				
				params.put("pv_nmorddom_i", direccionCliIt.get("NMORDDOM"));
				
				result = kernelManager.obtenDatosDomicilioGeneralWSporCdperson(params);
				direccionCliente = ((ArrayList<DirCli>) result.getItemList()).get(0);
				
			} catch (Exception e1) {
				logger.error("Error en llamar al PL de obtencion de ejecutaWSdireccionClienteGeneral",e1);
				return false;
			}	
			
			
			if(Constantes.UPDATE_MODE.equalsIgnoreCase(direccionCliIt.get("OPERACION"))){
				op =  Ice2sigsService.Operacion.ACTUALIZA;
			}else{
				op =  Ice2sigsService.Operacion.INSERTA;
			}
			
			logger.debug("********************* Entrando a Ejecuta WSdireccionClienteGeneral ******************************");
			logger.debug("********************* Operacion a Realizar: " + op);
			
			DirCliRespuesta direccionRespuesta = null;
			WrapperResultadosWS resultWS = null;
			
			
			direccionCliente.setCveCia(compania);
			direccionCliente.setNumDir(Integer.parseInt(direccionCliIt.get("NMORDDOM")));
			
			String usuarioCaptura = null;
			if(userVO!=null){
				if(StringUtils.isNotBlank(userVO.getClaveUsuarioCaptura())){
					usuarioCaptura = userVO.getClaveUsuarioCaptura();
				}else{
					usuarioCaptura = userVO.getCodigoPersona();
				}
				
			}
			direccionCliente.setUsuCap(usuarioCaptura);

			String usuario = "SIN USUARIO";
			if(userVO != null){
				usuario = userVO.getUser();
			}
			
			try{
				resultWS = ejecutaDomicilioClienteGeneralGS(op, direccionCliente);
				direccionRespuesta = (DirCliRespuesta) resultWS.getResultadoWS();
				
				logger.debug("XML de entrada: " + resultWS.getXmlIn());
				
				if (Estatus.EXITO.getCodigo() != direccionRespuesta.getCodigo() 
						&& Estatus.LLAVE_DUPLICADA.getCodigo() != direccionRespuesta.getCodigo()) {
					logger.error("Error al guardar domicilio: "+direccionCliIt);
					
					if(Estatus.LLAVE_INEXISTENTE.getCodigo() == direccionRespuesta.getCodigo() 
							&& Constantes.UPDATE_MODE.equalsIgnoreCase(direccionCliIt.get("OPERACION"))){

						//Reintenta la operacion en modo INSERT
						try{
							resultWS = ejecutaDomicilioClienteGeneralGS(Ice2sigsService.Operacion.INSERTA, direccionCliente);
							direccionRespuesta = (DirCliRespuesta) resultWS.getResultadoWS();
							
						}catch(WSException e){
							logger.error("Error al ejecutar WSdireccionClienteGeneral cdperson modo Reintenta INSERTA: " + cdperson , e);
							logger.error("Error al guardar domicilio: "+direccionCliIt);
							logger.error("Imprimpriendo el xml enviado al WS: Payload: " + e.getPayload());
							return false;
//							if (Ice2sigsService.Operacion.CONSULTA_GENERAL.getCodigo() != op.getCodigo()){
//								try {
//									kernelManager.movBitacobro(
//											(String) params.get("pv_cdunieco_i"),
//											(String) params.get("pv_cdramo_i"),
//											(String) params.get("pv_estado_i"),
//											(String) params.get("pv_nmpoliza_i"), 
//											(String) params.get("pv_nmsuplem_i"), 
//											Ice2sigsService.TipoError.ErrWScliCx.getCodigo(), 
//											"Msg: " + e.getMessage() + " ***Cause: " + e.getCause(),
//											usuario, (String) params.get("pv_ntramite_i"), "ws.ice2sigs.url", "clienteGeneralGS",
//											e.getPayload(), null);
//								} catch (Exception e1) {
//									logger.error("Error al insertar en bitacora", e1);
//								}
//							}
							
						}catch (Exception e){
							logger.error("Error al ejecutar WSdireccionClienteGeneral cdperson modo Reintenta INSERTA: " + cdperson , e);
							return false;
						}
					}else{
						return false;
					}
					
//					try {
//						kernelManager.movBitacobro(
//								(String) params.get("pv_cdunieco_i"),
//								(String) params.get("pv_cdramo_i"),
//								(String) params.get("pv_estado_i"),
//								(String) params.get("pv_nmpoliza_i"),
//								(String) params.get("pv_nmsuplem_i"), 
//								Ice2sigsService.TipoError.ErrWScli.getCodigo(),
//								direccionRespuesta.getCodigo() + " - " + direccionRespuesta.getMensaje(),
//								usuario, (String) params.get("pv_ntramite_i"), "ws.ice2sigs.url", "clienteGeneralGS",
//								resultWS.getXmlIn(), Integer.toString(direccionRespuesta.getCodigo()));
//					} catch (Exception e1) {
//						logger.error("Error al insertar en bitacora", e1);
//					}
				}
			}catch(WSException e){
				logger.error("Error al ejecutar WSdireccionClienteGeneral cdperson: " + cdperson , e);
				logger.error("Error al guardar domicilio: "+direccionCliIt);
				logger.error("Imprimpriendo el xml enviado al WS: Payload: " + e.getPayload());
				return false;
//				if (Ice2sigsService.Operacion.CONSULTA_GENERAL.getCodigo() != op.getCodigo()){
//					try {
//						kernelManager.movBitacobro(
//								(String) params.get("pv_cdunieco_i"),
//								(String) params.get("pv_cdramo_i"),
//								(String) params.get("pv_estado_i"),
//								(String) params.get("pv_nmpoliza_i"), 
//								(String) params.get("pv_nmsuplem_i"), 
//								Ice2sigsService.TipoError.ErrWScliCx.getCodigo(), 
//								"Msg: " + e.getMessage() + " ***Cause: " + e.getCause(),
//								usuario, (String) params.get("pv_ntramite_i"), "ws.ice2sigs.url", "clienteGeneralGS",
//								e.getPayload(), null);
//					} catch (Exception e1) {
//						logger.error("Error al insertar en bitacora", e1);
//					}
//				}
				
			}catch (Exception e){
				logger.error("Error al ejecutar WSdireccionClienteGeneral cdperson: " + cdperson , e);
				return false;
			}
		}
		
		return true;
	}

	
	public boolean ejecutaWSrecibos(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem,
			String rutaPoliza, String sucursal,
			String nmsolici, String ntramite, boolean async,
			String tipoMov, UserVO userVO) {
		
		boolean allInserted = true;
		String cdtipsit = null;
		String cdtipsitGS = null;
		
		logger.debug("*** Entrando a metodo Recibos WS ice2sigs, para la poliza: " + nmpoliza + " sucursal: " + sucursal + " tipoMov: " + tipoMov + "***");
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i", cdramo);
		params.put("pv_estado_i", estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_nmsuplem_i", nmsuplem);
		params.put("pv_ntramite_i", ntramite);
		
		WrapperResultados result = null;
		WrapperResultadosWS resultWS = null;
		
		ArrayList<ReciboWrapper> recibos =  null;
		try {
			result = kernelManager.obtenDatosRecibos(params);
			recibos = (ArrayList<ReciboWrapper>) result.getItemList();
			
			cdtipsit   = kernelManager.obtenCdtipsit(params);
			
			params.put("pv_cdtipsit_i", cdtipsit);
			
			cdtipsitGS = kernelManager.obtenCdtipsitGS(params);
			
		} catch (Exception e1) {
			logger.error("Error en llamar al PL de obtencion de RECIBOS",e1);
			return false;
		}

		String usuario = "SIN USUARIO";
		if(userVO != null){
			usuario = userVO.getUser();
		}
		
		if(async){
			params.put("USUARIO", usuario);
		}
		
		Recibo recibo = null;
		boolean comisionAgentes = false;
		for(ReciboWrapper recVO: recibos){
			recibo = recVO.getRecibo();
			Operacion operacion = Operacion.valueOf(recVO.getOperacion());
			
			if(recibo.getNumAgt() == -2){
				comisionAgentes = true;
			}
			
			try{
				
				if(async){
					// Se crea un HashMap por cada invocacion asincrona del WS, para evitar issue (sobreescritura de valores):
					HashMap<String, Object> paramsBitacora = new HashMap<String, Object>();
					paramsBitacora.putAll(params);
					paramsBitacora.put("NumRec", recibo.getNumRec());
					paramsBitacora.put("TipEnd", recibo.getTipEnd());
					paramsBitacora.put("NumEnd", recibo.getNumEnd());
					
					ejecutaReciboGS(operacion, recibo, paramsBitacora, async);
				}else{
					resultWS = ejecutaReciboGS(operacion, recibo, null, async);
					ReciboRespuesta respuesta = (ReciboRespuesta) resultWS.getResultadoWS();
					logger.debug("Resultado al ejecutar el WS Recibo: " + recibo.getNumRec() + " >>>"
							+ respuesta.getCodigo() + " - " + respuesta.getMensaje());
					logger.debug("XML de entrada: " + resultWS.getXmlIn());

					if (Estatus.EXITO.getCodigo() != respuesta.getCodigo()) {
						logger.error("Guardando en bitacora el estatus");
						
						if(Estatus.LLAVE_DUPLICADA.getCodigo() != respuesta.getCodigo() ){
							allInserted =  false;
						}

						try {
							kernelManager.movBitacobro((String) params.get("pv_cdunieco_i"),
									(String) params.get("pv_cdramo_i"),
									(String) params.get("pv_estado_i"),
									(String) params.get("pv_nmpoliza_i"),
									(String) params.get("pv_nmsuplem_i"),
									Ice2sigsService.TipoError.ErrWSrec.getCodigo(),
									"Error Recibo " + recibo.getNumRec() + " TipEnd: " + recibo.getTipEnd() + " NumEnd: " + recibo.getNumEnd()
											+ " >>> " + respuesta.getCodigo() + " - "
											+ respuesta.getMensaje(),
									 usuario, (String) params.get("pv_ntramite_i"), "ws.ice2sigs.url", "reciboGS",
									 resultWS.getXmlIn(), Integer.toString(respuesta.getCodigo()));
						} catch (Exception e1) {
							logger.error("Error al insertar en Bitacora", e1);
						}
					}
				}
			}catch(WSException e){
				allInserted =  false;
				
				logger.error("Error al insertar recibo: "+recibo.getNumRec()+" tramite: "+ntramite);
				logger.error("Imprimpriendo el xml enviado al WS: Payload: " + e.getPayload());
				try {
					kernelManager.movBitacobro(
							(String) params.get("pv_cdunieco_i"),
							(String) params.get("pv_cdramo_i"),
							(String) params.get("pv_estado_i"),
							(String) params.get("pv_nmpoliza_i"),
							(String) params.get("pv_nmsuplem_i"),
							Ice2sigsService.TipoError.ErrWSrecCx.getCodigo(),
							"Error Recibo " + recibo.getNumRec() + " TipEnd: " + recibo.getTipEnd() + " NumEnd: " + recibo.getNumEnd()
									+ " Msg: " + e.getMessage() + " ***Cause: "
									+ e.getCause(),
							 usuario, (String) params.get("pv_ntramite_i"), "ws.ice2sigs.url", "reciboGS",
							 e.getPayload(), null);
				} catch (Exception e1) {
					logger.error("Error al insertar en Bitacora", e1);
				}
			}catch (Exception e){
				allInserted =  false;
				logger.error("Error Excepcion al insertar recibo: "+recibo.getNumRec()+" tramite: "+ntramite,e);
			}
		}
		
		if(comisionAgentes){
			ArrayList<Ccomision> comisiones =  null;
			try {
				result = kernelManager.obtenDatosComisiones(params);
				comisiones = (ArrayList<Ccomision>) result.getItemList();
				
			} catch (Exception e1) {
				logger.error("Error en llamar al PL de obtencion de Comisiones",e1);
				return false;
			}
	
			Operacion operacion = Operacion.INSERTA;
			for(Ccomision comision: comisiones){
				
				try{
					//Se fija a que las comisiones sean hilos asyncronos.
					boolean asyncComisiones = true;
					if(asyncComisiones){
						// Se crea un HashMap por cada invocacion asincrona del WS, para evitar issue (sobreescritura de valores):
						HashMap<String, Object> paramsBitacora = new HashMap<String, Object>();
						paramsBitacora.putAll(params);
						paramsBitacora.put("NumRec", comision.getNumRecc());
						paramsBitacora.put("NumAgt", comision.getNumAgtc());
						
						ejecutaComisionReciboAgenteGS(operacion, comision, paramsBitacora, async);
					}else{
						resultWS = ejecutaComisionReciboAgenteGS(operacion, comision, null, async);
						CcomisionRespuesta respuesta = (CcomisionRespuesta) resultWS.getResultadoWS();
						logger.debug("Resultado al ejecutar el WS Comisiones: " + comision.getNumRecc() + " Agente: "+ comision.getNumAgtc() + " >>>"
								+ respuesta.getCodigo() + " - " + respuesta.getMensaje());
						logger.debug("XML de entrada: " + resultWS.getXmlIn());
	
						if (Estatus.EXITO.getCodigo() != respuesta.getCodigo()) {
							logger.error("Guardando en bitacora el estatus");
	
							try {
								kernelManager.movBitacobro((String) params.get("pv_cdunieco_i"),
										(String) params.get("pv_cdramo_i"),
										(String) params.get("pv_estado_i"),
										(String) params.get("pv_nmpoliza_i"),
										(String) params.get("pv_nmsuplem_i"),
										Ice2sigsService.TipoError.ErrWSrec.getCodigo(),
										"Error Comision " + comision.getNumRecc() + " Agente: " + comision.getNumAgtc() 
												+ " >>> " + respuesta.getCodigo() + " - "
												+ respuesta.getMensaje(),
										 usuario, (String) params.get("pv_ntramite_i"), "ws.ice2sigs.url", "comisionReciboAgenteGS",
										 resultWS.getXmlIn(), Integer.toString(respuesta.getCodigo()));
							} catch (Exception e1) {
								logger.error("Error al insertar en Bitacora", e1);
							}
						}
					}
				}catch(WSException e){
					logger.error("Error al insertar comision: "+ comision.getNumRecc() + " Agente: "+ comision.getNumAgtc() +" tramite: "+ntramite);
					logger.error("Imprimpriendo el xml enviado al WS: Payload: " + e.getPayload());
					try {
						kernelManager.movBitacobro(
								(String) params.get("pv_cdunieco_i"),
								(String) params.get("pv_cdramo_i"),
								(String) params.get("pv_estado_i"),
								(String) params.get("pv_nmpoliza_i"),
								(String) params.get("pv_nmsuplem_i"),
								Ice2sigsService.TipoError.ErrWSrecCx.getCodigo(),
								"Error Comision " + comision.getNumRecc() + " Agente: " + comision.getNumAgtc()
										+ " Msg: " + e.getMessage() + " ***Cause: "
										+ e.getCause(),
								 usuario, (String) params.get("pv_ntramite_i"), "ws.ice2sigs.url", "comisionReciboAgenteGS",
								 e.getPayload(), null);
					} catch (Exception e1) {
						logger.error("Error al insertar en Bitacora", e1);
					}
				}catch (Exception e){
					logger.error("Error Excepcion al insertar comision: "+ comision.getNumRecc() + " Agente: " + comision.getNumAgtc()+" tramite: "+ntramite,e);
				}
			}
		}
		
		/**
		 * PARA EL GUARDADO CADA PDF DE RECIBO
		 */
		logger.debug("*** Empieza generacion de URLs para Recibos ***");
		
		String visible = null;
		HashMap<String,String> imprimir = new HashMap<String, String>(); 
		for(ReciboWrapper recVO: recibos){
			
			recibo = recVO.getRecibo();
			
			/**
			 * Algunos recibos no se guardan en la base de datos
			 */
			if(!recVO.isGuardarDocumento()) continue;
			
			
			/**
			 * Por default se permite ver solo el primer recibo en combinacion con el tipo de endoso y los demas se guardan ocultos.
			 */
			String llave = recVO.getRecibo().getTipEnd() + Integer.toString(recVO.getRecibo().getNumEnd());
			
			if(!imprimir.containsKey(llave)){
				visible = Constantes.SI;
				imprimir.put(llave, Integer.toString(recVO.getRecibo().getNumRec()));
			}else{
				visible = Constantes.NO;
			}
			
			try{
				int numEndoso;
				String tipoEndoso;
				// Si es poliza nueva:
				if(("1").equals(tipoMov)) {
					numEndoso = 0;
					tipoEndoso = "";
				} else {
					numEndoso = recibo.getNumEnd();
					tipoEndoso = recibo.getTipEnd();
				}
				
				int modalidad = 0;
				if(StringUtils.isNotBlank(cdtipsit)){
					if(TipoSituacion.SALUD_VITAL.getCdtipsit().equalsIgnoreCase(cdtipsit)){
						modalidad = 1;
					}else if(TipoSituacion.SALUD_NOMINA.getCdtipsit().equalsIgnoreCase(cdtipsit)){
						modalidad = 2;
					}
				}
				
				
    				//Parametro1:  Sucursal
                    //Parametro2:  Ramo (213 o 214)
                    //Parametro3:  Poliza
                    //Parametro4:  Tipo de endoso (Si es vacio no enviar nada en otro caso poner A o D segun sea el caso)
                    //Parametro5:  Numero de endoso (Cuando es poliza nueva poner 0)
                    //Parametro6:  Numero de recibo (1,2,3..segun la forma de pago) Para nuestro caso es siempre el 1
					//String parametros = "?9999,"+modalidad+","+sucursal+","+cdtipsitGS+","+nmpoliza+",0,"+numEndoso+","+tipoEndoso+","+recibo.getNumRec(); // PARAMS RECIBO ANTERIORES
					String parametros = "?"+sucursal+","+cdtipsitGS+","+nmpoliza+","+tipoEndoso+","+numEndoso+","+recibo.getNumRec();
					
					logger.debug("URL Generada para Recibo: "+ urlImpresionRecibos + parametros);
					
					mesaControlDAO.guardarDocumento
					(
							cdunieco
							,cdramo
							,estado
							,nmpoliza
							,nmsuplem
							,new Date()
							,urlImpresionRecibos + parametros
							,"Recibo "+recibo.getNumRec()
							,nmsolici
							,ntramite
							,tipoMov
							,visible
							,null
							,TipoTramite.POLIZA_NUEVA.getCdtiptra()
							,"0"
							,Documento.RECIBO, null, null, false
							);
				//}
			}catch(Exception e){
				logger.error("Error al guardar indexaxion de recibo: " + recibo.getRmdbRn(), e);
			}
		}

		return allInserted;
	}

	public RespuestaVO ejecutaWSreclamosTramite(String ntramite, Ice2sigsService.Operacion op, boolean async, UserVO userVO) {
		
		logger.debug("********************* Entrando a Ejecuta WSreclamo ******************************");
		
		RespuestaVO res =  new RespuestaVO();
		res.setSuccess(true);
		List<Reclamo> resultReclamos = null;
		WrapperResultadosWS resultWS = null;
		
		//Se invoca servicio para obtener los datos del cliente
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("pv_ntramite_i", ntramite);
		
		String usuario = "SIN USUARIO";
		if(userVO != null){
			usuario = userVO.getUser();
		}
		params.put("USUARIO", usuario);
		
		try {
			resultReclamos = siniestrosManager.obtieneDatosReclamoWS(params);
		} catch (Exception e1) {
			logger.error("Error en llamar al PL de obtencion de obtieneDatosReclamoWS",e1);
			res.setSuccess(false);
			res.setMensaje("Sin datos. No se han guardado correctamente los calculos.");
			return res;
		}
		
		if(resultReclamos != null && resultReclamos.size() > 0){
			
			for(Reclamo reclamo : resultReclamos){
				if(reclamo.getNumPol() == 0){
					logger.debug("Sin datos para ejecutaWSreclamo.");
					res.setSuccess(false);
					res.setMensaje("Alguno de los reclamos no tiene la informaci\u00f3n necesaria para solicitar el pago.");
					return res;
				}
				
				params.put("pv_cdunieco_i", Integer.toString(reclamo.getSucPol()));
				params.put("pv_cdramo_i",   Integer.toString(reclamo.getRamPol()));
				params.put("pv_estado_i",   "M");
				params.put("pv_nmpoliza_i", Integer.toString(reclamo.getNumPol()));
				
				try{
					logger.debug(">>>>>>> Enviando el Reclamo: " + reclamo.getIcodreclamo());
					
					if(async){
						// Se crea un HashMap por cada invocacion asincrona del WS, para evitar issue (sobreescritura de valores):
						HashMap<String, Object> paramsBitacora = new HashMap<String, Object>();
						paramsBitacora.putAll(params);
						paramsBitacora.put("NumSin", reclamo.getIcodreclamo());
						paramsBitacora.put("NumInc", reclamo.getIncPol());
						
						ejecutaReclamoGS(op, reclamo, paramsBitacora, async);
					}else{
						
						resultWS = ejecutaReclamoGS(op, reclamo, params, async);
						ReclamoRespuesta respuesta = (ReclamoRespuesta) resultWS.getResultadoWS();
						logger.debug("XML de entrada: " + resultWS.getXmlIn());
						
						if (Estatus.EXITO.getCodigo() != respuesta.getCodigo()) {
							logger.error("Guardando en bitacora el estatus de Error del WS");

							try {
								kernelManager.movBitacobro((String) params.get("pv_cdunieco_i"),
										(String) params.get("pv_cdramo_i"),
										(String) params.get("pv_estado_i"),
										(String) params.get("pv_nmpoliza_i"),
										(String) params.get("pv_nmsuplem_i"),
										Ice2sigsService.TipoError.ErrWSsin.getCodigo(),
										"Error en ReclamoCod: " + reclamo.getIcodreclamo() + " Inciso: " + reclamo.getIncPol() 
												+ " >>> " + respuesta.getCodigo() + " - "
												+ respuesta.getMensaje(),
										 usuario, (String) params.get("pv_ntramite_i"), "ws.ice2sigs.url", "reclamoGS",
										 resultWS.getXmlIn(), Integer.toString(respuesta.getCodigo()));
							} catch (Exception e1) {
								logger.error("Error al insertar en Bitacora", e1);
							}
							
							res.setSuccess(false);
							res.setMensaje("Error al enviar alg\u00fan Reclamo para la solicitud de Pago. Intente nuevamente.");
						}
					}
					
				}catch(WSException e){
					logger.error("Error al enviar el Reclamo: " + reclamo.getIcodreclamo(), e);
					res.setSuccess(false);
					res.setMensaje("Error al enviar alg\u00fan Reclamo para la solicitud de Pago. Intente nuevamente.");
					logger.error("Imprimpriendo el xml enviado al WS: Payload: " + e.getPayload());
					try {
						kernelManager.movBitacobro(
								(String) params.get("pv_cdunieco_i"),
								(String) params.get("pv_cdramo_i"),
								(String) params.get("pv_estado_i"),
								(String) params.get("pv_nmpoliza_i"),
								(String) params.get("pv_nmsuplem_i"),
								Ice2sigsService.TipoError.ErrWSsinCx.getCodigo(),
								"Error en ReclamoCod: " + reclamo.getIcodreclamo() + " Inciso: " + reclamo.getIncPol()
										+ " Msg: " + e.getMessage() + " ***Cause: "
										+ e.getCause(),
								 usuario, (String) params.get("pv_ntramite_i"), "ws.ice2sigs.url", "reclamoGS",
								 e.getPayload(), null);
					} catch (Exception e1) {
						logger.error("Error al insertar en Bitacora", e1);
					}
				}catch (Exception e){
					logger.error("Error al enviar el Reclamo: " + reclamo.getIcodreclamo(), e);
					res.setSuccess(false);
					res.setMensaje("Error al enviar alg\u00fan Reclamo para la solicitud de Pago. Intente nuevamente.");
				}
			}
		}else {
			logger.debug("Sin datos para ejecutaWSreclamo.");
			res.setSuccess(false);
			res.setMensaje("Sin datos. No se han guardado correctamente los calculos.");
			return res;
		}

		return res;
	}
	
   @Override
   public boolean ejecutaWSrecibos(
           String cdunieco, 
           String cdramo, 
           String estado, 
           String nmpoliza, 
           String nmsuplem,
           String sucursal, 
           String ntramite, 
           boolean async, 
           UserVO userVO, 
           ReciboWrapper reciboWrapper) {
        
        boolean allInserted = true;
        String cdtipsit = null;
        String cdtipsitGS = null;
        
        logger.debug("*** Entrando a metodo Recibos WS ice2sigs, para la poliza: " + nmpoliza + " sucursal: " + sucursal + "***");
        
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("pv_cdunieco_i", cdunieco);
        params.put("pv_cdramo_i", cdramo);
        params.put("pv_estado_i", estado);
        params.put("pv_nmpoliza_i", nmpoliza);
        params.put("pv_nmsuplem_i", nmsuplem);
        params.put("pv_ntramite_i", ntramite);	        
        WrapperResultadosWS resultWS = null;

        String usuario = "SIN USUARIO";
        if(userVO != null){
            usuario = userVO.getUser();
        }
        
        if(async){
            params.put("USUARIO", usuario);
        }
        
        boolean comisionAgentes = false;
        Recibo recibo = reciboWrapper.getRecibo();
        Operacion operacion = Operacion.valueOf(reciboWrapper.getOperacion());
        
        if(recibo.getNumAgt() == -2){
            comisionAgentes = true;
        }
        
        try{	                
            if(async){
                // Se crea un HashMap por cada invocacion asincrona del WS, para evitar issue (sobreescritura de valores):
                HashMap<String, Object> paramsBitacora = new HashMap<String, Object>();
                paramsBitacora.putAll(params);
                paramsBitacora.put("NumRec", recibo.getNumRec());
                paramsBitacora.put("TipEnd", recibo.getTipEnd());
                paramsBitacora.put("NumEnd", recibo.getNumEnd());	                    
                ejecutaReciboGS(operacion, recibo, paramsBitacora, async);
            }else{
                resultWS = ejecutaReciboGS(operacion, recibo, null, async);
                ReciboRespuesta respuesta = (ReciboRespuesta) resultWS.getResultadoWS();
                logger.debug("Resultado al ejecutar el WS Recibo: " + recibo.getNumRec() + " >>>" + respuesta.getCodigo() + " - " + respuesta.getMensaje());
                logger.debug("XML de entrada: " + resultWS.getXmlIn());
                if (Estatus.EXITO.getCodigo() != respuesta.getCodigo()) {
                    logger.error("Guardando en bitacora el estatus");                    
                    if(Estatus.LLAVE_DUPLICADA.getCodigo() != respuesta.getCodigo() ){
                        allInserted =  false;
                    }
                    try {
                        kernelManager.movBitacobro((String) params.get("pv_cdunieco_i"),
                                (String) params.get("pv_cdramo_i"),
                                (String) params.get("pv_estado_i"),
                                (String) params.get("pv_nmpoliza_i"),
                                (String) params.get("pv_nmsuplem_i"),
                                Ice2sigsService.TipoError.ErrWSrec.getCodigo(),
                                "Error Recibo " + recibo.getNumRec() + 
                                " TipEnd: "     + recibo.getTipEnd() + 
                                " NumEnd: "     + recibo.getNumEnd() + 
                                " >>> " + 
                                respuesta.getCodigo() + " - "+ respuesta.getMensaje(),
                                usuario, (String) params.get("pv_ntramite_i"), "ws.ice2sigs.url", "reciboGS",
                                resultWS.getXmlIn(), Integer.toString(respuesta.getCodigo()));
                    } catch (Exception e1) {
                        logger.error("Error al insertar en Bitacora", e1);
                    }
                }
            }
        }catch(WSException e){
            allInserted =  false;	                
            logger.error("Error al insertar recibo: "+recibo.getNumRec()+" tramite: "+ntramite);
            logger.error("Imprimpriendo el xml enviado al WS: Payload: " + e.getPayload());
//            try {
//                kernelManager.movBitacobro(
//                        (String) params.get("pv_cdunieco_i"),
//                        (String) params.get("pv_cdramo_i"),
//                        (String) params.get("pv_estado_i"),
//                        (String) params.get("pv_nmpoliza_i"),
//                        (String) params.get("pv_nmsuplem_i"),
//                        Ice2sigsService.TipoError.ErrWSrecCx.getCodigo(),
//                        "Error Recibo " + recibo.getNumRec() + " TipEnd: " + recibo.getTipEnd() + " NumEnd: " + recibo.getNumEnd()
//                                + " Msg: " + e.getMessage() + " ***Cause: "
//                                + e.getCause(),
//                         usuario, (String) params.get("pv_ntramite_i"), "ws.ice2sigs.url", "reciboGS",
//                         e.getPayload(), null);
//            } catch (Exception e1) {
//                logger.error("Error al insertar en Bitacora", e1);
//            }
        }catch (Exception e){
            allInserted =  false;
            logger.error("Error Excepcion al insertar recibo: "+recibo.getNumRec()+" tramite: "+ntramite,e);
        }
        return allInserted;
   }
	
	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	
	public String getUrlImpresionRecibos() {
		return urlImpresionRecibos;
	}

	public void setUrlImpresionRecibos(String urlImpresionRecibos) {
		this.urlImpresionRecibos = urlImpresionRecibos;
	}

	
	/**
	 * Setter method
	 * @param kernelManager
	 */
	public void setKernelManager(KernelManagerSustituto kernelManager) {
		this.kernelManager = kernelManager;
	}

	public void setSiniestrosManager(SiniestrosManager siniestrosManager) {
		this.siniestrosManager = siniestrosManager;
	}

}
