package mx.com.gseguros.wizard.configuracion.producto.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Edgar Perez
 * @version 2.0
 * @since 1.0
 *Clase que contiene atributos de la pantalla Lista de Valores, getters y setters 
 */
public class ListaDeValoresVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String nombre;
	private String descripcion;
	private String numeroTabla;
	private String enviarTablaErrores;
	private String modificaValores;
	private String cdCatalogo1;
	private String dsCatalogo1;
	private String cdCatalogo2;
	private String dsCatalogo2;
	private String claveDependencia;	
	private String ottipoac;
	private String ottipotb;
	private String clnatura;
	private String dsNatura;
	private String descripcionFormato;
	private String formatoDescripcion;
	private String minimoDescripcion;
	private String maximoDescripcion;
	//private String tipTran;
	private String cdAtribu;
	private String clave;
	private String formatoClave;
	private String minimoClave;
	private String maximoClave;
	
	/*private String clave2;
	private String formatoClave2;
	private String minimoClave2;
	private String maximoClave2;
	private String clave3;
	private String formatoClave3;
	private String minimoClave3;
	private String maximoClave3;
	private String clave4;
	private String formatoClave4;
	private String minimoClave4;
	private String maximoClave4;
	private String clave5;
	private String formatoClave5;
	private String minimoClave5;
	private String maximoClave5;
	*/
	//private String otClave;
	//private String otValor;
	
	
	/**
	 * 
	 * @return el tipo de transferencia
	 */
	/*public String getTipTran() {
		return tipTran;
	}*/
	/**
	 * 
	 * @param tipTran tipo de transferencia para el set
	 */
	/*public void setTipTran(String tipTran) {
		this.tipTran = tipTran;
	}*/
	/**
	 * 
	 * @return nombre
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * 
	 * @param nombre para el set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * 
	 * @return descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * 
	 * @param descripcion para el set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * 
	 * @return numero de la tabla
	 */
	public String getNumeroTabla() {
		return numeroTabla;
	}
	/**
	 * 
	 * @param numeroTabla para el set
	 */
	public void setNumeroTabla(String numeroTabla) {
		this.numeroTabla = numeroTabla;
	}
	/**
	 * 
	 * @return enviar tabla de errores
	 */
	public String getEnviarTablaErrores() {
		return enviarTablaErrores;
	}
	/**
	 * 
	 * @param enviarTablaErrores para el set
	 */
	public void setEnviarTablaErrores(String enviarTablaErrores) {
		this.enviarTablaErrores = enviarTablaErrores;
	}
	/**
	 * 
	 * @return modifica valores
	 */
	public String getModificaValores() {
		return modificaValores;
	}
	/**
	 * 
	 * @param modificaValores para el set
	 */
	public void setModificaValores(String modificaValores) {
		this.modificaValores = modificaValores;
	}
	/**
	 * 
	 * @return codigo del catalogo 1
	 */
	public String getCdCatalogo1() {
		return cdCatalogo1;
	}
	/**
	 * 
	 * @param cdCatalogo1 para el set
	 */
	public void setCdCatalogo1(String cdCatalogo1) {
		this.cdCatalogo1 = cdCatalogo1;
	}
	/**
	 * 
	 * @return descripcion del catalogo 1
	 */
	public String getDsCatalogo1() {
		return dsCatalogo1;
	}
	/**
	 * 
	 * @param dsCatalogo1 para el set
	 */
	public void setDsCatalogo1(String dsCatalogo1) {
		this.dsCatalogo1 = dsCatalogo1;
	}
	/**
	 * 
	 * @return codigo del catalogo 2
	 */
	public String getCdCatalogo2() {
		return cdCatalogo2;
	}
	/**
	 * 
	 * @param cdCatalogo2 para el set
	 */
	public void setCdCatalogo2(String cdCatalogo2) {
		this.cdCatalogo2 = cdCatalogo2;
	}
	/**
	 * 
	 * @return descripcion del catalogo 2
	 */
	public String getDsCatalogo2() {
		return dsCatalogo2;
	}
	/**
	 * 
	 * @param dsCatalogo2 para el set
	 */
	public void setDsCatalogo2(String dsCatalogo2) {
		this.dsCatalogo2 = dsCatalogo2;
	}
	
	/**
	 * 
	 * @return clave
	 */
	public String getClave() {
		return clave;
	}
	
	/**
	 * 
	 * @param clave para el set
	 */
	public void setClave(String clave) {
		this.clave = clave;
	}
	
	/**
	 * 
	 * @return tipo de asociacion
	 */
	public String getOttipoac() {
		return ottipoac;
	}
	/**
	 * 
	 * @param ottipoac para el set
	 */
	public void setOttipoac(String ottipoac) {
		this.ottipoac = ottipoac;
	}
	/**
	 * 
	 * @return tipo de tabla
	 */
	public String getOttipotb() {
		return ottipotb;
	}
	/**
	 * 
	 * @param ottipotb para el set
	 */
	public void setOttipotb(String ottipotb) {
		this.ottipotb = ottipotb;
	}
	/**
	 * 
	 * @return clnatura
	 */
	public String getClnatura() {
		return clnatura;
	}
	/**
	 * 
	 * @param clnatura para el set
	 */
	public void setClnatura(String clnatura) {
		this.clnatura = clnatura;
	}
	/**
	 * 
	 * @return clave de la dependencia
	 */
	public String getClaveDependencia() {
		return claveDependencia;
	}
	/**
	 * 
	 * @param claveDependencia para el set
	 */
	public void setClaveDependencia(String claveDependencia) {
		this.claveDependencia = claveDependencia;
	}
	/**
	 * 
	 * @return formato de la clave
	 */
	public String getFormatoClave() {
		return formatoClave;
	}
	/**
	 * 
	 * @param formatoClave para el set
	 */
	public void setFormatoClave(String formatoClave) {
		this.formatoClave = formatoClave;
	}
	/**
	 * 
	 * @return minimo de la clave
	 */
	public String getMinimoClave() {
		return minimoClave;
	}
	/**
	 * 
	 * @param minimoClave para el set
	 */
	public void setMinimoClave(String minimoClave) {
		this.minimoClave = minimoClave;
	}
	/**
	 * 
	 * @return maximo de la clave
	 */
	public String getMaximoClave() {
		return maximoClave;
	}
	/**
	 * 
	 * @param maximoClave para el set
	 */
	public void setMaximoClave(String maximoClave) {
		this.maximoClave = maximoClave;
	}
	/**
	 * 
	 * @return descripcion
	 */
	public String getDescripcionFormato() {
		return descripcionFormato;
	}
	/**
	 * 
	 * @param descripcionFormato para el set
	 */
	public void setDescripcionFormato(String descripcionFormato) {
		this.descripcionFormato = descripcionFormato;
	}
	/**
	 * 
	 * @return formato de la descripcion
	 */
	public String getFormatoDescripcion() {
		return formatoDescripcion;
	}
	/**
	 * 
	 * @param formatoDescripcion para el set
	 */
	public void setFormatoDescripcion(String formatoDescripcion) {
		this.formatoDescripcion = formatoDescripcion;
	}
	/**
	 * 
	 * @return minimo de la descripcion
	 */
	public String getMinimoDescripcion() {
		return minimoDescripcion;
	}
	/**
	 * 
	 * @param minimoDescripcion del set
	 */
	public void setMinimoDescripcion(String minimoDescripcion) {
		this.minimoDescripcion = minimoDescripcion;
	}
	/**
	 * 
	 * @return maximo de la descripcion
	 */
	public String getMaximoDescripcion() {
		return maximoDescripcion;
	}
	/**
	 * 
	 * @param maximoDescripcion del set
	 */
	public void setMaximoDescripcion(String maximoDescripcion) {
		this.maximoDescripcion = maximoDescripcion;
	}
	/**
	 * 
	 * @return codigo del atributo
	 */
	public String getCdAtribu() {
		return cdAtribu;
	}
	/**
	 * 
	 * @param cdAtribu del set
	 */
	public void setCdAtribu(String cdAtribu) {
		this.cdAtribu = cdAtribu;
	}
	/*
	public String getOtClave() {
		return otClave;
	}
	public void setOtClave(String otClave) {
		this.otClave = otClave;
	}
	public String getOtValor() {
		return otValor;
	}
	public void setOtValor(String otValor) {
		this.otValor = otValor;
	}
	
	public String getClave2() {
		return clave2;
	}
	public void setClave2(String clave2) {
		this.clave2 = clave2;
	}
	public String getFormatoClave2() {
		return formatoClave2;
	}
	public void setFormatoClave2(String formatoClave2) {
		this.formatoClave2 = formatoClave2;
	}
	public String getMinimoClave2() {
		return minimoClave2;
	}
	public void setMinimoClave2(String minimoClave2) {
		this.minimoClave2 = minimoClave2;
	}
	public String getMaximoClave2() {
		return maximoClave2;
	}
	public void setMaximoClave2(String maximoClave2) {
		this.maximoClave2 = maximoClave2;
	}
	public String getClave3() {
		return clave3;
	}
	public void setClave3(String clave3) {
		this.clave3 = clave3;
	}
	public String getFormatoClave3() {
		return formatoClave3;
	}
	public void setFormatoClave3(String formatoClave3) {
		this.formatoClave3 = formatoClave3;
	}
	public String getMinimoClave3() {
		return minimoClave3;
	}
	public void setMinimoClave3(String minimoClave3) {
		this.minimoClave3 = minimoClave3;
	}
	public String getMaximoClave3() {
		return maximoClave3;
	}
	public void setMaximoClave3(String maximoClave3) {
		this.maximoClave3 = maximoClave3;
	}
	public String getClave4() {
		return clave4;
	}
	public void setClave4(String clave4) {
		this.clave4 = clave4;
	}
	public String getFormatoClave4() {
		return formatoClave4;
	}
	public void setFormatoClave4(String formatoClave4) {
		this.formatoClave4 = formatoClave4;
	}
	public String getMinimoClave4() {
		return minimoClave4;
	}
	public void setMinimoClave4(String minimoClave4) {
		this.minimoClave4 = minimoClave4;
	}
	public String getMaximoClave4() {
		return maximoClave4;
	}
	public void setMaximoClave4(String maximoClave4) {
		this.maximoClave4 = maximoClave4;
	}
	public String getClave5() {
		return clave5;
	}
	public void setClave5(String clave5) {
		this.clave5 = clave5;
	}
	public String getFormatoClave5() {
		return formatoClave5;
	}
	public void setFormatoClave5(String formatoClave5) {
		this.formatoClave5 = formatoClave5;
	}
	public String getMinimoClave5() {
		return minimoClave5;
	}
	public void setMinimoClave5(String minimoClave5) {
		this.minimoClave5 = minimoClave5;
	}
	public String getMaximoClave5() {
		return maximoClave5;
	}
	public void setMaximoClave5(String maximoClave5) {
		this.maximoClave5 = maximoClave5;
	}
	*/
	public String getDsNatura() {
		return dsNatura;
	}
	public void setDsNatura(String dsNatura) {
		this.dsNatura = dsNatura;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
}
