package mx.com.gseguros.wizard.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ClaveVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7064429733644094174L;
	private String idBase;
	private String clave;
	private String recalcular;
	private boolean switchRecalcualar;
	private String expresion;
	private String codigoExpresionKey;
	private String codigoSecuencia;
	private String codigoVariable;
	/**
	 * @return the clave
	 */
	public String getClave() {
		return clave;
	}
	/**
	 * @param clave the clave to set
	 */
	public void setClave(String clave) {
		this.clave = clave;
	}
	/**
	 * @return the recalcular
	 */
	public String getRecalcular() {
		return recalcular;
	}
	/**
	 * @param recalcular the recalcular to set
	 */
	public void setRecalcular(String recalcular) {
		this.recalcular = recalcular;
	}
	/**
	 * @return the expresion
	 */
	public String getExpresion() {
		return expresion;
	}
	/**
	 * @param expresion the expresion to set
	 */
	public void setExpresion(String expresion) {
		this.expresion = expresion;
	}
	/**
	 * @return the idBase
	 */
	public String getIdBase() {
		return idBase;
	}
	/**
	 * @param idBase the idBase to set
	 */
	public void setIdBase(String idBase) {
		this.idBase = idBase;
	}
	/**
	 * @return the switchRecalcualar
	 */
	public boolean isSwitchRecalcualar() {
		return switchRecalcualar;
	}
	/**
	 * @param switchRecalcualar the switchRecalcualar to set
	 */
	public void setSwitchRecalcualar(boolean switchRecalcualar) {
		this.switchRecalcualar = switchRecalcualar;
	}
	/**
	 * @return the codigoExpresionKey
	 */
	public String getCodigoExpresionKey() {
		return codigoExpresionKey;
	}
	/**
	 * @param codigoExpresionKey the codigoExpresionKey to set
	 */
	public void setCodigoExpresionKey(String codigoExpresionKey) {
		this.codigoExpresionKey = codigoExpresionKey;
	}
	/**
	 * @return the codigoSecuencia
	 */
	public String getCodigoSecuencia() {
		return codigoSecuencia;
	}
	/**
	 * @param codigoSecuencia the codigoSecuencia to set
	 */
	public void setCodigoSecuencia(String codigoSecuencia) {
		this.codigoSecuencia = codigoSecuencia;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	public String getCodigoVariable() {
		return codigoVariable;
	}
	public void setCodigoVariable(String codigoVariable) {
		this.codigoVariable = codigoVariable;
	}
}
