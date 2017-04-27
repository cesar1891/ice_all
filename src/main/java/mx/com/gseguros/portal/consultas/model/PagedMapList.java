/**
 * 
 */
package mx.com.gseguros.portal.consultas.model;

import java.util.List;
import java.util.Map;

/**
 * @author hlopez
 * @date 07/07/2016
 *
 */
public class PagedMapList {

    List<Map<String,String>> rangeList;
    long totalItems;
    String messageResult;
    
    public List<Map<String, String>> getRangeList() {
        return rangeList;
    }
    public void setRangeList(List<Map<String, String>> rangeList) {
        this.rangeList = rangeList;
    }
    public long getTotalItems() {
        return totalItems;
    }
    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }
    public String getMessageResult() {
        return messageResult;
    }
    public void setMessageResult(String messageResult) {
        this.messageResult = messageResult;
    }

    
}
