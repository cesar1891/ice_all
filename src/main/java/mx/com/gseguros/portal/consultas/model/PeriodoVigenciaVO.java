package mx.com.gseguros.portal.consultas.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MAVR
 *
 */
public class PeriodoVigenciaVO implements Serializable{

	private static final long serialVersionUID = -8555353864912795413L;

	/**
	 * Codigo de la persona
	 */
	private String estatus;	
	private String dias;
	private String anios;
	private String feinicial;
	private String fefinal;
	
	
	
	public String getEstatus() {
		return estatus;
	}



	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}



	public String getDias() {
		return dias;
	}



	public void setDias(String dias) {
		this.dias = dias;
	}



	public String getAnios() {
		return anios;
	}



	public void setAnios(String anios) {
		this.anios = anios;
	}



	public String getFeinicial() {
		return feinicial;
	}



	public void setFeinicial(String feinicial) {
		this.feinicial = feinicial;
	}



	public String getFefinal() {
		return fefinal;
	}



	public void setFefinal(String fefinal) {
		this.fefinal = fefinal;
	}



	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}		
}