
package mx.com.gseguros.ws.folioserviciopublico.model;

import java.io.Serializable;


public class ResponseFolioVO extends ResponseVO implements Serializable {

	private static final long serialVersionUID = -1919939398694232391L;
	
	protected EmAdmfolIdVO folio;

    /**
     * Obtiene el valor de la propiedad folio.
     * 
     * @return
     *     possible object is
     *     {@link EmAdmfolIdVO }
     *     
     */
    public EmAdmfolIdVO getFolio() {
        return folio;
    }

    /**
     * Define el valor de la propiedad folio.
     * 
     * @param value
     *     allowed object is
     *     {@link EmAdmfolIdVO }
     *     
     */
    public void setFolio(EmAdmfolIdVO value) {
        this.folio = value;
    }

}