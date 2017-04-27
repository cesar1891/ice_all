package mx.com.gseguros.confpantallas.delegate;

import java.util.HashMap;
import java.util.List;

import mx.com.gseguros.confpantallas.base.dao.DinamicDaoInterface;
import mx.com.gseguros.confpantallas.model.DinamicTatriVo;
import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


public class AdminBuscaControlDelegate {
	
	public AdminBuscaControlDelegate() {
		super();
		//TODO: cambiar usando Spring Ioc:
		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext());
		dinamicDAO = (DinamicDaoInterface)context.getBean("dinamicDAOImpl");
	}

	private DinamicDaoInterface dinamicDAO; 

	public String gatDataControl(String ramo, String descripcion, String query){

		String rgs = "";
		HashMap<String, String> data = new HashMap<String, String>();
		try {
			data.put("operacion", "getListas");
			data.put("query", query);
			data.put("cdramo", ramo);
			data.put("descripcion", descripcion);
			List<DinamicTatriVo> ltsP = dinamicDAO.getDatosControlTatrisit(data);
			JSONArray jsonObject = JSONArray.fromObject(ltsP);
			System.out.println(jsonObject);
			rgs = jsonObject.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rgs;
	}

	public void setDinamicDAO(DinamicDaoInterface dinamicDAO) {
		this.dinamicDAO = dinamicDAO;
	}
	
}