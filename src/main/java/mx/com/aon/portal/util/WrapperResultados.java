package mx.com.aon.portal.util;


import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class WrapperResultados implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** tipos de error */
	private static final String MSGTITLE_ERROR = "1";
	private static final String MSGTITLE_INFO = "2";
	private static final String MSGTITLE_INTERROG = "3";
     
	/** identificador del mensaje resultante de la operacion **/
	private String msgId;
 
	/** Tipo de mensaje o error, resultante de la operacion **/
	private String msgTitle;
	
	private String msgText;
	
	/**En caso de estar paginado el resultado de la lista itemList, almacena el total de la lista original sin paginar */
	int notPagedTotalItems;
	
	/** Lista de resultados de consultas o cualquier cursor de salida */
	@SuppressWarnings("unchecked")
	private List itemList;
	 
	/** Mapa para colocar cualquier otro parametro de salida */
	private Map<String,Object> itemMap; 
     
     
     /**
      * Permite saber si el mensaje recibido fue de error
      */
     public boolean isMsgError(){
         return this.msgTitle.equals(MSGTITLE_ERROR);
     }
     /**
      * Permite saber si el mensaje recibido fue de error
      */
     public boolean isMsgInformation(){
         return this.msgTitle.equals(MSGTITLE_INFO);
     }
     /**
      * Permite saber si el mensaje recibido fue de error
      */
     public boolean isMsgInterrogation(){
         return this.msgTitle.equals(MSGTITLE_INTERROG);
     }     
     
     /**
      * @return the msgId
      */
     public String getMsgId() {
         
      if(StringUtils.isNotBlank(msgId)){
           StringTokenizer tokens = new StringTokenizer(msgId);  
           if(tokens.hasMoreTokens()){  
               msgId = tokens.nextToken();  
           }  
      }
      
      return msgId;
     }

     /**
      * @param msgId the msgId to set
      */
     public void setMsgId(String msgId) {
      this.msgId = msgId;
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

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public int getNotPagedTotalItems() {
        return notPagedTotalItems;
    }

    public void setNotPagedTotalItems(int notPagedTotalItems) {
        this.notPagedTotalItems = notPagedTotalItems;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }
    
    @Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
