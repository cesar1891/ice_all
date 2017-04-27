package mx.com.gseguros.confpantallas.base.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.confpantallas.model.DinamicComboVo;
import mx.com.gseguros.confpantallas.model.DinamicControlAttrVo;
import mx.com.gseguros.confpantallas.model.DinamicControlVo;
import mx.com.gseguros.confpantallas.model.DinamicItemBean;
import mx.com.gseguros.confpantallas.model.DinamicPanelAttrGetVo;
import mx.com.gseguros.confpantallas.model.DinamicPanelAttrVo;
import mx.com.gseguros.confpantallas.model.DinamicPanelVo;
import mx.com.gseguros.confpantallas.model.DinamicTatriVo;


public interface DinamicDaoInterface {
	
	public List<DinamicControlAttrVo> GetAttrGrid(HashMap<String, String> mapa);
	
	public List<Map> GetListados (HashMap<String, String> mapa);
	
	public List<HashMap> GetListadosHM (HashMap<String, String> mapa);
	
	public List<HashMap> GetDataGrid (String qry);
	
	public List<DinamicTatriVo> getDatosControlTatrisit (HashMap<String, String> mapa);
	
	public List<DinamicPanelAttrGetVo> getDinamicPanelAttrGetVo (HashMap<String, String> mapa);
	
	public List<DinamicComboVo> getListaCombox (HashMap<String, String> mapa);
	
	public List<DinamicComboVo> getDinamicComboVo (HashMap<String, String> mapa);

	public List<DinamicItemBean> getDinamicItemBean (HashMap<String, String> mapa);
	
	public String getString (HashMap<String, String> mapa);
	
	public void ejecuta(HashMap<String, String> mapa);
	
	public void ejecuta(String query, DinamicPanelVo objeto);
	
	public void ejecuta(String query, DinamicPanelAttrVo objeto);
	
	public void ejecuta(String query, DinamicControlVo objeto);
	
	public void ejecuta(String query, DinamicControlAttrVo objeto);
	
	public void ejecutaG(String query, DinamicControlAttrVo objeto);
	
	public void ejecutaSql(String query, DinamicControlAttrVo objeto);
	
	public String setCFExtjs (HashMap<String, String> mapa);
	
	public String setPanel (HashMap<String, Object> mapa);
	
	public List<DinamicPanelVo> getPanelVORowMapper(String qry);

	public List<DinamicPanelAttrVo> getPanelAttrVORowMapper(String panel);
	
	public List<DinamicControlVo> getControlVORowMapper(String panel);
	
	public List<DinamicControlAttrVo> getControlAttrVORowMapper(String panel, Integer control);
	
	public List<DinamicPanelAttrVo> getColumnasAttrVORowMapper(String namePanel);
	
	public DinamicPanelVo mapRow(ResultSet rs, int rownumber) throws SQLException; 
	
}