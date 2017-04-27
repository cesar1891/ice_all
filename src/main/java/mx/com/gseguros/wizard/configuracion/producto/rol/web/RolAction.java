package mx.com.gseguros.wizard.configuracion.producto.rol.web;

import java.util.ArrayList;
import java.util.List;

import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.RamaVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.rol.model.RolAtributoVariableVO;
import mx.com.gseguros.wizard.configuracion.producto.rol.model.RolVO;
import mx.com.gseguros.wizard.configuracion.producto.service.RolManager;
import mx.com.gseguros.wizard.configuracion.producto.web.Padre;
import mx.com.gseguros.wizard.model.MensajesVO;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 
 * @author Edgar Perez
 * @version 2.0
 * @since 1.0
 * 
 * Clase que contiene los metodos de cargar, agregar y editar el rol de un producto 
 *
 */
public class RolAction extends Padre {

	private static final long serialVersionUID = 3480546700588201196L;
	private static final transient Log log = LogFactory.getLog(RolAction.class);
	
	/**
	 * Manager con implementacion de Endpoint para la consulta a BD.
	 */
	private RolManager rolManager;
	
	/**
	 * Atributo de respuesta interpretado por strust como la correcta
	 * realizacion de. cada peticion.
	 */
	private boolean success;
	
	/**
	 * Atributo de respuesta interpretado por strust con la lista de atributos variables tipo
	 * RolAtributoVariableVO con los valores de la consulta.
	 */
	private List<RolAtributoVariableVO> listaAtributoVariable;
	
	/**
	 * Atributo de respuesta interpretado por strust con el catalogo de roles tipo
	 * List<LlaveValorVO> con los valores de la consulta.
	 */
	private List<LlaveValorVO> catalogoRoles;
	
	/**
	 * Atributo de respuesta interpretado por strust con el catalogo de obligatoriedad tipo
	 * List<LlaveValorVO> con los valores de la consulta.
	 */
	private List<LlaveValorVO> catalogoObligatorio;
	
	/**
	 * Atributo de respuesta interpretado por strust con el catalogo de atributos variables tipo
	 * List<LlaveValorVO> con los valores de la consulta.
	 */
	private List<LlaveValorVO> catalogoAtributosVariables;
	
	/**
	 * Atributo agregado por struts que contiene el codigo de ramo para el rol.
	 */
	private String codigoRamo;
	
	/**
	 * Atributo agregado por struts que contiene el codigo de tipo de situacion para el rol.
	 */
	private String codigoTipoSituacion;
	
	/**
	 * Atributo agregado por struts que contiene el codigo de rol.
	 */
	private String codigoRol;
	
	/**
	 * Atributo agregado por struts que contiene el codigo de nivel para el rol.
	 */
	private int codigoNivel;
	
	/**
	 * Atributo agregado por struts que contiene el codigo del atributo variable del rol.
	 */
	private String codigoAtributoVariable;
	
	/**
	 * Atributo agregado por struts que contiene la descripcion del atributo variable para el rol.
	 */
	private String descripcionAtributoVariable;
	
	/**
	 * Atributo agregado por struts que contiene la clave del rol.
	 */
	private String clave;
	
	/**
	 * Atributo agregado por struts que contiene la descripcion del rol.
	 */
	private String descripcion;
	
	/**
	 * Atributo agregado por struts que contiene el codigo de la lista de valores para el rol.
	 */
	private String codigoListaDeValores;
	
	/**
	 * Atributo agregado por struts que contiene la descripcion de la lista de valores para el rol.
	 */
	private String descripcionListaDeValores;
	
	/**
	 * Atributo agregado por struts que contiene el codigo para el saber si agrega o edita un rol.
	 */
	private int edita;
	
	/**
	 * Atributo agregado por struts que contiene la obligatoriedad del rol.
	 */
	private String obligatorio;
	
	/**
	 * Atributo agregado por struts que contiene el codigo de la composicion del rol.
	 */
	private String codigoComposicion;
	
	/**
	 * Atributo agregado por struts que contiene el numero maximo del rol.
	 */
	private String numeroMaximo;
	
	/**
	 * Atributo agregado por struts que contiene el domicilio del rol.
	 */
	private String domicilio;
	
	/**
	 * Atributo agregado por struts que contiene el formato para el rol.
	 */
	private String formato;
	
	/**
	 * Atributo agregado por struts que contiene el limite maximo del rol
	 */
	private String limiteMaximo;
	
	/**
	 * Atributo agregado por struts que contiene el limite minimo del rol.
	 */
	private String limiteMinimo;
	
	/**
	 * Atributo agregado por struts que contiene la lista del rol asociado de tipo List<RolVO>
	 */
	private List<RolVO> listaRolAsociado;
	
	/**
	 * Atributo agregado por struts que indica si aparece el cotizador del rol.
	 */
	private String apareceCotizador;

	/**
	 * Atributo agregado por struts que indica si el cotizador del rol es modificable.
	 */
	private String modificaCotizador;

	/**
	 * Atributo agregado por struts que contiene Dato complementario  del rol.
	 */
	private String datoComplementario;
	
	/**
	 * Atributo agregado por struts que indica Obligatoriedad para el dato complementario del rol.
	 */
	private String obligatorioComplementario;
	
	/**
	 * Atributo agregado por struts que indica si si es modificable el dato complementario del rol.
	 */
	private String modificableComplementario;
	
	/**
	 * Atributo agregado por struts que indica si aparece el endoso del rol.
	 */
	private String apareceEndoso;
	
	/**
	 * Atributo agregado por struts que contiene la Obligatoriedad para el endoso del rol.
	 */
	private String obligatorioEndoso;
	
	/**
	 * Atributo agregado por struts que indica se el endoso del rol es modificable.
	 */
	private String modificaEndoso;
	
	/**
	 * Atributo agregado por struts que contiene el codigo de la expresion asociada al atributo del rol.
	 */
	private String codigoExpresion;
	
	/**
	 * Atributo que casa a la expresion con su pantalla padre.
	 */
	private String codigoExpresionSession;
	
	/**
	 * Atributo agregado por struts que indica si el atributo variable es
	 * retarificado.
	 */
	private String retarificacion;
	
	private String condicion;
	private String padre;
	private String orden;
	private String agrupador;
	
	/**
	 * Almacena mensajes de respuesta de algun proceso invocado para mostrarlos en pantalla
	 */
	private String mensajeRespuesta;
	
	
	/**
	 * Metodo <code>execute</code> con el que es llamado desde Struts para
	 * atender la petici�n web.
	 * 
	 * @return INPUT
	 * @throws Exception
	 */
	public String execute() throws Exception {
		if(log.isDebugEnabled()){
			log.debug("-------------->Entro a RolAction");
		}
		return INPUT;
	}
	
	/**
     * Metodo <code>listaAtributosVariablesJSON</code> con el que es cargada la lista de 
     * atributos variables del rol.
     * 
     * @return success
     * @throws Exception
     */
	public String listaAtributosVariablesJSON() throws Exception{
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("entro a lista de atributos variables json");
			log.debug("codigoRol="+codigoRol);
			log.debug("session.containsKey('CODIGO_ROL')="+session.containsKey("CODIGO_ROL"));
			log.debug("session.get('CODIGO_ROL')="+session.get("CODIGO_ROL"));
		}
		listaAtributoVariable = null;
		String codigoRolSession =null;
		if(session.containsKey("CODIGO_NIVEL0")){
			codigoRamo=(String) session.get("CODIGO_NIVEL0");
			if(isDebugEnabled){
				log.debug("CODIGORAMO--"+codigoRamo);
			}
			codigoNivel=0;
			codigoTipoSituacion="NA";
		}
		if(session.containsKey("CODIGO_NIVEL2")){
			codigoTipoSituacion=(String) session.get("CODIGO_NIVEL2");
			if(isDebugEnabled){
				log.debug("codigoTipoSituacion--"+codigoTipoSituacion);
			}
			codigoNivel=1;
		}
		
		if( (edita==1 && !session.containsKey("ATRIBUTOS_VARIABLES_ROL") )||
				( (!session.containsKey("CODIGO_ROL") && codigoRol!=null) || 
						( session.containsKey("CODIGO_ROL") && codigoRol!=null && !codigoRol.equals(session.get("CODIGO_ROL")) ) ) ){
			
			if(codigoRamo != null && codigoRol!=null){
				session.put("CODIGO_ROL", codigoRol);
				if(isDebugEnabled){
					log.debug("ROLL--"+codigoRol);
					log.debug("RAMO--"+codigoRamo);
					log.debug("TIPOSITUACION--"+codigoTipoSituacion);
					log.debug("CODIGONIVEL--"+codigoNivel);
				}
				listaAtributoVariable = rolManager.atributosVariablesJson(Integer.parseInt(codigoRamo), 
						codigoTipoSituacion,codigoRol,codigoNivel);
				if(listaAtributoVariable!=null && !listaAtributoVariable.isEmpty()){
					if(isDebugEnabled){
					for(RolAtributoVariableVO ravvo:listaAtributoVariable){
						log.debug("codigo rol atributos"+ravvo.getCodigoRol());
						if(StringUtils.isBlank(ravvo.getRetarificacion()))ravvo.setRetarificacion("N");
					}
					}
				}
			}
		}else if(session.containsKey("ATRIBUTOS_VARIABLES_ROL") ){
			if(isDebugEnabled){
				log.debug("entro al if de cargar atributos variables rol de session");
			}
			listaAtributoVariable = (List<RolAtributoVariableVO>) session.get("ATRIBUTOS_VARIABLES_ROL");
			log.debug("LISTA ATRIBUTOS ROLES TAMANO: " + listaAtributoVariable.size());
			//log.debug("LISTA ATRIBUTOS ROLES ATRIBUTO: " + listaAtributoVariable.get(0).getCodigoAtributoVariable());
		}
		if(listaAtributoVariable==null){
			listaAtributoVariable = new ArrayList<RolAtributoVariableVO>();
		}
		session.put("ATRIBUTOS_VARIABLES_ROL", listaAtributoVariable);
		success = true;
		return SUCCESS;
		
		
	}
	
	/**
     * Metodo <code>catalogoRoles</code> con el que es cargado el catalogo de roles.
     * 
     * @return success
     * @throws Exception
     */
	public String catalogoRoles() throws Exception{
		if(log.isDebugEnabled()){
			log.debug("Entro a catalogo Roles json");
		}
		catalogoRoles = null;
		if (!session.containsKey("CATALOGO_ROLEs")) {
				catalogoRoles = rolManager.catalogoRolesJson();
				session.put("CATALOGO_ROLES", catalogoRoles);
				
		} else {
			catalogoRoles = (List<LlaveValorVO>) session.get("CATALOGO_ROLES");
		}
		success = true;
		return SUCCESS;
	}
	
	/**
     * Metodo <code>catalogoObligatorioJSON</code> con el que es cargado el catalogo de obligatoriedad
     * para los roles.
     * 
     * @return success
     * @throws Exception
     */
	public String catalogoObligatorioJSON() throws Exception{
		if(log.isDebugEnabled()){
			log.debug("Entro a catalogo obligatorios json");
		}
		catalogoObligatorio = null;
		
		if (!session.containsKey("CATALOGO_OBLIGATORIO")) {
				catalogoObligatorio = rolManager.catalogoObligatorioJson();
			session.put("CATALOGO_OBLIGATORIO", catalogoObligatorio);
		} else {
			catalogoObligatorio = (List<LlaveValorVO>) session.get("CATALOGO_OBLIGATORIO");
		}
		success = true;
		return SUCCESS;
	}
	
	/**
     * Metodo <code>catalogoAtributosVariablesJson</code> con el que es cargado el catalogo de los
     * atributos variables para el rol.
     * 
     * @return success
     * @throws Exception
     */
	public String catalogoAtributosVariablesJson() throws Exception{
		if(log.isDebugEnabled()){
			log.debug("Entro a catalogo de atributos variables json");
		}
		
				catalogoAtributosVariables = rolManager.catalogoAtributosVariablesJson();
	
		success = true;
		return SUCCESS;
	}
	
	/**
     * Metodo <code>agregaRolCatalogo</code> con el que es agregado un rol al catalogo de roles
     * 
     * @return success
     * @throws Exception
     */
	public String agregaRolCatalogo() throws Exception{
		if(log.isDebugEnabled()){
			log.debug("Entro a agregar un rol al catalogo");
		}
		LlaveValorVO rol = new LlaveValorVO();
		rol.setKey(null);
		rol.setValue(descripcion);
		rolManager.agregaRolCatalogo(rol);
		success=true;
		
		return SUCCESS;
		
	}
	
	/**
     * Metodo <code>agregarAtributoVariableRol</code> con el que es agregado o editado 
     * un atributo variable al rol.
     * 
     * @return success
     * @throws Exception
     */
	public String agregarAtributoVariableRol() throws Exception{
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("El valor de retarificacion es:  "+ retarificacion);
			log.debug("Entro a agregar atributo variable");
		}
		
		if(session.containsKey("CODIGO_NIVEL0")){
			codigoRamo=(String) session.get("CODIGO_NIVEL0");
			if(isDebugEnabled){
				log.debug("CODIGORAMO-->"+codigoRamo);
			}
			codigoNivel=0;
			codigoTipoSituacion="NA";
		}
		if(session.containsKey("CODIGO_NIVEL2")){
			codigoTipoSituacion=(String) session.get("CODIGO_NIVEL2");
			if(isDebugEnabled){
				log.debug("CODIGOTIPOSITUACION-->"+codigoTipoSituacion);
			}
			codigoNivel=1;
		}
		
		if(session.containsKey("CODIGO_ROL")){
			codigoRol=(String) session.get("CODIGO_ROL");
			
			if(isDebugEnabled){
				log.debug("CODIGOROL-->"+codigoRol);
			}
		}
		
		if(isDebugEnabled){
			log.debug("CODIGOROL-->"+codigoRol);
		}
		
		if(codigoRamo != null && codigoRol != null){
			
			if(obligatorio == null){
				obligatorio="N";			
			}if(apareceCotizador == null){
				apareceCotizador="N";			
			}if(modificaCotizador == null){
				modificaCotizador="N";			
			}if(datoComplementario == null){
				datoComplementario="N";			
			}if(obligatorioComplementario == null){
				obligatorioComplementario="N";			
			}if(modificableComplementario == null){
				modificableComplementario="N";			
			}if(apareceEndoso == null){
				apareceEndoso="N";			
			}if(obligatorioEndoso == null){
				obligatorioEndoso="N";			
			}if(modificaEndoso == null){
				modificaEndoso="N";			
			}if(retarificacion == null ){
				retarificacion="N";
			}
			
			if(descripcionListaDeValores.equals("Seleccione un valor")){
				descripcionListaDeValores=null;
			}
			if(session.containsKey(codigoExpresionSession))
				codigoExpresion = (String) session.get(codigoExpresionSession);
			else
				codigoExpresion = "0";
			/*if(isDebugEnabled){
				log.debug("RAMO-->"+codigoRamo);
				log.debug("TIPOSITUACION-->"+codigoTipoSituacion);
				log.debug("EDITA-->"+edita);
				log.debug("CODATRIBUTOVARIBLE-->"+codigoAtributoVariable);
				log.debug("OBLIGATORIO-->"+obligatorio);
				log.debug("DESCRIPCIONLISTAVALOR"+descripcionListaDeValores);
				log.debug("CODIGOLISTAVALOR"+codigoListaDeValores);
				log.debug("CHECK OBLIGAENDOSO"+obligatorioEndoso);
				log.debug("CHECK MODIFICAENDOSO"+modificaEndoso);
			}*/
			
			ArrayList<RolAtributoVariableVO> temporal = (ArrayList<RolAtributoVariableVO>) session.get("ATRIBUTOS_VARIABLES_ROL");
			RolAtributoVariableVO listaGrid = null;
			
			if(edita==0){
				if(descripcionAtributoVariable!=null && StringUtils.isNotBlank(descripcionAtributoVariable)){						
					listaGrid = new RolAtributoVariableVO();
					listaGrid.setCdRamo(Integer.parseInt(codigoRamo));
					listaGrid.setCodigoNivel(String.valueOf(codigoNivel));
					listaGrid.setCodigoRol(codigoRol);
					listaGrid.setCodigoTipsit(codigoTipoSituacion);
					listaGrid.setDescripcionAtributoVariable(descripcionAtributoVariable);
					listaGrid.setDescripcionListaDeValores(descripcionListaDeValores);
					listaGrid.setSwitchObligatorio(obligatorio);
					listaGrid.setCodigoAtributoVariable(codigoAtributoVariable);
					listaGrid.setCodigoListaDeValores(codigoListaDeValores);
					listaGrid.setApareceCotizador(apareceCotizador);
					listaGrid.setModificaCotizador(modificaCotizador);
					listaGrid.setDatoComplementario(datoComplementario);
					listaGrid.setObligatorioComplementario(obligatorioComplementario);
					listaGrid.setModificableComplementario(modificableComplementario);
					listaGrid.setApareceEndoso(apareceEndoso);
					listaGrid.setObligatorioEndoso(obligatorioEndoso);
					listaGrid.setModificaEndoso(modificaEndoso);
					listaGrid.setCodigoExpresion(codigoExpresion);
					listaGrid.setCodigoPadre(padre);
					listaGrid.setCodigoCondicion(condicion);
					listaGrid.setAgrupador(agrupador);
					listaGrid.setOrden(orden);
					listaGrid.setRetarificacion(retarificacion);
					temporal.add(listaGrid);					
				}
			}
			if(edita==1){
				for (RolAtributoVariableVO  temp: temporal){
					//log.debug("OTTABVAL--de la base "+temp.getOttabval());
					//log.debug("CODIGOlista---que se envia de jsp "+temp.getCodigoListaDeValores());
					/*if(isDebugEnabled){
						log.debug("CODIGOENVIADO--- "+codigoAtributoVariable);
						log.debug("EDITA-->"+edita);
						log.debug("TEMPCODIGOATRIBUTO--- "+temp.getCodigoAtributoVariable());
						log.debug("DESCRIPCIONENVIADO--- "+descripcionAtributoVariable);
						log.debug("TEMPDESCRIPCIONATRIBUTO--- "+temp.getDescripcionAtributoVariable());
					}*/
					if(temp.getSwitchObligatorio()== null || StringUtils.isBlank(temp.getSwitchObligatorio())){
						temp.setSwitchObligatorio("N");			
					}if(temp.getApareceCotizador()== null || StringUtils.isBlank(temp.getApareceCotizador())){
						temp.setApareceCotizador("N");			
					}if(temp.getModificaCotizador() == null || StringUtils.isBlank(temp.getModificaCotizador())){
						temp.setModificaCotizador("N");			
					}if(temp.getDatoComplementario() == null || StringUtils.isBlank(temp.getDatoComplementario())){
						temp.setDatoComplementario("N");			
					}if(temp.getObligatorioComplementario() == null || StringUtils.isBlank(temp.getObligatorioComplementario())){
						temp.setObligatorioComplementario("N");			
					}if(temp.getModificableComplementario() == null || StringUtils.isBlank(temp.getModificableComplementario())){
						temp.setModificableComplementario("N");			
					}if(temp.getApareceEndoso() == null || StringUtils.isBlank(temp.getApareceEndoso())){
						temp.setApareceEndoso("N");			
					}if(temp.getObligatorioEndoso() == null || StringUtils.isBlank(temp.getObligatorioEndoso())){
						temp.setObligatorioEndoso("N");			
					}if(temp.getModificaEndoso() == null || StringUtils.isBlank(temp.getModificaEndoso())){
						temp.setModificaEndoso("N");			
					}if(temp.getRetarificacion() == null || StringUtils.isBlank(temp.getRetarificacion())){
						temp.setRetarificacion("N");
					}
					
					temp.setCdRamo(Integer.parseInt(codigoRamo));
					temp.setCodigoNivel(String.valueOf(codigoNivel));
					temp.setCodigoRol(codigoRol);
					temp.setCodigoTipsit(codigoTipoSituacion);										
					temp.setCodigoListaDeValores(temp.getOttabval());
					//log.debug("OTTABVAL--- seteado al codigo de lista de valores "+temp.getOttabval());
					
						
					
					if(temp.getCodigoAtributoVariable().equals(codigoAtributoVariable)){
						
							temp.setCodigoListaDeValores(codigoListaDeValores);	
							//log.debug("CODIGOlista--- seteado al codigo de lista de valores "+temp.getCodigoListaDeValores());							
							temp.setCdRamo(Integer.parseInt(codigoRamo));
							temp.setCodigoNivel(String.valueOf(codigoNivel));
							temp.setCodigoRol(codigoRol);
							temp.setCodigoTipsit(codigoTipoSituacion);
							temp.setDescripcionListaDeValores(descripcionListaDeValores);
							temp.setSwitchObligatorio(obligatorio);
							temp.setApareceCotizador(apareceCotizador);
							temp.setModificaCotizador(modificaCotizador);
							temp.setDatoComplementario(datoComplementario);
							temp.setObligatorioComplementario(obligatorioComplementario);
							temp.setModificableComplementario(modificableComplementario);
							temp.setApareceEndoso(apareceEndoso);
							temp.setObligatorioEndoso(obligatorioEndoso);
							temp.setModificaEndoso(modificaEndoso);								
							temp.setCodigoExpresion(codigoExpresion);
							temp.setCodigoPadre(padre);
							temp.setCodigoCondicion(condicion);
							temp.setAgrupador(agrupador);
							temp.setOrden(orden);
							temp.setRetarificacion(retarificacion);
							
					}
				}			
			}
			session.put("ATRIBUTOS_VARIABLES_ROL", temporal);
			success = true;		
			return SUCCESS;
			
			
		}
		
		success = true;
		return SUCCESS;
	}
	
	/**
	 * Valida si el atributo variable del rol tiene hijos asociados
	 * @return SUCCESS
	 * @throws Exception
	 */
	public String validaHijosAtributoVariableRol() throws Exception {
		
		if (session.containsKey("CODIGO_NIVEL0")) {
			codigoRamo = (String) session.get("CODIGO_NIVEL0");
		}
		RolAtributoVariableVO atributoRol = new RolAtributoVariableVO();
		atributoRol.setCdRamo(Integer.parseInt(codigoRamo));
		atributoRol.setCodigoRol(codigoRol);
		atributoRol.setCodigoAtributoVariable(codigoAtributoVariable);
		
		if(rolManager.tieneHijosAtributoVariableRol(atributoRol)){
			mensajeRespuesta = Constantes.MESSAGE_ATRIBUTO_CON_HIJOS_ASOCIADOS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
     * Metodo <code>eliminarAtributoVariableRol</code> con el que son eliminados los valores
     * manualmente de la lista de valores del producto en el grid.
     * 
     * @return success
     * @throws Exception
     */
    public String eliminarAtributoVariableRol() throws Exception {
    	
    	boolean isDebugEnabled = log.isDebugEnabled();
    	if(isDebugEnabled){
    		log.debug("Entro a eliminar de la lista del grid.  codigoAtributoVariable=" + codigoAtributoVariable);
    	}
        if(codigoAtributoVariable != null){
        	ArrayList<RolAtributoVariableVO> temporal = (ArrayList<RolAtributoVariableVO>) session.get("ATRIBUTOS_VARIABLES_ROL");
            
        	//OBTENER DATOS DEL NIVEL DEL WIZARD EN EL QUE ESTAMOS
            if(session.containsKey("CODIGO_NIVEL0")){
    			codigoRamo=(String) session.get("CODIGO_NIVEL0");
    			if(isDebugEnabled){
    				log.debug("CODIGORAMO-->"+codigoRamo);
    			}
    			codigoNivel=0;
    			codigoTipoSituacion="NA";
    		}
    		if(session.containsKey("CODIGO_NIVEL2")){
    			codigoTipoSituacion=(String) session.get("CODIGO_NIVEL2");
    			if(isDebugEnabled){
    				log.debug("CODIGOTIPOSITUACION-->"+codigoTipoSituacion);
    			}
    			codigoNivel=1;
    		}
    		if(session.containsKey("CODIGO_ROL")){
    			codigoRol=(String) session.get("CODIGO_ROL");
    			if(isDebugEnabled){
    				log.debug("CODIGOROL-->"+codigoRol);
    			}
    		}
    		
    		//ELIMINAR EN BD
    		RolAtributoVariableVO atributoRol = new RolAtributoVariableVO();
    		atributoRol.setCdRamo(Integer.parseInt(codigoRamo));
    		atributoRol.setCodigoRol(codigoRol);
    		atributoRol.setCodigoAtributoVariable(codigoAtributoVariable);
    		MensajesVO mensajeVO;
    		mensajeVO = rolManager.eliminaAtributoVariableRol(atributoRol);
    		log.debug("MSG_ID:" + mensajeVO.getMsgId());
    		
    		//ELIMINAR DE SESION
    		//Si se borr� en BD  �  no se borro porque el registro a�n no existe en BD(solo estaba en sesi�n) ser� success:
    		if(mensajeVO.getMsgId().equals("200012") || mensajeVO.getMsgId().equals("500")){
    			if(isDebugEnabled && mensajeVO.getMsgId().equals("200012")){
    				log.debug("Se elimin� Atributo Variable de Rol en BD y se va a borrar de sesi�n" );
    			}
    			if(isDebugEnabled && mensajeVO.getMsgId().equals("500")){
    				log.debug("No exist�a el Atributo Variable de Rol en BD, pero se va a borrar de sesion" );
    			}
    			
    			boolean valorLista = false;
                RolAtributoVariableVO remover = null;
                for (RolAtributoVariableVO  temp: temporal){
                	if(isDebugEnabled){
                		log.debug("SESIONCODIGOATRIBUTO="+temp.getCodigoAtributoVariable());
                	}
                    if(temp.getCodigoAtributoVariable().equals(codigoAtributoVariable)){
                        remover = temp;
                        valorLista = true;
                    }
                }
                if(remover!=null){
                    temporal.remove(remover);
                    session.put("ATRIBUTOS_VARIABLES_ROL", temporal);
                    success = true;      
                }
                if(valorLista){
                	session.put("ATRIBUTOS_VARIABLES_ROL", temporal);
                	success = true;
                }
    		}else{
    			success = false;
    		}
        }
        
        return SUCCESS;
    }
	
    /**
     * Metodo <code>insertaRolInciso</code> con el que son insertados los valores
     * del rol al inciso.
     * 
     * @return success
     * @throws Exception
     */
    public String insertaRolInciso() throws Exception {
      boolean isDebugEnabled = log.isDebugEnabled();
      if(isDebugEnabled){
    	  log.debug("Entro a insertar rol");
      }
      if(session.containsKey("CODIGO_NIVEL0")){
			codigoRamo=(String) session.get("CODIGO_NIVEL0");
			if(isDebugEnabled){
				log.debug("CODIGORAMO-->"+codigoRamo);
			}
			codigoNivel=0;
			codigoTipoSituacion="NA";
		}
    	
      if(session.containsKey("CODIGO_NIVEL2")){
			codigoTipoSituacion=(String) session.get("CODIGO_NIVEL2");
			if(isDebugEnabled){
				log.debug("CODIGOTIPOSITUACION-->"+codigoTipoSituacion);
			}
			codigoNivel=1;
		}
		
		
      if(session.containsKey("CODIGO_ROL")){
			codigoRol=(String) session.get("CODIGO_ROL");
			/*
			//para prueba
			codigoRamo ="900";
			codigoTipoSituacion="NA";
			codigoNivel=0;
			*/
			if(isDebugEnabled){
				log.debug("CODIGOROL-->"+codigoRol);
			}
		}
      if(codigoRamo != null && codigoRol != null && codigoTipoSituacion != null && 
    		  session.containsKey("ATRIBUTOS_VARIABLES_ROL")){
    	  if(domicilio == null){
    		  domicilio="N";			
			}
    	  if(numeroMaximo == null){
    		  numeroMaximo="1";			
			} else { 
				numeroMaximo = "2";
			}
    	  //inserta cabecera del rol
    	  RolVO rol = new RolVO();
    	  rol.setCdtipsit(codigoTipoSituacion);
    	  rol.setCodigoComposicion(codigoComposicion);
    	  rol.setCodigoNivel(String.valueOf(codigoNivel));
    	  rol.setCodigoRamo(codigoRamo);
    	  rol.setNumeroMaximo(numeroMaximo);
    	  rol.setSwitchDomicilio(domicilio);
    	  rol.setCodigoRol(codigoRol);
    	  
    	  if(isDebugEnabled){
    		  log.debug("CDTIPSIT**"+codigoTipoSituacion);
    		  log.debug("COMPOSICION**"+codigoComposicion);
    		  log.debug("NIVEL**"+codigoNivel);
    		  log.debug("RAMO**"+codigoRamo);
    		  log.debug("NUMMAXIMO**"+numeroMaximo);
    		  log.debug("DOMICILIO**"+domicilio);
    	  }
    	  rolManager.agregarRolInciso(rol);
    	  
    	  //inserta atributos variables del rol
    	  List<RolAtributoVariableVO> AtributosRol = (ArrayList<RolAtributoVariableVO>) session.get("ATRIBUTOS_VARIABLES_ROL");
    	  for(RolAtributoVariableVO atribu: AtributosRol){
    		  if(isDebugEnabled){
    		  	log.debug("RAMOLISTA//"+atribu.getCdRamo());
    		  	log.debug("NIVELLISTA//"+atribu.getCodigoNivel());
    		  	log.debug("ROLLISTA//"+atribu.getCodigoRol());
    		  	log.debug("CODATRIBUVARIABLELISTA//"+atribu.getCodigoAtributoVariable());
    		  	log.debug("CODVALORESLISTA//"+atribu.getCodigoListaDeValores());
    		  	log.debug("OBLIGATORIOLISTA//"+atribu.getSwitchObligatorio());
    		  	log.debug("TIPSITLISTA//"+atribu.getCodigoTipsit());
    		  }
    		  	atribu.setCdRamo(Integer.parseInt(codigoRamo));
    		  	atribu.setCodigoNivel(String.valueOf(codigoNivel));
    		  	atribu.setCodigoRol(codigoRol);
    		  	atribu.setCodigoTipsit(codigoTipoSituacion);
        	  
    	  }
    	  rolManager.agregaAtributoVariableRol(AtributosRol);
    	
		success=true;
		 if(success){
		      List<RamaVO> temporalTree=(List<RamaVO>) session.get("ARBOL_PRODUCTOS");
		      session.put("ARBOL_PRODUCTOS_RECARGAR", temporalTree);
		      session.remove("ARBOL_PRODUCTOS");
		      }
		  
		return SUCCESS;
      }
      
      	success=false;
        return SUCCESS;
    }
    
    /**
     * Action para eliminar un Rol
     * @return
     * @throws Exception
     */
    public String eliminaRol() throws Exception {
    	
    	boolean isDebugEnabled = log.isDebugEnabled(); 
    	
    	//Se setean valores dependiendo el nivel actual
    	if(session.containsKey("CODIGO_NIVEL0")){
    		codigoRamo=(String) session.get("CODIGO_NIVEL0");
			if(isDebugEnabled){
				log.debug("CODIGORAMO-->"+codigoRamo);
			}
			codigoNivel=0;
			codigoTipoSituacion="NA";
    	}
    	if(session.containsKey("CODIGO_NIVEL2")){
    		codigoTipoSituacion=(String) session.get("CODIGO_NIVEL2");
    		if(isDebugEnabled){
    			log.debug("CODIGOTIPOSITUACION-->"+codigoTipoSituacion);
    		}
    		codigoNivel=1;
    	}
    	
    	//Eliminar rol en la BD
    	WrapperResultados resultado = null;
    	resultado = rolManager.eliminaRol(Integer.parseInt(codigoRamo), codigoRol, codigoNivel);
    	mensajeRespuesta = resultado.getMsgText();
    	
    	//Se recargan los datos del �rbol para que tomen los cambios anteriores 
    	List<RamaVO> temporalTree=(List<RamaVO>) session.get("ARBOL_PRODUCTOS");
		session.put("ARBOL_PRODUCTOS_RECARGAR", temporalTree);
		session.remove("ARBOL_PRODUCTOS");
		
		success=true;
    	return SUCCESS;
    }
    
	
    /**
     * Metodo <code>insertaAtributoVariableCatalogo</code> con el que son agregados
     * los atributos variables al catalogo.
     * 
     * @return success
     * @throws Exception
     */
    public String insertaAtributoVariableCatalogo() throws Exception {
      if(log.isDebugEnabled()){
    	  log.debug("Entro a insertar atributo variable al catalogo");
    	  log.debug("descripcion"+descripcionAtributoVariable);
    	  log.debug("formato"+formato);
    	  log.debug("maximo"+limiteMaximo);
    	  log.debug("minimo"+limiteMinimo);
      }
      RolAtributoVariableVO atributo = new RolAtributoVariableVO();
      if(formato.equals("on")){
    	  atributo.setFormato("A");
      }else{
    	  atributo.setFormato(formato);
      }
      atributo.setDescripcionAtributoVariable(descripcionAtributoVariable);
      atributo.setMaximo(limiteMaximo);
      atributo.setMinimo(limiteMinimo);
      
      rolManager.agregarAtributoVariableCatalogo(atributo);
      success=true;
      
      return SUCCESS;
    }
    
    /**
     * Metodo <code>borraSesionAtributosVariablesRol</code> con el que es eliminada la sesion 
     * de carga manual en el grid.
     * 
     * @return success
     * @throws Exception
     */
    public String borraSesionAtributosVariablesRol() throws Exception{
    	if(log.isDebugEnabled()){
    		log.debug("borrando sesion");
    	}
    	if(session.containsKey("ATRIBUTOS_VARIABLES_ROL"))
    		session.remove("ATRIBUTOS_VARIABLES_ROL");
    	if(session.containsKey("CODIGO_ROL"))
    		session.remove("CODIGO_ROL");
    	success = true;
    	return SUCCESS;
    }
    
    /**
     * Metodo <code>EditaRolAsociadoInciso</code> con el que es editado el rol asociado al inciso.
     * 
     * @return success
     * @throws Exception
     */
    public String cargaRolAsociadoInciso() throws Exception{
    	boolean isDebugEnabled = log.isDebugEnabled();
    	if(isDebugEnabled){
    		log.debug("Entro a editar rol");
    	}
    	if(session.containsKey("CODIGO_NIVEL0")){
			codigoRamo=(String) session.get("CODIGO_NIVEL0");
			if(isDebugEnabled){
				log.debug("CODIGORAMO-->"+codigoRamo);
			}
			codigoNivel=0;
			codigoTipoSituacion="NA";
		}
    	
      if(session.containsKey("CODIGO_NIVEL2")){
			codigoTipoSituacion=(String) session.get("CODIGO_NIVEL2");
			if(isDebugEnabled){
				log.debug("CODIGOTIPOSITUACION-->"+codigoTipoSituacion);
			}
			codigoNivel=1;
		}
		
		
      if(session.containsKey("CODIGO_ROL")){
			codigoRol=(String) session.get("CODIGO_ROL");
			/*
			//para prueba
			codigoRamo ="900";
			codigoTipoSituacion="NA";
			codigoNivel=0;
			codigoRol="1";
			*/
			if(isDebugEnabled){
				log.debug("CODIGOROL-->"+codigoRol);
			}
		}
			
    	  List<RolVO> listaRol = new ArrayList<RolVO>();			
    	  RolVO rolAsociado = new RolVO();
  			
    	  if(codigoRamo != null && codigoRol != null && codigoTipoSituacion != null){
    		  
  			rolAsociado = rolManager.obtieneRolAsociado(Integer.parseInt(codigoRamo),codigoTipoSituacion,codigoRol,codigoNivel);			
  			
  			
  			if (rolAsociado != null) {
  				if (StringUtils.isNotBlank(rolAsociado.getCodigoComposicion())) {
  					if (rolAsociado.getCodigoComposicion().equals("O"))
  						rolAsociado.setDescripcionComposicion("OPCIONAL");
  					else if (rolAsociado.getCodigoComposicion().equals("P"))
  						rolAsociado.setDescripcionComposicion("PROHIBIDO");
  					else if (rolAsociado.getCodigoComposicion().equals("R"))
  						rolAsociado.setDescripcionComposicion("REQUERIDO");
  				}
  			}
  				
  			
  			listaAtributoVariable = rolManager.atributosVariablesJson(Integer.parseInt(codigoRamo), 
					codigoTipoSituacion,codigoRol,codigoNivel);
  			
  			if(isDebugEnabled){
  				log.debug("DESCRIPCIONROL*--"+rolAsociado.getDescripcionRol());
  				log.debug("MASDEUNO*--"+rolAsociado.getNumeroMaximo());
  				log.debug("DOMICILIO*--"+rolAsociado.getSwitchDomicilio());
  				log.debug("OBLIGATORIEDAD*--"+rolAsociado.getDescripcionComposicion());
  				log.debug("CODIGOOBLIGATORIEDAD*--"+rolAsociado.getCodigoComposicion());
  			}
  			
  			if(isDebugEnabled){
  				for(RolAtributoVariableVO atribu: listaAtributoVariable){
  					log.debug("DESCRIPCIONATRIBUTOVARIABLE*--"+atribu.getDescripcionAtributoVariable());
  					log.debug("DESCRIPCIONLISTAVALORES*--"+atribu.getDescripcionListaDeValores());
  					log.debug("OBLIGATORIO*--"+atribu.getSwitchObligatorio());
  				}
  			}
  			if(listaAtributoVariable==null){
  				listaAtributoVariable = new ArrayList<RolAtributoVariableVO>();
  			}
  			session.put("ATRIBUTOS_VARIABLES_ROL", listaAtributoVariable);
  			listaRol.add(rolAsociado);
  			listaRolAsociado =listaRol;		
      }
    	
    	
    	success = true;
    	return SUCCESS;
    }
    
    
    
	public void setRolManager(RolManager rolManager) {
		this.rolManager = rolManager;
	}
	public boolean isSuccess() {
		return success;
	}	
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the listaAtributoVariable
	 */
	public List<RolAtributoVariableVO> getListaAtributoVariable() {
		return listaAtributoVariable;
	}

	/**
	 * @param listaAtributoVariable the listaAtributoVariable to set
	 */
	public void setListaAtributoVariable(
			List<RolAtributoVariableVO> listaAtributoVariable) {
		this.listaAtributoVariable = listaAtributoVariable;
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

	/**
	 * @return the codigoRol
	 */
	public String getCodigoRol() {
		return codigoRol;
	}

	/**
	 * @param codigoRol the codigoRol to set
	 */
	public void setCodigoRol(String codigoRol) {
		this.codigoRol = codigoRol;
	}

	/**
	 * @return the codigoAtributoVariable
	 */
	public String getCodigoAtributoVariable() {
		return codigoAtributoVariable;
	}

	/**
	 * @param codigoAtributoVariable the codigoAtributoVariable to set
	 */
	public void setCodigoAtributoVariable(String codigoAtributoVariable) {
		this.codigoAtributoVariable = codigoAtributoVariable;
	}

	/**
	 * @return the catalogoRoles
	 */
	public List<LlaveValorVO> getCatalogoRoles() {
		return catalogoRoles;
	}

	/**
	 * @param catalogoRoles the catalogoRoles to set
	 */
	public void setCatalogoRoles(List<LlaveValorVO> catalogoRoles) {
		this.catalogoRoles = catalogoRoles;
	}

	/**
	 * @return the catalogoObligatorio
	 */
	public List<LlaveValorVO> getCatalogoObligatorio() {
		return catalogoObligatorio;
	}

	/**
	 * @param catalogoObligatorio the catalogoObligatorio to set
	 */
	public void setCatalogoObligatorio(List<LlaveValorVO> catalogoObligatorio) {
		this.catalogoObligatorio = catalogoObligatorio;
	}

	/**
	 * @return the catalogoAtributosVariables
	 */
	public List<LlaveValorVO> getCatalogoAtributosVariables() {
		return catalogoAtributosVariables;
	}

	/**
	 * @param catalogoAtributosVariables the catalogoAtributosVariables to set
	 */
	public void setCatalogoAtributosVariables(
			List<LlaveValorVO> catalogoAtributosVariables) {
		this.catalogoAtributosVariables = catalogoAtributosVariables;
	}

	/**
	 * @return the codigoNivel
	 */
	public int getCodigoNivel() {
		return codigoNivel;
	}

	/**
	 * @param codigoNivel the codigoNivel to set
	 */
	public void setCodigoNivel(int codigoNivel) {
		this.codigoNivel = codigoNivel;
	}

	public String getCodigoTipoSituacion() {
		return codigoTipoSituacion;
	}

	public void setCodigoTipoSituacion(String codigoTipoSituacion) {
		this.codigoTipoSituacion = codigoTipoSituacion;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
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

	public String getDescripcionAtributoVariable() {
		return descripcionAtributoVariable;
	}

	public void setDescripcionAtributoVariable(String descripcionAtributoVariable) {
		this.descripcionAtributoVariable = descripcionAtributoVariable;
	}

	

	public String getDescripcionListaDeValores() {
		return descripcionListaDeValores;
	}

	public void setDescripcionListaDeValores(String descripcionListaDeValores) {
		this.descripcionListaDeValores = descripcionListaDeValores;
	}

	public int getEdita() {
		return edita;
	}

	public void setEdita(int edita) {
		this.edita = edita;
	}

	public String getCodigoListaDeValores() {
		return codigoListaDeValores;
	}

	public void setCodigoListaDeValores(String codigoListaDeValores) {
		this.codigoListaDeValores = codigoListaDeValores;
	}

	public String getCodigoComposicion() {
		return codigoComposicion;
	}

	public void setCodigoComposicion(String codigoComposicion) {
		this.codigoComposicion = codigoComposicion;
	}

	public String getNumeroMaximo() {
		return numeroMaximo;
	}

	public void setNumeroMaximo(String numeroMaximo) {
		this.numeroMaximo = numeroMaximo;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public String getLimiteMaximo() {
		return limiteMaximo;
	}

	public void setLimiteMaximo(String limiteMaximo) {
		this.limiteMaximo = limiteMaximo;
	}

	public String getLimiteMinimo() {
		return limiteMinimo;
	}

	public void setLimiteMinimo(String limiteMinimo) {
		this.limiteMinimo = limiteMinimo;
	}

	public List<RolVO> getListaRolAsociado() {
		return listaRolAsociado;
	}

	public void setListaRolAsociado(List<RolVO> listaRolAsociado) {
		this.listaRolAsociado = listaRolAsociado;
	}

	public String getApareceCotizador() {
		return apareceCotizador;
	}

	public void setApareceCotizador(String apareceCotizador) {
		this.apareceCotizador = apareceCotizador;
	}

	public String getModificaCotizador() {
		return modificaCotizador;
	}

	public void setModificaCotizador(String modificaCotizador) {
		this.modificaCotizador = modificaCotizador;
	}

	public String getDatoComplementario() {
		return datoComplementario;
	}

	public void setDatoComplementario(String datoComplementario) {
		this.datoComplementario = datoComplementario;
	}

	public String getObligatorioComplementario() {
		return obligatorioComplementario;
	}

	public void setObligatorioComplementario(String obligatorioComplementario) {
		this.obligatorioComplementario = obligatorioComplementario;
	}

	public String getModificableComplementario() {
		return modificableComplementario;
	}

	public void setModificableComplementario(String modificableComplementario) {
		this.modificableComplementario = modificableComplementario;
	}

	public String getApareceEndoso() {
		return apareceEndoso;
	}

	public void setApareceEndoso(String apareceEndoso) {
		this.apareceEndoso = apareceEndoso;
	}

	public String getObligatorioEndoso() {
		return obligatorioEndoso;
	}

	public void setObligatorioEndoso(String obligatorioEndoso) {
		this.obligatorioEndoso = obligatorioEndoso;
	}

	public String getModificaEndoso() {
		return modificaEndoso;
	}

	public void setModificaEndoso(String modificaEndoso) {
		this.modificaEndoso = modificaEndoso;
	}

	public String getCodigoExpresion() {
		return codigoExpresion;
	}

	public void setCodigoExpresion(String codigoExpresion) {
		this.codigoExpresion = codigoExpresion;
	}

	public String getCodigoExpresionSession() {
		return codigoExpresionSession;
	}

	public void setCodigoExpresionSession(String codigoExpresionSession) {
		this.codigoExpresionSession = codigoExpresionSession;
	}

	public String getCondicion() {
		return condicion;
	}

	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}

	public String getPadre() {
		return padre;
	}

	public void setPadre(String padre) {
		this.padre = padre;
	}

	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}

	public String getAgrupador() {
		return agrupador;
	}

	public void setAgrupador(String agrupador) {
		this.agrupador = agrupador;
	}

	public String getRetarificacion() {
		return retarificacion;
	}

	public void setRetarificacion(String retarificacion) {
		this.retarificacion = retarificacion;
	}

	public String getMensajeRespuesta() {
		return mensajeRespuesta;
	}

	public void setMensajeRespuesta(String mensajeRespuesta) {
		this.mensajeRespuesta = mensajeRespuesta;
	}
	
}
