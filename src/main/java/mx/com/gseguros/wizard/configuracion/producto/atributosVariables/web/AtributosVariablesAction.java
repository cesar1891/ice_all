package mx.com.gseguros.wizard.configuracion.producto.atributosVariables.web;

import java.util.ArrayList;
import java.util.List;

import mx.com.aon.portal.service.PagedList;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.wizard.configuracion.producto.atributosVariables.model.AtributosVariablesVO;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.RamaVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.reglanegocio.model.ReglaNegocioVO;
import mx.com.gseguros.wizard.configuracion.producto.service.AtributosVariablesManager;
import mx.com.gseguros.wizard.configuracion.producto.web.ExpresionesPadre;
import mx.com.gseguros.wizard.model.MensajesVO;
import mx.com.gseguros.wizard.service.CatalogService;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 
 * @author Edgar Perez
 * @version 2.0
 * @since 1.0
 * 
 * Clase que contiene los metodos de cargar y agregar a la lista de atributos
 * variables del producto
 * 
 */
public class AtributosVariablesAction extends ExpresionesPadre {

	private static final long serialVersionUID = 3480546700588201196L;
	private static final transient Log log = LogFactory
			.getLog(AtributosVariablesAction.class);

	/**
	 * Manager con implementacion de Endpoint para la consulta a BD.
	 */
	private AtributosVariablesManager atributosVariablesManager;
	
	private CatalogService catalogManager;

	/**
	 * Atributo de respuesta interpretado por strust como la correcta
	 * realizacion de. cada peticion.
	 */
	private boolean success;

	/**
	 * Atributo de respuesta interpretado por strust con la lista de beans tipo
	 * AtributosVariablesVO con los valores de la consulta.
	 */
	private List<AtributosVariablesVO> valoresDeAtributos;
	/**
	 * Atributo agregado por struts que contiene el codigo de la lista de
	 * valores de un atributo variable.
	 */
	private String codigoTabla;

	/**
	 * Atributo agregado por struts que contiene la clave del campo asociado al
	 * atributo variable.
	 */
	private String claveCampo;

	/**
	 * Atributo agregado por struts que contiene la descripcion del atributo
	 * variable.
	 */
	private String descripcion;

	/**
	 * Atributo agregado por struts que contiene el formato del atributo
	 * variable.
	 */
	private String formato;

	/**
	 * Atributo agregado por struts que contiene la obligatoriedad del atributo
	 * variable.
	 */
	private String obligatorio;

	/**
	 * Atributo agregado por struts que indica si el atributo variable es
	 * modificado en emision.
	 */
	private String modificaEmision;
	
	private String atributoParaTodos;

	/**
	 * Atributo agregado por struts que indica si el atributo variable es
	 * modificado en endoso.
	 */
	private String modificaEndoso;

	/**
	 * Atributo agregado por struts que indica si el atributo variable es
	 * retarificado.
	 */
	private String retarificacion;

	/**
	 * Atributo agregado por struts que indica si el atributo variable despliega
	 * un cotizador.
	 */
	private String despliegaCotizador;

	/**
	 * Atributo agregado por struts que contiene el codigo del ramo del
	 * producto.
	 */
	private String codigoRamo;

	/**
	 * Atributo agregado por struts que contiene el codigo de situacion del
	 * producto.
	 */
	private String codigoSituacion;

	/**
	 * Atributo agregado por struts que contiene el codigo de situacion del
	 * producto.
	 */
	private String codigoCobertura;

	/**
	 * Atributo agregado por struts que contiene la especificacion maxima del
	 * formato para el atributo variable numerico o alfanumerico.
	 */
	private String maximo;

	/**
	 * Atributo agregado por struts que contiene la especificacion minima del
	 * formato para el atributo variable numerico o alfanumerico.
	 */
	private String minimo;

	/**
	 * Atributo de respuesta interpretado por strust con la lista de beans tipo
	 * AtributosVariablesVO con los valores de la consulta.
	 */
	private List<AtributosVariablesVO> listaAtributosVariables;

	/**
	 * Atributo agregado por struts que contiene la especificacion del nivel
	 * para el atributo variable.
	 */
	private int nivel;

	/**
	 * Atributo agregado por struts que contiene el codigo del atributo variable
	 */
	private String codigoAtributo;

	/**
	 * Atributo agregado por struts que contiene la descripcion del atributo
	 * variable para la edicion.
	 */
	private String descripcionHidden;

	/**
	 * Atributo agregado por struts que contiene el formato del atributo
	 * variable para la edicion.
	 */
	private String formatoHidden;

	private String codigoRadioAtributosVariables;

	private String valordDefecto;

	private String codigoExpresion;

	private String codigoExpresionSession;
	/**
	 * /** sErGiO* Atributo de tipo lista que carga las condiciones.
	 */
	private List<ReglaNegocioVO> listaCondiciones;
	/**
	 * sErGiO* Atributo agregado por struts que contiene la condicion del
	 * atributo variable.
	 */
	private String condicion;
	/**
	 * sErGiO* Atributo agregado por struts que contien el padre del atributo
	 * variable.
	 */
	private String padre;
	/**
	 * sErGiO* Atributo agregado por struts que contien el orden del atributo
	 * variable.
	 */
	private String orden;
	/**
	 * sErGiO* Atributo agregado por struts que contien el agrupador del
	 * atributo variable.
	 */
	private String agrupador;
	/**
	 * sErGiO* Atributo tipo Lista agregado para cargar la lista Padre de A
	 */
	private List<LlaveValorVO> listaPadre;

	/**
	 * Atributo agregado por struts que indica si el atributo variable contiene
	 * un dato complementario.
	 */
	private String datoComplementario;

	/**
	 * Atributo agregado por struts que indica si el dato complementario es
	 * obligatorio.
	 */
	private String obligatorioComplementario;

	/**
	 * Atributo agregado por struts que indica si el dato complementario es
	 * modificable.
	 */
	private String modificableComplementario;

	/**
	 * Atributo agregado por struts que indica si aparece un endoso.
	 */
	private String apareceEndoso;

	/**
	 * Atributo agregado por struts que indica si un endoso es obligatorio.
	 */
	private String obligatorioEndoso;
	
	private String cdTipSituacion;
	private String codigoAtributoP;
	
	/**
	 * Atributo agregado por struts que indica si la clave del instrumento de pago
	 */
	private String cdInsCte;
	
	
	/**
	 * Almacena el mensaje de respuesta de una peticion 
	 */
	private String mensajeRespuesta;
	
    /**
     * Atributo agregado como parametro de la petici�n por struts que indica
     * el inicio de el n�mero de linea en cual iniciar
     */
    protected int start;
    
    /**
     * Atributo agregado como parametro de la petici�n por struts que indica la cantidad
     * de registros a ser consultados
     */
    protected int limit=20;
    
    /**
     * Atributo de respuesta interpretado por strust con el n�mero de registros totales
     * que devuelve la consulta.
     */
    protected int totalCount;
	

	/**
	 * Metodo <code>execute</code> con el que es llamado desde Struts para
	 * atender la petici�n web.
	 * 
	 * @return INPUT
	 * @throws Exception
	 */
	public String execute() throws Exception {
		log.debug("Entro execute de atributos variables");
		
		maximo = "";
		minimo = "";
		
		return INPUT;
	}

	/**
	 * Metodo <code>listaDeValoresAtributosVariables</code> con el que son
	 * llamado desde Struts todos los valores de los atributos variables.
	 * 
	 * @return success
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String listaDeValoresAtributosVariables() throws Exception {
		log.debug("Entro a lista de valores de atributos variables");

		PagedList pagedList = atributosVariablesManager.listaDeValoresAtributosVariables("1", start, limit);
		valoresDeAtributos = (List<AtributosVariablesVO>) pagedList.getItemsRangeList();
		totalCount = pagedList.getTotalItems();

		return SUCCESS;
	}

	/**
	 * Metodo que carga la lista padre para el combo de la pantalla de atributos
	 * variables.
	 * 
	 * @return
	 * @throws Exception
	 */

	@SuppressWarnings("unchecked")
	public String listaPadre() throws Exception {
		log.debug("Entro a la lista de Valores Padre para atributos Variables");
		
		if(StringUtils.isNotBlank(cdInsCte)){
			if(log.isDebugEnabled())log.debug("El valor de CdInsCte es: "+ cdInsCte);
			listaPadre = atributosVariablesManager.getPadreObjeto(cdInsCte, codigoAtributo);
			if(log.isDebugEnabled())log.debug("El valor de la lista para padres es: "+ listaPadre);
			session.put("LISTA_PADRE_ATRIBUTOS_VARIABLES", listaPadre);
			success = true;
			return SUCCESS;
		}
		if (session.containsKey("CODIGO_NIVEL6")) {
			if (session.containsKey("CODIGO_ATRIBUTO_VARIABLE_UNICO"))
				codigoAtributoP = (String) session
						.get("CODIGO_ATRIBUTO_VARIABLE_UNICO");
			String cdRamo = (String) session.get("CODIGO_NIVEL0");
			String cdGarantia = (String) session.get("CODIGO_NIVEL6");
			cdTipSituacion = (String) session.get("CODIGO_NIVEL2");
			listaPadre = atributosVariablesManager.getPadreGarantia(cdRamo, cdGarantia, cdTipSituacion,
					codigoAtributoP);
			session.put("LISTA_PADRE_ATRIBUTOS_VARIABLES", listaPadre);
			success = true;
		} else if (session.containsKey("CODIGO_NIVEL2")) {
			if (session.containsKey("CODIGO_ATRIBUTO_VARIABLE_UNICO"))
				codigoAtributoP = (String) session
						.get("CODIGO_ATRIBUTO_VARIABLE_UNICO");
			cdTipSituacion = (String) session.get("CODIGO_NIVEL2");
			if (session.containsKey("CODIGO_OBJETO")) {
				String cdObjeto = (String) session.get("CODIGO_OBJETO");
				listaPadre = atributosVariablesManager.getPadreObjeto(
						cdObjeto, codigoAtributoP);
				session.put("LISTA_PADRE_ATRIBUTOS_VARIABLES", listaPadre);
				success = true;
			} else if (session.containsKey("CODIGO_ROL")) {
				String cdRamo = (String) session.get("CODIGO_NIVEL0");
				String cdRol = (String) session.get("CODIGO_ROL");
				listaPadre = atributosVariablesManager.getPadreRol(cdRamo, cdRol,
						codigoAtributoP);
				session.put("LISTA_PADRE_ATRIBUTOS_VARIABLES", listaPadre);
				success = true;
			} else {
				listaPadre = atributosVariablesManager.getPadre(cdTipSituacion,
						codigoAtributoP);
				session.put("LISTA_PADRE_ATRIBUTOS_VARIABLES", listaPadre);
				success = true;
			}
		} else if (session.containsKey("CODIGO_NIVEL0")) {
			if (session.containsKey("CODIGO_ATRIBUTO_VARIABLE_UNICO"))
				codigoAtributoP = (String) session
				.get("CODIGO_ATRIBUTO_VARIABLE_UNICO");
			String cdRamo = (String) session.get("CODIGO_NIVEL0");
			
			if (session.containsKey("CODIGO_ROL")) {
				String cdRol = (String) session.get("CODIGO_ROL");
				listaPadre = atributosVariablesManager.getPadreRol(cdRamo, cdRol,
						codigoAtributoP);
				session.put("LISTA_PADRE_ATRIBUTOS_VARIABLES", listaPadre);
				success = true;
			} else {
				listaPadre = atributosVariablesManager.getPadrePoliza(cdRamo,
						codigoAtributoP);
				session.put("LISTA_PADRE_ATRIBUTOS_VARIABLES", listaPadre);
				success = true;
			}
		} else {

			log.debug("CODIGO OBJETO " + session.containsKey("CODIGO_OBJETO"));
			codigoAtributoP = (String) session
					.get("CODIGO_ATRIBUTO_VARIABLE_UNICO");
			log.debug("codigoAtributoP-->" + codigoAtributoP);
			cdTipSituacion = (String) session.get("CODIGO_NIVEL2");
			cdTipSituacion = (String) session.get("CODIGO_OBJETO");
			log.debug("cdTipSituacion" + cdTipSituacion);
			listaPadre = atributosVariablesManager.getPadreObjeto(
					cdTipSituacion, codigoAtributoP);
			session.put("LISTA_PADRE_ATRIBUTOS_VARIABLES", listaPadre);
			success = false;
		}
		
		
		return SUCCESS;
	}

	/**
	 * Metodo <code>guardarAtributosVariables</code> que es llamado desde
	 * Struts para insertar Atributos Variables a un producto.
	 * 
	 * @return SUCCESS
	 * @throws Exception
	 */
	// *NOTA: Retorna un valor number de la bd para el MSG_ID pero en este
	// momento no se esta manipulando
	@SuppressWarnings("unchecked")
	public String guardarAtributosVariables() throws Exception {
		log.debug(""
				+ "\n#######################################"
				+ "\n###### guardarAtributosVariables ######"
				);
		log.debug("codigoExpresionSession" + codigoExpresionSession);
		if (!(codigoExpresionSession != null
				&& StringUtils.isNotBlank(codigoExpresionSession) && codigoExpresionSession
				.equals("undefined")))
			codigoExpresionSession = "EXPRESION";

		log.debug("Entro a guardar los atributos variables");
		log.debug("Entro-->" + obligatorio);

		AtributosVariablesVO atributos = new AtributosVariablesVO();

		if (session.containsKey("CODIGO_NIVEL0"))
			codigoRamo = (String) session.get("CODIGO_NIVEL0");
		else
			codigoRamo = "1";

		log.debug("CODIGO RAMO-------------: " + codigoRamo);

		if (obligatorio == null) {
			obligatorio = "N";
		}
		if (modificaEmision == null) {
			modificaEmision = "N";
		}
		if (modificaEndoso == null) {
			modificaEndoso = "N";
		}
		if (retarificacion == null) {
			retarificacion = "N";
		}
		if (despliegaCotizador == null) {
			despliegaCotizador = "N";
		}
		if (apareceEndoso == null) {
			apareceEndoso = "N";
		}
		if (datoComplementario == null) {
			datoComplementario = "N";
		}
		if (obligatorioComplementario == null) {
			obligatorioComplementario = "N";
		}
		if (obligatorioEndoso == null) {
			obligatorioEndoso = "N";
		}
		if (modificableComplementario == null) {
			modificableComplementario = "N";
		}
		if(atributoParaTodos==null)
		{
			atributoParaTodos = "N";
		}

		if (StringUtils.isBlank(codigoRadioAtributosVariables))
			codigoRadioAtributosVariables = "A";
		log.debug(codigoRadioAtributosVariables);
		log.debug(descripcion);
		atributos.setCdRamo(codigoRamo);
		atributos.setClaveCampo(claveCampo);
		atributos.setDescripcion(descripcion);
		atributos.setCdFormato(codigoRadioAtributosVariables);
		atributos.setObligatorio(obligatorio);
		atributos.setEmision(modificaEmision);
		atributos.setEndosos(modificaEndoso);
		atributos.setRetarificacion(retarificacion);
		atributos.setCotizador(despliegaCotizador);
		atributos.setMaximo(maximo);
		atributos.setMinimo(minimo);
		atributos.setCdTabla(codigoTabla);
		atributos.setInserta("S");
		atributos.setApareceEndoso(apareceEndoso);
		atributos.setDatoComplementario(datoComplementario);
		atributos.setObligatorioComplementario(obligatorioComplementario);
		atributos.setObligatorioEndoso(obligatorioEndoso);
		atributos.setModificableComplementario(modificableComplementario);
		atributos.setAtributoParaTodos(atributoParaTodos);

		/* sErGiO */
		if (StringUtils.isNotBlank(agrupador)) {
			log.debug("agrupador:" + agrupador);
			atributos.setAgrupador(agrupador);
		}
		if (StringUtils.isNotBlank(orden)) {
			log.debug("orden:" + orden);
			atributos.setOrden(orden);
		}
		log.debug("condicion:" + condicion);
		if (StringUtils.isNotBlank(condicion)) {
			if (condicion.contains("Seleccione una")) {
				log.debug("Viene nula la condicion");
			} else {
				List<ReglaNegocioVO> listaCondiciones = (List<ReglaNegocioVO>) session
						.get("LISTA_REGLA_NEGOCIO_CONDICIONES");
				for (ReglaNegocioVO reglaNeg : listaCondiciones) {
					if (reglaNeg.getDescripcion().equals(condicion)) {
						atributos.setCodigoCondicion(reglaNeg.getNombre());
						success = true;
					}
				}

			}
		}
		log.debug("padre:" + padre);
		if (StringUtils.isNotBlank(padre)) {
			if (padre.contains("Seleccione un Padre")) {
				log.debug("Entro el padre vacio");
			} else {
				List<LlaveValorVO> listaAtributosPadres = (List<LlaveValorVO>) session
						.get("LISTA_PADRE_ATRIBUTOS_VARIABLES");
				for (LlaveValorVO listaPadres : listaAtributosPadres) {
					if (listaPadres.getValue().equals(padre)) {
						atributos.setCodigoPadre(listaPadres.getKey());
						log.debug("key Padre" + listaPadres.getKey());
						success = true;
					}
				}

			}
		}

		/* sErGiO */
		int codigoExpresionInt = 0;
		success = true;
		if (session.containsKey(codigoExpresionSession)) {
			// ExpresionVO e = (ExpresionVO)session.get(codigoExpresionSession);
			// codigoExpresionInt = insertarExpresion(success, e);
			codigoExpresionInt = (Integer) session.get(codigoExpresionSession);
		}

		atributos.setCodigoExpresion(Integer.toString(codigoExpresionInt));
		log.debug("CLAVECAMPO" + claveCampo);
		log.debug("DESCRIPCIONHIDDEN" + descripcionHidden);
		log.debug("FORMATHHIDEN" + formatoHidden);
		if (claveCampo != null && StringUtils.isNotBlank(claveCampo)
				&& !claveCampo.equals("undefined")) {
			log.debug("dentro del if");
			atributos.setCodigoAtributo(claveCampo);
			//atributos.setDescripcion(descripcionHidden);//se deja el valor del atributo descripcion
			atributos.setCdFormato(codigoRadioAtributosVariables);
		}	
		log.debug(atributos.getCdFormato());
		log.debug(atributos.getDescripcion());
		if (session.containsKey("CODIGO_NIVEL6")) {
			log.debug("DENTRO DE CODIGO_NIVEL6");
			codigoCobertura = (String) session.get("CODIGO_NIVEL6");
			codigoSituacion = (String) session.get("CODIGO_NIVEL2");
			atributos.setCodigoSituacion(codigoSituacion);
			atributos.setCodigoGarantia(codigoCobertura);
			MensajesVO msjCobertura = null;
			msjCobertura = atributosVariablesManager.guardarAtributosVariablesCobertura(atributos);
			//Atributo success:
			success = StringUtils.equals("2", msjCobertura.getTitle()) ? true: false;
			//Obtener mensaje de respuesta a mostrar:
//			Map<String, String> params = new HashMap<String, String>();
//			params.put("msg", msjCobertura.getMsgId());
//			msjCobertura = catalogManager.getMensajes(params, "OBTIENE_MENSAJES");
//			mensajeRespuesta = msjCobertura.getMsgText();
			
		} else if (session.containsKey("CODIGO_NIVEL2")) {
			log.debug("DENTRO DE CODIGO_NIVEL2");
			codigoSituacion = (String) session.get("CODIGO_NIVEL2");
			atributos.setCodigoSituacion(codigoSituacion);
			MensajesVO msjInciso = null;
			msjInciso = atributosVariablesManager.guardarAtributosVariablesInciso(atributos);
			
			//Atributo success:
			success = StringUtils.equals("2", msjInciso.getTitle()) ? true: false;
			//Obtener mensaje de respuesta a mostrar:
//			Map<String, String> params = new HashMap<String, String>();
//			params.put("msg", msjInciso.getMsgId());
//			msjInciso = catalogManager.getMensajes(params, "OBTIENE_MENSAJES");
			mensajeRespuesta = msjInciso.getMsgText();
			
		} else {
			log.debug("DENTRO DE ELSE");
			if (claveCampo != null && StringUtils.isNotBlank(claveCampo)
					&& !claveCampo.equals("undefined")
					&& descripcionHidden != null
					&& StringUtils.isNotBlank(descripcionHidden)
					&& !descripcionHidden.equals("undefined")) {
				atributos.setCodigoAtributo(claveCampo);
				//atributos.setDescripcion(descripcionHidden);//se deja el valor del atributo descripcion
			}
			try{
				mensajeRespuesta = atributosVariablesManager.guardarAtributosVariables(atributos);
			}catch(ApplicationException ae){
				mensajeRespuesta = ae.getMessage();
				success = false;
				log.debug(""
						+ "\n###### guardarAtributosVariables ######"
						+ "\n#######################################"
						);
				return SUCCESS;
			}
		}
		
		log.debug("Despues de agregar atributo variable :" + success);
		if (success) {
			if (session.containsKey(codigoExpresionSession))
				session.remove(codigoExpresionSession);
			if (session.containsKey("CATALOGO_VARIABLES"))
				session.remove("CATALOGO_VARIABLES");
			List<RamaVO> temporalTree = (List<RamaVO>) session
					.get("ARBOL_PRODUCTOS");
			session.put("ARBOL_PRODUCTOS_RECARGAR", temporalTree);
			session.remove("ARBOL_PRODUCTOS");
		}
		log.debug(""
				+ "\n###### guardarAtributosVariables ######"
				+ "\n#######################################"
				);
		return SUCCESS;
	}

	public String editarAtributosVariables() throws Exception {
		log.debug("Entro a editar atributo variable");
		listaAtributosVariables = new ArrayList<AtributosVariablesVO>();
		AtributosVariablesVO avvo = new AtributosVariablesVO();

		log.debug("CODIGO_NIVEL0 " + session.containsKey("CODIGO_NIVEL0"));
		log.debug("CODIGO_NIVEL2 " + session.containsKey("CODIGO_NIVEL2"));
		log.debug("CODIGO_NIVEL6 " + session.get("CODIGO_NIVEL6"));
		log.debug("CODIGO_NIVEL7 " + session.get("CODIGO_NIVEL7"));
		log.debug("CODIGO_NIVEL8 " + session.get("CODIGO_NIVEL8"));

		if (session.containsKey("CODIGO_NIVEL0")) {
			codigoRamo = (String) session.get("CODIGO_NIVEL0");
			nivel = 1;
			avvo.setCdRamo(codigoRamo);
		}
		if (session.containsKey("CODIGO_NIVEL2")) {
			codigoSituacion = (String) session.get("CODIGO_NIVEL2");
			nivel = 2;
			avvo.setCodigoSituacion(codigoSituacion);
		}

		if (session.containsKey("CODIGO_NIVEL6")) {
			codigoCobertura = (String) session.get("CODIGO_NIVEL6");
			nivel = 3;
			avvo.setCodigoGarantia(codigoCobertura);
		}
		if (session.containsKey("CODIGO_ATRIBUTO_VARIABLE_UNICO")) {
			codigoAtributo = (String) session
					.get("CODIGO_ATRIBUTO_VARIABLE_UNICO");
			avvo.setCodigoAtributo(codigoAtributo);
		}

		if (session.containsKey("CODIGO_NIVEL8")) {
			codigoAtributo = (String) session.get("CODIGO_NIVEL8");
			avvo.setCodigoAtributo(codigoAtributo);
		}

		avvo = atributosVariablesManager.obtenerAtributoVariablePorNivel(nivel,
				avvo);

		if (log.isDebugEnabled()) {
			log.debug("DESCRIPCION*****" + avvo.getDescripcion());
			log.debug("FORMATO*****" + avvo.getCdFormato());
			log.debug("EMISION*****" + avvo.getEmision());
			log.debug("ENDOSOS*****" + avvo.getEndosos());
			log.debug("RETARIFICACION*****" + avvo.getRetarificacion());
			log.debug("COTIZADOR*****" + avvo.getCotizador());
			log.debug("LISTAvALOR*****" + avvo.getDsTabla());
		}

		if (session.containsKey("CODIGO_NIVEL8")) {
			session.remove("CODIGO_NIVEL8");
		}
		if (session.containsKey("CODIGO_ATRIBUTO_VARIABLE_UNICO")) 
		session.remove("CODIGO_ATRIBUTO_VARIABLE_UNICO");

		listaAtributosVariables.add(avvo);
		success = true;
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String eliminarAtributosVariables() throws Exception {
		log.debug("Entrando a eliminarAtributosVariables()");
		
		//Obtener Datos del atributo variable
		AtributosVariablesVO atributos = new AtributosVariablesVO();
		if (session.containsKey("CODIGO_NIVEL0")) {
			codigoRamo = (String) session.get("CODIGO_NIVEL0");
			nivel = 1;
			atributos.setCdRamo(codigoRamo);
		}
		if (session.containsKey("CODIGO_NIVEL2")) {
			codigoSituacion = (String) session.get("CODIGO_NIVEL2");
			nivel = 2;
			atributos.setCodigoSituacion(codigoSituacion);
		}
		if (session.containsKey("CODIGO_NIVEL6")) {
			codigoCobertura = (String) session.get("CODIGO_NIVEL6");
			nivel = 3;
			atributos.setCodigoGarantia(codigoCobertura);
		}
		atributos.setClaveCampo(claveCampo);
		
		if(log.isDebugEnabled()) {
			log.debug("CODIGO_NIVEL0 " + session.get("CODIGO_NIVEL0"));
			log.debug("CODIGO_NIVEL2 " + session.get("CODIGO_NIVEL2"));
			log.debug("CODIGO_NIVEL6 " + session.get("CODIGO_NIVEL6"));
		}
		MensajesVO mensajeVO = null;
		mensajeVO = atributosVariablesManager.eliminarAtributosVariables(atributos, nivel);
		mensajeRespuesta = mensajeVO.getMsgText();
		if(mensajeVO.getMsgId()!=null&&mensajeVO.getMsgId().equals("200012")){
			if(StringUtils.isBlank(mensajeRespuesta))mensajeRespuesta = "El atributo variable se ha eliminado con \u00e9xito";
			success = true;
			//Si se elimino elemento, actualizar valores
			if (session.containsKey(codigoExpresionSession))
				session.remove(codigoExpresionSession);
			if (session.containsKey("CATALOGO_VARIABLES"))
				session.remove("CATALOGO_VARIABLES");
			List<RamaVO> temporalTree = (List<RamaVO>) session.get("ARBOL_PRODUCTOS");
			session.put("ARBOL_PRODUCTOS_RECARGAR", temporalTree);
			session.remove("ARBOL_PRODUCTOS");
		}else{
			success = false;
			if(StringUtils.isBlank(mensajeRespuesta))mensajeRespuesta = "Error al eliminar atributo variable";
		}
		
		return SUCCESS;
	}

	public String obtenerCodigoExpresion() throws Exception
	{
		log.debug(""
				+ "\n####################################"
				+ "\n###### obtenerCodigoExpresion ######"
				);
		codigoExpresion = Integer.toString(expresionesManager
				.obtieneSecuenciaExpresion());
		log.debug("OBTIENE CODIGO EXPRESION: " + codigoExpresion);

		success = true;
		log.debug(""
				+ "\n###### obtenerCodigoExpresion ######"
				+ "\n####################################"
				);
		return SUCCESS;
	}

	public String borraSesionExpresion() throws Exception {
		session.remove("EXPRESION");
		if (session.containsKey("CATALOGO_VARIABLES")) {
			session.remove("CATALOGO_VARIABLES");
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	 * Valida si el atributo variable tiene hijos asociados
	 * @return
	 * @throws Exception
	 */
	public String validaHijosAtributoVariable() throws Exception {
		log.debug("Entrando a validaHijosAtributoVariable()");
		
		//Tomar datos para el atributo variable: 
		AtributosVariablesVO atributoVariable = new AtributosVariablesVO();
		if (session.containsKey("CODIGO_NIVEL0")) {
			codigoRamo = (String) session.get("CODIGO_NIVEL0");
			nivel = 1;
			atributoVariable.setCdRamo(codigoRamo);
		}
		if (session.containsKey("CODIGO_NIVEL2")) {
			codigoSituacion = (String) session.get("CODIGO_NIVEL2");
			nivel = 2;
			atributoVariable.setCodigoSituacion(codigoSituacion);
		}
		if (session.containsKey("CODIGO_NIVEL6")) {
			codigoCobertura = (String) session.get("CODIGO_NIVEL6");
			nivel = 3;
			atributoVariable.setCodigoGarantia(codigoCobertura);
		}
		atributoVariable.setCodigoAtributo(claveCampo);
		
		//Elegir el endpoint a ejecutar
		
		switch(nivel){
			case 1 :
				if(atributosVariablesManager.tieneHijosAtributoVariableProducto(atributoVariable)){
					mensajeRespuesta = Constantes.MESSAGE_ATRIBUTO_CON_HIJOS_ASOCIADOS;
				}

				log.debug("tipo :Producto");
				break;
			case 2 :
				if(atributosVariablesManager.tieneHijosAtributoVariableIncisoRiesgo(atributoVariable)){
					mensajeRespuesta = Constantes.MESSAGE_ATRIBUTO_CON_HIJOS_ASOCIADOS;
				}
		
				log.debug("tipo :Riesgo/Inciso");
				break;
			case 3 :
				if(atributosVariablesManager.tieneHijosAtributoVariableCobertura(atributoVariable)){
					mensajeRespuesta = Constantes.MESSAGE_ATRIBUTO_CON_HIJOS_ASOCIADOS;
				}
		
				log.debug("tipo :Cobertura");
				break;
			default: 
		
				log.debug("tipo :NONE");
				break;
		}
		
		success = true;
		return SUCCESS;
	}

	// Getters and Setters

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getCodigoTabla() {
		return codigoTabla;
	}

	public void setCodigoTabla(String codigoTabla) {
		this.codigoTabla = codigoTabla;
	}

	public String getClaveCampo() {
		return claveCampo;
	}

	public void setClaveCampo(String claveCampo) {
		this.claveCampo = claveCampo;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public String getModificaEmision() {
		return modificaEmision;
	}

	public void setModificaEmision(String modificaEmision) {
		this.modificaEmision = modificaEmision;
	}

	public String getModificaEndoso() {
		return modificaEndoso;
	}

	public void setModificaEndoso(String modificaEndoso) {
		this.modificaEndoso = modificaEndoso;
	}

	public String getRetarificacion() {
		return retarificacion;
	}

	public void setRetarificacion(String retarificacion) {
		this.retarificacion = retarificacion;
	}

	public String getDespliegaCotizador() {
		return despliegaCotizador;
	}

	public void setDespliegaCotizador(String despliegaCotizador) {
		this.despliegaCotizador = despliegaCotizador;
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

	public String getCodigoRamo() {
		return codigoRamo;
	}

	public void setCodigoRamo(String codigoRamo) {
		this.codigoRamo = codigoRamo;
	}

	public void setAtributosVariablesManager(
			AtributosVariablesManager atributosVariablesManager) {
		this.atributosVariablesManager = atributosVariablesManager;
	}

	public void setCatalogManager(CatalogService catalogManager) {
		this.catalogManager = catalogManager;
	}

	public List<AtributosVariablesVO> getValoresDeAtributos() {
		return valoresDeAtributos;
	}

	public void setValoresDeAtributos(
			List<AtributosVariablesVO> valoresDeAtributos) {
		this.valoresDeAtributos = valoresDeAtributos;
	}

	public String getMaximo() {
		return maximo;
	}

	public void setMaximo(String maximo) {
		this.maximo = maximo;
	}

	public String getMinimo() {
		return minimo;
	}

	public void setMinimo(String minimo) {
		this.minimo = minimo;
	}

	/**
	 * @return the codigoSituacion
	 */
	public String getCodigoSituacion() {
		return codigoSituacion;
	}

	/**
	 * @param codigoSituacion
	 *            the codigoSituacion to set
	 */
	public void setCodigoSituacion(String codigoSituacion) {
		this.codigoSituacion = codigoSituacion;
	}

	/**
	 * @return the codigoCobertura
	 */
	public String getCodigoCobertura() {
		return codigoCobertura;
	}

	/**
	 * @param codigoCobertura
	 *            the codigoCobertura to set
	 */
	public void setCodigoCobertura(String codigoCobertura) {
		this.codigoCobertura = codigoCobertura;
	}

	/**
	 * @return the listaAtributosVariables
	 */
	public List<AtributosVariablesVO> getListaAtributosVariables() {
		return listaAtributosVariables;
	}

	/**
	 * @param listaAtributosVariables
	 *            the listaAtributosVariables to set
	 */
	public void setListaAtributosVariables(
			List<AtributosVariablesVO> listaAtributosVariables) {
		this.listaAtributosVariables = listaAtributosVariables;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public String getCodigoAtributo() {
		return codigoAtributo;
	}

	public void setCodigoAtributo(String codigoAtributo) {
		this.codigoAtributo = codigoAtributo;
	}

	public String getDescripcionHidden() {
		return descripcionHidden;
	}

	public void setDescripcionHidden(String descripcionHidden) {
		this.descripcionHidden = descripcionHidden;
	}

	public String getFormatoHidden() {
		return formatoHidden;
	}

	public void setFormatoHidden(String formatoHidden) {
		this.formatoHidden = formatoHidden;
	}

	/**
	 * @return the codigoRadioAtributosVariables
	 */
	public String getCodigoRadioAtributosVariables() {
		return codigoRadioAtributosVariables;
	}

	/**
	 * @param codigoRadioAtributosVariables
	 *            the codigoRadioAtributosVariables to set
	 */
	public void setCodigoRadioAtributosVariables(
			String codigoRadioAtributosVariables) {
		this.codigoRadioAtributosVariables = codigoRadioAtributosVariables;
	}

	/**
	 * @return the valordDefecto
	 */
	public String getValordDefecto() {
		return valordDefecto;
	}

	/**
	 * @param valordDefecto
	 *            the valordDefecto to set
	 */
	public void setValordDefecto(String valordDefecto) {
		this.valordDefecto = valordDefecto;
	}

	/**
	 * @return the codigoExpresion
	 */
	public String getCodigoExpresion() {
		return codigoExpresion;
	}

	/**
	 * @param codigoExpresion
	 *            the codigoExpresion to set
	 */
	public void setCodigoExpresion(String codigoExpresion) {
		this.codigoExpresion = codigoExpresion;
	}

	/**
	 * 
	 * @return
	 */
	public String getCondicion() {
		return condicion;
	}

	/**
	 * 
	 * @param condicion
	 */
	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}

	/**
	 * 
	 * @return
	 */
	public String getPadre() {
		return padre;
	}

	/**
	 * 
	 * @param padre
	 */
	public void setPadre(String padre) {
		this.padre = padre;
	}

	/**
	 * 
	 * @return
	 */
	public String getOrden() {
		return orden;
	}

	/**
	 * 
	 * @param orden
	 */
	public void setOrden(String orden) {
		this.orden = orden;
	}

	/**
	 * 
	 * @return
	 */
	public String getAgrupador() {
		return agrupador;
	}

	/**
	 * 
	 * @param agrupador
	 */
	public void setAgrupador(String agrupador) {
		this.agrupador = agrupador;
	}

	public List<ReglaNegocioVO> getListaCondiciones() {
		return listaCondiciones;
	}

	/**
	 * 
	 * @param listaCondiciones
	 */
	public void setListaCondiciones(List<ReglaNegocioVO> listaCondiciones) {
		this.listaCondiciones = listaCondiciones;
	}

	/**
	 * 
	 * @return
	 */
	public List<LlaveValorVO> getListaPadre() {
		return listaPadre;
	}

	/**
	 * 
	 * @param listaPadre
	 */
	public void setListaPadre(List<LlaveValorVO> listaPadre) {
		this.listaPadre = listaPadre;
	}

	/**
	 * 
	 * @return
	 */
	public String getCodigoAtributoP() {
		return codigoAtributoP;
	}

	/**
	 * 
	 * @param codigoAtributoP
	 */
	public void setCodigoAtributoP(String codigoAtributoP) {
		this.codigoAtributoP = codigoAtributoP;
	}

	/**
	 * 
	 * @return
	 */
	public String getCdTipSituacion() {
		return cdTipSituacion;
	}

	/**
	 * 
	 * @param cdTipSituacion
	 */
	public void setCdTipSituacion(String cdTipSituacion) {
		this.cdTipSituacion = cdTipSituacion;
	}

	public String getCodigoExpresionSession() {
		return codigoExpresionSession;
	}

	public void setCodigoExpresionSession(String codigoExpresionSession) {
		this.codigoExpresionSession = codigoExpresionSession;
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

	public String getMensajeRespuesta() {
		return mensajeRespuesta;
	}

	public void setMensajeRespuesta(String mensajeRespuesta) {
		this.mensajeRespuesta = mensajeRespuesta;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getCdInsCte() {
		return cdInsCte;
	}

	public void setCdInsCte(String cdInsCte) {
		this.cdInsCte = cdInsCte;
	}

	public String getAtributoParaTodos() {
		return atributoParaTodos;
	}

	public void setAtributoParaTodos(String atributoParaTodos) {
		this.atributoParaTodos = atributoParaTodos;
	}

}