package mx.com.gseguros.portal.consultas.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author hector.lopez
 *
 */
public class ConsultaDatosAgenteVO implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8555353864912795413L;

	private String cdagente;
	private String cdideper;
	private String nombre;
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

	public void setFedesde(String date) {
		this.fedesde = date;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}	
	
}
