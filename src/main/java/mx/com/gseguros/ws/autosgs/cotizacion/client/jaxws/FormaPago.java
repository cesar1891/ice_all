
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para formaPago complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="formaPago">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dividor" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idFormaPago" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="primaNeta" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="primaNetaRecargo" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="primaTotal" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="recargo" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "formaPago", propOrder = {
    "dividor",
    "idFormaPago",
    "nombre",
    "primaNeta",
    "primaNetaRecargo",
    "primaTotal",
    "recargo"
})
public class FormaPago {

    protected int dividor;
    protected Integer idFormaPago;
    protected String nombre;
    protected double primaNeta;
    protected double primaNetaRecargo;
    protected double primaTotal;
    protected double recargo;

    /**
     * Obtiene el valor de la propiedad dividor.
     * 
     */
    public int getDividor() {
        return dividor;
    }

    /**
     * Define el valor de la propiedad dividor.
     * 
     */
    public void setDividor(int value) {
        this.dividor = value;
    }

    /**
     * Obtiene el valor de la propiedad idFormaPago.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdFormaPago() {
        return idFormaPago;
    }

    /**
     * Define el valor de la propiedad idFormaPago.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdFormaPago(Integer value) {
        this.idFormaPago = value;
    }

    /**
     * Obtiene el valor de la propiedad nombre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Define el valor de la propiedad nombre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Obtiene el valor de la propiedad primaNeta.
     * 
     */
    public double getPrimaNeta() {
        return primaNeta;
    }

    /**
     * Define el valor de la propiedad primaNeta.
     * 
     */
    public void setPrimaNeta(double value) {
        this.primaNeta = value;
    }

    /**
     * Obtiene el valor de la propiedad primaNetaRecargo.
     * 
     */
    public double getPrimaNetaRecargo() {
        return primaNetaRecargo;
    }

    /**
     * Define el valor de la propiedad primaNetaRecargo.
     * 
     */
    public void setPrimaNetaRecargo(double value) {
        this.primaNetaRecargo = value;
    }

    /**
     * Obtiene el valor de la propiedad primaTotal.
     * 
     */
    public double getPrimaTotal() {
        return primaTotal;
    }

    /**
     * Define el valor de la propiedad primaTotal.
     * 
     */
    public void setPrimaTotal(double value) {
        this.primaTotal = value;
    }

    /**
     * Obtiene el valor de la propiedad recargo.
     * 
     */
    public double getRecargo() {
        return recargo;
    }

    /**
     * Define el valor de la propiedad recargo.
     * 
     */
    public void setRecargo(double value) {
        this.recargo = value;
    }

}
