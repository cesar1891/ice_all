package mx.com.gseguros.confpantallas.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DinamicOpenPanelVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	DinamicPanelVo panel;
	List<DinamicPanelAttrVo> attrs;
	List<DinamicOpenPanelVO> attrsHijos;
	List<DinamicOpenControlVO> controles;
	
	public DinamicOpenPanelVO (DinamicPanelVo panel, List<DinamicPanelAttrVo> attrs, List<DinamicOpenControlVO> controles){
		this.panel = panel;
		this.attrs = attrs;
		this.attrsHijos = new ArrayList<DinamicOpenPanelVO>();
		this.controles = controles;
		
	}
	
	public DinamicPanelVo getPanel() {
		return panel;
	}
	public void setPanel(DinamicPanelVo panel) {
		this.panel = panel;
	}
	public List<DinamicPanelAttrVo> getAttrs() {
		return attrs;
	}
	public void setAttrs(List<DinamicPanelAttrVo> attrs) {
		this.attrs = attrs;
	}

	public List<DinamicOpenPanelVO> getAttrsHijos() {
		return attrsHijos;
	}

	public void setAttrsHijos(List<DinamicOpenPanelVO> attrsHijos) {
		this.attrsHijos = attrsHijos;
	}   
	
	public void addAttrsHijos(DinamicOpenPanelVO hijo){
		this.attrsHijos.add(hijo);
	}

	public List<DinamicOpenControlVO> getControles() {
		return controles;
	}

	public void setControles(List<DinamicOpenControlVO> controles) {
		this.controles = controles;
	}

}
