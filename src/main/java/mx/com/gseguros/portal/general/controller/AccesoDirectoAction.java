package mx.com.gseguros.portal.general.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.BaseObjectVO;
import mx.com.aon.portal.model.EmpresaVO;
import mx.com.aon.portal.model.IsoVO;
import mx.com.aon.portal.model.RamaVO;
import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal.service.LoginManager;
import mx.com.aon.portal.service.NavigationManager;
import mx.com.aon.portal.service.UsuarioManager;
import mx.com.aon.portal.service.principal.PrincipalManager;
import mx.com.gseguros.portal.general.model.RolVO;
import mx.com.gseguros.portal.general.util.RolSistema;
import mx.com.gseguros.utils.Constantes;

/**
 *
 * @author HMLT
 */
public class AccesoDirectoAction extends PrincipalCoreAction {

	private static final long serialVersionUID = 7885456537983878685L;
	
	static final Logger logger = LoggerFactory.getLogger(AccesoDirectoAction.class);

	public static final String DEFAULT_DATE_FORMAT_PARAM       = "defaultDateFormat";
	public static final String DEFAULT_DECIMAL_SEPARATOR_PARAM = "defaultDecimalSeparator";
	public static final String DEFAULT_DECIMAL_PRECISION_PARAM = "decimalPrecision";
	
	public static final String ACCESO_CODIGOS_POSTALES = "codigosPostales";
	public static final String ACCESO_COTIZADOR        = "cotizador";
	public static final String ACCESO_COTIZADOR_AUTO_INDIVIDUAL = "cotizadorAutoIndividual";
	public static final String ACCESO_COTIZADOR_AUTO_FLOTILLA   = "cotizadorAutoFlotilla";
	public static final String ACCESO_COTIZADOR_GRUPO  = "cotizadorGrupo";
	public static final String ACCESO_COTIZADOR_GRUPO2 = "cotizadorGrupo2";
	public static final String ACCESO_CONSULTA_POLIZAS = "consultaPolizas";
	public static final String ACCESO_CONSULTA_ASEGURADOS       = "consultaAsegurados";
	public static final String ACCESO_CLIENTE_UNICO    = "clienteUnico";
	public static final String ACCESO_ENDOSO_POLIZAS_NO_SICAPS  = "endosoPolizasNoSICAPS";
	public static final String ACCESO_ENDOSO_DOMPOLIZAS_NO_SICAPS = "endosoDomicilioNOSICAPS";  
	public static final String ENDOSOS_AUTOS           = "endososAutos";
	public static final String FLUJO_MESA_CONTROL      = "flujoMesaControl";
	public static final String IMPRESION_DOCUMENTOS    = "impresionDocumentos";
	public static final String IMPRESION_RECIBOS       = "impresionRecibos";
	public static final String MENU_PRINCIPAL          = "menuPrincipal";
	public static final String MESA_CONTROL_AGENTES    = "mesaControlAgentes";
	public static final String REDIRECT_SIMPLE         = "redirectSimple";
	public static final String ACCESO_RSTN_COTIZACION_SALUD_IND = "RstnCotiSalInd";
	public static final String ACCESO_RSTN_COMPLEMENTARIOS_SALUD_IND = "RstnCompSalInd";
	public static final String ACCESO_RSTN_COTIZAR_SALUD_COLECTIVO = "RstnCotiSalCol";
	public static final String ACCESO_RSTN_COMPLEMENTARIOS_SALUD_COL = "RstnCompSalCol";
	public static final String ACCESO_RSTN_CONSULTA_ASEGURADOS = "RstnConsAseg";
	public static final String ACCESO_RSTN_AUTORIZACION_SERVICIO = "RstnAutorizacionServ";
	public static final String ACCESO_RSTN_ALTA_PREVIO= "RstnAltaTramitePrevio";
	public static final String ACCESO_RSTN_ALTA_COMPLEMENTO = "RstnAltaTramiteComplemento";
	public static final String ACCESO_RSTN_CONSULTA_SINIESTROS = "RstnConsutaSiniestros";
	/**
	 * Success property
	 */
	private boolean success;

	private LoginManager loginManager;
	private NavigationManager navigationManager;
	@Autowired
	private mx.com.gseguros.portal.general.service.NavigationManager navigationManagerNuevo;
	private UsuarioManager usuarioManager;
	private transient PrincipalManager principalManagerJdbcTemplate;

	private HashMap<String, String> params;

	private String user;
	/**
	 * Clave de usuario de captura que env&iacute;a GSeguros
	 */
	private String e;
	private String codigoCliente;
	private String codigoRol;
	private String _codigoCliente = null;
	private boolean codigoValido;
	private int numReg;

	private List<RamaVO> listaRolCliente;
	private List<UserVO> userList;

	/**
	 * Crea el usuario en sesion automaticamente
	 * 
	 * @return String result
	 */
	public boolean instanciaUsuarioLigaDirecta() {
		logger.debug(">>>> Entrando a instanciar usuario para Liga Directa ****");
		try {

			creaSesionDeUsuario(user);

			obtenRolesClientes();

			setCodigoCliente(codigoCliente);
			setCodigoRol(codigoRol);
			// setCodigoCliente("6442");
			// setCodigoRol(RolSistema.AGENTE.getCdsisrol());

			obtenCodigoTree();

			UserVO usuario = (UserVO) session.get("USUARIO");

			logger.debug(">>>> usuario name: {}", usuario.getName());
			logger.debug(">>>> usuario user: {}", usuario.getUser());
			
			// Se setea el cdperson:
			UserVO userVO = loginManager.obtenerDatosUsuario(usuario.getUser());
			usuario.setCodigoPersona(userVO.getCodigoPersona());
			logger.debug(">>>> usuario codigopersona: {}", usuario.getCodigoPersona());
			
			logger.debug(">>>> usuario claveUsuarioCaptura: {}", usuario.getClaveUsuarioCaptura());
			if (usuario != null && usuario.getEmpresa() != null) {
				logger.debug(">>>> usuario empresa cdelemento id: {}", usuario.getEmpresa().getElementoId());
			}
		} catch (Exception e) {
			logger.error(">>>> Error en el proceso Interno", e);
			return false;
		}
		return true;
	}

	/**
	 * Realiza un acceso directo a distintos m&oacute;dulos de la
	 * aplicaci&oacute;n
	 * 
	 * @return
	 * @throws Exception
	 */
	public String accesoDirecto() throws Exception {

		Boolean rolExistente = false;
		String acceso = (String) params.get("acceso");
		String tipoUsuario = (String) params.get("tipoUsuario");
		logger.info(">>>> Entrando a Acceso Directo: {} con usuario: {}", acceso, user);

		if (ACCESO_CODIGOS_POSTALES.equals(acceso)
				|| ACCESO_COTIZADOR.equals(acceso)
				|| ACCESO_COTIZADOR_AUTO_INDIVIDUAL.equals(acceso)
				|| ACCESO_COTIZADOR_AUTO_FLOTILLA.equals(acceso)
				|| ACCESO_COTIZADOR_GRUPO.equals(acceso)
				|| ACCESO_COTIZADOR_GRUPO2.equals(acceso)
				|| ACCESO_CONSULTA_POLIZAS.equals(acceso)
				|| ACCESO_CONSULTA_ASEGURADOS.equals(acceso)
				|| ACCESO_CLIENTE_UNICO.equals(acceso)
				|| ENDOSOS_AUTOS.equals(acceso)
				|| MENU_PRINCIPAL.equals(acceso)
				|| MESA_CONTROL_AGENTES.equals(acceso)
				|| ACCESO_ENDOSO_POLIZAS_NO_SICAPS.equals(acceso)
				|| ACCESO_ENDOSO_DOMPOLIZAS_NO_SICAPS.equals(acceso)
				|| FLUJO_MESA_CONTROL.equals(acceso)
				|| IMPRESION_DOCUMENTOS.equals(acceso)
				|| IMPRESION_RECIBOS.equals(acceso)
				|| REDIRECT_SIMPLE.equals(acceso)
                || ACCESO_RSTN_COTIZACION_SALUD_IND.equals(acceso)
				|| ACCESO_RSTN_COMPLEMENTARIOS_SALUD_IND.equals(acceso)
				|| ACCESO_RSTN_COTIZAR_SALUD_COLECTIVO.equals(acceso)
				|| ACCESO_RSTN_COMPLEMENTARIOS_SALUD_COL.equals(acceso)
				|| ACCESO_RSTN_CONSULTA_ASEGURADOS.equals(acceso)
				|| ACCESO_RSTN_AUTORIZACION_SERVICIO.equals(acceso)
				|| ACCESO_RSTN_ALTA_PREVIO.equals(acceso)
				|| ACCESO_RSTN_ALTA_COMPLEMENTO.equals(acceso)
				|| ACCESO_RSTN_CONSULTA_SINIESTROS.equals(acceso)
		        ) {
			
			// Patch para traducir el rol, ya que el portal manda un codigo: 
			if (ACCESO_CONSULTA_POLIZAS.equals(acceso)) {
				// logica para asignar rol en base al parametro tipoUsuario:
				if ("2".equals(tipoUsuario)) {
					codigoRol = RolSistema.AGENTE.getCdsisrol();
				} else if ("7".equals(tipoUsuario)) {
					codigoRol = RolSistema.PROMOTOR_AUTO.getCdsisrol();
				}
			}
			
			// TODO: ELIMINAR CUANDO SE RESUELVA LA PARTE DEL ROL EMPLEADO DE AGENTE:
			if (RolSistema.AGENTE.getCdsisrol().equals(codigoRol)) {
				Pattern pattern = Pattern.compile("A[0-9]+");
				Matcher matcher = pattern.matcher(user);
				if (matcher.find()) {
					user = matcher.group(0);
					logger.info("Accede el Empleado del Agente {}", user);
				}
			}
			
			// Invocar servicio para obtener los roles:
			listaRolCliente = usuarioManager.getClientesRoles(user);
			// si el usuario no tiene el rol solicitado, redirigir a pagina de rol invalido
			for (RamaVO ramaVO : listaRolCliente) {
				for (Object obj : ramaVO.getChildren()) {
					if (((RamaVO) obj).getCodigoObjeto().equals(codigoRol)) {
						rolExistente = true;
						break;
					}
				}
			}
			if (!rolExistente) {
				acceso = "rolInvalido";
			} else {
				boolean sesionExitosa = instanciaUsuarioLigaDirecta();
				if (!sesionExitosa) {
					session.put("MsgLigaDirecta", "El usuario no existe o no tiene un rol asociado.");
				}
				logger.info(">>>> Redirigiendo a Acceso Directo: {}", acceso);
			}

		} else {
			logger.warn(">>>> No esta definido el Acceso Directo: {}", acceso);
			acceso = "login";
		}

		return acceso;
	}

	/**
	 * Crea la sesi&oacute;n de usuario en el sistema
	 * 
	 * @param usuario
	 * @param claveUsuarioCaptura
	 * @return
	 * @throws Exception
	 */
	private boolean creaSesionDeUsuario(String usuario) throws Exception {

		boolean exito = false;

		String dateFormat = ServletActionContext.getServletContext()
				.getInitParameter(DEFAULT_DATE_FORMAT_PARAM);
		// String decimalSeparator =
		// ServletActionContext.getServletContext().getInitParameter(DEFAULT_DECIMAL_SEPARATOR_PARAM);

		UserVO userVO = new UserVO();
		userVO.setUser(usuario);
		userVO = loginManager.obtenerDatosUsuario(usuario);

		logger.debug(">>>> DATOS USUARIO *****: {}", userVO);

		// userVO.setDecimalSeparator(decimalSeparator);
		IsoVO isoVO = navigationManagerNuevo.getVariablesIso(userVO.getUser());

		logger.debug(">>>> DATOS USUARIO222 *****: {}", userVO);
		userVO.setClientFormatDate(isoVO.getClientDateFormat());
		userVO.setFormatDate(dateFormat);
		userVO.setDecimalSeparator(isoVO.getFormatoNumerico());
		// Se agrega la clave interna de GSeguros de usuario que captura:
		logger.debug("claveUsuarioCaptura={}", e);
		userVO.setClaveUsuarioCaptura(e);

		logger.debug(">>>> DATOS USUARIO333 *****: {}", userVO);
		session.put(Constantes.USER, userVO);
		session.put("userVO", userVO);

		exito = true;
		return exito;
	}

	/**
	 * Metodo que carga la pantalla que contiene el arbol de clientes y roles
	 * 
	 * @return success
	 * @throws Exception
	 */
	public String obtenRolesClientes() throws Exception {

		String retorno = SUCCESS;

		// /////////////////////////////////////////////////////////////////////////////
		// //// Crear un usuario y complementar su propiedades: //////
		// //// formato decha, formato numerico, idioma, tamanio paginacion
		// grid, //////
		// //// cliente format date y region. Se guarda en sesion //////
		// /////////////////////////////////////////////////////////////////////////////
		UserVO usuario = (UserVO) session.get("USUARIO");
		session.put("MessageConf", null);
		listaRolCliente = usuarioManager.getClientesRoles(usuario.getUser());
		numReg = navigationManager.getNumRegistro(usuario.getUser());
		userList = usuarioManager.getAttributesUser(usuario.getUser());
		// Se agrega la clave interna de GSeguros de usuario que captura:
		Iterator<UserVO> itUserList = userList.iterator();
		while (itUserList.hasNext()) {
			UserVO usVO = (UserVO) itUserList.next();
			usVO.setClaveUsuarioCaptura(e);
		}

		logger.warn(">>>> Usuarios totales: {} pero solo el de sesion se complemento", userList != null ? userList.size() : "null");

		complementaUsuario(usuario);
		session.put("USUARIO", usuario);
		// /////////////////////////////////////////////////////////////////////////////

		// //////////////////////////////////////////////
		// //// Verificar los permisos del usuario //////
		// //////////////////////////////////////////////
		if (listaRolCliente == null || listaRolCliente.isEmpty())
		// sin permisos
		{
			// success = false; //jtezva -> inecesario, no es json
			session.clear();
			throw new Exception(
					"Usted no posee un Rol Asociado por favor contacte al Administrador!");
		} else if ((listaRolCliente.size() == 1)
				&& (listaRolCliente.get(0).getChildren().length == 1))
		// solo tiene un cliente y un rol para ese cliente, se redirecciona la
		// pagina
		{
			codigoRol = userList.get(0).getRoles().get(0).getClave();
			codigoCliente = userList.get(0).getEmpresa().getElementoId();
			_codigoCliente = userList.get(0).getEmpresa().getElementoId();
			boolean configuracionCompleta = false;
			try {
				principalManagerJdbcTemplate
						.configuracionCompleta(_codigoCliente);
				configuracionCompleta = true;
			} catch (Exception e) {
				configuracionCompleta = false;
				logger.error(e.getMessage(), e);
				addActionError(e.getMessage());
				// success = false; //jtezva -> inecesario, no es json
				session.put("MessageConf", e.getMessage()); // Se pone en sesion
															// el mensaje a
															// mostrar
				// return SUCCESS; //jtezva -> se sustituyo por la variable
				// retorno
			}
			if (configuracionCompleta) {
				for (UserVO user : userList) {
					usuario.setEmpresa(user.getEmpresa());
					if (usuario.getRoles() != null) {
						usuario.setRolActivo(usuario.getRoles().get(0));
					} else {
						usuario.setRolActivo(userList.get(0).getRoles().get(0));
					}
				}
				complementaUsuario(userList.get(0));

				session.put("CARGA_USUARIO_COMPLETO", userList);
				obtenCodigoTree();
				retorno = "load";
			}
		} else
		// tiene almenos dos clientes o dos roles distintos
		{
			logger.debug(">>>> listaRolCliente={}", listaRolCliente.size());
			for (RamaVO nodos : listaRolCliente) {
				if (nodos.getChildren() != null) {
					nodos.setLeaf(false);
				}
			}
			session.put("CARGA_USUARIO_COMPLETO", userList);
			success = true;
			logger.debug(">>>> retorno={}", retorno);
		}
		// //////////////////////////////////////////////

		return retorno;
	}

	@SuppressWarnings("unchecked")
	public String obtenCodigoTree() throws Exception {
		String retorno = SUCCESS;
		logger.debug(">>>> Entrada metodo getCodigoTree");
		if (StringUtils.isNotBlank(codigoCliente)
				&& StringUtils.isNotBlank(codigoRol))
		// Si se reciben los parametros del cliente
		{
			if (codigoCliente.trim().equals(codigoRol.trim()))
			// Si el cliente y el rol llegaron iguales desde el cliente
			{
				codigoValido = false;
			} else
			// Buscamos el usuario con ese rol para ese cliente
			{
				// Se carga la lista de roles que se puso en sesion cuando se
				// cargo
				// la vista del arbol con obtenRolesClientes()
				List<UserVO> listaUsuario = (List<UserVO>) session
						.get("CARGA_USUARIO_COMPLETO");

				EmpresaVO empresa = null;
				RolVO rolActivo = new RolVO();
				boolean rolActivado = false;
				for (UserVO userVO : listaUsuario) {
					empresa = userVO.getEmpresa();
					for (RolVO rols : userVO.getRoles()) {
						if (empresa.getElementoId().trim()
								.equals(codigoCliente.trim())
								&& rols.getClave().trim()
										.equals(codigoRol.trim())) {
							rolActivo = rols;
							rolActivado = true;
							_codigoCliente = userVO.getEmpresa()
									.getElementoId();
							break;
						}
					}
					if (rolActivado) {
						if (rolActivo.getClave().equals(
								RolSistema.AGENTE.getCdsisrol())) {
							userVO.setCodigoPersona("0");
						}
						userVO.setRolActivo(rolActivo);
						session.put("USUARIO", userVO);
						break;
					}
				}
				try {
					String msg = principalManagerJdbcTemplate
							.configuracionCompleta(_codigoCliente);
					logger.debug(">>>> Configuraci�n: {}", msg);
					codigoValido = true;
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					addActionError(e.getMessage());
					codigoValido = false;
					retorno = "confcompleta";
				}
			} // else
		} // if
		else
		// No se recibieron datos del cliente
		{
			codigoValido = false;
		}
		if (retorno.equals(SUCCESS))
		// si no intento guardar la configuracion de un usuario leido por
		// parametros incorrectos
		// o si se cargo un usuario, se cargo correctamente
		{
			logger.debug(">>>> Salida metodo getCodigoTree");
			logger.debug(">>>> CODIGO VALIDO: {}", codigoValido);
			UserVO usuario = (UserVO) session.get("USUARIO");
			complementaUsuario(usuario);
			session.put("USUARIO", usuario);
		}
		return retorno;
	}

	/**
	 * Metodo que complementa los datos necesarios de un usuario para que no se
	 * ciclen los jsp en taglibs.jsp: String directorioIdioma =
	 * "/biosnet/"+((mx.
	 * com.aon.portal.model.UserVO)session.getAttribute("USUARIO"
	 * )).getIdioma().getLabel();
	 * 
	 * @param usuario
	 */
	private void complementaUsuario(UserVO usuario) throws Exception {
		IsoVO isoLocal = navigationManagerNuevo.getVariablesIso(usuario.getUser());
		BaseObjectVO languague = new BaseObjectVO();
		languague.setValue(isoLocal.getCdIdioma());
		languague.setLabel(isoLocal.getLanguague());
		BaseObjectVO pais = new BaseObjectVO();
		pais.setValue(isoLocal.getPais());
		usuario.setPais(pais);
		String cdPais = usuario.getPais().getValue();
		logger.debug(">>>> El pa�s del usuario es : {}", cdPais);
		usuario.setFormatoFecha(isoLocal.getFormatoFecha());
		usuario.setFormatoNumerico(isoLocal.getFormatoNumerico());
		usuario.setIdioma(languague);
		usuario.setTamagnoPaginacionGrid(numReg);
		logger.debug(">>>> En seleccionaRolCliente: {}", isoLocal.getClientDateFormat());
		usuario.setClientFormatDate(isoLocal.getClientDateFormat());
		// Agregados c�digos de Idioma y Regi�n
		// languague.setValue(iso.getCdIdioma());
		// usuario.setIdioma(languague);
		BaseObjectVO region = new BaseObjectVO();
		region.setValue(isoLocal.getCdRegion());
		usuario.setRegion(region);
	}

	// Getters and setters:

	public HashMap<String, String> getParams() {
		return params;
	}

	public void setParams(HashMap<String, String> params) {
		this.params = params;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getCodigoCliente() {
		return codigoCliente;
	}

	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

	public String getCodigoRol() {
		return codigoRol;
	}

	public void setCodigoRol(String codigoRol) {
		this.codigoRol = codigoRol;
	}

	public void setLoginManager(LoginManager loginManager) {
		this.loginManager = loginManager;
	}

	public void setNavigationManager(NavigationManager navigationManager) {
		this.navigationManager = navigationManager;
	}

	public List<RamaVO> getListaRolCliente() {
		return listaRolCliente;
	}

	public void setListaRolCliente(List<RamaVO> listaRolCliente) {
		this.listaRolCliente = listaRolCliente;
	}

	public List<UserVO> getUserList() {
		return userList;
	}

	public void setUserList(List<UserVO> userList) {
		this.userList = userList;
	}

	public void setUsuarioManager(UsuarioManager usuarioManager) {
		this.usuarioManager = usuarioManager;
	}

	public boolean isCodigoValido() {
		return codigoValido;
	}

	public void setCodigoValido(boolean codigoValido) {
		this.codigoValido = codigoValido;
	}

	public int getNumReg() {
		return numReg;
	}

	public void setNumReg(int numReg) {
		this.numReg = numReg;
	}

	public void setPrincipalManagerJdbcTemplate(PrincipalManager principalManagerJdbcTemplate) {
		this.principalManagerJdbcTemplate = principalManagerJdbcTemplate;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getE() {
		return e;
	}

	public void setE(String e) {
		this.e = e;
	}

}