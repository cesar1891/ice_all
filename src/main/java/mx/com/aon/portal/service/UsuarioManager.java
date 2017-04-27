package mx.com.aon.portal.service;

import java.util.List;

import mx.com.aon.portal.model.RamaVO;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;

/**
 * Interface de servicios para estructura.
 *
 */
public interface UsuarioManager {

    public List<RamaVO> getClientesRoles(String user)throws ApplicationException;

    public List<UserVO> getAttributesUser(String user) throws ApplicationException;

}
