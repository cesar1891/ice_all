package mx.com.gseguros.portal.reclamoExpress.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.gseguros.portal.general.model.BaseVO;
import mx.com.gseguros.portal.reclamoExpress.model.ReclamoExpressDetalleVO;
import mx.com.gseguros.portal.reclamoExpress.model.ReclamoExpressVO;
import mx.com.gseguros.portal.reclamoExpress.service.ReclamoExpressManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
*
* @author MAVR
*/
@ParentPackage(value="default")
@Namespace("/reclamoExpress")
@Controller(value="ReclamoExpressAction")
@Scope(value="prototype")
public class ReclamoExpressAction extends PrincipalCoreAction {
	private static final long serialVersionUID = 7279218302506728952L;
	private org.apache.log4j.Logger logger =org.apache.log4j.Logger.getLogger(ReclamoExpressAction.class);
	
	/**
     * Success property
     */
    private boolean success;
    
    private String mensajeRes;
    
    private Map<String, String> params = new HashMap<String, String>();
    private List<HashMap<String,String>> datosTablas = new ArrayList<HashMap<String,String>>();
    
    @Autowired
	@Qualifier("reclamoExpressManagerImpl")
    private ReclamoExpressManager reclamoExpressManager;
    
    
    private List<BaseVO> datosReclamos;
    private List<BaseVO> datosSecuenciales;
    
    
    private List<ReclamoExpressVO> reclamoExpress;
    
    /***CAMPOS DEL FORMULARIO***/
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
    
    
    /***RECLAMOS PARA COMBO***/
    public String consultaReclamos(){
    	logger.debug("***Entrando a consultaReclamos***");
    	
    	try{
    		datosReclamos = reclamoExpressManager.obtieneReclamos();
    	}catch(Exception e){
    		logger.error("Error al obtener reclamos",e);
    		return SUCCESS;
    	}
    	success = true;
    	return SUCCESS;
    }
    
    /***SECUENCIALES PARA COMBO***/
    public String consultaSecuenciales(){
    	logger.debug("***Entrando a consultaSecuenciales***");
    	int reclamo = Integer.parseInt(params.get("reclamo"));
    	
    	try{
    		datosSecuenciales = reclamoExpressManager.obtieneSecuenciales(reclamo);
    	}catch(Exception e){
    		logger.error("Error al obtener secuenciales",e);
    		return SUCCESS;
    	}
    	success = true;
    	return SUCCESS;
    }
    
    /***CONSULTA DEL RECLAMO***/
    public String consultaReclamoExpress(){
    	logger.debug("***Entrando a consultaReclamoExpress***");
    	
    	reclamoExpress = new ArrayList<ReclamoExpressVO>();    	
    	int reclamo = Integer.parseInt(params.get("reclamo"));
		int secuencial = Integer.parseInt(params.get("secuencial"));
		//System.out.println("Este es el reclamo:"+reclamo);
		//System.out.println("Este es el secuencial:"+secuencial);
    	try{
    		reclamoExpress = reclamoExpressManager.obtieneReclamoExpress(reclamo,secuencial);
    		
    	}catch(Exception e){
    		logger.error("Error al obtener reclamo express",e);
    		return SUCCESS;
    	}
    	
    	success = true;
    	return SUCCESS;
    }
    
    /***GUARDA RECLAMO***/    
    public String guardaReclamoExpress(){
    	logger.debug("***Entrando a guardaReclamoExpress***");
    	ReclamoExpressVO reclamoExpressVO = new ReclamoExpressVO();
    	reclamoExpressVO.setReclamo(reclamo);
    	reclamoExpressVO.setSecuencial(secuencial);
    	reclamoExpressVO.setFechaCaptura(fechaCaptura);
    	reclamoExpressVO.setSucursal(sucursal);
    	reclamoExpressVO.setSucursalNombre(sucursalNombre);
    	reclamoExpressVO.setRamo(ramo);
    	reclamoExpressVO.setPoliza(poliza);
    	reclamoExpressVO.setIdCliente(idCliente);
    	reclamoExpressVO.setCliente(cliente);
    	reclamoExpressVO.setIdAsegurado(idAsegurado);
    	reclamoExpressVO.setAsegurado(asegurado);
    	reclamoExpressVO.setFactura(factura);
    	reclamoExpressVO.setFechaFactura(fechaFactura);
    	reclamoExpressVO.setImporte(importe);
    	reclamoExpressVO.setIva(iva);
    	reclamoExpressVO.setIvaRetenido(ivaRetenido);
    	reclamoExpressVO.setIsr(isr);
    	reclamoExpressVO.setIdProveedor(idProveedor);
    	reclamoExpressVO.setProveedor(proveedor);
    	reclamoExpressVO.setProveedorRfc(this.limpiaRFC(proveedorRfc));
    	reclamoExpressVO.setFechaProcesamiento(fechaProcesamiento);
    	reclamoExpressVO.setTipoReclamo(tipoReclamo);
    	reclamoExpressVO.setSiniestro(siniestro);
    	reclamoExpressVO.setSiniestroSerie(siniestroSerie);
    	reclamoExpressVO.setFechaPago(fechaPago);
    	reclamoExpressVO.setDestino(destino);
    	reclamoExpressVO.setDestinoNombre(destinoNombre);
    	reclamoExpressVO.setFechaAplicacion(fechaAplicacion);
    	reclamoExpressVO.setIdTipoServicio(idTipoServicio);
    	reclamoExpressVO.setTipoServicio(tipoServicio);
    	reclamoExpressVO.setSolicitudCxp(solicitudCxp);
    	reclamoExpressVO.setClavePoliza(clavePoliza);
    	reclamoExpressVO.setClaveReclamo(claveReclamo);
    	reclamoExpressVO.setTipoPago(tipoPago);
    	reclamoExpressVO.setReferencia(referencia);
    	reclamoExpressVO.setIdSESAS(idSESAS);
    	reclamoExpressVO.setConducto((conducto != null && !conducto.isEmpty())?conducto:"S/R");
    	reclamoExpressVO.setAtencionHosp((atencionHosp != null && !atencionHosp.isEmpty())?atencionHosp:"S/R");
    	reclamoExpressVO.setCausaReclamo(causaReclamo);
    	if(idIcd != null && !idIcd.isEmpty()){
    		reclamoExpressVO.setIdIcd(idIcd);
    		reclamoExpressVO.setIcdNombre(this.extraeNombre(icdNombre));
    	} else {
    		reclamoExpressVO.setIdIcd("Z719");
    		reclamoExpressVO.setIcdNombre("CONSULTA,NO ESPECIFICADA");
    	}
    	reclamoExpressVO.setProcedimiento1((procedimiento1 != null && !procedimiento1.isEmpty())?procedimiento1:"00000");
    	reclamoExpressVO.setProcedimiento2((procedimiento2 != null && !procedimiento2.isEmpty())?procedimiento2:"00000");
    	reclamoExpressVO.setProcedimiento3((procedimiento3 != null && !procedimiento3.isEmpty())?procedimiento3:"00000");
    	reclamoExpressVO.setFechaIngreso((fechaIngreso != null && !fechaIngreso.isEmpty())?fechaIngreso:"01/01/1800");
    	reclamoExpressVO.setFechaAlta((fechaAlta != null && !fechaAlta.isEmpty())?fechaAlta:"01/01/1800");
    	reclamoExpressVO.setMotivoEgreso((motivoEgreso != null && !motivoEgreso.isEmpty())?motivoEgreso:"S/R");    	
    	
    	
    	logger.debug(reclamoExpressVO.toString());
    	
    	try{
    		success = reclamoExpressManager.guardaReclamoExpress(reclamoExpressVO);
    	}catch(Exception e){
    		logger.error("Error al insertar reclamacion", e);
    		success = false;
    		return SUCCESS;
    	}
    	
    	success = true;
    	return SUCCESS;
    }
    
    /***GUARDA DETALLE***/    
    public String guardaDetalleExpress(){
    	logger.debug("***Entrando a guardaDetalleExpress***");
    	//logger.debug(datosTablas);
    	//logger.debug(params);
    	try{
    		//1. Eliminamos registros
    		//Creamos un objeto detalle auxiliar
    		ReclamoExpressDetalleVO reclamoExpressDetalleVOAux = new ReclamoExpressDetalleVO();
    		reclamoExpressDetalleVOAux.setFechaProcesamiento(params.get("fechaProcesamiento"));
    		reclamoExpressDetalleVOAux.setClavePoliza(params.get("clavePoliza"));
    		reclamoExpressDetalleVOAux.setClaveReclamo(params.get("claveReclamo"));
    		
    		reclamoExpressManager.borraDetalleExpress(reclamoExpressDetalleVOAux);
    		reclamoExpressDetalleVOAux = null;
    	} catch(Exception e) {
    		logger.debug("Error al eliminar detalle",e);
    		success=false;
    	}
    	
    	try{
    		//2. Guardamos los registros
    		for(int i=0; i < datosTablas.size(); i++){
    			//Creamos objeto detalle
    			ReclamoExpressDetalleVO reclamoExpressDetalleVO = new ReclamoExpressDetalleVO();
    			reclamoExpressDetalleVO.setFechaProcesamiento(params.get("fechaProcesamiento"));
    			reclamoExpressDetalleVO.setClavePoliza(params.get("clavePoliza"));
    			reclamoExpressDetalleVO.setClaveReclamo(params.get("claveReclamo"));
    			reclamoExpressDetalleVO.setClaveDetalle(
					StringUtils.leftPad(
							Integer.toString(
									Integer.parseInt(datosTablas.get(i).get("indice")) + 1
							),
							5 ,"0")
				);
    			reclamoExpressDetalleVO.setIdAsegurado(params.get("idAsegurado"));
    			reclamoExpressDetalleVO.setIdSESAS(params.get("idSesas"));
    			String tipoProcAux = "";
    			switch(Integer.parseInt(datosTablas.get(i).get("idconcep"))){
    				case 1:
    					tipoProcAux = "CPT";
    					break;    				
    				case 2:
    					tipoProcAux = "HCPC";
    					break;
    				case 3:
    					tipoProcAux = "UB";
    					break;
					default:
						tipoProcAux = "CPT";
						break;
    			}
    			reclamoExpressDetalleVO.setTipoProcedimiento(tipoProcAux);
    			
    			String procAux;
    			String procIdAux;
    			String procNombreAux;
    			procAux = datosTablas.get(i).get("dsconcep");
    			procIdAux = procAux.substring(0, procAux.indexOf(" "));
    			procNombreAux = procAux.substring(procAux.indexOf(" "));
    			
    			reclamoExpressDetalleVO.setProcedimiento(procIdAux);
    			reclamoExpressDetalleVO.setProcedimientoNombre(procNombreAux.trim());
    			reclamoExpressDetalleVO.setImporte(datosTablas.get(i).get("importe"));
    			reclamoExpressDetalleVO.setCobertura(datosTablas.get(i).get("cdgarant"));
    			reclamoExpressDetalleVO.setCoberturaNombre(datosTablas.get(i).get("dsgarant"));
    			reclamoExpressDetalleVO.setSubcobertura(datosTablas.get(i).get("cdconval"));
    			reclamoExpressDetalleVO.setSubcoberturaNombre(datosTablas.get(i).get("dsconval"));
    			reclamoExpressDetalleVO.setClasificacion("SV"); //Salud Vital
    			reclamoExpressDetalleVO.setPrograma("Salud Vital");
    			
    			System.out.println(reclamoExpressDetalleVO.toString());
    			
    			reclamoExpressManager.guardaDetalleExpress(reclamoExpressDetalleVO);
    		}
    	}catch(Exception e){
    		logger.debug("Error al guardar detalle",e);
    		success=false;    		
    	}
    	
    	success = true;
    	return SUCCESS;
    }
    
    
    public String limpiaRFC(String rfc){
    	String rfcAux = "";
    	rfcAux = rfc.replace("-", "");
    	return rfcAux;
    }
    
    public String extraeNombre(String cadena){
    	String nombreAux = "";
    	nombreAux = cadena.substring(cadena.indexOf(" "));
    	return nombreAux;
    }
    
    
	public List<BaseVO> getDatosReclamos() {
		return datosReclamos;
	}
	
	public void setDatosReclamos(List<BaseVO> datosReclamos) {
		this.datosReclamos = datosReclamos;
	}

	public List<ReclamoExpressVO> getReclamoExpress() {
		return reclamoExpress;
	}

	public void setReclamoExpress(List<ReclamoExpressVO> reclamoExpress) {
		this.reclamoExpress = reclamoExpress;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

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
	
	public String getConducto() {
		return conducto;
	}

	public void setConducto(String conducto) {
		this.conducto = conducto;
	}
	
	public String getAtencionHosp() {
		return atencionHosp;
	}

	public void setAtencionHosp(String atencionHosp) {
		this.atencionHosp = atencionHosp;
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
	
	public List<HashMap<String, String>> getDatosTablas() {
		return datosTablas;
	}

	public void setDatosTablas(List<HashMap<String, String>> datosTablas) {
		this.datosTablas = datosTablas;
	}
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public List<BaseVO> getDatosSecuenciales() {
		return datosSecuenciales;
	}

	public void setDatosSecuenciales(List<BaseVO> datosSecuenciales) {
		this.datosSecuenciales = datosSecuenciales;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
    
}
