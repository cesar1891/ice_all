/**
 * 
 */
package mx.com.gseguros.portal.general.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author eflores
 * @date 26/05/2008
 *
 */
public class UsuarioVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String cdUsuario;
    private String dsUsuario;
    private String dsNombre;
    private String dsNombre1;
    private String dsApellido;
    private String dsApellido1;
    private String otSexo;
    private String dSexo;
    private String feNacimi;
    private String cdrfc;
    private String curp;
    private String dsEmail;
    private String cdrol;
    private String esAgente;
    private String esAdmin;
    private String cdunieco;
    private String cdunisld;
    
    /**
     * Para Agente, inicio vigencia y fin licencia
     */
    private String feini;
    private String fefinlic;
    
    private String swActivo;
    
    /**
     * Para Agente: Clase y Estatus
     * claseag
     * statusag
     * Beto 19mayo15
     */
    private String claseag; 
    private String statusag;
    private String cdoficin;
    private String cdbroker;
    /**
     * Se agrega campo: Empresa
     * cdempresa
     */
    private String cdempresa;
    
    private String swsusmat;
    
    /**
     * @return the cdUsuario
     */
    public String getCdUsuario() {
        return cdUsuario;
    }

    /**
     * @param cdUsuario the cdUsuario to set
     */
    public void setCdUsuario(String cdUsuario) {
        this.cdUsuario = cdUsuario;
    }

    /**
     * @return the dsUsuario
     */
    public String getDsUsuario() {
        return dsUsuario;
    }

    /**
     * @param dsUsuario the dsUsuario to set
     */
    public void setDsUsuario(String dsUsuario) {
        this.dsUsuario = dsUsuario;
    }
    
    /**
     * @return String
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

	public String getDsNombre() {
		return dsNombre;
	}

	public void setDsNombre(String dsNombre) {
		this.dsNombre = dsNombre;
	}

	public String getDsNombre1() {
		return dsNombre1;
	}

	public void setDsNombre1(String dsNombre1) {
		this.dsNombre1 = dsNombre1;
	}

	public String getDsApellido() {
		return dsApellido;
	}

	public void setDsApellido(String dsApellido) {
		this.dsApellido = dsApellido;
	}

	public String getDsApellido1() {
		return dsApellido1;
	}

	public void setDsApellido1(String dsApellido1) {
		this.dsApellido1 = dsApellido1;
	}

	public String getOtSexo() {
		return otSexo;
	}

	public void setOtSexo(String otSexo) {
		this.otSexo = otSexo;
	}

	public String getdSexo() {
		return dSexo;
	}

	public void setdSexo(String dSexo) {
		this.dSexo = dSexo;
	}

	public String getFeNacimi() {
		return feNacimi;
	}

	public void setFeNacimi(String feNacimi) {
		this.feNacimi = feNacimi;
	}

	public String getCdrfc() {
		return cdrfc;
	}

	public void setCdrfc(String cdrfc) {
		this.cdrfc = cdrfc;
	}

	public String getDsEmail() {
		return dsEmail;
	}

	public void setDsEmail(String dsEmail) {
		this.dsEmail = dsEmail;
	}

	public String getCdrol() {
		return cdrol;
	}

	public void setCdrol(String cdrol) {
		this.cdrol = cdrol;
	}

	public String getCurp() {
		return curp;
	}

	public void setCurp(String curp) {
		this.curp = curp;
	}

	public String getEsAgente() {
		return esAgente;
	}

	public void setEsAgente(String esAgente) {
		this.esAgente = esAgente;
	}

	public String getEsAdmin() {
		return esAdmin;
	}

	public void setEsAdmin(String esAdmin) {
		this.esAdmin = esAdmin;
	}

	public String getCdunieco() {
		return cdunieco;
	}

	public void setCdunieco(String cdunieco) {
		this.cdunieco = cdunieco;
	}

	public String getFeini() {
		return feini;
	}

	public void setFeini(String feini) {
		this.feini = feini;
	}

	public String getFefinlic() {
		return fefinlic;
	}

	public void setFefinlic(String fefinlic) {
		this.fefinlic = fefinlic;
	}

	public String getSwActivo() {
		return swActivo;
	}

	public void setSwActivo(String swActivo) {
		this.swActivo = swActivo;
	}
	
	public String getClaseag() {
		return claseag;
	}

	public void setClaseag(String claseag) {
		this.claseag = claseag;
	}

	public String getStatusag() {
		return statusag;
	}

	public void setStatusag(String statusag) {
		this.statusag = statusag;
	}

	public String getCdoficin() {
		return cdoficin;
	}

	public void setCdoficin(String cdoficin) {
		this.cdoficin = cdoficin;
	}

	public String getCdbroker() {
		return cdbroker;
	}

	public void setCdbroker(String cdbroker) {
		this.cdbroker = cdbroker;
	}

	public String getCdempresa() {
		return cdempresa;
	}

	public void setCdempresa(String cdempresa) {
		this.cdempresa = cdempresa;
	}

    public String getCdunisld() {
        return cdunisld;
    }

    public void setCdunisld(String cdunisld) {
        this.cdunisld = cdunisld;
    }

    public String getSwsusmat() {
        return swsusmat;
    }

    public void setSwsusmat(String swsusmat) {
        this.swsusmat = swsusmat;
    }
}
