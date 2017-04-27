package mx.com.gseguros.portal.siniestros.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Alberto
 *
 */
public class ConsultaPorcentajeVO implements Serializable{

	private static final long serialVersionUID = -8555353864912795413L;
	private String porcentaje;
	private String mtomedico;
	

	public String getMtomedico() {
		return mtomedico;
	}

	public void setMtomedico(String mtomedico) {
		this.mtomedico = mtomedico;
	}

	public String getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(String porcentaje) {
		this.porcentaje = porcentaje;
	}






	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}	
	
}
