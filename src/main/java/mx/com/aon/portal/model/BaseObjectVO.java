package mx.com.aon.portal.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class BaseObjectVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2524616201209878356L;
	
	/**
	 * Atributo que es un valor 
	 */
	private String value;
	
	/**
	 * Atributo que es visible,
	 */
	private String label;
	
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
	

}
