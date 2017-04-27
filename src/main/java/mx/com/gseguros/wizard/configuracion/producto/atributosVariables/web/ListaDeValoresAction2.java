package mx.com.gseguros.wizard.configuracion.producto.atributosVariables.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal.service.PagedList;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.model.ClavesVO;
import mx.com.gseguros.wizard.configuracion.producto.model.ListaDeValoresVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.service.ListaDeValoresManager;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.DatosClaveAtributoVO;
import mx.com.gseguros.wizard.configuracion.producto.web.Padre;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * @author Edgar Perez
 * @version 2.0
 * @since 1.0
 * 
 * Clase que contiene los metodos de cargar y agregar a la lista de atributos
 * variables del producto asi como la validacion de las listas de los catalogos
 * mostrados
 * 
 */
public class ListaDeValoresAction2 extends Padre {

	private static final long serialVersionUID = -5072133136069506205L;
	private static final transient Log log = LogFactory
			.getLog(ListaDeValoresAction2.class);

	private static final String CARGAMANUAL = "CARGAMANUAL";

	/**
	 * Manager con implementacion de Endpoint para la consulta a BD.
	 */
	private ListaDeValoresManager listaDeValoresManager;
	
	/**
	 * Manager con implementacion de JdbcTemplate para la consulta a BD.
	 */
	private ListaDeValoresManager listaDeValoresManagerJdbcTemplate;
	/**
	 * Atributo de respuesta interpretado por strust con la lista de beans tipo
	 * ListaDeValoresVO con los valores de la consulta para el catalogo 1.
	 */
	private List<LlaveValorVO> listaCargaManual;

	/**
	 * Atributo de respuesta interpretado por strust con la lista de beans tipo
	 * ListaDeValoresVO con los valores de la consulta para el catalogo 1.
	 */
	private List<ListaDeValoresVO> listaDeValores;

	/**
	 * Atributo de respuesta interpretado por strust con la lista de beans tipo
	 * ListaDeValoresVO con los valores de la consulta para el catalogo 2.
	 */
	// private List<ClavesVO> listaUnaYcincoClaves;
	/**
	 * Atributo de respuesta interpretado por strust con la lista de beans tipo
	 * ListaDeValoresVO con los valores de la consulta para el catalogo 1.
	 */
	private List<ListaDeValoresVO> listaDeValores2;

	/**
	 * Atributo de respuesta interpretado por strust como la correcta
	 * realizacion de. cada peticion.
	 */
	private boolean success;

	/**
	 * Atributo agregado por struts que contiene el nombre de la lista de
	 * valores.
	 */
	private String nombre;

	/**
	 * Atributo agregado por struts que contiene la descripcion de la lista de
	 * valores.
	 */
	private String descripcion;

	/**
	 * Atributo agregado por struts que contiene el numero de la lista de
	 * valores.
	 */
	private String numero;

	/**
	 * Atributo agregado por struts que indica si envia a la tabla error.
	 */
	private String enviaTablaError;

	/**
	 * Atributo agregado por struts que indica si modifica el valor.
	 */
	private String modificaValor;

	/**
	 * Atributo agregado por struts que contiene la clave de la lista de
	 * valores.
	 */
	private String clave;

	/**
	 * Atributo agregado por struts que contiene el formato de la clave de la
	 * lista de valores
	 */
	private String formatoClave;

	/**
	 * Atributo agregado por struts que contiene el numero maximo de la clave de
	 * la lista de valores.
	 */
	private String maximoClave;

	/**
	 * Atributo agregado por struts que contiene el numero minimo de la clave de
	 * la lista de valores.
	 */
	private String minimoClave;

	/**
	 * Atributo agregado por struts que contiene la descripcion de la lista de
	 * valores
	 */
	private String descripcionFormato;

	/**
	 * Atributo agregado por struts que contiene el formato de la descripcion de
	 * la lista de valores
	 */
	private String formatoDescripcion;

	/**
	 * Atributo agregado por struts que contiene el numero maximo de la
	 * descripcion de la lista de valores.
	 */
	private String maximoDescripcion;

	/**
	 * Atributo agregado por struts que contiene el numero minimo de la
	 * descripcion de la lista de valores.
	 */
	private String minimoDescripcion;

	/**
	 * Atributo agregado por struts que contiene la descripcion del catalogo 1
	 * de la lista de valores.
	 */
	private String catalogo1;

	/**
	 * Atributo agregado por struts que contiene la descripcion del catalogo 2
	 * de la lista de valores.
	 */
	private String catalogo2;

	/**
	 * Atributo agregado por struts que contiene la clave de la dependencia de
	 * la lista de valores.
	 */
	private String claveDependencia;

	/**
	 * Atributo agregado por struts que contiene el valor de la clave en la
	 * lista de carga manual.
	 */
	private String valorClave;

	/**
	 * Atributo agregado por struts que contiene el valor de la descripcion en
	 * la lista de carga manual.
	 */
	private String valorDescripcion;

	/**
	 * Atributo agregado por struts que contiene el valor de la clave en la
	 * lista de carga manual.
	 */
	private String otClave;

	/**
	 * Atributo agregado por struts que contiene el valor de la descripcion en
	 * la lista de carga manual.
	 */
	private String otValor;

	/**
	 * Atributo agregado por struts que contiene el identificador del elemento
	 * seleccionado en el grid de la lista de carga manual.
	 */
	private String selectedId;

	/**
	 * valor con el que es cargada la lista de valores
	 */
	// private String valor; eliminar
	private String tabla;
	private String nmTabla;
	private String tipoTabla;
	private String tipoTransaccion;

	private List<ListaDeValoresVO> editaListaDeValores;
	private List<DatosClaveAtributoVO> editarClaveListaDeValores;
	private ListaDeValoresVO cabeceraListaDeValores;
	private ListaDeValoresVO claveListaDeValores;
	private ListaDeValoresVO descripcionListaDeValores;
	private String hiddenRadioClave;
	private String hiddenRadioDescripcion;
	private String radioClave;
	private String radioDescripcion;
	private String codigoTabla;
	private List<LlaveValorVO> listaClavesDependencia;

    private String mensajeRespuesta;
    
    
    /**
     * Valores Para paginacion del grid de la Carga Manual de valores de Una Clave
     */
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
		if (isDebugEnabled) {
			log.debug("Entro execute de lista de valores");
		}
		return INPUT;
	}

	/**
	 * Metodo <code>listaDeValoresCatalogo</code> con el que son llamado desde
	 * Struts todos los valores del catalogo.
	 * 
	 * @return success
	 * @throws Exception
	 */
	public String listaDeValoresCatalogo() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if (isDebugEnabled) {
			log.debug("Entro a cargar catalogo 1");
		}
		// if (!session.containsKey("CATALOGO1")) {
		// valor="5";eliminar
		listaDeValores = listaDeValoresManagerJdbcTemplate.listaDeValoresCatalogo("1");
		// session.put("CATALOGO1", listaDeValores);
		// } else {
		// listaDeValores = (List<ListaDeValoresVO>) session.get("CATALOGO1");
		// }

		success = true;

		return SUCCESS;
	}

	/**
	 * Metodo <code>listaDeValoresCatalogo2</code> con el que son llamado
	 * desde Struts todos los valores del catalogo 2.
	 * 
	 * @return success
	 * @throws Exception
	 */

	public String listaDeValoresCatalogo2() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if (isDebugEnabled) {
			log.debug("Entro a cargar catalogo 2");
		}
		// if (!session.containsKey("CATALOGO2")) {
		// valor="5";
		listaDeValores2 = listaDeValoresManagerJdbcTemplate.listaDeValoresCatalogo("5");
		// session.put("CATALOGO2", listaDeValores2);
		// } else {
		// listaDeValores2 = (List<ListaDeValoresVO>) session.get("CATALOGO2");
		// }

		success = true;

		return SUCCESS;
	}

	/**
	 * Metodo <code>deshabilitarValorCombo</code> con el que deshabilita los
	 * valores del catalogo 1 y dos cuando son seleccionados.
	 * 
	 * @return SUCCESS
	 * @throws Exception
	 */
	
	public String guardarListaDeValores() throws Exception {
		
        boolean isDebugEnabled = log.isDebugEnabled();
        
		List<ListaDeValoresVO> catalogos1 = listaDeValoresManagerJdbcTemplate
				.listaDeValoresCatalogo("1");
		String codigoCatalogo1 = "";
        //boolean claveRepetida = false;
        
        if (isDebugEnabled) {
            log.debug("------nombre----------------------->" + nombre);
            log.debug("------descripcion------------------>" + descripcion);
        }
        
        if (tipoTransaccion == null || StringUtils.isBlank(tipoTransaccion)) {
			tipoTransaccion = "1";// mandar este valor desde la jsp (1=insert,
			// 2=update)
		}
        /////////////////////////////Se valida que no se repitan los nombres tanto de clave como de descripcion
        if(tipoTransaccion.equals("1")){
	        for (ListaDeValoresVO clave : catalogos1) {
	            if (isDebugEnabled) {
	                log.debug("------clave----->" + clave.getCdCatalogo1());
	                log.debug("------descr----->" + clave.getDsCatalogo1());
	            }
	            
	            if (clave.getCdCatalogo1().equalsIgnoreCase(nombre)) {
	                mensajeRespuesta = "Lista de valores no agregada: Ya existe una Tabla con la clave " 
	                    + clave.getCdCatalogo1();
	                
	                if (isDebugEnabled) {
	                    log.debug("------La clave se repite !!!!!!!" + mensajeRespuesta);
	                }
	                
	                success = false;
	                return SUCCESS;
	            } 
	            
	            if (clave.getDsCatalogo1().equalsIgnoreCase(descripcion)) {
	                mensajeRespuesta = "Lista de valores no agregada: Ya existe una Tabla con el nombre " 
	                    + clave.getDsCatalogo1();
	                
	                if (isDebugEnabled) {
	                    log.debug("------El nombre se repite !!!!!!!" + mensajeRespuesta);
	                }
	                
	                success = false;
	                return SUCCESS;
	            }
	        }
		}
        ////////////////////////////////Se valida tamanio de las claves y las descripciones
        if(StringUtils.isNotBlank(minimoClave) && StringUtils.isNotBlank(maximoClave)){
        	if (Integer.parseInt(minimoClave) > Integer.parseInt(maximoClave)) {
        		mensajeRespuesta = "El tama�o m�nimo de la Clave no puede ser mayor al m�ximo";
        		success = false;
        		return SUCCESS;
        	}
        }
        
        if(StringUtils.isNotBlank(minimoDescripcion) && StringUtils.isNotBlank(maximoDescripcion)){
        	if (Integer.parseInt(minimoDescripcion) > Integer.parseInt(maximoDescripcion)) {
        		mensajeRespuesta = "El tama�o m�nimo de la Descripci�n no puede ser mayor al m�ximo";
        		success = false;
        		return SUCCESS;
        	}
        }
        ///////////////////////////////////////////////////////////////////////////////////
        
		for (ListaDeValoresVO clave : catalogos1) {
            if (clave.getDsCatalogo1().equals(catalogo1)) {
				codigoCatalogo1 = clave.getCdCatalogo1();
				break;
			}
		}

		List<ListaDeValoresVO> catalogo = listaDeValoresManagerJdbcTemplate
				.listaDeValoresCatalogo("5");

		String codigoCatalogo2 = "";
		for (ListaDeValoresVO clave : catalogo) {

			if (clave.getDsCatalogo1().equals(catalogo2)) {
				codigoCatalogo2 = clave.getCdCatalogo1();
				break;
			}
		}
		if (enviaTablaError == null) {
			enviaTablaError = "N";

		}
		if (modificaValor == null) {
			modificaValor = "N";

		}

		tipoTabla = "1";// pasar el valor desde jsp (1=1clave, 5=5Claves)
		
		ListaDeValoresVO unaClave = new ListaDeValoresVO();
		if (nmTabla != null && StringUtils.isNotBlank(nmTabla)) {
			unaClave.setNumeroTabla(nmTabla);
		} else {
			unaClave.setNumeroTabla(null);
		}
		// tabla PL P_GUARDA_TABLA
		unaClave.setNombre(nombre);
		unaClave.setDescripcion(descripcion);
		// unaClave.setNumeroTabla(nmTabla);
		unaClave.setEnviarTablaErrores(enviaTablaError);
		unaClave.setModificaValores(modificaValor);
		unaClave.setOttipoac("U");
		unaClave.setOttipotb(tipoTabla);
		unaClave.setClnatura("1");
		unaClave.setCdCatalogo1(codigoCatalogo1);
		unaClave.setCdCatalogo2(codigoCatalogo2);
		unaClave.setClaveDependencia(claveDependencia);

		// descripcion PL GUARDA_ATRIBUTOS
		unaClave.setCdAtribu("1");// siempre es 1 en la BD para insertar
		// descripcion de una clave
		unaClave.setDescripcionFormato(descripcionFormato);
		if (radioDescripcion != null
				&& StringUtils.isNotBlank(radioDescripcion)) {
			unaClave.setFormatoDescripcion(radioDescripcion);
		} else {
			if (StringUtils.isNotBlank(hiddenRadioDescripcion))
				unaClave.setFormatoDescripcion(hiddenRadioDescripcion);
			else
				unaClave.setFormatoDescripcion("A");
		}

		unaClave.setMaximoDescripcion(maximoDescripcion);
		unaClave.setMinimoDescripcion(minimoDescripcion);
		// log.debug("NMTABLA*--------->"+nmTabla);
		// nmTabla="1";//**********provisional para prueba***********
		nmTabla = listaDeValoresManager.guardaListaValores(unaClave);// valor
		// autogenerado
		// al
		// guardar
		
		if (isDebugEnabled) {
			log.debug("NMTABLAgenerado*--------->" + nmTabla);
		}

		if (nmTabla != null && StringUtils.isNotBlank(nmTabla)) {
			// clave 1 y 5 PL P_GUARDA_CLAVES
			ClavesVO claves = new ClavesVO();

			claves.setClave(clave);
			if (radioClave != null && StringUtils.isNotBlank(radioClave)) {
				claves.setFormatoClave(radioClave);
			} else {
				if (StringUtils.isNotBlank(hiddenRadioClave))
					claves.setFormatoClave(hiddenRadioClave);
				else
					claves.setFormatoClave("A");
			}
			claves.setMaximoClave(maximoClave);
			claves.setMinimoClave(minimoClave);

			if (isDebugEnabled) {
				log.debug("***hiddenRadioClave--------->" + hiddenRadioClave);
				log.debug("***hiddenRadioDescripcion--------->"
						+ hiddenRadioDescripcion);
				log.debug("***radioDescripcion--------->" + radioDescripcion);
				log.debug("***radioClave--------->" + radioClave);
				log.debug("NOMBRE--------->" + nombre);
				log.debug("DESCRIPCION--------->" + descripcion);
				log.debug("NUMERO--------->" + unaClave.getNumeroTabla());// lista.getNumeroTabla()
				log.debug("NMTABLA--------->" + nmTabla);
				log.debug("ENVIAERROR--------->" + enviaTablaError);
				log.debug("MODIFICAVALOR--------->" + modificaValor);
				log.debug("CDCATALOGO1--------->" + codigoCatalogo1);
				log.debug("CDCATALOGO2--------->" + codigoCatalogo2);
				log.debug("CLAVEDEPENDENCIA--------->" + claveDependencia);
				log.debug("CLAVE--------->" + clave);
				log.debug("TIPTRAN--------->" + tipoTransaccion);// lista.getTipTran()
				log.debug("OTTIPOAC--------->" + unaClave.getOttipoac());// lista.getOttipoac()
				// log.debug("FORMATOCLAVE--------->"+formatoClave);
				log.debug("MINCLAVE--------->" + minimoClave);
				log.debug("MAXCLAVE--------->" + maximoClave);
				log.debug("DESCRIPCIONFORMATO--------->" + descripcionFormato);
				// log.debug("FORMATODESCRIPCION--------->"+formatoDescripcion);
				log.debug("MINDESCRIPCION--------->" + minimoDescripcion);
				log.debug("MAXDESCRIPCION--------->" + maximoDescripcion);
				log.debug("CLAVE objet--------->" + claves.getClave());
				log.debug("MINCLAVE objet--------->" + claves.getMinimoClave());
				log.debug("MAXCLAVE objet--------->" + claves.getMaximoClave());
				// log.debug("objet--------->"+claves);
				log.debug("formatoclave objet--------->"
						+ claves.getFormatoClave());
				log.debug("formatodesc objet--------->"
						+ unaClave.getFormatoDescripcion());

			}

			listaDeValoresManager.guardaClaveListaDeValores(claves,
					tipoTransaccion, nmTabla);


			List<ListaDeValoresVO> listaUnaDescripcion = new ArrayList<ListaDeValoresVO>();
			listaUnaDescripcion.add(unaClave);

			listaDeValoresManager.guardaDescripcionListaDeValores(
					listaUnaDescripcion, tipoTransaccion, nmTabla);
			
			
			if(isDebugEnabled){
				log.debug("Lista listaCargaManual para agregar Claves-Valor: "+ listaCargaManual);
				log.debug("Lista listaClavesDependencia para eliminar Claves-Valor Sobrantes: "+ listaClavesDependencia);
			}
			
			List<LlaveValorVO> clavesNoInsertadas = null; 
			clavesNoInsertadas = listaDeValoresManager.guardaValoresCargaManual(listaCargaManual, nmTabla, listaClavesDependencia , nombre);
			log.debug("Las claves no insertadas son : " + clavesNoInsertadas);
			
			/**************************************************************************************************************************************
			 *  Si NO se quiere obtener los registros no insertados y mostrarlos en el mensaje de respuesta, 
			 * comentar el siguiente for y solo regresar el mensaje:"Datos Guardados correctamente, los registros duplicados no han sido guardados"
			 ****************************************************************************************************************************************/
			mensajeRespuesta = "Datos Guardados correctamente. Los registros duplicados no han sido guardados: [";
				try{
			if(clavesNoInsertadas != null && !clavesNoInsertadas.isEmpty()){

				for(LlaveValorVO noInsertada : clavesNoInsertadas){
					if(!mensajeRespuesta.endsWith("["))mensajeRespuesta += ",";
					mensajeRespuesta += noInsertada.getKey();
				}
				mensajeRespuesta += "]";
			}else {
				mensajeRespuesta = "Datos Guardados correctamente";
			}
			
				}catch(Exception e ){
					log.error("Error al Guardar los Valores de la tabla de 1 clave: "+e.getMessage(),e);
					
				}
			log.debug("MensajeRespuesta: "+ mensajeRespuesta);
			success = true;
			return SUCCESS;
		} else {
            mensajeRespuesta = "Lista de valores no agregada";
			success = false;
			return SUCCESS;
		}
	}

	/**
	 * Metodo <code>listaDeValoresCatalogo2</code> con el que son llamado
	 * desde Struts todos los valores del catalogo 2.
	 * 
	 * @return success
	 * @throws Exception
	 */
	public String listaDeValoresCargaManual() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if (isDebugEnabled) {
			log.debug("Entro a cargar grid lista de valores para tabla: "+ tabla);
		}

		if (StringUtils.isNotBlank(tabla) && !tabla.equals("undefined")) {
			PagedList pagedList = listaDeValoresManagerJdbcTemplate.listaValoresCargaManual(tabla, start , limit);
			listaCargaManual = pagedList.getItemsRangeList();
			setTotalCount(pagedList.getTotalItems());
		}
		
		if (listaCargaManual == null) listaCargaManual = new ArrayList<LlaveValorVO>();
		
		/*
		 * For Para pruebas de llenado
		 * for(int i=0;i<50;i++){
			LlaveValorVO tmp= new LlaveValorVO();
			tmp.setKey(Integer.toString(i));
			tmp.setValue(Integer.toString(i));
			listaCargaManual.add(tmp);
		}*/
		if (isDebugEnabled) log.debug("listaCargaManual=" + listaCargaManual);
		
		/*TODO
		 *Quitar codigo de sesion para la carga manual 
		 */
		//session.put(CARGAMANUAL, listaCargaManual);
		success = true;
		return SUCCESS;
	}

	/**
	 * Metodo <code>listaCargaManualGrid</code> con el que son cargados los
	 * valores manualmente a la lista de valores del producto en el grid.
	 * @deprecated Metodo que dejara de usarse ya que los valores ya no son mantenidos en session.
	 * @return success
	 * @throws Exception
	 */
	public String listaCargaManualGrid() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if (isDebugEnabled) {
			log.debug("Entro a cargar lista del grid + selectedId ="
					+ selectedId);
			log
					.debug("session.get('CARGAMANUAL') ="
							+ session.get(CARGAMANUAL));
		}

		/*
		 * Cuando se EDITA un valor y/o clave
		 */
		if (selectedId != null) {

			ArrayList<LlaveValorVO> temporal = (ArrayList<LlaveValorVO>) session
					.get(CARGAMANUAL);
			LlaveValorVO elemento = temporal.get(Integer.parseInt(selectedId));
			elemento.setKey(otClave);
			elemento.setValue(otValor);
			temporal.set(Integer.parseInt(selectedId), elemento);
			success = true;

		} 

		/*
		 * Cuando se AGREGA nuevo un valor y/o clave
		 */
		else {

			/*
			 * Cuando se crea el primer valor y/o clave de la lista
			 */
			if (session.get(CARGAMANUAL) == null) {

				ArrayList<LlaveValorVO> lista = new ArrayList<LlaveValorVO>();

				LlaveValorVO valor = new LlaveValorVO();

				valor.setKey(valorClave);
				valor.setValue(StringEscapeUtils.escapeHtml4(valorDescripcion));

				lista.add(valor);
				session.put(CARGAMANUAL, lista);
				success = true;
			} 
			
			/*
			 * Cuando se agrega un elemento (valor y clave) m�s a la lista 
			 */
			
			else {
				ArrayList<LlaveValorVO> temporal = (ArrayList<LlaveValorVO>) session
						.get(CARGAMANUAL);

				boolean valorLista = true;

				if (isDebugEnabled) {
					log.debug("temporal.isEmpty() = " + temporal.isEmpty());
				}
				/*
				 * Se aseguran que el ArrayList<LlaveValorVO> no est� vac�o 
				 */
				if (!temporal.isEmpty()) {
					for (LlaveValorVO temp : temporal) {
						if (isDebugEnabled) {
							// log.debug("OTCLAVE--- "+temp.getOtClave());
							// log.debug("VALORCALVE--- "+valorClave);
							log.debug("Entro a cargar lista del grid");
						}
						if ( temp.getKey().equals(valorClave) /*&& 
								temp.getValue().equals(valorDescripcion)*/ ) {
							valorLista = false;
							success = false;	// NUEVO
							break;
						}
					}
				}
				if (isDebugEnabled) {
					log.debug("valorLista = " + valorLista);
				}
				if (valorLista) {
					LlaveValorVO listaGrid = new LlaveValorVO();
					listaGrid.setKey(valorClave);
					listaGrid.setValue(StringEscapeUtils
							.escapeHtml4(valorDescripcion));
					temporal.add(listaGrid);
					session.put(CARGAMANUAL, temporal);
					success = true;
				}

			}

		}
		return SUCCESS;
	}

	/**
	 * Metodo <code>eliminarDelistaCargaManual</code> con el que son
	 * eliminados los valores manualmente de la lista de valores del producto en
	 * el grid.
	 * @deprecated Metodo que dejara de usarse porque los datos del grid, ya no son mantenidos en sesion
	 * @return success
	 * @throws Exception
	 */
	public String eliminarDelistaCargaManual() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if (isDebugEnabled) {
			log.debug("Entro a eliminar de la lista del grid");
			log.debug("valorClave" + valorClave);
		}
		if (valorClave != null) {
			ArrayList<LlaveValorVO> temporal = (ArrayList<LlaveValorVO>) ActionContext
					.getContext().getSession().get(CARGAMANUAL);

			boolean valorLista = false;

			LlaveValorVO remover = null;
			for (LlaveValorVO temp : temporal) {
				if (temp.getKey().equals(valorClave)) {

					remover = temp;
					
					ArrayList<LlaveValorVO> llavesEliminar = null;
					
					if(session.containsKey("LLAVES_ELIMINAR")) llavesEliminar = (ArrayList<LlaveValorVO>) session.get("LLAVES_ELIMINAR");
					if(llavesEliminar == null) llavesEliminar = new ArrayList<LlaveValorVO>();
					llavesEliminar.add(remover);
					
					session.put("LLAVES_ELIMINAR", llavesEliminar);
					
					valorLista = true;
					break;
				}
			}
			if (remover != null) {

				temporal.remove(remover);
				session.put(CARGAMANUAL, temporal);
				success = true;
			}
			if (valorLista) {
				session.put(CARGAMANUAL, temporal);
				success = true;
			}
		}
		return SUCCESS;
	}

	/**
	 * Metodo <code>borraSesionCargaManual</code> con el que es eliminada la
	 * sesion de carga manual en el grid.
	 * @deprecated MEtodo que dejara de usarse, a que los datos del grid yano son mantenidos en sesion
	 * @return success
	 * @throws Exception
	 */
	public String borraSesionCargaManual() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if (isDebugEnabled) {
			log.debug("borrando sesion");
		}
		if(session.containsKey(CARGAMANUAL))session.remove(CARGAMANUAL);
		//if(session.containsKey("LLAVES_ELIMINAR"))session.remove("LLAVES_ELIMINAR");
		
		success = true;
		return SUCCESS;
	}

	public String Cabecera() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if (isDebugEnabled) {
			log.debug("editarListaDeValores");
			log.debug("NUMEROTABLA-->" + nmTabla);
		}
		editaListaDeValores = new ArrayList<ListaDeValoresVO>();
		// ListaDeValoresVO ldvVO = new ListaDeValoresVO();

		ListaDeValoresVO ldvVO = null;
		if (nmTabla != null) {
			ldvVO = new ListaDeValoresVO();
			cabeceraListaDeValores = listaDeValoresManager
					.obtieneCabeceraListaDeValores(nmTabla);
			ldvVO.setNumeroTabla(nmTabla);
			ldvVO.setNombre(cabeceraListaDeValores.getNombre());
			ldvVO.setDescripcion(cabeceraListaDeValores.getDescripcion());
			ldvVO.setEnviarTablaErrores(cabeceraListaDeValores
					.getEnviarTablaErrores());
			ldvVO.setModificaValores(cabeceraListaDeValores
					.getModificaValores());
			ldvVO.setCdCatalogo1(cabeceraListaDeValores.getCdCatalogo1());
			ldvVO.setCdCatalogo2(cabeceraListaDeValores.getCdCatalogo2());
			ldvVO.setDsCatalogo1(cabeceraListaDeValores.getDsCatalogo1());
			ldvVO.setDsCatalogo2(cabeceraListaDeValores.getDsCatalogo2());
			ldvVO.setClaveDependencia(cabeceraListaDeValores.getClaveDependencia());
			claveListaDeValores = listaDeValoresManager
					.obtieneClaveListaDeValores(nmTabla);
			ldvVO.setClave(claveListaDeValores.getClave());
			ldvVO.setFormatoClave(claveListaDeValores.getFormatoClave());
			ldvVO.setMinimoClave(claveListaDeValores.getMinimoClave());
			ldvVO.setMaximoClave(claveListaDeValores.getMaximoClave());

			descripcionListaDeValores = listaDeValoresManager
					.obtieneDescripcionListaDeValores(nmTabla);
			ldvVO.setCdAtribu(descripcionListaDeValores.getCdAtribu());
			ldvVO.setDescripcionFormato(descripcionListaDeValores
					.getDescripcionFormato());
			ldvVO.setFormatoDescripcion(descripcionListaDeValores
					.getFormatoDescripcion());
			ldvVO.setMinimoDescripcion(descripcionListaDeValores
					.getMinimoDescripcion());
			ldvVO.setMaximoDescripcion(descripcionListaDeValores
					.getMaximoDescripcion());
			log.debug("MINIMODESCRIPCION-->"
					+ descripcionListaDeValores.getMinimoDescripcion());
			log.debug("MAXIMODESCRIPCION-->"
					+ descripcionListaDeValores.getMaximoDescripcion());
			log.debug("formatoDESCRIPCION-->"
					+ descripcionListaDeValores.getFormatoDescripcion());
			log.debug("clavedESCRIPCION-->"
					+ claveListaDeValores.getFormatoClave());
		}

		editaListaDeValores.add(ldvVO);
		
		/*TODO
		 * quitar codigo de sesion para el grid de carga manual
		 */
		if (session.containsKey(CARGAMANUAL))
			session.remove(CARGAMANUAL);
		if (session.containsKey("CARGA_MANUAL_EN SESSION"))
			session.remove("CARGA_MANUAL_EN SESSION");
		success = true;
		return SUCCESS;
	}

	/**
	 * Metodo <code>cargaClaveDependcia</code> con el son cargadas las claves
	 * de dependencia
	 * 
	 * @return success
	 * @throws Exception
	 */
	public String cargaClaveDependcia() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if (isDebugEnabled) {
			log.debug("Entro a cargar claves dependencia");
			log.debug("CODIGOtABLA-->" + codigoTabla);
		}
		if (codigoTabla != null) {
			String langCode = "0";
			listaClavesDependencia = listaDeValoresManager
					.listaClavesDependencias(codigoTabla, langCode);

		} else {
			listaClavesDependencia = new ArrayList<LlaveValorVO>();
		}

		success = true;

		return SUCCESS;
	}
	
	
	
	//eliminarTablaClave
	public String eliminarTablaClave() throws Exception{
		
		try{
		   log.debug("NUMEROTABLA-->" + nmTabla);
		   Map<String, String> params =  new HashMap<String, String>();
		   params.put("pi_nmtabla_i",nmTabla);
		   listaDeValoresManager.eliminarTablaClave(params);
           success = true;
		   return SUCCESS;
		}catch(ApplicationException e){
			 success = false;
			 return SUCCESS;
		}catch( Exception e){
            success = false;
            return SUCCESS;
        }
		   
	}



	// getters y setters

	public void setListaDeValoresManager(
			ListaDeValoresManager listaDeValoresManager) {
		this.listaDeValoresManager = listaDeValoresManager;
	}

	public List<ListaDeValoresVO> getListaDeValores() {
		return listaDeValores;
	}

	public void setListaDeValores(List<ListaDeValoresVO> listaDeValores) {
		this.listaDeValores = listaDeValores;
	}

	/*
	 * eliminar public String getValor() { return valor; }
	 * 
	 * 
	 * public void setValor(String valor) { this.valor = valor; }
	 */

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getCatalogo1() {
		return catalogo1;
	}

	public void setCatalogo1(String catalogo1) {
		this.catalogo1 = catalogo1;
	}

	public String getCatalogo2() {
		return catalogo2;
	}

	public void setCatalogo2(String catalogo2) {
		this.catalogo2 = catalogo2;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getEnviaTablaError() {
		return enviaTablaError;
	}

	public void setEnviaTablaError(String enviaTablaError) {
		this.enviaTablaError = enviaTablaError;
	}

	public String getModificaValor() {
		return modificaValor;
	}

	public void setModificaValor(String modificaValor) {
		this.modificaValor = modificaValor;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getFormatoClave() {
		return formatoClave;
	}

	public void setFormatoClave(String formatoClave) {
		this.formatoClave = formatoClave;
	}

	public String getMaximoClave() {
		return maximoClave;
	}

	public void setMaximoClave(String maximoClave) {
		this.maximoClave = maximoClave;
	}

	public String getMinimoClave() {
		return minimoClave;
	}

	public void setMinimoClave(String minimoClave) {
		this.minimoClave = minimoClave;
	}

	public String getDescripcionFormato() {
		return descripcionFormato;
	}

	public void setDescripcionFormato(String descripcionFormato) {
		this.descripcionFormato = descripcionFormato;
	}

	public String getFormatoDescripcion() {
		return formatoDescripcion;
	}

	public void setFormatoDescripcion(String formatoDescripcion) {
		this.formatoDescripcion = formatoDescripcion;
	}

	public String getMaximoDescripcion() {
		return maximoDescripcion;
	}

	public void setMaximoDescripcion(String maximoDescripcion) {
		this.maximoDescripcion = maximoDescripcion;
	}

	public String getMinimoDescripcion() {
		return minimoDescripcion;
	}

	public void setMinimoDescripcion(String minimoDescripcion) {
		this.minimoDescripcion = minimoDescripcion;
	}

	public String getClaveDependencia() {
		return claveDependencia;
	}

	public void setClaveDependencia(String claveDependencia) {
		this.claveDependencia = claveDependencia;
	}

	public List<LlaveValorVO> getListaCargaManual() {
		return listaCargaManual;
	}

	public void setListaCargaManual(List<LlaveValorVO> listaCargaManual) {
		this.listaCargaManual = listaCargaManual;
	}

	public String getValorClave() {
		return valorClave;
	}

	public void setValorClave(String valorClave) {
		this.valorClave = valorClave;
	}

	public String getValorDescripcion() {
		return valorDescripcion;
	}

	public void setValorDescripcion(String valorDescripcion) {
		this.valorDescripcion = valorDescripcion;
	}

	public String getOtClave() {
		return otClave;
	}

	public void setOtClave(String otClave) {
		this.otClave = otClave;
	}

	public String getOtValor() {
		return otValor;
	}

	public void setOtValor(String otValor) {
		this.otValor = otValor;
	}

	public String getSelectedId() {
		return selectedId;
	}

	public void setSelectedId(String selectedId) {
		this.selectedId = selectedId;
	}

	public String getNmTabla() {
		return nmTabla;
	}

	public void setNmTabla(String nmTabla) {
		this.nmTabla = nmTabla;
	}

	public String getTipoTransaccion() {
		return tipoTransaccion;
	}

	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}

	public String getTipoTabla() {
		return tipoTabla;
	}

	public void setTipoTabla(String tipoTabla) {
		this.tipoTabla = tipoTabla;
	}

	public List<DatosClaveAtributoVO> getEditarClaveListaDeValores() {
		return editarClaveListaDeValores;
	}

	public void setEditarClaveListaDeValores(
			List<DatosClaveAtributoVO> editarClaveListaDeValores) {
		this.editarClaveListaDeValores = editarClaveListaDeValores;
	}

	public String getTabla() {
		return tabla;
	}

	public void setTabla(String tabla) {
		this.tabla = tabla;
	}

	public ListaDeValoresVO getCabeceraListaDeValores() {
		return cabeceraListaDeValores;
	}

	public void setCabeceraListaDeValores(
			ListaDeValoresVO cabeceraListaDeValores) {
		this.cabeceraListaDeValores = cabeceraListaDeValores;
	}

	public ListaDeValoresVO getClaveListaDeValores() {
		return claveListaDeValores;
	}

	public void setClaveListaDeValores(ListaDeValoresVO claveListaDeValores) {
		this.claveListaDeValores = claveListaDeValores;
	}

	public ListaDeValoresVO getDescripcionListaDeValores() {
		return descripcionListaDeValores;
	}

	public void setDescripcionListaDeValores(
			ListaDeValoresVO descripcionListaDeValores) {
		this.descripcionListaDeValores = descripcionListaDeValores;
	}

	public List<ListaDeValoresVO> getEditaListaDeValores() {
		return editaListaDeValores;
	}

	public void setEditaListaDeValores(
			List<ListaDeValoresVO> editaListaDeValores) {
		this.editaListaDeValores = editaListaDeValores;
	}

	public String getHiddenRadioClave() {
		return hiddenRadioClave;
	}

	public void setHiddenRadioClave(String hiddenRadioClave) {
		this.hiddenRadioClave = hiddenRadioClave;
	}

	public String getHiddenRadioDescripcion() {
		return hiddenRadioDescripcion;
	}

	public void setHiddenRadioDescripcion(String hiddenRadioDescripcion) {
		this.hiddenRadioDescripcion = hiddenRadioDescripcion;
	}

	public String getRadioClave() {
		return radioClave;
	}

	public void setRadioClave(String radioClave) {
		this.radioClave = radioClave;
	}

	public String getRadioDescripcion() {
		return radioDescripcion;
	}

	public void setRadioDescripcion(String radioDescripcion) {
		this.radioDescripcion = radioDescripcion;
	}

	public String getCodigoTabla() {
		return codigoTabla;
	}

	public void setCodigoTabla(String codigoTabla) {
		this.codigoTabla = codigoTabla;
	}

	public List<LlaveValorVO> getListaClavesDependencia() {
		return listaClavesDependencia;
	}

	public void setListaClavesDependencia(
			List<LlaveValorVO> listaClavesDependencia) {
		this.listaClavesDependencia = listaClavesDependencia;
	}

	/**
	 * @return the listaDeValores2
	 */
	public List<ListaDeValoresVO> getListaDeValores2() {
		return listaDeValores2;
	}

	/**
	 * @param listaDeValores2
	 *            the listaDeValores2 to set
	 */
	public void setListaDeValores2(List<ListaDeValoresVO> listaDeValores2) {
		this.listaDeValores2 = listaDeValores2;
	}

    /**
     * @return the mensajeRespuesta
     */
    public String getMensajeRespuesta() {
        return mensajeRespuesta;
    }

    /**
     * @param mensajeRespuesta the mensajeRespuesta to set
     */
    public void setMensajeRespuesta(String mensajeRespuesta) {
        this.mensajeRespuesta = mensajeRespuesta;
    }

	public void setListaDeValoresManagerJdbcTemplate(
			ListaDeValoresManager listaDeValoresManagerJdbcTemplate) {
		this.listaDeValoresManagerJdbcTemplate = listaDeValoresManagerJdbcTemplate;
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

	/*
	 * public List<ClavesVO> getListaUnayCincoClaves() { return
	 * listaUnaYcincoClaves; }
	 * 
	 * 
	 * public void setListaUnayCincoClaves(List<ClavesVO> listaUnayCincoClaves) {
	 * this.listaUnaYcincoClaves = listaUnayCincoClaves; }
	 */
}
