package mx.com.gseguros.portal.reclamoExpress.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MAVR
 */
public class ReclamoExpressVO implements Serializable {

	
	private static final long serialVersionUID = -8951912318013775821L;

	private String reclamo;
	private String secuencial;
	private String fechaCaptura;
	private String sucursal;
	private String sucursalNombre;
	private String ramo;
	private String poliza;
	private String idCliente;
	private String cliente;
	private String idAsegurado;
	private String asegurado;
	private String factura;
	private String fechaFactura;
	private String importe;
	private String iva;
	private String ivaRetenido;
	private String isr;
	private String idProveedor;
	private String proveedor;
	private String proveedorRfc;
	private String fechaProcesamiento;
	private String tipoReclamo;
	private String siniestro;
	private String siniestroSerie;
	private String fechaPago;
	private String destino;
	private String destinoNombre;
	private String fechaAplicacion;
	private String idTipoServicio;
	private String tipoServicio;
	private String solicitudCxp;
	private String clavePoliza;
	private String claveReclamo;
	private String tipoPago;
	private String referencia;
	private String idSESAS;
	private String conducto;
	private String atencionHosp;
	private String causaReclamo;
	private String idIcd;
	private String icdNombre;
	private String procedimiento1;
	private String procedimiento2;
	private String procedimiento3;
	private String fechaIngreso;
	private String fechaAlta;
	private String motivoEgreso;
	//Variable para saber si es modo de creaci&oacute;n de edici&oacute;n.
	private String modo;
	
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
	public String getFechaCaptura() {
		return fechaCaptura;
	}
	public void setFechaCaptura(String fechaCaptura) {
		this.fechaCaptura = fechaCaptura;
	}
	public String getSucursal() {
		return sucursal;
	}
	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}
	public String getSucursalNombre() {
		return sucursalNombre;
	}
	public void setSucursalNombre(String sucursalNombre) {
		this.sucursalNombre = sucursalNombre;
	}
	public String getRamo() {
		return ramo;
	}
	public void setRamo(String ramo) {
		this.ramo = ramo;
	}
	public String getPoliza() {
		return poliza;
	}
	public void setPoliza(String poliza) {
		this.poliza = poliza;
	}
	
	public String getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getIdAsegurado() {
		return idAsegurado;
	}
	public void setIdAsegurado(String idAsegurado) {
		this.idAsegurado = idAsegurado;
	}
	public String getAsegurado() {
		return asegurado;
	}
	public void setAsegurado(String asegurado) {
		this.asegurado = asegurado;
	}
	public String getFactura() {
		return factura;
	}
	public void setFactura(String factura) {
		this.factura = factura;
	}
	public String getFechaFactura() {
		return fechaFactura;
	}
	public void setFechaFactura(String fechaFactura) {
		this.fechaFactura = fechaFactura;
	}
	public String getImporte() {
		return importe;
	}
	public void setImporte(String importe) {
		this.importe = importe;
	}
	public String getIva() {
		return iva;
	}
	public void setIva(String iva) {
		this.iva = iva;
	}
	public String getIvaRetenido() {
		return ivaRetenido;
	}
	public void setIvaRetenido(String ivaRetenido) {
		this.ivaRetenido = ivaRetenido;
	}
	public String getIsr() {
		return isr;
	}
	public void setIsr(String isr) {
		this.isr = isr;
	}
	public String getIdProveedor() {
		return idProveedor;
	}
	public void setIdProveedor(String idProveedor) {
		this.idProveedor = idProveedor;
	}
	public String getProveedor() {
		return proveedor;
	}
	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}
	public String getProveedorRfc() {
		return proveedorRfc;
	}
	public void setProveedorRfc(String proveedorRfc) {
		this.proveedorRfc = proveedorRfc;
	}
	public String getFechaProcesamiento() {
		return fechaProcesamiento;
	}
	public void setFechaProcesamiento(String fechaProcesamiento) {
		this.fechaProcesamiento = fechaProcesamiento;
	}
	public String getTipoReclamo() {
		return tipoReclamo;
	}
	public void setTipoReclamo(String tipoReclamo) {
		this.tipoReclamo = tipoReclamo;
	}
	public String getSiniestro() {
		return siniestro;
	}
	public void setSiniestro(String siniestro) {
		this.siniestro = siniestro;
	}
	public String getSiniestroSerie() {
		return siniestroSerie;
	}
	public void setSiniestroSerie(String siniestroSerie) {
		this.siniestroSerie = siniestroSerie;
	}
	public String getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(String fechaPago) {
		this.fechaPago = fechaPago;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public String getDestinoNombre() {
		return destinoNombre;
	}
	public void setDestinoNombre(String destinoNombre) {
		this.destinoNombre = destinoNombre;
	}
	public String getFechaAplicacion() {
		return fechaAplicacion;
	}
	public void setFechaAplicacion(String fechaAplicacion) {
		this.fechaAplicacion = fechaAplicacion;
	}
	public String getIdTipoServicio() {
		return idTipoServicio;
	}
	public void setIdTipoServicio(String idTipoServicio) {
		this.idTipoServicio = idTipoServicio;
	}
	public String getTipoServicio() {
		return tipoServicio;
	}
	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}
	public String getSolicitudCxp() {
		return solicitudCxp;
	}
	public void setSolicitudCxp(String solicitudCxp) {
		this.solicitudCxp = solicitudCxp;
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
	public String getTipoPago() {
		return tipoPago;
	}
	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
		
	public String getIdSESAS() {
		return idSESAS;
	}
	public void setIdSESAS(String idSESAS) {
		this.idSESAS = idSESAS;
	}
	
	public String getAtencionHosp() {
		return atencionHosp;
	}
	public void setAtencionHosp(String atencionHosp) {
		this.atencionHosp = atencionHosp;
	}
	
	public String getConducto() {
		return conducto;
	}
	public void setConducto(String conducto) {
		this.conducto = conducto;
	}
	public String getCausaReclamo() {
		return causaReclamo;
	}
	public void setCausaReclamo(String causaReclamo) {
		this.causaReclamo = causaReclamo;
	}
	public String getIdIcd() {
		return idIcd;
	}
	public void setIdIcd(String idIcd) {
		this.idIcd = idIcd;
	}
	public String getIcdNombre() {
		return icdNombre;
	}
	public void setIcdNombre(String icdNombre) {
		this.icdNombre = icdNombre;
	}
	public String getProcedimiento1() {
		return procedimiento1;
	}
	public void setProcedimiento1(String procedimiento1) {
		this.procedimiento1 = procedimiento1;
	}
	public String getProcedimiento2() {
		return procedimiento2;
	}
	public void setProcedimiento2(String procedimiento2) {
		this.procedimiento2 = procedimiento2;
	}
	public String getProcedimiento3() {
		return procedimiento3;
	}
	public void setProcedimiento3(String procedimiento3) {
		this.procedimiento3 = procedimiento3;
	}
	public String getFechaIngreso() {
		return fechaIngreso;
	}
	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	public String getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(String fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	public String getMotivoEgreso() {
		return motivoEgreso;
	}
	public void setMotivoEgreso(String motivoEgreso) {
		this.motivoEgreso = motivoEgreso;
	}	
	public String getModo() {
		return modo;
	}
	public void setModo(String modo) {
		this.modo = modo;
	}
	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
		
}
