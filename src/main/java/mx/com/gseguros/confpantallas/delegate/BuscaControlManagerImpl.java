package mx.com.gseguros.confpantallas.delegate;

import java.util.HashMap;
import java.util.List;

import mx.com.gseguros.confpantallas.base.dao.DinamicDaoInterface;
import mx.com.gseguros.confpantallas.model.DinamicTatriVo;

import org.apache.log4j.Logger;


public class BuscaControlManagerImpl implements BuscaControlManager {
	
	private Logger logger = Logger.getLogger(BuscaControlManagerImpl.class);
	
	private DinamicDaoInterface dinamicDAO;
	
	@Override
	public List<DinamicTatriVo> getDataControl(String ramo, String descripcion, String query) {

		List<DinamicTatriVo> ltsP = null;
		HashMap<String, String> data = new HashMap<String, String>();
		try {
			data.put("operacion", "getListas");
			data.put("query", query);
			data.put("cdramo", ramo);
			data.put("descripcion", descripcion);
			ltsP = dinamicDAO.getDatosControlTatrisit(data);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return ltsP;
	}

	public void setDinamicDAO(DinamicDaoInterface dinamicDAO) {
		this.dinamicDAO = dinamicDAO;
	}
	
}