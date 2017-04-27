
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para monto complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="monto">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="amparada" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="editable" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="idTipoMonto" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="incremento" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="montoAbierto" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="montoInicial" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="montofinal" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="tipoUnidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="valorActual" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="valores" type="{http://www.w3.org/2001/XMLSchema}int" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "monto", propOrder = {
    "amparada",
    "editable",
    "idTipoMonto",
    "incremento",
    "montoAbierto",
    "montoInicial",
    "montofinal",
    "tipoUnidad",
    "valorActual",
    "valores"
})
public class Monto {

    protected boolean amparada;
    protected boolean editable;
    protected int idTipoMonto;
    protected int incremento;
    protected boolean montoAbierto;
    protected int montoInicial;
    protected int montofinal;
    protected String tipoUnidad;
    protected int valorActual;
    @XmlElement(nillable = true)
    protected List<Integer> valores;

    /**
     * Obtiene el valor de la propiedad amparada.
     * 
     */
    public boolean isAmparada() {
        return amparada;
    }

    /**
     * Define el valor de la propiedad amparada.
     * 
     */
    public void setAmparada(boolean value) {
        this.amparada = value;
    }

    /**
     * Obtiene el valor de la propiedad editable.
     * 
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Define el valor de la propiedad editable.
     * 
     */
    public void setEditable(boolean value) {
        this.editable = value;
    }

    /**
     * Obtiene el valor de la propiedad idTipoMonto.
     * 
     */
    public int getIdTipoMonto() {
        return idTipoMonto;
    }

    /**
     * Define el valor de la propiedad idTipoMonto.
     * 
     */
    public void setIdTipoMonto(int value) {
        this.idTipoMonto = value;
    }

    /**
     * Obtiene el valor de la propiedad incremento.
     * 
     */
    public int getIncremento() {
        return incremento;
    }

    /**
     * Define el valor de la propiedad incremento.
     * 
     */
    public void setIncremento(int value) {
        this.incremento = value;
    }

    /**
     * Obtiene el valor de la propiedad montoAbierto.
     * 
     */
    public boolean isMontoAbierto() {
        return montoAbierto;
    }

    /**
     * Define el valor de la propiedad montoAbierto.
     * 
     */
    public void setMontoAbierto(boolean value) {
        this.montoAbierto = value;
    }

    /**
     * Obtiene el valor de la propiedad montoInicial.
     * 
     */
    public int getMontoInicial() {
        return montoInicial;
    }

    /**
     * Define el valor de la propiedad montoInicial.
     * 
     */
    public void setMontoInicial(int value) {
        this.montoInicial = value;
    }

    /**
     * Obtiene el valor de la propiedad montofinal.
     * 
     */
    public int getMontofinal() {
        return montofinal;
    }

    /**
     * Define el valor de la propiedad montofinal.
     * 
     */
    public void setMontofinal(int value) {
        this.montofinal = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoUnidad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoUnidad() {
        return tipoUnidad;
    }

    /**
     * Define el valor de la propiedad tipoUnidad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoUnidad(String value) {
        this.tipoUnidad = value;
    }

    /**
     * Obtiene el valor de la propiedad valorActual.
     * 
     */
    public int getValorActual() {
        return valorActual;
    }

    /**
     * Define el valor de la propiedad valorActual.
     * 
     */
    public void setValorActual(int value) {
        this.valorActual = value;
    }

    /**
     * Gets the value of the valores property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the valores property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getValores().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getValores() {
        if (valores == null) {
            valores = new ArrayList<Integer>();
        }
        return this.valores;
    }

}
