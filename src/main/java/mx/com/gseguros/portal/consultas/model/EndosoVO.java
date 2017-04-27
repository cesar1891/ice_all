package mx.com.gseguros.portal.consultas.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MAVR
 *
 */
public class EndosoVO implements Serializable{

	private static final long serialVersionUID = -8555353864912795413L;
	
	private String cdperson;	
	private String tipoendoso;
	private String tipoextraprima;
	private String descripcion;
	private String resumen;
	private String texto;
	
	
		
	public String getCdperson() {
		return cdperson;
	}



	public void setCdperson(String cdperson) {
		this.cdperson = cdperson;
	}



	public String getTipoendoso() {
		return tipoendoso;
	}



	public void setTipoendoso(String tipoendoso) {
		this.tipoendoso = tipoendoso;
	}



	public String getTipoextraprima() {
		return tipoextraprima;
	}



	public void setTipoextraprima(String tipoextraprima) {
		this.tipoextraprima = tipoextraprima;
	}



	public String getDescripcion() {
		return descripcion;
	}



	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}



	public String getResumen() {
		return resumen;
	}



	public void setResumen(String resumen) {
		this.resumen = resumen;
	}



	public String getTexto() {
		return texto;
	}



	public void setTexto(String texto) {
		this.texto = texto;
	}



	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}		
}