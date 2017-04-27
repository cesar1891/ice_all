package mx.com.gseguros.portal.general.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal.model.IsoVO;
import mx.com.aon.portal.model.UsuarioRolEmpresaVO;
import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.portal.general.model.UsuarioVO;

public interface UsuarioDAO {

	public String creaEditaRolSistema(Map params) throws Exception;
	
	public GenericVO guardaUsuario(Map<String, String> params) throws Exception;

	public void cambiaEstatusUsuario(Map params) throws Exception;

	public List<UsuarioVO> obtieneUsuarios(Map params) throws Exception;
	
	public List<GenericVO> obtienerRolesPorPrivilegio(Map params) throws Exception;
	
	public List<Map<String, String>> obtieneRolesUsuario(Map params) throws Exception;
	
	public String guardaRolUsuario(Map params) throws Exception;

	public List<Map<String, String>> obtieneProductosAgente(Map params) throws Exception;
	
	public String guardaProductoAgente(Map params) throws Exception;
	
	public List<UsuarioRolEmpresaVO> obtieneRolesCliente(String user) throws Exception;

	public IsoVO obtieneVariablesIso(String user) throws Exception;
	
	public void guardarSesion(String idSesion,String cdusuari,String cdsisrol,String userAgent,boolean esMovil,Date fecha) throws Exception;
	
	public List<Map<String, String>> obtieneImpresorasUsuario(String cdusuario) throws Exception;
	
	public String guardaImpresorasUsuario(String cdusuario,
			  String ip,
			  String tipo, 
			  String descripcion,
			  String swactivo,
			  String impAsig,
			  String nombre)
					  throws Exception;
	public String habilitaDeshabilitaImpresora(String pv_habilita,
			  String pv_impresora_i,
			  String pv_CdUsuari_i)
					  throws Exception;
	public String insertaActualizaImpresora(String pv_nombre_i,
			  String pv_ip_i,
			  String pv_tipo_i,
			  String pv_descripcion_i,
			  String pv_swactivo_i)
					  throws Exception;
	
	/* *
	 * Valida la edad de los asegurados en una poliza
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @return Lista de los asegurados de edad invalida
	 * @throws Exception
	 * /
	public List<Map<String,String>> validaEdadAsegurados(String cdunieco, String cdramo, String estado,
			String nmpoliza, String nmsuplem) throws Exception;
	*/
	

}