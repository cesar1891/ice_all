package mx.com.gseguros.wizard.configuracion.producto.service;

import java.util.List;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.tipoObjeto.model.DatoVariableObjetoVO;
import mx.com.gseguros.wizard.configuracion.producto.tipoObjeto.model.TipoObjetoVO;
import mx.com.gseguros.wizard.model.MensajesVO;


public interface TipoObjetoManager {

	List<LlaveValorVO> catalogoTipoObjetoJson(String tipoObjeto)throws ApplicationException;

	void agregaObjetoAlCatalogo(LlaveValorVO objeto)throws ApplicationException;

	List<DatoVariableObjetoVO> listaDatosVariablesObjetos(String codigoObjeto)throws ApplicationException;

	void agregarTipoObjetoInciso(TipoObjetoVO objeto)throws ApplicationException;
	
	MensajesVO borrarTipoObjetoInciso(TipoObjetoVO objeto)throws ApplicationException;

	void agregaDatosVariablesObjeto(List<DatoVariableObjetoVO> atributosObjeto)throws ApplicationException;
	
	void borraTatriObjeto(TipoObjetoVO objeto)throws ApplicationException;
	
	/**
	 * Metodo que valida si un atributo variable de Objeto tiene hijos asociados.
	 * @param tipoObjeto Atributo Variable de Objeto que se va a validar
	 * @return true si tiene hijos asociados, false si no los tiene.
	 * @throws ApplicationException
	 */
	public boolean tieneHijosAtributoVariableObjeto(TipoObjetoVO tipoObjeto) throws ApplicationException;

}
