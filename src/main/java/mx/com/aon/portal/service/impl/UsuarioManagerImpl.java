package mx.com.aon.portal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mx.com.aon.portal.model.EmpresaVO;
import mx.com.aon.portal.model.RamaVO;
import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal.model.UsuarioRolEmpresaVO;
import mx.com.aon.portal.service.UsuarioManager;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.general.model.RolVO;

import org.apache.log4j.Logger;

public class UsuarioManagerImpl extends AbstractManagerJdbcTemplateInvoke implements UsuarioManager {

	private static Logger logger = Logger.getLogger(UsuarioManagerImpl.class);



	/**
	 * Obtiene la lista de los Nodos y las Hojas para el arbol de Seleccion
	 * de Rol y Cliente
	 * @param User - usuario admitido desde el login
	 * @return Lista con elementos del Arbol.
	 * @throws ApplicationException
	 */
	@SuppressWarnings("unchecked")
	public List<RamaVO> getClientesRoles(String user)throws ApplicationException {
        HashMap map = new HashMap();
        map.put("PV_CDUSUARIO_I", user);
        String endpointName = "CARGA_ROLES_CLIENTES";
        List resultado = getAllBackBoneInvoke(map, endpointName);
        List<RamaVO> listaRamas = null;
        if (resultado !=null) {
            listaRamas = new ArrayList<RamaVO>();


            RamaVO ramaVO = null;
            RamaVO children = null;

            RolVO rolVO = null;
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


	public List<UserVO> getAttributesUser(String user)	throws ApplicationException {
        HashMap map = new HashMap();
        map.put("PV_CDUSUARIO_I", user);
        String endpointName = "CARGA_ROLES_CLIENTES";
        List resultado = getAllBackBoneInvoke(map, endpointName);
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
            userVO.setCdUnieco(usuarioRolEmpresaVO.getCdUnieco());

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
                    userVO.setCdUnieco(usuarioRolEmpresaVO.getCdUnieco());

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

}
