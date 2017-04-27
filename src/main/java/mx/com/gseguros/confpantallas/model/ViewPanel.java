package mx.com.gseguros.confpantallas.model;

import java.util.ArrayList;
import java.util.List;

public class ViewPanel implements java.io.Serializable  {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String idPadre;
	private List<DinamicPanelAttrGetVo> listaAttr;
	private ArrayList<String> listaCtrls;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<DinamicPanelAttrGetVo> getListaAttr() {
		return listaAttr;
	}
	public void setListaAttr(List<DinamicPanelAttrGetVo> listaAttr) {
		this.listaAttr = listaAttr;
	}
	public ArrayList<String> getListaCtrls() {
		return listaCtrls;
	}
	public void setListaCtrls(ArrayList<String> listaCtrls) {
		this.listaCtrls = listaCtrls;
	}
	public String getIdPadre() {
		return idPadre;
	}
	public void setIdPadre(String idPadre) {
		this.idPadre = idPadre;
	}

}
