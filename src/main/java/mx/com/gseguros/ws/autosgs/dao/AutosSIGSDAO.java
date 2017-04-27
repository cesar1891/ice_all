package mx.com.gseguros.ws.autosgs.dao;

import java.util.Date;
import java.util.Map;

import mx.com.gseguros.portal.general.model.PolizaVO;


public interface AutosSIGSDAO {
		
	public Integer endosoDomicilio(Map<String, Object> params) throws Exception;

	public Integer cambioDomicilioCP(Map<String, Object> params) throws Exception;
	
	public Integer cambioDomicilioSinCPColonia(Map<String, Object> params) throws Exception;

	public Integer insertaReciboAuto(Map<String, Object> params) throws Exception;

	public Integer confirmaRecibosAuto(Map<String, Object> params) throws Exception;

	public Integer ejecutaVidaPorRecibo(Map<String, Object> params) throws Exception;
	
	public Integer endosoPlacasMotor(Map<String, Object> params) throws Exception;

	public Integer endosoTipoServicio(Map<String, Object> params) throws Exception;

	public Integer endosoVigenciaPol(Map<String, Object> params) throws Exception;

	public Integer endosoTextoLibre(Map<String, Object> params) throws Exception;

	public Integer endosoSerie(Map<String, Object> params) throws Exception;
	
	public Integer endosoBeneficiario(Map<String, Object> params) throws Exception;
	
	public Integer endosoAseguradoAlterno(Map<String, Object> params) throws Exception;
	
	public Integer endosoAdaptacionesRC(Map<String, Object> params) throws Exception;
	
//	public Integer endosoVigencia(Map<String, Object> params) throws Exception;
	
	public Integer endosoNombreCliente(Map<String, Object> params) throws Exception;
	
	public Integer endosoRfcCliente(Map<String, Object> params) throws Exception;
	
	public Integer endosoCambioCliente(Map<String, Object> params) throws Exception;
	
	public void revierteEndosoFallidoSigs(Map<String, Object> params) throws Exception;
	
	public void revierteEndosoBFallidoSigs(Map<String, Object> params) throws Exception;
	
	public String CambioClientenombreRFCfechaNacimiento(Map<String, Object> params) throws Exception;

	public Integer obtieneTipoCliWS(String codigoExterno, String compania) throws Exception;
	
	/**
	 * Valida agente, ramo y cdtipend.
	 * Si no valida bien lanza excepcion
	 */
	public void validarAgenteParaNuevoTramite(String cdagente, String ramo, String cdtipend) throws Exception;
	
	public void actualizaTramiteMC(PolizaVO poliza, String estra) throws Exception;
	
	public void actualizaTramiteEmisionMC(String inNumsuc,String inNumram,String inNumpol,String inRensuc,String inRenram,String inRenpol,String inUsuario) throws Exception;
	
	public Integer spIdentificaRenovacion(String inNumsuc,String inNumram,String inNumpol,Date vFechaEmision,Date vInicioVigencia,Date vFinVigencia,String inRensuc,String inRenram,String inRenpol) throws Exception;
	
	public Integer integraDxnAutos(Map<String, String> params) throws Exception;
    
    public Integer EndoBeneficiarioVidaAuto(Map<String, Object> params) throws Exception;
    
    public Integer endosoCambioModeloDescripcion(Map<String, Object> params) throws Exception;
    
    public Integer endosoTipoCarga(Map<String, Object> params) throws Exception;
}
