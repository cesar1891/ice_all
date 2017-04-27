package mx.com.gseguros.portal.emision.model;

import java.io.Serializable;

public class EmisionVO implements Serializable {

	private static final long serialVersionUID = -5475666674004879327L;

	private String nmpoliza;
	
	private String nmpoliex;
	
	private String nmsuplem;
	
	private String esDxN;
	
	private String message;
	
	private String cdideper;
	

	public String getNmpoliza() {
		return nmpoliza;
	}

	public void setNmpoliza(String nmpoliza) {
		this.nmpoliza = nmpoliza;
	}

	public String getNmpoliex() {
		return nmpoliex;
	}

	public void setNmpoliex(String nmpoliex) {
		this.nmpoliex = nmpoliex;
	}

	public String getNmsuplem() {
		return nmsuplem;
	}

	public void setNmsuplem(String nmsuplem) {
		this.nmsuplem = nmsuplem;
	}

	public String getEsDxN() {
		return esDxN;
	}

	public void setEsDxN(String esDxN) {
		this.esDxN = esDxN;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCdideper() {
		return cdideper;
	}

	public void setCdideper(String cdideper) {
		this.cdideper = cdideper;
	}
	
}