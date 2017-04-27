package mx.com.gseguros.portal.general.controller;

import java.util.List;
import java.util.Map;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.gseguros.portal.general.service.WebServicesManager;
import mx.com.gseguros.utils.WebServicesUtil;
import mx.com.gseguros.ws.ice2sigs.service.Ice2sigsService;
import mx.com.gseguros.ws.recibossigs.client.axis2.GeneradorReciboDxnWsServiceStub.GeneraRecDxnResponseE;
import mx.com.gseguros.ws.recibossigs.service.RecibosSigsService;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.llom.util.AXIOMUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class WebServicesAction extends PrincipalCoreAction {

	private static final long serialVersionUID = -7264510862184393230L;
	
	private Logger logger = Logger.getLogger(WebServicesAction.class);
	
	private WebServicesManager webServicesManager;
	private Ice2sigsService ice2sigsService;
	private RecibosSigsService recibosSigsService;
	
	private boolean success;
	
	private String mensajeRespuesta;
	
	private Map<String,String> params;
	
	private List<Map<String, String>> loadList;
    private List<Map<String, String>> saveList;

    
    public String obtienePeticionesFallidasWS() throws Exception {
       	try {
       		loadList = webServicesManager.obtienePeticionesFallidasWS(params);
       	}catch( Exception e){
       		logger.error("Error en obtienePeticionesFallidasWS",e);
       		success =  false;
       		return SUCCESS;
       	}
       	success = true;
       	return SUCCESS;
    }

    public String obtienePayloadPeticionWS(){
    	
    	try {
    		loadList = webServicesManager.obtieneDetallePeticionWS(params);
    		mensajeRespuesta = WebServicesUtil.formatXml(loadList.get(0).get("XMLIN"));
    	}catch( Exception e){
    		mensajeRespuesta = e.getMessage();
    		logger.error("Error en obtienePayloadPeticionWS",e);
    		success =  false;
    		return SUCCESS;
    	}
    	success = true;
    	return SUCCESS;
    }
    
    public String reenviaPeticionWS(){
    	
    	try {
    		loadList = webServicesManager.obtieneDetallePeticionWS(params);
    		Map<String, String> peticionWS = loadList.get(0);
    		logger.debug("Datos a detalle de la peticion WS a ejecutar: " + peticionWS);

    		String urlWS =  getText(peticionWS.get("CDURLWS"));
    		String metodoWS = peticionWS.get("METODOWS");
    		String xmlEnvio = peticionWS.get("XMLIN");
    		
    		logger.debug("Url WS: " + urlWS);
    		logger.debug("Metodo WS: " + metodoWS);
    		
    		/**
    		 * Si es el ws de reclamos se avisa que no es debido ejecutarlo de esta forma pues faltan otros procesos
    		 * para este caso si se puede ejecutar desde un boton el cual es la solicitud de pago en la mesa de control
    		 */
    		if("ws.ice2sigs.url".equals(peticionWS.get("CDURLWS")) && "reclamoGS".equalsIgnoreCase(metodoWS)){
    			mensajeRespuesta = "Por favor ejecute este WS desde el boton Solicitar Pago en la pantalla de Mesa de Control de Siniestros.";
        		success = false;
        		return SUCCESS;	
    		}
    		
    		/**
    		 * Invocacion del WS de la peticion
    		 */
    		OMElement resultadoWS = WebServicesUtil.invocaWebServiceAxis2Soap11(urlWS, metodoWS, AXIOMUtil.stringToOM(xmlEnvio), null , null, false);
    		mensajeRespuesta = WebServicesUtil.formatXml(resultadoWS.toString());
    		
    		
    		/**
    		 * Si es el Ws de obtencion de Calendarios de DXN se deben de mandar a insertar a la bd, generar pdfs y finalizar el proceso
    		 * Debe recordarse que para la ejecucion de este WS anteriormente se debio de enviar el WS de el primer Recibo
    		 */
    		if("ws.recibossigs.url".equals(peticionWS.get("CDURLWS")) && "generaRecDxn".equalsIgnoreCase(metodoWS)){
    			
    			String sucursal = peticionWS.get("CDUNIECO");
    			if(StringUtils.isNotBlank(sucursal) && "1".equals(sucursal)) sucursal = "1000";
    			
    			GeneraRecDxnResponseE calendarios = GeneraRecDxnResponseE.Factory.parse(resultadoWS.getXMLStreamReaderWithoutCaching());
    			
    			boolean gcal = false;
    					
    			if(RecibosSigsService.Estatus.EXITO.getCodigo() == calendarios.getGeneraRecDxnResponse().get_return().getCodigo()){
    				gcal = recibosSigsService.guardaCalendariosDxnFinaliza(peticionWS.get("CDUNIECO"), peticionWS.get("CDRAMO"), peticionWS.get("ESTADO"), peticionWS.get("NMPOLIZA"),
        					peticionWS.get("NMSUPLEM"), sucursal, null, peticionWS.get("NTRAMITE"), calendarios.getGeneraRecDxnResponse().get_return());
    			}
    			
    			if(!gcal){
    				mensajeRespuesta = "Detalle: Error al insertar los calendarios y metodo DxnFinaliza. El tipo de resultado que devuelve el WS no se pudo parsear.";
            		success = false;
            		return SUCCESS;	
    			}
    		}
    		
    	}catch( Exception e){
    		mensajeRespuesta = e.getMessage();
    		logger.error("Error en reenviaPeticionWS",e);
    		success =  false;
    		return SUCCESS;
    	}
    	success = true;
    	return SUCCESS;
    }
    
    public String eliminaPeticionWS(){
    	
    	logger.debug("eliminaPeticionesWS SaveList: "+ saveList);
    	try {
    		if(!webServicesManager.eliminaPeticionWS(saveList)){
        		success = false;
        		return SUCCESS;	
    		}
    	}catch( Exception e){
    		logger.error("Error en eliminaPeticionWS",e);
    		success =  false;
    		return SUCCESS;
    	}
    	success = true;
    	return SUCCESS;
    }

    // getters and setters
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Map<String,String> getParams() {
		return params;
	}

	public void setParams(Map<String,String> params) {
		this.params = params;
	}
	
	public List<Map<String, String>> getLoadList() {
		return loadList;
	}

	public void setLoadList(List<Map<String, String>> loadList) {
		this.loadList = loadList;
	}

	public List<Map<String, String>> getSaveList() {
		return saveList;
	}

	public void setSaveList(List<Map<String, String>> saveList) {
		this.saveList = saveList;
	}



	public void setWebServicesManager(WebServicesManager webServicesManager) {
		this.webServicesManager = webServicesManager;
	}



	public void setIce2sigsService(Ice2sigsService ice2sigsService) {
		this.ice2sigsService = ice2sigsService;
	}



	public void setRecibosSigsService(RecibosSigsService recibosSigsService) {
		this.recibosSigsService = recibosSigsService;
	}



	public String getMensajeRespuesta() {
		return mensajeRespuesta;
	}



	public void setMensajeRespuesta(String mensajeRespuesta) {
		this.mensajeRespuesta = mensajeRespuesta;
	}

}