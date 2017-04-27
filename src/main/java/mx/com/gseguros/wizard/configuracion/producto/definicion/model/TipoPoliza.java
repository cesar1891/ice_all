/**
 * 
 */
package mx.com.gseguros.wizard.configuracion.producto.definicion.model;

/**
 * @author Adolfo
 *
 */
public class TipoPoliza {

	private String codigoPoliza;
	
	private boolean marcado;

	public TipoPoliza(String codigoPoliza, boolean marcado) {
		this.codigoPoliza = codigoPoliza;
		this.marcado = marcado;
	}
	
	/**
	 * @return the codigoPoliza
	 */
	public String getCodigoPoliza() {
		return codigoPoliza;
	}

	/**
	 * @return the marcado
	 */
	public boolean isMarcado() {
		return marcado;
	}
}