package mx.com.gseguros.portal.general.dao;

import java.util.List;
import java.util.Map;

import mx.com.gseguros.portal.general.model.ComponenteVO;

public interface PantallasDAO
{
	/**
	 * PKG_CONF_PANTALLAS.P_GET_TCONFCMP
	 */
	public List<ComponenteVO>obtenerComponentes(
			String cdtiptra
			,String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String cdsisrol
			,String pantalla
			,String seccion
			,String orden) throws Exception;
	/**
	 * PKG_CONF_PANTALLAS.P_GET_TCONFCMP
	 */
	public List<Map<String,String>>obtenerParametros(String cdtiptra
			,String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String cdsisrol
			,String pantalla
			,String seccion
			,String orden) throws Exception;
	/**
	 * PKG_CONF_PANTALLAS.P_MOV_TCONFCMP
	 */
	public void movParametros(Map<String,String> params) throws Exception;
	/**
	 * PKG_CONF_PANTALLAS.P_INSERTA_TCONFCMP
	 */
	public void insertarParametros (Map<String,String> params) throws Exception;
	/**
	 * PKG_CONF_PANTALLAS.P_OBT_ARBOL_TCONFCMP
	 */
	public List<Map<String,String>>obtenerArbol()throws Exception;
	
	/**
	 * Obtiene codigo ExtJS generado por el dise�ador visual de pantallas
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,String>> obtienePantalla(Map<String,String> params) throws Exception;
	
	/**
	 * 
	 * @param cdpantalla
	 * @param datos
	 * @param componentes
	 * @throws Exception
	 */
	public void insertaPantalla(String cdpantalla, String datos, String componentes) throws Exception;
}