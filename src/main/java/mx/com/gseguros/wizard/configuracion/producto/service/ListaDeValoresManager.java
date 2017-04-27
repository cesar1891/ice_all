package mx.com.gseguros.wizard.configuracion.producto.service;

import java.util.List;
import java.util.Map;

import mx.com.aon.portal.service.PagedList;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.model.ClavesVO;
import mx.com.gseguros.wizard.configuracion.producto.model.ListaDeValoresVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.DatosClaveAtributoVO;

/**
 * ListaDeValoresManager
 * 
 * <pre>
 *    Interfaz para petici�n de informaci�n de lista de valores
 * &lt;Pre&gt;
 * 
 * &#064;author   &lt;a href=&quot;mailto:edgar.perez@biosnetmx.com&quot;&gt;Edgar P&amp;erez&lt;/a&gt;
 * &#064;version	 2.0
 * &#064;since	 1.0
 * 
 */
public interface ListaDeValoresManager {

	
	/**
	 * Extrae todos los valores de los catalogos dela lista de valores
	 * asociados a un producto.
	 * 
	 * @return Lista con la informacion de todos los catalogos asociados 
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	List<ListaDeValoresVO> listaDeValoresCatalogo(String valor)throws ApplicationException;

	/**
	 * Guarda los valores introducidos en la cabecera de la tabla de una y de cinco claves
	 * @param objeto tipo ListaDeValoresVO
	 * @return NMTABLA numero de la tabla en el cual es insertada la lista de valores
	 * @throws ApplicationException -
	 * 				Es lanzada en errores de configuracion de aplicacion error en
	 *             	las consultas a BD.
	 */
	String guardaListaValores(ListaDeValoresVO lista)throws ApplicationException;

	/**
	 * Guarda la clave de la lista de valores
	 * @param lista de claves
	 * @throws ApplicationException -
	 * 				Es lanzada en errores de configuracion de aplicacion error en
	 *             	las consultas a BD.
	 */
	void guardaClaveListaDeValores(ClavesVO clave, String tipoTransaccion, String nmTabla)throws ApplicationException;

	/**
	 * Guarda la descripcion de la lista de valores
	 * @param lista de descripcion
	 * @throws ApplicationException -
	 * 				Es lanzada en errores de configuracion de aplicacion error en
	 *             	las consultas a BD.
	 */
	void guardaDescripcionListaDeValores(List<ListaDeValoresVO> descripcion,String tipoTransaccion,String nmTabla)throws ApplicationException;


	/**
	 * Guarda la lista de atributos generada en el grid de la lista de valores
	 * @param lista del grid
	 * @param numero de tabla
	 * @throws ApplicationException -
	 * 				Es lanzada en errores de configuracion de aplicacion error en
	 *             	las consultas a BD.
	 * @return List<LlaveValorVO> que contiene las claves que no han sido guardadas
	 */
	List<LlaveValorVO> guardaValoresCargaManual(List<LlaveValorVO> listaGrid, String nmTabla, List<LlaveValorVO> listaClavesDependencia , String nombre)throws ApplicationException;
	
	/**
	 * Elimina la lista de atributos boorrada en el grid de la lista de valores
	 * @param LlaveValorVO llave a Eliminar
	 * @param String cdTabla
	 * @throws ApplicationException -
	 * 				Es lanzada en errores de configuracion de aplicacion error en
	 *             	las consultas a BD.
	 */
	void eliminaValoresCargaManual(LlaveValorVO llaveEliminar, String cdTabla)throws ApplicationException;

	//***********************1 y 5 claves(consulta)*****************************************
	/**
	 * Obtiene los valores de la tabla de 1 y 5 claves
	 * 
	 * @return objeto ListaDeValoresVO con la informacion de la tabla
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	ListaDeValoresVO consultaTabla(String nmTabla)throws ApplicationException;

	/**
	 * Obtiene lista de valores de las claves para la tabla de 1 y 5 claves
	 * 
	 * @return Lista de objetos ClavesVO con la informacion de la tabla
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	List<DatosClaveAtributoVO> consultaClaves(String nmTabla)throws ApplicationException;
	
	/**
	 * Obtiene lista de valores de las descripciones para la tabla de 1 y 5 claves
	 * 
	 * @return Lista de objetos ListaDeValoresVO con la informacion de la descripcion
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	List<DatosClaveAtributoVO> consultaDescripciones(String nmTabla)throws ApplicationException;
	
	//falta definir como se carga la lista del grid(carga manual)
	PagedList listaValoresCargaManual(String tabla , int start , int limit)throws ApplicationException;

	
	//***********************conversion de objetos a lista y viceversa*******************************
	ClavesVO convierteAClaveVO(List<DatosClaveAtributoVO> dca)throws ApplicationException;
	
	List<DatosClaveAtributoVO> convierteAListaDCAVO(ClavesVO clavesVO)throws ApplicationException;

	//***********edicion de lista de valores**********************
	ListaDeValoresVO obtieneCabeceraListaDeValores(String nmTabla) throws ApplicationException;

	ListaDeValoresVO obtieneClaveListaDeValores(String nmTabla)throws ApplicationException;

	ListaDeValoresVO obtieneDescripcionListaDeValores(String nmTabla)throws ApplicationException;

	List<LlaveValorVO> listaClavesDependencias(String codigoTabla,String langCode)throws ApplicationException;
	
	void eliminarTablaClave(Map<String, String> params) throws ApplicationException;
}
