package mx.com.aon.portal.model.principal;

import java.io.Serializable;

/**
 * 
 * @author sergio.ramirez
 *
 */
public class ConfiguracionVO implements Serializable{

	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = -2836593875194605941L;
	private String claveConfig;
	private String descripcionConfig;
	private String claveElemento;
	private String claveRol;
	private String claveSeccion;
	private int swHabilitado;
	private String especificacion;
	private byte[] contenido;
	private byte[] contenidoDos;
	private String claveTipo;
	private String archivoLoad;
	private String contenidoDato;
	private String contenidoDatoSeg;
	
	
	
	
	/**
	 * 
	 * @return
	 */
	public byte[] getContenido() {
		return contenido;
	}
	/**
	 * 
	 * @param contenido
	 */
	public void setContenido(byte[] contenido) {
		this.contenido = contenido;
	}
	/**
	 * 
	 * @return
	 */
	public int getSwHabilitado() {
		return swHabilitado;
	}
	/**
	 * 
	 * @param swHabilitado
	 */
	public void setSwHabilitado(int swHabilitado) {
		this.swHabilitado = swHabilitado;
	}
	/**
	 * 
	 * @return
	 */
	public String getClaveConfig() {
		return claveConfig;
	}
	/**
	 * 
	 * @param claveConfig
	 */
	public void setClaveConfig(String claveConfig) {
		this.claveConfig = claveConfig;
	}
	/**
	 * 
	 * @return
	 */
	public String getDescripcionConfig() {
		return descripcionConfig;
	}
	/**
	 * 
	 * @param descripcionConfig
	 */
	public void setDescripcionConfig(String descripcionConfig) {
		this.descripcionConfig = descripcionConfig;
	}
	/**
	 * 
	 * @return
	 */
	public String getClaveElemento() {
		return claveElemento;
	}
	/**
	 * 
	 * @param claveElemento
	 */
	public void setClaveElemento(String claveElemento) {
		this.claveElemento = claveElemento;
	}
	/**
	 * 
	 * @return
	 */
	public String getClaveRol() {
		return claveRol;
	}
	/**
	 * 
	 * @param claveRol
	 */
	public void setClaveRol(String claveRol) {
		this.claveRol = claveRol;
	}
	/**
	 * 
	 * @return
	 */
	public String getClaveSeccion() {
		return claveSeccion;
	}
	/**
	 * 
	 * @param claveSeccion
	 */
	public void setClaveSeccion(String claveSeccion) {
		this.claveSeccion = claveSeccion;
	}
	/**
	 * 
	 * @return
	 */
	
	public String getEspecificacion() {
		return especificacion;
	}
	/**
	 * 
	 * @param especificacion
	 */
	public void setEspecificacion(String especificacion) {
		this.especificacion = especificacion;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getClaveTipo() {
		return claveTipo;
	}
	/**
	 * 
	 * @param claveTipo
	 */
	public void setClaveTipo(String claveTipo) {
		this.claveTipo = claveTipo;
	}
	/**
	 * 
	 * @return
	 */
	public String getArchivoLoad() {
		return archivoLoad;
	}
	/**
	 * 
	 * @param archivoLoad
	 */
	public void setArchivoLoad(String archivoLoad) {
		this.archivoLoad = archivoLoad;
	}
	/**
	 * 
	 * @return
	 */
	public byte[] getContenidoDos() {
		return contenidoDos;
	}
	/**
	 * 
	 * @param contenidoDos
	 */
	public void setContenidoDos(byte[] contenidoDos) {
		this.contenidoDos = contenidoDos;
	}
	/**
	 * 
	 * @return
	 */
	public String getContenidoDato() {
		return contenidoDato;
	}
	/**
	 * 
	 * @param contenidoDato
	 */
	public void setContenidoDato(String contenidoDato) {
		this.contenidoDato = contenidoDato;
	}
	/**
	 * 
	 * @return
	 */
	public String getContenidoDatoSeg() {
		return contenidoDatoSeg;
	}
	/**
	 * 
	 * @param contenidoDatoSeg
	 */
	public void setContenidoDatoSeg(String contenidoDatoSeg) {
		this.contenidoDatoSeg = contenidoDatoSeg;
	}
	
	
	
}
