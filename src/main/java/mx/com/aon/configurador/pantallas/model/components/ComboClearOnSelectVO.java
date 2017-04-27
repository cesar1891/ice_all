/**
 * 
 */
package mx.com.aon.configurador.pantallas.model.components;

import java.io.Serializable;

/**
 * Clase Value Object con los atributos necesarios para la generacion de la sentencia Ext.getCmp('idCombo').clearValue()
 * 
 * @author aurora.lozada
 * 
 */
public class ComboClearOnSelectVO implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -7307312981920524049L;

    private String idCombo;
    
    private String cdatribu;
    
    private String ottabval;

    
    // Constructors

    @Override
    public String toString() {

        StringBuffer bufferClear = new StringBuffer();
        bufferClear.append("Ext.getCmp('").append(this.idCombo).append("').clearValue()");
        return bufferClear.toString();

    }

    public ComboClearOnSelectVO() {

    }

    public ComboClearOnSelectVO(String idCombo) {
        this.idCombo = idCombo;
    
    }

   
    
    // Getters y setters

    /**
     * @return the idCombo
     */
    public String getIdCombo() {
        return idCombo;
    }

    /**
     * @param idCombo the idCombo to set
     */
    public void setIdCombo(String idCombo) {
        this.idCombo = idCombo;
    }

	public String getCdatribu() {
		return cdatribu;
	}

	public void setCdatribu(String cdatribu) {
		this.cdatribu = cdatribu;
	}

	public String getOttabval() {
		return ottabval;
	}

	public void setOttabval(String ottabval) {
		this.ottabval = ottabval;
	}
    
}
