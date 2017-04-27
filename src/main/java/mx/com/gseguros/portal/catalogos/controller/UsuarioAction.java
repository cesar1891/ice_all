package mx.com.gseguros.portal.catalogos.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal.service.LoginManager;
import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.general.model.UsuarioVO;
import mx.com.gseguros.portal.general.service.PantallasManager;
import mx.com.gseguros.portal.general.service.UsuarioManager;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.utils.Utils;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.JSONUtil;

public class UsuarioAction extends PrincipalCoreAction {

	private static final long serialVersionUID = -7264510862184393230L;
	
	private Logger logger = Logger.getLogger(UsuarioAction.class);
	
	private boolean success;
	
	private String errorMessage;
	
	private UsuarioManager usuarioManager;
	private PantallasManager pantallasManager;
	private LoginManager loginManager;
	
	private Map<String,String> params;
	
	private Item fields;
	private Item columns;
	private Item items;
	
	private List<UsuarioVO> usuarios;
	private List<GenericVO> lista;
	
	private List<Map<String, String>> loadList;
    private List<Map<String, String>> saveList;
    
    private String user;
    private String password;
    private String newpassword;

	public String cargaPantallaUsuarios() throws Exception{
		try
		{
			
			logger.debug("... generando pantalla para gestion de usuarios");
			UserVO usuario=(UserVO)session.get("USUARIO");
			
			GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			
			gc.generaParcial(pantallasManager.obtenerComponentes(
					null, null, null,
					null, null, usuario.getRolActivo().getClave(),
					"PANTALLA_USUARIOS", "BUSQUEDA", null));
			
			items   = gc.getItems();
			
			gc.generaParcial(pantallasManager.obtenerComponentes(
					null, null, null,
					null, null, usuario.getRolActivo().getClave(),
					"PANTALLA_USUARIOS", "MODEL", null));
			
			fields  = gc.getFields();
			
			gc.generaParcial(pantallasManager.obtenerComponentes(
					null, null, null,
					null, null, usuario.getRolActivo().getClave(),
					"PANTALLA_USUARIOS", "COLUMNMODEL", null));
			
			columns = gc.getColumns();
			
		}
		catch(Exception ex)
		{
			logger.error("Error en pantalla de Usuarios",ex);
		}
		return SUCCESS;
	}
	
	public String creaEditaRolSistema() throws Exception {
		try{
			usuarioManager.creaEditaRolSistema(params);
			success=true;
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			errorMessage = "Error al guardar el Rol.";
		}
		return SUCCESS;
	}
	
	public String guardaUsuario() throws Exception {
    	try{
        	usuarioManager.guardaUsuario(params);
        	
        	/**
        	 *PARA CREAR EN LDAP USER PASS, Como el pass no se esta pidiendo por el momento no se crea hasta que el usuario se loguea por primer vez. 
        	 *
        	if(Constantes.INSERT_MODE.equalsIgnoreCase(params.get("accion"))){
        		boolean existsUser = loginManager.validaUsuarioLDAP(true, params.get("cdusuari"), params.get("password"));
            	
        		if(!existsUser){
        			logger.info("No existe usuario, Insertando usuario "+params.get("cdusuari")+"/"+params.get("password")+" en LDAP");
        			loginManager.insertaUsuarioLDAP(params.get("cdusuari"), params.get("password"));
        			logger.info("Usuario Creado en LDAP, redireccionando a menu de Roles...");
        		}else { 
        			logger.info("Usuario Ya existia en LDAP, Consulte a soporte para modificar Password si es necesario.");
        		}
        	}
        	*/
    		
        	success=true;
    	} catch(Exception e) {
    		logger.error(e.getMessage(), e);
    		errorMessage = "Error al guardar el usuario." + e.getMessage();
    	}
		
    	return SUCCESS;
    }
	
	public String cambiarPasswordUsuarioLDAP() throws Exception {
		try {
			logger.info("... Cambiando Password de Usuario: " + user);
			
			boolean existeUsuario = loginManager.validaUsuarioLDAP(true, user, "dummy");
			if (existeUsuario) {
				//Cambio de Password
				success = loginManager.cambiarPasswordUsuarioLDAP(user,newpassword);
				if (!success) {
					errorMessage = "No se pudo realizar el cambio de contrase&ntilde;a, intente nuevamente";
				}
			} else {
				logger.info("El usuario "+user+" no existe en LDAP.");
				errorMessage = "El usuario no existe en LDAP.";
			}
			return SUCCESS;

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			errorMessage = "Error en el proceso de cambio de Password. Consulte a Soporte T\u00e9cnico.";
			return SUCCESS;
		}
	}
	
	public String activaDesactivaUsuario() throws Exception {
    	try{
        	usuarioManager.cambiaEstatusUsuario(params);
    	} catch(Exception e) {
    		logger.error(e.getMessage(), e);
    		errorMessage = "Error al cambiar el estatus del usuario." + e.getMessage();
    	}
		
    	success=true;
    	return SUCCESS;
    }

	public String eliminaUsuarioLDAP() throws Exception {
		
		logger.debug("Eliminando usuario para reset de password en LDAP, user: " + user);
		try{
			loginManager.eliminarUsuarioLDAP(user);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			errorMessage = "Error al resetear el password del usuario." + e.getMessage();
		}
		
		success=true;
		return SUCCESS;
	}
	
    public String obtieneUsuarios() throws Exception {
    	
    	try{
    		UserVO usuario=(UserVO)session.get("USUARIO");
    		params.put("pv_cdsisrol_i", usuario.getRolActivo().getClave());
    		
    		usuarios = usuarioManager.obtieneUsuarios(params);
    		success=true;
    	} catch(Exception e) {
    		logger.error(e.getMessage(), e);
    		errorMessage = "Error al obtener usuarios. Intente m\u00e1s tarde";
    	}
    	
    	//logger.debug("Resultado de usuarios para la busqueda: "+ usuarios);
    	return SUCCESS;
    }

    public String obtienerRolesPorPrivilegio() throws Exception {
    	
    	try{
    		UserVO usuario=(UserVO)session.get("USUARIO");
    		
    		params = new HashMap<String, String>();
    		params.put("pv_cdsisrol_i", usuario.getRolActivo().getClave());
    		
    		lista = usuarioManager.obtienerRolesPorPrivilegio(params);
    		success=true;
    	} catch(Exception e) {
    		logger.error(e.getMessage(), e);
    		errorMessage = "Error al obtener roles. Intente m\u00e1s tarde";
    	}
    	
    	return SUCCESS;
    }
    
    public String obtieneImpresorasUsuario() throws Exception{
    	
    	try{
    		logger.debug(Utils.log(
    				"\n#######################################",
    				"\n#######################################",
    				"\n###### obtieneImpresorasUsuario ######",
    				"\n###### params=",params,"        ######"
    				));
    		

    		loadList=usuarioManager.obtieneImpresorasUsuario(params.get("pv_cdusuario_i"));
    		
  		
    		
    		success =  true;
    		
    	}catch( Exception e){
    		errorMessage = Utils.manejaExcepcion(e);
       		success =  false;
       		return SUCCESS;
       	}
       	success = true;
       	
       	logger.debug(Utils.log(
				 "\n###### obtieneImpresorasUsuario ######"
				,"\n#########################"
				));
    	
    	
    	return SUCCESS;
    }
    
    
    public String guardaEditaImpresora(){
    	
    	
    	logger.debug(Utils.log(
				"\n##########################################",
				"\n##########################################",
				"\n###### guardaEditaImpresora         ######",
				"\n###### saveList=",saveList,"        ######"
				));
    	
    	
    	try{
    		Utils.validate(saveList,"No se recibierRon datos");
    		
    		for(Map<String, String> imp : saveList){
    			
        		String impresora=imp.get("IMPRESORA");
        		String ip=imp.get("IP");
        		String tipo=imp.get("TIPO");
        		String descripcion=imp.get("DESCRIPCION");
        		Utils.validate(impresora,"No se recibi\u00F3 impresora",
        				ip,"No se recibi\u00F3 ip",
        				tipo,"No se recibi\u00F3 tipo",
        				descripcion,"No se recibi\u00F3 descripcion");
        		
        		errorMessage=usuarioManager.insertaActualizaImpresora(impresora, ip, tipo, descripcion, "S");
        	}
        	
    	}catch( Exception e){
    		errorMessage = Utils.manejaExcepcion(e);
       		success =  false;
       		return SUCCESS;
       	}
    	
    	logger.debug(Utils.log(
				 "\n###### guardaEditaImpresora         ######"
				,"\n##########################################"
				));
    	
    	success =  true;
    	return SUCCESS;
    }
    
    public String habilitaDeshabilitaImpresora(){
    	
    	
    	logger.debug(Utils.log(
				"\n##########################################",
				"\n##########################################",
				"\n###### habilitaDeshabilitaImpresora ######",
				"\n###### saveList=",saveList,"        ######"
				));
    	try{
    		Utils.validate(saveList,"No se recibieron datos");
    		
    		for(Map<String, String> imp : saveList){
    			
        		String pv_habilita=imp.get("enable");
        		String pv_impresora_i=imp.get("IMPRESORA");
        		Utils.validate(pv_habilita,"No se recibi\u00F3 habilita",
        				pv_impresora_i,"No se recibi\u00F3 pv_impresora_i");
        		
        		errorMessage=usuarioManager.habilitaDeshabilitaImpresora(pv_habilita, pv_impresora_i.toLowerCase(), null);
        	}
        	
    	}catch( Exception e){
    		errorMessage = Utils.manejaExcepcion(e);
       		success =  false;
       		return SUCCESS;
       	}
    	
    	
    	
    	
    	logger.debug(Utils.log(
				 "\n###### habilitaDeshabilitaImpresora ######"
				,"\n##########################################"
				));
    	success =  true;
    	return SUCCESS;
    }
    
    public String guardaImpresorasUsuario(){// por el momento no sirve
   		
   		try{
   			
   			logger.debug(Utils.log(
    				"\n#######################################",
    				"\n#######################################",
    				"\n###### guardaImpresorasUsuario ######",
    				"\n###### params=",params,"        ######",
    				"\n###### saveList=",saveList,"        ######"
    				));
    		
    		Utils.validate(params , "No se recibieron par\u00E1metros");
    		String cdusuario=params.get("CDUSUARIO");
    		for(Map<String, String> imp : saveList){
    			
        		String impresora=imp.get("IMPRESORA");
        		String ip=imp.get("IP");
        		String tipo=imp.get("TIPO");
        		String descripcion=imp.get("DESCRIPCION");
        		String disponible=imp.get("DISPONIBLE");
        		String alta=imp.get("ALTA");
        		
        		
        		
            	Utils.validate(
            			ip,"No se recib\u00F3 ip",
            			tipo,"No se recib\u00F3 tipo",
            			descripcion,"No se recibi\u00F3 descripcion",
            			disponible,"No se recibi\u00F3 disponible",
            			alta,"No se recibi\u00F3 alta",
            			impresora,"No se recibi\u00F3 impresora");
            	usuarioManager.guardaImpresorasUsuario(cdusuario,
            											impresora,
            											ip,
            											tipo,
            											descripcion,
            											disponible,
            											alta);
    		}
    		
        		
    		
   			
   			
   		}catch( Exception e){
       		logger.error("Error en obtieneRolesUsuario",e);
       		success =  false;
       		return SUCCESS;
       	}
       	
       	success = true;
       	
    	logger.debug(Utils.log(
				 "\n###### guardaImpresorasUsuario ######"
				,"\n#########################"
				));
       	return SUCCESS;
    }
    
    
    
    public String obtieneRolesUsuario() throws Exception {
       	try {
       		loadList = usuarioManager.obtieneRolesUsuario(params);
       	}catch( Exception e){
       		logger.error("Error en obtieneRolesUsuario",e);
       		success =  false;
       		return SUCCESS;
       	}
       	success = true;
       	return SUCCESS;
       }
    
       public String guardaRolesUsuario(){
       	
       	try {
       		logger.debug("guardaRolesUsuario SaveList: "+ saveList);
       		usuarioManager.guardaRolesUsuario(params, saveList);
       	}catch( Exception e){
       		logger.error("Error en guardaRolesUsuario",e);
       		success =  false;
       		return SUCCESS;
       	}
       	success = true;
       	return SUCCESS;
       }

       public String obtieneProductosAgente() throws Exception {
    	   try {
    		   loadList = usuarioManager.obtieneProductosAgente(params);
    	   }catch( Exception e){
    		   logger.error("Error en obtieneProductosAgente",e);
    		   success =  false;
    		   return SUCCESS;
    	   }
    	   success = true;
    	   return SUCCESS;
       }
       
       public String guardaProductosAgente(){
    	   
    	   try {
    		   logger.debug("guardaRolesUsuario SaveList: "+ saveList);
    		   usuarioManager.guardaProductosAgente(params, saveList);
    	   }catch( Exception e){
    		   logger.error("Error en guardaProductosAgente",e);
    		   success =  false;
    		   return SUCCESS;
    	   }
    	   success = true;
    	   return SUCCESS;
       }


    // getters and setters
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Map<String,String> getParams() {
		return params;
	}

	public void setParams(Map<String,String> params) {
		this.params = params;
	}
	
	public void setUsuarioManager(UsuarioManager usuarioManager) {
		this.usuarioManager = usuarioManager;
	}

	public void setPantallasManager(PantallasManager pantallasManager) {
		this.pantallasManager = pantallasManager;
	}

	public Item getFields() {
		return fields;
	}

	public void setFields(Item fields) {
		this.fields = fields;
	}

	public Item getColumns() {
		return columns;
	}

	public void setColumns(Item columns) {
		this.columns = columns;
	}

	public Item getItems() {
		return items;
	}

	public void setItems(Item items) {
		this.items = items;
	}

	public List<UsuarioVO> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<UsuarioVO> usuarios) {
		this.usuarios = usuarios;
	}
	
	public String getParamsJson() {
		try {
			return JSONUtil.serialize(params);
		} catch (Exception e) {
			logger.error("Error al generar JSON de params",e);
			return null;
		}
	}

	public List<Map<String, String>> getLoadList() {
		return loadList;
	}

	public void setLoadList(List<Map<String, String>> loadList) {
		this.loadList = loadList;
	}

	public List<Map<String, String>> getSaveList() {
		return saveList;
	}

	public void setSaveList(List<Map<String, String>> saveList) {
		this.saveList = saveList;
	}

	public void setLoginManager(LoginManager loginManager) {
		this.loginManager = loginManager;
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

	public String getNewpassword() {
		return newpassword;
	}

	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}

	public List<GenericVO> getLista() {
		return lista;
	}

	public void setLista(List<GenericVO> lista) {
		this.lista = lista;
	}
}