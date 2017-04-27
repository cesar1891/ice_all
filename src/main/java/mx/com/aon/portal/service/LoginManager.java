/*
 * AON
 * 
 * Creado el 24/01/2008 11:35:11 a.m. (dd/mm/aaaa hh:mm:ss)
 *
 * Copyright (C) Biosnet S.C.    
 * All rights reserved. Todos los derechos reservados.
 *
 * http://www.biosnetmx.com/
 */
package mx.com.aon.portal.service;

import java.util.Hashtable;

import mx.com.aon.portal.model.UserVO;

/**
 * LoginManager
 * 
 * <pre>
 *  Interfaz Manager para la validacion y consulta de acceso a la aplicacion
 * <Pre>
 * 
 * @author &lt;a href=&quot;mailto:freddy.juarez@biosnetmx.com&quot;&gt;Freddy Ju&aacute;rez&lt;/a&gt;
 * @version	 1.0
 * 
 * @since	 1.0
 * 
 */
public interface LoginManager {
	
	/**
	 * Valida si existe un usuario en LDAP deacuerdo a sus credenciales
	 * @param unicamenteExiste true si solo se desea validar el usuario, false false si se desea validar el usuario Y password
	 * @param user
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public boolean validaUsuarioLDAP(boolean unicamenteExiste, String user, String password) throws Exception;
	
	/**
	 * Valida los datos de conexion de LDAP
	 * @param dn
	 * @param password
	 * @return
	 */
	public boolean validaDatosConexionLDAP(String dn, String password) throws Exception;
	
	/**
	 * Obtiene los datos de conexion de LDAP
	 * @param user
	 * @param pass
	 * @return
	 */
	public Hashtable obtieneDatosConexionLDAP(String user, String pass);
	
	/**
	 * Inserta un nuevo usuario/password en LDAP
	 * @param user
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public boolean insertaUsuarioLDAP(String user, String password) throws Exception;


	/**
	 * Funcion que valida el usuario y password pasados por parametro y en caso
	 * de ser valido consulta el perfil de la pagina principal para agregarlo a
	 * la sesion.
	 * 
	 * @param user
	 *            - Identificador del usuario
	 * @return UserVO Bean con la informacion del usuario
	 * @throws Exception
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD
	 */
    UserVO obtenerDatosUsuario(String user) throws Exception;

	public boolean cambiarPasswordUsuarioLDAP(String user, String passwordNuevo) throws Exception;

	/**
	 * Eliminar un usuario/password en LDAP
	 * @param user
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public boolean eliminarUsuarioLDAP(String user) throws Exception;
}
