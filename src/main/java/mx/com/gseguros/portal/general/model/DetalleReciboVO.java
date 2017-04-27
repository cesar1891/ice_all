package mx.com.gseguros.portal.general.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class DetalleReciboVO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public DetalleReciboVO() {
		super();
	}

	/**
	 * Clave del tipo de concepto del detalle del recibo
	 */
	private String cdtipcon;
	
	/**
	 * Descripci&oacute;n del tipo de concepto del detalle del recibo
	 */
	private String dstipcon;
	
	/**
	 * Importe del detalle del recibo
	 */
	private String ptimport;

	
	//Getters and setters:

	public String getCdtipcon() {
		return cdtipcon;
	}

	public void setCdtipcon(String cdtipcon) {
		this.cdtipcon = cdtipcon;
	}

	public String getDstipcon() {
		return dstipcon;
	}

	public void setDstipcon(String dstipcon) {
		this.dstipcon = dstipcon;
	}

	public String getPtimport() {
		return ptimport;
	}

	public void setPtimport(String ptimport) {
		this.ptimport = ptimport;
	}
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	
}