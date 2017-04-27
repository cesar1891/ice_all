
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para wsObtenerTotalesFormaPago complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="wsObtenerTotalesFormaPago">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arg0" type="{http://com.gs.cotizador.ws.cotizacionindividual}obtenerTotalesFormaPagoRequest" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wsObtenerTotalesFormaPago", propOrder = {
    "arg0"
})
public class WsObtenerTotalesFormaPago {

    protected ObtenerTotalesFormaPagoRequest arg0;

    /**
     * Obtiene el valor de la propiedad arg0.
     * 
     * @return
     *     possible object is
     *     {@link ObtenerTotalesFormaPagoRequest }
     *     
     */
    public ObtenerTotalesFormaPagoRequest getArg0() {
        return arg0;
    }

    /**
     * Define el valor de la propiedad arg0.
     * 
     * @param value
     *     allowed object is
     *     {@link ObtenerTotalesFormaPagoRequest }
     *     
     */
    public void setArg0(ObtenerTotalesFormaPagoRequest value) {
        this.arg0 = value;
    }

}
