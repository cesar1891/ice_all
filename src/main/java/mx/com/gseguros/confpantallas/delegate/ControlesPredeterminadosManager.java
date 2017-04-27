package mx.com.gseguros.confpantallas.delegate;

import java.util.List;

import mx.com.gseguros.confpantallas.model.DinamicColumnaAttrVo;
import mx.com.gseguros.confpantallas.model.NodoVO;


public interface ControlesPredeterminadosManager {
	
	/**
	 * 
	 * @return
	 */
	/* TODO: Revisar si se utiliza o no
	public String getTablas();
	*/
	
	/**
	 * 
	 * @param query
	 * @param leaf
	 * @param idAttr
	 * @param cdramo
	 * @return
	 */
	public List<NodoVO> getInfo(String query, Boolean leaf, String idAttr, String cdramo);
	public List<DinamicColumnaAttrVo> getColumnas(String panelName);
	
}