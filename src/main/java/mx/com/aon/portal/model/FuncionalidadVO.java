package mx.com.aon.portal.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by IntelliJ IDEA.
 * User: gabrielforradellas
 * Date: Oct 21, 2008
 * Time: 9:59:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class FuncionalidadVO {
	
	private String cdElemento;
	private String dsElemen;
	private String cdSisRol;
	private String dsSisRol;
	private String cdUsuario;
	private String dsNombre;
	private String cdFunciona;
	private String dsFunciona;
	private String swEstado;
	private String dsEstado;
	private String cdOpera;
	private String dsOpera;
	
	public String getCdOpera() {
		return cdOpera;
	}
	public void setCdOpera(String cdOpera) {
		this.cdOpera = cdOpera;
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
	public String getCdUsuario() {
		return cdUsuario;
	}
	public void setCdUsuario(String cdUsuario) {
		this.cdUsuario = cdUsuario;
	}
	public String getDsNombre() {
		return dsNombre;
	}
	public void setDsNombre(String dsNombre) {
		this.dsNombre = dsNombre;
	}
	public String getCdFunciona() {
		return cdFunciona;
	}
	public void setCdFunciona(String cdFunciona) {
		this.cdFunciona = cdFunciona;
	}
	public String getDsFunciona() {
		return dsFunciona;
	}
	public void setDsFunciona(String dsFunciona) {
		this.dsFunciona = dsFunciona;
	}
	public String getSwEstado() {
		return swEstado;
	}
	public void setSwEstado(String swEstado) {
		this.swEstado = swEstado;
	}
	public String getDsEstado() {
		return dsEstado;
	}
	public void setDsEstado(String dsEstado) {
		this.dsEstado = dsEstado;
	}
	public String getDsOpera() {
		return dsOpera;
	}
	public void setDsOpera(String dsOpera) {
		this.dsOpera = dsOpera;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}

}
