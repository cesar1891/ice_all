package mx.com.gseguros.cotizacionautos.model;

import java.io.Serializable;

public class TipoConceptoPrimerPago implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String height;
	private String width;
	private String style;
	private String html;
	
	
	public TipoConceptoPrimerPago (){
		this.height = "15";
		this.width = "94";
		this.style = "margin-left:5px;font-size: 8pt; font-family: sans-serif;";
		this.html = "Primer Pago";
		
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
