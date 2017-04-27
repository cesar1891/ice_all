package mx.com.gseguros.portal.general.procesoarchivo;

import java.io.File;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.portal.general.model.RespuestaVO;
import mx.com.gseguros.portal.general.validacionformato.CampoVO;

import org.apache.log4j.Logger;

/**
 * Realiza el procesamiento de un archivo
 * @author Ricardo
 *
 */
public class ProcesadorArchivosContext {

	private static Logger logger = Logger.getLogger(ProcesadorArchivosContext.class);
	
	private Map<Strategy, ProcesamientoArchivoStrategy> procesamientoArchivoStrategies;
	
	public enum Strategy {
		ARCHIVO_TABLA_1_CLAVE,
		ARCHIVO_TABLA_5_CLAVES
	}
	
	/**
	 * Ejecuta el procesamiento del archivo
	 * @param archivo
	 * @param campos
	 * @param tipoTabla
	 * @param strategy
	 * @return
	 * @throws Exception
	 */
	public RespuestaVO ejecutaProcesamientoArchivo(File archivo, List<CampoVO> campos, Integer tipoTabla, Integer tipoproceso, String feCierre, Strategy strategy) throws Exception {
		
		logger.debug("Entrando a ejecutaProcesamientoArchivo, strategy=" + strategy);
		
		ProcesamientoArchivoStrategy procesoArchivoStrategy = procesamientoArchivoStrategies.get(strategy);
		return procesoArchivoStrategy.ejecutaProcesamiento(archivo, campos, tipoTabla, tipoproceso,  feCierre);
	}

	
	public void setProcesamientoArchivoStrategies(Map<Strategy, ProcesamientoArchivoStrategy> procesamientoArchivoStrategies) {
		this.procesamientoArchivoStrategies = procesamientoArchivoStrategies;
	}
	
}