package mx.com.gseguros.portal.catalogos.controller;

import java.util.HashMap;
import java.util.List;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.gseguros.portal.catalogos.service.ClausuladoManager;
import mx.com.gseguros.portal.general.model.BaseVO;

import org.apache.log4j.Logger;

public class ClausuladoAction extends PrincipalCoreAction {

	private static final long serialVersionUID = -7264510862184393230L;
	
	private Logger logger = Logger.getLogger(ClausuladoAction.class);
	
	private boolean success;
	
	private HashMap<String,String> params;
	
	private String msgResult;
	
	private List<BaseVO> listaGenerica;
	
	private ClausuladoManager clausuladoManager;

    public String cargaClausulas(){
    	logger.debug(" **** Entrando a cargaClausulas ****");
    	try {
    		listaGenerica = clausuladoManager.consultaClausulas(null,null);
    		logger.debug("Resultado de cargaClausulas:" + listaGenerica);
    	}catch( Exception e){
    		logger.error("Error al obtener cargaClausulas ",e);
    		return SUCCESS;
    	}
    	success = true;
    	return SUCCESS;
    }
    
    
    public String consultaClausulas(){
    	logger.debug(" **** Entrando a consultaClausulas ****");
    	try {
    		listaGenerica = clausuladoManager.consultaClausulas(params.get("cdclausu"), params.get("dsclausu"));
    		logger.debug("Resultado de consultaClausulas=" + listaGenerica);
    	}catch( Exception e){
    		logger.error("Error al obtener consultaClausulas ",e);
    		return SUCCESS;
    	}
    	success = true;
    	return SUCCESS;
    }
    
    
    public String insertaClausula(){
    	logger.debug(" **** Entrando a insertaClausula ****");
    	try {
    		clausuladoManager.insertaClausula(params.get("descripcion"), params.get("contenido"));
    	}catch( Exception e){
    		logger.error("Error al insertaClausula ",e);
    		return SUCCESS;
    	}
    	success = true;
    	return SUCCESS;
    }
    
    
    public String consultaClausulaDetalle(){
    	logger.debug(" **** Entrando a consultaClausulaDetalle ****");
    	try {
    		msgResult = clausuladoManager.consultaClausulaDetalle(params.get("cdclausu"));
    	}catch( Exception e){
    		logger.error("Error al consultaClausulaDetalle ",e);
    		return SUCCESS;
    	}
    	success = true;
    	return SUCCESS;
    }

    
    public String actualizaClausula(){
    	logger.debug(" **** Entrando a actualizaClausula ****");
    	try {
    		clausuladoManager.actualizaClausula(params.get("cdclausu"), params.get("descripcion"), params.get("contenido"));
    	}catch( Exception e){
    		logger.error("Error al actualizaClausula ",e);
    		return SUCCESS;
    	}
    	success = true;
    	return SUCCESS;
    }

    
    
    //Getters and setters
    
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public HashMap<String, String> getParams() {
		return params;
	}

	public void setParams(HashMap<String, String> params) {
		this.params = params;
	}
	
	public String getMsgResult() {
		return msgResult;
	}

	public void setMsgResult(String msgResult) {
		this.msgResult = msgResult;
	}

	public List<BaseVO> getListaGenerica() {
		return listaGenerica;
	}

	public void setListaGenerica(List<BaseVO> listaGenerica) {
		this.listaGenerica = listaGenerica;
	}


	public void setClausuladoManager(ClausuladoManager clausuladoManager) {
		this.clausuladoManager = clausuladoManager;
	}
	
}