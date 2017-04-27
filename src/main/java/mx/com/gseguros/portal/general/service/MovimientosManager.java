package mx.com.gseguros.portal.general.service;

import java.util.List;
import java.util.Map;

import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.portal.general.model.Movimiento;

public interface MovimientosManager
{
	public void ejecutar(
			UserVO usuario
			,Movimiento movimiento
			,Map<String,String> params
			,List<Map<String,String>> list
			)throws Exception;
	
	public Map<String,String> ejecutarRecuperandoMapa(
			UserVO usuario
			,Movimiento movimiento
			,Map<String,String> params
			,List<Map<String,String>> list
			)throws Exception;
	
	public Map<String,String> ejecutarRecuperandoMapaAltaFamilia(
			UserVO usuario
			,Movimiento movimiento
			,Map<String,String> params
			,List<Map<String,String>> list
			)throws Exception;
	
	public List<Map<String,String>> ejecutarRecuperandoLista(
			UserVO usuario
			,Movimiento movimiento
			,Map<String,String> params
			,List<Map<String,String>> list
			)throws Exception;
	
	public Map<String,Object> ejecutarRecuperandoMapaLista(
			UserVO usuario
			,Movimiento movimiento
			,Map<String,String> params
			,List<Map<String,String>> list
			)throws Exception;
}