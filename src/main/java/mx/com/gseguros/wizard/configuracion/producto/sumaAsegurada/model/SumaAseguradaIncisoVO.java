package mx.com.gseguros.wizard.configuracion.producto.sumaAsegurada.model;

import java.io.Serializable;

public class SumaAseguradaIncisoVO implements Serializable {

	
	private static final long serialVersionUID = -7280429973281231980L;

	/**
	 * Codigo de ramo para la suma asegurada asociada al inciso.
	 */
	private String codigoRamo;
	
	/**
	 * Codigo de tipo de situacion para la suma asegurada asociada al inciso.
	 */
	private String codigoTipoSituacion;
	
	/**
	 * Codigo de ramo para la suma asegurada asociada al producto o inciso.
	 */
	private String codigoCapital;
	
	/**
	 * Descripcion de ramo para la suma asegurada asociada al producto o inciso.
	 */
	private String descripcionCapital;
	
	/**
	 * obligatoriedad de la suma asegurada para el inciso.
	 */
	//private String switchObligatorio;
	
	/**
	 * insercion de la suma asegurada para el inciso.
	 */
	//private String switchInserta;
	
	/**
	 * codigo de la expresion de la suma asegurada asociada a el inciso.
	 */
	private String codigoExpresion;
	
	/**
	 * codigo de lista de valores de la suma asegurada del inciso.
	 */
	private String codigoListaValor;
	
	/**
	 * descripcion de lista de valores de la suma asegurada del inciso.
	 */
	private String descripcionListaValor;
	
	/**
	 * requerimiento de la lista de valores de la suma asegurada para el inciso.
	 */
	//private String switchListaValor;

	private String codigoTipoCapital;
	private String descripcionTipoCapital;
	private String codigoLeyenda;
	private String descripcionLeyenda;
	private String switchReinstalacion;
	private String codigoCobertura;
	private String descripcionCobertura;
	//getters y setters
	public String getCodigoRamo() {
		return codigoRamo;
	}

	public void setCodigoRamo(String codigoRamo) {
		this.codigoRamo = codigoRamo;
	}

	public String getCodigoTipoSituacion() {
		return codigoTipoSituacion;
	}

	public void setCodigoTipoSituacion(String codigoTipoSituacion) {
		this.codigoTipoSituacion = codigoTipoSituacion;
	}

	public String getCodigoCapital() {
		return codigoCapital;
	}

	public void setCodigoCapital(String codigoCapital) {
		this.codigoCapital = codigoCapital;
	}
/*
	public String getSwitchObligatorio() {
		return switchObligatorio;
	}

	public void setSwitchObligatorio(String switchObligatorio) {
		this.switchObligatorio = switchObligatorio;
	}

	public String getSwitchInserta() {
		return switchInserta;
	}

	public void setSwitchInserta(String switchInserta) {
		this.switchInserta = switchInserta;
	}
*/
	public String getCodigoExpresion() {
		return codigoExpresion;
	}

	public void setCodigoExpresion(String codigoExpresion) {
		this.codigoExpresion = codigoExpresion;
	}

	public String getCodigoListaValor() {
		return codigoListaValor;
	}

	public void setCodigoListaValor(String codigoListaValor) {
		this.codigoListaValor = codigoListaValor;
	}

	public String getDescripcionListaValor() {
		return descripcionListaValor;
	}

	public void setDescripcionListaValor(String descripcionListaValor) {
		this.descripcionListaValor = descripcionListaValor;
	}
/*
	public String getSwitchListaValor() {
		return switchListaValor;
	}

	public void setSwitchListaValor(String switchListaValor) {
		this.switchListaValor = switchListaValor;
	}
*/
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

	public String getDescripcionTipoCapital() {
		return descripcionTipoCapital;
	}

	public void setDescripcionTipoCapital(String descripcionTipoCapital) {
		this.descripcionTipoCapital = descripcionTipoCapital;
	}

	public String getCodigoLeyenda() {
		return codigoLeyenda;
	}

	public void setCodigoLeyenda(String codigoLeyenda) {
		this.codigoLeyenda = codigoLeyenda;
	}

	public String getDescripcionLeyenda() {
		return descripcionLeyenda;
	}

	public void setDescripcionLeyenda(String descripcionLeyenda) {
		this.descripcionLeyenda = descripcionLeyenda;
	}

	public String getSwitchReinstalacion() {
		return switchReinstalacion;
	}

	public void setSwitchReinstalacion(String switchReinstalacion) {
		this.switchReinstalacion = switchReinstalacion;
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

	
}
