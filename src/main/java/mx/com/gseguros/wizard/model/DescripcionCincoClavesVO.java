package mx.com.gseguros.wizard.model;

import java.io.Serializable;
import java.util.List;

import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * @author Edgar Perez
 * @version 2.0
 * @since 1.0
 *Clase que contiene atributos de la tabla de cinco claves, getters y setters 
 */
public class DescripcionCincoClavesVO implements Serializable{
	
	private static final long serialVersionUID = 8679969555087372715L;
	private String identificador;
	private String identificadorTemporal;
	private String descripcionClave1;
	private String descripcionClave2;
	private String descripcionClave3;
	private String descripcionClave4;
	private String descripcionClave5;
	private String fechaDesde;
	private String fechaHasta;
	private List<LlaveValorVO> atributos;
	/**
	 * @return the descripcionClave1
	 */
	public String getDescripcionClave1() {
		return descripcionClave1;
	}
	/**
	 * @param descripcionClave1 the descripcionClave1 to set
	 */
	public void setDescripcionClave1(String descripcionClave1) {
		this.descripcionClave1 = descripcionClave1;
	}
	/**
	 * @return the descripcionClave2
	 */
	public String getDescripcionClave2() {
		return descripcionClave2;
	}
	/**
	 * @param descripcionClave2 the descripcionClave2 to set
	 */
	public void setDescripcionClave2(String descripcionClave2) {
		this.descripcionClave2 = descripcionClave2;
	}
	/**
	 * @return the descripcionClave3
	 */
	public String getDescripcionClave3() {
		return descripcionClave3;
	}
	/**
	 * @param descripcionClave3 the descripcionClave3 to set
	 */
	public void setDescripcionClave3(String descripcionClave3) {
		this.descripcionClave3 = descripcionClave3;
	}
	/**
	 * @return the descripcionClave4
	 */
	public String getDescripcionClave4() {
		return descripcionClave4;
	}
	/**
	 * @param descripcionClave4 the descripcionClave4 to set
	 */
	public void setDescripcionClave4(String descripcionClave4) {
		this.descripcionClave4 = descripcionClave4;
	}
	/**
	 * @return the descripcionClave5
	 */
	public String getDescripcionClave5() {
		return descripcionClave5;
	}
	/**
	 * @param descripcionClave5 the descripcionClave5 to set
	 */
	public void setDescripcionClave5(String descripcionClave5) {
		this.descripcionClave5 = descripcionClave5;
	}
	/**
	 * @return the atributos
	 */
	public List<LlaveValorVO> getAtributos() {
		return atributos;
	}
	/**
	 * @param atributos the atributos to set
	 */
	public void setAtributos(List<LlaveValorVO> atributos) {
		this.atributos = atributos;
	}
	/**
	 * 
	 * @return fechaDesde  
	 */
	public String getFechaDesde() {
		return fechaDesde;
	}
	/**
	 * 
	 * @param fechaDesde the fechaDesde to set
	 */
	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	/**
	 * 
	 * @return fechaHasta
	 */
	public String getFechaHasta() {
		return fechaHasta;
	}
	/**
	 * 
	 * @param fechaHasta the fechaHasta to set
	 */
	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	/**
	 * @return the identificador
	 */
	public String getIdentificador() {
		return identificador;
	}
	/**
	 * @param identificador the identificador to set
	 */
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	/**
	 * @return the identificadorTemporal
	 */
	public String getIdentificadorTemporal() {
		return identificadorTemporal;
	}
	/**
	 * @param identificadorTemporal the identificadorTemporal to set
	 */
	public void setIdentificadorTemporal(String identificadorTemporal) {
		this.identificadorTemporal = identificadorTemporal;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
}
