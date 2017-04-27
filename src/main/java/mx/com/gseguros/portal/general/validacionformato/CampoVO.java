package mx.com.gseguros.portal.general.validacionformato;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CampoVO {

	/**
	 * Valor alfanumerico
	 */
	public static final String ALFANUMERICO = "A";
	/**
	 * Valor numerico entero
	 */
	public static final String NUMERICO = "N";
	/**
	 * Valor con punto decimal
	 */
	public static final String PORCENTAJE = "P";
	/**
	 * Valor de fecha
	 */
	public static final String FECHA = "F";
	
	
	public CampoVO() {
		super();
	}
	
	public CampoVO(String type, Integer minLength, Integer maxLength, boolean nullable) {
		super();
		this.type = type;
		this.minLength = minLength;
		this.maxLength = maxLength;
		this.nullable = nullable;
	}
	
	public CampoVO(String type, Integer minLength, Integer maxLength, boolean nullable, String dateFormat) {
		this(type, minLength, maxLength, nullable);
		this.dateFormat = dateFormat;
	}


	private String type;
	
	private Integer minLength;
	
	private Integer maxLength;
	
	private Integer minValue;
	
	private Integer maxValue;
	
	private boolean nullable;
	
	private String dateFormat;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getMinLength() {
		return minLength;
	}

	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public Integer getMinValue() {
		return minValue;
	}

	public void setMinValue(Integer minValue) {
		this.minValue = minValue;
	}

	public Integer getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Integer maxValue) {
		this.maxValue = maxValue;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
}