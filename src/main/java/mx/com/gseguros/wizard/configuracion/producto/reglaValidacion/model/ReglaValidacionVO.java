package mx.com.gseguros.wizard.configuracion.producto.reglaValidacion.model;

import java.io.Serializable;

public class ReglaValidacionVO implements Serializable {

	
	private static final long serialVersionUID = -5079771931817296283L;

	/**
	 * Codigo del bloque para la regla de validacion.
	 */
	private String codigoBloque;

	/**
	 * Descripcion del bloque para la regla de validacion.
	 */
	private String descripcionBloque;
	
	/**
	 * Codigo de validacion para la regla de validacion.
	 */
	private String codigoValidacion;

	/**
	 * Descripcion de validacion para la regla de validacion.
	 */
	private String descripcionValidacion;
	
	/**
	 * Codigo de condicion para la regla de validacion.
	 */
	private String codigoCondicion;

	/**
	 * Descripcion de condicion para la regla de validacion.
	 */
	private String descripcionCondicion;
	
	/**
	 * Secuencia para la regla de validacion.
	 */
	private String secuencia;

	
	//GETTERS Y SETTERS
	public String getCodigoBloque() {
		return codigoBloque;
	}

	public void setCodigoBloque(String codigoBloque) {
		this.codigoBloque = codigoBloque;
	}

	public String getDescripcionBloque() {
		return descripcionBloque;
	}

	public void setDescripcionBloque(String descripcionBloque) {
		this.descripcionBloque = descripcionBloque;
	}

	public String getCodigoValidacion() {
		return codigoValidacion;
	}

	public void setCodigoValidacion(String codigoValidacion) {
		this.codigoValidacion = codigoValidacion;
	}

	public String getDescripcionValidacion() {
		return descripcionValidacion;
	}

	public void setDescripcionValidacion(String descripcionValidacion) {
		this.descripcionValidacion = descripcionValidacion;
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

	public String getSecuencia() {
		return secuencia;
	}

	public void setSecuencia(String secuencia) {
		this.secuencia = secuencia;
	}

	

}
