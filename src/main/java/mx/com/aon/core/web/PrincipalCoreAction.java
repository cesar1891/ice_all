package mx.com.aon.core.web;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.json.JSONUtil;

import com.opensymphony.xwork2.ActionSupport;


public abstract class PrincipalCoreAction extends ActionSupport implements SessionAware {

    // RBS: Prueba de cambio de encoding: á Á é É i Í ó Ó u Ú
    
	private static final long serialVersionUID = -7975229967003917194L;

	protected final transient Logger logger = Logger.getLogger(PrincipalCoreAction.class);
	
	protected Map<String,Object> session;
	
	
	/**
	 * Convierte el valor de una propiedad a formato JSON 
	 * @param nombrePropiedad nombre de la propiedad del action que se quiere serializar a JSON
	 * @return Valor de la propiedad en formato JSON
	 */
	public String convertToJSON(String nombrePropiedad) {
		
		String strJSON = "";
		try {
			Object someObject = this;
			Field field = someObject.getClass().getDeclaredField(nombrePropiedad);
				field.setAccessible(true); // You might want to set modifier to public first.
				Object value;
				value = field.get(someObject);
		        //logger.debug(new StringBuilder(field.getName()).append("=").append(value));
		        strJSON = JSONUtil.serialize(value);
		        //logger.debug(new StringBuilder("convertToJSON ").append(nombrePropiedad).append("=").append(strJSON));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return strJSON;
	}
	
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
		
	}
	
}