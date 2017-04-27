package mx.com.gseguros.portal.cotizacion.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlistSmapVO
{
	private Map<String,String>      smap;
	private List<Map<String,String>>slist;
	
	public SlistSmapVO()
	{
		smap  = new HashMap<String,String>();
		slist = new ArrayList<Map<String,String>>();
	}

	/*
	 * Getters y setters
	 */
	public Map<String, String> getSmap() {
		return smap;
	}

	public void setSmap(Map<String, String> smap) {
		this.smap = smap;
	}

	public List<Map<String, String>> getSlist() {
		return slist;
	}

	public void setSlist(List<Map<String, String>> slist) {
		this.slist = slist;
	}
}