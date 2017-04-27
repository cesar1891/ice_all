package mx.com.gseguros.confpantallas.action;

import mx.com.gseguros.confpantallas.delegate.AdminCargaPanelesDelegate;

/**
 * Does some thing in old style.
 *
 * @deprecated use {@link CargaInfoAction} instead.  
 */
@Deprecated
public class CargaInfo {
	private String tarea;
	private String regreso;
	private String valor;
	private String tabla;
	
	public String execute() {
		AdminCargaPanelesDelegate admPanel = new AdminCargaPanelesDelegate();
		if("llenaComboPaneles".equals(getTarea())){
			setRegreso(admPanel.GetListadePaneles());
		}else if("llenaCombo".equals(getTarea())){
			setRegreso(admPanel.getDataCombo(getTabla(),getValor()));
		}else if("llenaComboHijo".equals(getTarea())){
			setRegreso(admPanel.getDataComboHijo(getTabla(),getValor()));
		}else if("llenaGrid".equals(getTarea())){
			setRegreso(admPanel.getDataGrid(getTabla(),getValor()));
		}
		
		return "SUCCESS";
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
