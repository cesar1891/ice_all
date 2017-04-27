package mx.com.gseguros.cotizacionautos.model;

import java.io.Serializable;
import java.util.List;

public class AseguradoraVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	TipoImagen colImagen;
	String name;
	TipoConceptoTotal colConceptoTotal;	
	List panelTotales;
	TipoConceptoPrimerPago colConceptoPrimerPago;
	List panelPrimerPago;
	TipoConceptoPagosSub colConceptoPagosSubsecuentes;
	List panelPagosSubsecuentes;
	TipoConceptoCompra colConceptoCompra;
	List panelCompra;
	String coma;
	
	public TipoImagen getColImagen() {
		return colImagen;
	}
	public void setColImagen(TipoImagen colImagen) {
		this.colImagen = colImagen;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List getPanelTotales() {
		return panelTotales;
	}
	public void setPanelTotales(List panelTotales) {
		this.panelTotales = panelTotales;
	}
	public TipoConceptoTotal getColConceptoTotal() {
		return colConceptoTotal;
	}
	public void setColConceptoTotal(TipoConceptoTotal colConceptoTotal) {
		this.colConceptoTotal = colConceptoTotal;
	}
	public TipoConceptoPrimerPago getColConceptoPrimerPago() {
		return colConceptoPrimerPago;
	}
	public void setColConceptoPrimerPago(
			TipoConceptoPrimerPago colConceptoPrimerPago) {
		this.colConceptoPrimerPago = colConceptoPrimerPago;
	}
	public List getPanelPrimerPago() {
		return panelPrimerPago;
	}
	public void setPanelPrimerPago(List panelPrimerPago) {
		this.panelPrimerPago = panelPrimerPago;
	}
	public TipoConceptoPagosSub getColConceptoPagosSubsecuentes() {
		return colConceptoPagosSubsecuentes;
	}
	public void setColConceptoPagosSubsecuentes(
			TipoConceptoPagosSub colConceptoPagosSubsecuentes) {
		this.colConceptoPagosSubsecuentes = colConceptoPagosSubsecuentes;
	}
	public List getPanelPagosSubsecuentes() {
		return panelPagosSubsecuentes;
	}
	public void setPanelPagosSubsecuentes(List panelPagosSubsecuentes) {
		this.panelPagosSubsecuentes = panelPagosSubsecuentes;
	}
	public TipoConceptoCompra getColConceptoCompra() {
		return colConceptoCompra;
	}
	public void setColConceptoCompra(TipoConceptoCompra colConceptoCompra) {
		this.colConceptoCompra = colConceptoCompra;
	}
	public List getPanelCompra() {
		return panelCompra;
	}
	public void setPanelCompra(List panelCompra) {
		this.panelCompra = panelCompra;
	}
	public String getComa() {
		return coma;
	}
	public void setComa(String coma) {
		this.coma = coma;
	}

	
}
