/**
 * 
 */
package mx.com.aon.configurador.pantallas.model.components;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *  Clase Value Object con los atributos necesarios
 *  para obtener datos de un item diferenciante en los
 *  registros (planes, aseguradoras y formas de pago)
 *  para resultado del proceso de cotizacion
 * 
 * @author  aurora.lozada
 * 
 */
public class ItemVO implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 7907324619000051830L;
    
    private String clave;
    
    private String descripcion;
    
     /**
     * @return the clave
     */
    public String getClave() {
        return clave;
    }

    /**
     * @param clave the clave to set
     */
    public void setClave(String clave) {
        this.clave = clave;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
}
