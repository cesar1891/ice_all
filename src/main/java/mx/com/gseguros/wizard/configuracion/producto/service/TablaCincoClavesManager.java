package mx.com.gseguros.wizard.configuracion.producto.service;

import java.util.List;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.model.ClavesVO;
import mx.com.gseguros.wizard.configuracion.producto.model.ListaDeValoresVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.DatosClaveAtributoVO;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.DescripcionCincoClavesVO;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.DescripcionVeinticincoAtributosVO;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.ValoresCincoClavesVO;

/**
 * TablaCincoClavesManager
 * 
 * <pre>
 *    Interfaz para petici�n de informaci�n valores para la tabla de cinco claves
 * &lt;Pre&gt;
 * 
 * &#064;author   &lt;a href=&quot;mailto:edgar.perez@biosnetmx.com&quot;&gt;Edgar P&amp;erez&lt;/a&gt;
 * &#064;version	 2.0
 * &#064;since	 1.0
 * 
 */
public interface TablaCincoClavesManager {

	/**
	 * Obtiene las descripciones de las claves para la tabla de  5 claves
	 * 
	 * @return objeto DescripcionCincoClavesVO con las descripciones de las claves para la 
	 * 		tabla de cinco claves
	 * @param numeroTabla
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	DescripcionCincoClavesVO obtieneDescripcionCincoClaves(String nmTabla)throws ApplicationException;
	
	/**
	 * Obtiene las descripciones de los atributos para la tabla de  5 claves
	 * 
	 * @return lista de objetos LlaveValorVO con las descripciones de los atributos para la 
	 * 		tabla de cinco claves
	 * @param numeroTabla
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	DescripcionVeinticincoAtributosVO obtieneDescripcionAtributosCincoClaves(String nmTabla)throws ApplicationException;
	
	/**
	 * Inserta los valores de las claves y de atributos para la tabla de  5 claves
	 * 
	 * @return lista de objetos DescripcionCincoClavesVO de las claves que no se pudieron insertar por estar repetidas
	 * @param numeroTabla numero de tabla
	 * @param itipoTransitopoTransito accion de insertar o actualizar (1=insert 2=update)
	 * @param valoresEntrada objeto ValoresCincoClaves
	 * @param listaDescripciones List<DescripcionCincoClavesVO> descripciones iniciales
	 * @param listaDescripcionesanteriores List<DescripcionCincoClavesVO> descripciones anteriores
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	
	List<DescripcionCincoClavesVO> insertaValoresCincoClaves(String nmTabla , List<ValoresCincoClavesVO> listaValores5Claves)
			throws ApplicationException;
	
	/**
	 * Obtiene los valores de las descripciones, atributos y la cabecera para la tabla de 5 claves
	 * 
	 * @return objeto ValoresCincoClavesVO con las descripciones y atributos para la 
	 * 		tabla de cinco claves
	 * @param 
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	//ValoresCincoClavesVO obtieneValoresCincoClaves(String nmTabla,String tipoTabla)throws ApplicationException;
	
	/**
	 * Obtiene los valores de las descripcionesde las claves para la tabla de 5 claves
	 * @param numeroTabla numero de tabla
	 * @return lista de objetos DescripcionCincoClavesVO con las descripciones para la 
	 * 		tabla de cinco claves
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	List<DescripcionCincoClavesVO> obtieneValoresClave(String nmTabla) throws ApplicationException;
	
	/**
	 * Obtiene los valores de los atributos para la tabla de 5 claves
	 * @param Descripcion Veinticinco Atributos
	 * @param numeroTabla numero de tabla
	 * @param claves objeto DescripcionCincoClavesVO 
	 * @return lista List <LlaveValorVO> con los atributos para la 
	 * 		tabla de cinco claves
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	List<LlaveValorVO> obtieneValoresAtributos(String nmTabla,
			DescripcionCincoClavesVO claves, DescripcionVeinticincoAtributosVO descripcionAtributos)throws ApplicationException;
	
	//***********************conversion de objetos a lista y viceversa*******************************
	/**
	 * convierte una lista de DatosClaveAtrivutoVO a objeto de tipo ClavesVO
	 * @param List<DatosClaveAtributoVO> lista de objetos DatosClaveAtributoVO
	 * @return objeto ClavesVO con los atributos para la 
	 * 		tabla de cinco claves
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	ClavesVO convierteAClaveVO(List<DatosClaveAtributoVO> dca)throws ApplicationException;
	
	/**
	 * convierte un objeto de tipo ClavesVO a lista de DatosClaveAtrivutoVO 
	 * @param clavesVO objeto ClavesVO 
	 * @return List<DatosClaveAtributoVO> lista de objetos DatosClaveAtributoVO 
	 * 		tabla de cinco claves
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	List<DatosClaveAtributoVO> convierteAListaDCAVO(ClavesVO clavesVO)throws ApplicationException;
	
	/**
	 * convierte un objeto de tipo DescripcionVeinticincoAtributosVO a lista de LlaveValorVO
	 * @param Identificador
	 * @param Descripcion Veinticinco Atributos  
	 * @param dva objeto DescripcionVeinticincoAtributosVO 
	 * @return List<LlaveValorVO> lista de objetos LlaveValorVO 
	 * 	
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	List<LlaveValorVO> convierteAListaLVVO(String identificador , DescripcionVeinticincoAtributosVO descripcionAtributos , DescripcionVeinticincoAtributosVO dva)throws ApplicationException;
	
	/**
	  * Implementacion del metrodo para obtener la lista de descripcion de atributos que sirve como axiliar en caso de que no haya aun claves agregadas
	  * @param identificador
	  * @param descripcionAtributos
	  * @return Lista de descripciones de atributos dummy
	  */
	public List<LlaveValorVO> obtieneValorDummyAtributos(String identificador , DescripcionVeinticincoAtributosVO descripcionAtributos);
	
	
	/**
	 * convierte una lista de LlaveValorVO a objeto de tipo DescripcionVeinticincoAtributosVO
	 * @param List<LlaveValorVO> lista de objetos LlaveValorVO
	 * @return objeto DescripcionVeinticincoAtributosVO con los atributos para la 
	 * 		tabla de cinco claves
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	DescripcionVeinticincoAtributosVO convierteAdvaVO(List<LlaveValorVO> llvVO) throws ApplicationException;
	
	/**
	 * une la descripcion de Veinticinco Atributos con sus valores 
	 * @param llvVO
	 * @param ldVO
	 * @return jllvVO lista LlaveValorVO - List<LlaveValorVO>
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	List<LlaveValorVO> juntaListas(List<LlaveValorVO> llvVO, List<LlaveValorVO> ldVO)throws ApplicationException;
	
	/**
	 * ordena una lista de tipo List<LlaveValorVO> 
	 * @param llvVO
	 * @return nllvVO lista LlaveValorVO - List<LlaveValorVO>
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	List<LlaveValorVO> ordenaLlvVO(List<LlaveValorVO> llvVO)throws ApplicationException;
	
	/**
	 * convierte una lista de valores a una lista de LlaveValorVO 
	 * @param lldvVO List<ListaDeValoresVO>
	 * @return List<LlaveValor>
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	List<LlaveValorVO> convierteALlaveValorVO(List<ListaDeValoresVO> lldvVO) throws ApplicationException;
	
	/**
	 * convierte una lista List<DatosClaveAtributoVO> de valores a una lista de LlaveValorVO 
	 * @param lldvVO List<DatosClaveAtributoVO>
	 * @return List<LlaveValor>
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	List<LlaveValorVO> convierteALlaveValorVODeDatos(List<DatosClaveAtributoVO> lldvVO)throws ApplicationException;
	/**
	 * une la lista de tipo LlaveValorVO original con la temporal
	 * @param base List<LlaveValorVO>
	 * @param temporal List<LlaveValorVO>
	 * @return List<LlaveValorVO>
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	List<LlaveValorVO> juntaBaseYTemporal(List<LlaveValorVO> base,List<LlaveValorVO> temporal)throws ApplicationException;
}
