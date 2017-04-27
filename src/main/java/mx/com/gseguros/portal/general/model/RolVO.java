package mx.com.gseguros.portal.general.model;

import java.io.Serializable;
import java.util.List;

import mx.com.aon.portal.model.EmpresaVO;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Rol de sistema
 * @author Ricardo
 *
 */
public class RolVO implements Serializable {

	private static final long serialVersionUID = -6843784051408138330L;

	private String clave;
	
	private String descripcion;
	
	private String condicion;
	
	private boolean activado;
	
	private List<EmpresaVO> empresas;


	// Getters && Setters

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCondicion() {
		return condicion;
	}

	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}

	public boolean isActivado() {
		return activado;
	}

	public void setActivado(boolean activado) {
		this.activado = activado;
	}

	public List<EmpresaVO> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<EmpresaVO> empresas) {
		this.empresas = empresas;
	}
	
	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
	
}