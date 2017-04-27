package mx.com.gseguros.confpantallas.model;

import java.io.Serializable;

public class NodoVO implements Serializable {
	
	private static final long serialVersionUID = 3995593350601624055L;

	public NodoVO() {
		super();
	}
	
	public NodoVO(String id, String text, boolean leaf) {
		super();
		this.id = id;
		this.text = text;
		this.leaf = leaf;
	}

	
	private String id;
	
	private String text;
	
	private boolean leaf;
	
	
	//Getters and setters:

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

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

}