package mx.com.gseguros.portal.general.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal.model.EmpresaVO;
import mx.com.aon.portal.model.RamaVO;
import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal.model.UsuarioRolEmpresaVO;
import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.exception.DaoException;
import mx.com.gseguros.portal.general.dao.UsuarioDAO;
import mx.com.gseguros.portal.general.dao.impl.UsuarioDAOImpl;
import mx.com.gseguros.portal.general.model.RolVO;
import mx.com.gseguros.portal.general.model.UsuarioVO;
import mx.com.gseguros.portal.general.service.UsuarioManager;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.Utils;

import org.apache.log4j.Logger;

public class UsuarioManagerImpl implements UsuarioManager {
	
	private Logger logger = Logger.getLogger(UsuarioDAOImpl.class);
	
	
	private UsuarioDAO usuarioDAO;
	
	
	@Override
	public boolean creaEditaRolSistema(Map<String, String> params) throws Exception {
		if(Constantes.MSG_TITLE_OK.equals(usuarioDAO.creaEditaRolSistema(params))){
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public GenericVO guardaUsuario(Map<String, String> params) throws Exception {
		return usuarioDAO.guardaUsuario(params);
	}

	@Override
	public void cambiaEstatusUsuario(Map<String, String> params) throws Exception {
		usuarioDAO.cambiaEstatusUsuario(params);
	}

	@Override
	public List<UsuarioVO> obtieneUsuarios(Map<String, String> params) throws Exception {
		return usuarioDAO.obtieneUsuarios(params);
	}

	@Override
	public List<GenericVO> obtienerRolesPorPrivilegio(Map<String, String> params) throws Exception{
		try {
			return usuarioDAO.obtienerRolesPorPrivilegio(params);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
		
	}
	
	@Override
	public List<Map<String, String>> obtieneRolesUsuario(Map<String, String> params) throws Exception{
		try {
			return usuarioDAO.obtieneRolesUsuario(params);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	
	}
	@Override
	public List<Map<String, String>> obtieneImpresorasUsuario(String cdusuario) throws Exception{
		logger.debug(Utils.join(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ obtieneImpresorasUsuario @@@@@@"
				,"\n@@@@@@ params=" , cdusuario
				));
		List<Map<String, String>> imp=null;
		String paso=null;
		try {
			paso="Obteniendo impresoras";
			imp=usuarioDAO.obtieneImpresorasUsuario(cdusuario);
			
			paso="Convertiendo booleanos y formato";
			for(Map<String, String> m: imp){
				m.put("ALTA", m.get("ALTA").equalsIgnoreCase("S")?"true":"false");
				m.put("DISPONIBLE", m.get("DISPONIBLE")!=null && m.get("DISPONIBLE").equalsIgnoreCase("S")?"SI":"NO");
				m.put("IMPRESORA",m.get("IMPRESORA").toUpperCase());
				m.put("DESCRIPCION",m.get("DESCRIPCION").toUpperCase());
				
			}
			logger.debug(Utils.join(
					"\n@@@@@ imp= ",imp));
			
		} catch (Exception ex) {
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.join(
				 "\n@@@@@@ obtieneImpresorasUsuario @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return imp;
	}
	
	@Override
	public String habilitaDeshabilitaImpresora(String pv_habilita,
			  String pv_impresora_i,
			  String pv_CdUsuari_i) throws Exception{
		
		logger.debug(Utils.join(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ habilitaDeshabilitaImpresora @@@@@@"
				,"\n@@@@@@ pv_habilita=" , pv_habilita
				,"\n@@@@@@ pv_impresora_i=" , pv_impresora_i
				,"\n@@@@@@ pv_CdUsuari_i=" , pv_CdUsuari_i
				));
		
		String paso=null,mensaje=null;
		try{
			paso="Haciendo peticion DAO";
			mensaje=usuarioDAO.habilitaDeshabilitaImpresora(pv_habilita, pv_impresora_i, null);
			
		}catch (Exception ex) {
			Utils.generaExcepcion(ex, paso);
		}
		
		
		logger.debug(Utils.join(
				 "\n@@@@@@ habilitaDeshabilitaImpresora @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return mensaje;
		
	}
	
	@Override
	public String insertaActualizaImpresora(String pv_nombre_i,
											  String pv_ip_i,
											  String pv_tipo_i,
											  String pv_descripcion_i,
											  String pv_swactivo_i)
													  	throws Exception{
		
		
	
	logger.debug(Utils.join(
			 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
			,"\n@@@@@@ insertaActualizaImpresora @@@@@@"
			,"\n@@@@@@ pv_nombre_i=" , pv_nombre_i
			,"\n@@@@@@ pv_ip_i=" , pv_ip_i
			,"\n@@@@@@ pv_tipo_i=" , pv_tipo_i
			,"\n@@@@@@ pv_descripcion_i=" , pv_descripcion_i
			,"\n@@@@@@ pv_swactivo_i=" , pv_swactivo_i
			));
	
				String paso=null,mensaje=null;
				try{
					paso="Haciendo peticion DAO";
					mensaje=usuarioDAO.insertaActualizaImpresora(pv_nombre_i, pv_ip_i, pv_tipo_i, pv_descripcion_i, pv_swactivo_i);
					
				}catch (Exception ex) {
					Utils.generaExcepcion(ex, paso);
				}
				
				
				logger.debug(Utils.join(
			 "\n@@@@@@ insertaActualizaImpresora @@@@@@"
			,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
			));
	return mensaje;
		
		
		
		
	}
	
	@Override
	public boolean guardaImpresorasUsuario(String cdusuario,
										   String impresora,
										   String ip,
										   String tipo,
										   String descripcion,
										   String disponible,
										   String alta) throws Exception{
		
		
		logger.debug(Utils.join(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ guardaImpresorasUsuario @@@@@@"
				,"\n@@@@@@ cdusuario= " , cdusuario
				,"\n@@@@@@ impresora= " , impresora
				,"\n@@@@@@ ip= " , ip
				,"\n@@@@@@ tipo= " , tipo
				,"\n@@@@@@ descripcion= " , descripcion
				,"\n@@@@@@ disponible= " , disponible
				,"\n@@@@@@ alta= " , alta
				));
		boolean allUpdated = true;
		String paso=null;

		try{
			
			paso="Guarda impresoras";
			usuarioDAO.guardaImpresorasUsuario(cdusuario, ip, tipo, descripcion, disponible, alta, impresora);

    		
        		
		}catch(Exception ex)
		{
			
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(Utils.join(
				 "\n@@@@@@ guardaImpresorasUsuario @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		
		
		return allUpdated;
	}
	
	@Override
	public boolean guardaRolesUsuario(Map<String, String> params, List<Map<String, String>> saveList) throws Exception{
		boolean allUpdated = true;
		
		for(Map<String, String> rol : saveList){
			try {
				params.put("PV_ACCION_I", rol.get("EXISTE_ROL"));
				params.put("PV_CDSISROL_I", rol.get("CDSISROL"));
				usuarioDAO.guardaRolUsuario(params);
			} catch (DaoException daoExc) {
				logger.error("Error al guardar Rol ",daoExc);
				allUpdated = false;
			}
		}
		
		return allUpdated;
	}

	@Override
	public List<Map<String, String>> obtieneProductosAgente(Map<String, String> params) throws Exception{
		try {
			return usuarioDAO.obtieneProductosAgente(params);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
		
	}
	
	@Override
	public boolean guardaProductosAgente(Map<String, String> params, List<Map<String, String>> saveList) throws Exception{
		boolean allUpdated = true;
		
		for(Map<String, String> rol : saveList){
			try {
				params.put("PV_ACCION_I", rol.get("TIENE_CDRAMO"));
				params.put("PV_CDRAMO_I", rol.get("CDRAMO"));
				usuarioDAO.guardaProductoAgente(params);
			} catch (DaoException daoExc) {
				logger.error("Error al guardar Rol ",daoExc);
				allUpdated = false;
			}
		}
		
		return allUpdated;
	}
	
	
	/**
	 * Obtiene la lista de los Nodos y las Hojas para el arbol de Seleccion
	 * de Rol y Cliente
	 * @param User - usuario admitido desde el login
	 * @return Lista con elementos del Arbol.
	 * @throws Exception
	 */
	//TODO: Implementar el uso de este metodo, ya que actualmente se utiliza la forma anterior
	@Override
	public List<RamaVO> getClientesRoles(String user) throws Exception {
        
        List<UsuarioRolEmpresaVO> resultado = usuarioDAO.obtieneRolesCliente(user);

        List<RamaVO> listaRamas = null;
        if (resultado !=null) {
            listaRamas = new ArrayList<RamaVO>();

            RamaVO ramaVO = null;
            RamaVO children = null;

            List<RamaVO> listaChildrens = null;

            UsuarioRolEmpresaVO usuarioRolEmpresaVO = (UsuarioRolEmpresaVO)resultado.get(0);

            ramaVO = new RamaVO();
            ramaVO.setText(usuarioRolEmpresaVO.getDsElemen());
            ramaVO.setCodigoObjeto(usuarioRolEmpresaVO.getCdElemento());
            ramaVO.setNick(usuarioRolEmpresaVO.getCdUsuario());
            ramaVO.setName(usuarioRolEmpresaVO.getDsUsuario());
            ramaVO.setDsRol(usuarioRolEmpresaVO.getDsSisRol());
            ramaVO.setClaveRol(usuarioRolEmpresaVO.getCdSisRol());
            ramaVO.setCdElemento(usuarioRolEmpresaVO.getCdElemento());

            listaChildrens = new ArrayList<RamaVO>();

            listaRamas.add(ramaVO);
            boolean agregado = false;

            for (int i = 0; i < resultado.size(); i++) {
                usuarioRolEmpresaVO  =  (UsuarioRolEmpresaVO)resultado.get(i);
                if (usuarioRolEmpresaVO.getCdElemento().equals(ramaVO.getCodigoObjeto())) {
                    children = new RamaVO();
                    children.setText(usuarioRolEmpresaVO.getDsSisRol());
                    children.setCodigoObjeto(usuarioRolEmpresaVO.getCdSisRol());
                    children.setCdElemento(ramaVO.getCodigoObjeto());
                    listaChildrens.add(children);
                    agregado = true;
                } else {
                    //seteando los hijos de la rama anterior
                    ramaVO.setChildren(listaChildrens.toArray(new RamaVO[listaChildrens.size()]));
                    agregado = true;

                    ramaVO = new RamaVO();
                    ramaVO.setText(usuarioRolEmpresaVO.getDsElemen());
                    ramaVO.setCodigoObjeto(usuarioRolEmpresaVO.getCdElemento());
                    ramaVO.setNick(usuarioRolEmpresaVO.getCdUsuario());
                    ramaVO.setName(usuarioRolEmpresaVO.getDsUsuario());
                    ramaVO.setDsRol(usuarioRolEmpresaVO.getDsSisRol());
                    ramaVO.setClaveRol(usuarioRolEmpresaVO.getCdSisRol());
                    ramaVO.setCdElemento(usuarioRolEmpresaVO.getCdElemento());

                    children = new RamaVO();
                    children.setText(usuarioRolEmpresaVO.getDsSisRol());
                    children.setCodigoObjeto(usuarioRolEmpresaVO.getCdSisRol());
                    children.setCdElemento(usuarioRolEmpresaVO.getCdElemento());
                    listaChildrens = new ArrayList<RamaVO>();
                    listaChildrens.add(children);

                    listaRamas.add(ramaVO);

                }
            }
            if (agregado) {
                ramaVO.setChildren(listaChildrens.toArray(new RamaVO[listaChildrens.size()]));
            }
        }
        return listaRamas;
	}

	//TODO: Implementar el uso de este metodo, ya que actualmente se utiliza la forma anterior
	@Override
	public List<UserVO> getAttributesUser(String user)	throws Exception {
		
		List<UsuarioRolEmpresaVO> resultado = usuarioDAO.obtieneRolesCliente(user);
		
        List<UserVO> listaUsuarios = null;
        
        if (resultado !=null) {
        	
            listaUsuarios = new ArrayList<UserVO>();

            UserVO userVO = null;
            EmpresaVO empresaVO = null;
            RolVO rolVO = null;
            List<RolVO> listaRoles = null;

            UsuarioRolEmpresaVO usuarioRolEmpresaVO = (UsuarioRolEmpresaVO)resultado.get(0);

            userVO = new UserVO();
            userVO.setUser(usuarioRolEmpresaVO.getCdUsuario());
            userVO.setCodigoPersona(usuarioRolEmpresaVO.getCdPerson());
            userVO.setName(usuarioRolEmpresaVO.getDsUsuario());

            empresaVO = new EmpresaVO();
            empresaVO.setElementoId(usuarioRolEmpresaVO.getCdElemento());
            empresaVO.setNombre(usuarioRolEmpresaVO.getDsElemen());
            userVO.setEmpresa(empresaVO);
            listaRoles = new ArrayList<RolVO>();
            userVO.setRoles(listaRoles);

            listaUsuarios.add(userVO);

            for (int i = 0; i < resultado.size(); i++) {
                usuarioRolEmpresaVO  =  (UsuarioRolEmpresaVO)resultado.get(i);
                if (usuarioRolEmpresaVO.getCdElemento().equals(userVO.getEmpresa().getElementoId())) {
                    rolVO = new RolVO();
                    rolVO.setClave(usuarioRolEmpresaVO.getCdSisRol());
                    rolVO.setDescripcion(usuarioRolEmpresaVO.getDsSisRol());
                    userVO.getRoles().add(rolVO);
                } else {

                    userVO = new UserVO();
                    userVO.setUser(usuarioRolEmpresaVO.getCdUsuario());
                    userVO.setCodigoPersona(usuarioRolEmpresaVO.getCdPerson());
                    userVO.setName(usuarioRolEmpresaVO.getDsUsuario());

                    empresaVO = new EmpresaVO();
                    empresaVO.setElementoId(usuarioRolEmpresaVO.getCdElemento());
                    empresaVO.setNombre(usuarioRolEmpresaVO.getDsElemen());
                    userVO.setEmpresa(empresaVO);
                    listaRoles = new ArrayList<RolVO>();
                    userVO.setRoles(listaRoles);

                    rolVO = new RolVO();
                    rolVO.setClave(usuarioRolEmpresaVO.getCdSisRol());
                    rolVO.setDescripcion(usuarioRolEmpresaVO.getDsSisRol());
                    userVO.getRoles().add(rolVO);

                    listaUsuarios.add(userVO);

                }
            }
        }
        return listaUsuarios;
	}


	public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
		this.usuarioDAO = usuarioDAO;
	}
	
	
	
}