package mx.com.aon.portal.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by IntelliJ IDEA.
 * User: gabrielforradellas
 * Date: Nov 7, 2008
 * Time: 5:21:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class UsuarioRolEmpresaVO {

    private String cdUsuario;
    private String cdPerson;
    private String dsUsuario;
    private String cdElemento;
    private String dsElemen;
    private String cdSisRol;
    private String dsSisRol;
    /**
     * Unidad economica 
     * TODO: Eliminar propiedad, analizar en donde debe ir
     */
    private String cdUnieco;


    public String getCdUsuario() {
        return cdUsuario;
    }

    public void setCdUsuario(String cdUsuario) {
        this.cdUsuario = cdUsuario;
    }

    public String getCdPerson() {
        return cdPerson;
    }

    public void setCdPerson(String cdPerson) {
        this.cdPerson = cdPerson;
    }

    public String getDsUsuario() {
        return dsUsuario;
    }

    public void setDsUsuario(String dsUsuario) {
        this.dsUsuario = dsUsuario;
    }

    public String getCdElemento() {
        return cdElemento;
    }

    public void setCdElemento(String cdElemento) {
        this.cdElemento = cdElemento;
    }

    public String getDsElemen() {
        return dsElemen;
    }

    public void setDsElemen(String dsElemen) {
        this.dsElemen = dsElemen;
    }

    public String getCdSisRol() {
        return cdSisRol;
    }

    public void setCdSisRol(String cdSisRol) {
        this.cdSisRol = cdSisRol;
    }

    public String getDsSisRol() {
        return dsSisRol;
    }

    public void setDsSisRol(String dsSisRol) {
        this.dsSisRol = dsSisRol;
    }
    
    public String getCdUnieco() {
		return cdUnieco;
	}

	public void setCdUnieco(String cdUnieco) {
		this.cdUnieco = cdUnieco;
	}

	/**
     * @return String
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }	
}
