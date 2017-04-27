package mx.com.aon.tmp;

import mx.com.aon.portal.service.PagedList;
import mx.com.gseguros.exception.ApplicationException;

public interface PagerManager {
	
	/**
	 * Se consultan los datos a partir de una serie de atributos
	 * para poder limitar la consulta al tamano y posicion en la tabla.
	 *
	 * @param arg objeto que contiene los parametros para la busqueda
	 * @param endpointName nombre del endpoint al que se va a consultar
	 * @param start linea inicial de consulta
	 * @param limit cantidad de registros a ser consultados
	 * @return Lista con los bean's obtenidos de la consulta
	 * @throws ApplicationException Excepcion con la informacion y descripcion del problema en la ejecucion
	 */
	PagedList getPagedData(Object arg, String endpointName, int start, int limit) throws ApplicationException;
}
