package mx.com.gseguros.portal.general.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ParamReporteVO implements Serializable {

	private static final long serialVersionUID = 1710915269673004791L;
	
	/**
	 * Nombre del par&aacute;metro
	 */
	private String nombre;
	
	/**
	 * Descripci&oacute;n del par&aacute;metro
	 */
	private String descripcion;
	
	/**
	 * Tipo de par&aacute;metro
	 */
	private String tipo;
	
	/**
	 * Valor del par&aacute;metro
	 */
	private String valor;
	
	/**
	 * Indica si el par&aacute;metro es obligatorio
	 */
	private boolean obligatorio;
	
	
	public ParamReporteVO() {
		super();
	}

	
	//Getters and setters:
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
	
	public boolean isObligatorio() {
		return obligatorio;
	}

	public void setObligatorio(boolean obligatorio) {
		this.obligatorio = obligatorio;
	}


	/**
     * @return String
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
	
}