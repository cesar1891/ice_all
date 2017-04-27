package mx.com.gseguros.ws.nada.service;

import mx.com.gseguros.ws.model.WrapperResultadosWS;
import mx.com.gseguros.ws.nada.client.axis2.VehicleStub.VehicleTypes;
import mx.com.gseguros.ws.nada.client.axis2.VehicleStub.VehicleValue_Struc;



public interface NadaService {

	/**
	 * Metodo que obtiene los datos de un Auto en el servicio de NADA 
	 * @param vin VIN del automovil
	 * @param mileAge kilometraje
	 * @param vehicleType tipo de vehiculo dentro de los tipos del stub, VehicleTypes
	 * @param period fecha actual yyyyMM
	 * @param region region en NADA
	 * @return Wrapper con datos y el xml de entrada
	 * @throws Exception
	 */
	public WrapperResultadosWS ejecutaDatosAutoNadaWS(String vin, int mileAge, VehicleTypes vehicleType, int period, int region) throws Exception;
	
	/**
	 * Metodo que obtiene un nuevo Token o el mas reciente del sistema NADA
	 * @return
	 * @throws Exception
	 */
	public String obtieneLoginTokenNada() throws Exception;
	
	/**
	 * Metodo que obtiene los datos de un automovil en NADA, usando unicamente el vin y valores por defecto para usarlos en ICE 
	 * @param vin
	 * @return
	 */
	public VehicleValue_Struc obtieneDatosAutomovilNADA(String vin);
	
}
