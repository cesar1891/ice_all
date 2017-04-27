package mx.com.gseguros.ws.ice2sigs.service;

import java.util.List;
import java.util.Map;

import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.portal.general.model.RespuestaVO;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteGeneral;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteGeneralRespuesta;
import mx.com.gseguros.ws.ice2sigs.client.model.ReciboWrapper;

public interface Ice2sigsService {

	public enum Operacion {

		INSERTA(1), ACTUALIZA(2), CONSULTA(3), CONSULTA_GENERAL(4);

		private int codigo;

		private Operacion(int codigo) {
			this.codigo = codigo;
		}

		public int getCodigo() {
			return codigo;
		}

	}

	public enum Estatus {

		EXITO(0), LLAVE_DUPLICADA(1), HAY_UN_CAMPO_NULO_QUE_ES_OBLIGATORIO(2), LLAVE_INEXISTENTE(3),
		ENTIDAD_NULA(5), RELACION_NO_IDENTIFICADA(6), ERROR_DESCONOCIDO(7), OPERACION_INVALIDA_PARA_ESTA_ENTIDAD(8)
;

		private int codigo;

		private Estatus(int codigo) {
			this.codigo = codigo;
		}

		public int getCodigo() {
			return codigo;
		}

	}

	public enum TipoError {
		
		ErrWScli("1"), ErrWScliCx("2"), ErrWSrec("3"), ErrWSrecCx("4"), ErrWSsin("5"), ErrWSsinCx("6");
		
		private String codigo;
		
		private TipoError(String codigo) {
			this.codigo = codigo;
		}
		
		public String getCodigo() {
			return codigo;
		}
		
	}

	/**
	 * Ejecuta el metodo expuesto de cliente del WS de iceToSigs
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @param op
	 * @param userVO
	 * @return
	 */
	@Deprecated
	public boolean ejecutaWSclienteSalud(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, String ntramite,
			Ice2sigsService.Operacion op, UserVO userVO);

	/**
	 * Ejecuta el metodo expuesto de cliente del WS de iceToSigs
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @param op
	 * @param userVO
	 * @return
	 */
	public ClienteGeneralRespuesta ejecutaWSclienteGeneral(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, String ntramite, String cdperson,
			Ice2sigsService.Operacion op, ClienteGeneral cliente, UserVO userVO, boolean async);

	
	/**
	 * Ejecuta el metodo expuesto para las direcciones de clientes del WS Ice2Sigs
	 * @param cdperson
	 * @param compania
	 * @param direccionesCliSave
	 * @param direccionesCliUpdate
	 * @param userVO
	 * @return
	 */
	public boolean ejecutaWSdireccionClienteGeneral(String cdperson, String compania, List<Map<String,String>> direccionesCliSave, List<Map<String,String>> direccionesCliUpdate, boolean sinCodigoWS, UserVO userVO);
	
	
	/**
	 * Ejecuta el metodo expuesto de recibos del WS iceToSigs
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @param rutaPoliza
	 * @param sucursal
	 * @param nmsolici
	 * @param ntramite
	 * @param async
	 * @param operacion
	 * @param tipoMov
	 * @param userVO
	 * @return
	 */
	public boolean ejecutaWSrecibos(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem,
			String rutaPoliza, String sucursal,
			String nmsolici, String ntramite, boolean async,
			String tipoMov, UserVO userVO);

	/**
	 * Metodo que ejecuta el WS para mandar los datos de un siniestro a GS
	 * @param ntramite
	 * @param async
	 * @param userVO
	 * @return
	 */
	public RespuestaVO ejecutaWSreclamosTramite(String ntramite, Ice2sigsService.Operacion op, boolean async, UserVO userVO);
	
	/**
	 * Ejecuta WS para actualizar informacion del folio consolidado en SIGS
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @param sucursal
	 * @param ntramite
	 * @param async
	 * @param userVO
	 * @param reciboWrapper
	 * @return
	 */
	public boolean ejecutaWSrecibos(
	           String cdunieco, 
	           String cdramo, 
	           String estado, 
	           String nmpoliza, 
	           String nmsuplem,
	           String sucursal, 
	           String ntramite, 
	           boolean async, 
	           UserVO userVO, 
	           ReciboWrapper reciboWrapper);
}
