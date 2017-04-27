
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para version complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="version">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="amis" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="descripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="modelos" type="{http://www.w3.org/2001/XMLSchema}int" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "version", namespace = "http://com.gs.cotizador.ws.catalogos", propOrder = {
    "amis",
    "descripcion",
    "modelos"
})
public class Version {

    protected int amis;
    protected String descripcion;
    @XmlElement(nillable = true)
    protected List<Integer> modelos;

    /**
     * Obtiene el valor de la propiedad amis.
     * 
     */
    public int getAmis() {
        return amis;
    }

    /**
     * Define el valor de la propiedad amis.
     * 
     */
    public void setAmis(int value) {
        this.amis = value;
    }

    /**
     * Obtiene el valor de la propiedad descripcion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Define el valor de la propiedad descripcion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcion(String value) {
        this.descripcion = value;
    }

    /**
     * Gets the value of the modelos property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the modelos property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getModelos().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getModelos() {
        if (modelos == null) {
            modelos = new ArrayList<Integer>();
        }
        return this.modelos;
    }

}
