package mx.com.gseguros.confpantallas.action;

import java.util.List;

import mx.com.gseguros.confpantallas.delegate.AdminAbrePanelDelegate;
import mx.com.gseguros.confpantallas.model.DinamicOpenPanelVO;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;


public class AbrePanelAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(AbrePanelAction.class);
	
	private String nombrepanel;
	private String tarea;
	private List<DinamicOpenPanelVO> regreso;
	
	public String execute() {
		logger.debug("Entro a mi ACTION AbrePanel ll");
		if(getTarea().equals("existe")){
			logger.info(":::BUscando Panel Solicitado::: ");
			AdminAbrePanelDelegate adm = new AdminAbrePanelDelegate();
			setRegreso(adm.ExistePanel(getNombrepanel()));
		}
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

	public List<DinamicOpenPanelVO> getRegreso() {
		return regreso;
	}

	public void setRegreso(List<DinamicOpenPanelVO> regreso) {
		this.regreso = regreso;
	}


}
