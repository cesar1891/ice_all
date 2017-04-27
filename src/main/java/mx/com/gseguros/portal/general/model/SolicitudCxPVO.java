package mx.com.gseguros.portal.general.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import mx.com.gseguros.utils.Utils;

public class SolicitudCxPVO implements Serializable {

	private static final long serialVersionUID = 7528394434792370991L;

	private String producto;
	private String numsol;
	private String cvedpto;
	private String fecha;
	private String tip_pro;
	private String id_pro;
	private String destino;
	private String destinoComp;
	private String nomdes;
	private String rfc;
	private String factura;
	private String benef;
	private String inomovtos;
	private String mtotalap;


	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }


	public String getProducto() {
		return producto;
	}


	public void setProducto(String producto) {
		this.producto = producto;
	}


	public String getNumsol() {
		return numsol;
	}


	public void setNumsol(String numsol) {
		this.numsol = numsol;
	}


	public String getCvedpto() {
		return cvedpto;
	}


	public void setCvedpto(String cvedpto) {
		this.cvedpto = cvedpto;
	}


	public String getFecha() {
		return fecha;
	}


	public void setFecha(String fecha) {
		this.fecha = fecha;
	}


	public String getTip_pro() {
		return tip_pro;
	}


	public void setTip_pro(String tip_pro) {
		this.tip_pro = tip_pro;
	}


	public String getId_pro() {
		return id_pro;
	}


	public void setId_pro(String id_pro) {
		this.id_pro = id_pro;
	}


	public String getDestino() {
		return destino;
	}


	public void setDestino(String destino) {
		this.destino = destino;
	}


	public String getNomdes() {
		return nomdes;
	}


	public void setNomdes(String nomdes) {
		this.nomdes = nomdes;
	}


	public String getRfc() {
		return rfc;
	}


	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	
	public String getFactura() {
		return factura;
	}


	public void setFactura(String factura) {
		this.factura = factura;
	}


	public String getBenef() {
		return benef;
	}


	public void setBenef(String benef) {
		this.benef = benef;
	}


	public String getInomovtos() {
		return inomovtos;
	}


	public void setInomovtos(String inomovtos) {
		this.inomovtos = inomovtos;
	}


	public String getMtotalap() {
		return mtotalap;
	}


	public void setMtotalap(String mtotalap) {
		this.mtotalap = mtotalap;
	}


	public String getDestinoComp() {
		return destinoComp;
	}


	public void setDestinoComp(String destinoComp) {
		this.destinoComp = destinoComp;
	}
	
}
