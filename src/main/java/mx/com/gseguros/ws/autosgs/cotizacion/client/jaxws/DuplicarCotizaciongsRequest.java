
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para duplicarCotizaciongsRequest complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="duplicarCotizaciongsRequest">
 *   &lt;complexContent>
 *     &lt;extension base="{http://com.gs.cotizador.ws.cotizacionindividual}request">
 *       &lt;sequence>
 *         &lt;element name="idCotizaciongs" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "duplicarCotizaciongsRequest", propOrder = {
    "idCotizaciongs"
})
public class DuplicarCotizaciongsRequest
    extends Request
{

    protected int idCotizaciongs;

    /**
     * Obtiene el valor de la propiedad idCotizaciongs.
     * 
     */
    public int getIdCotizaciongs() {
        return idCotizaciongs;
    }

    /**
     * Define el valor de la propiedad idCotizaciongs.
     * 
     */
    public void setIdCotizaciongs(int value) {
        this.idCotizaciongs = value;
    }

}
