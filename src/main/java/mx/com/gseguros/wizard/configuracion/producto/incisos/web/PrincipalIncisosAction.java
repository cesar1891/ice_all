package mx.com.gseguros.wizard.configuracion.producto.incisos.web;

import java.util.List;

import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.RamaVO;
import mx.com.gseguros.wizard.configuracion.producto.incisos.model.IncisoVO;
import mx.com.gseguros.wizard.configuracion.producto.service.IncisoManager;
import mx.com.gseguros.wizard.configuracion.producto.web.Padre;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 
 * @author Edgar Perez
 * @version 2.0
 * @since 1.0
 *
 *Clase que contiene los metodos de cargar lista de incisos del producto, agregar,
 *editar y asociar un inciso al producto 
 */
public class PrincipalIncisosAction extends Padre{

	private static final long serialVersionUID = 3480546700588201196L;
	private static final transient Log log = LogFactory.getLog(PrincipalIncisosAction.class);
	
	
	/**
	 * Manager con implementacion de Endpoint para la consulta a BD.
	 */
	private IncisoManager incisoManager;
	/**
	 * Atributo de respuesta interpretado por strust como la correcta
	 * realizacion de. cada peticion.
	 */
	private boolean success;
	
	/**
	 * Atributo de respuesta interpretado por strust con la lista de beans tipo
	 * IncisoVO con los valores de la consulta.
	 */
	private List<IncisoVO> incisos;
	
	/**
	 * Atributo agregado por struts que contiene la descripcion del inciso
	 * asociado al producto.
	 */
	private String descripcion;
	
	/**
	 * Atributo agregado por struts que indica la obligatoriedad del inciso
	 * asociado al producto.
	 */
	private String obligatorio;
	
	/**
	 * Atributo agregado por struts que indica la insercion del inciso
	 * asociado al producto.
	 */
	private String inserta;
	
	/**
	 * Atributo agregado por struts que contiene la agrupacion del inciso
	 * asociado al producto.
	 */
	private String agrupador;
	
	/**
	 * Atributo agregado por struts que contiene el numero del inciso
	 * asociado al producto.
	 */
	private String numeroInciso;
	
	/**
	 * Atributo agregado por struts que contiene la descripcion del inciso
	 * agregado al catalogo.
	 */
	private String descripcionCatalogo;
	
	/**
	 * Atributo agregado por struts que contiene la clave del inciso
	 * seleccionado.
	 */
	private String clave;
	
	/**
	 * Atributo agregado por struts que contiene la clave del inciso
	 * agregado al catalogo.
	 */
	private String claveCatalogo;
	
	/**
	 * Atributo agregado por struts que indica la insercion del subinciso
	 * asociado al producto.
	 */
	private String subIncisos;
	
	/**
	 * Atributo agregado por struts que contiene el codigo del ramo
	 * del producto.
	 */
	private String codigoRamo;
	
	/**
	 * Atributo agregado por struts que contiene el codigo del tipo
	 * del producto.
	 */
	private String codigoTipo;
	
	/**
	 * Atributo agregado por struts que contiene el codigo del tipo
	 * de transaccion a realizar, 0 para insertar y 1 pra editar.
	 */
	private String editarInciso;
	
	
	/**
	 * Atributo para almacenar los mensajes devueltos por BD 
	 */
	private String messageResult;
	
	/**
	 * Metodo <code>execute</code> con el que es llamado desde Struts para
	 * atender la petici�n web.
	 * 
	 * @return INPUT
	 * @throws Exception
	 */
	public String execute() throws Exception {
		return INPUT;
	}
	
	/**
	 * Metodo <code>listaDeIncisosDelProducto</code> con el que son llamado desde
	 * Struts todas los incisos contenidos en un producto.
	 * 
	 * @return success
	 * @throws Exception
	 */
	public String listaDeIncisosDelProducto() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("Entro a lista de incisos del producto");
		}
		
			return INPUT;
	}
	public String editarInciso() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("Entro a lista de incisos del producto");
		}
		boolean bandera=true;
		if(session.containsKey("CODIGO_NIVEL0"))
			codigoRamo=(String) session.get("CODIGO_NIVEL0");
		else
		codigoRamo="400";
//		
//		if(session.containsKey("CODIGO_NIVEL1"))
//			codigoTipo=(String) session.get("CODIGO_NIVEL1");
//		else
//			codigoTipo="P1";
//			
//		
		if(session.containsKey("CODIGO_NIVEL2"))
			codigoTipo=(String) session.get("CODIGO_NIVEL2");
		else{
			//codigoTipo="P1";
			bandera=false;
		}
		if(isDebugEnabled){
			log.debug("bandera===================="+bandera);
		}
		if(codigoRamo != null && codigoTipo != null && bandera){			
			incisos = incisoManager.incisosDelProducto(codigoRamo,codigoTipo);
			if(isDebugEnabled){
				log.debug("entro al inciso de la base");
			}
		}else{
			if(isDebugEnabled){
				log.debug("entro al inciso vacio");
			}
			IncisoVO incisoVO= new IncisoVO();
			incisoVO.setCdagrupa(" ");
			incisoVO.setCdramo(" ");
			incisoVO.setCdtipsit(" ");
			incisoVO.setDstipsit(" ");
			incisoVO.setMode(" ");			
			incisoVO.setSwinsert("N");
			incisoVO.setSwobliga("N");
			incisoVO.setSwsitdec("N");
			incisoVO.setTtiposit("N");
		}
		return SUCCESS;
	}	
	/**
	 * Metodo <code>listaDeIncisosJSON</code> con el que son llamado desde
	 * Struts todas los incisos contenidos en el catalogo.
	 * 
	 * @return success
	 * @throws Exception
	 */
	public String listaDeIncisosJSON() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("Entro a lista de incisos json");
		}
		
		incisos = incisoManager.incisosJson();
		success = true;
		
		return SUCCESS;
	}
	
	/**
	 * Metodo <code>agregarNuevoInciso</code> con el que es agregado
	 * un nuevo inciso en el catalogo.
	 * @return SUCCESS
	 * @throws Exception
	 */
	//*NOTA: Retorna un valor number de la bd para el MSG_ID pero en este momento no se esta manipulando
	public String agregarNuevoInciso() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("Entro a agregar un inciso a la lista");
		}
		IncisoVO inciso = new IncisoVO();
		
		if(subIncisos != null){
			inciso.setSwsitdec("S");			
		}else{
			inciso.setSwsitdec("N");
		}
		/*	
		if(isDebugEnabled){
		 	log.debug("clave-------->"+claveCatalogo);
			log.debug("descripcion-->"+descripcionCatalogo);
			log.debug("subinciso---->"+subIncisos);
		}
		*/
		inciso.setCdtipsit(claveCatalogo);
		inciso.setDstipsit(descripcionCatalogo);
		inciso.setSwsitdec(subIncisos);
		try{
			WrapperResultados res = incisoManager.agregarInciso(inciso);
			messageResult = res.getMsgText();
		}catch(ApplicationException ae ){
			messageResult = ae.getMessage();
			success=false;
			log.debug("messageResult= " + messageResult);
		}
		success=true;
		
		return SUCCESS;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String eliminarInciso() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("Entro a eliminar un inciso de la lista");
		}
		IncisoVO inciso = new IncisoVO();
		if(session.containsKey("CODIGO_NIVEL0")){
			codigoRamo=(String) session.get("CODIGO_NIVEL0");
		}
		if(session.containsKey("CODIGO_NIVEL2")) {
			codigoTipo=(String) session.get("CODIGO_NIVEL2");
		}
		
		if(isDebugEnabled){
			log.debug("codigoRamo---->"+codigoRamo);
			log.debug("codigoTipo---->"+codigoTipo);
		}
		inciso.setCdramo(codigoRamo);
		inciso.setCdtipsit(codigoTipo);
		
		incisoManager.eliminarInciso(inciso);
		success=true;
		if(success){
			List<RamaVO> temporalTree=(List<RamaVO>) session.get("ARBOL_PRODUCTOS");
			session.put("ARBOL_PRODUCTOS_RECARGAR", temporalTree);
			session.remove("ARBOL_PRODUCTOS");
		}
		
		return SUCCESS;
	}
	
	/**
	 * Metodo <code>asociarIncisoSeleccionado</code> que es llamado desde Struts para
	 * asociar un nuevo inciso en un producto.
	 * 
	 * @return SUCCESS
	 * @throws Exception
	 */
	//*NOTA: Retorna un valor number de la bd para el MSG_ID pero en este momento no se esta manipulando
	public String asociarIncisoSeleccionado() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("Entro a asociar un inciso a la lista");
		}
		
		
		/**
		 * EL NUMERO DEL INCISOS EN ESTE CASO SIEMPRE ES 1
		 * */
		numeroInciso = "1";
		
		if(session.containsKey("CODIGO_NIVEL0"))
			codigoRamo=(String) session.get("CODIGO_NIVEL0");
		else
		codigoRamo="1";
		
		if(isDebugEnabled){
			log.debug("codigoRamo"+codigoRamo);
		}
		
		IncisoVO inciso = new IncisoVO();
		if(obligatorio == null){
			obligatorio = "N";			
		}else if(obligatorio.equals("on")){
			obligatorio = "S";
		}
		if(inserta == null){
			inserta = "N";			
		}else if(inserta.equals("on")){
			inserta = "S";
		}
		inciso.setCdtipsit(clave);
		inciso.setSwobliga(obligatorio);
		inciso.setSwinsert(inserta);
		
		inciso.setCdagrupa(agrupador);
		inciso.setNmsituac(numeroInciso);
		inciso.setCdramo(codigoRamo);
		
		inciso.setDsriesgo(descripcion);
		
		if(isDebugEnabled){
			log.debug("clave="+clave);
			log.debug("obligatorio="+obligatorio);
			log.debug("inserta="+inserta);
			log.debug("agrupador="+agrupador);
			log.debug("numeroInciso="+numeroInciso);
			log.debug("codigoRamo="+codigoRamo);
		}
		
		incisoManager.asociarInciso(inciso);
		/*
		if(isDebugEnabled){
			log.debug("CLAVE ENVIADA---------->"+clave);
			log.debug("OBLIGATORIO ENVIADA---------->"+inciso.getSwobliga());
			log.debug("AGRUPADOR ENVIADA---------->"+agrupador);
			log.debug("NUM INCISO ENVIADA---------->"+numeroInciso);
			log.debug("COD RAMO ENVIADA---------->"+codigoRamo);
			log.debug("INSERTA ENVIADA---------->"+inciso.getSwinsert());
			log.debug("obli ENVIADA---------->"+obligatorio);
			log.debug("inser ENVIADA---------->"+inserta);
		}
		*/
		success=true;
		if(success){
			List<RamaVO> temporalTree=(List<RamaVO>) session.get("ARBOL_PRODUCTOS");
			session.put("ARBOL_PRODUCTOS_RECARGAR", temporalTree);
			session.remove("ARBOL_PRODUCTOS");
		}
		return SUCCESS;
	}


	
	//Getters and Setters
	

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<IncisoVO> getIncisos() {
		return incisos;
	}

	public void setIncisos(List<IncisoVO> incisos) {
		this.incisos = incisos;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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

	public String getAgrupador() {
		return agrupador;
	}

	public void setAgrupador(String agrupador) {
		this.agrupador = agrupador;
	}

	public String getNumeroInciso() {
		return numeroInciso;
	}

	public void setNumeroInciso(String numeroInciso) {
		this.numeroInciso = numeroInciso;
	}

	public String getDescripcionCatalogo() {
		return descripcionCatalogo;
	}

	public void setDescripcionCatalogo(String descripcionCatalogo) {
		this.descripcionCatalogo = descripcionCatalogo;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public void setIncisoManager(IncisoManager incisoManager) {
		if(log.isDebugEnabled()){
			log.debug("MANAGERSET"+incisoManager);
		}
		this.incisoManager = incisoManager;
	}

	public String getClaveCatalogo() {
		return claveCatalogo;
	}

	public void setClaveCatalogo(String claveCatalogo) {
		this.claveCatalogo = claveCatalogo;
	}

	public String getCodigoRamo() {
		return codigoRamo;
	}

	public void setCodigoRamo(String codigoRamo) {
		this.codigoRamo = codigoRamo;
	}

	public String getSubIncisos() {
		return subIncisos;
	}

	public void setSubIncisos(String subIncisos) {
		this.subIncisos = subIncisos;
	}


	public String getCodigoTipo() {
		return codigoTipo;
	}

	public void setCodigoTipo(String codigoTipo) {
		this.codigoTipo = codigoTipo;
	}

	/**
	 * @return the editarInciso
	 */
	public String getEditarInciso() {
		return editarInciso;
	}

	/**
	 * @param editarInciso the editarInciso to set
	 */
	public void setEditarInciso(String editarInciso) {
		this.editarInciso = editarInciso;
	}

	public String getMessageResult() {
		return messageResult;
	}

	public void setMessageResult(String messageResult) {
		this.messageResult = messageResult;
	}
	
}
