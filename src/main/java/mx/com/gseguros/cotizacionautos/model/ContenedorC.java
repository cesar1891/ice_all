package mx.com.gseguros.cotizacionautos.model;

import java.io.Serializable;

public class ContenedorC implements Serializable {
	private static final long serialVersionUID = 1L;
	
	TipoMoneda colMoneda;
	TipoClickCompra colClickCompra;
	
	public TipoMoneda getColMoneda() {
		return colMoneda;
	}
	public void setColMoneda(TipoMoneda colMoneda) {
		this.colMoneda = colMoneda;
	}
	public TipoClickCompra getColClickCompra() {
		return colClickCompra;
	}
	public void setColClickCompra(TipoClickCompra colClickCompra) {
		this.colClickCompra = colClickCompra;
	}
	public ContenedorC(TipoMoneda colMoneda, TipoClickCompra colClickCompra) {
		this.colMoneda = colMoneda;
		this.colClickCompra = colClickCompra;
	}
	public ContenedorC() {
	}
}
