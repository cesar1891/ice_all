package mx.com.aon.portal.service.impl.principal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mx.com.aon.portal.model.principal.ClienteVO;
import mx.com.aon.portal.model.principal.ConfiguracionVO;
import mx.com.aon.portal.model.principal.PaginaVO;
import mx.com.aon.portal.model.principal.RolesVO;
import mx.com.aon.portal.model.principal.SeccionVO;
import mx.com.aon.portal.model.principal.TipoVO;
import mx.com.aon.portal.service.PagedList;
import mx.com.aon.portal.service.impl.AbstractManagerJdbcTemplateInvoke;
import mx.com.aon.portal.service.principal.PrincipalManager;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.exception.ApplicationException;

public class PrincipalManagerJdbcTemplateImpl extends
		AbstractManagerJdbcTemplateInvoke implements PrincipalManager {

	public void addPagina(String claveConfiguracion, String paginaNombre,
			String claveClienteInsert, String claveRolInsert)
			throws ApplicationException {
		// TODO Auto-generated method stub

	}

	public String agregarNuevaConfiguracion(ConfiguracionVO configuracionVo)
			throws ApplicationException {
		HashMap map = new HashMap();
		map.put("pv_cdconfig_i", configuracionVo.getClaveConfig());
		map.put("pv_dsconfig_i", configuracionVo.getDescripcionConfig());
		map.put("pv_cdelemento_i", configuracionVo.getClaveElemento());
		map.put("pv_cdrol_i", configuracionVo.getClaveRol());
		map.put("pv_cdseccion_i", configuracionVo.getClaveSeccion());
		map.put("pv_swhabilitado_i", configuracionVo.getSwHabilitado());
		map.put("pv_dsespecificacion_i", configuracionVo.getEspecificacion());
		map.put("pv_dscontenido_i", configuracionVo.getContenido());
		map.put("pv_cdtipo_i", configuracionVo.getClaveTipo());
		map.put("pv_dsarchivo_i", configuracionVo.getArchivoLoad());
		map.put("pv_dscontenidosec_i", configuracionVo.getContenidoDatoSeg());
		
		WrapperResultados res = returnBackBoneInvoke(map, "GUARDAR_NUEVA_CONFIGURACION");
		return res.getMsgText();
	}

	public void agregarconfiguracion(ConfiguracionVO configuracionVo)
			throws ApplicationException {


	}

	public String borrarConfig(String claveDeConfiguracion,
			String claveDeSeccion) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	public void borrarPagina(String id) throws ApplicationException {
		// TODO Auto-generated method stub

	}

	public void copiarConfig(String idCliente, String idRol,
			String idClienteNvo, String idRolNvo) throws ApplicationException {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	public String editarNuevaConfiguracion(ConfiguracionVO configuracionVo)
			throws ApplicationException {

		HashMap map = new HashMap();
		map.put("pv_cdconfig_i", configuracionVo.getClaveConfig());
		map.put("pv_dsconfig_i", configuracionVo.getDescripcionConfig());
		map.put("pv_cdelemento_i", configuracionVo.getClaveElemento());
		map.put("pv_cdrol_i", configuracionVo.getClaveRol());
		map.put("pv_cdseccion_i", configuracionVo.getClaveSeccion());
		map.put("pv_swhabilitado_i", configuracionVo.getSwHabilitado());
		map.put("pv_dsespecificacion_i", configuracionVo.getEspecificacion());
		map.put("pv_dscontenido_i", configuracionVo.getContenido());
		map.put("pv_cdtipo_i", configuracionVo.getClaveTipo());
		map.put("pv_dsarchivo_i", configuracionVo.getArchivoLoad());
		map.put("pv_dscontenidosec_i", configuracionVo.getContenidoDatoSeg());
		
		WrapperResultados res = returnBackBoneInvoke(map, "GUARDA_CONFIGURACION");
		return res.getMsgText();
	}

	public List<PaginaVO> getAllPaginas(String nombrePagina, String rolPagina,
			String clientePagina) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public PagedList getAllPaginas(String nombrePagina, String rolPagina,
			String clientePagina, int start, int limit) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<ClienteVO> getListaCliente() throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<RolesVO> getListaRol() throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<SeccionVO> getListaSeccion() throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<TipoVO> getListaTipo(String tabla)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	public PagedList getRoles(String claveNombre, String rol, String cliente,
			String seccion, int start, int limit) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	public String guardarPagina(String claveConfiguracion, String paginaNombre,
			String claveClienteInsert, String claveRolInsert)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	public String configuracionCompleta(String cdElemento)
			throws ApplicationException {
		HashMap map = new HashMap();
		map.put("pv_cdelemento_i", cdElemento);

		WrapperResultados res = returnBackBoneInvoke(map, "VALIDAR_CONFIGURACION_COMPLETA");
		return res.getMsgText();
	}

}
