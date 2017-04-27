package mx.com.gseguros.wizard.model;

import java.io.Serializable;

public class HojaVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5287377547620035284L;
	private String id;
	private String text;
	private boolean leaf=true;
	private String cls="file";
	private String descripcion;
	private String funcion;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the cls
	 */
	public String getCls() {
		return cls;
	}
	/**
	 * @param cls the cls to set
	 */
	public void setCls(String cls) {
		this.cls = cls;
	}
	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return the funcion
	 */
	public String getFuncion() {
		return funcion;
	}
	/**
	 * @param funcion the funcion to set
	 */
	public void setFuncion(String funcion) {
		this.funcion = funcion;
	}
	/**
	 * @return the leaf
	 */
	public boolean isLeaf() {
		return leaf;
	}
	/**
	 * @param leaf the leaf to set
	 */
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	
}
