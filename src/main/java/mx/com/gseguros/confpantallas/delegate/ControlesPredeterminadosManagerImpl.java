package mx.com.gseguros.confpantallas.delegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.confpantallas.base.dao.DinamicDaoInterface;
import mx.com.gseguros.confpantallas.model.DinamicColumnaAttrVo;
import mx.com.gseguros.confpantallas.model.NodoVO;

import org.apache.log4j.Logger;

public class ControlesPredeterminadosManagerImpl implements ControlesPredeterminadosManager {

	private Logger logger = Logger.getLogger(ControlesPredeterminadosManagerImpl.class);
	
	private DinamicDaoInterface dinamicDAO;
	
	/* TODO: Revisar si se utiliza o no
	public String getTablas(){
		
		String rgs = "";
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("operacion", "getListas");
		data.put("query", "ListadeTablasPredeterminados");
		try {
			HashMap<String, Object> dataR = new HashMap<String, Object>();
			//dataR = dnc.dispatch(data);
			logger.debug("data=" + data);
			List<Map> lts = (List<Map>) dataR.get("rgs");
			Iterator<Map> itLts = lts.iterator();
			StringBuffer strJson = new StringBuffer();
			strJson.append("{\"children\":[");
			while (itLts.hasNext()) {
				Map map = itLts.next();
				strJson.append("{\"text\":\"" + map.get("NOMBRE") + "\",\"leaf\":false, id:\"tabla_" + map.get("TABLA") + "\"},");
			}
			rgs = strJson.toString();
			rgs = rgs.substring(0, strJson.toString().length()-1);
			rgs = rgs + "]}";
			logger.debug("rgs="+ rgs);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return rgs;
	}
	*/
	
	
	public List<NodoVO> getInfo(String query, Boolean leaf, String idAttr, String cdramo) {
		
		List<NodoVO> nodos = new ArrayList<NodoVO>();
		String rgs = "";
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("operacion", "getListas");
		data.put("query", query);
		data.put("cdramo", cdramo);
		data.put("tabla", "");
		try {
			List<Map> lts = dinamicDAO.GetListados(data);
			
			logger.debug("lts==="+ lts);
			
			Iterator<Map> itLts = lts.iterator();
			StringBuffer strJson = new StringBuffer();
			strJson.append("{\"children\":[");
			while (itLts.hasNext()) {
				Map map = itLts.next();
				NodoVO nodo = new NodoVO();
				nodo.setId( new StringBuilder().append(idAttr).append("_").append(map.get("TABLA")).toString() );
				nodo.setText( new StringBuilder().append(map.get("NOMBRE")).toString() );
				nodo.setLeaf(leaf);
				nodos.add(nodo);
				strJson.append("{\"text\":\"").append(map.get("NOMBRE")).append("\",\"leaf\":").append(leaf).append(", id:\"").append(idAttr).append("_").append(map.get("TABLA")).append("\"},");
			}
			rgs = strJson.toString();
			rgs = rgs.substring(0, strJson.toString().length()-1);
			rgs = rgs + "]}";
			logger.debug("rgs="+ rgs);
			logger.debug("nodos="+ nodos);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return nodos;
	}

	public void setDinamicDAO(DinamicDaoInterface dinamicDAO) {
		this.dinamicDAO = dinamicDAO;
	}
	
	public List<DinamicColumnaAttrVo> getColumnas(String panelName) {
		List<DinamicColumnaAttrVo> rgs = new ArrayList<DinamicColumnaAttrVo>();
		DinamicColumnaAttrVo registro = new DinamicColumnaAttrVo();
		registro.setTexto("Uno");
		registro.setWidth(80);
		registro.setDataIndex("titulo");
		registro.setTipoG("string");
		registro.setName("grigG_2");
		rgs.add(registro);
		return rgs;
	}

	
}