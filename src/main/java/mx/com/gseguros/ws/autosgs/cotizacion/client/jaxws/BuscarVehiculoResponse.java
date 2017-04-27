
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para buscarVehiculoResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="buscarVehiculoResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://com.gs.cotizador.ws.cotizacionindividual}response">
 *       &lt;sequence>
 *         &lt;element name="vehiculos" type="{http://com.gs.cotizador.ws.cotizacionindividual}vehiculo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "buscarVehiculoResponse", propOrder = {
    "vehiculos"
})
public class BuscarVehiculoResponse
    extends Response
{

    @XmlElement(nillable = true)
    protected List<Vehiculo> vehiculos;

    /**
     * Gets the value of the vehiculos property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the vehiculos property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVehiculos().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Vehiculo }
     * 
     * 
     */
    public List<Vehiculo> getVehiculos() {
        if (vehiculos == null) {
            vehiculos = new ArrayList<Vehiculo>();
        }
        return this.vehiculos;
    }

}
