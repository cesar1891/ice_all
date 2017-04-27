package mx.com.gseguros.ws.autosgs.tractocamiones.service;

public interface TractoCamionService {

	/**
	 * Metdodo que valida la poliza de un tractocamion
	 * @param numFolio
	 * @param sucursalAdmin
	 * @return
	 */
	public Object validarPolizaTractoCamion(String numeroPoliza, String rfcCliente);
}
