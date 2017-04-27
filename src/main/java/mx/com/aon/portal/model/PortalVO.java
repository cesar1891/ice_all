package mx.com.aon.portal.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
/**
 * 
 * @author sergio.ramirez
 *
 */
public class PortalVO implements Serializable{

	/**
	 *Serial Version 
	 */
	private static final long serialVersionUID = 2678604925515292005L;
	
	/**
	 * Atributos asignados a la posicion de los componentes en relacion con la
	 * lista generada desde el action, para que cada uno de estos obtenga un 
	 * atributo unico asignado mediante session y poder ser desplegado mediante un 
	 * componente de velocity.
	 */
	
	private String claveConfigura;
	private String descripcionConfigura;
	private String claveElemento;
	private String claveSeccion;
	private String especificacion;
	private String contenido;
	private String claveTipo;
	private String descripcionArchivo;
	private String descripcionContenidoSec;
	private String otroContenido;
	
	/**
	 * 
	 * @return
	 */
	public String getClaveSeccion() {
		return claveSeccion;
	}
	/**
	 * 
	 * @param claveSeccion
	 */
	public void setClaveSeccion(String claveSeccion) {
		this.claveSeccion = claveSeccion;
	}
	/**
	 * 
	 * @return
	 */
	public String getEspecificacion() {
		return especificacion;
	}
	/**
	 * 
	 * @param especificacion
	 */
	public void setEspecificacion(String especificacion) {
		this.especificacion = especificacion;
	}
	/**
	 * 
	 * @return
	 */
	public String getContenido() {
		return contenido;
	}
	/**
	 * 
	 * @param contenido
	 */
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	/**
	 * 
	 * @return
	 */
	public String getClaveTipo() {
		return claveTipo;
	}
	/**
	 * 
	 * @param claveTipo
	 */
	public void setClaveTipo(String claveTipo) {
		this.claveTipo = claveTipo;
	}
	/**
	 * 
	 * @return
	 */
	public String getDescripcionArchivo() {
		return descripcionArchivo;
	}
	/**
	 * 
	 * @param descripcionArchivo
	 */
	public void setDescripcionArchivo(String descripcionArchivo) {
		this.descripcionArchivo = descripcionArchivo;
	}
	/**
	 * 
	 * @return
	 */
	public String getClaveConfigura() {
		return claveConfigura;
	}
	/**
	 * 
	 * @param claveConfigura
	 */
	public void setClaveConfigura(String claveConfigura) {
		this.claveConfigura = claveConfigura;
	}
	/**
	 * 
	 * @return
	 */
	public String getDescripcionConfigura() {
		return descripcionConfigura;
	}
	/**
	 * 
	 * @param descripcionConfigura
	 */
	public void setDescripcionConfigura(String descripcionConfigura) {
		this.descripcionConfigura = descripcionConfigura;
	}
	/**
	 * 
	 * @return
	 */
	public String getClaveElemento() {
		return claveElemento;
	}
	/**
	 * 
	 * @param claveElemento
	 */
	public void setClaveElemento(String claveElemento) {
		this.claveElemento = claveElemento;
	}
	/**
	 * 
	 * @return
	 */
	public String getDescripcionContenidoSec() {
		return descripcionContenidoSec;
	}
	/**
	 * 
	 * @param descripcionContenidoSec
	 */
	public void setDescripcionContenidoSec(String descripcionContenidoSec) {
		this.descripcionContenidoSec = descripcionContenidoSec;
	}
	/**
	 * 
	 * @return
	 */
	public String getOtroContenido() {
		return otroContenido;
	}
	/**
	 * 
	 * @param otroContenido
	 */
	public void setOtroContenido(String otroContenido) {
		this.otroContenido = otroContenido;
	}
	
	/**
     * @return String
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
	
	
}