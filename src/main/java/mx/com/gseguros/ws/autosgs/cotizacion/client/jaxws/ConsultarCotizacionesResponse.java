
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para consultarCotizacionesResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="consultarCotizacionesResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://com.gs.cotizador.ws.cotizacionindividual}response">
 *       &lt;sequence>
 *         &lt;element name="cotizaciones" type="{http://com.gs.cotizador.ws.cotizacionindividual}cotizacionDTO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarCotizacionesResponse", propOrder = {
    "cotizaciones"
})
public class ConsultarCotizacionesResponse
    extends Response
{

    @XmlElement(nillable = true)
    protected List<CotizacionDTO> cotizaciones;

    /**
     * Gets the value of the cotizaciones property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cotizaciones property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCotizaciones().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CotizacionDTO }
     * 
     * 
     */
    public List<CotizacionDTO> getCotizaciones() {
        if (cotizaciones == null) {
            cotizaciones = new ArrayList<CotizacionDTO>();
        }
        return this.cotizaciones;
    }

}
