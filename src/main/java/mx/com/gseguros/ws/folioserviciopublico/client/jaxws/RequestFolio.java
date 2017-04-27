
package mx.com.gseguros.ws.folioserviciopublico.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para requestFolio complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="requestFolio">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="num_folio" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="sucursal_admin" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "requestFolio", propOrder = {
    "numFolio",
    "sucursalAdmin"
})
public class RequestFolio {

    @XmlElement(name = "num_folio")
    protected int numFolio;
    @XmlElement(name = "sucursal_admin")
    protected int sucursalAdmin;

    /**
     * Obtiene el valor de la propiedad numFolio.
     * 
     */
    public int getNumFolio() {
        return numFolio;
    }

    /**
     * Define el valor de la propiedad numFolio.
     * 
     */
    public void setNumFolio(int value) {
        this.numFolio = value;
    }

    /**
     * Obtiene el valor de la propiedad sucursalAdmin.
     * 
     */
    public int getSucursalAdmin() {
        return sucursalAdmin;
    }

    /**
     * Define el valor de la propiedad sucursalAdmin.
     * 
     */
    public void setSucursalAdmin(int value) {
        this.sucursalAdmin = value;
    }

}
