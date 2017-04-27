
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para SDTPoliza complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="SDTPoliza">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="sucursal" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="ramos" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="numpol" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="tipendo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="endoso" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SDTPoliza", namespace = "KB_WSEmisionPoliza", propOrder = {

})
public class SDTPoliza {

    @XmlElement(namespace = "KB_WSEmisionPoliza")
    protected short sucursal;
    @XmlElement(namespace = "KB_WSEmisionPoliza")
    protected short ramos;
    @XmlElement(namespace = "KB_WSEmisionPoliza")
    protected long numpol;
    @XmlElement(namespace = "KB_WSEmisionPoliza", required = true)
    protected String tipendo;
    @XmlElement(namespace = "KB_WSEmisionPoliza")
    protected long endoso;

    /**
     * Obtiene el valor de la propiedad sucursal.
     * 
     */
    public short getSucursal() {
        return sucursal;
    }

    /**
     * Define el valor de la propiedad sucursal.
     * 
     */
    public void setSucursal(short value) {
        this.sucursal = value;
    }

    /**
     * Obtiene el valor de la propiedad ramos.
     * 
     */
    public short getRamos() {
        return ramos;
    }

    /**
     * Define el valor de la propiedad ramos.
     * 
     */
    public void setRamos(short value) {
        this.ramos = value;
    }

    /**
     * Obtiene el valor de la propiedad numpol.
     * 
     */
    public long getNumpol() {
        return numpol;
    }

    /**
     * Define el valor de la propiedad numpol.
     * 
     */
    public void setNumpol(long value) {
        this.numpol = value;
    }

    /**
     * Obtiene el valor de la propiedad tipendo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipendo() {
        return tipendo;
    }

    /**
     * Define el valor de la propiedad tipendo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipendo(String value) {
        this.tipendo = value;
    }

    /**
     * Obtiene el valor de la propiedad endoso.
     * 
     */
    public long getEndoso() {
        return endoso;
    }

    /**
     * Define el valor de la propiedad endoso.
     * 
     */
    public void setEndoso(long value) {
        this.endoso = value;
    }

}
