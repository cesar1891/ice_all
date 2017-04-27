package mx.com.gseguros.portal.consultas.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author hector.lopez
 *
 */
public class TarifaVO implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8555353864912795413L;

	private String cdgarant;//garantia
	private String dsgarant;//nombre garantia
	private String sumaAsegurada;
	private String montoPrima;
	private String montoComision;
	
	public String getCdgarant() {
		return cdgarant;
	}
	public void setCdgarant(String cdgarant) {
		this.cdgarant = cdgarant;
	}
	public String getDsgarant() {
		return dsgarant;
	}
	public void setDsgarant(String dsgarant) {
		this.dsgarant = dsgarant;
	}
	public String getSumaAsegurada() {
		return sumaAsegurada;
	}
	public void setSumaAsegurada(String sumaAsegurada) {
		this.sumaAsegurada = sumaAsegurada;
	}
	public String getMontoPrima() {
		return montoPrima;
	}
	public void setMontoPrima(String montoPrima) {
		this.montoPrima = montoPrima;
	}
	public String getMontoComision() {
		return montoComision;
	}
	public void setMontoComision(String montoComision) {
		this.montoComision = montoComision;
	}


	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}	
		
}
