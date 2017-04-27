package mx.com.gseguros.portal.general.dao;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.model.ParamReporteVO;
import mx.com.gseguros.portal.general.model.ReporteVO;

public interface ReportesDAO {
	
	
	/**
	 * Obtiene la lista de los reportes disponibles
	 * @return lista de reportes
	 * @throws Exception
	 */
	public List<ReporteVO> obtenerListaReportes() throws Exception;
	
	
	/**
	 * Obtiene los parametros que necesita un reporte
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<ComponenteVO> obtenerParametrosReporte(Map<String, Object> params) throws Exception;
	
	
	/**
	 * Actualiza el valor de un parametro asociado a un reporte
	 * @param cdreporte
	 * @param username
	 * @param paramReporteVO
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> actualizarParametroReporte(String cdreporte, String username, ParamReporteVO paramReporteVO) throws Exception;
	
	
	/**
	 * Arma el reporte con los valores de parametros actuales y lo almacena
	 * @param cdreporte codigo del reporte a armar
	 * @param username usuario asociado al reporte
	 * @throws Exception
	 */
	public void armarReporte(String cdreporte, String username) throws Exception;
	
	
	/**
	 * Extrae el flujo de datos del reporte almacenado previamente
	 * @param cdreporte codigo del reporte a extraer
	 * @param username usuario acosiado al reporte
	 * @return flujo de datos que el conforman el reporte
	 * @throws Exception
	 */
	public InputStream obtenerReporte(String cdreporte, String username) throws Exception;

	public InputStream exportaTablaApoyo(Map<String, String> params) throws Exception;
	
}