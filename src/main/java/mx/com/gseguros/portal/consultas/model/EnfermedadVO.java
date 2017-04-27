package mx.com.gseguros.portal.consultas.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MAVR
 *
 */
public class EnfermedadVO implements Serializable{

	private static final long serialVersionUID = -8555353864912795413L;
	
	private String icd;	
	private String enfermedad;
	
	
	public String getIcd() {
		return icd;
	}



	public void setIcd(String icd) {
		this.icd = icd;
	}



	public String getEnfermedad() {
		return enfermedad;
	}



	public void setEnfermedad(String enfermedad) {
		this.enfermedad = enfermedad;
	}



	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}		
}