/**
 * 
 */
package mx.com.aon.configurador.pantallas.model.components;

import java.io.Serializable;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


/**
 *  Clase Value Object con los atributos necesarios
 *  para la generacion de las columnas del grid en resultados
 *  del proceso de cotizacion
 * 
 * @author  aurora.lozada
 * 
 */
public class ColumnGridVO implements Serializable {

    private static final long serialVersionUID = -4607527332222066663L;
    protected final transient Logger logger = Logger.getLogger(ColumnGridVO.class);
    
    private String header;
    
    private String dataIndex;
    
    private int width;
    
    private Boolean sortable;
    
    private String id;
    
    private boolean hidden;
    
   
    //Constructors
    
    @Override
    public String toString() {
        
        String jsonResult = JSONObject.fromObject(this).toString();
        
        if (StringUtils.isBlank(header)) {
            return jsonResult;
        }
        
        String jsonResult1 = new StringBuilder("\"header\":\"").append(getHeader()).append("\"").toString();
        
        String jsonResult2 = new StringBuilder("\"header\":")
        			.append("'<span title=\"Detalle del plan\"; style=\"color:white;font-size:11px;font-family:Arial,Helvetica,sans-serif;\">' + ' ? ' + ' ' +'</span>' + '<span style=\"color:white;font-size:11px; text-decoration:underline; font-family:Arial,Helvetica,sans-serif;\">' + \" ")
        			.append(getHeader())
        			.append(" \" ")
        			.append("+ '</span>'").toString();
       
        jsonResult = jsonResult.replace(jsonResult1, jsonResult2);
        
        float factor = 100;
        int j = 0; // contador letras MAYUSCULAS
        int k = 0; // contador letras MAYUSCULAS & minusculas (omite espacios)

        for (int i = 0; i < getHeader().length(); i++) {
        	char c = getHeader().charAt(i); 
        	if ( c >= 'A' && c <= 'Z' ) { ++j; } 
        	if ( (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9' ) ) { ++k; } 
        }

        if ( j > k/2 ) {			// CASO MAYUSCULAS (> la mitad de todas las letras del header)
        	factor = getHeader().length() * (35/4);				// valor arbitrario (width=8.75) que es el numero que ocupa una letra MAYUSCULA
        } else if ( j < k/2 ) {	// caso minusculas (< la mitad de todas las letras del header) 
        	factor = getHeader().length() / 12;
        	factor *= 100;
        }

        return jsonResult.replace( "\"width\":100", "\"width\":"+ factor );
    }


    
    //Constructors
    
    public ColumnGridVO(){
        
    }
    
    public ColumnGridVO(String header, String dataIndex, int width, 
            Boolean sortable, String id, boolean hidden) {
       
        this.header = header;
        this.dataIndex = dataIndex;
        this.width = width;
        this.sortable = sortable;
        this.id = id;
        this.hidden = hidden;
    }

    public String getDataIndex() {
        return dataIndex;
    }

    public void setDataIndex(String dataIndex) {
        this.dataIndex = dataIndex;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Boolean getSortable() {
        return sortable;
    }

    public void setSortable(Boolean sortable) {
        this.sortable = sortable;
    }

}