/**
 * 
 */
package mx.com.aon.tmp;

import java.io.Serializable;

/**
 * @author Alejandro Garcia
 * Clase generica para obtener los mensajes de los servicios.
 */
public class MensajesVO implements Serializable{
	
	private static final long serialVersionUID = -1782825571002172407L;
	private String msgId;
	private String msgText;
	private String title;
	private String text;
	
	
	/**
	 * @return the msgId
	 */
	public String getMsgId() {
		return msgId;
	}
	/**
	 * @param msgId the msgId to set
	 */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	/**
	 * @return the msgText
	 */
	public String getMsgText() {
		return msgText;
	}
	/**
	 * @param msgText the msgText to set
	 */
	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

}
