package mx.com.gseguros.portal.consultas.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MAVR
 *
 */
public class PlanVO implements Serializable{

	private static final long serialVersionUID = -8555353864912795413L;
	
	private String plan;
	private String fecha;
	private String descripcion;	
	private String tipoprograma;
	private String calculopor;
	private String beneficiomaximoanual;
	private String beneficiomaximovida;
	private String identificadortarifa;
	private String zona;
			
	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTipoprograma() {
		return tipoprograma;
	}

	public void setTipoprograma(String tipoprograma) {
		this.tipoprograma = tipoprograma;
	}

	public String getCalculopor() {
		return calculopor;
	}

	public void setCalculopor(String calculopor) {
		this.calculopor = calculopor;
	}

	public String getBeneficiomaximoanual() {
		return beneficiomaximoanual;
	}

	public void setBeneficiomaximoanual(String beneficiomaximoanual) {
		this.beneficiomaximoanual = beneficiomaximoanual;
	}

	public String getBeneficiomaximovida() {
		return beneficiomaximovida;
	}

	public void setBeneficiomaximovida(String beneficiomaximovida) {
		this.beneficiomaximovida = beneficiomaximovida;
	}

	public String getIdentificadortarifa() {
		return identificadortarifa;
	}

	public void setIdentificadortarifa(String identificadortarifa) {
		this.identificadortarifa = identificadortarifa;
	}

	public String getZona() {
		return zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}		
}