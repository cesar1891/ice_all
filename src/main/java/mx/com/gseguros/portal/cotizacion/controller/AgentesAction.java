package mx.com.gseguros.portal.cotizacion.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.kernel.service.KernelManagerSustituto;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.portal.consultas.model.ConsultaDatosAgenteVO;
import mx.com.gseguros.portal.cotizacion.model.AgentePolizaVO;

public class AgentesAction extends PrincipalCoreAction
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AgentesAction.class);
	private List<HashMap<String,String>> datosPorcentajeAgente;
	private List<GenericVO> datosGeneralesAgente;    
    private ConsultaDatosAgenteVO datosAgente;
    private HashMap<String,String> params;
    private List<AgentePolizaVO> datosPolizaAgente;
    private boolean success;
    private KernelManagerSustituto kernelManager;
    
    public String execute() throws Exception {
    	return SUCCESS;
    }
    
	public String principal()
	{
		log.debug(""
				+ "\n#######################################"
				+ "\n#######################################"
				+ "\n###### mesa de control principal ######"
				+ "\n######                           ######"
				);
		log.debug(""
				+ "\n######                           ######"
				+ "\n###### mesa de control principal ######"
				+ "\n#######################################"
				+ "\n#######################################"
				);
		return SUCCESS;
	}
	
	public KernelManagerSustituto getKernelManager() {
		return kernelManager;
	}

	public void setKernelManager(KernelManagerSustituto kernelManager) {
		this.kernelManager = kernelManager;
	}

	/**
	    * Obtiene los datos de la tarifa de la poliza
	    * @return String result
	    */
	   public String obtieneAgentesPolizas(){
	   	logger.debug(" **** Entrando a obtieneAgentesPolizas ****");
	   	try {
	   		
	   		WrapperResultados result = kernelManager.obtenerAgentePoliza(
						params.get("cdunieco"), params.get("cdramo"),
						params.get("estado"), params.get("nmpoliza"));
	   		
	   		datosPolizaAgente = (ArrayList<AgentePolizaVO>) result.getItemList();
	   		
	   		logger.debug("Resultado de la consultaDatosPolizaAgente:" + datosPolizaAgente);
	   		
	   	}catch( Exception e){
	   		logger.error("Error al obtener los consultaDatosPolizaAgente ",e);
	   		return SUCCESS;
	   	}
	   	
	   	success = true;
	   	return SUCCESS;
	   	
	   }

	   /**
	    * Obtiene los datos de la tarifa de la poliza por puntos
	    * @return String result
	    */
	   public String obtenerAgentePolizaPorPuntos(){
		   logger.debug(" **** Entrando a obtenerAgentePolizaPorPuntos ****");
		   try {
			   
			   WrapperResultados result = kernelManager.obtenerAgentePolizaPorPuntos(
					   params.get("cdunieco"), params.get("cdramo"),
					   params.get("estado"), params.get("nmpoliza"));
			   
			   datosPolizaAgente = (ArrayList<AgentePolizaVO>) result.getItemList();
			   
			   logger.debug("Resultado de la obtenerAgentePolizaPorPuntos:" + datosPolizaAgente);
			   
		   }catch( Exception e){
			   logger.error("Error al obtener los obtenerAgentePolizaPorPuntos ",e);
			   return SUCCESS;
		   }
		   
		   success = true;
		   return SUCCESS;
		   
	   }
	   
	   
	   public String obtieneTiposAgente(){
		   	logger.debug(" **** Entrando a consultaGeneralAgente ****");
		   	try {
		   		
		   		WrapperResultados result = kernelManager.obtenerTiposAgente();
		   		
		   		datosGeneralesAgente = (ArrayList<GenericVO>) result.getItemList();
		   		logger.debug("Resultado de la consultaGeneralAgente:" + datosGeneralesAgente);
		   		
		   	}catch( Exception e){
		   		logger.error("Error al obtener los consultaDatosPolizaAgente ",e);
		   		return SUCCESS;
		   	}
		   	
		   	success = true;
		   	return SUCCESS;
		   	
		   }


	   /**
	    * Obtiene los datos de la tarifa de la poliza
	    * @return String result
	    */
	   public String guardaPorcentajeAgentes(){
	   	logger.debug(" **** Entrando a guardaPorcentajeAgentes ****");
	   	try {
	   			//Obtenemos los datos globales para el envio
		   		for(int i=0;i<datosPorcentajeAgente.size();i++)
		   		{
		   			//System.out.println("CONTADOR  "+i+ " VALOR --> "+datosPorcentajeAgente.get(i));
		   			Map<String,Object>mapArchivo=new LinkedHashMap<String,Object>(0);
		   	   		mapArchivo.put("pi_CDUNIECO" , params.get("cdunieco"));	
		   	   		mapArchivo.put("pi_CDRAMO"   , params.get("ramo"));
		   	   		mapArchivo.put("pi_ESTADO"   , params.get("estado"));
		   	   		mapArchivo.put("pi_NMPOLIZA" , params.get("nmpoliza"));
			   	   	mapArchivo.put("pi_CDAGENTE_NVO" , datosPorcentajeAgente.get(i).get("cdagente"));// nuevo
			   	    mapArchivo.put("pi_CDAGENTE" , datosPorcentajeAgente.get(i).get("cdagenteA"));	//	anterior
			   		mapArchivo.put("pi_NMSUPLEM" , datosPorcentajeAgente.get(i).get("nmsuplem"));
			   		mapArchivo.put("pi_CDTIPOAG" , datosPorcentajeAgente.get(i).get("cdtipoAg"));
			   		mapArchivo.put("pi_PORREDAU" , datosPorcentajeAgente.get(i).get("porredau"));
			   		mapArchivo.put("pi_NMCUADRO" , datosPorcentajeAgente.get(i).get("nmcuadro"));
			   		mapArchivo.put("pi_CDSUCURS" , datosPorcentajeAgente.get(i).get("cdsucurs"));
			   		mapArchivo.put("pi_PORPARTI" , datosPorcentajeAgente.get(i).get("porparti"));
			   		mapArchivo.put("pi_NVOPCCOM" , datosPorcentajeAgente.get(i).get("nvacomision"));
			   		WrapperResultados result=kernelManager.guardarPorcentajeAgentes(mapArchivo) ;		   		
		   		}
	   	}catch( Exception e){
	   		logger.error("Error al obtener los consultaDatosPolizaAgente ",e);
	   		return SUCCESS;
	   	}
	   	
	   	success = true;
	   	return SUCCESS;
	   	
	   }

	   /**
	    * Obtiene los datos de la tarifa de la poliza
	    * @return String result
	    */
	   public String guardaPorcentajeAgentesPorPuntos(){
		   logger.debug(" **** Entrando a guardaPorcentajeAgentesPorPuntos ****");
		   try {
			   //Obtenemos los datos globales para el envio
			   for(int i=0;i<datosPorcentajeAgente.size();i++)
			   {
				   //System.out.println("CONTADOR  "+i+ " VALOR --> "+datosPorcentajeAgente.get(i));
				   Map<String,Object>mapArchivo=new LinkedHashMap<String,Object>(0);
				   mapArchivo.put("pi_CDUNIECO" , params.get("cdunieco"));	
				   mapArchivo.put("pi_CDRAMO"   , params.get("ramo"));
				   mapArchivo.put("pi_ESTADO"   , params.get("estado"));
				   mapArchivo.put("pi_NMPOLIZA" , params.get("nmpoliza"));
				   mapArchivo.put("pi_CDAGENTE_NVO" , datosPorcentajeAgente.get(i).get("cdagente"));// nuevo
				   mapArchivo.put("pi_CDAGENTE" , datosPorcentajeAgente.get(i).get("cdagenteA"));	//	anterior
				   mapArchivo.put("pi_NMSUPLEM" , datosPorcentajeAgente.get(i).get("nmsuplem"));
				   mapArchivo.put("pi_CDTIPOAG" , datosPorcentajeAgente.get(i).get("cdtipoAg"));
				   mapArchivo.put("pi_PORREDAU" , datosPorcentajeAgente.get(i).get("porredau"));
				   mapArchivo.put("pi_NMCUADRO" , datosPorcentajeAgente.get(i).get("nmcuadro"));
				   mapArchivo.put("pi_CDSUCURS" , datosPorcentajeAgente.get(i).get("cdsucurs"));
				   mapArchivo.put("pi_PORPARTI" , datosPorcentajeAgente.get(i).get("porparti"));
				   mapArchivo.put("COMISION"    , datosPorcentajeAgente.get(i).get("comision"));
				   mapArchivo.put("pi_NVOPCCOM" , datosPorcentajeAgente.get(i).get("nvacomision"));
				   WrapperResultados result=kernelManager.guardaPorcentajeAgentesPorPuntos(mapArchivo) ;		   		
			   }
		   }catch( Exception e){
			   logger.error("Error al obtener los guardaPorcentajeAgentesPorPuntos ",e);
			   return SUCCESS;
		   }
		   
		   success = true;
		   return SUCCESS;
		   
	   }

	   public String guardaEliminaPorcentajeAgentes(){
		   	logger.debug(" **** Entrando a guardaEliminaPorcentajeAgentes ****");
		   	logger.debug(params);
		   	try {
			   		Map<String,Object>mapArchivo=new LinkedHashMap<String,Object>(0);
		   	   		mapArchivo.put("pi_CDUNIECO" , params.get("cdunieco"));	
		   	   		mapArchivo.put("pi_CDRAMO"   , params.get("ramo"));
		   	   		mapArchivo.put("pi_ESTADO"   , params.get("estado"));
		   	   		mapArchivo.put("pi_NMPOLIZA" , params.get("nmpoliza"));
			   	   	mapArchivo.put("pi_CDAGENTE_NVO" , params.get("cdagente"));// nuevo
			   	    mapArchivo.put("pi_CDAGENTE" , params.get("cdagenteA"));//	anterior
			   		mapArchivo.put("pi_NMSUPLEM" , params.get("nmsuplem"));
			   		mapArchivo.put("pi_CDTIPOAG" , params.get("cdtipoAg"));
			   		mapArchivo.put("pi_PORREDAU" , params.get("porredau"));
			   		mapArchivo.put("pi_NMCUADRO" , params.get("nmcuadro"));
			   		mapArchivo.put("pi_CDSUCURS" , params.get("cdsucurs"));
			   		mapArchivo.put("pi_PORPARTI" , params.get("porparti"));
			   		mapArchivo.put("pi_NVOPCCOM" , params.get("nvacomision"));
			   		mapArchivo.put("pi_ACCION"   , params.get("accion"));
			   		WrapperResultados result=kernelManager.guardarEliminarPorcentajeAgentes(mapArchivo) ;
			   		
		   	}catch( Exception e){
		   		logger.error("Error al obtener los consultaDatosPolizaAgente ",e);
		   		return SUCCESS;
		   	}
		   	
		   	success = true;
		   	return SUCCESS;
		   	
		   }

	   public String guardaEliminaPorcentajeAgentesPorPuntos(){
		   logger.debug(" **** Entrando a guardaEliminaPorcentajeAgentesPorPuntos ****");
		   logger.debug(params);
		   try {
			   Map<String,Object>mapArchivo=new LinkedHashMap<String,Object>(0);
			   mapArchivo.put("pi_CDUNIECO" , params.get("cdunieco"));	
			   mapArchivo.put("pi_CDRAMO"   , params.get("ramo"));
			   mapArchivo.put("pi_ESTADO"   , params.get("estado"));
			   mapArchivo.put("pi_NMPOLIZA" , params.get("nmpoliza"));
			   mapArchivo.put("pi_CDAGENTE_NVO" , params.get("cdagente"));// nuevo
			   mapArchivo.put("pi_CDAGENTE" , params.get("cdagenteA"));//	anterior
			   mapArchivo.put("pi_NMSUPLEM" , params.get("nmsuplem"));
			   mapArchivo.put("pi_CDTIPOAG" , params.get("cdtipoAg"));
			   mapArchivo.put("pi_PORREDAU" , params.get("porredau"));
			   mapArchivo.put("pi_NMCUADRO" , params.get("nmcuadro"));
			   mapArchivo.put("pi_CDSUCURS" , params.get("cdsucurs"));
			   mapArchivo.put("pi_PORPARTI" , params.get("porparti"));
			   mapArchivo.put("COMISION"    , params.get("comision"));
			   mapArchivo.put("pi_NVOPCCOM" , params.get("nvacomision"));
			   mapArchivo.put("pi_ACCION"   , params.get("accion"));
			   WrapperResultados result=kernelManager.guardaEliminaPorcentajeAgentesPorPuntos(mapArchivo) ;
			   
		   }catch( Exception e){
			   logger.error("Error al obtener los consultaDatosPolizaAgente ",e);
			   return SUCCESS;
		   }
		   
		   success = true;
		   return SUCCESS;
		   
	   }

		
	   public List<HashMap<String, String>> getDatosPorcentajeAgente() {
		return datosPorcentajeAgente;
	}

	public void setDatosPorcentajeAgente(
			List<HashMap<String, String>> datosPorcentajeAgente) {
		this.datosPorcentajeAgente = datosPorcentajeAgente;
	}

	public List<GenericVO> getDatosGeneralesAgente() {
		return datosGeneralesAgente;
	}

	public void setDatosGeneralesAgente(List<GenericVO> datosGeneralesAgente) {
		this.datosGeneralesAgente = datosGeneralesAgente;
	}

	public ConsultaDatosAgenteVO getDatosAgente() {
		return datosAgente;
	}

	public void setDatosAgente(ConsultaDatosAgenteVO datosAgente) {
		this.datosAgente = datosAgente;
	}

	public HashMap<String, String> getParams() {
		return params;
	}

	public void setParams(HashMap<String, String> params) {
		this.params = params;
	}

	public List<AgentePolizaVO> getDatosPolizaAgente() {
		return datosPolizaAgente;
	}

	public void setDatosPolizaAgente(
			List<AgentePolizaVO> datosPolizaAgente) {
		this.datosPolizaAgente = datosPolizaAgente;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	


	
	
}