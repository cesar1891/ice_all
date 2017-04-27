package mx.com.gseguros.portal.general.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.portal.general.dao.ReportesDAO;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.model.ParamReporteVO;
import mx.com.gseguros.portal.general.model.ReporteVO;
import mx.com.gseguros.portal.general.service.ReportesManager;

import org.apache.log4j.Logger;

public class ReportesManagerImpl implements ReportesManager {
	
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ReportesManagerImpl.class);
	
	private ReportesDAO reportesDAO;


	@Override
	public List<ReporteVO> obtenerListaReportes() throws Exception {
		return reportesDAO.obtenerListaReportes();
	}


	@Override
	public List<ComponenteVO> obtenerParametrosReportes(String cdreporte, String cdPantalla, String cdSeccion) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdreporte_i",  cdreporte);
		params.put("pv_cdpantalla_i", cdPantalla);
		params.put("pv_cdseccion_i",  cdSeccion);
		return reportesDAO.obtenerParametrosReporte(params);
	}
	
	
	@Override
	public InputStream obtenerDatosReporte(String cdreporte, String username, Map<String, String> params) throws Exception {
		
		// Se genera una lista con los parametros de entrada del reporte:
		List<ParamReporteVO> paramsReporte = new ArrayList<ParamReporteVO>();
		for(Map.Entry<String, String> entry : params.entrySet()){
		    ParamReporteVO paramReporte = new ParamReporteVO();
		    paramReporte.setNombre(entry.getKey());
		    paramReporte.setValor(entry.getValue());
		    paramsReporte.add(paramReporte);
		}
		
		logger.debug(">>>>> Params en obtenerDatosReporte: "+params);
		
		// Se actualizan los valores de los parametros del reporte:
		for(ParamReporteVO paramReporte : paramsReporte) {
			reportesDAO.actualizarParametroReporte(cdreporte, username, paramReporte);
		}
		
		// Se arma el reporte:
		reportesDAO.armarReporte(cdreporte, username);
		
		// Se obtienen los bytes del reporte:
		return reportesDAO.obtenerReporte(cdreporte, username);
	}
	
	
	public InputStream exportaTablaApoyo(Map<String, String> params) throws Exception{
		
		return reportesDAO.exportaTablaApoyo(params);
	}
	
	public void setReportesDAO(ReportesDAO reportesDAO) {
		this.reportesDAO = reportesDAO;
	}

}