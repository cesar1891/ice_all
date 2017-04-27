package mx.com.gseguros.wizard.service.impl;

import java.util.List;
import java.util.Map;

import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.wizard.dao.TablasApoyoDAO;
import mx.com.gseguros.wizard.service.TablasApoyoManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TablasApoyoManagerImpl implements TablasApoyoManager {
	
	@Autowired
	private TablasApoyoDAO tablasApoyoDAO;

	@Override
	public Map<String, Object> obtieneValoresTablaApoyo5claves(Map<String,String> params) throws Exception
	{
		return tablasApoyoDAO.obtieneValoresTablaApoyo5claves(params);
	}

	@Override
	public Map<String, Object> obtieneValoresTablaApoyo1clave(Map<String,String> params) throws Exception
	{
		return tablasApoyoDAO.obtieneValoresTablaApoyo1clave(params);
	}
	
	@Override
	public boolean guardaValoresTablaApoyo(Map<String, String> params, List<Map<String, String>> deleteList, List<Map<String, String>> saveList, List<Map<String, String>> updateList, boolean es1clave) throws Exception
	{
		
		params.put("PV_SWCOMMIT_I", Constantes.SI);
		
		/*
		 * Para Hacer primero los deletes de registros eliminados.
		 */
		
		params.put("PV_ACCION_I", Constantes.DELETE_MODE);
		
		for(Map<String, String> valoreLlaveAtri : deleteList){
				params.put("PV_OTCLAVE1_I", valoreLlaveAtri.get("OTCLAVE1"));
				params.put("PV_OTCLAVE2_I", valoreLlaveAtri.get("OTCLAVE2"));
				params.put("PV_OTCLAVE3_I", valoreLlaveAtri.get("OTCLAVE3"));
				params.put("PV_OTCLAVE4_I", valoreLlaveAtri.get("OTCLAVE4"));
				params.put("PV_OTCLAVE5_I", valoreLlaveAtri.get("OTCLAVE5"));
				
				params.put("PV_FEDESDE_I", valoreLlaveAtri.get("FEDESDE"));
				params.put("PV_FEHASTA_I", valoreLlaveAtri.get("FEHASTA"));
				
				params.put("PV_OTVALOR01_I", valoreLlaveAtri.get("OTVALOR01"));
				params.put("PV_OTVALOR02_I", valoreLlaveAtri.get("OTVALOR02"));
				params.put("PV_OTVALOR03_I", valoreLlaveAtri.get("OTVALOR03"));
				params.put("PV_OTVALOR04_I", valoreLlaveAtri.get("OTVALOR04"));
				params.put("PV_OTVALOR05_I", valoreLlaveAtri.get("OTVALOR05"));
				params.put("PV_OTVALOR06_I", valoreLlaveAtri.get("OTVALOR06"));
				params.put("PV_OTVALOR07_I", valoreLlaveAtri.get("OTVALOR07"));
				params.put("PV_OTVALOR08_I", valoreLlaveAtri.get("OTVALOR08"));
				params.put("PV_OTVALOR09_I", valoreLlaveAtri.get("OTVALOR09"));
				params.put("PV_OTVALOR10_I", valoreLlaveAtri.get("OTVALOR10"));
				params.put("PV_OTVALOR11_I", valoreLlaveAtri.get("OTVALOR11"));
				params.put("PV_OTVALOR12_I", valoreLlaveAtri.get("OTVALOR12"));
				params.put("PV_OTVALOR13_I", valoreLlaveAtri.get("OTVALOR13"));
				params.put("PV_OTVALOR14_I", valoreLlaveAtri.get("OTVALOR14"));
				params.put("PV_OTVALOR15_I", valoreLlaveAtri.get("OTVALOR15"));
				params.put("PV_OTVALOR16_I", valoreLlaveAtri.get("OTVALOR16"));
				params.put("PV_OTVALOR17_I", valoreLlaveAtri.get("OTVALOR17"));
				params.put("PV_OTVALOR18_I", valoreLlaveAtri.get("OTVALOR18"));
				params.put("PV_OTVALOR19_I", valoreLlaveAtri.get("OTVALOR19"));
				params.put("PV_OTVALOR20_I", valoreLlaveAtri.get("OTVALOR20"));
				params.put("PV_OTVALOR21_I", valoreLlaveAtri.get("OTVALOR21"));
				params.put("PV_OTVALOR22_I", valoreLlaveAtri.get("OTVALOR22"));
				params.put("PV_OTVALOR23_I", valoreLlaveAtri.get("OTVALOR23"));
				params.put("PV_OTVALOR24_I", valoreLlaveAtri.get("OTVALOR24"));
				params.put("PV_OTVALOR25_I", valoreLlaveAtri.get("OTVALOR25"));
				params.put("PV_OTVALOR26_I", valoreLlaveAtri.get("OTVALOR26"));
				
				
				if(es1clave){
					tablasApoyoDAO.guardaValoresTablaApoyo1Clave(params);
				}else {
					tablasApoyoDAO.guardaValoresTablaApoyo(params);
				}
		}
		
		/*
		 * Para Hacer Save de nuevos registros.
		 */
		
		params.put("PV_ACCION_I", Constantes.INSERT_MODE);
		
		for(Map<String, String> valoreLlaveAtri : saveList){
				params.put("PV_OTCLAVE1_I", valoreLlaveAtri.get("OTCLAVE1"));
				params.put("PV_OTCLAVE2_I", valoreLlaveAtri.get("OTCLAVE2"));
				params.put("PV_OTCLAVE3_I", valoreLlaveAtri.get("OTCLAVE3"));
				params.put("PV_OTCLAVE4_I", valoreLlaveAtri.get("OTCLAVE4"));
				params.put("PV_OTCLAVE5_I", valoreLlaveAtri.get("OTCLAVE5"));
				
				params.put("PV_FEDESDE_I", valoreLlaveAtri.get("FEDESDE"));
				params.put("PV_FEHASTA_I", valoreLlaveAtri.get("FEHASTA"));
				
				params.put("PV_OTVALOR01_I", valoreLlaveAtri.get("OTVALOR01"));
				params.put("PV_OTVALOR02_I", valoreLlaveAtri.get("OTVALOR02"));
				params.put("PV_OTVALOR03_I", valoreLlaveAtri.get("OTVALOR03"));
				params.put("PV_OTVALOR04_I", valoreLlaveAtri.get("OTVALOR04"));
				params.put("PV_OTVALOR05_I", valoreLlaveAtri.get("OTVALOR05"));
				params.put("PV_OTVALOR06_I", valoreLlaveAtri.get("OTVALOR06"));
				params.put("PV_OTVALOR07_I", valoreLlaveAtri.get("OTVALOR07"));
				params.put("PV_OTVALOR08_I", valoreLlaveAtri.get("OTVALOR08"));
				params.put("PV_OTVALOR09_I", valoreLlaveAtri.get("OTVALOR09"));
				params.put("PV_OTVALOR10_I", valoreLlaveAtri.get("OTVALOR10"));
				params.put("PV_OTVALOR11_I", valoreLlaveAtri.get("OTVALOR11"));
				params.put("PV_OTVALOR12_I", valoreLlaveAtri.get("OTVALOR12"));
				params.put("PV_OTVALOR13_I", valoreLlaveAtri.get("OTVALOR13"));
				params.put("PV_OTVALOR14_I", valoreLlaveAtri.get("OTVALOR14"));
				params.put("PV_OTVALOR15_I", valoreLlaveAtri.get("OTVALOR15"));
				params.put("PV_OTVALOR16_I", valoreLlaveAtri.get("OTVALOR16"));
				params.put("PV_OTVALOR17_I", valoreLlaveAtri.get("OTVALOR17"));
				params.put("PV_OTVALOR18_I", valoreLlaveAtri.get("OTVALOR18"));
				params.put("PV_OTVALOR19_I", valoreLlaveAtri.get("OTVALOR19"));
				params.put("PV_OTVALOR20_I", valoreLlaveAtri.get("OTVALOR20"));
				params.put("PV_OTVALOR21_I", valoreLlaveAtri.get("OTVALOR21"));
				params.put("PV_OTVALOR22_I", valoreLlaveAtri.get("OTVALOR22"));
				params.put("PV_OTVALOR23_I", valoreLlaveAtri.get("OTVALOR23"));
				params.put("PV_OTVALOR24_I", valoreLlaveAtri.get("OTVALOR24"));
				params.put("PV_OTVALOR25_I", valoreLlaveAtri.get("OTVALOR25"));
				params.put("PV_OTVALOR26_I", valoreLlaveAtri.get("OTVALOR26"));
				
				
				if(es1clave){
					tablasApoyoDAO.guardaValoresTablaApoyo1Clave(params);
				}else {
					tablasApoyoDAO.guardaValoresTablaApoyo(params);
				}
		}
		
		/*
		 * Para Hacer Update de registros modificados.
		 */
		
		params.put("PV_ACCION_I", Constantes.UPDATE_MODE);
		
		for(Map<String, String> valoreLlaveAtri : updateList){
				params.put("PV_OTCLAVE1_I", valoreLlaveAtri.get("OTCLAVE1"));
				params.put("PV_OTCLAVE2_I", valoreLlaveAtri.get("OTCLAVE2"));
				params.put("PV_OTCLAVE3_I", valoreLlaveAtri.get("OTCLAVE3"));
				params.put("PV_OTCLAVE4_I", valoreLlaveAtri.get("OTCLAVE4"));
				params.put("PV_OTCLAVE5_I", valoreLlaveAtri.get("OTCLAVE5"));
				
				params.put("PV_FEDESDE_I", valoreLlaveAtri.get("FEDESDE"));
				params.put("PV_FEHASTA_I", valoreLlaveAtri.get("FEHASTA"));
				
				params.put("PV_OTVALOR01_I", valoreLlaveAtri.get("OTVALOR01"));
				params.put("PV_OTVALOR02_I", valoreLlaveAtri.get("OTVALOR02"));
				params.put("PV_OTVALOR03_I", valoreLlaveAtri.get("OTVALOR03"));
				params.put("PV_OTVALOR04_I", valoreLlaveAtri.get("OTVALOR04"));
				params.put("PV_OTVALOR05_I", valoreLlaveAtri.get("OTVALOR05"));
				params.put("PV_OTVALOR06_I", valoreLlaveAtri.get("OTVALOR06"));
				params.put("PV_OTVALOR07_I", valoreLlaveAtri.get("OTVALOR07"));
				params.put("PV_OTVALOR08_I", valoreLlaveAtri.get("OTVALOR08"));
				params.put("PV_OTVALOR09_I", valoreLlaveAtri.get("OTVALOR09"));
				params.put("PV_OTVALOR10_I", valoreLlaveAtri.get("OTVALOR10"));
				params.put("PV_OTVALOR11_I", valoreLlaveAtri.get("OTVALOR11"));
				params.put("PV_OTVALOR12_I", valoreLlaveAtri.get("OTVALOR12"));
				params.put("PV_OTVALOR13_I", valoreLlaveAtri.get("OTVALOR13"));
				params.put("PV_OTVALOR14_I", valoreLlaveAtri.get("OTVALOR14"));
				params.put("PV_OTVALOR15_I", valoreLlaveAtri.get("OTVALOR15"));
				params.put("PV_OTVALOR16_I", valoreLlaveAtri.get("OTVALOR16"));
				params.put("PV_OTVALOR17_I", valoreLlaveAtri.get("OTVALOR17"));
				params.put("PV_OTVALOR18_I", valoreLlaveAtri.get("OTVALOR18"));
				params.put("PV_OTVALOR19_I", valoreLlaveAtri.get("OTVALOR19"));
				params.put("PV_OTVALOR20_I", valoreLlaveAtri.get("OTVALOR20"));
				params.put("PV_OTVALOR21_I", valoreLlaveAtri.get("OTVALOR21"));
				params.put("PV_OTVALOR22_I", valoreLlaveAtri.get("OTVALOR22"));
				params.put("PV_OTVALOR23_I", valoreLlaveAtri.get("OTVALOR23"));
				params.put("PV_OTVALOR24_I", valoreLlaveAtri.get("OTVALOR24"));
				params.put("PV_OTVALOR25_I", valoreLlaveAtri.get("OTVALOR25"));
				params.put("PV_OTVALOR26_I", valoreLlaveAtri.get("OTVALOR26"));
				
				
				if(es1clave){
					tablasApoyoDAO.guardaValoresTablaApoyo1Clave(params);
				}else {
					tablasApoyoDAO.guardaValoresTablaApoyo(params);
				}
		}
		
		return true; 
	}
}