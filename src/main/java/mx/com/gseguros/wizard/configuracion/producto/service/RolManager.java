package mx.com.gseguros.wizard.configuracion.producto.service;

import java.util.List;

import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.rol.model.RolAtributoVariableVO;
import mx.com.gseguros.wizard.configuracion.producto.rol.model.RolVO;
import mx.com.gseguros.wizard.model.MensajesVO;

public interface RolManager {
	public abstract List<RolAtributoVariableVO> atributosVariablesJson(
			int codigoRamo, String codigotTipoSituacion, String codigoRol, int codigoNivel)
			throws ApplicationException;

	public abstract List<LlaveValorVO> catalogoRolesJson()
			throws ApplicationException;

	public abstract List<LlaveValorVO> catalogoObligatorioJson()
			throws ApplicationException;

	public abstract List<LlaveValorVO> catalogoAtributosVariablesJson()
			throws ApplicationException;

	public abstract void agregaRolCatalogo(LlaveValorVO rol)
			throws ApplicationException;

	public abstract void agregaAtributoVariableRol(
			List<RolAtributoVariableVO> atributosRol) throws ApplicationException;
	
	public abstract MensajesVO eliminaAtributoVariableRol(RolAtributoVariableVO atributoRol) throws ApplicationException;

	public abstract void agregarRolInciso(RolVO rol)throws ApplicationException;

	public abstract void agregarAtributoVariableCatalogo(
			RolAtributoVariableVO atributo)throws ApplicationException;

	public abstract RolVO obtieneRolAsociado(int codigoRamo,
			String codigoTipoSituacion, String codigoRol, int codigoNivel) throws ApplicationException;
	
	public abstract WrapperResultados eliminaRol(int codigoRamo, String codigoRol, int codigoNivel) throws ApplicationException;
	
	/**
	 * Metodo que valida si un atributo variable de Rol tiene hijos asociados.
	 * @param atributoRol Atributo Variable de Rol que se va a validar
	 * @return true si tiene hijos asociados, false si no los tiene.
	 * @throws ApplicationException
	 */
	public boolean tieneHijosAtributoVariableRol(RolAtributoVariableVO atributoRol) throws ApplicationException;
}
