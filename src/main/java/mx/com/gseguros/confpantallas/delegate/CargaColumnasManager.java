package mx.com.gseguros.confpantallas.delegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mx.com.gseguros.confpantallas.base.dao.DinamicDaoInterface;
import mx.com.gseguros.confpantallas.model.DinamicColumnaAttrVo;
import mx.com.gseguros.confpantallas.model.DinamicPanelAttrVo;

import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class CargaColumnasManager{
	
	private Integer consec = 0;
	
	private DinamicDaoInterface dinamicDAO;
	
	public CargaColumnasManager(){
		super();
		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext());
		dinamicDAO = (DinamicDaoInterface)context.getBean("dinamicDAOImpl");

	}

	public List<DinamicColumnaAttrVo> getColumnas(String panelName) {
		List<DinamicColumnaAttrVo> rgs = new ArrayList<DinamicColumnaAttrVo>();
		List<DinamicPanelAttrVo> ltsP = dinamicDAO.getColumnasAttrVORowMapper(panelName);
		int vez = 0;
		HashMap<String, String> mapa = new HashMap<String, String>();
		for (DinamicPanelAttrVo dinamicPanelAttrVo : ltsP) {
			vez++;
			if("dataIndex".equals(dinamicPanelAttrVo.getAttr())){
				mapa.put("dataIndex", dinamicPanelAttrVo.getValor());
			}else if("text".equals(dinamicPanelAttrVo.getAttr())){
				mapa.put("text", dinamicPanelAttrVo.getValor());
			}else if("tipoG".equals(dinamicPanelAttrVo.getAttr())){
				mapa.put("tipoG", dinamicPanelAttrVo.getValor());
			}else if("width".equals(dinamicPanelAttrVo.getAttr())){
				mapa.put("width", dinamicPanelAttrVo.getValor());
			}
			if(vez == 4){
				vez = 0;
				rgs.add(this.setDataColumna(mapa));
			}
		}
		return rgs;
	}
	
	private DinamicColumnaAttrVo setDataColumna(HashMap<String, String> mapa){
		consec ++;
		DinamicColumnaAttrVo ctrol = new DinamicColumnaAttrVo();
		ctrol.setDataIndex(mapa.get("dataIndex"));
		ctrol.setTexto(mapa.get("text"));
		ctrol.setTipoG(mapa.get("tipoG"));
		ctrol.setWidth(Integer.valueOf(mapa.get("width")));
		ctrol.setName("colG_"+consec);
		return ctrol;
	}

}
