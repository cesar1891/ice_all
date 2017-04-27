package mx.com.gseguros.portal.general.model;

import java.io.Serializable;

import mx.com.gseguros.utils.Utils;

public class BaseVO implements Serializable {

	private static final long serialVersionUID = 7528394434792370991L;

	private String key;
	
	private String value;

	public BaseVO() {
		super();
	}

	public BaseVO(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return Utils.log("key:",key," value:",value);
	}
}
