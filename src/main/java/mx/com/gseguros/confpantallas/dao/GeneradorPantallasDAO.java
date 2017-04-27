package mx.com.gseguros.confpantallas.dao;

import java.util.List;
import java.util.Map;

public interface GeneradorPantallasDAO {

	/**
	 * Obtiene los datos de una pantalla
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> obtienePantalla(Map<String, String> params) throws Exception;

	/**
	 * Inserta los datos de una pantalla
	 * @param cdpantalla C&oacute;digo de la pantalla
	 * @param datos datos de la pantalla
	 * @param componentes componente de la pantalla
	 * @return estatus de la operaci&oacute;n
	 * @throws Exception
	 */
	public String insertaPantalla(String cdpantalla, String datos, String componentes) throws Exception;
	
	
	

}