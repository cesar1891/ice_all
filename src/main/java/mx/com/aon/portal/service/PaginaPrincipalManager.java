package mx.com.aon.portal.service;

import java.util.List;

import mx.com.aon.portal.model.PortalVO;
import mx.com.gseguros.exception.ApplicationException;

/**
 * 
 * @author sergio.ramirez
 * 
 */
public interface PaginaPrincipalManager {
	
	/**
	 * Inteface del action que permite obtener de manera inmediata la lista de
	 * los atributos que se llenan para cargar los componentes de la pantalla
	 * principal.
	 * 
	 * @param rolUsuario - Rol del usuario que esta entrando al sistema.
	 * @param elemento - Elemento en la estructura a la que pertenece.
	 * @return
	 * @throws ApplicationException
	 */
	public List<PortalVO> getConfiguracionInicial(String rolUsuario,
			String elemento) throws ApplicationException;
	
	
	public String getUserEmail(String cdUsuario)throws ApplicationException;
}
