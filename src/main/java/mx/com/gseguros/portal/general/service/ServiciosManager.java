package mx.com.gseguros.portal.general.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ServiciosManager {
	
    public String reemplazarDocumentoCotizacion(StringBuilder sb, String cdunieco,String cdramo,String estado,String nmpoliza) throws Exception;
	
	public void grabarEvento(
			StringBuilder sb
			,String cdmodulo
			,String cdevento
			,Date fecha
			,String cdusuari
			,String cdsisrol
			,String ntramite
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsolici
			,String cdagente
			,String cdusuariDes
			,String cdsisrolDes
			)throws Exception;
	
	public void recibosSubsecuentes(
			String rutaDocumentosTemporal
			,boolean test
			) throws Exception;
	
	public int generarFlagsTramites () throws Exception;
	
	public int actualizarFlagsTramites () throws Exception;
	
	public void bloquearProceso (String cdproceso, boolean bloquear, String cdusuari, String cdsisrol) throws Exception;
	
	public List<Map<String, String>> recuperarPersonasPorRFCoPorNombre (String rfc, String nombre) throws Exception;
    
    public List<Map<String, String>> recuperarPolizasPorCdpersonYcdramo (String cdperson, String cdramo) throws Exception;
    
    public List<Map<String, String>> recuperarCoberturasAmparadasPorPolizaYasegurado (String cdunieco, String cdramo, String estado,
            String nmpoliza, String nmsuplem, String nmsituac) throws Exception;
    
    public Map<String, String> recuperarDetallePoliza (String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception;
    
    public List<Map<String, String>> recuperarDetalleAseguradosPoliza (String cdunieco, String cdramo, String estado, String nmpoliza,
            String nmsuplem) throws Exception;
    
}