package mx.com.gseguros.portal.general.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ClausulaVO implements Serializable {

	private static final long serialVersionUID = 2582531730846897501L;
	
	/**
	 * Clave de la clausula
	 */
	private String cdclausu;
	
	/**
	 * Descripcion de la clausula
	 */
	private String dsclausu;
	
	/**
	 * Tipo de Clausula
	 */
	private String cdtipcla;
	
	/**
	 * Estatus de la exclusion
	 */
	private String status;
	
	/**
	 * Contenido de la clausula modificado 
	 */
	private String linea_usuario;
	
	/**
	 * Contenido original de la clausula
	 */
	private String linea_general;
	
	/**
	 * Indica si fue modificado el contenido original de la clausula
	 */
	private String swmodi;
	
	/**
	 * Unidad economica asociada a la clausula
	 */
	private String cdunieco;
	
	/**
	 * Ramo asociado a la clausula
	 */
	private String cdramo;
	
	/**
	 * Estado asociado a la clausula
	 */
	private String estado;
	
	/**
	 * Numero de poliza asociado a la clausula
	 */
	private String nmpoliza;
	
	/**
	 * Numero de situacion asociado a la clausula
	 */
	private String nmsituac;
	
	/**
	 * Numero de suplemento asociado a la clausula
	 */
	private String nmsuplem;
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public String getCdclausu() {
		return cdclausu;
	}

	public void setCdclausu(String cdclausu) {
		this.cdclausu = cdclausu;
	}

	public String getDsclausu() {
		return dsclausu;
	}

	public void setDsclausu(String dsclausu) {
		this.dsclausu = dsclausu;
	}

	public String getCdtipcla() {
		return cdtipcla;
	}

	public void setCdtipcla(String cdtipcla) {
		this.cdtipcla = cdtipcla;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLinea_usuario() {
		return linea_usuario;
	}

	public void setLinea_usuario(String linea_usuario) {
		this.linea_usuario = linea_usuario;
	}

	public String getLinea_general() {
		return linea_general;
	}

	public void setLinea_general(String linea_general) {
		this.linea_general = linea_general;
	}

	public String getSwmodi() {
		return swmodi;
	}

	public void setSwmodi(String swmodi) {
		this.swmodi = swmodi;
	}

	public String getCdunieco() {
		return cdunieco;
	}

	public void setCdunieco(String cdunieco) {
		this.cdunieco = cdunieco;
	}

	public String getCdramo() {
		return cdramo;
	}

	public void setCdramo(String cdramo) {
		this.cdramo = cdramo;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNmpoliza() {
		return nmpoliza;
	}

	public void setNmpoliza(String nmpoliza) {
		this.nmpoliza = nmpoliza;
	}

	public String getNmsituac() {
		return nmsituac;
	}

	public void setNmsituac(String nmsituac) {
		this.nmsituac = nmsituac;
	}

	public String getNmsuplem() {
		return nmsuplem;
	}

	public void setNmsuplem(String nmsuplem) {
		this.nmsuplem = nmsuplem;
	}

}