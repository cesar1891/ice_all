package mx.com.gseguros.portal.general.controller;

import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import mx.com.aon.core.web.PrincipalCoreAction;

@Controller
@Scope("prototype")
@ParentPackage(value="default")
@Namespace("/indicadores")
public class IndicadoresAction extends PrincipalCoreAction{
	
	private static final long serialVersionUID = 5937028140085297466L;

	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(IndicadoresAction.class);
	
	private boolean success;
	
	private Map<String,String> params;

	private List<Map<String,String>> list;
	
	private String respuesta;

	
	@Action(value="principal",
	    results = {
	        @Result(name="error", location="/jsp-script/general/errorPantalla.jsp"),
	        @Result(name="success", location="/jsp-script/proceso/indicadores/indicadores.jsp")
	    }
	)
	public String execute() throws Exception {
		return SUCCESS;
	}

	
	// Getters and setters:
	
	public String getRespuesta() {
		return respuesta;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
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

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	
}
