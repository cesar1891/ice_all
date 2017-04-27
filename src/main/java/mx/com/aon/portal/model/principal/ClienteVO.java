package mx.com.aon.portal.model.principal;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
/**
 * 
 * @author sergio.ramirez
 *
 */
public class ClienteVO implements Serializable{

	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = -2341280900993947683L;
	
	private String claveCliente;
	private String descripcion;
	private String clavePersona;
	/**
	 * 
	 * @return
	 */
	public String getClaveCliente() {
		return claveCliente;
	}
	/**
	 * 
	 * @param claveCliente
	 */
	public void setClaveCliente(String claveCliente) {
		this.claveCliente = claveCliente;
	}
	/**
	 * 
	 * @return
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * 
	 * @param descripcion
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * 
	 * @return
	 */
	public String getClavePersona() {
		return clavePersona;
	}
	/**
	 * 
	 * @param clavePersona
	 */
	public void setClavePersona(String clavePersona) {
		this.clavePersona = clavePersona;
	}

    /**
     * @return String
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
