
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the mx.com.gseguros.ws.autosgs.client.jaxws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _BuscarCotizacionesResponse_QNAME = new QName("http://com.gs.cotizador.ws.cotizacionindividual", "buscarCotizacionesResponse");
    private final static QName _WsBuscarCPResponse_QNAME = new QName("http://com.gs.cotizador.ws.cotizacionindividual", "wsBuscarCPResponse");
    private final static QName _WsCotizacionEnEmisionResponse_QNAME = new QName("http://com.gs.cotizador.ws.cotizacionindividual", "wsCotizacionEnEmisionResponse");
    private final static QName _ConsultarConfiguracionPaquete_QNAME = new QName("http://com.gs.cotizador.ws.cotizacionindividual", "consultarConfiguracionPaquete");
    private final static QName _WsEmitirCotizacionResponse_QNAME = new QName("http://com.gs.cotizador.ws.cotizacionindividual", "wsEmitirCotizacionResponse");
    private final static QName _WsCotizacionEnEmision_QNAME = new QName("http://com.gs.cotizador.ws.cotizacionindividual", "wsCotizacionEnEmision");
    private final static QName _WsBuscarVehiculoResponse_QNAME = new QName("http://com.gs.cotizador.ws.cotizacionindividual", "wsBuscarVehiculoResponse");
    private final static QName _WsBuscarVehiculo_QNAME = new QName("http://com.gs.cotizador.ws.cotizacionindividual", "wsBuscarVehiculo");
    private final static QName _ConsultarEstatusCotizaciones_QNAME = new QName("http://com.gs.cotizador.ws.cotizacionindividual", "consultarEstatusCotizaciones");
    private final static QName _WsGuardarCotizacionResponse_QNAME = new QName("http://com.gs.cotizador.ws.cotizacionindividual", "wsGuardarCotizacionResponse");
    private final static QName _WsObtenerTotalesFormaPagoResponse_QNAME = new QName("http://com.gs.cotizador.ws.cotizacionindividual", "wsObtenerTotalesFormaPagoResponse");
    private final static QName _BuscarCotizaciones_QNAME = new QName("http://com.gs.cotizador.ws.cotizacionindividual", "buscarCotizaciones");
    private final static QName _WsEmitirCotizacion_QNAME = new QName("http://com.gs.cotizador.ws.cotizacionindividual", "wsEmitirCotizacion");
    private final static QName _ConsultarEstatusCotizacionesResponse_QNAME = new QName("http://com.gs.cotizador.ws.cotizacionindividual", "consultarEstatusCotizacionesResponse");
    private final static QName _WsDuplicarCotizacionResponse_QNAME = new QName("http://com.gs.cotizador.ws.cotizacionindividual", "wsDuplicarCotizacionResponse");
    private final static QName _ConsultarConfiguracionPaqueteResponse_QNAME = new QName("http://com.gs.cotizador.ws.cotizacionindividual", "consultarConfiguracionPaqueteResponse");
    private final static QName _WsDuplicarCotizacion_QNAME = new QName("http://com.gs.cotizador.ws.cotizacionindividual", "wsDuplicarCotizacion");
    private final static QName _WsRecuperaCotizacion_QNAME = new QName("http://com.gs.cotizador.ws.cotizacionindividual", "wsRecuperaCotizacion");
    private final static QName _WsBuscarCP_QNAME = new QName("http://com.gs.cotizador.ws.cotizacionindividual", "wsBuscarCP");
    private final static QName _WsObtenerTotalesFormaPago_QNAME = new QName("http://com.gs.cotizador.ws.cotizacionindividual", "wsObtenerTotalesFormaPago");
    private final static QName _WsRecuperaCotizacionResponse_QNAME = new QName("http://com.gs.cotizador.ws.cotizacionindividual", "wsRecuperaCotizacionResponse");
    private final static QName _WsGuardarCotizacion_QNAME = new QName("http://com.gs.cotizador.ws.cotizacionindividual", "wsGuardarCotizacion");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: mx.com.gseguros.ws.autosgs.client.jaxws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Cotizacion }
     * 
     */
    public Cotizacion createCotizacion() {
        return new Cotizacion();
    }

    /**
     * Create an instance of {@link Cotizacion.FormasDePago }
     * 
     */
    public Cotizacion.FormasDePago createCotizacionFormasDePago() {
        return new Cotizacion.FormasDePago();
    }

    /**
     * Create an instance of {@link WsGuardarCotizacion }
     * 
     */
    public WsGuardarCotizacion createWsGuardarCotizacion() {
        return new WsGuardarCotizacion();
    }

    /**
     * Create an instance of {@link WsRecuperaCotizacionResponse }
     * 
     */
    public WsRecuperaCotizacionResponse createWsRecuperaCotizacionResponse() {
        return new WsRecuperaCotizacionResponse();
    }

    /**
     * Create an instance of {@link WsRecuperaCotizacion }
     * 
     */
    public WsRecuperaCotizacion createWsRecuperaCotizacion() {
        return new WsRecuperaCotizacion();
    }

    /**
     * Create an instance of {@link WsBuscarCP }
     * 
     */
    public WsBuscarCP createWsBuscarCP() {
        return new WsBuscarCP();
    }

    /**
     * Create an instance of {@link WsObtenerTotalesFormaPago }
     * 
     */
    public WsObtenerTotalesFormaPago createWsObtenerTotalesFormaPago() {
        return new WsObtenerTotalesFormaPago();
    }

    /**
     * Create an instance of {@link WsDuplicarCotizacionResponse }
     * 
     */
    public WsDuplicarCotizacionResponse createWsDuplicarCotizacionResponse() {
        return new WsDuplicarCotizacionResponse();
    }

    /**
     * Create an instance of {@link ConsultarConfiguracionPaqueteResponse }
     * 
     */
    public ConsultarConfiguracionPaqueteResponse createConsultarConfiguracionPaqueteResponse() {
        return new ConsultarConfiguracionPaqueteResponse();
    }

    /**
     * Create an instance of {@link WsDuplicarCotizacion }
     * 
     */
    public WsDuplicarCotizacion createWsDuplicarCotizacion() {
        return new WsDuplicarCotizacion();
    }

    /**
     * Create an instance of {@link ConsultarEstatusCotizacionesResponse }
     * 
     */
    public ConsultarEstatusCotizacionesResponse createConsultarEstatusCotizacionesResponse() {
        return new ConsultarEstatusCotizacionesResponse();
    }

    /**
     * Create an instance of {@link WsEmitirCotizacion }
     * 
     */
    public WsEmitirCotizacion createWsEmitirCotizacion() {
        return new WsEmitirCotizacion();
    }

    /**
     * Create an instance of {@link WsObtenerTotalesFormaPagoResponse }
     * 
     */
    public WsObtenerTotalesFormaPagoResponse createWsObtenerTotalesFormaPagoResponse() {
        return new WsObtenerTotalesFormaPagoResponse();
    }

    /**
     * Create an instance of {@link BuscarCotizaciones }
     * 
     */
    public BuscarCotizaciones createBuscarCotizaciones() {
        return new BuscarCotizaciones();
    }

    /**
     * Create an instance of {@link WsGuardarCotizacionResponse }
     * 
     */
    public WsGuardarCotizacionResponse createWsGuardarCotizacionResponse() {
        return new WsGuardarCotizacionResponse();
    }

    /**
     * Create an instance of {@link ConsultarEstatusCotizaciones }
     * 
     */
    public ConsultarEstatusCotizaciones createConsultarEstatusCotizaciones() {
        return new ConsultarEstatusCotizaciones();
    }

    /**
     * Create an instance of {@link WsBuscarVehiculo }
     * 
     */
    public WsBuscarVehiculo createWsBuscarVehiculo() {
        return new WsBuscarVehiculo();
    }

    /**
     * Create an instance of {@link WsBuscarVehiculoResponse }
     * 
     */
    public WsBuscarVehiculoResponse createWsBuscarVehiculoResponse() {
        return new WsBuscarVehiculoResponse();
    }

    /**
     * Create an instance of {@link WsEmitirCotizacionResponse }
     * 
     */
    public WsEmitirCotizacionResponse createWsEmitirCotizacionResponse() {
        return new WsEmitirCotizacionResponse();
    }

    /**
     * Create an instance of {@link WsCotizacionEnEmision }
     * 
     */
    public WsCotizacionEnEmision createWsCotizacionEnEmision() {
        return new WsCotizacionEnEmision();
    }

    /**
     * Create an instance of {@link ConsultarConfiguracionPaquete }
     * 
     */
    public ConsultarConfiguracionPaquete createConsultarConfiguracionPaquete() {
        return new ConsultarConfiguracionPaquete();
    }

    /**
     * Create an instance of {@link WsBuscarCPResponse }
     * 
     */
    public WsBuscarCPResponse createWsBuscarCPResponse() {
        return new WsBuscarCPResponse();
    }

    /**
     * Create an instance of {@link WsCotizacionEnEmisionResponse }
     * 
     */
    public WsCotizacionEnEmisionResponse createWsCotizacionEnEmisionResponse() {
        return new WsCotizacionEnEmisionResponse();
    }

    /**
     * Create an instance of {@link BuscarCotizacionesResponse }
     * 
     */
    public BuscarCotizacionesResponse createBuscarCotizacionesResponse() {
        return new BuscarCotizacionesResponse();
    }

    /**
     * Create an instance of {@link ObtenerTotalesFormaPagoResponse }
     * 
     */
    public ObtenerTotalesFormaPagoResponse createObtenerTotalesFormaPagoResponse() {
        return new ObtenerTotalesFormaPagoResponse();
    }

    /**
     * Create an instance of {@link Paquete }
     * 
     */
    public Paquete createPaquete() {
        return new Paquete();
    }

    /**
     * Create an instance of {@link ArrayList }
     * 
     */
    public ArrayList createArrayList() {
        return new ArrayList();
    }

    /**
     * Create an instance of {@link DuplicarCotizaciongsRequest }
     * 
     */
    public DuplicarCotizaciongsRequest createDuplicarCotizaciongsRequest() {
        return new DuplicarCotizaciongsRequest();
    }

    /**
     * Create an instance of {@link TotalFormaPago }
     * 
     */
    public TotalFormaPago createTotalFormaPago() {
        return new TotalFormaPago();
    }

    /**
     * Create an instance of {@link MapElementsArray }
     * 
     */
    public MapElementsArray createMapElementsArray() {
        return new MapElementsArray();
    }

    /**
     * Create an instance of {@link Request }
     * 
     */
    public Request createRequest() {
        return new Request();
    }

    /**
     * Create an instance of {@link ConfiguracionPaqueteRequest }
     * 
     */
    public ConfiguracionPaqueteRequest createConfiguracionPaqueteRequest() {
        return new ConfiguracionPaqueteRequest();
    }

    /**
     * Create an instance of {@link Vehiculo }
     * 
     */
    public Vehiculo createVehiculo() {
        return new Vehiculo();
    }

    /**
     * Create an instance of {@link FormaPagoDTO }
     * 
     */
    public FormaPagoDTO createFormaPagoDTO() {
        return new FormaPagoDTO();
    }

    /**
     * Create an instance of {@link BuscarVehiculoResponse }
     * 
     */
    public BuscarVehiculoResponse createBuscarVehiculoResponse() {
        return new BuscarVehiculoResponse();
    }

    /**
     * Create an instance of {@link Grupo }
     * 
     */
    public Grupo createGrupo() {
        return new Grupo();
    }

    /**
     * Create an instance of {@link BuscarCPRequest }
     * 
     */
    public BuscarCPRequest createBuscarCPRequest() {
        return new BuscarCPRequest();
    }

    /**
     * Create an instance of {@link DuplicarCotizaciongsResponse }
     * 
     */
    public DuplicarCotizaciongsResponse createDuplicarCotizaciongsResponse() {
        return new DuplicarCotizaciongsResponse();
    }

    /**
     * Create an instance of {@link MapElements }
     * 
     */
    public MapElements createMapElements() {
        return new MapElements();
    }

    /**
     * Create an instance of {@link Inciso }
     * 
     */
    public Inciso createInciso() {
        return new Inciso();
    }

    /**
     * Create an instance of {@link EmisionPolizaRequest }
     * 
     */
    public EmisionPolizaRequest createEmisionPolizaRequest() {
        return new EmisionPolizaRequest();
    }

    /**
     * Create an instance of {@link TotalFormaPagoDTO }
     * 
     */
    public TotalFormaPagoDTO createTotalFormaPagoDTO() {
        return new TotalFormaPagoDTO();
    }

    /**
     * Create an instance of {@link ObtenerTotalesFormaPagoRequest }
     * 
     */
    public ObtenerTotalesFormaPagoRequest createObtenerTotalesFormaPagoRequest() {
        return new ObtenerTotalesFormaPagoRequest();
    }

    /**
     * Create an instance of {@link GuardarCotizacionResponse }
     * 
     */
    public GuardarCotizacionResponse createGuardarCotizacionResponse() {
        return new GuardarCotizacionResponse();
    }

    /**
     * Create an instance of {@link EstatusCotizacionResponse }
     * 
     */
    public EstatusCotizacionResponse createEstatusCotizacionResponse() {
        return new EstatusCotizacionResponse();
    }

    /**
     * Create an instance of {@link EstatusCotizacion }
     * 
     */
    public EstatusCotizacion createEstatusCotizacion() {
        return new EstatusCotizacion();
    }

    /**
     * Create an instance of {@link Cobertura }
     * 
     */
    public Cobertura createCobertura() {
        return new Cobertura();
    }

    /**
     * Create an instance of {@link Monto }
     * 
     */
    public Monto createMonto() {
        return new Monto();
    }

    /**
     * Create an instance of {@link CodigoPostal }
     * 
     */
    public CodigoPostal createCodigoPostal() {
        return new CodigoPostal();
    }

    /**
     * Create an instance of {@link ConsultarCotizacionesResponse }
     * 
     */
    public ConsultarCotizacionesResponse createConsultarCotizacionesResponse() {
        return new ConsultarCotizacionesResponse();
    }

    /**
     * Create an instance of {@link EstatusCotizacionRequest }
     * 
     */
    public EstatusCotizacionRequest createEstatusCotizacionRequest() {
        return new EstatusCotizacionRequest();
    }

    /**
     * Create an instance of {@link Agente }
     * 
     */
    public Agente createAgente() {
        return new Agente();
    }

    /**
     * Create an instance of {@link CotizacionDTO }
     * 
     */
    public CotizacionDTO createCotizacionDTO() {
        return new CotizacionDTO();
    }

    /**
     * Create an instance of {@link Response }
     * 
     */
    public Response createResponse() {
        return new Response();
    }

    /**
     * Create an instance of {@link ConfiguracionPaqueteResponse }
     * 
     */
    public ConfiguracionPaqueteResponse createConfiguracionPaqueteResponse() {
        return new ConfiguracionPaqueteResponse();
    }

    /**
     * Create an instance of {@link BuscarVehiculoRequest }
     * 
     */
    public BuscarVehiculoRequest createBuscarVehiculoRequest() {
        return new BuscarVehiculoRequest();
    }

    /**
     * Create an instance of {@link ConsultaFolioCotizacionRequest }
     * 
     */
    public ConsultaFolioCotizacionRequest createConsultaFolioCotizacionRequest() {
        return new ConsultaFolioCotizacionRequest();
    }

    /**
     * Create an instance of {@link CotizacionRequest }
     * 
     */
    public CotizacionRequest createCotizacionRequest() {
        return new CotizacionRequest();
    }

    /**
     * Create an instance of {@link Complemento }
     * 
     */
    public Complemento createComplemento() {
        return new Complemento();
    }

    /**
     * Create an instance of {@link FormaPago }
     * 
     */
    public FormaPago createFormaPago() {
        return new FormaPago();
    }

    /**
     * Create an instance of {@link ConsultarCotizacionesRequest }
     * 
     */
    public ConsultarCotizacionesRequest createConsultarCotizacionesRequest() {
        return new ConsultarCotizacionesRequest();
    }

    /**
     * Create an instance of {@link ConfiguracionPaquete }
     * 
     */
    public ConfiguracionPaquete createConfiguracionPaquete() {
        return new ConfiguracionPaquete();
    }

    /**
     * Create an instance of {@link BuscarCPResponse }
     * 
     */
    public BuscarCPResponse createBuscarCPResponse() {
        return new BuscarCPResponse();
    }

    /**
     * Create an instance of {@link Submarca }
     * 
     */
    public Submarca createSubmarca() {
        return new Submarca();
    }

    /**
     * Create an instance of {@link Marca }
     * 
     */
    public Marca createMarca() {
        return new Marca();
    }

    /**
     * Create an instance of {@link Version }
     * 
     */
    public Version createVersion() {
        return new Version();
    }

    /**
     * Create an instance of {@link SDTClientesSDTClientesItem }
     * 
     */
    public SDTClientesSDTClientesItem createSDTClientesSDTClientesItem() {
        return new SDTClientesSDTClientesItem();
    }

    /**
     * Create an instance of {@link SDTPoliza }
     * 
     */
    public SDTPoliza createSDTPoliza() {
        return new SDTPoliza();
    }

    /**
     * Create an instance of {@link Cotizacion.FormasDePago.Entry }
     * 
     */
    public Cotizacion.FormasDePago.Entry createCotizacionFormasDePagoEntry() {
        return new Cotizacion.FormasDePago.Entry();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BuscarCotizacionesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.gs.cotizador.ws.cotizacionindividual", name = "buscarCotizacionesResponse")
    public JAXBElement<BuscarCotizacionesResponse> createBuscarCotizacionesResponse(BuscarCotizacionesResponse value) {
        return new JAXBElement<BuscarCotizacionesResponse>(_BuscarCotizacionesResponse_QNAME, BuscarCotizacionesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WsBuscarCPResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.gs.cotizador.ws.cotizacionindividual", name = "wsBuscarCPResponse")
    public JAXBElement<WsBuscarCPResponse> createWsBuscarCPResponse(WsBuscarCPResponse value) {
        return new JAXBElement<WsBuscarCPResponse>(_WsBuscarCPResponse_QNAME, WsBuscarCPResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WsCotizacionEnEmisionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.gs.cotizador.ws.cotizacionindividual", name = "wsCotizacionEnEmisionResponse")
    public JAXBElement<WsCotizacionEnEmisionResponse> createWsCotizacionEnEmisionResponse(WsCotizacionEnEmisionResponse value) {
        return new JAXBElement<WsCotizacionEnEmisionResponse>(_WsCotizacionEnEmisionResponse_QNAME, WsCotizacionEnEmisionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarConfiguracionPaquete }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.gs.cotizador.ws.cotizacionindividual", name = "consultarConfiguracionPaquete")
    public JAXBElement<ConsultarConfiguracionPaquete> createConsultarConfiguracionPaquete(ConsultarConfiguracionPaquete value) {
        return new JAXBElement<ConsultarConfiguracionPaquete>(_ConsultarConfiguracionPaquete_QNAME, ConsultarConfiguracionPaquete.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WsEmitirCotizacionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.gs.cotizador.ws.cotizacionindividual", name = "wsEmitirCotizacionResponse")
    public JAXBElement<WsEmitirCotizacionResponse> createWsEmitirCotizacionResponse(WsEmitirCotizacionResponse value) {
        return new JAXBElement<WsEmitirCotizacionResponse>(_WsEmitirCotizacionResponse_QNAME, WsEmitirCotizacionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WsCotizacionEnEmision }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.gs.cotizador.ws.cotizacionindividual", name = "wsCotizacionEnEmision")
    public JAXBElement<WsCotizacionEnEmision> createWsCotizacionEnEmision(WsCotizacionEnEmision value) {
        return new JAXBElement<WsCotizacionEnEmision>(_WsCotizacionEnEmision_QNAME, WsCotizacionEnEmision.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WsBuscarVehiculoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.gs.cotizador.ws.cotizacionindividual", name = "wsBuscarVehiculoResponse")
    public JAXBElement<WsBuscarVehiculoResponse> createWsBuscarVehiculoResponse(WsBuscarVehiculoResponse value) {
        return new JAXBElement<WsBuscarVehiculoResponse>(_WsBuscarVehiculoResponse_QNAME, WsBuscarVehiculoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WsBuscarVehiculo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.gs.cotizador.ws.cotizacionindividual", name = "wsBuscarVehiculo")
    public JAXBElement<WsBuscarVehiculo> createWsBuscarVehiculo(WsBuscarVehiculo value) {
        return new JAXBElement<WsBuscarVehiculo>(_WsBuscarVehiculo_QNAME, WsBuscarVehiculo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarEstatusCotizaciones }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.gs.cotizador.ws.cotizacionindividual", name = "consultarEstatusCotizaciones")
    public JAXBElement<ConsultarEstatusCotizaciones> createConsultarEstatusCotizaciones(ConsultarEstatusCotizaciones value) {
        return new JAXBElement<ConsultarEstatusCotizaciones>(_ConsultarEstatusCotizaciones_QNAME, ConsultarEstatusCotizaciones.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WsGuardarCotizacionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.gs.cotizador.ws.cotizacionindividual", name = "wsGuardarCotizacionResponse")
    public JAXBElement<WsGuardarCotizacionResponse> createWsGuardarCotizacionResponse(WsGuardarCotizacionResponse value) {
        return new JAXBElement<WsGuardarCotizacionResponse>(_WsGuardarCotizacionResponse_QNAME, WsGuardarCotizacionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WsObtenerTotalesFormaPagoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.gs.cotizador.ws.cotizacionindividual", name = "wsObtenerTotalesFormaPagoResponse")
    public JAXBElement<WsObtenerTotalesFormaPagoResponse> createWsObtenerTotalesFormaPagoResponse(WsObtenerTotalesFormaPagoResponse value) {
        return new JAXBElement<WsObtenerTotalesFormaPagoResponse>(_WsObtenerTotalesFormaPagoResponse_QNAME, WsObtenerTotalesFormaPagoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BuscarCotizaciones }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.gs.cotizador.ws.cotizacionindividual", name = "buscarCotizaciones")
    public JAXBElement<BuscarCotizaciones> createBuscarCotizaciones(BuscarCotizaciones value) {
        return new JAXBElement<BuscarCotizaciones>(_BuscarCotizaciones_QNAME, BuscarCotizaciones.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WsEmitirCotizacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.gs.cotizador.ws.cotizacionindividual", name = "wsEmitirCotizacion")
    public JAXBElement<WsEmitirCotizacion> createWsEmitirCotizacion(WsEmitirCotizacion value) {
        return new JAXBElement<WsEmitirCotizacion>(_WsEmitirCotizacion_QNAME, WsEmitirCotizacion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarEstatusCotizacionesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.gs.cotizador.ws.cotizacionindividual", name = "consultarEstatusCotizacionesResponse")
    public JAXBElement<ConsultarEstatusCotizacionesResponse> createConsultarEstatusCotizacionesResponse(ConsultarEstatusCotizacionesResponse value) {
        return new JAXBElement<ConsultarEstatusCotizacionesResponse>(_ConsultarEstatusCotizacionesResponse_QNAME, ConsultarEstatusCotizacionesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WsDuplicarCotizacionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.gs.cotizador.ws.cotizacionindividual", name = "wsDuplicarCotizacionResponse")
    public JAXBElement<WsDuplicarCotizacionResponse> createWsDuplicarCotizacionResponse(WsDuplicarCotizacionResponse value) {
        return new JAXBElement<WsDuplicarCotizacionResponse>(_WsDuplicarCotizacionResponse_QNAME, WsDuplicarCotizacionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarConfiguracionPaqueteResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.gs.cotizador.ws.cotizacionindividual", name = "consultarConfiguracionPaqueteResponse")
    public JAXBElement<ConsultarConfiguracionPaqueteResponse> createConsultarConfiguracionPaqueteResponse(ConsultarConfiguracionPaqueteResponse value) {
        return new JAXBElement<ConsultarConfiguracionPaqueteResponse>(_ConsultarConfiguracionPaqueteResponse_QNAME, ConsultarConfiguracionPaqueteResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WsDuplicarCotizacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.gs.cotizador.ws.cotizacionindividual", name = "wsDuplicarCotizacion")
    public JAXBElement<WsDuplicarCotizacion> createWsDuplicarCotizacion(WsDuplicarCotizacion value) {
        return new JAXBElement<WsDuplicarCotizacion>(_WsDuplicarCotizacion_QNAME, WsDuplicarCotizacion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WsRecuperaCotizacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.gs.cotizador.ws.cotizacionindividual", name = "wsRecuperaCotizacion")
    public JAXBElement<WsRecuperaCotizacion> createWsRecuperaCotizacion(WsRecuperaCotizacion value) {
        return new JAXBElement<WsRecuperaCotizacion>(_WsRecuperaCotizacion_QNAME, WsRecuperaCotizacion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WsBuscarCP }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.gs.cotizador.ws.cotizacionindividual", name = "wsBuscarCP")
    public JAXBElement<WsBuscarCP> createWsBuscarCP(WsBuscarCP value) {
        return new JAXBElement<WsBuscarCP>(_WsBuscarCP_QNAME, WsBuscarCP.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WsObtenerTotalesFormaPago }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.gs.cotizador.ws.cotizacionindividual", name = "wsObtenerTotalesFormaPago")
    public JAXBElement<WsObtenerTotalesFormaPago> createWsObtenerTotalesFormaPago(WsObtenerTotalesFormaPago value) {
        return new JAXBElement<WsObtenerTotalesFormaPago>(_WsObtenerTotalesFormaPago_QNAME, WsObtenerTotalesFormaPago.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WsRecuperaCotizacionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.gs.cotizador.ws.cotizacionindividual", name = "wsRecuperaCotizacionResponse")
    public JAXBElement<WsRecuperaCotizacionResponse> createWsRecuperaCotizacionResponse(WsRecuperaCotizacionResponse value) {
        return new JAXBElement<WsRecuperaCotizacionResponse>(_WsRecuperaCotizacionResponse_QNAME, WsRecuperaCotizacionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WsGuardarCotizacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.gs.cotizador.ws.cotizacionindividual", name = "wsGuardarCotizacion")
    public JAXBElement<WsGuardarCotizacion> createWsGuardarCotizacion(WsGuardarCotizacion value) {
        return new JAXBElement<WsGuardarCotizacion>(_WsGuardarCotizacion_QNAME, WsGuardarCotizacion.class, null, value);
    }

}
