package mx.com.gseguros.ws.autosgs.infovehiculo.service.impl;

import mx.com.gseguros.exception.WSException;
import mx.com.gseguros.ws.autosgs.infovehiculo.client.axis2.VehiculoWSServiceStub;
import mx.com.gseguros.ws.autosgs.infovehiculo.client.axis2.VehiculoWSServiceStub.ObtenerValorComercial;
import mx.com.gseguros.ws.autosgs.infovehiculo.client.axis2.VehiculoWSServiceStub.ObtenerValorComercialE;
import mx.com.gseguros.ws.autosgs.infovehiculo.client.axis2.VehiculoWSServiceStub.ObtenerValorComercialResponseE;
import mx.com.gseguros.ws.autosgs.infovehiculo.client.axis2.VehiculoWSServiceStub.ResponseValor;
import mx.com.gseguros.ws.autosgs.infovehiculo.service.InfoVehiculoService;
import mx.com.gseguros.ws.model.WrapperResultadosWS;

import org.apache.axis2.AxisFault;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class InfoVehiculoServiceImpl implements InfoVehiculoService {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(InfoVehiculoServiceImpl.class);
	
	private static final long WS_TIMEOUT =  20000;
	
	@Value("${ws.info.valor.auto.url}")
	private String endpoint;
	
	
	public ResponseValor obtieneDatosVehiculoGS(int claveGS, int modelo){
		
		logger.debug(">>>>> Entrando a metodo WS validarPolizaTractoCamion");
		
		ResponseValor resVal = null;
		
		if(claveGS > 0 && modelo > 0){
			try{
				WrapperResultadosWS resultWS = this.ejecutaObtieneDatosVehiculoGS(claveGS, modelo);
						
				logger.info(
						new StringBuilder()
						.append("\n@@@@@@@@@@@@@@@@@@@@@@@@")
						.append("\n@@@@@@ WEBSERVICE @@@@@@")
						.append("\n@@@@@@ cdtipsit= ").append(resultWS)
						);
				
				resVal = (ResponseValor) resultWS.getResultadoWS();
				logger.info(
						new StringBuilder()
						.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
						.append("\n@@@@@@ WEBSERVICE @@@@@@")
						.append("\n@@@@@@ cdtipsit=").append(resVal)
						);
				
				if( resVal != null && resVal.getExito()){
					logger.debug("Respuesta de WS ejecutaObtieneDatosVehiculoGS Codigo(): " + resVal.getExito());
					logger.debug("Respuesta de WS ejecutaObtieneDatosVehiculoGS Mensaje(): " +resVal.getMensaje());
				}else{
					logger.error("WS ejecutaObtieneDatosVehiculoGS, Resultado devuelto nulo o no existoso.");
				}
				
			} catch(WSException wse){
				logger.error("Error en WS ejecutaObtieneDatosVehiculoGS, xml enviado: " + wse.getPayload(), wse);
			} catch (Exception e){
				logger.error("Error en WS ejecutaObtieneDatosVehiculoGS: " + e.getMessage(),e);
			}
		}else{
			logger.error("Error en WS ejecutaObtieneDatosVehiculoGS datos en cero!");
		}
		
		return resVal;
	}
	
	private WrapperResultadosWS ejecutaObtieneDatosVehiculoGS(int claveGS, int modelo) throws Exception{
		
		WrapperResultadosWS resultWS = new WrapperResultadosWS();
		VehiculoWSServiceStub stubGS = null;
		
		try {
			logger.info(new StringBuffer("endpoint a invocar=").append(endpoint));
			stubGS = new VehiculoWSServiceStub(endpoint);
			stubGS._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
		} catch (AxisFault e) {
			logger.error(e);
			throw new Exception("Error de preparacion de Axis2: " + e.getMessage());
		}
		
		ObtenerValorComercial obtenerValorComercial = new ObtenerValorComercial();
		obtenerValorComercial.setCve_gs(claveGS);
		obtenerValorComercial.setModelo(modelo);
		
		ObtenerValorComercialE obtenerValorComercialE = new ObtenerValorComercialE();
		obtenerValorComercialE.setObtenerValorComercial(obtenerValorComercial);
		
		try {
			ObtenerValorComercialResponseE responseE = stubGS.obtenerValorComercial(obtenerValorComercialE);
			ResponseValor resVal = responseE.getObtenerValorComercialResponse().get_return();
			
			resultWS.setResultadoWS(resVal);
			resultWS.setXmlIn(stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
			
			logger.debug("Xml enviado para ejecutavalidarPolizaTractoCamion: " + stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
			
		} catch (Exception re) {
			throw new WSException("Error de conexion ejecutavalidarPolizaTractoCamion: " + re.getMessage(), re, stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
		}
		
		return resultWS;
	}
	
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

}