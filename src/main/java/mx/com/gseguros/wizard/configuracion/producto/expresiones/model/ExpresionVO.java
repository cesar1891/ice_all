package mx.com.gseguros.wizard.configuracion.producto.expresiones.model;

import java.io.Serializable;
import java.util.List;

public class ExpresionVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -253116246741937373L;
	private int codigoExpresion;
	private String otExpresiones;
	private String otExpresion;
	private String switchRecalcular;
	private String ottiporg;
	private String ottipexp;
	private List<VariableVO> variablesTemporales;

	
	/**
	 * @return the otExpresiones
	 */
	public String getOtExpresiones() {
		return otExpresiones;
	}
	/**
	 * @param otExpresiones the otExpresiones to set
	 */
	public void setOtExpresiones(String otExpresiones) {
		this.otExpresiones = otExpresiones;
	}
	/**
	 * @return the otExpresion
	 */
	public String getOtExpresion() {
		return otExpresion;
	}
	/**
	 * @param otExpresion the otExpresion to set
	 */
	public void setOtExpresion(String otExpresion) {
		this.otExpresion = otExpresion;
	}
	/**
	 * @return the switchRecalcular
	 */
	public String getSwitchRecalcular() {
		return switchRecalcular;
	}
	/**
	 * @param switchRecalcular the switchRecalcular to set
	 */
	public void setSwitchRecalcular(String switchRecalcular) {
		this.switchRecalcular = switchRecalcular;
	}
	/**
	 * @return the variablesTemporales
	 */
	public List<VariableVO> getVariablesTemporales() {
		return variablesTemporales;
	}
	/**
	 * @param variablesTemporales the variablesTemporales to set
	 */
	public void setVariablesTemporales(List<VariableVO> variablesTemporales) {
		this.variablesTemporales = variablesTemporales;
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
	public String getOttiporg() {
		return ottiporg;
	}
	public void setOttiporg(String ottiporg) {
		this.ottiporg = ottiporg;
	}
	public String getOttipexp() {
		return ottipexp;
	}
	public void setOttipexp(String ottipexp) {
		this.ottipexp = ottipexp;
	}

}
