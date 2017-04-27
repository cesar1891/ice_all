
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para buscarCPResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="buscarCPResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://com.gs.cotizador.ws.cotizacionindividual}response">
 *       &lt;sequence>
 *         &lt;element name="codigosPostales" type="{http://com.gs.cotizador.ws.cotizacionindividual}codigoPostal" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "buscarCPResponse", propOrder = {
    "codigosPostales"
})
public class BuscarCPResponse
    extends Response
{

    @XmlElement(nillable = true)
    protected List<CodigoPostal> codigosPostales;

    /**
     * Gets the value of the codigosPostales property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the codigosPostales property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCodigosPostales().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CodigoPostal }
     * 
     * 
     */
    public List<CodigoPostal> getCodigosPostales() {
        if (codigosPostales == null) {
            codigosPostales = new ArrayList<CodigoPostal>();
        }
        return this.codigosPostales;
    }

}
