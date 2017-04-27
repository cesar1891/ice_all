
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para submarca complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="submarca">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idSegmento" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="marca" type="{http://com.gs.cotizador.ws.catalogos}marca" minOccurs="0"/>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "submarca", namespace = "http://com.gs.cotizador.ws.catalogos", propOrder = {
    "id",
    "idSegmento",
    "marca",
    "nombre"
})
public class Submarca {

    protected int id;
    protected int idSegmento;
    protected Marca marca;
    protected String nombre;

    /**
     * Obtiene el valor de la propiedad id.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Define el valor de la propiedad id.
     * 
     */
    public void setId(int value) {
        this.id = value;
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
     * Obtiene el valor de la propiedad marca.
     * 
     * @return
     *     possible object is
     *     {@link Marca }
     *     
     */
    public Marca getMarca() {
        return marca;
    }

    /**
     * Define el valor de la propiedad marca.
     * 
     * @param value
     *     allowed object is
     *     {@link Marca }
     *     
     */
    public void setMarca(Marca value) {
        this.marca = value;
    }

    /**
     * Obtiene el valor de la propiedad nombre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Define el valor de la propiedad nombre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

}
