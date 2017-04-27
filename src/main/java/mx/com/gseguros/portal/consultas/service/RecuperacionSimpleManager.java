package mx.com.gseguros.portal.consultas.service;

import java.util.List;
import java.util.Map;

import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.portal.consultas.model.PagedMapList;
import mx.com.gseguros.portal.consultas.model.RecuperacionSimple;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSlist2VO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSmapVO;

public interface RecuperacionSimpleManager
{
	
	public ManagerRespuestaSmapVO recuperacionSimple(
			RecuperacionSimple procedimiento
			,Map<String,String>parametros
			,String cdsisrol
			,String cdusuari
			)throws Exception;
	
	public ManagerRespuestaSlist2VO recuperacionSimpleLista(
			RecuperacionSimple procedimiento
			,Map<String,String>parametros
			,String cdsisrol
			,String cdusuari
			)throws Exception;
	
	public Map<String,String> recuperarMapa(
			String cdusuari
			,String cdsisrol
			,RecuperacionSimple consulta
			,Map<String,String> params
			,UserVO usuario
			)throws Exception;
	
	public List<Map<String,String>> recuperarLista(
			String cdusuari
			,String cdsisrol
			,RecuperacionSimple consulta
			,Map<String,String> params
			,UserVO usuario
			)throws Exception;
	
	public PagedMapList recuperarListaPaginada(
            String cdusuari
            ,String cdsisrol
            ,RecuperacionSimple consulta
            ,Map<String,String> params
            ,String start
            ,String limit
            ,UserVO usuario
            )throws Exception;
	
}