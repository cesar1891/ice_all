package mx.com.gseguros.portal.general.dao;

import java.util.List;
import java.util.Map;


public interface WebServicesDAO {

	/**
	 * Consulta a la base de datos las peticiones de WS fallidas
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> obtienePeticionesFallidasWS(Map params) throws Exception;

	/**
	 * Metodo que obtiene a detalle el contenido de un registro de la Bitacora de WS
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> obtieneDetallePeticionWS(Map params) throws Exception;

	/**
	 * Metodo que elimina un registro de la Bitacora de WS
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String eliminaPeticionWS(Map params) throws Exception;

}