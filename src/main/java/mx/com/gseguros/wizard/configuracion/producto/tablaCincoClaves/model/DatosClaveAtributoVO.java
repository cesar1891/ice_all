package mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class DatosClaveAtributoVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7603323219978212954L;
	private int numeroClave;
	private String descripcion;
	private String formato;
	private String descripcionFormato;
	private String maximo;
	private String minimo;
	private String numeroTabla;
	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return the formato
	 */
	public String getFormato() {
		return formato;
	}
	/**
	 * @param formato the formato to set
	 */
	public void setFormato(String formato) {
		this.formato = formato;
	}
	/**
	 * @return the numeroClave
	 */
	public int getNumeroClave() {
		return numeroClave;
	}
	/**
	 * @param numeroClave the numeroClave to set
	 */
	public void setNumeroClave(int numeroClave) {
		this.numeroClave = numeroClave;
	}
	/**
	 * @return the maximo
	 */
	public String getMaximo() {
		return maximo;
	}
	/**
	 * @param maximo the maximo to set
	 */
	public void setMaximo(String maximo) {
		this.maximo = maximo;
	}
	/**
	 * @return the minimo
	 */
	public String getMinimo() {
		return minimo;
	}
	/**
	 * @param minimo the minimo to set
	 */
	public void setMinimo(String minimo) {
		this.minimo = minimo;
	}
	/**
	 * @return the descripcionFormato
	 */
	public String getDescripcionFormato() {
		return descripcionFormato;
	}
	/**
	 * @param descripcionFormato the descripcionFormato to set
	 */
	public void setDescripcionFormato(String descripcionFormato) {
		this.descripcionFormato = descripcionFormato;
	}
	public String getNumeroTabla() {
		return numeroTabla;
	}
	public void setNumeroTabla(String numeroTabla) {
		this.numeroTabla = numeroTabla;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
}
