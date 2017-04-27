package mx.com.gseguros.ws.tipocambio.service.impl;

import mx.com.gseguros.exception.WSException;
import mx.com.gseguros.ws.model.WrapperResultadosWS;
import mx.com.gseguros.ws.tipocambio.client.axis2.TipoCambioWSServiceStub;
import mx.com.gseguros.ws.tipocambio.client.axis2.TipoCambioWSServiceStub.ObtenerTipoCambio;
import mx.com.gseguros.ws.tipocambio.client.axis2.TipoCambioWSServiceStub.ObtenerTipoCambioE;
import mx.com.gseguros.ws.tipocambio.client.axis2.TipoCambioWSServiceStub.ObtenerTipoCambioResponseE;
import mx.com.gseguros.ws.tipocambio.client.axis2.TipoCambioWSServiceStub.ResponseTipoCambio;
import mx.com.gseguros.ws.tipocambio.service.TipoCambioDolarGSService;

import org.apache.axis2.AxisFault;

/**
 * Implementacion de los metodos para invocar al WS recibossigs
 * @author Ricardo
 *
 */
public class TipoCambioDolarGSServiceImpl implements TipoCambioDolarGSService {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TipoCambioDolarGSService.class);
	
	private static final long WS_TIMEOUT =  20000;
	
	private String endpoint;
	
	
	public ResponseTipoCambio obtieneTipoCambioDolarGS(int tipoMoneda){
		
		logger.debug(">>>>> Entrando a metodo WS obtieneTipoCambio para tipo Moneda: " + tipoMoneda);
		ResponseTipoCambio tipoCambio = null;
		
		try{
			
			WrapperResultadosWS resultWS = this.ejecutaTipoCambioDolarGS(tipoMoneda);
			tipoCambio = (ResponseTipoCambio) resultWS.getResultadoWS();
			
		} catch(WSException wse){
			logger.error("Error en WS Tipo Cambio Dolar, xml enviado: " + wse.getPayload(), wse);
		} catch (Exception e){
			logger.error("Error en WS Tipocambio Dolar" + e.getMessage(),e);
		}
		
		return tipoCambio;
	}
	
	public WrapperResultadosWS ejecutaTipoCambioDolarGS(int tipoMoneda) throws Exception{
		
		WrapperResultadosWS resultWS = new WrapperResultadosWS();
		ResponseTipoCambio resTipoCambio = null;
		TipoCambioWSServiceStub stubGS = null;
		
		try {
			logger.info(new StringBuffer("endpoint a invocar=").append(endpoint));
			stubGS = new TipoCambioWSServiceStub(endpoint);
			stubGS._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
		} catch (AxisFault e) {
			logger.error(e);
			throw new Exception("Error de preparacion de Axis2: " + e.getMessage());
		}
		
		ObtenerTipoCambio obtenerTipoCambio =  new ObtenerTipoCambio();
		obtenerTipoCambio.setTipo_moneda(tipoMoneda);
		
		ObtenerTipoCambioE obteberTipoCambioE = new ObtenerTipoCambioE();
		obteberTipoCambioE.setObtenerTipoCambio(obtenerTipoCambio);
		
		try {
			ObtenerTipoCambioResponseE obtenerTipoCambioResponseE = stubGS.obtenerTipoCambio(obteberTipoCambioE);
			resTipoCambio = obtenerTipoCambioResponseE.getObtenerTipoCambioResponse().get_return();
			
			resultWS.setResultadoWS(resTipoCambio);
			resultWS.setXmlIn(stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
			
			logger.debug("Xml enviado para obtener los datos del auto: " + stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
			
			if(resTipoCambio != null && resTipoCambio.getExito() && resTipoCambio.getTipoCambio() != null && resTipoCambio.getTipoCambio().getVenCam() != null){
				logger.info("Respuesta Tipo de Cambio Venta WS: " + resTipoCambio.getTipoCambio().getVenCam());
			}else{
				logger.error("Resultado de Tipo de Cambio WS es Nulo o no fue existoso.");
			}
			
		} catch (Exception re) {
			throw new WSException("Error de conexion: " + re.getMessage(), re, stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
		}
		
		return resultWS;
	}
	
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

}