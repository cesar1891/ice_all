package mx.com.gseguros.wizard.configuracion.producto.librerias.web;

import java.util.ArrayList;
import java.util.List;

import mx.com.aon.portal.service.PagedList;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.ExpresionVO;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.VariableVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.reglanegocio.model.ReglaNegocioVO;
import mx.com.gseguros.wizard.configuracion.producto.service.ReglaNegocioManager;
import mx.com.gseguros.wizard.configuracion.producto.util.ReglaNegocio;
import mx.com.gseguros.wizard.configuracion.producto.web.ExpresionesPadre;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Edgar Perez
 * @version 2.0
 * @since 1.0
 * 
 * Clase que contiene los metodos para cargar y agregar informacion a las librerias
 * 
 * 
 */
public class PrincipalLibreriasAction extends ExpresionesPadre {

	private static final long serialVersionUID = 8018809548707829539L;
	private static final transient Log log = LogFactory
			.getLog(PrincipalLibreriasAction.class);

	/**
	 * Atributo de respuesta interpretado por strust como la correcta
	 * realizacion de. cada peticion.
	 */
	private boolean success;

	/**
	 * Atributo agregado por struts que contiene el valor del grid de la libreria que se va 
	 * a cargar en el metodo cargaReglaNegocio.
	 */
	private String numeroGrid;
	
	/**
	 * Atributo agregado por struts que contiene el tipo de combo que se va a cargar 
	 * en el metodo cargaComboTipo.
	 */
	private String comboTipo;
	
	/**
	 * Atributo agregado por struts que contiene el tipo de libreria donde se insertara 
	 * la cabecera en el metodo insertaReglaNegocio.
	 */
	private String libreria;

	/**
	 * Manager con implementacion de Endpoint para la consulta a BD.
	 */
	private ReglaNegocioManager reglaNegocioManager;

	/**
	 * Atributo Enum que contiene las los codigos de la regla de negocios
	 */
	private ReglaNegocio reglaNegocio;

	/**
	 * Atributo de respuesta interpretado por strust con la lista de beans tipo
	 * ReglaNegocioVO con los valores de la consulta para la libreria de Variables Temporales.
	 */
	private List<ReglaNegocioVO> listaReglaNegocioVariables;
	
	/**
	 * Atributo de respuesta interpretado por strust con la lista de beans tipo
	 * ReglaNegocioVO con los valores de las Variaes Temporales asociadas a un producto.
	 */
	private List<ReglaNegocioVO> listaReglaNegocioVariablesProducto;
	/**
	 * Atributo agregado por struts que contiene el codigo de la variable seleccionada 
	 * para asociarla al producto.
	 */	
	private String codigoVariableProducto;
	/**
	 * Atributo agregado por struts que contiene la descripcion de la variable seleccionada 
	 * para asociarla al producto.
	 */
	private String descripcionVariableProducto;
	/**
	 * Atributo de respuesta interpretado por strust con la lista de beans tipo
	 * ReglaNegocioVO con los valores de la consulta para la libreria de Validaciones.
	 */
	private List<ReglaNegocioVO> listaReglaNegocioValidaciones;
	
	/**
	 * Atributo de respuesta interpretado por strust con la lista de beans tipo
	 * ReglaNegocioVO con los valores de la consulta para la libreria de Condiciones.
	 */
	private List<ReglaNegocioVO> listaReglaNegocioCondiciones;
	
	/**
	 * Atributo de respuesta interpretado por strust con la lista de beans tipo
	 * ReglaNegocioVO con los valores de la consulta para la libreria de Autorizaciones.
	 */
	private List<ReglaNegocioVO> listaReglaNegocioAutorizaciones;
	
	/**
	 * Atributo de respuesta interpretado por strust con la lista de beans tipo
	 * ReglaNegocioVO con los valores de la consulta para la libreria de Conceptos de Tarificacion.
	 */
	private List<ReglaNegocioVO> listaReglaNegocioTarificacion;
	
	/**
	 * Atributo de respuesta interpretado por strust con la lista de beans tipo
	 * LlaveValorVO con los valores de la consulta que llenan los combos de las librerias de
	 * Validaciones y Concepto de Tarificacion.
	 */
	private List<LlaveValorVO> listaTipo;
	
	/**
	 * Atributo agregado por struts que contiene el nombre para la cabecera de la 
	 * libreria.
	 */
	private String nombreCabecera;
	
	/**
	 * Atributo agregado por struts que contiene la descripcion para la cabecera 
	 * de la libreria.
	 */
	private String descripcionCabecera;
	
	/**
	 * Atributo agregado por struts que contiene el tipo para la cabecera 
	 * de las librerias validaciones y canceptos de tarificacion.
	 */
	private String tipo;
	
	/**
	 * Atributo agregado por struts que contiene el mensaje para la cabecera de la 
	 * libreria validaciones.
	 */
	private String mensaje;
	
	/**
	 * Atributo agregado por struts que contiene el nivel para la cabecera de la 
	 * libreria Autorizaciones.
	 */
	private String nivel;
	
	/**
	 * Atributo agregado por struts que contiene el codigo de la expresion para asociar la cabecera 
	 * a las librerias.
	 */
	private int codigoExpresion;
	
	/**
	 * Atributo agregado por struts que contiene el la descripcion del tipo para la cabecera de la 
	 * libreria validaciones y concepto de tarificacion.
	 */
	private String tipoDescripcion;
	
	/**
	 * Lista que contiene la expresion de la regla de negocio
	 */
	private List<ExpresionVO> listaExpresionesReglaNegocio;
	
	/**
	 * Lista que contiene las variables de la expresion de la regla de negocio
	 */
	private List<VariableVO> listaVariablesReglaNegocio;

	private String cdramo;
    
    private String agregarVariableTemp;
    
    private String mensajeDelAction;
    
    private String mensajeValidacion;
    
	private int start = 0;
	private int limit = 20;
	private int totalCount;
	
	/**
	 * Metodo <code>execute</code> con el que es llamado desde Struts para
	 * atender la petici�n web.
	 * 
	 * @return INPUT
	 * @throws Exception
	 */
	public String execute() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("entro al execute de librerias");
		}
		return INPUT;
	}
	
	@SuppressWarnings("unchecked")
	public String listaCatalogoVariablesProducto() throws Exception{
		if(session.containsKey("CATALOGO_VARIABLES_TEMPORALES_PRODUCTO"))
			listaReglaNegocioVariables = (List<ReglaNegocioVO>) session.get("CATALOGO_VARIABLES_TEMPORALES_PRODUCTO");
		else{
			listaReglaNegocioVariables = reglaNegocioManager.obtenerReglasNegocio(reglaNegocio.VariableTemporal);
			session.put("CATALOGO_VARIABLES_TEMPORALES_PRODUCTO", listaReglaNegocioVariables);
		}
		success = true;
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String listaVariablesAsociadasAlProducto() throws Exception
	{
		log.info(""
				+ "\n###############################################"
				+ "\n###### listaVariablesAsociadasAlProducto ######"
				);
		log.debug("Entro a lista de variables asociadas al producto: "+session.get("CODIGO_NIVEL0"));
		if(session.containsKey("CODIGO_NIVEL0"))
		{
			cdramo = (String)session.get("CODIGO_NIVEL0");
		}
		if(session.containsKey("VARIABLES_TEMPORALES_ASOCIADAS_AL_PRODUCTO") && !session.containsKey("REFRESH_VTP") && false)
		{
			listaReglaNegocioVariablesProducto = (List<ReglaNegocioVO>) session.get("VARIABLES_TEMPORALES_ASOCIADAS_AL_PRODUCTO");
			log.info("desde sesion: "+listaReglaNegocioVariablesProducto);
		}
		else
		{
			session.remove("REFRESH_VTP");
			if(cdramo != null && StringUtils.isNotBlank(cdramo))
			{
				listaReglaNegocioVariablesProducto = reglaNegocioManager.obtenerVariablesTemporalesAsociadasAlProducto(cdramo);
				log.info("desde bd: "+listaReglaNegocioVariablesProducto);
			}
		}
		if(listaReglaNegocioVariablesProducto==null)
		{
			listaReglaNegocioVariablesProducto = new ArrayList<ReglaNegocioVO>();
		}
		session.put("VARIABLES_TEMPORALES_ASOCIADAS_AL_PRODUCTO", listaReglaNegocioVariablesProducto);
		session.put("VARIABLES_TEMPORALES_ASOCIADAS_AL_PRODUCTO_DESDE_LA_BASE", listaReglaNegocioVariablesProducto);
		
		success = true;
		log.info(""
				+ "\n###### listaVariablesAsociadasAlProducto ######"
				+ "\n###############################################"
				);
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String asociarVariableAlProducto() throws Exception
	{
		log.info(""
				+ "\n#######################################"
				+ "\n###### asociarVariableAlProducto ######"
				);
		boolean isDebugEnabled = log.isDebugEnabled();
		
		if(isDebugEnabled){
			log.debug("dentro de asociarVariableAlProducto");
			log.debug(codigoVariableProducto);
		}
		if(codigoVariableProducto != null && 
				StringUtils.isNotBlank(codigoVariableProducto)&& 
				!codigoVariableProducto.equals("undefined")){
			if(isDebugEnabled){
				log.debug("codigoVariableProducto = " + codigoVariableProducto+
						"session.containsKey('CATALOGO_VARIABLES_TEMPORALES_PRODUCTO') = " +session.containsKey("CATALOGO_VARIABLES_TEMPORALES_PRODUCTO") + 
						"session.containsKey('VARIABLES_TEMPORALES_ASOCIADAS_AL_PRODUCTO') = " + session.containsKey("VARIABLES_TEMPORALES_ASOCIADAS_AL_PRODUCTO"));
			}
			if(!session.containsKey("CATALOGO_VARIABLES_TEMPORALES_PRODUCTO"))
				listaCatalogoVariablesProducto();
			if(!session.containsKey("VARIABLES_TEMPORALES_ASOCIADAS_AL_PRODUCTO"))
				listaVariablesAsociadasAlProducto();
			listaReglaNegocioVariables = (List<ReglaNegocioVO>) session.get("CATALOGO_VARIABLES_TEMPORALES_PRODUCTO");
			listaReglaNegocioVariablesProducto = (List<ReglaNegocioVO>) session.get("VARIABLES_TEMPORALES_ASOCIADAS_AL_PRODUCTO");
			if(contieneVariable(codigoVariableProducto, listaReglaNegocioVariables)!=-1)
			{
				int posicion = contieneVariable(codigoVariableProducto, listaReglaNegocioVariables);
				if(isDebugEnabled)
				{
					log.debug("posicion = " + posicion);
				}
				ReglaNegocioVO variableTemporalVO = listaReglaNegocioVariables.get(posicion);
				String codigoRamo;
				if(session.containsKey("CODIGO_NIVEL0"))
				{
					codigoRamo = (String) session.get("CODIGO_NIVEL0");
				}else{
					codigoRamo = "1";
				}
				List<ReglaNegocioVO>listaConRegistroUnico=new ArrayList<ReglaNegocioVO>();
				listaConRegistroUnico.add(variableTemporalVO);
				reglaNegocioManager.asociarVariablesDelProducto(listaConRegistroUnico,codigoRamo);
				variableTemporalVO.setTemporal(true);
				listaReglaNegocioVariablesProducto.add(variableTemporalVO);
				listaReglaNegocioVariables.remove(posicion);
				session.put("CATALOGO_VARIABLES_TEMPORALES_PRODUCTO", listaReglaNegocioVariables);
				session.put("VARIABLES_TEMPORALES_ASOCIADAS_AL_PRODUCTO", listaReglaNegocioVariablesProducto);
			}
			else
			{
				if(isDebugEnabled)
				{
					log.debug("posicion es -1");
				}
			}
		}
		success = true;
		log.info(""
				+ "\n###### asociarVariableAlProducto ######"
				+ "\n#######################################"
				);
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String desasociarVariableDelProducto() throws Exception{
		log.info(""
				+ "\n###########################################"
				+ "\n###### desasociarVariableDelProducto ######"
				);
		log.debug("desasociarVariableDelProducto: "+codigoVariableProducto);
		if(codigoVariableProducto != null && 
				StringUtils.isNotBlank(codigoVariableProducto)&& 
				!codigoVariableProducto.equals("undefined")){
			
			if(!session.containsKey("CATALOGO_VARIABLES_TEMPORALES_PRODUCTO")){
//				log.debug("CARGA CATALOGO DE VARIABLES PROD");
				listaCatalogoVariablesProducto();
			}
			if(!session.containsKey("VARIABLES_TEMPORALES_ASOCIADAS_AL_PRODUCTO")){
//				log.debug("CARGA VARIABLES ASOCIADAS");
				listaVariablesAsociadasAlProducto();
			}
				
			listaReglaNegocioVariables = (List<ReglaNegocioVO>) session.get("CATALOGO_VARIABLES_TEMPORALES_PRODUCTO");
			listaReglaNegocioVariablesProducto = (List<ReglaNegocioVO>) session.get("VARIABLES_TEMPORALES_ASOCIADAS_AL_PRODUCTO");
			
//			log.debug("listaReglaNegocioVariablesProducto para eliminar variable: "+listaReglaNegocioVariablesProducto);
			if(contieneVariable(codigoVariableProducto, listaReglaNegocioVariablesProducto)!=-1){
//				log.debug("ENTRO AL IF DE CONTIENE VARIABLE DEL PROD");
				
				int posicion = contieneVariable(codigoVariableProducto, listaReglaNegocioVariablesProducto);
				ReglaNegocioVO variableVO = listaReglaNegocioVariablesProducto.get(posicion);
				
				String codigoRamo;
				if(session.containsKey("CODIGO_NIVEL0"))
				{
					codigoRamo = (String) session.get("CODIGO_NIVEL0");
				}else{
					codigoRamo = "1";
				}
				List<ReglaNegocioVO> listaConRegistroUnico=new ArrayList<ReglaNegocioVO>();
				listaConRegistroUnico.add(variableVO);
				reglaNegocioManager.desasociarVariablesDelProducto(listaConRegistroUnico, codigoRamo);
				
				if(!variableVO.isTemporal()){
//					log.debug("ENTRO AL IF DE TEMPORAL---NO TMP");
					if(!session.containsKey("VARIABLES_DESASOCIADAS_DEL_PRODUCTO")){
						//log.debug("NO CONTIENE LA LAVE VARIABLES_DESASOCIADAS_DEL_PRODUCTO");
						session.put("VARIABLES_DESASOCIADAS_DEL_PRODUCTO", new ArrayList<ReglaNegocioVO>());
					}	
					List<ReglaNegocioVO> listaVariablesDesasociadas = (List<ReglaNegocioVO>) session.get("VARIABLES_DESASOCIADAS_DEL_PRODUCTO");
					if(listaVariablesDesasociadas.isEmpty() || contieneVariable(variableVO.getNombre(), listaVariablesDesasociadas) == -1){
						//log.debug("NOMBRE DE VARIABLE VO"+variableVO.getNombre());
						listaVariablesDesasociadas.add(variableVO);
						session.put("VARIABLES_DESASOCIADAS_DEL_PRODUCTO", listaVariablesDesasociadas);
					}
				}
				variableVO.setTemporal(false);
				listaReglaNegocioVariables.add(variableVO);
				listaReglaNegocioVariablesProducto.remove(posicion);
				session.put("CATALOGO_VARIABLES_TEMPORALES_PRODUCTO", listaReglaNegocioVariables);
				session.put("VARIABLES_TEMPORALES_ASOCIADAS_AL_PRODUCTO", listaReglaNegocioVariablesProducto);
			}
		}
		success = true;
		log.info(""
				+ "\n###### desasociarVariableDelProducto ######"
				+ "\n###########################################"
				);
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public int contieneVariable(String nombre, List<ReglaNegocioVO> lista){
		int posicion = -1;
		if(!lista.isEmpty()){
			for(int i = 0 ; i < lista.size(); i++){
				if(nombre.equals(lista.get(i).getNombre()))
					posicion = i;
			}
		}
		return posicion;
	}
	
	@SuppressWarnings("unchecked")
	public List<ReglaNegocioVO> removerListaDeOtraLista(List<ReglaNegocioVO> lista, List<ReglaNegocioVO> listaARemover){
		log.info(""
				+ "\n#####################################"
				+ "\n###### removerListaDeOtraLista ######"
				);
		log.info("lista padre: "+lista);
		log.info("hijos desconocidos: "+listaARemover);
		List<Integer> variablesARemover = new ArrayList<Integer>();
		if(lista != null && !lista.isEmpty() && listaARemover != null && !listaARemover.isEmpty() )
		{
			for(ReglaNegocioVO variableProducto:lista)
			{
				for(int i=0;i<listaARemover.size();i++)
				{
					if(
						variableProducto.getCodigoExpresion()!=null&&listaARemover.get(i).getCodigoExpresion()!=null
						&&variableProducto.getCodigoExpresion().equals(listaARemover.get(i).getCodigoExpresion())
						&&variableProducto.getNombre()!=null&&listaARemover.get(i).getNombre()!=null
						&&variableProducto.getNombre().equals(listaARemover.get(i).getNombre())
						&&variableProducto.getDescripcion()!=null&&listaARemover.get(i).getDescripcion()!=null
						&&variableProducto.getDescripcion().equals(listaARemover.get(i).getDescripcion())
						)
					{
						log.info("se planea remover el hijo: "+i);
						variablesARemover.add(i);
					}
				}
			}				
			if(!variablesARemover.isEmpty())
			{
				for(int i=variablesARemover.size()-1;i>=0;i--)
				{
					log.info("se quita hijo "+variablesARemover.get(i).intValue());
					lista.remove(variablesARemover.get(i).intValue());
				}
			}
		}
		log.info("padre final: "+lista);
		log.info(""
				+ "\n###### removerListaDeOtraLista ######"
				+ "\n#####################################"
				);
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	public String guardarVariablesTemporalesDelProducto() throws Exception{
		log.info(""
				+ "\n###################################################"
				+ "\n###### guardarVariablesTemporalesDelProducto ######"
				);
		String codigoRamo;
		if(session.containsKey("CODIGO_NIVEL0"))
		{
			codigoRamo = (String) session.get("CODIGO_NIVEL0");
		}else{
			codigoRamo = "1";
		}
		log.info("codigoRamo: "+codigoRamo);
		log.debug("SESSION VARIABLES DESASOCIADAS: "+session.get("VARIABLES_DESASOCIADAS_DEL_PRODUCTO"));
		if(
			session.containsKey("VARIABLES_DESASOCIADAS_DEL_PRODUCTO")
			&&session.get("VARIABLES_DESASOCIADAS_DEL_PRODUCTO")!=null
			&&!((List<ReglaNegocioVO>)session.get("VARIABLES_DESASOCIADAS_DEL_PRODUCTO")).isEmpty()
			)
		{
			log.info("desasociar variables: "+(List<ReglaNegocioVO>)session.get("VARIABLES_DESASOCIADAS_DEL_PRODUCTO"));
			reglaNegocioManager.desasociarVariablesDelProducto((List<ReglaNegocioVO>)session.get("VARIABLES_DESASOCIADAS_DEL_PRODUCTO"), codigoRamo);
		}
		if(
			session.containsKey("VARIABLES_TEMPORALES_ASOCIADAS_AL_PRODUCTO")
			&&session.get("VARIABLES_TEMPORALES_ASOCIADAS_AL_PRODUCTO")!=null
			&&!((List<ReglaNegocioVO>)session.get("VARIABLES_TEMPORALES_ASOCIADAS_AL_PRODUCTO")).isEmpty())
		{
			listaReglaNegocioVariables = (List<ReglaNegocioVO>) session.get("VARIABLES_TEMPORALES_ASOCIADAS_AL_PRODUCTO");
			log.info("asociar variables: "+listaReglaNegocioVariables);
			List<ReglaNegocioVO> variablesAsociadasDesdeLaBase=(List<ReglaNegocioVO>) session.get("VARIABLES_TEMPORALES_ASOCIADAS_AL_PRODUCTO_DESDE_LA_BASE");
			log.info("varibles en bd: "+variablesAsociadasDesdeLaBase);
			listaReglaNegocioVariables = removerListaDeOtraLista(listaReglaNegocioVariables, variablesAsociadasDesdeLaBase);
			log.info("variables que realmente se asocian: "+listaReglaNegocioVariables);
			success = true;
			if(!listaReglaNegocioVariables.isEmpty())
			{
				log.info("Se agregan");
				reglaNegocioManager.asociarVariablesDelProducto(listaReglaNegocioVariables,codigoRamo);
				success = true;
			}else
			{
				log.info("No hay ninguna que agregar");
				success = true;
			}
		}else{
			success=true;
		}
		log.info(""
				+ "\n###### guardarVariablesTemporalesDelProducto ######"
				+ "\n###################################################"
				);
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String remueveCosasDeSessionEnLibrerias() throws Exception{
		if(session.containsKey("CATALOGO_VARIABLES_TEMPORALES_PRODUCTO")){
			session.remove("CATALOGO_VARIABLES_TEMPORALES_PRODUCTO");
		}
		if(session.containsKey("VARIABLES_TEMPORALES_ASOCIADAS_AL_PRODUCTO")){
			session.remove("VARIABLES_TEMPORALES_ASOCIADAS_AL_PRODUCTO");
		}
		if(session.containsKey("VARIABLES_TEMPORALES_ASOCIADAS_AL_PRODUCTO_DESDE_LA_BASE")){
			session.remove("VARIABLES_TEMPORALES_ASOCIADAS_AL_PRODUCTO_DESDE_LA_BASE");
		}
		if(session.containsKey("VARIABLES_DESASOCIADAS_DEL_PRODUCTO")){
			session.remove("VARIABLES_DESASOCIADAS_DEL_PRODUCTO");
		}
		log.debug("variables asociadas al producto en session"+session.get("VARIABLES_TEMPORALES_ASOCIADAS_AL_PRODUCTO"));
		session.put("REFRESH_VTP", "ok");
		session.put("VARIABLES_TEMPORALES_ASOCIADAS_AL_PRODUCTO", new ArrayList<ReglaNegocioVO>());
		log.debug("variables asociadas al producto en session"+session.get("VARIABLES_TEMPORALES_ASOCIADAS_AL_PRODUCTO"));
		success = true;
		return SUCCESS;
	}
	
	
	/**
	 * Metodo <code>cargaVariablesTemporales</code> con el que son llamado
	 * desde Struts todos los valores para cargar los grids  de
	 * las librerias.
	 * 
	 * @return success
	 * @throws Exception
	 */
	@SuppressWarnings({"unchecked", "static-access"})
	public String cargaReglaNegocio() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("Entro a cargar grid variables temporales");
			log.debug("GRID--"+numeroGrid);
		}
		if (numeroGrid != null) {

			PagedList pagedList = null;
			switch (Integer.parseInt(numeroGrid)) {
			case 1:
				pagedList = reglaNegocioManager.obtenerReglasNegocioPagedList(reglaNegocio.VariableTemporal, start, limit);
				listaReglaNegocioVariables = pagedList.getItemsRangeList();
				totalCount = pagedList.getTotalItems();
				
				break;
			case 2:
				pagedList = reglaNegocioManager.obtenerReglasNegocioPagedList(reglaNegocio.Validacion, start, limit);
				listaReglaNegocioValidaciones = pagedList.getItemsRangeList();
				totalCount = pagedList.getTotalItems();

				break;
			case 3:
				pagedList = reglaNegocioManager.obtenerReglasNegocioPagedList(reglaNegocio.Condicion, start, limit);
				listaReglaNegocioCondiciones = pagedList.getItemsRangeList();
				totalCount = pagedList.getTotalItems();
				
				session.put("LISTA_REGLA_NEGOCIO_CONDICIONES", listaReglaNegocioCondiciones);
				break;
			case 4:
				pagedList = reglaNegocioManager.obtenerReglasNegocioPagedList(reglaNegocio.Autorizacion, start, limit);
				listaReglaNegocioAutorizaciones = pagedList.getItemsRangeList();
				totalCount = pagedList.getTotalItems();
				
				break;
			case 5:
				pagedList = reglaNegocioManager.obtenerReglasNegocioPagedList(reglaNegocio.ConceptoTarificacion, start, limit);
				listaReglaNegocioTarificacion = pagedList.getItemsRangeList();
				totalCount = pagedList.getTotalItems();
				
				break;
			}
		}else{
			return INPUT;
		}
		success = true;
		return SUCCESS;

	}

	/**
	 * Metodo <code>cargaComboTipo</code> con el que son llamado
	 * desde Struts todos los valores para cargar los combos de la cabecera de la libreria
	 * Validaciones y Concepto de Tarificacion
	 * 
	 * @return success
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String cargaComboTipo() throws Exception{
		boolean isDebugEnabled = log.isDebugEnabled();
		
		if(isDebugEnabled){
			log.debug("comboTipo="+comboTipo);
		}
		if(comboTipo.equals("validaciones")){
			if(isDebugEnabled){
				log.debug("entro a cargar combo de tipo de validaciones");
			}
			listaTipo = reglaNegocioManager.obtenerTiposValidaciones();
		}else if(comboTipo.equals("tarificacion")){
			if(isDebugEnabled){
				log.debug("entro a cargar combo de tipo de conceptos de tarificacion");
			}
			listaTipo = reglaNegocioManager.obtenerTiposConceptosTarificacion();	
		}else{
			return INPUT;
		}
		success=true;
		return SUCCESS;
	}
	
	@SuppressWarnings("static-access")
	public String insertaReglaNegocio() throws Exception{
		log.debug(""
				+ "\n#################################"
				+ "\n###### insertaReglaNegocio ######"
				);
		//libreria no se utiliza...se puede eliminar
		log.debug("Entro a insertar grid de librerias");
		log.debug("GRID--"+numeroGrid);
		log.debug("session.get('EXPRESION') = "+session.get("EXPRESION"));
        
        //////////////////////////////////////////////////////////////////////Validacion
        if(session.containsKey("CATALOGO_VARIABLES_TEMPORALES_PRODUCTO"))
            listaReglaNegocioVariables = (List<ReglaNegocioVO>) session.get("CATALOGO_VARIABLES_TEMPORALES_PRODUCTO");
        
        log.debug(":::: listaReglaNegocioVariables :::: " + listaReglaNegocioVariables);
        
        if (listaReglaNegocioVariables == null) {
            listaReglaNegocioVariables = reglaNegocioManager.obtenerReglasNegocio(reglaNegocio.VariableTemporal);
        }
        
        agregarVariableTemp = "true";
        if (listaReglaNegocioVariables != null) {
            for (ReglaNegocioVO regla : listaReglaNegocioVariables)
            {    
                if (nombreCabecera != null && nombreCabecera.equalsIgnoreCase(regla.getNombre())) {
                    mensajeDelAction = "El nombre de la regla ya existe";
                    mensajeValidacion = "El nombre de la regla ya existe";
                    log.debug(":::: Ya existe regla!!!!!!!!!!!!!!!!!!!!!!!!!! :::: ");
                    agregarVariableTemp = "false";
                    break;
                }
            }    
        }
        log.debug(":::: agregarVariableTemp :::: " + agregarVariableTemp);
        log.debug(":::: mensajeDelAction :: " + mensajeDelAction);
        log.debug(":::: mensajeValidacion :: " + mensajeValidacion);
        ////////////////////////////////////////////////////////////////////////////////
                
		success=true;
		ReglaNegocioVO negocio = null;
        
		if (numeroGrid != null && "true".equals(agregarVariableTemp)) {
			switch (Integer.parseInt(numeroGrid)) {
			case 1:
				if(session.containsKey("EXPRESION")){
					int codigoExpresion = (Integer)session.get("EXPRESION");
					if(success){						
						negocio = new ReglaNegocioVO();
						negocio.setCodigo(reglaNegocio.VariableTemporal);
						negocio.setCodigoExpresion(Integer.toString(codigoExpresion));
						negocio.setNombre(nombreCabecera);
						negocio.setDescripcion(descripcionCabecera);
                        log.debug("CODIGO---"+negocio.getCodigo());
						log.debug("NOMBRE---"+negocio.getNombre());
						log.debug("DESCRIPCION---"+negocio.getDescripcion());
						log.debug("CD EXPRESION---"+negocio.getCodigoExpresion());
						reglaNegocioManager.insertarReglaNegocio(negocio);
						
					}
				}
				break;
			case 2:
				if(session.containsKey("EXPRESION")){
					int codigoExpresion = (Integer)session.get("EXPRESION");
					if(success){						
						negocio = new ReglaNegocioVO();
						negocio.setCodigo(reglaNegocio.Validacion);
						negocio.setCodigoExpresion(Integer.toString(codigoExpresion));
						negocio.setNombre(nombreCabecera);
						negocio.setDescripcion(descripcionCabecera);
						negocio.setTipo(tipo);
						negocio.setMensaje(mensaje);
                        log.debug("CODIGO---"+negocio.getCodigo());
						log.debug("NOMBRE VALIDACION---"+negocio.getNombre());
						log.debug("DESCRIPCION VALIDACION---"+negocio.getDescripcion());
						log.debug("CD EXPRESION VALIDACION---"+negocio.getCodigoExpresion());
						log.debug("TIPO VALICION---"+negocio.getTipo());
						log.debug("TIPO DESCRIPCION---"+tipoDescripcion);
						log.debug("MENSAJE VALICION---"+negocio.getMensaje());
						reglaNegocioManager.insertarReglaNegocio(negocio);
						
					}
				}
				break;
				
			case 3:			
				if(session.containsKey("EXPRESION")){
					int codigoExpresion = (Integer)session.get("EXPRESION");
					if(success){						
						negocio = new ReglaNegocioVO();
						negocio.setCodigo(reglaNegocio.Condicion);
						negocio.setCodigoExpresion(Integer.toString(codigoExpresion));
						negocio.setNombre(nombreCabecera);
						negocio.setDescripcion(descripcionCabecera);
                        log.debug("CODIGO---"+negocio.getCodigo());
						log.debug("NOMBRE---"+negocio.getNombre());
						log.debug("DESCRIPCION---"+negocio.getDescripcion());
						log.debug("CD EXPRESION---"+negocio.getCodigoExpresion());
						reglaNegocioManager.insertarReglaNegocio(negocio);						
					}
				}
				break;
			case 4:
				if(session.containsKey("EXPRESION")){
					int codigoExpresion = (Integer)session.get("EXPRESION");
					if(success){						
						negocio = new ReglaNegocioVO();
						negocio.setCodigo(reglaNegocio.Autorizacion);
						negocio.setCodigoExpresion(Integer.toString(codigoExpresion));
						negocio.setNombre(nombreCabecera);
						negocio.setDescripcion(descripcionCabecera);
						negocio.setNivel(nivel);
                        log.debug("CODIGO---"+negocio.getCodigo());
						log.debug("NOMBRE---"+negocio.getNombre());
						log.debug("DESCRIPCION---"+negocio.getDescripcion());
						log.debug("NIVEL---"+negocio.getNivel());
						log.debug("CD EXPRESION---"+negocio.getCodigoExpresion());
						reglaNegocioManager.insertarReglaNegocio(negocio);						
					}
				}
				break;
				
			case 5:
				if(session.containsKey("EXPRESION")){
					int codigoExpresion = (Integer)session.get("EXPRESION");					
					if(success){						
						negocio = new ReglaNegocioVO();
						negocio.setCodigo(reglaNegocio.ConceptoTarificacion);
						negocio.setCodigoExpresion(Integer.toString(codigoExpresion));
						negocio.setNombre(nombreCabecera);
						negocio.setDescripcion(descripcionCabecera);
						negocio.setTipo(tipo);
                        log.debug("CODIGO---"+negocio.getCodigo());
						log.debug("NOMBRE---"+negocio.getNombre());
						log.debug("DESCRIPCION---"+negocio.getDescripcion());
						log.debug("TIPO TARIFICACION---"+negocio.getTipo());
						log.debug("TIPO DESCRIPCION---"+tipoDescripcion);
						log.debug("CD EXPRESION---"+negocio.getCodigoExpresion());
						reglaNegocioManager.insertarReglaNegocio(negocio);						
					}
				}
				break;
				
			}
		}else{
			success = false;
			return SUCCESS;
		}
		
		success = true;
		log.debug(""
				+ "\n###### insertaReglaNegocio ######"
				+ "\n#################################"
				);
		return SUCCESS;
	}
	
	
	@SuppressWarnings("static-access")
	public String insertaReglaNegocioExp() throws Exception{
		log.debug(""
				+ "\n####################################"
				+ "\n###### insertaReglaNegocioExp ######"
				);
		//libreria no se utiliza...se puede eliminar
		log.debug("Entro a insertar grid de librerias");
		log.debug("GRID--"+numeroGrid);
		log.debug("session.get('EXPRESION') = "+session.get("EXPRESION"));

        log.debug(":::: mensajeDelAction :: " + mensajeDelAction);
        log.debug(":::: mensajeValidacion :: " + mensajeValidacion);
        ////////////////////////////////////////////////////////////////////////////////
                
		success=true;
		ReglaNegocioVO negocio = null;
        
		if (numeroGrid != null) {
			switch (Integer.parseInt(numeroGrid)) {
			case 1:
				if(session.containsKey("EXPRESION")){
					int codigoExpresion = (Integer)session.get("EXPRESION");
					if(success){						
						negocio = new ReglaNegocioVO();
						negocio.setCodigo(reglaNegocio.VariableTemporal);
						negocio.setCodigoExpresion(Integer.toString(codigoExpresion));
						negocio.setNombre(nombreCabecera);
						negocio.setDescripcion(descripcionCabecera);
                        log.debug("CODIGO---"+negocio.getCodigo());
						log.debug("NOMBRE---"+negocio.getNombre());
						log.debug("DESCRIPCION---"+negocio.getDescripcion());
						log.debug("CD EXPRESION---"+negocio.getCodigoExpresion());
						reglaNegocioManager.insertarReglaNegocio(negocio);
						
					}
				}
				break;
			case 2:
				if(session.containsKey("EXPRESION")){
					int codigoExpresion = (Integer)session.get("EXPRESION");
					if(success){						
						negocio = new ReglaNegocioVO();
						negocio.setCodigo(reglaNegocio.Validacion);
						negocio.setCodigoExpresion(Integer.toString(codigoExpresion));
						negocio.setNombre(nombreCabecera);
						negocio.setDescripcion(descripcionCabecera);
						negocio.setTipo(tipo);
						negocio.setMensaje(mensaje);
                        log.debug("CODIGO---"+negocio.getCodigo());
						log.debug("NOMBRE VALIDACION---"+negocio.getNombre());
						log.debug("DESCRIPCION VALIDACION---"+negocio.getDescripcion());
						log.debug("CD EXPRESION VALIDACION---"+negocio.getCodigoExpresion());
						log.debug("TIPO VALICION---"+negocio.getTipo());
						log.debug("TIPO DESCRIPCION---"+tipoDescripcion);
						log.debug("MENSAJE VALICION---"+negocio.getMensaje());
						reglaNegocioManager.insertarReglaNegocio(negocio);
						
					}
				}
				break;
				
			case 3:			
				if(session.containsKey("EXPRESION")){
					int codigoExpresion = (Integer)session.get("EXPRESION");
					if(success){						
						negocio = new ReglaNegocioVO();
						negocio.setCodigo(reglaNegocio.Condicion);
						negocio.setCodigoExpresion(Integer.toString(codigoExpresion));
						negocio.setNombre(nombreCabecera);
						negocio.setDescripcion(descripcionCabecera);
                        log.debug("CODIGO---"+negocio.getCodigo());
						log.debug("NOMBRE---"+negocio.getNombre());
						log.debug("DESCRIPCION---"+negocio.getDescripcion());
						log.debug("CD EXPRESION---"+negocio.getCodigoExpresion());
						reglaNegocioManager.insertarReglaNegocio(negocio);						
					}
				}
				break;
			case 4:
				if(session.containsKey("EXPRESION")){
					int codigoExpresion = (Integer)session.get("EXPRESION");
					if(success){						
						negocio = new ReglaNegocioVO();
						negocio.setCodigo(reglaNegocio.Autorizacion);
						negocio.setCodigoExpresion(Integer.toString(codigoExpresion));
						negocio.setNombre(nombreCabecera);
						negocio.setDescripcion(descripcionCabecera);
						negocio.setNivel(nivel);
                        log.debug("CODIGO---"+negocio.getCodigo());
						log.debug("NOMBRE---"+negocio.getNombre());
						log.debug("DESCRIPCION---"+negocio.getDescripcion());
						log.debug("NIVEL---"+negocio.getNivel());
						log.debug("CD EXPRESION---"+negocio.getCodigoExpresion());
						reglaNegocioManager.insertarReglaNegocio(negocio);						
					}
				}
				break;
				
			case 5:
				if(session.containsKey("EXPRESION")){
					int codigoExpresion = (Integer)session.get("EXPRESION");					
					if(success){						
						negocio = new ReglaNegocioVO();
						negocio.setCodigo(reglaNegocio.ConceptoTarificacion);
						negocio.setCodigoExpresion(Integer.toString(codigoExpresion));
						negocio.setNombre(nombreCabecera);
						negocio.setDescripcion(descripcionCabecera);
						negocio.setTipo(tipo);
                        log.debug("CODIGO---"+negocio.getCodigo());
						log.debug("NOMBRE---"+negocio.getNombre());
						log.debug("DESCRIPCION---"+negocio.getDescripcion());
						log.debug("TIPO TARIFICACION---"+negocio.getTipo());
						log.debug("TIPO DESCRIPCION---"+tipoDescripcion);
						log.debug("CD EXPRESION---"+negocio.getCodigoExpresion());
						reglaNegocioManager.insertarReglaNegocio(negocio);						
					}
				}
				break;
				
			}
		}else{
			success = false;
			return SUCCESS;
		}
		
		success = true;
		log.debug(""
				+ "\n###### insertaReglaNegocioExp ######"
				+ "\n####################################"
				);
		return SUCCESS;
	}
	
	/**
	 * Metodo <code>cargaExpresionReglaNegocio</code> con el que son llamado
	 * desde Struts todos los valores para cargar la expresion asociada a la cabecera de las librerias
	 * 
	 * @return success
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String cargaExpresionReglaNegocio()throws Exception{
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("Entro a cargar lista de expresiones regla de negocios");
		}
		//if(Integer.toString(codigoExpresion) != null){			
			listaExpresionesReglaNegocio = cargaExpresion(codigoExpresion);
			listaVariablesReglaNegocio = cargaVariablesExpresion (codigoExpresion);
			session.put("CATALOGO_VARIABLES", listaVariablesReglaNegocio);
			if(isDebugEnabled){
				log.debug("SIZE"+listaVariablesReglaNegocio.size());
				log.debug("CODIGO EXPRESION---"+listaExpresionesReglaNegocio.get(0).getCodigoExpresion());
				log.debug("EXPRESION---"+listaExpresionesReglaNegocio.get(0).getOtExpresion());
				log.debug("CD EXPRESION---"+listaExpresionesReglaNegocio.get(0).getCodigoExpresion());
			}
		//}
		return SUCCESS;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public String borraVariableTemporal() throws Exception{
		
		if(log.isDebugEnabled())log.debug("borraVariableTemporal - cdVariat: "+ descripcionVariableProducto);
		try{
			mensajeDelAction = reglaNegocioManager.borraVarTmp(descripcionVariableProducto);
			
		}catch(Exception ae){
			log.error("Error en el metodo guardaPresiniestroGastosFunerarios "+ae.getMessage(),ae);
			mensajeDelAction = "Error al borrar la variable temporal. Consulte a soporte";
			success = false;
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	
	//Getters y Setters
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setReglaNegocioManager(ReglaNegocioManager reglaNegocioManager) {
		this.reglaNegocioManager = reglaNegocioManager;
	}

	public ReglaNegocio getReglaNegocio() {
		return reglaNegocio;
	}

	public void setReglaNegocio(ReglaNegocio reglaNegocio) {
		this.reglaNegocio = reglaNegocio;
	}

	public List<ReglaNegocioVO> getListaReglaNegocioVariables() {
		return listaReglaNegocioVariables;
	}

	public void setListaReglaNegocioVariables(
			List<ReglaNegocioVO> listaReglaNegocioVariables) {
		this.listaReglaNegocioVariables = listaReglaNegocioVariables;
	}

	public List<ReglaNegocioVO> getListaReglaNegocioValidaciones() {
		return listaReglaNegocioValidaciones;
	}

	public void setListaReglaNegocioValidaciones(
			List<ReglaNegocioVO> listaReglaNegocioValidaciones) {
		this.listaReglaNegocioValidaciones = listaReglaNegocioValidaciones;
	}

	public List<ReglaNegocioVO> getListaReglaNegocioCondiciones() {
		return listaReglaNegocioCondiciones;
	}

	public void setListaReglaNegocioCondiciones(
			List<ReglaNegocioVO> listaReglaNegocioCondiciones) {
		this.listaReglaNegocioCondiciones = listaReglaNegocioCondiciones;
	}

	public List<ReglaNegocioVO> getListaReglaNegocioAutorizaciones() {
		return listaReglaNegocioAutorizaciones;
	}

	public void setListaReglaNegocioAutorizaciones(
			List<ReglaNegocioVO> listaReglaNegocioAutorizaciones) {
		this.listaReglaNegocioAutorizaciones = listaReglaNegocioAutorizaciones;
	}

	public List<ReglaNegocioVO> getListaReglaNegocioTarificacion() {
		return listaReglaNegocioTarificacion;
	}

	public void setListaReglaNegocioTarificacion(
			List<ReglaNegocioVO> listaReglaNegocioTarificacion) {
		this.listaReglaNegocioTarificacion = listaReglaNegocioTarificacion;
	}

	public String getNumeroGrid() {
		return numeroGrid;
	}

	public void setNumeroGrid(String numeroGrid) {
		this.numeroGrid = numeroGrid;
	}	

	public List<LlaveValorVO> getListaTipo() {
		return listaTipo;
	}

	public void setListaTipo(List<LlaveValorVO> listaTipo) {
		this.listaTipo = listaTipo;
	}

	public String getComboTipo() {
		return comboTipo;
	}

	public void setComboTipo(String comboTipo) {
		this.comboTipo = comboTipo;
	}	

	public String getNombreCabecera() {
		return nombreCabecera;
	}

	public void setNombreCabecera(String nombreCabecera) {
		this.nombreCabecera = nombreCabecera;
	}

	public String getDescripcionCabecera() {
		return descripcionCabecera;
	}

	public void setDescripcionCabecera(String descripcionCabecera) {
		this.descripcionCabecera = descripcionCabecera;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public String getLibreria() {
		return libreria;
	}

	public void setLibreria(String libreria) {
		this.libreria = libreria;
	}

	public int getCodigoExpresion() {
		return codigoExpresion;
	}

	public void setCodigoExpresion(int codigoExpresion) {
		this.codigoExpresion = codigoExpresion;
	}

	public String getTipoDescripcion() {
		return tipoDescripcion;
	}

	public void setTipoDescripcion(String tipoDescripcion) {
		this.tipoDescripcion = tipoDescripcion;
	}

	public List<ExpresionVO> getListaExpresionesReglaNegocio() {
		return listaExpresionesReglaNegocio;
	}

	public void setListaExpresionesReglaNegocio(
			List<ExpresionVO> listaExpresionesReglaNegocio) {
		this.listaExpresionesReglaNegocio = listaExpresionesReglaNegocio;
	}

	public List<VariableVO> getListaVariablesReglaNegocio() {
		return listaVariablesReglaNegocio;
	}

	public void setListaVariablesReglaNegocio(
			List<VariableVO> listaVariablesReglaNegocio) {
		this.listaVariablesReglaNegocio = listaVariablesReglaNegocio;
	}

	public List<ReglaNegocioVO> getListaReglaNegocioVariablesProducto() {
		return listaReglaNegocioVariablesProducto;
	}

	public void setListaReglaNegocioVariablesProducto(
			List<ReglaNegocioVO> listaReglaNegocioVariablesProducto) {
		this.listaReglaNegocioVariablesProducto = listaReglaNegocioVariablesProducto;
	}
	
	public String getCodigoVariableProducto() {
		return codigoVariableProducto;
	}
	
	public void setCodigoVariableProducto(String codigoVariableProducto) {
		this.codigoVariableProducto = codigoVariableProducto;
	}
	
	public String getDescripcionVariableProducto() {
		return descripcionVariableProducto;
	}
	
	public void setDescripcionVariableProducto(String descripcionVariableProducto) {
		this.descripcionVariableProducto = descripcionVariableProducto;
	}
	public String getCdramo() {
		return cdramo;
	}
	public void setCdramo(String cdramo) {
		this.cdramo = cdramo;
	}
    /**
     * @return the agregarVariableTemp
     */
    public String getAgregarVariableTemp() {
        return agregarVariableTemp;
    }
    /**
     * @param agregarVariableTemp the agregarVariableTemp to set
     */
    public void setAgregarVariableTemp(String agregarVariableTemp) {
        this.agregarVariableTemp = agregarVariableTemp;
    }
    /**
     * @return the mensajeDelAction
     */
    public String getMensajeDelAction() {
        return mensajeDelAction;
    }
    /**
     * @param mensajeDelAction the mensajeDelAction to set
     */
    public void setMensajeDelAction(String mensajeDelAction) {
        this.mensajeDelAction = mensajeDelAction;
    }
    /**
     * @return the mensajeValidacion
     */
    public String getMensajeValidacion() {
        return mensajeValidacion;
    }
    /**
     * @param mensajeValidacion the mensajeValidacion to set
     */
    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
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

	

}
