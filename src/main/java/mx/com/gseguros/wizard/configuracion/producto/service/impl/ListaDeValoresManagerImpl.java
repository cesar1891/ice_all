package mx.com.gseguros.wizard.configuracion.producto.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal.service.PagedList;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.model.ClavesVO;
import mx.com.gseguros.wizard.configuracion.producto.model.ListaDeValoresVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.service.ListaDeValoresManager;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.DatosClaveAtributoVO;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 
 * @author Edgar Perez
 * @version 2.0
 * @since 1.0
 * 
 * Clase que implementa los metodos para agregar y obtener 
 * las los catalogos de la lista de valores asociadas a un producto
 *
 */
public class ListaDeValoresManagerImpl implements ListaDeValoresManager {

	private static Logger logger = Logger.getLogger(ListaDeValoresManagerImpl.class);
	
	/**
	 * Mapa en el cual se introducen los Manager's para ser extraidos y
	 * utilizados como servicios.
	 */
	
	private Map endpoints;
	
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
	public List<ListaDeValoresVO> listaDeValoresCatalogo(String valor)
			throws ApplicationException {return null;}


	/**
	 * Implementacion que guarda la lista de valores.
	 * 
	 * @return NmTabla numero de la tabla generado despues de guardar la primera vez
	 * 			en la BD la lista de valores
	 *         
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	public String  guardaListaValores(ListaDeValoresVO lista)
			throws ApplicationException {return null;}
	
	/**
	 * Implementacion que guarda la clave de la lista de valores.
	 *
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	//insertar objeto clavesVO ***modificado
	public void guardaClaveListaDeValores(ClavesVO clave,String tipoTransaccion,String nmTabla)
			throws ApplicationException {return;}

	/**
	 * Implementacion que guarda la descripcion de la tabla una y cinco claves.
	 *  
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	public void guardaDescripcionListaDeValores(List<ListaDeValoresVO> descripcion,String tipoTransaccion ,String nmTabla)
	throws ApplicationException {return;}
	
	/**
	 * Implementacion que guarda la descripcion de la tabla una y cinco claves.
	 *
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	public List<LlaveValorVO> guardaValoresCargaManual(List<LlaveValorVO> listaGrid, String nmTabla, List<LlaveValorVO> listaClavesDependencia , String cdTabla)
	throws ApplicationException {return null;}
	
	
	/**
	 * Implementacion que guarda la descripcion de la tabla una y cinco claves.
	 * @param LlaveValorVO llave a Eliminar
	 * @param String cdTabla
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	public void eliminaValoresCargaManual(LlaveValorVO llaveEliminar, String cdTabla)
	throws ApplicationException {return;}
	//*********************** 5 claves*******************************
	/**
	 * Implementacion que obtiene los valores de la tabla una y cinco claves.
	 * @return objeto ListaDeValoresVO
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	public ListaDeValoresVO consultaTabla(String nmTabla)
	throws ApplicationException {return null;}

	/**
	 * Implementacion que obtiene los valores de las claves de la tabla una y cinco claves.
	 * @return listaClaves List<DatosClaveAtributoVO>
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	//regresar objeto tipo ClavesVO ****modificado
	public List<DatosClaveAtributoVO> consultaClaves(String nmTabla)
	throws ApplicationException {return null;}
	
	/**
	 * Implementacion que obtiene los valores de las descripciones de la tabla una y cinco claves.
	 * @return listaDescripciones List<DatosClaveAtributoVO>
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	public List<DatosClaveAtributoVO> consultaDescripciones(String nmTabla)
	throws ApplicationException {return null;}
	
	/**
	 * Implementacion que obtiene los valores del grid carga manual de la tabla una clave.
	 * @return listaCargaManual List<LlaveValorVO>
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	public PagedList listaValoresCargaManual(String tabla , int start , int limit)
	throws ApplicationException {
		/**
		 * TODO generate Code
		 */
	        return null;
	}
	
	//***********************conversion de objetos a lista y viceversa*******************************
	
	 public ClavesVO convierteAClaveVO(List<DatosClaveAtributoVO> dca) throws ApplicationException{
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
	
	 public List<DatosClaveAtributoVO> convierteAListaDCAVO(ClavesVO clavesVO){
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
	 
	 
	 //************** Edicion de lista de valores****************************
	 public ListaDeValoresVO obtieneCabeceraListaDeValores(String nmTabla) throws ApplicationException {return null;}

	 public ListaDeValoresVO obtieneClaveListaDeValores(String nmTabla) throws ApplicationException {return null;}

	 public ListaDeValoresVO obtieneDescripcionListaDeValores(String nmTabla) throws ApplicationException {return null;}
	 
	 public List<LlaveValorVO> listaClavesDependencias(String codigoTabla,
			 String langCode) throws ApplicationException {return null;}

	 public void setEndpoints(Map endpoints) {
		this.endpoints = endpoints;
	}
	 
	 
		
	 
	public void eliminarTablaClave(Map<String, String> params) throws ApplicationException {return;}
	 
	 
	 

}
