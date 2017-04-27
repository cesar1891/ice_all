package mx.com.aon.portal.model;

import java.io.Serializable;
/**
 * 
 * @author sergio.ramirez
 *
 */
public class PrincipalVO implements Serializable{

	/**
	 *Serial Version 
	 */
	private static final long serialVersionUID = -4552038050466767691L;
	/**
	 *Atributos declarados para ser mostrados en la pantalla principal dependiendo
	 *configuracion de cada cliente. 
	 */
	private String tipoDato;
	private String especificacionAction;
	private String seccionPantalla;
	private String contenidoBlob;
	public String getTipoDato() {
		return tipoDato;
	}
	public void setTipoDato(String tipoDato) {
		this.tipoDato = tipoDato;
	}
	public String getEspecificacionAction() {
		return especificacionAction;
	}
	public void setEspecificacionAction(String especificacionAction) {
		this.especificacionAction = especificacionAction;
	}
	public String getSeccionPantalla() {
		return seccionPantalla;
	}
	public void setSeccionPantalla(String seccionPantalla) {
		this.seccionPantalla = seccionPantalla;
	}
	public String getContenidoBlob() {
		return contenidoBlob;
	}
	public void setContenidoBlob(String contenidoBlob) {
		this.contenidoBlob = contenidoBlob;
	}
	
	
	

}
