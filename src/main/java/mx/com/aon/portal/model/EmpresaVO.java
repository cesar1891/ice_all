package mx.com.aon.portal.model;

import java.io.Serializable;

public class EmpresaVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4252924787861752335L;
	private String empresaId;
	private String elementoId;
	private String nombre;
	
	
	//Gettes && Setters
	public String getEmpresaId() { return empresaId; }
	public void setEmpresaId(String empresaId) { this.empresaId = empresaId; }
	
	public String getElementoId() { return elementoId; }
	public void setElementoId(String elementoId) { this.elementoId = elementoId; }
	
	public String getNombre() {	return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }
	
}
