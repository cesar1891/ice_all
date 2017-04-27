package mx.com.gseguros.mesacontrol.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
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
import mx.com.gseguros.mesacontrol.model.AgrupadorMC;
import mx.com.gseguros.mesacontrol.model.FlujoVO;
import mx.com.gseguros.mesacontrol.service.FlujoMesaControlManager;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.cotizacion.service.CotizacionManager;
import mx.com.gseguros.portal.despachador.model.RespuestaTurnadoVO;
import mx.com.gseguros.portal.despachador.service.DespachadorManager;
import mx.com.gseguros.portal.endosos.service.EndososManager;
import mx.com.gseguros.portal.general.util.RolSistema;
import mx.com.gseguros.portal.general.util.TipoModelado;
import mx.com.gseguros.portal.general.util.TipoTramite;
import mx.com.gseguros.utils.Utils;

@Controller
@Scope("prototype")
@ParentPackage(value="default")
@Namespace("/flujomesacontrol")
public class FlujoMesaControlAction extends PrincipalCoreAction
{
	private static final long serialVersionUID = 4896753376957054283L;
	
	private static Logger logger = LoggerFactory.getLogger(FlujoMesaControlAction.class);
	
	private boolean success;
	
	private String message;
	
	private Map<String,Item> items;
	
	private FlujoVO flujo;
	
	private Map<String,String> params;
	
	private Map<String,Object> datosTramite;
	
	private List<Map<String,String>> list;
	
	private int start
	            ,limit
	            ,total;
	
	public FlujoMesaControlAction()
	{
		this.session=ActionContext.getContext().getSession();
	}
	
	@Autowired
	private FlujoMesaControlManager flujoMesaControlManager;
	
	@Autowired
	private CotizacionManager cotizacionManager;
	
	@Autowired
	private EndososManager endososManager;
	
	@Autowired
	private DespachadorManager despachadorManager;
	
	@Action(value   = "workflow",
	        results = {
			    @Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
                @Result(name="success" , location="/jsp-script/proceso/flujoMesaControl/workflow.jsp")
            }
	)
	public String workflow () {
		logger.debug(Utils.log(
				"\n######################",
				"\n###### workflow ######",
				"\n###### params = ", params));
		String result = ERROR;
		try {
			UserVO usuario = Utils.validateSession(session);
			if (!"ICE".equals(usuario.getUser()) || !RolSistema.PARAMETRIZADOR_SISTEMAS.getCdsisrol().equals(usuario.getRolActivo().getClave())) {
				throw new ApplicationException("Usuario sin permisos");
			}
			items = flujoMesaControlManager.workflow(usuario.getRolActivo().getClave());
			if (params == null) {
				params = new LinkedHashMap<String, String>();
			}
			if (!params.containsKey("cdtipmod")) {
				params.put("cdtipmod", String.valueOf(TipoModelado.FLUJOS_PROCESOS.getCdtipmod()));
			}
			result = SUCCESS;
		} catch (Exception ex) {
			message = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
				"\n###### result = ", result,
				"\n###### workflow ######",
				"\n######################"));
		return result;
	}
	
	@Action(value   = "jsplumb",
	        results = {
			    @Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
                @Result(name="success" , location="/jsp-script/proceso/flujoMesaControl/jsplumb.jsp")
            }
	)
	public String jsplumb()
	{
		logger.debug(Utils.log(
				 "\n#####################"
				,"\n###### jsplumb ######"
				,"\n#####################"
				));
		return SUCCESS;
	}
	
	@Action(value   = "registrarEntidad",
			results = { @Result(name="success", type="json") }
			)
	public String registrarEntidad()
	{
		logger.debug(Utils.log(
				 "\n##############################"
				,"\n###### registrarEntidad ######"
				,"\n###### params=",params
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			String cdtipflu   = params.get("cdtipflu")
			       ,cdflujomc = params.get("cdflujomc")
			       ,tipo      = params.get("tipo")
			       ,clave     = params.get("clave")
			       ,webid     = params.get("webid")
			       ,xpos      = params.get("xpos")
			       ,ypos      = params.get("ypos");
			
			Utils.validate(
					cdtipflu   , "No se recibi\u00f3 el tipo de flujo"
					,cdflujomc , "No se recibi\u00f3 el flujo"
					,tipo      , "No se recibi\u00f3 el tipo de entidad"
					,clave     , "No se recibi\u00f3 la clave de entidad"
					,webid     , "No se recibi\u00f3 el id de entidad"
					,xpos      , "No se recibi\u00f3 x de entidad"
					,ypos      , "No se recibi\u00f3 y de entidad"
					);
			
			String cdentidad = flujoMesaControlManager.registrarEntidad(
					cdtipflu
					,cdflujomc
					,tipo
					,clave
					,webid
					,xpos
					,ypos
					);
			
			params.put("cdentidad" , cdentidad);
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### registrarEntidad ######"
					,"\n##############################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return SUCCESS;
	}
	
	@Action(value   = "borrarEntidad",
			results = { @Result(name="success", type="json") }
			)
	public String borrarEntidad()
	{
		logger.debug(Utils.log(
				 "\n###########################"
				,"\n###### borrarEntidad ######"
				,"\n###### params=",params
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			String cdtipflu   = params.get("cdtipflu")
			       ,cdflujomc = params.get("cdflujomc")
			       ,tipo      = params.get("tipo")
			       ,clave     = params.get("clave")
			       ,webid     = params.get("webid");
			
			Utils.validate(
					cdtipflu   , "No se recibi\u00f3 el tipo de flujo"
					,cdflujomc , "No se recibi\u00f3 el flujo"
					,tipo      , "No se recibi\u00f3 el tipo de entidad"
					,clave     , "No se recibi\u00f3 la clave de entidad"
					,webid     , "No se recibi\u00f3 el id de entidad"
					);
			
			flujoMesaControlManager.borrarEntidad(
					cdtipflu
					,cdflujomc
					,tipo
					,clave
					,webid
					);
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### borrarEntidad ######"
					,"\n###########################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return SUCCESS;
	}
	
	@Action(value   = "registrarConnection",
			results = { @Result(name="success", type="json") }
			)
	public String registrarConnection()
	{
		logger.debug(Utils.log(
				 "\n#################################"
				,"\n###### registrarConnection ######"
				,"\n###### params=",params
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			String cdtipflu   = params.get("cdtipflu")
			       ,cdflujomc = params.get("cdflujomc")
			       ,idorigen  = params.get("idorigen")
			       ,iddestin  = params.get("iddestin");
			
			Utils.validate(
					cdtipflu   , "No se recibi\u00f3 el tipo de flujo"
					,cdflujomc , "No se recibi\u00f3 el flujo"
					,idorigen  , "No se recibi\u00f3 el origen"
					,iddestin  , "No se recibi\u00f3 el destino"
					);
			
			String cdaccion = flujoMesaControlManager.registrarConnection(
					cdtipflu
					,cdflujomc
					,idorigen
					,iddestin
					);
			
			params.put("cdaccion" , cdaccion);
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### registrarConnection ######"
					,"\n#################################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return SUCCESS;
	}
	
	@Action(value   = "borrarConnection",
			results = { @Result(name="success", type="json") }
			)
	public String borrarConnection()
	{
		logger.debug(Utils.log(
				 "\n##############################"
				,"\n###### borrarConnection ######"
				,"\n###### params=",params
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			String cdtipflu   = params.get("cdtipflu")
			       ,cdflujomc = params.get("cdflujomc")
			       ,cdaccion  = params.get("cdaccion");
			
			Utils.validate(
					cdtipflu   , "No se recibi\u00f3 el tipo de flujo"
					,cdflujomc , "No se recibi\u00f3 el flujo"
					,cdaccion  , "No se recibi\u00f3 la clave"
					);
			
			flujoMesaControlManager.borrarConnection(
					cdtipflu
					,cdflujomc
					,cdaccion
					);
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### borrarConnection ######"
					,"\n##############################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return SUCCESS;
	}
	
	@Action(value   = "movimientoTtipflumc",
			results = { @Result(name="success", type="json") }
			)
	public String movimientoTtipflumc()
	{
		logger.debug(Utils.log(
				 "\n#################################"
				,"\n###### movimientoTtipflumc ######"
				,"\n###### params=",params
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			String accion      = params.get("ACCION")
			       ,cdtipflu   = params.get("CDTIPFLU")
			       ,dstipflu   = params.get("DSTIPFLU")
			       ,cdtiptra   = params.get("CDTIPTRA")
			       ,swreqpol   = params.get("SWREQPOL")
			       ,swmultipol = params.get("SWMULTIPOL")
			       ,cdtipsup   = params.get("CDTIPSUP")
			       ,cdtipmod   = params.get("CDTIPMOD")
			       ,swexterno  = params.get("SWEXTERNO");
			
			Utils.validate(
					accion     , "No se recibi\u00f3 la acci\u00f3n"
					,dstipflu  , "No se recibi\u00f3 el nombre"
					,cdtiptra  , "No se recibi\u00f3 el tipo de tr\u00e1mite"
					,cdtipmod  , "No se recibi\u00f3 el tipo de modelado"
					);
			
			cdtipflu = flujoMesaControlManager.movimientoTtipflumc(
					accion
					,cdtipflu
					,dstipflu
					,cdtiptra
					,swreqpol
					,swmultipol
					,cdtipsup
					,cdtipmod
					,swexterno
					);
			
			params.put("CDTIPFLU",cdtipflu);
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### movimientoTtipflumc ######"
					,"\n#################################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return SUCCESS;
	}
	
	@Action(value   = "movimientoTflujomc",
			results = { @Result(name="success", type="json") }
			)
	public String movimientoTflujomc()
	{
		logger.debug(Utils.log(
				 "\n################################"
				,"\n###### movimientoTflujomc ######"
				,"\n###### params=",params
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			String accion     = params.get("ACCION")
			       ,cdtipflu  = params.get("CDTIPFLU")
			       ,cdflujomc = params.get("CDFLUJOMC")
			       ,dsflujomc = params.get("DSFLUJOMC")
			       ,swfinal   = params.get("SWFINAL")
			       ,cdtipram  = params.get("CDTIPRAM")
			       ,swgrupo   = params.get("SWGRUPO");
			
			Utils.validate(
					accion     , "No se recibi\u00f3 la acci\u00f3n"
					,cdtipflu  , "No se recibi\u00f3 el padre"
					,dsflujomc , "No se recibi\u00f3 el nombre"
					,cdtipram  , "No se recibi\u00f3 el tipo de ramo"
					);
			
			cdflujomc = flujoMesaControlManager.movimientoTflujomc(
					accion
					,cdtipflu
					,cdflujomc
					,dsflujomc
					,swfinal
					,cdtipram
					,swgrupo
					);
			
			params.put("CDFLUJOMC" , cdflujomc); // regresa el nuevo cuando se inserta
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### movimientoTflujomc ######"
					,"\n################################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return SUCCESS;
	}
	
	@Action(value   = "movimientoCatalogo",
			results = { @Result(name="success", type="json") }
			)
	public String movimientoCatalogo()
	{
		logger.debug(Utils.log(
				 "\n################################"
				,"\n###### movimientoCatalogo ######"
				,"\n###### params=",params
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			String accion = params.get("ACCION")
			       ,tipo  = params.get("tipo");
			
			Utils.validate(
					accion , "No se recibi\u00f3 la acci\u00f3n"
					,tipo  , "No se recibi\u00f3 el tipo"
					);
			
			flujoMesaControlManager.movimientoCatalogo(
					accion
					,tipo
					,params
					);
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### movimientoCatalogo ######"
					,"\n################################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return SUCCESS;
	}
	
	@Action(value   = "cargarModelado",
			results = { @Result(name="success", type="json") }
			)
	public String cargarModelado()
	{
		logger.debug(Utils.log(
				 "\n############################"
				,"\n###### cargarModelado ######"
				,"\n###### params=",params
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			String cdtipflu   = params.get("cdtipflu")
				   ,cdflujomc = params.get("cdflujomc");
			
			Utils.validate(
					cdtipflu   , "No se recibi\u00f3 el tipo de flujo"
					,cdflujomc , "No se recibi\u00f3 la clave de flujo"
					);
			
			list = flujoMesaControlManager.cargarModelado(
					cdtipflu
					,cdflujomc
					);
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### cargarModelado ######"
					,"\n############################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return SUCCESS;
	}
	
	@Action(value   = "cargarDatosEstado",
			results = { @Result(name="success", type="json") }
			)
	public String cargarDatosEstado()
	{
		logger.debug(Utils.log(
				 "\n###############################"
				,"\n###### cargarDatosEstado ######"
				,"\n###### params=",params
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			String cdtipflu    = params.get("cdtipflu")
				   ,cdflujomc  = params.get("cdflujomc")
				   ,cdestadomc = params.get("cdestadomc");
			
			Utils.validate(
					cdtipflu    , "No se recibi\u00f3 el tipo de flujo"
					,cdflujomc  , "No se recibi\u00f3 la clave de flujo"
					,cdestadomc , "No se recibi\u00f3 la clave de status"
					);
			
			Map<String,Object> res = flujoMesaControlManager.cargarDatosEstado(
					cdtipflu
					,cdflujomc
					,cdestadomc
					);
			
			params = (Map<String,String>)res.get("mapa");
			list   = (List<Map<String,String>>)res.get("lista");
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### cargarDatosEstado ######"
					,"\n###############################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return SUCCESS;
	}
	
	@Action(value           = "guardarDatosEstado",
			results         = { @Result(name="success", type="json") },
            interceptorRefs = {
			    @InterceptorRef(value = "json", params = {"enableSMD", "true", "ignoreSMDMethodInterfaces", "false" })
			})
	public String guardarDatosEstado()
	{
		logger.debug(Utils.log(
				 "\n################################"
				,"\n###### guardarDatosEstado ######"
				,"\n###### params=" , params
				,"\n###### list="   , list
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			Utils.validate(list , "No se recibieron permisos ni avisos");
			
			String cdtipflu    = params.get("CDTIPFLU")
			       ,cdflujomc  = params.get("CDFLUJOMC")
			       ,cdestadomc = params.get("CDESTADOMC")
			       ,accion     = params.get("ACCION")
			       ,webid      = params.get("WEBID")
			       ,xpos       = params.get("XPOS")
			       ,ypos       = params.get("YPOS")
			       ,timemaxh   = params.get("TIMEMAXH")
			       ,timemaxm   = params.get("TIMEMAXM")
			       ,timewrn1h  = params.get("TIMEWRN1H")
			       ,timewrn1m  = params.get("TIMEWRN1M")
			       ,timewrn2h  = params.get("TIMEWRN2H")
			       ,timewrn2m  = params.get("TIMEWRN2M")
			       ,cdtipasig  = params.get("CDTIPASIG")
			       ,swescala   = params.get("SWESCALA")
			       ,statusout  = params.get("STATUSOUT")
			       ,swfinnode  = params.get("SWFINNODE")
			       ,cdetapa    = params.get("CDETAPA")
			       ;
			
			Utils.validate(
					cdtipflu    , "No se recibi\u00f3 el tipo de flujo"
					,cdflujomc  , "No se recibi\u00f3 la clave de flujo"
					,cdestadomc , "No se recibi\u00f3 la clave de status"
					,accion     , "No se recibi\u00f3 la operaci\u00f3n"
					,webid      , "No se recibi\u00f3 el id"
					,xpos       , "No se recibi\u00f3 x"
					,ypos       , "No se recibi\u00f3 y"
					,timemaxh   , "No se recibi\u00f3 horas max"
					,timemaxm   , "No se recibi\u00f3 minutos max"
					,timewrn1h  , "No se recibi\u00f3 horas max alerta 1"
					,timewrn1m  , "No se recibi\u00f3 minutos max alerta 1"
					,timewrn2h  , "No se recibi\u00f3 horas max alerta 2"
					,timewrn2m  , "No se recibi\u00f3 minutos max alerta 2"
					,cdtipasig  , "No se recibi\u00f3 tipo de asignaci\u00f3n"
					,cdetapa    , "No se recibi\u00f3 el indicador"
					);
			
			flujoMesaControlManager.guardarDatosEstado(
					cdtipflu
					,cdflujomc
					,cdestadomc
					,accion
					,webid
					,xpos
					,ypos
					,timemaxh
					,timemaxm
					,timewrn1h
					,timewrn1m
					,timewrn2h
					,timewrn2m
					,cdtipasig
					,swescala
					,list
					,statusout
					,"S".equals(swfinnode)
					,cdetapa
					);
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### guardarDatosEstado ######"
					,"\n################################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return SUCCESS;
	}
	
	@Action(value   = "cargarDatosValidacion",
			results = { @Result(name="success", type="json") }
			)
	public String cargarDatosValidacion()
	{
		logger.debug(Utils.log(
				 "\n###################################"
				,"\n###### cargarDatosValidacion ######"
				,"\n###### params=",params
				));
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			String cdtipflu   = params.get("cdtipflu")
				   ,cdflujomc = params.get("cdflujomc")
				   ,cdvalida  = params.get("cdvalida");
			
			Utils.validate(
					cdtipflu   , "No se recibi\u00f3 el tipo de flujo"
					,cdflujomc , "No se recibi\u00f3 la clave de flujo"
					,cdvalida  , "No se recibi\u00f3 la clave de validaci\u00f3n"
					);
			
			Map<String,String> res = flujoMesaControlManager.cargarDatosValidacion(
					cdtipflu
					,cdflujomc
					,cdvalida
					);
			
			params.putAll(res);
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### cargarDatosValidacion ######"
					,"\n###################################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return SUCCESS;
	}
	
	@Action(value   = "guardarDatosValidacion",
			results = { @Result(name="success", type="json") }
			)
	public String guardarDatosValidacion()
	{
		logger.debug(Utils.log(
				 "\n####################################"
				,"\n###### guardarDatosValidacion ######"
				,"\n###### params=",params
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			String cdtipflu    = params.get("CDTIPFLU")
			       ,cdflujomc  = params.get("CDFLUJOMC")
			       ,cdvalida   = params.get("CDVALIDA")
			       ,webid      = params.get("WEBID")
			       ,xpos       = params.get("XPOS")
			       ,ypos       = params.get("YPOS")
			       ,dsvalida   = params.get("DSVALIDA")
			       ,cdvalidafk = params.get("CDVALIDAFK")
			       ,jsvalida   = params.get("JSVALIDA")
			       ,accion     = params.get("ACCION");
			
			Utils.validate(
					cdtipflu    , "No se recibi\u00f3 el tipo de flujo"
					,cdflujomc  , "No se recibi\u00f3 la clave de flujo"
					,cdvalida   , "No se recibi\u00f3 la clave de validaci\u00f3n"
					,webid      , "No se recibi\u00f3 el id"
					,xpos       , "No se recibi\u00f3 x"
					,ypos       , "No se recibi\u00f3 y"
					,dsvalida   , "No se recibi\u00f3 el nombre"
					,cdvalidafk , "No se recibi\u00f3 la validaci\u00f3n"
					,accion     , "No se recibi\u00f3 el tipo de operaci\u00f3n"
					);
			
			flujoMesaControlManager.guardarDatosValidacion(
					cdtipflu
					,cdflujomc
					,cdvalida
					,webid
					,xpos
					,ypos
					,dsvalida
					,cdvalidafk
					,jsvalida
					,accion
					);
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### guardarDatosValidacion ######"
					,"\n####################################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return SUCCESS;
	}
	
	@Action(value           = "guardarCoordenadas",
			results         = { @Result(name="success", type="json") },
            interceptorRefs = {
			    @InterceptorRef(value = "json", params = {"enableSMD", "true", "ignoreSMDMethodInterfaces", "false" })
			})
	public String guardarCoordenadas()
	{
		logger.debug(Utils.log(
				 "\n################################"
				,"\n###### guardarCoordenadas ######"
				,"\n###### params=" , params
				,"\n###### list="   , list
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params, "No se recibieron datos");
			
			String cdtipflu   = params.get("cdtipflu")
			       ,cdflujomc = params.get("cdflujomc");
			
			Utils.validate(list, "No se recibieron entidades");
			
			flujoMesaControlManager.guardarCoordenadas(
					cdtipflu
					,cdflujomc
					,list
					);
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### guardarCoordenadas ######"
					,"\n################################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return SUCCESS;
	}
	
	@Action(value   = "ejecutaValidacion",
			results = { @Result(name="success", type="json") }
			)
	public String ejecutaValidacion()
	{
		logger.debug(Utils.log(
				 "\n###############################"
				,"\n###### ejecutaValidacion ######"
				,"\n###### flujo="  , flujo
				,"\n###### params=" , params
				));
		try
		{
			UserVO usuario = Utils.validateSession(session);
			
			Utils.validate(flujo  , "No se recibieron datos del flujo");
			Utils.validate(params , "No se recibieron par\u00e1metros");
			
			String cdvalidafk = params.get("cdvalidafk");
			
			Utils.validate(cdvalidafk , "No se recibi\u00f3 clave de validaci\u00f3n");
			
			params.put("salida" , flujoMesaControlManager.ejecutaValidacion(
					flujo,
					cdvalidafk,
					usuario.getUser(),
					usuario.getRolActivo().getClave()
					));
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### params=" , params
					,"\n###### ejecutaValidacion ######"
					,"\n###############################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		return SUCCESS;
	}
	
	@Action(value   = "cargarDatosRevision",
			results = { @Result(name="success", type="json") }
			)
	public String cargarDatosRevision()
	{
		logger.debug(Utils.log(
				 "\n#################################"
				,"\n###### cargarDatosRevision ######"
				,"\n###### params=",params
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			String cdtipflu   = params.get("cdtipflu")
				   ,cdflujomc = params.get("cdflujomc")
				   ,cdrevisi  = params.get("cdrevisi");
			
			Utils.validate(
					cdtipflu   , "No se recibi\u00f3 el tipo de flujo"
					,cdflujomc , "No se recibi\u00f3 la clave de flujo"
					,cdrevisi  , "No se recibi\u00f3 la clave de revisi\u00f3n"
					);
			
			Map<String,Object> res = flujoMesaControlManager.cargarDatosRevision(
					cdtipflu
					,cdflujomc
					,cdrevisi
					);
			
			params = (Map<String,String>)res.get("mapa");
			list   = (List<Map<String,String>>)res.get("lista");
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### cargarDatosRevision ######"
					,"\n#################################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return SUCCESS;
	}
	
	@Action(value           = "guardarDatosRevision",
			results         = { @Result(name="success", type="json") },
            interceptorRefs = {
			    @InterceptorRef(value = "json", params = {"enableSMD", "true", "ignoreSMDMethodInterfaces", "false" })
			})
	public String guardarDatosRevision()
	{
		logger.debug(Utils.log(
				 "\n##################################"
				,"\n###### guardarDatosRevision ######"
				,"\n###### params=" , params
				,"\n###### list="   , list
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			Utils.validate(list , "No se recibieron documentos");
			
			String cdtipflu   = params.get("CDTIPFLU")
			       ,cdflujomc = params.get("CDFLUJOMC")
			       ,cdrevisi  = params.get("CDREVISI")
			       ,dsrevisi  = params.get("DSREVISI")
			       ,accion    = params.get("ACCION")
			       ,webid     = params.get("WEBID")
			       ,xpos      = params.get("XPOS")
			       ,ypos      = params.get("YPOS")
			       ;
			
			Utils.validate(
					cdtipflu   , "No se recibi\u00f3 el tipo de flujo"
					,cdflujomc , "No se recibi\u00f3 la clave de flujo"
					,cdrevisi  , "No se recibi\u00f3 la clave de revisi\u00f3n"
					,dsrevisi  , "No se recibi\u00f3 el nombre de revisi\u00f3n"
					,accion    , "No se recibi\u00f3 la operaci\u00f3n"
					,webid     , "No se recibi\u00f3 el id"
					,xpos      , "No se recibi\u00f3 x"
					,ypos      , "No se recibi\u00f3 y"
					);
			
			flujoMesaControlManager.guardarDatosRevision(
					cdtipflu
					,cdflujomc
					,cdrevisi
					,dsrevisi
					,accion
					,webid
					,xpos
					,ypos
					,list
					);
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### guardarDatosRevision ######"
					,"\n##################################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return SUCCESS;
	}
	
	@Action(value   = "movimientoTdocume",
			results = { @Result(name="success", type="json") }
			)
	public String movimientoTdocume()
	{
		logger.debug(Utils.log(
				 "\n###############################"
				,"\n###### movimientoTdocume ######"
				,"\n###### params=",params
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			String accion    = params.get("ACCION")
			       ,cddocume = params.get("CDDOCUME")
			       ,dsdocume = params.get("DSDOCUME")
			       ,cdtiptra = params.get("CDTIPTRA");
			
			Utils.validate(
					accion    , "No se recibi\u00f3 la acci\u00f3n"
					,dsdocume , "No se recibi\u00f3 el nombre"
					,cdtiptra , "No se recibi\u00f3 el tipo de tr\u00e1mite"
					);
			
			flujoMesaControlManager.movimientoTdocume(
					accion
					,cddocume
					,dsdocume
					,cdtiptra
					);
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### movimientoTdocume ######"
					,"\n###############################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return SUCCESS;
	}
	
	@Action(value   = "cargarDatosAccion",
			results = { @Result(name="success", type="json") }
			)
	public String cargarDatosAccion()
	{
		logger.debug(Utils.log(
				 "\n###############################"
				,"\n###### cargarDatosAccion ######"
				,"\n###### params=",params
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			String cdtipflu   = params.get("cdtipflu")
				   ,cdflujomc = params.get("cdflujomc")
				   ,cdaccion  = params.get("cdaccion");
			
			Utils.validate(
					cdtipflu   , "No se recibi\u00f3 el tipo de flujo"
					,cdflujomc , "No se recibi\u00f3 la clave de flujo"
					,cdaccion  , "No se recibi\u00f3 la clave de acci\u00f3n"
					);
			
			Map<String,Object> res = flujoMesaControlManager.cargarDatosAccion(
					cdtipflu
					,cdflujomc
					,cdaccion
					);
			
			params = (Map<String,String>)res.get("mapa");
			list   = (List<Map<String,String>>)res.get("lista");
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### cargarDatosAccion ######"
					,"\n###############################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return SUCCESS;
	}
	
	@Action(value           = "guardarDatosAccion",
			results         = { @Result(name="success", type="json") },
            interceptorRefs = {
			    @InterceptorRef(value = "json", params = {"enableSMD", "true", "ignoreSMDMethodInterfaces", "false" })
			})
	public String guardarDatosAccion()
	{
		logger.debug(Utils.log(
				 "\n################################"
				,"\n###### guardarDatosAccion ######"
				,"\n###### params=" , params
				,"\n###### list="   , list
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			Utils.validate(list , "No se recibieron permisos");
			
			String cdtipflu   = params.get("CDTIPFLU")
			       ,cdflujomc = params.get("CDFLUJOMC")
			       ,cdaccion  = params.get("CDACCION")
			       ,dsaccion  = params.get("DSACCION")
			       ,accion    = params.get("ACCION")
			       ,idorigen  = params.get("IDORIGEN")
			       ,iddestin  = params.get("IDDESTIN")
			       ,cdvalor   = params.get("CDVALOR")
			       ,cdicono   = params.get("CDICONO")
			       ,swescala  = params.get("SWESCALA")
			       ,aux       = params.get("AUX");
			
			Utils.validate(
					cdtipflu   , "No se recibi\u00f3 el tipo de flujo"
					,cdflujomc , "No se recibi\u00f3 la clave de flujo"
					,cdaccion  , "No se recibi\u00f3 la clave de acci\u00f3n"
					,dsaccion  , "No se recibi\u00f3 el nombre de acci\u00f3n"
					,accion    , "No se recibi\u00f3 la operaci\u00f3n"
					,idorigen  , "No se recibi\u00f3 el origen"
					,iddestin  , "No se recibi\u00f3 el destino"
					);
			
			flujoMesaControlManager.guardarDatosAccion(
					cdtipflu
					,cdflujomc
					,cdaccion
					,dsaccion
					,accion
					,idorigen
					,iddestin
					,cdvalor
					,cdicono
					,swescala
					,aux
					,list
					);
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### guardarDatosAccion ######"
					,"\n################################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return SUCCESS;
	}
	
	@Action(value   = "debugScreen",
	        results = {
			    @Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
                @Result(name="success" , location="/jsp-script/proceso/flujoMesaControl/debugScreen.jsp")
            }
	)
	public String debugScreen()
	{
		logger.debug(Utils.log(
				 "\n#########################"
				,"\n###### debugScreen ######"
				));
		String result = ERROR;
		try
		{
			items = flujoMesaControlManager.debugScreen();
			
			result = SUCCESS;
			
			logger.debug(Utils.log(
					 "\n###### debugScreen ######"
					,"\n#########################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		return result;
	}
	
	@Action(value   = "mesaControl",
	        results = {
			    @Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
                @Result(name="success" , location="/jsp-script/proceso/flujoMesaControl/mesaControl.jsp")
            }
	)
	public String mesaControl()
	{
		logger.debug(Utils.log(
				 "\n#########################"
				,"\n###### mesaControl ######"
				));
		String result = ERROR;
		try
		{
			UserVO usuario = Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			String agrupamc = params.get("AGRUPAMC");
			Utils.validate(agrupamc, "No se recibi\u00f3 el agrupador");
			
			try
			{
				AgrupadorMC agrupador = AgrupadorMC.valueOf(agrupamc);
			}
			catch(Exception ex)
			{
				throw new ApplicationException("No se reconoce el agrupador");
			}
			
			Map<String,Object> manRes = flujoMesaControlManager.mesaControl(
					usuario.getRolActivo().getClave()
					,agrupamc
					,usuario.getUser()
					);
			
			items = (Map<String,Item>)manRes.get("items");
			params.putAll((Map<String,String>)manRes.get("mapa"));
			
			params.put("CDUSUARI" , usuario.getUser());
			params.put("CDSISROL" , usuario.getRolActivo().getClave());
			params.put("CDUNIECO" , usuario.getCdUnieco());
			
			result = SUCCESS;
			
			logger.debug(Utils.log(
					 "\n###### mesaControl ######"
					,"\n#########################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		return result;
	}
	
	@Action(value   = "recuperarTramites",
			results = { @Result(name="success", type="json") }
			)
	public String recuperarTramites()
	{
		logger.debug(Utils.log(
				 "\n###############################"
				,"\n###### recuperarTramites ######"
				,"\n###### params=" , params
				,"\n###### start="  , start
				,"\n###### limit="  , limit
				));
		try
		{
			UserVO usuario = Utils.validateSession(session);
			
			Utils.validate(params, "No se recibieron datos");
			
			//obligatorios
			String agrupamc = params.get("AGRUPAMC")
			       ,status  = params.get("STATUS");
			
			//opcionales
			String cdunieco  = params.get("CDUNIECO")
			       ,cdramo   = params.get("CDRAMO")
			       ,cdtipsit = params.get("CDTIPSIT")
			       ,estado   = params.get("ESTADO")
			       ,nmpoliza = params.get("NMPOLIZA")
			       ,cdagente = params.get("CDAGENTE")
			       ,ntramite = params.get("NTRAMITE")
			       ,fedesde  = params.get("FEDESDE")
			       ,fehasta  = params.get("FEHASTA")
			       ,filtro   = params.get("FILTRO")
			       ,dscontra = params.get("DSCONTRA");
			
			String cdpersonCliente = params.get("CDPERSONCLI");
			
			Utils.validate(
					agrupamc , "No se recibi\u00f3n el agrupador"
					,status  , "No se recibi\u00f3n el status"
					);
			
			try
			{
				AgrupadorMC agrupador = AgrupadorMC.valueOf(agrupamc);
			}
			catch(Exception ex)
			{
				throw new ApplicationException("No se reconoce el agrupador");
			}
			
			Map<String,Object> manRes = flujoMesaControlManager.recuperarTramites(
					agrupamc
					,status
					,usuario.getUser()
					,usuario.getRolActivo().getClave()
					,cdunieco
					,cdramo
					,cdtipsit
					,estado
					,nmpoliza
					,cdagente
					,ntramite
					,fedesde
					,fehasta
					,cdpersonCliente
					,filtro
					,dscontra
					,start
					,limit
					);
			
			list  = (List<Map<String,String>>)manRes.get("lista");
			total = (Integer)manRes.get("total");
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### list=",list
					,"\n###### recuperarTramites ######"
					,"\n###############################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		return SUCCESS;
	}
	
	@Action(value   = "recuperarPolizaUnica",
			results = { @Result(name="success", type="json") }
			)
	public String recuperarPolizaUnica()
	{
		logger.debug(Utils.log(
				 "\n##################################"
				,"\n###### recuperarPolizaUnica ######"
				,"\n###### params=",params
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params, "No se recibieron datos");
			
			String cdunieco  = params.get("CDUNIECO")
			       ,ramo     = params.get("RAMO")
			       ,estado   = params.get("ESTADO")
			       ,nmpoliza = params.get("NMPOLIZA");
			
			Utils.validate(
					cdunieco  , "No se recibi\u00f3 la sucursal"
					,ramo     , "No se recibi\u00f3 el ramo"
					,estado   , "No se recibi\u00f3 el estado"
					,nmpoliza , "No se recibi\u00f3 la p\u00f3liza"
					);
			
			params.putAll(flujoMesaControlManager.recuperarPolizaUnica(cdunieco,ramo,estado,nmpoliza));
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### params=",params
					,"\n###### recuperarPolizaUnica ######"
					,"\n##################################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return SUCCESS;
	}
	
	@Action(value   = "registrarTramite",
			results = { @Result(name="success", type="json") }
			)
	public String registrarTramite()
	{
		logger.debug(Utils.log(
				 "\n##############################"
				,"\n###### registrarTramite ######"
				,"\n###### params=",params
				));
		try
		{
			UserVO usuario = Utils.validateSession(session);
			
			Utils.validate(params, "No se recibieron datos");
			
			String cdtiptra    = params.get("CDTIPTRA")
			       ,cdtipsup   = params.get("CDTIPSUP")
			       ,cdtipflu   = params.get("CDTIPFLU")
			       ,cdflujomc  = params.get("CDFLUJOMC")
			       ,cdsucadm   = params.get("CDSUCADM")
			       ,cdsucdoc   = params.get("CDSUCDOC")
			       ,cdramo     = params.get("CDRAMO")
			       ,cdtipsit   = params.get("CDTIPSIT")
			       ,nmpoliza   = params.get("NMPOLIZA")
			       ,cdagente   = params.get("CDAGENTE")
			       ,referencia = params.get("REFERENCIA")
			       ,nombre     = params.get("NOMBRE")
			       ,status     = params.get("STATUS")
			       ,comments   = params.get("COMMENTS")
			       ,estado     = params.get("ESTADO")
			       ,cduniext   = params.get("CDUNIEXT")
			       ,ramo       = params.get("RAMO")
			       ,nmpoliex   = params.get("NMPOLIEX");
			       
			Utils.validate(
					cdtiptra   , "No se recibi\u00f3 el tipo de tr\u00e1imte"
					,cdtipflu  , "No se recibi\u00f3 el flujo"
					,cdflujomc , "No se recibi\u00f3 el proceso"
					,status    , "No se recibi\u00f3 el status"
					);
			
			Date ferecepc  = new Date()
			     ,festatus = new Date();
			
			// Si hay otvalores se mandan
			Map<String, String> otvalores = null;
			for (int i = 1; i <= 50; i++) {
			    String key = Utils.join("otvalor", StringUtils.leftPad(String.valueOf(i), 2, "0"));
			    if (params.containsKey(key)) {
			        if (otvalores == null) {
			            otvalores = new HashMap<String, String>();
			        }
			        otvalores.put(key, params.get(key));
			    }
			}
			
			String ntramite = flujoMesaControlManager.registrarTramite(
					cdsucdoc
					,cdramo
					,estado
					,nmpoliza
					,null //nmsuplem
					,cdsucadm
					,cdsucdoc
					,cdtiptra
					,ferecepc
					,cdagente
					,referencia
					,nombre
					,festatus
					,status
					,comments
					,null //nmsolici
					,cdtipsit
					,usuario.getUser()
					,usuario.getRolActivo().getClave()
					,null //swimpres
					,cdtipflu
					,cdflujomc
					,otvalores
					,cdtipsup
					,cduniext
					,ramo
					,nmpoliex
					,true, false
					);
			
			if(TipoTramite.ENDOSO.getCdtiptra().equals(cdtiptra))
			{
				logger.debug("Guardando clave y descripci\u00f3n de tipo de endoso en valores adicionales");
				
				cotizacionManager.actualizarOtvalorTramitePorDsatribu(
						ntramite
						,"CDTIPSUP"
						,cdtipsup
						,"U"
						);
				
				cotizacionManager.actualizarOtvalorTramitePorDsatribu(
						ntramite
						,"DSTIPSUP"
						,endososManager.obtieneDescripcionEndoso(cdtipsup)
						,"U"
						);
			}
			
			params.put("ntramite" , ntramite);
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### params=",params
					,"\n###### registrarTramite ######"
					,"\n##############################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		return SUCCESS;
	}
	
	@Action(value   = "cargarAccionesEntidad",
			results = { @Result(name="success", type="json") }
			)
	public String cargarAccionesEntidad()
	{
		logger.debug(Utils.log(
				 "\n###################################"
				,"\n###### cargarAccionesEntidad ######"
				,"\n###### params=",params
				));
		try
		{
			UserVO usuario   = Utils.validateSession(session);
			String cdusuari  = usuario.getUser()
			       ,cdsisrol = usuario.getRolActivo().getClave();
			
			Utils.validate(params, "No se recibieron datos");
			
			String cdtipflu   = params.get("cdtipflu")
			       ,cdflujomc = params.get("cdflujomc")
			       ,tipoent   = params.get("tipoent")
			       ,cdentidad = params.get("cdentidad")
			       ,webid     = params.get("webid");
			       
			Utils.validate(
					cdtipflu   , "No se recibi\u00f3 el flujo"
					,cdflujomc , "No se recibi\u00f3 el proceso"
					,tipoent   , "No se recibi\u00f3 la entidad"
					,cdentidad , "No se recibi\u00f3 la clave"
					);
			
			list = flujoMesaControlManager.cargarAccionesEntidad(
					cdtipflu
					,cdflujomc
					,tipoent
					,cdentidad
					,webid
					,cdsisrol
					);
			
			params.put("cdusuari" , cdusuari);
			params.put("cdsisrol" , cdsisrol);
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### cargarAccionesEntidad ######"
					,"\n###################################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		return SUCCESS;
	}
	
	@Action(value   = "pantallaExterna",
	        results = {
			    @Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
                @Result(name="success" , location="/jsp-script/proceso/flujoMesaControl/pantallaExterna.jsp")
            }
	)
	public String pantallaExterna()
	{
		logger.debug(Utils.log(
				 "\n#############################"
				,"\n###### pantallaExterna ######"
				,"\n###### params=" , params
				,"\n###### flujo="  , flujo
				));
		
		String result = ERROR;
		
		try
		{
			UserVO usuario = Utils.validateSession(session);
			
			Utils.validate(params, "No se recibieron datos");
			
			String urlExterna = params.get("url");
			
			Utils.validate(urlExterna , "No hay url");
			
			String parametros = Utils.join(
					"?flujo.cdtipflu="   , flujo.getCdtipflu()
					,"&flujo.cdflujomc=" , flujo.getCdflujomc()
					,"&flujo.tipoent="   , flujo.getTipoent()
					,"&flujo.claveent="  , flujo.getClaveent()
					,"&flujo.webid="     , flujo.getWebid()
					,"&flujo.ntramite="  , flujo.getNtramite()
					,"&flujo.status="    , flujo.getStatus()
					,"&flujo.cdunieco="  , flujo.getCdunieco()
					,"&flujo.cdramo="    , flujo.getCdramo()
					,"&flujo.estado="    , flujo.getEstado()
					,"&flujo.nmpoliza="  , flujo.getNmpoliza()
					,"&flujo.nmsituac="  , flujo.getNmsituac()
					,"&flujo.nmsuplem="  , flujo.getNmsuplem()
					);
			
			urlExterna = Utils.join(urlExterna,parametros.replace("flujo.",""),"&cdusuari=",usuario.getUser(),"&cdsisrol=",usuario.getRolActivo().getClave());
			
			params.put("urlInterna" , parametros);
			params.put("urlExterna" , urlExterna);
			
			result = SUCCESS;
			
			logger.debug(Utils.log(
					 "\n###### result=",result
					,"\n###### pantallaExterna ######"
					,"\n#############################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return result;
	}
	
	@Action(value   = "controladorExterno",
	        results = {
			    @Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
                @Result(name="success" , location="/jsp-script/proceso/flujoMesaControl/controladorExterno.jsp")
            }
	)
	public String controladorExterno()
	{
		logger.debug(Utils.log(
				 "\n#################################"
				,"\n###### controladorExterno ######"
				,"\n###### flujo="  , flujo
				));
		
		String result = ERROR;
		
		try
		{
			UserVO usuario = Utils.validateSession(session);
			
			Utils.validate(flujo , "No se recibieron datos");
			
			result = SUCCESS;
			
			logger.debug(Utils.log(
					 "\n###### result=",result
					,"\n###### controladorExterno ######"
					,"\n################################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return result;
	}
	
	@Action(value   = "pantallaDiagnosticoFlujo",
	        results = {
			    @Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
                @Result(name="success" , location="/jsp-script/proceso/flujoMesaControl/diagnosticoFlujo.jsp")
            }
	)
	public String pantallaDiagnosticoFlujo()
	{
		logger.debug(Utils.log(
				 "\n######################################"
				,"\n###### pantallaDiagnosticoFlujo ######"
				,"\n###### flujo=", flujo
				));
		
		String result = ERROR;
		
		try
		{
			UserVO usuario = Utils.validateSession(session);
			
			Utils.validate(flujo , "No se recibieron datos");
			
			result = SUCCESS;
			
			logger.debug(Utils.log(
					 "\n###### result=",result
					,"\n###### pantallaDiagnosticoFlujo ######"
					,"\n######################################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return result;
	}
	
	@Action(value   = "procesoDemo",
			results = { @Result(name="success", type="json") }
			)
	public String cambiarFechaRecepcion()
	{
		logger.debug(Utils.log(
				 "\n#########################"
				,"\n###### procesoDemo ######"
				,"\n###### flujo=",flujo
				));
		try
		{
			UserVO usuario = Utils.validateSession(session);
			
			Utils.validate(flujo, "No se recibieron datos");
			
			flujoMesaControlManager.procesoDemo(
					flujo
					,usuario.getUser()
					,usuario.getRolActivo().getClave()
					);
			
			message = "Se agreg\u00f3 un nuevo registro de detalle para el tr\u00e1mite";
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### procesoDemo ######"
					,"\n#########################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		return SUCCESS;
	}
	
	@Action(value   = "ejecutaRevision",
			results = { @Result(name="success", type="json") }
			)
	public String ejecutaRevision()
	{
		logger.debug(Utils.log(
				 "\n#############################"
				,"\n###### ejecutaRevision ######"
				,"\n###### flujo="  , flujo
				,"\n###### params=" , params
				));
		try
		{
			UserVO usuario = Utils.validateSession(session);
			String cdusuari = usuario.getUser();
            String cdsisrol = usuario.getRolActivo().getClave();
			
			Utils.validate(flujo  , "No se recibieron datos del flujo");
			
			Map<String, Object> result = flujoMesaControlManager.ejecutaRevision(flujo, cdusuari, cdsisrol);
			
			list = (List<Map<String, String>>) result.get("lista");
			
			if (params == null) {
				params = new HashMap<String, String>();
			}
			
			params.put("swconfirm", (String) result.get("swconfirm"));
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### params=" , params
					,"\n###### ejecutaRevision ######"
					,"\n#############################"
					));
			
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		return SUCCESS;
	}
	
	/*
	@Action(value   = "turnarTramite",
			results = { @Result(name="success", type="json") }
			)
	public String turnarTramite()
	{
		logger.debug(Utils.log(
				 "\n###########################"
				,"\n###### turnarTramite ######"
				,"\n###### params=" , params
				));
		try
		{
			UserVO user        = Utils.validateSession(session);
			String cdusuariSes = user.getUser();
			String cdsisrolSes = user.getRolActivo().getClave();
			
			Utils.validate(params, "No se recibieron datos");
			
			String ntramite      = params.get("ntramite")
			       ,statusOld    = params.get("statusOld")
			       ,cdtipasigOld = params.get("cdtipasigOld")
			       ,statusNew    = params.get("statusNew")
			       ,cdtipasigNew = params.get("cdtipasigNew")
			       ,comments     = params.get("comments")
			       ,swagente     = params.get("swagente");
			
			boolean cerrado = "S".equals(params.get("cerrado"));
			
			Utils.validate(
					ntramite      , "No se recibi\u00f3 el tr\u00e1mite"
					,statusOld    , "No se recibi\u00f3 el status anterior"
					,cdtipasigOld , "No se recibi\u00f3 el tipo de status anterior"
					,statusNew    , "No se recibi\u00f3 el status nuevo"
					,cdtipasigNew , "No se recibi\u00f3 el tipo de status nuevo"
					);
			
			message = flujoMesaControlManager.turnarTramite(
					ntramite
					,statusOld
					,cdtipasigOld
					,statusNew
					,cdtipasigNew
					,cdusuariSes
					,cdsisrolSes
					,comments
					,cerrado
					,swagente
					);
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### message=" , message
					,"\n###### turnarTramite ######"
					,"\n###########################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		return SUCCESS;
	}
	*/
	
	@Action(value   = "recuperarDatosTramiteValidacionCliente",
			results = { @Result(name="success", type="json") }
			)
	public String recuperarDatosTramiteValidacionCliente()
	{
		logger.debug(Utils.log(
				 "\n####################################################"
				,"\n###### recuperarDatosTramiteValidacionCliente ######"
				,"\n###### flujo=" , flujo
				));
		try
		{
			UserVO user     = Utils.validateSession(session);
			String cdusuari = user.getUser();
			String cdsisrol = user.getRolActivo().getClave();
			
			Utils.validate(flujo, "No se recibieron datos de flujo");
			
			datosTramite = flujoMesaControlManager.recuperarDatosTramiteValidacionCliente(flujo);
			
			datosTramite.put("CDUSUARI" , cdusuari);
			datosTramite.put("CDSISROL" , cdsisrol);
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### recuperarDatosTramiteValidacionCliente ######"
					,"\n####################################################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		return SUCCESS;
	}
	
	@Action(value   = "turnarDesdeComp",
			results = { @Result(name="success", type="json") }
			)
	public String turnar () {
		logger.debug(Utils.log(
				 "\n#############################"
				,"\n###### turnarDesdeComp ######"
				,"\n###### params=" , params));
		try {
			UserVO user = Utils.validateSession(session);
			
			String cdusuari = user.getUser(),
			       cdsisrol = user.getRolActivo().getClave();
			
			Utils.validate(params, "No se recibieron datos");
			
			String cdtipflu    = params.get("CDTIPFLU"),
			       cdflujomc   = params.get("CDFLUJOMC"),
			       ntramite    = params.get("NTRAMITE"),
			       statusOld   = params.get("STATUSOLD"),
			       statusNew   = params.get("STATUSNEW"),
			       swagente    = params.get("SWAGENTE"),
			       comments    = params.get("COMMENTS"),
			       cdrazrecha  = params.get("CDRAZRECHA"),
			       cdusuariDes = params.get("CDUSUARI_DES"),
			       cdsisrolDes = params.get("CDSISROL_DES"),
			       ntrasust    = params.get("NTRASUST"),
			       correos     = params.get("CORREOS");
			
			boolean cerrado              = "S".equals(params.get("cerrado")),
			        soloCorreosRecibidos = "S".equals(params.get("SOLO_CORREOS_RECIBIDOS"));
			
			Date fechaHoy = new Date();
			
			Utils.validate(ntramite  , "No se recibi\u00f3 el tr\u00e1mite",
			               statusNew , "No se recibi\u00f3 el status nuevo");
			
			RespuestaTurnadoVO despacho = despachadorManager.turnarTramite(
			        cdusuari,
			        cdsisrol,
			        ntramite,
			        statusNew,
			        comments,
			        cdrazrecha,
			        cdusuariDes,
			        cdsisrolDes,
			        "S".equalsIgnoreCase(swagente),
			        false, // porEscalamiento
			        fechaHoy,
			        false, //sinGrabarDetalle
			        false,
			        ntrasust,
			        soloCorreosRecibidos,
			        correos
			        );
			
			message = despacho.getMessage();
			success = true;
			logger.debug(Utils.log(
					 "\n###### turnarDesdeComp ######"
					,"\n#############################"
					));
		} catch (Exception ex) {
			message = Utils.manejaExcepcion(ex);
		}
		return SUCCESS;
	}
	
	@Action(value   = "recuperarPolizaUnicaDanios",
			results = { @Result(name="success", type="json") }
			)
	public String recuperarPolizaUnicaDanios()
	{
		logger.debug(Utils.log(
				 "\n########################################"
				,"\n###### recuperarPolizaUnicaDanios ######"
				,"\n###### params=",params
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params, "No se recibieron datos");
			
			String cduniext = params.get("CDUNIEXT")
			       ,ramo     = params.get("RAMO")
			       ,nmpoliex = params.get("NMPOLIEX");
			
			Utils.validate(
					cduniext  , "No se recibi\u00f3n la sucursal"
					,ramo     , "No se recibi\u00f3n el ramo"
					,nmpoliex , "No se recibi\u00f3n la p\u00f3nliza"
					);
			
			params.putAll(flujoMesaControlManager.recuperarPolizaUnicaDanios(cduniext,ramo,nmpoliex));
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### params=",params
					,"\n###### recuperarPolizaUnicaDanios ######"
					,"\n########################################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return SUCCESS;
	}
	
	@Action(value           = "guardarTtipflurol",
			results         = { @Result(name="success", type="json") },
            interceptorRefs = {
			    @InterceptorRef(value = "json", params = {"enableSMD", "true", "ignoreSMDMethodInterfaces", "false" })
			})
	public String guardarTtipflurol()
	{
		logger.debug(Utils.log(
				 "\n###############################"
				,"\n###### guardarTtipflurol ######"
				,"\n###### params = " , params
				,"\n###### list   = " , list
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			if(list==null)
			{
				throw new ApplicationException("No se recibi\u00f3 lista");
			}
			
			String cdtipflu = params.get("cdtipflu");
			
			Utils.validate(cdtipflu , "No se recibi\u00f3 el tr\u00e1mite");
			
			flujoMesaControlManager.guardarTtipflurol(cdtipflu,list);
			
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### message = " , message
				,"\n###### guardarTtipflurol ######"
				,"\n###############################"
				));
		return SUCCESS;
	}
	
	@Action(value           = "guardarTflujorol",
			results         = { @Result(name="success", type="json") },
            interceptorRefs = {
			    @InterceptorRef(value = "json", params = {"enableSMD", "true", "ignoreSMDMethodInterfaces", "false" })
			})
	public String guardarTflujorol()
	{
		logger.debug(Utils.log(
				 "\n##############################"
				,"\n###### guardarTflujorol ######"
				,"\n###### params = " , params
				,"\n###### list   = " , list
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			if(list==null)
			{
				throw new ApplicationException("No se recibi\u00f3 lista");
			}
			
			String cdtipflu    = params.get("cdtipflu")
					,cdflujomc = params.get("cdflujomc");
			
			Utils.validate(
					cdtipflu   , "No se recibi\u00f3 el tr\u00e1mite"
					,cdflujomc , "No se recibi\u00f3 el proceso"
					);
			
			flujoMesaControlManager.guardarTflujorol(cdtipflu,cdflujomc,list);
			
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### message = " , message
				,"\n###### guardarTflujorol ######"
				,"\n##############################"
				));
		return SUCCESS;
	}
	
	@Action(value   = "cargarDatosTitulo",
			results = { @Result(name="success", type="json") }
			)
	public String cargarDatosTitulo()
	{
		logger.debug(Utils.log(
				 "\n###############################"
				,"\n###### cargarDatosTitulo ######"
				,"\n###### params=",params
				));
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			String cdtipflu   = params.get("cdtipflu")
				   ,cdflujomc = params.get("cdflujomc")
				   ,webid     = params.get("webid");
			
			Utils.validate(
					cdtipflu   , "No se recibi\u00f3 el tipo de flujo"
					,cdflujomc , "No se recibi\u00f3 la clave de flujo"
					,webid     , "No se recibi\u00f3 el id web"
					);
			
			Map<String,String> res = flujoMesaControlManager.cargarDatosTitulo(
					cdtipflu
					,cdflujomc
					,webid
					);
			
			params.putAll(res);
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### cargarDatosTitulo ######"
					,"\n###############################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return SUCCESS;
	}
	
	@Action(value   = "guardarDatosTitulo",
			results = { @Result(name="success", type="json") }
			)
	public String guardarDatosTitulo()
	{
		logger.debug(Utils.log(
				 "\n################################"
				,"\n###### guardarDatosTitulo ######"
				,"\n###### params=",params
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			String cdtipflu    = params.get("CDTIPFLU")
			       ,cdflujomc  = params.get("CDFLUJOMC")
			       ,cdtitulo   = params.get("CDTITULO")
			       ,webid      = params.get("WEBID")
			       ,xpos       = params.get("XPOS")
			       ,ypos       = params.get("YPOS")
			       ,dstitulo   = params.get("DSTITULO")
			       ,accion     = params.get("ACCION");
			
			Utils.validate(
					cdtipflu    , "No se recibi\u00f3 el tipo de flujo"
					,cdflujomc  , "No se recibi\u00f3 la clave de flujo"
					,cdtitulo   , "No se recibi\u00f3 la clave de t\u00edtulo"
					,webid      , "No se recibi\u00f3 el id"
					,xpos       , "No se recibi\u00f3 x"
					,ypos       , "No se recibi\u00f3 y"
					,dstitulo   , "No se recibi\u00f3 el nombre"
					,accion     , "No se recibi\u00f3 el tipo de operaci\u00f3n"
					);
			
			flujoMesaControlManager.guardarDatosTitulo(
					cdtipflu
					,cdflujomc
					,cdtitulo
					,webid
					,xpos
					,ypos
					,dstitulo
					,accion
					);
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### guardarDatosTitulo ######"
					,"\n################################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return SUCCESS;
	}
	
	@Action(value   = "modificarDetalleTramiteMC",
			results = { @Result(name="success", type="json") }
			)
	public String modificarDetalleTramiteMC()
	{
		logger.debug(Utils.log(
				 "\n#######################################"
				,"\n###### modificarDetalleTramiteMC ######"
				,"\n###### params = " , params
				));
		
		try
		{
			UserVO usuario = Utils.validateSession(session);
			String cdusuari = usuario.getUser();
			String cdsisrol = usuario.getRolActivo().getClave();
			
			Utils.validate(params , "No se recibieron datos");
			
			String ntramite   = params.get("ntramite")
					,nmordina = params.get("nmordina")
					,comments = params.get("comments");
			
			Utils.validate(
					ntramite  , "No se recibi\u00f3 el tr\u00e1mite"
					,nmordina , "No se recibi\u00f3 el ordinal"
					,comments , "No se recibi\u00f3 el detalle"
					);
			
			params.put("comments", flujoMesaControlManager.modificarDetalleTramiteMC(
					ntramite,
					nmordina,
					comments,
					cdusuari,
					cdsisrol
					));
			
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### message = " , message
				,"\n###### modificarDetalleTramiteMC ######"
				,"\n#######################################"
				));
		
		return SUCCESS;
	}
	
	@Action(value   = "recuperarChecklistInicial",
			results = { @Result(name="success", type="json") }
			)
	public String recuperarChecklistInicial () {
		logger.debug(Utils.log(
				 "\n#######################################"
				,"\n###### recuperarChecklistInicial ######"
				,"\n###### params = " , params));
		try {
			String cdsisrol = Utils.validateSession(session).getRolActivo().getClave();
			Utils.validate(params , "No se recibieron datos");
			String ntramite = params.get("ntramite");
			Utils.validate(ntramite, "Falta ntramite");
			params.putAll(flujoMesaControlManager.recuperarChecklistInicial(ntramite, cdsisrol));
			success = true;
		} catch (Exception ex) {
			message = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
				 "\n###### success = " , success
				,"\n###### message = " , message
				,"\n###### recuperarChecklistInicial ######"
				,"\n#######################################"));
		return SUCCESS;
	}
	
	@Action(value   = "guardarDatosCorreo",
			results = { @Result(name="success", type="json") }
			)
	public String guardarDatosCorreo()
	{
		logger.debug(Utils.log(
				 "\n################################"
				,"\n###### guardarDatosCorreo ######"
				,"\n###### params=",params
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			String cdtipflu    = params.get("CDTIPFLU");
			String cdflujomc   = params.get("CDFLUJOMC");
			String cdmail      = params.get("CDMAIL");
			String dsmail      = params.get("DSMAIL");
			String dsdestino   = params.get("DSDESTINO");
			String dsasunto    = params.get("DSASUNTO");
			String dsmensaje   = params.get("DSMENSAJE");
			String vardestino  = params.get("VARDESTINO");
			String varasunto   = params.get("VARASUNTO");
			String varmensaje  = params.get("VARMENSAJE");
			String webid       = params.get("WEBID");
			String xpos        = params.get("XPOS");
			String ypos        = params.get("YPOS");
			String accion      = params.get("ACCION");
			
			Utils.validate(
					cdtipflu    , "No se recibi\u00f3 el tipo de flujo"
					,cdflujomc  , "No se recibi\u00f3 la clave de flujo"
					,cdmail     , "No se recibi\u00f3 el codigo del mail"
					,dsmail     , "No se recibi\u00f3 la descripcion del mail"
					,dsdestino  , "No se recibi\u00f3 el destinatario"
//					,dsasunto   , "No se recibi\u00f3 el asunto"
					,dsmensaje  , "No se recibi\u00f3 el mensaje"
					,webid      , "No se recibi\u00f3 el id"
					,xpos       , "No se recibi\u00f3 x"
					,ypos       , "No se recibi\u00f3 y"
					,accion     , "No se recibi\u00f3 el tipo de operaci\u00f3n"
					);
			
			flujoMesaControlManager.guardarDatosCorreo(
					cdtipflu,
					cdflujomc,
					cdmail,
					dsmail,
					dsdestino,
					dsasunto,
					dsmensaje,
					vardestino,
					varasunto,
					varmensaje,
					webid,
					xpos,
					ypos,
					accion
					);
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### guardarDatosCorreo ######"
					,"\n################################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return SUCCESS;
	}

	@Action(value   = "cargarDatosCorreo",
			results = { @Result(name="success", type="json") }
			)
	public String cargarDatosCorreo()
	{
		logger.debug(Utils.log(
				 "\n###################################"
				,"\n###### cargarDatosCorreo ######"
				,"\n###### params=",params
				));
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			String cdtipflu   = params.get("cdtipflu")
				   ,cdflujomc = params.get("cdflujomc")
				   ,cdmail    = params.get("cdmail");
			
			Utils.validate(
					cdtipflu   , "No se recibi\u00f3 el tipo de flujo"
					,cdflujomc , "No se recibi\u00f3 la clave de flujo"
					,cdmail    , "No se recibi\u00f3 la clave de mail"
					);
			
			Map<String,String> res = flujoMesaControlManager.cargarDatosCorreo(
					cdtipflu
					,cdflujomc
					,cdmail
					);
			
			params.putAll(res);
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### cargarDatosValidacion ######"
					,"\n###################################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return SUCCESS;
	}
	
	@Action(value   = "enviaCorreoFlujo",
			results = { @Result(name="success", type="json") }
			)
	public String enviaCorreoFlujo()
	{
		logger.debug(Utils.log(
				 "\n###############################"
				,"\n###### enviaCorreoFlujo ######"
				,"\n###### flujo="  , flujo
				,"\n###### params=" , params
				));
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(flujo  , "No se recibieron datos del flujo");
			Utils.validate(params , "No se recibieron par\u00e1metros");						
			
			Map<String, String> res = flujoMesaControlManager.enviaCorreoFlujo(flujo, params);			
			
//			params.put("salida" , flujoMesaControlManager.ejecutaValidacion(
//					flujo
//					));
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### params=" , params
					,"\n###### enviaCorreoFlujo #######"
					,"\n###############################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		return SUCCESS;
	}
	
	@Action(value   = "regresarTramiteVencido",
	        results = { @Result(name="success", type="json") }
	)
	public String regresarTramiteVencido () {
		logger.debug(Utils.log(
			"\n####################################",
			"\n###### regresarTramiteVencido ######",
			"\n###### params = " , params
		));
		try {
			UserVO usuario = Utils.validateSession(session);
			String cdusuari = usuario.getUser();
			String cdsisrol = usuario.getRolActivo().getClave();
			Utils.validate(params , "No se recibieron par\u00e1metros");
			String ntramite = params.get("ntramite");
			Utils.validate(ntramite, "Falta ntramite");
			boolean soloRevisar = "S".equals(params.get("soloRevisar"));
			params.putAll(flujoMesaControlManager.regresarTramiteVencido(ntramite, soloRevisar, cdusuari, cdsisrol));
			success = true;
		} catch (Exception ex) {
			message = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
			"\n###### params  = " , params,
			"\n###### success = " , success,
			"\n###### message = " , message,
			"\n###### regresarTramiteVencido #######",
			"\n#####################################"
		));
		return SUCCESS;
	}
	
	@Action(value   = "movimientoTrequisi",
			results = { @Result(name="success", type="json") }
			)
	public String movimientoTrequisi()
	{
		logger.debug(Utils.log(
				 "\n################################"
				,"\n###### movimientoTrequisi ######"
				,"\n###### params=",params
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			String accion      = params.get("ACCION")
			       ,cdrequisi  = params.get("CDREQUISI")
			       ,dsrequisi  = params.get("DSREQUISI")
			       ,cdtiptra   = params.get("CDTIPTRA")
			       ,swpidedato = params.get("SWPIDEDATO");
			
			Utils.validate(
					accion     , "No se recibi\u00f3 la acci\u00f3n"
					,dsrequisi , "No se recibi\u00f3 el nombre"
					,cdtiptra  , "No se recibi\u00f3 el tipo de tr\u00e1mite"
					);
			
			flujoMesaControlManager.movimientoTrequisi(
					accion
					,cdrequisi
					,dsrequisi
					,cdtiptra
					,"S".equals(swpidedato)
					);
			
			success = true;
			
			logger.debug(Utils.log(
					 "\n###### movimientoTrequisi ######"
					,"\n################################"
					));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		return SUCCESS;
	}
	
	@Action(value   = "marcarRequisitoRevision",
			results = { @Result(name="success", type="json") }
			)
	public String marcarRequisitoRevision () {
		logger.debug(Utils.log(
				"\n#####################################",
				"\n###### marcarRequisitoRevision ######",
				"\n###### params = ", params));
		try {
			UserVO usuario = Utils.validateSession(session);
			Utils.validate(params , "No se recibieron datos");
			String cdtipflu  = params.get("cdtipflu"),
				   cdflujomc = params.get("cdflujomc"),
				   ntramite  = params.get("ntramite"),
				   cdrequisi = params.get("cdrequisi"),
				   swactivo  = params.get("swactivo"),
				   dsdato    = params.get("dsdato");
			Utils.validate(
					cdtipflu  , "Falta cdtipflu",
					cdflujomc , "Falta cdflujomc",
					ntramite  , "Falta ntramite",
					cdrequisi , "Falta cdrequisi",
					swactivo  , "Falta swactivo");
			flujoMesaControlManager.marcarRequisitoRevision(
					cdtipflu,
					cdflujomc,
					ntramite,
					cdrequisi,
					"S".equals(swactivo),
					dsdato,
					usuario.getUser(),
					usuario.getRolActivo().getClave());
			success = true;
		} catch (Exception ex) {
			message = Utils.manejaExcepcion(ex);
		}		
		logger.debug(Utils.log(
				"\n###### success = " , success,
				"\n###### message = " , message,
				"\n###### marcarRequisitoRevision ######",
				"\n#####################################"));
		return SUCCESS;
	}
	
	@Action(value   = "marcarRevisionConfirmada",
			results = { @Result(name="success", type="json") }
			)
	public String marcarRevisionConfirmada () {
		logger.debug(Utils.log(
				"\n######################################",
				"\n###### marcarRevisionConfirmada ######",
				"\n###### params = ", params));
		try {
			UserVO usuario = Utils.validateSession(session);
			Utils.validate(params , "No se recibieron datos");
			String cdtipflu  = params.get("cdtipflu"),
				   cdflujomc = params.get("cdflujomc"),
				   ntramite  = params.get("ntramite"),
				   cdrevisi  = params.get("cdrevisi"),
				   swconfirm = params.get("swconfirm");
			Utils.validate(
					cdtipflu  , "Falta cdtipflu",
					cdflujomc , "Falta cdflujomc",
					ntramite  , "Falta ntramite",
					cdrevisi  , "Falta cdrevisi",
					swconfirm , "Falta swconfirm");
			flujoMesaControlManager.marcarRevisionConfirmada(
					cdtipflu,
					cdflujomc,
					ntramite,
					cdrevisi,
					"S".equals(swconfirm),
					usuario.getUser(),
					usuario.getRolActivo().getClave());
			success = true;
		} catch (Exception ex) {
			message = Utils.manejaExcepcion(ex);
		}		
		logger.debug(Utils.log(
				"\n###### success = " , success,
				"\n###### message = " , message,
				"\n###### marcarRevisionConfirmada ######",
				"\n######################################"));
		return SUCCESS;
	}
	
	@Action(value   = "actualizaStatusMesaControl",
			results = { @Result(name="success", type="json") }
			)
	public String actualizaStatusMesaControl () {
		logger.debug(Utils.log(
				"\n########################################",
				"\n###### actualizaStatusMesaControl ######",
				"\n###### flujo = ", flujo));
		try {
			Utils.validateSession(session);
			Utils.validate(flujo, "No hay flujo");
			String ntramite = flujo.getNtramite(),
			       status   = flujo.getAux();
			Utils.validate(ntramite, "No hay ntramite",
				status, "No hay status");
			flujoMesaControlManager.actualizaStatusMesaControl(ntramite, status);
			success = true;
		} catch (Exception ex) {
			message = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
				"\n###### success = ", success,
				"\n###### message = ", message,
				"\n###### actualizaStatusMesaControl ######",
				"\n########################################"));
		return SUCCESS;
	}
	
	@Action(value   = "recuperarCotiColec",
			results = { @Result(name="success", type="json") }
			)
	public String recuperarCotiColec () {
		logger.debug(Utils.log(
				"\n################################",
				"\n###### recuperarCotiColec ######",
				"\n###### params = ", params));
		try {
			UserVO usuario = Utils.validateSession(session);
			Utils.validate(params, "No hay datos");
			String ntramite = params.get("ntramite"),
			       nmsolici = params.get("nmsolici"),
			       status   = params.get("status");
			Utils.validate(ntramite , "Falta ntramite",
					       nmsolici , "Falta nmsolici",
					       status   , "Falta status");
			flujoMesaControlManager.recuperarCotiColec(usuario.getUser(), usuario.getRolActivo().getClave(), ntramite, nmsolici, status);
			success = true;
		} catch (Exception ex) {
			message = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
				"\n###### success = ", success,
				"\n###### message = ", message,
				"\n###### recuperarCotiColec ######",
				"\n################################"));
		return SUCCESS;
	}
	
	@Action(value   = "guardarVentanaDatosTramite",
            results = { @Result(name="success", type="json") }
            )
	public String guardarVentanaDatosTramite () {
	    logger.debug(Utils.log(
	            "\n########################################",
	            "\n###### guardarVentanaDatosTramite ######",
	            "\n###### params = ", params));
	    try {
	        Utils.validateSession(session);
	        Utils.validate(params, "No se recibieron datos de ventana de datos de tr\u00e1mite");
	        String ntramite = params.get("ntramite");
	        Utils.validate(ntramite, "Falta ntramite");
	        flujoMesaControlManager.guardarVentanaDatosTramite(ntramite, params);
	        success = true;
	    } catch (Exception ex) {
	        message = Utils.manejaExcepcion(ex);
	    }
        logger.debug(Utils.log(
                "\n###### success = " , success,
                "\n###### message = " , message,
                "\n###### guardarVentanaDatosTramite ######",
                "\n########################################"));
	    return SUCCESS;
	}
	
	@Action(value   = "guardarAuxiliarFlujo",
            results = { @Result(name="success", type="json") }
            )
	public String guardarAuxiliarFlujo () {
	    logger.debug(Utils.log("\n##################################",
	                           "\n###### guardarAuxiliarFlujo ######",
	                           "\n###### flujo = ", flujo));
	    try {
	        Utils.validateSession(session);
	        Utils.validate(flujo, "Faltan los datos del flujo");
	        Utils.validate(flujo.getAux(), "Falta auxiliar");
	        flujoMesaControlManager.guardarAuxiliarFlujo(flujo.getNtramite(), flujo.getAux());
	        success = true;
	    } catch (Exception ex) {
	        message = Utils.manejaExcepcion(ex);
	    }
        logger.debug(Utils.log("\n###### success = " , success,
                               "\n###### message = " , message,
                               "\n###### guardarAuxiliarFlujo ######",
                               "\n##################################"));
	    return SUCCESS;
	}
    
    @Action(value   = "pruebaGuardarObjeto",
            results = { @Result(name="success", type="json") }
            )
    public String pruebaGuardarObjeto () {
        logger.debug(Utils.log("\n#################################",
                               "\n###### pruebaGuardarObjeto ######",
                               "\n###### flujo = ", flujo));
        try {
            flujoMesaControlManager.pruebaGuardarObjeto();
            success = true;
        } catch (Exception ex) {
            message = Utils.manejaExcepcion(ex);
        }
        logger.debug(Utils.log("\n###### success = " , success,
                               "\n###### message = " , message,
                               "\n###### pruebaGuardarObjeto ######",
                               "\n#################################"));
        return SUCCESS;
    }
	
	@Action(value   = "pruebaGuardarLista",
            results = { @Result(name="success", type="json") }
            )
    public String pruebaGuardarLista () {
        logger.debug(Utils.log("\n################################",
                               "\n###### pruebaGuardarLista ######",
                               "\n###### flujo = ", flujo));
        try {
            flujoMesaControlManager.pruebaGuardarLista();
            success = true;
        } catch (Exception ex) {
            message = Utils.manejaExcepcion(ex);
        }
        logger.debug(Utils.log("\n###### success = " , success,
                               "\n###### message = " , message,
                               "\n###### pruebaGuardarLista ######",
                               "\n################################"));
        return SUCCESS;
    }
	
	@Action(value   = "cambiarTipoEndosoTramite",
            results = { @Result(name="success", type="json") }
            )
	public String cambiarTipoEndosoTramite () {
	    logger.debug("{}", Utils.log("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
	                                 "\n@@@@@@ cambiarTipoEndosoTramite @@@@@@",
	                                 "\n@@@@@@ params = ", params));
	    try {
	        UserVO usuario = Utils.validateSession(session);
	        Utils.validate(params, "No se recibieron datos para cambiar motivo de endoso");
	        String ntramite = params.get("NTRAMITE"),
	               cdtipsup = params.get("CDTIPSUP"),
	               comments = params.get("COMMENTS"),
	               swagente = params.get("SWAGENTE"),
	               status   = params.get("STATUS");
	        Utils.validate(ntramite , "Falta ntramite",
	                       cdtipsup , "Falta cdtipsup",
	                       status   , "Falta status");
	        flujoMesaControlManager.cambiarTipoEndosoTramite(ntramite, status, cdtipsup, comments, "S".equals(swagente),
	                usuario.getUser(), usuario.getRolActivo().getClave());
	        success = true;
	    } catch (Exception ex) {
	        message = Utils.manejaExcepcion(ex);
	    }
        logger.debug("{}", Utils.log("\n@@@@@@ success = ", success,
                                     "\n@@@@@@ message = ", message,
                                     "\n@@@@@@ cambiarTipoEndosoTramite @@@@@@",
                                     "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"));
	    return SUCCESS;
	}
	
	////////////////////////////////////////////////////////
	// GETTERS Y SETTERS                                  //
	                                                      //
	public boolean isSuccess() {                          //
		return success;                                   //
	}                                                     //
                                                          //
	public void setSuccess(boolean success) {             //
		this.success = success;                           //
	}                                                     //
                                                          //
	public String getMessage() {                          //
		return message;                                   //
	}                                                     //
                                                          //
	public void setMessage(String message) {              //
		this.message = message;                           //
	}                                                     //
                                                          //
	public Map<String, Item> getItems() {                 //
		return items;                                     //
	}                                                     //
                                                          //
	public void setItems(Map<String, Item> items) {       //
		this.items = items;                               //
	}                                                     //
                                                          //
	public FlujoVO getFlujo() {                           //
		return flujo;                                     //
	}                                                     //
	                                                      //
	public void setFlujo(FlujoVO flujo) {                 //
		this.flujo = flujo;                               //
	}                                                     //
	                                                      //
	public Map<String, String> getParams() {              //
		return params;                                    //
	}                                                     //
	                                                      //
	public void setParams(Map<String, String> params) {   //
		this.params = params;                             //
	}                                                     //
	                                                      //
	public List<Map<String, String>> getList() {          //
		return list;                                      //
	}                                                     //
	                                                      //
	public void setList(List<Map<String, String>> list) { //
		this.list = list;                                 //
	}                                                     //
                                                          //
	public int getStart() {                               //
		return start;                                     //
	}                                                     //
                                                          //
	public void setStart(int start) {                     //
		this.start = start;                               //
	}                                                     //
                                                          //
	public int getLimit() {                               //
		return limit;                                     //
	}                                                     //
                                                          //
	public void setLimit(int limit) {                     //
		this.limit = limit;                               //
	}                                                     //
                                                          //
	public int getTotal() {                               //
		return total;                                     //
	}                                                     //
                                                          //
	public void setTotal(int total) {                     //
		this.total = total;                               //
	}                                                     //
                                                          //
	public Map<String, Object> getDatosTramite() {        //
		return datosTramite;                              //
	}                                                     //
                                                          //
	public void setDatosTramite(Map<String, Object>       //
	                            datosTramite) {           //
		this.datosTramite = datosTramite;                 //
	}                                                     //
                                                          //
    ////////////////////////////////////////////////////////
	
}