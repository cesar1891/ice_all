package mx.com.aon.portal.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.BaseObjectVO;
import mx.com.aon.portal.model.EmpresaVO;
import mx.com.aon.portal.model.IsoVO;
import mx.com.aon.portal.model.RamaVO;
import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal.service.NavigationManager;
import mx.com.aon.portal.service.UsuarioManager;
import mx.com.aon.portal.service.principal.PrincipalManager;
import mx.com.gseguros.portal.general.model.RolVO;
import mx.com.gseguros.portal.general.util.RolSistema;
import mx.com.gseguros.utils.Utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

/**
 *
 * @author sergio.ramirez
 *
 */
public class LoadClientesRolesAction extends PrincipalCoreAction {

    private static final long serialVersionUID = -7704147817021446988L;
    private boolean success;
    private String user;
    private NavigationManager navigationManager;
    private mx.com.gseguros.portal.general.service.NavigationManager navigationManagerNuevo;
    private UsuarioManager usuarioManager;
    private transient PrincipalManager principalManagerJdbcTemplate;
    private List<RamaVO> listaRolCliente;
    private List<UserVO> userList;
    private IsoVO iso;
    private String codigoCliente;
    private String codigoRol;
    private boolean codigoValido;
    private int numReg;
    private int registrosEncontrados;
    private String _codigoCliente = null;
    private boolean soloUnRol = false;

    /**
     * Cambia el usuario actual en sesion por uno
     * que tenga el rol seleccionado del �rbol
     * @return success
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public String obtenCodigoTree() throws Exception
    {
        String retorno=SUCCESS;
        logger.debug("Entrada metodo getCodigoTree");
        if (StringUtils.isNotBlank(codigoCliente) && StringUtils.isNotBlank(codigoRol))
        // Si se reciben los parametros del cliente
        {
            if (codigoCliente.trim().equals(codigoRol.trim()))
            // Si el cliente y el rol llegaron iguales desde el cliente
            {
                codigoValido=false;		
            }
            else
            // Buscamos el usuario con ese rol para ese cliente
            {
                //Se carga la lista de roles que se puso en sesion cuando se cargo
                //la vista del arbol con obtenRolesClientes()
                List<UserVO> listaUsuario = (List<UserVO>) session.get("CARGA_USUARIO_COMPLETO");
                
                EmpresaVO empresa = null;
                RolVO rolActivo = new RolVO();
                boolean rolActivado = false;
                for (UserVO userVO : listaUsuario) {
                    empresa = userVO.getEmpresa();
                    for (RolVO rols : userVO.getRoles()) {
                        if (empresa.getElementoId().trim().equals(codigoCliente.trim()) && rols.getClave().trim().equals(codigoRol.trim())) {
                            rolActivo = rols;
                            rolActivado = true;
                            _codigoCliente = userVO.getEmpresa().getElementoId();
                            break;
                        }
                    }
                    if (rolActivado) {
                        if (rolActivo.getClave().equals(RolSistema.AGENTE.getCdsisrol())) {
                            userVO.setCodigoPersona("0");
                        }
                        userVO.setRolActivo(rolActivo);
                        session.put("USUARIO", userVO);
                        logger.debug("session: "+session);
                        String userAgent = ServletActionContext.getRequest().getHeader("User-Agent");
                        try
                        {
                        	navigationManagerNuevo.guardarSesion(
                        			ServletActionContext.getRequest().getSession().getId(),
                        			userVO.getUser(),
                        			rolActivo.getClave(),
                        			userAgent,
                        			Utils.esSesionMovil(userAgent),
                        			new Date());
                        }
                        catch(Exception ex)
                        {
                        	logger.error("error al guardar sesion en base de datos",ex);
                        }
                        break;
                    }
                }
                try
                {
                    String msg = principalManagerJdbcTemplate.configuracionCompleta(_codigoCliente);
                    logger.debug("Configuraci�n: " + msg);
                    codigoValido = true;
                } catch (Exception e) {
                    logger.error(e.toString(),e);
                    addActionError(e.getMessage());
                    codigoValido = false;
                    retorno= "confcompleta";
                }
            } // else
        } // if
        else
        // No se recibieron datos del cliente
        {
            codigoValido = false;
        }
        if(retorno.equals(SUCCESS))
        //si no intento guardar la configuracion de un usuario leido por parametros incorrectos
        //o si se cargo un usuario, se cargo correctamente
        {
            logger.debug("Salida metodo getCodigoTree");
            logger.debug("CODIGO VALIDO: " + codigoValido);
            UserVO usuario = (UserVO) session.get("USUARIO");
            complementaUsuario(usuario);
            session.put("USUARIO", usuario);
        }
        return retorno;
    }
    
    /**
     * Metodo que complementa los datos necesarios de un usuario
     * para que no se ciclen los jsp en taglibs.jsp:
     * String directorioIdioma = "/biosnet/"+((mx.com.aon.portal.model.UserVO)session.getAttribute("USUARIO")).getIdioma().getLabel();
     * @param usuario 
     */
    private void complementaUsuario(UserVO usuario) throws Exception
    {
        IsoVO isoLocal = navigationManagerNuevo.getVariablesIso(usuario.getUser());
        //logger.debug("isoLocal=====" + isoLocal);
        BaseObjectVO languague = new BaseObjectVO();
        languague.setValue(isoLocal.getCdIdioma());
        languague.setLabel(isoLocal.getLanguague());
        BaseObjectVO pais = new BaseObjectVO();
        pais.setValue(isoLocal.getPais());
        usuario.setPais(pais);
        String cdPais = usuario.getPais().getValue();
        //logger.debug("El pais del usuario es : " + cdPais);
        usuario.setFormatoFecha(isoLocal.getFormatoFecha());
        usuario.setFormatoNumerico(isoLocal.getFormatoNumerico());
        usuario.setIdioma(languague);
        usuario.setTamagnoPaginacionGrid(numReg);
        //logger.debug("En seleccionaRolCliente: " + isoLocal.getClientDateFormat());
        usuario.setClientFormatDate(isoLocal.getClientDateFormat());
        //Agregados c�digos de Idioma y Regi�n
        //languague.setValue(iso.getCdIdioma());
        //usuario.setIdioma(languague);
        BaseObjectVO region = new BaseObjectVO();
        region.setValue(isoLocal.getCdRegion());
        usuario.setRegion(region);
    }

    /**
     * M�todo que carga la pantalla que contiene el �rbol de clientes y roles
     * @return success
     * @throws Exception
     */
    public String obtenRolesClientes() throws Exception {

        String retorno=SUCCESS;

        ///////////////////////////////////////////////////////////////////////////////
        ////// Crear un usuario y complementar su propiedades:                   //////
        ////// formato decha, formato numerico, idioma, tamanio paginacion grid, //////
        ////// cliente format date y region. Se guarda en sesi�n                 //////
        ///////////////////////////////////////////////////////////////////////////////
        UserVO usuario = (UserVO) session.get("USUARIO");
        session.put("MessageConf", null);
        registrosEncontrados = 0;
        listaRolCliente = usuarioManager.getClientesRoles(usuario.getUser());
        iso = navigationManagerNuevo.getVariablesIso(usuario.getUser());
        numReg = navigationManager.getNumRegistro(usuario.getUser());
        userList = usuarioManager.getAttributesUser(usuario.getUser());
        logger.info("Usuarios totales: "+(userList!=null?userList.size():"null")+ " pero solo el de sesion se complemento");
        complementaUsuario(usuario);
        session.put("USUARIO", usuario);
        ///////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////
        ////// Verificar los permisos del usuario //////
        ////////////////////////////////////////////////
        if (listaRolCliente==null || listaRolCliente.isEmpty())
        // sin permisos
        {
            //success = false; //jtezva -> inecesario, no es json
            session.clear();
            //registrosEncontrados = 0; //jtezva -> inecesario, no es json
            throw new Exception("Usted no posee un Rol Asociado por favor contacte al Administrador!");
        }
        else if((listaRolCliente.size() == 1) && (listaRolCliente.get(0).getChildren().length == 1))
        // solo tiene un cliente y un rol para ese cliente, se redirecciona la pagina
        {
            codigoRol = userList.get(0).getRoles().get(0).getClave();
            codigoCliente = userList.get(0).getEmpresa().getElementoId();
            _codigoCliente = userList.get(0).getEmpresa().getElementoId();
            boolean configuracionCompleta=false;
            try
            {
                principalManagerJdbcTemplate.configuracionCompleta(_codigoCliente);
                configuracionCompleta=true;
            }
            catch (Exception e)
            {
                configuracionCompleta=false;
                logger.error(e.toString(), e);
                addActionError(e.getMessage());
                //success = false; //jtezva -> inecesario, no es json
                session.put("MessageConf", e.getMessage()); //Se pone en sesi�n el mensaje a mostrar
                //return SUCCESS; //jtezva -> se sustituyo por la variable retorno
            }
            if(configuracionCompleta)
            {
                for (UserVO user : userList) {
                    usuario.setEmpresa(user.getEmpresa());
                    if (usuario.getRoles() != null) {
                        usuario.setRolActivo(usuario.getRoles().get(0));
                    } else {
                        usuario.setRolActivo(userList.get(0).getRoles().get(0));
                    }
                }
                //session.put("USUARIO", usuario);//jtezva -> repetido
                //success = true;//jtezva -> inecesario, ya no es json
                registrosEncontrados = listaRolCliente.size();

                /* jtezva -> todo este bloque equivale a complementar al usuario
                //BaseObjectVO languague = new BaseObjectVO();
                languague.setValue(iso.getLanguague());

                //BaseObjectVO pais = new BaseObjectVO();
                pais.setValue(iso.getPais());

                userList.get(0).setPais(pais);
                userList.get(0).setFormatoFecha(iso.getFormatoFecha());
                userList.get(0).setFormatoNumerico(iso.getFormatoNumerico());
                userList.get(0).setIdioma(languague);
                userList.get(0).setTamagnoPaginacionGrid(numReg);
                logger.debug("En seleccionaRolCliente: " + iso.getClientDateFormat());
                userList.get(0).setClientFormatDate(iso.getClientDateFormat());

                //Agregados c�digos de Idioma y Regi�n
                languague.setValue(iso.getCdIdioma());
                userList.get(0).setIdioma(languague);
                //BaseObjectVO region = new BaseObjectVO();
                region.setValue(iso.getCdRegion());
                userList.get(0).setRegion(region);


                //userList.get(0).etRegion().
                */
                complementaUsuario(userList.get(0));
                
                session.put("CARGA_USUARIO_COMPLETO", userList);
                obtenCodigoTree();
                retorno="load";
                soloUnRol=true;
            }
        }
        else
        // tiene almenos dos clientes o dos roles distintos
        {
            logger.debug("listaRolCliente=" + listaRolCliente.size());
            for (RamaVO nodos : listaRolCliente) {
                if (nodos.getChildren() != null) {
                    nodos.setLeaf(false);
                }
            }
            session.put("CARGA_USUARIO_COMPLETO", userList);
            success = true;
            logger.debug("retorno=" + retorno);
        }
        ////////////////////////////////////////////////
        
        return retorno;
    }

    /**
     * Carga los elementos del �rbol de clientes y roles para el usuario en sesi�n.
     * Se supone que esta llamada json se llama desde la vista de �rbol
     * que fue cargada por obtenRolesClientes(), por lo que no se valida
     * que este vacia, porque de ser asi se habria validado en obtenRolesClientes()
     * @return SUCCESS
     */
    public String jsonObtenRolesClientes()
    {
        try {
            UserVO usuario = (UserVO) session.get("USUARIO");
            listaRolCliente = usuarioManager.getClientesRoles(usuario.getUser());
            for (RamaVO nodos : listaRolCliente)
            {
                if (nodos.getChildren() != null)
                {
                    nodos.setLeaf(false);
                }
            }
        }
        catch (Exception ex)
        {
            logger.error(ex.toString(),ex);
            listaRolCliente=new ArrayList<RamaVO>(0);
        }
        return SUCCESS;
    }

    /**
     *
     * @return
     */
    public List<RamaVO> getListaRolCliente() {
        return listaRolCliente;
    }

    /**
     *
     * @param listaRolCliente
     */
    public void setListaRolCliente(List<RamaVO> listaRolCliente) {
        this.listaRolCliente = listaRolCliente;
    }

    /**
     *
     * @param navigationManager
     */
    public void setNavigationManager(NavigationManager navigationManager) {
        this.navigationManager = navigationManager;
    }

    /**
     *
     * @return
     */
    public String getUser() {
        return user;
    }

    /**
     *
     * @param user
     */
    public void setUser(String user) {
        this.user = user;
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
     * @return
     */
    public IsoVO getIso() {
        return iso;
    }

    /**
     *
     * @param iso
     */
    public void setIso(IsoVO iso) {
        this.iso = iso;
    }

    /**
     *
     * @return
     */
    public String getCodigoCliente() {
        return codigoCliente;
    }

    /**
     *
     * @param codigoCliente
     */
    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    /**
     *
     * @return
     */
    public String getCodigoRol() {
        return codigoRol;
    }

    /**
     *
     * @param codigoRol
     */
    public void setCodigoRol(String codigoRol) {
        this.codigoRol = codigoRol;
    }

    /**
     *
     * @return
     */
    public boolean isCodigoValido() {
        return codigoValido;
    }

    /**
     *
     * @param codigoValido
     */
    public void setCodigoValido(boolean codigoValido) {
        this.codigoValido = codigoValido;
    }

    /**
     * @see Metodo implementado solo por herencia.
     */
    public void prepare() throws Exception {
    }

    /**
     *
     * @return
     */
    public int getNumReg() {
        return numReg;
    }

    /**
     *
     * @param numReg
     */
    public void setNumReg(int numReg) {
        this.numReg = numReg;
    }

    /**
     *
     * @return
     */
    public List<UserVO> getUserList() {
        return userList;
    }

    /**
     *
     * @param userList
     */
    public void setUserList(List<UserVO> userList) {
        this.userList = userList;
    }

    public int getRegistrosEncontrados() {
        return registrosEncontrados;
    }

    public void setRegistrosEncontrados(int registrosEncontrados) {
        this.registrosEncontrados = registrosEncontrados;
    }

    public void setPrincipalManagerJdbcTemplate(
            PrincipalManager principalManagerJdbcTemplate) {
        this.principalManagerJdbcTemplate = principalManagerJdbcTemplate;
    }

    public void setUsuarioManager(UsuarioManager usuarioManager) {
        this.usuarioManager = usuarioManager;
    }

	public void setNavigationManagerNuevo(
			mx.com.gseguros.portal.general.service.NavigationManager navigationManagerNuevo) {
		this.navigationManagerNuevo = navigationManagerNuevo;
	}

	public boolean isSoloUnRol() {
		return soloUnRol;
	}

	public void setSoloUnRol(boolean soloUnRol) {
		this.soloUnRol = soloUnRol;
	}
}