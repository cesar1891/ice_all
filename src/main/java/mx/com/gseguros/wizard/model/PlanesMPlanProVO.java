package mx.com.gseguros.wizard.model;
/**
 * Estructura para obtener datos del plan
 * 
 * @param cdRamo: Producto
 * @param cdPlan: Plan
 * @param cdTipSit: Tipo de Situacion
 * @param dsTipSit
 * @param cdGarant: Garantia
 * @param dsGarant
 * @param swOblig: Es Obligatorio?
 */
public class PlanesMPlanProVO {
	
	private String cdRamo;
	private String cdPlan;
	private String cdTipSit;
	private String dsTipSit;
	private String cdGarant;
	private String dsGarant;
	private String swOblig;


    public String getCdRamo() {
        return cdRamo;
    }

    public void setCdRamo(String cdRamo) {
        this.cdRamo = cdRamo;
    }

    public String getCdPlan() {
        return cdPlan;
    }

    public void setCdPlan(String cdPlan) {
        this.cdPlan = cdPlan;
    }

    public String getCdTipSit() {
        return cdTipSit;
    }

    public void setCdTipSit(String cdTipSit) {
        this.cdTipSit = cdTipSit;
    }

    public String getCdGarant() {
        return cdGarant;
    }

    public void setCdGarant(String cdGarant) {
        this.cdGarant = cdGarant;
    }

    public String getSwOblig() {
        return swOblig;
    }

    public void setSwOblig(String swOblig) {
        this.swOblig = swOblig;
    }

	public String getDsTipSit() {
		return dsTipSit;
	}

	public void setDsTipSit(String dsTipSit) {
		this.dsTipSit = dsTipSit;
	}

	public String getDsGarant() {
		return dsGarant;
	}

	public void setDsGarant(String dsGarant) {
		this.dsGarant = dsGarant;
	}
}
