package mx.com.gseguros.wizard.configuracion.producto.service;

import java.util.List;

import mx.com.aon.portal.service.PagedList;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.DatosClaveAtributoVO;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.DescripcionCincoClavesVO;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.DescripcionVeinticincoAtributosVO;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.ValoresCincoClavesVO;

/**
 * Metodo paraq 
 * Interfaz para petici�n de informaci�n valores para la tabla de cinco claves para el uso de JdbcTemplate
 *
 */
public interface TablaCincoClavesManagerJdbcTemplate {
	
	/**
	 * Obtiene los valores de las descripcionesde las claves para la tabla de 5 claves
	 * @param numeroTabla numero de tabla
	 * @param inicio del paginado
	 * @param limite del paginado
	 * @return lista de objetos DescripcionCincoClavesVO con las descripciones para la tabla de cinco claves 
	 * @throws ApplicationException
	 */
	public PagedList obtieneValoresClave(String nmTabla , int start , int limit) throws ApplicationException;
	
	
	/**
	 * Metodo para Borrar un registro de claves de la tabla de 5 claves dada por nmtabla
	 * @param nmTabla
	 * @param clave1
	 * @param clave2
	 * @param clave3
	 * @param clave4
	 * @param clave5
	 * @return Mensaje Resultado
	 * @throws ApplicationException
	 */
	public String borraValoresClave(String nmTabla , String clave1, String clave2, String clave3, String clave4, String clave5) throws ApplicationException;
	
	public List<DatosClaveAtributoVO> consultaCincoClaves(String nmtabla) throws ApplicationException;

	public List<DatosClaveAtributoVO> consultaDescripciones(String nmtabla) throws ApplicationException;
	public DescripcionVeinticincoAtributosVO obtieneDescripcionAtributosCincoClaves(String nmTabla)throws ApplicationException;
	public List<LlaveValorVO> obtieneValorDummyAtributos(String identificador , DescripcionVeinticincoAtributosVO descripcionAtributos);
	public List<LlaveValorVO> obtieneValoresAtributos(String nmTabla, DescripcionCincoClavesVO claves, DescripcionVeinticincoAtributosVO descripcionAtributos)throws ApplicationException;
	public List<DescripcionCincoClavesVO> insertaValoresCincoClaves(String nmTabla , List<ValoresCincoClavesVO> listaValores5Claves) throws ApplicationException;
	
}
