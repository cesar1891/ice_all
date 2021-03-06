
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.8
 * Generated source version: 2.2
 * 
 */
@WebService(name = "CotizacionIndividualWS", targetNamespace = "http://com.gs.cotizador.ws.cotizacionindividual")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface CotizacionIndividualWS {


    /**
     * 
     * @param arg0
     * @return
     *     returns mx.com.gseguros.ws.autosgs.client.jaxws.GuardarCotizacionResponse
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "wsGuardarCotizacion", targetNamespace = "http://com.gs.cotizador.ws.cotizacionindividual", className = "mx.com.gseguros.ws.autosgs.client.jaxws.WsGuardarCotizacion")
    @ResponseWrapper(localName = "wsGuardarCotizacionResponse", targetNamespace = "http://com.gs.cotizador.ws.cotizacionindividual", className = "mx.com.gseguros.ws.autosgs.client.jaxws.WsGuardarCotizacionResponse")
    public GuardarCotizacionResponse wsGuardarCotizacion(
        @WebParam(name = "arg0", targetNamespace = "")
        CotizacionRequest arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns mx.com.gseguros.ws.autosgs.client.jaxws.Response
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "wsEmitirCotizacion", targetNamespace = "http://com.gs.cotizador.ws.cotizacionindividual", className = "mx.com.gseguros.ws.autosgs.client.jaxws.WsEmitirCotizacion")
    @ResponseWrapper(localName = "wsEmitirCotizacionResponse", targetNamespace = "http://com.gs.cotizador.ws.cotizacionindividual", className = "mx.com.gseguros.ws.autosgs.client.jaxws.WsEmitirCotizacionResponse")
    public Response wsEmitirCotizacion(
        @WebParam(name = "arg0", targetNamespace = "")
        EmisionPolizaRequest arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns mx.com.gseguros.ws.autosgs.client.jaxws.Response
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "wsCotizacionEnEmision", targetNamespace = "http://com.gs.cotizador.ws.cotizacionindividual", className = "mx.com.gseguros.ws.autosgs.client.jaxws.WsCotizacionEnEmision")
    @ResponseWrapper(localName = "wsCotizacionEnEmisionResponse", targetNamespace = "http://com.gs.cotizador.ws.cotizacionindividual", className = "mx.com.gseguros.ws.autosgs.client.jaxws.WsCotizacionEnEmisionResponse")
    public Response wsCotizacionEnEmision(
        @WebParam(name = "arg0", targetNamespace = "")
        ConsultaFolioCotizacionRequest arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns mx.com.gseguros.ws.autosgs.client.jaxws.GuardarCotizacionResponse
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "wsRecuperaCotizacion", targetNamespace = "http://com.gs.cotizador.ws.cotizacionindividual", className = "mx.com.gseguros.ws.autosgs.client.jaxws.WsRecuperaCotizacion")
    @ResponseWrapper(localName = "wsRecuperaCotizacionResponse", targetNamespace = "http://com.gs.cotizador.ws.cotizacionindividual", className = "mx.com.gseguros.ws.autosgs.client.jaxws.WsRecuperaCotizacionResponse")
    public GuardarCotizacionResponse wsRecuperaCotizacion(
        @WebParam(name = "arg0", targetNamespace = "")
        ConsultaFolioCotizacionRequest arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns mx.com.gseguros.ws.autosgs.client.jaxws.ObtenerTotalesFormaPagoResponse
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "wsObtenerTotalesFormaPago", targetNamespace = "http://com.gs.cotizador.ws.cotizacionindividual", className = "mx.com.gseguros.ws.autosgs.client.jaxws.WsObtenerTotalesFormaPago")
    @ResponseWrapper(localName = "wsObtenerTotalesFormaPagoResponse", targetNamespace = "http://com.gs.cotizador.ws.cotizacionindividual", className = "mx.com.gseguros.ws.autosgs.client.jaxws.WsObtenerTotalesFormaPagoResponse")
    public ObtenerTotalesFormaPagoResponse wsObtenerTotalesFormaPago(
        @WebParam(name = "arg0", targetNamespace = "")
        ObtenerTotalesFormaPagoRequest arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns mx.com.gseguros.ws.autosgs.client.jaxws.DuplicarCotizaciongsResponse
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "wsDuplicarCotizacion", targetNamespace = "http://com.gs.cotizador.ws.cotizacionindividual", className = "mx.com.gseguros.ws.autosgs.client.jaxws.WsDuplicarCotizacion")
    @ResponseWrapper(localName = "wsDuplicarCotizacionResponse", targetNamespace = "http://com.gs.cotizador.ws.cotizacionindividual", className = "mx.com.gseguros.ws.autosgs.client.jaxws.WsDuplicarCotizacionResponse")
    public DuplicarCotizaciongsResponse wsDuplicarCotizacion(
        @WebParam(name = "arg0", targetNamespace = "")
        DuplicarCotizaciongsRequest arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns mx.com.gseguros.ws.autosgs.client.jaxws.BuscarCPResponse
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "wsBuscarCP", targetNamespace = "http://com.gs.cotizador.ws.cotizacionindividual", className = "mx.com.gseguros.ws.autosgs.client.jaxws.WsBuscarCP")
    @ResponseWrapper(localName = "wsBuscarCPResponse", targetNamespace = "http://com.gs.cotizador.ws.cotizacionindividual", className = "mx.com.gseguros.ws.autosgs.client.jaxws.WsBuscarCPResponse")
    public BuscarCPResponse wsBuscarCP(
        @WebParam(name = "arg0", targetNamespace = "")
        BuscarCPRequest arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns mx.com.gseguros.ws.autosgs.client.jaxws.BuscarVehiculoResponse
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "wsBuscarVehiculo", targetNamespace = "http://com.gs.cotizador.ws.cotizacionindividual", className = "mx.com.gseguros.ws.autosgs.client.jaxws.WsBuscarVehiculo")
    @ResponseWrapper(localName = "wsBuscarVehiculoResponse", targetNamespace = "http://com.gs.cotizador.ws.cotizacionindividual", className = "mx.com.gseguros.ws.autosgs.client.jaxws.WsBuscarVehiculoResponse")
    public BuscarVehiculoResponse wsBuscarVehiculo(
        @WebParam(name = "arg0", targetNamespace = "")
        BuscarVehiculoRequest arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns mx.com.gseguros.ws.autosgs.client.jaxws.ConfiguracionPaqueteResponse
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "consultarConfiguracionPaquete", targetNamespace = "http://com.gs.cotizador.ws.cotizacionindividual", className = "mx.com.gseguros.ws.autosgs.client.jaxws.ConsultarConfiguracionPaquete")
    @ResponseWrapper(localName = "consultarConfiguracionPaqueteResponse", targetNamespace = "http://com.gs.cotizador.ws.cotizacionindividual", className = "mx.com.gseguros.ws.autosgs.client.jaxws.ConsultarConfiguracionPaqueteResponse")
    public ConfiguracionPaqueteResponse consultarConfiguracionPaquete(
        @WebParam(name = "arg0", targetNamespace = "")
        ConfiguracionPaqueteRequest arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns mx.com.gseguros.ws.autosgs.client.jaxws.ConsultarCotizacionesResponse
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "buscarCotizaciones", targetNamespace = "http://com.gs.cotizador.ws.cotizacionindividual", className = "mx.com.gseguros.ws.autosgs.client.jaxws.BuscarCotizaciones")
    @ResponseWrapper(localName = "buscarCotizacionesResponse", targetNamespace = "http://com.gs.cotizador.ws.cotizacionindividual", className = "mx.com.gseguros.ws.autosgs.client.jaxws.BuscarCotizacionesResponse")
    public ConsultarCotizacionesResponse buscarCotizaciones(
        @WebParam(name = "arg0", targetNamespace = "")
        ConsultarCotizacionesRequest arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns mx.com.gseguros.ws.autosgs.client.jaxws.EstatusCotizacionResponse
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "consultarEstatusCotizaciones", targetNamespace = "http://com.gs.cotizador.ws.cotizacionindividual", className = "mx.com.gseguros.ws.autosgs.client.jaxws.ConsultarEstatusCotizaciones")
    @ResponseWrapper(localName = "consultarEstatusCotizacionesResponse", targetNamespace = "http://com.gs.cotizador.ws.cotizacionindividual", className = "mx.com.gseguros.ws.autosgs.client.jaxws.ConsultarEstatusCotizacionesResponse")
    public EstatusCotizacionResponse consultarEstatusCotizaciones(
        @WebParam(name = "arg0", targetNamespace = "")
        EstatusCotizacionRequest arg0);

}
