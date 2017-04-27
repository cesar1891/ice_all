package mx.com.gseguros.ws.nada.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import mx.com.gseguros.exception.WSException;
import mx.com.gseguros.ws.model.WrapperResultadosWS;
import mx.com.gseguros.ws.nada.client.axis2.SecureLoginStub;
import mx.com.gseguros.ws.nada.client.axis2.SecureLoginStub.GetToken;
import mx.com.gseguros.ws.nada.client.axis2.SecureLoginStub.GetTokenRequest;
import mx.com.gseguros.ws.nada.client.axis2.SecureLoginStub.GetTokenResponse;
import mx.com.gseguros.ws.nada.client.axis2.VehicleStub;
import mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetDefaultVehicleAndValueByVin;
import mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetDefaultVehicleAndValueByVinResponse;
import mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetHighVehicleAndValueByVin;
import mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetHighVehicleAndValueByVinResponse;
import mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetVehicleValuesByVinRequest;
import mx.com.gseguros.ws.nada.client.axis2.VehicleStub.VehicleTypes;
import mx.com.gseguros.ws.nada.client.axis2.VehicleStub.VehicleValue_Struc;
import mx.com.gseguros.ws.nada.service.NadaService;

import org.apache.axis2.AxisFault;

/**
 * Implementacion de los metodos para invocar al WS recibossigs
 * @author Ricardo
 *
 */
public class NadaServiceImpl implements NadaService {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(NadaServiceImpl.class);
	
	private static final long WS_TIMEOUT =  20000;
	
	private String endpoint;
	private String endpointToken;
	private String user;
	private String password;
	
	public VehicleValue_Struc obtieneDatosAutomovilNADA(String vin){
		
		logger.debug(">>>>> Entrando a metodo WS obtieneDatosAutomovilNADA para vin: " + vin);
		VehicleValue_Struc datosVehiculo = null;
		
		try{
			// Obtenems la fecha actual en el formato yyyyMM:
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
			String fecha = formatter.format(Calendar.getInstance().getTime());
			
			WrapperResultadosWS resultWS = this.ejecutaDatosAutoNadaWS(vin, 0, VehicleTypes.UsedCar, Integer.parseInt(fecha), 3);
			datosVehiculo = (VehicleValue_Struc) resultWS.getResultadoWS();
			
		} catch(WSException wse){
			logger.error("Error en WS Datos Auto NADA, xml enviado: " + wse.getPayload(), wse);
		} catch (Exception e){
			logger.error("Error en WS obtieneDatosAutomovilNADA" + e.getMessage(),e);
		}
		
		
//		if (Estatus.EXITO.getCodigo() != calendarios.getCodigo()) {
//			logger.error("Guardando en bitacora el estatus");
//
//			try {
//				kernelManager.movBitacobro((String) params.get("pv_cdunieco_i"), (String) params.get("pv_cdramo_i"),
//						(String) params.get("pv_estado_i"), (String) params.get("pv_nmpoliza_i"), (String) params.get("pv_nmsuplem_i"),
//						RecibosSigsService.TipoError.ErrWsDXN.getCodigo(), calendarios.getCodigo() + " - " + calendarios.getMensaje(), userVO.getUser(), (String) params.get("pv_ntramite_i"),
//						"ws.recibossigs.url", "generaRecDxn", resultWS.getXmlIn(), Integer.toString(calendarios.getCodigo()));
//			} catch (Exception e1) {
//				logger.error("Error al insertar en Bitacora", e1);
//			}
//			return false;
//		}
		
		return datosVehiculo;
	}
	
	public String obtieneLoginTokenNada() throws Exception{
		
		String tokenGenerado = null;
		SecureLoginStub stubGS = null;
		
		try {
			logger.info(new StringBuffer("endpointToken a invocar=").append(endpointToken));
			stubGS = new SecureLoginStub(endpointToken);
			stubGS._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
		} catch (AxisFault e) {
			logger.error(e);
			throw new Exception("Error de preparacion de Axis2: " + e.getMessage());
		}
		
		GetTokenResponse respuestaToken = null;
		
		GetTokenRequest getTokenRequest = new GetTokenRequest(); 
		getTokenRequest.setUsername(user);
		getTokenRequest.setPassword(password);
		
		GetToken getToken =  new GetToken();
		getToken.setTokenRequest(getTokenRequest);
		
		try {
			respuestaToken = stubGS.getToken(getToken);
			tokenGenerado = respuestaToken.getGetTokenResult();
			logger.debug("Xml para generar Token: " + stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
			logger.debug("Resultado para ejecucion de WS obtieneLoginTokenNada: " + tokenGenerado);
		} catch (Exception re) {
			throw new WSException("Error de conexion: " + re.getMessage(), re, stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
		}
		
		return tokenGenerado;
	}

	public WrapperResultadosWS ejecutaDatosAutoNadaWS(String vin, int mileAge, VehicleTypes vehicleType, int period, int region) throws Exception{
		
		WrapperResultadosWS resultWS = new WrapperResultadosWS();
		VehicleValue_Struc datosVehiculo = null;
		VehicleValue_Struc datosVehiculoDefault = null;
		VehicleStub stubGS = null;
		
		try {
			logger.info(new StringBuffer("endpoint a invocar=").append(endpoint));
			stubGS = new VehicleStub(endpoint);
			stubGS._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
		} catch (AxisFault e) {
			logger.error(e);
			throw new Exception("Error de preparacion de Axis2: " + e.getMessage());
		}
		
		GetVehicleValuesByVinRequest getVehicleValuesByVinRequest = new GetVehicleValuesByVinRequest(); 
		
		try{
			getVehicleValuesByVinRequest.setToken(this.obtieneLoginTokenNada());
		}catch(WSException wse){
			logger.error("Error en WS Token, xml enviado: " + wse.getPayload(), wse);
			throw new Exception("Error al obtener el Token para esta peticion. " + wse.getMessage());
		}catch(Exception tkn){
			logger.error(tkn);
			throw new Exception("Error al obtener el Token para esta peticion. " + tkn.getMessage());
		}
		
		getVehicleValuesByVinRequest.setVin(vin);
		getVehicleValuesByVinRequest.setMileage(mileAge);
		getVehicleValuesByVinRequest.setVehicleType(vehicleType);
		getVehicleValuesByVinRequest.setPeriod(period);
		getVehicleValuesByVinRequest.setRegion(region);
		
		GetHighVehicleAndValueByVin getHighVehicleAndValueByVin12 =  new GetHighVehicleAndValueByVin();
		getHighVehicleAndValueByVin12.setVehicleRequest(getVehicleValuesByVinRequest);
		
		GetHighVehicleAndValueByVinResponse  getHighVehicleAndValueByVinResponse = null;
		
		GetDefaultVehicleAndValueByVin getDefaultVehicleAndValueByVin =  new GetDefaultVehicleAndValueByVin();
		getDefaultVehicleAndValueByVin.setVehicleRequest(getVehicleValuesByVinRequest);
		
		GetDefaultVehicleAndValueByVinResponse  getDefaultVehicleAndValueByVinResponse = null;
		
		try {
			getHighVehicleAndValueByVinResponse = stubGS.getHighVehicleAndValueByVin(getHighVehicleAndValueByVin12);
			datosVehiculo = getHighVehicleAndValueByVinResponse.getGetHighVehicleAndValueByVinResult();
			
			getDefaultVehicleAndValueByVinResponse = stubGS.getDefaultVehicleAndValueByVin(getDefaultVehicleAndValueByVin);
			datosVehiculoDefault = getDefaultVehicleAndValueByVinResponse.getGetDefaultVehicleAndValueByVinResult();
			
			
			if( datosVehiculo != null){
				datosVehiculo.setAvgTradeIn(datosVehiculoDefault.getAvgTradeIn());
			}else{
				logger.error("Respuesta de WS NADA Nula.");
			}
			
			resultWS.setResultadoWS(datosVehiculo);
			resultWS.setXmlIn(stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
			
			logger.debug("Xml enviado para obtener los datos del auto: " + stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
			
		} catch (Exception re) {
			throw new WSException("Error de conexion: " + re.getMessage(), re, stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
		}
		
		return resultWS;
	}
	
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public void setEndpointToken(String endpointToken) {
		this.endpointToken = endpointToken;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}