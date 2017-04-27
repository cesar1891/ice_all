package mx.com.gseguros.portal.consultas.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MAVR
 *
 */
public class DatosComplementariosVO implements Serializable{

	private static final long serialVersionUID = -8555353864912795413L;

	/**
	 * Codigo de la persona
	 */
	private String cdperson;
	private String nombre;
	private String fenacimi;	
	private String edad;
	private String dsplan;
	private String agente;
	
	
	
	public String getCdperson() {
		return cdperson;
	}



	public void setCdperson(String cdperson) {
		this.cdperson = cdperson;
	}



	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public String getFenacimi() {
		return fenacimi;
	}



	public void setFenacimi(String fenacimi) {
		this.fenacimi = fenacimi;
	}



	public String getEdad() {
		return edad;
	}



	public void setEdad(String edad) {
		this.edad = edad;
	}

	public String getDsplan() {
		return dsplan;
	}



	public void setDsplan(String dsplan) {
		this.dsplan = dsplan;
	}



	public String getAgente() {
		return agente;
	}



	public void setAgente(String agente) {
		this.agente = agente;
	}



	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}		
}