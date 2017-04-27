/**
 * 
 */
package mx.com.aon.portal.service;

import java.util.List;
import java.util.Map;

/**
 * @author eflores
 * @date 07/07/2008
 *
 */
public class PagedList {

    @SuppressWarnings("unchecked")
    List itemsRangeList;
    int totalItems;
    String messageResult;

    /** Mapa para colocar cualquier otro parametro de salida */
	 private Map<String,Object> itemMap; 
	 
    @SuppressWarnings("unchecked")
    public List getItemsRangeList() {
        return itemsRangeList;
    }
    @SuppressWarnings("unchecked")
    public void setItemsRangeList(List itemsRangeList) {
        this.itemsRangeList = itemsRangeList;
    }


    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }


    public String getMessageResult() {
        return messageResult;
    }

    public void setMessageResult(String messageResult) {
        this.messageResult = messageResult;
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
}
