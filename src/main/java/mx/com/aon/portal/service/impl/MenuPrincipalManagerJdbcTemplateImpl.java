package mx.com.aon.portal.service.impl;

import java.util.HashMap;
import java.util.List;

import mx.com.aon.portal.model.MenuPrincipalVO;
import mx.com.aon.portal.service.MenuPrincipalManager;
import mx.com.gseguros.exception.ApplicationException;

import org.apache.log4j.Logger;

public class MenuPrincipalManagerJdbcTemplateImpl extends AbstractManagerJdbcTemplateInvoke implements MenuPrincipalManager {

	private static Logger logger = Logger.getLogger(MenuPrincipalManagerJdbcTemplateImpl.class);
	

	/**
	 *  Obtiene menu principal
	 *  Hace uso del Store Procedure PKG_MENU.P_FORMA_MENU
	 *  
	 *  @param cdElemento
	 *  @param cdRol
	 *  @param cdUsuario
	 *
	 *  @return List MenuPrincipalVO
	 *
	 *  @throws ApplicationException
	 */
	@SuppressWarnings("unchecked")
	public List<MenuPrincipalVO> getListaMenus(String cdElemento, String cdRol, String cdUsuario) throws ApplicationException {
		
        logger.debug("Entrando a getListaMenus()");
        HashMap map = new HashMap();
        map.put("PV_CDELEMENTO_I", cdElemento);
		map.put("PV_CDROL_I", cdRol);
		map.put("PV_CDUSUARIO_I", cdUsuario);
        String endpointName = "CARGA_MENU_PRINCIPAL";
        return  getAllBackBoneInvoke(map, endpointName);
	}
	
	/**
	 *  Obtiene el menu vertical
	 *  Hace uso del Store Procedure PKG_MENU.P_FORMA_MENU
	 */
	@SuppressWarnings("unchecked")
	public List<MenuPrincipalVO> getListaMenuVertical(String claveCliente, String claveRol, String usuario) throws ApplicationException {
		logger.debug("Entrando a getListaMenuVertical()");
        HashMap map = new HashMap();
        map.put("pv_cdelemento_i", claveCliente);
		map.put("pv_cdrol_i", claveRol);
		map.put("pv_cdusuario_i", usuario);
        String endpointName = "CARGA_MENU_VERTICAL";
        return  getAllBackBoneInvoke(map, endpointName);
	}
}
