package mx.com.aon.portal.service;

import java.util.List;

import mx.com.aon.portal.model.IsoVO;
import mx.com.aon.portal.model.ItemVO;
import mx.com.aon.portal.model.RamaVO;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;

public interface NavigationManager {

	/**
	 * Extrae la lista con los elementos del menu de navegacion del portal
	 * @param perfil con el que se hace la consulta
	 * @return List con los elementos del menu
	 * @throws ApplicationException 
	 */
	public List<ItemVO> getMenuNavegacion(String perfil ) throws ApplicationException;

	public List<RamaVO> getClientesRoles(String user)throws ApplicationException;

	public int getNumRegistro(String user) throws ApplicationException;

	public IsoVO getVariablesIso(String user)throws ApplicationException;

	public List<UserVO> getAttributesUser(String user) throws ApplicationException;
	
}
