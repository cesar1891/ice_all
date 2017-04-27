package mx.com.gseguros.confpantallas.action;

import java.util.List;

import mx.com.gseguros.confpantallas.delegate.ControlesPredeterminadosManager;
import mx.com.gseguros.confpantallas.model.NodoVO;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

public class PintaTreePanelAction extends ActionSupport {
	
	private static final long serialVersionUID = -7144316786149361705L;

	private Logger logger = Logger.getLogger(PintaPanelAction.class);
	
	private transient ControlesPredeterminadosManager controlesPredeterminadosManager;
	
	//TODO:Eliminar propiedad
	private String json;
	
	private List<NodoVO> children;
	
	private String nodo;
	
	private String tabla;
	
	
	public String execute() {
		
		Integer sep = nodo.indexOf("_");
		String tarea = nodo.substring(0,sep);
		String sNodo = nodo.substring(sep+1);
		logger.debug("Pinto que paso por PintaTreePanel NODO ::::::: [" + sNodo + "]");
		logger.debug("Pinto que paso por PintaTreePanel TAREA ::::::: [" + tarea + "]");
		
		sep = tabla.indexOf("_");
		if(sep > -1){
			tabla = tabla.substring(sep+1);
		}
		if(tarea.equals("inicio")){
			children = controlesPredeterminadosManager.getInfo("ListadeTablasPredeterminados",false,"tabla","");
		}else if(tarea.equals("tabla")){
			String query = "";
			if(tabla.equals("TATRIPOL")){
				query = "ListadeRamosTatripol";
			}else if(tabla.equals("TATRISIT")){
				query = "ListadeRamosTatrisit";
			}
			children = controlesPredeterminadosManager.getInfo(query,false,"ramo","");
		}else if(tarea.equals("ramo")){
			String query = "";
			if(tabla.equals("TATRIPOL")){
				query = "ListadeCamposTatripol";
			}else if(tabla.equals("TATRISIT")){
				query = "ListadeCamposTatrisit";
			}
			children = controlesPredeterminadosManager.getInfo(query,true,"campo",sNodo);
		}
		return "success";
	}
	
	
	// Getters and setters:
	
	public void setControlesPredeterminadosManager(ControlesPredeterminadosManager controlesPredeterminadosManager) {
		this.controlesPredeterminadosManager = controlesPredeterminadosManager;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public List<NodoVO> getChildren() {
		return children;
	}

	public void setChildren(List<NodoVO> children) {
		this.children = children;
	}

	public String getNodo() {
		return nodo;
	}

	public void setNodo(String nodo) {
		this.nodo = nodo;
	}

	public String getTabla() {
		return tabla;
	}

	public void setTabla(String tabla) {
		this.tabla = tabla;
	}

}