package mx.com.gseguros.cotizacionautos.model;

import java.io.Serializable;

public class TipoMoneda implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String height;
	private String width;
	private String style;
	private String html;
	
	public TipoMoneda(String dato){
		this.height = "15";
		this.width = "12";
		this.style = "text-align: right;margin-left:5px;font-size: 8pt; font-family: sans-serif;";
		this.html = dato;
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
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	

}
