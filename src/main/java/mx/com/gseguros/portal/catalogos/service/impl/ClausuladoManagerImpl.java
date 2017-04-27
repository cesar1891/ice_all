package mx.com.gseguros.portal.catalogos.service.impl;

import java.util.List;

import mx.com.gseguros.portal.catalogos.dao.ClausuladoDAO;
import mx.com.gseguros.portal.catalogos.service.ClausuladoManager;
import mx.com.gseguros.portal.general.model.BaseVO;

public class ClausuladoManagerImpl implements ClausuladoManager {
	
	private ClausuladoDAO clausuladoDAO;

	public List<BaseVO> consultaClausulas(String cdclause, String dsclausu) throws Exception {

		return clausuladoDAO.consultaClausulas(cdclause, dsclausu);
	}

	public BaseVO insertaClausula(String dsclausu, String contenido) throws Exception {
		
		return clausuladoDAO.insertaClausula(dsclausu, contenido);
	}

	public BaseVO actualizaClausula(String cdclausu, String dsclausu, String contenido) throws Exception {
		
		return clausuladoDAO.actualizaClausula(cdclausu, dsclausu, contenido);
	}

	public String consultaClausulaDetalle(String cdclausu) throws Exception {
		
		return clausuladoDAO.consultaClausulaDetalle(cdclausu);
	}

	public void setClausuladoDAO(ClausuladoDAO clausuladoDAO) {
		this.clausuladoDAO = clausuladoDAO;
	}
	
}