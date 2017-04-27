package mx.com.gseguros.wizard.configuracion.producto.coberturas.web;

import java.util.ArrayList;
import java.util.List;

import mx.com.gseguros.wizard.configuracion.producto.coberturas.model.CoberturaVO;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.RamaVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.service.CoberturaManager;
import mx.com.gseguros.wizard.configuracion.producto.web.Padre;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 
 * @author Edgar Perez
 * @version 2.0
 * @since 1.0
 *
 *Clase que contiene los metodos de cargar, agregar,
 *editar y asociar una cobertura al producto 
 */
public class PrincipalCoberturasAction extends Padre {

	
	private static final long serialVersionUID = -2974533224097605584L;
	private static final transient Log log = LogFactory.getLog(PrincipalCoberturasAction.class);
	
	/**
	 * Manager con implementacion de Endpoint para la consulta a BD.
	 */
	private CoberturaManager coberturaManager;
	
	/**
	 * Atributo de respuesta interpretado por strust como la correcta
	 * realizacion de cada peticion.
	 */
	private boolean success;
	
	/**
	 * Atributo de respuesta interpretado por strust con la lista de beans tipo
	 * LlaveValorVO con los valores de la consulta de coberturas.
	 */
	private List<LlaveValorVO> listaCoberturas;
	
	/**
	 * Atributo de respuesta interpretado por strust con la lista de beans tipo
	 * LlaveValorVO con los valores de la consulta de condiciones de coberturas.
	 */
	private List<LlaveValorVO> listaCondicionCobertura;
	
	/**
	 * Atributo de respuesta interpretado por strust con la lista de beans tipo
	 * LlaveValorVO con los valores de la consulta de tipos de coberturas.
	 */
	private List<LlaveValorVO> listaTipoCobertura;
	
	/**
	 * Atributo de respuesta interpretado por strust con la lista de beans tipo
	 * LlaveValorVO con los valores de la consulta de ramos para coberturas.
	 */
	private List<LlaveValorVO> listaRamoCobertura;
	
	/**
	 * Atributo de respuesta interpretado por strust con la lista de beans tipo
	 * LlaveValorVO con los valores de la consulta de suma asegurada para coberturas.
	 */
	private List<LlaveValorVO> listaSumaAsegurada;
	
	/**
	 * Atributo agregado por struts que contiene la clave de la cobertura
	 * asociada al producto.
	 */
	private String claveCobertura;
	
	/**
	 * Atributo agregado por struts que contiene la descripcion de la cobertura
	 * asociada al producto.
	 */
	private String descripcionCobertura;
	
	/**
	 * Atributo agregado por struts que contiene el tipo de la cobertura
	 * asociada al producto.
	 */
	private String tipoCobertura;
	
	/**
	 * Atributo agregado por struts que contiene la descripcion del ramo para la cobertura
	 * asociada al producto.
	 */
	private String ramoCobertura;
	
	/**
	 * Atributo agregado por struts que contiene el codigo del ramo para la cobertura
	 * asociada al producto.
	 */
	private String codigoRamo;
	
	/**
	 * Atributo agregado por struts que contiene la clave del tipo de situacion para la cobertura
	 * asociada al producto.
	 */
	private String codigoTipoSituacion;
	
	/**
	 * Atributo agregado por struts que contiene el valor de la reinstalacion de la cobertura
	 * asociada al producto.
	 */
	private String reinstalacion;
	
	/**
	 * Atributo agregado por struts que contiene el valor del indice inflacionario de la cobertura
	 * asociada al producto.
	 */
	private String indiceInflacionario;
	
	/**
	 * Atributo agregado por struts que contiene la descripcion del combo que se desea cargar 
	 * en la pantalla de coberturas
	 */
	private String combo;
	
	/**
	 * Atributo agregado por struts que contiene la clave del tipo de cobertura
	 * asociada al producto.
	 */
	private String claveTipoCobertura;
	
	/**
	 * Atributo agregado por struts que contiene la clave del ramo para la cobertura
	 * agregada al combo.
	 */
	private String claveRamoCobertura;
	
	/**
	 * Atributo agregado por struts que contiene el valor de la suma asegurada de la cobertura
	 * asociada al producto.
	 */
	private String sumaAsegurada;
	
	/**
	 * Atributo agregado por struts que contiene el valor obligatorio de la cobertura
	 * asociada al producto.
	 */
	private String obligatorio;
	
	/**
	 * Atributo agregado por struts que contiene el valor inserta de la cobertura
	 * asociada al producto.
	 */
	private String inserta;
	
	/**
	 * Atributo agregado por struts que contiene la descripcion del capital para la cobertura
	 * asociada al producto.
	 */
	private String descripcionCapital;
	
	/**
	 * Atributo agregado por struts que contiene el codigo del capital para la cobertura
	 * asociada al producto.
	 */
	private String codigoCapital;
	
	/**
	 * Atributo agregado por struts que contiene la descripcion de la condicion para la cobertura
	 * asociada al producto.
	 */
	private String descripcionCondicion;
	
	/**
	 * Atributo agregado por struts que contiene el codigo de la condicion para la cobertura
	 * asociada al producto.
	 */
	private String codigoCondicion;
	
	/**
	 * Atributo de respuesta interpretado por strust con la lista de beans tipo
	 * CoberturaVO con los valores de la consulta de coberturas asociadas.
	 */
	private List<CoberturaVO> listaCoberturaAsociada;
	


	/**
	 * Metodo <code>execute</code> con el que es llamado desde Struts para
	 * atender la peticion web.
	 * 
	 * @return INPUT
	 * @throws Exception
	 */
	public String execute() throws Exception {
		return INPUT;
	}


	/**
	 * Metodo <code>cargaCombosCoberturas</code> con el que son llamado
	 * desde Struts todos los valores para cargar los combos  de
	 * la pantalla coberturas.
	 * 
	 * @return success
	 * @throws Exception
	 */
	public String cargaCombosCoberturas() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("Entro a cargar combos de coberturas");
		}
		if(session.containsKey("CODIGO_NIVEL0")){
			codigoRamo=(String)session.get("CODIGO_NIVEL0");
		}else
		codigoRamo="120";
		if(isDebugEnabled){
			log.debug("COMBO--"+combo);
			log.debug("CODIGORAMO--"+codigoRamo);
		}
		if (combo != null) {
			if(combo.equals("cobertura")){
				listaCoberturas = coberturaManager.obtieneCoberturas();
			}
			if(combo.equals("condicion")){
				listaCondicionCobertura = coberturaManager.obtieneCondicionCobertura();
			}
			if(combo.equals("tipo")){
				listaTipoCobertura = coberturaManager.obtieneTipoCobertura();
			}
			if(combo.equals("ramo")){
				listaRamoCobertura = coberturaManager.obtieneRamoCobertura();
			}
			if(combo.equals("sumaAsegurada")&& codigoRamo!= null){
				if(session.containsKey("CODIGO_NIVEL0")){
				codigoRamo=(String) session.get("CODIGO_NIVEL0");
				}
				listaSumaAsegurada = coberturaManager.obtieneSumaAseguradaCobertura(codigoRamo);
			}
			
		}
		success = true;
		return SUCCESS;

	}
	
	/**
	 * Metodo <code>agregaCobertura</code> con el que es insertada la nueva cobertura a la BD
	 * 
	 * @return success
	 * @throws Exception
	 */
	public String agregaCobertura() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("Entro a agregar cobertura");
		}
		
		 if(reinstalacion == null){
			reinstalacion = "N";
			
		}if(indiceInflacionario == null){
			indiceInflacionario = "N";
				
		}
		boolean bandera = true;
		listaCoberturas = coberturaManager.obtieneCoberturas();
		for(LlaveValorVO coberturas : listaCoberturas){
			if(coberturas.getKey().equalsIgnoreCase(claveCobertura)){
				bandera = false;
			}
		}
		if(bandera){
			if(isDebugEnabled){
				log.debug("CLAVE--"+claveCobertura);
				log.debug("DESCRIPCION--"+descripcionCobertura);
				log.debug("TIPO--"+claveTipoCobertura);
				log.debug("RAMO--"+claveRamoCobertura);
				log.debug("REINSTALACION--"+reinstalacion);
				log.debug("INDICEINFLA--"+indiceInflacionario);
			}
			CoberturaVO cobertura = new CoberturaVO();
			cobertura.setClaveCobertura(claveCobertura);
			cobertura.setDescripcionCobertura(descripcionCobertura);
			cobertura.setTipoCobertura(claveTipoCobertura);
			cobertura.setRamoCobertura(claveRamoCobertura);
			cobertura.setReinstalacion(reinstalacion);
			cobertura.setIndiceInflacionario(indiceInflacionario);
		
			coberturaManager.insertaCobertura(cobertura);
			
			success = true;
		}else{
			success = false;
		}
		return SUCCESS;

	}
	
	/**
	 * Metodo <code>asociaCobertura</code> con el que es asociada la cobertura al producto
	 * 
	 * @return success
	 * @throws Exception
	 */
	public String asociaCobertura() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("Entro a asociar cobertura");
		}
		
		if(session.containsKey("CODIGO_NIVEL0")){
			codigoRamo=(String) session.get("CODIGO_NIVEL0");
		}
		if(session.containsKey("CODIGO_NIVEL2")){
			codigoTipoSituacion=(String) session.get("CODIGO_NIVEL2");
		}else
			codigoTipoSituacion = "NA";
		
		//para prueba
		//codigoRamo="120";
		//codigoTipoSituacion="A2";
		
		if(inserta == null)
			inserta = "N";		
		else if(inserta.equals("on"))
			inserta="S";		
			
		if(obligatorio == null)
			obligatorio = "N";			
		else if(obligatorio.equals("on"))
			obligatorio ="S";
		
		if(sumaAsegurada == null)
			sumaAsegurada = "N";			
		else if(sumaAsegurada.equals("on"))			
			sumaAsegurada = "S";
				
		
		if(codigoRamo!=null && codigoTipoSituacion!=null){
			if(isDebugEnabled){
				log.debug("CODIGORAMO--"+codigoRamo);
				log.debug("CODIGOSITUACION--"+codigoTipoSituacion);
				log.debug("CODIGOCOBERTURA--"+claveCobertura);
				log.debug("CODIGOCAPITAL--"+codigoCapital);
				log.debug("CODIGOCONDICION--"+codigoCondicion);
				log.debug("INSERTA--"+inserta);
				log.debug("OBLIGATORIO--"+obligatorio);
				log.debug("SUMAASEGURADA--"+sumaAsegurada);
			}
			CoberturaVO cobertura = new CoberturaVO();
			cobertura.setCodigoRamo(codigoRamo);
			cobertura.setCodigoTipoSituacion(codigoTipoSituacion);
			cobertura.setClaveCobertura(claveCobertura);
			cobertura.setCodigoCapital(codigoCapital);
			cobertura.setCodigoCondicion(codigoCondicion);
			cobertura.setInserta(inserta);
			cobertura.setObligatorio(obligatorio);
			
		
			coberturaManager.asociaCobertura(cobertura);
			
			success=true;
		}
		if(success){
			List<RamaVO> temporalTree=(List<RamaVO>) session.get("ARBOL_PRODUCTOS");
			session.put("ARBOL_PRODUCTOS_RECARGAR", temporalTree);
			session.remove("ARBOL_PRODUCTOS");
		}
		return SUCCESS;
	}
	
	/**
	 * Metodo <code>cargaCoberturaAsociada</code> con el que se obtiene la cobertura asociada al producto
	 * 
	 * @return success
	 * @throws Exception
	 */
	public String cargaCoberturaAsociada() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("Entro a cargar cobertura asociada");
		}
		
		if(session.containsKey("CODIGO_NIVEL0")){
			codigoRamo=(String) session.get("CODIGO_NIVEL0");
		}
		if(session.containsKey("CODIGO_NIVEL2")){
			codigoTipoSituacion=(String) session.get("CODIGO_NIVEL2");
		}		
		if(session.containsKey("CODIGO_NIVEL4")){
			claveCobertura=(String) session.get("CODIGO_NIVEL4");
		}
		//para prueba
		
		
		
		
		List<CoberturaVO> listaAsociada = new ArrayList<CoberturaVO>();			
		CoberturaVO coberturaAsociada = new CoberturaVO();
		if(codigoRamo!=null && codigoTipoSituacion!=null && claveCobertura!=null){			
			coberturaAsociada = coberturaManager.obtieneCoberturaAsociada(codigoRamo,codigoTipoSituacion,claveCobertura);
			if(isDebugEnabled){
				log.debug("DESCRIPCIONCOBERTURA--"+coberturaAsociada.getDescripcionCobertura());
				log.debug("SWSUMAASEGURADA--"+coberturaAsociada.getSumaAsegurada());
				log.debug("DESCRIPCIONCAPITAL--"+coberturaAsociada.getDescripcionCapital());
				log.debug("OBLIGATORIO--"+coberturaAsociada.getObligatorio());
				log.debug("INSERTA--"+coberturaAsociada.getInserta());
				log.debug("DESCRIPCIONCONDICION--"+coberturaAsociada.getDescripcionCondicion());
			}
			success=true;
		}
		listaAsociada.add(coberturaAsociada);
		listaCoberturaAsociada =listaAsociada;
			
		return SUCCESS;
	}
	//GETTERS Y SETTERS
	public boolean isSuccess() {
		return success;
	}


	public void setSuccess(boolean success) {
		this.success = success;
	}


	public void setCoberturaManager(CoberturaManager coberturaManager) {
		this.coberturaManager = coberturaManager;
	}


	public List<LlaveValorVO> getListaCoberturas() {
		return listaCoberturas;
	}


	public void setListaCoberturas(List<LlaveValorVO> listaCoberturas) {
		this.listaCoberturas = listaCoberturas;
	}

	
	public List<LlaveValorVO> getListaTipoCobertura() {
		return listaTipoCobertura;
	}


	public void setListaTipoCobertura(List<LlaveValorVO> listaTipoCobertura) {
		this.listaTipoCobertura = listaTipoCobertura;
	}


	public List<LlaveValorVO> getListaCondicionCobertura() {
		return listaCondicionCobertura;
	}


	public void setListaCondicionCobertura(
			List<LlaveValorVO> listaCondicionCobertura) {
		this.listaCondicionCobertura = listaCondicionCobertura;
	}


	public List<LlaveValorVO> getListaRamoCobertura() {
		return listaRamoCobertura;
	}


	public void setListaRamoCobertura(List<LlaveValorVO> listaRamoCobertura) {
		this.listaRamoCobertura = listaRamoCobertura;
	}


	public String getCombo() {
		return combo;
	}


	public void setCombo(String combo) {
		this.combo = combo;
	}


	public String getClaveCobertura() {
		return claveCobertura;
	}


	public void setClaveCobertura(String claveCobertura) {
		this.claveCobertura = claveCobertura;
	}


	public String getDescripcionCobertura() {
		return descripcionCobertura;
	}


	public void setDescripcionCobertura(String descripcionCobertura) {
		this.descripcionCobertura = descripcionCobertura;
	}


	public String getTipoCobertura() {
		return tipoCobertura;
	}


	public void setTipoCobertura(String tipoCobertura) {
		this.tipoCobertura = tipoCobertura;
	}


	public String getRamoCobertura() {
		return ramoCobertura;
	}


	public void setRamoCobertura(String ramoCobertura) {
		this.ramoCobertura = ramoCobertura;
	}


	public String getClaveTipoCobertura() {
		return claveTipoCobertura;
	}


	public void setClaveTipoCobertura(String claveTipoCobertura) {
		this.claveTipoCobertura = claveTipoCobertura;
	}


	public String getClaveRamoCobertura() {
		return claveRamoCobertura;
	}


	public void setClaveRamoCobertura(String claveRamoCobertura) {
		this.claveRamoCobertura = claveRamoCobertura;
	}


	public String getReinstalacion() {
		return reinstalacion;
	}


	public void setReinstalacion(String reinstalacion) {
		this.reinstalacion = reinstalacion;
	}


	public String getIndiceInflacionario() {
		return indiceInflacionario;
	}


	public void setIndiceInflacionario(String indiceInflacionario) {
		this.indiceInflacionario = indiceInflacionario;
	}


	public List<LlaveValorVO> getListaSumaAsegurada() {
		return listaSumaAsegurada;
	}


	public void setListaSumaAsegurada(List<LlaveValorVO> listaSumaAsegurada) {
		this.listaSumaAsegurada = listaSumaAsegurada;
	}


	public String getCodigoRamo() {
		return codigoRamo;
	}


	public void setCodigoRamo(String codigoRamo) {
		this.codigoRamo = codigoRamo;
	}


	public String getSumaAsegurada() {
		return sumaAsegurada;
	}


	public void setSumaAsegurada(String sumaAsegurada) {
		this.sumaAsegurada = sumaAsegurada;
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


	public String getDescripcionCapital() {
		return descripcionCapital;
	}


	public void setDescripcionCapital(String descripcionCapital) {
		this.descripcionCapital = descripcionCapital;
	}


	public String getCodigoCapital() {
		return codigoCapital;
	}


	public void setCodigoCapital(String codigoCapital) {
		this.codigoCapital = codigoCapital;
	}


	public String getDescripcionCondicion() {
		return descripcionCondicion;
	}


	public void setDescripcionCondicion(String descripcionCondicion) {
		this.descripcionCondicion = descripcionCondicion;
	}


	public String getCodigoCondicion() {
		return codigoCondicion;
	}


	public void setCodigoCondicion(String codigoCondicion) {
		this.codigoCondicion = codigoCondicion;
	}


	public String getCodigoSituacion() {
		return codigoTipoSituacion;
	}


	public void setCodigoSituacion(String codigoSituacion) {
		this.codigoTipoSituacion = codigoSituacion;
	}


	public String getCodigoTipoSituacion() {
		return codigoTipoSituacion;
	}


	public void setCodigoTipoSituacion(String codigoTipoSituacion) {
		this.codigoTipoSituacion = codigoTipoSituacion;
	}


	public List<CoberturaVO> getListaCoberturaAsociada() {
		return listaCoberturaAsociada;
	}


	public void setListaCoberturaAsociada(List<CoberturaVO> listaCoberturaAsociada) {
		this.listaCoberturaAsociada = listaCoberturaAsociada;
	}

}
