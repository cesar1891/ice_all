
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para guardarCotizacionResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="guardarCotizacionResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://com.gs.cotizador.ws.cotizacionindividual}response">
 *       &lt;sequence>
 *         &lt;element name="cotizacion" type="{http://com.gs.cotizador.ws.cotizacionindividual}cotizacion" minOccurs="0"/>
 *         &lt;element name="formasDePago" type="{http://com.gs.cotizador.ws.cotizacionindividual}mapElementsArray" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "guardarCotizacionResponse", propOrder = {
    "cotizacion",
    "formasDePago"
})
public class GuardarCotizacionResponse
    extends Response
{

    protected Cotizacion cotizacion;
    protected MapElementsArray formasDePago;

    /**
     * Obtiene el valor de la propiedad cotizacion.
     * 
     * @return
     *     possible object is
     *     {@link Cotizacion }
     *     
     */
    public Cotizacion getCotizacion() {
        return cotizacion;
    }

    /**
     * Define el valor de la propiedad cotizacion.
     * 
     * @param value
     *     allowed object is
     *     {@link Cotizacion }
     *     
     */
    public void setCotizacion(Cotizacion value) {
        this.cotizacion = value;
    }

    /**
     * Obtiene el valor de la propiedad formasDePago.
     * 
     * @return
     *     possible object is
     *     {@link MapElementsArray }
     *     
     */
    public MapElementsArray getFormasDePago() {
        return formasDePago;
    }

    /**
     * Define el valor de la propiedad formasDePago.
     * 
     * @param value
     *     allowed object is
     *     {@link MapElementsArray }
     *     
     */
    public void setFormasDePago(MapElementsArray value) {
        this.formasDePago = value;
    }

}
