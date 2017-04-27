package mx.com.gseguros.ws.model;


import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class WrapperResultadosWS implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Objeto Resultante del llamado a un WS
     */
    private Object resultadoWS;
	
    /**
	 * Xml de envio o entrada al WS
	 */
	private String xmlIn;
	
	/**
	 * Xml de respuesta o salida del WS
	 */
	private String xmlOut;
 
	    
	public Object getResultadoWS() {
		return resultadoWS;
	}


	public void setResultadoWS(Object resultadoWS) {
		this.resultadoWS = resultadoWS;
	}


	public String getXmlIn() {
		return xmlIn;
	}


	public void setXmlIn(String xmlIn) {
		this.xmlIn = xmlIn;
	}


	public String getXmlOut() {
		return xmlOut;
	}


	public void setXmlOut(String xmlOut) {
		this.xmlOut = xmlOut;
	}
	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
