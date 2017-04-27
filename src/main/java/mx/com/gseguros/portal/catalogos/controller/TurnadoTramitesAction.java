package mx.com.gseguros.portal.catalogos.controller;

import java.util.List;
import java.util.Map;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.portal.cotizacion.model.Item;
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
public class TurnadoTramitesAction extends PrincipalCoreAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4621627159825334070L;

	private static final Logger logger = LoggerFactory.getLogger(TurnadoTramitesAction.class);
	
	private boolean                  success;
	private String                   message;
	private Map<String,String>       params;
	private List<Map<String,String>> list;
	private Map<String,Item>         items;
	
	@Autowired
	private MesaControlManager mesaControlManager;
	
	@Action(value   = "pantallaExclusionTurnados",
		    results = {
		        @Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
		        @Result(name="success" , location="/jsp-script/catalogos/pantallaExclusionTurnado.jsp")
		    })
	public String pantallaExclusionTurnados() 
	{
		logger.debug(Utils.log(
				 "\n##############INICIO###################"
				,"\n###### pantallaExclusionTurnados ######"
				,"\n###### params=",params
				));
		
		String success = ERROR;
		
		try
		{
			UserVO usuario = Utils.validateSession(session);
			items = mesaControlManager.pantallaExclusionTurnados(usuario.getRolActivo().getClave());
			
			success = SUCCESS;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				"\n###### params=",params
				,"\n###### pantallaExclusionTurnados ######"
				,"\n##############FINAL####################"
				));
	
		return success;
	}

	@Action(value   = "exclusionTurnados",
			results = { @Result(name="success", type="json") }
		)
	public String exclusionTurnados()
	{
		logger.debug(Utils.log(
				 "\n##############INICIO###################"
				,"\n###### ExclusionTurnados ######"
				,"\n###### params=",params
				));
		try
		{
			Utils.validate(params                 , "No se recibieron datos");
			String usuario = params.get("usuario");
			String accion = params.get("accion");
			Utils.validate(usuario                 , "No se recibio usuario");
			Utils.validate(accion                 , "No se recibio accion");
			mesaControlManager.movimientoExclusionUsuario(usuario, accion);
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
				"\n###### params=",params
				,"\n###### ExclusionTurnados ######"
				,"\n##############FINAL####################"
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
