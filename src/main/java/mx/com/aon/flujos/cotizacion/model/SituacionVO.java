package mx.com.aon.flujos.cotizacion.model;

import java.io.Serializable;

public class SituacionVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4740351709294063143L;

	/**
	 * CLAVE TIPO SITUAC
	 */
	private String cdTipsit;
	
	/**
     *CLAVE DEL PLAN 
     */
    private String cdPlan;

    /**
	 * NUMERO DE SITUAC
	 */
	private String nmSituac;

	
	/**
	 * Compania Aseguradora
	 */
	private String compAsegur;

	
	
	public String getCdTipsit() {
		return cdTipsit;
	}

	public void setCdTipsit(String cdTipsit) {
		this.cdTipsit = cdTipsit;
	}

	public String getCdPlan() {
		return cdPlan;
	}

	public void setCdPlan(String cdPlan) {
		this.cdPlan = cdPlan;
	}

	public String getNmSituac() {
		return nmSituac;
	}

	public void setNmSituac(String nmSituac) {
		this.nmSituac = nmSituac;
	}

	public String getCompAsegur() {
		return compAsegur;
	}

	public void setCompAsegur(String compAsegur) {
		this.compAsegur = compAsegur;
	}
	
}
