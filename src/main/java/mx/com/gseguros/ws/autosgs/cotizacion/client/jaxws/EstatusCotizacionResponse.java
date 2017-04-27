
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para estatusCotizacionResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="estatusCotizacionResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://com.gs.cotizador.ws.cotizacionindividual}response">
 *       &lt;sequence>
 *         &lt;element name="estatusCotizaciones" type="{http://com.gs.cotizador.ws.cotizacionindividual}estatusCotizacion" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "estatusCotizacionResponse", propOrder = {
    "estatusCotizaciones"
})
public class EstatusCotizacionResponse
    extends Response
{

    @XmlElement(nillable = true)
    protected List<EstatusCotizacion> estatusCotizaciones;

    /**
     * Gets the value of the estatusCotizaciones property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the estatusCotizaciones property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEstatusCotizaciones().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EstatusCotizacion }
     * 
     * 
     */
    public List<EstatusCotizacion> getEstatusCotizaciones() {
        if (estatusCotizaciones == null) {
            estatusCotizaciones = new ArrayList<EstatusCotizacion>();
        }
        return this.estatusCotizaciones;
    }

}
