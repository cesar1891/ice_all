package mx.com.aon.portal.model.principal;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * @author sergio.ramirez
 * @see
 */
public class PaginaVO implements Serializable {

	/**
	 *Serial Version 
	 */
	private static final long serialVersionUID = 8313685288069513943L;
	private String claveConfiguracion;
	private String claveRol;
	private String claveElemento;
	private String dsConfiguracion;
	private String dsElemento; 
	private String claveSistemaRol;
	private String dsSistemaRol;
	private String claveEstado;
	
    /**
     * @return String
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
    
	public String getClaveConfiguracion() {
		return claveConfiguracion;
	}
	public void setClaveConfiguracion(String claveConfiguracion) {
		this.claveConfiguracion = claveConfiguracion;
	}
	public String getClaveRol() {
		return claveRol;
	}
	public void setClaveRol(String claveRol) {
		this.claveRol = claveRol;
	}
	
	public String getClaveElemento() {
		return claveElemento;
	}
	public void setClaveElemento(String claveElemento) {
		this.claveElemento = claveElemento;
	}
	public String getDsConfiguracion() {
		return dsConfiguracion;
	}
	public void setDsConfiguracion(String dsConfiguracion) {
		this.dsConfiguracion = dsConfiguracion;
	}
	public String getDsElemento() {
		return dsElemento;
	}
	public void setDsElemento(String dsElemento) {
		this.dsElemento = dsElemento;
	}
	public String getClaveSistemaRol() {
		return claveSistemaRol;
	}
	public void setClaveSistemaRol(String claveSistemaRol) {
		this.claveSistemaRol = claveSistemaRol;
	}
	public String getDsSistemaRol() {
		return dsSistemaRol;
	}
	public void setDsSistemaRol(String dsSistemaRol) {
		this.dsSistemaRol = dsSistemaRol;
	}
	public String getClaveEstado() {
		return claveEstado;
	}
	public void setClaveEstado(String claveEstado) {
		this.claveEstado = claveEstado;
	}
	
	

}
