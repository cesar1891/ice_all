package mx.com.gseguros.portal.siniestros.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Alberto
 *
 */
public class ConsultaTDETAUTSVO implements Serializable{

	private static final long serialVersionUID = -8555353864912795413L;
	private String nmautser;
	private String cdtipaut;
	private String cdmedico;
	private String nombreMedico;
	private String cdtipmed;
	private String descTipMed;
	private String cdcpt;
	private String desccpt;
	private String precio;
	private String cantporc;
	private String ptimport;
	
	

	
	public String getDescTipMed() {
		return descTipMed;
	}



	public void setDescTipMed(String descTipMed) {
		this.descTipMed = descTipMed;
	}



	public String getCdtipmed() {
		return cdtipmed;
	}



	public void setCdtipmed(String cdtipmed) {
		this.cdtipmed = cdtipmed;
	}



	public String getNmautser() {
		return nmautser;
	}



	public void setNmautser(String nmautser) {
		this.nmautser = nmautser;
	}



	public String getCdtipaut() {
		return cdtipaut;
	}



	public void setCdtipaut(String cdtipaut) {
		this.cdtipaut = cdtipaut;
	}



	public String getCdmedico() {
		return cdmedico;
	}



	public void setCdmedico(String cdmedico) {
		this.cdmedico = cdmedico;
	}



	public String getNombreMedico() {
		return nombreMedico;
	}



	public void setNombreMedico(String nombreMedico) {
		this.nombreMedico = nombreMedico;
	}



	public String getCdcpt() {
		return cdcpt;
	}



	public void setCdcpt(String cdcpt) {
		this.cdcpt = cdcpt;
	}



	public String getDesccpt() {
		return desccpt;
	}



	public void setDesccpt(String desccpt) {
		this.desccpt = desccpt;
	}



	public String getPrecio() {
		return precio;
	}



	public void setPrecio(String precio) {
		this.precio = precio;
	}



	public String getCantporc() {
		return cantporc;
	}



	public void setCantporc(String cantporc) {
		this.cantporc = cantporc;
	}



	public String getPtimport() {
		return ptimport;
	}



	public void setPtimport(String ptimport) {
		this.ptimport = ptimport;
	}



	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}	
	
}
