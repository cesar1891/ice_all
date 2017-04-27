package mx.com.gseguros.portal.cotizacion.controller;

import java.util.Map;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.gseguros.mesacontrol.model.FlujoVO;
import mx.com.gseguros.portal.mesacontrol.service.MesaControlManager;
import mx.com.gseguros.utils.Utils;

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
@Namespace("/mesacontrol")
public class MesaControl2Action extends PrincipalCoreAction
{
	private final static Logger logger = LoggerFactory.getLogger(MesaControl2Action.class);
	
	private String message;
	
	private boolean success;
	
	private Map<String,String> params;
	
	private FlujoVO flujo;
	
	@Autowired
	private MesaControlManager mesaControlManager;
	
	@Action(value   = "marcarTramiteVistaPrevia",
			results = { @Result(name="success", type="json") }
			)
	public String marcarTramiteVistaPrevia()
	{
		long stamp = System.currentTimeMillis();
		
		logger.debug(Utils.log(
				 "\n######################################"
				,"\n###### marcarTramiteVistaPrevia ######"
				,"\n###### params=" , params
				,"\n###### stamp="  , stamp
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params, "No se recibieron datos");
			
			String ntramite = params.get("ntramite");
			
			Utils.validate(ntramite, "No se recibi\u00f3 el tr\u00e1mite");
			
			mesaControlManager.marcarTramiteVistaPrevia(ntramite);
			
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success=" , success
				,"\n###### message=" , message
				,"\n###### stamp="   , stamp
				,"\n###### marcarTramiteVistaPrevia ######"
				,"\n######################################"
				));
		
		return SUCCESS;
	}
	
	@Action(value   = "borrarNmsoliciTramite",
			results = { @Result(name="success", type="json") }
			)
	public String borrarNmsoliciTramite()
	{
		logger.debug(Utils.log(
				 "\n###################################"
				,"\n###### borrarNmsoliciTramite ######"
				,"\n###### flujo=" , flujo
				));
		
		try
		{
			Utils.validateSession(session);

			Utils.validate(flujo, "No hay flujo");
			
			mesaControlManager.borrarNmsoliciTramite(flujo.getNtramite());
			
			message = "Operaci\u00f3n sobre solicitud correcta";
			
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success=" , success
				,"\n###### message=" , message
				,"\n###### borrarNmsoliciTramite ######"
				,"\n###################################"
				));
		
		return SUCCESS;
	}

	////// Getters y Setters //////////////////////////////////////////////////////////////////
	
	public String getMessage() {
		return message;
	}

	////// Getters y Setters //////////////////////////////////////////////////////////////////
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	////// Getters y Setters //////////////////////////////////////////////////////////////////

	public boolean isSuccess() {
		return success;
	}
	
	////// Getters y Setters //////////////////////////////////////////////////////////////////

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	////// Getters y Setters //////////////////////////////////////////////////////////////////

	public Map<String, String> getParams() {
		return params;
	}
	
	////// Getters y Setters //////////////////////////////////////////////////////////////////

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	
	////// Getters y Setters //////////////////////////////////////////////////////////////////

	public FlujoVO getFlujo() {
		return flujo;
	}
	
	////// Getters y Setters //////////////////////////////////////////////////////////////////

	public void setFlujo(FlujoVO flujo) {
		this.flujo = flujo;
	}
	
	////// Getters y Setters //////////////////////////////////////////////////////////////////
	
}