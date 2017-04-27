
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para configuracionPaqueteResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="configuracionPaqueteResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://com.gs.cotizador.ws.cotizacionindividual}response">
 *       &lt;sequence>
 *         &lt;element name="configuracionPaquete" type="{http://com.gs.cotizador.ws.cotizacionindividual}configuracionPaquete" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "configuracionPaqueteResponse", propOrder = {
    "configuracionPaquete"
})
public class ConfiguracionPaqueteResponse
    extends Response
{

    protected ConfiguracionPaquete configuracionPaquete;

    /**
     * Obtiene el valor de la propiedad configuracionPaquete.
     * 
     * @return
     *     possible object is
     *     {@link ConfiguracionPaquete }
     *     
     */
    public ConfiguracionPaquete getConfiguracionPaquete() {
        return configuracionPaquete;
    }

    /**
     * Define el valor de la propiedad configuracionPaquete.
     * 
     * @param value
     *     allowed object is
     *     {@link ConfiguracionPaquete }
     *     
     */
    public void setConfiguracionPaquete(ConfiguracionPaquete value) {
        this.configuracionPaquete = value;
    }

}
