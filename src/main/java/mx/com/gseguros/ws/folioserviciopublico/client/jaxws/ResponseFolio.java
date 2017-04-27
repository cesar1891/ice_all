
package mx.com.gseguros.ws.folioserviciopublico.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para responseFolio complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="responseFolio">
 *   &lt;complexContent>
 *     &lt;extension base="{http://com.gs.folioserviciopublico.soap.folio}response">
 *       &lt;sequence>
 *         &lt;element name="folio" type="{http://com.gs.folioserviciopublico.soap.folio}emAdmfolId" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "responseFolio", propOrder = {
    "folio"
})
public class ResponseFolio
    extends Response
{

    protected EmAdmfolId folio;

    /**
     * Obtiene el valor de la propiedad folio.
     * 
     * @return
     *     possible object is
     *     {@link EmAdmfolId }
     *     
     */
    public EmAdmfolId getFolio() {
        return folio;
    }

    /**
     * Define el valor de la propiedad folio.
     * 
     * @param value
     *     allowed object is
     *     {@link EmAdmfolId }
     *     
     */
    public void setFolio(EmAdmfolId value) {
        this.folio = value;
    }

}
