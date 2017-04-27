package mx.com.gseguros.wizard.configuracion.producto.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal.service.PagedList;
import mx.com.aon.portal.service.impl.AbstractManagerJdbcTemplateInvoke;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.wizard.configuracion.producto.model.ClavesVO;
import mx.com.gseguros.wizard.configuracion.producto.model.ListaDeValoresVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.service.ListaDeValoresManager;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.DatosClaveAtributoVO;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


public class ListaDeValoresManagerJdbcTemplateImpl extends AbstractManagerJdbcTemplateInvoke implements ListaDeValoresManager {
	
	private static Logger logger = Logger.getLogger(ListaDeValoresManagerJdbcTemplateImpl.class);
	
	/**
	 * Implementacion que extrae los catalogos de la lista de valores.
	 * 
	 * 
	 * @return catalogo List<ListaDeValoresVO> - Lista con la informacion de los
	 *         catalogos asociadas a la lista de valores.
	 *         
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	@SuppressWarnings("unchecked")
	public List<ListaDeValoresVO> listaDeValoresCatalogo(String valor)
			throws ApplicationException {
		
		HashMap params = new HashMap();
		params.put("PV_OTTIPOTB_I", valor);
		 
		List<ListaDeValoresVO> catalogo = null;
	    try {
	    		catalogo = (List<ListaDeValoresVO>) getAllBackBoneInvoke( params, "OBTIENE_CATALOGO_LISTA_VALORES");
	        } catch (ApplicationException e) {
	        	throw new ApplicationException("Error regresando lista de valores del catalogo");
	        }
	   return catalogo;
	}

	public List<DatosClaveAtributoVO> consultaClaves(String nmTabla)
			throws ApplicationException {
		 
		 List<DatosClaveAtributoVO> listaClaves = null;
	        try {
	        	HashMap<String,Object> params = new HashMap<String, Object>();
	        	params.put("PI_NMTABLA", nmTabla);
	        	WrapperResultados res = returnBackBoneInvoke(params, "OBTIENE_CLAVES_1Y5");
	        	
	        	listaClaves = (List<DatosClaveAtributoVO>) res.getItemList();
	        
	        } catch (Exception e) {
	            throw new ApplicationException("Error regresando las claves de 1 y 5");
	        }
	        return listaClaves;
	}

	public List<DatosClaveAtributoVO> consultaDescripciones(String nmTabla)
			throws ApplicationException {
		 
		 List<DatosClaveAtributoVO> listaDescripciones = null;
	        try {
	        	
	        	HashMap<String,Object> params = new HashMap<String, Object>();
	        	params.put("PI_NMTABLA", nmTabla);
	        	WrapperResultados res = returnBackBoneInvoke(params, "OBTIENE_DESCRIPCIONES_1Y5");
	        	
	        	listaDescripciones = (List<DatosClaveAtributoVO>) res.getItemList();
	        
	        } catch (Exception e) {
	            throw new ApplicationException("Error regresando las descripciones de 1 y 5");
	        }
	        return listaDescripciones;
	}

	public ListaDeValoresVO consultaTabla(String nmTabla)
			throws ApplicationException {
		 
		 ListaDeValoresVO tabla = null;
	        try {
	        	HashMap<String,Object> params = new HashMap<String, Object>();
	        	params.put("PI_NMTABLA", nmTabla);
	        	WrapperResultados res = returnBackBoneInvoke(params, "OBTIENE_TABLA_1Y5_CLAVES");
	        	tabla = (ListaDeValoresVO) res.getItemList().get(0);
	        
	        } catch (Exception e) {
	        	logger.error(e);
	            throw new ApplicationException("Error regresando la tabla de 1 y 5 claves");
	        }
	        return tabla;
	}

	public ClavesVO convierteAClaveVO(List<DatosClaveAtributoVO> dca)
			throws ApplicationException {
		  ClavesVO clavesVO= new ClavesVO();  
		  if(!dca.isEmpty()){
		   for(DatosClaveAtributoVO dcaVO: dca){
		    //logger.debug("NUMCLAVEIMPL**"+dcaVO.getNumeroClave());
		    //logger.debug("DESCLAVEIMPL**"+dcaVO.getDescripcion());
			   switch(dcaVO.getNumeroClave()){
		    case 0:
		     clavesVO.setClave(dcaVO.getDescripcion());
		     clavesVO.setFormatoClave(dcaVO.getFormato());
		     clavesVO.setMaximoClave(dcaVO.getMaximo());
		     clavesVO.setMinimoClave(dcaVO.getMinimo());
		     break;
		    case 1:
		     clavesVO.setClave2(dcaVO.getDescripcion());
		     clavesVO.setFormatoClave2(dcaVO.getFormato());
		     clavesVO.setMaximoClave2(dcaVO.getMaximo());
		     clavesVO.setMinimoClave2(dcaVO.getMinimo());
		     break; 
		    case 2:
		     clavesVO.setClave3(dcaVO.getDescripcion());
		     clavesVO.setFormatoClave3(dcaVO.getFormato());
		     clavesVO.setMaximoClave3(dcaVO.getMaximo());
		     clavesVO.setMinimoClave3(dcaVO.getMinimo());
		     break;
		    case 3:
		     clavesVO.setClave4(dcaVO.getDescripcion());
		     clavesVO.setFormatoClave4(dcaVO.getFormato());
		     clavesVO.setMaximoClave4(dcaVO.getMaximo());
		     clavesVO.setMinimoClave4(dcaVO.getMinimo());
		     break;
		    case 4:
		     clavesVO.setClave5(dcaVO.getDescripcion());
		     clavesVO.setFormatoClave5(dcaVO.getFormato());
		     clavesVO.setMaximoClave5(dcaVO.getMaximo());
		     clavesVO.setMinimoClave5(dcaVO.getMinimo());
		     break;
		    }
		    
		   }
		  }
		 //logger.debug("CLAVEIMPLEMENTACION----"+clavesVO.getClave());
		 //logger.debug("FORMATOCLAVEIMPLEMENTACION----"+clavesVO.getFormatoClave());
		 //logger.debug("CLAVEIMPLEMENTACION2----"+clavesVO.getClave2());
		 //logger.debug("FORMATOCLAVEIMPLEMENTACION2----"+clavesVO.getFormatoClave2());
		  return clavesVO;
		 }

	public List<DatosClaveAtributoVO> convierteAListaDCAVO(ClavesVO clavesVO)
			throws ApplicationException {
		  List<DatosClaveAtributoVO> ldcaVO= new ArrayList<DatosClaveAtributoVO>();
		  DatosClaveAtributoVO dcaVO=null;
		  if(clavesVO.getClave()!=null && StringUtils.isNotBlank(clavesVO.getClave())){
		   dcaVO= new DatosClaveAtributoVO();
		   dcaVO.setNumeroClave(1);
		   dcaVO.setDescripcion(clavesVO.getClave());
		   dcaVO.setFormato(clavesVO.getFormatoClave());
		   dcaVO.setMaximo(clavesVO.getMaximoClave());
		   dcaVO.setMinimo(clavesVO.getMinimoClave());
		   ldcaVO.add(dcaVO);
		  }
		  if(clavesVO.getClave2()!=null && StringUtils.isNotBlank(clavesVO.getClave2())){
		   dcaVO= new DatosClaveAtributoVO();
		   dcaVO.setNumeroClave(2);
		   dcaVO.setDescripcion(clavesVO.getClave2());
		   dcaVO.setFormato(clavesVO.getFormatoClave2());
		   dcaVO.setMaximo(clavesVO.getMaximoClave2());
		   dcaVO.setMinimo(clavesVO.getMinimoClave2());
		   ldcaVO.add(dcaVO);
		  }
		  if(clavesVO.getClave3()!=null && StringUtils.isNotBlank(clavesVO.getClave3())){
		   dcaVO= new DatosClaveAtributoVO();
		   dcaVO.setNumeroClave(3);
		   dcaVO.setDescripcion(clavesVO.getClave3());
		   dcaVO.setFormato(clavesVO.getFormatoClave3());
		   dcaVO.setMaximo(clavesVO.getMaximoClave3());
		   dcaVO.setMinimo(clavesVO.getMinimoClave3());
		   ldcaVO.add(dcaVO);
		  }
		  if(clavesVO.getClave4()!=null && StringUtils.isNotBlank(clavesVO.getClave4())){
		   dcaVO= new DatosClaveAtributoVO();
		   dcaVO.setNumeroClave(4);
		   dcaVO.setDescripcion(clavesVO.getClave4());
		   dcaVO.setFormato(clavesVO.getFormatoClave4());
		   dcaVO.setMaximo(clavesVO.getMaximoClave4());
		   dcaVO.setMinimo(clavesVO.getMinimoClave4());
		   ldcaVO.add(dcaVO);
		  }
		  if(clavesVO.getClave5()!=null && StringUtils.isNotBlank(clavesVO.getClave5())){
		   dcaVO= new DatosClaveAtributoVO();
		   dcaVO.setNumeroClave(5);
		   dcaVO.setDescripcion(clavesVO.getClave5());
		   dcaVO.setFormato(clavesVO.getFormatoClave5());
		   dcaVO.setMaximo(clavesVO.getMaximoClave5());
		   dcaVO.setMinimo(clavesVO.getMinimoClave5());
		   ldcaVO.add(dcaVO);
		  }
		  return ldcaVO;
		 }

	public void eliminarTablaClave(Map<String, String> params)
			throws ApplicationException {
		   logger.debug("into ListaValoresManagerImpl.eliminarTablaClave()");
			  try{
		          returnBackBoneInvoke(params, "ELIMINAR_TABLA_CLAVE");
		        	
				 logger.debug("elimino bien");
			  } catch (Exception bae) {
				  logger.error("Exception in invoke 'ELIMINAR_TABLA_CLAVE'", bae);
				  throw new ApplicationException("Error al eliminar tabla clave ");
			  }
			  
	}

	public void guardaClaveListaDeValores(ClavesVO clave,
			String tipoTransaccion, String nmTabla) throws ApplicationException {
		Map params = new HashMap<String, Object>();
		params.put("PI_TIP_TRAN", tipoTransaccion);
		params.put("PI_NMTABLA", nmTabla);
		params.put("PI_DSCLAVE1", clave.getClave());
		params.put("PI_SWFORMA1", clave.getFormatoClave());
		params.put("PI_NMLMIN1", clave.getMinimoClave());
		params.put("PI_NMLMAX1", clave.getMaximoClave());
		params.put("PI_DSCLAVE2", clave.getClave2());
		params.put("PI_SWFORMA2", clave.getFormatoClave2());
		params.put("PI_NMLMIN2", clave.getMinimoClave2());
		params.put("PI_NMLMAX2", clave.getMaximoClave2());
		params.put("PI_DSCLAVE3", clave.getClave3());
		params.put("PI_SWFORMA3", clave.getFormatoClave3());
		params.put("PI_NMLMIN3", clave.getMinimoClave3());
		params.put("PI_NMLMAX3", clave.getMaximoClave3());
		params.put("PI_DSCLAVE4", clave.getClave4());
		params.put("PI_SWFORMA4", clave.getFormatoClave4());
		params.put("PI_NMLMIN4", clave.getMinimoClave4());
		params.put("PI_NMLMAX4", clave.getMaximoClave4());
		params.put("PI_DSCLAVE5", clave.getClave5());
		params.put("PI_SWFORMA5", clave.getFormatoClave5());
		params.put("PI_NMLMIN5", clave.getMinimoClave5());
		params.put("PI_NMLMAX5", clave.getMaximoClave5());
		
		try {
			returnBackBoneInvoke(params, "GUARDA_CLAVE_LISTA_DE_VALORES");

        } catch (Exception e) {
            throw new ApplicationException("Error intentando guardar la clave de la lista de valores");
        }
		
	}

	public void guardaDescripcionListaDeValores(
			List<ListaDeValoresVO> descripcion, String tipoTransaccion,
			String nmTabla) throws ApplicationException {
		
		for(ListaDeValoresVO lista : descripcion){
			try {
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("PI_TIP_TRAN", tipoTransaccion);
				params.put("PI_NMTABLA", nmTabla);
				params.put("PI_CDATRIBU", lista.getCdAtribu());
				params.put("PI_DSATRIBU", lista.getDescripcionFormato());
				params.put("PI_SWFORMAT", lista.getFormatoDescripcion());
				params.put("PI_NMLMIN", lista.getMinimoDescripcion());
				params.put("PI_NMLMAX", lista.getMaximoDescripcion());
				
	            returnBackBoneInvoke(params, "GUARDA_DESCRIPCION_LISTA_DE_VALORES");
	        } catch (Exception e) {
	            throw new ApplicationException("Error intentando guardar la descripcion de la lista de valores");
	        }
		}
		
	}

	public String guardaListaValores(ListaDeValoresVO lista)
			throws ApplicationException {
		
		
        ListaDeValoresVO nmTabla = new ListaDeValoresVO();
        HashMap<String, Object> params =  new HashMap<String, Object>();
        params.put("PI_CDTABLA", lista.getNombre());
        params.put("PI_NMTABLA", lista.getNumeroTabla());
        params.put("PI_DSTABLA", lista.getDescripcion());
        params.put("PI_OTTIPOAC", lista.getOttipoac());
        params.put("PI_OTTIPOTB", lista.getOttipotb());
        params.put("PI_SWMODIFI", lista.getModificaValores());
        params.put("PI_SWERROR", lista.getEnviarTablaErrores());
        params.put("PI_CLNATURA", lista.getClnatura());
        params.put("PI_CDTABLJ1", lista.getCdCatalogo1());
        params.put("PI_CDTABLJ2", lista.getCdCatalogo2());
        params.put("PI_CDTABLJ3", lista.getClaveDependencia());

        try {
			WrapperResultados res = returnBackBoneInvoke(params, "GUARDA_LISTA_DE_VALORES");
			nmTabla.setNumeroTabla((String)res.getItemMap().get("PI_NMTABLA"));
            
            if(logger.isDebugEnabled()){
            	logger.debug("NUMERO TABLA----------"+nmTabla.getNumeroTabla());
            }
        } catch (Exception e) {
            throw new ApplicationException("Error intentando guardar la lista de valores");
        }
        return nmTabla.getNumeroTabla();
		
	}

	public List<LlaveValorVO> guardaValoresCargaManual(List<LlaveValorVO> listaGrid, String nmTabla, List<LlaveValorVO> listaClavesDependencia , String cdTabla) throws ApplicationException {
		List<LlaveValorVO> clavesNoInsertadas = new ArrayList<LlaveValorVO>();
		boolean eliminar = false;
		if(listaClavesDependencia != null && !listaClavesDependencia.isEmpty()) eliminar = true;
		
		Map params = new HashMap<String, String>();
		params.put("PI_NMTABLA", nmTabla);
		
		if(listaGrid!=null && !listaGrid.isEmpty()){
			for(LlaveValorVO claveVO : listaGrid ){
				boolean inserted = true;
				WrapperResultados res = null;
				
				claveVO.setValue(StringEscapeUtils.escapeHtml4(claveVO.getValue()));
				params.put("PI_OTCLAVE", claveVO.getKey());
				params.put("PI_OTVALOR", claveVO.getValue());
				params.put("PI_ACCION", claveVO.getNick());
					
				try {
					res = returnBackBoneInvoke(params, "GUARDA_LISTA_CARGA_MANUAL");
					
					if(res != null && StringUtils.isNotBlank(res.getMsgId())){
						if(res.getMsgId().equals(Constantes.REGISTRO_DUPLICADO)){
							LlaveValorVO claveRep = new LlaveValorVO();
							claveRep.setKey(claveVO.getKey());
							clavesNoInsertadas.add(claveRep);
							inserted = false;
						}
						
						if(res.getMsgTitle().equals("1")){
							inserted = false;
						}
					}
					
					//Se valida si el registro tenia unId anterior, para esto si ese registro ya fue insertado, si el valor original no esta Vacio
					// y si la lista de elementos a eliminar no esta vacia. Con esos datos se hace el match para eliminar el registro pasado si se encuentra en la lista
					if(StringUtils.isNotBlank(claveVO.getId()) && eliminar ){
						
						for(LlaveValorVO claveEliminar : listaClavesDependencia){
							if(StringUtils.isNotBlank(claveEliminar.getKey()) && claveEliminar.getKey().equals(claveVO.getId())){
								if(inserted){
									eliminaValoresCargaManual( claveEliminar , cdTabla);
								}
								claveEliminar.setKey("");
								
							}
						}
							
						
					}
			
				} catch (Exception e) {
					logger.error("Error intentando guardar la lista de valores de carga manual" + e.getMessage() );
					throw new ApplicationException("Error al guardar la lista de valores de carga manual. Consulte a su soporte");
				}		
			}
		}
		if(eliminar){
			logger.debug("PARA ELIMINAR FINALES: "+ listaClavesDependencia);
			for(LlaveValorVO claveEliminar : listaClavesDependencia){
				if(StringUtils.isNotBlank(claveEliminar.getKey())){
					eliminaValoresCargaManual( claveEliminar , cdTabla);
				}
			}	
		}
		
        return clavesNoInsertadas;
	}

	public List<LlaveValorVO> listaClavesDependencias(String codigoTabla,
			String langCode) throws ApplicationException {
	 	Map params = new HashMap<String, String>();
		params.put("PV_CDTABLA_I", codigoTabla);
		params.put("PV_LANGCODE", langCode);
		
		List<LlaveValorVO> clavesDependencia = null;
		try {
			WrapperResultados res = returnBackBoneInvoke(params, "OBTIENE_LISTA_CLAVES_DEPENDENCIAS");
            clavesDependencia = (List<LlaveValorVO>)res.getItemList();
        } catch (Exception e) {
            throw new ApplicationException("Error intentando cargar la lista de claves de dependencia");
        }
        return clavesDependencia;
 }

	public PagedList listaValoresCargaManual(String tabla , int start , int limit)
			throws ApplicationException {

		HashMap map = new HashMap();
		map.put("PI_TABLA", tabla);
		map.put("PI_ATRIB_DESC", null);
		map.put("PI_NOM_CLAVE01", null);
		map.put("PI_NOM_CLAVE02", null);
		map.put("PI_VAL_CLAVE02", null);
		map.put("PI_NOM_CLAVE03", null);
		map.put("PI_VAL_CLAVE03", null);
		map.put("PI_NOM_CLAVE04", null);
		map.put("PI_VAL_CLAVE04", null);
		map.put("PI_NOM_CLAVE05", null);
		map.put("PI_VAL_CLAVE05", null);
		
        return pagedBackBoneInvoke(map, "OBTIENE_LISTA_CARGA_MANUAL", start, limit);
	}

	public ListaDeValoresVO obtieneCabeceraListaDeValores(String nmTabla)
			throws ApplicationException {
		 
		 ListaDeValoresVO cabecera = null;
	        try {
	        	HashMap<String,Object> params = new HashMap<String, Object>();
	        	params.put("PI_NMTABLA", nmTabla);
	        	WrapperResultados res = returnBackBoneInvoke(params, "OBTIENE_TABLA_1Y5_CLAVES");
	        	
	        	cabecera = (ListaDeValoresVO) res.getItemList().get(0);
	        
	        } catch (Exception e) {
	        	logger.error(e);
	            throw new ApplicationException("Error regresando la cabecera de lista de valores");
	        }
	        return cabecera;
	 }

	public ListaDeValoresVO obtieneClaveListaDeValores(String nmTabla)
			throws ApplicationException {
		 
		 ListaDeValoresVO clave = null;
	        try {
	        	HashMap<String,Object> params = new HashMap<String, Object>();
	        	params.put("PI_NMTABLA", nmTabla);
	        	WrapperResultados res = returnBackBoneInvoke(params, "OBTIENE_CLAVE_LISTA_DE_VALORES");
	        	clave = (ListaDeValoresVO) res.getItemList().get(0);
	        	
	        } catch (Exception e) {
	        	logger.error(e);
	            throw new ApplicationException("Error regresando la clave de lista de valores");
	        }
	        return clave;
	 }

	public ListaDeValoresVO obtieneDescripcionListaDeValores(String nmTabla)
			throws ApplicationException {
		 
		 ListaDeValoresVO descripcion = null;
	        try {
	        	HashMap<String,Object> params = new HashMap<String, Object>();
	        	params.put("PI_NMTABLA", nmTabla);
	        	WrapperResultados res = returnBackBoneInvoke(params, "OBTIENE_DESCRIPCION_LISTA_DE_VALORES");
	        	descripcion = (ListaDeValoresVO) res.getItemList().get(0);
	        
	        } catch (Exception e) {
	        	logger.error(e);
	            throw new ApplicationException("Error regresando la descripcion de lista de valores");
	        }
	        return descripcion;
	 }

	public void eliminaValoresCargaManual(LlaveValorVO llaveEliminar, String cdTabla) throws ApplicationException {
		//logger.debug("******numero tabla impl"+nmTabla);
		Map params = new HashMap<String, String>();
		params.put("pi_tabla", cdTabla);
		params.put("pi_clave01", llaveEliminar.getKey());
		params.put("pi_clave02", null);
		params.put("pi_clave03", null);
		params.put("pi_clave04", null);
		params.put("pi_clave05", null);
		
		try {
			returnBackBoneInvoke(params, "ELIMINA_LISTA_CARGA_MANUAL");
        } catch (Exception e) {
            throw new ApplicationException("Error intentando guardar la lista de valores de carga manual");
        }
	}

}
