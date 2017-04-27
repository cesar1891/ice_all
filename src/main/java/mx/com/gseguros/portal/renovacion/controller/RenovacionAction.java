package mx.com.gseguros.portal.renovacion.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.consultas.service.ConsultasManager;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaImapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSlistVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaVoidVO;
import mx.com.gseguros.portal.cotizacion.service.CotizacionManager;
import mx.com.gseguros.portal.general.model.UsuarioVO;
import mx.com.gseguros.portal.general.util.RolSistema;
import mx.com.gseguros.portal.general.util.TipoEndoso;
import mx.com.gseguros.portal.renovacion.service.RenovacionManager;
import mx.com.gseguros.utils.Utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
@Controller
@Scope("prototype")
@ParentPackage(value="default")
@Namespace("/renovacion")
public class RenovacionAction extends PrincipalCoreAction
{
	private static final long        serialVersionUID = 6666057023524182296L;
	private static final Logger      logger           = Logger.getLogger(RenovacionAction.class);
	private boolean                  success          = true;
	private boolean                  exito            = false;
	private Map<String,String>       smap1            = null;
	private Map<String,String>       params           = null;
	private String                   respuesta;
	private String                   respuestaOculta  = null;
	private Map<String,Item>         imap             = null;
	private List<Map<String,String>> slist1           = null;
	
	//Dependencias inyectadas
	@Autowired
	private RenovacionManager renovacionManager;
	
	@Autowired
	private CotizacionManager cotizacionManager;
	
	@Autowired
	private ConsultasManager  consultasManager;

	@Value("${ruta.servidor.reports}")
    private String rutaServidorReports;
    
    @Value("${pass.servidor.reports}")
    private String passServidorReports;	
    
    @Value("${ruta.documentos.poliza}")
    private String rutaDocumentosPoliza;
    
	public String pantallaRenovacion()
	{
		logger.info(
				new StringBuilder()
				.append("\n################################")
				.append("\n###### pantallaRenovacion ######")
				.toString()
				);
		
		exito = true;
		
		String cdsisrol = null;
		
		//sesion
		try
		{
			if(session==null)
			{
				throw new ApplicationException("No hay sesion");
			}
			UserVO usuario = (UserVO)session.get("USUARIO");
			if(usuario==null)
			{
				throw new ApplicationException("No hay usuario en sesion");
			}
			cdsisrol = usuario.getRolActivo().getClave();
			if(StringUtils.isBlank(cdsisrol))
			{
				throw new ApplicationException("No hay rol en la sesion");
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
			respuesta       = new StringBuilder("Error al obtener datos de sesion #").append(timestamp).toString();
			respuestaOculta = ex.getMessage();
			logger.error(respuesta,ex);
		}
		
		//proceso
		if(exito)
		{
			ManagerRespuestaImapVO managerResponse = renovacionManager.pantallaRenovacion(cdsisrol);
			exito           = managerResponse.isExito();
			respuesta       = managerResponse.getRespuesta();
			respuestaOculta = managerResponse.getRespuestaOculta();
			if(exito)
			{
				imap = managerResponse.getImap();
			}
		}
			
		logger.info(
				new StringBuilder()
				.append("\n###### pantallaRenovacion ######")
				.append("\n################################")
				.toString()
				);
		
		String result = SUCCESS;
		if(!exito)
		{
			result = ERROR;
		}
		return result;
	}
	
	/**
	 * Busca polizas renovables
	 * @param smap1.anio
	 * @param smap1.mes
	 * @return success
	 * @return slist1
	 */
	public String buscarPolizasRenovables()
	{
		logger.info(
				new StringBuilder()
				.append("\n#####################################")
				.append("\n###### buscarPolizasRenovables ######")
				.append("\n###### smap1=").append(smap1)
				.toString()
				);
		
		success = true;
		
		String cdunieco = null;
		String cdramo   = null;
		String anio     = null;
		String mes      = null;
		
		//datos completos
		try
		{
			if(smap1==null)
			{
				throw new ApplicationException("No se recibieron datos");
			}
			cdunieco = smap1.get("cdunieco");
			cdramo   = smap1.get("cdramo");
			anio     = smap1.get("anio");
			mes      = smap1.get("mes");
			if(StringUtils.isBlank(anio))
			{
				throw new ApplicationException("No se recibio el año");
			}
			if(StringUtils.isBlank(mes))
			{
				throw new ApplicationException("No se recibio el mes");
			}
		}
		catch(ApplicationException ax)
		{
			long timestamp  = System.currentTimeMillis();
			success         = false;
			respuesta       = new StringBuilder(ax.getMessage()).append(" #").append(timestamp).toString();
			respuestaOculta = ax.getMessage();
			logger.error(respuesta,ax);
		}
		catch(Exception ex)
		{
			long timestamp  = System.currentTimeMillis();
			success         = false;
			respuesta       = new StringBuilder("Error al obtener datos para buscar polizas #").append(timestamp).toString();
			respuestaOculta = ex.getMessage();
			logger.error(respuesta,ex);
		}
		
		//proceso
		if(success)
		{
			ManagerRespuestaSlistVO managerResp = renovacionManager.buscarPolizasRenovables(cdunieco,cdramo,anio,mes);
			success         = managerResp.isExito();
			respuesta       = managerResp.getRespuesta();
			respuestaOculta = managerResp.getRespuestaOculta();
			if(success)
			{
				slist1 = managerResp.getSlist();
			}
		}
		
		logger.info(
				new StringBuilder()
				.append("\n###### slist1=").append(slist1)
				.append("\n###### buscarPolizasRenovables ######")
				.append("\n#####################################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String renovarPolizas()
	{
		logger.info(
				new StringBuilder()
				.append("\n############################")
				.append("\n###### renovarPolizas ######")
				.append("\n###### slist1=").append(slist1)
				.toString()
				);
		
		exito = true;
		
		String anio     = null;
		String mes      = null;
		String cdusuari = null;
		
		UserVO usuario = null;
		
		//datos completos
		try
		{
			session=ActionContext.getContext().getSession();
			if(session==null)
			{
				throw new ApplicationException("No hay sesion");
			}
			if(session.get("USUARIO")==null)
			{
				throw new ApplicationException("No hay usuario en la sesion");
			}
			usuario = (UserVO)session.get("USUARIO");
			cdusuari       = usuario.getUser();
			if(StringUtils.isBlank(cdusuari))
			{
				throw new ApplicationException("No hay clave de usuario");
			}
			if(slist1==null||slist1.size()==0)
			{
				throw new ApplicationException("No se recibieron polizas");
			}
			if(smap1==null)
			{
				throw new ApplicationException("No se recibieron datos de busqueda");
			}
			anio = smap1.get("anio");
			mes  = smap1.get("mes");
			if(StringUtils.isBlank(anio))
			{
				throw new ApplicationException("No hay año");
			}
			if(StringUtils.isBlank(mes))
			{
				throw new ApplicationException("No hay mes");
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
			respuesta       = new StringBuilder("Error inesperado al obtener datos #").append(timestamp).toString();
			respuestaOculta = ex.getMessage();
			logger.error(respuesta,ex);
		}
		
		//proceso
		if(exito)
		{
			ManagerRespuestaVoidVO resp = renovacionManager.renovarPolizas(
					slist1
					,cdusuari
					,anio
					,mes
					,rutaDocumentosPoliza
					,rutaServidorReports
					,passServidorReports
					,usuario
					);
			exito           = resp.isExito();
			respuesta       = resp.getRespuesta();
			respuestaOculta = resp.getRespuestaOculta();
		}
		
		logger.info(
				new StringBuilder()
				.append("\n###### renovarPolizas ######")
				.append("\n############################")
				.toString()
				);
		return SUCCESS;
	}

	public String renovacionIndividual()
	{
		logger.info(
				new StringBuilder()
				.append("\n#####################################")
				.append("\n###### renovacionIndividual ######")
				.append("\n###### smap1=").append(smap1)
				.toString()
				);
		
		success = true;
		
		String cdsisrol = null;
		
		//datos completos
		try
		{
			UserVO usuario = (UserVO)session.get("USUARIO");
			cdsisrol = usuario.getRolActivo().getClave();
			ManagerRespuestaImapVO managerResponse = renovacionManager.pantallaRenovacionIndividual(cdsisrol);
			exito           = managerResponse.isExito();
			respuesta       = managerResponse.getRespuesta();
			respuestaOculta = managerResponse.getRespuestaOculta();
			if(exito)
			{
				imap = managerResponse.getImap();
			}
		}
		catch(Exception ex)
		{
			long timestamp  = System.currentTimeMillis();
			success         = false;
			respuesta       = new StringBuilder("Error al obtener atributos de pantalla #").append(timestamp).toString();
			respuestaOculta = ex.getMessage();
			logger.error(respuesta,ex);
		}
		logger.info(
				new StringBuilder()
				.append("\n###### slist1=").append(slist1)
				.append("\n###### renovacionIndividual ######")
				.append("\n#####################################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String buscarPolizasIndividualesRenovables()
	{
		logger.info(
				new StringBuilder()
				.append("\n#####################################")
				.append("\n###### buscarPolizasIndividualesRenovables ######")
				.append("\n###### smap1=").append(smap1)
				.toString()
				);
		
		success = true;
		
		//datos completos
		try{
			Utils.validate(smap1, "No se recibieron datos");
			Utils.validate(smap1.get("cdunieco"), "No se recibio la oficina",
						   smap1.get("cdramo")  , "No se recibio el producto",
						   smap1.get("estado")  , "No se recibio el estado",
						   smap1.get("nmpoliza"), "No se recibio la poliza");
			String cdunieco = smap1.get("cdunieco");
			String cdramo   = smap1.get("cdramo");
			String estado   = smap1.get("estado");
			String nmpoliza = smap1.get("nmpoliza");			
            String cdusuari  = null,
                   cdsisrol  = null;
            UserVO usuario = Utils.validateSession(session);
            if(usuario.getRolActivo().getClave().equals(RolSistema.AGENTE.getCdsisrol())){
                cdusuari  = usuario.getUser();
                cdsisrol  = usuario.getRolActivo().getClave();
            }
            logger.info(new StringBuilder().append("\n###### cdusuari=").append(cdusuari).append("\n###### cdsisrol=").append(cdsisrol));
            //proceso
			ManagerRespuestaSlistVO managerResp = renovacionManager.buscarPolizasRenovacionIndividual(cdunieco, cdramo, estado, nmpoliza, cdusuari, cdsisrol);
			logger.info(new StringBuilder().append("managerResp ").append(managerResp).toString());
			slist1 			= managerResp.getSlist();
			success         = managerResp.isExito();
			respuesta       = managerResp.getRespuesta();
			respuestaOculta = managerResp.getRespuestaOculta();
			
		}catch(Exception ex){
			respuesta = Utils.manejaExcepcion(ex);
		}
		logger.info(
				new StringBuilder()
				.append("\n###### slist1=").append(slist1)
				.append("\n###### buscarPolizasIndividualesRenovables ######")
				.append("\n#####################################")
				.toString()
				);
		return SUCCESS;
	}
	
	
	public String buscarPolizasIndividualesMasivasRenovables()
	{
		logger.info(
				new StringBuilder()
				.append("\n#####################################")
				.append("\n###### buscarPolizasIndividualesMasivasRenovables ######")
				.append("\n###### smap1=").append(smap1)
				.toString()
				);
		
		success = true;
		ManagerRespuestaSlistVO managerResp = null;
		//datos completos
		try{	
			Utils.validate(smap1, "No se recibieron datos");
			Utils.validate(smap1.get("fecini")  , "No se recibio la fecha inicio",
					   	   smap1.get("fecfin")  , "No se recibio la fecha fin",
					   	   smap1.get("cdramo")  , "No se recibio el producto"
					);
			String cdunieco   = smap1.get("cdunieco");
			String cdramo     = smap1.get("cdramo");
			String estado     = smap1.get("estado");
			String nmpoliza   = null;//smap1.get("nmpoliza");
			String cdtipsit	  = smap1.get("cdtipsit");
			String fecini	  = smap1.get("fecini");
			String fecfin	  = smap1.get("fecfin");
			String status	  = smap1.get("status");
			String retenedora = smap1.get("retenedora");
			String cdperson	  = smap1.get("cdperson");
			//proceso
			UserVO usuario = Utils.validateSession(session);
			String cdusuari  = null,
	               cdsisrol  = null;
			if(usuario.getRolActivo().getClave().equals(RolSistema.AGENTE.getCdsisrol())){
			    cdusuari  = usuario.getUser();
		        cdsisrol  = usuario.getRolActivo().getClave();
			}
			logger.info(new StringBuilder().append("\n###### cdusuari=").append(cdusuari).append("\n###### cdsisrol=").append(cdsisrol));
			managerResp = renovacionManager.buscarPolizasRenovacionIndividualMasiva(
					cdunieco, 
					cdramo, 
					estado, 
					nmpoliza, 
					cdtipsit, 
					fecini, 
					fecfin, 
					status,
					cdperson,
					retenedora,
					cdusuari,
					cdsisrol
					);
			logger.info(new StringBuilder().append("managerResp ").append(managerResp).toString());
			slist1 			= managerResp.getSlist();
			success         = managerResp.isExito();
			respuesta       = managerResp.getRespuesta();
			respuestaOculta = managerResp.getRespuestaOculta();
			exito = true;
		}catch(Exception ex){
			respuesta 		= Utils.manejaExcepcion(ex);
		}
		logger.info(
				new StringBuilder()
				.append("\n###### slist1=").append(slist1)
				.append("\n###### buscarPolizasIndividualesMasivasRenovables ######")
				.append("\n#####################################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String renovarPolizaIndividual(){
		logger.info(
				new StringBuilder()
				.append("\n###### params=").append(params)
				.append("\n###### Entrando a renovarPolizaIndividual ######")
				.append("\n################################################")
				.toString()
				);
		try{
			Utils.validate(params, "No se recibieron datos");
			Utils.validate(params.get("cdunieco"),"No se recibio oficina",
						   params.get("cdramo")  ,"No se recibio producto",
						   params.get("estado")  ,"No se recibio estado",
						   params.get("nmpoliza"),"No se recibio numero de poliza",
						   params.get("feefecto"),"No se recibio la fecha de efecto",
						   params.get("feproren"),"No se recibio la fecha de proxima renovacion",
						   params.get("cdmoneda"),"No se recibio la moneda"
						   );
			String cdunieco  = params.get("cdunieco");
			String cdramo    = params.get("cdramo");
			String estado    = params.get("estado");
			String nmpoliza  = params.get("nmpoliza");
			String feefecto  = params.get("feefecto");
			String feproren  = params.get("feproren");
			String cdmoneda  = params.get("cdmoneda");
			String estadoNew = params.get("estadoNew");
			UserVO usuario   = Utils.validateSession(session);			
			ManagerRespuestaSlistVO resp = renovacionManager.renuevaPolizaIndividual(
					cdunieco, 
					cdramo, 
					estado, 
					nmpoliza, 
					usuario.getUser(),
					feefecto,
					feproren,
					estadoNew,
					cdmoneda);
			logger.info(
					new StringBuilder()
					.append("\n###### resp ")
					.append(resp)
					.toString()
					);
			if(resp.isExito()){
				setSlist1(resp.getSlist());
				exito = true;
				respuesta = resp.getRespuesta();
			}
			else{
				exito = false;
				respuesta = resp.getRespuesta();
			}
			
		}catch(Exception ex){
			respuesta = Utils.manejaExcepcion(ex);
		}
		logger.info(
				new StringBuilder()
				.append("\n###### Saliendo de renovarPolizaIndividual ######")
				.append("\n#################################################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String actualizaValoresCotizacion(){
		logger.info(
				new StringBuilder()
				.append("\n###### params=").append(params)
				.append("\n###### Entrando a actualizaValoresCotizacion ######")
				.append("\n################################################")
				.toString()
				);
		try{
			UserVO usuario = (UserVO)session.get("USUARIO");			
			renovacionManager.actualizaValoresCotizacion(
					params, 
					usuario.getUser(), 
					usuario.getEmpresa().getElementoId(), 
					TipoEndoso.RENOVACION.getCdTipSup().toString());
			
		}catch(Exception ex){
			respuesta = Utils.manejaExcepcion(ex);
		}
		logger.info(
				new StringBuilder()
				.append("\n###### Saliendo de actualizaValoresCotizacion ######")
				.append("\n#################################################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String confirmarPolizaIndividual(){
		logger.info(
				new StringBuilder()
				.append("\n###### params=").append(params)
				.append("\n###### Entrando a confirmarPolizaIndividual ######")
				.append("\n################################################")
				.toString()
				);
		try{
			Utils.validate(params, "No se recibieron datos");
			Utils.validate(params.get("cdunieco"),"No se recibio oficina",
						   params.get("cdramo")  ,"No se recibio producto",
						   params.get("estado")  ,"No se recibio estado",
						   params.get("nmpoliza"),"No se recibio numero de poliza",
						   params.get("nmsuplem"),"No se recibio nmsuplem",
						   params.get("ntramite"),"No se recibio numero de tramite",
						   params.get("cdperpag"),"No se recibio el periodo de pago",
						   params.get("feefecto"),"No se recibio la fecha de efecto"
						   );
			String cdunieco = params.get("cdunieco");
			String cdramo   = params.get("cdramo");
			String estado   = params.get("estado");
			String nmpoliza = params.get("nmpoliza");
			String nmsuplem = params.get("nmsuplem");
			String ntramite = params.get("ntramite");
			String cdperpag = params.get("cdperpag");
			String feefecto = params.get("feefecto");
			UserVO usuario = (UserVO)session.get("USUARIO");
			Map<String, String> result = renovacionManager.confirmarCotizacion(
					cdunieco, 
					cdramo, 
					estado, 
					nmpoliza, 
					nmsuplem, 
					ntramite,
					cdperpag,
					feefecto,
					usuario,
					rutaDocumentosPoliza//,
//					rutaServidorReports,
//					passServidorReports
					);
			List<Map<String, String>> lista = new ArrayList<Map<String,String>>();			
			logger.info(new StringBuilder().append("\n###### lista").append(lista));
			if(!result.isEmpty()){
				lista.add(result);
				respuesta = "Poliza confirmada";
				exito = true;
			}else{
				respuesta = "No se pudo confirmar la poliza";
			}
			setSlist1(lista);
		}catch(Exception ex){
			respuesta = Utils.manejaExcepcion(ex);
		}
		logger.info(
				new StringBuilder()
				.append("\n###### Saliendo de renovarPolizaIndividual ######")
				.append("\n#################################################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String obtenerItemsTvalopol(){
		logger.info(
				new StringBuilder()
				.append("\n###### params=").append(params)
				.append("\n###### Entrando a confirmarPolizaIndividual ######")
				.append("\n################################################")
				.toString()
				);		
		try{
			Utils.validate(params,"No se recibieron parametros");
			Utils.validate(params.get("cdramo")  ,"No se recibio el ramo",
						   params.get("cdtipsit"),"No se recibio el tipo de ramo");
			String cdramo = params.get("cdramo");
			String cdtipsit = params.get("cdtipsit");
			params.put("items", renovacionManager.obtenerItemsTatripol(cdramo, cdtipsit));
			exito = true;
		}
		catch(Exception ex){
			respuesta = Utils.manejaExcepcion(ex);
		}
		return SUCCESS;
	}
	
	public String renovarPolizasMasivasIndividuales(){
		logger.info(
				new StringBuilder()
				.append("\n###### slist1=").append(slist1)
				.append("\n###### Entrando a renovarPolizasMasivasIndividuales ######")
				.append("\n##########################################################")
				.toString()
				);
		session=ActionContext.getContext().getSession();
		try{
			Utils.validate(slist1, "No se recibieron polizas a renovar");
			renovacionManager.renovarPolizasMasivasIndividuales(slist1);
			exito = true;
		}
		catch(Exception ex){
			respuesta = Utils.manejaExcepcion(ex);
		}
		logger.info(
				new StringBuilder()
				.append("\n###### Saliendo a renovarPolizasMasivasIndividuales ######")
				.append("\n##########################################################")
				.toString()
				);		
		return SUCCESS;
	}
	
	public String obtenerCondicionesRenovacionIndividual(){
		logger.info(
				new StringBuilder()
				.append("\n###### smap1=").append(smap1)
				.append("\n###### Entrando a obtenerCondicionesRenovacionIndividual ######")
				.append("\n##########################################################")
				.toString()
				);
		List<Map<String, String>> slist = new ArrayList<Map<String,String>>();
		try{
			Utils.validate(smap1, "No se recibieron parametros de entrada");
			Utils.validate(smap1.get("anio"),"No se recibio el a\u00F1o");
			String nmperiod = smap1.get("nmperiod");
			String cdunieco = smap1.get("cdunieco");
			String cdramo   = smap1.get("cdramo");
			String anio     = smap1.get("anio");
			String mes      = smap1.get("mes");
			slist = renovacionManager.obtenerCondicionesRenovacionIndividual(nmperiod, cdunieco, cdramo, anio, mes);
			setSlist1(slist);
			exito = true;
		}
		catch(Exception ex){
			respuesta = Utils.manejaExcepcion(ex);
		}
		return SUCCESS;
	}
	
	public String movimientoCondicionesRenovacionIndividual(){
		logger.info(
				new StringBuilder()
				.append("\n###### params=").append(params)
				.append("\n###### Entrando a movimientoCondicionesRenovacionIndividual ######")
				.append("\n##########################################################")
				.toString()
				);
		exito = false;
		try{
			Utils.validate(params, "No se recibieron parametros de entrada");
			if(params.get("operacion").equals("I")){
			    Utils.validate(params.get("anio"),      "No se recibio el a\u00F1o",
                               params.get("mes") ,      "No se recibio el mes",
                               params.get("operacion") ,"No se recibio la operacion",
                               params.get("valor"),     "No se recibio valor");
			    if(params.get("criterio").equals("between")){
			        Utils.validate(params.get("valor2"), "No se recibio segundo valor");
			    }
			}
			else{
    			Utils.validate(params.get("anio"),"No se recibio el a\u00F1o",
    						   params.get("mes") ,"No se recibio el mes",
    						   params.get("operacion") ,"No se recibio la operacion");
			}
			String nmperiod  = params.get("nmperiod");
			String cdunieco  = params.get("cdunieco");
			String cdramo    = params.get("cdramo");
			String anio      = params.get("anio");
			String mes       = params.get("mes");
			String criterio  = params.get("criterio");
			String campo     = params.get("campo");
			String valor     = params.get("valor");
			String valor2    = params.get("valor2");
			String operacion = params.get("operacion");
			renovacionManager.movimientoCondicionesRenovacionIndividual(nmperiod, cdunieco, cdramo, anio, mes, criterio, campo, valor, valor2, operacion);
			exito = true;
		}
		catch(Exception ex){
			respuesta = Utils.manejaExcepcion(ex);
		}
		return SUCCESS;
	}
	
	public String obtenerCalendarizacionRenovacionIndividual(){
		logger.info(
				new StringBuilder()
				.append("\n###### smap1=").append(smap1)
				.append("\n###### Entrando a obtenerCalendarizacionRenovacionIndividual ######")
				.append("\n##########################################################")
				.toString()
				);
		List<Map<String, String>> slist = new ArrayList<Map<String,String>>();
		try{
			Utils.validate(smap1, "No se recibieron parametros de entrada");
			Utils.validate(smap1.get("anio"),"No se recibio el a\u00F1o"/*,
						   smap1.get("mes"),"No se recibio el mes"*/);
			String anio = smap1.get("anio");
			String mes  = smap1.get("mes");
			slist = renovacionManager.obtenerCalendarizacionRenovacionIndividual(anio, mes);
			setSlist1(slist);
			exito = true;
		}
		catch(Exception ex){
			respuesta = Utils.manejaExcepcion(ex);
		}
		return SUCCESS;
	}
	
	public String movimientoCalendarizacionRenovacionIndividual(){
		logger.info(
				new StringBuilder()
				.append("\n###### params=").append(params)
				.append("\n###### Entrando a movimientoCalendarizacionRenovacionIndividual ######")
				.append("\n##########################################################")
				.toString()
				);
		exito = false;
		try{
			Utils.validate(params, "No se recibieron parametros de entrada");
			if(params.get("operacion").toUpperCase().equals("B") || params.get("operacion").toUpperCase().equals("D")){
				Utils.validate(params.get("nmperiod"),"No se recibio el identificador");
			}
			else{
				Utils.validate(params.get("feinicio"),"No se recibio la fecha de inicio",
							   params.get("fefinal") ,"No se recibio la fecha fin",
							   params.get("feaplica") ,"No se recibio la fecha de ejecucion");
			}
			String nmperiod  = params.get("nmperiod");
			String anio      = params.get("anio");
			String mes       = params.get("mes");
			String cdunieco  = params.get("cdunieco");
			String cdramo    = params.get("cdramo");
			String feinicio  = params.get("feinicio");
			String fefinal   = params.get("fefinal");
			String feaplica  = params.get("feaplica");
			String operacion = params.get("operacion");
			renovacionManager.movimientoCalendarizacionRenovacionIndividual(nmperiod, anio, mes, cdunieco, cdramo, feinicio, fefinal, feaplica, operacion);
			exito = true;
		}
		catch(Exception ex){
			respuesta = Utils.manejaExcepcion(ex);
		}
		return SUCCESS;
	}
	
	public String validaAgenteSIGS(){
	    logger.info(
                new StringBuilder()
                .append("\n###### params=").append(params)
                .append("\n###### Entrando a validaAgenteSIGS ######")
                .append("\n##########################################################")
                .toString()
                );
	    try{
	        Utils.validate(params, "No se recibieron parametros");
	        String cdunieco = params.get("cdunieco");
	        String cdramo   = params.get("cdramo");
	        String estado   = params.get("estado");
	        String nmpoliza = params.get("nmpoliza");
	        String nmsuplem = params.get("nmsuplem");
	        Utils.validate(cdunieco, "No se recibio la oficina",
	                       cdramo,   "No se recibio el producto",
	                       estado,   "No se recibio el estado",
	                       nmpoliza, "No se recibio la poliza",
	                       nmsuplem, "No se recibio el suplemento");
	        respuesta = cotizacionManager.validaAgenteActivo(cdunieco, cdramo, estado, nmpoliza, nmsuplem);	        
	    }
	    catch(Exception ex){
	        respuesta = Utils.manejaExcepcion(ex);
	    }
	    return SUCCESS;
	}
	
	public String validateValueExclusion(){
	    logger.info(
                new StringBuilder()
                .append("\n###### params=").append(params)
                .append("\n###### Entrando a validateValueExclusion ######")
                .append("\n##########################################################")
                .toString()
                );
        try{
            Utils.validate(params, "No se recibieron parametros");
            String criterio = params.get("criterio");
            String valor    = params.get("valor");
            Utils.validate(criterio, "No se recibio el criterio",
                           valor,    "No se recibio el valor");
            respuesta = renovacionManager.validaValorExclusion(criterio, valor);
            exito = true;
        }
        catch(Exception ex){
            exito     = false;
            respuesta = Utils.manejaExcepcion(ex);
        }
        logger.info(
                new StringBuilder()
                .append("\n###### exito=").append(exito)
                .append("\n###### respuesta=").append(respuesta)
                .append("\n###### Saliendo a validateValueExclusion ######")
                .append("\n##########################################################")
                .toString()
                );
        return SUCCESS;
	}
	
	@Action(value   = "renovarColectivo",
            results = { @Result(name="success", type="json") }
            )
	public String renovacionColectivo(){
		logger.debug(Utils.log(
				"\n####################################",
				"\n###### renovacionColectivo ######",
				"\n###### params=",params
				));
        try{
            Utils.validate(params, "No se recibieron parametros");
            logger.debug("entro");
            String cdusuari = "AUTOMATICO"
				,cdunieco   = params.get("cdunieco")
				,cdramo     = params.get("cdramo")
				,nmpoliza   = params.get("nmpoliza")
				,fecdesde   = params.get("fecdesde")
				,fechasta   = params.get("fechasta")
				,procedimiento = params.get("procedimiento");
	            
            respuesta = renovacionManager.renovacionColectivo(cdusuari,cdunieco,cdramo,nmpoliza,fecdesde,fechasta,procedimiento);
            exito = true;
        }
        catch(Exception ex){
            exito     = false;
            respuesta = Utils.manejaExcepcion(ex);
        }
        logger.debug(Utils.log(
				"\n###### renovacionColectivo ######",
				"\n####################################"
				));
        return SUCCESS;
	}
	//Getters y setters
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isExito() {
		return exito;
	}

	public void setExito(boolean exito) {
		this.exito = exito;
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

	public void setRenovacionManager(RenovacionManager renovacionManager) {
		this.renovacionManager = renovacionManager;
	}

	public Map<String, Item> getImap() {
		return imap;
	}

	public void setImap(Map<String, Item> imap) {
		this.imap = imap;
	}

	public List<Map<String, String>> getSlist1() {
		return slist1;
	}

	public void setSlist1(List<Map<String, String>> slist1) {
		this.slist1 = slist1;
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

	public String getRutaDocumentosPoliza() {
		return rutaDocumentosPoliza;
	}
}