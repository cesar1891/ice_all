package mx.com.gseguros.wizard.configuracion.producto.service;

import java.util.List;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.service.impl.TablaApoyoManagerImpl;

/**
 * interface que contien los metodos expuestos por uno de los Manager para la
 * consulta de las tablas de apoyo que existan en la base de datos.
 * 
 * @since 1.0
 * @author <a href="mailto:adolfo.gonzalez@biosnettcs.com">Adolfo Gonzalez</a>
 * @version $Id$
 * @see TablaApoyoManagerImpl
 */
public interface TablaApoyoManager {

	/**
	 * Metodo que se utiliza para obtener los datos de la tabla de apoyo de
	 * Naturales.
	 * 
	 * @return Lista con los valores que se encuentren en base de datos.
	 *         <p>
	 *         El metodo devuelve la lista llena si encuentra resultados, de lo
	 *         contrario retorna una lista vacia, no retorna <b>null</b>
	 * @throws ApplicationException
	 */
	List<LlaveValorVO> obtenerListaNaturales() throws ApplicationException;

	/**
	 * Metodo que se utiliza para obtener los datos de la tabla de apoyo de Tipo
	 * de Seguros.
	 * 
	 * @return Lista con los valores que se encuentren en base de datos.
	 *         <p>
	 *         El metodo devuelve la lista llena si encuentra resultados, de lo
	 *         contrario retorna una lista vacia, no retorna <b>null</b>
	 * @throws ApplicationException
	 */
	List<LlaveValorVO> obtenerTipoSeguros() throws ApplicationException;

	/**
	 * Metodo que se utiliza para obtener los datos de la tabla de apoyo de
	 * Obligatoriedad de los Roles.
	 * 
	 * @return Lista con los valores que se encuentren en base de datos.
	 *         <p>
	 *         El metodo devuelve la lista llena si encuentra resultados, de lo
	 *         contrario retorna una lista vacia, no retorna <b>null</b>
	 * @throws ApplicationException
	 */
	List<LlaveValorVO> obtenerObligatoriedadRol() throws ApplicationException;
	
	/**
	 * Metodo que se utiliza para obtener los datos de la tabla de apoyo de
	 * las leyendas para la Suma asegurada.
	 * 
	 * @return Lista con los valores que se encuentren en base de datos.
	 *         <p>
	 *         El metodo devuelve la lista llena si encuentra resultados, de lo
	 *         contrario retorna una lista vacia, no retorna <b>null</b>
	 * @throws ApplicationException
	 */
	List<LlaveValorVO> obtenerLeyendaSumaAsegurada() throws ApplicationException;		
}