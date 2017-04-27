package mx.com.gseguros.wizard.controller;

import java.util.List;
import java.util.Map;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.wizard.service.TablasApoyoManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller("tablasApoyoAction")
@Scope("prototype")
public class TablasApoyoAction extends PrincipalCoreAction {

	private static final long serialVersionUID = 3547571651089343357L;

	@Autowired
	private TablasApoyoManager tablasApoyoManager;
	
	
	private boolean success;
	
	
	/**
     * Parametros enviados a los catalogos
     */
    private Map<String, String> params;
	
	/**
     * Lista de elementos a cargar en grid por tipo Map
     */
    private List<Map<String, String>> loadList;
    
    /**
     * Lista para guardar varios elementos
     */
    private List<Map<String, String>> saveList;

    /**
     * Lista para guardar varios elementos
     */
    private List<Map<String, String>> deleteList;
    
    /**
     * Lista para guardar varios elementos
     */
    private List<Map<String, String>> updateList;
    
    /**
     * Mensaje de respuesta del servicio
     */
    private String msgRespuesta;

    
    public String execute() throws Exception {
    	return SUCCESS;
    }
	
	public String obtieneValoresTablaApoyo5claves()throws Exception{
    	
    	try{
    		
    		logger.debug("Parametros para obtener valores de Tabla de Apoyo 01 : " + params);

    		Map<String, Object> resultado = tablasApoyoManager.obtieneValoresTablaApoyo5claves(params);
    		loadList = (List<Map<String, String>>) resultado.get("PV_REGISTRO_O"); 
    		
   			msgRespuesta = (String)resultado.get("PV_MSG_TEXT_O");
   			
//    		logger.debug("Tabla de Apoyo Lista Resultante: " + loadList);
    	}catch(ApplicationException ae){
    		logger.error("Error al obtieneValoresTablaApoyo5claves",ae);
//    		logger.debug("Tabla de Apoyo Lista Resultante: " + loadList);
    		msgRespuesta = ae.getMessage();
    		return SUCCESS;
    	}catch(Exception ex){
    		logger.error("Error al obtieneValoresTablaApoyo5claves",ex);
    		msgRespuesta = "Error al ejecutar la consulta. Consulte a Soporte.";
    		return SUCCESS;
    	}
    	
    	success = true;
    	return SUCCESS;
    }

	public String obtieneValoresTablaApoyo1clave()throws Exception{
		
		try{
			
			logger.debug("Parametros para obtener valores de Tabla de Apoyo:02 " + params);
			
			Map<String, Object> resultado = tablasApoyoManager.obtieneValoresTablaApoyo1clave(params);
    		loadList = (List<Map<String, String>>) resultado.get("PV_REGISTRO_O"); 
   			msgRespuesta = (String)resultado.get("PV_MSG_TEXT_O");
   			
    		logger.debug("Tabla de Apoyo Lista Resultante: " + loadList);
		}catch(ApplicationException ae){
			logger.error("Error al obtieneValoresTablaApoyo1clave",ae);
    		logger.debug("Tabla de Apoyo Lista Resultante: " + loadList);
			msgRespuesta = ae.getMessage();
			success = false;
			return SUCCESS;
		}catch(Exception ex){
			logger.error("Error al obtieneValoresTablaApoyo5claves",ex);
			msgRespuesta = "Error al ejecutar la consulta. Consulte a Soporte.";
			success = false;
			return SUCCESS;
		}
		
		success = true;
		return SUCCESS;
	}
	
	public String guardaValoresTablaApoyo()throws Exception{
    	
    	try{
    		logger.debug("Guardando Valores de la Tabla de Apoyo...");
    		logger.debug("Parametros: " + params);
    		logger.debug("DeleteList: " + deleteList);
    		logger.debug("SaveList:   " + saveList);
    		logger.debug("UdateList:  " + updateList);
    		
    		tablasApoyoManager.guardaValoresTablaApoyo(params,deleteList,saveList,updateList, Constantes.SI.equalsIgnoreCase(params.get("ES_UNA_CLAVE")));
    		
    		logger.debug("Valores de Tabla Guardados... ");
    		
    	}catch(Exception ex){
    		logger.error("Error al guardaValoresTablaApoyo",ex);
    		msgRespuesta = ex.getMessage();
    		success = false;
    		return SUCCESS;
    	}
    	
    	success = true;
    	return SUCCESS;
    }


	public boolean isSuccess() {
		return success;
	}


	public void setSuccess(boolean success) {
		this.success = success;
	}


	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
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

	public String getMsgRespuesta() {
		return msgRespuesta;
	}

	public void setMsgRespuesta(String msgRespuesta) {
		this.msgRespuesta = msgRespuesta;
	}

	public List<Map<String, String>> getDeleteList() {
		return deleteList;
	}

	public void setDeleteList(List<Map<String, String>> deleteList) {
		this.deleteList = deleteList;
	}

	public List<Map<String, String>> getUpdateList() {
		return updateList;
	}

	public void setUpdateList(List<Map<String, String>> updateList) {
		this.updateList = updateList;
	}
}