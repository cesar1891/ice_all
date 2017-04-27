package mx.com.gseguros.portal.general.service;

import java.util.List;
import java.util.Map;

import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.general.model.ComponenteVO;

public interface PantallasManager
{
	/**
	 * PKG_CONF_PANTALLAS.P_GET_TCONFCMP
	 */
	public List<ComponenteVO> obtenerComponentes(
			 String cdtiptra
			,String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String cdsisrol
			,String pantalla
			,String seccion
			,String orden
			) throws Exception;
	
	/**
	 * PKG_CONF_PANTALLAS.P_GET_TCONFCMP
	 */
	public List<Map<String, String>> obtenerParametros(
			 String cdtiptra
			,String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String cdsisrol
			,String pantalla
			,String seccion
			,String orden
			) throws Exception;
	
	/**
	 * PKG_CONF_PANTALLAS.P_MOV_TCONFCMP
	 */
	public void movParametros(
			 String cdtiptra
			,String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String cdsisrol
			,String pantalla
			,String seccion
			,String orden
			,String accion
			,String idproceso
			) throws Exception;
	
	/**
	 * PKG_CONF_PANTALLAS.P_INSERTA_TCONFCMP
	 */
	public void                     insertarParametros         (Map<String,String> params) throws Exception;
	
	/**
	 * PKG_CONF_PANTALLAS.P_OBT_ARBOL_TCONFCMP
	 */
	public Item                     obtenerArbol               ()                          throws Exception;
	
	public Map<String,String> obtienePantalla(Map<String,String> params) throws Exception;
	
	public List<ComponenteVO> recuperarComboDocs(String proceso);
}