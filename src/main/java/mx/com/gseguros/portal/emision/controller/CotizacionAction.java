package mx.com.gseguros.portal.emision.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
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

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionContext;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.kernel.service.KernelManagerSustituto;
import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.externo.service.StoredProceduresManager;
import mx.com.gseguros.mesacontrol.model.FlujoVO;
import mx.com.gseguros.mesacontrol.service.FlujoMesaControlManager;
import mx.com.gseguros.portal.catalogos.service.PersonasManager;
import mx.com.gseguros.portal.consultas.model.AseguradosFiltroVO;
import mx.com.gseguros.portal.consultas.model.PolizaAseguradoVO;
import mx.com.gseguros.portal.consultas.model.PolizaDTO;
import mx.com.gseguros.portal.consultas.model.RecuperacionSimple;
import mx.com.gseguros.portal.consultas.service.ConsultasManager;
import mx.com.gseguros.portal.consultas.service.ConsultasPolizaManager;
import mx.com.gseguros.portal.consultas.service.RecuperacionSimpleManager;
import mx.com.gseguros.portal.cotizacion.model.DatosUsuario;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaImapSmapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSlistSmapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSlistVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSmapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaVoidVO;
import mx.com.gseguros.portal.cotizacion.model.ParametroCotizacion;
import mx.com.gseguros.portal.cotizacion.model.PuntajeCoberturasPlanVO;
import mx.com.gseguros.portal.cotizacion.service.CotizacionManager;
import mx.com.gseguros.portal.despachador.model.RespuestaTurnadoVO;
import mx.com.gseguros.portal.despachador.service.DespachadorManager;
import mx.com.gseguros.portal.documentos.service.DocumentosManager;
import mx.com.gseguros.portal.emision.service.EmisionManager;
import mx.com.gseguros.portal.endosos.service.EndososManager;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.model.Reporte;
import mx.com.gseguros.portal.general.service.CatalogosManager;
import mx.com.gseguros.portal.general.service.PantallasManager;
import mx.com.gseguros.portal.general.service.ReportesManager;
import mx.com.gseguros.portal.general.service.ServiciosManager;
import mx.com.gseguros.portal.general.util.EstatusTramite;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.portal.general.util.ObjetoBD;
import mx.com.gseguros.portal.general.util.Ramo;
import mx.com.gseguros.portal.general.util.RolSistema;
import mx.com.gseguros.portal.general.util.TipoArchivo;
import mx.com.gseguros.portal.general.util.TipoEndoso;
import mx.com.gseguros.portal.general.util.TipoProcesoBloqueo;
import mx.com.gseguros.portal.general.util.TipoRamo;
import mx.com.gseguros.portal.general.util.TipoSituacion;
import mx.com.gseguros.portal.general.util.TipoTramite;
import mx.com.gseguros.portal.mesacontrol.service.MesaControlManager;
import mx.com.gseguros.portal.siniestros.service.SiniestrosManager;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.FTPSUtils;
import mx.com.gseguros.utils.HttpUtil;
import mx.com.gseguros.utils.Utils;
import mx.com.gseguros.ws.folioserviciopublico.client.axis2.FolioWSServiceStub.EmAdmfolId;
import mx.com.gseguros.ws.folioserviciopublico.service.AgentePorFolioService;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteGeneral;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteGeneralRespuesta;
import mx.com.gseguros.ws.ice2sigs.service.Ice2sigsService;
import mx.com.gseguros.ws.nada.client.axis2.VehicleStub.VehicleValue_Struc;
import mx.com.gseguros.ws.nada.service.NadaService;
import mx.com.gseguros.ws.tipocambio.client.axis2.TipoCambioWSServiceStub.ResponseTipoCambio;
import mx.com.gseguros.ws.tipocambio.service.TipoCambioDolarGSService;

public class CotizacionAction extends PrincipalCoreAction
{

	private static final long       serialVersionUID = 3237792502541753915L;
	private static final Logger     logger           = LoggerFactory.getLogger(CotizacionAction.class);
	private static SimpleDateFormat renderFechas     = new SimpleDateFormat("dd/MM/yyyy"); 
	private static SimpleDateFormat renderHora       = new SimpleDateFormat  ("HH:mm");
	
	private transient CatalogosManager       catalogosManager;
	private ConsultasManager                  consultasManager;
	private String                            error;
	private Map<String,Item>                  imap;
	private transient KernelManagerSustituto kernelManager;
	private PantallasManager                  pantallasManager;
	private List<Map<String,String>>          slist1;
	private List<Map<String,String>>          slist2;
	private Map<String,String>                smap1;
	private Map<String,String>                params;
	private StoredProceduresManager           storedProceduresManager;
	private NadaService          			   nadaService;
	private TipoCambioDolarGSService          tipoCambioService;
	private transient Ice2sigsService        ice2sigsService;
	private AgentePorFolioService             agentePorFolioService;
	private boolean                          success;
	private String                            respuesta;
	private String                            respuestaOculta = null;
	private boolean                          exito           = false;
	private File                              censo;
	private String                            censoFileName;
	private String                            censoContentType;
	private List<Map<String,Object>>          olist1;
	private CotizacionManager                 cotizacionManager;
	private SiniestrosManager                 siniestrosManager;
	private FlujoVO                           flujo;
	private String                            start;
	private String                            limit;
	private String                            total;
	private String                            saMed;
	
	@Autowired
	private EmisionManager emisionManager;
	
	@Autowired
	private ServiciosManager serviciosManager;
	
	@Autowired
	private ReportesManager reportesManager;
	
	@Autowired
	private DocumentosManager documentosManager;
	
	@Autowired
	private MesaControlManager mesaControlManager;
	
	@Autowired
	private FlujoMesaControlManager flujoMesaControlManager;
	
	@Autowired
	private RecuperacionSimpleManager recuperacionSimpleManager;
	
	@Autowired
	private EndososManager endososManager;
	
	@Autowired
	private ConsultasPolizaManager consultasPolizaManager;
	
	@Autowired
	private DespachadorManager despachadorManager;
	
	@Autowired
    private PersonasManager personasManager;
	
	@Value("${ruta.servidor.reports}")
    private String rutaServidorReports;

	@Value("${pass.servidor.reports}")
    private String passServidorReports;	
	
	@Value("${sigs.facultaDatosPolizaSicaps.url}")
    private String sigsFacultaDatosPolizaSicapsUrl;	
	
	@Value("${ruta.documentos.poliza}")
    private String rutaDocumentosPoliza;
	
	@Value("${ruta.documentos.temporal}")
    private String rutaDocumentosTemporal;
	
	@Value("${user.server.layouts}")
    private String userServerLayouts;	

	@Value("${pass.server.layouts}")
    private String passServerLayouts;	

	@Value("${directorio.server.layouts}")
    private String directorioServerLayouts;	

	@Value("${dominio.server.layouts}")
    private String dominioServerLayouts;	
	
	@Value("${dominio.server.layouts2}")
    private String dominioServerLayouts2;
	
	@Value("${sigs.obtenerDatosPorSucRamPol.url}")
    private String sigsObtenerDatosPorSucRamPolUrl;
	
	public CotizacionAction()
	{
		logger.debug("new CotizacionAction");
		this.session=ActionContext.getContext().getSession();
	}
	
	/////////////////////////////////
	////// cotizacion dinamica //////
	/*/////////////////////////////*/
	public String pantallaCotizacion()
	{
		this.session=ActionContext.getContext().getSession();
		logger.debug("\n"
				+ "\n################################"
				+ "\n###### pantallaCotizacion ######"
				+ "\n###### smap1: "+smap1
				+ "\n###### flujo: "+flujo
				+ "\n###### session!=null: "+(session!=null)
				);
		
		success = true;
		exito   = true;
		
		UserVO usuario     = null;
		String cdunieco    = null;
		String cdramo      = null;
		String cdtipsit    = null;
		String situacion   = null;
		String agrupacion  = null;
		String cdagente    = null;
		GeneradorCampos gc = null;
		String cdusuari    = null;
		String cdsisrol    = null;
		
		boolean renovacion = false;
		
		if(flujo!=null)
		{
			try
			{
				smap1 = new HashMap<String,String>();
				smap1.put("ntramite" , flujo.getNtramite());
				smap1.put("cdunieco" , flujo.getCdunieco());
				smap1.put("cdramo"   , flujo.getCdramo());
				
				logger.debug("Se recuperan datos del tramite accediendo por flujo");
				
				Map<String,Object> datosFlujo = flujoMesaControlManager.recuperarDatosTramiteValidacionCliente(flujo);
				
				Map<String,String> tramite = (Map<String,String>)datosFlujo.get("TRAMITE");
				
				cdagente = tramite.get("CDAGENTE");
				
				cdtipsit = tramite.get("CDTIPSIT");
				
				logger.debug("CDAGENTE={}",cdagente);

				smap1.put("cdagente" , cdagente);
				
				smap1.put("cdtipsit" , cdtipsit);
				
				logger.debug(Utils.log("\nsmap1 recuperado de flujo=",smap1));
				
				renovacion = TipoTramite.RENOVACION.getCdtiptra().equals(tramite.get("CDTIPTRA"));
				
				if(renovacion && !tramite.get("RENPOLIEX").isEmpty())
                {
                    smap1.put("renovacion","S");
                    smap1.put("RENUNIEXT" ,tramite.get("RENUNIEXT"));
                    smap1.put("RENRAMO"   ,tramite.get("RENRAMO")  );
                    smap1.put("RENPOLIEX" ,tramite.get("RENPOLIEX"));
                }   
				logger.debug("Es renovacion = {}", renovacion);
			}
			catch(Exception ex)
			{
				success = false;
				exito   = false;
				logger.error("Error al recuperar datos de flujo",ex);
				return ERROR;
			}
		}
		
		//instanciar el generador de campos
    	gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
		gc.setEsMovil(session!=null&&session.containsKey("ES_MOVIL")&&((Boolean)session.get("ES_MOVIL"))==true);
		if(!gc.isEsMovil() && smap1.containsKey("movil"))
		{
			gc.setEsMovil(true);
		}
		
		//datos de entrada
		if(exito)
		{
		    try
		    {
		    	usuario  = (UserVO) session.get("USUARIO");
		    	cdusuari = usuario.getUser();
		    	cdsisrol = usuario.getRolActivo().getClave();
		        cdramo   = smap1.get("cdramo");
		        cdtipsit = smap1.get("cdtipsit");
		        
		        smap1.put("cdusuari" , cdusuari);
		        smap1.put("cdsisrol" , cdsisrol);
				gc.setCdtipsit(cdtipsit);
		    }
		    catch(Exception ex)
		    {
		    	long timestamp  = System.currentTimeMillis();
		    	exito           = false;
		    	respuesta       = "No hay datos suficientes #"+timestamp;
		    	respuestaOculta = ex.getMessage();
		    	logger.error(respuesta,ex);
		    }
		}
		
		//poner valores a ntramite, cdunieco y cdramo
		if(exito)
		{
			try
			{
				//cuando viene ntramite tambien vienen cdunieco y cdramo 
		        if(StringUtils.isNotBlank(smap1.get("ntramite")))
		        {
		        	cdunieco = smap1.get("cdunieco");
		        }
		        //cuando no hay ntramite es porque esta cotizando un agente por fuera,
		        //y se obtiene cdunieco por medio de ese agente
		        else
		        {
	        		DatosUsuario datUsu=kernelManager.obtenerDatosUsuario(usuario.getUser(),cdtipsit);
	        		cdunieco=datUsu.getCdunieco();
	        		smap1.put("ntramite" , "");
	        		smap1.put("cdunieco" , cdunieco);
	        		
	        		//recuperamos agente de ser el que esta en sesion
	        		if(cdsisrol.equals(RolSistema.AGENTE.getCdsisrol()))
	        		{
	        			cdagente = datUsu.getCdagente();
	        		}
	        		smap1.put("cdagente" , cdagente);
		        }
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = "Usted no puede cotizar este producto #"+timestamp;
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
	            	smap1.putAll(tipoSituacion);
	            }
	            else
	            {
	            	throw new ApplicationException("No se ha parametrizado la situacion en ttipram");
	            }
	        }
	        catch(Exception ex)
	        {
	        	long timestamp  = System.currentTimeMillis();
	        	respuesta       = "Error al cargar tipo de situacion #"+timestamp;
	        	respuestaOculta = ex.getMessage();
	        	logger.error(respuesta,ex);
	        	
	        	this.addActionError("No se ha parametrizado el tipo de situación para el producto #"+timestamp);
	        	smap1.put("SITUACION"  , "PERSONA");
	        	smap1.put("AGRUPACION" , "SOLO");
	        }
	        finally
	        {
	        	situacion  = smap1.get("SITUACION");
	        	agrupacion = smap1.get("AGRUPACION");
	        }
		}
		
		// obtener campos de tatrisit
        if(exito)
        {	
	        List<ComponenteVO>camposAgrupados    = new ArrayList<ComponenteVO>(0);
	        List<ComponenteVO>camposIndividuales = new ArrayList<ComponenteVO>(0);
	        
	        imap  = new HashMap<String,Item>();
	        
	        try
	        {
	        	
	        	List<ComponenteVO>tatripoldxn = pantallasManager.obtenerComponentes(
						TipoTramite.POLIZA_NUEVA.getCdtiptra()
						,cdunieco
						,cdramo
						,cdtipsit
						,"I"
						,cdsisrol
						,"COTIZACION_CUSTOM"
						,"TATRIPOLDXN16"
						,null
						);
				
				gc.generaComponentes(tatripoldxn, true, false, true, false, false, false);
				imap.put("panelDxnItems", gc.getItems());
				
				
				
		        List<ComponenteVO>tatrisit = kernelManager.obtenerTatrisit(cdtipsit,usuario.getUser());
		        List<ComponenteVO>temp     = new ArrayList<ComponenteVO>();
		        
		        //iteracion para descartar componentes
		        for(ComponenteVO tatriIte:tatrisit)
				{
		        	//para permitir edicion cuando no tienen valor
		        	if(tatriIte.getValue()==null&&tatriIte.getDefaultValue()==null)
		        	{
		        		tatriIte.setComboVacio(true);
		        	}
		        	
		        	//para presentar en pantalla
		        	if(tatriIte.getSwpresen().equalsIgnoreCase("S"))
		        	{
		        		logger.debug("Se agrega como campo de cotizacion = {}", tatriIte.getNameCdatribu());
		        		temp.add(tatriIte);
		        	}
		        	//cuanto no van en pantalla
		        	else
		        	{
		        		//[parche] para poner para AF y PU
		        		if(cdtipsit.equalsIgnoreCase(TipoSituacion.AUTOS_FRONTERIZOS.getCdtipsit())||
		        				cdtipsit.equalsIgnoreCase(TipoSituacion.AUTOS_PICK_UP.getCdtipsit()))
		        		{
		        			if(tatriIte.getNameCdatribu().equalsIgnoreCase("26"))
		        			{
		        				tatriIte.setOculto(true);
		        				temp.add(tatriIte);
		        			}
		        		}
		        		
		        		if ( // Mostramos campos complementarios, la condicion es la misma que dentro
							        // del metodo CotizacionAutoManagerImpl.emisionAutoIndividual
		        				    // y del metodo CotizacionAutoManagerImpl.cotizacionAutoIndividual
							        // deben mantenerse iguales
		        				renovacion &&
		        				(StringUtils.isBlank(tatriIte.getSwtarifi())||tatriIte.getSwtarifi().equalsIgnoreCase("N"))
		        		) {
		        			logger.debug("Se agrega como campo de complementarios = {}", tatriIte.getNameCdatribu());
		        			temp.add(tatriIte);
		        		} else {
		        			logger.debug("No se agrega = {}", tatriIte.getNameCdatribu());
		        		}
		        	}
		        	
		        	//[parche] para AF y PU
		        	if(cdtipsit.equalsIgnoreCase(TipoSituacion.AUTOS_FRONTERIZOS.getCdtipsit())
		        			||cdtipsit.equalsIgnoreCase(TipoSituacion.AUTOS_PICK_UP.getCdtipsit()))
		        	{
		        		if(tatriIte.getNameCdatribu().equalsIgnoreCase("24"))
		        		{
			        		ResponseTipoCambio rtc=tipoCambioService.obtieneTipoCambioDolarGS(2);
			        		if(rtc!=null&&rtc.getTipoCambio()!=null&&rtc.getTipoCambio().getVenCam()!=null)
			        		{
			        			tatriIte.setOculto(true);
			        			tatriIte.setValue(rtc.getTipoCambio().getVenCam().doubleValue()+"");
			        		}
		        		}
		        	}
					//[parche] para ramo 6
					else if(cdramo.equals(Ramo.SERVICIO_PUBLICO.getCdramo()))
					{
						//agente
						if(tatriIte.getNameCdatribu().equalsIgnoreCase("17"))
						{
							//valor inicial si es agente
							if(cdsisrol.equals(RolSistema.AGENTE.getCdsisrol())
									&&StringUtils.isNotBlank(cdagente))
							{
								tatriIte.setValue(cdagente);
								tatriIte.setSoloLectura(true);
								logger.debug(
										new StringBuilder()
										.append("\n@@@@@@ parche pone cdagente=")
										.append(cdagente)
										.append(" @@@@@@")
										.toString()
										);
							}
							//sustituir componente si es promotor o suscriptor
							else if(cdsisrol.equalsIgnoreCase(RolSistema.PROMOTOR_AUTO.getCdsisrol())
									||cdsisrol.equalsIgnoreCase(RolSistema.SUSCRIPTOR_AUTO.getCdsisrol())
									||cdsisrol.equalsIgnoreCase(RolSistema.EMISOR_SUSCRI_DANIOS.getCdsisrol())
									||cdsisrol.equalsIgnoreCase(RolSistema.JEFE_SUSCRI_DANIOS.getCdsisrol())
									||cdsisrol.equalsIgnoreCase(RolSistema.GERENTE_SUSCRI_DANIOS.getCdsisrol())
									||cdsisrol.equalsIgnoreCase(RolSistema.SUBDIRECTOR_SUSCRI_DANIOS.getCdsisrol())
									||cdsisrol.equalsIgnoreCase(RolSistema.TECNICO_SUSCRI_DANIOS.getCdsisrol())
									)
							{
								List<ComponenteVO>componenteSustitutoListaAux=pantallasManager.obtenerComponentes(
										TipoTramite.POLIZA_NUEVA.getCdtiptra() , null         , "|"+cdramo+"|"
										,cdtipsit                              ,  "W"         , cdsisrol
										,"COTIZACION_CUSTOM"                   , "SUSTITUTOS" , "17");
								temp.remove(tatriIte);
								componenteSustitutoListaAux.get(0).setSwsuscri("N");
								temp.add(componenteSustitutoListaAux.get(0));
								
								if(flujo!=null) //entra por flujo y solo puede haber un agente
								{
									ComponenteVO agente = componenteSustitutoListaAux.get(0);
									
									if(StringUtils.isNotBlank(agente.getCatalogo()))
									{
										logger.debug("Se encontro y modifico el campo agente por venir de flujo: {}",agente);
										agente.setCatalogo("CATALOGO_CERRADO");
										agente.setQueryParam(null);
										agente.setParamName1(Utils.join("'params._",cdagente,"'"));
										agente.setParamValue1(Utils.join("'",cotizacionManager.cargarNombreAgenteTramite(smap1.get("ntramite")),"'"));
										agente.setParamName2(null);
										agente.setParamValue2(null);
										agente.setParamName3(null);
										agente.setParamValue3(null);
										agente.setParamName4(null);
										agente.setParamValue4(null);
										agente.setParamName5(null);
										agente.setParamValue5(null);
									}
								}
							}
						}
						//clave gs
						else if(tatriIte.getNameCdatribu().equalsIgnoreCase("22"))
						{
							if(cdtipsit.equals(TipoSituacion.SERVICIO_PUBLICO_AUTO.getCdtipsit()))
							{
								temp.remove(tatriIte);
								//agregar combo
								List<ComponenteVO>listaAuxComboAutos=pantallasManager.obtenerComponentes(
										TipoTramite.POLIZA_NUEVA.getCdtiptra()
										, null                   
										,cdramo
										,cdtipsit
										,"W"
										,cdsisrol
										,"COTIZACION_CUSTOM"
										,"COMBO_SERV_PUBL_AUTO"
										,null);
								listaAuxComboAutos.get(0).setSwsuscri("N");
								temp.add(listaAuxComboAutos.get(0));
							}
						}
						//descuento
						else if(tatriIte.getNameCdatribu().equalsIgnoreCase("21"))
						{
							List<ComponenteVO>componenteSustitutoListaAux=pantallasManager.obtenerComponentes(
									TipoTramite.POLIZA_NUEVA.getCdtiptra() , null         , "|"+cdramo+"|"
									,cdtipsit                              ,  "W"         , cdsisrol
									,"COTIZACION_CUSTOM"                   , "SUSTITUTOS" , "21");
							temp.remove(tatriIte);
							componenteSustitutoListaAux.get(0).setSwsuscri("N");
							temp.add(componenteSustitutoListaAux.get(0));
						}
					}
		        	
		        	//SERV PUBL MICRO
					if(cdtipsit.equalsIgnoreCase(TipoSituacion.SERVICIO_PUBLICO_MICRO.getCdtipsit()))
					{
						//NEGOCIO
						if(tatriIte.getNameCdatribu().equalsIgnoreCase("30"))
						{
							List<ComponenteVO>componenteSustitutoListaAux=pantallasManager.obtenerComponentes(
									TipoTramite.POLIZA_NUEVA.getCdtiptra() , null         , "|"+cdramo+"|"
									,cdtipsit                              ,  "W"         , cdsisrol
									,"COTIZACION_CUSTOM"                   , "SUSTITUTOS" , "30");
							temp.remove(tatriIte);
							componenteSustitutoListaAux.get(0).setSwsuscri("N");
							temp.add(componenteSustitutoListaAux.get(0));
						}
					}
		        	//SERV PUBL AUTO
					else if(cdtipsit.equalsIgnoreCase(TipoSituacion.SERVICIO_PUBLICO_AUTO.getCdtipsit()))
					{
						//NEGOCIO
						if(tatriIte.getNameCdatribu().equalsIgnoreCase("31"))
						{
							List<ComponenteVO> componenteSustitutoListaAux = pantallasManager.obtenerComponentes(
									TipoTramite.POLIZA_NUEVA.getCdtiptra() , null         , "|"+cdramo+"|"
									,cdtipsit                              ,  "W"         , cdsisrol
									,"COTIZACION_CUSTOM"                   , "SUSTITUTOS" , "31");
							temp.remove(tatriIte);
							componenteSustitutoListaAux.get(0).setSwsuscri("N");
							temp.add(componenteSustitutoListaAux.get(0));
						}
					}
					
					//[parche] para fronterizos
					if(cdramo.equals(Ramo.AUTOS_FRONTERIZOS.getCdramo()))
					{
						logger.debug("tatriIte=" + tatriIte);
						//agente
						if(tatriIte.getNameCdatribu().equalsIgnoreCase("32"))
						{
							logger.debug("tatriIte==" + tatriIte);
							//valor inicial si es agente
							if(cdsisrol.equals(RolSistema.AGENTE.getCdsisrol())
									&&StringUtils.isNotBlank(cdagente))
							{
								tatriIte.setValue(cdagente);
								tatriIte.setSoloLectura(true);
								logger.debug(
										new StringBuilder()
										.append("\n@@@@@@ parche pone cdagente=")
										.append(cdagente)
										.append(" @@@@@@")
										.toString()
										);
							}
							//sustituir componente si es promotor o suscriptor
							else if(cdsisrol.equalsIgnoreCase(RolSistema.PROMOTOR_AUTO.getCdsisrol())
									||cdsisrol.equalsIgnoreCase(RolSistema.SUSCRIPTOR_AUTO.getCdsisrol())
									||cdsisrol.equalsIgnoreCase(RolSistema.EMISOR_SUSCRI_DANIOS.getCdsisrol())
									||cdsisrol.equalsIgnoreCase(RolSistema.JEFE_SUSCRI_DANIOS.getCdsisrol())
									||cdsisrol.equalsIgnoreCase(RolSistema.GERENTE_SUSCRI_DANIOS.getCdsisrol())
									||cdsisrol.equalsIgnoreCase(RolSistema.SUBDIRECTOR_SUSCRI_DANIOS.getCdsisrol())
									||cdsisrol.equalsIgnoreCase(RolSistema.TECNICO_SUSCRI_DANIOS.getCdsisrol())
									)
							{
								List<ComponenteVO>componenteSustitutoListaAux=pantallasManager.obtenerComponentes(
										TipoTramite.POLIZA_NUEVA.getCdtiptra() , null         , "|"+cdramo+"|"
										,cdtipsit                              ,  "W"         , cdsisrol
										,"COTIZACION_CUSTOM"                   , "SUSTITUTOS" , "32");
								temp.remove(tatriIte);
								componenteSustitutoListaAux.get(0).setSwsuscri("N");
								temp.add(componenteSustitutoListaAux.get(0));
								
								if(flujo!=null) //cuando entra por mesa de control ya debe haber agente
								{
									ComponenteVO agente = componenteSustitutoListaAux.get(0);
									
									if(StringUtils.isNotBlank(agente.getCatalogo()))
									{
										logger.debug("Se encontro y modifico el campo agente por venir de flujo: {}",agente);
										agente.setCatalogo("CATALOGO_CERRADO");
										agente.setQueryParam(null);
										agente.setParamName1(Utils.join("'params._",cdagente,"'"));
										agente.setParamValue1(Utils.join("'",cotizacionManager.cargarNombreAgenteTramite(smap1.get("ntramite")),"'"));
										agente.setParamName2(null);
										agente.setParamValue2(null);
										agente.setParamName3(null);
										agente.setParamValue3(null);
										agente.setParamName4(null);
										agente.setParamValue4(null);
										agente.setParamName5(null);
										agente.setParamValue5(null);
									}
								}
							}
						}
					}
	        		
					//[parche] para ramo 16 y sucriptor auto, sustituir campo Numero de serie:
					if(cdramo.equals(Ramo.AUTOS_FRONTERIZOS.getCdramo())) {
						if(tatriIte.getNameCdatribu().equalsIgnoreCase("3")) {
							List<ComponenteVO> componenteSustitutoListaAux = pantallasManager.obtenerComponentes(
									TipoTramite.POLIZA_NUEVA.getCdtiptra() , null         , "|"+cdramo+"|"
									,cdtipsit                              ,  "W"         , cdsisrol
									,"COTIZACION_CUSTOM"                   , "SUSTITUTOS" , "3");
							// Si se encuentra el componente lo reemplazamos:
							if(componenteSustitutoListaAux != null && componenteSustitutoListaAux.size() > 0) {
								temp.remove(tatriIte);
								componenteSustitutoListaAux.get(0).setSwsuscri("N");
								temp.add(componenteSustitutoListaAux.get(0));
							}
						}
					}
				}
		        tatrisit=temp;
		        
		        //segunda ronda para separar los individuales de los agrupados
				for(ComponenteVO tatriIte:tatrisit)
				{
					if(tatriIte.getSwsuscri().equalsIgnoreCase("S"))//S=individual
					{
						tatriIte.setColumna(Constantes.SI);
						camposIndividuales.add(tatriIte);
					}
					else
					{
						camposAgrupados.add(tatriIte);
					}
					
					//[parche] para SL y SN
					if(cdtipsit.equalsIgnoreCase("SL")||cdtipsit.equalsIgnoreCase("SN"))
					{
						if(tatriIte.getCatalogo()!=null&&
								(tatriIte.getCatalogo().equalsIgnoreCase("2CODPOS")
										||tatriIte.getCatalogo().equalsIgnoreCase("2CODPOSN")))//codigo postal
						{
							tatriIte.setCatalogo("");
						}
					}
				}
	
				gc.generaComponentes(camposAgrupados,true,true,true,false,false,false);
				imap.put("fieldsAgrupados" , gc.getFields());
				imap.put("camposAgrupados" , gc.getItems());
				
				if(camposIndividuales.size()>0)
				{
					gc.generaComponentes(camposIndividuales,true,true,true,true,true,false);
					imap.put("camposIndividuales" , gc.getColumns());
					imap.put("fieldsIndividuales" , gc.getFields());
					imap.put("itemsIndividuales"  , gc.getItems());
				}
				else
				{
					imap.put("camposIndividuales" , null);
					imap.put("fieldsIndividuales" , null);
					imap.put("itemsIndividuales"  , null);
				}
				
				logger.debug("camposAgrupados: "+camposAgrupados);
				logger.debug("camposIndividuales: "+camposIndividuales);
	        }
	        catch(Exception ex)
	        {
	        	long timestamp  = System.currentTimeMillis();
	        	exito           = false;
	        	respuesta       = "Error al obtener componentes #"+timestamp;
	        	respuestaOculta = ex.getMessage();
	        	logger.error(respuesta,ex);
	        }
        }
        
        //obtener validaciones de situaciones
        if(exito)
        {
        	try
        	{
        		List<ComponenteVO>validaciones=pantallasManager.obtenerComponentes(
						null, null, cdramo, cdtipsit, null, null, "VALIDACIONES_COTIZA", gc.isEsMovil()?"MOVIL":"DESKTOP", null);
				if(validaciones.size()>0)
				{
					gc.generaComponentes(validaciones, true, false, false, false, false, true);
					imap.put("validacionCustomButton" , gc.getButtons());
				}
				else
				{
					throw new ApplicationException(
							new StringBuilder()
							.append("No se han definido las validaciones en VALIDACIONES_COTIZA>")
							.append(gc.isEsMovil()?"MOVIL":"DESKTOP")
							.toString()
							);
				}
        	}
        	catch(Exception ex)
        	{
        		long timestamp  = System.currentTimeMillis();
        		respuesta       = "Error al cargar las validaciones para el producto #"+timestamp;
        		respuestaOculta = ex.getMessage();
        		logger.error(respuesta,ex);
        		
        		this.addActionError("No se han definido las validaciones para el producto #"+timestamp);
        		imap.put("validacionCustomButton" , null);
        	}
        }
        
        //obtener atributos extras de situacion
        if(exito)
        {
        	try
        	{
        		List<ComponenteVO>modeloExtra = pantallasManager.obtenerComponentes(
						null, null, cdramo, cdtipsit, null, null, "VALIDACIONES_COTIZA", "MODELO", null);
				gc.generaComponentes(modeloExtra, true, true, true, true, true, false);
				if(modeloExtra.size()>0)
				{
					imap.put("modeloExtraFields"  , gc.getFields());
					imap.put("modeloExtraColumns" , gc.getColumns());
					imap.put("modeloExtraItems"   , gc.getItems());
				}
				else
				{
					imap.put("modeloExtraFields"  , null);
					imap.put("modeloExtraColumns" , null);
					imap.put("modeloExtraItems"   , null);
				}
	        }
	        catch(Exception ex)
	        {
	        	long timestamp  = System.currentTimeMillis();
	        	exito           = false;
	        	respuesta       = "Error al obtener los atributos extras de la situaci\u00f3n #"+timestamp;
	        	respuestaOculta = ex.getMessage();
	        	logger.error(respuesta,ex);
	        }
        }
        
        //atributos derechos para auto
		if(exito&&situacion.equals("AUTO"))
		{
			try
			{
				List<ComponenteVO>cdatribusDerechos=pantallasManager.obtenerComponentes(
						null, null, cdramo, cdtipsit, null, null, "COTIZACION_CUSTOM", "CDATRIBU_DERECHO", null);
				if(cdatribusDerechos.size()>0)
				{
					String cdatribusConcatenados="";
					for(int i=0;i<cdatribusDerechos.size();i++)
					{
						cdatribusConcatenados=cdatribusConcatenados+cdatribusDerechos.get(i).getNameCdatribu();
						if(i<cdatribusDerechos.size()-1)
						{
							cdatribusConcatenados=cdatribusConcatenados+",";
						}
					}
					smap1.put("CDATRIBU_DERECHO",cdatribusConcatenados);
				}
				else
				{
					throw new ApplicationException("No se han definido atributos en COTIZACION_CUSTOM>CDATRIBU_DERECHO");
				}
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				respuesta       = "Error al cargar los CDATRIBU_DERECHO #"+timestamp;
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
				
				this.addActionError("No se han definido los atributos de coberturas para el producto #"+timestamp);
				smap1.put("CDATRIBU_DERECHO",null);
			}
		}
		
		if(exito)
		{
			try
			{
				smap1.put("customCode" , consultasManager.recuperarCodigoCustom("0", cdsisrol));
			}
			catch(Exception ex)
			{
				smap1.put("customCode" , "/* error */");
				logger.error("Error sin impacto funcional",ex);
			}
		}
		
		if (exito) {
		    try {
		        List<ComponenteVO> tatripol = pantallasManager.obtenerComponentes(
		                null, //cdtiptra
		                null, //cdunieco
		                "|" + cdramo + "|",
		                "|" + cdtipsit + "|",
		                null, //estado
		                cdsisrol,
		                "COTIZACION_CUSTOM",
		                "TATRI_POL",
		                null //orden
		                );
		        if (tatripol.size() == 0) {
		            throw new ApplicationException("WARNING no hay TATRI_POL");
		        }
		        
		        gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
		        gc.setEsMovil(session != null && session.containsKey("ES_MOVIL") && ((Boolean)session.get("ES_MOVIL")) == true);
		        if (!gc.isEsMovil() && smap1.containsKey("movil")) {
		            gc.setEsMovil(true);
		        }
		        
		        gc.generaComponentes(tatripol, true, false, true, false, false, false);
		        imap.put("tatripol", gc.getItems());
		    } catch (Exception ex) {
		        logger.error("Error al recuperar tatripol (COTIZACION_CUSTOM>TATRI_POL)", ex);
		    }
		}
		
		//respuesta
		String respuesta = null;
		if(exito)
		{
			if(gc.isEsMovil())
			{
				respuesta = "success_mobile";
			}
			else
			{
				respuesta = SUCCESS;
			}
		}
		else
		{
			if(gc.isEsMovil())
			{
				respuesta = "error_mobile";
			}
			else
			{
				respuesta = ERROR;
			}
		}
		
		logger.debug(""
				+ "\nrespuesta: "+respuesta
				+ "\n###### pantallaCotizacion ######"
				+ "\n################################"
				);
		
		return respuesta;
	}
	
	public String webServiceNada()
	{
		logger.debug(""
				+ "\n############################"
				+ "\n###### webServiceNada ######"
				);
		logger.debug("smap1: "+smap1);
		
		String  vim                  = null;
		success                      = true;
		VehicleValue_Struc datosAuto = null;
		String cdramo                = null;
		String cdtipsit              = null;
		String tipoVehiculo          = null;
		String codigoPostal          = null;
		
		//revisar numero de serie
		if(success)
		{
			
			success = smap1!=null&&StringUtils.isNotBlank(vim=smap1.get("vim"))
					&&StringUtils.isNotBlank(cdramo=smap1.get("cdramo"))
					&&StringUtils.isNotBlank(cdtipsit=smap1.get("cdtipsit"))
					&&StringUtils.isNotBlank(tipoVehiculo=smap1.get("tipoveh"))
					&&StringUtils.isNotBlank(codigoPostal=smap1.get("codpos"))
					;
			logger.debug("Vils tipo de vehiculo: "+ tipoVehiculo);
			if(!success)
			{
				error="No se recibi\u00f3 el n\u00famero de serie";
				logger.error(error);
			}
		}
		
		//obtener factor convenido
		if(success)
		{
			try
			{
				LinkedHashMap<String,Object>params=new LinkedHashMap<String,Object>();
				params.put("param1",cdramo);
				params.put("param2",cdtipsit);
				Map<String,String>factor=storedProceduresManager.procedureMapCall(
						ObjetoBD.OBTIENE_FACTOR_CONVENIDO.getNombre(), params, null);
				smap1.putAll(factor);
			}
			catch(Exception ex)
			{
				logger.warn("error SIN IMPACTO FUNCIONAL al obtener factor convenido o el factor no se encuentra",ex);
				smap1.put("FACTOR_MIN","0");
				smap1.put("FACTOR_MAX","0");
			}
		}
		
		//llamar web service
		if(success)
		{
			datosAuto = nadaService.obtieneDatosAutomovilNADA(vim);
			success   = datosAuto!=null;
			if(!success)
			{
				error="No se encontr\u00f3 informaci\u00f3n para el n\u00famero de serie";
				logger.error(error);
				/*parche
				datosAuto = new VehicleValue_Struc();
				datosAuto.setVehicleYear(1);
				datosAuto.setSeriesDescr("a");
				datosAuto.setBodyDescr("b");
				datosAuto.setAvgTradeIn(BigDecimal.valueOf(123d));
				datosAuto.setMakeDescr("c");
				success=true;*/
			}
		}
		
		int tipoValorVehiculo = 3;
		
		if(success){
			
			try{
				tipoValorVehiculo = cotizacionManager.obtieneTipoValorAutomovil(codigoPostal,tipoVehiculo);
				logger.debug("Tipo de Valor de auto a tomar: "+ tipoValorVehiculo);
			}catch(Exception ex){
				logger.error("Error al consultar el tipo de valor del automovil segun CP y tipo auto",ex);
				success = false;
			}
		}
		
		//datos regresados
		if(success)
		{
			smap1.put("AUTO_ANIO"        , datosAuto.getVehicleYear()+"");
			smap1.put("AUTO_DESCRIPCION" , datosAuto.getSeriesDescr()+" "+datosAuto.getBodyDescr());
			
			logger.debug("AvgTradeIn:" + datosAuto.getAvgTradeIn().toString());
			logger.debug("TradeIn:"    + datosAuto.getAvgTradeIn().toString());
			
			String precioAuto = null;
			if(tipoValorVehiculo == 1 || tipoValorVehiculo == 0){
				precioAuto = datosAuto.getAvgTradeIn().toString();
			} else if(tipoValorVehiculo == 2){
				precioAuto = datosAuto.getTradeIn().toString();				
			}
				
			smap1.put("AUTO_PRECIO", precioAuto);
			smap1.put("AUTO_MARCA", datosAuto.getMakeDescr());
		}
		
		logger.debug(""
				+ "\n###### webServiceNada ######"
				+ "\n############################"
				);
		return SUCCESS;
	}

	public String webServiceNadaEndosos()
	{
		logger.debug(""
				+ "\n##################################"
				+ "\n###### webServiceNadaEdosos ######"
				);
		logger.debug("smap1: "+smap1);
		
		String  vim                  = null;
		success                      = true;
		VehicleValue_Struc datosAuto = null;
		String cdunieco                = null;
		String cdramo                = null;
		String nmpoliza                = null;
		String cdtipsit              = null;
		String tipoVehiculo          = null;
		String nmsituac              = null;
		String nmsuplem              = null;
		
		//revisar numero de serie
		if(success)
		{
			
			success = smap1!=null&&StringUtils.isNotBlank(vim=smap1.get("vim"))
					&&StringUtils.isNotBlank(cdunieco=smap1.get("cdunieco"))
					&&StringUtils.isNotBlank(cdramo=smap1.get("cdramo"))
					&&StringUtils.isNotBlank(nmpoliza=smap1.get("nmpoliza"))
					&&StringUtils.isNotBlank(cdtipsit=smap1.get("cdtipsit"))
					&&StringUtils.isNotBlank(tipoVehiculo=smap1.get("tipoveh"))
					&&StringUtils.isNotBlank(nmsituac=smap1.get("nmsituac"))
					&&StringUtils.isNotBlank(nmsuplem=smap1.get("nmsuplem"))
					;
			logger.debug("Vils tipo de vehiculo: "+ tipoVehiculo);
			if(!success)
			{
				error="No se recibi\u00f3 el n\u00famero de serie";
				logger.error(error);
			}
		}
		
		//obtener factor convenido
		if(success)
		{
			try
			{
				LinkedHashMap<String,Object>params=new LinkedHashMap<String,Object>();
				params.put("param1",cdramo);
				params.put("param2",cdtipsit);
				Map<String,String>factor=storedProceduresManager.procedureMapCall(
						ObjetoBD.OBTIENE_FACTOR_CONVENIDO.getNombre(), params, null);
				smap1.putAll(factor);
			}
			catch(Exception ex)
			{
				logger.warn("error SIN IMPACTO FUNCIONAL al obtener factor convenido o el factor no se encuentra",ex);
				smap1.put("FACTOR_MIN","0");
				smap1.put("FACTOR_MAX","0");
			}
		}
		
		if(success)
		{
			try
			{
				ResponseTipoCambio rtc=tipoCambioService.obtieneTipoCambioDolarGS(2);
        		if(rtc!=null&&rtc.getTipoCambio()!=null&&rtc.getTipoCambio().getVenCam()!=null)
        		{
        			smap1.put("PRECIO_DOLAR", rtc.getTipoCambio().getVenCam().doubleValue()+"");
        		}
			}
			catch(Exception ex)
			{
				logger.warn("error SIN IMPACTO FUNCIONAL al obtener factor convenido o el factor no se encuentra",ex);
				smap1.put("FACTOR_MIN","0");
				smap1.put("FACTOR_MAX","0");
			}
		}
		
		//llamar web service
		if(success)
		{
			datosAuto = nadaService.obtieneDatosAutomovilNADA(vim);
			success   = datosAuto!=null;
			if(!success)
			{
				error="No se encontr\u00f3 informaci\u00f3n para el n\u00famero de serie";
				logger.error(error);
				/*parche
				datosAuto = new VehicleValue_Struc();
				datosAuto.setVehicleYear(1);
				datosAuto.setSeriesDescr("a");
				datosAuto.setBodyDescr("b");
				datosAuto.setAvgTradeIn(BigDecimal.valueOf(123d));
				datosAuto.setMakeDescr("c");
				success=true;*/
			}
		}
		
		int tipoValorVehiculo = 3;
		String codigoPostal =  null;
		
		if(success){
			
			try{
				codigoPostal = cotizacionManager.obtieneCodigoPostalAutomovil(cdunieco,cdramo,"M",nmpoliza,nmsituac,nmsuplem);
				logger.debug("Tipo de Valor de auto a tomar: "+ tipoValorVehiculo);
			}catch(Exception ex){
				logger.error("Error al consultar el tipo de valor del automovil segun CP y tipo auto",ex);
				success = false;
			}
		}
		
		if(success){
			
			try{
				tipoValorVehiculo = cotizacionManager.obtieneTipoValorAutomovil(codigoPostal,tipoVehiculo);
				logger.debug("Tipo de Valor de auto a tomar: "+ tipoValorVehiculo);
			}catch(Exception ex){
				logger.error("Error al consultar el tipo de valor del automovil segun CP y tipo auto",ex);
				success = false;
			}
		}
		
		//datos regresados
		if(success)
		{
			smap1.put("AUTO_ANIO"        , datosAuto.getVehicleYear()+"");
			smap1.put("AUTO_DESCRIPCION" , datosAuto.getSeriesDescr()+" "+datosAuto.getBodyDescr());
			
			logger.debug("AvgTradeIn:" + datosAuto.getAvgTradeIn().toString());
			logger.debug("TradeIn:"    + datosAuto.getAvgTradeIn().toString());
			
			String precioAuto = null;
			if(tipoValorVehiculo == 1 || tipoValorVehiculo == 0){
				precioAuto = datosAuto.getAvgTradeIn().toString();
			} else if(tipoValorVehiculo == 2){
				precioAuto = datosAuto.getTradeIn().toString();				
			}
			
			smap1.put("AUTO_PRECIO", precioAuto);
			smap1.put("AUTO_MARCA", datosAuto.getMakeDescr());
		}
		
		logger.debug(""
				+ "\n###### webServiceNada ######"
				+ "\n############################"
				);
		return SUCCESS;
	}
	
	public String emitirColectivo()
	{
		logger.debug(Utils.log(""
				,"\n#############################"
				,"\n###### emitirColectivo ######"
				,"\n###### smap1 ", smap1
				));
		
		exito   = false;
		success = false;

		String cdperson = null;
		
		UserVO usuario  = (UserVO)session.get("USUARIO");
		String cdusuari = usuario.getUser();
		String cdsisrol = usuario.getRolActivo().getClave();
		String cdelemen = usuario.getEmpresa().getElementoId();
		String cdunieco = smap1.get("cdunieco");
		String cdramo   = smap1.get("cdramo");
		String cdtipsit = smap1.get("cdtipsit");
		String estado   = smap1.get("estado");
		String nmpoliza = smap1.get("nmpoliza");
		String cdperpag = smap1.get("cdperpag");
		String ntramite = smap1.get("ntramite");
		String fechaIni = smap1.get("feini");
		String fechaFin = smap1.get("fefin");
		String caseIdRstn = smap1.get("caseIdRstn");
		
		try
		{
			String paso = null;
			try
			{
			    if (StringUtils.isNotBlank(ntramite) && StringUtils.isNotBlank(caseIdRstn)) {
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
			    
				//---------------------------------
				paso = "Obtener datos del usuario";
				logger.debug(Utils.log("","paso=",paso));
				
				DatosUsuario datUs = kernelManager.obtenerDatosUsuario(cdusuari,cdtipsit);
				cdperson           = datUs.getCdperson();
				
				//----------------------------
				paso = "Revisando domicilios";
				logger.debug(Utils.log("","paso=",paso));
				
				List<Map<String,String>>lisUsuSinDir=null;
				try
				{
					Map<String,String>paramValidar=new LinkedHashMap<String,String>(0);
					paramValidar.put("pv_cdunieco" , cdunieco);
					paramValidar.put("pv_cdramo"   , cdramo);
					paramValidar.put("pv_estado"   , "W");
					paramValidar.put("pv_nmpoliza" , nmpoliza);
					lisUsuSinDir=kernelManager.PValInfoPersonas(paramValidar);
				}
				catch(Exception ex)
				{
					logger.error(Utils.log("","Error sin impacto funcional al validar domicilios: "),ex);
					lisUsuSinDir=null;
				}
				
				if(lisUsuSinDir!=null&&lisUsuSinDir.size()>0)
				{
					if(Ramo.SERVICIO_PUBLICO.getCdramo().equals(cdramo) || Ramo.AUTOS_FRONTERIZOS.getCdramo().equals(cdramo))
					{
						respuesta = "Favor de verificar y guardar correctamente la direcci\u00f3n y datos del contratante.";
					}
					else
					{
						respuesta = "Favor de verificar la direcci\u00f3n de los siguientes asegurados:<br/>";
						// f a v o r
						//0 1 2 3 4 5
						if(lisUsuSinDir.get(0).get("nombre").substring(0,5).equalsIgnoreCase("favor"))
						{
							respuesta=lisUsuSinDir.get(0).get("nombre");
						}
						else
						{
							for(int i=0;i<lisUsuSinDir.size();i++)
							{
								respuesta+=lisUsuSinDir.get(i).get("nombre")+"<br/>";
							}					
						}
					}
					
					logger.debug(Utils.log("","Se va a terminar el proceso porque faltan direcciones"));
					throw new ApplicationException(respuesta);
				}
				
				//------------------------------------
				paso = "Insertando maestro historico";
				logger.debug(Utils.log("","paso=",paso));
				
				Map<String,Object>map2=new LinkedHashMap<String,Object>(0);
	            map2.put("pv_cdunieco_i"  , cdunieco);
	            map2.put("pv_cdramo_i"    , cdramo);
	            map2.put("pv_estado_i"    , estado);
	            map2.put("pv_nmpoliza_i"  , nmpoliza);
	            map2.put("pv_nmsuplem_i"  , "0");
	            map2.put("pv_feINival_i"  , renderFechas.parse(fechaIni));
	            map2.put("pv_hhinival_i"  , renderHora.format(new Date()));
	            map2.put("pv_fefINval_i"  , renderFechas.parse(fechaFin));
	            map2.put("pv_hhfinval_i"  , renderHora.format(new Date()));
	            map2.put("pv_swanula_i"   , null);
	            map2.put("pv_nsuplogi_i"  , "0");
	            map2.put("pv_nsupusua_i"  , null);
	            map2.put("pv_nsupsess_i"  , null);
	            map2.put("pv_fesessio_i"  , null);
	            map2.put("pv_swconfir_i"  , null);
	            map2.put("pv_nmrenova_i"  , null);
	            map2.put("pv_nsuplori_i"  , null);
	            map2.put("pv_cdorddoc_i"  , null);
	            map2.put("pv_swpolfro_i"  , null);
	            map2.put("pv_pocofron_i"  , null);
	            map2.put("pv_swpoldec_i"  , null);
	            map2.put("pv_tippodec_i"  , null);
	            map2.put("pv_accion_i"    , "I");
	            kernelManager.insertaMaestroHistoricoPoliza(map2);
				
	            //---------------------------------------------
	            paso = "Registrando descripcion de suplemento";
	            logger.debug(Utils.log("","paso=",paso));
	            
	            cotizacionManager.movimientoTdescsup(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,"0"//nsuplogi
						,TipoTramite.POLIZA_NUEVA.getCdtiptra()//cdtipsup
						,renderFechas.parse(fechaIni)
						,nmpoliza//nmsolici
						,new Date()//fesolici
						,new Date()//ferefere
						,null//cdseqpol
						,cdusuari
						,null//nusuasus
						,null//nlogisus
						,cdperson
						,Constantes.INSERT_MODE
						);
	            
	            //-----------------
	            paso = "Emitiendo";
	            logger.debug(Utils.log("","paso=",paso));
	            
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
				paramEmi.put("pv_fecha"     , new Date());
				paramEmi.put("pv_ntramite"  , ntramite);
				WrapperResultados wr=kernelManager.emitir(paramEmi);
				
				String nmpolizaEmi = (String)wr.getItemMap().get("nmpoliza")
				       ,nmpoliexEmi = (String)wr.getItemMap().get("nmpoliex")
				       ,nmsuplemEmi = (String)wr.getItemMap().get("nmsuplem");
				
				smap1.put("nmpolizaEmi" , nmpolizaEmi);
				smap1.put("nmpoliexEmi" , nmpoliexEmi);
				smap1.put("nmsuplemEmi" , nmsuplemEmi);
				
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
	            	    ,nmpolizaEmi
	            	    ,nmpoliza
	            	    ,null
	            	    ,null
	            	    ,null);
	            }
	            catch(Exception ex)
	            {
	            	logger.error(Utils.log("","Error al grabar evento, sin impacto"),ex);
	            }
				
				paso = "Generando documentos";
				logger.debug(Utils.log("","paso=",paso));
				
				documentosManager.generarDocumentosParametrizadosAsync(
						cdunieco
	            		,cdramo
	            		,"M"
	            		,nmpolizaEmi
	            		,"0"
	            		,nmsuplemEmi
	            		,DocumentosManager.PROCESO_EMISION //proceso
	            		,ntramite
	            		,nmpoliza //nmsolici
	            		);
				
				// JTEZVA 2016 09 09 se mandan avisos cuando se emite si estan configurados
				try {
					flujoMesaControlManager.mandarCorreosStatusTramite(ntramite, cdsisrol, false);
				} catch (Exception ex) {
					logger.error("Error al enviar correos de emision", ex);
				}
	            
				//-------------------------------------
				paso = "Insertando detalle de emision";
				logger.debug(Utils.log("","paso=",paso));
				
				mesaControlManager.movimientoDetalleTramite(
						ntramite
						,new Date()
						,null
						,"El tr\u00e1mite se emiti\u00f3"
						,cdusuari
						,null
						,cdsisrol
						,"S"
						,EstatusTramite.CONFIRMADO.getCodigo()
						,true
						);
				
				//---------------------------------------------
				paso = "Ejecutando WS para recibos colectivos";
				logger.debug(Utils.log("","paso=",paso));
				
				ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo,
						"M", nmpolizaEmi, 
						nmsuplemEmi, this.rutaDocumentosPoliza+"/"+ntramite,
						cdunieco, nmpoliza,ntramite, 
						true, TipoEndoso.EMISION_POLIZA.getCdTipSup().toString(),
						usuario);
				
				respuesta = Utils.join("Se ha emitido la p\u00f3liza ",nmpolizaEmi," [",nmpoliexEmi,"]");
				logger.debug(Utils.log("","respuesta final=",respuesta));
				
				//termina correctamente
				exito   = true;
				success = true;
			}
			catch(Exception ex)
			{
				Utils.generaExcepcion(ex, paso);
			}
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(""
				,"\n###### emitirColectivo ######"
				,"\n#############################"
				));
		return SUCCESS;
	}

	public String pantallaCotizacionDemo() {
		this.session=ActionContext.getContext().getSession();
		logger.debug("\n"
				+ "\n####################################"
				+ "\n####################################"
				+ "\n###### pantallaCotizacionDemo ######"
				+ "\n######                        ######"
				);
		logger.debug("smap1: "+smap1);
		logger.debug("session: "+session);
		
		GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
		gc.setEsMovil(session!=null&&session.containsKey("ES_MOVIL")&&((Boolean)session.get("ES_MOVIL"))==true);
		if(!gc.isEsMovil() && smap1.containsKey("movil"))
		{
			gc.setEsMovil(true);
		}
		
		UserVO usuario    = (UserVO) session.get("USUARIO");
		String cdtipsit   = smap1.get("cdtipsit");
		String cdpantalla = smap1.get("cdpantalla");
		
		String ntramite;
		String cdunieco=null;
		String cdramo=null;
		
		smap1.put("user",usuario.getUser());
		
		/////////////////////////////////////////////////////////
		////// poner valores a ntramite, cdunieco y cdramo //////
        if(smap1.get("ntramite")!=null)
        //cuando viene ntramite tambien vienen cdunieco y cdramo
        {
        	ntramite = smap1.get("ntramite");
        	cdunieco = smap1.get("cdunieco");
        	cdramo   = smap1.get("cdramo");
        }
        else
        //cuando no hay ntramite es porque esta cotizando un agente por fuera,
        //y se obtiene cdunieco y cdramo por medio de ese agente
        {
        	try
        	{
        		DatosUsuario datUsu=kernelManager.obtenerDatosUsuario(usuario.getUser(),cdtipsit);//cdramo
        		ntramite="";
        		cdunieco=datUsu.getCdunieco();
        		if(StringUtils.isBlank(smap1.get("cdramo")))
        		{
        			cdramo=datUsu.getCdramo();
        		}
        		else
        		{
        			cdramo=smap1.get("cdramo");
        		}
        		smap1.put("ntramite","");
        		smap1.put("cdunieco",cdunieco);
        		smap1.put("cdramo",cdramo);
        	}
        	catch(Exception ex)
        	{
        		logger.error("error al obtener los datos del agente",ex);
        	}
        }
		////// poner valores a ntramite, cdunieco y cdramo //////
        /////////////////////////////////////////////////////////
        
        ////////////////////////////////////////
        ////// obtener campos de tatrisit //////
        gc.setCdtipsit(cdtipsit);
        
        List<ComponenteVO>camposAgrupados    = new ArrayList<ComponenteVO>(0);
        List<ComponenteVO>camposIndividuales = new ArrayList<ComponenteVO>(0);
        
        imap = new HashMap<String,Item>();
        
		params =  new HashMap<String,String>();
		params.put("PV_CDPANTALLA_I", cdpantalla);
		params.put("PV_CDRAMO_I", cdramo);
		params.put("PV_CDTIPSIT_I", cdtipsit);
		
		Map<String,String> result = null;
		try {
			result = pantallasManager.obtienePantalla(params);
		} catch (Exception e) {
			logger.error("Error al obtener codigo de pantalla para pantalla: " + params, e);
		}
		
		smap1.put("variablesGeneradas", result.get("COMPONENTES"));
		smap1.put("panelGenerado", result.get("DATOS"));
        try
        {
        	List<ComponenteVO>tatrisit=kernelManager.obtenerTatrisit(cdtipsit,usuario.getUser());
        	
	        List<ComponenteVO>temp=new ArrayList<ComponenteVO>();
	        for(ComponenteVO tatriIte:tatrisit)
			{
	        	if(tatriIte.getValue()==null&&tatriIte.getDefaultValue()==null)
	        	{
	        		tatriIte.setComboVacio(true);
	        	}
	        	if(tatriIte.getSwpresen().equalsIgnoreCase("S"))
	        	{
	        		temp.add(tatriIte);
	        	}
	        	else
	        	{
	        		if(cdtipsit.equalsIgnoreCase(TipoSituacion.AUTOS_FRONTERIZOS.getCdtipsit())||
	        				cdtipsit.equalsIgnoreCase(TipoSituacion.AUTOS_PICK_UP.getCdtipsit()))
	        		{
	        			if(tatriIte.getNameCdatribu().equalsIgnoreCase("26"))
	        			{
	        				tatriIte.setOculto(true);
	        				temp.add(tatriIte);
	        			}
	        		}
	        	}
	        	if(
	        			(cdtipsit.equalsIgnoreCase(TipoSituacion.AUTOS_FRONTERIZOS.getCdtipsit())
	        			||cdtipsit.equalsIgnoreCase(TipoSituacion.AUTOS_PICK_UP.getCdtipsit())
	        			)
	        			&&tatriIte.getNameCdatribu().equalsIgnoreCase("24")
	        			)
	        	{
	        		ResponseTipoCambio rtc=tipoCambioService.obtieneTipoCambioDolarGS(2);
	        		if(rtc!=null&&rtc.getTipoCambio()!=null&&rtc.getTipoCambio().getVenCam()!=null)
	        		{
	        			tatriIte.setOculto(true);
	        			tatriIte.setValue(rtc.getTipoCambio().getVenCam().doubleValue()+"");
	        		}
	        	}
			}
	        tatrisit=temp;
	        
			for(ComponenteVO tatriIte:tatrisit)
			{
				////////////////////
				////// custom //////
				if(cdtipsit.equalsIgnoreCase("SL")||cdtipsit.equalsIgnoreCase("SN"))
				{
					if(tatriIte.getCatalogo()!=null&&
							(tatriIte.getCatalogo().equalsIgnoreCase("2CODPOS")
									||tatriIte.getCatalogo().equalsIgnoreCase("2CODPOSN")))//codigo postal
					{
						tatriIte.setCatalogo("");
					}
				}
				////// custom //////
				////////////////////
					
				if(tatriIte.getSwsuscri().equalsIgnoreCase("S"))//S=individual
				{
					tatriIte.setColumna(Constantes.SI);
					camposIndividuales.add(tatriIte);
				}
				else
				{
					camposAgrupados.add(tatriIte);
				}
			}

			gc.generaParcial(camposAgrupados);
			//imap.put("camposAgrupados" , gc.getItems());
			imap.put("fieldsAgrupados" , gc.getFields());
        	
			if(camposIndividuales.size()>0)
			{
				gc.generaParcialConEditor(camposIndividuales);
				imap.put("itemsIndividuales"  , gc.getItems());
				imap.put("camposIndividuales" , gc.getColumns());
				imap.put("fieldsIndividuales" , gc.getFields());
			}
			else
			{
				imap.put("itemsIndividuales"  , null);
				imap.put("camposIndividuales" , null);
				imap.put("fieldsIndividuales" , null);
			}
			
			List<ComponenteVO>validaciones=pantallasManager.obtenerComponentes(
					null, null, cdramo, cdtipsit, null, null, "VALIDACIONES_COTIZA", gc.isEsMovil()?"MOVIL":"DESKTOP", null);
			if(validaciones.size()>0)
			{
				gc.generaComponentes(validaciones, true, false, false, false, false, true);
				imap.put("validacionCustomButton" , gc.getButtons());
			}
			else
			{
				imap.put("validacionCustomButton" , null);
			}
			
			List<ComponenteVO>modeloExtra = pantallasManager.obtenerComponentes(
					null, null, cdramo, cdtipsit, null, null, "VALIDACIONES_COTIZA", "MODELO", null);
			gc.generaComponentes(modeloExtra, true, true, true, true, true, false);
			if(modeloExtra.size()>0)
			{
				imap.put("modeloExtraFields"  , gc.getFields());
				imap.put("modeloExtraColumns" , gc.getColumns());
				imap.put("modeloExtraItems"   , gc.getItems());
			}
			else
			{
				imap.put("modeloExtraFields"  , null);
				imap.put("modeloExtraColumns" , null);
				imap.put("modeloExtraItems"   , null);
			}
        }
        catch(Exception ex)
        {
        	logger.error("error al obtener los campos de cotizacion",ex);
        }
        
		logger.debug("camposAgrupados: "+camposAgrupados);
		logger.debug("camposIndividuales: "+camposIndividuales);
        ////// obtener campos de tatrisit //////
        ////////////////////////////////////////
        
		logger.debug("\n"
				+ "\n######                        ######"
				+ "\n###### pantallaCotizacionDemo ######"
				+ "\n####################################"
				+ "\n####################################"
				);
		return gc.isEsMovil() ? "success_mobile" : SUCCESS;
	}
	/*/////////////////////////////*/
	////// cotizacion dinamica //////
	/////////////////////////////////

	/////////////////////
	////// cotizar //////
	/*/////////////////*/
	public String cotizar()
	{
		logger.debug(Utils.log(
				 "\n#####################"
				,"\n###### cotizar ######"
				,"\n###### smap1=",smap1
				));
		try
		{
			logger.debug("Validando datos para cotizar");

			UserVO usuario  = (UserVO)session.get("USUARIO");
			String cdusuari = usuario.getUser();
			String cdelemen = usuario.getEmpresa().getElementoId();
			
			Utils.validate(session                , "No hay sesion");
			Utils.validate(session.get("USUARIO") , "No hay usuario en la sesion");
			
			Utils.validate(smap1, "No se recibieron datos para cotizar");
			String cdunieco    = smap1.get("cdunieco");
			String cdramo      = smap1.get("cdramo");
			String cdtipsit    = smap1.get("cdtipsit");
			String cdagente    = smap1.get("cdagenteAux");
			String cdpersonCli = smap1.get("cdpersonCli");
			String nmorddomCli = smap1.get("nmorddomCli");
			String cdideperCli = smap1.get("cdideperCli");
			String fromSigs    = smap1.get("cargarXpoliza");
			String ntramite    = smap1.get("ntramite");
			String agentesec    = smap1.get("AGENTESEC");
			String porcensec    = smap1.get("PORCENSEC");
			
			ArrayList<String> planSigs= new ArrayList<String>();
			for(Map<String, String> inciso:slist1)
			{
			  planSigs.add(inciso.get("planSigs"));
			}
			Utils.validate(slist1, "No se recibieron datos de incisos");
			String nmsolici = slist1.get(0).get("nmpoliza");
			String feini    = slist1.get(0).get("feini");
			String fefin    = slist1.get(0).get("fefin");
			String fesolici = slist1.get(0).get("FESOLICI");
	
			boolean noTarificar = StringUtils.isNotBlank(smap1.get("notarificar"))&&smap1.get("notarificar").equals("si");
			boolean conIncisos  = StringUtils.isNotBlank(smap1.get("conincisos"))&&smap1.get("conincisos").equals("si");
			String modPrim = StringUtils.isNotBlank(smap1.get("modPrim"))?smap1.get("modPrim"):"";

			Map<String,String>tvalopol=new HashMap<String,String>();
			for(Entry<String,String>en:slist1.get(0).entrySet())
			{
				String key=en.getKey();
				if(key.length()>"aux.".length()
						&&key.substring(0,"aux.".length()).equals("aux.")
						)
				{
					tvalopol.put(key.substring("aux.".length()),en.getValue());
				}
			}
			
			if(fromSigs==null)fromSigs="N";
			Map<String,String>parame = flujoMesaControlManager.tramiteMC(ntramite, nmsolici, cdunieco, cdramo, cdtipsit);
			if(parame.get("Mensaje")!=null)
			{
				logger.debug(Utils.log(
						 "\n#######################"
						,"\n###### cotizar ######"
						,"\n",parame.get("Mensaje")
						,"\n######################"
						));
			}
			
	         ManagerRespuestaSlistSmapVO resp= new ManagerRespuestaSlistSmapVO();
	            if(modPrim.isEmpty())
	            {
	                resp=cotizacionManager.cotizar(
					cdunieco
					,cdramo
					,cdtipsit
					,cdusuari
					,cdelemen
					,nmsolici
					,feini
					,fefin
					,fesolici
					,cdpersonCli
					,nmorddomCli
					,cdideperCli
					,noTarificar
					,conIncisos
					,slist1
					,smap1.containsKey("movil")
					,tvalopol
					,cdagente
					,usuario	
					,fromSigs
					,parame.get("RENUNIEXT")
					,parame.get("RENRAMO")
					,parame.get("RENPOLIEX")
					,ntramite
					);
	            }
	            else
	            {
	                String mensajeModPrim = cotizacionManager.aplicaDescAutos(cdunieco, cdramo, nmsolici, modPrim, cdtipsit);
	                resp.setExito(true);
	                resp.setSmap(smap1);
	                if(!mensajeModPrim.isEmpty())
	                {
	                    resp.setRespuesta(mensajeModPrim);
	                    resp.setRespuestaOculta(mensajeModPrim);
	                }
	            }
			exito           = resp.isExito();
			respuesta       = resp.getRespuesta();
			respuestaOculta = resp.getRespuestaOculta();
			if(exito)
			{
			    ArrayList<String> paqYplan = new ArrayList<String>();
			    String columna="",fila="";//pauete
			    if(parame!=null && !parame.isEmpty() && parame.size()>0 && parame.get("RENPOLIEX")!=null)
			    {
			         boolean emergency= false;
			         
			         if(   !cdtipsit.equals(TipoSituacion.AUTOS_FRONTERIZOS.getCdtipsit())//AF
			             &&!cdtipsit.equals(TipoSituacion.AUTOS_PICK_UP.getCdtipsit())//PU
			             &&!cdtipsit.equals(TipoSituacion.PICK_UP_CARGA.getCdtipsit())//PC
			             &&!cdtipsit.equals(TipoSituacion.REMOLQUES_INDISTINTOS.getCdtipsit())//RQ
			             &&!cdtipsit.equals(TipoSituacion.TRACTOCAMIONES_ARMADOS.getCdtipsit())//TC
			             && slist1.get(0).get("parametros.pv_otvalor03").trim().equals("04")//SERVICIO EMERGENCIA
			           )
	                 {
			             emergency= true;
	                 }
			        
			        if(
			                !cdtipsit.equals(TipoSituacion.PICK_UP_CARGA.getCdtipsit()) 
			             && !cdtipsit.equals(TipoSituacion.CAMIONES_CARGA.getCdtipsit())
			             && !emergency
			          )
			        {
			            List<Map<String,String>> listaResultados=cotizacionManager.cargarResultadosCotizacion(
	                            cdusuari
	                            ,cdunieco
	                            ,cdramo
	                            ,"W"
	                            ,resp.getSmap().get("nmpoliza")==null?nmsolici:resp.getSmap().get("nmpoliza")
	                            ,cdelemen
	                            ,cdtipsit
	                            );
	                    logger.debug("listaResultados: "+listaResultados);
	                    
	                     paqYplan = cargarPoliza(parame.get("RENUNIEXT"), parame.get("RENRAMO"), parame.get("RENPOLIEX"), "paqYplan", cdtipsit, null);
	                    //pre sonbreado
	                     columna = paqYplan.get(1);//forma Pago
	                     fila= paqYplan.get(0);//paquete
	                     
	                       if(modPrim.isEmpty())
	                        {   
	                            String facultada = modificaPrimas(ntramite, listaResultados, Integer.parseInt(paqYplan.get(0).trim()), paqYplan, cdunieco, cdramo, resp.getSmap().get("nmpoliza")==null?nmsolici:resp.getSmap().get("nmpoliza") , cdtipsit,parame.get("RENUNIEXT"), parame.get("RENRAMO"), parame.get("RENPOLIEX"));
	                            resp.setRespuesta(facultada.substring(1,(facultada.length()-1)));
	                        }

	                    logger.debug(Utils.log(paqYplan));
	                    resp= cotizacionManager.cotizarContinuacion(cdusuari,cdunieco,cdramo,cdelemen,cdtipsit,resp.getSmap().get("nmpoliza")==null?nmsolici:resp.getSmap().get("nmpoliza"),smap1.containsKey("movil"));
			        }
			        if(paqYplan.isEmpty())
			        {  
			            paqYplan = cargarPoliza(parame.get("RENUNIEXT"), parame.get("RENRAMO"), parame.get("RENPOLIEX"), "paqYplan", cdtipsit, null);
			            if(paqYplan.get(0) != null && paqYplan.get(1) != null)
			            {
			                columna = paqYplan.get(1);//forma Pago
			                fila= paqYplan.get(0);//paquete
			            }
			        }//pre sonbreado
			      
	                if(columna.equals("1P"))                              {columna="PRESTIGIO";}
	                else if(columna.equals("1A") || columna.equals("2A")) {columna="CONFORT AMPLIA";}
	                else if(columna.equals("2L") || columna.equals("4L")) {columna="CONFORT LIMITADA";}
	                else if(columna.equals("5B") || columna.equals("3B")) {columna="CONFORT BASICA";}
	                else if(columna.equals("3A"))                         {columna="CONFORT AMPLIA S/ROBO";}
	                
    	            if(        fila.equals("1") 
    	                    || fila.equals("98") 
                            || fila.equals("11")
                            || fila.equals("12")){fila="Contado/Anual";} 
	                else if(   fila.equals("2")
	                        || fila.equals("5")
	                        || fila.equals("97")
	                        || fila.equals("63")){fila="Semestral";} 
	                else if(   fila.equals("3") 
	                        || fila.equals("6")
	                        || fila.equals("61")){fila="Trimestral";}
	                else if(  fila.equals("4") 
                            || fila.equals("7") ){fila="Mensual";} 
//	                else if(fila.equals("2"))  {fila="DXN Anual";}//  \r\n" + 
//	                else if(fila.equals("4"))  {fila="";}//  MENSUAL6\r\n" + 
//	                else if(fila.equals("5"))  {fila="";}//  ANUAL04\r\n" + 
//	                else if(fila.equals("6"))  {fila="";}//  DXN Quincenal\r\n" + 
//	                else if(fila.equals("7"))  {fila="";}//  DXN Catorcenal\r\n" + 
//	                else if(fila.equals("8"))  {fila="";}//  DXN Mensual\r\n" + 
//	                else if(fila.equals("9"))  {fila="";}//  DXN 16 Dias\r\n" + 
//	                else if(fila.equals("10")) {fila="";}//  DXN Semanal\r\n" + 
//	                else if(fila.equals("11")) {fila="";}//  DXN Decenal\r\n" + 
//	                else if(fila.equals("13")) {fila="";}//  Multianual\r\n" + 
//	                else if(fila.equals("14")) {fila="";}//  Mensual S/RF\r\n" + 
	                else if(fila.equals("62")) {fila="Semanal";}//  \r\n" + 
	                else if(fila.equals("64")) {fila="Contado";}//  \r\n" + 
//	                else if() {fila="";}//  SEMESTRAL A\r\n" + 
			    }
	            resp= cotizacionManager.cotizarContinuacion(cdusuari,cdunieco,cdramo,cdelemen,cdtipsit,resp.getSmap()==null?nmsolici:resp.getSmap().get("nmpoliza"),smap1.containsKey("movil"));
			    if(!fila.isEmpty() && !columna.isEmpty())
			    { 
			        resp.getSmap().put("fila", fila);
			        resp.getSmap().put("columna", columna);
			        
			    }
			    exito           = resp.isExito();
	            respuesta       = resp.getRespuesta();
	            respuestaOculta = resp.getRespuestaOculta();
	            
	            if(exito)
	            {
	                if(StringUtils.isNotBlank(porcensec) && StringUtils.isNotBlank(agentesec))
	                {
	                    cotizacionManager.guardaDatosAgenteSecundarioSigs(ntramite, agentesec, porcensec);
	                }
    				smap1.putAll(resp.getSmap());
    				slist2=resp.getSlist();
	            }
			}
			success = exito;
			error   = respuesta;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
			success   = false;
			error     = respuesta;
		}
		
		logger.debug(Utils.log(
				 "\n###### cotizar ######"
				,"\n#####################"
				));
		return SUCCESS;
	}
	
	 public String modificaPrimas(String ntramite, List<Map<String, String>> listaResultados, Integer formpagSigs, ArrayList<String> paquete, String cdunieco, String cdramo, String nmpoliza, String cdtipsit, String renuniext, String renramo, String renpoliex) throws Exception
	    {
	    	String mensaje = "Error Modificacion de primas seg�n sigs";
	    		try
				{		int i = 0;
						String mnprima = null;
						paquete.remove(0);
						if(paquete.size()==1)
						{i=0;}
						logger.debug(Utils.log("Forma de pago sigs :",formpagSigs));
			            for(Map<String,String>res:listaResultados)
			            {	
			            	String dsperpag = res.get("DSPERPAG");
			            	String cdplan   = res.get("CDPLAN");
			            	if (formpagSigs == 1 && dsperpag.contains("Contado/Anual") && paquete.get(i).trim().equals(cdplan.trim()))
			            	{
			            		formpagSigs = Integer.parseInt(res.get("CDPERPAG"));
			            		mnprima = res.get("MNPRIMA");break;
		        			}
			        		else if ( (formpagSigs == 2 || formpagSigs == 5) && dsperpag.contains("Semestral") && paquete.get(i).trim().equals(cdplan.trim()))
		        			{
			        			formpagSigs = Integer.parseInt(res.get("CDPERPAG"));
			        			mnprima = res.get("MNPRIMA");break;
		        			}
			        		else if ( (formpagSigs == 3 || formpagSigs == 6) && dsperpag.contains("Trimestral") && paquete.get(i).trim().equals(cdplan.trim()))
		        			{
			        			formpagSigs = Integer.parseInt(res.get("CDPERPAG"));
			        			mnprima = res.get("MNPRIMA");break;
		        			}
			        		else if ( (formpagSigs == 4 || formpagSigs == 7) && dsperpag.contains("Mensual") && paquete.get(i).trim().equals(cdplan.trim()))
		        			{
			        			formpagSigs = Integer.parseInt(res.get("CDPERPAG"));
			        			mnprima = res.get("MNPRIMA");break;
		        			}
			            }
			            try
			    		{
			    			String params  = Utils.join("sucursal=",cdunieco,"&ramo=",cdramo,"&poliza=",nmpoliza,"&primaObjetivo=",mnprima,"&renuniext=",renuniext,"&renramo=",renramo,"&cdtipsit=",cdtipsit,"&renpoliex=",renpoliex,"&cdplan=",formpagSigs,"&cdperpag=",paquete.toString());
			    				   mensaje = HttpUtil.sendPost(sigsFacultaDatosPolizaSicapsUrl,params);
			    			if(mensaje != null)
			    			{
			    				return mensaje;
			    			}	
			    			logger.debug(Utils.log("\n WS consumido con exito: "));//,mensaje
			    			return mensaje;
			    		}
			    		catch (Exception ex)
			    		{
			    			throw new Exception(Utils.manejaExcepcion(ex));
			    		}
				}
				catch (Exception ex)
				{
					Utils.generaExcepcion(ex, mensaje);
				}
			return "mensaje";
	    }
	 @SuppressWarnings("unchecked")
	public ArrayList<String> cargarPoliza(String cdunieco, String cdramo, String cdpoliza, String tipoflot, String cdtipsit, String cargaCot)
		{
			logger.debug(Utils.log(
					 "\n###############################"
					,"\n###### cargar por Poliza ######"
					,"\n###### smap1 = " , smap1
					));
			if(cargaCot == null)
			{
				cargaCot="N";
			}
			ArrayList<String> paquetesYFormaPago = new ArrayList<String>();
			try
			{
				String params      = Utils.join("sucursal=",cdunieco,"&ramo=",cdramo,"&poliza=",cdpoliza,"&tipoflot=",tipoflot,"&cdtipsit=",cdtipsit,"&cargaCot=",cargaCot)
					  ,respuestaWS =HttpUtil.sendPost(sigsObtenerDatosPorSucRamPolUrl,params);
					HashMap<String, ArrayList<String>> someObject = (HashMap<String, ArrayList<String>>)JSONUtil.deserialize(respuestaWS);
					Map<String,String>parametros = (Map<String,String>)someObject.get("params");
					String formpagSigs = parametros.get("formpagSigs");
					paquetesYFormaPago.add(formpagSigs);
					paquetesYFormaPago.addAll(1,someObject.get("paquetes"));
					logger.debug(Utils.log(paquetesYFormaPago));
			}
			catch (Exception ex)
			{
				respuesta = Utils.manejaExcepcion(ex);
			}
			return paquetesYFormaPago;
		}
	/*
	public String cotizarOld()
	{
		logger.debug("\n"
				+ "\n###############################"
				+ "\n###############################"
				+ "\n######      cotizar      ######"
				+ "\n######                   ######"
				);
		logger.debug("smap1: "+smap1);
		logger.debug("slist1: "+slist1);
		
		this.session=ActionContext.getContext().getSession();
		
		try
		{
			String cdunieco = smap1.get("cdunieco");
			String cdramo   = smap1.get("cdramo");
			String cdtipsit = smap1.get("cdtipsit");
			
			UserVO usuario    = (UserVO)session.get("USUARIO");
			String user       = usuario.getUser();
			String cdelemento = usuario.getEmpresa().getElementoId();
			
			String nmpoliza = slist1.get(0).get("nmpoliza");
			String feini    = slist1.get(0).get("feini");
			String fefin    = slist1.get(0).get("fefin");
			
			String cdpersonCli = smap1.get("cdpersonCli");
			String cdideperCli = smap1.get("cdideperCli");
			
			boolean noTarificar = StringUtils.isNotBlank(smap1.get("notarificar"))&&smap1.get("notarificar").equals("si");
			Date fechaHoy = new Date();
			
			boolean conIncisos = StringUtils.isNotBlank(smap1.get("conincisos"))&&smap1.get("conincisos").equals("si");
			
			smap1.put("nmpoliza",nmpoliza);//salida
			
			if(noTarificar==false)
			{
				////////////////////////////////
				////// si no hay nmpoliza //////
				if(nmpoliza==null||nmpoliza.length()==0)
				{
					try
					{
						WrapperResultados wrapperNumeroPoliza = kernelManager.calculaNumeroPoliza(cdunieco,cdramo,"W");
						nmpoliza = (String)wrapperNumeroPoliza.getItemMap().get("NUMERO_POLIZA");
					}
					catch(Exception ex)
					{
						throw new ApplicationException("Falta parametrizar la numeraci\u00f3n de p\u00f3liza");
					}
				}
				////// si no hay nmpoliza //////
				////////////////////////////////
				
				smap1.put("nmpoliza",nmpoliza);//salida
				
				//////////////////////
	            ////// mpolizas //////
	            Map<String,String>mapaMpolizas=new HashMap<String,String>(0);
	            mapaMpolizas.put("pv_cdunieco"  , cdunieco);
	            mapaMpolizas.put("pv_cdramo"    , cdramo);
	            mapaMpolizas.put("pv_estado"    , "W");
	            mapaMpolizas.put("pv_nmpoliza"  , nmpoliza);
	            mapaMpolizas.put("pv_nmsuplem"  , "0");
	            mapaMpolizas.put("pv_status"    , "V");
	            mapaMpolizas.put("pv_swestado"  , "0");
	            mapaMpolizas.put("pv_nmsolici"  , null);
	            mapaMpolizas.put("pv_feautori"  , null);
	            mapaMpolizas.put("pv_cdmotanu"  , null);
	            mapaMpolizas.put("pv_feanulac"  , null);
	            mapaMpolizas.put("pv_swautori"  , "N");
	            mapaMpolizas.put("pv_cdmoneda"  , "001");
	            mapaMpolizas.put("pv_feinisus"  , null);
	            mapaMpolizas.put("pv_fefinsus"  , null);
	            mapaMpolizas.put("pv_ottempot"  , "R");
	            mapaMpolizas.put("pv_feefecto"  , feini);
	            mapaMpolizas.put("pv_hhefecto"  , "12:00");
	            mapaMpolizas.put("pv_feproren"  , fefin);
	            mapaMpolizas.put("pv_fevencim"  , null);
	            mapaMpolizas.put("pv_nmrenova"  , "0");
	            mapaMpolizas.put("pv_ferecibo"  , null);
	            mapaMpolizas.put("pv_feultsin"  , null);
	            mapaMpolizas.put("pv_nmnumsin"  , "0");
	            mapaMpolizas.put("pv_cdtipcoa"  , "N");
	            mapaMpolizas.put("pv_swtarifi"  , "A");
	            mapaMpolizas.put("pv_swabrido"  , null);
	            mapaMpolizas.put("pv_feemisio"  , renderFechas.format(fechaHoy));
	            mapaMpolizas.put("pv_cdperpag"  , "12");
	            mapaMpolizas.put("pv_nmpoliex"  , cdideperCli);
	            mapaMpolizas.put("pv_nmcuadro"  , "P1");
	            mapaMpolizas.put("pv_porredau"  , "100");
	            mapaMpolizas.put("pv_swconsol"  , "S");
	            mapaMpolizas.put("pv_nmpolant"  , null);
	            mapaMpolizas.put("pv_nmpolnva"  , null);
	            mapaMpolizas.put("pv_fesolici"  , renderFechas.format(fechaHoy));
	            mapaMpolizas.put("pv_cdramant"  , user);
	            mapaMpolizas.put("pv_cdmejred"  , null);
	            mapaMpolizas.put("pv_nmpoldoc"  , null);
	            mapaMpolizas.put("pv_nmpoliza2" , null);
	            mapaMpolizas.put("pv_nmrenove"  , null);
	            mapaMpolizas.put("pv_nmsuplee"  , null);
	            mapaMpolizas.put("pv_ttipcamc"  , null);
	            mapaMpolizas.put("pv_ttipcamv"  , null);
	            mapaMpolizas.put("pv_swpatent"  , null);
	            mapaMpolizas.put("pv_pcpgocte"  , "100");
	            mapaMpolizas.put("pv_tipoflot"  , null);
	            mapaMpolizas.put("pv_accion"    , "U");
	            kernelManager.insertaMaestroPolizas(mapaMpolizas);
	            ////// mpolizas //////
	            //////////////////////
				
	            String llaveRol="";
	            String llaveSexo="";
	            String llaveFenacimi="DATE";
	            String llaveCodPostal="";
	            
	            if(conIncisos)
	            {
		            ////////////////////////////////
		            ////// ordenar al titular //////
		            
		            ////// 1. indicar para la situacion el indice //////
		            try {
		            	LinkedHashMap<String,Object>p=new LinkedHashMap<String,Object>();
		            	p.put("cdtipsit",cdtipsit);
		            	Map<String,String>atributos=consultasManager.consultaDinamica(ObjetoBD.OBTIENE_ATRIBUTOS, p).get(0);
		            	if(atributos.get("PARENTESCO") != null) {
		            		llaveRol=atributos.get("PARENTESCO");
		                	if(llaveRol.length()==1) {
		                		llaveRol="0"+llaveRol;
		                	}
		                	llaveRol="parametros.pv_otvalor"+llaveRol;
		            	}
		            	if(atributos.get("SEXO") != null) {
		            		llaveSexo=atributos.get("SEXO");
		            		if(llaveSexo.length()==1) {
		                		llaveSexo="0"+llaveSexo;
		                	}
		                	llaveSexo="parametros.pv_otvalor"+llaveSexo;
		            	}
		            	if(atributos.get("FENACIMI") != null) {
		            		llaveFenacimi=atributos.get("FENACIMI");
		                	if(llaveFenacimi.length()==1) {
		                		llaveFenacimi="0"+llaveFenacimi;
		                	}
		                	llaveFenacimi="parametros.pv_otvalor"+llaveFenacimi;
		            	}
		            	if(atributos.get("CODPOSTAL") != null) {
		            		llaveCodPostal=atributos.get("CODPOSTAL");
		                	if(llaveCodPostal.length()==1) {
		                		llaveCodPostal="0"+llaveCodPostal;
		                	}
		                	llaveCodPostal="parametros.pv_otvalor"+llaveCodPostal;
		            	}
		            } catch(Exception ex){
		            	logger.error("error al obtener atributos", ex);
		            }
		            ////// 1. indicar para la situacion el indice //////
		            
		            ////// parche. Validar codigo postal //////
		            if(StringUtils.isNotBlank(llaveCodPostal)&&StringUtils.isNotBlank(slist1.get(0).get(llaveCodPostal)))
		            {
		            	LinkedHashMap<String,Object>paramsValues=new LinkedHashMap<String,Object>();
		            	paramsValues.put("param1",slist1.get(0).get(llaveCodPostal));
		            	paramsValues.put("param2",cdtipsit);
		            	storedProceduresManager.procedureVoidCall(ObjetoBD.VALIDA_CODPOSTAL_TARIFA.getNombre(), paramsValues, null);
		            }
		            //// parche. Validar codigo postal //////
		            
		            ////// 2. ordenar //////
		            int indiceTitular=-1;
		            for(int i=0;i<slist1.size();i++)
		            {
		            	if(slist1.get(i).get(llaveRol).equalsIgnoreCase("T"))
		            	{
		            		indiceTitular=i;
		            	}
		            }
		            List<Map<String,String>> temp    = new ArrayList<Map<String,String>>(0);
		            Map<String,String>       titular = slist1.get(indiceTitular);
		            temp.add(titular);
		            slist1.remove(indiceTitular);
		            temp.addAll(slist1);
		            slist1=temp;
		            ////// 2. ordenar //////
		            
		            ////// ordenar al titular //////
		            ////////////////////////////////
	            }
	            
	            //////////////////////////////////////////
	            ////// mpolisit y tvalosit iterados //////
	            int contador=1;
	            for(Map<String,String>inciso:slist1)
	            {
	            	//////////////////////////////
	            	////// mpolisit iterado //////
	            	Map<String,Object>mapaPolisitIterado=new HashMap<String,Object>(0);
	                mapaPolisitIterado.put("pv_cdunieco_i"   , cdunieco);
	                mapaPolisitIterado.put("pv_cdramo_i"     , cdramo);
	                mapaPolisitIterado.put("pv_estado_i"     , "W");
	                mapaPolisitIterado.put("pv_nmpoliza_i"   , nmpoliza);
	                mapaPolisitIterado.put("pv_nmsituac_i"   , contador+"");
	                mapaPolisitIterado.put("pv_nmsuplem_i"   , "0");
	                mapaPolisitIterado.put("pv_status_i"     , "V");
	                mapaPolisitIterado.put("pv_cdtipsit_i"   , cdtipsit);
	                mapaPolisitIterado.put("pv_swreduci_i"   , null);
	                mapaPolisitIterado.put("pv_cdagrupa_i"   , "1");
	                mapaPolisitIterado.put("pv_cdestado_i"   , "0");
	                mapaPolisitIterado.put("pv_fefecsit_i"   , renderFechas.parse(feini));
	                mapaPolisitIterado.put("pv_fecharef_i"   , renderFechas.parse(feini));
	                mapaPolisitIterado.put("pv_cdgrupo_i"    , null);
	                mapaPolisitIterado.put("pv_nmsituaext_i" , null);
	                mapaPolisitIterado.put("pv_nmsitaux_i"   , null);
	                mapaPolisitIterado.put("pv_nmsbsitext_i" , null);
	                mapaPolisitIterado.put("pv_cdplan_i"     , "1");
	                mapaPolisitIterado.put("pv_cdasegur_i"   , "30");
	                mapaPolisitIterado.put("pv_accion_i"     , "I");
	                kernelManager.insertaPolisit(mapaPolisitIterado);
	            	////// mpolisit iterado //////
	            	//////////////////////////////
	                
	                //////////////////////////////
	                ////// tvalosit iterado //////
	                
	                ////// 1. tvalosit base //////
	                Map<String,String>mapaValositIterado=new HashMap<String,String>(0);
	                mapaValositIterado.put("pv_cdunieco" , cdunieco);
	                mapaValositIterado.put("pv_cdramo"   , cdramo);
	                mapaValositIterado.put("pv_estado"   , "W");
	                mapaValositIterado.put("pv_nmpoliza" , nmpoliza);
	                mapaValositIterado.put("pv_nmsituac" , contador+"");
	                mapaValositIterado.put("pv_nmsuplem" , "0");
	                mapaValositIterado.put("pv_status"   , "V");
	                mapaValositIterado.put("pv_cdtipsit" , cdtipsit);
	                mapaValositIterado.put("pv_accion_i" , "I");
	                ////// 1. tvalosit base //////
	                
	                ////// 2. tvalosit desde form //////
	                for(Entry<String,String>en:inciso.entrySet())
	                {
	                	// p a r a m e t r o s . p v _ o t v a l o r 
	                	//0 1 2 3 4 5 6 7 8 9 0 1
	                	String key=en.getKey();
	                	String value=en.getValue();
	                	if(key.length()>11&&key.substring(0,11).equalsIgnoreCase("parametros."))
	                	{
	                		mapaValositIterado.put(key.substring(11),value);
	                	}
	                }
	                ////// 2. tvalosit desde form //////
	                
	                ////// 3. completar faltantes //////
	                for(int i=1;i<=99;i++)
	                {
	                	String key="pv_otvalor"+i;
	                	if(i<10)
	                	{
	                		key="pv_otvalor0"+i;
	                	}
	                	if(!mapaValositIterado.containsKey(key))
	                	{
	                		mapaValositIterado.put(key,null);
	                	}
	                }
	                ////// 3. completar faltantes //////
	                
	                ////// 4. custom //////
                	ManagerRespuestaSmapVO respTvalositConst=cotizacionManager.obtenerParametrosCotizacion(
                			ParametroCotizacion.TVALOSIT_CONSTANTE
                			,cdramo
                			,cdtipsit
                			,null
                			,null);
                	if(respTvalositConst.isExito())
                	{
                		for(int i=1;i<=13;i++)
                		{
                			String key = respTvalositConst.getSmap().get(Utils.join("P",i,"CLAVE"));
                			String val = respTvalositConst.getSmap().get(Utils.join("P",i,"VALOR"));
                			if(StringUtils.isNotBlank(key)&&StringUtils.isNotBlank(val))
                			{
	                			mapaValositIterado.put
	                			(
	                					Utils.join
	                					(
	                							"pv_otvalor"
	                							,StringUtils.leftPad(key,2,"0")
	                					)
	                					,val
	                			);
                			}
                		}
                	}
	                
                	if(cdtipsit.equals(TipoSituacion.GASTOS_MEDICOS_INDIVIDUAL.getCdtipsit()))
                	{
                		mapaValositIterado.put("pv_otvalor22",
                				cotizacionManager.cargarTabuladoresGMIParche(mapaValositIterado.get("pv_otvalor16"), "22")
                		);
                		mapaValositIterado.put("pv_otvalor23",
                				cotizacionManager.cargarTabuladoresGMIParche(mapaValositIterado.get("pv_otvalor16"), "23")
                		);
                	}
	                ////// 4. custom //////
	                
	                kernelManager.insertaValoresSituaciones(mapaValositIterado);
	                ////// tvalosit iterado //////
	                //////////////////////////////
	                
	                contador++;
	            }
	            ////// mpolisit y tvalosit iterados //////
	            //////////////////////////////////////////
		        
	            /////////////////////////////
	            ////// clonar personas //////
	            contador=1;
	            for(Map<String,String> inciso : slist1)
	            {
	                Map<String,Object> mapaClonPersonaIterado=new HashMap<String,Object>(0);
	                mapaClonPersonaIterado.put("pv_cdelemen_i"  , cdelemento);
	                mapaClonPersonaIterado.put("pv_cdunieco_i"  , cdunieco);
	                mapaClonPersonaIterado.put("pv_cdramo_i"    , cdramo);
	                mapaClonPersonaIterado.put("pv_estado_i"    , "W");
	                mapaClonPersonaIterado.put("pv_nmpoliza_i"  , nmpoliza);
	                mapaClonPersonaIterado.put("pv_nmsituac_i"  , contador+"");
	                mapaClonPersonaIterado.put("pv_cdtipsit_i"  , cdtipsit);
	                mapaClonPersonaIterado.put("pv_fecha_i"     , fechaHoy);
	                mapaClonPersonaIterado.put("pv_cdusuario_i" , user);
	                mapaClonPersonaIterado.put("pv_p_nombre"    , inciso.get("nombre"));
	                mapaClonPersonaIterado.put("pv_s_nombre"    , inciso.get("nombre2"));
	                mapaClonPersonaIterado.put("pv_apellidop"   , inciso.get("apat"));
	                mapaClonPersonaIterado.put("pv_apellidom"   , inciso.get("amat"));
	                mapaClonPersonaIterado.put("pv_sexo"        , inciso.containsKey(llaveSexo)?inciso.get(llaveSexo):llaveSexo);
	                mapaClonPersonaIterado.put("pv_fenacimi"    , inciso.containsKey(llaveFenacimi)?
	                		renderFechas.parse(inciso.get(llaveFenacimi)):(
	                				llaveFenacimi.equalsIgnoreCase("DATE")?
	                						fechaHoy :
	                							renderFechas.parse(llaveFenacimi)));
	                mapaClonPersonaIterado.put("pv_parentesco"  , inciso.containsKey(llaveRol)?inciso.get(llaveRol):llaveRol);
	                kernelManager.clonaPersonas(mapaClonPersonaIterado);
	                contador++;
	            }
	            ////// clonar personas //////
	            /////////////////////////////
	
	            ////// mpoliper contratante recuperado //////
	            if(StringUtils.isNotBlank(cdpersonCli))
	            {
	            	LinkedHashMap<String,Object> paramsMpoliper=new LinkedHashMap<String,Object>(0);
	    			paramsMpoliper.put("pv_cdunieco_i" , cdunieco);
	    			paramsMpoliper.put("pv_cdramo_i"   , cdramo);
	    			paramsMpoliper.put("pv_estado_i"   , "W");
	    			paramsMpoliper.put("pv_nmpoliza_i" , nmpoliza);
	    			paramsMpoliper.put("pv_nmsituac_i" , "0");
					paramsMpoliper.put("pv_cdrol_i"    , "1");
					paramsMpoliper.put("pv_cdperson_i" , cdpersonCli);
					paramsMpoliper.put("pv_nmsuplem_i" , "0");
					paramsMpoliper.put("pv_status_i"   , "V");
					paramsMpoliper.put("pv_nmorddom_i" , "1");
					paramsMpoliper.put("pv_swreclam_i" , null);
					paramsMpoliper.put("pv_accion_i"   , "I");
					paramsMpoliper.put("pv_swexiper_i" , "S");
					kernelManager.movMpoliper(paramsMpoliper);
	            }
	            ////// mpoliper contratante recuperado //////
	            
	            ////////////////////////
	            ////// coberturas //////
	            /* ////////////////////* /
	            Map<String,String> mapCoberturas=new HashMap<String,String>(0);
	            mapCoberturas.put("pv_cdunieco_i" , cdunieco);
	            mapCoberturas.put("pv_cdramo_i"   , cdramo);
	            mapCoberturas.put("pv_estado_i"   , "W");
	            mapCoberturas.put("pv_nmpoliza_i" , nmpoliza);
	            mapCoberturas.put("pv_nmsituac_i" , "0");
	            mapCoberturas.put("pv_nmsuplem_i" , "0");
	            mapCoberturas.put("pv_cdgarant_i" , "TODO");
	            mapCoberturas.put("pv_cdtipsup_i" , "1");
	            kernelManager.coberturas(mapCoberturas);
	            /* ////////////////////* /
	            ////// coberturas //////
	            ////////////////////////
	            
	            //////////////////////////
	            ////// TARIFICACION //////
	            /* //////////////////////* /
	            Map<String,String> mapaTarificacion=new HashMap<String,String>(0);
	            mapaTarificacion.put("pv_cdusuari_i" , user);
	            mapaTarificacion.put("pv_cdelemen_i" , cdelemento);
	            mapaTarificacion.put("pv_cdunieco_i" , cdunieco);
	            mapaTarificacion.put("pv_cdramo_i"   , cdramo);
	            mapaTarificacion.put("pv_estado_i"   , "W");
	            mapaTarificacion.put("pv_nmpoliza_i" , nmpoliza);
	            mapaTarificacion.put("pv_nmsituac_i" , "0");
	            mapaTarificacion.put("pv_nmsuplem_i" , "0");
	            mapaTarificacion.put("pv_cdtipsit_i" , cdtipsit);
	            kernelManager.ejecutaASIGSVALIPOL(mapaTarificacion);
	            /* //////////////////////* /
	            ////// TARIFICACION //////
	            //////////////////////////
		    }
            
            ///////////////////////////////////
            ////// Generacion cotizacion //////
            /* ///////////////////////////////* /
            Map<String,String> mapaDuroResultados=new HashMap<String,String>(0);
            mapaDuroResultados.put("pv_cdusuari_i" , user);
            mapaDuroResultados.put("pv_cdunieco_i" , cdunieco);
            mapaDuroResultados.put("pv_cdramo_i"   , cdramo);
            mapaDuroResultados.put("pv_estado_i"   , "W");
            mapaDuroResultados.put("pv_nmpoliza_i" , nmpoliza);
            mapaDuroResultados.put("pv_cdelemen_i" , cdelemento);
            mapaDuroResultados.put("pv_cdtipsit_i" , cdtipsit);
            List<Map<String,String>> listaResultados=kernelManager.obtenerResultadosCotizacion2(mapaDuroResultados);
            logger.debug("listaResultados: "+listaResultados);
            /* ///////////////////////////////* /
            ////// Generacion cotizacion //////
            ///////////////////////////////////
            
            ////////////////////////////////
            ////// Agrupar resultados //////
            /*
            NMSUPLEM=0,
			FEFECSIT=13/01/2014,
			NMPOLIZA=3853,
			MNPRIMA=4571.92,           <--2
			CDPERPAG=7,                <--1
			DSPLAN=Plus 500,           <--3
			FEVENCIM=13/01/2015,
			STATUS=V,
			NMSITUAC=3,
			ESTADO=W,
			DSPERPAG=DXN Catorcenal,   <--(1)
			CDCIAASEG=20,
			CDIDENTIFICA=2,
			CDTIPSIT=SL,
			FEEMISIO=13/01/2014,
			CDUNIECO=1,
			CDRAMO=2,
			CDPLAN=M,                  <--(3)
			DSUNIECO=PUEBLA
             * /
            
            ////// 1. encontrar planes, formas de pago y algun nmsituac//////
            Map<String,String>formasPago = new LinkedHashMap<String,String>();
            Map<String,String>planes     = new LinkedHashMap<String,String>();
            String nmsituac="";
            for(Map<String,String>res:listaResultados)
            {
            	String cdperpag = res.get("CDPERPAG");
            	String dsperpag = res.get("DSPERPAG");
            	String cdplan   = res.get("CDPLAN");
            	String dsplan   = res.get("DSPLAN");
            	if(!formasPago.containsKey(cdperpag))
            	{
            		formasPago.put(cdperpag,dsperpag);
            	}
            	if(!planes.containsKey(cdplan))
            	{
            		planes.put(cdplan,dsplan);
            	}
            	nmsituac=res.get("NMSITUAC");
            }
            logger.debug("formas de pago: "+formasPago);
            logger.debug("planes: "+planes);
            ////// 1. encontrar planes y formas de pago //////
            
            ////// 2. crear formas de pago //////
            List<Map<String,String>>tarifas=new ArrayList<Map<String,String>>();
            for(Entry<String,String>formaPago:formasPago.entrySet())
            {
            	Map<String,String>tarifa=new HashMap<String,String>();
            	tarifa.put("CDPERPAG",formaPago.getKey());
            	tarifa.put("DSPERPAG",formaPago.getValue());
            	tarifa.put("NMSITUAC",nmsituac);
            	tarifas.add(tarifa);
            }
            logger.debug("tarifas despues de formas de pago: "+tarifas);
            ////// 2. crear formas de pago //////
            
            ////// 3. crear planes //////
            for(Map<String,String>tarifa:tarifas)
            {
            	for(Entry<String,String>plan:planes.entrySet())
                {
                	tarifa.put("CDPLAN"+plan.getKey(),plan.getKey());
                	tarifa.put("DSPLAN"+plan.getKey(),plan.getValue());
                }
            }
            logger.debug("tarifas despues de planes: "+tarifas);
            ////// 3. crear planes //////
            
            ////// 4. crear primas //////
            for(Map<String,String>res:listaResultados)
            {
            	String cdperpag = res.get("CDPERPAG");
            	String mnprima  = res.get("MNPRIMA");
            	String cdplan   = res.get("CDPLAN");
            	for(Map<String,String>tarifa:tarifas)
                {
            		if(tarifa.get("CDPERPAG").equals(cdperpag))
            		{
            			if(tarifa.containsKey("MNPRIMA"+cdplan))
            			{
            				logger.debug("ya hay prima para "+cdplan+" en "+cdperpag+": "+tarifa.get("MNPRIMA"+cdplan));
            				tarifa.put("MNPRIMA"+cdplan,((Double)Double.parseDouble(tarifa.get("MNPRIMA"+cdplan))+(Double)Double.parseDouble(mnprima))+"");
            				logger.debug("nueva: "+tarifa.get("MNPRIMA"+cdplan));
            			}
            			else
            			{
            				logger.debug("primer prima para "+cdplan+" en "+cdperpag+": "+mnprima);
            				tarifa.put("MNPRIMA"+cdplan,mnprima);
            			}
            		}
                }
            }
            logger.debug("tarifas despues de primas: "+tarifas);
            
            slist2=tarifas;
            ////// 4. crear primas //////
            
            ////// Agrupar resultados //////
            ////////////////////////////////
            
            ///////////////////////////////////
            ////// columnas para el grid //////
            List<ComponenteVO>tatriPlanes=new ArrayList<ComponenteVO>();
            
            ////// 1. forma de pago //////
            ComponenteVO tatriCdperpag=new ComponenteVO();
        	tatriCdperpag.setType(ComponenteVO.TIPO_GENERICO);
        	tatriCdperpag.setLabel("CDPERPAG");
        	tatriCdperpag.setTipoCampo(ComponenteVO.TIPOCAMPO_NUMERICO);
        	tatriCdperpag.setNameCdatribu("CDPERPAG");
        	
        	/*Map<String,String>mapaCdperpag=new HashMap<String,String>();
        	mapaCdperpag.put("OTVALOR10","CDPERPAG");
        	tatriCdperpag.setMapa(mapaCdperpag);* /
        	tatriPlanes.add(tatriCdperpag);
        	
        	ComponenteVO tatriDsperpag=new ComponenteVO();
        	tatriDsperpag.setType(ComponenteVO.TIPO_GENERICO);
        	tatriDsperpag.setLabel("Forma de pago");
        	tatriDsperpag.setTipoCampo(ComponenteVO.TIPOCAMPO_ALFANUMERICO);
        	tatriDsperpag.setNameCdatribu("DSPERPAG");
        	tatriDsperpag.setColumna(Constantes.SI);
        	
        	/*Map<String,String>mapaDsperpag=new HashMap<String,String>();
        	mapaDsperpag.put("OTVALOR08","S");
        	mapaDsperpag.put("OTVALOR10","DSPERPAG");
        	tatriDsperpag.setMapa(mapaDsperpag);* /
        	tatriPlanes.add(tatriDsperpag);
        	////// 1. forma de pago //////
        	
        	////// 2. nmsituac //////
        	ComponenteVO tatriNmsituac=new ComponenteVO();
        	tatriNmsituac.setType(ComponenteVO.TIPO_GENERICO);
        	tatriNmsituac.setLabel("NMSITUAC");
        	tatriNmsituac.setTipoCampo(ComponenteVO.TIPOCAMPO_NUMERICO);
        	tatriNmsituac.setNameCdatribu("NMSITUAC");
        	
        	/*Map<String,String>mapaNmsituac=new HashMap<String,String>();
        	mapaNmsituac.put("OTVALOR10","NMSITUAC");
        	tatriNmsituac.setMapa(mapaNmsituac);* /
        	tatriPlanes.add(tatriNmsituac);
        	////// 2. nmsituac //////
        	
        	////// 2. planes //////
            for(Entry<String,String>plan:planes.entrySet())
            {
            	////// prima
            	ComponenteVO tatriPrima=new ComponenteVO();
            	tatriPrima.setType(ComponenteVO.TIPO_GENERICO);
            	tatriPrima.setLabel(plan.getValue());
            	tatriPrima.setTipoCampo(ComponenteVO.TIPOCAMPO_PORCENTAJE);
            	tatriPrima.setColumna(Constantes.SI);
            	tatriPrima.setNameCdatribu("MNPRIMA"+plan.getKey());
            	tatriPrima.setRenderer("function(v)"
            			+ "{"
            			+ "    debug('valor:',v);"
            			+ "    v=v.toFixed(2);"
            			+ "    debug('valor fixed:',v);"
            			+ "    var v2='';"
            			+ "    var ultimoPunto=-3;"
            			+ "    for(var i=(v+'').length-1;i>=0;i--)"
            			+ "    {"
            			+ "        var digito=(v+'').charAt(i);"
            			+ "        if(digito=='.')"
            			+ "        {"
            			+ "            ultimoPunto=-2;"
            			+ "        }"
            			+ "        if(ultimoPunto>-3)"
            			+ "        {"
            			+ "            ultimoPunto=ultimoPunto+1;"
            			+ "        }"
            			+ "        if(ultimoPunto%3==0&&ultimoPunto>0)"
            			+ "        {"
            			+ "            digito=digito+',';"
            			+ "        }"
            			+ "        v2=digito+v2;"
            			+ "        if(i==0)"
            			+ "        {"
            			+ "            v2='$ '+v2;"
            			+ "        }"
            			+ "    }"
            			+ "    return v2;"
            			+ "}");
            	
            	/*Map<String,String>mapaPlan=new HashMap<String,String>();
            	mapaPlan.put("OTVALOR08","S");
            	mapaPlan.put("OTVALOR09","MONEY");
            	mapaPlan.put("OTVALOR10","MNPRIMA"+plan.getKey());
            	tatriPrima.setMapa(mapaPlan);* /
            	tatriPlanes.add(tatriPrima);
            	
            	////// cdplan
            	ComponenteVO tatriCdplan=new ComponenteVO();
             	tatriCdplan.setType(ComponenteVO.TIPO_GENERICO);
             	tatriCdplan.setLabel("CDPLAN"+plan.getKey());
             	tatriCdplan.setTipoCampo(ComponenteVO.TIPOCAMPO_ALFANUMERICO);
             	tatriCdplan.setNameCdatribu("CDPLAN"+plan.getKey());
             	tatriCdplan.setColumna(ComponenteVO.COLUMNA_OCULTA);
             	
             	/*Map<String,String>mapaCdplan=new HashMap<String,String>();
             	//mapaCdplan.put("OTVALOR08","H");
             	mapaCdplan.put("OTVALOR10","CDPLAN"+plan.getKey());
             	tatriCdplan.setMapa(mapaCdplan);* /
             	tatriPlanes.add(tatriCdplan);
             	
             	////// dsplan
             	ComponenteVO tatriDsplan=new ComponenteVO();
             	tatriDsplan.setType(ComponenteVO.TIPO_GENERICO);
             	tatriDsplan.setLabel("DSPLAN"+plan.getKey());
             	tatriDsplan.setTipoCampo(ComponenteVO.TIPOCAMPO_ALFANUMERICO);
             	tatriDsplan.setNameCdatribu("DSPLAN"+plan.getKey());
             	
             	/*Map<String,String>mapaDsplan=new HashMap<String,String>();
             	//mapaDsplan.put("OTVALOR08","H");
             	mapaDsplan.put("OTVALOR10","DSPLAN"+plan.getKey());
             	tatriDsplan.setMapa(mapaDsplan);* /
             	tatriPlanes.add(tatriDsplan);
            }
            ////// 2. planes //////
            
            GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
            gc.setEsMovil(session!=null&&session.containsKey("ES_MOVIL")&&((Boolean)session.get("ES_MOVIL"))==true);
            if(!gc.isEsMovil()&&smap1.containsKey("movil"))
            {
            	gc.setEsMovil(true);
            }
            gc.genera(tatriPlanes);
            
            String columnas = gc.getColumns().toString();
            // c o l u m n s : [
            //0 1 2 3 4 5 6 7 8
            smap1.put("columnas",columnas.substring(8));
            
            String fields = gc.getFields().toString();
            // f i e l d s : [
            //0 1 2 3 4 5 6 7
            smap1.put("fields",fields.substring(7));
            ////// columnas para el grid //////
            ///////////////////////////////////
            
			success=true;
		}
		catch(Exception ex)
		{
			logger.debug("error al cotizar",ex);
			success=false;
			error=ex.getMessage();
		}
		
		logger.debug("\n"
				+ "\n######                   ######"
				+ "\n######      cotizar      ######"
				+ "\n###############################"
				+ "\n###############################"
				);
		return SUCCESS;
	}
	/*/////////////////*/
	////// cotizar //////
	/////////////////////
	
	public String cargarCotizacion()
	{
		logger.debug(Utils.log(
				"\n##############################",
				"\n###### cargarCotizacion ######",
				"\n###### smap1 = ", smap1
				));
		success = true;
		
		String cdunieco = smap1.get("cdunieco");
		String cdramo   = smap1.get("cdramo");
		String estado   = "W";
		String cdtipsit = smap1.get("cdtipsit");
		String nmpoliza = smap1.get("nmpoliza");
		
		String ntramiteIn = smap1.get("ntramiteIn");
		
		UserVO usuario  = (UserVO)session.get("USUARIO");
		String cdusuari = usuario.getUser();
		String cdsisrol = usuario.getRolActivo().getClave();
		
		//validar nmpoliza contra producto y situacion
		if(success)
		{
			try
			{
				Map<String,String>tipoSituacion=cotizacionManager.cargarTipoSituacion(cdramo,cdtipsit);
				
				LinkedHashMap<String,Object>paramsValidaCargarCotizacion=new LinkedHashMap<String,Object>();
				paramsValidaCargarCotizacion.put("param1" , cdramo);
				paramsValidaCargarCotizacion.put("param2" , cdtipsit);
				paramsValidaCargarCotizacion.put("param3" , nmpoliza);
				Map<String,String>datosParaComplementar=storedProceduresManager.procedureMapCall(ObjetoBD.VALIDA_CARGAR_COTIZACION.getNombre(), paramsValidaCargarCotizacion, null);
				
				/*
				 * cuando se encuentra cdunieco y ntramite para esa cotizacion y no es auto:
				 */
				if(tipoSituacion.get("SITUACION").equals("PERSONA")
						&&datosParaComplementar.containsKey("NTRAMITE"))
				{
					throw new ApplicationException("La cotizaci\u00f3n ya se encuentra en tr\u00e1mite de emisi\u00f3n");
				}
				/*
				 * cuando se encuentra cdunieco y ntramite y es auto, se mandara a datos complementarios
				 */
				else
				{
					/*
					 * 
					 * 
					 * JTEZVA MIERCOLES 10 AGOSTO 2016
					 * ESTE BLOQUE DEBE TENER LA MISMA LOGICA QUE EL BLOQUE FINAL EN
					 * CotizacionAutoManagerImpl.cargarCotizacionAutoFlotilla()
					 * 
					 * EL BLOQUE COMPLETO DE ELSE IF
					 * 
					 * 
					 */
					if(datosParaComplementar.containsKey("ESTADO")
							&&datosParaComplementar.containsKey("NMPOLIZA")
							)//para clonar emitidas
					{
						cdunieco = datosParaComplementar.get("CDUNIECO");
						estado   = datosParaComplementar.get("ESTADO");
						nmpoliza = datosParaComplementar.get("NMPOLIZA");
					}
					else if(datosParaComplementar.containsKey("CDUNIECO_RECUPERADO"))//para normales
					{
						if (
							"S".equals(datosParaComplementar.get("LIGADA")) &&
							(
								StringUtils.isBlank(ntramiteIn) ||
								!ntramiteIn.equals(datosParaComplementar.get("NTRAMITE_LIGADO"))
							)
						) { // Esa cotizacion es la ultima hecha para un tramite, y no es el tramite actual
							String error = Utils.join("Esta cotizaci\u00f3n pertenece al tr\u00e1mite ",
									datosParaComplementar.get("NTRAMITE_LIGADO"));
							
							Map<String, String> tramite = siniestrosManager.obtenerTramiteCompleto(datosParaComplementar.get("NTRAMITE_LIGADO"));
							String status = tramite.get("STATUS");
							
							String dsstatus = cotizacionManager.recuperarDescripcionEstatusTramite(status);
							error = Utils.join(error, " (estatus: '", dsstatus, "')");
							
							logger.debug(Utils.log("cotizacion ligada al tramite ", datosParaComplementar.get("NTRAMITE_LIGADO"),
									", status ", status, "."));
							
							if (EstatusTramite.RECHAZADO.getCodigo().equals(status)) {
								error = Utils.join(error, ", por favor generar un nuevo tr\u00e1mite");
							} else {
								error = Utils.join(error, ", favor de acceder desde mesa de control");
							}
							
							throw new ApplicationException(error);
						}
						cdunieco = datosParaComplementar.get("CDUNIECO_RECUPERADO");
						smap1.put("CDUNIECO" , cdunieco);
					}
					else if(datosParaComplementar.containsKey("NTRAMITE")
							&&datosParaComplementar.containsKey("CDUNIECO")
							)//para complementar/clonar tramite
					{
						String ntramiteCot = datosParaComplementar.get("NTRAMITE");
						Map<String, String> tramiteCot = siniestrosManager.obtenerTramiteCompleto(ntramiteCot);
						String statusTramiteCot = tramiteCot.get("STATUS");
						
						String dsstatusTramiteCot = cotizacionManager.recuperarDescripcionEstatusTramite(statusTramiteCot);
						
						if (StringUtils.isBlank(ntramiteIn)) { // entran desde cotizacion abierta
							if ("S".equals(datosParaComplementar.get("SWORIGENMESA"))) { // intentar recuperar uno de tramite creado en mesa
								String error = Utils.join(
										"Esta cotizaci\u00f3n pertenece al tr\u00e1mite ",
										ntramiteCot,
										" (estatus: '", dsstatusTramiteCot, "')");
								if (EstatusTramite.RECHAZADO.getCodigo().equals(statusTramiteCot)) {
									error = Utils.join(error, ", por favor generar un nuevo tr\u00e1mite");
								} else {
									error = Utils.join(error, ", favor de acceder desde mesa de control");
								}
								
								throw new ApplicationException(error);
							}
						} else { // entran desde un tramite
							if (ntramiteIn.equals(ntramiteCot)) { // es del mismo tramite
								String error = Utils.join("Esta cotizaci\u00f3n se encuentra confirmada para este tr\u00e1mite (", ntramiteCot
										,", estatus: '", dsstatusTramiteCot,"'), favor de acceder desde mesa de control para complementarla");
								throw new ApplicationException(error);
							} else { // intentan recuperar de otro tramite
								String error = Utils.join("Esta cotizaci\u00f3n pertenece al tr\u00e1mite ", ntramiteCot,
										" (estatus: '", dsstatusTramiteCot, "')");
								
								if (EstatusTramite.RECHAZADO.getCodigo().equals(statusTramiteCot)) {
									error = Utils.join(error, ", por favor generar un nuevo tr\u00e1mite"); // de otro que esta cancelado
								} else {
									error = Utils.join(error, ", favor de acceder desde mesa de control"); // de otro activo
								}
								
								throw new ApplicationException(error);
							}
						}
						cdunieco = datosParaComplementar.get("CDUNIECO");
					}
					smap1.putAll(datosParaComplementar);
				}
			}
			catch(Exception ex)
			{
				logger.error("error obtenido al validar carga de cotizacion",ex);
				error=ex.getMessage();
				success = false;
			}
		}
		//validar nmpoliza contra producto y situacion
		
		//recupera tvalosit
		if(success)
		{
			try
			{
				LinkedHashMap<String,Object>paramsObtenerTvalosit=new LinkedHashMap<String,Object>();
				paramsObtenerTvalosit.put("param1" , cdunieco);
				paramsObtenerTvalosit.put("param2" , cdramo);
				paramsObtenerTvalosit.put("param3" , estado);
				paramsObtenerTvalosit.put("param4" , cdtipsit);
				paramsObtenerTvalosit.put("param5" , nmpoliza);
				paramsObtenerTvalosit.put("param6" , cdusuari);
				paramsObtenerTvalosit.put("param7" , cdsisrol);
//				slist1 = storedProceduresManager.procedureListCall(ObjetoBD.OBTIENE_TVALOSIT_COTIZACION.getNombre(), paramsObtenerTvalosit, null);
//				if(slist1==null||slist1.size()==0)
//				{
					if((cdramo.contains("6") || cdramo.contains("16")) && cdsisrol.contains("EJECUTIVOCUENTA"))
					{
						paramsObtenerTvalosit.put("param7" , "SUSCRIAUTO");// ???
					}
					slist1 = storedProceduresManager.procedureListCall(ObjetoBD.OBTIENE_TVALOSIT_COTIZACION.getNombre(), paramsObtenerTvalosit, null);
//				}
				if(slist1==null||slist1.size()==0)
				{
					throw new ApplicationException("No se puede cargar la cotizaci\u00f3n");
				}
				for(Map<String,String>iInciso:slist1)
				{
					String iCdunieco = iInciso.get("CDUNIECO");
					String iEstado   = iInciso.get("ESTADO");
					String iNmsituac = iInciso.get("NMSITUAC");
					logger.debug("iCdunieco: "+iCdunieco);
					logger.debug("iEstado: "+iEstado);
					logger.debug("iNmsituac: "+iNmsituac);
					LinkedHashMap<String,Object>paramsObtenerMpersonaCotizacion=new LinkedHashMap<String,Object>();
					paramsObtenerMpersonaCotizacion.put("param1",iCdunieco);
					paramsObtenerMpersonaCotizacion.put("param2",cdramo);
					paramsObtenerMpersonaCotizacion.put("param3",iEstado);
					paramsObtenerMpersonaCotizacion.put("param4",nmpoliza);
					paramsObtenerMpersonaCotizacion.put("param5",iNmsituac);
					iInciso.putAll(storedProceduresManager.procedureMapCall(ObjetoBD.OBTIENE_MPERSONA_COTIZACION.getNombre(),
							paramsObtenerMpersonaCotizacion, null));
					//copiar OTVALORXX a PARAMETROS.PV_OTVALORXX
					Map<String,String>iIncisoOtvalor=new HashMap<String,String>();
					for(Entry<String,String>en:iInciso.entrySet())
					{
						String key   = en.getKey();
						String value = en.getValue();
						if(key.length()>"otvalor".length()
								&&key.substring(0, "otvalor".length()).equalsIgnoreCase("otvalor"))
						{
							iIncisoOtvalor.put(("PARAMETROS.PV_"+key).toLowerCase(),value);
						}
						else
						{
							iIncisoOtvalor.put(key.toLowerCase(),value);
						}
					}
					iInciso.putAll(iIncisoOtvalor);
				}
			}
			catch(Exception ex)
			{
				logger.error("error al recuperar tvalosit",ex);
				error   = ex.getMessage();
				success = false;
			}
		}
		//recupera tvalosit
		
		//recupera mpolizas
		if(success)
		{
			try
			{
				List<Map<String,String>>polizas=consultasManager.cargarMpolizasPorParametrosVariables(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,null
						,null
						,null);
				smap1.put("FESOLICI" , polizas.get(0).get("FESOLICI"));
				smap1.put("FEEFECTO" , polizas.get(0).get("FEEFECTO"));
				smap1.put("FEPROREN" , polizas.get(0).get("FEPROREN"));
			}
			catch(Exception ex)
			{
				logger.error("error al obtener datos de poliza",ex);
				error   = ex.getMessage();
				success = false;
			}
		}
		//recupera mpolizas
		
		//recuperar dias validos cotizacion
		if(success)
		{
			ManagerRespuestaSmapVO dias=cotizacionManager.obtenerParametrosCotizacion(ParametroCotizacion.DIAS_VALIDOS_COTIZACION,cdramo,cdtipsit,null,null);
			if(dias.isExito())
			{
				smap1.put("diasValidos" , dias.getSmap().get("P1VALOR"));
			}
			else
			{
				logger.error("error al obtener dias validos de cotizacion, se cargan 15 por defecto, no impacta el flujo: "+dias.getRespuestaOculta());
				smap1.put("diasValidos" , "15");
			}
		}
		//recuperar dias validos cotizacion
		
		//recuperar tvalopol
		if(success)
		{
			try
			{
				Map<String,String>tvalopol=cotizacionManager.cargarTvalopol(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						);
				for(Entry<String,String>en:tvalopol.entrySet())
				{
					smap1.put(Utils.join("aux.",en.getKey().substring("parametros.pv_".length())),en.getValue());
				}
			}
			catch(Exception ex)
			{
				logger.error("Error al recuperar tvlopol",ex);
				error   = ex.getMessage();
				success = false;
			}
		}
		//recuperar tvalopol
		
		logger.debug(""
				+ "\n###### cargarCotizacion ######"
				+ "\n##############################"
				);
		return SUCCESS;
	}
	
	public String guardarSituacionesAuto()
	{
		logger.debug(""
				+ "\n####################################"
				+ "\n###### guardarSituacionesAuto ######"
				);
		logger.debug("smap1: "+smap1);
		
		String cdunieco = null;
		String cdramo   = null;
		String cdtipsit = null;
		String estado   = null;
		String nmpoliza = null;
		success = true;
		
		//obtener parametros
		if(success)
		{
			try
			{
				cdunieco = smap1.get("cdunieco");
				cdramo   = smap1.get("cdramo");
				cdtipsit = smap1.get("cdtipsit");
				estado   = smap1.get("estado");
				nmpoliza = smap1.get("nmpoliza");
				logger.debug("cdunieco: " + cdunieco);
				logger.debug("cdramo: "   + cdramo);
				logger.debug("cdtipsit: " + cdtipsit);
				logger.debug("estado: "   + estado);
				logger.debug("nmpoliza: " + nmpoliza);
			}
			catch(Exception ex)
			{
				logger.error("error al obtener parametros",ex);
				error   = "No se recibieron los datos necesarios";
				success = false;
			}
		}
		
		//poner cdperson restantes
		if(success)
		{
			try
			{
				for(Map<String,String>iAsegu:slist1)
				{
					if(StringUtils.isBlank(iAsegu.get("cdperson")))
					{
						Map<String,Object>cdperson=storedProceduresManager.procedureParamsCall(
								ObjetoBD.GENERAR_CDPERSON.getNombre(),
								new LinkedHashMap<String,Object>(),
								null,
								new String[]{"pv_cdperson_o"},
								null);
						iAsegu.put("cdperson",(String)cdperson.get("pv_cdperson_o"));
					}
				}
			}
			catch(Exception ex)
			{
				logger.error("error al completar cdperson de asegurados",ex);
				error   = ex.getMessage();
				success = false;
			}
		}
		
		//ordenar nmsituac
		if(success)
		{
			try
			{
				List<Map<String,String>>listaAux=new ArrayList<Map<String,String>>();
				
				//agregar titular
				for(Map<String,String>aseg:slist1)
				{
					if(aseg.get("cdrol").equals("1"))
					{
						listaAux.add(aseg);
					}
				}
				
				//agregar demas
				for(Map<String,String>aseg:slist1)
				{
					if(!aseg.get("cdrol").equals("1"))
					{
						listaAux.add(aseg);
					}
				}
				slist1=listaAux;
				
				//poner nmsituac
				int i=0;
				for(Map<String,String>aseg:slist1)
				{
					i=i+1;
					aseg.put("nmsituac",i+"");
				}
			}
			catch(Exception ex)
			{
				logger.error("error al ordenar nmsituac",ex);
				error   = ex.getMessage();
				success = false;
			}
		}
		
		//borrar mpoliper
		if(success)
		{
			try
			{
				LinkedHashMap<String,Object>paramsBorrarMpoliper=new LinkedHashMap<String,Object>();
				paramsBorrarMpoliper.put("param1" , cdunieco);
				paramsBorrarMpoliper.put("param2" , cdramo);
				paramsBorrarMpoliper.put("param3" , estado);
				paramsBorrarMpoliper.put("param4" , nmpoliza);
				storedProceduresManager.procedureVoidCall(ObjetoBD.BORRAR_MPOLIPER.getNombre(), paramsBorrarMpoliper, null);
			}
			catch(Exception ex)
			{
				logger.error("error al borrar mpoliper",ex);
				error   = ex.getMessage();
				success = false;
			}
		}
		
		//insertar mpersona y mpoliper
		if(success)
		{
			for(Map<String,String>aseg:slist1)
			{
				//insertar mpersona
				if(success)
				{
					try
					{
						LinkedHashMap<String,Object> parametros=new LinkedHashMap<String,Object>(0);
						parametros.put("param01_pv_cdperson_i"    , aseg.get("cdperson"));
						parametros.put("param02_pv_cdtipide_i"    , "1");
						parametros.put("param03_pv_cdideper_i"    , aseg.get("cdideper"));
						parametros.put("param04_pv_dsnombre_i"    , aseg.get("nombre"));
						parametros.put("param05_pv_cdtipper_i"    , "1");
						parametros.put("param06_pv_otfisjur_i"    , aseg.get("tpersona"));
						parametros.put("param07_pv_otsexo_i"      , aseg.get("sexo"));
						parametros.put("param08_pv_fenacimi_i"    , renderFechas.parse((String)aseg.get("fenacimi")));
						parametros.put("param09_pv_cdrfc_i"       , aseg.get("cdrfc"));
						parametros.put("param10_pv_dsemail_i"     , "");
						parametros.put("param11_pv_dsnombre1_i"   , aseg.get("segundo_nombre"));
						parametros.put("param12_pv_dsapellido_i"  , aseg.get("Apellido_Paterno"));
						parametros.put("param13_pv_dsapellido1_i" , aseg.get("Apellido_Materno"));
						parametros.put("param14_pv_feingreso_i"   , new Date());
						parametros.put("param15_pv_cdnacion_i"    , aseg.get("nacional"));
						parametros.put("param16"                  , null);
						parametros.put("param17"                  , null);
						parametros.put("param18"                  , null);
						parametros.put("param19"                  , null);
						parametros.put("param18"                  , null);
						parametros.put("param19"                  , null);
						parametros.put("param20"                  , null);
						parametros.put("param21"                  , null);
						parametros.put("param22"                  , null);
						parametros.put("param23"                  , null);
						parametros.put("param24"                  , "0");//usuario default externo
						parametros.put("param25_pv_accion_i"      , "I");
						String[] tipos=new String[]{
								"VARCHAR","VARCHAR","VARCHAR","VARCHAR",
								"VARCHAR","VARCHAR","VARCHAR","DATE",
								"VARCHAR","VARCHAR","VARCHAR","VARCHAR",
								"VARCHAR","DATE"   ,"VARCHAR","VARCHAR",
								"VARCHAR","VARCHAR","VARCHAR","VARCHAR",
								"VARCHAR","VARCHAR","VARCHAR","VARCHAR",
								"VARCHAR"
						};
						storedProceduresManager.procedureVoidCall(ObjetoBD.MOV_MPERSONA.getNombre(), parametros, tipos);
					}
					catch(Exception ex)
					{
						logger.error("error al insertar mpersona "+aseg,ex);
						error   = ex.getMessage();
						success = false;
					}
				}
				
				//insertar mpoliper
				if(success)
				{
					try
					{
						LinkedHashMap<String,Object> parametros=new LinkedHashMap<String,Object>(0);
						parametros.put("param01_pv_cdunieco_i" , cdunieco);
						parametros.put("param02_pv_cdramo_i"   , cdramo);
						parametros.put("param03_pv_estado_i"   , estado);
						parametros.put("param04_pv_nmpoliza_i" , nmpoliza);
						parametros.put("param05_pv_nmsituac_i" , aseg.get("nmsituac"));
						parametros.put("param06_pv_cdrol_i"    , aseg.get("cdrol"));
						parametros.put("param07_pv_cdperson_i" , aseg.get("cdperson"));
						parametros.put("param08_pv_nmsuplem_i" , "0");
						parametros.put("param09_pv_status_i"   , "V");
						parametros.put("param10_pv_nmorddom_i" , "1");
						parametros.put("param11_pv_swreclam_i" , null);
						parametros.put("param12_pv_accion_i"   , "I");
						parametros.put("param13_pv_swexiper_i" , aseg.get("swexiper"));
						storedProceduresManager.procedureVoidCall(ObjetoBD.MOV_MPOLIPER.getNombre(), parametros, null);
					}
					catch(Exception ex)
					{
						logger.error("error al insertar mpoliper "+aseg,ex);
						error   = ex.getMessage();
						success = false;
					}
				}
				
				/**
				 * SOLO PARA EL CONTRATANTE
				 */
				if("1".equalsIgnoreCase(aseg.get("cdrol")))
				{
					String cdIdeperAseg = aseg.get("cdideper");
					/**
					 * En caso  de que si tenga un cdIdeper y el domicilio aun no este guardado en BD, se buscan los datos en el WS y se insertan en BD
					 */
					if(StringUtils.isNotBlank(cdIdeperAseg) && !"0".equalsIgnoreCase(cdIdeperAseg) && !"0L".equalsIgnoreCase(cdIdeperAseg)){
						try{
							
							UserVO usuario = (UserVO) session.get("USUARIO");
							String usuarioCaptura =  null;
							
							if(usuario!=null){
								if(StringUtils.isNotBlank(usuario.getClaveUsuarioCaptura())){
									usuarioCaptura = usuario.getClaveUsuarioCaptura();
								}else{
									usuarioCaptura = usuario.getCodigoPersona();
								}
								
							}
							
							WrapperResultados result = kernelManager.existeDomicilioContratante(cdIdeperAseg, aseg.get("cdperson"));
							
							if(result != null && result.getItemMap() != null && result.getItemMap().containsKey("EXISTE_DOMICILIO")){
								if(StringUtils.isBlank((String)result.getItemMap().get("EXISTE_DOMICILIO")) || !Constantes.SI.equalsIgnoreCase((String)result.getItemMap().get("EXISTE_DOMICILIO"))){
									/**
									 *  Si no existe Domicilio, Se va al WS por la informacion del mismo
									 */
									HashMap<String, Object> parametros =  new HashMap<String, Object>();
									parametros.put("pv_cdunieco_i", cdunieco);
									parametros.put("pv_cdramo_i",   cdramo);
									parametros.put("pv_estado_i",   estado);
									parametros.put("pv_nmpoliza_i", nmpoliza);
									
							    	String cdtipsitGS = kernelManager.obtenCdtipsitGS(parametros);
							    	
							    	ClienteGeneral clienteGeneral = new ClienteGeneral();
							    	//clienteGeneral.setRfcCli((String)aseg.get("cdrfc"));
							    	clienteGeneral.setRamoCli(Integer.parseInt(cdtipsitGS));
							    	clienteGeneral.setNumeroExterno(cdIdeperAseg);
							    	
							    	ClienteGeneralRespuesta clientesRes = ice2sigsService.ejecutaWSclienteGeneral(null, null, null, null, null, null, null, Ice2sigsService.Operacion.CONSULTA_GENERAL, clienteGeneral, null, false);
							    	
							    	if(clientesRes !=null && ArrayUtils.isNotEmpty(clientesRes.getClientesGeneral())){
							    		ClienteGeneral cliDom = null;
							    		
							    		if(clientesRes.getClientesGeneral().length == 1){
							    			logger.debug("Cliente unico encontrado en WS, guardando informacion del WS...");
							    			cliDom = clientesRes.getClientesGeneral()[0];
							    		}else {
							    			logger.error("Error, No se pudo obtener el domicilio del WS. Se ha encontrado mas de Un Domicilio!");
							    		}
							    		
							    		/*Cuando se encontraba el cliente de una lista
							    		 * for(ClienteGeneral cliIter : clientesRes.getClientesGeneral()){
							    			if(cdIdeperAseg.equalsIgnoreCase(cliIter.getNumeroExterno())){
							    				cliDom = cliIter;
							    			}
							    		}*/
							    		
							    		
							    		String codPosImp = cliDom.getCodposCli();
			                            if(StringUtils.isNotBlank(codPosImp) && codPosImp.length() == 4){
			                                codPosImp = "0"+codPosImp;//Se agrega un cero a la izquierda del codigo postal en caso de que falte
			                            }
			                            
							    		if(cliDom != null){
							    			HashMap<String,String> paramDomicil = new HashMap<String, String>();
							    			paramDomicil.put("pv_cdperson_i", (String)aseg.get("cdperson"));
							    			paramDomicil.put("pv_nmorddom_i", "1");
							    			paramDomicil.put("pv_msdomici_i", cliDom.getCalleCli());
							    			paramDomicil.put("pv_nmtelefo_i", cliDom.getTelefonoCli());
							    			paramDomicil.put("pv_cdpostal_i", codPosImp);
							    			
							    			String edoAdosPos = Integer.toString(cliDom.getEstadoCli());
							    			if(edoAdosPos.length() ==  1){
							    				edoAdosPos = "0"+edoAdosPos;
							    			}
							    			
							    			HashMap<String,String> paramsMunCol = new HashMap<String, String>();
				                            paramsMunCol.put("pv_cdpostal_i", codPosImp);
				                            paramsMunCol.put("pv_cdedo_i",    edoAdosPos);
				                            paramsMunCol.put("pv_dsmunici_i", cliDom.getMunicipioCli());
				                            paramsMunCol.put("pv_dscoloni_i", cliDom.getColoniaCli());
				                            
				                            Map<String,String> munycol= personasManager.obtieneMunicipioYcolonia(paramsMunCol);
							    			
							    			paramDomicil.put("pv_cdedo_i",    codPosImp+edoAdosPos);
							    			paramDomicil.put("pv_cdmunici_i", munycol.get("CDMUNICI"));
							    			paramDomicil.put("pv_cdcoloni_i", munycol.get("CDCOLONI"));
							    			paramDomicil.put("pv_nmnumero_i", cliDom.getNumeroCli());
							    			paramDomicil.put("pv_nmnumint_i", null);
							    			paramDomicil.put("pv_cdtipdom_i", "1");
							    			paramDomicil.put("pv_cdusuario_i", usuarioCaptura);
							    			paramDomicil.put("pv_swactivo_i", Constantes.SI);
							    			paramDomicil.put("pv_accion_i", "I");

							    			kernelManager.pMovMdomicil(paramDomicil);
							    			
							    			HashMap<String,String> paramValoper = new HashMap<String, String>();
							    			paramValoper.put("pv_cdunieco", "0");
							    			paramValoper.put("pv_cdramo",   "0");
							    			paramValoper.put("pv_estado",   null);
							    			paramValoper.put("pv_nmpoliza", "0");
							    			paramValoper.put("pv_nmsituac", null);
							    			paramValoper.put("pv_nmsuplem", null);
							    			paramValoper.put("pv_status",   null);
							    			paramValoper.put("pv_cdrol",    "1");
							    			paramValoper.put("pv_cdperson", (String)aseg.get("cdperson"));
							    			paramValoper.put("pv_cdatribu", null);
							    			paramValoper.put("pv_cdtipsit", null);
							    			
							    			paramValoper.put("pv_otvalor01", cliDom.getCveEle());
							    			paramValoper.put("pv_otvalor02", cliDom.getPasaporteCli());
							    			paramValoper.put("pv_otvalor08", cliDom.getOrirecCli());
							    			paramValoper.put("pv_otvalor11", cliDom.getNacCli());
							    			paramValoper.put("pv_otvalor20", (cliDom.getOcuPro() > 0) ? Integer.toString(cliDom.getOcuPro()) : "0");
							    			paramValoper.put("pv_otvalor25", cliDom.getCurpCli());
							    			paramValoper.put("pv_otvalor38", cliDom.getTelefonoCli());
							    			paramValoper.put("pv_otvalor39", cliDom.getMailCli());
							    			
							    			paramValoper.put("pv_otvalor51", cliDom.getFaxCli());
							    			paramValoper.put("pv_otvalor52", cliDom.getCelularCli());
							    			
							    			kernelManager.pMovTvaloper(paramValoper);
							    			
							    		}else{
							    			logger.error("Error. Cliente no encontrado en WS !");
							    		}
							    	}else{
							    		logger.error("Error, No se pudo obtener el domicilio del WS.");
							    	}
							    	
								}else{
									logger.debug("Ya Existe el domicilio del cdiper.");
								}
							}else{
								logger.error("Error al verificar si hay datos en mdomicil!!");
							}
						}catch(Exception eWS){
							logger.error("Error en obtencion de Domicilio para contratante.",eWS);
						}
					}
					
				}
			}
		}
		
		logger.debug("slist1: "+slist1);
		logger.debug(""
				+ "\n###### guardarSituacionesAuto ######"
				+ "\n####################################"
				);
		return SUCCESS;
	}
	
	public String pantallaCotizacionGrupo2()
	{
		logger.debug(Utils.log(
				 "\n######################################"
				,"\n###### pantallaCotizacionGrupo2 ######"
				,"\n###### smap1=" , smap1
				,"\n###### flujo=" , flujo
				));
		
		String cdramo         = null
		       ,cdtipsit      = null
		       ,ntramite      = null
		       ,ntramiteVacio = null
		       ,status        = null
		       ,cdusuari      = null
		       ,cdsisrol      = null
		       ,nombreUsuario = null
		       ,cdagente      = null
		       ,paso          = null
		       ,result        = ERROR;
		
		//datos completos
		try
		{
			try
			{
			    if (smap1 != null && "S".equals(smap1.get("rstn")) && StringUtils.isNotBlank(smap1.get("ntramiteRstn"))) {
			        String pasoRstn = "Construyendo flujo RSTN";
			        try {
	                    UserVO user = Utils.validateSession(session);
	                    String ntramiteRstn = smap1.get("ntramiteRstn");
	                    Utils.validate(ntramiteRstn, "Falta ntramiteRstn");
	                    String cdsisrolRstn = user.getRolActivo().getClave();
	                    if ("S".equals(smap1.get("emitirRstn"))) {
	                        cdsisrolRstn = "EMITIR";
	                    }
	                    flujo = flujoMesaControlManager.generarYRecuperarFlujoRSTN(ntramiteRstn, user.getUser(), cdsisrolRstn);
	                    smap1 = null;
	                } catch (Exception ex) {
	                    Utils.generaExcepcion(ex, pasoRstn);
	                }
			    }
			    
				if(flujo!=null)
				{
					paso = "Recuperando datos del flujo";
					logger.debug(Utils.log("", "paso=", paso));
					
					smap1 = new HashMap<String,String>();
					smap1.put("cdunieco" , flujo.getCdunieco());
					smap1.put("cdramo"   , flujo.getCdramo());
					
					Map<String,Object> datosFlujo = flujoMesaControlManager.recuperarDatosTramiteValidacionCliente(flujo);
					
					Map<String,String> tramite = (Map<String,String>)datosFlujo.get("TRAMITE");
					
					String nmsolici = tramite.get("NMSOLICI");
					if(StringUtils.isBlank(nmsolici))
					{
						nmsolici = "0";
					}
					
					smap1.put("cdtipsit" , tramite.get("CDTIPSIT"));
					smap1.put("estado"   , flujo.getEstado());
					smap1.put("ntramite" , flujo.getNtramite());
					smap1.put("cdagente" , tramite.get("CDAGENTE"));
					smap1.put("status"   , flujo.getStatus());
					smap1.put("sincenso" , tramite.get("OTVALOR02"));
					smap1.put("cdtipsup" , tramite.get("CDTIPSUP"));
					
					if ("M".equals(flujo.getEstado())) {
						smap1.put("nmpoliza" , flujo.getNmpoliza());
					} else if(Integer.parseInt(nmsolici) > 0) {
						smap1.put("nmpoliza" , nmsolici);
					} else {
						smap1.put("nmpoliza" , "");
						smap1.put("ntramite" , "");
						smap1.put("ntramiteVacio" , flujo.getNtramite());
					}
					
                    if(tramite.get("CDTIPSUP").equalsIgnoreCase(TipoEndoso.RENOVACION.getCdTipSup().toString())){
                        PolizaAseguradoVO polizaAseguradoVO = new PolizaAseguradoVO();
                        polizaAseguradoVO.setCdunieco(smap1.get("cdunieco"));
                        polizaAseguradoVO.setCdramo(smap1.get("cdramo"));
                        polizaAseguradoVO.setEstado(smap1.get("estado"));
                        polizaAseguradoVO.setNmpoliza(smap1.get("nmpoliza"));
                        List<PolizaDTO> lista = consultasPolizaManager.obtieneDatosPoliza(polizaAseguradoVO);
                        if (lista != null && !lista.isEmpty()) {
                            smap1.put("nmpolant" , lista.get(0).getNmpolant());
                            consultasManager.copiarArchivosRenovacionColectivo(smap1.get("cdunieco"), smap1.get("cdramo"), "M", Integer.parseInt(lista.get(0).getNmpolant().substring(7,13))+"", 
                                    tramite.get("NTRAMITE"), this.rutaDocumentosPoliza);
                        }else{
                            smap1.put("nmpolant" , "");
                        }
                    }else{
                        smap1.put("nmpolant" , "");
                    }
                    logger.debug(Utils.log("","datos recuperados del flujo smap1=",smap1));
				}
				
				paso = "Verificando datos completos";
				logger.debug(Utils.log("", "paso=", paso));
				
				Utils.validate(smap1, "No se recibieron datos");
				
				cdramo   = smap1.get("cdramo");
				cdtipsit = smap1.get("cdtipsit");
				
				Utils.validate(
						cdramo    , "No hay cdramo"
						,cdtipsit , "No hay cdtipsit"
						);
				
				ntramite      = smap1.get("ntramite");
				ntramiteVacio = smap1.get("ntramiteVacio");
				status        = smap1.get("status");
				cdagente      = smap1.get("cdagente");
				
				if(StringUtils.isBlank(status))
				{
					status = "0";
				}
				
				paso = "Verificando datos de sesi\u00f3n";
				logger.debug(Utils.log("", "paso=", paso));
				
				UserVO usuario = Utils.validateSession(session);
				
				cdusuari      = usuario.getUser();
				cdsisrol      = usuario.getRolActivo().getClave();
				nombreUsuario = usuario.getName();
				
				if(RolSistema.SUSCRIPTOR_TECNICO_ESPECIALISTA.getCdsisrol().equals(cdsisrol))
				{
					cdsisrol = RolSistema.SUSCRIPTOR_TECNICO.getCdsisrol();
				}
				
				smap1.put("cdsisrol" , cdsisrol);
				smap1.put("cdusuari" , cdusuari);
				
				if ((RolSistema.AGENTE.getCdsisrol().equals(cdsisrol)
						||RolSistema.EJECUTIVO_INTERNO.getCdsisrol().equals(cdsisrol)
						||RolSistema.MESA_DE_CONTROL.getCdsisrol().equals(cdsisrol))
						&& "17".equals(status)) {
					status = "24"; // devolucion
					smap1.put("status", status);
				}
				
				paso = "Invocando proceso";
				logger.debug(Utils.log("", "paso=", paso));
				
				ManagerRespuestaImapSmapVO resp = cotizacionManager.pantallaCotizacionGrupo(
						cdramo
						,cdtipsit
						,ntramite
						,ntramiteVacio
						,status
						,cdusuari
						,cdsisrol
						,nombreUsuario
						,cdagente
						);
				exito           = resp.isExito();
				respuesta       = resp.getRespuesta();
				
				if(!exito)
				{
					throw new ApplicationException(respuesta);
				}
				
				smap1.putAll(resp.getSmap());
				imap=resp.getImap();
				
				result = SUCCESS;
			}
			catch(Exception ex)
			{
				Utils.generaExcepcion(ex, paso);
			}
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(""
				,"\n###### result="    , result
				,"\n###### respuesta=" , respuesta
				,"\n###### pantallaCotizacionGrupo2 ######"
				,"\n######################################"
				));
		return result;
	}
	
	public String pantallaCotizacionGrupo()
	{
		logger.debug(Utils.log(""
				,"\n#####################################"
				,"\n###### pantallaCotizacionGrupo ######"
				,"\n###### smap1=", smap1
				,"\n###### flujo=", flujo
				));
		
		imap = new HashMap<String,Item>();
		
		String cdramo         = null
		       ,cdtipsit      = null
		       ,ntramite      = null
		       ,ntramiteVacio = null
		       ,status        = null
		       ,cdusuari      = null
		       ,cdsisrol      = null
		       ,nombreUsuario = null
		       ,nombreAgente  = null
		       ,cdAgente      = null
		       ,paso          = null
		       ,result        = ERROR;
		
		try
		{
			try
			{
				if(flujo!=null)
				{
					paso = "Recuperando datos del flujo";
					logger.debug(Utils.log("", "paso=", paso));
					
					smap1 = new HashMap<String,String>();
					smap1.put("cdunieco" , flujo.getCdunieco());
					smap1.put("cdramo"   , flujo.getCdramo());
					
					Map<String,Object> datosFlujo = flujoMesaControlManager.recuperarDatosTramiteValidacionCliente(flujo);
					logger.debug("Valor de recuperacion ==> :{}",datosFlujo);
					
					
					Map<String,String> tramite = (Map<String,String>)datosFlujo.get("TRAMITE");
					logger.debug("Valor de tramite ==> :{}",tramite);
					
					
					
					String nmsolici = tramite.get("NMSOLICI");
					if(StringUtils.isBlank(nmsolici))
					{
						nmsolici = "0";
					}
					
					smap1.put("cdtipsit" , tramite.get("CDTIPSIT"));
					smap1.put("estado"   , flujo.getEstado());
					smap1.put("ntramite" , flujo.getNtramite());
					smap1.put("cdagente" , tramite.get("CDAGENTE"));
					smap1.put("status"   , flujo.getStatus());
					smap1.put("sincenso" , tramite.get("OTVALOR02"));
					smap1.put("cdtipsup" , tramite.get("CDTIPSUP"));
					
					if ("M".equals(flujo.getEstado())) {
						smap1.put("nmpoliza" , flujo.getNmpoliza());
					} else if (Integer.parseInt(nmsolici) > 0) {
						smap1.put("nmpoliza" , nmsolici);
					} else {
						smap1.put("nmpoliza" , "");
						smap1.put("ntramite" , "");
						smap1.put("ntramiteVacio" , flujo.getNtramite());
					}
					
					if(tramite.get("CDTIPSUP").equalsIgnoreCase(TipoEndoso.RENOVACION.getCdTipSup().toString())){
						PolizaAseguradoVO polizaAseguradoVO = new PolizaAseguradoVO();
						polizaAseguradoVO.setCdunieco(smap1.get("cdunieco"));
						polizaAseguradoVO.setCdramo(smap1.get("cdramo"));
						polizaAseguradoVO.setEstado(smap1.get("estado"));
						polizaAseguradoVO.setNmpoliza(smap1.get("nmpoliza"));
						List<PolizaDTO> lista = consultasPolizaManager.obtieneDatosPoliza(polizaAseguradoVO);
						if (lista != null && !lista.isEmpty()) {
							smap1.put("nmpolant" , lista.get(0).getNmpolant());
							consultasManager.copiarArchivosRenovacionColectivo(smap1.get("cdunieco"), smap1.get("cdramo"), "M", Integer.parseInt(lista.get(0).getNmpolant().substring(7,13))+"", 
                                    tramite.get("NTRAMITE"), this.rutaDocumentosPoliza);
						}else{
							smap1.put("nmpolant" , "");
						}
					}else{
						smap1.put("nmpolant" , "");
					}
					logger.debug(Utils.log("","datos recuperados del flujo smap1=",smap1));
				}
				
				paso = "Verificando datos completos";
				logger.debug(Utils.log("", "paso=", paso));
				
				Utils.validate(smap1, "No se recibieron datos");
				
				cdramo   = smap1.get("cdramo");
				cdtipsit = smap1.get("cdtipsit");
				
				Utils.validate(
						cdramo    , "No hay cdramo"
						,cdtipsit , "No hay cdtipsit"
						);
				
				ntramite      = smap1.get("ntramite");
				ntramiteVacio = smap1.get("ntramiteVacio");
				status        = smap1.get("status");
				if(StringUtils.isBlank(status))
				{
					status = "0";
				}
				
				//datos sesion
				paso = "Verificando datos de sesi\u00f3n";
				logger.debug(Utils.log("", "paso=", paso));
				UserVO usuario  = Utils.validateSession(session);
				cdusuari        = usuario.getUser();
				cdsisrol        = usuario.getRolActivo().getClave();
				
				if(RolSistema.SUSCRIPTOR_TECNICO_ESPECIALISTA.getCdsisrol().equals(cdsisrol))
				{
					cdsisrol = RolSistema.SUSCRIPTOR_TECNICO.getCdsisrol();
				}
				
				nombreUsuario   = usuario.getName();
				
				smap1.put("cdsisrol" , cdsisrol);
				smap1.put("cdusuari" , cdusuari);
				
				if ((RolSistema.AGENTE.getCdsisrol().equals(cdsisrol)
						||RolSistema.EJECUTIVO_INTERNO.getCdsisrol().equals(cdsisrol)
						||RolSistema.MESA_DE_CONTROL.getCdsisrol().equals(cdsisrol))
						&& "17".equals(status)) {
					status = "24"; // devolucion
					smap1.put("status", status);
				}
				
				//si entran por agente
				paso = "Recuperando datos del agente";
				logger.debug(Utils.log("", "paso=", paso));
				if(StringUtils.isBlank(ntramite)&&StringUtils.isBlank(ntramiteVacio))
				{
					DatosUsuario datUsu = kernelManager.obtenerDatosUsuario(cdusuari,cdtipsit);
					
	        		String cdunieco = datUsu.getCdunieco();
	        		
	        		smap1.put("cdunieco",cdunieco);
	        		
	        		cdAgente     = datUsu.getCdagente();
	        		nombreAgente = nombreUsuario;
				}
				//si entran por tramite o tramite vacio
				else if(StringUtils.isNotBlank(ntramite)||StringUtils.isNotBlank(ntramiteVacio))
				{
					cdAgente     = smap1.get("cdagente");
					nombreAgente = cotizacionManager.cargarNombreAgenteTramite(StringUtils.isNotBlank(ntramite)?ntramite:ntramiteVacio);
				}
				
				//generando componentes
				paso = "Generando componentes";
				logger.debug(Utils.log("", "paso=", paso));
				GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
				
				List<ComponenteVO>columnaEditorPlan=pantallasManager.obtenerComponentes(
						null, null, null,
						null, null, null,
						"COTIZACION_GRUPO", "EDITOR_PLANES", null);
				gc.generaComponentes(columnaEditorPlan, true, false, false, true, true, false);
				imap.put("editorPlanesColumn",gc.getColumns());
				
				List<ComponenteVO>columnaEditorSumaAseg=pantallasManager.obtenerComponentes(
						null, null, cdramo,
						null, null, null,
						"COTIZACION_GRUPO", "EDITOR_SUMA_ASEG", null);
				gc.generaComponentes(columnaEditorSumaAseg, true, false, false, true, true, false);
				imap.put("editorSumaAsegColumn",gc.getColumns());
				
				List<ComponenteVO>columnaEditorPAquete=pantallasManager.obtenerComponentes(
						null, null, null,
						null, null, null,
						"COTIZACION_GRUPO", "EDITOR_PAQUETE", null);
				gc.generaComponentes(columnaEditorPAquete, true, false, false, true, true, false);
				imap.put("editorPaqueteColumn",gc.getColumns());
				
				List<ComponenteVO>columnaEditorAyudaMater=pantallasManager.obtenerComponentes(
						null, null, null,
						null, null, null,
						"COTIZACION_GRUPO", "EDITOR_AYUDAMATER", null);
				gc.generaComponentes(columnaEditorAyudaMater, true, false, false, true, true, false);
				imap.put("editorAyudaMaterColumn",gc.getColumns());
				
				List<ComponenteVO>columnaEditorAsisInterMater=pantallasManager.obtenerComponentes(
						null, null, null,
						null, null, null,
						"COTIZACION_GRUPO", "EDITOR_ASISINTE", null);
				gc.generaComponentes(columnaEditorAsisInterMater, true, false, false, true, true, false);
				imap.put("editorAsisInterColumn",gc.getColumns());
				
				List<ComponenteVO>columnaEditorEmerextr=pantallasManager.obtenerComponentes(
						null, null, null,
						null, null, null,
						"COTIZACION_GRUPO", "EDITOR_EMEREXTR", null);
				gc.generaComponentes(columnaEditorEmerextr, true, false, false, true, true, false);
				imap.put("editorEmerextrColumn",gc.getColumns());
				
				List<ComponenteVO>columnaEditorDeducible=pantallasManager.obtenerComponentes(
						null, null, null,
						null, null, null,
						"COTIZACION_GRUPO", "EDITOR_DEDUCIBLE", null);
				gc.generaComponentes(columnaEditorDeducible, true, false, false, true, true, false);
				imap.put("editorDeducibleColumn",gc.getColumns());
				
				List<ComponenteVO>componentesContratante=pantallasManager.obtenerComponentes(
						null               , "|"+cdramo+"|" , "|"+status+"|" ,
						null               , null           , cdsisrol       ,
						"COTIZACION_GRUPO" , "CONTRATANTE"  , null);
				gc.generaComponentes(componentesContratante, true,true,true,false,false,false);
				imap.put("itemsContratante"  , gc.getItems());
				imap.put("fieldsContratante" , gc.getFields());
				
				List<ComponenteVO>componentesRiesgo=pantallasManager.obtenerComponentes(
						null, null, null,
						null, null, cdsisrol,
						"COTIZACION_GRUPO", "RIESGO", null);
				gc.generaComponentes(componentesRiesgo, true,true,true,false,false,false);
				imap.put("itemsRiesgo"  , gc.getItems());
				imap.put("fieldsRiesgo" , gc.getFields());
				
				List<ComponenteVO>componentesAgente=pantallasManager.obtenerComponentes(
						null, null, null,
						null, null, null,
						"COTIZACION_GRUPO", "AGENTE", null);
				componentesAgente.get(0).setDefaultValue(nombreAgente);
				componentesAgente.get(1).setDefaultValue(cdAgente);
				gc.generaComponentes(componentesAgente, true,false,true,false,false,false);
				imap.put("itemsAgente"  , gc.getItems());
				
				List<ComponenteVO>comboFormaPago=pantallasManager.obtenerComponentes(
						null, null, null,
						null, null, null,
						"COTIZACION_GRUPO", "COMBO_FORMA_PAGO", null);
				gc.generaComponentes(comboFormaPago, true,false,true,false,false,false);
				imap.put("comboFormaPago"  , gc.getItems());
				
				List<ComponenteVO>comboRepartoPago=pantallasManager.obtenerComponentes(
						null, null, null,
						null, null, null,
						"COTIZACION_GRUPO", "COMBO_REPARTO_PAGO", null);
				gc.generaComponentes(comboRepartoPago, true,false,true,false,false,false);
				imap.put("comboRepartoPago"  , gc.getItems());
				
				List<ComponenteVO>comboPool = pantallasManager.obtenerComponentes(
						null, null, null,
						null, null, cdsisrol,
						"COTIZACION_GRUPO", "COMBO_POOL", null);
				gc.generaComponentes(comboPool, true,false,true,false,false,false);
				imap.put("comboPool"  , gc.getItems());
				
				List<ComponenteVO>datosPoliza = pantallasManager.obtenerComponentes(
						null, null, null,
						null, null, cdsisrol,
						"COTIZACION_GRUPO", "DATOS_POLIZA", null);
				gc.generaComponentes(datosPoliza, true,false,true,false,false,false);
				imap.put("datosPoliza"  , gc.getItems());
				
				List<ComponenteVO>botones=pantallasManager.obtenerComponentes(
						null, null, "|"+status+"|",
						null, null, cdsisrol,
						"COTIZACION_GRUPO", "BOTONES", null);
				if(botones!=null&&botones.size()>0)
				{
					gc.generaComponentes(botones, true, false, false, false, false, true);
					imap.put("botones" , gc.getButtons());
				}
				else
				{
					imap.put("botones" , null);
				}
				
				//obtener permisos
				paso = "Recuperando persmisos de pantalla";
				logger.debug(Utils.log("", "paso=", paso));
				smap1.put("status",status);
				smap1.putAll(cotizacionManager.cargarPermisosPantallaGrupo(cdsisrol, status));
				
				//campos para asegurados
				paso = "Recuperando campos de asegurados";
				logger.debug(Utils.log("", "paso=", paso));
				if(smap1.containsKey("ASEGURADOS")
						&& StringUtils.isNotBlank(smap1.get("ASEGURADOS"))
						&& smap1.get("ASEGURADOS").equals("S")
				)
				{
					List<ComponenteVO>componentesExtraprimas=pantallasManager.obtenerComponentes(
							null  , null , null
							,cdtipsit , null , cdsisrol
							,"COTIZACION_GRUPO", "ASEGURADOS", null);
					gc.generaComponentes(componentesExtraprimas, true, true, false, true, false, false);
					imap.put("aseguradosColumns" , gc.getColumns());
					imap.put("aseguradosFields"  , gc.getFields());
				}
				
				//campos para extraprimas
				paso = "Recuperando campos de extraprimas";
				logger.debug(Utils.log("", "paso=", paso));
				if(smap1.containsKey("EXTRAPRIMAS")
						&& StringUtils.isNotBlank(smap1.get("EXTRAPRIMAS"))
						&& smap1.get("EXTRAPRIMAS").equals("S")
				)
				{
					List<ComponenteVO>componentesExtraprimas=pantallasManager.obtenerComponentes(
							null  , null , null
							,null , null , cdsisrol
							,"COTIZACION_GRUPO", "EXTRAPRIMAS", null);
					gc.generaComponentes(componentesExtraprimas, true, true, false, true, true, false);
					imap.put("extraprimasColumns" , gc.getColumns());
					imap.put("extraprimasFields"  , gc.getFields());
				}
				
				//campos para recuperados (asegurados)
				paso = "Recuperando campos de asegurados recuperados";
				logger.debug(Utils.log("", "paso=", paso));
				if(smap1.containsKey("ASEGURADOS_EDITAR")
						&& StringUtils.isNotBlank(smap1.get("ASEGURADOS_EDITAR"))
						&& smap1.get("ASEGURADOS_EDITAR").equals("S")
				)
				{
					List<ComponenteVO>componentesRecuperados=pantallasManager.obtenerComponentes(
							null  , null , null
							,null , null , cdsisrol
							,"COTIZACION_GRUPO", "RECUPERADOS", null);
					gc.generaComponentes(componentesRecuperados, true, true, false, true, true, false);
					imap.put("recuperadosColumns" , gc.getColumns());
					imap.put("recuperadosFields"  , gc.getFields());
				}
				
				try
				{
					smap1.put("customCode" , consultasManager.recuperarCodigoCustom("21", cdsisrol));
				}
				catch(Exception ex)
				{
					smap1.put("customCode" , "/* error */");
					logger.error("Error sin impacto funcional al recuperar codigo custom",ex);
				}
				
				/////////////////
				result = SUCCESS;
			}
			catch(Exception ex)
			{
				Utils.generaExcepcion(ex, paso);
			}
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(""
				,"\n###### result="    , result
				,"\n###### respuesta=" , respuesta
				,"\n###### pantallaCotizacionGrupo ######"
				,"\n#####################################"
				));
		return result;
	}
	
	public String pantallaCotizacionGrupoEndoso()
	{
		logger.debug(Utils.log(""
				,"\n###########################################"
				,"\n###### pantallaCotizacionGrupoEndoso ######"
				,"\n###### smap1=", smap1
				,"\n###### flujo=", flujo
				));
		
		imap = new HashMap<String,Item>();
		
		String cdramo         = null
		       ,cdtipsit      = null
		       ,ntramite      = null
		       ,ntramiteVacio = null
		       ,status        = null
		       ,cdusuari      = null
		       ,cdsisrol      = null
		       ,nombreUsuario = null
		       ,nombreAgente  = null
		       ,cdAgente      = null
		       ,paso          = null
		       ,result        = ERROR;
		
		try
		{
			try
			{
				if(flujo!=null)
				{
					paso = "Recuperando datos del flujo";
					logger.debug(Utils.log("", "paso=", paso));
					
					smap1 = new HashMap<String,String>();
					smap1.put("cdunieco" , flujo.getCdunieco());
					smap1.put("cdramo"   , flujo.getCdramo());
					
					Map<String,Object> datosFlujo = flujoMesaControlManager.recuperarDatosTramiteValidacionCliente(flujo);
					
					Map<String,String> tramite = (Map<String,String>)datosFlujo.get("TRAMITE");
					
					String nmsolici = tramite.get("NMSOLICI");
					if(StringUtils.isBlank(nmsolici))
					{
						nmsolici = "0";
					}
					
					smap1.put("cdtipsit" , tramite.get("CDTIPSIT"));
					smap1.put("estado"   , flujo.getEstado());
					smap1.put("ntramite" , flujo.getNtramite());
					smap1.put("cdagente" , tramite.get("CDAGENTE"));
					smap1.put("status"   , flujo.getStatus());
					smap1.put("sincenso" , tramite.get("OTVALOR02"));
					
					if(Integer.parseInt(nmsolici)>0)
					{
						smap1.put("nmpoliza" , nmsolici);
					}
					else
					{
						smap1.put("nmpoliza" , "");
						smap1.put("ntramite" , "");
						smap1.put("ntramiteVacio" , flujo.getNtramite());
					}
					
					logger.debug(Utils.log("","datos recuperados del flujo smap1=",smap1));
				}
				
				paso = "Verificando datos completos";
				logger.debug(Utils.log("", "paso=", paso));
				
				Utils.validate(smap1, "No se recibieron datos");
				
				cdramo   = smap1.get("cdramo");
				cdtipsit = smap1.get("cdtipsit");
				
				Utils.validate(
						cdramo    , "No hay cdramo"
						,cdtipsit , "No hay cdtipsit"
						);
				
				ntramite      = smap1.get("ntramite");
				ntramiteVacio = smap1.get("ntramiteVacio");
				status        = smap1.get("status");
				if(StringUtils.isBlank(status))
				{
					status = "0";
				}
				
				//datos sesion
				paso = "Verificando datos de sesi\u00f3n";
				logger.debug(Utils.log("", "paso=", paso));
				UserVO usuario  = Utils.validateSession(session);
				cdusuari        = usuario.getUser();
				cdsisrol        = usuario.getRolActivo().getClave();
				
				if(RolSistema.SUSCRIPTOR_TECNICO_ESPECIALISTA.getCdsisrol().equals(cdsisrol))
				{
					cdsisrol = RolSistema.SUSCRIPTOR_TECNICO.getCdsisrol();
				}
				
				nombreUsuario   = usuario.getName();
				
				smap1.put("cdsisrol" , cdsisrol);
				smap1.put("cdusuari" , cdusuari);
				
				//si entran por agente
				paso = "Recuperando datos del agente";
				logger.debug(Utils.log("", "paso=", paso));
				if(StringUtils.isBlank(ntramite)&&StringUtils.isBlank(ntramiteVacio))
				{
					DatosUsuario datUsu = kernelManager.obtenerDatosUsuario(cdusuari,cdtipsit);
					
	        		String cdunieco = datUsu.getCdunieco();
	        		
	        		smap1.put("cdunieco",cdunieco);
	        		
	        		cdAgente     = datUsu.getCdagente();
	        		nombreAgente = nombreUsuario;
				}
				//si entran por tramite o tramite vacio
				else if(StringUtils.isNotBlank(ntramite)||StringUtils.isNotBlank(ntramiteVacio))
				{
					cdAgente     = smap1.get("cdagente");
					nombreAgente = cotizacionManager.cargarNombreAgenteTramite(StringUtils.isNotBlank(ntramite)?ntramite:ntramiteVacio);
				}
				
				//generando componentes
				paso = "Generando componentes";
				logger.debug(Utils.log("", "paso=", paso));
				GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
				
				List<ComponenteVO>columnaEditorPlan=pantallasManager.obtenerComponentes(
						null, null, null,
						null, null, null,
						"COTIZACION_GRUPO", "EDITOR_PLANES", null);
				gc.generaComponentes(columnaEditorPlan, true, false, false, true, true, false);
				imap.put("editorPlanesColumn",gc.getColumns());
				
				List<ComponenteVO>columnaEditorSumaAseg=pantallasManager.obtenerComponentes(
						null, null, cdramo,
						null, null, null,
						"COTIZACION_GRUPO", "EDITOR_SUMA_ASEG", null);
				gc.generaComponentes(columnaEditorSumaAseg, true, false, false, true, true, false);
				imap.put("editorSumaAsegColumn",gc.getColumns());
				
				List<ComponenteVO>columnaEditorPAquete=pantallasManager.obtenerComponentes(
						null, null, null,
						null, null, null,
						"COTIZACION_GRUPO", "EDITOR_PAQUETE", null);
				gc.generaComponentes(columnaEditorPAquete, true, false, false, true, true, false);
				imap.put("editorPaqueteColumn",gc.getColumns());
				
				List<ComponenteVO>columnaEditorAyudaMater=pantallasManager.obtenerComponentes(
						null, null, null,
						null, null, null,
						"COTIZACION_GRUPO", "EDITOR_AYUDAMATER", null);
				gc.generaComponentes(columnaEditorAyudaMater, true, false, false, true, true, false);
				imap.put("editorAyudaMaterColumn",gc.getColumns());
				
				List<ComponenteVO>columnaEditorAsisInterMater=pantallasManager.obtenerComponentes(
						null, null, null,
						null, null, null,
						"COTIZACION_GRUPO", "EDITOR_ASISINTE", null);
				gc.generaComponentes(columnaEditorAsisInterMater, true, false, false, true, true, false);
				imap.put("editorAsisInterColumn",gc.getColumns());
				
				List<ComponenteVO>columnaEditorEmerextr=pantallasManager.obtenerComponentes(
						null, null, null,
						null, null, null,
						"COTIZACION_GRUPO", "EDITOR_EMEREXTR", null);
				gc.generaComponentes(columnaEditorEmerextr, true, false, false, true, true, false);
				imap.put("editorEmerextrColumn",gc.getColumns());
				
				List<ComponenteVO>columnaEditorDeducible=pantallasManager.obtenerComponentes(
						null, null, null,
						null, null, null,
						"COTIZACION_GRUPO", "EDITOR_DEDUCIBLE", null);
				gc.generaComponentes(columnaEditorDeducible, true, false, false, true, true, false);
				imap.put("editorDeducibleColumn",gc.getColumns());
				
				List<ComponenteVO>componentesContratante=pantallasManager.obtenerComponentes(
						null               , "|"+cdramo+"|" , "|"+status+"|" ,
						null               , null           , cdsisrol       ,
						"COTIZACION_GRUPO" , "CONTRATANTEEND"  , null);
				gc.generaComponentes(componentesContratante, true,true,true,false,false,false);
				imap.put("itemsContratante"  , gc.getItems());
				imap.put("fieldsContratante" , gc.getFields());
				
				List<ComponenteVO>componentesRiesgo=pantallasManager.obtenerComponentes(
						null, null, null,
						null, null, cdsisrol,
						"COTIZACION_GRUPO", "RIESGOEND", null);
				gc.generaComponentes(componentesRiesgo, true,true,true,false,false,false);
				imap.put("itemsRiesgo"  , gc.getItems());
				imap.put("fieldsRiesgo" , gc.getFields());
				
				List<ComponenteVO>componentesAgente=pantallasManager.obtenerComponentes(
						null, null, null,
						null, null, null,
						"COTIZACION_GRUPO", "AGENTE", null);
				componentesAgente.get(0).setDefaultValue(nombreAgente);
				componentesAgente.get(1).setDefaultValue(cdAgente);
				gc.generaComponentes(componentesAgente, true,false,true,false,false,false);
				imap.put("itemsAgente"  , gc.getItems());
				
				List<ComponenteVO>comboFormaPago=pantallasManager.obtenerComponentes(
						null, null, null,
						null, null, null,
						"COTIZACION_GRUPO", "COMBO_FORMA_PAGO", null);
				gc.generaComponentes(comboFormaPago, true,false,true,false,false,false);
				imap.put("comboFormaPago"  , gc.getItems());
				
				List<ComponenteVO>comboRepartoPago=pantallasManager.obtenerComponentes(
						null, null, null,
						null, null, null,
						"COTIZACION_GRUPO", "COMBO_REPARTO_PAGO", null);
				gc.generaComponentes(comboRepartoPago, true,false,true,false,false,false);
				imap.put("comboRepartoPago"  , gc.getItems());
				
				List<ComponenteVO>comboPool = pantallasManager.obtenerComponentes(
						null, null, null,
						null, null, cdsisrol,
						"COTIZACION_GRUPO", "COMBO_POOLEND", null);
				gc.generaComponentes(comboPool, true,false,true,false,false,false);
				imap.put("comboPool"  , gc.getItems());
				
				List<ComponenteVO>datosPoliza = pantallasManager.obtenerComponentes(
						null, null, null,
						null, null, cdsisrol,
						"COTIZACION_GRUPO", "DATOS_POLIZAEND", null);
				gc.generaComponentes(datosPoliza, true,false,true,false,false,false);
				imap.put("datosPoliza"  , gc.getItems());
				
				List<ComponenteVO>botones=pantallasManager.obtenerComponentes(
						null, null, "|"+status+"|",
						null, null, cdsisrol,
						"COTIZACION_GRUPO", "BOTONESEND", null);
				if(botones!=null&&botones.size()>0)
				{
					gc.generaComponentes(botones, true, false, false, false, false, true);
					imap.put("botones" , gc.getButtons());
				}
				else
				{
					imap.put("botones" , null);
				}
				
				//obtener permisos
				paso = "Recuperando persmisos de pantalla";
				logger.debug(Utils.log("", "paso=", paso));
				smap1.put("status",status);
				smap1.putAll(cotizacionManager.cargarPermisosPantallaGrupo(cdsisrol, status));
				
				//campos para asegurados
				paso = "Recuperando campos de asegurados";
				logger.debug(Utils.log("", "paso=", paso));
				if(smap1.containsKey("ASEGURADOS")
						&& StringUtils.isNotBlank(smap1.get("ASEGURADOS"))
						&& smap1.get("ASEGURADOS").equals("S")
				)
				{
					List<ComponenteVO>componentesExtraprimas=pantallasManager.obtenerComponentes(
							null  , null , null
							,cdtipsit , null , cdsisrol
							,"COTIZACION_GRUPO", "ASEGURADOS", null);
					gc.generaComponentes(componentesExtraprimas, true, true, false, true, false, false);
					imap.put("aseguradosColumns" , gc.getColumns());
					imap.put("aseguradosFields"  , gc.getFields());
				}
				
				//campos para extraprimas
				paso = "Recuperando campos de extraprimas";
				logger.debug(Utils.log("", "paso=", paso));
				if(smap1.containsKey("EXTRAPRIMAS")
						&& StringUtils.isNotBlank(smap1.get("EXTRAPRIMAS"))
						&& smap1.get("EXTRAPRIMAS").equals("S")
				)
				{
					List<ComponenteVO>componentesExtraprimas=pantallasManager.obtenerComponentes(
							null  , null , null
							,null , null , cdsisrol
							,"COTIZACION_GRUPO", "EXTRAPRIMAS", null);
					gc.generaComponentes(componentesExtraprimas, true, true, false, true, true, false);
					imap.put("extraprimasColumns" , gc.getColumns());
					imap.put("extraprimasFields"  , gc.getFields());
				}
				
				//campos para recuperados (asegurados)
				paso = "Recuperando campos de asegurados recuperados";
				logger.debug(Utils.log("", "paso=", paso));
				if(smap1.containsKey("ASEGURADOS_EDITAR")
						&& StringUtils.isNotBlank(smap1.get("ASEGURADOS_EDITAR"))
						&& smap1.get("ASEGURADOS_EDITAR").equals("S")
				)
				{
					List<ComponenteVO>componentesRecuperados=pantallasManager.obtenerComponentes(
							null  , null , null
							,null , null , cdsisrol
							,"COTIZACION_GRUPO", "RECUPERADOS", null);
					gc.generaComponentes(componentesRecuperados, true, true, false, true, true, false);
					imap.put("recuperadosColumns" , gc.getColumns());
					imap.put("recuperadosFields"  , gc.getFields());
				}
				
				try
				{
					smap1.put("customCode" , consultasManager.recuperarCodigoCustom("21", cdsisrol));
				}
				catch(Exception ex)
				{
					smap1.put("customCode" , "/* error */");
					logger.error("Error sin impacto funcional al recuperar codigo custom",ex);
				}
				
				/////////////////
				result = SUCCESS;
			}
			catch(Exception ex)
			{
				Utils.generaExcepcion(ex, paso);
			}
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(""
				,"\n###### result="    , result
				,"\n###### respuesta=" , respuesta
				,"\n###### pantallaCotizacionGrupoEndoso ######"
				,"\n###########################################"
				));
		return result;
	}
	
	public String obtenerCoberturasPlan()
	{
		logger.debug(""
				+ "\n###################################"
				+ "\n###### obtenerCoberturasPlan ######"
				+ "\nsmap1: "+smap1
				);
		try
		{
			success = true;
			exito   = true;
			LinkedHashMap<String,Object>params=new LinkedHashMap<String,Object>();
			params.put("param1" , smap1.get("cdramo"));
			params.put("param2" , smap1.get("cdtipsit"));
			params.put("param3" , smap1.get("cdplan"));
			slist1=storedProceduresManager.procedureListCall(ObjetoBD.OBTIENE_COBERTURAS_X_PLAN.getNombre(), params, null);
		}
		catch(Exception ex)
		{
			long timestamp=System.currentTimeMillis();
			logger.error(timestamp+" error al obtener coberturas plan");
			respuesta       = "Error inesperado #"+timestamp;
			respuestaOculta = ex.getMessage();
			exito           = false;
		}
		logger.debug(""
				+ "\n###### obtenerCoberturasPlan ######"
				+ "\n###################################"
				);
		return SUCCESS;
	}
	
	public String obtenerTatrigarCoberturas()
	{
		logger.debug(""
				+ "\n#######################################"
				+ "\n###### obtenerTatrigarCoberturas ######"
				+ "\nsmap1: "+smap1
				);
		try
		{
			success = true;
			exito   = true;
			List<ComponenteVO>componentesTatrigar;
			
			if(TipoEndoso.RENOVACION.getCdTipSup().toString().equalsIgnoreCase(smap1.get("cdtipsup"))){
			    if(smap1.get("cdplan").equalsIgnoreCase(smap1.get("cdplanOrig"))){
			        String nmpolant = smap1.get("nmpolant");
                    Map<String,String>params=new HashMap<String,String>();
                    params.put("pv_cdunieco_i"   , Integer.parseInt(nmpolant.substring(0,4))+"");
                    params.put("pv_cdramo_i"     , smap1.get("cdramo"));
                    params.put("pv_estado_i"     , "M");
                    params.put("pv_nmpoliza_i"   , Integer.parseInt(nmpolant.substring(7,13))+"");
                    params.put("pv_cdgrupo_i"    , smap1.get("cdgrupo"));
                    params.put("pv_cdplan_i"     , smap1.get("cdplan"));
                    params.put("pv_sexo_i"       , "H");
                    params.put("pv_cdtipsit_i"   , smap1.get("cdtipsit"));
                    params.put("pv_cdgarant_i"   , smap1.get("cdgarant"));
                    params.put("pv_cdatrivar_i"  , smap1.get("cdatrivar"));
                    componentesTatrigar=cotizacionManager.obtenerAtributosPolizaOriginal(params);
                    logger.debug("Valor de los componentes recuperados ===> "+componentesTatrigar);
                }
                else{
                    Map<String,String>params=new HashMap<String,String>();
                    params.put("pv_cdramo_i"    , smap1.get("cdramo"));
                    params.put("pv_cdtipsit_i"  , smap1.get("cdtipsit"));
                    params.put("pv_cdgarant_i"  , smap1.get("cdgarant"));
                    params.put("pv_cdatrivar_i" , smap1.get("cdatrivar"));
                    componentesTatrigar=kernelManager.obtenerTatrigar(params);
                    logger.debug("Valor de los componentes recuperados ===> "+componentesTatrigar);
                }
			}else{
			    Map<String,String>params=new HashMap<String,String>();
	            params.put("pv_cdramo_i"    , smap1.get("cdramo"));
	            params.put("pv_cdtipsit_i"  , smap1.get("cdtipsit"));
	            params.put("pv_cdgarant_i"  , smap1.get("cdgarant"));
	            params.put("pv_cdatrivar_i" , smap1.get("cdatrivar"));
	            componentesTatrigar=kernelManager.obtenerTatrigar(params);
			}
			GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
            gc.setCdramo(smap1.get("cdramo"));
            gc.setCdtipsit(smap1.get("cdtipsit"));
            gc.setCdgarant(smap1.get("cdgarant"));
            gc.generaComponentes(componentesTatrigar, false, true, true, false, false, false);
            respuesta       = gc.getItems().toString();
            respuestaOculta = gc.getFields().toString();
		}
		catch(Exception ex)
		{
			long timestamp = System.currentTimeMillis();
			logger.error(timestamp+" error al obtener tatrigar de coberturas",ex);
			respuesta       = "Error inesperado #"+timestamp;
			respuestaOculta = ex.getMessage();
			exito           = false;
		}
		logger.debug(""
				+ "\n###### obtenerTatrigarCoberturas ######"
				+ "\n#######################################"
				);
		return SUCCESS;
	}
	
	public String subirCenso()
	{
		logger.debug(""
				+ "\n########################"
				+ "\n###### subirCenso ######"
				+ "\n censo "+censo+""
				+ "\n censoFileName "+censoFileName+""
				+ "\n censoContentType "+censoContentType+""
				+ "\n smap1 "+smap1
				);
		
		success = true;
		exito   = true;
		
		String ntramite=smap1.get("ntramite");
		if(StringUtils.isBlank(ntramite))
		{
			String timestamp = smap1.get("timestamp");
			//censo.renameTo(new File(this.rutaDocumentosTemporal+"/censo_"+timestamp));
			try {
            	FileUtils.copyFile(censo, new File(this.rutaDocumentosTemporal+"/censo_"+timestamp));
            	logger.debug("archivo movido");
			} catch (Exception e) {
				logger.error("archivo NO movido", e);
			}
			
			logger.debug("censo renamed to: "+this.rutaDocumentosTemporal+"/censo_"+timestamp);
		}
		
		logger.debug(""
				+ "\n###### subirCenso ######"
				+ "\n########################"
				);
		return SUCCESS;
	}
	
	public String subirCensoCompleto2()
	{
		this.session=ActionContext.getContext().getSession();
		logger.debug(
				new StringBuilder()
				.append("\n#################################")
				.append("\n###### subirCensoCompleto2 ######")
				.append("\n###### smap1=").append(smap1)
				.append("\n###### olist1=").append(olist1)
				.toString()
				);
		
		exito   = true;
		success = true;
		
		String censoTimestamp  = null;
		String clasif          = null;
		String LINEA_EXTENDIDA = null;
		String cdunieco        = null;
		String cdramo          = null;
		String cdtipsit        = null;
		String nmpoliza        = null;
		String cdperpag        = null;
		String pcpgocte        = null;
		String cdusuari        = null;
		String cdelemen        = null;
		String rutaDocsTemp    = null;
		String feini           = null;
		String fefin           = null;
		String cdsisrol        = null;
		String cdagente        = null;
		String codpostalCli    = null;
		String cdedoCli        = null;
		String cdmuniciCli     = null;
		String ntramite        = null;
		String ntramiteVacio   = null;
		String cdpersonCli     = null;
		String nombreCli       = null;
		String rfcCli          = null;
		String dsdomiciCli     = null;
		String nmnumeroCli     = null;
		String nmnumintCli     = null;
		String cdideper_       = null;
		String cdideext_       = null;
		String nmpolant        = null;
		String nmrenova        = null;
		String estatuRenovacion= null;
		
		String nombreCensoConfirmado = null;
		
		//datos completos
		try
		{
			if(smap1==null)
			{
				throw new ApplicationException("No se recibieron datos");
			}
			censoTimestamp  = smap1.get("timestamp");
			clasif          = smap1.get("clasif");
			LINEA_EXTENDIDA = smap1.get("LINEA_EXTENDIDA");
			cdunieco        = smap1.get("cdunieco");
			cdramo          = smap1.get("cdramo");
			cdtipsit        = smap1.get("cdtipsit");
			nmpoliza        = smap1.get("nmpoliza");
			cdperpag        = smap1.get("cdperpag");
			pcpgocte        = smap1.get("pcpgocte");
			feini           = smap1.get("feini");
			fefin           = smap1.get("fefin");
			cdagente        = smap1.get("cdagente");
			codpostalCli    = smap1.get("codpostal");
			cdedoCli        = smap1.get("cdedo");
			cdmuniciCli     = smap1.get("cdmunici");
			ntramite        = smap1.get("ntramite");
			ntramiteVacio   = smap1.get("ntramiteVacio");
			cdpersonCli     = smap1.get("cdperson");
			nombreCli       = smap1.get("nombre");
			rfcCli          = smap1.get("cdrfc");
			dsdomiciCli     = smap1.get("dsdomici");
			nmnumeroCli     = smap1.get("nmnumero");
			nmnumintCli     = smap1.get("nmnumint");
			cdideper_       = smap1.get("cdideper_");
			cdideext_       = smap1.get("cdideext_");
			nmpolant        = smap1.get("nmpolant");
			nmrenova        = smap1.get("nmrenova");
			estatuRenovacion= smap1.get("estatusRenovacion");
			
			nombreCensoConfirmado = smap1.get("nombreCensoConfirmado");
			
			if(session==null)
			{
				throw new ApplicationException("No hay sesion");
			}
			if(session.get("USUARIO")==null)
			{
				throw new ApplicationException("No usuario en la sesion");
			}
			UserVO user = (UserVO)session.get("USUARIO");
			cdusuari = user.getUser();
			cdelemen = user.getEmpresa().getElementoId();
			cdsisrol = user.getRolActivo().getClave();
			
			rutaDocsTemp = rutaDocumentosTemporal;
		}
		catch(ApplicationException ax)
		{
			long timestamp  = System.currentTimeMillis();
			exito           = false;
			respuesta       = new StringBuilder(ax.getMessage()).append(" #").append(timestamp).toString();
			respuestaOculta = ax.getMessage();
			logger.error(respuesta,ax);
		}
		catch(Exception ex)
		{
			long timestamp  = System.currentTimeMillis();
			exito           = false;
			respuesta       = new StringBuilder("Error al validar datos #").append(timestamp).toString();
			respuestaOculta = ex.getMessage();
			logger.error(respuesta,ex);
		}
		
		//proceso
		if(exito)
		{
			UserVO usuarioSesion = (UserVO)session.get("USUARIO");
			ManagerRespuestaSmapVO resp =  null;
			
			if(StringUtils.isBlank(nombreCensoConfirmado)){
				 resp = cotizacionManager.subirCensoCompleto(
						cdunieco
						,cdramo
						,nmpoliza
						,feini
						,fefin
						,cdperpag
						,pcpgocte
						,rutaDocsTemp
						,censoTimestamp
						,dominioServerLayouts
						,userServerLayouts
						,passServerLayouts
						,directorioServerLayouts
						,cdtipsit
						,cdusuari
						,cdsisrol
						,cdagente
						,codpostalCli
						,cdedoCli
						,cdmuniciCli
						,olist1
						,clasif
						,LINEA_EXTENDIDA
						,cdpersonCli
						,nombreCli
						,rfcCli
						,dsdomiciCli
						,nmnumeroCli
						,nmnumintCli
						,ntramite
						,ntramiteVacio
						,cdelemen
						,nombreCensoConfirmado
						,cdideper_
						,cdideext_
						,nmpolant
						,nmrenova
						,usuarioSesion
						);
			}else{
				resp = cotizacionManager.confirmarCensoCompleto(
						cdunieco
						,cdramo
						,nmpoliza
						,feini
						,fefin
						,cdperpag
						,pcpgocte
						,rutaDocsTemp
						,censoTimestamp
						,dominioServerLayouts
						,userServerLayouts
						,passServerLayouts
						,directorioServerLayouts
						,cdtipsit
						,cdusuari
						,cdsisrol
						,cdagente
						,codpostalCli
						,cdedoCli
						,cdmuniciCli
						,olist1
						,clasif
						,LINEA_EXTENDIDA
						,cdpersonCli
						,nombreCli
						,rfcCli
						,dsdomiciCli
						,nmnumeroCli
						,nmnumintCli
						,ntramite
						,ntramiteVacio
						,cdelemen
						,nombreCensoConfirmado
						,cdideper_
						,cdideext_
						,nmpolant
						,nmrenova
						,usuarioSesion
						,estatuRenovacion
						);
			}
			
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
				.append("\n###### subirCensoCompleto2 ######")
				.append("\n#################################")
				.toString()
				);
		return SUCCESS;
	}

	public String subirCensoCompleto3() {
		this.session=ActionContext.getContext().getSession();
		logger.debug(Utils.log(
			 "\n#################################"
			,"\n###### subirCensoCompleto3 ######"
			,"\n###### smap1="  , smap1
			,"\n###### olist1=" , olist1
		));
		
		success = true;
		exito   = true;
		
		String censoTimestamp   = smap1.get("timestamp");
		String clasif           = smap1.get("clasif");
		String LINEA_EXTENDIDA  = smap1.get("LINEA_EXTENDIDA");
		String cdunieco         = smap1.get("cdunieco");
		String cdtipsit         = smap1.get("cdtipsit");
		String cdramo           = smap1.get("cdramo");
		String nmpoliza         = smap1.get("nmpoliza");
		String cdperpag         = smap1.get("cdperpag");
		String pcpgocte         = smap1.get("pcpgocte");
		UserVO usuario          = (UserVO)session.get("USUARIO");
		String user             = usuario.getUser();
		String cdelemento       = usuario.getEmpresa().getElementoId();
		String cdsisrol         = usuario.getRolActivo().getClave();
		final String LINEA      = "1";
		String ntramite         = smap1.get("ntramite");
		boolean hayTramite      = StringUtils.isNotBlank(ntramite);
		String ntramiteVacio    = smap1.get("ntramiteVacio");
		boolean hayTramiteVacio = StringUtils.isNotBlank(ntramiteVacio);
		Date fechaHoy           = new Date();
		String feini            = smap1.get("feini");
		String fefin            = smap1.get("fefin");
		String nmpolant         = smap1.get("nmpolant");
		String nmrenova         = smap1.get("nmrenova");
		String estado           = smap1.get("estado");
		String nmsuplem         = smap1.get("nmsuplem");
		
		censo = new File(this.rutaDocumentosTemporal+"/censo_"+censoTimestamp);
		
		String nombreCensoConfirmado = smap1.get("nombreCensoConfirmado");
		boolean pagoRepartido = false;
		if(exito){
			try{
				pagoRepartido = cotizacionManager.validaPagoPolizaRepartido(cdunieco,cdramo,estado,nmpoliza);
			} catch(Exception ex){
				respuesta       = Utils.join("Error al recuperar reparto de pago #",System.currentTimeMillis());
				respuestaOculta = ex.getMessage();
				exito           = false;
				logger.error(respuesta,ex);
			}
		}
		
		boolean pideNumCliemte = false;
		if(exito){
			try{
				pideNumCliemte = consultasManager.validaClientePideNumeroEmpleado(cdunieco,cdramo,estado,nmpoliza);
			} catch(Exception ex){
				respuesta       = Utils.join("Error al recuperar parametrizacion de numero de empleado por cliente");
				respuestaOculta = ex.getMessage();
				exito           = false;
				logger.error(respuesta,ex);
			}
		}
		
		String nombreCenso = null;
		
		if(exito&&StringUtils.isBlank(nombreCensoConfirmado)){
			FileInputStream input       = null;
			Workbook        workbook    = null;
			Sheet           sheet       = null;
			Long            inTimestamp = null;
			File            archivoTxt  = null;
			PrintStream     output      = null;
			
			try{
				input       = new FileInputStream(censo);
				workbook    = WorkbookFactory.create(input);
				sheet       = workbook.getSheetAt(0);
				inTimestamp = System.currentTimeMillis();
				nombreCenso = "censo_"+inTimestamp+"_"+nmpoliza+".txt";
				archivoTxt  = new File(this.rutaDocumentosTemporal+"/"+nombreCenso);
				output      = new PrintStream(archivoTxt);
			} catch(Exception ex){
				long etimestamp = System.currentTimeMillis();
				exito           = false;
				respuesta       = "Error al procesar censo #"+etimestamp;
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
			
			if(exito&&workbook.getNumberOfSheets()!=1) {
				long etimestamp = System.currentTimeMillis();
				exito           = false;
				respuesta       = "Favor de revisar el n\u00famero de hojas del censo #"+etimestamp;
				logger.error(respuesta);
			}
			
			if(exito){
				//Iterate through each rows one by one
				logger.debug(""
					+ "\n##############################################"
					+ "\n###### "+archivoTxt.getAbsolutePath()+" ######"
				);
				
	            Iterator<Row> rowIterator        = sheet.iterator();
	            int           fila               = 0;
	            int           nFamilia           = 0;
	            StringBuilder bufferErroresCenso = new StringBuilder();
	            int           filasLeidas        = 0;
	            int           filasProcesadas    = 0;
	            int           filasError         = 0;
	            
	            Map<Integer,String>  familias       = new LinkedHashMap<Integer,String>();
				Map<Integer,Boolean> estadoFamilias = new LinkedHashMap<Integer,Boolean>();
				Map<Integer,Integer> errorFamilia   = new LinkedHashMap<Integer,Integer>();
				Map<Integer,String>  titulares      = new LinkedHashMap<Integer,String>();
	            
				boolean[] gruposValidos = new boolean[olist1.size()];
				
				HashMap<String,String> codigosPostales = null;
				
				try
				{
					codigosPostales = cotizacionManager.obtieneCodigosPostalesProductos();
				}
				catch(Exception ex)
				{
					respuesta       = Utils.join("Error al validar Codigos Postales del Producto, sin datos. #",System.currentTimeMillis());
					respuestaOculta = ex.getMessage();
					exito           = false;
					logger.error(respuesta,ex);
				}
				
				if(codigosPostales == null){
					codigosPostales = new HashMap<String, String>();
				}
				
	            while (rowIterator.hasNext()&&exito) {
	                Row           row            = rowIterator.next();
	                Date          auxDate        = null;
	                Cell          auxCell        = null;
	                StringBuilder bufferLinea    = new StringBuilder();
	                StringBuilder bufferLineaStr = new StringBuilder();
	                boolean       filaBuena      = true;
	                
	                if(Utils.isRowEmpty(row)) {
	                	break;
	                }
	                
	                fila        = fila + 1;
	                filasLeidas = filasLeidas + 1;
	                
	                String parentesco      = null;
	                String dependiente     = null;
	                String nombre          = "";
	                double cdgrupo         = -1d;
	                int total              = 1;
	                String dsnombre        = null;
	                String dsnombre1       = null;
	                String dsapellido      = null;
	                String dsapellido1     = null;
	                String genero          = null;
	                String fechaNacAfectad = null;
	                int errorSubcobertura  = 0;
	                String banCertificado  = "0";
	                //GRUPO
	                try {
	                	cdgrupo = row.getCell(0).getNumericCellValue();
		                logger.debug("GRUPO: "+(String.format("%.0f",row.getCell(0).getNumericCellValue())+"|"));
		                bufferLinea.append(String.format("%.0f",row.getCell(0).getNumericCellValue())+"|");
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Grupo' (A) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(0)),"-"));
	                }
	                
	                //CERTIFICADO
	                try {
	                    auxCell=row.getCell(1);
	                    dependiente = auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"0|";
	                    if(auxCell!=null){
	                        HashMap<String, Object> paramCertificado = new HashMap<String, Object>();
                            paramCertificado.put("pv_cdunieco_i", cdunieco);
                            paramCertificado.put("pv_cdramo_i"  , cdramo);
                            paramCertificado.put("pv_estado_i"  , estado);
                            paramCertificado.put("pv_nmpoliza_i", nmpoliza);
                            paramCertificado.put("pv_nmsitaux_i", String.format("%.0f",auxCell.getNumericCellValue()));
                            String certificadoValido = cotizacionManager.validaCertificadoGrupo(paramCertificado);
                            
                            if(certificadoValido.equalsIgnoreCase("s")){
                                logger.debug("CERTIFICADO: "+dependiente);
                                bufferLinea.append(dependiente);
                            }else{
                                //mandamos excepcion
                                banCertificado = "1";
                                throw new ApplicationException("No es valido");
                            }
	                    }else{
	                        logger.debug("CERTIFICADO: "+dependiente);
	                        bufferLinea.append(dependiente);
	                    }
	                } catch(Exception ex) {
	                    logger.error("error al leer dependiente como numero, se intentara como string:",ex);
	                    try {
	                        dependiente = row.getCell(1).getStringCellValue()+"|";
	                        if("|".equals(dependiente)) {
	                            dependiente = "0|";
	                            logger.debug("CERTIFICADO: "+dependiente);
	                            bufferLinea.append(dependiente);
	                        }else{
	                            HashMap<String, Object> paramCertificado = new HashMap<String, Object>();
                                paramCertificado.put("pv_cdunieco_i", cdunieco);
                                paramCertificado.put("pv_cdramo_i"  , cdramo);
                                paramCertificado.put("pv_estado_i"  , estado);
                                paramCertificado.put("pv_nmpoliza_i", nmpoliza);
                                paramCertificado.put("pv_nmsitaux_i", row.getCell(1).getStringCellValue());
                                String certificadoValido = cotizacionManager.validaCertificadoGrupo(paramCertificado);
                                
                                if(certificadoValido.equalsIgnoreCase("s")){
                                    logger.debug("CERTIFICADO: "+dependiente);
                                    bufferLinea.append(dependiente);
                                }else{
                                    //mandamos excepcion
                                    banCertificado = "1";
                                    throw new ApplicationException("No es valido");
                                }
	                        }
	                        
	                    } catch(Exception ex2) {
	                        logger.error("error dependiente:",ex2);
	                        filaBuena = false;
	                        if(banCertificado.equalsIgnoreCase("1")){
	                            bufferErroresCenso.append(Utils.join("Error el titular no se encuentra vigente, en el campo 'Certificado' (B) de la fila ",fila," "));
	                        }else{
	                            bufferErroresCenso.append(Utils.join("Error en el campo 'Certificado' (B) de la fila ",fila," "));
	                        }
	                    }
	                } finally {
	                    bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(1)),"-"));
	                }
	                
	                //PARENTESCO
	                try {
	                	parentesco = row.getCell(2).getStringCellValue();
	                	if(!"T".equalsIgnoreCase(parentesco) &&!"C".equalsIgnoreCase(parentesco)
	                			&&!"H".equalsIgnoreCase(parentesco)
	                			&&!"P".equalsIgnoreCase(parentesco)
	                			&&!"D".equalsIgnoreCase(parentesco)) {
	                		throw new ApplicationException("El parentesco no se reconoce [T,C,H,P,D]");
	                	}
	                	logger.debug("PARENTESCO: "+(row.getCell(2).getStringCellValue()+"|"));
		                bufferLinea.append(parentesco+"|");
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	if(fila==1) {
	                		bufferErroresCenso.append(Utils.join("Error en el campo 'Parentesco' (C) de la fila ",fila," la primer fila debe ser titular, se excluir\u00e1n las filas hasta el siguiente titular "));
	                	}else {
	                		bufferErroresCenso.append(Utils.join("Error en el campo 'Parentesco' (C) de la fila ",fila," "));
	                	}
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(2)),"-"));
	                }
	                
	                //PATERNO
	                try {
		                logger.debug("PATERNO: "+(row.getCell(3).getStringCellValue()+"|"));
		                bufferLinea.append(row.getCell(3).getStringCellValue()+"|");
		                nombre = Utils.join(nombre,row.getCell(3).getStringCellValue()," ");
		                dsapellido = row.getCell(3).getStringCellValue();
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Apellido paterno' (D) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(3)),"-"));
	                }
	                
	                //MATERNO
	                try {
		                logger.debug("MATERNO: "+(row.getCell(4).getStringCellValue()+"|"));
		                bufferLinea.append(row.getCell(4).getStringCellValue()+"|");
		                nombre = Utils.join(nombre,row.getCell(4).getStringCellValue()," ");
		                dsapellido1 = row.getCell(4).getStringCellValue();
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Apellido materno' (E) de la fila ",fila," "));
	                }finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(4)),"-"));
	                }
	                
	                //PRIMER NOMBRE
	                try {
		                logger.debug("NOMBRE: "+(row.getCell(5).getStringCellValue()+"|"));
		                bufferLinea.append(row.getCell(5).getStringCellValue()+"|");
		                nombre = Utils.join(nombre,row.getCell(5).getStringCellValue()," ");
		                dsnombre = row.getCell(5).getStringCellValue();
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Nombre' (F) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(5)),"-"));
	                }
	                
	                //SEGUNDO NOMBRE
	                try {
		                auxCell=row.getCell(6);
		                logger.debug("SEGUNDO NOMBRE: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
		                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
		                nombre = Utils.join(nombre,auxCell!=null?auxCell.getStringCellValue():"");
		                dsnombre1 = auxCell!=null?auxCell.getStringCellValue():"";
		                if("T".equals(parentesco)||!"0|".equals(dependiente)) {
		                	nFamilia++;
		                	familias.put(nFamilia,"");
		                	estadoFamilias.put(nFamilia,true);
		                	titulares.put(nFamilia,nombre);
		                }
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Segundo nombre' (G) de la fila ",fila," "));
	                } finally  {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(6)),"-"));
	                }
	                
	                //SEXO
	                try {
	                	String sexo = row.getCell(7).getStringCellValue();
	                	if(!"H".equalsIgnoreCase(sexo)
	                			&&!"M".equalsIgnoreCase(sexo)
	                	) {
	                		throw new ApplicationException("No se reconoce el sexo [H,M]");
	                	}
	                	genero = row.getCell(7).getStringCellValue();
		                logger.debug("SEXO: "+(sexo+"|"));
		                bufferLinea.append(sexo+"|");
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Sexo' (H) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(7)),"-"));
	                }
	                
	                //FECHA NACIMIENTO
	                try {
		                auxDate=row.getCell(8).getDateCellValue();
		                logger.debug(Utils.log("�auxDate: ",auxDate));
		                if(auxDate!=null) {
		                	Calendar cal = Calendar.getInstance();
		                	cal.setTime(auxDate);
		                	if(cal.get(Calendar.YEAR)>2100 ||cal.get(Calendar.YEAR)<1900) { //ELP
		                		throw new ApplicationException("El anio de la fecha no es valido");
		                	}
		                }
		                logger.debug("FECHA NACIMIENTO: "+(auxDate!=null?renderFechas.format(auxDate)+"|":"|"));
		                bufferLinea.append(auxDate!=null?renderFechas.format(auxDate)+"|":"|");
		                fechaNacAfectad = auxDate!=null?renderFechas.format(auxDate):"";
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Fecha de nacimiento' (I) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(8)),"-"));
	                }
	                
	                //validamos asegurados duplicados
                    try {
                        auxCell=row.getCell(6);
                        if(dsapellido!= null && dsapellido1 != null && dsnombre != null && genero != null && fechaNacAfectad!= null && total <= 1){
                            logger.debug("Entra a la validacion de asegurados duplicados ==> :{} :{} :{} :{} :{} :{} ",dsapellido,dsapellido,dsnombre,dsnombre1,genero,fechaNacAfectad);
                            total = total+1;
                            HashMap<String, Object> paramPersona = new HashMap<String, Object>();
                            paramPersona.put("pv_cdunieco_i",   cdunieco );
                            paramPersona.put("pv_cdramo_i",     cdramo);
                            paramPersona.put("pv_estado_i",     estado);
                            paramPersona.put("pv_nmpoliza_i",   nmpoliza );
                            paramPersona.put("pv_dsnombre_i",   dsnombre);
                            paramPersona.put("pv_dsnombre1_i",  dsnombre1);
                            paramPersona.put("pv_dsapellido_i", dsapellido);
                            paramPersona.put("pv_dsapellido1_i",dsapellido1);
                            paramPersona.put("pv_genero_i",     genero);
                            paramPersona.put("pv_fenacimi_i",   renderFechas.parse(fechaNacAfectad));
                            logger.debug("Valor a enviar paramPersona ==> :{}",paramPersona);
                            
                            String duplicadoAseg = cotizacionManager.obtenerAseguradoDuplicado(paramPersona);
                            logger.debug("Existe Duplicados ==> :{}",duplicadoAseg);
                            if(Integer.parseInt(duplicadoAseg) == 1){
                                throw new Exception("El asegurado esta duplicado");
                            }
                        }
                    } catch(Exception ex) {
                        filaBuena = false;
                        logger.debug("Existe Duplicados ==> :{}",ex);
                        bufferErroresCenso.append(Utils.join("Error el asegurado se encuentra duplicado en la fila ",fila," "));
                    } finally {
                        bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(6)),"-"));
                    }
	                
	                //CODIGO POSTAL
                    
                    String codpostal = null;
	                try {
		                logger.debug("COD POSTAL: "+(String.format("%.0f",row.getCell(9).getNumericCellValue())+"|"));
		                bufferLinea.append(String.format("%.0f",row.getCell(9).getNumericCellValue())+"|");
		                
		                codpostal = String.format("%.0f",row.getCell(9).getNumericCellValue());
		                
                	} catch(Exception ex2) {
	                	logger.warn("error al leer codigo postal como numero, se intentara como string:",ex2);
	                	try {
	                		logger.debug("COD POSTAL: "+row.getCell(9).getStringCellValue()+"|");
			                bufferLinea.append(row.getCell(9).getStringCellValue()+"|");
			                
			                codpostal = row.getCell(9).getStringCellValue();
			                
	                	} catch(Exception ex) {
		                	filaBuena = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Codigo postal' (J) de la fila ",fila," "));
		                }
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(9)),"-"));
	                }
	                
	                logger.debug(">>> Validando codigo postal existente en el producto");
	                if(StringUtils.isBlank(codpostal) || !codigosPostales.containsKey(codpostal)){
	                	logger.error("Codigo Postal inexistente en la fila: " + fila);
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Codigo postal' No v\u00e1lido (J) de la fila ",fila," "));
	                }else{
	                	logger.debug("<<< Codigo postal correcto..");
	                }
	                
	                //ESTADO
	                try {
		                logger.debug("ESTADO: "+(row.getCell(10).getStringCellValue()+"|"));
		                bufferLinea.append(row.getCell(10).getStringCellValue()+"|");
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Estado' (K) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(10)),"-"));
	                }
	                
	                //MUNICIPIO
	                try {
		                logger.debug("MUNICIPIO: "+(row.getCell(11).getStringCellValue()+"|"));
		                bufferLinea.append(row.getCell(11).getStringCellValue()+"|");
                	} catch(Exception ex){
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Municipio' (L) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(11)),"-"));
	                }
	                
	                //COLONIA
	                try {
		                logger.debug("COLONIA: "+(row.getCell(12).getStringCellValue()+"|"));
		                bufferLinea.append(row.getCell(12).getStringCellValue()+"|");
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Colonia' (M) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(12)),"-"));
	                }
	                
	                //CALLE
	                try {
		                logger.debug("CALLE: "+(row.getCell(13).getStringCellValue()+"|"));
		                bufferLinea.append(row.getCell(13).getStringCellValue()+"|");
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Calle' (N) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(13)),"-"));
	                }
	                
	                //NUM. EXTERIOR
	                try {
	                	String numExt = extraerStringDeCelda(row.getCell(14));
	                	if(StringUtils.isBlank(numExt)) {
	                		throw new ApplicationException("Falta numero exterior");
	                	}
		                logger.debug("NUM EXT: "+numExt);
		                bufferLinea.append(Utils.join(numExt,"|"));
                	} catch(Exception ex){
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Numero exterior' (O) de la fila ",fila," "));
	                } finally{
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(14)),"-"));
	                }
	                
	                //NUM. INTERIOR
	                try {
	                	String numInt = extraerStringDeCelda(row.getCell(15));
		                logger.debug("NUM INT: "+numInt);
		                bufferLinea.append(Utils.join(numInt,"|"));
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Numero interior' (P) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(15)),"-"));
	                }
	                
	                //RFC
	                try {
	                	auxCell=row.getCell(16);
	                	logger.debug("RFC: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
		                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
		                
		                if((auxCell==null||StringUtils.isBlank(auxCell.getStringCellValue()))
		                		&&pagoRepartido
		                		&&"T".equals(parentesco)){
		                	throw new ApplicationException("Sin rfc para un titular en pago repartido");
		                }
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'RFC' (Q) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(16)),"-"));
	                }
	                
	                //CORREO
	                try {
		                auxCell=row.getCell(17);
		                logger.debug("CORREO: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
		                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Correo' (R) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(17)),"-"));
	                }
	                
	                //TELEFONO
	                try{
		                auxCell=row.getCell(18);
		                logger.debug("TELEFONO: "+( auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|"));
		                bufferLinea.append(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|");
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Telefono' (S) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(19)),"-"));
	                }
	                
                    //IDENTIDAD NO.DE EMPLEADO
	                try {
		                auxCell=row.getCell(19);
		                logger.debug("IDENTIDAD: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
		                
		                if(cdunieco.equalsIgnoreCase("1403")){
		                	if(auxCell!=null){
		                		//Validamos que en verdad
		                		String identidad = auxCell.getStringCellValue();
		                		String identidadModificada[] = identidad.split("\\-");
		                		String seccion1 = StringUtils.leftPad(identidadModificada[0].toString(), 6, "0");
		                		logger.debug("Seccion 1 IDENTIDAD : {}",seccion1);
		                		String seccion2 = StringUtils.leftPad(identidadModificada[1].toString(), 2, "0");
		                		logger.debug("Seccion 2 IDENTIDAD : {}",seccion2);
		                		
		                		if(StringUtils.isNumeric(seccion1) && StringUtils.isNumeric(seccion2)){
		                			bufferLinea.append(seccion1.toString()+"-"+seccion2.toString()+"|");
		                		}else{
		                			//mandamos excepcion
			                		throw new ApplicationException("No es numero");
		                		}		                		
		                	}else{
		                		//mandamos excepcion
		                		throw new ApplicationException("La identidad no puede ser null");
		                	}
		                }else{
		                	bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
		                }
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Identidad' (T) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(19)),"-"));
	                }
	                
	                //FECHA DE RECONOCIMIENTO DE ANTIGUEDAD
	                try {
		                auxDate=row.getCell(20)!=null?row.getCell(20).getDateCellValue():null;
		                if(auxDate!=null) {
		                	Calendar cal = Calendar.getInstance();
		                	cal.setTime(auxDate);
		                	if(cal.get(Calendar.YEAR)>2100 ||cal.get(Calendar.YEAR)<1900){
		                		throw new ApplicationException("El anio de la fecha no es valido");
		                	}
		                }
		                logger.debug("FECHA RECONOCIMIENTO ANTIGUEDAD  ===>: "+(auxDate!=null?renderFechas.format(auxDate)+"|":"|"));
		                bufferLinea.append(auxDate!=null?renderFechas.format(auxDate)+"|":"|");
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Fecha de reconocimiento antiguedad' (U) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(20)),"-"));
	                }
	                
	                //OCUPACION
	                try {
		                auxCell=row.getCell(21);
		                logger.debug("OCUPACION: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
		                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Ocupaci&oacute;n' (V) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(21)),"-"));
	                }
	                
	                //EXT. OCUPACIONAL
	                try {
		                auxCell=row.getCell(22);
		                logger.debug("EXT. OCUPACIONAL: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
		                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Ext. Ocupacional' (W) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(22)),"-"));
	                }
	                
	                //PESO
	                try {
		                auxCell=row.getCell(23);
		                logger.debug("PESO: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
		                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Peso' (X) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(23)),"-"));
	                }
	                
	                //ESTATURA
	                try {
		                auxCell=row.getCell(24);
		                logger.debug("ESTATURA: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
		                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Estatura' (Y) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(24)),"-"));
	                }
	                
	                //EXT. SOBREPESO
	                try {
		                auxCell=row.getCell(25);
		                logger.debug("EXT. SOBREPESO: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
		                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Ext. Sobrepeso' (Z) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(25)),"-"));
	                }
	                //ESTADO CIVIL
	                try {
		                auxCell=row.getCell(26);
		                logger.debug("ESTADO CIVIL: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
		                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Estado Civil' (AA) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(26)),"-"));
	                }
	                
	                //FECHA INGRESO
	                try {
		                auxDate=row.getCell(27)!=null?row.getCell(27).getDateCellValue():null;
		                if(auxDate!=null) {
		                	Calendar cal = Calendar.getInstance();
		                	cal.setTime(auxDate);
		                	if(cal.get(Calendar.YEAR)>2100 ||cal.get(Calendar.YEAR)<1900) {
		                		throw new ApplicationException("El anio de la fecha no es valido");
		                	}
		                }
		                bufferLinea.append(auxDate!=null?renderFechas.format(auxDate)+"|":"|");
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Fecha Ingreso ' (AB) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(27)),"-"));
	                }

	                //ID SISA
	                try {
		                logger.debug("ID SISA: "+(String.format("%.0f",row.getCell(28).getNumericCellValue())+"|"));
		                bufferLinea.append(String.format("%.0f",row.getCell(28).getNumericCellValue())+"|");
                	} catch(Exception ex2) {
	                	logger.warn("error al leer el peso, se intentara como string:",ex2);
	                	try {
	                		auxCell=row.getCell(28);
			                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
	                	} catch(Exception ex) {
		                	filaBuena = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Id. SISA' (AC) de la fila ",fila," "));
		                }
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(28)),"-"));
	                }
	                
	                //PLAZA
	                try {
		                auxCell=row.getCell(29);
		                logger.debug("PLAZA: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
		                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Correo' (AD) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(29)),"-"));
	                }
	                
	                //ID ASEGURADO
	                try {
		                auxCell=row.getCell(30);
		                logger.debug("ID ASEGURADO: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
		                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Id. Asegurado' (AE) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(30)),"-"));
	                }
	                logger.debug(Utils.log("** NUEVA_FILA (filaBuena=",filaBuena,",cdgrupo=",cdgrupo,") **"));
	                
	                if(filaBuena)
	                {
	                	familias.put(nFamilia,Utils.join(familias.get(nFamilia),bufferLinea.toString(),"\n"));
	                	filasProcesadas = filasProcesadas + 1;
	                	gruposValidos[((int)cdgrupo)-1]=true;
	                }
	                else
	                {
	                	filasError = filasError + 1;
	                	bufferErroresCenso.append(Utils.join(": ",bufferLineaStr.toString(),"\n"));
	                	estadoFamilias.put(nFamilia,false);
	                	
	                	if(!errorFamilia.containsKey(nFamilia))
	                	{
	                		errorFamilia.put(nFamilia,fila);
	                	}
	                }
	                
	                if(cdgrupo>0d)
	                {
	                	logger.debug(Utils.log("cdgrupo=",cdgrupo,", valido=",gruposValidos[((int)cdgrupo)-1]));
	                }
	            }
	            
	            if(exito)
	            {
	            	for(int i=0;i<gruposValidos.length;i++)
	            	{
	            		logger.debug(Utils.log("gruposValidos[i]=",gruposValidos[i]));
	            	}
	            }
	            
	            if(exito)
	            {
	            	boolean       sonGruposValidos = true;
	            	StringBuilder errorGrupos      = new StringBuilder();
	            	/*for(int i=0;i<gruposValidos.length;i++)
	            	{
	            		if(!gruposValidos[i])
	            		{
	            			sonGruposValidos = false;
	            			errorGrupos.append("Debe haber al menos un asegurado v\u00E1lido para el grupo ").append(i+1).append("\n");
	            		}
	            	}*/
	            	if(!sonGruposValidos)
	            	{
	            		exito           = false;
	            		respuesta       = errorGrupos.append("\n")
	            				.append(bufferErroresCenso.toString())
	            				.append("\nError #").append(System.currentTimeMillis()).toString();
	            		respuestaOculta = respuesta;
	            		logger.error(bufferErroresCenso.toString());
	            		logger.error(respuesta);
	            	}
	            }
	            
	            if(exito)
				{
	            	logger.debug("\nFamilias: {}\nEstado familias: {}\nErrorFamilia: {}\nTitulares: {}"
		            		,familias,estadoFamilias,errorFamilia,titulares);
		            
		            for(Entry<Integer,Boolean>en:estadoFamilias.entrySet())
		            {
		            	int     n = en.getKey();
		            	boolean v = en.getValue();
		            	if(v)
		            	{
		            		output.print(familias.get(n));
		            	}
		            	/*else
		            	{
		            		bufferErroresCenso.append(Utils.join("La familia ",n," del titular '",titulares.get(n),"' no fue incluida por error en la fila ",errorFamilia.get(n),"\n"));
		            	}*/
		            }
		            
					smap1.put("erroresCenso"    , bufferErroresCenso.toString());
					smap1.put("filasLeidas"     , Integer.toString(filasLeidas));
					smap1.put("filasProcesadas" , Integer.toString(filasProcesadas));
					smap1.put("filasErrores"    , Integer.toString(filasError));
				}
	            
	            if(exito)
	            {
	            	try
	            	{
	            		input.close();
	            		output.close();
	            	}
	            	catch(Exception ex)
	            	{
	            		long etimestamp = System.currentTimeMillis();
	            		exito           = false;
	            		respuesta       = "Error al transformar el archivo #"+etimestamp;
	            		respuestaOculta = ex.getMessage();
	            		logger.error(respuesta,ex);
	            	}
	            }
	            
	            logger.debug(""
	            		+ "\n###### "+archivoTxt.getAbsolutePath()+" ######"
						+ "\n##############################################"
						);
				
	            if(exito)
	            {
					exito = FTPSUtils.upload
							(
								this.dominioServerLayouts,
								this.userServerLayouts,
								this.passServerLayouts,
								archivoTxt.getAbsolutePath(),
								this.directorioServerLayouts+"/"+nombreCenso
						    )
							&&FTPSUtils.upload
							(
									this.dominioServerLayouts2,
									this.userServerLayouts,
									this.passServerLayouts,
									archivoTxt.getAbsolutePath(),
									this.directorioServerLayouts+"/"+nombreCenso
							);
					
					if(!exito)
					{
						long etimestamp = System.currentTimeMillis();
						exito           = false;
						respuesta       = "Error al transferir archivo al servidor #"+etimestamp;
						respuestaOculta = respuesta;
						logger.error(respuesta);
					}
	            }
				
				if(exito)
				{
					try
					{
						String cdedo         = smap1.get("cdedo");
						String cdmunici      = smap1.get("cdmunici");
						String cdplanes[]    = new String[5];
						
						for(Map<String,Object>iGrupo:olist1)
						{
							String  cdgrupo      = (String)iGrupo.get("letra");
							String  cdplan       = (String)iGrupo.get("cdplan");
							Integer indGrupo     = Integer.valueOf(cdgrupo);
							cdplanes[indGrupo-1] = cdplan;
						}
						cotizacionManager.guardarCensoCompletoMultisaludEndoso(nombreCenso
								,cdunieco    , cdramo      , estado
								,nmpoliza    , cdedo       , cdmunici
								,cdplanes[0] , cdplanes[1] , cdplanes[2]
								,cdplanes[3] , cdplanes[4] , "N"
								,nmsuplem
						);
					}
					catch(Exception ex)
					{
						long etimestamp = System.currentTimeMillis();
						exito           = false;
						respuesta       = "Error al guardar los datos #"+etimestamp;
						respuestaOculta = ex.getMessage();
						logger.error(respuesta,ex);
						
					}
				}
			}
		}
		
		if(exito&&StringUtils.isBlank(nombreCensoConfirmado))
		{
			smap1.put("nombreCensoParaConfirmar", nombreCenso);
			exito     = true;
			respuesta = Utils.join("Se ha revisado el censo [REV. ",System.currentTimeMillis(),"]");
			logger.debug(respuesta);
			return SUCCESS;
		}
		
		if(exito)
		{
			logger.debug(Utils.log("########################user ",user));
			tvalositSigsvdefTvalogarContratanteTramiteSigsvalipolObject aux=this.tvalositSigsvdefTvalogarContratanteTramiteSigsvalipolEndoso(
					clasif    , LINEA      , LINEA_EXTENDIDA
					,cdunieco , cdramo     , nmpoliza
					,cdtipsit , hayTramite , hayTramiteVacio
					,user     , cdelemento , ntramiteVacio
					,true     , null, null
					,false //sincenso
					,false //censoAtrasado
					,false //resubirCenso
					,cdperpag
					,cdsisrol
					,false
					,false //asincrono
					,null
					,null
					,null
					,estado
					,nmsuplem
					);
			exito           = aux.exito;
			respuesta       = aux.respuesta;
			respuestaOculta = aux.respuestaOculta;
		}
		
		if(exito)
		{
			respuesta       = Utils.join("Se han complementado los asegurados",
			        StringUtils.isBlank(respuesta)
    			        ? ""
    			        : Utils.join(". ", respuesta));
			respuestaOculta = "Todo OK";
		}
		
		logger.debug(""
				+ "\n###### subirCensoCompleto3 ######"
				+ "\n################################"
				);
		return SUCCESS;
	}
	
	
	
	public String subirCensoCompleto()
	{
		this.session=ActionContext.getContext().getSession();
		logger.debug(Utils.log(
				 "\n################################"
				,"\n###### subirCensoCompleto ######"
				,"\n###### smap1="  , smap1
				,"\n###### olist1=" , olist1
				));
		
		success = true;
		exito   = true;
		
		String censoTimestamp   = smap1.get("timestamp");
		String clasif           = smap1.get("clasif");
		String LINEA_EXTENDIDA  = smap1.get("LINEA_EXTENDIDA");
		String cdunieco         = smap1.get("cdunieco");
		String cdtipsit         = smap1.get("cdtipsit");
		String cdramo           = smap1.get("cdramo");
		String nmpoliza         = smap1.get("nmpoliza");
		String cdperpag         = smap1.get("cdperpag");
		String pcpgocte         = smap1.get("pcpgocte");
		UserVO usuario          = (UserVO)session.get("USUARIO");
		String user             = usuario.getUser();
		String cdelemento       = usuario.getEmpresa().getElementoId();
		String cdsisrol         = usuario.getRolActivo().getClave();
		final String LINEA      = "1";
		String ntramite         = smap1.get("ntramite");
		boolean hayTramite      = StringUtils.isNotBlank(ntramite);
		String ntramiteVacio    = smap1.get("ntramiteVacio");
		boolean hayTramiteVacio = StringUtils.isNotBlank(ntramiteVacio);
		Date fechaHoy           = new Date();
		String feini            = smap1.get("feini");
		String fefin            = smap1.get("fefin");
		String nmpolant         = smap1.get("nmpolant");
		String nmrenova         = smap1.get("nmrenova");
		String esRenovacion		= smap1.get("esRenovacion");
		String agrupador        = smap1.get("cdpool");
		censo = new File(this.rutaDocumentosTemporal+"/censo_"+censoTimestamp);
		
		String nombreCensoConfirmado = smap1.get("nombreCensoConfirmado");
		
		
		if(exito&&StringUtils.isNotBlank(nombreCensoConfirmado)){
			try
			{
				Map<String,String> smapCenso =  new HashMap<String, String>();
				
				smapCenso.putAll(getSmap1());
				new ConfirmaCensoConcurrente1(smapCenso,this).start();
			}
			catch(Exception ex)
			{
				long etimestamp = System.currentTimeMillis();
				logger.error(etimestamp+" error mpolizas",ex);
				respuesta       = "Error al cotizar #"+etimestamp;
				respuestaOculta = ex.getMessage();
				exito           = false;
			}
			
			logger.debug(""
					+ "\n###### subirCensoCompleto ######"
					+ "\n################################"
					);
			return SUCCESS;
			
		}
		
		//mpolizas
		if(exito)
		{
			try
			{
				Map<String,String>mapaMpolizas=new HashMap<String,String>(0);
	            mapaMpolizas.put("pv_cdunieco"  , cdunieco);
	            mapaMpolizas.put("pv_cdramo"    , cdramo);
	            mapaMpolizas.put("pv_estado"    , "W");
	            mapaMpolizas.put("pv_nmpoliza"  , nmpoliza);
	            mapaMpolizas.put("pv_nmsuplem"  , "0");
	            mapaMpolizas.put("pv_status"    , "V");
	            mapaMpolizas.put("pv_swestado"  , "0");
	            mapaMpolizas.put("pv_nmsolici"  , null);
	            mapaMpolizas.put("pv_feautori"  , null);
	            mapaMpolizas.put("pv_cdmotanu"  , null);
	            mapaMpolizas.put("pv_feanulac"  , null);
	            mapaMpolizas.put("pv_swautori"  , "N");
	            mapaMpolizas.put("pv_cdmoneda"  , "001");
	            mapaMpolizas.put("pv_feinisus"  , null);
	            mapaMpolizas.put("pv_fefinsus"  , null);
	            mapaMpolizas.put("pv_ottempot"  , "R");
	            mapaMpolizas.put("pv_feefecto"  , feini);
	            mapaMpolizas.put("pv_hhefecto"  , "12:00");
	            mapaMpolizas.put("pv_feproren"  , fefin);
	            mapaMpolizas.put("pv_fevencim"  , null);
	            mapaMpolizas.put("pv_nmrenova"  , StringUtils.isBlank(nmrenova) ? "0" : nmrenova);
	            mapaMpolizas.put("pv_ferecibo"  , null);
	            mapaMpolizas.put("pv_feultsin"  , null);
	            mapaMpolizas.put("pv_nmnumsin"  , "0");
	            mapaMpolizas.put("pv_cdtipcoa"  , "N");
	            mapaMpolizas.put("pv_swtarifi"  , "A");
	            mapaMpolizas.put("pv_swabrido"  , null);
	            mapaMpolizas.put("pv_feemisio"  , renderFechas.format(fechaHoy));
	            mapaMpolizas.put("pv_cdperpag"  , cdperpag);
	            mapaMpolizas.put("pv_nmpoliex"  , null);
	            mapaMpolizas.put("pv_nmcuadro"  , "P1");
	            mapaMpolizas.put("pv_porredau"  , "100");
	            mapaMpolizas.put("pv_swconsol"  , "S");
	            mapaMpolizas.put("pv_nmpolant"  , nmpolant);
	            mapaMpolizas.put("pv_nmpolnva"  , null);
	            mapaMpolizas.put("pv_fesolici"  , renderFechas.format(fechaHoy));
	            mapaMpolizas.put("pv_cdramant"  , null);
	            mapaMpolizas.put("pv_cdmejred"  , null);
	            mapaMpolizas.put("pv_nmpoldoc"  , null);
	            mapaMpolizas.put("pv_nmpoliza2" , null);
	            mapaMpolizas.put("pv_nmrenove"  , null);
	            mapaMpolizas.put("pv_nmsuplee"  , null);
	            mapaMpolizas.put("pv_ttipcamc"  , null);
	            mapaMpolizas.put("pv_ttipcamv"  , null);
	            mapaMpolizas.put("pv_swpatent"  , null);
	            mapaMpolizas.put("pv_pcpgocte"  , pcpgocte);
	            mapaMpolizas.put("pv_tipoflot"  , "F");
	            mapaMpolizas.put("pv_agrupador" , agrupador);
	            mapaMpolizas.put("pv_accion"    , "U");
	            kernelManager.insertaMaestroPolizas(mapaMpolizas);
			}
			catch(Exception ex)
			{
				long etimestamp = System.currentTimeMillis();
				logger.error(etimestamp+" error mpolizas",ex);
				respuesta       = "Error al cotizar #"+etimestamp;
				respuestaOculta = ex.getMessage();
				exito           = false;
			}
		}
		
		boolean pagoRepartido = false;
		if(exito)
		{
			try
			{
				pagoRepartido = cotizacionManager.validaPagoPolizaRepartido(cdunieco,cdramo,"W",nmpoliza);
			}
			catch(Exception ex)
			{
				respuesta       = Utils.join("Error al recuperar reparto de pago #",System.currentTimeMillis());
				respuestaOculta = ex.getMessage();
				exito           = false;
				logger.error(respuesta,ex);
			}
		}
		
		boolean pideNumCliemte = false;
		if(exito)
		{
			try
			{
				pideNumCliemte = consultasManager.validaClientePideNumeroEmpleado(cdunieco,cdramo,"W",nmpoliza);
			}
			catch(Exception ex)
			{
				respuesta       = Utils.join("Error al recuperar parametrizacion de numero de empleado por cliente");
				respuestaOculta = ex.getMessage();
				exito           = false;
				logger.error(respuesta,ex);
			}
		}
		
		String nombreCenso = null;
		int    filasError=0;
		
		if(exito&&StringUtils.isBlank(nombreCensoConfirmado))
		{
			FileInputStream input       = null;
			Workbook        workbook    = null;
			Sheet           sheet       = null;
			Long            inTimestamp = null;
			File            archivoTxt  = null;
			PrintStream     output      = null;
			
			try
			{	
				input       = new FileInputStream(censo);
				workbook    = WorkbookFactory.create(input);
				sheet       = workbook.getSheetAt(0);
				inTimestamp = System.currentTimeMillis();
				nombreCenso = "censo_"+inTimestamp+"_"+nmpoliza+".txt";
				archivoTxt  = new File(this.rutaDocumentosTemporal+"/"+nombreCenso);
				output      = new PrintStream(archivoTxt);
			}
			catch(Exception ex)
			{
				long etimestamp = System.currentTimeMillis();
				exito           = false;
				respuesta       = "Error al procesar censo #"+etimestamp;
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
			
			if(exito&&workbook.getNumberOfSheets()!=1)
			{
				long etimestamp = System.currentTimeMillis();
				exito           = false;
				respuesta       = "Favor de revisar el n\u00famero de hojas del censo #"+etimestamp;
				logger.error(respuesta);
			}
			
			if(exito)
			{
				//Iterate through each rows one by one
				logger.debug(""
						+ "\n##############################################"
						+ "\n###### "+archivoTxt.getAbsolutePath()+" ######"
						);
				
	            Iterator<Row> rowIterator        = sheet.iterator();
	            int           fila               = 0;
	            int           nFamilia           = 0;
	            StringBuilder bufferErroresCenso = new StringBuilder();
	            int           filasLeidas        = 0;
	            int           filasProcesadas    = 0;
	                          filasError         = 0;
	                          
  	            Map<Integer,List<Map<String,String>>> listaFamilias = new HashMap<Integer, List<Map<String,String>>>(); 
	            List<Map<String,String>>             filasFamilia = new ArrayList<Map<String,String>>(); 
	            
	            Map<Integer,String>  familias       = new LinkedHashMap<Integer,String>();
				Map<Integer,Boolean> estadoFamilias = new LinkedHashMap<Integer,Boolean>();
				Map<Integer,Integer> errorFamilia   = new LinkedHashMap<Integer,Integer>();
				Map<Integer,String>  titulares      = new LinkedHashMap<Integer,String>();
	            
				boolean[] gruposValidos = new boolean[olist1.size()];
				
				HashMap<String,String> codigosPostales = null;
				
				try
				{
					codigosPostales = cotizacionManager.obtieneCodigosPostalesProductos();
				}
				catch(Exception ex)
				{
					respuesta       = Utils.join("Error al validar Codigos Postales del Producto, sin datos. #",System.currentTimeMillis());
					respuestaOculta = ex.getMessage();
					exito           = false;
					logger.error(respuesta,ex);
				}
				
				if(codigosPostales == null){
					codigosPostales = new HashMap<String, String>();
				}
				
				boolean exitoCPs =  true;
				
				StringBuffer erroresCP = new StringBuffer("\n No se puede continuar. Corrija los errores presentados en C\u00f3digos Postales: \n");
				String renovacionGral   = null;
				
				if(TipoEndoso.RENOVACION.getCdTipSup().toString().equalsIgnoreCase(esRenovacion)){
					renovacionGral = "R";
				}else{
					renovacionGral = "C";
				}
				
				List<Map<String, String>> configCampo =  new ArrayList<Map<String, String>>();
				try {
					configCampo = documentosManager.obtenerValorDefectoParametrizados(cdsisrol,null,renovacionGral);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
	            while (rowIterator.hasNext()&&exito) 
	            {
	                Row           row            = rowIterator.next();
	                Date          auxDate        = null;
	                Cell          auxCell        = null;
	                StringBuilder bufferLinea    = new StringBuilder();
	                StringBuilder bufferLineaStr = new StringBuilder();
	                boolean       filaBuena      = true;
	                
	                String parentesco   = null;
                    String apellidoP    = null;
                    String apellidoM    = null;
                    String nombre1      = null;
                    String nombre2      = null;
                    String sexo         = null;
                    String fechaNac     = null;
                    String codpostal    = null;
                    String estado       = null;
                    String municipio    = null;
                    String colonia      = null;
                    String calle        = null;
                    String numExt       = null;
                    String numInt       = null;
                    String rfcAsegurado = null;
                    String emailAseg    = null;
                    String telefono     = null;
                    String identidad    = null;
                    String fecanti      = null;
                    String ocupacion    = null;
                    String extOcupacion = null;
                    String peso         = null;
                    String estatura     = null;
                    String extSobrepeso = null;
                    String estadoCivil  = null;
                    String feingreso    = null;
                    String idSisa       = null;
                    String plaza        = null;
                    String cveAsegurado = null;
                    String dependiente = null;
                    
	                if(Utils.isRowEmpty(row))
	                {
	                	break;
	                }
	                
	                fila               = fila + 1;
	                filasLeidas        = filasLeidas + 1;
	                String nombre      = "";
	                double cdgrupo     = -1d;
	                
	                //GRUPO
	                if(Constantes.SI.equalsIgnoreCase(configCampo.get(0).get("OBLIGATORIO")) || Constantes.NO.equalsIgnoreCase(configCampo.get(0).get("OBLIGATORIO"))){
		                try {
		                	cdgrupo = row.getCell(0).getNumericCellValue();
			                logger.debug("GRUPO: "+(String.format("%.0f",row.getCell(0).getNumericCellValue())+"|"));
			                bufferLinea.append(String.format("%.0f",row.getCell(0).getNumericCellValue())+"|");
			                
			                if(cdgrupo>olist1.size()){
			                	bufferErroresCenso.append(Utils.join("Grupo no permitido: ",cdgrupo," (grupos: ",olist1.size(),") en la fila ",fila," "));
			                	throw new ApplicationException("El grupo de excel no existe");
			                }
	                	} catch(Exception ex) {
		                	try{
		                		cdgrupo = Double.parseDouble(row.getCell(0).getStringCellValue());
				                logger.debug("GRUPO: "+(row.getCell(0).getStringCellValue()+"|"));
				                bufferLinea.append(row.getCell(0).getStringCellValue()+"|");
				                if(cdgrupo>olist1.size()) {
				                	bufferErroresCenso.append(Utils.join("Grupo no permitido: ",cdgrupo," (grupos: ",olist1.size(),") en la fila ",fila," "));
				                	throw new ApplicationException("El grupo de excel no existe");
				                }
		                	}catch (Exception e) {
								filaBuena = false;
			                	bufferErroresCenso.append(Utils.join("Error en el campo 'Grupo' (A) de la fila ",fila," "));
							}
		                } finally {
		                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(0)),"-"));
		                }
	                }
	                
		              //CERTIFICADO
	                try {
		                auxCell=row.getCell(1);
		                dependiente = auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue()):"";
		                if(Constantes.SI.equalsIgnoreCase(configCampo.get(1).get("OBLIGATORIO"))){
		                	if(StringUtils.isBlank(dependiente)){
		                		throw new ApplicationException("El dependiente no puede ir en blanco");
		                	}
		                }
		                logger.debug("CERTIFICADO: "+auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|");
		                bufferLinea.append(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"0|");
	                } catch(Exception ex) {
	                	logger.error("error al leer dependiente como numero, se intentara como string:",ex);
	                	try {
	                		auxCell=row.getCell(1);
	                		dependiente = auxCell!=null?auxCell.getStringCellValue():"";
	                		if(Constantes.SI.equalsIgnoreCase(configCampo.get(1).get("OBLIGATORIO"))){
			                	if(StringUtils.isBlank(dependiente)){
			                		throw new ApplicationException("El dependiente no puede ir en blanco");
			                	}
			                }
	                		logger.debug("CERTIFICADO: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"0|"));
			                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"0|");
	                	} catch(Exception ex2) {
		                	logger.error("error dependiente:",ex2);
		                	filaBuena = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Certificado' (B) de la fila ",fila," "));
		                }
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(1)),"-"));
	                }
	                
	                
		            //PARENTESCO
		            if(Constantes.SI.equalsIgnoreCase(configCampo.get(2).get("OBLIGATORIO")) || Constantes.NO.equalsIgnoreCase(configCampo.get(2).get("OBLIGATORIO"))){
		                try
	                	{
		                	parentesco = row.getCell(2).getStringCellValue();
		                	if(!"T".equalsIgnoreCase(parentesco)
	                			&&!"C".equalsIgnoreCase(parentesco) &&!"H".equalsIgnoreCase(parentesco)
	                			&&!"P".equalsIgnoreCase(parentesco) &&!"D".equalsIgnoreCase(parentesco)){
		                			throw new ApplicationException("El parentesco no se reconoce [T,C,H,P,D]");
		                	}
			                logger.debug("PARENTESCO: "+(parentesco+"|"));
			                bufferLinea.append(parentesco+"|");
			                
			                if(fila==1&&!"T".equals(parentesco)){
			                	throw new ApplicationException("La primer fila debe ser titular");
			                }		                
	                	} catch(Exception ex){
		                	filaBuena = false;
		                	if(fila==1) {
		                		bufferErroresCenso.append(Utils.join("Error en el campo 'Parentesco' (C) de la fila ",fila," la primer fila debe ser titular, se excluir\u00e1n las filas hasta el siguiente titular "));
		                	}
		                	else {
		                		bufferErroresCenso.append(Utils.join("Error en el campo 'Parentesco' (C) de la fila ",fila," "));
		                	}
		                }
		                finally {
		                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(2)),"-"));
		                }
		            }
		            
		            //PATERNO
	                try {
		                auxCell=row.getCell(3);
		                apellidoP = auxCell!=null?auxCell.getStringCellValue():"";
		                if(Constantes.SI.equalsIgnoreCase(configCampo.get(3).get("OBLIGATORIO"))){
		                	if(StringUtils.isBlank(apellidoP)){
		                		throw new ApplicationException("El apellido Paterno no puede ir en blanco");
		                	}
		                }
		                logger.debug("PATERNO: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
	                	bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
	                	nombre = Utils.join(nombre,auxCell!=null?auxCell.getStringCellValue():"");
	                	
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Apellido paterno' (D) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(3)),"-"));
	                }
		            
	                //MATERNO
	                try {
	                	auxCell=row.getCell(4);
	                	apellidoM = auxCell!=null?auxCell.getStringCellValue():"";
	                	if(Constantes.SI.equalsIgnoreCase(configCampo.get(4).get("OBLIGATORIO"))){
		                	if(StringUtils.isBlank(apellidoM)){
		                		throw new ApplicationException("El apellido Materno no puede ir en blanco");
		                	}
		                }
	                	logger.debug("MATERNO: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
	                	bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
                		nombre = Utils.join(nombre,auxCell!=null?auxCell.getStringCellValue():"");	                	
                	}catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Apellido materno' (E) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(4)),"-"));
	                }	                
	                
	                //PRIMER NOMBRE
	                try {
	                	auxCell = row.getCell(5);
	                	nombre1 = auxCell!=null?auxCell.getStringCellValue():"";
	                	if(Constantes.SI.equalsIgnoreCase(configCampo.get(5).get("OBLIGATORIO"))){
		                	if(StringUtils.isBlank(nombre1)){
		                		throw new ApplicationException("El Nombre no puede ir en blanco");
		                	}
		                }	                	
	                	logger.debug("NOMBRE: "+(auxCell!=null?auxCell.getStringCellValue():""+"|"));
		                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
		                nombre = Utils.join(nombre,auxCell!=null?auxCell.getStringCellValue():"");
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Nombre' (F) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(5)),"-"));
	                }
	                
	                //SEGUNDO NOMBRE 
	                try {
		                auxCell=row.getCell(6);
		                nombre2 = auxCell!=null?auxCell.getStringCellValue():"";
		                if(Constantes.SI.equalsIgnoreCase(configCampo.get(6).get("OBLIGATORIO"))){
		                	if(StringUtils.isBlank(nombre2)){
		                		throw new ApplicationException("El segundo nombre no puede ir en blanco");
		                	}
		                }
		                logger.debug("SEGUNDO NOMBRE: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
		                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
		                nombre = Utils.join(nombre,auxCell!=null?auxCell.getStringCellValue():"");
		                
		                if("T".equals(parentesco)){
		                	if(nFamilia > 0){
		                		listaFamilias.put(nFamilia, filasFamilia);
		                		filasFamilia = new ArrayList<Map<String,String>>(); 
		                	}
		                	
		                	nFamilia++;
		                	familias.put(nFamilia,"");
		                	estadoFamilias.put(nFamilia,true);
		                	titulares.put(nFamilia,nombre);		                	
		                }
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Segundo nombre' (G) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(6)),"-"));
	                }
		            
	                //SEXO
	                if(Constantes.SI.equalsIgnoreCase(configCampo.get(7).get("OBLIGATORIO")) || Constantes.NO.equalsIgnoreCase(configCampo.get(7).get("OBLIGATORIO"))){
		                try{
		                	sexo = row.getCell(7).getStringCellValue();
		                	if(!"H".equalsIgnoreCase(sexo) &&!"M".equalsIgnoreCase(sexo)){
		                		throw new ApplicationException("No se reconoce el sexo [H,M]");
		                	}
			                logger.debug("SEXO: "+(sexo+"|"));
			                bufferLinea.append(sexo+"|");
	                	} catch(Exception ex){
		                	filaBuena = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Sexo' (H) de la fila ",fila," "));
		                } finally {
		                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(7)),"-"));
		                }
	                }

	                //FECHA NACIMIENTO
	                if(Constantes.SI.equalsIgnoreCase(configCampo.get(8).get("OBLIGATORIO")) || Constantes.NO.equalsIgnoreCase(configCampo.get(8).get("OBLIGATORIO"))){
		                try {
		                	// Primer intento, se tratara el campo como Date:
		                	auxDate=row.getCell(8).getDateCellValue();
			                if(auxDate!=null) {
			                	Calendar cal = Calendar.getInstance();
			                	cal.setTime(auxDate);
			                	if(cal.get(Calendar.YEAR)>2100 ||cal.get(Calendar.YEAR)<1900) {
			                		throw new ApplicationException("El anio de la fecha no es valido");
			                	}
			                }
	                        fechaNac = auxDate!=null? renderFechas.format(auxDate):"";
	        				
	                        logger.debug("FECHA NACIMIENTO: "+(auxDate!=null?renderFechas.format(auxDate)+"|":"|"));
			                bufferLinea.append(auxDate!=null?renderFechas.format(auxDate)+"|":"|");
	                	} catch(Exception ex){
		                	// Segundo intento, se tratara el campo como String:
		                	try {
								auxDate= renderFechas.parse(row.getCell(8).getStringCellValue());
								if(auxDate!=null) {
				                	Calendar cal = Calendar.getInstance();
				                	cal.setTime(auxDate);
				                	if(cal.get(Calendar.YEAR)>2100 ||cal.get(Calendar.YEAR)<1900) {
				                		throw new ApplicationException("El anio de la fecha no es valido");
				                	}
				                }
								fechaNac = auxDate!=null?renderFechas.format(auxDate):"";
				                logger.debug("FECHA NACIMIENTO: "+(auxDate!=null?renderFechas.format(auxDate)+"|":"|"));
				                bufferLinea.append(auxDate!=null?renderFechas.format(auxDate)+"|":"|");
			                } catch (Exception e) {
								filaBuena = false;
			                	bufferErroresCenso.append(Utils.join("Error en el campo 'Fecha de nacimiento' (I) de la fila ",fila," "));
							}
	                	}finally {
		                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(8)),"-"));
		                }
	                }
	                
		              
			            //CODIGO POSTAL
			            try {
			                auxCell=row.getCell(9);
			                codpostal = auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue()):"";
			                if(Constantes.SI.equalsIgnoreCase(configCampo.get(9).get("OBLIGATORIO"))){
			                	if(StringUtils.isBlank(codpostal)){
			                		throw new ApplicationException("El codigo postal no puede ir en blanco");
			                	}
			                }
			                logger.debug("COD POSTAL: "+(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|"));
			                bufferLinea.append(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|");
	                	} catch(Exception ex){
		                	try {
		                		logger.warn("error al leer codigo postal  como numero, se intentara como string:",ex);
		                		auxCell=row.getCell(9);
		                		codpostal = auxCell!=null?auxCell.getStringCellValue():"";
				                if(Constantes.SI.equalsIgnoreCase(configCampo.get(9).get("OBLIGATORIO"))){
				                	if(StringUtils.isBlank(codpostal)){
				                		throw new ApplicationException("El codigo postal no puede ir en blanco");
				                	}
				                }
				                logger.debug("COD POSTAL: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
				                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
		                	} catch(Exception e){
			                	filaBuena = false;
			                	bufferErroresCenso.append(Utils.join("Error en el campo 'Codigo postal' (J) de la fila ",fila," "));
			                }
		                } finally {
		                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(9)),"-"));
		                }

		                logger.debug(">>> Validando codigo postal existente en el producto");
		                if(StringUtils.isNotBlank(codpostal)){
		                	if(StringUtils.isBlank(codpostal) || !codigosPostales.containsKey(codpostal)){
			                	logger.error("Codigo Postal inexistente en la fila: " + (row.getRowNum()+1) +"Para la el asegurado: " + nombre1+" "+ nombre2 + " "+apellidoP+" " +apellidoM);
			                	exitoCPs = false;
			                	erroresCP.append("\n *** C\u00f3digo Postal inexistente '").append(codpostal).append("' en la fila: ").append((row.getRowNum()+1)).append(" para la el asegurado: ")
			                	.append(nombre1).append(" ").append(nombre2).append(" ").append(apellidoP).append(" ").append(apellidoM);
			                	
			                	//Para aniadir al conjunto de errores
			                	filaBuena = false;
			                	bufferErroresCenso.append(Utils.join("Error en el campo 'Codigo postal' No v\u00e1lido (J) de la fila ",fila," "));
			                }else{
			                	logger.debug("<<< Codigo postal correcto..");
			                }
		                }
	                
		            //ESTADO
	                try{
	                	auxCell = row.getCell(10);
	                	estado = auxCell!=null?auxCell.getStringCellValue():"";
	                	if(Constantes.SI.equalsIgnoreCase(configCampo.get(10).get("OBLIGATORIO"))){
		                	if(StringUtils.isBlank(estado)){
		                		throw new ApplicationException("El estado no puede ir en blanco");
		                	}
		                }
	                	logger.debug("ESTADO: "+(auxCell!=null?auxCell.getStringCellValue():""+"|"));
		                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
                	} catch(Exception ex){
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Estado' (K) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(10)),"-"));
	                }
	                
	                //MUNICIPIO
	                try {
	                	auxCell = row.getCell(11);
	                	municipio = auxCell!=null?auxCell.getStringCellValue():"";
	                	if(Constantes.SI.equalsIgnoreCase(configCampo.get(11).get("OBLIGATORIO"))){
		                	if(StringUtils.isBlank(estado)){
		                		throw new ApplicationException("El Municipio no puede ir en blanco");
		                	}
		                }
	                	logger.debug("MUNICIPIO: "+(auxCell!=null?auxCell.getStringCellValue():""+"|"));
		                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Municipio' (L) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(11)),"-"));
	                }
	                
	                //COLONIA
	                try {
	                	auxCell = row.getCell(12);
	                	colonia = auxCell!=null?auxCell.getStringCellValue():"";
	                	if(Constantes.SI.equalsIgnoreCase(configCampo.get(11).get("OBLIGATORIO"))){
		                	if(StringUtils.isBlank(colonia)){
		                		throw new ApplicationException("La colonia no puede ir en blanco");
		                	}
		                }
	                	logger.debug("COLONIA: "+(auxCell!=null?auxCell.getStringCellValue():""+"|"));
		                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Colonia' (M) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(12)),"-"));
	                }
	                
	                //CALLE
	                try {
	                	auxCell = row.getCell(13);
	                	calle = auxCell!=null?auxCell.getStringCellValue():"";
	                	if(Constantes.SI.equalsIgnoreCase(configCampo.get(11).get("OBLIGATORIO"))){
		                	if(StringUtils.isBlank(calle)){
		                		throw new ApplicationException("La calle no puede ir en blanco");
		                	}
		                }
	                	logger.debug("MUNICIPIO: "+(auxCell!=null?auxCell.getStringCellValue():""+"|"));
		                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Calle' (N) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(13)),"-"));
	                }
	                
	                //NUM. EXTERIOR
                    try {
                    	auxCell=row.getCell(14);
                    	numExt = auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue()):"";
                    	if(Constantes.SI.equalsIgnoreCase(configCampo.get(14).get("OBLIGATORIO"))){
		                	if(StringUtils.isBlank(numExt)){
		                		throw new ApplicationException("El Num. exterior no puede ir en blanco");
		                	}
		                }
                    	logger.debug("NUM. EXT: "+(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|"));
                    	bufferLinea.append(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|");
                    } catch(Exception ex2) {
                        logger.warn("error al leer el No. exterior, se intentara como string:",ex2);
                        try {
                        	auxCell = row.getCell(14);
                            numExt  = auxCell!=null?auxCell.getStringCellValue():"";
                            if(Constantes.SI.equalsIgnoreCase(configCampo.get(14).get("OBLIGATORIO"))){
    		                	if(StringUtils.isBlank(numExt)){
    		                		throw new ApplicationException("El Num. exterior no puede ir en blanco");
    		                	}
    		                }
                            logger.debug("NUM. EXT: "+(auxCell!=null?auxCell.getStringCellValue():""+"|"));
    		                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
                        }
                        catch(Exception ex) {
                            filaBuena = false;
                            bufferErroresCenso.append(Utils.join("Error en el campo 'Numero exterior' (O) de la fila ",fila," "));
                        }
                    } finally {
                        bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(14)),"-"));
                    }
	                
	                //NUM. INTERIOR
                    try {
                    	auxCell=row.getCell(15);
                    	numInt = auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue()):"";
                    	if(Constantes.SI.equalsIgnoreCase(configCampo.get(15).get("OBLIGATORIO"))){
		                	if(StringUtils.isBlank(numInt)){
		                		throw new ApplicationException("El Num. interior no puede ir en blanco");
		                	}
		                }
                    	logger.debug("NUM. INT: "+(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|"));
                    	bufferLinea.append(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|");
                    } catch(Exception ex2) {
                        logger.warn("error al leer el No. interior, se intentara como string:",ex2);
                        try {
                        	auxCell = row.getCell(15);
                        	numInt  = auxCell!=null?auxCell.getStringCellValue():"";
                            if(Constantes.SI.equalsIgnoreCase(configCampo.get(15).get("OBLIGATORIO"))){
    		                	if(StringUtils.isBlank(numExt)){
    		                		throw new ApplicationException("El Numero no puede ir en blanco");
    		                	}
    		                }
                            logger.debug("NUM. INT: "+(auxCell!=null?auxCell.getStringCellValue():""+"|"));
    		                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
                        }
                        catch(Exception ex) {
                            filaBuena = false;
                            bufferErroresCenso.append(Utils.join("Error en el campo 'Numero interior' (P) de la fila ",fila," "));
                        }
                    } finally {
                        bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(15)),"-"));
                    }
                    
	                
	                //RFC
	                try {
	                	auxCell=row.getCell(16);
	                	rfcAsegurado = auxCell!=null?auxCell.getStringCellValue():"";
	                	if(Constantes.SI.equalsIgnoreCase(configCampo.get(16).get("OBLIGATORIO"))){
		                	if(StringUtils.isBlank(rfcAsegurado)){
		                		throw new ApplicationException("El RFC no puede ir en blanco");
		                	}
		                }
	                	logger.debug("RFC: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
		                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
		                
		                if((auxCell==null||StringUtils.isBlank(auxCell.getStringCellValue()))
		                		&&pagoRepartido&&"T".equals(parentesco)){
		                	throw new ApplicationException("Sin rfc para un titular en pago repartido");
		                }
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'RFC' (Q) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(16)),"-"));
	                }
	                
	                
	              //CORREO
	                try {
		                auxCell=row.getCell(17);
		                emailAseg = auxCell!=null?auxCell.getStringCellValue():"";
		                if(Constantes.SI.equalsIgnoreCase(configCampo.get(17).get("OBLIGATORIO"))){
		                	if(StringUtils.isBlank(emailAseg)){
		                		throw new ApplicationException("El email no puede ir en blanco");
		                	}
		                }
		                logger.debug("CORREO: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
		                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Correo' (R) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(17)),"-"));
	                }
	                
		            //TELEFONO
	                try {
		                auxCell=row.getCell(18);
		                telefono = auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue()):"";
		                if(Constantes.SI.equalsIgnoreCase(configCampo.get(18).get("OBLIGATORIO"))){
		                	if(StringUtils.isBlank(telefono)){
		                		throw new ApplicationException("El telefono no puede ir en blanco");
		                	}
		                }
		                logger.debug("TELEFONO: "+(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|"));
		                bufferLinea.append(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|");
                	} catch(Exception ex){
	                	try {
	                		logger.warn("error al leer telefono como numero, se intentara como string:",ex);
	                		auxCell=row.getCell(18);
			                telefono = auxCell!=null?auxCell.getStringCellValue():"";
			                if(Constantes.SI.equalsIgnoreCase(configCampo.get(18).get("OBLIGATORIO"))){
			                	if(StringUtils.isBlank(telefono)){
			                		throw new ApplicationException("El telefono no puede ir en blanco");
			                	}
			                }
			                logger.debug("TELEFONO: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
			                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
	                	} catch(Exception e){
		                	filaBuena = false;
		                	logger.debug("Valor de e:{}",e);
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Telefono' (S) de la fila ",fila," "));
		                }
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(18)),"-"));
	                }

		            //IDENTIDAD NO.DE EMPLEADO
		            try {
		                auxCell=row.getCell(19);
		                identidad = auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue()):"";
		                if(Constantes.SI.equalsIgnoreCase(configCampo.get(19).get("OBLIGATORIO"))){
		                	if(StringUtils.isBlank(identidad)){
		                		throw new ApplicationException("La identidad no puede ir en blanco");
		                	}
		                }
		                
		                if(cdunieco.equalsIgnoreCase("1403")){
		                	if(StringUtils.isNotBlank(identidad)){
		                		String identidadModificada[] = identidad.split("\\-");
		                		String seccion1 = StringUtils.leftPad(identidadModificada[0].toString(), 6, "0");
		                		logger.debug("Seccion 1 IDENTIDAD : {}",seccion1);
		                		String seccion2 = StringUtils.leftPad(identidadModificada[1].toString(), 2, "0");
		                		logger.debug("Seccion 2 IDENTIDAD : {}",seccion2);
		                		
		                		if(StringUtils.isNumeric(seccion1) && StringUtils.isNumeric(seccion2)){
		                			bufferLinea.append(seccion1.toString()+"-"+seccion2.toString()+"|");
		                		}else{
		                			//mandamos excepcion
			                		throw new ApplicationException("No es numero");
		                		}
		                	}
		                }
		                logger.debug("IDENTIDAD: "+(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|"));
		                bufferLinea.append(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|");		                
                	} catch(Exception ex){
	                	try {
	                		logger.warn("error al leer telefono como numero, se intentara como string:",ex);
	                		auxCell=row.getCell(19);
	                		identidad = auxCell!=null?auxCell.getStringCellValue():"";
			                if(Constantes.SI.equalsIgnoreCase(configCampo.get(19).get("OBLIGATORIO"))){
			                	if(StringUtils.isBlank(identidad)){
			                		throw new ApplicationException("La identidad no puede ir en blanco");
			                	}
			                }
			                
			                if(cdunieco.equalsIgnoreCase("1403")){
			                	if(StringUtils.isNotBlank(identidad)){
			                		String identidadModificada[] = identidad.split("\\-");
			                		String seccion1 = StringUtils.leftPad(identidadModificada[0].toString(), 6, "0");
			                		logger.debug("Seccion 1 IDENTIDAD : {}",seccion1);
			                		String seccion2 = StringUtils.leftPad(identidadModificada[1].toString(), 2, "0");
			                		logger.debug("Seccion 2 IDENTIDAD : {}",seccion2);
			                		
			                		if(StringUtils.isNumeric(seccion1) && StringUtils.isNumeric(seccion2)){
			                			bufferLinea.append(seccion1.toString()+"-"+seccion2.toString()+"|");
			                		}else{
			                			//mandamos excepcion
				                		throw new ApplicationException("No es numero");
			                		}
			                	}
			                }
			                
			                logger.debug("IDENTIDAD: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
			                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
			                
	                	} catch(Exception e){
		                	filaBuena = false;
		                	logger.debug("Valor de e:{}",e);
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Identidad' (T) de la fila ",fila," "));
		                }
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(19)),"-"));
	                }
		            
	                //FECHA DE RECONOCIMIENTO DE ANTIGUEDAD
	                try {
	                	auxCell=row.getCell(20);
		                auxDate=auxCell!=null?auxCell.getDateCellValue():null;
		                if(auxDate!=null) {
		                	Calendar cal = Calendar.getInstance();
		                	cal.setTime(auxDate);
		                	if(cal.get(Calendar.YEAR)>2100 ||cal.get(Calendar.YEAR)<1900) {
		                		throw new ApplicationException("El anio de la fecha no es valido");
		                	}
		                }
		                
		                fecanti = auxDate!=null?renderFechas.format(auxDate):"";
		                if(Constantes.SI.equalsIgnoreCase(configCampo.get(20).get("OBLIGATORIO"))){
		                	if(StringUtils.isBlank(fecanti)){
		                		throw new ApplicationException("La fecha de antiguedad no puede ir en blanco");
		                	}
		                }
		                
        				logger.debug("FECHA RECONOCIMIENTO ANTIGUEDAD: "+(auxDate!=null?renderFechas.format(auxDate)+"|":"|"));
		                bufferLinea.append(auxDate!=null?renderFechas.format(auxDate)+"|":"|");
                	} catch(Exception ex) {
	                	// Segundo intento, se tratara el campo como String:
	                	try {
							auxDate= renderFechas.parse(row.getCell(20).getStringCellValue());
							if(auxDate!=null) {
			                	Calendar cal = Calendar.getInstance();
			                	cal.setTime(auxDate);
			                	if(cal.get(Calendar.YEAR)>2100 ||cal.get(Calendar.YEAR)<1900) {
			                		throw new ApplicationException("El anio de la fecha no es valido");
			                	}
			                }
							fecanti = auxDate!=null?renderFechas.format(auxDate):"";
							if(Constantes.SI.equalsIgnoreCase(configCampo.get(20).get("OBLIGATORIO"))){
			                	if(StringUtils.isBlank(fecanti)){
			                		throw new ApplicationException("La fecha de antiguedad no puede ir en blanco");
			                	}
			                }
			                logger.debug("FECHA RECONOCIMIENTO ANTIGUEDAD: "+(auxDate!=null?renderFechas.format(auxDate)+"|":"|"));
			                bufferLinea.append(auxDate!=null?renderFechas.format(auxDate)+"|":"|");
		                } catch (Exception e) {
							filaBuena = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Fecha de reconocimiento antiguedad' (U) de la fila ",fila," "));
						}
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(20)),"-"));
	                }
	                
		            //OCUPACION
	                try {
		                auxCell=row.getCell(21);
		                ocupacion = auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue()):"";
		                if(Constantes.SI.equalsIgnoreCase(configCampo.get(21).get("OBLIGATORIO"))){
		                	if(StringUtils.isBlank(ocupacion)){
		                		throw new ApplicationException("La ocupacion no puede ir en blanco");
		                	}
		                }
		                logger.debug("OCUPACION: "+(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|"));
		                bufferLinea.append(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|");
                	} catch(Exception ex){
	                	try {
	                		logger.warn("error en ocupacion como numero, se intentara como string:",ex);
	                		auxCell=row.getCell(21);
	                		ocupacion = auxCell!=null?auxCell.getStringCellValue():"";
			                if(Constantes.SI.equalsIgnoreCase(configCampo.get(21).get("OBLIGATORIO"))){
			                	if(StringUtils.isBlank(ocupacion)){
			                		throw new ApplicationException("La ocupacion no puede ir en blanco");
			                	}
			                }
			                logger.debug("OCUPACION: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
			                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
	                	} catch(Exception e){
		                	filaBuena = false;
		                	logger.debug("Valor de e:{}",e);
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Ocupaci&oacute;n' (V) de la fila ",fila," "));
		                }
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(21)),"-"));
	                }
	                
	                //EXT. OCUPACIONAL
                	try {
	                	auxCell=row.getCell(22);
		                extOcupacion = auxCell!=null?String.format("%.2f",auxCell.getNumericCellValue()):"";
		                if(Constantes.SI.equalsIgnoreCase(configCampo.get(22).get("OBLIGATORIO"))){
		                	if(StringUtils.isBlank(extOcupacion)){
		                		throw new ApplicationException("la Ext. ocupacional no puede ir en blanco");
		                	}
		                }
		                
		                logger.debug("EXTRAPRIMA OCUPACION: "+(auxCell!=null?String.format("%.2f",auxCell.getNumericCellValue())+"|":"|"));
		                bufferLinea.append(auxCell!=null?String.format("%.2f",auxCell.getNumericCellValue())+"|":"|");
	                } catch(Exception ex) {
	                    logger.error("error al leer la extraprima de ocupaci�n como numero, se intentara como string:",ex);
	                    try {
	                    	auxCell=row.getCell(22);
			                extOcupacion = auxCell!=null?auxCell.getStringCellValue():"";
			                if(Constantes.SI.equalsIgnoreCase(configCampo.get(22).get("OBLIGATORIO"))){
			                	if(StringUtils.isBlank(extOcupacion)){
			                		throw new ApplicationException("la Ext. ocupacional no puede ir en blanco");
			                	}
			                }
			                
			                logger.debug("EXTRAPRIMA OCUPACION: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
			                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
	                        
	                    } catch(Exception ex2) {
	                        filaBuena = false;
	                        bufferErroresCenso.append(Utils.join("Error en el campo 'Extraprima de ocupacion' (W) de la fila ",fila," "));
	                    }
	                } finally {
	                    bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(22)),"-"));
	                }

                	//PESO
                	try {
	                	auxCell=row.getCell(23);
	                	peso = auxCell!=null?String.format("%.2f",auxCell.getNumericCellValue()):"";
	                	if(Constantes.SI.equalsIgnoreCase(configCampo.get(23).get("OBLIGATORIO"))){
		                	if(StringUtils.isBlank(peso)){
		                		throw new ApplicationException("El peso no puede ir en blanco");
		                	}
		                }
	                	
	                	logger.debug("PESO: "+(auxCell!=null?String.format("%.2f",auxCell.getNumericCellValue())+"|":"|"));
		                bufferLinea.append(auxCell!=null?String.format("%.2f",auxCell.getNumericCellValue())+"|":"|");
	                } catch(Exception ex) {
	                    logger.error("error al leer el peso como numero, se intentara como string:",ex);
	                    try {
	                    	auxCell=row.getCell(23);
			                peso =auxCell!=null?auxCell.getStringCellValue():"";
			                if(Constantes.SI.equalsIgnoreCase(configCampo.get(23).get("OBLIGATORIO"))){
			                	if(StringUtils.isBlank(peso)){
			                		throw new ApplicationException("El peso no puede ir en blanco");
			                	}
			                }
			                
			                logger.debug("PESO: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
			                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
	                        
	                    } catch(Exception ex2) {
	                        filaBuena          = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Peso' (X) de la fila ",fila," "));
	                    }
	                } finally {
	                    bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(23)),"-"));
	                }
                	
                	//ESTATURA
                	try {
	                	auxCell=row.getCell(24);
	                	estatura = auxCell!=null?String.format("%.2f",auxCell.getNumericCellValue()):"";
	                	if(Constantes.SI.equalsIgnoreCase(configCampo.get(24).get("OBLIGATORIO"))){
		                	if(StringUtils.isBlank(estatura)){
		                		throw new ApplicationException("La estatura no puede ir en blanco");
		                	}
		                }
	                	
	                	logger.debug("ESTATURA: "+(auxCell!=null?String.format("%.2f",auxCell.getNumericCellValue())+"|":"|"));
		                bufferLinea.append(auxCell!=null?String.format("%.2f",auxCell.getNumericCellValue())+"|":"|");
	                } catch(Exception ex) {
	                    logger.error("error al leer la estatura como numero, se intentara como string:",ex);
	                    try {
	                    	auxCell=row.getCell(24);
	                    	estatura = auxCell!=null?auxCell.getStringCellValue():"";
	                    	if(Constantes.SI.equalsIgnoreCase(configCampo.get(24).get("OBLIGATORIO"))){
			                	if(StringUtils.isBlank(estatura)){
			                		throw new ApplicationException("La estatura no puede ir en blanco");
			                	}
			                }
	                    	
	                    	logger.debug("ESTATURA: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
			                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
	                        
	                    } catch(Exception ex2) {
	                        filaBuena          = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Estatura' (Y) de la fila ",fila," "));
	                    }
	                } finally {
	                    bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(24)),"-"));
	                }

                	//EXT. SOBREPESO
                	try {
	                	auxCell=row.getCell(25);
		                extSobrepeso = auxCell!=null?String.format("%.2f",auxCell.getNumericCellValue()):"";
		                if(Constantes.SI.equalsIgnoreCase(configCampo.get(25).get("OBLIGATORIO"))){
		                	if(StringUtils.isBlank(extSobrepeso)){
		                		throw new ApplicationException("El sobrepeso no puede ir en blanco");
		                	}
		                }
		                
		                logger.debug("EXTRAPRIMA SOBREPESO: "+(auxCell!=null?String.format("%.2f",auxCell.getNumericCellValue())+"|":"|"));
		                bufferLinea.append(auxCell!=null?String.format("%.2f",auxCell.getNumericCellValue())+"|":"|");
	                } catch(Exception ex) {
	                    logger.error("error al leer la extraprima como numero, se intentara como string:",ex);
	                    try {
	                    	auxCell=row.getCell(25);
			                extSobrepeso = auxCell!=null?auxCell.getStringCellValue():"";
			                if(Constantes.SI.equalsIgnoreCase(configCampo.get(25).get("OBLIGATORIO"))){
			                	if(StringUtils.isBlank(extSobrepeso)){
			                		throw new ApplicationException("El sobrepeso no puede ir en blanco");
			                	}
			                }
			                
			                logger.debug("EXTRAPRIMA SOBREPESO: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
			                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
	                        
	                    } catch(Exception ex2) {
	                        filaBuena          = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Extraprima de sobrepeso' (Z) de la fila ",fila," "));
	                    }
	                } finally {
	                    bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(25)),"-"));
	                }
	                
                	//ESTADO CIVIL
	                try {
	                	auxCell     = row.getCell(26);
	                	estadoCivil = auxCell!=null?auxCell.getStringCellValue():"";
		                
	                	if(Constantes.SI.equalsIgnoreCase(configCampo.get(26).get("OBLIGATORIO"))){
		                	if(StringUtils.isBlank(estadoCivil)){
		                		throw new ApplicationException("El estado civil no puede ir en blanco");
		                	}
		                }
	                	
	                	estadoCivil = auxCell!=null?auxCell.getStringCellValue():"S";
		                
	                	//TODO: quitar cdtipsit estatico y ponerlo por subramo
	                	if("SSI".equals(cdtipsit)&&StringUtils.isBlank(estadoCivil)) {
	                		throw new ApplicationException("El estado civil es obligatorio");
	                	}
	                	
	                	if(StringUtils.isNotBlank(estadoCivil)){
		                	if(
        						!estadoCivil.equals("C") &&!estadoCivil.equals("S") &&!estadoCivil.equals("D")
        						&&!estadoCivil.equals("V")&&!estadoCivil.equals("O")) {
	                			throw new ApplicationException("El estado civil no se reconoce [C, S, D, V, O]");
	                		}
	                	}
	                	
		                logger.debug(new StringBuilder("EDO CIVIL: ").append(estadoCivil).append("|").toString());
		                bufferLinea.append(new StringBuilder(estadoCivil).append("|").toString());
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Estado civil' (AA) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(26)),"-"));
	                }

	                //FECHA INGRESO
                    try
                    {
                        auxDate=row.getCell(27)!=null?row.getCell(27).getDateCellValue():null;
                        if(auxDate!=null) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(auxDate);
                            if(cal.get(Calendar.YEAR)>2100||cal.get(Calendar.YEAR)<1900) {
                                throw new ApplicationException("El anio de la fecha de ingreso no es valido");
                            }
                        }
                        
                        feingreso = auxDate!=null?renderFechas.format(auxDate):"";
                        if(Constantes.SI.equalsIgnoreCase(configCampo.get(27).get("OBLIGATORIO"))){
		                	if(StringUtils.isBlank(feingreso)){
		                		throw new ApplicationException("La fecha de ingreso no puede ir en blanco");
		                	}
		                }
                        
                        logger.debug(new StringBuilder("FECHA INGRESO EMPLEADO: ").append(auxDate!=null?renderFechas.format(auxDate):"").append("|").toString());
                        bufferLinea.append(auxDate!=null?new StringBuilder(renderFechas.format(auxDate)).append("|").toString():"|");
                    } catch(Exception ex) {
                        // Segundo intento, se tratara el campo como String:
                        try {
                            auxDate= ((extraerStringDeCelda(row.getCell(27))!=null) && 
                                        StringUtils.isNotBlank(extraerStringDeCelda(row.getCell(27)))) ?
                                                renderFechas.parse(extraerStringDeCelda(row.getCell(27))):
                                                null;
                            if(auxDate!=null) {
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(auxDate);
                                if(cal.get(Calendar.YEAR)>2100 ||cal.get(Calendar.YEAR)<1900) {
                                    throw new ApplicationException("El anio de la fecha no es valido");
                                }
                            }
                            
                            feingreso = auxDate!=null?renderFechas.format(auxDate):"";
                            if(Constantes.SI.equalsIgnoreCase(configCampo.get(27).get("OBLIGATORIO"))){
    		                	if(StringUtils.isBlank(feingreso)){
    		                		throw new ApplicationException("La fecha de ingreso no puede ir en blanco");
    		                	}
    		                }
                            
                            logger.debug("FECHA INGRESO EMPLEADO: "+(auxDate!=null?renderFechas.format(auxDate)+"|":"|"));
                            bufferLinea.append(auxDate!=null?renderFechas.format(auxDate)+"|":"|");
                        } catch (Exception e) {
                            logger.debug("Valor del Error => "+e);
                            filaBuena = false;
                            bufferErroresCenso.append(Utils.join("Error en el campo 'Fecha de ingreso empleado' (AB) de la fila ",fila," "));
                        }
                    } finally {
                        bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(27)),"-"));
                    }
                    
                    //ID SISA
	                try {
		                auxCell=row.getCell(28);
		                idSisa = auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue()):"";
		                
		                if(Constantes.SI.equalsIgnoreCase(configCampo.get(28).get("OBLIGATORIO"))){
		                	if(StringUtils.isBlank(idSisa)){
		                		throw new ApplicationException("El identificador no puede ir en blanco");
		                	}
		                }
		                logger.debug("ID SISA: "+(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|"));
		                bufferLinea.append(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|");
                	} catch(Exception ex2) {
	                	logger.warn("error al leer la clave de SISA, se intentara como string:",ex2);
	                	try {
	                		auxCell=row.getCell(28);
	                		idSisa = auxCell!=null?auxCell.getStringCellValue():"";
	                		if(Constantes.SI.equalsIgnoreCase(configCampo.get(28).get("OBLIGATORIO"))){
			                	if(StringUtils.isBlank(idSisa)){
			                		throw new ApplicationException("El identificador no puede ir en blanco");
			                	}
			                }
	                		logger.debug("ID SISA: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
	                		bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
	                	} catch(Exception ex) {
		                	filaBuena = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Id. SISA' (AC) de la fila ",fila," "));
		                }
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(28)),"-"));
	                }
	                
	                //PLAZA
	                try {
		                auxCell=row.getCell(29);
		                plaza = auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue()):"";
		                if(Constantes.SI.equalsIgnoreCase(configCampo.get(29).get("OBLIGATORIO"))){
		                	if(StringUtils.isBlank(plaza)){
		                		throw new ApplicationException("La plaza no puede ir en blanco");
		                	}
		                }
		                logger.debug("PLAZA: "+(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|"));
		                bufferLinea.append(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|");
                	} catch(Exception ex){
	                	try {
	                		logger.warn("error al leer plaza como numero, se intentara como string:",ex);
	                		auxCell=row.getCell(29);
	                		plaza = auxCell!=null?auxCell.getStringCellValue():"";
			                if(Constantes.SI.equalsIgnoreCase(configCampo.get(29).get("OBLIGATORIO"))){
			                	if(StringUtils.isBlank(plaza)){
			                		throw new ApplicationException("La plaza no puede ir en blanco");
			                	}
			                }
			                logger.debug("PLAZA: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
			                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
	                	} catch(Exception e){
		                	filaBuena = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Plaza' (AD) de la fila ",fila," "));
		                }
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(29)),"-"));
	                }
	                
	                
                    //ID. ASEGURADO
	                try {
                        logger.debug("ID. ASEGURADO: "+(String.format("%.0f",row.getCell(30).getNumericCellValue())+"|"));
                        auxCell=row.getCell(30);
                        cveAsegurado = auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue()):"";
		                
                        if(Constantes.SI.equalsIgnoreCase(configCampo.get(30).get("OBLIGATORIO"))){
		                	if(StringUtils.isBlank(cveAsegurado)){
		                		throw new ApplicationException("El Asegurado no puede ir en blanco");
		                	}
		                }
                        bufferLinea.append(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|");
                        /*boolean exitoValidacion;
                        if(StringUtils.isNotBlank(cveAsegurado) && (Integer.parseInt(cveAsegurado) > 0)){
                            long timestamp=System.currentTimeMillis();
                            Map<String,Object>managerResult=personasManager.obtenerPersonaPorCdperson(cveAsegurado,timestamp);
                            exitoValidacion = (Boolean)managerResult.get("exito");
                           if(exitoValidacion){
                        	   logger.debug("ID ASEGURADO: "+(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|"));
                        	   bufferLinea.append(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|");
                           }else{
                               filaBuena = false;
                               bufferErroresCenso.append(Utils.join("Error en el campo 'Id. Asegurado' :"+cveAsegurado+" ,no se encuentra en SICAPS (AE) de la fila ",fila," "));
                           }                            
                        }else{
                            String respuesta  = personasManager.obtieneAseguradoSICAPS((nombre1+" "+nombre2),apellidoP,apellidoM,renderFechas.parse(fechaNac));
                            if(Integer.parseInt(respuesta) > 0){
                                filaBuena = false;
                                bufferErroresCenso.append(Utils.join("Error en el campo 'Id. Asegurado'. El asegurado ya se encuentra en SICAPS clave: "+respuesta +" (AE) de la fila ",fila," "));
                            }else{
                            	logger.debug("ID ASEGURADO: "+(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|"));
                            	bufferLinea.append(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|");
                            }                            
                        }*/
                    } catch(Exception ex2) { 
                        logger.warn("error al leer el campo 'Id. Asegurado', se intentara como string ==>");
                        try {
                            auxCell=row.getCell(30);
                            cveAsegurado = auxCell!=null?auxCell.getStringCellValue():"";
                            
                            if(Constantes.SI.equalsIgnoreCase(configCampo.get(30).get("OBLIGATORIO"))){
    		                	if(StringUtils.isBlank(cveAsegurado)){
    		                		throw new ApplicationException("El Asegurado no puede ir en blanco");
    		                	}
    		                }
                            bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
                            /*boolean exitoValidacion;
                            if(StringUtils.isNotBlank(cveAsegurado)){
                                long timestamp=System.currentTimeMillis();
                                Map<String,Object>managerResult=personasManager.obtenerPersonaPorCdperson(cveAsegurado,timestamp);
                                exitoValidacion = (Boolean)managerResult.get("exito");
                               if(exitoValidacion){
                            	   logger.debug("ID ASEGURADO: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
                            	   bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
                               }else{
                                   filaBuena = false;
                                   bufferErroresCenso.append(Utils.join("Error en el campo 'Id. Asegurado' :"+cveAsegurado+" ,no se encuentra en SICAPS (AE) de la fila ",fila," "));
                               }                            
                            }else{
                                String respuesta  = personasManager.obtieneAseguradoSICAPS((nombre1+" "+nombre2),apellidoP,apellidoM,renderFechas.parse(fechaNac));
                                if(Integer.parseInt(respuesta) > 0){
                                    filaBuena = false;
                                    bufferErroresCenso.append(Utils.join("Error en el campo 'Id. Asegurado'. El asegurado ya se encuentra en SICAPS clave: "+respuesta +" (AE) de la fila ",fila," "));
                                }else{
                                	logger.debug("ID ASEGURADO: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
                                	bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
                                }                                
                            }*/
                            
                            //bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
                        } catch(Exception ex) {
                            filaBuena = false;
                            bufferErroresCenso.append(Utils.join("Error en el campo 'Id. Asegurado' (AE) de la fila ",fila," "));
                        }
                    } finally {
                        bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(30)),"-"));
                    }                    
	                
	                //TRAMITE
	                if(TipoEndoso.RENOVACION.getCdTipSup().toString().equalsIgnoreCase(esRenovacion)){
			            try {
			                auxCell=row.getCell(31);
			                String tramiteRe = auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue()):"";
			                if(Constantes.SI.equalsIgnoreCase(configCampo.get(31).get("OBLIGATORIO"))){
			                	if(StringUtils.isBlank(tramiteRe)){
			                		throw new ApplicationException("El tramite no puede ir en blanco");
			                	}
			                }
			                if(tramiteRe!=null){
			                	if(!ntramite.equalsIgnoreCase(tramiteRe)){
	                                throw new ApplicationException("El tramite no concuerda");
	                            }
			                }else{
	                            throw new ApplicationException("El tramite no puede ser null");
	                        }
			                
	                	} catch(Exception ex){
		                	try {
		                		logger.warn("error al leer tramite como numero, se intentara como string:",ex);
		                		auxCell=row.getCell(31);
		                		String tramiteRe = auxCell!=null?auxCell.getStringCellValue():"";
				                if(Constantes.SI.equalsIgnoreCase(configCampo.get(31).get("OBLIGATORIO"))){
				                	if(StringUtils.isBlank(tramiteRe)){
				                		throw new ApplicationException("El tramite no puede ir en blanco");
				                	}
				                }
				                
				                if(tramiteRe!=null){
				                	if(!ntramite.equalsIgnoreCase(tramiteRe)){
		                                throw new ApplicationException("El tramite no concuerda");
		                            }
				                }else{
		                            throw new ApplicationException("El tramite no puede ser null");
		                        }
		                	} catch(Exception e){
			                	filaBuena = false;
			                	bufferErroresCenso.append(Utils.join("Error en el campo 'Tramite' (AF) de la fila ",fila," "));
			                }
		                } finally {
		                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(31)),"-"));
		                }
	                }
	                
	              	logger.debug(Utils.log("** NUEVA_FILA (filaBuena=",filaBuena,",cdgrupo=",cdgrupo,") **"));
	                
	                if(filaBuena)
	                {
	                    familias.put(nFamilia,Utils.join(familias.get(nFamilia),bufferLinea.toString(),"\n"));
	                	filasProcesadas = filasProcesadas + 1;
	                	gruposValidos[((int)cdgrupo)-1]=true;
	                	

	                	Map<String,String> params =  new HashMap<String, String>();
    					
	                	params.put("pv_cdunieco_i"       , cdunieco);
    					params.put("pv_cdramo_i"         , cdramo);
    					params.put("pv_estado_i"         , "W");
    					params.put("pv_nmpoliza_i"       , nmpoliza);
    					params.put("pv_cdgrupo_i"        , String.format("%.0f",cdgrupo));
    					params.put("pv_parentesco_i"     , parentesco);
    					params.put("pv_dsapellido_i"     , apellidoP);
    					params.put("pv_dsapellido1_i"    , apellidoM);
    					params.put("pv_dsnombre_i"       , nombre1);
    					params.put("pv_dsnombre1_i"      , nombre2);
    					params.put("pv_otsexo_i"         , sexo);
    					params.put("pv_fenacimi_i"       , fechaNac);
    					params.put("pv_cdpostal_i"       , codpostal);
    					params.put("pv_dsestado_i"       , estado);
    					params.put("pv_dsmunicipio_i"    , municipio);
    					params.put("pv_dscolonia_i"      , colonia);
    					params.put("pv_dsdomici_i"       , calle);
    					params.put("pv_nmnumero_i"       , numExt);
    					params.put("pv_nmnumint_i"       , numInt);
    					params.put("pv_cdrfc_i"          , rfcAsegurado);
    					params.put("pv_dsemail_i"        , emailAseg);
    					params.put("pv_nmtelefo_i"       , telefono);
    					params.put("pv_identidad_i"      , identidad);
    					params.put("pv_fecantig_i"       , fecanti);
    					params.put("pv_expocupacion_i"   , extOcupacion);
    					params.put("pv_peso_i"           , peso);
    					params.put("pv_estatura_i"       , estatura);
    					params.put("pv_expsobrepeso_i"   , extSobrepeso);
    					params.put("pv_edocivil_i"       , estadoCivil);
    					params.put("pv_feingresoempleo_i", feingreso);
    					params.put("pv_plaza_i"          , plaza);
    					params.put("pv_idasegurado_i"    , cveAsegurado);
    					filasFamilia.add(params);
	    					
	                }
	                else
	                {
	                	filasError = filasError + 1;
	                	bufferErroresCenso.append(Utils.join(": ",bufferLineaStr.toString(),"\n"));
	                	estadoFamilias.put(nFamilia,false);
	                	
	                	if(!errorFamilia.containsKey(nFamilia))
	                	{
	                		errorFamilia.put(nFamilia,fila);
	                	}
	                }
	                
	                if(cdgrupo>0d && cdgrupo<=olist1.size())
	                {
	                	logger.debug(Utils.log("cdgrupo=",cdgrupo,", valido=",gruposValidos[((int)cdgrupo)-1]));
	                }
	                else
	                {
	                	logger.debug(Utils.log("cdgrupo=",cdgrupo,", !no se puede imprimir valido"));
	                }
	            }
	            
	            
	            /* Solo si se quiere que el error de errores de Codigos postales salga apart del conjunto de demas errores 
	             * antes de seguir procesando e impedir que continue el flujo.
	             *if(exito && !exitoCPs)
	            {
	            	exito = false;
	            	respuesta = erroresCP.toString();
	            }*/
	            
	            if(exito)
	            {
	            	for(int i=0;i<gruposValidos.length;i++)
	            	{
	            		logger.debug(Utils.log("gruposValidos[i]=",gruposValidos[i]));
	            	}
	            }
	            
	            if(exito)
	            {
	            	boolean       sonGruposValidos = true;
	            	StringBuilder errorGrupos      = new StringBuilder();
	            	for(int i=0;i<gruposValidos.length;i++)
	            	{
	            		if(!gruposValidos[i])
	            		{
	            			sonGruposValidos = false;
	            			errorGrupos.append("Debe haber al menos un asegurado v\u00E1lido para el grupo ").append(i+1).append("\n");
	            		}
	            	}
	            	if(!sonGruposValidos)
	            	{
	            		exito           = false;
	            		respuesta       = errorGrupos.append("\n")
	            				.append(bufferErroresCenso.toString())
	            				.append("\nError #").append(System.currentTimeMillis()).toString();
	            		respuestaOculta = respuesta;
	            		logger.error(bufferErroresCenso.toString());
	            		logger.error(respuesta);
	            	}
	            }
	            
	            if(exito)
				{
	            	logger.debug("\nFamilias: {}\nEstado familias: {}\nErrorFamilia: {}\nTitulares: {}"
		            		,familias,estadoFamilias,errorFamilia,titulares);
		            
		            for(Entry<Integer,Boolean>en:estadoFamilias.entrySet())
		            {
		            	int     n = en.getKey();
		            	boolean v = en.getValue();
		            	if(v)
		            	{
		            		output.print(familias.get(n));
		            	}
		            	else
		            	{
		            		bufferErroresCenso.append(Utils.join("La familia ",n," del titular '",titulares.get(n),"' no fue incluida por error en la fila ",errorFamilia.get(n),"\n"));
		            	}
		            }
		            
					smap1.put("erroresCenso"    , bufferErroresCenso.toString());
					smap1.put("filasLeidas"     , Integer.toString(filasLeidas));
					smap1.put("filasProcesadas" , Integer.toString(filasProcesadas));
					smap1.put("filasErrores"    , Integer.toString(filasError));
					
					if(nFamilia > 0 && !filasFamilia.isEmpty()){
	            		listaFamilias.put(nFamilia, filasFamilia);
	            		
	            		try
	    				{
	            			for(Entry<Integer, List<Map<String,String>>> entry : listaFamilias.entrySet())
	            			{
	            				Integer numFam = entry.getKey();
	            				
	            				if(estadoFamilias.containsKey(numFam) && estadoFamilias.get(numFam)){
	            				    List<Map<String,String>> listaZwcenso = new ArrayList<Map<String,String>> ();
	            					for(Map<String,String> paramsElemFam: listaFamilias.get(numFam)){
	            					    listaZwcenso.add(paramsElemFam);
	            					}
	            					cotizacionManager.insertaRegistroInfoCenso(listaZwcenso);
	            				}
	            			}
	    				}
	    	            catch(Exception ex)
	    	            {
	    	            	logger.error("Error al insetar registro de censo", ex);
	    	            }
	            		
	            	}
				}
	            
	            if(exito)
	            {
	            	try
	            	{
	            		input.close();
	            		output.close();
	            	}
	            	catch(Exception ex)
	            	{
	            		long etimestamp = System.currentTimeMillis();
	            		exito           = false;
	            		respuesta       = "Error al transformar el archivo #"+etimestamp;
	            		respuestaOculta = ex.getMessage();
	            		logger.error(respuesta,ex);
	            	}
	            }
	            
	            logger.debug(""
	            		+ "\n###### "+archivoTxt.getAbsolutePath()+" ######"
						+ "\n##############################################"
						);
				
	            if(exito)
	            {
					exito = FTPSUtils.upload
							(
								dominioServerLayouts
								,userServerLayouts
								,passServerLayouts
								,archivoTxt.getAbsolutePath()
								,directorioServerLayouts+"/"+nombreCenso
						    )
							&&FTPSUtils.upload
							(
								dominioServerLayouts2
								,userServerLayouts
								,passServerLayouts
								,archivoTxt.getAbsolutePath()
								,directorioServerLayouts+"/"+nombreCenso
							);
					
					if(!exito)
					{
						long etimestamp = System.currentTimeMillis();
						exito           = false;
						respuesta       = "Error al transferir archivo al servidor #"+etimestamp;
						respuestaOculta = respuesta;
						logger.error(respuesta);
					}
	            }
				
			}
		}
		
		if(exito&&StringUtils.isBlank(nombreCensoConfirmado))
		{
			smap1.put("nombreCensoParaConfirmar", nombreCenso);
			respuesta = Utils.join("Se ha revisado el censo [REV. ",System.currentTimeMillis(),"]");
		}
		
		logger.debug(""
				+ "\n###### subirCensoCompleto ######"
				+ "\n################################"
				);
		return SUCCESS;
	}
	
	private class tvalositSigsvdefTvalogarContratanteTramiteSigsvalipolObject
	{
		public boolean exito           = false;
		public String  respuesta       = null;
		public String  respuestaOculta = null;
	}
	
	public String generarTramiteGrupo2()
	{
		logger.debug(
				new StringBuilder()
				.append("\n##################################")
				.append("\n###### generarTramiteGrupo2 ######")
				.append("\n###### smap1=").append(smap1)
				.append("\n###### olist1=").append(olist1)
				.toString()
				);
		
		this.session=ActionContext.getContext().getSession();
		
		exito   = true;
		success = true;
		
		String cdunieco                = null;
		String cdramo                  = null;
		String nmpoliza                = null;
		String feini                   = null;
		String fefin                   = null;
		String cdperpag                = null;
		String pcpgocte                = null;
		String ntramite                = null;
		String ntramiteVacio           = null;
		String miTimestamp             = null;
		String tipoCenso               = null;
		String cdtipsit                = null;
		String codpostal               = null;
		String cdedo                   = null;
		String cdmunici                = null;
		String cdagente                = null;
		String cdusuari                = null;
		String cdsisrol                = null;
		String clasif                  = null;
		String LINEA_EXTENDIDA         = null;
		String cdpersonCli             = null;
		String nombreCli               = null;
		String rfcCli                  = null;
		String dsdomiciCli             = null;
		String nmnumeroCli             = null;
		String nmnumintCli             = null;
		String cdelemen                = null;
		String cdpool                  = null;
		String nombreCensoConfirmado   = null;
		String cdideper_               = null;
		String cdideext_               = null;
		String nmpolant                = null;
		String nmrenova                = null;
		
		boolean sincenso      = false;
		boolean censoAtrasado = false;
		boolean resubirCenso  = false;
		boolean complemento   = false;
		boolean asincrono     = false;
		boolean duplicar      = false;
		
		//datos de entrada
		try
		{
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
			cdsisrol       = usuario.getRolActivo().getClave();
			cdelemen       = usuario.getEmpresa().getElementoId();
			
			if(smap1==null)
			{
				throw new ApplicationException("No se recibieron datos");
			}
			cdunieco        = smap1.get("cdunieco");
			cdramo          = smap1.get("cdramo");
			nmpoliza        = smap1.get("nmpoliza");
			feini           = smap1.get("feini");
			fefin           = smap1.get("fefin");
			cdperpag        = smap1.get("cdperpag");
			pcpgocte        = smap1.get("pcpgocte");
			ntramite        = smap1.get("ntramite");
			ntramiteVacio   = smap1.get("ntramiteVacio");
			miTimestamp     = smap1.get("timestamp");
			tipoCenso       = smap1.get("tipoCenso");
			cdtipsit        = smap1.get("cdtipsit");
			codpostal       = smap1.get("codpostal");
			cdedo           = smap1.get("cdedo");
			cdmunici        = smap1.get("cdmunici");
			cdagente        = smap1.get("cdagente");
			clasif          = smap1.get("clasif");
			LINEA_EXTENDIDA = smap1.get("LINEA_EXTENDIDA");
			cdpersonCli     = smap1.get("cdperson");
			nombreCli       = smap1.get("nombre");
			rfcCli          = smap1.get("cdrfc");
			dsdomiciCli     = smap1.get("dsdomici");
			nmnumeroCli     = smap1.get("nmnumero");
			nmnumintCli     = smap1.get("nmnumint");
			cdpool          = smap1.get("cdpool");
			cdideper_       = smap1.get("cdideper_");
			cdideext_       = smap1.get("cdideext_");
			nmpolant        = smap1.get("nmpolant");
			nmrenova        = smap1.get("nmrenova");
			
			String sincensoS      = smap1.get("sincenso");
			sincenso              = StringUtils.isNotBlank(sincensoS)&&sincensoS.equals("S");
			String censoAtrasadoS = smap1.get("censoAtrasado");
			censoAtrasado         = StringUtils.isNotBlank(censoAtrasadoS)&&censoAtrasadoS.equals("S");
			String resubirCensoS  = smap1.get("resubirCenso");
			resubirCenso          = StringUtils.isNotBlank(resubirCensoS)&&resubirCensoS.equals("S");
			
			complemento = "S".equals(smap1.get("complemento"));
			
			nombreCensoConfirmado = smap1.get("nombreCensoConfirmado");
			
			asincrono = StringUtils.isNotBlank(smap1.get("asincrono"))&&smap1.get("asincrono").equalsIgnoreCase("si");
			
			duplicar = "S".equals(smap1.get("duplicar"));
		}
		catch(ApplicationException ax)
		{
			long timestamp  = System.currentTimeMillis();
			exito           = false;
			respuesta       = new StringBuilder(ax.getMessage()).append(" #").append(timestamp).toString();
			respuestaOculta = ax.getMessage();
			logger.error(respuesta,ax);
		}
		catch(Exception ex)
		{
			long timestamp  = System.currentTimeMillis();
			exito           = false;
			respuesta       = new StringBuilder("Error al validar datos #").append(timestamp).toString();
			respuestaOculta = ex.getMessage();
			logger.error(respuesta,ex);
		}
		
		//proceso
		if(exito)
		{
			UserVO usuarioSesion = (UserVO)session.get("USUARIO");
			ManagerRespuestaSmapVO resp=cotizacionManager.generarTramiteGrupo(
					cdunieco
					,cdramo
					,nmpoliza
					,feini
					,fefin
					,cdperpag
					,pcpgocte
					,smap1
					,ntramite
					,ntramiteVacio
					,miTimestamp
					,rutaDocumentosTemporal
					,tipoCenso
					,dominioServerLayouts
					,userServerLayouts
					,passServerLayouts
					,directorioServerLayouts
					,cdtipsit
					,olist1
					,codpostal
					,cdedo
					,cdmunici
					,cdagente
					,cdusuari
					,cdsisrol
					,clasif
					,LINEA_EXTENDIDA
					,cdpersonCli
					,nombreCli
					,rfcCli
					,dsdomiciCli
					,nmnumeroCli
					,nmnumintCli
					,cdelemen
					,sincenso
					,censoAtrasado
					,resubirCenso
					,complemento
					,cdpool
					,nombreCensoConfirmado
					,asincrono
					,cdideper_
					,cdideext_
					,nmpolant
					,StringUtils.isBlank(nmrenova) ? "0" : nmrenova
					,usuarioSesion
					,duplicar
					);
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
				.append("\n###### generarTramiteGrupo2 ######")
				.append("\n##################################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String generarTramiteGrupo()
	{
		this.session=ActionContext.getContext().getSession();
		logger.debug(Utils.log(
				 "\n#################################"
				,"\n###### generarTramiteGrupo ######"
				,"\n###### smap1="  , smap1
				,"\n###### olist1=" , olist1
				));
		try
		{
			UserVO usuario = Utils.validateSession(session);
			
			String paso             = null
			       ,inTimestamp     = smap1.get("timestamp")
			       ,clasif          = smap1.get("clasif")
			       ,LINEA_EXTENDIDA = smap1.get("LINEA_EXTENDIDA")
			       ,cdunieco        = smap1.get("cdunieco")
			       ,cdramo          = smap1.get("cdramo")
			       ,cdtipsit        = smap1.get("cdtipsit")
			       ,nmpoliza        = smap1.get("nmpoliza")
			       ,feini           = smap1.get("feini")
			       ,fefin           = smap1.get("fefin")
			       ,cdpool          = smap1.get("cdpool")
			       ,cdperpag        = smap1.get("cdperpag")
			       ,user            = usuario.getUser()
			       ,cdelemento      = usuario.getEmpresa().getElementoId()
			       ,cdsisrol        = usuario.getRolActivo().getClave()
			       ,nombreCenso     = null
			       ,ntramite        = smap1.get("ntramite")
			       ,cdagente        = smap1.get("cdagente")
			       ,ntramiteVacio   = smap1.get("ntramiteVacio")
			       ,tipoCenso       = smap1.get("tipoCenso")
			       ,pcpgocte        = smap1.get("pcpgocte");
			
			//String ptajepar         = smap1.get("cdreppag");
			
			Date fechaHoy = new Date();
			
			final String LINEA = "1";
			
			boolean esCensoSolo      = StringUtils.isNotBlank(tipoCenso)&&tipoCenso.equalsIgnoreCase("solo")
			        ,hayTramite      = StringUtils.isNotBlank(ntramite)
			        ,hayTramiteVacio = StringUtils.isNotBlank(ntramiteVacio);
			
			String sincensoS       = smap1.get("sincenso")
			       ,censoAtrasadoS = smap1.get("censoAtrasado")
			       ,resubirCensoS  = smap1.get("resubirCenso");
			
			boolean sincenso       = StringUtils.isNotBlank(sincensoS)&&sincensoS.equals("S")
			        ,censoAtrasado = StringUtils.isNotBlank(censoAtrasadoS)&&censoAtrasadoS.equals("S")
			        ,resubirCenso  = StringUtils.isNotBlank(resubirCensoS)&&resubirCensoS.equals("S");
			
			boolean complemento = "S".equals(smap1.get("complemento"));
			
			censo = new File(this.rutaDocumentosTemporal+"/censo_"+inTimestamp);
			
			String nombreCensoConfirmado = smap1.get("nombreCensoConfirmado");
			
			boolean asincrono = StringUtils.isNotBlank(smap1.get("asincrono"))&&smap1.get("asincrono").equalsIgnoreCase("si");
			
			String cdedo      = smap1.get("cdedo")
			       ,cdmunici  = smap1.get("cdmunici")
			       ,codpostal = smap1.get("codpostal");
			
			String nmpolant  = smap1.get("nmpolant")
			       ,nmrenova = smap1.get("nmrenova");
			
			boolean duplicar = "S".equals(smap1.get("duplicar"));
			
			logger.debug(Utils.log(
					"\ninTimestamp: " , inTimestamp
					,"\nclasif: "     , clasif
					,"\ncenso: "      , censo
					));
			
			try
			{
				//nmpoliza
				if(StringUtils.isBlank(nmpoliza) || duplicar)
				{
					paso = "Generando n\u00f1mero de p\u00f3liza";
					logger.debug(paso);
					
					nmpoliza = (String)kernelManager.calculaNumeroPoliza(cdunieco,cdramo,"W").getItemMap().get("NUMERO_POLIZA");
					smap1.put("nmpoliza",nmpoliza);
				}
				else
				{
					paso = "Guardando bloqueo";
					logger.debug(paso);
					
					//cotizacionManager.movimientoTbloqueo(cdunieco,cdramo,"W",nmpoliza,"-8",Constantes.INSERT_MODE);
				}
				
				if(StringUtils.isNotBlank(nombreCensoConfirmado))
				{
					nombreCenso = nombreCensoConfirmado;
				}
				else
				{
					nombreCenso = "censo_"+inTimestamp+"_"+nmpoliza+".txt";
				}
				
				//mpolizas
				paso = "Insertando maestro de p\u00f3liza";
				logger.debug(paso);
				
				Map<String,String>mapaMpolizas=new HashMap<String,String>(0);
	            mapaMpolizas.put("pv_cdunieco"  , cdunieco);
	            mapaMpolizas.put("pv_cdramo"    , cdramo);
	            mapaMpolizas.put("pv_estado"    , "W");
	            mapaMpolizas.put("pv_nmpoliza"  , nmpoliza);
	            mapaMpolizas.put("pv_nmsuplem"  , "0");
	            mapaMpolizas.put("pv_status"    , "V");
	            mapaMpolizas.put("pv_swestado"  , "0");
	            mapaMpolizas.put("pv_nmsolici"  , null);
	            mapaMpolizas.put("pv_feautori"  , null);
	            mapaMpolizas.put("pv_cdmotanu"  , null);
	            mapaMpolizas.put("pv_feanulac"  , null);
	            mapaMpolizas.put("pv_swautori"  , "N");
	            mapaMpolizas.put("pv_cdmoneda"  , "001");
	            mapaMpolizas.put("pv_feinisus"  , null);
	            mapaMpolizas.put("pv_fefinsus"  , null);
	            mapaMpolizas.put("pv_ottempot"  , "R");
	            mapaMpolizas.put("pv_feefecto"  , feini);
	            mapaMpolizas.put("pv_hhefecto"  , "12:00");
	            mapaMpolizas.put("pv_feproren"  , fefin);
	            mapaMpolizas.put("pv_fevencim"  , null);
	            mapaMpolizas.put("pv_nmrenova"  , StringUtils.isBlank(nmrenova) ? "0" : nmrenova);
	            mapaMpolizas.put("pv_ferecibo"  , null);
	            mapaMpolizas.put("pv_feultsin"  , null);
	            mapaMpolizas.put("pv_nmnumsin"  , "0");
	            mapaMpolizas.put("pv_cdtipcoa"  , "N");
	            mapaMpolizas.put("pv_swtarifi"  , "A");
	            mapaMpolizas.put("pv_swabrido"  , null);
	            mapaMpolizas.put("pv_feemisio"  , renderFechas.format(fechaHoy));
	            mapaMpolizas.put("pv_cdperpag"  , cdperpag);
	            mapaMpolizas.put("pv_nmpoliex"  , null);
	            mapaMpolizas.put("pv_nmcuadro"  , "P1");
	            mapaMpolizas.put("pv_porredau"  , "100");
	            mapaMpolizas.put("pv_swconsol"  , "S");
	            mapaMpolizas.put("pv_nmpolant"  , nmpolant);
	            mapaMpolizas.put("pv_nmpolnva"  , null);
	            mapaMpolizas.put("pv_fesolici"  , renderFechas.format(fechaHoy));
	            mapaMpolizas.put("pv_cdramant"  , null);
	            mapaMpolizas.put("pv_cdmejred"  , null);
	            mapaMpolizas.put("pv_nmpoldoc"  , null);
	            mapaMpolizas.put("pv_nmpoliza2" , null);
	            mapaMpolizas.put("pv_nmrenove"  , null);
	            mapaMpolizas.put("pv_nmsuplee"  , null);
	            mapaMpolizas.put("pv_ttipcamc"  , null);
	            mapaMpolizas.put("pv_ttipcamv"  , null);
	            mapaMpolizas.put("pv_swpatent"  , null);
	            mapaMpolizas.put("pv_pcpgocte"  , pcpgocte);
	            mapaMpolizas.put("pv_tipoflot"  , "F");
	            mapaMpolizas.put("pv_agrupador" , cdpool);
	            mapaMpolizas.put("pv_accion"    , "U");
	            kernelManager.insertaMaestroPolizas(mapaMpolizas);
				
				//fechas mpolisit
	            paso = "Actualizando fechas de vigencia de asegurados";
				logger.debug(paso);
				
				cotizacionManager.actualizarFefecsitMpolisit(cdunieco,cdramo,"W",nmpoliza,"0");
				
				//tvalopol
				paso = "Insertando datos adicionales de p\u00f3liza";
				logger.debug(paso);
				
				Map<String,String>params=new HashMap<String,String>();
				params.put("pv_cdunieco"  , cdunieco);
				params.put("pv_cdramo"    , cdramo);
				params.put("pv_estado"    , "W");
				params.put("pv_nmpoliza"  , nmpoliza);
				params.put("pv_nmsuplem"  , "0");
				params.put("pv_status"    , "V");
				params.put("pv_otvalor09" , smap1.get("dctocmer"));
				params.put("pv_otvalor10" , smap1.get("tipoDerPol"));
				params.put("pv_otvalor11" , smap1.get("montoDerPol"));
				params.put("pv_otvalor12" , smap1.get("recargoPers"));
				params.put("pv_otvalor13" , smap1.get("recargoPago"));
				params.put("pv_otvalor14" , smap1.get("cdgiro"));
				params.put("pv_otvalor15" , smap1.get("cdrelconaseg"));
				params.put("pv_otvalor16" , smap1.get("cdformaseg"));
				params.put("pv_otvalor17" , smap1.get("cdperpag"));
				params.put("pv_otvalor19" , smap1.get("morbilidad"));
				//incorporando valor para guardar el numero de contrato en los tramites
				params.put("pv_otvalor20", smap1.get("numcontrato"));
				
				kernelManager.pMovTvalopol(params);
				
				paso = "Guardando relaci\u00f3n p\u00f3liza - contratante";
				logger.debug(paso);
				
				String cdperson  = smap1.get("cdperson");
				String nmorddom  = smap1.get("nmorddom");
				String exiper    = smap1.get("swexiper");
				
				if(StringUtils.isBlank(cdperson)){					
					cdperson = cotizacionManager.guardarContratanteColectivo(cdunieco, cdramo, "W", nmpoliza, "0", smap1.get("cdrfc"), cdperson, smap1.get("nombre"),
							codpostal, cdedo, cdmunici, smap1.get("dsdomici"), smap1.get("nmnumero"), smap1.get("nmnumint"), "1", false, usuario);
					exiper = Constantes.NO;
				}
				
				cotizacionManager.borrarMpoliperSituac0(cdunieco, cdramo, "W", nmpoliza, "0", "1");
				
				LinkedHashMap<String,Object> parametros=new LinkedHashMap<String,Object>(0);
				parametros.put("param01_pv_cdunieco_i" , cdunieco);
				parametros.put("param02_pv_cdramo_i"   , cdramo);
				parametros.put("param03_pv_estado_i"   , "W");
				parametros.put("param04_pv_nmpoliza_i" , nmpoliza);
				parametros.put("param05_pv_nmsituac_i" , "0");
				parametros.put("param06_pv_cdrol_i"    , "1");
				parametros.put("param07_pv_cdperson_i" , cdperson);
				parametros.put("param08_pv_nmsuplem_i" , "0");
				parametros.put("param09_pv_status_i"   , "V");
				parametros.put("param10_pv_nmorddom_i" , nmorddom);
				parametros.put("param11_pv_swreclam_i" , null);
				parametros.put("param12_pv_accion_i"   , "I");
				parametros.put("param13_pv_swexiper_i" , exiper);
				logger.debug("34.- MOV_MPOLIPER");
				storedProceduresManager.procedureVoidCall(ObjetoBD.MOV_MPOLIPER.getNombre(), parametros, null);
				logger.debug("35.-VALOR DE SWEXIPER : "+ exiper);
				
				//enviar archivo
				if((!hayTramite||hayTramiteVacio||censoAtrasado||resubirCenso||duplicar)&&!sincenso&&!complemento&&StringUtils.isBlank(nombreCensoConfirmado))
				{
					paso = "Procesando censo";
					logger.debug(paso);
					
					FileInputStream input      = null;
					Workbook        workbook   = null;
					Sheet           sheet      = null;
					File            archivoTxt = null;
					PrintStream     output     = null;
					
					StringBuilder bufferErroresCenso = new StringBuilder("");
					int filasLeidas     = 0;
					int filasProcesadas = 0;
					int filasErrores    = 0;
	
					int nGrupos       = olist1.size();
					boolean[] bGrupos = new boolean[nGrupos];
					
					input    = new FileInputStream(censo);
					workbook = WorkbookFactory.create(input);
					sheet    = workbook.getSheetAt(0);
					
					archivoTxt = new File(this.rutaDocumentosTemporal+"/"+nombreCenso);
					output     = new PrintStream(archivoTxt);
					
					if(workbook.getNumberOfSheets()!=1)
					{
						throw new ApplicationException("Favor de revisar el n\u00famero de hojas del censo");
					}
					
					paso = "Procesando archivo de censo";
					logger.debug(paso);
					
					int nSituac = 0;
					
					if(esCensoSolo)
					{
						paso = "Procesando censo solo";
						logger.debug(paso);
						
						Map<Integer,String>  familias       = new LinkedHashMap<Integer,String>();
						Map<Integer,Boolean> estadoFamilias = new LinkedHashMap<Integer,Boolean>();
						Map<Integer,Integer> errorFamilia   = new LinkedHashMap<Integer,Integer>();
						Map<Integer,String>  titulares      = new LinkedHashMap<Integer,String>();
						
						//Iterate through each rows one by one
						logger.debug(""
								+ "\n##############################################"
								+ "\n###### "+archivoTxt.getAbsolutePath()+" ######"
								);
			            Iterator<Row> rowIterator = sheet.iterator();
			            int           fila        = 0;
			            int           nFamilia    = 0;
			            while (rowIterator.hasNext()) 
			            {
			            	boolean       filaBuena      = true;
			            	StringBuilder bufferLinea    = new StringBuilder();
			            	StringBuilder bufferLineaStr = new StringBuilder();
			            	
			                Row  row     = rowIterator.next();
			                Date auxDate = null;
			                Cell auxCell = null;
			                
			                if(Utils.isRowEmpty(row))
			                {
			                	break;
			                }
			                
			                fila        = fila + 1;
			                nSituac     = nSituac + 1;
			                filasLeidas = filasLeidas + 1;
			                
			                String nombre = "";
			                
		                    try
			                {
				                auxCell=row.getCell(0);
				                logger.debug("NOMBRE: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
				                bufferLinea.append(Utils.join(auxCell!=null?auxCell.getStringCellValue().trim()+"|":"|"));
				                nombre = Utils.join(nombre,auxCell!=null?auxCell.getStringCellValue():""," ");
			                }
			                catch(Exception ex)
			                {
			                	filaBuena          = false;
			                	bufferErroresCenso.append(Utils.join("Error en el campo 'Nombre' (A) de la fila ",fila," "));
			                }
		                    finally
		                    {
		                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(0)),"-"));
		                    }
		                    
			                try
			                {
				                auxCell=row.getCell(1);
				                logger.debug("APELLIDO: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
				                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue().trim()+"|":"|");
				                nombre = Utils.join(nombre,auxCell!=null?auxCell.getStringCellValue():""," ");
			                }
			                catch(Exception ex)
			                {
			                	filaBuena          = false;
			                	bufferErroresCenso.append(Utils.join("Error en el campo 'Apellido paterno' (B) de la fila ",fila," "));
			                }
		                    finally
		                    {
		                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(1)),"-"));
		                    }
			                
		                	try
		                	{
				                auxCell=row.getCell(2);
				                logger.debug("APELLIDO 2: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
				                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue().trim()+"|":"|");
				                nombre = Utils.join(nombre,auxCell!=null?auxCell.getStringCellValue():"");
			                }
			                catch(Exception ex)
			                {
			                	filaBuena          = false;
			                	bufferErroresCenso.append(Utils.join("Error en el campo 'Apellido materno' (C) de la fila ",fila," "));
			                }
		                    finally
		                    {
		                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(2)),"-"));
		                    }
		                	
		                	try
		                	{
				                auxCell=row.getCell(3);
				                logger.debug("EDAD: "+(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|"));
				                bufferLinea.append(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|");
				                
				                if(row.getCell(4)!=null) {
					                auxDate=row.getCell(4).getDateCellValue();
					                if(auxDate!=null)
					                {
					                	Calendar cal = Calendar.getInstance();
					                	Calendar anio = Calendar.getInstance();
					                	cal.setTime(auxDate);
					                	if(anio.before(cal)
					                			||cal.get(Calendar.YEAR)<1900
					                			)
					                	{
					                		throw new ApplicationException("El anio de la fecha no es valido");
					                	}
					                }
					                logger.debug("FENACIMI: "+(auxDate!=null?renderFechas.format(auxDate)+"|":"|"));
					                bufferLinea.append(auxDate!=null?renderFechas.format(auxDate)+"|":"|");
				                } else {
				                	logger.debug("FENACIMI: "+"|");
				                	bufferLinea.append("|");
				                }
				                
				                if(
				                		(
				                				row.getCell(3) == null
				                				|| row.getCell(3).getNumericCellValue() == 0d
				                		)
				                		&& (
				                				row.getCell(4) == null
				                				|| row.getCell(4).getDateCellValue() == null
				                			)
				                )
				                {
				                	logger.error(Utils.join("No hay edad ni fecha de nacimiento para la fila ",fila));
				                	throw new ApplicationException(Utils.join("No hay edad ni fecha de nacimiento para la fila ",fila));
				                }
			                }
			                catch(Exception ex)
			                {
			                	filaBuena          = false;
			                	bufferErroresCenso.append(Utils.join("Error en el campo 'Edad' o 'Fecha de nacimiento' (D, E) de la fila ",fila," "));
			                }
		                    finally
		                    {
		                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(3)),"-"));
		                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(4)),"-"));
		                    }
		                	
		                	try
		                	{
		                		String sexo = row.getCell(5).getStringCellValue().trim();
		                		if(StringUtils.isBlank(sexo)
		                				||(!sexo.equals("H")&&!sexo.equals("M")))
		                		{
		                			throw new ApplicationException("El sexo no se reconoce [H,M]");
		                		}
				                logger.debug("SEXO: "+sexo+"|");
				                bufferLinea.append(sexo+"|");
			                }
			                catch(Exception ex)
			                {
			                	filaBuena          = false;
			                	bufferErroresCenso.append(Utils.join("Error en el campo 'Sexo' (F) de la fila ",fila," "));
			                }
		                    finally
		                    {
		                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(5)),"-"));
		                    }
			                
			                try
		                	{
		                		String parentesco = row.getCell(6).getStringCellValue().trim();
		                		if(StringUtils.isBlank(parentesco)
		                				||(!parentesco.equals("T")
		                						&&!parentesco.equals("H")
		                						&&!parentesco.equals("P")
		                						&&!parentesco.equals("C")
		                						&&!parentesco.equals("D")
		                						)
		                						)
		                		{
		                			throw new ApplicationException("El parentesco no se reconoce [T,C,P,H,D]");
		                		}
				                logger.debug("PARENTESCO: "+parentesco+"|");
				                bufferLinea.append(parentesco+"|");
				                
				                if("T".equals(parentesco))
				                {
				                	nFamilia++;
				                	familias.put(nFamilia,"");
				                	estadoFamilias.put(nFamilia,true);
				                	titulares.put(nFamilia,nombre);
				                }
				                else if(fila==1)
				                {
				                	throw new ApplicationException("El primer registro debe ser titular");
				                }
			                }
			                catch(Exception ex)
			                {
			                	filaBuena          = false;
			                	if(fila==1)
			                	{
			                		bufferErroresCenso.append(Utils.join("Error en el campo 'Parentesco' (G) de la fila ",fila," la primer fila debe ser titular, se excluir\u00e1n las filas hasta el siguiente titular "));
			                	}
			                	else
			                	{
			                		bufferErroresCenso.append(Utils.join("Error en el campo 'Parentesco' (G) de la fila ",fila," "));
			                	}
			                }
		                    finally
		                    {
		                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(6)),"-"));
		                    }
			                
		                	try
		                	{
				                auxCell=row.getCell(7);
				                logger.debug("OCUPACION: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
				                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue().trim()+"|":"|");
			                }
			                catch(Exception ex)
			                {
			                	filaBuena          = false;
			                	bufferErroresCenso.append(Utils.join("Error en el campo 'Ocupacion' (H) de la fila ",fila," "));
			                }
		                    finally
		                    {
		                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(7)),"-"));
		                    }
			                
		                	try
		                	{
				                auxCell=row.getCell(8);
				                logger.debug("EXTRAPRIMA OCUPACION: "+(auxCell!=null?String.format("%.2f",auxCell.getNumericCellValue())+"|":"|"));
				                bufferLinea.append(auxCell!=null?String.format("%.2f",auxCell.getNumericCellValue())+"|":"|");
			                }
			                catch(Exception ex)
			                {
			                	filaBuena          = false;
			                	bufferErroresCenso.append(Utils.join("Error en el campo 'Extraprima de ocupacion' (I) de la fila ",fila," "));
			                }
		                    finally
		                    {
		                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(8)),"-"));
		                    }
			                
		                	try
		                	{
				                auxCell=row.getCell(9);
				                logger.debug("PESO: "+(auxCell!=null?String.format("%.2f",auxCell.getNumericCellValue())+"|":"|"));
				                bufferLinea.append(auxCell!=null?String.format("%.2f",auxCell.getNumericCellValue())+"|":"|");
			                }
			                catch(Exception ex)
			                {
			                	filaBuena          = false;
			                	bufferErroresCenso.append(Utils.join("Error en el campo 'Peso' (J) de la fila ",fila," "));
			                }
		                    finally
		                    {
		                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(9)),"-"));
		                    }
			                
			                try
		                	{
				                auxCell=row.getCell(10);
				                logger.debug("ESTATURA: "+(auxCell!=null?String.format("%.2f",auxCell.getNumericCellValue())+"|":"|"));
				                bufferLinea.append(auxCell!=null?String.format("%.2f",auxCell.getNumericCellValue())+"|":"|");
			                }
			                catch(Exception ex)
			                {
			                	filaBuena          = false;
			                	bufferErroresCenso.append(Utils.join("Error en el campo 'Estatura' (K) de la fila ",fila," "));
			                }
		                    finally
		                    {
		                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(10)),"-"));
		                    }
			                
			                try
		                	{
				                auxCell=row.getCell(11);
				                logger.debug("EXTRAPRIMA SOBREPESO: "+(auxCell!=null?String.format("%.2f",auxCell.getNumericCellValue())+"|":"|"));
				                bufferLinea.append(auxCell!=null?String.format("%.2f",auxCell.getNumericCellValue())+"|":"|");
			                }
			                catch(Exception ex)
			                {
			                	filaBuena          = false;
			                	bufferErroresCenso.append(Utils.join("Error en el campo 'Extraprima de sobrepeso' (L) de la fila ",fila," "));
			                }
		                    finally
		                    {
		                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(11)),"-"));
		                    }
			                
			                try
		                	{
				                logger.debug("GRUPO: "+String.format("%.0f",row.getCell(12).getNumericCellValue())+"|");
				                bufferLinea.append(String.format("%.0f",row.getCell(12).getNumericCellValue())+"|");
				                
				                double cdgrupo=row.getCell(12).getNumericCellValue();
				                if(cdgrupo>nGrupos||cdgrupo<1d)
				                {
				                	filaBuena = false;
				                	bufferErroresCenso.append(Utils.join("No existe el grupo (M) de la fila ",fila," "));
				                }
				                else
				                {
				                	bGrupos[new Double(cdgrupo).intValue()-1]=true;
				                }
			                }
			                catch(Exception ex)
			                {
			                	filaBuena          = false;
			                	bufferErroresCenso.append(Utils.join("Error en el campo 'Grupo' (M) de la fila ",fila," "));
			                }
		                    finally
		                    {
		                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(12)),"-"));
		                    }
			                
			                bufferLinea.append("\n");
			                logger.debug("** NUEVA_FILA **");
			                
			                if(filaBuena)
			                {
			                	familias.put(nFamilia,Utils.join(familias.get(nFamilia),bufferLinea.toString()));
			                	filasProcesadas = filasProcesadas + 1;
			                }
			                else
			                {
			                	filasErrores = filasErrores + 1;
			                	bufferErroresCenso.append(Utils.join(": ",bufferLineaStr.toString(),"\n"));
			                	estadoFamilias.put(nFamilia,false);
			                	
			                	if(!errorFamilia.containsKey(nFamilia))
			                	{
			                		errorFamilia.put(nFamilia,fila);
			                	}
			                }
			            }
			            
			            logger.debug("\nFamilias: {}\nEstado familias: {}\nErrorFamilia: {}\nTitulares: {}"
			            		,familias,estadoFamilias,errorFamilia,titulares);
			            
			            for(Entry<Integer,Boolean>en:estadoFamilias.entrySet())
			            {
			            	int     n = en.getKey();
			            	boolean v = en.getValue();
			            	if(v)
			            	{
			            		output.print(familias.get(n));
			            	}
			            	else
			            	{
			            		bufferErroresCenso.append(Utils.join("La familia ",n," del titular '",titulares.get(n),"' no fue incluida por error en la fila ",errorFamilia.get(n),"\n"));
			            	}
			            }
			            
			            //if(!clasif.equals(LINEA)) // Si el censo no es de linea (menor a 50 asegurados), se valida el num. de titulares 
			            //{
			            ManagerRespuestaSmapVO familiasMinimas = cotizacionManager.obtenerParametrosCotizacion(
			            		ParametroCotizacion.NUMERO_FAMILIAS_COTI_COLECTIVO
			            		,cdramo
			            		,cdtipsit
			            		,null
			            		,null
			            		);
			            int nMin = 0;
			            try
			            {
			            	nMin = Integer.parseInt(familiasMinimas.getSmap().get("P1VALOR"));
			            }
			            catch(Exception ex)
			            {
			            	throw new ApplicationException("Error al validar el n\u00FAmero de titulares");
			            }
			            
			            if(nFamilia<nMin)
			            {
			            	throw new ApplicationException(Utils.join("El n\u00FAmero de titulares debe ser por lo menos "
			            			,nMin
			            			,", se encontraron "
			            			,nFamilia));
			            }
			            //}
			            
			            input.close();
			            output.close();
			            logger.debug(""
			            		+ "\n###### "+archivoTxt.getAbsolutePath()+" ######"
								+ "\n##############################################"
								);
					}
					else //censo agrupado
					{
						paso = "Procesando censo agrupado";
						logger.debug(paso);
						
						//Iterate through each rows one by one
						logger.debug(""
								+ "\n##############################################"
								+ "\n###### "+archivoTxt.getAbsolutePath()+" ######"
								);
			            Iterator<Row> rowIterator = sheet.iterator();
			            int fila = 0;
			            while (rowIterator.hasNext()) 
			            {
			                Row row = rowIterator.next();
			                
			                if(Utils.isRowEmpty(row))
			                {
			                	break;
			                }
			                
			                boolean       filaBuena      = true;
			                StringBuilder bufferLinea    = new StringBuilder("");
			                StringBuilder bufferLineaStr = new StringBuilder("");
			                
			                fila        = fila + 1;
			                filasLeidas = filasLeidas + 1;
			                
		                	try
		                	{
				                logger.debug("EDAD: "+String.format("%.0f",row.getCell(0).getNumericCellValue())+"|");
				                bufferLinea.append(String.format("%.0f",row.getCell(0).getNumericCellValue())+"|");
			                }
			                catch(Exception ex)
			                {
			                	filaBuena = false;
			                	bufferErroresCenso.append(Utils.join("Error en el campo 'Edad' (A) de la fila ",fila," "));
			                }
		                    finally
		                    {
		                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(0)),"-"));
		                    }
			                
			                try
		                	{
			                	String sexo = row.getCell(1).getStringCellValue();
			                	if(!"H".equals(sexo)
			                			&&!"M".equals(sexo))
			                	{
			                		throw new ApplicationException("Genero (sexo) incorrecto");
			                	}
				                logger.debug("SEXO: "+sexo+"|");
				                bufferLinea.append(sexo+"|");
			                }
			                catch(Exception ex)
			                {
			                	filaBuena = false;
			                	bufferErroresCenso.append(Utils.join("Error en el campo 'Sexo' (B) de la fila ",fila," "));
			                }
		                    finally
		                    {
		                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(1)),"-"));
		                    }
			                
			                try
		                	{
				                logger.debug("CUANTOS: "+String.format("%.0f",row.getCell(2).getNumericCellValue())+"|");
				                bufferLinea.append(String.format("%.0f",row.getCell(2).getNumericCellValue())+"|");
				                
				                nSituac = nSituac + (int)row.getCell(2).getNumericCellValue();
			                }
			                catch(Exception ex)
			                {
			                	filaBuena = false;
			                	bufferErroresCenso.append(Utils.join("Error en el campo 'Cantidad' (C) de la fila ",fila," "));
			                }
		                    finally
		                    {
		                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(2)),"-"));
		                    }
			                
			                try
		                	{
				                logger.debug("GRUPO: "+String.format("%.0f",row.getCell(3).getNumericCellValue())+"|");
				                bufferLinea.append(String.format("%.0f",row.getCell(3).getNumericCellValue())+"|");
				                
				                double cdgrupo=row.getCell(3).getNumericCellValue();
				                if(cdgrupo>nGrupos||cdgrupo<1d)
				                {
				                	filaBuena = false;
				                	bufferErroresCenso.append(Utils.join("No existe el grupo (D) de la fila ",fila," "));
				                }
				                else
				                {
				                	bGrupos[new Double(cdgrupo).intValue()-1]=true;
				                }
			                }
			                catch(Exception ex)
			                {
			                	filaBuena = false;
			                	bufferErroresCenso.append(Utils.join("Error en el campo 'Grupo' (D) de la fila ",fila," "));
			                }
		                    finally
		                    {
		                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(3)),"-"));
		                    }
			                
			                bufferLinea.append("\n");
			                logger.debug("** NUEVA_FILA **");
			                
			                if(filaBuena)
			                {
			                	output.print(bufferLinea.toString());
			                	filasProcesadas = filasProcesadas + 1;
			                }
			                else
			                {
			                	filasErrores = filasErrores + 1;
			                	bufferErroresCenso.append(Utils.join(": ",bufferLineaStr.toString(),"\n"));
			                }
			            }
			            input.close();
			            output.close();
			            logger.debug(""
			            		+ "\n###### "+archivoTxt.getAbsolutePath()+" ######"
								+ "\n##############################################"
								);
					}
					
					smap1.put("erroresCenso"    , bufferErroresCenso.toString());
					smap1.put("filasLeidas"     , Integer.toString(filasLeidas));
					smap1.put("filasProcesadas" , Integer.toString(filasProcesadas));
					smap1.put("filasErrores"    , Integer.toString(filasErrores));
					
					if(clasif.equals(LINEA)&&nSituac>49)
					{
						//se condiciona por rol lanzar la excepcion, para que guarde las coberturas sin necesidad de editar subgrupo, para roles restringidos (EGS)
						if(!(RolSistema.AGENTE.getCdsisrol().equals(cdsisrol) || RolSistema.EJECUTIVO_INTERNO.getCdsisrol().equals(cdsisrol) || RolSistema.MESA_DE_CONTROL.getCdsisrol().equals(cdsisrol))
								&&TipoSituacion.RECUPERA_COLECTIVO.getCdtipsit().equals(cdtipsit)){
							throw new ApplicationException("No se permiten m\u00e1s de 49 asegurados");
						}
					}
					else if(!clasif.equals(LINEA)&&nSituac<50)
					{
						throw new ApplicationException("No se permiten menos de 50 asegurados");
					}
					
					int cdgrupoVacio=0;
					for(int i=0;i<nGrupos;i++)
					{
						if(!bGrupos[i])
						{
							cdgrupoVacio=i+1;
							break;
						}
					}
					if(cdgrupoVacio>0)
					{
						throw new ApplicationException(Utils.join("No hay asegurados para el grupo ",cdgrupoVacio));
					}
					
					boolean transferidoAmbosServer = FTPSUtils.upload(					
								this.dominioServerLayouts,
								this.userServerLayouts,
								this.passServerLayouts,
								archivoTxt.getAbsolutePath(),
								this.directorioServerLayouts+"/"+nombreCenso
							)
							&&FTPSUtils.upload
							(
								this.dominioServerLayouts2,
								this.userServerLayouts,
								this.passServerLayouts,
								archivoTxt.getAbsolutePath(),
								this.directorioServerLayouts+"/"+nombreCenso
							);
						
					if(!transferidoAmbosServer)
					{
						throw new ApplicationException("Error al transferir archivo al servidor");
					}
				}
				
				//pl censo
				if(!hayTramite||hayTramiteVacio||censoAtrasado||resubirCenso||duplicar)
				{
					paso = "Ejecutando procedimiento de censo";
					logger.debug(paso);
					
					String cdplanes[]    = new String[5];
					
					int nGru=0;
					for(Map<String,Object>iGrupo:olist1)
					{
						String  cdgrupo      = (String)iGrupo.get("letra");
						String  cdplan       = (String)iGrupo.get("cdplan");
						Integer indGrupo     = Integer.valueOf(cdgrupo);
						cdplanes[indGrupo-1] = cdplan;
						nGru=nGru+1;
					}
					
					if(esCensoSolo||sincenso)
					{
						LinkedHashMap<String,Object> paramsProcCenso = new LinkedHashMap<String,Object>();
						paramsProcCenso.put("param01",sincenso?"layout_censo"+nGru+".txt":nombreCenso);
						paramsProcCenso.put("param02",cdunieco);
						paramsProcCenso.put("param03",cdramo);
						paramsProcCenso.put("param04","W");
						paramsProcCenso.put("param05",nmpoliza);
						paramsProcCenso.put("param06",cdedo);
						paramsProcCenso.put("param07",cdmunici);
						paramsProcCenso.put("param08",cdplanes[0]);
						paramsProcCenso.put("param09",cdplanes[1]);
						paramsProcCenso.put("param10",cdplanes[2]);
						paramsProcCenso.put("param11",cdplanes[3]);
						paramsProcCenso.put("param12",cdplanes[4]);
						paramsProcCenso.put("param13","N");
						storedProceduresManager.procedureVoidCall(
								ObjetoBD.CARGAR_CENSO.getNombre(), paramsProcCenso, null);
					}
					else
					{
						LinkedHashMap<String,Object>paramsProcCenso=new LinkedHashMap<String,Object>();
						paramsProcCenso.put("param01",nombreCenso);
						paramsProcCenso.put("param02",cdunieco);
						paramsProcCenso.put("param03",cdramo);
						paramsProcCenso.put("param04","W");
						paramsProcCenso.put("param05",nmpoliza);
						paramsProcCenso.put("param06",cdedo);
						paramsProcCenso.put("param07",cdmunici);
						paramsProcCenso.put("param08",cdplanes[0]);
						paramsProcCenso.put("param09",cdplanes[1]);
						paramsProcCenso.put("param10",cdplanes[2]);
						paramsProcCenso.put("param11",cdplanes[3]);
						paramsProcCenso.put("param12",cdplanes[4]);
						storedProceduresManager.procedureVoidCall(
								ObjetoBD.CARGAR_CENSO_AGRUPADO.getNombre(), paramsProcCenso, null);
					}
				}
				
				if((!hayTramite||hayTramiteVacio||censoAtrasado||resubirCenso||duplicar)
						&&StringUtils.isBlank(nombreCensoConfirmado)
				)
				{
					smap1.put("nombreCensoParaConfirmar" , nombreCenso);
					
					exito = true;
					
					respuesta = Utils.join("Se ha revisado el censo [REV. ",System.currentTimeMillis(),"]");
					
					logger.debug(respuesta);
					
					return SUCCESS;
				}
				logger.debug(Utils.log("########################user 2 ",user));
				tvalositSigsvdefTvalogarContratanteTramiteSigsvalipolObject aux = this.tvalositSigsvdefTvalogarContratanteTramiteSigsvalipol(
							clasif    , LINEA         , LINEA_EXTENDIDA
							,cdunieco , cdramo        , nmpoliza
							,cdtipsit , hayTramite    , hayTramiteVacio
							,user     , cdelemento    , ntramiteVacio
							,false    , ntramite      , cdagente
							,sincenso , censoAtrasado , resubirCenso
							,cdperpag , cdsisrol      , complemento
							,asincrono,cdmunici,cdedo,codpostal,false, //censoCompleto
							duplicar
							);
				if(!aux.exito)
				{
					throw new ApplicationException(aux.respuesta);
				}
				
				if(StringUtils.isBlank(respuesta))
				{
					respuesta = Utils.join("Se gener\u00f3 el tr\u00e1mite ", smap1.get("ntramite"),
					        StringUtils.isBlank(respuesta)
    					        ? ""
    					        : Utils.join(". ", respuesta));
				}
			}
			catch(Exception ex)
			{
				Utils.generaExcepcion(ex, paso);
			}
			
			success = true;
			exito   = true;
			
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### exito=" , exito
				,"\n###### generarTramiteGrupo ######"
				,"\n#################################"
				));
		return SUCCESS;
	}
	
	private tvalositSigsvdefTvalogarContratanteTramiteSigsvalipolObject tvalositSigsvdefTvalogarContratanteTramiteSigsvalipol(
			String clasif
			,String LINEA
			,String LINEA_EXTENDIDA
			,String cdunieco
			,String cdramo
			,String nmpoliza
			,String cdtipsit
			,boolean hayTramite
			,boolean hayTramiteVacio
			,String cdusuari
			,String cdelemento
			,String ntramiteVacio
			,boolean reinsertaContratante
			,String ntramite
			,String cdagente
			,boolean sincenso
			,boolean censoAtrasado
			,boolean resubirCenso
			,String cdperpag
			,String cdsisrol
			,boolean complemento
			,boolean asincrono
			,String cdmunici
			,String cdedo
			,String codpostal
			,boolean censoCompleto
			,boolean duplicar
			)
	{
		logger.debug(Utils.log(
				 "\n###########################################################"
				,"\n## tvalositSigsvdefTvalogarContratanteTramiteSigsvalipol ##"
				,"\n## clasif="               , clasif
				,"\n## LINEA="                , LINEA
				,"\n## LINEA_EXTENDIDA="      , LINEA_EXTENDIDA
				,"\n## cdunieco="             , cdunieco
				,"\n## cdramo="               , cdramo
				,"\n## nmpoliza="             , nmpoliza
				,"\n## cdtipsit="             , cdtipsit
				,"\n## hayTramite="           , hayTramite
				,"\n## hayTramiteVacio="      , hayTramiteVacio
				,"\n## cdusuari="             , cdusuari
				,"\n## cdelemento="           , cdelemento
				,"\n## ntramiteVacio="        , ntramiteVacio
				,"\n## reinsertaContratante=" ,reinsertaContratante
				,"\n## ntramite="             , ntramite
				,"\n## cdagente="             , cdagente
				,"\n## sincenso="             , sincenso
				,"\n## censoAtrasado="        , censoAtrasado
				,"\n## resubirCenso="         , resubirCenso
				,"\n## cdperpag="             , cdperpag
				,"\n## cdsisrol="             , cdsisrol
				,"\n## complemento="          , complemento
				,"\n## asincrono="            , asincrono
				,"\n## censoCompleto="        , censoCompleto
				,"\n## duplicar="             , duplicar
				));
		tvalositSigsvdefTvalogarContratanteTramiteSigsvalipolObject resp =
				new tvalositSigsvdefTvalogarContratanteTramiteSigsvalipolObject();
		resp.exito = true;
		
		// Actualizar plan de mpolisit
		if (resp.exito && (
			RolSistema.SUSCRIPTOR_TECNICO.getCdsisrol().equals(cdsisrol) ||
			RolSistema.SUPERVISOR_TECNICO_SALUD.getCdsisrol().equals(cdsisrol) ||
			RolSistema.SUBDIRECTOR_SALUD.getCdsisrol().equals(cdsisrol) ||
			RolSistema.DIRECTOR_SALUD.getCdsisrol().equals(cdsisrol)
		)) {
			try {
				for (Map<String,Object> iGrupo : olist1) {
					String cdgrupo = (String)iGrupo.get("letra"),
					       cdplan  = (String)iGrupo.get("cdplan");
					cotizacionManager.actualizarCdplanGrupo(cdunieco, cdramo, "W", nmpoliza, "0", cdgrupo, cdplan);
				}
			} catch (Exception ex) {
				long timestamp = System.currentTimeMillis();
				resp.exito = false;
				resp.respuesta = Utils.join("Error al actualizar plan de grupos #", timestamp);
				logger.error(resp.respuesta, ex);
			}
		}
		
		//tvalosit
		logger.debug("1.- Valor de resp.exito ===>"+resp.exito);
		if(resp.exito)
		{
			try
			{
				logger.debug("2.- Valor de clasif: {} LINEA :{} LINEA_EXTENDIDA: {}",clasif,LINEA,LINEA_EXTENDIDA );
				if(clasif.equals(LINEA)&&LINEA_EXTENDIDA.equals("S"))
				{
					for(Map<String,Object>iGrupo:olist1)
					{
						String cdgrupo = (String)iGrupo.get("letra");
						
						//SUMA ASEGURADA Y MATERNIDAD
						String ptsumaaseg = (String)iGrupo.get("ptsumaaseg");
						String ayudamater = (String)iGrupo.get("ayudamater");
						Object incrinflL  = iGrupo.get("incrinfl");
						String incrinfl   = incrinflL!=null? incrinflL.toString() : "";
						Object extrrenoL  = iGrupo.get("extrreno");
						String extrreno   = extrrenoL!=null? extrrenoL.toString() : "";
						Object cesicomiL  = iGrupo.get("cesicomi");
						String cesicomi   = cesicomiL!=null? cesicomiL.toString() : "";
						Object pondubicL  = iGrupo.get("pondubic");
						String pondubic   = pondubicL!=null? pondubicL.toString() : "";
						Object descbonoL  = iGrupo.get("descbono");
						String descbono   = descbonoL!=null? descbonoL.toString() : "";
						
						//nuevos factores de Lolita
						Object gastadmiL = iGrupo.get("gastadmi");
						String gastadmi  = gastadmiL!=null? gastadmiL.toString() : "";
						
						Object utilidadL = iGrupo.get("utilidad");
						String utilidad  = utilidadL!=null? utilidadL.toString() : "";
						
						Object comiagenL = iGrupo.get("comiagen");
						String comiagen  = comiagenL!=null? comiagenL.toString() : "";
						
						Object comipromL = iGrupo.get("comiprom");
						String comiprom  = comipromL!=null? comipromL.toString() : "";
						
						Object bonoinceL = iGrupo.get("bonoince");
						String bonoince  = bonoinceL!=null? bonoinceL.toString() : "";
						
						Object otrogastL = iGrupo.get("otrogast");
						String otrogast  = otrogastL!=null? otrogastL.toString() : "";
						
						//nuevos factores de Lolita fin
						
						logger.debug("3.- Entra  la cotizacionManager.movimientoMpolisitTvalositGrupo ");
						cotizacionManager.movimientoMpolisitTvalositGrupo(
								cdunieco, cdramo, "W", nmpoliza,
								cdgrupo, ptsumaaseg, incrinfl, extrreno,
								cesicomi, pondubic, descbono, gastadmi,
								utilidad, comiagen, comiprom, bonoince, otrogast,
								(String)iGrupo.get("nombre"),ayudamater);
						
						logger.debug("4.- Entra a actualizaValoresDefectoSituacion");
						cotizacionManager.actualizaValoresDefectoSituacion(cdunieco,cdramo,"W",nmpoliza,"0");
					}
				}
				else
				{
					logger.debug("5.- Entra al else");
					for(Map<String,Object>iGrupo:olist1)
					{
						String cdgrupo = (String)iGrupo.get("letra");
						
						//SUMA ASEGURADA y ayuda maternidad
						String ptsumaaseg = (String)iGrupo.get("ptsumaaseg");
						String ayudamater = "0";
						Object incrinflL  = iGrupo.get("incrinfl");
						String incrinfl   = incrinflL!=null? incrinflL.toString() : "0";
						Object extrrenoL  = iGrupo.get("extrreno");
						String extrreno   = extrrenoL!=null? extrrenoL.toString() : "0";
						Object cesicomiL  = iGrupo.get("cesicomi");
						String cesicomi   = cesicomiL!=null? cesicomiL.toString() : "0";
						Object pondubicL  = iGrupo.get("pondubic");
						String pondubic   = pondubicL!=null? pondubicL.toString() : "0";
						Object descbonoL  = iGrupo.get("descbono");
						String descbono   = descbonoL!=null? descbonoL.toString() : "0";
						
						//nuevos factores de Lolita
						Object gastadmiL = iGrupo.get("gastadmi");
						String gastadmi  = gastadmiL!=null? gastadmiL.toString() : "0";
						
						Object utilidadL = iGrupo.get("utilidad");
						String utilidad  = utilidadL!=null? utilidadL.toString() : "0";
						
						Object comiagenL = iGrupo.get("comiagen");
						String comiagen  = comiagenL!=null? comiagenL.toString() : "0";
						
						Object comipromL = iGrupo.get("comiprom");
						String comiprom  = comipromL!=null? comipromL.toString() : "0";
						
						Object bonoinceL = iGrupo.get("bonoince");
						String bonoince  = bonoinceL!=null? bonoinceL.toString() : "0";
						
						Object otrogastL = iGrupo.get("otrogast");
						String otrogast  = otrogastL!=null? otrogastL.toString() : "0";
						//nuevos factores de Lolita fin
						
						List<Map<String,String>>tvalogars=(List<Map<String,String>>)iGrupo.get("tvalogars");
						for(Map<String,String>iTvalogar:tvalogars)
						{
							String cdgarant  = iTvalogar.get("cdgarant");
							boolean amparada = "S".equals(iTvalogar.get("amparada"));
							if("4AYM".equalsIgnoreCase(cdgarant) && amparada )
							{
								ayudamater = iTvalogar.get("parametros.pv_otvalor001");
							}
						}
						logger.debug("5.- Entra a cotizacionManager.movimientoMpolisitTvalositGrupo");
						cotizacionManager.movimientoMpolisitTvalositGrupo(
								cdunieco, cdramo, "W", nmpoliza,
								cdgrupo, ptsumaaseg, incrinfl, extrreno,
								cesicomi, pondubic, descbono, gastadmi,
								utilidad, comiagen, comiprom, bonoince, otrogast,
								(String)iGrupo.get("nombre"),ayudamater);
					}
				}
			}
			catch(Exception ex)
			{
				long timestamp       = System.currentTimeMillis();
				resp.exito           = false;
				resp.respuesta       = "Error al guardar grupos #"+timestamp;
				resp.respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		logger.debug("6.- Valor de Exito:{} codpostal : {}  StringUtils.isNotBlank(codpostal) :{}",exito,codpostal,StringUtils.isNotBlank(codpostal));
		if(exito && StringUtils.isNotBlank(codpostal)){
			try {
				logger.debug("7.- Entra a cotizacionManager.actualizaDomicilioAseguradosColectivo");
				cotizacionManager.actualizaDomicilioAseguradosColectivo(cdunieco, cdramo, "W", nmpoliza, "0", codpostal, cdedo, cdmunici);
			} catch (Exception ex) {
				long timestamp       = System.currentTimeMillis();
				resp.exito           = false;
				resp.respuesta       = "Error al guardar domiciio tvalopol #"+timestamp;
				resp.respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		logger.debug("8.- Valor de resp.exito : {} hayTramite:{} hayTramiteVacio:{}",resp.exito,hayTramite,hayTramiteVacio);
		if(resp.exito
				&&(!hayTramite||hayTramiteVacio||duplicar)
				&&
				(
					RolSistema.AGENTE.getCdsisrol().equals(cdsisrol)
					||RolSistema.EJECUTIVO_INTERNO.getCdsisrol().equals(cdsisrol)
					||RolSistema.MESA_DE_CONTROL.getCdsisrol().equals(cdsisrol)
					||RolSistema.SUSCRIPTOR.getCdsisrol().equals(cdsisrol)
				)
		)
		{
			asincrono = true;
			logger.debug("9.- asincrono : {}",asincrono);
		}
		
		//sigsvdef
		logger.debug("10.- resp.exito : {} hayTramite: {} hayTramiteVacio: {} censoAtrasado: {} resubirCenso: {} complemento:{} asincrono:{} ",resp.exito, 
				hayTramite,hayTramiteVacio,censoAtrasado,resubirCenso,complemento,asincrono);
		
		if(resp.exito&&(!hayTramite||hayTramiteVacio||censoAtrasado||resubirCenso||complemento||censoCompleto||duplicar)&&asincrono==false)
		{
			try
			{
				/*
				Map<String,String> mapCoberturas=new HashMap<String,String>(0);
	            mapCoberturas.put("pv_cdunieco_i" , cdunieco);
	            mapCoberturas.put("pv_cdramo_i"   , cdramo);
	            mapCoberturas.put("pv_estado_i"   , "W");
	            mapCoberturas.put("pv_nmpoliza_i" , nmpoliza);
	            mapCoberturas.put("pv_nmsituac_i" , "0");
	            mapCoberturas.put("pv_nmsuplem_i" , "0");
	            mapCoberturas.put("pv_cdgarant_i" , "TODO");
	            mapCoberturas.put("pv_cdtipsup_i" , "1");
	            kernelManager.coberturas(mapCoberturas);
	            */
				logger.debug("11.- Entra a cotizacionManager.ejecutaValoresDefectoConcurrente");
	            cotizacionManager.ejecutaValoresDefectoConcurrente(
	            		cdunieco
	            		,cdramo
	            		,"W"
	            		,nmpoliza
	            		,"0"
	            		,"0"
	            		,"1"
	            		,cdperpag
	            		);
	            
	            try
	            {
	            	logger.debug("12.- Entra a serviciosManager.grabarEvento");
	            	serviciosManager.grabarEvento(new StringBuilder("\nCotizacion grupo")
	            	    ,Constantes.MODULO_COTIZACION //cdmodulo
	            	    ,Constantes.EVENTO_COTIZAR     //cdevento
	            	    ,new Date()   //fecha
	            	    ,cdusuari
	            	    ,((UserVO)session.get("USUARIO")).getRolActivo().getClave()
	            	    ,ntramite
	            	    ,cdunieco
	            	    ,cdramo
	            	    ,"W"
	            	    ,nmpoliza
	            	    ,nmpoliza
	            	    ,cdagente
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
				long timestamp       = System.currentTimeMillis();
				resp.exito           = false;
				resp.respuesta       = "Error al insertar valores por defecto para las coberturas #"+timestamp;
				resp.respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		logger.debug("13.- Valor de resp.exito : {} asincrono:{} ",resp.exito,asincrono);
		//tvalogar
		if(resp.exito&&asincrono==false)
		{
			try
			{
				logger.debug("14.- Valor de clasif : {} LINEA:{}  LINEA_EXTENDIDA:{}",clasif,LINEA, LINEA_EXTENDIDA);
				if(clasif.equals(LINEA)&&LINEA_EXTENDIDA.equals("S"))
				{
					for(Map<String,Object>iGrupo:olist1)
					{
						String cdgrupo = (String)iGrupo.get("letra");
						
						//HOSPITALIZACION (DEDUCIBLE)
						String cdgarant = "4HOS";
						String cdatribu = "001";
						String valor    = (String)iGrupo.get("deducible");
						logger.debug("15.- cotizacionManager.movimientoTvalogarGrupo");
						cotizacionManager.movimientoTvalogarGrupo(cdunieco, cdramo, "W", nmpoliza, "0", cdtipsit, cdgrupo, cdgarant, "V", cdatribu, valor);
						
						//ASISTENCIA INTERNACIONAL VIAJES --AHORA MEDICAMENTOS
						String asisinte = (String)iGrupo.get("asisinte");
						if (StringUtils.isBlank(asisinte)) {
							asisinte = "0";
						}
						cdgarant = "4MED";
						logger.debug("16.- Asistente :{}",Integer.parseInt(asisinte));
						if(Integer.parseInt(asisinte)>0)
						{
							logger.debug("17.- cotizacionManager.movimientoMpoligarGrupo");
							cotizacionManager.movimientoMpoligarGrupo(
									cdunieco
									,cdramo
									,"W"
									,nmpoliza
									,"0"
									,cdtipsit
									,cdgrupo
									,cdgarant
									,"V"
									,"001"
									,Constantes.INSERT_MODE
									,"S"
									);
							logger.debug("18.- cotizacionManager.movimientoTvalogarGrupo");
							cotizacionManager.movimientoTvalogarGrupo(
									cdunieco
									,cdramo
									,"W" //estado
									,nmpoliza
									,"0" //nmsuplem
									,cdtipsit
									,cdgrupo
									,cdgarant
									,"V" //status
									,"001"
									,asisinte
									);
						}
						else
						{
							logger.debug("19.- cotizacionManager.movimientoMpoligarGrupo");
							cotizacionManager.movimientoMpoligarGrupo(
									cdunieco, cdramo, "W", nmpoliza, "0", cdtipsit, cdgrupo, cdgarant, "V", "001", Constantes.DELETE_MODE, null);
						}
						
						//EMERGENCIA EXTRANJERO
						String emerextr = (String)iGrupo.get("emerextr");
						cdgarant = "4EE";
						logger.debug("20.- emerextr : {}",emerextr);
						if(emerextr.equalsIgnoreCase("S"))
						{
							logger.debug("21.- cotizacionManager.movimientoMpoligarGrupo");
							cotizacionManager.movimientoMpoligarGrupo(
									cdunieco, cdramo, "W", nmpoliza, "0", cdtipsit, cdgrupo, cdgarant, "V", "001", Constantes.INSERT_MODE, null);
						}
						else
						{
							logger.debug("22.- cotizacionManager.movimientoMpoligarGrupo");
							cotizacionManager.movimientoMpoligarGrupo(
									cdunieco, cdramo, "W", nmpoliza, "0", cdtipsit, cdgrupo, cdgarant, "V", "001", Constantes.DELETE_MODE, null);
						}
					}
				}
				else
				{
					logger.debug("23.- Entra al else");
					for(Map<String,Object>iGrupo:olist1)
					{
						String cdgrupo = (String)iGrupo.get("letra");
						
						List<Map<String,String>>tvalogars=(List<Map<String,String>>)iGrupo.get("tvalogars");
						for(Map<String,String>iTvalogar:tvalogars)
						{
							String cdgarant  = iTvalogar.get("cdgarant");
							boolean amparada = StringUtils.isNotBlank(iTvalogar.get("amparada"))
									&&iTvalogar.get("amparada").equalsIgnoreCase("S");
							
							logger.debug("24.- cdgarant : {} amparada:{}",cdgarant,amparada);
								if(amparada)
								{
									logger.debug("25.- cotizacionManager.movimientoMpoligarGrupo");
									cotizacionManager.movimientoMpoligarGrupo(
											cdunieco, cdramo, "W", nmpoliza, "0", cdtipsit, cdgrupo, cdgarant, "V", "001", Constantes.INSERT_MODE, null);
									//buscar cdatribus
									boolean hayAtributos=false;
									Map<String,String>listaCdatribu=new HashMap<String,String>();
									for(Entry<String,String>iAtribTvalogar:iTvalogar.entrySet())
									{
										String key=iAtribTvalogar.getKey();
										if(key!=null
												&&key.length()>"parametros.pv_otvalor".length()
												&&key.substring(0, "parametros.pv_otvalor".length()).equalsIgnoreCase("parametros.pv_otvalor"))
										{
											hayAtributos=true;
											listaCdatribu.put(key.substring("parametros.pv_otvalor".length(), key.length()),iAtribTvalogar.getValue());
										}
									}
									if(hayAtributos)
									{
										for(Entry<String,String>atributo:listaCdatribu.entrySet())
										{
											if(StringUtils.isNotBlank(atributo.getValue()))
											{
												logger.debug("26.- cotizacionManager.movimientoTvalogarGrupo");
											    cotizacionManager.movimientoTvalogarGrupo(
													cdunieco, cdramo, "W", nmpoliza, "0", cdtipsit, cdgrupo, cdgarant, "V",
													atributo.getKey(), atributo.getValue());
											}
										}
									}
								}
								else
								{
									logger.debug("27.- cotizacionManager.movimientoMpoligarGrupo");
									cotizacionManager.movimientoMpoligarGrupo(
											cdunieco, cdramo, "W", nmpoliza, "0", cdtipsit, cdgrupo, cdgarant, "V", "001", Constantes.DELETE_MODE, null);
								}
							//}
						}
					}
				}
			}
			catch(Exception ex)
			{
				long timestamp       = System.currentTimeMillis();
				resp.exito           = false;
				resp.respuesta       = "Error al guardar las coberturas #"+timestamp; 
				resp.respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		
		// Se inserta el maestro y detalle de los grupos:
				try {
				    
					try {
					    cotizacionManager.eliminarGrupos(cdunieco, cdramo, Constantes.POLIZA_WORKING, nmpoliza, cdtipsit);
					} catch (Exception e) {
						logger.warn("No se eliminaron los grupos de la poliza: {}", e);
					}
					for(Map<String,Object> grupoIte : olist1) {
						logger.debug("grupoIte=={}", grupoIte);
						// Guardar el maestro de grupos mpoligrup:
						cotizacionManager.insertaMpoligrup(cdunieco, cdramo, Constantes.POLIZA_WORKING, nmpoliza, cdtipsit, (String)grupoIte.get("letra"), (String)grupoIte.get("nombre"), (String)grupoIte.get("cdplan"),(String)grupoIte.get("dsplanl"), null, "0", "0", Constantes.NO, Constantes.NO, Constantes.NO);
						// Guardar el detalle de grupos mgrupogar:
						cotizacionManager.insertaMgrupogar(cdunieco, cdramo, Constantes.POLIZA_WORKING, nmpoliza, cdtipsit, (String)grupoIte.get("letra"), (String)grupoIte.get("cdplan"), "0");
					}
				} catch(Exception ex) {
					long timestamp = System.currentTimeMillis();
		        	resp.exito = false;
		        	resp.respuesta = new StringBuilder("Error al guardar grupos de la poliza #").append(timestamp).toString();
		        	resp.respuestaOculta = ex.getMessage();
		        	logger.error(resp.respuesta,ex);
				}
		
		
		//contratante
		logger.debug("28.- Valor de resp.exito:{}",resp.exito);
		
		logger.debug("29,30,31,32,33,34,35,36,37.- XXX Se Quito insercion de contratante MOV_MPERSONA,MDOMICIL");
		
		Date fechaHoy = new Date();
		
		//tramite
		logger.debug("38.- resp.exito:{} hayTramite:{} hayTramiteVacio: {} censoAtrasado: {}",resp.exito,hayTramite,hayTramiteVacio,censoAtrasado);
		if(resp.exito&&(!hayTramite||hayTramiteVacio||censoAtrasado||duplicar))
		{
			try
			{
				logger.debug("39.- hayTramite: {} hayTramiteVacio:{}",hayTramite,hayTramiteVacio);
				if(!hayTramite&&!hayTramiteVacio)//es agente
				{
					Map<String,String> valores = new LinkedHashMap<String,String>();
					valores.put("otvalor01" , clasif);
					valores.put("otvalor02" , sincenso ? "S" : "N");
					
					logger.debug("40.- consultasManager.recuperarDatosFlujoEmision");
					Map<String,String> datosFlujo = consultasManager.recuperarDatosFlujoEmision(cdramo,"C");

					String estatus = EstatusTramite.EN_ESPERA_DE_COTIZACION.getCodigo();
					try {
            			estatus = flujoMesaControlManager.recuperarEstatusDefectoRol(cdsisrol);
            		} catch (Exception ex) {
            			logger.warn("Error sin impacto al querer recuperar estatus por defecto de un rol", ex);
            		}
					
					//logger.debug("");
					logger.debug("41.- mesaControlManager.movimientoTramite");
					String ntramiteNew = mesaControlManager.movimientoTramite(
							cdunieco
							,cdramo
							,"W"
							,"0"
							,"0"
							,cdunieco
							,cdunieco
							,TipoTramite.POLIZA_NUEVA.getCdtiptra()
							,fechaHoy
							,cdagente
							,null
							,null
							,fechaHoy
							,estatus
							,null
							,nmpoliza
							,cdtipsit
							,cdusuari
							,cdsisrol
							,null //swimpres
	            			,datosFlujo.get("cdtipflu")
	            			,datosFlujo.get("cdflujomc")
							,valores
							,TipoEndoso.EMISION_POLIZA.getCdTipSup().toString(), null, null, null
							);
					smap1.put("ntramite",ntramiteNew);
					
					logger.debug("42.- mesaControlManager.movimientoDetalleTramite");
					
					RespuestaTurnadoVO despacho = despachadorManager.turnarTramite(
					        cdusuari,
					        cdsisrol,
					        ntramiteNew,
					        estatus,
					        "Se guard\u00f3 un nuevo tr\u00e1mite en mesa de control desde cotizaci\u00f3n de agente",
					        null,  // cdrazrecha
					        null,  // cdusuariDes
					        null,  // cdsisrolDes
					        true,  // permisoAgente
					        false, // porEscalamiento
					        fechaHoy,
					        false  //sinGrabarDetalle
					        );
					resp.respuesta = despacho.getMessage();
					
					/* JTEZVA 7 sep 2016
					 * el tramite no se turna por lo que no lleva doble detalle
					mesaControlManager.movimientoDetalleTramite(
							ntramiteNew
							,new Date()
							,null
							,"Se guard\u00f3 un nuevo tr\u00e1mite en mesa de control desde cotizaci\u00f3n de agente"
							,cdusuari
							,null
							,cdsisrol
							,"S"
							,estatus
							,false
							);
					*/
	            	
	            	/* ya no turna, solo lo crea y ya JTEZVA 2016 09 02
	            	 * smap1.put("nombreUsuarioDestino"
	            			,cotizacionManager.turnaPorCargaTrabajo(ntramiteNew,"COTIZADOR",EstatusTramite.EN_ESPERA_DE_COTIZACION.getCodigo())
	            			);*/
	            	
	            	try
		            {
	            		logger.debug("43.- serviciosManager.grabarEvento");
		            	serviciosManager.grabarEvento(new StringBuilder("\nNuevo tramite grupo")
		            	    ,Constantes.MODULO_EMISION               //cdmodulo
		            	    ,Constantes.EVENTO_GENERAR_TRAMITE_GRUPO //cdevento
		            	    ,fechaHoy   //fecha
		            	    ,cdusuari
		            	    ,((UserVO)session.get("USUARIO")).getRolActivo().getClave()
		            	    ,ntramiteNew
		            	    ,cdunieco
		            	    ,cdramo
		            	    ,"W"
		            	    ,nmpoliza
		            	    ,nmpoliza
		            	    ,cdagente
		            	    ,null
		            	    ,null);
		            }
		            catch(Exception ex)
		            {
		            	logger.error("Error al grabar evento, sin impacto",ex);
		            }
				}
				else
				{
					logger.debug("44.- Entra al Else");
					Map<String,Object>params=new HashMap<String,Object>();
					String ntramiteActualiza = ntramite;
					if(hayTramiteVacio)
					{
						ntramiteActualiza = ntramiteVacio;
					}
					logger.debug("45.- kernelManager.mesaControlUpdateSolici");
					kernelManager.mesaControlUpdateSolici(ntramiteActualiza, nmpoliza);
					params.put("pv_ntramite_i"  , ntramiteActualiza);
					params.put("pv_otvalor01_i" , clasif);
					params.put("pv_otvalor02_i" , sincenso ? "S" : "N");
					logger.debug("46.- siniestrosManager.actualizaOTValorMesaControl");
					siniestrosManager.actualizaOTValorMesaControl(params);
					
					if (duplicar) {
					    RespuestaTurnadoVO despacho = despachadorManager.turnarTramite(
					            cdusuari,
					            cdsisrol,
					            ntramite,
					            EstatusTramite.EN_ESPERA_DE_COTIZACION.getCodigo(),
					            Utils.join("Se duplica la cotizaci\u00f3n para generar la nueva solicitud ", nmpoliza),
					            null,  // cdrazrecha
					            null,  // cdusuariDes
					            null,  // cdsisrolDes
					            true,  // permisoAgente
					            false, // porEscalamiento
					            fechaHoy,
					            false  // sinGrabarDetalle
					            );
					    resp.respuesta = despacho.getMessage();
					}
				}
			}
			catch(Exception ex)
			{
				long timestamp       = System.currentTimeMillis();
				resp.exito           = false;
				resp.respuesta       = "Error al guardar tr\u00e1mite #"+timestamp;
				resp.respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		//mpoliage
		logger.debug("47.- resp.exito: {} hayTramite:{} hayTramiteVacio:{} ",resp.exito,hayTramite,hayTramiteVacio);
		if(resp.exito&&(!hayTramite||hayTramiteVacio||duplicar))
		{
			try
			{
				Map<String,String>datosAgenteExterno=cotizacionManager.obtenerDatosAgente(cdagente,cdramo);
    			String nmcuadro=datosAgenteExterno.get("NMCUADRO");
				
				Map<String,Object>map3=new LinkedHashMap<String,Object>(0);
		        map3.put("pv_cdunieco_i" , cdunieco);
		        map3.put("pv_cdramo_i"   , cdramo);
		        map3.put("pv_estado_i"   , "W");
		        map3.put("pv_nmpoliza_i" , nmpoliza);
		        map3.put("pv_cdagente_i" , cdagente);
		        map3.put("pv_nmsuplem_i" , "0");
		        map3.put("pv_status_i"   , "V");
		        map3.put("pv_cdtipoag_i" , "1");
		        map3.put("pv_porredau_i" , "0");
		        map3.put("pv_nmcuadro_i" , nmcuadro);
		        map3.put("pv_cdsucurs_i" , null);
		        map3.put("pv_accion_i"   , "I");
		        if(!hayTramite)
		        {
		        	map3.put("pv_ntramite_i" , smap1.get("ntramite"));
		        }
		        else if(hayTramiteVacio)
		        {
		        	map3.put("pv_ntramite_i" , ntramiteVacio);
		        }
		        else
		        {
		        	map3.put("pv_ntramite_i" , ntramite);//nunca deberia ir por aqui 
		        }
		        map3.put("pv_porparti_i" , "100");
		        logger.debug("48.-  kernelManager.movMPoliage");
		        kernelManager.movMPoliage(map3);
			}
			catch(Exception ex)
			{
				long timestamp       = System.currentTimeMillis();
				resp.exito           = false;
				resp.respuesta       = "Error al insertar datos del agente #"+timestamp;
				resp.respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		//TDESCSUP
		if(resp.exito&&(!hayTramite||hayTramiteVacio))
		{
			
		}
		
		//sigsvalipol
		logger.debug("49.- resp.exito:{} asincrono:{}",resp.exito,asincrono);
		if(resp.exito&&asincrono==false)
		{
			try
			{
				//cotizacionManager.movimientoTbloqueo(cdunieco, cdramo, "W", nmpoliza, "-8", Constantes.DELETE_MODE);
				logger.debug(Utils.log("cdusuari", cdusuari, "cdsisrol", cdsisrol));
				logger.debug("50.- cotizacionManager.ejecutaTarificacionConcurrente");
				cotizacionManager.ejecutaTarificacionConcurrente(
	            		cdunieco
	            		,cdramo
	            		,"W"
	            		,nmpoliza
	            		,"0"
	            		,"0"
	            		,"1"
	            		,cdperpag
	            		,cdusuari
	            		,cdsisrol
	            		);
			}
			catch(Exception ex)
			{
				long timestamp       = System.currentTimeMillis();
				resp.exito           = false;
				resp.respuesta       = "Error al cotizar #"+timestamp;
				resp.respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		logger.debug("51.- resp.exito:{} asincrono:{}",resp.exito,asincrono);
		if(resp.exito&&asincrono==true)
		{
			logger.debug("52.- cotizacionManager.procesoColectivoAsincrono");
			cotizacionManager.procesoColectivoAsincrono(
					hayTramite
					,hayTramiteVacio
					,censoAtrasado
					,complemento
					,cdunieco
					,cdramo
					,nmpoliza
					,cdperpag
					,clasif
					,LINEA
					,LINEA_EXTENDIDA
					,olist1
					,cdtipsit
					,cdusuari
					,cdsisrol
					,duplicar
					);
		}
		
		logger.debug(Utils.log(
				 "\n###### resp=",resp
				,"\n###### tvalositSigsvdefTvalogarContratanteTramiteSigsvalipol ######"
				,"\n###################################################################"
				));
		return resp;
	}
	
	private tvalositSigsvdefTvalogarContratanteTramiteSigsvalipolObject tvalositSigsvdefTvalogarContratanteTramiteSigsvalipolEndoso(
			String clasif
			,String LINEA
			,String LINEA_EXTENDIDA
			,String cdunieco
			,String cdramo
			,String nmpoliza
			,String cdtipsit
			,boolean hayTramite
			,boolean hayTramiteVacio
			,String cdusuari
			,String cdelemento
			,String ntramiteVacio
			,boolean reinsertaContratante
			,String ntramite
			,String cdagente
			,boolean sincenso
			,boolean censoAtrasado
			,boolean resubirCenso
			,String cdperpag
			,String cdsisrol
			,boolean complemento
			,boolean asincrono
			,String cdmunici
			,String cdedo
			,String codpostal
			,String estado
			,String nmsuplem
			)
	{
		logger.debug(
				new StringBuilder()
				.append("\n#################################################################")
				.append("\n## tvalositSigsvdefTvalogarContratanteTramiteSigsvalipolEndoso ##")
				.append("\n## clasif: ")              .append(clasif)
				.append("\n## LINEA: ")               .append(LINEA)
				.append("\n## LINEA_EXTENDIDA: ")     .append(LINEA_EXTENDIDA)
				.append("\n## cdunieco: ")            .append(cdunieco)
				.append("\n## cdramo: ")              .append(cdramo)
				.append("\n## nmpoliza: ")            .append(nmpoliza)
				.append("\n## cdtipsit: ")            .append(cdtipsit)
				.append("\n## hayTramite: ")          .append(hayTramite)
				.append("\n## hayTramiteVacio: ")     .append(hayTramiteVacio)
				.append("\n## cdusuari: ")            .append(cdusuari)
				.append("\n## cdelemento: ")          .append(cdelemento)
				.append("\n## ntramiteVacio: ")       .append(ntramiteVacio)
				.append("\n## reinsertaContratante: ").append(reinsertaContratante)
				.append("\n## ntramite: ")            .append(ntramite)
				.append("\n## cdagente: ")            .append(cdagente)
				.append("\n## sincenso: ")            .append(sincenso)
				.append("\n## censoAtrasado: ")       .append(censoAtrasado)
				.append("\n## resubirCenso: ")        .append(resubirCenso)
				.append("\n## cdperpag: ")            .append(cdperpag)
				.append("\n## cdsisrol: ")            .append(cdsisrol)
				.append("\n## complemento: ")         .append(complemento)
				.append("\n## asincrono: ")           .append(asincrono)
				.toString()
				);
		tvalositSigsvdefTvalogarContratanteTramiteSigsvalipolObject resp =
				new tvalositSigsvdefTvalogarContratanteTramiteSigsvalipolObject();
		resp.exito = true;
		
		//tvalosit
		logger.debug("1.- Valor de resp.exito ===>"+resp.exito);
		if(resp.exito)
		{
			try
			{
				logger.debug("2.- Valor de clasif: {} LINEA :{} LINEA_EXTENDIDA: {}",clasif,LINEA,LINEA_EXTENDIDA );
				if(clasif.equals(LINEA)&&LINEA_EXTENDIDA.equals("S"))
				{
					for(Map<String,Object>iGrupo:olist1)
					{
						String cdgrupo = (String)iGrupo.get("letra");
						
						//SUMA ASEGURADA Y MATERNIDAD
						String ptsumaaseg = (String)iGrupo.get("ptsumaaseg");
						String ayudamater = (String)iGrupo.get("ayudamater");
						Object incrinflL  = iGrupo.get("incrinfl");
						String incrinfl   = incrinflL!=null? incrinflL.toString() : "";
						Object extrrenoL  = iGrupo.get("extrreno");
						String extrreno   = extrrenoL!=null? extrrenoL.toString() : "";
						Object cesicomiL  = iGrupo.get("cesicomi");
						String cesicomi   = cesicomiL!=null? cesicomiL.toString() : "";
						Object pondubicL  = iGrupo.get("pondubic");
						String pondubic   = pondubicL!=null? pondubicL.toString() : "";
						Object descbonoL  = iGrupo.get("descbono");
						String descbono   = descbonoL!=null? descbonoL.toString() : "";
						
						//nuevos factores de lolita
						Object gastadmiL = iGrupo.get("gastadmi");
						String gastadmi  = gastadmiL!=null? gastadmiL.toString() : "";
						
						Object utilidadL = iGrupo.get("utilidad");
						String utilidad  = utilidadL!=null? utilidadL.toString() : "";
						
						Object comiagenL = iGrupo.get("comiagen");
						String comiagen  = comiagenL!=null? comiagenL.toString() : "";
						
						Object comipromL = iGrupo.get("comiprom");
						String comiprom  = comipromL!=null? comipromL.toString() : "";
						
						Object bonoinceL = iGrupo.get("bonoince");
						String bonoince  = bonoinceL!=null? bonoinceL.toString() : "";
						
						Object otrogastL = iGrupo.get("otrogast");
						String otrogast  = otrogastL!=null? otrogastL.toString() : "";
						
						//nuevos factores de lolita fin
						
						logger.debug("3.- Entra  la cotizacionManager.movimientoMpolisitTvalositGrupo ");
						cotizacionManager.movimientoMpolisitTvalositGrupo(
								cdunieco, cdramo, estado, nmpoliza,
								cdgrupo, ptsumaaseg, incrinfl, extrreno,
								cesicomi, pondubic, descbono, gastadmi,
								utilidad, comiagen, comiprom, bonoince, otrogast,
								(String)iGrupo.get("nombre"),ayudamater);
						
						logger.debug("4.- Entra a actualizaValoresDefectoSituacion");
						cotizacionManager.actualizaValoresDefectoSituacion(cdunieco,cdramo,estado,nmpoliza,nmsuplem);
					}
				}
				else
				{
					logger.debug("5.- Entra al else");
					for(Map<String,Object>iGrupo:olist1)
					{
						String cdgrupo = (String)iGrupo.get("letra");
						
						//SUMA ASEGURADA y ayuda maternidad
						String ptsumaaseg = (String)iGrupo.get("ptsumaaseg");
						String ayudamater = "0";
						Object incrinflL  = iGrupo.get("incrinfl");
						String incrinfl   = incrinflL!=null? incrinflL.toString() : "0";
						Object extrrenoL  = iGrupo.get("extrreno");
						String extrreno   = extrrenoL!=null? extrrenoL.toString() : "0";
						Object cesicomiL  = iGrupo.get("cesicomi");
						String cesicomi   = cesicomiL!=null? cesicomiL.toString() : "0";
						Object pondubicL  = iGrupo.get("pondubic");
						String pondubic   = pondubicL!=null? pondubicL.toString() : "0";
						Object descbonoL  = iGrupo.get("descbono");
						String descbono   = descbonoL!=null? descbonoL.toString() : "0";
						
						//nuevos factores de Lolita
						Object gastadmiL = iGrupo.get("gastadmi");
						String gastadmi  = gastadmiL!=null? gastadmiL.toString() : "0";
						
						Object utilidadL = iGrupo.get("utilidad");
						String utilidad  = utilidadL!=null? utilidadL.toString() : "0";
						
						Object comiagenL = iGrupo.get("comiagen");
						String comiagen  = comiagenL!=null? comiagenL.toString() : "0";
						
						Object comipromL = iGrupo.get("comiprom");
						String comiprom  = comipromL!=null? comipromL.toString() : "0";
						
						Object bonoinceL = iGrupo.get("bonoince");
						String bonoince  = bonoinceL!=null? bonoinceL.toString() : "0";
						
						Object otrogastL = iGrupo.get("otrogast");
						String otrogast  = otrogastL!=null? otrogastL.toString() : "0";
						//nuevos factores de Lolita fin
						
						List<Map<String,String>>tvalogars=(List<Map<String,String>>)iGrupo.get("tvalogars");
						for(Map<String,String>iTvalogar:tvalogars)
						{
							String cdgarant  = iTvalogar.get("cdgarant");
							boolean amparada = "S".equals(iTvalogar.get("amparada"));
							if("4AYM".equalsIgnoreCase(cdgarant) && amparada )
							{
								ayudamater = iTvalogar.get("parametros.pv_otvalor001");
							}
						}
						logger.debug("5.- Entra a cotizacionManager.movimientoMpolisitTvalositGrupo");
						cotizacionManager.movimientoMpolisitTvalositGrupo(
								cdunieco, cdramo, estado, nmpoliza,
								cdgrupo, ptsumaaseg, incrinfl, extrreno,
								cesicomi, pondubic, descbono, gastadmi,
								utilidad, comiagen, comiprom, bonoince, otrogast,
								(String)iGrupo.get("nombre"),ayudamater);
					}
				}
			}
			catch(Exception ex)
			{
				long timestamp       = System.currentTimeMillis();
				resp.exito           = false;
				resp.respuesta       = "Error al guardar grupos #"+timestamp;
				resp.respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		logger.debug("6.- Valor de Exito:{} codpostal : {}  StringUtils.isNotBlank(codpostal) :{}",exito,codpostal,StringUtils.isNotBlank(codpostal));
		if(exito && StringUtils.isNotBlank(codpostal)){
			try {
				logger.debug("7.- Entra a cotizacionManager.actualizaDomicilioAseguradosColectivo");
				cotizacionManager.actualizaDomicilioAseguradosColectivo(cdunieco, cdramo, estado, nmpoliza, nmsuplem, codpostal, cdedo, cdmunici);
			} catch (Exception ex) {
				long timestamp       = System.currentTimeMillis();
				resp.exito           = false;
				resp.respuesta       = "Error al guardar domiciio tvalopol #"+timestamp;
				resp.respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		logger.debug("8.- Valor de resp.exito : {} hayTramite:{} hayTramiteVacio:{}",resp.exito,hayTramite,hayTramiteVacio);
		if(resp.exito
				&&(!hayTramite||hayTramiteVacio)
				&&
				(
					RolSistema.AGENTE.getCdsisrol().equals(cdsisrol)
					||RolSistema.EJECUTIVO_INTERNO.getCdsisrol().equals(cdsisrol)
					||RolSistema.MESA_DE_CONTROL.getCdsisrol().equals(cdsisrol)
					||RolSistema.SUSCRIPTOR.getCdsisrol().equals(cdsisrol)
				)
		)
		{
			asincrono = true;
			logger.debug("9.- asincrono : {}",asincrono);
		}
		
		//sigsvdef
		logger.debug("10.- resp.exito : {} hayTramite: {} hayTramiteVacio: {} censoAtrasado: {} resubirCenso: {} complemento:{} asincrono:{} ",resp.exito, 
				hayTramite,hayTramiteVacio,censoAtrasado,resubirCenso,complemento,asincrono);
		
		if(resp.exito&&(!hayTramite||hayTramiteVacio||censoAtrasado||resubirCenso||complemento)&&asincrono==false)
		{
			try
			{
				/*
				Map<String,String> mapCoberturas=new HashMap<String,String>(0);
	            mapCoberturas.put("pv_cdunieco_i" , cdunieco);
	            mapCoberturas.put("pv_cdramo_i"   , cdramo);
	            mapCoberturas.put("pv_estado_i"   , "W");
	            mapCoberturas.put("pv_nmpoliza_i" , nmpoliza);
	            mapCoberturas.put("pv_nmsituac_i" , "0");
	            mapCoberturas.put("pv_nmsuplem_i" , "0");
	            mapCoberturas.put("pv_cdgarant_i" , "TODO");
	            mapCoberturas.put("pv_cdtipsup_i" , "1");
	            kernelManager.coberturas(mapCoberturas);
	            */
				logger.debug("11.- Entra a cotizacionManager.ejecutaValoresDefectoConcurrente");
	            cotizacionManager.ejecutaValoresDefectoConcurrente(
	            		cdunieco
	            		,cdramo
	            		,estado
	            		,nmpoliza
	            		,nmsuplem
	            		,"0"
	            		,"3"
	            		,cdperpag
	            		);
	            
	            try
	            {
	            	logger.debug("12.- Entra a serviciosManager.grabarEvento");
	            	serviciosManager.grabarEvento(new StringBuilder("\nCotizacion grupo")
	            	    ,Constantes.MODULO_COTIZACION //cdmodulo
	            	    ,Constantes.EVENTO_COTIZAR     //cdevento
	            	    ,new Date()   //fecha
	            	    ,cdusuari
	            	    ,((UserVO)session.get("USUARIO")).getRolActivo().getClave()
	            	    ,ntramite
	            	    ,cdunieco
	            	    ,cdramo
	            	    ,estado
	            	    ,nmpoliza
	            	    ,nmpoliza
	            	    ,cdagente
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
				long timestamp       = System.currentTimeMillis();
				resp.exito           = false;
				resp.respuesta       = "Error al insertar valores por defecto para las coberturas #"+timestamp;
				resp.respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		logger.debug("13.- Valor de resp.exito : {} asincrono:{} ",resp.exito,asincrono);
		//tvalogar
		if(resp.exito&&asincrono==false)
		{
			try
			{
				logger.debug("14.- Valor de clasif : {} LINEA:{}  LINEA_EXTENDIDA:{}",clasif,LINEA, LINEA_EXTENDIDA);
				if(clasif.equals(LINEA)&&LINEA_EXTENDIDA.equals("S"))
				{
					for(Map<String,Object>iGrupo:olist1)
					{
						String cdgrupo = (String)iGrupo.get("letra");
						
						//HOSPITALIZACION (DEDUCIBLE)
						String cdgarant = "4HOS";
						String cdatribu = "001";
						String valor    = (String)iGrupo.get("deducible");
						logger.debug("15.- cotizacionManager.movimientoTvalogarGrupo");
						cotizacionManager.movimientoTvalogarGrupo(cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdtipsit, cdgrupo, cdgarant, "V", cdatribu, valor);
						
						//ASISTENCIA INTERNACIONAL VIAJES --AHORA MEDICAMENTOS
						String asisinte = (String)iGrupo.get("asisinte");
						if (StringUtils.isBlank(asisinte)) {
							asisinte = "0";
						}
						cdgarant = "4MED";
						logger.debug("16.- Asistente :{}",Integer.parseInt(asisinte));
						if(Integer.parseInt(asisinte)>0)
						{
							logger.debug("17.- cotizacionManager.movimientoMpoligarGrupo");
							cotizacionManager.movimientoMpoligarGrupo(
									cdunieco
									,cdramo
									,estado
									,nmpoliza
									,nmsuplem
									,cdtipsit
									,cdgrupo
									,cdgarant
									,"V"
									,"001"
									,Constantes.INSERT_MODE
									,"S"
									);
							logger.debug("18.- cotizacionManager.movimientoTvalogarGrupo");
							cotizacionManager.movimientoTvalogarGrupo(
									cdunieco
									,cdramo
									,estado
									,nmpoliza
									,nmsuplem
									,cdtipsit
									,cdgrupo
									,cdgarant
									,"V" //status
									,"001"
									,asisinte
									);
						}
						else
						{
							logger.debug("19.- cotizacionManager.movimientoMpoligarGrupo");
							cotizacionManager.movimientoMpoligarGrupo(
									cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdtipsit, cdgrupo, cdgarant, "V", "001", Constantes.DELETE_MODE, null);
						}
						
						//EMERGENCIA EXTRANJERO
						String emerextr = (String)iGrupo.get("emerextr");
						cdgarant = "4EE";
						logger.debug("20.- emerextr : {}",emerextr);
						if(emerextr.equalsIgnoreCase("S"))
						{
							logger.debug("21.- cotizacionManager.movimientoMpoligarGrupo");
							cotizacionManager.movimientoMpoligarGrupo(
									cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdtipsit, cdgrupo, cdgarant, "V", "001", Constantes.INSERT_MODE, null);
						}
						else
						{
							logger.debug("22.- cotizacionManager.movimientoMpoligarGrupo");
							cotizacionManager.movimientoMpoligarGrupo(
									cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdtipsit, cdgrupo, cdgarant, "V", "001", Constantes.DELETE_MODE, null);
						}
					}
				}
				else
				{
					logger.debug("23.- Entra al else");
					for(Map<String,Object>iGrupo:olist1)
					{
						String cdgrupo = (String)iGrupo.get("letra");
						
						List<Map<String,String>>tvalogars=(List<Map<String,String>>)iGrupo.get("tvalogars");
						for(Map<String,String>iTvalogar:tvalogars)
						{
							String cdgarant  = iTvalogar.get("cdgarant");
							boolean amparada = StringUtils.isNotBlank(iTvalogar.get("amparada"))
									&&iTvalogar.get("amparada").equalsIgnoreCase("S");
							
							logger.debug("24.- cdgarant : {} amparada:{}",cdgarant,amparada);
								if(amparada)
								{
									logger.debug("25.- cotizacionManager.movimientoMpoligarGrupo");
									cotizacionManager.movimientoMpoligarGrupo(
											cdunieco, cdramo,  estado, nmpoliza, nmsuplem, cdtipsit, cdgrupo, cdgarant, "V", "001", Constantes.INSERT_MODE, null);
									//buscar cdatribus
									boolean hayAtributos=false;
									Map<String,String>listaCdatribu=new HashMap<String,String>();
									for(Entry<String,String>iAtribTvalogar:iTvalogar.entrySet())
									{
										String key=iAtribTvalogar.getKey();
										if(key!=null
												&&key.length()>"parametros.pv_otvalor".length()
												&&key.substring(0, "parametros.pv_otvalor".length()).equalsIgnoreCase("parametros.pv_otvalor"))
										{
											hayAtributos=true;
											listaCdatribu.put(key.substring("parametros.pv_otvalor".length(), key.length()),iAtribTvalogar.getValue());
										}
									}
									if(hayAtributos)
									{
										for(Entry<String,String>atributo:listaCdatribu.entrySet())
										{
											if(StringUtils.isNotBlank(atributo.getValue()))
											{
												logger.debug("26.- cotizacionManager.movimientoTvalogarGrupo");
											    cotizacionManager.movimientoTvalogarGrupo(
													cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdtipsit, cdgrupo, cdgarant, "V",
													atributo.getKey(), atributo.getValue());
											}
										}
									}
								}
								else
								{
									logger.debug("27.- cotizacionManager.movimientoMpoligarGrupo");
									cotizacionManager.movimientoMpoligarGrupo(
											cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdtipsit, cdgrupo, cdgarant, "V", "001", Constantes.DELETE_MODE, null);
								}
							//}
						}
					}
				}
			}
			catch(Exception ex)
			{
				long timestamp       = System.currentTimeMillis();
				resp.exito           = false;
				resp.respuesta       = "Error al guardar las coberturas #"+timestamp; 
				resp.respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		//contratante
		/*logger.debug("28.- Valor de resp.exito:{}",resp.exito);
		if(resp.exito)
		{
			try
			{
				
				UserVO usuario = (UserVO) session.get("USUARIO");
				String usuarioCaptura =  null;
				
				if(usuario!=null){
					if(StringUtils.isNotBlank(usuario.getClaveUsuarioCaptura())){
						usuarioCaptura = usuario.getClaveUsuarioCaptura();
					}else{
						usuarioCaptura = usuario.getCodigoPersona();
					}
					
				}
				
				String cdperson  = smap1.get("cdperson");
				String exiper    = "N";
				String cdideper_ = smap1.get("cdideper_");
				String cdideext_ = smap1.get("cdideext_");
				
				boolean nuevoCdperson = StringUtils.isBlank(cdperson);
				logger.debug("29.- Valor de nuevoCdperson: {}",nuevoCdperson);
				if(nuevoCdperson)
				{
					logger.debug("30.- storedProceduresManager.procedureParamsCall");
					Map<String,Object>cdpersonMap=storedProceduresManager.procedureParamsCall(
							ObjetoBD.GENERAR_CDPERSON.getNombre(),
							new LinkedHashMap<String,Object>(),
							null,
							new String[]{"pv_cdperson_o"},
							null);
					cdperson = (String)cdpersonMap.get("pv_cdperson_o");
				}else{
					logger.debug("31.- cotizacionManager.obtieneDatosContratantePoliza");
					Map<String,String> datosCont = cotizacionManager.obtieneDatosContratantePoliza(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
					if(datosCont != null && !datosCont.isEmpty() && Constantes.SI.equalsIgnoreCase(datosCont.get("SWEXIPER"))){
						exiper = "S";
					}
				}
				
				logger.debug("32.- nuevoCdperson: {} reinsertaContratante: {} censoAtrasado: {} resubirCenso:{} ",nuevoCdperson,reinsertaContratante,censoAtrasado,resubirCenso);
				if(nuevoCdperson||reinsertaContratante||censoAtrasado||resubirCenso)
				{
					logger.debug("33.- valor:{}", !Constantes.SI.equalsIgnoreCase(exiper));
					if(!Constantes.SI.equalsIgnoreCase(exiper)){
						LinkedHashMap<String,Object> parametros=new LinkedHashMap<String,Object>(0);
						parametros.put("param01_pv_cdperson_i"    , cdperson);
						parametros.put("param02_pv_cdtipide_i"    , "1");
						parametros.put("param03_pv_cdideper_i"    , cdideper_);
						parametros.put("param04_pv_dsnombre_i"    , smap1.get("nombre"));
						parametros.put("param05_pv_cdtipper_i"    , "1");
						parametros.put("param06_pv_otfisjur_i"    , "M");
						parametros.put("param07_pv_otsexo_i"      , "H");
						parametros.put("param08_pv_fenacimi_i"    , new Date());
						parametros.put("param09_pv_cdrfc_i"       , smap1.get("cdrfc"));
						parametros.put("param10_pv_dsemail_i"     , "");
						parametros.put("param11_pv_dsnombre1_i"   , null);
						parametros.put("param12_pv_dsapellido_i"  , null);
						parametros.put("param13_pv_dsapellido1_i" , null);
						parametros.put("param14_pv_feingreso_i"   , new Date());
						parametros.put("param15_pv_cdnacion_i"    , "001");
						parametros.put("param16"                  , null);
						parametros.put("param17"                  , null);
						parametros.put("param18"                  , null);
						parametros.put("param19"                  , null);
						parametros.put("param20"                  , null);
						parametros.put("param21"                  , cdideext_);
						parametros.put("param22"                  , null);
						parametros.put("param23"                  , null);
						parametros.put("param24"                  , usuarioCaptura);
						parametros.put("param25_pv_accion_i"      , "I");
						String[] tipos=new String[]{
								"VARCHAR","VARCHAR","VARCHAR","VARCHAR",
								"VARCHAR","VARCHAR","VARCHAR","DATE",
								"VARCHAR","VARCHAR","VARCHAR","VARCHAR",
								"VARCHAR","DATE"   ,"VARCHAR","VARCHAR",
								"VARCHAR","VARCHAR","VARCHAR","VARCHAR",
								"VARCHAR","VARCHAR","VARCHAR","VARCHAR",
								"VARCHAR"
						};
						logger.debug("33.- MOV_MPERSONA");
						storedProceduresManager.procedureVoidCall(ObjetoBD.MOV_MPERSONA.getNombre(), parametros, tipos);
					}
				}
				
				LinkedHashMap<String,Object> parametros=new LinkedHashMap<String,Object>(0);
				parametros.put("param01_pv_cdunieco_i" , cdunieco);
				parametros.put("param02_pv_cdramo_i"   , cdramo);
				parametros.put("param03_pv_estado_i"   , estado);
				parametros.put("param04_pv_nmpoliza_i" , nmpoliza);
				parametros.put("param05_pv_nmsituac_i" , "0");
				parametros.put("param06_pv_cdrol_i"    , "1");
				parametros.put("param07_pv_cdperson_i" , cdperson);
				parametros.put("param08_pv_nmsuplem_i" , nmsuplem);
				parametros.put("param09_pv_status_i"   , "V");
				parametros.put("param10_pv_nmorddom_i" , "1");
				parametros.put("param11_pv_swreclam_i" , null);
				parametros.put("param12_pv_accion_i"   , "I");
				parametros.put("param13_pv_swexiper_i" , exiper);
				logger.debug("34.- MOV_MPOLIPER");
				storedProceduresManager.procedureVoidCall(ObjetoBD.MOV_MPOLIPER.getNombre(), parametros, null);
				logger.debug("35.-VALOR DE SWEXIPER : "+ exiper);
				
				logger.debug("36.- validacion: {}",!Constantes.SI.equalsIgnoreCase(exiper));
				if(!Constantes.SI.equalsIgnoreCase(exiper)){
					Map<String,String> paramDomicil = new HashMap<String, String>();
					paramDomicil.put("pv_cdperson_i" , cdperson);
					paramDomicil.put("pv_nmorddom_i" , "1");
					paramDomicil.put("pv_msdomici_i" , smap1.get("dsdomici"));
					paramDomicil.put("pv_nmtelefo_i" , null);
					paramDomicil.put("pv_cdpostal_i" , smap1.get("codpostal"));
					paramDomicil.put("pv_cdedo_i"    , smap1.get("cdedo"));
					paramDomicil.put("pv_cdmunici_i" , smap1.get("cdmunici"));
					paramDomicil.put("pv_cdcoloni_i" , null);
					paramDomicil.put("pv_nmnumero_i" , smap1.get("nmnumero"));
					paramDomicil.put("pv_nmnumint_i" , smap1.get("nmnumint"));
					paramDomicil.put("pv_cdtipdom_i", "1");
	    			paramDomicil.put("pv_cdusuario_i", usuarioCaptura);
	    			paramDomicil.put("pv_swactivo_i" , Constantes.SI);
					paramDomicil.put("pv_accion_i"   , Constantes.INSERT_MODE);
					logger.debug("37.- kernelManager.pMovMdomicil");
					kernelManager.pMovMdomicil(paramDomicil);
				}
				
			}
			catch(Exception ex)
			{
				long timestamp       = System.currentTimeMillis();
				resp.exito           = false;
				resp.respuesta       = "Error al guardar el contratante #"+timestamp;
				resp.respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}*/
		
		//tramite
		logger.debug("38.- resp.exito:{} hayTramite:{} hayTramiteVacio: {} censoAtrasado: {}",resp.exito,hayTramite,hayTramiteVacio,censoAtrasado);
		if(resp.exito&&(!hayTramite||hayTramiteVacio||censoAtrasado))
		{
			try
			{
				logger.debug("39.- hayTramite: {} hayTramiteVacio:{}",hayTramite,hayTramiteVacio);
				if(!hayTramite&&!hayTramiteVacio)//es agente
				{
					Map<String,String> valores = new LinkedHashMap<String,String>();
					valores.put("otvalor01" , clasif);
					valores.put("otvalor02" , sincenso ? "S" : "N");
					
					logger.debug("40.- consultasManager.recuperarDatosFlujoEmision");
					Map<String,String> datosFlujo = consultasManager.recuperarDatosFlujoEmision(cdramo,"C");
					
					String estatus = EstatusTramite.EN_ESPERA_DE_COTIZACION.getCodigo();
                    try {
                        estatus = flujoMesaControlManager.recuperarEstatusDefectoRol(cdsisrol);
                    } catch (Exception ex) {
                        logger.warn("Error sin impacto al querer recuperar estatus por defecto de un rol", ex);
                    }
					
					Date fechaHoy = new Date();
					
					//logger.debug("");
					logger.debug("41.- mesaControlManager.movimientoTramite");
					String ntramiteNew = mesaControlManager.movimientoTramite(
							cdunieco
							,cdramo
							,estado
							,nmpoliza
							,nmsuplem
							,cdunieco
							,cdunieco
							,TipoTramite.POLIZA_NUEVA.getCdtiptra()
							,fechaHoy
							,cdagente
							,null
							,null
							,fechaHoy
							,estatus
							,null
							,nmpoliza
							,cdtipsit
							,cdusuari
							,cdsisrol
							,null //swimpres
	            			,datosFlujo.get("cdtipflu")
	            			,datosFlujo.get("cdflujomc")
							,valores
							,TipoEndoso.EMISION_POLIZA.getCdTipSup().toString(), null, null, null
							);
					smap1.put("ntramite",ntramiteNew);
					
					logger.debug("42.- mesaControlManager.movimientoDetalleTramite");
					
					RespuestaTurnadoVO despacho = despachadorManager.turnarTramite(
                            cdusuari,
                            cdsisrol,
                            ntramiteNew,
                            estatus,
                            "Se guard\u00f3 un nuevo tr\u00e1mite en mesa de control desde cotizaci\u00f3n de agente",
                            null,  // cdrazrecha
                            null,  // cdusuariDes
                            null,  // cdsisrolDes
                            true,  // permisoAgente
                            false, // porEscalamiento
                            fechaHoy,
                            false  //sinGrabarDetalle
                            );
					resp.respuesta = despacho.getMessage();
					
					/* JTEZVA 7 sep 2016
                     * el tramite no se turna por lo que no lleva doble detalle
					mesaControlManager.movimientoDetalleTramite(
							ntramiteNew
							,new Date()
							,null
							,"Se guard\u00f3 un nuevo tr\u00e1mite en mesa de control desde cotizaci\u00f3n de agente"
							,cdusuari
							,null
							,cdsisrol
							,"S"
							,EstatusTramite.PENDIENTE.getCodigo()
							,false
							);
	            	
	            	smap1.put("nombreUsuarioDestino"
	            			,cotizacionManager.turnaPorCargaTrabajo(ntramiteNew,"COTIZADOR",EstatusTramite.EN_ESPERA_DE_COTIZACION.getCodigo())
	            			);
	            	*/
	            	
	            	try
		            {
	            		logger.debug("43.- serviciosManager.grabarEvento");
		            	serviciosManager.grabarEvento(new StringBuilder("\nNuevo tramite grupo")
		            	    ,Constantes.MODULO_EMISION               //cdmodulo
		            	    ,Constantes.EVENTO_GENERAR_TRAMITE_GRUPO //cdevento
		            	    ,new Date()   //fecha
		            	    ,cdusuari
		            	    ,((UserVO)session.get("USUARIO")).getRolActivo().getClave()
		            	    ,ntramiteNew
		            	    ,cdunieco
		            	    ,cdramo
		            	    ,estado
		            	    ,nmpoliza
		            	    ,nmpoliza
		            	    ,cdagente
		            	    ,null
		            	    ,null);
		            }
		            catch(Exception ex)
		            {
		            	logger.error("Error al grabar evento, sin impacto",ex);
		            }
				}
				else
				{
					logger.debug("44.- Entra al Else");
					Map<String,Object>params=new HashMap<String,Object>();
					String ntramiteActualiza = ntramite;
					if(hayTramiteVacio)
					{
						ntramiteActualiza = ntramiteVacio;
					}
					logger.debug("45.- kernelManager.mesaControlUpdateSolici");
					kernelManager.mesaControlUpdateSolici(ntramiteActualiza, nmpoliza);
					params.put("pv_ntramite_i"  , ntramiteActualiza);
					params.put("pv_otvalor01_i" , clasif);
					params.put("pv_otvalor02_i" , sincenso ? "S" : "N");
					logger.debug("46.- siniestrosManager.actualizaOTValorMesaControl");
					siniestrosManager.actualizaOTValorMesaControl(params);
				}
			}
			catch(Exception ex)
			{
				long timestamp       = System.currentTimeMillis();
				resp.exito           = false;
				resp.respuesta       = "Error al guardar tr\u00e1mite #"+timestamp;
				resp.respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		//mpoliage
		logger.debug("47.- resp.exito: {} hayTramite:{} hayTramiteVacio:{} ",resp.exito,hayTramite,hayTramiteVacio);
		if(resp.exito&&(!hayTramite||hayTramiteVacio))
		{
			try
			{
				Map<String,String>datosAgenteExterno=cotizacionManager.obtenerDatosAgente(cdagente,cdramo);
    			String nmcuadro=datosAgenteExterno.get("NMCUADRO");
				
				Map<String,Object>map3=new LinkedHashMap<String,Object>(0);
		        map3.put("pv_cdunieco_i" , cdunieco);
		        map3.put("pv_cdramo_i"   , cdramo);
		        map3.put("pv_estado_i"   , estado);
		        map3.put("pv_nmpoliza_i" , nmpoliza);
		        map3.put("pv_cdagente_i" , cdagente);
		        map3.put("pv_nmsuplem_i" , nmsuplem);
		        map3.put("pv_status_i"   , "V");
		        map3.put("pv_cdtipoag_i" , "1");
		        map3.put("pv_porredau_i" , "0");
		        map3.put("pv_nmcuadro_i" , nmcuadro);
		        map3.put("pv_cdsucurs_i" , null);
		        map3.put("pv_accion_i"   , "I");
		        if(!hayTramite)
		        {
		        	map3.put("pv_ntramite_i" , smap1.get("ntramite"));
		        }
		        else if(hayTramiteVacio)
		        {
		        	map3.put("pv_ntramite_i" , ntramiteVacio);
		        }
		        else
		        {
		        	map3.put("pv_ntramite_i" , ntramite);//nunca deberia ir por aqui 
		        }
		        map3.put("pv_porparti_i" , "100");
		        logger.debug("48.-  kernelManager.movMPoliage");
		        kernelManager.movMPoliage(map3);
			}
			catch(Exception ex)
			{
				long timestamp       = System.currentTimeMillis();
				resp.exito           = false;
				resp.respuesta       = "Error al insertar datos del agente #"+timestamp;
				resp.respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		//TDESCSUP
		if(resp.exito&&(!hayTramite||hayTramiteVacio))
		{
			
		}
		
		//sigsvalipol
		logger.debug("49.- resp.exito:{} asincrono:{}",resp.exito,asincrono);
		if(resp.exito&&asincrono==false)
		{
			try
			{
				logger.debug("50.- cotizacionManager.sigsvalipolEnd");
				
				String cdtipsup   = TipoEndoso.ALTA_ASEGURADOS.getCdTipSup().toString();
				
				/*cotizacionManager.ejecutasigsvdefEnd(
            		cdunieco
            		,cdramo
            		,estado
            		,nmpoliza
            		,"0"
            		,nmsuplem
            		,"TODO"
            		,cdtipsup
        		);*/
				
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
			}
			catch(Exception ex)
			{
				long timestamp       = System.currentTimeMillis();
				resp.exito           = false;
				resp.respuesta       = "Error al cotizar #"+timestamp;
				resp.respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		logger.debug("51.- resp.exito:{} asincrono:{}",resp.exito,asincrono);
		if(resp.exito&&asincrono==true)
		{
			logger.debug("52.- cotizacionManager.procesoColectivoAsincrono");
			cotizacionManager.procesoColectivoAsincrono(
					hayTramite
					,hayTramiteVacio
					,censoAtrasado
					,complemento
					,cdunieco
					,cdramo
					,nmpoliza
					,cdperpag
					,clasif
					,LINEA
					,LINEA_EXTENDIDA
					,olist1
					,cdtipsit
					,cdusuari
					,cdsisrol
					,false // duplicar
					);
		}
		
		logger.debug(Utils.log(
				 "\n###### resp=",resp
				,"\n###### tvalositSigsvdefTvalogarContratanteTramiteSigsvalipolEndoso ######"
				,"\n#########################################################################"
				));
		return resp;
	}
	
	public String obtenerDetalleCotizacionGrupo()
	{
		logger.debug(""
				+ "\n###########################################"
				+ "\n###### obtenerDetalleCotizacionGrupo ######"
				+ "\nsmap1: "+smap1
				);
		try
		{
			LinkedHashMap<String,Object>params=new LinkedHashMap<String,Object>();
			params.put("param1" , smap1.get("cdunieco"));
			params.put("param2" , smap1.get("cdramo"));
			params.put("param3" , smap1.get("estado"));
			params.put("param4" , smap1.get("nmpoliza"));
			params.put("param5" , smap1.get("cdplan"));
			params.put("param6" , smap1.get("cdperpag"));
			slist1=storedProceduresManager.procedureListCall(ObjetoBD.OBTIENE_DETALLE_COTI_GRUPO.getNombre(),
					params, null);
			for(Map<String,String>detalle:slist1)
			{
				String header = detalle.get("NOMBRE")+ " (" + detalle.get("PARENTESCO") +")";
				String nmsitaux3 = detalle.get("NMSITAUX");
				if(nmsitaux3.length()==1)
				{
					nmsitaux3="00"+nmsitaux3;
				}
				else if(nmsitaux3.length()==2)
				{
					nmsitaux3="0"+nmsitaux3;
				}
				String nmsituac3 = detalle.get("NMSITUAC");
				if(nmsituac3.length()==1)
				{
					nmsituac3="00"+nmsituac3;
				}
				else if(nmsituac3.length()==2)
				{
					nmsituac3="0"+nmsituac3;
				}
				detalle.put("GRUPO",nmsitaux3+"_"+nmsituac3+"_"+header);
			}
		}
		catch(Exception ex)
		{
			long timestamp  = System.currentTimeMillis();
			logger.error(timestamp+" error inesperado al obtener detalle de cotizacion",ex);
			exito           = false;
			respuesta       = "Error al obtener detalle #"+timestamp;
			respuestaOculta = ex.getMessage();
		}
		logger.debug(""
				+ "\n###### obtenerDetalleCotizacionGrupo ######"
				+ "\n###########################################"
				);
		return SUCCESS;
	}
	
	public String cargarDatosCotizacionGrupo()
	{
		logger.debug(""
				+ "\n########################################"
				+ "\n###### cargarDatosCotizacionGrupo ######"
				+ "\nsmap1 "+smap1
				);
		success = true;
		try
		{
			params=cotizacionManager.cargarDatosCotizacionGrupo(
					smap1.get("cdunieco"), smap1.get("cdramo"),
					smap1.get("cdtipsit"), smap1.get("estado"),
					smap1.get("nmpoliza"), smap1.get("ntramite"));
		    respuesta       = "Todo OK";
		    respuestaOculta = "Todo OK";
		    exito           = true;
		}
		catch(Exception ex)
		{
			long timestamp=System.currentTimeMillis();
			logger.error("Error al cargar datos de cotizacion grupo "+timestamp,ex);
			exito           = false;
			respuesta       = "Error inesperado #"+timestamp;
			respuestaOculta = ex.getMessage();
		}
		
		try
        {
		    /**
	         * Para consultar si este tramite esta en espera de validacion de cambio de nombre de plan para alguno de los grupos
	         */
		    
		    //Se fija valor default N
		    smap1.put("ESPERA_AUT_NOMPLAN_SUPERV", "N");
		    
	        Map<String, String> datosBloq = cotizacionManager.consultaBloqueoProcesoTramite(smap1.get("ntramite"), TipoProcesoBloqueo.AUTORIZAR_NOMPLAN_SUPERVISOR.getClaveProceso());
	        if(datosBloq != null && !datosBloq.isEmpty()){
	            if(datosBloq.containsKey("VALOR") && "B".equalsIgnoreCase(datosBloq.get("VALOR"))){
	                smap1.put("ESPERA_AUT_NOMPLAN_SUPERV", "S");
	            }
	        }
        }
        catch(Exception ex)
        {
            long timestamp=System.currentTimeMillis();
            logger.error("Error al cargar datos de aprobacion de cambio de nombre de plan."+timestamp,ex);
            exito           = false;
            respuesta       = "Error inesperado #"+timestamp;
            respuestaOculta = ex.getMessage();
        }
		
		logger.debug(""
				+ "\n###### cargarDatosCotizacionGrupo ######"
				+ "\n########################################"
				);
		return SUCCESS;
	}
	
	public String cargarDatosCotizacionGrupo2()
	{
		logger.debug(""
				+ "\n#########################################"
				+ "\n###### cargarDatosCotizacionGrupo2 ######"
				+ "\n###### smap1 "+smap1
				);
		
		exito   = true;
		success = true;
		
		String cdunieco = null;
		String cdramo   = null;
		String cdtipsit = null;
		String estado   = null;
		String nmpoliza = null;
		String ntramite = null;
		
		//datos completos
		try
		{
			if(smap1==null)
			{
				throw new ApplicationException("No se recibieron datos");
			}
			cdunieco = smap1.get("cdunieco");
			cdramo   = smap1.get("cdramo");
			cdtipsit = smap1.get("cdtipsit");
			estado   = smap1.get("estado");
			nmpoliza = smap1.get("nmpoliza");
			ntramite = smap1.get("ntramite");
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
				throw new ApplicationException("No se recibio la situacion");
			}
			if(StringUtils.isBlank(estado))
			{
				throw new ApplicationException("No se recibio el estado");
			}
			if(StringUtils.isBlank(nmpoliza))
			{
				throw new ApplicationException("No se recibio el numero de cotizacion");
			}
			if(StringUtils.isBlank(ntramite))
			{
				throw new ApplicationException("No se recibio el numero de tramite");
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
		catch(Exception ex)
		{
			long timestamp  = System.currentTimeMillis();
			exito           = false;
			respuesta       = new StringBuilder("Error al validar datos para cargar cotizacion #").append(timestamp).toString();
			respuestaOculta = ex.getMessage();
			logger.error(respuesta,ex);
		}
		
		//proceso
		if(exito)
		{
			ManagerRespuestaSmapVO resp=cotizacionManager.cargarDatosCotizacionGrupo2(
					cdunieco
					,cdramo
					,cdtipsit
					,estado
					,nmpoliza
					,ntramite);
			exito           = resp.isExito();
			respuesta       = resp.getRespuesta();
			respuestaOculta = resp.getRespuestaOculta();
			if(exito)
			{
				params = resp.getSmap();
			}
		}
		
		logger.debug(""
				+ "\n###### cargarDatosCotizacionGrupo2 ######"
				+ "\n#########################################"
				);
		return SUCCESS;
	}
	
	public String cargarDatosCotizacionGrupoEndoso()
	{
		logger.debug(""
				+ "\n##############################################"
				+ "\n###### cargarDatosCotizacionGrupoEndoso ######"
				+ "\nsmap1 "+smap1
				);
		success = true;
		try
		{
			params=cotizacionManager.cargarDatosCotizacionGrupoEndoso(
					smap1.get("cdunieco"), smap1.get("cdramo"),
					smap1.get("cdtipsit"), smap1.get("estado"),
					smap1.get("nmpoliza"), smap1.get("ntramite"));
			
			logger.debug("Valor de los params ",params);
		    respuesta       = "Todo OK";
		    respuestaOculta = "Todo OK";
		    exito           = true;
		}
		catch(Exception ex)
		{
			long timestamp=System.currentTimeMillis();
			logger.error("Error al cargar datos de cotizacion grupo "+timestamp,ex);
			exito           = false;
			respuesta       = "Error inesperado #"+timestamp;
			respuestaOculta = ex.getMessage();
		}
		logger.debug(""
				+ "\n###### cargarDatosCotizacionGrupoEndoso ######"
				+ "\n##############################################"
				);
		return SUCCESS;
	}
	
	public String cargarGruposCotizacion2()
	{
		logger.debug(
				new StringBuilder()
				.append("\n#####################################")
				.append("\n###### cargarGruposCotizacion2 ######")
				.append("\n###### smap1=").append(smap1)
				.toString()
				);
		
		exito   = true;
		success = true;
		
		String cdunieco = null;
		String cdramo   = null;
		String estado   = null;
		String nmpoliza = null;
		
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
				throw new ApplicationException("No se recibio la poliza");
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
		catch(Exception ex)
		{
			long timestamp  = System.currentTimeMillis();
			exito           = false;
			respuesta       = new StringBuilder("Error al validar datos para cargar grupos #").append(timestamp).toString();
			respuestaOculta = ex.getMessage();
			logger.error(respuesta,ex);
		}
		
		//proceso
		if(exito)
		{
			ManagerRespuestaSlistVO resp = cotizacionManager.cargarGruposCotizacion2(cdunieco,cdramo,estado,nmpoliza);
			exito           = resp.isExito();
			respuesta       = resp.getRespuesta();
			respuestaOculta = resp.getRespuestaOculta();
			if(exito)
			{
				slist1 = resp.getSlist();
			}
		}
		
		logger.debug(
				new StringBuilder()
				.append("\n###### cargarGruposCotizacion2 ######")
				.append("\n#####################################")
				.toString()
				);
		return SUCCESS;
	}

	public String cargarGruposCotizacionReexpedicion()
	{
		logger.debug(
				new StringBuilder()
				.append("\n################################################")
				.append("\n###### cargarGruposCotizacionReexpedicion ######")
				.append("\n###### smap1=").append(smap1)
				.toString()
				);
		
		exito   = true;
		success = true;
		
		String cdunieco = null;
		String cdramo   = null;
		String estado   = null;
		String nmpoliza = null;
		
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
				throw new ApplicationException("No se recibio la poliza");
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
		catch(Exception ex)
		{
			long timestamp  = System.currentTimeMillis();
			exito           = false;
			respuesta       = new StringBuilder("Error al validar datos para cargar grupos #").append(timestamp).toString();
			respuestaOculta = ex.getMessage();
			logger.error(respuesta,ex);
		}
		
		//proceso
		if(exito)
		{
			ManagerRespuestaSlistVO resp = cotizacionManager.cargarGruposCotizacionReexpedicion(cdunieco,cdramo,estado,nmpoliza);
			exito           = resp.isExito();
			respuesta       = resp.getRespuesta();
			respuestaOculta = resp.getRespuestaOculta();
			if(exito)
			{
				slist1 = resp.getSlist();
			}
		}
		
		logger.debug(
				new StringBuilder()
				.append("\n###### cargarGruposCotizacionReexpedicion ######")
				.append("\n################################################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String cargarGruposCotizacion()
	{
		logger.debug(""
				+ "\n####################################"
				+ "\n###### cargarGruposCotizacion ######"
				+ "\n smap1: "+smap1
				);
		success = true;
		try
		{
			slist1=cotizacionManager.cargarGruposCotizacion(smap1.get("cdunieco"),smap1.get("cdramo"),smap1.get("estado"),smap1.get("nmpoliza"));
			exito           = true;
			respuesta       = "Todo OK";
			respuestaOculta = "Todo OK";
		}
		catch(Exception ex)
		{
			long timestamp=System.currentTimeMillis();
			logger.error("error al cargar grupos de cotizacion "+timestamp,ex);
			exito           = false;
			respuesta       = "Error inesperado #"+timestamp;
			respuestaOculta = ex.getMessage();
		}
		logger.debug(""
				+ "\n###### cargarGruposCotizacion ######"
				+ "\n####################################"
				);
		return SUCCESS;
	}
	
	public String cargarDatosGrupoLinea()
	{
		logger.debug(""
				+ "\n###################################"
				+ "\n###### cargarDatosGrupoLinea ######"
				+ "\n smap1: "+smap1
				);
		success = true;
		try
		{
			params=cotizacionManager.cargarDatosGrupoLinea(
					smap1.get("cdunieco")
					,smap1.get("cdramo")
					,smap1.get("estado")
					,smap1.get("nmpoliza")
					,smap1.get("letra")
					);
			exito           = true;
			respuesta       = "Todo OK";
			respuestaOculta = "Todo OK";
		}
		catch(Exception ex)
		{
			long timestamp=System.currentTimeMillis();
			logger.error("error al obtener datos de grupo de linea "+timestamp,ex);
			exito           = false;
			respuesta       = "Error inesperado #"+timestamp;
			respuestaOculta = ex.getMessage();
		}
		logger.debug(""
				+ "\n###### cargarDatosGrupoLinea ######"
				+ "\n###################################"
				);
		return SUCCESS;
	}
	
	public String cargarTvalogarsGrupo()
	{
		logger.debug(""
				+ "\n##################################"
				+ "\n###### cargarTvalogarsGrupo ######"
				+ "\nsmap1: "+smap1
				);
		success = true;
		try
		{
			slist1 = cotizacionManager.cargarTvalogarsGrupo(
					smap1.get("cdunieco")
					,smap1.get("cdramo")
					,smap1.get("estado")
					,smap1.get("nmpoliza")
					,smap1.get("letra"));
			exito           = true;
			respuesta       = "Todo OK";
			respuestaOculta = "Todo OK";
		}
		catch(Exception ex)
		{
			long timestamp=System.currentTimeMillis();
			logger.error("error al obtener tvalogars grupo #"+timestamp,ex);
			exito           = false;
			respuesta       = "Error inesperado #"+timestamp;
			respuestaOculta = ex.getMessage();
		}
		logger.debug(""
				+ "\n###### cargarTvalogarsGrupo ######"
				+ "\n##################################"
				);
		return SUCCESS;
	}
	
	public String cargarTarifasPorEdad()
	{
		logger.debug(""
				+ "\n##################################"
				+ "\n###### cargarTarifasPorEdad ######"
				+ "\nsmap1: "+smap1
				);
		success = true;
		try
		{
			slist1=cotizacionManager.cargarTarifasPorEdad(
					smap1.get("cdunieco")
					,smap1.get("cdramo")
					,smap1.get("estado")
					,smap1.get("nmpoliza")
					,smap1.get("nmsuplem")
					,smap1.get("cdplan")
					,smap1.get("cdgrupo")
					,smap1.get("cdperpag"));
			exito           = true;
			respuesta       = "Todo OK";
			respuestaOculta = "Todo OK";
		}
		catch(Exception ex)
		{
			long timestamp=System.currentTimeMillis();
			logger.error("Error al obtener tarifas por edad #"+timestamp,ex);
			exito           = false;
			respuesta       = "Error inesperado #"+timestamp;
			respuestaOculta = ex.getMessage();
		}
		logger.debug(""
				+ "\n###### cargarTarifasPorEdad ######"
				+ "\n##################################"
				);
		return SUCCESS;
	}
	
	public String cargarTarifasPorCobertura()
	{
		logger.debug(""
				+ "\n#######################################"
				+ "\n###### cargarTarifasPorCobertura ######"
				+ "\nsmap1: "+smap1
				);
		success = true;
		try
		{
			slist1=cotizacionManager.cargarTarifasPorCobertura(
					smap1.get("cdunieco")
					,smap1.get("cdramo")
					,smap1.get("estado")
					,smap1.get("nmpoliza")
					,smap1.get("nmsuplem")
					,smap1.get("cdplan")
					,smap1.get("cdgrupo")
					,smap1.get("cdperpag"));
			exito           = true;
			respuesta       = "Todo OK";
			respuestaOculta = "Todo OK";
		}
		catch(Exception ex)
		{
			long timestamp=System.currentTimeMillis();
			logger.error("Error al obtener tarifas por cobertura #"+timestamp,ex);
			exito           = false;
			respuesta       = "Error inesperado #"+timestamp;
			respuestaOculta = ex.getMessage();
		}
		logger.debug(""
				+ "\n###### cargarTarifasPorCobertura ######"
				+ "\n#######################################"
				);
		return SUCCESS;
	}
		
	/** 
	 *************cargarAseguradosExtraprimas****************
	 ********************************************************
	 **Carga los valores de asegurados y 
	 **la informacion a nivel de situacion 
	 **como lo son las extraprimas, ademas  
	 **de agruparlos por familia para el
	 **ramo multialud colectivo (MSC)
	 * @return
	 */
	public String cargarAseguradosExtraprimas()
	{
		logger.debug(""
				+ "\n#########################################"
				+ "\n###### cargarAseguradosExtraprimas ######"
				+ "\nsmap1: "+smap1
				);
		success = true;
		exito   = true;
		
		if(exito)
		{
			try
			{
				Utils.validateSession(session);				
				Utils.validate(smap1 , "No se recibieron datos");				
				String cdunieco  = null
						,cdramo   = null
						,estado   = null
						,nmpoliza = null
						,nmsuplem = null
						,cdgrupo  = null;
				cdunieco = smap1.get("cdunieco");
				cdramo   = smap1.get("cdramo");
				estado   = smap1.get("estado");
				nmpoliza = smap1.get("nmpoliza");
				nmsuplem = smap1.get("nmsuplem");
				cdgrupo  = smap1.get("cdgrupo");
				
				Utils.validate(
						cdunieco  , "No se recibio la sucursal"
						,cdramo   , "No se recibio el producto"
						,estado   , "No se recibio el estado"
						,nmpoliza , "No se recibio el numero de cotizacion"
						,nmpoliza , "No se recibio el numero de cotizacion"
						,nmpoliza , "No se recibio el numero de cotizacion"
						,nmsuplem , "No se recibio el suplemento"
						,cdgrupo  , "No se recibio la clave de grupo"
				);		
				smap1.put("start", start);
				smap1.put("limit", limit);
			    slist1 = cotizacionManager.cargarAseguradosExtraprimas(
			    		cdunieco
			    		,cdramo
			    		,estado
			    		,nmpoliza
			    		,nmsuplem
			    		,cdgrupo
			    		,start
			    		,limit
			    		);
			    for(Map<String,String>iAsegurado:slist1){
			    	String tpl = null;
			    	if(StringUtils.isBlank(iAsegurado.get("TITULAR"))){
			    		tpl = "Asegurados";
			    	}
			    	else
			    	{
			    		tpl = new StringBuilder()
	    	                    .append("Familia (")
	    	                    .append(iAsegurado.get("FAMILIA"))
	    	                    .append(") de ")
	    	                    .append(iAsegurado.get("TITULAR"))
	    	            		.toString();
			    	}
			    	iAsegurado.put("AGRUPADOR",
			    			new StringBuilder()
			    	            .append(StringUtils.leftPad(iAsegurado.get("FAMILIA"),3,"0"))
			    	            .append("_")
			    	            .append(tpl)
			    	            .toString());
			    }
			    Map<String,String> total = slist1.remove(slist1.size()-1);
				this.total = total.get("total");
			    logger.debug(Utils.log(
						 "\n##########################################"
						,"\n###### start=" , start
						,"\n###### limit=" , limit
						,"\n###### total=" , total.get("total")
						));			
				exito   = true;
				success = true;
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = "Error al cargar extraprimas #"+timestamp;
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		logger.debug(""
				+ "\n###### cargarAseguradosExtraprimas ######"
				+ "\n#########################################"
				);
		return SUCCESS;
	}
	
	/**
	 ****************************************************
	 ************cargarAseguradosExtraprimas2************
	 ****************************************************
	 **Paginado de la asegurados que muestra 
	 **los valores por situacion como extraprimas,
	 **ademas de agruparlos por familia.
	 ****************************************************
	 * @return
	 */
	public String cargarAseguradosExtraprimas2()
	{
		logger.debug(Utils.log(
				 "\n##########################################"
				,"\n###### cargarAseguradosExtraprimas2 ######"
				,"\n###### smap1=" , smap1
				));
		
		
		//datos completos
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(smap1 , "No se recibieron datos");
			
			String cdunieco  = null
					,cdramo   = null
					,estado   = null
					,nmpoliza = null
					,nmsuplem = null
					,cdgrupo  = null;
			
			cdunieco = smap1.get("cdunieco");
			cdramo   = smap1.get("cdramo");
			estado   = smap1.get("estado");
			nmpoliza = smap1.get("nmpoliza");
			nmsuplem = smap1.get("nmsuplem");
			cdgrupo  = smap1.get("cdgrupo");
			
			Utils.validate(
					cdunieco  , "No se recibio la sucursal"
					,cdramo   , "No se recibio el producto"
					,estado   , "No se recibio el estado"
					,nmpoliza , "No se recibio el numero de cotizacion"
					,nmsuplem , "No se recibio el suplemento"
					,cdgrupo  , "No se recibio la clave de grupo"
			);		
			smap1.put("start", start);
			smap1.put("limit", limit);				
			
		    slist1 = cotizacionManager.cargarAseguradosExtraprimas2(
		    		cdunieco
		    		,cdramo
		    		,estado
		    		,nmpoliza
		    		,nmsuplem
		    		,cdgrupo
		    		,start
		    		,limit
		    		);
		    Map<String,String> total = slist1.remove(slist1.size()-1);
			this.total = total.get("total");
		    logger.debug(Utils.log(
					 "\n##########################################"
					,"\n###### start=" , start
					,"\n###### limit=" , limit
					,"\n###### total=" , total.get("total")
					));			
			exito   = true;
			success = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### respuesta=" , respuesta
				,"\n###### exito="     , exito
				,"\n###### slist1="    , slist1
				,"\n###### cargarAseguradosExtraprimas2 ######"
				,"\n##########################################"
				));
		return SUCCESS;
	}
	
	/**
	 ****************************************************
	 ************cargarAseguradosExtraprimas3************
	 ****************************************************
	 **Paginado de la asegurados que muestra 
	 **los valores por situacion como extraprimas,
	 **ademas de agruparlos por familia.
	 ****************************************************
	 * @return
	 */
	public String cargarAseguradosExtraprimas3()
	{
		logger.debug(Utils.log(
				 "\n##########################################"
				,"\n###### cargarAseguradosExtraprimas3 ######"
				,"\n###### smap1=" , smap1
				));
		
		
		//datos completos
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(smap1 , "No se recibieron datos");
			
			String cdunieco  = null
					,cdramo   = null
					,estado   = null
					,nmpoliza = null
					,nmsuplem = null
					,cdgrupo  = null;
			
			cdunieco = smap1.get("cdunieco");
			cdramo   = smap1.get("cdramo");
			estado   = smap1.get("estado");
			nmpoliza = smap1.get("nmpoliza");
			nmsuplem = smap1.get("nmsuplem");
			cdgrupo  = smap1.get("cdgrupo");
			
			Utils.validate(
					cdunieco  , "No se recibio la sucursal"
					,cdramo   , "No se recibio el producto"
					,estado   , "No se recibio el estado"
					,nmpoliza , "No se recibio el numero de cotizacion"
					,nmsuplem , "No se recibio el suplemento"
					,cdgrupo  , "No se recibio la clave de grupo"
			);			
			
		    slist1 = cotizacionManager.cargarAseguradosExtraprimas(
		    		cdunieco
		    		,cdramo
		    		,estado
		    		,nmpoliza
		    		,nmsuplem
		    		,cdgrupo
		    		);		
			exito   = true;
			success = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### respuesta=" , respuesta
				,"\n###### exito="     , exito
				,"\n###### slist1="    , slist1
				,"\n###### cargarAseguradosExtraprimas3 ######"
				,"\n##########################################"
				));
		return SUCCESS;
	}
	
	public String guardarExtraprimasAseguradosEnd()
	{
		logger.debug(Utils.log(
				 "\n##########################################"
				,"\n###### guardarExtraprimasAseguradosEnd ######"
				,"\n###### slist1: "+slist1
				));
		
		try
		{
			for(Map<String,String>iAsegurado:slist1)
			{
				cotizacionManager.guardarExtraprimaAsegurado(
						iAsegurado.get("cdunieco")
						,iAsegurado.get("cdramo")
						,iAsegurado.get("estado")
						,iAsegurado.get("nmpoliza")
						,iAsegurado.get("nmsuplem")
						,iAsegurado.get("nmsituac")
						,iAsegurado.get("cdtipsit")
						,iAsegurado.get("ocupacion")
						,iAsegurado.get("extpri_ocupacion")
						,iAsegurado.get("peso")
						,iAsegurado.get("estatura")
						,iAsegurado.get("extpri_estatura")
						,iAsegurado.get("cdgrupo")
						);
			}
			
			respuesta = "Se guardaron todos los datos";
			success   = true;
			exito     = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### exito="   , exito
				,"\n###### success=" , success
				,"\n###### guardarExtraprimasAseguradosEnd ######"
				,"\n##########################################"
				));
		
		return SUCCESS;
	}
	
	/**
	 ****************************************************
	 ************guardarExtraprimasAsegurados************
	 ****************************************************
	 **Guarda los cambios en la pagina de asegurados
	 **agrupados de una sola vez
	 * @return
	 */
	public String guardarExtraprimasAsegurados()
	{
		logger.debug(Utils.log(
				 "\n##########################################"
				,"\n###### guardarExtraprimasAsegurados ######"
				,"\n###### cdunieco: "+params.get("cdunieco")
				,"\n###### cdramo: "+params.get("cdramo")
				,"\n###### estado: "+params.get("estado")
				,"\n###### nmpoliza: "+params.get("nmpoliza")
				,"\n###### nmsuplem: "+params.get("nmsuplem")
				,"\n###### cdtipsit: "+params.get("cdtipsit")
				,"\n###### guardarExt: "+params.get("guardarExt")
				,"\n###### slist1: "+slist1
				));
		
		try
		{
			String cdunieco = params.get("cdunieco");
			String cdramo   = params.get("cdramo");
			String estado   = params.get("estado");
			String nmpoliza = params.get("nmpoliza");
			String nmsuplem = params.get("nmsuplem");
			String cdtipsit = params.get("cdtipsit");
			Utils.validate(cdunieco, "no se recibio oficina");
			Utils.validate(cdramo, "no se recibio ramo");
			Utils.validate(estado, "no se recibio estado");
			Utils.validate(nmpoliza, "no se recibio poliza");
			Utils.validate(cdtipsit, "no se recibio tipo de situacion");
			Utils.validate(slist1,"no se recibieron datos para guardar");
			cotizacionManager.guardarExtraprimaAsegurado(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplem
					,cdtipsit
					,slist1
					);			
			respuesta = "Se guardaron todos los datos";
			success   = true;
			exito     = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### exito="   , exito
				,"\n###### success=" , success
				,"\n###### guardarExtraprimasAsegurados ######"
				,"\n##########################################"
				));
		
		return SUCCESS;
	}
	
	public String ejecutaSigsvalipol()
	{
		logger.debug(""
				+ "\n################################"
				+ "\n###### ejecutaSigsvalipol ######"
				+ "\nsmap1: "+smap1
				);
		exito   = true;
		success = true;
		if(exito)
		{
			try
			{
				UserVO usuario  = (UserVO)session.get("USUARIO");
				String cdusuari = usuario.getUser();
				String cdelemen = usuario.getEmpresa().getElementoId();
				Map<String,String> mapaTarificacion=new HashMap<String,String>(0);
	            mapaTarificacion.put("pv_cdusuari_i" , cdusuari);
	            mapaTarificacion.put("pv_cdelemen_i" , cdelemen);
	            mapaTarificacion.put("pv_cdunieco_i" , smap1.get("cdunieco"));
	            mapaTarificacion.put("pv_cdramo_i"   , smap1.get("cdramo"));
	            mapaTarificacion.put("pv_estado_i"   , smap1.get("estado"));
	            mapaTarificacion.put("pv_nmpoliza_i" , smap1.get("nmpoliza"));
	            mapaTarificacion.put("pv_nmsituac_i" , smap1.get("nmsituac"));
	            mapaTarificacion.put("pv_nmsuplem_i" , smap1.get("nmsuplem"));
	            mapaTarificacion.put("pv_cdtipsit_i" , smap1.get("cdtipsit"));
	            kernelManager.ejecutaASIGSVALIPOL_EMI(mapaTarificacion);
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = "Error al tarificar #"+timestamp;
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		logger.debug(""
				+ "\n###### ejecutaSigsvalipol ######"
				+ "\n################################"
				);
		return SUCCESS;
	}
	
	public String cargarAseguradosGrupo()
	{
		logger.debug(""
				+ "\n###################################"
				+ "\n###### cargarAseguradosGrupo ######"
				+ "\nsmap1 "+smap1
				);
		success = true;
		exito   = true;
		if(exito)
		{
			try
			{
				slist1=cotizacionManager.cargarAseguradosGrupo(
						smap1.get("cdunieco")
						,smap1.get("cdramo")
						,smap1.get("estado")
						,smap1.get("nmpoliza")
						,smap1.get("nmsuplem")
						,smap1.get("cdgrupo")
						);
				for(Map<String,String>iAsegurado:slist1)
			    {
			    	String tpl = null;
			    	if(StringUtils.isBlank(iAsegurado.get("TITULAR")))
			    	{
			    		tpl = "Asegurados";
			    	}
			    	else
			    	{
			    		tpl = new StringBuilder()
			    		        .append("Familia (")
			    		        .append(iAsegurado.get("FAMILIA"))
			    		        .append(") de ")
    			    		    .append(iAsegurado.get("TITULAR"))
			    		        .toString();
			    	}
			    	iAsegurado.put("AGRUPADOR",
			    			new StringBuilder()
			    	            .append(StringUtils.leftPad(iAsegurado.get("FAMILIA"),3,"0"))
			    	            .append("_")
			    	            .append(tpl)
			    	            .toString());
			    }
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = "Error al cargar los asegurados del grupo #"+timestamp;
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		logger.debug(""
				+ "\n###### cargarAseguradosGrupo ######"
				+ "\n###################################"
				);
		return SUCCESS;
	}
	
	public String cargarAseguradosGrupoPag()
	{
		logger.debug(""
				+ "\n###################################"
				+ "\n###### cargarAseguradosGrupoPag ######"
				+ "\nsmap1 "+smap1
				);
		success = true;
		exito   = true;
		if(exito)
		{
			try
			{
				slist1=cotizacionManager.cargarAseguradosGrupo(
						smap1.get("cdunieco")
						,smap1.get("cdramo")
						,smap1.get("estado")
						,smap1.get("nmpoliza")
						,smap1.get("nmsuplem")
						,smap1.get("cdgrupo")
						,start
						,limit
						);
				Map<String,String> total = slist1.remove(slist1.size()-1);
				this.total = total.get("total");
				for(Map<String,String>iAsegurado:slist1)
			    {
			    	String tpl = null;
			    	if(StringUtils.isBlank(iAsegurado.get("TITULAR")))
			    	{
			    		tpl = "Asegurados";
			    	}
			    	else
			    	{
			    		tpl = new StringBuilder()
			    		        .append("Familia (")
			    		        .append(iAsegurado.get("FAMILIA"))
			    		        .append(") de ")
    			    		    .append(iAsegurado.get("TITULAR"))
			    		        .toString();
			    	}
			    	iAsegurado.put("AGRUPADOR",
			    			new StringBuilder()
			    	            .append(StringUtils.leftPad(iAsegurado.get("FAMILIA"),3,"0"))
			    	            .append("_")
			    	            .append(tpl)
			    	            .toString());
			    }
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = "Error al cargar los asegurados del grupo #"+timestamp;
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		logger.debug(""
				+ "\n###### cargarAseguradosGrupoPag ######"
				+ "\n###################################"
				);
		return SUCCESS;
	}
	
	public String guardarAseguradosCotizacion()
	{
		this.session=ActionContext.getContext().getSession();
		logger.debug(""
				+ "\n#########################################"
				+ "\n###### guardarAseguradosCotizacion ######"
				+ "\nsmap1 "+smap1
				+ "\nslist1 "+slist1
				);
		success = true;
		exito   = true;
		
		if(exito)
		{
			try
			{
				String cdunieco = smap1.get("cdunieco");
				String cdramo   = smap1.get("cdramo");
				String estado   = smap1.get("estado");
				String nmpoliza = smap1.get("nmpoliza");
				String cdgrupo  = smap1.get("cdgrupo");
				
				cotizacionManager.borrarMpoliperGrupo(cdunieco,cdramo,estado,nmpoliza,cdgrupo);
				
				for(Map<String,String>aseg:slist1)
				{
					Map<String,Object> parametros=new LinkedHashMap<String,Object>(0);
					parametros=new LinkedHashMap<String,Object>(0);
					parametros.put("pv_cdunieco_i",	cdunieco);
					parametros.put("pv_cdramo_i",	cdramo);
					parametros.put("pv_estado_i",	estado);
					parametros.put("pv_nmpoliza_i",	nmpoliza);
					parametros.put("pv_nmsituac_i",	(String)aseg.get("nmsituac"));
					parametros.put("pv_cdrol_i", 	(String)aseg.get("cdrol"));
					parametros.put("pv_cdperson_i",	(String)aseg.get("cdperson"));
					parametros.put("pv_nmsuplem_i",	"0");
					parametros.put("pv_status_i",	"V");
					parametros.put("pv_nmorddom_i",	"1");
					parametros.put("pv_swreclam_i",	null);
					parametros.put("pv_accion_i",	"I");
					parametros.put("pv_swexiper_i", (String)aseg.get("swexiper"));
					kernelManager.movMpoliper(parametros);
				}
				
				respuesta       = "Se han guardado los asegurados";
				respuestaOculta = "Todo OK";
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = "Error al guardar las personas #"+timestamp;
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		logger.debug(""
				+ "\n###### guardarAseguradosCotizacion ######"
				+ "\n#########################################"
				);
		return SUCCESS;
	}
	
	public String guardarReporteCotizacionGrupo()
	{
		logger.debug(Utils.log(
				 "\n###########################################"
				,"\n###### guardarReporteCotizacionGrupo ######"
				,"\n###### smap1=", smap1
				));
		
		try
		{
			UserVO usuario = Utils.validateSession(session);
			
			String cdusuari  = usuario.getUser()
			       ,cdunieco = smap1.get("cdunieco")
			       ,cdramo   = smap1.get("cdramo")
			       ,estado   = smap1.get("estado")
			       ,nmpoliza = smap1.get("nmpoliza")
			       ,cdperpag = smap1.get("cdperpag")
			       ,cdtipsit = smap1.get("cdtipsit")
			       ,ntramite = smap1.get("ntramite")
			       ,nGrupos  = smap1.get("nGrupos")
			       ,status   = smap1.get("status")
			       ,caseIdRstn = smap1.get("caseIdRstn");
			
			Utils.validate(
					cdunieco  , "Falta cdunieco"
					,cdramo   , "Falta cdramo"
					,estado   , "Falta estado"
					,nmpoliza , "Falta nmpoliza"
					,cdperpag , "Falta cdperpag"
					,cdtipsit , "Falta cdtipsit"
					,ntramite , "Falta ntramite"
					,nGrupos  , "Falta nGrupos"
					,status   , "Falta status"
					);
			
			if(Ramo.GASTOS_MEDICOS_MAYORES_PRUEBA.getCdramo().equals(cdramo) || cotizacionManager.isEstatusGeneraDocumentosCotizacion(status))
			{
			
				int bloqueos = 0;
				do
				{
					logger.debug("Recuperando conteo de tbloqueo de primer ciclo");
					ManagerRespuestaSmapVO resp = recuperacionSimpleManager.recuperacionSimple(
							RecuperacionSimple.RECUPERAR_CONTEO_BLOQUEO
							,smap1
							,usuario.getRolActivo().getClave()
							,cdusuari
							);
					
					bloqueos = Integer.parseInt(resp.getSmap().get("CONTEO"));
					
					logger.debug(Utils.log("Conteo recuperado=",bloqueos));
					
					logger.debug("Esperando 30 segundos del primer ciclo...");
					Thread.sleep(1000l*30l);
					
				}
				while(bloqueos>0);
				
				do
				{
					logger.debug("Recuperando conteo de tbloqueo de segundo ciclo");
					ManagerRespuestaSmapVO resp = recuperacionSimpleManager.recuperacionSimple(
							RecuperacionSimple.RECUPERAR_CONTEO_BLOQUEO
							,smap1
							,usuario.getRolActivo().getClave()
							,cdusuari
							);
					
					bloqueos = Integer.parseInt(resp.getSmap().get("CONTEO"));
					
					logger.debug(Utils.log("Conteo recuperado=",bloqueos));
					
					logger.debug("Esperando 30 segundos de segundo ciclo...");
					Thread.sleep(1000l*30l);
					
				}
				while(bloqueos>0);
				
				logger.debug(Utils.log("","Se termino el bloqueo"));
				
				try {
                    /**
                     * Elimina los documentos de cotizacion para cuando afectan datos de las caratulas no se visualicen documentos anteriores
                     */
					cotizacionManager.eliminaDocsCotiza(cdunieco, cdramo, Constantes.POLIZA_WORKING, nmpoliza, ntramite);
                    
                } catch (Exception e) {
                    logger.warn("No se eliminaron los documentos de cotizacion de la poliza: {}", e);
                }
				
				String urlReporteCotizacion=Utils.join(
						  rutaServidorReports
						, "?p_unieco="      , cdunieco
						, "&p_ramo="        , cdramo
						, "&p_estado="      , estado
						, "&p_poliza="      , nmpoliza
						, "&p_cdperpag="    , cdperpag
						, "&p_perpag="      , cdperpag
						, "&p_usuari="      , cdusuari
						, "&p_suplem=0"
						, "&p_cdplan="
	                    , "&destype=cache"
	                    , "&desformat=PDF"
	                    , "&userid="        , passServidorReports
	                    , "&ACCESSIBLE=YES"
	                    , "&report="        , getText("rdf.cotizacion.nombre."+cdtipsit)
	                    , "&paramform=no"
	                    );
				
				String nombreArchivoCotizacion = Utils.join("cotizacion_",nmpoliza,".pdf")
				       ,pathArchivoCotizacion  = Utils.join(
				    		   rutaDocumentosPoliza
				    		   ,"/" , ntramite
				    		   ,"/" , nombreArchivoCotizacion
				    		   );
				
				logger.debug(Utils.log("Se va a ejecutar el reporte=",urlReporteCotizacion));
				
				HttpUtil.generaArchivo(urlReporteCotizacion, pathArchivoCotizacion);
				
				documentosManager.guardarDocumento(
						cdunieco
						,cdramo
						,estado
						,"0"
						,"0"
						,new Date()
						,nombreArchivoCotizacion
						,Utils.join("COTIZACI\u00d3N EN RESUMEN (",nmpoliza,")")
						,nmpoliza
						,ntramite
						,"1"
						,null
						,null
						,TipoTramite.POLIZA_NUEVA.getCdtiptra()
						,null
						,null
						,null
						,null, false
						);
                
                if (Ramo.GASTOS_MEDICOS_MAYORES_PRUEBA.getCdramo().equals(cdramo)) {
                    HttpUtil.enviarArchivoRSTN(
                            HttpUtil.RSTN_DEFAULT_PATH + caseIdRstn, 
                            pathArchivoCotizacion, 
                            Utils.join("COTIZACION EN RESUMEN (",nmpoliza,")"),
                            HttpUtil.RSTN_DOC_CLASS_COTIZACION);
                }
				
				String urlReporteCotizacion2=Utils.join(
						  rutaServidorReports
						, "?p_unieco="      , cdunieco
						, "&p_ramo="        , cdramo
						, "&p_estado="      , estado
						, "&p_poliza="      , nmpoliza
						, "&p_cdperpag="    , cdperpag
						, "&p_usuari="      , cdusuari
						, "&p_suplem=0"
						, "&p_cdplan="
						, "&destype=cache"
						, "&desformat=PDF"
						, "&userid="        , passServidorReports
						, "&ACCESSIBLE=YES"
						, "&report="        , getText(Utils.join("rdf.cotizacion2.nombre.",cdtipsit))
						, "&paramform=no"
	                  );
				
				String nombreArchivoCotizacion2 = Utils.join("cotizacion2_",nmpoliza,".pdf")
				       ,pathArchivoCotizacion2  = Utils.join(
				    		   rutaDocumentosPoliza
				    		   ,"/" , ntramite
				    		   ,"/" , nombreArchivoCotizacion2
				    		   );
				
				logger.debug(Utils.log("Se va a ejecutar el reporte2=",urlReporteCotizacion2));
				
				HttpUtil.generaArchivo(urlReporteCotizacion2, pathArchivoCotizacion2);
				
				documentosManager.guardarDocumento(
						cdunieco
						,cdramo
						,estado
						,"0"
						,"0"
						,new Date()
						,nombreArchivoCotizacion2
						,Utils.join("COTIZACI\u00d3N A DETALLE (",nmpoliza,")")
						,nmpoliza
						,ntramite
						,"1"
						,null
						,null
						,TipoTramite.POLIZA_NUEVA.getCdtiptra()
						,null
						,null
						,null
						,null, false
						);
				
				if (Ramo.GASTOS_MEDICOS_MAYORES_PRUEBA.getCdramo().equals(cdramo)) {
				    HttpUtil.enviarArchivoRSTN(
				            HttpUtil.RSTN_DEFAULT_PATH + caseIdRstn, 
				            pathArchivoCotizacion2, 
				            Utils.join("COTIZACION A DETALLE (",nmpoliza,")"),
                            HttpUtil.RSTN_DOC_CLASS_COTIZACION);
				}
				
				// Documentos generados para el Ramo Multisalud excepto para el cdtipsit TMS:
				if (Ramo.MULTISALUD.getCdramo().equals(cdramo)
						&& !TipoSituacion.TRADICIONALES_MEGASALUD.getCdtipsit().equals(cdtipsit))
				{
					//pdf resumen
					String urlReporteResumenCotizacion=Utils.join(
							  rutaServidorReports
							, "?p_unieco="      , cdunieco
							, "&p_ramo="        , cdramo
							, "&p_estado="      , estado
							, "&p_poliza="      , nmpoliza
							, "&p_perpag="      , cdperpag
							, "&p_usuari="      , cdusuari
							, "&p_suplem=0"
		                    , "&destype=cache"
		                    , "&desformat=PDF"
		                    , "&userid="        , passServidorReports
		                    , "&ACCESSIBLE=YES"
		                    , "&report="        , getText(Utils.join("rdf.resumen.cotizacion.col.",cdramo))
		                    , "&paramform=no"
		                    );
					
					String nombreArchivoResumenCotizacion = Utils.join("resumen_cotizacion_col_",nmpoliza,".pdf")
					       ,pathArchivoResumenCotizacion  = Utils.join(
					    		   rutaDocumentosPoliza
					    		   ,"/" , ntramite
					    		   ,"/" , nombreArchivoResumenCotizacion
					    		   );
					
					HttpUtil.generaArchivo(urlReporteResumenCotizacion, pathArchivoResumenCotizacion);
					
					documentosManager.guardarDocumento(
							cdunieco
							,cdramo
							,estado
							,"0"
							,"0"
							,new Date()
							,nombreArchivoResumenCotizacion
							,Utils.join("RESUMEN DE COTIZACI\u00d3N (PDF) (",nmpoliza,")")
							,nmpoliza
							,ntramite
							,"1"
							,null
							,null
							,TipoTramite.POLIZA_NUEVA.getCdtiptra()
							,null
							,null
							,null
							,null, false
							);
					
					//exceles grupos
					for(int i=1 ; i<=Integer.parseInt(nGrupos) ; i++)
					{
						Map<String,String> paramsGrupo = new LinkedHashMap<String,String>();
						paramsGrupo.put("pv_cdunieco_i" , cdunieco);
						paramsGrupo.put("pv_cdramo_i"   , cdramo);
						paramsGrupo.put("pv_estado_i"   , estado);
						paramsGrupo.put("pv_nmpoliza_i" , nmpoliza);
						paramsGrupo.put("pv_nmsuplem_i" , "0");
						paramsGrupo.put("pv_cdperpag_i" , cdperpag);
						paramsGrupo.put("pv_cdgrupo_i"  , i+"");
						paramsGrupo.put("pv_cdusuari_i" , cdusuari);
						
						InputStream excelGrupo = reportesManager.obtenerDatosReporte(Reporte.SALUD_COLECTIVO_COTIZACION_GRUPO.getCdreporte()
								,cdusuari
								,paramsGrupo
								);
						
						String nombreCotGrupo = Utils.join("COTIZACION_GRUPO_",i,"_",nmpoliza,TipoArchivo.XLS.getExtension());
						
						FileUtils.copyInputStreamToFile(excelGrupo, new File(Utils.join(
										rutaDocumentosPoliza,"/",ntramite,"/",nombreCotGrupo
						)));
						
						documentosManager.guardarDocumento(
								cdunieco
								,cdramo
								,estado
								,"0"
								,"0"
								,new Date()
								,nombreCotGrupo
								,Utils.join("COTIZACI\u00d3N GRUPO ",i, " (",nmpoliza,")")
								,nmpoliza
								,ntramite
								,"1"
								,null
								,null
								,TipoTramite.POLIZA_NUEVA.getCdtiptra()
								,null
								,null
								,null
								,null, false
								);
					}
				}
				
			}
			
			exito = true;
			
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
			logger.error(respuesta,ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### exito=", exito
				,"\n###### guardarReporteCotizacionGrupo ######"
				,"\n###########################################"
				));
		
		return SUCCESS;
	}
	
	public String cargarCduniecoAgenteAuto () {
		logger.debug(Utils.log(
				"\n######################################",
				"\n###### cargarCduniecoAgenteAuto ######",
				"\n###### smap1 = ", smap1
		));
		try {
			
			Utils.validate(smap1, "No se recibieron datos");
			
			String cdagente  = smap1.get("cdagente"),
					cdtipram = smap1.get("cdtipram");
			
			if (StringUtils.isBlank(cdtipram)) {
				cdtipram = TipoRamo.AUTOS.getCdtipram();
			}
			
			smap1.put("cdunieco",cotizacionManager.cargarCduniecoAgenteAuto(cdagente, cdtipram));
			success = true;
			exito = true;
		} catch(Exception ex) {
			respuesta  = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
				"\n###### smap1 = ", smap1,
				"\n###### cargarCduniecoAgenteAuto ######",
				"\n######################################"
				));
		return SUCCESS;
	}
	
	public String cargarCdagentePorFolio()
	{
		logger.debug(new StringBuilder()
		      .append("\n####################################")
		      .append("\n###### cargarCdagentePorFolio ######")
		      .append("\nsmap1").append(smap1)
		      .toString()
		      );
		
		success = true;
		exito   = true;
		
		int folio    = -1;
		int cdunieco = -1;
		int cdramo   = -1;
		String cdsisrol = "";
		String cdusuari = "";
		int idusu    = 0;
		String cdtipsit = "";
		//checar datos
		if(exito)
		{
			
			try
			{
				UserVO usuario=(UserVO)session.get("USUARIO");
				
				folio    = Integer.valueOf(smap1.get("folio"));
				cdunieco = Integer.valueOf(smap1.get("cdunieco"));
				cdramo   = Integer.valueOf(smap1.get("cdramo"));
				cdsisrol = usuario.getRolActivo().getClave();
				cdusuari = usuario.getUser();
				cdtipsit = String.valueOf(smap1.get("cdtipsit"));
				
				if(StringUtils.isNotBlank(smap1.get("idusu")))
				{
					idusu    = Integer.valueOf(smap1.get("idusu"));
				}
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = new StringBuilder().append("Datos incompletos #").append(timestamp).toString();
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		//recuperar cdagente
		if(exito)
		{
			try
			{
				EmAdmfolId agente = agentePorFolioService.obtieneAgentePorFolioSucursal(folio,cdunieco,cdramo,cdsisrol,cdusuari,idusu,cdtipsit);
				if(agente==null)
				{
					throw new ApplicationException("No existe el agente");
				}
				smap1.put("cdagente",String.valueOf(agente.getNumAge()));
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = new StringBuilder().append("Este folio no pertenece a un agente #").append(timestamp).toString();
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		logger.debug(new StringBuilder()
		      .append("\nsmap1").append(smap1)
		      .append("\n###### cargarCdagentePorFolio ######")
		      .append("\n####################################")
		      .toString()
		      );
		return SUCCESS;
	}
	
	public String obtenerParametrosCotizacion()
	{
		logger.debug(
				new StringBuilder()
				.append("\n#########################################")
				.append("\n###### obtenerParametrosCotizacion ######")
				.append("\n###### smap1=").append(smap1)
				.toString()
				);
		
		success = true;
		exito   = true;
		
		ParametroCotizacion parametro  = null;
		String              sParametro = null;
		String              cdramo     = null;
		String              cdtipsit   = null;
		String              clave4     = null;
		String              clave5     = null;
		
		//datos completos
		if(exito)
		{
			try
			{
				sParametro = smap1.get("parametro");
				cdramo     = smap1.get("cdramo");
				cdtipsit   = smap1.get("cdtipsit");
				clave4     = smap1.get("clave4");
				clave5     = smap1.get("clave5");
				if(StringUtils.isBlank(sParametro))
				{
					throw new ApplicationException("No se especifica el parametro");
				}
				if(StringUtils.isBlank(cdramo))
				{
					throw new ApplicationException("No se especifica el ramo");
				}
				if(StringUtils.isBlank(cdtipsit))
				{
					throw new ApplicationException("No se especifica la situacion");
				}
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = "Datos incompletos para obtener par\u00e1metros #"+timestamp;
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		//parametro existente
		if(exito)
		{
			try
			{
				parametro = ParametroCotizacion.valueOf(sParametro);
				if(parametro==null)
				{
					throw new ApplicationException("El parametro no se encuentra en el enum");
				}
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = new StringBuilder("Par\u00e1metro no definido #").append(timestamp).toString();
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		//obtener parametro
		if(exito)
		{
			try
			{
				ManagerRespuestaSmapVO resp=cotizacionManager.obtenerParametrosCotizacion(parametro, cdramo, cdtipsit, clave4, clave5);
				exito           = resp.isExito();
				respuesta       = resp.getRespuesta();
				respuestaOculta = resp.getRespuestaOculta();
				if(exito)
				{
				    smap1.putAll(resp.getSmap());
				}
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = "Error inesperado al obtener par\u00e1metros #"+timestamp;
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		logger.debug(
				new StringBuilder()
				.append("\n###### smap1=").append(smap1)
				.append("\n###### obtenerParametrosCotizacion ######")
				.append("\n#########################################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String cargarAutoPorClaveGS()
	{
		logger.debug(
				new StringBuilder()
				.append("\n##################################")
				.append("\n###### cargarAutoPorClaveGS ######")
				.append("\n###### smap1=").append(smap1)
				.toString()
				);
		
		success = true;
		exito   = true;
		
		String cdramo   = null;
		String clavegs  = null;
		String cdtipsit = null;
		String cdsisrol = null;
		String tipoUnidad = null;
		
		//datos
		try
		{
			if(session==null)
			{
				throw new ApplicationException("No hay sesion");
			}
			if(session.get("USUARIO")==null)
			{
				throw new ApplicationException("No hay usuario en la sesion");
			}
			UserVO usuario=(UserVO)session.get("USUARIO");
			cdsisrol=usuario.getRolActivo().getClave();
			if(StringUtils.isBlank(cdsisrol))
			{
				throw new ApplicationException("El usuario no tiene rol");
			}
			
			cdramo   = smap1.get("cdramo");
			clavegs  = smap1.get("clavegs");
			cdtipsit = smap1.get("cdtipsit");
			tipoUnidad = smap1.get("tipounidad");
			if(StringUtils.isBlank(cdramo))
			{
				throw new ApplicationException("No se recibio el ramo");
			}
			if(StringUtils.isBlank(clavegs))
			{
				throw new ApplicationException("No se recibio la clave gs");
			}
			if(StringUtils.isBlank(cdtipsit))
			{
				throw new ApplicationException("No se recibio la situacion");
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
		catch(Exception ex)
		{
			long timestamp  = System.currentTimeMillis();
			exito           = false;
			respuesta       = "Error al validar datos #"+timestamp;
			respuestaOculta = ex.getMessage();
			logger.error(respuesta,ex);
		}
		
		//obtener datos auto
		if(exito)
		{
			try
			{
				ManagerRespuestaSmapVO resp = cotizacionManager.cargarAutoPorClaveGS(cdramo,clavegs,cdtipsit,cdsisrol,tipoUnidad);
				
				exito           = resp.isExito();
				respuesta       = resp.getRespuesta();
				respuestaOculta = resp.getRespuestaOculta();
				
				if(exito)
				{
					smap1.putAll(resp.getSmap());
				}
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = "Error inesperado al obtener datos del auto #"+timestamp;
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		logger.debug(
				new StringBuilder()
				.append("\n###### smap1=").append(smap1)
				.append("\n###### cargarAutoPorClaveGS ######")
				.append("\n##################################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String cargarClaveGSPorAuto()
	{
		logger.debug(
				new StringBuilder()
				.append("\n##################################")
				.append("\n###### cargarClaveGSPorAuto ######")
				.append("\n###### smap1=").append(smap1)
				.toString()
				);
		
		success = true;
		exito   = true;
		
		String cdramo = null;
		String modelo = null;
		
		//datos
		try
		{
			cdramo = smap1.get("cdramo");
			modelo = smap1.get("modelo");
			if(StringUtils.isBlank(cdramo))
			{
				throw new ApplicationException("No se recibio el ramo");
			}
			if(StringUtils.isBlank(modelo))
			{
				throw new ApplicationException("No se recibio el modelo");
			}
		}
		catch(Exception ex)
		{
			long timestamp  = System.currentTimeMillis();
			exito           = false;
			respuesta       = "Datos incompletos #"+timestamp;
			respuestaOculta = ex.getMessage();
			logger.error(respuesta,ex);
		}
		
		//obtener datos auto
		if(exito)
		{
			try
			{
				ManagerRespuestaSmapVO resp = cotizacionManager.cargarClaveGSPorAuto(cdramo,modelo);
				
				exito           = resp.isExito();
				respuesta       = resp.getRespuesta();
				respuestaOculta = resp.getRespuestaOculta();
				
				if(exito)
				{
					smap1.putAll(resp.getSmap());
				}
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = "Error inesperado al obtener clave GS del auto #"+timestamp;
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		logger.debug(
				new StringBuilder()
				.append("\n###### smap1=").append(smap1)
				.append("\n###### cargarClaveGSPorAuto ######")
				.append("\n##################################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String cargarSumaAseguradaAuto()
	{
		logger.debug(
				new StringBuilder()
				.append("\n#####################################")
				.append("\n###### cargarSumaAseguradaAuto ######")
				.append("\n###### smap1=").append(smap1)
				.toString()
				);
		
		success = true;
		exito   = true;
		
		String cdsisrol = null;
		String modelo   = null;
		String version  = null;
		String cdramo   = null;
		String cdtipsit = null;
		
		//datos
		try
		{
			cdsisrol = smap1.get("cdsisrol");
			modelo   = smap1.get("modelo");
			version  = smap1.get("version");
			cdramo   = smap1.get("cdramo");
			cdtipsit = smap1.get("cdtipsit");
			if(StringUtils.isBlank(cdsisrol))
			{
				throw new ApplicationException("No se recibio el rol");
			}
			if(StringUtils.isBlank(modelo)||modelo.length()!=4)
			{
				throw new ApplicationException("No se recibio el modelo");
			}
			if(StringUtils.isBlank(version))
			{
				throw new ApplicationException("No se recibio la version");
			}
			if(StringUtils.isBlank(cdramo))
			{
				throw new ApplicationException("No se recibio el ramo");
			}
			if(StringUtils.isBlank(cdtipsit))
			{
				throw new ApplicationException("No se recibio la situacion");
			}
		}
		catch(Exception ex)
		{
			long timestamp  = System.currentTimeMillis();
			exito           = false;
			respuesta       = new StringBuilder().append("Datos incompletos para obtener valor comercial #").append(timestamp).toString();
			respuestaOculta = ex.getMessage();
			logger.error(respuesta,ex);
		}
		
		if(exito)
		{
			try
			{
				ManagerRespuestaSmapVO resp = cotizacionManager.cargarSumaAseguradaAuto(cdsisrol,modelo,version,cdramo,cdtipsit);
				
				exito           = resp.isExito();
				respuesta       = resp.getRespuesta();
				respuestaOculta = resp.getRespuestaOculta();
				
				if(exito)
				{
					smap1.putAll(resp.getSmap());
				}
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = new StringBuilder().append("Error inesperado al obtener valor comercial #").append(timestamp).toString();
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		logger.debug(
				new StringBuilder()
				.append("\n###### cargarSumaAseguradaAuto ######")
				.append("\n#####################################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String agregarClausulaICD()
	{
		logger.debug(
				new StringBuilder()
				.append("\n################################")
				.append("\n###### agregarClausulaICD ######")
				.append("\n###### smap1=").append(smap1)
				.toString()
				);
		
		success = true;
		exito   = true;
		
		String cdunieco = null;
		String cdramo   = null;
		String estado   = null;
		String nmpoliza = null;
		String nmsituac = null;
		String cdclausu = null;
		String nmsuplem = null;
		String icd      = null;
		
		//datos completos
		try
		{
			cdunieco = smap1.get("cdunieco");
			cdramo   = smap1.get("cdramo");
			estado   = smap1.get("estado");
			nmpoliza = smap1.get("nmpoliza");
			nmsituac = smap1.get("nmsituac");
			cdclausu = smap1.get("cdclausu");
			nmsuplem = smap1.get("nmsuplem");
			icd      = smap1.get("icd");
			if(StringUtils.isBlank(cdunieco)
					||StringUtils.isBlank(cdramo)
					||StringUtils.isBlank(estado)
					||StringUtils.isBlank(nmpoliza)
					||StringUtils.isBlank(nmsituac)
					||StringUtils.isBlank(cdclausu)
					||StringUtils.isBlank(nmsuplem)
					||StringUtils.isBlank(icd)
					)
			{
				throw new ApplicationException("Dato requerido no encontrado");
			}
		}
		catch(Exception ex)
		{
			long timestamp  = System.currentTimeMillis();
			exito           = false;
			respuesta       = new StringBuilder().append("Datos incompletos para guardar ICD #").append(timestamp).toString();
			respuestaOculta = ex.getMessage();
			logger.error(respuesta,ex);
		}
		
		if(exito)
		{
			try
			{
				ManagerRespuestaVoidVO resp = cotizacionManager.agregarClausulaICD(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,nmsituac
						,cdclausu
						,nmsuplem
						,icd);
				exito           = resp.isExito();
				respuesta       = resp.getRespuesta();
				respuestaOculta = resp.getRespuestaOculta();
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = new StringBuilder().append("Error inesperado al guardar ICD #").append(timestamp).toString();
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		logger.debug(
				new StringBuilder()
				.append("\n###### agregarClausulaICD ######")
				.append("\n################################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String cargarClausulaICD()
	{
		logger.debug(
				new StringBuilder()
				.append("\n###############################")
				.append("\n###### cargarClausulaICD ######")
				.append("\n###### smap1=").append(smap1)
				.toString()
				);
		
		success = true;
		exito   = true;
		
		String cdunieco = null;
		String cdramo   = null;
		String estado   = null;
		String nmpoliza = null;
		String nmsituac = null;
		String cdclausu = null;
		String nmsuplem = null;
		
		//datos completos
		try
		{
			cdunieco = smap1.get("cdunieco");
			cdramo   = smap1.get("cdramo");
			estado   = smap1.get("estado");
			nmpoliza = smap1.get("nmpoliza");
			nmsituac = smap1.get("nmsituac");
			cdclausu = smap1.get("cdclausu");
			nmsuplem = smap1.get("nmsuplem");
			if(StringUtils.isBlank(cdunieco)
					||StringUtils.isBlank(cdramo)
					||StringUtils.isBlank(estado)
					||StringUtils.isBlank(nmpoliza)
					||StringUtils.isBlank(nmsituac)
					||StringUtils.isBlank(cdclausu)
					||StringUtils.isBlank(nmsuplem)
					)
			{
				throw new ApplicationException("Dato requerido no encontrado");
			}
		}
		catch(Exception ex)
		{
			long timestamp  = System.currentTimeMillis();
			exito           = false;
			respuesta       = new StringBuilder().append("Datos incompletos para cargar ICD #").append(timestamp).toString();
			respuestaOculta = ex.getMessage();
			logger.error(respuesta,ex);
			
			slist1 = new ArrayList<Map<String,String>>();
		}
		
		if(exito)
		{
			try
			{
				ManagerRespuestaSlistVO resp=cotizacionManager.cargarClausulaICD(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,nmsituac
						,cdclausu
						,nmsuplem);
				exito           = resp.isExito();
				respuesta       = resp.getRespuesta();
				respuestaOculta = resp.getRespuestaOculta();
				slist1          = resp.getSlist();
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = new StringBuilder().append("Error inesperado al cargar ICD #").append(timestamp).toString();
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
				
				slist1 = new ArrayList<Map<String,String>>();
			}
		}
		
		logger.debug(
				new StringBuilder()
				.append("\n###### slist1=").append(slist1)
				.append("\n###### cargarClausulaICD ######")
				.append("\n###############################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String borrarClausulaICD()
	{
		logger.debug(
				new StringBuilder()
				.append("\n###############################")
				.append("\n###### borrarClausulaICD ######")
				.append("\n###### smap1=").append(smap1)
				.toString()
				);
		
		success = true;
		exito   = true;
		
		String cdunieco = null;
		String cdramo   = null;
		String estado   = null;
		String nmpoliza = null;
		String nmsituac = null;
		String cdclausu = null;
		String nmsuplem = null;
		String icd      = null;
		
		//datos completos
		try
		{
			cdunieco = smap1.get("cdunieco");
			cdramo   = smap1.get("cdramo");
			estado   = smap1.get("estado");
			nmpoliza = smap1.get("nmpoliza");
			nmsituac = smap1.get("nmsituac");
			cdclausu = smap1.get("cdclausu");
			nmsuplem = smap1.get("nmsuplem");
			icd      = smap1.get("icd");
			if(StringUtils.isBlank(cdunieco)
					||StringUtils.isBlank(cdramo)
					||StringUtils.isBlank(estado)
					||StringUtils.isBlank(nmpoliza)
					||StringUtils.isBlank(nmsituac)
					||StringUtils.isBlank(cdclausu)
					||StringUtils.isBlank(nmsuplem)
					||StringUtils.isBlank(icd)
					)
			{
				throw new ApplicationException("Dato requerido no encontrado");
			}
		}
		catch(Exception ex)
		{
			long timestamp  = System.currentTimeMillis();
			exito           = false;
			respuesta       = new StringBuilder().append("Datos incompletos para borrar ICD #").append(timestamp).toString();
			respuestaOculta = ex.getMessage();
			logger.error(respuesta,ex);
		}
		
		if(exito)
		{
			try
			{
				ManagerRespuestaVoidVO resp = cotizacionManager.borrarClausulaICD(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,nmsituac
						,cdclausu
						,nmsuplem
						,icd);
				exito           = resp.isExito();
				respuesta       = resp.getRespuesta();
				respuestaOculta = resp.getRespuestaOculta();
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = new StringBuilder().append("Error inesperado al borrar ICD #").append(timestamp).toString();
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		logger.debug(
				new StringBuilder()
				.append("\n###### borrarClausulaICD ######")
				.append("\n###############################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String validarDescuentoAgente()
	{
		logger.debug(
				new StringBuilder()
				.append("\n####################################")
				.append("\n###### validarDescuentoAgente ######")
				.append("\n###### smap1=").append(smap1)
				.toString()
				);
		
		success = true;
		exito   = true;
		
		String tipoUnidad = null;
		String cdagente   = null;
		String uso        = null;
		String descuento  = null;
		
		String zona       = "9";
		String promotoria = "13";
		
		//datos completos
		try
		{
			if(smap1==null)
			{
				throw new ApplicationException("No hay parametros");
			}
			tipoUnidad = smap1.get("tipoUnidad");
			cdagente   = smap1.get("cdagente");
			uso        = smap1.get("uso");
			descuento  = smap1.get("descuento");
			if(StringUtils.isBlank(tipoUnidad))
			{
				throw new ApplicationException("No se recibio el tipo de unidad");
			}
			if(StringUtils.isBlank(cdagente))
			{
				throw new ApplicationException("No se recibio el codigo de agente");
			}
			if(StringUtils.isBlank(uso))
			{
				throw new ApplicationException("No se recibio el tipo de uso");
			}
			if(StringUtils.isBlank(descuento))
			{
				throw new ApplicationException("No se recibio el descuento");
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
		catch(Exception ex)
		{
			long timestamp  = System.currentTimeMillis();
			exito           = false;
			respuesta       = new StringBuilder("Error al validar datos para el descuento #").append(timestamp).toString();
			respuestaOculta = ex.getMessage();
			logger.error(respuesta,ex);
		}
		
		//proceso
		if(exito)
		{
			ManagerRespuestaVoidVO resp = cotizacionManager.validarDescuentoAgente(tipoUnidad,uso,zona,promotoria,cdagente,descuento);
			exito           = resp.isExito();
			respuesta       = resp.getRespuesta();
			respuestaOculta = resp.getRespuestaOculta();
		}
		
		logger.debug(
				new StringBuilder()
				.append("\n###### validarDescuentoAgente ######")
				.append("\n####################################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String cargarClienteCotizacion()
	{
		logger.debug(
				new StringBuilder()
				.append("\n#####################################")
				.append("\n###### cargarClienteCotizacion ######")
				.append("\n###### smap1=").append(smap1)
				.toString()
				);
		
		success = true;
		exito   = true;
		
		String cdunieco = null;
		String cdramo   = null;
		String estado   = null;
		String nmpoliza = null;
		
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
				throw new ApplicationException("No se recibio la poliza");
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
		catch(Exception ex)
		{
			long timestamp  = System.currentTimeMillis();
			exito           = false;
			respuesta       = new StringBuilder("Error al validar datos #").append(timestamp).toString();
			respuestaOculta = ex.getMessage();
			logger.error(respuesta,ex);
		}
		
		//proceso
		if(exito)
		{
			ManagerRespuestaSmapVO resp = cotizacionManager.cargarClienteCotizacion(cdunieco,cdramo,estado,nmpoliza);
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
				.append("\n###### cargarClienteCotizacion ######")
				.append("\n#####################################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String cargarConceptosGlobalesGrupo()
	{
		logger.debug(
				new StringBuilder()
				.append("\n##########################################")
				.append("\n###### cargarConceptosGlobalesGrupo ######")
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
		String cdperpag = null;
		
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
			cdperpag = smap1.get("cdperpag");
			if(StringUtils.isBlank(cdunieco))
			{
				throw new ApplicationException("Falta la sucursal");
			}
			if(StringUtils.isBlank(cdramo))
			{
				throw new ApplicationException("Falta el producto");
			}
			if(StringUtils.isBlank(estado))
			{
				throw new ApplicationException("Falta el estado");
			}
			if(StringUtils.isBlank(nmpoliza))
			{
				throw new ApplicationException("Falta la poliza");
			}
			if(StringUtils.isBlank(nmsuplem))
			{
				throw new ApplicationException("Falta el suplemento");
			}
			if(StringUtils.isBlank(cdperpag))
			{
				throw new ApplicationException("Falta el tipo de pago");
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
		catch(Exception ex)
		{
			long timestamp  = System.currentTimeMillis();
			exito           = false;
			respuesta       = new StringBuilder("Error al validar datos #").append(timestamp).toString();
			respuestaOculta = ex.getMessage();
			logger.error(respuesta,ex);
		}
		
		//proceso
		if(exito)
		{
			ManagerRespuestaSmapVO resp = cotizacionManager.cargarConceptosGlobalesGrupo(cdunieco,cdramo,estado,nmpoliza,nmsuplem,cdperpag);
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
				.append("\n###### smap1=").append(smap1)
				.append("\n###### cargarConceptosGlobalesGrupo ######")
				.append("\n##########################################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String obtenerTiposSituacion()
	{
		logger.debug(
				new StringBuilder()
				.append("\n###################################")
				.append("\n###### obtenerTiposSituacion ######")
				.toString()
				);
		
		exito   = true;
		success = true;
		
		ManagerRespuestaSlistVO resp=cotizacionManager.obtenerTiposSituacion();
		exito           = resp.isExito();
		respuesta       = resp.getRespuesta();
		respuestaOculta = resp.getRespuestaOculta();
		if(exito)
		{
			slist1 = resp.getSlist();
		}
		
		logger.debug(
				new StringBuilder()
				.append("\n###### obtenerTiposSituacion ######")
				.append("\n###################################")
				.toString()
				);
		return SUCCESS;
	}
	
	/**
	 ***********guardarValoresSituaciones***********
	 ***********************************************
	 **Guarda los cambios en la pagina de asegurados
	 **agrupados por familia para los datos de 
	 **situacion como extraprimas 
	 * @return
	 */
	public String guardarValoresSituaciones(){
		logger.debug(
				new StringBuilder()
				.append("\n#######################################")
				.append("\n###### guardarValoresSituaciones ######")
				.append("\n###### slist1=").append(slist1)
				.append("\n###### params=").append(params)
				.toString()
				);

		exito   = true;
		success = true;
		Boolean guardarExt= false;
		//proceso
		try{
			Utils.validate(slist1, "No se recibieron datos");
			Utils.validate(params, "No se recibieron parametros");
			String cdunieco = params.get("cdunieco");
			String cdramo   = params.get("cdramo");
			String estado	= params.get("estado");
			String nmpoliza = params.get("nmpoliza");
			String cdtipsit = params.get("cdtipsit");
			String nmsuplem = params.get("nmsuplem");
			ManagerRespuestaVoidVO resp = cotizacionManager.guardarValoresSituaciones(cdunieco, cdramo, estado, nmpoliza, nmsuplem, slist1, cdtipsit, guardarExt);
			exito           = resp.isExito();
			respuesta       = resp.getRespuesta();
			respuestaOculta = resp.getRespuestaOculta();
		}catch(Exception ex){
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(
				new StringBuilder()
				.append("\n###### guardarValoresSituaciones ######")
				.append("\n#######################################")
				.toString()
				);
		return SUCCESS;
	}
	
	/**
	 *************guardarValoresSituacionesTitular*************
	 **********************************************************
	 **Cambia el valor de extraprima ocupacional
	 **para todos los titulares de la poliza
	 **recibida 
	 * @return
	 */
	public String guardarValoresSituacionesTitular()
	{
		logger.debug(
				new StringBuilder()
				.append("\n#######################################")
				.append("\n###### guardarValoresSituacionesTitular ######")
				.append("\n###### params=").append(params)
				.toString()
				);
		success = true;		
		try{
			Utils.validate(params, "No se recibieron parametros");
			Utils.validate(params.get("cdunieco"), "No se recibio oficina",
						   params.get("cdramo")	 , "No se recibio el producto",
						   params.get("estado")	 , "No se recibio el estado",
						   params.get("nmpoliza"), "No se recibio el numero de poliza",
						   params.get("nmsuplem"), "No se recibio el suplemento",
						   params.get("cdtipsit"), "No se recibio el tipo de situacion",
						   params.get("valor")	 , "No se recibio el valor",
						   params.get("cdgrupo") , "No se recibio el grupo");
			String cdunieco = params.get("cdunieco");
			String cdramo 	= params.get("cdramo");
			String estado	= params.get("estado");
			String nmpoliza = params.get("nmpoliza");
			String nmsuplem = params.get("nmsuplem");
			String cdtipsit = params.get("cdtipsit");
			String valor 	= params.get("valor");
			String cdgrupo 	= params.get("cdgrupo");
		//proceso
			ManagerRespuestaVoidVO resp = cotizacionManager.guardarValoresSituacionesTitular(cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdtipsit, valor, cdgrupo);
			exito           = resp.isExito();
			respuesta       = resp.getRespuesta();
			respuestaOculta = resp.getRespuestaOculta();
		}catch(Exception ex){
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(
				new StringBuilder()
				.append("\n###### guardarValoresSituaciones ######")
				.append("\n#######################################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String validarCambioZonaGMI()
	{
		logger.debug(
				new StringBuilder()
				.append("\n##################################")
				.append("\n###### validarCambioZonaGMI ######")
				.append("\n###### smap1=").append(smap1)
				.toString()
				);
		
		exito   = true;
		success = true;
		
		String cdramo    = null;
		String cdtipsit	 = null;
		String codpostal = null;
		
		//datos completos
		try
		{
			if(smap1==null)
			{
				throw new ApplicationException("No se recibieron datos");
			}
			cdramo    = smap1.get("cdramo");
			cdtipsit  = smap1.get("cdtipsit");
			codpostal = smap1.get("codpostal");
			if(StringUtils.isBlank(cdramo))
			{
				throw new ApplicationException("No se recibio el producto");
			}
			if(StringUtils.isBlank(cdtipsit))
			{
				throw new ApplicationException("No se recibio la modalidad");
			}
			if(StringUtils.isBlank(codpostal))
			{
				throw new ApplicationException("No se recibio el codigo postal");
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
			ManagerRespuestaVoidVO resp=cotizacionManager.validarCambioZonaGMI(
					null //cdunieco
					,cdramo
					,cdtipsit
					,null //estado
					,null //nmpoliza
					,"0"  //nmsuplem
					,null //nmsituac
					,codpostal);
			exito           = resp.isExito();
			respuesta       = resp.getRespuesta();
			respuestaOculta = resp.getRespuestaOculta();
		}
		
		logger.debug(
				new StringBuilder()
				.append("\n###### validarCambioZonaGMI ######")
				.append("\n##################################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String validarEnfermedadCatastGMI()
	{
		logger.debug(
				new StringBuilder()
				.append("\n########################################")
				.append("\n###### validarEnfermedadCatastGMI ######")
				.append("\n###### smap1=").append(smap1)
				.toString()
				);
		
		exito   = true;
		success = true;
		
		String cdramo   = null;
		String circHosp = null;
		
		//datos completos
		try
		{
			if(smap1==null)
			{
				throw new ApplicationException("No se recibieron datos");
			}
			cdramo   = smap1.get("cdramo");
			circHosp = smap1.get("circHosp");
			if(StringUtils.isBlank(cdramo))
			{
				throw new ApplicationException("No se recibio el producto");
			}
			if(StringUtils.isBlank(circHosp))
			{
				throw new ApplicationException("No se recibio el circulo hospitalario");
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
			ManagerRespuestaVoidVO resp=cotizacionManager.validarEnfermedadCatastGMI(
					null //cdunieco
					,cdramo
					,null //estado
					,null //nmpoliza
					,"0"  //nmsuplem
					,null //nmsituac
					,circHosp);
			exito           = resp.isExito();
			respuesta       = resp.getRespuesta();
			respuestaOculta = resp.getRespuestaOculta();
		}
		
		logger.debug(
				new StringBuilder()
				.append("\n###### validarEnfermedadCatastGMI ######")
				.append("\n########################################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String guardarContratanteColectivo()
	{
		logger.debug(Utils.log(
				 "\n#########################################"
				,"\n###### guardarContratanteColectivo ######"
				,"\n###### smap1=",smap1
				));
		
		try
		{
			logger.debug("Validando datos de entrada");
			Utils.validate(smap1, "No se recibieron datos");
			String cdunieco = smap1.get("cdunieco");
			String cdramo   = smap1.get("cdramo");
			String estado   = smap1.get("estado");
			String nmpoliza = smap1.get("nmpoliza");
			String nmsuplem = smap1.get("nmsuplem");
			String rfc      = smap1.get("cdrfc");
			String cdperson = smap1.get("cdperson");
			String nombre   = smap1.get("nombre");
			String cdpostal = smap1.get("codpostal");
			String cdedo    = smap1.get("cdedo");
			String cdmunici = smap1.get("cdmunici");
			String dsdomici = smap1.get("dsdomici");
			String nmnumero = smap1.get("nmnumero");
			String nmnumint = smap1.get("nmnumint");
			String nmorddom = smap1.get("nmorddom");
			
			if(StringUtils.isBlank(nmorddom)){
				nmorddom =  "1"; // valor default de domicilio
			}
			
			String confirmaEmision = smap1.get("confirmaEmision");
			boolean esConfirmaEmision = (StringUtils.isNotBlank(confirmaEmision) && Constantes.SI.equalsIgnoreCase(confirmaEmision));
			
			Utils.validate(cdunieco , "No se recibio la sucursal");
			Utils.validate(cdramo   , "No se recibio el ramo");
			Utils.validate(estado   , "No se recibio el estado");
			Utils.validate(nmpoliza , "No se recibio el numero de poliza");
			if(!esConfirmaEmision) Utils.validate(nombre   , "No se recibio el nombre");
			if(!esConfirmaEmision) Utils.validate(cdpostal , "No se recibio el codigo postal");
			if(!esConfirmaEmision) Utils.validate(cdedo    , "No se recibio el estado");
			if(!esConfirmaEmision) Utils.validate(cdmunici , "No se recibio el municipio");
			if(esConfirmaEmision)  Utils.validate(cdperson , "No se recibio el cdperson");
			
			UserVO usuarioSesion = (UserVO)session.get("USUARIO");
			
			String cdpersonGuardado = cotizacionManager.guardarContratanteColectivo(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplem
					,rfc
					,cdperson
					,nombre
					,cdpostal
					,cdedo
					,cdmunici
					,dsdomici
					,nmnumero
					,nmnumint
					,nmorddom
					,esConfirmaEmision
					,usuarioSesion);
			
			if(StringUtils.isBlank(cdperson)){
				smap1.put("cdperson", cdpersonGuardado);
			}
			
			if(StringUtils.isBlank(cdpersonGuardado)){
				exito =  false;
				respuesta =  "No se guard\u00f3 correctamente la informaci\u00f3n del contratante";
			}else{
				exito = true;
			}
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### guardarContratanteColectivo ######"
				,"\n#########################################"
				));
		return SUCCESS;
	}
	
	public String cargarTramite()
	{
		logger.debug(Utils.log(
				 "\n###########################"
				,"\n###### cargarTramite ######"
				,"\n###### smap1=",smap1
				));
		
		try
		{
			logger.debug("Validando datos");
			Utils.validate(smap1, "No se recibieron datos");
			String ntramite = smap1.get("ntramite");
			Utils.validate(ntramite, "No se recibio el numero de tramite");
			
			ManagerRespuestaSmapVO resp = cotizacionManager.cargarTramite(ntramite);
			exito           = resp.isExito();
			respuesta       = resp.getRespuesta();
			respuestaOculta = resp.getRespuestaOculta();
			if(exito)
			{
				smap1.putAll(resp.getSmap());
			}
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### cargarTramite ######"
				,"\n###########################"
				));
		return SUCCESS;
	}
	
	public String cargarTipoCambioWS()
	{
		logger.debug(Utils.log(
				 "\n################################"
				,"\n###### cargarTipoCambioWS ######"
				));
		
		smap1=new HashMap<String,String>();
		smap1.put("dolar" , "0");
		
		ResponseTipoCambio rtc=tipoCambioService.obtieneTipoCambioDolarGS(2);
		if(rtc!=null&&rtc.getTipoCambio()!=null&&rtc.getTipoCambio().getVenCam()!=null)
		{
			smap1.put("dolar" , rtc.getTipoCambio().getVenCam().toString());
		}
		
		logger.debug(Utils.log(
				 "\n###### smap1=",smap1
				,"\n###### cargarTipoCambioWS ######"
				,"\n################################"
				));
		return SUCCESS;
	}
	
    private String extraerStringDeCelda(Cell cell)
    {
        try
        {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            String cadena = cell.getStringCellValue();
            return cadena==null?"":cadena;
        }
        catch(Exception ex)
        {
            return "";
        }
    }
	
	public String complementoSaludGrupo()
	{
		logger.debug(Utils.log(
				 "\n###################################"
				,"\n###### complementoSaludGrupo ######"
				,"\n###### smap1="         , smap1
				,"\n###### censo="         , censo
				,"\n###### censoFileName=" , censoFileName
				));
		try
		{
			success = true;
			exito   = false;
			
			UserVO user = Utils.validateSession(session);
			
			Utils.validate(smap1 , "No se recibieron datos");
			
			String cdunieco     = smap1.get("cdunieco");
			String cdramo       = smap1.get("cdramo");
			String cdtipsit     = smap1.get("cdtipsit");
			String estado       = smap1.get("estado");
			String nmpoliza     = smap1.get("nmpoliza");
			String ntramite     = smap1.get("ntramite");
			String complemento  = smap1.get("complemento");
			String cdagente     = smap1.get("cdagente");
			String codpostalCli = smap1.get("codpostal");
			String cdestadoCli  = smap1.get("cdestado");
			String cdmuniciCli  = smap1.get("cdmunici");
			String cdplan1      = smap1.get("cdplan1");
			String cdplan2      = smap1.get("cdplan2");
			String cdplan3      = smap1.get("cdplan3");
			String cdplan4      = smap1.get("cdplan4");
			String cdplan5      = smap1.get("cdplan5");
			
			Utils.validate(
					cdunieco      , "No se recibio la sucursal"
					,cdramo       , "No se recibio el producto"
					,cdtipsit     , "No se recibio la modalidad"
					,estado       , "No se recibio el estado"
					,nmpoliza     , "No se recibio el estado"
					,ntramite     , "No se recibio el tramite"
					,complemento  , "No se recibio el complemento"
					,cdagente     , "No se recibio la clave de agente"
					,codpostalCli , "No se recibio el codigo postal"
					,cdestadoCli  , "No se recibio el estado"
					,cdmuniciCli  , "No se recibio el municipio"
					,cdplan1      , "No se recibio el plan 1"
					);
			
			Map<String,Object> managerResp =cotizacionManager.complementoSaludGrupo(
					ntramite
					,cdunieco
					,cdramo
					,estado
					,nmpoliza
					,complemento
					,censo
					,rutaDocumentosTemporal
					,dominioServerLayouts
					,userServerLayouts
					,passServerLayouts
					,directorioServerLayouts
					,cdtipsit
					,user.getUser()
					,user.getRolActivo().getClave()
					,cdagente
					,codpostalCli
					,cdestadoCli
					,cdmuniciCli
					,cdplan1
					,cdplan2
					,cdplan3
					,cdplan4
					,cdplan5
					);
			
			smap1.put("erroresCenso"    , (String)managerResp.get("erroresCenso"));
			smap1.put("filasLeidas"     , (String)managerResp.get("filasLeidas"));
			smap1.put("filasProcesadas" , (String)managerResp.get("filasProcesadas"));
			smap1.put("filasErrores"    , (String)managerResp.get("filasErrores"));
			
			slist1 = (List<Map<String,String>>)managerResp.get("registros");
			
			exito = true;
			
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
				 "\n###### complementoSaludGrupo ######"
				,"\n###################################"
				));
		return SUCCESS;
	}
	
	public String guardarConfiguracionGarantias()
	{
		logger.debug(Utils.log(
				 "\n###########################################"
				,"\n###### guardarConfiguracionGarantias ######"
				,"\n###### smap1="  , smap1
				,"\n###### slist1=" , slist1!=null
				));
		
		try
		{
			Utils.validate(smap1  , "No se recibieron datos");
			Utils.validate(slist1 , "No se recibieron datos de configuraci\u00F3n");
			
			String cdramo   = smap1.get("cdramo");
			String cdtipsit = smap1.get("cdtipsit");
			String cdplan   = smap1.get("cdplan");
			String cdpaq    = smap1.get("cdpaq");
			String dspaq    = smap1.get("dspaq");
			String derpol   = smap1.get("derpol");
			
			Utils.validate(
					cdramo    , "No se recibi\u00F3 el producto"
					,cdtipsit , "No se recibi\u00F3 la modalidad"
					,cdplan   , "No se recibi\u00F3 la clave de plan"
					,cdpaq    , "No se recibi\u00F3 la clave de paquete"
					,dspaq    , "No se recibi\u00F3 el nombre de paquete"
					,derpol   , "No se recibieron los derechos de p\u00F3liza"
					);
			
			smap1.put("cdPaqueteNuevo",cotizacionManager.guardarConfiguracionGarantias(
					cdramo
					,cdtipsit
					,cdplan
					,cdpaq
					,dspaq
					,derpol
					,slist1
					));
			
			success = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### guardarConfiguracionGarantias ######"
				,"\n###########################################"
				));
		return SUCCESS;
	}
	
	public String obtenerCoberturasPlanColec()
	{
		logger.debug(""
				+ "\n########################################"
				+ "\n###### obtenerCoberturasPlanColec ######"
				+ "\nsmap1: "+smap1
				);
		try
		{
			success = true;
			exito   = true;
			slist1 = cotizacionManager.obtenerCoberturasPlanColec(smap1.get("cdramo"),smap1.get("cdtipsit"),smap1.get("cdplan"),smap1.get("cdsisrol"));
		}
		catch(Exception ex)
		{
			long timestamp=System.currentTimeMillis();
			logger.error(timestamp+" error al obtener coberturas plan", ex);
			respuesta       = "Error inesperado #"+timestamp;
			respuestaOculta = ex.getMessage();
			exito           = false;
		}
		logger.debug(""
				+ "\n###### obtenerCoberturasPlanColec ######"
				+ "\n########################################"
				);
		return SUCCESS;
	}

	public String obtienePlanDefinitivo()
	{
		logger.debug(""
				+ "\n########################################"
				+ "\n###### obtienePlanDefinitivo ######"
				+ "\nsmap1:  "+smap1
				+ "\nslist1: "+slist1
				);
		
		String planSeleccionado = null;
		String nombrePlanLargo  = null;
		
		try
		{
			
			/**
			 * Se obtiene lista de coberturas seleccionadas en pantalla
			 */
			Map<String,String> coberturasSel = new HashMap<String, String>();
			
			for(Map<String,String> cobSel:slist1){
			    
			    if("4EAC".equalsIgnoreCase(cobSel.get("amparada"))) continue; //Para saltar la cobertura de Evento de Alto Costo
			    
				if(Constantes.SI.equalsIgnoreCase(cobSel.get("amparada"))){
					coberturasSel.put(cobSel.get("cdgarant"), cobSel.get("cdgarant"));
				}
			}
			
			/**
			 * Se obtiene la lista de planes a iterar del producto 
			 */
			
			LinkedHashMap<String,Object>param=new LinkedHashMap<String,Object>();
			param.put("param1",smap1.get("cdramo"));
			param.put("param2",smap1.get("cdtipsit"));
			List<Map<String,String>>listaPlanes=storedProceduresManager.procedureListCall(
					ObjetoBD.OBTIENE_PLANES_X_PRODUCTO.getNombre(), param, null);
			
			logger.debug("Lista de planes obtenidos: "+ listaPlanes);
			
			HashMap<String, PuntajeCoberturasPlanVO> puntajeCoberturas = new HashMap<String, PuntajeCoberturasPlanVO>();
			
			int puntajeMenorSeparacion = 999999999;
			
			/**
			 * Por cada uno de los planes del prodcto se calcula el puntaje para decidir el plan con menos coberturas a sumar
			 * y a restar. sumando preferentemente
			 */
			
			String cdplanOrig = smap1.get("cdplan");
			String dsplanOrig = "";
			
			for(Map<String,String> plan : listaPlanes){
				
				if(StringUtils.isNotBlank(cdplanOrig) && cdplanOrig.equalsIgnoreCase(plan.get("CDPLAN"))){
					dsplanOrig = plan.get("DSPLAN");
				}
				
				//Coberturas del plan
				List<Map<String,String>> coberturasPlanList = cotizacionManager.obtieneCobeturasNombrePlan(smap1.get("cdramo"),smap1.get("cdtipsit"),plan.get("CDPLAN"));
				
				//Coberturas sobrantes o faltantes del plan original 

				int faltantes = 0; //coberturas faltantes a sumar del plan iterado para igualar el plan seleccionado
				int sobrantes = 0; //coberturas sobrantes a restar del plan iterado para igualar el plan seleccionado
				
				StringBuilder nombreLargo = new StringBuilder(plan.get("DSPLAN"));
				
				String ultimaGarantia = null;
				
				
				for(Map<String,String> cob : coberturasPlanList){
				    
				    if("4EAC".equalsIgnoreCase(cob.get("CDGARANT"))) continue; //Para saltar la cobertura de Evento de Alto Costo
				    if("4EE".equalsIgnoreCase(cob.get("CDGARANT")))  continue; //Para saltar la cobertura de Emergencia en el Extranjero y manejarla al ultimo
				    
					//si la cobertura esta en el plan original y no esta seleccionada en pantalla
					if(Constantes.SI.equalsIgnoreCase(cob.get("SWOBLIGA")) && !coberturasSel.containsKey(cob.get("CDGARANT"))){
						sobrantes ++;
						nombreLargo.append(" -").append(StringUtils.isBlank(cob.get("DSGARANT_CORTA"))? cob.get("CDGARANT") : cob.get("DSGARANT_CORTA"));//TGARANTI
						ultimaGarantia = " SIN "+cob.get("DSGARANT");
						
					}else //si la cobertura se considera dentro del plan predefinido y se encuentra seleccionada en pantalla
						if(Constantes.NO.equalsIgnoreCase(cob.get("SWOBLIGA")) && coberturasSel.containsKey(cob.get("CDGARANT"))){
							faltantes ++;
							nombreLargo.append(" +").append(StringUtils.isBlank(cob.get("DSGARANT_CORTA"))? cob.get("CDGARANT") : cob.get("DSGARANT_CORTA"));//TGARANTI
							ultimaGarantia = " CON "+cob.get("DSGARANT");
						}
				}
				
				// Se repite el for anterior solo para manejar la cobertura de Emergecnia en el Extranjero
				for(Map<String,String> cob : coberturasPlanList){
                    
                    if("4EAC".equalsIgnoreCase(cob.get("CDGARANT"))) continue; //Para saltar la cobertura de Evento de Alto Costo
                    if(!"4EE".equalsIgnoreCase(cob.get("CDGARANT"))) continue; //Para saltar las coberturas que no son Emergencia en el Extranjero
                    
                    logger.debug(" :::: Tratando la cobertura de Emergencia en el Etranjero luego de iterar todas las demas cobeturas :::: ");
                    
                    //si la cobertura esta en el plan original y no esta seleccionada en pantalla
                    if(Constantes.SI.equalsIgnoreCase(cob.get("SWOBLIGA")) && !coberturasSel.containsKey(cob.get("CDGARANT"))){
                        sobrantes ++;
                        nombreLargo.append(" -").append(StringUtils.isBlank(cob.get("DSGARANT_CORTA"))? cob.get("CDGARANT") : cob.get("DSGARANT_CORTA"));//TGARANTI
                        ultimaGarantia = " SIN "+cob.get("DSGARANT");
                        
                    }else //si la cobertura se considera dentro del plan predefinido y se encuentra seleccionada en pantalla
                        if(Constantes.NO.equalsIgnoreCase(cob.get("SWOBLIGA")) && coberturasSel.containsKey(cob.get("CDGARANT"))){
                            faltantes ++;
                            nombreLargo.append(" +").append(StringUtils.isBlank(cob.get("DSGARANT_CORTA"))? cob.get("CDGARANT") : cob.get("DSGARANT_CORTA"));//TGARANTI
                            ultimaGarantia = " CON "+cob.get("DSGARANT");
                        }
                }
				
				//se agrega el plan a generar puntaje
				PuntajeCoberturasPlanVO puntajePlan =  new PuntajeCoberturasPlanVO();
				puntajePlan.setCdplan(plan.get("CDPLAN"));
				puntajePlan.setDsplan(plan.get("DSPLAN"));
				
				if( (faltantes == 0 && sobrantes == 1) || (faltantes == 1 && sobrantes == 0)){
					puntajePlan.setDsplanLargo(plan.get("DSPLAN")+ultimaGarantia);
				}else{
					puntajePlan.setDsplanLargo(nombreLargo.toString());
				}
				
				
				puntajePlan.setCoberturasAquitar(sobrantes);
				puntajePlan.setCoberturasAagregar(faltantes);
				puntajePlan.setTotalCoberturasDif(faltantes+sobrantes);
				
				puntajeCoberturas.put(plan.get("CDPLAN"), puntajePlan);
				
				if((faltantes+sobrantes) < puntajeMenorSeparacion){
					puntajeMenorSeparacion = faltantes+sobrantes;
				}
				
			}
			logger.debug("PLan Resumen:::::"+puntajeCoberturas);
			logger.debug("Puntaje Menor Separacion:::::"+puntajeMenorSeparacion);
			
			//Se seleccionan los planes con menor puntaje absoluto
			HashMap<String, PuntajeCoberturasPlanVO> planesMenorPuntaje = new HashMap<String, PuntajeCoberturasPlanVO>();
			
			for(String plan : puntajeCoberturas.keySet()){
				PuntajeCoberturasPlanVO planIt = puntajeCoberturas.get(plan);
				if(planIt.getTotalCoberturasDif() == puntajeMenorSeparacion){
					planesMenorPuntaje.put(plan, puntajeCoberturas.get(plan));
				}
			}
			
			
			//De los planes con menor puntaje abosuluto se obtiene el que tenga la menor cantidad de coberturas negativas (a quitar)
			PuntajeCoberturasPlanVO planMasProximo = null;
			int menorCoberturasAquitar = 999999999;
			
			for(String plan : planesMenorPuntaje.keySet()){
				PuntajeCoberturasPlanVO planIt = planesMenorPuntaje.get(plan);
				
				if(planIt.getCoberturasAquitar() < menorCoberturasAquitar){
					menorCoberturasAquitar = planIt.getCoberturasAquitar();
					planMasProximo = planIt;
				}
			}
			
			logger.debug("Puntaje Menor Coberturas a Quitar:::::"+menorCoberturasAquitar);
			logger.debug("Plan mas proximo Elegido ::::: "+planMasProximo);
			
			smap1.put("NVO_CDPLAN", planMasProximo.getCdplan());
			smap1.put("NVO_DSPLAN", planMasProximo.getDsplan());
			smap1.put("DSPLAN_ORIG", dsplanOrig);
			smap1.put("NVO_NOMBRE", planMasProximo.getDsplanLargo());
			
			success = true;
			exito   = true;
			 
		}
		catch(Exception ex)
		{
			long timestamp=System.currentTimeMillis();
			logger.error(timestamp+" error al obtener coberturas plan", ex);
			respuesta       = "Error inesperado #"+timestamp;
			respuestaOculta = ex.getMessage();
			exito           = false;
		}
		logger.debug(""
				+ "\n###### obtienePlanDefinitivo ######"
				+ "\n########################################"
				);
		return SUCCESS;
	}

	
	
	public String lanzaAprobacionNombrePlan()
	{
	    logger.debug(""
	            + "\n########################################"
	            + "\n###### lanzaAprobacionNombrePlan ######"
	            + "\nsmap1:  "+smap1
	            );
	    
	    try
	    {
	        
	        Utils.validate(smap1    , "No hay parametros");
	        
	        String ntramite =  smap1.get("ntramite");
	        String bloquear =  smap1.get("tipobloqueo");
	        
	        Utils.validate(ntramite, "No hay numero de tramite", bloquear, "No hay indicacion de bloqueo");
	        
	        UserVO usuario  = (UserVO) session.get("USUARIO");
            String cdsisrol = usuario.getRolActivo().getClave();
	        
            /**
             * Se lanza insercion de registro o eliminacion para bloqueo o desbloqueo
             */
            cotizacionManager.ejectutaBloqueoProcesoTramite(ntramite, TipoProcesoBloqueo.AUTORIZAR_NOMPLAN_SUPERVISOR.getClaveProceso(),
                    cdsisrol, TipoProcesoBloqueo.AUTORIZAR_NOMPLAN_SUPERVISOR.getDescripcion(), bloquear,
                    "D".equalsIgnoreCase(bloquear)? Constantes.DELETE_MODE : Constantes.INSERT_MODE);
	        success = true;
	        
	    }
        catch(Exception ex)
        {
            respuesta = Utils.manejaExcepcion(ex);
            success =  false;
        }
	    logger.debug(""
	            + "\n###### lanzaAprobacionNombrePlan ######"
	            + "\n########################################"
	            );
	    
	    return SUCCESS;
	}

	public String obtieneNombresCoberturasPlan()
	{
	    logger.debug(""
	            + "\n########################################"
	            + "\n###### obtieneNombresCoberturasPlan ######"
	            + "\nsmap1:  "+smap1
	            );
	    
	    slist1 = new ArrayList<Map<String,String>>();
	    
	    try
	    {
	            
            //Coberturas del plan
            List<Map<String,String>> coberturasPlanList = cotizacionManager.obtieneCobeturasNombrePlan(smap1.get("cdramo"),smap1.get("cdtipsit"),smap1.get("cdplan"));
            
            logger.debug("coberturas de la busqiueda:::"+coberturasPlanList);
            
            for(Map<String,String> cob : coberturasPlanList){
                if("4EAC".equalsIgnoreCase(cob.get("CDGARANT"))) continue; //Para saltar la cobertura de Evento de Alto Costo
                if(StringUtils.isBlank(smap1.get("cdplan")) || Constantes.SI.equalsIgnoreCase(cob.get("SWOBLIGA"))){
                    Map<String,String> cobertura =  new HashMap<String, String>();
                    cobertura.put("CDGARANT", cob.get("CDGARANT"));
                    cobertura.put("DSGARANT", cob.get("DSGARANT"));
                    cobertura.put("DSGARANT_CORTA", StringUtils.isBlank(cob.get("DSGARANT_CORTA"))? "" : cob.get("DSGARANT_CORTA"));
                    slist1.add(cobertura);    
                }
            }
            
            logger.debug("Coberturas obtenidas para edicion de Nombre:::"+slist1);
	        
	        success = true;
	        exito   = true;
	        
	    }
	    catch(Exception ex)
	    {
	        long timestamp=System.currentTimeMillis();
	        logger.error(timestamp+" error al obtener coberturas plan", ex);
	        respuesta       = "Error inesperado #"+timestamp;
	        respuestaOculta = ex.getMessage();
	        exito           = false;
	    }
	    logger.debug(""
	            + "\n###### obtieneNombresCoberturasPlan ######"
	            + "\n########################################"
	            );
	    return SUCCESS;
	}
	
	public String restaurarRespaldoCenso()
	{
		logger.debug(Utils.log(
				 "\n####################################"
				,"\n###### restaurarRespaldoCenso ######"
				,"\n###### smap1=" , smap1
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(smap1, "No se recibieron datos");
			
			String cdunieco  = smap1.get("cdunieco")
			       ,cdramo   = smap1.get("cdramo")
			       ,estado   = smap1.get("estado")
			       ,nmpoliza = smap1.get("nmpoliza");
			
			Utils.validate(
					cdunieco  , "No se recibi\u00f3 la sucursal"
					,cdramo   , "No se recibi\u00f3 el producto"
					,estado   , "No se recibi\u00f3 el estado"
					,nmpoliza , "No se recibi\u00f3 la p\u00f3liza"
					);
			
			cotizacionManager.restaurarRespaldoCenso(cdunieco,cdramo,estado,nmpoliza);
			
			success = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success="   , success
				,"\n###### respuesta=" , respuesta
				,"\n###### restaurarRespaldoCenso ######"
				,"\n####################################"
				));
		return SUCCESS;
	}
	
	public String borrarRespaldoCenso()
	{
		logger.debug(Utils.log(
				 "\n#################################"
				,"\n###### borrarRespaldoCenso ######"
				,"\n###### smap1=" , smap1
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(smap1, "No se recibieron datos");
			
			String cdunieco  = smap1.get("cdunieco")
			       ,cdramo   = smap1.get("cdramo")
			       ,nmpoliza = smap1.get("nmpoliza");
			
			Utils.validate(
					cdunieco  , "No se recibi\u00f3 la sucursal"
					,cdramo   , "No se recibi\u00f3 el producto"
					,nmpoliza , "No se recibi\u00f3 la p\u00f3liza"
					);
			
			cotizacionManager.borrarRespaldoCenso(cdunieco,cdramo,nmpoliza);
			
			success = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success="   , success
				,"\n###### respuesta=" , respuesta
				,"\n###### borrarRespaldoCenso ######"
				,"\n#################################"
				));
		return SUCCESS;
	}
	
	private class ConfirmaCensoConcurrente1 extends Thread
    {
		private Map<String,String> smap1;
		private CotizacionAction   cotAction;
    	
    	private UserVO usuarioSesion;
    	List<Map<String,Object>>grupos;
    	
    	public ConfirmaCensoConcurrente1(Map<String,String> parametros, CotizacionAction cotAction) {
    		this.smap1 = parametros;
    		this.cotAction =  cotAction;
    	}
    	
    	@Override
    	public void run()
    	{
    		try
    		{
    			
    			boolean success = true;
    			boolean exito   = true;
    			
    			String censoTimestamp   = smap1.get("timestamp");
    			String clasif           = smap1.get("clasif");
    			String LINEA_EXTENDIDA  = smap1.get("LINEA_EXTENDIDA");
    			String cdunieco         = smap1.get("cdunieco");
    			String cdtipsit         = smap1.get("cdtipsit");
    			String cdramo           = smap1.get("cdramo");
    			String nmpoliza         = smap1.get("nmpoliza");
    			String cdperpag         = smap1.get("cdperpag");
    			String pcpgocte         = smap1.get("pcpgocte");
    			UserVO usuario          = (UserVO)session.get("USUARIO");
    			String user             = usuario.getUser();
    			String cdelemento       = usuario.getEmpresa().getElementoId();
    			String cdsisrol         = usuario.getRolActivo().getClave();
    			final String LINEA      = "1";
    			String ntramite         = smap1.get("ntramite");
    			boolean hayTramite      = StringUtils.isNotBlank(ntramite);
    			String ntramiteVacio    = smap1.get("ntramiteVacio");
    			boolean hayTramiteVacio = StringUtils.isNotBlank(ntramiteVacio);
    			Date fechaHoy           = new Date();
    			String feini            = smap1.get("feini");
    			String fefin            = smap1.get("fefin");
    			String nmpolant         = smap1.get("nmpolant");
    			String nmrenova         = smap1.get("nmrenova");
    			
    			String estatuRenovacion = smap1.get("estatusRenovacion");
    			String esRenovacion		= smap1.get("esRenovacion");
    			
    			String nombreCensoConfirmado = smap1.get("nombreCensoConfirmado");
    			String nombreCenso= nombreCensoConfirmado;
    			
    			
    			mesaControlManager.marcarTramiteComoStatusTemporal(ntramite,EstatusTramite.EN_TARIFA.getCodigo());
    				try
    				{
						String cdedo         = smap1.get("cdedo");
						String cdmunici      = smap1.get("cdmunici");
						String cdplanes[]    = new String[5];
						
						for(Map<String,Object>iGrupo:olist1)
						{
							String  cdgrupo      = (String)iGrupo.get("letra");
							String  cdplan       = (String)iGrupo.get("cdplan");
							Integer indGrupo     = Integer.valueOf(cdgrupo);
							cdplanes[indGrupo-1] = cdplan;
						}
						
						cotizacionManager.guardarCensoCompletoMultisalud(nombreCenso
								,cdunieco    , cdramo      , "W"
								,nmpoliza    , cdedo       , cdmunici
								,cdplanes[0] , cdplanes[1] , cdplanes[2]
								,cdplanes[3] , cdplanes[4] , "N"
								);
					}
    				catch(Exception ex)
    				{
    					long timestamp = System.currentTimeMillis();
    					throw new ApplicationException(new StringBuilder("Error al ejecutar procedimiento del censo #").append(timestamp).toString());
    				}
    				
    			
    			try
    			{
    				logger.debug(Utils.log("########################user 5",user));
    				tvalositSigsvdefTvalogarContratanteTramiteSigsvalipolObject aux=cotAction.tvalositSigsvdefTvalogarContratanteTramiteSigsvalipol(
    						clasif    , LINEA      , LINEA_EXTENDIDA
    						,cdunieco , cdramo     , nmpoliza
    						,cdtipsit , hayTramite , hayTramiteVacio
    						,user     , cdelemento , ntramiteVacio
    						,true     , null/*ntramite*/, null/*cdagente*/
    						,false //sincenso
    						,false //censoAtrasado
    						,false //resubirCenso
    						,cdperpag
    						,cdsisrol
    						,false
    						,false //asincrono
    						,null
    						,null
    						,null
    						,true //censoCompleto
    						,false // duplicar
    						);
    				exito     = aux.exito;
    				respuesta = StringUtils.isBlank(aux.respuesta)
    				        ? ""
    				        : aux.respuesta;
    				respuestaOculta = aux.respuestaOculta;
    			}
    			catch(Exception ex)
    			{
    				long timestamp = System.currentTimeMillis();
    				throw new ApplicationException(new StringBuilder("Error al ejecutar procesoColectivoInterno al confirmar censo concurrente #").append(timestamp).toString());
    			}
    			
    			long stamp = System.currentTimeMillis();
    			logger.debug(Utils.log(stamp,"Mandando el tramite a estatus completo despues de subir censo cocurrente y proceso colectivo interno"));
    			
    			if(estatuRenovacion.equalsIgnoreCase(EstatusTramite.EN_ESPERA_DE_COTIZACION.getCodigo())){
    				mesaControlManager.marcarTramiteComoStatusTemporal(ntramite,EstatusTramite.EN_ESPERA_DE_COTIZACION.getCodigo());
    			}else{
    				mesaControlManager.marcarTramiteComoStatusTemporal(ntramite,EstatusTramite.TRAMITE_COMPLETO.getCodigo());
    			}
    		}
    		catch(Exception ex)
    		{
    			logger.error("Error en confirmar censo concurrente", ex);
    		}
    	}
    }
	
	public String consultaExtraprimaOcup() throws Exception {
		
		try {
			String otvalor = cotizacionManager.consultaExtraprimOcup(smap1.get("cdtipsit"));
			params = new HashMap<String, String>();
			params.put("otvalor", otvalor);
			exito = true;
			success =true;
		} catch (Exception e) {
			respuesta = Utils.manejaExcepcion(e);
		}
		return SUCCESS;
	}
	
	public String obtenSumaAseguradosMedicamentos()
	{
		logger.debug(""
				+ "\n#############################################"
				+ "\n###### obtenSumaAseguradosMedicamentos ######"
				+ "\nsmap1: "+smap1
				);
		try
		{
			success = true;
			exito   = true;
			saMed =  cotizacionManager.obtenSumaAseguradosMedicamentos(smap1.get("cdramo"),smap1.get("cdtipsit"),"4MED");
		}
		catch(Exception ex)
		{
			long timestamp=System.currentTimeMillis();
			logger.error(timestamp+" error al obtener la suma asegurada pr defecto", ex);
			respuesta       = "Error inesperado #"+timestamp;
			respuestaOculta = ex.getMessage();
			exito           = false;
		}
		logger.debug(""
				+ "\n###### obtenSumaAseguradosMedicamentos ######"
				+ "\n#############################################"
				);
		return SUCCESS;
	}
	
	
	
	
	public String buscarEmpleados()
	{
		logger.debug(""
				+ "\n########################################"
				+ "\n###### buscarEmpleados ######"
				+ "\nparams: "+params
				);
		try
		{
			
			success = true;
			exito   = true;
			String pv_administradora_i=  params.get("administradora");
			String pv_retenedora_i=  params.get("retenedora");
			String pv_clave_i=  params.get("clave_empleado");
			String pv_rfc_i=  params.get("rfc");
			String pv_apellido_paterno_i=  params.get("ap_paterno");
			String pv_apellido_materno_i=  params.get("ap_materno");
			String pv_nombre_i=  params.get("nombre");
			slist1= cotizacionManager.buescaEmpleado(pv_administradora_i,pv_retenedora_i,pv_clave_i,
					pv_nombre_i,
					pv_apellido_paterno_i,
					pv_apellido_materno_i, 
					pv_rfc_i);
		}
		catch(Exception ex)
		{
			long timestamp=System.currentTimeMillis();
			logger.error(timestamp+" error al obtener coberturas plan", ex);
			respuesta       = "Error inesperado #"+timestamp;
			respuestaOculta = ex.getMessage();
			exito           = false;
		}
		logger.debug(""
				+ "\n###### buscarEmpleados ######"
				+ "\n########################################"
				);
		return SUCCESS;
	}
	
	
	public String guardaEmpleados(){
        logger.debug(""
                + "\n########################################"
                + "\n###### guardaEmpleados ######"+smap1
                + "\nparams: "+params
                );
        try
        {
            
            success = true;
            exito   = true;
            String pv_numsuc_i=  params.get("administradora");
            String pv_cveent_i=  params.get("retenedora");
            String pv_cveemp_i=  params.get("clave");
            String pv_nomemp_i=  params.get("nombre");
            String pv_apaterno_i=  params.get("apaterno");
            String pv_amaterno_i=  params.get("amaterno");
            String pv_rfc_i=  params.get("rfc");
            String pv_curp_i=  params.get("curp");
            String pv_usuario_i=  params.get("usuario");
            String pv_feregist_i=  params.get("nombre");
            UserVO usuario=(UserVO)session.get("USUARIO");
            String cdusuario=usuario.getUser();
           cotizacionManager.guardaEmpleado(pv_numsuc_i,
                   pv_cveent_i,
                   pv_cveemp_i, 
                   pv_nomemp_i, 
                   pv_apaterno_i,
                   pv_amaterno_i,
                   pv_rfc_i, 
                   pv_curp_i, 
                   cdusuario, 
                   null,
                   null)
           ;
        }
        catch(Exception ex)
        {
            long timestamp=System.currentTimeMillis();
            logger.error(timestamp+" error al obtener coberturas plan", ex);
            respuesta       = "Error inesperado #"+timestamp;
            respuestaOculta = ex.getMessage();
            exito           = false;
        }
        logger.debug(""
                + "\n###### guardaEmpleados ######"
                + "\n########################################"
                );
        return SUCCESS;
    }
	   
	public String refrescarCensoColectivo() {
        logger.debug(Utils.log(
                 "\n#####################################"
                ,"\n###### refrescarCensoColectivo ######"
                ,"\n###### smap1=" , smap1
                ));
        
        try{
            Utils.validateSession(session);
            
            Utils.validate(smap1, "No se recibieron datos");
            
            String cdunieco  = smap1.get("cdunieco")
                   ,cdramo   = smap1.get("cdramo")
                   ,estado   = smap1.get("estado")
                   ,nmpolant = smap1.get("nmpolant")
                   ,ntramite = smap1.get("ntramite");
            
            Utils.validate(
                    cdunieco  , "No se recibi\u00f3 la sucursal"
                    ,cdramo   , "No se recibi\u00f3 el producto"
                    ,estado   , "No se recibi\u00f3 el estado"
                    ,nmpolant , "No se recibi\u00f3 el nmpolant"
                    ,ntramite , "No se recibi\u00f3 el tr&aacute;mite"
                    );
            HashMap<String,String> params = (HashMap<String, String>) siniestrosManager.obtenerTramiteCompleto(ntramite);
            
            cotizacionManager.refrescarCensoColectivo(cdunieco,cdramo,estado,Integer.parseInt(nmpolant.substring(7,13))+"",params.get("NMSOLICI"));
            success = true;
        } catch(Exception ex){
            respuesta = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(
                 "\n###### success="   , success
                ,"\n###### respuesta=" , respuesta
                ,"\n###### borrarRespaldoCenso ######"
                ,"\n#################################"
                ));
        return SUCCESS;
    }
	  
	/*
    @Action(value   = "cargarAseguradosFiltroGrupoPag",
            results = {
                @Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
                @Result(name="success" , location="/jsp-script/proceso/templates/template.jsp")
            })
    */
	public String cargarAseguradosFiltroGrupoPag()
	{
		try
			{
				// validar sesion
		        UserVO usuarioSesion = Utils.validateSession(session);
		        
		        Utils.validate(smap1, "No hay datos");        
				//message = "Correcto";
				logger.info(""
						+ "\n############################################"
						+ "\n###### cargarAseguradosFiltroGrupoPag ######");
				
				//AseguradosFiltroVO asegurados=consultasAseguradoFiltroManager.cargarAseguradosFiltroGrupo(
				AseguradosFiltroVO asegurados=cotizacionManager.cargarAseguradosFiltroGrupo(
						smap1.get("cdunieco")
						,smap1.get("cdramo")
						,smap1.get("estado")
						,smap1.get("nmpoliza")
						,smap1.get("nmsuplem")
						,smap1.get("cdgrupo")
						//,params.get("nmsitaux")
						//,params.get("start")
						//,params.get("limit")
						,start
						,limit
						,smap1.get("filtro")
						,smap1.get("valorFiltro")
						);
			
				slist1 = asegurados.getAsegurados();
				total = String.valueOf(asegurados.getTotal());
				
				for(Map<String,String>iAsegurado:slist1)
			    {
			    	String tpl = null;
			    	if(StringUtils.isBlank(iAsegurado.get("TITULAR"))){
			    		tpl = "Asegurados";
			    	} else {
			    		tpl = new StringBuilder()
			    		        .append("Familia (")
			    		        .append(iAsegurado.get("FAMILIA"))
			    		        .append(") de ")
    			    		    .append(iAsegurado.get("TITULAR"))
			    		        .toString();
			    	}
			    	iAsegurado.put("AGRUPADOR",
			    			new StringBuilder()
			    	            .append(StringUtils.leftPad(iAsegurado.get("FAMILIA"),3,"0"))
			    	            .append("_")
			    	            .append(tpl)
			    	            .toString());
			    }
				success = true;
				exito= true;
 
			}
			catch(Exception e)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = "Error al cargar los asegurados del grupo #"+timestamp;
				respuestaOculta = e.getMessage();
				logger.error(respuesta,e);
				//message = Utils.manejaExcepcion(e);
			}
		return SUCCESS;
	}

	/** 
	 *************cargarAseguradosExtraprimas****************
	 ********************************************************
	 **Carga los valores de asegurados y 
	 **la informacion a nivel de situacion 
	 **como lo son las extraprimas, ademas  
	 **de agruparlos por familia para el
	 **ramo multialud colectivo (MSC)
	 * @return
	 */
	public String cargarAseguradosFiltroExtraprimas()
	{
		logger.debug(""
				+ "\n###############################################"
				+ "\n###### cargarAseguradosFiltroExtraprimas ######"
				+ "\nsmap1: "+smap1
				);
		success = true;
		exito   = true;
		
		if(exito)
		{
			try
			{
				Utils.validateSession(session);				
				Utils.validate(smap1 , "No se recibieron datos");				
				String cdunieco  = null
						,cdramo   = null
						,estado   = null
						,nmpoliza = null
						,nmsuplem = null
						,cdgrupo  = null
						,filtro   = null
						,valorFiltro = null;				
				cdunieco = smap1.get("cdunieco");
				cdramo   = smap1.get("cdramo");
				estado   = smap1.get("estado");
				nmpoliza = smap1.get("nmpoliza");
				nmsuplem = smap1.get("nmsuplem");
				cdgrupo  = smap1.get("cdgrupo");
				filtro  = smap1.get("filtro");
				valorFiltro  = smap1.get("valorFiltro");
				Utils.validate(
						cdunieco  , "No se recibio la sucursal"
						,cdramo   , "No se recibio el producto"
						,estado   , "No se recibio el estado"
						,nmpoliza , "No se recibio el numero de cotizacion"
						,nmpoliza , "No se recibio el numero de cotizacion"
						,nmpoliza , "No se recibio el numero de cotizacion"
						,nmsuplem , "No se recibio el suplemento"
						,cdgrupo  , "No se recibio la clave de grupo"
						,filtro  , "No se recibio la clave de filtro"
						//,valorFiltro  , "No se recibio la clave de valorFiltro"
				);		
				smap1.put("start", start);
				smap1.put("limit", limit);
			    slist1 = cotizacionManager.cargarAseguradosFiltroExtraprimas(
			    		cdunieco
			    		,cdramo
			    		,estado
			    		,nmpoliza
			    		,nmsuplem
			    		,cdgrupo
			    		,start
			    		,limit
			    		,filtro
			    		,valorFiltro
			    		);
			    for(Map<String,String>iAsegurado:slist1){
			    	String tpl = null;
			    	if(StringUtils.isBlank(iAsegurado.get("TITULAR"))){
			    		tpl = "Asegurados";
			    	}
			    	else
			    	{
			    		tpl = new StringBuilder()
	    	                    .append("Familia (")
	    	                    .append(iAsegurado.get("FAMILIA"))
	    	                    .append(") de ")
	    	                    .append(iAsegurado.get("TITULAR"))
	    	            		.toString();
			    	}
			    	iAsegurado.put("AGRUPADOR",
			    			new StringBuilder()
			    	            .append(StringUtils.leftPad(iAsegurado.get("FAMILIA"),3,"0"))
			    	            .append("_")
			    	            .append(tpl)
			    	            .toString());
			    }
						
				exito   = true;
				success = true;
			}	
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = "Error al cargar extraprimas #"+timestamp;
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		logger.debug(""
				+ "\n###### cargarAseguradosFiltroExtraprimas ######"
				+ "\n#########################################"
				);
		return SUCCESS;
	}
	
	/**
	 ****************************************************
	 ************cargarAseguradosExtraprimas2************
	 ****************************************************
	 **Paginado de la asegurados que muestra 
	 **los valores por situacion como extraprimas,
	 **ademas de agruparlos por familia.
	 ****************************************************
	 * @return
	 */
	public String cargarAseguradosFiltroExtraprimas2()
	{
		logger.debug(Utils.log(
				 "\n##########################################"
				,"\n###### cargarAseguradosFiltroExtraprimas2 ######"
				,"\n###### smap1=" , smap1
				));
			
		//datos completos
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(smap1 , "No se recibieron datos");
			
			String cdunieco  = null
					,cdramo   = null
					,estado   = null
					,nmpoliza = null
					,nmsuplem = null
					,cdgrupo  = null
					,filtro = null
					,valorFiltro = null;
			
			cdunieco = smap1.get("cdunieco");
			cdramo   = smap1.get("cdramo");
			estado   = smap1.get("estado");
			nmpoliza = smap1.get("nmpoliza");
			nmsuplem = smap1.get("nmsuplem");
			cdgrupo  = smap1.get("cdgrupo");
			filtro  = smap1.get("filtro");
			valorFiltro  = smap1.get("valorFiltro");
			
			Utils.validate(
					cdunieco  , "No se recibio la sucursal"
					,cdramo   , "No se recibio el producto"
					,estado   , "No se recibio el estado"
					,nmpoliza , "No se recibio el numero de cotizacion"
					,nmsuplem , "No se recibio el suplemento"
					,cdgrupo  , "No se recibio la clave de grupo"
					,filtro  , "No se recibio la clave de filtro"
					//,valorFiltro  , "No se recibio la clave de valorFiltro"
			);		
			smap1.put("start", start);
			smap1.put("limit", limit);				
			
		    slist1 = cotizacionManager.cargarAseguradosFiltroExtraprimas2(
		    		cdunieco
		    		,cdramo
		    		,estado
		    		,nmpoliza
		    		,nmsuplem
		    		,cdgrupo
		    		,start
		    		,limit
		    		,filtro
		    		,valorFiltro
		    		);
		
			exito   = true;
			success = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		return SUCCESS;
	}
	
    public String tratamientoLayoutEmision()
    {
        this.session=ActionContext.getContext().getSession();
        logger.debug(Utils.log(
                 "\n################################"
                ,"\n###### subirCensoCompleto ######"
                ,"\n###### smap1="  , smap1
                ,"\n###### olist1=" , olist1
                ));
        
        success = true;
        exito   = true;
        
        String censoTimestamp   = smap1.get("timestamp");
        String clasif           = smap1.get("clasif");
        String LINEA_EXTENDIDA  = smap1.get("LINEA_EXTENDIDA");
        String cdunieco         = smap1.get("cdunieco");
        String cdtipsit         = smap1.get("cdtipsit");
        String cdramo           = smap1.get("cdramo");
        String nmpoliza         = smap1.get("nmpoliza");
        String cdperpag         = smap1.get("cdperpag");
        String pcpgocte         = smap1.get("pcpgocte");
        UserVO usuario          = (UserVO)session.get("USUARIO");
        String user             = usuario.getUser();
        String cdelemento       = usuario.getEmpresa().getElementoId();
        String cdsisrol         = usuario.getRolActivo().getClave();
        final String LINEA     = "1";
        String ntramite         = smap1.get("ntramite");
        boolean hayTramite     = StringUtils.isNotBlank(ntramite);
        String ntramiteVacio    = smap1.get("ntramiteVacio");
        boolean hayTramiteVacio = StringUtils.isNotBlank(ntramiteVacio);
        Date fechaHoy           = new Date();
        String feini            = smap1.get("feini");
        String fefin            = smap1.get("fefin");
        String nmpolant         = smap1.get("nmpolant");
        String nmrenova         = smap1.get("nmrenova");
        String esRenovacion     = smap1.get("esRenovacion");
        String agrupador        = smap1.get("cdpool");
        censo = new File(this.rutaDocumentosTemporal+"/censo_"+censoTimestamp);
        
        String nombreCensoConfirmado = smap1.get("nombreCensoConfirmado");
        
        String nombreCenso = null;
        int    filasError=0;
        
        if(exito&&StringUtils.isBlank(nombreCensoConfirmado))
        {
            FileInputStream input       = null;
            Workbook        workbook    = null;
            Sheet           sheet       = null;
            Long            inTimestamp = null;
            File            archivoTxt  = null;
            PrintStream     output      = null;
            
            try
            {   
                input       = new FileInputStream(censo);
                workbook    = WorkbookFactory.create(input);
                sheet       = workbook.getSheetAt(0);
                inTimestamp = System.currentTimeMillis();
                nombreCenso = "censo_"+inTimestamp+"_"+nmpoliza+".txt";
                archivoTxt  = new File(this.rutaDocumentosTemporal+"/"+nombreCenso);
                output      = new PrintStream(archivoTxt);
            }
            catch(Exception ex)
            {
                long etimestamp = System.currentTimeMillis();
                exito           = false;
                respuesta       = "Error al procesar censo #"+etimestamp;
                respuestaOculta = ex.getMessage();
                logger.error(respuesta,ex);
            }
            
            if(exito&&workbook.getNumberOfSheets()!=1)
            {
                long etimestamp = System.currentTimeMillis();
                exito           = false;
                respuesta       = "Favor de revisar el n\u00famero de hojas del censo #"+etimestamp;
                logger.error(respuesta);
            }
            
            if(exito)
            {
                //Iterate through each rows one by one
                logger.debug(""
                        + "\n##############################################"
                        + "\n###### "+archivoTxt.getAbsolutePath()+" ######"
                        );
                
                Iterator<Row> rowIterator        = sheet.iterator();
                int           fila               = 0;
                int           nFamilia           = 0;
                StringBuilder bufferErroresCenso = new StringBuilder();
                int           filasLeidas        = 0;
                int           filasProcesadas    = 0;
                              filasError         = 0;
                if(exito)
                {
                    exito = FTPSUtils.upload
                    (
                        this.dominioServerLayouts,
                        this.userServerLayouts,
                        this.passServerLayouts,
                        archivoTxt.getAbsolutePath(),
                        this.directorioServerLayouts+"/"+nombreCenso
                    )
                    &&FTPSUtils.upload
                    (
                    	this.dominioServerLayouts2,
                        this.userServerLayouts,
                        this.passServerLayouts,
                        archivoTxt.getAbsolutePath(),
                        this.directorioServerLayouts+"/"+nombreCenso
                    );
                    
                    if(!exito)
                    {
                        long etimestamp = System.currentTimeMillis();
                        exito           = false;
                        respuesta       = "Error al transferir archivo al servidor #"+etimestamp;
                        respuestaOculta = respuesta;
                        logger.error(respuesta);
                    }
                }
                
            }
        }
        
        logger.debug(""
                + "\n###### subirCensoCompleto ######"
                + "\n################################"
                );
        return SUCCESS;
    }
    
    public String procesarCargaMasivaFlotillaEmision()
    {
        this.session=ActionContext.getContext().getSession();
        logger.debug(Utils.log(
                 "\n################################################"
                ,"\n###### procesarCargaMasivaFlotillaEmision ######"
                ,"\n###### smap1="  , smap1
                ,"\n###### olist1=" , olist1
                ));
        
        if(smap1.get("timestamp") != null && !smap1.get("timestamp").isEmpty())
        {
            success = true;
            exito   = true;
            
            try
            {   
                Utils.validate(smap1 , "No se recibio dato de fecha del sistema");
                Utils.validate(olist1, "No se recibio la lista de pantalla"     );
                
                respuesta       = "Error al recuperar layOut Complementario";
                String inTimestamp =smap1.get("timestamp");
                String nombreCenso = "excel_"+inTimestamp+".xls";
                File layOutCompl  = new File(this.rutaDocumentosTemporal+"/"+nombreCenso);
                FileInputStream input       = new FileInputStream(layOutCompl);
                Workbook workbook    = WorkbookFactory.create(input);
                Sheet sheet       = workbook.getSheetAt(0);
                Iterator<Row>   rowIterator = sheet.iterator();
                Row row = rowIterator.next();
                int fila = 0;

                logger.debug("Capturando los valores de vista para quitarles espacios en blanco.... ");
                List<Map<String,Object>>  olistMod = olist1;
                for(Map<String,Object> filaVista:olistMod)
                {
                	for (Map.Entry<String, Object> celda : filaVista.entrySet()) {
                		String celdatrim = String.format(celda.getValue().toString()).trim();
                        celda.setValue(celdatrim);
                    }
                }
                logger.debug("Valores de pantalla sin espacios creado.");
                
                while (rowIterator.hasNext()) 
                {   //filaVista  >>>   olist1.get(fila)
                    logger.debug("Iterando del layout inciso numero: "+fila);
                    row = rowIterator.next();
                    
                    //Valor Vehiculo Modificador
                    if(olist1.get(fila).get("CDTIPSIT").toString().equals(TipoSituacion.AUTOS_FRONTERIZOS.getCdtipsit())|| olist1.get(fila).get("CDTIPSIT").toString().equals(TipoSituacion.AUTOS_PICK_UP.getCdtipsit()))
                    {   String valVeh = String.format("%.2f", Double.parseDouble(olist1.get(fila).get("parametros.pv_otvalor07").toString()));
                        olist1.get(fila).put("parametros.pv_otvalor07",valVeh);
                    }else{
                        String valVeh = String.format("%.2f", Double.parseDouble(olist1.get(fila).get("parametros.pv_otvalor13").toString()));
                        olist1.get(fila).put("parametros.pv_otvalor13",valVeh);
                    }                  
                    
                    String clveVeh = StringUtils.leftPad((int)Double.parseDouble(row.getCell(0).toString())+"",5,"0"),
                           modelo = row.getCell(4).toString().length() == 4 ? row.getCell(4).toString() : row.getCell(4).toString().substring(0,4),
                           valorVeh = String.format("%.2f", Double.parseDouble(row.getCell(6).toString())),
                           serie =  "";
                           if(row.getCell(9) != null)
                           {try{serie = String.format("%.0f",Double.parseDouble(row.getCell(9).toString()));
                        	}catch (Exception e){serie =  String.format(row.getCell(9).toString()).trim();}
                           }
                           row.getCell(0).setCellValue(clveVeh);row.getCell(4).setCellValue(modelo);row.getCell(6).setCellValue(valorVeh);row.getCell(9).setCellValue(serie);
                           row.getCell(3).setCellValue(String.format(row.getCell(3).toString()).trim());
                    //valida datos ingresados previamente con los de lay out ingresado       
                    if(    !olistMod.get(fila).get("CDTIPSIT").toString().equals(TipoSituacion.MOTOS.getCdtipsit())//Clave no aplicable para motos 
                        && !olistMod.get(fila).get("CDTIPSIT").toString().equals(TipoSituacion.AUTOS_FRONTERIZOS.getCdtipsit())//Ni autos fonterizos
                        && !olistMod.get(fila).containsValue(row.getCell(0).toString())) //Clave Vehiculo
                    {   respuesta       ="El layout ingresado no corresponde al ingresado previamente en el inciso "+(fila+1)+" en la clave vehiculo "+row.getCell(0).toString();
                        exito           = false;   break;
                    }if(!olistMod.get(fila).containsValue(row.getCell(6).toString())) //Valor Vehiculo
                    {   respuesta       ="El layout ingresado no corresponde al ingresado previamente en el inciso "+(fila+1)+" en el valor del Vehiculo "+row.getCell(6).toString();
                        exito           = false;   break;
                    }if(!olistMod.get(fila).containsValue(String.format(row.getCell(3).toString().toString()).trim())) //Descripcion
                    {   respuesta       ="El layout ingresado no corresponde al ingresado previamente en el inciso "+(fila+1)+" en la descripcion del vehiculo "+row.getCell(3).toString();
                        exito           = false;   break;
                    }if(!olistMod.get(fila).containsValue(row.getCell(4).toString())) //Modelo
                    {   respuesta       ="El layout ingresado no corresponde al ingresado previamente en el inciso "+(fila+1)+" en el modelo "+row.getCell(4).toString();
                        exito           = false;   break;
                    }
//                    if(!olistMod.get(fila).containsValue(row.getCell(9).toString())) //No. Serie
//                    {   respuesta       ="El layout ingresado no corresponde al ingresado previamente  en el inciso "+(fila+1)+" en el numero de serie "+row.getCell(9).toString();
//                        exito           = false;   break;
//                    }
                    //Obliga a tener los siguientes datos
                    logger.debug("El excel introducido, coincide en el inciso numero: "+(fila+1));
                    if(row.getCell(10) == null || row.getCell(10).toString().equals(""))// Numero de motor
                    {   respuesta       ="Favor de introducir numero de motor en el inciso "+(fila+1)+" en el layout a ingresado";
                        exito           = false;   break;
                    }if(row.getCell(11) == null || row.getCell(11).toString().equals(""))// Placas
                    {   respuesta       ="Favor de introducir placas en el inciso "+(fila+1)+" en el layout a ingresado";
                        exito           = false;   break;   
                    }
//                    if(row.getCell(12) == null || row.getCell(12).toString().equals(""))// Conductor 
//                    {   respuesta       ="Favor de introducir conductor en el inciso "+(fila+1)+" en el layout a ingresado";
//                        exito           = false;   break;
//                    }if(row.getCell(13) == null)                               // Beneficiario
//                    {   respuesta       ="Favor de introducir beneficiario en el inciso "+(fila)+" en el layout a ingresado";
//                        exito           = false;   break;
//                    }
                    
                    respuesta = "Actualizando valores complementarios en el inciso: "+(fila);
                    if(row.getCell(10)!=null)
                    {//Motor
	                    if(olist1.get(fila).get("CDTIPSIT").toString().equals(TipoSituacion.AUTOS_FRONTERIZOS.getCdtipsit())|| olist1.get(fila).get("CDTIPSIT").toString().equals(TipoSituacion.AUTOS_PICK_UP.getCdtipsit()))
	                    {   olist1.get(fila).put("parametros.pv_otvalor27",row.getCell(10).toString());
	                    }else{
	                        olist1.get(fila).put("parametros.pv_otvalor38",row.getCell(10).toString());
	                    }
                    }
                    if(row.getCell(11)!=null)
                    {//Placas
		                if(olist1.get(fila).get("CDTIPSIT").toString().equals(TipoSituacion.SERVICIO_PUBLICO_AUTO.getCdtipsit()))
		                {   olist1.get(fila).put("parametros.pv_otvalor40",row.getCell(11).toString());
		                }else if(olist1.get(fila).get("CDTIPSIT").toString().equals(TipoSituacion.SERVICIO_PUBLICO_MICRO.getCdtipsit())){
		                    olist1.get(fila).put("parametros.pv_otvalor35",row.getCell(11).toString());
		                }else if(olist1.get(fila).get("CDTIPSIT").toString().equals(TipoSituacion.AUTOS_FRONTERIZOS.getCdtipsit()) ||olist1.get(fila).get("CDTIPSIT").toString().equals(TipoSituacion.AUTOS_PICK_UP.getCdtipsit())){
		                    olist1.get(fila).put("parametros.pv_otvalor28",row.getCell(11).toString());
		                }else{
		                    olist1.get(fila).put("parametros.pv_otvalor39",row.getCell(11).toString());
		                }
                    }
                    if(row.getCell(12)!=null)
                    {//Conductor
	                    if(olist1.get(fila).get("CDTIPSIT").toString().equals(TipoSituacion.AUTOS_FRONTERIZOS.getCdtipsit())|| olist1.get(fila).get("CDTIPSIT").toString().equals(TipoSituacion.AUTOS_PICK_UP.getCdtipsit()))
	                    {   olist1.get(fila).put("parametros.pv_otvalor44",row.getCell(12).toString());
	                    }else{
	                        olist1.get(fila).put("parametros.pv_otvalor40",row.getCell(12).toString());
	                    }
                    }
                    if(row.getCell(13)!=null)
                    {//BENEFICIARIO PREFERENTE
	                    if(olist1.get(fila).get("CDTIPSIT").toString().equals(TipoSituacion.REMOLQUES_INDISTINTOS.getCdtipsit()))
	                    {   olist1.get(fila).put("parametros.pv_otvalor25",row.getCell(13).toString());
	                    }else if(olist1.get(fila).get("CDTIPSIT").toString().equals(TipoSituacion.SERVICIO_PUBLICO_MICRO.getCdtipsit())){
	                        olist1.get(fila).put("parametros.pv_otvalor37",row.getCell(13).toString());
	                    }else if(olist1.get(fila).get("CDTIPSIT").toString().equals(TipoSituacion.PICK_UP_CARGA.getCdtipsit())){
	                        olist1.get(fila).put("parametros.pv_otvalor47",row.getCell(13).toString());
	                    }else if(olist1.get(fila).get("CDTIPSIT").toString().equals(TipoSituacion.CAMIONES_CARGA.getCdtipsit())){
	                        olist1.get(fila).put("parametros.pv_otvalor49",row.getCell(13).toString());
	                    }else if(olist1.get(fila).get("CDTIPSIT").toString().equals(TipoSituacion.PICK_UP_PARTICULAR.getCdtipsit())){
	                        olist1.get(fila).put("parametros.pv_otvalor53",row.getCell(13).toString());
	                    }else if(olist1.get(fila).get("CDTIPSIT").toString().equals(TipoSituacion.AUTOS_RESIDENTES.getCdtipsit())){
	                        olist1.get(fila).put("parametros.pv_otvalor56",row.getCell(13).toString());
	                    }else if(olist1.get(fila).get("CDTIPSIT").toString().equals(TipoSituacion.AUTOS_FRONTERIZOS.getCdtipsit()) ||olist1.get(fila).get("CDTIPSIT").toString().equals(TipoSituacion.AUTOS_PICK_UP.getCdtipsit())){
	                        olist1.get(fila).put("parametros.pv_otvalor40",row.getCell(13).toString());
	                    }else if(olist1.get(fila).get("CDTIPSIT").toString().equals(TipoSituacion.TURISTA_LICENCIA.getCdtipsit()) ||olist1.get(fila).get("CDTIPSIT").toString().equals(TipoSituacion.TURISTA_VEHICULO.getCdtipsit())){
	                        olist1.get(fila).put("parametros.pv_otvalor41",row.getCell(13).toString());
	                    }else{
	                        olist1.get(fila).put("parametros.pv_otvalor42",row.getCell(13).toString());
	                    }
                    }
                    if(row.getCell(9)!=null)
                    {//NUMERO DE SERIE
	                    if(olist1.get(fila).get("CDTIPSIT").toString().equals(TipoSituacion.AUTOS_FRONTERIZOS.getCdtipsit())|| olist1.get(fila).get("CDTIPSIT").toString().equals(TipoSituacion.AUTOS_PICK_UP.getCdtipsit()))
	                    {   if(!row.getCell(9).toString().isEmpty()){olist1.get(fila).put("parametros.pv_otvalor03",row.getCell(9).toString());}
	                    }else if(olist1.get(fila).get("CDTIPSIT").toString().equals(TipoSituacion.SERVICIO_PUBLICO_MICRO.getCdtipsit())){
	                    	if(!row.getCell(9).toString().isEmpty()){olist1.get(fila).put("parametros.pv_otvalor33",row.getCell(9).toString());}
	                    }else if(olist1.get(fila).get("CDTIPSIT").toString().equals(TipoSituacion.SERVICIO_PUBLICO_AUTO.getCdtipsit()))
	                    {   if(!row.getCell(9).toString().isEmpty()){olist1.get(fila).put("parametros.pv_otvalor35",row.getCell(9).toString());}
	                    }else{
	                    	if(!row.getCell(9).toString().isEmpty()){olist1.get(fila).put("parametros.pv_otvalor37",row.getCell(9).toString());}
	                    }
                    }
                    fila++;                    
                }
                
                if(!exito)
                {
                    throw new ApplicationException(respuesta);
                }
                else
                {
                    respuesta=null;
                }
            }
            catch(Exception ex)
            {
                long etimestamp = System.currentTimeMillis();
                exito           = false;
                respuestaOculta = ex.getMessage();
                logger.error(respuesta,ex);
            }
        }
        
        logger.debug(""
                + "\n###### procesarCargaMasivaFlotillaEmision ######"
                + "\n################################################"
                );
        return SUCCESS;
    }
		
	///////////////////////////////
	////// getters y setters //////
	/*///////////////////////////*/
	public void setKernelManager(KernelManagerSustituto kernelManager) {
		this.kernelManager = kernelManager;
	}

	public void setCatalogosManager(CatalogosManager catalogosManager) {
		this.catalogosManager = catalogosManager;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Map<String, String> getSmap1() {
		return smap1;
	}

	public void setSmap1(Map<String, String> smap1) {
		this.smap1 = smap1;
	}

	public Map<String, Item> getImap() {
		return imap;
	}

	public List<Map<String, String>> getSlist1() {
		return slist1;
	}

	public void setSlist1(List<Map<String, String>> slist1) {
		this.slist1 = slist1;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<Map<String, String>> getSlist2() {
		return slist2;
	}

	public void setSlist2(List<Map<String, String>> slist2) {
		this.slist2 = slist2;
	}

	public void setConsultasManager(ConsultasManager consultasManager) {
		this.consultasManager = consultasManager;
	}

	public void setStoredProceduresManager(
			StoredProceduresManager storedProceduresManager) {
		this.storedProceduresManager = storedProceduresManager;
	}
	public void setPantallasManager(PantallasManager pantallasManager) {
		this.pantallasManager = pantallasManager;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public void setNadaService(NadaService nadaService) {
		this.nadaService = nadaService;
	}

	public void setIce2sigsService(Ice2sigsService ice2sigsService) {
		this.ice2sigsService = ice2sigsService;
	}

	public void setTipoCambioService(TipoCambioDolarGSService tipoCambioService) {
		this.tipoCambioService = tipoCambioService;
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

	public boolean isExito() {
		return exito;
	}

	public void setExito(boolean exito) {
		this.exito = exito;
	}

	public File getCenso() {
		return censo;
	}

	public void setCenso(File censo) {
		this.censo = censo;
	}

	public String getCensoFileName() {
		return censoFileName;
	}

	public void setCensoFileName(String censoFileName) {
		this.censoFileName = censoFileName;
	}

	public String getCensoContentType() {
		return censoContentType;
	}

	public void setCensoContentType(String censoContentType) {
		this.censoContentType = censoContentType;
	}

	public List<Map<String, Object>> getOlist1() {
		return olist1;
	}

	public void setOlist1(List<Map<String, Object>> olist1) {
		this.olist1 = olist1;
	}

	public void setCotizacionManager(CotizacionManager cotizacionManager) {
		this.cotizacionManager = cotizacionManager;
	}

	public void setSiniestrosManager(SiniestrosManager siniestrosManager) {
		this.siniestrosManager = siniestrosManager;
	}

	public void setAgentePorFolioService(AgentePorFolioService agentePorFolioService) {
		this.agentePorFolioService = agentePorFolioService;
	}

	public void setServiciosManager(ServiciosManager serviciosManager) {
		this.serviciosManager = serviciosManager;
	}
	
	public static void main(String[] args)
	{
		FTPSUtils.downloadChildrenFiles("10.1.1.134", "weblogic", "weblogic123", "/home/jtezva/Escritorio/destino", "/export/home/weblogic/origen");
	}

	public FlujoVO getFlujo() {
		return flujo;
	}

	public void setFlujo(FlujoVO flujo) {
		this.flujo = flujo;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getSaMed() {
		return saMed;
	}

	public void setSaMed(String saMed) {
		this.saMed = saMed;
	}
	
	public String getRutaServidorReports() {
		return rutaServidorReports;
	}

	public String getPassServidorReports() {
		return passServidorReports;
	}

	public String getSigsFacultaDatosPolizaSicapsUrl() {
		return sigsFacultaDatosPolizaSicapsUrl;
	}

	public String getRutaDocumentosPoliza() {
		return rutaDocumentosPoliza;
	}

	public String getRutaDocumentosTemporal() {
		return rutaDocumentosTemporal;
	}

	public String getUserServerLayouts() {
		return userServerLayouts;
	}

	public String getPassServerLayouts() {
		return passServerLayouts;
	}

	public String getDirectorioServerLayouts() {
		return directorioServerLayouts;
	}

	public String getDominioServerLayouts() {
		return dominioServerLayouts;
	}

	public String getDominioServerLayouts2() {
		return dominioServerLayouts2;
	}

	public String getSigsObtenerDatosPorSucRamPolUrl() {
		return sigsObtenerDatosPorSucRamPolUrl;
	}
	
}