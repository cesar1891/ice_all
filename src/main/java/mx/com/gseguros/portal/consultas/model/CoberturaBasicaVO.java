package mx.com.gseguros.portal.consultas.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MAVR
 *
 */
public class CoberturaBasicaVO implements Serializable{

	private static final long serialVersionUID = -8555353864912795413L;

	private String descripcion;	
	private String copagoporcentaje;
	private String copagomonto;
	private String incluido;
	private String beneficiomaximo;
	private String beneficiomaximovida;
			
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCopagoporcentaje() {
		return copagoporcentaje;
	}

	public void setCopagoporcentaje(String copagoporcentaje) {
		this.copagoporcentaje = copagoporcentaje;
	}

	public String getCopagomonto() {
		return copagomonto;
	}

	public void setCopagomonto(String copagomonto) {
		this.copagomonto = copagomonto;
	}

	public String getIncluido() {
		return incluido;
	}

	public void setIncluido(String incluido) {
		this.incluido = incluido;
	}

	public String getBeneficiomaximo() {
		return beneficiomaximo;
	}


	public void setBeneficiomaximo(String beneficiomaximo) {
		this.beneficiomaximo = beneficiomaximo;
	}

	public String getBeneficiomaximovida() {
		return beneficiomaximovida;
	}

	public void setBeneficiomaximovida(String beneficiomaximovida) {
		this.beneficiomaximovida = beneficiomaximovida;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}		
}