package mx.com.gseguros.confpantallas.delegate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mx.com.gseguros.confpantallas.base.dao.DinamicDaoInterface;
import mx.com.gseguros.confpantallas.model.DinamicControlVo;
import mx.com.gseguros.confpantallas.model.DinamicOpenControlVO;
import mx.com.gseguros.confpantallas.model.DinamicOpenPanelVO;
import mx.com.gseguros.confpantallas.model.DinamicPanelAttrVo;
import mx.com.gseguros.confpantallas.model.DinamicPanelVo;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class AdminAbrePanelDelegate {
	private Logger logger = Logger.getLogger(AdminPanelesDelegate.class);
	private DinamicDaoInterface dinamicDAO;
	
	public AdminAbrePanelDelegate() {
		super();
		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext());
		dinamicDAO = (DinamicDaoInterface)context.getBean("dinamicDAOImpl");
	}

	public List<DinamicOpenPanelVO> ExistePanel(String panel){
		List<DinamicOpenPanelVO> rgs = new ArrayList<DinamicOpenPanelVO>(); 
		boolean isR = false;
		try{
			List<DinamicPanelVo> pnls = dinamicDAO.getPanelVORowMapper(panel);
			String hijos = "";
			for(DinamicPanelVo e:pnls) {
				if(hijos.equals("")){
					hijos = this.getHijos(pnls, e);
				}
				int isPadre = hijos.lastIndexOf(String.valueOf(e.getId()));
				if(isPadre < 0){
					if(isR){
						isR=false;
						hijos = "";
					}
					List<DinamicPanelAttrVo> attrPnl = dinamicDAO.getPanelAttrVORowMapper(String.valueOf(e.getId()));
					rgs.add(new DinamicOpenPanelVO(e, attrPnl, this.ltsControles(String.valueOf(e.getId()))));  
				}else{
					List<DinamicPanelAttrVo> attrPnlHijos = dinamicDAO.getPanelAttrVORowMapper(String.valueOf(e.getId()));
					DinamicOpenPanelVO pH = new DinamicOpenPanelVO(e, attrPnlHijos, this.ltsControles(String.valueOf(e.getId())));
					this.asignaHijo(rgs, pH);
					isR = true;
					System.out.println(pH);
				}
			}
		}catch  (Exception e){
			logger.error(e);
			logger.info("El panel " + panel + " no existe");
		}
		return rgs;
	}
	
	private List<DinamicOpenControlVO> ltsControles (String panel){
		List<DinamicOpenControlVO> rgs = new ArrayList<DinamicOpenControlVO>();
		for(DinamicControlVo c: dinamicDAO.getControlVORowMapper(panel)){
			rgs.add(new DinamicOpenControlVO(c, dinamicDAO.getControlAttrVORowMapper(panel, c.getIdControl())));
		}
		return rgs;
	}
	
	private String getHijos (List<DinamicPanelVo> pnls, DinamicPanelVo e){
		String rgs = "";
		Iterator<DinamicPanelVo> it = pnls.iterator();
		while (it.hasNext()) {
			DinamicPanelVo dinamicPanelVo = (DinamicPanelVo) it.next();
			if(dinamicPanelVo.getIdPadre() == e.getIdPadre() && dinamicPanelVo.getId() != e.getId()){
				rgs += String.valueOf(dinamicPanelVo.getId()) + "|";
			}
		}
		return rgs;
	}
	
	private void asignaHijo(List<DinamicOpenPanelVO> lts, DinamicOpenPanelVO pH){
		Iterator<DinamicOpenPanelVO> it = lts.iterator();
		while (it.hasNext()) {
			DinamicOpenPanelVO dinamicOpenPanelVO = (DinamicOpenPanelVO) it.next();
			if(dinamicOpenPanelVO.getPanel().getIdPadre() == pH.getPanel().getIdPadre()){
				dinamicOpenPanelVO.addAttrsHijos(pH);
				break;
			}
		}
	}
}
