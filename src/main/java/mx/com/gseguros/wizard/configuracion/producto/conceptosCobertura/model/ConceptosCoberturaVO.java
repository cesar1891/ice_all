package mx.com.gseguros.wizard.configuracion.producto.conceptosCobertura.model;

import java.io.Serializable;

public class ConceptosCoberturaVO implements Serializable {

	
	private static final long serialVersionUID = 7149537862815177617L;

	/**
	 * Codigo del periodo para el concepto por cobertura.
	 */
	private String codigoPeriodo;

	/**
	 * Descripcion del periodo para concepto por cobertura .
	 */
	private String descripcionPeriodo;
	
	/**
	 * Codigo de la cobertura para el concepto por cobertura.
	 */
	private String codigoCobertura;

	/**
	 * Descripcion de la cobertura para concepto por cobertura .
	 */
	private String descripcionCobertura;
	
	/**
	 * Codigo del concepto para el concepto por cobertura.
	 */
	private String codigoConcepto;

	/**
	 * Descripcion del concepto para concepto por cobertura .
	 */
	private String descripcionConcepto;
	
	/**
	 * Codigo del comportamiento para el concepto por cobertura.
	 */
	private String codigoComportamiento;

	/**
	 * Descripcion del comportamiento para concepto por cobertura .
	 */
	private String descripcionComportamiento;
	
	/**
	 * Codigo de la condicion para el concepto por cobertura.
	 */
	private String codigoCondicion;

	/**
	 * Descripcion de la condicion para concepto por cobertura .
	 */
	private String descripcionCondicion;
	
	/**
	 * orden para concepto por cobertura.
	 */
	private String orden;
	private String cdtipcon;
	private String dstipcon;
	private String cdexpres;

	
	//GETTERS Y SETTERS
	public String getCodigoPeriodo() {
		return codigoPeriodo;
	}

	public void setCodigoPeriodo(String codigoPeriodo) {
		this.codigoPeriodo = codigoPeriodo;
	}

	public String getDescripcionPeriodo() {
		return descripcionPeriodo;
	}

	public void setDescripcionPeriodo(String descripcionPeriodo) {
		this.descripcionPeriodo = descripcionPeriodo;
	}

	public String getCodigoCobertura() {
		return codigoCobertura;
	}

	public void setCodigoCobertura(String codigoCobertura) {
		this.codigoCobertura = codigoCobertura;
	}

	public String getDescripcionCobertura() {
		return descripcionCobertura;
	}

	public void setDescripcionCobertura(String descripcionCobertura) {
		this.descripcionCobertura = descripcionCobertura;
	}

	public String getCodigoConcepto() {
		return codigoConcepto;
	}

	public void setCodigoConcepto(String codigoConcepto) {
		this.codigoConcepto = codigoConcepto;
	}

	public String getDescripcionConcepto() {
		return descripcionConcepto;
	}

	public void setDescripcionConcepto(String descripcionConcepto) {
		this.descripcionConcepto = descripcionConcepto;
	}

	public String getCodigoComportamiento() {
		return codigoComportamiento;
	}

	public void setCodigoComportamiento(String codigoComportamiento) {
		this.codigoComportamiento = codigoComportamiento;
	}

	public String getDescripcionComportamiento() {
		return descripcionComportamiento;
	}

	public void setDescripcionComportamiento(String descripcionComportamiento) {
		this.descripcionComportamiento = descripcionComportamiento;
	}

	public String getCodigoCondicion() {
		return codigoCondicion;
	}

	public void setCodigoCondicion(String codigoCondicion) {
		this.codigoCondicion = codigoCondicion;
	}

	public String getDescripcionCondicion() {
		return descripcionCondicion;
	}

	public void setDescripcionCondicion(String descripcionCondicion) {
		this.descripcionCondicion = descripcionCondicion;
	}

	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}

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

	public String getCdexpres() {
		return cdexpres;
	}

	public void setCdexpres(String cdexpres) {
		this.cdexpres = cdexpres;
	}
}
