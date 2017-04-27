/**
 * 
 */
package mx.com.gseguros.wizard.configuracion.producto.definicion.model;

import java.io.Serializable;

/**
 * @author paola
 *
 */
public class ClausulaVO  implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idBase;
    private String codigoClausula;
    private String descripcionClausula;
    private String descripcionLinea;
    private int numeroLineas;
    private int codigoRamo;

    
	public String getCodigoClausula() {
		return codigoClausula;
	}
	public void setCodigoClausula(String codigoClausula) {
		this.codigoClausula = codigoClausula;
	}
	public String getDescripcionClausula() {
		return descripcionClausula;
	}
	public void setDescripcionClausula(String descripcionClausula) {
		this.descripcionClausula = descripcionClausula;
	}
	public String getDescripcionLinea() {
		return descripcionLinea;
	}
	public void setDescripcionLinea(String descripcionLinea) {
		this.descripcionLinea = descripcionLinea;
	}
	public int getCodigoRamo() {
		return codigoRamo;
	}
	public void setCodigoRamo(int codigoRamo) {
		this.codigoRamo = codigoRamo;
	}
	public int getNumeroLineas() {
		return numeroLineas;
	}
	public void setNumeroLineas(int numeroLineas) {
		this.numeroLineas = numeroLineas;
	}
	public String getIdBase() {
		return idBase;
	}
	public void setIdBase(String idBase) {
		this.idBase = idBase;
	}

}
