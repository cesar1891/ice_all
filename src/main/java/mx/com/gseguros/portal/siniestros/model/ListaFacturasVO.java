package mx.com.gseguros.portal.siniestros.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Alberto
 *
 */
public class ListaFacturasVO implements Serializable{

	private static final long serialVersionUID = -8555353864912795413L;
	
	private String cdfactur;
	private String fefactur;
	private String cdprovee;
	private String cdrol;
	private String cdperson;
	private String mtolocal;
	private String desProvee;
	
	
	public String getCdfactur() {
		return cdfactur;
	}


	public void setCdfactur(String cdfactur) {
		this.cdfactur = cdfactur;
	}


	public String getFefactur() {
		return fefactur;
	}


	public void setFefactur(String fefactur) {
		this.fefactur = fefactur;
	}


	public String getCdprovee() {
		return cdprovee;
	}


	public void setCdprovee(String cdprovee) {
		this.cdprovee = cdprovee;
	}


	public String getCdrol() {
		return cdrol;
	}


	public void setCdrol(String cdrol) {
		this.cdrol = cdrol;
	}


	public String getCdperson() {
		return cdperson;
	}


	public void setCdperson(String cdperson) {
		this.cdperson = cdperson;
	}


	public String getMtolocal() {
		return mtolocal;
	}


	public void setMtolocal(String mtolocal) {
		this.mtolocal = mtolocal;
	}


	public String getDesProvee() {
		return desProvee;
	}


	public void setDesProvee(String desProvee) {
		this.desProvee = desProvee;
	}


	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}	
	
}
