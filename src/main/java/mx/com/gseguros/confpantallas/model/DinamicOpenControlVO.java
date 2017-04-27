package mx.com.gseguros.confpantallas.model;

import java.io.Serializable;
import java.util.List;

public class DinamicOpenControlVO implements Serializable {

	private static final long serialVersionUID = 1L;

	DinamicControlVo control;
	List<DinamicControlAttrVo> controlAttr;
	
	public DinamicOpenControlVO(DinamicControlVo control,List<DinamicControlAttrVo> controlAttr){
		this.control = control;
		this.controlAttr = controlAttr;
	}

	public List<DinamicControlAttrVo> getControlAttr() {
		return controlAttr;
	}

	public void setControlAttr(List<DinamicControlAttrVo> controlAttr) {
		this.controlAttr = controlAttr;
	}

	public DinamicControlVo getControl() {
		return control;
	}

	public void setControl(DinamicControlVo control) {
		this.control = control;
	}
	
}
