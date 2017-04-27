package mx.com.gseguros.wizard.configuracion.producto.tipoObjeto.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.RamaVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.reglanegocio.model.ReglaNegocioVO;
import mx.com.gseguros.wizard.configuracion.producto.service.TipoObjetoManager;
import mx.com.gseguros.wizard.configuracion.producto.tipoObjeto.model.DatoVariableObjetoVO;
import mx.com.gseguros.wizard.configuracion.producto.tipoObjeto.model.TipoObjetoVO;
import mx.com.gseguros.wizard.configuracion.producto.web.Padre;
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
 * Clase que contiene los metodos de cargar, agregar y editar el tipo de objeto de un producto 
 *
 */
public class TipoObjetoAction extends Padre {

	private static final long serialVersionUID = 22909377646070700L;
	private static final transient Log log = LogFactory.getLog(TipoObjetoAction.class);

	/**
	 * Manager con implementacion de Endpoint para la consulta a BD.
	 */
	private TipoObjetoManager tipoObjetoManager;
	
	/**
	 * Manager con implementacion de Endppoints para catalogos
	 */
	private CatalogService catalogManager;
	
	/**
	 * Atributo de respuesta interpretado por strust como la correcta
	 * realizacion de. cada peticion.
	 */
	private boolean success;
	
	/**
	 * Atributo de respuesta interpretado por strust con el catalogo de tipo de objeto 
	 * List<LlaveValorVO> con los valores de la consulta.
	 */
	private List<LlaveValorVO> catalogoTipoDeObjeto;
	
	/**
	 * Atributo agregado por struts que contiene el codigo del tipo de objeto.
	 */
	private String codigoObjeto;
	
	/**
	 * Atributo agregado por struts que contiene la descripcion del del tipo de objeto.
	 */
	private String descripcionObjeto;
	
	/**
	 * Atributo agregado por struts que contiene la descripcion del atributo
	 * variable del objeto.
	 */
	private String descripcion;
	
	/**
	 * Atributo agregado por struts que contiene el formato del atributo
	 * variable del objeto.
	 */
	private String codigoRadioAtributosVariables;
	
	/**
	 * Atributo agregado por struts que indica si el atributo variable del objeto es
	 * modificado en emision.
	 */
	private String modificaEmision;
	
	/**
	 * Atributo agregado por struts que indica si el atributo variable del objeto es
	 * modificado en endoso.
	 */
	private String modificaEndoso;
	
	/**
	 * Atributo agregado por struts que indica si el atributo variable del objeto es
	 * retarificado.
	 */
	private String retarificacion;
	
	/**
	 * Atributo agregado por struts que contiene la especificacion maxima 
	 * del formato para el atributo variable del objeto numerico o alfanumerico.
	 */
	private String maximo;
	
	/**
	 * Atributo agregado por struts que contiene la especificacion minima 
	 * del formato para el atributo variable del objeto numerico o alfanumerico.
	 */
	private String minimo;
	
	/**
	 * Atributo agregado por struts que contiene el codigo de 
	 * la lista de valores de un atributo variable del objeto.
	 */
	private String codigoTabla;
	
	/**
	 * Atributo agregado por struts que contiene la descripcion de 
	 * la lista de valores de un atributo variable del objeto.
	 */
	private String descripcionTabla;
	
	/**
	 * Atributo agregado por struts que indica la obligatoriedad del dato variable para el objeto.
	 */
	private String obligatorio;
	/**
	 * Atributo agregado por struts que contiene la condicion
	 * del atributo variable para el objeto.
	 */
	private String condicion;
	/**
	 * Atributo agregado por struts que contien el padre del 
	 * atributo variable.
	 */
	private String padre;
	/**
	 * Atributo agregado por struts que contien el orden del 
	 * atributo variable.
	 */
	private String orden;
	/**
	 * Atributo agregado por struts que contien el agrupador del 
	 * atributo variable.
	 */
	private String agrupador;
	
	
	/**
	 * Atributo agregado por struts que indica la obligatoriedad del dato variable para el objeto.
	 */
	private List<DatoVariableObjetoVO> listaDatosVariables;
	
	/**
	 * Atributo agregado por struts que indica se se edita el dato variable del objeto.
	 */
	private int edita;
	
	//private String descripcionHidden;
	
	/**
	 * Atributo agregado por struts que contiene el codigo del atributo
	 * variable del objeto.
	 */
	private String claveCampo;
	
	//private String claveCampoTemporal;
	
	/**
	 * Atributo agregado por struts que contiene el codigo de tipo de situacion para el tipo de objeto.
	 */
	private String codigoTipoSituacion;
	
	/**
	 * Atributo agregado por struts que indica si aparece el cotizador del tipo de objeto.
	 */
	private String apareceCotizador;

	/**
	 * Atributo agregado por struts que contiene Dato complementario  del tipo de objeto.
	 */
	private String datoComplementario;
	
	/**
	 * Atributo agregado por struts que indica Obligatoriedad para el dato complementario del tipo de objeto.
	 */
	private String obligatorioComplementario;
	
	/**
	 * Atributo agregado por struts que indica si si es modificable el dato complementario del tipo de objeto.
	 */
	private String modificableComplementario;
	
	/**
	 * Atributo agregado por struts que indica si aparece el endoso del tipo de objeto.
	 */
	private String apareceEndoso;
	
	/**
	 * Atributo agregado por struts que contiene la Obligatoriedad para el endoso del tipo de objeto.
	 */
	private String obligatorioEndoso;
	
	/**
	 * Atributo utilizado para almacenar los mensajes de respuesta de una peticion a BD
	 */
	private String mensajeRespuesta;
	
	/**
	 * Metodo <code>execute</code> con el que es llamado desde Struts para
	 * atender la petici&oacute;n web
	 * 
	 * @return INPUT
	 * @throws Exception
	 */
	public String execute() throws Exception {
		if(log.isDebugEnabled()){
			log.debug("-------------->Entro a TipoObjetoAction");
		}
		return INPUT;
	}
	
	/**
     * Metodo <code>catalogoTipoDeObjeto</code> con el que es cargado el catalogo de tipo de objeto.
     * 
     * @return success
     * @throws Exception
     */
	public String catalogoTipoDeObjeto() throws Exception{
		if(log.isDebugEnabled()){
			log.debug("Entro a catalogo Tipo de Objeto");
		}
		catalogoTipoDeObjeto = null;
		//if (!session.containsKey("CATALOGO_TIPO_OBJETO")) {
			String tipoObjeto=null;
			catalogoTipoDeObjeto = tipoObjetoManager.catalogoTipoObjetoJson(tipoObjeto);
				//session.put("CATALOGO_TIPO_OBJETO", catalogoTipoDeObjeto);
				if(log.isDebugEnabled()){
					for(LlaveValorVO tipo: catalogoTipoDeObjeto){
						log.debug("DSOBJETO="+tipo.getValue());
					}
				}
				
		//} else {
			//catalogoTipoDeObjeto = (List<LlaveValorVO>) session.get("CATALOGO_TIPO_OBJETO");
		//}
		success = true;
		return SUCCESS;
	}
	
	/**
     * Metodo <code>agregaTipoObjetoCatalogo</code> con el que es agregado un tipo de objeto al catalogo
     * 
     * @return success
     * @throws Exception
     */
	public String agregaTipoObjetoCatalogo() throws Exception{
		if(log.isDebugEnabled()){
			log.debug("Entro a agregar un tipo de objeto al catalogo");
		}
		LlaveValorVO objeto = new LlaveValorVO();		
		objeto.setValue(descripcionObjeto);
		tipoObjetoManager.agregaObjetoAlCatalogo(objeto);
		success=true;
		
		return SUCCESS;
		
	}
	
	/**
     * Metodo <code>listaDatosVariablesObjetos</code> con el que es cargado el catalogo de los
     * atributos variables para el objeto.
     * 
     * @return success
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public String listaDatosVariablesObjetos() throws Exception{
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("Entro a lista de datos variables json");
		}
		String codigoObjetoSesion="";
		if((String)session.get("CODIGO_OBJETO")!= null){
			codigoObjetoSesion= (String)session.get("CODIGO_OBJETO");
		}else{
			codigoObjetoSesion= "";
		}
		if(!codigoObjetoSesion.equals(codigoObjeto)){
			if(codigoObjeto!=null){
				session.put("CODIGO_OBJETO", codigoObjeto);
				if(isDebugEnabled){
					log.debug("CODIGOOBJETO--"+codigoObjeto);
					log.debug("CODIGOOBJETOSESION--"+codigoObjetoSesion);
				}
				listaDatosVariables=tipoObjetoManager.listaDatosVariablesObjetos(codigoObjeto);
				
				/*if(isDebugEnabled){
					if(listaDatosVariables!=null && !listaDatosVariables.isEmpty()){
						for(DatoVariableObjetoVO objetoVo:listaDatosVariables){
							log.debug("CDpadre"+objetoVo.getCodigoPadre());
							log.debug("DSpadre"+objetoVo.getDsAtributoPadre());
						}
					}
				}*/
				
			}else{
				listaDatosVariables = new ArrayList<DatoVariableObjetoVO>();
			}
			session.put("DATOS_VARIABLES_OBJETO", listaDatosVariables);
		}else{
			listaDatosVariables = (List<DatoVariableObjetoVO>) session.get("DATOS_VARIABLES_OBJETO");
		}
		success = true;
		return SUCCESS;
	}

	/**
     * Metodo <code>agregarAtributoVariableRol</code> con el que es agregado o editado 
     * un atributo variable al rol.
     * 
     * @return success
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public String agregarDatoVariableObjeto() throws Exception{
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("Entro a agregar dato variable objeto");
		}
		/*
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
		*/
		if(session.containsKey("CODIGO_OBJETO")){
			codigoObjeto=(String) session.get("CODIGO_OBJETO");
			if(isDebugEnabled){
				log.debug("CODIGOobjeto-->"+codigoObjeto);
			}
		}
		/*else{
			//codigoTipoSituacion="NA";
			//codigoNivel=0;
			
			//para prueba
			codigoRamo ="900";
			codigoTipoSituacion="NA";
			codigoNivel=0;
			//codigoRol="72";
		}
		*/
		
		if(codigoObjeto != null){
			if(obligatorio == null){
				obligatorio="N";			
			}if(modificaEmision == null){
				modificaEmision="N";			
			}if(modificaEndoso == null){
				modificaEndoso="N";			
			}if(retarificacion == null){
				retarificacion="N";			
			}if(apareceCotizador == null){
				apareceCotizador="N";			
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
			}if(descripcionTabla.equals("Seleccione un valor")){
				descripcionTabla=null;
			}if(StringUtils.isBlank(codigoTabla)){
				codigoTabla=null;
			}
			
			if(isDebugEnabled){
				log.debug("DESCRIPCION-->"+descripcion);
				log.debug("FORMATO-->"+codigoRadioAtributosVariables);
				//log.debug("EDITA-->"+edita);
				/*log.debug("MINIMO-->"+minimo);
				log.debug("MAXIMO-->"+maximo);
				log.debug("EMISION-->"+modificaEmision);
				log.debug("ENDOSO-->"+modificaEndoso);
				log.debug("RETARIFICACION-->"+retarificacion);
				log.debug("CODIGOLISTAVALOR-->"+codigoTabla);
				log.debug("DESCRIPCIONLISTAVALOR-->"+descripcionTabla);
				*/
				log.debug("CLAVECAMPO inicial-->"+claveCampo);
			}
			
			ArrayList<DatoVariableObjetoVO> temporal = (ArrayList<DatoVariableObjetoVO>) session.get("DATOS_VARIABLES_OBJETO");
			DatoVariableObjetoVO listaGrid = null;
			int sizeList= temporal.size();
			
			if(edita==0){
				if(isDebugEnabled){
					log.debug("EDITA--- "+edita);
					log.debug("SIZE--- "+sizeList);
				}
				log.debug("padre:"+padre);
				log.debug("orden:"+orden);
				log.debug("agrupador:"+agrupador);
				log.debug("condicion:"+condicion);
				listaGrid = new DatoVariableObjetoVO();
				//if(descripcionAtributoVariable!=null && StringUtils.isNotBlank(descripcionAtributoVariable)){						
					if(StringUtils.isBlank(claveCampo)){
						if(isDebugEnabled){
							log.debug("ENTRA IF CLAVECAMPO AGREGA");
						}
						int codigoTemporal=sizeList++;
						String claveCampoTemporal=String.valueOf(codigoTemporal)+"*";
						listaGrid.setCodigoAtributoVariable(claveCampoTemporal);
						if(isDebugEnabled){
							log.debug("CLAVECAMPOTEMPORAL"+claveCampoTemporal);
						}
					}else{
						listaGrid.setCodigoAtributoVariable(claveCampo);
					}
					listaGrid.setCodigoObjeto(codigoObjeto);
					listaGrid.setDescripcionAtributoVariable(descripcion);
					listaGrid.setCodigoFormato(codigoRadioAtributosVariables);
					listaGrid.setMinimo(minimo);
					listaGrid.setMaximo(maximo);
					listaGrid.setSwitchObligatorio(obligatorio);
					listaGrid.setEmision(modificaEmision);
					listaGrid.setEndoso(modificaEndoso);
					listaGrid.setRetarificacion(retarificacion);
					listaGrid.setCodigoTabla(codigoTabla);
					listaGrid.setDescripcionTabla(descripcionTabla);
					listaGrid.setApareceCotizador(apareceCotizador);					
					listaGrid.setDatoComplementario(datoComplementario);
					listaGrid.setObligatorioComplementario(obligatorioComplementario);
					listaGrid.setModificableComplementario(modificableComplementario);
					listaGrid.setApareceEndoso(apareceEndoso);
					listaGrid.setObligatorioEndoso(obligatorioEndoso);
					if(StringUtils.isNotBlank(orden)){
					listaGrid.setOrden(orden);
					}
					if(StringUtils.isNotBlank(agrupador)){
					listaGrid.setAgrupador(agrupador);
					}
					List<ReglaNegocioVO> listaCondiciones = (List<ReglaNegocioVO>) session.get("LISTA_REGLA_NEGOCIO_CONDICIONES");
					if(StringUtils.isNotBlank(condicion)){
						if(condicion.contains("Seleccione una")){
							log.debug("No entro a la lista de condicion");
						}else{
							listaGrid.setDsCondicion(condicion);
							for(ReglaNegocioVO reglaNeg : listaCondiciones){
								if(reglaNeg.getDescripcion().equals(condicion)){
									listaGrid.setCodigoCondicion(reglaNeg.getNombre());
									success=true;
								}
							}
						}
					}
					if(StringUtils.isNotBlank(padre)){
						if(padre.contains("Seleccione un Padre")){
							log.debug("No entro a la lista");
						}else{
							listaGrid.setDsAtributoPadre(padre);
							List<LlaveValorVO> listaAtributosPadres = (List<LlaveValorVO>) session.get("LISTA_PADRE_ATRIBUTOS_VARIABLES");
							for(LlaveValorVO listaPadres:listaAtributosPadres){
								if(listaPadres.getValue().equals(padre)){
									listaGrid.setCodigoPadre(listaPadres.getKey());
									success=true;
								}
							}
							
						}
						
					}
					
					
					
					temporal.add(listaGrid);					
				//}
					}
			if(edita==1){
				if(isDebugEnabled){
					log.debug("EDITA--- "+edita);
				}
				for (DatoVariableObjetoVO  temp: temporal){
					if(isDebugEnabled){
						log.debug("CLAVECAMPO-->"+claveCampo);
						log.debug("TEMPCODIGOATRIBUTO--- "+temp.getCodigoAtributoVariable());
						//log.debug("DESCRIPCIONENVIADO--- "+descripcion);
						//log.debug("TEMPDESCRIPCIONATRIBUTO--- "+temp.getDescripcionAtributoVariable());
					}
										
					if(temp.getCodigoAtributoVariable().equals(claveCampo)){
							if(temp.getCodigoAtributoVariable().endsWith("*")){
								temp.setCodigoAtributoVariable("");
							}
							else{
								temp.setCodigoAtributoVariable(claveCampo);
							}
							temp.setCodigoObjeto(codigoObjeto);
							temp.setDescripcionAtributoVariable(descripcion);
							temp.setCodigoFormato(codigoRadioAtributosVariables);
							temp.setMinimo(minimo);
							temp.setMaximo(maximo);
							temp.setSwitchObligatorio(obligatorio);
							temp.setEmision(modificaEmision);
							temp.setEndoso(modificaEndoso);
							temp.setRetarificacion(retarificacion);
							temp.setCodigoTabla(codigoTabla);
							temp.setDescripcionTabla(descripcionTabla);
							temp.setApareceCotizador(apareceCotizador);
							temp.setDatoComplementario(datoComplementario);
							temp.setObligatorioComplementario(obligatorioComplementario);
							temp.setModificableComplementario(modificableComplementario);
							temp.setApareceEndoso(apareceEndoso);
							temp.setObligatorioEndoso(obligatorioEndoso);
							temp.setOrden(orden);
							temp.setAgrupador(agrupador);
							List<ReglaNegocioVO> listaCondiciones = (List<ReglaNegocioVO>) session.get("LISTA_REGLA_NEGOCIO_CONDICIONES");
							if(StringUtils.isNotBlank(condicion)){
								if(condicion.contains("Seleccione una")){
									log.debug("No entro a la lista");
								}else{
									for(ReglaNegocioVO reglaNeg : listaCondiciones){
										if(reglaNeg.getDescripcion().equals(condicion)){
											temp.setCodigoCondicion(reglaNeg.getNombre());
											success=true;
										}
									}
								
								}
							}
							log.debug("padre:"+ padre);
							if(StringUtils.isNotBlank(padre)){
								if(padre.contains("Seleccione un Padre")){
									log.debug("No entro a la lista");
								}else{
									List<LlaveValorVO> listaAtributosPadres = (List<LlaveValorVO>) session.get("LISTA_PADRE_ATRIBUTOS_VARIABLES");
									for(LlaveValorVO listaPadres:listaAtributosPadres){
										if(listaPadres.getValue().equals(padre)){
											temp.setCodigoPadre(listaPadres.getKey());
											success=true;
										}
									}								
								}
							}
																
					}
					if(isDebugEnabled){
						log.debug("CODIGOATRIBUTOENVIADO---logger "+temp.getCodigoAtributoVariable());
					}
				}
				/*if(isDebugEnabled){
				 	for (DatoVariableObjetoVO  temp: temporal){
						log.debug("TEMPCODIGOATRIBUTO---logger "+temp.getCodigoAtributoVariable());
						log.debug("TEMPDESCRIPCIONATRIBUTO---logger "+temp.getDescripcionAtributoVariable());
					}
				}*/
			}
			session.put("DATOS_VARIABLES_OBJETO", temporal);
			success = true;		
			return SUCCESS;
			
			
		}
		
		success = true;
		return SUCCESS;
	}
	
	
	/**
     * Metodo <code>eliminarDatoVariableObjeto</code> con el que son eliminados los datos variables
     * del objeto.
     * 
     * @return success
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public String eliminarDatoVariableObjeto() throws Exception {
    	boolean isDebugEnabled = log.isDebugEnabled();
    	if(isDebugEnabled){
    		log.debug("Entro a eliminar de la lista del grid");
    		log.debug("DESCRIPCIONDATOVAR"+descripcion);
    	}
        if(descripcion != null){
        ArrayList<DatoVariableObjetoVO> temporal = (ArrayList<DatoVariableObjetoVO>) session.get("DATOS_VARIABLES_OBJETO");
            boolean valorLista = false;
           
            DatoVariableObjetoVO remover = null;
            for (DatoVariableObjetoVO  temp: temporal){
            	if(isDebugEnabled){
            		log.debug("SESIONDESCRIPCIONATRIBUTO"+temp.getDescripcionAtributoVariable());
            	}
                if(temp.getDescripcionAtributoVariable().equals(descripcion)){
                    
                    remover = temp;
                    if(isDebugEnabled)
	                    log.debug("Encontro registro");
                    valorLista = true;
                }
            }
            
            if(remover!=null){
               
               	if(isDebugEnabled)
	            	log.debug("Eliminado");
                temporal.remove(remover);
                session.put("DATOS_VARIABLES_OBJETO", temporal);
                success = true;      
            }
            if(valorLista){
                    session.put("DATOS_VARIABLES_OBJETO", temporal);
                    success = true;
            }
        }
        //eliminacion del objeto en BD
        if(codigoObjeto != null && claveCampo != null){
        	TipoObjetoVO objetoVO = new TipoObjetoVO();
        	objetoVO.setCodigoTipoObjeto(codigoObjeto);
        	objetoVO.setCodigoAtributoVariable(claveCampo);
        	
        	tipoObjetoManager.borraTatriObjeto(objetoVO);
        }
        return SUCCESS;
    }
    
    /**
     * Valida si el atributo variable del objeto tiene hijos asociados
     * @return
     * @throws Exception
     */
    public String validaHijosAtributoVariableObjeto() throws Exception {
    	TipoObjetoVO objetoVO = new TipoObjetoVO();
    	objetoVO.setCodigoTipoObjeto(codigoObjeto);
    	objetoVO.setCodigoAtributoVariable(claveCampo);
    	
    	if(tipoObjetoManager.tieneHijosAtributoVariableObjeto(objetoVO)){
			mensajeRespuesta = Constantes.MESSAGE_ATRIBUTO_CON_HIJOS_ASOCIADOS;
		}
		success = true;
    	return SUCCESS;
    }
    
    /**
     * Metodo <code>insertaTipoObjetoInciso</code> con el que son insertados los valores
     * del tipo de objeto al inciso.
     * 
     * @return success
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public String insertaTipoObjetoInciso() throws Exception {
    	boolean isDebugEnabled = log.isDebugEnabled();
    	if(isDebugEnabled){
    		log.debug("Entro a insertar tipo objeto");
    	}
          	
      if(session.containsKey("CODIGO_NIVEL2")){
			codigoTipoSituacion=(String) session.get("CODIGO_NIVEL2");
			if(isDebugEnabled){
				log.debug("CODIGOTIPOSITUACION-->"+codigoTipoSituacion);
			}
		}
      log.debug("CODIGO OBJETO 0: " + codigoObjeto);
		log.debug("CODIGO OBJETO 1: " + session.get("CODIGO_OBJETO"));
      if(session.containsKey("CODIGO_OBJETO")){
    	  if (!StringUtils.isBlank((String)session.get("CODIGO_OBJETO"))) {
    		  codigoObjeto=(String) session.get("CODIGO_OBJETO");
    		  if(isDebugEnabled){
    			  log.debug("CODIGO_OBJETO-->"+codigoObjeto);
    		  }
    	  }
      }
      if(codigoObjeto!= null && codigoTipoSituacion != null && 
    		  session.containsKey("DATOS_VARIABLES_OBJETO")){
    	      	  
    	  //inserta cabecera del objeto
    	  TipoObjetoVO objeto = new TipoObjetoVO();
    	  objeto.setCodigoTipoSituacion(codigoTipoSituacion);
    	  objeto.setCodigoTipoObjeto(codigoObjeto);
    	 
    	  if(isDebugEnabled){
    		  log.debug("CDTIPSIT**"+codigoTipoSituacion);
    		  log.debug("CODOBJETO**"+codigoObjeto);
    	  }
    	  
    	  tipoObjetoManager.agregarTipoObjetoInciso(objeto);
    	  
    	  //inserta datos variables del tipo de objeto
    	  List<DatoVariableObjetoVO> AtributosObjeto = (ArrayList<DatoVariableObjetoVO>) session.get("DATOS_VARIABLES_OBJETO");
    	  for(DatoVariableObjetoVO atribu: AtributosObjeto){
    		  
    		  if(atribu.getCodigoAtributoVariable().endsWith("*")){
    			  atribu.setCodigoAtributoVariable("");
    		  }
    		  if(StringUtils.isBlank(atribu.getCodigoFormato())){
    			  atribu.setCodigoFormato("A");
    		  }
    		  if(StringUtils.isBlank(atribu.getCodigoObjeto())){
    			  atribu.setCodigoObjeto(codigoObjeto);
    		  }
    		  if(isDebugEnabled){
    			  log.debug("CODIGOATRIBUTO//"+atribu.getCodigoAtributoVariable());
    			  log.debug("DESCRIPCIONATRIBUTO//"+atribu.getDescripcionAtributoVariable());
    		  }
    		  
    	  }
    	  tipoObjetoManager.agregaDatosVariablesObjeto(AtributosObjeto);
    	
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
     * Elimina el objeto (elimina la asociaci&oacute;n que tiene el objeto con el Inciso/Riesgo) 
     * 
     * @return success
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public String eliminaTipoObjetoInciso() throws Exception {
    
    	boolean isDebugEnabled = log.isDebugEnabled();
    	if(isDebugEnabled){
    		log.debug("Entro a eliminar tipo objeto");
    		log.debug("codigoObjeto: " + codigoObjeto);
    	}
    	
    	if(session.containsKey("CODIGO_NIVEL2")){
			codigoTipoSituacion = (String) session.get("CODIGO_NIVEL2");
		}
    	
		if(session.containsKey("CODIGO_OBJETO")){
			if (!StringUtils.isBlank((String)session.get("CODIGO_OBJETO"))) {
				codigoObjeto=(String) session.get("CODIGO_OBJETO");
			}
		}
		
		if(isDebugEnabled){
			log.debug("CODIGOTIPOSITUACION-->"+codigoTipoSituacion);
			log.debug("CODIGO_OBJETO-->"+codigoObjeto);
		}
		
		if(codigoObjeto!= null && codigoTipoSituacion != null && session.containsKey("DATOS_VARIABLES_OBJETO")){
			
			MensajesVO mensajeVO = null;
			
			TipoObjetoVO objeto = new TipoObjetoVO();
			objeto.setCodigoTipoSituacion(codigoTipoSituacion);
			objeto.setCodigoTipoObjeto(codigoObjeto);
			
			//Eliminar objeto
			mensajeVO = tipoObjetoManager.borrarTipoObjetoInciso(objeto);
    	  
			//Obtener mensaje de respuesta que se va a mostrar:
			Map<String, String> params = new HashMap<String, String>();
			params.put("msg", mensajeVO.getMsgId());
			mensajeVO = catalogManager.getMensajes(params, "OBTIENE_MENSAJES");
			if(isDebugEnabled){
				log.debug("title-->" + mensajeVO.getTitle());
				log.debug("mensajeRespuesta-->" + mensajeVO.getMsgText());
			}
			mensajeRespuesta = mensajeVO.getMsgText();
			if( "2".equals(mensajeVO.getTitle()) ){
				success=true;
			}
			
			//Recargar el arbol de productos
			if(success){
				List<RamaVO> temporalTree=(List<RamaVO>) session.get("ARBOL_PRODUCTOS");
				session.put("ARBOL_PRODUCTOS_RECARGAR", temporalTree);
				session.remove("ARBOL_PRODUCTOS");
			}
		}
	
        return SUCCESS;
    }
    
    /**
     * Metodo <code>borraSesionesTipoObjeto</code> con el que son eliminadas las sesiones 
     * de tipo de objeto (codigo objeto,datos variables).
     * 
     * @return success
     * @throws Exception
     */
    public String borraSesionesTipoObjeto() throws Exception{
    	if(log.isDebugEnabled()){
    		log.debug("borrando sesion");
    	}
    	if(session.containsKey("DATOS_VARIABLES_OBJETO"))
    		session.remove("DATOS_VARIABLES_OBJETO");
    	if(session.containsKey("CODIGO_OBJETO"))
    		session.remove("CODIGO_OBJETO");
    	success = true;
    	return SUCCESS;
    }
	
	public List<LlaveValorVO> getCatalogoTipoDeObjeto() {
		return catalogoTipoDeObjeto;
	}

	public void setCatalogoTipoDeObjeto(List<LlaveValorVO> catalogoTipoDeObjeto) {
		this.catalogoTipoDeObjeto = catalogoTipoDeObjeto;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setTipoObjetoManager(TipoObjetoManager tipoObjetoManager) {
		this.tipoObjetoManager = tipoObjetoManager;
	}
	
	public void setCatalogManager(CatalogService catalogManager) {
		this.catalogManager = catalogManager;
	}

	public String getCodigoObjeto() {
		return codigoObjeto;
	}

	public void setCodigoObjeto(String codigoObjeto) {
		this.codigoObjeto = codigoObjeto;
	}

	public String getDescripcionObjeto() {
		return descripcionObjeto;
	}

	public void setDescripcionObjeto(String descripcionObjeto) {
		this.descripcionObjeto = descripcionObjeto;
	}

	

	public List<DatoVariableObjetoVO> getListaDatosVariables() {
		return listaDatosVariables;
	}

	public void setListaDatosVariables(
			List<DatoVariableObjetoVO> listaDatosVariables) {
		this.listaDatosVariables = listaDatosVariables;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	

	public String getCodigoRadioAtributosVariables() {
		return codigoRadioAtributosVariables;
	}

	public void setCodigoRadioAtributosVariables(
			String codigoRadioAtributosVariables) {
		this.codigoRadioAtributosVariables = codigoRadioAtributosVariables;
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

	public String getCodigoTabla() {
		return codigoTabla;
	}

	public void setCodigoTabla(String codigoTabla) {
		this.codigoTabla = codigoTabla;
	}

	public String getDescripcionTabla() {
		return descripcionTabla;
	}

	public void setDescripcionTabla(String descripcionTabla) {
		this.descripcionTabla = descripcionTabla;
	}

	public String getObligatorio() {
		return obligatorio;
	}

	public void setObligatorio(String obligatorio) {
		this.obligatorio = obligatorio;
	}

	public int getEdita() {
		return edita;
	}

	public void setEdita(int edita) {
		this.edita = edita;
	}
/*
	public String getDescripcionHidden() {
		return descripcionHidden;
	}

	public void setDescripcionHidden(String descripcionHidden) {
		this.descripcionHidden = descripcionHidden;
	}
*/
	public String getClaveCampo() {
		return claveCampo;
	}

	public void setClaveCampo(String claveCampo) {
		this.claveCampo = claveCampo;
	}

	public String getCodigoTipoSituacion() {
		return codigoTipoSituacion;
	}

	public void setCodigoTipoSituacion(String codigoTipoSituacion) {
		this.codigoTipoSituacion = codigoTipoSituacion;
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

	public String getApareceCotizador() {
		return apareceCotizador;
	}

	public void setApareceCotizador(String apareceCotizador) {
		this.apareceCotizador = apareceCotizador;
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
	
/*
	public String getClaveCampoTemporal() {
		return claveCampoTemporal;
	}

	public void setClaveCampoTemporal(String claveCampoTemporal) {
		this.claveCampoTemporal = claveCampoTemporal;
	}
*/
	
	
}
