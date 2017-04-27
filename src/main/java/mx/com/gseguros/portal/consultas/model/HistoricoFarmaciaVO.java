package mx.com.gseguros.portal.consultas.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MAVR
 *
 */
public class HistoricoFarmaciaVO implements Serializable{

	private static final long serialVersionUID = -8555353864912795413L;

	/**
	 * Codigo de la persona
	 */
	private String cdperson;	
	private String tigrupo;
	private String total;
	private String poliza;
	private String estatus;
	private String dtfecini;
	private String dtfecfin;
	private String iultimoafiliado;
	private String maximo;
	private String orden;
	private String iultimo;
	private String pendiente;
	private String gastototal;
	private String disponible;
	
	
	
	public String getCdperson() {
		return cdperson;
	}



	public void setCdperson(String cdperson) {
		this.cdperson = cdperson;
	}



	public String getTigrupo() {
		return tigrupo;
	}



	public void setTigrupo(String tigrupo) {
		this.tigrupo = tigrupo;
	}



	public String getTotal() {
		return total;
	}



	public void setTotal(String total) {
		this.total = total;
	}



	public String getPoliza() {
		return poliza;
	}



	public void setPoliza(String poliza) {
		this.poliza = poliza;
	}



	public String getEstatus() {
		return estatus;
	}



	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}



	public String getDtfecini() {
		return dtfecini;
	}



	public void setDtfecini(String dtfecini) {
		this.dtfecini = dtfecini;
	}



	public String getDtfecfin() {
		return dtfecfin;
	}



	public void setDtfecfin(String dtfecfin) {
		this.dtfecfin = dtfecfin;
	}



	public String getIultimoafiliado() {
		return iultimoafiliado;
	}



	public void setIultimoafiliado(String iultimoafiliado) {
		this.iultimoafiliado = iultimoafiliado;
	}



	public String getMaximo() {
		return maximo;
	}



	public void setMaximo(String maximo) {
		this.maximo = maximo;
	}

	
	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}



	public String getIultimo() {
		return iultimo;
	}



	public void setIultimo(String iultimo) {
		this.iultimo = iultimo;
	}



	public String getPendiente() {
		return pendiente;
	}



	public void setPendiente(String pendiente) {
		this.pendiente = pendiente;
	}

	

	public String getGastototal() {
		return gastototal;
	}



	public void setGastototal(String gastototal) {
		this.gastototal = gastototal;
	}



	public String getDisponible() {
		return disponible;
	}



	public void setDisponible(String disponible) {
		this.disponible = disponible;
	}



	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}		
}