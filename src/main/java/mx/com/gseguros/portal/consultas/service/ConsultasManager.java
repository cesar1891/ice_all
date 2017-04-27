package mx.com.gseguros.portal.consultas.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaImapVO;
import mx.com.gseguros.portal.cotizacion.model.ParametroGeneral;
import mx.com.gseguros.portal.general.util.ObjetoBD;

public interface ConsultasManager
{

	/**
	 * Obtiene datos ejecutando un objeto de BD
	 * @param objetoBD Objeto de BD a ejecutar
	 * @param params   Parametros que recibe el objeto de BD a ejecutar
	 * @return lista de elementos obtenidos de la consulta
	 * @throws Exception
	 */
	public List<Map<String,String>> consultaDinamica(ObjetoBD objetoBD, LinkedHashMap<String,Object>params) throws Exception;
	
	public void validarDatosCliente(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza)throws Exception;
	
	public void validarDatosObligatoriosPrevex(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza)throws Exception;
	
	public void validarDatosDXN(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem)throws Exception;
	
	@Deprecated
	public Map<String,String>cargarAtributosBaseCotizacion(String cdtipsit)throws Exception;
	
	@Deprecated
	public Map<String,String>cargarInformacionPoliza(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdusuari
			)throws Exception;
	
	@Deprecated
	public List<Map<String,String>>cargarMpolizasPorParametrosVariables(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsolici
			,String cdramant
			)throws Exception;

	public List<Map<String,String>> obtieneContratantePoliza(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String cdrol
			,String cdperson
			)throws Exception;

	
	/**
	 * Indica si un producto es o no de Salud
	 * @param cdramo Ramo del producto a validar
	 * @return true si el producto es de salud, false si no
	 * @throws Exception
	 */
	boolean esProductoSalud(String cdramo) throws Exception;
	
	public String validacionesSuplemento(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String cdtipsup
			)throws Exception;
	
	/**
	 * Copia lo documentos de una poliza a un tramite nuevo.
	 * @param cduniecoOrigen
	 * @param cdramoOrigen
	 * @param estadoOrigen
	 * @param nmpolizaOrigen
	 * @param ntramiteDestino
	 * @param rutaDocumentosDestino
	 * @return boolean exito
	 * @throws Exception
	 */
	public boolean copiarArchivosUsuarioTramite(String cduniecoOrigen, String cdramoOrigen, String estadoOrigen, 
			String nmpolizaOrigen, String ntramiteDestino, String rutaDocumentosDestino)throws Exception;
	
	@Deprecated
	public boolean validaClientePideNumeroEmpleado(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;
	
	@Deprecated
	public boolean validarVentanaDocumentosBloqueada(
			String ntramite
			,String cdtiptra
			,String cdusuari
			,String cdsisrol
			)throws Exception;

	public Map<String,String> consultaFeNacContratanteAuto(Map<String,String> params)throws Exception;
	
	@Deprecated
	public String recuperarTparagen(ParametroGeneral paragen) throws Exception;
	
	@Deprecated
	public Map<String,String> recuperarDatosFlujoEmision(String cdramo, String tipoflot) throws Exception;
	
	public String recuperarCodigoCustom(String cdpantalla, String cdsisrol) throws Exception;
	
	/**
	 * Proceso para modificar los permisos de edicion de coberturas de acuerdo al producto y plan
	 * @param cdramo
	 * @param cdtipsit
	 * @param cdplan
	 * @param cdgarant
	 * @param cdsisrol
	 * @param swmodifi
	 * @param accion
	 * @throws Exception
	 */
	public void modificaPermisosEdicionCoberturas(int cdramo, String cdtipsit, String cdplan, String cdgarant, String cdsisrol, String swmodifi, String accion) throws Exception;

	/**
	 * Proceso para obtener los permisos de edicion de coberturas de acuerdo al producto y plan
	 * @param cdramo
	 * @param cdtipsit
	 * @param cdplan
	 * @param cdgarant
	 * @param cdsisrol
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,String>> consultaPermisosEdicionCoberturas(int cdramo, String cdtipsit, String cdplan, String cdgarant, String cdsisrol) throws Exception;

	@Deprecated
	public String recuperarCdpersonClienteTramite(String ntramite) throws Exception;
	
	@Deprecated
	public Map<String,String> recuperarDatosFlujoEndoso(String cdramo, String cdtipsup) throws Exception;
	
	boolean esTramiteSalud(String ntramite) throws Exception;
	
	public void actualizaFlujoTramite(String ntramite, String cdflujomc, String cdtipflu) throws Exception;
	
	public ManagerRespuestaImapVO pantallaTrafudoc(String cdsisrol) throws Exception;
	
	public List<Map<String, String>> obtenerCursorTrafudoc(String cdfunci, String cdramo, String cdtipsit) throws Exception;
	
	@Deprecated
	public List<Map<String,String>>cargarTvalosit (String cdunieco, String cdramo, String estado, String nmpoliza,
	        String nmsuplem) throws Exception;
	
	@Deprecated
	public Map<String,String>cargarMpoliperSituac (String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem
            ,String nmsituac) throws Exception;
	
	public String recuperaAgentePoliza(String cdunieco, String cdramo, String estado, String nmpoliza, String cdusuari) throws Exception;

    public String documentosXFamilia(String pv_cdunieco_i, 
                                        String pv_cdramo_i, 
                                        String pv_estado_i, 
                                        String pv_nmpoliza_i,
                                        String pv_nmsuplem_i, 
                                        String pv_cdusuari);

    public void ejecutaFusionFam(String pv_cdunieco_i, String pv_cdramo_i, String pv_estado_i, String pv_nmpoliza_i,
            String pv_nmsuplem_i,String pv_tipoMov_i,
            String pv_cdtiptra_i, UserVO usuario);
    
    public boolean copiarArchivosRenovacionColectivo(String cduniecoOrigen, String cdramoOrigen, String estadoOrigen, 
            String nmpolizaOrigen, String ntramiteDestino, String rutaDocumentosDestino)throws Exception;

    /**
     * Recupera la descripcion de un tipo de suplemento
     * @param cdtipsup
     * @return
     * @throws Exception
     */
    public String recuperarDstipsupPorCdtipsup(String cdtipsup) throws Exception;
    
    /**
     * Recupera tramite de endoso
     * @param cdunieco
     * @param cdramo
     * @param estado
     * @param nmpoliza
     * @param nmsuplem
     * @return
     * @throws Exception
     */
    public String recuperarTramitePorNmsuplem(
            String cdunieco
            ,String cdramo
            ,String estado
            ,String nmpoliza
            ,String nmsuplem
            )throws Exception;

	public List<Map<String, String>> obtieneRangoPeriodoGracia(String pv_cdramo_i, String pv_cdtipsit_i, String pv_cdagente_i)
			throws Exception;

	public boolean esDxn(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem) throws Exception;
}