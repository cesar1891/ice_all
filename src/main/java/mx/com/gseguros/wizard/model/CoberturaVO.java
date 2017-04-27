package mx.com.gseguros.wizard.model;

import java.io.Serializable;

/**
 * Clase VO que contien los datos de las coberturas.
 * 
 * @since 1.0
 * @author <a href="mailto:edgar.perez@biosnettcs.com">Edgar Perez</a>
 * @version 2.0
 */
public class CoberturaVO implements Serializable {

	
	private static final long serialVersionUID = 4727537916347665217L;
	
	/**
	 * Clave asignada para una nueva cobertura .
	 */
	private String claveCobertura;
	
	/**
	 * Descripcion asignada para una nueva cobertura .
	 */
	private String descripcionCobertura;
	
	/**
	 * Tipo asignado para una nueva cobertura .
	 */
	private String tipoCobertura;
	
	/**
	 * Ramo asignado para una nueva cobertura .
	 */
	private String ramoCobertura;
	
	/**
	 * reinstalacion de la cobertura.
	 */
	private String reinstalacion;
	
	/**
	 * indice inflacionario de la cobertura.
	 */
	private String indiceInflacionario;
	
	/**
	 * obligatoriedad de la cobertura.
	 */
	private String obligatorio;
	
	/**
	 * insercion de la cobertura.
	 */
	private String inserta;
	
	/**
	 * clave del capital de la cobertura.
	 */
	private String codigoCapital;
	
	/**
	 * clave de la condicion de la cobertura.
	 */
	private String codigoCondicion;
	
	/**
	 * codigo del ramo de la cobertura.
	 */
	private String codigoRamo;
	
	/**
	 * codigo del tipo de situacion de la cobertura.
	 */
	private String codigoTipoSituacion;
	
	/**
	 * insercion de la suma asegurada para la cobertura.
	 */
	private String sumaAsegurada;
	
	/**
	 * descripcion del capital de la cobertura.
	 */
	private String descripcionCapital;
	
	/**
	 * descripcion de la condicion de la cobertura.
	 */
	private String descripcionCondicion;
	
	/**
	 * Retorna clave asignada para la cobertura.
	 * 
	 * @return the claveCobertura
	 */
	public String getClaveCobertura() {
		return claveCobertura;
	}

	/**
	 * Asigna clave para la cobertura.
	 * 
	 * @param claveCobertura
	 *            the claveCobertura to set
	 */
	public void setClaveCobertura(String claveCobertura) {
		this.claveCobertura = claveCobertura;
	}

	/**
	 * Retorna descripcion asignada para la cobertura.
	 * 
	 * @return the descripcionCobertura
	 */
	public String getDescripcionCobertura() {
		return descripcionCobertura;
	}

	/**
	 * Asigna descripcion para la cobertura.
	 * 
	 * @param descripcionCobertura
	 *            the descripcionCobertura to set
	 */
	public void setDescripcionCobertura(String descripcionCobertura) {
		this.descripcionCobertura = descripcionCobertura;
	}

	/**
	 * Retorna tipo cobertura asignada para la cobertura.
	 * 
	 * @return the tipoCobertura
	 */
	public String getTipoCobertura() {
		return tipoCobertura;
	}

	/**
	 * Asigna tipo para la cobertura.
	 * 
	 * @param tipoCobertura
	 *            the tipoCobertura to set
	 */
	public void setTipoCobertura(String tipoCobertura) {
		this.tipoCobertura = tipoCobertura;
	}

	/**
	 * Retorna ramo cobertura asignada para la cobertura.
	 * 
	 * @return the ramoCobertura
	 */
	public String getRamoCobertura() {
		return ramoCobertura;
	}

	/**
	 * Asigna ramo para la cobertura.
	 * 
	 * @param ramoCobertura
	 *            the ramoCobertura to set
	 */
	public void setRamoCobertura(String ramoCobertura) {
		this.ramoCobertura = ramoCobertura;
	}

	/**
	 * Retorna reinstalacion asignada para la cobertura.
	 * 
	 * @return the reinstalacion
	 */
	public String getReinstalacion() {
		return reinstalacion;
	}

	/**
	 * Asigna reinstalacion para la cobertura.
	 * 
	 * @param reinstalacion
	 *            the reinstalacion to set
	 */
	public void setReinstalacion(String reinstalacion) {
		this.reinstalacion = reinstalacion;
	}

	/**
	 * Retorna indice inflacionario asignado para la cobertura.
	 * 
	 * @return the indiceInflacionario
	 */
	public String getIndiceInflacionario() {
		return indiceInflacionario;
	}

	/**
	 * Asigna indice Inflacionario para la cobertura.
	 * 
	 * @param indiceInflacionario
	 *            the indiceInflacionario to set
	 */
	public void setIndiceInflacionario(String indiceInflacionario) {
		this.indiceInflacionario = indiceInflacionario;
	}

	/**
	 * Retorna obligatoriedad asignada para la cobertura.
	 * 
	 * @return the obligatorio
	 */
	public String getObligatorio() {
		return obligatorio;
	}

	/**
	 * Asigna obligatorio para la cobertura.
	 * 
	 * @param obligatorio
	 *            the obligatorio to set
	 */
	public void setObligatorio(String obligatorio) {
		this.obligatorio = obligatorio;
	}

	/**
	 * Retorna insercion asignada para la cobertura.
	 * 
	 * @return the inserta
	 */
	public String getInserta() {
		return inserta;
	}

	/**
	 * Asigna inserta para la cobertura.
	 * 
	 * @param inserta
	 *            the inserta to set
	 */
	public void setInserta(String inserta) {
		this.inserta = inserta;
	}

	/**
	 * Retorna codigo capital asignado para la cobertura.
	 * 
	 * @return the codigoCapital
	 */
	public String getCodigoCapital() {
		return codigoCapital;
	}

	/**
	 * Asigna codigo Capital para la cobertura.
	 * 
	 * @param codigoCapital
	 *            the codigoCapital to set
	 */
	public void setCodigoCapital(String codigoCapital) {
		this.codigoCapital = codigoCapital;
	}

	/**
	 * Retorna codigo condicion asignado para la cobertura.
	 * 
	 * @return the codigoCondicion
	 */
	public String getCodigoCondicion() {
		return codigoCondicion;
	}

	/**
	 * Asigna codigo Condicion para la cobertura.
	 * 
	 * @param codigoCondicion
	 *            the codigoCondicion to set
	 */
	public void setCodigoCondicion(String codigoCondicion) {
		this.codigoCondicion = codigoCondicion;
	}

	/**
	 * Retorna codigo ramo asignado para la cobertura.
	 * 
	 * @return the codigoRamo
	 */
	public String getCodigoRamo() {
		return codigoRamo;
	}

	/**
	 * Asigna codigo Ramo para la cobertura.
	 * 
	 * @param codigoRamo
	 *            the codigoRamo to set
	 */
	public void setCodigoRamo(String codigoRamo) {
		this.codigoRamo = codigoRamo;
	}

	/**
	 * Retorna codigo tipo situacion asignado para la cobertura.
	 * 
	 * @return the codigoTipoSituacion
	 */
	public String getCodigoTipoSituacion() {
		return codigoTipoSituacion;
	}

	/**
	 * Asigna codigo Tipo Situacion para la cobertura.
	 * 
	 * @param codigoTipoSituacion
	 *            the codigoTipoSituacion to set
	 */
	public void setCodigoTipoSituacion(String codigoTipoSituacion) {
		this.codigoTipoSituacion = codigoTipoSituacion;
	}

	/**
	 * Retorna suma asegurada asignada para la cobertura.
	 * 
	 * @return the indiceInflacionario
	 */
	public String getSumaAsegurada() {
		return sumaAsegurada;
	}

	/**
	 * Asigna codigo suma asegurada para la cobertura.
	 * 
	 * @param sumaAsegurada
	 *            the sumaAsegurada to set
	 */
	public void setSumaAsegurada(String sumaAsegurada) {
		this.sumaAsegurada = sumaAsegurada;
	}

	/**
	 * Retorna descripcion Condicion asignada para la cobertura.
	 * 
	 * @return the indiceInflacionario
	 */
	public String getDescripcionCondicion() {
		return descripcionCondicion;
	}

	/**
	 * Asigna descripcion Condicion para la cobertura.
	 * 
	 * @param descripcionCondicion
	 *            the descripcionCondicion to set
	 */
	public void setDescripcionCondicion(String descripcionCondicion) {
		this.descripcionCondicion = descripcionCondicion;
	}

	/**
	 * Retorna descripcion Capital asignada para la cobertura.
	 * 
	 * @return the indiceInflacionario
	 */
	public String getDescripcionCapital() {
		return descripcionCapital;
	}

	/**
	 * Asigna descripcion Capital para la cobertura.
	 * 
	 * @param descripcionCapital
	 *            the descripcionCapital to set
	 */
	public void setDescripcionCapital(String descripcionCapital) {
		this.descripcionCapital = descripcionCapital;
	}
	
}
