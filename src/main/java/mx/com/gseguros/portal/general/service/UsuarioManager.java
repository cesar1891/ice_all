package mx.com.gseguros.portal.general.service;

import java.util.List;
import java.util.Map;

import mx.com.aon.portal.model.RamaVO;
import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.portal.general.model.UsuarioVO;

public interface UsuarioManager {
	
	public boolean creaEditaRolSistema(Map<String, String> params) throws Exception;
	
	public GenericVO guardaUsuario(Map<String, String> params) throws Exception;

	public void cambiaEstatusUsuario(Map<String, String> params) throws Exception;

	public List<UsuarioVO> obtieneUsuarios(Map<String, String> params) throws Exception;
	
	public List<GenericVO> obtienerRolesPorPrivilegio(Map<String, String> params) throws Exception;
	
	public List<Map<String, String>> obtieneRolesUsuario(Map<String, String> params) throws Exception;
	
	public boolean guardaRolesUsuario(Map<String, String> params, List<Map<String, String>> saveList) throws Exception;

	public List<Map<String, String>> obtieneProductosAgente(Map<String, String> params) throws Exception;
	
	public boolean guardaProductosAgente(Map<String, String> params, List<Map<String, String>> saveList) throws Exception;

    public List<RamaVO> getClientesRoles(String user)throws Exception;

    public List<UserVO> getAttributesUser(String user) throws Exception;
    
    public List<Map<String, String>> obtieneImpresorasUsuario(String cdusuario) throws Exception;

    public boolean guardaImpresorasUsuario(String cdusuario,
			   String impresora,
			   String ip,
			   String tipo,
			   String descripcion,
			   String disponible,
			   String alta) throws Exception;
    
    public String habilitaDeshabilitaImpresora(String pv_habilita,
			  String pv_impresora_i,
			  String pv_CdUsuari_i) throws Exception;
    
    public String insertaActualizaImpresora(String pv_nombre_i,
			  String pv_ip_i,
			  String pv_tipo_i,
			  String pv_descripcion_i,
			  String pv_swactivo_i)
					  	throws Exception;
}
