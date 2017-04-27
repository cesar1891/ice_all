package mx.com.gseguros.portal.general.controller;

import java.util.List;
import java.util.Map;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.general.service.JaimeErickManager;
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
@Namespace("/erickjaime")
public class JaimeErickAction extends PrincipalCoreAction
{
	private Logger logger = LoggerFactory.getLogger(JaimeErickAction.class);
	
	private boolean success;
	
	private String message;
	
	private Map<String,String> params;
	
	private Map<String,Item> items;
	
	private List<Map<String,String>> list;
	
	@Autowired
	private JaimeErickManager jaimeErickManager;
	
	@Action(value   = "jspErickJaime",
		    results = {
		        @Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
		        @Result(name="success" , location="/jsp-script/proceso/demo/jspErickJaime.jsp")
		    })
	public String jspErickJaime()
	{
		logger.debug(Utils.log(
				 "\n###########################"
				,"\n###### jspErickJaime ######"
				));
		
		String result = ERROR;
		
		try
		{
			items = jaimeErickManager.recuperarElementosPantalla();
			
			result = SUCCESS;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### result=" , result
				,"\n###### jspErickJaime ######"
				,"\n###########################"
				));
		return result;
	}
	
	@Action(value   = "guardarEnBase",
			results = { @Result(name="success", type="json") }
			)
	public String guardarEnBase()
	{
		logger.debug(Utils.log(
				 "\n###########################"
				,"\n###### guardarEnBase ######"
				));
		
		try
		{
			Utils.validate(params,"No se recibieron datos");
			
			String nombre = params.get("nombre");
			String edad   = params.get("edad");
			String fecha  = params.get("fenacimi");
			
			jaimeErickManager.guardarEnBase(nombre,edad,fecha);
			
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success=" , success
				,"\n###### guardarEnBase ######"
				,"\n###########################"
				));
		return SUCCESS;
	}

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

	public Map<String, Item> getItems() {
		return items;
	}

	public void setItems(Map<String, Item> items) {
		this.items = items;
	}

	public List<Map<String, String>> getList() {
		return list;
	}

	public void setList(List<Map<String, String>> list) {
		this.list = list;
	}
}