package mx.com.gseguros.ws.tipocambio.service;

import mx.com.gseguros.ws.tipocambio.client.axis2.TipoCambioWSServiceStub.ResponseTipoCambio;

public interface TipoCambioDolarGSService {
	public ResponseTipoCambio obtieneTipoCambioDolarGS(int tipoMoneda);
}
