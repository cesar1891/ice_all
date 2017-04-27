package mx.com.gseguros.portal.endosos.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.mesacontrol.model.FlujoVO;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaImapSmapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSmapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaVoidVO;
import mx.com.gseguros.portal.general.model.PolizaVO;
import mx.com.gseguros.portal.general.model.RespuestaVO;

public interface EndososManager
{
    public List<Map<String,String>>  obtenerEndosos(Map<String,String>params)                 throws Exception;
    public Map<String,String>        guardarEndosoNombres(Map<String,Object>params)           throws Exception;
    public List<Map<String,String>>  retarificarEndosos(Map<String,String>params ) throws Exception;
    @Deprecated
    public Map<String, String>       confirmarEndosoB(Map<String, String> params)             throws Exception;
    
    public Map<String,String>        guardarEndosoDomicilio(Map<String,Object>params)         throws Exception;    
    public List<Map<String, String>> obtieneCoberturasDisponibles(Map<String, String> params) throws Exception;
    
    
    @Deprecated
    public Map<String,String>        iniciaEndoso(Map<String,Object>params)        throws Exception;
    
	public List<Map<String, String>> obtenerAtributosCoberturas(Map<String, String> params)   throws Exception;
	/**
	 * PKG_COTIZA.P_EJECUTA_SIGSVALIPOL_END
	 */
	@Deprecated
	public Map<String,Object> sigsvalipolEnd(
			String cdusuari
			,String cdelemen
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String cdtipsup
			)throws Exception;
	public Map<String,String>        guardarEndosoClausulas(Map<String,Object>params)         throws Exception;
	/**
	 * PKG_ENDOSOS.P_CALC_VALOR_ENDOSO
	 */
	public Map<String,String>        calcularValorEndoso(Map<String,Object>params)            throws Exception;
	/**
	 * PKG_ENDOSOS.P_CALC_VALOR_ENDOSO
	 */
	public Map<String,String>        calcularValorEndoso(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,Date   feinival
			,String cdtipsup) throws Exception;
	/**
	 * PKG_ENDOSOS.P_ENDOSO_INICIA
	 */
	@Deprecated
	public Map<String,String>        iniciarEndoso(Map<String,String>params)                  throws Exception;
	
	/**
	 * PKG_ENDOSOS.P_ENDOSO_INICIA
	 */
	@Deprecated
	public Map<String,String>        iniciarEndoso(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String fecha
			,String cdelemento
			,String cdusuari
			,String proceso
			,String cdtipsup) throws Exception;
	
	/**
	 * PKG_SATELITES.P_INSERTA_TWORKSUP_END
	 * @param accion TODO
	 */
	public void                      movimientoTworksupEnd(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdtipsup
			,String nmsuplem
			,String nmsituac, String accion) throws Exception;
	public void                      insertarTworksupSitTodas(Map<String,String>params)       throws Exception;
	public Map<String, String>       obtieneDatosMpolisit(Map<String, String> params)         throws Exception;
	public Map<String, String>       obtieneDatosMpolisit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza)         throws Exception;
	public List<Map<String, String>> obtenerNombreEndosos(String cdsisrol, Integer cdramo, String cdtipsit) throws Exception;
	public String                    obtieneDescripcionEndoso(String cdtipsup) throws Exception;
	public void                      actualizaNombreCliente(Map<String, String> params)           throws Exception;
	public void                      actualizaRfcCliente(Map<String, String> params)           throws Exception;
	public void                      actualizarFenacimi(Map<String, String> params)           throws Exception;
	public void                      actualizarSexo(Map<String, String> params)               throws Exception;
	public List<Map<String, String>> obtenerCdpersonMpoliper(Map<String, String> params)      throws Exception;
	
	/**
	 * Obtiene el N&uacute;mero de Tr&aacute;mite de Emisi&oacute;n de una p&oacute;liza
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @return
	 * @throws Exception
	 */
	public String obtenerNtramiteEmision(String cdunieco,String cdramo,String estado,String nmpoliza) throws Exception;
	
	/**
	 * Valida si existe un endoso anterior pendiente
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param cdtipsup
	 * @return
	 */
	public RespuestaVO validaEndosoAnterior(String cdunieco, String cdramo, String estado, String nmpoliza, String cdtipsup);
	
	public void actualizaVigenciaPoliza(String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String feefecto
			,String feproren) throws Exception;
	
	public void                      actualizaDeducibleValosit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String deducible) throws Exception;
	public void                      actualizaCopagoValosit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String deducible) throws Exception;
	
	public Map<String,String>        pClonarPolizaReexped(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String fecha
			,String cdplan
			,String cdusuario
			,String newcdunieco) throws Exception;

	public Map<String,String>        pClonarCotizacionTotal(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String fecha
			,String cdusuario
			,String newcdunieco
			,String tipoClonacion) throws Exception;
	
	public boolean clonaGruposReexp(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cduniecoNueva
			,String nmpolizaNueva
			,List<Map<String,String>> grupos) throws Exception;

	public boolean actualizaGruposReexp(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,List<Map<String,String>> grupos) throws Exception;
	
	public boolean actualizaTodosGruposReexp(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza) throws Exception;
	
	public boolean valoresDefectoGruposReexp(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdtipsup
			,List<Map<String,String>> grupos) throws Exception;
	
	public boolean valoresDefectoGruposCotizacion(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdtipsup) throws Exception;
	
	@Deprecated
	public List<Map<String, String>> obtenerValositUltimaImagen(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem) throws Exception;
	/**
	 * PKG_ENDOSOS.P_INS_NEW_EXTRAPRIMA_TVALOSIT
	 */
	public void                      actualizaExtraprimaValosit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String extraprima) throws Exception;
	/**
	 * PKG_ENDOSOS.P_INS_MPOLIZAS_CDPERPAG
	 */
	public void                      insertarPolizaCdperpag(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdperpag) throws Exception;
	/**
	 * PKG_ENDOSOS.P_GET_FEINIVAL_END_FP
	 */
	public Date                      obtenerFechaEndosoFormaPago(
			String cdunieco,String cdramo,String estado,String nmpoliza) throws Exception;
	/**
	 * P_CALC_RECIBOS_SUB_ENDOSO_FP
	 */
	public void calcularRecibosEndosoFormaPago(String cdunieco,String cdramo,
			String estado,String nmpoliza,String nmsuplem) throws Exception;

	/**
	 * Cancela recibos endoso cliente
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @throws Exception
	 */
	public void cancelaRecibosCambioCliente(String cdunieco,String cdramo,
			String estado,String nmpoliza,String nmsuplem) throws Exception;
	
	/**
	 * P_CALCULA_COMISION_BASE
	 */
	public void calcularComisionBase(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem) throws Exception;
	/**
	 * PKG_CONSULTA.P_GET_AGENTE_POLIZA
	 * @return a.cdunieco,
			a.cdramo,
			a.estado,
			a.nmpoliza,
			a.cdagente,
			a.nmsuplem,
			a.status,
			a.cdtipoag,
			porredau,
			a.porparti,
			nombre,
			cdsucurs,
			nmcuadro
	 */
	public List<Map<String,String>> obtenerAgentesEndosoAgente(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem) throws Exception;

	public List<Map<String,String>> obtenerAseguradosPoliza(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem) throws Exception;
	/**
	 * PKG_SATELITES.P_MOV_MPOLIAGE
	 */
	public void pMovMpoliage(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdagente
			,String nmsuplem
			,String status
			,String cdtipoag
			,String porredau
			,String nmcuadro
			,String cdsucurs
			,String accion
			,String ntramite
			,String porparti
			) throws Exception;
	/**
	 * PKG_SATELITES.P_GET_NMSUPLEM_EMISION
	 */
	public String pGetSuplemEmision(
			 String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza) throws Exception;
	
	public String obtieneFechaInicioVigenciaPoliza
	(
		String cdunieco,
		String cdramo,
		String estado,
		String nmpoliza
		) throws Exception;
	
	public boolean validaEndosoSimple
	(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza
			) throws Exception;
	
	public void validaNuevaCobertura(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String cdgarant
			) throws Exception;
	
	public void calcularRecibosCambioAgente(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem) throws Exception;
	
	public void calcularRecibosCambioContratante(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem) throws Exception;
	
	public List<Map<String,String>> habilitaRecibosSubsecuentes(
			Date fechaDeInicio
			,Date fechaDeFin
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza) throws Exception;
	
	public void validaEstadoCodigoPostal(Map<String,String>params) throws Exception;
	public void actualizaTvalositCoberturasAdicionales(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdtipsup) throws Exception;
	
	public ManagerRespuestaImapSmapVO obtenerComponentesSituacionCobertura(String cdramo,String cdtipsit,String cdtipsup,String cdgarant);
	public void actualizaTvalositSituacionCobertura(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String cdatribu
			,String otvalor);
	public ManagerRespuestaImapSmapVO endosoAtributosSituacionGeneral(
			String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String nmpoliza
			,String cdusuari
			,String cdtipsup);
	public ManagerRespuestaSmapVO cargarTvalositTitular(String cdunieco,String cdramo,String estado,String nmpoliza,String nmsuplem);
	
	public ManagerRespuestaVoidVO guardarEndosoAtributosSituacionGeneral(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdtipsit
			,String cdtipsup
			,String ntramite
			,String feefecto
			,Map<String,String>tvalosit
			,UserVO usuario
			,String rutaDocsPoliza
			,String rutaServReports
			,String passServReports
			,FlujoVO flujo
			)throws Exception;
	
	public ManagerRespuestaVoidVO guardarEndosoBeneficiarios(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,List<Map<String,String>>mpoliperMpersona
			,String cdelemen
			,String cdusuari
			,String cdtipsup
			,String ntramite
			,String cdsisrol
			,UserVO usuarioSesion
			,FlujoVO flujo
			,String feefecto
			)throws Exception;

	
	public void guardaAseguradoAlterno(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String aseguradoAlterno
			)throws Exception;
	
	/**
	 * 
	 * @param poliza
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,String>> obtenerListaDocumentosEndosos(PolizaVO poliza,String cdmoddoc) throws Exception;
	
	public int recuperarDiasDiferenciaEndosoValidos(String cdramo,String cdtipsup)throws Exception;
	
	public boolean revierteEndosoFallido(String cdunieco,String cdramo,String estado,String nmpoliza,String nsuplogi ,String nmsuplem, Integer codigoError
			,String mensajeError, boolean esEndosoB);
	
	public boolean revierteDomicilio(Map<String, String> params);

	public boolean revierteNombrePersona(Map<String, String> params);

	/**
	 * Para reasignar el parentesco cuando se da de baja el titular en el endoso de baja de asegurado
	 * @param params
	 * @return
	 */
	public void reasignaParentescoTitular(Map<String, String> params)throws Exception;
	
	
	/**
	 * 
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsituac
	 * @param cdclausu
	 * @param nmsuplem
	 * @param status
	 * @param cdtipcla
	 * @param swmodi
	 * @param dslinea
	 * @param accion
	 * @throws Exception
	 */
	public void guardarMpolicot(String cdunieco, String cdramo, String estado,
			String nmpoliza, String nmsituac, String cdclausu, String nmsuplem,
			String status, String cdtipcla, String swmodi, String dslinea,
			String accion) throws Exception;
	
	public boolean esMismaPersonaContratante(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			);
	
	public String recuperarCdtipsitInciso1(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;

	/**
	 * Obtiene si una poliza es tipo flotilla, pyme o ninguno
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @return
	 * @throws Exception
	 */
	public String obtieneTipoFlot(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;
	
	public Map<String,Object> pantallaEndosoAltaBajaFamilia(
			String cdusuari
			,String cdsisrol
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String tipoflot
			,String tipo
			,String cdtipsup
			,String contexto
			)throws Exception;
	
	public Map<String,String> recuperarComponentesAltaAsegurado(
			String cdramo
			,String cdtipsit
			,String depFam
			,String cdsisrol
			,String contexto
			)throws Exception;
	
	public void validaDuplicidadParentesco(
			String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem)
			throws Exception;
	
	public void sacaEndoso(String cdunieco, String cdramo, String estado,
			String nmpoliza, String nsuplogi, String nmsuplem) throws Exception;
	
	public String obtieneNumeroAtributo(String cdtipsit, String nombreAtributo) throws Exception;
	
	public String confirmarEndosoAltaFamilia(
			String cdusuari
			,String cdsisrol
			,String cdelemen
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdtipsup
			,String nmsuplem
			,String nsuplogi
			,Date fecha
			,String rutaDocumentosPoliza
			,String rutaServidorReports
			,String passServidorReports
			,UserVO usuario
			,List<String> incisos
			,String cdtipsitPrimerInciso
			,String nmsolici
			,FlujoVO flujo
			)throws Exception;
	
	public String confirmarEndosoBajaFamilia(
			String cdusuari
			,String cdsisrol
			,String cdelemen
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdtipsup
			,String nmsuplem
			,String nsuplogi
			,Date fecha
			,String rutaDocumentosPoliza
			,String rutaServidorReports
			,String passServidorReports
			,UserVO usuario
			,List<String> incisos
			,String cdtipsitPrimerInciso
			,String nmsolici
			,FlujoVO flujo
			)throws Exception;
	
	@Deprecated
	public String recuperarUltimoNmsuplem(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;
	
	public List<Map<String,String>> obtenerInfoFamiliaEndoso(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String ntramite) throws Exception;
	
	public void  clonarGarantiaCapitales(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String cdgrupo
			,String cdplan
			,String sexo) throws Exception;
	
	public void actualizaExtraprimaValosit2(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String extraprima
			,String extraprimaOcu) throws Exception;
	
	public Map<String,Item> cargaInfoPantallaClonacion() throws Exception;
	
	public List<Map<String, String>> buscarCotizaciones(String cdunieco, String cdramo, String cdtipsit, String estado, String nmpoliza, String ntramite, String status, String fecini, String fecfin, String cdsisrol, String cdusuari) throws Exception;
	
	public Map<String,Object> procesarCensoClonacion(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String cduniecoOrig,
			String cdramoOrig,
			String estadoOrig,
			String nmpolizaOrig,
			File   censo,
			String rutaDocumentosTemporal,
			String dominioServerLayouts,
			String userServerLayouts,
			String passServerLayouts,
			String rootServerLayouts,
			String cdtipsit,
			String cdusuari,
			String cdsisrol)throws Exception;
	
	public void confirmarClonacionCondiciones(
		String cdunieco,
		String cdramo,
		String estado,
		String nmpoliza,
		String cdtipsit,
		int    numSituac) throws Exception;
	
	@Deprecated
	public void confirmarEndosoB(
    		String cdunieco
    		,String cdramo
    		,String estado
    		,String nmpoliza
    		,String nmsuplem
    		,String nsuplogi
    		,String cdtipsup
    		,String dscoment
    		)throws Exception;
	
	public void BeneficiarioVida_M(
	        String cdunieco
            ,String cdramo
            ,String estado
            ,String nmpoliza
            ,String nmsuplem
            )throws Exception;
}