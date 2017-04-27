package mx.com.gseguros.portal.consultas.dao;


import java.util.List;
import java.util.Map;


public interface ConsultasPerfilMedicoDAO
{
	
	public List<Map<String,String>> consultaPerfilAsegurados(Map<String,String> params) throws Exception;
	
  	public List<Map<String,String>> consultaICDSAsegurado(Map<String,String> params) throws Exception;

}