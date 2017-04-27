package mx.com.gseguros.cotizacionautos.model;

import java.io.Serializable;

public class PlanVO  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String height;
	private String width;
	private String url;
	private String coma;
	
	public PlanVO() {
		this.height = "30";
		this.width = "99";
	}
	
	public PlanVO(String name, String url, String coma) {
		this.height = "30";
		this.width = "99";
		this.name = name;
		this.url = "<img width = "+this.width+"  height = "+this.height+" src=\""+ url +"\"/>";
		this.coma = coma;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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

	public String getComa() {
		return coma;
	}

	public void setComa(String coma) {
		this.coma = coma;
	}
	
}
