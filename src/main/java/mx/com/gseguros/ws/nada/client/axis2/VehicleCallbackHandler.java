
/**
 * VehicleCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.1  Built on : Oct 19, 2009 (10:59:00 EDT)
 */

    package mx.com.gseguros.ws.nada.client.axis2;

    /**
     *  VehicleCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class VehicleCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public VehicleCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public VehicleCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for getLowVehicleAndValueByVin method
            * override this method for handling normal response from getLowVehicleAndValueByVin operation
            */
           public void receiveResultgetLowVehicleAndValueByVin(
                    mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetLowVehicleAndValueByVinResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getLowVehicleAndValueByVin operation
           */
            public void receiveErrorgetLowVehicleAndValueByVin(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ping method
            * override this method for handling normal response from ping operation
            */
           public void receiveResultping(
                    mx.com.gseguros.ws.nada.client.axis2.VehicleStub.PingResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ping operation
           */
            public void receiveErrorping(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getMileageAdj method
            * override this method for handling normal response from getMileageAdj operation
            */
           public void receiveResultgetMileageAdj(
                    mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetMileageAdjResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getMileageAdj operation
           */
            public void receiveErrorgetMileageAdj(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getMsrpVehicleAndValueByVin method
            * override this method for handling normal response from getMsrpVehicleAndValueByVin operation
            */
           public void receiveResultgetMsrpVehicleAndValueByVin(
                    mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetMsrpVehicleAndValueByVinResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getMsrpVehicleAndValueByVin operation
           */
            public void receiveErrorgetMsrpVehicleAndValueByVin(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getVehicle method
            * override this method for handling normal response from getVehicle operation
            */
           public void receiveResultgetVehicle(
                    mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetVehicleResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getVehicle operation
           */
            public void receiveErrorgetVehicle(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getYears method
            * override this method for handling normal response from getYears operation
            */
           public void receiveResultgetYears(
                    mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetYearsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getYears operation
           */
            public void receiveErrorgetYears(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getHighVehicleAndValueByVin method
            * override this method for handling normal response from getHighVehicleAndValueByVin operation
            */
           public void receiveResultgetHighVehicleAndValueByVin(
                    mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetHighVehicleAndValueByVinResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getHighVehicleAndValueByVin operation
           */
            public void receiveErrorgetHighVehicleAndValueByVin(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getExclusiveAccessories method
            * override this method for handling normal response from getExclusiveAccessories operation
            */
           public void receiveResultgetExclusiveAccessories(
                    mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetExclusiveAccessoriesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getExclusiveAccessories operation
           */
            public void receiveErrorgetExclusiveAccessories(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getDefaultVehicleAndValueByVin method
            * override this method for handling normal response from getDefaultVehicleAndValueByVin operation
            */
           public void receiveResultgetDefaultVehicleAndValueByVin(
                    mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetDefaultVehicleAndValueByVinResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getDefaultVehicleAndValueByVin operation
           */
            public void receiveErrorgetDefaultVehicleAndValueByVin(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getRegions method
            * override this method for handling normal response from getRegions operation
            */
           public void receiveResultgetRegions(
                    mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetRegionsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getRegions operation
           */
            public void receiveErrorgetRegions(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getBodyUids method
            * override this method for handling normal response from getBodyUids operation
            */
           public void receiveResultgetBodyUids(
                    mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetBodyUidsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getBodyUids operation
           */
            public void receiveErrorgetBodyUids(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getSeries method
            * override this method for handling normal response from getSeries operation
            */
           public void receiveResultgetSeries(
                    mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetSeriesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getSeries operation
           */
            public void receiveErrorgetSeries(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getAccessories method
            * override this method for handling normal response from getAccessories operation
            */
           public void receiveResultgetAccessories(
                    mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetAccessoriesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getAccessories operation
           */
            public void receiveErrorgetAccessories(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getVehicleByVic method
            * override this method for handling normal response from getVehicleByVic operation
            */
           public void receiveResultgetVehicleByVic(
                    mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetVehicleByVicResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getVehicleByVic operation
           */
            public void receiveErrorgetVehicleByVic(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getVehicleAndValueByUid method
            * override this method for handling normal response from getVehicleAndValueByUid operation
            */
           public void receiveResultgetVehicleAndValueByUid(
                    mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetVehicleAndValueByUidResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getVehicleAndValueByUid operation
           */
            public void receiveErrorgetVehicleAndValueByUid(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getExperianAutoCheckReport method
            * override this method for handling normal response from getExperianAutoCheckReport operation
            */
           public void receiveResultgetExperianAutoCheckReport(
                    mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetExperianAutoCheckReportResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getExperianAutoCheckReport operation
           */
            public void receiveErrorgetExperianAutoCheckReport(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getInclusiveAccessories method
            * override this method for handling normal response from getInclusiveAccessories operation
            */
           public void receiveResultgetInclusiveAccessories(
                    mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetInclusiveAccessoriesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getInclusiveAccessories operation
           */
            public void receiveErrorgetInclusiveAccessories(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getVehicleValueByUid method
            * override this method for handling normal response from getVehicleValueByUid operation
            */
           public void receiveResultgetVehicleValueByUid(
                    mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetVehicleValueByUidResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getVehicleValueByUid operation
           */
            public void receiveErrorgetVehicleValueByUid(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getTotalAdjFloorValues method
            * override this method for handling normal response from getTotalAdjFloorValues operation
            */
           public void receiveResultgetTotalAdjFloorValues(
                    mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetTotalAdjFloorValuesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getTotalAdjFloorValues operation
           */
            public void receiveErrorgetTotalAdjFloorValues(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getAdjustedValuesByUid method
            * override this method for handling normal response from getAdjustedValuesByUid operation
            */
           public void receiveResultgetAdjustedValuesByUid(
                    mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetAdjustedValuesByUidResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getAdjustedValuesByUid operation
           */
            public void receiveErrorgetAdjustedValuesByUid(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getBaseVehicleValueByUid method
            * override this method for handling normal response from getBaseVehicleValueByUid operation
            */
           public void receiveResultgetBaseVehicleValueByUid(
                    mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetBaseVehicleValueByUidResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getBaseVehicleValueByUid operation
           */
            public void receiveErrorgetBaseVehicleValueByUid(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getAuctionValues method
            * override this method for handling normal response from getAuctionValues operation
            */
           public void receiveResultgetAuctionValues(
                    mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetAuctionValuesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getAuctionValues operation
           */
            public void receiveErrorgetAuctionValues(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getRegionByStateCode method
            * override this method for handling normal response from getRegionByStateCode operation
            */
           public void receiveResultgetRegionByStateCode(
                    mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetRegionByStateCodeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getRegionByStateCode operation
           */
            public void receiveErrorgetRegionByStateCode(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getVehicles method
            * override this method for handling normal response from getVehicles operation
            */
           public void receiveResultgetVehicles(
                    mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetVehiclesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getVehicles operation
           */
            public void receiveErrorgetVehicles(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for validateVin method
            * override this method for handling normal response from validateVin operation
            */
           public void receiveResultvalidateVin(
                    mx.com.gseguros.ws.nada.client.axis2.VehicleStub.ValidateVinResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from validateVin operation
           */
            public void receiveErrorvalidateVin(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getMakes method
            * override this method for handling normal response from getMakes operation
            */
           public void receiveResultgetMakes(
                    mx.com.gseguros.ws.nada.client.axis2.VehicleStub.GetMakesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getMakes operation
           */
            public void receiveErrorgetMakes(java.lang.Exception e) {
            }
                


    }
    