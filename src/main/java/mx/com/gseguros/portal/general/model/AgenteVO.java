package mx.com.gseguros.portal.general.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AgenteVO implements Serializable {

	private static final long serialVersionUID = -3618918597350653968L;

	/**
	 * C&oacute;digo del agente
	 */
	private String cdagente;
	
	/**
	 * 
	 */
	private String cdideper;
	
	/**
	 * 
	 */
	private String nombre;
	
	/**
	 * 
	 */
	private String fedesde;

	public String getCdagente() {
		return cdagente;
	}

	public void setCdagente(String cdagente) {
		this.cdagente = cdagente;
	}

	public String getCdideper() {
		return cdideper;
	}

	public void setCdideper(String cdideper) {
		this.cdideper = cdideper;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFedesde() {
		return fedesde;
	}

	public void setFedesde(String fedesde) {
		this.fedesde = fedesde;
	}

	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	
}