package mx.com.aon.portal.model;

import java.io.Serializable;

public class CondicionCoberturaVO implements Serializable{
	
	private static final long serialVersionUID = 7588500634327948238L;
	
	private String cdcondic;
	private String dscondic;
	public String getCdcondic() {
		return cdcondic;
	}
	public void setCdcondic(String cdcondic) {
		this.cdcondic = cdcondic;
	}
	public String getDscondic() {
		return dscondic;
	}
	public void setDscondic(String dscondic) {
		this.dscondic = dscondic;
	}
	
	
}
