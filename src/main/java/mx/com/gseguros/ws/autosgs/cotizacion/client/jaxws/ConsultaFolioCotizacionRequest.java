
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para consultaFolioCotizacionRequest complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="consultaFolioCotizacionRequest">
 *   &lt;complexContent>
 *     &lt;extension base="{http://com.gs.cotizador.ws.cotizacionindividual}request">
 *       &lt;sequence>
 *         &lt;element name="folio" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idAgente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idTipoProducto" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="numVersion" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultaFolioCotizacionRequest", propOrder = {
    "folio",
    "idAgente",
    "idTipoProducto",
    "numVersion"
})
public class ConsultaFolioCotizacionRequest
    extends Request
{

    protected int folio;
    protected String idAgente;
    protected int idTipoProducto;
    protected int numVersion;

    /**
     * Obtiene el valor de la propiedad folio.
     * 
     */
    public int getFolio() {
        return folio;
    }

    /**
     * Define el valor de la propiedad folio.
     * 
     */
    public void setFolio(int value) {
        this.folio = value;
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
     * Obtiene el valor de la propiedad idTipoProducto.
     * 
     */
    public int getIdTipoProducto() {
        return idTipoProducto;
    }

    /**
     * Define el valor de la propiedad idTipoProducto.
     * 
     */
    public void setIdTipoProducto(int value) {
        this.idTipoProducto = value;
    }

    /**
     * Obtiene el valor de la propiedad numVersion.
     * 
     */
    public int getNumVersion() {
        return numVersion;
    }

    /**
     * Define el valor de la propiedad numVersion.
     * 
     */
    public void setNumVersion(int value) {
        this.numVersion = value;
    }

}
