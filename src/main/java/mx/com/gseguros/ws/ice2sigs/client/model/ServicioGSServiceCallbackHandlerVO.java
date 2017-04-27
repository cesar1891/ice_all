package mx.com.gseguros.ws.ice2sigs.client.model;

import java.io.Serializable;

import mx.com.gseguros.portal.general.model.PolizaVO;

import org.apache.log4j.Logger;

public class ServicioGSServiceCallbackHandlerVO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(ServicioGSServiceCallbackHandlerVO.class);
	
	private String usuario;
	
	private PolizaVO polizaVO;

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public PolizaVO getPolizaVO() {
		return polizaVO;
	}

	public void setPolizaVO(PolizaVO polizaVO) {
		this.polizaVO = polizaVO;
	}
	
}