package mx.com.gseguros.confpantallas.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.confpantallas.delegate.AdminCargaPanelesDelegate;
import mx.com.gseguros.confpantallas.model.DinamicData;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Does some thing in old style.
 *
 * @deprecated use {@link PintaPanelAction} instead.  
 */
@Deprecated
public class PintaPanel extends ActionSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nombrepanel;
	private String tarea;

	public String execute() {
		ActionContext contexto = ActionContext.getContext();
		Map<String, Object> sesion = contexto.getSession();
		 AdminCargaPanelesDelegate adm = new AdminCargaPanelesDelegate();
		 if(getTarea().equals("pintaPanel")){
			 HashMap<String, Object> data = adm.GeneraJson(getNombrepanel());
			 
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
