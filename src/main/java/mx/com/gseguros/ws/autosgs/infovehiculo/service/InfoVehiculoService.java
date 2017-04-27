package mx.com.gseguros.ws.autosgs.infovehiculo.service;

import mx.com.gseguros.ws.autosgs.infovehiculo.client.axis2.VehiculoWSServiceStub.ResponseValor;

public interface InfoVehiculoService {

	/**
	 * Metdodo que obtiene la informacion del valor de un vehiculo
	 * @param claveGS
	 * @param modelo
	 * @return
	 */
	public ResponseValor obtieneDatosVehiculoGS(int claveGS, int modelo);
}
