package mx.com.gseguros.portal.consultas.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MAVR
 *
 */
public class AseguradoDetalleVO implements Serializable{

	private static final long serialVersionUID = -8555353864912795413L;

	/**
	 * Codigo de la persona
	 */
	private String cdperson;	
	private String edad;
	private String identidad;
	private String nombre;
	private String tiposangre;
	private String antecedentes;
	private String oii;
	private String telefono;
	private String direccion;
	private String correo;
	private String mcp;
	private String mcpespecialidad;
	private String ocp;
	private String ocpespecialidad;
	
	

	public String getCdperson() {
		return cdperson;
	}

	public void setCdperson(String cdperson) {
		this.cdperson = cdperson;
	}

	public String getEdad() {
		return edad;
	}

	public void setEdad(String edad) {
		this.edad = edad;
	}

	public String getIdentidad() {
		return identidad;
	}

	public void setIdentidad(String identidad) {
		this.identidad = identidad;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTiposangre() {
		return tiposangre;
	}

	public void setTiposangre(String tiposangre) {
		this.tiposangre = tiposangre;
	}

	public String getAntecedentes() {
		return antecedentes;
	}

	public void setAntecedentes(String antecedentes) {
		this.antecedentes = antecedentes;
	}

	public String getOii() {
		return oii;
	}

	public void setOii(String oii) {
		this.oii = oii;
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

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getMcp() {
		return mcp;
	}

	public void setMcp(String mcp) {
		this.mcp = mcp;
	}

	public String getMcpespecialidad() {
		return mcpespecialidad;
	}

	public void setMcpespecialidad(String mcpespecialidad) {
		this.mcpespecialidad = mcpespecialidad;
	}

	public String getOcp() {
		return ocp;
	}

	public void setOcp(String ocp) {
		this.ocp = ocp;
	}

	public String getOcpespecialidad() {
		return ocpespecialidad;
	}

	public void setOcpespecialidad(String ocpespecialidad) {
		this.ocpespecialidad = ocpespecialidad;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}		
}