package mx.com.gseguros.confpantallas.model;

public class DinamicItemBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public String key;
	public String value;
	
	public DinamicItemBean(){	
		super();
	}
	
	public DinamicItemBean(String key, String value) {
		this.key = (key!=null)?key.trim():"";
		this.value = (value!=null)?value.trim():"";
	}
	
	public String getKey() {
		return (key!=null)?key.trim():"";
	}
	
	public void setKey(String key) {
		this.key = key.trim();
	}
	public String getValue() {
		return (value!=null)?value.trim():"";
	}
	
	public void setValue(String value) {
		this.value = value.trim();
	}
}

