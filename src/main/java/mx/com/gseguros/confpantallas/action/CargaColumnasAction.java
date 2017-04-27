package mx.com.gseguros.confpantallas.action;

import java.util.List;

import mx.com.gseguros.confpantallas.delegate.CargaColumnasManager;
import mx.com.gseguros.confpantallas.model.DinamicColumnaAttrVo;

import com.opensymphony.xwork2.ActionSupport;

public class CargaColumnasAction extends ActionSupport {
	private static final long serialVersionUID = -6144366788149461705L;
	//private Logger logger = Logger.getLogger(CargaColumnasAction.class);
	
	//private transient CargaColumnasManager cargaColumnas;
	
	private String panelName;
	private List<DinamicColumnaAttrVo> data;
	
	public String execute() {
		CargaColumnasManager cargaCol = new CargaColumnasManager();
		setData(cargaCol.getColumnas(panelName));
		return SUCCESS;
	}
	
	
	public String getPanelName() {
		return panelName;
	}
	public void setPanelName(String panelName) {
		this.panelName = panelName;
	}
	public List<DinamicColumnaAttrVo> getData() {
		return data;
	}
	public void setData(List<DinamicColumnaAttrVo> data) {
		this.data = data;
	}


//	public CargaColumnasManager getCargaColumnas() {
//		return cargaColumnas;
//	}


//	public void setCargaColumnas(CargaColumnasManager cargaColumnas) {
//		this.cargaColumnas = cargaColumnas;
//	}


}
