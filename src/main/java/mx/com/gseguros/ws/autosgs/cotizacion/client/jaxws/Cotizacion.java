
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para cotizacion complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="cotizacion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="agente" type="{http://com.gs.cotizador.ws.cotizacionindividual}agente" minOccurs="0"/>
 *         &lt;element name="claveGS" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="cliente" type="{KB_ObtenerClientes}SDTClientes.SDTClientesItem" minOccurs="0"/>
 *         &lt;element name="codigoPostal" type="{http://com.gs.cotizador.ws.cotizacionindividual}codigoPostal" minOccurs="0"/>
 *         &lt;element name="codigoPostalVacio" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="cveUsuarioCaptura" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="descuentoAgente" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="descuentoMaximoAgente" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="finVigencia" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="formaPago" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="formasDePago">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *                             &lt;element name="value" type="{http://com.gs.cotizador.ws.cotizacionindividual}arrayList" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="idAgenteCompartido" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idBanco" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idCotizacion" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idEstatusCotizacion" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idIncisoActual" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idOrigenSolicitud" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="incisos" type="{http://com.gs.cotizador.ws.cotizacionindividual}inciso" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="inicioVigencia" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="mesesSinIntereses" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="moneda" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="numFolio" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="porcenComisionAgente2" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="prospecto" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="sdtPoliza" type="{KB_WSEmisionPoliza}SDTPoliza" minOccurs="0"/>
 *         &lt;element name="tipoProducto" type="{http://com.gs.cotizador.ws.cotizacionindividual}tipoProducto" minOccurs="0"/>
 *         &lt;element name="tipoServicio" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="totalFormaPago" type="{http://com.gs.cotizador.ws.cotizacionindividual}totalFormaPago" minOccurs="0"/>
 *         &lt;element name="versionTarifa" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="vigencia" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cotizacion", propOrder = {
    "agente",
    "claveGS",
    "cliente",
    "codigoPostal",
    "codigoPostalVacio",
    "cveUsuarioCaptura",
    "descuentoAgente",
    "descuentoMaximoAgente",
    "finVigencia",
    "formaPago",
    "formasDePago",
    "idAgenteCompartido",
    "idBanco",
    "idCotizacion",
    "idEstatusCotizacion",
    "idIncisoActual",
    "idOrigenSolicitud",
    "incisos",
    "inicioVigencia",
    "mesesSinIntereses",
    "moneda",
    "numFolio",
    "porcenComisionAgente2",
    "prospecto",
    "sdtPoliza",
    "tipoProducto",
    "tipoServicio",
    "totalFormaPago",
    "versionTarifa",
    "vigencia"
})
public class Cotizacion {

    protected Agente agente;
    protected int claveGS;
    protected SDTClientesSDTClientesItem cliente;
    protected CodigoPostal codigoPostal;
    protected boolean codigoPostalVacio;
    protected int cveUsuarioCaptura;
    protected int descuentoAgente;
    protected int descuentoMaximoAgente;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar finVigencia;
    protected int formaPago;
    @XmlElement(required = true)
    protected Cotizacion.FormasDePago formasDePago;
    protected int idAgenteCompartido;
    protected int idBanco;
    protected int idCotizacion;
    protected int idEstatusCotizacion;
    protected int idIncisoActual;
    protected int idOrigenSolicitud;
    @XmlElement(nillable = true)
    protected List<Inciso> incisos;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar inicioVigencia;
    protected int mesesSinIntereses;
    protected int moneda;
    protected int numFolio;
    protected int porcenComisionAgente2;
    protected boolean prospecto;
    protected SDTPoliza sdtPoliza;
    protected TipoProducto tipoProducto;
    protected int tipoServicio;
    protected TotalFormaPago totalFormaPago;
    protected int versionTarifa;
    protected int vigencia;

    /**
     * Obtiene el valor de la propiedad agente.
     * 
     * @return
     *     possible object is
     *     {@link Agente }
     *     
     */
    public Agente getAgente() {
        return agente;
    }

    /**
     * Define el valor de la propiedad agente.
     * 
     * @param value
     *     allowed object is
     *     {@link Agente }
     *     
     */
    public void setAgente(Agente value) {
        this.agente = value;
    }

    /**
     * Obtiene el valor de la propiedad claveGS.
     * 
     */
    public int getClaveGS() {
        return claveGS;
    }

    /**
     * Define el valor de la propiedad claveGS.
     * 
     */
    public void setClaveGS(int value) {
        this.claveGS = value;
    }

    /**
     * Obtiene el valor de la propiedad cliente.
     * 
     * @return
     *     possible object is
     *     {@link SDTClientesSDTClientesItem }
     *     
     */
    public SDTClientesSDTClientesItem getCliente() {
        return cliente;
    }

    /**
     * Define el valor de la propiedad cliente.
     * 
     * @param value
     *     allowed object is
     *     {@link SDTClientesSDTClientesItem }
     *     
     */
    public void setCliente(SDTClientesSDTClientesItem value) {
        this.cliente = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoPostal.
     * 
     * @return
     *     possible object is
     *     {@link CodigoPostal }
     *     
     */
    public CodigoPostal getCodigoPostal() {
        return codigoPostal;
    }

    /**
     * Define el valor de la propiedad codigoPostal.
     * 
     * @param value
     *     allowed object is
     *     {@link CodigoPostal }
     *     
     */
    public void setCodigoPostal(CodigoPostal value) {
        this.codigoPostal = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoPostalVacio.
     * 
     */
    public boolean isCodigoPostalVacio() {
        return codigoPostalVacio;
    }

    /**
     * Define el valor de la propiedad codigoPostalVacio.
     * 
     */
    public void setCodigoPostalVacio(boolean value) {
        this.codigoPostalVacio = value;
    }

    /**
     * Obtiene el valor de la propiedad cveUsuarioCaptura.
     * 
     */
    public int getCveUsuarioCaptura() {
        return cveUsuarioCaptura;
    }

    /**
     * Define el valor de la propiedad cveUsuarioCaptura.
     * 
     */
    public void setCveUsuarioCaptura(int value) {
        this.cveUsuarioCaptura = value;
    }

    /**
     * Obtiene el valor de la propiedad descuentoAgente.
     * 
     */
    public int getDescuentoAgente() {
        return descuentoAgente;
    }

    /**
     * Define el valor de la propiedad descuentoAgente.
     * 
     */
    public void setDescuentoAgente(int value) {
        this.descuentoAgente = value;
    }

    /**
     * Obtiene el valor de la propiedad descuentoMaximoAgente.
     * 
     */
    public int getDescuentoMaximoAgente() {
        return descuentoMaximoAgente;
    }

    /**
     * Define el valor de la propiedad descuentoMaximoAgente.
     * 
     */
    public void setDescuentoMaximoAgente(int value) {
        this.descuentoMaximoAgente = value;
    }

    /**
     * Obtiene el valor de la propiedad finVigencia.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFinVigencia() {
        return finVigencia;
    }

    /**
     * Define el valor de la propiedad finVigencia.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFinVigencia(XMLGregorianCalendar value) {
        this.finVigencia = value;
    }

    /**
     * Obtiene el valor de la propiedad formaPago.
     * 
     */
    public int getFormaPago() {
        return formaPago;
    }

    /**
     * Define el valor de la propiedad formaPago.
     * 
     */
    public void setFormaPago(int value) {
        this.formaPago = value;
    }

    /**
     * Obtiene el valor de la propiedad formasDePago.
     * 
     * @return
     *     possible object is
     *     {@link Cotizacion.FormasDePago }
     *     
     */
    public Cotizacion.FormasDePago getFormasDePago() {
        return formasDePago;
    }

    /**
     * Define el valor de la propiedad formasDePago.
     * 
     * @param value
     *     allowed object is
     *     {@link Cotizacion.FormasDePago }
     *     
     */
    public void setFormasDePago(Cotizacion.FormasDePago value) {
        this.formasDePago = value;
    }

    /**
     * Obtiene el valor de la propiedad idAgenteCompartido.
     * 
     */
    public int getIdAgenteCompartido() {
        return idAgenteCompartido;
    }

    /**
     * Define el valor de la propiedad idAgenteCompartido.
     * 
     */
    public void setIdAgenteCompartido(int value) {
        this.idAgenteCompartido = value;
    }

    /**
     * Obtiene el valor de la propiedad idBanco.
     * 
     */
    public int getIdBanco() {
        return idBanco;
    }

    /**
     * Define el valor de la propiedad idBanco.
     * 
     */
    public void setIdBanco(int value) {
        this.idBanco = value;
    }

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

    /**
     * Obtiene el valor de la propiedad idEstatusCotizacion.
     * 
     */
    public int getIdEstatusCotizacion() {
        return idEstatusCotizacion;
    }

    /**
     * Define el valor de la propiedad idEstatusCotizacion.
     * 
     */
    public void setIdEstatusCotizacion(int value) {
        this.idEstatusCotizacion = value;
    }

    /**
     * Obtiene el valor de la propiedad idIncisoActual.
     * 
     */
    public int getIdIncisoActual() {
        return idIncisoActual;
    }

    /**
     * Define el valor de la propiedad idIncisoActual.
     * 
     */
    public void setIdIncisoActual(int value) {
        this.idIncisoActual = value;
    }

    /**
     * Obtiene el valor de la propiedad idOrigenSolicitud.
     * 
     */
    public int getIdOrigenSolicitud() {
        return idOrigenSolicitud;
    }

    /**
     * Define el valor de la propiedad idOrigenSolicitud.
     * 
     */
    public void setIdOrigenSolicitud(int value) {
        this.idOrigenSolicitud = value;
    }

    /**
     * Gets the value of the incisos property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the incisos property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIncisos().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Inciso }
     * 
     * 
     */
    public List<Inciso> getIncisos() {
        if (incisos == null) {
            incisos = new java.util.ArrayList<Inciso>();
        }
        return this.incisos;
    }

    /**
     * Obtiene el valor de la propiedad inicioVigencia.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getInicioVigencia() {
        return inicioVigencia;
    }

    /**
     * Define el valor de la propiedad inicioVigencia.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setInicioVigencia(XMLGregorianCalendar value) {
        this.inicioVigencia = value;
    }

    /**
     * Obtiene el valor de la propiedad mesesSinIntereses.
     * 
     */
    public int getMesesSinIntereses() {
        return mesesSinIntereses;
    }

    /**
     * Define el valor de la propiedad mesesSinIntereses.
     * 
     */
    public void setMesesSinIntereses(int value) {
        this.mesesSinIntereses = value;
    }

    /**
     * Obtiene el valor de la propiedad moneda.
     * 
     */
    public int getMoneda() {
        return moneda;
    }

    /**
     * Define el valor de la propiedad moneda.
     * 
     */
    public void setMoneda(int value) {
        this.moneda = value;
    }

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
     * Obtiene el valor de la propiedad porcenComisionAgente2.
     * 
     */
    public int getPorcenComisionAgente2() {
        return porcenComisionAgente2;
    }

    /**
     * Define el valor de la propiedad porcenComisionAgente2.
     * 
     */
    public void setPorcenComisionAgente2(int value) {
        this.porcenComisionAgente2 = value;
    }

    /**
     * Obtiene el valor de la propiedad prospecto.
     * 
     */
    public boolean isProspecto() {
        return prospecto;
    }

    /**
     * Define el valor de la propiedad prospecto.
     * 
     */
    public void setProspecto(boolean value) {
        this.prospecto = value;
    }

    /**
     * Obtiene el valor de la propiedad sdtPoliza.
     * 
     * @return
     *     possible object is
     *     {@link SDTPoliza }
     *     
     */
    public SDTPoliza getSdtPoliza() {
        return sdtPoliza;
    }

    /**
     * Define el valor de la propiedad sdtPoliza.
     * 
     * @param value
     *     allowed object is
     *     {@link SDTPoliza }
     *     
     */
    public void setSdtPoliza(SDTPoliza value) {
        this.sdtPoliza = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoProducto.
     * 
     * @return
     *     possible object is
     *     {@link TipoProducto }
     *     
     */
    public TipoProducto getTipoProducto() {
        return tipoProducto;
    }

    /**
     * Define el valor de la propiedad tipoProducto.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoProducto }
     *     
     */
    public void setTipoProducto(TipoProducto value) {
        this.tipoProducto = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoServicio.
     * 
     */
    public int getTipoServicio() {
        return tipoServicio;
    }

    /**
     * Define el valor de la propiedad tipoServicio.
     * 
     */
    public void setTipoServicio(int value) {
        this.tipoServicio = value;
    }

    /**
     * Obtiene el valor de la propiedad totalFormaPago.
     * 
     * @return
     *     possible object is
     *     {@link TotalFormaPago }
     *     
     */
    public TotalFormaPago getTotalFormaPago() {
        return totalFormaPago;
    }

    /**
     * Define el valor de la propiedad totalFormaPago.
     * 
     * @param value
     *     allowed object is
     *     {@link TotalFormaPago }
     *     
     */
    public void setTotalFormaPago(TotalFormaPago value) {
        this.totalFormaPago = value;
    }

    /**
     * Obtiene el valor de la propiedad versionTarifa.
     * 
     */
    public int getVersionTarifa() {
        return versionTarifa;
    }

    /**
     * Define el valor de la propiedad versionTarifa.
     * 
     */
    public void setVersionTarifa(int value) {
        this.versionTarifa = value;
    }

    /**
     * Obtiene el valor de la propiedad vigencia.
     * 
     */
    public int getVigencia() {
        return vigencia;
    }

    /**
     * Define el valor de la propiedad vigencia.
     * 
     */
    public void setVigencia(int value) {
        this.vigencia = value;
    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
     *                   &lt;element name="value" type="{http://com.gs.cotizador.ws.cotizacionindividual}arrayList" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "entry"
    })
    public static class FormasDePago {

        protected List<Cotizacion.FormasDePago.Entry> entry;

        /**
         * Gets the value of the entry property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the entry property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getEntry().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Cotizacion.FormasDePago.Entry }
         * 
         * 
         */
        public List<Cotizacion.FormasDePago.Entry> getEntry() {
            if (entry == null) {
                entry = new java.util.ArrayList<Cotizacion.FormasDePago.Entry>();
            }
            return this.entry;
        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
         *         &lt;element name="value" type="{http://com.gs.cotizador.ws.cotizacionindividual}arrayList" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "key",
            "value"
        })
        public static class Entry {

            protected Integer key;
            protected mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws.ArrayList value;

            /**
             * Obtiene el valor de la propiedad key.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getKey() {
                return key;
            }

            /**
             * Define el valor de la propiedad key.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setKey(Integer value) {
                this.key = value;
            }

            /**
             * Obtiene el valor de la propiedad value.
             * 
             * @return
             *     possible object is
             *     {@link mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws.ArrayList }
             *     
             */
            public mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws.ArrayList getValue() {
                return value;
            }

            /**
             * Define el valor de la propiedad value.
             * 
             * @param value
             *     allowed object is
             *     {@link mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws.ArrayList }
             *     
             */
            public void setValue(mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws.ArrayList value) {
                this.value = value;
            }

        }

    }

}
