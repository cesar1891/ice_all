
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para grupo complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="grupo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="complementos" type="{http://com.gs.cotizador.ws.cotizacionindividual}complemento" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="idGrupo" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idTipoCobertura" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
@XmlType(name = "grupo", propOrder = {
    "complementos",
    "idGrupo",
    "idTipoCobertura",
    "nombre"
})
public class Grupo {

    @XmlElement(nillable = true)
    protected List<Complemento> complementos;
    protected int idGrupo;
    protected int idTipoCobertura;
    protected String nombre;

    /**
     * Gets the value of the complementos property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the complementos property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComplementos().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Complemento }
     * 
     * 
     */
    public List<Complemento> getComplementos() {
        if (complementos == null) {
            complementos = new ArrayList<Complemento>();
        }
        return this.complementos;
    }

    /**
     * Obtiene el valor de la propiedad idGrupo.
     * 
     */
    public int getIdGrupo() {
        return idGrupo;
    }

    /**
     * Define el valor de la propiedad idGrupo.
     * 
     */
    public void setIdGrupo(int value) {
        this.idGrupo = value;
    }

    /**
     * Obtiene el valor de la propiedad idTipoCobertura.
     * 
     */
    public int getIdTipoCobertura() {
        return idTipoCobertura;
    }

    /**
     * Define el valor de la propiedad idTipoCobertura.
     * 
     */
    public void setIdTipoCobertura(int value) {
        this.idTipoCobertura = value;
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
