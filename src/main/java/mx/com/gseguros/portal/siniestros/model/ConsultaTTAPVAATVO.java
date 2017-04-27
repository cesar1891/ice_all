package mx.com.gseguros.portal.siniestros.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Alberto
 *
 */
public class ConsultaTTAPVAATVO implements Serializable{

	private static final long serialVersionUID = -8555353864912795413L;
	private String nmtabla;
	private String otclave1;
	private String otclave2;
	private String otclave3;
	private String otclave4;
	private String otclave5;
	private String fedesde;
	private String fehasta;
	private String otvalor01;
	

	
	
	public String getOtclave4() {
		return otclave4;
	}




	public void setOtclave4(String otclave4) {
		this.otclave4 = otclave4;
	}




	public String getNmtabla() {
		return nmtabla;
	}




	public void setNmtabla(String nmtabla) {
		this.nmtabla = nmtabla;
	}




	public String getOtclave1() {
		return otclave1;
	}




	public void setOtclave1(String otclave1) {
		this.otclave1 = otclave1;
	}




	public String getOtclave2() {
		return otclave2;
	}




	public void setOtclave2(String otclave2) {
		this.otclave2 = otclave2;
	}




	public String getOtclave3() {
		return otclave3;
	}




	public void setOtclave3(String otclave3) {
		this.otclave3 = otclave3;
	}




	public String getOtclave5() {
		return otclave5;
	}




	public void setOtclave5(String otclave5) {
		this.otclave5 = otclave5;
	}




	public String getFedesde() {
		return fedesde;
	}




	public void setFedesde(String fedesde) {
		this.fedesde = fedesde;
	}




	public String getFehasta() {
		return fehasta;
	}




	public void setFehasta(String fehasta) {
		this.fehasta = fehasta;
	}




	public String getOtvalor01() {
		return otvalor01;
	}




	public void setOtvalor01(String otvalor01) {
		this.otvalor01 = otvalor01;
	}




	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}	
	
}
