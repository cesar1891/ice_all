package mx.com.aon.portal.service.principal;

import java.util.ArrayList;
import java.util.List;

import mx.com.aon.portal.model.principal.ClienteVO;
import mx.com.aon.portal.model.principal.ConfiguracionVO;
import mx.com.aon.portal.model.principal.PaginaVO;
import mx.com.aon.portal.model.principal.RolesVO;
import mx.com.aon.portal.model.principal.SeccionVO;
import mx.com.aon.portal.model.principal.TipoVO;
import mx.com.aon.portal.service.PagedList;
import mx.com.gseguros.exception.ApplicationException;
/**
 * 
 * @author sergio.ramirez
 *
 */
public interface PrincipalManager {
	/**
	 * 
	 * @param rol
	 * @param cliente
	 * @param seccion
	 * @param seccion2 
	 * @return
	 * @throws ApplicationException
	 */
	public PagedList getRoles(String claveNombre, String rol, String cliente, String seccion, int start, int limit) throws ApplicationException;
	//public ArrayList<RolVO> getRoles(String nombreClave, String rol, String cliente, String seccion) throws ApplicationException;
	/**
	 * 
	 * @return
	 * @throws ApplicationException
	 */
	public ArrayList<ClienteVO> getListaCliente() throws ApplicationException;
	/**
	 * 
	 * @return
	 * @throws ApplicationException
	 */
	public ArrayList<SeccionVO> getListaSeccion() throws ApplicationException;
	/**
	 * 
	 * @return
	 * @throws ApplicationException
	 */
	public ArrayList<RolesVO> getListaRol()throws ApplicationException;
	/**
	 * 
	 * @param tabla
	 * @return
	 * @throws ApplicationException
	 */
	public ArrayList<TipoVO> getListaTipo(String tabla)throws ApplicationException;
	/**
	 * 
	 * @param configuracionVo
	 * @throws ApplicationException
	 */
	public void agregarconfiguracion(ConfiguracionVO configuracionVo)throws ApplicationException;
	
	public String  agregarNuevaConfiguracion(ConfiguracionVO configuracionVo) throws ApplicationException;

	public String  editarNuevaConfiguracion(ConfiguracionVO configuracionVo) throws ApplicationException;
	
	/**
	 * 
	 * @param idCliente
	 * @param idRol
	 * @param idClienteNvo
	 * @param idRolNvo
	 * @throws ApplicationException
	 */
	public void copiarConfig(String idCliente,  String idRol, String idClienteNvo, String idRolNvo) throws ApplicationException;
	/**
	 * 
	 * @param id
	 * @param claveDeSeccion 
	 * @throws ApplicationException
	 */
	public String borrarConfig(String claveDeConfiguracion, String claveDeSeccion)throws ApplicationException;
	
	/**
	 * 
	 * @param nombrePagina
	 * @param rolPagina
	 * @param clientePagina
	 * @return
	 * @throws ApplicationException
	 */
	public List<PaginaVO> getAllPaginas(String nombrePagina, String rolPagina,String clientePagina) throws ApplicationException;
	
	/**
	 * @param nombrePagina
	 * @param rolPagina
	 * @param clientePagina
	 * @param start
	 * @param limit
	 * @return
	 * @throws ApplicationException
	 */
	public PagedList getAllPaginas(String nombrePagina, String rolPagina, String clientePagina, int start, int limit) throws ApplicationException;
	
	/**
	 * 
	 * @param claveConfiguracion
	 * @param paginaNombre
	 * @param claveClienteInsert
	 * @param claveRolInsert
	 * @throws ApplicationException
	 */
	public void addPagina(String claveConfiguracion, String paginaNombre, String claveClienteInsert, String claveRolInsert)throws ApplicationException;
	
	/**
	 * 
	 * @param id
	 * @throws ApplicationException
	 */
	public void borrarPagina(String id) throws ApplicationException;
	
	public String  guardarPagina(String claveConfiguracion, String paginaNombre, String claveClienteInsert, String claveRolInsert) throws ApplicationException;
	
	public String configuracionCompleta (String cdElemento) throws ApplicationException; 
}
