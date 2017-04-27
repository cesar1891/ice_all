package mx.com.gseguros.portal.endosos.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.mesacontrol.model.FlujoVO;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.endosos.service.EndososAutoManager;
import mx.com.gseguros.portal.endosos.service.EndososManager;
import mx.com.gseguros.utils.Utils;

@Controller
@Scope("prototype")
@ParentPackage(value="default")
@Namespace("/endosos")
public class NuevoEndososAction extends PrincipalCoreAction
{
	private static final Logger logger = LoggerFactory.getLogger(NuevoEndososAction.class);
	
	private boolean success;
	
	private String message;
	
	private Map<String, String> params;
	
	private List<Map<String, String>> list;
	
	private FlujoVO flujo;
	
	private Map<String, Item> items;
	
	@Autowired
	private EndososAutoManager endososAutoManager;
	
	@Autowired
	private EndososManager endososManager;
	
	public NuevoEndososAction () {
		this.session = ActionContext.getContext().getSession();
	}
	
	@Action(value   = "sacaEndosoFlujo",
			results = { @Result(name="success", type="json") }
			)
	public String sacaEndosoFlujo()
	{
		logger.debug(Utils.log(
				 "\n#############################"
				,"\n###### sacaEndosoFlujo ######"
				,"\n###### flujo = " , flujo
				));
		
		try
		{
			Utils.validate(flujo , "No se recibieron datos del flujo");
			
			endososAutoManager.sacaEndosoFlujo(flujo);
			
			message = "Se ha revertido el endoso";
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### sacaEndosoFlujo ######"
				,"\n#############################"
				));
		return SUCCESS;
	}
	
	@Actions({
		@Action(value   = "endosoAltaAsegurados",
	        results = {
	            @Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
				@Result(name="success" , location="/jsp-script/proceso/endosos/endosoAltaAsegurados.jsp")
	        },
	        interceptorRefs = {
	        	@InterceptorRef(value = "json", params = {"enableSMD", "true", "ignoreSMDMethodInterfaces", "false" })
			}),
	    @Action(value   = "includes/endosoAltaAsegurados",
			results = {
				@Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
				@Result(name="success" , location="/jsp-script/proceso/endosos/endosoAltaAsegurados.jsp")
		    },
			interceptorRefs = {
				@InterceptorRef(value = "json", params = {"enableSMD", "true", "ignoreSMDMethodInterfaces", "false" })
			})
	})
	public String endosoAltaAsegurados () {
		logger.debug(Utils.log(
			"\n##################################",
			"\n###### endosoAltaAsegurados ######",
			"\n###### params = " , params,
			"\n###### flujo  = " , flujo));
		String result = ERROR;
		try {
			if (flujo == null) {
				throw new ApplicationException("No hay tr\u00e1mite");
			}
			UserVO usuario = Utils.validateSession(session);
			Map<String, Object> datosManager = endososAutoManager.endosoAltaAsegurados(flujo.getCdunieco(), flujo.getCdramo(),
					flujo.getEstado(), flujo.getNmpoliza(), usuario.getUser(), usuario.getRolActivo().getClave(), flujo.getStatus());
			params = (Map<String, String>) datosManager.get("vigencias");
			params.put("permisoEmitir", (String) datosManager.get("permisoEmitir"));
			params.put("permisoAutorizar", (String) datosManager.get("permisoAutorizar"));
			items = (Map<String, Item>) datosManager.get("items");
			result = SUCCESS;
		} catch (Exception ex) {
			message = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
			"\n###### result  = " , result,
			"\n###### message = " , message,
			"\n###### endosoAltaAsegurados ######",
			"\n##################################"));
		return result;
	}
	
	@Action(value   = "validacionSigsAgente",
			results = { @Result(name="success", type="json") }
			)
	public String validacionSigsAgente()
	{
		logger.debug(Utils.log(
				 "\n###################################"
				,"\n###### validacionSigsAgente ######"
				,"\n###### params = ", params
				));
		
		try
		{
			Utils.validate(params, "No se recibieron datos");
			
			String cdagente  = params.get("cdagente"),
					cdramo   = params.get("cdramo"),
					cdtipsit = params.get("cdtipsit"),
					cdtipend = params.get("cdtipend");
			
			Utils.validate(
					cdagente , "Falta cdagente",
					cdramo   , "Falta cdramo",
					cdtipsit , "Falta cdtipsit",
					cdtipend , "Falta cdtipend"
					);
			
			if (!"B".equals(cdtipend)) {
				endososAutoManager.validacionSigsAgente(cdagente, cdramo, cdtipsit, cdtipend);
			} else {
				logger.debug("No aplica validacion de agente para tipo B");
			}
			
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
				"\n###### success = ", success,
				"\n###### message = ", message,
				"\n###### validacionSigsAgente ######",
				"\n##################################"
				));
		return SUCCESS;
	}
	
	@Action(value   = "guardarFechaEfectoEndosoPendiente",
			results = { @Result(name="success", type="json") }
			)
	public String guardarFechaEfectoEndosoPendiente () {
		logger.debug(Utils.log(
				"\n###############################################",
				"\n###### guardarFechaEfectoEndosoPendiente ######",
				"\n###### params = " , params,
				"\n###### flujo  = " , flujo));
		try {
			UserVO usuario = Utils.validateSession(session);
			Utils.validate(params, "No hay params");
			Utils.validate(flujo, "No hay flujo");
			String fecha    = params.get("fecha"),
			       cdtipsup = params.get("cdtipsup");
			Utils.validate(fecha    , "Falta fecha",
			               cdtipsup , "Falta cdtipsup");
			params.putAll(endososAutoManager.guardarFechaEfectoEndosoPendiente(flujo.getCdunieco(), flujo.getCdramo(),
					flujo.getEstado(), flujo.getNmpoliza(), fecha, usuario.getEmpresa().getElementoId(), usuario.getUser(),
					"END", cdtipsup));
			success = true;
		} catch (Exception ex) {
			message = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
				"\n###### success = " , success,
				"\n###### message = " , message,
				"\n###### params  = " , params,
				"\n###### guardarFechaEfectoEndosoPendiente ######",
				"\n###############################################"));
		return SUCCESS;
	}
	
	@Action(value   = "recuperarDatosEndosoPendiente",
			results = { @Result(name="success", type="json") }
			)
	public String recuperarDatosEndosoPendiente () {
		logger.debug(Utils.log(
				"\n###########################################",
				"\n###### recuperarDatosEndosoPendiente ######",
				"\n###### flujo = " , flujo));
		try {
			Utils.validateSession(session);
			Utils.validate(flujo, "No hay tr\u00e1mite");
			String cdunieco = flujo.getCdunieco(),
			       cdramo   = flujo.getCdramo(),
			       estado   = flujo.getEstado(),
			       nmpoliza = flujo.getNmpoliza();
			Utils.validate(cdunieco , "Falta cdunieco",
					cdramo   , "Falta cdramo",
					estado   , "Falta estado",
					nmpoliza , "Falta nmpoliza");
			Utils.validate(params, "Faltan params");
			String cdtipsup = params.get("cdtipsup");
			Utils.validate(cdtipsup, "Falta cdtipsup");
			params = endososAutoManager.recuperarDatosEndosoPendiente(cdunieco, cdramo, estado, nmpoliza, cdtipsup);
			success = true;
		} catch (Exception ex) {
			message = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
				"\n###### success = " , success,
				"\n###### message = " , message,
				"\n###### params  = " , params,
				"\n###### recuperarDatosEndosoPendiente ######",
				"\n###########################################"));
		return SUCCESS;
	}
	
	@Action(value   = "guardarAseguradoParaEndosoAlta",
			results = { @Result(name="success", type="json") }
			)
	public String guardarAseguradoParaEndosoAlta () {
		logger.debug(Utils.log(
				"\n############################################",
				"\n###### guardarAseguradoParaEndosoAlta ######",
				"\n###### params = ", params));
		try {
			UserVO usuario = Utils.validateSession(session);
			String cdusuari = usuario.getUser(),
			       cdsisrol = usuario.getRolActivo().getClave();
			
			String usuarioCaptura =  null;
			
			if(usuario!=null){
				if(StringUtils.isNotBlank(usuario.getClaveUsuarioCaptura())){
					usuarioCaptura = usuario.getClaveUsuarioCaptura();
				}else{
					usuarioCaptura = usuario.getCodigoPersona();
				}
				
			}
			
			Utils.validate(params, "No hay params");
			String cdunieco   = params.get("cdunieco"),
			       cdramo     = params.get("cdramo"),
			       estado     = params.get("estado"),
			       nmpoliza   = params.get("nmpoliza"),
			       accion     = params.get("ACCION"),
			       nombre     = params.get("DSNOMBRE"),
			       nombre2    = params.get("DSNOMBRE1"),
			       apat       = params.get("DSAPELLIDO"),
			       amat       = params.get("DSAPELLIDO1"),
			       sexo       = params.get("OTSEXO"),
			       fenacimi   = params.get("FENACIMI"),
			       rfc        = params.get("CDRFC"),
			       nacional   = params.get("CDNACION"),
			       edocivil   = params.get("CDESTCIVIL"),
			       feingreso  = params.get("FEINGRESO"),
			       cdperson   = params.get("CDPERSON"),
			       cdtipide   = params.get("CDTIPIDE"),
			       cdideper   = params.get("CDIDEPER"),
			       cdtipper   = params.get("CDTIPPER"),
			       dsemail    = params.get("DSEMAIL"),
			       canaling   = params.get("CANALING"),
			       conducto   = params.get("CONDUCTO"),
			       ptcumupr   = params.get("PTCUMUPR"),
			       residencia = params.get("RESIDENCIA"),
			       nongrata   = params.get("NONGRATA"),
			       cdideext   = params.get("CDIDEEXT"),
			       cdsucemi   = params.get("CDSUCEMI"),
			       otfisjur   = params.get("OTFISJUR"),
			       nmsituac   = params.get("NMSITUAC"),
			       cdrol      = params.get("CDROL"),
			       nmorddom   = params.get("NMORDDOM"),
			       swreclam   = params.get("SWRECLAM"),
			       swexiper   = params.get("SWEXIPER"),
			       nmsuplem   = params.get("nmsuplem"),
			       nsuplogi   = params.get("nsuplogi"),
			       fesolici   = params.get("fesolici"),
			       feendoso   = params.get("feendoso");
			Utils.validate(cdunieco , "Falta cdunieco",
					       cdramo   , "Falta cdramo",
					       estado   , "Falta estado",
					       nmpoliza , "Falta nmpoliza",
					       accion   , "Falta accion",
					       nmsuplem , "Falta nmsuplem",
					       nsuplogi , "Falta nsuplogi",
					       feendoso , "Falta feendoso");
			Map<String, String> valosit = new HashMap<String, String>();
			for (Entry<String, String> en : params.entrySet()) {
				if (en.getKey().indexOf("otvalor") != -1) {
					valosit.put(en.getKey(), en.getValue());
				}
			}
			endososAutoManager.guardarAseguradoParaEndosoAlta(
					cdunieco,
					cdramo,
					estado,
					nmpoliza,
					cdusuari,
					cdsisrol,
					accion,
					nombre,
					nombre2,
					apat,
					amat,
					sexo,
					fenacimi,
					rfc,
					nacional,
					edocivil,
					feingreso,
					cdperson,
					cdtipide,
					cdideper,
					cdtipper,
					dsemail,
					canaling,
					conducto,
					ptcumupr,
					residencia,
					nongrata,
					cdideext,
					cdsucemi,
					otfisjur,
					nmsituac,
					cdrol,
					nmorddom,
					swreclam,
					swexiper,
					nmsuplem,
					nsuplogi,
					fesolici,
					feendoso,
					valosit,
					usuarioCaptura
					);
			success = true;
		} catch (Exception ex) {
			message = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
				"\n###### success = " , success,
				"\n###### message = " , message,
				"\n###### guardarAseguradoParaEndosoAlta ######",
				"\n############################################"));
		return SUCCESS;
	}
	
	@Action(value   = "sacaendoso",
			results = { @Result(name="success", type="json") }
			)
	public String sacaendoso () {
		logger.debug(Utils.log(
				"\n########################",
				"\n###### sacaendoso ######",
				"\n###### params = ", params));
		try {
			Utils.validateSession(session);
			Utils.validate(params, "No hay datos");
			String cdunieco = params.get("cdunieco"),
			       cdramo   = params.get("cdramo"),
			       estado   = params.get("estado"),
			       nmpoliza = params.get("nmpoliza"),
			       nmsuplem = params.get("nmsuplem"),
			       nsuplogi = params.get("nsuplogi");
			Utils.validate(cdunieco , "Falta cdunieco",
			               cdramo   , "Falta cdramo",
			               estado   , "Falta estado",
			               nmpoliza , "Falta nmpoliza",
			               nmsuplem , "Falta nmsuplem",
			               nsuplogi , "Falta nsuplogi");
			endososManager.sacaEndoso(cdunieco, cdramo, estado, nmpoliza, nsuplogi, nmsuplem);
			success = true;
		} catch (Exception ex) {
			message = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
				"\n###### success = " , success,
				"\n###### message = " , message,
				"\n###### sacaendoso ######",
				"\n########################"));
		return SUCCESS;
	}
	
	@Action(value   = "tarificarEndosoAltaAsegurados",
			results = { @Result(name="success", type="json") }
			)
	public String tarificarEndosoAltaAsegurados () {
		logger.debug(Utils.log(
				"\n###########################################",
				"\n###### tarificarEndosoAltaAsegurados ######",
				"\n###### params = ", params));
		try {
			UserVO usuario = Utils.validateSession(session);
			Utils.validate(params, "No se recibieron datos");
			String cdunieco = params.get("cdunieco"),
			       cdramo   = params.get("cdramo"),
			       estado   = params.get("estado"),
			       nmpoliza = params.get("nmpoliza"),
			       nmsuplem = params.get("nmsuplem"),
			       feinival = params.get("feinival");
			Utils.validate(cdunieco , "Falta cdunieco",
			               cdramo   , "Falta cdramo",
			               estado   , "Falta estado",
			               nmpoliza , "Falta nmpoliza",
			               nmsuplem , "Falta nmsuplem",
			               feinival , "Falta feinival");
			list = endososAutoManager.tarificarEndosoAltaAsegurados(usuario.getUser(),
					usuario.getEmpresa().getElementoId(), cdunieco, cdramo, estado, nmpoliza, nmsuplem, feinival);
			success = true;
		} catch (Exception ex) {
			message = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
				"\n###### success = " , success,
				"\n###### message = " , message,
				"\n###### tarificarEndosoAltaAsegurados ######",
				"\n###########################################"));
		return SUCCESS;
	}
	
	@Action(value   = "confirmarEndosoSaludFlujo",
			results = { @Result(name="success", type="json") }
			)
	public String confirmarEndosoSaludFlujo () {
		logger.debug(Utils.log(
				"\n#######################################",
				"\n###### confirmarEndosoSaludFlujo ######",
				"\n###### params = " , params,
				"\n###### flujo  = " , flujo));
		try {
			UserVO usuario = Utils.validateSession(session);
			String cdusuari = usuario.getUser(),
			       cdsisrol = usuario.getRolActivo().getClave(),
			       cdelemen = usuario.getEmpresa().getElementoId();
			Utils.validate(flujo, "Falta flujo");
			String ntramite = flujo.getNtramite(),
			       cdunieco = flujo.getCdunieco(),
			       cdramo   = flujo.getCdramo(),
			       estado   = flujo.getEstado(),
			       nmpoliza = flujo.getNmpoliza(),
			       status   = flujo.getStatus();
			Utils.validate(ntramite , "Falta ntramite",
			               cdunieco , "Falta cdunieco",
			               cdramo   , "Falta cdramo",
			               estado   , "Falta estado",
			               nmpoliza , "Falta nmpoliza",
			               status   , "Falta status");
			Utils.validate(params, "Faltan params");
			String nmsuplem = params.get("nmsuplem"),
			       nsuplogi = params.get("nsuplogi"),
			       fesolici = params.get("fesolici"),
			       feinival = params.get("feinival"),
			       autoriza = params.get("autoriza"),
			       cdtipsup = params.get("cdtipsup");
			Utils.validate(nmsuplem , "Falta nmsuplem",
			               nsuplogi , "Falta nsuplogi",
			               fesolici , "Falta fesolici",
			               feinival , "Falta feinival",
			               autoriza , "Falta autoriza",
			               cdtipsup , "Falta cdtipsup");
			params.putAll(endososAutoManager.confirmarEndosoSaludFlujo(cdusuari, cdsisrol, cdelemen, ntramite, cdunieco, cdramo,
					estado, nmpoliza, status, nmsuplem, nsuplogi, Utils.parse(fesolici), Utils.parse(feinival), "S".equals(autoriza),
					cdtipsup, usuario));
			success = true;
		} catch (Exception ex) {
			message = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
				"\n###### success = " , success,
				"\n###### message = " , message,
				"\n###### params  = " , params,
				"\n###### confirmarEndosoSaludFlujo ######",
				"\n#######################################"));
		return SUCCESS;
	}
	
	@Actions({
		@Action(value   = "endosoCoberturasFlujo",
	        results = {
	            @Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
				@Result(name="success" , location="/jsp-script/proceso/endosos/endosoCoberturasFlujo.jsp")
	        }/*,
	        interceptorRefs = {
	        	@InterceptorRef(value = "json", params = {"enableSMD", "true", "ignoreSMDMethodInterfaces", "false" })
			}*/),
	    @Action(value   = "includes/endosoCoberturasFlujo",
			results = {
				@Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
				@Result(name="success" , location="/jsp-script/proceso/endosos/endosoCoberturasFlujo.jsp")
		    },
			interceptorRefs = {
				@InterceptorRef(value = "json", params = {"enableSMD", "true", "ignoreSMDMethodInterfaces", "false" })
			})
	})
	public String endosoCoberturas () {
		logger.debug(Utils.log(
			"\n###################################",
			"\n###### endosoCoberturasFlujo ######",
			"\n###### params = " , params,
			"\n###### flujo  = " , flujo));
		String result = ERROR;
		try {
			if (flujo == null) {
				throw new ApplicationException("No hay tr\u00e1mite");
			}
			UserVO usuario = Utils.validateSession(session);
			Utils.validate(params, "Faltan params");
			String cdtipsup = params.get("cdtipsup");
			Utils.validate(cdtipsup, "Falta cdtipsup");
			Map<String, Object> datosManager = endososAutoManager.endosoCoberturasFlujo(flujo.getCdunieco(), flujo.getCdramo(),
					flujo.getEstado(), flujo.getNmpoliza(), usuario.getUser(), usuario.getRolActivo().getClave(), flujo.getStatus());
			params = (Map<String, String>) datosManager.get("vigencias");
			params.put("cdtipsup", cdtipsup);
			params.put("permisoEmitir", (String) datosManager.get("permisoEmitir"));
			params.put("permisoAutorizar", (String) datosManager.get("permisoAutorizar"));
			items = (Map<String, Item>) datosManager.get("items");
			result = SUCCESS;
		} catch (Exception ex) {
			message = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
			"\n###### result  = " , result,
			"\n###### message = " , message,
			"\n###### endosoCoberturasFlujo ######",
			"\n###################################"));
		return result;
	}
	
	@Action(value   = "agregarCoberturaEndosoCoberturas",
			results = { @Result(name="success", type="json") }
			)
	public String agregarCoberturaEndosoCoberturas () {
		logger.debug(Utils.log(
				"\n##############################################",
				"\n###### agregarCoberturaEndosoCoberturas ######",
				"\n###### params = ", params));
		try {
			Utils.validateSession(session);
			Utils.validate(params, "Faltan params");
			String cdunieco  = params.get("cdunieco"),
			       cdramo    = params.get("cdramo"),
			       estado    = params.get("estado"),
			       nmpoliza  = params.get("nmpoliza"),
			       nmsituac  = params.get("nmsituac"),
			       cdgarant  = params.get("cdgarant"),
			       nmsuplem  = params.get("nmsuplem"),
			       cdatribu1 = params.get("cdatribu1"),
			       otvalor1  = params.get("otvalor1"),
	    		   cdatribu2 = params.get("cdatribu2"),
			       otvalor2  = params.get("otvalor2"),
			       cdatribu3 = params.get("cdatribu3"),
			       otvalor3  = params.get("otvalor3"),
			       cdtipsit  = params.get("cdtipsit");
			Utils.validate(cdunieco , "Falta cdunieco",
			               cdramo   , "Falta cdramo",
			               estado   , "Falta estado",
			               nmpoliza , "Falta nmpoliza",
			               nmsituac , "Falta nmsituac",
			               cdgarant , "Falta cdgarant",
			               nmsuplem , "Falta nmsuplem",
			               cdtipsit , "Falta cdtipsit");
			endososAutoManager.agregarCoberturaEndosoCoberturas(cdunieco, cdramo, estado, nmpoliza,
					nmsituac, cdgarant, nmsuplem, cdatribu1, otvalor1, cdatribu2, otvalor2, cdatribu3, otvalor3,
					cdtipsit);
			success = true;
		} catch (Exception ex) {
			message = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
				"\n###### success = " , success,
				"\n###### message = " , message,
				"\n###### agregarCoberturaEndosoCoberturas ######",
				"\n##############################################"));
		return SUCCESS;
	}
	
	@Action(value   = "quitarCoberturaAgregadaEndCob",
			results = { @Result(name="success", type="json") }
			)
	public String quitarCoberturaAgregadaEndCob () {
		logger.debug(Utils.log(
				"\n###########################################",
				"\n###### quitarCoberturaAgregadaEndCob ######",
				"\n###### params = ", params));
		try {
			Utils.validateSession(session);
			Utils.validate(params, "Faltan params");
			String cdunieco = params.get("cdunieco"),
			       cdramo   = params.get("cdramo"),
			       estado   = params.get("estado"),
			       nmpoliza = params.get("nmpoliza"),
			       nmsituac = params.get("nmsituac"),
			       cdgarant = params.get("cdgarant"),
			       nmsuplem = params.get("nmsuplem"),
			       cdtipsit = params.get("cdtipsit"),
			       cdatribu1 = params.get("cdatribu1"),
	    		   cdatribu2 = params.get("cdatribu2"),
			       cdatribu3 = params.get("cdatribu3");
			Utils.validate(cdunieco , "Falta cdunieco",
			               cdramo   , "Falta cdramo",
			               estado   , "Falta estado",
			               nmpoliza , "Falta nmpoliza",
			               nmsituac , "Falta nmsituac",
			               cdgarant , "Falta cdgarant",
			               nmsuplem , "Falta nmsuplem",
			               cdtipsit , "Falta cdtipsit");
			endososAutoManager.quitarCoberturaAgregadaEndCob(cdunieco, cdramo, estado, nmpoliza,
					nmsituac, cdgarant, nmsuplem, cdatribu1, cdatribu2, cdatribu3, cdtipsit);
			success = true;
		} catch (Exception ex) {
			message = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
				"\n###### success = " , success,
				"\n###### message = " , message,
				"\n###### quitarCoberturaAgregadaEndCob ######",
				"\n###########################################"));
		return SUCCESS;
	}
	
	@Action(value   = "eliminarCoberturaEndosoCoberturas",
			results = { @Result(name="success", type="json") }
			)
	public String eliminarCoberturaEndosoCoberturas () {
		logger.debug(Utils.log(
				"\n###############################################",
				"\n###### eliminarCoberturaEndosoCoberturas ######",
				"\n###### params = ", params));
		try {
			Utils.validateSession(session);
			Utils.validate(params, "Faltan params");
			String cdunieco  = params.get("cdunieco"),
			       cdramo    = params.get("cdramo"),
			       estado    = params.get("estado"),
			       nmpoliza  = params.get("nmpoliza"),
			       nmsituac  = params.get("nmsituac"),
			       cdgarant  = params.get("cdgarant"),
			       nmsuplem  = params.get("nmsuplem"),
			       cdtipsit  = params.get("cdtipsit");
			Utils.validate(cdunieco , "Falta cdunieco",
			               cdramo   , "Falta cdramo",
			               estado   , "Falta estado",
			               nmpoliza , "Falta nmpoliza",
			               nmsituac , "Falta nmsituac",
			               cdgarant , "Falta cdgarant",
			               nmsuplem , "Falta nmsuplem",
			               cdtipsit , "Falta cdtipsit");
			endososAutoManager.eliminarCoberturaEndosoCoberturas(cdunieco, cdramo, estado, nmpoliza,
					nmsituac, cdgarant, nmsuplem, cdtipsit);
			success = true;
		} catch (Exception ex) {
			message = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
				"\n###### success = " , success,
				"\n###### message = " , message,
				"\n###### eliminarCoberturaEndosoCoberturas ######",
				"\n###############################################"));
		return SUCCESS;
	}
	
	@Action(value   = "restaurarCoberturaEliminadaEndCob",
			results = { @Result(name="success", type="json") }
			)
	public String restaurarCoberturaEliminadaEndCob () {
		logger.debug(Utils.log(
				"\n###############################################",
				"\n###### restaurarCoberturaEliminadaEndCob ######",
				"\n###### params = ", params));
		try {
			Utils.validateSession(session);
			Utils.validate(params, "Faltan params");
			String cdunieco  = params.get("cdunieco"),
			       cdramo    = params.get("cdramo"),
			       estado    = params.get("estado"),
			       nmpoliza  = params.get("nmpoliza"),
			       nmsituac  = params.get("nmsituac"),
			       cdgarant  = params.get("cdgarant"),
			       nmsuplem  = params.get("nmsuplem"),
			       cdtipsit  = params.get("cdtipsit"),
			       cdatribu1 = params.get("cdatribu1"),
	    		   cdatribu2 = params.get("cdatribu2"),
			       cdatribu3 = params.get("cdatribu3");
			Utils.validate(cdunieco , "Falta cdunieco",
			               cdramo   , "Falta cdramo",
			               estado   , "Falta estado",
			               nmpoliza , "Falta nmpoliza",
			               nmsituac , "Falta nmsituac",
			               cdgarant , "Falta cdgarant",
			               nmsuplem , "Falta nmsuplem",
			               cdtipsit , "Falta cdtipsit");
			endososAutoManager.restaurarCoberturaEliminadaEndCob(cdunieco, cdramo, estado, nmpoliza,
					nmsituac, cdgarant, nmsuplem, cdatribu1, cdatribu2, cdatribu3, cdtipsit);
			success = true;
		} catch (Exception ex) {
			message = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
				"\n###### success = " , success,
				"\n###### message = " , message,
				"\n###### restaurarCoberturaEliminadaEndCob ######",
				"\n###############################################"));
		return SUCCESS;
	}
	
	@Action(value   = "tarificarEndosoCoberturasFlujo",
			results = { @Result(name="success", type="json") }
			)
	public String tarificarEndosoCoberturasFlujo () {
		logger.debug(Utils.log(
				"\n############################################",
				"\n###### tarificarEndosoCoberturasFlujo ######",
				"\n###### params = ", params));
		try {
			UserVO usuario = Utils.validateSession(session);
			Utils.validate(params, "No se recibieron datos");
			String cdunieco = params.get("cdunieco"),
			       cdramo   = params.get("cdramo"),
			       estado   = params.get("estado"),
			       nmpoliza = params.get("nmpoliza"),
			       nmsuplem = params.get("nmsuplem"),
			       feinival = params.get("feinival"),
			       cdtipsup = params.get("cdtipsup");
			Utils.validate(cdunieco , "Falta cdunieco",
			               cdramo   , "Falta cdramo",
			               estado   , "Falta estado",
			               nmpoliza , "Falta nmpoliza",
			               nmsuplem , "Falta nmsuplem",
			               feinival , "Falta feinival",
			               cdtipsup , "Falta cdtipsup");
			list = endososAutoManager.tarificarEndosoCoberturasFlujo(usuario.getUser(),
					usuario.getEmpresa().getElementoId(), cdunieco, cdramo, estado, nmpoliza, nmsuplem, feinival, cdtipsup);
			success = true;
		} catch (Exception ex) {
			message = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
				"\n###### success = " , success,
				"\n###### message = " , message,
				"\n###### tarificarEndosoCoberturasFlujo ######",
				"\n############################################"));
		return SUCCESS;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////// GETTERS Y SETTERS //////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	public boolean isSuccess() {
		return success;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	public void setSuccess(boolean success) {
		this.success = success;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	public String getMessage() {
		return message;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	public void setMessage(String message) {
		this.message = message;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	public Map<String, String> getParams() {
		return params;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	public FlujoVO getFlujo() {
		return flujo;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	public void setFlujo(FlujoVO flujo) {
		this.flujo = flujo;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	public Map<String, Item> getItems() {
		return items;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	public void setItems(Map<String, Item> items) {
		this.items = items;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	public List<Map<String, String>> getList() {
		return list;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	public void setList(List<Map<String, String>> list) {
		this.list = list;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////// GETTERS Y SETTERS //////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////
}