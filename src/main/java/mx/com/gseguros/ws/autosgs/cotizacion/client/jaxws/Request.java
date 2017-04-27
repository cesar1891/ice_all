
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para request complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="request">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigo" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "request", propOrder = {
    "codigo"
})
@XmlSeeAlso({
    DuplicarCotizaciongsRequest.class,
    ConfiguracionPaqueteRequest.class,
    BuscarCPRequest.class,
    EmisionPolizaRequest.class,
    ObtenerTotalesFormaPagoRequest.class,
    EstatusCotizacionRequest.class,
    BuscarVehiculoRequest.class,
    ConsultaFolioCotizacionRequest.class,
    CotizacionRequest.class,
    ConsultarCotizacionesRequest.class
})
public class Request {

    protected long codigo;

    /**
     * Obtiene el valor de la propiedad codigo.
     * 
     */
    public long getCodigo() {
        return codigo;
    }

    /**
     * Define el valor de la propiedad codigo.
     * 
     */
    public void setCodigo(long value) {
        this.codigo = value;
    }

}
