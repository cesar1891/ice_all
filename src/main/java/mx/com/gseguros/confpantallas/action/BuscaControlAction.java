package mx.com.gseguros.confpantallas.action;

import java.util.List;

import mx.com.gseguros.confpantallas.delegate.BuscaControlManager;
import mx.com.gseguros.confpantallas.model.DinamicTatriVo;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;


public class BuscaControlAction extends ActionSupport {
	
	private static final long serialVersionUID = -6028755323301161889L;

	private Logger logger = Logger.getLogger(BuscaControlAction.class);
	
	private transient BuscaControlManager buscaControlManager; 
	
	private String tarea;
	
	private List<DinamicTatriVo> atributos;

	private String strRamo;
	private String campo;
	private String tabla;
	
	public String execute() {
		
		Integer sep = tabla.indexOf("_");
		String tablaO = "";String query = "";
		if(sep > -1){
			tablaO = tabla.substring(sep+1);
		}
		if(getTarea().equals("getDatosCtl")) {
			int c = strRamo.indexOf("_");
			String r = strRamo.substring(c+1);
			if(tablaO.equals("TATRIPOL")){
				query = "DatosControlTatripol";
			}else if(tablaO.equals("TATRISIT")){
				query = "DatosControlTatrisit";
			}
			atributos = buscaControlManager.getDataControl(r, getCampo(), query);
		}
		
		return SUCCESS;
	}
	
	
	//Getters and setters:
	
	public void setBuscaControlManager(BuscaControlManager buscaControlManager) {
		this.buscaControlManager = buscaControlManager;
	}

	public String getTarea() {
		return tarea;
	}

	public void setTarea(String tarea) {
		this.tarea = tarea;
	}
	
	public List<DinamicTatriVo> getAtributos() {
		return atributos;
	}

	public void setAtributos(List<DinamicTatriVo> atributos) {
		this.atributos = atributos;
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
