package mx.com.gseguros.confpantallas.model;

import java.util.List;

import net.sf.json.JSONArray;

public class DinamicData implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String nombreVar;
	private String descripcion;
	
	private List<DinamicItemBean> listAtributos;
	
	public String getItemsJSON() {
		JSONArray jsonArray = JSONArray.fromObject( listAtributos );  
		return jsonArray.toString();
	}

	public String getNombreVar() {
		return nombreVar;
	}

	public void setNombreVar(String nombreVar) {
		this.nombreVar = nombreVar;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<DinamicItemBean> getListAtributos() {
		return listAtributos;
	}

	public void setListAtributos(List<DinamicItemBean> listAtributos) {
		this.listAtributos = listAtributos;
	}


}
