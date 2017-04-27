
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para configuracionPaqueteRequest complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="configuracionPaqueteRequest">
 *   &lt;complexContent>
 *     &lt;extension base="{http://com.gs.cotizador.ws.cotizacionindividual}request">
 *       &lt;sequence>
 *         &lt;element name="amis" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="cp" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idProducto" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idRamo" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idTipoServicio" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idTipoVehiculo" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="id_estado" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="modelo" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
@XmlType(name = "configuracionPaqueteRequest", propOrder = {
    "amis",
    "cp",
    "idProducto",
    "idRamo",
    "idTipoServicio",
    "idTipoVehiculo",
    "idEstado",
    "modelo",
    "version"
})
public class ConfiguracionPaqueteRequest
    extends Request
{

    protected int amis;
    protected int cp;
    protected int idProducto;
    protected int idRamo;
    protected int idTipoServicio;
    protected int idTipoVehiculo;
    @XmlElement(name = "id_estado")
    protected int idEstado;
    protected int modelo;
    protected int version;

    /**
     * Obtiene el valor de la propiedad amis.
     * 
     */
    public int getAmis() {
        return amis;
    }

    /**
     * Define el valor de la propiedad amis.
     * 
     */
    public void setAmis(int value) {
        this.amis = value;
    }

    /**
     * Obtiene el valor de la propiedad cp.
     * 
     */
    public int getCp() {
        return cp;
    }

    /**
     * Define el valor de la propiedad cp.
     * 
     */
    public void setCp(int value) {
        this.cp = value;
    }

    /**
     * Obtiene el valor de la propiedad idProducto.
     * 
     */
    public int getIdProducto() {
        return idProducto;
    }

    /**
     * Define el valor de la propiedad idProducto.
     * 
     */
    public void setIdProducto(int value) {
        this.idProducto = value;
    }

    /**
     * Obtiene el valor de la propiedad idRamo.
     * 
     */
    public int getIdRamo() {
        return idRamo;
    }

    /**
     * Define el valor de la propiedad idRamo.
     * 
     */
    public void setIdRamo(int value) {
        this.idRamo = value;
    }

    /**
     * Obtiene el valor de la propiedad idTipoServicio.
     * 
     */
    public int getIdTipoServicio() {
        return idTipoServicio;
    }

    /**
     * Define el valor de la propiedad idTipoServicio.
     * 
     */
    public void setIdTipoServicio(int value) {
        this.idTipoServicio = value;
    }

    /**
     * Obtiene el valor de la propiedad idTipoVehiculo.
     * 
     */
    public int getIdTipoVehiculo() {
        return idTipoVehiculo;
    }

    /**
     * Define el valor de la propiedad idTipoVehiculo.
     * 
     */
    public void setIdTipoVehiculo(int value) {
        this.idTipoVehiculo = value;
    }

    /**
     * Obtiene el valor de la propiedad idEstado.
     * 
     */
    public int getIdEstado() {
        return idEstado;
    }

    /**
     * Define el valor de la propiedad idEstado.
     * 
     */
    public void setIdEstado(int value) {
        this.idEstado = value;
    }

    /**
     * Obtiene el valor de la propiedad modelo.
     * 
     */
    public int getModelo() {
        return modelo;
    }

    /**
     * Define el valor de la propiedad modelo.
     * 
     */
    public void setModelo(int value) {
        this.modelo = value;
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
