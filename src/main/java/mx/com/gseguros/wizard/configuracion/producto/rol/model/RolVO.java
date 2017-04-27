package mx.com.gseguros.wizard.configuracion.producto.rol.model;

import java.io.Serializable;

/**
 * Clase VO que contien los datos del rol para asociarlo a un inciso.
 * 
 * @since 1.0
 * @author <a href="mailto:edgar.perez@biosnettcs.com">Edgar Perez</a>
 * @version $Id$
 */
public class RolVO implements Serializable{
    
	private static final long serialVersionUID = 8740140027570650459L;
	
	/**
	 * Codigo de rol asociado al inciso.
	 */
	private String codigoRol;
	
	/**
	 * Descripcion del rol asociado al inciso.
	 */
	private String descripcionRol;
    
	/**
	 * Codigo de ramo del rol asociado al inciso.
	 */
	private String codigoRamo;
    
	/**
	 * Codigo de nivel del rol asociado al inciso.
	 */
	private String codigoNivel;
    
	/**
	 * Codigo de composicion del rol asociado al inciso.
	 */
	private String codigoComposicion;
	
	/**
	 * Descripcion de composicion del rol asociado al inciso.
	 */
	private String descripcionComposicion;
    
	/**
	 * Numero maximo del rol asociado al inciso.
	 */
	private String numeroMaximo;
    
	/**
	 * Codigo de tipo de situacion del rol asociado al inciso.
	 */
	private String cdtipsit;
    
	/**
	 * Obligatoriedad del rol asociado al inciso.
	 */
	private String switchDomicilio;

	//GETTERS & SETTERS
	
	/**
	 * Retorna codigo de rol asociado al inciso.
	 * @return the codigoRol
	 */
	public String getCodigoRol() {
		return codigoRol;
	}
	
	/**
	 * Asigna codigo de rol asociado al inciso.
	 * @param codigoRol the codigoRol to set
	 */
	public void setCodigoRol(String codigoRol) {
		this.codigoRol = codigoRol;
	}
	
	/**
	 * Retorna codigo de ramo del rol asociado al inciso.
	 * @return the codigoRamo
	 */
	public String getCodigoRamo() {
		return codigoRamo;
	}
	
	/**
	 * Asigna codigo de ramo asociado al inciso.
	 * @param codigoRamo the codigoRamo to set
	 */
	public void setCodigoRamo(String codigoRamo) {
		this.codigoRamo = codigoRamo;
	}
	
	/**
	 * Retorna codigo de nivel del rol asociado al inciso.
	 * @return the codigoNivel
	 */
	public String getCodigoNivel() {
		return codigoNivel;
	}
	
	/**
	 * Asigna codigo de nivel asociado al inciso.
	 * @param codigoNivel the codigoNivel to set
	 */
	public void setCodigoNivel(String codigoNivel) {
		this.codigoNivel = codigoNivel;
	}
	
	/**
	 * Retorna codigo de composicion del rol asociado al inciso.
	 * @return the codigoComposicion
	 */
	public String getCodigoComposicion() {
		return codigoComposicion;
	}
	
	/**
	 * Asigna codigo de composicion asociado al inciso.
	 * @param codigoComposicion the codigoComposicion to set
	 */
	public void setCodigoComposicion(String codigoComposicion) {
		this.codigoComposicion = codigoComposicion;
	}
	
	/**
	 * Retorna numero maximo del rol asociado al inciso.
	 * @return the numeroMaximo
	 */
	public String getNumeroMaximo() {
		return numeroMaximo;
	}
	
	/**
	 * Asigna numero maximo del rol asociado al inciso.
	 * @param numeroMaximo the numeroMaximo to set
	 */
	public void setNumeroMaximo(String numeroMaximo) {
		this.numeroMaximo = numeroMaximo;
	}
	
	/**
	 * Retorna codigo de tipo de situacion del rol asociado al inciso.
	 * @return the cdtipsit
	 */
	public String getCdtipsit() {
		return cdtipsit;
	}
	
	/**
	 * Asigna codigo de tipo de situacion del rol asociado al inciso.
	 * @param cdtipsit the cdtipsit to set
	 */
	public void setCdtipsit(String cdtipsit) {
		this.cdtipsit = cdtipsit;
	}
	
	/**
	 * Retorna domicilio del rol asociado al inciso.
	 * @return the switchDomicilio
	 */
	public String getSwitchDomicilio() {
		return switchDomicilio;
	}
	
	/**
	 * Asigna domicilio del rol asociado al inciso.
	 * @param switchDomicilio the switchDomicilio to set
	 */
	public void setSwitchDomicilio(String switchDomicilio) {
		this.switchDomicilio = switchDomicilio;
	}

	/**
	 * Retorna descripcion del rol asociado al inciso.
	 * @return the descripcionRol
	 */
	public String getDescripcionRol() {
		return descripcionRol;
	}

	/**
	 * Asigna descripcion del rol asociado al inciso.
	 * @param descripcionRol
	 */
	public void setDescripcionRol(String descripcionRol) {
		this.descripcionRol = descripcionRol;
	}

	/**
	 * Retorna descripcion del rol asociado al inciso.
	 * @return the descripcionComposicion
	 */
	public String getDescripcionComposicion() {
		return descripcionComposicion;
	}

	/**
	 * Asigna descripcion de composicion asociado al inciso.
	 * @param descripcionComposicion
	 */
	public void setDescripcionComposicion(String descripcionComposicion) {
		this.descripcionComposicion = descripcionComposicion;
	}

}
