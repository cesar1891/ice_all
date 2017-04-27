package mx.com.gseguros.cotizacionautos.model;

import java.io.Serializable;

public class TipoClickCompra implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String height;
	private String width;
	private String id;
	private String funcion;
	private String style;
	private String html;
	private String coma;
	
	String cdaseg;
	String cdplan;
	String nmsituac;
	
	public TipoClickCompra(String id, String coma, String cdaseg, String cdplan, String nmsituac){
		this.height = "30";
		this.width = "61";
		this.funcion = "callComprar(this)";
		this.id = id;
		this.style = "margin-right:5px;text-align: center;";
		this.html = "<img src=\"../../images/cotizacionautos/icono_ok.png\"";
		this.coma = coma;

		this.cdaseg = cdaseg;
		this.cdplan = cdplan;
		this.nmsituac = nmsituac;
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


	public String getComa() {
		return coma;
	}


	public void setComa(String coma) {
		this.coma = coma;
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


	public String getNmsituac() {
		return nmsituac;
	}


	public void setNmsituac(String nmsituac) {
		this.nmsituac = nmsituac;
	}

}
