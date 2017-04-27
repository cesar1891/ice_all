package mx.com.gseguros.portal.general.service;

import java.util.List;
import java.util.Map;


public interface WebServicesManager {
	
	/**
	 * Metodo que obtiene la bitacora de WS Fallidos, filtrados por producto, sucursal y poliza
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> obtienePeticionesFallidasWS(Map<String, String> params) throws Exception;

	/**
	 * Metodo que Otiene el detalle de una peticion Fallida, el cual contiene el cdURL, metodo, Xml a enviar
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> obtieneDetallePeticionWS(Map<String, String> params) throws Exception;

	/**
	 * Metodo que recibe una lista de peticiones a eliminar
	 * @param listaEliminar
	 * @return
	 * @throws Exception
	 */
	public boolean eliminaPeticionWS(List<Map<String, String>> listaEliminar) throws Exception;

}
