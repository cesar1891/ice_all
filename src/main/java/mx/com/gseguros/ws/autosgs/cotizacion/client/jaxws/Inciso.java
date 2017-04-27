
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para inciso complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="inciso">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="adaptaciones" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="beneficiarioPref" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="capacidadTonelaje" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="conductor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="configuracionPaquete" type="{http://com.gs.cotizador.ws.cotizacionindividual}configuracionPaquete" minOccurs="0"/>
 *         &lt;element name="descripcionVehiculo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="equipoEspecial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="formaPago" type="{http://com.gs.cotizador.ws.cotizacionindividual}formaPago" minOccurs="0"/>
 *         &lt;element name="idInciso" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idPaqueteSeleccionado" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="idSegmento" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idTipoCarga" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idTipoValor" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="marca" type="{http://com.gs.cotizador.ws.catalogos}marca" minOccurs="0"/>
 *         &lt;element name="mayorDeDiezAnios" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="menorDeTreinta" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="modelo" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="modeloMayorAVeinte" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="numEconomico" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numMotor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numOcupantes" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="numPlacas" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numSerie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="primaNeta" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="submarca" type="{http://com.gs.cotizador.ws.catalogos}submarca" minOccurs="0"/>
 *         &lt;element name="tipVehiCA" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="tipoUso" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="tipoVehiculo" type="{http://com.gs.cotizador.ws.cotizacionindividual}tipoVehiculo" minOccurs="0"/>
 *         &lt;element name="valor" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="version" type="{http://com.gs.cotizador.ws.catalogos}version" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "inciso", propOrder = {
    "adaptaciones",
    "beneficiarioPref",
    "capacidadTonelaje",
    "conductor",
    "configuracionPaquete",
    "descripcionVehiculo",
    "equipoEspecial",
    "formaPago",
    "idInciso",
    "idPaqueteSeleccionado",
    "idSegmento",
    "idTipoCarga",
    "idTipoValor",
    "marca",
    "mayorDeDiezAnios",
    "menorDeTreinta",
    "modelo",
    "modeloMayorAVeinte",
    "numEconomico",
    "numMotor",
    "numOcupantes",
    "numPlacas",
    "numSerie",
    "primaNeta",
    "submarca",
    "tipVehiCA",
    "tipoUso",
    "tipoVehiculo",
    "valor",
    "version"
})
public class Inciso {

    protected String adaptaciones;
    protected String beneficiarioPref;
    protected int capacidadTonelaje;
    protected String conductor;
    protected ConfiguracionPaquete configuracionPaquete;
    protected String descripcionVehiculo;
    protected String equipoEspecial;
    protected FormaPago formaPago;
    protected int idInciso;
    protected Integer idPaqueteSeleccionado;
    protected int idSegmento;
    protected int idTipoCarga;
    protected int idTipoValor;
    protected Marca marca;
    protected boolean mayorDeDiezAnios;
    protected boolean menorDeTreinta;
    protected int modelo;
    protected boolean modeloMayorAVeinte;
    protected String numEconomico;
    protected String numMotor;
    protected int numOcupantes;
    protected String numPlacas;
    protected String numSerie;
    protected double primaNeta;
    protected Submarca submarca;
    protected int tipVehiCA;
    protected int tipoUso;
    protected TipoVehiculo tipoVehiculo;
    protected double valor;
    protected Version version;

    /**
     * Obtiene el valor de la propiedad adaptaciones.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdaptaciones() {
        return adaptaciones;
    }

    /**
     * Define el valor de la propiedad adaptaciones.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdaptaciones(String value) {
        this.adaptaciones = value;
    }

    /**
     * Obtiene el valor de la propiedad beneficiarioPref.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeneficiarioPref() {
        return beneficiarioPref;
    }

    /**
     * Define el valor de la propiedad beneficiarioPref.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeneficiarioPref(String value) {
        this.beneficiarioPref = value;
    }

    /**
     * Obtiene el valor de la propiedad capacidadTonelaje.
     * 
     */
    public int getCapacidadTonelaje() {
        return capacidadTonelaje;
    }

    /**
     * Define el valor de la propiedad capacidadTonelaje.
     * 
     */
    public void setCapacidadTonelaje(int value) {
        this.capacidadTonelaje = value;
    }

    /**
     * Obtiene el valor de la propiedad conductor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConductor() {
        return conductor;
    }

    /**
     * Define el valor de la propiedad conductor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConductor(String value) {
        this.conductor = value;
    }

    /**
     * Obtiene el valor de la propiedad configuracionPaquete.
     * 
     * @return
     *     possible object is
     *     {@link ConfiguracionPaquete }
     *     
     */
    public ConfiguracionPaquete getConfiguracionPaquete() {
        return configuracionPaquete;
    }

    /**
     * Define el valor de la propiedad configuracionPaquete.
     * 
     * @param value
     *     allowed object is
     *     {@link ConfiguracionPaquete }
     *     
     */
    public void setConfiguracionPaquete(ConfiguracionPaquete value) {
        this.configuracionPaquete = value;
    }

    /**
     * Obtiene el valor de la propiedad descripcionVehiculo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionVehiculo() {
        return descripcionVehiculo;
    }

    /**
     * Define el valor de la propiedad descripcionVehiculo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionVehiculo(String value) {
        this.descripcionVehiculo = value;
    }

    /**
     * Obtiene el valor de la propiedad equipoEspecial.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEquipoEspecial() {
        return equipoEspecial;
    }

    /**
     * Define el valor de la propiedad equipoEspecial.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEquipoEspecial(String value) {
        this.equipoEspecial = value;
    }

    /**
     * Obtiene el valor de la propiedad formaPago.
     * 
     * @return
     *     possible object is
     *     {@link FormaPago }
     *     
     */
    public FormaPago getFormaPago() {
        return formaPago;
    }

    /**
     * Define el valor de la propiedad formaPago.
     * 
     * @param value
     *     allowed object is
     *     {@link FormaPago }
     *     
     */
    public void setFormaPago(FormaPago value) {
        this.formaPago = value;
    }

    /**
     * Obtiene el valor de la propiedad idInciso.
     * 
     */
    public int getIdInciso() {
        return idInciso;
    }

    /**
     * Define el valor de la propiedad idInciso.
     * 
     */
    public void setIdInciso(int value) {
        this.idInciso = value;
    }

    /**
     * Obtiene el valor de la propiedad idPaqueteSeleccionado.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdPaqueteSeleccionado() {
        return idPaqueteSeleccionado;
    }

    /**
     * Define el valor de la propiedad idPaqueteSeleccionado.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdPaqueteSeleccionado(Integer value) {
        this.idPaqueteSeleccionado = value;
    }

    /**
     * Obtiene el valor de la propiedad idSegmento.
     * 
     */
    public int getIdSegmento() {
        return idSegmento;
    }

    /**
     * Define el valor de la propiedad idSegmento.
     * 
     */
    public void setIdSegmento(int value) {
        this.idSegmento = value;
    }

    /**
     * Obtiene el valor de la propiedad idTipoCarga.
     * 
     */
    public int getIdTipoCarga() {
        return idTipoCarga;
    }

    /**
     * Define el valor de la propiedad idTipoCarga.
     * 
     */
    public void setIdTipoCarga(int value) {
        this.idTipoCarga = value;
    }

    /**
     * Obtiene el valor de la propiedad idTipoValor.
     * 
     */
    public int getIdTipoValor() {
        return idTipoValor;
    }

    /**
     * Define el valor de la propiedad idTipoValor.
     * 
     */
    public void setIdTipoValor(int value) {
        this.idTipoValor = value;
    }

    /**
     * Obtiene el valor de la propiedad marca.
     * 
     * @return
     *     possible object is
     *     {@link Marca }
     *     
     */
    public Marca getMarca() {
        return marca;
    }

    /**
     * Define el valor de la propiedad marca.
     * 
     * @param value
     *     allowed object is
     *     {@link Marca }
     *     
     */
    public void setMarca(Marca value) {
        this.marca = value;
    }

    /**
     * Obtiene el valor de la propiedad mayorDeDiezAnios.
     * 
     */
    public boolean isMayorDeDiezAnios() {
        return mayorDeDiezAnios;
    }

    /**
     * Define el valor de la propiedad mayorDeDiezAnios.
     * 
     */
    public void setMayorDeDiezAnios(boolean value) {
        this.mayorDeDiezAnios = value;
    }

    /**
     * Obtiene el valor de la propiedad menorDeTreinta.
     * 
     */
    public boolean isMenorDeTreinta() {
        return menorDeTreinta;
    }

    /**
     * Define el valor de la propiedad menorDeTreinta.
     * 
     */
    public void setMenorDeTreinta(boolean value) {
        this.menorDeTreinta = value;
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
     * Obtiene el valor de la propiedad modeloMayorAVeinte.
     * 
     */
    public boolean isModeloMayorAVeinte() {
        return modeloMayorAVeinte;
    }

    /**
     * Define el valor de la propiedad modeloMayorAVeinte.
     * 
     */
    public void setModeloMayorAVeinte(boolean value) {
        this.modeloMayorAVeinte = value;
    }

    /**
     * Obtiene el valor de la propiedad numEconomico.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumEconomico() {
        return numEconomico;
    }

    /**
     * Define el valor de la propiedad numEconomico.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumEconomico(String value) {
        this.numEconomico = value;
    }

    /**
     * Obtiene el valor de la propiedad numMotor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumMotor() {
        return numMotor;
    }

    /**
     * Define el valor de la propiedad numMotor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumMotor(String value) {
        this.numMotor = value;
    }

    /**
     * Obtiene el valor de la propiedad numOcupantes.
     * 
     */
    public int getNumOcupantes() {
        return numOcupantes;
    }

    /**
     * Define el valor de la propiedad numOcupantes.
     * 
     */
    public void setNumOcupantes(int value) {
        this.numOcupantes = value;
    }

    /**
     * Obtiene el valor de la propiedad numPlacas.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumPlacas() {
        return numPlacas;
    }

    /**
     * Define el valor de la propiedad numPlacas.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumPlacas(String value) {
        this.numPlacas = value;
    }

    /**
     * Obtiene el valor de la propiedad numSerie.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumSerie() {
        return numSerie;
    }

    /**
     * Define el valor de la propiedad numSerie.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumSerie(String value) {
        this.numSerie = value;
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
     * Obtiene el valor de la propiedad submarca.
     * 
     * @return
     *     possible object is
     *     {@link Submarca }
     *     
     */
    public Submarca getSubmarca() {
        return submarca;
    }

    /**
     * Define el valor de la propiedad submarca.
     * 
     * @param value
     *     allowed object is
     *     {@link Submarca }
     *     
     */
    public void setSubmarca(Submarca value) {
        this.submarca = value;
    }

    /**
     * Obtiene el valor de la propiedad tipVehiCA.
     * 
     */
    public int getTipVehiCA() {
        return tipVehiCA;
    }

    /**
     * Define el valor de la propiedad tipVehiCA.
     * 
     */
    public void setTipVehiCA(int value) {
        this.tipVehiCA = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoUso.
     * 
     */
    public int getTipoUso() {
        return tipoUso;
    }

    /**
     * Define el valor de la propiedad tipoUso.
     * 
     */
    public void setTipoUso(int value) {
        this.tipoUso = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoVehiculo.
     * 
     * @return
     *     possible object is
     *     {@link TipoVehiculo }
     *     
     */
    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }

    /**
     * Define el valor de la propiedad tipoVehiculo.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoVehiculo }
     *     
     */
    public void setTipoVehiculo(TipoVehiculo value) {
        this.tipoVehiculo = value;
    }

    /**
     * Obtiene el valor de la propiedad valor.
     * 
     */
    public double getValor() {
        return valor;
    }

    /**
     * Define el valor de la propiedad valor.
     * 
     */
    public void setValor(double value) {
        this.valor = value;
    }

    /**
     * Obtiene el valor de la propiedad version.
     * 
     * @return
     *     possible object is
     *     {@link Version }
     *     
     */
    public Version getVersion() {
        return version;
    }

    /**
     * Define el valor de la propiedad version.
     * 
     * @param value
     *     allowed object is
     *     {@link Version }
     *     
     */
    public void setVersion(Version value) {
        this.version = value;
    }

}
