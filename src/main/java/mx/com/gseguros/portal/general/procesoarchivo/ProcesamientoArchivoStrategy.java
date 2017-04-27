package mx.com.gseguros.portal.general.procesoarchivo;

import java.io.File;
import java.util.List;

import mx.com.gseguros.portal.general.model.RespuestaVO;
import mx.com.gseguros.portal.general.validacionformato.CampoVO;

/**
 * Realiza el procesamiento de archivos de distintos tipos
 * @author Ricardo
 */
public interface ProcesamientoArchivoStrategy {
	
	
	
	/**
	 * Realiza el proceso del archivo para cierre de periodos
	 * @param archivo
	 * @param campos
	 * @param nmtabla
	 * @param tipoproceso
	 * @param feCierre
	 * @return
	 * @throws Exception
	 */
	public RespuestaVO ejecutaProcesamiento(File archivo, List<CampoVO> campos, Integer nmtabla, Integer tipoproceso, String feCierre) throws Exception;
	
}