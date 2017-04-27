package mx.com.gseguros.portal.consultas.dao;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.portal.cotizacion.model.ParametroGeneral;

public interface ConsultasDAO
{
	public List<Map<String,String>> consultaDinamica(String storedProcedure,LinkedHashMap<String,Object>params) throws Exception;
	
	public List<Map<String,String>>cargarMpolizasPorParametrosVariables(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsolici
			,String cdramant
			)throws Exception;
	
	public List<Map<String,String>>cargarTconvalsit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			)throws Exception;
	
	public List<Map<String,String>>cargarTbasvalsit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			)throws Exception;
	
	public Map<String,String>cargarMpoliperSituac(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac)throws Exception;
	
	public Map<String,String>cargarMpolisitSituac(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac)throws Exception;
	
	public List<Map<String,String>>cargarTvalosit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem)throws Exception;

	public List<Map<String,String>>cargarMpoliage(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza)throws Exception;
	
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
	
	public void validarAtributosDXN(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem)throws Exception;
	
	public Map<String,String>cargarUltimoNmsuplemPoliza(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza)throws Exception;
	
	public List<Map<String,String>>cargarMpoliperOtrosRolesPorNmsituac(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String rolesPipes)throws Exception;
	
	public List<Map<String,String>>cargarTiposSituacionPorRamo(String cdramo)throws Exception;
	
	public boolean verificarCodigoPostalFronterizo(String cdpostal)throws Exception;
	
	public Map<String,String>cargarAtributosBaseCotizacion(String cdtipsit)throws Exception;
	
	public Map<String,String>cargarInformacionPoliza(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdusuari
			)throws Exception;
	
	public String recuperarPorcentajeRecargoPorProducto(String cdramo,String cdperpag)throws Exception;
	
	public List<Map<String,String>>recuperarValoresPantalla(
			String pantalla
			,String cdramo
			,String cdtipsit
			)throws Exception;
	
	public List<Map<String,String>>recuperarValoresAtributosFactores(String cdramo,String cdtipsit)throws Exception;
	
	public List<Map<String,String>>obtieneContratantePoliza(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String cdrol
			,String cdperson
			)throws Exception;
	
	public List<Map<String,String>>recuperarPolizasEndosables(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmpoliex
			,String ramo
			,String cdagente
			,String statusVig
			,String finicio //Se agrega campo fecha de inicio param No. 9
			,String ffin //Se agrega campo fecha de fin param No. 10
			,String cdsisrol
			)throws Exception;
	
	public List<Map<String,String>>recuperarHistoricoPoliza(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;
	
	public List<Map<String,String>>recuperarIncisosPolizaGrupoFamilia(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdgrupo
			,String nmfamili
			,String nivel
			,String start
			,String limit
			,String dsatribu
			,String otvalor
			)throws Exception;

	public List<Map<String,String>>recuperarDatosIncisoEnNivelPoliza(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;
	
	public String recuperarValorAtributoUnico(
			String cdtipsit
			,String cdatribu
			,String otclave
			)throws Exception;
	
	public List<Map<String,String>>recuperarGruposPoliza(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;
	
	public List<Map<String,String>>recuperarFamiliasPoliza(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;
	
	/**
	 * Indica si un producto es o no de Salud
	 * @param cdramo Ramo del producto a validar
	 * @return true si el producto es de salud, false si no
	 * @throws Exception
	 */
	public boolean esProductoSalud(String cdramo) throws Exception;
	
	public List<String> recuperarDescripcionAtributosProducto(String cdramo) throws Exception;

	public List<String> recuperarDescripcionAtributosSituacionPorRamo(String cdramo) throws Exception;
	
	public Map<String,String> recuperarFechasLimiteEndoso(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdsisrol
			,String cdusuari
			,String cdtipsup
			)throws Exception;
	
	public List<Map<String,String>> recuperarEndososRehabilitables(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;
	
	public List<Map<String,String>> recuperarEndososCancelables(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;
	
	public boolean recuperarPermisoDevolucionPrimasUsuario(String cdusuari) throws Exception;
	
	public String recuperarValorMaximoSituacionPorRol(String cdtipsit,String cdsisrol) throws Exception;

	public String obtieneSubramoGS(String cdramo,String cdtipsit) throws Exception;
	
	public String recuperarCdtipsitExtraExcel(
			int fila
			,String proc
			,String param1
			,String param2
			,String param3
			)throws Exception;
	
	public Map<String,String>recuperarCotizacionFlotillas(String cdramo,String nmpoliza,String cdusuari,String cdsisrol) throws Exception;
	
	public Map<String,List<Map<String,String>>> recuperarEstadisticasCotizacionEmision(
			Date feinicio
			,Date fefin
			,String cdunieco
			,String cdramo
			,String cdusuari
			,String cdagente
			) throws Exception;
	
	public Map<String,List<Map<String,String>>> recuperarEstadisticasTareas(
			Date feinicio
			,Date fefin
			,String cdmodulo
			,String cdtarea
			,String cdunieco
			,String cdramo
			,String cdusuari
			,String cdsisrol
			) throws Exception;
	
	public String obtieneConteoSituacionCoberturaAmparada(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String cdtipsit
			,String cdatribu
			)throws Exception;
	
	public String validacionesSuplemento(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String cdtipsup
			)throws Exception;
	
	public List<Map<String,String>> recuperarRevisionColectivos(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;

	public List<Map<String,String>> recuperarRevisionColectivosFinal(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;

	public List<Map<String,String>> recuperarRevisionColectivosEndosos(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			)throws Exception;
	
	public boolean copiaDocumentosTdocupol(
			 String cduniecoOrig
			,String cdramoOrig
			,String estadoOrig
			,String nmpolizaOrig
			,String ntramiteDestino
			)throws Exception;
	
	public String recuperarDerechosPolizaPorPaqueteRamo1(String paquete) throws Exception;
	
	public boolean validaPagoPolizaRepartido(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;
	
	public List<Map<String,String>> recuperarAtributosPorRol(String cdtipsit,String cdsisrol) throws Exception;
	
	public boolean validaClientePideNumeroEmpleado(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;
	
	public List<Map<String,String>>recuperarUsuariosReasignacionTramite(String ntramite, String cdusuari, String cdsisrol) throws Exception;
	
	public boolean validarVentanaDocumentosBloqueada(
			String ntramite
			,String cdtiptra
			,String cdusuari
			,String cdsisrol
			)throws Exception;
	
	public List<Map<String,String>> recuperarMovimientosEndosoAltaBajaAsegurados(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			)throws Exception;
	
	public String recuperarConteoTbloqueo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;

	public Map<String,String> consultaFeNacContratanteAuto(Map<String,String> params)throws Exception;
	
	public List<Map<String,String>> recuperarSubramos(String cdramo) throws Exception;
	
	public String recuperarTparagen(ParametroGeneral paragen) throws Exception;
	
	public List<Map<String,String>> recuperarTiposRamo() throws Exception;
	
	public List<Map<String,String>> recuperarRamosPorTipoRamo(String cdtipram) throws Exception;
	
	public List<Map<String,String>> recuperarSucursalesPorTipoRamo(String cdtipram) throws Exception;
	
	public List<Map<String,String>> recuperarPolizasParaImprimir(
			String cdtipram
			,String cduniecos
			,String cdramo
			,String ramo
			,String nmpoliza
			,Date fecha
			,String cdusuariLike
			,String cdagente
			,String cdusuariSesion
			,String cduniecoSesion
			)throws Exception;
	
	public String recuperarUltimoNmsuplem(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;
	
	public String recuperarSecuenciaLote() throws Exception;
	
	public String recuperarImpresionesDisponiblesPorTipoRamo(
			String cdtipram
			,String tipolote
			) throws Exception;
	
	public Map<String,String>recuperarDetalleImpresionLote(String lote,String tramite) throws Exception;
	
	public List<Map<String,String>> recuperarImpresorasPorPapelYSucursal(
			String cdusuari
			,String papel
			,String activo
			)throws Exception;
	
	public List<Map<String,String>> recuperarComboUsuarios(String cadena) throws Exception;
	
	public List<Map<String,String>> recuperarConfigImpresionSucursales(String cdusuari, String cdunieco, String cdtipram) throws Exception;
	
	public List<Map<String,String>> recuperarConfigImpresionAgentes(String cdusuari, String cdunieco, String cdtipram) throws Exception;
	
	public void movPermisoImpresionSucursal(
			String cdusuari
			,String cdunieco
			,String cdtipram
			,String cduniecoPer
			,String swaplica
			,String accion
			)throws Exception;
	
	public void movPermisoImpresionAgente(
			String cdusuari
			,String cdunieco
			,String cdtipram
			,String cduniecoPer
			,String swaplica
			,String accion
			)throws Exception;
	
	public List<Map<String,String>> recuperarRecibosLote(
			String cdtipram
			,String cduniecos
			,Date feinicio
			,Date fefin
			,String cdusuari
			,String cdunieco
			,String tiporeciboimp
			)throws Exception;
	
	public List<Map<String,String>> recuperarDetalleRemesa(String ntramite, String tipolote) throws Exception;
	
	public List<Map<String,String>> recuperarArchivosParaImprimirLote(
			String lote
			,String papel
			,String tipolote
			)throws Exception;
	
	public Map<String,String> recuperarDatosPolizaParaDocumentos(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;
	
	public String recuperarTipoRamoPorCdramo(String cdramo) throws Exception;
	
	public String recuperarTramitePorNmsuplem(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			)throws Exception;
	
	public Map<String,String> recuperarRemesaEmisionEndoso(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String ntramite
			)throws Exception;
	
	public String recuperarDstipsupPorCdtipsup(String cdtipsup) throws Exception;
	
	public List<Map<String,String>> recuperarSucursalesPermisoImpresion(
			String cdtipram
			,String cdusuari
			,String cdunieco
			)throws Exception;
	
	public List<Map<String,String>> recuperarConfigImpresionUsuarios(String cdusuari, String cdunieco, String cdtipram) throws Exception;
	
	public void movPermisoImpresionUsuario(
			String cdusuari
			,String cdunieco
			,String cdtipram
			,String cdusuariPer
			,String swaplica
			,String accion
			)throws Exception;
	
	public List<Map<String,String>> recuperarRolesTodos() throws Exception;

	public List<Map<String,String>> obtieneBeneficiariosPoliza(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
	)throws Exception;
	
	public Map<String,String> recuperarDatosFlujoEmision(String cdramo, String tipoflot) throws Exception;

	public String recuperarDiasFechaFacturacion(String cdtipsit, String cdsisrol) throws Exception;
	
	public void guardarDatosDemo(String ntramite, Map<String,String> params) throws Exception;
	
	public Map<String,String> cargarDatosDemo(String ntramite) throws Exception;
	
	public String recuperarPermisoBotonEnviarCenso(String cdsisrol) throws Exception;
	
	public int recuperarConteoTbloqueoTramite(String ntramite)throws Exception;

	public List<Map<String,String>> recuperarExclusionTurnados() throws Exception;
	
	public List<Map<String,String>> cargarCotizadoresActivos(String cadena) throws Exception;
	
	public List<Map<String,String>> obtieneMotivosReexp(String cdramo, String cdtipsit) throws Exception;
	
	public String recuperarCodigoCustom(String cdpantalla, String cdsisrol) throws Exception;
	
	public String recuperarPermisoBotonEmitir(String cdsisrol, String cdusuari, String cdtipsit) throws Exception;
	
	public List<Map<String,String>> recuperarClavesPlanRamo4(String cdramo, String cdusuari, String cdtipsit) throws Exception;
	
	public List<Map<String,String>> recuperarListaTatrisitSinPadre(String tipsit, String atribu) throws Exception;
	
	public List<Map<String,String>> recuperarFormasDePagoPorRamoTipsit(String cdramo, String cdtipsit) throws Exception;

	public List<Map<String,String>> recuperarClientesPorNombreApellido(String cadena) throws Exception;
	
	public List<Map<String, String>> recuperarConveniosPorPoliza(String cdunieco, String cdramo, String cdtipsit, String estado, String nmpoliza, String cdcontra) throws Exception;

	public List<Map<String, String>> recuperarCancelacionesConveniosPorPoliza(String cdunieco, String cdramo, String cdtipsit, String estado, String nmpoliza) throws Exception;
	
	public void insertarConvenioPoliza(String cdunieco, String cdramo, String estado, String cdtipsit, String nmpoliza, String diasgrac, String cdconven, String status, Date fecregis, String cdusureg, Date fecmodif, String cdusumod, String operacion) throws Exception;

	public void insertarCancelacionesConvenioPoliza(String cdunieco, String cdramo, String estado, String nmpoliza, String status, Date fecregis, String cdusureg, Date fecmodif, String cdusumod, String operacion) throws Exception;
	
	public List<Map<String,String>> recuperaCoberturasExtraprima(String cdramo, String cdtipsit) throws Exception;
	
	
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
	 * @return permisos de edicion
	 * @throws Exception
	 */
	public List<Map<String,String>> consultaPermisosEdicionCoberturas(int cdramo, String cdtipsit, String cdplan, String cdgarant, String cdsisrol) throws Exception;

	public String recuperarCdpersonClienteTramite(String ntramite) throws Exception;
	
	public String recuperarEsSaludDaniosTramite(String ntramite) throws Exception;
	
	public List<Map<String,String>> llenaCombo(String cdunieco,String cdramo,String estado,String nmpoliza) throws Exception;
	
	public String recuperarCduniextPorLlavePoliza(String cdunieco,String cdramo,String estado,String nmpoliza) throws Exception;
	
	public Map<String,String> recuperarDatosFlujoEndoso(String cdramo, String cdtipsup) throws Exception;
	
	public Map<String, String> recuperarTtipsupl (String cdtipsup) throws Exception;
	
	public Map<String,String> recuperarDatosFlujoRenovacion (String cdramo, String tipoflot) throws Exception;
	
	public String recuperarCorreoEmisionTramite (String ntramite) throws Exception;
	
	/**
	 * Proceso que obtiene todos los nombres de los contratantes
	 * @param cdunieco
	 * @param cdramo
	 * @return Lista de contratantes
	 * @throws Exception
	 */
	public List<Map<String,String>> obtenerContratantes(String cdunieco, String cdramo, String cadena)throws Exception;
	
	/**
	 * Proceso que obtiene todos los nombres de los contratantes
	 * @param cadena
	 * @return Lista de contratantes
	 * @throws Exception
	 */
	public List<Map<String,String>> obtenerContratantesRfc(String cadena)throws Exception;
		
	public String eliminaZwimpxlayout(String pv_idproceso_i)throws Exception;
	
	public List<Map<String,String>> cargaLayoutImpresion(String filename,
			 String tipoimp,
			 String cdusuario)throws Exception;
	
	public List<Map<String, String>> getDocumentosLayout(String pv_idproceso_i
			  ,String pv_cdtipimp_i
			  ,String pv_papel_i
			  ,String pv_cdusuari_i
			  ,String pv_cdsisrol_i)throws Exception;
	
	public void actualizaFlujoTramite(
			String ntramite,
			String cdflujomc,
			String cdtipflu)throws Exception;

	public List<Map<String, String>> formasPagoRetenedora(String administradora, String retenedora) throws Exception;

    public List<Map<String, String>> obtieneCdagente(String pv_cdusuari_i) throws Exception;

    public List<Map<String, String>> remesaDocumentosLayout(
                                                String pv_idproceso_i, 
                                                String pv_cdtipimp_i, 
                                                String pv_cdusuari_i,
                                                String pv_cdsisrol_i) throws Exception;
    
    public List<Map<String, String>> obtieneRetAdmin(String administradora, String retenedora) throws Exception;
    
    public List<Map<String, String>> obtenerCursorTrafudoc(
            String cdfunci,
            String cdramo,
            String cdtipsit) throws Exception;

    public Map<String, String> obtieneUsuarioXAgente(String pv_cdagente_i) throws Exception;
    
    public String recuperaAgentePoliza(String cdunieco, String cdramo, String estado, String nmpoliza, String cdusuari) throws Exception;

    public String verificaFusFamilia(String pv_cdunieco_i, 
                                     String pv_cdramo_i, 
                                     String pv_estado_i,
                                     String pv_nmpoliza_i, 
                                     String pv_nmsuplem_i, 
                                     String pv_cdusuari) throws Exception;

    public List<Map<String, String>> titularesFus(String pv_cdunieco_i, String pv_cdramo_i, String pv_estado_i,
            String pv_nmpoliza_i, String pv_nmsuplem_i) throws Exception;

    public void actualizaEstadoTFusLock(String pv_cdunieco_i, 
                                        String pv_cdramo_i, 
                                        String pv_estado_i, 
                                        String pv_nmpoliza_i,
                                        String pv_nmsuplem_i, 
                                        String pv_swestado_i) throws Exception;

    public List<Map<String, String>> docsXTitular(String pv_cdunieco_i
                                                , String pv_cdramo_i
                                                , String pv_estado_i
                                                , String pv_nmpoliza_i
                                                , String pv_nmsuplem_i
                                                , String pv_nmsitaux_i) throws Exception;

   public  void movTdocupolFus(String pv_cdunieco_i, String pv_cdramo_i, String pv_estado_i,
            String pv_nmpoliza_i, String pv_nmsuplem_i, String pv_nmsolici_i, String pv_ntramite_i, Date pv_feinici_i,
            String pv_cddocume_i, String pv_dsdocume_i, String pv_tipmov_i, String pv_swvisible_i, String pv_cdtiptra_i,
            String pv_codidocu_i, Date pv_fefecha_i, String pv_cdorddoc_i, String pv_cdmoddoc_i, String pv_nmcertif_i,
            String pv_nmsituac_i, String pv_cdusuari_i, String pv_cdsisrol_i) throws Exception;

   public String obtieneNmsituaext(String pv_cdunieco_i, String pv_cdramo_i, String pv_estado_i, String pv_nmpoliza_i,
        String pv_nmsuplem_i, String pv_nmsitaux_i) throws Exception;
        
   public boolean isServicioCargaFederal(String cellValue) throws Exception;
   
   public List<Map<String, String>> obtieneRangoPeriodoGracia(String pv_cdramo_i, String pv_cdtipsit_i, String pv_cdagente_i)
		throws Exception;

   public List<GenericVO> obtieneComentariosNegocio(String pv_cdramo_i, String pv_cdtipsit_i, String pv_negocio_i)
		throws Exception;        
   
   public List<Map<String,String>> recuperarEndososSiniestralidad(
           String cdunieco
           ,String cdramo
           ,String estado
           ,String nmpoliza
           )throws Exception;
   
   public List<Map<String,String>> recuperarEndososRehabilitablesSiniestralidad(
           String cdunieco
           ,String cdramo
           ,String estado
           ,String nmpoliza
           )throws Exception;
   
	public List<Map<String, String>> recuperarDatosValorDefectoLayout(
			String cdsisrol
			,String campo
			,String renovacionGral
			)throws Exception;
	
	public String esDXN(
	        String cdunieco, 
	        String cdramo, 
	        String estado, 
	        String nmpoliza, 
	        String nmsuplem) throws Exception;
}