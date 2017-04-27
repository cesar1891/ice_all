package mx.com.gseguros.confpantallas.action;

import mx.com.gseguros.confpantallas.delegate.AdminBuscaControlDelegate;

/**
 * Does some thing in old style.
 *
 * @deprecated use {@link BuscaControlAction} instead.  
 */
@Deprecated
public class BuscaControl {
	private String tarea;
	private String regreso;
	private String strRamo;
	private String campo;
	private String tabla;
	
	public String execute() {
		
		AdminBuscaControlDelegate admBusca = new AdminBuscaControlDelegate();
		Integer sep = getTabla().indexOf("_");
		String tablaO = "";String query = "";
		if(sep > -1){
			tablaO = getTabla().substring(sep+1);
		}
		if(getTarea().equals("getDatosCtl")){
			int c = getStrRamo().indexOf("_");
			String r = getStrRamo().substring(c+1);
			if(tablaO.equals("TATRIPOL")){
				query = "DatosControlTatripol";
			}else if(tablaO.equals("TATRISIT")){
				query = "DatosControlTatrisit";
			}
			setRegreso(admBusca.gatDataControl(r, getCampo(), query));
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

	public String getStrRamo() {
		return strRamo;
	}

	public void setStrRamo(String strRamo) {
		this.strRamo = strRamo;
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public String getTabla() {
		return tabla;
	}

	public void setTabla(String tabla) {
		this.tabla = tabla;
	}
}
