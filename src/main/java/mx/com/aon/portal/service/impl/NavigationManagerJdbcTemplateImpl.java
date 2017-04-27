package mx.com.aon.portal.service.impl;

import java.util.HashMap;
import java.util.List;

import mx.com.aon.portal.model.IsoVO;
import mx.com.aon.portal.model.ItemVO;
import mx.com.aon.portal.model.RamaVO;
import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal.service.NavigationManager;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.exception.ApplicationException;
@Deprecated
public class NavigationManagerJdbcTemplateImpl extends AbstractManagerJdbcTemplateInvoke implements NavigationManager {
	
	public List<RamaVO> getClientesRoles(String user) throws ApplicationException {
		List<RamaVO> listaRolesClientes = null;
		try{
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("PV_CDUSUARIO_I", user);
			WrapperResultados respuesta = returnResult(params, "CARGA_ROLES_CLIENTES");
			
			listaRolesClientes=(List<RamaVO>) respuesta.getItemList();
		}catch (Exception bae) {
			throw new ApplicationException("Error retrieving data");
		}
		return(List<RamaVO>) listaRolesClientes;
	}
	
	public List<ItemVO> getMenuNavegacion(String perfil) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getNumRegistro(String user) throws ApplicationException {
		// TODO Auto-generated method stub
		return 0;
	}

	public IsoVO getVariablesIso(String user) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<UserVO> getAttributesUser(String user) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

}