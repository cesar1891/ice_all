
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para totalFormaPagoDTO complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="totalFormaPagoDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="derechoPoliza" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="iva" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="pagoSubSecuente" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="primaNeta" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="primerPago" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="recargoPagoFraccionado" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="subTotal" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="total" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "totalFormaPagoDTO", propOrder = {
    "derechoPoliza",
    "iva",
    "pagoSubSecuente",
    "primaNeta",
    "primerPago",
    "recargoPagoFraccionado",
    "subTotal",
    "total"
})
public class TotalFormaPagoDTO {

    protected double derechoPoliza;
    protected double iva;
    protected double pagoSubSecuente;
    protected double primaNeta;
    protected double primerPago;
    protected double recargoPagoFraccionado;
    protected double subTotal;
    protected double total;

    /**
     * Obtiene el valor de la propiedad derechoPoliza.
     * 
     */
    public double getDerechoPoliza() {
        return derechoPoliza;
    }

    /**
     * Define el valor de la propiedad derechoPoliza.
     * 
     */
    public void setDerechoPoliza(double value) {
        this.derechoPoliza = value;
    }

    /**
     * Obtiene el valor de la propiedad iva.
     * 
     */
    public double getIva() {
        return iva;
    }

    /**
     * Define el valor de la propiedad iva.
     * 
     */
    public void setIva(double value) {
        this.iva = value;
    }

    /**
     * Obtiene el valor de la propiedad pagoSubSecuente.
     * 
     */
    public double getPagoSubSecuente() {
        return pagoSubSecuente;
    }

    /**
     * Define el valor de la propiedad pagoSubSecuente.
     * 
     */
    public void setPagoSubSecuente(double value) {
        this.pagoSubSecuente = value;
    }

    /**
     * Obtiene el valor de la propiedad primaNeta.
     * 
     */
    public double getPrimaNeta() {
        return primaNeta;
    }

    /**
     * Define el valor de la propiedad primaNeta.
     * 
     */
    public void setPrimaNeta(double value) {
        this.primaNeta = value;
    }

    /**
     * Obtiene el valor de la propiedad primerPago.
     * 
     */
    public double getPrimerPago() {
        return primerPago;
    }

    /**
     * Define el valor de la propiedad primerPago.
     * 
     */
    public void setPrimerPago(double value) {
        this.primerPago = value;
    }

    /**
     * Obtiene el valor de la propiedad recargoPagoFraccionado.
     * 
     */
    public double getRecargoPagoFraccionado() {
        return recargoPagoFraccionado;
    }

    /**
     * Define el valor de la propiedad recargoPagoFraccionado.
     * 
     */
    public void setRecargoPagoFraccionado(double value) {
        this.recargoPagoFraccionado = value;
    }

    /**
     * Obtiene el valor de la propiedad subTotal.
     * 
     */
    public double getSubTotal() {
        return subTotal;
    }

    /**
     * Define el valor de la propiedad subTotal.
     * 
     */
    public void setSubTotal(double value) {
        this.subTotal = value;
    }

    /**
     * Obtiene el valor de la propiedad total.
     * 
     */
    public double getTotal() {
        return total;
    }

    /**
     * Define el valor de la propiedad total.
     * 
     */
    public void setTotal(double value) {
        this.total = value;
    }

}
