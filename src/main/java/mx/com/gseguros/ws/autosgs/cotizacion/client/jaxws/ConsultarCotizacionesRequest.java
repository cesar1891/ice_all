
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para consultarCotizacionesRequest complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="consultarCotizacionesRequest">
 *   &lt;complexContent>
 *     &lt;extension base="{http://com.gs.cotizador.ws.cotizacionindividual}request">
 *       &lt;sequence>
 *         &lt;element name="cveCliente" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="estatus" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="fechaCotizacion" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="numAgente" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="numCotizacion" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="prospecto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarCotizacionesRequest", propOrder = {
    "cveCliente",
    "estatus",
    "fechaCotizacion",
    "numAgente",
    "numCotizacion",
    "prospecto"
})
public class ConsultarCotizacionesRequest
    extends Request
{

    protected int cveCliente;
    protected int estatus;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaCotizacion;
    protected int numAgente;
    protected int numCotizacion;
    protected String prospecto;

    /**
     * Obtiene el valor de la propiedad cveCliente.
     * 
     */
    public int getCveCliente() {
        return cveCliente;
    }

    /**
     * Define el valor de la propiedad cveCliente.
     * 
     */
    public void setCveCliente(int value) {
        this.cveCliente = value;
    }

    /**
     * Obtiene el valor de la propiedad estatus.
     * 
     */
    public int getEstatus() {
        return estatus;
    }

    /**
     * Define el valor de la propiedad estatus.
     * 
     */
    public void setEstatus(int value) {
        this.estatus = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaCotizacion.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaCotizacion() {
        return fechaCotizacion;
    }

    /**
     * Define el valor de la propiedad fechaCotizacion.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaCotizacion(XMLGregorianCalendar value) {
        this.fechaCotizacion = value;
    }

    /**
     * Obtiene el valor de la propiedad numAgente.
     * 
     */
    public int getNumAgente() {
        return numAgente;
    }

    /**
     * Define el valor de la propiedad numAgente.
     * 
     */
    public void setNumAgente(int value) {
        this.numAgente = value;
    }

    /**
     * Obtiene el valor de la propiedad numCotizacion.
     * 
     */
    public int getNumCotizacion() {
        return numCotizacion;
    }

    /**
     * Define el valor de la propiedad numCotizacion.
     * 
     */
    public void setNumCotizacion(int value) {
        this.numCotizacion = value;
    }

    /**
     * Obtiene el valor de la propiedad prospecto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProspecto() {
        return prospecto;
    }

    /**
     * Define el valor de la propiedad prospecto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProspecto(String value) {
        this.prospecto = value;
    }

}
