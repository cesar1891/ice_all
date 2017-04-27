package mx.com.gseguros.confpantallas.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.confpantallas.delegate.AdminCargaPanelesDelegate;
import mx.com.gseguros.confpantallas.model.DinamicData;
import mx.com.gseguros.confpantallas.model.ViewBean;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class PintaJson extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String nombrepanel;
	private String tarea;
	
	public String execute() {
		ActionContext contexto = ActionContext.getContext();
		Map<String, Object> sesion = contexto.getSession();
		 AdminCargaPanelesDelegate adm = new AdminCargaPanelesDelegate();
		 if(getTarea().equals("pintaJson")){
			 HashMap<String, Object> data = adm.GeneraJson(getNombrepanel());
			 List<ViewBean> listadePaneles = (List<ViewBean>) data.get("lista");
			 ViewBean pnl = new ViewBean();
			 pnl = listadePaneles.get(0);
			 //este es el codigo Extjs
			 System.out.println("este es el codigo Extjs");
			 System.out.println(pnl.getCodigo());
			
			 
			 //lo subo a session para manipularlo en otra ventana 
			 sesion.put("datoSesion", data.get("lista"));
			 List<DinamicData> lt = adm.GetListaTablas((List<String>) data.get("listaCmb"),(List<String>) data.get("listaCmbHijo"));
			 if(lt.size() > 0){
				 sesion.put("listaCatalogosEA", lt);
			 }
			 //sesion.put("datoSesion", adm.GetPanelesLista(getNombrepanel()));
		 }
		 return "SUCCESS";
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
