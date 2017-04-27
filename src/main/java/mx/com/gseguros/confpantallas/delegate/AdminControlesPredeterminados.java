package mx.com.gseguros.confpantallas.delegate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.confpantallas.base.dao.DinamicDaoInterface;

import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Does some thing in old style.
 *
 * @deprecated use {@link ControlesPredeterminadosManagerImpl} instead.  
 */
public class AdminControlesPredeterminados {

	public AdminControlesPredeterminados() {
		super();
		//TODO: cambiar usando Spring Ioc:
		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext());
		dinamicDAO = (DinamicDaoInterface)context.getBean("dinamicDAOImpl");
	}
	
	private DinamicDaoInterface dinamicDAO;
	
	public String getTablas(){
		String rgs = "";
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("operacion", "getListas");
		data.put("query", "ListadeTablasPredeterminados");
		try {
			HashMap<String, Object> dataR = new HashMap<String, Object>();
			//dataR = dnc.dispatch(data);
			System.out.println(data);
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
			System.out.println(rgs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rgs;
	}
	
	public String getInfo(String query, Boolean leaf, String idAttr, String cdramo){
		String rgs = "";
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("operacion", "getListas");
		data.put("query", query);
		data.put("cdramo", cdramo);
		data.put("tabla", "");
		try {
			List<Map> lts = dinamicDAO.GetListados(data);
			
			Iterator<Map> itLts = lts.iterator();
			StringBuffer strJson = new StringBuffer();
			strJson.append("{\"children\":[");
			while (itLts.hasNext()) {
				Map map = itLts.next();
				strJson.append("{\"text\":\"").append(map.get("NOMBRE")).append("\",\"leaf\":").append(leaf).append(", id:\"").append(idAttr).append("_").append(map.get("TABLA")).append("\"},");
			}
			rgs = strJson.toString();
			rgs = rgs.substring(0, strJson.toString().length()-1);
			rgs = rgs + "]}";
			System.out.println(rgs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rgs;
	}

	public void setDinamicDAO(DinamicDaoInterface dinamicDAO) {
		this.dinamicDAO = dinamicDAO;
	}
	
}