
package mx.com.gseguros.ws.folioserviciopublico.model;

import java.io.Serializable;

import javax.xml.datatype.XMLGregorianCalendar;

public class EmAdmfolIdVO implements Serializable {

	private static final long serialVersionUID = 1042239016033816311L;
	
    protected XMLGregorianCalendar fecEnt;
    protected XMLGregorianCalendar fecSta;
    protected int numAge;
    protected int numEnd;
    protected int numFol;
    protected int numPol;
    protected short numRam;
    protected short numSuc;
    protected short numUsu;
    protected int status;
    protected int sucAdm;
    protected int tipEnd;

    /**
     * Obtiene el valor de la propiedad fecEnt.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFecEnt() {
        return fecEnt;
    }

    /**
     * Define el valor de la propiedad fecEnt.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFecEnt(XMLGregorianCalendar value) {
        this.fecEnt = value;
    }

    /**
     * Obtiene el valor de la propiedad fecSta.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFecSta() {
        return fecSta;
    }

    /**
     * Define el valor de la propiedad fecSta.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFecSta(XMLGregorianCalendar value) {
        this.fecSta = value;
    }

    /**
     * Obtiene el valor de la propiedad numAge.
     * 
     */
    public int getNumAge() {
        return numAge;
    }

    /**
     * Define el valor de la propiedad numAge.
     * 
     */
    public void setNumAge(int value) {
        this.numAge = value;
    }

    /**
     * Obtiene el valor de la propiedad numEnd.
     * 
     */
    public int getNumEnd() {
        return numEnd;
    }

    /**
     * Define el valor de la propiedad numEnd.
     * 
     */
    public void setNumEnd(int value) {
        this.numEnd = value;
    }

    /**
     * Obtiene el valor de la propiedad numFol.
     * 
     */
    public int getNumFol() {
        return numFol;
    }

    /**
     * Define el valor de la propiedad numFol.
     * 
     */
    public void setNumFol(int value) {
        this.numFol = value;
    }

    /**
     * Obtiene el valor de la propiedad numPol.
     * 
     */
    public int getNumPol() {
        return numPol;
    }

    /**
     * Define el valor de la propiedad numPol.
     * 
     */
    public void setNumPol(int value) {
        this.numPol = value;
    }

    /**
     * Obtiene el valor de la propiedad numRam.
     * 
     */
    public short getNumRam() {
        return numRam;
    }

    /**
     * Define el valor de la propiedad numRam.
     * 
     */
    public void setNumRam(short value) {
        this.numRam = value;
    }

    /**
     * Obtiene el valor de la propiedad numSuc.
     * 
     */
    public short getNumSuc() {
        return numSuc;
    }

    /**
     * Define el valor de la propiedad numSuc.
     * 
     */
    public void setNumSuc(short value) {
        this.numSuc = value;
    }

    /**
     * Obtiene el valor de la propiedad numUsu.
     * 
     */
    public short getNumUsu() {
        return numUsu;
    }

    /**
     * Define el valor de la propiedad numUsu.
     * 
     */
    public void setNumUsu(short value) {
        this.numUsu = value;
    }

    /**
     * Obtiene el valor de la propiedad status.
     * 
     */
    public int getStatus() {
        return status;
    }

    /**
     * Define el valor de la propiedad status.
     * 
     */
    public void setStatus(int value) {
        this.status = value;
    }

    /**
     * Obtiene el valor de la propiedad sucAdm.
     * 
     */
    public int getSucAdm() {
        return sucAdm;
    }

    /**
     * Define el valor de la propiedad sucAdm.
     * 
     */
    public void setSucAdm(int value) {
        this.sucAdm = value;
    }

    /**
     * Obtiene el valor de la propiedad tipEnd.
     * 
     */
    public int getTipEnd() {
        return tipEnd;
    }

    /**
     * Define el valor de la propiedad tipEnd.
     * 
     */
    public void setTipEnd(int value) {
        this.tipEnd = value;
    }

}