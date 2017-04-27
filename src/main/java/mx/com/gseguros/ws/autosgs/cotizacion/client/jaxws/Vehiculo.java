
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para vehiculo complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="vehiculo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="amis" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="descripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idSegmento" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="tipoVehiCA" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vehiculo", propOrder = {
    "amis",
    "descripcion",
    "idSegmento",
    "tipoVehiCA"
})
public class Vehiculo {

    protected int amis;
    protected String descripcion;
    protected int idSegmento;
    protected int tipoVehiCA;

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
     * Obtiene el valor de la propiedad idSegmento.
     * 
     */
    public int getIdSegmento() {
        return idSegmento;
    }

    /**
     * Define el valor de la propiedad idSegmento.
     * 
     */
    public void setIdSegmento(int value) {
        this.idSegmento = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoVehiCA.
     * 
     */
    public int getTipoVehiCA() {
        return tipoVehiCA;
    }

    /**
     * Define el valor de la propiedad tipoVehiCA.
     * 
     */
    public void setTipoVehiCA(int value) {
        this.tipoVehiCA = value;
    }

}
