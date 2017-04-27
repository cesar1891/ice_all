/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.gseguros.portal.cotizacion.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionContext;

import mx.com.aon.configurador.pantallas.model.components.GridVO;
import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.kernel.service.KernelManagerSustituto;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.externo.service.StoredProceduresManager;
import mx.com.gseguros.mesacontrol.model.FlujoVO;
import mx.com.gseguros.mesacontrol.service.FlujoMesaControlManager;
import mx.com.gseguros.portal.consultas.dao.ConsultasDAO;
import mx.com.gseguros.portal.consultas.model.PolizaAseguradoVO;
import mx.com.gseguros.portal.consultas.model.PolizaDTO;
import mx.com.gseguros.portal.consultas.service.ConsultasManager;
import mx.com.gseguros.portal.consultas.service.ConsultasPolizaManager;
import mx.com.gseguros.portal.cotizacion.model.DatosUsuario;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.cotizacion.service.CotizacionManager;
import mx.com.gseguros.portal.despachador.model.RespuestaTurnadoVO;
import mx.com.gseguros.portal.despachador.service.DespachadorManager;
import mx.com.gseguros.portal.documentos.model.Documento;
import mx.com.gseguros.portal.documentos.service.DocumentosManager;
import mx.com.gseguros.portal.emision.service.EmisionManager;
import mx.com.gseguros.portal.endosos.service.EndososManager;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.service.CatalogosManager;
import mx.com.gseguros.portal.general.service.PantallasManager;
import mx.com.gseguros.portal.general.service.ServiciosManager;
import mx.com.gseguros.portal.general.util.EstatusTramite;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.portal.general.util.ObjetoBD;
import mx.com.gseguros.portal.general.util.Ramo;
import mx.com.gseguros.portal.general.util.Rango;
import mx.com.gseguros.portal.general.util.RolSistema;
import mx.com.gseguros.portal.general.util.TipoEndoso;
import mx.com.gseguros.portal.general.util.TipoSituacion;
import mx.com.gseguros.portal.general.util.TipoTramite;
import mx.com.gseguros.portal.general.util.Validacion;
import mx.com.gseguros.portal.mesacontrol.service.MesaControlManager;
import mx.com.gseguros.portal.siniestros.service.SiniestrosManager;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.HttpUtil;
import mx.com.gseguros.utils.Utils;
import mx.com.gseguros.ws.autosgs.emision.model.EmisionAutosVO;
import mx.com.gseguros.ws.autosgs.service.EmisionAutosService;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteGeneral;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteGeneralRespuesta;
import mx.com.gseguros.ws.ice2sigs.service.Ice2sigsService;
import mx.com.gseguros.ws.recibossigs.service.RecibosSigsService;

/**
 * 
 * @author Jair
 */
@SuppressWarnings("deprecation")
public class ComplementariosAction extends PrincipalCoreAction
{

	private static final long serialVersionUID = -1269892388621564059L;
	private final static Logger logger = LoggerFactory.getLogger(ComplementariosAction.class);
	private Item items;
	private Item fields;
	
	private KernelManagerSustituto kernelManager;
	private transient Ice2sigsService ice2sigsService;
	private transient RecibosSigsService recibosSigsService;
	private CatalogosManager catalogosManager;
	private StoredProceduresManager storedProceduresManager;
	
	private Map<String, String> panel1;
	private Map<String, String> panel2;
	private Map<String, String> parametros;
	private String cdunieco;
	private String sucursalGS;
	private String cdRamoGS;
	private String cdramo;
	private String estado;
	private String nmpoliza;
	private String nmpolAlt;
	private String nmsuplem;
	private String cdIdeper;
	private String cdtipsit;
	private boolean success = true;
	private boolean retryWS;
	private boolean retryRec;
	private ScreenInterceptor scrInt = new ScreenInterceptor();
	private static SimpleDateFormat renderFechas = new SimpleDateFormat("dd/MM/yyyy");
	private Calendar calendarHoy = Calendar.getInstance();
	private Item item1;
	private Item item2;
	private Item item3;
	private Item item4;
	private Item item5;
	private Map<String, String> map1;
	private Map<String, String> map2;
	private Map<String, String> map3;
	private Map<String, String> map4;
	private List<Map<String,Object>> list1;
	private List<Map<String,Object>> list2;
	private List<Map<String,Object>> list3;
	private List<Map<String,Object>> list4;
	private Map<String,Object> omap1;
	private String cdperson;
	private List<Map<String,String>>slist1;
	private GridVO gridResultados;
	private String mensajeRespuesta;
	private String auxiliarProductoCdramo   = null;
	private String auxiliarProductoCdtipsit = null;
	private String tipoGrupoInciso;
	private FlujoVO flujo;
	
	private ConsultasManager consultasManager;
	private boolean exito = false;
	private PantallasManager pantallasManager;
	private EmisionAutosService emisionAutosService;
	private boolean clienteWS;
	private String mensajeEmail;
	private String respuesta;
	private String respuestaOculta;
	private CotizacionManager cotizacionManager;
	private EmisionManager    emisionManager;
	
	private String message;
	private Map<String, String> params;
	
	@Autowired
	private ConsultasPolizaManager consultasPolizaManager;
	
	@Autowired
	private ServiciosManager serviciosManager;
	
	@Autowired
	private DocumentosManager documentosManager;
	
	@Autowired
	private MesaControlManager mesaControlManager;
	
	@Autowired
	private FlujoMesaControlManager flujoMesaControlManager;
	
	@Autowired
	private EndososManager endososManager;
	
	@Autowired
	private SiniestrosManager  siniestrosManager;
    
    @Autowired
    private ConsultasDAO consultasDAO;
	
	@Autowired
	private DespachadorManager despachadorManager;
	
	public static final String SUCURSAL_SALUD_NOVA = "1403";

	@Value("${ruta.servidor.reports}")
    private String rutaServidorReports;
	
	@Value("${pass.servidor.reports}")
    private String passServidorReports;	
	
	@Value("${ruta.documentos.poliza}")
    private String rutaDocumentosPoliza;
	
	@Value("${recibo.impresion.autos.url}")
    private String reciboImpresionAutosUrl;

	@Value("${caic.impresion.autos.url}")
    private String caicImpresionAutosUrl;	
	
	@Value("${aeua.impresion.autos.url}")
    private String aeuaImpresionAutosUrl;	
	
	@Value("${ap.impresion.autos.url}")
    private String apImpresionAutosUrl;

	@Value("${incisos.flotillas.impresion.autos.url}")
    private String incisosFlotillasImpresionAutosUrl;			

	@Value("${incisos.flotillas.excel.impresion.autos.url}")
	private String incisosFlotillasExcelImpresionAutosUrl;			
				
	@Value("${tarjeta.iden.impresion.autos.url}")
    private String tarjetaIdenImpresionAutosUrl;				
					
	@Value("${numero.incisos.reporte}")
    private String numeroIncisosReporte;	
	
	@Value("${caratula.impresion.autos.url}")
    private String caratulaImpresionAutosUrl;
	
	@Value("${caratula.impresion.autos.serviciopublico.url}")
    private String caratulaImpresionAutosServiciopublicoUrl;
	
	@Value("${caratula.impresion.autos.flotillas.url}")
    private String caratulaImpresionAutosFlotillasUrl;
	
	@Value("${manual.agente.txtinfocobredgs}")
    private String manualAgenteTxtinfocobredgs;
	
	@Value("${manual.agente.condgralescobsegvida}")
    private String manualAgenteCondgralescobsegvida;
	
	@Value("${manual.agente.txtinfocobgesgs}")
    private String manualAgenteTxtinfocobgesgs;
	
	@Value("${caratula.impresion.autos.docextra.url}")
    private String caratulaImpresionAutosDocExtra;
	
	public ComplementariosAction() {
		this.session=ActionContext.getContext().getSession();
	}
	
	@SuppressWarnings("deprecation")
	public String mostrarPantalla()
	{
		logger.debug(
				new StringBuilder()
				.append("\n###################################################")
				.append("\n###### ComplementariosAction mostrarPantalla ######")
				.append("\nmap1: ").append(map1)
				.append("\ncdunieco: ").append(cdunieco)
				.append("\ncdramo: ").append(cdramo)
				.append("\nestado: ").append(estado)
				.append("\nnmpoliza: ").append(nmpoliza)
				.append("\ncdtipsit: ").append(cdtipsit)
				.append("\nflujo: ").append(flujo)
				.toString()
				);
		
		success         = true;
		exito           = true;
		String result   = SUCCESS;
		UserVO usuario  = null;
		String cdsisrol = null;
		String caseIdRstn = null;
		
		if (exito && map1 != null && "S".equals(map1.get("rstn")) && StringUtils.isNotBlank(map1.get("ntramite"))) { // para RSTN recuperar datos
		    try {
		        String paso = "Construyendo flujo RSTN";
		        try {
		            UserVO user = Utils.validateSession(session);
		            String ntramite = map1.get("ntramite");
		            Utils.validate(ntramite, "Falta ntramite");
		            flujo = flujoMesaControlManager.generarYRecuperarFlujoRSTN(ntramite, user.getUser(), user.getRolActivo().getClave());
		            caseIdRstn = map1.get("caseIdRstn");
		            map1 = null;
		        } catch (Exception ex) {
		            Utils.generaExcepcion(ex, paso);
		        }
		    } catch (Exception ex) {
		        exito = false;
		        respuesta = Utils.manejaExcepcion(ex);
		    }
		}
		
		if(exito)
		{
			if(flujo!=null)
			{
				try
				{
				    map4=consultasPolizaManager.obtieneTvalopol(cdunieco, cdramo, estado, nmpoliza);
				    
					Map<String,Object> datosTramite = flujoMesaControlManager.recuperarDatosTramiteValidacionCliente(flujo);
					Map<String,String> tramite      = (Map<String,String>)datosTramite.get("TRAMITE");
					cdunieco = flujo.getCdunieco();
					cdramo   = flujo.getCdramo();
					estado   = flujo.getEstado();
					nmpoliza = tramite.get("NMSOLICI");
					cdtipsit = tramite.get("CDTIPSIT");
					map1 = new HashMap<String, String>();
					map1.put("ntramite" , flujo.getNtramite());
					
					map1.put("caseIdRstn", caseIdRstn);
					
					if("RECUPERAR".equals(flujo.getAux()))
					{
						//se recibieron 3 propiedades de una pantalla anterior, hay que actualizarlas
						logger.debug("flujo antes de actualizar sus 3 propiedades de pantalla nueva={}",flujo);
						flujoMesaControlManager.recuperarPropiedadesDePantallaComponenteActualPorConexionSinPermisos(flujo);
						
						logger.debug("flujo despues de actualizar sus 3 propiedades de pantalla nueva={}",flujo);
					}
					
					map1.put("cdpercli" , consultasManager.recuperarCdpersonClienteTramite(flujo.getNtramite()));
				}
				catch(Exception ex)
				{
					long timestamp  = System.currentTimeMillis();
					exito           = false;
					respuesta       = "Error al procesar flujo #"+timestamp;
					respuestaOculta = ex.getMessage();
					logger.error(respuesta,ex);
				}
			}
		}
		
		//revisar datos
		if(exito)
		{
			try
			{
				if(session==null
						||session.get("USUARIO")==null
						||StringUtils.isBlank(cdunieco)
						||StringUtils.isBlank(cdramo)
						||StringUtils.isBlank(estado)
						||StringUtils.isBlank(nmpoliza)
						||StringUtils.isBlank(cdtipsit)
						)
				{
					throw new Exception("Faltan datos (sesion o cdunieco o cdramo o estado o npoliza o cdtipsit)");
				}
				else
				{
				    map4=consultasPolizaManager.obtieneTvalopol(cdunieco, cdramo, estado, nmpoliza);
                    
					usuario  = (UserVO)session.get("USUARIO");
					cdsisrol = usuario.getRolActivo().getClave();
					
					if(map1==null)
					{
						map1=new LinkedHashMap<String,String>(0);
					}
					
					map1.put("sesiondsrol",cdsisrol);
				}
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = "No se recibieron datos necesarios #"+timestamp;
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
        
        //obtener tipo situacion
		if(exito)
		{
	        try
	        {
	            Map<String,String>tipoSituacion=cotizacionManager.cargarTipoSituacion(cdramo,cdtipsit);
	            if(tipoSituacion!=null)
	            {
	            	map1.putAll(tipoSituacion);
	            }
	            else
	            {
	            	throw new Exception("No se ha parametrizado la situacion en ttipram");
	            }
	        }
	        catch(Exception ex)
	        {
	        	long timestamp  = System.currentTimeMillis();
	        	respuesta       = "Error al cargar tipo de situacion #"+timestamp;
	        	respuestaOculta = ex.getMessage();
	        	logger.error(respuesta,ex);
	        	
	        	this.addActionError("No se ha parametrizado el tipo de situaci\u00f3n para el producto #"+timestamp);
	        	map1.put("SITUACION"  , "PERSONA");
	        	map1.put("AGRUPACION" , "SOLO");
	        }
		}
		
		//cargar campos dinamicos
		if(exito)
		{
			try
			{
				List<ComponenteVO>listaTatrisit = kernelManager.obtenerTatripol(new String[]{cdramo,cdtipsit,"I"});
				GeneradorCampos gc              = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
				
				gc.setCdramo(cdramo);
				gc.genera(listaTatrisit);
				
				items  = gc.getItems();
				fields = gc.getFields();

				fields.add(Item.crear(null, null, Item.OBJ).add("name", "panel1.dsciaaseg"));
				fields.add(Item.crear(null, null, Item.OBJ).add("name", "panel1.nombreagente"));
				fields.add(Item.crear(null, null, Item.OBJ).add("name", "panel1.dsramo"));
				fields.add(Item.crear(null, null, Item.OBJ).add("name", "panel2.nmpoliza"));
				fields.add(Item.crear(null, null, Item.OBJ).add("name", "panel2.estado"));
				fields.add(Item.crear(null, null, Item.OBJ).add("name", "panel2.fesolici"));
				fields.add(Item.crear(null, null, Item.OBJ).add("name", "panel2.solici"));
				fields.add(Item.crear(null, null, Item.OBJ).add("name", "panel2.feefec"));
				fields.add(Item.crear(null, null, Item.OBJ).add("name", "panel2.ferenova"));
				fields.add(Item.crear(null, null, Item.OBJ).add("name", "panel2.cdtipopol"));
				fields.add(Item.crear(null, null, Item.OBJ).add("name", "panel2.cdperpag"));
				fields.add(Item.crear(null, null, Item.OBJ).add("name", "panel2.dsplan"));
				fields.add(Item.crear(null, null, Item.OBJ).add("name", "panel2.nmcuadro"));
				fields.add(Item.crear(null, null, Item.OBJ).add("name", "panel2.nmrenova"));
				fields.add(Item.crear(null, null, Item.OBJ).add("name", "panel2.nmpolant"));
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = "Error al cargar la pantalla #"+timestamp;
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		//retroactividad
		if(exito)
		{
			try
			{
				LinkedHashMap<String,Object>paramsRetroactividad = new LinkedHashMap<String,Object>();
				paramsRetroactividad.put("param1" , cdunieco);
				paramsRetroactividad.put("param2" , cdramo);
				paramsRetroactividad.put("param3" , TipoEndoso.EMISION_POLIZA.getCdTipSup()+"");
				paramsRetroactividad.put("param4" , usuario.getUser());
				paramsRetroactividad.put("param5" , cdtipsit);
				Map<String,String>retroactividad=storedProceduresManager.procedureMapCall(
						ObjetoBD.OBTIENE_RETROACTIVIDAD_TIPSUP.getNombre(), paramsRetroactividad, null);
				
				int retroac=Integer.valueOf(retroactividad.get("RETROAC"));
				int diferi =Integer.valueOf(retroactividad.get("DIFERI"));
				
				Calendar calendarMin=Calendar.getInstance();
				Calendar calendarMax=Calendar.getInstance();
				
				calendarMin.add(Calendar.DAY_OF_YEAR, retroac*-1);
				calendarMax.add(Calendar.DAY_OF_YEAR, diferi);
				
				map1.put("fechamin" , renderFechas.format(calendarMin.getTime()));
				map1.put("fechamax" , renderFechas.format(calendarMax.getTime()));
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				respuesta       = "Error al obtener retroactividad #"+timestamp;
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
				
				this.addActionError("No se ha definido la retroactividad para el producto #"+timestamp);
				map1.put("fechamin",renderFechas.format(new Date()));
				map1.put("fechamax",renderFechas.format(new Date()));
			}
		}
		
		//url pantalla asegurados
		if(exito)
		{
			try
			{
				List<ComponenteVO>listaAux=pantallasManager.obtenerComponentes(
						null
						,null
						,cdramo
						,cdtipsit
						,null
						,cdsisrol
						,"PANTALLA_EMISION"
						,"URL_ASEGURADOS"
						,null);
				if(listaAux==null
						||listaAux.size()==0)
				{
					throw new Exception("No se encuentra la url de pantalla de asegurados");
				}
				else if(listaAux.size()>1)
				{
					throw new Exception("Url repetida para pantalla de asegurados");
				}
				else
				{
					ComponenteVO url=listaAux.get(0);
					map1.put("urlAsegurados",url.getNameCdatribu());
				}
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				respuesta       = "Error al obtener la url de la pesta&ntilde;a de asegurados #"+timestamp;
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
				
				this.addActionError("No se ha definido la url de la pesta�a de asegurados #"+timestamp);
				map1.put("urlAsegurados","");
			}
		}
		
		if(exito)
		{
			try
			{
				map1.put("cambioCuadro",cotizacionManager.cargarBanderaCambioCuadroPorProducto(cdramo)?"S":"N");
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = "Error al cargar parametrizacion de cuadro de comisiones pantalla #"+timestamp;
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		if(exito)
		{
			try
			{
				map1.put("customCode" , consultasManager.recuperarCodigoCustom("58", cdsisrol));
			}
			catch(Exception ex)
			{
				map1.put("customCode" , "/* error */");
				logger.error("Error sin impacto funcional",ex);
			}
		}
		
		if(!exito)
		{
			result = ERROR;
		}
		
		logger.debug(
				new StringBuilder()
				.append("\nresult: ").append(result)
				.append("\n###### ComplementariosAction mostrarPantalla ######")
				.append("\n###################################################")
				.toString()
				);
		return result;
	}

	/*
	 * json
	 * 
	 * @out success
	 */
	@SuppressWarnings("rawtypes")
	public String cargar() {
		UserVO usuarioSesion = (UserVO) session.get("USUARIO");
		panel1 = new HashMap<String, String>(0);
		panel2 = new HashMap<String, String>(0);
		try {
			// ////////////////
			// //// duro //////
			/*
			 * //////////////* panel1.put("dsciaaseg","#General de seguros");
			 * panel1.put("dsramo","#Salud Vital");
			 * panel1.put("nombreagente","#Alvaro");
			 * panel2.put("nmpoliza",nmpoliza); panel2.put("estado","#W");
			 * panel2.put("fesolici","06/09/1969");
			 * panel2.put("solici","696969"); panel2.put("feefec","06/09/1969");
			 * panel2.put("ferenova","06/09/1969");
			 * panel2.put("cdtipopol","#R"); panel2.put("cdperpag","#12"); /
			 */// ///////////*/
				// //// duro //////
				// ////////////////

			/////////////////////////////////////
			////// Cargar info de mpolizas //////
			/*/////////////////////////////////*/
			Map<String, Object> parameters = new HashMap<String, Object>(0);
			parameters.put("pv_cdunieco", cdunieco);
			parameters.put("pv_cdramo", cdramo);
			parameters.put("pv_estado", estado);
			parameters.put("pv_nmpoliza", nmpoliza);
			parameters.put("pv_cdusuari", usuarioSesion.getUser());
			
			Map<String, Object> select = kernelManager
					.getInfoMpolizas(parameters);
			panel1.put("dsciaaseg", (String) select.get("dsunieco"));
			panel1.put("dsramo", (String) select.get("dsramo"));
			panel1.put("nombreagente", (String) select.get("nombre"));
			panel2.put("nmpoliza", nmpoliza);
			panel2.put("estado", estado);
			panel2.put("fesolici",
					renderFechas.format((Date) select.get("fesolici")));
			panel2.put("solici", (String) select.get("nmsolici"));
			panel2.put("feefec",
					renderFechas.format((Date) select.get("feefecto")));
			panel2.put("ferenova",
					renderFechas.format((Date) select.get("feproren")));
			panel2.put("cdtipopol", (String) select.get("ottempot"));
			panel2.put("cdperpag", (String) select.get("cdperpag"));
			panel2.put("dsplan", (String) select.get("dsplan"));
			panel2.put("nmcuadro" , (String) select.get("nmcuadro"));
			panel2.put("nmrenova", (String) select.get("nmrenova"));
			panel2.put("nmpolant", (String) select.get("nmpolant"));
			/*/////////////////////////////////*/
			////// Cargar info de mpolizas //////
			/////////////////////////////////////
			
			/////////////////////////////////
			//////   cargar tvalopol   //////
			////// can throw exception //////
			/*/////////////////////////////*/
			try
			{
				Map<String,String> paramGetValopol=new HashMap<String,String>(0);
				paramGetValopol.put("pv_cdunieco",cdunieco);
				paramGetValopol.put("pv_cdramo",cdramo);
				paramGetValopol.put("pv_estado",estado);
				paramGetValopol.put("pv_nmpoliza",nmpoliza);
				parametros=new HashMap<String,String>(0);
				Map<String,Object>parametrosCargados=kernelManager.pGetTvalopol(paramGetValopol);
				Iterator it=parametrosCargados.entrySet().iterator();
				while(it.hasNext())
				{
					Entry<String,Object> entry=(Map.Entry<String, Object>) it.next();
					parametros.put("pv_"+entry.getKey(), (String)entry.getValue());
				}
				logger.debug("panel1: "+panel1);
				logger.debug("panel2: "+panel2);
				logger.debug("parametros: "+parametros);
			}
			catch(Exception ex)
			{
				logger.error("No hubo valopol X(");
				parametros=null;
			}
			/*/////////////////////////////*/
			//////   cargar tvalopol   //////
			////// can throw exception //////
			/////////////////////////////////
			
			success = true;
		} catch (Exception ex) {
			panel1 = new HashMap<String, String>(0);
			panel2 = new HashMap<String, String>(0);
			logger.error("error al obtener los datos de mpolizas", ex);
			success = false;
		}
		return SUCCESS;
	}

	public String guardar() {
		try
		{
			logger.debug("### action:");
			logger.debug("panel1: "+panel1);
			logger.debug("panel2: "+panel2);
			logger.debug("map1: "+map1);
			logger.debug("parametros: "+parametros);
			
			UserVO usuarioSesion=(UserVO) session.get("USUARIO");
			
			map1.put("pv_cdusuari",usuarioSesion.getUser());
			Map<String,Object> anterior=kernelManager.getInfoMpolizasCompleta(map1);
			logger.debug("anterior: "+anterior);
			/*String columnasLeidas[]=new String[]{"status","swestado","nmsolici","feautori","cdmotanu","feanulac",
    				"swautori","cdmoneda","feinisus","fefinsus",
    	            "ottempot","feefecto","hhefecto","feproren","fevencim","nmrenova","ferecibo","feultsin","nmnumsin","cdtipcoa",
    	            "swtarifi","swabrido","feemisio","cdperpag","nmpoliex","nmcuadro","porredau","swconsol","nmpolant","nmpolnva",
    	            "fesolici","cdramant","cdmejred","nmpoldoc","nmpoliza2","nmrenove","nmsuplee","ttipcamc","ttipcamv","swpatent"};
    	            
    	            
    	            {pv_nmrenove=null, pv_fesolici=09/09/2013, pv_swconsol=S, pv_porredau=100, pv_swtarifi=A,
    	            pv_nmsolici=6969, pv_feautori=null, pv_nmcuadro=VB, pv_nmrenova=0, pv_cdunieco=1, pv_nmpoliza2=null,
    	            pv_feefecto=09/09/2013, pv_hhefecto=12:00, pv_fevencim=null, pv_status=V, pv_feproren=09/09/2014,
    	            pv_feinisus=null, pv_nmpoldoc=null, pv_cdmoneda=001, pv_nmpolnva=null, pv_cdmotanu=null, pv_nmpolant=null,
    	            pv_ottempot=R, pv_ttipcamc=null, pv_estado=W, pv_swautori=N, pv_nmpoliza=1237, pv_accion=U, pv_nmsuplee=null,
    	            pv_swestado=0, pv_cdtipcoa=N, pv_ttipcamv=null, pv_feultsin=null, pv_nmsuplem=0, pv_cdperpag=12, pv_cdramant=null,
    	            pv_feemisio=2013-09-09 00:00:00.0, pv_swpatent=null, pv_nmpoliex=null, pv_fefinsus=null, pv_nmnumsin=0,
    	            pv_ferecibo=null, pv_swabrido=null, pv_cdmejred=null, pv_cdramo=2, pv_feanulac=null}
    	            
    	            
    	            */
			Map<String,String> nuevo=new HashMap<String,String>(0);
            nuevo.put("pv_cdunieco",     map1.get("pv_cdunieco"));
            nuevo.put("pv_cdramo",       map1.get("pv_cdramo"));
            nuevo.put("pv_estado",       panel2.get("estado"));
            nuevo.put("pv_nmpoliza",     panel2.get("nmpoliza"));
            nuevo.put("pv_nmsuplem",     "0");
            nuevo.put("pv_status",       (String)anterior.get("status"));
            nuevo.put("pv_swestado",     (String)anterior.get("swestado"));
            nuevo.put("pv_nmsolici",     panel2.get("solici"));
            nuevo.put("pv_feautori",     anterior.get("feautori")!=null?renderFechas.format(anterior.get("feautori")):null);
            nuevo.put("pv_cdmotanu",     (String)anterior.get("cdmotanu"));
            nuevo.put("pv_feanulac",     anterior.get("feanulac")!=null?renderFechas.format(anterior.get("feanulac")):null);
            nuevo.put("pv_swautori",     (String)anterior.get("swautori"));
            nuevo.put("pv_cdmoneda",     (String)anterior.get("cdmoneda"));
            nuevo.put("pv_feinisus",     anterior.get("feinisus")!=null?renderFechas.format(anterior.get("feinisus")):null);
            nuevo.put("pv_fefinsus",     anterior.get("fefinsus")!=null?renderFechas.format(anterior.get("fefinsus")):null);
            nuevo.put("pv_ottempot",     panel2.get("cdtipopol"));
            nuevo.put("pv_feefecto",     panel2.get("feefec"));//renderFechas.format(calendarHoy.getTime()));
            nuevo.put("pv_hhefecto",     (String)anterior.get("hhefecto"));
            nuevo.put("pv_feproren",     panel2.get("ferenova"));//renderFechas.format(fechaEnUnAnio.getTime()));
            nuevo.put("pv_fevencim",     anterior.get("fevencim")!=null?renderFechas.format(anterior.get("fevencim")):null);
//            nuevo.put("pv_nmrenova",     (String) (panel2.get("nmrenova")==null?"0":panel2.get("nmrenova"))); 
            nuevo.put("pv_nmrenova",     (String) (panel2.get("nmrenova")==null?"0":panel2.get("nmrenova"))); 
            nuevo.put("pv_ferecibo",     anterior.get("ferecibo")!=null?renderFechas.format(anterior.get("ferecibo")):null);
            nuevo.put("pv_feultsin",     anterior.get("feultsin")!=null?renderFechas.format(anterior.get("feultsin")):null);
            nuevo.put("pv_nmnumsin",     (String)anterior.get("nmnumsin"));
            nuevo.put("pv_cdtipcoa",     (String)anterior.get("cdtipcoa"));
            nuevo.put("pv_swtarifi",     (String)anterior.get("swtarifi"));
            nuevo.put("pv_swabrido",     (String)anterior.get("swabrido"));
            nuevo.put("pv_feemisio",     anterior.get("feemisio")!=null?renderFechas.format(anterior.get("feemisio")):null);
            nuevo.put("pv_cdperpag",     panel2.get("cdperpag"));
            nuevo.put("pv_nmpoliex",     (String)anterior.get("nmpoliex"));
            nuevo.put("pv_nmcuadro",     StringUtils.isNotBlank(panel2.get("nmcuadro"))?
            		panel2.get("nmcuadro")
            		:(String)anterior.get("nmcuadro"));
            nuevo.put("pv_porredau",     (String)anterior.get("porredau"));
            nuevo.put("pv_swconsol",     (String)anterior.get("swconsol"));
//            nuevo.put("pv_nmpolant",     panel2.get("nmpolant")==null?"":panel2.get("nmpolant"));//  // Se agrega TextField
            nuevo.put("pv_nmpolant",     (String) (panel2.get("nmpolant")==null?null:panel2.get("nmpolant")));//  // Se agrega TextField
            nuevo.put("pv_nmpolnva",     (String)anterior.get("nmpolnva"));
            nuevo.put("pv_fesolici",     panel2.get("fesolici"));
            nuevo.put("pv_cdramant",     (String)anterior.get("cdramant"));
            nuevo.put("pv_cdmejred",     (String)anterior.get("cdmejred"));
            nuevo.put("pv_nmpoldoc",     (String)anterior.get("nmpoldoc"));
            nuevo.put("pv_nmpoliza2",    (String)anterior.get("nmpoliza2"));
            nuevo.put("pv_nmrenove",     (String)anterior.get("nmrenove"));
            nuevo.put("pv_nmsuplee",     (String)anterior.get("nmsuplee"));
            nuevo.put("pv_ttipcamc",     (String)anterior.get("ttipcamc"));
            nuevo.put("pv_ttipcamv",     (String)anterior.get("ttipcamv"));
            nuevo.put("pv_swpatent",     (String)anterior.get("swpatent"));
            nuevo.put("pv_pcpgocte",     "100");
            nuevo.put("pv_tipoflot",     null);
            nuevo.put("pv_agrupador",    null);
            nuevo.put("pv_accion",       "U");
            kernelManager.insertaMaestroPolizas(nuevo);
            
            if(StringUtils.isNotBlank(panel2.get("nmcuadro")))
            {
	            Map<String,Object>paramsPoliage=new LinkedHashMap<String,Object>();
	            paramsPoliage.put("pv_cdunieco_i" , map1.get("pv_cdunieco"));
	            paramsPoliage.put("pv_cdramo_i"   , map1.get("pv_cdramo"));
	            paramsPoliage.put("pv_estado_i"   , panel2.get("estado"));
	            paramsPoliage.put("pv_nmpoliza_i" , panel2.get("nmpoliza"));
	            paramsPoliage.put("pv_nmsuplem_i" , "0");
	            paramsPoliage.put("pv_status_i"   , "V");
	            paramsPoliage.put("pv_nmcuadro_i" , panel2.get("nmcuadro"));
	            emisionManager.getActualizaCuadroComision(paramsPoliage);
            }
            
            if(parametros==null){
            	parametros = new HashMap<String, String>();
            }
            parametros.putAll(map1);
            parametros.put("pv_status", "V");
            parametros.put("pv_nmsuplem", "0");
            
            if (!Ramo.GASTOS_MEDICOS_MAYORES_PRUEBA.getCdramo().equals(map1.get("pv_cdramo"))) {
                kernelManager.pMovTvalopol(parametros);
            }
            
			success = true;
		}
		catch(Exception ex)
		{
			logger.error("error al guardar la poliza",ex);
			success=false;
		}
		return SUCCESS;
	}

	public String pantallaAsegurados() {
		logger.debug(Utils.log(
				 "\n################################"
				,"\n###### pantallaAsegurados ######"
				));
		logger.debug("map1: "+map1);
		if(map1!=null)
		{
			if(map1.get("cdtipsit")!=null)
			{
				auxiliarProductoCdtipsit = map1.get("cdtipsit");
			}
			if(map1.get("cdramo")!=null)
			{
				auxiliarProductoCdramo = map1.get("cdramo");
			}
		}
		return scrInt.intercept(this,
				ScreenInterceptor.PANTALLA_COMPLEMENTARIOS_ASEGURADOS);
	}

	public String mostrarPantallaAsegurados() {
		try {
			item1 = new Item("fields", null, Item.ARR);// para los atributos del
														// modelo usuario
			item1.add(Item.crear(null, null, Item.OBJ).add(new Item("name", "nmsituac")));
			item1.add(Item.crear(null, null, Item.OBJ)
					.add(Item.crear("name", "cdrol"))
					.add(Item.crear("type", "Generic"))
					);
			item1.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("name", "fenacimi"))
					.add(new Item("type", "date"))
					.add(new Item("dateFormat", "d/m/Y"))
					);
			item1.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("name", "sexo"))
					.add(new Item("type", "Generic"))
					);
			item1.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("name", "tpersona"))
					.add(new Item("type", "Generic"))
					);
			item1.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("name", "nacional"))
					.add(new Item("type", "Generic"))
					);
			item1.add(Item.crear(null, null, Item.OBJ).add(new Item("name", "cdperson")));
			item1.add(Item.crear(null, null, Item.OBJ).add(new Item("name", "nombre")));
			item1.add(Item.crear(null, null, Item.OBJ).add(new Item("name", "segundo_nombre")));
			item1.add(Item.crear(null, null, Item.OBJ).add(new Item("name", "Apellido_Paterno")));
			item1.add(Item.crear(null, null, Item.OBJ).add(new Item("name", "Apellido_Materno")));
			item1.add(Item.crear(null, null, Item.OBJ).add(new Item("name", "cdrfc")));
			item1.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("name", "estomador"))
					.add(new Item("type", "boolean"))
					);
			item1.add(Item.crear(null, null, Item.OBJ).add(new Item("name", "Parentesco")));
			item1.add(Item.crear(null, null, Item.OBJ).add(new Item("name", "swexiper")));
			item1.add(Item.crear(null, null, Item.OBJ).add(new Item("name", "cdideper")));
			item1.add(Item.crear(null, null, Item.OBJ).add(new Item("name", "cdideext")));
			item1.add(Item.crear(null, null, Item.OBJ).add(new Item("name", "cdestciv")));//Estado Civil del asegurado
			item1.add(Item.crear(null, null, Item.OBJ).add(new Item("name", "numsoc")));//Numero de socio
			item1.add(Item.crear(null, null, Item.OBJ).add(new Item("name", "clvfam")));//Clave familiar
			item1.add(Item.crear(null, null, Item.OBJ).add(new Item("name", "ocup")));//Ocupacion del asegurado
			
			logger.debug("Modelo armado para persona: "+item1.toString());
			/*
			nmsituac
    		cdrol
    		fenacimi
    		sexo
    		cdperson
    		nombre
    		segundo_nombre
    		Apellido_Paterno
    		Apellido_Materno
    		cdrfc*/

			item3 = new Item("columns", null, Item.ARR);// para las columnas del grid
			item3.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "Nombre"))
					.add(new Item("dataIndex", "nombre"))
					.add(new Item("width", 100))
					//.add(Item.crear("editor",null,Item.OBJ)
					//		.add("xtype","textfield")
					//		.add("allowBlank",false)
					//	)
					.add(Item.crear("editor","editorNombreContratantep2").setQuotes(""))
					);
			item3.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "Segundo nombre"))
					.add(new Item("dataIndex", "segundo_nombre"))
					.add(new Item("width", 100))
					.add(Item.crear("editor",null,Item.OBJ)
							.add("xtype","textfield")
							.add("allowBlank",true)
						)
					);
			item3.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "Apellido paterno"))
					.add(new Item("dataIndex", "Apellido_Paterno"))
					.add(new Item("width", 100))
					.add(Item.crear("editor",null,Item.OBJ)
							.add("xtype","textfield")
							.add("allowBlank",true)
						)
					);
			item3.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "Apellido materno"))
					.add(new Item("dataIndex", "Apellido_Materno"))
					.add(new Item("width", 100))
					.add(Item.crear("editor",null,Item.OBJ)
							.add("xtype","textfield")
							.add("allowBlank",true)
						)
					);
			item3.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "Fecha de nacimiento"))
					.add(new Item("dataIndex", "fenacimi"))
					.add(new Item("width", 100))
					.add(Item.crear("editor","editorFechap2").setQuotes(""))
					.add(Item.crear("renderer","Ext.util.Format.dateRenderer('d M Y')").setQuotes(""))
					);
			item3.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "Sexo"))
					.add(new Item("dataIndex", "sexo"))
					.add(new Item("width", 100))
					.add(Item.crear("renderer","rendererSexop2").setQuotes(""))
					.add(Item.crear("editor","editorGenerosp2").setQuotes(""))
					);
			item3.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "T. Persona"))
					.add(new Item("dataIndex", "tpersona"))
					.add(new Item("width", 100))
					.add(Item.crear("renderer","rendererTpersonap2").setQuotes(""))
					.add(Item.crear("editor","editorTpersonap2").setQuotes(""))
					);
			item3.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "Nacionalidad"))
					.add(new Item("dataIndex", "nacional"))
					.add(new Item("width", 100))
					.add(Item.crear("renderer","rendererNacionesp2").setQuotes(""))
					.add(Item.crear("editor","editorNacionesp2").setQuotes(""))
					);
			item3.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "RFC"))
					.add(new Item("dataIndex", "cdrfc"))
					.add(new Item("width", 120))
					.add(Item.crear("editor","editorRFCAp2").setQuotes(""))
					);
			item3.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("xtype", "actioncolumn"))
					.add(new Item("width", 80))
					.add(new Item("menuDisabled", true))
					.add(new Item("header", "Acciones"))
					.add(new Item("items", null,Item.ARR)
						.add(Item.crear(null,null,Item.OBJ)
								.add("icon","resources/fam3icons/icons/report_key.png")
								.add("tooltip","Editar domicilios")
								.add(Item.crear("scope","this").setQuotes(""))
								.add(Item.crear("handler","this.onDomiciliosClick").setQuotes(""))
								)
						));
			item3.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "nmsituac"))
					.add(new Item("dataIndex", "nmsituac"))
					.add(new Item("flex", 1))
					.add(Item.crear("hidden",true))
					);
			item3.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "cdperson"))
					.add(new Item("dataIndex", "cdperson"))
					.add(new Item("flex", 1))
					.add(Item.crear("hidden",true))
					);
			item3.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "cdrol"))
					.add(new Item("dataIndex", "cdrol"))
					.add(new Item("flex", 1))
					.add(Item.crear("hidden",true))
					);
			item3.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "swexiper"))
					.add(new Item("dataIndex", "swexiper"))
					.add(new Item("flex", 1))
					.add(Item.crear("hidden",true))
					);

			item3.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "cdideper"))
					.add(new Item("dataIndex", "cdideper"))
					.add(new Item("flex", 1))
					.add(Item.crear("hidden",true))
					);
			item3.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "cdideext"))
					.add(new Item("dataIndex", "cdideext"))
					.add(new Item("flex", 1))
					.add(Item.crear("hidden",true))
					);
			
			item2 = new Item("columns", null, Item.ARR);// para las columnas del grid
			/*item2.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "Rol"))
					.add(new Item("dataIndex", "cdrol"))
					.add(new Item("flex", 1))
					.add(Item.crear("renderer","rendererRolp2").setQuotes(""))
					.add(Item.crear("editor","editorRolesp2").setQuotes(""))
					);*/
			item2.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("xtype", "checkcolumn"))
					.add(new Item("header", "Contratante"))
					.add(new Item("dataIndex", "estomador"))
					.add(new Item("width", 90))
					);
			item2.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "Nombre"))
					.add(new Item("dataIndex", "nombre"))
					.add(new Item("width", 100))
					.add(Item.crear("editor",null,Item.OBJ)
							.add("xtype","textfield")
							.add("allowBlank",false)
						)
					);
			item2.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "Segundo nombre"))
					.add(new Item("dataIndex", "segundo_nombre"))
					.add(new Item("width", 120))
					.add(Item.crear("editor",null,Item.OBJ)
							.add("xtype","textfield")
							.add("allowBlank",true)
						)
					);
			item2.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "Apellido paterno"))
					.add(new Item("dataIndex", "Apellido_Paterno"))
					.add(new Item("width", 120))
					.add(Item.crear("editor",null,Item.OBJ)
							.add("xtype","textfield")
							.add("allowBlank",false)
						)
					);
			item2.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "Apellido materno"))
					.add(new Item("dataIndex", "Apellido_Materno"))
					.add(new Item("width", 120))
					.add(Item.crear("editor",null,Item.OBJ)
							.add("xtype","textfield")
							.add("allowBlank",false)
						)
					);
			item2.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "Fecha de nacimiento"))
					.add(new Item("dataIndex", "fenacimi"))
					.add(new Item("width", 100))
					.add(Item.crear("editor","editorFechap2").setQuotes(""))
					.add(Item.crear("renderer","Ext.util.Format.dateRenderer('d M Y')").setQuotes(""))
					);
			item2.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "Sexo"))
					.add(new Item("dataIndex", "sexo"))
					.add(new Item("width", 90))
					.add(Item.crear("renderer","rendererSexop2").setQuotes(""))
					.add(Item.crear("editor","editorGenerosBp2").setQuotes(""))
					);
			item2.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "T. Persona"))
					.add(new Item("dataIndex", "tpersona"))
					.add(new Item("width", 100))
					.add(Item.crear("renderer","rendererTpersonap2").setQuotes(""))
					.add(Item.crear("editor","editorTpersonaBp2").setQuotes(""))
					);
			item2.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "Parentesco"))
					.add(new Item("dataIndex", "Parentesco"))
					.add(new Item("width", 100))
					.add(Item.crear("renderer","rendererParentescop2").setQuotes(""))
					.add(Item.crear("editor","editorParentescoBp2").setQuotes(""))
					);
			item2.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "RFC"))
					.add(new Item("dataIndex", "cdrfc"))
					.add(new Item("width", 120))
					.add(Item.crear("editor","editorRFCBp2").setQuotes(""))
					);
			item2.add(Item.crear(null, null, Item.OBJ) //ELP
					.add(new Item("header", "Estado Civil"))
					.add(new Item("dataIndex", "cdestciv"))
					.add(new Item("width", 100))
					.add(Item.crear("renderer","rendererEstcivp2").setQuotes(""))
					.add(Item.crear("editor","editorEstcivp2").setQuotes(""))	
					);
			item2.add(Item.crear(null, null, Item.OBJ)
					//.add(new Item("header", "No. Empleado"))
					.add(new Item("header", "No. Socio / Empleado"))
					.add(new Item("dataIndex", "numsoc"))
					.add(new Item("width", 130))
					.add(Item.crear("editor",null,Item.OBJ)
							.add("xtype","textfield")
							.add("name","numsoc")
							.add("allowBlank",true)
							.add("maxLength",6)
						)
					);
			item2.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "Clave Familiar"))
					.add(new Item("dataIndex", "clvfam"))
					.add(new Item("width", 100))
					.add(Item.crear("editor",null,Item.OBJ)
							.add("xtype","textfield")
							.add("name","clvfam")
							.add("allowBlank",true)
							.add("maxLength",2)
						)
					);
			item2.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "Ocupaci\u00f3n"))
					.add(new Item("dataIndex", "ocup"))
					.add(new Item("width", 100))
					.add(Item.crear("renderer","rendererOcupp2").setQuotes(""))
					.add(Item.crear("editor","editorOcupp2").setQuotes(""))
					);
			
			/*xtype: 'actioncolumn',
	                        width: 30,
	                        sortable: false,
	                        menuDisabled: true,
	                        items: [{
	                            icon:'resources/extjs4/resources/fam3icons/icons/delete.png',
	                            //iconCls: 'icon-delete',
	                            tooltip: 'Quitar inciso',
	                            scope: this,
	                            handler: this.onRemoveClick
	                        }]*/
			item2.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("xtype", "actioncolumn"))
					.add(new Item("width", 130))
					//.add(new Item("width", 80))
					.add(new Item("menuDisabled", true))
					.add(new Item("header", "Acciones"))
					.add(new Item("items", null,Item.ARR)
						.add(Item.crear(null,null,Item.OBJ)
								.add("icon","resources/fam3icons/icons/text_list_bullets.png")
								.add("tooltip","Editar coberturas")
								.add(Item.crear("scope","this").setQuotes(""))
								.add(Item.crear("handler","this.onEditarClick").setQuotes(""))
//								.add(Item.crear("isDisabled ","function(view,rowIndex,colIndex,item,record){if(record.get('estomador'))return true;}").setQuotes(""))
								)
						.add(Item.crear(null,null,Item.OBJ)
								.add("icon","resources/fam3icons/icons/report_key.png")
								.add("tooltip","Editar domicilios")
								.add(Item.crear("scope","this").setQuotes(""))
								.add(Item.crear("handler","this.onDomiciliosClick").setQuotes(""))
								.add(Item.crear("isDisabled ","function(view,rowIndex,colIndex,item,record){if(record.get('estomador'))return true;}").setQuotes(""))
								)
						.add(Item.crear(null,null,Item.OBJ)
								.add("icon","resources/fam3icons/icons/lock.png")
								.add("tooltip","Editar exclusiones")
								.add(Item.crear("scope","this").setQuotes(""))
								.add(Item.crear("handler","this.onExclusionClick").setQuotes(""))
//								.add(Item.crear("isDisabled ","function(view,rowIndex,colIndex,item,record){if(record.get('estomador'))return true;}").setQuotes(""))
								)
						.add(Item.crear(null,null,Item.OBJ)
								.add("icon","resources/fam3icons/icons/user_edit.png")
								.add("tooltip","Datos de situaci\u00f3n asegurado")
								.add(Item.crear("scope","this").setQuotes(""))
								.add(Item.crear("handler","this.onValositClick").setQuotes(""))
//								.add(Item.crear("isDisabled ","function(view,rowIndex,colIndex,item,record){if(record.get('estomador'))return true;}").setQuotes(""))
								)
						.add(Item.crear(null,null,Item.OBJ)
								.add("icon","resources/fam3icons/icons/money.png")
								.add("tooltip","Beneficiarios")
								.add(Item.crear("scope","this").setQuotes(""))
								.add(Item.crear("handler","this.onBeneficiariosClick").setQuotes(""))
								)
						/*
						.add(Item.crear(null,null,Item.OBJ)
								.add("icon","resources/fam3icons/icons/money.png")
								.add("tooltip","Es el contratante")
								.add(Item.crear("scope","this").setQuotes(""))
								.add(Item.crear("handler","this.onTomadorClick").setQuotes(""))
								)
						.add(Item.crear(null,null,Item.OBJ)
								.add("icon","resources/extjs4/resources/fam3icons/icons/delete.png")
								.add("tooltip","Quitar asegurado")
								.add(Item.crear("scope","this").setQuotes(""))
								.add(Item.crear("handler","this.onRemoveClick").setQuotes(""))
								)*/
					)
					.add(Item.crear("editor",null,Item.OBJ)
							.add("xtype","textfield")
							.add("allowBlank",false)
						)
					);
			item2.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "Nacionalidad"))
					.add(new Item("dataIndex", "nacional"))
					.add(new Item("flex", 1))
					.add(Item.crear("renderer","rendererNacionesp2").setQuotes(""))
					.add(Item.crear("editor","editorNacionesBp2").setQuotes(""))
					.add(Item.crear("hidden",true))
					);
			item2.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "nmsituac"))
					.add(new Item("dataIndex", "nmsituac"))
					.add(new Item("flex", 1))
					.add(Item.crear("hidden",true))
					);
			item2.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "cdperson"))
					.add(new Item("dataIndex", "cdperson"))
					.add(new Item("flex", 1))
					.add(Item.crear("hidden",true))
					);
			item2.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "cdrol"))
					.add(new Item("dataIndex", "cdrol"))
					.add(new Item("flex", 1))
					.add(Item.crear("hidden",true))
					);
			item2.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "swexiper"))
					.add(new Item("dataIndex", "swexiper"))
					.add(new Item("flex", 1))
					.add(Item.crear("hidden",true))
					);
			item2.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "cdideper"))
					.add(new Item("dataIndex", "cdideper"))
					.add(new Item("flex", 1))
					.add(Item.crear("hidden",true))
					);
			item2.add(Item.crear(null, null, Item.OBJ)
					.add(new Item("header", "cdideext"))
					.add(new Item("dataIndex", "cdideext"))
					.add(new Item("flex", 1))
					.add(Item.crear("hidden",true))
					);
			
			String maxLenContratante = null;
			try
			{
				maxLenContratante = catalogosManager.obtieneCantidadMaxima(
					map1.get("cdramo")
					,map1.get("cdtipsit")
					,TipoTramite.POLIZA_NUEVA
					,Rango.LONGITUD
					,Validacion.LONGITUD_MAX_CONTRATANTE);
			}
			catch(Exception ex)
			{
				logger.error("error sin efectos en obtener cantidad maxima",ex);
				maxLenContratante = "0";
			}
			
			map1.put("maxLenContratante",maxLenContratante);

			String nmOrdDom = null;
			try
			{
				List<Map<String,String>> res= consultasManager.obtieneContratantePoliza(map1.get("cdunieco"),map1.get("cdramo"),"W",map1.get("nmpoliza"),null,"1",null);
				
				if(res!=null && !res.isEmpty()){
					Map<String,String> contratante = res.get(0);
					if(contratante.get("NMORDDOM")!=null){
						nmOrdDom = contratante.get("NMORDDOM");
					}
				}
			}
			catch(Exception ex)
			{
				logger.error("Error sin impacto al obtener numero de domicilio, no se encontro ningun contratante en la poliza.",ex);
			}
			
			map1.put("NMORDDOM",nmOrdDom);
			
			logger.debug("map1: "+map1);
			
		} catch (Exception ex) {
			logger.error("error al generar los campos dinamicos", ex);
			item1 = null;
		}
		return SUCCESS;
	}
	
	public String pantallaAseguradosAuto()
	{
		logger.debug(Utils.log(
				 "\n####################################"
				,"\n###### pantallaAseguradosAuto ######"
				,"\n###### map1 = ", map1
				));
		String cdunieco = map1.get("cdunieco");
		String cdramo   = map1.get("cdramo");
		String cdtipsit = map1.get("cdtipsit");
		String estado   = map1.get("estado");
		String nmpoliza = map1.get("nmpoliza");
		
		GeneradorCampos gc;
		
		boolean success = true;
		if(success)
		{
			try
			{
				UserVO usuario  = (UserVO)session.get("USUARIO");
				String cdsisrol = usuario.getRolActivo().getClave();
				String pantalla = "EDITAR_ASEGURADOS";
				
				String seccion  = "ASEGURADO";
				List<ComponenteVO>componenteAsegurado=pantallasManager.obtenerComponentes(
						null, cdunieco, cdramo, cdtipsit, estado, cdsisrol, pantalla, seccion, null);
				gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
				gc.generaComponentes(componenteAsegurado, true, true, true, true, true, false);
				item1=gc.getFields();
				item2=gc.getItems();
				
				seccion  = "ASEG_EDITOR";
				componenteAsegurado=pantallasManager.obtenerComponentes(
						null, cdunieco, cdramo, cdtipsit, estado, cdsisrol, pantalla, seccion, null);
				gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
				gc.generaComponentes(componenteAsegurado, true, true, true, true, true, false);
				item3=gc.getColumns();
				
				
				seccion = "VALIDA_BORRAR";
				List<ComponenteVO> botonBorrar=pantallasManager.obtenerComponentes(
						null, cdunieco, cdramo, cdtipsit, estado, cdsisrol, pantalla, seccion, null);
				if(botonBorrar.size()>0)
				{
					gc.generaComponentes(botonBorrar, true, false, false, false, false, true);
					item4=gc.getButtons();
				}
				else
				{
					item4=null;
				}
				
				seccion = "VALIDA_GUARDAR";
				List<ComponenteVO> botonGuardar=pantallasManager.obtenerComponentes(
						null, cdunieco, cdramo, cdtipsit, estado, cdsisrol, pantalla, seccion, null);
				if(botonGuardar.size()>0)
				{
					gc.generaComponentes(botonGuardar, true, false, false, false, false, true);
					item5=gc.getButtons();
				}
				else
				{
					item5=null;
				}
			}
			catch(Exception ex)
			{
				logger.error("error al cargar la pantalla de asegurados de auto",ex);
			}
		}
		
		logger.debug(Utils.log(
				 "\n###### pantallaAseguradosAuto ######"
				,"\n####################################"
				));
		return SUCCESS;
	}
	
	public String cargarPantallaAsegurados() {
		logger.debug("cargarPantallaAsegurados map1: "+map1);
		try
		{
			list1=kernelManager.obtenerAsegurados(map1);
			/*Iterator it=list1.iterator();
			while(it.hasNext())
			{
				Map<String,Object>aseg=(Map<String, Object>) it.next();
				if(aseg!=null && aseg.get("Parentesco")!= null && StringUtils.isNotBlank((String)aseg.get("Parentesco"))){
					String cdperson = (String) aseg.get("cdperson");
					if(StringUtils.isBlank(cdperson)){
						Map<String,Object>cdpersonRes=storedProceduresManager.procedureParamsCall(
								ObjetoBD.GENERAR_CDPERSON.getNombre(),
								new LinkedHashMap<String,Object>(),
								null,
								new String[]{"pv_cdperson_o"},
								null);
						aseg.put("cdperson",(String)cdpersonRes.get("pv_cdperson_o"));
					}
					
				}
			}*/
			success=true;
		}
		catch(Exception ex)
		{
			logger.error("error al cargar los asegurados",ex);
			list1=new ArrayList<Map<String,Object>>(0);
			success=false;
		}
		return SUCCESS;
	}
	
	public String guardarPantallaAsegurados()
	{
		logger.debug(Utils.log(
				 "\n############################################"
				,"\n###### guardarPantallaAseguradosSalud ######"
				));
		logger.debug(Utils.log("map1="  , map1));
		logger.debug(Utils.log("list1=" , list1));
		try
		{
			UserVO usuario = (UserVO) session.get("USUARIO");
			logger.debug(Utils.log("usuario ->",usuario));
			String usuarioCaptura =  null;
			
			if(usuario!=null){
				if(StringUtils.isNotBlank(usuario.getClaveUsuarioCaptura())){
					usuarioCaptura = usuario.getClaveUsuarioCaptura();
				}else{
					usuarioCaptura = usuario.getCodigoPersona();
				}
				
			}
			
			/**
			 * Para validar Zona de CP Estado y municipio del titular Respecto a la cotizacion
			 */
			String cdpersonContrat = null;
			HashMap<String, String> cdpersonsAsegs = new HashMap<String, String>();
			
			for(Map<String,Object>personaIt:list1){
				logger.debug(Utils.log("cdrol",(String) personaIt.get("cdrol")));
				logger.debug(Utils.log("cdrol",(String) personaIt.get("cdperson")));
				
				String cdrolIt = (String) personaIt.get("cdrol");
				String cdpersonIt = (String) personaIt.get("cdperson");
				
				
				if(StringUtils.isNotBlank(cdrolIt) && "1".equals(cdrolIt)){
					cdpersonContrat = cdpersonIt;
				}else{
					cdpersonsAsegs.put(cdpersonIt, cdrolIt);
				}
			}
			
			/**
			 * Validar el domicilio de algun asegurado que tambien es contrantante, 
			 * pues debe heredar el domicilio del titular y debe de ser el mismo de la cotizacion
			 */
			if(cdpersonsAsegs.containsKey(cdpersonContrat)){
				try
				{
					Map<String,String> param=new LinkedHashMap<String,String>(0);
					param.put("pv_cdunieco_i",	map1.get("pv_cdunieco"));
					param.put("pv_cdramo_i",	map1.get("pv_cdramo"));
					param.put("pv_estado_i",	map1.get("pv_estado"));
					param.put("pv_nmpoliza_i",	map1.get("pv_nmpoliza"));
					param.put("pv_cdperson_i",	cdpersonContrat);
					param.put("pv_nmorddom_i",	map1.get("pv_nmorddom"));
					boolean validacionDomTitu = cotizacionManager.validaDomicilioCotizacionTitular(param);
					
					if(!validacionDomTitu){
						mensajeRespuesta = "El C\u00f3digo Postal (Estado y Municipio) de la cotizaci\u00f3n debe coincidir con el domicilio del asegurado contratante.";
						success = false;
						return SUCCESS;
					}
					 
				}
				catch(Exception ex)
				{
					logger.error("Error de validaci\u00f3n de C\u00f3digo Postal (Estado y Municipio) de la cotizaci\u00f3n debe coincidir con el domicilio del asegurado contratante.",ex);
					mensajeRespuesta = "Error al validar el C\u00f3digo Postal. Consulte a Soporte.";
					success = false;
					return SUCCESS;
				}
			}
			
			
			///////////////////////////////////////////////
			////// para borrar los mpoliper anterior //////
			/*///////////////////////////////////////////*/
			try
			{
				Map<String,String> param=new LinkedHashMap<String,String>(0);
				param.put("pv_cdunieco_i",	map1.get("pv_cdunieco"));
				param.put("pv_cdramo_i",	map1.get("pv_cdramo"));
				param.put("pv_estado_i",	map1.get("pv_estado"));
				param.put("pv_nmpoliza_i",	map1.get("pv_nmpoliza"));
				kernelManager.borrarMpoliper(param);
			}
			catch(Exception ex)
			{
				logger.error("error cachado, no hay personas que borrar, pero no afecta.",ex);
			}
			/*///////////////////////////////////////////*/
			////// para borrar los mpoliper anterior //////
			///////////////////////////////////////////////
			
			
			int i=1;
			for(Map<String,Object>aseg:list1)
			{
				
				String nmsituaext = null, ns =null, cf =null;
				String clvfam    = (String) aseg.get("clvfam");
				String numsoc    = (String) aseg.get("numsoc");
				String nmsituac  = (String) aseg.get("nmsituac");
				Boolean estomador = (Boolean) aseg.get("estomador");
				
				
				//N�mero de socio y Clave Familiar, para el atributo SITUAEXT ELP
				
				logger.debug(Utils.log("Numero de Socio ->",numsoc));
				logger.debug(Utils.log("Clave Familiar  ->",clvfam));
				logger.debug(Utils.log("Estado Civil    ->",(String) aseg.get("cdestciv")));
				logger.debug(Utils.log("Ocupacion       ->",(String) aseg.get("ocup")));
				logger.debug(Utils.log("Nmsituac        ->",(String) aseg.get("nmsituac")));
				logger.debug(Utils.log("Estomador       ->",estomador));
				
				if(StringUtils.isBlank(numsoc) || StringUtils.isBlank(clvfam)){
					nmsituaext = "";
				}else{
					logger.debug(Utils.log("Generando Situaext..."));
					ns = StringUtils.leftPad(numsoc, 6, "0");
					
					if(clvfam.length() < 2){
						cf = clvfam;
					}
					
					cf = StringUtils.leftPad(clvfam, 2, "0");
					
					//NMSITUAEXT
					nmsituaext = ns + "-" + cf;
					logger.debug(Utils.log("nmsituaext ->",nmsituaext));
				}
						
						
				Map<String,Object> parametros=new LinkedHashMap<String,Object>(0);
				String swExiper = (String)aseg.get("swexiper");
				logger.debug(Utils.log("swexiper ->"+swExiper));
		
				if(StringUtils.isBlank(swExiper) || swExiper.equalsIgnoreCase("N")){
				
					String cdIdeperAseg = (String) aseg.get("cdideper");
					String cdIdeExtAseg = (String) aseg.get("cdideext");
					parametros.put("pv_cdperson_i"    , (String)aseg.get("cdperson"));
					parametros.put("pv_cdtipide_i"    , "1");
					parametros.put("pv_cdideper_i"    , cdIdeperAseg);
					parametros.put("pv_dsnombre_i"    , (String)aseg.get("nombre"));
					parametros.put("pv_cdtipper_i"    , "1");
					parametros.put("pv_otfisjur_i"    , (String)aseg.get("tpersona"));
					parametros.put("pv_otsexo_i"      , (String)aseg.get("sexo"));
					parametros.put("pv_fenacimi_i"    , renderFechas.parse((String)aseg.get("fenacimi")));
					parametros.put("pv_cdrfc_i"       , (String)aseg.get("cdrfc"));
					parametros.put("pv_dsemail_i"     , "");
					parametros.put("pv_dsnombre1_i"   , (String)aseg.get("segundo_nombre"));
					parametros.put("pv_dsapellido_i"  , (String)aseg.get("Apellido_Paterno"));
					parametros.put("pv_dsapellido1_i" , (String)aseg.get("Apellido_Materno"));
					parametros.put("pv_feingreso_i"   , calendarHoy.getTime());
					parametros.put("pv_cdnacion_i"    , (String)aseg.get("nacional"));
					parametros.put("pv_canaling_i"    , null);
					parametros.put("pv_conducto_i"    , null);
					parametros.put("pv_ptcumupr_i"    , null);
					parametros.put("pv_residencia_i"  , null);
					parametros.put("pv_nongrata_i"    , null);
					parametros.put("pv_cdideext_i"    , cdIdeExtAseg);
					parametros.put("pv_cdestciv_i"    , (String)aseg.get("cdestciv")); 
					parametros.put("pv_cdsucemi_i"    , null);
					parametros.put("pv_cdusuario_i"   , usuarioCaptura);
					parametros.put("pv_dsocupacion_i" , (String)aseg.get("ocup"));
					parametros.put("pv_accion_i"      , "I");
					logger.debug("#iteracion mov mpersonas "+i);
					kernelManager.movMpersona(parametros);
					
					
				}
				
				
				//se actualiza situaext
				emisionManager.actualizaNmsituaextMpolisit(
						map1.get("pv_cdunieco")
						,map1.get("pv_cdramo")
						,map1.get("pv_estado")
						,map1.get("pv_nmpoliza")
						,nmsituac
						,"0"
						,nmsituaext
						);
				logger.debug(Utils.log("Se agrego el nmsituaext en mpolisit "+i));
				
				String cdRolAseg = (String)aseg.get("cdrol");
				
				parametros=new LinkedHashMap<String,Object>(0);
				parametros.put("pv_cdunieco_i",	map1.get("pv_cdunieco"));
				parametros.put("pv_cdramo_i",	map1.get("pv_cdramo"));
				parametros.put("pv_estado_i",	map1.get("pv_estado"));
				parametros.put("pv_nmpoliza_i",	map1.get("pv_nmpoliza"));
				parametros.put("pv_nmsituac_i",	nmsituac); 
				parametros.put("pv_cdrol_i", 	cdRolAseg);
				parametros.put("pv_cdperson_i",	(String)aseg.get("cdperson"));
				parametros.put("pv_nmsuplem_i",	"0");
				parametros.put("pv_status_i",	"V");
				/** PARA EL CONTRATANTE SE ENVIA EL DOMICILIO QUE SE SELECCIONA EN PANTALLA SI NO SE MANDA POR DEFAULT EL 1 PARA ASEGURADOS **/
				parametros.put("pv_nmorddom_i",	(StringUtils.isNotBlank(cdpersonContrat) && cdpersonContrat.equals((String)aseg.get("cdperson"))) ? map1.get("pv_nmorddom") : "1");
				parametros.put("pv_swreclam_i",	null);
				parametros.put("pv_accion_i",	"I");
				parametros.put("pv_swexiper_i", (String)aseg.get("swexiper"));
				logger.debug("#iteracion mov mpoliper "+i);
				
				
				/**
				 * SOLO PARA EL CONTRATANTE
				 */
				if(((String)aseg.get("nmsituac")).equals("0"))
				{
					kernelManager.borraMpoliper(parametros);
					
				}
				
				kernelManager.movMpoliper(parametros);
				
				if(swExiper.equalsIgnoreCase("S") && !nmsituac.equals("0") 
						&& estomador){ 
					logger.debug(Utils.log("Es un tomador...."));
					//Se actualizan los datos de un contratante en MPERSONA
					emisionManager.actualizaDatosMpersona(
							map1.get("pv_cdunieco")
							,map1.get("pv_cdramo")
							,map1.get("pv_estado")
							,map1.get("pv_nmpoliza")
							,nmsituac
							,"0"
							,(String)aseg.get("cdestciv")
							,(String)aseg.get("ocup")
							);
				}else if(swExiper.equalsIgnoreCase("S") && !nmsituac.equals("0")){
					//Se actualizan los datos de un asegurado en MPERSONA
					emisionManager.actualizaDatosMpersona(
							map1.get("pv_cdunieco")
							,map1.get("pv_cdramo")
							,map1.get("pv_estado")
							,map1.get("pv_nmpoliza")
							,nmsituac
							,"0"
							,(String)aseg.get("cdestciv")
							,(String)aseg.get("ocup")
							);
				}
				
				//////////////////////////
				//para que cambie tvalosit
				try
				{
					Map<String,String>paramsGetValosit = new LinkedHashMap<String,String>(0);
					paramsGetValosit.put("pv_cdunieco_i" , map1.get("pv_cdunieco"));
					paramsGetValosit.put("pv_cdramo_i"   , map1.get("pv_cdramo"));
					paramsGetValosit.put("pv_estado_i"   , map1.get("pv_estado"));
					paramsGetValosit.put("pv_nmpoliza_i" , map1.get("pv_nmpoliza"));
					paramsGetValosit.put("pv_nmsituac_i" , (String)aseg.get("nmsituac"));
					logger.debug("paramsValositAseguradoIterado: "+paramsGetValosit);
					Map<String,Object>valositAsegurado=kernelManager.obtieneValositSituac(paramsGetValosit);
					logger.debug("valositAseguradoIterado: "+valositAsegurado);
					
					Map<String,Object>valositAseguradoIteradoTemp=new LinkedHashMap<String,Object>(0);
					//poner pv_ a los leidos
					Iterator it=valositAsegurado.entrySet().iterator();
					while(it.hasNext())
					{
						Entry en=(Entry)it.next();
						valositAseguradoIteradoTemp.put("pv_"+(String)en.getKey(),en.getValue());//agregar pv_ a los anteriores
					}
					valositAsegurado=valositAseguradoIteradoTemp;
					logger.debug("se puso pv_");
					
					try
					{
						Map<String,String>atributos=consultasManager.cargarAtributosBaseCotizacion(map1.get("cdtipsit"));
					
						String cdatribuFenacimi = atributos.get("FENACIMI");
						if(cdatribuFenacimi.length()==1)
						{
							cdatribuFenacimi = "0"+cdatribuFenacimi;
						}
						valositAsegurado.put("pv_otvalor"+cdatribuFenacimi, (String)aseg.get("fenacimi"));
						
						String cdatribusSexo = atributos.get("SEXO");
						if(cdatribusSexo.length()==1)
						{
							cdatribusSexo = "0"+cdatribusSexo;
						}
						valositAsegurado.put("pv_otvalor"+cdatribusSexo, (String)aseg.get("sexo"));
						logger.debug("se agregaron los nuevos");
					}
					catch(Exception ex)
					{
						logger.error("error en obtener los atributos",ex);
					}
					
					//convertir a string el total
					Map<String,String>paramsNuevos=new LinkedHashMap<String,String>(0);
					it=valositAsegurado.entrySet().iterator();
					while(it.hasNext())
					{
						Entry en=(Entry)it.next();
						paramsNuevos.put((String)en.getKey(),(String)en.getValue());
					}
					logger.debug("se pasaron a string");
					
					paramsNuevos.put("pv_cdunieco" , map1.get("pv_cdunieco"));
					paramsNuevos.put("pv_cdramo"   , map1.get("pv_cdramo"));
					paramsNuevos.put("pv_estado"   , map1.get("pv_estado"));
					paramsNuevos.put("pv_nmpoliza" , map1.get("pv_nmpoliza"));
					paramsNuevos.put("pv_nmsituac" , (String)aseg.get("nmsituac"));
					logger.debug("los actualizados seran: "+paramsNuevos);
					
					kernelManager.actualizaValoresSituaciones(paramsNuevos);
				}
				catch(Exception ex)
				{
					logger.error("exception no lanzada a pantalla",ex);
				}
				//para que cambie tvalosit
				//////////////////////////
				
				i++;
			}
			success=true;
		}
		catch(Exception ex)
		{
			logger.error("error al guardar asegurados",ex);
			success=false;
		}
		
		logger.debug(Utils.log(
				 "\n###### pantallaAseguradosSalud ######"
				,"\n#####################################"
				));
		
		return SUCCESS;
	}

	public String generarCdperson()
	{
		try
		{
			cdperson=kernelManager.generaCdperson();
			if(session.get("cdpersonciclado")!=null&&((String)session.get("cdpersonciclado")).equals(cdperson))
			{
				logger.debug("###############################################");
				logger.debug("###############################################");
				logger.debug("##################CICLADO######################");
				logger.debug("###############################################");
				logger.debug("###############################################");
				return generarCdperson();
			}
			else
			{
				session.put("cdpersonciclado",cdperson);
			}
			success=true;
		}
		catch(Exception ex)
		{
			logger.error("error al generar cdperson",ex);
			success=false;
		}
		return SUCCESS;
	}
	
	public String detalleCotizacion()
	{
		try
		{
			logger.debug("panel1: "+panel1);
			/*nmsituac,parentesco,orden,Codigo_Garantia, Nombre_garantia, cdtipcon, Importe*/
			slist1=kernelManager.obtenerDetallesCotizacion(panel1);
			success=true;
		}
		catch(Exception ex)
		{
			logger.error("Error al obtener el detalle de cotizacion",ex);
			success=false;
		}
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String retarificar () {
		logger.debug(Utils.log(
				"\n#########################",
				"\n###### retarificar ######",
				"\n###### cdunieco = ", cdunieco,
				"\n###### cdramo   = ", cdramo,
				"\n###### cdtipsit = ", cdtipsit,
				"\n###### panel1   = ", panel1
		));
		try {
			try {
				if (consultasManager.esProductoSalud(cdramo)) {
					logger.debug("Revisando solicitud de emisi\u00f3n para salud");
					if(!"S".equals(panel1.get("renovacion"))){
						emisionManager.validarDocumentoTramite(
								emisionManager.recuperarTramiteCotizacion(cdunieco, cdramo, "W", panel1.get("nmpoliza")),
								"1" // CDDOCUME 1 = solicitud de emision
						);
					}
					panel1.put("mensajeAgenteActivo", cotizacionManager.validaAgenteActivo(cdunieco, cdramo, "W", panel1.get("nmpoliza"), "0"));					
				}
			} catch(Exception ex) {
				logger.error("error surgido al validar solicitud de emision de salud", ex);
				mensajeRespuesta = "Favor de subir la solicitud de emisi\u00f3n";
				return SUCCESS;
			}
			
			boolean notarifica = panel1!=null
					&&StringUtils.isNotBlank(panel1.get("notarifica"))
					&&panel1.get("notarifica").equals("si");
			
			try {
				consultasManager.validarDatosCliente(cdunieco, cdramo, "W", panel1.get("nmpoliza"));
			} catch (Exception ex) {
				logger.error("Error al validar datos de cliente",ex);
				mensajeRespuesta=ex.getMessage();
				return SUCCESS;
			}
			
			try {
				consultasManager.validarDatosObligatoriosPrevex(cdunieco, cdramo, "W", panel1.get("nmpoliza"));
			} catch(Exception ex) {
				logger.error("error al validar datos obligatorios de prevex",ex);
				mensajeRespuesta=ex.getMessage();
				return SUCCESS;
			}
			
			try {
				consultasManager.validarDatosDXN(cdunieco, cdramo, "W", panel1.get("nmpoliza"), "0");
			} catch (Exception ex) {
				logger.error("Error al validar Datos de DxN",ex);
				mensajeRespuesta = ex.getMessage();
				return SUCCESS;
			}
						
			///////////////////////////////////
			////// validar la extraprima //////
			/*///////////////////////////////*/
			List<Map<String,String>> aseguradosExtraprimadosInvalidos = null;
			try {
				Map<String,String>paramValExtraprima=new LinkedHashMap<String,String>(0);
				paramValExtraprima.put("pv_cdunieco_i" , cdunieco);
				paramValExtraprima.put("pv_cdramo_i"   , cdramo);
				paramValExtraprima.put("pv_estado_i"   , "W");
				paramValExtraprima.put("pv_nmpoliza_i" , panel1.get("nmpoliza"));
				aseguradosExtraprimadosInvalidos = kernelManager.validarExtraprima(paramValExtraprima).getItemList();
			} catch(Exception ex) {
				logger.warn("Error sin impacto funcional al validar extraprimas: ",ex);
			}
			if(aseguradosExtraprimadosInvalidos != null && aseguradosExtraprimadosInvalidos.size() > 0) {
				StringBuilder msjeErrorExtraprimas = new StringBuilder("Favor de verificar las extraprimas y los endosos de extraprima de: <br/>");
				for (Map<String, String> map : aseguradosExtraprimadosInvalidos) {
					msjeErrorExtraprimas.append(map.get("ASEGURADO")).append("<br/>");
				}
				mensajeRespuesta = msjeErrorExtraprimas.toString();
				return SUCCESS;
			}
			/*///////////////////////////////*/
			////// validar la extraprima //////
			///////////////////////////////////
			
			//////////////////////////////////////////
			////// validar que tengan direccion //1548
			List<Map<String,String>>lisUsuSinDir=null;
			try {
				Map<String,String>paramValidar=new LinkedHashMap<String,String>(0);
				paramValidar.put("pv_cdunieco" , cdunieco);
				paramValidar.put("pv_cdramo"   , cdramo);
				paramValidar.put("pv_estado"   , "W");
				paramValidar.put("pv_nmpoliza" , panel1.get("nmpoliza"));
				lisUsuSinDir=kernelManager.PValInfoPersonas(paramValidar);
			} catch(Exception ex) {
				logger.warn("Error sin impacto funcional al validar domicilios: ",ex);
				lisUsuSinDir=null;
			}
			
			if (lisUsuSinDir!=null&&lisUsuSinDir.size()>0) {
				if (Ramo.SERVICIO_PUBLICO.getCdramo().equals(cdramo) || Ramo.AUTOS_FRONTERIZOS.getCdramo().equals(cdramo)) {
					mensajeRespuesta="Favor de verificar y guardar correctamente la direcci\u00f3n y datos del contratante.";
				} else {
					mensajeRespuesta="Favor de verificar la direcci\u00f3n de los siguientes asegurados:<br/>";
					// f a v o r
					//0 1 2 3 4 5
					if (lisUsuSinDir.get(0).get("nombre").substring(0,5).equalsIgnoreCase("favor")) {
						mensajeRespuesta=lisUsuSinDir.get(0).get("nombre");
					} else {
						for (int i = 0; i < lisUsuSinDir.size(); i++) {
							mensajeRespuesta+=lisUsuSinDir.get(i).get("nombre")+"<br/>";
						}					
					}
				}
				
				logger.debug("Se va a terminar el proceso porque faltan direcciones");
				return SUCCESS;
			}

			
			
			try {
				Map<String,String>paramValidarMenor=new LinkedHashMap<String,String>(0);
				paramValidarMenor.put("pv_cdunieco" , cdunieco);
				paramValidarMenor.put("pv_cdramo"   , cdramo);
				paramValidarMenor.put("pv_estado"   , "W");
				paramValidarMenor.put("pv_nmpoliza" , panel1.get("nmpoliza"));
				String existeMenor = kernelManager.validaTitularMenorEdad(paramValidarMenor);
				
				if(Constantes.SI.equalsIgnoreCase(existeMenor)){
					this.respuestaOculta = "El Titular es Menor de Edad, se requerir\u00e1 una autorizaci\u00f3n posterior.";
				}
			} catch(Exception ex) {
				logger.warn("Error sin impacto funcional al validar Titular menor de edad: ",ex);
			}
			
			////// validar que tengan direccion //1548
			//////////////////////////////////////////
			
			
			//Validar datos de Producto de Autos para WS de Emision
			if (Ramo.AUTOS_FRONTERIZOS.getCdramo().equalsIgnoreCase(cdramo)) {
				try {
					HashMap<String, String> paramsValidaAut = new HashMap<String, String>();
					paramsValidaAut.put("pv_cdunieco_i", cdunieco);
					paramsValidaAut.put("pv_cdramo_i",   cdramo);
					paramsValidaAut.put("pv_estado_i",   "W");
					paramsValidaAut.put("pv_nmpoliza_i", panel1.get("nmpoliza"));
					paramsValidaAut.put("pv_nmsituac_i", "1");
					paramsValidaAut.put("pv_nmsuplem_i", "0");
					kernelManager.validaDatosAutos(paramsValidaAut);
					
				} catch (Exception e) {
					logger.error("Error al Validar datos de Auto: " + e.getMessage(), e);
					mensajeRespuesta = e.getMessage();
					return SUCCESS;
				}
			}
			
			
			UserVO usuario=(UserVO)session.get("USUARIO");
			
			if (!notarifica) {
				//////////////////////////
				////// sigsvdef end //////
				/*//////////////////////*/
				Map<String,String> mapCoberturas=new HashMap<String,String>(0);
	            //mapCoberturas.put("pv_cdunieco_i",   datosUsuario.getCdunieco());
	            //mapCoberturas.put("pv_cdramo_i",     datosUsuario.getCdramo());
	            mapCoberturas.put("pv_cdunieco_i",   cdunieco);//se agrega desde el formulario
	            mapCoberturas.put("pv_cdramo_i",     cdramo);//se agrega desde el formulario
	            mapCoberturas.put("pv_estado_i",     "W");
	            mapCoberturas.put("pv_nmpoliza_i",   panel1.get("nmpoliza"));
	            mapCoberturas.put("pv_nmsituac_i",   "0");
	            mapCoberturas.put("pv_nmsuplem_i",   "0");//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	            mapCoberturas.put("pv_cdgarant_i",   "TODO");//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	            mapCoberturas.put("pv_cdtipsup_i",   "1");
	            kernelManager.coberturasEnd(mapCoberturas);
				/*//////////////////////*/
				////// sigsvdef end //////
				//////////////////////////
				
				////////////////////////////////
				////// retarifica         //////
				/*////////////////////////////*/
				Map<String,String> mapaTarificacion=new HashMap<String,String>(0);
		        mapaTarificacion.put("pv_cdusuari_i",   usuario.getUser());
		        mapaTarificacion.put("pv_cdelemen_i",   usuario.getEmpresa().getElementoId());
		        //mapaTarificacion.put("pv_cdunieco_i",   datosUsuario.getCdunieco());
		        //mapaTarificacion.put("pv_cdramo_i",     datosUsuario.getCdramo());
		        mapaTarificacion.put("pv_cdunieco_i",   cdunieco);
		        mapaTarificacion.put("pv_cdramo_i",     cdramo);
		        mapaTarificacion.put("pv_estado_i",     "W");
		        mapaTarificacion.put("pv_nmpoliza_i",   panel1.get("nmpoliza"));
		        mapaTarificacion.put("pv_nmsituac_i",   "0");
		        mapaTarificacion.put("pv_nmsuplem_i",   "0");
		        //mapaTarificacion.put("pv_cdtipsit_i",   datosUsuario.getCdtipsit());
		        mapaTarificacion.put("pv_cdtipsit_i",   cdtipsit);
		        mx.com.aon.portal.util.WrapperResultados wr4=kernelManager.ejecutaASIGSVALIPOL_EMI(mapaTarificacion);
		        /*////////////////////////////*/
		        ////// retarifica         //////
		        ////////////////////////////////
			}
			
			try {
				String error = consultasManager.validacionesSuplemento(
						cdunieco
						,cdramo
						,"W"
						,panel1.get("nmpoliza")
						,null
						,"0"
						,"1"
						);
				if (StringUtils.isNotBlank(error)) {
					mensajeRespuesta = error;
					return SUCCESS;
				}
			} catch (Exception ex) {
				long timestamp = System.currentTimeMillis();
				logger.error(Utils.join("Error en validaciones #",timestamp),ex);
				mensajeRespuesta = Utils.join("Error en validaciones #",timestamp);
				return SUCCESS;
			}
	        
			///////////////////////////////////
			////// Generacion cotizacion //////
			/*///////////////////////////////*
			Map<String,String> mapaDuroResultados=new HashMap<String,String>(0);
			mapaDuroResultados.put("pv_cdusuari_i", usuario.getUser());
			mapaDuroResultados.put("pv_cdunieco_i", datosUsuario.getCdunieco());
			mapaDuroResultados.put("pv_cdramo_i",   datosUsuario.getCdramo());
			mapaDuroResultados.put("pv_estado_i",   "W");
			mapaDuroResultados.put("pv_nmpoliza_i", panel1.get("nmpoliza"));
			mapaDuroResultados.put("pv_cdelemen_i", usuario.getEmpresa().getElementoId());
			mapaDuroResultados.put("pv_cdtipsit_i", datosUsuario.getCdtipsit());
			List<ResultadoCotizacionVO> listaResultados=kernelManager.obtenerResultadosCotizacion(mapaDuroResultados);
			//utilizando logica anterior
			CotizacionManagerImpl mangerAnterior=new CotizacionManagerImpl();
			gridResultados=managerAnterior.adaptarDatosCotizacion(listaResultados);
			logger.debug("### session poniendo resultados con grid: "+listaResultados.size());
			session.put(ResultadoCotizacionAction.DATOS_GRID, gridResultados);
			/*///////////////////////////////*/
			////// Generacion cotizacion //////
			///////////////////////////////////
			
			////////////////////////////////
			////// obtener coberturas //////
	        /*////////////////////////////*/
			
			/**
			OBTENER INFORMACION DE POLIZAS
			pv_cdunieco
            pv_cdramo
            pv_estado
            pv_nmpoliza
            pv_cdusuari
            /**/
			Map<String,String>paramObtenerPoliza=new LinkedHashMap<String,String>(0);
			//paramObtenerPoliza.put("pv_cdunieco" , datosUsuario.getCdunieco());
			//paramObtenerPoliza.put("pv_cdramo"   , datosUsuario.getCdramo());
			paramObtenerPoliza.put("pv_cdunieco" , cdunieco);
			paramObtenerPoliza.put("pv_cdramo"   , cdramo);
			paramObtenerPoliza.put("pv_estado"   , "W");
			paramObtenerPoliza.put("pv_nmpoliza" , panel1.get("nmpoliza"));
			paramObtenerPoliza.put("pv_cdusuari" , usuario.getUser());
			Map<String,Object>polizaCompleta=kernelManager.getInfoMpolizasCompleta(paramObtenerPoliza);
			logger.debug("poliza a emitir: "+polizaCompleta);
			/**/
			
			/**
			OBTENER COBERTURAS
			pv_cdunieco_i
			pv_cdramo_i
			pv_estado_i
			pv_nmpoliza_i
			pv_cdplan_i
			pv_cdperpag_i
			
			out:
			status","swestado","nmsolici","feautori","cdmotanu","feanulac",
			"swautori","cdmoneda","feinisus","fefinsus",
            "ottempot","feefecto","hhefecto","feproren","fevencim","nmrenova","ferecibo","feultsin","nmnumsin","cdtipcoa",
            "swtarifi","swabrido","feemisio","cdperpag","nmpoliex","nmcuadro","porredau","swconsol","nmpolant","nmpolnva",
            "fesolici","cdramant","cdmejred","nmpoldoc","nmpoliza2","nmrenove","nmsuplee","ttipcamc","ttipcamv","swpatent
			*/
			Map<String,String>paramDetallePoliza=new LinkedHashMap<String,String>(0);
			//paramDetallePoliza.put("pv_cdunieco_i" , datosUsuario.getCdunieco());
			//paramDetallePoliza.put("pv_cdramo_i"   , datosUsuario.getCdramo());
			paramDetallePoliza.put("pv_cdunieco_i" , cdunieco);
			paramDetallePoliza.put("pv_cdramo_i"   , cdramo);
			paramDetallePoliza.put("pv_estado_i"   , "W");
			paramDetallePoliza.put("pv_nmpoliza_i" , panel1.get("nmpoliza"));
			paramDetallePoliza.put("pv_cdplan_i"   , null);
			paramDetallePoliza.put("pv_cdperpag_i" , (String) polizaCompleta.get("cdperpag"));
			slist1=kernelManager.obtenerDetallesCotizacion(paramDetallePoliza);
			success=true; 
			/**/
			/*////////////////////////////*/
			////// obtener coberturas //////
			////////////////////////////////
			
	        success=true;
		} catch (Exception ex) {
			logger.debug("error al retarificar",ex);
			success=false;
		}
		
		logger.debug(Utils.log(
				"\n###### success          = ", success,
				"\n###### mensajeRespuesta = ", mensajeRespuesta,
				"\n###### retarificar ######",
				"\n#########################"
		));
		return SUCCESS;
	}
	
	@SuppressWarnings("deprecation")
	public String emitir()
	{
		logger.debug(
				new StringBuilder()
				.append("\n####################")
				.append("\n###### emitir ######")
				.append("\n###### panel1=").append(panel1)
				.append("\n###### panel2=").append(panel2)
				.toString()
				);
		
		////// variables 
		success                = true;
		retryWS                = false;
		retryRec               = false;
		String ntramite        = null;
		UserVO us              = null;
		String cdunieco        = null;
		String cdramo          = null;
		String cdtipsit        = null;
		String estado          = "W";
		String nmpoliza        = null;
		String cdpersonSesion  = null;
		String cdusuari        = null;
		String cdsisrol        = null;
		String cdelemen        = null;
		String rutaCarpeta     = null;
		String cdperpag        = null;
		String nmpolizaEmitida = null;
		String nmpoliexEmitida = null;
		String nmsuplemEmitida = null;
		String esDxN           = null;
		String cdIdeperRes     = null;
		String tipoMov         = TipoTramite.POLIZA_NUEVA.getCdtiptra();
		boolean esFlotilla     = false;
		String caseIdRstn = null;
		
		tipoGrupoInciso = "I";
		
		Date fechaHoy = new Date();
		
		////// obtener parametros
		if(success)
		{
			try
			{
				success = success && (ntramite = panel1.get("pv_ntramite")      )!=null;
				success = success && (us       = (UserVO)session.get("USUARIO") )!=null;
				success = success && (cdunieco = panel2.get("pv_cdunieco")      )!=null;
				success = success && (cdramo   = panel2.get("pv_cdramo")        )!=null;
				success = success && (cdtipsit = panel2.get("pv_cdtipsit")      )!=null;
				success = success && (nmpoliza = panel2.get("pv_nmpoliza")      )!=null;
				esFlotilla = StringUtils.isNotBlank(panel1.get("flotilla"))
						&&panel1.get("flotilla").equalsIgnoreCase("si");
				
				if(StringUtils.isNotBlank(panel1.get("tipoGrupoInciso"))
						&&panel1.get("tipoGrupoInciso").equals("C"))
				{
					tipoGrupoInciso = "C";
				}
				
				if (success) {
				    caseIdRstn = panel2.get("caseIdRstn");
				    logger.debug(Utils.log("caseIdRstn: ", caseIdRstn));
				    if (StringUtils.isNotBlank(caseIdRstn)) {
				        try {
				            cotizacionManager.actualizarOtvalorTramitePorDsatribu(
			                        ntramite
			                        ,"CASEIDRSTN"
			                        ,caseIdRstn
			                        ,"U"
			                        );
				        } catch (Exception ex) {
				            logger.error("WARNING al guardar caseIdRstn en otvalor", ex);
				        }
				    }
				}
				
				if(!success)
				{
					mensajeRespuesta="No se recibieron todos los datos";
				}
			}
			catch(Exception ex)
			{
				logger.error("Error al procesar los datos",ex);
				mensajeRespuesta = "Error al procesar los datos";
				success          = false;
			}
		}
		
		////// datos de la sesion del usuario
		if(success)
		{
			try
			{
				DatosUsuario datUs = kernelManager.obtenerDatosUsuario(us.getUser(),cdtipsit);
				cdpersonSesion = datUs.getCdperson();
				cdusuari       = us.getUser(); 
				cdelemen       = us.getEmpresa().getElementoId();
				cdsisrol       = us.getRolActivo().getClave();
			}
			catch(Exception ex)
			{
				logger.error("error al obtener los datos de usuario",ex);
				mensajeRespuesta = ex.getMessage();
				success          = false;
			}
		}
		
		////// validar edad asegurados
		boolean necesitaAutorizacion=false;
		if(success)
		{
			try
			{
				LinkedHashMap<String,Object>paramValidaEdadAsegu=new LinkedHashMap<String,Object>();
				paramValidaEdadAsegu.put("param1", cdunieco);
				paramValidaEdadAsegu.put("param2", cdramo);
				paramValidaEdadAsegu.put("param3", estado);
				paramValidaEdadAsegu.put("param4", nmpoliza);
				paramValidaEdadAsegu.put("param5", "0");
				List<Map<String,String>> listaAseguradosEdadInvalida = consultasManager.consultaDinamica(
						ObjetoBD.VALIDA_EDAD_ASEGURADOS, paramValidaEdadAsegu);
				
				if(listaAseguradosEdadInvalida.size()>0)
				{
					necesitaAutorizacion=true;
					panel1.put("necesitaAutorizacion" , "S");
					mensajeRespuesta = "La p\u00f3liza se envi\u00f3 a autorizaci\u00f3n debido a que:<br/>";
					for(Map<String,String>iAseguradoEdadInvalida:listaAseguradosEdadInvalida)
					{
						mensajeRespuesta = mensajeRespuesta + iAseguradoEdadInvalida.get("NOMBRE");
						if(iAseguradoEdadInvalida.get("SUPERAMINI").substring(0, 1).equals("-"))
						{
							mensajeRespuesta = mensajeRespuesta + " no llega a la edad de "+iAseguradoEdadInvalida.get("EDADMINI")+" a&ntilde;os<br/>";
						}
						else
						{
							mensajeRespuesta = mensajeRespuesta + " supera la edad de "+iAseguradoEdadInvalida.get("EDADMAXI")+" a&ntilde;os<br/>";
						}
					}
		        	
		        	RespuestaTurnadoVO despacho = despachadorManager.turnarTramite(
		        	        cdusuari,
		        	        cdsisrol,
		        	        ntramite,
		        	        EstatusTramite.EN_ESPERA_DE_AUTORIZACION.getCodigo(),
		        	        mensajeRespuesta.replaceAll("<br/>", "\n").replaceAll("&ntilde;", "\u00f1"),
		        	        null,  // cdrazrecha
		        	        null,  // cdusuariDes
		        	        null,  // cdsisrolDes
		        	        true,  // permisoAgente
		        	        false, // porEscalamiento,
		        	        fechaHoy,
		        	        false  // sinGrabarDetalle
		        	        );
		        	
		        	mensajeRespuesta = Utils.join(mensajeRespuesta, ". ", despacho.getMessage());
		        	
					success = false;
				}
			}
			catch(Exception ex)
			{
				logger.error("error al validar la edad de los asegurados",ex);
				mensajeRespuesta = ex.getMessage();
				success          = false;
			}
		}
		
		if(success&&!necesitaAutorizacion)
		{
			try
			{
				boolean cuadroNatural=cotizacionManager.validarCuadroComisionNatural(cdunieco,cdramo,estado,nmpoliza);
				if(!cuadroNatural)
				{
					necesitaAutorizacion=true;
					panel1.put("necesitaAutorizacion" , "S");
					mensajeRespuesta = "La p\u00f3liza se envi\u00f3 a autorizaci\u00f3n debido a que se cambio el cuadro de comisiones";
		        	
		        	RespuestaTurnadoVO despacho = despachadorManager.turnarTramite(
		        	        cdusuari,
		        	        cdsisrol,
		        	        ntramite,
		        	        EstatusTramite.EN_ESPERA_DE_AUTORIZACION.getCodigo(),
		        	        mensajeRespuesta,
		        	        null,  // cdrazrecha
		        	        null,  // cdusuariDes
		        	        null,  // cdsisrolDes
		        	        true,  // permisoAgente
		        	        false, // porEscalamiento
		        	        fechaHoy,
		        	        false  // sinGrabarDetalle
		        	        );
		        	
		        	mensajeRespuesta = Utils.join(mensajeRespuesta, ". ", despacho.getMessage());
		        	
					success = false;
				}
			}
			catch(Exception ex)
			{
				logger.error("error al validar cuadro de comision natural",ex);
				mensajeRespuesta = ex.getMessage();
				success          = false;
			}
		}
		
		////// obtener forma de pago
		if(success)
		{
			try
			{
				Map<String,String>paramObtenerPoliza=new LinkedHashMap<String,String>(0);
				paramObtenerPoliza.put("pv_cdunieco" , cdunieco);
				paramObtenerPoliza.put("pv_cdramo"   , cdramo);
				paramObtenerPoliza.put("pv_estado"   , estado);
				paramObtenerPoliza.put("pv_nmpoliza" , nmpoliza);
				paramObtenerPoliza.put("pv_cdusuari" , cdusuari);
				Map<String,Object>polizaCompleta=kernelManager.getInfoMpolizasCompleta(paramObtenerPoliza);
				logger.debug("poliza a emitir: "+polizaCompleta);
				cdperpag = (String)polizaCompleta.get("cdperpag");
			}
			catch(Exception ex)
			{
				logger.error("error al obtener los datos completos de la poliza",ex);
				mensajeRespuesta = ex.getMessage();
				success          = false;
			}
		}
		
		////// emision
		if(success)
		{
			try
			{
				Map<String, String> respTvalopol= new HashMap<String, String>();
				respTvalopol= consultasPolizaManager.obtieneTvalopol(cdunieco, cdramo, estado, nmpoliza);//Rescatamos RPF
				Map<String,Object>paramEmi=new LinkedHashMap<String,Object>(0);
				paramEmi.put("pv_cdusuari"  , cdusuari);
				paramEmi.put("pv_cdunieco"  , cdunieco);
				paramEmi.put("pv_cdramo"    , cdramo);
				paramEmi.put("pv_estado"    , estado);
				paramEmi.put("pv_nmpoliza"  , nmpoliza);
				paramEmi.put("pv_nmsituac"  , "1");
				paramEmi.put("pv_nmsuplem"  , "0");
				paramEmi.put("pv_cdelement" , cdelemen); 
				paramEmi.put("pv_cdcia"     , cdunieco);
				paramEmi.put("pv_cdplan"    , null);
				paramEmi.put("pv_cdperpag"  , cdperpag);
				paramEmi.put("pv_cdperson"  , cdpersonSesion);
				paramEmi.put("pv_fecha"     , new Date());
				paramEmi.put("pv_ntramite"  , ntramite);
				mx.com.aon.portal.util.WrapperResultados wr=kernelManager.emitir(paramEmi);
				
				//Parche para no perder RPF hasta modificar SP
				try {
					Map<String, String> respTvalopolEmitidoSinRPF= new HashMap<String, String>();
					respTvalopolEmitidoSinRPF= consultasPolizaManager.obtieneTvalopol(cdunieco, cdramo, "M", wr.getItemMap().get("nmpoliza")+"");//Rescatamos RPF
					respTvalopolEmitidoSinRPF.put("parametros.pv_otvalor17", StringUtils.isBlank(respTvalopol.get("parametros.pv_otvalor17"))?respTvalopolEmitidoSinRPF.get("parametros.pv_otvalor17"):respTvalopol.get("parametros.pv_otvalor17"));
					kernelManager.pMovTvalopol(respTvalopolEmitidoSinRPF);
				} catch (Exception e) {
					logger.error("Error al grabar tavolopol con valor RPF");
				}
				
				logger.debug("emision obtenida "+wr.getItemMap());
				
				nmpolizaEmitida = (String)wr.getItemMap().get("nmpoliza");
				this.nmpoliza   = nmpolizaEmitida;
				nmpoliexEmitida = (String)wr.getItemMap().get("nmpoliex");
				nmsuplemEmitida = (String)wr.getItemMap().get("nmsuplem");
				this.nmsuplem   = nmsuplemEmitida;
				esDxN           = (String)wr.getItemMap().get("esdxn");
				cdIdeperRes     = (String)wr.getItemMap().get("CDIDEPER");
				this.cdIdeper   =  cdIdeperRes;
				
				panel2.put("nmpoliza",nmpolizaEmitida);
				panel2.put("nmpoliex",nmpoliexEmitida);
				
				try
	            {
	            	serviciosManager.grabarEvento(new StringBuilder("\nEmision")
	            	    ,Constantes.MODULO_EMISION  //cdmodulo
	            	    ,Constantes.EVENTO_EMISION  //cdevento
	            	    ,new Date() //fecha
	            	    ,cdusuari
	            	    ,((UserVO)session.get("USUARIO")).getRolActivo().getClave()
	            	    ,ntramite
	            	    ,cdunieco
	            	    ,cdramo
	            	    ,"M"
	            	    ,nmpolizaEmitida
	            	    ,nmpoliza
	            	    ,null
	            	    ,null
	            	    ,null);
	            }
	            catch(Exception ex)
	            {
	            	logger.error("Error al grabar evento, sin impacto",ex);
	            }
			}
			catch(Exception ex)
			{
				logger.error("error en el pl de emitir",ex);
				mensajeRespuesta = ex.getMessage();
				success          = false;
			}
		}
		
		rutaCarpeta=this.rutaDocumentosPoliza+"/"+ntramite;
		
		////// ws cliente y recibos
		if(success)
		{
					String _cdunieco = cdunieco;
					String _cdramo   = cdramo;
					String edoPoliza = "M";
					String _nmpoliza = nmpolizaEmitida;
					String _nmsuplem = nmsuplemEmitida;
					String sucursal = cdunieco;
					if("1".equals(sucursal))
					{
						sucursal = "1000";
					}
					logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>> Parametros para WS de cliente, Recibos y Autos: <<<<<<<<<<<<<<<<<<<<<<< ");
					logger.debug(">>>>>>>>>> cdunieco: "   + _cdunieco);
					logger.debug(">>>>>>>>>> cdramo: "     + _cdramo);
					logger.debug(">>>>>>>>>> estado: "     + edoPoliza);
					logger.debug(">>>>>>>>>> nmpoliza: "   + _nmpoliza);
					logger.debug(">>>>>>>>>> suplemento: " + _nmsuplem);
					logger.debug(">>>>>>>>>> sucursal: "   + sucursal);
					logger.debug(">>>>>>>>>> nmsolici: "   + nmpoliza);
					logger.debug(">>>>>>>>>> nmtramite: "  + ntramite);
					
					//ws cliente
					
					if(Ramo.AUTOS_FRONTERIZOS.getCdramo().equalsIgnoreCase(cdramo) 
				    		|| Ramo.SERVICIO_PUBLICO.getCdramo().equalsIgnoreCase(cdramo)
				    	)
					{
						if(StringUtils.isBlank(cdIdeperRes)){
							
							ClienteGeneralRespuesta resCli = ice2sigsService.ejecutaWSclienteGeneral(_cdunieco, _cdramo, edoPoliza, _nmpoliza, _nmsuplem, ntramite, null, Ice2sigsService.Operacion.INSERTA, null, us, false);
							
							if(resCli != null && Ice2sigsService.Estatus.EXITO.getCodigo() == resCli.getCodigo() && ArrayUtils.isNotEmpty(resCli.getClientesGeneral())){
								cdIdeperRes = resCli.getClientesGeneral()[0].getNumeroExterno();
								if(StringUtils.isNotBlank(cdIdeperRes) && !cdIdeperRes.equalsIgnoreCase("0") && !cdIdeperRes.equalsIgnoreCase("0L")){
								
									HashMap<String, String> paramsIdeper =  new HashMap<String, String>();
									paramsIdeper.put("pv_cdunieco_i", _cdunieco);
									paramsIdeper.put("pv_cdramo_i",   _cdramo);
									paramsIdeper.put("pv_estado_i",   edoPoliza);
									paramsIdeper.put("pv_nmpoliza_i", _nmpoliza);
									paramsIdeper.put("pv_nmsuplem_i", _nmsuplem);
									paramsIdeper.put("pv_cdideper_i", cdIdeperRes);
									
									kernelManager.actualizaCdIdeper(paramsIdeper);
									
									this.cdIdeper = cdIdeperRes;
											
								}else {
									success = false;
									retryWS = true;
									mensajeRespuesta = "Error al crear Cliente en WS, no se pudo obtener el numero de Cliente";
									logger.error("Error al crear Cliente en WS, no se pudo obtener el numero de Cliente, respondio: "+ cdIdeperRes);
								} 
							}else{
								success = false;
								retryWS = true;
								mensajeRespuesta = "Error al crear Cliente en WS.";
								logger.error("Error al Crear el cliente en WS!, Datos Nulos");
							}
						}
					}
						
					////// ws de cotizacion y emision para autos
					if(success
							&& (Ramo.AUTOS_FRONTERIZOS.getCdramo().equalsIgnoreCase(cdramo) 
						    		|| Ramo.SERVICIO_PUBLICO.getCdramo().equalsIgnoreCase(cdramo)
						    		|| Ramo.AUTOS_RESIDENTES.getCdramo().equalsIgnoreCase(cdramo)
						    	)
							)
					{
						EmisionAutosVO aux=null;
						try{
							
							aux = emisionAutosService.cotizaEmiteAutomovilWS(cdunieco, cdramo,
							edoPoliza, nmpolizaEmitida, nmsuplemEmitida, ntramite,cdtipsit , us);
						
							success = aux!=null && StringUtils.isNotBlank(aux.getNmpoliex()) && !"0".equals(aux.getNmpoliex());
							retryWS = !success;
						}catch(Exception ex)
						{
							logger.error("error en el pl de emitir",ex);
							mensajeRespuesta = ex.getMessage()!=null?ex.getMessage().trim().equals("")?"Error en el Web Service para emitir":ex.getMessage():"Error en el Web Service para emitir";
							success          = false;
						}
						
						
						if(success)
						{
							logger.debug("Emision de Auto en WS Exitosa, Numero de Poliza: " + aux.getNmpoliex());
							this.nmpolAlt = aux.getNmpoliex();
							this.sucursalGS = aux.getSucursal();
							panel2.put("nmpoliex", this.nmpolAlt);
							cdRamoGS = aux.getSubramo();
							
							//Insetar Poliza Externa WS Auto
							try{
								HashMap<String, String> paramsInsertaPolAlt = new HashMap<String, String>();
								paramsInsertaPolAlt.put("pv_cdunieco_i", cdunieco);
								paramsInsertaPolAlt.put("pv_cdramo_i"  ,   cdramo);
								paramsInsertaPolAlt.put("pv_estado_i"  ,   edoPoliza);
								paramsInsertaPolAlt.put("pv_nmpoliza_i", nmpolizaEmitida);
								paramsInsertaPolAlt.put("pv_nmsuplem_i", nmsuplemEmitida);
								paramsInsertaPolAlt.put("pv_nmpoliex_i", this.nmpolAlt);
								paramsInsertaPolAlt.put("pv_cduniext_i", this.sucursalGS);
								paramsInsertaPolAlt.put("pv_ramo_i"    , cdRamoGS);
								kernelManager.actualizaPolizaExterna(paramsInsertaPolAlt);
								
							}catch(Exception e){
								logger.error("Error al Insertar Poliza Externa: " + e.getMessage(), e);
								mensajeRespuesta = "Error al insertar Poliza Externa: " + this.nmpolAlt;
								success = false;
							}
							
							if(!aux.isExitoRecibos()){
								//this.retryRec = true;  //Ya no aplica
								success =  false;
							}
							
						}else {
							if(StringUtils.isBlank(mensajeRespuesta))
								mensajeRespuesta = "Error en el Web Service de emisi\u00f3n. No se pudo emitir la p\u00f3liza";
						}
						
						if(!success){
							logger.warn("No se ha emitido correctamente la poliza en el Web Service de Autos, Se revierte la emision en ICE");
							emisionManager.revierteEmision(_cdunieco, _cdramo, edoPoliza, _nmpoliza, _nmsuplem);
						}
						
					}
					
					//ws recibos
					if( success && (cdtipsit.equalsIgnoreCase(TipoSituacion.SALUD_VITAL.getCdtipsit())
							||cdtipsit.equalsIgnoreCase(TipoSituacion.SALUD_NOMINA.getCdtipsit())
							||cdtipsit.equalsIgnoreCase(TipoSituacion.MULTISALUD.getCdtipsit())
							||cdtipsit.equalsIgnoreCase(TipoSituacion.RECUPERA_INDIVIDUAL.getCdtipsit())
							||cdtipsit.equalsIgnoreCase(TipoSituacion.GASTOS_MEDICOS_INDIVIDUAL.getCdtipsit())
							))
					{
						try
						{
							if(StringUtils.isNotBlank(esDxN) && "S".equalsIgnoreCase(esDxN))
							{	
								// Ejecutamos el Web Service de Recibos:
								ice2sigsService.ejecutaWSrecibos(_cdunieco, _cdramo,
										edoPoliza, _nmpoliza, 
										_nmsuplem, rutaCarpeta,
										sucursal, nmpoliza, ntramite, 
										false, tipoMov,
										us);
								// Ejecutamos el Web Service de Recibos DxN:
								recibosSigsService.generaRecibosDxN(_cdunieco, _cdramo, edoPoliza, _nmpoliza, _nmsuplem, sucursal, nmpoliza, ntramite, us);
							}
							else
							{
								// Ejecutamos el Web Service de Recibos:
								ice2sigsService.ejecutaWSrecibos(_cdunieco, _cdramo,
										edoPoliza, _nmpoliza, 
										_nmsuplem, rutaCarpeta,
										sucursal, nmpoliza,ntramite, 
										true, tipoMov,
										us);
							}
						}
						catch(Exception ex)
						{
							logger.error("error al lanzar ws recibos",ex);
							mensajeRespuesta = ex.getMessage();
							success          = false;
						}
					}
		}
				
		////// crear carpeta para los documentos
		/*
		if(success)
		{
            File carpeta = new File(rutaCarpeta);
            if(!carpeta.exists())
            {
            	logger.debug("no existe la carpeta::: "+rutaCarpeta);
            	carpeta.mkdir();
            	if(carpeta.exists())
            	{
            		logger.debug("carpeta creada");
            	}
            	else
            	{
            		logger.debug("carpeta NO creada");
            		success          = false;
            		mensajeRespuesta = "Error al crear la carpeta de documentos";
            	}
            }
            else
            {
            	logger.debug("existe la carpeta   ::: "+rutaCarpeta);
            }
		}
		*/
		
		////// documentos
		if(success)
		{
			try
			{
				documentosManager.generarDocumentosParametrizados(
						cdunieco
						,cdramo
						,"M" //estado
						,nmpolizaEmitida
						,"0" //nmsituac
						,nmsuplemEmitida
						,DocumentosManager.PROCESO_EMISION //proceso
						,ntramite
						,nmpoliza //nmsolici
						,null
						);
				
				/*
				String cdorddoc = emisionManager.insercionDocumentosParametrizados(
	            		cdunieco
	            		,cdramo
	            		,"M"
	            		,nmpolizaEmitida
	            		,"0"
	            		,nmsuplemEmitida
	            		,"EMISION"
	            		);
	            logger.debug("cdorddoc: {}",cdorddoc);
	            
	            List<Map<String,String>> docsATransferir = cotizacionManager.generarDocumentosBaseDatos(
	            		cdorddoc
	            		,nmpoliza
	            		,ntramite
	            		);
	            
	            String rutaDocsBaseDatos = consultasManager.recuperarTparagen(ParametroGeneral.DIRECTORIO_REPORTES);
	            logger.debug(Utils.join("\nrutaDocsBaseDatos:",rutaDocsBaseDatos));
	            
	            for(Map<String,String>doc:docsATransferir)
	            {
	            	try
	            	{
	            		String origen  = Utils.join(rutaDocsBaseDatos,doc.get("CDDOCUME"));
	            		String destino = Utils.join(rutaDocumentosPoliza,"/",ntramite,"/",doc.get("CDDOCUME"));
	            		logger.debug(Utils.log("\nIntentando mover desde:",origen,",hacia:",destino));
	            		FileUtils.moveFile(
	            				new File(origen)
	            				,new File(destino)
	            				);
	            	}
	            	catch(Exception ex)
	            	{
	            		logger.error("Error al transferir archivo ",ex);
	            	}
	            }
	            */
				
				/*
				List<Map<String,String>>listaDocu=kernelManager.obtenerListaDocumentos(
						cdunieco
						,cdramo
						,"M"
						,nmpolizaEmitida
						,nmsuplemEmitida
						,ntramite
						);
				
				//listaDocu contiene: nmsolici,nmsituac,descripc,descripl
				for(Map<String,String> docu:listaDocu)
				{
					logger.debug("docu iterado: "+docu);
					String descripc=docu.get("descripc");
					String descripl=docu.get("descripl");
					String url=this.rutaServidorReports
							+ "?destype=cache"
							+ "&desformat=PDF"
							+ "&userid="+this.passServidorReports
							+ "&report="+descripl
							+ "&paramform=no"
							+ "&ACCESSIBLE=YES" //parametro que habilita salida en PDF
							+ "&p_unieco="+cdunieco
							+ "&p_ramo="+cdramo
							+ "&p_estado='M'"
							+ "&p_poliza="+nmpolizaEmitida
							+ "&p_suplem="+nmsuplemEmitida
							+ "&desname="+rutaCarpeta+"/"+descripc;
					if(descripc.substring(0, 6).equalsIgnoreCase("CREDEN"))
					{
						// C R E D E N C I A L _ X X X X X X . P D F
						//0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
						url+="&p_cdperson="+descripc.substring(11, descripc.lastIndexOf("."));
					}
					logger.debug(Utils.log(
							 "\n#################################"
							,"\n###### Se solicita reporte ######"
							+ "\na "+url);
					HttpUtil.generaArchivo(url,rutaCarpeta+"/"+descripc);
					logger.debug(Utils.log(
							,"\n######                    ######"
							,"\n###### reporte solicitado ######"
							 "\n################################"
							+ "");
				}
				*/
				
				/**
				 * Para Guardar URls de Caratula Recibos y documentos de Autos Externas
				 */
				if(Ramo.AUTOS_FRONTERIZOS.getCdramo().equalsIgnoreCase(cdramo) 
			    		|| Ramo.SERVICIO_PUBLICO.getCdramo().equalsIgnoreCase(cdramo)
			    		|| Ramo.AUTOS_RESIDENTES.getCdramo().equalsIgnoreCase(cdramo)
			    	){
					
					String parametros = null;
					
					String urlCaratula = null;
					if(Ramo.AUTOS_FRONTERIZOS.getCdramo().equalsIgnoreCase(cdramo) 
				    		|| Ramo.AUTOS_RESIDENTES.getCdramo().equalsIgnoreCase(cdramo)
				    	){
						urlCaratula = this.caratulaImpresionAutosUrl;
					}else if(Ramo.SERVICIO_PUBLICO.getCdramo().equalsIgnoreCase(cdramo)){
						urlCaratula = this.caratulaImpresionAutosServiciopublicoUrl;
					}
					
					if("C".equalsIgnoreCase(tipoGrupoInciso)){
						urlCaratula = this.caratulaImpresionAutosFlotillasUrl;
					}
					
					String urlRecibo = this.reciboImpresionAutosUrl;
					String urlCaic = this.caicImpresionAutosUrl;
					String urlAeua = this.aeuaImpresionAutosUrl;
					String urlAp = this.apImpresionAutosUrl;
					
					String urlIncisosFlot = this.incisosFlotillasImpresionAutosUrl;
					String urlIncisosExcelFlot = this.incisosFlotillasExcelImpresionAutosUrl;
					String urlTarjIdent = this.tarjetaIdenImpresionAutosUrl;
					String numIncisosReporte = this.numeroIncisosReporte;
					
					String urlDocsExtra = this.caratulaImpresionAutosDocExtra;
					
					this.mensajeEmail = "<span style=\"font-family: Verdana, Geneva, sans-serif;\">"+
										"<br>Estimado(a) cliente,<br/><br/>"+
										"Anexamos a este e-mail la documentaci\u00f3n de la p\u00f3liza de Autom\u00f3viles contratada con GENERAL DE SEGUROS.<br/>"+
										"Para visualizar el documento favor de dar click en el link correspondiente.<br/>";
					
					/**
					 * Para Caratula
					 */
					parametros = "?"+sucursalGS+","+cdRamoGS+","+this.nmpolAlt+",,0";
					logger.debug("URL Generada para Caratula: "+ urlCaratula + parametros);
					this.mensajeEmail += "<br/><br/><a style=\"font-weight: bold\" href=\""+urlCaratula + parametros+"\">Car\u00e1tula de p\u00f3liza</a>";
					
					documentosManager.guardarDocumento(
							cdunieco
							,cdramo
							,"M"
							,nmpolizaEmitida
							,nmsuplemEmitida
							,new Date()
							,urlCaratula + parametros
							,"Car\u00e1tula de P\u00f3liza"
							,nmpoliza
							,ntramite
							,TipoEndoso.EMISION_POLIZA.getCdTipSup().toString()
							,Constantes.SI
							,null
							,TipoTramite.POLIZA_NUEVA.getCdtiptra()
							,"0"
							,Documento.EXTERNO_CARATULA
							,null
							,null, false
							);
					
					
					/**
					 * Para Recibos
					 */
					String visible = null;
					HashMap<String,String> imprimir = new HashMap<String, String>(); 
					
					List<Map<String,String>> recibos = consultasPolizaManager.obtieneRecibosPolizaAuto(cdunieco, cdramo, "M", nmpolizaEmitida, nmsuplemEmitida);
					
					if(recibos!= null && !recibos.isEmpty()){
						for(Map<String,String> reciboIt : recibos){
							
							/**
							 * Si el Recibo Tiene estatus 1 se guarda en tdocupol como documento de la poliza, excepto algunos endosos como el de forma de pago,
							 * donde se generan recibos negativos para cancelar y esos no deben de guardarse, estos casos el estatus es distinto de 1
							 */
							if(!"1".equals(reciboIt.get("CDESTADO"))) continue;
							
							String llave = reciboIt.get("TIPEND") + reciboIt.get("NUMEND");
							
							if(!imprimir.containsKey(llave)){
								visible = Constantes.SI;
								imprimir.put(llave, reciboIt.get("NUMREC"));
							}else{
								visible = Constantes.NO;
							}
							
							//parametros = "?9999,0,"+sucursalGS+","+cdRamoGS+","+this.nmpolAlt+",0,0,,"+reciboIt.get("NUMREC"); // PARAMS RECIBO ANTERIORES
							parametros = "?"+sucursalGS+","+cdRamoGS+","+this.nmpolAlt+",,0,"+reciboIt.get("NUMREC");
							
							logger.debug("URL Generada para Recibo "+reciboIt.get("NUMREC")+": "+ urlRecibo + parametros);
							
							if(Constantes.SI.equalsIgnoreCase(visible)){
								this.mensajeEmail += "<br/><br/><a style=\"font-weight: bold\" href=\""+urlRecibo + parametros+"\">Recibo provisional de primas</a>";
							}
							
							documentosManager.guardarDocumento(
									cdunieco
									,cdramo
									,"M"
									,nmpolizaEmitida
									,nmsuplemEmitida
									,new Date()
									,urlRecibo + parametros
									,"Recibo "+reciboIt.get("NUMREC")
									,nmpoliza
									,ntramite
									,TipoEndoso.EMISION_POLIZA.getCdTipSup().toString()
									,visible
									,null
									,TipoTramite.POLIZA_NUEVA.getCdtiptra()
									,"0"
									,Documento.RECIBO
									,null
									,null, false
									);
						}
					}
					
					
					boolean imprimirCaic = false;
					boolean imprimirAeua = false;
					boolean imprimirAP = false;
					
					List<Map<String,String>> listaEndosos = emisionAutosService.obtieneEndososImprimir(cdunieco, cdramo, "M", nmpolizaEmitida, nmsuplemEmitida);
					
					if(listaEndosos!= null && !listaEndosos.isEmpty()){
						Map<String,String> emision = listaEndosos.get(0);
						if(emision != null && emision.containsKey("CAIC") && Constantes.SI.equalsIgnoreCase(emision.get("CAIC"))){
							imprimirCaic = true;
						}
						if(emision != null && emision.containsKey("AP") && Constantes.SI.equalsIgnoreCase(emision.get("AP"))){
							imprimirAP = true;
						}
						if(emision != null && emision.containsKey("AEUA") && Constantes.SI.equalsIgnoreCase(emision.get("AEUA"))){
							imprimirAeua = true;
						}
					}
					
					/**
					 * Para AP inciso 1
					 */
					if(imprimirAP){
						parametros = "?"+sucursalGS+","+cdRamoGS+","+this.nmpolAlt+",,0,0";
						logger.debug("URL Generada para AP Inciso 1: "+ urlAp + parametros);
						this.mensajeEmail += "<br/><br/><a style=\"font-weight: bold\" href=\""+urlAp + parametros+"\">Anexo cobertura de AP</a>";
						
						documentosManager.guardarDocumento(
								cdunieco
								,cdramo
								,"M"
								,nmpolizaEmitida
								,nmsuplemEmitida
								,new Date()
								,urlAp + parametros
								,"AP"
								,nmpoliza
								,ntramite
								,TipoEndoso.EMISION_POLIZA.getCdTipSup().toString()
								,Constantes.SI
								,null
								,TipoTramite.POLIZA_NUEVA.getCdtiptra()
								,"0"
								,Documento.EXTERNO_AP
								,null
								,null, false
								);
					}
					
					if(imprimirCaic){
						/**
						 * Para CAIC inciso 1
						 */
						parametros = "?"+sucursalGS+","+cdRamoGS+","+this.nmpolAlt+",,0,0";
						logger.debug("URL Generada para CAIC Inciso 1: "+ urlCaic + parametros);
						this.mensajeEmail += "<br/><br/><a style=\"font-weight: bold\" href=\""+urlCaic + parametros+"\">Anexo de cobertura RC USA</a>";
						
						documentosManager.guardarDocumento(
								cdunieco
								,cdramo
								,"M"
								,nmpolizaEmitida
								,nmsuplemEmitida
								,new Date()
								,urlCaic + parametros
								,"CAIC"
								,nmpoliza
								,ntramite
								,TipoEndoso.EMISION_POLIZA.getCdTipSup().toString()
								,Constantes.SI
								,null
								,TipoTramite.POLIZA_NUEVA.getCdtiptra()
								,"0"
								,Documento.EXTERNO_CAIC
								,null
								,null, false
								);
					}

					if(imprimirAeua){
						/**
						 * Para AEUA inciso 1
						 */
						parametros = "?"+sucursalGS+","+cdRamoGS+","+this.nmpolAlt+",,0,0";
						logger.debug("URL Generada para AEUA Inciso 1: "+ urlAeua + parametros);
						this.mensajeEmail += "<br/><br/><a style=\"font-weight: bold\" href=\""+urlAeua + parametros+"\">Asistencia en Estados Unidos y Canad\u00E1</a>";
						
						documentosManager.guardarDocumento(
								cdunieco
								,cdramo
								,"M"
								,nmpolizaEmitida
								,nmsuplemEmitida
								,new Date()
								,urlAeua + parametros
								,"Asistencia en Estados Unidos y Canad\u00E1"
								,nmpoliza
								,ntramite
								,TipoEndoso.EMISION_POLIZA.getCdTipSup().toString()
								,Constantes.SI
								,null
								,TipoTramite.POLIZA_NUEVA.getCdtiptra()
								,"0"
								,Documento.EXTERNO_AEUA
								,null
								,null, false
								);
					}
					
					if("C".equalsIgnoreCase(tipoGrupoInciso)){
						/**
						 * Para Incisos Flotillas
						 */
						parametros = "?"+sucursalGS+","+cdRamoGS+","+this.nmpolAlt+",,0";
						logger.debug("URL Generada para urlIncisosFlotillas: "+ urlIncisosFlot + parametros);
						this.mensajeEmail += "<br/><br/><a style=\"font-weight: bold\" href=\""+urlIncisosFlot + parametros+"\">Relaci\u00f3n de Incisos Flotillas</a>";
						
						documentosManager.guardarDocumento(
								cdunieco
								,cdramo
								,"M"
								,nmpolizaEmitida
								,nmsuplemEmitida
								,new Date()
								,urlIncisosFlot + parametros
								,"Incisos Flotillas"
								,nmpoliza
								,ntramite
								,TipoEndoso.EMISION_POLIZA.getCdTipSup().toString()
								,Constantes.SI
								,null
								,TipoTramite.POLIZA_NUEVA.getCdtiptra()
								,"0"
								,Documento.EXTERNO_INCISOS_FLOTILLAS
								,null
								,null, false
								);

						/**
						 * Para Incisos Flotillas EXCEL
						 */
						
						if(StringUtils.isNotBlank(urlIncisosExcelFlot)){
							parametros = "?"+sucursalGS+","+cdRamoGS+","+this.nmpolAlt+",,0,0";
							logger.debug("URL Generada para urlIncisosEXCELFlotillas: "+ urlIncisosExcelFlot + parametros);
							this.mensajeEmail += "<br/><br/><a style=\"font-weight: bold\" href=\""+urlIncisosExcelFlot + parametros+"\">Relaci\u00f3n de Incisos EXCEL</a>";
							
							documentosManager.guardarDocumento(
									cdunieco
									,cdramo
									,"M"
									,nmpolizaEmitida
									,nmsuplemEmitida
									,new Date()
									,urlIncisosExcelFlot + parametros
									,"Incisos EXCEL"
									,nmpoliza
									,ntramite
									,TipoEndoso.EMISION_POLIZA.getCdTipSup().toString()
									,Constantes.SI
									,null
									,TipoTramite.POLIZA_NUEVA.getCdtiptra()
									,"0"
									,Documento.EXTERNO_INCISOS_FLOTILLAS
									,null
									,null, false
									);
						}
						
						/**
						 * Para Tarjeta Identificacion
						 */
						
						int numeroIncisos = consultasPolizaManager.obtieneNumeroDeIncisosPoliza(cdunieco, cdramo, "M", nmpolizaEmitida, nmsuplemEmitida);
						
						if(numeroIncisos > 0 ){
							int numeroReportes =  numeroIncisos/Integer.parseInt(numIncisosReporte);
							int reporteSobrante = numeroIncisos % Integer.parseInt(numIncisosReporte);
							
							logger.debug("Tarjeta de Identificacion ::: Numero de Reportes exactos: "+ numeroReportes);
							logger.debug("Tarjeta de Identificacion ::: Numero de incisos sobrantes: "+ reporteSobrante);
							
							if(reporteSobrante > 0 ){
								numeroReportes += 1;
							}
							
							/**
							 * Se divide reporte de tarjeta de identifiacion para flotillas ya que puede ser muy grande el archivo y se divide en una cantidad
							 * de autos por pagina predeterminada.
							 */
							for(int numReporte = 1; numReporte <= numeroReportes; numReporte++){
								
								int desdeInciso = ((numReporte-1) * Integer.parseInt(numIncisosReporte))+1;
								int hastaInciso = numReporte * Integer.parseInt(numIncisosReporte);
								
								if(numReporte == numeroReportes && reporteSobrante > 0 ){
									hastaInciso = ((numReporte-1) * Integer.parseInt(numIncisosReporte)) + reporteSobrante;
								}
								
								parametros = "?"+sucursalGS+","+cdRamoGS+","+this.nmpolAlt+",,0,"+desdeInciso+","+hastaInciso;
								logger.debug("URL Generada para Tarjeta Identificacion: "+ urlTarjIdent + parametros);
								this.mensajeEmail += "<br/><br/><a style=\"font-weight: bold\" href=\""+urlTarjIdent + parametros+"\">Tarjeta de Identificaci\u00f3n. " +desdeInciso+" - " + hastaInciso + " de "+ numeroIncisos+"</a>";
								
								documentosManager.guardarDocumento(
										cdunieco
										,cdramo
										,"M"
										,nmpolizaEmitida
										,nmsuplemEmitida
										,new Date()
										,urlTarjIdent + parametros
										,"Tarjeta de Identificacion. " +desdeInciso+" - " + hastaInciso + " de "+ numeroIncisos 
										,nmpoliza
										,ntramite
										,TipoEndoso.EMISION_POLIZA.getCdTipSup().toString()
										,Constantes.SI
										,null
										,TipoTramite.POLIZA_NUEVA.getCdtiptra()
										,"0"
										,Documento.EXTERNO_TARJETA_IDENTIFICACION
										,null
										,null, false
										);
							}
							
						}
						
					}
					
					/**
					 * TODO: Datos Temporales, quitar cuando las caratulas de autos ya tengan la informacion completa
					 */
					
					PolizaAseguradoVO datosPol = new PolizaAseguradoVO();
					
					datosPol.setCdunieco(cdunieco);
					datosPol.setCdramo(cdramo);
					datosPol.setEstado("M");
					datosPol.setNmpoliza(nmpolizaEmitida);
			
					List<PolizaDTO> listaPolizas = consultasPolizaManager.obtieneDatosPoliza(datosPol);
					PolizaDTO polRes = listaPolizas.get(0);
					
					boolean reduceGS = (StringUtils.isNotBlank(polRes.getReduceGS()) && Constantes.SI.equalsIgnoreCase(polRes.getReduceGS()))?true:false;
					boolean gestoria = (StringUtils.isNotBlank(polRes.getGestoria()) && Constantes.SI.equalsIgnoreCase(polRes.getGestoria()))?true:false;
					boolean cobVida  = (StringUtils.isNotBlank(polRes.getCobvida()) && Constantes.SI.equalsIgnoreCase(polRes.getCobvida()))?true:false;
					
					if(reduceGS){
						/**
						 * Para cobertura de reduce GS
						 */
						
						this.mensajeEmail += "<br/><br/><a style=\"font-weight: bold\" href=\""+this.manualAgenteTxtinfocobredgs+"\">Reduce GS</a>";
						
						documentosManager.guardarDocumento(
								cdunieco
								,cdramo
								,"M"
								,nmpolizaEmitida
								,nmsuplemEmitida
								,new Date()
								,this.manualAgenteTxtinfocobredgs
								,"Reduce GS"
								,nmpoliza
								,ntramite
								,TipoEndoso.EMISION_POLIZA.getCdTipSup().toString()
								,Constantes.SI
								,null
								,TipoTramite.POLIZA_NUEVA.getCdtiptra()
								,"0"
								,Documento.EXTERNO_REDUCE_GS
								,null
								,null, false
								);
					}
					if(gestoria){
						/**
						 * Para cobertura de gestoria GS
						 */
						
						this.mensajeEmail += "<br/><br/><a style=\"font-weight: bold\" href=\""+this.manualAgenteTxtinfocobgesgs+"\">Gestoria GS</a>";
						
						documentosManager.guardarDocumento(
								cdunieco
								,cdramo
								,"M"
								,nmpolizaEmitida
								,nmsuplemEmitida
								,new Date()
								,this.manualAgenteTxtinfocobgesgs
								,"Gestoria GS"
								,nmpoliza
								,ntramite
								,TipoEndoso.EMISION_POLIZA.getCdTipSup().toString()
								,Constantes.SI
								,null
								,TipoTramite.POLIZA_NUEVA.getCdtiptra()
								,"0"
								,Documento.EXTERNO_GESTORIA_GS
								,null
								,null, false
								);
					}
					
					if(cobVida){
						/**
						 * Para cobertura de Vida
						 */
						String reporteEspVida = this.getText("rdf.emision.nombre.esp.cobvida");
						String pdfEspVidaNom = "SOL_VIDA_AUTO.pdf";
						
						String url=this.rutaServidorReports
								+ "?destype=cache"
								+ "&desformat=PDF"
								+ "&userid="+this.passServidorReports
								+ "&report="+reporteEspVida
								+ "&paramform=no"
								+ "&ACCESSIBLE=YES" //parametro que habilita salida en PDF
								+ "&p_unieco="+cdunieco
								+ "&p_ramo="+cdramo
								+ "&p_estado='M'"
								+ "&p_poliza="+nmpolizaEmitida
								+ "&p_suplem="+nmsuplemEmitida
								+ "&desname="+rutaCarpeta+"/"+pdfEspVidaNom;
						
						HttpUtil.generaArchivo(url,rutaCarpeta+"/"+pdfEspVidaNom);
						
						
						documentosManager.guardarDocumento(
								cdunieco
								,cdramo
								,"M"
								,nmpolizaEmitida
								,nmsuplemEmitida
								,new Date()
								,pdfEspVidaNom
								,"Especificaciones Seguro de Vida"
								,nmpoliza
								,ntramite
								,TipoEndoso.EMISION_POLIZA.getCdTipSup().toString()
								,Constantes.SI
								,null
								,TipoTramite.POLIZA_NUEVA.getCdtiptra()
								,"0"
								,Documento.EXTERNO_ESPECIF_SEGURO_VIDA
								,null
								,null, false
								);

						this.mensajeEmail += "<br/><br/><a style=\"font-weight: bold\" href=\""+this.manualAgenteCondgralescobsegvida+"\">Condiciones Generales Seguro de Vida</a>";
						
						documentosManager.guardarDocumento(
								cdunieco
								,cdramo
								,"M"
								,nmpolizaEmitida
								,nmsuplemEmitida
								,new Date()
								,this.manualAgenteCondgralescobsegvida
								,"Condiciones Generales Seguro de Vida"
								,nmpoliza
								,ntramite
								,TipoEndoso.EMISION_POLIZA.getCdTipSup().toString()
								,Constantes.SI
								,null
								,TipoTramite.POLIZA_NUEVA.getCdtiptra()
								,"0"
								,Documento.EXTERNO_CONDIC_GRALES_SEGURO_VIDA
								,null
								,null, false
								);
					}
					
					if(StringUtils.isNotBlank(urlDocsExtra)){
						/**
						 * Para documento Sanas Practicas
						 */
						parametros = "?"+sucursalGS+","+cdRamoGS+","+this.nmpolAlt+",2";
						logger.debug("URL Generada para Sanas Practicas: "+ urlDocsExtra + parametros);
						this.mensajeEmail += "<br/><br/><a style=\"font-weight: bold\" href=\""+urlDocsExtra + parametros+"\">Sanas Pr\u00e1cticas</a>";
						
						documentosManager.guardarDocumento(
								cdunieco
								,cdramo
								,"M"
								,nmpolizaEmitida
								,nmsuplemEmitida
								,new Date()
								,urlDocsExtra + parametros
								,"Sanas Pr\u00e1cticas"
								,nmpoliza
								,ntramite
								,TipoEndoso.EMISION_POLIZA.getCdTipSup().toString()
								,Constantes.SI
								,null
								,TipoTramite.POLIZA_NUEVA.getCdtiptra()
								,"0"
								,Documento.EXTERNO_DOCUMENTO_EXTRA
								,null
								,null, false
								);
	
						/**
						 * Para documento Constancia de Recepcion
						 */
						parametros = "?"+sucursalGS+","+cdRamoGS+","+this.nmpolAlt+",1";
						logger.debug("URL Generada para Constancia de Recepcion de Documentacion Contractual: "+ urlDocsExtra + parametros);
						this.mensajeEmail += "<br/><br/><a style=\"font-weight: bold\" href=\""+urlDocsExtra + parametros+"\">Constancia de Recepci\u00f3n de Documentaci\u00f3n Contractual</a>";
						
						documentosManager.guardarDocumento(
								cdunieco
								,cdramo
								,"M"
								,nmpolizaEmitida
								,nmsuplemEmitida
								,new Date()
								,urlDocsExtra + parametros
								,"Constancia de Recepci\u00f3n de Documentaci\u00f3n Contractual"
								,nmpoliza
								,ntramite
								,TipoEndoso.EMISION_POLIZA.getCdTipSup().toString()
								,Constantes.SI
								,null
								,TipoTramite.POLIZA_NUEVA.getCdtiptra()
								,"0"
								,Documento.EXTERNO_DOCUMENTO_EXTRA
								,null
								,null, false
								);
					}
					
					// JTEZVA 2016 09 08 Se complementan las ligas con los documentos ice
					this.mensajeEmail += emisionManager.generarLigasDocumentosEmisionLocalesIce(ntramite);
					
					this.mensajeEmail += "<br/><br/><br/>Agradecemos su preferencia.<br/>"+
										 "General de Seguros<br/>"+
										 "</span>";
					
					flujoMesaControlManager.guardarMensajeCorreoEmision(
							ntramite,
							Utils.cambiaAcentosUnicodePorGuionesBajos(mensajeEmail)
					);
					
					mensajeEmail = Utils.cambiaGuionesBajosPorAcentosHtml(Utils.cambiaAcentosUnicodePorGuionesBajos(mensajeEmail));
				}
			}
			catch(Exception ex)
			{
				logger.error("error al generar documentacion de emision",ex);
				mensajeRespuesta = ex.getMessage();
				success          = false;
			}
		}
		
		////// detalle emision
		if(success)
		{
			try
			{
			    RespuestaTurnadoVO despacho = despachadorManager.turnarTramite(
                        cdusuari,
                        cdsisrol,
                        ntramite,
                        EstatusTramite.CONFIRMADO.getCodigo(),
                        "El tr\u00e1mite se emiti\u00f3",
                        null,  // cdrazrecha
                        null,  // cdusuariDes
                        null,  // cdsisrolDes
                        true,  // permiso agente
                        false, // porEscalamiento
                        fechaHoy,
                        false  // sinGrabarDetalle
                        );
			    
				//logger.debug("se inserta detalle nuevo para emision");
	        	/*Map<String,Object>parDmesCon=new LinkedHashMap<String,Object>(0);
	        	parDmesCon.put("pv_ntramite_i"   , ntramite);
	        	parDmesCon.put("pv_feinicio_i"   , new Date());
	        	parDmesCon.put("pv_cdclausu_i"   , null);
	        	parDmesCon.put("pv_comments_i"   , "El tr\u00e1mite se emiti\u00f3");
	        	parDmesCon.put("pv_cdusuari_i"   , cdusuari);
	        	parDmesCon.put("pv_cdmotivo_i"   , null);
	        	parDmesCon.put("pv_cdsisrol_i"   , cdsisrol);
	        	kernelManager.movDmesacontrol(parDmesCon);*/
				/*
				mesaControlManager.movimientoDetalleTramite(
						ntramite
						,new Date()
						,null//cdclausu
						,"El tr\u00e1mite se emiti\u00f3"
						,cdusuari
						,null//cdmotivo
						,cdsisrol
						,"S"//swagente
						,EstatusTramite.CONFIRMADO.getCodigo()
						,true
						);
				*/
			}
			catch(Exception ex)
			{
				logger.error("error al insertar detalle de emision",ex);
				mensajeRespuesta = ex.getMessage();
				success          = false;
			}
		}
		
		/* jtezva 2016 nov 22: ya se manda con el despachador
		if (success) {
			try {
				flujoMesaControlManager.mandarCorreosStatusTramite(ntramite, cdsisrol, false);
			} catch (Exception ex) {
				logger.error("Error al enviar correos de emision", ex);
			}
		}
		*/
		
		//Se actualiza valores en sigs de poliza original y emitida
		if (success) 
        {
            try 
            {
                Map<String,String>parame = siniestrosManager.obtenerTramiteCompleto(ntramite);
                if(!parame.isEmpty() && parame.size()>0 && parame.get("RENPOLIEX")!=null )
                {
                    logger.debug(Utils.log(
                             "\nPoliza extraida del sigs"
                            ,"\n datos originales: ",parame.get("RENUNIEXT"),"/", parame.get("RENRAMO"),"/", parame.get("RENPOLIEX")
                            ,"\n datos renovados : ",cdunieco,"/",cdramo,"/", nmpolizaEmitida
                            ));
                    
//                    try {
                   Map<String, String> infoPoliza = consultasDAO.cargarInformacionPoliza(cdunieco, cdramo, "M", nmpolizaEmitida, cdusuari);
                   consultasPolizaManager.actualizaTramiteEmisionMC(parame.get("RENUNIEXT"), parame.get("RENRAMO"), parame.get("RENPOLIEX"), infoPoliza.get("cduniext"), infoPoliza.get("ramo"), infoPoliza.get("nmpoliex"), us.getUser());
//                    } catch (Exception e) {
//                        mensajeRespuesta = "La poliza ya se habia emitido con anterioridad";
//                    }
                    
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date vInicioVigencia = sdf.parse(infoPoliza.get("feefecto")),
                          vFinVigencia   = sdf.parse(infoPoliza.get("feproren"));
//                    try {
                        Integer IdRenova = consultasPolizaManager.spIdentificaRenovacion(infoPoliza.get("CDUNIEXT"), infoPoliza.get("RAMO"), infoPoliza.get("nmpoliex"),  new Date(), vInicioVigencia, vFinVigencia , parame.get("RENUNIEXT"), parame.get("RENRAMO"), parame.get("RENPOLIEX"));
                        
//                    } catch (Exception e) {
//                        mensajeRespuesta = "La poliza no se logr\u00F3 registrar en el identificador de renovaciones";
//                    }
                }
            } 
            catch (Exception ex) 
            {
//                mensajeRespuesta = "Emitida pero sin registro en sistema sigs";
                logger.error("Error actualizando segrenovaciones_renovada", ex);
            }
        }
		
		logger.debug(
				new StringBuilder()
				.append("\n###### emitir ######")
				.append("\n####################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String autorizaEmision()
	{
		this.session=ActionContext.getContext().getSession();
		logger.debug(Utils.log(
				 "\n#############################"
				,"\n###### autorizaEmision ######"
				,"\n###### panel1=" , panel1
				,"\n###### params=" , params
				,"\n###### flujo="  , flujo
				));
		
		////// variables
		success                 = true;
		retryWS                 = false;
		String ntramiteAut      = null;
		String cdunieco         = null;
		String cdramo           = null;
		String estado           = "W";
		String nmpoliza         = null;
		String ntramite         = null;
		String cdusuari         = null;
		String cdelemen         = null;
		String cdperson         = null;
		String comentarios      = null;
		String fechaEmision     = null;
		Date   fechaEmisionDate = null;
		Date   fechaHoy         = new Date();
		String cdperpag         = null;
		UserVO us               = null;
		String nmpolizaEmitida  = null;
		String nmpoliexEmitida  = null;
		String nmsuplemEmitida  = null;
		String esDxN            = null;
		String cdIdeperRes      = null;
		String tipoMov          = TipoTramite.POLIZA_NUEVA.getCdtiptra();
		String rutaCarpeta      = null;
		String swagente         = "N";
		String cdsisrol         = null;
		
		boolean procesoFlujo = false;
		
		if(flujo!=null)
		{
			procesoFlujo = true;
			
			try
			{
				us = Utils.validateSession(session);
				
				ntramiteAut = null;
				ntramite    = flujo.getNtramite();
				
				Map<String,Object> datos   = flujoMesaControlManager.recuperarDatosTramiteValidacionCliente(flujo);
				Map<String,String> tramite = (Map<String,String>)datos.get("TRAMITE");
				cdunieco = tramite.get("CDUNIECO");
				cdramo   = tramite.get("CDRAMO");
				nmpoliza = tramite.get("NMSOLICI");
				
				cdusuari     = us.getUser();
				cdelemen     = us.getEmpresa().getElementoId();
				cdsisrol     = us.getRolActivo().getClave();
				
				cdtipsit = endososManager.recuperarCdtipsitInciso1(cdunieco, cdramo, estado, nmpoliza);
				
				DatosUsuario datUs = kernelManager.obtenerDatosUsuario(us.getUser(),cdtipsit);
				cdperson = datUs.getCdperson();
				
				comentarios = params.get("dscoment");
				swagente    = params.get("swagente");
			}
			catch(Exception ex)
			{
				mensajeRespuesta = Utils.manejaExcepcion(ex);
				success = false;
			}
		}
		else
		{
			////// obtener parametros
			if(success)
			{
				try
				{
					success = success && ( ntramiteAut      = panel1.get("ntramite")           )!=null;
					success = success && ( cdunieco         = panel1.get("cdunieco")           )!=null;
					success = success && ( cdramo           = panel1.get("cdramo")             )!=null;
					success = success && ( nmpoliza         = panel1.get("nmpoliza")           )!=null;
					success = success && ( ntramite         = panel1.get("otvalor03")          )!=null;
					success = success && ( cdusuari         = panel1.get("otvalor01")          )!=null;
					success = success && ( cdelemen         = panel1.get("otvalor02")          )!=null;
					success = success && ( cdperson         = panel1.get("otvalor04")          )!=null;
					success = success && ( comentarios      = panel2.get("observaciones")      )!=null;
					success = success && ( fechaEmision     = panel1.get("ferecepc")           )!=null;
					success = success && ( cdtipsit         = panel1.get("cdtipsit")           )!=null;
					success = success && ( fechaEmisionDate = renderFechas.parse(fechaEmision) )!=null;
					success = success && ( us               = (UserVO)session.get("USUARIO")   )!=null;
					if(!success)
					{
						mensajeRespuesta = "No se recibieron todos los datos";
					}
				}
				catch(Exception ex)
				{
					logger.error("error al procesar los datos",ex);
					mensajeRespuesta = "Error al procesar los datos";
					success          = false;
				}
			}
		}
		
		////// cdtener datos poliza
		if(success)
		{
			try
			{
				Map<String,String>paramObtenerPoliza=new LinkedHashMap<String,String>(0);
				paramObtenerPoliza.put("pv_cdunieco" , cdunieco);
				paramObtenerPoliza.put("pv_cdramo"   , cdramo);
				paramObtenerPoliza.put("pv_estado"   , estado);
				paramObtenerPoliza.put("pv_nmpoliza" , nmpoliza);
				paramObtenerPoliza.put("pv_cdusuari" , cdusuari);
				Map<String,Object>polizaCompleta=kernelManager.getInfoMpolizasCompleta(paramObtenerPoliza);
				logger.debug("poliza a emitir: "+polizaCompleta);
				cdperpag = (String)polizaCompleta.get("cdperpag");
				
				if(procesoFlujo)
				{
					fechaEmisionDate = (Date)polizaCompleta.get("feemisio");
				}
			}
			catch(Exception ex)
			{
				logger.error("error al obtener los datos completos de la poliza",ex);
				mensajeRespuesta = ex.getMessage();
				success          = false;
			}
		}
		
		////// emision
		if(success)
		{
			try
			{
				Map<String,Object>paramEmi=new LinkedHashMap<String,Object>(0);
				paramEmi.put("pv_cdusuari"  , cdusuari);
				paramEmi.put("pv_cdunieco"  , cdunieco);
				paramEmi.put("pv_cdramo"    , cdramo);
				paramEmi.put("pv_estado"    , estado);
				paramEmi.put("pv_nmpoliza"  , nmpoliza);
				paramEmi.put("pv_nmsituac"  , "1");
				paramEmi.put("pv_nmsuplem"  , "0");
				paramEmi.put("pv_cdelement" , cdelemen); 
				paramEmi.put("pv_cdcia"     , cdunieco);
				paramEmi.put("pv_cdplan"    , null);
				paramEmi.put("pv_cdperpag"  , cdperpag);
				paramEmi.put("pv_cdperson"  , cdperson);
				paramEmi.put("pv_fecha"     , fechaEmisionDate);
				paramEmi.put("pv_ntramite"  , ntramite);
				mx.com.aon.portal.util.WrapperResultados wr=kernelManager.emitir(paramEmi);
				logger.debug("emision obtenida "+wr.getItemMap());
				
				nmpolizaEmitida = (String)wr.getItemMap().get("nmpoliza");
				this.nmpoliza = nmpolizaEmitida;
				nmpoliexEmitida = (String)wr.getItemMap().get("nmpoliex");
				nmsuplemEmitida = (String)wr.getItemMap().get("nmsuplem");
				this.nmsuplem = nmsuplemEmitida;
				esDxN           = (String)wr.getItemMap().get("esdxn");
				cdIdeperRes     = (String)wr.getItemMap().get("CDIDEPER");
				this.cdIdeper   = cdIdeperRes;
			}
			catch(Exception ex)
			{
				logger.error("error en el pl de emision",ex);
				mensajeRespuesta = ex.getMessage();
				success          = false;
			}
		}

	rutaCarpeta=this.rutaDocumentosPoliza+"/"+ntramite;
	
	////// ws cliente y recibos
	if(success)
	{
					String _cdunieco = cdunieco;
					String _cdramo   = cdramo;
					String edoPoliza = "M";
					String _nmpoliza = nmpolizaEmitida;
					String _nmsuplem = nmsuplemEmitida;
					String sucursal = cdunieco;
					if("1".equals(sucursal))
					{
						sucursal = "1000";
					}
					
					
					//ws recibos
					if( success && (cdtipsit.equalsIgnoreCase(TipoSituacion.SALUD_VITAL.getCdtipsit())
							||cdtipsit.equalsIgnoreCase(TipoSituacion.SALUD_NOMINA.getCdtipsit())
							||cdtipsit.equalsIgnoreCase(TipoSituacion.MULTISALUD.getCdtipsit())
							||cdtipsit.equalsIgnoreCase(TipoSituacion.RECUPERA_INDIVIDUAL.getCdtipsit())
							||cdtipsit.equalsIgnoreCase(TipoSituacion.GASTOS_MEDICOS_INDIVIDUAL.getCdtipsit())
							))
					{
						try
						{
							if(StringUtils.isNotBlank(esDxN) && "S".equalsIgnoreCase(esDxN))
							{	
								// Ejecutamos el Web Service de Recibos:
								ice2sigsService.ejecutaWSrecibos(_cdunieco, _cdramo,
										edoPoliza, _nmpoliza, 
										_nmsuplem, rutaCarpeta,
										sucursal, nmpoliza, ntramite, 
										false, tipoMov,
										us);
								// Ejecutamos el Web Service de Recibos DxN:
								recibosSigsService.generaRecibosDxN(_cdunieco, _cdramo, edoPoliza, _nmpoliza, _nmsuplem, sucursal, nmpoliza, ntramite, us);
							}
							else
							{
								// Ejecutamos el Web Service de Recibos:
								ice2sigsService.ejecutaWSrecibos(_cdunieco, _cdramo,
										edoPoliza, _nmpoliza, 
										_nmsuplem, rutaCarpeta,
										sucursal, nmpoliza,ntramite, 
										true, tipoMov,
										us);
							}
						}
						catch(Exception ex)
						{
							logger.error("error al lanzar ws recibos",ex);
							mensajeRespuesta = ex.getMessage();
							success          = false;
						}
					}
				}
		
		////// documentacion
		if(success)
		{
			try
			{
				documentosManager.generarDocumentosParametrizados(
						cdunieco
						,cdramo
						,"M" //estado
						,nmpolizaEmitida
						,"0" //nmsituac
						,nmsuplemEmitida
						,DocumentosManager.PROCESO_EMISION //proceso
						,ntramite
						,nmpoliza //nmsolici
, null
						);
				
			}
			catch(Exception ex)
			{
				logger.error("error al crear documentacion",ex);
				success          = false;
				mensajeRespuesta = ex.getMessage();
			}
		}
		
		////// detalle emision
		if(success)
		{
			try
			{
				RespuestaTurnadoVO despacho = despachadorManager.turnarTramite(
				        us.getUser(),
				        us.getRolActivo().getClave(),
				        ntramite,
				        EstatusTramite.CONFIRMADO.getCodigo(),
				        Utils.join("La emisi\u00f3n del tr\u00e1mite se autoriz\u00f3 con las siguientes observaciones: ", 
	                            (StringUtils.isBlank(comentarios)
	                                ? "(sin observaciones)"
	                                : Utils.join("\n", comentarios)
	                            )
	                        ),
				        null, // cdrazrecha
				        null, // cdusuariDes
				        null, // cdsisrolDes
				        "S".equals(swagente),
				        false, // porEscalamiento
				        fechaHoy,
				        false  // sinGrabarDetalle
				        );
	        	mensajeRespuesta = "P\u00f3liza emitida: "+nmpoliexEmitida;
			}
			catch(Exception ex)
			{
				logger.error("error al guardar detalle de emision",ex);
				mensajeRespuesta = ex.getMessage();
				success          = false;
			}
		}
		
		message = mensajeRespuesta;
		
		logger.debug(Utils.log(
				 "\n###### autorizaEmision ######"
				,"\n#############################"
				));
		return SUCCESS;
	}
	
	public String reintentaWSautos(){
		String estatusEmision = null;
		
		logger.debug("\n\n<<<<<<>>>>>>  Reintentando Emision  <<<<<<>>>>>>\n");
		
		 estatusEmision =  this.emitir();
		 
		logger.debug("\n\n<<<<<<>>>>>>  Fin Reintento de Emision, Estatus ::::: " +estatusEmision + "\n");
		
		return estatusEmision;
	}
	
	public String ejecutaWSManualCliente() {

		String cdunieco = map1.get("cdunieco");
		String cdramo = map1.get("cdramo");
		String estado = map1.get("estado");
		String nmpoliza = map1.get("nmpoliza");
		String nmsuplem = map1.get("nmsuplem");
		//String sucursal = map1.get("sucursal");

		String nmtramite = map1.get("nmtramite");
		
		String operacion = map1.get("operacion");
		if(StringUtils.isBlank(operacion)) operacion = "INSERTA";
		Ice2sigsService.Operacion operation = Ice2sigsService.Operacion.valueOf(operacion);
		
		String cdIdeperRes = null;
		
		UserVO usuarioSesion = null;
		if(session != null){
			usuarioSesion = (UserVO) session.get("USUARIO");
		}
		
		if(usuarioSesion == null){
			usuarioSesion = new UserVO();
			usuarioSesion.setUser("ICE");
		}

		// Ejecutamos el Web Service de Cliente Salud:
		//ice2sigsService.ejecutaWSclienteSalud(cdunieco, cdramo, estado, nmpoliza, nmsuplem, nmtramite, operation, (UserVO) session.get("USUARIO"));
		ClienteGeneralRespuesta resCli = ice2sigsService.ejecutaWSclienteGeneral(cdunieco, cdramo, estado, nmpoliza, nmsuplem, nmtramite, null,operation, null, usuarioSesion, true);
		
		if(resCli != null && Ice2sigsService.Estatus.EXITO.getCodigo() == resCli.getCodigo() && ArrayUtils.isNotEmpty(resCli.getClientesGeneral())){
			cdIdeperRes = resCli.getClientesGeneral()[0].getNumeroExterno();
			if(StringUtils.isNotBlank(cdIdeperRes) && !cdIdeperRes.equalsIgnoreCase("0") && !cdIdeperRes.equalsIgnoreCase("0L")){
			
				HashMap<String, String> paramsIdeper =  new HashMap<String, String>();
				paramsIdeper.put("pv_cdunieco_i", cdunieco);
				paramsIdeper.put("pv_cdramo_i",   cdramo);
				paramsIdeper.put("pv_estado_i",   estado);
				paramsIdeper.put("pv_nmpoliza_i", nmpoliza);
				paramsIdeper.put("pv_nmsuplem_i", nmsuplem);
				paramsIdeper.put("pv_cdideper_i", cdIdeperRes);
				
				kernelManager.actualizaCdIdeper(paramsIdeper);
				
				this.cdIdeper = cdIdeperRes;
						
			}else {
				success = false;
				mensajeRespuesta = "Error al crear Cliente en WS, no se pudo obtener el numero de Cliente";
				logger.error("Error al crear Cliente en WS, no se pudo obtener el numero de Cliente, respondio: "+ cdIdeperRes);
			} 
		}else{
			success = false;
			mensajeRespuesta = "Error al crear Cliente en WS.";
			logger.error("Error al Crear el cliente en WS!, Datos Nulos");
		}
		
		success = true;
		return SUCCESS;
	}

	public String ejecutaWSManualRecibos() {

		try{
			
			Utils.validate(map1 , "No se recibieron par\u00e1metros");
			
			String cdunieco = map1.get("cdunieco");
			String cdramo = map1.get("cdramo");
			String estado = map1.get("estado");
			String nmpoliza = map1.get("nmpoliza");
			String nmsuplem = map1.get("nmsuplem");
			String sucursal = map1.get("sucursal");
			
			String nmsolici = map1.get("nmsolici");
			String nmtramite = map1.get("nmtramite");
			String tipoMov = map1.get("tipmov");
			
			
			Utils.validate(cdunieco , "No se recibieron par\u00e1metros");
			Utils.validate(cdramo , "No se recibieron par\u00e1metros");
			Utils.validate(estado , "No se recibieron par\u00e1metros");
			Utils.validate(nmpoliza , "No se recibieron par\u00e1metros");
			Utils.validate(nmsuplem , "No se recibieron par\u00e1metros");
			
			UserVO usuarioSesion = null;
			if(session != null){
				usuarioSesion = (UserVO) session.get("USUARIO");
			}
			
			if(usuarioSesion == null){
				usuarioSesion = new UserVO();
				usuarioSesion.setUser("ICE");
			}
			
			if(StringUtils.isBlank(tipoMov) && StringUtils.isBlank(nmtramite) && StringUtils.isBlank(nmsolici)){
				Map<String,String> datos =  consultasPolizaManager.obtieneDatosLigasRecibosPoliza(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
				
				logger.debug("Parametros Obtenidos para Regenerar Recibos: "+datos);
				
				Utils.validate(datos , "No se obtuvieron par\u00e1metros");
				sucursal = cdunieco;	
				
				tipoMov   = datos.get("CDTIPSUP");
				nmtramite = datos.get("NTRAMITE");
				nmsolici  = datos.get("NMSOLICI");
				
				Utils.validate(tipoMov , "No se obtuvieron par\u00e1metros");
				Utils.validate(nmtramite , "No se obtuvieron par\u00e1metros");
				//Utils.validate(nmsolici , "No se obtuvieron par\u00e1metros");

				// Ejecutamos el Web Service de Recibos Sincrono:
				success = ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo, estado, nmpoliza, nmsuplem, null, sucursal, nmsolici, nmtramite, false, tipoMov, usuarioSesion);

				Utils.validate(success, "No se regeneraron correctamente todos los recibos.");
				
				
				if(success && (TipoEndoso.EMISION_POLIZA.getCdTipSup().toString().equalsIgnoreCase((tipoMov)) || TipoEndoso.RENOVACION.getCdTipSup().toString().equalsIgnoreCase((tipoMov)) )){
					boolean esDxn = false;
					
					// Ejecutamos el Web Service de Recibos DxN:
					try{
						esDxn = this.consultasManager.esDxn(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
					}catch (Exception e){
						logger.error("Error al buscar obtener esDxn",e);
						success=false;
					}
					
					if(success && esDxn){
						success = recibosSigsService.generaRecibosDxN(cdunieco, cdramo, estado, nmpoliza, nmsuplem, sucursal, nmsolici, nmtramite, usuarioSesion);
					}
					Utils.validate(success, "No se regeneraron todos los recibos y calendarios Dxn. Verificar en Tbitacobros");
				}
				
			}else{
				// Ejecutamos el Web Service de Recibos Asincrono:
				ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo, estado, nmpoliza, nmsuplem, null, sucursal, nmsolici, nmtramite, true, tipoMov, usuarioSesion);
			}
			
			
			
		}catch(Exception e){
			respuesta = Utils.manejaExcepcion(e);
			
			success = false;
			return SUCCESS;
		}
		
		success = true;
		return SUCCESS;
	}
	
	
	public String ejecutaWSManualRecibosDxN() {
		
		String cdunieco = map1.get("cdunieco");
		String cdramo = map1.get("cdramo");
		String estado = map1.get("estado");
		String nmpoliza = map1.get("nmpoliza");
		String nmsuplem = map1.get("nmsuplem");
		String sucursal = map1.get("sucursal");
		String nmsolici = map1.get("nmsolici");
		String nmtramite = map1.get("nmtramite");
		
		UserVO usuarioSesion = null;
		if(session != null){
			usuarioSesion = (UserVO) session.get("USUARIO");
		}
		
		if(usuarioSesion == null){
			usuarioSesion = new UserVO();
			usuarioSesion.setUser("ICE");
		}

		// Ejecutamos el Web Service de Recibos DxN:
		recibosSigsService.generaRecibosDxN(cdunieco, cdramo, estado, nmpoliza, nmsuplem, sucursal, nmsolici, nmtramite, usuarioSesion);
		success = true;
		return SUCCESS;
	}
	
	public String buscarPersonasRepetidas()
	{
		logger.debug(Utils.log(
				 "\n#######################################"
				,"\n###### buscar personas repetidas ######"
				,"\n###### map1 = ", map1
				));
		clienteWS = false;
		
		try
		{
		    slist1=kernelManager.buscarRFC(map1);
		    
		    logger.debug("slist antes de WS: "+ slist1);
		    
		    boolean soloBD = false;
		    if(map1.containsKey("soloBD") && Constantes.SI.equalsIgnoreCase(map1.get("soloBD"))){
		    	soloBD = true;
		    }
		    
		    logger.debug("Busqueda de cliente solo BD: " + soloBD);
		    
		    /**
		     * Si no se encuentra el RFC en la BD se consulta a un WS de personas
		     */
		    if(!soloBD && (Ramo.AUTOS_FRONTERIZOS.getCdramo().equalsIgnoreCase(map1.get("pv_cdramo_i")) 
		    		|| Ramo.SERVICIO_PUBLICO.getCdramo().equalsIgnoreCase(map1.get("pv_cdramo_i"))
		    		|| Ramo.AUTOS_RESIDENTES.getCdramo().equalsIgnoreCase(map1.get("pv_cdramo_i"))
		    		|| (Ramo.MULTISALUD.getCdramo().equalsIgnoreCase(map1.get("pv_cdramo_i")) && TipoSituacion.MULTISALUD_COLECTIVO.getCdtipsit().equalsIgnoreCase(map1.get("cdtipsit")))
		    	) && (slist1 == null || slist1.isEmpty())){
		    	logger.debug("Buscando RFC en WS...");
		    	
		    	slist1 = new ArrayList<Map<String, String>>();
		    	HashMap<String, Object> params =  new HashMap<String, Object>();
		    	params.putAll(map1);
		    	
		    	String cdtipsitGS = null;
		    	
		    	params.put("pv_cdtipsit_i", params.get("cdtipsit"));
		    	
		    	if(Ramo.AUTOS_FRONTERIZOS.getCdramo().equalsIgnoreCase(map1.get("pv_cdramo_i"))){
		    		cdtipsitGS = kernelManager.obtenCdtipsitGS(params);
		    	}else if(Ramo.SERVICIO_PUBLICO.getCdramo().equalsIgnoreCase(map1.get("pv_cdramo_i"))
		    			|| Ramo.AUTOS_RESIDENTES.getCdramo().equalsIgnoreCase(map1.get("pv_cdramo_i"))
			    	){
		    		cdtipsitGS = kernelManager.obtenSubramoGS(params);
		    	}else{
		    		cdtipsitGS = kernelManager.obtenSubramoGS(params);
		    	}
		    	
		    	ClienteGeneral clienteGeneral = new ClienteGeneral();
		    	clienteGeneral.setRfcCli(map1.get("pv_rfc_i"));
		    	clienteGeneral.setRamoCli(Integer.parseInt(cdtipsitGS));
		    	
		    	ClienteGeneralRespuesta clientesRes = ice2sigsService.ejecutaWSclienteGeneral(null, null, null, null, null, null, null, Ice2sigsService.Operacion.CONSULTA_GENERAL, clienteGeneral, null, false);
		    	
		    	if(clientesRes == null){
		    		success = false;
		    		return SUCCESS;
		    	}
		    	
		    	ClienteGeneral[] listaClientesGS = clientesRes.getClientesGeneral();
		    	if(listaClientesGS != null && listaClientesGS.length > 0 ){
		    		logger.debug("Agregando Clientes de GS a Lista, " + listaClientesGS.length);
		    		clienteWS = true;
		    		
		    		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		    		Calendar calendar =  Calendar.getInstance();
			    	HashMap<String, String> agregar = null;
			    	
			    	for(ClienteGeneral cli: listaClientesGS){
			    		agregar = new HashMap<String,String>();
			    		
				    	agregar.put("RFCCLI",    cli.getRfcCli());
				    	agregar.put("NOMBRECLI", (cli.getFismorCli() == 1) ? (cli.getNombreCli()+" "+cli.getApellidopCli()+" "+cli.getApellidomCli()) : cli.getRazSoc() );
				    	agregar.put("NOMBRE",    (cli.getFismorCli() == 1) ? cli.getNombreCli() : cli.getRazSoc());
				    	agregar.put("SNOMBRE",   "");
				    	
				    	String apellidoPat = "";
				    	if(StringUtils.isNotBlank(cli.getApellidopCli()) && !cli.getApellidopCli().trim().equalsIgnoreCase("null")){
				    		apellidoPat = cli.getApellidopCli();
				    	}
				    	agregar.put("APPAT",     (cli.getFismorCli() == 1) ? apellidoPat : "");
				    	
				    	String apellidoMat = "";
				    	if(StringUtils.isNotBlank(cli.getApellidomCli()) && !cli.getApellidomCli().trim().equalsIgnoreCase("null")){
				    		apellidoMat = cli.getApellidomCli();
				    	}
				    	agregar.put("APMAT",     (cli.getFismorCli() == 1) ? apellidoMat : "");
				    	
				    	if(cli.getFecnacCli()!= null){
				    		calendar.set(cli.getFecnacCli().get(Calendar.YEAR), cli.getFecnacCli().get(Calendar.MONTH), cli.getFecnacCli().get(Calendar.DAY_OF_MONTH));
							agregar.put("FENACIMICLI", sdf.format(calendar.getTime()));
				    	}else {
				    		agregar.put("FENACIMICLI", "");
				    	}
				    	agregar.put("DIRECCIONCLI", cli.getCalleCli()+" "+(StringUtils.isNotBlank(cli.getNumeroCli())?cli.getNumeroCli():"")+" "+cli.getColoniaCli()+" "+cli.getMunicipioCli()+(StringUtils.isNotBlank(cli.getCodposCli())?" C.P. "+cli.getCodposCli():""));
				    	
				    	agregar.put("CODPOSTAL", cli.getCodposCli());
				    	String edoAdosPos = Integer.toString(cli.getEstadoCli());
		    			if(edoAdosPos.length() ==  1){
		    				edoAdosPos = "0"+edoAdosPos;
		    			}
				    	agregar.put("CDEDO", edoAdosPos);
				    	agregar.put("CDMUNICI", "");
				    	agregar.put("NMORDDOM", "1");// DEFAULT NUMERO DE ORDINAL DE DOMICILIO AL IMPORTAR 
				    	agregar.put("DSDOMICIL", cli.getCalleCli());
				    	agregar.put("NMNUMERO", cli.getNumeroCli());
				    	agregar.put("NMNUMINT", "");
				    	
				    	agregar.put("CLAVECLI",     "");
				    	agregar.put("CDIDEPER",     cli.getNumeroExterno());
				    	
				    	String sexo = "H"; //Hombre
				    	if(cli.getSexoCli() > 0){
				    		if(cli.getSexoCli() == 2) sexo = "M";
				    	}
				    	agregar.put("SEXO",     sexo);
				    	
				    	String tipoPersona = "F"; //Fisica
				    	if(cli.getFismorCli() > 0){
				    		if(cli.getFismorCli() == 2){
				    			tipoPersona = "M";
				    		}else if(cli.getFismorCli() == 3){
				    			tipoPersona = "S";
				    		}
				    	}
				    	agregar.put("TIPOPERSONA",     tipoPersona);
				    	
				    	String nacionalidad = "001";// Nacional
				    	if(StringUtils.isNotBlank(cli.getNacCli()) && !cli.getNacCli().equalsIgnoreCase("1")){
				    		nacionalidad = "002";
				    	}
				    	agregar.put("NACIONALIDAD",     nacionalidad);
				    	
				    	slist1.add(agregar);
				    	
			    	}
			    	
		    	}else {
		    		logger.debug("No se encontraron clientes en GS.");
		    	}
		    	
		    }
		    
		    for(Map<String,String>rfc:slist1)
		    {
		    	rfc.put("DISPLAY",rfc.get("RFCCLI")+"<br/>"+rfc.get("NOMBRECLI")+"<br/>"+rfc.get("DIRECCIONCLI"));
		    }
		    success=true;
		}
		catch(Exception ex)
		{
			logger.error("Error al buscar RFC",ex);
			success=false;
		}
		logger.debug(Utils.log(
				 "\n###### buscar personas repetidas ######"
				,"\n#######################################"
				));
		return SUCCESS;
	}
	
	public String obtenerRamos()
	{
		try
		{
			slist1=kernelManager.obtenerRamos(map1.get("cdunieco"));
		}
		catch(Exception ex)
		{
			logger.error("error al obtener los ramos",ex);
		}
		success=true;
		return SUCCESS;
	}
	
	public String obtenerTipsit()
	{
		try
		{
			slist1=kernelManager.obtenerTipsit(map1.get("cdramo"));
		}
		catch(Exception ex)
		{
			logger.error("error al obtener los tipsit",ex);
		}
		success=true;
		return SUCCESS;
	}
	
	/////////////////////////////////
	////// getters ans setters //////
	/*/////////////////////////////*/
	public Item getItems() {
		return items;
	}

	public void setItems(Item items) {
		this.items = items;
	}

	public void setKernelManager(KernelManagerSustituto kernelManager) {
		this.kernelManager = kernelManager;
	}

	public Map<String, String> getPanel1() {
		return panel1;
	}

	public void setPanel1(Map<String, String> panel1) {
		this.panel1 = panel1;
	}

	public Map<String, String> getPanel2() {
		return panel2;
	}

	public void setPanel2(Map<String, String> panel2) {
		this.panel2 = panel2;
	}

	public Map<String, String> getParametros() {
		return parametros;
	}

	public void setParametros(Map<String, String> parametros) {
		this.parametros = parametros;
	}

	public Item getFields() {
		return fields;
	}

	public void setFields(Item fields) {
		this.fields = fields;
	}

	public String getCdunieco() {
		return cdunieco;
	}

	public void setCdunieco(String cdunieco) {
		this.cdunieco = cdunieco;
	}

	public String getCdramo() {
		return cdramo;
	}

	public void setCdramo(String cdramo) {
		this.cdramo = cdramo;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNmpoliza() {
		return nmpoliza;
	}

	public void setNmpoliza(String nmpoliza) {
		this.nmpoliza = nmpoliza;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Item getItem1() {
		return item1;
	}

	public void setItem1(Item item1) {
		this.item1 = item1;
	}

	public Item getItem2() {
		return item2;
	}

	public void setItem2(Item item2) {
		this.item2 = item2;
	}

	public Item getItem3() {
		return item3;
	}

	public void setItem3(Item item3) {
		this.item3 = item3;
	}

	public Item getItem4() {
		return item4;
	}

	public void setItem4(Item item4) {
		this.item4 = item4;
	}

	public Map<String, String> getMap1() {
		return map1;
	}

	public void setMap1(Map<String, String> map1) {
		this.map1 = map1;
	}

	public Map<String, String> getMap2() {
		return map2;
	}

	public void setMap2(Map<String, String> map2) {
		this.map2 = map2;
	}

	public Map<String, String> getMap3() {
		return map3;
	}

	public void setMap3(Map<String, String> map3) {
		this.map3 = map3;
	}

	public Map<String, String> getMap4() {
		return map4;
	}

	public void setMap4(Map<String, String> map4) {
		this.map4 = map4;
	}
	
	public String getCdatribuRol(String auxiliarProductoCdtipsit)
	{
		logger.error("DEPRECATED getCdatribuRol",new Exception("DEPRECATED getCdatribuRol"));
		return null;
	}
	
	public String getCdatribuSexo(String auxiliarProductoCdtipsit)
	{
		logger.error("DEPRECATED getCdatribuSexo",new Exception("DEPRECATED getCdatribuSexo"));
		return null;
	}
	
	public String pantallaJavaExterno()
	{
		logger.debug(Utils.log(
				 "\n#################################"
				,"\n###### pantallaJavaExterno ######"
				,"\n###### pantallaJavaExterno ######"
				,"\n#################################"
				));
		return SUCCESS;
	}
	
	public String operacionJavaExterno()
	{
		logger.debug(Utils.log(
				 "\n##################################"
				,"\n###### operacionJavaExterno ######"
				,"\n###### panel1 = ", panel1
				));
		String a = panel1.get("a");
		String b = panel1.get("b");
		String c = null;
		try
		{
			File carpeta         = new File("/opt/ice/gseguros");
			String clase         = "Probando";
			String metodo        = "prueba";
			Class<?>[]parametros = new Class[]
					{
					String.class
					,String.class
					};
			ClassLoader loader = new URLClassLoader(new URL[]{carpeta.toURI().toURL()},getClass().getClassLoader());
			Class<?>    cls    = loader.loadClass(clase);
			Method      m      = cls.getMethod(metodo, parametros);
			
			c=(String)m.invoke(cls.newInstance(),a,b);
			logger.debug("c: "+c);
			panel1.put("c",c);
		}
		catch(Exception ex)
		{
			logger.error("error:",ex);
		}
		logger.debug(Utils.log(
				 "\n###### operacionJavaExterno ######"
				,"\n##################################"
				));
		return SUCCESS;
	}
	
	public String pantallaCompiladora()
	{
		logger.debug(Utils.log(
				 "\n#################################"
				,"\n###### pantallaCompiladora ######"
				,"\n###### pantallaCompiladora ######"
				,"\n#################################"
				));
		return SUCCESS;
	}
	
	public String compilarProceso()
	{
		logger.debug(Utils.log(
				 "\n#############################"
				,"\n###### compilarProceso ######"
				,"\n###### map1 = ", map1
				));
		//ENTRADA
		
		//VARIABLES DE ENTRADA OBTENIDAS DEL MAPA
		String archivo = map1.get("archivo");
		String codigo  = map1.get("codigo");
		
		//PROPIEDAD PARA EL SUBMIT DE EXT
		success = true;
		
		exito = true;
		
		//ESCRIBIR ARCHIVO JAVA
		if(exito)
		{
			try
			{
				File archivoJava = new File(archivo);
				PrintStream streamArchivoJava = new PrintStream(archivoJava);
				streamArchivoJava.print(codigo);
				streamArchivoJava.close();
			}
			catch(Exception ex)
			{
				logger.error("error al crear archivo java",ex);
				mensajeRespuesta=ex.getMessage();
				exito=false;
			}
		}
		
		//RECONOCER SISTEMA OPERATIVO
		boolean esWindows = false;
		if(exito)
		{
			if(System.getProperty("os.name").toLowerCase().indexOf("windows") != -1)
			{
				esWindows = true;
			}
			logger.debug("es windows: "+esWindows);
		}
		
		//COMPILAR
		if(exito)
		{
			try
			{
				String[] finalCommand; 
				if(esWindows)
				{
					finalCommand = new String[4];
			        // Use the appropriate path for your windows version.
			        //finalCommand[0] = "C:\\winnt\\system32\\cmd.exe";    // Windows NT/2000
			        finalCommand[0] = "C:\\windows\\system32\\cmd.exe";    // Windows XP/2003
			        //finalCommand[0] = "C:\\windows\\syswow64\\cmd.exe";  // Windows 64-bit
			        finalCommand[1] = "/y";
			        finalCommand[2] = "/c";
			        finalCommand[3] = "javac "+archivo;
				}
				else
				{
					finalCommand = new String[3];
			        finalCommand[0] = "/bin/sh";
			        finalCommand[1] = "-c";
			        finalCommand[2] = "javac "+archivo;
				}
				
				//EJECUTAR COMANDO
				final Process pr = Runtime.getRuntime().exec(finalCommand);
				pr.waitFor();
				
				//ESCUCHAR ERRORES
				mensajeRespuesta="";
				BufferedReader br_err = null;
				try
				{
					br_err = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
					String buff = null;
					while ((buff = br_err.readLine()) != null)
					{
						logger.error("Error :" + buff);
						mensajeRespuesta=mensajeRespuesta+(buff.replaceAll(" ", "&nbsp;"))+"<br/>";
						exito=false;
						try
						{
							Thread.sleep(1);
						} catch(Exception e) {}
					}
					br_err.close();
				}
				catch (Exception ex)
				{
					logger.error("Error al imprimir errores de proceso",ex);
					mensajeRespuesta=mensajeRespuesta+ex;
					exito=false;
				}
				finally
				{
					try
					{
						br_err.close();
					} catch (Exception ex) {}
				}
			}
			catch(Exception ex)
			{
				logger.error("error al compilar",ex);
				mensajeRespuesta=ex.getMessage();
				exito=false;				
			}
		}
		
		logger.debug(Utils.log(
				 "\n###### compilarProceso ######"
				,"\n#############################"
				));
		return SUCCESS;
	}
	
	public String redireccion()
	{
		logger.debug(Utils.log(
				 "\n#########################"
				,"\n###### redireccion ######"
				,"\n###### map1 = ", map1
				,"\n###### redireccion ######"
				,"\n#########################"
				));
		return SUCCESS;
	}

	public String guardarCartaRechazo()
	{
		logger.debug(Utils.log(
				 "\n#################################"
				,"\n###### guardarCartaRechazo ######"
				,"\n###### map1 = ", map1
				));
		
		try
		{
			String cdsisrol = Utils.validateSession(session).getRolActivo().getClave();
			
			String ntramite   = map1.get("ntramite");
			String comments   = map1.get("comments");
			String cdrazrecha = map1.get("cdrazrecha");
			
			// Cuando es por falta de informacion se meten en la carta los reqs y docs obligatorios faltantes
			if ("5".equals(cdrazrecha)) {
				logger.debug("Se agregan los reqs y docs faltantes por ser cdrazrecha 5");
				List<Map<String, String>> reqsDocsFaltan = flujoMesaControlManager.recuperarRequisitosDocumentosObligatoriosFaltantes(ntramite);
				List<Map<String, String>> docsFaltan = new ArrayList<Map<String, String>>();
				List<Map<String, String>> reqsFaltan = new ArrayList<Map<String, String>>();
				for (Map<String, String> faltante : reqsDocsFaltan) {
					if ("REQ".equals(faltante.get("TIPO"))) {
						reqsFaltan.add(faltante);
					} else if ("DOC".equals(faltante.get("TIPO"))) {
						docsFaltan.add(faltante);
					}
				}
				
				if (docsFaltan.size() + reqsFaltan.size() > 0) {
					logger.debug("Si hay reqs y/o docs faltantes");
					StringBuilder sb = new StringBuilder(StringUtils.isBlank(comments)
						? ""
						: comments);
					
					sb.append("\n");
					
					if (reqsFaltan.size() > 0) {
						sb.append("\nREQUISITOS OBLIGATORIOS FALTANTES:\n");
						int i = 1;
						for (Map<String, String> req : reqsFaltan) {
							sb.append(Utils.join(
									i++, ". ", req.get("DESCRIP"), "\n"
									));
						}
					}
					
					if (docsFaltan.size() > 0) {
						sb.append("\nDOCUMENTOS OBLIGATORIOS FALTANTES:\n");
						int i = 1;
						for (Map<String, String> doc : docsFaltan) {
							sb.append(Utils.join(
									i++, ". ", doc.get("DESCRIP"), "\n"
									));
						}
					}
					
					comments = sb.toString();
				} else {
					logger.debug("No hay reqs y/o docs faltantes");
				}
			}
			
			logger.debug(Utils.log("comments = ", comments));
			// Se reemplazan acentos y otros caracteres:
			String commentsM   = comments.
					replaceAll("\u00E1", "_a_").
					replaceAll("\u00C1", "_A_").
					replaceAll("\u00E9", "_e_").
					replaceAll("\u00C9", "_E_").
					replaceAll("\u00ED", "_i_").
					replaceAll("\u00CD", "_I_").
					replaceAll("\u00F3", "_o_").
					replaceAll("\u00D3", "_O_").
					replaceAll("\u00FA", "_u_").
					replaceAll("\u00DA", "_U_").
					replaceAll("\u00F1", "_n_").
					replaceAll("\u00D1", "_N_").
					replaceAll("\n"    , "_s_").
					replaceAll("\""    , "");
			//String cdsisrol    = map1.get("cdsisrol");
			String cdunieco    = map1.get("cdunieco");
			String cdramo      = map1.get("cdramo");
			String estado      = map1.get("estado");
			String nmpoliza    = map1.get("nmpoliza");
			
			if("R".equals(nmpoliza))
			{
				FlujoVO flujo = new FlujoVO();
				flujo.setNtramite(ntramite);
				try
				{
					Map<String,Object> datosValidacionJS = flujoMesaControlManager.recuperarDatosTramiteValidacionCliente(flujo);
					nmpoliza = ((Map<String,String>)datosValidacionJS.get("TRAMITE")).get("NMSOLICI");
				}
				catch(Exception ex)
				{
					logger.error("Error al obtener datos de poliza desde flujo",ex);
					return SUCCESS;
				}
			}
			
			String nombreRdf = getText("rdf.emision.rechazo.danios.nombre"); // RDF de carta rechazo danios
			
			if (consultasManager.esProductoSalud(cdramo)) {
				logger.debug("Es salud");
				if(
					cdsisrol.equals(RolSistema.COORDINADOR_MEDICO.getCdsisrol())
					||cdsisrol.equals(RolSistema.COORDINADOR_MEDICO_MULTIREGIONAL.getCdsisrol())
					||cdsisrol.equals(RolSistema.GERENTE_MEDICO_MULTIREGIONAL.getCdsisrol())
					||cdsisrol.equals(RolSistema.MEDICO.getCdsisrol())
					||cdsisrol.equals(RolSistema.MEDICO_AJUSTADOR.getCdsisrol())
				) {
					logger.debug("Es medico");
					nombreRdf = this.getText("rdf.emision.rechazo.medico.nombre");
				} else {
					logger.debug("No es medico");
					nombreRdf = this.getText("rdf.emision.rechazo.admin.nombre");
				}
			} else {
				logger.debug("No es salud");
			}
			
			logger.debug("nombreRdf = {}", nombreRdf);
			
			String rutaCarpeta = Utils.join(this.rutaDocumentosPoliza, "/", ntramite);
			
			String url = Utils.join(
					this.rutaServidorReports,
					"?destype=cache",
					"&desformat=PDF",
					"&paramform=no",
					"&ACCESSIBLE=YES", //parametro que habilita salida en PDF
					"&userid=", this.passServidorReports,
					"&report=", nombreRdf,
					"&p_ntramite=", ntramite,
					"&p_ramo=", cdramo,
					"&p_comments=", commentsM
			);
			logger.debug(Utils.log(
					 "\n#################################"
					,"\n###### Se solicita reporte ######"
					,"\n###### ",url
					));
			HttpUtil.generaArchivo(url,Utils.join(rutaCarpeta, "/", this.getText("pdf.emision.rechazo.nombre")));
			logger.debug(Utils.log(
					 "\n###### Se solicita reporte ######"
					,"\n#################################"
					));
			//HashMap<String, Object> paramsR = new HashMap<String, Object>();
			//paramsR.put("pv_cdunieco_i"  , cdunieco);
			//paramsR.put("pv_cdramo_i"    , cdramo);
			//paramsR.put("pv_estado_i"    , estado);
			//paramsR.put("pv_nmpoliza_i"  , nmpoliza);
			//paramsR.put("pv_nmsuplem_i"  , 0);
			//paramsR.put("pv_feinici_i"   , new Date());
			//paramsR.put("pv_cddocume_i"  , this.getText("pdf.emision.rechazo.nombre"));
			//paramsR.put("pv_dsdocume_i"  , "CARTA RECHAZO");
			//paramsR.put("pv_nmsolici_i"  , nmpoliza);
			//paramsR.put("pv_ntramite_i"  , ntramite);
			//paramsR.put("pv_tipmov_i"    , TipoTramite.POLIZA_NUEVA.getCdtiptra());
			//paramsR.put("pv_swvisible_i" , Constantes.SI);
			//kernelManager.guardarArchivo(paramsR);
			
			documentosManager.guardarDocumento(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,"0"
					,new Date()
					,this.getText("pdf.emision.rechazo.nombre")
					,"CARTA RECHAZO"
					,nmpoliza
					,ntramite
					,TipoTramite.POLIZA_NUEVA.getCdtiptra()
					,Constantes.SI
					,null
					,TipoTramite.POLIZA_NUEVA.getCdtiptra()
					,null
					,null
					,null
					,null, false
					);
	    }
		catch(Exception ex)
		{
			logger.error("error al crear la carta rechazo",ex);
		}
	
		logger.debug(Utils.log(
				 "\n###### guardarCartaRechazo ######"
				,"\n#################################"
				));
		return SUCCESS;
	}
	
	public List<Map<String, Object>> getList1() {
		return list1;
	}

	public void setList1(List<Map<String, Object>> list1) {
		this.list1 = list1;
	}

	public List<Map<String, Object>> getList2() {
		return list2;
	}

	public void setList2(List<Map<String, Object>> list2) {
		this.list2 = list2;
	}

	public List<Map<String, Object>> getList3() {
		return list3;
	}

	public void setList3(List<Map<String, Object>> list3) {
		this.list3 = list3;
	}

	public List<Map<String, Object>> getList4() {
		return list4;
	}

	public void setList4(List<Map<String, Object>> list4) {
		this.list4 = list4;
	}

	public Map<String, Object> getOmap1() {
		return omap1;
	}

	public void setOmap1(Map<String, Object> omap1) {
		this.omap1 = omap1;
	}

	public String getCdperson() {
		return cdperson;
	}

	public void setCdperson(String cdperson) {
		this.cdperson = cdperson;
	}

	public List<Map<String, String>> getSlist1() {
		return slist1;
	}

	public void setSlist1(List<Map<String, String>> slist1) {
		this.slist1 = slist1;
	}

	public GridVO getGridResultados() {
		return gridResultados;
	}

	public void setGridResultados(GridVO gridResultados) {
		this.gridResultados = gridResultados;
	}

	public void setIce2sigsService(Ice2sigsService ice2sigsService) {
		this.ice2sigsService = ice2sigsService;
	}

	public void setRecibosSigsService(RecibosSigsService recibosSigsService) {
		this.recibosSigsService = recibosSigsService;
	}

	public String getMensajeRespuesta() {
		return mensajeRespuesta;
	}

	public void setMensajeRespuesta(String mensajeRespuesta) {
		this.mensajeRespuesta = mensajeRespuesta;
	}

	public String getCdtipsit() {
		return cdtipsit;
	}

	public void setCdtipsit(String cdtipsit) {
		this.cdtipsit = cdtipsit;
	}

	public void setCatalogosManager(CatalogosManager catalogosManager) {
		this.catalogosManager = catalogosManager;
	}

	public void setConsultasManager(ConsultasManager consultasManager) {
		this.consultasManager = consultasManager;
	}

	public boolean isExito() {
		return exito;
	}

	public void setExito(boolean exito) {
		this.exito = exito;
	}
	
	public void setPantallasManager(PantallasManager pantallasManager) {
		this.pantallasManager = pantallasManager;
	}

	public Item getItem5() {
		return item5;
	}

	public void setItem5(Item item5) {
		this.item5 = item5;
	}
	
	public void setEmisionAutosService(EmisionAutosService emisionAutosService) {
		this.emisionAutosService = emisionAutosService;
	}

	public boolean isClienteWS() {
		return clienteWS;
	}

	public void setClienteWS(boolean clienteWS) {
		this.clienteWS = clienteWS;
	}

	public void setStoredProceduresManager(
			StoredProceduresManager storedProceduresManager) {
		this.storedProceduresManager = storedProceduresManager;
	}

	public boolean isRetryWS() {
		return retryWS;
	}

	public void setRetryWS(boolean retryWS) {
		this.retryWS = retryWS;
	}

	public String getNmsuplem() {
		return nmsuplem;
	}

	public void setNmsuplem(String nmsuplem) {
		this.nmsuplem = nmsuplem;
	}

	public String getCdIdeper() {
		return cdIdeper;
	}

	public void setCdIdeper(String cdIdeper) {
		this.cdIdeper = cdIdeper;
	}

	public String getNmpolAlt() {
		return nmpolAlt;
	}

	public void setNmpolAlt(String nmpolAlt) {
		this.nmpolAlt = nmpolAlt;
	}

	public String getMensajeEmail() {
		return mensajeEmail;
	}

	public void setMensajeEmail(String mensajeEmail) {
		this.mensajeEmail = mensajeEmail;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public String getRespuestaOculta() {
		return respuestaOculta;
	}

	public void setRespuestaOculta(String respuestaOculta) {
		this.respuestaOculta = respuestaOculta;
	}

	public void setCotizacionManager(CotizacionManager cotizacionManager) {
		this.cotizacionManager = cotizacionManager;
	}

	public String getTipoGrupoInciso() {
		return tipoGrupoInciso;
	}

	public void setTipoGrupoInciso(String tipoGrupoInciso) {
		this.tipoGrupoInciso = tipoGrupoInciso;
	}

	public void setEmisionManager(EmisionManager emisionManager) {
		this.emisionManager = emisionManager;
	}

	public boolean isRetryRec() {
		return retryRec;
	}

	public void setRetryRec(boolean retryRec) {
		this.retryRec = retryRec;
	}

	public String getCdRamoGS() {
		return cdRamoGS;
	}

	public void setCdRamoGS(String cdRamoGS) {
		this.cdRamoGS = cdRamoGS;
	}

	public String getSucursalGS() {
		return sucursalGS;
	}

	public void setSucursalGS(String sucursalGS) {
		this.sucursalGS = sucursalGS;
	}

	public void setServiciosManager(ServiciosManager serviciosManager) {
		this.serviciosManager = serviciosManager;
	}

	public FlujoVO getFlujo() {
		return flujo;
	}

	public void setFlujo(FlujoVO flujo) {
		this.flujo = flujo;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public String getRutaServidorReports() {
		return rutaServidorReports;
	}

	public String getPassServidorReports() {
		return passServidorReports;
	}

}