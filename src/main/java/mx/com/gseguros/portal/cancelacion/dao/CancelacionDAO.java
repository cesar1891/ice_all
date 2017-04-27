package mx.com.gseguros.portal.cancelacion.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.portal.general.model.PolizaVO;

public interface CancelacionDAO
{
    public List<Map<String,String>> buscarPolizas             (Map<String,String> params) throws Exception;
    public Map<String,String>       obtenerDetalleCancelacion (Map<String,String> params) throws Exception;
    
	@Deprecated
    public List<Map<String,String>> obtenerPolizasCandidatas  (Map<String,String> params) throws Exception;
	
    public void                     seleccionaPolizas         (Map<String,Object> params) throws Exception;
    
    @Deprecated
    public String                   cancelaPoliza             (Map<String,String> params) throws Exception;

	@Deprecated
    public void                     seleccionaPolizaUnica     (Map<String,Object> params) throws Exception;
	
    public void                     actualizarTagrucan        (Map<String,String> params) throws Exception;
    public void                     cancelacionMasiva         (Map<String,String> params) throws Exception;
    /**
	 * PKG_CONSULTA.P_IMP_DOC_CANCELACION
	 * @return nmsolici,nmsituac,descripc,descripl,ntramite,nmsuplem
	 */
	public List<Map<String,String>> reimprimeDocumentos       (Map<String,String> params) throws Exception;
	public ArrayList<PolizaVO> obtienePolizasCancelacionMasiva(Map<String,String> params) throws Exception;
	
	public void seleccionaPolizaUnica(
			String cdunieco
			,String cdramo
			,String nmpoliza
			,String agencia
			,Date   fechapro
			)throws Exception;
	
	public List<Map<String,String>> obtenerPolizasCandidatas(
			String asegurado
			,String cdunieco
			,String cdramo
			,String nmpoliza
			,String nmsituac
			)throws Exception;
	
	public Map<String,Object> cancelaPoliza(
			String cdunieco
			,String cdramo
			,String cduniage
			,String estado
			,String nmpoliza
			,String nmsituac
			,String cdrazon
			,String comenta
			,Date feefecto
			,Date fevencim
			,Date fecancel
			,String cdusuari
			,String cdtipsup
			,String cdsisrol
			)throws Exception;
	
	public void validaCancelacionAProrrata(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;
	
	public boolean validaRazoCancelacion(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdrazon
			)throws Exception;
}