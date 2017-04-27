
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para cotizacionRequest complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="cotizacionRequest">
 *   &lt;complexContent>
 *     &lt;extension base="{http://com.gs.cotizador.ws.cotizacionindividual}request">
 *       &lt;sequence>
 *         &lt;element name="cotizacion" type="{http://com.gs.cotizador.ws.cotizacionindividual}cotizacion" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cotizacionRequest", propOrder = {
    "cotizacion"
})
public class CotizacionRequest
    extends Request
{

    protected Cotizacion cotizacion;

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

}
