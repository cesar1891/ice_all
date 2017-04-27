package mx.com.gseguros.portal.cotizacion.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.consultas.model.AseguradosFiltroVO;
import mx.com.gseguros.portal.cotizacion.model.ConfiguracionCoberturaDTO;
import mx.com.gseguros.portal.cotizacion.model.DatosUsuario;
import mx.com.gseguros.portal.cotizacion.model.PInsertaTbasvalsitDTO;
import mx.com.gseguros.portal.cotizacion.model.PInsertaTconvalsitDTO;
import mx.com.gseguros.portal.cotizacion.model.PMovMpolisitDTO;
import mx.com.gseguros.portal.cotizacion.model.PMovTvalositDTO;
import mx.com.gseguros.portal.cotizacion.model.ParametroCotizacion;
import mx.com.gseguros.portal.general.model.ComponenteVO;

public interface CotizacionDAO {
	public void movimientoTvalogarGrupoCompleto(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, String cdtipsit,
			String cdgrupo, String cdgarant, String status,
			Map<String, String> valores) throws Exception;

	public void movimientoTvalogarGrupo(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, String cdtipsit,
			String cdgrupo, String cdgarant, String status, String cdatribu,
			String valor) throws Exception;

	public void movimientoMpolisitTvalositGrupo(Map<String, String> params)
			throws Exception;

	public void movimientoMpoligarGrupo(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, String cdtipsit,
			String cdgrupo, String cdgarant, String status, String cdmoneda,
			String accion, String respvalogar) throws Exception;

	public Map<String, String> cargarDatosCotizacionGrupo(
			Map<String, String> params) throws Exception;

	public Map<String, String> cargarDatosCotizacionGrupo2(String cdunieco,
			String cdramo, String cdtipsit, String estado, String nmpoliza,
			String ntramite) throws Exception;

	public List<Map<String, String>> cargarGruposCotizacion(
			Map<String, String> params) throws Exception;

	public List<Map<String, String>> cargarGruposCotizacion2(String cdunieco,
			String cdramo, String estado, String nmpoliza) throws Exception;

	public Map<String, String> cargarDatosGrupoLinea(Map<String, String> params)
			throws Exception;

	public List<Map<String, String>> cargarTvalogarsGrupo(
			Map<String, String> params) throws Exception;

	public List<Map<String, String>> cargarTarifasPorEdad(
			Map<String, String> params) throws Exception;

	public List<Map<String, String>> cargarTarifasPorCobertura(
			Map<String, String> params) throws Exception;

	public String cargarNombreAgenteTramite(String ntramite) throws Exception;

	public Map<String, String> cargarPermisosPantallaGrupo(String cdsisrol,
			String status) throws Exception;

	public Map<String, String> obtieneTipoValorAutomovil(
			Map<String, String> params) throws Exception;
	
		public String obtieneCodigoPostalAutomovil(
				Map<String, String> params) throws Exception;

	public void guardarCensoCompletoMultisalud(String nombreArchivo,
			String cdunieco, String cdramo, String estado, String nmpoliza,
			String cdestadoCli, String cdmuniciCli, String cdplan1,
			String cdplan2, String cdplan3, String cdplan4, String cdplan5,
			String complemento) throws Exception;

	public List<Map<String, String>> cargarAseguradosExtraprimas(
			Map<String, String> params) throws Exception;

	public void guardarExtraprimaAsegurado(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String ocupacion
			,String extraprimaOcupacion
			,String peso
			,String estatura
			,String extraprimaSobrepeso
			)throws Exception;

	public void guardarExtraprimaAsegurado(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			List<Map<String,String>> valores)throws Exception;
	
	public List<Map<String, String>> cargarAseguradosGrupo(
			Map<String, String> params) throws Exception;

	public List<Map<String,String>>cargarAseguradosGrupo(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			String cdgrupo,
			String start,
			String limit) throws Exception;
	
	public void borrarMpoliperGrupo(Map<String, String> params)
			throws Exception;

	@Deprecated
	public Map<String, String> cargarTipoSituacion(Map<String, String> params)
			throws Exception;

	public Map<String, String> cargarTipoSituacion(String cdramo,
			String cdtipsit) throws Exception;

	public String cargarCduniecoAgenteAuto(String cdagente, String cdtipram)
			throws Exception;

	public Map<String, String> obtenerDatosAgente(String cdagente, String cdramo)
			throws Exception;

	public Map<String, String> obtenerParametrosCotizacion(
			ParametroCotizacion parametro, String cdramo, String cdtipsit,
			String clave4, String clave5) throws Exception;

	public Map<String, String> cargarAutoPorClaveGS(String cdramo,
			String clavegs, String cdtipsit, String cdsisrol, String tipoUnidad) throws Exception;

	public Map<String, String> cargarClaveGSPorAuto(Map<String, String> params)
			throws Exception;

	public Map<String, String> cargarSumaAseguradaAuto(
			Map<String, String> params) throws Exception;

	public void movimientoMpolicotICD(Map<String, String> params)
			throws Exception;

	public List<Map<String, String>> cargarMpolicotICD(
			Map<String, String> params) throws Exception;

	public List<Map<String, String>> cargarConfiguracionGrupo(
			Map<String, String> params) throws Exception;

	public ComponenteVO cargarComponenteTatrisit(Map<String, String> params)
			throws Exception;

	public ComponenteVO cargarComponenteTatrigar(Map<String, String> params)
			throws Exception;

	public void validarDescuentoAgente(String tipoUnidad, String uso,
			String zona, String promotoria, String cdagente, String descuento)
			throws Exception;

	/*
	public List<Map<String, String>> impresionDocumentosPoliza(String cdunieco,
			String cdramo, String estado, String nmpoliza, String nmsuplem,
			String ntramite) throws Exception;
	*/

	public void movimientoTdescsup(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nsuplogi, String cdtipsup,
			Date feemisio, String nmsolici, Date fesolici, Date ferefere,
			String cdseqpol, String cdusuari, String nusuasus, String nlogisus,
			String cdperson, String accion) throws Exception;

	public DatosUsuario cargarInformacionUsuario(String cdusuari,
			String cdtipsit) throws Exception;

	public List<ComponenteVO> cargarTatrisit(String cdtipsit, String cdusuari)
			throws Exception;

	public List<ComponenteVO> cargarTatripol(String cdramo, String cdtipsit,
			String cdtippol) throws Exception;

	public Map<String, String> cargarClienteCotizacion(String cdunieco,
			String cdramo, String estado, String nmpoliza) throws Exception;

	public Map<String, String> cargarConceptosGlobalesGrupo(String cdunieco,
			String cdramo, String estado, String nmpoliza, String nmsuplem,
			String cdperpag) throws Exception;

	public String calculaNumeroPoliza(String cdunieco, String cdramo,
			String estado) throws Exception;

	public void movimientoPoliza(String cdunieco, String cdramo, String estado,
			String nmpoliza, String nmsuplem, String status, String swestado,
			String nmsolici, String feautori, String cdmotanu, String feanulac,
			String swautori, String cdmoneda, String feinisus, String fefinsus,
			String ottempot, String feini, String hhefecto, String fefin,
			String fevencim, String nmrenova, String ferecibo, String feultsin,
			String nmnumsin, String cdtipcoa, String swtarifi, String swabrido,
			String feemisio, String cdperpag, String nmpoliex, String nmcuadro,
			String porredau, String swconsol, String nmpolant, String nmpolnva,
			String fesolici, String cdramant, String cdmejred, String nmpoldoc,
			String nmpoliza2, String nmrenove, String nmsuplee,
			String ttipcamc, String ttipcamv, String swpatent, String pcpgocte,
			String tipoflot, String agrupador, String accion) throws Exception;

	public void movimientoTvalopol(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, String status,
			Map<String, String> valores) throws Exception;

	public void procesarCenso(String nombreProcedure, String cdusuari,
			String cdsisrol, String nombreCenso, String cdunieco,
			String cdramo, String estado, String nmpoliza, String cdtipsit,
			String cdagente, String codpostal, String cdedo, String cdmunici,
			String complemento
			) throws Exception;

	public void actualizaMpolisitTvalositGrupo(String cdunieco, String cdramo,
			String estado, String nmpoliza, String cdgrupo, String nombreGrupo,
			String cdplan, Map<String, String> valores) throws Exception;

	public void valoresPorDefecto(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsituac, String nmsuplem,
			String cdgarant, String cdtipsup) throws Exception;

	public void movimientoMpoliper(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsituac, String cdrol,
			String cdperson, String nmsuplem, String status, String nmorddom,
			String swreclam, String accion, String swexiper) throws Exception;

	public void movimientoMpoliage(String cdunieco, String cdramo,
			String estado, String nmpoliza, String cdagente, String nmsuplem,
			String status, String cdtipoag, String porredau, String nmcuadro,
			String cdsucurs, String accion, String ntramite, String porparti)
			throws Exception;

	public void tarificaEmi(String cdusuari, String cdelemen, String cdunieco,
			String cdramo, String estado, String nmpoliza, String nmsituac,
			String nmsuplem, String cdtipsit) throws Exception;

	public List<Map<String, String>> obtenerTiposSituacion() throws Exception;

	public List<Map<String, String>> cargarSituacionesGrupo(String cdunieco,
			String cdramo, String estado, String nmpoliza, String nmsuplem,
			String cdgrupo) throws Exception;

	public void actualizaValoresSituacion(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, String nmsituac,
			Map<String, String> valores) throws Exception;

	public void validarCambioZonaGMI(String cdunieco, String cdramo,
			String cdtipsit, String estado, String nmpoliza, String nmsuplem,
			String nmsituac, String codpostal) throws Exception;

	public void validarEnfermedadCatastGMI(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, String nmsituac,
			String circHosp) throws Exception;

	public Map<String, String> cargarRetroactividadSuplemento(String cdunieco,
			String cdramo, String cdtipsup, String cdusuari, String cdtipsit)
			throws ApplicationException, Exception;

	public Map<String, String> cargarSumaAseguradaRamo5(String cdtipsit,
			String clave, String modelo, String cdsisrol) throws Exception;

	public Map<String, String> cargarDatosComplementariosAutoInd(
			String cdunieco, String cdramo, String estado, String nmpoliza)
			throws ApplicationException, Exception;

	public Map<String, String> cargarTvalopol(String cdunieco, String cdramo,
			String estado, String nmpoliza) throws ApplicationException,
			Exception;

	public Map<String, String> cargarTvalosit(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsituac)
			throws ApplicationException, Exception;

	public void actualizaMpolizas(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, String swestado,
			String nmsolici, Date feautori, String cdmotanu, Date feanulac,
			String swautori, String cdmoneda, Date feinisus, Date fefinsus,
			String ottempot, Date feefecto, String hhefecto, Date feproren,
			Date fevencim, String nmrenova, Date ferecibo, Date feultsin,
			String nmnumsin, String cdtipcoa, String swtarifi, String swabrido,
			Date feemisio, String cdperpag, String nmpoliex, String nmcuadro,
			String porredau, String swconsol, String nmpolant, String nmpolnva,
			Date fesolici, String cdramant, String cdmejred, String nmpoldoc,
			String nmpoliza2, String nmrenove, String nmsuplee,
			String ttipcamc, String ttipcamv, String swpatent, String nmpolmst,
			String pcpgocte) throws Exception;

	public void borrarAgentesSecundarios(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem) throws Exception;

	public Map<String, String> cargarConfiguracionCotizacion(String cdramo,
			String cdtipsit, String cdusuari) throws ApplicationException,
			Exception;

	public void guardarConfiguracionCotizacion(String cdramo, String cdtipsit,
			String cdusuari, Map<String, String> valores) throws Exception;

	public Map<String, String> cargarRangoDescuentoRamo5(String cdtipsit,
			String cdagente, String negocio, String cdsisrol, String cdusuari)
			throws ApplicationException, Exception;

	public List<List<Map<String, String>>> cargarParamerizacionConfiguracionCoberturas(
			String cdtipsit, String cdsisrol, String negocio,
			String tipoServicio, String modelo, String tipoPersona,
			String submarca, String clavegs) throws Exception;

	public Map<String, String> cargarDatosVehiculoRamo5(String cdunieco,
			String cdramo, String estado, String nmpoliza)
			throws ApplicationException, Exception;

	public void borrarMpoliperTodos(String cdunieco, String cdramo,
			String estado, String nmpoliza) throws Exception;

	public void guardarTbasvalsit(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsituac, String nmsuplem,
			String status, String cdtipsit, Map<String, String> valores)
			throws Exception;

	public void guardarTconvalsit(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsituac, String nmsuplem,
			String status, String cdtipsit, Map<String, String> valores)
			throws Exception;

	public void borrarTbasvalsit(String cdunieco, String cdramo, String estado,
			String nmpoliza) throws Exception;

	public void borrarTconvalsit(String cdunieco, String cdramo, String estado,
			String nmpoliza) throws Exception;

	public void movimientoMpolisit(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsituac, String nmsuplem,
			String status, String cdtipsit, String swreduci, String cdagrupa,
			String cdestado, Date fefecsit, Date fecharef, String cdgrupo,
			String nmsituaext, String nmsitaux, String nmsbsitext,
			String cdplan, String cdasegur, String accion) throws Exception;

	public void movimientoTvalosit(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsituac, String nmsuplem,
			String status, String cdtipsit, Map<String, String> valores,
			String accion) throws Exception;

	public void clonarPersonas(String cdelemen, String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsituac, String cdtipsit,
			Date fecha, String cdusuari, String nombre1, String nombre2,
			String apellido1, String apellido2, String sexo, Date fenacimi,
			String parentesco) throws Exception;

	public List<Map<String, String>> cargarResultadosCotizacion(
			String cdusuari, String cdunieco, String cdramo, String estado,
			String nmpoliza, String cdelemen, String cdtipsit) throws Exception;

	public List<Map<String, String>> cargarParametrizacionExcel(String proceso,
			String cdramo, String cdtipsit) throws Exception;

	public String cargarClaveTtapvat1(String cdtabla, String otvalor,
			Map<String, List<Map<String, String>>> buffer) throws Exception;

	public List<Map<String, String>> cargarResultadosCotizacionAutoFlotilla(
			String cdunieco, String cdramo, String estado, String nmpoliza)
			throws Exception;

	public List<Map<String, String>> cargarDetallesCotizacionAutoFlotilla(
			String cdunieco, String cdramo, String estado, String nmpoliza,
			String cdperpag) throws Exception;

	public List<Map<String, String>> cargarDetallesCoberturasCotizacionAutoFlotilla(
			String cdunieco, String cdramo, String estado, String nmpoliza,
			String cdperpag) throws Exception;

	public String cargarTabuladoresGMIParche(String circulo, String cdatribu)
			throws Exception;

	public void sigsvdefEnd(String cdunieco, String cdramo, String estado,
			String nmpoliza, String nmsituac, String nmsuplem, String cdgarant,
			String cdtipsup) throws Exception;

	public void borrarMpoliperSituac0(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, String cdrol)
			throws Exception;

	public String cargarTipoVehiculoRamo5(String clave) throws Exception;

	public Map<String, String> cargarDetalleNegocioRamo5(String negocio,
			String cdramo, String cdtipsit, String cdsisrol, String cdusuari)
			throws Exception;

	public Map<String, List<Map<String, String>>> cargarConfiguracionTvalositFlotillas(
			String cdramo, String cdtipsit, String negocio) throws Exception;

	public boolean cargarBanderaCambioCuadroPorProducto(String cdramo)
			throws Exception;

	public Map<String, String> cargarRangoDescuentoRamo5TodasSituaciones(
			String cdagente, String negocio, String cdsisrol, String cdusuari)
			throws Exception;

	public void validarCodpostalTarifa(String codpostal, String cdtipsit)
			throws Exception;

	public boolean validaDomicilioCotizacionTitular(Map<String, String> params)
			throws Exception;

	public boolean validarCuadroComisionNatural(String cdunieco, String cdramo,
			String estado, String nmpoliza) throws Exception;

	/**
	 * Aplica los parches que se piden en cotizacion
	 */
	public void aplicarAjustesCotizacionPorProducto(String cdunieco,
			String cdramo, String estado, String nmpoliza, String cdtipsit,
			String tipocot) throws Exception;

	public List<List<Map<String, String>>> cargarParamerizacionConfiguracionCoberturasRol(
			String cdtipsit, String cdsisrol) throws Exception;

	public String cargarPorcentajeCesionComisionAutos(String cdunieco,
			String cdramo, String estado, String nmpoliza) throws Exception;

	public void ejecutaValoresDefectoTarificacionConcurrente(String cdunieco,
			String cdramo, String estado, String nmpoliza, String nmsuplem,
			String nmsituac, String tipotari, String cdperpag) throws Exception;

	public void movimientoMpolisitLote(List<PMovMpolisitDTO> lista)
			throws Exception;

	public void movimientoTvalositLote(List<PMovTvalositDTO> lista)
			throws Exception;

	public void movimientoTbasvalsitLote(List<PInsertaTbasvalsitDTO> lista)
			throws Exception;

	public void movimientoTconvalsitLote(List<PInsertaTconvalsitDTO> lista)
			throws Exception;

	public void movimientoMpoliagr(String cdunieco, String cdramo,
			String estado, String nmpoliza, String cdagrupa, String nmsuplem,
			String status, String cdperson, String nmorddom, String cdforpag,
			String cdbanco, String cdsucurs, String cdcuenta, String cdrazon,
			String swregula, String cdperreg, Date feultreg, String cdgestor,
			String cdrol, String cdbanco2, String cdsucurs2, String cdcuenta2,
			String cdtipcta, String cdtipcta2, String cdpagcom,
			String nmpresta, String nmpresta2, String cdbanco3,
			String cdsucurs3, String cdcuenta3, String cdtipcta3,
			String nmpresta3, String nmcuenta, String accion) throws Exception;

	public Map<String, String> validarReemplazoDocumentoCotizacion(
			String cdunieco, String cdramo, String estado, String nmpoliza)
			throws Exception;

	public void grabarEvento(StringBuilder sb, String cdmodulo,
			String cdevento, Date fecha, String cdusuari, String cdsisrol,
			String ntramite, String cdunieco, String cdramo, String estado,
			String nmpoliza, String nmsolici, String cdagente,
			String cdusuariDes, String cdsisrolDes, String status)
			throws Exception;

	public void ejecutaValoresDefectoConcurrente(String cdunieco,
			String cdramo, String estado, String nmpoliza, String nmsuplem,
			String nmsituac, String tipotari, String cdperpag) throws Exception;

	public void ejecutaTarificacionConcurrente(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, String nmsituac,
			String tipotari, String cdperpag) throws Exception;

	public void actualizaValoresDefectoSituacion(String cdunieco,
			String cdramo, String estado, String nmpoliza, String nmsuplem)
			throws Exception;

	public void procesaLayoutCensoMultisalud(String nombreArchivo,
			String cdunieco, String cdramo, String estado, String nmpoliza,
			String cdestadoCli, String cdmuniciCli, String cdplan1,
			String cdplan2, String cdplan3, String cdplan4, String cdplan5,
			String complemento) throws Exception;
	
	public String guardarConfiguracionGarantias(
			String cdramo
			,String cdtipsit
			,String cdplan
			,String cdpaquete
			,boolean nuevo
			,String dspaquete
			,String derpol
			,List<ConfiguracionCoberturaDTO>lista
			)throws Exception;
	
	public List<Map<String,String>> obtenerCoberturasPlanColec(
			String cdramo
			,String cdtipsit
			,String cdplan
			,String cdsisrol
			)throws Exception;

	public List<Map<String,String>> obtieneCobeturasNombrePlan(
			String cdramo
			,String cdtipsit
			,String cdplan
			)throws Exception;
	
	/**
     * Metodo para insertar semaforos o bloqueos de procesos customizados, solo son requeridos
     * el numero de tramite y proceso, los demas valores son opcionales.
     * @param ntramite
     * @param tipoProceso
     * @param rol
     * @param descripcion
     * @param valor
     * @param operacion
     * @return
     * @throws Exception
     */
    public boolean ejectutaBloqueoProcesoTramite(
             String ntramite
            ,String claveProceso
            ,String cdrol
            ,String descripcion
            ,String valor
            ,String operacion
            )throws Exception;
    
    /**
     * consulta bloqueo o semaforo de proceso de tramite
     * @param ntramite
     * @param claveProceso
     * @return
     * @throws Exception
     */
    public Map<String,String> consultaBloqueoProcesoTramite(
            String ntramite
            ,String claveProceso
            )throws Exception;

	public List<Map<String,String>> obtieneDatosContratantePoliza(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			)throws Exception;
	
	public String insercionDocumentosParametrizados(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String proceso
			)throws Exception;
	
	public void actualizaCesionComision(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;

	public void actualizaDomicilioAseguradosColectivo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdpostal
			,String cdedo
			,String cdmunici
			)throws Exception;
	
	public void movimientoMsupleme(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			Date feinival,
			String hhinival,
			Date fefinval,
			String hhfinval,
			String swanula,
			String nsuplogi,
			String nsupusua,
			String nsupsess,
			String fesessio,
			String swconfir,
			String nmrenova,
			String nsuplori,
			String cdorddoc,
			String swpolfro,
			String pocofron,
			String swpoldec,
			String tippodec,
			String accion) throws Exception;
	
	
	public void procesaIncisoDefinitivo(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsituac,
			String cdelement,
			String cdperson,
			String cdasegur,
			String cdplan,
			String cdperpag) throws Exception;
	
	public List<Map<String,String>> recuperarListaDocumentosParametrizados(
			String cdorddoc
			,String nmsolici
			,String ntramite
			)throws Exception;
	
	public List<Map<String,String>> generarDocumentosBaseDatos(
					String cdorddoc
					,String nmsolici
					,String ntramite
					)throws Exception;
	
	public Map<String, String>  obtieneValidacionRetroactividad(
			String numSerie
			,String feini)throws Exception;

	public void getActualizaCuadroComision(Map<String, Object> paramsPoliage) throws Exception;
	
	public void eliminarMpolirec(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			)throws Exception;
	
	public void actualizarFefecsitMpolisit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			)throws Exception;
	
	public void guardarPantallaActTvalosit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String otvalor01,String otvalor02,String otvalor03,String otvalor04,String otvalor05,String otvalor06,String otvalor07,String otvalor08,String otvalor09,String otvalor10
			,String otvalor11,String otvalor12,String otvalor13,String otvalor14,String otvalor15,String otvalor16,String otvalor17,String otvalor18,String otvalor19,String otvalor20
			,String otvalor21,String otvalor22,String otvalor23,String otvalor24,String otvalor25,String otvalor26,String otvalor27,String otvalor28,String otvalor29,String otvalor30
			,String otvalor31,String otvalor32,String otvalor33,String otvalor34,String otvalor35,String otvalor36,String otvalor37,String otvalor38,String otvalor39,String otvalor40
			,String otvalor41,String otvalor42,String otvalor43,String otvalor44,String otvalor45,String otvalor46,String otvalor47,String otvalor48,String otvalor49,String otvalor50
			,String otvalor51,String otvalor52,String otvalor53,String otvalor54,String otvalor55,String otvalor56,String otvalor57,String otvalor58,String otvalor59,String otvalor60
			,String otvalor61,String otvalor62,String otvalor63,String otvalor64,String otvalor65,String otvalor66,String otvalor67,String otvalor68,String otvalor69,String otvalor70
			,String otvalor71,String otvalor72,String otvalor73,String otvalor74,String otvalor75,String otvalor76,String otvalor77,String otvalor78,String otvalor79,String otvalor80
			,String otvalor81,String otvalor82,String otvalor83,String otvalor84,String otvalor85,String otvalor86,String otvalor87,String otvalor88,String otvalor89,String otvalor90
			,String otvalor91,String otvalor92,String otvalor93,String otvalor94,String otvalor95,String otvalor96,String otvalor97,String otvalor98,String otvalor99
			,String swvalor01,String swvalor02,String swvalor03,String swvalor04,String swvalor05,String swvalor06,String swvalor07,String swvalor08,String swvalor09,String swvalor10
			,String swvalor11,String swvalor12,String swvalor13,String swvalor14,String swvalor15,String swvalor16,String swvalor17,String swvalor18,String swvalor19,String swvalor20
			,String swvalor21,String swvalor22,String swvalor23,String swvalor24,String swvalor25,String swvalor26,String swvalor27,String swvalor28,String swvalor29,String swvalor30
			,String swvalor31,String swvalor32,String swvalor33,String swvalor34,String swvalor35,String swvalor36,String swvalor37,String swvalor38,String swvalor39,String swvalor40
			,String swvalor41,String swvalor42,String swvalor43,String swvalor44,String swvalor45,String swvalor46,String swvalor47,String swvalor48,String swvalor49,String swvalor50
			,String swvalor51,String swvalor52,String swvalor53,String swvalor54,String swvalor55,String swvalor56,String swvalor57,String swvalor58,String swvalor59,String swvalor60
			,String swvalor61,String swvalor62,String swvalor63,String swvalor64,String swvalor65,String swvalor66,String swvalor67,String swvalor68,String swvalor69,String swvalor70
			,String swvalor71,String swvalor72,String swvalor73,String swvalor74,String swvalor75,String swvalor76,String swvalor77,String swvalor78,String swvalor79,String swvalor80
			,String swvalor81,String swvalor82,String swvalor83,String swvalor84,String swvalor85,String swvalor86,String swvalor87,String swvalor88,String swvalor89,String swvalor90
			,String swvalor91,String swvalor92,String swvalor93,String swvalor94,String swvalor95,String swvalor96,String swvalor97,String swvalor98,String swvalor99
			)throws Exception;
	
	public void borrarIncisoCotizacion(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			)throws Exception;
	
	public void reenumerarSituaciones(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			)throws Exception;
	
	public boolean isEstatusGeneraDocumentosCotizacion(String status) throws Exception;
	
	public Map<String, String> cargarDatosCotizacionGrupoEndoso(
			Map<String, String> params) throws Exception;
	
	public void guardarCensoCompletoMultisaludEndoso(String nombreArchivo,
			String cdunieco, String cdramo, String estado, String nmpoliza,
			String cdestadoCli, String cdmuniciCli, String cdplan1,
			String cdplan2, String cdplan3, String cdplan4, String cdplan5,
			String complemento, String nmsuplem) throws Exception;
	
	public void restaurarRespaldoCenso(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;
	
	public void borrarRespaldoCenso(
			String cdunieco
			,String cdramo
			,String nmpoliza
			)throws Exception;
	
	
	/**
	 * Inserta el dato de morbilidad en una poliza
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @throws Exception
	 */
	public void insertaMorbilidad(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem) throws Exception;
	
	public void guardarLayoutGenerico(String nombreArchivo) throws Exception;
	
	public void movimientoTbloqueo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String accion
			)throws Exception;
	
	public String consultaExtraprimOcup(
			String cdtipsit
			)throws Exception;
	
	public List<Map<String,String>>cargarAseguradosExtraprimas(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdgrupo
			,String start
			,String limit)throws Exception;
	
	public void insertaRegistroInfoCenso (List<Map<String, String>> lista) throws Exception;
	
	public List<Map<String, String>> cargarSituacionesGrupo(String cdunieco,
			String cdramo, 
			String estado, 
			String nmpoliza, 
			String nmsuplem,
			String cdgrupo, 
			String start, 
			String limit) throws Exception;
	
	/**
	 ***********actualizaValoresSituacion***********
	 ***********************************************
	 **Actualiza los valores por situacion de
	 **una sola vez
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @param valores
	 * @throws Exception
	 */
	public void actualizaValoresSituacion(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			List<Map<String,String>> valores)throws Exception;

	/**
	 ****************actualizaValoresSituacionTitulares****************
	 ******************************************************************
	 **
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @param cdtipsit
	 * @param valor
	 * @param cdgrupo
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,String>> actualizaValoresSituacionTitulares(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			String cdtipsit,
			String valor,
			String cdgrupo)throws Exception;
	
	public void actualizaNmsituaextMpolisit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac 
			,String nmsuplem
			,String nmsituaext
			)throws Exception;
	
	public void actualizaDatosMpersona(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String cdestciv
			,String ocup
			)throws Exception;
	
	public String validandoCoberturasPLan(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac)throws Exception;
	
	public String[] obtenerCorreosReportarIncidenciasPorTipoSituacion(
			String cdtipsit
			)throws Exception,Exception;
	
	public String obtenSumaAseguradosMedicamentos(
			String cdramo,
			String cdtipsit,
			String cdgarant
			)throws Exception;
	
	public List<ComponenteVO> recuperarTatrirol(String cdramo, String cdrol, String cdtipsit, String cdperson) throws Exception;
	
	public void movimientoMpolisitV2 (String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsituac, String nmsuplem,
			String status, String cdtipsit, String swreduci, String cdagrupa,
			String cdestado, Date fefecsit, Date fecharef, String cdgrupo,
			String nmsituaext, String nmsitaux, String nmsbsitext,
			String cdplan, String cdasegur, String accion) throws Exception;
	
	public Map<String, String> obtieneValidacionDescuentoR6(String tipoUnidad, String uso, String zona,
			String promotoria, String cdagente, String cdtipsit, String cdatribu)throws Exception;

	/**
	 * Insercion de relacion de grupo con poliza colectiva
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param cdtipsit
	 * @param cdgrupo
	 * @param dsgrupo
	 * @param cdplan
	 * @param cdcveplan
	 * @param nmsumaaseg
	 * @param nmdeducible
	 * @param swmat
	 * @param swmed
	 * @param swee
	 * @throws Exception
	 */
	public void insertaMpoligrup(String cdunieco, String cdramo, String estado, String nmpoliza, String cdtipsit,
			String cdgrupo, String dsgrupo, String cdplan, String dsplanVariable, String cdcveplan, String nmsumaaseg, String nmdeducible,
			String swmat, String swmed, String swee) throws Exception;

	/**
	 * Elimina el registro de los documentos de cotizacion de Tdocupol
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param ntramite TODO
	 * @throws Exception
	 */
	public void eliminaDocsCotiza(String cdunieco, String cdramo, String estado, String nmpoliza, String ntramite) throws Exception;
	
	
	/**
	 * Insercion de relacion grupo - cobertura
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param cdtipsit
	 * @param cdgrupo
	 * @param dsgrupo
	 * @param cdplan
	 * @param cdcveplan
	 * @param nmsumaaseg
	 * @param nmdeducible
	 * @param swmat
	 * @param swmed
	 * @param swee
	 * @throws Exception
	 */
	public void insertaMgrupogar(String cdunieco, String cdramo, String estado, String nmpoliza, String cdtipsit,
			String cdgrupo, String cdplan, String nmsumaaseg) throws Exception;

	/**
	 * Elimina todos los grupos de una poliza
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param cdtipsit
	 * @throws Exception
	 */
	public void eliminarGrupos(String cdunieco, String cdramo, String estado, String nmpoliza, String cdtipsit)
			throws Exception;

	public List<Map<String, String>> buscaEmpleado(String pv_administradora_i
            ,String pv_retenedora_i,String pv_clave_i, String pv_nombre_i, String pv_apellido_paterno_i,
			String pv_apellido_materno_i, String pv_rfc_i) throws Exception;

    public String guardaEmpleado(String pv_numsuc_i, String pv_cveent_i, String pv_cveemp_i, String pv_nomemp_i,
            String pv_apaterno_i, String pv_amaterno_i, String pv_rfc_i, String pv_curp_i, String pv_usuario_i,
            String pv_feregist_i, String pv_accion_i) throws Exception;
	
   public List<ComponenteVO> obtenerAtributosPolizaOriginal(Map<String, String> params) throws Exception;

   public String obtenerAseguradoDuplicado(HashMap<String, Object> paramPersona) throws Exception;
   
   public boolean aplicaDxn(String cdtipsit, String cdsisrol, String cdusuari) throws Exception;
   
   public String validaCertificadoGrupo(HashMap<String, Object> paramCertificado) throws Exception;
   
   /**
    * Valida que los datos de incisos del sigs tengan correspondiente sicaps
    */
   public String validaDatosAutoSigs(List<Map<String,String>> incisosSigs) throws Exception;

   public void refrescarCensoColectivo(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsolici)throws Exception;
   
	/**
    * Metodo para consultar los asegurados por grupo luego de aplicar el filtro de busqueda
    * @param cdunieco
    * @param cdramo
    * @param estado
    * @param nmpoliza
    * @param nmsuplem
    * @param cdgrupo
    * @param numsitaux
    * @paran start
    * @param limit
    * @param filtro
    * @param valorFiltro
    * @return
    * @throws Exception
    */

	public AseguradosFiltroVO cargarAseguradosFiltroGrupo(HashMap<String, String> params) throws Exception;	
	
	public List<Map<String,String>>cargarAseguradosFiltroExtraprimas(Map<String,String>params)throws Exception;

	public List<Map<String, String>> cargarSituacionesFiltroGrupo(
			String cdunieco
			, String cdramo
			, String estado
			, String nmpoliza
			, String nmsuplem
			, String cdgrupo
			, String start
			, String limit
			, String filtro
			, String valorFiltro) throws Exception;

	/**
	 * Obtiene Mapa de los codigos postales de un producto.
	 * @param cdramo
	 * @return
	 * @throws Exception
	 */
	HashMap<String, String> obtieneCodigosPostalesProductos() throws Exception;

	/**
	 * Obtiene las coberturas por plan y si son obligatorias. 
	 * @param pv_cdramo_i
	 * @param pv_cdplan_i
	 * @param pv_cdtipsit_i
	 * @return lista de coberturas por plan
	 * @throws Exception
	 */
	public List<Map<String, String>> getCoberturas(String pv_cdramo_i, String pv_cdplan_i, String pv_cdtipsit_i)
			throws Exception;

	public String aplicaDescAutos(String pcdunieco, String pcdramo, String pnmpoliza, String pdesc, String pisflot) throws Exception;
	
	/**
	 * Metodo que aplica recargo por pago fraccionado en renovaciones al finalizar la cotización
	 * @param cdunieco
	 * @param cdramo
	 * @param nmpoliza
	 * @param recargoPF
	 * @param flotilla
	 */
	public String aplicaRecargoPagoFraccionado(String cdunieco, String cdramo, String nmpoliza, String recargoPF, String flotilla) throws Exception;
	
	/**
	 * Mapa de regreso: {
	 *     MINIMO: -15.5,
	 *     MAXIMO: 25.5
	 * }
	 */
	public Map<String, String> recuperarRangoDescuentoRecargo (String cdramo, String cdtipsit, String cdusuari, String cdsisrol) throws Exception;

	public List<Map<String, String>> cargarResultadosCotizacionLicenciaFlotilla(String cdunieco, String cdramo, String estado,
			String nmpoliza) throws Exception;
}