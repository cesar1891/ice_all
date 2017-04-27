package mx.com.aon.portal.web;

import java.util.ArrayList;
import java.util.List;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.MenuPrincipalVO;
import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal.service.MenuPrincipalManager;

import org.apache.commons.lang3.StringUtils;

/**
 * @see Clase Java que carga el menu.
 * @author sergio.ramirez
 * 
 */

public class MenuAction extends PrincipalCoreAction {

	/**
	 * @serial Serial Version
	 */
	private static final long serialVersionUID = -7185390642113025561L;
	private static final String VENTANA_EXTERNA = "E";
	private boolean success;
	private MenuPrincipalManager menuPrincipalManagerJdbcTemplate;
	private List<MenuPrincipalVO> listaMenu;
	private List<MenuPrincipalVO> listaMenuVertical;
	private String claveCliente;
	private String claveRol;
	private String usuario;

	/**
	 * @see Metodo execute regresa por defecto INPUT
	 */
	public String execute() throws Exception {
		logger.debug("Entro al metodo execute");
		return INPUT;
	}

	/**
	 * Metodo que asigna los submenus jerarquicos del menu principal.
	 * 
	 * @param nivelPadre
	 * @return items
	 */
	public List<MenuPrincipalVO> buscaHijos(String nivelPadre) {
		List<MenuPrincipalVO> items = new ArrayList<MenuPrincipalVO>();
		for (MenuPrincipalVO item : listaMenu) {
			if (StringUtils.isNotBlank(item.getNivelPadre()) && item.getNivelPadre().equals(nivelPadre)){
				items.add(item);
		}
		}
		return items;
	}

	/**
	 * Metodo que asigna el contenido del menu principal carga la lista e itera
	 * el contenido y lo asigna con respecto al nivel jerarquico de cada una de
	 * estos.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String llenadoMenuPrincipal() throws Exception {

		UserVO user = (UserVO) session.get("USUARIO");

		if (user.getListaMenu() == null || user.getListaMenu().size() == 0) {

			claveCliente = user.getEmpresa().getElementoId();
			claveRol = user.getRolActivo().getClave();
			usuario = user.getUser();
			logger.debug("!!!Entrando al metodo llenadoMenuPrincipal()");
			logger.debug(". claveCliente=" + claveCliente);
			logger.debug(". claveRol=" + claveRol);
			logger.debug(". usuario=" + usuario);
			listaMenu = menuPrincipalManagerJdbcTemplate.getListaMenus(claveCliente, claveRol, usuario);
			//logger.debug("listaMenu=" + listaMenu + " Size:" + listaMenu.size());
			//logger.debug("listaMenu=" + listaMenu.toString());
			/**
			 * Por medio de este set se concatenaron la clave de Producto y
			 * Situacion para las pantallas que se despliegan del menu.
			 * Se agrego cdtitulo
			 */
			for (MenuPrincipalVO test : listaMenu) {
				if (StringUtils.isNotBlank(test.getHref())
						&& StringUtils.isNotBlank(test.getClaveRamo())
						&& StringUtils.isNotBlank(test.getClaveTipoSituacion())) {
					/*test.setHref(test.getHref() + "?CDRAMO="
							+ test.getClaveRamo() + "&&CDTIPSIT="
							+ test.getClaveTipoSituacion());*/
					test.setHref("javascript:LoadPage(\'" + test.getHref() 
							+ "?CDRAMO="       + test.getClaveRamo()
							+ "&CDTITULO="     + (test.getCdTitulo()==null?"":test.getCdTitulo()) 
							+ "&CDTIPSIT="     + test.getClaveTipoSituacion()+"\')");
				} 
				else if (StringUtils.isNotBlank(test.getClaveRamo())) {
					test.setHref("javascript:LoadPage(\'" + test.getHref() 
							+ "?CDTITULO="     + (test.getCdTitulo()==null?"":test.getCdTitulo())
							+ "&CDRAMO="       + test.getClaveRamo()+"\')");
					
				} else {
                   // Manejo del caso de los catalogos dinamicos
				   if (!VENTANA_EXTERNA.equals(test.getSwTipdes())) {	
	                   if (StringUtils.isNotBlank(test.getHref()) && test.getHref().indexOf("?")>0) {
	                        test.setHref("javascript:LoadPage(\'"+test.getHref()
	                        		+ "&CDTITULO=" + (test.getCdTitulo()==null?"":test.getCdTitulo())
	                        		+"\')");
	                   } else {
	                       //Manejo del resto de los casos
	                        test.setHref("javascript:LoadPage(\'"+test.getHref()
	                        		+ "?CDTITULO=" + (test.getCdTitulo()==null?"":test.getCdTitulo())
	                        		+"\')");
	                   }
				   } else {
					   if (StringUtils.isNotBlank(test.getHref()) && test.getHref().indexOf("?")>0) {
	                        test.setHref(test.getHref()
	                        		+ "&CDTITULO=" + (test.getCdTitulo()==null?"":test.getCdTitulo()));
	                   } else {
	                       //Manejo del resto de los casos
	                        test.setHref(test.getHref()
	                        		+ "?CDTITULO=" + (test.getCdTitulo()==null?"":test.getCdTitulo()));
	                   }
					   test.setHrefTarget("_blank");
				   }
                }
				
			}
			//logger.debug("listaMenu->" + listaMenu);
			if (listaMenu != null && !listaMenu.isEmpty()) {
				List<MenuPrincipalVO> menusPadres = buscaHijos("0");
				if (!menusPadres.isEmpty()) {
					for (MenuPrincipalVO menuPadre : menusPadres) {
						List<MenuPrincipalVO> menus2 = buscaHijos(menuPadre
								.getNivel());
						if (!menus2.isEmpty()) {
							for (MenuPrincipalVO menu2 : menus2) {
								List<MenuPrincipalVO> menus3 = buscaHijos(menu2
										.getNivel());
								if (!menus3.isEmpty()) {
									for (MenuPrincipalVO menu3 : menus3) {
										List<MenuPrincipalVO> menus4 = buscaHijos(menu3
												.getNivel());
										if (!menus4.isEmpty()) {
											for (MenuPrincipalVO menu4 : menus4) {
												List<MenuPrincipalVO> menus5 = buscaHijos(menu4
														.getNivel());
												if (!menus5.isEmpty()) {
													for (MenuPrincipalVO menu5 : menus5) {
														List<MenuPrincipalVO> menus6 = buscaHijos(menu5
																.getNivel());
														menu5.setMenu(menus6
																.toArray());
													}
													menu4.setMenu(menus5
															.toArray());
												}
											}
											menu3.setMenu(menus4.toArray());
										}
									}
									menu2.setMenu(menus3.toArray());
								}
							}
							menuPadre.setMenu(menus2.toArray());
						}
					}
				}
				listaMenu = menusPadres;
				//logger.debug("listaMenu->" + listaMenu);
				//logger.debug("listaMenu size = " + listaMenu.size());
			}
			user.setListaMenu(listaMenu);
			session.put("USUARIO", user);
		} else {
			this.listaMenu = user.getListaMenu();
		}
		
		
		//Para la Lista del Menu Vertical
		if (user.getListaMenuVertical() == null || user.getListaMenuVertical().size() == 0) {
			
			claveCliente = user.getEmpresa().getElementoId();
			claveRol = user.getRolActivo().getClave();
			usuario = user.getUser();
			listaMenuVertical = menuPrincipalManagerJdbcTemplate.getListaMenuVertical(claveCliente,claveRol, usuario);
			
			//logger.debug("listaMenuVertical->" + listaMenuVertical);
			logger.debug("esMovil" + session.get("ES_MOVIL"));
			if(listaMenuVertical != null){
			logger.debug("listaMenuVertical size: " + listaMenuVertical.size() );
			}
			
			user.setListaMenuVertical(listaMenuVertical);
			session.put("USUARIO", user);
		}else{
			this.listaMenuVertical = user.getListaMenuVertical();
			//logger.debug("user.getListaMenuVertical()=" + user.getListaMenuVertical());
			//logger.debug("user.getListaMenuVertical size= " + user.getListaMenuVertical().size());
		}
		
		success = true;
		return SUCCESS;
	}

	/**
	 * 
	 * @return
	 */
	public String getClaveCliente() {
		return claveCliente;
	}

	/**
	 * 
	 * @param claveCliente
	 */
	public void setClaveCliente(String claveCliente) {
		this.claveCliente = claveCliente;
	}

	/**
	 * 
	 * @return
	 */
	public String getClaveRol() {
		return claveRol;
	}

	/**
	 * 
	 * @param claveRol
	 */
	public void setClaveRol(String claveRol) {
		this.claveRol = claveRol;
	}

	/**
	 * 
	 * @return
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * 
	 * @param usuario
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * 
	 * @return
	 */
	public List<MenuPrincipalVO> getListaMenu() {
		return listaMenu;
	}

	/**
	 * 
	 * @param listaMenu
	 */
	public void setListaMenu(List<MenuPrincipalVO> listaMenu) {
		this.listaMenu = listaMenu;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<MenuPrincipalVO> getListaMenuVertical() {
		return listaMenuVertical;
	}

	/**
	 * 
	 * @param listaMenuVertical
	 */
	public void setListaMenuVertical(List<MenuPrincipalVO> listaMenuVertical) {
		this.listaMenuVertical = listaMenuVertical;
	}

	/*
	 * 
	 * @param menuPrincipalManager
	 *
	public void setMenuPrincipalManager(
			MenuPrincipalManager menuPrincipalManager) {
		this.menuPrincipalManager = menuPrincipalManager;
	}*/
	
	/**
	 * 
	 * @param menuPrincipalManagerJdbcTemplate
	 */
	public void setMenuPrincipalManagerJdbcTemplate(
			MenuPrincipalManager menuPrincipalManagerJdbcTemplate) {
		this.menuPrincipalManagerJdbcTemplate = menuPrincipalManagerJdbcTemplate;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * 
	 * @param success
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * Metodo implementado (sin uso)
	 */
	public void prepare() throws Exception {
	}
}
