package mx.com.gseguros.portal.consultas.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author hector.lopez
 *
 */
public class ReciboAgenteVO implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8555353864912795413L;

	private String nmrecibo;
	private String feinicio;
	private String fefin;
	private String dsgarant;
	private String ptimport;
	
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}


	public String getNmrecibo() {
		return nmrecibo;
	}


	public void setNmrecibo(String nmrecibo) {
		this.nmrecibo = nmrecibo;
	}


	public String getFeinicio() {
		return feinicio;
	}


	public void setFeinicio(String feinicio) {
		this.feinicio = feinicio;
	}


	public String getFefin() {
		return fefin;
	}


	public void setFefin(String fefin) {
		this.fefin = fefin;
	}


	public String getDsgarant() {
		return dsgarant;
	}


	public void setDsgarant(String dsgarant) {
		this.dsgarant = dsgarant;
	}


	public String getPtimport() {
		return ptimport;
	}


	public void setPtimport(String ptimport) {
		this.ptimport = ptimport;
	}	
	
	
		
}
