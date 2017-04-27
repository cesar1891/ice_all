
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para duplicarCotizaciongsResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="duplicarCotizaciongsResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://com.gs.cotizador.ws.cotizacionindividual}response">
 *       &lt;sequence>
 *         &lt;element name="idCotizacion" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "duplicarCotizaciongsResponse", propOrder = {
    "idCotizacion"
})
public class DuplicarCotizaciongsResponse
    extends Response
{

    protected int idCotizacion;

    /**
     * Obtiene el valor de la propiedad idCotizacion.
     * 
     */
    public int getIdCotizacion() {
        return idCotizacion;
    }

    /**
     * Define el valor de la propiedad idCotizacion.
     * 
     */
    public void setIdCotizacion(int value) {
        this.idCotizacion = value;
    }

}
