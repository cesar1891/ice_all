/**
 * 
 */
package mx.com.aon.portal.model.menuusuario;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author eflores
 * @date 26/05/2008
 *
 */
public class MenuVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private String cdMenu;
    
    private String dsMenu;
    
    private String cdElemento;
    
    private String dsElemento;
    
    private String cdPerson;
    
    private String dsPerson;
    
    private String cdRol;
    
    private String dsRol;
    
    private String cdUsuario;
    
    private String dsUsuario;
    
    private String cdEstado;
    
    private String dsEstado;
    
    private String cdTipoMenu;
    
    private String dsTipoMenu;
    
    /**
     * @return the cdElemento
     */
    public String getCdElemento() {
        return cdElemento;
    }

    /**
     * @param cdElemento the cdElemento to set
     */
    public void setCdElemento(String cdElemento) {
        this.cdElemento = cdElemento;
    }


    /**
     * @return the cdEstado
     */
    public String getCdEstado() {
        return cdEstado;
    }

    /**
     * @param cdEstado the cdEstado to set
     */
    public void setCdEstado(String cdEstado) {
        this.cdEstado = cdEstado;
    }

    /**
     * @return the cdMenu
     */
    public String getCdMenu() {
        return cdMenu;
    }

    /**
     * @param cdMenu the cdMenu to set
     */
    public void setCdMenu(String cdMenu) {
        this.cdMenu = cdMenu;
    }

    /**
     * @return the cdPerson
     */
    public String getCdPerson() {
        return cdPerson;
    }

    /**
     * @param cdPerson the cdPerson to set
     */
    public void setCdPerson(String cdPerson) {
        this.cdPerson = cdPerson;
    }

    /**
     * @return the cdRol
     */
    public String getCdRol() {
        return cdRol;
    }

    /**
     * @param cdRol the cdRol to set
     */
    public void setCdRol(String cdRol) {
        this.cdRol = cdRol;
    }

    /**
     * @return the cdTipoMenu
     */
    public String getCdTipoMenu() {
        return cdTipoMenu;
    }

    /**
     * @param cdTipoMenu the cdTipoMenu to set
     */
    public void setCdTipoMenu(String cdTipoMenu) {
        this.cdTipoMenu = cdTipoMenu;
    }

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
     * @return the dsMenu
     */
    public String getDsMenu() {
        return dsMenu;
    }

    /**
     * @param dsMenu the dsMenu to set
     */
    public void setDsMenu(String dsMenu) {
        this.dsMenu = dsMenu;
    }

    /**
     * @return the dsElemento
     */
    public String getDsElemento() {
        return dsElemento;
    }

    /**
     * @param dsElemento the dsElemento to set
     */
    public void setDsElemento(String dsElemento) {
        this.dsElemento = dsElemento;
    }

    /**
     * @return the dsPerson
     */
    public String getDsPerson() {
        return dsPerson;
    }

    /**
     * @param dsPerson the dsPerson to set
     */
    public void setDsPerson(String dsPerson) {
        this.dsPerson = dsPerson;
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

    /**
     * @return the dsEstado
     */
    public String getDsEstado() {
        return dsEstado;
    }

    /**
     * @param dsEstado the dsEstado to set
     */
    public void setDsEstado(String dsEstado) {
        this.dsEstado = dsEstado;
    }

    /**
     * @return the dsTipoMenu
     */
    public String getDsTipoMenu() {
        return dsTipoMenu;
    }

    /**
     * @param dsTipoMenu the dsTipoMenu to set
     */
    public void setDsTipoMenu(String dsTipoMenu) {
        this.dsTipoMenu = dsTipoMenu;
    }

	/**
	 * @return the dsRol
	 */
	public String getDsRol() {
		return dsRol;
	}

	/**
	 * @param dsRol the dsRol to set
	 */
	public void setDsRol(String dsRol) {
		this.dsRol = dsRol;
	}
}
