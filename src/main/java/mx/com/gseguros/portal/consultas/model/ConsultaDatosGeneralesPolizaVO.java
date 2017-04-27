package mx.com.gseguros.portal.consultas.model;

import java.io.Serializable;

/**
 * @author MAVR
 *
 */
public class ConsultaDatosGeneralesPolizaVO extends PolizaDTO implements Serializable{

	private static final long serialVersionUID = -8555353864912795413L;
	
	private String agente;

	public String getAgente() {
		return agente;
	}

	public void setAgente(String agente) {
		this.agente = agente;
	}
	
	
	
}