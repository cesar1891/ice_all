
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para consultarEstatusCotizacionesResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="consultarEstatusCotizacionesResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://com.gs.cotizador.ws.cotizacionindividual}estatusCotizacionResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarEstatusCotizacionesResponse", propOrder = {
    "_return"
})
public class ConsultarEstatusCotizacionesResponse {

    @XmlElement(name = "return")
    protected EstatusCotizacionResponse _return;

    /**
     * Obtiene el valor de la propiedad return.
     * 
     * @return
     *     possible object is
     *     {@link EstatusCotizacionResponse }
     *     
     */
    public EstatusCotizacionResponse getReturn() {
        return _return;
    }

    /**
     * Define el valor de la propiedad return.
     * 
     * @param value
     *     allowed object is
     *     {@link EstatusCotizacionResponse }
     *     
     */
    public void setReturn(EstatusCotizacionResponse value) {
        this._return = value;
    }

}
