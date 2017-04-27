package mx.com.gseguros.portal.general.validacionformato;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


/**
 * Valida datos de un archivo de acuerdo a un formato establecido
 * @author Ricardo
 *
 */
public class ValidadorFormatoContext {

	private static Logger logger = Logger.getLogger(ValidadorFormatoContext.class);
	
	private Map<Strategy, ValidacionesFormatoStrategy> validacionesFormatoStrategies;
	
	public enum Strategy {
		VALIDACION_EXCEL,
		VALIDACION_CSV
	}
	
	
	/**
	 * Ejecuta validaciones de formato de un archivo
	 * @param archivo Archivo a validar
	 * @param campos Configuracion de campos a validar 
	 * @param nombreArchivoErrores Nombre del archivo de errores a generar en caso de error
	 * @param strategy strategy del archivo a validar
	 * @return File de errores o null si no los hay
	 * @throws Exception
	 */
	public File ejecutaValidacionesFormato(File archivo, List<CampoVO> campos, String nombreArchivoErrores, Strategy strategy) throws Exception {
		
		logger.debug("Entrando a ejecutaValidacionesFormato, strategy=" + strategy);
		
		ValidacionesFormatoStrategy validaFormatoStrategy = validacionesFormatoStrategies.get(strategy);
		return validaFormatoStrategy.ejecutaValidacionesFormato(archivo, campos, nombreArchivoErrores);
	}


	public void setValidacionesFormatoStrategies(Map<Strategy, ValidacionesFormatoStrategy> validacionesFormatoStrategies) {
		this.validacionesFormatoStrategies = validacionesFormatoStrategies;
	}
	
}