package mx.com.gseguros.portal.consultas.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author hector.lopez
 *
 */
public class CoberturaVO implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8555353864912795413L;

	private String nmsituac;
	private String cdgarant;
	private String dsgarant;
	private String ptcapita;
	private String cdcapita;
	private String dscapita;
	private String cdtipfra;
	private String dstipfra;
	private String pttasa;


	public String getNmsituac() {
		return nmsituac;
	}


	public void setNmsituac(String nmsituac) {
		this.nmsituac = nmsituac;
	}


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


	public String getCdcapita() {
		return cdcapita;
	}


	public void setCdcapita(String cdcapita) {
		this.cdcapita = cdcapita;
	}


	public String getDscapita() {
		return dscapita;
	}


	public void setDscapita(String dscapita) {
		this.dscapita = dscapita;
	}


	public String getCdtipfra() {
		return cdtipfra;
	}


	public void setCdtipfra(String cdtipfra) {
		this.cdtipfra = cdtipfra;
	}


	public String getDstipfra() {
		return dstipfra;
	}


	public void setDstipfra(String dstipfra) {
		this.dstipfra = dstipfra;
	}


	public String getPttasa() {
		return pttasa;
	}


	public void setPttasa(String pttasa) {
		this.pttasa = pttasa;
	}	
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
}
