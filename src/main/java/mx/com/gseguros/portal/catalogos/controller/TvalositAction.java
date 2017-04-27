package mx.com.gseguros.portal.catalogos.controller;

import java.util.List;
import java.util.Map;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.portal.catalogos.service.TvalositManager;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.utils.Utils;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
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
@Namespace("/tvalosit")
public class TvalositAction extends PrincipalCoreAction
{
	private static Logger logger = LoggerFactory.getLogger(TvalositAction.class);
	
	private boolean success;
	
	private String message;
	
	private Map<String,String> params;
	
	private List<Map<String,String>> list;
	
	private Map<String,Item> items;
	
	@Autowired
	private TvalositManager tvalositManager;
	
	@Actions({
			@Action(value   = "pantallaActTvalosit",
		            results = {
		            @Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
		            @Result(name="success" , location="/jsp-script/catalogos/pantallaActTvalosit.jsp")
		        }),
		    @Action(value   = "includes/pantallaActTvalosit",
				    results = {
			        @Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
			        @Result(name="success" , location="/jsp-script/catalogos/pantallaActTvalosit.jsp")
			})
	})
	public String pantallaActTvalosit()
	{
		logger.debug(Utils.log(""
				,"\n#################################"
				,"\n###### pantallaActTvalosit ######"
				,"\n###### params=",params
				));
		
		String result = ERROR;
		
		try
		{
			UserVO usuario = Utils.validateSession(session);
			
			Utils.validate(params, "No se recibieron datos");
			
			String origen    = params.get("origen")
			       ,cdunieco = params.get("cdunieco")
			       ,cdramo   = params.get("cdramo")
			       ,estado   = params.get("estado")
			       ,nmpoliza = params.get("nmpoliza")
			       ,nmsituac = params.get("nmsituac")
			       ,cdtipsit = params.get("cdtipsit");
			
			Utils.validate(
					origen    , "No se recibi\u00f3 el origen"
					,cdunieco , "No se recibi\u00f3 la sucursal"
					,cdramo   , "No se recibi\u00f3 el producto"
					,estado   , "No se recibi\u00f3 el estado"
					,nmpoliza , "No se recibi\u00f3 la p\u00f3liza"
					,nmsituac , "No se recibi\u00f3 la situaci\u00f3n"
					,cdtipsit , "No se recibi\u00f3 la modalidad"
					);
			
			items = tvalositManager.pantallaActTvalosit(
					origen
					,usuario.getRolActivo().getClave()
					,cdtipsit
					,ServletActionContext.getServletContext().getServletContextName()
					);
			
			result = SUCCESS;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(""
				,"\n###### result=",result
				,"\n###### pantallaActTvalosit ######"
				,"\n#################################"
				));
		return result;
	}
	
	@Action(value   = "cargarPantallaActTvalosit",
			results = { @Result(name="success", type="json") }
			)
	public String cargarPantallaActTvalosit()
	{
		logger.debug(Utils.log(""
				,"\n#######################################"
				,"\n###### cargarPantallaActTvalosit ######"
				,"\n###### params=", params
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params, "No se recibieron datos");
			
			String cdunieco  = params.get("cdunieco")
			       ,cdramo   = params.get("cdramo")
			       ,estado   = params.get("estado")
			       ,nmpoliza = params.get("nmpoliza")
			       ,nmsituac = params.get("nmsituac");
			
			Utils.validate(
					cdunieco  , "No se recibi\u00f3 la sucursal"
					,cdramo   , "No se recibi\u00f3 el producto"
					,estado   , "No se recibi\u00f3 el estado"
					,nmpoliza , "No se recibi\u00f3 la p\u00f3liza"
					,nmsituac , "No se recibi\u00f3 la situaci\u00f3n"
					);
			
			params.putAll(tvalositManager.cargarPantallaActTvalosit(cdunieco,cdramo,estado,nmpoliza,nmsituac));
			
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(""
				,"\n###### cargarPantallaActTvalosit ######"
				,"\n#######################################"
				));
		
		return SUCCESS;
	}
	
	@Action(value   = "guardarPantallaActTvalosit",
			results = { @Result(name="success", type="json") }
			)
	public String guardarPantallaActTvalosit()
	{
		logger.debug(Utils.log(""
				,"\n########################################"
				,"\n###### guardarPantallaActTvalosit ######"
				,"\n###### params=", params
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params, "No se recibieron datos");
			
			String cdunieco  = params.get("cdunieco")
			       ,cdramo   = params.get("cdramo")
			       ,estado   = params.get("estado")
			       ,nmpoliza = params.get("nmpoliza")
			       ,nmsituac = params.get("nmsituac");
			
			Utils.validate(
					cdunieco  , "No se recibi\u00f3 la sucursal"
					,cdramo   , "No se recibi\u00f3 el producto"
					,estado   , "No se recibi\u00f3 el estado"
					,nmpoliza , "No se recibi\u00f3 la p\u00f3liza"
					,nmsituac , "No se recibi\u00f3 la situaci\u00f3n"
					);
			
			tvalositManager.guardarPantallaActTvalosit(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsituac
					,"0"
					,params
					);
			
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(""
				,"\n###### guardarPantallaActTvalosit ######"
				,"\n########################################"
				));
		
		return SUCCESS;
	}
	
	/*
	 * 
	 * 
	 * 
	 * 
	 * GETTERS y SETTERS
	 * 
	 * 
	 * 
	 * 
	 */
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
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

	public List<Map<String, String>> getList() {
		return list;
	}

	public void setList(List<Map<String, String>> list) {
		this.list = list;
	}

	public Map<String, Item> getItems() {
		return items;
	}

	public void setItems(Map<String, Item> items) {
		this.items = items;
	}
	
}