/**
 * 
 */
package mx.com.aon.flujos.cotizacion.model;

import java.io.Serializable;

/**
 * 
 *  Clase Value Object de una descripcion de ayuda correspondiente a una cobertura
 * 
 * @author  aurora.lozada
 * 
 */


public class AyudaCoberturaCotizacionVO implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 6324062215613460974L;
    
    private String cdGarant;
    
    private String dsGarant;
    
    private String dsAyuda;

    /**
     * @return the cdGarant
     */
    public String getCdGarant() {
        return cdGarant;
    }

    /**
     * @param cdGarant the cdGarant to set
     */
    public void setCdGarant(String cdGarant) {
        this.cdGarant = cdGarant;
    }

    /**
     * @return the dsGarant
     */
    public String getDsGarant() {
        return dsGarant;
    }

    /**
     * @param dsGarant the dsGarant to set
     */
    public void setDsGarant(String dsGarant) {
        this.dsGarant = dsGarant;
    }

    /**
     * @return the dsAyuda
     */
    public String getDsAyuda() {
        return dsAyuda;
    }

    /**
     * @param dsAyuda the dsAyuda to set
     */
    public void setDsAyuda(String dsAyuda) {
        this.dsAyuda = dsAyuda;
    }
   
}
