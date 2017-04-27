package mx.com.gseguros.portal.consultas.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author hector.lopez
 *
 */
public class HistoricoVO implements Serializable{

	
	private static final long serialVersionUID = -8555353864912795413L;

	/**
	 * Sucursal
	 */
	private String cdunieco;
	
	/**
	 * Ramo
	 */
	private String cdramo;
	
	/**
	 * Estado de la poliza
	 */
	private String estado;
	
	/**
	 * Numero de la poliza
	 */
	private String nmpoliza;
	
	/**
	 * Numero de Endoso-historico (formato largo)
	 */
	private String nmsuplem;
	
	/**
	 * Fecha de inicio de vigencia del endoso
	 */
	private String feinival;
	
	/**
	 * Fecha de fin de vigencia del endoso
	 */
	private String fefinval;
	
	public String getFefinval() {
		return fefinval;
	}
	public void setFefinval(String fefinval) {
		this.fefinval = fefinval;
	}
	/**
	 * Numero consecutivo de endoso (formato corto)
	 */
	private String nsuplogi;
	
	/**
	 * Fecha de emision de la poliza
	 */
	private String feemisio;
	
	/**
	 * TODO: // Investigar que es
	 */
	private String nlogisus;
	
	/**
	 * Nombre del tipo de endoso
	 */
	private String dstipsup;
	
	
	/**
	 * Importe de prima total
	 */
	private String ptpritot;
		
	/**
	 * Plan de la poliza 
	 */
	private String dsplan;
	
	/*
	 * Observaciones
	 */
	private String observa;
		
	public String getDsplan() {
		return dsplan;
	}
	public void setDsplan(String dsplan) {
		this.dsplan = dsplan;
	}
	/**
	 * C&oacute;digo de la p&oacute;liza usado en SISA 
	 */
	private String icodpoliza;
	
	/**
	 * Origen de la poliza (SISA, ICE, etc)
	 */
	private String origen;
	
	
	
	
	public String getNmsuplem() {
		return nmsuplem;
	}
	public void setNmsuplem(String nmsuplem) {
		this.nmsuplem = nmsuplem;
	}
	public String getFeinival() {
		return feinival;
	}
	public void setFeinival(String feinival) {
		this.feinival = feinival;
	}
	public String getNsuplogi() {
		return nsuplogi;
	}
	public void setNsuplogi(String nsuplogi) {
		this.nsuplogi = nsuplogi;
	}
	public String getFeemisio() {
		return feemisio;
	}
	public void setFeemisio(String feemisio) {
		this.feemisio = feemisio;
	}
	public String getNlogisus() {
		return nlogisus;
	}
	public void setNlogisus(String nlogisus) {
		this.nlogisus = nlogisus;
	}
	public String getDstipsup() {
		return dstipsup;
	}
	public void setDstipsup(String dstipsup) {
		this.dstipsup = dstipsup;
	}
	public String getPtpritot() {
		return ptpritot;
	}
	public void setPtpritot(String ptpritot) {
		this.ptpritot = ptpritot;
	}	
	
	public String getCdunieco() {
		return cdunieco;
	}
	public void setCdunieco(String cdunieco) {
		this.cdunieco = cdunieco;
	}
	public String getCdramo() {
		return cdramo;
	}
	public void setCdramo(String cdramo) {
		this.cdramo = cdramo;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getNmpoliza() {
		return nmpoliza;
	}
	public void setNmpoliza(String nmpoliza) {
		this.nmpoliza = nmpoliza;
	}
	public String getIcodpoliza() {
		return icodpoliza;
	}
	public void setIcodpoliza(String icodpoliza) {
		this.icodpoliza = icodpoliza;
	}
	public String getOrigen() {
		return origen;
	}
	public void setOrigen(String origen) {
		this.origen = origen;		
	}
	
	public String getObserva() {
		return observa;
	}
	public void setObserva(String observa) {
		this.observa = observa;
	}
	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
}
