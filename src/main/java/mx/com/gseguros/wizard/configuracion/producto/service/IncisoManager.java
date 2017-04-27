package mx.com.gseguros.wizard.configuracion.producto.service;

import java.util.List;

import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.incisos.model.IncisoVO;

/**
 * IncisoManager
 * 
 * <pre>
 *    Interfaz para petici�n de informaci�n de inciso
 * &lt;Pre&gt;
 * 
 * &#064;author   &lt;a href=&quot;mailto:edgar.perez@biosnetmx.com&quot;&gt;Edgar P&amp;erez&lt;/a&gt;
 * &#064;version	 2.0
 * 
 * &#064;since	 1.0
 * 
 */

public interface IncisoManager {

	/**
	 * Extrae todos los incisos de un producto.
	 * @return Lista con la informacion de todos los incisos de un
	 *         producto solicitado.
	 * @throws ApplicationException -
	 * 				Es lanzada en errores de configuracion de aplicacion error en
	 *             	las consultas a BD.
	 */
	public abstract List<IncisoVO> incisosDelProducto(String codigoRamo, String codigoTipo)throws ApplicationException;
	
	/**
	 * Extrae todos los incisos asociados a un producto.
	 * 
	 * @return Lista con la informacion de todos los incisos asociados al
	 *         producto solicitado.
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	public abstract List<IncisoVO> incisosJson()throws ApplicationException;
	
	/**
	 * Agrega un nuevo inciso(riesgo) al catalogo
	 * @param inciso
	 * @return
	 * @throws ApplicationException -
	 * Es lanzada en errores de configuracion de aplicacion error en las consultas a BD.
	 */
	public abstract WrapperResultados agregarInciso(IncisoVO inciso)throws ApplicationException;
	
	/**
	 * 
	 * @param incisoAsociado
	 * @throws ApplicationException - 
	 * 				Es lanzada en errores de configuracion de aplicacion error en
	 *             	las consultas a BD.
	 */
	public abstract void asociarInciso(IncisoVO incisoAsociado)throws ApplicationException;

	/**
	 * 
	 * @param inciso a eliminar
	 * @throws ApplicationException - 
	 * 				Es lanzada en errores de configuracion de aplicacion error en
	 *             	las consultas a BD.
	 */
	public abstract void eliminarInciso(IncisoVO inciso)throws ApplicationException;

}
