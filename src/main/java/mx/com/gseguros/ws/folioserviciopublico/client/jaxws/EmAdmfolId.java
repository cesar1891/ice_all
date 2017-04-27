
package mx.com.gseguros.ws.folioserviciopublico.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para emAdmfolId complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="emAdmfolId">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fecEnt" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fecSta" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="numAge" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="numEnd" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="numFol" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="numPol" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="numRam" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="numSuc" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="numUsu" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}unsignedShort"/>
 *         &lt;element name="sucAdm" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="tipEnd" type="{http://www.w3.org/2001/XMLSchema}unsignedShort"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "emAdmfolId", propOrder = {
    "fecEnt",
    "fecSta",
    "numAge",
    "numEnd",
    "numFol",
    "numPol",
    "numRam",
    "numSuc",
    "numUsu",
    "status",
    "sucAdm",
    "tipEnd"
})
public class EmAdmfolId {

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fecEnt;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fecSta;
    protected int numAge;
    protected int numEnd;
    protected int numFol;
    protected int numPol;
    protected short numRam;
    protected short numSuc;
    protected short numUsu;
    @XmlSchemaType(name = "unsignedShort")
    protected int status;
    protected int sucAdm;
    @XmlSchemaType(name = "unsignedShort")
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
