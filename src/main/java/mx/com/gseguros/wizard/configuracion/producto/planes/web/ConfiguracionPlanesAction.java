package mx.com.gseguros.wizard.configuracion.producto.planes.web;

import java.util.List;

import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.RamaVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.planes.service.PlanesConfiguracionManager;
import mx.com.gseguros.wizard.configuracion.producto.web.Padre;
import mx.com.gseguros.wizard.model.MensajesVO;
import mx.com.gseguros.wizard.model.PlanesMPlanProVO;
import mx.com.gseguros.wizard.service.CatalogService;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author sergio.ramirez
 *
 */
public class ConfiguracionPlanesAction extends Padre{

	/**
	 *@serial version 
	 */
	private static final long serialVersionUID = -2897051659302102895L;
	private static final transient Log log = LogFactory.getLog(ConfiguracionPlanesAction.class);
	private boolean success;
	private PlanesConfiguracionManager   planesConfiguracionManager; 
	
	/**
	 * Manager con implementacion de Endpoints para catalogos
	 */
	private CatalogService catalogManager;
	
	private List<LlaveValorVO> planes;
	private List<LlaveValorVO> coberturas;
	private List<RamaVO> ramaPlanes;
	
	private String cdPlan;
	private String cdRamo;
	private String cdTipSit;
	private String cdGarant;
	
	private String dsPlan;
	private String codigoPlan;
	private String codigoCondicion;
	private String key;
	private String value;
	
	/**
	 * Atributo utilizado para almacenar los mensajes de respuesta de una peticion a BD
	 */
	private String mensajeRespuesta;
	
	
	public String execute() throws Exception{
		log.debug("###ENTRANDO AL METODO PLANES#####");
		return INPUT;
	}
	
	
	public String cargaPlanesConfiguracion() throws Exception{
		
		log.debug("entrando a cargar la lista de planes");
		log.debug("CLAVE PRODUCTO"+cdRamo);
		if(session.containsKey("CODIGO_NIVEL0") && session.containsKey("CODIGO_NIVEL2")){
			cdRamo=(String) session.get("CODIGO_NIVEL0");
			cdTipSit=(String) session.get("CODIGO_NIVEL2");
			log.debug("CODIGORAMO--"+cdRamo);
			log.debug("CDTIPSIT--"+cdTipSit);
			planes= planesConfiguracionManager.getListaPlanes(cdRamo, cdTipSit);
			ramaPlanes = planesConfiguracionManager.getRamaPlanes(cdRamo, cdTipSit); 
			log.debug("ramaPlanes"+ramaPlanes);
			log.debug("PLANES:"+planes);
			success=true;
		}else{
			success=false;
		}
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String cargaCoberturasPlanes() throws Exception{
		log.debug("entro al metodo que carga las coberturas");
		log.debug("cdTipSit"+cdTipSit);
		log.debug("cdPlan"+cdPlan);
		log.debug("codigoPlan"+codigoPlan);
		if(session.containsKey("CODIGO_NIVEL0") && session.containsKey("CODIGO_NIVEL2") && StringUtils.isNotBlank(codigoPlan)){
			cdRamo=(String) session.get("CODIGO_NIVEL0");
			cdTipSit=(String) session.get("CODIGO_NIVEL2");
			cdPlan = codigoPlan;
			session.put("CLAVE_PLAN_CONFIGURACION", codigoPlan);
			coberturas = planesConfiguracionManager.getCoberturasPlanes(cdRamo, cdPlan, cdTipSit);
			success=true;
		}else{
			success=false;			
		}
		
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String guardaPlanes() throws Exception{
		if(log.isDebugEnabled()){
			log.debug("ENTRANDO AL METODO QUE GUARDA UN PLAN");
			log.debug(dsPlan);
			log.debug(cdPlan);
		}
		if(StringUtils.isNotBlank(dsPlan)&& session.containsKey("CODIGO_NIVEL0") && StringUtils.isNotBlank(cdPlan)){
			cdRamo=(String) session.get("CODIGO_NIVEL0");
			cdTipSit=(String) session.get("CODIGO_NIVEL2");
			planesConfiguracionManager.guardaPlanesConfiguracion(cdRamo,cdTipSit, cdPlan, dsPlan);
			success=true;
		}else if(StringUtils.isNotBlank(value)&& session.containsKey("CODIGO_NIVEL0") && StringUtils.isNotBlank(key)){
			cdRamo=(String) session.get("CODIGO_NIVEL0");
			cdTipSit=(String) session.get("CODIGO_NIVEL2");
			if(log.isDebugEnabled()){
				log.debug("key:"+key);
				log.debug("value"+value);
				key=cdPlan;
				value=dsPlan;
			}
			planesConfiguracionManager.guardaPlanesConfiguracion(cdRamo,cdTipSit, cdPlan, dsPlan);
		}
		if(success){
			List<RamaVO> temporalTree=(List<RamaVO>) session.get("ARBOL_PRODUCTOS");
			session.put("ARBOL_PRODUCTOS_RECARGAR", temporalTree);
			session.remove("ARBOL_PRODUCTOS");
		}
		
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String editaPlan() throws Exception{
		if(log.isDebugEnabled()){
			log.debug("ENTRANDO AL METODO QUE EDITA UN PLAN");
			log.debug(dsPlan);
			log.debug(cdPlan);
		}
		if(StringUtils.isNotBlank(dsPlan)&& session.containsKey("CODIGO_NIVEL0") && StringUtils.isNotBlank(cdPlan)){
			cdRamo=(String) session.get("CODIGO_NIVEL0");
			cdTipSit=(String) session.get("CODIGO_NIVEL2");
			planesConfiguracionManager.editaPlan(cdRamo, cdPlan, dsPlan);
			success=true;
		}else if(StringUtils.isNotBlank(value)&& session.containsKey("CODIGO_NIVEL0") && StringUtils.isNotBlank(key)){
			cdRamo=(String) session.get("CODIGO_NIVEL0");
			cdTipSit=(String) session.get("CODIGO_NIVEL2");
			if(log.isDebugEnabled()){
				log.debug("key:"+key);
				log.debug("value"+value);
				key=cdPlan;
				value=dsPlan;
			}
			planesConfiguracionManager.editaPlan(cdRamo, cdPlan, dsPlan);
		}
		if(success){
			List<RamaVO> temporalTree=(List<RamaVO>) session.get("ARBOL_PRODUCTOS");
			session.put("ARBOL_PRODUCTOS_RECARGAR", temporalTree);
			session.remove("ARBOL_PRODUCTOS");
		}
		
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String guardaCoberturasPlanes() throws Exception{
		if(log.isDebugEnabled()){
			log.debug("ENTRANDO AL METODO QUE GUARDA  COBERTURAS");
			log.debug("codigoCondicion"+codigoCondicion);
			
		}
		if(session.containsKey("CLAVE_PLAN_CONFIGURACION")&& session.containsKey("CODIGO_NIVEL0") && 
			session.containsKey("CODIGO_NIVEL2") && StringUtils.isNotBlank(codigoCondicion)){
			
			cdRamo=(String) session.get("CODIGO_NIVEL0");
			cdTipSit=(String) session.get("CODIGO_NIVEL2");
			String cdPlanTmp=(String) session.get("CLAVE_PLAN_CONFIGURACION");
			String obligatorio="0";
			if(log.isDebugEnabled()){
				log.debug("CDRAMO:"+cdRamo);
				log.debug("CDTIPSIT:"+cdTipSit);
				log.debug("CDPLAN:"+cdPlanTmp);
			}
			MensajesVO mensajeVO = null;
			mensajeVO = planesConfiguracionManager.guardaCoberturaPlanes(cdRamo, cdPlanTmp, cdTipSit, codigoCondicion,obligatorio);
			log.debug("mensajeVO text: "+mensajeVO.getText()+", title: "+mensajeVO.getTitle());
			
//			//Obtener mensaje de respuesta que se va a mostrar a partir de un codigo:
//			Map<String, String> params = new HashMap<String, String>();
//			params.put("msg", mensajeVO.getTitle());//el codigo viene en pv_title_o 
//			mensajeVO = catalogManager.getMensajes(params, "OBTIENE_MENSAJES");
//			if(log.isDebugEnabled()){
//				log.debug("title-->>" + mensajeVO.getTitle());
//				log.debug("MsgText-->>" + mensajeVO.getMsgText());
//			}
			if("2".equals(mensajeVO.getTitle())||mensajeVO.getTitle()==null||mensajeVO.getTitle().equals("")){//Cuando pv_title_o es 2, fue success
				success=true;
			}
			mensajeRespuesta = mensajeVO.getMsgText();
			
		}else{
			success=false;
		}
		if(success){
			List<RamaVO> temporalTree=(List<RamaVO>) session.get("ARBOL_PRODUCTOS");
			session.put("ARBOL_PRODUCTOS_RECARGAR", temporalTree);
			session.remove("ARBOL_PRODUCTOS");
		}
		return SUCCESS;
	}
	
	/**
	 * Elimina cobertura asociada a un plan
	 * @return
	 * @throws Exception
	 */
	public String eliminaCoberturasPlanes() throws Exception{
		
		if(session.containsKey("CLAVE_PLAN_CONFIGURACION")&& session.containsKey("CODIGO_NIVEL0") && session.containsKey("CODIGO_NIVEL2")){
			cdRamo = (String) session.get("CODIGO_NIVEL0");
			cdTipSit = (String) session.get("CODIGO_NIVEL2");
			cdPlan = (String) session.get("CLAVE_PLAN_CONFIGURACION");
		}
		
		if(log.isDebugEnabled()){
			log.debug("cdRamo==" + cdRamo);
			log.debug("cdTipSit==" + cdTipSit);
			log.debug("cdPlan==" + cdPlan);
			log.debug("cdGarant==" + cdGarant);
		}
		
		PlanesMPlanProVO datosPlan = new PlanesMPlanProVO();
		datosPlan.setCdRamo(cdRamo);
		datosPlan.setCdTipSit(cdTipSit);
		datosPlan.setCdPlan(cdPlan);
		datosPlan.setCdGarant(cdGarant);
		mensajeRespuesta = planesConfiguracionManager.borrarCoberturasPlan(datosPlan);
		
		// Se recarga el arbol de productos en sesion:
		List<RamaVO> temporalTree=(List<RamaVO>) session.get("ARBOL_PRODUCTOS");
		session.put("ARBOL_PRODUCTOS_RECARGAR", temporalTree);
		session.remove("ARBOL_PRODUCTOS");
		
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String eliminaPlan() throws Exception{
		if(log.isDebugEnabled()){
			log.debug("ENTRANDO AL METODO QUE ELIMINA UN PLAN");
			log.debug("cdPlan=" + cdPlan);
		}
		
		if(StringUtils.isNotBlank(cdPlan) && session.containsKey("CODIGO_NIVEL0") && session.containsKey("CODIGO_NIVEL2")){
			cdRamo=(String) session.get("CODIGO_NIVEL0");
			cdTipSit=(String) session.get("CODIGO_NIVEL2");
			planesConfiguracionManager.eliminaPlan(cdRamo, cdTipSit, cdPlan);
			
			List<RamaVO> temporalTree=(List<RamaVO>) session.get("ARBOL_PRODUCTOS");
			session.put("ARBOL_PRODUCTOS_RECARGAR", temporalTree);
			session.remove("ARBOL_PRODUCTOS");
			success=true;
		}
		
		return SUCCESS;
	}
	
	
	public String modificaCoberturaPlan() throws  Exception{
			 String cdGarant = key;
			 String dsGarant = value;
			 
			log.debug("ConfiguracionPlanesAction.modificaCoberturaPlan"); 
			log.debug("cdGarant:"+ cdGarant );
			log.debug("dsGarant:"+ dsGarant );
			planesConfiguracionManager.modificaCoberturaPlan( cdGarant, dsGarant );
			success=true;
			return SUCCESS;
	}

	
	/**
	 * 
	 * @return success
	 */
	public boolean isSuccess() {
		return success;
	}
	/**
	 * 
	 * @param success
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * 
	 * @return planes
	 */
	public List<LlaveValorVO> getPlanes() {
		return planes;
	}

	/**
	 * 
	 * @param planes
	 */
	public void setPlanes(List<LlaveValorVO> planes) {
		this.planes = planes;
	}

	/**
	 * 
	 * @param planesConfiguracionManager
	 */
	public void setPlanesConfiguracionManager(
			PlanesConfiguracionManager planesConfiguracionManager) {
		this.planesConfiguracionManager = planesConfiguracionManager;
	}

	public void setCatalogManager(CatalogService catalogManager) {
		this.catalogManager = catalogManager;
	}

	/**
	 * 
	 * @return cdRamo
	 */
	public String getCdRamo() {
		return cdRamo;
	}

	/**
	 * 
	 * @param cdRamo
	 */
	public void setCdRamo(String cdRamo) {
		this.cdRamo = cdRamo;
	}

	/**
	 * 
	 * @return coberturas
	 */
	public List<LlaveValorVO> getCoberturas() {
		return coberturas;
	}

	/**
	 * 
	 * @param coberturas
	 */
	public void setCoberturas(List<LlaveValorVO> coberturas) {
		this.coberturas = coberturas;
	}

	/**
	 * 
	 * @return cdPlan
	 */
	public String getCdPlan() {
		return cdPlan;
	}

	/**
	 * 
	 * @param cdPlan
	 */
	public void setCdPlan(String cdPlan) {
		this.cdPlan = cdPlan;
	}

	/**
	 * 
	 * @return cdTipSit
	 */
	public String getCdTipSit() {
		return cdTipSit;
	}

	/**
	 * 
	 * @param cdTipSit
	 */
	public void setCdTipSit(String cdTipSit) {
		this.cdTipSit = cdTipSit;
	}
	
	public String getCdGarant() {
		return cdGarant;
	}


	public void setCdGarant(String cdGarant) {
		this.cdGarant = cdGarant;
	}

	/**
	 * 
	 * @return dsPlan
	 */
	public String getDsPlan() {
		return dsPlan;
	}

	/**
	 * 
	 * @param dsPlan
	 */
	public void setDsPlan(String dsPlan) {
		this.dsPlan = dsPlan;
	}

	/**
	 * 
	 * @return codigoPlan
	 */
	public String getCodigoPlan() {
		return codigoPlan;
	}

	/**
	 * 
	 * @param codigoPlan
	 */
	public void setCodigoPlan(String codigoPlan) {
		this.codigoPlan = codigoPlan;
	}

	/**
	 * 
	 * @return codigoCondicion
	 */
	public String getCodigoCondicion() {
		return codigoCondicion;
	}

	/**
	 * 
	 * @param codigoCondicion
	 */
	public void setCodigoCondicion(String codigoCondicion) {
		this.codigoCondicion = codigoCondicion;
	}

	/**
	 * 
	 * @return
	 */
	public String getKey() {
		return key;
	}

	/**
	 * 
	 * @param key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * 
	 * @return
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	
	public String getMensajeRespuesta() {
		return mensajeRespuesta;
	}


	public void setMensajeRespuesta(String mensajeRespuesta) {
		this.mensajeRespuesta = mensajeRespuesta;
	}


	/**
	 * 
	 * @return ramaPlanes
	 */
	public List<RamaVO> getRamaPlanes() {
		return ramaPlanes;
	}

	/**
	 * 
	 * @param ramaPlanes
	 */
	public void setRamaPlanes(List<RamaVO> ramaPlanes) {
		this.ramaPlanes = ramaPlanes;
	}
	
	
	
}
