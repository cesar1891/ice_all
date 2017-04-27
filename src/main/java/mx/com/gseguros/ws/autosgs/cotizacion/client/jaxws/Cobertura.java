
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para cobertura complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="cobertura">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="comision" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="deducible" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="descuento_agente" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="error" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="grupo" type="{http://com.gs.cotizador.ws.cotizacionindividual}grupo" minOccurs="0"/>
 *         &lt;element name="idCobertura" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="idCoberturaPaquete" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="idTipoCobertura" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="monto" type="{http://com.gs.cotizador.ws.cotizacionindividual}monto" minOccurs="0"/>
 *         &lt;element name="mostrarsa" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="prima_bruta" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="prima_neta" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="seleccionado" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="suma_asegurada" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cobertura", propOrder = {
    "comision",
    "deducible",
    "descuentoAgente",
    "error",
    "grupo",
    "idCobertura",
    "idCoberturaPaquete",
    "idTipoCobertura",
    "monto",
    "mostrarsa",
    "nombre",
    "primaBruta",
    "primaNeta",
    "seleccionado",
    "sumaAsegurada"
})
public class Cobertura {

    protected double comision;
    protected double deducible;
    @XmlElement(name = "descuento_agente")
    protected double descuentoAgente;
    protected boolean error;
    protected Grupo grupo;
    protected Integer idCobertura;
    protected Integer idCoberturaPaquete;
    protected int idTipoCobertura;
    protected Monto monto;
    protected boolean mostrarsa;
    protected String nombre;
    @XmlElement(name = "prima_bruta")
    protected double primaBruta;
    @XmlElement(name = "prima_neta")
    protected double primaNeta;
    protected boolean seleccionado;
    @XmlElement(name = "suma_asegurada")
    protected double sumaAsegurada;

    /**
     * Obtiene el valor de la propiedad comision.
     * 
     */
    public double getComision() {
        return comision;
    }

    /**
     * Define el valor de la propiedad comision.
     * 
     */
    public void setComision(double value) {
        this.comision = value;
    }

    /**
     * Obtiene el valor de la propiedad deducible.
     * 
     */
    public double getDeducible() {
        return deducible;
    }

    /**
     * Define el valor de la propiedad deducible.
     * 
     */
    public void setDeducible(double value) {
        this.deducible = value;
    }

    /**
     * Obtiene el valor de la propiedad descuentoAgente.
     * 
     */
    public double getDescuentoAgente() {
        return descuentoAgente;
    }

    /**
     * Define el valor de la propiedad descuentoAgente.
     * 
     */
    public void setDescuentoAgente(double value) {
        this.descuentoAgente = value;
    }

    /**
     * Obtiene el valor de la propiedad error.
     * 
     */
    public boolean isError() {
        return error;
    }

    /**
     * Define el valor de la propiedad error.
     * 
     */
    public void setError(boolean value) {
        this.error = value;
    }

    /**
     * Obtiene el valor de la propiedad grupo.
     * 
     * @return
     *     possible object is
     *     {@link Grupo }
     *     
     */
    public Grupo getGrupo() {
        return grupo;
    }

    /**
     * Define el valor de la propiedad grupo.
     * 
     * @param value
     *     allowed object is
     *     {@link Grupo }
     *     
     */
    public void setGrupo(Grupo value) {
        this.grupo = value;
    }

    /**
     * Obtiene el valor de la propiedad idCobertura.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdCobertura() {
        return idCobertura;
    }

    /**
     * Define el valor de la propiedad idCobertura.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdCobertura(Integer value) {
        this.idCobertura = value;
    }

    /**
     * Obtiene el valor de la propiedad idCoberturaPaquete.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdCoberturaPaquete() {
        return idCoberturaPaquete;
    }

    /**
     * Define el valor de la propiedad idCoberturaPaquete.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdCoberturaPaquete(Integer value) {
        this.idCoberturaPaquete = value;
    }

    /**
     * Obtiene el valor de la propiedad idTipoCobertura.
     * 
     */
    public int getIdTipoCobertura() {
        return idTipoCobertura;
    }

    /**
     * Define el valor de la propiedad idTipoCobertura.
     * 
     */
    public void setIdTipoCobertura(int value) {
        this.idTipoCobertura = value;
    }

    /**
     * Obtiene el valor de la propiedad monto.
     * 
     * @return
     *     possible object is
     *     {@link Monto }
     *     
     */
    public Monto getMonto() {
        return monto;
    }

    /**
     * Define el valor de la propiedad monto.
     * 
     * @param value
     *     allowed object is
     *     {@link Monto }
     *     
     */
    public void setMonto(Monto value) {
        this.monto = value;
    }

    /**
     * Obtiene el valor de la propiedad mostrarsa.
     * 
     */
    public boolean isMostrarsa() {
        return mostrarsa;
    }

    /**
     * Define el valor de la propiedad mostrarsa.
     * 
     */
    public void setMostrarsa(boolean value) {
        this.mostrarsa = value;
    }

    /**
     * Obtiene el valor de la propiedad nombre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Define el valor de la propiedad nombre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Obtiene el valor de la propiedad primaBruta.
     * 
     */
    public double getPrimaBruta() {
        return primaBruta;
    }

    /**
     * Define el valor de la propiedad primaBruta.
     * 
     */
    public void setPrimaBruta(double value) {
        this.primaBruta = value;
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
     * Obtiene el valor de la propiedad seleccionado.
     * 
     */
    public boolean isSeleccionado() {
        return seleccionado;
    }

    /**
     * Define el valor de la propiedad seleccionado.
     * 
     */
    public void setSeleccionado(boolean value) {
        this.seleccionado = value;
    }

    /**
     * Obtiene el valor de la propiedad sumaAsegurada.
     * 
     */
    public double getSumaAsegurada() {
        return sumaAsegurada;
    }

    /**
     * Define el valor de la propiedad sumaAsegurada.
     * 
     */
    public void setSumaAsegurada(double value) {
        this.sumaAsegurada = value;
    }

}
