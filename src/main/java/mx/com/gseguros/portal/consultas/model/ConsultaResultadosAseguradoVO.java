package mx.com.gseguros.portal.consultas.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MAVR
 *
 */
public class ConsultaResultadosAseguradoVO implements Serializable {

	private static final long serialVersionUID = -8555353864912795413L;
	
	private String cdramo;
	private String dsramo;
	private String cdunieco;	
	private String dsunieco;
	private String estado;
	private String nmpoliza;
	private String cdperson;
	private String nombreAsegurado;
	private String identificacion;
	private String nmpoliex;
	private String resultadosAsegurado;
	private String nmsituac;
		
	/**
	 * C&oacute;digo de la p&oacute;liza usado en SISA 
	 */
	private String icodpoliza;
	
	/**
	 * Origen de la p\u00F3liza: SISA, ICE, etc.
	 */
	private String origen;
	
	/**
	 * Vigencia
	 */
	private String feinivigencia;
	private String fefinvigencia;
	
	
	
	public String getCdunieco() {
		return cdunieco;
	}

	public void setCdunieco(String cdunieco) {
		this.cdunieco = cdunieco;
	}

	public String getDsunieco() {
		return dsunieco;
	}

	public void setDsunieco(String dsunieco) {
		this.dsunieco = dsunieco;
	}

	public String getCdramo() {
		return cdramo;
	}

	public void setCdramo(String cdramo) {
		this.cdramo = cdramo;
	}

	public String getDsramo() {
		return dsramo;
	}

	public void setDsramo(String dsramo) {
		this.dsramo = dsramo;
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

	public String getNombreAsegurado() {
		return nombreAsegurado;
	}

	public void setNombreAsegurado(String nombreAsegurado) {
		this.nombreAsegurado = nombreAsegurado;
	}

	public String getNmpoliex() {
		return nmpoliex;
	}

	public void setNmpoliex(String nmpoliex) {
		this.nmpoliex = nmpoliex;
	}

	public String getIcodpoliza() {
		return icodpoliza;
	}

	public void setIcodpoliza(String icodpoliza) {
		this.icodpoliza = icodpoliza;
	}
	
	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}
	
	public String getFeinivigencia() {
		return feinivigencia;
	}

	public void setFeinivigencia(String feinivigencia) {
		this.feinivigencia = feinivigencia;
	}

	public String getFefinvigencia() {
		return fefinvigencia;
	}

	public void setFefinvigencia(String fefinvigencia) {
		this.fefinvigencia = fefinvigencia;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}

	public String getCdperson() {
		return cdperson;
	}

	public void setCdperson(String cdperson) {
		this.cdperson = cdperson;
	}

	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public String getResultadosAsegurado() {
		return resultadosAsegurado;
	}
	
	public String getNmsituac() {
		return nmsituac;
	}

	public void setNmsituac(String nmsituac) {
		this.nmsituac = nmsituac;
	}

	public void setResultadosAsegurado(String resultadosAsegurado) {
		this.resultadosAsegurado = resultadosAsegurado;
	}	
			
}