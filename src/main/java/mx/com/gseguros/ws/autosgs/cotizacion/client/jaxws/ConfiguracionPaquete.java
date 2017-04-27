
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para configuracionPaquete complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="configuracionPaquete">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="coberturasUnicas" type="{http://com.gs.cotizador.ws.cotizacionindividual}cobertura" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="idConfiguracionPaquete" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="montos" type="{http://www.w3.org/2001/XMLSchema}int" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="paquetes" type="{http://com.gs.cotizador.ws.cotizacionindividual}paquete" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="versionTarifa" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "configuracionPaquete", propOrder = {
    "coberturasUnicas",
    "idConfiguracionPaquete",
    "montos",
    "paquetes",
    "versionTarifa"
})
public class ConfiguracionPaquete {

    @XmlElement(nillable = true)
    protected List<Cobertura> coberturasUnicas;
    protected int idConfiguracionPaquete;
    @XmlElement(nillable = true)
    protected List<Integer> montos;
    @XmlElement(nillable = true)
    protected List<Paquete> paquetes;
    protected int versionTarifa;

    /**
     * Gets the value of the coberturasUnicas property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the coberturasUnicas property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCoberturasUnicas().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Cobertura }
     * 
     * 
     */
    public List<Cobertura> getCoberturasUnicas() {
        if (coberturasUnicas == null) {
            coberturasUnicas = new ArrayList<Cobertura>();
        }
        return this.coberturasUnicas;
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
     * Gets the value of the montos property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the montos property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMontos().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getMontos() {
        if (montos == null) {
            montos = new ArrayList<Integer>();
        }
        return this.montos;
    }

    /**
     * Gets the value of the paquetes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the paquetes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPaquetes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Paquete }
     * 
     * 
     */
    public List<Paquete> getPaquetes() {
        if (paquetes == null) {
            paquetes = new ArrayList<Paquete>();
        }
        return this.paquetes;
    }

    /**
     * Obtiene el valor de la propiedad versionTarifa.
     * 
     */
    public int getVersionTarifa() {
        return versionTarifa;
    }

    /**
     * Define el valor de la propiedad versionTarifa.
     * 
     */
    public void setVersionTarifa(int value) {
        this.versionTarifa = value;
    }

}
