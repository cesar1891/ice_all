
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para wsObtenerTotalesFormaPagoResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="wsObtenerTotalesFormaPagoResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://com.gs.cotizador.ws.cotizacionindividual}obtenerTotalesFormaPagoResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wsObtenerTotalesFormaPagoResponse", propOrder = {
    "_return"
})
public class WsObtenerTotalesFormaPagoResponse {

    @XmlElement(name = "return")
    protected ObtenerTotalesFormaPagoResponse _return;

    /**
     * Obtiene el valor de la propiedad return.
     * 
     * @return
     *     possible object is
     *     {@link ObtenerTotalesFormaPagoResponse }
     *     
     */
    public ObtenerTotalesFormaPagoResponse getReturn() {
        return _return;
    }

    /**
     * Define el valor de la propiedad return.
     * 
     * @param value
     *     allowed object is
     *     {@link ObtenerTotalesFormaPagoResponse }
     *     
     */
    public void setReturn(ObtenerTotalesFormaPagoResponse value) {
        this._return = value;
    }

}
