package mx.com.gseguros.portal.siniestros.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Random;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Ricardo
 *
 */
public class DatosGeneralesSiniestroVO implements Serializable {

	//private static final long serialVersionUID = -7555353864912795413L;
	
	private String contraRecibo;
	private String estado;
	private String oficinaEmisora;
	private String oficinaDocumento;
	private String fechaRecepcion;
	private String fechaOcurrencia;
	private String origenSiniestro;
	private String plan;
	private String circuloHospitalario;
	private String zonaTarificacion;
	private String sumAseguradaContr;
	private String tipoPago;
	private String poliza;
	private String sumaDisponible;
	private String fechaInicioVigencia;
	private String fechaFinVigencia;
	private String estatusPoliza;
	private String fechaAntiguedad;
	private String fechaAntiguedadGSS;
	private String tiempoAntiguedadGSS;
	private String formaPagoPoliza;
	private String aseguradoAfectado;
	private String proveedor;
	private String circuloHospitalario2;
	private String icd;
	private String icdSecundario;
	
	
	//Getters and setters	

	public String getContraRecibo() {
		return contraRecibo;
	}

	public void setContraRecibo(String contraRecibo) {
		this.contraRecibo = contraRecibo;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getOficinaEmisora() {
		return oficinaEmisora;
	}

	public void setOficinaEmisora(String oficinaEmisora) {
		this.oficinaEmisora = oficinaEmisora;
	}

	public String getOficinaDocumento() {
		return oficinaDocumento;
	}

	public void setOficinaDocumento(String oficinaDocumento) {
		this.oficinaDocumento = oficinaDocumento;
	}

	public String getFechaRecepcion() {
		return fechaRecepcion;
	}

	public void setFechaRecepcion(String fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}

	public String getFechaOcurrencia() {
		return fechaOcurrencia;
	}

	public void setFechaOcurrencia(String fechaOcurrencia) {
		this.fechaOcurrencia = fechaOcurrencia;
	}

	public String getOrigenSiniestro() {
		return origenSiniestro;
	}

	public void setOrigenSiniestro(String origenSiniestro) {
		this.origenSiniestro = origenSiniestro;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getCirculoHospitalario() {
		return circuloHospitalario;
	}

	public void setCirculoHospitalario(String circuloHospitalario) {
		this.circuloHospitalario = circuloHospitalario;
	}

	public String getZonaTarificacion() {
		return zonaTarificacion;
	}

	public void setZonaTarificacion(String zonaTarificacion) {
		this.zonaTarificacion = zonaTarificacion;
	}

	public String getSumAseguradaContr() {
		return sumAseguradaContr;
	}

	public void setSumAseguradaContr(String sumAseguradaContr) {
		this.sumAseguradaContr = sumAseguradaContr;
	}

	public String getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}

	public String getPoliza() {
		return poliza;
	}

	public void setPoliza(String poliza) {
		this.poliza = poliza;
	}

	public String getSumaDisponible() {
		return sumaDisponible;
	}

	public void setSumaDisponible(String sumaDisponible) {
		this.sumaDisponible = sumaDisponible;
	}

	public String getFechaInicioVigencia() {
		return fechaInicioVigencia;
	}

	public void setFechaInicioVigencia(String fechaInicioVigencia) {
		this.fechaInicioVigencia = fechaInicioVigencia;
	}

	public String getFechaFinVigencia() {
		return fechaFinVigencia;
	}

	public void setFechaFinVigencia(String fechaFinVigencia) {
		this.fechaFinVigencia = fechaFinVigencia;
	}

	public String getEstatusPoliza() {
		return estatusPoliza;
	}

	public void setEstatusPoliza(String estatusPoliza) {
		this.estatusPoliza = estatusPoliza;
	}

	public String getFechaAntiguedad() {
		return fechaAntiguedad;
	}

	public void setFechaAntiguedad(String fechaAntiguedad) {
		this.fechaAntiguedad = fechaAntiguedad;
	}

	public String getFechaAntiguedadGSS() {
		return fechaAntiguedadGSS;
	}

	public void setFechaAntiguedadGSS(String fechaAntiguedadGSS) {
		this.fechaAntiguedadGSS = fechaAntiguedadGSS;
	}

	public String getTiempoAntiguedadGSS() {
		return tiempoAntiguedadGSS;
	}

	public void setTiempoAntiguedadGSS(String tiempoAntiguedadGSS) {
		this.tiempoAntiguedadGSS = tiempoAntiguedadGSS;
	}

	public String getFormaPagoPoliza() {
		return formaPagoPoliza;
	}

	public void setFormaPagoPoliza(String formaPagoPoliza) {
		this.formaPagoPoliza = formaPagoPoliza;
	}

	public String getAseguradoAfectado() {
		return aseguradoAfectado;
	}

	public void setAseguradoAfectado(String aseguradoAfectado) {
		this.aseguradoAfectado = aseguradoAfectado;
	}

	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	public String getCirculoHospitalario2() {
		return circuloHospitalario2;
	}

	public void setCirculoHospitalario2(String circuloHospitalario2) {
		this.circuloHospitalario2 = circuloHospitalario2;
	}

	public String getIcd() {
		return icd;
	}

	public void setIcd(String icd) {
		this.icd = icd;
	}

	public String getIcdSecundario() {
		return icdSecundario;
	}

	public void setIcdSecundario(String icdSecundario) {
		this.icdSecundario = icdSecundario;
	}
	
	
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	public static void main(String[] args) {
		try {
			DatosGeneralesSiniestroVO data = new DatosGeneralesSiniestroVO();
			Object obj = data;
			for (Field field : obj.getClass().getDeclaredFields()) {
			    field.setAccessible(true); // You might want to set modifier to public first.
			    field.set(data, "Valor " + new Random().nextInt(100+1));
			    /*
			    Object value;
			    value = field.get(obj);
			    if (value != null) {
			        System.out.println(field.getName() + "=" + value);
			    }
			    */
			}
			System.out.println("obj=" + obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}