package mx.com.gseguros.ws.folioserviciopublico.service.impl;

import mx.com.gseguros.exception.WSException;
import mx.com.gseguros.ws.folioserviciopublico.client.axis2.FolioWSServiceStub;
import mx.com.gseguros.ws.folioserviciopublico.client.axis2.FolioWSServiceStub.EmAdmfolId;
import mx.com.gseguros.ws.folioserviciopublico.client.axis2.FolioWSServiceStub.RequestFolio;
import mx.com.gseguros.ws.folioserviciopublico.client.axis2.FolioWSServiceStub.ResponseFolio;
import mx.com.gseguros.ws.folioserviciopublico.client.axis2.FolioWSServiceStub.ValidarFolio;
import mx.com.gseguros.ws.folioserviciopublico.client.axis2.FolioWSServiceStub.ValidarFolioE;
import mx.com.gseguros.ws.folioserviciopublico.client.axis2.FolioWSServiceStub.ValidarFolioResponseE;
import mx.com.gseguros.ws.folioserviciopublico.service.AgentePorFolioService;
import mx.com.gseguros.ws.model.WrapperResultadosWS;

import org.apache.axis2.AxisFault;

/**
 * Implementaci�n de los m�todos para invocar al WS recibossigs
 * @author Ricardo
 *
 */
public class AgentePorFolioServiceImpl implements AgentePorFolioService {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AgentePorFolioServiceImpl.class);
	
	private static final long WS_TIMEOUT =  20000;
	
	private String endpoint;
	
	
	public EmAdmfolId obtieneAgentePorFolioSucursal(int numFolio, int sucursalAdmin, int cdramo, String cdsisrol, String cdusuari, int idusu, String cdtipsit){
		
		logger.debug(">>>>> Entrando a metodo WS ObtieneAgentePorFolioSucursal para servicio publico");
		
		EmAdmfolId resAgenteFolio = null;
		
		if(numFolio != 0 ){
			try{
				WrapperResultadosWS resultWS = this.ejecutaAgentePorFolioYSucursalWS(numFolio, sucursalAdmin, cdramo, cdsisrol, cdusuari, idusu, cdtipsit);
						
				ResponseFolio resFol = (ResponseFolio) resultWS.getResultadoWS();
				
				if( resFol != null && resFol.getExito()){
					logger.debug("Respuesta de WS AgentePorFolioYSucursal Codigo(): " +resFol.getCodigo());
					logger.debug("Respuesta de WS AgentePorFolioYSucursal Mensaje(): " +resFol.getMensaje());
					
					resAgenteFolio = resFol.getFolio();
					
					if(resAgenteFolio != null && resAgenteFolio.getNumAge() != 0){
						logger.debug("Agente obtenido: " + resAgenteFolio.getNumAge());
					}else {
						logger.error("Error en WS obtener Agente por Folio y sucursal, el numero de Agente que regresa es 0");
						resAgenteFolio = null;
					}
					
				}else{
					logger.error("Error en Autos WS AgentePorFolioYSucursal, respuesta no exitosa");
				}
				
			} catch(WSException wse){
				logger.error("Error en WS obtener Agente por Folio y sucursal, xml enviado: " + wse.getPayload(), wse);
			} catch (Exception e){
				logger.error("Error en WS obtener Agente por Folio y sucursal: " + e.getMessage(),e);
			}
		}else{
			logger.error("Error en WS obtener Agente por Folio y sucursal, El valor del Folio es 0");
		}
		
		return resAgenteFolio;
	}
	
	private WrapperResultadosWS ejecutaAgentePorFolioYSucursalWS(int numFolio, int sucursalAdmin, int cdramo, String cdsisrol, String cdusuari, int idusu, String cdtipsit) throws Exception{
		
		WrapperResultadosWS resultWS = new WrapperResultadosWS();
		FolioWSServiceStub stubGS = null;
		
		try {
			logger.info(new StringBuffer("endpoint a invocar=").append(endpoint));
			stubGS = new FolioWSServiceStub(endpoint);
			stubGS._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
		} catch (AxisFault e) {
			logger.error(e);
			throw new Exception("Error de preparacion de Axis2: " + e.getMessage());
		}
		
		RequestFolio requestFolio = new RequestFolio();
		requestFolio.setNum_folio(numFolio);
		requestFolio.setSucursal_admin(sucursalAdmin);
		
		requestFolio.setCdramo(cdramo);
		requestFolio.setCdsisrol(cdsisrol);
		requestFolio.setCdtipsit(cdtipsit);
		requestFolio.setCdusuario(cdusuari);
		requestFolio.setId_usu(idusu);
		
		ValidarFolio validarFolio = new ValidarFolio();
		validarFolio.setArg0(requestFolio);
		
		ValidarFolioE validarFolioE = new ValidarFolioE();
		validarFolioE.setValidarFolio(validarFolio);
		
		try {
			ValidarFolioResponseE validResE = stubGS.validarFolio(validarFolioE);
			ResponseFolio resFol = validResE.getValidarFolioResponse().get_return();
			
			resultWS.setResultadoWS(resFol);
			resultWS.setXmlIn(stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
			
			logger.debug("Xml enviado para obtener Agente por Folio y sucursal: " + stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
			
		} catch (Exception re) {
			throw new WSException("Error de conexion obtener Agente por Folio y sucursal: " + re.getMessage(), re, stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
		}
		
		return resultWS;
	}
	
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

}