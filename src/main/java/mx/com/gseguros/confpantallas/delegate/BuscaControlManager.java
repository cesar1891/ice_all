package mx.com.gseguros.confpantallas.delegate;

import java.util.List;

import mx.com.gseguros.confpantallas.model.DinamicTatriVo;



public interface BuscaControlManager {

	/**
	 * 
	 * @param ramo
	 * @param descripcion
	 * @param query
	 * @return
	 */
	public List<DinamicTatriVo> getDataControl(String ramo, String descripcion, String query);
	
}