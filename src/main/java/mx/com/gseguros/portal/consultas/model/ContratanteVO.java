package mx.com.gseguros.portal.consultas.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MAVR
 *
 */
public class ContratanteVO implements Serializable{

	private static final long serialVersionUID = -8555353864912795413L;
	
	private String razonsocial;
	private String zonacosto;
	private String domicilio;	
	private String ciudad;
	private String estado;
	private String representante;
	private String telefono1;
	private String area1;
	private String puesto;
	private String telefono2;	
	private String area2;
	private String giro;
	private String fax;	
	private String areafax;
	private String relacion;
	private String codigopostal;
	private String tipocontratante;
	private String rfc;	
	private String imss;
	
	
	public String getRazonsocial() {
		return razonsocial;
	}


	public void setRazonsocial(String razonsocial) {
		this.razonsocial = razonsocial;
	}


	public String getZonacosto() {
		return zonacosto;
	}


	public void setZonacosto(String zonacosto) {
		this.zonacosto = zonacosto;
	}


	public String getDomicilio() {
		return domicilio;
	}


	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}


	public String getCiudad() {
		return ciudad;
	}


	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public String getRepresentante() {
		return representante;
	}


	public void setRepresentante(String representante) {
		this.representante = representante;
	}


	public String getTelefono1() {
		return telefono1;
	}


	public void setTelefono1(String telefono1) {
		this.telefono1 = telefono1;
	}


	public String getArea1() {
		return area1;
	}


	public void setArea1(String area1) {
		this.area1 = area1;
	}


	public String getPuesto() {
		return puesto;
	}


	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}


	public String getTelefono2() {
		return telefono2;
	}


	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}


	public String getArea2() {
		return area2;
	}


	public void setArea2(String area2) {
		this.area2 = area2;
	}


	public String getGiro() {
		return giro;
	}


	public void setGiro(String giro) {
		this.giro = giro;
	}


	public String getFax() {
		return fax;
	}


	public void setFax(String fax) {
		this.fax = fax;
	}


	public String getAreafax() {
		return areafax;
	}


	public void setAreafax(String areafax) {
		this.areafax = areafax;
	}


	public String getRelacion() {
		return relacion;
	}


	public void setRelacion(String relacion) {
		this.relacion = relacion;
	}


	public String getCodigopostal() {
		return codigopostal;
	}


	public void setCodigopostal(String codigopostal) {
		this.codigopostal = codigopostal;
	}


	public String getTipocontratante() {
		return tipocontratante;
	}


	public void setTipocontratante(String tipocontratante) {
		this.tipocontratante = tipocontratante;
	}


	public String getRfc() {
		return rfc;
	}


	public void setRfc(String rfc) {
		this.rfc = rfc;
	}


	public String getImss() {
		return imss;
	}


	public void setImss(String imss) {
		this.imss = imss;
	}


	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}		
}