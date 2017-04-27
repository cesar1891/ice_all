package mx.com.gseguros.portal.catalogos.dao;

public interface ClienteDAO {
	/**
	 * 
	 * @param sucursal
	 * @param ramo
	 * @param poliza
	 * @return
	 * @throws Exception
	 */
	String obtieneInformacionCliente(String sucursal, String ramo, String poliza) throws Exception;

}
