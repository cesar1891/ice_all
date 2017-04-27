
/**
 * ServicioGSServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.1  Built on : Oct 19, 2009 (10:59:00 EDT)
 */

    package mx.com.gseguros.ws.ice2sigs.client.axis2;

    /**
     *  ServicioGSServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class ServicioGSServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public ServicioGSServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public ServicioGSServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for agenteGS method
            * override this method for handling normal response from agenteGS operation
            */
           public void receiveResultagenteGS(
                    mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.AgenteGSResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from agenteGS operation
           */
            public void receiveErroragenteGS(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for reclamoGS method
            * override this method for handling normal response from reclamoGS operation
            */
           public void receiveResultreclamoGS(
                    mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ReclamoGSResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from reclamoGS operation
           */
            public void receiveErrorreclamoGS(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for direccionClienteGeneralGS method
            * override this method for handling normal response from direccionClienteGeneralGS operation
            */
           public void receiveResultdireccionClienteGeneralGS(
                    mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.DireccionClienteGeneralGSResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from direccionClienteGeneralGS operation
           */
            public void receiveErrordireccionClienteGeneralGS(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for clienteSaludGS method
            * override this method for handling normal response from clienteSaludGS operation
            */
           public void receiveResultclienteSaludGS(
                    mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteSaludGSResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from clienteSaludGS operation
           */
            public void receiveErrorclienteSaludGS(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for clienteGS method
            * override this method for handling normal response from clienteGS operation
            */
           public void receiveResultclienteGS(
                    mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteGSResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from clienteGS operation
           */
            public void receiveErrorclienteGS(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for reciboGS method
            * override this method for handling normal response from reciboGS operation
            */
           public void receiveResultreciboGS(
                    mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ReciboGSResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from reciboGS operation
           */
            public void receiveErrorreciboGS(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for agenteSaludGS method
            * override this method for handling normal response from agenteSaludGS operation
            */
           public void receiveResultagenteSaludGS(
                    mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.AgenteSaludGSResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from agenteSaludGS operation
           */
            public void receiveErroragenteSaludGS(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for clienteGeneralGS method
            * override this method for handling normal response from clienteGeneralGS operation
            */
           public void receiveResultclienteGeneralGS(
                    mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteGeneralGSResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from clienteGeneralGS operation
           */
            public void receiveErrorclienteGeneralGS(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for comisionReciboAgenteGS method
            * override this method for handling normal response from comisionReciboAgenteGS operation
            */
           public void receiveResultcomisionReciboAgenteGS(
                    mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ComisionReciboAgenteGSResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from comisionReciboAgenteGS operation
           */
            public void receiveErrorcomisionReciboAgenteGS(java.lang.Exception e) {
            }
                


    }
    