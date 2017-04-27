/**
 * 
 */
package mx.com.aon.flujos.cotizacion.model;

import java.io.Serializable;

/**
 * 
 * Clase Value Object de una cobertura
 * 
 * @author  aurora.lozada
 * 
 */

public class CoberturaCotizacionVO implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 8265386389298070520L;
    
    private String cdGarant;
    
    private String dsGarant;
    
    private String sumaAsegurada;
    
    private String deducible;
    
    private String cdCiaaseg;
    
    private String cdRamo;
    
    private String orden;

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
     * @return the sumaAsegurada
     */
    public String getSumaAsegurada() {
        return sumaAsegurada;
    }

    /**
     * @param sumaAsegurada the sumaAsegurada to set
     */
    public void setSumaAsegurada(String sumaAsegurada) {
        this.sumaAsegurada = sumaAsegurada;
    }

    /**
     * @return the deducible
     */
    public String getDeducible() {
        return deducible;
    }

    /**
     * @param deducible the deducible to set
     */
    public void setDeducible(String deducible) {
        this.deducible = deducible;
    }

    /**
     * @return the cdCiaaseg
     */
    public String getCdCiaaseg() {
        return cdCiaaseg;
    }

    /**
     * @param cdCiaaseg the cdCiaaseg to set
     */
    public void setCdCiaaseg(String cdCiaaseg) {
        this.cdCiaaseg = cdCiaaseg;
    }

    /**
     * @return the cdRamo
     */
    public String getCdRamo() {
        return cdRamo;
    }

    /**
     * @param cdRamo the cdRamo to set
     */
    public void setCdRamo(String cdRamo) {
        this.cdRamo = cdRamo;
    }

	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}
  
}
