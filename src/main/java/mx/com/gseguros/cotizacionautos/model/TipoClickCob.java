package mx.com.gseguros.cotizacionautos.model;

import java.io.Serializable;

public class TipoClickCob implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String height;
	private String width;
	private String rowspan;
	private String url;
	private String id;
	private String funcion;
	private String cdaseg;
	private String cdplan;

	
	
	public TipoClickCob(String id, String cdplan, String cdaseg){
		this.height = "95";
		this.width = "26";
		this.rowspan = "4";
		this.url = "<img width = "+this.width+"  height = "+this.height+" src=\"../../images/cotizacionautos/cob";
		this.funcion = "callCoberturas(this)";
		this.id = id;
		this.cdplan = cdplan;
		this.cdaseg = cdaseg;
	}
	
	
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}


	public String getRowspan() {
		return rowspan;
	}


	public void setRowspan(String rowspan) {
		this.rowspan = rowspan;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getFuncion() {
		return funcion;
	}


	public void setFuncion(String funcion) {
		this.funcion = funcion;
	}


	public String getCdaseg() {
		return cdaseg;
	}


	public void setCdaseg(String cdaseg) {
		this.cdaseg = cdaseg;
	}


	public String getCdplan() {
		return cdplan;
	}


	public void setCdplan(String cdplan) {
		this.cdplan = cdplan;
	}

}
