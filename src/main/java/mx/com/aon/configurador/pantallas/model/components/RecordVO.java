package mx.com.aon.configurador.pantallas.model.components;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class RecordVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2482429634819102676L;

	private String name;
	
	private String type;
	
	private String mapping;
	
	
	

	
	//Constructors
	
	/*@Override
	public String toString() {
		
		return JSONObject.fromObject(this).toString();
	}*/

	public RecordVO(){
		
	}
	
	public RecordVO(String name, String type, String mapping){
		this.name = name;
		this.type = type;
		this.mapping = mapping;
	}
	
	//Getters y setters
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMapping() {
		return mapping;
	}

	public void setMapping(String mapping) {
		this.mapping = mapping;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}

}
