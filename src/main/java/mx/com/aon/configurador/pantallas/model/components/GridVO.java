/**
 * 
 */
package mx.com.aon.configurador.pantallas.model.components;

import java.io.Serializable;
import java.util.List;

import mx.com.aon.flujos.cotizacion.model.ResultadoCotizacionVO;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *  Clase Value Object con los atributos necesarios
 *  para la formacion del grid en resultados
 *  del proceso de cotizacion: records para el store,
 *  columns, elementos y datos.
 * 
 * @author  aurora.lozada
 * 
 */
public class GridVO implements Serializable{

    
    /**
     * 
     */
    private static final long serialVersionUID = -630315437593322612L;

    
    
    private List <RecordVO> recordList;
    
    private List <ColumnGridVO> columnList;
    
    private List <ResultadoCotizacionVO> resultList;
    
    private List <ItemVO> itemListPlan;
    
    private List <ItemVO> itemListAseguradora;
    
    private List <ItemVO> itemListPago;

    /**
     * @return the recordList
     */
    public List<RecordVO> getRecordList() {
        return recordList;
    }

    /**
     * @param recordList the recordList to set
     */
    public void setRecordList(List<RecordVO> recordList) {
        this.recordList = recordList;
    }

    /**
     * @return the columnList
     */
    public List<ColumnGridVO> getColumnList() {
        return columnList;
    }

    /**
     * @param columnList the columnList to set
     */
    public void setColumnList(List<ColumnGridVO> columnList) {
        this.columnList = columnList;
    }

    /**
     * @return the resultList
     */
    public List<ResultadoCotizacionVO> getResultList() {
        return resultList;
    }

    /**
     * @param resultList the resultList to set
     */
    public void setResultList(List<ResultadoCotizacionVO> resultList) {
        this.resultList = resultList;
    }

    /**
     * @return the itemListAseguradora
     */
    public List<ItemVO> getItemListAseguradora() {
        return itemListAseguradora;
    }

    /**
     * @param itemListAseguradora the itemListAseguradora to set
     */
    public void setItemListAseguradora(List<ItemVO> itemListAseguradora) {
        this.itemListAseguradora = itemListAseguradora;
    }

    /**
     * @return the itemListPago
     */
    public List<ItemVO> getItemListPago() {
        return itemListPago;
    }

    /**
     * @param itemListPago the itemListPago to set
     */
    public void setItemListPago(List<ItemVO> itemListPago) {
        this.itemListPago = itemListPago;
    }

    /**
     * @return the itemListPlan
     */
    public List<ItemVO> getItemListPlan() {
        return itemListPlan;
    }

    /**
     * @param itemListPlan the itemListPlan to set
     */
    public void setItemListPlan(List<ItemVO> itemListPlan) {
        this.itemListPlan = itemListPlan;
    }

    public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}

  
}
