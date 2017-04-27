package mx.com.gseguros.wizard.service;

import java.util.List;
import java.util.Map;

public interface TablasApoyoManager {

	public Map<String, Object> obtieneValoresTablaApoyo5claves(Map<String,String> params) throws Exception;

	public Map<String, Object> obtieneValoresTablaApoyo1clave(Map<String,String> params) throws Exception;
	
	public boolean guardaValoresTablaApoyo(Map<String, String> params, List<Map<String, String>> deleteList, List<Map<String, String>> saveList, List<Map<String, String>> updateList, boolean es1clave) throws Exception;
}