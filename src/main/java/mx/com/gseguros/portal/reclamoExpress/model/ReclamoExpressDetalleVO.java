package mx.com.gseguros.portal.reclamoExpress.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MAVR
 */
public class ReclamoExpressDetalleVO implements Serializable {

	
	private static final long serialVersionUID = -8951912318013775821L;
	private String reclamo;
	private String secuencial;
	private String fechaProcesamiento;
	private String clavePoliza;
	private String claveReclamo;
	private String claveDetalle;
	private String idSESAS;
	private String idAsegurado;
	private String asegurado;
	private String tipoProcedimiento;
	private String procedimiento;
	private String procedimientoNombre;
	private String cobertura;
	private String coberturaNombre;
	private String subcobertura;
	private String subcoberturaNombre;
	private String importe;
	private String clasificacion;
	private String programa;
	
	
	
	public String getReclamo() {
		return reclamo;
	}



	public void setReclamo(String reclamo) {
		this.reclamo = reclamo;
	}



	public String getSecuencial() {
		return secuencial;
	}



	public void setSecuencial(String secuencial) {
		this.secuencial = secuencial;
	}



	public String getFechaProcesamiento() {
		return fechaProcesamiento;
	}



	public void setFechaProcesamiento(String fechaProcesamiento) {
		this.fechaProcesamiento = fechaProcesamiento;
	}



	public String getClavePoliza() {
		return clavePoliza;
	}



	public void setClavePoliza(String clavePoliza) {
		this.clavePoliza = clavePoliza;
	}



	public String getClaveReclamo() {
		return claveReclamo;
	}



	public void setClaveReclamo(String claveReclamo) {
		this.claveReclamo = claveReclamo;
	}



	public String getClaveDetalle() {
		return claveDetalle;
	}



	public void setClaveDetalle(String claveDetalle) {
		this.claveDetalle = claveDetalle;
	}



	public String getIdSESAS() {
		return idSESAS;
	}



	public void setIdSESAS(String idSESAS) {
		this.idSESAS = idSESAS;
	}



	public String getIdAsegurado() {
		return idAsegurado;
	}



	public void setIdAsegurado(String idAsegurado) {
		this.idAsegurado = idAsegurado;
	}



	public String getTipoProcedimiento() {
		return tipoProcedimiento;
	}



	public void setTipoProcedimiento(String tipoProcedimiento) {
		this.tipoProcedimiento = tipoProcedimiento;
	}



	public String getProcedimiento() {
		return procedimiento;
	}



	public void setProcedimiento(String procedimiento) {
		this.procedimiento = procedimiento;
	}



	public String getProcedimientoNombre() {
		return procedimientoNombre;
	}



	public void setProcedimientoNombre(String procedimientoNombre) {
		this.procedimientoNombre = procedimientoNombre;
	}



	public String getCobertura() {
		return cobertura;
	}



	public void setCobertura(String cobertura) {
		this.cobertura = cobertura;
	}



	public String getCoberturaNombre() {
		return coberturaNombre;
	}



	public void setCoberturaNombre(String coberturaNombre) {
		this.coberturaNombre = coberturaNombre;
	}



	public String getSubcobertura() {
		return subcobertura;
	}



	public void setSubcobertura(String subcobertura) {
		this.subcobertura = subcobertura;
	}



	public String getSubcoberturaNombre() {
		return subcoberturaNombre;
	}



	public void setSubcoberturaNombre(String subcoberturaNombre) {
		this.subcoberturaNombre = subcoberturaNombre;
	}



	public String getImporte() {
		return importe;
	}



	public void setImporte(String importe) {
		this.importe = importe;
	}



	public String getClasificacion() {
		return clasificacion;
	}



	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}



	public String getPrograma() {
		return programa;
	}



	public void setPrograma(String programa) {
		this.programa = programa;
	}

	

	public String getAsegurado() {
		return asegurado;
	}



	public void setAsegurado(String asegurado) {
		this.asegurado = asegurado;
	}



	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
		
}
