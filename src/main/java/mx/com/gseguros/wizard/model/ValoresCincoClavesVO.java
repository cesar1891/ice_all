package mx.com.gseguros.wizard.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class ValoresCincoClavesVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 456583666939508951L;
	private String nombre;
	private String descripcion;
	private int numero;
	private String tipoTransito;
	private String fechaDesde;
	private String fechaHasta;
	private String fechaDesdeAnterior;
	private String fechaHastaAnterior;
	private DescripcionCincoClavesVO descripcionCincoClaves;
	private DescripcionCincoClavesVO descripcionCincoClavesAnterior;
	private DescripcionVeinticincoAtributosVO descripcionVeinticincoAtributos;
	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
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
	 * @return the numero
	 */
	public int getNumero() {
		return numero;
	}
	/**
	 * @param numero the numero to set
	 */
	public void setNumero(int numero) {
		this.numero = numero;
	}
	/**
	 * @return the fechaDesde
	 */
	public String getFechaDesde() {
		return fechaDesde;
	}
	/**
	 * @param fechaDesde the fechaDesde to set
	 */
	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	/**
	 * @return the fechaHasta
	 */
	public String getFechaHasta() {
		return fechaHasta;
	}
	/**
	 * @param fechaHasta the fechaHasta to set
	 */
	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	/**
	 * @return the fechaDesdeAnterior
	 */
	public String getFechaDesdeAnterior() {
		return fechaDesdeAnterior;
	}
	/**
	 * @param fechaDesdeAnterior the fechaDesdeAnterior to set
	 */
	public void setFechaDesdeAnterior(String fechaDesdeAnterior) {
		this.fechaDesdeAnterior = fechaDesdeAnterior;
	}
	/**
	 * @return the fechahastaAnterior
	 */
	public String getFechaHastaAnterior() {
		return fechaHastaAnterior;
	}
	/**
	 * @param fechahastaAnterior the fechahastaAnterior to set
	 */
	public void setFechaHastaAnterior(String fechahastaAnterior) {
		this.fechaHastaAnterior = fechahastaAnterior;
	}
	/**
	 * @return the descripcionCincoClaves
	 */
	public DescripcionCincoClavesVO getDescripcionCincoClaves() {
		return descripcionCincoClaves;
	}
	/**
	 * @param descripcionCincoClaves the descripcionCincoClaves to set
	 */
	public void setDescripcionCincoClaves(DescripcionCincoClavesVO descripcionCincoClaves) {
		this.descripcionCincoClaves = descripcionCincoClaves;
	}
	/**
	 * @return the descripcionCincoClavesAnterior
	 */
	public DescripcionCincoClavesVO getDescripcionCincoClavesAnterior() {
		return descripcionCincoClavesAnterior;
	}
	/**
	 * @param descripcionCincoClavesAnterior the descripcionCincoClavesAnterior to set
	 */
	public void setDescripcionCincoClavesAnterior(
			DescripcionCincoClavesVO descripcionCincoClavesAnterior) {
		this.descripcionCincoClavesAnterior = descripcionCincoClavesAnterior;
	}
	/**
	 * @return the descripcionVeinticincoAtributos
	 */
	public DescripcionVeinticincoAtributosVO getDescripcionVeinticincoAtributos() {
		return descripcionVeinticincoAtributos;
	}
	/**
	 * @param descripcionVeinticincoAtributos the descripcionVeinticincoAtributos to set
	 */
	public void setDescripcionVeinticincoAtributos(
			DescripcionVeinticincoAtributosVO descripcionVeinticincoAtributos) {
		this.descripcionVeinticincoAtributos = descripcionVeinticincoAtributos;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	public String getTipoTransito() {
		return tipoTransito;
	}
	public void setTipoTransito(String tipoTransito) {
		this.tipoTransito = tipoTransito;
	}

}
