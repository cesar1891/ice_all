/*
 * AON
 * 
 * Creado el 24/01/2008 11:36:24 a.m. (dd/mm/aaaa hh:mm:ss)
 *
 * Copyright (C) Biosnet S.C.    
 * All rights reserved. Todos los derechos reservados.
 *
 * http://www.biosnetmx.com/
 */
package mx.com.aon.portal.service.impl;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import mx.com.aon.portal.model.PerfilVO;
import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal.model.UsuarioRolEmpresaVO;
import mx.com.aon.portal.service.LoginManager;
import mx.com.aon.tmp.Endpoint;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.general.dao.UsuarioDAO;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * LoginManagerImpl
 * 
 * <pre>
 *  Implementacion del manager para la autentificacion del usuario y consulta de perfil
 * de la pantalla principal
 * 
 * <Pre>
 * 
 * @author &lt;a href=&quot;mailto:freddy.juarez@biosnetmx.com&quot;&gt;Freddy Ju&aacute;rez&lt;/a&gt;
 * @version	 1.0
 * 
 * @since	 1.0
 * 
 */
public class LoginManagerImpl implements LoginManager {

	private static Logger logger = Logger.getLogger(LoginManagerImpl.class);

	private Map<String, Endpoint> endpoints;

	private String Ldap_provider_url;
	private String Ldap_factory_initial;
	private String Ldap_security_authentication;
	private String Ldap_security_principal;
	private String Ldap_security_credentials;
	private String Ldap_base_search;
	
	@Autowired
	private UsuarioDAO usuarioDAO;

	public UserVO obtenerDatosUsuario(String user) throws Exception {

		Map<String, String> mapa = new HashMap<String, String>();
		mapa.put("user", user);

		UserVO userVO = null;
		if (user == null || user.equals("")) {
			throw new ApplicationException("Usuario y/o password no validos");
		}

        List<UsuarioRolEmpresaVO> rolesCliente = usuarioDAO.obtieneRolesCliente(user);
		
		userVO = new UserVO();
		if(rolesCliente != null && rolesCliente.size() > 0) {
			UsuarioRolEmpresaVO usuarioRolEmpresaVO = (UsuarioRolEmpresaVO)rolesCliente.get(0);
			userVO.setUser(usuarioRolEmpresaVO.getCdUsuario());
			userVO.setCdUnieco(usuarioRolEmpresaVO.getCdUnieco());
			userVO.setCodigoPersona(usuarioRolEmpresaVO.getCdPerson());
			userVO.setName(usuarioRolEmpresaVO.getDsUsuario());
			//userVO.setLastName(user);
		}
		userVO.setPerfil("DEFAULT");

		// TODO: Probar quitando el siguiente codigo para intentar mejorar
		// el performance:
		PerfilVO perfil = new PerfilVO();
		perfil.setFooterCenter("/resources/static/infoTit.html");
		perfil.setFooterLeft("/resources/static/infoTit.html");
		perfil.setFooterRight("/resources/static/infoTit.html");
		perfil.setKnewthat("/resources/static/infoTit.html");
		perfil.setLeft_1("/resources/static/navTit.html");
		perfil.setLeft_2("/resources/static/infoTit.html");
		perfil.setLeft_3("/resources/static/infoTit.html");
		perfil.setLeft_4("/resources/static/infoTit.html");
		perfil.setLeft_5("/resources/static/infoTit.html");
		perfil.setMain("/resources/static/infoTit.html");
		perfil.setMainDown("/resources/static/infoTit.html");
		perfil.setNav("");
		perfil.setNews("/resources/static/infoTit.html");
		perfil.setOtherLeft("/resources/static/infoTit.html");
		perfil.setOtherRight("/resources/static/infoTit.html");
		perfil.setOthers("/resources/static/infoTit.html");
		perfil.setRight_1("/resources/static/infoTit.html");
		perfil.setRight_2("/resources/static/infoTit.html");
		perfil.setRight_3("/resources/static/infoTit.html");
		perfil.setRight_4("/resources/static/infoTit.html");
		perfil.setRight_5("/resources/static/infoTit.html");
		perfil.setTop("/resources/static/info.html");
		perfil.setTopCenter("/resources/static/infoTit.html");
		perfil.setTopLeft("/resources/static/infoTit.html");
		perfil.setTopRight("/resources/static/infoTit.html");

		// Endpoint manager = (Endpoint)endpoints.get("CONSULTA_PERFIL");
		userVO.setFuentesPerfil(perfil);

		return userVO;
	}

	public boolean validaUsuarioLDAP(boolean unicamenteExiste, String user,
			String password) throws Exception {
		boolean existeUsuario = false;
		DirContext ctx;
//		logger.debug("URLLDAP=" + Ldap_provider_url);
//		logger.debug("ContextoLDAP=" + Ldap_factory_initial);
//		logger.debug("TipoAuthLDAP=" + Ldap_security_authentication);
//		logger.debug("UsuarioLDAP=" + Ldap_security_principal);
//		logger.debug("PasswordLDAP=" + Ldap_security_credentials);
//		logger.debug("SearchBaseLDAP=" + Ldap_base_search);

		Hashtable env = obtieneDatosConexionLDAP(Ldap_security_principal,
				Ldap_security_credentials);
		
		ctx = new InitialLdapContext(env, null);

		SearchControls searchCtls = new SearchControls();
		String returnedAtts[] = { "uid", "sn", "givenName", "mail", "cn" };
		searchCtls.setReturningAttributes(returnedAtts);
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		String searchFilter = "(cn=" + user + ")";
		// String searchBase = "cn=Users,dc=biosnettcs,dc=com";
		NamingEnumeration<SearchResult> results = ctx.search(Ldap_base_search,
				searchFilter, searchCtls);
		while (results.hasMoreElements()) {
			SearchResult searchResult = (SearchResult) results.next();
			Attributes attrs = searchResult.getAttributes();
			// OBTENEMOS LA UNIDAD ORGANIZATIVA DEL UID BUSCADO CON SU UID Y
			// LO COMPLETAMOS CON LA BASE
			String dn = searchResult.getName() + "," + Ldap_base_search;

			if (attrs != null) {
				// EL UID EXISTE AHORA VALIDAR PASSWORD
				if (unicamenteExiste) {
					existeUsuario = true;
				} else {
					existeUsuario = validaDatosConexionLDAP(dn, password);
					// SI VALIDO ES false PASSWORD INCORRECTO, SI ES true
					// PASSWORD CORRECTO
				}

			}
		}
		ctx.close();
		return existeUsuario;
	}

	public boolean validaDatosConexionLDAP(String dn, String password) throws Exception{
		boolean validadaUsuario = false;
		Hashtable env1 = obtieneDatosConexionLDAP(dn, password);
			DirContext ctx1 = new InitialLdapContext(env1, null);
			validadaUsuario = true;
			ctx1.close();
		return validadaUsuario;
	}

	public Hashtable obtieneDatosConexionLDAP(String user, String pass) {
		Hashtable<String, String> env = new Hashtable<String, String>();
		try {
			env.put(Context.INITIAL_CONTEXT_FACTORY, Ldap_factory_initial);	
			env.put(Context.SECURITY_AUTHENTICATION,
					Ldap_security_authentication);
			env.put(Context.SECURITY_PRINCIPAL, user);
			env.put(Context.SECURITY_CREDENTIALS, pass);
			env.put(Context.PROVIDER_URL, Ldap_provider_url);

		} catch (Exception e) {
			logger.error("Error en la creacion de conexion LDAP", e);
		}
		return env;
	}

	// m&eacute;todo para insertar el registro en ldap
	public boolean insertaUsuarioLDAP(String user, String password)
			throws Exception {
		DirContext ctx;
		Hashtable env = obtieneDatosConexionLDAP(Ldap_security_principal,
				Ldap_security_credentials);
		logger.debug(env);

		ctx = new InitialLdapContext(env, null);
		Attributes matchAttrs = new BasicAttributes(true);
		matchAttrs.put(new BasicAttribute("uid", user));
		matchAttrs.put(new BasicAttribute("cn", user));
		matchAttrs.put(new BasicAttribute("sn", user));
		matchAttrs.put(new BasicAttribute("userpassword", password
				.getBytes("UTF8")));
		matchAttrs.put(new BasicAttribute("objectclass", "top"));
		matchAttrs.put(new BasicAttribute("objectclass", "person"));
		matchAttrs
				.put(new BasicAttribute("objectclass", "organizationalPerson"));
		matchAttrs.put(new BasicAttribute("objectclass", "inetorgperson"));
		String name = "cn=" + user + "," + Ldap_base_search;
		InitialLdapContext iniDirContext = (InitialLdapContext) ctx;
		iniDirContext.bind(name, ctx, matchAttrs);
		return true;
	}
	
	// m&eacute;todo para cambiar Password en ldap
		public boolean cambiarPasswordUsuarioLDAP(String user, String passwordNuevo)
				throws Exception {
			DirContext ctx;
			Hashtable env = obtieneDatosConexionLDAP(Ldap_security_principal,
					Ldap_security_credentials);
			logger.debug(env);
			ctx = new InitialLdapContext(env, null);
			
			ModificationItem[] mods = new ModificationItem[1];
			Attribute mod1 = new BasicAttribute("userpassword", passwordNuevo.getBytes("UTF8"));
			 mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, mod1);
            ctx.modifyAttributes("cn=" + user +","+ Ldap_base_search, mods);
	        return true;
		}

	@Override
	public boolean eliminarUsuarioLDAP(String user)
			throws Exception {
		try {
			DirContext ctx;
			Hashtable env = obtieneDatosConexionLDAP(Ldap_security_principal,
					Ldap_security_credentials);
			logger.debug(env);
			ctx = new InitialLdapContext(env, null);
			ctx.destroySubcontext("cn=" + user +","+ Ldap_base_search);
	        return true;
		} catch (Exception e) {
            return false;
        }
	}
	// Getters and Setters

	public void setEndpoints(Map<String, Endpoint> endpoints) {
		this.endpoints = endpoints;
	}

	public void setLdap_provider_url(String ldap_provider_url) {
		Ldap_provider_url = ldap_provider_url;
	}

	public void setLdap_factory_initial(String ldap_factory_initial) {
		Ldap_factory_initial = ldap_factory_initial;
	}

	public void setLdap_security_authentication(
			String ldap_security_authentication) {
		Ldap_security_authentication = ldap_security_authentication;
	}

	public void setLdap_security_principal(String ldap_security_principal) {
		Ldap_security_principal = ldap_security_principal;
	}

	public void setLdap_security_credentials(String ldap_security_credentials) {
		Ldap_security_credentials = ldap_security_credentials;
	}

	public void setLdap_base_search(String ldap_base_search) {
		Ldap_base_search = ldap_base_search;
	}
}
