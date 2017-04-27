
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para complemento complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="complemento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idCobertura" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idComplementoCobertura" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
@XmlType(name = "complemento", propOrder = {
    "idCobertura",
    "idComplementoCobertura",
    "nombre"
})
public class Complemento {

    protected int idCobertura;
    protected int idComplementoCobertura;
    protected String nombre;

    /**
     * Obtiene el valor de la propiedad idCobertura.
     * 
     */
    public int getIdCobertura() {
        return idCobertura;
    }

    /**
     * Define el valor de la propiedad idCobertura.
     * 
     */
    public void setIdCobertura(int value) {
        this.idCobertura = value;
    }

    /**
     * Obtiene el valor de la propiedad idComplementoCobertura.
     * 
     */
    public int getIdComplementoCobertura() {
        return idComplementoCobertura;
    }

    /**
     * Define el valor de la propiedad idComplementoCobertura.
     * 
     */
    public void setIdComplementoCobertura(int value) {
        this.idComplementoCobertura = value;
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
