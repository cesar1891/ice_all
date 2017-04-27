package mx.com.gseguros.ws.autosgs.service;

import java.util.List;
import java.util.Map;

import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.ws.autosgs.emision.model.EmisionAutosVO;

public interface EmisionAutosService {

	/**
	 * Ejecuta el WS de Autos de SIGS
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @param ntramite
	 * @param userVO
	 * @return
	 * @throws Exception 
	 */
	public EmisionAutosVO cotizaEmiteAutomovilWS(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, String ntramite, String cdtipsit, UserVO userVO) throws Exception;
	
	/**
	 * Para saber que movimientos de endosos se imprimiran en un endoso principal.
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @return
	 */
	public List<Map<String,String>> obtieneEndososImprimir(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem);
	
	public Integer enviaRecibosAutosSigs(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, String nmpoliex, String subramo, String sucursal) throws Exception;
	
	public int endosoCambioClienteAutos(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem);
	
	public int endosoCambioRfcClienteAutos(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem);
	
	public int endosoCambioNombreClienteAutos(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem);

	public int endosoCambioDomicil(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, UserVO usuarioSesion);

	public int actualizaDatosCambioDomicilCP(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, UserVO usuarioSesion);

	public int actualizaDatosCambioDomicilSinCP(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, UserVO usuarioSesion);
}
