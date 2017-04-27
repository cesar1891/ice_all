
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para agente complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="agente">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="emiteCamion" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="idAgente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="iva" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sucursal" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "agente", propOrder = {
    "email",
    "emiteCamion",
    "idAgente",
    "iva",
    "nombre",
    "sucursal"
})
public class Agente {

    protected String email;
    protected boolean emiteCamion;
    protected String idAgente;
    protected int iva;
    protected String nombre;
    protected int sucursal;

    /**
     * Obtiene el valor de la propiedad email.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define el valor de la propiedad email.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Obtiene el valor de la propiedad emiteCamion.
     * 
     */
    public boolean isEmiteCamion() {
        return emiteCamion;
    }

    /**
     * Define el valor de la propiedad emiteCamion.
     * 
     */
    public void setEmiteCamion(boolean value) {
        this.emiteCamion = value;
    }

    /**
     * Obtiene el valor de la propiedad idAgente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdAgente() {
        return idAgente;
    }

    /**
     * Define el valor de la propiedad idAgente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdAgente(String value) {
        this.idAgente = value;
    }

    /**
     * Obtiene el valor de la propiedad iva.
     * 
     */
    public int getIva() {
        return iva;
    }

    /**
     * Define el valor de la propiedad iva.
     * 
     */
    public void setIva(int value) {
        this.iva = value;
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

    /**
     * Obtiene el valor de la propiedad sucursal.
     * 
     */
    public int getSucursal() {
        return sucursal;
    }

    /**
     * Define el valor de la propiedad sucursal.
     * 
     */
    public void setSucursal(int value) {
        this.sucursal = value;
    }

}
