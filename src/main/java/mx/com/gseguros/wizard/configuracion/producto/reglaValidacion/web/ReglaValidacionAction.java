package mx.com.gseguros.wizard.configuracion.producto.reglaValidacion.web;

import java.util.List;

import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.reglaValidacion.model.ReglaValidacionVO;
import mx.com.gseguros.wizard.configuracion.producto.service.ReglaValidacionManager;
import mx.com.gseguros.wizard.configuracion.producto.web.Padre;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 
 * @author Edgar Perez
 * @version 2.0
 * @since 1.0
 * 
 * Clase que contiene los metodos de cargar, agregar y editar las reglas de validacion de un producto. 
 *
 */
public class ReglaValidacionAction extends Padre {
	
	private static final long serialVersionUID = 7346907378434705770L;
	private static final transient Log log = LogFactory.getLog(ReglaValidacionAction.class);
	
	/**
	 * Manager con implementacion de Endpoint para la consulta a BD.
	 */
	private ReglaValidacionManager reglaValidacionManager;
	/**
	 * Atributo de respuesta interpretado por strust como la correcta
	 * realizacion de. cada peticion.
	 */
	private boolean success;
	
	/**
	 * Atributo de respuesta interpretado por strust con la lista de reglas de validacion para el producto
	 * List<ReglaValidacionVO> con los valores de la consulta.
	 */
	private List<ReglaValidacionVO> listaReglasValidacion;
	
	/**
	 * Atributo de respuesta interpretado por strust con la lista de bloques para la regla de validacion
	 * List<LlaveValorVO> con los valores de la consulta.
	 */
	private List<LlaveValorVO> listaBloque;
	
	/**
	 * Atributo de respuesta interpretado por strust con la lista de validaciones para la regla de validacion
	 * List<LlaveValorVO> con los valores de la consulta.
	 */
	private List<LlaveValorVO> listaValidacion;
	
	/**
	 * Atributo de respuesta interpretado por strust con la lista de condiciones para la regla de validacion
	 * List<LlaveValorVO> con los valores de la consulta.
	 */
	private List<LlaveValorVO> listaCondicion;
		
	/**
	 * Atributo agregado por struts que contiene el tipo de lista que se cargara para la regla de validacion.
	 */
	private String lista;
	
	/**
	 * Atributo agregado por struts que contiene el codigo de ramo de regla de validacion.
	 */
	private String codigoRamo;
	
	/**
	 * Atributo agregado por struts que contiene el codigo de bloque para la regla de validacion.
	 */
	private String codigoBloque;
	
	/**
	 * Atributo agregado por struts que contiene la descripcion de bloque para la regla de validacion.
	 */
	private String descripcionBloque;
	
	/**
	 * Atributo agregado por struts que contiene el codigo de validacion para la regla de validacion.
	 */
	private String codigoValidacion;
	
	/**
	 * Atributo agregado por struts que contiene la descripcion de validacion para la regla de validacion.
	 */
	private String descripcionValidacion;
	
	/**
	 * Atributo agregado por struts que contiene el codigo de condicion para la regla de validacion.
	 */
	private String codigoCondicion;
	
	/**
	 * Atributo agregado por struts que contiene la descripcion de condicion para la regla de validacion.
	 */
	private String descripcionCondicion;
	
	/**
	 * Atributo agregado por struts que contiene la secuencia para la regla de validacion.
	 */
	private String secuencia;
	
	/**
	 * Atributo agregado por struts que contiene la secuencia para editar la regla de validacion.
	 */
	private String secuenciaHidden;
	
	/**
	 * Metodo <code>execute</code> con el que es llamado desde Struts para
	 * atender la peticion web.
	 * 
	 * @return INPUT
	 * @throws Exception
	 */
	public String execute() throws Exception {
		if(log.isDebugEnabled()){
			log.debug("Entro a Reglas de validacion");
		}
		return INPUT;
	}
	
	/**
     * Metodo <code>cargaListasReglasDeValidacion</code> con el que son cargadas las listas
     * de la pantalla reglas de validacion.
     * 
     * @return success
     * @throws Exception
     */
	public String cargaListasReglasDeValidacion() throws Exception{
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("Entro a catalogos de reglas de validacion");
		}
		
		if(session.containsKey("CODIGO_NIVEL0")){
			codigoRamo=(String) session.get("CODIGO_NIVEL0");
		}
		if(isDebugEnabled){
			log.debug("CODIGORAMO-->>"+codigoRamo);						
		}
		
		if(lista.equals("reglasDeValidacion") && codigoRamo != null){				
			//codigoRamo="100";//prueba
			listaReglasValidacion = reglaValidacionManager.obtieneReglasDeValidacion(codigoRamo);
				
				if(isDebugEnabled){
					for(ReglaValidacionVO regla: listaReglasValidacion){
						log.debug("DSBLOQUE"+regla.getDescripcionBloque());
					}
				}
		}
		if(lista.equals("bloques")){
			listaBloque = reglaValidacionManager.obtieneListaBloques();
		}
		
		if(lista.equals("validaciones")){
			listaValidacion = reglaValidacionManager.obtieneListaValidaciones();
		}
		if(lista.equals("condiciones")){			
			listaCondicion = reglaValidacionManager.obtieneListaCondiciones();			
		}
		success = true;
		return SUCCESS;		
	}
	
	/**
     * Metodo <code>asociarReglaValidacion</code> con el que es agregado o editado 
     * una regla de validacion.
     * 
     * @return success
     * @throws Exception
     */
	public String asociarReglaValidacion() throws Exception{
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("Entro a asociar regla de validacion");
		}
		
		if(session.containsKey("CODIGO_NIVEL0")){
			codigoRamo=(String) session.get("CODIGO_NIVEL0");
			if(isDebugEnabled){
				log.debug("CODIGORAMO-->"+codigoRamo);
				log.debug("SECUENCIA-->"+secuencia);
			}
		}		
		
		if(codigoRamo != null){
							
				ReglaValidacionVO reglaValidacion = new ReglaValidacionVO();
				
				reglaValidacion.setCodigoBloque(codigoBloque);
				reglaValidacion.setDescripcionBloque(descripcionBloque);
				reglaValidacion.setCodigoValidacion(codigoValidacion);
				reglaValidacion.setDescripcionValidacion(descripcionValidacion);
				reglaValidacion.setCodigoCondicion(codigoCondicion);
				reglaValidacion.setDescripcionCondicion(descripcionCondicion);
				reglaValidacion.setSecuencia(secuencia);
				if(secuencia== null){
					reglaValidacion.setSecuencia(secuenciaHidden);
				}
				reglaValidacionManager.asociarReglaDeValidacion(reglaValidacion,codigoRamo);
			
		}
		
		

		success = true;
		return SUCCESS;
	}

	/**
     * Metodo <code>eliminarReglaValidacion</code> con el que es eliminada la regla de validacion de un producto.
     * 
     * @return success
     * @throws Exception
     */
    public String eliminarReglaValidacion() throws Exception {
      boolean isDebugEnabled = log.isDebugEnabled();
      if(isDebugEnabled){
    	  log.debug("Entro a eliminar regla de validacion");    	  
      }
      
      if(session.containsKey("CODIGO_NIVEL0")){
    	  	codigoRamo=(String) session.get("CODIGO_NIVEL0");
    	  	if(isDebugEnabled){
    	  		log.debug("CODIGORAMO-->"+codigoRamo);
    	  	}
      } 
      if(codigoRamo!=null){
    		  reglaValidacionManager.eliminaReglaDeValidacion(codigoRamo,codigoBloque,secuencia);
      }
      success=true;
      return SUCCESS;
    }
    
    
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<ReglaValidacionVO> getListaReglasValidacion() {
		return listaReglasValidacion;
	}

	public void setListaReglasValidacion(
			List<ReglaValidacionVO> listaReglasValidacion) {
		this.listaReglasValidacion = listaReglasValidacion;
	}

	public void setReglaValidacionManager(
			ReglaValidacionManager reglaValidacionManager) {
		this.reglaValidacionManager = reglaValidacionManager;
	}

	public String getLista() {
		return lista;
	}

	public void setLista(String lista) {
		this.lista = lista;
	}

	public String getCodigoRamo() {
		return codigoRamo;
	}

	public void setCodigoRamo(String codigoRamo) {
		this.codigoRamo = codigoRamo;
	}

	public List<LlaveValorVO> getListaBloque() {
		return listaBloque;
	}

	public void setListaBloque(List<LlaveValorVO> listaBloque) {
		this.listaBloque = listaBloque;
	}

	public List<LlaveValorVO> getListaValidacion() {
		return listaValidacion;
	}

	public void setListaValidacion(List<LlaveValorVO> listaValidacion) {
		this.listaValidacion = listaValidacion;
	}

	public List<LlaveValorVO> getListaCondicion() {
		return listaCondicion;
	}

	public void setListaCondicion(List<LlaveValorVO> listaCondicion) {
		this.listaCondicion = listaCondicion;
	}

	public String getCodigoBloque() {
		return codigoBloque;
	}

	public void setCodigoBloque(String codigoBloque) {
		this.codigoBloque = codigoBloque;
	}

	public String getDescripcionBloque() {
		return descripcionBloque;
	}

	public void setDescripcionBloque(String descripcionBloque) {
		this.descripcionBloque = descripcionBloque;
	}

	public String getCodigoValidacion() {
		return codigoValidacion;
	}

	public void setCodigoValidacion(String codigoValidacion) {
		this.codigoValidacion = codigoValidacion;
	}

	public String getDescripcionValidacion() {
		return descripcionValidacion;
	}

	public void setDescripcionValidacion(String descripcionValidacion) {
		this.descripcionValidacion = descripcionValidacion;
	}

	public String getCodigoCondicion() {
		return codigoCondicion;
	}

	public void setCodigoCondicion(String codigoCondicion) {
		this.codigoCondicion = codigoCondicion;
	}

	public String getDescripcionCondicion() {
		return descripcionCondicion;
	}

	public void setDescripcionCondicion(String descripcionCondicion) {
		this.descripcionCondicion = descripcionCondicion;
	}

	public String getSecuencia() {
		return secuencia;
	}

	public void setSecuencia(String secuencia) {
		this.secuencia = secuencia;
	}

	public String getSecuenciaHidden() {
		return secuenciaHidden;
	}

	public void setSecuenciaHidden(String secuenciaHidden) {
		this.secuenciaHidden = secuenciaHidden;
	}
}
