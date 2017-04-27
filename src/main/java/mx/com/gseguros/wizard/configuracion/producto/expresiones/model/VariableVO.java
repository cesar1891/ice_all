package mx.com.gseguros.wizard.configuracion.producto.expresiones.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class VariableVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8700003310245137329L;
	private String idAction;
	private int codigoExpresion;
	private String codigoVariable;
	private String tabla;
	private String descripcionTabla;
	private String descripcionColumna;
	private int columna;
	private String switchFormato;
	private List<ClaveVO> claves;
	
	/**
	 * @return the tabla
	 */
	public String getTabla() {
		return tabla;
	}
	/**
	 * @param tabla the tabla to set
	 */
	public void setTabla(String tabla) {
		this.tabla = tabla;
	}
	/**
	 * @return the claves
	 */
	public List<ClaveVO> getClaves() {
		return claves;
	}
	/**
	 * @param claves the claves to set
	 */
	public void setClaves(List<ClaveVO> claves) {
		this.claves = claves;
	}
	/**
	 * @return the codigoExpresion
	 */
	public int getCodigoExpresion() {
		return codigoExpresion;
	}
	/**
	 * @param codigoExpresion the codigoExpresion to set
	 */
	public void setCodigoExpresion(int codigoExpresion) {
		this.codigoExpresion = codigoExpresion;
	}
	/**
	 * @return the codigoVariable
	 */
	public String getCodigoVariable() {
		return codigoVariable;
	}
	/**
	 * @param codigoVariable the codigoVariable to set
	 */
	public void setCodigoVariable(String codigoVariable) {
		this.codigoVariable = codigoVariable;
	}
	/**
	 * @return the columna
	 */
	public int getColumna() {
		return columna;
	}
	/**
	 * @param columna the columna to set
	 */
	public void setColumna(int columna) {
		this.columna = columna;
	}
	public String getDescripcionTabla() {
		return descripcionTabla;
	}
	public void setDescripcionTabla(String descripcionTabla) {
		this.descripcionTabla = descripcionTabla;
	}
	public String getDescripcionColumna() {
		return descripcionColumna;
	}
	public void setDescripcionColumna(String descripcionColumna) {
		this.descripcionColumna = descripcionColumna;
	}
	/**
	 * @return the idAction
	 */
	public String getIdAction() {
		return idAction;
	}
	/**
	 * @param idAction the idAction to set
	 */
	public void setIdAction(String idAction) {
		this.idAction = idAction;
	}
	/**
	 * @return the switchFormato
	 */
	public String getSwitchFormato() {
		return switchFormato;
	}
	/**
	 * @param switchFormato the switchFormato to set
	 */
	public void setSwitchFormato(String switchFormato) {
		this.switchFormato = switchFormato;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        VariableVO varVO = (VariableVO) obj;
        return codigoVariable.equals(varVO.codigoVariable);

	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(codigoExpresion).toHashCode();
	}

}
