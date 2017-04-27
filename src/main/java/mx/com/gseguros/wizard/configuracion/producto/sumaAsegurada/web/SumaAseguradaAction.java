package mx.com.gseguros.wizard.configuracion.producto.sumaAsegurada.web;

import java.util.List;

import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.service.SumaAseguradaManager;
import mx.com.gseguros.wizard.configuracion.producto.service.SumaAseguradaManagerJdbcTemplate;
import mx.com.gseguros.wizard.configuracion.producto.sumaAsegurada.model.SumaAseguradaIncisoVO;
import mx.com.gseguros.wizard.configuracion.producto.sumaAsegurada.model.SumaAseguradaVO;
import mx.com.gseguros.wizard.configuracion.producto.web.Padre;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 
 * @author Edgar Perez
 * @version 2.0
 * @since 1.0
 * 
 * Clase que contiene los metodos de cargar, agregar y editar la suma asegurada 
 * a nivel de un producto y de inciso. 
 *
 */
public class SumaAseguradaAction extends Padre {
	
	private static final long serialVersionUID = -6011901716984441288L;
	private static final transient Log log = LogFactory.getLog(SumaAseguradaAction.class);
	
	/**
	 * Manager con implementacion de Endpoint para la consulta a BD.
	 */
	private SumaAseguradaManager sumaAseguradaManager;
	
	/**
	 * Manager con implementacion JDBCTemplate para la consulta a BD.
	 */
	private SumaAseguradaManagerJdbcTemplate sumaAseguradaManagerJdbcTemplateImpl;
	
	/**
	 * Atributo de respuesta interpretado por strust como la correcta
	 * realizacion de. cada peticion.
	 */
	private boolean success;
	
	/**
	 * Atributo de respuesta interpretado por strust con el catalogo de tipo de suma asegurada 
	 * List<LlaveValorVO> con los valores de la consulta.
	 */
	private List<LlaveValorVO> catalogoTipoSumaAsegurada;
	
	/**
	 * Atributo de respuesta interpretado por strust con el catalogo de tipo de suma asegurada 
	 * List<LlaveValorVO> con los valores de la consulta.
	 */
	private List<LlaveValorVO> catalogoSumaAsegurada;
	
	/**
	 * Atributo de respuesta interpretado por strust con el catalogo de moneda de suma asegurada 
	 * List<LlaveValorVO> con los valores de la consulta.
	 */
	private List<LlaveValorVO> catalogoMoneda;
	
	/**
	 * Atributo de respuesta interpretado por strust con la lista de sumas aseguradas a nivel de producto
	 * List<SumaAseguradaVO> con los valores de la consulta.
	 */
	private List<SumaAseguradaVO> listaSumasAseguradas;
	
	/**
	 * Atributo agregado por struts que contiene el tipo de catalogo que se cargara para la suma asegurada.
	 */
	private String catalogo;
	
	/**
	 * Atributo agregado por struts que contiene el codigo de ramo de suma asegurada.
	 */
	private String codigoRamo;
	
	/**
	 * Atributo agregado por struts que contiene el codigo de capital para la suma asegurada.
	 */
	private String codigoCapital;
	
	/**
	 * Atributo agregado por struts que contiene la suma asegurada para el producto.
	 */
	private String sumaAseguradaProducto;
	
	/**
	 * Atributo agregado por struts que contiene la descripcion del tipo de suma asegurada para el producto.
	 */
	private String descripcionTipoSumaAsegurada;
	
	/**
	 * Atributo agregado por struts que contiene el codigo del tipo de suma asegurada para el producto.
	 */
	private String codigoTipoSumaAsegurada;
	
	/**
	 * Atributo agregado por struts que contiene la descripcion de la moneda de suma asegurada para el producto.
	 */
	private String descripcionMonedaSumaAsegurada;
	
	/**
	 * Atributo agregado por struts que contiene el codigo de la moneda de suma asegurada para el producto.
	 */
	private String codigoMonedaSumaAsegurada;
	
	/**
	 * Atributo agregado por struts que contiene el codigo de tipo de situacion para la 
	 * suma asegurada a nivel de inciso.
	 */
	private String codigoTipoSituacion;
	
	/**
	 * Atributo de respuesta interpretado por strust con la lista de sumas aseguradas a nivel de inciso
	 * List<SumaAseguradaIncisoVO> con los valores de la consulta.
	 */
	private List<SumaAseguradaIncisoVO> listaSumasAseguradasInciso;
	
	//**********suma asegurada nivel inciso*****************
	/**
	 * Atributo agregado por struts que contiene el codigo de la suma asegurada a nivel de inciso.
	 */
	private String codigoSumaAsegurada;
	
	/**
	 * Atributo agregado por struts que contiene la descripcion de la suma asegurada a nivel de inciso.
	 */
	private String descripcionSumaAsegurada;
	
	/**
	 * Atributo agregado por struts que contiene el codigo del valor para la suma asegurada a nivel de inciso.
	 */
	private String codigoListaValor;
	
	/**
	 * Atributo agregado por struts que contiene la descripcion del valor para la suma asegurada a nivel de inciso.
	 */
	private String descripcionListaValor;
	
	/**
	 * Atributo agregado por struts que contiene el valor por defecto para la suma asegurada a nivel de inciso.
	 */
	private String valorDefecto;
	
	/**
	 * Atributo agregado por struts que contiene la obligatoriedad para la suma asegurada a nivel de inciso.
	 */
	private String obligatorio;
	
	/**
	 * Atributo agregado por struts que contiene la insercion para la suma asegurada a nivel de inciso.
	 */
	private String inserta;
	
	/**
	 * Atributo agregado por struts que contiene el codigo de la expresion asociada a la suma asegurada 
	 * a nivel de inciso.
	 */
	private String codigoExpresion;
	
	/**
	 * Atributo agregado por struts que contiene el nivel de la suma asegurada (producto o inciso).
	 */
	private String nivel;
	
	/**
	 * Atributo de respuesta interpretado por strust con la lista de las leyendas para las sumas aseguradas a nivel de cobertura
	 * List<SumaAseguradaIncisoVO> con los valores de la consulta.
	 */
	private List<LlaveValorVO> catalogoLeyenda;
	
	/**
	 * Atributo agregado por struts que contiene el codigo de la cobertura.
	 */
	private String codigoCobertura;
	
	private String codigoTipoCapital;
	private String descripcionCapital;
	private String codigoLeyenda;
	private String descripcionLeyenda;
	private String switchReinstalacion;
	
	/**
	 * Metodo <code>execute</code> con el que es llamado desde Struts para
	 * atender la peticion web.
	 * 
	 * @return INPUT
	 * @throws Exception
	 */
	public String execute() throws Exception {
		if(log.isDebugEnabled()){
			log.debug("Entro a SumaAseguradaAction");
		}
		return INPUT;
	}
	
	/**
     * Metodo <code>catalogosSumaAsegurada</code> con el que es cargado los catalogos de suma asegurada.
     * 
     * @return success
     * @throws Exception
     */
	public String catalogosSumaAsegurada() throws Exception{
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("Entro a catalogos de suma asegurada");
		}
		catalogoTipoSumaAsegurada = null;
		if(session.containsKey("CODIGO_NIVEL0")){
			codigoRamo=(String) session.get("CODIGO_NIVEL0");
		}
		if(session.containsKey("CODIGO_NIVEL2")){
			codigoTipoSituacion=(String) session.get("CODIGO_NIVEL2");
		}
		if(session.containsKey("CODIGO_NIVEL6")){
			codigoCobertura=(String) session.get("CODIGO_NIVEL6");
		}
		if(isDebugEnabled){
			log.debug("CODIGORAMO--"+codigoRamo);			
			log.debug("CDTIPSIT--"+codigoTipoSituacion);
			log.debug("CDCOBERTURA--"+codigoCobertura);
		}
		if(catalogo.equals("tipoSumaAsegurada")){				
			catalogoTipoSumaAsegurada = sumaAseguradaManagerJdbcTemplateImpl.catalogoTipoSumaAsegurada();
				/*if(isDebugEnabled){
					for(LlaveValorVO tipo: catalogoTipoSumaAsegurada){
						log.debug("DSTIPOSUMA"+tipo.getValue());
					}
				}*/
		}
		if(catalogo.equals("sumaAsegurada")&& codigoRamo!= null){
			catalogoSumaAsegurada = sumaAseguradaManagerJdbcTemplateImpl.catalogoSumaAsegurada(codigoRamo);
		}
		
		if(catalogo.equals("monedaSumaAsegurada")){
			catalogoMoneda = sumaAseguradaManagerJdbcTemplateImpl.catalogoMonedaSumaASegurada();
		}
		if(catalogo.equals("gridSumaAsegurada")&& codigoRamo!=null){
			codigoCapital = null;
			//codigoRamo="777";//solo para prueba
			listaSumasAseguradas = sumaAseguradaManagerJdbcTemplateImpl.listaSumaAsegurada(codigoRamo,codigoCapital);
			/*if(isDebugEnabled){
				for(SumaAseguradaVO lista: listaSumasAseguradas){
					log.debug("DSCAPITAL"+ lista.getDescripcionCapital());
					log.debug("DSMONEDA"+lista.getDescripcionMoneda());
				}
			}*/
		}
		if(catalogo.equals("gridSumaAseguradaInciso")&& codigoRamo!=null && codigoCobertura!=null && codigoTipoSituacion!=null){
			codigoCapital = null;			
			listaSumasAseguradasInciso = sumaAseguradaManagerJdbcTemplateImpl.listaSumaAseguradaInciso(codigoRamo,codigoCobertura,codigoCapital,codigoTipoSituacion);			
		}
		if(catalogo.equals("leyenda")){
			catalogoLeyenda=getTablasManager().obtenerLeyendaSumaAsegurada();
		}		
		success = true;
		return SUCCESS;
	}
	
	/**
     * Metodo <code>agregarSumaAsegurada</code> con el que es agregado o editado 
     * una suma asegurada.
     * 
     * @return success
     * @throws Exception
     */
	public String agregarSumaAsegurada() throws Exception{
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("Entro a agregar suma asegurada");
		}
		
		if(session.containsKey("CODIGO_NIVEL0")){
			codigoRamo=(String) session.get("CODIGO_NIVEL0");
			if(isDebugEnabled){
				log.debug("CODIGORAMO-->"+codigoRamo);
			}
		}
		if(session.containsKey("CODIGO_NIVEL2")){
			codigoTipoSituacion=(String) session.get("CODIGO_NIVEL2");
		}
		if(session.containsKey("CODIGO_NIVEL6")){
			codigoCobertura=(String) session.get("CODIGO_NIVEL6");
		}
		/*if(nivel.equals("producto")){
			if(codigoRamo != null){
				if(isDebugEnabled){
					log.debug("RAMO-->"+codigoRamo);
					log.debug("CODCAPITAL-->"+codigoCapital);
					log.debug("DSCAPITAL-->"+sumaAseguradaProducto);
					log.debug("CODTIPOCAPITAL-->"+codigoTipoSumaAsegurada);
					log.debug("DSTIPOCAPITAL-->"+descripcionTipoSumaAsegurada);
					log.debug("CODMONEDA"+codigoMonedaSumaAsegurada);
					log.debug("DSMONEDA"+descripcionMonedaSumaAsegurada);
				}
			
				SumaAseguradaVO sumaAsegurada = new SumaAseguradaVO();
				sumaAsegurada.setCodigoRamo(codigoRamo);
				sumaAsegurada.setCodigoCapital(codigoCapital);
				sumaAsegurada.setDescripcionCapital(sumaAseguradaProducto);
				sumaAsegurada.setCodigoTipoCapital(codigoTipoSumaAsegurada);
				sumaAsegurada.setDescripcionTipoCapital(descripcionTipoSumaAsegurada);
				sumaAsegurada.setCodigoMoneda(codigoMonedaSumaAsegurada);
				sumaAsegurada.setDescripcionMoneda(descripcionMonedaSumaAsegurada);
			
				sumaAseguradaManager.agregaSumaAseguradaProducto(sumaAsegurada);
			
			}
		}*/
		//if(nivel.equals("inciso")){
			if(codigoRamo != null && codigoTipoSituacion != null){
				if(switchReinstalacion == null){
					switchReinstalacion="N";			
				}/*if(obligatorio == null){
					obligatorio="N";			
				}if(inserta == null){
					inserta="N";			
				}*/
				if(isDebugEnabled){
					log.debug("RAMO-->"+codigoRamo);
					log.debug("codigoTipoCapital-->"+codigoTipoCapital);
					log.debug("descripcionCapital-->"+descripcionCapital);
					log.debug("codigoCobertura"+codigoCobertura);
					log.debug("descripcionLeyenda"+descripcionLeyenda);
					log.debug("codigoLeyenda"+codigoLeyenda);
					log.debug("TIPOSITUACION-->"+codigoTipoSituacion);
					log.debug("CODLISTAVALOR-->"+codigoListaValor);
					log.debug("DSLISTAVALOR-->"+descripcionListaValor);
					log.debug("switchReinstalacion"+switchReinstalacion);
					log.debug("EXPRESION " + codigoExpresion);
				}
				
				SumaAseguradaIncisoVO sumaAseguradaInciso = new SumaAseguradaIncisoVO();
				if(codigoCapital != null){
					sumaAseguradaInciso.setCodigoCapital(codigoCapital);
				}else{
					sumaAseguradaInciso.setCodigoCapital(null);
				}
				sumaAseguradaInciso.setCodigoRamo(codigoRamo);				
				sumaAseguradaInciso.setCodigoTipoCapital(codigoTipoCapital);
				sumaAseguradaInciso.setDescripcionCapital(descripcionCapital);
				sumaAseguradaInciso.setCodigoCobertura(codigoCobertura);
				
				if ("Seleccione leyenda".equals(descripcionLeyenda)) {
					codigoLeyenda = "0";
				}
				
				sumaAseguradaInciso.setCodigoLeyenda(codigoLeyenda);				
				sumaAseguradaInciso.setCodigoTipoSituacion(codigoTipoSituacion);
				
				if ("Seleccione un valor".equals(descripcionListaValor)) {
					codigoListaValor = "0";
				}
				
				sumaAseguradaInciso.setCodigoListaValor(codigoListaValor);
				sumaAseguradaInciso.setSwitchReinstalacion(switchReinstalacion);
				if (StringUtils.isNotBlank(codigoExpresion))
					sumaAseguradaInciso.setCodigoExpresion(codigoExpresion);
				//sumaAseguradaInciso.setCodigoCapital(codigoSumaAsegurada);
				//sumaAseguradaInciso.setDescripcionCapital(descripcionSumaAsegurada);
				//sumaAseguradaInciso.setSwitchListaValor(valorDefecto);
				//sumaAseguradaInciso.setSwitchObligatorio(obligatorio);
				//sumaAseguradaInciso.setSwitchInserta(inserta);
				
				
				//sumaAseguradaManager.agregaSumaAseguradaInciso(sumaAseguradaInciso);con backbone
				sumaAseguradaManagerJdbcTemplateImpl.agregaSumaAseguradaInciso(sumaAseguradaInciso);
			}
		//}

		success = true;
		return SUCCESS;
	}
	
	/**
     * Metodo <code>eliminarSumaAsegurada</code> con el que es eliminada la suma asegurada de un producto
     * o de un inciso.
     * 
     * @return success
     * @throws Exception
     */
    public String eliminarSumaAsegurada() throws Exception {
      boolean isDebugEnabled = log.isDebugEnabled();
      if(isDebugEnabled){
    	  log.debug("Entro a eliminar suma asegurada");
    	  log.debug("codigo"+codigoCapital);
      }
      
      if(session.containsKey("CODIGO_NIVEL0")){
    	  	codigoRamo=(String) session.get("CODIGO_NIVEL0");
    	  	if(isDebugEnabled){
    	  		log.debug("CODIGORAMO-->"+codigoRamo);
    	  	}
      }
      if(session.containsKey("CODIGO_NIVEL2")){
			codigoTipoSituacion=(String) session.get("CODIGO_NIVEL2");
      }
      if(isDebugEnabled){
    	  log.debug("NIVEL"+nivel);
      }
      
      if(nivel.equals("producto")){
    	  if(codigoCapital != null && codigoRamo!=null){
    		  sumaAseguradaManagerJdbcTemplateImpl.eliminaSumaAseguradaProducto(codigoCapital,codigoRamo);
    		  //if(isDebugEnabled){
    		  //	log.debug("ELIMINA NIVEL PROD");
    		  //}
    	  }else{
    		  success=false;
    	  }
      }if(nivel.equals("inciso")){
    	  if(codigoCapital != null && codigoRamo!=null && codigoTipoSituacion!=null){
    		  sumaAseguradaManagerJdbcTemplateImpl.eliminaSumaAseguradaInciso(codigoCapital,codigoRamo,codigoTipoSituacion);
    		  //if(isDebugEnabled){
    		  //log.debug("ELIMINA NIVEL INC");
    		  //}
    	  }else{
    		  success=false;
    	  }
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

	public List<LlaveValorVO> getCatalogoTipoSumaAsegurada() {
		return catalogoTipoSumaAsegurada;
	}

	public void setCatalogoTipoSumaAsegurada(
			List<LlaveValorVO> catalogoTipoSumaAsegurada) {
		this.catalogoTipoSumaAsegurada = catalogoTipoSumaAsegurada;
	}

	public void setSumaAseguradaManager(SumaAseguradaManager sumaAseguradaManager) {
		this.sumaAseguradaManager = sumaAseguradaManager;
	}

	/**
	 * Metodo utilizado por Spring para inyectar el manager
	 * @param sumaAseguradaManagerJdbcTemplateImpl
	 */
	public void setSumaAseguradaManagerJdbcTemplateImpl(
			SumaAseguradaManagerJdbcTemplate sumaAseguradaManagerJdbcTemplateImpl) {
		this.sumaAseguradaManagerJdbcTemplateImpl = sumaAseguradaManagerJdbcTemplateImpl;
	}

	public String getCatalogo() {
		return catalogo;
	}

	public void setCatalogo(String catalogo) {
		this.catalogo = catalogo;
	}

	public String getCodigoRamo() {
		return codigoRamo;
	}

	public void setCodigoRamo(String codigoRamo) {
		this.codigoRamo = codigoRamo;
	}

	public List<LlaveValorVO> getCatalogoSumaAsegurada() {
		return catalogoSumaAsegurada;
	}

	public void setCatalogoSumaAsegurada(List<LlaveValorVO> catalogoSumaAsegurada) {
		this.catalogoSumaAsegurada = catalogoSumaAsegurada;
	}

	public List<LlaveValorVO> getCatalogoMoneda() {
		return catalogoMoneda;
	}

	public void setCatalogoMoneda(List<LlaveValorVO> catalogoMoneda) {
		this.catalogoMoneda = catalogoMoneda;
	}

	public List<SumaAseguradaVO> getListaSumasAseguradas() {
		return listaSumasAseguradas;
	}

	public void setListaSumasAseguradas(List<SumaAseguradaVO> listaSumasAseguradas) {
		this.listaSumasAseguradas = listaSumasAseguradas;
	}

	public String getCodigoCapital() {
		return codigoCapital;
	}

	public void setCodigoCapital(String codigoCapital) {
		this.codigoCapital = codigoCapital;
	}

	public String getSumaAseguradaProducto() {
		return sumaAseguradaProducto;
	}

	public void setSumaAseguradaProducto(String sumaAseguradaProducto) {
		this.sumaAseguradaProducto = sumaAseguradaProducto;
	}

	public String getDescripcionTipoSumaAsegurada() {
		return descripcionTipoSumaAsegurada;
	}

	public void setDescripcionTipoSumaAsegurada(String descripcionTipoSumaAsegurada) {
		this.descripcionTipoSumaAsegurada = descripcionTipoSumaAsegurada;
	}

	public String getCodigoTipoSumaAsegurada() {
		return codigoTipoSumaAsegurada;
	}

	public void setCodigoTipoSumaAsegurada(String codigoTipoSumaAsegurada) {
		this.codigoTipoSumaAsegurada = codigoTipoSumaAsegurada;
	}

	public String getDescripcionMonedaSumaAsegurada() {
		return descripcionMonedaSumaAsegurada;
	}

	public void setDescripcionMonedaSumaAsegurada(
			String descripcionMonedaSumaAsegurada) {
		this.descripcionMonedaSumaAsegurada = descripcionMonedaSumaAsegurada;
	}

	public String getCodigoMonedaSumaAsegurada() {
		return codigoMonedaSumaAsegurada;
	}

	public void setCodigoMonedaSumaAsegurada(String codigoMonedaSumaAsegurada) {
		this.codigoMonedaSumaAsegurada = codigoMonedaSumaAsegurada;
	}

	public String getCodigoTipoSituacion() {
		return codigoTipoSituacion;
	}

	public void setCodigoTipoSituacion(String codigoTipoSituacion) {
		this.codigoTipoSituacion = codigoTipoSituacion;
	}

	public List<SumaAseguradaIncisoVO> getListaSumasAseguradasInciso() {
		return listaSumasAseguradasInciso;
	}

	public void setListaSumasAseguradasInciso(
			List<SumaAseguradaIncisoVO> listaSumasAseguradasInciso) {
		this.listaSumasAseguradasInciso = listaSumasAseguradasInciso;
	}

	public String getCodigoSumaAsegurada() {
		return codigoSumaAsegurada;
	}

	public void setCodigoSumaAsegurada(String codigoSumaAsegurada) {
		this.codigoSumaAsegurada = codigoSumaAsegurada;
	}

	public String getDescripcionSumaAsegurada() {
		return descripcionSumaAsegurada;
	}

	public void setDescripcionSumaAsegurada(String descripcionSumaAsegurada) {
		this.descripcionSumaAsegurada = descripcionSumaAsegurada;
	}

	public String getCodigoListaValor() {
		return codigoListaValor;
	}

	public void setCodigoListaValor(String codigoListaValor) {
		this.codigoListaValor = codigoListaValor;
	}

	public String getDescripcionListaValor() {
		return descripcionListaValor;
	}

	public void setDescripcionListaValor(String descripcionListaValor) {
		this.descripcionListaValor = descripcionListaValor;
	}

	public String getValorDefecto() {
		return valorDefecto;
	}

	public void setValorDefecto(String valorDefecto) {
		this.valorDefecto = valorDefecto;
	}

	public String getObligatorio() {
		return obligatorio;
	}

	public void setObligatorio(String obligatorio) {
		this.obligatorio = obligatorio;
	}

	public String getInserta() {
		return inserta;
	}

	public void setInserta(String inserta) {
		this.inserta = inserta;
	}

	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public String getCodigoExpresion() {
		return codigoExpresion;
	}

	public void setCodigoExpresion(String codigoExpresion) {
		this.codigoExpresion = codigoExpresion;
	}

	public List<LlaveValorVO> getCatalogoLeyenda() {
		return catalogoLeyenda;
	}

	public void setCatalogoLeyenda(List<LlaveValorVO> catalogoLeyenda) {
		this.catalogoLeyenda = catalogoLeyenda;
	}

	public String getCodigoCobertura() {
		return codigoCobertura;
	}

	public void setCodigoCobertura(String codigoCobertura) {
		this.codigoCobertura = codigoCobertura;
	}

	public String getCodigoTipoCapital() {
		return codigoTipoCapital;
	}

	public void setCodigoTipoCapital(String codigoTipoCapital) {
		this.codigoTipoCapital = codigoTipoCapital;
	}

	public String getDescripcionCapital() {
		return descripcionCapital;
	}

	public void setDescripcionCapital(String descripcionCapital) {
		this.descripcionCapital = descripcionCapital;
	}

	public String getCodigoLeyenda() {
		return codigoLeyenda;
	}

	public void setCodigoLeyenda(String codigoLeyenda) {
		this.codigoLeyenda = codigoLeyenda;
	}

	public String getSwitchReinstalacion() {
		return switchReinstalacion;
	}

	public void setSwitchReinstalacion(String switchReinstalacion) {
		this.switchReinstalacion = switchReinstalacion;
	}

	public String getDescripcionLeyenda() {
		return descripcionLeyenda;
	}

	public void setDescripcionLeyenda(String descripcionLeyenda) {
		this.descripcionLeyenda = descripcionLeyenda;
	}

}
