package mx.com.aon.portal.web;

import java.util.ArrayList;
import java.util.List;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.PerfilVO;
import mx.com.aon.portal.model.PortalVO;
import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal.service.PaginaPrincipalManager;
import mx.com.gseguros.exception.ApplicationException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Clase que carga la configuracion asignada para cada pantalla.
 * 
 * @author sergio.ramirez
 * 
 */
public class PaginaPrincipalAction extends PrincipalCoreAction {

	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = -9215785716722178049L;
	/*
	 * CDTIPO que representa el tipo 'ALERTA'
	 */
	private static final String CODIGO_TIPO_ALERTA_PANTALLA = "9";
	
	private PaginaPrincipalManager paginaPrincipalManager;
	private boolean success;
	private List<PortalVO> componentes;

	private String rolCliente;
	private String nombreCliente;
	private String cadenaFinal;
	private String rutaPubImage;

	public void limpiaContenido() throws Exception {
		/*
		 * Si el usuario cambia de rol y cliente, se eliminan los contenidos para la nueva pagina
		 * principal de acuerdo al nuevo rol y cliente.
		 * 		  
		 */
		String[] contenidos = {"CONTENIDO_TOP", "CONTENIDO_TOP_IMAGE", "CONTENIDO_NAV", "CONTENIDO_NAV_IMAGE",
								"CONTENIDO_TOPLEFT", "CONTENIDO_TOPLEFT_IMAGE", "CONTENIDO_TOPCENTER", 
								"CONTENIDO_TOPCENTER_IMAGE", "CONTENIDO_TOPRIGHT", "CONTENIDO_TOPRIGHT_IMAGE",
								"CONTENIDO_LEFT_1", "CONTENIDO_LEFT_1_IMAGE", "CONTENIDO_LEFT_1_FILE",
								"CONTENIDO_LEFT_2", "CONTENIDO_LEFT_2_IMAGE", "CONTENIDO_LEFT_2_FILE",
								"CONTENIDO_LEFT_3", "CONTENIDO_LEFT_3_IMAGE", "CONTENIDO_LEFT_3_FILE",
								"CONTENIDO_LEFT_4", "CONTENIDO_LEFT_4_IMAGE", "CONTENIDO_LEFT_4_FILE",
								"CONTENIDO_LEFT_5", "CONTENIDO_LEFT_5_IMAGE", "CONTENIDO_LEFT_5_FILE",
								"CONTENIDO_RIGHT_1", "CONTENIDO_RIGHT_1_IMAGE", "CONTENIDO_RIGHT_1_FILE",
								"CONTENIDO_RIGHT_2", "CONTENIDO_RIGHT_2_IMAGE", "CONTENIDO_RIGHT_2_FILE",
								"CONTENIDO_RIGHT_3", "CONTENIDO_RIGHT_3_IMAGE", "CONTENIDO_RIGHT_3_FILE",
								"CONTENIDO_RIGHT_4", "CONTENIDO_RIGHT_4_IMAGE", "CONTENIDO_RIGHT_4_FILE",
								"CONTENIDO_RIGHT_5", "CONTENIDO_RIGHT_5_IMAGE", "CONTENIDO_RIGHT_5_FILE",
								"CONTENIDO_MAIN", "CONTENIDO_MAIN_IMAGE", "CONTENIDO_NEWS", "CONTENIDO_NEWS_IMAGE",
								"CONTENIDO_NEWS_FILE", "CONTENIDO_KNEWTHAT", "CONTENIDO_KNEWTHAT_IMAGE",
								"CONTENIDO_KNEWTHAT_FILE", "CONTENIDO_MAINDOWN", "CONTENIDO_MAINDOWN_IMAGE",
								"CONTENIDO_OTHERLEFT", "CONTENIDO_OTHERLEFT_IMAGE", "CONTENIDO_OTHERLEFT_FILE",
								"CONTENIDO_OTHERRIGHT", "CONTENIDO_OTHERRIGHT_IMAGE", "CONTENIDO_OTHERRIGHT_FILE",
								"CONTENIDO_OTHERS", "CONTENIDO_OTHERS_IMAGE", "CONTENIDO_FOOTERLEFT", "CONTENIDO_FOOTERLEFT_IMAGE",
								"CONTENIDO_FOOTERCENTER", "CONTENIDO_FOOTERCENTER_IMAGE",
								"CONTENIDO_FOOTERRIGHT", "CONTENIDO_FOOTERRIGHT_IMAGE"};

		for (int i = 0; i < contenidos.length; i++){
			if (session.get(contenidos[i])!=null)
				session.remove(contenidos[i]);
		}
		
		session.remove("SECCIONES_ALERTAS_PANTALLA");
		
	/*
	 * Termina eliminacion de contenidos
	 * 
	 */
		
	}
	                                                
	
	
	public String execute() throws Exception {
		return INPUT;
	}

	/**
	 * Metodo que carga la lista antes que cualquier otra cosa para subirlas a
	 * session y cargar los componentes.
	 * 
	 * @throws ApplicationException
	 */
	public void prepare() throws Exception {
		limpiaContenido();
		UserVO user = (UserVO) session.get("USUARIO");
		rolCliente = user.getRolActivo().getClave();
		nombreCliente = user.getEmpresa().getElementoId();
			logger.debug("((((((((((Estoy en prepare()");
			logger.debug("((((((((((Esto es CONFIGURACIONES_EXISTENTES " + session.get("CONFIGURACIONES_EXISTENTES"));
			session.remove("CONFIGURACIONES_EXISTENTES");
			logger.debug("((((((((((Ahora esto es CONFIGURACIONES_EXISTENTES " + session.get("CONFIGURACIONES_EXISTENTES"));
		componentes = paginaPrincipalManager.getConfiguracionInicial(rolCliente, nombreCliente);
		session.put("CONFIGURACIONES_EXISTENTES", componentes);

		
	}
	
	// TODO Cambiar este metodo para que carge de alguna variable o constante en archivos .properties, XML o BD.
	public String obtenCadena(String cadena) throws Exception{
		if(cadena.contains("AON")){
		String[] cadenaMetodo =  cadena.split("AON");
	    cadenaFinal = "\\AON"+cadenaMetodo[1];
	    
	    
	    logger.debug("cadenaFinal:"+ cadenaFinal);
		}else if(cadena.contains("catweb-configuracion")){
			String [] cadenaMetodoDos = cadena.split("catweb-configuracion");
			cadenaFinal = "\\catweb-configuracion"+cadenaMetodoDos[1];
			logger.debug("cadenaFinal:"+ cadenaFinal);
		}else if( StringUtils.isNotBlank(cadena) ){
			//ruta temporal mapeada a ruta http
			rutaPubImage = "/acw/images/config_pag/";
			cadenaFinal = rutaPubImage + cadena;
			logger.debug("cadenaFinal=" +  cadenaFinal);
		}else{		
			cadenaFinal=null;
		}
		
		return cadenaFinal;

	}

	
	
	@SuppressWarnings("unchecked")
	public String obtenPaginaPrincipal() throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Entrando al metodo getConfiguracionPagina");
		}
		if (session.get("CONFIGURACIONES_EXISTENTES") != null) {
			logger.debug("<<<< ENTRE A IF DE CONFIGURACIONES_EXISTENTES");
			
			List<PortalVO> lista = (List<PortalVO>) session.get("CONFIGURACIONES_EXISTENTES");
			logger.debug("entro en la lista de CONFIGURACIONES_EXISTENTES");
			logger.debug("<<<<<<< Esta es mi configuracion existente: " + session.get("CONFIGURACIONES_EXISTENTES"));
			//logger.debug("lista=" + lista);
			
			UserVO user = (UserVO) session.get("USUARIO");
			
			String email=paginaPrincipalManager.getUserEmail(user.getUser());
			
			if(email != null){
				user.setEmail(email);
		        logger.debug("Email del usuario>> "+user.getEmail());	
			}else {
		        logger.error("ERROR no se pudo obtener el email del usuario>> "+user.getUser());
			}
			
			if (lista.size() == 0){
				limpiaContenido();
				logger.debug("((((((((((Estoy en lista.size() == 0");
				logger.debug("((((((((((Esto es CONFIGURACIONES_EXISTENTES " + session.get("CONFIGURACIONES_EXISTENTES"));
				session.remove("CONFIGURACIONES_EXISTENTES");
				logger.debug("((((((((((Ahora esto es CONFIGURACIONES_EXISTENTES " + session.get("CONFIGURACIONES_EXISTENTES"));
				
				PerfilVO perfil = new PerfilVO();
				user.setFuentesPerfil(perfil);
	
				
			}
			session.put("USUARIO", user);
			//Lista que almacenara las secciones de la pagina que van a mostrar alertas en PANTALLA
			List<String> seccionesAlertasPantallaList = new ArrayList<String>(0);
			
			
			for (PortalVO portalVO : lista) {
				if (StringUtils.isNotBlank(portalVO.getClaveSeccion())) {
					switch (Integer.parseInt(portalVO.getClaveSeccion())) {
					
					case 1:
						if (StringUtils.isNotBlank(portalVO.getEspecificacion())) {
							session.put("CONTENIDO_TOP", StringEscapeUtils.unescapeHtml4(portalVO.getEspecificacion()));
						
						} else if (StringUtils.isNotBlank(portalVO.getDescripcionArchivo())) {
														
							logger.debug("contenido-top:"+portalVO.getDescripcionArchivo());
							session.put("CONTENIDO_TOP_IMAGE", obtenCadena(portalVO.getDescripcionArchivo()));
		
						}
						break;
					case 2:
						if (StringUtils.isNotBlank(portalVO.getEspecificacion())) {
							session.put("CONTENIDO_NAV", StringEscapeUtils.unescapeHtml4(portalVO.getEspecificacion()));
						} else if (StringUtils.isNotBlank(portalVO.getDescripcionArchivo())) {
														
							logger.debug("contenido-nav:"+portalVO.getDescripcionArchivo());
							session.put("CONTENIDO_NAV_IMAGE", obtenCadena(portalVO.getDescripcionArchivo()));
						}
						break;
					case 3:
						if (StringUtils.isNotBlank(portalVO.getEspecificacion())) {
							session.put("CONTENIDO_TOPLEFT", StringEscapeUtils.unescapeHtml4(portalVO.getEspecificacion()));
						} else if (StringUtils.isNotBlank(portalVO.getDescripcionArchivo())) {
														
							logger.debug("contenido-topLeft"+portalVO.getDescripcionArchivo());
							session.put("CONTENIDO_TOPLEFT_IMAGE", obtenCadena(portalVO.getDescripcionArchivo()));
						}
						
						//Si es tipo ALERTA, se agrega el nombre del div en donde se mostraran
						if( esAlertaEnPantalla(portalVO.getClaveTipo()) ){ seccionesAlertasPantallaList.add("top_lefta"); }
						logger.debug("estoy en switch caso 3");
						break;
					case 4:
						if (StringUtils.isNotBlank(portalVO.getEspecificacion())) {
							session.put("CONTENIDO_TOPCENTER",StringEscapeUtils.unescapeHtml4(portalVO.getEspecificacion()));
						} else if (StringUtils.isNotBlank(portalVO.getDescripcionArchivo())) {
													
							logger.debug("contenido-topCenter"+portalVO.getDescripcionArchivo());
							session.put("CONTENIDO_TOPCENTER_IMAGE",obtenCadena(portalVO.getDescripcionArchivo()));
						}

						//Si es tipo ALERTA, se agrega el nombre del div en donde se mostraran
						if( esAlertaEnPantalla(portalVO.getClaveTipo()) ){ seccionesAlertasPantallaList.add("top_centera"); }
						break;
					case 5:
						if (StringUtils.isNotBlank(portalVO.getEspecificacion())) {
							session.put("CONTENIDO_TOPRIGHT",StringEscapeUtils.unescapeHtml4(portalVO.getEspecificacion()));
						} else if (StringUtils.isNotBlank(portalVO.getDescripcionArchivo())) {
							
							logger.debug("contenido-topRight"+portalVO.getDescripcionArchivo());
							session.put("CONTENIDO_TOPRIGHT_IMAGE", obtenCadena(portalVO.getDescripcionArchivo()));
						}
						
						//Si es tipo ALERTA, se agrega el nombre del div en donde se mostraran
						if( esAlertaEnPantalla(portalVO.getClaveTipo()) ){seccionesAlertasPantallaList.add("top_righta"); }
						break;
					case 6:

						if (StringUtils.isNotBlank(portalVO.getEspecificacion())) {
							session.put("CONTENIDO_LEFT_1", StringEscapeUtils.unescapeHtml4(portalVO.getEspecificacion()));
						}else if (StringUtils.isNotBlank(portalVO.getDescripcionArchivo())) {							
							logger.debug("contenido-left-1"+portalVO.getDescripcionArchivo());
							session.put("CONTENIDO_LEFT_1_IMAGE", obtenCadena(portalVO.getDescripcionArchivo()));						
						}if(StringUtils.isNotBlank(portalVO.getOtroContenido())){
							logger.debug("contenido-left-1-otro"+portalVO.getOtroContenido());
							session.put("CONTENIDO_LEFT_1_FILE", obtenCadena(portalVO.getOtroContenido()));
						}

						//Si es tipo ALERTA, se agrega el nombre del div en donde se mostraran
						if( esAlertaEnPantalla(portalVO.getClaveTipo()) ){ seccionesAlertasPantallaList.add("left_1"); }
						break;
					case 7:
						if (StringUtils.isNotBlank(portalVO.getEspecificacion())) {
							session.put("CONTENIDO_LEFT_2", StringEscapeUtils.unescapeHtml4(portalVO.getEspecificacion()));
						}else if (StringUtils.isNotBlank(portalVO.getDescripcionArchivo())) {
							
							logger.debug("contenido-left-2"+portalVO.getDescripcionArchivo());
							session.put("CONTENIDO_LEFT_2_IMAGE",obtenCadena(portalVO.getDescripcionArchivo()));
						}if(StringUtils.isNotBlank(portalVO.getOtroContenido())){
							logger.debug("contenido-left-2-otro"+portalVO.getOtroContenido());
							session.put("CONTENIDO_LEFT_2_FILE", obtenCadena(portalVO.getOtroContenido()));
						}
						
						//Si es tipo ALERTA, se agrega el nombre del div en donde se mostraran
						if( esAlertaEnPantalla(portalVO.getClaveTipo()) ){ seccionesAlertasPantallaList.add("left_2"); }
						break;
					case 8:
						if (StringUtils.isNotBlank(portalVO.getEspecificacion())) {
							session.put("CONTENIDO_LEFT_3", StringEscapeUtils.unescapeHtml4(portalVO.getEspecificacion()));
						} else if (StringUtils.isNotBlank(portalVO.getDescripcionArchivo())) {
							
							logger.debug("contenido-left-3"+portalVO.getDescripcionArchivo());
							session.put("CONTENIDO_LEFT_3_IMAGE",obtenCadena(portalVO.getDescripcionArchivo()));
						}if(StringUtils.isNotBlank(portalVO.getOtroContenido())){
							logger.debug("contenido-left-3-otro"+portalVO.getOtroContenido());
							session.put("CONTENIDO_LEFT_3_FILE", obtenCadena(portalVO.getOtroContenido()));
						}
						
						//Si es tipo ALERTA, se agrega el nombre del div en donde se mostraran
						if( esAlertaEnPantalla(portalVO.getClaveTipo()) ){ seccionesAlertasPantallaList.add("left_3"); }
						break;
					case 9:

						if (StringUtils.isNotBlank(portalVO.getEspecificacion())) {
							session.put("CONTENIDO_LEFT_4", StringEscapeUtils.unescapeHtml4(portalVO.getEspecificacion()));
						    logger.debug("contenido_left_4"+ portalVO.getEspecificacion() );
						} else if (StringUtils.isNotBlank(portalVO.getDescripcionArchivo())) {
							
							logger.debug("contenido-left-4_image"+portalVO.getDescripcionArchivo());
							session.put("CONTENIDO_LEFT_4_IMAGE",obtenCadena(portalVO.getDescripcionArchivo()));
						}if(StringUtils.isNotBlank(portalVO.getOtroContenido())){
							logger.debug("contenido-left-4-otro"+portalVO.getOtroContenido());
							session.put("CONTENIDO_LEFT_4_FILE", obtenCadena(portalVO.getOtroContenido()));
						}

						//Si es tipo ALERTA, se agrega el nombre del div en donde se mostraran
						if( esAlertaEnPantalla(portalVO.getClaveTipo()) ){ seccionesAlertasPantallaList.add("left_4"); }
						break;
					case 10:
						if (StringUtils.isNotBlank(portalVO.getEspecificacion())) {
							session.put("CONTENIDO_LEFT_5", StringEscapeUtils.unescapeHtml4(portalVO.getEspecificacion()));
						} else if (StringUtils.isNotBlank(portalVO.getDescripcionArchivo())) {
							
							logger.debug("contenido-left-5"+portalVO.getDescripcionArchivo());
							session.put("CONTENIDO_LEFT_5_IMAGE",obtenCadena(portalVO.getDescripcionArchivo()));
						}if(StringUtils.isNotBlank(portalVO.getOtroContenido())){
							logger.debug("contenido-left-5-otro"+portalVO.getOtroContenido());
							session.put("CONTENIDO_LEFT_5_FILE", obtenCadena(portalVO.getOtroContenido()));
						}
						
						//Si es tipo ALERTA, se agrega el nombre del div en donde se mostraran
						if( esAlertaEnPantalla(portalVO.getClaveTipo()) ){ seccionesAlertasPantallaList.add("left_5"); }
						break;
					case 11:
						if (StringUtils.isNotBlank(portalVO.getEspecificacion())) {
							session.put("CONTENIDO_RIGHT_1", StringEscapeUtils.unescapeHtml4(portalVO.getEspecificacion()));
						} else if (StringUtils.isNotBlank(portalVO.getDescripcionArchivo())) {
							
							logger.debug("contenido-right-1"+portalVO.getDescripcionArchivo());
							session.put("CONTENIDO_RIGHT_1_IMAGE",obtenCadena(portalVO.getDescripcionArchivo()));
						}if(StringUtils.isNotBlank(portalVO.getOtroContenido())){
							
							logger.debug("contenido-right-1-otro"+portalVO.getOtroContenido());
							session.put("CONTENIDO_RIGHT_1_FILE", obtenCadena(portalVO.getOtroContenido()));
						}
						
						//Si es tipo ALERTA, se agrega el nombre del div en donde se mostraran
						if( esAlertaEnPantalla(portalVO.getClaveTipo()) ){ seccionesAlertasPantallaList.add("right_1"); }
						break;
					case 12:
						if (StringUtils.isNotBlank(portalVO.getEspecificacion())) {
							session.put("CONTENIDO_RIGHT_2", StringEscapeUtils.unescapeHtml4(portalVO.getEspecificacion()));
						} else if (StringUtils.isNotBlank(portalVO.getDescripcionArchivo())) {
							
							logger.debug("contenido-right-2"+portalVO.getDescripcionArchivo());
							session.put("CONTENIDO_RIGHT_2_IMAGE",obtenCadena(portalVO.getDescripcionArchivo()));
						}if(StringUtils.isNotBlank(portalVO.getOtroContenido())){
							
							logger.debug("contenido-right-2-otro"+portalVO.getOtroContenido());
							session.put("CONTENIDO_RIGHT_2_FILE", obtenCadena(portalVO.getOtroContenido()));
						}

						//Si es tipo ALERTA, se agrega el nombre del div en donde se mostraran
						if( esAlertaEnPantalla(portalVO.getClaveTipo()) ){ seccionesAlertasPantallaList.add("right_2"); }
						break;
					case 13:

						if (StringUtils.isNotBlank(portalVO.getEspecificacion())) {
							session.put("CONTENIDO_RIGHT_3", StringEscapeUtils.unescapeHtml4(portalVO.getEspecificacion()));
						} else if (StringUtils.isNotBlank(portalVO.getDescripcionArchivo())) {
							
							logger.debug("contenido-right-3"+portalVO.getDescripcionArchivo());
							session.put("CONTENIDO_RIGHT_3_IMAGE",obtenCadena(portalVO.getDescripcionArchivo()));
						}if(StringUtils.isNotBlank(portalVO.getOtroContenido())){
							
							logger.debug("contenido-right-3-otro"+portalVO.getOtroContenido());
							session.put("CONTENIDO_RIGHT_3_FILE", obtenCadena(portalVO.getOtroContenido()));
						}

						//Si es tipo ALERTA, se agrega el nombre del div en donde se mostraran
						if( esAlertaEnPantalla(portalVO.getClaveTipo()) ){ seccionesAlertasPantallaList.add("right_3"); }
						break;
					case 14:

						if (StringUtils.isNotBlank(portalVO.getEspecificacion())) {
							session.put("CONTENIDO_RIGHT_4", StringEscapeUtils.unescapeHtml4(portalVO.getEspecificacion()));
						} else if (StringUtils.isNotBlank(portalVO.getDescripcionArchivo())) {
							
							logger.debug("contenido-right-4"+portalVO.getDescripcionArchivo());
							session.put("CONTENIDO_RIGHT_4_IMAGE",obtenCadena(portalVO.getDescripcionArchivo()));
							
						}if(StringUtils.isNotBlank(portalVO.getOtroContenido())){
							
							logger.debug("contenido-right-4-otro"+portalVO.getOtroContenido());
							session.put("CONTENIDO_RIGHT_4_FILE", obtenCadena(portalVO.getOtroContenido()));
						}

						//Si es tipo ALERTA, se agrega el nombre del div en donde se mostraran
						if( esAlertaEnPantalla(portalVO.getClaveTipo()) ){ seccionesAlertasPantallaList.add("right_4"); }
						break;
					case 15:

						if (StringUtils.isNotBlank(portalVO.getEspecificacion())) {
							session.put("CONTENIDO_RIGHT_5", StringEscapeUtils.unescapeHtml4(portalVO.getEspecificacion()));
						} else if (StringUtils.isNotBlank(portalVO.getDescripcionArchivo())) {
							
							logger.debug("contenido-right-5"+portalVO.getDescripcionArchivo());
							session.put("CONTENIDO_RIGHT_5_IMAGE", obtenCadena(portalVO.getDescripcionArchivo()));
						}if(StringUtils.isNotBlank(portalVO.getOtroContenido())){
							
							logger.debug("contenido-right-5-otro"+portalVO.getOtroContenido());
							session.put("CONTENIDO_RIGHT_5_FILE", obtenCadena(portalVO.getOtroContenido()));
						}

						//Si es tipo ALERTA, se agrega el nombre del div en donde se mostraran
						if( esAlertaEnPantalla(portalVO.getClaveTipo()) ){ seccionesAlertasPantallaList.add("right_5"); }
						
						break;
					case 16:

						if (StringUtils.isNotBlank(portalVO.getEspecificacion())) {
							session.put("CONTENIDO_MAIN", StringEscapeUtils.unescapeHtml4(portalVO.getEspecificacion()));
						} else if (StringUtils.isNotBlank(portalVO.getDescripcionArchivo())) {
							
							logger.debug("contenido-main"+portalVO.getContenido());
							session.put("CONTENIDO_MAIN_IMAGE",obtenCadena(portalVO.getContenido()));
						}

						//Si es tipo ALERTA, se agrega el nombre del div en donde se mostraran
						if( esAlertaEnPantalla(portalVO.getClaveTipo()) ){ seccionesAlertasPantallaList.add("main"); }
						break;
					case 17:

						if (StringUtils.isNotBlank(portalVO.getEspecificacion())) {
							session.put("CONTENIDO_NEWS", StringEscapeUtils.unescapeHtml4(portalVO.getEspecificacion()));
						}else if (StringUtils.isNotBlank(portalVO.getDescripcionArchivo())) {
							
							logger.debug("contenido-news"+portalVO.getDescripcionArchivo());
							session.put("CONTENIDO_NEWS_IMAGE",obtenCadena(portalVO.getDescripcionArchivo()));
						}if(StringUtils.isNotBlank(portalVO.getOtroContenido())){
							
							logger.debug("contenido-news-otro"+portalVO.getOtroContenido());
							session.put("CONTENIDO_NEWS_FILE", obtenCadena(portalVO.getOtroContenido()));
						}

						//Si es tipo ALERTA, se agrega el nombre del div en donde se mostraran
						if( esAlertaEnPantalla(portalVO.getClaveTipo()) ){ seccionesAlertasPantallaList.add("news"); }
						break;
					case 18:

						if (StringUtils.isNotBlank(portalVO.getEspecificacion())) {
							session.put("CONTENIDO_KNEWTHAT",StringEscapeUtils.unescapeHtml4(portalVO.getEspecificacion()));
						}else if (StringUtils.isNotBlank(portalVO.getDescripcionArchivo())) {
							
							logger.debug("contenido-newThat"+portalVO.getDescripcionArchivo());
							session.put("CONTENIDO_KNEWTHAT_IMAGE",obtenCadena(portalVO.getDescripcionArchivo()));
						}if(StringUtils.isNotBlank(portalVO.getOtroContenido())){
							
							logger.debug("contenido-newThat-otro:"+portalVO.getOtroContenido());
							session.put("CONTENIDO_KNEWTHAT_FILE", obtenCadena(portalVO.getOtroContenido()));
						}

						//Si es tipo ALERTA, se agrega el nombre del div en donde se mostraran
						if( esAlertaEnPantalla(portalVO.getClaveTipo()) ){ seccionesAlertasPantallaList.add("knewthat"); }
						
						break;
					case 19:

						if (StringUtils.isNotBlank(portalVO.getEspecificacion())) {
							session.put("CONTENIDO_MAINDOWN",StringEscapeUtils.unescapeHtml4(portalVO.getEspecificacion()));
						} else if (StringUtils.isNotBlank(portalVO.getDescripcionArchivo())) {
							
							logger.debug("contenido-mainDown"+portalVO.getDescripcionArchivo());
							session.put("CONTENIDO_MAINDOWN_IMAGE",obtenCadena(portalVO.getDescripcionArchivo()));
						}

						//Si es tipo ALERTA, se agrega el nombre del div en donde se mostraran
						if( esAlertaEnPantalla(portalVO.getClaveTipo()) ){ seccionesAlertasPantallaList.add("mainDown"); }
						break;
					case 20:

						if (StringUtils.isNotBlank(portalVO.getEspecificacion())) {
							session.put("CONTENIDO_OTHERLEFT",StringEscapeUtils.unescapeHtml4(portalVO.getEspecificacion()));
						} else if (StringUtils.isNotBlank(portalVO.getDescripcionArchivo())) {
							
							logger.debug("contenido-otherLeft"+portalVO.getDescripcionArchivo());
							session.put("CONTENIDO_OTHERLEFT_IMAGE",obtenCadena(portalVO.getDescripcionArchivo()));
						}if(StringUtils.isNotBlank(portalVO.getOtroContenido())){
							
							logger.debug("contenido-otherLeft-otro:"+portalVO.getOtroContenido());
							session.put("CONTENIDO_OTHERLEFT_FILE", obtenCadena(portalVO.getOtroContenido()));
						}

						//Si es tipo ALERTA, se agrega el nombre del div en donde se mostraran
						if( esAlertaEnPantalla(portalVO.getClaveTipo()) ){ seccionesAlertasPantallaList.add("otherLeft"); }
						break;
					case 21:

						if (StringUtils.isNotBlank(portalVO.getEspecificacion())) {
							session.put("CONTENIDO_OTHERRIGHT",StringEscapeUtils.unescapeHtml4(portalVO.getEspecificacion()));
						} else if (StringUtils.isNotBlank(portalVO.getDescripcionArchivo())) {
							
							logger.debug("contenido-otherRight"+portalVO.getDescripcionArchivo());
							session.put("CONTENIDO_OTHERRIGHT_IMAGE",obtenCadena(portalVO.getDescripcionArchivo()));
							
						} if(StringUtils.isNotBlank(portalVO.getOtroContenido())){							
							logger.debug("contenido-otherRight-otro:"+portalVO.getOtroContenido());
							session.put("CONTENIDO_OTHERRIGHT_FILE", obtenCadena(portalVO.getOtroContenido()));
						}

						//Si es tipo ALERTA, se agrega el nombre del div en donde se mostraran
						if( esAlertaEnPantalla(portalVO.getClaveTipo()) ){ seccionesAlertasPantallaList.add("otherRight"); }
						break;
					case 22:

						if (StringUtils.isNotBlank(portalVO.getEspecificacion())) {
							session.put("CONTENIDO_OTHERS", StringEscapeUtils.unescapeHtml4(portalVO.getEspecificacion()));
						} else if (StringUtils.isNotBlank(portalVO.getDescripcionArchivo())) {
							
							logger.debug("contenido-others"+portalVO.getDescripcionArchivo());
							session.put("CONTENIDO_OTHERS_IMAGE",obtenCadena(portalVO.getDescripcionArchivo()));
						}

						//Si es tipo ALERTA, se agrega el nombre del div en donde se mostraran
						if( esAlertaEnPantalla(portalVO.getClaveTipo()) ){ seccionesAlertasPantallaList.add("others"); }
						break;
					case 23:

						if (StringUtils.isNotBlank(portalVO.getEspecificacion())) {
							session.put("CONTENIDO_FOOTERLEFT",StringEscapeUtils.unescapeHtml4(portalVO.getEspecificacion()));
						} else if (StringUtils.isNotBlank(portalVO.getDescripcionArchivo())) {
							
							logger.debug("contenido-footerLeft"+portalVO.getDescripcionArchivo());
							session.put("CONTENIDO_FOOTERLEFT_IMAGE",obtenCadena(portalVO.getDescripcionArchivo()));
						}

						//Si es tipo ALERTA, se agrega el nombre del div en donde se mostraran
						if( esAlertaEnPantalla(portalVO.getClaveTipo()) ){ seccionesAlertasPantallaList.add("footer_left"); }
						break;
					case 24:

						if (StringUtils.isNotBlank(portalVO.getEspecificacion())) {
							session.put("CONTENIDO_FOOTERCENTER",StringEscapeUtils.unescapeHtml4(portalVO.getEspecificacion()));
						} else if (StringUtils.isNotBlank(portalVO.getDescripcionArchivo())) {
							
							logger.debug("contenido-footerCenter"+portalVO.getDescripcionArchivo());
							session.put("CONTENIDO_FOOTERCENTER_IMAGE", obtenCadena(portalVO.getDescripcionArchivo()));
						}

						//Si es tipo ALERTA, se agrega el nombre del div en donde se mostraran
						if( esAlertaEnPantalla(portalVO.getClaveTipo()) ){ seccionesAlertasPantallaList.add("footer_center"); }
						break;
					case 25:

						if (StringUtils.isNotBlank(portalVO.getEspecificacion())) {
							session.put("CONTENIDO_FOOTERRIGHT",StringEscapeUtils.unescapeHtml4(portalVO.getEspecificacion()));
						} else if (StringUtils.isNotBlank(portalVO.getDescripcionArchivo())) {
							
							logger.debug("contenido-footerRight"+portalVO.getDescripcionArchivo());
							session.put("CONTENIDO_FOOTERRIGHT_IMAGE",obtenCadena(portalVO.getDescripcionArchivo()));
							
						}

						//Si es tipo ALERTA, se agrega el nombre del div en donde se mostraran
						if( esAlertaEnPantalla(portalVO.getClaveTipo()) ){ seccionesAlertasPantallaList.add("footer_right"); }
						break;
					default:
						logger.debug("La lista no trae nada y entro a default");
						break;
					}
				}
			}
			
			//Guardar en sesion secciones con alertas en pantalla:
			if(!seccionesAlertasPantallaList.isEmpty()){
				session.put("SECCIONES_ALERTAS_PANTALLA", seccionesAlertasPantallaList);
				logger.debug("seccionesAlertasPantallaList=" + seccionesAlertasPantallaList);
				logger.debug("seccionesAlertasPantallaList size=" + seccionesAlertasPantallaList.size());
			}
		}
		else
		{
			logger.debug("<<<< CONFIGURACIONES_EXISTENTES ES NULL");			
		}
		
		return SUCCESS; 
	}
	
	/**
	 * Regresa true si claveTipo corresponde al codigo 'ALERTA', sino regresa false
	 * @param claveTipo
	 * @return
	 */
	private boolean esAlertaEnPantalla(String claveTipo){
		if ( StringUtils.isNotBlank(claveTipo) && StringUtils.equals(claveTipo, CODIGO_TIPO_ALERTA_PANTALLA) ){
			return true;
		}else{
			return false;
		}
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
	 * 
	 * @param paginaPrincipalManager
	 */
	public void setPaginaPrincipalManager(
			PaginaPrincipalManager paginaPrincipalManager) {
		this.paginaPrincipalManager = paginaPrincipalManager;
	}
	/**
	 * 
	 * @return
	 */
	public List<PortalVO> getComponentes() {
		return componentes;
	}

	/**
	 * 
	 * @param componentes
	 */
	public void setComponentes(List<PortalVO> componentes) {
		this.componentes = componentes;
	}

	/**
	 * 
	 * @return
	 */
	public String getRolCliente() {
		return rolCliente;
	}

	/**
	 * 
	 * @param rolCliente
	 */
	public void setRolCliente(String rolCliente) {
		this.rolCliente = rolCliente;
	}

	/**
	 * 
	 * @return
	 */
	public String getNombreCliente() {
		return nombreCliente;
	}

	/**
	 * 
	 * @param nombreCliente
	 */
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	/**
	 * 
	 * @return
	 */
	public String getCadenaFinal() {
		return cadenaFinal;
	}
	/**
	 * 
	 * @param cadenaFinal
	 */
	public void setCadenaFinal(String cadenaFinal) {
		this.cadenaFinal = cadenaFinal;
	}

}
