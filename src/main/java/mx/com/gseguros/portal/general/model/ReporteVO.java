package mx.com.gseguros.portal.general.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ReporteVO implements Serializable {

	private static final long serialVersionUID = 1710915269673004791L;

	private String cdReporte;
	
	private String dsReporte;	
	
	private String cdPantalla;	
	
	private String cdSeccion;	
	
	
	public ReporteVO() {
		super();
	}

	public ReporteVO(String cdReporte) {
		super();
		this.cdReporte = cdReporte;
	}

	public ReporteVO(String cdReporte, String dsReporte) {
		super();
		this.cdReporte = cdReporte;
		this.dsReporte = dsReporte;
	}


	//Getters and setters:
	public String getCdReporte() {
		return cdReporte;
	}

	public void setCdReporte(String cdReporte) {
		this.cdReporte = cdReporte;
	}

	public String getDsReporte() {
		return dsReporte;
	}

	public void setDsReporte(String dsReporte) {
		this.dsReporte = dsReporte;
	}
	
	/**
     * @return String
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

	public String getCdPantalla() {
		return cdPantalla;
	}

	public void setCdPantalla(String cdPantalla) {
		this.cdPantalla = cdPantalla;
	}

	public String getCdSeccion() {
		return cdSeccion;
	}

	public void setCdSeccion(String cdSeccion) {
		this.cdSeccion = cdSeccion;
	}
}
