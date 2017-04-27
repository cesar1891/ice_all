/**
 * 
 */
package mx.com.gseguros.wizard.configuracion.producto.definicion.model;

import java.io.Serializable;



/**
 * @author paola
 *
 */
public class PeriodoVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idBase;
    private int codigoRamo;
    private int codigoPeriodo;
    private String inicioFormato;
    private String finFormato;
    private String inicio;//Sin formato
    private String fin;//Sin formato
 
 

    public String getFin() throws java.text.ParseException {       
      return fin;
    }
    public void setFin(String fin) {
        this.fin = fin;
    }
    public String getInicio() {        
        
        return inicio;        
    }
    public void setInicio(String inicio) {
        this.inicio = inicio;
    }
	public int getCodigoRamo() {
		return codigoRamo;
	}
	public void setCodigoRamo(int codigoRamo) {
		this.codigoRamo = codigoRamo;
	}
	public int getCodigoPeriodo() {
		return codigoPeriodo;
	}
	public void setCodigoPeriodo(int codigoPeriodo) {
		this.codigoPeriodo = codigoPeriodo;
	}
	public String getInicioFormato() {
		return inicioFormato;
	}
	public void setInicioFormato(String inicioFormato) {
		this.inicioFormato = inicioFormato;
	}
	public String getFinFormato() {
		return finFormato;
	}
	public void setFinFormato(String finFormato) {
		this.finFormato = finFormato;
	}
	public String getIdBase() {
		return idBase;
	}
	public void setIdBase(String idBase) {
		this.idBase = idBase;
	}

}
