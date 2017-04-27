package mx.com.aon.portal.model;

public class ConfiguracionVO {
	private String cdElemen;
	private String dsElemen;
	private String cdConfig;
	private String dsConfig;
	private String cdEstado;
	private String dsEstado;
	private String cdLinope;
	private String dsLinope;


	public String getDsConfig() {
		return dsConfig;
	}
	public void setDsConfig(String dsConfig) {
		this.dsConfig = dsConfig;
	}
	public String getDsEstado() {
		return dsEstado;
	}
	public void setDsEstado(String dsEstado) {
		this.dsEstado = dsEstado;
	}
	public String getDsLinope() {
		return dsLinope;
	}
	public void setDsLinope(String dsLinope) {
		this.dsLinope = dsLinope;
	}


    public String getCdEstado() {
        return cdEstado;
    }

    public void setCdEstado(String cdEstado) {
        this.cdEstado = cdEstado;
    }

    public String getCdLinope() {
        return cdLinope;
    }

    public void setCdLinope(String cdLinope) {
        this.cdLinope = cdLinope;
    }
	public String getCdConfig() {
		return cdConfig;
	}
	public void setCdConfig(String cdConfig) {
		this.cdConfig = cdConfig;
	}
	public String getCdElemen() {
		return cdElemen;
	}
	public void setCdElemen(String cdElemen) {
		this.cdElemen = cdElemen;
	}
	public String getDsElemen() {
		return dsElemen;
	}
	public void setDsElemen(String dsElemen) {
		this.dsElemen = dsElemen;
	}
}
