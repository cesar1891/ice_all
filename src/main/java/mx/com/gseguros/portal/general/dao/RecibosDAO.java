package mx.com.gseguros.portal.general.dao;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.portal.general.model.DetalleReciboVO;
import mx.com.gseguros.portal.general.model.ReciboVO;

public interface RecibosDAO {
	
	/**
	 * Obtiene los recibos de una p&oacute;liza
	 * @param params
	 * @return lista con los recibos asociados
	 * @throws Exception
	 */
	public List<ReciboVO> obtieneRecibos(Map<String, Object> params) throws Exception;
	
	
	/**
	 * Obtiene los detalles de un recibo
	 * @param params
	 * @return lista con el detalle del recibo
	 * @throws Exception
	 */
	public List<DetalleReciboVO> obtieneDetalleRecibo(Map<String, Object> params) throws Exception;

	public List<Map<String, String>> obtenerDatosRecibosSISA(String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception;
	
	public String consolidarRecibos(String cdunieco, String cdramo, String estado, String nmpoliza, String usuario, List<Map<String, String>> lista) throws Exception;
	
	public void desconsolidarRecibos(String cdunieco, String cdramo, String estado, String nmpoliza, String usuario, String folio) throws Exception;
	
	public List<DetalleReciboVO> obtieneDetalleReciboSISA(String cdunieco, 
	        String cdramo, 
            String estado, 
            String nmpoliza,
            String nmrecibo,
            String nmfolcon) throws Exception;
	
	public List<Map<String, String>> obtenerInfoRecibos(String cdunieco, String cdramo, String estado, String nmpoliza, String nmrecibo, String nmsuplem) throws Exception;
	
	public InputStream obtenerReporte(String cdunieco, String cdramo, String estado, String nmpoliza, String[] lista) throws Exception;
	
	public InputStream obtenerReporteRecibos(String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception;
	
	public List<Map<String, String>> obtenerBitacoraConsolidacion(String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception;
	
	public void borrarDocumentoReciboConsolidado(String cdunieco, String cdramo, String estado, String nmpoliza, String nmfolio) throws Exception;
	
	public String obtenerLigaRecibo(String cdunieco, String cdramo, String estado, String nmpoliza, String folio) throws Exception;
	
	public String obtenerSuplementoEmision(String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception;
	
	public String obtenerTramiteEmision(String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception;
}
