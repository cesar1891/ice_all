package mx.com.gseguros.portal.siniestros.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Alberto
 *
 */
public class AutorizaServiciosVO implements Serializable{

	private static final long serialVersionUID = -8555353864912795413L;
	private String nmautser;
	private String nmautant;
	private String fesolici;
	private String polizaafectada;
	private String cdprovee;
	private String nombreProveedor;
	private String statusTramite;
	private String descICD;
	private String cobertura;
	private String subcobertura;

	public String getNmautser() {
		return nmautser;
	}

	public void setNmautser(String nmautser) {
		this.nmautser = nmautser;
	}

	public String getNmautant() {
		return nmautant;
	}

	public void setNmautant(String nmautant) {
		this.nmautant = nmautant;
	}

	public String getFesolici() {
		return fesolici;
	}

	public void setFesolici(String fesolici) {
		this.fesolici = fesolici;
	}

	public String getPolizaafectada() {
		return polizaafectada;
	}

	public void setPolizaafectada(String polizaafectada) {
		this.polizaafectada = polizaafectada;
	}

	public String getCdprovee() {
		return cdprovee;
	}

	public void setCdprovee(String cdprovee) {
		this.cdprovee = cdprovee;
	}

	public String getNombreProveedor() {
		return nombreProveedor;
	}

	public void setNombreProveedor(String nombreProveedor) {
		this.nombreProveedor = nombreProveedor;
	}

	public String getStatusTramite() {
		return statusTramite;
	}

	public void setStatusTramite(String statusTramite) {
		this.statusTramite = statusTramite;
	}

	public String getDescICD() {
		return descICD;
	}

	public void setDescICD(String descICD) {
		this.descICD = descICD;
	}

	public String getCobertura() {
		return cobertura;
	}

	public void setCobertura(String cobertura) {
		this.cobertura = cobertura;
	}

	public String getSubcobertura() {
		return subcobertura;
	}

	public void setSubcobertura(String subcobertura) {
		this.subcobertura = subcobertura;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}	
	
}
