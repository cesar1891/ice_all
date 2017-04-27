package mx.com.gseguros.portal.endosos.model;

import mx.com.gseguros.portal.despachador.model.RespuestaTurnadoVO;

public class RespuestaConfirmacionEndosoVO {

	/**
	 * Indica si el endoso fue confirmado
	 */
	private boolean confirmado;
	
	/**
	 * Numero de tr&aacute;mite del endoso que fue generado
	 */
	private String numeroTramite;
	
	private RespuestaTurnadoVO respuestaTurnado;

	public boolean isConfirmado() {
		return confirmado;
	}

	public void setConfirmado(boolean confirmado) {
		this.confirmado = confirmado;
	}

	public String getNumeroTramite() {
		return numeroTramite;
	}

	public void setNumeroTramite(String numeroTramite) {
		this.numeroTramite = numeroTramite;
	}

    public RespuestaTurnadoVO getRespuestaTurnado() {
        return respuestaTurnado;
    }

    public void setRespuestaTurnado(RespuestaTurnadoVO respuestaTurnado) {
        this.respuestaTurnado = respuestaTurnado;
    }
	
}