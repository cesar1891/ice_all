package mx.com.gseguros.portal.emision.controller;

import java.util.Map;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.mesacontrol.model.FlujoVO;
import mx.com.gseguros.portal.emision.service.ClienteTramiteManager;
import mx.com.gseguros.utils.Utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
@ParentPackage(value="default")
@Namespace("/emision")
public class ClienteTramiteAction extends PrincipalCoreAction
{
	private final static Logger logger = LoggerFactory.getLogger(ClienteTramiteAction.class);
	
	private Map<String,String> params;
	
	private boolean success;
	
	private FlujoVO flujo;
	
	private String message;
	
	@Autowired
	private ClienteTramiteManager clienteTramiteManager;
	
	@Action(value   = "contratanteTramite",
		    results = {
		        @Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
		        @Result(name="success" , location="/jsp-script/proceso/emision/pantallaClienteTramite.jsp")
		    })
	public String contratanteTramite()
	{
		logger.debug(Utils.log(
				 "\n################################"
				,"\n###### contratanteTramite ######"
				,"\n###### flujo=" , flujo
				));
		
		String result = ERROR;
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(flujo, "No se recibieron datos del tr\u00e1mite");
			
			params.putAll(clienteTramiteManager.pantallaContratanteTramite(flujo.getNtramite())); 
			
			result = SUCCESS;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### result=" , result
				,"\n###### contratanteTramite ######"
				,"\n################################"
				));
		return result;
	}
	
	@Action(value   = "movimientoClienteTramite",
			results = { @Result(name="success", type="json") }
			)
	public String movimientoClienteTramite()
	{
		logger.debug(Utils.log(
				 "\n######################################"
				,"\n###### movimientoClienteTramite ######"
				,"\n###### params=", params
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params, "No se recibieron datos");
			
			String ntramite  = params.get("ntramite")
			       ,cdperson = params.get("cdperson")
			       ,accion   = params.get("accion");
			
			Utils.validate(
					ntramite , "No se recibi\u00f3 el tr\u00e1mite"
					,accion  , "No se recibi\u00f3 la acci\u00f3n");
			
			if(("I".equals(accion)||"U".equals(accion)) && StringUtils.isBlank(cdperson))
			{
				throw new ApplicationException("Falta la clave de cliente");
			}
			
			clienteTramiteManager.movimientoClienteTramite(ntramite,cdperson,accion);
			
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success=", success				 
				,"\n###### message=", message
				,"\n###### movimientoClienteTramite ######"
				,"\n######################################"
				));
		return SUCCESS;
	}
	
	@Action(value   = "recuperarNmsoliciTramite",
			results = { @Result(name="success", type="json") }
			)
	public String recuperarNmsoliciTramite()
	{
		logger.debug(Utils.log(
				 "\n######################################"
				,"\n###### recuperarNmsoliciTramite ######"
				,"\n###### params=", params
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params, "No se recibieron datos");
			
			String ntramite  = params.get("ntramite");
			
			Utils.validate(ntramite, "No se recibi\u00f3 el tr\u00e1mite");
			
			params.put("nmsolici", clienteTramiteManager.recuperarNmsoliciTramite(ntramite));
			
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success=", success				 
				,"\n###### message=", message
				,"\n###### recuperarNmsoliciTramite ######"
				,"\n######################################"
				));
		return SUCCESS;
	}
	
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	//////////////////////                   //////////////////////
	////////////////////// GETTERS Y SETTERS //////////////////////
	//////////////////////                   //////////////////////
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
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
}