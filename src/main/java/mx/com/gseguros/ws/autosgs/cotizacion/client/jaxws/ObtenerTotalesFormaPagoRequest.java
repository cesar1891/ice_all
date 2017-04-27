
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para obtenerTotalesFormaPagoRequest complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="obtenerTotalesFormaPagoRequest">
 *   &lt;complexContent>
 *     &lt;extension base="{http://com.gs.cotizador.ws.cotizacionindividual}request">
 *       &lt;sequence>
 *         &lt;element name="formaPago" type="{http://com.gs.cotizador.ws.cotizacionindividual}formaPago" minOccurs="0"/>
 *         &lt;element name="idConfiguracionPaquete" type="{http://www.w3.org/2001/XMLSchema}int" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="idCotizacion" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="iva" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obtenerTotalesFormaPagoRequest", propOrder = {
    "formaPago",
    "idConfiguracionPaquete",
    "idCotizacion",
    "iva"
})
public class ObtenerTotalesFormaPagoRequest
    extends Request
{

    protected FormaPago formaPago;
    @XmlElement(nillable = true)
    protected List<Integer> idConfiguracionPaquete;
    protected int idCotizacion;
    protected int iva;

    /**
     * Obtiene el valor de la propiedad formaPago.
     * 
     * @return
     *     possible object is
     *     {@link FormaPago }
     *     
     */
    public FormaPago getFormaPago() {
        return formaPago;
    }

    /**
     * Define el valor de la propiedad formaPago.
     * 
     * @param value
     *     allowed object is
     *     {@link FormaPago }
     *     
     */
    public void setFormaPago(FormaPago value) {
        this.formaPago = value;
    }

    /**
     * Gets the value of the idConfiguracionPaquete property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the idConfiguracionPaquete property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdConfiguracionPaquete().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getIdConfiguracionPaquete() {
        if (idConfiguracionPaquete == null) {
            idConfiguracionPaquete = new ArrayList<Integer>();
        }
        return this.idConfiguracionPaquete;
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
     * Obtiene el valor de la propiedad iva.
     * 
     */
    public int getIva() {
        return iva;
    }

    /**
     * Define el valor de la propiedad iva.
     * 
     */
    public void setIva(int value) {
        this.iva = value;
    }

}
