package mx.com.gseguros.portal.general.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaImapVO;
import mx.com.gseguros.portal.general.model.DetalleReciboVO;
import mx.com.gseguros.portal.general.model.ReciboVO;

public interface RecibosManager {

	/**
	 * 
	 * @param cdunieco
	 * @param cdramo
	 * @param nmpoliza
	 * @param nmsuplem
	 * @return
	 * @throws Exception
	 */
	public List<ReciboVO> obtieneRecibos(String cdunieco, String cdramo, String nmpoliza, String nmsuplem) throws Exception;

	public ManagerRespuestaImapVO pantallaRecibosSISA(String cdsisrol) throws Exception;
	
	/**
	 * Obtiene los detalles de un recibo
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmrecibo
	 * @return lista del detalle del recibo solicitado
	 * @throws Exception
	 */
	public List<DetalleReciboVO> obtieneDetallesRecibo(String cdunieco, String cdramo, String estado, String nmpoliza, String nmrecibo) throws Exception;
	
	public List<Map<String, String>> obtenerDatosRecibosSISA(String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception;
	
	public String consolidarRecibos(String cdunieco, String cdramo, String estado, String nmpoliza, UserVO user, List<Map<String, String>> lista) throws Exception;
	
	public void desconsolidarRecibos(String cdunieco, String cdramo, String estado, String nmpoliza, UserVO user, List<Map<String,String>> lista) throws Exception;
	
	public List<DetalleReciboVO> obtieneDetallesReciboSISA(String cdunieco, String cdramo, String estado, String nmpoliza, String nmrecibo, String nmfolcon) throws Exception;
	
	public void actualizarFolioSIGS(String cdunieco, String cdramo, String estado, String nmpoliza, String rmdbRn, UserVO user, List<Map<String, String>> lista) throws Exception;
	
	public void actualizarReciboSIGS(String cdunieco, String cdramo, String estado, String nmpoliza, UserVO user, List<Map<String, String>> lista) throws Exception;
	
	public InputStream obtenerDatosReporte(String cdunieco, String cdramo, String estado, String nmpoliza, String[] lista) throws Exception;
	
	public InputStream obtenerReporteRecibos(String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception;
	
	public List<Map<String, String>> obtenerBitacoraConsolidacion(String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception;
	
	public void generaDocumentoReciboConsolidado(
            String cdunieco, 
            String cdramo, 
            String estado, 
            String nmpoliza, 
            String nmsuplem, 
            String nmsolici, 
            String ntramite,
            String nmimpres,
            String folio) throws Exception;
	
	public String obtenerLigaRecibo(String cdunieco, String cdramo, String estado, String nmpoliza, String folio) throws Exception;
	
	public String obtenerSuplementoEmision(String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception;
	
	public String obtenerTramiteEmision(String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception;
}
