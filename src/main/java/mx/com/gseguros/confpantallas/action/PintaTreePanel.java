package mx.com.gseguros.confpantallas.action;

import mx.com.gseguros.confpantallas.delegate.AdminControlesPredeterminados;

/**
 * Does some thing in old style.
 *
 * @deprecated use {@link PintaTreePanelAction} instead.  
 */
@Deprecated
public class PintaTreePanel {
	
	private String json;
	private String nodo;
	private String tabla;
	
	public String execute() {
		
		AdminControlesPredeterminados admCP = new AdminControlesPredeterminados();
		Integer sep = getNodo().indexOf("_");
		String tarea = getNodo().substring(0,sep);
		String sNodo = getNodo().substring(sep+1);
		System.out.println("Pinto que paso por PintaTreePanel NODO ::::::: [" + sNodo + "]");
		System.out.println("Pinto que paso por PintaTreePanel TAREA ::::::: [" + tarea + "]");
		sep = getTabla().indexOf("_");
		String tabla = "";
		if(sep > -1){
			tabla = getTabla().substring(sep+1);
		}
		if(tarea.equals("inicio")){
			setJson(admCP.getInfo("ListadeTablasPredeterminados",false,"tabla",""));
		}else if(tarea.equals("tabla")){
			String query = "";
			if(tabla.equals("TATRIPOL")){
				query = "ListadeRamosTatripol";
			}else if(tabla.equals("TATRISIT")){
				query = "ListadeRamosTatrisit";
			}
			setJson(admCP.getInfo(query,false,"ramo",""));
		}else if(tarea.equals("ramo")){
			String query = "";
			if(tabla.equals("TATRIPOL")){
				query = "ListadeCamposTatripol";
			}else if(tabla.equals("TATRISIT")){
				query = "ListadeCamposTatrisit";
			}
			setJson(admCP.getInfo(query,true,"campo",sNodo));
		}
		return "success";
		
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
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
