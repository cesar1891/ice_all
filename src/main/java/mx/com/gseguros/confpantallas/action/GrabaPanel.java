package mx.com.gseguros.confpantallas.action;

import java.util.HashMap;

import mx.com.gseguros.confpantallas.delegate.AdminPanelesDelegate;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

public class GrabaPanel extends ActionSupport {
	
	private static final long serialVersionUID = -5458409876380834872L;

	private Logger logger = Logger.getLogger(GrabaPanel.class);
	
	private String nombrepanel;
	private String tarea;
	private String regreso;
	private String json;
	
	//TODO:es temporal, cambiar logica
	//private boolean success;
	private boolean existe;
	private String panel;
	
	 public String execute() {
		 logger.debug("Ya ingrese a mi action :::::");
		 AdminPanelesDelegate adm = new AdminPanelesDelegate();
		 if(tarea.equals("existe")){
			 existe = adm.ExistePanel(nombrepanel);
			 if(existe){
				 setRegreso("ok");
			 }else{
				 setRegreso("");
			 }
		 }else if(tarea.equals("graba")){
			 HashMap<String, String> txt = adm.SetPanel(nombrepanel, json); 
			 setPanel(txt.get("panel"));
			 setRegreso(txt.get("rgs"));
		 }
		 //success = true;
		 //return "SUCCESS";
		 return SUCCESS;
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

	public String getRegreso() {
		return regreso;
	}

	public void setRegreso(String regreso) {
		this.regreso = regreso;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	
	
/*	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}*/
	
	public boolean isExiste() {
		return existe;
	}

	public void setExiste(boolean existe) {
		this.existe = existe;
	}

	public String getPanel() {
		return panel;
	}

	public void setPanel(String panel) {
		this.panel = panel;
	}
	
}