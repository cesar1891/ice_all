package mx.com.gseguros.portal.consultas.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MAVR
 *
 */
public class ConsultaDatosTitularVO implements Serializable{

	private static final long serialVersionUID = -8555353864912795413L;
	
	private String identificacion;
	private String nombre;
	private String fenacimiento;	
	private String edad;
	private String sexo;
	private String edocivil;
	private String feingreso;
	private String telefono;
	private String direccion;
	private String colonia;	
	private String codigopostal;
	private String celular;
	private String email;
				
	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFenacimiento() {
		return fenacimiento;
	}

	public void setFenacimiento(String fenacimiento) {
		this.fenacimiento = fenacimiento;
	}

	public String getEdad() {
		return edad;
	}

	public void setEdad(String edad) {
		this.edad = edad;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getEdocivil() {
		return edocivil;
	}

	public void setEdocivil(String edocivil) {
		this.edocivil = edocivil;
	}

	public String getFeingreso() {
		return feingreso;
	}

	public void setFeingreso(String feingreso) {
		this.feingreso = feingreso;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getColonia() {
		return colonia;
	}

	public void setColonia(String colonia) {
		this.colonia = colonia;
	}

	public String getCodigopostal() {
		return codigopostal;
	}

	public void setCodigopostal(String codigopostal) {
		this.codigopostal = codigopostal;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}		
}