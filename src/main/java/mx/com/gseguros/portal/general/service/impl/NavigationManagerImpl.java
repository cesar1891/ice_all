package mx.com.gseguros.portal.general.service.impl;

import java.util.Date;
import java.util.List;

import mx.com.aon.portal.model.IsoVO;
import mx.com.aon.portal.model.ItemVO;
import mx.com.aon.portal.model.RamaVO;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.general.dao.UsuarioDAO;
import mx.com.gseguros.portal.general.service.NavigationManager;

import org.apache.log4j.Logger;

public class NavigationManagerImpl implements NavigationManager {
	
	private UsuarioDAO usuarioDAO;
	private static final Logger logger = Logger.getLogger(NavigationManagerImpl.class);
	
	@Override
	public List<ItemVO> getMenuNavegacion(String perfil) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public List<UserVO> getAttributesUser(String user) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public int getNumRegistro(String user) throws ApplicationException {
		// TODO Auto-generated method stub
		return 0;
	}

	//TODO: Implementar el uso de este metodo, ya que actualmente se utiliza la forma anterior
	@Override
	public List<RamaVO> getClientesRoles(String user) throws Exception {
		//return usuarioDAO.obtieneRolesCliente(user);
		//TODO: Completar codigo
		/*
		List<RamaVO> listaRolesClientes = null;
		try{
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("PV_CDUSUARIO_I", user);
			
			usuarioDAO.obtieneRolesCliente(user)
			
			WrapperResultados respuesta = returnResult(params, "CARGA_ROLES_CLIENTES");
			
			
			listaRolesClientes=(List<RamaVO>) respuesta.getItemList();
		}catch (Exception bae) {
			throw new ApplicationException("Error retrieving data");
		}
		return(List<RamaVO>) listaRolesClientes;
		*/
		return null;
	}
	
	
	@Override
	public IsoVO getVariablesIso(String user) throws Exception {
		return usuarioDAO.obtieneVariablesIso(user);
	}


	public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
		this.usuarioDAO = usuarioDAO;
	}

	@Override
	public void guardarSesion(String idSesion, String cdusuari, String cdsisrol, String userAgent, boolean esMovil, Date fecha) throws Exception
	{
		logger.info(""
				+ "\n###########################"
				+ "\n###### guardarSesion ######"
				);
		logger.info("idSesion: "+idSesion);
		logger.info("cdusuari: "+cdusuari);
		logger.info("cdsisrol: "+cdsisrol);
		logger.info("userAgent: "+userAgent);
		logger.info("esMovil: "+esMovil);
		logger.info("fecha: "+fecha);
		usuarioDAO.guardarSesion(idSesion,cdusuari,cdsisrol,userAgent,esMovil,fecha);
	}
}