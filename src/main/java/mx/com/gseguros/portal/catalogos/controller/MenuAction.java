package mx.com.gseguros.portal.catalogos.controller;

import java.util.List;
import java.util.Map;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.gseguros.portal.general.service.MenuManager;

import org.apache.log4j.Logger;
import org.apache.struts2.json.JSONUtil;

public class MenuAction extends PrincipalCoreAction {

	private static final long serialVersionUID = -7264510862184393230L;
	
	private Logger logger = Logger.getLogger(MenuAction.class);
	
	private boolean success;
	
	private String errorMessage;
	
	private MenuManager menuManager;
	
	private Map<String,String> params;
	
	private List<Map<String, String>> loadList;
    private List<Map<String, String>> saveList;

    /*public String obtieneUsuarios() throws Exception {
    	
    	try{
    		usuarios = usuarioManager.obtieneUsuarios(params);
    		success=true;
    	} catch(Exception e) {
    		logger.error(e.getMessage(), e);
    		errorMessage = "Error al obtener usuarios. Intente m&aacute;s tarde";
    	}
    	
    	logger.debug("Resultado de usuarios para la busqueda: "+ usuarios);
    	return SUCCESS;
    }
    public String obtieneRolesUsuario() throws Exception {
       	try {
       		loadList = usuarioManager.obtieneRolesUsuario(params);
       	}catch( Exception e){
       		logger.error("Error en obtieneRolesUsuario",e);
       		success =  false;
       		return SUCCESS;
       	}
       	success = true;
       	return SUCCESS;
    }
    
    */
    public String obtieneOpcionesLiga() throws Exception {
       	try {
       		loadList = menuManager.obtieneOpcionesLiga(params);
       	}catch( Exception e){
       		logger.error("Error en obtieneOpcionesLiga",e);
       		success =  false;
       		return SUCCESS;
       	}
       	success = true;
       	return SUCCESS;
    }

    public String obtieneMenusPorRol() throws Exception {
    	try {
    		loadList = menuManager.obtieneMenusPorRol(params);
    	}catch( Exception e){
    		logger.error("Error en obtieneMenusPorRol",e);
    		success =  false;
    		return SUCCESS;
    	}
    	success = true;
    	return SUCCESS;
    }

    public String obtieneOpcionesMenu() throws Exception {
    	try {
    		loadList = menuManager.obtieneOpcionesMenu(params);
    	}catch( Exception e){
    		logger.error("Error en obtieneOpcionesMenu",e);
    		success =  false;
    		return SUCCESS;
    	}
    	success = true;
    	return SUCCESS;
    }

    public String obtieneOpcionesSubMenu() throws Exception {
    	try {
    		loadList = menuManager.obtieneOpcionesSubMenu(params);
    	}catch( Exception e){
    		logger.error("Error en obtieneOpcionesSubMenu",e);
    		success =  false;
    		return SUCCESS;
    	}
    	success = true;
    	return SUCCESS;
    }
    
    public String guardaOpcionLiga(){
       	
       	try {
       		menuManager.guardaOpcionLiga(params);
       	}catch( Exception e){
       		logger.error("Error en guardaOpcionLiga",e);
       		success =  false;
       		return SUCCESS;
       	}
       	success = true;
       	return SUCCESS;
    }

    public String guardaMenu(){
    	
    	try {
    		menuManager.guardaMenu(params);
    	}catch( Exception e){
    		logger.error("Error en guardaMenu",e);
    		errorMessage = e.getMessage();
    		success =  false;
    		return SUCCESS;
    	}
    	success = true;
    	return SUCCESS;
    }

    public String guardaOpcionMenu(){
    	
    	try {
    		menuManager.guardaOpcionMenu(params);
    	}catch( Exception e){
    		logger.error("Error en guardaOpcionMenu",e);
    		errorMessage = e.getMessage();
    		success =  false;
    		return SUCCESS;
    	}
    	success = true;
    	return SUCCESS;
    }

    public String eliminaOpcionLiga(){
    	
    	try {
    		menuManager.eliminaOpcionLiga(params);
    	}catch( Exception e){
    		errorMessage = e.getMessage();
    		logger.error("Error en eliminaOpcionLiga",e);
    		success =  false;
    		return SUCCESS;
    	}
    	success = true;
    	return SUCCESS;
    }

    public String eliminaMenu(){
    	
    	try {
    		menuManager.eliminaMenu(params);
    	}catch( Exception e){
    		logger.error("Error en eliminaMenu",e);
    		success =  false;
    		return SUCCESS;
    	}
    	success = true;
    	return SUCCESS;
    }

    public String eliminaOpcionMenu(){
    	
    	try {
    		menuManager.eliminaOpcionMenu(params);
    	}catch( Exception e){
    		logger.error("Error en eliminaOpcionMenu",e);
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
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Map<String,String> getParams() {
		return params;
	}

	public void setParams(Map<String,String> params) {
		this.params = params;
	}
	
	public String getParamsJson() {
		try {
			return JSONUtil.serialize(params);
		} catch (Exception e) {
			logger.error("Error al generar JSON de params",e);
			return null;
		}
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

	public void setMenuManager(MenuManager menuManager) {
		this.menuManager = menuManager;
	}
}