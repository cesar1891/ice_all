package mx.com.aon.portal.model.principal;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
/**
 * 
 * @author sergio.ramirez
 *
 */
public class TipoVO implements Serializable {

	/**
	 *Serial Version 
	 */
	private static final long serialVersionUID = -6824887235493316862L;
	
	private String clave;
	private String valor;
	/**
	 * 
	 * @return
	 */
	public String getClave() {
		return clave;
	}
	/**
	 * 
	 * @param clave
	 */
	public void setClave(String clave) {
		this.clave = clave;
	}
	/**
	 * 
	 * @return
	 */
	public String getValor() {
		return valor;
	}
	/**
	 * 
	 * @param valor
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}
	/**
	 * 
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("clave", clave)
				.append("valor", valor).toString();

	}
}
