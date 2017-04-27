package mx.com.gseguros.portal.siniestros.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Alberto
 *
 */
public class DatosSiniestroVO implements Serializable{

	private static final long serialVersionUID = -8555353864912795413L;
	private String cdgarant;
	private String subcobertura;
	private String cdcapita;
	private String luc;
	private String deducible;
	private String copago;
	private String benefmax;
	private String icd;
	private String cpt;
	private String limites;
	private String tipoCopago;

	
	public String getCdgarant() {
		return cdgarant;
	}


	public void setCdgarant(String cdgarant) {
		this.cdgarant = cdgarant;
	}


	public String getSubcobertura() {
		return subcobertura;
	}


	public void setSubcobertura(String subcobertura) {
		this.subcobertura = subcobertura;
	}


	public String getCdcapita() {
		return cdcapita;
	}


	public void setCdcapita(String cdcapita) {
		this.cdcapita = cdcapita;
	}


	public String getLuc() {
		return luc;
	}


	public void setLuc(String luc) {
		this.luc = luc;
	}


	public String getDeducible() {
		return deducible;
	}


	public void setDeducible(String deducible) {
		this.deducible = deducible;
	}


	public String getCopago() {
		return copago;
	}


	public void setCopago(String copago) {
		this.copago = copago;
	}


	public String getBenefmax() {
		return benefmax;
	}


	public void setBenefmax(String benefmax) {
		this.benefmax = benefmax;
	}


	public String getIcd() {
		return icd;
	}


	public void setIcd(String icd) {
		this.icd = icd;
	}


	public String getCpt() {
		return cpt;
	}


	public void setCpt(String cpt) {
		this.cpt = cpt;
	}


	public String getLimites() {
		return limites;
	}


	public void setLimites(String limites) {
		this.limites = limites;
	}


	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}


	public String getTipoCopago() {
		return tipoCopago;
	}


	public void setTipoCopago(String tipoCopago) {
		this.tipoCopago = tipoCopago;
	}	
	
	
}
