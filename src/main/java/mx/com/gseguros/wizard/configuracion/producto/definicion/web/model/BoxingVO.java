package mx.com.gseguros.wizard.configuracion.producto.definicion.web.model;

import java.io.Serializable;

import mx.com.gseguros.wizard.configuracion.producto.definicion.model.ClausulaVO;


public class BoxingVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3330518708773175968L;
	
	ClausulaVO c;

	/**
	 * @return the c
	 */
	public ClausulaVO getC() {
		return c;
	}

	/**
	 * @param c the c to set
	 */
	public void setC(ClausulaVO c) {
		this.c = c;
	}
}
