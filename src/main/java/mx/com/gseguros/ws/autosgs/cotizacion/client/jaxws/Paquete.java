
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para paquete complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="paquete">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="coberturas" type="{http://com.gs.cotizador.ws.cotizacionindividual}cobertura" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="idConfiguracionPaquete" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idPaquete" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="seleccionado" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "paquete", propOrder = {
    "coberturas",
    "idConfiguracionPaquete",
    "idPaquete",
    "nombre",
    "seleccionado"
})
public class Paquete {

    @XmlElement(nillable = true)
    protected List<Cobertura> coberturas;
    protected int idConfiguracionPaquete;
    protected int idPaquete;
    protected String nombre;
    protected boolean seleccionado;

    /**
     * Gets the value of the coberturas property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the coberturas property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCoberturas().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Cobertura }
     * 
     * 
     */
    public List<Cobertura> getCoberturas() {
        if (coberturas == null) {
            coberturas = new ArrayList<Cobertura>();
        }
        return this.coberturas;
    }

    /**
     * Obtiene el valor de la propiedad idConfiguracionPaquete.
     * 
     */
    public int getIdConfiguracionPaquete() {
        return idConfiguracionPaquete;
    }

    /**
     * Define el valor de la propiedad idConfiguracionPaquete.
     * 
     */
    public void setIdConfiguracionPaquete(int value) {
        this.idConfiguracionPaquete = value;
    }

    /**
     * Obtiene el valor de la propiedad idPaquete.
     * 
     */
    public int getIdPaquete() {
        return idPaquete;
    }

    /**
     * Define el valor de la propiedad idPaquete.
     * 
     */
    public void setIdPaquete(int value) {
        this.idPaquete = value;
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
     * Obtiene el valor de la propiedad seleccionado.
     * 
     */
    public boolean isSeleccionado() {
        return seleccionado;
    }

    /**
     * Define el valor de la propiedad seleccionado.
     * 
     */
    public void setSeleccionado(boolean value) {
        this.seleccionado = value;
    }

}
