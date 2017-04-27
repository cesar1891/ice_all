package mx.com.gseguros.portal.catalogos.dao;

import java.util.List;

import mx.com.gseguros.portal.general.model.BaseVO;

public interface ClausuladoDAO {
	
	public List<BaseVO> consultaClausulas(String cdclause, String dsclausu)
			throws Exception;

	public BaseVO insertaClausula(String dsclausu, String contenido)
			throws Exception;

	public BaseVO actualizaClausula(String cdclausu, String dsclausu, String contenido)
			throws Exception;

	public String consultaClausulaDetalle(String cdclausu)
			throws Exception;

}