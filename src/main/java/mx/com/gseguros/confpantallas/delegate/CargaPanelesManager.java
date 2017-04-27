package mx.com.gseguros.confpantallas.delegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mx.com.gseguros.confpantallas.model.DinamicComboVo;
import mx.com.gseguros.confpantallas.model.DinamicData;
import mx.com.gseguros.confpantallas.model.DinamicPanelAttrGetVo;

public interface CargaPanelesManager {
	
	/**
	 * 
	 * @return
	 */
	public List<DinamicComboVo> GetListadePaneles();
	
	/**
	 * 
	 * @param tabla
	 * @param valor
	 * @return
	 */
	public List<DinamicComboVo> getDataCombo(String tabla, String valor);
	
	/**
	 * 
	 * @param tabla
	 * @param valor
	 * @return
	 */
	public List<HashMap> getDataGrid(String tabla, String valor);
	
	/**
	 * 
	 * @param tabla
	 * @param valor
	 * @return
	 */
	public List<DinamicComboVo> getDataComboHijo(String tabla, String valor);
	
	/**
	 * 
	 * @param comboNames
	 * @param comboNamesHijo
	 * @return
	 */
	public List<DinamicData> GetListaTablas(List<String> comboNames, List<String> comboNamesHijo);
	
	/**
	 * 
	 * @param comboNamesHijo
	 * @param txt
	 * @return
	 */
	public String isSoon(List<String> comboNamesHijo, String txt);
	
	/**
	 * 
	 * @param panel
	 * @return
	 */
	public HashMap<String, Object> GeneraJson(String panel); 
	
	/**
	 * 
	 * @param listaAttrPanel
	 * @param idP
	 * @param listaCtrls
	 * @param cabecero
	 * @return
	 */
	public String transToJson(List<DinamicPanelAttrGetVo> listaAttrPanel, Integer idP, ArrayList<String> listaCtrls, String cabecero);
	
	/**
	 * 
	 * @param idP
	 * @return
	 */
	public ArrayList<Object> transControlesAttrToJson(Integer idP);	
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public List<DinamicPanelAttrGetVo> GetListaDinamicPanelAttrGetVo (HashMap<String, Object> data);
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public List<HashMap> GetLista (HashMap<String, Object> data);
	
}