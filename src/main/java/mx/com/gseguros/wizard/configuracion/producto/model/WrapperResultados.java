package mx.com.gseguros.wizard.configuracion.producto.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;



public class WrapperResultados implements Serializable {

		
	private int msgId;
	
	private String msg;
	
	private String msgLabel;
	
	@SuppressWarnings("unchecked")
	private List itemList;
	
	private Map<String,Object> itemMap; 

	/**
	 * UID por defecto
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @return the msgId
	 */
	public int getMsgId() {
		return msgId;
	}

	/**
	 * @param msgId the msgId to set
	 */
	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the itemList
	 */
	@SuppressWarnings("unchecked")
	public List getItemList() {
		return itemList;
	}

	/**
	 * @param itemList the itemList to set
	 */
	@SuppressWarnings("unchecked")
	public void setItemList(List itemList) {
		this.itemList = itemList;
	}

	/**
	 * @return the itemMap
	 */
	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	/**
	 * @param itemMap the itemMap to set
	 */
	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

	/**
	 * @return the msgLabel
	 */
	public String getMsgLabel() {
		return msgLabel;
	}

	/**
	 * @param msgLabel the msgLabel to set
	 */
	public void setMsgLabel(String msgLabel) {
		this.msgLabel = msgLabel;
	}


}
