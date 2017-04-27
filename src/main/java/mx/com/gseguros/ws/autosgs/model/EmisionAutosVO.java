package mx.com.gseguros.ws.autosgs.model;


import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class EmisionAutosVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Numero Externo de la Poliza Generada
     */
    private String nmpoliex;
    
    /**
     * Numero de Subramo, (Corresponde al Ramo para GS)
     */
    private String subramo;
	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public String getNmpoliex() {
		return nmpoliex;
	}

	public void setNmpoliex(String nmpoliex) {
		this.nmpoliex = nmpoliex;
	}

	public String getSubramo() {
		return subramo;
	}

	public void setSubramo(String subramo) {
		this.subramo = subramo;
	}
}
