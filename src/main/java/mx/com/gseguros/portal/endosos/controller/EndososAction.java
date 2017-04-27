package mx.com.gseguros.portal.endosos.controller;

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

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionContext;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.kernel.service.KernelManagerSustituto;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.externo.service.StoredProceduresManager;
import mx.com.gseguros.mesacontrol.dao.FlujoMesaControlDAO;
import mx.com.gseguros.mesacontrol.model.FlujoVO;
import mx.com.gseguros.mesacontrol.service.FlujoMesaControlManager;
import mx.com.gseguros.portal.cancelacion.service.CancelacionManager;
import mx.com.gseguros.portal.catalogos.service.PersonasManager;
import mx.com.gseguros.portal.consultas.model.PolizaAseguradoVO;
import mx.com.gseguros.portal.consultas.model.PolizaDTO;
import mx.com.gseguros.portal.consultas.service.ConsultasManager;
import mx.com.gseguros.portal.consultas.service.ConsultasPolizaManager;
import mx.com.gseguros.portal.cotizacion.controller.ComplementariosCoberturasAction;
import mx.com.gseguros.portal.cotizacion.dao.CotizacionDAO;
import mx.com.gseguros.portal.cotizacion.model.DatosUsuario;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaImapSmapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSmapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaVoidVO;
import mx.com.gseguros.portal.cotizacion.service.CotizacionManager;
import mx.com.gseguros.portal.despachador.model.RespuestaTurnadoVO;
import mx.com.gseguros.portal.despachador.service.DespachadorManager;
import mx.com.gseguros.portal.documentos.model.Documento;
import mx.com.gseguros.portal.documentos.service.DocumentosManager;
import mx.com.gseguros.portal.emision.service.EmisionManager;
import mx.com.gseguros.portal.endosos.model.RespuestaConfirmacionEndosoVO;
import mx.com.gseguros.portal.endosos.service.EndososAutoManager;
import mx.com.gseguros.portal.endosos.service.EndososManager;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.model.RespuestaVO;
import mx.com.gseguros.portal.general.service.PantallasManager;
import mx.com.gseguros.portal.general.util.EstatusTramite;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.portal.general.util.ObjetoBD;
import mx.com.gseguros.portal.general.util.Ramo;
import mx.com.gseguros.portal.general.util.RolSistema;
import mx.com.gseguros.portal.general.util.TipoEndoso;
import mx.com.gseguros.portal.general.util.TipoFlotilla;
import mx.com.gseguros.portal.general.util.TipoSituacion;
import mx.com.gseguros.portal.general.util.TipoTramite;
import mx.com.gseguros.portal.mesacontrol.dao.MesaControlDAO;
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
import mx.com.gseguros.ws.ice2sigs.service.Ice2sigsService.Estatus;

public class EndososAction extends PrincipalCoreAction
{
	private static final long        serialVersionUID = 84257834070419933L;
	private final static Logger      logger             = LoggerFactory.getLogger(EndososAction.class);
	private boolean                  success;
	private SimpleDateFormat         renderFechas     = new SimpleDateFormat("dd/MM/yyyy");
	private static SimpleDateFormat renderFechaHora = new SimpleDateFormat("ddMMyyyyHHss");

	public static final String       ENDOSO_SIMPLE_NO_PERMITIDO = "Este endoso solo lo puede capturar si no hay endosos anteriores";
	
	private List<Map<String,String>> slist1;
	private List<Map<String,String>> slist2;
	private Map<String,String>       smap1;
	private Map<String,String>       smap2;
	private Map<String,String>       smap3;
	private Map<String,String>       smap4;
	private Map<String,Object>       omap1;
	private EndososManager           endososManager;
	private KernelManagerSustituto   kernelManager;
	private PantallasManager         pantallasManager;
	private transient Ice2sigsService ice2sigsService;
	private Item                     item1;
	private Item                     item2;
	private Item                     item3;
	private String                   mensaje;
	private boolean                  endosoConfirmado;//Para saber si el endoso se ha enviado a la mesa de autorizacion
	private Map<String,String>       parametros;
	private Map<String,Item>         imap1;
	private String                   error;
	private CancelacionManager       cancelacionManager;
	private boolean                  endosoSimple = false;
	private ConsultasManager         consultasManager;
	
	private String message;
	private String nmsuplement;
	private String nsuplogi;
	
	@Autowired
	private ConsultasPolizaManager   consultasPolizaManager;
	private CotizacionDAO  			 cotizacionDAO;
	private StoredProceduresManager  storedProceduresManager;
	
	@Autowired
	@Qualifier("emisionAutosServiceImpl")
	private EmisionAutosService emisionAutosService;
	
	@Autowired
	private PersonasManager personasManager;
	
	@Autowired
	private EndososAutoManager endososAutoManager;
	
	@Autowired
	private DocumentosManager documentosManager;
	
	@Autowired
	private MesaControlManager mesaControlManager;
	
	@Autowired
	private CotizacionManager cotizacionManager;
	
	@Autowired
	private SiniestrosManager  siniestrosManager;

	@Autowired
	private EmisionManager    emisionManager;
	
	@Autowired
    private FlujoMesaControlManager flujoMesaControlManager;
	
	private boolean exito           = false;
	private String  respuesta;
	private String  respuestaOculta = null;
	
	private String columnas;

	private FlujoVO flujo;
	
	@Autowired
	private MesaControlDAO mesaControlDAO;
	
	@Autowired
	private FlujoMesaControlDAO flujoMesaControlDAO;
	
	@Autowired
	private DespachadorManager despachadorManager;

	@Value("${ruta.servidor.reports}")
    private String rutaServidorReports;

	@Value("${pass.servidor.reports}")
    private String passServidorReports;	
	
	@Value("${ruta.documentos.poliza}")
    private String rutaDocumentosPoliza;
	
	@Value("${ruta.documentos.temporal}")
    private String rutaDocumentosTemporal;
	
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
	
	@Value("${caratula.impresion.autos.endosob.url}")
    private String caratulaImpresionAutosEndosobUrl;
	
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
	
	public EndososAction()
	{
		logger.debug("new EndososAction");
		this.session=ActionContext.getContext().getSession();
	}
	
	//////////////////////////////
	////// marco de endosos //////
	/*//////////////////////////*/
	public String marco()
	{
		logger.debug(""
				+ "\n##############################"
				+ "\n##############################"
				+ "\n###### marco de endosos ######"
				+ "\n######                  ######"
				);
		success=true;
		logger.debug(""
				+ "\n######                  ######"
				+ "\n###### marco de endosos ######"
				+ "\n##############################"
				+ "\n##############################"
				);
		return SUCCESS;
	}
	/*//////////////////////////*/
	////// marco de endosos //////
	//////////////////////////////
	
	/////////////////////////////
	////// obtener endosos //////
	/*/////////////////////////*/
	public String obtenerEndosos()
	{
		logger.debug(""
				+ "\n############################"
				+ "\n############################"
				+ "\n###### obtenerEndosos ######"
				+ "\n######                ######"
				);
		try
		{
			smap1.put("pv_cdsisrol_i", ((UserVO)session.get("USUARIO")).getRolActivo().getClave());
			logger.debug("smap1: "+smap1);
			slist1=endososManager.obtenerEndosos(smap1);
			success=true;
		}
		catch(Exception ex)
		{
			logger.error("error al obtener los endosos",ex);
			success=false;
		}
		logger.debug(""
				+ "\n######                ######"
				+ "\n###### obtenerEndosos ######"
				+ "\n############################"
				+ "\n############################"
				);
		return SUCCESS;
	}
	
	public String validarCP()
	{
		logger.debug(""
				+ "\n############################"
				+ "\n############################"
				+ "\n###### validarCP ###########"
				+ "\n######                ######"
				);
		
		try
		{
			endososManager.validaEstadoCodigoPostal(parametros);
			
		}
		catch(Exception ex)
		{
			logger.error("Error en Valida CP",ex);
			this.mensaje = ex.getMessage();
			
		}
		logger.debug(""
				+ "\n######                ######"
				+ "\n###### validarCP ###########"
				+ "\n############################"
				+ "\n############################"
				);
		success=true;
		return SUCCESS;
	}
	/*/////////////////////////*/
	////// obtener endosos //////
	/////////////////////////////
	
	//////////////////////////////////////////////
	////// pantalla de endoso de coberturas //////
	/*
	smap1.pv_cdunieco:1006
	smap1.pv_cdramo:2
	smap1.pv_estado:M
	smap1.pv_nmpoliza:18
	smap1.pv_nmsituac:2
	smap1.pv_cdperson:512022
	smap1.cdrfc:MAVA900817
	smap1.pv_cdrol:2
	smap1.nombreAsegurado:NOMBRE  PATERNO MATERNO
	smap1.ntramite:662
	smap1.botonCopiar:0
	smap1.altabaja:alta
	smap1.cdtipsit:SL
	*/
	/*//////////////////////////////////////////*/
	public String pantallaEndosoCoberturas() {
		
		logger.debug(new StringBuilder()
	        .append("\n######################################")
	        .append("\n######################################")
	        .append("\n###### pantallaEndosoCoberturas ######")
	        .append("\n######                          ######").toString());
		logger.debug(new StringBuilder("smap1: ").append(smap1).toString());
		RespuestaVO resp = null;
		
		try {
			/*
			// Valida si hay un endoso anterior pendiente:
			resp = endososManager.validaEndosoAnterior(smap1.get("pv_cdunieco"), smap1.get("pv_cdramo"),
									smap1.get("pv_estado"), smap1.get("pv_nmpoliza"), 
									smap1.get("altabaja").equalsIgnoreCase("alta") ?
											TipoEndoso.ALTA_COBERTURAS.getCdTipSup().toString():
											TipoEndoso.BAJA_COBERTURAS.getCdTipSup().toString());
			error = resp.getMensaje();
			*/
			
			// Si el origen es del marco general de endosos, asignamos el tipo de endoso:
			if("MARCO_ENDOSOS_GENERAL".equals(smap1.get("pantallaOrigen"))) {
				if(smap1.get("cdtipsup").equals(TipoEndoso.ALTA_COBERTURAS.getCdTipSup().toString())) {
					smap1.put("altabaja", "alta");
				} else {
					smap1.put("altabaja", "baja");
				}
			}
			
			//Se obtienen las columnas del grid de incisos:
			List<ComponenteVO> componentes = pantallasManager.obtenerComponentes(null, null, smap1.get("CDRAMO"), null, null, null, "ENDOSO_COBERTURA", "GRID_INCISOS", null);
			GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			gc.generaComponentes(componentes, true, false, false, true, false, false);
			columnas = gc.getColumns().toString();
			logger.debug("columnasTatrisit=" + columnas);
			try{
				slist2=consultasPolizaManager.obtieneCoberturas(smap1.get("CDRAMO"), smap1.get("CDTIPSIT"), smap1.get("CDMEJRED"));
			}catch(Exception ex){
				logger.error("Error obteniendo coberturas. {}",ex);
				
			}
			
		} catch (Exception e) {
			Utils.manejaExcepcion(e);
		}
		
		logger.debug(new StringBuilder()
		        .append("\n######                          ######")
		        .append("\n###### pantallaEndosoCoberturas ######")
		        .append("\n######################################")
		        .append("\n######################################").toString());
		
		//return resp.isSuccess() ? SUCCESS : ERROR;
		return SUCCESS;
	}
	/*//////////////////////////////////////////*/
	////// pantalla de endoso de coberturas //////
	//////////////////////////////////////////////

	//////////////////////////////////////////////
	////// pantalla de endoso de domicilio  //////
	/*
	smap1.pv_cdunieco:1006
	smap1.pv_cdramo:2
	smap1.pv_estado:M
	smap1.pv_nmpoliza:18
	smap1.pv_nmsituac:2
	smap1.pv_cdperson:512022
	smap1.cdrfc:MAVA900817
	smap1.pv_cdrol:2
	smap1.nombreAsegurado:NOMBRE  PATERNO MATERNO
	smap1.botonCopiar:0
	smap1.cdtipsit:SL
	smap1.ntramite:662
	*/
	/*//////////////////////////////////////////*/
	public String pantallaEndosoDomicilio()
	{
		
		String respuestaAction = ERROR;
		
		try
		{
			if(!smap1.containsKey("pv_nmpoliza") && smap1.containsKey("NMPOLIZA")){
				String cdtipsit1 = null;
				String nombrePersona = null;
				try {
					cdtipsit1 = endososManager.recuperarCdtipsitInciso1((String)smap1.get("CDUNIECO"), (String)smap1.get("CDRAMO"), (String)smap1.get("ESTADO"), (String)smap1.get("NMPOLIZA"));
					
					long timestamp = System.currentTimeMillis();
					Map<String, Object> datos = personasManager.obtenerPersonaPorCdperson(smap1.get("CDPERSON"), timestamp);
					Map<String,String> persona = (Map<String,String>)datos.get("persona");
					
					nombrePersona = persona.get("DSNOMBRE")+" "+(StringUtils.isNotBlank(persona.get("DSAPELLIDO1"))?persona.get("DSAPELLIDO1"):"");
					
				} catch (Exception e) {
					logger.error("Error al obtener el cdtipsit de la poliza para el primer inciso",e);
				}
				
				smap1.put("pv_nmpoliza", smap1.get("NMPOLIZA"));
				smap1.put("pv_estado", smap1.get("ESTADO"));
				smap1.put("cdrfc", smap1.get("CDRFC"));
				smap1.put("pv_nmsituac", null);
				smap1.put("pv_cdrol", null);
				smap1.put("pv_cdunieco", smap1.get("CDUNIECO"));
				smap1.put("ntramite", smap1.get("NTRAMITE"));
				smap1.put("habilitaEdicion", "0");
				smap1.put("botonCopiar", "0");
				smap1.put("pv_cdramo", smap1.get("CDRAMO"));
				smap1.put("cdtipsit", cdtipsit1);
				smap1.put("pv_cdperson", smap1.get("CDPERSON"));
				smap1.put("nombreAsegurado", nombrePersona);
				
			}
			
			/**
			 * Si no trae la lista de incisos se considera en automatico que es para el contratante
			 */
			if(slist1 ==  null || slist1.isEmpty()){
				
				logger.debug("Rellenando slist1 para contratante");
				String nmsitucRes = consultasPolizaManager.obtieneNmsituacContratantePoliza(smap1.get("pv_cdunieco_i"), smap1.get("pv_cdramo_i"), smap1.get("pv_estado_i"), smap1.get("pv_nmpoliza_i"));
				slist1 = new ArrayList<Map<String,String>>();
				
				HashMap<String,String> contratante =  new HashMap<String, String>(); 
				contratante.put("CDPERSON", smap1.get("CDPERSON"));
				contratante.put("DSNOMBRE", smap1.get("DSNOMBRE"));
				contratante.put("DSNOMBRE1", smap1.get("DSNOMBRE1"));
				contratante.put("DSAPELLIDO", smap1.get("DSAPELLIDO"));
				contratante.put("DSAPELLIDO1", smap1.get("DSAPELLIDO1"));
				contratante.put("CDRFC", smap1.get("CDRFC"));
				contratante.put("NMSITUAC", nmsitucRes);
				contratante.put("CDROL", "1");
				
				slist1.add(contratante);
			}
			
			logger.debug(Utils.log(
			         "\n#####################################"
			        ,"\n###### pantallaEndosoDomicilio ######"
			        ,"\n###### smap1="  , smap1
			        ,"\n###### slist1=" , slist1
			        ));
			
			
			transformaEntrada(smap1, slist1, true);
			
			//Si despues de transforma entrada no hay cdrol de contratante o inciso se deja default el rol 1
			if(StringUtils.isBlank(smap1.get("pv_cdrol"))){
				smap1.put("pv_cdrol", "1");
			}
			
			// Valida si hay un endoso anterior pendiente:
			RespuestaVO resp = endososManager.validaEndosoAnterior(smap1.get("pv_cdunieco"), smap1.get("pv_cdramo"),
					smap1.get("pv_estado"), smap1.get("pv_nmpoliza"),
					TipoEndoso.CAMBIO_DOMICILIO.getCdTipSup().toString());
			if(!resp.isSuccess())
			{
				throw new ApplicationException(resp.getMensaje());
			}
			error = resp.getMensaje();
			
			ComplementariosCoberturasAction actionDomicilio=new ComplementariosCoberturasAction();
			actionDomicilio.setSession(session);
			actionDomicilio.setSmap1(smap1);
			actionDomicilio.setKernelManager(kernelManager);
			actionDomicilio.setPantallasManager(pantallasManager);
			actionDomicilio.mostrarPantallaDomicilio();
			item1=actionDomicilio.getItem1();
			item2=actionDomicilio.getItem2();
			item3=actionDomicilio.getItem3();
			
			respuestaAction = SUCCESS;
		}
		catch(Exception ex)
		{
			logger.error("error al cargar pantalla de endoso de valosit simple",ex);
			error=ex.getMessage();
			respuestaAction = ERROR;
		}
		
		logger.debug(Utils.log(
				 "\n###### respuestaAction=",respuestaAction
				,"\n###### pantallaEndosoDomicilio ######"
		        ,"\n#####################################"
		        ));
		
		return respuestaAction;
	}
	/*//////////////////////////////////////////*/
	////// pantalla de endoso de domicilio Auto  //////
	//////////////////////////////////////////////

	//////////////////////////////////////////////
	////// pantalla de endoso de domicilio  //////
	/*
	smap1.pv_cdunieco:1006
	smap1.pv_cdramo:2
	smap1.pv_estado:M
	smap1.pv_nmpoliza:18
	smap1.pv_nmsituac:2
	smap1.pv_cdperson:512022
	smap1.cdrfc:MAVA900817
	smap1.pv_cdrol:2
	smap1.nombreAsegurado:NOMBRE  PATERNO MATERNO
	smap1.botonCopiar:0
	smap1.cdtipsit:SL
	smap1.ntramite:662
	 */
	/*//////////////////////////////////////////*/
	public String endosoDomicilioAuto()
	{
		
		smap1.put("pv_cdunieco", smap1.get("CDUNIECO"));
		smap1.put("pv_cdramo", smap1.get("CDRAMO"));
		smap1.put("pv_estado", smap1.get("ESTADO"));
		smap1.put("pv_nmpoliza", smap1.get("NMPOLIZA"));
		smap1.put("pv_cdperson", smap1.get("CDPERSON"));
		
		logger.debug(new StringBuilder()
		.append("\n#########################################")
		.append("\n#########################################")
		.append("\n###### pantallaEndosoDomicilioAuto ######")
		.append("\n######                         ######").toString());
		logger.debug(new StringBuilder("smap1: ").append(smap1).toString());
		logger.debug(new StringBuilder("session: ").append(session).toString());
		
		// Valida si hay un endoso anterior pendiente:
		RespuestaVO resp = endososManager.validaEndosoAnterior(smap1.get("pv_cdunieco"), smap1.get("pv_cdramo"),
				smap1.get("pv_estado"), smap1.get("pv_nmpoliza"),
				TipoEndoso.CAMBIO_DOMICILIO.getCdTipSup().toString());
		error = resp.getMensaje();
		
		if(resp.isSuccess()) {
			ComplementariosCoberturasAction actionDomicilio=new ComplementariosCoberturasAction();
			actionDomicilio.setSession(session);
			actionDomicilio.setSmap1(smap1);
			actionDomicilio.setKernelManager(kernelManager);
			actionDomicilio.setPantallasManager(pantallasManager);
			actionDomicilio.mostrarPantallaDomicilio();
			item1=actionDomicilio.getItem1();
			//item2=actionDomicilio.getItem2();
			item3=actionDomicilio.getItem3();
			
		}
		
		logger.debug(new StringBuilder()
		.append("\n######                         ######")
		.append("\n###### pantallaEndosoDomicilio ######")
		.append("\n#####################################")
		.append("\n#####################################").toString());
		
		return resp.isSuccess() ? SUCCESS : ERROR;
	}
	/*//////////////////////////////////////////*/
	////// pantalla de endoso de domicilio  //////
	//////////////////////////////////////////////
	
	public String endosoAseguradoAlterno()
	{
		
		smap1.put("pv_cdunieco", smap1.get("CDUNIECO"));
		smap1.put("pv_cdramo", smap1.get("CDRAMO"));
		smap1.put("pv_estado", smap1.get("ESTADO"));
		smap1.put("pv_nmpoliza", smap1.get("NMPOLIZA"));
		smap1.put("pv_cdperson", smap1.get("CDPERSON"));
		
		logger.debug(new StringBuilder()
		.append("\n#####################################")
		.append("\n#####################################")
		.append("\n###### endosoAseguradoAlterno ######")
		.append("\n######                         ######").toString());
		logger.debug(new StringBuilder("smap1: ").append(smap1).toString());
		logger.debug(new StringBuilder("session: ").append(session).toString());		
		logger.debug(new StringBuilder()
		.append("\n######                         ######")
		.append("\n###### endosoAseguradoAlterno  ######")
		.append("\n#####################################")
		.append("\n#####################################").toString());
		
		return SUCCESS;
	}
	
	public String guardarEndosoAseguradoAlterno()
	{
		logger.debug(Utils.log(
				 "\n###########################################"
				,"\n###### guardarEndosoAseguradoAlterno ######"
				,"\n###### smap1=" , smap1
				,"\n###### flujo=" , flujo
				));
		
		this.session = ActionContext.getContext().getSession();
		
		try
		{
			String paso = null;
			
			try  //Esto deberia ir en un manager
			{
				UserVO usuario = Utils.validateSession(session);
				
				String cdelemen = usuario.getEmpresa().getElementoId();
				String cdusuari = usuario.getUser();
				String cdsisrol = usuario.getRolActivo().getClave();
				String cdtipsup = TipoEndoso.ASEGURADO_ALTERNO.getCdTipSup().toString();
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				
				String fechaEndoso = sdf.format(new Date());
				
				Date dFechaEndoso = renderFechas.parse(fechaEndoso);
				
				paso = "Iniciando endoso";
				logger.debug(paso);
				
				Map<String,String>respuestaIniciarEndoso = endososManager.iniciarEndoso(
						smap1.get("CDUNIECO")
						,smap1.get("CDRAMO")
						,smap1.get("ESTADO")
						,smap1.get("NMPOLIZA")
						,fechaEndoso
						,cdelemen
						,cdusuari
						,"END"
						,cdtipsup
						);
				
				//String nmsuplem= smap1.get("NMSUPLEM");
				String nmsuplem = respuestaIniciarEndoso.get("pv_nmsuplem_o");
				String nsuplogi = respuestaIniciarEndoso.get("pv_nsuplogi_o");
				
				logger.debug(Utils.log("smap1.size=",smap1.size()));
				//Generar un map, con los valores que vienen por default
				
				paso = "Generando valores variables";
				logger.debug(paso);
				
				Map<String,String> otvalores = new HashMap<String,String>();
				for(int i = 1; i<= 50; i++){
					if(i <10){
						otvalores.put("otvalor0"+i, smap1.get("OTVALOR0"+i));
					}else{
						otvalores.put("otvalor"+i, smap1.get("OTVALOR"+i));
					}
				}
				
				paso = "Guardando atributos de p\u00f3liza";
				logger.debug(paso);
				
				//2.- Mandamos a guardar la informaci�n de TVALOPOL
				cotizacionDAO.movimientoTvalopol(smap1.get("CDUNIECO"), smap1.get("CDRAMO"),smap1.get("ESTADO"), smap1.get("NMPOLIZA"), nmsuplem, smap1.get("STATUS"), otvalores);
				
		   		//// Se confirma el endoso si cumple la validacion de fechas:
				RespuestaConfirmacionEndosoVO respConfirmacionEndoso = this.confirmarEndoso(
						smap1.get("CDUNIECO")
						,smap1.get("CDRAMO")
						,smap1.get("ESTADO")
						,smap1.get("NMPOLIZA")
						,nmsuplem
						,nsuplogi
						,cdtipsup
						,""
						,dFechaEndoso
						,null
						,flujo
						,cdusuari
						,cdsisrol
						);
				
				// Si el endoso fue confirmado:
				if(respConfirmacionEndoso.isConfirmado()) {
					endosoConfirmado = true;
					mensaje = "Endoso generado";
					if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
	                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
	                }
	            } else {
					mensaje = new StringBuilder().append("El endoso ").append(nsuplogi)
							.append(" se guard\u00f3 en mesa de control para autorizaci\u00f3n ")
							.append("con n\u00famero de tr\u00e1mite ").append(respConfirmacionEndoso.getNumeroTramite()).toString();
					if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
	                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
	                }
				}
				
				success = true;
			}
			catch(Exception ex)
			{
				Utils.generaExcepcion(ex, paso);
			}
		}
		catch(Exception ex)
		{
			error = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success=" , success
				,"\n###### mensaje=" , mensaje
				,"\n###### error="   , error
				,"\n###### guardarEndosoAseguradoAlterno ######"
				,"\n###########################################"
				));
		return SUCCESS;
	}
	
	public String endosoVigenciaPoliza()
	{
		smap1.put("pv_cdunieco", smap1.get("CDUNIECO"));
		smap1.put("pv_cdramo", smap1.get("CDRAMO"));
		smap1.put("pv_estado", smap1.get("ESTADO"));
		smap1.put("pv_nmpoliza", smap1.get("NMPOLIZA"));
		smap1.put("pv_cdperson", smap1.get("CDPERSON"));
		
		String FEEFECTO[] = smap1.get("FEEFECTO").toString().split("\\/");
		String FEPROREN[] = smap1.get("FEPROREN").toString().split("\\/");
		Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        // Establecer las fechas
        cal1.set(Integer.parseInt(FEEFECTO[2].toString()), Integer.parseInt(FEEFECTO[1].toString()) , Integer.parseInt(FEEFECTO[0].toString()));
        cal2.set(Integer.parseInt(FEPROREN[2].toString()), Integer.parseInt(FEPROREN[1].toString()) , Integer.parseInt(FEPROREN[0].toString()));
        long milis1 = cal1.getTimeInMillis();
        long milis2 = cal2.getTimeInMillis();
        long diff = milis2 - milis1;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        
		smap1.put("pv_difDate",diffDays+"");
		logger.debug(new StringBuilder()
		.append("\n#####################################")
		.append("\n#####################################")
		.append("\n###### endosoAseguradoAlterno ######")
		.append("\n######                         ######").toString());
		logger.debug(new StringBuilder("smap1: ").append(smap1).toString());
		logger.debug(new StringBuilder("session: ").append(session).toString());		
		logger.debug(new StringBuilder()
		.append("\n######                         ######")
		.append("\n###### endosoAseguradoAlterno  ######")
		.append("\n#####################################")
		.append("\n#####################################").toString());
		
		return SUCCESS;
	}
	
	public String guardarEndosoVigenciaPoliza() {
        
		logger.debug(new StringBuilder()
				.append("\n###########################################")
				.append("\n###########################################")
				.append("\n######  guardarEndosoCambioVigencia  ######")
				.append("\n###### smap1=").append(smap1)
				.append("\n######                               ######").toString());
		
		this.session = ActionContext.getContext().getSession();
        UserVO usuario = (UserVO) session.get("USUARIO");
		
		try {
			
			String cdelemen     = usuario.getEmpresa().getElementoId();
			String cdusuari     = usuario.getUser();
			String cdsisrol     = usuario.getRolActivo().getClave();
			String cdtipsup     = TipoEndoso.VIGENCIA_POLIZA.getCdTipSup().toString();
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			String fechaEndoso = sdf.format(new Date());
			Date   dFechaEndoso = renderFechas.parse(fechaEndoso);
			// Se inicia endoso:
			Map<String,String>paramsIniciarEndoso=new HashMap<String,String>(0);
			paramsIniciarEndoso.put("pv_cdunieco_i" , smap1.get("CDUNIECO"));
			paramsIniciarEndoso.put("pv_cdramo_i"   , smap1.get("CDRAMO"));
			paramsIniciarEndoso.put("pv_estado_i"   , smap1.get("ESTADO"));
			paramsIniciarEndoso.put("pv_nmpoliza_i" , smap1.get("NMPOLIZA"));
			paramsIniciarEndoso.put("pv_fecha_i"    , fechaEndoso);
			paramsIniciarEndoso.put("pv_cdelemen_i" , cdelemen);
			paramsIniciarEndoso.put("pv_cdusuari_i" , cdusuari);
			paramsIniciarEndoso.put("pv_proceso_i"  , "END");
			paramsIniciarEndoso.put("pv_cdtipsup_i" , cdtipsup);
			//1.- Mandamos a iniciar el endoso 
			Map<String,String>respuestaIniciarEndoso=endososManager.iniciarEndoso(paramsIniciarEndoso);
			//String nmsuplem= smap1.get("NMSUPLEM");
			String nmsuplem=respuestaIniciarEndoso.get("pv_nmsuplem_o");
			String nsuplogi=respuestaIniciarEndoso.get("pv_nsuplogi_o");
			
			// Realizamos la modificacion de las fechas de FEEFECTO y FEPROREN
			endososManager.actualizaVigenciaPoliza(smap1.get("CDUNIECO") ,smap1.get("CDRAMO") , smap1.get("ESTADO"),smap1.get("NMPOLIZA"), nmsuplem, smap1.get("FEEFECTO"), smap1.get("FEPROREN")  );
			
			// Se confirma el endoso si cumple la validacion de fechas: 
			RespuestaConfirmacionEndosoVO respConfirmacionEndoso = this.confirmarEndoso(
					smap1.get("CDUNIECO")
					,smap1.get("CDRAMO")
					,smap1.get("ESTADO")
					,smap1.get("NMPOLIZA")
					,nmsuplem
					,nsuplogi
					,cdtipsup
					,""
					,dFechaEndoso
					,null
					,flujo
					,cdusuari
					,cdsisrol
					);
			
			// Si el endoso fue confirmado:
			if(respConfirmacionEndoso.isConfirmado()) {
				endosoConfirmado = true;
				mensaje = "Endoso generado";
				if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
				    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
				}
            } else {
				mensaje = new StringBuilder().append("El endoso ").append(nsuplogi)
						.append(" se guard\u00f3 en mesa de control para autorizaci\u00f3n ")
						.append("con n\u00famero de tr\u00e1mite ").append(respConfirmacionEndoso.getNumeroTramite()).toString();
				if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
                }
			}
			success = true;

		} catch(Exception ex) {
			logger.error("Error al generar endoso de asegurado Alterno",ex);
			success = false;
			error   = ex.getMessage();
		}
		logger.debug(new StringBuilder()
				.append("\n######                               ######")
				.append("\n######  guardarEndosoCambioVigencia  ######")
				.append("\n###########################################")
				.append("\n###########################################").toString());
		return SUCCESS;
	}
	
	
	//////////////////////////////////////////////
	////// pantalla de endoso de domicilio  //////
	/*
	smap1.pv_cdunieco:1006
	smap1.pv_cdramo:2
	smap1.pv_estado:M
	smap1.pv_nmpoliza:18
	smap1.pv_nmsituac:2
	smap1.pv_cdperson:512022
	smap1.cdrfc:MAVA900817
	smap1.pv_cdrol:2
	smap1.nombreAsegurado:NOMBRE  PATERNO MATERNO
	smap1.botonCopiar:0
	smap1.cdtipsit:SL
	smap1.ntramite:662
	*/
	/*//////////////////////////////////////////*/
	public String pantallaEndosoDomicilioSimple()
	{
		logger.debug(Utils.log(
				 "\n###########################################"
				,"\n###### pantallaEndosoDomicilioSimple ######"
				,"\n###### smap1="  , smap1
				,"\n###### slist1=" , slist1
				));
		
		String respuesta = ERROR;
		endosoSimple     = true;
		
		try
		{
			transformaEntrada(smap1, slist1, true);
			
			String cdunieco = smap1.get("pv_cdunieco");
			String cdramo   = smap1.get("pv_cdramo");
			String estado   = smap1.get("pv_estado");
			String nmpoliza = smap1.get("pv_nmpoliza");
			
			// Se valida si el endoso es permitido:
			boolean permitido = endososManager.validaEndosoSimple(cdunieco, cdramo, estado, nmpoliza);
			if(!permitido) {
				throw new ApplicationException(EndososAction.ENDOSO_SIMPLE_NO_PERMITIDO);
			}
			
			// Valida si hay un endoso anterior pendiente, sino lanzamos una excepcion con el mensaje de error:
			RespuestaVO resp = endososManager.validaEndosoAnterior(smap1.get("pv_cdunieco"), smap1.get("pv_cdramo"),
					smap1.get("pv_estado"), smap1.get("pv_nmpoliza"),
					TipoEndoso.CORRECCION_DATOS_ASEGURADOS.getCdTipSup().toString());
			if(!resp.isSuccess()) {
				throw new Exception(resp.getMensaje());
			}
			
			// Obtenemos la fecha de inicio de vigencia de la poliza:
			mensaje = endososManager.obtieneFechaInicioVigenciaPoliza(cdunieco, cdramo, estado, nmpoliza);
		
			ComplementariosCoberturasAction actionDomicilio=new ComplementariosCoberturasAction();
			actionDomicilio.setSession(session);
			actionDomicilio.setSmap1(smap1);
			actionDomicilio.setKernelManager(kernelManager);
			actionDomicilio.setPantallasManager(pantallasManager);
			actionDomicilio.mostrarPantallaDomicilio();
			item1=actionDomicilio.getItem1();
			item2=actionDomicilio.getItem2();
			item3=actionDomicilio.getItem3();
			
			respuesta = SUCCESS;
		}
		catch(Exception ex)
		{
			logger.error("error al cargar pantalla de endoso de valosit simple",ex);
			error=ex.getMessage();
			respuesta = ERROR;
		}
		
		logger.debug(Utils.log(
				 "\n###### pantallaEndosoDomicilioSimple ######"
				,"\n###########################################"
				));
		
		return respuesta;
	}
	/*//////////////////////////////////////////*/
	////// pantalla de endoso de domicilio  //////
	//////////////////////////////////////////////
	
	///////////////////////////////////////////
	////// pantalla de endoso de nombres //////
	/*
	smap1:
		cdramo: "2"
		cdtipsit: "SL"
		cdunieco: "1006"
		estado: "M"
		nmpoliza: "18"
		ntramite: "662"
	slist1:
		[{Apellido_Materno: "MATERNO"
		Apellido_Paterno: "PATERNO"
		Parentesco: "D"
		cdperson: "512022"
		cdrfc: "MAVA900817"
		cdrol: "2"
		fenacimi: "17/08/1990"
		nacional: "001"
		nmsituac: "2"
		nombre: "NOMBRE"
		segundo_nombre: null
		sexo: "M"
		swexiper: "S"
		tpersona: "F"}]
	*/
	/*///////////////////////////////////////*/
	public String pantallaEndosoNombres()
	{
		logger.debug(Utils.log(
				 "\n###################################"
				,"\n###### pantallaEndosoNombres ######"
				,"\n###### smap1="  , smap1
				,"\n###### slist1=" , slist1
				));
		
		RespuestaVO resp = null;
		
		try
		{
			
			/**
			 * Si no trae la lista de incisos se considera en automatico que es para el contratante
			 */
			if(slist1 ==  null || slist1.isEmpty()){
				
				logger.debug("Rellenando slist1 para contratante");
				String nmsitucRes = consultasPolizaManager.obtieneNmsituacContratantePoliza(smap1.get("pv_cdunieco_i"), smap1.get("pv_cdramo_i"), smap1.get("pv_estado_i"), smap1.get("pv_nmpoliza_i"));
				slist1 = new ArrayList<Map<String,String>>();
				
				HashMap<String,String> contratante =  new HashMap<String, String>(); 
				contratante.put("CDPERSON", smap1.get("CDPERSON"));
				
				contratante.put("OTFISJUR", smap1.get("OTFISJUR"));

				contratante.put("DSNOMBRE", smap1.get("DSNOMBRE"));
				contratante.put("DSNOMBRE1", smap1.get("DSNOMBRE1"));
				contratante.put("DSAPELLIDO", smap1.get("DSAPELLIDO"));
				contratante.put("DSAPELLIDO1", smap1.get("DSAPELLIDO1"));
				contratante.put("CDRFC", smap1.get("CDRFC"));
				contratante.put("NMSITUAC", nmsitucRes);
				contratante.put("nmsituac", nmsitucRes);
				contratante.put("CDROL", "1");
				
				slist1.add(contratante);
			}
			
			transformaEntrada(smap1, slist1, true);
			
			//Valida si hay un endoso anterior pendiente:
			resp = endososManager.validaEndosoAnterior(
					smap1.get("cdunieco"), smap1.get("cdramo"), smap1.get("estado"), smap1.get("nmpoliza"),
					TipoEndoso.CORRECCION_NOMBRE_Y_RFC.getCdTipSup().toString());
			error = resp.getMensaje();
		}
		catch(Exception ex)
		{
			logger.debug("Error al convertir entrada al endoso de nombres",ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### pantallaEndosoNombres ######"
				,"\n###################################"
				));
		
		return resp!=null&&resp.isSuccess() ? SUCCESS : ERROR;
	}
	/*///////////////////////////////////////*/
	////// pantalla de endoso de nombres //////
	///////////////////////////////////////////
	
	///////////////////////////////////////////
	////// pantalla de endoso de nombres //////
	/*
	smap1:
		cdramo: "2"
		cdtipsit: "SL"
		cdunieco: "1006"
		estado: "M"
		nmpoliza: "18"
		ntramite: "662"
	slist1:
		[{Apellido_Materno: "MATERNO"
		Apellido_Paterno: "PATERNO"
		Parentesco: "D"
		cdperson: "512022"
		cdrfc: "MAVA900817"
		cdrol: "2"
		fenacimi: "17/08/1990"
		nacional: "001"
		nmsituac: "2"
		nombre: "NOMBRE"
		segundo_nombre: null
		sexo: "M"
		swexiper: "S"
		tpersona: "F"}]
	*/
	/*///////////////////////////////////////*/
	public String pantallaEndosoNombresSimple()
	{
		logger.debug(Utils.log(
				 "\n#########################################"
				,"\n###### pantallaEndosoNombresSimple ######"
				,"\n###### smap1="  , smap1
				,"\n###### slist1=" , slist1
				));
		
		String respuesta = ERROR;
		
		endosoSimple = true;
		
		try
		{
			transformaEntrada(smap1,slist1,true);
			
			String cdunieco = smap1.get("cdunieco");
			String cdramo   = smap1.get("cdramo");
			String estado   = smap1.get("estado");
			String nmpoliza = smap1.get("nmpoliza");
			
			// Validacion de endoso simple:
			boolean permitido = endososManager.validaEndosoSimple(cdunieco, cdramo, estado, nmpoliza);
			if(!permitido) {
				throw new ApplicationException(EndososAction.ENDOSO_SIMPLE_NO_PERMITIDO);
			}
			
			// Valida si hay un endoso anterior pendiente, sino lanzamos una excepcion con el mensaje de error:
			RespuestaVO resp = endososManager.validaEndosoAnterior(
					smap1.get("cdunieco"), smap1.get("cdramo"), smap1.get("estado"), smap1.get("nmpoliza"),
					TipoEndoso.CORRECCION_DATOS_ASEGURADOS.getCdTipSup().toString());
			if(!resp.isSuccess()) {
				throw new Exception(resp.getMensaje());
			}
			
			mensaje = endososManager.obtieneFechaInicioVigenciaPoliza(cdunieco, cdramo, estado, nmpoliza);
			
			respuesta = SUCCESS;
		}
		catch(Exception ex)
		{
			logger.error("Error al abrir la pantalla de endoso de nombres simple", ex);
			respuesta = ERROR;
			error = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### pantallaEndosoNombresSimple ######"
				,"\n#########################################"
				));
		
		return respuesta;
	}
	/*///////////////////////////////////////*/
	////// pantalla de endoso de nombres //////
	///////////////////////////////////////////
	
	////////////////////////////////////////////////////
	////// generar el endoso de cambio de nombres //////
	/*////////////////////////////////////////////////*/
	public String guardarEndosoNombres() {
		/*
		 * se obtiene la sesion manualmente por el enableSMD de struts...xml
		 */
		this.session=ActionContext.getContext().getSession();
        UserVO usuario=(UserVO) session.get("USUARIO");
		
        logger.debug(Utils.log(
				 "\n##################################"
				,"\n###### guardarEndosoNombres ######"
				,"\n###### omap1  = " , omap1
				,"\n###### slist1 = " , slist1
				,"\n###### flujo  = " , flujo
				));
		
        try {
			
			String usuarioCaptura =  null;
			
			if(usuario!=null){
				if(StringUtils.isNotBlank(usuario.getClaveUsuarioCaptura())){
					usuarioCaptura = usuario.getClaveUsuarioCaptura();
				}else{
					usuarioCaptura = usuario.getCodigoPersona();
				}
			}
			
			String cdtipsit1 = null;
			try {
				cdtipsit1 = endososManager.recuperarCdtipsitInciso1((String)omap1.get("pv_cdunieco_i"),
						(String)omap1.get("pv_cdramo_i"),
						(String)omap1.get("pv_estado_i"),
						(String)omap1.get("pv_nmpoliza_i"));
			} catch (Exception e) {
				logger.error("Error al obtener el cdtipsit de la poliza para el primer inciso",e);
			}
			
			String nmsituac = "0"; //valor default todas
			
			if(slist1 !=null && !slist1.isEmpty() && slist1.size() == 1){
				Map<String,String> personaEndoso =  slist1.get(0);//temporalmente solo se toma uno 
				if(personaEndoso.containsKey("NMSITUAC") && StringUtils.isNotBlank(personaEndoso.get("NMSITUAC"))){
					nmsituac = personaEndoso.get("NMSITUAC");
				}
			}
			
			/*
			 * ya viene en el omap1 desde el jsp:
			 * pv_cdunieco_i
			 * pv_cdramo_i
			 * pv_estado_i
			 * pv_nmpoliza_i
			 * pv_fecha_i
			 * 
			 * hay que poner:
			 * pv_cdelemen_i
			 * pv_cdusuari_i
			 * pv_proceso_i
			 * pv_cdtipsup_i
			 */
			omap1.put("pv_cdelemen_i" , usuario.getEmpresa().getElementoId());
			omap1.put("pv_cdusuari_i" , usuario.getUser());
			omap1.put("pv_proceso_i"  , "END");
			omap1.put("pv_cdtipsup_i", TipoEndoso.CORRECCION_NOMBRE_Y_RFC.getCdTipSup().toString());
			
			/*
			 * sobreescribir la fecha con object
			 */
			omap1.put("pv_fecha_i",renderFechas.parse((String)omap1.get("pv_fecha_i")));
			
			Map<String,String> respuestaEndosoNombres=endososManager.guardarEndosoNombres(omap1);
			
			for(Map<String,String>persona:slist1) {
				Map<String,String>paramPersona=new HashMap<String,String>();
				paramPersona.put("pv_cdperson_i"    , persona.get("cdperson"));
				paramPersona.put("pv_dsnombre_i"    , persona.get("nombre"));
				paramPersona.put("pv_dsnombre1_i"   , persona.get("nombre2"));
				paramPersona.put("pv_dsapellido_i"  , persona.get("apat"));
				paramPersona.put("pv_dsapellido1_i" , persona.get("amat"));
				paramPersona.put("pv_cdrfc_i"       , persona.get("rfc"));
				
				/**
				 * Se acutalizan nombre y rfc aunque no sean contrantates, se actualizan por cdperson
				 */
				this.endososManager.actualizaNombreCliente(paramPersona);
				this.endososManager.actualizaRfcCliente(paramPersona);
				
				
				/**
				 * Se inserta tworksup para que el programa de impresion sepa que situaciones tomar para imrpimir
				 */
				
				//////////////////////////////
			    ////// inserta tworksup //////
			    endososManager.movimientoTworksupEnd((String)omap1.get("pv_cdunieco_i"),
						(String)omap1.get("pv_cdramo_i"),
						(String)omap1.get("pv_estado_i"),
						(String)omap1.get("pv_nmpoliza_i")
			    		,TipoEndoso.CORRECCION_NOMBRE_Y_RFC.getCdTipSup().toString()
			    		,respuestaEndosoNombres.get("pv_nmsuplem_o")
			    		,persona.get("NMSITUAC"), "I"
			    		);
			    ////// inserta tworksup //////
				
				
			}
			
			// Se confirma el endoso si cumple la validacion de fechas:
			RespuestaConfirmacionEndosoVO respConfirmacionEndoso = this.confirmarEndoso((String)omap1.get("pv_cdunieco_i"),
					(String)omap1.get("pv_cdramo_i"),
					(String)omap1.get("pv_estado_i"),
					(String)omap1.get("pv_nmpoliza_i"),
					respuestaEndosoNombres.get("pv_nmsuplem_o"),
					respuestaEndosoNombres.get("pv_nsuplogi_o"),
					TipoEndoso.CORRECCION_NOMBRE_Y_RFC.getCdTipSup().toString(),
					"",
					(Date)omap1.get("pv_fecha_i"),
					cdtipsit1
					,flujo
					,usuario.getUser()
					,usuario.getRolActivo().getClave()
					);
		    
			// Si el endoso fue confirmado:
			if(respConfirmacionEndoso.isConfirmado()) {
				endosoConfirmado = true;
				
				//Se inserta Tworksup para Impresiones
				for(Map<String,String>persona:slist1) {
					//////////////////////////////
				    ////// inserta tworksup //////
					String nmSituacParaWorksup = (StringUtils.isNotBlank(persona.get("NMSITUAC"))
													&& !persona.get("NMSITUAC").equalsIgnoreCase("0")
													)?persona.get("NMSITUAC"):null;
					
				    endososManager.movimientoTworksupEnd((String)omap1.get("pv_cdunieco_i"),
							(String)omap1.get("pv_cdramo_i"),
							(String)omap1.get("pv_estado_i"),
							(String)omap1.get("pv_nmpoliza_i")
				    		,TipoEndoso.CORRECCION_NOMBRE_Y_RFC.getCdTipSup().toString()
				    		,respuestaEndosoNombres.get("pv_nmsuplem_o")
				    		,nmSituacParaWorksup, "I"
				    		);
				    ////// inserta tworksup //////
				}
				
				/*///////////////////////////////////*/
				////// re generar los documentos //////
			    ///////////////////////////////////////
				
				documentosManager.generarDocumentosParametrizados(
						(String)omap1.get("pv_cdunieco_i")
						,(String)omap1.get("pv_cdramo_i")
						,(String)omap1.get("pv_estado_i")
						,(String)omap1.get("pv_nmpoliza_i")
						,nmsituac
						,respuestaEndosoNombres.get("pv_nmsuplem_o")
						,documentosManager.PROCESO_ENDOSO
						,null //ntramite
						,null //nmsolici
						, null
						);
				
				/*
			    List<Map<String,String>>listaDocu = endososManager.reimprimeDocumentos(
			    		(String)omap1.get("pv_cdunieco_i")
			    		,(String)omap1.get("pv_cdramo_i")
			    		,(String)omap1.get("pv_estado_i")
			    		,(String)omap1.get("pv_nmpoliza_i")
			    		,respuestaEndosoNombres.get("pv_nmsuplem_o")
			    		,TipoEndoso.CORRECCION_NOMBRE_Y_RFC.getCdTipSup().toString()
			    		);
			    logger.debug("documentos que se regeneran: "+listaDocu);
			    
			    String rutaCarpeta=this.rutaDocumentosPoliza+"/"+listaDocu.get(0).get("ntramite");
			    
				//listaDocu contiene: nmsolici,nmsituac,descripc,descripl
				for(Map<String,String> docu:listaDocu) {
					logger.debug("docu iterado: "+docu);
					String nmsolici=docu.get("nmsolici");
					String nmsituac=docu.get("nmsituac");
					String descripc=docu.get("descripc");
					String descripl=docu.get("descripl");
					String url=this.rutaServidorReports
							+ "?destype=cache"
							+ "&desformat=PDF"
							+ "&userid="+this.passServidorReports
							+ "&report="+descripl
							+ "&paramform=no"
							+ "&ACCESSIBLE=YES" //parametro que habilita salida en PDF
							+ "&p_unieco="+(String)omap1.get("pv_cdunieco_i")
							+ "&p_ramo="+(String)omap1.get("pv_cdramo_i")
							+ "&p_estado="+(String)omap1.get("pv_estado_i")
							+ "&p_poliza="+(String)omap1.get("pv_nmpoliza_i")
							+ "&p_suplem="+respuestaEndosoNombres.get("pv_nmsuplem_o")
							+ "&desname="+rutaCarpeta+"/"+descripc;
					if(descripc.substring(0, 6).equalsIgnoreCase("CREDEN")) {
						// C R E D E N C I A L _ X X X X X X . P D F
						//0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
						url+="&p_cdperson="+descripc.substring(11, descripc.lastIndexOf("_"));
					}
					logger.debug(""
							+ "\n#################################"
							+ "\n###### Se solicita reporte ######"
							+ "\na "+url+""
							+ "\n#################################");
					HttpUtil.generaArchivo(url,rutaCarpeta+"/"+descripc);
					logger.debug(""
							+ "\n######                    ######"
							+ "\n###### reporte solicitado ######"
							+ "\na "+url+""
							+ "\n################################"
							+ "\n################################"
							+ "");
				}
				*/
			    /*///////////////////////////////////*/
				////// re generar los documentos //////
			    ///////////////////////////////////////
				        
				String saludDanios = "S";
				ClienteGeneral clienteGeneral = new ClienteGeneral();
				clienteGeneral.setClaveCia(saludDanios);
				
				// Ejecutamos el Web Service de Cliente Salud:
				ice2sigsService.ejecutaWSclienteGeneral((String) omap1.get("pv_cdunieco_i"),
						(String) omap1.get("pv_cdramo_i"),
						(String) omap1.get("pv_estado_i"),
						(String) omap1.get("pv_nmpoliza_i"),
						respuestaEndosoNombres.get("pv_nmsuplem_o"),
						respConfirmacionEndoso.getNumeroTramite(), null,
						Ice2sigsService.Operacion.ACTUALIZA, clienteGeneral, (UserVO) session.get("USUARIO"), false);
				
				mensaje="Se ha guardado el endoso "+respuestaEndosoNombres.get("pv_nsuplogi_o");
			} else {
				mensaje="El endoso "+respuestaEndosoNombres.get("pv_nsuplogi_o")
						+" se guard\u00f3 en mesa de control para autorizaci\u00f3n "
						+ "con n\u00famero de tr\u00e1mite " + respConfirmacionEndoso.getNumeroTramite();
				if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
                }
			}
			if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
                mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
            }
			success=true;
		} catch(Exception ex) {
			logger.error("error al generar endoso de nombres",ex);
			success=false;
			error=ex.getMessage();
		}
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### error   = " , error
				,"\n###### mensaje = " , mensaje
				,"\n###### guardarEndosoNombres ######"
				,"\n##################################"
				));
		return SUCCESS;
	}
	/*////////////////////////////////////////////////*/
	////// generar el endoso de cambio de nombres //////
	////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////
	////// generar el endoso de cambio de nombres //////
	/*////////////////////////////////////////////////*/
	public String guardarEndosoNombresSimple() {
        
		logger.debug(Utils.log(
				 "\n########################################"
				,"\n###### guardarEndosoNombresSimple ######"
				,"\n###### omap1  = " , omap1
				,"\n###### slist1 = " , slist1
				,"\n###### flujo  = " , flujo
				));
		
		// Se obtiene la sesion manualmente por el enableSMD de struts...xml:
		this.session = ActionContext.getContext().getSession();
        UserVO usuario = (UserVO) session.get("USUARIO");
		
		try {
			
			String usuarioCaptura =  null;
			
			if(usuario!=null){
				if(StringUtils.isNotBlank(usuario.getClaveUsuarioCaptura())){
					usuarioCaptura = usuario.getClaveUsuarioCaptura();
				}else{
					usuarioCaptura = usuario.getCodigoPersona();
				}
				
			}
			
			String cdelemen     = usuario.getEmpresa().getElementoId();
			String cdusuari     = usuario.getUser();
			String cdsisrol     = usuario.getRolActivo().getClave();
			String cdtipsup     = TipoEndoso.CORRECCION_DATOS_ASEGURADOS.getCdTipSup().toString();
			String fechaEndoso  = (String)omap1.get("pv_fecha_i");
			Date   dFechaEndoso = renderFechas.parse(fechaEndoso);
			String cdunieco     = (String)omap1.get("pv_cdunieco_i");
			String cdramo       = (String)omap1.get("pv_cdramo_i");
			String estado       = (String)omap1.get("pv_estado_i");
			String nmpoliza     = (String)omap1.get("pv_nmpoliza_i");
			
			// Se inicia endoso:
			Map<String,String>paramsIniciarEndoso=new HashMap<String,String>(0);
			paramsIniciarEndoso.put("pv_cdunieco_i" , cdunieco);
			paramsIniciarEndoso.put("pv_cdramo_i"   , cdramo);
			paramsIniciarEndoso.put("pv_estado_i"   , estado);
			paramsIniciarEndoso.put("pv_nmpoliza_i" , nmpoliza);
			paramsIniciarEndoso.put("pv_fecha_i"    , fechaEndoso);
			paramsIniciarEndoso.put("pv_cdelemen_i" , cdelemen);
			paramsIniciarEndoso.put("pv_cdusuari_i" , cdusuari);
			paramsIniciarEndoso.put("pv_proceso_i"  , "END");
			paramsIniciarEndoso.put("pv_cdtipsup_i" , cdtipsup);
			Map<String,String>respuestaIniciarEndoso=endososManager.iniciarEndoso(paramsIniciarEndoso);
			
			String nmsuplem=respuestaIniciarEndoso.get("pv_nmsuplem_o");
			String nsuplogi=respuestaIniciarEndoso.get("pv_nsuplogi_o");
			
			for(Map<String,String>persona:slist1) {
				Map<String,String>paramPersona=new HashMap<String,String>();
				paramPersona.put("pv_cdperson_i"    , persona.get("cdperson"));
				paramPersona.put("pv_dsnombre_i"    , persona.get("nombre"));
				paramPersona.put("pv_dsnombre1_i"   , persona.get("nombre2"));
				paramPersona.put("pv_dsapellido_i"  , persona.get("apat"));
				paramPersona.put("pv_dsapellido1_i" , persona.get("amat"));
				paramPersona.put("pv_cdrfc_i"       , persona.get("rfc"));
				
				/**
				 * Se acutalizan nombre y rfc aunque no sean contrantates, se actualizan por cdperson
				 */
				this.endososManager.actualizaNombreCliente(paramPersona);
				this.endososManager.actualizaRfcCliente(paramPersona);
			}
			
			// Se confirma el endoso si cumple la validacion de fechas: 
			RespuestaConfirmacionEndosoVO respConfirmacionEndoso = this.confirmarEndoso(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplem
					,nsuplogi
					,cdtipsup
					,""
					,dFechaEndoso
					,TipoSituacion.SALUD_VITAL.getCdtipsit()
					,flujo
					,cdusuari
					,cdsisrol
					);

			// Si el endoso fue confirmado:
			if(respConfirmacionEndoso.isConfirmado()) {
				endosoConfirmado = true;
				
				// Regeneramos los documentos:
				this.regeneraDocumentos(
						cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdtipsup, null,cdusuari);
				
				String saludDanios = "S";
				ClienteGeneral clienteGeneral = new ClienteGeneral();
				clienteGeneral.setClaveCia(saludDanios);
				
				// Ejecutamos el Web Service de Cliente Salud:
				ice2sigsService.ejecutaWSclienteGeneral(cdunieco, cdramo, estado, nmpoliza, nmsuplem, respConfirmacionEndoso.getNumeroTramite(), null, Ice2sigsService.Operacion.ACTUALIZA, clienteGeneral, (UserVO) session.get("USUARIO"), false);
				
				mensaje = "Endoso generado";
				if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
                }
				
            } else {
				mensaje = new StringBuilder().append("El endoso ").append(nsuplogi)
						.append(" se guard\u00f3 en mesa de control para autorizaci\u00f3n ")
						.append("con n\u00famero de tr\u00e1mite ").append(respConfirmacionEndoso.getNumeroTramite()).toString();
				if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
                }
			}
			
			success = true;

		} catch(Exception ex) {
			logger.error("Error al generar endoso de nombres simple",ex);
			success = false;
			error   = ex.getMessage();
		}
		
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### error   = " , error
				,"\n###### mensaje = " , mensaje
				,"\n###### guardarEndosoNombresSimple ######"
				,"\n########################################"
				));
		
		return SUCCESS;
	}
	/*////////////////////////////////////////////////*/
	////// generar el endoso de cambio de nombres //////
	////////////////////////////////////////////////////
	
	//////////////////////////////////////////
	////// guardar endosos de clausulas //////
	//////                              //////
	////// smap1.pv_cdunieco_i          //////
    ////// smap1.pv_cdramo_i            //////
    ////// smap1.pv_estado_i            //////
    ////// smap1.pv_nmpoliza_i          //////
    ////// smap1.pv_nmsituac_i          //////
    ////// smap1.pv_cdclausu_i          //////
    ////// smap1.pv_nmsuplem_i          //////
    ////// smap1.pv_ntramite_i          //////
    ////// smap1.pv_cdtipsit_i          //////
    ////// smap1.pv_status_i            //////
    ////// smap1.pv_cdtipcla_i          //////
    ////// smap1.pv_swmodi_i            //////
    ////// smap1.pv_accion_i            //////
    ////// smap1.confirmar              //////
    ////// smap1.pv_dslinea_i           //////
    ////// smap1.fecha_endoso           //////
	/*//////////////////////////////////////*/
	public String guardarEndosoClausulaPaso() {
		
		logger.debug(Utils.log(
				 "\n#######################################"
				,"\n###### guardarEndosoClausulaPaso ######"
				,"\n###### smap1 = " , smap1
				,"\n###### flujo = " , flujo
				));
		
		try {
			UserVO usuario=(UserVO)session.get("USUARIO");
			
			String fechaEndoso  = smap1.get("fecha_endoso");
			Date   fechaEndosoD = renderFechas.parse(fechaEndoso);
			
			////////////////////////////
			////// iniciar endoso //////
			/*
			 * pv_cdunieco_i-
			 * pv_cdramo_i-
			 * pv_estado_i-
			 * pv_nmpoliza_i-
			 * pv_fecha_i
			 * pv_cdelemen_i
			 * pv_cdusuari_i
			 * pv_proceso_i
			 * pv_cdtipsup_i
			 */
			omap1=new LinkedHashMap<String,Object>(0);
			omap1.put("pv_cdunieco_i" , smap1.get("pv_cdunieco_i"));
			omap1.put("pv_cdramo_i"   , smap1.get("pv_cdramo_i"));
			omap1.put("pv_estado_i"   , smap1.get("pv_estado_i"));
			omap1.put("pv_nmpoliza_i" , smap1.get("pv_nmpoliza_i"));
			omap1.put("pv_fecha_i"    , fechaEndosoD);
			omap1.put("pv_cdelemen_i" , usuario.getEmpresa().getElementoId());
			omap1.put("pv_cdusuari_i" , usuario.getUser());
			omap1.put("pv_proceso_i"  , "END");
			omap1.put("pv_cdtipsup_i", TipoEndoso.CAMBIO_ENDOSOS_EXCLUSION_O_TEXTOS.getCdTipSup().toString());
			
			Map<String,String> resEnd=endososManager.guardarEndosoClausulas(omap1);
			////// iniciar endoso //////
			////////////////////////////
			
			if(!(smap1.get("confirmar")!=null&&smap1.get("confirmar").equalsIgnoreCase("si"))) {
				
				endososManager.guardarMpolicot(smap1.get("pv_cdunieco_i"), smap1.get("pv_cdramo_i"), smap1.get("pv_estado_i"), smap1.get("pv_nmpoliza_i"), 
						smap1.get("pv_nmsituac_i"), smap1.get("pv_cdclausu_i"), resEnd.get("pv_nmsuplem_o"), smap1.get("pv_status_i"), smap1.get("pv_cdtipcla_i"), 
						smap1.get("pv_swmodi_i"), smap1.get("pv_dslinea_i"), smap1.get("pv_accion_i"));
			
			} else {
				// Se confirma el endoso si cumple la validacion de fechas: 
				RespuestaConfirmacionEndosoVO respConfirmacionEndoso = this.confirmarEndoso(
						smap1.get("pv_cdunieco_i"), 
						smap1.get("pv_cdramo_i"), 
						smap1.get("pv_estado_i"), 
						smap1.get("pv_nmpoliza_i"), 
						resEnd.get("pv_nmsuplem_o"), 
						resEnd.get("pv_nsuplogi_o"), 
						TipoEndoso.CAMBIO_ENDOSOS_EXCLUSION_O_TEXTOS.getCdTipSup().toString(),
						"",
						fechaEndosoD,
						smap1.get("pv_cdtipsit_i")
						,flujo
						,usuario.getUser()
						,usuario.getRolActivo().getClave()
						);
			    
				// Si el endoso fue confirmado:
				if(respConfirmacionEndoso.isConfirmado()) {
					endosoConfirmado = true;

				    ///////////////////////////////////////
				    ////// re generar los documentos //////
				    /*///////////////////////////////////*/
					documentosManager.generarDocumentosParametrizados(
							smap1.get("pv_cdunieco_i")
							,smap1.get("pv_cdramo_i")
							,smap1.get("pv_estado_i")
							,smap1.get("pv_nmpoliza_i")
							,"0" //nmsituac
							,resEnd.get("pv_nmsuplem_o")
							,DocumentosManager.PROCESO_ENDOSO
							,null //ntramite
							,null //nmsolici
, null
							);
					
					/*
				    List<Map<String,String>>listaDocu=endososManager.reimprimeDocumentos(
				    		smap1.get("pv_cdunieco_i")
				    		,smap1.get("pv_cdramo_i")
				    		,smap1.get("pv_estado_i")
				    		,smap1.get("pv_nmpoliza_i")
				    		,resEnd.get("pv_nmsuplem_o")
				    		,TipoEndoso.CAMBIO_ENDOSOS_EXCLUSION_O_TEXTOS.getCdTipSup().toString()
				    		);
				    logger.debug("documentos que se regeneran: "+listaDocu);
				    
				    String rutaCarpeta=this.rutaDocumentosPoliza+"/"+listaDocu.get(0).get("ntramite");
				    
					//listaDocu contiene: nmsolici,nmsituac,descripc,descripl
					for(Map<String,String> docu:listaDocu) {
						logger.debug("docu iterado: "+docu);
						String nmsolici=docu.get("nmsolici");
						String nmsituac=docu.get("nmsituac");
						String descripc=docu.get("descripc");
						String descripl=docu.get("descripl");
						String url=this.rutaServidorReports
								+ "?destype=cache"
								+ "&desformat=PDF"
								+ "&userid="+this.passServidorReports
								+ "&report="+descripl
								+ "&paramform=no"
								+ "&ACCESSIBLE=YES" //parametro que habilita salida en PDF
								+ "&p_unieco="+smap1.get("pv_cdunieco_i")
								+ "&p_ramo="+smap1.get("pv_cdramo_i")
								+ "&p_estado="+smap1.get("pv_estado_i")
								+ "&p_poliza="+smap1.get("pv_nmpoliza_i")
								+ "&p_suplem="+resEnd.get("pv_nmsuplem_o")
								+ "&desname="+rutaCarpeta+"/"+descripc;
						if(descripc.substring(0, 6).equalsIgnoreCase("CREDEN")) {
							// C R E D E N C I A L _ X X X X X X . P D F
							//0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
							url+="&p_cdperson="+descripc.substring(11, descripc.lastIndexOf("_"));
						}
						logger.debug(""
								+ "\n#################################"
								+ "\n###### Se solicita reporte ######"
								+ "\na "+url+""
								+ "\n#################################");
						HttpUtil.generaArchivo(url,rutaCarpeta+"/"+descripc);
						logger.debug(""
								+ "\n######                    ######"
								+ "\n###### reporte solicitado ######"
								+ "\na "+url+""
								+ "\n################################"
								+ "\n################################"
								+ "");
					}
					*/
				    /*///////////////////////////////////*/
					////// re generar los documentos //////
				    ///////////////////////////////////////
					
					////// confirmar endoso //////
					//////////////////////////////
					
					mensaje="Se ha confirmado el endoso "+resEnd.get("pv_nsuplogi_o");
					if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
	                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
	                }
				} else {
					mensaje="El endoso "+resEnd.get("pv_nsuplogi_o")
							+" se guard\u00f3 en mesa de control para autorizaci\u00f3n "
							+ "con n\u00famero de tr\u00e1mite " + respConfirmacionEndoso.getNumeroTramite();
					if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
	                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
	                }
				}
			}
			success=true;
		} catch(Exception ex) {
			logger.error("error al guardar endoso de clausula paso",ex);
			success=false;
			error=ex.getMessage();
		}
		
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### error   = " , error
				,"\n###### mensaje = " , mensaje
				,"\n###### guardarEndosoClausulaPaso ######"
				,"\n#######################################"
				));
		
		return SUCCESS;
	}
	/*//////////////////////////////////////*/
	////// guardar endosos de clausulas //////
	//////////////////////////////////////////
	
	//////////////////////////////////////////////////////
	////// generar el endoso de cambio de domicilio //////
	//////                                          //////
    ////// smap1:                                   //////
	////// pv_cdunieco                              //////
	////// pv_cdramo                                //////
    ////// pv_estado                                //////
	////// pv_nmpoliza                              //////
    ////// pv_nmsituac                              //////
	////// pv_cdrol                                 //////
	////// pv_cdperson                              //////
    ////// CODPOSTAL                                //////
    ////// NMORDDOM                                 //////
    ////// NMNUMINT                                 //////
    ////// asegurado                                //////
    ////// Municipio                                //////
    ////// NMNUMERO                                 //////
    ////// rfc                                      //////
    ////// NMTELEFO                                 //////
    ////// CDMUNICI                                 //////
    ////// estado                                   //////
    ////// CDEDO                                    //////
    ////// CDCOLONI                                 //////
    ////// DSDOMICI                                 //////
    //////                                          //////
    ////// smap2:                                   //////
    ////// pv_fecha_i                               //////
    ////// cdtipsit                                 //////
    //////                                          //////
	/*//////////////////////////////////////////////////*/
	public String guardarEndosoDomicilio() {
		
		logger.debug(Utils.log(
				 "\n####################################"
				,"\n###### guardarEndosoDomicilio ######"
				,"\n###### smap1      = " , smap1
				,"\n###### smap2      = " , smap2
				,"\n###### parametros = " , parametros
				,"\n###### flujo      = " , flujo
				));
		
		try {
			
			UserVO usuario=(UserVO) session.get("USUARIO");
			
			String usuarioCaptura =  null;
			
			if(usuario!=null){
				if(StringUtils.isNotBlank(usuario.getClaveUsuarioCaptura())){
					usuarioCaptura = usuario.getClaveUsuarioCaptura();
				}else{
					usuarioCaptura = usuario.getCodigoPersona();
				}
				
			}
			
			/**
			 * Validar que el Codigo Postal pertenezca al Estado correcto
			 */
			if (smap1 != null && smap1.size() > 0 && smap1 != null && smap1.size() > 0
						&& smap2.containsKey("cdtipsit")
						&& TipoSituacion.MULTISALUD.getCdtipsit().equalsIgnoreCase(smap2.get("cdtipsit"))) {
				
				HashMap<String,String> params =  new HashMap<String, String>();
				params.put("pv_estado_i", smap1.get("CDEDO"));
				params.put("pv_codpos_i", smap1.get("CODPOSTAL"));
				endososManager.validaEstadoCodigoPostal(params);
			}
			
			/*
			 * pv_cdunieco_i
			 * pv_cdramo_i
			 * pv_estado_i
			 * pv_nmpoliza_i
			 * pv_fecha_i
			 * pv_cdelemen_i
			 * pv_cdusuari_i
			 * pv_proceso_i
			 * pv_cdtipsup_i
			 */
			Map<String,Object> mapGuaEnd=new LinkedHashMap<String,Object>(0);
			mapGuaEnd.put("pv_cdunieco_i" , smap1.get("pv_cdunieco"));
			mapGuaEnd.put("pv_cdramo_i"   , smap1.get("pv_cdramo"));
			mapGuaEnd.put("pv_estado_i"   , smap1.get("pv_estado"));
			mapGuaEnd.put("pv_nmpoliza_i" , smap1.get("pv_nmpoliza"));
			mapGuaEnd.put("pv_fecha_i"    , renderFechas.parse((String)smap2.get("pv_fecha_i")));
			mapGuaEnd.put("pv_cdelemen_i" , usuario.getEmpresa().getElementoId());
			mapGuaEnd.put("pv_cdusuari_i" , usuario.getUser());
			mapGuaEnd.put("pv_proceso_i"  , "END");
			mapGuaEnd.put("pv_cdtipsup_i", TipoEndoso.CAMBIO_DOMICILIO.getCdTipSup().toString());
			Map<String,String> resEndDomi=endososManager.guardarEndosoNombres(mapGuaEnd);
			
			/*
			pv_cdunieco    smap1  ready!
			pv_cdramo      smap1  ready!
			pv_estado      smap1  ready!
			pv_nmpoliza    smap1  ready!
			pv_nmsituac    smap1  ready!
			pv_nmsuplem    #0
			pv_status      #V
			pv_cdrol       smap1  ready!
			pv_cdperson    smap1  ready!
			pv_cdatribu    #null
			pv_cdtipsit    session
			pv_otvalor01   parametros ready!
			...
			*/
			if(parametros!=null&&parametros.size()>0) {
				parametros.putAll(smap1);
				parametros.put("pv_nmsuplem" , resEndDomi.get("pv_nmsuplem_o"));
				parametros.put("pv_status"   , "V");
				parametros.put("pv_cdatribu" , null);
				parametros.put("pv_cdtipsit" , smap2.get("cdtipsit"));
				kernelManager.pMovTvaloper(parametros);
			}
			
			
			/////////////////////////////////////////
			////// guardar persona datos fijos //////
			/*/////////////////////////////////////*/
			/*
			pv_cdperson_i smap1.pv_cdperson
			pv_nmorddom_i smap1.NMORDDOM
			pv_msdomici_i smap1.DSDOMICI
			pv_nmtelefo_i smap1.NMTELEFO
			pv_cdpostal_i smap1.CODPOSTAL
			pv_cdedo_i    smap1.CDEDO
			pv_cdmunici_i smap1.CDMUNICI
			pv_cdcoloni_i smap1.CDCOLONI
			pv_nmnumero_i smap1.NMNUMERO
			pv_nmnumint_i smap1.NMNUMINT
			pv_accion_i   #U
			pv_msg_id_o   -
			pv_title_o    -
			*/
			Map<String,String>paramDomicil=new LinkedHashMap<String,String>(0);
			paramDomicil.put("pv_cdperson_i" , smap1.get("pv_cdperson"));
			paramDomicil.put("pv_nmorddom_i" , smap1.get("NMORDDOM"));
			paramDomicil.put("pv_msdomici_i" , smap1.get("DSDOMICI"));
			paramDomicil.put("pv_nmtelefo_i" , smap1.get("NMTELEFO"));
			paramDomicil.put("pv_cdpostal_i" , smap1.get("CODPOSTAL"));
        	paramDomicil.put("pv_cdedo_i"    , smap1.get("CDEDO"));
			paramDomicil.put("pv_cdmunici_i" , smap1.get("CDMUNICI"));
			paramDomicil.put("pv_cdcoloni_i" , smap1.get("CDCOLONI"));
			paramDomicil.put("pv_nmnumero_i" , smap1.get("NMNUMERO"));
			paramDomicil.put("pv_nmnumint_i" , smap1.get("NMNUMINT"));
			paramDomicil.put("pv_cdtipdom_i" , smap1.get("CDTIPDOM"));
			paramDomicil.put("pv_cdusuario_i", usuarioCaptura);
			paramDomicil.put("pv_swactivo_i",  Constantes.SI);
			paramDomicil.put("pv_accion_i"   , "U");			
			kernelManager.pMovMdomicil(paramDomicil);
			/*/////////////////////////////////////*/
			////// guardar persona datos fijos //////
			/////////////////////////////////////////
			
			// Se confirma el endoso si cumple la validacion de fechas: 
			RespuestaConfirmacionEndosoVO respConfirmacionEndoso = this.confirmarEndoso(
					(String)mapGuaEnd.get("pv_cdunieco_i"),
					(String)mapGuaEnd.get("pv_cdramo_i"),
					(String)mapGuaEnd.get("pv_estado_i"),
					(String)mapGuaEnd.get("pv_nmpoliza_i"),
					resEndDomi.get("pv_nmsuplem_o"),
					resEndDomi.get("pv_nsuplogi_o"),
					TipoEndoso.CAMBIO_DOMICILIO.getCdTipSup().toString(),
					"",
					renderFechas.parse((String)smap2.get("pv_fecha_i")),
					smap2.get("cdtipsit")
					,flujo
					,usuario.getUser()
					,usuario.getRolActivo().getClave()
					);
		    
			// Si el endoso fue confirmado:
			if(respConfirmacionEndoso.isConfirmado()) {
				endosoConfirmado = true;
				
			    ///////////////////////////////////////
			    ////// re generar los documentos //////
			    /*///////////////////////////////////*/
				documentosManager.generarDocumentosParametrizados(
						smap1.get("pv_cdunieco")
						,smap1.get("pv_cdramo")
						,smap1.get("pv_estado")
						,smap1.get("pv_nmpoliza")
						,"0" //nmsituac
						,resEndDomi.get("pv_nmsuplem_o")
						,DocumentosManager.PROCESO_ENDOSO
						,null //ntramite
						,null //nmsolici
, null
						);
				
				/*
			    List<Map<String,String>>listaDocu=endososManager.reimprimeDocumentos(
			    		smap1.get("pv_cdunieco")
			    		,smap1.get("pv_cdramo")
			    		,smap1.get("pv_estado")
			    		,smap1.get("pv_nmpoliza")
			    		,resEndDomi.get("pv_nmsuplem_o")
			    		,TipoEndoso.CAMBIO_DOMICILIO.getCdTipSup().toString()
			    		);
			    logger.debug("documentos que se regeneran: "+listaDocu);
			    
			    String rutaCarpeta=this.rutaDocumentosPoliza+"/"+listaDocu.get(0).get("ntramite");
			    
				//listaDocu contiene: nmsolici,nmsituac,descripc,descripl
				for(Map<String,String> docu:listaDocu) {
					logger.debug("docu iterado: "+docu);
					String nmsolici=docu.get("nmsolici");
					String nmsituac=docu.get("nmsituac");
					String descripc=docu.get("descripc");
					String descripl=docu.get("descripl");
					String url=this.rutaServidorReports
							+ "?destype=cache"
							+ "&desformat=PDF"
							+ "&userid="+this.passServidorReports
							+ "&report="+descripl
							+ "&paramform=no"
							+ "&ACCESSIBLE=YES" //parametro que habilita salida en PDF
							+ "&p_unieco="+smap1.get("pv_cdunieco")
							+ "&p_ramo="+smap1.get("pv_cdramo")
							+ "&p_estado="+smap1.get("pv_estado")
							+ "&p_poliza="+smap1.get("pv_nmpoliza")
							+ "&p_suplem="+resEndDomi.get("pv_nmsuplem_o")
							+ "&desname="+rutaCarpeta+"/"+descripc;
					if(descripc.substring(0, 6).equalsIgnoreCase("CREDEN")) {
						// C R E D E N C I A L _ X X X X X X . P D F
						//0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
						url+="&p_cdperson="+descripc.substring(11, descripc.lastIndexOf("_"));
					}
					logger.debug(""
							+ "\n#################################"
							+ "\n###### Se solicita reporte ######"
							+ "\na "+url+""
							+ "\n#################################");
					HttpUtil.generaArchivo(url,rutaCarpeta+"/"+descripc);
					logger.debug(""
							+ "\n######                    ######"
							+ "\n###### reporte solicitado ######"
							+ "\na "+url+""
							+ "\n################################"
							+ "\n################################"
							+ "");
				}
				*/
			    /*///////////////////////////////////*/
				////// re generar los documentos //////
			    ///////////////////////////////////////
				
				// Ejecutamos el Web Service de Cliente Salud:
//				ice2sigsService.ejecutaWSclienteSalud(
//						smap1.get("pv_cdunieco"), 
//						smap1.get("pv_cdramo"), 
//						smap1.get("pv_estado"), 
//						smap1.get("pv_nmpoliza"), 
//						resEndDomi.get("pv_nmsuplem_o"), 
//						respConfirmacionEndoso.getNumeroTramite(),
//						Ice2sigsService.Operacion.ACTUALIZA, 
//						(UserVO) session.get("USUARIO"));
				
				String saludDanios = "S";
				
				ClienteGeneral clienteGeneral = new ClienteGeneral();
				clienteGeneral.setClaveCia(saludDanios);
				
				// Ejecutamos el Web Service de Cliente Salud:
				ice2sigsService.ejecutaWSclienteGeneral(null, null, null, null, null, null, smap1.get("pv_cdperson"), Ice2sigsService.Operacion.ACTUALIZA, clienteGeneral, (UserVO) session.get("USUARIO"), false);
    			
				ArrayList<Map<String,String>> saveList = new ArrayList<Map<String,String>>();
				ArrayList<Map<String,String>> updateList = new ArrayList<Map<String,String>>();

    			Map<String,String> domicilioEndoso = new HashMap<String,String>();
    			
    			
    			/**
    			 * SE FIJAN VALORES AUNQUE EL UNICO QUE SE TOMA ES EL NMORDDOM
    			 */
    			domicilioEndoso.put("NMORDDOM" ,smap1.get("NMORDDOM"));
    			domicilioEndoso.put("CODPOSTAL",smap1.get("CODPOSTAL"));
    			domicilioEndoso.put("CDEDO"    ,smap1.get("CDEDO"));
    			domicilioEndoso.put("CDMUNICI" ,smap1.get("CDMUNICI"));
    			domicilioEndoso.put("CDCOLONI" ,smap1.get("CDCOLONI"));
    			domicilioEndoso.put("DSDOMICI" ,smap1.get("DSDOMICI"));
    			domicilioEndoso.put("NMNUMERO" ,smap1.get("NMNUMERO"));
    			domicilioEndoso.put("NMNUMINT" ,smap1.get("NMNUMINT"));
    			
    			updateList.add(domicilioEndoso);
    			
    			ice2sigsService.ejecutaWSdireccionClienteGeneral(smap1.get("pv_cdperson"), saludDanios, saveList, updateList, false, usuario);
								
			    mensaje="Se ha guardado el endoso "+resEndDomi.get("pv_nsuplogi_o");
			    if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
                }
			} else {
				mensaje="El endoso "+resEndDomi.get("pv_nsuplogi_o")
						+" se guard\u00f3 en mesa de control para autorizaci\u00f3n "
						+ "con n\u00famero de tr\u00e1mite " + respConfirmacionEndoso.getNumeroTramite();
				if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
                }
			}
			success=true;
			
		} catch(Exception ex) {
			logger.error("error al guardar los datos de endoso de domicilio",ex);
			success = false;
			error = ex.getMessage();
		}
		
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### error   = " , error
				,"\n###### mensaje = " , mensaje
				,"\n###### guardarEndosoDomicilio ######"
				,"\n####################################"
				));
		
		return SUCCESS;
	}
	/*//////////////////////////////////////////////////*/
	////// generar el endoso de cambio de domicilio //////
	//////////////////////////////////////////////////////

	//////////////////////////////////////////////////////
	////// generar el endoso de cambio de domicilio auto //////
	//////                                          //////
	////// smap1:                                   //////
	////// pv_cdunieco                              //////
	////// pv_cdramo                                //////
	////// pv_estado                                //////
	////// pv_nmpoliza                              //////
	////// pv_nmsituac                              //////
	////// pv_cdrol                                 //////
	////// pv_cdperson                              //////
	////// CODPOSTAL                                //////
	////// NMORDDOM                                 //////
	////// NMNUMINT                                 //////
	////// asegurado                                //////
	////// Municipio                                //////
	////// NMNUMERO                                 //////
	////// rfc                                      //////
	////// NMTELEFO                                 //////
	////// CDMUNICI                                 //////
	////// estado                                   //////
	////// CDEDO                                    //////
	////// CDCOLONI                                 //////
	////// DSDOMICI                                 //////
	//////                                          //////
	////// smap2:                                   //////
	////// pv_fecha_i                               //////
	////// cdtipsit                                 //////
	//////                                          //////
	/*//////////////////////////////////////////////////*/
	public String guardarEndosoDomicilioAuto()
	{
		logger.debug(Utils.log(
				 "\n########################################"
				,"\n###### guardarEndosoDomicilioAuto ######"
				,"\n###### smap1      = " , smap1
				,"\n###### smap2      = " , smap2
				,"\n###### smap3      = " , smap3
				,"\n###### parametros = " , parametros
				,"\n###### flujo      = " , flujo
				));
		try
		{			
			UserVO usuario = Utils.validateSession(session);
			
			String usuarioCaptura =  null;
			
			if(usuario!=null){
				if(StringUtils.isNotBlank(usuario.getClaveUsuarioCaptura())){
					usuarioCaptura = usuario.getClaveUsuarioCaptura();
				}else{
					usuarioCaptura = usuario.getCodigoPersona();
				}
				
			}
			
			String paso = null;
			
			try // Esto deberia ir en un manager
			{
				paso = "Validando c\u00f3digo postal";
				logger.debug(paso);
				
				/**
				 * Validar que el Codigo Postal pertenezca al Estado correcto
				 */
				if (smap1 != null && smap1.size() > 0 && smap1 != null && smap1.size() > 0
						&& smap2.containsKey("cdtipsit")
						&& TipoSituacion.MULTISALUD.getCdtipsit().equalsIgnoreCase(smap2.get("cdtipsit"))) {
					
					HashMap<String,String> params =  new HashMap<String, String>();
					params.put("pv_estado_i", smap1.get("CDEDO"));
					params.put("pv_codpos_i", smap1.get("CODPOSTAL"));
					endososManager.validaEstadoCodigoPostal(params);
				}
				
				paso = "Guardando endoso";
				logger.debug(paso);
				
				/*
				 * pv_cdunieco_i
				 * pv_cdramo_i
				 * pv_estado_i
				 * pv_nmpoliza_i
				 * pv_fecha_i
				 * pv_cdelemen_i
				 * pv_cdusuari_i
				 * pv_proceso_i
				 * pv_cdtipsup_i
				 */
				Map<String,Object> mapGuaEnd=new LinkedHashMap<String,Object>(0);
				mapGuaEnd.put("pv_cdunieco_i" , smap1.get("pv_cdunieco"));
				mapGuaEnd.put("pv_cdramo_i"   , smap1.get("pv_cdramo"));
				mapGuaEnd.put("pv_estado_i"   , smap1.get("pv_estado"));
				mapGuaEnd.put("pv_nmpoliza_i" , smap1.get("pv_nmpoliza"));
				mapGuaEnd.put("pv_fecha_i"    , renderFechas.parse((String)smap2.get("pv_fecha_i")));
				mapGuaEnd.put("pv_cdelemen_i" , usuario.getEmpresa().getElementoId());
				mapGuaEnd.put("pv_cdusuari_i" , usuario.getUser());
				mapGuaEnd.put("pv_proceso_i"  , "END");
				mapGuaEnd.put("pv_cdtipsup_i", TipoEndoso.CAMBIO_DOMICILIO.getCdTipSup().toString());
				Map<String,String> resEndDomi=endososManager.guardarEndosoNombres(mapGuaEnd);
				
				paso = "Guardando atributos de persona";
				logger.debug(paso);
				
				/*
				pv_cdunieco    smap1  ready!
				pv_cdramo      smap1  ready!
				pv_estado      smap1  ready!
				pv_nmpoliza    smap1  ready!
				pv_nmsituac    smap1  ready!
				pv_nmsuplem    #0
				pv_status      #V
				pv_cdrol       smap1  ready!
				pv_cdperson    smap1  ready!
				pv_cdatribu    #null
				pv_cdtipsit    session
				pv_otvalor01   parametros ready!
				...
				 */
				if(parametros!=null&&parametros.size()>0) {
					parametros.putAll(smap1);
					parametros.put("pv_nmsuplem" , resEndDomi.get("pv_nmsuplem_o"));
					parametros.put("pv_status"   , "V");
					parametros.put("pv_cdatribu" , null);
					parametros.put("pv_cdtipsit" , smap2.get("cdtipsit"));
					kernelManager.pMovTvaloper(parametros);
				}
				
				/////////////////////////////////////////
				////// guardar persona datos fijos //////
				/*/////////////////////////////////////*/
				
				paso = "Guardando domicilio";
				logger.debug(paso);
				
				/*
				pv_cdperson_i smap1.pv_cdperson
				pv_nmorddom_i smap1.NMORDDOM
				pv_msdomici_i smap1.DSDOMICI
				pv_nmtelefo_i smap1.NMTELEFO
				pv_cdpostal_i smap1.CODPOSTAL
				pv_cdedo_i    smap1.CDEDO
				pv_cdmunici_i smap1.CDMUNICI
				pv_cdcoloni_i smap1.CDCOLONI
				pv_nmnumero_i smap1.NMNUMERO
				pv_nmnumint_i smap1.NMNUMINT
				pv_accion_i   #U
				pv_msg_id_o   -
				pv_title_o    -
				 */
				Map<String,String>paramDomicil=new LinkedHashMap<String,String>(0);
				paramDomicil.put("pv_cdperson_i" , smap1.get("pv_cdperson"));
				paramDomicil.put("pv_nmorddom_i" , smap1.get("NMORDDOM"));
				paramDomicil.put("pv_msdomici_i" , smap1.get("DSDOMICI"));
				paramDomicil.put("pv_nmtelefo_i" , smap1.get("NMTELEFO"));
				paramDomicil.put("pv_cdpostal_i" , smap1.get("CODPOSTAL"));
				paramDomicil.put("pv_cdedo_i"    , smap1.get("CDEDO"));
				paramDomicil.put("pv_cdmunici_i" , smap1.get("CDMUNICI"));
				paramDomicil.put("pv_cdcoloni_i" , smap1.get("CDCOLONI"));
				paramDomicil.put("pv_nmnumero_i" , smap1.get("NMNUMERO"));
				paramDomicil.put("pv_nmnumint_i" , smap1.get("NMNUMINT"));
				paramDomicil.put("pv_cdtipdom_i" , smap1.get("CDTIPDOM"));
				paramDomicil.put("pv_cdusuario_i", usuarioCaptura);
				paramDomicil.put("pv_swactivo_i",  Constantes.SI);
				paramDomicil.put("pv_accion_i"   , "U");			
				kernelManager.pMovMdomicil(paramDomicil);
				/*/////////////////////////////////////*/
				////// guardar persona datos fijos //////
				/////////////////////////////////////////
				
				paso = "Confirmando endoso";
				logger.debug(paso);
				
				// Se confirma el endoso si cumple la validacion de fechas: 
				RespuestaConfirmacionEndosoVO respConfirmacionEndoso = this.confirmarEndoso(
						(String)mapGuaEnd.get("pv_cdunieco_i"),
						(String)mapGuaEnd.get("pv_cdramo_i"),
						(String)mapGuaEnd.get("pv_estado_i"),
						(String)mapGuaEnd.get("pv_nmpoliza_i"),
						resEndDomi.get("pv_nmsuplem_o"),
						resEndDomi.get("pv_nsuplogi_o"),
						TipoEndoso.CAMBIO_DOMICILIO.getCdTipSup().toString(),
						"",
						renderFechas.parse((String)smap2.get("pv_fecha_i")),
						smap2.get("cdtipsit")
						,flujo
						,usuario.getUser()
						,usuario.getRolActivo().getClave()
						);
				
				// Si el endoso fue confirmado:
				if(respConfirmacionEndoso.isConfirmado()) {
					endosoConfirmado = true;
					
					paso = "Generando documentos";
					logger.debug(paso);
					
					///////////////////////////////////////
					////// re generar los documentos //////
					/*///////////////////////////////////*/
					documentosManager.generarDocumentosParametrizados(
							smap1.get("pv_cdunieco")
							,smap1.get("pv_cdramo")
							,smap1.get("pv_estado")
							,smap1.get("pv_nmpoliza")
							,"0" //nmsituac
							,resEndDomi.get("pv_nmsuplem_o")
							,DocumentosManager.PROCESO_ENDOSO
							,null //ntramite
							,null //nmsolici
	, null
							);
					
					/*
					List<Map<String,String>>listaDocu=endososManager.reimprimeDocumentos(
							smap1.get("pv_cdunieco")
							,smap1.get("pv_cdramo")
							,smap1.get("pv_estado")
							,smap1.get("pv_nmpoliza")
							,resEndDomi.get("pv_nmsuplem_o")
							,TipoEndoso.CAMBIO_DOMICILIO.getCdTipSup().toString()
							);
					logger.debug("documentos que se regeneran: "+listaDocu);
					
					//listaDocu contiene: nmsolici,nmsituac,descripc,descripl
					for(Map<String,String> docu:listaDocu) {
						
						String rutaCarpeta=this.rutaDocumentosPoliza+"/"+listaDocu.get(0).get("ntramite");
						
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
								+ "&p_unieco="+smap1.get("pv_cdunieco")
								+ "&p_ramo="+smap1.get("pv_cdramo")
								+ "&p_estado="+smap1.get("pv_estado")
								+ "&p_poliza="+smap1.get("pv_nmpoliza")
								+ "&p_suplem="+resEndDomi.get("pv_nmsuplem_o")
								+ "&desname="+rutaCarpeta+"/"+descripc;
						if(descripc.substring(0, 6).equalsIgnoreCase("CREDEN")) {
							// C R E D E N C I A L _ X X X X X X . P D F
							//0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
							url+="&p_cdperson="+descripc.substring(11, descripc.lastIndexOf("_"));
						}
						logger.debug(""
								+ "\n#################################"
								+ "\n###### Se solicita reporte ######"
								+ "\na "+url+""
								+ "\n#################################");
						HttpUtil.generaArchivo(url,rutaCarpeta+"/"+descripc);
						logger.debug(""
								+ "\n######                    ######"
								+ "\n###### reporte solicitado ######"
								+ "\na "+url+""
								+ "\n################################"
								+ "\n################################"
								+ "");
					}
					*/
					
					
					
					/*///////////////////////////////////*/
					////// re generar los documentos //////
					///////////////////////////////////////
	
					paso = "Ejecutando servicios web";
					logger.debug(paso);
					
					/**
					 * PARA WS ENDOSO DE AUTOS
					 */
					int numEndRes = emisionAutosService.endosoCambioDomicil(smap1.get("pv_cdunieco"), smap1.get("pv_cdramo"), smap1.get("pv_estado"), smap1.get("pv_nmpoliza"), resEndDomi.get("pv_nmsuplem_o"), usuario);
					
					if(numEndRes == 0){
						mensaje = "Error al generar el endoso, sigs. Consulte a Soporte.";
						error = "Error al generar el endoso, sigs. Consulte a Soporte.";
						logger.error("Error al ejecutar sp de endoso sigs");
						
						boolean endosoRevertido = endososManager.revierteEndosoFallido(smap1.get("pv_cdunieco"), smap1.get("pv_cdramo"), smap1.get("pv_estado"), smap1.get("pv_nmpoliza"), resEndDomi.get("pv_nsuplogi_o"), resEndDomi.get("pv_nmsuplem_o"), 88888, "Error en endoso B tipo: "+TipoEndoso.CAMBIO_DOMICILIO.toString(), true);
								
						if(endosoRevertido){
							
							Map<String,String> paramRevDom = new HashMap<String, String>();
							paramRevDom.put("pv_cdperson_i" , smap3.get("cdperson"));
							paramRevDom.put("pv_dsdomici_i" , smap3.get("calle"));
							paramRevDom.put("pv_cdpostal_i" , smap3.get("cp"));
							paramRevDom.put("pv_nmnumero_i" , smap3.get("numext"));
							paramRevDom.put("pv_nmnumint_i" , smap3.get("numint"));
							paramRevDom.put("pv_cdedo_i"    , smap3.get("cdedo"));
							paramRevDom.put("pv_cdmunici_i" , smap3.get("cdmunici"));
							paramRevDom.put("pv_cdcoloni_i" , smap3.get("cdcoloni"));
							endososManager.revierteDomicilio(paramRevDom);
							
							logger.error("Endoso revertido exitosamente.");
							error+=" Favor de volver a intentar.";
						}else{
							logger.error("Error al revertir el endoso");
							error+=" No se ha revertido el endoso.";
						}
						
						success = false;
						return SUCCESS;
					}else{
						ejecutaCaratulaEndosoBsigs(smap1.get("pv_cdunieco"),smap1.get("pv_cdramo"),smap1.get("pv_estado"),smap1.get("pv_nmpoliza"),resEndDomi.get("pv_nmsuplem_o"), smap1.get("NTRAMITE"), TipoEndoso.CAMBIO_DOMICILIO.getCdTipSup().toString(), Integer.toString(numEndRes));
					}
					
					mensaje="Se ha guardado el endoso "+resEndDomi.get("pv_nsuplogi_o");
					if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
	                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
	                }
				} else {
					mensaje="El endoso "+resEndDomi.get("pv_nsuplogi_o")
							+" se guard\u00f3 en mesa de control para autorizaci\u00f3n "
							+ "con n\u00famero de tr\u00e1mite " + respConfirmacionEndoso.getNumeroTramite();
					if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
	                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
	                }
				}
				success=true;
			}
			catch(Exception ex)
			{
				Utils.generaExcepcion(ex, paso);
			}
			
		}
		catch(Exception ex)
		{
			error = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### mensaje = " , mensaje
				,"\n###### error   = " , error
				,"\n###### guardarEndosoDomicilioAuto ######"
				,"\n########################################"
				));
		
		return SUCCESS;
	}
	/*//////////////////////////////////////////////////*/
	////// generar el endoso de cambio de domicilio //////
	//////////////////////////////////////////////////////
	
	//////////////////////////////////////////////////////
	////// generar el endoso de cambio de domicilio //////
	//////                                          //////
    ////// smap1:                                   //////
	////// pv_cdunieco                              //////
	////// pv_cdramo                                //////
    ////// pv_estado                                //////
	////// pv_nmpoliza                              //////
    ////// pv_nmsituac                              //////
	////// pv_cdrol                                 //////
	////// pv_cdperson                              //////
    ////// CODPOSTAL                                //////
    ////// NMORDDOM                                 //////
    ////// NMNUMINT                                 //////
    ////// asegurado                                //////
    ////// Municipio                                //////
    ////// NMNUMERO                                 //////
    ////// rfc                                      //////
    ////// NMTELEFO                                 //////
    ////// CDMUNICI                                 //////
    ////// estado                                   //////
    ////// CDEDO                                    //////
    ////// CDCOLONI                                 //////
    ////// DSDOMICI                                 //////
    //////                                          //////
    ////// smap2:                                   //////
    ////// pv_fecha_i                               //////
    ////// cdtipsit                                 //////
    //////                                          //////
	/*//////////////////////////////////////////////////*/
	public String guardarEndosoDomicilioSimple() {
		
		logger.debug(Utils.log(
				 "\n##########################################"
				,"\n###### guardarEndosoDomicilioSimple ######"
				,"\n###### smap1      = " , smap1
				,"\n###### smap2      = " , smap2
				,"\n###### parametros = " , parametros
				,"\n###### flujo      = " , flujo
				));
		
		try {
			
			UserVO usuario=(UserVO) session.get("USUARIO");
			
			String usuarioCaptura =  null;
			
			if(usuario!=null){
				if(StringUtils.isNotBlank(usuario.getClaveUsuarioCaptura())){
					usuarioCaptura = usuario.getClaveUsuarioCaptura();
				}else{
					usuarioCaptura = usuario.getCodigoPersona();
				}
				
			}
			
			/**
			 * Validar que el Codigo Postal pertenezca al Estado correcto
			 */
			if (smap1 != null && smap1.size() > 0 && smap1 != null && smap1.size() > 0
						&& smap2.containsKey("cdtipsit")
						&& TipoSituacion.MULTISALUD.getCdtipsit().equalsIgnoreCase(smap2.get("cdtipsit"))) {
				HashMap<String,String> params =  new HashMap<String, String>();
				params.put("pv_estado_i", smap1.get("CDEDO"));
				params.put("pv_codpos_i", smap1.get("CODPOSTAL"));
				endososManager.validaEstadoCodigoPostal(params);
			}
			
			/*
			 * pv_cdunieco_i
			 * pv_cdramo_i
			 * pv_estado_i
			 * pv_nmpoliza_i
			 * pv_fecha_i
			 * pv_cdelemen_i
			 * pv_cdusuari_i
			 * pv_proceso_i
			 * pv_cdtipsup_i
			 */
			
			String cdunieco     = smap1.get("pv_cdunieco");
			String cdramo       = smap1.get("pv_cdramo");
			String cdtipsit     = smap2.get("cdtipsit");
			String estado       = smap1.get("pv_estado");
			String nmpoliza     = smap1.get("pv_nmpoliza");
			String fechaEndoso  = smap2.get("pv_fecha_i");
			Date   dFechaEndoso = renderFechas.parse(fechaEndoso);
			String cdelemento   = usuario.getEmpresa().getElementoId();
			String cdusuari     = usuario.getUser();
			String cdsisrol     = usuario.getRolActivo().getClave();
			String cdtipsup     = TipoEndoso.CORRECCION_DATOS_ASEGURADOS.getCdTipSup().toString();
			
			Map<String,String>paramsIniciarEndoso=new HashMap<String,String>(0);
			paramsIniciarEndoso.put("pv_cdunieco_i" , cdunieco);
			paramsIniciarEndoso.put("pv_cdramo_i"   , cdramo);
			paramsIniciarEndoso.put("pv_estado_i"   , estado);
			paramsIniciarEndoso.put("pv_nmpoliza_i" , nmpoliza);
			paramsIniciarEndoso.put("pv_fecha_i"    , fechaEndoso);
			paramsIniciarEndoso.put("pv_cdelemen_i" , cdelemento);
			paramsIniciarEndoso.put("pv_cdusuari_i" , cdusuari);
			paramsIniciarEndoso.put("pv_proceso_i"  , "END");
			paramsIniciarEndoso.put("pv_cdtipsup_i" , cdtipsup);
			Map<String,String>respuestaIniciarEndoso=endososManager.iniciarEndoso(paramsIniciarEndoso);
			
			String nmsuplem=respuestaIniciarEndoso.get("pv_nmsuplem_o");
			String nsuplogi=respuestaIniciarEndoso.get("pv_nsuplogi_o");
			
			/*
			pv_cdunieco    smap1  ready!
			pv_cdramo      smap1  ready!
			pv_estado      smap1  ready!
			pv_nmpoliza    smap1  ready!
			pv_nmsituac    smap1  ready!
			pv_nmsuplem    #0
			pv_status      #V
			pv_cdrol       smap1  ready!
			pv_cdperson    smap1  ready!
			pv_cdatribu    #null
			pv_cdtipsit    session
			pv_otvalor01   parametros ready!
			...
			*/
			if(parametros!=null&&parametros.size()>0) {
				parametros.putAll(smap1);
				parametros.put("pv_nmsuplem" , nmsuplem);
				parametros.put("pv_status"   , Constantes.STATUS_VIVO);
				parametros.put("pv_cdatribu" , null);
				parametros.put("pv_cdtipsit" , cdtipsit);
				kernelManager.pMovTvaloper(parametros);
			}
			
			/////////////////////////////////////////
			////// guardar persona datos fijos //////
			/*/////////////////////////////////////*/
			/*
			pv_cdperson_i smap1.pv_cdperson
			pv_nmorddom_i smap1.NMORDDOM
			pv_msdomici_i smap1.DSDOMICI
			pv_nmtelefo_i smap1.NMTELEFO
			pv_cdpostal_i smap1.CODPOSTAL
			pv_cdedo_i    smap1.CDEDO
			pv_cdmunici_i smap1.CDMUNICI
			pv_cdcoloni_i smap1.CDCOLONI
			pv_nmnumero_i smap1.NMNUMERO
			pv_nmnumint_i smap1.NMNUMINT
			pv_accion_i   #U
			pv_msg_id_o   -
			pv_title_o    -
			*/
			Map<String,String>paramDomicil=new LinkedHashMap<String,String>(0);
			paramDomicil.put("pv_cdperson_i" , smap1.get("pv_cdperson"));
			paramDomicil.put("pv_nmorddom_i" , smap1.get("NMORDDOM"));
			paramDomicil.put("pv_msdomici_i" , smap1.get("DSDOMICI"));
			paramDomicil.put("pv_nmtelefo_i" , smap1.get("NMTELEFO"));
			paramDomicil.put("pv_cdpostal_i" , smap1.get("CODPOSTAL"));
        	paramDomicil.put("pv_cdedo_i"    , smap1.get("CDEDO"));
			paramDomicil.put("pv_cdmunici_i" , smap1.get("CDMUNICI"));
			paramDomicil.put("pv_cdcoloni_i" , smap1.get("CDCOLONI"));
			paramDomicil.put("pv_nmnumero_i" , smap1.get("NMNUMERO"));
			paramDomicil.put("pv_nmnumint_i" , smap1.get("NMNUMINT"));
			paramDomicil.put("pv_cdtipdom_i" , smap1.get("CDTIPDOM"));
			paramDomicil.put("pv_cdusuario_i", usuarioCaptura);
			paramDomicil.put("pv_swactivo_i",  Constantes.SI);
			paramDomicil.put("pv_accion_i"   , Constantes.UPDATE_MODE);			
			kernelManager.pMovMdomicil(paramDomicil);
			/*/////////////////////////////////////*/
			////// guardar persona datos fijos //////
			/////////////////////////////////////////
			
			this.regeneraDocumentos(cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdtipsup, null,cdusuari);
			
			// Se confirma el endoso si cumple la validacion de fechas: 
			RespuestaConfirmacionEndosoVO respConfirmacionEndoso = this.confirmarEndoso(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplem
					,nsuplogi
					,cdtipsup
					,""
					,dFechaEndoso
					,cdtipsit
					,flujo
					,cdusuari
					,cdsisrol
					);
			
			// Si el endoso fue confirmado:
			if(respConfirmacionEndoso.isConfirmado()) {
				endosoConfirmado = true;
				
				// TODO: preguntarle a Alvaro si aqui no se generan documentos:
				
				String saludDanios = "S";
				ClienteGeneral clienteGeneral = new ClienteGeneral();
				clienteGeneral.setClaveCia(saludDanios);
				
				// Ejecutamos el Web Service de Cliente Salud:
				ice2sigsService.ejecutaWSclienteGeneral(cdunieco, cdramo, estado, nmpoliza, nmsuplem, respConfirmacionEndoso.getNumeroTramite(), null, Ice2sigsService.Operacion.ACTUALIZA, clienteGeneral, (UserVO) session.get("USUARIO"), false);
				
				ArrayList<Map<String,String>> saveList = new ArrayList<Map<String,String>>();
				ArrayList<Map<String,String>> updateList = new ArrayList<Map<String,String>>();

    			Map<String,String> domicilioEndoso = new HashMap<String,String>();
    			
    			
    			/**
    			 * SE FIJAN VALORES AUNQUE EL UNICO QUE SE TOMA ES EL NMORDDOM
    			 */
    			domicilioEndoso.put("NMORDDOM" ,smap1.get("NMORDDOM"));
    			domicilioEndoso.put("CODPOSTAL",smap1.get("CODPOSTAL"));
    			domicilioEndoso.put("CDEDO"    ,smap1.get("CDEDO"));
    			domicilioEndoso.put("CDMUNICI" ,smap1.get("CDMUNICI"));
    			domicilioEndoso.put("CDCOLONI" ,smap1.get("CDCOLONI"));
    			domicilioEndoso.put("DSDOMICI" ,smap1.get("DSDOMICI"));
    			domicilioEndoso.put("NMNUMERO" ,smap1.get("NMNUMERO"));
    			domicilioEndoso.put("NMNUMINT" ,smap1.get("NMNUMINT"));
    			
    			updateList.add(domicilioEndoso);
    			
    			ice2sigsService.ejecutaWSdireccionClienteGeneral(smap1.get("pv_cdperson"), saludDanios, saveList, updateList, false, usuario);
				
				mensaje = "Endoso generado";
				if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
                }
			} else {
				mensaje = new StringBuilder().append("El endoso ").append(nsuplogi)
						.append(" se guard\u00f3 en mesa de control para autorizaci\u00f3n ")
						.append("con n\u00famero de tr\u00e1mite ").append(respConfirmacionEndoso.getNumeroTramite()).toString();
				if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
                }
			}
			success=true;

		} catch(Exception ex) {
			logger.error("error al guardar los datos de endoso de domicilio simple", ex);
			success = false;
			error = ex.getMessage();
		}
		
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### error   = " , error
				,"\n###### mensaje = " , mensaje
				,"\n###### guardarEndosoDomicilioSimple ######"
				,"\n##########################################"
				));
		
		return SUCCESS;
	}
	/*//////////////////////////////////////////////////*/
	////// generar el endoso de cambio de domicilio //////
	//////////////////////////////////////////////////////
	
	////////////////////////////////////////////
	////// obtener coberturas disponibles //////
	/*////////////////////////////////////////*/
	public String obtenerCoberturasDisponibles()
	{
		logger.debug(""
				+ "\n##########################################"
				+ "\n##########################################"
				+ "\n###### obtenerCoberturasDisponibles ######"
				+ "\n######                              ######"
				);
		logger.debug("smap1: "+smap1);
		try
		{
			slist1=endososManager.obtieneCoberturasDisponibles(smap1);
		}
		catch(Exception ex)
		{
			logger.error("Error al cargar las coberturas disponibles", ex);
		}
		success=true;
		logger.debug(""
				+ "\n######                              ######"
				+ "\n###### obtenerCoberturasDisponibles ######"
				+ "\n##########################################"
				+ "\n##########################################"
				);
		return SUCCESS;
	}
	/*////////////////////////////////////////*/
	////// obtener coberturas disponibles //////
	////////////////////////////////////////////
	
	/////////////////////////////////////
	////// guardarEndosoCoberturas //////
	////// smap1:                  //////
	//////     nmsituac            //////
	//////     cdperson            //////
    //////     altabaja            //////
	//////     confirmar           //////
	////// omap1:                  //////
	//////     pv_cdunieco_i       //////
	//////     pv_cdramo_i         //////
	//////     pv_estado_i         //////
	//////     pv_nmpoliza_i       //////
	//////     pv_fecha_i          //////
	////// slist1: coberturas      //////
	//////         Editadas        //////
	////// [                       //////
	//////     garantia            //////
	//////     ,cdcapita           //////
	//////     ,status             //////
	//////     ,ptcapita,          //////
	//////     ,ptreduci           //////
	//////     ,fereduci           //////
	//////     ,swrevalo           //////
	//////     ,cdagrupa           //////
	//////     ,cdatribu           //////
	//////     ,otvalor            //////
	////// ]                       //////
	/*/////////////////////////////////*/
	public String guardarEndosoCoberturas()
	{
		logger.debug(Utils.log(
				 "\n#####################################"
				,"\n###### guardarEndosoCoberturas ######"
				,"\n###### smap1  = " , smap1
				,"\n###### omap1  = " , omap1
				,"\n###### slist1 = " , slist1
				,"\n###### flujo  = " , flujo
				));
		
		this.session = ActionContext.getContext().getSession();
		
		
		try //esto deberia estar en un manager
		{
		    
			UserVO usuario = Utils.validateSession(session);
			
			String paso = null;
			
			logger.debug(Utils.log(
	                 "\n#####################################"
	                ,"\n###### guardarBeneficiarios #########"
	                ,"\n###### listaBeneficiarios = " , session.get("listaBeneficiarios")
	                ));
			
			if(smap1.get("confirmar").equalsIgnoreCase("si"))
            {
                String usuarioCaptura =  null;
                paso = "Obteniendo Lista de Pantalla Beneficiarios";
                if(usuario!=null){
                    if(StringUtils.isNotBlank(usuario.getClaveUsuarioCaptura())){
                        usuarioCaptura = usuario.getClaveUsuarioCaptura();
                    }else{
                        usuarioCaptura = usuario.getCodigoPersona();
                    }
                    List<Map<String,String>>mpoliperMpersona =(List<Map<String, String>>) session.get("listaBeneficiarios");
                
                    paso = "Iterando registros";
                
                    String cdunieco   = (String)omap1.get("pv_cdunieco_i")
                           ,cdramo    = (String)omap1.get("pv_cdramo_i")
                           ,estado    = (String)omap1.get("pv_estado_i")
                           ,nmpoliza  = (String)omap1.get("pv_nmpoliza_i");
                
                    if(mpoliperMpersona!= null &&  mpoliperMpersona.size()>0) {
                    personasManager.guardarBeneficiarios(cdunieco, cdramo, estado, nmpoliza, usuarioCaptura, mpoliperMpersona);
                    session.put("listaBeneficiarios",null);
                    }
                }
            }
			
			logger.debug(Utils.log(
	                "\n###### guardarBeneficiarios ##########"
	                ,"\n#####################################"
	                ));
	        
    			try {
    			    logger.debug(paso);
    			 
    			    paso = "Validando tipo de endoso";
				logger.debug(paso);
				
				// Se determina el tipo de endoso solicitado:
				TipoEndoso tipoEndoso = smap1.get("altabaja").equalsIgnoreCase("alta") ? 
						TipoEndoso.ALTA_COBERTURAS : TipoEndoso.BAJA_COBERTURAS;
				if(tipoEndoso == TipoEndoso.ALTA_COBERTURAS) {
					logger.debug("ES UN ALTA DE COBERTURAS.....");
				} else {
					logger.debug("ES UNA BAJA DE COBERTURAS.....");
				}
				
				if(tipoEndoso == TipoEndoso.ALTA_COBERTURAS)
				{
					paso = "Validando nuevas coberturas";
					logger.debug(paso);
					
					// Se realiza validacion de fecha y nuevas coberturas:
					for(Map<String,String> nueva : slist1) 
					{
						endososManager.validaNuevaCobertura(
								(String)omap1.get("pv_cdunieco_i")
								,(String)omap1.get("pv_cdramo_i")
								,(String)omap1.get("pv_estado_i")
								,(String)omap1.get("pv_nmpoliza_i")
								,nueva.get("nmsituac")
								,nueva.get("garantia")
								);
					}
				}
				
				paso = "Iniciando endoso";
				logger.debug(paso);
				
				// Se inicia endoso:
				/*
				 * pv_cdunieco_i
				 * pv_cdramo_i
				 * pv_estado_i
				 * pv_nmpoliza_i
				 * pv_fecha_i
				 * pv_cdelemen_i
				 * pv_cdusuari_i
				 * pv_proceso_i
				 * pv_cdtipsup_i
				 */
				omap1.put("pv_fecha_i",renderFechas.parse((String)omap1.get("pv_fecha_i")));
				omap1.put("pv_cdelemen_i" , usuario.getEmpresa().getElementoId());
				omap1.put("pv_cdusuari_i" , usuario.getUser());
				omap1.put("pv_proceso_i"  , "END");
				omap1.put("pv_cdtipsup_i", tipoEndoso.getCdTipSup().toString());
				smap2 = endososManager.iniciaEndoso(omap1);
				
				for(Map<String,String> coberturasEditadas : slist1)
				{
					paso = "Ejecutando movimiento de cobertura";
					logger.debug(paso);
					
					// ****** Insertamos las coberturas correspondientes ******
					Map<String,String> paramsMovPoligar =new LinkedHashMap<String,String>(0);
					paramsMovPoligar.put("pv_cdunieco_i" , (String)omap1.get("pv_cdunieco_i"));
					paramsMovPoligar.put("pv_cdramo_i"   , (String)omap1.get("pv_cdramo_i"));
					paramsMovPoligar.put("pv_estado_i"   , (String)omap1.get("pv_estado_i"));
					paramsMovPoligar.put("pv_nmpoliza_i" , (String)omap1.get("pv_nmpoliza_i"));
					paramsMovPoligar.put("pv_nmsituac_i" , coberturasEditadas.get("nmsituac"));
					paramsMovPoligar.put("pv_cdgarant_i" , coberturasEditadas.get("garantia"));
					paramsMovPoligar.put("pv_nmsuplem_i" , smap2.get("pv_nmsuplem_o"));
					paramsMovPoligar.put("pv_cdcapita_i" , coberturasEditadas.get("cdcapita"));
					paramsMovPoligar.put("pv_status_i"   , coberturasEditadas.get("status"));
					paramsMovPoligar.put("pv_cdtipbca_i" , coberturasEditadas.get("cdtipbca"));
					paramsMovPoligar.put("pv_ptvalbas_i" , coberturasEditadas.get("ptvalbas"));
					paramsMovPoligar.put("pv_swmanual_i" , coberturasEditadas.get("swmanual"));
					paramsMovPoligar.put("pv_swreas_i"   , coberturasEditadas.get("swreas"));
					paramsMovPoligar.put("pv_cdagrupa_i" , coberturasEditadas.get("cdagrupa"));
					paramsMovPoligar.put("PV_ACCION"     , tipoEndoso == TipoEndoso.ALTA_COBERTURAS ? "I" : "B");
					paramsMovPoligar.put("pv_cdtipsup_i" , tipoEndoso.getCdTipSup().toString());
					kernelManager.movPoligar(paramsMovPoligar);
					
					paso = "Actualizando atributos adicionales de situaci\u00f3n";
					logger.debug(paso);
					
					// ****** Insertamos en TVALOSIT ******
					// Cargar Valosit anterior:
					Map<String,String>paramsValositAsegurado=new LinkedHashMap<String,String>(0);
					paramsValositAsegurado.put("pv_cdunieco_i" , (String)omap1.get("pv_cdunieco_i"));
					paramsValositAsegurado.put("pv_cdramo_i"   , (String)omap1.get("pv_cdramo_i"));
					paramsValositAsegurado.put("pv_estado_i"   , (String)omap1.get("pv_estado_i"));
					paramsValositAsegurado.put("pv_nmpoliza_i" , (String)omap1.get("pv_nmpoliza_i"));
					paramsValositAsegurado.put("pv_nmsituac_i" , coberturasEditadas.get("nmsituac"));
					Map<String,Object> valositAsegurado = kernelManager.obtieneValositSituac(paramsValositAsegurado);
					logger.debug("valosit anterior: "+valositAsegurado);
					// poner pv_ al anterior
					Map<String,Object> valositAseguradoIterado = new LinkedHashMap<String,Object>(0);
					Iterator it = valositAsegurado.entrySet().iterator();
					while(it.hasNext()) {
						Entry en = (Entry)it.next();
						valositAseguradoIterado.put("pv_"+(String)en.getKey(),en.getValue());//agregar pv_ a los anteriores
					}
					valositAsegurado = valositAseguradoIterado;
					logger.debug("se puso pv_ en el anterior");
					// convertir a string el total que es object:
					Map<String,String>paramsNuevos = new LinkedHashMap<String,String>(0);
					it = valositAsegurado.entrySet().iterator();
					while(it.hasNext()) {
						Entry en = (Entry)it.next();
						paramsNuevos.put((String)en.getKey(),(String)en.getValue());
					}
					logger.debug("se pasaron a string");
					/*
					pv_cdunieco
		    		pv_cdramo
		    		pv_estado
		    		pv_nmpoliza
		    		pv_nmsituac
		    		pv_nmsuplem
		    		pv_status
		    		pv_cdtipsit
		    		...pv_otvalor[01-50]
		    		pv_accion_i
					*/
					paramsNuevos.put("pv_cdunieco" , (String)omap1.get("pv_cdunieco_i"));
					paramsNuevos.put("pv_cdramo"   , (String)omap1.get("pv_cdramo_i"));
					paramsNuevos.put("pv_estado"   , (String)omap1.get("pv_estado_i"));
					paramsNuevos.put("pv_nmpoliza" , (String)omap1.get("pv_nmpoliza_i"));
					paramsNuevos.put("pv_nmsituac" , coberturasEditadas.get("nmsituac"));
					paramsNuevos.put("pv_nmsuplem" , smap2.get("pv_nmsuplem_o"));
					paramsNuevos.put("pv_status"   , "V");
					paramsNuevos.put("pv_cdtipsit" , coberturasEditadas.get("cdtipsit"));
					paramsNuevos.put("pv_accion_i" , "I");
					logger.debug("los actualizados seran: "+paramsNuevos);
					kernelManager.insertaValoresSituaciones(paramsNuevos);
					
					paso = "Actualizando atributos de coberturas adicionales";
					logger.debug(paso);
					
					// ****** Actualizamos TVALOSIT ******
					//
					endososManager.actualizaTvalositCoberturasAdicionales(
							(String)omap1.get("pv_cdunieco_i"), (String)omap1.get("pv_cdramo_i"),
							(String)omap1.get("pv_estado_i"), (String)omap1.get("pv_nmpoliza_i"),
							smap2.get("pv_nmsuplem_o"), tipoEndoso.getCdTipSup().toString());
				}
				
				// ****** Si es una alta ejecutamos valores por defecto ******
				if(tipoEndoso == TipoEndoso.ALTA_COBERTURAS)
				{
					paso = "Actualizando valores de sitauci\u00f3n ligados a valores de cobertura";
					logger.debug(paso);
					
					for(Map<String,String> coberturasEditadas : slist1)
					{
						String cdatribu1 = coberturasEditadas.get("cdatribu1");
						String otvalor1  = coberturasEditadas.get("otvalor1");
						String cdatribu2 = coberturasEditadas.get("cdatribu2");
						String otvalor2  = coberturasEditadas.get("otvalor2");
						String cdatribu3 = coberturasEditadas.get("cdatribu3");
						String otvalor3  = coberturasEditadas.get("otvalor3");
						if(StringUtils.isNotBlank(cdatribu1) && StringUtils.isNotBlank(otvalor1))
						{
							endososManager.actualizaTvalositSituacionCobertura(
									(String)omap1.get("pv_cdunieco_i"), (String)omap1.get("pv_cdramo_i"),
									(String)omap1.get("pv_estado_i"), (String)omap1.get("pv_nmpoliza_i"),
									smap2.get("pv_nmsuplem_o"),
									coberturasEditadas.get("nmsituac"),
									cdatribu1, otvalor1);
						}
						if(StringUtils.isNotBlank(cdatribu2) && StringUtils.isNotBlank(otvalor2))
						{
							endososManager.actualizaTvalositSituacionCobertura(
									(String)omap1.get("pv_cdunieco_i"), (String)omap1.get("pv_cdramo_i"),
									(String)omap1.get("pv_estado_i"), (String)omap1.get("pv_nmpoliza_i"),
									smap2.get("pv_nmsuplem_o"),
									coberturasEditadas.get("nmsituac"),
									cdatribu2, otvalor2);
						}
						if(StringUtils.isNotBlank(cdatribu3) && StringUtils.isNotBlank(otvalor3))
						{
							endososManager.actualizaTvalositSituacionCobertura(
									(String)omap1.get("pv_cdunieco_i"), (String)omap1.get("pv_cdramo_i"),
									(String)omap1.get("pv_estado_i"), (String)omap1.get("pv_nmpoliza_i"),
									smap2.get("pv_nmsuplem_o"),
									coberturasEditadas.get("nmsituac"),
									cdatribu3, otvalor3);
						}
					}
					
					paso = "Ejecutando valores por defecto";
					logger.debug(paso);
				
					for(Map<String,String> coberturasEditadas : slist1)
					{
						Map<String,String> paramSigsvdef = new LinkedHashMap<String,String>(0);
						paramSigsvdef.put("pv_cdunieco_i" , (String)omap1.get("pv_cdunieco_i"));
						paramSigsvdef.put("pv_cdramo_i"   , (String)omap1.get("pv_cdramo_i"));
						paramSigsvdef.put("pv_estado_i"   , (String)omap1.get("pv_estado_i"));
						paramSigsvdef.put("pv_nmpoliza_i" , (String)omap1.get("pv_nmpoliza_i"));
						paramSigsvdef.put("pv_nmsituac_i" , coberturasEditadas.get("nmsituac"));
						paramSigsvdef.put("pv_nmsuplem_i" , smap2.get("pv_nmsuplem_o"));
						paramSigsvdef.put("pv_cdgarant_i" , coberturasEditadas.get("garantia"));
						paramSigsvdef.put("pv_cdtipsup_i" , tipoEndoso.getCdTipSup().toString());
						kernelManager.coberturas(paramSigsvdef);
					}
				}
				
				paso = "Tarificando endoso";
				logger.debug(paso);
				
				// ****** Ejecutamos SIGSVDEF_END ******
				endososManager.sigsvalipolEnd(
						usuario.getUser()
						,usuario.getEmpresa().getElementoId()
						,(String)omap1.get("pv_cdunieco_i")
						,(String)omap1.get("pv_cdramo_i")
						,(String)omap1.get("pv_estado_i")
						,(String)omap1.get("pv_nmpoliza_i")
						,"0"
						,smap2.get("pv_nmsuplem_o")
						,tipoEndoso.getCdTipSup().toString()
						);
				
					paso = "Calculando valor de endoso";
					logger.debug(paso);
					
					Map<String,Object>paramCalcValorEndoso=new LinkedHashMap<String,Object>(0);
					paramCalcValorEndoso.put("pv_cdunieco_i" , (String)omap1.get("pv_cdunieco_i"));
					paramCalcValorEndoso.put("pv_cdramo_i"   , (String)omap1.get("pv_cdramo_i"));
					paramCalcValorEndoso.put("pv_estado_i"   , (String)omap1.get("pv_estado_i"));
					paramCalcValorEndoso.put("pv_nmpoliza_i" , (String)omap1.get("pv_nmpoliza_i"));
					paramCalcValorEndoso.put("pv_nmsituac_i" , "0");
					paramCalcValorEndoso.put("pv_nmsuplem_i" , smap2.get("pv_nmsuplem_o"));
					paramCalcValorEndoso.put("pv_feinival_i" , (Date)omap1.get("pv_fecha_i"));
					paramCalcValorEndoso.put("pv_cdtipsup_i", tipoEndoso.getCdTipSup().toString());
					endososManager.calcularValorEndoso(paramCalcValorEndoso);
					
					/*// Se confirma el endoso si cumple la validacion de fechas: 
					RespuestaConfirmacionEndosoVO respConfirmacionEndoso = this.confirmarEndoso(
							(String)omap1.get("pv_cdunieco_i")
							,(String)omap1.get("pv_cdramo_i")
							,(String)omap1.get("pv_estado_i")
							,(String)omap1.get("pv_nmpoliza_i")
							,smap2.get("pv_nmsuplem_o")
							,smap2.get("pv_nsuplogi_o")
							,tipoEndoso.getCdTipSup().toString()
							,""
							,(Date)omap1.get("pv_fecha_i")
							,null
							,flujo
							,usuario.getUser()
							,usuario.getRolActivo().getClave()
							);*/
					
					if(smap1.get("confirmar").equals("auto")){
						paso = "Realizando PDF de Vista Previa de Autos";
						logger.debug(paso);
						
						String cdunieco = (String)omap1.get("pv_cdunieco_i"),
							   cdramo   = (String)omap1.get("pv_cdramo_i"),
							   estado   = (String)omap1.get("pv_estado_i"),
							   nmpoliza = (String)omap1.get("pv_nmpoliza_i"),
							   nmsuplem = smap2.get("pv_nmsuplem_o");

						String rutaReports    = rutaServidorReports;
						String passReports    = passServidorReports;
						String rutaDocumentos = rutaDocumentosTemporal;
						String tipoFlotilla   = (String)omap1.get("tipoflot");
						String nombreReporte  = getText("rdf.endosos.nombre.auto.individual");
						String cdperpag       = smap1.get("CDPERPAG");
						String p_plan         = smap1.get("CDMEJRED");
						
						if(TipoFlotilla.Tipo_PyMES.getCdtipsit().equals(tipoFlotilla)){
							nombreReporte = getText("rdf.endosos.nombre.auto.pymes");
						}
						
						Date fechaHoy = new Date();
						
						String pdfEndosoNom = renderFechaHora.format(fechaHoy)+nmpoliza+"CotizacionPrevia.pdf";

						String url = rutaReports
								+ "?destype=cache"
								+ "&desformat=PDF"
								+ "&userid="+passReports
								+ "&report="+nombreReporte
								+ "&paramform=no"
								+ "&ACCESSIBLE=YES" //parametro que habilita salida en PDF
								+ "&p_unieco="+cdunieco
								+ "&p_ramo="+cdramo
								+ "&p_estado="+estado
								+ "&p_poliza="+nmpoliza
								+ "&p_suplem="+nmsuplem
								+ "&p_plan="+p_plan
								+ "&p_perpag="+cdperpag
								+ "&desname="+rutaDocumentos+"/"+pdfEndosoNom;
						
						paso = "Guardando PDF de Vista Previa de Autos en Temporal";
						logger.debug(paso);
						HttpUtil.generaArchivo(url,rutaDocumentos+"/"+pdfEndosoNom);
						
						smap2.put("pdfEndosoNom_o",pdfEndosoNom);
					}
					
					RespuestaConfirmacionEndosoVO respConfirmacionEndoso =new RespuestaConfirmacionEndosoVO();
					/*if(smap1.get("confirmar").equalsIgnoreCase("si")){
					 // Se confirma el endoso si cumple la validacion de fechas: 
                        respConfirmacionEndoso = this.confirmarEndoso(
                                (String)omap1.get("pv_cdunieco_i")
                                ,(String)omap1.get("pv_cdramo_i")
                                ,(String)omap1.get("pv_estado_i")
                                ,(String)omap1.get("pv_nmpoliza_i")
                                ,smap2.get("pv_nmsuplem_o")
                                ,smap2.get("pv_nsuplogi_o")
                                ,tipoEndoso.getCdTipSup().toString()
                                ,""
                                ,(Date)omap1.get("pv_fecha_i")
                                ,null
                                ,flujo
                                ,usuario.getUser()
                                ,usuario.getRolActivo().getClave()
                                );
					}
					*/
					if(smap1.get("confirmar").equalsIgnoreCase("si"))
					{
						for(Map<String,String> coberturasEditadas : slist1)
						{
							
							if(tipoEndoso == TipoEndoso.BAJA_COBERTURAS && coberturasEditadas.get("garantia").equals("040") ){
							    paso = "Asigando status Mpoliper a M";
		                        logger.debug(paso);
		                        endososManager.BeneficiarioVida_M((String)omap1.get("pv_cdunieco_i")
		                                                        , (String)omap1.get("pv_cdramo_i")
		                                                        , (String)omap1.get("pv_estado_i")
		                                                        , (String)omap1.get("pv_nmpoliza_i")
		                                                        , "0");
							    
								}
						}
						
                     // Se confirma el endoso si cumple la validacion de fechas: 
                        respConfirmacionEndoso = this.confirmarEndoso(
                                (String)omap1.get("pv_cdunieco_i")
                                ,(String)omap1.get("pv_cdramo_i")
                                ,(String)omap1.get("pv_estado_i")
                                ,(String)omap1.get("pv_nmpoliza_i")
                                ,smap2.get("pv_nmsuplem_o")
                                ,smap2.get("pv_nsuplogi_o")
                                ,tipoEndoso.getCdTipSup().toString()
                                ,""
                                ,(Date)omap1.get("pv_fecha_i")
                                ,null
                                ,flujo
                                ,usuario.getUser()
                                ,usuario.getRolActivo().getClave()
                                );
                     // Si el endoso fue confirmado:
					if(respConfirmacionEndoso.isConfirmado()) {
						endosoConfirmado = true;
					
						paso = "Generando documentos";
						logger.debug(paso);
						
					    ///////////////////////////////////////
					    ////// re generar los documentos //////
					    /*///////////////////////////////////*/
						Map<String,String> datosPoliza = documentosManager.generarDocumentosParametrizados(
								(String)omap1.get("pv_cdunieco_i")
								,(String)omap1.get("pv_cdramo_i")
								,(String)omap1.get("pv_estado_i")
								,(String)omap1.get("pv_nmpoliza_i")
								,"0" //nmsituac
								,smap2.get("pv_nmsuplem_o")
								,DocumentosManager.PROCESO_ENDOSO
								,null //ntramite
								,null //nmsolici
								, null
								);
						
						String ntramite = datosPoliza.get("ntramite");
						String nmsolici = datosPoliza.get("nmsolici");
						
						/*
					    List<Map<String,String>> docs = endososManager.reimprimeDocumentos(
					    		(String)omap1.get("pv_cdunieco_i"), (String)omap1.get("pv_cdramo_i"),
					    		(String)omap1.get("pv_estado_i"), (String)omap1.get("pv_nmpoliza_i"),
					    		respEndCob.get("pv_nmsuplem_o"), tipoEndoso.getCdTipSup().toString());
					    logger.debug("documentos que se regeneran: "+docs);
					    
					    if(!CollectionUtils.isEmpty(docs)) {
					    	
					    	String rutaCarpeta= new StringBuilder(this.rutaDocumentosPoliza)
			    				.append("/").append(docs.get(0).get("ntramite")).toString();
			    
							//listaDocu contiene: nmsolici,nmsituac,descripc,descripl
							for(Map<String,String> doc : docs) {
								logger.debug("docu iterado: "+doc);
								String nmsolici = doc.get("nmsolici");
								String nmsituac = doc.get("nmsituac");
								String descripc = doc.get("descripc");
								String descripl = doc.get("descripl");
								
								String url = new StringBuilder(this.rutaServidorReports)
										.append("?destype=cache")
										.append("&desformat=PDF")
										.append("&userid=").append(this.passServidorReports)
										.append("&report=").append(descripl)
										.append("&paramform=no").append("&ACCESSIBLE=YES") //habilita salida en PDF
										.append("&p_unieco=").append((String)omap1.get("pv_cdunieco_i"))
										.append("&p_ramo=").append((String)omap1.get("pv_cdramo_i"))
										.append("&p_estado=").append((String)omap1.get("pv_estado_i"))
										.append("&p_poliza=").append((String)omap1.get("pv_nmpoliza_i"))
										.append("&p_suplem=").append(respEndCob.get("pv_nmsuplem_o"))
										.append("&desname=").append(rutaCarpeta).append("/").append(descripc).toString();
								if(descripc.substring(0, 6).equalsIgnoreCase("CREDEN")) {
									// C R E D E N C I A L _ X X X X X X . P D F
									//0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
									url+="&p_cdperson="+descripc.substring(11, descripc.lastIndexOf("_"));
								}
								logger.debug(new StringBuilder()
										.append("\n#################################")
										.append("\n###### Se solicita reporte ######")
										.append("\na ").append(url)
										.append("\n#################################").toString());
								
								HttpUtil.generaArchivo(url, rutaCarpeta+"/"+descripc);
								
								logger.debug(new StringBuilder()
									.append("\n######                    ######")
									.append("\n###### reporte solicitado ######")
									.append("\na ").append(url)
									.append("\n################################")
									.append("\n################################").toString());
							}
					    }
					    */
					    /*///////////////////////////////////*/
						////// re generar los documentos //////
					    ///////////////////////////////////////
						
					    if(consultasManager.esProductoSalud((String)omap1.get("pv_cdramo_i"))) {
					    	// Si el producto es de Salud, ejecutamos el Web Service de Recibos:
							String sucursal = (String)omap1.get("pv_cdunieco_i");
							//String nmsolici = docs.get(0).get("nmsolici");
							String nmtramite = ntramite;//docs.get(0).get("ntramite");
							// En este caso tipomov y cdtipsup son iguales: 
							String tipomov = tipoEndoso.getCdTipSup().toString();
							String rutaCarpeta= new StringBuilder(this.rutaDocumentosPoliza)
		    				.append("/").append(nmtramite).toString();
							
							ice2sigsService.ejecutaWSrecibos((String)omap1.get("pv_cdunieco_i"), (String)omap1.get("pv_cdramo_i"), 
									(String)omap1.get("pv_estado_i"), (String)omap1.get("pv_nmpoliza_i"), 
									smap2.get("pv_nmsuplem_o"), rutaCarpeta, 
									sucursal, nmsolici, nmtramite, 
									true, tipomov, 
									(UserVO) session.get("USUARIO"));
					    }else{
					    	String cdunieco = (String)omap1.get("pv_cdunieco_i");
					    	String cdramo   = (String)omap1.get("pv_cdramo_i");
					    	String estado   = (String)omap1.get("pv_estado_i");
					    	String nmpoliza = (String)omap1.get("pv_nmpoliza_i");
					    	String nmsuplem = smap2.get("pv_nmsuplem_o");
					    	String nsuplogi = smap2.get("pv_nsuplogi_o");
					    	       ntramite = (String)omap1.get("pv_ntramite_i");
					    	       
					    	       
			    	        paso = "Realizando endoso en Web Service Autos";
							logger.debug(paso);
					    	
					    	EmisionAutosVO aux = emisionAutosService.cotizaEmiteAutomovilWS(cdunieco, cdramo, estado, nmpoliza, nmsuplem, ntramite, null, (UserVO) session.get("USUARIO"));
							if(aux == null || !aux.isExitoRecibos()){
								success = false;
								mensaje = "Error al generar el endoso, en WS. Consulte a Soporte.";
								error   = "Error al generar el endoso, en WS. Consulte a Soporte.";
								logger.error("Error al ejecutar los WS de endoso");
								
								boolean endosoRevertido = endososManager.revierteEndosoFallido(cdunieco, cdramo, estado, nmpoliza, nsuplogi, nmsuplem, (aux == null)? Integer.valueOf(99999) : aux.getResRecibos(), "Error en endoso Tavalosit auto, tipo: "+tipoEndoso.toString(), false);
								
								if(endosoRevertido){
									logger.error("Endoso revertido exitosamente.");
									error+=" Favor de volver a intentar.";
								}else{
									logger.error("Error al revertir el endoso");
									error+=" No se ha revertido el endoso.";
								}
								
						    	if(aux!=null && aux.isEndosoSinRetarif()){
						    		throw new ApplicationException("Endoso sin Tarifa. "+error);
						    	}
								
								return SUCCESS;
							}
							
							String tipoGrupoInciso = smap1.get("TIPOFLOT");
							
							Map<String,String> incisosAfectados = new HashMap<String, String>();
							
							for(Map<String,String> coberturasIncisos : slist1){
							
								String inciso = coberturasIncisos.get("nmsituac");
								
								if(StringUtils.isNotBlank(inciso)){
									incisosAfectados.put(inciso,inciso);
								}
							}
							logger.debug(Utils.log(
					                 "\n###### ntramite = " , ntramite
					                ,"\n###### (String)omap1.get(pv_ntramite_i) = " , (String)omap1.get("pv_ntramite_i")
					                ,"\n#####################################"
					                ));
							ejecutaCaratulaEndosoTarifaSigs(cdunieco,cdramo,estado,nmpoliza,nmsuplem, ntramite, tipoEndoso.getCdTipSup().toString(), tipoGrupoInciso, aux,incisosAfectados);
					    }
						
						mensaje = new StringBuilder("Se ha confirmado el endoso ").append(smap2.get("pv_nsuplogi_o")).toString();
						if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
		                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
		                }
					} else {
						mensaje = new StringBuilder("El endoso ")
									.append(smap2.get("pv_nsuplogi_o"))
									.append(" se guard\u00f3 en mesa de control para autorizaci\u00f3n ")
									.append("con n\u00famero de tr\u00e1mite ")
									.append(respConfirmacionEndoso.getNumeroTramite()).toString();
						if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
		                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
		                }
					}
				} else {
					mensaje = "Se ha guardado el endoso "+smap2.get("pv_nsuplogi_o");
					if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
	                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
	                }
				}
				
				success = true;
			}
			catch(Exception ex)
			{
				Utils.generaExcepcion(ex, paso);
			}
		}
		catch(Exception ex)
		{
			error = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### mensaje = " , mensaje
				,"\n###### error   = " , error 
				,"\n###### smap2 = " , smap2
				,"\n###### guardarEndosoCoberturas ######"
				,"\n#####################################"
				));
		
		return SUCCESS;
	}
	/*/////////////////////////////////*/
	////// guardarEndosoCoberturas //////
	/////////////////////////////////////
	
	/*/////////////////////////////////*/
	////// retarificarEndosos //////
	/////////////////////////////////////
	
public String retarificarEndosos()
	{
		logger.debug(Utils.log(
				 "\n#####################################"
				,"\n###### retarificarEndosos ###########"
				,"\n###### smap4  = " , smap4
				));
			this.session = ActionContext.getContext().getSession();
		
		
		try 
		{
			String paso = null;
			
			paso = "Recuperando Datos para Preview de Endosos";
			logger.debug(paso);
			
			Map<String,String>paramDetallePoliza=new LinkedHashMap<String,String>(0);
			paramDetallePoliza.put("pv_cdunieco_i" , smap4.get("cdunieco"));
			paramDetallePoliza.put("pv_cdramo_i"   , smap4.get("cdramo"  ));
			paramDetallePoliza.put("pv_estado_i"   , smap4.get("estado"  ));
			paramDetallePoliza.put("pv_nmpoliza_i" , smap4.get("nmpoliza"));
			paramDetallePoliza.put("pv_nmsuplem_i",  smap4.get("nmsuplem"));
			slist1=endososManager.retarificarEndosos(paramDetallePoliza);
			
			paso = "Realizando Sacaendoso de Preview";
			logger.debug(paso);
			
			endososManager.sacaEndoso(smap4.get("cdunieco")
									, smap4.get("cdramo")
									, smap4.get("estado")
									, smap4.get("nmpoliza")
									, smap4.get("nsuplogi")
									, smap4.get("nmsuplem"));
			
			success = true;
		}
		catch(Exception ex)
		{
			error = Utils.manejaExcepcion(ex);
		}
		
		
		logger.debug(""
				+ "\n######                              ######"
				+ "\n###### retarificarEndosos           ######"
				+ "\n##########################################"
				+ "\n##########################################"
				);
		return SUCCESS;
	}
	
	/*/////////////////////////////////*/
	////// retarificarEndosos //////
	/////////////////////////////////////
	
	/////////////////////////////////
	////// endoso B de valosit //////
	////// smap1:              //////
	//////     cdunieco        //////
	//////     cdramo          //////
	//////     estado          //////
	//////     nmpoliza        //////
	//////     cdtipsit        //////
	//////     nmsituac        //////
	//////     ntramite        //////
	//////     nmsuplem        //////
	/*/////////////////////////////*/
	public String entrarEndosoValositBasico()
	{
		logger.debug(Utils.log(
				 "\n#######################################"
		        ,"\n###### entrarEndosoValositBasico ######"
		        ,"\n###### smap1="  , smap1
		        ,"\n###### slist1=" , slist1
		        ));
		
		String respuesta = ERROR;
		
		try
		{
			transformaEntrada(smap1, slist1, true);
			
			// Valida si hay un endoso anterior pendiente:
			RespuestaVO resp = endososManager.validaEndosoAnterior(
					smap1.get("cdunieco")
					,smap1.get("cdramo")
					,smap1.get("estado")
					,smap1.get("nmpoliza")
					,TipoEndoso.CORRECCION_ANTIGUEDAD_Y_PARENTESCO.getCdTipSup().toString());
			error = resp.getMensaje();
			if(!resp.isSuccess())
			{
				throw new ApplicationException(resp.getMensaje());
			}
			
			String cdusuari;
			{
				UserVO usuario = (UserVO)session.get("USUARIO");
				cdusuari=usuario.getUser();
			}
			List<ComponenteVO>tatrisit=kernelManager.obtenerTatrisit(smap1.get("cdtipsit"),cdusuari);
			GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			gc.setCdtipsit(smap1.get("cdtipsit"));
			List<ComponenteVO>tatriTemp=new ArrayList<ComponenteVO>(0);
			//si es agrupado solo dejar los atributos con N, si es individual solo los que tengan S
			for(ComponenteVO t:tatrisit) {
				//S=individual
				if(t.getSwsuscri().equalsIgnoreCase("S")&&t.getSwtarifi().equalsIgnoreCase("N")) {
					tatriTemp.add(t);
				}
			}
			tatrisit=tatriTemp;
			gc.genera(tatrisit);
			item1=gc.getFields();
			item2=gc.getItems();
			
			respuesta = SUCCESS;
		}
		catch(Exception ex)
		{
			logger.error("error al mostrar pantalla de endoso valosit basico",ex);
			error = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### respuesta=",respuesta
				,"\n###### entrarEndosoValositBasico ######"
				,"\n#######################################"
		        ));
		return respuesta;
	}

	public String entrarEndosoParentescoAntiguedad()
	{
		logger.debug(Utils.log(
				"\n###############################################"
				,"\n###### entrarEndosoParentescoAntiguedad ######"
				,"\n###### smap1="  , smap1
				,"\n###### slist1=" , slist1
				));
		
		String respuesta = ERROR;
		
		try
		{
			transformaEntrada(smap1, slist1, true);
			
			if(smap1 != null && !smap1.isEmpty()){
				if(!smap1.containsKey("FEINIVAL") && smap1.containsKey("FEEFECTO")){
					smap1.put("FEINIVAL", smap1.get("FEEFECTO"));
				}
			}
			
			// Valida si hay un endoso anterior pendiente:
			RespuestaVO resp = endososManager.validaEndosoAnterior(
					smap1.get("cdunieco")
					,smap1.get("cdramo")
					,smap1.get("estado")
					,smap1.get("nmpoliza")
					,TipoEndoso.CORRECCION_ANTIGUEDAD_Y_PARENTESCO.getCdTipSup().toString());
			error = resp.getMensaje();
			if(!resp.isSuccess())
			{
				throw new ApplicationException(resp.getMensaje());
			}
			
			String cdusuari;
			{
				UserVO usuario = (UserVO)session.get("USUARIO");
				cdusuari=usuario.getUser();
			}
			List<ComponenteVO>tatrisit=kernelManager.obtenerTatrisit(smap1.get("cdtipsit"),cdusuari);
			GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			gc.setCdtipsit(smap1.get("cdtipsit"));
			List<ComponenteVO>tatriTemp=new ArrayList<ComponenteVO>(0);
			//si es agrupado solo dejar los atributos con N, si es individual solo los que tengan S
			for(ComponenteVO t:tatrisit) {
				//S=individual
				if(t.getSwsuscri().equalsIgnoreCase("S")&&t.getSwtarifi().equalsIgnoreCase("N")) {
					tatriTemp.add(t);
				}
			}
			tatrisit=tatriTemp;
			gc.genera(tatrisit);
			item1=gc.getFields();
			item2=gc.getItems();
			
			respuesta = SUCCESS;
		}
		catch(Exception ex)
		{
			logger.error("error al mostrar pantalla de endoso valosit basico",ex);
			error = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				"\n###### respuesta=",respuesta
				,"\n###### entrarEndosoParentescoAntiguedad ######"
				,"\n##############################################"
				));
		return respuesta;
	}
	/*/////////////////////////////*/
	////// endoso B de valosit //////
	/////////////////////////////////
	
	/////////////////////////////////
	////// endoso B de valosit //////
	////// smap1:              //////
	//////     cdunieco        //////
	//////     cdramo          //////
	//////     estado          //////
	//////     nmpoliza        //////
	//////     cdtipsit        //////
	//////     nmsituac        //////
	//////     ntramite        //////
	//////     nmsuplem        //////
	/*/////////////////////////////*/
	public String entrarEndosoValositBasicoSimple()
	{
		logger.debug(Utils.log(
		         "\n#############################################"
		        ,"\n###### entrarEndosoValositBasicoSimple ######"
		        ,"\n###### smap1="  , smap1
		        ,"\n###### slist1=" , slist1
		        ));
		
		endosoSimple     = true;
		String respuesta = ERROR;
		try
		{
			transformaEntrada(smap1, slist1, true);
			
			String cdunieco = smap1.get("cdunieco");
			String cdramo   = smap1.get("cdramo");
			String estado   = smap1.get("estado");
			String nmpoliza = smap1.get("nmpoliza");
			
			// Validacion de endoso simple:
			boolean permitido = endososManager.validaEndosoSimple(cdunieco, cdramo, estado, nmpoliza);
			if(!permitido) {
				throw new ApplicationException(EndososAction.ENDOSO_SIMPLE_NO_PERMITIDO);
			}
			
			// Valida si hay un endoso anterior pendiente:
			RespuestaVO resp = endososManager.validaEndosoAnterior(
					smap1.get("cdunieco"), smap1.get("cdramo"), smap1.get("estado"), smap1.get("nmpoliza"),
					TipoEndoso.CORRECCION_DATOS_ASEGURADOS.getCdTipSup().toString());
			// Si la validacion no es exitosa lanzamos excepcion con el mensaje de error:
			if(!resp.isSuccess()) {
				throw new ApplicationException(resp.getMensaje());
			}
			
			mensaje = endososManager.obtieneFechaInicioVigenciaPoliza(cdunieco, cdramo, estado, nmpoliza);
			
			String cdusuari;
			{
				UserVO usuario=(UserVO)session.get("USUARIO");
				cdusuari=usuario.getUser();
			}
			List<ComponenteVO>tatrisit=kernelManager.obtenerTatrisit(smap1.get("cdtipsit"),cdusuari);
			GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			gc.setCdtipsit(smap1.get("cdtipsit"));
			List<ComponenteVO>tatriTemp=new ArrayList<ComponenteVO>(0);
			for(ComponenteVO t:tatrisit) {
				//si es agrupado solo dejar los atributos con N, si es individual solo los que tengan S (S=individual)
				if(t.getSwsuscri().equalsIgnoreCase("S")&&t.getSwtarifi().equalsIgnoreCase("N")) {
					tatriTemp.add(t);
				}
			}
			tatrisit=tatriTemp;
			gc.genera(tatrisit);
			item1=gc.getFields();
			item2=gc.getItems();
			
			respuesta = SUCCESS;
		}
		catch(Exception ex)
		{
			respuesta = ERROR;
			error     = Utils.manejaExcepcion(ex);
			logger.error("Error al mostrar la pantalla de valosit", ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### respuesta=",respuesta
				,"\n###### entrarEndosoValositBasicoSimple ######"
		        ,"\n#############################################"
		        ));
		
		return respuesta;
	}
	/*/////////////////////////////*/
	////// endoso B de valosit //////
	/////////////////////////////////
	
	///////////////////////////////////////////
	////// guardar endoso valosit basico //////
	////// smap1:                        //////
	//////     fecha_endoso              //////
	//////     cdunieco                  //////
	//////     cdramo                    //////
	//////     estado                    //////
	//////     nmpoliza                  //////
	//////     cdtipsit                  //////
	//////     nmsituac                  //////
	//////     confirmar                 //////
	////// parametros: tvalosit          //////
	/*///////////////////////////////////////*/
	public String guardarEndosoValositBasico(){
		
		logger.debug(Utils.log(
				 "\n########################################"
				,"\n###### guardarEndosoValositBasico ######"
				,"\n###### smap1      = " , smap1
				,"\n###### parametros = " , parametros
				,"\n###### flujo      = " , flujo
				));
		
		try {
			UserVO usuario=(UserVO)session.get("USUARIO");
			/*
			 * pv_cdunieco_i
			 * pv_cdramo_i
			 * pv_estado_i
			 * pv_nmpoliza_i
			 * pv_fecha_i
			 * pv_cdelemen_i
			 * pv_cdusuari_i
			 * pv_proceso_i
			 * pv_cdtipsup_i
			 */
			omap1=new LinkedHashMap<String,Object>();
			omap1.put("pv_cdunieco_i" , smap1.get("cdunieco"));
			omap1.put("pv_cdramo_i"   , smap1.get("cdramo"));
			omap1.put("pv_estado_i"   , smap1.get("estado"));
			omap1.put("pv_nmpoliza_i" , smap1.get("nmpoliza"));
			omap1.put("pv_fecha_i"    , renderFechas.parse(smap1.get("fecha_endoso")));
			omap1.put("pv_cdelemen_i" , usuario.getEmpresa().getElementoId());
			omap1.put("pv_cdusuari_i" , usuario.getUser());
			omap1.put("pv_proceso_i"  , "END");
			omap1.put("pv_cdtipsup_i", TipoEndoso.CORRECCION_ANTIGUEDAD_Y_PARENTESCO.getCdTipSup().toString());
			Map<String,String> respEnd=endososManager.iniciaEndoso(omap1);
			
			//cargar anterior valosit
			Map<String,String>paramsValositAsegurado=new LinkedHashMap<String,String>(0);
			paramsValositAsegurado.put("pv_cdunieco_i", smap1.get("cdunieco"));
			paramsValositAsegurado.put("pv_cdramo_i",   smap1.get("cdramo"));
			paramsValositAsegurado.put("pv_estado_i",   smap1.get("estado"));
			paramsValositAsegurado.put("pv_nmpoliza_i", smap1.get("nmpoliza"));
			paramsValositAsegurado.put("pv_nmsituac_i", smap1.get("nmsituac"));
			Map<String,Object>valositAsegurado=kernelManager.obtieneValositSituac(paramsValositAsegurado);
			logger.debug("valosit anterior: "+valositAsegurado);
			
			//poner pv_ al anterior
			Map<String,Object>valositAseguradoIterado=new LinkedHashMap<String,Object>(0);
			Iterator it=valositAsegurado.entrySet().iterator();
			while(it.hasNext()) {
				Entry en=(Entry)it.next();
				valositAseguradoIterado.put("pv_"+(String)en.getKey(),en.getValue());//agregar pv_ a los anteriores
			}
			valositAsegurado=valositAseguradoIterado;
			logger.debug("se puso pv_ en el anterior");
			
			//agregar los del form al leido
			Iterator it2=parametros.entrySet().iterator();
			while(it2.hasNext()) {
				Entry en=(Entry)it2.next();
				valositAsegurado.put((String)en.getKey(),en.getValue());//tienen pv_ los del form
				//ya agregamos todos los nuevos en el mapa
			}
			logger.debug("se agregaron los nuevos");
			
			//convertir a string el total
			Map<String,String>paramsNuevos=new LinkedHashMap<String,String>(0);
			it=valositAsegurado.entrySet().iterator();
			while(it.hasNext()) {
				Entry en=(Entry)it.next();
				paramsNuevos.put((String)en.getKey(),(String)en.getValue());
			}
			logger.debug("se pasaron a string");
			
			/*
			pv_cdunieco
    		pv_cdramo
    		pv_estado
    		pv_nmpoliza
    		pv_nmsituac
    		pv_nmsuplem
    		pv_status
    		pv_cdtipsit
    		...pv_otvalor[01-50]
    		pv_accion_i
			*/
			paramsNuevos.put("pv_cdunieco" , smap1.get("cdunieco"));
			paramsNuevos.put("pv_cdramo"   , smap1.get("cdramo"));
			paramsNuevos.put("pv_estado"   , smap1.get("estado"));
			paramsNuevos.put("pv_nmpoliza" , smap1.get("nmpoliza"));
			paramsNuevos.put("pv_nmsituac" , smap1.get("nmsituac"));
			paramsNuevos.put("pv_nmsuplem" , respEnd.get("pv_nmsuplem_o"));
			paramsNuevos.put("pv_status"   , "V");
			paramsNuevos.put("pv_cdtipsit" , smap1.get("cdtipsit"));
			paramsNuevos.put("pv_accion_i" , "I");
			logger.debug("los actualizados seran: "+paramsNuevos);
			kernelManager.insertaValoresSituaciones(paramsNuevos);

/**
 * No se requiere mandar mpoliper, se comentan lineas
 */
			
			//////////////////////
			////// cdperson //////
//			Map<String,String>mapCdperson=new HashMap<String,String>(0);
//			mapCdperson.put("pv_cdunieco" , smap1.get("cdunieco"));
//			mapCdperson.put("pv_cdramo"   , smap1.get("cdramo"));
//			mapCdperson.put("pv_estado"   , smap1.get("estado"));
//			mapCdperson.put("pv_nmpoliza" , smap1.get("nmpoliza"));
//			mapCdperson.put("pv_nmsituac" , smap1.get("nmsituac"));
//			List<Map<String,String>>listCdperson=endososManager.obtenerCdpersonMpoliper(mapCdperson);
			////// cdperson //////
			//////////////////////
			
            //////////////////////
			////// mpoliper //////
//			Map<String,Object>mapaMpoliper=new LinkedHashMap<String,Object>(0);
//			mapaMpoliper.put("pv_cdunieco_i" , smap1.get("cdunieco"));
//			mapaMpoliper.put("pv_cdramo_i"   , smap1.get("cdramo"));
//			mapaMpoliper.put("pv_estado_i"   , smap1.get("estado"));
//			mapaMpoliper.put("pv_nmpoliza_i" , smap1.get("nmpoliza"));
//			mapaMpoliper.put("pv_nmsituac_i" , smap1.get("nmsituac"));
//			mapaMpoliper.put("pv_cdrol_i"    , "2");
//			mapaMpoliper.put("pv_cdperson_i" , listCdperson.get(0).get("CDPERSON"));
//			mapaMpoliper.put("pv_nmsuplem_i" , respEnd.get("pv_nmsuplem_o"));
//			mapaMpoliper.put("pv_status_i"   , "V");
//			mapaMpoliper.put("pv_nmorddom_i" , "1");
//			mapaMpoliper.put("pv_swreclam_i" , null);
//			mapaMpoliper.put("pv_accion_i"   , "I");
//			mapaMpoliper.put("pv_swexiper_i" , Constantes.SI);
//			kernelManager.movMpoliper(mapaMpoliper);
			////// mpoliper //////
			//////////////////////
			
			if(smap1.get("confirmar").equalsIgnoreCase("si")) {
				// Se confirma el endoso si cumple la validacion de fechas: 
				RespuestaConfirmacionEndosoVO respConfirmacionEndoso = this.confirmarEndoso(
						smap1.get("cdunieco"),
						smap1.get("cdramo"),
						smap1.get("estado"),
						smap1.get("nmpoliza"),
						respEnd.get("pv_nmsuplem_o"),
						respEnd.get("pv_nsuplogi_o"),
						TipoEndoso.CORRECCION_ANTIGUEDAD_Y_PARENTESCO.getCdTipSup().toString(),
						"",
						renderFechas.parse(smap1.get("fecha_endoso")),
						smap1.get("cdtipsit")
						,flujo
						,usuario.getUser()
						,usuario.getRolActivo().getClave()
						);
		    
				// Si el endoso fue confirmado:
				if(respConfirmacionEndoso.isConfirmado()) {
					endosoConfirmado = true;
				
				    ///////////////////////////////////////
				    ////// re generar los documentos //////
				    /*///////////////////////////////////*/
					documentosManager.generarDocumentosParametrizados(
							smap1.get("cdunieco")
							,smap1.get("cdramo")
							,smap1.get("estado")
							,smap1.get("nmpoliza")
							,"0" //nmsituac
							,respEnd.get("pv_nmsuplem_o")
							,DocumentosManager.PROCESO_ENDOSO
							,null //ntramite
							,null //nmsolici
, null
							);
					
					/*
				    List<Map<String,String>>listaDocu=endososManager.reimprimeDocumentos(
				    		smap1.get("cdunieco")
				    		,smap1.get("cdramo")
				    		,smap1.get("estado")
				    		,smap1.get("nmpoliza")
				    		,respEnd.get("pv_nmsuplem_o")
				    		,TipoEndoso.CORRECCION_ANTIGUEDAD_Y_PARENTESCO.getCdTipSup().toString()
				    		);
				    logger.debug("documentos que se regeneran: "+listaDocu);
				    
				    String rutaCarpeta=this.rutaDocumentosPoliza+"/"+listaDocu.get(0).get("ntramite");
				    
					//listaDocu contiene: nmsolici,nmsituac,descripc,descripl
					for(Map<String,String> docu:listaDocu) {
						logger.debug("docu iterado: "+docu);
						String nmsolici=docu.get("nmsolici");
						String nmsituac=docu.get("nmsituac");
						String descripc=docu.get("descripc");
						String descripl=docu.get("descripl");
						String url=this.rutaServidorReports
								+ "?destype=cache"
								+ "&desformat=PDF"
								+ "&userid="+this.passServidorReports
								+ "&report="+descripl
								+ "&paramform=no"
								+ "&ACCESSIBLE=YES" //parametro que habilita salida en PDF
								+ "&p_unieco="+smap1.get("cdunieco")
								+ "&p_ramo="+smap1.get("cdramo")
								+ "&p_estado="+smap1.get("estado")
								+ "&p_poliza="+smap1.get("nmpoliza")
								+ "&p_suplem="+respEnd.get("pv_nmsuplem_o")
								+ "&desname="+rutaCarpeta+"/"+descripc;
						if(descripc.substring(0, 6).equalsIgnoreCase("CREDEN")) {
							// C R E D E N C I A L _ X X X X X X . P D F
							//0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
							url+="&p_cdperson="+descripc.substring(11, descripc.lastIndexOf("_"));
						}
						logger.debug(""
								+ "\n#################################"
								+ "\n###### Se solicita reporte ######"
								+ "\na "+url+""
								+ "\n#################################");
						HttpUtil.generaArchivo(url,rutaCarpeta+"/"+descripc);
						logger.debug(""
								+ "\n######                    ######"
								+ "\n###### reporte solicitado ######"
								+ "\na "+url+""
								+ "\n################################"
								+ "\n################################"
								+ "");
					}
					*/
				    /*///////////////////////////////////*/
					////// re generar los documentos //////
				    ///////////////////////////////////////
					
					mensaje="Se ha confirmado el endoso "+respEnd.get("pv_nsuplogi_o");
					if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
	                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
	                }
				} else {
					mensaje="El endoso "+respEnd.get("pv_nsuplogi_o")
							+" se guard\u00f3 en mesa de control para autorizaci\u00f3n "
							+ "con n\u00famero de tr\u00e1mite " + respConfirmacionEndoso.getNumeroTramite();
					if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
	                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
	                }
				}
			} else {
				mensaje="Se ha guardado el endoso "+respEnd.get("pv_nsuplogi_o");
			}
			success=true;
		} catch(Exception ex) {
			logger.error("error al guardar endosos de valosit basico",ex);
			success=false;
			error=ex.getMessage();
		}
		
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### error   = " , error
				,"\n###### mensaje = " , mensaje
				,"\n###### guardarEndosoValositBasico ######"
				,"\n########################################"
				));
		
		return SUCCESS;
	}

	public String guardarEndosoParentescoAntiguedad()
	{
		logger.debug(Utils.log(
				 "\n###############################################"
				,"\n###### guardarEndosoParentescoAntiguedad ######"
				,"\n###### smap1      = " , smap1
				,"\n###### slist1     = " , slist1
				,"\n###### parametros = " , parametros
				,"\n###### flujo      = " , flujo
				));
		
		try {
			UserVO usuario=(UserVO)session.get("USUARIO");
			/*
			 * pv_cdunieco_i
			 * pv_cdramo_i
			 * pv_estado_i
			 * pv_nmpoliza_i
			 * pv_fecha_i
			 * pv_cdelemen_i
			 * pv_cdusuari_i
			 * pv_proceso_i
			 * pv_cdtipsup_i
			 */
			
			boolean simple   = "S".equals(smap1.get("simple"));
			String  cdtipsup = simple ?
                                   TipoEndoso.CORRECCION_DATOS_ASEGURADOS.getCdTipSup().toString() :
                                   TipoEndoso.CORRECCION_ANTIGUEDAD_Y_PARENTESCO.getCdTipSup().toString();
			
			omap1=new LinkedHashMap<String,Object>();
			omap1.put("pv_cdunieco_i" , smap1.get("cdunieco"));
			omap1.put("pv_cdramo_i"   , smap1.get("cdramo"));
			omap1.put("pv_estado_i"   , smap1.get("estado"));
			omap1.put("pv_nmpoliza_i" , smap1.get("nmpoliza"));
			omap1.put("pv_fecha_i"    , renderFechas.parse(smap1.get("fecha_endoso")));
			omap1.put("pv_cdelemen_i" , usuario.getEmpresa().getElementoId());
			omap1.put("pv_cdusuari_i" , usuario.getUser());
			omap1.put("pv_proceso_i"  , "END");
			omap1.put("pv_cdtipsup_i" , cdtipsup);
			Map<String,String> respEnd=endososManager.iniciaEndoso(omap1);
			
			
			for(Map<String,String> asegurado : slist1){
				
				//cargar anterior valosit
				Map<String,String>paramsValositAsegurado=new LinkedHashMap<String,String>(0);
				paramsValositAsegurado.put("pv_cdunieco_i", smap1.get("cdunieco"));
				paramsValositAsegurado.put("pv_cdramo_i",   smap1.get("cdramo"));
				paramsValositAsegurado.put("pv_estado_i",   smap1.get("estado"));
				paramsValositAsegurado.put("pv_nmpoliza_i", smap1.get("nmpoliza"));
				paramsValositAsegurado.put("pv_nmsituac_i", asegurado.get("NMSITUAC"));
				Map<String,Object>valositAsegurado=kernelManager.obtieneValositSituac(paramsValositAsegurado);
				logger.debug("valosit anterior: "+valositAsegurado);
				
				//poner pv_ al anterior
				Map<String,Object>valositAseguradoIterado=new LinkedHashMap<String,Object>(0);
				Iterator it=valositAsegurado.entrySet().iterator();
				while(it.hasNext()) {
					Entry en=(Entry)it.next();
					valositAseguradoIterado.put("pv_"+(String)en.getKey(),en.getValue());//agregar pv_ a los anteriores
				}
				valositAsegurado=valositAseguradoIterado;
				logger.debug("se puso pv_ en el anterior");
				
				try{
					//Meter los nuevos valores de parentesco y antiguedad
					String numParentesco = endososManager.obtieneNumeroAtributo(smap1.get("cdtipsit"), "PARENTESCO");
					String numAntiguedad = endososManager.obtieneNumeroAtributo(smap1.get("cdtipsit"), "F. ANTIG");
					
					
					String paramParent = new StringBuilder("pv_otvalor").append(StringUtils.leftPad(String.valueOf(numParentesco), 2, "0")).toString();
					String paramAntig  = new StringBuilder("pv_otvalor").append(StringUtils.leftPad(String.valueOf(numAntiguedad), 2, "0")).toString();
					
					logger.debug("Insertando parametro: " + paramParent + " con valor de: "+asegurado.get("CDPARENTESCO"));
					logger.debug("Insertando parametro: " + paramAntig + " con valor de: "+asegurado.get("FECANTIG"));
					
					valositAseguradoIterado.put(paramParent, asegurado.get("CDPARENTESCO"));
					valositAseguradoIterado.put(paramAntig, asegurado.get("FECANTIG"));
					
					logger.debug("se agregaron los nuevos");
				
				}catch(Exception ex){
					logger.error("Error al insertar los atributos de parentesco y antig&#252;edad",ex);
					throw new ApplicationException("Error al insertar los atributos de parentesco y antiguedad.");
				}
				
				//convertir a string el total
				Map<String,String>paramsNuevos=new LinkedHashMap<String,String>(0);
				it=valositAsegurado.entrySet().iterator();
				while(it.hasNext()) {
					Entry en=(Entry)it.next();
					paramsNuevos.put((String)en.getKey(),(String)en.getValue());
				}
				logger.debug("se pasaron a string");
				
				/*
				pv_cdunieco
	    		pv_cdramo
	    		pv_estado
	    		pv_nmpoliza
	    		pv_nmsituac
	    		pv_nmsuplem
	    		pv_status
	    		pv_cdtipsit
	    		...pv_otvalor[01-50]
	    		pv_accion_i
				 */
				paramsNuevos.put("pv_cdunieco" , smap1.get("cdunieco"));
				paramsNuevos.put("pv_cdramo"   , smap1.get("cdramo"));
				paramsNuevos.put("pv_estado"   , smap1.get("estado"));
				paramsNuevos.put("pv_nmpoliza" , smap1.get("nmpoliza"));
				paramsNuevos.put("pv_nmsituac" , asegurado.get("NMSITUAC"));
				paramsNuevos.put("pv_nmsuplem" , respEnd.get("pv_nmsuplem_o"));
				paramsNuevos.put("pv_status"   , "V");
				paramsNuevos.put("pv_cdtipsit" , smap1.get("cdtipsit"));
				paramsNuevos.put("pv_accion_i" , "I");
				logger.debug("los actualizados seran: "+paramsNuevos);
				kernelManager.insertaValoresSituaciones(paramsNuevos);
				
			}
			
			// Validacion de duplicidad de parentesco:
			try {
				endososManager.validaDuplicidadParentesco(smap1.get("cdunieco"), smap1.get("cdramo"), smap1.get("estado"), smap1.get("nmpoliza"), respEnd.get("pv_nmsuplem_o"));
			} catch(Exception ae) {
				
				// Si falla se elimina el endoso:
				endososManager.sacaEndoso(smap1.get("cdunieco"), smap1.get("cdramo"), smap1.get("estado"), smap1.get("nmpoliza"), respEnd.get("pv_nsuplogi_o"), respEnd.get("pv_nmsuplem_o"));
				// Se asigna el mensaje de error para el usuario:
				throw new ApplicationException(ae.getMessage());
			}
			
			if(smap1.get("confirmar").equalsIgnoreCase("si")) {
				// Se confirma el endoso si cumple la validacion de fechas: 
				RespuestaConfirmacionEndosoVO respConfirmacionEndoso = this.confirmarEndoso(
						smap1.get("cdunieco"),
						smap1.get("cdramo"),
						smap1.get("estado"),
						smap1.get("nmpoliza"),
						respEnd.get("pv_nmsuplem_o"),
						respEnd.get("pv_nsuplogi_o"),
						cdtipsup,
						"",
						renderFechas.parse(smap1.get("fecha_endoso")),
						smap1.get("cdtipsit")
						,flujo
						,usuario.getUser()
						,usuario.getRolActivo().getClave()
						);
				
				// Si el endoso fue confirmado:
				if(respConfirmacionEndoso.isConfirmado()) {
					endosoConfirmado = true;
					
					///////////////////////////////////////
					////// re generar los documentos //////
					/*///////////////////////////////////*/
					documentosManager.generarDocumentosParametrizados(
							smap1.get("cdunieco")
							,smap1.get("cdramo")
							,smap1.get("estado")
							,smap1.get("nmpoliza")
							,"0" //nmsituac
							,respEnd.get("pv_nmsuplem_o")
							,DocumentosManager.PROCESO_ENDOSO
							,null //ntramite
							,null //nmsolici
, null
							);
					
					/*
					List<Map<String,String>>listaDocu=endososManager.reimprimeDocumentos(
							smap1.get("cdunieco")
							,smap1.get("cdramo")
							,smap1.get("estado")
							,smap1.get("nmpoliza")
							,respEnd.get("pv_nmsuplem_o")
							,TipoEndoso.CORRECCION_ANTIGUEDAD_Y_PARENTESCO.getCdTipSup().toString()
							);
					logger.debug("documentos que se regeneran: "+listaDocu);
					
					String rutaCarpeta=this.rutaDocumentosPoliza+"/"+listaDocu.get(0).get("ntramite");
					
					//listaDocu contiene: nmsolici,nmsituac,descripc,descripl
					for(Map<String,String> docu:listaDocu) {
						logger.debug("docu iterado: "+docu);
						String nmsolici=docu.get("nmsolici");
						String nmsituac=docu.get("nmsituac");
						String descripc=docu.get("descripc");
						String descripl=docu.get("descripl");
						String url=this.rutaServidorReports
								+ "?destype=cache"
								+ "&desformat=PDF"
								+ "&userid="+this.passServidorReports
								+ "&report="+descripl
								+ "&paramform=no"
								+ "&ACCESSIBLE=YES" //parametro que habilita salida en PDF
								+ "&p_unieco="+smap1.get("cdunieco")
								+ "&p_ramo="+smap1.get("cdramo")
								+ "&p_estado="+smap1.get("estado")
								+ "&p_poliza="+smap1.get("nmpoliza")
								+ "&p_suplem="+respEnd.get("pv_nmsuplem_o")
								+ "&desname="+rutaCarpeta+"/"+descripc;
						if(descripc.substring(0, 6).equalsIgnoreCase("CREDEN")) {
							// C R E D E N C I A L _ X X X X X X . P D F
							//0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
							url+="&p_cdperson="+descripc.substring(11, descripc.lastIndexOf("_"));
						}
						logger.debug(""
								+ "\n#################################"
								+ "\n###### Se solicita reporte ######"
								+ "\na "+url+""
								+ "\n#################################");
						HttpUtil.generaArchivo(url,rutaCarpeta+"/"+descripc);
						logger.debug(""
								+ "\n######                    ######"
								+ "\n###### reporte solicitado ######"
								+ "\na "+url+""
								+ "\n################################"
								+ "\n################################"
								+ "");
					}
					*/
					/*///////////////////////////////////*/
					////// re generar los documentos //////
					///////////////////////////////////////
					
					mensaje="Se ha confirmado el endoso "+respEnd.get("pv_nsuplogi_o");
					if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
	                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
	                }
				} else {
					mensaje="El endoso "+respEnd.get("pv_nsuplogi_o")
					+" se guard\u00f3 en mesa de control para autorizaci\u00f3n "
					+ "con n\u00famero de tr\u00e1mite " + respConfirmacionEndoso.getNumeroTramite();
					if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
	                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
	                }
				}
			} else {				
				mensaje="Se ha guardado el endoso "+respEnd.get("pv_nsuplogi_o");
			}
			success=true;
		} catch(Exception ex) {
			logger.error("error al guardar endosos de valosit basico",ex);
			success=false;
			error=ex.getMessage();
		}
		
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### error   = " , error
				,"\n###### mensaje = " , mensaje
				,"\n###### guardarEndosoParentescoAntiguedad ######"
				,"\n###############################################"
				));
		
		return SUCCESS;
	}
	/*///////////////////////////////////////*/
	////// guardar endoso valosit basico //////
	///////////////////////////////////////////
	
	///////////////////////////////////////////
	////// guardar endoso valosit basico //////
	////// smap1:                        //////
	//////     fecha_endoso              //////
	//////     cdunieco                  //////
	//////     cdramo                    //////
	//////     estado                    //////
	//////     nmpoliza                  //////
	//////     cdtipsit                  //////
	//////     nmsituac                  //////
	//////     confirmar                 //////
	////// parametros: tvalosit          //////
	/*///////////////////////////////////////*/
	public String guardarEndosoValositBasicoSimple() {
		
		logger.debug(Utils.log(
				 "\n##############################################"
				,"\n###### guardarEndosoValositBasicoSimple ######"
				,"\n###### smap1      = " , smap1
				,"\n###### parametros = " , parametros
				,"\n###### flujo      = " , flujo
				));
		
		try {
			UserVO usuario=(UserVO)session.get("USUARIO");
			/*
			 * pv_cdunieco_i
			 * pv_cdramo_i
			 * pv_estado_i
			 * pv_nmpoliza_i
			 * pv_fecha_i
			 * pv_cdelemen_i
			 * pv_cdusuari_i
			 * pv_proceso_i
			 * pv_cdtipsup_i
			 */
			String cdunieco     = smap1.get("cdunieco");
			String cdramo       = smap1.get("cdramo");
			String cdtipsit     = smap1.get("cdtipsit");
			String estado       = smap1.get("estado");
			String nmpoliza     = smap1.get("nmpoliza");
			String fechaEndoso  = smap1.get("fecha_endoso");
			Date   dFechaEndoso = renderFechas.parse(fechaEndoso);
			String cdtipsup     = TipoEndoso.CORRECCION_DATOS_ASEGURADOS.getCdTipSup().toString();
			String cdelemento   = usuario.getEmpresa().getElementoId();
			String cdusuari     = usuario.getUser();
			String cdsisrol     = usuario.getRolActivo().getClave();
			

			Map<String,String>paramsIniciarEndoso=new HashMap<String,String>(0);
			paramsIniciarEndoso.put("pv_cdunieco_i" , cdunieco);
			paramsIniciarEndoso.put("pv_cdramo_i"   , cdramo);
			paramsIniciarEndoso.put("pv_estado_i"   , estado);
			paramsIniciarEndoso.put("pv_nmpoliza_i" , nmpoliza);
			paramsIniciarEndoso.put("pv_fecha_i"    , fechaEndoso);
			paramsIniciarEndoso.put("pv_cdelemen_i" , cdelemento);
			paramsIniciarEndoso.put("pv_cdusuari_i" , cdusuari);
			paramsIniciarEndoso.put("pv_proceso_i"  , "END");
			paramsIniciarEndoso.put("pv_cdtipsup_i" , cdtipsup);
			Map<String,String>respuestaIniciarEndoso=endososManager.iniciarEndoso(paramsIniciarEndoso);
			
			String nmsuplem=respuestaIniciarEndoso.get("pv_nmsuplem_o");
			String nsuplogi=respuestaIniciarEndoso.get("pv_nsuplogi_o");
			
			//cargar anterior valosit
			Map<String,String>paramsValositAsegurado=new LinkedHashMap<String,String>(0);
			paramsValositAsegurado.put("pv_cdunieco_i", cdunieco);
			paramsValositAsegurado.put("pv_cdramo_i",   cdramo);
			paramsValositAsegurado.put("pv_estado_i",   estado);
			paramsValositAsegurado.put("pv_nmpoliza_i", nmpoliza);
			paramsValositAsegurado.put("pv_nmsituac_i", smap1.get("nmsituac"));
			Map<String,Object>valositAsegurado=kernelManager.obtieneValositSituac(paramsValositAsegurado);
			logger.debug("valosit anterior: "+valositAsegurado);
			
			//poner pv_ al anterior
			Map<String,Object>valositAseguradoIterado=new LinkedHashMap<String,Object>(0);
			Iterator it=valositAsegurado.entrySet().iterator();
			while(it.hasNext()) {
				Entry en=(Entry)it.next();
				valositAseguradoIterado.put("pv_"+(String)en.getKey(),en.getValue());//agregar pv_ a los anteriores
			}
			valositAsegurado=valositAseguradoIterado;
			logger.debug("se puso pv_ en el anterior");
			
			//agregar los del form al leido
			Iterator it2=parametros.entrySet().iterator();
			while(it2.hasNext()) {
				Entry en=(Entry)it2.next();
				valositAsegurado.put((String)en.getKey(),en.getValue());//tienen pv_ los del form
				//ya agregamos todos los nuevos en el mapa
			}
			logger.debug("se agregaron los nuevos");
			
			//convertir a string el total
			Map<String,String>paramsNuevos=new LinkedHashMap<String,String>(0);
			it=valositAsegurado.entrySet().iterator();
			while(it.hasNext()) {
				Entry en=(Entry)it.next();
				paramsNuevos.put((String)en.getKey(),(String)en.getValue());
			}
			logger.debug("se pasaron a string");
			
			/*
			pv_cdunieco
    		pv_cdramo
    		pv_estado
    		pv_nmpoliza
    		pv_nmsituac
    		pv_nmsuplem
    		pv_status
    		pv_cdtipsit
    		...pv_otvalor[01-50]
    		pv_accion_i
			*/
			paramsNuevos.put("pv_cdunieco" , cdunieco);
			paramsNuevos.put("pv_cdramo"   , cdramo);
			paramsNuevos.put("pv_estado"   , estado);
			paramsNuevos.put("pv_nmpoliza" , nmpoliza);
			paramsNuevos.put("pv_nmsituac" , smap1.get("nmsituac"));
			paramsNuevos.put("pv_nmsuplem" , nmsuplem);
			paramsNuevos.put("pv_status"   , Constantes.STATUS_VIVO);
			paramsNuevos.put("pv_cdtipsit" , cdtipsit);
			paramsNuevos.put("pv_accion_i" , Constantes.INSERT_MODE);
			logger.debug("los actualizados seran: "+paramsNuevos);
			kernelManager.insertaValoresSituaciones(paramsNuevos);
            
			//////////////////////
			////// cdperson //////
			Map<String,String>mapCdperson=new HashMap<String,String>(0);
			mapCdperson.put("pv_cdunieco" , cdunieco);
			mapCdperson.put("pv_cdramo"   , cdramo);
			mapCdperson.put("pv_estado"   , estado);
			mapCdperson.put("pv_nmpoliza" , nmpoliza);
			mapCdperson.put("pv_nmsituac" , smap1.get("nmsituac"));
			List<Map<String,String>>listCdperson=endososManager.obtenerCdpersonMpoliper(mapCdperson);
			////// cdperson //////
			//////////////////////
			
            //////////////////////
			////// mpoliper //////
			Map<String,Object>mapaMpoliper=new LinkedHashMap<String,Object>(0);
			mapaMpoliper.put("pv_cdunieco_i" , cdunieco);
			mapaMpoliper.put("pv_cdramo_i"   , cdramo);
			mapaMpoliper.put("pv_estado_i"   , estado);
			mapaMpoliper.put("pv_nmpoliza_i" , nmpoliza);
			mapaMpoliper.put("pv_nmsituac_i" , smap1.get("nmsituac"));
			mapaMpoliper.put("pv_cdrol_i"    , "2");
			mapaMpoliper.put("pv_cdperson_i" , listCdperson.get(0).get("CDPERSON"));
			mapaMpoliper.put("pv_nmsuplem_i" , nmsuplem);
			mapaMpoliper.put("pv_status_i"   , Constantes.STATUS_VIVO);
			mapaMpoliper.put("pv_nmorddom_i" , "1");
			mapaMpoliper.put("pv_swreclam_i" , null);
			mapaMpoliper.put("pv_accion_i"   , Constantes.INSERT_MODE);
			mapaMpoliper.put("pv_swexiper_i" , Constantes.SI);
			kernelManager.movMpoliper(mapaMpoliper);
			////// mpoliper //////
			//////////////////////
			
			// Se confirma el endoso si cumple la validacion de fechas: 
			RespuestaConfirmacionEndosoVO respConfirmacionEndoso = this.confirmarEndoso(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplem
					,nsuplogi
					,cdtipsup
					,""
					,dFechaEndoso
					,cdtipsit
					,flujo
					,cdusuari
					,cdsisrol
					);
			
			// Si el endoso fue confirmado:
			if(respConfirmacionEndoso.isConfirmado()) {
				endosoConfirmado = true;
				
				// Regeneramos los documentos:
				this.regeneraDocumentos(cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdtipsup, null,cdusuari);
				
				String saludDanios = "S";
				ClienteGeneral clienteGeneral = new ClienteGeneral();
				clienteGeneral.setClaveCia(saludDanios);
				
				// Ejecutamos el Web Service de Cliente Salud:
				ice2sigsService.ejecutaWSclienteGeneral(cdunieco, cdramo, estado, nmpoliza, nmsuplem, respConfirmacionEndoso.getNumeroTramite(), null, Ice2sigsService.Operacion.ACTUALIZA, clienteGeneral, (UserVO) session.get("USUARIO"), false);
				
				mensaje = "Endoso generado";
				if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
                }
			} else {
				mensaje = new StringBuilder().append("El endoso ").append(nsuplogi)
						.append(" se guard\u00f3 en mesa de control para autorizaci\u00f3n ")
						.append("con n\u00famero de tr\u00e1mite ").append(respConfirmacionEndoso.getNumeroTramite()).toString();
				if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
                }
			}
			success=true;
			
		} catch(Exception ex) {
			logger.error("error al guardar endosos de valosit basico simple", ex);
			success = false;
			error = ex.getMessage();
		}
		
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### error   = " , error
				,"\n###### mensaje = " , mensaje
				,"\n###### guardarEndosoValositBasicoSimple ######"
				,"\n##############################################"
				));
		
		return SUCCESS;
	}
	/*///////////////////////////////////////*/
	////// guardar endoso valosit basico //////
	///////////////////////////////////////////

	/////////////////////////////////
	////// editor de pantallas //////
	/*/////////////////////////////*/
	public String editorPantallas()
	{
		logger.debug(""
				+ "\n#############################"
				+ "\n#############################"
				+ "\n###### editorPantallas ######"
				+ "\n######                 ######"
				);
		String result = ERROR;
		try
		{
			UserVO usuario = Utils.validateSession(session);
			if (!"ICE".equals(usuario.getUser()) || !RolSistema.PARAMETRIZADOR_SISTEMAS.getCdsisrol().equals(usuario.getRolActivo().getClave())) {
				throw new ApplicationException("Usuario sin permisos");
			}
			GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			
			imap1=new LinkedHashMap<String,Item>(0);
			
			List<ComponenteVO>tatriFiltro=pantallasManager.obtenerComponentes(
					null, null, null, null, null, null, "EDITORPANTALLAS", "FILTRO", null);
			gc.generaParcial(tatriFiltro);
			imap1.put("itemsFiltro"  , gc.getItems());
			imap1.put("fieldsFiltro" , gc.getFields());
			
			List<ComponenteVO>tatriModelo=pantallasManager.obtenerComponentes(
					null, null, null, null, null, null,"EDITORPANTALLAS", "MODELO",null);
			gc.generaParcial(tatriModelo);
			imap1.put("itemsModelo"   , gc.getItems());
			imap1.put("fieldsModelo"  , gc.getFields());
			imap1.put("columnsModelo" , gc.getColumns());
			
			imap1.put("storeArbol",pantallasManager.obtenerArbol());
			result = SUCCESS;
		}
		catch(Exception ex)
		{
			logger.error("error al cargar el editor de pantallas",ex);
			message = Utils.manejaExcepcion(ex);
		}
		logger.debug(""
				+ "\n######                 ######"
				+ "\n###### editorPantallas ######"
				+ "\n#############################"
				+ "\n#############################"
				);
		return result;
	}
	/*/////////////////////////////*/
	////// editor de pantallas //////
	/////////////////////////////////
	
	////////////////////////////////
	////// visor de pantallas //////
	/*////////////////////////////*/
	public String visorPantallas()
	{
		logger.debug(""
				+ "\n############################"
				+ "\n############################"
				+ "\n###### visorPantallas ######"
				+ "\n######                ######"
				);
		logger.debug("smap1: "+smap1);
		try
		{
			List<ComponenteVO>lt=pantallasManager.obtenerComponentes(
					smap1.get("cdtiptra")
					,smap1.get("cdunieco")
					,smap1.get("cdramo")
					,smap1.get("cdtipsit")
					,smap1.get("estado")
					,smap1.get("rol")
					,smap1.get("pantalla")
					,smap1.get("seccion")
					,smap1.get("orden"));
			
			GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			gc.generaParcial(lt);
			item1=gc.getFields();
			item2=gc.getItems();
			item3=gc.getColumns();
		}
		catch(Exception ex)
		{
			logger.error("error al visualizar pantalla",ex);
		}
		logger.debug(""
				+ "\n######                ######"
				+ "\n###### visorPantallas ######"
				+ "\n############################"
				+ "\n############################"
				);
		return SUCCESS;
	}
	/*////////////////////////////*/
	////// visor de pantallas //////
	////////////////////////////////
	
	////////////////////////////////////////////
	////// obtener parametros de pantalla //////
	/*////////////////////////////////////////*/
	public String obtenerParametrosPantalla()
	{
		logger.debug(""
				+ "\n#######################################"
				+ "\n#######################################"
				+ "\n###### obtenerParametrosPantalla ######"
				+ "\n######                           ######"
				);
		logger.debug("smap1: "+smap1);
		try
		{
			slist1=pantallasManager.obtenerParametros(
					smap1.get("cdtiptra")
					,smap1.get("cdunieco")
					,smap1.get("cdramo")
					,smap1.get("cdtipsit")
					,smap1.get("estado")
					,smap1.get("rol")
					,smap1.get("pantalla")
					,smap1.get("seccion")
					,smap1.get("orden")
					);
			success=true;
		}
		catch(Exception ex)
		{
			logger.error("error al obtener parametros pantalla",ex);
			success=false;
		}
		logger.debug(""
				+ "\n######                           ######"
				+ "\n###### obtenerParametrosPantalla ######"
				+ "\n#######################################"
				+ "\n#######################################"
				);
		return SUCCESS;
	}
	/*////////////////////////////////////////*/
	////// obtener parametros de pantalla //////
	////////////////////////////////////////////
	
	////////////////////////////////////////////
	////// guardar parametros de pantalla //////
	/*////////////////////////////////////////*/
	public String guardarParametrosPantalla()
	{
		logger.debug(""
				+ "\n#######################################"
				+ "\n#######################################"
				+ "\n###### guardarParametrosPantalla ######"
				+ "\n######                           ######"
				);
		logger.debug("smap1: "+smap1);
		logger.debug("slist1: "+slist1);
		
		String idpro  = System.currentTimeMillis() + "";
		String accion = Constantes.DELETE_MODE;
		
		try
		{
			if(slist1!=null)
			{	
				for(Map<String,String>nuevo:slist1)
				{
					Map<String,String>mapaConPVI=new HashMap<String,String>();
					for(Entry<String,String>en:nuevo.entrySet())
					{
						mapaConPVI.put("PV_"+en.getKey()+"_I",en.getValue());
					}
					mapaConPVI.put("PV_IDPRO_I",idpro);
					pantallasManager.insertarParametros(mapaConPVI);
				}
				
				accion = Constantes.INSERT_MODE;
			}
			success=true;
		}
		catch(Exception ex)
		{
			accion = Constantes.DELETE_MODE;
			logger.error("error al guardar parametros pantalla",ex);
			success=false;
		}
		
		try
		{
			pantallasManager.movParametros(
					smap1.get("cdtiptra")
					,smap1.get("cdunieco")
					,smap1.get("cdramo")
					,smap1.get("cdtipsit")
					,smap1.get("estado")
					,smap1.get("rol")
					,smap1.get("pantalla")
					,smap1.get("seccion")
					,smap1.get("orden")
					,accion
					,idpro);
		}
		catch(Exception ex)
		{
			logger.error("error al guardar parametros pantalla",ex);
		}
		logger.debug(""
				+ "\n######                           ######"
				+ "\n###### guardarParametrosPantalla ######"
				+ "\n#######################################"
				+ "\n#######################################"
				);
		return SUCCESS;
	}
	/*////////////////////////////////////////*/
	////// guardar parametros de pantalla //////
	////////////////////////////////////////////
	
	//////////////////////////////////////////////
	////// pantalla de endosos de clausulas //////
	/*
	smap1.cdunieco:1006
	smap1.cdramo:2
	smap1.estado:M
	smap1.nmpoliza:18
	smap1.cdtipsit:SL
	smap1.nmsituac:2
	smap1.ntramite:662
	smap1.nmsuplem:245668011510000012
	*/
	/*//////////////////////////////////////////*/
	public String pantallaEndosoClausulas() {
		logger.debug(Utils.log(
				 "\n##############################################"
				,"\n###### pantalla de endosos de clausulas ######"
				,"\n###### smap1 = ", smap1
				,"\n###### icisos slits1 = ", slist1
				));
		
		RespuestaVO resp = null;
		
		try
		{
			boolean validaInciso = true;
			if( (slist1 == null || slist1.isEmpty()) && smap1.containsKey("pantallaOrigen") && "MARCO_ENDOSOS_GENERAL".equals(smap1.get("pantallaOrigen")) && StringUtils.isBlank(smap1.get("nmsituac")) ){
				logger.debug(" <<<<<<<>>>>>>> Sin incisos... Entrando a endoso exclusion o textos NIVEL POLIZA <<<<<<<>>>>>>>");
				validaInciso = false;
				smap1.put("nmsituac", "0");
				
				String cdtipsit1 = null;
				try {
					cdtipsit1 = endososManager.recuperarCdtipsitInciso1(smap1.get("CDUNIECO"), smap1.get("CDRAMO"),
									smap1.get("ESTADO"), smap1.get("NMPOLIZA"));
				} catch (Exception e) {
					logger.error("Error al obtener el cdtipsit de la poliza para el primer inciso",e);
				}
				smap1.put("cdtipsit", cdtipsit1);
			}
			
			transformaEntrada(smap1, slist1, validaInciso );
			
			// Valida si hay un endoso anterior pendiente:
			resp = endososManager.validaEndosoAnterior(smap1.get("cdunieco"), smap1.get("cdramo"),
					smap1.get("estado"), smap1.get("nmpoliza"),
					TipoEndoso.CAMBIO_ENDOSOS_EXCLUSION_O_TEXTOS.getCdTipSup().toString());
			error = resp.getMensaje();
			
			try
			{
				List<ComponenteVO>autocompleterICD=pantallasManager.obtenerComponentes(
						TipoTramite.POLIZA_NUEVA.getCdtiptra(), null, null
						, null, null, null
						, "PANTALLA_EXCLUSION", "COMBO_ICD", null);
				
				GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
				
				gc.generaComponentes(autocompleterICD, true, false, true, false, false, false);
				
				item1=gc.getItems();
			}
			catch(Exception ex)
			{
				long timestamp = System.currentTimeMillis();
				logger.error(new StringBuilder("Error al obtener combo de icd #").append(timestamp).toString(),ex);
			}
		}
		catch(Exception ex)
		{
			logger.error("Error al construir pantalla de endoso de clausulas",ex);
			error = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(new StringBuilder()
		        .append("\n######                                  ######")
		        .append("\n###### pantalla de endosos de clausulas ######")
		        .append("\n##############################################")
		        .append("\n##############################################").toString());
		
		return resp!=null&&resp.isSuccess() ? SUCCESS : ERROR;
	}
	/*//////////////////////////////////////////*/
	////// pantalla de endosos de clausulas //////
	//////////////////////////////////////////////
	
	///////////////////////////////////////////////////////////////
	////// pantalla de endoso de alta y/o baja de asegurados //////
	/*
	smap1:
		NMSUPLEM=245665412050000000,
		DSTIPSIT=SALUD VITAL,
		FEINIVAL=27/12/2013,
		NMPOLIZA=1,
		PRIMA_TOTAL=10830.45,
		NMPOLIEX=1904213000001000000,
		NSUPLOGI=0,
		DSCOMENT=EMISIÒN DE LA POLIZA,
		ESTADO=M,
		CDTIPSIT=SL,
		NTRAMITE=396,
		CDUNIECO=1904,
		FEEMISIO=27/12/2013,
		CDRAMO=2
	smap2
		alta=si
	*/
	/*///////////////////////////////////////////////////////////*/
	public String pantallaEndosoAltaBajaAsegurado() {
		this.session=ActionContext.getContext().getSession();
		logger.debug(new StringBuilder("\n")
		        .append("\n###############################################################")
		        .append("\n###############################################################")
		        .append("\n###### pantalla de endoso de alta y/o baja de asegurados ######")
		        .append("\n######                                                   ######").toString());
		logger.debug(new StringBuilder("smap1: ").append(smap1).toString());
		
		RespuestaVO resp = null;
		
		try
		{
			transformaEntrada(smap1, slist1, false);
		
			// Valida si hay un endoso anterior pendiente:
			resp = endososManager.validaEndosoAnterior(smap1.get("CDUNIECO"), smap1.get("CDRAMO"),
					smap1.get("ESTADO"), smap1.get("NMPOLIZA"),
					smap2.get("alta").equalsIgnoreCase("si")?
							TipoEndoso.ALTA_ASEGURADOS.getCdTipSup().toString():
							TipoEndoso.BAJA_ASEGURADOS.getCdTipSup().toString());
			error = resp.getMensaje();
			
			if(resp.isSuccess()) {
				try {
					String cdtipsit=smap1.get("CDTIPSIT");
					
					imap1=new HashMap<String,Item>();
					
					GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
					
					gc.generaParcial(pantallasManager.obtenerComponentes(
							null, null, null,
							null, null, null,
							"ENDOSOABASEGU", "MODELO", null));
					
					imap1.put("modelo"   , gc.getFields());
					imap1.put("columnas" , gc.getColumns());
					
					gc.generaParcial(pantallasManager.obtenerComponentes(
							null, null, null,
							null, null, null,
							"ENDOSOABASEGU", "PANELLECTURA", null));
					
					imap1.put("panelLectura" , gc.getItems());
					
					////////////////////////////////////////////////
					////// campos de tatrisit para individual //////
					String cdusuari;
					{
						UserVO usuario=(UserVO)session.get("USUARIO");
						cdusuari=usuario.getUser();
					}
					List<ComponenteVO>tatrisit=kernelManager.obtenerTatrisit(cdtipsit,cdusuari);
					gc.setCdtipsit(cdtipsit);
					
					List<String>exclusiones=new ArrayList<String>();
					
					List<ComponenteVO>tatriTemp=new ArrayList<ComponenteVO>(0);
					for(ComponenteVO t:tatrisit)
					//solo dejar los atributos si es individual, los que tengan S
					{
						if(t.getSwsuscri().equalsIgnoreCase("S"))//S=individual
						{
							String name=t.getNameCdatribu();
							logger.debug("se busca "+name+" en excluciones");
							if(!exclusiones.contains(name))
							{
								logger.debug("no encontrado");
								tatriTemp.add(t);
							}
						}
					}
					tatrisit=tatriTemp;
					
					tatriTemp=pantallasManager.obtenerComponentes(
							null, null, null,
							null, null, null,
							"ENDOSOABASEGU", "FORMULARIO", null);
					
					tatriTemp.addAll(tatrisit);
					tatrisit=tatriTemp;
					
					gc.generaParcial(tatrisit);
					
					imap1.put("formulario" , gc.getItems());
					
					Map<String,String>paramsTatriper=new HashMap<String,String>();
					paramsTatriper.put("pv_cdramo_i"   , smap1.get("CDRAMO"));
					paramsTatriper.put("pv_cdrol_i"    , "2"/*ASEGURADO*/);
					paramsTatriper.put("pv_cdtipsit_i" , smap1.get("CDTIPSIT"));
					paramsTatriper.put("pv_cdperson_i" , "0"/*ATRIBUTOS BASICOS SIN TOMAR CUMULO DE ALGUNA PERSONA EXISTENTE*/);
					List<ComponenteVO>tatriper=kernelManager.obtenerTatriper(paramsTatriper);
					GeneradorCampos gc2=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
					gc2.setCdramo(smap1.get("CDRAMO"));
					gc2.setCdtipsit(smap1.get("CDTIPSIT"));
					gc2.setCdrol("2"/*ASEGURADO*/);
					gc2.setAuxiliar(true);
					gc2.generaParcial(tatriper);
					imap1.put("formularioAtriper" , gc2.getItems());
					////// campos de tatrisit para individual //////
					////////////////////////////////////////////////
				} catch(Exception ex) {
					logger.error("Error al mostrar pantalla de alta y/o baja de asegurados", ex);
				}
			}
		}
		catch(Exception ex)
		{
			logger.error("Error al constuir pantalla de endoso de alta/baja de inciso/asegurado(s)",ex);
			error = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(new StringBuilder("\n")
		        .append("\n###### pantalla de endoso de alta y/o baja de asegurados ######")
		        .append("\n######                                                   ######")
		        .append("\n###############################################################")
		        .append("\n###############################################################").toString());
		
		return resp!=null&&resp.isSuccess() ? SUCCESS : ERROR;
	}
	/*///////////////////////////////////////////////////////////*/
	////// pantalla de endoso de alta y/o baja de asegurados //////
	///////////////////////////////////////////////////////////////
	
	/////////////////////////////////////////////////////////
	////// guardar endoso de alta o baja de asegurados //////
	/*
	smap1: (cuando es por baja)
	    cdrfc,
	    cdperson,
	    fenacimi,
        sexo=H,
        swexiper,
        Apellido_Materno,
        nacional,
        nombre,
        nmsituac,
        cdrol,
        segundo_nombre,
        Parentesco=T,
        tpersona=F,
        Apellido_Paterno
    smap1: (cuando es por alta)
        amat,
        nmsituac,
        nombre,
        nombre2,
        tpersona,
        rfc,
        apat,
        cdnacion,
        parametros.pv_otvalor01, //generico
        parametros.pv_otvalor02, //generico
        parametros.pv_otvalor16, //generico
        parametros.pv_otvalor18, //generico
        parametros.pv_otvalor19  //generico
    smap2:
        NMSUPLEM,
        DSTIPSIT,
        FEINIVAL,
        NMPOLIZA,
        PRIMA_TOTAL,
        NMPOLIEX,
        NSUPLOGI,
        DSCOMENT,
        ESTADO,
        CDTIPSIT,
        NTRAMITE,
        FEEMISIO,
        CDRAMO,
        CDUNIECO
    slist1:[
        {dsclausu=ENDOSO LIBRE, linea_usuario=TEXTO LIBREasd, linea_general=, cdclausu=END215, cdtipcla=}
        ]
	*/
	/*/////////////////////////////////////////////////////*/
	public String guardarEndosoAltaBajaAsegurado()
	{
		this.session=ActionContext.getContext().getSession();
		
		logger.debug(Utils.log(
				 "\n############################################"
				,"\n###### guardarEndosoAltaBajaAsegurado ######"
				,"\n###### smap1  = " , smap1
				,"\n###### smap2  = " , smap2
				,"\n###### smap3  = " , smap3
				,"\n###### slist1 = " , slist1
				,"\n###### flujo  = " , flujo
				));
		
		try
		{
			UserVO usuario=(UserVO) session.get("USUARIO");
			
			String usuarioCaptura =  null;
			
			if(usuario!=null){
				if(StringUtils.isNotBlank(usuario.getClaveUsuarioCaptura())){
					usuarioCaptura = usuario.getClaveUsuarioCaptura();
				}else{
					usuarioCaptura = usuario.getCodigoPersona();
				}
				
			}
			
			boolean alta=smap1.containsKey("apat");
			
			String cdunieco = smap2.get("CDUNIECO");
			String cdramo   = smap2.get("CDRAMO");
			String estado   = smap2.get("ESTADO");
			String nmpoliza = smap2.get("NMPOLIZA");
			String cdtipsit = smap2.get("CDTIPSIT");
			String nmsituac = smap1.get("nmsituac");
			String rfc      = smap1.get("rfc");
			String nombre   = smap1.get("nombre");
			String nombre2  = smap1.get("nombre2");
			String tpersona = smap1.get("tpersona");
			String apat     = smap1.get("apat");
			String amat     = smap1.get("amat");
			String cdnacion = smap1.get("cdnacion");
			String cdelemen = usuario.getEmpresa().getElementoId();
			String cdusuari = usuario.getUser();
			Date   fechaHoy = new Date();
			String ntramite = smap2.get("NTRAMITE");
			String cdsisrol = usuario.getRolActivo().getClave();
			
			String fechaEndoso    = smap3.get("fecha_endoso");
			Date   fechaEndosoD   = renderFechas.parse(fechaEndoso);
			
			String ocupacion = smap1.get("dsocupacion");
			
			/*
			 * Parche para validar que PREVEX tenga 49 y 50 de tvaloper
			 */
			if(alta)
			{
				LinkedHashMap<String,Object>paramsValues=new LinkedHashMap<String,Object>();
				paramsValues.put("param1",cdunieco);
				paramsValues.put("param2",cdramo);
				paramsValues.put("param3",estado);
				paramsValues.put("param4",nmpoliza);
				Map<String,String>agente=storedProceduresManager.procedureMapCall(
						ObjetoBD.OBTIENE_CDAGENTE_POLIZA.getNombre(), paramsValues, null);
				if(agente.get("CDAGENTE").equals("9759"))
				{
					if(StringUtils.isBlank(smap1.get("aux.otvalor49"))
							||StringUtils.isBlank(smap1.get("aux.otvalor50")))
					{
						throw new Exception("Falta complementar c\u00f3digo de cliente externo y clave familiar");
					}
				}
			}
			
			DatosUsuario datUsu   = kernelManager.obtenerDatosUsuario(cdusuari,cdtipsit);//cdperson
			String cdpersonSesion = datUsu.getCdperson();
			
			Map<String,String>paramsIniciarEndoso=new HashMap<String,String>(0);
			paramsIniciarEndoso.put("pv_cdunieco_i" , cdunieco);
			paramsIniciarEndoso.put("pv_cdramo_i"   , cdramo);
			paramsIniciarEndoso.put("pv_estado_i"   , estado);
			paramsIniciarEndoso.put("pv_nmpoliza_i" , nmpoliza);
			paramsIniciarEndoso.put("pv_fecha_i"    , fechaEndoso);
			paramsIniciarEndoso.put("pv_cdelemen_i" , cdelemen);
			paramsIniciarEndoso.put("pv_cdusuari_i" , cdusuari);
			paramsIniciarEndoso.put("pv_proceso_i"  , "END");
			paramsIniciarEndoso.put("pv_cdtipsup_i", alta 
					? TipoEndoso.ALTA_ASEGURADOS.getCdTipSup().toString()
					: TipoEndoso.BAJA_ASEGURADOS.getCdTipSup().toString());
			Map<String,String>respuestaIniciarEndoso=endososManager.iniciarEndoso(paramsIniciarEndoso);
			
			String nmsuplem=respuestaIniciarEndoso.get("pv_nmsuplem_o");
			String nsuplogi=respuestaIniciarEndoso.get("pv_nsuplogi_o");
			
			Map<String,String>paramsObtenerDatosMpolisit=new HashMap<String,String>();
			paramsObtenerDatosMpolisit.put("pv_cdunieco_i" , cdunieco);
			paramsObtenerDatosMpolisit.put("pv_cdramo_i"   , cdramo);
			paramsObtenerDatosMpolisit.put("pv_estado_i"   , estado);
			paramsObtenerDatosMpolisit.put("pv_nmpoliza_i" , nmpoliza);
			Map<String,String>respuestaObtenerDatosMpolisit=endososManager.obtieneDatosMpolisit(paramsObtenerDatosMpolisit);
			String nmsituacNuevo=respuestaObtenerDatosMpolisit.get("pv_nmsituac_o");
			String cdplan=respuestaObtenerDatosMpolisit.get("pv_cdplan_o");
			
			if(alta)
			{
				nmsituac=nmsituacNuevo;
				
				//////////////////////
        		////// cdperson //////
				String cdperson=kernelManager.generaCdperson();
				////// cdperson //////
				//////////////////////
				
				/////////////////////
				////// polisit //////
				Map<String,Object>mapaPolisit=new HashMap<String,Object>(0);
                mapaPolisit.put("pv_cdunieco_i",    cdunieco);
                mapaPolisit.put("pv_cdramo_i",      cdramo);
                mapaPolisit.put("pv_estado_i",      estado);
                mapaPolisit.put("pv_nmpoliza_i",    nmpoliza);
                mapaPolisit.put("pv_nmsituac_i",    nmsituac);
                mapaPolisit.put("pv_nmsuplem_i",    nmsuplem);
                mapaPolisit.put("pv_status_i",      "V");
                mapaPolisit.put("pv_cdtipsit_i",    cdtipsit);
                mapaPolisit.put("pv_swreduci_i",    null);
                mapaPolisit.put("pv_cdagrupa_i",    "1");
                mapaPolisit.put("pv_cdestado_i",    "0");
                mapaPolisit.put("pv_fefecsit_i",    fechaEndosoD);
                mapaPolisit.put("pv_fecharef_i",    fechaEndosoD);
                mapaPolisit.put("pv_cdgrupo_i",     null);
                mapaPolisit.put("pv_nmsituaext_i",  null);
                mapaPolisit.put("pv_nmsitaux_i",    null);
                mapaPolisit.put("pv_nmsbsitext_i",  null);
                mapaPolisit.put("pv_cdplan_i",      cdplan);
                mapaPolisit.put("pv_cdasegur_i",    "30");
                mapaPolisit.put("pv_accion_i",      "I");
                kernelManager.insertaPolisit(mapaPolisit);
				////// polisit //////
                /////////////////////
                
                /////////////////////
                ////// valosit //////                
                
                ////// 1. mapa valosit base //////
                Map<String,String>mapaValosit=new HashMap<String,String>(0);
                mapaValosit.put("pv_cdunieco",    cdunieco);
                mapaValosit.put("pv_cdramo",      cdramo);
                mapaValosit.put("pv_estado",      estado);
                mapaValosit.put("pv_nmpoliza",    nmpoliza);
                mapaValosit.put("pv_nmsituac",    nmsituac);
                mapaValosit.put("pv_nmsuplem",    nmsuplem);
                mapaValosit.put("pv_status",      "V");
                mapaValosit.put("pv_cdtipsit",    cdtipsit);
                mapaValosit.put("pv_accion_i",   "I");
                ////// 1. mapa valosit base //////
                
                ////// 2. obtener el titular //////
                Map<String,String>mapaObtenerValosit=new HashMap<String,String>(0);
                mapaObtenerValosit.put("pv_cdunieco_i" , cdunieco);
                mapaObtenerValosit.put("pv_nmpoliza_i" , nmpoliza);
                mapaObtenerValosit.put("pv_cdramo_i"   , cdramo);
                mapaObtenerValosit.put("pv_estado_i"   , estado);
                mapaObtenerValosit.put("pv_nmsituac_i" , "1");
                Map<String,Object>valositTitular=kernelManager.obtieneValositSituac(mapaObtenerValosit);
                ////// 2. obtener el titular //////
                
                ////// 3. copiar los otvalor del titular a la base //////
                for(Entry<String,Object> en:valositTitular.entrySet())
                {
                	String key=en.getKey();
                	if(key.substring(0,3).equalsIgnoreCase("otv"))
                	{
                		mapaValosit.put("pv_"+key,(String)en.getValue());
                	}
                }
                ////// 3. copiar los otvalor del titular a la base //////
                
                ////// 4. sustituir los otvalor por los nuevos del form //////
                for(Entry<String,String> en:smap1.entrySet())
                {
                	String key=en.getKey();
                	if(key.substring(0,3).equalsIgnoreCase("par"))
                	{
                		// p a r a m e t r o s . pv_otvalorXX
                		//0 1 2 3 4 5 6 7 8 9 0 1
                		mapaValosit.put(key.substring(11),en.getValue());
                	}
                }
                ////// 4. sustituir los otvalor por los nuevos del form //////
                
                kernelManager.insertaValoresSituaciones(mapaValosit);
                
                ////// valosit //////
                /////////////////////
                
                //atributos
            	Map<String,String>atributos=consultasManager.cargarAtributosBaseCotizacion(cdtipsit);
            	
            	String llaveSexo = null;
            	if(atributos.get("SEXO") != null) {
            		llaveSexo=StringUtils.leftPad(atributos.get("SEXO"),2,"0");
                	llaveSexo="parametros.pv_otvalor"+llaveSexo;
            	}
            	String sexo = smap1.get(llaveSexo);

    			
            	String llaveFenacimi=null;
            	if(atributos.get("FENACIMI") != null) {
            		llaveFenacimi=StringUtils.leftPad(atributos.get("FENACIMI"),2,"0");
                	llaveFenacimi="parametros.pv_otvalor"+llaveFenacimi;
            	}
            	String fenacimi = smap1.get(llaveFenacimi);
                //atributos
                
                //////////////////////
                ////// mpersona //////
                Map<String,Object> mapaMpersona=new LinkedHashMap<String,Object>(0);
				mapaMpersona.put("pv_cdperson_i"    , cdperson); 
				mapaMpersona.put("pv_cdtipide_i"    , "1");
				mapaMpersona.put("pv_cdideper_i"    , rfc);
				mapaMpersona.put("pv_dsnombre_i"    , nombre);
				mapaMpersona.put("pv_cdtipper_i"    , "1");
				mapaMpersona.put("pv_otfisjur_i"    , tpersona);
				mapaMpersona.put("pv_otsexo_i"      , sexo);
				mapaMpersona.put("pv_fenacimi_i"    , renderFechas.parse(fenacimi));
				mapaMpersona.put("pv_cdrfc_i"       , rfc);
				mapaMpersona.put("pv_dsemail_i"     , "");
				mapaMpersona.put("pv_dsnombre1_i"   , nombre2);
				mapaMpersona.put("pv_dsapellido_i"  , apat);
				mapaMpersona.put("pv_dsapellido1_i" , amat);
				mapaMpersona.put("pv_feingreso_i"   , fechaHoy);
				mapaMpersona.put("pv_cdnacion_i"    , cdnacion);
				mapaMpersona.put("pv_canaling_i"    , null);
				mapaMpersona.put("pv_conducto_i"    , null);
				mapaMpersona.put("pv_ptcumupr_i"    , null);
				mapaMpersona.put("pv_residencia_i"  , null);
				mapaMpersona.put("pv_nongrata_i"    , null);
				mapaMpersona.put("pv_cdideext_i"    , null);
				mapaMpersona.put("pv_cdestciv_i"    , null);
				mapaMpersona.put("pv_cdsucemi_i"    , null);
				mapaMpersona.put("pv_cdusuario_i"   , usuarioCaptura);
				mapaMpersona.put("pv_dsocupacion_i" , ocupacion);
				mapaMpersona.put("pv_accion_i"      , "I");
				kernelManager.movMpersona(mapaMpersona);
                ////// mpersona //////
                //////////////////////
				
				//////////////////////
				////// mpoliper //////
				Map<String,Object>mapaMpoliper=new LinkedHashMap<String,Object>(0);
				mapaMpoliper.put("pv_cdunieco_i" , cdunieco);
				mapaMpoliper.put("pv_cdramo_i"   , cdramo);
				mapaMpoliper.put("pv_estado_i"   , estado);
				mapaMpoliper.put("pv_nmpoliza_i" , nmpoliza);
				mapaMpoliper.put("pv_nmsituac_i" , nmsituac);
				mapaMpoliper.put("pv_cdrol_i"    , "2");
				mapaMpoliper.put("pv_cdperson_i" , cdperson);
				mapaMpoliper.put("pv_nmsuplem_i" , nmsuplem);
				mapaMpoliper.put("pv_status_i"   , "V");
				mapaMpoliper.put("pv_nmorddom_i" , "1");
				mapaMpoliper.put("pv_swreclam_i" , null);
				mapaMpoliper.put("pv_accion_i"   , "I");
				mapaMpoliper.put("pv_swexiper_i" , Constantes.NO);
				kernelManager.movMpoliper(mapaMpoliper);
				////// mpoliper //////
				//////////////////////
				
				/*
				 * INSERCION DE TVALOPER
				 */
				Map<String,String>tvaloperParams=new HashMap<String,String>();
				tvaloperParams.put("pv_cdunieco" , cdunieco);
				tvaloperParams.put("pv_cdramo"   , cdramo);
				tvaloperParams.put("pv_estado"   , estado);
				tvaloperParams.put("pv_nmpoliza" , nmpoliza);
				tvaloperParams.put("pv_nmsituac" , nmsituac);
				tvaloperParams.put("pv_nmsuplem" , nmsuplem);
				tvaloperParams.put("pv_status"   , "V"/*VIVO*/);
				tvaloperParams.put("pv_cdrol"    , "2"/*ASEGURADO*/);
				tvaloperParams.put("pv_cdperson" , cdperson);
				tvaloperParams.put("pv_cdatribu" , null);
				tvaloperParams.put("pv_cdtipsit" , cdtipsit);
				for(Entry<String,String>en:smap1.entrySet())
				{
					String key=en.getKey();
					if(key.length()>4
							&&key.substring(0, 4).equals("aux."))
					{
						tvaloperParams.put("pv_otvalor"+key.substring("aux.otvalor".length(), key.length()),en.getValue());
					}
				}
				kernelManager.pMovTvaloper(tvaloperParams);
                
				///////////////////////
				////// clausulas //////
				/*///////////////////*/
				for(Map<String,String>cla:slist1)
				{
					//{dsclausu=ENDOSO LIBRE, linea_usuario=TEXTO LIBREasd, linea_general=, cdclausu=END215, cdtipcla=}
					String cdclausu = cla.get("cdclausu");
					String dslinea  = cla.get("linea_usuario");
					String cdtipcla = cla.get("cdtipcla");
					
					endososManager.guardarMpolicot(cdunieco, cdramo, estado, nmpoliza, 
							nmsituac, cdclausu, nmsuplem, Constantes.STATUS_VIVO, cdtipcla, 
							null, dslinea, Constantes.INSERT_MODE);					
				}
				/*///////////////////*/
				////// clausulas //////
				///////////////////////
				
				// Validacion de duplicidad de parentesco:
				try {
					endososManager.validaDuplicidadParentesco(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
				} catch(ApplicationException ae) {
					
					// Si falla se elimina el endoso:
					endososManager.sacaEndoso(cdunieco, cdramo, estado, nmpoliza, nsuplogi, nmsuplem);
					// Se asigna el mensaje de error para el usuario:
					throw new Exception(ae.getMessage());
				}
				
				///////////////////////////////////
				////// validacion extraprima //////
				/*///////////////////////////////*/
				Map<String,String>paramValExtraprima=new LinkedHashMap<String,String>(0);
				paramValExtraprima.put("pv_cdunieco_i" , cdunieco);
				paramValExtraprima.put("pv_cdramo_i"   , cdramo);
				paramValExtraprima.put("pv_estado_i"   , estado);
				paramValExtraprima.put("pv_nmpoliza_i" , nmpoliza);
				paramValExtraprima.put("pv_nmsituac_i" , nmsituac);
				String statusValidacionExtraprimas=(String) kernelManager.validarExtraprimaSituac(paramValExtraprima).getItemMap().get("status");
				logger.debug("tiene status la extraprima: "+statusValidacionExtraprimas);
				if(statusValidacionExtraprimas.equalsIgnoreCase("N"))
				{
					// Se asigna el mensaje de error para el usuario:
					error="Favor de verificar las extraprimas y los endosos de extraprima";
					throw new Exception(error);
				}
				/*///////////////////////////////*/
				////// validacion extraprima //////
				///////////////////////////////////
				
				///////////////////////
				////// domicilio //////
				
				////// 1. obtener asegurados //////
				Map<String,String> mapaObtenerAsegurados=new HashMap<String,String>(0);
				mapaObtenerAsegurados.put("pv_cdunieco" , cdunieco);
				mapaObtenerAsegurados.put("pv_cdramo"   , cdramo);
				mapaObtenerAsegurados.put("pv_estado"   , estado);
				mapaObtenerAsegurados.put("pv_nmpoliza" , nmpoliza);
				mapaObtenerAsegurados.put("pv_nmsuplem" , nmsuplem);
				List<Map<String,Object>>asegurados=kernelManager.obtenerAsegurados(mapaObtenerAsegurados);
				////// 1. obtener asegurados //////
				
				////// 2. obtener cdperson titular //////
				String cdpersonTitular = "";
				for(Map<String,Object>aseguradoIterado:asegurados)
				{
					if(((String)aseguradoIterado.get("nmsituac")).equalsIgnoreCase("0"))
					{
						cdpersonTitular=(String)aseguradoIterado.get("cdperson");
					}
				}
				////// 2. obtener cdperson titular //////
				
				////// 3. obtener el domicilio del titular //////
				Map<String,String> mapaObtenerDomicilio=new HashMap<String,String>(0);
				mapaObtenerDomicilio.put("pv_cdunieco_i" , cdunieco);
				mapaObtenerDomicilio.put("pv_cdramo_i"   , cdramo);
				mapaObtenerDomicilio.put("pv_estado_i"   , estado);
				mapaObtenerDomicilio.put("pv_nmpoliza_i" , nmpoliza);
				mapaObtenerDomicilio.put("pv_nmsituac_i" , nmsituac);
				mapaObtenerDomicilio.put("pv_nmsuplem_i" , nmsuplem);
				mapaObtenerDomicilio.put("pv_cdperson_i" , cdpersonTitular);
				mapaObtenerDomicilio.put("pv_cdtipsit_i" , cdtipsit);
				mapaObtenerDomicilio.put("pv_nmorddom_i" , "1"); // Domicilio 1 default para los asegurados
				
				Map<String,String>domicilioTitular=kernelManager.obtenerDomicilio(mapaObtenerDomicilio);
				////// 3. obtener el domicilio del titular //////
				
				////// 4. mdomicil //////
				Map<String,String>mapaDomicilio=new HashMap<String,String>(0);
				mapaDomicilio.put("pv_cdperson_i" , cdperson);
				mapaDomicilio.put("pv_nmorddom_i" , domicilioTitular.get("NMORDDOM"));
				mapaDomicilio.put("pv_msdomici_i" , domicilioTitular.get("DSDOMICI"));
				mapaDomicilio.put("pv_nmtelefo_i" , domicilioTitular.get("NMTELEFO"));
				mapaDomicilio.put("pv_cdpostal_i" , domicilioTitular.get("CODPOSTAL"));
				mapaDomicilio.put("pv_cdedo_i"    , domicilioTitular.get("CDEDO"));
				mapaDomicilio.put("pv_cdmunici_i" , domicilioTitular.get("CDMUNICI"));
				mapaDomicilio.put("pv_cdcoloni_i" , domicilioTitular.get("CDCOLONI"));
				mapaDomicilio.put("pv_nmnumero_i" , domicilioTitular.get("NMNUMERO"));
				mapaDomicilio.put("pv_nmnumint_i" , domicilioTitular.get("NMNUMINT"));
				mapaDomicilio.put("pv_cdtipdom_i" , "1");
				mapaDomicilio.put("pv_cdusuario_i", usuarioCaptura);
				mapaDomicilio.put("pv_swactivo_i" , Constantes.SI);
				mapaDomicilio.put("pv_accion_i"   , "I");
				kernelManager.pMovMdomicil(mapaDomicilio);
				////// 4. mdomicil //////
				
				////// domicilio //////
				///////////////////////
				
                /////////////////////////////////
                ////// valores por defecto //////
                Map<String,String> mapaCoberturas=new HashMap<String,String>(0);
                mapaCoberturas.put("pv_cdunieco_i",   cdunieco);//se agrega desde el formulario
                mapaCoberturas.put("pv_cdramo_i",     cdramo);//se agrega desde el formulario
                mapaCoberturas.put("pv_estado_i",     estado);
                mapaCoberturas.put("pv_nmpoliza_i",   nmpoliza);
                mapaCoberturas.put("pv_nmsituac_i",   nmsituac);
                mapaCoberturas.put("pv_nmsuplem_i",   nmsuplem);
                mapaCoberturas.put("pv_cdgarant_i",   "TODO");
				mapaCoberturas.put("pv_cdtipsup_i", TipoEndoso.ALTA_ASEGURADOS.getCdTipSup().toString());
                kernelManager.coberturas(mapaCoberturas);
                ////// valores por defecto //////
                /////////////////////////////////
                
                //////////////////////////////
                ////// inserta tworksup //////
                endososManager.movimientoTworksupEnd(
                		cdunieco
                		,cdramo
                		,estado
                		,nmpoliza
                		,TipoEndoso.ALTA_ASEGURADOS.getCdTipSup().toString()
                		,nmsuplem
                		,nmsituac, "I"
                		);
                ////// inserta tworksup //////
                //////////////////////////////
                
                //////////////////////////
                ////// tarificacion //////
    			endososManager.sigsvalipolEnd(
    					cdusuari
    					,cdelemen
    					,cdunieco
    					,cdramo
    					,estado
    					,nmpoliza
    					,nmsituac
    					,nmsuplem
    					,TipoEndoso.ALTA_ASEGURADOS.getCdTipSup().toString()
    					);
                ////// tarificacion //////
    			//////////////////////////
    			
    			//////////////////////////
    			////// valor endoso //////
    			Map<String,Object>mapaValorEndoso=new LinkedHashMap<String,Object>(0);
				mapaValorEndoso.put("pv_cdunieco_i" , cdunieco);
				mapaValorEndoso.put("pv_cdramo_i"   , cdramo);
				mapaValorEndoso.put("pv_estado_i"   , estado);
				mapaValorEndoso.put("pv_nmpoliza_i" , nmpoliza);
				mapaValorEndoso.put("pv_nmsituac_i" , nmsituac);
				mapaValorEndoso.put("pv_nmsuplem_i" , nmsuplem);
				mapaValorEndoso.put("pv_feinival_i" , fechaEndosoD);
				mapaValorEndoso.put("pv_cdtipsup_i" , TipoEndoso.ALTA_ASEGURADOS.getCdTipSup().toString());
				endososManager.calcularValorEndoso(mapaValorEndoso);
    			////// valor endoso //////
    			//////////////////////////
			}
			else
			{
				String cdperson = smap1.get("cdperson");
				
				////////////////////////////
				////// polisit muerto //////
				Map<String,Object>mapaPolisit=new HashMap<String,Object>(0);
                mapaPolisit.put("pv_cdunieco_i",    cdunieco);
                mapaPolisit.put("pv_cdramo_i",      cdramo);
                mapaPolisit.put("pv_estado_i",      estado);
                mapaPolisit.put("pv_nmpoliza_i",    nmpoliza);
                mapaPolisit.put("pv_nmsituac_i",    nmsituac);
                mapaPolisit.put("pv_nmsuplem_i",    nmsuplem);
                mapaPolisit.put("pv_status_i",      "V");
                mapaPolisit.put("pv_cdtipsit_i",    cdtipsit);
                mapaPolisit.put("pv_swreduci_i",    null);
                mapaPolisit.put("pv_cdagrupa_i",    "1");
                mapaPolisit.put("pv_cdestado_i",    "0");
                mapaPolisit.put("pv_fefecsit_i",    fechaEndosoD);
                mapaPolisit.put("pv_fecharef_i",    fechaEndosoD);
                mapaPolisit.put("pv_cdgrupo_i",     null);
                mapaPolisit.put("pv_nmsituaext_i",  null);
                mapaPolisit.put("pv_nmsitaux_i",    null);
                mapaPolisit.put("pv_nmsbsitext_i",  null);
                mapaPolisit.put("pv_cdplan_i",      cdplan);
                mapaPolisit.put("pv_cdasegur_i",    "30");
                mapaPolisit.put("pv_accion_i",      "D");
                kernelManager.insertaPolisit(mapaPolisit);
				////// polisit muerto //////
                ////////////////////////////
                
                ////////////////////////////
                ////// valosit muerto //////                
                
                ////// 1. mapa valosit base //////
                Map<String,String>mapaValosit=new HashMap<String,String>(0);
                mapaValosit.put("pv_cdunieco",    cdunieco);
                mapaValosit.put("pv_cdramo",      cdramo);
                mapaValosit.put("pv_estado",      estado);
                mapaValosit.put("pv_nmpoliza",    nmpoliza);
                mapaValosit.put("pv_nmsituac",    nmsituac);
                mapaValosit.put("pv_nmsuplem",    nmsuplem);
                mapaValosit.put("pv_status",      "V");
                mapaValosit.put("pv_cdtipsit",    cdtipsit);
                mapaValosit.put("pv_accion_i",   "D");
                ////// 1. mapa valosit base //////
                
                ////// 2. obtener el original //////
                Map<String,String>mapaObtenerValosit=new HashMap<String,String>(0);
                mapaObtenerValosit.put("pv_cdunieco_i" , cdunieco);
                mapaObtenerValosit.put("pv_nmpoliza_i" , nmpoliza);
                mapaObtenerValosit.put("pv_cdramo_i"   , cdramo);
                mapaObtenerValosit.put("pv_estado_i"   , estado);
                mapaObtenerValosit.put("pv_nmsituac_i" , nmsituac);
                Map<String,Object>valositOriginal=kernelManager.obtieneValositSituac(mapaObtenerValosit);
                ////// 2. obtener el original //////
                
                ////// 3. copiar los otvalor del original a la base //////
                for(Entry<String,Object> en:valositOriginal.entrySet())
                {
                	String key=en.getKey();
                	if(key.substring(0,3).equalsIgnoreCase("otv"))
                	{
                		mapaValosit.put("pv_"+key,(String)en.getValue());
                	}
                }
                ////// 3. copiar los otvalor del original a la base //////
                
                kernelManager.insertaValoresSituaciones(mapaValosit);
                
                ////// valosit muerto//////
                ///////////////////////////
                
                /////////////////////////////
				////// mpoliper muerto //////
				Map<String,Object>mapaMpoliper=new LinkedHashMap<String,Object>(0);
				mapaMpoliper.put("pv_cdunieco_i" , cdunieco);
				mapaMpoliper.put("pv_cdramo_i"   , cdramo);
				mapaMpoliper.put("pv_estado_i"   , estado);
				mapaMpoliper.put("pv_nmpoliza_i" , nmpoliza);
				mapaMpoliper.put("pv_nmsituac_i" , nmsituac);
				mapaMpoliper.put("pv_cdrol_i"    , "2");
				mapaMpoliper.put("pv_cdperson_i" , cdperson);
				mapaMpoliper.put("pv_nmsuplem_i" , nmsuplem);
				mapaMpoliper.put("pv_status_i"   , "V");
				mapaMpoliper.put("pv_nmorddom_i" , "1");
				mapaMpoliper.put("pv_swreclam_i" , null);
				mapaMpoliper.put("pv_accion_i"   , "D");
				mapaMpoliper.put("pv_swexiper_i" , Constantes.SI);
				kernelManager.movMpoliper(mapaMpoliper);
				////// mpoliper muerto //////
				/////////////////////////////
                
                //////////////////////////////
                ////// inserta tworksup //////
                endososManager.movimientoTworksupEnd(
                		cdunieco
                		,cdramo
                		,estado
                		,nmpoliza
                		,TipoEndoso.BAJA_ASEGURADOS.getCdTipSup().toString()
                		,nmsuplem
                		,nmsituac, "I"
                		);
                ////// inserta tworksup //////
                //////////////////////////////
                
                //////////////////////////
		        ////// tarificacion //////
				endososManager.sigsvalipolEnd(
						cdusuari
						,cdelemen
						,cdunieco
						,cdramo
						,estado
						,nmpoliza
						,nmsituac
						,nmsuplem
						,TipoEndoso.BAJA_ASEGURADOS.getCdTipSup().toString()
						);
		        ////// tarificacion //////
				//////////////////////////
				
				//////////////////////////
				////// valor endoso //////
				Map<String,Object>mapaValorEndoso=new LinkedHashMap<String,Object>(0);
				mapaValorEndoso.put("pv_cdunieco_i" , cdunieco);
				mapaValorEndoso.put("pv_cdramo_i"   , cdramo);
				mapaValorEndoso.put("pv_estado_i"   , estado);
				mapaValorEndoso.put("pv_nmpoliza_i" , nmpoliza);
				mapaValorEndoso.put("pv_nmsituac_i" , nmsituac);
				mapaValorEndoso.put("pv_nmsuplem_i" , nmsuplem);
				mapaValorEndoso.put("pv_feinival_i" , fechaEndosoD);
				mapaValorEndoso.put("pv_cdtipsup_i" , TipoEndoso.BAJA_ASEGURADOS.getCdTipSup().toString());
				endososManager.calcularValorEndoso(mapaValorEndoso);
				////// valor endoso //////
				//////////////////////////
				
				/**
				 * Si el Parentesco en la baja de asegurado es el titular se reasigna
				 */
				if("T".equalsIgnoreCase(smap1.get("Parentesco"))){
					Map<String,String>mapaReasignaParent=new LinkedHashMap<String,String>(0);
					mapaReasignaParent.put("pv_cdunieco_i" , cdunieco);
					mapaReasignaParent.put("pv_cdramo_i"   , cdramo);
					mapaReasignaParent.put("pv_estado_i"   , estado);
					mapaReasignaParent.put("pv_nmpoliza_i" , nmpoliza);
					mapaReasignaParent.put("pv_nmsuplem_i" , nmsuplem);
					endososManager.reasignaParentescoTitular(mapaReasignaParent);
				}
				
				
			}
			
			// Se confirma el endoso si cumple la validacion de fechas: 
			RespuestaConfirmacionEndosoVO respConfirmacionEndoso = this.confirmarEndoso(
					cdunieco, 
					cdramo, 
					estado,
					nmpoliza,
					nmsuplem,
					nsuplogi, 
					//alta?"9":"10",
					alta ? TipoEndoso.ALTA_ASEGURADOS.getCdTipSup().toString()
					     : TipoEndoso.BAJA_ASEGURADOS.getCdTipSup().toString(),
					"",
					fechaEndosoD,
					cdtipsit
					,flujo
					,cdusuari
					,cdsisrol
					);
			
			String tramiteGenerado = respConfirmacionEndoso.getNumeroTramite();
		    
			String mensajeDespacho = null;
			if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
			    mensajeDespacho = respConfirmacionEndoso.getRespuestaTurnado().getMessage();
			}
			
			List<Map<String,String>>invalidos = new ArrayList<Map<String,String>>();
			if(alta)
			{
				LinkedHashMap<String,Object> paramsValidaEdad=new LinkedHashMap<String,Object>();
				paramsValidaEdad.put("1cdunieco" , cdunieco);
				paramsValidaEdad.put("2cdramo"   , cdramo);
				paramsValidaEdad.put("3estado"   , estado);
				paramsValidaEdad.put("4nmpoliza" , nmpoliza);
				paramsValidaEdad.put("5nmsuplem" , nmsuplem);
				invalidos=consultasManager.consultaDinamica(ObjetoBD.VALIDA_EDAD_ASEGURADOS, paramsValidaEdad);
				
				if (invalidos.size() > 0 && respConfirmacionEndoso.isConfirmado()) {
				    respConfirmacionEndoso.setConfirmado(false);
					String cdtipsup = alta ? TipoEndoso.ALTA_ASEGURADOS.getCdTipSup().toString()
						     : TipoEndoso.BAJA_ASEGURADOS.getCdTipSup().toString();
					
					String mensajeRespuesta = "El endoso se envi\u00f3 a autorizaci\u00f3n debido a que:<br/>";
					for(Map<String,String>iAseguradoEdadInvalida:invalidos)
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
					        respConfirmacionEndoso.getNumeroTramite(),
					        EstatusTramite.ENDOSO_EN_ESPERA.getCodigo(),
					        mensajeRespuesta,
					        null,  // cdrazrecha
					        null,  // cdusuariDes
					        null,  // cdsisrolDes
					        false, // permisoAgente
					        false, // porEscalamiento
					        fechaHoy,
					        false  // sinGrabarDetalle
					        );
					mensajeDespacho = StringUtils.isBlank(mensajeDespacho)
					        ? despacho.getMessage()
					        : Utils.join(mensajeDespacho, ". ", despacho.getMessage());
				}
			}
			
			if (respConfirmacionEndoso.isConfirmado()) {
			
			    ///////////////////////////////////////
			    ////// re generar los documentos //////
			    /*///////////////////////////////////*/
				Map<String,String> datosPoliza = documentosManager.generarDocumentosParametrizados(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,"0" //nmsituac
						,nmsuplem
						,DocumentosManager.PROCESO_ENDOSO
						,ntramite
						,null //nmsolici
, null
						);
				
				String nmsolici    = datosPoliza.get("nmsolici");
				String rutaCarpeta = Utils.join(this.rutaDocumentosPoliza,"/",ntramite);
				
				String sucursal = cdunieco;
				
				String tipomov = alta?"9":"10";
				
				// Ejecutamos el Web Service de Recibos:
				ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo, 
						estado, nmpoliza, 
						nmsuplem, rutaCarpeta, 
						sucursal, nmsolici, ntramite, 
						true, tipomov, 
						(UserVO) session.get("USUARIO"));
				
				mensaje = Utils.join("Se ha guardado el endoso ", nsuplogi);
			}
			else
			{
				String mensajeInvalido = "";
				if(alta&&invalidos.size()>0)
				{
					Map<String,String>aseguradoInvalido=invalidos.get(0);
					mensajeInvalido = " debido a que "+aseguradoInvalido.get("NOMBRE");
					if(aseguradoInvalido.get("SUPERAMINI").substring(0, 1).equals("-"))
					{
						mensajeInvalido = mensajeInvalido + " no llega a la edad de "+aseguradoInvalido.get("EDADMINI")+" a&ntilde;os<br/>";
					}
					else
					{
						mensajeInvalido = mensajeInvalido + " supera la edad de "+aseguradoInvalido.get("EDADMAXI")+" a&ntilde;os<br/>";
					}
				}
					mensaje="El endoso "+nsuplogi
							+" se guard\u00f3 en mesa de control para autorizaci\u00f3n"
							+" con n\u00famero de tr\u00e1mite "+tramiteGenerado
							+mensajeInvalido;
			}
			
			if (StringUtils.isNotBlank(mensajeDespacho)) {
			    mensaje = Utils.join(mensaje, ". ", mensajeDespacho);
			}
			success=true;
		}
		catch(Exception ex)
		{
			logger.error("error al guardar endoso de alta o baja de asegurado", ex);
			success=false;
			error=ex.getMessage();
		}
		
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### error   = " , error
				,"\n###### mensaje = " , mensaje
				,"\n###### guardarEndosoAltaBajaAsegurado ######"
				,"\n############################################"
				));
		
		return SUCCESS;
	}
	/*/////////////////////////////////////////////////////*/
	////// guardar endoso de alta o baja de asegurados //////
	/////////////////////////////////////////////////////////
	
	/////////////////////////////////////////
	////// prueba de pantalla dinamica //////
	/*/////////////////////////////////////*/
	public String testpantalla()
	{
		try
		{
			UserVO usuario=(UserVO)session.get("USUARIO");
			
			GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			
			gc.generaParcial(pantallasManager.obtenerComponentes(
					null, null, null,
					null, null, usuario.getRolActivo().getClave(),
					"TESTPANTALLA", "TEST", null));
			
			item1=gc.getFields();
			item2=gc.getColumns();
			item3=gc.getItems();
		}
		catch(Exception ex)
		{
			logger.error("error en la prueba de pantalla dinamica",ex);
		}
		return SUCCESS;
	}
	/*/////////////////////////////////////*/
	////// prueba de pantalla dinamica //////
	/////////////////////////////////////////
	
	
	////////////////////////////
	////// endoso de edad //////
	/*
	smap1:
	    'cdunieco' , 'cdramo'   , 'cdtipsit'
	    ,'estado'  , 'nmpoliza' , 'ntramite'
	    ,'masedad(si,no)
	*/
	/*////////////////////////*/
	public String endosoEdad()
	{
		this.session=ActionContext.getContext().getSession();
		logger.debug(new StringBuilder("\n")
		        .append("\n############################")
		        .append("\n############################")
		        .append("\n###### endoso de edad ######")
		        .append("\n######                ######").toString());
		logger.debug(new StringBuilder("smap1: ").append(smap1).toString());
		logger.debug(new StringBuilder("slist1: ").append(slist1).toString());

		RespuestaVO resp = null;
		
		try
		{
			transformaEntrada(smap1, slist1, true);
			
			// Valida si hay un endoso anterior pendiente:
			resp = endososManager.validaEndosoAnterior(
					smap1.get("cdunieco")
					,smap1.get("cdramo")
					,smap1.get("estado")
					,smap1.get("nmpoliza")
					,smap1.get("masedad").equalsIgnoreCase("si")?
							TipoEndoso.INCREMENTO_EDAD_ASEGURADO.getCdTipSup().toString():
							TipoEndoso.DECREMENTO_EDAD_ASEGURADO.getCdTipSup().toString());
			error = resp.getMensaje();
			
			if(resp.isSuccess()){
				try {
					imap1=new HashMap<String,Item>();
					GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
					gc.generaParcial(pantallasManager.obtenerComponentes(
							null, null, null,
							null, null, null,
							"ENDOSO_EDAD", "MODELO", null));
					
					imap1.put("modelo"   , gc.getFields());
					imap1.put("columnas" , gc.getColumns());
				} catch(Exception ex) {
					logger.error("error al cargar la pantalla de endoso de edad",ex);
				}
			}
		}
		catch(Exception ex)
		{
			logger.error("Error al mostrar pantalla de endoso de edad",ex);
			error = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(new StringBuilder("\n")
	        .append("\n######                ######")
	        .append("\n###### endoso de edad ######")
	        .append("\n############################")
	        .append("\n############################").toString());
		
		return resp!=null&&resp.isSuccess() ? SUCCESS : ERROR;
	}
	/*////////////////////////*/
	////// endoso de edad //////
	////////////////////////////
	
	////////////////////////////////////
	////// guardar endoso de edad //////
	/*
	smap1: {cdramo=2, nmpoliza=23, estado=M, masedad=si, cdtipsit=SL, ntramite=480, cdunieco=1205}
	smap2: {fecha_endoso=15/01/2014}
	slist1: [{cdperson=511783, nmsituac=1, fenacimi=01/01/1969}
	        ,{cdperson=511782, nmsituac=2, fenacimi=01/01/1974}
	        ,{cdperson=511785, nmsituac=3, fenacimi=01/01/1996}
	        ,{cdperson=511786, nmsituac=4, fenacimi=01/01/1950}]
	*/
	/*////////////////////////////////*/
	public String guardarEndosoEdad() {
		this.session=ActionContext.getContext().getSession();
		
		logger.debug(Utils.log(
				 "\n###############################"
				,"\n###### guardarEndosoEdad ######"
				,"\n###### smap1  = " , smap1
				,"\n###### smap2  = " , smap2
				,"\n###### slist1 = " , slist1
				,"\n###### flujo  = " , flujo
				));
		
		try {
			String  cdunieco    = smap1.get("cdunieco");
			String  cdramo      = smap1.get("cdramo");
			String  cdtipsit    = smap1.get("cdtipsit");
			String  estado      = smap1.get("estado");
			String  nmpoliza    = smap1.get("nmpoliza");
			String  ntramite    = smap1.get("ntramite");
			String  cdelemen    = ((UserVO)session.get("USUARIO")).getEmpresa().getElementoId();
			String  cdusuari    = ((UserVO)session.get("USUARIO")).getUser();
			String  cdsisrol    = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
			Date    fechaHoy    = new Date();
			String  fechaEndoso = smap2.get("fecha_endoso");
			boolean incremento  = smap1.get("masedad").equalsIgnoreCase("si");
			String cdtipsup = incremento 
					? TipoEndoso.INCREMENTO_EDAD_ASEGURADO.getCdTipSup().toString()
					: TipoEndoso.DECREMENTO_EDAD_ASEGURADO.getCdTipSup().toString();
			
			Map<String,String>paramsIniciarEndoso=new HashMap<String,String>(0);
			paramsIniciarEndoso.put("pv_cdunieco_i" , cdunieco);
			paramsIniciarEndoso.put("pv_cdramo_i"   , cdramo);
			paramsIniciarEndoso.put("pv_estado_i"   , estado);
			paramsIniciarEndoso.put("pv_nmpoliza_i" , nmpoliza);
			paramsIniciarEndoso.put("pv_fecha_i"    , fechaEndoso);
			paramsIniciarEndoso.put("pv_cdelemen_i" , cdelemen);
			paramsIniciarEndoso.put("pv_cdusuari_i" , cdusuari);
			paramsIniciarEndoso.put("pv_proceso_i"  , "END");
			paramsIniciarEndoso.put("pv_cdtipsup_i" , cdtipsup);
			Map<String,String>respuestaIniciarEndoso=endososManager.iniciarEndoso(paramsIniciarEndoso);
			
			String nmsuplem=respuestaIniciarEndoso.get("pv_nmsuplem_o");
			String nsuplogi=respuestaIniciarEndoso.get("pv_nsuplogi_o");
			
			for(Map<String,String>inciso:slist1) {
				String nmsituacIte=inciso.get("nmsituac");
				String cdpersonIte=inciso.get("cdperson");
				String fenacimiIte=inciso.get("fenacimi");
				String sexo=inciso.get("sexo");
				String cdplan=inciso.get("cdplan");
				String cdgrupo=inciso.get("cdgrupo");
				
				/////////////////////
                ////// valosit //////
                
                ////// 1. mapa valosit base //////
                Map<String,String>mapaValosit=new HashMap<String,String>(0);
                mapaValosit.put("pv_cdunieco",    cdunieco);
                mapaValosit.put("pv_cdramo",      cdramo);
                mapaValosit.put("pv_estado",      estado);
                mapaValosit.put("pv_nmpoliza",    nmpoliza);
                mapaValosit.put("pv_nmsituac",    nmsituacIte);
                mapaValosit.put("pv_nmsuplem",    nmsuplem);
                mapaValosit.put("pv_status",      "V");
                mapaValosit.put("pv_cdtipsit",    cdtipsit);
                mapaValosit.put("pv_accion_i",   "I");
                ////// 1. mapa valosit base //////
                
                ////// 2. obtener el original //////
                Map<String,String>mapaObtenerValosit=new HashMap<String,String>(0);
                mapaObtenerValosit.put("pv_cdunieco_i" , cdunieco);
                mapaObtenerValosit.put("pv_nmpoliza_i" , nmpoliza);
                mapaObtenerValosit.put("pv_cdramo_i"   , cdramo);
                mapaObtenerValosit.put("pv_estado_i"   , estado);
                mapaObtenerValosit.put("pv_nmsituac_i" , nmsituacIte);
                Map<String,Object>valositOriginal=kernelManager.obtieneValositSituac(mapaObtenerValosit);
                ////// 2. obtener el original //////
                
                ////// 3. copiar los otvalor del original a la base //////
                for(Entry<String,Object> en:valositOriginal.entrySet()) {
                	String key=en.getKey();
                	if(key.substring(0,3).equalsIgnoreCase("otv")) {
                		mapaValosit.put("pv_"+key,(String)en.getValue());
                	}
                }
                ////// 3. copiar los otvalor del original a la base //////
                
                ////// 4. sustituir los otvalor por los nuevos del form //////
				Map<String,String>atributos=consultasManager.cargarAtributosBaseCotizacion(cdtipsit);
				String cdatribuFenacimi = StringUtils.leftPad(atributos.get("FENACIMI"),2,"0");
                mapaValosit.put("pv_otvalor"+cdatribuFenacimi,fenacimiIte);
                ////// 4. sustituir los otvalor por los nuevos del form //////
                
                kernelManager.insertaValoresSituaciones(mapaValosit);
                
                ////// valosit //////
                /////////////////////
                
                //////////////////////
                ////// mpersona //////
                Map<String,String> mapaMpersona=new LinkedHashMap<String,String>(0);
				mapaMpersona.put("pv_cdperson_i" , cdpersonIte); 
				mapaMpersona.put("pv_fenacimi_i" , fenacimiIte);
				endososManager.actualizarFenacimi(mapaMpersona);
                ////// mpersona //////
                //////////////////////
				
				/////////////////////////////////
                ////// valores por defecto //////
                /*Map<String,String> mapaCoberturas=new HashMap<String,String>(0);
                mapaCoberturas.put("pv_cdunieco_i",   cdunieco);//se agrega desde el formulario
                mapaCoberturas.put("pv_cdramo_i",     cdramo);//se agrega desde el formulario
                mapaCoberturas.put("pv_estado_i",     estado);
                mapaCoberturas.put("pv_nmpoliza_i",   nmpoliza);
                mapaCoberturas.put("pv_nmsituac_i",   nmsituacIte);
                mapaCoberturas.put("pv_nmsuplem_i",   nmsuplem);
                mapaCoberturas.put("pv_cdgarant_i",   "TODO");
                mapaCoberturas.put("pv_cdtipsup_i",   cdtipsup);
                kernelManager.coberturas(mapaCoberturas);*/
                ////// valores por defecto //////
                /////////////////////////////////
                
                //////////////////////////////
                ////// inserta tworksup //////
                endososManager.movimientoTworksupEnd(
                		cdunieco
                		,cdramo
                		,estado
                		,nmpoliza
                		,cdtipsup
                		,nmsuplem
                		,nmsituacIte, "I"
                		);
                ////// inserta tworksup //////
                //////////////////////////////
                endososManager.clonarGarantiaCapitales(
	        		cdunieco
	        		,cdramo
	        		,estado
	        		,nmpoliza
	        		,nmsuplem
	        		,nmsituacIte
	        		,cdgrupo
	        		,cdplan
	        		,sexo
	    		);
                //////////////////////////
                ////// tarificacion //////
    			endososManager.sigsvalipolEnd(
    					cdusuari
    					,cdelemen
    					,cdunieco
    					,cdramo
    					,estado
    					,nmpoliza
    					,nmsituacIte
    					,nmsuplem
    					,cdtipsup
    					);
                ////// tarificacion //////
    			//////////////////////////
    			
    			//////////////////////////
    			////// valor endoso //////
    			Map<String,Object>mapaValorEndoso=new LinkedHashMap<String,Object>(0);
    			mapaValorEndoso.put("pv_cdunieco_i" , cdunieco);
    			mapaValorEndoso.put("pv_cdramo_i"   , cdramo);
    			mapaValorEndoso.put("pv_estado_i"   , estado);
    			mapaValorEndoso.put("pv_nmpoliza_i" , nmpoliza);
    			mapaValorEndoso.put("pv_nmsituac_i" , nmsituacIte);
    			mapaValorEndoso.put("pv_nmsuplem_i" , nmsuplem);
    			mapaValorEndoso.put("pv_feinival_i" , renderFechas.parse(fechaEndoso));
    			mapaValorEndoso.put("pv_cdtipsup_i" , cdtipsup);
    			endososManager.calcularValorEndoso(mapaValorEndoso);
    			////// valor endoso //////
    			//////////////////////////
			}
			
			// Se confirma el endoso si cumple la validacion de fechas: 
			RespuestaConfirmacionEndosoVO respConfirmacionEndoso = this.confirmarEndoso(
					cdunieco,
					cdramo,
					estado,
					nmpoliza,
					nmsuplem,
					nsuplogi,
					cdtipsup,
					"",
					renderFechas.parse(fechaEndoso),
					cdtipsit
					,flujo
					,cdusuari
					,cdsisrol
					);
			
			// Si el endoso fue confirmado:
			if(respConfirmacionEndoso.isConfirmado()) {
				endosoConfirmado = true;

			    ///////////////////////////////////////
			    ////// re generar los documentos //////
			    /*///////////////////////////////////*/
				Map<String,String> datosPoliza = documentosManager.generarDocumentosParametrizados(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,"0" //nmsituac
						,nmsuplem
						,DocumentosManager.PROCESO_ENDOSO
						,ntramite
						,null //nmsolici
						, null
						);
				
				String nmsolici    = datosPoliza.get("nmsolici");
				String rutaCarpeta = Utils.join(this.rutaDocumentosPoliza,"/",ntramite);
				
				/*
			    List<Map<String,String>>listaDocu=endososManager.reimprimeDocumentos(
			    		cdunieco
			    		,cdramo
			    		,estado
			    		,nmpoliza
			    		,nmsuplem
			    		,cdtipsup
			    		);
			    logger.debug("documentos que se regeneran: "+listaDocu);
			    
			    String rutaCarpeta=this.rutaDocumentosPoliza+"/"+ntramite;
			    
				//listaDocu contiene: nmsolici,nmsituac,descripc,descripl
				for(Map<String,String> docu:listaDocu) {
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
							+ "&p_estado="+estado
							+ "&p_poliza="+nmpoliza
							+ "&p_suplem="+nmsuplem
							+ "&desname="+rutaCarpeta+"/"+descripc;
					if(descripc.substring(0, 6).equalsIgnoreCase("CREDEN")) {
						// C R E D E N C I A L _ X X X X X X . P D F
						//0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
						url+="&p_cdperson="+descripc.substring(11, descripc.lastIndexOf("_"));
					}
					logger.debug(""
							+ "\n#################################"
							+ "\n###### Se solicita reporte ######"
							+ "\na "+url+""
							+ "\n#################################");
					HttpUtil.generaArchivo(url,rutaCarpeta+"/"+descripc);
					logger.debug(""
							+ "\n######                    ######"
							+ "\n###### reporte solicitado ######"
							+ "\na "+url+""
							+ "\n################################"
							+ "\n################################"
							+ "");
				}
				*/
			    /*///////////////////////////////////*/
				////// re generar los documentos //////
			    ///////////////////////////////////////
				
				String saludDanios = "S";
				ClienteGeneral clienteGeneral = new ClienteGeneral();
				clienteGeneral.setClaveCia(saludDanios);
				
				// Ejecutamos el Web Service de Cliente Salud:
				ice2sigsService.ejecutaWSclienteGeneral(cdunieco, cdramo, estado, nmpoliza, nmsuplem, respConfirmacionEndoso.getNumeroTramite(), null, Ice2sigsService.Operacion.ACTUALIZA, clienteGeneral, (UserVO) session.get("USUARIO"), false);
				
				String sucursal = cdunieco;
				//String nmsolici = listaDocu.get(0).get("nmsolici");
				String nmtramite = ntramite;//listaDocu.get(0).get("ntramite");
				
				// Ejecutamos el Web Service de Recibos:
				ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo, 
						estado, nmpoliza, 
						nmsuplem, rutaCarpeta, 
						sucursal, nmsolici, nmtramite, 
						true, cdtipsup, 
						(UserVO) session.get("USUARIO"));
				mensaje="Endoso confirmado "+nsuplogi;
				if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
                }
			} else {
				mensaje="El endoso "+nsuplogi
						+" se guard\u00f3 en mesa de control para autorizaci\u00f3n "
						+ "con n\u00famero de tr\u00e1mite " + respConfirmacionEndoso.getNumeroTramite();
				if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
                }
			}
			
			success=true;
		} catch(Exception ex) {
			logger.debug("error al guardar endoso de edad",ex);
			success=false;
			error=ex.getMessage();
		}
		
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### error   = " , error
				,"\n###### mensaje = " , mensaje
				,"\n###### guardarEndosoEdad ######"
				,"\n###############################"
				));
		
		return SUCCESS;
	}
	/*////////////////////////////////*/
	////// guardar endoso de edad //////
	////////////////////////////////////
	
	//////////////////////////////////////
	////// endoso de cambio de sexo //////
	/*
	smap1:
		cdramo: "2"
		cdtipsit: "SL"
		cdunieco: "1006"
		estado: "M"
		hombremujer: "no"
		nmpoliza: "18"
		ntramite: "662"
	*/
	/*//////////////////////////////////*/
	public String endosoSexo() {
		
		logger.debug(new StringBuilder("\n")
		        .append("\n#######################################")
		        .append("\n#######################################")
		        .append("\n###### endoso de camibio de sexo ######")
		        .append("\n######                           ######").toString());
		logger.debug(new StringBuilder("smap1: ").append(smap1).toString());
		logger.debug(new StringBuilder("smap2: ").append(smap2).toString());
		logger.debug(new StringBuilder("slist1: ").append(slist1).toString());
		
		this.session = ActionContext.getContext().getSession();
		
		RespuestaVO resp = null;
		
		try
		{
			transformaEntrada(smap1, slist1, true);
			
			// Valida si hay un endoso anterior pendiente:
			resp = endososManager.validaEndosoAnterior(
					smap1.get("cdunieco")
					,smap1.get("cdramo")
					,smap1.get("estado")
					,smap1.get("nmpoliza")
					,smap1.get("hombremujer").equalsIgnoreCase("si")?
							TipoEndoso.MODIFICACION_SEXO_H_A_M.getCdTipSup().toString():
							TipoEndoso.MODIFICACION_SEXO_M_A_H.getCdTipSup().toString());
			error = resp.getMensaje();
			
			if(resp.isSuccess()){
				try {
					imap1=new HashMap<String,Item>();
					GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
					gc.generaParcial(pantallasManager.obtenerComponentes(
							null, null, null,
							null, null, null,
							"ENDOSO_SEXO", "MODELO", null));
					
					imap1.put("modelo"   , gc.getFields());
					imap1.put("columnas" , gc.getColumns());
				} catch(Exception ex) {
					logger.error("error al mostrar pantalla de endoso de cambio de sexo",ex);
				}
			}
		}
		catch(Exception ex)
		{
			logger.error("Error al construir pantalla de endoso de cambio de sexo",ex);
			error = Utils.manejaExcepcion(ex);
		}
		
		
		logger.debug(new StringBuilder("\n")
		        .append("\n######                           ######")
		        .append("\n###### endoso de camibio de sexo ######")
		        .append("\n#######################################")
		        .append("\n#######################################").toString());
		
		return resp!=null&&resp.isSuccess() ? SUCCESS : ERROR;
	}
	/*//////////////////////////////////*/
	////// endoso de cambio de sexo //////
	//////////////////////////////////////
	
	//////////////////////////////////////////////
	////// guardar endoso de cambio de sexo //////
	/*
	smap1: {cdramo=2, nmpoliza=23, estado=M, cdtipsit=SL, ntramite=480, cdunieco=1205, hombremujer=si}
	smap2: {fecha_endoso=16/01/2014}
    slist1:
           [
                {cdperson=511816, nmsituac=1}
                ,{cdperson=511811, nmsituac=2}
           ]
	*/
	/*//////////////////////////////////////////*/
	public String guardarEndosoSexo()
	{
		logger.debug(Utils.log(
				 "\n###############################"
				,"\n###### guardarEndosoSexo ######"
				,"\n###### smap1  = " , smap1
				,"\n###### smap2  = " , smap2
				,"\n###### slist1 = " , slist1
				,"\n###### flujo  = " , flujo
				));
		
		this.session=ActionContext.getContext().getSession();
		
		try {
			String  cdunieco    = smap1.get("cdunieco");
			String  cdramo      = smap1.get("cdramo");
			String  cdtipsit    = smap1.get("cdtipsit");
			String  estado      = smap1.get("estado");
			String  nmpoliza    = smap1.get("nmpoliza");
			String  ntramite    = smap1.get("ntramite");
			String  cdelemen    = ((UserVO)session.get("USUARIO")).getEmpresa().getElementoId();
			String  cdusuari    = ((UserVO)session.get("USUARIO")).getUser();
			String  cdsisrol    = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
			Date    fechaHoy    = new Date();
			String  fechaEndoso = smap2.get("fecha_endoso");
			boolean hombremujer = smap1.get("hombremujer").equalsIgnoreCase("si");
			String cdtipsup = hombremujer 
					? TipoEndoso.MODIFICACION_SEXO_H_A_M.getCdTipSup().toString()
					: TipoEndoso.MODIFICACION_SEXO_M_A_H.getCdTipSup().toString();
			String sexo         = hombremujer?"M":"H";
			
			Map<String,String>paramsIniciarEndoso=new HashMap<String,String>(0);
			paramsIniciarEndoso.put("pv_cdunieco_i" , cdunieco);
			paramsIniciarEndoso.put("pv_cdramo_i"   , cdramo);
			paramsIniciarEndoso.put("pv_estado_i"   , estado);
			paramsIniciarEndoso.put("pv_nmpoliza_i" , nmpoliza);
			paramsIniciarEndoso.put("pv_fecha_i"    , fechaEndoso);
			paramsIniciarEndoso.put("pv_cdelemen_i" , cdelemen);
			paramsIniciarEndoso.put("pv_cdusuari_i" , cdusuari);
			paramsIniciarEndoso.put("pv_proceso_i"  , "END");
			paramsIniciarEndoso.put("pv_cdtipsup_i" , cdtipsup);
			Map<String,String>respuestaIniciarEndoso=endososManager.iniciarEndoso(paramsIniciarEndoso);
			
			String nmsuplem=respuestaIniciarEndoso.get("pv_nmsuplem_o");
			String nsuplogi=respuestaIniciarEndoso.get("pv_nsuplogi_o");
			
			for(Map<String,String>inciso:slist1) {
				String nmsituacIte=inciso.get("nmsituac");
				String cdpersonIte=inciso.get("cdperson");
				String cdplan=inciso.get("cdplan");
				String cdgrupo=inciso.get("cdgrupo");
				
                
                /////////////////////
                ////// valosit //////
                
                ////// 1. mapa valosit base //////
                Map<String,String>mapaValosit=new HashMap<String,String>(0);
                mapaValosit.put("pv_cdunieco",    cdunieco);
                mapaValosit.put("pv_cdramo",      cdramo);
                mapaValosit.put("pv_estado",      estado);
                mapaValosit.put("pv_nmpoliza",    nmpoliza);
                mapaValosit.put("pv_nmsituac",    nmsituacIte);
                mapaValosit.put("pv_nmsuplem",    nmsuplem);
                mapaValosit.put("pv_status",      "V");
                mapaValosit.put("pv_cdtipsit",    cdtipsit);
                mapaValosit.put("pv_accion_i",   "I");
                ////// 1. mapa valosit base //////
                
                ////// 2. obtener el original //////
                Map<String,String>mapaObtenerValosit=new HashMap<String,String>(0);
                mapaObtenerValosit.put("pv_cdunieco_i" , cdunieco);
                mapaObtenerValosit.put("pv_nmpoliza_i" , nmpoliza);
                mapaObtenerValosit.put("pv_cdramo_i"   , cdramo);
                mapaObtenerValosit.put("pv_estado_i"   , estado);
                mapaObtenerValosit.put("pv_nmsituac_i" , nmsituacIte);
                Map<String,Object>valositOriginal=kernelManager.obtieneValositSituac(mapaObtenerValosit);
                ////// 2. obtener el original //////
                
                ////// 3. copiar los otvalor del original a la base //////
                for(Entry<String,Object> en:valositOriginal.entrySet()) {
                	String key=en.getKey();
                	if(key.substring(0,3).equalsIgnoreCase("otv")) {
                		mapaValosit.put("pv_"+key,(String)en.getValue());
                	}
                }
                ////// 3. copiar los otvalor del original a la base //////
                
                ////// 4. sustituir los otvalor por los nuevos del form //////
				Map<String,String>atributos=consultasManager.cargarAtributosBaseCotizacion(cdtipsit);
				String cdatribuSexo = StringUtils.leftPad(atributos.get("SEXO"),2,"0");
                mapaValosit.put("pv_otvalor"+cdatribuSexo,sexo);
                ////// 4. sustituir los otvalor por los nuevos del form //////
                
                kernelManager.insertaValoresSituaciones(mapaValosit);
                
                ////// valosit //////
                /////////////////////
                
                //////////////////////
                ////// mpersona //////
                Map<String,String> mapaMpersona=new LinkedHashMap<String,String>(0);
				mapaMpersona.put("pv_cdperson_i" , cdpersonIte); 
				mapaMpersona.put("pv_sexo_i"     , sexo);
				endososManager.actualizarSexo(mapaMpersona);
                ////// mpersona //////
                //////////////////////
				
				/////////////////////////////////
                ////// valores por defecto //////
                /*Map<String,String> mapaCoberturas=new HashMap<String,String>(0);
                mapaCoberturas.put("pv_cdunieco_i",   cdunieco);//se agrega desde el formulario
                mapaCoberturas.put("pv_cdramo_i",     cdramo);//se agrega desde el formulario
                mapaCoberturas.put("pv_estado_i",     estado);
                mapaCoberturas.put("pv_nmpoliza_i",   nmpoliza);
                mapaCoberturas.put("pv_nmsituac_i",   nmsituacIte);
                mapaCoberturas.put("pv_nmsuplem_i",   nmsuplem);
                mapaCoberturas.put("pv_cdgarant_i",   "TODO");
                mapaCoberturas.put("pv_cdtipsup_i",   cdtipsup);
                kernelManager.coberturas(mapaCoberturas);*/
                ////// valores por defecto //////
                /////////////////////////////////
                
                //////////////////////////////
                ////// inserta tworksup //////
                endososManager.movimientoTworksupEnd(
                		cdunieco
                		,cdramo
                		,estado
                		,nmpoliza
                		,cdtipsup
                		,nmsuplem
                		,nmsituacIte, "I"
                		);
                
				//////////////////////////////
                ////// inserta tworksup //////
                endososManager.clonarGarantiaCapitales(
	        		cdunieco
	        		,cdramo
	        		,estado
	        		,nmpoliza
	        		,nmsuplem
	        		,nmsituacIte
	        		,cdgrupo
	        		,cdplan
	        		,sexo
	    		);
                ////// inserta tworksup //////
                //////////////////////////////
                //aqui va el llamado al Sp que mecomento Alex
                //////////////////////////
                ////// tarificacion //////
    			endososManager.sigsvalipolEnd(
    					cdusuari
    					,cdelemen
    					,cdunieco
    					,cdramo
    					,estado
    					,nmpoliza
    					,nmsituacIte
    					,nmsuplem
    					,cdtipsup
    					);
                ////// tarificacion //////
    			//////////////////////////
    			
    			//////////////////////////
    			////// valor endoso //////
    			Map<String,Object>mapaValorEndoso=new LinkedHashMap<String,Object>(0);
    			mapaValorEndoso.put("pv_cdunieco_i" , cdunieco);
    			mapaValorEndoso.put("pv_cdramo_i"   , cdramo);
    			mapaValorEndoso.put("pv_estado_i"   , estado);
    			mapaValorEndoso.put("pv_nmpoliza_i" , nmpoliza);
    			mapaValorEndoso.put("pv_nmsituac_i" , nmsituacIte);
    			mapaValorEndoso.put("pv_nmsuplem_i" , nmsuplem);
    			mapaValorEndoso.put("pv_feinival_i" , renderFechas.parse(fechaEndoso));
    			mapaValorEndoso.put("pv_cdtipsup_i" , cdtipsup);
    			endososManager.calcularValorEndoso(mapaValorEndoso);
    			////// valor endoso //////
    			//////////////////////////
			}
			
			// Se confirma el endoso si cumple la validacion de fechas: 
			RespuestaConfirmacionEndosoVO respConfirmacionEndoso = this.confirmarEndoso(
					cdunieco,
					cdramo,
					estado,
					nmpoliza,
					nmsuplem,
					nsuplogi,
					cdtipsup,
					"",
					renderFechas.parse(fechaEndoso),
					cdtipsit
					,flujo
					,cdusuari
					,cdsisrol
					);
		    
			// Si el endoso fue confirmado:
			if(respConfirmacionEndoso.isConfirmado()) {
				endosoConfirmado = true;
				
			    ///////////////////////////////////////
			    ////// re generar los documentos //////
			    /*///////////////////////////////////*/
				Map<String,String> datosPoliza = documentosManager.generarDocumentosParametrizados(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,"0" //nmsituac
						,nmsuplem
						,DocumentosManager.PROCESO_ENDOSO
						,ntramite
						,null //nmsolici
						,null
						);
				
				String nmsolici    = datosPoliza.get("nmsolici");
				String rutaCarpeta = Utils.join(this.rutaDocumentosPoliza,"/",ntramite);
				String saludDanios = "S";
				ClienteGeneral clienteGeneral = new ClienteGeneral();
				clienteGeneral.setClaveCia(saludDanios);
				
				// Ejecutamos el Web Service de Cliente Salud:
				ice2sigsService.ejecutaWSclienteGeneral(cdunieco, cdramo, estado, nmpoliza, nmsuplem, respConfirmacionEndoso.getNumeroTramite(), null, Ice2sigsService.Operacion.ACTUALIZA, clienteGeneral, (UserVO) session.get("USUARIO"), false);
				
				String sucursal = cdunieco;
				//String nmsolici = listaDocu.get(0).get("nmsolici");
				String nmtramite = ntramite;//listaDocu.get(0).get("ntramite");
				
				// Ejecutamos el Web Service de Recibos:
				ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo, 
						estado, nmpoliza, 
						nmsuplem, rutaCarpeta, 
						sucursal, nmsolici, nmtramite, 
						true, cdtipsup, 
						(UserVO) session.get("USUARIO"));
				
				mensaje="Endoso confirmado "+nsuplogi;
			} else {
				mensaje="El endoso "+nsuplogi
						+" se guard\u00f3 en mesa de control para autorizaci\u00f3n "
						+ "con n\u00famero de tr\u00e1mite " + respConfirmacionEndoso.getNumeroTramite();
			}
			if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
                mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
            }
			success=true;
		} catch(Exception ex) {
			success=false;
			error=ex.getMessage();
			logger.error("error al guardar endoso de cambio de sexo",ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### error   = " , error
				,"\n###### mensaje = " , mensaje
				,"\n###### guardarEndosoSexo ######"
				,"\n###############################"
				));
		
		return SUCCESS;
	}
	/*//////////////////////////////////////////*/
	////// guardar endoso de cambio de sexo //////
	//////////////////////////////////////////////
	
	//////////////////////////////
	////// confirmar endoso //////
	/*//////////////////////////*/
	/**
	 * Confirma un endoso si no excede un maximo de dias permitidos (+-30 dias) 
	 * //+- 30 dias ? PKG_SATELITES.P_MOV_MESACONTROL : PKG_ENDOSOS.P_CONFIRMAR_ENDOSOB
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @param nsuplogi
	 * @param cdtipsup
	 * @param dscoment
	 * @param fechaEndoso
	 * @param cdtipsit
	 * @return Respuesta de la confirmacion del Endoso
	 * @throws Exception
	 */
	private RespuestaConfirmacionEndosoVO confirmarEndoso(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nsuplogi
			,String cdtipsup
			,String dscoment
			,Date fechaEndoso
			,String cdtipsit
			,FlujoVO flujo
			,String cdusuari
			,String cdsisrol
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n#############################"
				,"\n###### confirmarEndoso ######"
				,"\n###### cdunieco    = " , cdunieco
				,"\n###### cdramo      = " , cdramo
				,"\n###### estado      = " , estado
				,"\n###### nmpoliza    = " , nmpoliza
				,"\n###### nmsuplem    = " , nmsuplem
				,"\n###### nsuplogi    = " , nsuplogi
				,"\n###### cdtipsup    = " , cdtipsup
				,"\n###### dscoment    = " , dscoment
				,"\n###### fechaEndoso = " , fechaEndoso
				,"\n###### cdtipsit    = " , cdtipsit
				,"\n###### flujo       = " , flujo
				,"\n###### cdusuari    = " , cdusuari
				,"\n###### cdsisrol    = " , cdsisrol
				));
		
		RespuestaConfirmacionEndosoVO respuesta = new RespuestaConfirmacionEndosoVO();
		
		String paso = null;
		
		try
		{
			if(consultasManager.esProductoSalud(cdramo)) // SALUD
			{
				if(flujo == null) // SALUD SIN FLUJO
				{
					paso = "Recuperando d\u00edas de endoso v\u00e1lido";
					logger.debug(paso);
					
					long numMaximoDias = (long)endososManager.recuperarDiasDiferenciaEndosoValidos(cdramo,cdtipsup);
					
					paso = "Recuperando tr\u00e1mite de emisi\u00f3n";
					logger.debug(paso);
					
					// Se obtiene el numero de tramite de emision de una poliza:
					String ntramiteEmision=endososManager.obtenerNtramiteEmision(cdunieco, cdramo, estado, nmpoliza);
					
					// Se almacena la diferencia entre la fecha actual y a fecha que tendra el endoso:
					Date fechaHoy=new Date();
					long diferenciaFechaActualVSEndoso = fechaHoy.getTime() - fechaEndoso.getTime();
					diferenciaFechaActualVSEndoso = Math.abs(diferenciaFechaActualVSEndoso);
					// Se almacena el maximo de dias permitidos para realizar un endoso (30 dias):
					long maximoDiasPermitidos = numMaximoDias*24l*60l*60l*1000l;
					
					paso = "Recuperando descripci\u00f3n de endoso";
					logger.debug(paso);
					
					String descEndoso = endososManager.obtieneDescripcionEndoso(cdtipsup); 
					
					logger.debug("************* diferenciaFechaActualVSEndoso=" + diferenciaFechaActualVSEndoso);
					logger.debug("************* maximoDiasPermitidos         =" + maximoDiasPermitidos);
					
					String estatusTramite = null;
					if(diferenciaFechaActualVSEndoso > maximoDiasPermitidos) {
						logger.debug("************* El Endoso esta en espera, confirmado false");
						estatusTramite = EstatusTramite.ENDOSO_EN_ESPERA.getCodigo();
						respuesta.setConfirmado(false);
					} else {
						// Se confirma endoso:
						Map<String,String> paramsConfirmarEndosoB = new LinkedHashMap<String,String>(0);
						paramsConfirmarEndosoB.put("pv_cdunieco_i" , cdunieco);
						paramsConfirmarEndosoB.put("pv_cdramo_i"   , cdramo);
						paramsConfirmarEndosoB.put("pv_estado_i"   , estado);
						paramsConfirmarEndosoB.put("pv_nmpoliza_i" , nmpoliza);
						paramsConfirmarEndosoB.put("pv_nmsuplem_i" , nmsuplem);
						paramsConfirmarEndosoB.put("pv_nsuplogi_i" , nsuplogi);
						paramsConfirmarEndosoB.put("pv_cdtipsup_i" , cdtipsup);
						paramsConfirmarEndosoB.put("pv_dscoment_i" , dscoment);
						
						endososManager.confirmarEndosoB(paramsConfirmarEndosoB);
						
						estatusTramite = EstatusTramite.ENDOSO_CONFIRMADO.getCodigo();
						respuesta.setConfirmado(true);
						logger.debug("************* El Endoso fue confirmado, confirmado true");
					}
					
					Map<String,String> valores = new LinkedHashMap<String,String>();
					valores.put("otvalor01" , ntramiteEmision);
					valores.put("otvalor02" , cdtipsup);
					valores.put("otvalor03" , descEndoso);
					valores.put("otvalor04" , nsuplogi);
					valores.put("otvalor05" , ((UserVO)session.get("USUARIO")).getUser());
					
					String cdtipflu    = null
							,cdflujomc = null;
					
					Map<String,String> datosFlujoEndoso = consultasManager.recuperarDatosFlujoEndoso(cdramo,cdtipsup);
					
					cdtipflu  = datosFlujoEndoso.get("cdtipflu");
					cdflujomc = datosFlujoEndoso.get("cdflujomc");
					
					String ntramiteGenerado = mesaControlManager.movimientoTramite(
							cdunieco
							,cdramo
							,estado
							,nmpoliza
							,nmsuplem
							,cdunieco
							,cdunieco
							,TipoTramite.ENDOSO.getCdtiptra()
							,fechaHoy

							,null
							,null
							,null
							,fechaHoy

							,estatusTramite
							,dscoment
							,null
							,cdtipsit
							,((UserVO)session.get("USUARIO")).getUser()
							,((UserVO)session.get("USUARIO")).getRolActivo().getClave()
							,null //swimpres
							,cdtipflu
							,cdflujomc
							,valores
							,cdtipsup
							,null
							,null
							,null
							);
					
					boolean sinGrabarDetalle = true;
					if (!respuesta.isConfirmado()) { // Cuando no esta confirmado hay que guardar el detalle para quien autoriza
					    sinGrabarDetalle = false;
					}
					
				    respuesta.setRespuestaTurnado(despachadorManager.turnarTramite(
				            cdusuari,
				            cdsisrol,
				            ntramiteGenerado, 
				            estatusTramite,
				            respuesta.isConfirmado()
				                ? Utils.join("Se confirma el endoso ", nsuplogi)
				                : "La fecha de efecto del endoso supera la fecha permitida",
				            null,  // cdrazrecha
				            null,  // cdusuariDes
				            null,  // cdsisrolDes
				            true,  // permisoAgente
				            false, // porEscalamiento
				            fechaHoy,
				            sinGrabarDetalle
				            ));
					
					// Si fue confirmado no asignamos numero de tramite:
					if(respuesta.isConfirmado()) {
						respuesta.setNumeroTramite(null);
					} else {
						//respuesta.setNumeroTramite( (String)wr.getItemMap().get("ntramite") );
						respuesta.setNumeroTramite(ntramiteGenerado);
					}
				}
				else // SALUD CON FLUJO
				{
					paso = "Recuperando d\u00edas de endoso v\u00e1lido";
					logger.debug(paso);
					
					long numMaximoDias = (long)endososManager.recuperarDiasDiferenciaEndosoValidos(cdramo,cdtipsup);
					
					paso = "Recuperando tr\u00e1mite de emisi\u00f3n";
					logger.debug(paso);
					
					// Se almacena la diferencia entre la fecha actual y a fecha que tendra el endoso:
					Date fechaHoy=new Date();
					long diferenciaFechaActualVSEndoso = fechaHoy.getTime() - fechaEndoso.getTime();
					diferenciaFechaActualVSEndoso = Math.abs(diferenciaFechaActualVSEndoso);
					// Se almacena el maximo de dias permitidos para realizar un endoso (30 dias):
					long maximoDiasPermitidos = numMaximoDias*24l*60l*60l*1000l;
					
					logger.debug("************* diferenciaFechaActualVSEndoso=" + diferenciaFechaActualVSEndoso);
					logger.debug("************* maximoDiasPermitidos         =" + maximoDiasPermitidos);
					
					String estatusTramite = null;
					if(diferenciaFechaActualVSEndoso > maximoDiasPermitidos)
					{
						logger.debug("************* El Endoso esta en espera, confirmado false");
						estatusTramite = EstatusTramite.ENDOSO_EN_ESPERA.getCodigo();
						respuesta.setConfirmado(false);
					}
					else
					{
						// Se confirma endoso:
						Map<String,String> paramsConfirmarEndosoB = new LinkedHashMap<String,String>(0);
						paramsConfirmarEndosoB.put("pv_cdunieco_i" , cdunieco);
						paramsConfirmarEndosoB.put("pv_cdramo_i"   , cdramo);
						paramsConfirmarEndosoB.put("pv_estado_i"   , estado);
						paramsConfirmarEndosoB.put("pv_nmpoliza_i" , nmpoliza);
						paramsConfirmarEndosoB.put("pv_nmsuplem_i" , nmsuplem);
						paramsConfirmarEndosoB.put("pv_nsuplogi_i" , nsuplogi);
						paramsConfirmarEndosoB.put("pv_cdtipsup_i" , cdtipsup);
						paramsConfirmarEndosoB.put("pv_dscoment_i" , dscoment);
						
						endososManager.confirmarEndosoB(paramsConfirmarEndosoB);
						
						estatusTramite = EstatusTramite.ENDOSO_CONFIRMADO.getCodigo();
						respuesta.setConfirmado(true);
						logger.debug("************* El Endoso fue confirmado, confirmado true");
					}
                    
                    String comments = Utils.join("Endoso confirmado: ",StringUtils.isBlank(dscoment) ? "(sin comentarios)" : dscoment);
                    if (estatusTramite.equals(EstatusTramite.ENDOSO_EN_ESPERA.getCodigo())) {
                        comments = Utils.join("Endoso enviado a autorizaci\u00f3n: ",StringUtils.isBlank(dscoment) ? "(sin comentarios)" : dscoment);
                    }
					
					paso = "Actualizando estatus de tr\u00e1mite";
					logger.debug(paso);
					respuesta.setRespuestaTurnado(despachadorManager.turnarTramite(
                            cdusuari,
                            cdsisrol,
                            flujo.getNtramite(), 
                            estatusTramite,
                            comments,
                            null,  // cdrazrecha
                            null,  // cdusuariDes
                            null,  // cdsisrolDes
                            true,  // permisoAgente
                            false, // porEscalamiento
                            fechaHoy,
                            true   //sinGrabarDetalle
                            ));
					
					paso = "Guardando detalle de tr\u00e1mite de endoso";
					logger.debug(paso);
					
					mesaControlDAO.movimientoDetalleTramite(
							flujo.getNtramite()
							,new Date() //feinicio
							,null //cdclausu
							,comments
							,cdusuari
							,null //cdmotivo
							,cdsisrol
							,"S" //swagente
							,null //cdusuariDest
							,null //cdsisrolDest
							,estatusTramite
							,true //cerrado
							);
					
					paso = "Actualizar suplemento del tr\u00e1mite";
					logger.debug(paso);
					
					mesaControlManager.actualizarNmsuplemTramite(flujo.getNtramite(),nmsuplem);
					
					paso = "Actualizando atributos variables de tr\u00e1mite";
					logger.debug(paso);
					
					mesaControlDAO.actualizarOtvalorTramitePorDsatribu(
							flujo.getNtramite()
							,"MITE%EMISI"
							,endososManager.obtenerNtramiteEmision(cdunieco, cdramo, estado, nmpoliza)
							,Constantes.UPDATE_MODE
							);
					
					mesaControlDAO.actualizarOtvalorTramitePorDsatribu(
							flujo.getNtramite()
							,"CDTIPSUP"
							,cdtipsup
							,Constantes.UPDATE_MODE
							);
					
					mesaControlDAO.actualizarOtvalorTramitePorDsatribu(
							flujo.getNtramite()
							,"DSTIPSUP"
							,endososManager.obtieneDescripcionEndoso(cdtipsup)
							,Constantes.UPDATE_MODE
							);
					
					mesaControlDAO.actualizarOtvalorTramitePorDsatribu(
							flujo.getNtramite()
							,"NSUPLOGI"
							,nsuplogi
							,Constantes.UPDATE_MODE
							);
					
					mesaControlDAO.actualizarOtvalorTramitePorDsatribu(
							flujo.getNtramite()
							,"CDUSUARI"
							,cdusuari
							,Constantes.UPDATE_MODE
							);
					
					// Si fue confirmado no asignamos numero de tramite:
					if(respuesta.isConfirmado()) {
						respuesta.setNumeroTramite(null);
					} else {
						//respuesta.setNumeroTramite( (String)wr.getItemMap().get("ntramite") );
						respuesta.setNumeroTramite(flujo.getNtramite());
					}
				}
			}
			else// AUTOS
			{
				if(flujo == null) // AUTOS SIN FLUJO
				{
					paso = "Recuperando tr\u00e1mite de emisi\u00f3n";
					logger.debug(paso);
					
					// Se obtiene el numero de tramite de emision de una poliza:
					String ntramiteEmision = endososManager.obtenerNtramiteEmision(cdunieco, cdramo, estado, nmpoliza);
					
					paso = "Recuperando descripci\u00f3n de endoso";
					logger.debug(paso);
					
					String descEndoso = endososManager.obtieneDescripcionEndoso(cdtipsup); 
					
					// Se confirma endoso:
					Map<String,String> paramsConfirmarEndosoB = new LinkedHashMap<String,String>(0);
					paramsConfirmarEndosoB.put("pv_cdunieco_i" , cdunieco);
					paramsConfirmarEndosoB.put("pv_cdramo_i"   , cdramo);
					paramsConfirmarEndosoB.put("pv_estado_i"   , estado);
					paramsConfirmarEndosoB.put("pv_nmpoliza_i" , nmpoliza);
					paramsConfirmarEndosoB.put("pv_nmsuplem_i" , nmsuplem);
					paramsConfirmarEndosoB.put("pv_nsuplogi_i" , nsuplogi);
					paramsConfirmarEndosoB.put("pv_cdtipsup_i" , cdtipsup);
					paramsConfirmarEndosoB.put("pv_dscoment_i" , dscoment);
					
					endososManager.confirmarEndosoB(paramsConfirmarEndosoB);
					
					String estatusTramite = EstatusTramite.ENDOSO_CONFIRMADO.getCodigo();
					respuesta.setConfirmado(true);
					logger.debug("************* El Endoso fue confirmado, confirmado true");
					
					Map<String,String> valores = new LinkedHashMap<String,String>();
					valores.put("otvalor01" , ntramiteEmision);
					valores.put("otvalor02" , cdtipsup);
					valores.put("otvalor03" , descEndoso);
					valores.put("otvalor04" , nsuplogi);
					valores.put("otvalor05" , ((UserVO)session.get("USUARIO")).getUser());
					
					String cdtipflu    = null
							,cdflujomc = null;
					
					Map<String,String> datosFlujoEndoso = consultasManager.recuperarDatosFlujoEndoso(cdramo,cdtipsup);
					
					cdtipflu  = datosFlujoEndoso.get("cdtipflu");
					cdflujomc = datosFlujoEndoso.get("cdflujomc");
					
					Date fechaHoy = new Date();

					String ntramiteGenerado = mesaControlManager.movimientoTramite(
							cdunieco
							,cdramo
							,estado
							,nmpoliza
							,nmsuplem
							,cdunieco
							,cdunieco
							,TipoTramite.ENDOSO.getCdtiptra()
							,fechaHoy

							,null
							,null
							,null
							,fechaHoy

							,estatusTramite
							,dscoment
							,null
							,cdtipsit
							,((UserVO)session.get("USUARIO")).getUser()
							,((UserVO)session.get("USUARIO")).getRolActivo().getClave()
							,null //swimpres
							,cdtipflu
							,cdflujomc
							,valores
							,cdtipsup
							,null
							,null
							,null
							);
					
					respuesta.setRespuestaTurnado(despachadorManager.turnarTramite(
					        cdusuari,
					        cdsisrol,
					        ntramiteGenerado,
					        estatusTramite,
					        dscoment,
					        null,  // cdrazrecha,
					        null,  // cdusuariDes,
					        null,  // cdsisrolDes,
					        true,  // permisoAgente,
					        false, // porEscalamiento,
					        fechaHoy,
					        false  // sinGrabarDetalle
					        ));
					
					// Si fue confirmado no asignamos numero de tramite:
					if(respuesta.isConfirmado()) {
						respuesta.setNumeroTramite(null);
					} else {
						//respuesta.setNumeroTramite( (String)wr.getItemMap().get("ntramite") );
						respuesta.setNumeroTramite(ntramiteGenerado);
					}
				}
				else // AUTOS CON FLUJO
				{
				    Date fechaHoy = new Date();
				    
					paso = "Confirmando endoso de flujo";
					logger.debug(paso);
				
					endososManager.confirmarEndosoB(
							cdunieco
							,cdramo
							,estado
							,nmpoliza
							,nmsuplem
							,nsuplogi
							,cdtipsup
							,dscoment
							);
					
					String comments = Utils.join("Endoso confirmado: ",StringUtils.isBlank(dscoment) ? "(sin comentarios)" : dscoment);
                    
                    paso = "Actualizando estatus de tr\u00e1mite";
                    logger.debug(paso);
                    respuesta.setRespuestaTurnado(despachadorManager.turnarTramite(
                            cdusuari,
                            cdsisrol,
                            flujo.getNtramite(), 
                            EstatusTramite.ENDOSO_CONFIRMADO.getCodigo(),
                            comments,
                            null,  // cdrazrecha
                            null,  // cdusuariDes
                            null,  // cdsisrolDes
                            true,  // permisoAgente
                            false, // porEscalamiento
                            fechaHoy,
                            true   //sinGrabarDetalle
                            ));
					
					paso = "Guardando detalle de tr\u00e1mite de endoso";
					logger.debug(paso);
					
					mesaControlDAO.movimientoDetalleTramite(
							flujo.getNtramite()
							,fechaHoy //feinicio
							,null //cdclausu
							,comments
							,cdusuari
							,null //cdmotivo
							,cdsisrol
							,"S" //swagente
							,null //cdusuariDest
							,null //cdsisrolDest
							,EstatusTramite.ENDOSO_CONFIRMADO.getCodigo()
							,true //cerrado
							);
					
					paso = "Actualizar suplemento del tr\u00e1mite";
					logger.debug(paso);
					
					mesaControlManager.actualizarNmsuplemTramite(flujo.getNtramite(),nmsuplem);
					
					paso = "Actualizando atributos variables de tr\u00e1mite";
					logger.debug(paso);
					
					mesaControlDAO.actualizarOtvalorTramitePorDsatribu(
							flujo.getNtramite()
							,"MITE%EMISI"
							,endososManager.obtenerNtramiteEmision(cdunieco, cdramo, estado, nmpoliza)
							,Constantes.UPDATE_MODE
							);
					
					mesaControlDAO.actualizarOtvalorTramitePorDsatribu(
							flujo.getNtramite()
							,"CDTIPSUP"
							,cdtipsup
							,Constantes.UPDATE_MODE
							);
					
					mesaControlDAO.actualizarOtvalorTramitePorDsatribu(
							flujo.getNtramite()
							,"DSTIPSUP"
							,endososManager.obtieneDescripcionEndoso(cdtipsup)
							,Constantes.UPDATE_MODE
							);
					
					mesaControlDAO.actualizarOtvalorTramitePorDsatribu(
							flujo.getNtramite()
							,"NSUPLOGI"
							,nsuplogi
							,Constantes.UPDATE_MODE
							);
					
					mesaControlDAO.actualizarOtvalorTramitePorDsatribu(
							flujo.getNtramite()
							,"CDUSUARI"
							,cdusuari
							,Constantes.UPDATE_MODE
							);
					
					respuesta.setConfirmado(true);
				}
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}

		logger.debug(Utils.log(
				 "\n###### respuesta = ", respuesta
				,"\n###### confirmarEndoso ######"
				,"\n#############################"
				));
		
	    return respuesta;
	}
	/*//////////////////////////*/
	////// confirmar endoso //////
	//////////////////////////////
	
	//////////////////////////////
	////// autorizar endoso //////
	/*//////////////////////////*/
	public String autorizarEndoso()
	{
		logger.debug(Utils.log(
				 "\n#############################"
				,"\n###### autorizarEndoso ######"
				,"\n###### smap1 = " , smap1
				,"\n###### flujo = " , flujo
				));
		try
		{
			String cdunieco      = null
					,cdramo      = null
					,estado      = null
					,nmpoliza    = null
					,nmsuplem    = null
					,nsuplogi    = null
					,cdtipsup    = null
					,ntramiteEmi = null
					,ntramiteEnd = null
					,status      = null
					,coment      = null;

			Date fechaHoy = new Date();
			
			UserVO usuario = Utils.validateSession(session);
			
			String cdusuari = usuario.getUser();
			
			String cdsisrol = usuario.getRolActivo().getClave();
			
			if(flujo!=null)
			{
				logger.debug("Viene por flujo y se recuperan los datos del flujo");
				
				cdunieco = flujo.getCdunieco();
				cdramo   = flujo.getCdramo();
				estado   = flujo.getEstado();
				nmpoliza = flujo.getNmpoliza();
				
				nmsuplem = flujoMesaControlManager.obtenerSuplementoTramite(flujo.getNtramite());
				
				if (StringUtils.isBlank(nmsuplem) || "-1".equals(nmsuplem)) {
				    throw new ApplicationException("Error al recuperar suplemento de endoso");
				}
				
				Map<String,Object> datos = flujoMesaControlDAO.recuperarDatosTramiteValidacionCliente(
						flujo.getCdtipflu()
						,flujo.getCdflujomc()
						,flujo.getTipoent()
						,flujo.getClaveent()
						,flujo.getWebid()
						,flujo.getNtramite()
						,flujo.getStatus()
						,flujo.getCdunieco()
						,flujo.getCdramo()
						,flujo.getEstado()
						,flujo.getNmpoliza()
						,flujo.getNmsituac()
						,flujo.getNmsuplem()
						);
				
				Map<String,String> tramite = (Map<String,String>)datos.get("TRAMITE");
				
				nsuplogi = tramite.get("OTVALOR04");
				
				cdtipsup = tramite.get("OTVALOR02");
				
				ntramiteEmi = tramite.get("OTVALOR01");
				
				ntramiteEnd = flujo.getNtramite();
				
				status = EstatusTramite.ENDOSO_CONFIRMADO.getCodigo();
				
				coment = flujo.getAux();
			}
			else
			{
				cdunieco    = smap1.get("cdunieco");
				cdramo      = smap1.get("cdramo");
				estado      = smap1.get("estado");
				nmpoliza    = smap1.get("nmpoliza");
				nmsuplem    = smap1.get("nmsuplem");
				nsuplogi    = smap1.get("nsuplogi");
				cdtipsup    = smap1.get("cdtipsup");
				ntramiteEmi = smap1.get("ntramiteemi");
				ntramiteEnd = smap1.get("ntramiteend");
				status      = smap1.get("status");
				coment      = smap1.get("observacion");
				//cdtipsit    = smap1.get("cdtipsit");
			}
			
			logger.debug(Utils.log(
					 "\ncdunieco    = " , cdunieco
					,"\ncdramo      = " , cdramo
					,"\nestado      = " , estado
					,"\nnmpoliza    = " , nmpoliza
					,"\nnmsuplem    = " , nmsuplem
					,"\nnsuplogi    = " , nsuplogi
					,"\ncdtipsup    = " , cdtipsup
					,"\nntramiteEmi = " , ntramiteEmi
					,"\nntramiteEnd = " , ntramiteEnd
					,"\nstatus      = " , status
					,"\ncoment      = " , coment
					));
			
			Map<String,String>paramConfirmarEndosoB=new LinkedHashMap<String,String>(0);
			paramConfirmarEndosoB.put("pv_cdunieco_i" , cdunieco);
			paramConfirmarEndosoB.put("pv_cdramo_i"   , cdramo);
			paramConfirmarEndosoB.put("pv_estado_i"   , estado);
			paramConfirmarEndosoB.put("pv_nmpoliza_i" , nmpoliza);
			paramConfirmarEndosoB.put("pv_nmsuplem_i" , nmsuplem);
			paramConfirmarEndosoB.put("pv_nsuplogi_i" , nsuplogi);
			paramConfirmarEndosoB.put("pv_cdtipsup_i" , cdtipsup);
			paramConfirmarEndosoB.put("pv_dscoment_i" , coment);
			endososManager.confirmarEndosoB(paramConfirmarEndosoB);
			String nmsolici = null;
			String rutaCarpeta = null;
			
			RespuestaTurnadoVO despacho = despachadorManager.turnarTramite(
			        cdusuari,
			        cdsisrol,
			        ntramiteEnd,
			        status,
			        "Se autoriza endoso", // comments
			        null,  // cdrazrecha
			        null,  // cdusuariDes
			        null,  // cdsisrolDes
			        true,  // permisoAgente
			        false, // porEscalamiento 
			        fechaHoy,
			        true  // sinGrabarDetalle
			        );
			
			message = despacho.getMessage();
			
			mesaControlDAO.movimientoDetalleTramite(
					flujo.getNtramite()
					,fechaHoy //feinicio
					,null //cdclausu
					,StringUtils.isBlank(coment) ? "Endoso autorizado sin observaciones" : Utils.join("Endoso autorizado: ",coment)
					,cdusuari
					,null //cdmotivo
					,cdsisrol
					,"S" //swagente
					,null //cdusuariDest
					,null //cdsisrolDest
					,EstatusTramite.ENDOSO_CONFIRMADO.getCodigo()
					,true //cerrado
					);
			
			///////////////////////////////////////
			///// Generacion de Documentos ///////
			///////////////////////////////////////
			Map<String,String> datosPoliza = documentosManager.generarDocumentosParametrizados(
				cdunieco
				,cdramo
				,estado
				,nmpoliza
				,"0"
				,nmsuplem
				,DocumentosManager.PROCESO_ENDOSO
				,ntramiteEmi
				,null
				,null
			);
			nmsolici    = datosPoliza.get("nmsolici");
			rutaCarpeta = Utils.join(this.rutaDocumentosPoliza,"/",ntramiteEmi);
			
			String sucursal = cdunieco;
			
			//Creamos un enum en base al tipo de endoso enviado: 
			TipoEndoso enumTipoEndoso = null;
			for (TipoEndoso te : TipoEndoso.values()) {
			    if( cdtipsup.equals(te.getCdTipSup().toString()) ) {
			    	enumTipoEndoso = te;
			    	break;
			    }
			}
			
			String saludDanios = "S";
			ClienteGeneral clienteGeneral = new ClienteGeneral();
			clienteGeneral.setClaveCia(saludDanios);
			
			switch (enumTipoEndoso) {
			case CORRECCION_DATOS_ASEGURADOS:
			case CORRECCION_NOMBRE_Y_RFC:
			case CAMBIO_DOMICILIO:
				
				// Ejecutamos el Web Service de Cliente Salud:
				ice2sigsService.ejecutaWSclienteGeneral(cdunieco, cdramo, estado, nmpoliza, nmsuplem, ntramiteEnd, null, Ice2sigsService.Operacion.ACTUALIZA, clienteGeneral, (UserVO) session.get("USUARIO"), false);
				
				break;
							
			case ALTA_COBERTURAS:
			case BAJA_COBERTURAS:
			case ALTA_ASEGURADOS:
			case BAJA_ASEGURADOS:
			case DEDUCIBLE_MAS:
			case DEDUCIBLE_MENOS:
			case COPAGO_MAS:
			case COPAGO_MENOS:
			case EXTRAPRIMA_MAS:
			case EXTRAPRIMA_MENOS:
			case CAMBIO_FORMA_PAGO:
			case CAMBIO_AGENTE:
			case CANCELACION_POR_REEXPEDICION:
			case CAMBIO_CONTRATANTE:
			case DESPAGO:
			case COASEGURO_INCREMENTO:
			case COASEGURO_DECREMENTO:
			case SUMA_ASEGURADA_INCREMENTO:
			case SUMA_ASEGURADA_DECREMENTO:
			case REHABILITACION:
			case REHABILITACION_ENDOSO:
			case CIRCULO_HOSPITALARIO_COASEGURO_INCREMENTO:
			case CIRCULO_HOSPITALARIO_COASEGURO_DECREMENTO:
			case AMPLIACION_DE_VIGENCIA:
			case VIGENCIA_POLIZA:
			case REHABILITACION_DESPAGO:
			case CANCELACION_UNICA:
			case CANCELACION_MASIVA:
				// Ejecutamos el Web Service de Recibos:
				ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo, estado, nmpoliza, nmsuplem, 
						rutaCarpeta, sucursal, nmsolici, ntramiteEmi, true, cdtipsup, 
						(UserVO) session.get("USUARIO"));
				break;
			case INCREMENTO_EDAD_ASEGURADO:
			case DECREMENTO_EDAD_ASEGURADO:
			case MODIFICACION_SEXO_H_A_M:
			case MODIFICACION_SEXO_M_A_H:
			case CAMBIO_DOMICILIO_ASEGURADO_TITULAR:

				// Ejecutamos el Web Service de Cliente Salud:
				ice2sigsService.ejecutaWSclienteGeneral(cdunieco, cdramo, estado, nmpoliza, nmsuplem, ntramiteEnd, null, Ice2sigsService.Operacion.ACTUALIZA, clienteGeneral, (UserVO) session.get("USUARIO"), false);
				
				// Ejecutamos el Web Service de Recibos:
				ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo, 
						estado, nmpoliza, 
						nmsuplem, rutaCarpeta, 
						sucursal, nmsolici, ntramiteEmi, 
						true, cdtipsup, 
						(UserVO) session.get("USUARIO"));
				break;
			default:
				logger.debug("**** NO HAY WEB SERVICE PARA CDTIPSUP " + cdtipsup + " ******");
				break;
		}
			
			success=true;
		}
		catch(Exception ex)
		{
			error   = Utils.manejaExcepcion(ex);
			message = error;
		}
		
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### error   = " , error
				,"\n###### message = " , message
				,"\n###### autorizarEndoso ######"
				,"\n#############################"
				));
		
		return SUCCESS;
	}
	/*//////////////////////////*/
	////// autorizar endoso //////
	//////////////////////////////
	
	///////////////////////////////////
	////// endoso domicilio full //////
	/*
	smap1:
	    cdrfc=MAVA900817001,
	    cdperson=511965,
	    fenacimi=1990-08-17T00:00:00,
	    sexo=H,
	    Apellido_Materno=MAT,
	    nombre=NOMBRE1,
	    nombrecompleto=NOMBRE1  PAT MAT,
	    nmsituac=1,
	    segundo_nombre=null,
	    Parentesco=T,
	    CDTIPSIT=SL,
	    NTRAMITE=615,
	    CDUNIECO=1006,
	    CDRAMO=2,
	    NMSUPLEM=245673812540000005,
	    NMPOLIZA=14,
	    swexiper=S,
	    NMPOLIEX=1006213000014000000,
	    nacional=001,
	    activo=true,
	    NSUPLOGI=8,
	    ESTADO=M,
	    cdrol=2,
	    tpersona=F,
	    Apellido_Paterno=PAT
	*/
	/*///////////////////////////////*/
	public String endosoDomicilioFull() {
		
		if(!smap1.containsKey("cdperson") && smap1.containsKey("CDPERSON")){
			String cdtipsit1 = null;
			
			try {
				cdtipsit1 = endososManager.recuperarCdtipsitInciso1((String)smap1.get("CDUNIECO"), (String)smap1.get("CDRAMO"), (String)smap1.get("ESTADO"), (String)smap1.get("NMPOLIZA"));
			} catch (Exception e) {
				logger.error("Error al obtener el cdtipsit de la poliza para el primer inciso",e);
			}
			
			smap1.put("pv_nmpoliza", smap1.get("NMPOLIZA"));
			smap1.put("pv_estado", smap1.get("ESTADO"));
			smap1.put("cdrfc", smap1.get("CDRFC"));
			smap1.put("pv_nmsituac", null);
			smap1.put("pv_cdrol", null);
			smap1.put("pv_cdunieco", smap1.get("CDUNIECO"));
			smap1.put("ntramite", smap1.get("NTRAMITE"));
			smap1.put("pv_cdramo", smap1.get("CDRAMO"));
			smap1.put("cdtipsit", cdtipsit1);
			smap1.put("CDTIPSIT", cdtipsit1);
			
		}
		
		logger.debug("endosoDomicilioFull()");
		logger.debug(new StringBuilder("\n")
	        .append("\n#################################")
	        .append("\n#################################")
	        .append("\n###### endosoDomicilioFull ######")
	        .append("\n######                     ######").toString());
		logger.debug(new StringBuilder("smap1: ").append(smap1).toString());
		logger.debug(new StringBuilder("slist1: ").append(slist1).toString());
		
		this.session=ActionContext.getContext().getSession();
		
		RespuestaVO resp = null;
		
		try
		{
			transformaEntrada(smap1, slist1, true);
			
			// Valida si hay un endoso anterior pendiente:
			resp = endososManager.validaEndosoAnterior(smap1.get("CDUNIECO"),smap1.get("CDRAMO"),
					smap1.get("ESTADO"), smap1.get("NMPOLIZA"),
					TipoEndoso.CAMBIO_DOMICILIO_ASEGURADO_TITULAR.getCdTipSup().toString());
			error = resp.getMensaje();
			
			if(resp.isSuccess()){
				try {
					UserVO usuario=(UserVO)session.get("USUARIO");
					
					String cdunieco = smap1.get("CDUNIECO");
					String cdramo   = smap1.get("CDRAMO");
					String cdtipsit = smap1.get("CDTIPSIT");
					String estado   = smap1.get("ESTADO");
					String nmpoliza = smap1.get("NMPOLIZA");
					String nmsuplem = smap1.get("NMSUPLEM");
					String rol      = usuario.getRolActivo().getClave();
					String rolAsegu = smap1.get("cdrol");
					
					GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
					
					imap1=new HashMap<String,Item>();
					
					gc.generaParcial(pantallasManager.obtenerComponentes(
							null, cdunieco, cdramo,
							cdtipsit, estado, rol,
							"ENDOSO_DOMICILIO_FULL", "PANEL_LECTURA", null));
								
					imap1.put("itemsLectura",gc.getItems());
					
					gc.generaParcial(pantallasManager.obtenerComponentes(
							null, cdunieco, cdramo,
							cdtipsit, estado, rol,
							"ENDOSO_DOMICILIO_FULL", "ITEMS_DOMICIL", null));
					
					imap1.put("itemsDomicil"  , gc.getItems());
					imap1.put("fieldsDomicil" , gc.getFields());
					
				} catch(Exception ex) {
					logger.error("Error al cargar la pantalla de domicilio full",ex);
				}
			}
		}
		catch(Exception ex)
		{
			logger.error("Error al construir pantalla de endoso de domicilio de titular",ex);
			error = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(new StringBuilder("\n")
	        .append("\n######                     ######")
	        .append("\n###### endosoDomicilioFull ######")
	        .append("\n#################################")
	        .append("\n#################################").toString());
		
		return resp!=null&&resp.isSuccess() ? SUCCESS : ERROR;
	}
	///////////////////////////////////
	////// endoso domicilio full //////
	/*///////////////////////////////*/
	/*///////////////////////////////*/
	public String endosoDomicilioAutoFull() {
		
		logger.debug("endosoDomicilioFull()");
		logger.debug(new StringBuilder("\n")
		.append("\n#####################################")
		.append("\n#####################################")
		.append("\n###### endosoDomicilioAutoFull ######")
		.append("\n######                         ######").toString());
		
		try{
			
			LinkedHashMap<String,Object>paramsValues=new LinkedHashMap<String,Object>();
			paramsValues.put("param1", smap1.get("CDUNIECO"));
			paramsValues.put("param2", smap1.get("CDRAMO"));
			paramsValues.put("param3", smap1.get("ESTADO"));
			paramsValues.put("param4", smap1.get("NMPOLIZA"));
			paramsValues.put("param5", "0");// nmsituac contrantante auto
			paramsValues.put("param6", "1");// rol para contratante
			paramsValues.put("param7", null);
			
			List<Map<String,String>> datosContr=storedProceduresManager.procedureListCall(
					ObjetoBD.OBTIENE_MPOLIPER.getNombre(), paramsValues, null);
			if(datosContr!=null && !datosContr.isEmpty() && datosContr.get(0) != null)
			{
				Map<String,String> datos = datosContr.get(0);
				smap1.put("nmsituac", datos.get("NMSITUAC"));
				smap1.put("cdperson", datos.get("CDPERSON"));
				smap1.put("cdrol"   , datos.get("CDROL"));
			}else{
			    message = "Error al obtener datos de contratante para pantalla de endoso de domicilio autos";
				logger.error(message);
				return ERROR;
			}
			
		}catch(Exception e){
			logger.error("Error al obtener datos de contratante para pantalla de endoso de domicilio autos",e);
			return ERROR;
		}
		
		
		logger.debug(new StringBuilder("smap1: ").append(smap1).toString());
		
		this.session=ActionContext.getContext().getSession();
		
		// Valida si hay un endoso anterior pendiente:
		RespuestaVO resp = endososManager.validaEndosoAnterior(smap1.get("CDUNIECO"),smap1.get("CDRAMO"),
				smap1.get("ESTADO"), smap1.get("NMPOLIZA"),
				TipoEndoso.CAMBIO_DOMICILIO_ASEGURADO_TITULAR.getCdTipSup().toString());
		error = resp.getMensaje();
		
		if(resp.isSuccess()){
			try {
				UserVO usuario=(UserVO)session.get("USUARIO");
				
				String cdunieco = smap1.get("CDUNIECO");
				String cdramo   = smap1.get("CDRAMO");
				String cdtipsit = smap1.get("CDTIPSIT");
				String estado   = smap1.get("ESTADO");
				String rol      = usuario.getRolActivo().getClave();
				
				GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
				
				imap1=new HashMap<String,Item>();
				
				gc.generaParcial(pantallasManager.obtenerComponentes(
						null, cdunieco, cdramo,
						cdtipsit, estado, rol,
						"ENDOSO_DOMICILIO_FULL", "PANEL_LECTURA", null));
				
				imap1.put("itemsLectura",gc.getItems());
				
				gc.generaParcial(pantallasManager.obtenerComponentes(
						null, cdunieco, cdramo,
						cdtipsit, estado, rol,
						"ENDOSO_DOMICILIO_FULL", "ITEMS_DOMICIL", null));
				
				imap1.put("itemsDomicil"  , gc.getItems());
				imap1.put("fieldsDomicil" , gc.getFields());
				
				String cdtipsit1 = endososManager.recuperarCdtipsitInciso1(cdunieco, cdramo, estado, smap1.get("NMPOLIZA"));
				
				smap1.put("CDTIPSIT1",cdtipsit1);
				
			} catch(Exception ex) {
				logger.error("Error al cargar la pantalla de domicilio full",ex);
			}
		}
		
		logger.debug(new StringBuilder("\n")
		.append("\n######                         ######")
		.append("\n###### endosoDomicilioAutoFull ######")
		.append("\n#####################################")
		.append("\n#####################################").toString());
		
		return resp.isSuccess() ? SUCCESS : ERROR;
	}
	///////////////////////////////////
	////// endoso domicilio full //////
	/*///////////////////////////////*/
	
	////////////////////////////////////////
	////// guardarEndosoDomicilioFull //////
	/*
	smap1:
	    cdrfc=MAVA900817001,
	    cdperson=511965,
	    fenacimi=1990-08-17T00:00:00,
	    sexo=H,
	    Apellido_Materno=MAT,
	    nombre=NOMBRE1,
	    nombrecompleto=NOMBRE1  PAT MAT,
	    nmsituac=1,
	    segundo_nombre=null,
	    Parentesco=T,
	    CDTIPSIT=SL,
	    NTRAMITE=615,
	    CDUNIECO=1006,
	    CDRAMO=2,
	    NMSUPLEM=245673812540000005,
	    NMPOLIZA=14,
	    swexiper=S,
	    NMPOLIEX=1006213000014000000,
	    nacional=001,
	    activo=true,
	    NSUPLOGI=8,
	    ESTADO=M,
	    cdrol=2,
	    tpersona=F,
	    Apellido_Paterno=PAT
	smap2:
	    CDCOLONI: "137617"
	    CDEDO: "9600030"
	    CDMUNICI: "9600030003"
	    CODPOSTAL: "96000"
	    DSDOMICI: "FLORES"
	    NMNUMERO: "ROSAS"
	    NMNUMINT: "1"
	    NMORDDOM: "1"
	    NMTELEFO: "AA"
	*/
	/*////////////////////////////////////*/
	public String guardarEndosoDomicilioFull()
	{
		this.session = ActionContext.getContext().getSession();
		
		logger.debug(Utils.log(
				 "\n########################################"
				,"\n###### guardarEndosoDomicilioFull ######"
				,"\n###### smap1      = " , smap1
				,"\n###### smap2      = " , smap2
				,"\n###### smap3      = " , smap3
				,"\n###### parametros = " , parametros
				,"\n###### flujo      = " , flujo
				));
		
		try
		{
			UserVO usuario      = (UserVO) session.get("USUARIO");
			String cdelemento   = usuario.getEmpresa().getElementoId();
			String cdusuari     = usuario.getUser();
			String cdsisrol     = usuario.getRolActivo().getClave();
			String cdunieco     = smap1.get("CDUNIECO");
			String cdramo       = smap1.get("CDRAMO");
			String estado       = smap1.get("ESTADO");
			String nmpoliza     = smap1.get("NMPOLIZA");
			String sFechaEndoso = smap3.get("fecha_endoso");
			Date   dFechaEndoso = renderFechas.parse(sFechaEndoso);
			String cdtipsit     = smap1.get("CDTIPSIT");
			
			String cdpersonCli = smap1.get("CDPERSON");
			String cdpersonAsegTitular = smap1.get("cdperson");
			
			String nmordom      = smap2.get("NMORDDOM");
			String cdtipdom     = smap2.get("CDTIPDOM");
			String dsdomici     = smap2.get("DSDOMICI");
			String nmtelefo     = smap2.get("NMTELEFO");
			String cdpostal     = smap2.get("CODPOSTAL");
			String cdestado     = smap2.get("CDEDO");
			String cdmunici     = smap2.get("CDMUNICI");
			String cdcoloni     = smap2.get("CDCOLONI");
			String nmnumext     = smap2.get("NMNUMERO");
			String nmnumint     = smap2.get("NMNUMINT");
			String cdtipsup     = TipoEndoso.CAMBIO_DOMICILIO_ASEGURADO_TITULAR.getCdTipSup().toString();
			String proceso      = "END";
			String ntramite     = smap1.get("NTRAMITE");
			
			String nmsituac = "0"; //valor default nmsituac todas
			
			if(smap1.containsKey("nmsituac") && StringUtils.isNotBlank(smap1.get("nmsituac"))){
				nmsituac = smap1.get("nmsituac");
			}
			
			boolean esPolizaColectiva  = (StringUtils.isNotBlank(smap1.get("TIPOFLOT")) && "F".equalsIgnoreCase(smap1.get("TIPOFLOT")));//flotilla o Colectiva
			
			/**
			 * Para determinar si una poliza es contributoria o no contributoria, PCPGOCTE en 0 pagan titulares,
			 * entre 0 y 100 se divide entre contratante y titulares, 100 paga contratante
			 * si no trae valor por default se considera que solo paga el contratante.
			 */
			boolean soloPagaContratante = (StringUtils.isBlank(smap1.get("PCPGOCTE")) || "100".equalsIgnoreCase(smap1.get("PCPGOCTE")));
			
			String usuarioCaptura =  null;
			
			if(usuario!=null){
				if(StringUtils.isNotBlank(usuario.getClaveUsuarioCaptura())){
					usuarioCaptura = usuario.getClaveUsuarioCaptura();
				}else{
					usuarioCaptura = usuario.getCodigoPersona();
				}
				
			}
			/**
			 * Validar que el Codigo Postal pertenezca al Estado correcto
			 */
			if(smap1!=null&&smap1.size()>0 && smap1.containsKey("CDTIPSIT") && TipoSituacion.MULTISALUD.getCdtipsit().equalsIgnoreCase(smap1.get("CDTIPSIT"))){
				HashMap<String,String> params =  new HashMap<String, String>();
				params.put("pv_estado_i", smap2.get("CDEDO"));
				params.put("pv_codpos_i", smap2.get("CODPOSTAL"));
				endososManager.validaEstadoCodigoPostal(params);
			}
			
			//PKG_ENDOSOS.P_ENDOSO_INICIA
			Map<String,String> resIniEnd=endososManager.iniciarEndoso(cdunieco, cdramo, estado
					,nmpoliza, sFechaEndoso, cdelemento, cdusuari, proceso, cdtipsup);
			
			String nmsuplem = resIniEnd.get("pv_nmsuplem_o");
			String nsuplogi = resIniEnd.get("pv_nsuplogi_o");
			
			
			if(!esPolizaColectiva){
				String keyCodPostal = "";
				String keyEstado    = "";
				String keyMunicipio = "";
				if(cdtipsit.equalsIgnoreCase("SL")||cdtipsit.equalsIgnoreCase("SN")) {
					keyCodPostal = "pv_otvalor03";
					keyEstado    = "pv_otvalor04";
					keyMunicipio = "pv_otvalor17";

				}
				//PKG_CONSULTA.P_OBT_VALOSIT_ULTIMA_IMAGEN
				List<Map<String,String>>valositsPoliza=endososManager.obtenerValositUltimaImagen(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
				/*
				CDUNIECO,CDRAMO,ESTADO,NMPOLIZA,NMSITUAC,NMSUPLEM,STATUS,CDTIPSIT,OTVALOR01,OTVALOR02
				,OTVALOR03,OTVALOR04,OTVALOR05,OTVALOR06,OTVALOR07,OTVALOR08,OTVALOR09,OTVALOR10,OTVALOR11
				,OTVALOR12,OTVALOR13,OTVALOR14,OTVALOR15,OTVALOR16,OTVALOR17,OTVALOR18,OTVALOR19,OTVALOR20
				,OTVALOR21,OTVALOR22,OTVALOR23,OTVALOR24,OTVALOR25,OTVALOR26,OTVALOR27,OTVALOR28,OTVALOR29
				,OTVALOR30,OTVALOR31,OTVALOR32,OTVALOR33,OTVALOR34,OTVALOR35,OTVALOR36,OTVALOR37,OTVALOR38
				,OTVALOR39,OTVALOR40,OTVALOR41,OTVALOR42,OTVALOR43,OTVALOR44,OTVALOR45,OTVALOR46,OTVALOR47
				,OTVALOR48,OTVALOR49,OTVALOR50
				*/
				
				for(Map<String,String>valositIte:valositsPoliza) {
					String nmsituacIte=valositIte.get("NMSITUAC");
					
					//otvalor05 -> pv_otvalor05
					Map<String,String>otvalorValositIte=new HashMap<String,String>();
					for(Entry<String,String>en:valositIte.entrySet()) {
						String key   = en.getKey();
						String value = en.getValue();
						
						if(key.substring(0,5).equalsIgnoreCase("otval")) {
							otvalorValositIte.put("pv_"+(key.toLowerCase()),value);
						}
					}
					
					otvalorValositIte.put(keyCodPostal , cdpostal);
					otvalorValositIte.put(keyEstado    , cdestado);
					otvalorValositIte.put(keyMunicipio , cdmunici);
					
					//PKG_SATELITES2.P_MOV_TVALOSIT
					kernelManager.insertaValoresSituaciones(cdunieco, cdramo, estado, nmpoliza
							,nmsituacIte, nmsuplem, Constantes.STATUS_VIVO, cdtipsit, Constantes.INSERT_MODE, otvalorValositIte);
				}
				
				//PKG_SATELITES.P_OBT_DATOS_MPOLIPER
				/* nmsituac,cdrol,fenacimi,sexo,cdperson,nombre
				 * ,segundo_nombre,Apellido_Paterno ,Apellido_Materno
				 * ,cdrfc,Parentesco,tpersona,nacional,swexiper
				 */
				
				
				Map<String,String>paramDomicilAsegTitular=new LinkedHashMap<String,String>(0);
				paramDomicilAsegTitular.put("pv_cdperson_i" , cdpersonAsegTitular);
				paramDomicilAsegTitular.put("pv_nmorddom_i" , StringUtils.isNotBlank(nmordom)? nmordom : "1"); 
				paramDomicilAsegTitular.put("pv_msdomici_i" , dsdomici);
				paramDomicilAsegTitular.put("pv_nmtelefo_i" , nmtelefo);
				paramDomicilAsegTitular.put("pv_cdpostal_i" , cdpostal);
				paramDomicilAsegTitular.put("pv_cdedo_i"    , cdestado);
				paramDomicilAsegTitular.put("pv_cdmunici_i" , cdmunici);
				paramDomicilAsegTitular.put("pv_cdcoloni_i" , cdcoloni);
				paramDomicilAsegTitular.put("pv_nmnumero_i" , nmnumext);
				paramDomicilAsegTitular.put("pv_nmnumint_i" , nmnumint);
				paramDomicilAsegTitular.put("pv_cdtipdom_i" , cdtipdom);
				paramDomicilAsegTitular.put("pv_cdusuario_i", usuarioCaptura);
				paramDomicilAsegTitular.put("pv_swactivo_i",  Constantes.SI);
				paramDomicilAsegTitular.put("pv_accion_i"   , Constantes.UPDATE_MODE);			
				kernelManager.pMovMdomicil(paramDomicilAsegTitular);
				
				
				List<Map<String,Object>>mpoliperPoliza=kernelManager.obtenerAsegurados(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
				for(Map<String,Object>mpoliperIte:mpoliperPoliza) {
					String cdpersonIte=(String)mpoliperIte.get("cdperson");
					String cdrolIte=(String)mpoliperIte.get("cdrol");
					
					//////////////////////
					////// mdomicil //////
					//PKG_SATELITES.P_MOV_MDOMICIL
					/*//////////////////*/
					
					if(StringUtils.isNotBlank(cdrolIte) && !cdrolIte.equalsIgnoreCase("1")){
						Map<String,String>paramDomicilIte=new LinkedHashMap<String,String>(0);
						paramDomicilIte.put("pv_cdperson_i" , cdpersonIte);
						paramDomicilIte.put("pv_nmorddom_i" , "1"); //Domicilio default para asegurados 
						paramDomicilIte.put("pv_msdomici_i" , dsdomici);
						paramDomicilIte.put("pv_nmtelefo_i" , nmtelefo);
						paramDomicilIte.put("pv_cdpostal_i" , cdpostal);
						paramDomicilIte.put("pv_cdedo_i"    , cdestado);
						paramDomicilIte.put("pv_cdmunici_i" , cdmunici);
						paramDomicilIte.put("pv_cdcoloni_i" , cdcoloni);
						paramDomicilIte.put("pv_nmnumero_i" , nmnumext);
						paramDomicilIte.put("pv_nmnumint_i" , nmnumint);
						paramDomicilIte.put("pv_cdtipdom_i" , cdtipdom);
						paramDomicilIte.put("pv_cdusuario_i", usuarioCaptura);
						paramDomicilIte.put("pv_swactivo_i",  Constantes.SI);
						paramDomicilIte.put("pv_accion_i"   , Constantes.UPDATE_MODE);			
						kernelManager.pMovMdomicil(paramDomicilIte);
					}
					
					/*//////////////////*/
					////// mdomicil //////
					//////////////////////
				}
				
				//////////////////////////////
	            ////// inserta tworksup //////
	            Map<String,String>mapaTworksupEnd=new LinkedHashMap<String,String>(0);
	            mapaTworksupEnd.put("pv_cdunieco_i" , cdunieco);
	            mapaTworksupEnd.put("pv_cdramo_i"   , cdramo);
	            mapaTworksupEnd.put("pv_estado_i"   , estado);
	            mapaTworksupEnd.put("pv_nmpoliza_i" , nmpoliza);
	            mapaTworksupEnd.put("pv_cdtipsup_i" , cdtipsup);
	            mapaTworksupEnd.put("pv_nmsuplem_i" , nmsuplem);
	            endososManager.insertarTworksupSitTodas(mapaTworksupEnd);
	            ////// inserta tworksup //////
	            //////////////////////////////
	            
	            //////////////////////////
	            ////// tarificacion //////
				endososManager.sigsvalipolEnd(
						cdusuari
						,cdelemento
						,cdunieco
						,cdramo
						,estado
						,nmpoliza
						,"0"
						,nmsuplem
						,cdtipsup
						);
	            ////// tarificacion //////
				//////////////////////////
				
				//////////////////////////
				////// valor endoso //////
				Map<String,Object>mapaValorEndoso=new LinkedHashMap<String,Object>(0);
				mapaValorEndoso.put("pv_cdunieco_i" , cdunieco);
				mapaValorEndoso.put("pv_cdramo_i"   , cdramo);
				mapaValorEndoso.put("pv_estado_i"   , estado);
				mapaValorEndoso.put("pv_nmpoliza_i" , nmpoliza);
				mapaValorEndoso.put("pv_nmsituac_i" , "1"); //no importa
				mapaValorEndoso.put("pv_nmsuplem_i" , nmsuplem);
				mapaValorEndoso.put("pv_feinival_i" , dFechaEndoso);
				mapaValorEndoso.put("pv_cdtipsup_i" , cdtipsup);
				endososManager.calcularValorEndoso(mapaValorEndoso);
				////// valor endoso //////
				//////////////////////////
			}else{
				
				//////////////////////////////
			    ////// inserta tworksup //////
			    endososManager.movimientoTworksupEnd(
			    		cdunieco
			    		,cdramo
			    		,estado
			    		,nmpoliza
			    		,cdtipsup
			    		,nmsuplem
			    		,nmsituac, "I"
			    		);
			    ////// inserta tworksup //////
			    
			    //////////////////////////////
				Map<String,String>paramDomicilAsegTitular=new LinkedHashMap<String,String>(0);
				paramDomicilAsegTitular.put("pv_cdperson_i" , cdpersonAsegTitular);//cdperson del asegurado titular seleccionado
				paramDomicilAsegTitular.put("pv_nmorddom_i" , nmordom);//nmordom del asegurado titular seleccionado
				paramDomicilAsegTitular.put("pv_msdomici_i" , dsdomici);
				paramDomicilAsegTitular.put("pv_nmtelefo_i" , nmtelefo);
				paramDomicilAsegTitular.put("pv_cdpostal_i" , cdpostal);
				paramDomicilAsegTitular.put("pv_cdedo_i"    , cdestado);
				paramDomicilAsegTitular.put("pv_cdmunici_i" , cdmunici);
				paramDomicilAsegTitular.put("pv_cdcoloni_i" , cdcoloni);
				paramDomicilAsegTitular.put("pv_nmnumero_i" , nmnumext);
				paramDomicilAsegTitular.put("pv_nmnumint_i" , nmnumint);
				paramDomicilAsegTitular.put("pv_cdtipdom_i" , cdtipdom);
				paramDomicilAsegTitular.put("pv_cdusuario_i", usuarioCaptura);
				paramDomicilAsegTitular.put("pv_swactivo_i",  Constantes.SI);
				paramDomicilAsegTitular.put("pv_accion_i"   , Constantes.UPDATE_MODE);			
				kernelManager.pMovMdomicil(paramDomicilAsegTitular);
			}
			
			logger.debug("inicia confirmacion : ");
			
			// Se confirma el endoso si cumple la validacion de fechas: 
			RespuestaConfirmacionEndosoVO respConfirmacionEndoso = this.confirmarEndoso(
					cdunieco,
					cdramo,
					estado,
					nmpoliza,
					nmsuplem,
					nsuplogi,
					cdtipsup,
					"",
					dFechaEndoso,
					cdtipsit
					,flujo
					,cdusuari
					,cdsisrol
					);
		    
			// Si el endoso fue confirmado:
			if(respConfirmacionEndoso.isConfirmado()) {
				endosoConfirmado = true;
				
				//tworsksup para impresiones
				if(esPolizaColectiva){
					//////////////////////////////
				    ////// inserta tworksup //////
				    endososManager.movimientoTworksupEnd(
				    		cdunieco
				    		,cdramo
				    		,estado
				    		,nmpoliza
				    		,cdtipsup
				    		,nmsuplem
				    		,nmsituac, "I"
				    		);
				    ////// inserta tworksup //////
				}
				
				///////////////////////////////////////
			    ////// re generar los documentos //////
			    /*///////////////////////////////////*/
				Map<String,String> datosPoliza = documentosManager.generarDocumentosParametrizados(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,nmsituac //nmsituac
						,nmsuplem
						,DocumentosManager.PROCESO_ENDOSO
						,ntramite
						,null //nmsolici
						,null
						);
				
				String nmsolici    = datosPoliza.get("nmsolici");
				String rutaCarpeta = Utils.join(this.rutaDocumentosPoliza,"/",ntramite);
				
				/*
			    List<Map<String,String>>listaDocu=endososManager.reimprimeDocumentos(
			    		cdunieco
			    		,cdramo
			    		,estado
			    		,nmpoliza
			    		,nmsuplem
			    		,TipoEndoso.CAMBIO_DOMICILIO_ASEGURADO_TITULAR.getCdTipSup().toString()
			    		);
			    logger.debug("documentos que se regeneran: "+listaDocu);
			    
			    String rutaCarpeta=this.rutaDocumentosPoliza+"/"+ntramite;
			    
				//listaDocu contiene: nmsolici,nmsituac,descripc,descripl
				for(Map<String,String> docu:listaDocu) {
					logger.debug("docu iterado: "+docu);
					String nmsolici=docu.get("nmsolici");
					//String nmsituac=docu.get("nmsituac");
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
							+ "&p_estado="+estado
							+ "&p_poliza="+nmpoliza
							+ "&p_suplem="+nmsuplem
							+ "&desname="+rutaCarpeta+"/"+descripc;
					if(descripc.substring(0, 6).equalsIgnoreCase("CREDEN")) {
						// C R E D E N C I A L _ X X X X X X . P D F
						//0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
						url+="&p_cdperson="+descripc.substring(11, descripc.lastIndexOf("_"));
					}
					logger.debug(""
							+ "\n#################################"
							+ "\n###### Se solicita reporte ######"
							+ "\na "+url+""
							+ "\n#################################");
					HttpUtil.generaArchivo(url,rutaCarpeta+"/"+descripc);
					logger.debug(""
							+ "\n######                    ######"
							+ "\n###### reporte solicitado ######"
							+ "\na "+url+""
							+ "\n################################"
							+ "\n################################"
							+ "");
				}
				*/
			    /*///////////////////////////////////*/
				////// re generar los documentos //////
			    ///////////////////////////////////////

				
				//Si no es una poliza colectiva,  o si es una poliza colectiva y pagan los titulares, se consideran contratantes
				if(!esPolizaColectiva || (esPolizaColectiva && !soloPagaContratante)){
					
					String saludDanios = "S";
					
					ClienteGeneral clienteGeneral = new ClienteGeneral();
					clienteGeneral.setClaveCia(saludDanios);
					
					// Ejecutamos el Web Service de Cliente Salud:
					ice2sigsService.ejecutaWSclienteGeneral(null, null, null, null, null, null, cdpersonAsegTitular, Ice2sigsService.Operacion.ACTUALIZA, clienteGeneral, (UserVO) session.get("USUARIO"), false);
					
					String sucursal = cdunieco;
					//String nmsolici = listaDocu.get(0).get("nmsolici");
					String nmtramite = ntramite;
					
					String tipomov = TipoEndoso.CAMBIO_DOMICILIO_ASEGURADO_TITULAR.getCdTipSup().toString();
					
					// Ejecutamos el Web Service de Recibos:
					ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo, 
							estado, nmpoliza, 
							nmsuplem, rutaCarpeta, 
							sucursal, nmsolici, nmtramite, 
							true, tipomov, 
							(UserVO) session.get("USUARIO"));
					
					
					ArrayList<Map<String,String>> saveList = new ArrayList<Map<String,String>>();
					ArrayList<Map<String,String>> updateList = new ArrayList<Map<String,String>>();

	    			Map<String,String> domicilioEndoso = new HashMap<String,String>();
	    			
	    			
	    			/**
	    			 * SE FIJAN VALORES AUNQUE EL UNICO QUE SE TOMA ES EL NMORDDOM
	    			 */
	    			domicilioEndoso.put("NMORDDOM" ,nmordom);
	    			domicilioEndoso.put("CODPOSTAL",cdpostal);
	    			domicilioEndoso.put("CDEDO"    ,cdestado);
	    			domicilioEndoso.put("CDMUNICI" ,cdmunici);
	    			domicilioEndoso.put("CDCOLONI" ,cdcoloni);
	    			domicilioEndoso.put("DSDOMICI" ,dsdomici);
	    			domicilioEndoso.put("NMNUMERO" ,nmnumext);
	    			domicilioEndoso.put("NMNUMINT" ,nmnumint);
	    			
	    			updateList.add(domicilioEndoso);
	    			
	    			ice2sigsService.ejecutaWSdireccionClienteGeneral(cdpersonAsegTitular, saludDanios, saveList, updateList, false, usuario);
				}

			    mensaje="Se ha guardado el endoso "+nsuplogi;
			    
			} else {
				mensaje="El endoso "+nsuplogi
						+" se guard\u00f3 en mesa de control para autorizaci\u00f3n "
						+ "con n\u00famero de tr\u00e1mite " + respConfirmacionEndoso.getNumeroTramite();
			}
			if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
                mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
            }
			success=true;
		} catch(Exception ex) {
			logger.error("error al guardar los datos de endoso de domicilio full",ex);
			success=false;
			error=ex.getMessage();
		}
		
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### error   = " , error
				,"\n###### mensaje = " , mensaje
				,"\n###### guardarEndosoDomicilioFull ######"
				,"\n########################################"
				));
		return SUCCESS;
	}
	/*////////////////////////////////////*/
	////// guardarEndosoDomicilioFull //////
	////////////////////////////////////////

	
	////////////////////////////////////////
	////// guardarEndosoDomicilioAutoFull //////
	/*
	smap1:
	    cdrfc=MAVA900817001,
	    cdperson=511965,
	    fenacimi=1990-08-17T00:00:00,
	    sexo=H,
	    Apellido_Materno=MAT,
	    nombre=NOMBRE1,
	    nombrecompleto=NOMBRE1  PAT MAT,
	    nmsituac=1,
	    segundo_nombre=null,
	    Parentesco=T,
	    CDTIPSIT=SL,
	    NTRAMITE=615,
	    CDUNIECO=1006,
	    CDRAMO=2,
	    NMSUPLEM=245673812540000005,
	    NMPOLIZA=14,
	    swexiper=S,
	    NMPOLIEX=1006213000014000000,
	    nacional=001,
	    activo=true,
	    NSUPLOGI=8,
	    ESTADO=M,
	    cdrol=2,
	    tpersona=F,
	    Apellido_Paterno=PAT
	smap2:
	    CDCOLONI: "137617"
	    CDEDO: "9600030"
	    CDMUNICI: "9600030003"
	    CODPOSTAL: "96000"
	    DSDOMICI: "FLORES"
	    NMNUMERO: "ROSAS"
	    NMNUMINT: "1"
	    NMORDDOM: "1"
	    NMTELEFO: "AA"
	 */
	/*////////////////////////////////////*/
	public String guardarEndosoDomicilioAutoFull()
	{
		this.session = ActionContext.getContext().getSession();
		
		logger.debug(Utils.log(
				 "\n############################################"
				,"\n###### guardarEndosoDomicilioAutoFull ######"
				,"\n###### smap1      = " , smap1
				,"\n###### smap2      = " , smap2
				,"\n###### parametros = " , parametros
				,"\n###### flujo      = " , flujo
				));
		
		try
		{
			UserVO usuario      = (UserVO) session.get("USUARIO");
			String cdelemento   = usuario.getEmpresa().getElementoId();
			String cdusuari     = usuario.getUser();
			String cdsisrol     = usuario.getRolActivo().getClave();
			String cdunieco     = smap1.get("CDUNIECO");
			String cdramo       = smap1.get("CDRAMO");
			String estado       = smap1.get("ESTADO");
			String nmpoliza     = smap1.get("NMPOLIZA");
			String nmsituac     = smap1.get("nmsituac");
			String sFechaEndoso = smap3.get("fecha_endoso");
			Date   dFechaEndoso = renderFechas.parse(sFechaEndoso);
			String cdtipsit     = smap1.get("CDTIPSIT");
			String cdperson     = smap1.get("cdperson");
			String cdrol        = smap1.get("cdrol");
			String nmordom      = smap2.get("NMORDDOM");
			String cdtipdom     = smap2.get("CDTIPDOM");
			String dsdomici     = smap2.get("DSDOMICI");
			String nmtelefo     = smap2.get("NMTELEFO");
			String cdpostal     = smap2.get("CODPOSTAL");
			String cdestado     = smap2.get("CDEDO");
			String cdmunici     = smap2.get("CDMUNICI");
			String cdcoloni     = smap2.get("CDCOLONI");
			String nmnumext     = smap2.get("NMNUMERO");
			String nmnumint     = smap2.get("NMNUMINT");
			String cdtipsup     = TipoEndoso.CAMBIO_DOMICILIO_ASEGURADO_TITULAR.getCdTipSup().toString();
			String proceso      = "END";
			String ntramite     = smap1.get("NTRAMITE");
			
			String usuarioCaptura =  null;
			
			if(usuario!=null){
				if(StringUtils.isNotBlank(usuario.getClaveUsuarioCaptura())){
					usuarioCaptura = usuario.getClaveUsuarioCaptura();
				}else{
					usuarioCaptura = usuario.getCodigoPersona();
				}
				
			}
			
			String paso = null;
			
			try //esto deberia ir en un manager
			{
				paso = "Validando c\u00f3digo postal";
				logger.debug(paso);
				
				/**
				 * Validar que el Codigo Postal pertenezca al Estado correcto
				 */
				if(smap1!=null&&smap1.size()>0 && smap1.containsKey("CDTIPSIT") && TipoSituacion.MULTISALUD.getCdtipsit().equalsIgnoreCase(smap1.get("CDTIPSIT"))){
					HashMap<String,String> params =  new HashMap<String, String>();
					params.put("pv_estado_i", smap2.get("CDEDO"));
					params.put("pv_codpos_i", smap2.get("CODPOSTAL"));
					endososManager.validaEstadoCodigoPostal(params);
				}
				
				paso = "Iniciando endoso";
				logger.debug(paso);
				
				//PKG_ENDOSOS.P_ENDOSO_INICIA
				Map<String,String> resIniEnd=endososManager.iniciarEndoso(cdunieco, cdramo, estado
						,nmpoliza, sFechaEndoso, cdelemento, cdusuari, proceso, cdtipsup);
				
				String nmsuplem = resIniEnd.get("pv_nmsuplem_o");
				String nsuplogi = resIniEnd.get("pv_nsuplogi_o");
				
				paso = "Recuperando valores de situaci\u00f3n";
				logger.debug(paso);
				
				//PKG_CONSULTA.P_OBT_VALOSIT_ULTIMA_IMAGEN
				List<Map<String,String>>valositsPoliza=endososManager.obtenerValositUltimaImagen(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
				/*
				CDUNIECO,CDRAMO,ESTADO,NMPOLIZA,NMSITUAC,NMSUPLEM,STATUS,CDTIPSIT,OTVALOR01,OTVALOR02
				,OTVALOR03,OTVALOR04,OTVALOR05,OTVALOR06,OTVALOR07,OTVALOR08,OTVALOR09,OTVALOR10,OTVALOR11
				,OTVALOR12,OTVALOR13,OTVALOR14,OTVALOR15,OTVALOR16,OTVALOR17,OTVALOR18,OTVALOR19,OTVALOR20
				,OTVALOR21,OTVALOR22,OTVALOR23,OTVALOR24,OTVALOR25,OTVALOR26,OTVALOR27,OTVALOR28,OTVALOR29
				,OTVALOR30,OTVALOR31,OTVALOR32,OTVALOR33,OTVALOR34,OTVALOR35,OTVALOR36,OTVALOR37,OTVALOR38
				,OTVALOR39,OTVALOR40,OTVALOR41,OTVALOR42,OTVALOR43,OTVALOR44,OTVALOR45,OTVALOR46,OTVALOR47
				,OTVALOR48,OTVALOR49,OTVALOR50
				 */
				
				String valorCodPosOriginal = null;
				
				paso = "Actualizando valores de situaci\u00f3n";
				logger.debug(paso);
				
				for(Map<String,String>valositIte:valositsPoliza) {
					String nmsituacIte=valositIte.get("NMSITUAC");
					String keyCodPostal = "pv_otvalor"+valositIte.get("CDATRIBU");
					valorCodPosOriginal = valositIte.get("OTVALOR"+valositIte.get("CDATRIBU"));
					logger.debug("OTvalor encontrado para CP: "  +keyCodPostal);
					
					//otvalor05 -> pv_otvalor05
					Map<String,String>otvalorValositIte=new HashMap<String,String>();
					for(Entry<String,String>en:valositIte.entrySet()) {
						String key   = en.getKey();
						String value = en.getValue();
						
						if(key.substring(0,5).equalsIgnoreCase("otval")) {
							otvalorValositIte.put("pv_"+(key.toLowerCase()),value);
						}
					}
					
					otvalorValositIte.put(keyCodPostal , cdpostal);
					
					//PKG_SATELITES2.P_MOV_TVALOSIT
					kernelManager.insertaValoresSituaciones(cdunieco, cdramo, estado, nmpoliza
							,nmsituacIte, nmsuplem, Constantes.STATUS_VIVO, valositIte.get("CDTIPSIT"), Constantes.INSERT_MODE, otvalorValositIte);
				}
				
				paso = "Guardando domicilio";
				logger.debug(paso);
				
				Map<String,String>paramDomicilIte=new LinkedHashMap<String,String>(0);
				paramDomicilIte.put("pv_cdperson_i" , cdperson);
				paramDomicilIte.put("pv_nmorddom_i" , nmordom);
				paramDomicilIte.put("pv_msdomici_i" , dsdomici);
				paramDomicilIte.put("pv_nmtelefo_i" , nmtelefo);
				paramDomicilIte.put("pv_cdpostal_i" , cdpostal);
				paramDomicilIte.put("pv_cdedo_i"    , cdestado);
				paramDomicilIte.put("pv_cdmunici_i" , cdmunici);
				paramDomicilIte.put("pv_cdcoloni_i" , cdcoloni);
				paramDomicilIte.put("pv_nmnumero_i" , nmnumext);
				paramDomicilIte.put("pv_nmnumint_i" , nmnumint);
				paramDomicilIte.put("pv_cdtipdom_i" , cdtipdom);
				paramDomicilIte.put("pv_cdusuario_i", usuarioCaptura);
				paramDomicilIte.put("pv_swactivo_i",  Constantes.SI);
				paramDomicilIte.put("pv_accion_i"   , Constantes.UPDATE_MODE);			
				kernelManager.pMovMdomicil(paramDomicilIte);
				
				paso = "Insertando valores temporales de endoso";
				logger.debug(paso);
				
				//////////////////////////////
				////// inserta tworksup //////
				Map<String,String>mapaTworksupEnd=new LinkedHashMap<String,String>(0);
				mapaTworksupEnd.put("pv_cdunieco_i" , cdunieco);
				mapaTworksupEnd.put("pv_cdramo_i"   , cdramo);
				mapaTworksupEnd.put("pv_estado_i"   , estado);
				mapaTworksupEnd.put("pv_nmpoliza_i" , nmpoliza);
				mapaTworksupEnd.put("pv_cdtipsup_i" , cdtipsup);
				mapaTworksupEnd.put("pv_nmsuplem_i" , nmsuplem);
				endososManager.insertarTworksupSitTodas(mapaTworksupEnd);
				////// inserta tworksup //////
				//////////////////////////////
				logger.debug("Codigo Postal Original:>"+valorCodPosOriginal+"<Codigo Postal Nuevo:>"+cdpostal+"<");
	
				
				/**
				 *SE COMENTA PARA YA NO TARIFICAR 
				 * 
				if(cdpostal.equalsIgnoreCase(valorCodPosOriginal)){
					logger.debug("El codigo Postal para Endoso de Domicilio Full no ha cambiado, no se ejecuta SIGSVALIPOL, CALCULA_VALOR_ENDOSO");
				}else{
					logger.debug("Codigo postal nuevo para Endoso de Domicilio Full, ejecutando SIGSVALIPOL, CALCULA_VALOR_ENDOSO");
					//////////////////////////
					////// tarificacion //////
					endososManager.sigsvalipolEnd(
							cdusuari
							,cdelemento
							,cdunieco
							,cdramo
							,estado
							,nmpoliza
							,"0"
							,nmsuplem
							,cdtipsup
							);
					////// tarificacion //////
					//////////////////////////
					
					//////////////////////////
					////// valor endoso //////
					Map<String,Object>mapaValorEndoso=new LinkedHashMap<String,Object>(0);
					mapaValorEndoso.put("pv_cdunieco_i" , cdunieco);
					mapaValorEndoso.put("pv_cdramo_i"   , cdramo);
					mapaValorEndoso.put("pv_estado_i"   , estado);
					mapaValorEndoso.put("pv_nmpoliza_i" , nmpoliza);
					mapaValorEndoso.put("pv_nmsituac_i" , "1");
					mapaValorEndoso.put("pv_nmsuplem_i" , nmsuplem);
					mapaValorEndoso.put("pv_feinival_i" , dFechaEndoso);
					mapaValorEndoso.put("pv_cdtipsup_i" , cdtipsup);
					endososManager.calcularValorEndoso(mapaValorEndoso);
					////// valor endoso //////
					//////////////////////////
					
				}
				
				*/
				
				// Se confirma el endoso si cumple la validacion de fechas: 
				RespuestaConfirmacionEndosoVO respConfirmacionEndoso = this.confirmarEndoso(
						cdunieco,
						cdramo,
						estado,
						nmpoliza,
						nmsuplem,
						nsuplogi,
						cdtipsup,
						"",
						dFechaEndoso,
						null
						,flujo
						,cdusuari
						,cdsisrol
						);
				
				// Si el endoso fue confirmado:
				if(respConfirmacionEndoso.isConfirmado()) {
					endosoConfirmado = true;
					///////////////////////////////////////
					////// re generar los documentos //////
					/*///////////////////////////////////*/
					
					paso = "Generando documentos";
					logger.debug(paso);
					
					Map<String,String> datosPoliza = documentosManager.generarDocumentosParametrizados(
							cdunieco
							,cdramo
							,estado
							,nmpoliza
							,"0" //nmsituac
							,nmsuplem
							,DocumentosManager.PROCESO_ENDOSO
							,ntramite
							,null //nmsolici
	, null
							);
					
					String nmsolici    = datosPoliza.get("nmsolici");
					String rutaCarpeta = Utils.join(this.rutaDocumentosPoliza,"/",ntramite);
					
					/*
					List<Map<String,String>>listaDocu=endososManager.reimprimeDocumentos(
							cdunieco
							,cdramo
							,estado
							,nmpoliza
							,nmsuplem
							,TipoEndoso.CAMBIO_DOMICILIO_ASEGURADO_TITULAR.getCdTipSup().toString()
							);
					logger.debug("documentos que se regeneran: "+listaDocu);
					
					String rutaCarpeta=this.rutaDocumentosPoliza+"/"+ntramite;
					
					//listaDocu contiene: nmsolici,nmsituac,descripc,descripl
					for(Map<String,String> docu:listaDocu) {
						logger.debug("docu iterado: "+docu);
						String nmsolici=docu.get("nmsolici");
						//String nmsituac=docu.get("nmsituac");
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
								+ "&p_estado="+estado
								+ "&p_poliza="+nmpoliza
								+ "&p_suplem="+nmsuplem
								+ "&desname="+rutaCarpeta+"/"+descripc;
						if(descripc.substring(0, 6).equalsIgnoreCase("CREDEN")) {
							// C R E D E N C I A L _ X X X X X X . P D F
							//0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
							url+="&p_cdperson="+descripc.substring(11, descripc.lastIndexOf("_"));
						}
						logger.debug(""
								+ "\n#################################"
								+ "\n###### Se solicita reporte ######"
								+ "\na "+url+""
								+ "\n#################################");
						HttpUtil.generaArchivo(url,rutaCarpeta+"/"+descripc);
						logger.debug(""
								+ "\n######                    ######"
								+ "\n###### reporte solicitado ######"
								+ "\na "+url+""
								+ "\n################################"
								+ "\n################################"
								+ "");
					}
					*/
					/*///////////////////////////////////*/
					////// re generar los documentos //////
					///////////////////////////////////////
					
					if(consultasManager.esProductoSalud(cdramo)){
						
						logger.debug("Es producto de salud, llamando Web Services para endoso de Cambio de Domicilio");
						
						String saludDanios = "S";
						
						ClienteGeneral clienteGeneral = new ClienteGeneral();
						clienteGeneral.setClaveCia(saludDanios);
						
						// Ejecutamos el Web Service de Cliente Salud:
						ice2sigsService.ejecutaWSclienteGeneral(null, null, null, null, null, null, cdperson, Ice2sigsService.Operacion.ACTUALIZA, clienteGeneral, (UserVO) session.get("USUARIO"), false);
	//					ice2sigsService.ejecutaWSclienteSalud(cdunieco, cdramo, estado, nmpoliza, nmsuplem, respConfirmacionEndoso.getNumeroTramite(), Ice2sigsService.Operacion.ACTUALIZA, (UserVO) session.get("USUARIO"));
						//ejecutaWSclienteSaludEndoso(cdunieco, cdramo, estado, nmpoliza, nmsuplem, "ACTUALIZA");
						
						String sucursal = cdunieco;
						//String nmsolici = listaDocu.get(0).get("nmsolici");
						String nmtramite = ntramite;
						
						String tipomov = TipoEndoso.CAMBIO_DOMICILIO_ASEGURADO_TITULAR.getCdTipSup().toString();
						
						// Ejecutamos el Web Service de Recibos:
						ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo, 
								estado, nmpoliza, 
								nmsuplem, rutaCarpeta, 
								sucursal, nmsolici, nmtramite, 
								true, tipomov, 
								(UserVO) session.get("USUARIO"));
					}else{
						
						logger.debug("Es producto de autos, llamando servicios para endoso de Cambio de Domicilio");
						
						int numEndRes = 0;
						
						/**
						 * PARA WS ENDOSO DE AUTOS SIN TARIFICACION DONDE PUEDE INCLUIR LA COLONIA y con o sin cambio de CP
						 */
						numEndRes = emisionAutosService.actualizaDatosCambioDomicilSinCP(cdunieco, cdramo, estado, nmpoliza, nmsuplem, usuario);
						
						if(numEndRes == 0){
							mensaje = "Error al generar el endoso, sigs. Consulte a Soporte.";
							error = "Error al generar el endoso, sigs. Consulte a Soporte.";
							logger.error("Error al ejecutar sp de endoso sigs");
							
							boolean endosoRevertido = endososManager.revierteEndosoFallido(cdunieco, cdramo, estado, nmpoliza, nsuplogi, nmsuplem, 88888, "Error en endoso B tipo: "+TipoEndoso.CAMBIO_DOMICILIO.toString(), true);
							
							if(endosoRevertido){
								
								Map<String,String> paramRevDom = new HashMap<String, String>();
								paramRevDom.put("pv_cdperson_i" , smap3.get("cdperson"));
								paramRevDom.put("pv_dsdomici_i" , smap3.get("calle"));
								paramRevDom.put("pv_cdpostal_i" , smap3.get("cp"));
								paramRevDom.put("pv_nmnumero_i" , smap3.get("numext"));
								paramRevDom.put("pv_nmnumint_i" , smap3.get("numint"));
								paramRevDom.put("pv_cdedo_i"    , smap3.get("cdedo"));
								paramRevDom.put("pv_cdmunici_i" , smap3.get("cdmunici"));
								paramRevDom.put("pv_cdcoloni_i" , smap3.get("cdcoloni"));
								endososManager.revierteDomicilio(paramRevDom);
								
								logger.error("Endoso revertido exitosamente.");
								error+=" Favor de volver a intentar.";
							}else{
								logger.error("Error al revertir el endoso");
								error+=" No se ha revertido el endoso.";
							}
							
							success = false;
							return SUCCESS;
						}else{
							ejecutaCaratulaEndosoBsigs(cdunieco,cdramo,estado,nmpoliza,nmsuplem, ntramite, cdtipsup, Integer.toString(numEndRes));
						}
						
					}
					
					mensaje="Se ha guardado el endoso "+nsuplogi;
					
				} else {
					mensaje="El endoso "+nsuplogi
							+" se guard\u00f3 en mesa de control para autorizaci\u00f3n "
							+ "con n\u00famero de tr\u00e1mite " + respConfirmacionEndoso.getNumeroTramite();
				}
				if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
                }
				success=true;
			}
			catch(Exception ex)
			{
				Utils.generaExcepcion(ex, paso);
			}
		}
		catch(Exception ex)
		{
			error = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### mensaje = " , mensaje
				,"\n###### error   = " , error
				,"\n###### guardarEndosoDomicilioFull ######"
				,"\n########################################"
				));
		return SUCCESS;
	}
	/*////////////////////////////////////*/
	////// guardarEndosoDomicilioFull //////
	////////////////////////////////////////

	
	/////////////////////////////
	////// endosoDeducible //////
	/*
	smap1:
		CDRAMO: "2"
		CDTIPSIT: "SL"
		CDUNIECO: "1006"
		DSCOMENT: ""
		DSTIPSIT: "SALUD VITAL"
		ESTADO: "M"
		FEEMISIO: "22/01/2014"
		FEINIVAL: "22/01/2014"
		NMPOLIEX: "1006213000024000000"
		NMPOLIZA: "24"
		NMSUPLEM: "245668019180000000"
		NSUPLOGI: "1"
		NTRAMITE: "678"
		PRIMA_TOTAL: "12207.37"
	smap2:
		masdeducible: "si"
	*/
	/*/////////////////////////*/
	public String endosoDeducible() {
		
		logger.debug(new StringBuilder("\n")
		        .append("\n#############################")
		        .append("\n#############################")
		        .append("\n###### endosoDeducible ######")
		        .append("\n######                 ######").toString());
		logger.debug(new StringBuilder("smap1: ").append(smap1).toString());
		logger.debug(new StringBuilder("smap2: ").append(smap2).toString());
		
		this.session=ActionContext.getContext().getSession();
		
		RespuestaVO resp = null;
		
		try
		{
			transformaEntrada(smap1, slist1, false);
			
			String cdunieco = smap1.get("CDUNIECO");
			String cdramo   = smap1.get("CDRAMO");
			String estado   = smap1.get("ESTADO");
			String nmpoliza = smap1.get("NMPOLIZA");
			String cdtipsit = smap1.get("CDTIPSIT");
			String cdtipsup = smap2.get("masdeducible").equalsIgnoreCase("si")?
					TipoEndoso.DEDUCIBLE_MAS.getCdTipSup().toString():
					TipoEndoso.DEDUCIBLE_MENOS.getCdTipSup().toString();
					
			UserVO usuario    = (UserVO)session.get("USUARIO");
			String cdelemento = usuario.getEmpresa().getElementoId();
			String cdusuari   = usuario.getUser();
			String rol        = usuario.getRolActivo().getClave();
			
			// Valida si hay un endoso anterior pendiente:
			resp = endososManager.validaEndosoAnterior(cdunieco, cdramo, estado, nmpoliza, cdtipsup);
			error = resp.getMensaje();
			
			String nmsituacTitular   = "1";
			String llaveDeducible    = "";
			String cdatribuDeducible = "";
			
			String nombreItemDeducibleOriginal = "DEDUCIBLE ORIGINAL";
			String nombreItemNuevoDeducible    = "NUEVO DEDUCIBLE";
			
			String llaveItemDeducibleOriginal = "itemDeducibleLectura";
			String llaveItemNuevoDeducible    = "itemDeducible";
			String llavePanelLectura          = "itemsLectura";
			
			String pantalla = "ENDOSO_DEDUCIBLE";
			
			//obtener campo deducible
			if(resp.isSuccess())
			{
				try
				{
					List<ComponenteVO>tatrisitAux = kernelManager.obtenerTatrisit(cdtipsit,cdusuari);
					
					for(ComponenteVO tatri:tatrisitAux)
					{
						if(tatri.getLabel().lastIndexOf("DEDUCIBLE")>-1)
						{
							cdatribuDeducible = tatri.getNameCdatribu();
							llaveDeducible    = new StringBuilder("otvalor").append(StringUtils.leftPad(tatri.getNameCdatribu(),2,"0")).toString();
						}
					}
					
					logger.debug(new StringBuilder("cdatribuDeducible=").append(cdatribuDeducible).toString());
					logger.debug(new StringBuilder("llaveDeducible=")   .append(llaveDeducible).toString());
				}
				catch(Exception ex)
				{
					logger.error("Error al obtener componente de deducible", ex);
					error = ex.getMessage();
					resp.setSuccess(false);
				}
			}
			
			if(resp.isSuccess()) {
				try {
					Map<String,Object>valositTitular=kernelManager.obtieneValositSituac(cdunieco,cdramo,estado,nmpoliza,nmsituacTitular);
					if(llaveDeducible.length()>0
							&&valositTitular.containsKey(llaveDeducible)
							&&((String)valositTitular.get(llaveDeducible))!=null) {
						String deducible=(String)valositTitular.get(llaveDeducible);
						logger.debug("deducible de la poliza: "+deducible);
						smap1.put("deducible"    , deducible);
						smap1.put("masdeducible" , smap2.get("masdeducible"));
					} else {
						throw new Exception("No hay deducible definido para este producto");
					}
					
					List<ComponenteVO>tatrisit = kernelManager.obtenerTatrisit(cdtipsit,cdusuari);
					List<ComponenteVO>temp     = new ArrayList<ComponenteVO>();
					for(ComponenteVO tatrisitIte:tatrisit) {
						if(tatrisitIte.getNameCdatribu().equalsIgnoreCase(cdatribuDeducible)) {
							temp.add(tatrisitIte);
						}
					}
					tatrisit=temp;
					
					GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
					gc.setCdtipsit(cdtipsit);
					
					imap1=new HashMap<String,Item>();
					tatrisit.get(0).setLabel(nombreItemNuevoDeducible);
					
					gc.generaParcial(tatrisit);
					
					imap1.put(llaveItemNuevoDeducible,gc.getItems());
					
					tatrisit.get(0).setSoloLectura(true);
					tatrisit.get(0).setLabel(nombreItemDeducibleOriginal);
					
					gc.generaParcial(tatrisit);
					
					imap1.put(llaveItemDeducibleOriginal,gc.getItems());
					
					gc.generaParcial(pantallasManager.obtenerComponentes(
							null, cdunieco, cdramo,
							cdtipsit, estado, rol, 
							pantalla, "PANEL_LECTURA", null));
					
					imap1.put(llavePanelLectura,gc.getItems());
					
				} catch(Exception ex) {
					logger.error("Error al mostrar pantalla endoso deducible", ex);
					error = ex.getMessage();
					resp.setSuccess(false);
				}
			}
		}
		catch(Exception ex)
		{
			logger.error("Error al construir pantalla de deducible",ex);
			error = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(new StringBuilder("\n")
		        .append("\n######                 ######")
		        .append("\n###### endosoDeducible ######")
		        .append("\n#############################")
		        .append("\n#############################").toString());
		
		return resp!=null&&resp.isSuccess() ? SUCCESS : ERROR;
	}
	/*/////////////////////////*/
	////// endosoDeducible //////
	/////////////////////////////
	
	////////////////////////////////////
	////// guardarEndosoDeducible //////
	/*
	smap1:
		NMSUPLEM=245668111370000000,
		DSTIPSIT=SALUD VITAL,
		FEINIVAL=23/01/2014,
		NMPOLIZA=24,
		deducible=10000,
		PRIMA_TOTAL=16039.29,
		NMPOLIEX=1006213000024000000,
		NSUPLOGI=2,
		DSCOMENT=,
		ESTADO=M,
		masdeducible=si,
		CDTIPSIT=SL,
		NTRAMITE=678,
		CDUNIECO=1006,
		FEEMISIO=22/01/2014,
		CDRAMO=2
	smap2:
		deducible=10000,
		fecha_endoso=23/01/2014
	*/
	/*////////////////////////////////*/
	public String guardarEndosoDeducible() {
		this.session=ActionContext.getContext().getSession();
		
		logger.debug(Utils.log(
				 "\n####################################"
				,"\n###### guardarEndosoDeducible ######"
				,"\n###### smap1 = " , smap1
				,"\n###### smap2 = " , smap2
				,"\n###### flujo = " , flujo
				));
		
		try {
			UserVO usuario    = (UserVO)session.get("USUARIO");
			String cdunieco   = smap1.get("CDUNIECO");
			String cdramo     = smap1.get("CDRAMO");
			String estado     = smap1.get("ESTADO");
			String nmpoliza   = smap1.get("NMPOLIZA");
			String fecha      = smap2.get("fecha_endoso");
			Date   dFecha     = renderFechas.parse(fecha);
			String cdelemento = usuario.getEmpresa().getElementoId();
			String cdusuari   = usuario.getUser();
			String cdsisrol   = usuario.getRolActivo().getClave();
			String proceso    = "END";
			String cdtipsup   = smap1.get("masdeducible").equalsIgnoreCase("si")?
					TipoEndoso.DEDUCIBLE_MAS.getCdTipSup().toString():
					TipoEndoso.DEDUCIBLE_MENOS.getCdTipSup().toString();
			String deducible  = smap2.get("deducible");
			String cdtipsit   = smap1.get("CDTIPSIT");
			String ntramite   = smap1.get("NTRAMITE");
			
			//PKG_ENDOSOS.P_ENDOSO_INICIA
			Map<String,String>resIniEnd=endososManager.iniciarEndoso(cdunieco, cdramo, estado, nmpoliza, fecha, cdelemento, cdusuari, proceso, cdtipsup);
			
			String nmsuplem = resIniEnd.get("pv_nmsuplem_o");
			String nsuplogi = resIniEnd.get("pv_nsuplogi_o");
			
			//PKG_ENDOSOS.P_INS_NEW_DEDUCIBLE_TVALOSIT
			endososManager.actualizaDeducibleValosit(cdunieco, cdramo, estado, nmpoliza, nmsuplem, deducible);
			
			//////////////////////////////
            ////// inserta tworksup //////
			//PKG_SATELITES.P_INSERTA_TWORKSUP_SIT_TODAS
            Map<String,String>mapaTworksupEnd=new LinkedHashMap<String,String>(0);
            mapaTworksupEnd.put("pv_cdunieco_i" , cdunieco);
            mapaTworksupEnd.put("pv_cdramo_i"   , cdramo);
            mapaTworksupEnd.put("pv_estado_i"   , estado);
            mapaTworksupEnd.put("pv_nmpoliza_i" , nmpoliza);
            mapaTworksupEnd.put("pv_cdtipsup_i" , cdtipsup);
            mapaTworksupEnd.put("pv_nmsuplem_i" , nmsuplem);
            endososManager.insertarTworksupSitTodas(mapaTworksupEnd);
            ////// inserta tworksup //////
            //////////////////////////////
            
            //////////////////////////
            ////// tarificacion //////
            //PKG_COTIZA.P_EJECUTA_SIGSVALIPOL_END
			endososManager.sigsvalipolEnd(
					cdusuari
					,cdelemento
					,cdunieco
					,cdramo
					,estado
					,nmpoliza
					,"0"
					,nmsuplem
					,cdtipsup
					);
            ////// tarificacion //////
			//////////////////////////
			
			//////////////////////////
			////// valor endoso //////
			//PKG_ENDOSOS.P_CALC_VALOR_ENDOSO
			Map<String,Object>mapaValorEndoso=new LinkedHashMap<String,Object>(0);
			mapaValorEndoso.put("pv_cdunieco_i" , cdunieco);
			mapaValorEndoso.put("pv_cdramo_i"   , cdramo);
			mapaValorEndoso.put("pv_estado_i"   , estado);
			mapaValorEndoso.put("pv_nmpoliza_i" , nmpoliza);
			mapaValorEndoso.put("pv_nmsituac_i" , "1");
			mapaValorEndoso.put("pv_nmsuplem_i" , nmsuplem);
			mapaValorEndoso.put("pv_feinival_i" , dFecha);
			mapaValorEndoso.put("pv_cdtipsup_i" , cdtipsup);
			endososManager.calcularValorEndoso(mapaValorEndoso);
			////// valor endoso //////
			//////////////////////////
			
			// Se confirma el endoso si cumple la validacion de fechas: 
			RespuestaConfirmacionEndosoVO respConfirmacionEndoso = this.confirmarEndoso(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplem
					,nsuplogi
					,cdtipsup
					,""
					,dFecha
					,cdtipsit
					,flujo
					,cdusuari
					,cdsisrol
					);
			
			// Si el endoso fue confirmado:
			if(respConfirmacionEndoso.isConfirmado()) {
				endosoConfirmado = true;
				
				// Regeneramos los documentos:
				String nmsolici = this.regeneraDocumentos(cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdtipsup, ntramite,cdusuari);
				
				String sucursal = cdunieco;
				
				// Ejecutamos el Web Service de Recibos:
				ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo, 
						estado, nmpoliza, 
						nmsuplem, null, 
						sucursal, nmsolici, ntramite, 
						true, cdtipsup, 
						(UserVO) session.get("USUARIO"));
				
				mensaje="Se ha guardado el endoso "+nsuplogi;
			} else {
				mensaje="El endoso "+nsuplogi
						+" se guard\u00f3 en mesa de control para autorizaci\u00f3n "
						+ "con n\u00famero de tr\u00e1mite " + respConfirmacionEndoso.getNumeroTramite();
			}
			if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
                mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
            }
			success=true;
		} catch(Exception ex) {
			error=ex.getMessage();
			success=false;
			logger.error("error al guardar endoso de deducible",ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### error   = " , error
				,"\n###### mensaje = " , mensaje
				,"\n###### guardarEndosoDeducible ######"
				,"\n####################################"
				));
		
		return SUCCESS;
	}
	/*////////////////////////////////*/
	////// guardarEndosoDeducible //////
	////////////////////////////////////
	
	//////////////////////////
	////// endosoCopago //////
	/*
	smap1:
		CDRAMO: "2"
		CDTIPSIT: "SL"
		CDUNIECO: "1006"
		DSCOMENT: ""
		DSTIPSIT: "SALUD VITAL"
		ESTADO: "M"
		FEEMISIO: "22/01/2014"
		FEINIVAL: "22/01/2014"
		NMPOLIEX: "1006213000024000000"
		NMPOLIZA: "24"
		NMSUPLEM: "245668019180000000"
		NSUPLOGI: "1"
		NTRAMITE: "678"
		PRIMA_TOTAL: "12207.37"
	smap2:
		mascopago: "si"
	*/
	/*//////////////////////*/
	public String endosoCopago() {
		
		logger.debug(new StringBuilder("\n")
		        .append("\n##########################")
		        .append("\n##########################")
		        .append("\n###### endosoCopago ######")
		        .append("\n######              ######").toString());
		logger.debug(new StringBuilder("smap1: ").append(smap1).toString());
		logger.debug(new StringBuilder("smap2: ").append(smap2).toString());
		
		this.session=ActionContext.getContext().getSession();
		
		RespuestaVO resp = null;
		
		try
		{
			transformaEntrada(smap1, slist1, false);
			
			String cdunieco = smap1.get("CDUNIECO");
			String cdramo   = smap1.get("CDRAMO");
			String estado   = smap1.get("ESTADO");
			String nmpoliza = smap1.get("NMPOLIZA");
			String cdtipsit = smap1.get("CDTIPSIT");
			String cdtipsup = smap2.get("mascopago").equalsIgnoreCase("si")?
					TipoEndoso.COPAGO_MAS.getCdTipSup().toString():
					TipoEndoso.COPAGO_MENOS.getCdTipSup().toString();
					
			UserVO usuario    = (UserVO)session.get("USUARIO");
			String cdelemento = usuario.getEmpresa().getElementoId();
			String cdusuari   = usuario.getUser();
			String rol        = usuario.getRolActivo().getClave();
			
			// Valida si hay un endoso anterior pendiente:
			resp = endososManager.validaEndosoAnterior(cdunieco, cdramo, estado, nmpoliza, cdtipsup);
			error = resp.getMensaje();
			
			String nmsituacTitular = "1";
			String llaveCopago     = "";
			String cdatribuCopago  = "";
			
			String nombreItemCopagoOriginal = "COPAGO ORIGINAL";
			String nombreItemNuevoCopago    = "NUEVO COPAGO";
			
			String llaveItemCopagoOriginal = "itemCopagoLectura";
			String llaveItemNuevoCopago    = "itemCopago";
			String llavePanelLectura          = "itemsLectura";
			
			String pantalla = "ENDOSO_COPAGO";
			
			if(cdtipsit.equals("SL")||cdtipsit.equals("SN")) {
				llaveCopago    = "otvalor06";
				cdatribuCopago = "6";
			}
			
			if(resp.isSuccess()) {
				try {
					Map<String,Object>valositTitular=kernelManager.obtieneValositSituac(cdunieco,cdramo,estado,nmpoliza,nmsituacTitular);
					if(llaveCopago.length()>0
							&&valositTitular.containsKey(llaveCopago)
							&&((String)valositTitular.get(llaveCopago))!=null) {
						String copago=(String)valositTitular.get(llaveCopago);
						logger.debug("copago de la poliza: "+copago);
						smap1.put("copago"    , copago);
						smap1.put("mascopago" , smap2.get("mascopago"));
					} else {
						throw new Exception("No hay copago definido para este producto");
					}
					
					List<ComponenteVO>tatrisit = kernelManager.obtenerTatrisit(cdtipsit,cdusuari);
					List<ComponenteVO>temp     = new ArrayList<ComponenteVO>();
					for(ComponenteVO tatrisitIte:tatrisit) {
						if(tatrisitIte.getNameCdatribu().equalsIgnoreCase(cdatribuCopago)) {
							temp.add(tatrisitIte);
						}
					}
					tatrisit=temp;
					
					GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
					gc.setCdtipsit(cdtipsit);
					
					imap1=new HashMap<String,Item>();
					tatrisit.get(0).setLabel(nombreItemNuevoCopago);
					
					gc.generaParcial(tatrisit);
					
					imap1.put(llaveItemNuevoCopago,gc.getItems());
					
					tatrisit.get(0).setSoloLectura(true);
					tatrisit.get(0).setLabel(nombreItemCopagoOriginal);
					
					gc.generaParcial(tatrisit);
					
					imap1.put(llaveItemCopagoOriginal,gc.getItems());
					
					gc.generaParcial(pantallasManager.obtenerComponentes(
							null, cdunieco, cdramo,
							cdtipsit, estado, rol,
							pantalla, "PANEL_LECTURA", null));
					
					imap1.put(llavePanelLectura,gc.getItems());
					
				} catch(Exception ex) {
					logger.error("error al mostrar pantalla endoso copago",ex);
					error = ex.getMessage();
					resp.setSuccess(false);
				}
			}
		}
		catch(Exception ex)
		{
			logger.error("Error al construir la pantalla de endoso de copago",ex);
			error = Utils.manejaExcepcion(ex);
		}
		
		logger.debug("\n"
				+ "\n######              ######"
				+ "\n###### endosoCopago ######"
				+ "\n##########################"
				+ "\n##########################"
				);
		return resp!=null&&resp.isSuccess() ? SUCCESS : ERROR;
	}
	/*//////////////////////*/
	////// endosoCopago //////
	//////////////////////////
	
	/////////////////////////////////
	////// guardarEndosoCopago //////
	/*
	smap1:
		NMSUPLEM=245668111370000000,
		DSTIPSIT=SALUD VITAL,
		FEINIVAL=23/01/2014,
		NMPOLIZA=24,
		copago=10000,
		PRIMA_TOTAL=16039.29,
		NMPOLIEX=1006213000024000000,
		NSUPLOGI=2,
		DSCOMENT=,
		ESTADO=M,
		mascopago=si,
		CDTIPSIT=SL,
		NTRAMITE=678,
		CDUNIECO=1006,
		FEEMISIO=22/01/2014,
		CDRAMO=2
	smap2:
		copago=10000,
		fecha_endoso=23/01/2014
	*/
	/*////////////////////////////////*/
	public String guardarEndosoCopago() {
		this.session=ActionContext.getContext().getSession();
		
		logger.debug(Utils.log(
				 "\n#################################"
				,"\n###### guardarEndosoCopago ######"
				,"\n###### smap1 = " , smap1
				,"\n###### smap2 = " , smap2
				,"\n###### flujo = " , flujo
				));
		
		try
		{
			UserVO usuario    = (UserVO)session.get("USUARIO");
			String cdunieco   = smap1.get("CDUNIECO");
			String cdramo     = smap1.get("CDRAMO");
			String estado     = smap1.get("ESTADO");
			String nmpoliza   = smap1.get("NMPOLIZA");
			String fecha      = smap2.get("fecha_endoso");
			Date   dFecha     = renderFechas.parse(fecha);
			String cdelemento = usuario.getEmpresa().getElementoId();
			String cdusuari   = usuario.getUser();
			String cdsisrol   = usuario.getRolActivo().getClave();
			String proceso    = "END";
			String cdtipsup   = smap1.get("mascopago").equalsIgnoreCase("si")?
					TipoEndoso.COPAGO_MAS.getCdTipSup().toString():
					TipoEndoso.COPAGO_MENOS.getCdTipSup().toString();
			String copago     = smap2.get("copago");
			String cdtipsit   = smap1.get("CDTIPSIT");
			String ntramite   = smap1.get("NTRAMITE");
			
			//PKG_ENDOSOS.P_ENDOSO_INICIA
			Map<String,String>resIniEnd=endososManager.iniciarEndoso(cdunieco, cdramo, estado, nmpoliza, fecha, cdelemento, cdusuari, proceso, cdtipsup);
			
			String nmsuplem = resIniEnd.get("pv_nmsuplem_o");
			String nsuplogi = resIniEnd.get("pv_nsuplogi_o");
			
			//PKG_ENDOSOS.P_INS_NEW_COPAGO_TVALOSIT
			endososManager.actualizaCopagoValosit(cdunieco, cdramo, estado, nmpoliza, nmsuplem, copago);
			
			//////////////////////////////
            ////// inserta tworksup //////
			//PKG_SATELITES.P_INSERTA_TWORKSUP_SIT_TODAS
            Map<String,String>mapaTworksupEnd=new LinkedHashMap<String,String>(0);
            mapaTworksupEnd.put("pv_cdunieco_i" , cdunieco);
            mapaTworksupEnd.put("pv_cdramo_i"   , cdramo);
            mapaTworksupEnd.put("pv_estado_i"   , estado);
            mapaTworksupEnd.put("pv_nmpoliza_i" , nmpoliza);
            mapaTworksupEnd.put("pv_cdtipsup_i" , cdtipsup);
            mapaTworksupEnd.put("pv_nmsuplem_i" , nmsuplem);
            endososManager.insertarTworksupSitTodas(mapaTworksupEnd);
            ////// inserta tworksup //////
            //////////////////////////////
            
            //////////////////////////
            ////// tarificacion //////
            //PKG_COTIZA.P_EJECUTA_SIGSVALIPOL_END
			endososManager.sigsvalipolEnd(
					cdusuari
					,cdelemento
					,cdunieco
					,cdramo
					,estado
					,nmpoliza
					,"0"
					,nmsuplem
					,cdtipsup
					);
            ////// tarificacion //////
			//////////////////////////
			
			//////////////////////////
			////// valor endoso //////
			//PKG_ENDOSOS.P_CALC_VALOR_ENDOSO
			Map<String,Object>mapaValorEndoso=new LinkedHashMap<String,Object>(0);
			mapaValorEndoso.put("pv_cdunieco_i" , cdunieco);
			mapaValorEndoso.put("pv_cdramo_i"   , cdramo);
			mapaValorEndoso.put("pv_estado_i"   , estado);
			mapaValorEndoso.put("pv_nmpoliza_i" , nmpoliza);
			mapaValorEndoso.put("pv_nmsituac_i" , "1");
			mapaValorEndoso.put("pv_nmsuplem_i" , nmsuplem);
			mapaValorEndoso.put("pv_feinival_i" , dFecha);
			mapaValorEndoso.put("pv_cdtipsup_i" , cdtipsup);
			endososManager.calcularValorEndoso(mapaValorEndoso);
			////// valor endoso //////
			//////////////////////////
			
			// Se confirma el endoso si cumple la validacion de fechas: 
			RespuestaConfirmacionEndosoVO respConfirmacionEndoso = this.confirmarEndoso(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplem
					,nsuplogi
					,cdtipsup
					,""
					,dFecha
					,cdtipsit
					,flujo
					,cdusuari
					,cdsisrol
					);
			
			// Si el endoso fue confirmado:
			if(respConfirmacionEndoso.isConfirmado()) {
				endosoConfirmado = true;
				
				// Regeneramos los documentos:
				String nmsolici = this.regeneraDocumentos(cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdtipsup, ntramite,cdusuari);
				
				String sucursal = cdunieco;

				// Ejecutamos el Web Service de Recibos:
				ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo, 
						estado, nmpoliza, 
						nmsuplem, null, 
						sucursal, nmsolici, ntramite, 
						true, cdtipsup, 
						(UserVO) session.get("USUARIO"));
				
				mensaje="Se ha guardado el endoso "+nsuplogi;
			} else {
				mensaje="El endoso "+nsuplogi
						+" se guard\u00f3 en mesa de control para autorizaci\u00f3n "
						+ "con n\u00famero de tr\u00e1mite " + respConfirmacionEndoso.getNumeroTramite();
			}
			if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
                mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
            }
			success=true;
		} catch(Exception ex) {
			error=ex.getMessage();
			success=false;
			logger.error("error al guardar endoso de copago",ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### error   = " , error
				,"\n###### mensaje = " , mensaje
				,"\n###### guardarEndosoCopago ######"
				,"\n#################################"
				));
		
		return SUCCESS;
	}
	/*/////////////////////////////*/
	////// guardarEndosoCopago //////
	/////////////////////////////////
	
	/////////////////////////////////
	////// regenera documentos //////
	/*/////////////////////////////*/
	/**
	 * PKG_CONSULTA.P_reImp_documentos
	 */
	private String regeneraDocumentos(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdtipsup
			,String ntramite
			,String cdusuari) throws Exception
	{
		String nmsolici = null;
		///////////////////////////////////////
	    ////// re generar los documentos //////
	    /*///////////////////////////////////*/
		
		//PKG_CONSULTA.P_reImp_documentos
		Map<String,String> datosPoliza = documentosManager.generarDocumentosParametrizados(
				cdunieco
				,cdramo
				,estado
				,nmpoliza
				,"0" //nmsituac
				,nmsuplem
				,DocumentosManager.PROCESO_ENDOSO
				,ntramite
				,nmsolici, null
				);
		
		nmsolici = datosPoliza.get("nmsolici");
		
		/*
	    List<Map<String,String>>listaDocu=endososManager.reimprimeDocumentos(
	    		cdunieco
	    		,cdramo
	    		,estado
	    		,nmpoliza
	    		,nmsuplem
	    		,cdtipsup
	    		);
	    logger.debug("documentos que se regeneran: "+listaDocu);
	    
	    if(StringUtils.isBlank(ntramite))
	    {
	    	ntramite = listaDocu.get(0).get("ntramite");
	    }
	    String rutaCarpeta=this.rutaDocumentosPoliza+"/"+ntramite;
	    
		//listaDocu contiene: nmsolici,nmsituac,descripc,descripl
		for(Map<String,String> docu:listaDocu)
		{
			logger.debug("docu iterado: "+docu);
			nmsolici = docu.get("nmsolici");
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
					+ "&p_estado="+estado
					+ "&p_poliza="+nmpoliza
					+ "&p_suplem="+nmsuplem
					+ "&desname="+rutaCarpeta+"/"+descripc;
			if(descripc.substring(0, 6).equalsIgnoreCase("CREDEN"))
			{
				// C R E D E N C I A L _ X X X X X X . P D F
				//0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
				logger.debug("descripc: "+descripc+" descripc.lastIndexOf(_): "+descripc.lastIndexOf("_"));
				url+="&p_cdperson="+descripc.substring(11, descripc.lastIndexOf("_"));
			}
			logger.debug(""
					+ "\n#################################"
					+ "\n###### Se solicita reporte ######"
					+ "\na "+url+""
					+ "\n#################################");
			HttpUtil.generaArchivo(url,rutaCarpeta+"/"+descripc);
			logger.debug(""
					+ "\n######                    ######"
					+ "\n###### reporte solicitado ######"
					+ "\na "+url+""
					+ "\n################################"
					+ "\n################################"
					+ "");
		}
		*/
		return nmsolici;
	    /*///////////////////////////////////*/
		////// re generar los documentos //////
	    ///////////////////////////////////////
	}
	/*/////////////////////////////*/
	////// regenera documentos //////
	/////////////////////////////////
	
	////////////////////////////////
	////// endosoReexpedicion //////
	/*
	smap1:
		CDRAMO: "2"
		CDTIPSIT: "SL"
		CDUNIECO: "1006"
		DSCOMENT: ""
		DSTIPSIT: "SALUD VITAL"
		ESTADO: "M"
		FEEMISIO: "22/01/2014"
		FEINIVAL: "22/01/2014"
		NMPOLIEX: "1006213000024000000"
		NMPOLIZA: "24"
		NMSUPLEM: "245668019180000000"
		NSUPLOGI: "1"
		NTRAMITE: "678"
		PRIMA_TOTAL: "12207.37"
	*/
	/*////////////////////////////*/
	public String endosoReexpedicion() {
		
		logger.debug(new StringBuilder("\n")
	        .append("\n################################")
	        .append("\n################################")
	        .append("\n###### endosoReexpedicion ######")
	        .append("\n######                    ######").toString());
		logger.debug(new StringBuilder("smap1: ").append(smap1).toString());
		
		this.session = ActionContext.getContext().getSession();
		
		RespuestaVO resp = null;
		
		try
		{
			transformaEntrada(smap1, slist1, false);
			
			String cdunieco = smap1.get("CDUNIECO");
			String cdramo   = smap1.get("CDRAMO");
			String estado   = smap1.get("ESTADO");
			String nmpoliza = smap1.get("NMPOLIZA");
			String cdtipsit = smap1.get("CDTIPSIT");
			String cdtipsup = TipoEndoso.CANCELACION_POR_REEXPEDICION.getCdTipSup().toString();
			
			String cdPantalla            = "ENDOSO_REEXPEDICION";
			String cdPanelLectura        = "PANEL_LECTURA";
			String cdDatosEndoso         = "DATOS_ENDOSO";
			String keyItemsPanelLectura  = "itemsPanelLectura";
			String keyFieldsPanelLectura = "fieldsFormLectura";
			String keyItemsDatosEndoso   = "itemsDatosEndoso";
					
			UserVO usuario    = (UserVO)session.get("USUARIO");
			String cdelemento = usuario.getEmpresa().getElementoId();
			String cdusuari   = usuario.getUser();
			String rol        = usuario.getRolActivo().getClave();
			
			// Valida si hay un endoso anterior pendiente:
			resp = endososManager.validaEndosoAnterior(cdunieco, cdramo, estado, nmpoliza, cdtipsup);
			error = resp.getMensaje();
			
			if(resp.isSuccess()) {
				try {
					GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
					imap1=new HashMap<String,Item>();
					
					gc.generaParcial(pantallasManager.obtenerComponentes(
							null, cdunieco, cdramo,
							cdtipsit, estado, rol,
							cdPantalla, cdPanelLectura, null));
					
					imap1.put(keyItemsPanelLectura,gc.getItems());
					imap1.put(keyFieldsPanelLectura,gc.getFields());
					
					gc.generaParcial(pantallasManager.obtenerComponentes(
							null, cdunieco, cdramo,
							cdtipsit, estado, rol,
							cdPantalla, cdDatosEndoso, null));
					
					imap1.put(keyItemsDatosEndoso,gc.getItems());
				} catch(Exception ex) {
					logger.error("error al mostrar pantalla endoso reexpedicion",ex);
					error = ex.getMessage();
					resp.setSuccess(false);
				}
			}
			
			GeneradorCampos gcTatri            = null;
			GeneradorCampos gcGral             = null;
			List<ComponenteVO>tatrisitOriginal = null;
			
			//componentes
			if(resp.isSuccess())
			{
				try
				{
					gcTatri = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
					gcTatri.setCdramo(cdramo);
					gcTatri.setCdtipsit(cdtipsit);
					
					gcGral = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
					gcGral.setCdramo(cdramo);
					
					//TATRISIT ORIGINAL
					tatrisitOriginal = cotizacionDAO.cargarTatrisit(cdtipsit, cdusuari);
					
					//columnas base
					List<ComponenteVO>tatrisitColsBase = new ArrayList<ComponenteVO>();
					for(ComponenteVO iTatri:tatrisitOriginal)
					{
						if(StringUtils.isNotBlank(iTatri.getSwsuscri())
								&&iTatri.getSwsuscri().equals("N")
								&&iTatri.getSwGrupo().equals("S")
								&&iTatri.getSwGrupoLinea().equals("N")
								)
						{
							logger.debug(new StringBuilder("SE AGREGA PARA COLUMNA BASE ").append(iTatri).toString());
							iTatri.setColumna("S");
							tatrisitColsBase.add(iTatri);
						}
					}
					
					if(tatrisitColsBase.size()>0)
					{
						gcTatri.generaComponentes(tatrisitColsBase, true, true, false, true, true, false);
						imap1.put("colsBaseFields"  , gcTatri.getFields());
						imap1.put("colsBaseColumns" , gcTatri.getColumns());
					}
					else
					{
						imap1.put("colsBaseFields"  , null);
						imap1.put("colsBaseColumns" , null);
					}
					//columnas base
					
					//columnas extendidas (de coberturas)
					List<ComponenteVO> tatrisitColsCober = new ArrayList<ComponenteVO>();
					for(ComponenteVO iTatri:tatrisitOriginal)
					{
						if(StringUtils.isNotBlank(iTatri.getSwsuscri())
								&&iTatri.getSwsuscri().equals("N")
								&&iTatri.getSwGrupo().equals("S")
								)
						{
							logger.debug(new StringBuilder("SE AGREGA PARA COLUMNA DE COBERTURA ").append(iTatri).toString());
							iTatri.setColumna("S");
							tatrisitColsCober.add(iTatri);
						}
					}
					if(tatrisitColsCober.size()>0)
					{
						gcTatri.generaComponentes(tatrisitColsCober, true, true, false, true, true, false);
						imap1.put("colsExtFields"  , gcTatri.getFields());
						imap1.put("colsExtColumns" , gcTatri.getColumns());
					}
					else
					{
						imap1.put("colsExtFields"  , null);
						imap1.put("colsExtColumns" , null);
					}
					//columnas extendidas (de coberturas)
					
					//factores
					List<ComponenteVO>factores = new ArrayList<ComponenteVO>();
					for(ComponenteVO iTatri:tatrisitOriginal)
					{
						if(StringUtils.isNotBlank(iTatri.getSwsuscri())
								&&iTatri.getSwsuscri().equals("N")
								&&iTatri.getSwGrupo().equals("N")
								&&iTatri.getSwGrupoFact().equals("S")
								)
						{
							logger.debug(new StringBuilder("SE AGREGA PARA FACTOR ").append(iTatri).toString());
							iTatri.setColumna("S");
							factores.add(iTatri);
							iTatri.setMenorCero(true);
						}
					}
					
					List<ComponenteVO>columnaEditorPlan=pantallasManager.obtenerComponentes(
							null, null, null,
							null, null, null,
							"COTIZACION_GRUPO", "EDITOR_PLANES_REEXP", null);
					gcGral.generaComponentes(columnaEditorPlan, true, false, false, true, true, false);
					imap1.put("editorPlanesColumn",gcGral.getColumns());
					
					
				}
				catch(Exception ex)
				{
					logger.error("Error al obtener componentes",ex);
				}
			}
			//componentes
		}
		catch(Exception ex)
		{
			logger.error("Error al construir pantalla de endoso de cancelacion por reexpedicion",ex);
			error = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(new StringBuilder("\n")
		        .append("\n######                    ######")
		        .append("\n###### endosoReexpedicion ######")
		        .append("\n################################")
		        .append("\n################################").toString());
		
		return resp!=null&&resp.isSuccess() ? SUCCESS : ERROR;
	}
	/*////////////////////////////*/
	////// endosoReexpedicion //////
	////////////////////////////////
	
	///////////////////////////////////////
	////// guardarEndosoReexpedicion //////
	/*
	smap1:
		NMSUPLEM=245668111370000000,
		DSTIPSIT=SALUD VITAL,
		FEINIVAL=23/01/2014,
		NMPOLIZA=24,
		copago=10000,
		PRIMA_TOTAL=16039.29,
		NMPOLIEX=1006213000024000000,
		NSUPLOGI=2,
		DSCOMENT=,
		ESTADO=M,
		mascopago=si,
		CDTIPSIT=SL,
		NTRAMITE=678,
		CDUNIECO=1006,
		FEEMISIO=22/01/2014,
		CDRAMO=2
	smap2:
		comment: ""
		fecha_endoso: "10/02/2015"
		trazreexped: "4"
		cdplan : "A"
	smap3:
		cdrfc=FLOR881209,
		cdperson=512043,
		dstempot=RENOVABLE,
		cdagente=11000,
		nmsolici=4225,
		dscuadro=SALUD VITAL 18%,
		feemisio=23/01/2014,
		porredau=100,
		feproren=23/01/2015,
		feefecto=23/01/2014,
		dsperpag=ANUAL,
		dsmoneda=PESOS,
		titular=TITULAR  FLORES FLORES,
		nmpoliex=1006213000025000000
	*/
	/*//////////////////////////////////////*/
	public String guardarEndosoReexpedicion()
	{
		logger.debug(Utils.log(
				 "\n#######################################"
				,"\n###### guardarEndosoReexpedicion ######"
				,"\n###### smap1  = " , smap1
				,"\n###### smap2  = " , smap2
				,"\n###### smap3  = " , smap3
				,"\n###### grupos a actualizar:"
				,"\n###### slist1 = " , slist1
				,"\n###### grupos a no actualizar:"
				,"\n###### slist2 = " , slist2
				,"\n###### flujo  = " , flujo
				));
		
		this.session=ActionContext.getContext().getSession();
		
		try {
			UserVO usuario        = (UserVO)session.get("USUARIO");
			String cdunieco       = smap1.get("CDUNIECO");
			String newcdunieco    = smap2.get("NEWCDUNIECO");
			String cdramo         = smap1.get("CDRAMO");
			String estado         = smap1.get("ESTADO");
			String nmpoliza       = smap1.get("NMPOLIZA");
			String sFecha         = smap2.get("fecha_endoso");
			Date   dFecha         = renderFechas.parse(sFecha);
			String cdelemento     = usuario.getEmpresa().getElementoId();
			String cdusuari       = usuario.getUser();
			String cdsisrol       = usuario.getRolActivo().getClave();
			String proceso        = "END";
			String cdtipsup       = TipoEndoso.CANCELACION_POR_REEXPEDICION.getCdTipSup().toString();
			String cdtipsit       = smap1.get("CDTIPSIT");
			String ntramite       = smap1.get("NTRAMITE");
			String feIniVig       = smap3.get("feefecto");
			String feFinvig       = smap3.get("feproren");
			String cdrazonReexp   = smap2.get("trazreexped");
			String comentaReexp   = smap2.get("comment");
			String cdplan         = smap2.get("cdplan");
			
			String usuarioTramite =  "";
			
			boolean esProductoSalud = consultasManager.esProductoSalud(cdramo);
			boolean esPolizaColectiva  = (StringUtils.isNotBlank(smap1.get("TIPOFLOT")) && "F".equalsIgnoreCase(smap1.get("TIPOFLOT")));//flotilla o Colectiva

			boolean actualizarGrupos =  (slist1 != null && !slist1.isEmpty());
			boolean clonarGrupos     =  (slist2 != null && !slist2.isEmpty());
			
			
			//PKG_ENDOSOS.P_ENDOSO_INICIA
			Map<String,String>resIniEnd=endososManager.iniciarEndoso(cdunieco, cdramo, estado, nmpoliza,
					sFecha, cdelemento, cdusuari, proceso, cdtipsup);
			
			String nmsuplem = resIniEnd.get("pv_nmsuplem_o");
			String nsuplogi = resIniEnd.get("pv_nsuplogi_o");
			
			if(StringUtils.isBlank(newcdunieco)){
				newcdunieco = cdunieco;
			}
			
			//P_CLONAR_POLIZA_REEXPED
			Map<String,String>resReexped = endososManager.pClonarPolizaReexped(cdunieco, cdramo, estado, nmpoliza, sFecha,"".equals(cdplan)?smap3.get("cdplan"):cdplan, cdusuari, newcdunieco);
			String nmpolizaNuevaPoliza = resReexped.get("pv_nmpolnew_o");
			String ntramiteNuevaPoliza = resReexped.get("pv_ntramite_o");
			
			/**
			 * Si se actializaron planes y suma asegurada de grupos
			 */
			if(esProductoSalud && esPolizaColectiva && (actualizarGrupos || clonarGrupos)){
				
				//Se clonan tablas para tarificacion de grupos que no cambian valores
				if(clonarGrupos){
					boolean exitoGrupos = endososManager.clonaGruposReexp(cdunieco, cdramo, estado, nmpoliza, newcdunieco, nmpolizaNuevaPoliza, slist2);
					
					if(!exitoGrupos){
						long timestamp = System.currentTimeMillis();
						
						error="Error al Clonar Grupos para endoso de Reexpedicion"+ timestamp;
						logger.error(error);
						success=false;
						return SUCCESS;
					}
				}

				// Se actualizan valores de tarificacion por grupo que haya cambiado y disparan valores por defecto
				if(actualizarGrupos){
					boolean exitoGrupos = endososManager.actualizaGruposReexp(newcdunieco, cdramo, "W", nmpolizaNuevaPoliza, slist1);
					
					if(exitoGrupos){
						exitoGrupos = endososManager.valoresDefectoGruposReexp(newcdunieco, cdramo, "W", nmpolizaNuevaPoliza,"0", cdtipsup, slist1); 
						
						if(exitoGrupos){
							cotizacionManager.ejecutasigsvdefEnd(newcdunieco, cdramo, "W", nmpolizaNuevaPoliza, "0", "0", "TODO", cdtipsup);
						}
					}
					
					if(!exitoGrupos){
						long timestamp = System.currentTimeMillis();
						error="Error al Actualizar Grupos para endoso de Reexpedicion"+ timestamp;
						logger.error(error);
						success=false;
						return SUCCESS;
					}
				}
				
			}
			
			//pkg_cancela.p_selecciona_poliza_unica
			cancelacionManager.seleccionaPolizaUnica(cdunieco, cdramo, nmpoliza, null, dFecha);
			
			//pkg_cancela.p_cancela_poliza
			String nmsuplemCancela = cancelacionManager.cancelaPoliza(cdunieco, cdramo, null, estado, nmpoliza, null
					,cdrazonReexp, comentaReexp, feIniVig, feFinvig, sFecha, cdusuari, cdtipsup,usuario.getRolActivo().getClave());
			
			// Se confirma el endoso si cumple la validacion de fechas: 
			RespuestaConfirmacionEndosoVO respConfirmacionEndoso = this.confirmarEndoso(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplemCancela
					,nsuplogi
					,cdtipsup
					,comentaReexp
					,dFecha
					,cdtipsit
					,flujo
					,cdusuari
					,cdsisrol
					);
			
			
			consultasManager.copiarArchivosUsuarioTramite(cdunieco, cdramo, estado, nmpoliza, ntramiteNuevaPoliza, this.rutaDocumentosPoliza);
			
			/**
			 * Para cambiar el estatus del tramite nuevo
			 */
			if(esProductoSalud && esPolizaColectiva){
				long timestamp = System.currentTimeMillis();
				
				/**
				 * Si se hicieron cambios a los gruos se manda a estatus cotizador,
				 * si no se realizaron cambios se manda a estatus carga completa
				 */
				if(actualizarGrupos){
					Map<String, Object> res =  siniestrosManager.moverTramite(ntramiteNuevaPoliza, EstatusTramite.EN_ESPERA_DE_COTIZACION.getCodigo(), "Se Reexpide Poliza del tramite original: "+ ntramite , usuario.getUser(), usuario.getRolActivo().getClave(), 
							null, RolSistema.SUSCRIPTOR_TECNICO.getCdsisrol(), null, null, "N", timestamp, false);
					
					if(res.containsKey("NOMBRE") && StringUtils.isNotBlank((String)res.get("NOMBRE"))){
						usuarioTramite = " asignado a: "+(String)res.get("NOMBRE");
					}
					
					usuarioTramite+=" en suscripci\u00F3n t\u00e9cnica para su recotizaci\u00f3n";
				}else{
					Map<String, Object> res =  siniestrosManager.moverTramite(ntramiteNuevaPoliza, EstatusTramite.TRAMITE_COMPLETO.getCodigo(), "Se Reexpide Poliza del tramite original: "+ ntramite , usuario.getUser(), usuario.getRolActivo().getClave(), 
							null, RolSistema.SUSCRIPTOR.getCdsisrol(), null, null, "N", timestamp, false);
					
					if(res.containsKey("NOMBRE") && StringUtils.isNotBlank((String)res.get("NOMBRE"))){
						usuarioTramite = " asignado a: "+(String)res.get("NOMBRE");
					}
					
					usuarioTramite+=" en suscripci\u00F3n de emisi\u00f3n para su reexpedici\u00f3n";
					
				}
			}
			
			// Si el endoso fue confirmado:
			if(respConfirmacionEndoso.isConfirmado()) {
				endosoConfirmado = true;
				
				Map<String,String> datosPoliza = documentosManager.generarDocumentosParametrizados(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,"0" //nmsituac
						,nmsuplemCancela
						,DocumentosManager.PROCESO_ENDOSO
						,ntramite
						,null//nmsolici
						,null
						);
				
				if(StringUtils.isBlank(ntramite)){
					ntramite = datosPoliza.get("ntramite");
				}
				
				String nmsolici = datosPoliza.get("nmsolici");
				
				mensaje="Se ha generado la p\u00f3liza "+nmpolizaNuevaPoliza
						+" con n\u00famero de tr\u00e1mite "+ntramiteNuevaPoliza + usuarioTramite;
				
				String sucursal = cdunieco;
				
				// Ejecutamos el Web Service de Recibos:
				ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo, 
						estado, nmpoliza, 
						nmsuplemCancela, null, 
						sucursal, nmsolici, ntramite, 
						true, cdtipsup, 
						(UserVO) session.get("USUARIO"));
				
			} else {
				mensaje="El endoso "+nsuplogi
						+" se guard\u00f3 en mesa de control para autorizaci\u00f3n "
						+ "con n\u00famero de tr\u00e1mite "+respConfirmacionEndoso.getNumeroTramite()+". "
						+ "La p\u00f3liza reexpedida es "+nmpolizaNuevaPoliza+" con tr\u00e1mite "
						+ "de emisi\u00f3n "+ntramiteNuevaPoliza;
			}
			if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
                mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
            }
			success=true;
			
		} catch(Exception ex) {
			error=ex.getMessage();
			success=false;
			logger.error("error al guardar endoso de reexpedicion",ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### error   = " , error
				,"\n###### mensaje = " , mensaje
				,"\n###### guardarEndosoReexpedicion ######"
				,"\n#######################################"
				));
		
		return SUCCESS;
	}
	/*///////////////////////////////////*/
	////// guardarEndosoReexpedicion //////
	///////////////////////////////////////
	
	//////////////////////////////
	////// endosoExtraprima //////
	/*
	smap1:
		cdrfc=MAVA900817001,
		cdperson=511965,
		fenacimi=1990-08-17T00:00:00,
		sexo=H,
		Apellido_Materno=MAT,
		nombre=NOMBRE1,
		nombrecompleto=NOMBRE1  PAT MAT,
		nmsituac=1,
		segundo_nombre=null,
		Parentesco=T,
		CDTIPSIT=SL,
		NTRAMITE=615,
		CDUNIECO=1006,
		CDRAMO=2,
		NMSUPLEM=245667912520000001,
		NMPOLIZA=14,
		swexiper=S,
		NMPOLIEX=1006213000014000000,
		nacional=001,
		activo=true,
		NSUPLOGI=4,
		ESTADO=M,
		cdrol=2,
		tpersona=F,
		Apellido_Paterno=PAT
	smap2:
		masextraprima: "si"
	*/
	/*//////////////////////*/
	public String endosoExtraprima() {
		
		
		try{
			
			logger.debug("<<<<<<>>>>>> Endoso de Extraprima, Determinando tipoFloat:::");
			
			String cduniecoPol = smap1.get("CDUNIECO");
			String cdramoPol   = smap1.get("CDRAMO");
			String estadoPol   = smap1.get("ESTADO");
			String nmpolizaPol = smap1.get("NMPOLIZA");
			
			String tipoFlot = endososManager.obtieneTipoFlot(cduniecoPol, cdramoPol, estadoPol, nmpolizaPol);
			
			if(StringUtils.isBlank(tipoFlot) || (!tipoFlot.equalsIgnoreCase("F") && !tipoFlot.equalsIgnoreCase("P"))){
				return this.endosoExtraprimaIndividual();
			}
			
		}catch(Exception ex){
			logger.debug("Error al identificar el tipo de producto, se omite.",ex);
		}
		
		logger.debug(new StringBuilder("\n")
		        .append("\n##############################")
		        .append("\n##############################")
		        .append("\n###### endosoExtraprima ######")
		        .append("\n######                  ######").toString());
		logger.debug(new StringBuilder("smap1: ").append(smap1).toString());
		logger.debug(new StringBuilder("smap2: ").append(smap2).toString());
		
		this.session=ActionContext.getContext().getSession();
		
		RespuestaVO resp = null;
		
		try
		{
			transformaEntrada(smap1, slist1, true);
			
			String cdunieco = smap1.get("CDUNIECO");
			String cdramo   = smap1.get("CDRAMO");
			String estado   = smap1.get("ESTADO");
			String nmpoliza = smap1.get("NMPOLIZA");
			String cdtipsit = smap1.get("CDTIPSIT");
			String nmsituac = smap1.get("nmsituac");
			String cdtipsup = smap2.get("masextraprima").equalsIgnoreCase("si")?
					TipoEndoso.EXTRAPRIMA_MAS.getCdTipSup().toString():
					TipoEndoso.EXTRAPRIMA_MENOS.getCdTipSup().toString();
					
			UserVO usuario    = (UserVO)session.get("USUARIO");
			String cdelemento = usuario.getEmpresa().getElementoId();
			String cdusuari   = usuario.getUser();
			String rol        = usuario.getRolActivo().getClave();
			
			// Valida si hay un endoso anterior pendiente:
			resp = endososManager.validaEndosoAnterior(cdunieco, cdramo, estado, nmpoliza, cdtipsup);
			error = resp.getMensaje();
			
			String llaveExtraprima     = "";
			String cdatribuExtraprima  = "";
			String llaveExtraprimaOcu     = "";
			String cdatribuExtraprimaOcu  = "";
			
			
			String nombreItemExtraprimaOriginal 	= "Extraprima sobrepeso";
			String nombreItemNuevaExtraprima    	= "Nueva extraprima sobrepeso";
			String nombreItemExtraprimaOriginalOcu 	= "Extraprima ocupacional";
			String nombreItemNuevaExtraprimaOcu    	= "Nueva extraprima ocupacional";
			
			String llaveItemExtraprimaOriginal	  	= "itemExtraprimaLectura";
			String llaveItemNuevaExtraprima    	  	= "itemExtraprima";
			String llaveItemExtraprimaOriginalOcu 	= "itemExtraprimaLecturaOcu";
			String llaveItemNuevaExtraprimaOcu    	= "itemExtraprimaOcu";
			
			
			
			String llavePanelLectura          		= "itemsLectura";
			
			String pantalla = "ENDOSO_EXTRAPRIMA";
			
			//obtener campo extraprima
			if(resp.isSuccess())
			{
				try
				{
					List<ComponenteVO>tatrisitAux = kernelManager.obtenerTatrisit(cdtipsit,cdusuari);
					
					for(ComponenteVO tatri:tatrisitAux)
					{
						if(tatri.getLabel().lastIndexOf("EXTRAPRIMA S")>-1)
						{
							cdatribuExtraprima = tatri.getNameCdatribu();
							llaveExtraprima    = new StringBuilder("otvalor").append(StringUtils.leftPad(tatri.getNameCdatribu(),2,"0")).toString();
						}
						
						if(tatri.getLabel().lastIndexOf("EXTRAPRIMA O")>-1)
						{
							cdatribuExtraprimaOcu = tatri.getNameCdatribu();
							llaveExtraprimaOcu    = new StringBuilder("otvalor").append(StringUtils.leftPad(tatri.getNameCdatribu(),2,"0")).toString();
						}
					}
					
					logger.debug(new StringBuilder("cdatribuExtraprima=").append(cdatribuExtraprima).toString());
					logger.debug(new StringBuilder("llaveExtraprima=")   .append(llaveExtraprima).toString());
					logger.debug(new StringBuilder("cdatribuExtraprimaOcu=").append(cdatribuExtraprimaOcu).toString());
					logger.debug(new StringBuilder("llaveExtraprimaOcu=")   .append(llaveExtraprimaOcu).toString());
				}
				catch(Exception ex)
				{
					logger.error("Error al obtener componente de extraprima", ex);
					error = ex.getMessage();
					resp.setSuccess(false);
				}
			}
			
			if(resp.isSuccess()) {
				try {
					Map<String,Object>valosit=kernelManager.obtieneValositSituac(cdunieco,cdramo,estado,nmpoliza,nmsituac);
					if(llaveExtraprima.length()>0&&valosit.containsKey(llaveExtraprima)) {
						String extraprima=(String)valosit.get(llaveExtraprima);
						if(StringUtils.isBlank(extraprima)) {
							extraprima="0";
						}
						logger.debug("extraprima del asegurado: "+extraprima);
						smap1.put("extraprima"    , extraprima);
						smap1.put("masextraprima" , smap2.get("masextraprima"));
					} else {
						throw new Exception("No hay extraprima definida para este producto");
					}
					
					if(llaveExtraprimaOcu.length()>0&&valosit.containsKey(llaveExtraprimaOcu)) {
						String extraprima=(String)valosit.get(llaveExtraprimaOcu);
						if(StringUtils.isBlank(extraprima)) {
							extraprima="0";
						}
						logger.debug("extraprima del asegurado: "+extraprima);
						smap1.put("extraprimaOcu"    , extraprima);
						smap1.put("masextraprimaOcu" , smap2.get("masextraprima"));
					} else {
						throw new Exception("No hay extraprima definida para este producto");
					}
					
					List<ComponenteVO>tatrisit = kernelManager.obtenerTatrisit(cdtipsit,cdusuari);
					List<ComponenteVO>temp     = new ArrayList<ComponenteVO>();
					for(ComponenteVO tatrisitIte:tatrisit) {
						if(tatrisitIte.getNameCdatribu().equalsIgnoreCase(cdatribuExtraprima)) {
							temp.add(tatrisitIte);
						}
					}
					tatrisit=temp;
					
					List<ComponenteVO>tatrisitOcu = kernelManager.obtenerTatrisit(cdtipsit,cdusuari);
					List<ComponenteVO>tempOcu     = new ArrayList<ComponenteVO>();
					for(ComponenteVO tatrisitIte:tatrisitOcu) {
						if(tatrisitIte.getNameCdatribu().equalsIgnoreCase(cdatribuExtraprimaOcu)) {
							tempOcu.add(tatrisitIte);
						}
					}
					tatrisitOcu=tempOcu;
					
					GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
					gc.setCdtipsit(cdtipsit);
					
					imap1=new HashMap<String,Item>();
					
					tatrisit.get(0).setLabel(nombreItemNuevaExtraprima);
					gc.generaParcial(tatrisit);
					imap1.put(llaveItemNuevaExtraprima,gc.getItems());
					tatrisit.get(0).setSoloLectura(true);
					tatrisit.get(0).setLabel(nombreItemExtraprimaOriginal);
					gc.generaParcial(tatrisit);
					imap1.put(llaveItemExtraprimaOriginal,gc.getItems());
					
					//imap1=new HashMap<String,Item>();
					tatrisitOcu.get(0).setLabel(nombreItemNuevaExtraprimaOcu);
					gc.generaParcial(tatrisitOcu);
					imap1.put(llaveItemNuevaExtraprimaOcu,gc.getItems());
					tatrisitOcu.get(0).setSoloLectura(true);
					tatrisitOcu.get(0).setLabel(nombreItemExtraprimaOriginalOcu);
					gc.generaParcial(tatrisitOcu);
					imap1.put(llaveItemExtraprimaOriginalOcu,gc.getItems());
					
					
					logger.debug("Valor del imap1 Total ===>"+imap1);
					gc.generaParcial(pantallasManager.obtenerComponentes(
							null, cdunieco, cdramo,
							cdtipsit, estado, rol,
							pantalla, "PANEL_LECTURA", null));
					
					imap1.put(llavePanelLectura,gc.getItems());
					
				} catch(Exception ex) {
					logger.error("error al mostrar pantalla endoso extraprima",ex);
					error = ex.getMessage();
					resp.setSuccess(false);
				}
			}
		}
		catch(Exception ex)
		{
			logger.error("Error al contruir pantalla de endoso de extraprima",ex);
			error = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(new StringBuilder("\n")
		        .append("\n######                  ######")
		        .append("\n###### endosoExtraprima ######")
		        .append("\n##############################")
		        .append("\n##############################").toString());
		
		return resp!=null&&resp.isSuccess() ? SUCCESS : ERROR;
	}

	public String endosoExtraprimaIndividual() {
		
		logger.debug(new StringBuilder("\n")
		        .append("\n########################################")
		        .append("\n########################################")
		        .append("\n###### endosoExtraprimaIndividual ######")
		        .append("\n######                            ######").toString());
		logger.debug(new StringBuilder("smap1: ").append(smap1).toString());
		logger.debug(new StringBuilder("smap2: ").append(smap2).toString());
		
		this.session=ActionContext.getContext().getSession();
		
		RespuestaVO resp = null;
		
		try
		{
			transformaEntrada(smap1, slist1, true);
			
			String cdunieco = smap1.get("CDUNIECO");
			String cdramo   = smap1.get("CDRAMO");
			String estado   = smap1.get("ESTADO");
			String nmpoliza = smap1.get("NMPOLIZA");
			String cdtipsit = smap1.get("CDTIPSIT");
			String nmsituac = smap1.get("nmsituac");
			String cdtipsup = smap2.get("masextraprima").equalsIgnoreCase("si")?
					TipoEndoso.EXTRAPRIMA_MAS.getCdTipSup().toString():
					TipoEndoso.EXTRAPRIMA_MENOS.getCdTipSup().toString();
					
			UserVO usuario    = (UserVO)session.get("USUARIO");
			String cdelemento = usuario.getEmpresa().getElementoId();
			String cdusuari   = usuario.getUser();
			String rol        = usuario.getRolActivo().getClave();
			
			// Valida si hay un endoso anterior pendiente:
			resp = endososManager.validaEndosoAnterior(cdunieco, cdramo, estado, nmpoliza, cdtipsup);
			error = resp.getMensaje();
			
			String llaveExtraprima     = "";
			String cdatribuExtraprima  = "";
			
			String nombreItemExtraprimaOriginal = "EXTRAPRIMA ORIGINAL";
			String nombreItemNuevaExtraprima    = "NUEVA EXTRAPRIMA";
			
			String llaveItemExtraprimaOriginal = "itemExtraprimaLectura";
			String llaveItemNuevaExtraprima    = "itemExtraprima";
			String llavePanelLectura           = "itemsLectura";
			
			String pantalla = "ENDOSO_EXTRAPRIMA";
			
			//obtener campo extraprima
			if(resp.isSuccess())
			{
				try
				{
					List<ComponenteVO>tatrisitAux = kernelManager.obtenerTatrisit(cdtipsit,cdusuari);
					
					for(ComponenteVO tatri:tatrisitAux)
					{
						if(tatri.getLabel().lastIndexOf("EXTRAPRIMA")>-1)
						{
							cdatribuExtraprima = tatri.getNameCdatribu();
							llaveExtraprima    = new StringBuilder("otvalor").append(StringUtils.leftPad(tatri.getNameCdatribu(),2,"0")).toString();
						}
					}
					
					logger.debug(new StringBuilder("cdatribuExtraprima=").append(cdatribuExtraprima).toString());
					logger.debug(new StringBuilder("llaveExtraprima=")   .append(llaveExtraprima).toString());
				}
				catch(Exception ex)
				{
					logger.error("Error al obtener componente de extraprima", ex);
					error = ex.getMessage();
					resp.setSuccess(false);
				}
			}
			
			if(resp.isSuccess()) {
				try {
					Map<String,Object>valosit=kernelManager.obtieneValositSituac(cdunieco,cdramo,estado,nmpoliza,nmsituac);
					if(llaveExtraprima.length()>0
							&&valosit.containsKey(llaveExtraprima)) {
						String extraprima=(String)valosit.get(llaveExtraprima);
						if(StringUtils.isBlank(extraprima)) {
							extraprima="0";
						}
						logger.debug("extraprima del asegurado: "+extraprima);
						smap1.put("extraprima"    , extraprima);
						smap1.put("masextraprima" , smap2.get("masextraprima"));
					} else {
						throw new Exception("No hay extraprima definida para este producto");
					}
					
					List<ComponenteVO>tatrisit = kernelManager.obtenerTatrisit(cdtipsit,cdusuari);
					List<ComponenteVO>temp     = new ArrayList<ComponenteVO>();
					for(ComponenteVO tatrisitIte:tatrisit) {
						if(tatrisitIte.getNameCdatribu().equalsIgnoreCase(cdatribuExtraprima)) {
							temp.add(tatrisitIte);
						}
					}
					tatrisit=temp;
					
					GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
					gc.setCdtipsit(cdtipsit);
					
					imap1=new HashMap<String,Item>();
					tatrisit.get(0).setLabel(nombreItemNuevaExtraprima);
					
					gc.generaParcial(tatrisit);
					
					imap1.put(llaveItemNuevaExtraprima,gc.getItems());
					
					tatrisit.get(0).setSoloLectura(true);
					tatrisit.get(0).setLabel(nombreItemExtraprimaOriginal);
					
					gc.generaParcial(tatrisit);
					
					imap1.put(llaveItemExtraprimaOriginal,gc.getItems());
					
					gc.generaParcial(pantallasManager.obtenerComponentes(
							null, cdunieco, cdramo,
							cdtipsit, estado, rol,
							pantalla, "PANEL_LECTURA", null));
					
					imap1.put(llavePanelLectura,gc.getItems());
					
				} catch(Exception ex) {
					logger.error("error al mostrar pantalla endoso extraprima",ex);
					error = ex.getMessage();
					resp.setSuccess(false);
				}
			}
		}
		catch(Exception ex)
		{
			logger.error("Error al contruir pantalla de endoso de extraprima",ex);
			error = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(new StringBuilder("\n")
		        .append("\n######                            ######")
		        .append("\n###### endosoExtraprimaIndividual ######")
		        .append("\n########################################")
		        .append("\n########################################").toString());
		
		return resp!=null&&resp.isSuccess() ? "EXTRAINDIV" : ERROR;
	}
	/*//////////////////////////*/
	////// endosoExtraprima //////
	//////////////////////////////
	
	/////////////////////////////////////
	////// guardarEndosoExtraprima //////
	/*
	smap1:
		cdrfc:'MAVA900817001',
	    cdperson:'511965',
	    masextraprima:'si',
	    fenacimi:'1990-08-17T00:00:00',
	    sexo:'H',
	    Apellido_Materno:'MAT',
	    nombre:'NOMBRE1',
	    nombrecompleto:'NOMBRE1  PAT MAT',
	    nmsituac:'1',
	    segundo_nombre:'null',
	    Parentesco:'T',
	    CDTIPSIT:'SL',
	    NTRAMITE:'615',
	    CDUNIECO:'1006',
	    CDRAMO:'2',
	    extraprima:'0',
	    NMSUPLEM:'245668512050000001',
	    NMPOLIZA:'14',
	    swexiper:'S',
	    NMPOLIEX:'1006213000014000000',
	    nacional:'001',
	    activo:'true',
	    NSUPLOGI:'10',
	    ESTADO:'M',
	    cdrol:'2',
	    tpersona:'F',
	    Apellido_Paterno:'PAT'
	smap2:
		extraprima=10000,
		fecha_endoso=23/01/2014
	slist1:[
	    {
	    dsclausu=ENDOSO LIBRE,
	    linea_usuario=TEXTO LIBRE lel,
	    linea_general=,
	    cdclausu=END215,
	    cdtipcla=3
	    }]
	*/
	/*/////////////////////////////////*/
	public String guardarEndosoExtraprima() {
		this.session=ActionContext.getContext().getSession();
		
		logger.debug(Utils.log(
				 "\n#####################################"
				,"\n###### guardarEndosoExtraprima ######"
				,"\n###### smap1  = " , smap1
				,"\n###### smap2  = " , smap2
				,"\n###### slist1 = " , slist1
				,"\n###### flujo  = " , flujo
				));
		
		try {
			UserVO usuario    = (UserVO)session.get("USUARIO");
			String cdunieco   = smap1.get("CDUNIECO");
			String cdramo     = smap1.get("CDRAMO");
			String estado     = smap1.get("ESTADO");
			String nmpoliza   = smap1.get("NMPOLIZA");
			String nmsituac   = smap1.get("nmsituac");
			String cdtipsit   = smap1.get("CDTIPSIT");
			String ntramite   = smap1.get("NTRAMITE");
			String cdtipsup   = smap1.get("masextraprima").equalsIgnoreCase("si")?
					TipoEndoso.EXTRAPRIMA_MAS.getCdTipSup().toString():
						TipoEndoso.EXTRAPRIMA_MENOS.getCdTipSup().toString();
			String extraprima 	 = smap2.get("extraprima");
			String extraprimaOcu = smap2.get("extraprimaOcu");
			String banderapeso 	 = smap2.get("banderapeso");
			String banderaocup   = smap2.get("banderaocup");
			String fecha      = smap2.get("fecha_endoso");
			Date   dFecha     = renderFechas.parse(fecha);
			String cdelemento = usuario.getEmpresa().getElementoId();
			String cdusuari   = usuario.getUser();
			String cdsisrol   = usuario.getRolActivo().getClave();
			String proceso    = "END";
			
			//PKG_ENDOSOS.P_ENDOSO_INICIA
			Map<String,String>resIniEnd=endososManager.iniciarEndoso(cdunieco, cdramo, estado, nmpoliza, fecha, cdelemento, cdusuari, proceso, cdtipsup);
			
			String nmsuplem = resIniEnd.get("pv_nmsuplem_o");
			String nsuplogi = resIniEnd.get("pv_nsuplogi_o");
			
			//PKG_ENDOSOS.P_INS_NEW_EXTRAPRIMA_TVALOSIT
			endososManager.actualizaExtraprimaValosit2(cdunieco, cdramo, estado, nmpoliza, nmsituac, nmsuplem, extraprima, extraprimaOcu);
			
			//Iteracion
			for(Map<String,String>cla:slist1) {
				String cdclausu = cla.get("cdclausu");
				String dslinea  = cla.get("linea_usuario");
				String cdtipcla = cla.get("cdtipcla");					
				
				//PKG_SATELITES.P_MOV_MPOLICOT
				endososManager.guardarMpolicot(cdunieco, cdramo, estado, nmpoliza, 
						nmsituac, cdclausu, nmsuplem, Constantes.STATUS_VIVO, cdtipcla, 
						null, dslinea, Constantes.INSERT_MODE);
			}
			logger.debug("Valor de banderapeso ==>"+banderapeso);
			if(banderapeso.equalsIgnoreCase("1")){
				//pkg_satelites.valida_extraprima_situac_read
				String statusValidacionExtraprimas=(String)kernelManager.validarExtraprimaSituacRead(
						cdunieco,cdramo, estado, nmpoliza, nmsituac, nmsuplem).getItemMap().get("status");
				logger.debug("tiene status la extraprima: "+statusValidacionExtraprimas);
				if(statusValidacionExtraprimas.equalsIgnoreCase("N")) {
					error="Favor de verificar las extraprimas y los endosos de extraprima";
					throw new Exception(error);
				}
			}
			
			//PKG_SATELITES.P_INSERTA_TWORKSUP_END
			endososManager.movimientoTworksupEnd(cdunieco, cdramo, estado, nmpoliza, cdtipsup, nmsuplem, nmsituac, "I");

            //PKG_COTIZA.P_EJECUTA_SIGSVALIPOL_END
            endososManager.sigsvalipolEnd(cdusuari, cdelemento, cdunieco, cdramo, estado, nmpoliza, nmsituac, nmsuplem, cdtipsup);
			
			//PKG_ENDOSOS.P_CALC_VALOR_ENDOSO
			endososManager.calcularValorEndoso(cdunieco, cdramo, estado, nmpoliza, nmsituac, nmsuplem, dFecha, cdtipsup);			
			
			// Se confirma el endoso si cumple la validacion de fechas: 
			RespuestaConfirmacionEndosoVO respConfirmacionEndoso = this.confirmarEndoso(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplem
					,nsuplogi
					,cdtipsup
					,""
					,dFecha
					,cdtipsit
					,flujo
					,cdusuari
					,cdsisrol
					);
			
			// Si el endoso fue confirmado:
			if(respConfirmacionEndoso.isConfirmado()) {
				endosoConfirmado = true;
				
				// Regeneramos los documentos:
				String nmsolici = this.regeneraDocumentos(cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdtipsup, ntramite,cdusuari);
				
				String sucursal = cdunieco;
				
				// Ejecutamos el Web Service de Recibos:
				ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo, 
						estado, nmpoliza, 
						nmsuplem, null, 
						sucursal, nmsolici, ntramite, 
						true, cdtipsup, 
						(UserVO) session.get("USUARIO"));
				
				mensaje="Se ha guardado el endoso "+nsuplogi;
			} else {
				mensaje="El endoso "+nsuplogi
						+" se guard\u00f3 en mesa de control para autorizaci\u00f3n "
						+ "con n\u00famero de tr\u00e1mite " + respConfirmacionEndoso.getNumeroTramite();
			}
			if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
                mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
            }
			success=true;
			
		} catch(Exception ex) {
			error=ex.getMessage();
			success=false;
			logger.error("error al guardar endoso de extraprima",ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### error   = " , error
				,"\n###### mensaje = " , mensaje
				,"\n###### guardarEndosoExtraprima ######"
				,"\n#####################################"
				));
		
		return SUCCESS;
	}

	public String guardarEndosoExtraprimaIndividual() {
		this.session=ActionContext.getContext().getSession();
		logger.debug("\n"
				+ "\n###############################################"
				+ "\n###############################################"
				+ "\n###### guardarEndosoExtraprimaIndividual ######"
				+ "\n######                                   ######"
				);
		logger.debug("smap1:"+smap1);
		logger.debug("smap2:"+smap2);
		logger.debug("slist1:"+slist1);
		try {
			UserVO usuario    = (UserVO)session.get("USUARIO");
			String cdunieco   = smap1.get("CDUNIECO");
			String cdramo     = smap1.get("CDRAMO");
			String estado     = smap1.get("ESTADO");
			String nmpoliza   = smap1.get("NMPOLIZA");
			String nmsituac   = smap1.get("nmsituac");
			String cdtipsit   = smap1.get("CDTIPSIT");
			String ntramite   = smap1.get("NTRAMITE");
			String cdtipsup   = smap1.get("masextraprima").equalsIgnoreCase("si")?
					TipoEndoso.EXTRAPRIMA_MAS.getCdTipSup().toString():
						TipoEndoso.EXTRAPRIMA_MENOS.getCdTipSup().toString();
			String extraprima = smap2.get("extraprima");
			String fecha      = smap2.get("fecha_endoso");
			Date   dFecha     = renderFechas.parse(fecha);
			String cdelemento = usuario.getEmpresa().getElementoId();
			String cdusuari   = usuario.getUser();
			String cdsisrol   = usuario.getRolActivo().getClave();
			String proceso    = "END";
			
			//PKG_ENDOSOS.P_ENDOSO_INICIA
			Map<String,String>resIniEnd=endososManager.iniciarEndoso(cdunieco, cdramo, estado, nmpoliza, fecha, cdelemento, cdusuari, proceso, cdtipsup);
			
			String nmsuplem = resIniEnd.get("pv_nmsuplem_o");
			String nsuplogi = resIniEnd.get("pv_nsuplogi_o");
			
			//PKG_ENDOSOS.P_INS_NEW_EXTRAPRIMA_TVALOSIT
			endososManager.actualizaExtraprimaValosit(cdunieco, cdramo, estado, nmpoliza, nmsituac, nmsuplem, extraprima);
			
			//Iteracion
			for(Map<String,String>cla:slist1) {
				String cdclausu = cla.get("cdclausu");
				String dslinea  = cla.get("linea_usuario");
				String cdtipcla = cla.get("cdtipcla");					
				
				//PKG_SATELITES.P_MOV_MPOLICOT
				endososManager.guardarMpolicot(cdunieco, cdramo, estado, nmpoliza, 
						nmsituac, cdclausu, nmsuplem, Constantes.STATUS_VIVO, cdtipcla, 
						null, dslinea, Constantes.INSERT_MODE);
			}
			
			//pkg_satelites.valida_extraprima_situac_read
			String statusValidacionExtraprimas=(String)kernelManager.validarExtraprimaSituacRead(
					cdunieco,cdramo, estado, nmpoliza, nmsituac, nmsuplem).getItemMap().get("status");
			logger.debug("tiene status la extraprima: "+statusValidacionExtraprimas);
			if(statusValidacionExtraprimas.equalsIgnoreCase("N")) {
				error="Favor de verificar las extraprimas y los endosos de extraprima";
				throw new Exception(error);
			}
			
			//PKG_SATELITES.P_INSERTA_TWORKSUP_END
			endososManager.movimientoTworksupEnd(cdunieco, cdramo, estado, nmpoliza, cdtipsup, nmsuplem, nmsituac, "I");

            //PKG_COTIZA.P_EJECUTA_SIGSVALIPOL_END
            endososManager.sigsvalipolEnd(cdusuari, cdelemento, cdunieco, cdramo, estado, nmpoliza, nmsituac, nmsuplem, /*cdtipsit,*/ cdtipsup);
			
			//PKG_ENDOSOS.P_CALC_VALOR_ENDOSO
			endososManager.calcularValorEndoso(cdunieco, cdramo, estado, nmpoliza, nmsituac, nmsuplem, dFecha, cdtipsup);			
			

			// Se confirma el endoso si cumple la validacion de fechas: 
						RespuestaConfirmacionEndosoVO respConfirmacionEndoso = this.confirmarEndoso(
								cdunieco
								,cdramo
								,estado
								,nmpoliza
								,nmsuplem
								,nsuplogi
								,cdtipsup
								,""
								,dFecha
								,cdtipsit
								,flujo
								,cdusuari
								,cdsisrol
								);
			
			
			// Si el endoso fue confirmado:
			if(respConfirmacionEndoso.isConfirmado()) {
				
				// Regeneramos los documentos:
				String nmsolici = this.regeneraDocumentos(cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdtipsup, ntramite,cdusuari);
				
				String sucursal = cdunieco;
				
				// Ejecutamos el Web Service de Recibos:
				ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo, 
						estado, nmpoliza, 
						nmsuplem, null, 
						sucursal, nmsolici, ntramite, 
						true, cdtipsup, 
						(UserVO) session.get("USUARIO"));
				
				mensaje="Se ha guardado el endoso "+nsuplogi;
			} else {
				mensaje="El endoso "+nsuplogi
						+" se guard&oacute; en mesa de control para autorizaci&oacute;n "
						+ "con n&uacute;mero de tr&aacute;mite " + respConfirmacionEndoso.getNumeroTramite();
			}
			if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
                mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
            }
			success=true;
			
		} catch(Exception ex) {
			error=ex.getMessage();
			success=false;
			logger.error("error al guardar endoso de extraprima",ex);
		}
		
		logger.debug("\n"
				+ "\n######                                   ######"
				+ "\n###### guardarEndosoExtraprimaIndividual ######"
				+ "\n###############################################"
				+ "\n###############################################"
				);
		return SUCCESS;
	}
	/*/////////////////////////////////*/
	////// guardarEndosoExtraprima //////
	/////////////////////////////////////
	
	/////////////////////////////
	////// endosoFormaPago //////
	/*
	smap1:
		CDRAMO: "2"
		CDTIPSIT: "SL"
		CDUNIECO: "1006"
		DSCOMENT: ""
		DSTIPSIT: "SALUD VITAL"
		ESTADO: "M"
		FEEMISIO: "22/01/2014"
		FEINIVAL: "22/01/2014"
		NMPOLIEX: "1006213000024000000"
		NMPOLIZA: "24"
		NMSUPLEM: "245668019180000000"
		NSUPLOGI: "1"
		NTRAMITE: "678"
		PRIMA_TOTAL: "12207.37"
	*/
	/*////////////////////////////*/
	public String endosoFormaPago() {
		
		logger.debug(new StringBuilder("\n")
		        .append("\n#############################")
		        .append("\n#############################")
		        .append("\n###### endosoFormaPago ######")
		        .append("\n######                 ######").toString());
		logger.debug(new StringBuilder("smap1: ").append(smap1).toString());
		
		this.session=ActionContext.getContext().getSession();
		
		RespuestaVO resp = null;
		
		try
		{
			transformaEntrada(smap1, slist1, false);
			
			String cdunieco = smap1.get("CDUNIECO");
			String cdramo   = smap1.get("CDRAMO");
			String estado   = smap1.get("ESTADO");
			String nmpoliza = smap1.get("NMPOLIZA");
			String cdtipsit = smap1.get("CDTIPSIT");
			String cdtipsup = TipoEndoso.CAMBIO_FORMA_PAGO.getCdTipSup().toString();
			
			String cdPantalla            = "ENDOSO_FORMA_PAGO";
			String cdPanelLectura        = "PANEL_LECTURA";
			String keyItemsPanelLectura  = "itemsPanelLectura";
			String keyFieldsPanelLectura = "fieldsFormLectura";
			String cdItemsCambio         = "FORM_FORMA_PAGO";
			String keyItemCambioOriginal = "itemCambioOld";
			String keyItemCambioNuevo    = "itemCambioNew";
					
			UserVO usuario    = (UserVO)session.get("USUARIO");
			String cdelemento = usuario.getEmpresa().getElementoId();
			String cdusuari   = usuario.getUser();
			String rol        = usuario.getRolActivo().getClave();
			
			String nombreItemOriginal = "FORMA DE PAGO ORIGINAL";
			String nombreItemNuevo    = "NUEVA FORMA DE PAGO";
			
			String keyValorOriginal = "formapago";
			
			String llaveFechaInicio = "fechaInicio";
			
			// Valida si hay un endoso anterior pendiente:
			resp = endososManager.validaEndosoAnterior(cdunieco, cdramo, estado, nmpoliza, cdtipsup);
			error = resp.getMensaje();
			
			if(resp.isSuccess()) {
				try {
					GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
					imap1=new HashMap<String,Item>();
					
					gc.generaParcial(pantallasManager.obtenerComponentes(
							null, cdunieco, cdramo,
							cdtipsit, estado, rol,
							cdPantalla, cdPanelLectura, null));
					
					imap1.put(keyItemsPanelLectura,gc.getItems());
					imap1.put(keyFieldsPanelLectura,gc.getFields());
					
					List<ComponenteVO> camposCambio=pantallasManager.obtenerComponentes(
							null, cdunieco, cdramo,
							cdtipsit, estado, rol,
							cdPantalla, cdItemsCambio, null);
					
					camposCambio.get(0).setLabel(nombreItemOriginal);
					camposCambio.get(0).setSoloLectura(true);
					gc.generaParcial(camposCambio);
					imap1.put(keyItemCambioOriginal,gc.getItems());
					
					camposCambio.get(0).setLabel(nombreItemNuevo);
					camposCambio.get(0).setSoloLectura(false);
					camposCambio.get(0).setObligatorio(true);
					gc.generaParcial(camposCambio);
					imap1.put(keyItemCambioNuevo,gc.getItems());
					
					//PKG_ENDOSOS.P_GET_FEINIVAL_END_FP
					Date fechaInicioEndoso=endososManager.obtenerFechaEndosoFormaPago(cdunieco, cdramo, estado, nmpoliza);
					smap1.put(llaveFechaInicio,renderFechas.format(fechaInicioEndoso));
					
				} catch(Exception ex) {
					logger.error("error al mostrar pantalla endoso forma pago",ex);
					error = ex.getMessage();
					resp.setSuccess(false);
				}
			}
		}
		catch(Exception ex)
		{
			logger.debug("Error al construir pantalla de endoso de cambio de forma de pago",ex);
			error = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(new StringBuilder("\n")
		        .append("\n######                    ######")
		        .append("\n###### endosoFormaPago ######")
		        .append("\n################################")
		        .append("\n################################").toString());
		
		return resp!=null&&resp.isSuccess() ? SUCCESS : ERROR;
	}
	/*////////////////////////////*/
	////// endosoFormaPago //////
	////////////////////////////////
	
	///////////////////////////////////////
	////// guardarEndosoReexpedicion //////
	/*
	smap1:
		CDRAMO: "2"
		CDTIPSIT: "SL"
		CDUNIECO: "1006"
		DSCOMENT: ""
		DSTIPSIT: "SALUD VITAL"
		ESTADO: "M"
		FEEMISIO: "22/01/2014"
		FEINIVAL: "24/01/2014"
		NMPOLIEX: "1006213000020000000"
		NMPOLIZA: "20"
		NMSUPLEM: "245668219200000004"
		NSUPLOGI: "5"
		NTRAMITE: "670"
		PRIMA_TOTAL: "60712.07"
		perpag: "1" (original)
		fechaInicio : 10/10/2012 (original desde el metodo que crea la pantalla)
	smap2:
		cdperpag: "63", (nueva)
		fecha_endoso: "28/01/2014"
	*/
	/*//////////////////////////////////////*/
	
	public String guardarEndosoFormaPago()
	{
		this.session = ActionContext.getContext().getSession();
		
		logger.debug(Utils.log(
				 "\n####################################"
				,"\n###### guardarEndosoFormaPago ######"
				,"\n###### smap1 = " , smap1
				,"\n###### smap2 = " , smap2
				,"\n###### smap3 = " , smap3
				,"\n###### flujo = " , flujo
				));
		
		try
		{
			UserVO usuario        = (UserVO)session.get("USUARIO");
			String cdtipsit       = smap1.get("CDTIPSIT");
			String cdunieco       = smap1.get("CDUNIECO");
			String cdramo         = smap1.get("CDRAMO");
			String estado         = smap1.get("ESTADO");
			String nmpoliza       = smap1.get("NMPOLIZA");
			String ntramite       = smap1.get("NTRAMITE");
			String cdperpag       = smap2.get("cdperpag");
			String sFecha         = smap2.get("fecha_endoso");
			Date   dFecha         = renderFechas.parse(sFecha);
			String cdelemento     = usuario.getEmpresa().getElementoId();
			String cdusuari       = usuario.getUser();
			String cdsisrol       = usuario.getRolActivo().getClave();
			String proceso        = "END";
			String cdtipsup       = TipoEndoso.CAMBIO_FORMA_PAGO.getCdTipSup().toString();
			String p_plan         = smap1.get("CDMEJRED");
			
			String paso = null;
			
			try //Esto deberia ir en un manager
			{
				paso = "Recuperando tipo de producto";
				logger.debug(paso);
			
				boolean esProductoSalud = consultasManager.esProductoSalud(cdramo);
				
				paso = "Validando endosos pagados";
				logger.debug(paso);
				
				endososAutoManager.validarEndosoPagados(cdunieco, cdramo, estado, nmpoliza, cdtipsup);
				
				paso = "Iniciando endoso";
				logger.debug(paso);
				
				//PKG_ENDOSOS.P_ENDOSO_INICIA
				smap3 = endososManager.iniciarEndoso(cdunieco, cdramo, estado, nmpoliza, sFecha, cdelemento, cdusuari, proceso, cdtipsup);
				
				String nmsuplem = smap3.get("pv_nmsuplem_o");
				String nsuplogi = smap3.get("pv_nsuplogi_o");
				
				paso = "Insertando forma de pago";
				logger.debug(paso);
				
				//PKG_ENDOSOS.P_INS_MPOLIZAS_CDPERPAG
				endososManager.insertarPolizaCdperpag(cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdperpag);
				
				paso = "Calculando recibos";
				logger.debug(paso);
				
				//P_CALC_RECIBOS_SUB_ENDOSO_FP
				endososManager.calcularRecibosEndosoFormaPago(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
				
				paso = "Calculando comisiones";
				logger.debug(paso);
				
				//P_CALCULA_COMISION_BASE
				endososManager.calcularComisionBase(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
				
				paso = "Confirmando endoso";
				logger.debug(paso);
				
				/*// Se confirma el endoso si cumple la validacion de fechas: 
				RespuestaConfirmacionEndosoVO respConfirmacionEndoso = this.confirmarEndoso(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,nmsuplem
						,nsuplogi
						,cdtipsup
						,""
						,dFecha
						,cdtipsit
						,flujo
						,cdusuari
						,cdsisrol
						);*/
				
				if(smap2.get("confirmar").equals("no")){
					paso = "Realizando PDF de Vista Previa de Autos";
					logger.debug(paso);
					
					String rutaReports    = rutaServidorReports;
					String passReports    = passServidorReports;
					String rutaDocumentos = rutaDocumentosTemporal;
					String tipoFlotilla   =  smap1.get("TIPOFLOT");
					
					logger.debug("tipoFlotilla: "+tipoFlotilla);
					String nombreReporte = getText("rdf.endosos.nombre.auto.individual");
					
					if(TipoFlotilla.Tipo_PyMES.getCdtipsit().equals(tipoFlotilla)){
						paso = "Tomando rdf de Pymes";
						logger.debug(paso);
						nombreReporte = getText("rdf.endosos.nombre.auto.pymes");
					}
					
					Date fechaHoy = new Date();
					
					String pdfEndosoNom = renderFechaHora.format(fechaHoy)+nmpoliza+"CotizacionPrevia.pdf";

					String url = rutaReports
							+ "?destype=cache"
							+ "&desformat=PDF"
							+ "&userid="+passReports
							+ "&report="+nombreReporte
							+ "&paramform=no"
							+ "&ACCESSIBLE=YES" //parametro que habilita salida en PDF
							+ "&p_unieco="+cdunieco
							+ "&p_ramo="+cdramo
							+ "&p_estado="+estado
							+ "&p_poliza="+nmpoliza
							+ "&p_suplem="+nmsuplem
							+ "&p_plan="+p_plan
							+ "&p_perpag="+cdperpag
							+ "&desname="+rutaDocumentos+"/"+pdfEndosoNom;
					
					paso = "Guardando PDF de Vista Previa de Autos en Temporal";
					logger.debug(paso);
					HttpUtil.generaArchivo(url,rutaDocumentos+"/"+pdfEndosoNom);
					
					smap2.put("pdfEndosoNom_o",pdfEndosoNom);
				}
				
				RespuestaConfirmacionEndosoVO respConfirmacionEndoso = new RespuestaConfirmacionEndosoVO();
				
				/*if(smap2.get("confirmar").equals("si")){
				 // Se confirma el endoso si cumple la validacion de fechas: 
	                 respConfirmacionEndoso = this.confirmarEndoso(
	                        cdunieco
	                        ,cdramo
	                        ,estado
	                        ,nmpoliza
	                        ,nmsuplem
	                        ,nsuplogi
	                        ,cdtipsup
	                        ,""
	                        ,dFecha
	                        ,cdtipsit
	                        ,flujo
	                        ,cdusuari
	                        ,cdsisrol
	                        );
				}*/
				
				if(smap2.get("confirmar").equals("si")){// Si el endoso fue confirmado:
				 // Se confirma el endoso si cumple la validacion de fechas: 
                    respConfirmacionEndoso = this.confirmarEndoso(
                           cdunieco
                           ,cdramo
                           ,estado
                           ,nmpoliza
                           ,nmsuplem
                           ,nsuplogi
                           ,cdtipsup
                           ,""
                           ,dFecha
                           ,cdtipsit
                           ,flujo
                           ,cdusuari
                           ,cdsisrol
                           );
                    
					if(respConfirmacionEndoso.isConfirmado())
						{
							endosoConfirmado = true;
							
							paso = "Generando documentos";
							logger.debug(paso);
							
							// Regeneramos los documentos:
							String nmsolici=this.regeneraDocumentos(cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdtipsup, ntramite,cdusuari);
							
							String sucursal = cdunieco;
							
							if(esProductoSalud)
							{
								// Ejecutamos el Web Service de Recibos: 
								ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo, 
										estado, nmpoliza, 
										nmsuplem, null, 
										sucursal, nmsolici, ntramite, 
										true, cdtipsup, 
										(UserVO) session.get("USUARIO"));
							}
							else
							{
								paso = "Realizando endoso en Web Service Autos";
								logger.debug(paso);
								
								EmisionAutosVO aux = emisionAutosService.cotizaEmiteAutomovilWS(cdunieco, cdramo, estado, nmpoliza, nmsuplem, ntramite, null, (UserVO) session.get("USUARIO"));
								if(aux == null || !aux.isExitoRecibos())
								{
									success = false;
									mensaje = "Error al generar el endoso, en WS. Consulte a Soporte.";
									error   = "Error al generar el endoso, en WS. Consulte a Soporte.";
									logger.error("Error al ejecutar los WS de endoso");
									
									boolean endosoRevertido = endososManager.revierteEndosoFallido(cdunieco, cdramo, estado, nmpoliza, nsuplogi, nmsuplem, (aux == null)? Integer.valueOf(99999) : aux.getResRecibos(), "Error en endoso Tavalosit auto, tipo: "+TipoEndoso.CAMBIO_FORMA_PAGO.toString(), false);
									
									if(endosoRevertido){
										logger.error("Endoso revertido exitosamente.");
										error+=" Favor de volver a intentar.";
									}else{
										logger.error("Error al revertir el endoso");
										error+=" No se ha revertido el endoso.";
									}
									
									if(aux!=null && aux.isEndosoSinRetarif()){
							    		throw new ApplicationException("Endoso sin Tarifa. "+error);
							    	}
									
									return SUCCESS;
								}
								
								String tipoGrupoInciso = smap1.get("TIPOFLOT");
								
								ejecutaCaratulaEndosoTarifaSigs(cdunieco,cdramo,estado,nmpoliza,nmsuplem, ntramite, cdtipsup, tipoGrupoInciso, aux, null);
							}
							
							mensaje="Se ha guardado el endoso "+nsuplogi;
							
						} else {
							mensaje="El endoso "+nsuplogi
									+" se guard\u00f3 en mesa de control para autorizaci\u00f3n "
									+ "con n\u00famero de tr\u00e1mite " + respConfirmacionEndoso.getNumeroTramite();
						}
			}
				if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
                }
				success=true;
				
			}
			catch(Exception ex)
			{
				Utils.generaExcepcion(ex, paso);
			}
		}
		catch(Exception ex)
		{
			error = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### mensaje = " , mensaje
				,"\n###### error   = " , error
				,"\n###### smap2   = " , smap2
				,"\n###### guardarEndosoFormaPago ######"
				,"\n####################################"
				));
		
		return SUCCESS;
	}
	/*///////////////////////////////////*/
	////// guardarEndosoReexpedicion //////
	///////////////////////////////////////
	
	//////////////////////////
	////// endosoAgente //////
	/*
	smap1:
		CDRAMO      : "2"
		CDTIPSIT    : "SL"
		CDUNIECO    : "1002"
		DSCOMENT    : ""
		DSTIPSIT    : "SALUD VITAL"
		ESTADO      : "M"
		FEEMISIO    : "30/01/2014"
		FEINIVAL    : "15/01/2014"
		NMPOLIEX    : "1002213000064000000"
		NMPOLIZA    : "64"
		NMSUPLEM    : "245667313410000000"
		NSUPLOGI    : "4"
		NTRAMITE    : null
		PRIMA_TOTAL : "17339.97"
	*/
	/*//////////////////////*/
	public String endosoAgente()
	{
		logger.debug(Utils.log(
				 "\n##########################"
				,"\n###### endosoAgente ######"
				,"\n###### smap1=" , smap1
				,"\n###### flujo=" , flujo
				));
		
		this.session=ActionContext.getContext().getSession();
		
		RespuestaVO resp = null;
		
		try
		{
			transformaEntrada(smap1, slist1, false);
			
			///////////////////////
			////// variables //////
			String cdunieco           = smap1.get("CDUNIECO");
			String cdramo             = smap1.get("CDRAMO");
			String cdtipsit           = smap1.get("CDTIPSIT");
			String estado             = smap1.get("ESTADO");
			String nmpoliza           = smap1.get("NMPOLIZA");
			String nmsuplem           = smap1.get("NMSUPLEM");
			String rol                = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
			String orden              = null;
			String pantalla           = "ENDOSO_AGENTE";
			String seccionLectura     = "PANEL_LECTURA";
			String seccionModelo      = "MODELO";
			String seccionComboAgente = "COMBOAGENTE";
			String keyItemsPanelLec   = "itemsPanelLectura";
			String keyFieldsModelo    = "fieldsModelo";
			String keyColumnsGrid     = "columnsGrid";
			String keyComboAgentes    = "comboAgentes";
			String cdtipsup           = TipoEndoso.CAMBIO_AGENTE.getCdTipSup().toString();
			////// variables //////
			///////////////////////
			
			// Valida si hay un endoso anterior pendiente:
			resp = endososManager.validaEndosoAnterior(cdunieco, cdramo, estado, nmpoliza, cdtipsup);
			error = resp.getMensaje();
			
			if(resp.isSuccess()) {
				try {
					/////////////////////////////
					////// campos pantalla //////
					GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
					gc.generaParcial(pantallasManager.obtenerComponentes(
							null, cdunieco, cdramo,
							cdtipsit, estado, rol,
							pantalla, seccionLectura, orden));
					
					imap1=new HashMap<String,Item>();
					imap1.put(keyItemsPanelLec,gc.getItems());
					
					gc.generaParcial(pantallasManager.obtenerComponentes(
							null, cdunieco, cdramo,
							cdtipsit, estado, rol,
							pantalla, seccionModelo, orden));
					
					imap1.put(keyFieldsModelo,gc.getFields());
					imap1.put(keyColumnsGrid,gc.getColumns());
					
					gc.generaParcial(pantallasManager.obtenerComponentes(
							null, cdunieco, cdramo,
							cdtipsit, estado, rol,
							pantalla, seccionComboAgente, orden));
					
					imap1.put(keyComboAgentes,gc.getItems());
					
					Date fechaInicioEndoso=endososManager.obtenerFechaEndosoFormaPago(cdunieco, cdramo, estado, nmpoliza);
					smap1.put("fechaInicioEndoso",renderFechas.format(fechaInicioEndoso));
					////// campos pantalla //////
					/////////////////////////////
				} catch(Exception ex) {
					logger.error("error al cargar la pantalla de endoso de agente",ex);
					error=ex.getMessage();
				}
			}
		}
		catch(Exception ex)
		{
			logger.error("Error al construir pantalla de endoso de cambio de agente",ex);
			error = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### endosoAgente ######"
				,"\n##########################"
				));
		
		return resp!=null&&resp.isSuccess() ? SUCCESS : ERROR;
	}
	/*//////////////////////*/
	////// endosoAgente //////
	//////////////////////////
	
	///////////////////////////////////////
	////// cargarAgentesEndosoAgente //////
	/*///////////////////////////////////*/
	public String cargarAgentesEndosoAgente()
	{
		logger.debug("\n"
				+ "\n#######################################"
				+ "\n#######################################"
				+ "\n###### cargarAgentesEndosoAgente ######"
				+ "\n######                           ######"
				);
		logger.debug("smap1: "+smap1);
		try
		{
			String cdunieco = smap1.get("cdunieco");
			String cdramo   = smap1.get("cdramo");
			String estado   = smap1.get("estado");
			String nmpoliza = smap1.get("nmpoliza");
			String nmsuplem = smap1.get("nmsuplem");
			
			/*
			PKG_CONSULTA.P_GET_AGENTE_POLIZA
			a.cdunieco, a.cdramo, a.estado, a.nmpoliza, a.cdagente, a.nmsuplem, a.status, a.cdtipoag, porredau, a.porparti,nombre
			*/
			slist1=endososManager.obtenerAgentesEndosoAgente(cdunieco, cdramo, estado, nmpoliza, ""/*nmsuplem*/);
			
			for(Map<String,String>agente:slist1)
			{
				if(agente.get("porredau")!=null&&((Double)Double.parseDouble(agente.get("porredau")))>(double)0)
				{
					throw new Exception("Esta p\u00f3liza no permite el endoso de agente por tener sesi\u00f3n de comisi\u00f3n");
				}
			}
			
			success=true;
		}
		catch(Exception ex)
		{
			logger.error("error al cargar agentes para el endoso de agente",ex);
			error=ex.getMessage();
			success=false;
		}
		logger.debug("\n"
				+ "\n######                           ######"
				+ "\n###### cargarAgentesEndosoAgente ######"
				+ "\n#######################################"
				+ "\n#######################################"
				);
		return SUCCESS;
	}
	/*///////////////////////////////////*/
	////// cargarAgentesEndosoAgente //////
	///////////////////////////////////////
	
	/////////////////////////////////
	////// guardarEndosoAgente //////
	/*
	smap1:
		CDRAMO: "2"
		CDTIPSIT: "SL"
		CDUNIECO: "1002"
		DSCOMENT: ""
		DSTIPSIT: "SALUD VITAL"
		ESTADO: "M"
		FEEMISIO: "20/01/2014"
		FEINIVAL: "20/01/2014"
		NMPOLIEX: "1002213000019000000"
		NMPOLIZA: "19"
		NMSUPLEM: "245667814480000000"
		NSUPLOGI: "0"
		NTRAMITE: "573"
		PRIMA_TOTAL: "52694.6"
	smap2:
		agente: "11000"
		fecha_endoso: "31/01/2014"
		nmcuadro
		cdsucurs
	slist1:
		{NMSUPLEM=245667814480000000,
		NOMBRE="JIRO Y ASOCIADOS, AGENTE DE   "  "SEGUROS Y FIANZAS, S" .A. DE C.V.,
		NMPOLIZA=19,
		CDAGENTE=1170,
		STATUS=V,
		NMCUADRO=SV18,
		ESTADO=M,
		PORPARTI=100,
		CDUNIECO=1002,
		CDRAMO=2,
		CDTIPOAG=1,
		PORREDAU=0,
		CDSUCURS=null}
	*/
	/*/////////////////////////////*/
	public String guardarEndosoAgente()
	{
		logger.debug(Utils.log(
				 "\n#################################"
				,"\n###### guardarEndosoAgente ######"
				,"\n###### smap1="  , smap1
				,"\n###### smap2="  , smap2
				,"\n###### slist1=" , slist1
				,"\n###### flujo="  , flujo
				));
		
		this.session=ActionContext.getContext().getSession();
		try {
			////// variables //////
			String cdunieco            = smap1.get("CDUNIECO");
			String cdramo              = smap1.get("CDRAMO");
			String estado              = smap1.get("ESTADO");
			String nmpoliza            = smap1.get("NMPOLIZA");
			String sFecha              = smap2.get("fecha_endoso");
			Date   dFecha            = renderFechas.parse(sFecha);
			UserVO usuario             = (UserVO)session.get("USUARIO");
			String cdelemento          = usuario.getEmpresa().getElementoId();
			String cdusuari            = usuario.getUser();
			String cdsisrol            = usuario.getRolActivo().getClave();
			String proceso             = "END";
			String cdtipsup            = TipoEndoso.CAMBIO_AGENTE.getCdTipSup().toString();
			String comentariosEndoso   = "";
			String cdtipsit            = smap1.get("CDTIPSIT");
			String ntramite            = smap1.get("NTRAMITE");
			
			
			boolean esProductoSalud = consultasManager.esProductoSalud(cdramo);
			
			endososAutoManager.validarEndosoPagados(cdunieco, cdramo, estado, nmpoliza, cdtipsup);
			
			//PKG_ENDOSOS.P_ENDOSO_INICIA
			Map<String,String>resIniEnd=endososManager.iniciarEndoso(cdunieco, cdramo, estado, nmpoliza, sFecha, cdelemento, cdusuari, proceso, cdtipsup);
			
			String nmsuplem = resIniEnd.get("pv_nmsuplem_o");
			String nsuplogi = resIniEnd.get("pv_nsuplogi_o");
			
			//matar anteriores
			for(Map<String,String>agenteIte:slist1) {
				String cdagenteOrigIte = agenteIte.get("CDAGENTEORIG");
				String cdagenteIte = agenteIte.get("CDAGENTE");
				String cdtipoagIte = agenteIte.get("CDTIPOAG");
				String porredauIte = agenteIte.get("PORREDAU");
				String nmcuadroIte = agenteIte.get("NMCUADRO");
				String cdsucursIte = agenteIte.get("CDSUCURS");
				String porpartiIte = agenteIte.get("PORPARTI");
				
				/**
				 * insertar muerto
				 * PKG_SATELITES.P_MOV_MPOLIAGE
				 */
				endososManager.pMovMpoliage(cdunieco, cdramo, estado, nmpoliza,
						cdagenteOrigIte, nmsuplem, Constantes.STATUS_VIVO, cdtipoagIte, porredauIte,
						nmcuadroIte, cdsucursIte, Constantes.DELETE_MODE, ntramite, porpartiIte);
				
				/**
				 * insertar vivo
				 * PKG_SATELITES.P_MOV_MPOLIAGE
				 */
				endososManager.pMovMpoliage(cdunieco, cdramo, estado, nmpoliza,
						cdagenteIte, nmsuplem, Constantes.STATUS_VIVO, cdtipoagIte, porredauIte,
						nmcuadroIte, cdsucursIte, Constantes.INSERT_MODE, ntramite, porpartiIte);
			}
			
			endososManager.calcularRecibosCambioAgente(cdunieco,cdramo,estado,nmpoliza,nmsuplem);
			
	   		//// Se confirma el endoso si cumple la validacion de fechas:
			RespuestaConfirmacionEndosoVO respConfirmacionEndoso = confirmarEndoso(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplem
					,nsuplogi
					,cdtipsup
					,comentariosEndoso
					,dFecha
					,cdtipsit
					,flujo
					,cdusuari
					,cdsisrol
					);
	   		
			// Si el endoso fue confirmado:
			if(respConfirmacionEndoso.isConfirmado()) {
				endosoConfirmado = true;
	   			
				// Regeneramos los documentos:
	   			String nmsolici=this.regeneraDocumentos(cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdtipsup, ntramite,cdusuari);
	   			
				String sucursal = cdunieco;
				
				if(esProductoSalud) {
					// Ejecutamos el Web Service de Recibos:
		   			ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo, 
							estado, nmpoliza, 
							nmsuplem, null, 
							sucursal, nmsolici, ntramite, 
							true, cdtipsup, 
							(UserVO) session.get("USUARIO"));
				}else{
					EmisionAutosVO aux = emisionAutosService.cotizaEmiteAutomovilWS(cdunieco, cdramo, estado, nmpoliza, nmsuplem, ntramite, null, (UserVO) session.get("USUARIO"));
					if(aux == null || !aux.isExitoRecibos()){
						success = false;
						mensaje = "Error al generar el endoso, en WS. Consulte a Soporte.";
						error   = "Error al generar el endoso, en WS. Consulte a Soporte.";
						logger.error("Error al ejecutar los WS de endoso");
						
						boolean endosoRevertido = endososManager.revierteEndosoFallido(cdunieco, cdramo, estado, nmpoliza, nsuplogi, nmsuplem, (aux == null)? Integer.valueOf(99999) : aux.getResRecibos(), "Error en endoso Tavalosit auto, tipo: "+TipoEndoso.CAMBIO_AGENTE.toString(), false);
						
						if(endosoRevertido){
							logger.error("Endoso revertido exitosamente.");
							error+=" Favor de volver a intentar.";
						}else{
							logger.error("Error al revertir el endoso");
							error+=" No se ha revertido el endoso.";
						}
						
						if(aux!=null && aux.isEndosoSinRetarif()){
				    		throw new ApplicationException("Endoso sin Tarifa. "+error);
				    	}
						
						return SUCCESS;
					}
					
					String tipoGrupoInciso = smap1.get("TIPOFLOT");
					
					ejecutaCaratulaEndosoTarifaSigs(cdunieco,cdramo,estado,nmpoliza,nmsuplem, ntramite, cdtipsup, tipoGrupoInciso, aux, null);
				}
	   			
	   			mensaje="Se ha guardado el endoso "+nsuplogi;
	   			
			} else {
				mensaje="El endoso "+nsuplogi
						+" se guard\u00f3 en mesa de control para autorizaci\u00f3n "
						+ "con n\u00famero de tr\u00e1mite " + respConfirmacionEndoso.getNumeroTramite();
			}
			if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
                mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
            }
			success=true;
			
		} catch(Exception ex) {
			logger.error("Error al guardar endoso de agente", ex);
			success = false;
			error = ex.getMessage();
		}
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### error   = " , error
				,"\n###### mensaje = " , mensaje
				,"\n###### guardarEndosoAgente ######"
				,"\n#################################"
				));
		return SUCCESS;
	}
	/*/////////////////////////////*/
	////// guardarEndosoAgente //////
	/////////////////////////////////
	
	
	//////////////////////////
	////// endosoContratante //////
	/*
	smap1:
	CDRAMO      : "2"
	CDTIPSIT    : "SL"
	CDUNIECO    : "1002"
	DSCOMENT    : ""
	DSTIPSIT    : "SALUD VITAL"
	ESTADO      : "M"
	FEEMISIO    : "30/01/2014"
	FEINIVAL    : "15/01/2014"
	NMPOLIEX    : "1002213000064000000"
	NMPOLIZA    : "64"
	NMSUPLEM    : "245667313410000000"
	NSUPLOGI    : "4"
	NTRAMITE    : null
	PRIMA_TOTAL : "17339.97"
	*/
	/*//////////////////////*/
	public String endosoContratante() {
	
		logger.debug(new StringBuilder("\n")
		.append("\n###############################")
		.append("\n###############################")
		.append("\n###### endosoContratante ######")
		.append("\n######                   ######").toString());
		logger.debug(new StringBuilder("smap1: ").append(smap1).toString());
		
		this.session=ActionContext.getContext().getSession();
		
		RespuestaVO resp = null;
		
		try
		{
			transformaEntrada(smap1, slist1, false);
			
			///////////////////////
			////// variables //////
			String cdunieco           = smap1.get("CDUNIECO");
			String cdramo             = smap1.get("CDRAMO");
			String cdtipsit           = smap1.get("CDTIPSIT");
			String estado             = smap1.get("ESTADO");
			String nmpoliza           = smap1.get("NMPOLIZA");
			String nmsuplem           = smap1.get("NMSUPLEM");
			String rol                = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
			String orden              = null;
			String pantalla           = "ENDOSO_CONTRATANTE";
			String seccionLectura     = "PANEL_LECTURA";
			String seccionModelo      = "MODELO";
			String keyItemsPanelLec   = "itemsPanelLectura";
			String keyFieldsModelo    = "fieldsModelo";
			String keyColumnsGrid     = "columnsGrid";
			String cdtipsup           = TipoEndoso.CAMBIO_CONTRATANTE.getCdTipSup().toString();
			////// variables //////
			///////////////////////
			
			// Valida si hay un endoso anterior pendiente:
			resp = endososManager.validaEndosoAnterior(cdunieco, cdramo, estado, nmpoliza, cdtipsup);
			error = resp.getMensaje();
			
			if(resp.isSuccess()) {
				try {
					/////////////////////////////
					////// campos pantalla //////
					GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
					gc.generaParcial(pantallasManager.obtenerComponentes(
						null, cdunieco, cdramo,
						cdtipsit, estado, rol,
						pantalla, seccionLectura, orden));
					
					imap1=new HashMap<String,Item>();
					imap1.put(keyItemsPanelLec,gc.getItems());
					
					gc.generaParcial(pantallasManager.obtenerComponentes(
						null, cdunieco, cdramo,
						cdtipsit, estado, rol,
						pantalla, seccionModelo, orden));
					
					imap1.put(keyFieldsModelo,gc.getFields());
					imap1.put(keyColumnsGrid,gc.getColumns());
					
					
					Date fechaInicioEndoso=endososManager.obtenerFechaEndosoFormaPago(cdunieco, cdramo, estado, nmpoliza);
					smap1.put("fechaInicioEndoso",renderFechas.format(fechaInicioEndoso));
					
					boolean esProductoSalud = consultasManager.esProductoSalud(cdramo);
					
					if(!esProductoSalud){
						//smap1.put("fechaInicioEndoso",renderFechas.format(new Date()));
						smap1.put("fechaInicioEndoso",renderFechas.format(fechaInicioEndoso));
						smap1.put("cdtipsup",cdtipsup);
					}
					
					
					
					smap1.put("esProductoSalud", esProductoSalud? Constantes.SI : Constantes.NO);
					
					////// campos pantalla //////
					/////////////////////////////
				} catch(Exception ex) {
					logger.error("error al cargar la pantalla de endoso de contratante",ex);
					error=ex.getMessage();
				}
			}
		}
		catch(Exception ex)
		{
			logger.error("Error al constuir pantalla de endoso de cambio de contratante",ex);
			error = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(new StringBuilder("\n")
		.append("\n######                   ######")
		.append("\n###### endosoContratante ######")
		.append("\n###############################")
		.append("\n###############################").toString());
		
		return resp!=null&&resp.isSuccess() ? SUCCESS : ERROR;
	}
	/*//////////////////////*/
	////// endosoContratante //////
	//////////////////////////
	
	///////////////////////////////////////
	////// cargarContratantesEndosoContratante //////
	/*///////////////////////////////////*/
	public String cargarContratantesEndosoContratante()
	{
	logger.debug("\n"
	+ "\n#################################################"
	+ "\n#################################################"
	+ "\n###### cargarContratantesEndosoContratante ######"
	+ "\n######                                     ######"
	);
	logger.debug("smap1: "+smap1);
	try
	{
	String cdunieco = smap1.get("cdunieco");
	String cdramo   = smap1.get("cdramo");
	String estado   = smap1.get("estado");
	String nmpoliza = smap1.get("nmpoliza");
	String nmsuplem = smap1.get("nmsuplem");
	
	/*
	PKG_CONSULTA.P_GET_AGENTE_POLIZA
	a.cdunieco, a.cdramo, a.estado, a.nmpoliza, a.cdcontratante, a.nmsuplem, a.status, a.cdtipoag, porredau, a.porparti,nombre
	*/
	slist1=consultasManager.obtieneContratantePoliza(cdunieco, cdramo, estado, nmpoliza, null, "1", null);
	
	success=true;
	}
	catch(Exception ex)
	{
	logger.error("error al cargar contratantes para el endoso de contratante",ex);
	error=ex.getMessage();
	success=false;
	}
	logger.debug("\n"
	+ "\n######                                     ######"
	+ "\n###### cargarContratantesEndosoContratante ######"
	+ "\n#################################################"
	+ "\n#################################################"
	);
	return SUCCESS;
	}
	/*///////////////////////////////////*/
	////// cargarContratantesEndosoContratante //////
	///////////////////////////////////////
	
	/////////////////////////////////
	////// guardarEndosoContratante //////
	/*
	smap1:
	CDRAMO: "2"
	CDTIPSIT: "SL"
	CDUNIECO: "1002"
	DSCOMENT: ""
	DSTIPSIT: "SALUD VITAL"
	ESTADO: "M"
	FEEMISIO: "20/01/2014"
	FEINIVAL: "20/01/2014"
	NMPOLIEX: "1002213000019000000"
	NMPOLIZA: "19"
	NMSUPLEM: "245667814480000000"
	NSUPLOGI: "0"
	NTRAMITE: "573"
	PRIMA_TOTAL: "52694.6"
	smap2:
	contratante: "11000"
	fecha_endoso: "31/01/2014"
	nmcuadro
	cdsucurs
	slist1:
	{NMSUPLEM=245667814480000000,
	NOMBRE="JIRO Y ASOCIADOS, AGENTE DE   "  "SEGUROS Y FIANZAS, S" .A. DE C.V.,
	NMPOLIZA=19,
	CDAGENTE=1170,
	STATUS=V,
	NMCUADRO=SV18,
	ESTADO=M,
	PORPARTI=100,
	CDUNIECO=1002,
	CDRAMO=2,
	CDTIPOAG=1,
	PORREDAU=0,
	CDSUCURS=null}
	*/
	/*/////////////////////////////*/
	public String guardarEndosoContratante()
	{
		logger.debug(Utils.log(
				 "\n######################################"
				,"\n###### guardarEndosoContratante ######"
				,"\n###### smap1  = " , smap1
				,"\n###### smap2  = " , smap2
				,"\n###### slist1 = " , slist1
				,"\n###### flujo  = " , flujo
				));
		
		this.session=ActionContext.getContext().getSession();
		
		try
		{
			////// variables //////
			String cdunieco            = smap1.get("CDUNIECO");
			String cdramo              = smap1.get("CDRAMO");
			String estado              = smap1.get("ESTADO");
			String nmpoliza            = smap1.get("NMPOLIZA");
			String sFecha              = smap2.get("fecha_endoso");
			Date   dFecha              = renderFechas.parse(sFecha);
			UserVO usuario             = (UserVO)session.get("USUARIO");
			String cdelemento          = usuario.getEmpresa().getElementoId();
			String cdusuari            = usuario.getUser();
			String cdsisrol            = usuario.getRolActivo().getClave();
			String proceso             = "END";
			String cdtipsup            = TipoEndoso.CAMBIO_CONTRATANTE.getCdTipSup().toString();
			//String cdcontratante            = smap2.get("contratante");
			String tipoContratantePrincipal = "1";
			String sesionComision      = "0";
			String porcenParticip      = "100";
			//String nmcuadro            = smap2.get("nmcuadro");
			//String cdsucurs            = smap2.get("cdsucurs");
			String comentariosEndoso   = "";
			String cdtipsit            = smap1.get("CDTIPSIT");
			String ntramite            = smap1.get("NTRAMITE");
			String cdpostalNuevo       =  null;
			
			String paso = null;
			
			try // Esto deberia ir en un manager
			{
			
				long timestamp=System.currentTimeMillis();
				
				paso = "Recuperando tipo de producto";
				logger.debug(paso);
				
				boolean esProductoSalud = consultasManager.esProductoSalud(cdramo);
				
				paso = "Validando endosos pagados";
				logger.debug(paso);
				
				endososAutoManager.validarEndosoPagados(cdunieco, cdramo, estado, nmpoliza, cdtipsup);
				
				paso = "Recuperando domicilio";
				logger.debug(paso);
				
				Map<String,Object> managerResult = personasManager.obtenerDomicilioPorCdperson(smap2.get("cdpersonNvoContr"), null, timestamp);
				Map<String,String> domicilioNvo  = (Map<String,String>)managerResult.get("domicilio");
				
				if(domicilioNvo.containsKey("CODPOSTAL") && StringUtils.isNotBlank(domicilioNvo.get("CODPOSTAL"))){
					logger.debug("Nuevo codigo postal del contratante: "+ domicilioNvo.get("CODPOSTAL"));
					cdpostalNuevo = domicilioNvo.get("CODPOSTAL");
				}else{
					throw new ApplicationException("No se tiene el nuevo codigo postal para este contratante.");
				}
				
				paso = "Iniciando endoso";
				logger.debug(paso);
				
				//PKG_ENDOSOS.P_ENDOSO_INICIA
				Map<String,String>resIniEnd=endososManager.iniciarEndoso(cdunieco, cdramo, estado, nmpoliza, sFecha, cdelemento, cdusuari, proceso, cdtipsup);
				
				String nmsuplem = resIniEnd.get("pv_nmsuplem_o");
				String nsuplogi = resIniEnd.get("pv_nsuplogi_o");
				
				paso = "Insertando registro inactivo";
				logger.debug(paso);
				
				Map<String,String>contratanteIte =  slist1.get(0);
				
				//* insertar muerto
				HashMap<String,Object> paramsMpopliper = new HashMap<String, Object>();
				paramsMpopliper.put("pv_cdunieco_i", cdunieco);
				paramsMpopliper.put("pv_cdramo_i"  , cdramo);
				paramsMpopliper.put("pv_estado_i"  , estado);
				paramsMpopliper.put("pv_nmpoliza_i", nmpoliza);
				paramsMpopliper.put("pv_nmsituac_i", contratanteIte.get("NMSITUAC"));
				paramsMpopliper.put("pv_cdrol_i"   , contratanteIte.get("CDROL"));
				paramsMpopliper.put("pv_cdperson_i", contratanteIte.get("CDPERSON"));
				paramsMpopliper.put("pv_nmsuplem_i", nmsuplem);
				paramsMpopliper.put("pv_status_i"  , contratanteIte.get("STATUS"));//aunque el estatus se envia igual la accion de Eliminar lo fija en muerto
				paramsMpopliper.put("pv_nmorddom_i", contratanteIte.get("NMORDDOM"));
				paramsMpopliper.put("pv_swreclam_i", contratanteIte.get("SWRECLAM"));
				paramsMpopliper.put("pv_accion_i", 	 Constantes.DELETE_MODE);
				paramsMpopliper.put("pv_swexiper_i", "S");
				
				kernelManager.movMpoliper(paramsMpopliper);
				
				paso = "Insertando registro activo";
				logger.debug(paso);
				
				//* insertar vivo
				paramsMpopliper.put("pv_cdperson_i", smap2.get("cdpersonNvoContr"));
				paramsMpopliper.put("pv_accion_i", 	 Constantes.INSERT_MODE);
				
				//por default se manda al primer domicilio
				paramsMpopliper.put("pv_nmorddom_i", "1");
				
				kernelManager.movMpoliper(paramsMpopliper);
				
				if(esProductoSalud){
					
					paso = "Calculando recibos";
					logger.debug(paso);
					
					endososManager.calcularRecibosCambioContratante(cdunieco,cdramo,estado,nmpoliza,nmsuplem);
				}else{
					
					paso = "Recuperando valores de atributos";
					logger.debug(paso);
					
					//PKG_CONSULTA.P_OBT_VALOSIT_ULTIMA_IMAGEN
					List<Map<String,String>>valositsPoliza=endososManager.obtenerValositUltimaImagen(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
					/*
					CDUNIECO,CDRAMO,ESTADO,NMPOLIZA,NMSITUAC,NMSUPLEM,STATUS,CDTIPSIT,OTVALOR01,OTVALOR02
					,OTVALOR03,OTVALOR04,OTVALOR05,OTVALOR06,OTVALOR07,OTVALOR08,OTVALOR09,OTVALOR10,OTVALOR11
					,OTVALOR12,OTVALOR13,OTVALOR14,OTVALOR15,OTVALOR16,OTVALOR17,OTVALOR18,OTVALOR19,OTVALOR20
					,OTVALOR21,OTVALOR22,OTVALOR23,OTVALOR24,OTVALOR25,OTVALOR26,OTVALOR27,OTVALOR28,OTVALOR29
					,OTVALOR30,OTVALOR31,OTVALOR32,OTVALOR33,OTVALOR34,OTVALOR35,OTVALOR36,OTVALOR37,OTVALOR38
					,OTVALOR39,OTVALOR40,OTVALOR41,OTVALOR42,OTVALOR43,OTVALOR44,OTVALOR45,OTVALOR46,OTVALOR47
					,OTVALOR48,OTVALOR49,OTVALOR50
					 */
					
					for(Map<String,String>valositIte:valositsPoliza) {
						String nmsituacIte=valositIte.get("NMSITUAC");
						String keyCodPostal = "pv_otvalor"+valositIte.get("CDATRIBU");
						logger.debug("OTvalor encontrado para CP: "  +keyCodPostal);
						
						//otvalor05 -> pv_otvalor05
						Map<String,String>otvalorValositIte=new HashMap<String,String>();
						for(Entry<String,String>en:valositIte.entrySet()) {
							String key   = en.getKey();
							String value = en.getValue();
							
							if(key.substring(0,5).equalsIgnoreCase("otval")) {
								otvalorValositIte.put("pv_"+(key.toLowerCase()),value);
							}
						}
						
						otvalorValositIte.put(keyCodPostal , cdpostalNuevo);
						
						paso = "Guardando valores de atributos";
						logger.debug(paso);
						
						//PKG_SATELITES2.P_MOV_TVALOSIT
						kernelManager.insertaValoresSituaciones(cdunieco, cdramo, estado, nmpoliza
								,nmsituacIte, nmsuplem, Constantes.STATUS_VIVO, valositIte.get("CDTIPSIT"), Constantes.INSERT_MODE, otvalorValositIte);
					}
					
					
					//////////////////////////////
					////// inserta tworksup ////// 
		//			SE COMENTA PARA NO TARIFICAR
		//			Map<String,String>mapaTworksupEnd=new LinkedHashMap<String,String>(0);
		//			mapaTworksupEnd.put("pv_cdunieco_i" , cdunieco);
		//			mapaTworksupEnd.put("pv_cdramo_i"   , cdramo);
		//			mapaTworksupEnd.put("pv_estado_i"   , estado);
		//			mapaTworksupEnd.put("pv_nmpoliza_i" , nmpoliza);
		//			mapaTworksupEnd.put("pv_cdtipsup_i" , cdtipsup);
		//			mapaTworksupEnd.put("pv_nmsuplem_i" , nmsuplem);
		//			endososManager.insertarTworksupSitTodas(mapaTworksupEnd);
					////// inserta tworksup //////
					//////////////////////////////
					
					//////////////////////////
					////// tarificacion //////
		//			SE COMENTA PARA NO TARIFICAR
		//			endososManager.sigsvalipolEnd(
		//					cdusuari
		//					,cdelemento
		//					,cdunieco
		//					,cdramo
		//					,estado
		//					,nmpoliza
		//					,"0"
		//					,nmsuplem
		//					,cdtipsup
		//					);
					////// tarificacion //////
					//////////////////////////
					
					/*
					 * Cancela Recibos Cambio Cliente.
					 */
		//			SE COMENTA PARA NO TARIFICAR
		//			endososManager.cancelaRecibosCambioCliente(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
					
					//////////////////////////
					////// valor endoso //////
		//			SE COMENTA PARA NO TARIFICAR
		//			Map<String,Object>mapaValorEndoso=new LinkedHashMap<String,Object>(0);
		//			mapaValorEndoso.put("pv_cdunieco_i" , cdunieco);
		//			mapaValorEndoso.put("pv_cdramo_i"   , cdramo);
		//			mapaValorEndoso.put("pv_estado_i"   , estado);
		//			mapaValorEndoso.put("pv_nmpoliza_i" , nmpoliza);
		//			mapaValorEndoso.put("pv_nmsituac_i" , "1");
		//			mapaValorEndoso.put("pv_nmsuplem_i" , nmsuplem);
		//			mapaValorEndoso.put("pv_feinival_i" , dFecha);
		//			mapaValorEndoso.put("pv_cdtipsup_i" , cdtipsup);
		//			endososManager.calcularValorEndoso(mapaValorEndoso);
					////// valor endoso //////
					//////////////////////////
				}
				
				
				//// Se confirma el endoso si cumple la validacion de fechas: 
				RespuestaConfirmacionEndosoVO respConfirmacionEndoso = confirmarEndoso(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,nmsuplem
						,nsuplogi
						,cdtipsup
						,comentariosEndoso
						,dFecha
						,cdtipsit
						,flujo
						,cdusuari
						,cdsisrol
						);
				
				// Si el endoso fue confirmado:
				if(respConfirmacionEndoso.isConfirmado()) {
					endosoConfirmado = true;
				
					// Regeneramos los documentos:
					String nmsolici=this.regeneraDocumentos(cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdtipsup, ntramite,cdusuari);
					String sucursal = cdunieco;
					
		
					if(!esProductoSalud){
						
						Map<String,Object>resultData = personasManager.obtenerPersonaPorCdperson(smap2.get("cdpersonNvoContr"), timestamp);
						Map<String,String>resultPer =  (Map<String, String>) resultData.get("persona");
						
						if( resultPer!=null && resultPer.containsKey("CDIDEPER") && StringUtils.isBlank(resultPer.get("CDIDEPER"))){
							String saludDanios = "D";
							logger.debug("...Crear nueva Persona en WS desde endoso Contratante... , Compania: " + saludDanios);
							
							ClienteGeneral clienteGeneral = new ClienteGeneral();
							clienteGeneral.setClaveCia(saludDanios);
							
							ClienteGeneralRespuesta clientesRes = ice2sigsService.ejecutaWSclienteGeneral(null, null, null, null, null, null, smap2.get("cdpersonNvoContr"), Ice2sigsService.Operacion.INSERTA, clienteGeneral, null, false);
							
							if(clientesRes == null || (Estatus.EXITO.getCodigo() != clientesRes.getCodigo())){
								
								logger.error("Error en WS, exito false,Error al crear codigo externo de cliente, en endoso de contratante");
								mensaje = "Error al generar el endoso, en WS. Consulte a Soporte.";
								error   = "Error al generar el endoso, en WS. Consulte a Soporte.";
								
								boolean endosoRevertido = endososManager.revierteEndosoFallido(cdunieco, cdramo, estado, nmpoliza, nsuplogi, nmsuplem, 88888, "Error en endoso B tipo: "+TipoEndoso.CAMBIO_CONTRATANTE.toString(), true);
								if(endosoRevertido){
									logger.error("Endoso revertido exitosamente.");
									error+=" Favor de volver a intentar.";
								}else{
									logger.error("Error al revertir el endoso");
									error+=" No se ha revertido el endoso.";
								}
								
								success = false;
								return SUCCESS;
							}else{
								logger.debug("Codigo externo obtenido: " + clientesRes.getClientesGeneral()[0].getNumeroExterno());
								
								HashMap params = new HashMap<String, String>();
								params.put("pv_cdperson_i", smap2.get("cdpersonNvoContr"));
								params.put("pv_swsalud_i"  , saludDanios);
								params.put("pv_cdideper_i"  , clientesRes.getClientesGeneral()[0].getNumeroExterno());
								
								personasManager.actualizaCodigoExterno(params);
							
							}
						}
		
						/**
						 * PARA WS ENDOSO DE AUTOS
						 */
		//				SE COMENTA PARA NO TARIFICAR
		//				EmisionAutosVO aux = emisionAutosService.cotizaEmiteAutomovilWS(cdunieco, cdramo, estado, nmpoliza, nmsuplem, ntramite, null, (UserVO) session.get("USUARIO"));
		//				
		//				if(aux == null || (StringUtils.isBlank(aux.getNmpoliex()) && !aux.isEndosoSinRetarif())){
		//					
		//					mensaje = "Error al generar el endoso, en WS. Consulte a Soporte.";
		//					error   = "Error al generar el endoso, en WS. Consulte a Soporte.";
		//					logger.error("Error al ejecutar los WS de endoso");
		//					
		//					
		//					boolean endosoRevertido = endososManager.revierteEndosoFallido(cdunieco, cdramo, estado, nmpoliza, nsuplogi, nmsuplem, (aux == null)? Integer.valueOf(99999) : aux.getResRecibos(), "Error en endoso Tavalosit auto, tipo: "+TipoEndoso.CAMBIO_CONTRATANTE.toString(), false);
		//					if(endosoRevertido){
		//						logger.error("Endoso revertido exitosamente.");
		//						error+=" Favor de volver a intentar.";
		//					}else{
		//						logger.error("Error al revertir el endoso");
		//						error+=" No se ha revertido el endoso.";
		//					}
		//					
		//					success = false;
		//					return SUCCESS;
		//				}
						
						
						int numEndRes = 0;
		//				SE COMENTA PARA NO TARIFICAR
		//				if(aux.isEndosoSinRetarif()){
							
							/**
							 * PARA WS ENDOSO DE AUTOS
							 */
							numEndRes = emisionAutosService.endosoCambioClienteAutos(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
							
							if(numEndRes == 0){
								mensaje = "Error al generar el endoso Cambio de Contratante, sigs. Consulte a Soporte.";
								error = "Error al generar el endoso Cambio de Contratante, sigs. Consulte a Soporte.";
								logger.error("Error al ejecutar sp de endoso Cambio de Contratante sigs");
								
								boolean endosoRevertido = endososManager.revierteEndosoFallido(cdunieco, cdramo, estado, nmpoliza, nsuplogi, nmsuplem, 88888, "Error en endoso B tipo: "+TipoEndoso.CAMBIO_CONTRATANTE.toString(), true);
								
								if(endosoRevertido){
									logger.error("Endoso revertido exitosamente.");
									error+=" Favor de volver a intentar.";
								}else{
									logger.error("Error al revertir el endoso");
									error+=" No se ha revertido el endoso.";
								}
								
								success = false;
								return SUCCESS;
							}else{
								ejecutaCaratulaEndosoBsigs(cdunieco,cdramo,estado,nmpoliza,nmsuplem, ntramite, cdtipsup, Integer.toString(numEndRes));
							}
							
		//					SE COMENTA PARA NO TARIFICAR
							
		//				}else if(aux.isExitoRecibos()){
		//					
		//					String tipoGrupoInciso = smap1.get("TIPOFLOT");
		//					
		//					ejecutaCaratulaEndosoTarifaSigs(cdunieco,cdramo,estado,nmpoliza,nmsuplem, ntramite, cdtipsup, tipoGrupoInciso, aux);
		//					
		//				}else{
		//					mensaje = "Error al generar el endoso, sigs. Consulte a Soporte.";
		//					error = "Error al generar el endoso, sigs. Consulte a Soporte.";
		//					logger.error("Error al ejecutar sp de endoso sigs");
		//					
		//					success = false;
		//					return SUCCESS;
		//				}
					}else{
						
						/**
						 * TODO: No aplica aun ya que se como el cliente fue importado de Salud, si se crea uno de danios puede duplicarse en danios 
						 */
		//				if(Ramo.RECUPERA.getCdramo().equals(cdunieco)){
		//					Map<String,Object>resultData = personasManager.obtenerPersonaPorCdperson(smap2.get("cdpersonNvoContr"), timestamp);
		//					Map<String,String>resultPer =  (Map<String, String>) resultData.get("persona");
		//					
		//					if( resultPer!=null && resultPer.containsKey("CDIDEPER") && StringUtils.isBlank(resultPer.get("CDIDEPER"))){
		//						String saludDanios = "D";
		//						logger.debug("...Crear nueva Persona de danios en WS para RECUPERA..");
		//						
		//						ClienteGeneral clienteGeneral = new ClienteGeneral();
		//						clienteGeneral.setClaveCia(saludDanios);
		//						
		//						ClienteGeneralRespuesta clientesRes = ice2sigsService.ejecutaWSclienteGeneral(null, null, null, null, null, null, smap2.get("cdpersonNvoContr"), Ice2sigsService.Operacion.INSERTA, clienteGeneral, null, false);
		//						
		//						if(clientesRes == null || (Estatus.EXITO.getCodigo() != clientesRes.getCodigo())){
		//							
		//							logger.error("Error en WS, exito false,Error al crear codigo externo de cliente, en endoso de contratante");
		//							mensaje = "Error al generar el endoso, en WS. Consulte a Soporte.";
		//							error   = "Error al generar el endoso, en WS. Consulte a Soporte.";
		//							
		//							boolean endosoRevertido = endososManager.revierteEndosoFallido(cdunieco, cdramo, estado, nmpoliza, nsuplogi, nmsuplem, 88888, "Error en endoso B tipo: "+TipoEndoso.CAMBIO_CONTRATANTE.toString(), true);
		//							if(endosoRevertido){
		//								logger.error("Endoso revertido exitosamente.");
		//								error+=" Favor de volver a intentar.";
		//							}else{
		//								logger.error("Error al revertir el endoso");
		//								error+=" No se ha revertido el endoso.";
		//							}
		//							
		//							success = false;
		//							return SUCCESS;
		//						}else{
		//							logger.debug("Codigo externo obtenido: " + clientesRes.getClientesGeneral()[0].getNumeroExterno());
		//							
		//							HashMap params = new HashMap<String, String>();
		//							params.put("pv_cdperson_i", smap2.get("cdpersonNvoContr"));
		//							params.put("pv_swsalud_i"  , saludDanios);
		//							params.put("pv_cdideper_i"  , clientesRes.getClientesGeneral()[0].getNumeroExterno());
		//							
		//							personasManager.actualizaCodigoExterno(params);
		//						
		//						}
		//					}
		//				}
						
						// Ejecutamos el Web Service de Recibos:
						ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo, 
							estado, nmpoliza, 
							nmsuplem, null, 
							sucursal, nmsolici, ntramite, 
							true, cdtipsup, 
							(UserVO) session.get("USUARIO"));
					}
					
					
					mensaje="Se ha guardado el endoso "+nsuplogi;
				
				} else {
				mensaje="El endoso "+nsuplogi
					+" se guard\u00f3 en mesa de control para autorizaci\u00f3n "
					+ "con n\u00famero de tr\u00e1mite " + respConfirmacionEndoso.getNumeroTramite();
				}
				if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
                }
				success=true;
			}
			catch(Exception ex)
			{
				Utils.generaExcepcion(ex, paso);
			}
		}
		catch(Exception ex)
		{
			error = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### mensaje = " , mensaje
				,"\n###### error   = " , error
				,"\n###### guardarEndosoContratante ######"
				,"\n######################################"
				));
		
		return SUCCESS;
	}
	
	
	
	/*/////////////////////////////*/
	////// guardarEndosoContratante //////
	/////////////////////////////////
	
	//////////////////////////
	//////endosoNombreCliente //////
	/*
	smap1:
	CDRAMO      : "2"
	CDTIPSIT    : "SL"
	CDUNIECO    : "1002"
	DSCOMENT    : ""
	DSTIPSIT    : "SALUD VITAL"
	ESTADO      : "M"
	FEEMISIO    : "30/01/2014"
	FEINIVAL    : "15/01/2014"
	NMPOLIEX    : "1002213000064000000"
	NMPOLIZA    : "64"
	NMSUPLEM    : "245667313410000000"
	NSUPLOGI    : "4"
	NTRAMITE    : null
	PRIMA_TOTAL : "17339.97"
	 */
	/*//////////////////////*/
	public String endosoCambioClienteAuto() {
		
		logger.debug(new StringBuilder("\n")
		.append("\n#####################################")
		.append("\n#####################################")
		.append("\n###### endosoCambioClienteAuto ######")
		.append("\n######                         ######").toString());
		logger.debug(new StringBuilder("smap1: ").append(smap1).toString());
		
		this.session=ActionContext.getContext().getSession();
		
		///////////////////////
		//////variables //////
		String cdunieco           = smap1.get("CDUNIECO");
		String cdramo             = smap1.get("CDRAMO");
		String cdtipsit           = smap1.get("CDTIPSIT");
		String estado             = smap1.get("ESTADO");
		String nmpoliza           = smap1.get("NMPOLIZA");
		String nmsuplem           = smap1.get("NMSUPLEM");
		String rol                = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
		String orden              = null;
		String pantalla           = "ENDOSO_CONTRATANTE";
		String seccionLectura     = "PANEL_LECTURA";
		String seccionModelo      = "MODELO";
		String keyItemsPanelLec   = "itemsPanelLectura";
		String keyFieldsModelo    = "fieldsModelo";
		String keyColumnsGrid     = "columnsGrid";
		String cdtipsup           = TipoEndoso.CAMBIO_CONTRATANTE.getCdTipSup().toString();
		//////variables //////
		///////////////////////
		
		//Valida si hay un endoso anterior pendiente:
		RespuestaVO resp = endososManager.validaEndosoAnterior(cdunieco, cdramo, estado, nmpoliza, cdtipsup);
		error = resp.getMensaje();
		
		if(resp.isSuccess()) {
			try {
				/////////////////////////////
				//////campos pantalla //////
				GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
				gc.generaParcial(pantallasManager.obtenerComponentes(
						null, cdunieco, cdramo,
						cdtipsit, estado, rol,
						pantalla, seccionLectura, orden));
				
				imap1=new HashMap<String,Item>();
				imap1.put(keyItemsPanelLec,gc.getItems());
				
				gc.generaParcial(pantallasManager.obtenerComponentes(
						null, cdunieco, cdramo,
						cdtipsit, estado, rol,
						pantalla, seccionModelo, orden));
				
				imap1.put(keyFieldsModelo,gc.getFields());
				imap1.put(keyColumnsGrid,gc.getColumns());
				
				
				Date fechaInicioEndoso=endososManager.obtenerFechaEndosoFormaPago(cdunieco, cdramo, estado, nmpoliza);
				smap1.put("fechaInicioEndoso",renderFechas.format(fechaInicioEndoso));
				//////campos pantalla //////
				/////////////////////////////
			} catch(Exception ex) {
				logger.error("error al cargar la pantalla de endoso de nombre de cliente",ex);
				error=ex.getMessage();
			}
		}
		
		logger.debug(new StringBuilder("\n")
		.append("\n######                         ######")
		.append("\n###### endosoCambioClienteAuto ######")
		.append("\n#####################################")
		.append("\n#####################################").toString());
		
		return resp.isSuccess() ? SUCCESS : ERROR;
	}
	/*//////////////////////*/
	//////endosoNombreCliente //////
	//////////////////////////

	
	//////////////////////////
	//////endosoNombreCliente //////
	/*
	smap1:
	CDRAMO      : "2"
	CDTIPSIT    : "SL"
	CDUNIECO    : "1002"
	DSCOMENT    : ""
	DSTIPSIT    : "SALUD VITAL"
	ESTADO      : "M"
	FEEMISIO    : "30/01/2014"
	FEINIVAL    : "15/01/2014"
	NMPOLIEX    : "1002213000064000000"
	NMPOLIZA    : "64"
	NMSUPLEM    : "245667313410000000"
	NSUPLOGI    : "4"
	NTRAMITE    : null
	PRIMA_TOTAL : "17339.97"
	*/
	/*//////////////////////*/
	public String endosoNombreCliente() {
	
		logger.debug(new StringBuilder("\n")
		.append("\n#################################")
		.append("\n#################################")
		.append("\n###### endosoNombreCliente ######")
		.append("\n######                     ######").toString());
		logger.debug(new StringBuilder("smap1: ").append(smap1).toString());
		
		this.session=ActionContext.getContext().getSession();
		
		///////////////////////
		//////variables //////
		String cdunieco           = smap1.get("CDUNIECO");
		String cdramo             = smap1.get("CDRAMO");
		String cdtipsit           = smap1.get("CDTIPSIT");
		String estado             = smap1.get("ESTADO");
		String nmpoliza           = smap1.get("NMPOLIZA");
		String nmsuplem           = smap1.get("NMSUPLEM");
		String rol                = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
		String orden              = null;
		String pantalla           = "ENDOSO_CONTRATANTE";
		String seccionLectura     = "PANEL_LECTURA";
		String seccionModelo      = "MODELO";
		String keyItemsPanelLec   = "itemsPanelLectura";
		String keyFieldsModelo    = "fieldsModelo";
		String keyColumnsGrid     = "columnsGrid";
		String cdtipsup           = TipoEndoso.CAMBIO_NOMBRE_CLIENTE.getCdTipSup().toString();
		//////variables //////
		///////////////////////
		
		//Valida si hay un endoso anterior pendiente:
		RespuestaVO resp = endososManager.validaEndosoAnterior(cdunieco, cdramo, estado, nmpoliza, cdtipsup);
		error = resp.getMensaje();
		
		if(resp.isSuccess()) {
			try {
				/////////////////////////////
				//////campos pantalla //////
				GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
				gc.generaParcial(pantallasManager.obtenerComponentes(
				null, cdunieco, cdramo,
				cdtipsit, estado, rol,
				pantalla, seccionLectura, orden));
				
				imap1=new HashMap<String,Item>();
				imap1.put(keyItemsPanelLec,gc.getItems());
				
				gc.generaParcial(pantallasManager.obtenerComponentes(
				null, cdunieco, cdramo,
				cdtipsit, estado, rol,
				pantalla, seccionModelo, orden));
				
				imap1.put(keyFieldsModelo,gc.getFields());
				imap1.put(keyColumnsGrid,gc.getColumns());
				
				
				Date fechaInicioEndoso=endososManager.obtenerFechaEndosoFormaPago(cdunieco, cdramo, estado, nmpoliza);
				smap1.put("fechaInicioEndoso",renderFechas.format(fechaInicioEndoso));
				//////campos pantalla //////
				/////////////////////////////
			} catch(Exception ex) {
				logger.error("error al cargar la pantalla de endoso de nombre de cliente",ex);
				error=ex.getMessage();
			}
		}
		
		logger.debug(new StringBuilder("\n")
		.append("\n######                     ######")
		.append("\n###### endosoNombreCliente ######")
		.append("\n#################################")
		.append("\n#################################").toString());
		
		return resp.isSuccess() ? SUCCESS : ERROR;
	}
	/*//////////////////////*/
	//////endosoNombreCliente //////
	//////////////////////////
	
	/////////////////////////////////
	//////guardarEndosoNombreCliente //////
	/*
	smap1:
	CDRAMO: "2"
	CDTIPSIT: "SL"
	CDUNIECO: "1002"
	DSCOMENT: ""
	DSTIPSIT: "SALUD VITAL"
	ESTADO: "M"
	FEEMISIO: "20/01/2014"
	FEINIVAL: "20/01/2014"
	NMPOLIEX: "1002213000019000000"
	NMPOLIZA: "19"
	NMSUPLEM: "245667814480000000"
	NSUPLOGI: "0"
	NTRAMITE: "573"
	PRIMA_TOTAL: "52694.6"
	smap2:
	contratante: "11000"
	fecha_endoso: "31/01/2014"
	nmcuadro
	cdsucurs
	slist1:
	{NMSUPLEM=245667814480000000,
	NOMBRE="JIRO Y ASOCIADOS, AGENTE DE   "  "SEGUROS Y FIANZAS, S" .A. DE C.V.,
	NMPOLIZA=19,
	CDAGENTE=1170,
	STATUS=V,
	NMCUADRO=SV18,
	ESTADO=M,
	PORPARTI=100,
	CDUNIECO=1002,
	CDRAMO=2,
	CDTIPOAG=1,
	PORREDAU=0,
	CDSUCURS=null}
	*/
	/*/////////////////////////////*/
	public String guardarEndosoNombreCliente()
	{
		logger.debug(Utils.log(
				 "\n########################################"
				,"\n###### guardarEndosoNombreCliente ######"
				,"\n###### smap1  = " , smap1
				,"\n###### smap2  = " , smap2
				,"\n###### smap3  = " , smap3
				,"\n###### slist1 = " , slist1
				,"\n###### flujo  = " , flujo
				));
		
		this.session = ActionContext.getContext().getSession();
		
		try
		{
			//////variables //////
			String cdunieco            = smap1.get("CDUNIECO");
			String cdramo              = smap1.get("CDRAMO");
			String estado              = smap1.get("ESTADO");
			String nmpoliza            = smap1.get("NMPOLIZA");
			String sFecha              = smap2.get("fecha_endoso");
			Date   dFecha              = renderFechas.parse(sFecha);
			UserVO usuario             = (UserVO)session.get("USUARIO");
			String cdelemento          = usuario.getEmpresa().getElementoId();
			String cdusuari            = usuario.getUser();
			String cdsisrol            = usuario.getRolActivo().getClave();
			String proceso             = "END";
			String cdtipsup            = TipoEndoso.CAMBIO_NOMBRE_CLIENTE.getCdTipSup().toString();
			//String cdcontratante            = smap2.get("contratante");
			String tipoContratantePrincipal = "1";
			String sesionComision      = "0";
			String porcenParticip      = "100";
			//String nmcuadro            = smap2.get("nmcuadro");
			//String cdsucurs            = smap2.get("cdsucurs");
			String comentariosEndoso   = "";
			String cdtipsit            = smap1.get("CDTIPSIT");
			String ntramite            = smap1.get("NTRAMITE");
			
			String paso = null;
			
			try // Esto deberia ir en un manager
			{
			
				paso = "Iniciando endoso";
				logger.debug(paso);
				
				//PKG_ENDOSOS.P_ENDOSO_INICIA
				Map<String,String>resIniEnd = endososManager.iniciarEndoso(cdunieco, cdramo, estado, nmpoliza, sFecha, cdelemento, cdusuari, proceso, cdtipsup);
				
				String nmsuplem = resIniEnd.get("pv_nmsuplem_o");
				String nsuplogi = resIniEnd.get("pv_nsuplogi_o");
				
				paso = "Actualizando nombre";
				logger.debug(paso);
				
				//Actualizar Mpersona:
				
				this.endososManager.actualizaNombreCliente(smap3);
				
				////Se confirma el endoso si cumple la validacion de fechas: 
				RespuestaConfirmacionEndosoVO respConfirmacionEndoso = this.confirmarEndoso(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,nmsuplem
						,nsuplogi
						,cdtipsup
						,comentariosEndoso
						,dFecha
						,cdtipsit
						,flujo
						,cdusuari
						,cdsisrol
						);
				
				//Si el endoso fue confirmado:
				if(respConfirmacionEndoso.isConfirmado())
				{
					endosoConfirmado = true;
				
					paso = "Generando documentos";
					logger.debug(paso);
					
					//Regeneramos los documentos:
					String nmsolici=this.regeneraDocumentos(cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdtipsup, ntramite,cdusuari);
					
					/**
					 * PARA WS ENDOSO DE AUTOS
					 */
					int numEndRes = emisionAutosService.endosoCambioNombreClienteAutos(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
					
					if(numEndRes == 0){
						mensaje = "Error al generar el endoso, sigs. Consulte a Soporte.";
						error = "Error al generar el endoso, sigs. Consulte a Soporte.";
						logger.error("Error al ejecutar sp de endoso sigs");
						
						boolean endosoRevertido = endososManager.revierteEndosoFallido(cdunieco, cdramo, estado, nmpoliza, nsuplogi, nmsuplem, 88888, "Error en endoso B tipo: "+TipoEndoso.CAMBIO_NOMBRE_CLIENTE.toString(), true);
						
						if(endosoRevertido){
							
							Map<String,String> paramRevNom = new HashMap<String, String>();
							paramRevNom.put("pv_cdperson_i" , smap3.get("cdperson"));
							paramRevNom.put("pv_cdrfc_i" ,    smap3.get("rfc"));
							paramRevNom.put("pv_dsnombre_i" , smap3.get("nombre"));
							paramRevNom.put("pv_dsnombre1_i" , smap3.get("snombre"));
							paramRevNom.put("pv_dsapellido_i" , smap3.get("appat"));
							paramRevNom.put("pv_dsapellido1_i"    , smap3.get("apmat"));
							endososManager.revierteNombrePersona(paramRevNom);
							
							logger.error("Endoso revertido exitosamente.");
							error+=" Favor de volver a intentar.";
						}else{
							logger.error("Error al revertir el endoso");
							error+=" No se ha revertido el endoso.";
						}
						
						success = false;
						return SUCCESS;
					}else{
						ejecutaCaratulaEndosoBsigs(cdunieco,cdramo,estado,nmpoliza,nmsuplem, ntramite, cdtipsup, Integer.toString(numEndRes));
					}
					
					mensaje="Se ha guardado el endoso "+nsuplogi;
				
				} else {
					mensaje="El endoso "+nsuplogi
					+" se guard\u00f3 en mesa de control para autorizaci\u00f3n "
					+ "con n\u00famero de tr\u00e1mite " + respConfirmacionEndoso.getNumeroTramite();
				}
				if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
                }
				success=true;
			}
			catch(Exception ex)
			{
				Utils.generaExcepcion(ex, paso);
			}
			
		}
		catch(Exception ex)
		{
			error = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### mensaje = " , mensaje
				,"\n###### error   = " , error
				,"\n###### guardarEndosoNombreCliente ######"
				,"\n########################################"
				));
		
		return SUCCESS;
	}
	/*/////////////////////////////*/
	//////guardarEndosoNombreCliente //////
	/////////////////////////////////
	
	
	//////////////////////////
	//////endosoRFCCliente //////
	/*
	smap1:
	CDRAMO      : "2"
	CDTIPSIT    : "SL"
	CDUNIECO    : "1002"
	DSCOMENT    : ""
	DSTIPSIT    : "SALUD VITAL"
	ESTADO      : "M"
	FEEMISIO    : "30/01/2014"
	FEINIVAL    : "15/01/2014"
	NMPOLIEX    : "1002213000064000000"
	NMPOLIZA    : "64"
	NMSUPLEM    : "245667313410000000"
	NSUPLOGI    : "4"
	NTRAMITE    : null
	PRIMA_TOTAL : "17339.97"
	*/
	/*//////////////////////*/
	public String endosoRfcCliente() {
	
		logger.debug(new StringBuilder("\n")
		.append("\n###############################")
		.append("\n###############################")
		.append("\n###### endosoRFCCliente  ######")
		.append("\n######                   ######").toString());
		logger.debug(new StringBuilder("smap1: ").append(smap1).toString());
		
		this.session=ActionContext.getContext().getSession();
		
		///////////////////////
		//////variables //////
		String cdunieco           = smap1.get("CDUNIECO");
		String cdramo             = smap1.get("CDRAMO");
		String cdtipsit           = smap1.get("CDTIPSIT");
		String estado             = smap1.get("ESTADO");
		String nmpoliza           = smap1.get("NMPOLIZA");
		String nmsuplem           = smap1.get("NMSUPLEM");
		String rol                = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
		String orden              = null;
		String pantalla           = "ENDOSO_CONTRATANTE";
		String seccionLectura     = "PANEL_LECTURA";
		String seccionModelo      = "MODELO";
		String keyItemsPanelLec   = "itemsPanelLectura";
		String keyFieldsModelo    = "fieldsModelo";
		String keyColumnsGrid     = "columnsGrid";
		String cdtipsup           = TipoEndoso.CAMBIO_RFC_CLIENTE.getCdTipSup().toString();
		//////variables //////
		///////////////////////
		
		//Valida si hay un endoso anterior pendiente:
		RespuestaVO resp = endososManager.validaEndosoAnterior(cdunieco, cdramo, estado, nmpoliza, cdtipsup);
		error = resp.getMensaje();
		
		if(resp.isSuccess()) {
			try {
				/////////////////////////////
				//////campos pantalla //////
				GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
				gc.generaParcial(pantallasManager.obtenerComponentes(
				null, cdunieco, cdramo,
				cdtipsit, estado, rol,
				pantalla, seccionLectura, orden));
				
				imap1=new HashMap<String,Item>();
				imap1.put(keyItemsPanelLec,gc.getItems());
				
				gc.generaParcial(pantallasManager.obtenerComponentes(
				null, cdunieco, cdramo,
				cdtipsit, estado, rol,
				pantalla, seccionModelo, orden));
				
				imap1.put(keyFieldsModelo,gc.getFields());
				imap1.put(keyColumnsGrid,gc.getColumns());
				
				
				Date fechaInicioEndoso=endososManager.obtenerFechaEndosoFormaPago(cdunieco, cdramo, estado, nmpoliza);
				smap1.put("fechaInicioEndoso",renderFechas.format(fechaInicioEndoso));
				//////campos pantalla //////
				/////////////////////////////
			} catch(Exception ex) {
				logger.error("error al cargar la pantalla de endoso de rfc cliente",ex);
				error=ex.getMessage();
			}
		}
		
		logger.debug(new StringBuilder("\n")
		.append("\n######                   ######")
		.append("\n###### endosoRFCCliente  ######")
		.append("\n###############################")
		.append("\n###############################").toString());
		
		return resp.isSuccess() ? SUCCESS : ERROR;
	}
	/*//////////////////////*/
	//////endosoRFCCliente //////
	//////////////////////////
	
	/////////////////////////////////
	//////guardarEndosoRFCCliente //////
	/*
	smap1:
	CDRAMO: "2"
	CDTIPSIT: "SL"
	CDUNIECO: "1002"
	DSCOMENT: ""
	DSTIPSIT: "SALUD VITAL"
	ESTADO: "M"
	FEEMISIO: "20/01/2014"
	FEINIVAL: "20/01/2014"
	NMPOLIEX: "1002213000019000000"
	NMPOLIZA: "19"
	NMSUPLEM: "245667814480000000"
	NSUPLOGI: "0"
	NTRAMITE: "573"
	PRIMA_TOTAL: "52694.6"
	smap2:
	contratante: "11000"
	fecha_endoso: "31/01/2014"
	nmcuadro
	cdsucurs
	slist1:
	{NMSUPLEM=245667814480000000,
	NOMBRE="JIRO Y ASOCIADOS, AGENTE DE   "  "SEGUROS Y FIANZAS, S" .A. DE C.V.,
	NMPOLIZA=19,
	CDAGENTE=1170,
	STATUS=V,
	NMCUADRO=SV18,
	ESTADO=M,
	PORPARTI=100,
	CDUNIECO=1002,
	CDRAMO=2,
	CDTIPOAG=1,
	PORREDAU=0,
	CDSUCURS=null}
	*/
	/*/////////////////////////////*/
	public String guardarEndosoRfcCliente()
	{
		logger.debug(Utils.log(
				 "\n#####################################"
				,"\n###### guardarEndosoRfcCliente ######"
				,"\n###### smap1  = " , smap1
				,"\n###### smap2  = " , smap2
				,"\n###### smap3  = " , smap3
				,"\n###### slist1 = " , slist1
				,"\n###### flujo  = " , flujo
				));
		
		this.session = ActionContext.getContext().getSession();
		
		try
		{
			//////variables //////
			String cdunieco            = smap1.get("CDUNIECO");
			String cdramo              = smap1.get("CDRAMO");
			String estado              = smap1.get("ESTADO");
			String nmpoliza            = smap1.get("NMPOLIZA");
			String sFecha              = smap2.get("fecha_endoso");
			Date   dFecha              = renderFechas.parse(sFecha);
			UserVO usuario             = (UserVO)session.get("USUARIO");
			String cdelemento          = usuario.getEmpresa().getElementoId();
			String cdusuari            = usuario.getUser();
			String cdsisrol            = usuario.getRolActivo().getClave();
			String proceso             = "END";
			String cdtipsup            = TipoEndoso.CAMBIO_RFC_CLIENTE.getCdTipSup().toString();
			//String cdcontratante            = smap2.get("contratante");
			String tipoContratantePrincipal = "1";
			String sesionComision      = "0";
			String porcenParticip      = "100";
			//String nmcuadro            = smap2.get("nmcuadro");
			//String cdsucurs            = smap2.get("cdsucurs");
			String comentariosEndoso   = "";
			String cdtipsit            = smap1.get("CDTIPSIT");
			String ntramite            = smap1.get("NTRAMITE");
			
			String paso = null;
			
			try // Esto deberia ir en un manager
			{
				paso = "Iniciando endoso";
				logger.debug(paso);
				
				//PKG_ENDOSOS.P_ENDOSO_INICIA
				Map<String,String>resIniEnd=endososManager.iniciarEndoso(cdunieco, cdramo, estado, nmpoliza, sFecha, cdelemento, cdusuari, proceso, cdtipsup);
				
				String nmsuplem = resIniEnd.get("pv_nmsuplem_o");
				String nsuplogi = resIniEnd.get("pv_nsuplogi_o");
				
				paso = "Actualizando RFC";
				logger.debug(paso);
				
				//Actualizar Mpersona:
				
				this.endososManager.actualizaRfcCliente(smap3);
				
				////Se confirma el endoso si cumple la validacion de fechas: 
				RespuestaConfirmacionEndosoVO respConfirmacionEndoso = this.confirmarEndoso(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,nmsuplem
						,nsuplogi
						,cdtipsup
						,comentariosEndoso
						,dFecha
						,cdtipsit
						,flujo
						,cdusuari
						,cdsisrol
						);
				
				//Si el endoso fue confirmado:
				if(respConfirmacionEndoso.isConfirmado()) {
					endosoConfirmado = true;
				
					paso = "Generando documentos";
					logger.debug(paso);
					
					//Regeneramos los documentos:
					String nmsolici=this.regeneraDocumentos(cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdtipsup, ntramite,cdusuari);
					
					/**
					 * PARA WS ENDOSO DE AUTOS
					 */
					int numEndRes = emisionAutosService.endosoCambioRfcClienteAutos(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
					
					if(numEndRes == 0){
						mensaje = "Error al generar el endoso, sigs. Consulte a Soporte.";
						error = "Error al generar el endoso, sigs. Consulte a Soporte.";
						logger.error("Error al ejecutar sp de endoso sigs");
						
						boolean endosoRevertido = endososManager.revierteEndosoFallido(cdunieco, cdramo, estado, nmpoliza, nsuplogi, nmsuplem, 88888, "Error en endoso B tipo: "+TipoEndoso.CAMBIO_RFC_CLIENTE.toString(), true);
						
						if(endosoRevertido){
							
							Map<String,String> paramRevNom = new HashMap<String, String>();
							paramRevNom.put("pv_cdperson_i" , smap3.get("cdperson"));
							paramRevNom.put("pv_cdrfc_i" ,    smap3.get("rfc"));
							paramRevNom.put("pv_dsnombre_i" , smap3.get("nombre"));
							paramRevNom.put("pv_dsnombre1_i" , smap3.get("snombre"));
							paramRevNom.put("pv_dsapellido_i" , smap3.get("appat"));
							paramRevNom.put("pv_dsapellido1_i"    , smap3.get("apmat"));
							endososManager.revierteNombrePersona(paramRevNom);
							
							logger.error("Endoso revertido exitosamente.");
							error+=" Favor de volver a intentar.";
						}else{
							logger.error("Error al revertir el endoso");
							error+=" No se ha revertido el endoso.";
						}
						
						success = false;
						return SUCCESS;
					}else{
						ejecutaCaratulaEndosoBsigs(cdunieco,cdramo,estado,nmpoliza,nmsuplem, ntramite, cdtipsup, Integer.toString(numEndRes));
					}
					
					mensaje="Se ha guardado el endoso "+nsuplogi;
				
				} else {
					mensaje="El endoso "+nsuplogi
					+" se guard\u00f3 en mesa de control para autorizaci\u00f3n "
					+ "con n\u00famero de tr\u00e1mite " + respConfirmacionEndoso.getNumeroTramite();
				}
				if (respConfirmacionEndoso.getRespuestaTurnado() != null) {
                    mensaje = Utils.join(mensaje, ". ", respConfirmacionEndoso.getRespuestaTurnado().getMessage());
                }
				success=true;
			}
			catch(Exception ex)
			{
				Utils.generaExcepcion(ex, paso);
			}
		}
		catch(Exception ex)
		{
			error = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### mensaje = " , mensaje
				,"\n###### error   = " , error
				,"\n###### guardarEndosoRfcCliente  ######"
				,"\n######################################"
				));
		
		return SUCCESS;
	}
	/*/////////////////////////////*/
	//////guardarEndosoNombreCliente //////
	/////////////////////////////////
		
	public String pantallaRecibosSubsecuentes()
	{
		logger.debug(""
				+ "\n#########################################"
				+ "\n###### pantallaRecibosSubsecuentes ######"
				);
		
		imap1              = new HashMap<String,Item>();
		GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
		String pantalla    = "RECIBOS_SUBSECUENTES";
		String seccion     = "FILTRO";
		
		try
		{
			List<ComponenteVO> listaDeComponentes = pantallasManager.obtenerComponentes(null, null, null, null, null, null, pantalla, seccion, null);
			gc.generaComponentes(listaDeComponentes, true, false, true, false, false, false);
			imap1.put("itemsDeFormulario",gc.getItems());
		}
		catch(Exception ex)
		{
			logger.error("error al cargar la pantalla de recibos subsecuentes",ex);
		}
		
		logger.debug(""
				+ "\n###### pantallaRecibosSubsecuentes ######"
				+ "\n#########################################"
				);
		return SUCCESS;
	}
	
	public String habilitarRecibosSubsecuentes()
	{
		logger.debug(""
				+ "\n##########################################"
				+ "\n###### habilitarRecibosSubsecuentes ######"
				);
		logger.debug("smap1: "+smap1);
		try
		{
			String cdunieco      = smap1.get("cdunieco");
			String cdramo        = smap1.get("cdramo");
			String estado        = smap1.get("estado");
			String nmpoliza      = smap1.get("nmpoliza");
			String fechaDeInicio = smap1.get("fechaInicio");
			String fechaDeFin    = smap1.get("fechaFin");
			Date   fechaDeInicioDate = renderFechas.parse(fechaDeInicio);
			Date   fechaDeFinDate    = renderFechas.parse(fechaDeFin);
			slist1=endososManager.habilitaRecibosSubsecuentes(fechaDeInicioDate, fechaDeFinDate, cdunieco, cdramo, estado, nmpoliza);
			success=true;
			mensaje="Recibos habilitados correctamente";
		}
		catch(Exception ex)
		{
			logger.error("error al habilitar recibos subsecuentes",ex);
			mensaje=ex.getMessage();
			success=false;
		}
		logger.debug(""
				+ "\n###### habilitarRecibosSubsecuentes ######"
				+ "\n##########################################"
				);
		return SUCCESS;
	}
	
	public String obtenerComponenteSituacionCobertura()
	{
		logger.debug(
				new StringBuilder()
				.append("\n#################################################")
				.append("\n###### obtenerComponenteSituacionCobertura ######")
				.append("\n###### smap1=").append(smap1)
				.toString()
				);
		
		exito   = true;
		success = true;
		
		String cdramo   = null;
		String cdtipsit = null;
		String cdtipsup = null;
		String cdgarant = null;
		
		//datos completos
		try
		{
			if(smap1==null)
			{
				throw new ApplicationException("No se han recibido datos");
			}
			cdramo   = smap1.get("cdramo");
			cdtipsit = smap1.get("cdtipsit");
			cdtipsup = smap1.get("cdtipsup");
			cdgarant = smap1.get("cdgarant");
			
			if(StringUtils.isBlank(cdramo))
			{
				throw new ApplicationException("No se recibio el producto");
			}
			if(StringUtils.isBlank(cdtipsit))
			{
				throw new ApplicationException("No se recibio la modalidad");
			}
			if(StringUtils.isBlank(cdtipsup))
			{
				throw new ApplicationException("No se recibio el tipo de endoso");
			}
			if(StringUtils.isBlank(cdgarant))
			{
				throw new ApplicationException("No se recibio la cobertura");
			}
		}
		catch(ApplicationException ax)
		{
			long timestamp  = System.currentTimeMillis();
			exito           = false;
			respuesta       = new StringBuilder(ax.getMessage()).append(" #").append(timestamp).toString();
			respuestaOculta = ax.getMessage();
			logger.error(respuesta,ax);
		}
		
		//proceso
		if(exito)
		{
			ManagerRespuestaImapSmapVO resp=endososManager.obtenerComponentesSituacionCobertura(cdramo,cdtipsit,cdtipsup,cdgarant);
			exito           = resp.isExito();
			respuesta       = resp.getRespuesta();
			respuestaOculta = resp.getRespuestaOculta();
			if(exito)
			{
				smap1.putAll(resp.getSmap());
				if(smap1.get("CONITEM").equals("true"))
				{
					smap1.put("item",resp.getImap().get("item").toString());
				}
			}
		}
		
		logger.debug(
				new StringBuilder()
				.append("\n###### obtenerComponenteSituacionCobertura ######")
				.append("\n#################################################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String endosoAtributosSituacionGeneral()
	{
		this.session=ActionContext.getContext().getSession();
		logger.debug(
				new StringBuilder()
				.append("\n#############################################")
				.append("\n###### endosoAtributosSituacionGeneral ######")
				.append("\n###### smap1=").append(smap1)
				.toString()
				);
		
		String result = null;
		
		try
		{
			transformaEntrada(smap1, slist1, false);
			
			exito   = true;
			success = true;
			
			String cdunieco = null;
			String cdramo   = null;
			String cdtipsit = null;
			String estado   = null;
			String nmpoliza = null;
			String nmsuplem = null;
			String cdusuari = null;
			String cdtipsup = null;
			
			//datos completos
			try
			{
				if(smap1==null)
				{
					throw new ApplicationException("No se recibieron datos");
				}
				cdunieco = smap1.get("CDUNIECO");
				cdramo   = smap1.get("CDRAMO");
				cdtipsit = smap1.get("CDTIPSIT");
				estado   = smap1.get("ESTADO");
				nmpoliza = smap1.get("NMPOLIZA");
				nmsuplem = smap1.get("NMSUPLEM");
				cdtipsup = smap1.get("cdtipsup");
				
				if(StringUtils.isBlank(cdunieco))
				{
					throw new ApplicationException("No se recibio la sucursal");
				}
				if(StringUtils.isBlank(cdramo))
				{
					throw new ApplicationException("No se recibio el producto");
				}
				if(StringUtils.isBlank(cdtipsit))
				{
					throw new ApplicationException("No se recibio la modalidad");
				}
				if(StringUtils.isBlank(estado))
				{
					throw new ApplicationException("No se recibio el estado");
				}
				if(StringUtils.isBlank(nmpoliza))
				{
					throw new ApplicationException("No se recibio el numero de poliza");
				}
				if(StringUtils.isBlank(nmsuplem))
				{
					throw new ApplicationException("No se recibio el suplemento");
				}
				if(StringUtils.isBlank(cdtipsup))
				{
					throw new ApplicationException("No se recibio el tipo de endoso");
				}
				
				if(session==null)
				{
					throw new ApplicationException("No hay sesion");
				}
				if(session.get("USUARIO")==null)
				{
					throw new ApplicationException("No hay usuario en la sesion");
				}
				
				UserVO usuario = (UserVO)session.get("USUARIO");
				cdusuari       = usuario.getUser();
			}
			catch(ApplicationException ax)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = new StringBuilder(ax.getMessage()).append(" #").append(timestamp).toString();
				respuestaOculta = ax.getMessage();
				logger.error(respuesta,ax);
			}
			
			//construir
			if(exito)
			{
				ManagerRespuestaImapSmapVO resp = endososManager.endosoAtributosSituacionGeneral(
						cdunieco
						,cdramo
						,cdtipsit
						,estado
						,nmpoliza
						,cdusuari
						,cdtipsup);
				exito           = resp.isExito();
				respuesta       = resp.getRespuesta();
				respuestaOculta = resp.getRespuestaOculta();
				if(resp.isExito())
				{
					imap1 = resp.getImap();
					smap1.putAll(resp.getSmap());
				}
			}
			
			result = SUCCESS;
			if(!exito)
			{
				result = ERROR;
			}
		}
		catch(Exception ex)
		{
			logger.error("Error al constuir pantalla de endoso de atributos de situacion general",ex);
			exito     = false;
			result    = ERROR;
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(
				new StringBuilder()
				.append("\n###### result=").append(result)
				.append("\n###### endosoAtributosSituacionGeneral ######")
				.append("\n#############################################")
				.toString()
				);
		return result;
	}
	
	public String cargarTvalositTitular()
	{
		logger.debug(
				new StringBuilder()
				.append("\n###################################")
				.append("\n###### cargarTvalositTitular ######")
				.append("\n###### smap1=").append(smap1)
				.toString()
				);
		
		exito   = true;
		success = true;
		
		String cdunieco = null;
		String cdramo   = null;
		String estado   = null;
		String nmpoliza = null;
		String nmsuplem = null;
		
		//datos completos
		try
		{
			if(smap1==null)
			{
				throw new ApplicationException("No se recibieron datos");
			}
			cdunieco = smap1.get("cdunieco");
			cdramo   = smap1.get("cdramo");
			estado   = smap1.get("estado");
			nmpoliza = smap1.get("nmpoliza");
			nmsuplem = smap1.get("nmsuplem");
			
			if(StringUtils.isBlank(cdunieco))
			{
				throw new ApplicationException("No se recibio la sucursal");
			}
			if(StringUtils.isBlank(cdramo))
			{
				throw new ApplicationException("No se recibio el producto");
			}
			if(StringUtils.isBlank(estado))
			{
				throw new ApplicationException("No se recibio el estado");
			}
			if(StringUtils.isBlank(nmpoliza))
			{
				throw new ApplicationException("No se recibio el numero de poliza");
			}
			if(StringUtils.isBlank(nmsuplem))
			{
				throw new ApplicationException("No se recibio el suplemento");
			}
		}
		catch(ApplicationException ax)
		{
			long timestamp  = System.currentTimeMillis();
			exito           = false;
			respuesta       = new StringBuilder(ax.getMessage()).append(" #").append(timestamp).toString();
			respuestaOculta = ax.getMessage();
			logger.error(respuesta,ax);
		}
		
		//proceso
		if(exito)
		{
			ManagerRespuestaSmapVO resp = endososManager.cargarTvalositTitular(cdunieco,cdramo,estado,nmpoliza,nmsuplem);
			exito           = resp.isExito();
			respuesta       = resp.getRespuesta();
			respuestaOculta = resp.getRespuestaOculta();
			if(exito)
			{
				smap1.putAll(resp.getSmap());
			}
		}
		
		logger.debug(
				new StringBuilder()
				.append("\n###### cargarTvalositTitular ######")
				.append("\n###################################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String guardarEndosoAtributosSituacionGeneral()
	{
		this.session=ActionContext.getContext().getSession();
		
		logger.debug(Utils.log(
				 "\n####################################################"
				,"\n###### guardarEndosoAtributosSituacionGeneral ######"
				,"\n###### smap1 = " , smap1
				,"\n###### smap2 = " , smap2
				,"\n###### smap3 = " , smap3
				,"\n###### flujo = " , flujo
				));
		
		//datos completos
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(smap1 , "No se recibieron datos de poliza");
			
			Utils.validate(smap2 , "No se recibieron datos modificables");
			
			Utils.validate(smap3 , "No se recibieron datos del endoso");
			
			String cdunieco   = smap1.get("CDUNIECO")
					,cdramo   = smap1.get("CDRAMO")
					,cdtipsit = smap1.get("CDTIPSIT")
					,estado   = smap1.get("ESTADO")
					,nmpoliza = smap1.get("NMPOLIZA")
					,nmsuplem = smap1.get("NMSUPLEM")
					,cdtipsup = smap1.get("cdtipsup")
					,ntramite = smap1.get("NTRAMITE");
			
			Utils.validate(
					cdunieco  , "No se recibio la sucursal"
					,cdramo   , "No se recibio el producto"
					,cdtipsit , "No se recibio la modalidad"
					,estado   , "No se recibio el estado"
					,nmpoliza , "No se recibio el numero de poliza"
					,nmsuplem , "No se recibio el suplemento"
					,cdtipsup , "No se recibio el tipo de endoso"
					);
			
			String feefecto = smap3.get("feefecto");
			
			Utils.validate(feefecto , "No se recibio la fecha de efecto del endoso");
			
			//proceso
			ManagerRespuestaVoidVO resp = endososManager.guardarEndosoAtributosSituacionGeneral(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplem
					,cdtipsit
					,cdtipsup
					,ntramite
					,feefecto
					,smap2
					,(UserVO) session.get("USUARIO")
					,rutaDocumentosPoliza
					,rutaServidorReports
					,passServidorReports
					,flujo
					);
			
			exito     = resp.isExito();
			respuesta = resp.getRespuesta();
			
			if(!exito)
			{
				throw new ApplicationException(respuesta);
			}
			
			success = exito;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success   = " , success
				,"\n###### exito     = " , exito
				,"\n###### respuesta = " , respuesta
				,"\n###### guardarEndosoAtributosSituacionGeneral ######"
				,"\n####################################################"
				));
		
		return SUCCESS;
	}
	
	public String guardarEndosoBeneficiarios()
	{
		logger.debug(Utils.log(
				 "\n########################################"
				,"\n###### guardarEndosoBeneficiarios ######"
				,"\n###### smap1  = " , smap1
				,"\n###### slist1 = " , slist1
				,"\n###### flujo  = " , flujo
				));
		
		try
		{
		    logger.debug("Validando datos de entrada");
			Utils.validate(smap1, "No se recibieron datos");
			String cdunieco = smap1.get("cdunieco");
			String cdramo   = smap1.get("cdramo");
			String estado   = smap1.get("estado");
			String nmpoliza = smap1.get("nmpoliza");
			String nmsituac = smap1.get("nmsituac");
			String cdtipsup = smap1.get("cdtipsup");
			String ntramite = smap1.get("ntramite");
			String feefecto = smap1.get("feefecto");
			
			Utils.validate(cdunieco , "No se recibio la sucursal");
			Utils.validate(cdramo   , "No se recibio el producto");
			Utils.validate(estado   , "No se recibio el estado de la poliza");
			Utils.validate(nmpoliza , "No se recibio el numero de poliza");
			Utils.validate(nmsituac , "No se recibio el numero de situacion");
			Utils.validate(cdtipsup , "No se recibio el tipo de suplemento");
			Utils.validate(ntramite , "No se recibio el numero de tramite");
			
			UserVO usuarioSesion = Utils.validateSession(session);
			String cdelemen = ((UserVO)session.get("USUARIO")).getCdElemento();
			String cdusuari = ((UserVO)session.get("USUARIO")).getUser();
			String cdsisrol = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
			
			ManagerRespuestaVoidVO resp=endososManager.guardarEndosoBeneficiarios(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsituac
					,slist1
					,cdelemen
					,cdusuari
					,cdtipsup
					,ntramite
					,cdsisrol
					,usuarioSesion
					,flujo
					,feefecto
					);
			exito           = resp.isExito();
			respuesta       = resp.getRespuesta();
			respuestaOculta = resp.getRespuestaOculta();
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
            success=false;
            error=ex.getMessage();
		}
		
		logger.debug(Utils.log(
				 "\n###### success   = " , success
				,"\n###### exito     = " , exito
				,"\n###### respuesta = " , respuesta
				,"\n###### error     = " , error
				,"\n###### guardarEndosoBeneficiarios ######"
				,"\n########################################"
				));
		return SUCCESS;
	}
	
	
	
	private boolean ejecutaCaratulaEndosoBsigs(String cdunieco,String cdramo,String estado,String nmpoliza,String nmsuplem, String ntramite, String cdtipsup, String numEnd){
		
		/**
		 * Para Caratula Endoso B
		 */
		
		try {
			PolizaAseguradoVO datosPol = new PolizaAseguradoVO();
	
			datosPol.setCdunieco(cdunieco);
			datosPol.setCdramo(cdramo);
			datosPol.setEstado(estado);
			datosPol.setNmpoliza(nmpoliza);
			
			List<PolizaDTO> listaPolizas = consultasPolizaManager.obtieneDatosPoliza(datosPol);
			PolizaDTO polRes = listaPolizas.get(0);
	
	
			String parametros = null;
			String urlCaratula =  this.caratulaImpresionAutosEndosobUrl;
	
			parametros = "?"+polRes.getCduniext()+","+polRes.getCdramoext()+","+polRes.getNmpoliex()+",,0,"+ numEnd+",0";
			logger.debug("URL Generada para Caratula: "+ urlCaratula + parametros);
	
			String dstipsup = consultasManager.recuperarDstipsupPorCdtipsup(cdtipsup);
            
            StringBuilder mensajeEmail = new StringBuilder("<span style=\"font-family: Verdana, Geneva, sans-serif;\">").append(
                    "<br>Estimado(a) cliente,<br/><br/>").append(
                    "Anexamos a este e-mail la documentaci\u00f3n del endoso de '").append(dstipsup).append("' realizado con GENERAL DE SEGUROS.<br/>").append(
                    "Para visualizar los documentos favor de dar click en el link correspondiente.<br/>");
			
			mensajeEmail.append("<br/><br/><a style=\"font-weight: bold\" href=\"").append(urlCaratula).append(parametros).append("\">Car\u00e1tula de p\u00f3liza</a>");
			
			documentosManager.guardarDocumento(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplem
					,new Date()
					,urlCaratula + parametros
					,"Car\u00e1tula de P\u00f3liza"
					,nmpoliza
					,ntramite
					,cdtipsup
					,Constantes.SI
					,null
					,TipoTramite.POLIZA_NUEVA.getCdtiptra()
					,"0"
					,Documento.EXTERNO_CARATULA_B
					,null
					,null, false
					);
			
			mensajeEmail.append(emisionManager.generarLigasDocumentosEmisionLocalesIce(ntramite));
            
            mensajeEmail.append("<br/><br/><br/>Agradecemos su preferencia.<br/>").append(
                    "General de Seguros<br/>").append(
                    "</span>");
            
            try {
                String ntramiteEndoso =  consultasManager.recuperarTramitePorNmsuplem(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
                
                flujoMesaControlManager.guardarMensajeCorreoEmision(
                        ntramiteEndoso,
                        Utils.cambiaAcentosUnicodePorGuionesBajos(mensajeEmail.toString())
                );
                
                logger.debug("Enviando correos configurados");
                flujoMesaControlManager.mandarCorreosStatusTramite(ntramiteEndoso, RolSistema.SUSCRIPTOR_AUTO.getCdsisrol(), false);
            } catch (Exception ex) {
                logger.debug("Error al enviar correos de estatus al turnar", ex);
            }
			
		} catch (Exception e) {
			logger.error("Error al generar la Caratula tipo B para el tipo de endoso: " + cdtipsup,e);
			return false;
		}
		
		return true;
	}
	
	
/**
 * Para Guardar URls de Caratula Recibos y documentos de Autos Externas
 * @param cdunieco
 * @param cdramo
 * @param estado
 * @param nmpoliza
 * @param nmsuplem
 * @param ntramite
 * @param cdtipsup
 * @param tipoGrupoInciso
 * @param emisionWS
 * @param incisosAfectados
 * @return
 */
	private boolean ejecutaCaratulaEndosoTarifaSigs(String cdunieco,String cdramo,String estado,String nmpoliza,String nmsuplem, String ntramite, String cdtipsup, String tipoGrupoInciso, EmisionAutosVO emisionWS, Map<String,String> incisosAfectados){
		
		boolean soloIncisosAfectados = (incisosAfectados != null && !incisosAfectados.isEmpty());
		boolean soloUnInciso         = (soloIncisosAfectados && incisosAfectados.size() == 1);
		
		logger.debug(">>>>>>>>>>>  Imprimiendo Caratulas para Autos  <<<<<<<<<<<<<<");
		
		if(soloIncisosAfectados){
			logger.debug(">>>>>>>>>>>  Incisos Afectados  <<<<<<<<<<<<<< :::" + incisosAfectados);
			logger.debug(">>>>>>>>>>>  Solo un Inciso? <<<<<<<<<<<<<< :::" + soloUnInciso);
		}
		
		try {
			
			String rutaCarpeta = Utils.join(this.rutaDocumentosPoliza,"/",ntramite);
			
			List<Map<String,String>> listaEndosos = emisionAutosService.obtieneEndososImprimir(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
			
			if(listaEndosos == null || listaEndosos.isEmpty()){
				logger.error("Error al ejecutar caratula endoso con tarifa, Sin Datos de consulta.");
				return false;
			}
			
			String dstipsup = consultasManager.recuperarDstipsupPorCdtipsup(cdtipsup);
			
			StringBuilder mensajeEmail = new StringBuilder("<span style=\"font-family: Verdana, Geneva, sans-serif;\">").append(
                    "<br>Estimado(a) cliente,<br/><br/>").append(
                    "Anexamos a este e-mail la documentaci\u00f3n del endoso de '").append(dstipsup).append("' realizado con GENERAL DE SEGUROS.<br/>").append(
                    "Para visualizar los documentos favor de dar click en el link correspondiente.<br/>");
			
			for(Map<String,String> endosoIt : listaEndosos){
				if(StringUtils.isNotBlank(endosoIt.get("IMPRIMIR")) && Constantes.SI.equalsIgnoreCase(endosoIt.get("IMPRIMIR"))){

					
					String parametros = null;
					
					String urlCaratula = null;
					if(Ramo.AUTOS_FRONTERIZOS.getCdramo().equalsIgnoreCase(cdramo) 
				    		|| Ramo.AUTOS_RESIDENTES.getCdramo().equalsIgnoreCase(cdramo)
				    	){
						urlCaratula = this.caratulaImpresionAutosUrl;
					}else if(Ramo.SERVICIO_PUBLICO.getCdramo().equalsIgnoreCase(cdramo)){
						urlCaratula = this.caratulaImpresionAutosServiciopublicoUrl;
					}
					
					if(StringUtils.isNotBlank(tipoGrupoInciso)  && ("F".equalsIgnoreCase(tipoGrupoInciso) || "P".equalsIgnoreCase(tipoGrupoInciso))){
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
					
					/**
					 * Para Caratula
					 */
					parametros = "?"+emisionWS.getSucursal()+","+emisionWS.getSubramo()+","+emisionWS.getNmpoliex()+","+endosoIt.get("TIPOEND")+","+ (StringUtils.isBlank(endosoIt.get("NUMEND"))?"0":endosoIt.get("NUMEND"));
					logger.debug("URL Generada para Caratula: "+ urlCaratula + parametros);
					mensajeEmail.append("<br/><br/><a style=\"font-weight: bold\" href=\"").append(urlCaratula).append(parametros).append("\">Car\u00e1tula de p\u00f3liza</a>");
					
					documentosManager.guardarDocumento(
							cdunieco
							,cdramo
							,estado
							,nmpoliza
							,nmsuplem
							,new Date()
							,urlCaratula + parametros
							,"Car\u00e1tula de P\u00f3liza ("+endosoIt.get("TIPOEND")+" - "+endosoIt.get("NUMEND")+")"
							,nmpoliza
							,ntramite
							,cdtipsup
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
					
					List<Map<String,String>> recibos = consultasPolizaManager.obtieneRecibosPolizaAuto(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
					
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
							
							//parametros = "?9999,0,"+emisionWS.getSucursal()+","+emisionWS.getSubramo()+","+emisionWS.getNmpoliex()+",0,"+(StringUtils.isBlank(endosoIt.get("NUMEND"))?"0":endosoIt.get("NUMEND"))+","+endosoIt.get("TIPOEND")+","+reciboIt.get("NUMREC"); // PARAMS RECIBO ANTERIORES
                            parametros = "?"+emisionWS.getSucursal()+","+emisionWS.getSubramo()+","+emisionWS.getNmpoliex()+","+endosoIt.get("TIPOEND")+","+(StringUtils.isBlank(endosoIt.get("NUMEND"))?"0":endosoIt.get("NUMEND"))+","+reciboIt.get("NUMREC");
							
							logger.debug("URL Generada para Recibo "+reciboIt.get("NUMREC")+": "+ urlRecibo + parametros);
							
							if(Constantes.SI.equalsIgnoreCase(visible)){
                                mensajeEmail.append("<br/><br/><a style=\"font-weight: bold\" href=\"").append(urlRecibo).append(parametros).append("\">Recibo ").append(reciboIt.get("NUMREC")).append(" provisional de primas</a>");
                            }
							
							documentosManager.guardarDocumento(
									cdunieco
									,cdramo
									,estado
									,nmpoliza
									,nmsuplem
									,new Date()
									,urlRecibo + parametros
									,"Recibo "+reciboIt.get("NUMREC")+" ("+endosoIt.get("TIPOEND")+" - "+endosoIt.get("NUMEND")+")"
									,nmpoliza
									,ntramite
									,cdtipsup
									,visible
									,null
									,((TipoEndoso.EMISION_POLIZA.getCdTipSup().intValue() == Integer.parseInt(cdtipsup)) || (TipoEndoso.RENOVACION.getCdTipSup().intValue() == Integer.parseInt(cdtipsup)))?TipoTramite.POLIZA_NUEVA.getCdtiptra() : TipoTramite.ENDOSO.getCdtiptra()
									,"0"
									,Documento.RECIBO
									,null
									,null, false
									);
						}
					}
					
					
					
					/**
					 * Para AP inciso 1
					 */
					
					if(StringUtils.isNotBlank(endosoIt.get("AP")) && Constantes.SI.equalsIgnoreCase(endosoIt.get("AP"))){
						parametros = "?"+emisionWS.getSucursal()+","+emisionWS.getSubramo()+","+emisionWS.getNmpoliex()+","+endosoIt.get("TIPOEND")+","+ (StringUtils.isBlank(endosoIt.get("NUMEND"))?"0":endosoIt.get("NUMEND"))+",0";
						logger.debug("URL Generada para AP Inciso 1: "+ urlAp + parametros);
						
						mensajeEmail.append("<br/><br/><a style=\"font-weight: bold\" href=\"").append(urlAp).append(parametros).append("\">Anexo cobertura de AP</a>");
						
						documentosManager.guardarDocumento(
								cdunieco
								,cdramo
								,estado
								,nmpoliza
								,nmsuplem
								,new Date()
								,urlAp + parametros
								,"AP"
								,nmpoliza
								,ntramite
								,cdtipsup
								,Constantes.SI
								,null
								,TipoTramite.POLIZA_NUEVA.getCdtiptra()
								,"0"
								,Documento.EXTERNO_AP
								,null
								,null, false
								);
					}
					
					/**
					 * Para CAIC inciso 1
					 */
					
					if(StringUtils.isNotBlank(endosoIt.get("CAIC")) && Constantes.SI.equalsIgnoreCase(endosoIt.get("CAIC"))){
						parametros = "?"+emisionWS.getSucursal()+","+emisionWS.getSubramo()+","+emisionWS.getNmpoliex()+","+endosoIt.get("TIPOEND")+","+ (StringUtils.isBlank(endosoIt.get("NUMEND"))?"0":endosoIt.get("NUMEND"))+",0";
						logger.debug("URL Generada para CAIC Inciso 1: "+ urlCaic + parametros);
						
						mensajeEmail.append("<br/><br/><a style=\"font-weight: bold\" href=\"").append(urlCaic).append(parametros).append("\">Anexo de cobertura RC USA</a>");
						
						documentosManager.guardarDocumento(
								cdunieco
								,cdramo
								,estado
								,nmpoliza
								,nmsuplem
								,new Date()
								,urlCaic + parametros
								,"CAIC"
								,nmpoliza
								,ntramite
								,cdtipsup
								,Constantes.SI
								,null
								,TipoTramite.POLIZA_NUEVA.getCdtiptra()
								,"0"
								,Documento.EXTERNO_CAIC
								,null
								,null, false
								);
					}

					/**
					 * Para AEUA inciso 1
					 */
					
					if(StringUtils.isNotBlank(endosoIt.get("AEUA")) && Constantes.SI.equalsIgnoreCase(endosoIt.get("AEUA"))){
						parametros = "?"+emisionWS.getSucursal()+","+emisionWS.getSubramo()+","+emisionWS.getNmpoliex()+","+endosoIt.get("TIPOEND")+","+ (StringUtils.isBlank(endosoIt.get("NUMEND"))?"0":endosoIt.get("NUMEND"))+",0";
						logger.debug("URL Generada para AEUA Inciso 1: "+ urlAeua + parametros);
						
						mensajeEmail.append("<br/><br/><a style=\"font-weight: bold\" href=\"").append(urlAeua).append(parametros).append("\">Asistencia en Estados Unidos y Canad\u00E1</a>");
						
						documentosManager.guardarDocumento(
								cdunieco
								,cdramo
								,estado
								,nmpoliza
								,nmsuplem
								,new Date()
								,urlAeua + parametros
								,"Asistencia en Estados Unidos y Canad\u00E1"
								,nmpoliza
								,ntramite
								,cdtipsup
								,Constantes.SI
								,null
								,TipoTramite.POLIZA_NUEVA.getCdtiptra()
								,"0"
								,Documento.EXTERNO_AEUA
								,null
								,null, false
								);
					}
					
					if(StringUtils.isNotBlank(tipoGrupoInciso)  && ("F".equalsIgnoreCase(tipoGrupoInciso) || "P".equalsIgnoreCase(tipoGrupoInciso))){
						/**
						 * Para Incisos Flotillas
						 */
						parametros = "?"+emisionWS.getSucursal()+","+emisionWS.getSubramo()+","+emisionWS.getNmpoliex()+","+endosoIt.get("TIPOEND")+","+ (StringUtils.isBlank(endosoIt.get("NUMEND"))?"0":endosoIt.get("NUMEND"));
						logger.debug("URL Generada para urlIncisosFlotillas: "+ urlIncisosFlot + parametros);
						
						mensajeEmail.append("<br/><br/><a style=\"font-weight: bold\" href=\"").append(urlIncisosFlot).append(parametros).append("\">Relaci\u00f3n de Incisos Flotillas</a>");
						
						documentosManager.guardarDocumento(
								cdunieco
								,cdramo
								,estado
								,nmpoliza
								,nmsuplem
								,new Date()
								,urlIncisosFlot + parametros
								,"Incisos Flotillas"+" ("+endosoIt.get("TIPOEND")+" - "+endosoIt.get("NUMEND")+")"
								,nmpoliza
								,ntramite
								,cdtipsup
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
							parametros = "?"+emisionWS.getSucursal()+","+emisionWS.getSubramo()+","+emisionWS.getNmpoliex()+","+endosoIt.get("TIPOEND")+","+ (StringUtils.isBlank(endosoIt.get("NUMEND"))?"0":endosoIt.get("NUMEND"))+",0";
							logger.debug("URL Generada para urlIncisosExcelFlot: "+ urlIncisosExcelFlot + parametros);
							
							mensajeEmail.append("<br/><br/><a style=\"font-weight: bold\" href=\"").append(urlIncisosExcelFlot).append(parametros).append("\">Relaci\u00f3n de Incisos EXCEL</a>");
							
							documentosManager.guardarDocumento(
									cdunieco
									,cdramo
									,estado
									,nmpoliza
									,nmsuplem
									,new Date()
									,urlIncisosExcelFlot + parametros
									,"Incisos EXCEL"+" ("+endosoIt.get("TIPOEND")+" - "+endosoIt.get("NUMEND")+")"
									,nmpoliza
									,ntramite
									,cdtipsup
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
						
						int numeroIncisos = consultasPolizaManager.obtieneNumeroDeIncisosPoliza(cdunieco, cdramo, "M", nmpoliza, nmsuplem);
						
						if(numeroIncisos > 0 && !soloUnInciso){
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
								
								boolean imprimirReporte =  false; // Se usa solo cuando hay una lista de incisos Afectados
								
								int desdeInciso = ((numReporte-1) * Integer.parseInt(numIncisosReporte))+1;
								int hastaInciso = numReporte * Integer.parseInt(numIncisosReporte);
								
								if(numReporte == numeroReportes && reporteSobrante > 0 ){
									hastaInciso = ((numReporte-1) * Integer.parseInt(numIncisosReporte)) + reporteSobrante;
								}
								
								if(soloIncisosAfectados){
									String incisoComparar = null;
									for(int inciso = desdeInciso; inciso <= hastaInciso ; inciso++){
										
										incisoComparar = Integer.toString(inciso);
										if(incisosAfectados.containsKey(incisoComparar)){
											imprimirReporte =  true;
											break;
										}
									}
								}
								
								if(soloIncisosAfectados){
									if(imprimirReporte){
										parametros = "?"+emisionWS.getSucursal()+","+emisionWS.getSubramo()+","+emisionWS.getNmpoliex()+","+endosoIt.get("TIPOEND")+","+ (StringUtils.isBlank(endosoIt.get("NUMEND"))?"0":endosoIt.get("NUMEND"))+","+desdeInciso+","+hastaInciso;
										logger.debug("URL Generada para Tarjeta Identificacion: "+ urlTarjIdent + parametros);
										
										mensajeEmail.append("<br/><br/><a style=\"font-weight: bold\" href=\"").append(urlTarjIdent).append(parametros).append("\">Tarjeta de Identificaci\u00f3n. ").append(desdeInciso).append(" - ").append(hastaInciso).append(" de ").append(numeroIncisos).append("</a>");
										
										documentosManager.guardarDocumento(
												cdunieco
												,cdramo
												,estado
												,nmpoliza
												,nmsuplem
												,new Date()
												,urlTarjIdent + parametros
												,"Tarjeta de Identificacion"+" (Endoso: "+endosoIt.get("TIPOEND")+" - "+endosoIt.get("NUMEND")+"). " +desdeInciso+" - " + hastaInciso + " de "+ numeroIncisos
												,nmpoliza
												,ntramite
												,cdtipsup
												,Constantes.SI
												,null
												,TipoTramite.POLIZA_NUEVA.getCdtiptra()
												,"0"
												,Documento.EXTERNO_TARJETA_IDENTIFICACION
												,null
												,null, false
												);
									}else{
										logger.debug("No se imprime reporte de Tarjeta de Circulacion, no aplican incisos de este reporte pare este endoso. Incisos de Reporte: " + desdeInciso + "-" + hastaInciso);
									}
								}else{
									
									//Se imprimen Todos los reportes
									
									parametros = "?"+emisionWS.getSucursal()+","+emisionWS.getSubramo()+","+emisionWS.getNmpoliex()+","+endosoIt.get("TIPOEND")+","+ (StringUtils.isBlank(endosoIt.get("NUMEND"))?"0":endosoIt.get("NUMEND"))+","+desdeInciso+","+hastaInciso;
									logger.debug("URL Generada para Tarjeta Identificacion: "+ urlTarjIdent + parametros);
									
									mensajeEmail.append("<br/><br/><a style=\"font-weight: bold\" href=\"").append(urlTarjIdent).append(parametros).append("\">Tarjeta de Identificaci\u00f3n.").append("</a>");
									
									documentosManager.guardarDocumento(
											cdunieco
											,cdramo
											,estado
											,nmpoliza
											,nmsuplem
											,new Date()
											,urlTarjIdent + parametros
											,"Tarjeta de Identificacion"+" (Endoso: "+endosoIt.get("TIPOEND")+" - "+endosoIt.get("NUMEND")+"). " +desdeInciso+" - " + hastaInciso + " de "+ numeroIncisos
											,nmpoliza
											,ntramite
											,cdtipsup
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
							
						}else if(soloUnInciso){
							
							ArrayList<String> incisos = new ArrayList<String>(incisosAfectados.values());
							String numeroInciso = incisos.get(0);
							
							logger.debug("Imprimiendo solo una caratula a de Tarjeta de Identificacion para el inciso: " + numeroInciso);
							
							parametros = "?"+emisionWS.getSucursal()+","+emisionWS.getSubramo()+","+emisionWS.getNmpoliex()+","+endosoIt.get("TIPOEND")+","+ (StringUtils.isBlank(endosoIt.get("NUMEND"))?"0":endosoIt.get("NUMEND"))+","+numeroInciso;
							logger.debug("URL Generada para Tarjeta Identificacion: "+ urlTarjIdent + parametros);
							
							mensajeEmail.append("<br/><br/><a style=\"font-weight: bold\" href=\"").append(urlTarjIdent).append(parametros).append("\">Tarjeta de Identificaci\u00f3n.").append("</a>");
							
							documentosManager.guardarDocumento(
									cdunieco
									,cdramo
									,estado
									,nmpoliza
									,nmsuplem
									,new Date()
									,urlTarjIdent + parametros
									,"Tarjeta de Identificacion"+" (Endoso: "+endosoIt.get("TIPOEND")+" - "+endosoIt.get("NUMEND")+"). " +numeroInciso+" - " + numeroInciso + " de "+ numeroIncisos
									,nmpoliza
									,ntramite
									,cdtipsup
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
					
					/**
					 * Para cobertura de reduce GS
					 */
					if(StringUtils.isNotBlank(endosoIt.get("REDUCEGS")) && Constantes.SI.equalsIgnoreCase(endosoIt.get("REDUCEGS"))){
						
					    mensajeEmail.append("<br/><br/><a style=\"font-weight: bold\" href=\"").append(this.manualAgenteTxtinfocobredgs).append("\">Reduce GS</a>");
					    
						documentosManager.guardarDocumento(
								cdunieco
								,cdramo
								,estado
								,nmpoliza
								,nmsuplem
								,new Date()
								,this.manualAgenteTxtinfocobredgs
								,"Reduce GS"
								,nmpoliza
								,ntramite
								,cdtipsup
								,Constantes.SI
								,null
								,TipoTramite.POLIZA_NUEVA.getCdtiptra()
								,"0"
								,Documento.EXTERNO_REDUCE_GS
								,null
								,null, false
								);
					}
					
					/**
					 * Para cobertura de gestoria GS
					 */
					if(StringUtils.isNotBlank(endosoIt.get("GESTORIA")) && Constantes.SI.equalsIgnoreCase(endosoIt.get("GESTORIA"))){
						
					    mensajeEmail.append("<br/><br/><a style=\"font-weight: bold\" href=\"").append(this.manualAgenteTxtinfocobgesgs).append("\">Gestoria GS</a>");
					    
						documentosManager.guardarDocumento(
								cdunieco
								,cdramo
								,estado
								,nmpoliza
								,nmsuplem
								,new Date()
								,this.manualAgenteTxtinfocobgesgs
								,"Gestoria GS"
								,nmpoliza
								,ntramite
								,cdtipsup
								,Constantes.SI
								,null
								,TipoTramite.POLIZA_NUEVA.getCdtiptra()
								,"0"
								,Documento.EXTERNO_GESTORIA_GS
								,null
								,null, false
								);
					}

					
					/**
					 * Para cobertura de Seguro de Vida
					 */
					if(StringUtils.isNotBlank(endosoIt.get("COBVIDA")) && Constantes.SI.equalsIgnoreCase(endosoIt.get("COBVIDA"))){
						
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
								+ "&p_poliza="+nmpoliza
								+ "&p_suplem="+nmsuplem
								+ "&desname="+rutaCarpeta+"/"+pdfEspVidaNom;
						
						HttpUtil.generaArchivo(url,rutaCarpeta+"/"+pdfEspVidaNom);
						
						documentosManager.guardarDocumento(
								cdunieco
								,cdramo
								,estado
								,nmpoliza
								,nmsuplem
								,new Date()
								,pdfEspVidaNom
								,"Especificaciones Seguro de Vida"
								,nmpoliza
								,ntramite
								,cdtipsup
								,Constantes.SI
								,null
								,TipoTramite.POLIZA_NUEVA.getCdtiptra()
								,"0"
								,Documento.EXTERNO_ESPECIF_SEGURO_VIDA
								,null
								,null, false
								);
						
						mensajeEmail.append("<br/><br/><a style=\"font-weight: bold\" href=\"").append(this.manualAgenteCondgralescobsegvida).append("\">Condiciones Generales Seguro de Vida</a>");

						documentosManager.guardarDocumento(
								cdunieco
								,cdramo
								,estado
								,nmpoliza
								,nmsuplem
								,new Date()
								,this.manualAgenteCondgralescobsegvida
								,"Condiciones Generales Seguro de Vida"
								,nmpoliza
								,ntramite
								,cdtipsup
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
						parametros = "?"+emisionWS.getSucursal()+","+emisionWS.getSubramo()+","+emisionWS.getNmpoliex()+",2";
						logger.debug("URL Generada para Sanas Practicas: "+ urlDocsExtra + parametros);
						mensajeEmail.append("<br/><br/><a style=\"font-weight: bold\" href=\"").append(urlDocsExtra).append(parametros).append("\">Sanas Pr\u00e1cticas</a>");
						
						documentosManager.guardarDocumento(
								cdunieco
								,cdramo
								,estado
								,nmpoliza
								,nmsuplem
								,new Date()
								,urlDocsExtra + parametros
								,"Sanas Pr\u00e1cticas"
								,nmpoliza
								,ntramite
								,cdtipsup
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
						
						if(TipoEndoso.ALTA_ASEGURADOS.getCdTipSup().toString().equalsIgnoreCase(cdtipsup)){
							parametros = "?"+emisionWS.getSucursal()+","+emisionWS.getSubramo()+","+emisionWS.getNmpoliex()+",1";
							logger.debug("URL Generada para Constancia de Recepcion de Documentacion Contractual: "+ urlDocsExtra + parametros);
							mensajeEmail.append("<br/><br/><a style=\"font-weight: bold\" href=\"").append(urlDocsExtra).append(parametros).append("\">Constancia de Recepci\u00f3n de Documentaci\u00f3n Contractual</a>");
							
							documentosManager.guardarDocumento(
									cdunieco
									,cdramo
									,estado
									,nmpoliza
									,nmsuplem
									,new Date()
									,urlDocsExtra + parametros
									,"Constancia de Recepci\u00f3n de Documentaci\u00f3n Contractual"
									,nmpoliza
									,ntramite
									,cdtipsup
									,Constantes.SI
									,null
									,TipoTramite.POLIZA_NUEVA.getCdtiptra()
									,"0"
									,Documento.EXTERNO_DOCUMENTO_EXTRA
									,null
									,null, false
									);
						}
					}
				}
			}
			
			mensajeEmail.append(emisionManager.generarLigasDocumentosEmisionLocalesIce(ntramite));
			
			mensajeEmail.append("<br/><br/><br/>Agradecemos su preferencia.<br/>").append(
                    "General de Seguros<br/>").append(
                    "</span>");
			
			try {
                String ntramiteEndoso =  consultasManager.recuperarTramitePorNmsuplem(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
                
                flujoMesaControlManager.guardarMensajeCorreoEmision(
                        ntramiteEndoso,
                        Utils.cambiaAcentosUnicodePorGuionesBajos(mensajeEmail.toString())
                );
                
                logger.debug("Enviando correos configurados");
                flujoMesaControlManager.mandarCorreosStatusTramite(ntramiteEndoso, RolSistema.SUSCRIPTOR_AUTO.getCdsisrol(), false);
            } catch (Exception ex) {
                logger.debug("Error al enviar correos de estatus al turnar", ex);
            }
			
			mensajeEmail = new StringBuilder(Utils.cambiaGuionesBajosPorAcentosHtml(Utils.cambiaAcentosUnicodePorGuionesBajos(mensajeEmail.toString())));
			
		} catch (Exception e) {
			logger.error("Error al generar las Caratulas de endoso: " + cdtipsup, e);
			return false;
		}
		
		return true;
	}
	
	public String reenviaRecibosAuto()
	{
		logger.debug(Utils.log(
				"\n#################################"
				,"\n###### reenviaRecibosAuto ######"
				,"\n###### smap1="  , smap1
				));
		try{
			String cdunieco = smap1.get("cdunieco");
			String cdramo   = smap1.get("cdramo");
			String estado   = smap1.get("estado");
			String nmpoliza = smap1.get("nmpoliza");
			String nmsuplem = smap1.get("nmsuplem");
			String nmpoliex = smap1.get("nmpoliex");
			String subramo  = smap1.get("subramo");
			String sucursal = smap1.get("sucursal");
			
			Integer valida = emisionAutosService.enviaRecibosAutosSigs(cdunieco, cdramo, estado, nmpoliza, nmsuplem, nmpoliex, subramo, sucursal);
			
			if(valida == null || valida != 0){
				success = false;
			}else{
				success = true;
			}
			
		}catch(Exception ex){
			respuesta=Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				"\n###### reenviaRecibosAuto ######"
				,"\n###############################"
				));
		return SUCCESS;
	}
	
	/**
	 * Transforma los parametros que se reciben del marco de endosos nuevo
	 * en parametros que enviaban el marco de endosos anterior, para poder
	 * usar las pantallas de endosos anteriores en el nuevo marco
	 * @param smap1
	 * @param slist1
	 * @throws Exception
	 */
	public void transformaEntrada(Map<String,String>smap1, List<Map<String,String>>slist1, boolean validaLista) throws Exception
	{
		logger.debug("##### EndososAction.transformaEntrada ######");
		Utils.validate(smap1  , "No se recibieron datos");
		
		boolean origenMarcoGeneral = "MARCO_ENDOSOS_GENERAL".equals(smap1.get("pantallaOrigen"));
		logger.debug(Utils.log("\norigenMarcoGeneral=",origenMarcoGeneral));
		
		if(origenMarcoGeneral)
		{
			logger.debug("##### EndososAction.transformaEntrada origenMarcoGeneral ######");
			if(validaLista)
			{
				Utils.validate(slist1 , "No se recibieron incisos");
			}
			
			smap1.put("cdunieco"    , smap1.get("CDUNIECO"));
			smap1.put("pv_cdunieco" , smap1.get("CDUNIECO"));
			smap1.put("cdramo"      , smap1.get("CDRAMO"));
			smap1.put("pv_cdramo"   , smap1.get("CDRAMO"));
			smap1.put("estado"      , smap1.get("ESTADO"));
			smap1.put("pv_estado"   , smap1.get("ESTADO"));
			smap1.put("nmpoliza"    , smap1.get("NMPOLIZA"));
			smap1.put("pv_nmpoliza" , smap1.get("NMPOLIZA"));
			smap1.put("ntramite"    , smap1.get("NTRAMITE"));
			smap1.put("nmsuplem"    , smap1.get("NMSUPLEM"));
			
			if(smap1.get("cdtipsup").equals(TipoEndoso.CAMBIO_DOMICILIO.getCdTipSup().toString())
					||smap1.get("cdtipsup").equals(TipoEndoso.CORRECCION_DATOS_ASEGURADOS.getCdTipSup().toString())
					)
			{
				smap1.put("habilitaEdicion" , endososManager.esMismaPersonaContratante(
						smap1.get("CDUNIECO")
						,smap1.get("CDRAMO")
						,smap1.get("ESTADO")
						,smap1.get("NMPOLIZA")
						,slist1.get(0).get("NMSITUAC")) ? "1" : "0");
				
				if(smap1.get("cdtipsup").equals(TipoEndoso.CORRECCION_DATOS_ASEGURADOS.getCdTipSup().toString()))
				{
					endosoSimple = true;
				}
				
			}
			else if(smap1.get("cdtipsup").equals(TipoEndoso.ALTA_ASEGURADOS.getCdTipSup().toString())
					||smap1.get("cdtipsup").equals(TipoEndoso.BAJA_ASEGURADOS.getCdTipSup().toString())
			)
			{
				//necesitamos CDTIPSIT en el smap1
				smap1.put("CDTIPSIT" , endososManager.recuperarCdtipsitInciso1(
						smap1.get("CDUNIECO")
						,smap1.get("CDRAMO")
						,smap1.get("ESTADO")
						,smap1.get("NMPOLIZA")));
				
				if(smap2==null)
				{
					smap2 = new HashMap<String,String>();
				}
				if(smap1.get("cdtipsup").equals(TipoEndoso.ALTA_ASEGURADOS.getCdTipSup().toString()))
				{
					smap2.put("alta" , "si");
				}
				else
				{
					smap2.put("alta" , "no");
				}
				
				//necesitamos el NMSUPLEM mas nuevo
				String nmsuplem = endososManager.recuperarUltimoNmsuplem(
						smap1.get("CDUNIECO")
						,smap1.get("CDRAMO")
						,smap1.get("ESTADO")
						,smap1.get("NMPOLIZA")
						);
				smap1.put("NMSUPLEM" , nmsuplem);
				smap1.put("nmsuplem" , nmsuplem);
			}
			else if(smap1.get("cdtipsup").equals(TipoEndoso.INCREMENTO_EDAD_ASEGURADO.getCdTipSup().toString())
					||smap1.get("cdtipsup").equals(TipoEndoso.DECREMENTO_EDAD_ASEGURADO.getCdTipSup().toString())
			)
			{
				if(smap1.get("cdtipsup").equals(TipoEndoso.INCREMENTO_EDAD_ASEGURADO.getCdTipSup().toString()))
				{
					smap1.put("masedad" , "si");
				}
				else
				{
					smap1.put("masedad" , "no");
				}
			}
			else if(smap1.get("cdtipsup").equals(TipoEndoso.MODIFICACION_SEXO_H_A_M.getCdTipSup().toString())
					||smap1.get("cdtipsup").equals(TipoEndoso.MODIFICACION_SEXO_M_A_H.getCdTipSup().toString())
			)
			{
				if(smap1.get("cdtipsup").equals(TipoEndoso.MODIFICACION_SEXO_H_A_M.getCdTipSup().toString()))
				{
					smap1.put("hombremujer" , "si");
					if("M".equals(slist1.get(0).get("CVE_SEXO")))
					{
						throw new ApplicationException("Debe seleccionar un hombre");
					}
				}
				else
				{
					smap1.put("hombremujer" , "no");
					if("H".equals(slist1.get(0).get("CVE_SEXO")))
					{
						throw new ApplicationException("Debe seleccionar una mujer");
					}
				}
			}
			else if(smap1.get("cdtipsup").equals(TipoEndoso.CAMBIO_DOMICILIO_ASEGURADO_TITULAR.getCdTipSup().toString()))
			{
				if(!"T".equals(slist1.get(0).get("CVE_PARENTESCO")))
				{
					throw new ApplicationException("Debe seleccionar un titular para este endoso.");
				}
			}
			else if(smap1.get("cdtipsup").equals(TipoEndoso.DEDUCIBLE_MAS.getCdTipSup().toString())
					||smap1.get("cdtipsup").equals(TipoEndoso.DEDUCIBLE_MENOS.getCdTipSup().toString())
					)
			{
				//necesitamos CDTIPSIT en el smap1
				smap1.put("CDTIPSIT" , endososManager.recuperarCdtipsitInciso1(
						smap1.get("CDUNIECO")
						,smap1.get("CDRAMO")
						,smap1.get("ESTADO")
						,smap1.get("NMPOLIZA")));
				
				if(smap2==null)
				{
					smap2 = new HashMap<String,String>();
				}
				if(smap1.get("cdtipsup").equals(TipoEndoso.DEDUCIBLE_MAS.getCdTipSup().toString()))
				{
					smap2.put("masdeducible" , "si");
				}
				else
				{
					smap2.put("masdeducible" , "no");
				}
			}
			else if(smap1.get("cdtipsup").equals(TipoEndoso.COPAGO_MAS.getCdTipSup().toString())
					||smap1.get("cdtipsup").equals(TipoEndoso.COPAGO_MENOS.getCdTipSup().toString())
					)
			{
				//necesitamos CDTIPSIT en el smap1
				smap1.put("CDTIPSIT" , endososManager.recuperarCdtipsitInciso1(
						smap1.get("CDUNIECO")
						,smap1.get("CDRAMO")
						,smap1.get("ESTADO")
						,smap1.get("NMPOLIZA")));
				
				if(smap2==null)
				{
					smap2 = new HashMap<String,String>();
				}
				if(smap1.get("cdtipsup").equals(TipoEndoso.COPAGO_MAS.getCdTipSup().toString()))
				{
					smap2.put("mascopago" , "si");
				}
				else
				{
					smap2.put("mascopago" , "no");
				}
			}
			else if(smap1.get("cdtipsup").equals(TipoEndoso.CANCELACION_POR_REEXPEDICION.getCdTipSup().toString()))
			{
				//necesitamos CDTIPSIT en el smap1
				smap1.put("CDTIPSIT" , endososManager.recuperarCdtipsitInciso1(
						smap1.get("CDUNIECO")
						,smap1.get("CDRAMO")
						,smap1.get("ESTADO")
						,smap1.get("NMPOLIZA")));
			}
			else if(smap1.get("cdtipsup").equals(TipoEndoso.EXTRAPRIMA_MAS.getCdTipSup().toString())
					||smap1.get("cdtipsup").equals(TipoEndoso.EXTRAPRIMA_MENOS.getCdTipSup().toString())
					)
			{
				if(smap2==null)
				{
					smap2 = new HashMap<String,String>();
				}
				if(smap1.get("cdtipsup").equals(TipoEndoso.EXTRAPRIMA_MAS.getCdTipSup().toString()))
				{
					smap2.put("masextraprima" , "si");
				}
				else
				{
					smap2.put("masextraprima" , "no");
				}
			}
			else if(smap1.get("cdtipsup").equals(TipoEndoso.CAMBIO_FORMA_PAGO.getCdTipSup().toString()))
			{
				//necesitamos CDTIPSIT en el smap1
				smap1.put("CDTIPSIT" , endososManager.recuperarCdtipsitInciso1(
						smap1.get("CDUNIECO")
						,smap1.get("CDRAMO")
						,smap1.get("ESTADO")
						,smap1.get("NMPOLIZA")));
			}
			else if(smap1.get("cdtipsup").equals(TipoEndoso.CAMBIO_AGENTE.getCdTipSup().toString()))
			{
				//necesitamos CDTIPSIT en el smap1
				smap1.put("CDTIPSIT" , endososManager.recuperarCdtipsitInciso1(
						smap1.get("CDUNIECO")
						,smap1.get("CDRAMO")
						,smap1.get("ESTADO")
						,smap1.get("NMPOLIZA")));
			}
			else if(smap1.get("cdtipsup").equals(TipoEndoso.CAMBIO_CONTRATANTE.getCdTipSup().toString()))
			{
				//necesitamos CDTIPSIT en el smap1
				smap1.put("CDTIPSIT" , endososManager.recuperarCdtipsitInciso1(
						smap1.get("CDUNIECO")
						,smap1.get("CDRAMO")
						,smap1.get("ESTADO")
						,smap1.get("NMPOLIZA")));
			}
			else if(smap1.get("cdtipsup").equals(TipoEndoso.DESPAGO.getCdTipSup().toString()))
			{
				//necesitamos CDTIPSIT en el smap1
				smap1.put("CDTIPSIT" , endososManager.recuperarCdtipsitInciso1(
						smap1.get("CDUNIECO")
						,smap1.get("CDRAMO")
						,smap1.get("ESTADO")
						,smap1.get("NMPOLIZA")));
			}
			else if(smap1.get("cdtipsup").equals(TipoEndoso.SUMA_ASEGURADA_INCREMENTO.getCdTipSup().toString())
					||smap1.get("cdtipsup").equals(TipoEndoso.SUMA_ASEGURADA_DECREMENTO.getCdTipSup().toString())
			)
			{
				//necesitamos CDTIPSIT en el smap1
				smap1.put("CDTIPSIT" , endososManager.recuperarCdtipsitInciso1(
						smap1.get("CDUNIECO")
						,smap1.get("CDRAMO")
						,smap1.get("ESTADO")
						,smap1.get("NMPOLIZA")));
			}
			else if(smap1.get("cdtipsup").equals(TipoEndoso.BENEFICIARIO_AUTO.getCdTipSup().toString()))
			{
				smap1.put("cdrolPipes"   , "3");
				smap1.put("ultimaImagen" , "S");
			}
			else if(smap1.get("cdtipsup").equals(TipoEndoso.COASEGURO_INCREMENTO.getCdTipSup().toString())
					||smap1.get("cdtipsup").equals(TipoEndoso.COASEGURO_DECREMENTO.getCdTipSup().toString())
					||smap1.get("cdtipsup").equals(TipoEndoso.CIRCULO_HOSPITALARIO_COASEGURO_INCREMENTO.getCdTipSup().toString())
					||smap1.get("cdtipsup").equals(TipoEndoso.CIRCULO_HOSPITALARIO_COASEGURO_DECREMENTO.getCdTipSup().toString())
			)
			{
				//necesitamos CDTIPSIT en el smap1
				smap1.put("CDTIPSIT" , endososManager.recuperarCdtipsitInciso1(
						smap1.get("CDUNIECO")
						,smap1.get("CDRAMO")
						,smap1.get("ESTADO")
						,smap1.get("NMPOLIZA")));
			}
			else if(smap1.get("cdtipsup").equals(TipoEndoso.REHABILITACION_DESPAGO.getCdTipSup().toString()))
			{
				//necesitamos CDTIPSIT en el smap1
				smap1.put("CDTIPSIT" , endososManager.recuperarCdtipsitInciso1(
						smap1.get("CDUNIECO")
						,smap1.get("CDRAMO")
						,smap1.get("ESTADO")
						,smap1.get("NMPOLIZA")));
			}
			
			if(validaLista)
			{
				Map<String,String> inciso1 = slist1.get(0);
				smap1.put("Apellido_Materno" , inciso1.get("DSAPELLIDO1"));
				smap1.put("Apellido_Paterno" , inciso1.get("DSAPELLIDO"));
				smap1.put("CANALING"         , inciso1.get("CANALING"));
				smap1.put("CONDUCTO"         , inciso1.get("CONDUCTO"));
				smap1.put("PTCUMUPR"         , inciso1.get("PTCUMUPR"));
				smap1.put("Parentesco"       , inciso1.get("CVE_PARENTESCO"));
				smap1.put("RESIDENCIA"       , inciso1.get("RESIDENCIA"));
				smap1.put("cdideext"         , inciso1.get("CDIDEEXT"));
				smap1.put("cdideper"         , inciso1.get("CDIDEPER"));
				smap1.put("cdperson"         , inciso1.get("CDPERSON"));
				smap1.put("cdrfc"            , inciso1.get("CDRFC"));
				smap1.put("cdrol"            , inciso1.get("CDROL"));
				smap1.put("fenacimi"         , inciso1.get("FENACIMI"));
				smap1.put("nacional"         , inciso1.get("CDNACION"));
				smap1.put("nmsituac"         , inciso1.get("NMSITUAC"));
				smap1.put("nombre"           , inciso1.get("DSNOMBRE"));
				smap1.put("segundo_nombre"   , inciso1.get("DSNOMBRE1"));
				smap1.put("sexo"             , inciso1.get("CVE_SEXO"));
				smap1.put("swexiper"         , inciso1.get("SWEXIPER"));
				smap1.put("tpersona"         , inciso1.get("OTFISJUR"));
				
				smap1.put("cdtipsit"         , inciso1.get("CDTIPSIT"));
				smap1.put("CDTIPSIT"         , inciso1.get("CDTIPSIT"));
				smap1.put("nmsituac"         , inciso1.get("NMSITUAC"));
				smap1.put("pv_nmsituac"      , inciso1.get("NMSITUAC"));
				smap1.put("pv_cdperson"      , inciso1.get("CDPERSON"));
				smap1.put("cdrfc"            , inciso1.get("CDRFC"));
				smap1.put("pv_cdrol"         , inciso1.get("CDROL"));
				smap1.put("nombreAsegurado"  ,
						Utils.join(
								inciso1.get("DSNOMBRE")
								," "
								,inciso1.get("DSNOMBRE1")!=null?inciso1.get("DSNOMBRE1"):""
								," "
								,inciso1.get("DSAPELLIDO")
								," "
								,inciso1.get("DSAPELLIDO1")
								));
				smap1.put("nombrecompleto"  ,
						Utils.join(
								inciso1.get("DSNOMBRE")
								," "
								,inciso1.get("DSNOMBRE1")!=null?inciso1.get("DSNOMBRE1"):""
								," "
								,inciso1.get("DSAPELLIDO")
								," "
								,inciso1.get("DSAPELLIDO1")
								));
				
				for(Map<String,String> inciso : slist1)
				{
					inciso.put("Apellido_Materno" , inciso.get("DSAPELLIDO1"));
					inciso.put("Apellido_Paterno" , inciso.get("DSAPELLIDO"));
					inciso.put("CANALING"         , inciso.get("CANALING"));
					inciso.put("CONDUCTO"         , inciso.get("CONDUCTO"));
					inciso.put("PTCUMUPR"         , inciso.get("PTCUMUPR"));
					inciso.put("Parentesco"       , inciso.get("CVE_PARENTESCO"));
					inciso.put("RESIDENCIA"       , inciso.get("RESIDENCIA"));
					inciso.put("cdideext"         , inciso.get("CDIDEEXT"));
					inciso.put("cdideper"         , inciso.get("CDIDEPER"));
					inciso.put("cdperson"         , inciso.get("CDPERSON"));
					inciso.put("cdrfc"            , inciso.get("CDRFC"));
					inciso.put("cdrol"            , inciso.get("CDROL"));
					inciso.put("fenacimi"         , inciso.get("FENACIMI"));
					inciso.put("nacional"         , inciso.get("CDNACION"));
					inciso.put("nmsituac"         , inciso.get("NMSITUAC"));
					inciso.put("nombre"           , inciso.get("DSNOMBRE"));
					inciso.put("segundo_nombre"   , inciso.get("DSNOMBRE1"));
					inciso.put("sexo"             , inciso.get("CVE_SEXO"));
					inciso.put("swexiper"         , inciso.get("SWEXIPER"));
					inciso.put("tpersona"         , inciso.get("OTFISJUR"));
				}
			}
		}
		else
		{
			logger.debug("##### EndososAction.transformaEntrada normal ######");
		}
	}
	
	public String cargaAsegurados()
	{
		logger.debug("\n"
				+ "\n#############################"
				+ "\n#############################"
				+ "\n###### cargaAsegurados ######"
				+ "\n######                 ######"
				);
		logger.debug("smap1: "+smap1);
		try
		{
			String cdunieco = smap1.get("cdunieco");
			String cdramo   = smap1.get("cdramo");
			String estado   = smap1.get("estado");
			String nmpoliza = smap1.get("nmpoliza");
			String nmsuplem = smap1.get("nmsuplem");
			
			slist1=endososManager.obtenerAseguradosPoliza(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
			
			success=true;
		}
		catch(Exception ex)
		{
			logger.error("error al cargar asegurados para el endoso de parentesco",ex);
			error=ex.getMessage();
			success=false;
		}
		logger.debug("\n"
				+ "\n######                 ######"
				+ "\n###### cargaAsegurados ######"
				+ "\n#############################"
				+ "\n#############################"
				);
		return SUCCESS;
	}
	
	public String obtieneInfoFamiliaEndoso()
	{
		logger.debug("\n"
				+ "\n######################################"
				+ "\n######################################"
				+ "\n###### obtieneInfoFamiliaEndoso ######"
				+ "\n######                 		 ######"
				);
		logger.debug("smap1: "+smap1);
		try
		{
			String cdunieco = smap1.get("cdunieco");
			String cdramo   = smap1.get("cdramo");
			String estado   = smap1.get("estado");
			String nmpoliza = smap1.get("nmpoliza");
			String nmsuplem = smap1.get("nmsuplem");
			String ntramite = smap1.get("ntramite");
			
			slist1=endososManager.obtenerInfoFamiliaEndoso(cdunieco, cdramo, estado, nmpoliza, nmsuplem, ntramite);
			success=true;
		}
		catch(Exception ex)
		{
			logger.error("error al cargar asegurados para el endoso de parentesco",ex);
			error=ex.getMessage();
			success=false;
		}
		logger.debug("\n"
				+ "\n######                 	     ######"
				+ "\n###### obtieneInfoFamiliaEndoso ######"
				+ "\n######################################"
				+ "\n######################################"
				);
		return SUCCESS;
	}
	
	
	
	
	
	/****************************** BASE ACTION **********************************/
	
	///////////////////////////////
	////// getters y setters //////
	/*///////////////////////////*/
	public List<Map<String, String>> getSlist1() {
		return slist1;
	}

	public void setSlist1(List<Map<String, String>> slist1) {
		this.slist1 = slist1;
	}
	
	public Map<String, String> getSmap4() {
		return smap4;
	}

	public void setSmap4(Map<String, String> smap4) {
		this.smap4 = smap4;
	}

	public Map<String, String> getSmap1() {
		return smap1;
	}

	public void setSmap1(Map<String, String> smap1) {
		this.smap1 = smap1;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setEndososManager(EndososManager endososManager) {
		this.endososManager = endososManager;
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

	public void setKernelManager(KernelManagerSustituto kernelManager) {
		this.kernelManager = kernelManager;
	}

	public Map<String, Object> getOmap1() {
		return omap1;
	}

	public void setOmap1(Map<String, Object> omap1) {
		this.omap1 = omap1;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Map<String, String> getParametros() {
		return parametros;
	}

	public void setParametros(Map<String, String> parametros) {
		this.parametros = parametros;
	}

	public Map<String, String> getSmap2() {
		return smap2;
	}

	public void setSmap2(Map<String, String> smap2) {
		this.smap2 = smap2;
	}

	public List<Map<String, String>> getSlist2() {
		return slist2;
	}

	public void setSlist2(List<Map<String, String>> slist2) {
		this.slist2 = slist2;
	}

	public Item getItem3() {
		return item3;
	}

	public void setItem3(Item item3) {
		this.item3 = item3;
	}

	public Map<String, Item> getImap1() {
		return imap1;
	}

	public void setImap1(Map<String, Item> imap1) {
		this.imap1 = imap1;
	}

	public void setPantallasManager(PantallasManager pantallasManager) {
		this.pantallasManager = pantallasManager;
	}
	
	public void setIce2sigsService(Ice2sigsService ice2sigsService) {
		this.ice2sigsService = ice2sigsService;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Map<String, String> getSmap3() {
		return smap3;
	}

	public void setSmap3(Map<String, String> smap3) {
		this.smap3 = smap3;
	}

	public void setCancelacionManager(CancelacionManager cancelacionManager) {
		this.cancelacionManager = cancelacionManager;
	}

	public boolean isEndosoSimple() {
		return endosoSimple;
	}

	public void setEndosoSimple(boolean endosoSimple) {
		this.endosoSimple = endosoSimple;
	}

	public void setConsultasManager(ConsultasManager consultasManager) {
		this.consultasManager = consultasManager;
	}

	public void setStoredProceduresManager(
			StoredProceduresManager storedProceduresManager) {
		this.storedProceduresManager = storedProceduresManager;
	}

	public boolean isExito() {
		return exito;
	}

	public void setExito(boolean exito) {
		this.exito = exito;
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
	
	public String getColumnas() {
		return columnas;
	}

	public void setColumnas(String columnas) {
		this.columnas = columnas;
	}

	public boolean isEndosoConfirmado() {
		return endosoConfirmado;
	}

	public void setEndosoConfirmado(boolean endosoConfirmado) {
		this.endosoConfirmado = endosoConfirmado;
	}

	public void setCotizacionDAO(CotizacionDAO cotizacionDAO) {
		this.cotizacionDAO = cotizacionDAO;
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

	public String getNmsuplement() {
		return nmsuplement;
	}

	public void setNmsuplement(String nmsuplement) {
		this.nmsuplement = nmsuplement;
	}

	public String getNsuplogi() {
		return nsuplogi;
	}

	public void setNsuplogi(String nsuplogi) {
		this.nsuplogi = nsuplogi;
	}
	
	public String getRutaServidorReports() {
		return rutaServidorReports;
	}

	public String getPassServidorReports() {
		return passServidorReports;
	}

	public String getRutaDocumentosPoliza() {
		return rutaDocumentosPoliza;
	}

	public String getRutaDocumentosTemporal() {
		return rutaDocumentosTemporal;
	}

	public String getReciboImpresionAutosUrl() {
		return reciboImpresionAutosUrl;
	}

	public String getCaicImpresionAutosUrl() {
		return caicImpresionAutosUrl;
	}

	public String getAeuaImpresionAutosUrl() {
		return aeuaImpresionAutosUrl;
	}

	public String getApImpresionAutosUrl() {
		return apImpresionAutosUrl;
	}

	public String getIncisosFlotillasImpresionAutosUrl() {
		return incisosFlotillasImpresionAutosUrl;
	}

	public String getTarjetaIdenImpresionAutosUrl() {
		return tarjetaIdenImpresionAutosUrl;
	}

	public String getNumeroIncisosReporte() {
		return numeroIncisosReporte;
	}

	public String getCaratulaImpresionAutosUrl() {
		return caratulaImpresionAutosUrl;
	}

	public String getCaratulaImpresionAutosEndosobUrl() {
		return caratulaImpresionAutosEndosobUrl;
	}

	public String getCaratulaImpresionAutosServiciopublicoUrl() {
		return caratulaImpresionAutosServiciopublicoUrl;
	}

	public String getCaratulaImpresionAutosFlotillasUrl() {
		return caratulaImpresionAutosFlotillasUrl;
	}

	public String getManualAgenteTxtinfocobredgs() {
		return manualAgenteTxtinfocobredgs;
	}

	public String getManualAgenteCondgralescobsegvida() {
		return manualAgenteCondgralescobsegvida;
	}

	public String getManualAgenteTxtinfocobgesgs() {
		return manualAgenteTxtinfocobgesgs;
	}
	
}