package mx.com.gseguros.portal.cancelacion.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.portal.general.model.PolizaVO;

public interface CancelacionManager
{	
	public List<Map<String,String>> buscarPolizas             (Map<String,String> params) throws Exception;
	public Map<String,String>       obtenerDetalleCancelacion (Map<String,String> params) throws Exception;
	public List<Map<String,String>> obtenerPolizasCandidatas  (Map<String,String> params) throws Exception;
	public void                     seleccionaPolizas         (Map<String,Object> params) throws Exception;
	public String                   cancelaPoliza             (Map<String,String> params) throws Exception;
	public String                   cancelaPoliza             (
			String cdunieco
			,String cdramo
			,String cduniage
			,String estado
			,String nmpoliza
			,String nmsituac
			,String cdrazon
			,String comenta
			,String feefecto
			,String fevencim
			,String fecancel
			,String cdusuari
			,String cdtipsup
			,String cdsisrol) throws Exception;
	public void                     seleccionaPolizaUnica     (Map<String,Object> params) throws Exception;
	public void                     seleccionaPolizaUnica (
			String cdunieco
			,String cdramo
			,String nmpoliza
			,String agencia
			,Date   fecha) throws Exception;
	public void                     actualizarTagrucan        (Map<String,String> params) throws Exception;
	public void                     cancelacionMasiva         (Map<String,String> params) throws Exception;

	public ArrayList<PolizaVO> obtienePolizasCancelacionMasiva(Map<String,String> params) throws Exception;
	/**
	 * PKG_CONSULTA.P_IMP_DOC_CANCELACION
	 * @return nmsolici,nmsituac,descripc,descripl,ntramite,nmsuplem
	 */
	public List<Map<String, String>> reimprimeDocumentos      (String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String tipmov) throws Exception;
	
	public void validaCancelacionAProrrata(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;

	public boolean validaRazonCancelacion(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdrazon
			)throws Exception;
}