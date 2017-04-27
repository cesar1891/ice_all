package mx.com.gseguros.portal.cotizacion.controller;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionContext;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.kernel.service.KernelManagerSustituto;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.mesacontrol.model.FlujoVO;
import mx.com.gseguros.mesacontrol.service.FlujoMesaControlManager;
import mx.com.gseguros.portal.consultas.service.ConsultasManager;
import mx.com.gseguros.portal.consultas.service.ConsultasPolizaManager;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaImapSmapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSlist2SmapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSlistSmapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSlistVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSmapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaVoidVO;
import mx.com.gseguros.portal.cotizacion.service.CotizacionAutoManager;
import mx.com.gseguros.portal.cotizacion.service.CotizacionManager;
import mx.com.gseguros.portal.general.util.TipoSituacion;
import mx.com.gseguros.portal.general.util.TipoTramite;
import mx.com.gseguros.utils.HttpUtil;
import mx.com.gseguros.utils.Utils;

public class CotizacionAutoAction extends PrincipalCoreAction
{
    private static final long   serialVersionUID = -5890606583100529056L;
    private static final Logger logger           = LoggerFactory.getLogger(CotizacionAutoAction.class);
    
    private CotizacionAutoManager cotizacionAutoManager;
    private KernelManagerSustituto kernelManager;
    
    private Map<String,String>       smap1            = null;
    private Map<String,String>       smap2            = null;
    private String                   respuesta;
    private String                   respuestaOculta  = null;
    private boolean                  exito           = false;
    private Map<String,Item>         imap             = null;
    private List<Map<String,String>> slist1           = null;
    private List<Map<String,String>> slist2           = null;
    private List<Map<String,String>> slist3           = null;
    private File                     excel            = null;
    private String                   excelFileName    = null;
    private String                   excelContentType = null;
    private DateFormat renderFechas = new SimpleDateFormat("dd/MM/yyyy");
    
    @Autowired
    private ConsultasManager consultasManager;
    
    private FlujoVO flujo;
    
    @Autowired
    private FlujoMesaControlManager flujoMesaControlManager;
    
    @Autowired
    private CotizacionManager cotizacionManager;
    
    @Autowired
	private ConsultasPolizaManager consultasPolizaManager;
    
	@Value("${sigs.facultaDatosPolizaSicaps.url}")
    private String sigsFacultaDatosPolizaSicapsUrl;	
    
    @Value("${ruta.documentos.temporal}")
    private String rutaDocumentosTemporal;
    
    @Value("${sigs.obtenerDatosPorSucRamPol.url}")
    private String sigsObtenerDatosPorSucRamPolUrl;
    
    @Value("${ruta.servidor.reports}")
    private String rutaServidorReports;

    @Value("${pass.servidor.reports}")
    private String passServidorReports; 
    
    /**
     * Constructor que se asegura de que el action tenga sesion
     */
    public CotizacionAutoAction()
    {
        logger.debug("new CotizacionAutoAction");
        this.session=ActionContext.getContext().getSession();
    }
    
    public String cotizacionAutoIndividual()
    {
        logger.debug(Utils.log(""
                ,"\n######################################"
                ,"\n###### cotizacionAutoIndividual ######"
                ,"\n###### smap1=" , smap1
                ,"\n###### flujo=" , flujo
                ));
        
        String result = ERROR;
        
        try
        {
            UserVO usuario = Utils.validateSession(session);
            
            boolean renovacion = false;
            
            if(flujo!=null)
            {
                logger.debug(Utils.log("", "se va a crear el smap1 porque se entra desde flujo=", smap1));
                
                smap1 = new HashMap<String,String>();
                smap1.put("ntramite" , flujo.getNtramite());
                smap1.put("cdunieco" , flujo.getCdunieco());
                smap1.put("cdramo"   , flujo.getCdramo());
                
                logger.debug(Utils.log("", "recuperando tramite"));
                
                Map<String,Object> datosFlujo = flujoMesaControlManager.recuperarDatosTramiteValidacionCliente(flujo);
                
                Map<String,String> tramite = (Map<String,String>)datosFlujo.get("TRAMITE");
                logger.debug(Utils.log("", "tramite=", tramite));
                
                smap1.put("cdtipsit" , tramite.get("CDTIPSIT"));
                
                logger.debug(Utils.log("", "smap1 creado=", smap1));
                
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
            
            String cdusuari  = usuario.getUser()
                   ,cdsisrol = usuario.getRolActivo().getClave();
            
            smap1.put("cdusuari" , cdusuari);
            smap1.put("cdsisrol" , cdsisrol);
            
            Utils.validate(smap1,"No se recibieron datos");
            
            String ntramite  = smap1.get("ntramite")
                   ,cdunieco = smap1.get("cdunieco")
                   ,cdramo   = smap1.get("cdramo")
                   ,cdtipsit = smap1.get("cdtipsit");
            
            Utils.validate(
                    cdramo    , "No se recibi\u00f3 el producto"
                    ,cdtipsit , "No se recibi\u00f3 la modalidad"
                    );
            
            Map<String,Object> resp = cotizacionAutoManager.cotizacionAutoIndividual(
                    ntramite
                    ,cdunieco
                    ,cdramo
                    ,cdtipsit
                    ,cdusuari
                    ,cdsisrol
                    ,flujo
                    ,renovacion
                    );
            
            smap1.putAll((Map<String,String>)resp.get("smap"));
            
            imap = (Map<String,Item>)resp.get("items");
            
            exito = true;
            
            result = SUCCESS;
        }
        catch(Exception ex)
        {
            respuesta = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(""
                ,"\n###### exito="  , exito
                ,"\n###### result=" , result
                ,"\n###### cotizacionAutoIndividual ######"
                ,"\n######################################"
                ));
        return result;
    }
    
    public String cargarRetroactividadSuplemento()
    {
        logger.debug(Utils.log(""
                ,"\n############################################"
                ,"\n###### cargarRetroactividadSuplemento ######"
                ,"\n###### smap1=", smap1
                ));
        
        logger.debug(Utils.log("","Validando datos de entrada"));
        
        try
        {
            Utils.validate(smap1, "No se recibieron datos");
            
            String cdunieco = smap1.get("cdunieco")
                   ,cdramo   = smap1.get("cdramo")
                   ,cdtipsup = smap1.get("cdtipsup")
                   ,cdusuari = smap1.get("cdusuari")
                   ,cdtipsit = smap1.get("cdtipsit");
            
            Utils.validate(
                    cdunieco  , "No se recibi\u00f3 la sucursal"
                    ,cdramo   , "No se recibi\u00f3 el producto"
                    ,cdtipsup , "No se recibi\u00f3 el tipo de suplemento"
                    ,cdusuari , "No se recibi\u00f3 el nombre de usuario"
                    ,cdtipsit , "No se recibi\u00f3 la modalidad"
                    );
            
            ManagerRespuestaSmapVO resp=cotizacionAutoManager.cargarRetroactividadSuplemento(cdunieco,cdramo,cdtipsup,cdusuari,cdtipsit);
            
            exito     = resp.isExito();
            respuesta = resp.getRespuesta();
            
            if(!exito)
            {
                throw new ApplicationException(respuesta);
            }
            
            smap1.putAll(resp.getSmap());
        }
        catch(Exception ex)
        {
            respuesta = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(""
                ,"\n###### cargarRetroactividadSuplemento ######"
                ,"\n############################################"
                ));
        return SUCCESS;
    }
    
    public String cargarSumaAseguradaRamo5()
    {
        logger.debug(Utils.log(""
                ,"\n######################################"
                ,"\n###### cargarSumaAseguradaRamo5 ######"
                ,"\n###### smap1=", smap1
                ));
        
        logger.debug(Utils.log("","Validando datos de entrada"));
        
        try
        {
            Utils.validate(smap1, "No se recibieron datos");
            
            String cdtipsit = smap1.get("cdtipsit")
                   ,clave    = smap1.get("clave")
                   ,modelo   = smap1.get("modelo")
                   ,cdsisrol = smap1.get("cdsisrol");
            
            Utils.validate(
                    cdtipsit  , "No se recibi\u00f3 la modalidad"
                    ,clave    , "No se recibi\u00f3 la clave del auto"
                    ,modelo   , "No se recibi\u00f3 el modelo del auto"
                    ,cdsisrol , "No se recibi\u00f3 el rol del usuario"
                    );
            
            ManagerRespuestaSmapVO resp = cotizacionAutoManager.cargarSumaAseguradaRamo5(cdtipsit,clave,modelo,cdsisrol);
            
            exito     = resp.isExito();
            respuesta = resp.getRespuesta();
            if(!exito)
            {
                throw new ApplicationException(respuesta);
            }
            
            smap1.putAll(resp.getSmap());
        }
        catch(Exception ex)
        {
            respuesta = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(""
                ,"\n###### exito=" , exito
                ,"\n###### smap1=" , smap1
                ,"\n###### cargarSumaAseguradaRamo5 ######"
                ,"\n######################################"
                ));
        return SUCCESS;
    }
    
    public String emisionAutoIndividual()
    {
        logger.debug(Utils.log(""
                ,"\n###################################"
                ,"\n###### emisionAutoIndividual ######"
                ,"\n###### flujo=" , flujo
                ,"\n###### smap1=" , smap1
                ));
        
        String result = ERROR;
        
        try
        {
            UserVO usuario = Utils.validateSession(session);
            
            if(flujo!=null && smap1==null)
            {
                smap1 = new HashMap<String,String>();
                smap1.put("cdunieco" , flujo.getCdunieco());
                smap1.put("cdramo"   , flujo.getCdramo());
                smap1.put("estado"   , flujo.getEstado());
                
                logger.debug(Utils.log("", "recuperando tramite"));
                
                Map<String,Object> datosFlujo = flujoMesaControlManager.recuperarDatosTramiteValidacionCliente(flujo);
                
                Map<String,String> tramite = (Map<String,String>)datosFlujo.get("TRAMITE");
                logger.debug(Utils.log("", "tramite=", tramite));
                
                smap1.put("cdtipsit" , tramite.get("CDTIPSIT"));
                
                smap1.put("nmpoliza" , tramite.get("NMSOLICI"));
                
                smap1.put("ntramite" , flujo.getNtramite());
                
                smap1.put("swexiper" , cotizacionManager.recuperarOtvalorTramitePorDsatribu(flujo.getNtramite(), "SWEXIPER"));
                
                logger.debug(Utils.log("", "smap1 creado=", smap1));
                
            }
            
            //si viene de la pantalla de cotizacion hay que recuperar los atributos de la pantalla actual
            if(flujo!=null)
            {
                if("RECUPERAR".equals(flujo.getAux()))
                {
                    //se recibieron 3 propiedades de una pantalla anterior, hay que actualizarlas
                    logger.debug("flujo antes de actualizar sus 3 propiedades de pantalla nueva={}",flujo);
                    flujoMesaControlManager.recuperarPropiedadesDePantallaComponenteActualPorConexionSinPermisos(flujo);
                    
                    logger.debug("flujo despues de actualizar sus 3 propiedades de pantalla nueva={}",flujo);
                }
            }
            
            Utils.validate(smap1, "No se recibieron datos");
            
            // Asignamos valor a smap1.cdunieco si viene vacio: 
            if(StringUtils.isBlank(smap1.get("cdunieco"))) {
                smap1.put("cdunieco", flujo.getCdunieco());
                logger.debug("Nuevo valor de smap1.cdunieco: {}", smap1.get("cdunieco"));
            }
            String cdunieco = smap1.get("cdunieco")
                   ,cdramo   = smap1.get("cdramo")
                   ,cdtipsit = smap1.get("cdtipsit")
                   ,estado   = smap1.get("estado")
                   ,nmpoliza = smap1.get("nmpoliza")
                   ,ntramite = smap1.get("ntramite");
            
            Utils.validate(
                    cdunieco , "No se recibi\u00f3 la sucursal"
                    ,cdramo   , "No se recibi\u00f3 el ramo"
                    ,cdtipsit , "No se recibi\u00f3 la modalidad"
                    ,estado   , "No se recibi\u00f3 el estado de la poliza"
                    ,nmpoliza , "No se recibi\u00f3 el numero de poliza"
                    ,ntramite , "No se recibi\u00f3 el numero de tramite"
                    );
            
            String cdusuari  = usuario.getUser()
                   ,cdsisrol = usuario.getRolActivo().getClave();
            
            ManagerRespuestaImapSmapVO resp=cotizacionAutoManager.emisionAutoIndividual(cdunieco,cdramo,cdtipsit,estado,nmpoliza,ntramite,cdusuari,cdsisrol);
            exito           = resp.isExito();
            respuesta       = resp.getRespuesta();
            
            if(!exito)
            {
                throw new ApplicationException(respuesta);
            }
            
            smap1.putAll(resp.getSmap());
            smap1.put("cdsisrol" , cdsisrol);
            imap = resp.getImap();
            
            HashMap<String,String> params = new HashMap<String, String>();
            params.put("cdunieco" , cdunieco);
            params.put("cdramo"   , cdramo);
            params.put("estado"   , estado);
            params.put("nmpoliza" , nmpoliza);
            params.put("nmsuplem" , "0");
            
            Map<String,String> fechas = consultasManager.consultaFeNacContratanteAuto(params);
            
            if(fechas != null && !fechas.isEmpty())
            {
                smap1.put("AplicaCobVida" , fechas.get("APLICA"));
                smap1.put("FechaMinEdad"  , fechas.get("FECHAMIN"));
                smap1.put("FechaMaxEdad"  , fechas.get("FECHAMAX"));
            }
            
            result = SUCCESS;
        }
        catch(Exception ex)
        {
            respuesta = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(""
                ,"\n###### result=", result
                ,"\n###### emisionAutoIndividual ######"
                ,"\n###################################"
                ));
        return result;
    }
    
    public String cargarDatosComplementariosAutoInd()
    {
        logger.debug(Utils.log(""
                ,"\n###############################################"
                ,"\n###### cargarDatosComplementariosAutoInd ######"
                ,"\n###### smap1=", smap1
                ));
        
        try
        {
            logger.debug(Utils.log("","Validando datos de entrada"));
            
            Utils.validate(smap1, "No se recibieron datos");
            
            String cdunieco  = smap1.get("cdunieco")
                   ,cdramo   = smap1.get("cdramo")
                   ,estado   = smap1.get("estado")
                   ,nmpoliza = smap1.get("nmpoliza");
            
            Utils.validate(
                    cdunieco , "No se recibi\u00f3 la sucursal"
                    ,cdramo   , "No se recibi\u00f3 el producto"
                    ,estado   , "No se recibi\u00f3 el estado"
                    ,nmpoliza , "No se recibi\u00f3 la poliza"
                    );
            
            ManagerRespuestaSmapVO resp = cotizacionAutoManager.cargarDatosComplementariosAutoInd(cdunieco,cdramo,estado,nmpoliza);
            
            exito     = resp.isExito();
            respuesta = resp.getRespuesta();
            
            if(!exito)
            {
                throw new ApplicationException(respuesta);
            }
            
            smap1.putAll(resp.getSmap());
        }
        catch(Exception ex)
        {
            respuesta = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(""
                ,"\n###### exito=" , exito
                ,"\n###### smap1=" , smap1
                ,"\n###### cargarDatosComplementariosAutoInd ######"
                ,"\n###############################################"
                ));
        return SUCCESS;
    }
    
    public String cargarValoresSituacion()
    {
        logger.debug(Utils.log(""
                ,"\n####################################"
                ,"\n###### cargarValoresSituacion ######"
                ,"\n###### smap1=", smap1
                ));
        
        try
        {
            logger.debug(Utils.log("","Validando datos de entrada"));
            
            Utils.validate(smap1, "No se recibieron datos");
            
            String cdunieco  = smap1.get("cdunieco")
                   ,cdramo   = smap1.get("cdramo")
                   ,estado   = smap1.get("estado")
                   ,nmpoliza = smap1.get("nmpoliza")
                   ,nmsituac = smap1.get("nmsituac");
            
            Utils.validate(
                    cdunieco  , "No se recibi\u00f3 la sucursal"
                    ,cdramo   , "No se recibi\u00f3 el producto"
                    ,estado   , "No se recibi\u00f3 el estado"
                    ,nmpoliza , "No se recibi\u00f3 la poliza"
                    ,nmsituac , "No se recibi\u00f3 el numero de situacion"
                    );
            
            ManagerRespuestaSmapVO resp = cotizacionAutoManager.cargarValoresSituacion(cdunieco,cdramo,estado,nmpoliza,nmsituac);
            
            exito     = resp.isExito();
            respuesta = resp.getRespuesta();
            
            if(!exito)
            {
                throw new ApplicationException(respuesta);
            }
            
            smap1=resp.getSmap();
        }
        catch(Exception ex)
        {
            respuesta = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(""
                ,"\n###### exito=" , exito
                ,"\n###### smap1=" , smap1
                ,"\n###### cargarValoresSituacion ######"
                ,"\n####################################"
                ));
        return SUCCESS;
    }
    
    public String movimientoMpoliper()
    {
        logger.debug(Utils.log(""
                ,"\n################################"
                ,"\n###### movimientoMpoliper ######"
                ,"\n###### smap1=", smap1
                ));
        
        try
        {
        	
            logger.debug(Utils.log("","Validando datos de entrada"));
            
            Utils.validate(smap1, "No se recibieron datos de entrada");
            
            String cdunieco = smap1.get("cdunieco")
                   ,cdramo   = smap1.get("cdramo")
                   ,estado   = smap1.get("estado")
                   ,nmpoliza = smap1.get("nmpoliza")
                   ,nmsituac = smap1.get("nmsituac")
                   ,cdrol    = smap1.get("cdrol")
                   ,cdperson = smap1.get("cdperson")
                   ,nmsuplem = smap1.get("nmsuplem")
                   ,status   = smap1.get("status")
                   ,nmorddom = smap1.get("nmorddom")
                   ,swreclam = smap1.get("swreclam")
                   ,accion   = smap1.get("accion")
                   ,swexiper = smap1.get("swexiper");
            
            Utils.validate(
                    cdunieco , "No se recibi\u00f3 la sucursal"
                    ,cdramo   , "No se recibi\u00f3 el producto"
                    ,estado   , "No se recibi\u00f3 el estado"
                    ,nmpoliza , "No se recibi\u00f3 el numero de poliza"
                    ,nmsituac , "No se recibi\u00f3 el numero de situacion"
                    ,cdrol    , "No se recibi\u00f3 el rol"
                    ,cdperson , "No se recibi\u00f3 la clave de persona"
                    ,nmsuplem , "No se recibi\u00f3 el suplemento"
                    ,status   , "No se recibi\u00f3 el status"
                    ,nmorddom , "No se recibi\u00f3 el numero ordinal"
                    ,accion   , "No se recibi\u00f3 la accion"
                    ,swexiper , "No se recibi\u00f3 el estado de persona existente"
                    );
            
            Map<String, String> respTvalopol= new HashMap<String, String>();
    		respTvalopol= consultasPolizaManager.obtieneTvalopol(cdunieco, cdramo, estado, nmpoliza);//Rescatamos RPF
            
            ManagerRespuestaVoidVO resp = cotizacionAutoManager.movimientoMpoliper(
                    cdunieco
                    ,cdramo
                    ,estado
                    ,nmpoliza
                    ,nmsituac
                    ,cdrol
                    ,cdperson
                    ,nmsuplem
                    ,status
                    ,nmorddom
                    ,swreclam
                    ,accion
                    ,swexiper);
            
            exito = resp.isExito();
            
            if(!exito)
            {
                throw new ApplicationException(resp.getRespuesta());
            }
            
          //Parche para no perder RPF hasta modificar SP
    		try {
    			Map<String, String> respTvalopolEmitidoSinRPF= new HashMap<String, String>();
    			respTvalopolEmitidoSinRPF= consultasPolizaManager.obtieneTvalopol(cdunieco,cdramo,estado,nmpoliza);//Rescatamos RPF
    			respTvalopolEmitidoSinRPF.put("parametros.pv_otvalor17", respTvalopol.get("parametros.pv_otvalor17"));
    			kernelManager.pMovTvalopol(respTvalopolEmitidoSinRPF);
    		} catch (Exception e) {
    			logger.error("Error al grabar tavolopol con valor RPF");
    		}
        }
        catch(Exception ex)
        {
            respuesta = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(""
                ,"\n###### movimientoMpoliper ######"
                ,"\n################################"
                ));
        return SUCCESS;
    }
    
    public String guardarComplementariosAutoIndividual()
    {
        logger.debug(Utils.log(""
                ,"\n##################################################"
                ,"\n###### guardarComplementariosAutoIndividual ######"
                ,"\n###### smap1=" , smap1
                ,"\n###### smap2=" , smap2
                ));
        
        try
        {
            logger.debug(Utils.log("","Validando datos de entrada"));
            
            Utils.validate(smap1, "No se recibieron datos de poliza");
            
            Utils.validate(smap2, "No se recibieron datos adicionales de poliza");
            
            String cdunieco     = smap1.get("cdunieco")
                   ,cdramo      = smap1.get("cdramo")
                   ,estado      = smap1.get("estado")
                   ,nmpoliza    = smap1.get("nmpoliza")
                   ,agenteSec   = smap1.get("agente_sec")
                   ,porpartiSec = smap1.get("porparti")
                   ,feini       = smap1.get("feini")
                   ,fefin       = smap1.get("fefin")
                   ,ntramite    = smap1.get("ntramite")
                   ,cdagente    = smap1.get("cdagente");
            
            Utils.validate(
                    cdunieco  , "No se recibi\u00f3 la sucursal"
                    ,cdramo   , "No se recibi\u00f3 el producto"
                    ,estado   , "No se recibi\u00f3 el estado de la poliza"
                    ,nmpoliza , "No se recibi\u00f3 el numero de poliza"
                    ,feini    , "No se recibi\u00f3 la fecha de inicio"
                    ,fefin    , "No se recibi\u00f3 la fecha de fin"
                    ,ntramite , "No se recibi\u00f3 el numero de tramite"
                    ,cdagente , "No se recibi\u00f3 el agente"
                    );
            
            ManagerRespuestaVoidVO resp = cotizacionAutoManager.guardarComplementariosAutoIndividual(
                    cdunieco
                    ,cdramo
                    ,estado
                    ,nmpoliza
                    ,agenteSec
                    ,porpartiSec
                    ,feini
                    ,fefin
                    ,smap1
                    ,smap2
                    ,ntramite
                    ,cdagente
                    );
            
            exito     = resp.isExito();
            respuesta = resp.getRespuesta();
        }
        catch(Exception ex)
        {
            respuesta = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(""
                ,"\n###### guardarComplementariosAutoIndividual ######"
                ,"\n##################################################"
                ));
        return SUCCESS;
    }
    
    public String guardarConfigCotizacion()
    {
        logger.debug(Utils.log(""
                ,"\n#####################################"
                ,"\n###### guardarConfigCotizacion ######"
                ,"\n###### smap1=", smap1
                ));
        
        try
        {
            logger.debug(Utils.log("","Validando datos de entrada"));
            
            Utils.validate(smap1, "No se recibieron datos");
            
            String cdramo    = smap1.get("cdramo")
                   ,cdtipsit = smap1.get("cdtipsit")
                   ,cdusuari = smap1.get("cdusuari");
            
            Utils.validate(
                    cdramo    , "No se recibi\u00f3 el producto"
                    ,cdtipsit , "No se recibi\u00f3 la modalidad"
                    ,cdusuari , "No se recibi\u00f3 el nombre de usuario"
                    );
            
            ManagerRespuestaVoidVO resp = cotizacionAutoManager.guardarConfiguracionCotizacion(cdramo,cdtipsit,cdusuari,smap1);
            
            exito     = resp.isExito();
            respuesta = resp.getRespuesta();
            
            if(!exito)
            {
                throw new ApplicationException(respuesta);
            }
        }
        catch(Exception ex)
        {
            respuesta = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(""
                ,"\n###### exito="     , exito
                ,"\n###### respuesta=" , respuesta
                ,"\n###### guardarConfigCotizacion ######"
                ,"\n#####################################"
                ));
        return SUCCESS;
    }
    
    public String cargarConfigCotizacion()
    {
        logger.debug(Utils.log(""
                ,"\n####################################"
                ,"\n###### cargarConfigCotizacion ######"
                ,"\n###### smap1=", smap1
                ));
        
        try
        {
            logger.debug(Utils.log("","Validando datos de entrada"));
            
            Utils.validate(smap1, "No se recibieron datos de entrada");
            
            String cdramo    = smap1.get("cdramo")
                   ,cdtipsit = smap1.get("cdtipsit")
                   ,cdusuari = smap1.get("cdusuari");
            
            Utils.validate(
                    cdramo    , "No se recibi\u00f3 el producto"
                    ,cdtipsit , "No se recibi\u00f3 la modalidad"
                    ,cdusuari , "No se recibi\u00f3 el nombre de usuario"
                    );
            
            ManagerRespuestaSmapVO resp = cotizacionAutoManager.cargarConfiguracionCotizacion(cdramo,cdtipsit,cdusuari);
            
            exito     = resp.isExito();
            respuesta = resp.getRespuesta();
            
            if(!exito)
            {
                throw new ApplicationException(respuesta);
            }
            
            smap1 = resp.getSmap();
        }
        catch(Exception ex)
        {
            respuesta = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(""
                ,"\n###### exito="     , exito
                ,"\n###### respuesta=" , respuesta
                ,"\n###### cargarConfigCotizacion ######"
                ,"\n####################################"
                ));
        return SUCCESS;
    }
    
    public String cargarParamerizacionConfiguracionCoberturas()
    {
        logger.debug(Utils.log(""
                ,"\n#########################################################"
                ,"\n###### cargarParamerizacionConfiguracionCoberturas ######"
                ,"\n###### smap1=", smap1
                ));
        
        try
        {
            logger.debug(Utils.log("","Validando datos de entrada"));
            
            Utils.validate(smap1, "No se recibieron datos");
            
            String cdtipsit      = smap1.get("cdtipsit")
                   ,cdsisrol     = smap1.get("cdsisrol")
                   ,negocio      = smap1.get("negocio")
                   ,tipoServicio = smap1.get("tipoServicio")
                   ,modelo       = smap1.get("modelo")
                   ,tipoPersona  = smap1.get("tipoPersona")
                   ,submarca     = smap1.get("submarca")
                   ,clavegs      = smap1.get("clavegs");
            
            Utils.validate(
                    cdtipsit      , "No se recibi\u00f3 la modalidad"
                    ,cdsisrol     , "No se recibi\u00f3 el rol"
                    ,negocio      , "No se recibi\u00f3 el negocio"
                    ,tipoServicio , "No se recibi\u00f3 el tipo de servicio"
                    ,modelo       , "No se recibi\u00f3 el modelo"
                    ,tipoPersona  , "No se recibi\u00f3 el tipo de persona"
                    ,submarca     , "No se recibi\u00f3 la submarca"
                    ,clavegs      , "No se recibi\u00f3 la clave gs"
                    );
            
            ManagerRespuestaSlistVO resp = cotizacionAutoManager.cargarParamerizacionConfiguracionCoberturas(
                    cdtipsit
                    ,cdsisrol
                    ,negocio
                    ,tipoServicio
                    ,modelo
                    ,tipoPersona
                    ,submarca
                    ,clavegs
                    );
            
            exito     = resp.isExito();
            respuesta = resp.getRespuesta();
            
            if(!exito)
            {
                throw new ApplicationException(respuesta);
            }
            
            slist1 = resp.getSlist();
        }
        catch(Exception ex)
        {
            respuesta = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(""
                ,"\n###### exito="     , exito
                ,"\n###### respuesta=" , respuesta
                ,"\n###### cargarParamerizacionConfiguracionCoberturas ######"
                ,"\n#########################################################"
                ));
        return SUCCESS;
    }
    
    public String cotizacionAutoFlotilla()
    {
        logger.debug(Utils.log(""
                ,"\n####################################"
                ,"\n###### cotizacionAutoFlotilla ######"
                ,"\n###### smap1=", smap1
                ,"\n###### flujo=", flujo
                ));
        
        String result = ERROR;
        
        try
        {
            UserVO usuario  = Utils.validateSession(session);
            
            String cdusuari  = usuario.getUser()
                   ,cdsisrol = usuario.getRolActivo().getClave();
            
            boolean renovacion = false;
            
            if(flujo!=null
                &&
                (
                    smap1==null
                    ||!"MARCO_ENDOSOS_GENERAL".equals(smap1.get("pantallaOrigen"))
                )
            )//si hay flujo y (o no hay mapa o hay mapa pero no vengo de endosos)
            {
                //EN ENDOSO NO SE BOORA PORQUE
                //TRAE DATOS DEL MARCO
                
                logger.debug(Utils.log("FLUJO: se creara el mapa porque viene de flujo y no es endoso (es emision)"));
                
                smap1 = new HashMap<String,String>();
                
                smap1.put("cdunieco" , flujo.getCdunieco());
                smap1.put("cdramo"   , flujo.getCdramo());
                smap1.put("cdtipsit" , "AR");
                smap1.put("ntramite" , flujo.getNtramite());
                
                Map<String, String> tflujomc = flujoMesaControlManager.recuperaTflujomc(flujo.getCdflujomc());
                String tipoflot = null,
                       dsflujomc = tflujomc.get("DSFLUJOMC").toUpperCase();
                
                if (dsflujomc.indexOf("PYME") != -1) {
                    tipoflot = "P";
                } else if (dsflujomc.indexOf("FLOT") != -1) {
                    tipoflot = "F";
                } else {
                    throw new ApplicationException("El nombre del flujo (DSFLUJOMC) no contiene PyME o FLOT");
                }
                
                //smap1.put("tipoflot" , flujo.getAux().split(",")[0].split(":")[1]); //primer split= tipoflot:P onComprar:16', segundo split tipoflot P
                smap1.put("tipoflot", tipoflot);
                
                logger.debug(Utils.log("", "el mapa creado desde flujo es=", smap1));
                
                Map<String,Object> datosFlujo = flujoMesaControlManager.recuperarDatosTramiteValidacionCliente(flujo);
                
                Map<String,String> tramite = (Map<String,String>)datosFlujo.get("TRAMITE");
                logger.debug(Utils.log("", "tramite=", tramite));
                
                renovacion = TipoTramite.RENOVACION.getCdtiptra().equals(tramite.get("CDTIPTRA"));
                logger.debug("Es renovacion = {}", renovacion);
                
                if(renovacion)
                {
                    smap1.put("renuniext" , tramite.get("RENUNIEXT"));
                    smap1.put("renramo"   , tramite.get("RENRAMO"));
                    smap1.put("renpoliex" , tramite.get("RENPOLIEX"));
                }
            }
            else
            {
                logger.debug("FLUJO: No entra porque no es flujo o porque es flujo de endoso");
            }
            
            Utils.validate(smap1, "No se recibieron datos");
            
            String endoso = "MARCO_ENDOSOS_GENERAL".equals(smap1.get("pantallaOrigen")) ? "S" : "N";
            
            smap1.put("endoso",endoso);
            
            if(endoso.equals("S"))
            {
                smap1.put("cdramo"   , smap1.get("CDRAMO"));
                smap1.put("tipoflot" , smap1.get("TIPOFLOT"));
                smap1.put("cdtipsit" , "AR");
            }
            
            String cdunieco = smap1.get("cdunieco")
                   ,cdramo  = smap1.get("cdramo")
                   ,cdtipsit = smap1.get("cdtipsit")
                   ,ntramite = smap1.get("ntramite")
                   ,tipoflot = smap1.get("tipoflot"); 
            
            smap1.put("cdsisrol" , cdsisrol);
            smap1.put("cdusuari" , cdusuari);
            
            Utils.validate(
                    cdramo    , "No se recibi\u00f3 el producto"
                    ,cdtipsit , "No se recibi\u00f3 la modalidad"
                    ,tipoflot , "No se recibi\u00f3 el tipo de cotizacion"
                    );
            
            ManagerRespuestaImapSmapVO resp=cotizacionAutoManager.cotizacionAutoFlotilla(
                    cdusuari
                    ,cdsisrol
                    ,cdunieco
                    ,cdramo
                    ,cdtipsit
                    ,ntramite
                    ,tipoflot
                    ,"S".equals(endoso)
                    ,flujo
                    ,renovacion
                    );
            
            if("ARTL".equals(cdtipsit)){
            	smap1.put("cdtipsit","AR");
            	smap1.put("cdtipsit2","TL");
            }
            exito     = resp.isExito();
            respuesta = resp.getRespuesta();
            
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
            respuesta = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(""
                ,"\n###### result=", result
                ,"\n###### cotizacionAutoFlotilla ######"
                ,"\n####################################"
                ));
        return result;
    }
    
    public String cotizarAutosFlotilla()
    {
        logger.debug(Utils.log(""
                ,"\n##################################"
                ,"\n###### cotizarAutosFlotilla ######"
                ,"\n###### smap1="  , smap1
                ,"\n###### slist1=" , slist1
                ,"\n###### slist2=" , slist2
                ,"\n###### slist3=" , slist3
                ,"\n###### flujo="  , flujo
                ));
        
        try
        {
            logger.debug(Utils.log("","Validando datos de entrada"));
                        
            UserVO usuario  = Utils.validateSession(session);
            
            String cdusuari  = usuario.getUser()
                   ,cdsisrol = usuario.getRolActivo().getClave()
                   ,cdelemen = usuario.getEmpresa().getElementoId();
            
            Utils.validate(smap1, "No se recibieron datos de poliza");
            String ntramite ="";
            if(flujo!=null && !flujo.getNtramite().isEmpty())
            {
                if(smap1.get("cdunieco")== null || smap1.get("cdunieco").isEmpty())
                {
                    smap1.put("cdunieco",flujo.getCdunieco());
                }
                ntramite = flujo.getNtramite();
            }
            
            String  cdunieco     = smap1.get("cdunieco")
                   ,cdramo      = smap1.get("cdramo")
                   ,cdtipsit    = smap1.get("cdtipsit")
                   ,estado      = smap1.get("estado")
                   ,nmsolici    = smap1.get("nmpoliza")
                   ,feini       = smap1.get("feini")
                   ,fefin       = smap1.get("fefin")
                   ,cdagente    = smap1.get("cdagente")
                   ,cdpersonCli = smap1.get("cdpersonCli")
                   ,nmorddomCli = smap1.get("nmorddomCli")
                   ,cdideperCli = smap1.get("cdideperCli")
                   ,tipoflot    = smap1.get("tipoflot");
            boolean licencias = "S".equals(smap1.get("licencias"));
                
            Utils.validate(
                     cdunieco , "No se recibi\u00f3 la sucursal"
                    ,cdramo   , "No se recibi\u00f3 el producto"
                    ,cdtipsit , "No se recibi\u00f3 la modalidad"
                    ,estado   , "No se recibi\u00f3 el estado"
                    ,feini    , "No se recibi\u00f3 el inicio de vigencia"
                    ,fefin    , "No se recibi\u00f3 el fin de vigencia"
                    ,cdagente , "No se recibi\u00f3 el agente"
                    ,tipoflot , "No se recibi\u00f3 el tipo de cotizacion"
                    );
            
            Utils.validate(slist1, "No se recibieron las situaciones mixtas");
            Utils.validate(slist2, "No se recibieron las situaciones base");
            Utils.validate(slist3, "No se recibieron las configuraciones de plan");
            
            boolean noTarificar = StringUtils.isNotBlank(smap1.get("notarificar"))&&smap1.get("notarificar").equals("si");
            String modPrim = StringUtils.isNotBlank(smap1.get("modPrim"))?smap1.get("modPrim"):"";
            
            Map<String,String>tvalopol=new HashMap<String,String>();
            for(Entry<String,String>en:smap1.entrySet())
            {
                String key=en.getKey();
                if(key.length()>"tvalopol_".length()
                        &&key.substring(0,"tvalopol_".length()).equals("tvalopol_")
                        )
                {
                    tvalopol.put(Utils.join("otvalor",StringUtils.leftPad(key.substring("tvalopol_".length()),2,"0")),en.getValue());
                }
            }
            if(!licencias)
	            if(tvalopol.get("otvalor05").contains("DOLARES"))
	            {
	                tvalopol.put("otvalor05","2");
	            }
	            else
	            {
	                tvalopol.put("otvalor05","1");
	            }
            else if(licencias){
            	if(tvalopol.get("otvalor05").contains("DOLARES"))
	            {
	                tvalopol.put("otvalor05","2");
	            }
	            else if(tvalopol.get("otvalor05").contains("PESOS"))
	            {
	                tvalopol.put("otvalor05","1");
	            }
            }
            
            Map<String,String>parame = flujoMesaControlManager.tramiteMC(ntramite, nmsolici, cdunieco, cdramo, cdtipsit);
            if(parame.get("Mensaje")!=null)
            {
                logger.debug(Utils.log(
                         "\n##################################"
                        ,"\n###### cotizarAutosFlotilla ######"
                        ,"\n",parame.get("Mensaje")
                        ,"\n##################################"
                        ));
            }
            else if(!parame.isEmpty())
            {
                if(nmsolici== null && parame.get("NMSOLICI") != null)
                {nmsolici = parame.get("NMSOLICI");}
                if(parame.get("CDTIPTRA").equals("21"))
                {
                    String detalles = cotizacionManager.validaDatosAutoSigs(slist1);
                    if(!detalles.isEmpty())
                    throw new ApplicationException(detalles);
                }
            }

            ManagerRespuestaSlistSmapVO resp= new ManagerRespuestaSlistSmapVO();
            if(modPrim.isEmpty())
            {
               resp=cotizacionAutoManager.cotizarAutosFlotilla(
                    cdusuari
                    ,cdsisrol
                    ,cdelemen
                    ,cdunieco
                    ,cdramo
                    ,cdtipsit
                    ,estado
                    ,nmsolici
                    ,feini
                    ,fefin
                    ,cdagente
                    ,cdpersonCli
                    ,nmorddomCli
                    ,cdideperCli
                    ,slist1
                    ,slist2
                    ,slist3
                    ,noTarificar
                    ,tipoflot
                    ,tvalopol
                    ,usuario
                    ,parame.get("RENUNIEXT")
                    ,parame.get("RENRAMO")
                    ,parame.get("RENPOLIEX")
                    ,ntramite
                    );
            }
            else
            { //no es por inciso es por poliza el descuento
                String mensajeModPrim = cotizacionManager.aplicaDescAutos(cdunieco, cdramo, nmsolici, modPrim, "");
                if(!mensajeModPrim.isEmpty())
                {
                    resp.setExito(false);
                    resp.setRespuesta(mensajeModPrim);
                    resp.setRespuestaOculta(mensajeModPrim);
                }
                else
                    resp.setExito(true);
                    resp.setSmap(smap1);
            }
            
            exito     = resp.isExito();
            if(!exito)
            {
                throw new ApplicationException(respuesta);
            }
            
            String columna="",fila="";//pauete
            if(!parame.isEmpty() && parame.get("Mensaje")==null && ("|5|6|16|").lastIndexOf("|"+cdramo+"|")!=-1 && parame.get("RENPOLIEX")!=null && !parame.get("RENPOLIEX").isEmpty())
            {
                List<String> cdtipsits = new ArrayList<String>();
                boolean facultar = true,emergency= false;
                
                for(Map<String,String> tipsit: slist1) 
                {   
                    if(
                          !emergency && facultar 
                        &&!tipsit.get("cdtipsit").equals(TipoSituacion.AUTOS_FRONTERIZOS.getCdtipsit())//AF
                        &&!tipsit.get("cdtipsit").equals(TipoSituacion.AUTOS_PICK_UP.getCdtipsit())//PU
                        &&!tipsit.get("cdtipsit").equals(TipoSituacion.PICK_UP_CARGA.getCdtipsit())//PC
                        &&!tipsit.get("cdtipsit").equals(TipoSituacion.REMOLQUES_INDISTINTOS.getCdtipsit())//RQ
                        &&!tipsit.get("cdtipsit").equals(TipoSituacion.TRACTOCAMIONES_ARMADOS.getCdtipsit())//TC
                        && tipsit.get("parametros.pv_otvalor03").trim().equals("04")//SERVICIO EMERGENCIA 
                      )
                    {
                        emergency= true;
                    }    
                    
                    if(   facultar && !emergency &&
                          (   tipsit.get("cdtipsit").equals(TipoSituacion.PICK_UP_CARGA.getCdtipsit()) 
                           || tipsit.get("cdtipsit").equals(TipoSituacion.CAMIONES_CARGA.getCdtipsit()) 
                          )
                      )
                    {
                        facultar = false;
                    }
                    
                    cdtipsits.add(tipsit.get("cdtipsit"));
                }
                
                ArrayList<String> paqYplan = cargarPoliza(parame.get("RENUNIEXT"), parame.get("RENRAMO"), parame.get("RENPOLIEX"), "paqYplan", cdtipsits, null);
                columna = paqYplan.get(1);//forma Pago
                fila= paqYplan.get(0);//paquete
                
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
                else if(fila.equals("62")) {fila="Semanal";}
                else if(fila.equals("64")) {fila="Contado";} 
                
                List<Map<String,String>> listaResultados= resp.getSlist();
               
                if(modPrim.isEmpty() && facultar && !emergency)
                {  
                	String str = modificaPrimasFlotillas(ntramite, listaResultados, Integer.parseInt(paqYplan.get(0).trim()), paqYplan, cdunieco, cdramo, nmsolici==null?resp.getSmap().get("nmpoliza"):nmsolici , cdtipsits.toString(),parame.get("RENUNIEXT"), parame.get("RENRAMO"), parame.get("RENPOLIEX"));
                    resp.setRespuesta(str.substring(1,(str.length()-1)));
                }
                logger.debug(Utils.log(resp.getRespuesta()));
            }
            if(licencias){
            	resp.setSlist(cotizacionManager.cargarResultadosCotizacionLicenciaFlotilla(cdunieco, cdramo, estado, nmsolici==null?resp.getSmap().get("nmpoliza"):nmsolici));
            }else{
            	resp.setSlist(cotizacionManager.cargarResultadosCotizacionAutoFlotilla(cdunieco, cdramo, estado, nmsolici==null?resp.getSmap().get("nmpoliza"):nmsolici));
            }
            respuesta = resp.getRespuesta();
           
            smap1.putAll(resp.getSmap());
            if(!fila.isEmpty() && !columna.isEmpty())
            {
                smap1.put("fila", fila);
                smap1.put("columna", columna);
            }
            slist1 = resp.getSlist();
        }
        catch(Exception ex)
        {
            respuesta = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(""
                ,"\n###### exito="     , exito
                ,"\n###### respuesta=" , respuesta
                ,"\n###### cotizarAutosFlotilla ######"
                ,"\n##################################"
                ));
        return SUCCESS;
    }
    
    public String cargarValidacionTractocamionRamo5()
    {
        logger.debug(Utils.log(""
                ,"\n###############################################"
                ,"\n###### cargarValidacionTractocamionRamo5 ######"
                ,"\n###### smap1=", smap1
                ));
        
        try
        {
            logger.debug(Utils.log("","Validando datos de entrada"));
            
            Utils.validate(smap1,"No se recibieron datos para el web service");
            
            String poliza = smap1.get("poliza");
            String rfc    = smap1.get("rfc");
            
            Utils.validate(poliza,"No se recibi\u00f3 la poliza tractocamion para el webservice");
            
            ManagerRespuestaVoidVO resp = cotizacionAutoManager.cargarValidacionTractocamionRamo5(poliza,rfc);
            
            exito     = resp.isExito();
            respuesta = resp.getRespuesta();
            
            if(!exito)
            {
                throw new ApplicationException(respuesta);
            }
        }
        catch(Exception ex)
        {
            respuesta = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(""
                ,"\n###### cargarValidacionTractocamionRamo5 ######"
                ,"\n###############################################"
                ));
        return SUCCESS;
    }
    
    public String procesarCargaMasivaFlotilla()
    {
        logger.debug(Utils.log(""
                ,"\n#########################################"
                ,"\n###### procesarCargaMasivaFlotilla ######"
                ,"\n###### smap1="            , smap1
                ,"\n###### excel="            , excel
                ,"\n###### excelFileName="    , excelFileName
                ,"\n###### excelContentType=" , excelContentType
                ));
        
        try
        {
            logger.debug(Utils.log("","Validando datos de entrada"));
            
            Utils.validate(smap1, "No se recibieron datos");
            
            String cdramo    = smap1.get("cdramo")
                   ,cdtipsit = smap1.get("cdtipsit")
                   ,respetar = smap1.get("tomarMasiva")
                   ,tipoflot = smap1.get("tipoflot")
                   ,cdsisrol = ((UserVO)session.get("USUARIO")).getRolActivo().getClave()
                   ,negocio  = smap1.get("negocio");
            
            logger.debug(Utils.join(
                     "\ntipoflot=" , tipoflot
                    ,"\ncdsisrol=" , cdsisrol
                    ,"\nnegociol=" , negocio
                    ));

            Utils.validate(
                     cdramo   , "No se recibi\u00f3 el producto"
                    ,cdtipsit , "No se recibi\u00f3 la modalidad"
                    ,negocio  , "No se recibio negocio"
                    );
            
            Utils.validate(excel, "No se recibi\u00f3 el archivo");
            
            ManagerRespuestaSlistVO resp = cotizacionAutoManager.procesarCargaMasivaFlotilla(cdramo,cdtipsit,respetar,excel,tipoflot);//,tipoflot
            
            exito     = resp.isExito();
            respuesta = resp.getRespuesta();
            
            if(!exito)
            {
                throw new ApplicationException(respuesta);
            }

            //Pone vacio en los valores desc/rec de la lista
            resp.setSlist(cotizacionAutoManager.validaVacioDescRecg(resp.getSlist()));
            
            //Elimina incisos que no correspondan al negocio seleccionado
            resp.setSlist(cotizacionAutoManager.validaExcelCdtipsitXNegocio(tipoflot,negocio,resp.getSlist()));
            exito = !resp.getSlist().isEmpty();
            if(!exito)
            {
                if(tipoflot.equals("P"))
                throw new ApplicationException("Sin resultados de vehiculos acorde al negocio seleccionado y Autos PyMES.");
                else if(tipoflot.equals("F"))
                throw new ApplicationException("Sin resultados de vehiculos acorde al negocio seleccionado y Autos Flotilla.");
                else
                throw new ApplicationException("Sin resultados de vehiculos acorde al negocio seleccionado.");
            }            
            
            int lugarMensaje = resp.getSlist().size();
            Map<String, String> msn = resp.getSlist().get(lugarMensaje-1);
            
            if(msn.get("removidos") != null) 
            {
                respuestaOculta =msn.get("removidos");
                resp.getSlist().remove(lugarMensaje-1);
            }
            
            if(resp.getSlist().isEmpty())
            {   
                respuestaOculta="No se agregar�n los incisos por no corresponder al negocio seleccionado.";
                return SUCCESS;
            }
            
            //Para modificar solo PYMES ignorando el valor de vehiculo y haciendo consulta
            if(tipoflot.equals("P"))
            {
                String cdpost = smap1.get("codpos");
                String cambio = smap1.get("cambio");
                logger.debug(cambio);
                resp.setSlist( cotizacionAutoManager.modificadorValorVehPYME(resp.getSlist(),cdsisrol, cdpost, cambio));
            }
            
            slist1 = resp.getSlist();
        }
        catch(Exception ex)
        {
            respuesta = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(""
                ,"\n###### exito="  , exito
                ,"\n###### slist1=" , slist1
                ,"\n###### procesarCargaMasivaFlotilla ######"
                ,"\n#########################################"
                ));
        return SUCCESS;
    }
    
    public String cargarCotizacionAutoFlotilla()
    {
        logger.debug(Utils.log(""
                ,"\n##########################################"
                ,"\n###### cargarCotizacionAutoFlotilla ######"
                ,"\n###### smap1=",smap1
                ));
        
        try
        {
            logger.debug(Utils.log("","Validando datos de entrada"));
            
            Utils.validate(smap1, "No se recibieron datos");
            
            String cdramo      = smap1.get("cdramo")
                   ,nmpoliza   = smap1.get("nmpoliza")
                   ,ntramiteIn = smap1.get("ntramiteIn");
            
            Utils.validate(
                    cdramo    , "No se recibi\u00f3 el producto"
                    ,nmpoliza , "No se recibi\u00f3 el numero de poliza");
            
            UserVO usuario = Utils.validateSession(session);
            
            String cdusuari  = usuario.getUser()
                   ,cdsisrol = usuario.getRolActivo().getClave();
            
            ManagerRespuestaSlist2SmapVO resp = cotizacionAutoManager.cargarCotizacionAutoFlotilla(cdramo,nmpoliza,cdusuari,cdsisrol,ntramiteIn);
            
            exito     = resp.isExito();
            respuesta = resp.getRespuesta();
            
            if(!exito)
            {
                throw new ApplicationException(respuesta);
            }
            
            smap1.putAll(resp.getSmap());
            slist1 = resp.getSlist1();
            slist2 = resp.getSlist2();
        }
        catch(Exception ex)
        {
            respuesta = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(""
                ,"\n###### exito="     , exito
                ,"\n###### respuesta=" , respuesta
                ,"\n###### cargarCotizacionAutoFlotilla ######"
                ,"\n##########################################"
                ));
        return SUCCESS;
    }
    
    public String emisionAutoFlotilla()
    {
        logger.debug(Utils.log(""
                ,"\n#################################"
                ,"\n###### emisionAutoFlotilla ######"
                ,"\n###### smap1=", smap1
                ,"\n###### flujo=", flujo
                ));
        
        String result = ERROR;
        
        try
        {
            UserVO usuario = Utils.validateSession(session);
            
            String cdusuari = usuario.getUser()
                   ,cdsisrol = usuario.getRolActivo().getClave();
            
            if(flujo!=null  && smap1==null) //cuando viene desde la mesa de control
            {
                logger.debug(Utils.log("", "se creara el mapa porque viene de flujo"));
                
                smap1 = new HashMap<String,String>();
                
                smap1.put("cdunieco" , flujo.getCdunieco());
                smap1.put("cdramo"   , flujo.getCdramo());
                smap1.put("cdtipsit" , "AR");
                smap1.put("estado"   , flujo.getEstado());
                
                logger.debug(Utils.log("", "recuperando tramite"));
                
                Map<String,Object> datosFlujo = flujoMesaControlManager.recuperarDatosTramiteValidacionCliente(flujo);
                
                Map<String,String> tramite = (Map<String,String>)datosFlujo.get("TRAMITE");
                logger.debug(Utils.log("", "tramite=", tramite));
                
                smap1.put("nmpoliza" , tramite.get("NMSOLICI"));
                
                smap1.put("ntramite" , flujo.getNtramite());
                
                smap1.put("tipoflot" , flujo.getAux());
                
                smap1.put("swexiper" , cotizacionManager.recuperarOtvalorTramitePorDsatribu(flujo.getNtramite(), "SWEXIPER"));
                
                logger.debug(Utils.log("", "el mapa creado desde flujo es=", smap1));
                
            }
            
            //si viene de la pantalla de cotizacion hay que recuperar los atributos de la pantalla actual
            if(flujo!=null)
            {
                if("RECUPERAR".equals(flujo.getAux()))
                {
                    //se recibieron 3 propiedades de una pantalla anterior, hay que actualizarlas
                    logger.debug("flujo antes de actualizar sus 3 propiedades de pantalla nueva={}",flujo);
                    flujoMesaControlManager.recuperarPropiedadesDePantallaComponenteActualPorConexionSinPermisos(flujo);
                    
                    logger.debug("flujo despues de actualizar sus 3 propiedades de pantalla nueva={}",flujo);
                }
            }
            
            Utils.validate(smap1, "No se recibieron datos");
            
            // Asignamos valor a smap1.cdunieco si viene vacio: 
            if(StringUtils.isBlank(smap1.get("cdunieco"))) {
                smap1.put("cdunieco", flujo.getCdunieco());
                logger.debug("Nuevo valor de smap1.cdunieco: {}", smap1.get("cdunieco"));
            }
            String cdunieco = smap1.get("cdunieco")
                   ,cdramo   = smap1.get("cdramo")
                   ,cdtipsit = smap1.get("cdtipsit")
                   ,estado   = smap1.get("estado")
                   ,nmpoliza = smap1.get("nmpoliza")
                   ,ntramite = smap1.get("ntramite")
                   ,cdtipsit2 = smap1.get("cdtipsit2");
            
            Utils.validate(
                    cdunieco  , "No se recibi\u00f3 la sucursal"
                    ,cdramo   , "No se recibi\u00f3 el ramo"
                    ,cdtipsit , "No se recibi\u00f3 la modalidad"
                    ,estado   , "No se recibi\u00f3 el estado de la poliza"
                    ,nmpoliza , "No se recibi\u00f3 el numero de poliza"
                    ,ntramite , "No se recibi\u00f3 el numero de tramite"
                    );
            
            ManagerRespuestaImapSmapVO resp = cotizacionAutoManager.emisionAutoFlotilla(
                    cdunieco
                    ,cdramo
                    ,cdtipsit2!=null && !cdtipsit2.trim().equals("") ? "ARTL" : cdtipsit 
                    ,estado
                    ,nmpoliza
                    ,ntramite
                    ,cdusuari
                    ,cdsisrol
                    );
            
            exito     = resp.isExito();
            respuesta = resp.getRespuesta();
            
            if(!exito)
            {
                throw new ApplicationException(respuesta);
            }
            
            smap1.putAll(resp.getSmap());
            smap1.put("cdsisrol" , cdsisrol);
            imap = resp.getImap();
                
            HashMap<String,String> params = new HashMap<String, String>();
            params.put("cdunieco" , cdunieco);
            params.put("cdramo"   , cdramo);
            params.put("estado"   , estado);
            params.put("nmpoliza" , nmpoliza);
            params.put("nmsuplem" , "0");
            
            Map<String,String> fechas = consultasManager.consultaFeNacContratanteAuto(params);
            
            if(fechas != null && !fechas.isEmpty()){
                smap1.put("AplicaCobVida" , fechas.get("APLICA"));
                smap1.put("FechaMinEdad"  , fechas.get("FECHAMIN"));
                smap1.put("FechaMaxEdad"  , fechas.get("FECHAMAX"));
            }
            
            result = SUCCESS;
        }
        catch(Exception ex)
        {
            respuesta = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(""
                ,"\n###### result=", result
                ,"\n###### emisionAutoFlotilla ######"
                ,"\n#################################"
                ));
        return result;
    }
    
    public String guardarComplementariosAutoFlotilla()
    {
        logger.debug(Utils.log(""
                ,"\n################################################"
                ,"\n###### guardarComplementariosAutoFlotilla ######"
                ,"\n###### smap1="  , smap1
                ,"\n###### slist1=" , slist1
                ));
        
        try
        {
            logger.debug(Utils.log("","Validando datos de entrada"));
            
            Utils.validate(smap1  , "No se recibieron datos de poliza");
            
            Utils.validate(slist1 , "No se recibieron los incisos");
            
            String cdunieco     = smap1.get("cdunieco")
                   ,cdramo      = smap1.get("cdramo")
                   ,estado      = smap1.get("estado")
                   ,nmpoliza    = smap1.get("nmpoliza")
                   ,agenteSec   = smap1.get("agente_sec")
                   ,porpartiSec = smap1.get("porparti")
                   ,feini       = smap1.get("feini")
                   ,fefin       = smap1.get("fefin")
                   ,ntramite    = smap1.get("ntramite");
            
            Utils.validate(
                    cdunieco  , "No se recibi\u00f3 la sucursal"
                    ,cdramo   , "No se recibi\u00f3 el producto"
                    ,estado   , "No se recibi\u00f3 el estado de la poliza"
                    ,nmpoliza , "No se recibi\u00f3 el numero de poliza"
                    ,feini    , "No se recibi\u00f3 la fecha de inicio"
                    ,fefin    , "No se recibi\u00f3 la fecha de fin"
                    ,ntramite , "No se recibi\u00f3 el numero de tramite"
                    );
            
            ManagerRespuestaVoidVO resp = cotizacionAutoManager.guardarComplementariosAutoFlotilla(
                    cdunieco
                    ,cdramo
                    ,estado
                    ,nmpoliza
                    ,agenteSec
                    ,porpartiSec
                    ,feini
                    ,fefin
                    ,smap1
                    ,slist1
                    ,ntramite
                    );
            
            exito     = resp.isExito();
            respuesta = resp.getRespuesta();
            
            if(!exito)
            {
                throw new ApplicationException(respuesta);
            }
            
        }
        catch(Exception ex)
        {
            respuesta = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(""
                ,"\n###### exito="     , exito
                ,"\n###### respuesta=" , respuesta
                ,"\n###### guardarComplementariosAutoFlotilla ######"
                ,"\n################################################"
                ));
        return SUCCESS;
    }
    
    public String recotizarAutoFlotilla()
    {
        logger.debug(Utils.log(""
                ,"\n###################################"
                ,"\n###### recotizarAutoFlotilla ######"
                ,"\n###### smap1=",smap1
                ));
        
        try
        {
            logger.debug(Utils.log("","Validando datos de entrada"));
            
            Utils.validate(smap1, "No se recibieron datos");
            
            String cdunieco    = smap1.get("cdunieco")
                   ,cdramo     = smap1.get("cdramo")
                   ,cdtipsit   = smap1.get("cdtipsit")
                   ,estado     = smap1.get("estado")
                   ,nmpoliza   = smap1.get("nmpoliza")
                   ,notarifica = smap1.get("notarifica")
                   ,cdperpag   = smap1.get("cdperpag");
            
            Utils.validate(
                    cdunieco  , "No se recibi\u00f3 la sucursal"
                    ,cdramo   , "No se recibi\u00f3 el producto"
                    ,estado   , "No se recibi\u00f3 el estado"
                    ,nmpoliza , "No se recibi\u00f3 el numero de poliza"
                    ,cdtipsit , "No se recibi\u00f3 la modalidad"
                    ,cdperpag , "No se recibi\u00f3 la forma de pago"
                    );
            
            
            UserVO usuario = Utils.validateSession(session);
            
            String cdusuari  = usuario.getUser()
                   ,cdelemen = usuario.getEmpresa().getElementoId();
            
            ManagerRespuestaSlistVO resp = cotizacionAutoManager.recotizarAutoFlotilla(
                    cdunieco
                    ,cdramo
                    ,estado
                    ,nmpoliza
                    ,"0"
                    ,StringUtils.isNotBlank(notarifica)&&notarifica.equalsIgnoreCase("si")
                    ,cdusuari
                    ,cdelemen
                    ,cdtipsit
                    ,cdperpag
                    );
            
            exito     = resp.isExito();
            respuesta = resp.getRespuesta();
            
            if(!exito)
            {
                throw new ApplicationException(respuesta);
            }
            
            slist1 = resp.getSlist();
        }
        catch(Exception ex)
        {
            respuesta = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(""
                ,"\n###### exito="  , exito
                ,"\n###### slist1=" , slist1
                ,"\n###### recotizarAutoFlotilla ######"
                ,"\n###################################"
                ));
        return SUCCESS;
    }
    
    public String cargarObligatorioTractocamionRamo5()
    {
        logger.debug(Utils.log(""
                ,"\n################################################"
                ,"\n###### cargarObligatorioTractocamionRamo5 ######"
                ,"\n###### smap1=",smap1
                ));
        
        try
        {
            logger.debug(Utils.log("","Validando datos de entrada"));
            
            Utils.validate(smap1, "No se recibieron datos");
            
            String clave = smap1.get("clave");
            Utils.validate(clave, "No se recibi\u00f3 la clave del vehiculo");
            
            ManagerRespuestaSmapVO resp = cotizacionAutoManager.cargarObligatorioTractocamionRamo5(clave);
            
            exito     = resp.isExito();
            respuesta = resp.getRespuesta();
            
            if(!exito)
            {
                throw new ApplicationException(respuesta);
            }
            
            smap1.putAll(resp.getSmap());
        }
        catch(Exception ex)
        {
            respuesta = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(""
                ,"\n###### cargarObligatorioTractocamionRamo5 ######"
                ,"\n################################################"
                ));
        return SUCCESS;
    }
    
    public String cargarDetalleNegocioRamo5()
    {
        logger.debug(Utils.log(""
                ,"\n#######################################"
                ,"\n###### cargarDetalleNegocioRamo5 ######"
                ,"\n###### smap1=", smap1
                ));
        
        try
        {
            logger.debug(Utils.log("","Validando datos de entrada"));
            
            Utils.validate(smap1, "No se recibieron datos para cargar detalle de negocio");
            
            UserVO usuario = Utils.validateSession(session);
            
            String negocio   = smap1.get("negocio")
                   ,cdramo   = smap1.get("cdramo")
                   ,cdtipsit = smap1.get("cdtipsit")
                   ,cdusuari = usuario.getUser()
                   ,cdsisrol = usuario.getRolActivo().getClave();
            
            Utils.validate(negocio, "No se recibi\u00f3 clave de negocio");
            
            ManagerRespuestaSmapVO resp = cotizacionAutoManager.cargarDetalleNegocioRamo5(negocio, cdramo, cdtipsit, cdsisrol, cdusuari);
            
            exito     = resp.isExito();
            respuesta = resp.getRespuesta();
            
            if(!exito)
            {
                throw new ApplicationException(respuesta); 
            }
            
            smap1.putAll(resp.getSmap());
        }
        catch(Exception ex)
        {
            respuesta = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(
                 "\n###### cargarDetalleNegocioRamo5 ######"
                ,"\n#######################################"
                ));
        return SUCCESS;
    }
    
    public String guardarPantallaBeneficiarios()
    {
        logger.debug(Utils.log(""
                ,"\n##########################################"
                ,"\n###### guardarPantallaBeneficiarios ######"
                ,"\n###### smap1="  , smap1
                ,"\n###### slist1=" , slist1
                ));

        try
        {
            logger.debug(Utils.log("","Validando datos de entrada"));
            
            session.put("listaBeneficiarios", slist1);
            Utils.validate(smap1, "No se recibieron datos");
            
            String cdunieco  = smap1.get("cdunieco")
                   ,cdramo   = smap1.get("cdramo")
                   ,estado   = smap1.get("estado")
                   ,nmpoliza = smap1.get("nmpoliza")
                   ,nmsuplem = smap1.get("nmsuplem")
                   ,nmsituac = smap1.get("nmsituac");
            
            Utils.validate(
                    cdunieco  , "No se recibi\u00f3 la sucursal"
                    ,cdramo   , "No se recibi\u00f3 el producto"
                    ,estado   , "No se recibi\u00f3 el estado de la poliza"
                    ,nmpoliza , "No se recibi\u00f3 el numero de poliza"
                    ,nmsuplem , "No se recibi\u00f3 el numero de suplemento"
                    ,nmsituac , "No se recibi\u00f3 el numero de situacion"
                    );
            
            UserVO usuarioSesion = Utils.validateSession(session);
            
            ManagerRespuestaVoidVO resp=cotizacionAutoManager.guardarPantallaBeneficiarios(
                    cdunieco
                    ,cdramo
                    ,estado
                    ,nmpoliza
                    ,nmsuplem
                    ,nmsituac
                    ,slist1
                    ,usuarioSesion);
            
            exito     = resp.isExito();
            respuesta = resp.getRespuesta();
            
            if(!exito)
            {
                throw new ApplicationException(respuesta);
            }
            
        }
        catch(Exception ex)
        {
            respuesta = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(""
                ,"\n###### exito="     , exito
                ,"\n###### respuesta=" , respuesta
                ,"\n###### guardarPantallaBeneficiarios ######"
                ,"\n##########################################"
                ));
        return SUCCESS;
    }
    
    public String cargarParamerizacionConfiguracionCoberturasRol()
    {
        logger.debug(Utils.log(""
                ,"\n############################################################"
                ,"\n###### cargarParamerizacionConfiguracionCoberturasRol ######"
                ,"\n###### smap1=", smap1
                ));
        
        try
        {
            logger.debug(Utils.log("","Validando datos de entrada"));
            
            Utils.validate(smap1, "No se recibieron datos");
            
            String cdtipsit  = smap1.get("cdtipsit")
                   ,cdsisrol = smap1.get("cdsisrol");
            
            Utils.validate(
                    cdtipsit  , "No se recibi\u00f3 la modalidad"
                    ,cdsisrol , "No se recibi\u00f3 el rol"
                    );
            
            ManagerRespuestaSlistVO resp = cotizacionAutoManager.cargarParamerizacionConfiguracionCoberturasRol(cdtipsit,cdsisrol);
            
            exito     = resp.isExito();
            respuesta = resp.getRespuesta();
            
            if(!exito)
            {
                throw new ApplicationException(respuesta);
            }
            
            slist1 = resp.getSlist();
        }
        catch(Exception ex)
        {
            respuesta = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(""
                ,"\n###### exito="  , exito
                ,"\n###### slist1=" , slist1
                ,"\n###### cargarParamerizacionConfiguracionCoberturasRol ######"
                ,"\n############################################################"
                ));
        return SUCCESS;
    }
    
    public String obtieneValNumeroSerie()
    {
        logger.debug(Utils.log(""
                ,"\n###################################"
                ,"\n###### obtieneValNumeroSerie ######"
                ,"\n###### smap1=", smap1
                ));
        
        String paso = null;
        
        try
        {
            paso = "Validando datos de entrada";
            logger.debug(Utils.log("","paso=",paso));
            
            Utils.validate(smap1, "No se recibieron datos de poliza");
            
            String feini     = smap1.get("feini")
                   ,numSerie = smap1.get("numSerie");
            
            Utils.validate(
                    feini     , "No se recibi\u00f3 la fecha inicial"
                    ,numSerie , "No se recibi\u00f3 el numero de serie"
                    );
            
            //String iCodAviso = "exito";
            String iCodAviso = "";
            String CodAviso = "";
            
            String feAutorizacion = Utils.join(feini.substring(8,10),"/",feini.substring(5,7),"/",feini.substring(0,4));
            
            String[] value_split = numSerie.split("\\|");
            int contador= 1;
            for (String serie : value_split) {
            
            iCodAviso = cotizacionAutoManager.obtieneValidacionRetroactividad(serie, renderFechas.parse(feAutorizacion));
            
            if(iCodAviso.trim().length()>0){//Se agrega validacion para iterar si es una carga de flotillas o individual
                
                CodAviso +=  "El inciso "+contador+" "+iCodAviso+ (StringUtils.isNotBlank(iCodAviso) ?"<br/>" : "" );
                
            }
            contador++;
            }
            //CodAviso        += StringUtils.isNotBlank(CodAviso) ?" VERIFIQUE SI PUEDE REALIZAR EL ENDOSO O BIEN SI NECESITA REEXPEDIR LA POLIZA INGRESE TRAMITE MESA DE CONTROL":"";
            CodAviso        += StringUtils.isNotBlank(CodAviso) ?" Verifique si puede realizar el endoso o bien, si necesita reexpedir la p\u00F3liza ingrese el tr\u00E1mite en mesa de control.":"";
            exito           = StringUtils.isNotBlank(CodAviso) ? false : true;
            respuesta       = CodAviso.trim();
            respuestaOculta = CodAviso;
            
            logger.debug("respuesta--->"+respuesta+" : "+CodAviso.length());
        }
        catch(Exception ex)
        {
            respuesta = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(""
                ,"\n###### obtieneValNumeroSerie ######"
                ,"\n###################################"
                ));
        return SUCCESS;
    }

    public String obtieneValidacionDescuentoR6()
    {
        logger.debug(Utils.log(""
                ,"\n##########################################"
                ,"\n###### obtieneValidacionDescuentoR6 ######"
                ,"\n###### smap1=", smap1
                ));
        
        String paso = null;
        
        try
        {
            paso = "Validando datos de entrada";
            logger.debug(Utils.log("","paso=",paso));
            
            Utils.validate(smap1, "No se recibieron datos del auto");
            
            String tipoUnidad     = smap1.get("tipoUnidad")
            ,uso = smap1.get("uso")
            ,cdagente = smap1.get("cdagente")
            ,cdtipsit = smap1.get("cdtipsit")
            ,cdatribu = smap1.get("cdatribu");
            
            Utils.validate(
                    tipoUnidad     , "No se recibi\u00f3 la tipoUnidad"
                    ,uso       , "No se recibi\u00f3 el uso"
                    ,cdagente  , "No se recibi\u00f3 el cdagente"
                    ,cdtipsit  , "No se recibi\u00f3 el cdtipsit"
                    ,cdatribu  , "No se recibi\u00f3 el cdatribu"
                    );
            
            smap1 = cotizacionManager.obtieneValidacionDescuentoR6(tipoUnidad, uso, "9",
                    "13", cdagente, cdtipsit, cdatribu);
            
            logger.debug("Valores maximo y minimo de descuento ramo 6 --->"+smap1);
            
        }
        catch(Exception ex)
        {
            respuesta = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(""
                ,"\n###### obtieneValidacionDescuentoR6 ######"
                ,"\n##########################################"
                ));
        return SUCCESS;
    }
    
    public ArrayList<String> cargarPoliza(String cdunieco, String cdramo, String cdpoliza, String tipoflot, List<String> cdtipsit, String cargaCot)
    {
        logger.debug(Utils.log(
                 "\n###############################"
                ,"\n######## cargarPoliza #########"
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

 public String modificaPrimasFlotillas(String ntramite, List<Map<String, String>> listaResultados, Integer formpagSigs, ArrayList<String> paquete, String cdunieco, String cdramo, String nmpoliza, String cdtipsit, String renuniext, String renramo, String renpoliex) throws Exception
    {
     String mensaje = "Modificacion de primas seg\u00FAn sigs";
            try
            {       int i = 0;
                    String mnprima = null;
                    paquete.remove(0);
//                  Ciclamos paquetes devueltos del ice con relacion a los paquetes devueltos del sigs
                    logger.debug(Utils.log("Forma de pago sigs :",formpagSigs));
//                  for(String paq:paquete)
//                  {   
                        for(Map<String,String>res:listaResultados)
                        {   
                            String dsperpag = res.get("DSPERPAG");
//                          String cdperpag   = res.get("CDPERPAG");
                            if (formpagSigs == 1 && dsperpag.contains("Contado/Anual"))// && paquete.get(i).trim().equals(cdperpag.trim()))
                            {
                                formpagSigs = Integer.parseInt(res.get("CDPERPAG"));
                                mnprima = res.get("PRIMA");break;
                            }
                            else if ( (formpagSigs == 2 || formpagSigs == 5) && dsperpag.contains("Semestral"))// && paquete.get(i).trim().equals(cdperpag.trim()))
                            {
                                formpagSigs = Integer.parseInt(res.get("CDPERPAG"));
                                mnprima = res.get("PRIMA");break;
                            }
                            else if ( (formpagSigs == 3 || formpagSigs == 6) && dsperpag.contains("Trimestral"))// && paquete.get(i).trim().equals(cdperpag.trim()))
                            {
                                formpagSigs = Integer.parseInt(res.get("CDPERPAG"));
                                mnprima = res.get("PRIMA");break;
                            }
                            else if ( (formpagSigs == 4 || formpagSigs == 7) && dsperpag.contains("Mensual"))// && paquete.get(i).trim().equals(cdperpag.trim()))
                            {
                                formpagSigs = Integer.parseInt(res.get("CDPERPAG"));
                                mnprima = res.get("PRIMA");break;
                            }
                        }
//                  }
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
    
    
 public String obtieneFormaPago()
 {
     logger.debug(Utils.log(""
             ,"\n##############################"
             ,"\n###### obtieneFormaPago ######"
             ,"\n###### smap1=", smap1
             ));
     
     String paso = null;
     
     try
     {
         paso = "Validando datos de entrada";
         logger.debug(Utils.log("","paso=",paso));
         
         Utils.validate(smap1, "No se recibieron datos del auto");
         
         String administradora     = smap1.get("administradora")
         ,retenedora = smap1.get("retenedora");
         
         Utils.validate(
                 administradora     , "No se recibi\u00f3 la tipoUnidad"
                 ,retenedora       , "No se recibi\u00f3 el uso"
                 );
         
         smap1=cotizacionManager.obtieneFormapago(administradora, retenedora);
         exito=true;
         logger.debug(Utils.log("Forma de pago:",smap1));
         
     }
     catch(Exception ex)
     {
         exito=false;
         respuesta = Utils.manejaExcepcion(ex);
     }
     
     logger.debug(Utils.log(""
             ,"\n###### obtieneFormaPago ######"
             ,"\n##############################"
             ));
     return SUCCESS;
 }
 
 public String obtieneAdminRet()
 {
     logger.debug(Utils.log(""
             ,"\n##############################"
             ,"\n###### obtieneAdminRet ######"
             ,"\n###### smap1=", smap1
             ));
     
     String paso = null;
     
     try
     {
         paso = "Validando datos de entrada";
         logger.debug(Utils.log("","paso=",paso));
         
         Utils.validate(smap1, "No se recibieron datos del auto");
         
         String administradora     = smap1.get("administradora")
         ,retenedora = smap1.get("retenedora");
         
         Utils.validate(
                 administradora     , "No se recibi\u00f3 administradora"
                 ,retenedora       , "No se recibi\u00f3 retenedora"
                 );
         
         slist1=cotizacionManager.obtieneRetAdmin(administradora, retenedora);
         smap1=slist1.get(0);
                 
         exito=true;
         logger.debug(Utils.log("##### respuesta:",slist1));
         
     }
     catch(Exception ex)
     {
         exito=false;
         respuesta = Utils.manejaExcepcion(ex);
     }
     
     logger.debug(Utils.log(""
             ,"\n###### obtieneAdminRet ######"
             ,"\n##############################"
             ));
     return SUCCESS;
 }
     
 public String aplicaDxn()
 {
     logger.debug(Utils.log(""
             ,"\n#######################################"
             ,"\n###### aplicaDxn ######"
             ,"\n###### smap1=", smap1
             ));
     
     try
     {
         logger.debug(Utils.log("","Validando datos de entrada"));
         
         Utils.validate(smap1, "No se recibieron datos para cargar detalle de negocio");
         
         UserVO usuario = Utils.validateSession(session);
         
         String cdtipsit = smap1.get("cdtipsit")
                ,cdagente = smap1.get("cdagente")
                ,cdusuari = usuario.getUser()
                ,cdsisrol = usuario.getRolActivo().getClave();
         
         Utils.validate(cdtipsit, "No se recibi\u00f3 clave de negocio");
         
         smap1.put("aplicaDxn",cotizacionAutoManager.aplicaDxn(cdtipsit, cdsisrol, cdusuari,cdagente)?"S":"N");
         exito=true;
         
     }
     catch(Exception ex)
     {
         respuesta = Utils.manejaExcepcion(ex);
     }
     
     logger.debug(Utils.log(
              "\n###### aplicaDxn ######"
             ,"\n#######################################"
             ));
     return SUCCESS;
 }
 
 @SuppressWarnings("unchecked")
 public String datosFlujo() throws Exception
 {
     logger.debug(Utils.log(""
             ,"\n######################################"
             ,"\n###### getdatosFlujo ######"
             ,"\n###### flujo=" , flujo
             ));
     String result = ERROR;
     try
     {
         smap1 = new HashMap<String,String>();
         Map<String, Object> tramiteObj = flujoMesaControlManager.recuperarDatosTramiteValidacionCliente(flujo);
         smap1.putAll((Map<String,String>)tramiteObj.get("TRAMITE"));
         exito = true;
     }
     catch(Exception ex)
     {
         exito=false;
         respuesta = Utils.manejaExcepcion(ex);
     }
     logger.debug(Utils.log(""
             ,"\n###### exito="  , exito
             ,"\n###### result=" , result
             ,"\n###### getdatosFlujo ######"
             ,"\n###########################"
             ));
     return SUCCESS;
 }
 
 public String cargaMasivaFlotillaEmision()
 {
     logger.debug(Utils.log(""
             ,"\n#########################################"
             ,"\n###### procesarCargaMasivaFlotilla ######"
             ,"\n###### slist1="           , slist1
             ,"\n###### excel="            , excel
             ,"\n###### excelFileName="    , excelFileName
             ,"\n###### excelContentType=" , excelContentType
             ,"\n###### smap1="            , smap1
             ));
     
     try
     {
         logger.debug(Utils.log("","Validando datos de entrada"));
         Utils.validate(smap1, "No se recibieron datos");
         
         ManagerRespuestaSlistVO resp = new ManagerRespuestaSlistVO();
         if(smap1.get("accion").equals("guardar"))
         {
             String excelTimestamp   = smap1.get("timestamp");
             
             try{
                 String nombreexcel    = "excel_"+excelTimestamp+".xls";
                 File archivoTxt       = new File(this.rutaDocumentosTemporal+"/"+nombreexcel);
                 
                 if(excel!=null&&excel.exists())
                 {
                     try
                     {
                         FileUtils.copyFile(excel, archivoTxt);
                         exito     = true;
                         respuesta = Utils.join("Se esta procesando el archivo. ",archivoTxt,"]");
                         logger.debug(respuesta);
                         return SUCCESS;
                     }
                     catch(Exception ex)
                     {
                         logger.error("Error copiando archivo de usuario",ex);
                     }
                 }
                 else
                 {
                     logger.error(new StringBuilder("No existe el documento").append(excel).toString());
                 }
                 
             } catch(Exception ex){
                 long etimestamp = System.currentTimeMillis();
                 exito           = false;
                 respuesta       = "Error al procesar excel #"+etimestamp;
                 respuestaOculta = ex.getMessage();
                 logger.error(respuesta,ex);
             }
         }

         exito     = resp.isExito();
         respuesta = resp.getRespuesta();
         
         if(!exito)
         {
             throw new ApplicationException(respuesta);
         }

     }
     catch(Exception ex)
     {
         respuesta = Utils.manejaExcepcion(ex);
     }
     
     logger.debug(Utils.log(""
             ,"\n###### exito="  , exito
             ,"\n###### slist1=" , slist1
             ,"\n###### procesarCargaMasivaFlotilla ######"
             ,"\n#########################################"
             ));
     return SUCCESS;
 }
 
 public String obtieneRangoPeriodoGraciaAgente()
 {
     logger.debug(Utils.log(""
             ,"\n#########################################"
             ,"\n###### obtieneRangoPeriodoGraciaAgente ######"
             ,"\n###### slist1="           , slist1
             ,"\n###### smap1="            , smap1
             ));
     
     try
     {
    	 Utils.validate(smap1, "No se recibieron datos");
    	 String cdramo   = smap1.get("cdramo");
    	 String cdtipsit = smap1.get("cdtipsit");
    	 String cdagente = smap1.get("cdagente");
    	 Utils.validate( cdramo  ,"No se recibio cdramo"
    			 		,cdtipsit,"No se recibio cdtipsit"
    			 		,cdagente,"No se recibio cdagente");
         slist1=consultasManager.obtieneRangoPeriodoGracia(cdramo,cdtipsit,cdagente);
         
         
         exito=true;
     }
     catch(Exception ex)
     {
         respuesta = Utils.manejaExcepcion(ex);
     }
     
     logger.debug(Utils.log(""
             ,"\n###### exito="  , exito
             ,"\n###### slist1=" , slist1
             ,"\n###### obtieneRangoPeriodoGraciaAgente ######"
             ,"\n#########################################"
             ));
     return SUCCESS;
 }
 
     /*
     * Getters y setters
     */
    public void setCotizacionAutoManager(CotizacionAutoManager cotizacionAutoManager) {
        this.cotizacionAutoManager = cotizacionAutoManager;
    }
    
    public boolean isSuccess(){
        return true;
    }
    
    public Map<String, String> getSmap1() {
        return smap1;
    }

    public void setSmap1(Map<String, String> smap1) {
        this.smap1 = smap1;
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

    public Map<String, Item> getImap() {
        return imap;
    }

    public void setImap(Map<String, Item> imap) {
        this.imap = imap;
    }

    public Map<String, String> getSmap2() {
        return smap2;
    }

    public void setSmap2(Map<String, String> smap2) {
        this.smap2 = smap2;
    }

    public List<Map<String, String>> getSlist1() {
        return slist1;
    }

    public void setSlist1(List<Map<String, String>> slist1) {
        this.slist1 = slist1;
    }

    public List<Map<String, String>> getSlist2() {
        return slist2;
    }

    public void setSlist2(List<Map<String, String>> slist2) {
        this.slist2 = slist2;
    }

    public List<Map<String, String>> getSlist3() {
        return slist3;
    }

    public void setSlist3(List<Map<String, String>> slist3) {
        this.slist3 = slist3;
    }

    public File getExcel() {
        return excel;
    }

    public void setExcel(File excel) {
        this.excel = excel;
    }

    public String getExcelFileName() {
        return excelFileName;
    }

    public void setExcelFileName(String excelFileName) {
        this.excelFileName = excelFileName;
    }

    public String getExcelContentType() {
        return excelContentType;
    }

    public void setExcelContentType(String excelContentType) {
        this.excelContentType = excelContentType;
    }

    public FlujoVO getFlujo() {
        return flujo;
    }

    public void setFlujo(FlujoVO flujo) {
        this.flujo = flujo;
    }
    
    public String getSigsFacultaDatosPolizaSicapsUrl() {
		return sigsFacultaDatosPolizaSicapsUrl;
	}

	public String getRutaDocumentosTemporal() {
		return rutaDocumentosTemporal;
	}

	public String getSigsObtenerDatosPorSucRamPolUrl() {
		return sigsObtenerDatosPorSucRamPolUrl;
	}

	public String getRutaServidorReports() {
		return rutaServidorReports;
	}

	public String getPassServidorReports() {
		return passServidorReports;
	}
}