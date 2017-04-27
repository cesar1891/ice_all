package mx.com.gseguros.portal.general.validacionformato;

import java.io.File;
import java.util.List;

/**
 * Valida datos de archivo con cierto formato
 * @author Ricardo
 *
 */
public interface ValidacionesFormatoStrategy {

	/**
	 * Ejecuta las validaciones de formato a un archivo de entrada con un formato especifico
	 *  
	 * @param archivo Archivo a validar
	 * @param campos Descripcion de los campos a validar
	 * @param nombreArchivoErrores Nombre que tendra  el archivo de errores (si existen)
	 * @return File con los mensajes de error o null si no existieron errores
	 * @throws Exception
	 */
	public File ejecutaValidacionesFormato(File archivo, List<CampoVO> campos, String nombreArchivoErrores) throws Exception;
	
}