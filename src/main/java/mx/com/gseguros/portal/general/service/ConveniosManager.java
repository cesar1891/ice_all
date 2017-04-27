package mx.com.gseguros.portal.general.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.portal.cotizacion.model.Item;

public interface ConveniosManager {
	
	public Map<String,Item> recuperarElementosPantalla() throws Exception;

	public void guardarEnBase(String cdunieco, String cdramo, String estado, String cdtipsit, String nmpoliza, String diasgrac, String cdconven, String status, Date fecregis, String cdusureg, Date fecmodif, String cdusumod, String operacion) throws Exception;
	
	public List<Map<String, String>> buscarPoliza(String cdunieco, String cdramo, String cdtipsit, String estado, String nmpoliza, String cdcontra) throws Exception;
	
	public Map<String,Item> recuperarCancelacionesElementosPantalla() throws Exception;

	public void guardarCancelacionesEnBase(String cdunieco, String cdramo, String estado, String nmpoliza, String status, Date fecregis, String cdusureg, Date fecmodif, String cdusumod, String operacion) throws Exception;
	
	public List<Map<String, String>> buscarCancelacionesPoliza(String cdunieco, String cdramo, String cdtipsit, String estado, String nmpoliza) throws Exception;
}
