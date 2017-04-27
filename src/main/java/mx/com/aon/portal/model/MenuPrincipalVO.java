package mx.com.aon.portal.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
/**
 * 
 * @author sergio.ramirez
 *
 */
public class MenuPrincipalVO implements Serializable{

	/**
	 *Serial Version
	 */
	private static final long serialVersionUID = 5870634432218189288L;
	/**
	 * @see 
	 * Atributo declarado para el menu principal indicando el tipo de objeto.
	 */
	private String xtype;
	/**
	 * @see
	 * Atributo declarado para el menu y para los sub-menus
	 * de ese menu indicando el texto para cada uno de ellos.
	 */
	private String text;
	/**
	 * @see
	 * Atributo declarado para el menu principal indicando el icon-image de ese texto. 
	 */
	private String icon;
	/**
	 * @see
	 * Atributo declarado para el menu principal indicando el estilo de ese menu.
	 */
	private String cls;
	/**
	 * @see
	 * Atributo declarado para los sub-menus del menu principal para el manejo de eventos. 
	 */
	private String handler;
	/**
	 * @see
	 * Atributo declarado para los sub-menus del menu principal para el manejo de actions.
	 */
	private String navURL;
	/**
	 *@see
	 *Atributo declarado para los sub-menus del menu principal para el manejo de url.
	 */
	private String href;
	/**
	 *@see
	 *Atributo declarado para los sub-menus del menu principal para el manejo del target.
	 */
	private String hrefTarget;	
	/**
	 * Atributo declarado para guardar el tipo de ventana para mostrar la pantalla
	 */
	private String swTipdes;
	/**
	 * @see
	 * Atributo declarado para identificar el ramo del menu a cargar.
	 */
	private String claveRamo;
	/**
	 * @see
	 * Atributo que identifica al nivel del submenu.
	 */
	private String nivel;
	/**
	 * @see
	 * Atributo que identica el padre 
	 */
	private String nivelPadre;	
	/**
	 * @see
	 * atributo claveTiposituacion para transferencia de datos
	 */
	private String claveTipoSituacion;
	/**
	 * @see
	 * Atributo tipo objeto para almacenar los atributos del sub-menu.
	 */
	private Object[] menu;
	
	private String cdTitulo;
	
	
    /**
     * @return String
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }	
	/**
	 *Getters and Setters. 
	 * @return
	 */
	public String getXtype() {
		return xtype;
	}
	/**
	 * 
	 * @param xtype
	 */
	public void setXtype(String xtype) {
		this.xtype = xtype;
	}
	/**
	 * 
	 * @return
	 */
	public String getText() {
		return text;
	}
	/**
	 * 
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * 
	 * @return
	 */
	public String getIcon() {
		return icon;
	}
	/**
	 * 
	 * @param icon
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	/**
	 * 
	 * @return
	 */
	public String getCls() {
		return cls;
	}
	/**
	 * 
	 * @param cls
	 */
	public void setCls(String cls) {
		this.cls = cls;
	}
	/**
	 * 
	 * @return
	 */
	public String getHandler() {
		return handler;
	}
	/**
	 * 
	 * @param handler
	 */
	public void setHandler(String handler) {
		this.handler = handler;
	}
	/**
	 * 
	 * @return
	 */
	public String getNavURL() {
		return navURL;
	}
	/**
	 * 
	 * @param navURL
	 */
	public void setNavURL(String navURL) {
		this.navURL = navURL;
	}
	/**
	 * 
	 * @return
	 */
	public Object[] getMenu() {
		return menu;
	}
	/**
	 * 
	 * @param menu
	 */
	public void setMenu(Object[] menu) {
		this.menu = menu;
	}
	/**
	 * 
	 * @return
	 */
	public String getHref() {
		return href;
	}
	/**
	 * 
	 * @param href
	 */
	public void setHref(String href) {
		this.href = href;
	}
	/**
	 * 
	 * @return
	 */
	public String getClaveRamo() {
		return claveRamo;
	}
	/**
	 * 
	 * @param claveRamo
	 */
	public void setClaveRamo(String claveRamo) {
		this.claveRamo = claveRamo;
	}
	/**
	 * 
	 * @return
	 */
	public String getNivel() {
		return nivel;
	}
	/**
	 * 
	 * @param nivel
	 */
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	/**
	 * 
	 * @return
	 */
	public String getNivelPadre() {
		return nivelPadre;
	}
	/**
	 * 
	 * @param nivelPadre
	 */
	public void setNivelPadre(String nivelPadre) {
		this.nivelPadre = nivelPadre;
	}
	/**
	 * 
	 * @return
	 */
	public String getClaveTipoSituacion() {
		return claveTipoSituacion;
	}
	/**
	 * 
	 * @param claveTipoSituacion
	 */
	public void setClaveTipoSituacion(String claveTipoSituacion) {
		this.claveTipoSituacion = claveTipoSituacion;
	}
	public String getCdTitulo() {
		return cdTitulo;
	}
	public void setCdTitulo(String cdTitulo) {
		this.cdTitulo = cdTitulo;
	}
	public String getHrefTarget() {
		return hrefTarget;
	}
	public void setHrefTarget(String hrefTarget) {
		this.hrefTarget = hrefTarget;
	}
	public String getSwTipdes() {
		return swTipdes;
	}
	public void setSwTipdes(String swTipdes) {
		this.swTipdes = swTipdes;
	}
	
	
}
