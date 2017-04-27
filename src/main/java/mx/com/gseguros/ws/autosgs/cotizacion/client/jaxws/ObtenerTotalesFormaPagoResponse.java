
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para obtenerTotalesFormaPagoResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="obtenerTotalesFormaPagoResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://com.gs.cotizador.ws.cotizacionindividual}response">
 *       &lt;sequence>
 *         &lt;element name="totalesFormaPagoDTO" type="{http://com.gs.cotizador.ws.cotizacionindividual}totalFormaPagoDTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obtenerTotalesFormaPagoResponse", propOrder = {
    "totalesFormaPagoDTO"
})
public class ObtenerTotalesFormaPagoResponse
    extends Response
{

    protected TotalFormaPagoDTO totalesFormaPagoDTO;

    /**
     * Obtiene el valor de la propiedad totalesFormaPagoDTO.
     * 
     * @return
     *     possible object is
     *     {@link TotalFormaPagoDTO }
     *     
     */
    public TotalFormaPagoDTO getTotalesFormaPagoDTO() {
        return totalesFormaPagoDTO;
    }

    /**
     * Define el valor de la propiedad totalesFormaPagoDTO.
     * 
     * @param value
     *     allowed object is
     *     {@link TotalFormaPagoDTO }
     *     
     */
    public void setTotalesFormaPagoDTO(TotalFormaPagoDTO value) {
        this.totalesFormaPagoDTO = value;
    }

}
