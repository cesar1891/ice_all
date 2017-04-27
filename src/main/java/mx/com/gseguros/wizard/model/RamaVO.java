package mx.com.gseguros.wizard.model;

import java.io.Serializable;

public class RamaVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7680496740287557462L;
	private boolean allowDelete = true;
	private String text;
	private String id;
	private String href;
	private String hrefTarget = "center-iframe";
	private boolean leaf = false;
	private String qtip;
	private String qtipTitle;
	private String cls = "file";
	private Object[] children;
	private boolean expanded;
	
	private String codigoObjeto;
	
	private String tipoObjeto;
	
	private int nivel;
	
	private int posicion;
	

	/**
	 * @return the allowDelete
	 */
	public boolean isAllowDelete() {
		return allowDelete;
	}

	/**
	 * @param allowDelete
	 *            the allowDelete to set
	 */
	public void setAllowDelete(boolean allowDelete) {
		this.allowDelete = allowDelete;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the href
	 */
	public String getHref() {
		return href;
	}

	/**
	 * @param href
	 *            the href to set
	 */
	public void setHref(String href) {
		this.href = href;
	}

	/**
	 * @return the hrefTarget
	 */
	public String getHrefTarget() {
		return hrefTarget;
	}

	/**
	 * @param hrefTarget
	 *            the hrefTarget to set
	 */
	public void setHrefTarget(String hrefTarget) {
		this.hrefTarget = hrefTarget;
	}

	/**
	 * @return the leaf
	 */
	public boolean isLeaf() {
		return leaf;
	}

	/**
	 * @param leaf
	 *            the leaf to set
	 */
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	/**
	 * @return the qtip
	 */
	public String getQtip() {
		return qtip;
	}

	/**
	 * @param qtip
	 *            the qtip to set
	 */
	public void setQtip(String qtip) {
		this.qtip = qtip;
	}

	/**
	 * @return the qtipTitle
	 */
	public String getQtipTitle() {
		return qtipTitle;
	}

	/**
	 * @param qtipTitle
	 *            the qtipTitle to set
	 */
	public void setQtipTitle(String qtipTitle) {
		this.qtipTitle = qtipTitle;
	}

	/**
	 * @return the cls
	 */
	public String getCls() {
		return cls;
	}

	/**
	 * @param cls
	 *            the cls to set
	 */
	public void setCls(String cls) {
		this.cls = cls;
	}

	/**
	 * @return the children
	 */
	public Object[] getChildren() {
		return children;
	}

	/**
	 * @param children
	 *            the children to set
	 */
	public void setChildren(Object[] children) {
		this.children = children;
	}

	/**
	 * @return the expanded
	 */
	public boolean isExpanded() {
		return expanded;
	}

	/**
	 * @param expanded
	 *            the expanded to set
	 */
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	/**
	 * @return the tipoObjeto
	 */
	public String getTipoObjeto() {
		return tipoObjeto;
	}

	/**
	 * @param tipoObjeto the tipoObjeto to set
	 */
	public void setTipoObjeto(String tipoObjeto) {
		this.tipoObjeto = tipoObjeto;
	}

	/**
	 * @return the nivel
	 */
	public int getNivel() {
		return nivel;
	}

	/**
	 * @param nivel the nivel to set
	 */
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	/**
	 * @return the posicion
	 */
	public int getPosicion() {
		return posicion;
	}

	/**
	 * @param posicion the posicion to set
	 */
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

	/**
	 * @return the codigoObjeto
	 */
	public String getCodigoObjeto() {
		return codigoObjeto;
	}

	/**
	 * @param codigoObjeto the codigoObjeto to set
	 */
	public void setCodigoObjeto(String codigoObjeto) {
		this.codigoObjeto = codigoObjeto;
	}
}
