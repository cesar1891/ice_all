package mx.com.gseguros.portal.endosos.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.mesacontrol.model.FlujoVO;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaVoidVO;
import mx.com.gseguros.portal.cotizacion.model.SlistSmapVO;

public interface EndososAutoManager
{
	public Map<String,Object> construirMarcoEndosos(String cdusuari,String cdsisrol)throws Exception;
	
	public String recuperarColumnasIncisoRamo(String cdramo) throws Exception;
	
	public SlistSmapVO recuperarEndososClasificados(
			String cdramo
			,String nivel
			,String multiple
			,String tipoflot
			,List<Map<String,String>>incisos
			,String cdsisrol
			,String cancelada
			,String cdusuari
			,String cdtipsit
			,String cdunieco
			,String estado
			,String nmpoliza
			)throws Exception;
	
	/**
	 * 
	 * @param cdtipsup
	 * @param cdramo
	 * @param cdsisrol
	 * @return
	 * @throws Exception
	 */
	public Map<String,Item>pantallaEndosoValosit(String cdtipsup, String cdramo, String cdsisrol) throws Exception;
	
	public void guardarTvalositEndoso(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String status
			,String cdtipsit
			,String otvalor01,String otvalor02,String otvalor03,String otvalor04,String otvalor05
			,String otvalor06,String otvalor07,String otvalor08,String otvalor09,String otvalor10
			,String otvalor11,String otvalor12,String otvalor13,String otvalor14,String otvalor15
			,String otvalor16,String otvalor17,String otvalor18,String otvalor19,String otvalor20
			,String otvalor21,String otvalor22,String otvalor23,String otvalor24,String otvalor25
			,String otvalor26,String otvalor27,String otvalor28,String otvalor29,String otvalor30
			,String otvalor31,String otvalor32,String otvalor33,String otvalor34,String otvalor35
			,String otvalor36,String otvalor37,String otvalor38,String otvalor39,String otvalor40
			,String otvalor41,String otvalor42,String otvalor43,String otvalor44,String otvalor45
			,String otvalor46,String otvalor47,String otvalor48,String otvalor49,String otvalor50
			,String otvalor51,String otvalor52,String otvalor53,String otvalor54,String otvalor55
			,String otvalor56,String otvalor57,String otvalor58,String otvalor59,String otvalor60
			,String otvalor61,String otvalor62,String otvalor63,String otvalor64,String otvalor65
			,String otvalor66,String otvalor67,String otvalor68,String otvalor69,String otvalor70
			,String otvalor71,String otvalor72,String otvalor73,String otvalor74,String otvalor75
			,String otvalor76,String otvalor77,String otvalor78,String otvalor79,String otvalor80
			,String otvalor81,String otvalor82,String otvalor83,String otvalor84,String otvalor85
			,String otvalor86,String otvalor87,String otvalor88,String otvalor89,String otvalor90
			,String otvalor91,String otvalor92,String otvalor93,String otvalor94,String otvalor95
			,String otvalor96,String otvalor97,String otvalor98,String otvalor99
			,String tstamp)throws Exception;
	
	public void confirmarEndosoTvalositAuto(
			String cdtipsup
			,String tstamp
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String feefecto
			,String cdusuari
			,String cdsisrol
			,String cdelemen
			,UserVO usuarioSesion
			,List<Map<String,String>> incisos
			,FlujoVO flujo
			)throws Exception;
	
	/**
	 * @return incisoPoliza => Map String String,
	 *         tvalopol     => Map String String,
	 *         tconvalsit   => List Map String String,
	 *         cdperson     => String,
	 *         cdideper     => cdideper
	 */
	public Map<String,Object> recuperarDatosEndosoAltaIncisoAuto(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			)throws Exception;
	
	public Map<String, Object> confirmarEndosoAltaIncisoAuto(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,List<Map<String,String>>incisos
			,String cdusuari
			,String cdelemen
			,String cdtipsup
			,String fecha
			,UserVO usuarioSesion
			,String cdsisrol
			,FlujoVO flujo
			,String confirmar
			,String cdperpag
			,String cdagente
			)throws Exception;
	
	public Map<String,Item> endosoBajaIncisos(
			String cdramo
			)throws Exception;
	
	public void confirmarEndosoBajaIncisos(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,List<Map<String,String>>incisos
			,String cdusuari
			,String cdelemen
			,String cdtipsup
			,String fecha
			,UserVO usuarioSesion
			,boolean devolver
			,String cdsisrol
			,FlujoVO flujo
			)throws Exception;

	public void guardarEndosoDespago(
			 String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmrecibo
			,String nmimpres
			,String cdtipsup
			,UserVO usuarioSesion
			,String cdusuari
			,String cdsisrol
			,FlujoVO flujo
			)throws Exception;
	
	
	/**
	 * Para recuperar datos del asegurado alterno
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> obtieneAseguradoAlterno(String cdunieco, String cdramo ,String estado ,String nmpoliza, String nmsuplem) throws Exception;

	public void guardarEndosoAseguradoAlterno(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String ntramite
			,String cdelemen
			,String cdusuari
			,String cdtipsup
			,String status
			,String fechaEndoso
			,Date dFechaEndoso
			,String aseguradoAlterno
			,String cdsisrol
			,FlujoVO flujo
			)throws Exception;
	
	public void guardarEndosoVigenciaPoliza(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String ntramite
			,String cdelemen
			,String cdusuari
			,String cdtipsup
			,String status
			,String fechaEndoso
			,Date dFechaEndoso
			,String feefecto
			,String feproren
			,String nmsuplemOriginal
			,String cdsisrol
			,FlujoVO flujo
			)throws Exception;
	
	public void guardarEndosoTextoLibre(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String ntramite
			,String cdelemen
			,String cdusuari
			,String cdtipsup
			,String status
			,String fechaEndoso
			,Date dFechaEndoso
			,String feefecto
			,String feproren
			,List<Map<String,String>> situaciones
			,String dslinea
			,String cdsisrol
			,FlujoVO flujo
			)throws Exception;
	
	public void validarEndosoAnterior(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdtipsup
			)throws Exception;
	
	public void validarEndosoPagados(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdtipsup
			)throws Exception;
	
	public Map<String,Item>endosoClaveAuto(
			String cdsisrol
			,String cdramo
			,String cdtipsit
			)throws Exception;
	
	public Map<String,Object> guardarEndosoClaveAuto(
			String cdtipsup
			,String cdusuari
			,String cdsisrol
			,String cdelemen
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String feefecto
			,Map<String,String> valores
			,Map<String,String> incisoAnterior
			,UserVO usuarioSesion
			,FlujoVO flujo
			,String confirmar
			,String p_plan
			,String cdperpag
			)throws Exception;
	
	public List<Map<String,String>> obtenerRetroactividad(
			String cdsisrol
			,String cdramo
			,String cdtipsup
			,String fechaProceso
			)throws Exception;

	public List<Map<String,String>> obtieneRecibosPagados(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;
	
	public Map<String,Item> endosoDevolucionPrimas(
			String cdtipsup
			,String cdramo
			)throws Exception;
	
	public Map<String,Item> endosoRehabilitacionAuto(
			String cdsisrol
			,String cdramo
			)throws Exception;

	public Map<String,Item> endosoRehabilitacionSalud(
			String cdsisrol
			,String cdramo
			)throws Exception;
	
	public void confirmarEndosoRehabilitacionAuto(
			String cdusuari
			,String cdsisrol
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdtipsup
			,String nsuplogi
			,String cddevcia
			,String cdgestor
			,Date   feemisio
			,Date   feinival
			,Date   fefinval
			,Date   feefecto
			,Date   feproren
			,String cdmoneda
			,String nmsuplem
			,String cdelemen
			,UserVO usuarioSesion
			,FlujoVO flujo
			)throws Exception;

	public void confirmarEndosoRehabilitacionSalud(
			String cdusuari
			,String cdsisrol
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdtipsup
			,String nsuplogi
			,String cddevcia
			,String cdgestor
			,Date   feemisio
			,Date   feinival
			,Date   fefinval
			,Date   feefecto
			,Date   feproren
			,String cdmoneda
			,String nmsuplem
			,String cdelemen
			,UserVO usuarioSesion
			,FlujoVO flujo
			)throws Exception;
	
	public Map<String,Item> endosoCancelacionAuto(
			String cdsisrol
			,String cdramo
			)throws Exception;
	
	public Map<String,String> buscarError(String codigo,String rutaLogs,String archivo) throws Exception;
	
	public void confirmarEndosoCancelacionEndoso(
			String cdusuari
			,String cdsisrol
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdtipsup
			,String nsuplogi
			,String cddevcia
			,String cdgestor
			,Date   feemisio
			,Date   feinival
			,Date   fefinval
			,Date   feefecto
			,Date   feproren
			,String cdmoneda
			,String nmsuplem
			,String cdelemen
			,Date   fechainicio
			,UserVO usuarioSesion
			,FlujoVO flujo
			)throws Exception;
	
	public Map<String,Object> guardarEndosoDevolucionPrimas(
			String cdusuari
			,String cdsisrol
			,String cdelemen
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdtipsup
			,String tstamp
			,Date   feefecto
			,List<Map<String,String>> incisos
			,UserVO usuarioSesion
			,FlujoVO flujo
			,String confirmar
			,String cdperpag
			,String p_plan
			)throws Exception;
	
	public Map<String,Item> endosoCancelacionPolAuto(String cdsisrol, String cdramo) throws Exception;
	
	public Map<String,String> marcarPolizaCancelarPorEndoso(String cdunieco,String cdramo,String nmpoliza)throws Exception;
	
	public Map<String,Object> confirmarEndosoCancelacionPolAuto(
			String cdusuari
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdrazon
			,Date feefecto
			,Date fevencim
			,Date fecancel
			,String cdtipsup
			,UserVO usuarioSesion
			,String cdsisrol
			,FlujoVO flujo
			,String confirmar
			)throws Exception;
	
	public Map<String,Item> endosoValositFormsAuto(
			String cdtipsup
			,String cdsisrol
			,String cdramo
			,List<Map<String,String>> incisos
			)throws Exception;
	
	public void validaEndosoCambioVigencia(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;
	
	public Map<String, String> confirmarEndosoValositFormsAuto(
			String cdusuari
			,String cdsisrol
			,String cdelemen
			,String cdtipsup
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,Date feinival
			,List<Map<String,String>> incisos
			,UserVO usuarioSesion
			,FlujoVO flujo
			,String confirmar
			,String tipoflot
			,String cdperpag
			,String p_plan
			)throws Exception;
	
	public Map<String,Item> confirmarEndosoRehabilitacionPolAuto(String cdsisrol, String cdramo) throws Exception;
	
	public Map<String,String> marcarPolizaParaRehabilitar(String cdunieco,String cdramo,String nmpoliza) throws Exception;
	
	public void confirmarEndosoRehabilitacionPolAuto(
			String cdtipsup
			,String cdunieco
			,String cdramo 
			,String estado
			,String nmpoliza
			,Date feefecto
			,Date feproren
			,Date fecancel
			,Date feinival
			,String cdrazon
			,String cdperson
			,String cdmoneda
			,String nmcancel
			,String comments
			,String nmsuplem
			,UserVO usuarioSesion
			) throws Exception;
	
	public Map<String,String> guardarEndosoAmpliacionVigencia(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String ntramite
			,String cdelemen
			,String cdusuari
			,String cdtipsup
			,String status
			,String fechaEndoso
			,Date dFechaEndoso
			,String feefecto
			,String feproren
			,Date   feprorenOriginal
			,String nmsuplemOriginal
			,UserVO usuarioSesion
			,String tipoGrupoInciso
			,FlujoVO flujo
			,String cdsisrol
			,String confirmar
			,String TIPOFLOT
			,String cdperpag
			,String p_plan
			)throws Exception;
	
	public List<Map<String,String>> obtieneRecibosDespagados(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;
	
	public void guardarEndosoRehabilitacionDespago(
			 String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmrecibo
			,String nmimpres
			,String cdtipsup
			,UserVO usuarioSesion
			,String cdusuari
			,String cdsisrol
			,FlujoVO flujo
			)throws Exception;

	public int guardarEndosoNombreRFCFecha(String cdunieco, String cdramo, String estado, String nmpoliza,
			String cdperson, String cdtipide, String cdideper, String dsnombre, String cdtipper, String otfisjur,
			String otsexo, Date fechaNacimiento, String cdrfc, String dsemail, String dsnombre1, String dsapellido,
			String dsapellido1, String feingreso, String cdnacion, String canaling, String conducto, String ptcumupr,
			String residencia, String nongrata, String cdideext, String cdestciv, String cdsucemi, String cdusuari,
			String cdsisrol, String cdelemen, String cdtipsup, String fechaEndoso, Date dFechaEndoso, String tipoPantalla,
			String codigoCliExt,String sucursalEnt,String ramoEntrada,String polizaEnt, String cdpersonNew, String dsnombreComp,
			String tramite, String numsuplemen, String urlCaratula, UserVO usuarioSesion, FlujoVO flujo)throws Exception;
	
	public int guardarEndosoDomicilioNoSICAPS(String tipoPantalla, String sucursalEnt, String ramoEntrada,
			String polizaEnt, String codigoCliExt, String cdpersonNew, String codigoPostal, String cveEstado,
			String estado, String cveEdoSISG, String cveMinicipio, String municipio, String cveMunSISG,
			String cveColonia, String colonia, String calle, String numExterior, String numInterior, String cdusuari,
			String cdsisrol, String cdelemen, String cdtipsup, String fechaEndoso, Date dFechaEndoso,
			String urlCaratula, String telefono1, String telefono2, String telefono3, UserVO usuarioSesion)throws Exception;
	
	public void sacaEndosoFlujo(FlujoVO flujo) throws Exception;
	
	public void validacionSigsAgente (String cdagente, String cdramo, String cdtipsit, String cdtipend) throws Exception;
	
	public Map<String, String> guardarFechaEfectoEndosoPendiente (String cdunieco, String cdramo, String estado,
			String nmpoliza, String fecha, String cdelemen, String cdusuari, String proceso, String cdtipsup) throws Exception;
	
	public Map<String, String> recuperarDatosEndosoPendiente (String cdunieco, String cdramo, String estado,
			String nmpoliza, String cdtipsup) throws Exception;
	
	public Map<String, Object> endosoAltaAsegurados(String cdunieco, String cdramo, String estado, String nmpoliza,
			String cdusuari, String cdsisrol, String status) throws Exception;
	
	public String guardarAseguradoParaEndosoAlta (
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String cdusuari,
			String cdsisrol,
			String accion,
			String nombre,
			String nombre2,
			String apat,
			String amat,
			String sexo,
			String fenacimi,
			String rfc,
			String nacional,
			String edocivil,
			String feingreso,
			String cdperson,
			String cdtipide,
			String cdideper,
			String cdtipper,
			String dsemail,
			String canaling,
			String conducto,
			String ptcumupr,
			String residencia,
			String nongrata,
			String cdideext,
			String cdsucemi,
			String otfisjur,
			String nmsituac,
			String cdrol,
			String nmorddom,
			String swreclam,
			String swexiper,
			String nmsuplem,
			String nsuplogi,
			String fesolici,
			String feendoso,
			Map<String, String> valosit,
			String usuarioCaptura
			) throws Exception;
	
	public List<Map<String, String>> tarificarEndosoAltaAsegurados (String cdusuari, String cdelemen, String cdunieco,
			String cdramo, String estado, String nmpoliza, String nmsuplem, String feinival) throws Exception;
	
	public Map<String, String> confirmarEndosoSaludFlujo (String cdusuari, String cdsisrol, String cdelemen,
			String ntramite, String cdunieco, String cdramo, String estado, String nmpoliza,
			String status, String nmsuplem, String nsuplogi, Date fesolici, Date feinival, boolean autoriza,
			String cdtipsup, UserVO usuario) throws Exception;
	
	public Map<String, Object> endosoCoberturasFlujo (String cdunieco, String cdramo, String estado, String nmpoliza,
			String cdusuari, String cdsisrol, String status) throws Exception;
	
	public void agregarCoberturaEndosoCoberturas (String cdunieco, String cdramo, String estado, String nmpoliza,
			String nmsituac, String cdgarant, String nmsuplem, String cdatribu1, String otvalor1,
			String cdatribu2, String otvalor2, String cdatribu3, String otvalor3, String cdtipsit) throws Exception;
	
	public void quitarCoberturaAgregadaEndCob (String cdunieco, String cdramo, String estado, String nmpoliza,
			String nmsituac, String cdgarant, String nmsuplem, String cdatribu1,
			String cdatribu2, String cdatribu3, String cdtipsit) throws Exception;
	
	public void eliminarCoberturaEndosoCoberturas (String cdunieco, String cdramo, String estado, String nmpoliza,
			String nmsituac, String cdgarant, String nmsuplem, String cdtipsit) throws Exception;
	
	public void restaurarCoberturaEliminadaEndCob (String cdunieco, String cdramo, String estado, String nmpoliza,
			String nmsituac, String cdgarant, String nmsuplem, String cdatribu1,
			String cdatribu2, String cdatribu3, String cdtipsit) throws Exception;
	
	public List<Map<String, String>> tarificarEndosoCoberturasFlujo (String cdusuari, String cdelemen, String cdunieco,
			String cdramo, String estado, String nmpoliza, String nmsuplem, String feinival, String cdtipsup) throws Exception;
	
	public Map<String,Item> pantallaBeneficiariosAutoVida(String cdunieco,String cdramo,String estado,String cdsisrol,String cdtipsup)throws Exception;
	
   public List<Map<String,String>> obtieneBeneficiariosVidaAuto(
	            String cdunieco
	            ,String cdramo
	            ,String estado
	            ,String nmpoliza
	            )throws Exception;
   
   public ManagerRespuestaVoidVO confirmaEndosoBeneficiariosVidaAuto(
           String cdtipsup
           ,String tstamp
           ,String cdunieco
           ,String cdramo
           ,String estado
           ,String nmpoliza
           ,String nmsituac
           ,String feefecto
           ,String cdusuari
           ,String cdsisrol
           ,String cdelemen
           ,UserVO usuarioSesion
           ,List<Map<String,String>> incisos
           ,FlujoVO flujo
           )throws Exception;
   
   public Map<String,Item> endosoAjusteSiniestralidad(
           String cdtipsup
           ,String cdramo
           )throws Exception;
   
   public Map<String,Object> guardarEndosoAjusteSiniestralidad(
           String cdusuari
           ,String cdsisrol
           ,String cdelemen
           ,String cdunieco
           ,String cdramo
           ,String estado
           ,String nmpoliza
           ,String cdtipsup
           ,String tstamp
           ,Date   feefecto
           ,List<Map<String,String>> incisos
           ,UserVO usuarioSesion
           ,FlujoVO flujo
           ,String nmtramite
           )throws Exception;
   
}