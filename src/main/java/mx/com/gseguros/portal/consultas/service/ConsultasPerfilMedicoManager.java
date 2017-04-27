package mx.com.gseguros.portal.consultas.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.portal.consultas.model.AseguradoVO;
import mx.com.gseguros.portal.consultas.model.PerfilAseguradoVO;
import mx.com.gseguros.portal.general.model.PolizaVO;

public interface ConsultasPerfilMedicoManager {
	
	public List<Map<String, String>> consultaPerfilAsegurados(Map<String,String> params) throws Exception;
	
	public PerfilAseguradoVO consultaICDSAsegurado(Map<String,String> params) throws Exception;
	
	public List<AseguradoVO> obtienePerfilAsegurados(PolizaVO poliza, long start, long limit) throws Exception;

}
