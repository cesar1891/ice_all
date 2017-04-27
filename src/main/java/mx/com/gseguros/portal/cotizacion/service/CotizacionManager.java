package mx.com.gseguros.portal.cotizacion.service;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.portal.consultas.model.AseguradosFiltroVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaImapSmapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSlistSmapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSlistVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSmapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaVoidVO;
import mx.com.gseguros.portal.cotizacion.model.ParametroCotizacion;
import mx.com.gseguros.portal.general.model.ComponenteVO;


public interface CotizacionManager
{
	
	public void movimientoTvalogarGrupo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdtipsit
			,String cdgrupo
			,String cdgarant
			,String status
			,String cdatribu
			,String valor)throws Exception;
	
	public void movimientoMpolisitTvalositGrupo(
			String  cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdgrupo
			,String otvalor06
			,String otvalor07
			,String otvalor08
			,String otvalor09
			,String otvalor10
			,String otvalor11
			,String otvalor12
			,String otvalor22
			,String otvalor23
			,String otvalor24
			,String otvalor25
			,String otvalor26
			,String otvalor13
			,String otvalor16
			)throws Exception;
	
	public void movimientoMpoligarGrupo(
			String  cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdtipsit
			,String cdgrupo
			,String cdgarant
			,String status
			,String cdmoneda
			,String accion
			,String respvalogar)throws Exception;
	
	public Map<String,String> cargarDatosCotizacionGrupo(
			String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String nmpoliza
			,String ntramite) throws Exception;
	
	public ManagerRespuestaSmapVO cargarDatosCotizacionGrupo2(
			String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String nmpoliza
			,String ntramite);
	
	public Map<String,String> cargarDatosCotizacionGrupoEndoso(
			String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String nmpoliza
			,String ntramite) throws Exception;
	
	public List<Map<String,String>>cargarGruposCotizacion(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza)throws Exception;
	
	public ManagerRespuestaSlistVO cargarGruposCotizacion2(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza);

	public ManagerRespuestaSlistVO cargarGruposCotizacionReexpedicion(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza);
	
	public Map<String,String>cargarDatosGrupoLinea(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdgrupo)throws Exception;
	
	public List<Map<String,String>>cargarTvalogarsGrupo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdgrupo)throws Exception;
	
	public List<Map<String,String>>cargarTarifasPorEdad(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdplan
			,String cdgrupo
			,String cdperpag)throws Exception;
	
	public List<Map<String,String>>cargarTarifasPorCobertura(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdplan
			,String cdgrupo
			,String cdperpag)throws Exception;
	
	public String cargarNombreAgenteTramite(String ntramite)throws Exception;
	
	public Map<String,String>cargarPermisosPantallaGrupo(String cdsisrol,String status)throws Exception;
	
	public void guardarCensoCompletoMultisalud(
			String nombreArchivo
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdedo
			,String cdmunici
			,String cdplan1
			,String cdplan2
			,String cdplan3
			,String cdplan4
			,String cdplan5
			,String complemento
			)throws Exception;

	public int obtieneTipoValorAutomovil(String codigoPostal, String tipoVehiculo)throws Exception;

	public String obtieneCodigoPostalAutomovil(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsituac, String nmsuplem) throws Exception;
	
	/**
	 *********cargarAseguradosExtraprimas*********
	 *********************************************
	 **Obtiene asegurados y agrupa por familia
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @param cdgrupo
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,String>>cargarAseguradosExtraprimas(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdgrupo
			,String start
			,String limit)throws Exception;
		
	public void guardarExtraprimaAsegurado(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String cdtipsit
			,String ocupacion
			,String extraprimaOcupacion
			,String peso
			,String estatura
			,String extraprimaSobrepeso
			,String cdgrupo
			)throws Exception;
	
	/**
	 ***********guardarExtraprimaAsegurado***********
	 ************************************************
	 **Guarda valores de situacion de una sola vez
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @param cdtipsit
	 * @param slist1
	 * @throws Exception
	 */
	public void guardarExtraprimaAsegurado(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdtipsit
			,List<Map<String,String>> slist1
			)throws Exception;
	
	public List<Map<String,String>>cargarAseguradosGrupo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdgrupo)throws Exception;
	
	public List<Map<String,String>>cargarAseguradosGrupo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdgrupo
			,String start
			,String limit)throws Exception;
	
	public void borrarMpoliperGrupo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdgrupo)throws Exception;
	
	@Deprecated
	public Map<String,String>cargarTipoSituacion(String cdramo,String cdtipsit)throws Exception;
	
	public String cargarCduniecoAgenteAuto(String cdagente, String cdtipram)throws Exception;
	
	public Map<String,String>obtenerDatosAgente(String cdagente,String cdramo)throws Exception;
	
	public ManagerRespuestaSmapVO obtenerParametrosCotizacion(
			ParametroCotizacion parametro
			,String cdramo
			,String cdtipsit
			,String clave4
			,String clave5);
	
	public ManagerRespuestaSmapVO cargarAutoPorClaveGS(String cdramo,String clavegs,String cdtipsit,String cdsisrol, String tipoUnidad) throws Exception;
	
	public ManagerRespuestaSmapVO cargarClaveGSPorAuto(String cdramo,String modelo) throws Exception;
	
	public ManagerRespuestaSmapVO cargarSumaAseguradaAuto(
			String cdsisrol,String modelo,String version,String cdramo,String cdtipsit)throws Exception;
	
	public ManagerRespuestaVoidVO agregarClausulaICD(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String cdclausu
			,String nmsuplem
			,String icd)throws Exception;
	
	public ManagerRespuestaSlistVO cargarClausulaICD(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String cdclausu
			,String nmsuplem)throws Exception;
	
	public ManagerRespuestaVoidVO borrarClausulaICD(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String cdclausu
			,String nmsuplem
			,String icd)throws Exception;
	
	public List<Map<String,String>>cargarConfiguracionGrupo(String cdramo,String cdtipsit)throws Exception;
	
	public ComponenteVO cargarComponenteTatrisit(String cdtipsit,String cdusuari,String cdatribu)throws Exception;

	public ComponenteVO cargarComponenteTatrigar(String cdramo,String cdtipsit,String cdgarant,String cdatribu)throws Exception;
	
	public ManagerRespuestaVoidVO validarDescuentoAgente(
			String  tipoUnidad
			,String uso
			,String zona
			,String promotoria
			,String cdagente
			,String descuento);
	
	public void movimientoTdescsup(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nsuplogi
			,String cdtipsup
			,Date feemisio
			,String nmsolici
			,Date fesolici
			,Date ferefere
			,String cdseqpol
			,String cdusuari
			,String nusuasus
			,String nlogisus
			,String cdperson
			,String accion)throws Exception;
	
	public ManagerRespuestaImapSmapVO pantallaCotizacionGrupo(
			String cdramo
			,String cdtipsit
			,String ntramite
			,String ntramiteVacio
			,String status
			,String cdusuari
			,String cdsisrol
			,String nombreUsuario
			,String cdagente
			);
	
	public ManagerRespuestaSmapVO cargarClienteCotizacion(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza);
	
	public ManagerRespuestaSmapVO cargarConceptosGlobalesGrupo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdperpag);
	
	public ManagerRespuestaSmapVO generarTramiteGrupo(
			String cdunieco
			,String cdramo
			,String nmpoliza
			,String feini
			,String fefin
			,String cdperpag
			,String pcpgocte
			,Map<String,String> tvalopol
			,String ntramite
			,String ntramiteVacio
			,String miTimestamp
			,String rutaDocumentosTemporal
			,String tipoCenso
			,String dominioServerLayouts
			,String userServerLeyouts
			,String passServerLayouts
			,String directorioServerLayouts
			,String cdtipsit
			,List<Map<String,Object>>grupos
			,String codpostal
			,String cdedo
			,String cdmunici
			,String cdagente
			,String cdusuari
			,String cdsisrol
			,String clasif
			,String LINEA_EXTENDIDA
			,String cdpersonCli
			,String nombreCli
			,String rfcCli
			,String dsdomiciCli
			,String nmnumeroCli
			,String nmnumintCli
			,String cdelemen
			,boolean sincenso
			,boolean censoAtrasado
			,boolean resubirCenso
			,boolean complemento
			,String cdpool
			,String nombreCensoConfirmado
			,boolean asincrono
			,String cdideper_
			,String cdideext_
			,String nmpolant
			,String nmrenova
			,UserVO usuarioSesion
			,boolean duplicar
			);
	
	public ManagerRespuestaSlistVO obtenerTiposSituacion();
	
	public List<Map<String,String>>cargarAseguradosExtraprimas2(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdgrupo
			)throws Exception;
	/**
	 **************guardarValoresSituaciones**************
	 *****************************************************
	 * Recibe los valores a actualizar en
	 * base de datos y lanza valores por 
	 * defecto
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @param situaciones
	 * @param cdtipsit
	 * @param guardarExt
	 * @return
	 */
	public ManagerRespuestaVoidVO guardarValoresSituaciones(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			List<Map<String,String>>situaciones,
			String cdtipsit,
			Boolean guardarExt);
	
	/**
	 **************guardarValoresSituacionesTitular**************
	 ************************************************************
	 **Actualiza el valor de extraprima ocupacional
	 **y devuelve las situaciones correspondientes
	 **para lanzar los valores por defecto
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @param cdtipsit
	 * @param valor
	 * @param cdgrupo
	 * @return
	 */
	public ManagerRespuestaVoidVO guardarValoresSituacionesTitular(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			String cdtipsit,
			String valor,
			String cdgrupo);
	
	public ManagerRespuestaSmapVO subirCensoCompleto(
			String cdunieco
			,String cdramo
			,String nmpoliza
			,String feini
			,String fefin
			,String cdperpag
			,String pcpgocte
			,String rutaDocsTemp
			,String censoTimestamp
			,String dominioServerLayout
			,String usuarioServerLayout
			,String passwordServerLayout
			,String direcServerLayout
			,String cdtipsit
			,String cdusuari
			,String cdsisrol
			,String cdagente
			,String codpostalCli
			,String cdedoCli
			,String cdmuniciCli
			,List<Map<String,Object>>grupos
			,String clasif
			,String LINEA_EXTENDIDA
			,String cdpersonCli
			,String nombreCli
			,String rfcCli
			,String dsdomiciCli
			,String nmnumeroCli
			,String nmnumintCli
			,String ntramite
			,String ntramiteVacio
			,String cdelemen
			,String nombreCensoConfirmado
			,String cdideper_
			,String cdideext_
			,String nmpolant
			,String nmrenova
			,UserVO usuarioSesion
			);

	public ManagerRespuestaSmapVO confirmarCensoCompleto(
			String cdunieco
			,String cdramo
			,String nmpoliza
			,String feini
			,String fefin
			,String cdperpag
			,String pcpgocte
			,String rutaDocsTemp
			,String censoTimestamp
			,String dominioServerLayout
			,String usuarioServerLayout
			,String passwordServerLayout
			,String direcServerLayout
			,String cdtipsit
			,String cdusuari
			,String cdsisrol
			,String cdagente
			,String codpostalCli
			,String cdedoCli
			,String cdmuniciCli
			,List<Map<String,Object>>grupos
			,String clasif
			,String LINEA_EXTENDIDA
			,String cdpersonCli
			,String nombreCli
			,String rfcCli
			,String dsdomiciCli
			,String nmnumeroCli
			,String nmnumintCli
			,String ntramite
			,String ntramiteVacio
			,String cdelemen
			,String nombreCensoConfirmado
			,String cdideper_
			,String cdideext_
			,String nmpolant
			,String nmrenova
			,UserVO usuarioSesion
			,String estatuRenovacion
			);
	
	
	public ManagerRespuestaVoidVO validarCambioZonaGMI(
			String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String codpostal);
	public ManagerRespuestaVoidVO validarEnfermedadCatastGMI(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String circHosp);
	
	public String cargarTabuladoresGMIParche(
			String circulo
			,String cdatribu)throws Exception;
	
	public String guardarContratanteColectivo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String rfc
			,String cdperson
			,String nombre
			,String cdpostal
			,String cdedo
			,String cdmunici
			,String dsdomici
			,String nmnumero
			,String nmnumint
			,String nmorddom
			,boolean esConfirmaEmision
			,UserVO usuarioSesion
			)throws Exception;
	
	public ManagerRespuestaSmapVO cargarTramite(String ntramite)throws Exception;
	
	public boolean cargarBanderaCambioCuadroPorProducto(String cdramo);
	
	public ManagerRespuestaSlistSmapVO cotizar(
			String cdunieco
			,String cdramo
			,String cdtipsit
			,String cdusuari
			,String cdelemen
			,String nmpoliza
			,String feini
			,String fefin
			,String fesolici
			,String cdpersonCli
			,String nmorddomCli
			,String cdideperCli
			,boolean noTarificar
			,boolean conIncisos
			,List<Map<String,String>>incisos
			,boolean flagMovil
			,Map<String,String>tvalopol
			,String cdagente
			,UserVO usuarioSesion
			,String fromSigs
			,String cduniext
			,String renramo
			,String nmpolie
			,String ntramite
			)throws Exception;
	
	
	public ManagerRespuestaSlistSmapVO cotizarContinuacion(String nmpoliza,  String cdunieco, String cdramo, String cdelemen, String cdtipsit, String nmpoliza2, boolean flagMovil)throws Exception;
	
	public boolean validaDomicilioCotizacionTitular(Map<String,String> params)throws Exception;
	
	
	@Deprecated
	public boolean validarCuadroComisionNatural(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;
	
	@Deprecated
	public Map<String,String>cargarTvalopol(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;
	
	@Deprecated
	public String cargarPorcentajeCesionComisionAutos(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;
	
	@Deprecated
	public void ejecutaValoresDefectoTarificacionConcurrente(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String tipotari
			,String cdperpag
			)throws Exception;
	
	@Deprecated
	public void ejecutaValoresDefectoConcurrente(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String tipotari
			,String cdperpag
			)throws Exception;
	
	@Deprecated
	public void ejecutaTarificacionConcurrente(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String tipotari
			,String cdperpag
			,String cdusuari
			,String cdsisrol
			)throws Exception;
	
	@Deprecated
	public void actualizaValoresDefectoSituacion(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			)throws Exception;
	
	public Map<String,Object> complementoSaludGrupo(
			String ntramite
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String complemento
			,File censo
			,String rutaDocumentosTemporal
			,String dominioServerLayouts
			,String userServerLayouts
			,String passServerLayouts
			,String rootServerLayouts
			,String cdtipsit
			,String cdusuari
			,String cdsisrol
			,String cdagente
			,String codpostalCli
			,String cdestadoCli
			,String cdmuniciCli
			,String cdplan1
			,String cdplan2
			,String cdplan3
			,String cdplan4
			,String cdplan5
			)throws Exception;
	
	@Deprecated
	public boolean validaPagoPolizaRepartido(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;
	
	/*
	@Deprecated
	public String turnaPorCargaTrabajo(
			String ntramite
			,String cdsisrol
			,String status
			)throws Exception;
	*/
	
	public String guardarConfiguracionGarantias(
			String cdramo
			,String cdtipsit
			,String cdplan
			,String cdpaq
			,String dspaq
			,String derpol
			,List<Map<String,String>>tvalogars
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

	public Map<String,String> obtieneDatosContratantePoliza(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			)throws Exception;
	
	public void actualizaCesionComision(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;

	/**
	 * Actualiza todos los registros de tvalosit para productos que tengan codigo postal, estado y municipio
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @param cdpostal
	 * @param cdedo
	 * @param cdmunici
	 * @throws Exception
	 */
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
	
	public void procesoColectivoAsincrono(
			boolean hayTramite
			,boolean hayTramiteVacio
			,boolean censoAtrasado
			,boolean complemento
			,String cdunieco
			,String cdramo
			,String nmpoliza
			,String cdperpag
			,String clasif
			,String LINEA
			,String LINEA_EXTENDIDA
			,List<Map<String,Object>> olist1
			,String cdtipsit
			,String cdusuari
			,String cdsisrol
			,boolean duplicar
			);
	
	/**
	 * 
	 * @param comprarCdunieco
	 * @param comprarCdramo
	 * @param comprarNmpoliza
	 * @param cdtipsit
	 * @param fechaInicio
	 * @param fechaFin
	 * @param ntramite
	 * @param cdagenteExt
	 * @param comprarCdciaaguradora
	 * @param comprarCdplan
	 * @param comprarCdperpag
	 * @param cdusuari
	 * @param cdsisrol
	 * @param cdelemen
	 * @param esFlotilla
	 * @param tipoflot
	 * @param cdpersonCli
	 * @param cdideperCli
	 * @param nombreReporteCotizacion
	 * @param nombreReporteCotizacionFlot
	 * @param swdesdesigs TODO
	 * @param porredau 
	 * @param porparti 
	 * @return ntramite Numero de tramite creado
	 * @throws Exception
	 */
	public String procesoComprarCotizacion (
			String comprarCdunieco,
			String comprarCdramo,
			String comprarNmpoliza,
			String cdtipsit,
			String fechaInicio,
			String fechaFin,
			String ntramite,
			String cdagenteExt,
			String comprarCdciaaguradora,
			String comprarCdplan,
			String comprarCdperpag,
			String cdusuari,
			String cdsisrol,
			String cdelemen,
			boolean esFlotilla,
			String tipoflot,
			String cdpersonCli,
			String cdideperCli,
			String nombreReporteCotizacion,
			String nombreReporteCotizacionFlot,
			UserVO usuarioSesion,
			String swrenovacion,
			String sucursal,
			String ramo,
			String poliza, 
			String porparti, 
			String porredau
	) throws Exception;
	
	@Deprecated
	public List<Map<String,String>> recuperarListaDocumentosParametrizados(
			String cdorddoc
			,String nmsolici
			,String ntramite
			)throws Exception;
	
	/*
	@Deprecated
	public List<Map<String,String>> generarDocumentosBaseDatos(
			String cdorddoc
			,String nmsolici
			,String ntramite
			)throws Exception;
	*/
	
	@Deprecated
	public void actualizarFefecsitMpolisit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			)throws Exception;
	
	public void borrarIncisoCotizacion(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			)throws Exception;
	
	@Deprecated
	public boolean isEstatusGeneraDocumentosCotizacion(String status) throws Exception;
	
	public void guardarCensoCompletoMultisaludEndoso(
			String nombreArchivo
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdedo
			,String cdmunici
			,String cdplan1
			,String cdplan2
			,String cdplan3
			,String cdplan4
			,String cdplan5
			,String complemento
			,String nmsuplem
			)throws Exception;

	public void ejecutasigsvdefEnd(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String cdgarant
			,String cdtipsup)throws Exception;
	
	@Deprecated
	public void restaurarRespaldoCenso(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;

	@Deprecated
	public void borrarRespaldoCenso(
			String cdunieco
			,String cdramo
			,String nmpoliza
			)throws Exception;

	public void borrarMpoliperSituac0(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem,
			String cdrol)throws Exception;
	
	public void guardarLayoutGenerico(
			String nombreArchivo
			)throws Exception;
	
	@Deprecated
	public void movimientoTbloqueo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String accion
			)throws Exception;
	
	/**
	 * ACTUALIZA UN OTVALOR USANDO UN LIKE %+dsatribu+%
	 * @param ntramite
	 * @param dsatribu
	 * @param otvalor
	 * @param accion (I,U,D) Insert, Update, Delete
	 * @throws Exception
	 */
	public void actualizarOtvalorTramitePorDsatribu(
			String ntramite
			,String dsatribu
			,String otvalor
			,String accion
			)throws Exception;
	
	/**
	 * RECUPERA UN OTVALOR USANDO UN LIKE %+dsatribu+%
	 * @param ntramite
	 * @param dsatribu
	 * @return otvalor
	 * @throws Exception
	 */
	public String recuperarOtvalorTramitePorDsatribu(
			String ntramite
			,String dsatribu
			)throws Exception;
	
	/**
	 * Guarda censo informativo
	 * @param params
	 * @throws Exception
	 */
	public void insertaRegistroInfoCenso (List<Map<String,String>> lista) throws Exception;
	
	/**
	 * 
	 * @param cdtipsit
	 * @return
	 * @throws Exception
	 */
	public String consultaExtraprimOcup(String cdtipsit)throws Exception;
	
	/**
	 *************cargarAseguradosExtraprimas2*************
	 **Carga asegurados y agrupa por familia
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @param cdgrupo
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,String>> cargarAseguradosExtraprimas2(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdgrupo
			,String start
			,String limit
			)throws Exception;
	
	public String obtenSumaAseguradosMedicamentos(
			String cdramo,
			String cdtipsit,
			String cdgarant
			)throws Exception;
	
	public String recuperarDescripcionEstatusTramite (String status) throws Exception;
	
	@Deprecated
	// LOS METODOS DE MANAGER SIN LOGICA QUE SOLO INVOCAN A UN DAO DEBEN DEJAR DE USARSE
	public void actualizarCdplanGrupo(String cdunieco, String cdramo, String estado, String nmpoliza,
			String nmsuplem, String cdgrupo, String cdplan) throws Exception;

	public Map<String, String> obtieneValidacionDescuentoR6(String tipoUnidad, String uso, String zona,
			String promotoria, String cdagente, String cdtipsit, String cdatribu) throws Exception;
	public Map<String,String> obtieneFormapago(String administradora,String retenedora) throws Exception;
	public List<Map<String,String>>cargarAseguradosExtraprimas(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdgrupo)throws Exception;

	public List<Map<String,String>> buescaEmpleado(String pv_administradora_i
            ,String pv_retenedora_i,String pv_clave_i, String pv_nombre_i, String pv_apellido_paterno_i,
			String pv_apellido_materno_i, String pv_rfc_i) throws Exception;

    public String guardaEmpleado(String pv_numsuc_i, String pv_cveent_i, String pv_cveemp_i, String pv_nomemp_i,
            String pv_apaterno_i, String pv_amaterno_i, String pv_rfc_i, String pv_curp_i, String pv_usuario_i,
            String pv_feregist_i, String pv_accion_i) throws Exception;

	public List<ComponenteVO> obtenerAtributosPolizaOriginal(Map<String, String> params) throws Exception;
	
	public String obtenerAseguradoDuplicado(HashMap<String, Object> paramPersona) throws Exception;

    public List<Map<String, String>> obtieneRetAdmin(String administradora, String retenedora) throws Exception;
    
    /**
     * Agrega un asegurado y lo asigna a una poliza
     * @param cdunieco
     * @param cdramo
     * @param estado
     * @param nmpoliza
     * @param nmsuplem
     * @param feefecto
     * @param dsnombre
     * @param dsnombre1
     * @param paterno
     * @param materno
     * @param cdrfc
     * @param sexo
     * @param fenacimi
     * @param cdestciv
     * @param dsocupacion
     * @param cdtipsit
     * @param cdplan
     * @param nmorddom
     * @param cdagrupa
     * @param otvalor01
     * @param otvalor02
     * @param otvalor03
     * @param otvalor04
     * @param otvalor05
     * @param otvalor06
     * @param otvalor07
     * @param otvalor08
     * @param otvalor09
     * @param otvalor10
     * @throws Exception
     */
    public void agregarAsegurado(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem,
            String feefecto, String dsnombre, String dsnombre1, String paterno, String materno, String cdrfc,
            String sexo, String fenacimi, String cdestciv, String dsocupacion, String cdtipsit, String cdplan,
            String nmorddom, String cdagrupa, String otvalor01, String otvalor02, String otvalor03, String otvalor04,
            String otvalor05, String otvalor06, String otvalor07, String otvalor08, String otvalor09, String otvalor10)
            throws Exception;

    public String validaCertificadoGrupo(HashMap<String, Object> paramCertificado)throws Exception;
    
    public void guardaDatosAgenteSecundarioSigs(String ntramite,String agt_sec, String porc_part) throws Exception;
    
    public void refrescarCensoColectivo(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsolici)throws Exception;
	
	public AseguradosFiltroVO cargarAseguradosFiltroGrupo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdgrupo
			//,String nmsitaux
			,String filtro
			,String valorFiltro
			,String start
			,String limit)throws Exception;

	public List<Map<String, String>> cargarAseguradosFiltroExtraprimas(
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

	public List<Map<String, String>> cargarAseguradosFiltroExtraprimas2(
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
	
	public String validaAgenteActivo(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem) throws Exception;

	/**
	 * Recupera todos los codigos postales de un producto
	 * @param cdramo
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, String> obtieneCodigosPostalesProductos()throws Exception;

    public String aplicaDescAutos(String cdunieco, String cdramo, String nmpoliza,
            String modPrim, String cdtipsit)
            throws Exception;
    
    public String validaDatosAutoSigs(List<Map<String,String>> incisosSigs) throws Exception;
    
    public List<Map<String, String>> cargarResultadosCotizacion(
            String cdusuari, String cdunieco, String cdramo, String estado,
            String nmpoliza, String cdelemen, String cdtipsit) throws Exception;
    
    public List<Map<String, String>> cargarResultadosCotizacionAutoFlotilla(
            String cdunieco, String cdramo, String estado, String nmpoliza)
            throws Exception;

    /**
     * Metodo que elimina todos los documentos predefinidos de Cotizacion
     * @param cdunieco
     * @param cdramo
     * @param polizaWorking
     * @param nmpoliza
     * @param ntramite
     */
	public void eliminaDocsCotiza(String cdunieco, String cdramo, String estado, String nmpoliza,
			String ntramite)throws Exception;

	/**
	 * Metodo que inserta Datos de poliza de Grupo mpoligroup
	 * @param cdunieco
	 * @param cdramo
	 * @param polizaWorking
	 * @param nmpoliza
	 * @param cdtipsit
	 * @param string
	 * @param string2
	 * @param string3
	 * @param string4
	 * @param object
	 * @param string5
	 * @param string6
	 * @param no
	 * @param no2
	 * @param no3
	 */
	public void insertaMpoligrup(String cdunieco, String cdramo, String estado, String nmpoliza, String cdtipsit,
			String cdgrupo, String dsgrupo, String cdplan, String dsplanVariable, String cdcveplan, String nmsumaaseg, String nmdeducible,
			String swmat, String swmed, String swee)throws Exception;

	/**
	 * Metodo que inserta mgrupogar Garantias del grupo
	 * @param cdunieco
	 * @param cdramo
	 * @param polizaWorking
	 * @param nmpoliza
	 * @param cdtipsit
	 * @param string
	 * @param string2
	 * @param string3
	 */
	public void insertaMgrupogar(String cdunieco, String cdramo, String estado, String nmpoliza, String cdtipsit,
			String cdgrupo, String cdplan, String nmsumaaseg)throws Exception;

	/**
	 * Metodo que elimina los grupos de mpoligoup
	 * @param cdunieco
	 * @param cdramo
	 * @param polizaWorking
	 * @param nmpoliza
	 * @param cdtipsit
	 */
	public void eliminarGrupos(String cdunieco, String cdramo, String estado, String nmpoliza, String cdtipsit)throws Exception;
	
	/**
	 * Metodo que aplica recargo por pago fraccionado en renovaciones al finalizar la cotización
	 * @param cdunieco
	 * @param cdramo
	 * @param nmpoliza
	 * @param recargoPF
	 * @param flotilla
	 */
	public String aplicaRecargoPagoFraccionado(String cdunieco, String cdramo, String nmpoliza, String recargoPF, String flotilla) throws Exception;

	public List<Map<String, String>> cargarResultadosCotizacionLicenciaFlotilla(String cdunieco, String cdramo, String estado,
			String nmpoliza) throws Exception;
}