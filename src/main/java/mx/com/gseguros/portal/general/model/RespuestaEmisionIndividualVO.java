package mx.com.gseguros.portal.general.model;

import java.io.Serializable;

public class RespuestaEmisionIndividualVO implements Serializable {

	private static final long serialVersionUID = -6409943773466631318L;
	
	private String nmpoliza;
	
	private String nmsuplem;
	
	private String cdideper;
	
	private String nmpoliex;
	
	private boolean necesitaAutorizacion;
	

	public String getNmpoliza() {
		return nmpoliza;
	}

	public void setNmpoliza(String nmpoliza) {
		this.nmpoliza = nmpoliza;
	}

	public String getNmsuplem() {
		return nmsuplem;
	}

	public void setNmsuplem(String nmsuplem) {
		this.nmsuplem = nmsuplem;
	}

	public String getCdideper() {
		return cdideper;
	}

	public void setCdideper(String cdideper) {
		this.cdideper = cdideper;
	}

	public String getNmpoliex() {
		return nmpoliex;
	}

	public void setNmpoliex(String nmpoliex) {
		this.nmpoliex = nmpoliex;
	}

	public boolean isNecesitaAutorizacion() {
		return necesitaAutorizacion;
	}

	public void setNecesitaAutorizacion(boolean necesitaAutorizacion) {
		this.necesitaAutorizacion = necesitaAutorizacion;
	}
	
}