
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para emisionPolizaRequest complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="emisionPolizaRequest">
 *   &lt;complexContent>
 *     &lt;extension base="{http://com.gs.cotizador.ws.cotizacionindividual}request">
 *       &lt;sequence>
 *         &lt;element name="fechaEmision" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="idAgenteCompartido" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idCliente" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idCotizacion" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="incisos" type="{http://com.gs.cotizador.ws.cotizacionindividual}inciso" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="porcenComisionAgente2" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "emisionPolizaRequest", propOrder = {
    "fechaEmision",
    "idAgenteCompartido",
    "idCliente",
    "idCotizacion",
    "incisos",
    "porcenComisionAgente2"
})
public class EmisionPolizaRequest
    extends Request
{

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaEmision;
    protected int idAgenteCompartido;
    protected int idCliente;
    protected int idCotizacion;
    @XmlElement(nillable = true)
    protected List<Inciso> incisos;
    protected int porcenComisionAgente2;

    /**
     * Obtiene el valor de la propiedad fechaEmision.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaEmision() {
        return fechaEmision;
    }

    /**
     * Define el valor de la propiedad fechaEmision.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaEmision(XMLGregorianCalendar value) {
        this.fechaEmision = value;
    }

    /**
     * Obtiene el valor de la propiedad idAgenteCompartido.
     * 
     */
    public int getIdAgenteCompartido() {
        return idAgenteCompartido;
    }

    /**
     * Define el valor de la propiedad idAgenteCompartido.
     * 
     */
    public void setIdAgenteCompartido(int value) {
        this.idAgenteCompartido = value;
    }

    /**
     * Obtiene el valor de la propiedad idCliente.
     * 
     */
    public int getIdCliente() {
        return idCliente;
    }

    /**
     * Define el valor de la propiedad idCliente.
     * 
     */
    public void setIdCliente(int value) {
        this.idCliente = value;
    }

    /**
     * Obtiene el valor de la propiedad idCotizacion.
     * 
     */
    public int getIdCotizacion() {
        return idCotizacion;
    }

    /**
     * Define el valor de la propiedad idCotizacion.
     * 
     */
    public void setIdCotizacion(int value) {
        this.idCotizacion = value;
    }

    /**
     * Gets the value of the incisos property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the incisos property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIncisos().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Inciso }
     * 
     * 
     */
    public List<Inciso> getIncisos() {
        if (incisos == null) {
            incisos = new ArrayList<Inciso>();
        }
        return this.incisos;
    }

    /**
     * Obtiene el valor de la propiedad porcenComisionAgente2.
     * 
     */
    public int getPorcenComisionAgente2() {
        return porcenComisionAgente2;
    }

    /**
     * Define el valor de la propiedad porcenComisionAgente2.
     * 
     */
    public void setPorcenComisionAgente2(int value) {
        this.porcenComisionAgente2 = value;
    }

}
