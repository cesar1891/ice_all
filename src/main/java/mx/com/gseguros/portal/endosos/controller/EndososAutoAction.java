package mx.com.gseguros.portal.endosos.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionContext;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.mesacontrol.model.FlujoVO;
import mx.com.gseguros.portal.catalogos.service.PersonasManager;
import mx.com.gseguros.portal.consultas.service.ConsultasPolizaManager;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaVoidVO;
import mx.com.gseguros.portal.cotizacion.model.SlistSmapVO;
import mx.com.gseguros.portal.endosos.service.EndososAutoManager;
import mx.com.gseguros.portal.endosos.service.EndososManager;
import mx.com.gseguros.portal.general.util.TipoEndoso;
import mx.com.gseguros.portal.general.util.TipoSituacion;
import mx.com.gseguros.utils.Utils;

@Controller
@Scope("prototype")
public class EndososAutoAction extends PrincipalCoreAction
{
	private static final long serialVersionUID = -7583914370456999908L;

	private static Logger logger = Logger.getLogger(EndososAutoAction.class);
	
	private boolean                  success;
	private String                   respuesta;
	private Map<String,String>       smap1;
	private Map<String,String>       smap2;
	private Map<String,Object>       omap1;
	private Map<String,Item>         imap;
	private List<Map<String,String>> slist1;
	private List<Map<String,String>> slist2;
	private PersonasManager          personasManager;
	private String                   error;
	
	private FlujoVO flujo;
	
	private SimpleDateFormat renderFechas = new SimpleDateFormat("dd/MM/yyyy");
	
	private boolean exito           = false;
	private String  respuestaOculta = null;
	
	@Autowired
	private EndososAutoManager endososAutoManager;
	
	@Autowired
	private EndososManager endososManager;
	
	@Autowired
	private ConsultasPolizaManager consultasPolizaManager;

	@Value("${caratula.impresion.autos.endosob.url}")
    private String caratulaImpresionAutosEndosobUrl;
	
	@Value("${logs.ruta}")
    private String logsRuta;
	
	public EndososAutoAction()
	{
		this.session=ActionContext.getContext().getSession();
	}
	
	public String marcoEndosos()
	{
		logger.debug(Utils.log(
				 "\n##########################"
				,"\n###### marcoEndosos ######"
				,"\n###### smap1=" , smap1
				,"\n###### flujo=" , flujo
				));
		
		String result = ERROR;
		
		try
		{
			Utils.validate(session                , "No hay sesion");
			Utils.validate(session.get("USUARIO") , "No hay usuario en la sesion");
			if(smap1==null)
			{
				smap1=new HashMap<String,String>();
			}
			String cdusuari = ((UserVO)session.get("USUARIO")).getUser();
			String cdsisrol = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
			smap1.put("cdusuari" , ((UserVO)session.get("USUARIO")).getUser());
			smap1.put("cdsisrol" , cdsisrol);
			
			Map<String,Object> valores = endososAutoManager.construirMarcoEndosos(cdusuari,cdsisrol);
			imap = (Map<String,Item>)valores.get("items");
			smap1.put("cdagente" , (String)valores.get("cdagente"));
			
			result = SUCCESS;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### result=",result
				,"\n###### marcoEndosos ######"
				,"\n##########################"
				));
		return result;
	}
	
	public String recuperarColumnasIncisoRamo()
	{
		logger.debug(Utils.log(
				 "\n#########################################"
				,"\n###### recuperarColumnasIncisoRamo ######"
				,"\n###### smap1=",smap1
				));
		
		try
		{
			Utils.validate(smap1  , "No se recibieron datos");
			String cdramo=smap1.get("cdramo");
			Utils.validate(cdramo , "No se recibio el producto");
			
			smap1.put("columnas" , endososAutoManager.recuperarColumnasIncisoRamo(cdramo));
			
			success = true;
		}
		catch(Exception ex)
		{
			respuesta=Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### recuperarColumnasIncisoRamo ######"
				,"\n#########################################"
				));
		return SUCCESS;
	}
	
	public String recuperarEndososClasificados()
	{
		logger.debug(Utils.log(
				 "\n##########################################"
				,"\n###### recuperarEndososClasificados ######"
				,"\n###### smap1="  , smap1
				,"\n###### slist1=" , slist1
				));
		
		try
		{
			Utils.validate(smap1, "No se recibieron datos de entrada");
			String cdramo    = smap1.get("cdramo");
			String nivel     = smap1.get("nivel");
			String multiple  = smap1.get("multiple");
			String tipoflot  = smap1.get("tipoflot");
			String cancelada = smap1.get("cancelada");
			String cdtipsit  = smap1.get("cdtipsit");
			String cdunieco  = smap1.get("cdunieco");
			String estado	 = smap1.get("estado");
			String nmpoliza	 = smap1.get("nmpoliza");
			
			
			Utils.validate(cdramo    , "No se recibio el producto");
			Utils.validate(nivel     , "No se recibio el nivel de endoso");
			Utils.validate(multiple  , "No se recibio el tipo de seleccion");
			Utils.validate(tipoflot  , "No se recibio el tipo de poliza");
			Utils.validate(cancelada , "No se recibio el status de vigencia de poliza");
			Utils.validate(cdtipsit  , "No se recibio la modalidad");
			
			Utils.validate(session                , "No hay sesion");
			Utils.validate(session.get("USUARIO") , "No hay usuario en la sesion");
			String cdsisrol = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
			String cdusuari = ((UserVO)session.get("USUARIO")).getUser();
			
			if(slist1==null)
			{
				slist1=new ArrayList<Map<String,String>>();
			}
			
			SlistSmapVO resp = endososAutoManager.recuperarEndososClasificados(
					cdramo, nivel, multiple, tipoflot, slist1, cdsisrol,
					cancelada, cdusuari,cdtipsit,cdunieco,estado,nmpoliza);
			
			smap1.putAll(resp.getSmap());
			slist1=resp.getSlist();
			
			success = true;
		}
		catch(Exception ex)
		{
			respuesta=Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### recuperarEndososClasificados ######"
				,"\n##########################################"
				));
		return SUCCESS;
	}
	
	public String pantallaEndosoValosit()
	{
		logger.debug(Utils.log(
				 "\n###################################"
				,"\n###### pantallaEndosoValosit ######"
				,"\n###### smap1="  , smap1
				,"\n###### slist1=" , slist1
				,"\n###### flujo="  , flujo
				));
		
		String result=ERROR;
		
		try
		{
			Utils.validate(smap1,"No se recibieron datos de poliza");
			
			String cdtipsup = smap1.get("cdtipsup");
			String cdramo   = smap1.get("CDRAMO");
			Utils.validate(cdramo , "No se recibio el tipo de endoso");
			Utils.validate(cdramo , "No se recibio el producto");
			
			//override del tstamp
			smap1.put("tstamp" , Utils.generaTimestamp());
			
			String cdsisrol = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
			imap = endososAutoManager.pantallaEndosoValosit(cdtipsup,cdramo, cdsisrol);
			result = SUCCESS;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### result="    , result
				,"\n###### respuesta=" , respuesta
				,"\n###### pantallaEndosoValosit ######"
				,"\n###################################"
				));
		return result;
	}
	
	public String guardarTvalositEndoso()
	{
		logger.debug(Utils.log(
				 "\n###################################"
				,"\n###### guardarTvalositEndoso ######"
				,"\n###### smap1=",smap1
				));
		
		try
		{
			Utils.validate(smap1,"No se recibieron datos");
			
			endososAutoManager.guardarTvalositEndoso(
					smap1.get("CDUNIECO")
					,smap1.get("CDRAMO")
					,smap1.get("ESTADO")
					,smap1.get("NMPOLIZA")
					,smap1.get("NMSITUAC")
					,smap1.get("NMSUPLEM")
					,smap1.get("STATUS")
					,smap1.get("CDTIPSIT")
					,smap1.get("OTVALOR01"),smap1.get("OTVALOR02"),smap1.get("OTVALOR03"),smap1.get("OTVALOR04"),smap1.get("OTVALOR05")
					,smap1.get("OTVALOR06"),smap1.get("OTVALOR07"),smap1.get("OTVALOR08"),smap1.get("OTVALOR09"),smap1.get("OTVALOR10")
					,smap1.get("OTVALOR11"),smap1.get("OTVALOR12"),smap1.get("OTVALOR13"),smap1.get("OTVALOR14"),smap1.get("OTVALOR15")
					,smap1.get("OTVALOR16"),smap1.get("OTVALOR17"),smap1.get("OTVALOR18"),smap1.get("OTVALOR19"),smap1.get("OTVALOR20")
					,smap1.get("OTVALOR21"),smap1.get("OTVALOR22"),smap1.get("OTVALOR23"),smap1.get("OTVALOR24"),smap1.get("OTVALOR25")
					,smap1.get("OTVALOR26"),smap1.get("OTVALOR27"),smap1.get("OTVALOR28"),smap1.get("OTVALOR29"),smap1.get("OTVALOR30")
					,smap1.get("OTVALOR31"),smap1.get("OTVALOR32"),smap1.get("OTVALOR33"),smap1.get("OTVALOR34"),smap1.get("OTVALOR35")
					,smap1.get("OTVALOR36"),smap1.get("OTVALOR37"),smap1.get("OTVALOR38"),smap1.get("OTVALOR39"),smap1.get("OTVALOR40")
					,smap1.get("OTVALOR41"),smap1.get("OTVALOR42"),smap1.get("OTVALOR43"),smap1.get("OTVALOR44"),smap1.get("OTVALOR45")
					,smap1.get("OTVALOR46"),smap1.get("OTVALOR47"),smap1.get("OTVALOR48"),smap1.get("OTVALOR49"),smap1.get("OTVALOR50")
					,smap1.get("OTVALOR51"),smap1.get("OTVALOR52"),smap1.get("OTVALOR53"),smap1.get("OTVALOR54"),smap1.get("OTVALOR55")
					,smap1.get("OTVALOR56"),smap1.get("OTVALOR57"),smap1.get("OTVALOR58"),smap1.get("OTVALOR59"),smap1.get("OTVALOR60")
					,smap1.get("OTVALOR61"),smap1.get("OTVALOR62"),smap1.get("OTVALOR63"),smap1.get("OTVALOR64"),smap1.get("OTVALOR65")
					,smap1.get("OTVALOR66"),smap1.get("OTVALOR67"),smap1.get("OTVALOR68"),smap1.get("OTVALOR69"),smap1.get("OTVALOR70")
					,smap1.get("OTVALOR71"),smap1.get("OTVALOR72"),smap1.get("OTVALOR73"),smap1.get("OTVALOR74"),smap1.get("OTVALOR75")
					,smap1.get("OTVALOR76"),smap1.get("OTVALOR77"),smap1.get("OTVALOR78"),smap1.get("OTVALOR79"),smap1.get("OTVALOR80")
					,smap1.get("OTVALOR81"),smap1.get("OTVALOR82"),smap1.get("OTVALOR83"),smap1.get("OTVALOR84"),smap1.get("OTVALOR85")
					,smap1.get("OTVALOR86"),smap1.get("OTVALOR87"),smap1.get("OTVALOR88"),smap1.get("OTVALOR89"),smap1.get("OTVALOR90")
					,smap1.get("OTVALOR91"),smap1.get("OTVALOR92"),smap1.get("OTVALOR93"),smap1.get("OTVALOR94"),smap1.get("OTVALOR95")
					,smap1.get("OTVALOR96"),smap1.get("OTVALOR97"),smap1.get("OTVALOR98"),smap1.get("OTVALOR99")
					,smap1.get("tstamp")
					);
			
			success = true;
		}
		catch(Exception ex)
		{
			respuesta=Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### guardarTvalositEndoso ######"
				,"\n###################################"
				));
		return SUCCESS;
	}
	
	public String confirmarEndosoTvalositAuto()
	{
		logger.debug(Utils.log(
				 "\n#########################################"
				,"\n###### confirmarEndosoTvalositAuto ######"
				,"\n###### smap1="  , smap1
				,"\n###### slist1=" , slist1
				,"\n###### flujo="  , flujo
				));
		
		try
		{
			Utils.validate(session                , "No hay sesion");
			Utils.validate(session.get("USUARIO") , "No hay usuario en la sesion");
			
			UserVO usuarioSesion = (UserVO)session.get("USUARIO");
			String cdusuari = ((UserVO)session.get("USUARIO")).getUser();
			String cdsisrol = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
			String cdelemen = ((UserVO)session.get("USUARIO")).getEmpresa().getElementoId();
			
			Utils.validate(smap1  , "No se recibieron datos");
			Utils.validate(slist1 , "No se recibieron datos de inciso");
			
			String cdtipsup = smap1.get("cdtipsup");
			String tstamp   = smap1.get("tstamp");
			String cdunieco = smap1.get("cdunieco");
			String cdramo   = smap1.get("cdramo");
			String estado   = smap1.get("estado");
			String nmpoliza = smap1.get("nmpoliza");
			String feefecto = smap1.get("feefecto");
			
			Utils.validate(cdtipsup  , "No se recibio el tipo de endoso");
			Utils.validate(tstamp    , "No se recibio el id de proceso");
			Utils.validate(cdunieco  , "No se recibio la sucursal");
			Utils.validate(cdramo    , "No se recibio el producto");
			Utils.validate(estado    , "No se recibio el estado");
			Utils.validate(nmpoliza  , "No se recibio el numero de poliza");
			Utils.validate(feefecto  , "No se recibio la fecha de efecto");
			
			endososAutoManager.confirmarEndosoTvalositAuto(
					cdtipsup
					,tstamp
					,cdunieco
					,cdramo
					,estado
					,nmpoliza
					,feefecto
					,cdusuari
					,cdsisrol
					,cdelemen
					,usuarioSesion
					,slist1
					,flujo
					);
			
			success = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success="   , success
				,"\n###### respuesta=" , respuesta
				,"\n###### confirmarEndosoTvalositAuto ######"
				,"\n#########################################"
				));
		return SUCCESS;
	}
	
	public String recuperarDatosEndosoAltaIncisoAuto()
	{
		logger.debug(Utils.log(
				 "\n################################################"
				,"\n###### recuperarDatosEndosoAltaIncisoAuto ######"
				,"\n###### smap1=",smap1
				));
		
		try
		{
			Utils.validate(smap1 , "No se recibieron datos");
			
			String cdunieco = smap1.get("cdunieco");
			String cdramo   = smap1.get("cdramo");
			String estado   = smap1.get("estado");
			String nmpoliza = smap1.get("nmpoliza");
			String nmsuplem = smap1.get("nmsuplem");
			
			Utils.validate(cdunieco , "No se recibio la sucursal");
			Utils.validate(cdramo   , "No se recibio el producto");
			Utils.validate(estado   , "No se recibio el estado");
			Utils.validate(nmpoliza , "No se recibio el numero de poliza");
			Utils.validate(nmsuplem , "No se recibio el suplemento");
			
			Map<String,Object> valores = endososAutoManager.recuperarDatosEndosoAltaIncisoAuto(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplem
					);
			
			smap1 = (Map<String,String>)valores.get("incisoPoliza");
			smap1.putAll((Map<String,String>)valores.get("tvalopol"));
			smap1.put("CDPERSON" , (String)valores.get("cdperson"));
			smap1.put("CDIDEPER" , (String)valores.get("cdideper"));
			slist1 = (List<Map<String,String>>)valores.get("tconvalsit");
			
			success = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success="   , success
				,"\n###### respuesta=" , respuesta
				,"\n###### recuperarDatosEndosoAltaIncisoAuto ######"
				,"\n################################################"
				));
		return SUCCESS;
	}
	
	public String confirmarEndosoAltaIncisoAuto()
	{
		logger.debug(Utils.log(
				 "\n###########################################"
				,"\n###### confirmarEndosoAltaIncisoAuto ######"
				,"\n###### smap1  = " , smap1
				,"\n###### slist1 = " , slist1
				));
		
		try
		{
			UserVO usuarioSesion = Utils.validateSession(session);
			
			String cdusuari   = usuarioSesion.getUser()
					,cdelemen = usuarioSesion.getEmpresa().getElementoId();
					
			Utils.validate(smap1, "No se recibieron datos de poliza");
			
			Utils.validate(slist1, "No se recibieron incisos");
			
			String cdunieco      = smap1.get("cdunieco")
					,cdramo      = smap1.get("cdramo")
					,estado      = smap1.get("estado")
					,nmpoliza    = smap1.get("nmpoliza")
					,cdtipsup    = smap1.get("cdtipsup")
					,fechaEndoso = smap1.get("fechaEndoso")
					,comfirmar   = smap1.get("confirmar")
					,cdperpag    = smap1.get("cdperpag")
					,cdagente    = smap1.get("cdagente");
			
			Utils.validate(
					cdunieco     , "No se recibio la sucursal"
					,cdramo      , "No se recibio la sucursal"
					,estado      , "No se recibio el estado de la poliza"
					,nmpoliza    , "No se recibio el numero de poliza"
					,cdtipsup    , "No se recibio el codigo de endoso"
					,fechaEndoso , "No se recibio la fecha de efecto"
					,cdagente    , "No se recibio el agente");
			
			omap1 = endososAutoManager.confirmarEndosoAltaIncisoAuto(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,slist1
					,cdusuari
					,cdelemen
					,cdtipsup
					,fechaEndoso
					,usuarioSesion
					,usuarioSesion.getRolActivo().getClave()
					,flujo
					,comfirmar
					,cdperpag
					,cdagente
					);
			
			
			respuesta = "Endoso generado correctamente";
			success   = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success   = " , success
				,"\n###### respuesta = " , respuesta
				,"\n###### omap1     = " , omap1
				,"\n###### slist1    = " , slist1
				,"\n###### confirmarEndosoAltaIncisoAuto ######"
				,"\n###########################################"
				));
		
		return SUCCESS;
	}
	
	public String endosoBajaIncisos()
	{
		logger.debug(Utils.log(
				 "\n###############################"
				,"\n###### endosoBajaIncisos ######"
				,"\n###### smap1(poliza)="   , smap1
				,"\n###### slist1(incisos)=" , slist1
				));
		
		String result = ERROR;
		try
		{
			Utils.validate(smap1  , "No se recibieron datos de poliza");
			Utils.validate(slist1 , "No se recibieron incisos");
			
			String cdramo = smap1.get("CDRAMO");
			Utils.validate(cdramo , "No se recibio la sucursal");
	
			imap = endososAutoManager.endosoBajaIncisos(cdramo);
			
			result = SUCCESS;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success="   , success
				,"\n###### respuesta=" , respuesta
				,"\n###### endosoBajaIncisos ######"
				,"\n###############################"
				));
		return result;
	}
	
	public String confirmarEndosoBajaIncisos()
	{
		logger.debug(Utils.log(
				 "\n########################################"
				,"\n###### confirmarEndosoBajaIncisos ######"
				,"\n###### smap1  = " , smap1
				,"\n###### slist1 = " , slist1
				,"\n###### flujo  = " , flujo
				));
		
		try
		{
			UserVO usuarioSesion = Utils.validateSession(session);
			
			String cdusuari   = usuarioSesion.getUser()
					,cdelemen = usuarioSesion.getEmpresa().getElementoId()
					,cdsisrol = usuarioSesion.getRolActivo().getClave();
					
			Utils.validate(smap1, "No se recibieron datos de poliza");
			
			Utils.validate(slist1, "No se recibieron incisos");
			
			String cdunieco      = smap1.get("CDUNIECO")
					,cdramo      = smap1.get("CDRAMO")
					,estado      = smap1.get("ESTADO")
					,nmpoliza    = smap1.get("NMPOLIZA")
					,cdtipsup    = smap1.get("cdtipsup")
					,fechaEndoso = smap1.get("fechaEndoso")
					,devolucionP = smap1.get("devoPrim");
			
			Utils.validate(
					cdunieco     , "No se recibio la sucursal"
					,cdramo      , "No se recibio la sucursal"
					,estado      , "No se recibio el estado de la poliza"
					,nmpoliza    , "No se recibio el numero de poliza"
					,cdtipsup    , "No se recibio el codigo de endoso"
					,fechaEndoso , "No se recibio la fecha de efecto"
					,devolucionP , "No se recibio el parametro de devolucion de prima"
					);
			
			endososAutoManager.confirmarEndosoBajaIncisos(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,slist1
					,cdusuari
					,cdelemen
					,cdtipsup
					,fechaEndoso
					,usuarioSesion
					,"S".equals(devolucionP)
					,cdsisrol
					,flujo
					);
			
			respuesta = "Endoso generado correctamente";
			success   = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success   = " , success
				,"\n###### respuesta = " , respuesta
				,"\n###### confirmarEndosoBajaIncisos ######"
				,"\n########################################"
				));
		return SUCCESS;
	}

	public String endosoAseguradoAlterno()
	{
		
		try {
			if(smap2 == null){
				smap2 =  new HashMap<String, String>();
			}
			smap2.putAll(endososAutoManager.obtieneAseguradoAlterno(smap1.get("CDUNIECO"), smap1.get("CDRAMO"), smap1.get("ESTADO"), smap1.get("NMPOLIZA"), null)) ;
		} catch (Exception e) {
			logger.error("No se pudo cargar los valores del Asegurado alterno", e);
		}
		
		smap1.put("pv_cdunieco", smap1.get("CDUNIECO"));
		smap1.put("pv_cdramo"  , smap1.get("CDRAMO"));
		smap1.put("pv_estado"  , smap1.get("ESTADO"));
		smap1.put("pv_nmpoliza", smap1.get("NMPOLIZA"));
		smap1.put("pv_cdperson", smap1.get("CDPERSON"));
		smap1.put("FEINIVAL", null);
		
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
	
	public String endosoClientes()
	{
		
		smap1.put("pv_cdunieco", smap1.get("CDUNIECO"));
		smap1.put("pv_cdramo", smap1.get("CDRAMO"));
		smap1.put("pv_estado", smap1.get("ESTADO"));
		smap1.put("pv_nmpoliza", smap1.get("NMPOLIZA"));
		smap1.put("pv_cdperson", smap1.get("CDPERSON"));
		smap1.put("tipoPantalla", "0");
		smap1.put("FEINIVAL", null);
		smap1.put("cdtipsup",TipoEndoso.REHABILITACION_NOMBRE_RFC_FENAC.getCdTipSup().toString());
		smap1.put("rutaPDF",this.caratulaImpresionAutosEndosobUrl);
		
		logger.debug(new StringBuilder()
		.append("\n############################")
		.append("\n############################")
		.append("\n###### endosoClientes ######")
		.append("\n######                         ######").toString());
		logger.debug(new StringBuilder("smap1: ").append(smap1).toString());
		logger.debug(new StringBuilder("session: ").append(session).toString());		
		logger.debug(new StringBuilder()
		.append("\n######                 ######")
		.append("\n###### endosoClientes  ######")
		.append("\n#############################")
		.append("\n#############################").toString());
		
		return SUCCESS;
	}
	
	public String endososPolizasNoSicaps()
	{
		//List<Map<String, String>> loadList = new ArrayList<Map<String,String>>();
		HashMap<String,String>smap=new HashMap<String,String>();
		smap.put("cdtipsup",TipoEndoso.REHABILITACION_NOMBRE_RFC_FENAC.getCdTipSup().toString());
		smap.put("tipoPantalla","1");
		smap.put("rutaPDF",this.caratulaImpresionAutosEndosobUrl);
		smap1 = smap;
		logger.debug(new StringBuilder()
		.append("\n#####################################")
		.append("\n#####################################")
		.append("\n###### endososPolizasNoSicaps  ######")
		.append("\n######                         ######").toString());
		logger.debug(new StringBuilder("smap1: ").append(smap1).toString());
		logger.debug(new StringBuilder("session: ").append(session).toString());		
		logger.debug(new StringBuilder()
		.append("\n######                         ######")
		.append("\n###### endososPolizasNoSicaps  ######")
		.append("\n#####################################")
		.append("\n#####################################").toString());
		
		return SUCCESS;
	}

	public String endosoTextoLibre()
	{
		logger.debug(Utils.log(
				"\n###########################################"
				,"\n###########################################"
				,"\n###### 		endosoTextoLibre 	     ######"
				,"\n###### smap1="  , smap1
				,"\n######                               ######"));
		try {
			String cdunieco = smap1.get("CDUNIECO");
			String cdramo   = smap1.get("CDRAMO");
			String estado   = smap1.get("ESTADO");
			String nmpoliza = smap1.get("NMPOLIZA");
			String cdtipsup = TipoEndoso.ENDOSO_B_LIBRE.getCdTipSup().toString();
			
			Utils.validate(session                , "No hay sesion");
			Utils.validate(session.get("USUARIO") , "No hay usuario en la sesion");
			String cdsisrol = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
			logger.debug("VALOR DEL DATO -->"+cdsisrol);
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String fechaProceso   = sdf.format(new Date());
			//1.Obtenemos los n�meros de d�as Maximo y Minimo
			List<Map<String,String>> retroactividad = endososAutoManager.obtenerRetroactividad(cdsisrol,cdramo,cdtipsup, fechaProceso);
			endososAutoManager.validarEndosoAnterior(cdunieco, cdramo, estado, nmpoliza, cdtipsup);
//			endososAutoManager.validarEndosoPagados(cdunieco, cdramo, estado, nmpoliza, cdtipsup);
			success   = true;
			
			smap1.put("pv_cdunieco", smap1.get("CDUNIECO"));
			smap1.put("pv_cdramo", smap1.get("CDRAMO"));
			smap1.put("pv_estado", smap1.get("ESTADO"));
			smap1.put("pv_nmpoliza", smap1.get("NMPOLIZA"));
			smap1.put("pv_cdperson", smap1.get("CDPERSON"));
			smap1.put("pv_diasMinimo", retroactividad.get(0).get("DIASMINIMO"));
			smap1.put("pv_diasMaximo", retroactividad.get(0).get("DIASMAXIMO"));
			smap1.put("FEINIVAL", null);
			
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
			.append("\n######                         ######")
			.append("\n######  endosoTextoLibre   ######")
			.append("\n#####################################")
			.append("\n#####################################").toString());
		} catch (Exception ex) {
			respuesta = Utils.manejaExcepcion(ex);
		}
		return isSuccess() ? SUCCESS : ERROR;
	}
	
	public String endosoVigenciaPoliza()
	{
		logger.debug(Utils.log(
				"\n###########################################"
				,"\n###########################################"
				,"\n###### 		endosoVigenciaPoliza 	 ######"
				,"\n###### smap1="  , smap1
				,"\n######                               ######"));
		try {
			String cdunieco = smap1.get("CDUNIECO");
			String cdramo   = smap1.get("CDRAMO");
			String estado   = smap1.get("ESTADO");
			String nmpoliza = smap1.get("NMPOLIZA");
			String cdtipsup      = TipoEndoso.VIGENCIA_POLIZA.getCdTipSup().toString();
			
			Utils.validate(session                , "No hay sesion");
			Utils.validate(session.get("USUARIO") , "No hay usuario en la sesion");
			String cdsisrol = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
			logger.debug("VALOR DEL DATO -->"+cdsisrol);
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String fechaProceso   = sdf.format(new Date());
			//1.Obtenemos los n�meros de d�as Maximo y Minimo
			List<Map<String,String>> retroactividad = endososAutoManager.obtenerRetroactividad(cdsisrol,cdramo,cdtipsup, fechaProceso);
			endososAutoManager.validarEndosoAnterior(cdunieco, cdramo, estado, nmpoliza, cdtipsup);
			endososAutoManager.validarEndosoPagados(cdunieco, cdramo, estado, nmpoliza, cdtipsup);
			endososAutoManager.validaEndosoCambioVigencia(cdunieco, cdramo, estado, nmpoliza);
			success   = true;
			
			smap1.put("pv_cdunieco", smap1.get("CDUNIECO"));
			smap1.put("pv_cdramo", smap1.get("CDRAMO"));
			smap1.put("pv_estado", smap1.get("ESTADO"));
			smap1.put("pv_nmpoliza", smap1.get("NMPOLIZA"));
			smap1.put("pv_cdperson", smap1.get("CDPERSON"));
			
			smap1.put("FEINIVAL", null);
			
			String FEEFECTO[] = smap1.get("FEEFECTO").toString().split("\\/");
			String FEPROREN[] = smap1.get("FEPROREN").toString().split("\\/");
			
			final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;
			Calendar feproren = new GregorianCalendar(Integer.parseInt(FEPROREN[2].toString()), Integer.parseInt(FEPROREN[1].toString()) - 1 ,Integer.parseInt(FEPROREN[0].toString())); 
			java.sql.Date feprorenMod = new java.sql.Date(feproren.getTimeInMillis());
			
			Calendar fefecto = new GregorianCalendar(Integer.parseInt(FEEFECTO[2].toString()), Integer.parseInt(FEEFECTO[1].toString()) - 1 ,Integer.parseInt(FEEFECTO[0].toString()));  
			java.sql.Date fefectoMod = new java.sql.Date(fefecto.getTimeInMillis());
			
			long diferencia = ( feprorenMod.getTime() - fefectoMod.getTime() )/MILLSECS_PER_DAY; 
			smap1.put("pv_difDate",diferencia+"");
			
	        Date dateMin = java.sql.Date.valueOf(FEEFECTO[2].toString()+"-"+FEEFECTO[1].toString()+"-"+FEEFECTO[0].toString());
	        Calendar calMin = new GregorianCalendar();
	        calMin.setTime(dateMin);
	        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        calMin.add(Calendar.DATE, -Integer.parseInt(retroactividad.get(0).get("DIASMINIMO")));
	        String fechaMinima = sdf.format(calMin.getTime());
			smap1.put("pv_fechaMinima", fechaMinima);
			
			Date dateMax = java.sql.Date.valueOf(FEEFECTO[2].toString()+"-"+FEEFECTO[1].toString()+"-"+FEEFECTO[0].toString());
	        Calendar calMax = new GregorianCalendar();
	        calMax.setTime(dateMax);
	        calMax.add(Calendar.DATE, Integer.parseInt(retroactividad.get(0).get("DIASMAXIMO")));
	        String fechaMaxima = sdf.format(calMax.getTime());
	        smap1.put("pv_fechaMaxima", fechaMaxima);
			logger.debug(new StringBuilder()
			.append("\n######                         ######")
			.append("\n######  endosoVigenciaPoliza   ######")
			.append("\n#####################################")
			.append("\n#####################################").toString());
		} catch (Exception ex) {
			respuesta = Utils.manejaExcepcion(ex);
		}
		return isSuccess() ? SUCCESS : ERROR;
	}
	
	public String endosoAmpliacionVigencia()
	{
		logger.debug(Utils.log(
				"\n###########################################"
				,"\n###########################################"
				,"\n###### endosoAmpliacionVigencia 	 ######"
				,"\n###### smap1  = "  , smap1
				,"\n###### smap2  = "  , smap2
				,"\n######                               ######"));
		try {
			String cdunieco = smap1.get("CDUNIECO");
			String cdramo   = smap1.get("CDRAMO");
			String estado   = smap1.get("ESTADO");
			String nmpoliza = smap1.get("NMPOLIZA");
			String cdtipsup      = TipoEndoso.AMPLIACION_DE_VIGENCIA.getCdTipSup().toString();
			
			Utils.validate(session                , "No hay sesion");
			Utils.validate(session.get("USUARIO") , "No hay usuario en la sesion");
			String cdsisrol = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
			logger.debug("VALOR DEL DATO -->"+cdsisrol);
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String fechaProceso   = sdf.format(new Date());
			//1.Obtenemos los n�meros de d�as Maximo y Minimo
			List<Map<String,String>> retroactividad = endososAutoManager.obtenerRetroactividad(cdsisrol,cdramo,cdtipsup, fechaProceso);
			endososAutoManager.validarEndosoAnterior(cdunieco, cdramo, estado, nmpoliza, cdtipsup);
//			endososAutoManager.validarEndosoPagados(cdunieco, cdramo, estado, nmpoliza, cdtipsup);
			success   = true;
			
			smap1.put("pv_cdunieco", smap1.get("CDUNIECO"));
			smap1.put("pv_cdramo", smap1.get("CDRAMO"));
			smap1.put("pv_estado", smap1.get("ESTADO"));
			smap1.put("pv_nmpoliza", smap1.get("NMPOLIZA"));
			smap1.put("pv_cdperson", smap1.get("CDPERSON"));
			smap1.put("pv_diasMinimo", retroactividad.get(0).get("DIASMINIMO"));
			smap1.put("pv_diasMaximo", retroactividad.get(0).get("DIASMAXIMO"));
			smap1.put("FEINIVAL", null);
			
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
			.append("\n######                            ######")
			.append("\n######  endosoAmpliacionVigencia  ######")
			.append("\n########################################")
			.append("\n########################################").toString());
		} catch (Exception ex) {
			respuesta = Utils.manejaExcepcion(ex);
		}
		return isSuccess() ? SUCCESS : ERROR;
	}
	
	public String endosoDespago()
	{
		logger.debug(Utils.log(
				"\n##########################################"
				,"\n#########################################"
				,"\n###### 		endosoDespago 	       ######"
				,"\n###### smap1="  , smap1
				,"\n######                             ######"));
		try {
			EndososAction endososAction = new EndososAction();
			endososAction.setEndososManager(endososManager);
			endososAction.transformaEntrada(smap1, slist1, false);
			
			Utils.validate(session                , "No hay sesion");
			Utils.validate(session.get("USUARIO") , "No hay usuario en la sesion");
			String cdsisrol = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
			logger.debug("VALOR DEL DATO -->"+cdsisrol);
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String fechaProceso   = sdf.format(new Date());
			
			
			smap1.put("pv_cdunieco", smap1.get("CDUNIECO"));
			smap1.put("pv_cdramo", smap1.get("CDRAMO"));
			smap1.put("pv_estado", smap1.get("ESTADO"));
			smap1.put("pv_nmpoliza", smap1.get("NMPOLIZA"));
			smap1.put("pv_cdperson", smap1.get("CDPERSON"));
			smap1.put("FEINIVAL", fechaProceso);
			
			success = true;
			
			logger.debug(new StringBuilder()
			.append("\n######                         ######")
			.append("\n######  endosoVigenciaPoliza   ######")
			.append("\n#####################################")
			.append("\n#####################################").toString());
		} catch (Exception ex) {
			respuesta = Utils.manejaExcepcion(ex);
		}
		return isSuccess() ? SUCCESS : ERROR;
	}
	
	public String guardarEndosoAseguradoAlterno()
	{
		logger.debug(Utils.log(
				 "\n###########################################"
				,"\n###### guardarEndosoAseguradoAlterno ######"
				,"\n###### smap1 = " , smap1
				,"\n###### flujo = " , flujo
				));
		try
		{
			logger.debug("Validando datos de entrada");
			Utils.validate(smap1, "No se recibieron datos");
			
			String cdunieco = smap1.get("CDUNIECO");
			String cdramo   = smap1.get("CDRAMO");
			String estado   = smap1.get("ESTADO");
			String nmpoliza = smap1.get("NMPOLIZA");
			String status   = smap1.get("STATUS");
			String ntramite = smap1.get("NTRAMITE");
			
			String aseguradoAlterno = smap1.get("ASEG_ALTERNO");
			
			Utils.validate(cdunieco , "No se recibio la sucursal");
			Utils.validate(cdramo   , "No se recibio el producto");
			Utils.validate(estado   , "No se recibio el estado de la poliza");
			Utils.validate(nmpoliza , "No se recibio el numero de poliza");
			Utils.validate(session                , "No hay sesion");
			Utils.validate(session.get("USUARIO") , "No hay usuario en la sesion");
			
			String cdusuari = ((UserVO)session.get("USUARIO")).getUser();
			String cdsisrol = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
			String cdelemen = ((UserVO)session.get("USUARIO")).getEmpresa().getElementoId();
			String cdtipsup = TipoEndoso.ASEGURADO_ALTERNO.getCdTipSup().toString();
			String fechaEndoso   = smap1.get("FEINIVAL");
			Date   dFechaEndoso  = renderFechas.parse(fechaEndoso);
			
			endososAutoManager.guardarEndosoAseguradoAlterno(
					cdunieco,
					cdramo,
					estado,
					nmpoliza,
					ntramite,
					cdelemen,
					cdusuari,
					cdtipsup,
					status,
					fechaEndoso,
					dFechaEndoso,
					aseguradoAlterno,
					cdsisrol,
					flujo
					);
			
			respuesta = "Endoso generado correctamente";
			success   = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success   = " , success
				,"\n###### respuesta = " , respuesta
				,"\n###### guardarEndosoAseguradoAlterno ######"
				,"\n###########################################"
				));
		return SUCCESS;
	}
	
	public String guardarEndosoVigenciaPoliza()
	{
		logger.debug(Utils.log(
				 "\n#########################################"
				,"\n###### guardarEndosoCambioVigencia ######"
				,"\n###### smap1 = " , smap1
				,"\n###### flujo = " , flujo
				));
		try
		{
			logger.debug("Validando datos de entrada");
			Utils.validate(smap1, "No se recibieron datos");
			
			String cdunieco = smap1.get("CDUNIECO");
			String cdramo   = smap1.get("CDRAMO");
			String estado   = smap1.get("ESTADO");
			String nmpoliza = smap1.get("NMPOLIZA");
			String status   = smap1.get("STATUS");
			String feefecto = smap1.get("FEEFECTO");
			String feproren = smap1.get("FEPROREN");
			String ntramite = smap1.get("NTRAMITE");
			String nmsuplemOriginal = smap1.get("NMSUPLEM");
			
			Utils.validate(cdunieco , "No se recibio la sucursal");
			Utils.validate(cdramo   , "No se recibio el producto");
			Utils.validate(estado   , "No se recibio el estado de la poliza");
			Utils.validate(nmpoliza , "No se recibio el numero de poliza");
			Utils.validate(status   , "No se recibio el status");
			Utils.validate(feefecto , "No se recibio la fecha feproren");
			Utils.validate(feproren , "No se recibio la fecha feproren");
			
			Utils.validate(session                , "No hay sesion");
			Utils.validate(session.get("USUARIO") , "No hay usuario en la sesion");
			
			String cdusuari = ((UserVO)session.get("USUARIO")).getUser();
			String cdsisrol = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
			String cdelemen = ((UserVO)session.get("USUARIO")).getEmpresa().getElementoId();
			
			String cdtipsup      = TipoEndoso.VIGENCIA_POLIZA.getCdTipSup().toString();
			String fechaEndoso   = smap1.get("FEEFECTO");
			Date   dFechaEndoso  = renderFechas.parse(fechaEndoso);
			
			endososAutoManager.guardarEndosoVigenciaPoliza(
					cdunieco,
					cdramo,
					estado,
					nmpoliza,
					ntramite,
					cdelemen,
					cdusuari,
					cdtipsup,
					status,
					fechaEndoso,
					dFechaEndoso,
					feefecto,
					feproren,
					nmsuplemOriginal,
					cdsisrol,
					flujo
					);
			
			respuesta = "Endoso generado correctamente";
			success   = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success  = " , success
				,"\n###### repuesta = " , respuesta
				,"\n###### guardarEndosoCambioVigencia ######"
				,"\n#########################################"
				));
		return SUCCESS;
	}
	
	public String guardarEndosoAmpliacionVigencia() {
		
		logger.debug(Utils.log(
				 "\n#############################################"
				,"\n###### guardarEndosoAmpliacionVigencia ######"
				,"\n###### smap1 = " , smap1
				,"\n###### smap2 = " , smap2
				,"\n###### flujo = " , flujo
				));
		try
		{
			logger.debug("Validando datos de entrada");
			Utils.validate(smap1, "No se recibieron datos");
			
			String cdunieco = smap1.get("CDUNIECO");
			String cdramo   = smap1.get("CDRAMO");
			String estado   = smap1.get("ESTADO");
			String nmpoliza = smap1.get("NMPOLIZA");
			String status   = smap1.get("STATUS");
			String feefecto = smap1.get("FEEFECTO");
			String feproren = smap1.get("FEPROREN");
			String feprorenOriginal = smap1.get("FEPROREN_ORIG");
			String ntramite = smap1.get("NTRAMITE");
			String nmsuplemOriginal = smap1.get("NMSUPLEM");
			String confirmar        = smap1.get("confirmar");
			String TIPOFLOT         = smap1.get("TIPOFLOT");
			String cdperpag         = smap1.get("CDPERPAG");
			String p_plan           = smap1.get("CDMEJRED");
			
			Utils.validate(cdunieco , "No se recibio la sucursal");
			Utils.validate(cdramo   , "No se recibio el producto");
			Utils.validate(estado   , "No se recibio el estado de la poliza");
			Utils.validate(nmpoliza , "No se recibio el numero de poliza");
			Utils.validate(status   , "No se recibio el status");
			Utils.validate(feefecto , "No se recibio la fecha feefecto");
			Utils.validate(feprorenOriginal , "No se recibio la fecha feefecto original");
			Utils.validate(feproren , "No se recibio la fecha feproren");
			
			
			Utils.validate(session                , "No hay sesion");
			Utils.validate(session.get("USUARIO") , "No hay usuario en la sesion");
			
			UserVO usuarioSesion = (UserVO)session.get("USUARIO");
			String cdusuari = ((UserVO)session.get("USUARIO")).getUser();
			String cdsisrol = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
			String cdelemen = ((UserVO)session.get("USUARIO")).getEmpresa().getElementoId();
			
			String cdtipsup      = TipoEndoso.AMPLIACION_DE_VIGENCIA.getCdTipSup().toString();
			String fechaEndoso   = smap1.get("FEINIVAL");
			Date   dFechaEndoso  = renderFechas.parse(fechaEndoso);
			Date   dFeprorenOrig  = renderFechas.parse(feprorenOriginal);
			
			String tipoGrupoInciso = smap1.get("TIPOFLOT");
			
			smap2 = endososAutoManager.guardarEndosoAmpliacionVigencia(
					cdunieco,
					cdramo,
					estado,
					nmpoliza,
					ntramite,
					cdelemen,
					cdusuari,
					cdtipsup,
					status,
					fechaEndoso,
					dFechaEndoso,
					feefecto,
					feproren,
					dFeprorenOrig,
					nmsuplemOriginal,
					usuarioSesion,
					tipoGrupoInciso,
					flujo,
					cdsisrol,
					confirmar,
					TIPOFLOT,
					cdperpag,
					p_plan
					);
			
			respuesta = "Endoso generado correctamente";
			success   = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success   = " , success
				,"\n###### respuesta = " , respuesta
				,"\n###### smap2     = " , smap2
				,"\n###### guardarEndosoAmpliacionVigencia ######"
				,"\n#############################################"
				));
		return SUCCESS;
	}
	
	public String guardarEndosoDespago()
	{
		logger.debug(Utils.log(
				 "\n##################################"
				,"\n###### guardarEndosoDespago ######"
				,"\n###### smap1 = " , smap1
				,"\n###### flujo = " , flujo
				));
		try
		{
			logger.debug("Validando datos de entrada");
			Utils.validate(smap1, "No se recibieron datos");
			
			String cdunieco = smap1.get("CDUNIECO");
			String cdramo   = smap1.get("CDRAMO");
			String estado   = smap1.get("ESTADO");
			String nmpoliza = smap1.get("NMPOLIZA");
//			String status   = smap1.get("STATUS");
//			String ntramite = smap1.get("NTRAMITE");
			String nmsuplemOriginal = smap1.get("NMSUPLEM");
			String nmrecibo = smap1.get("NMRECIBO");
			String nmimpres = smap1.get("NMIMPRES");
			
			Utils.validate(cdunieco , "No se recibio la sucursal");
			Utils.validate(cdramo   , "No se recibio el producto");
			Utils.validate(estado   , "No se recibio el estado de la poliza");
			Utils.validate(nmpoliza , "No se recibio el numero de poliza");
//			Utils.validate(status   , "No se recibio el status");
			
			
			Utils.validate(session                , "No hay sesion");
			Utils.validate(session.get("USUARIO") , "No hay usuario en la sesion");
			
			UserVO usuarioSesion = (UserVO)session.get("USUARIO");
			
			String cdtipsup      = TipoEndoso.DESPAGO.getCdTipSup().toString();
			String fechaEndoso   = smap1.get("FEINIVAL");
//			Date   dFechaEndoso  = renderFechas.parse(fechaEndoso);
			
			endososAutoManager.guardarEndosoDespago(
					cdunieco,
					cdramo,
					estado,
					nmpoliza,
					nmsuplemOriginal,
					nmrecibo,
					nmimpres,
					cdtipsup,
					usuarioSesion,
					usuarioSesion.getUser(),
					usuarioSesion.getRolActivo().getClave(),
					flujo
					);
			
			respuesta = "Endoso generado correctamente";
			success   = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success   = " , success
				,"\n###### respuesta = " , respuesta
				,"\n###### guardarEndosoDespago ######"
				,"\n##################################"
				));
		return SUCCESS;
	}

	public String guardarEndosoTextoLibre()
	{
		logger.debug(Utils.log(
				 "\n#####################################"
				,"\n###### guardarEndosoTextoLibre ######"
				,"\n###### smap1  = " , smap1
				,"\n###### slist1 = " , slist1
				,"\n###### flujo  = " , flujo
				));
		try
		{
			logger.debug("Validando datos de entrada");
			Utils.validate(smap1, "No se recibieron datos");
			
			String cdunieco = smap1.get("CDUNIECO");
			String cdramo   = smap1.get("CDRAMO");
			String estado   = smap1.get("ESTADO");
			String nmpoliza = smap1.get("NMPOLIZA");
			String status   = smap1.get("STATUS");
			String feefecto = smap1.get("FEEFECTO");
			String feproren = smap1.get("FEPROREN");
			String ntramite = smap1.get("NTRAMITE");
			String dslinea  = smap1.get("TEXTOEND");
			List<Map<String,String>> situaciones = null;
			
			if(slist1 != null && !slist1.isEmpty() && slist1.get(0).containsKey("NMSITUAC") && StringUtils.isNotBlank(slist1.get(0).get("NMSITUAC"))){
				situaciones = slist1;
			}else{
				HashMap<String,String> nivelPoliza = new HashMap<String, String>();
				nivelPoliza.put("NIVEL_POLIZA", "NIVEL_POLIZA");
				nivelPoliza.put("NMSITUAC", "0");
				situaciones = new ArrayList<Map<String,String>>();
				situaciones.add(nivelPoliza);
			}
			
			
			Utils.validate(cdunieco , "No se recibio la sucursal");
			Utils.validate(cdramo   , "No se recibio el producto");
			Utils.validate(estado   , "No se recibio el estado de la poliza");
			Utils.validate(nmpoliza , "No se recibio el numero de poliza");
			Utils.validate(status   , "No se recibio el status");
			Utils.validate(feefecto , "No se recibio la fecha feproren");
			Utils.validate(feproren , "No se recibio la fecha feproren");
			
			
			Utils.validate(session                , "No hay sesion");
			Utils.validate(session.get("USUARIO") , "No hay usuario en la sesion");
			
			String cdusuari = ((UserVO)session.get("USUARIO")).getUser();
			String cdsisrol = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
			String cdelemen = ((UserVO)session.get("USUARIO")).getEmpresa().getElementoId();
			
			String cdtipsup      = TipoEndoso.ENDOSO_B_LIBRE.getCdTipSup().toString();
			String fechaEndoso   = smap1.get("FEINIVAL");
			Date   dFechaEndoso  = renderFechas.parse(fechaEndoso);
			
			endososAutoManager.guardarEndosoTextoLibre(
					cdunieco,
					cdramo,
					estado,
					nmpoliza,
					ntramite,
					cdelemen,
					cdusuari,
					cdtipsup,
					status,
					fechaEndoso,
					dFechaEndoso,
					feefecto,
					feproren,
					situaciones,
					dslinea,
					cdsisrol,
					flujo
					);
			
			respuesta = "Endoso generado correctamente";
			success   = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success   = " , true
				,"\n###### respuesta = " , respuesta
				,"\n###### guardarEndosoTextoLibre ######"
				,"\n#####################################"
				));
		
		return SUCCESS;
	}
	
	public String endosoClaveAuto()
	{
		logger.debug(Utils.log(
				 "\n#############################"
				,"\n###### endosoClaveAuto ######"
				,"\n###### smap1="  , smap1
				,"\n###### slist1=" , slist1
				));

		String result = ERROR;
		
		try
		{
			Utils.validate(session                , "No hay sesion");
			Utils.validate(session.get("USUARIO") , "No hay usuario en la sesion");
			String cdsisrol = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
			smap1.put("cdsisrol" , cdsisrol);
			
			Utils.validate(smap1  , "No se recibieron datos de poliza");
			Utils.validate(slist1 , "No se recibieron datos de inciso");
			
			String cdramo   = slist1.get(0).get("CDRAMO");
			String cdtipsit = slist1.get(0).get("CDTIPSIT");
			
			imap = endososAutoManager.endosoClaveAuto(cdsisrol,cdramo,cdtipsit);
			
			result = SUCCESS;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### result="    , result
				,"\n###### respuesta=" , respuesta
				,"\n###### endosoClaveAuto ######"
				,"\n#############################"
				));
		return result;
	}
	
	public String guardarEndosoClaveAuto()
	{
		logger.debug(Utils.log(
				 "\n####################################"
				,"\n###### guardarEndosoClaveAuto ######"
				,"\n###### smap1  = " , smap1 
				,"\n###### smap2  = " , smap2
				,"\n###### slist1 = " , slist1
				,"\n###### flujo  = " , flujo
				));
		
		try
		{
			Utils.validate(session                , "No hay sesion");
			Utils.validate(session.get("USUARIO") , "No hay usuario en la sesion");
			
			String cdusuari = ((UserVO)session.get("USUARIO")).getUser();
			String cdelemen = ((UserVO)session.get("USUARIO")).getEmpresa().getElementoId();
			String cdsisrol = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
			UserVO usuarioSesion = (UserVO)session.get("USUARIO");
			
			Utils.validate(smap1  , "No se recibieron datos de poliza");
			Utils.validate(smap2  , "No se recibieron datos nuevos de inciso");
			Utils.validate(slist1 , "No se recibio el inciso");
			
			String cdtipsup = smap1.get("cdtipsup");
			String cdunieco = smap1.get("CDUNIECO");
			String cdramo   = smap1.get("CDRAMO");
			String estado   = smap1.get("ESTADO");
			String nmpoliza = smap1.get("NMPOLIZA");
			String feefecto = smap2.get("feefecto");
			String confirmar= smap1.get("confirmar");
			String cdperpag = smap1.get("CDPERPAG");
			String p_plan   = smap1.get("CDMEJRED");
			
			Utils.validate(cdtipsup , "No se recibio el tipo de endoso");
			Utils.validate(cdunieco , "No se recibio la sucursal");
			Utils.validate(cdramo   , "No se recibio el producto");
			Utils.validate(estado   , "No se recibio el estado de la poliza");
			Utils.validate(nmpoliza , "No se recibio el numero de poliza");
			Utils.validate(feefecto , "No se recibio la fecha de efecto");
			
			omap1 = endososAutoManager.guardarEndosoClaveAuto(
					cdtipsup
					,cdusuari
					,cdsisrol
					,cdelemen
					,cdunieco
					,cdramo
					,estado
					,nmpoliza
					,feefecto
					,smap2
					,slist1.get(0)
					,usuarioSesion
					,flujo
					,confirmar
					,p_plan
					,cdperpag
					);
			
			success = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### guardarEndosoClaveAuto ######"
				,"\n###### omap1==> ",omap1
				,"\n####################################"
				));
		return SUCCESS;
	}
	
	public String endosoDevolucionPrimas()
	{
		logger.debug(Utils.log(
				 "\n####################################"
				,"\n###### endosoDevolucionPrimas ######"
				,"\n###### smap1="  , smap1
				,"\n###### slist1=" , slist1
				));

		String result = ERROR;
		
		try
		{
			Utils.validate(smap1  , "No se recibieron datos de poliza");
			Utils.validate(slist1 , "No se recibieron incisos");
			
			String cdramo   = smap1.get("CDRAMO");
			String cdtipsup = smap1.get("cdtipsup");
			
			Utils.validate(cdramo   , "No se recibio el producto");
			Utils.validate(cdtipsup , "No se recibio el codigo de endoso");
			
			smap1.put("tstamp" , Utils.generaTimestamp());
			
			imap = endososAutoManager.endosoDevolucionPrimas(cdtipsup, cdramo);
			try{
				slist2=consultasPolizaManager.obtieneCoberturas(smap1.get("CDRAMO"), smap1.get("CDTIPSIT"), smap1.get("CDMEJRED"));
			}catch(Exception ex){
				logger.error("Error obteniendo las coberturas", ex);
			}
			result = SUCCESS;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				"\n###### endosoDevolucionPrimas ######"
				,"\n####################################"
				));
		return result;
	}
	
	public String guardarEndosoDevolucionPrimas()
	{
		logger.debug(Utils.log(
				 "\n###########################################"
				,"\n###### guardarEndosoDevolucionPrimas ######"
				,"\n###### smap1  = " , smap1
				,"\n###### smap2  = " , smap2
				,"\n###### slist1 = " , slist1
				,"\n###### flujo  = " , flujo
				));
		
		try
		{
			UserVO user = Utils.validateSession(session);
			
			Utils.validate(smap1  , "No se recibieron datos de poliza");
			Utils.validate(smap2  , "No se recibieron datos de endoso");
			Utils.validate(slist1 , "No se recibieron incisos");
			
			
			String cdunieco = smap1.get("CDUNIECO")
					,cdramo   = smap1.get("CDRAMO")
					,estado   = smap1.get("ESTADO")
					,nmpoliza = smap1.get("NMPOLIZA")
					,cdtipsup = smap1.get("cdtipsup")
					,tstamp   = smap1.get("tstamp")
					,fechaEnd = smap2.get("feefecto")
					,confirmar= smap1.get("confirmar")
					,cdperpag = smap1.get("cdperpag")
			        ,p_plan   = smap1.get("p_plan");
			Utils.validate(
					cdunieco  , "No se recibio la sucursal"
					,cdramo   , "No se recibio el producto"
					,estado   , "No se recibio el estado de la poliza"
					,nmpoliza , "No se recibio el numero de poliza"
					,cdtipsup , "No se recibio el codigo de endoso"
					,tstamp   , "No se recibio el ID de proceso"
					,fechaEnd , "No se recibio la fecha de efecto"
					);
			
			omap1 = endososAutoManager.guardarEndosoDevolucionPrimas(
					user.getUser()
					,user.getRolActivo().getClave()
					,user.getEmpresa().getElementoId()
					,cdunieco
					,cdramo
					,estado
					,nmpoliza
					,cdtipsup
					,tstamp
					,renderFechas.parse(fechaEnd)
					,slist1
					,user
					,flujo
					,confirmar
					,cdperpag
					,p_plan
					);
			
			success = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success   = " , success
				,"\n###### respuesta = " , respuesta
				,"\n###### omap1     = " , omap1
				,"\n###### guardarEndosoDevolucionPrimas ######"
				,"\n###########################################"
				));
		return SUCCESS;
	}
	
	public String endosoRehabilitacionAuto()
	{
		logger.debug(Utils.log(
				 "\n######################################"
				,"\n###### endosoRehabilitacionAuto ######"
				,"\n###### smap1=",smap1
				));
		
		String result = ERROR;
		try
		{
			UserVO user = Utils.validateSession(session);
			
			Utils.validate(smap1 , "No se recibieron datos");
			
			String cdramo = smap1.get("CDRAMO");
			
			Utils.validate(cdramo , "No se recibio el producto");
			
			imap = endososAutoManager.endosoRehabilitacionAuto(user.getRolActivo().getClave(),cdramo);
			
			result = SUCCESS;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### result=",result
				,"\n###### endosoRehabilitacionAuto ######"
				,"\n######################################"
				));
		return result;
	}
	
	public String confirmarEndosoRehabilitacionAuto()
	{
		logger.debug(Utils.log(
				 "\n###############################################"
				,"\n###### confirmarEndosoRehabilitacionAuto ######"
				,"\n###### smap1 = " , smap1
				,"\n###### flujo = " , flujo
				));
		
		try
		{
			UserVO user = Utils.validateSession(session);
			
			Utils.validate(smap1 , "No se recibieron datos");
			
			String cdunieco = smap1.get("cdunieco")
					,cdramo   = smap1.get("cdramo")
					,estado   = smap1.get("estado")
					,nmpoliza = smap1.get("nmpoliza")
					,cdtipsup = smap1.get("cdtipsup")
					,nsuplogi = smap1.get("nsuplogi")
					,cddevcia = smap1.get("cddevcia")
					,cdgestor = smap1.get("cdgestor")
					,feemisio = smap1.get("feemisio")
					,feinival = smap1.get("feinival")
					,fefinval = smap1.get("fefinval")
					,feefecto = smap1.get("feefecto")
					,feproren = smap1.get("feproren")
					,cdmoneda = smap1.get("cdmoneda")
					,nmsuplem = smap1.get("nmsuplem");
			
			Utils.validate(
					cdunieco  , "No se recibio la sucursal"
					,cdramo   , "No se recibio el producto"
					,estado   , "No se recibio el estado de la poliza"
					,nmpoliza , "No se recibio el numero de poliza"
					,cdtipsup , "No se recibio el codigo de endoso"
					,nsuplogi , "No se recibio el consecutivo de endoso"
					,cddevcia , "No se recibio el tipo de endoso"
					,cdgestor , "No se recibio el numero de endoso"
					,feemisio , "No se recibio la fecha de emision de endoso"
					,feinival , "No se recibio la fecha de inicio de endoso"
					,fefinval , "No se recibio la fecha de fin de endoso"
					,feefecto , "No se recibio la fecha de efecto"
					,feproren , "No se recibio la fecha de proxima renovacion"
					,cdmoneda , "No se recibio la clave de moneda"
					,nmsuplem , "No se recibio el numero de suplemento"
					);
			
			endososAutoManager.confirmarEndosoRehabilitacionAuto(
					user.getUser()
					,user.getRolActivo().getClave()
					,cdunieco
					,cdramo
					,estado
					,nmpoliza
					,cdtipsup
					,nsuplogi
					,cddevcia
					,cdgestor
					,renderFechas.parse(feemisio)
					,renderFechas.parse(feinival)
					,renderFechas.parse(fefinval)
					,renderFechas.parse(feefecto)
					,renderFechas.parse(fefinval)
					,cdmoneda
					,nmsuplem
					,user.getEmpresa().getElementoId()
					,user
					,flujo
					);
			
			success = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success   = " , success
				,"\n###### respuesta = " , respuesta
				,"\n###### confirmarEndosoRehabilitacionAuto ######"
				,"\n###############################################"
				));
		return SUCCESS;
	}

	public String endosoRehabilitacionSalud()
	{
		logger.debug(Utils.log(
				"\n######################################"
				,"\n###### endosoRehabilitacionSalud ######"
				,"\n###### smap1=",smap1
				));
		
		String result = ERROR;
		try
		{
			UserVO user = Utils.validateSession(session);
			
			Utils.validate(smap1 , "No se recibieron datos");
			
			String cdramo = smap1.get("CDRAMO");
			
			Utils.validate(cdramo , "No se recibio el producto");
			
			imap = endososAutoManager.endosoRehabilitacionSalud(user.getRolActivo().getClave(),cdramo);
			
			result = SUCCESS;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				"\n###### result=",result
				,"\n###### endosoRehabilitacionSalud ######"
				,"\n######################################"
				));
		return result;
	}
	
	public String confirmarEndosoRehabilitacionSalud()
	{
		logger.debug(Utils.log(
				 "\n################################################"
				,"\n###### confirmarEndosoRehabilitacionSalud ######"
				,"\n###### smap1 = " , smap1
				,"\n###### flujo = " , flujo
				));
		
		try
		{
			UserVO user = Utils.validateSession(session);
			
			UserVO usuarioSesion = (UserVO)session.get("USUARIO");
			
			Utils.validate(smap1 , "No se recibieron datos");
			
			String cdunieco = smap1.get("cdunieco");
			String cdramo   = smap1.get("cdramo");
			String estado   = smap1.get("estado");
			String nmpoliza = smap1.get("nmpoliza");
			String cdtipsup = TipoEndoso.REHABILITACION_ENDOSO.getCdTipSup().toString();
			String nsuplogi = smap1.get("nsuplogi");
			String cddevcia = smap1.get("cddevcia");
			String cdgestor = smap1.get("cdgestor");
			String feemisio = smap1.get("feemisio");
			String feinival = smap1.get("feinival");
			String fefinval = smap1.get("fefinval");
			String feefecto = smap1.get("feefecto");
			String feproren = smap1.get("feproren");
			String cdmoneda = smap1.get("cdmoneda");
			String nmsuplem = smap1.get("nmsuplem");
			
			Utils.validate(cdunieco , "No se recibio la sucursal");
			Utils.validate(cdramo   , "No se recibio el producto");
			Utils.validate(estado   , "No se recibio el estado de la poliza");
			Utils.validate(nmpoliza , "No se recibio el numero de poliza");
			Utils.validate(cdtipsup , "No se recibio el codigo de endoso");
			Utils.validate(nsuplogi , "No se recibio el consecutivo de endoso");
			Utils.validate(cddevcia , "No se recibio el tipo de endoso");
			Utils.validate(cdgestor , "No se recibio el numero de endoso");
			Utils.validate(feemisio , "No se recibio la fecha de emision de endoso");
			Utils.validate(feinival , "No se recibio la fecha de inicio de endoso");
			Utils.validate(fefinval , "No se recibio la fecha de fin de endoso");
			Utils.validate(feefecto , "No se recibio la fecha de efecto");
			Utils.validate(feproren , "No se recibio la fecha de proxima renovacion");
			Utils.validate(cdmoneda , "No se recibio la clave de moneda");
			Utils.validate(nmsuplem , "No se recibio el numero de suplemento");
			
			endososAutoManager.confirmarEndosoRehabilitacionSalud(
					user.getUser()
					,user.getRolActivo().getClave()
					,cdunieco
					,cdramo
					,estado
					,nmpoliza
					,cdtipsup
					,nsuplogi
					,cddevcia
					,cdgestor
					,renderFechas.parse(feemisio)
					,renderFechas.parse(feinival)
					,renderFechas.parse(fefinval)
					,renderFechas.parse(feefecto)
					,renderFechas.parse(fefinval)
					,cdmoneda
					,nmsuplem
					,user.getEmpresa().getElementoId()
					,usuarioSesion
					,flujo
					);
			
			success = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success   = " , success
				,"\n###### respuesta = " , respuesta
				,"\n###### confirmarEndosoRehabilitacionSalud ######"
				,"\n################################################"
				));
		return SUCCESS;
	}
	
	public String endosoCancelacionAuto()
	{
		logger.debug(Utils.log(
				 "\n###################################"
				,"\n###### endosoCancelacionAuto ######"
				,"\n###### smap1=",smap1
				));
		
		String result = ERROR;
		try
		{
			UserVO user = Utils.validateSession(session);
			
			Utils.validate(smap1 , "No se recibieron datos");
			
			String cdramo = smap1.get("CDRAMO");
			
			Utils.validate(cdramo , "No se recibio el producto");
			
			imap = endososAutoManager.endosoCancelacionAuto(user.getRolActivo().getClave(),cdramo);
			
			result = SUCCESS;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### result=",result
				,"\n###### endosoCancelacionAuto ######"
				,"\n###################################"
				));
		return result;
	}
	
	public String buscarError()
	{
		logger.debug(Utils.log(
				 "\n#########################"
				,"\n###### buscarError ######"
				,"\n###### smap1=",smap1
				));
		
		try
		{
			Utils.validate(smap1                , "No se recibieron datos");
			Utils.validate(smap1.get("error")   , "No se recibio el numero de error");
			Utils.validate(smap1.get("archivo") , "No se recibio el nombre del archivo");
			
			smap1.putAll(endososAutoManager.buscarError(smap1.get("error"),logsRuta,smap1.get("archivo")));
			
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### buscarError ######"
				,"\n#########################"
				));
		return SUCCESS;
	}
	
	public String confirmarEndosoCancelacionEndoso()
	{
		logger.debug(Utils.log(
				 "\n##############################################"
				,"\n###### confirmarEndosoCancelacionEndoso ######"
				,"\n###### smap1 = " , smap1
				,"\n###### flujo = " , flujo
				));
		
		try
		{
			UserVO user = Utils.validateSession(session);
			
			Utils.validate(smap1 , "No se recibieron datos");
			
			String cdunieco = smap1.get("cdunieco")
					,cdramo   = smap1.get("cdramo")
					,estado   = smap1.get("estado")
					,nmpoliza = smap1.get("nmpoliza")
					,cdtipsup = smap1.get("cdtipsup")
					,nsuplogi = smap1.get("nsuplogi")
					,cddevcia = smap1.get("cddevcia")
					,cdgestor = smap1.get("cdgestor")
					,feemisio = smap1.get("feemisio")
					,feinival = smap1.get("feinival")
					,fefinval = smap1.get("fefinval")
					,feefecto = smap1.get("feefecto")
					,feproren = smap1.get("feproren")
					,cdmoneda = smap1.get("cdmoneda")
					,nmsuplem = smap1.get("nmsuplem")
					,feinicio = smap1.get("feinicio");
			
			Utils.validate(
					cdunieco  , "No se recibio la sucursal"
					,cdramo   , "No se recibio el producto"
					,estado   , "No se recibio el estado de la poliza"
					,nmpoliza , "No se recibio el numero de poliza"
					,cdtipsup , "No se recibio el codigo de endoso"
					,nsuplogi , "No se recibio el consecutivo de endoso"
					,cddevcia , "No se recibio el tipo de endoso"
					,cdgestor , "No se recibio el numero de endoso"
					,feemisio , "No se recibio la fecha de emision de endoso"
					,feinival , "No se recibio la fecha de inicio de endoso"
					,fefinval , "No se recibio la fecha de fin de endoso"
					,feefecto , "No se recibio la fecha de efecto"
					,feproren , "No se recibio la fecha de proxima renovacion"
					,cdmoneda , "No se recibio la clave de moneda"
					,nmsuplem , "No se recibio el numero de suplemento"
					,feinicio , "No se recibio la fecha de inicio"
					);
			
			endososAutoManager.confirmarEndosoCancelacionEndoso(
					user.getUser()
					,user.getRolActivo().getClave()
					,cdunieco
					,cdramo
					,estado
					,nmpoliza
					,cdtipsup
					,nsuplogi
					,cddevcia
					,cdgestor
					,renderFechas.parse(feemisio)
					,renderFechas.parse(feinival)
					,renderFechas.parse(fefinval)
					,renderFechas.parse(feefecto)
					,renderFechas.parse(fefinval)
					,cdmoneda
					,nmsuplem
					,user.getEmpresa().getElementoId()
					,renderFechas.parse(feinicio)
					,user
					,flujo
					);
			
			success = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success   = " , success
				,"\n###### respuesta = " , respuesta
				,"\n###### confirmarEndosoCancelacionEndoso ######"
				,"\n##############################################"
				));
		return SUCCESS;
	}
	
	public String endosoCancelacionPolAuto()
	{
		logger.debug(Utils.log(
				 "\n######################################"
				,"\n###### endosoCancelacionPolAuto ######"
				,"\n###### smap1=",smap1
				));
		
		String result = ERROR;
		try
		{
			UserVO user = Utils.validateSession(session);
			
			Utils.validate(smap1 , "No se recibieron datos");
			
			String cdramo = smap1.get("CDRAMO");
			
			Utils.validate(cdramo , "No se recibio el ramo");
			
			imap = endososAutoManager.endosoCancelacionPolAuto(user.getRolActivo().getClave(),cdramo);
			
			result = SUCCESS;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### endosoCancelacionPolAuto ######"
				,"\n######################################"
				));
		return result;
	}
	
	public String marcarPolizaCancelarPorEndoso()
	{
		logger.debug(Utils.log(
				 "\n###########################################"
				,"\n###### marcarPolizaCancelarPorEndoso ######"
				,"\n###### smap1=",smap1
				));
		
		try
		{
			Utils.validate(smap1 , "No se recibieron datos");
			String cdunieco = smap1.get("cdunieco");
			String cdramo   = smap1.get("cdramo");
			String nmpoliza = smap1.get("nmpoliza");
			
			smap1.putAll(endososAutoManager.marcarPolizaCancelarPorEndoso(cdunieco,cdramo,nmpoliza));
			
			success = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### marcarPolizaCancelarPorEndoso ######"
				,"\n###########################################"
				));
		return SUCCESS;
	}
	
	public String confirmarEndosoCancelacionPolAuto()
	{
		logger.debug(Utils.log(
				 "\n###############################################"
				,"\n###### confirmarEndosoCancelacionPolAuto ######"
				,"\n###### smap1 = " , smap1
				,"\n###### flujo = " , flujo
				));
		
		try
		{
			
			UserVO usuarioSesion = Utils.validateSession(session); 
			
			String cdusuari = usuarioSesion.getUser();
			String cdsisrol = usuarioSesion.getRolActivo().getClave();
			
			Utils.validate(smap1 , "No se recibieron datos");
			
			String cdunieco = smap1.get("cdunieco");
			String cdramo   = smap1.get("cdramo");
			String estado   = smap1.get("estado");
			String nmpoliza = smap1.get("nmpoliza");
			String cdrazon  = smap1.get("cdrazon");
			String feefecto = smap1.get("feefecto");
			String fevencim = smap1.get("fevencim");
			String fecancel = smap1.get("fecancel");
			String cdtipsup = smap1.get("cdtipsup");
			String confirmar= smap1.get("confirmar");
			
			Utils.validate(cdunieco  , "No se recibio la sucursal"
					       ,cdramo   , "No se recibio el producto"
					       ,estado   , "No se recibio el estado de la poliza"
					       ,nmpoliza , "No se recibio el numero de poliza"
					       ,cdrazon  , "No se recibio el motivo de cancelacion"
					       ,feefecto , "No se recibio la fecha de inicio de vigencia"
					       ,fevencim , "No se recibio la fecha de fin de vigencia"
					       ,fecancel , "No se recibio la fecha de cancelacion"
					       ,cdtipsup , "NO se recibio la clave de endoso");
			
			omap1 = endososAutoManager.confirmarEndosoCancelacionPolAuto(
					cdusuari
					,cdunieco
					,cdramo
					,estado
					,nmpoliza
					,cdrazon
					,renderFechas.parse(feefecto)
					,renderFechas.parse(fevencim)
					,renderFechas.parse(fecancel)
					,cdtipsup
					,usuarioSesion
					,cdsisrol
					,flujo
					,confirmar
					);
			
			success = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success   = " , success
				,"\n###### respuesta = " , respuesta
				,"\n###### confirmarEndosoCancelacionPolAuto ######"
				,"\n###############################################"
				));
		return SUCCESS;
	}
	
	public String endosoValositFormsAuto()
	{
		logger.debug(Utils.log(
				 "\n####################################"
				,"\n###### endosoValositFormsAuto ######"
				,"\n###### smap1="  , smap1
				,"\n###### slist1=" , Utils.size(slist1)
				));
		
		String result = ERROR;
		try
		{
			UserVO user = Utils.validateSession(session);
			
			Utils.validate(smap1  , "No se recibieron datos");
			Utils.validate(slist1 , "No se recibieron incisos");
			
			String cdramo   = smap1.get("CDRAMO");
			String cdtipsup = smap1.get("cdtipsup");
			
			Utils.validate(cdramo    , "No se recibio el producto"
					       ,cdtipsup , "No se recibio el codigo de endoso");
			
			imap = endososAutoManager.endosoValositFormsAuto(
					cdtipsup
					,user.getRolActivo().getClave()
					,cdramo
					,slist1
					);
			
			result = SUCCESS;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### endosoValositFormsAuto ######"
				,"\n####################################"
				));
		return result;
	}
	
	public String confirmarEndosoValositFormsAuto()
	{
		logger.debug(Utils.log(
				 "\n#############################################"
				,"\n###### confirmarEndosoValositFormsAuto ######"
				,"\n###### smap1     = " , smap1
				,"\n###### slist1    = " , slist1
				,"\n###### flujo     = " , flujo
				));
		
		try
		{
			UserVO user = Utils.validateSession(session);
			UserVO usuarioSesion = (UserVO)session.get("USUARIO");
			
			Utils.validate(smap1  , "No se recibieron datos de poliza");
			Utils.validate(slist1 , "No se recibieron incisos");
			
			String cdtipsup = smap1.get("cdtipsup");
			String cdunieco = smap1.get("CDUNIECO");
			String cdramo   = smap1.get("CDRAMO");
			String estado   = smap1.get("ESTADO");
			String nmpoliza = smap1.get("NMPOLIZA");
			String feinival = smap1.get("feinival");
			String confirmar= smap1.get("confirmar");
			String tipoflot = smap1.get("TIPOFLOT");
			String cdperpag = smap1.get("CDPERPAG");
			String p_plan   = smap1.get("CDMEJRED");
			
			Utils.validate(cdtipsup  , "No se recibio el codigo de endoso"
					       ,cdunieco , "No se recibio la sucursal"
					       ,cdramo   , "No se recibio el producto"
					       ,estado   , "No se recibio el estado de la poliza"
					       ,nmpoliza , "No se recibio el numero de poliza"
					       ,feinival , "No se recibio la fecha de endoso"
					       );
			
			smap2 = endososAutoManager.confirmarEndosoValositFormsAuto(
					user.getUser()
					,user.getRolActivo().getClave()
					,user.getEmpresa().getElementoId()
					,cdtipsup
					,cdunieco
					,cdramo
					,estado
					,nmpoliza
					,renderFechas.parse(feinival)
					,slist1
					,usuarioSesion
					,flujo
					,confirmar
					,tipoflot
					,cdperpag
					,p_plan
					);
			
			success = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success   = " , success
				,"\n###### respuesta = " , respuesta
				,"\n###### smap2     = " , smap2
				,"\n###### confirmarEndosoValositFormsAuto ######"
				,"\n#############################################"
				));
		return SUCCESS;
	}
	
	public String endosoRehabilitacionPolAuto()
	{
		logger.debug(Utils.log(
				 "\n#########################################"
				,"\n###### endosoRehabilitacionPolAuto ######"
				,"\n###### smap1=",smap1
				));
		
		String result = ERROR;
		try
		{
			UserVO user = Utils.validateSession(session);
			
			Utils.validate(smap1 , "No se recibieron datos");
			
			String cdramo = smap1.get("CDRAMO");
			
			Utils.validate(cdramo , "No se recibio el producto");
			
			imap = endososAutoManager.confirmarEndosoRehabilitacionPolAuto(user.getRolActivo().getClave(),cdramo);
			
			result = SUCCESS;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### endosoRehabilitacionPolAuto ######"
				,"\n#########################################"
				));
		return result;
	}
	
	public String marcarPolizaParaRehabilitar()
	{
		logger.debug(Utils.log(
				 "\n#########################################"
				,"\n###### marcarPolizaParaRehabilitar ######"
				,"\n###### smap1=",smap1
				));
		
		try
		{
			Utils.validate(smap1 , "No se recibieron datos");
			
			String cdunieco = smap1.get("cdunieco");
			String cdramo   = smap1.get("cdramo");
			String nmpoliza = smap1.get("nmpoliza");
			
			Utils.validate(cdunieco  , "No se recibio la sucursal"
					       ,cdramo   , "No se recibio el producto"
					       ,nmpoliza , "No se recibio el numero de poliza"
					       );
			
			smap1.putAll(endososAutoManager.marcarPolizaParaRehabilitar(cdunieco,cdramo,nmpoliza));
			
			success = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### marcarPolizaParaRehabilitar ######"
				,"\n#########################################"
				));
		return SUCCESS;
	}
	
	public String confirmarEndosoRehabilitacionPolAuto()
	{
		logger.debug(Utils.log(
				 "\n##################################################"
				,"\n###### confirmarEndosoRehabilitacionPolAuto ######"
				,"\n###### smap1=",smap1
				));
		
		try
		{
			Utils.validate(smap1 , "No se recibieron datos");
			
			String cdtipsup = smap1.get("cdtipsup");
			String cdunieco = smap1.get("cdunieco");
			String cdramo   = smap1.get("cdramo"); 
			String estado   = smap1.get("estado");
			String nmpoliza = smap1.get("nmpoliza");
			String f_efecto = smap1.get("feefecto");
			String f_proren = smap1.get("feproren");
			String f_cancel = smap1.get("fecancel");
			String f_inival = smap1.get("feinival");
			String cdrazon  = smap1.get("cdrazon");
			String cdperson = smap1.get("cdperson");
			String cdmoneda = smap1.get("cdmoneda");
			String nmcancel = smap1.get("nmcancel");
			String comments = smap1.get("comments");
			String nmsuplem = smap1.get("nmsuplem");
			
			UserVO usuarioSesion = (UserVO)session.get("USUARIO");
			
			Utils.validate(
					cdtipsup  , "No se recibio el codigo de endoso"
					,cdunieco , "No se recibio la sucursal"
					,cdramo   , "No se recibio el producto"
					,estado   , "No se recibio el estado"
					,nmpoliza , "No se recibio el numero de poliza"
					,f_efecto , "No se recibio el inicio de vigencia de la poliza"
					,f_proren , "No se recibio el fin de vigencia de la poliza"
					,f_cancel , "No se recibio la fecha de cancelacion"
					,f_inival , "No se recibio la fecha de rehabilitacion"
					,cdrazon  , "No se recibio el motivo de cancelacion"
					,cdperson , "No se recibio la clave de person"
					,cdmoneda , "No se recibio la clave de moneda"
					,nmcancel , "No se recibio el consecutivo de cancelacion"
					,nmsuplem , "No se recibio el numero de suplemento"
					);
			
			endososAutoManager.confirmarEndosoRehabilitacionPolAuto(
					cdtipsup
					,cdunieco
					,cdramo
					,estado
					,nmpoliza
					,renderFechas.parse(f_efecto)
					,renderFechas.parse(f_proren)
					,renderFechas.parse(f_cancel)
					,renderFechas.parse(f_inival)
					,cdrazon
					,cdperson
					,cdmoneda
					,nmcancel
					,comments
					,nmsuplem
					,usuarioSesion
					);
			
			success = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### confirmarEndosoRehabilitacionPolAuto ######"
				,"\n##################################################"
				));
		return SUCCESS;
	}
	
	
	public String obtieneRecibosPagados()
	{
		logger.debug(Utils.log(
				 "\n##########################################"
				,"\n###### obtieneRecibosPagados        ######"
				,"\n###### smap1="  , smap1
				));
		
		try
		{
			String cdunieco = smap1.get("CDUNIECO");
			String cdramo   = smap1.get("CDRAMO");
			String estado   = smap1.get("ESTADO");
			String nmpoliza = smap1.get("NMPOLIZA");
			
			slist1 = endososAutoManager.obtieneRecibosPagados(cdunieco, cdramo,estado, nmpoliza);
			
			success = true;
		}
		catch(Exception ex)
		{
			respuesta=Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### obtieneRecibosPagados ######"
				,"\n##########################################"
				));
		return SUCCESS;
	}
	
	public String obtieneRecibosDespagados()
	{
		logger.debug(Utils.log(
				 "\n##########################################"
				,"\n###### obtieneRecibosDespagados        ######"
				,"\n###### smap1="  , smap1
				));
		
		try
		{
			String cdunieco = smap1.get("CDUNIECO");
			String cdramo   = smap1.get("CDRAMO");
			String estado   = smap1.get("ESTADO");
			String nmpoliza = smap1.get("NMPOLIZA");
			
			slist1 = endososAutoManager.obtieneRecibosDespagados(cdunieco, cdramo,estado, nmpoliza);
			
			success = true;
		}
		catch(Exception ex)
		{
			respuesta=Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### obtieneRecibosPagados ######"
				,"\n##########################################"
				));
		return SUCCESS;
	}
	
	public String guardarEndosoRehabilitacionDespago()
	{
		logger.debug(Utils.log(
				 "\n################################################"
				,"\n###### guardarEndosoRehabilitacionDespago ######"
				,"\n###### smap1 = " , smap1
				,"\n###### flujo = " , flujo
				));
		try
		{
			logger.debug("Validando datos de entrada");
			
			Utils.validate(smap1, "No se recibieron datos");
			
			String cdunieco           = smap1.get("CDUNIECO")
					,cdramo           = smap1.get("CDRAMO")
					,estado           = smap1.get("ESTADO")
					,nmpoliza         = smap1.get("NMPOLIZA")
					//,status         = smap1.get("STATUS");
					//,ntramite       = smap1.get("NTRAMITE");
					,nmsuplemOriginal = smap1.get("NMSUPLEM")
					,nmrecibo         = smap1.get("NMRECIBO")
					,nmimpres         = smap1.get("NMIMPRES");
			
			Utils.validate(
					cdunieco  , "No se recibio la sucursal"
					,cdramo   , "No se recibio el producto"
					,estado   , "No se recibio el estado de la poliza"
					,nmpoliza , "No se recibio el numero de poliza"
					);
			
			//Utils.validate(status   , "No se recibio el status");
			
			UserVO usuarioSesion = Utils.validateSession(session);
			
			String cdtipsup      = TipoEndoso.REHABILITACION_DESPAGO.getCdTipSup().toString();
			String fechaEndoso   = smap1.get("FEINIVAL");
//			Date   dFechaEndoso  = renderFechas.parse(fechaEndoso);
			
			endososAutoManager.guardarEndosoRehabilitacionDespago(
					cdunieco,
					cdramo,
					estado,
					nmpoliza,
					nmsuplemOriginal,
					nmrecibo,
					nmimpres,
					cdtipsup,
					usuarioSesion,
					usuarioSesion.getUser(),
					usuarioSesion.getRolActivo().getClave(),
					flujo
					);
			
			respuesta = "Endoso generado correctamente";
			success   = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success  = " , success
				,"\n###### repuesta = " , respuesta
				,"\n###### guardarEndosoRehabilitacionDespago ######"
				,"\n################################################"
				));
		
		return SUCCESS;
	}
	
	public String guardarEndosoNombreRFCFecha()
	{
		logger.debug(Utils.log(
				 "\n#########################################"
				,"\n###### guardarEndosoNombreRFCFecha ######"
				,"\n###### smap1 = " , smap1
				,"\n###### flujo = " , flujo
				));
		try
		{
			logger.debug("Validando datos de entrada");
			
			Utils.validate(smap1, "No se recibieron datos");
			
			String tipoPantalla = smap1.get("tipoPantalla");
			String codigoCliExt = smap1.get("codigoCliExterno");
			String sucursalEnt  = smap1.get("sucursalEntrada");
			String ramoEntrada  = smap1.get("ramoEntrada");
			String polizaEnt    = smap1.get("polizaEntrada");
			String cdpersonNew  = smap1.get("cdpersonNuevo");
			String dsnombreComp = smap1.get("dsnombreComp");
			String tramite		= smap1.get("ntramite");
			String numsuplemen  = smap1.get("nmsuplem");
			String cdunieco     = smap1.get("cdunieco");
			String cdramo       = smap1.get("cdramo");
			String estado       = smap1.get("estado");
			String nmpoliza     = smap1.get("nmpoliza");
			String cdperson     = smap1.get("cdperson");
			String cdtipide     = smap1.get("cdtipide");
			String cdideper     = smap1.get("cdideper");
			String dsnombre     = smap1.get("dsnombre");
			String cdtipper     = smap1.get("cdtipper");
			String otfisjur     = smap1.get("otfisjur");
			String otsexo       = smap1.get("otsexo");
			String fenacimi     = smap1.get("fenacimi");
			String cdrfc        = smap1.get("cdrfc");
			String dsemail      = smap1.get("dsemail");
			String dsnombre1    = smap1.get("dsnombre1");
			String dsapellido   = smap1.get("dsapellido");
			String dsapellido1  = smap1.get("dsapellido1");
			String feingreso    = smap1.get("feingreso");
			String cdnacion     = smap1.get("cdnacion");
			String canaling     = smap1.get("canaling");
			String conducto     = smap1.get("conducto");
			String ptcumupr     = smap1.get("ptcumupr");
			String residencia   = smap1.get("residencia");
			String nongrata     = smap1.get("nongrata");
			String cdideext     = smap1.get("cdideext");
			String cdestciv     = smap1.get("cdestciv");
			String cdsucemi     = smap1.get("cdsucemi");
			
			UserVO usuarioSesion= (UserVO)session.get("USUARIO");
			String cdusuari     = ((UserVO)session.get("USUARIO")).getUser();
			String cdsisrol     = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
			String cdelemen     = ((UserVO)session.get("USUARIO")).getEmpresa().getElementoId();
			String cdtipsup     = TipoEndoso.REHABILITACION_NOMBRE_RFC_FENAC.getCdTipSup().toString();
			SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
			String fechaEndoso  = sdf.format(new Date());
			Date   dFechaEndoso = renderFechas.parse(fechaEndoso);
			String urlCaratula =  this.caratulaImpresionAutosEndosobUrl;
			
			Utils.validate(tipoPantalla   , "No se recibio el origen del llamado");
			
			if(tipoPantalla.equalsIgnoreCase("0")){
				Utils.validate(cdunieco , "No se recibio la sucursal");
				Utils.validate(cdramo   , "No se recibio el producto");
				Utils.validate(estado   , "No se recibio el estado de la poliza");
				Utils.validate(nmpoliza , "No se recibio el numero de poliza");
				Utils.validate(session                , "No hay sesion");
				Utils.validate(session.get("USUARIO") , "No hay usuario en la sesion");
			}else{
				//Utils.validate(codigoCliExt , "No se recibio el codigo de cliente externo");
				Utils.validate(sucursalEnt  , "No se recibio la sucursal");
				Utils.validate(ramoEntrada  , "No se recibio el producto");
				Utils.validate(polizaEnt    , "No se recibio el numero de poliza");
				//Utils.validate(cdpersonNew  , "No se recibio el asegurado");
			}
	
	
			String fenacimientoM= fenacimi.substring(8,10)+"/"+fenacimi.substring(5,7)+"/"+fenacimi.substring(0,4);
			int claveEndoso = endososAutoManager.guardarEndosoNombreRFCFecha(
					cdunieco,		cdramo,			estado,			nmpoliza,		cdperson,							cdtipide,			cdideper,
					dsnombre,		cdtipper,		otfisjur,		otsexo,			renderFechas.parse(fenacimientoM),	cdrfc,				dsemail,
					dsnombre1,		dsapellido,		dsapellido1,	feingreso,		cdnacion,							canaling,			conducto,
					ptcumupr,		residencia,		nongrata,		cdideext,		cdestciv,							cdsucemi,			cdusuari,
					cdsisrol,		cdelemen,		cdtipsup,		fechaEndoso,	dFechaEndoso,						tipoPantalla,		codigoCliExt,
					sucursalEnt,	ramoEntrada,	polizaEnt,		cdpersonNew,	dsnombreComp,
					tramite,		numsuplemen,	urlCaratula, usuarioSesion,     flujo
			);
			
			if(smap2==null)
			{
				smap2=new HashMap<String,String>();
			}
			smap2.put("numEndosoSIGS" ,claveEndoso+"");
			
			
			respuesta = "Endoso generado correctamente. N\u00famero de endoso : "+claveEndoso;
			success   = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success   = " , success
				,"\n###### respuesta = " , respuesta
				,"\n###### guardarEndosoNombreRFCFecha ######"
				,"\n#########################################"
				));
		
		return SUCCESS;
	}
	
	public String guardarEndosoDomicilioNoSICAPS() {
        
		logger.debug(Utils.log(
				"\n#############################################"
				,"\n############################################"
				,"\n###### guardarEndosoDomicilioNoSICAPS ######"
				,"\n###### smap1="  , smap1
				,"\n######                             	  ######"));
		try
		{
			logger.debug("Validando datos de entrada");
			Utils.validate(smap1, "No se recibieron datos");
			
			String tipoPantalla = smap1.get("tipoPantalla");
			String sucursalEnt  = smap1.get("sucursalEntrada");
			String ramoEntrada  = smap1.get("ramoEntrada");
			String polizaEnt    = smap1.get("polizaEntrada");
			String codigoCliExt = smap1.get("codigoCliExterno");
			String cdpersonNew  = smap1.get("cdpersonNuevo");
			String codigoPostal = smap1.get("codPostal");
			String cveEstado    = smap1.get("cveEdo");
			String estado       = smap1.get("desEdo");
			String cveEdoSISG   = smap1.get("cveEdo").substring(5);
			String cveMinicipio = smap1.get("cveMunicipio");
			String municipio    = smap1.get("desMunicipio");
			String cveMunSISG   = smap1.get("cveMunicipio").substring(7);
			String cveColonia   = smap1.get("cveColonia");
			String colonia      = smap1.get("desColonia");
			String calle        = smap1.get("desCalle");
			String numExterior  = smap1.get("numExterior");
			String numInterior  = smap1.get("numInterior");
			String telefono1  = smap1.get("telefono1");
			String telefono2  = smap1.get("telefono2");
			String telefono3  = smap1.get("telefono3");
			//String   = smap1.get("");
			
			UserVO usuarioSesion= (UserVO)session.get("USUARIO");
			String cdusuari     = ((UserVO)session.get("USUARIO")).getUser();
			String cdsisrol     = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
			String cdelemen     = ((UserVO)session.get("USUARIO")).getEmpresa().getElementoId();
			String cdtipsup     = TipoEndoso.CAMBIO_DOMICILIO.getCdTipSup().toString();
			SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
			String fechaEndoso  = sdf.format(new Date());
			Date   dFechaEndoso = renderFechas.parse(fechaEndoso);
			String urlCaratula =  this.caratulaImpresionAutosEndosobUrl;
			
			
			int claveEndoso = endososAutoManager.guardarEndosoDomicilioNoSICAPS(
					tipoPantalla, sucursalEnt, ramoEntrada, polizaEnt, codigoCliExt, cdpersonNew, codigoPostal, cveEstado,
					estado,	cveEdoSISG, cveMinicipio, municipio, cveMunSISG, cveColonia, colonia, calle, numExterior, numInterior,
					cdusuari, cdsisrol, cdelemen, cdtipsup, fechaEndoso, dFechaEndoso, urlCaratula, telefono1, telefono2, telefono3, usuarioSesion
			);
			
			Utils.validate(tipoPantalla   , "No se recibio el origen del llamado");
			Utils.validate(sucursalEnt  , "No se recibio la sucursal");
			Utils.validate(ramoEntrada  , "No se recibio el producto");
			Utils.validate(polizaEnt    , "No se recibio el numero de poliza");
			if(smap2==null)
			{
				smap2=new HashMap<String,String>();
			}
			smap2.put("numEndosoSIGS" ,claveEndoso+"");
			
			
			respuesta = "Endoso generado correctamente. N\u00famero de endoso : "+claveEndoso;
			success   = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### guardarEndosoDomicilioNoSICAPS ######"
				,"\n############################################"
				));
		return SUCCESS;
	}
	
	public String endosoBenefiarioVidaAuto(){
        logger.debug(Utils.log(""
                ,"\n######################################"
                ,"\n###### endosoBenefiarioVidaAuto ######"
                ,"\n###### smap1  = " ,smap1
                ,"\n###### slist1 = " ,slist1
                ));
        
        String result = ERROR;
        
        try
        {
            String paso = null;
            try
            {   
                paso = "Validando datos de entrada";
                
               Utils.validate(smap1, "No se recibieron datos");
                
                EndososAction endososAction = new EndososAction();
                endososAction.setEndososManager(endososManager);
                endososAction.transformaEntrada(smap1, slist1, true);
                
               //validados
                String cdunieco    = smap1.get("cdunieco")
                       ,cdramo     = smap1.get("cdramo")
                       ,estado     = smap1.get("estado")
                       ,nmpoliza   = smap1.get("nmpoliza")
                       ,nmsuplem   = smap1.get("nmsuplem")
                       ,nmsituac   = "0"//smap1.get("nmsituac")
                       ,cdrolPipes = "3"//smap1.get("cdrolPipes")
                       ,cdtipsup   = smap1.get("cdtipsup")
                       ,ntramite   = smap1.get("ntramite");
                
                Utils.validate(
                        cdunieco   , "No se recibio la sucursal"
                        ,cdramo     , "No se recibio el producto"
                        ,estado     , "No se recibio el estado de la poliza"
                        ,nmpoliza   , "No se recibio el numero de poliza"
                        ,nmsuplem   , "No se recibio el numero de suplemento"
                        ,nmsituac   , "No se recibio el numero de situacion"
                        ,cdrolPipes , "No se recibieron los roles permitidos"
                        ,cdtipsup   , "No se recibio el tipo de suplemento"
                        );
                
                if(!cdtipsup.equals("1"))
                {
                    Utils.validate(ntramite, "No se recibio el numero de tramite");
                }
                
                //no validados
                String ultimaImagen = smap1.get("ultimaImagen");
                if(StringUtils.isBlank(ultimaImagen)
                        ||!ultimaImagen.equals("S"))
                {
                    ultimaImagen="N";
                }
                
                smap1.put("ultimaImagen" , ultimaImagen);
                
                String cdsisrol = Utils.validateSession(session).getRolActivo().getClave();
                
                imap = endososAutoManager.pantallaBeneficiariosAutoVida(cdunieco,cdramo,estado,cdsisrol,cdtipsup);
                
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
                ,"\n###### endosoBenefiarioVidaAuto ######"
                ,"\n######################################"
                ));
        
        return result;
    }
    
       public String obtieneBeneficiariosVidaAuto()
        {
            logger.debug(Utils.log(
                     "\n##########################################"
                    ,"\n###### obtieneBeneficiariosVidaAuto ######"
                    ,"\n###### smap1="  , smap1
                    ));
            
            try
            {
                String cdunieco = smap1.get("cdunieco");
                String cdramo   = smap1.get("cdramo");
                String estado   = smap1.get("estado");
                String nmpoliza = smap1.get("nmpoliza");
                logger.debug(Utils.log(""
                        ,"\n cdunieco = ", cdunieco
                        ,"\n cdramo   = ", cdramo
                        ,"\n estado   = ", estado
                        ,"\n nmpoliza = ", nmpoliza
                        ,"\n###### obtieneBeneficiariosVidaAuto ######"
                        ));
                
                slist1 = endososAutoManager.obtieneBeneficiariosVidaAuto(cdunieco, cdramo,estado, nmpoliza);
                
                success = true;
            }
            catch(Exception ex)
            {
                respuesta=Utils.manejaExcepcion(ex);
            }
            
            logger.debug(Utils.log(
                     "\n###### obtieneBeneficiariosVidaAuto ######"
                    ,"\n###### slist1 => " , slist1
                    ,"\n##########################################"
                    ));
            return SUCCESS;
        }
       public String confirmaEndosoBeneficiariosVidaAuto()
       {
           logger.debug(Utils.log(
                    "\n#################################################"
                   ,"\n###### confirmaEndosoBeneficiariosVidaAuto ######"
                   ,"\n###### smap1="  , smap1
                   ,"\n###### slist1=" , slist1
                   ,"\n###### flujo="  , flujo
                   ));
           
           try
           {
               Utils.validate(session                , "No hay sesion");
               Utils.validate(session.get("USUARIO") , "No hay usuario en la sesion");
               
               UserVO usuarioSesion = (UserVO)session.get("USUARIO");
               String cdusuari = ((UserVO)session.get("USUARIO")).getUser();
               String cdsisrol = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
               String cdelemen = ((UserVO)session.get("USUARIO")).getEmpresa().getElementoId();
               
               Utils.validate(smap1  , "No se recibieron datos");
               Utils.validate(slist1 , "No se recibieron datos de inciso");
               String cdtipsup = smap1.get("cdtipsup");
               String tstamp   = smap1.get("tstamp");
               String cdunieco = smap1.get("cdunieco");
               String cdramo   = smap1.get("cdramo");
               String estado   = smap1.get("estado");
               String nmpoliza = smap1.get("nmpoliza");
               String feefecto = smap1.get("feefecto");
               String nmsituac = smap1.get("nmsituac");
               String ntramite = smap1.get("ntramite");
               
               Utils.validate(cdtipsup  , "No se recibio el tipo de endoso");
               Utils.validate(tstamp    , "No se recibio el id de proceso");
               Utils.validate(cdunieco  , "No se recibio la sucursal");
               Utils.validate(cdramo    , "No se recibio el producto");
               Utils.validate(estado    , "No se recibio el estado");
               Utils.validate(nmpoliza  , "No se recibio el numero de poliza");
               Utils.validate(feefecto  , "No se recibio la fecha de efecto"); 
               
               ManagerRespuestaVoidVO resp = endososAutoManager.confirmaEndosoBeneficiariosVidaAuto(
                       cdtipsup
                       ,tstamp
                       ,cdunieco 
                       ,cdramo
                       ,estado
                       ,nmpoliza
                       ,nmsituac
                       ,feefecto
                       ,cdusuari 
                       ,cdsisrol
                       ,cdelemen
                       ,usuarioSesion
                       ,slist1
                       ,flujo
                       );
               
               success         = resp.isExito();
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
                    "\n###### success="   , success
                   ,"\n###### exito="     , exito
                   ,"\n###### respuesta=" , respuesta
                   ,"\n###### error    =" , error
                   ,"\n###### confirmaEndosoBeneficiariosVidaAuto ######"
                   ,"\n#################################################"
                   ));
           return SUCCESS;
       }
       
       public String endosoAjusteSiniestralidad()
       {
           logger.debug(Utils.log(
                    "\n########################################"
                   ,"\n###### endosoAjusteSiniestralidad ######"
                   ,"\n###### smap1="  , smap1
                   ,"\n###### slist1=" , slist1
                   ));

           String result = ERROR;
           
           try
           {
               Utils.validate(smap1  , "No se recibieron datos de poliza");
               Utils.validate(slist1 , "No se recibieron incisos");
               
               String cdramo   = smap1.get("CDRAMO");
               String cdtipsup = smap1.get("cdtipsup");
               logger.debug(smap1);
               
               Utils.validate(cdramo   , "No se recibio el producto");
               Utils.validate(cdtipsup , "No se recibio el codigo de endoso");
               
               smap1.put("tstamp" , Utils.generaTimestamp());
               
               imap = endososAutoManager.endosoAjusteSiniestralidad(cdtipsup, cdramo);
               
               result = SUCCESS;
           }
           catch(Exception ex)
           {
               respuesta = Utils.manejaExcepcion(ex);
           }
           
           logger.debug(Utils.log(
                   "\n###### endosoAjusteSiniestralidad ######"
                   ,"\n#######################################"
                   ));
           return result;
       }
       
       public String guardarEndosoAjusteSiniestralidad()
       {
           logger.debug(Utils.log(
                    "\n###############################################"
                   ,"\n###### guardarEndosoAjusteSiniestralidad ######"
                   ,"\n###### smap1  = " , smap1
                   ,"\n###### smap2  = " , smap2
                   ,"\n###### slist1 = " , slist1
                   ,"\n###### flujo  = " , flujo
                   ));
           
           try
           {
               UserVO user = Utils.validateSession(session);
               logger.debug("Iniciando impresion de valores");
               logger.debug(smap1);
               logger.debug(smap2);
               logger.debug(slist1);
               logger.debug(flujo);
               logger.debug("Terminando impresion de valores");
               
               
               Utils.validate(smap1  , "No se recibieron datos de poliza");
               Utils.validate(smap2  , "No se recibieron datos de endoso");
               Utils.validate(slist1 , "No se recibieron incisos");
               
               
               String   cdunieco = smap1.get("CDUNIECO")
                       ,cdramo   = smap1.get("CDRAMO")
                       ,estado   = smap1.get("ESTADO")
                       ,nmpoliza = smap1.get("NMPOLIZA")
                       ,cdtipsup = smap1.get("cdtipsup")
                       ,tstamp   = smap1.get("tstamp")
                       ,fechaEnd = smap2.get("feefecto")
                       ,nmtramite= smap1.get("NTRAMITE");
               Utils.validate(
                       cdunieco  , "No se recibio la sucursal"
                       ,cdramo   , "No se recibio el producto"
                       ,estado   , "No se recibio el estado de la poliza"
                       ,nmpoliza , "No se recibio el numero de poliza"
                       ,cdtipsup , "No se recibio el codigo de endoso"
                       ,tstamp   , "No se recibio el ID de proceso"
                       ,fechaEnd , "No se recibio la fecha de efecto"
                       );
               
               omap1 = endososAutoManager.guardarEndosoAjusteSiniestralidad(
                       user.getUser()
                       ,user.getRolActivo().getClave()
                       ,user.getEmpresa().getElementoId()
                       ,cdunieco
                       ,cdramo
                       ,estado
                       ,nmpoliza
                       ,cdtipsup
                       ,tstamp
                       ,renderFechas.parse(fechaEnd)
                       ,slist1
                       ,user
                       ,flujo
                       ,nmtramite
                       );
               
               success = true;
           }
           catch(Exception ex)
           {
               respuesta = Utils.manejaExcepcion(ex);
           }
           
           logger.debug(Utils.log(
                    "\n###### success   = " , success
                   ,"\n###### respuesta = " , respuesta
                   ,"\n###### omap1     = " , omap1
                   ,"\n###### guardarEndosoAjusteSiniestralidad ######"
                   ,"\n###############################################"
                   ));
           return SUCCESS;
       }
       
	/*
	 * Getters y setters
	 */
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public Map<String, String> getSmap1() {
		return smap1;
	}

	public void setSmap1(Map<String, String> smap1) {
		this.smap1 = smap1;
	}
	
	public Map<String, Object> getOmap1() {
		return omap1;
	}

	public void setOmap1(Map<String, Object> omap1) {
		this.omap1 = omap1;
	}

	public List<Map<String, String>> getSlist1() {
		return slist1;
	}

	public void setSlist1(List<Map<String, String>> slist1) {
		this.slist1 = slist1;
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
	
	public FlujoVO getFlujo() {
		return flujo;
	}

	public void setFlujo(FlujoVO flujo) {
		this.flujo = flujo;
	}
	
	public List<Map<String, String>> getSlist2() {
		return slist2;
	}

	public void setSlist2(List<Map<String, String>> slist2) {
		this.slist2 = slist2;
	}
	
	public String getCaratulaImpresionAutosEndosobUrl() {
		return caratulaImpresionAutosEndosobUrl;
	}

	public String getLogsRuta() {
		return logsRuta;
	}
}