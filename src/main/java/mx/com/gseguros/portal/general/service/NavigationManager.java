package mx.com.gseguros.portal.general.service;

import java.util.Date;
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
	public List<ItemVO> getMenuNavegacion(String perfil) throws ApplicationException;

	public List<RamaVO> getClientesRoles(String user) throws Exception;

	public int getNumRegistro(String user) throws ApplicationException;

	public IsoVO getVariablesIso(String user) throws Exception;

	public List<UserVO> getAttributesUser(String user) throws ApplicationException;
	
	public void guardarSesion(String idSesion, String cdusuari, String cdsisrol, String userAgent, boolean esMovil, Date fecha) throws Exception;
	
}
