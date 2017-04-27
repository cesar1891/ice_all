package mx.com.gseguros.portal.emision.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.portal.consultas.model.DocumentoReciboParaMostrarDTO;
import mx.com.gseguros.portal.emision.model.EmisionVO;


public interface EmisionDAO {
	
	/**
	 * Proceso de emision general
	 * @param cdusuari
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsituac
	 * @param nmsuplem
	 * @param cdelemento
	 * @param cdcia
	 * @param cdplan
	 * @param cdperpag
	 * @param cdperson
	 * @param fecha
	 * @param ntramite
	 * @return Datos de la emision
	 * @throws Exception
	 */
	public EmisionVO emitir(String cdusuari, String cdunieco, String cdramo, String estado, String nmpoliza, 
			String nmsituac, String nmsuplem, String cdelemento, String cdcia, String cdplan, String cdperpag, 
			String cdperson, Date fecha, String ntramite) throws Exception;
	
	/**
	 * Emision remota para RECUPERA Individual
	 * @param cdusuari
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsituac
	 * @param nmsuplem
	 * @param cdelemento
	 * @param cdcia
	 * @param cdplan
	 * @param cdperpag
	 * @param cdperson
	 * @param fecha
	 * @param ntramite
	 * @return Datos de la emision
	 * @throws Exception
	 */
	public EmisionVO emitirPolizaRemotaRecupera(String cdusuari, String cdunieco, String cdramo, String estado, String nmpoliza, 
            String nmsituac, String nmsuplem, String cdelemento, String cdcia, String cdplan, String cdperpag, 
            String cdperson, Date fecha, String ntramite, String polizaremota) throws Exception;
	

	/**
	 * Actualiza el numero de poliza externo para una poliza de autos
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @param nmpoliex
	 * @param cduniext
	 * @param ramoGS
	 * @throws Exception
	 */
	public void actualizaNmpoliexAutos(String cdunieco, String cdramo, String estado, String nmpoliza, 
			String nmsuplem, String nmpoliex, String cduniext, String ramoGS) throws Exception;

	public void insertarMpoliimp(
			String ntramite
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String tipo
			,String nmtraope
			,String nmrecibo
			,String orden
			)throws Exception;
	
	public void marcarTramiteImpreso(String ntramite, String swimpres) throws Exception;
	
	/**
	 * SUMA A LAS REMESAS LA IMPRESION ACTUAL
	 * SI LA SUMA RESULTA ESTAR COMPLETA: ACTUALIZA LAS REMESAS, Y LOS HIJOS EMISION/ENDOSOS SI HAY
	 */
	public boolean sumarImpresiones(String lote, String tipolote, String peso) throws Exception;
	
	public void mostrarRecibosImpresosListaDeListas(List<DocumentoReciboParaMostrarDTO> lista)throws Exception;
	
	public void movimientoClienteTramite(String ntramite, String cdperson, String accion) throws Exception;
	
	public void validarDocumentoTramite (String ntramite, String cddocume) throws Exception;
	
	public String recuperarTramiteCotizacion (String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception;
	
	public void bloquearProceso (String cdproceso, boolean bloquear, String cdusuari, String cdsisrol) throws Exception;
	
	public void revierteEmision(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem) throws Exception;
	
	public List<Map<String, String>> recuperarDocumentosGeneradosPorParametrizacion (String ntramite) throws Exception;
	
	public void actualizarCdplanGrupo(String cdunieco, String cdramo, String estado, String nmpoliza,
			String nmsuplem, String cdgrupo, String cdplan) throws Exception;
	
	public Map<String, String> redireccionaReporteVidaAuto(Integer cdunieco, Integer cdramo,Integer nmpoliza, String tipoEndoso, String endoso) throws Exception;

    public List<Map<String,String>> obtieneDatosWsCotizacionServPublico(String pv_cdunieco_i, String pv_cdramo_i, String pv_estado_i,
            String pv_nmpoliza_i, String pv_nmsuplem_i, String pv_tipoend_i, String pv_numend_i) throws Exception;
	
}