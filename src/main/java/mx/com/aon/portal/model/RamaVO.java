package mx.com.aon.portal.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class RamaVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6461576745397117855L;
	private String id;
	private String text;
	private String codigoObjeto;
	private boolean leaf = true;
	private boolean allowDelete = false;
	private Object[] children;
	private boolean expanded;
	private String nick;
	private String name;
	private String claveRol;
	private String dsRol;
	private String cdElemento;
	
	
	
	public String getCdElemento() {
		return cdElemento;
	}
	public void setCdElemento(String cdElemento) {
		this.cdElemento = cdElemento;
	}
	public String getClaveRol() {
		return claveRol;
	}
	public void setClaveRol(String claveRol) {
		this.claveRol = claveRol;
	}
	public String getDsRol() {
		return dsRol;
	}
	public void setDsRol(String dsRol) {
		this.dsRol = dsRol;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getCodigoObjeto() {
		return codigoObjeto;
	}
	public void setCodigoObjeto(String ocodigoObjetodigoObjeto) {
		this.codigoObjeto = ocodigoObjetodigoObjeto;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public boolean isAllowDelete() {
		return allowDelete;
	}
	public void setAllowDelete(boolean allowDelete) {
		this.allowDelete = allowDelete;
	}
	public Object[] getChildren() {
		return children;
	}
	public void setChildren(Object[] children) {
		this.children = children;
	}
	public boolean isExpanded() {
		return expanded;
	}
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	
    /**
     * @return String
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
	
}
