package mx.com.aon.flujos.cotizacion.model;

import java.io.Serializable;

public class DatosEntradaCotizaVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4003531702936753817L;
	
	private String dsAtribu;
	
	private String otValor;
	
	private String dsNombre;
	
	private String dsValor;

	public String getDsAtribu() {
		return dsAtribu;
	}

	public void setDsAtribu(String dsAtribu) {
		this.dsAtribu = dsAtribu;
	}

	public String getOtValor() {
		return otValor;
	}

	public void setOtValor(String otValor) {
		this.otValor = otValor;
	}

	public String getDsNombre() {
		return dsNombre;
	}

	public void setDsNombre(String dsNombre) {
		this.dsNombre = dsNombre;
	}

	public String getDsValor() {
		return dsValor;
	}

	public void setDsValor(String dsValor) {
		this.dsValor = dsValor;
	}	
}
