package mx.com.aon.portal.service;

import java.util.List;

import mx.com.aon.portal.model.MenuPrincipalVO;
import mx.com.gseguros.exception.ApplicationException;
/**
 * 
 * @author sergio.ramirez
 *
 */
public interface MenuPrincipalManager {
/**
 * Interface utilizada por el action y la implementacion que cargan la lista del menu.
 * @param claveCliente
 * @param claveRol
 * @param usuario
 * @return
 * @throws ApplicationException
 */
	public List<MenuPrincipalVO> getListaMenus(String claveCliente, String claveRol, String usuario)throws ApplicationException;
	
	/**
	 * Interface utilizada por el action y la implementacion que cargan la lista del menu vertical.
	 * @param claveCliente
	 * @param claveRol
	 * @param usuario
	 * @return
	 * @throws ApplicationException
	 */
	public List<MenuPrincipalVO> getListaMenuVertical(String claveCliente,String claveRol, String usuario) throws ApplicationException;

}
