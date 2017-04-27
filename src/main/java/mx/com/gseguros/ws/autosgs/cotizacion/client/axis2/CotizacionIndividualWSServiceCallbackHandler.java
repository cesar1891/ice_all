
/**
 * CotizacionIndividualWSServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.1  Built on : Oct 19, 2009 (10:59:00 EDT)
 */

    package mx.com.gseguros.ws.autosgs.cotizacion.client.axis2;

    /**
     *  CotizacionIndividualWSServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class CotizacionIndividualWSServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public CotizacionIndividualWSServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public CotizacionIndividualWSServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for wsBuscarVehiculo method
            * override this method for handling normal response from wsBuscarVehiculo operation
            */
           public void receiveResultwsBuscarVehiculo(
                    mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.WsBuscarVehiculoResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from wsBuscarVehiculo operation
           */
            public void receiveErrorwsBuscarVehiculo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for wsDuplicarCotizacion method
            * override this method for handling normal response from wsDuplicarCotizacion operation
            */
           public void receiveResultwsDuplicarCotizacion(
                    mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.WsDuplicarCotizacionResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from wsDuplicarCotizacion operation
           */
            public void receiveErrorwsDuplicarCotizacion(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for wsRecuperaCotizacion method
            * override this method for handling normal response from wsRecuperaCotizacion operation
            */
           public void receiveResultwsRecuperaCotizacion(
                    mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.WsRecuperaCotizacionResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from wsRecuperaCotizacion operation
           */
            public void receiveErrorwsRecuperaCotizacion(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for wsGuardarCotizacion method
            * override this method for handling normal response from wsGuardarCotizacion operation
            */
           public void receiveResultwsGuardarCotizacion(
                    mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.WsGuardarCotizacionResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from wsGuardarCotizacion operation
           */
            public void receiveErrorwsGuardarCotizacion(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for wsBuscarCP method
            * override this method for handling normal response from wsBuscarCP operation
            */
           public void receiveResultwsBuscarCP(
                    mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.WsBuscarCPResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from wsBuscarCP operation
           */
            public void receiveErrorwsBuscarCP(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for wsObtenerTotalesFormaPago method
            * override this method for handling normal response from wsObtenerTotalesFormaPago operation
            */
           public void receiveResultwsObtenerTotalesFormaPago(
                    mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.WsObtenerTotalesFormaPagoResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from wsObtenerTotalesFormaPago operation
           */
            public void receiveErrorwsObtenerTotalesFormaPago(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for consultarConfiguracionPaquete method
            * override this method for handling normal response from consultarConfiguracionPaquete operation
            */
           public void receiveResultconsultarConfiguracionPaquete(
                    mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.ConsultarConfiguracionPaqueteResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from consultarConfiguracionPaquete operation
           */
            public void receiveErrorconsultarConfiguracionPaquete(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for wsEmitirCotizacion method
            * override this method for handling normal response from wsEmitirCotizacion operation
            */
           public void receiveResultwsEmitirCotizacion(
                    mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.WsEmitirCotizacionResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from wsEmitirCotizacion operation
           */
            public void receiveErrorwsEmitirCotizacion(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for consultarEstatusCotizaciones method
            * override this method for handling normal response from consultarEstatusCotizaciones operation
            */
           public void receiveResultconsultarEstatusCotizaciones(
                    mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.ConsultarEstatusCotizacionesResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from consultarEstatusCotizaciones operation
           */
            public void receiveErrorconsultarEstatusCotizaciones(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for wsCotizacionEnEmision method
            * override this method for handling normal response from wsCotizacionEnEmision operation
            */
           public void receiveResultwsCotizacionEnEmision(
                    mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.WsCotizacionEnEmisionResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from wsCotizacionEnEmision operation
           */
            public void receiveErrorwsCotizacionEnEmision(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for buscarCotizaciones method
            * override this method for handling normal response from buscarCotizaciones operation
            */
           public void receiveResultbuscarCotizaciones(
                    mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.BuscarCotizacionesResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from buscarCotizaciones operation
           */
            public void receiveErrorbuscarCotizaciones(java.lang.Exception e) {
            }
                


    }
    