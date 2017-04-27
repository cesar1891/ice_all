package mx.com.gseguros.portal.general.service;

import java.util.List;
import java.util.Map;


public interface MenuManager {
	
	
	public List<Map<String, String>> obtieneOpcionesLiga(Map<String, String> params) throws Exception;

	public List<Map<String, String>> obtieneMenusPorRol(Map<String, String> params) throws Exception;

	public List<Map<String, String>> obtieneOpcionesMenu(Map<String, String> params) throws Exception;

	public List<Map<String, String>> obtieneOpcionesSubMenu(Map<String, String> params) throws Exception;

	public String guardaOpcionLiga(Map<String, String> params) throws Exception;

	public String guardaMenu(Map<String, String> params) throws Exception;

	public String guardaOpcionMenu(Map<String, String> params) throws Exception;

	public String eliminaOpcionLiga(Map<String, String> params) throws Exception;

	public String eliminaMenu(Map<String, String> params) throws Exception;

	public String eliminaOpcionMenu(Map<String, String> params) throws Exception;


}
