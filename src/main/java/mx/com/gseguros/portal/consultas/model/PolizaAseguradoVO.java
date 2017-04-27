package mx.com.gseguros.portal.consultas.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author hector.lopez
 *
 */
public class PolizaAseguradoVO implements Serializable {

	private static final long serialVersionUID = -8555353864912795413L;

	private String cdunieco;
	private String dsunieco;
	private String cdramo;
	private String dsramo;
	private String estado;
	private String nmpoliza;
	private String nombreAsegurado;
	private String nombreAgente;
	private String nmpoliex;
	private String nmsuplem;
	private String cdsisrol;
	
	
	public String getCdsisrol() {
		return cdsisrol;
	}

	public void setCdsisrol(String cdsisrol) {
		this.cdsisrol = cdsisrol;
	}

	public String getNmsuplem() {
		return nmsuplem;
	}

	public void setNmsuplem(String nmsuplem) {
		this.nmsuplem = nmsuplem;
	}

	/**
	 * Descripci&oacute;n del Tipo de Producto
	 */
	private String dstipsit;
	
	/**
	 * C&oacute;digo del Tipo de Producto
	 */
	private String cdtipsit;
	
	/**
	 * C&oacute;digo del Subramo
	 */
	private String cdsubram;
	
	/**
	 * C&oacute;digo de la p&oacute;liza usado en SISA 
	 */
	private String icodpoliza;
	
	/**
	 * Origen de la p&oacute;liza: SISA, ICE, etc.
	 */
	private String origen;
	
	/**
	 * Vigencia
	 */
	private String feinivigencia;
	private String fefinvigencia;
	
	
	
	public String getCdunieco() {
		return cdunieco;
	}

	public void setCdunieco(String cdunieco) {
		this.cdunieco = cdunieco;
	}

	public String getDsunieco() {
		return dsunieco;
	}

	public void setDsunieco(String dsunieco) {
		this.dsunieco = dsunieco;
	}

	public String getCdramo() {
		return cdramo;
	}

	public void setCdramo(String cdramo) {
		this.cdramo = cdramo;
	}

	public String getDsramo() {
		return dsramo;
	}

	public void setDsramo(String dsramo) {
		this.dsramo = dsramo;
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

	public String getNombreAsegurado() {
		return nombreAsegurado;
	}

	public void setNombreAsegurado(String nombreAsegurado) {
		this.nombreAsegurado = nombreAsegurado;
	}

	public String getNmpoliex() {
		return nmpoliex;
	}

	public void setNmpoliex(String nmpoliex) {
		this.nmpoliex = nmpoliex;
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
	
	public String getFeinivigencia() {
		return feinivigencia;
	}

	public void setFeinivigencia(String feinivigencia) {
		this.feinivigencia = feinivigencia;
	}

	public String getFefinvigencia() {
		return fefinvigencia;
	}

	public void setFefinvigencia(String fefinvigencia) {
		this.fefinvigencia = fefinvigencia;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}

	public String getCdtipsit() {
		return cdtipsit;
	}

	public void setCdtipsit(String cdtipsit) {
		this.cdtipsit = cdtipsit;
	}

	public String getCdsubram() {
		return cdsubram;
	}

	public void setCdsubram(String cdsubram) {
		this.cdsubram = cdsubram;
	}

	public String getDstipsit() {
		return dstipsit;
	}

	public void setDstipsit(String dstipsit) {
		this.dstipsit = dstipsit;
	}

	public String getNombreAgente() {
		return nombreAgente;
	}

	public void setNombreAgente(String nombreAgente) {
		this.nombreAgente = nombreAgente;
	}	
		
}