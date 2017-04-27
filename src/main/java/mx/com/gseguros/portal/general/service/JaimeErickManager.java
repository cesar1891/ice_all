package mx.com.gseguros.portal.general.service;

import java.util.Map;

import mx.com.gseguros.portal.cotizacion.model.Item;

public interface JaimeErickManager {

	public Map<String,Item> recuperarElementosPantalla() throws Exception;

	public void guardarEnBase(String nombre, String edad, String fecha) throws Exception;
	
}