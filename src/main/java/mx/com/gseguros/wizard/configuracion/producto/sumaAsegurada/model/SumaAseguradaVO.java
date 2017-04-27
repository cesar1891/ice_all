package mx.com.gseguros.wizard.configuracion.producto.sumaAsegurada.model;

import java.io.Serializable;

public class SumaAseguradaVO implements Serializable {
	
	private static final long serialVersionUID = -8305391557932646835L;
	
	
	/**
	 * Codigo de ramo para la suma asegurada asociada al producto o inciso.
	 */
	private String codigoRamo;
	/**
	 * Codigo de capital asociado a la suma asegurada del producto o inciso.
	 */
	private String codigoCapital;
	/**
	 * descripcion de capital asociado a la suma asegurada del producto o inciso.
	 */
	private String descripcionCapital;
	/**
	 * Codigo de tipo de capital asociado a la suma asegurada del producto o inciso.
	 */
	private String codigoTipoCapital;
	
	/**
	 * Descripcion de tipo de capital asociado a la suma asegurada del producto o inciso.
	 */
	private String descripcionTipoCapital;
	/**
	 * Codigo de moneda asociado a la suma asegurada del producto o inciso.
	 */
	private String codigoMoneda;

	/**
	 * Descripcion de moneda asociada a la suma asegurada del producto o inciso.
	 */
	private String descripcionMoneda;

	
	
	public String getCodigoRamo() {
		return codigoRamo;
	}

	public void setCodigoRamo(String codigoRamo) {
		this.codigoRamo = codigoRamo;
	}

	public String getDescripcionCapital() {
		return descripcionCapital;
	}

	public void setDescripcionCapital(String descripcionCapital) {
		this.descripcionCapital = descripcionCapital;
	}

	public String getCodigoTipoCapital() {
		return codigoTipoCapital;
	}

	public void setCodigoTipoCapital(String codigoTipoCapital) {
		this.codigoTipoCapital = codigoTipoCapital;
	}

	public String getCodigoMoneda() {
		return codigoMoneda;
	}

	public void setCodigoMoneda(String codigoMoneda) {
		this.codigoMoneda = codigoMoneda;
	}

	public String getDescripcionMoneda() {
		return descripcionMoneda;
	}

	public void setDescripcionMoneda(String descripcionMoneda) {
		this.descripcionMoneda = descripcionMoneda;
	}

	public String getCodigoCapital() {
		return codigoCapital;
	}

	public void setCodigoCapital(String codigoCapital) {
		this.codigoCapital = codigoCapital;
	}

	public String getDescripcionTipoCapital() {
		return descripcionTipoCapital;
	}

	public void setDescripcionTipoCapital(String descripcionTipoCapital) {
		this.descripcionTipoCapital = descripcionTipoCapital;
	}
}
