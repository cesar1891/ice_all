package mx.com.gseguros.confpantallas.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.confpantallas.delegate.CargaPanelesManager;
import mx.com.gseguros.confpantallas.model.DinamicData;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class PintaPanelAction extends ActionSupport {
	
	private static final long serialVersionUID = -8373134780577920608L;

	private Logger logger = Logger.getLogger(PintaPanelAction.class);
	
	private transient CargaPanelesManager cargaPanelesManager;
	
	private String nombrepanel;
	
	private String tarea;

	public String execute() {
		ActionContext contexto = ActionContext.getContext();
		Map<String, Object> sesion = contexto.getSession();
		if(getTarea().equals("pintaPanel")){
		    HashMap<String, Object> data = cargaPanelesManager.GeneraJson(getNombrepanel());
		    sesion.put("datoSesion", data.get("lista"));
			List<DinamicData> lt = cargaPanelesManager.GetListaTablas(
					(List<String>) data.get("listaCmb"),
					(List<String>) data.get("listaCmbHijo"));
		    //if(lt.size() > 0){
		    	sesion.put("listaCatalogosEA", lt);
		    //}
            //sesion.put("datoSesion", adm.GetPanelesLista(getNombrepanel()));
		}
		return SUCCESS;
	}
	
	
	//Getters and setters:
	
	public void setCargaPanelesManager(CargaPanelesManager cargaPanelesManager) {
		this.cargaPanelesManager = cargaPanelesManager;
	}
	
	public String getNombrepanel() {
		return nombrepanel;
	}

	public void setNombrepanel(String nombrepanel) {
		this.nombrepanel = nombrepanel;
	}
	
	public String getTarea() {
		return tarea;
	}
	
	public void setTarea(String tarea) {
		this.tarea = tarea;
	}
	
}