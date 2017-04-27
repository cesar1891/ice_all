
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para wsBuscarVehiculo complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="wsBuscarVehiculo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arg0" type="{http://com.gs.cotizador.ws.cotizacionindividual}buscarVehiculoRequest" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wsBuscarVehiculo", propOrder = {
    "arg0"
})
public class WsBuscarVehiculo {

    protected BuscarVehiculoRequest arg0;

    /**
     * Obtiene el valor de la propiedad arg0.
     * 
     * @return
     *     possible object is
     *     {@link BuscarVehiculoRequest }
     *     
     */
    public BuscarVehiculoRequest getArg0() {
        return arg0;
    }

    /**
     * Define el valor de la propiedad arg0.
     * 
     * @param value
     *     allowed object is
     *     {@link BuscarVehiculoRequest }
     *     
     */
    public void setArg0(BuscarVehiculoRequest value) {
        this.arg0 = value;
    }

}
