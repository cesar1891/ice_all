package com.peritaje.siniestro.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.peritaje.siniestro.dao.PeritajeDAO;
import com.peritaje.siniestro.manager.PeritajeManager;


public class PeritajeManagerImpl implements PeritajeManager {
	
	private static Logger logger = Logger.getLogger(PeritajeManagerImpl.class);
	
	private transient PeritajeDAO peritajeDAO; 
	
	@Override
	public List<Map<String,String>> obtenerListaOrdenesInspeccion() throws Exception
	{
		logger.debug(""
				+ "\n###########################################"
				+ "\n###########################################"
				+ "\n###### obtenerListaOrdenesInspeccion ######"
				+ "\n######                               ######"
				);
		List<Map<String,String>> lista = peritajeDAO.obtenerListaOrdenesInspeccion(new HashMap<String,String>()); 
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		logger.debug("lista: "+lista);
		logger.debug(""
				+ "\n######                               ######"
				+ "\n###### obtenerListaOrdenesInspeccion ######"
				+ "\n###########################################"
				+ "\n###########################################"
				);
		return lista;
	}

	@Override
	public List<Map<String,String>> obtenerListaOrdenesAjuste() throws Exception
	{
		logger.debug(""
				+ "\n#######################################"
				+ "\n#######################################"
				+ "\n###### obtenerListaOrdenesAjuste ######"
				+ "\n######                           ######"
				);
		List<Map<String,String>> lista = peritajeDAO.obtenerListaOrdenesAjuste(new HashMap<String,String>()); 
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		logger.debug("lista: "+lista);
		logger.debug(""
				+ "\n######                           ######"
				+ "\n###### obtenerListaOrdenesAjuste ######"
				+ "\n#######################################"
				+ "\n#######################################"
				);
		return lista;
	}
	
	@Override
	public Map<String,String> obtenerDetalleOrdenInspeccion(String nmorden) throws Exception
	{
		logger.debug(""
				+ "\n###########################################"
				+ "\n###########################################"
				+ "\n###### obtenerDetalleOrdenInspeccion ######"
				+ "\n######                               ######"
				);
		logger.debug("nmorden: "+nmorden);
		Map<String,String> map = peritajeDAO.obtenerDetalleOrdenInspeccion(nmorden); 
		if(map==null)
		{
			map = new HashMap<String,String>();
		}
		logger.debug("map: "+map);
		logger.debug(""
				+ "\n######                               ######"
				+ "\n###### obtenerDetalleOrdenInspeccion ######"
				+ "\n###########################################"
				+ "\n###########################################"
				);
		return map;
	}
	
	@Override
	public Map<String,String> obtenerDetalleOrdenAjuste(String nmorden) throws Exception
	{
		logger.debug(""
				+ "\n#######################################"
				+ "\n#######################################"
				+ "\n###### obtenerDetalleOrdenAjuste ######"
				+ "\n######                           ######"
				);
		logger.debug("nmorden: "+nmorden);
		Map<String,String> map = peritajeDAO.obtenerDetalleOrdenAjuste(nmorden); 
		if(map==null)
		{
			map = new HashMap<String,String>();
		}
		logger.debug("map: "+map);
		logger.debug(""
				+ "\n######                           ######"
				+ "\n###### obtenerDetalleOrdenAjuste ######"
				+ "\n#######################################"
				+ "\n#######################################"
				);
		return map;
	}
	
	@Override
	public Map<String,String> obtenerDatosVehiculo(String nmorden) throws Exception
	{
		logger.debug(""
				+ "\n##################################"
				+ "\n##################################"
				+ "\n###### obtenerDatosVehiculo ######"
				+ "\n######                      ######"
				);
		logger.debug("nmorden: "+nmorden);
		Map<String,String> map = peritajeDAO.obtenerDatosVehiculo(nmorden); 
		if(map==null)
		{
			map = new HashMap<String,String>();
		}
		String poseeCabina = map.get("POSEECABINA");
		if(StringUtils.isNotBlank(poseeCabina)&&poseeCabina.equalsIgnoreCase("SI"))
		{
			map.put("POSEECABINA","1");
		}
		else
		{
			map.put("POSEECABINA","0");
		}
		logger.debug("map: "+map);
		logger.debug(""
				+ "\n######                      ######"
				+ "\n###### obtenerDatosVehiculo ######"
				+ "\n##################################"
				+ "\n##################################"
				);
		return map;
	}
	
	@Override
	public void guardarDatosVehiculo(
			String nmorden
			,String serialmot
			,String serialcarr
			,String kmactual
			,String cappuestos
			,String transmision
			,String tapiceria
			,String poseecabina
			,String destinado
			,String toneladas) throws Exception
	{
		logger.debug(""
				+ "\n##################################"
				+ "\n##################################"
				+ "\n###### guardarDatosVehiculo ######"
				+ "\n######                      ######"
				);
		peritajeDAO.guardarDatosVehiculo(nmorden
				,serialmot
				,serialcarr
				,kmactual
				,cappuestos
				,transmision
				,tapiceria
				,poseecabina
				,destinado
				,toneladas);
		logger.debug(""
				+ "\n######                      ######"
				+ "\n###### guardarDatosVehiculo ######"
				+ "\n##################################"
				+ "\n##################################"
				);
	}
	
	@Override
	public Map<String,String> obtenerDatosSeguridad(String nmorden) throws Exception
	{
		logger.debug(""
				+ "\n###################################"
				+ "\n###################################"
				+ "\n###### obtenerDatosSeguridad ######"
				+ "\n######                       ######"
				);
		logger.debug("nmorden: "+nmorden);
		Map<String,String> map = peritajeDAO.obtenerDatosSeguridad(nmorden); 
		if(map==null)
		{
			map = new HashMap<String,String>();
		}
		String campos[] = new String[]{"CORTACORR","ALARMA","BOVEDA","GRABAVIDRIO","TRANCAPEDAL","TRANCAPALAN","DISPSATELITAL"};
		for(String campo:campos)
		{
			String valor = map.get(campo);
			if(StringUtils.isNotBlank(valor)&&valor.equalsIgnoreCase("SI"))
			{
				map.put(campo,"1");
			}
			else
			{
				map.put(campo,"0");
			}
		}
		
		logger.debug("map: "+map);
		logger.debug(""
				+ "\n######                       ######"
				+ "\n###### obtenerDatosSeguridad ######"
				+ "\n###################################"
				+ "\n###################################"
				);
		return map;
	}
	
	@Override
	public void guardarDatosSeguridad(String nmorden, String alarma,
			String boveda, String cortacorr, String dispsatelital,
			String grabavidrio, String trancapalan, String trancapedal) throws Exception
	{
		logger.debug(""
				+ "\n###################################"
				+ "\n###################################"
				+ "\n###### guardarDatosSeguridad ######"
				+ "\n######                       ######"
				);
		peritajeDAO.guardarDatosSeguridad(nmorden, alarma, boveda, cortacorr,
				dispsatelital, grabavidrio, trancapalan, trancapedal);
		logger.debug(""
				+ "\n######                       ######"
				+ "\n###### guardarDatosSeguridad ######"
				+ "\n###################################"
				+ "\n###################################"
				);
	}
	
	@Override
	public Map<String,String> obtenerDetalleAccesorios(String nmorden) throws Exception
	{
		logger.debug(""
				+ "\n######################################"
				+ "\n######################################"
				+ "\n###### obtenerDetalleAccesorios ######"
				+ "\n######                          ######"
				);
		logger.debug("nmorden: "+nmorden);
		Map<String,String> map = peritajeDAO.obtenerDetalleAccesorios(nmorden); 
		if(map==null)
		{
			map = new HashMap<String,String>();
		}
		String campos[] = new String[]{"RADIOFIJO","RADIOREPRO","RADIOCD","LLANTAREPUES","AIREACONDIC","BLINDAJE","ESTACAOPLATA"};
		for(String campo:campos)
		{
			String valor = map.get(campo);
			if(StringUtils.isNotBlank(valor)&&valor.equalsIgnoreCase("SI"))
			{
				map.put(campo,"1");
			}
			else
			{
				map.put(campo,"0");
			}
		}
		
		logger.debug("map: "+map);
		logger.debug(""
				+ "\n######                          ######"
				+ "\n###### obtenerDetalleAccesorios ######"
				+ "\n######################################"
				+ "\n######################################"
				);
		return map;
	}
	
	@Override
	public void guardarDetalleAccesorios(String nmorden, String radiofijo,
			String radiorepro, String radiocd, String llantarepues,
			String aireacondic, String blindaje, String estacaoplata,
			String cava, String rines, String tazas) throws Exception
	{
		logger.debug(""
				+ "\n######################################"
				+ "\n######################################"
				+ "\n###### guardarDetalleAccesorios ######"
				+ "\n######                          ######"
				);
		peritajeDAO.guardarDetalleAccesorios(nmorden, radiofijo, radiorepro, radiocd,
				llantarepues, aireacondic, blindaje, estacaoplata,cava,rines,tazas);
		logger.debug(""
				+ "\n######                          ######"
				+ "\n###### guardarDetalleAccesorios ######"
				+ "\n######################################"
				+ "\n######################################"
				);
	}
	
	@Override
	public void guardarDetalleInspeccion(String nmorden, String otvalor01,
			String otvalor02, String otvalor03, String otvalor04,
			String otvalor05, String otvalor06, String otvalor07,
			String otvalor08, String otvalor09, String otvalor10,
			String otvalor11, String otvalor12, String otvalor13,
			String otvalor14, String otvalor15, String otvalor16,
			String otvalor17, String otvalor18, String otvalor19,
			String otvalor20, String otvalor21, String otvalor22,
			String otvalor23, String otvalor24, String otvalor25,
			String otvalor26, String otvalor27, String otvalor28,
			String otvalor29, String otvalor30, String observaciones) throws Exception
	{
		logger.debug(""
				+ "\n######################################"
				+ "\n######################################"
				+ "\n###### guardarDetalleInspeccion ######"
				+ "\n######                          ######"
				);
		peritajeDAO.guardarDetalleInspeccion(nmorden,
				otvalor01  , otvalor02 , otvalor03 , otvalor04 , otvalor05
				,otvalor06 , otvalor07 , otvalor08 , otvalor09 , otvalor10
				,otvalor11 , otvalor12 , otvalor13 , otvalor14 , otvalor15
				,otvalor16 , otvalor17 , otvalor18 , otvalor19 , otvalor20
				,otvalor21 , otvalor22 , otvalor23 , otvalor24 , otvalor25
				,otvalor26 , otvalor27 , otvalor28 , otvalor29 , otvalor30
				,observaciones
				);
		logger.debug(""
				+ "\n######                          ######"
				+ "\n###### guardarDetalleInspeccion ######"
				+ "\n######################################"
				+ "\n######################################"
				);
	}
	
	@Override
	public Map<String,String> obtenerDetalleAjuste(String nmorden) throws Exception
	{
		logger.debug(""
				+ "\n##################################"
				+ "\n##################################"
				+ "\n###### obtenerDetalleAjuste ######"
				+ "\n######                      ######"
				);
		logger.debug("nmorden: "+nmorden);
		Map<String,String> map = peritajeDAO.obtenerDetalleAjuste(nmorden); 
		if(map==null)
		{
			map = new HashMap<String,String>();
		}
		
		logger.debug("map: "+map);
		logger.debug(""
				+ "\n######                      ######"
				+ "\n###### obtenerDetalleAjuste ######"
				+ "\n##################################"
				+ "\n##################################"
				);
		return map;
	}
	
	@Override
	public List<Map<String,String>> obtenerListaRepuestos() throws Exception
	{
		logger.debug(""
				+ "\n###################################"
				+ "\n###################################"
				+ "\n###### obtenerListaRepuestos ######"
				+ "\n######                       ######"
				);
		List<Map<String,String>> lista = peritajeDAO.obtenerListaRepuestos(new HashMap<String,String>()); 
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		logger.debug("lista: "+lista);
		logger.debug(""
				+ "\n######                       ######"
				+ "\n###### obtenerListaRepuestos ######"
				+ "\n###################################"
				+ "\n###################################"
				);
		return lista;
	}
	
	@Override
	public List<Map<String,String>> obtenerListaManoObra() throws Exception
	{
		logger.debug(""
				+ "\n##################################"
				+ "\n##################################"
				+ "\n###### obtenerListaManoObra ######"
				+ "\n######                      ######"
				);
		List<Map<String,String>> lista = peritajeDAO.obtenerListaManoObra(new HashMap<String,String>()); 
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		logger.debug("lista: "+lista);
		logger.debug(""
				+ "\n######                      ######"
				+ "\n###### obtenerListaManoObra ######"
				+ "\n##################################"
				+ "\n##################################"
				);
		return lista;
	}
	
	@Override
	public void guardarRepuestosAjuste(String nmorden,List<Map<String, String>> repuestos) throws Exception
	{
		logger.debug(""
				+ "\n######################################"
				+ "\n######################################"
				+ "\n###### guardarDetalleInspeccion ######"
				+ "\n######                          ######"
				);
		peritajeDAO.guardarRepuestosAjuste(nmorden,repuestos);
		logger.debug(""
				+ "\n######                          ######"
				+ "\n###### guardarDetalleInspeccion ######"
				+ "\n######################################"
				+ "\n######################################"
				);
	}
	
	@Override
	public void guardarManoObraAjuste(String nmorden,List<Map<String, String>> repuestos) throws Exception
	{
		logger.debug(""
				+ "\n###################################"
				+ "\n###################################"
				+ "\n###### guardarManoObraAjuste ######"
				+ "\n######                       ######"
				);
		peritajeDAO.guardarManoObraAjuste(nmorden,repuestos);
		logger.debug(""
				+ "\n######                       ######"
				+ "\n###### guardarManoObraAjuste ######"
				+ "\n###################################"
				+ "\n###################################"
				);
	}
	
	public Map<String, String> obtenerDatosPresupuesto(String nmorden) throws Exception
	{
		logger.debug(""
				+ "\n#####################################"
				+ "\n#####################################"
				+ "\n###### obtenerDatosPresupuesto ######"
				+ "\n######                         ######"
				);
		logger.debug("nmorden: "+nmorden);
		Map<String,String> map = peritajeDAO.obtenerDatosPresupuesto(nmorden); 
		if(map==null)
		{
			map = new HashMap<String,String>();
		}
		
		logger.debug("map: "+map);
		logger.debug(""
				+ "\n######                         ######"
				+ "\n###### obtenerDatosPresupuesto ######"
				+ "\n#####################################"
				+ "\n#####################################"
				);
		return map;
	}
	
	//Getters and setters:
	public void setPeritajeDAO(PeritajeDAO peritajeDAO) {
		this.peritajeDAO = peritajeDAO;
	}
	
}