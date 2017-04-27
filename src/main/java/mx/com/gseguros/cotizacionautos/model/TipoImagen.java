package mx.com.gseguros.cotizacionautos.model;

import java.io.Serializable;

public class TipoImagen implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String height;
	private String width;
	private String url;
	private String rowspan;
	
	public TipoImagen (String url, String height){
		this.height = "20";
		this.width = "94";
		this.rowspan = "4";
		this.url =  "<img width = "+this.width+"  height = "+this.height+" src=\""+ url +"\"/>";
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRowspan() {
		return rowspan;
	}
	public void setRowspan(String rowspan) {
		this.rowspan = rowspan;
	}
	
	

}
