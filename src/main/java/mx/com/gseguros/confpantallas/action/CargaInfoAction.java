package mx.com.gseguros.confpantallas.action;

import java.util.List;

import mx.com.gseguros.confpantallas.delegate.CargaPanelesManager;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;


public class CargaInfoAction extends ActionSupport {
	
	private static final long serialVersionUID = 5160265967876309131L;
	
	private Logger log= Logger.getLogger(CargaInfoAction.class);
	
	private transient CargaPanelesManager cargaPanelesManager; 
	
	private String tarea;
	
	private List<?> success;
	
	private String valor;
	
	private String tabla;
	
	
	
	public String execute() {
		log.debug("Inicio de CargaInfo");
		if("llenaComboPaneles".equals(tarea)) {
			success = cargaPanelesManager.GetListadePaneles();
		}else if("llenaCombo".equals(tarea)) {
			success = cargaPanelesManager.getDataCombo(tabla, valor);
		}else if("llenaComboHijo".equals(tarea)) {
			success = cargaPanelesManager.getDataComboHijo(tabla, valor);
		}else if("llenaGrid".equals(tarea)) {
			success = cargaPanelesManager.getDataGrid(tabla, valor);
		}
		log.debug("Fin de CargaInfo");
		return SUCCESS;
	}

	
	//Getters and setters:
	
	public void setCargaPanelesManager(CargaPanelesManager cargaPanelesManager) {
		this.cargaPanelesManager = cargaPanelesManager;
	}
	
	public String getTarea() {
		return tarea;
	}

	public void setTarea(String tarea) {
		this.tarea = tarea;
	}

	public List<?> getSuccess() {
		return success;
	}

	public void setSuccess(List<?> success) {
		this.success = success;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getTabla() {
		return tabla;
	}

	public void setTabla(String tabla) {
		this.tabla = tabla;
	}
	
}