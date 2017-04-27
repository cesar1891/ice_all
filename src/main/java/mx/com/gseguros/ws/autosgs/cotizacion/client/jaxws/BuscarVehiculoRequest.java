
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para buscarVehiculoRequest complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="buscarVehiculoRequest">
 *   &lt;complexContent>
 *     &lt;extension base="{http://com.gs.cotizador.ws.cotizacionindividual}request">
 *       &lt;sequence>
 *         &lt;element name="filtro" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "buscarVehiculoRequest", propOrder = {
    "filtro",
    "version"
})
public class BuscarVehiculoRequest
    extends Request
{

    protected String filtro;
    protected int version;

    /**
     * Obtiene el valor de la propiedad filtro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFiltro() {
        return filtro;
    }

    /**
     * Define el valor de la propiedad filtro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFiltro(String value) {
        this.filtro = value;
    }

    /**
     * Obtiene el valor de la propiedad version.
     * 
     */
    public int getVersion() {
        return version;
    }

    /**
     * Define el valor de la propiedad version.
     * 
     */
    public void setVersion(int value) {
        this.version = value;
    }

}
