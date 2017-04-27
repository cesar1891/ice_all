package mx.com.gseguros.portal.general.dao;

import java.util.List;
import java.util.Map;


public interface MenuDAO {

		
	public List<Map<String, String>> obtieneOpcionesLiga(Map params) throws Exception;

	public List<Map<String, String>> obtieneMenusPorRol(Map params) throws Exception;

	public List<Map<String, String>> obtieneOpcionesMenu(Map params) throws Exception;

	public List<Map<String, String>> obtieneOpcionesSubMenu(Map params) throws Exception;

	public String guardaOpcionLiga(Map params) throws Exception;

	public String guardaMenu(Map params) throws Exception;

	public String guardaOpcionMenu(Map params) throws Exception;

	public String eliminaOpcionLiga(Map params) throws Exception;

	public String eliminaMenu(Map params) throws Exception;

	public String eliminaOpcionMenu(Map params) throws Exception;
	
}