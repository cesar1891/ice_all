package mx.com.aon.portal.web;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionContext;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.IsoVO;
import mx.com.aon.portal.model.RamaVO;
import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal.service.LoginManager;
import mx.com.aon.portal.service.NavigationManager;
import mx.com.gseguros.utils.Constantes;

public class AutenticacionAction extends PrincipalCoreAction {

	private static final long serialVersionUID = 0L;

	protected final transient Logger logger = LoggerFactory.getLogger(AutenticacionAction.class);

	private Map<String,String> params;
	private String user;
	private String password;
	private String passwordNuevo;
	private String decimalSeparator;
	private String dateFormat;
	private LoginManager loginManager;
	private NavigationManager navigationManager;
	@Autowired
	private mx.com.gseguros.portal.general.service.NavigationManager navigationManagerNuevo;
	private List<RamaVO> listaRolCliente;
	private boolean success;
	private String errorMessage;

	@Value("${login.auth.ldap.activa}")
    private String loginAuthLdapActiva;	
	
	@Value("${login.modo.agregar.usuarios.ldap}")
    private String loginModoAgregarUsuariosLdap;	
	
	public String execute() throws Exception {
		logger.debug("Entrando a execute");
		return INPUT;
	}

	/**
	 * Verifica si el usuario existe en LDAP
	 * @return mapa con datos de la verificacion
	 * @throws Exception
	 */
	public String existeUsuarioLDAP() throws Exception {
		try {
		    boolean existeUsuarioLDAP;
		    logger.debug("loginAuthLdapActiva={}", loginAuthLdapActiva);
            if(new Boolean(loginAuthLdapActiva)) {
                existeUsuarioLDAP = loginManager.validaUsuarioLDAP(true, user, password);
            } else {
                existeUsuarioLDAP = true;
            }
            params = new HashMap<String, String>();
            params.put("existeUsuarioLDAP", String.valueOf(existeUsuarioLDAP));
            params.put("modoAutenticacionLDAP", loginAuthLdapActiva);
            params.put("modoAgregarUsuariosLDAP", loginModoAgregarUsuariosLdap);
            
			success = true;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			errorMessage = "Error al validar credenciales de usuario en el servidor: " + ex.getMessage();
		}
		return SUCCESS;
	}
	
	
	public String autenticaUsuario() throws Exception {
		/**
		 * Si es true agrega los usuarios en LDAP si ya existen previamente en BD (flujo temporal), <br/> false si solo autentica en LDAP y BD (flujo correcto)
		 */
		if(new Boolean(loginModoAgregarUsuariosLdap)){
			logger.debug("Autentificacion,entrando a modo Agregar Usuarios a LDAP");
			return autenticaUsuarioAgregaLDAP();
		}
		
		logger.debug("Autentificacion,sin entrar a modo Agregar Usuarios a LDAP");
		
		try {
			
			boolean existeUsuario = true;
			if(new Boolean(loginAuthLdapActiva)){
				existeUsuario = loginManager.validaUsuarioLDAP(false, user, password); 
			}
			if (existeUsuario) {
				logger.info("Usuario {} ha sido valido exitosamente en LDAP, creando sesion...", user);
				success = creaSesionDeUsuario(user);
			} else {
				logger.info("El usuario {} no existe o la clave es incorrecta.", user);
				errorMessage = "El usuario no existe o la contrase�a es incorrecta";
			}
			return SUCCESS;

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			//errorMessage = "Error en el proceso de validaci\u00f3n de usuario. Consulte a Soporte T\u00e9cnico.";
			errorMessage = "Error en el proceso de validaci\u00f3n de usuario, detalle: " + ex.getMessage(); //Se agrega el mensaje de la Excepci�n en Pantalla
			return SUCCESS;
		}
	}
	
	public String autenticaUsuarioAgregaLDAP() throws Exception {
		try {
			boolean sesionUsuarioCreada = creaSesionDeUsuario(user);
			if (sesionUsuarioCreada) {
				boolean existeUsuarioLDAP = loginManager.validaUsuarioLDAP(true, user, password);
				
				if(existeUsuarioLDAP){
					logger.info("Usuario {} si existe en LDAP, validando Password...", user);
					boolean validPass = loginManager.validaUsuarioLDAP(false, user, password);
					if(!validPass){
						((SessionMap) session).invalidate();
						logger.info("Password Incorrecto!!! {}/{}", user, password);
						errorMessage = "El usuario no existe o la contrase�a es incorrecta";
					}else {
						logger.info("Password Correcto, redireccionando a menu de Roles...");
						success = true;
					}
				}else {
					logger.info("No existe usuario, Insertando usuario {} / {} en LDAP", user, password);
					loginManager.insertaUsuarioLDAP(user, password);
					logger.info("Usuario Creado en LDAP, redireccionando a menu de Roles...");
					success =  true;
				}
				
			} 
			return SUCCESS;

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			errorMessage = "Error en el proceso de validaci\u00f3n de usuario, detalle: " + ex.getMessage();
			return SUCCESS;
		}
	}
	
	
	private boolean creaSesionDeUsuario(String usuario) throws Exception {

		boolean exito = false;

		UserVO userVO = new UserVO();
		userVO.setUser(usuario);
		userVO = loginManager.obtenerDatosUsuario(usuario);

		userVO.setDecimalSeparator(decimalSeparator);
		IsoVO isoVO = navigationManagerNuevo.getVariablesIso(userVO.getUser());

		userVO.setClientFormatDate(isoVO.getClientDateFormat());
		userVO.setFormatDate(dateFormat);
		userVO.setDecimalSeparator(isoVO.getFormatoNumerico());

		session.put(Constantes.USER, userVO);
		session.put("userVO", userVO);

		//TODO: cambiar a nuevo manager *****
		listaRolCliente = navigationManager.getClientesRoles(userVO.getUser());
		
		logger.debug("listaRolCliente ====={}",listaRolCliente);

		if (listaRolCliente == null || listaRolCliente.isEmpty()) {
			session.clear();
			errorMessage = "Usted no posee un rol asociado, por favor contacte al administrador";
		} else {
			exito = true;
		}
		return exito;
	}
	
	
	public String logoutUsuario() throws Exception {
		try {
			logger.debug(":X :X :X Terminando Session X: X: X: {}", ServletActionContext.getRequest().getSession().getId());
			
			//session.clear();
			((SessionMap) session).invalidate();
			logger.debug("session={}", ActionContext.getContext().getSession());
			return SUCCESS;
		} catch (Exception ex) {
			logger.error("Error al terminar la sesion", ex);
			errorMessage = "Error al terminar la sesion";
			return SUCCESS;
		}
	}
	
	
	/**
	 * Petici\u00f3n para mantener la sesi\u00f3n del servidor 
	 * @return
	 * @throws Exception
	 */
	public String mantenerSesionActiva() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * Petici\u00f3n para mantener una sola sesi\u00f3n 
	 * @return
	 * @throws Exception
	 */
	public String mantenerSesionUnica() throws Exception {
		
		try {
			  String cdusuari = params.get("cdusuari");
//			  logger.debug("cdusuario="+ cdusuari);
			  String cdsisrol = params.get("cdsisrol");
//			  logger.debug("cdsisrol="+ cdsisrol);
			  
			  
			  if(session == null || cdusuari == null || cdsisrol == null)
			  {
				   params.put("bloqueo", "S");
//				   logger.debug("BLOQUEO");
			  }
			  else
			  {
				  UserVO usuario = (UserVO)session.get("USUARIO");
				  if(usuario==null)
				  {
					  params.put("bloqueo", "S");
//					  logger.debug("BLOQUEO");
				  }
				  else
				  {
					  String cdusuariSesion = usuario.getUser();
					  if(usuario.getRolActivo() != null)
					  {
						  String cdsisrolSesion = usuario.getRolActivo().getClave();
//						  logger.debug("cdusuariSesion= "+ cdusuariSesion+" \ncdsisrolSesion= "+cdsisrolSesion);
						  if(!cdusuari.equals(cdusuariSesion) || !cdsisrol.equals(cdsisrolSesion))
						  {
							  params.put("bloqueo", "S");
//							  logger.debug("BLOQUEO");
						  }
						  else
						  {
							  params.put("bloqueo", "N");
//							  logger.debug("DESBLOQUEO");
						  }
					  }
					  else
					  {
						  params.put("bloqueo", "S");
//						  logger.debug("BLOQUEO");
					  }
				  }
				  				  
			  }
			  success = true;
		} catch (Exception ex) {
			logger.error("Error en el proceso Interno", ex);
			errorMessage = "Error en el proceso Interno";
		}
		return SUCCESS;
	}
	
	/*
	 * Action temporal para GSeguros, que redirigira a la pantalla de cotizacion
	 * Salud Vital
	 * 
	 * @return
	 * @throws Exception
	 */
	/*
	public String cotizacionSaludVital() throws Exception {
		
		try {
			creaSesionDeUsuario(user);
		} catch (Exception ex) {
			logger.error("Error en el proceso Interno", ex);
			errorMessage = "Error en el proceso Interno";
		}
		
		return SUCCESS;
	}
	*/
	
//	private boolean validaSessionUnica(String usuario, String sessionId) throws Exception {
//
//		boolean exito = false;
//
//		
//		
//
//		return exito;
//	}
	
	public void setLoginManager(LoginManager loginManager) {
		this.loginManager = loginManager;
	}

	public void setNavigationManager(NavigationManager navigationManager) {
		this.navigationManager = navigationManager;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	public String getPasswordNuevo() {
		return passwordNuevo;
	}

	public void setPasswordNuevo(String passwordNuevo) {
		this.passwordNuevo = passwordNuevo;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}


	public void setNavigationManagerNuevo(
			mx.com.gseguros.portal.general.service.NavigationManager navigationManagerNuevo) {
		this.navigationManagerNuevo = navigationManagerNuevo;
	}


	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public mx.com.gseguros.portal.general.service.NavigationManager getNavigationManagerNuevo() {
		return navigationManagerNuevo;
	}
	
	public String getLoginAuthLdapActiva() {
		return loginAuthLdapActiva;
	}

	public String getLoginModoAgregarUsuariosLdap() {
		return loginModoAgregarUsuariosLdap;
	}
}