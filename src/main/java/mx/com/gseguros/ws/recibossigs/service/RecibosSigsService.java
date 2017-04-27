package mx.com.gseguros.ws.recibossigs.service;

import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.ws.recibossigs.client.axis2.GeneradorReciboDxnWsServiceStub.GeneradorRecibosDxnRespuesta;


public interface RecibosSigsService {

	public enum Estatus {

		EXITO(0), LLAVE_DUPLICADA(1);

		private int codigo;

		private Estatus(int codigo) {
			this.codigo = codigo;
		}

		public int getCodigo() {
			return codigo;
		}

	}

	public enum TipoError {
		
		ErrWsDXN("7"), ErrWsDXNCx("8");
		
		private String codigo;
		
		private TipoError(String codigo) {
			this.codigo = codigo;
		}
		
		public String getCodigo() {
			return codigo;
		}
		
	}
	
	/**
	 * Genera los recibos de descuento por nomina, solo se utiliza en emision para obtener los calendarios en la emision, luego de esto en endosos se utiliza el WS de Recibos normal
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @param sucursal
	 * @param nmsolici
	 * @param ntramite
	 * @return
	 */
	public boolean generaRecibosDxN(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem,
			String sucursal, String nmsolici, String ntramite, UserVO userVO);
	
	public boolean guardaCalendariosDxnFinaliza(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem, String sucursal, String nmsolici, String ntramite, GeneradorRecibosDxnRespuesta calendarios);

}
