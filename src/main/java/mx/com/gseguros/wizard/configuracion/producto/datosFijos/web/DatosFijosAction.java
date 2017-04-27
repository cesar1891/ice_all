package mx.com.gseguros.wizard.configuracion.producto.datosFijos.web;

import java.util.ArrayList;
import java.util.List;

import mx.com.gseguros.wizard.configuracion.producto.datosFijos.model.DatoFijoVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.service.DatosFijosManager;
import mx.com.gseguros.wizard.configuracion.producto.web.ExpresionesPadre;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;




public class DatosFijosAction extends ExpresionesPadre{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6725366989933154399L;
	private static final transient Log log = LogFactory
	.getLog(DatosFijosAction.class);
	private boolean success;
	private DatosFijosManager datosFijosManager;
	private List<DatoFijoVO> listaDatosFijos;
	private List<LlaveValorVO> catalogoBloque;
	private List<LlaveValorVO> catalogoCampo;
	
	private String codigoRamo;
	private String descripcionBloque;
	private String claveBloque;
	private String descripcionCampo;
	private String claveCampo;
	private String codigoExpresion;
	

	public String execute() throws Exception {
		return INPUT;
	}
	public String insertarDatoFijo() throws Exception{
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("Entrando a insertarDatoFijo + codigoExpresion="+ codigoExpresion);
		}
		log.debug("BOLQUE: " + session.get("CLAVE_BLOQUE_DATO_FIJO"));
		success=true;
		if(session.containsKey("CODIGO_NIVEL0")){
			codigoRamo = (String) session.get("CODIGO_NIVEL0");
		}
		if(isDebugEnabled){
			log.debug("+++++++codigoRamo="+codigoRamo);
			log.debug("+++++++codigoExpresion="+ codigoExpresion+" contains()"+session.containsKey("EXPRESION"));
		}
		if(session.containsKey("EXPRESION")){
			int codigoExpresionInt = (Integer)session.get("EXPRESION");
			if(success){						
				codigoExpresion=Integer.toString(codigoExpresionInt);
				DatoFijoVO dfvo= new DatoFijoVO();
				dfvo.setCodigoBloque(claveBloque);
				dfvo.setCodigoCampo(claveCampo);
				dfvo.setCodigoExpresion(codigoExpresion);
				dfvo.setCodigoFuncio("SIGS2035");
				dfvo.setCodigoRamo(codigoRamo);
				dfvo.setDescripcionBloque(descripcionBloque);
				dfvo.setDescripcionCampo(descripcionCampo);	
				datosFijosManager.insertarDatoFijo(dfvo);
				
			}
		}
		
		
		success=true;
		return SUCCESS;
	}
	public String listaDatosFijosJson() throws Exception{
		boolean isDebugEnabled = log.isDebugEnabled();
		
		if(session.containsKey("CODIGO_NIVEL0")){
			codigoRamo=(String)session.get("CODIGO_NIVEL0");
		}else{
			codigoRamo="1";
		}
		if(isDebugEnabled){
			log.debug("codigoRamo="+codigoRamo);
		}
//		if(session.containsKey("LISTA_DATOS_FIJOS"))
//			listaDatosFijos = (List<DatoFijoVO>) session.get("LISTA_DATOS_FIJOS");
//		else{
			listaDatosFijos = datosFijosManager.listaDatosFijos(codigoRamo);			
			
//		}
		if(listaDatosFijos==null)
			listaDatosFijos= new ArrayList<DatoFijoVO>();
		
		//session.put("LISTA_DATOS_FIJOS", listaDatosFijos);
		success=true;
		return SUCCESS;
	}
	
	public String catalogoBloque() throws Exception{
		if(session.containsKey("CATALOGO_BLOQUE_DATOS_FIJOS"))
			catalogoBloque = (List<LlaveValorVO>) session.get("CATALOGO_BLOQUE_DATOS_FIJOS");
		else{
			catalogoBloque = datosFijosManager.catalogoBloque();
			/*
			catalogoBloque = new ArrayList<LlaveValorVO>();
			LlaveValorVO bloque = null;
			for(int i=0;i<10;i++){
				bloque = new LlaveValorVO();
				bloque.setKey(Integer.toString(i));
				bloque.setValue("value"+i);
				catalogoBloque.add(bloque);
			}*/
		}
		if(catalogoBloque==null)
			catalogoBloque = new ArrayList<LlaveValorVO>();
		success = true;
		return SUCCESS;
	}
	
	public String subeClaveBloque(){
		boolean isDebugEnabled = log.isDebugEnabled();
		
		if(claveBloque!=null && StringUtils.isNotBlank(claveBloque) && !claveBloque.equals("undefined")){
			session.put("CLAVE_BLOQUE_DATO_FIJO", claveBloque);
			if(isDebugEnabled){
				log.debug("SUBI CLAVE_BLOQUE_DATO_FIJO a session +catalogoBloque"+claveBloque);
			}
		}else{
			if(isDebugEnabled){
				log.debug("No subi CLAVE_BLOQUE_DATO_FIJO a session+catalogoBloque"+claveBloque);
			}
			if(session.containsKey("CLAVE_BLOQUE_DATO_FIJO"))
				session.remove("CLAVE_BLOQUE_DATO_FIJO");
		}	
		success = true;
		return SUCCESS;
	}
	
	public String catalogoCampo() throws Exception{
		boolean isDebugEnabled = log.isDebugEnabled();
		if(session.containsKey("CLAVE_BLOQUE_DATO_FIJO")){
			if(isDebugEnabled){
				log.debug("existe CLAVE_BLOQUE_DATO_FIJO en session");
			}
			String claveBloqueDatoFijo = (String) session.get("CLAVE_BLOQUE_DATO_FIJO");
			catalogoCampo= datosFijosManager.catalogoCampo(claveBloqueDatoFijo);
			/*
			catalogoCampo = new ArrayList<LlaveValorVO>();
			LlaveValorVO campo = null;
			for(int i=0;i<10;i++){
				campo = new LlaveValorVO();
				campo.setKey(Integer.toString(i));
				campo.setValue(claveBloqueDatoFijo+i);
				catalogoCampo.add(campo);
			}*/
		}
		if(catalogoCampo==null){
			catalogoCampo = new ArrayList<LlaveValorVO>();
//			LlaveValorVO campoDefault = new LlaveValorVO();
//			campoDefault.setValue("noo!!!");
//			catalogoCampo.add(campoDefault);
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}
	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
	/**
	 * @return the datosFijosManager
	 */
	public DatosFijosManager getDatosFijosManager() {
		return datosFijosManager;
	}
	/**
	 * @param datosFijosManager the datosFijosManager to set
	 */
	public void setDatosFijosManager(DatosFijosManager datosFijosManager) {
		this.datosFijosManager = datosFijosManager;
	}
	
	/**
	 * @return the catalogoBloque
	 */
	public List<LlaveValorVO> getCatalogoBloque() {
		return catalogoBloque;
	}
	/**
	 * @param catalogoBloque the catalogoBloque to set
	 */
	public void setCatalogoBloque(List<LlaveValorVO> catalogoBloque) {
		this.catalogoBloque = catalogoBloque;
	}
	/**
	 * @return the catalogoCampo
	 */
	public List<LlaveValorVO> getCatalogoCampo() {
		return catalogoCampo;
	}
	/**
	 * @param catalogoCampo the catalogoCampo to set
	 */
	public void setCatalogoCampo(List<LlaveValorVO> catalogoCampo) {
		this.catalogoCampo = catalogoCampo;
	}
	/**
	 * @return the descripcionBloque
	 */
	public String getDescripcionBloque() {
		return descripcionBloque;
	}
	/**
	 * @param descripcionBloque the descripcionBloque to set
	 */
	public void setDescripcionBloque(String descripcionBloque) {
		this.descripcionBloque = descripcionBloque;
	}
	/**
	 * @return the claveBloque
	 */
	public String getClaveBloque() {
		return claveBloque;
	}
	/**
	 * @param claveBloque the claveBloque to set
	 */
	public void setClaveBloque(String claveBloque) {
		this.claveBloque = claveBloque;
	}
	/**
	 * @return the descripcionCampo
	 */
	public String getDescripcionCampo() {
		return descripcionCampo;
	}
	/**
	 * @param descripcionCampo the descripcionCampo to set
	 */
	public void setDescripcionCampo(String descripcionCampo) {
		this.descripcionCampo = descripcionCampo;
	}
	/**
	 * @return the claveCampo
	 */
	public String getClaveCampo() {
		return claveCampo;
	}
	/**
	 * @param claveCampo the claveCampo to set
	 */
	public void setClaveCampo(String claveCampo) {
		this.claveCampo = claveCampo;
	}
	/**
	 * @return the codigoExpresion
	 */
	public String getCodigoExpresion() {
		return codigoExpresion;
	}
	/**
	 * @param codigoExpresion the codigoExpresion to set
	 */
	public void setCodigoExpresion(String codigoExpresion) {
		this.codigoExpresion = codigoExpresion;
	}

	/**
	 * @return the listaDatosFijos
	 */
	public List<DatoFijoVO> getListaDatosFijos() {
		return listaDatosFijos;
	}

	/**
	 * @param listaDatosFijos the listaDatosFijos to set
	 */
	public void setListaDatosFijos(List<DatoFijoVO> listaDatosFijos) {
		this.listaDatosFijos = listaDatosFijos;
	}
	/**
	 * @return the codigoRamo
	 */
	public String getCodigoRamo() {
		return codigoRamo;
	}
	/**
	 * @param codigoRamo the codigoRamo to set
	 */
	public void setCodigoRamo(String codigoRamo) {
		this.codigoRamo = codigoRamo;
	}


}
