package mx.com.gseguros.portal.siniestros.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Alberto
 *
 */
public class CoberturaPolizaVO implements Serializable{

	private static final long serialVersionUID = -8555353864912795413L;
	private String cdgarant;
	private String dsgarant;
	private String ptcapita;
	

	
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



	public String getPtcapita() {
		return ptcapita;
	}



	public void setPtcapita(String ptcapita) {
		this.ptcapita = ptcapita;
	}



	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}	
	
}
