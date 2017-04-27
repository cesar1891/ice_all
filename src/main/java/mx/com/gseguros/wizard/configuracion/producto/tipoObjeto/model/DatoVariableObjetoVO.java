package mx.com.gseguros.wizard.configuracion.producto.tipoObjeto.model;

import java.io.Serializable;

/**
 * Clase VO que contien los datos variables del tipo de objeto.
 * 
 * @since 1.0
 * @author <a href="mailto:edgar.perez@biosnettcs.com">Edgar Perez</a>
 * @version $Id$
 */
public class DatoVariableObjetoVO implements Serializable {

	
	private static final long serialVersionUID = -2381016273907865108L;
	
	private String codigoObjeto;
	/**
	 * Codigo del atributo variable para el tipo de objeto asociado al inciso.
	 */
	private String codigoAtributoVariable;
	/**
	 * Descripcion del atributo variable para el tipo de objeto asociado al inciso.
	 */
	private String descripcionAtributoVariable;   
	private String codigoFormato;
	private String minimo; 
	private String maximo;  
	/**
	 * obligatoriedad del atributo variable para el tipo de objeto asociado al inciso.
	 */
	private String switchObligatorio;  
    private String emision;      
    private String endoso;    
    private String retarificacion;
    private String codigoTabla;
    private String descripcionTabla;
    
    /**
	*Atributos agregados para extender funcionalidad
	*
	*/
    private String codigoPadre;
    private String agrupador;
    private String orden;
    private String codigoCondicion;
    private String dsAtributoPadre;
    private String dsCondicion;
    
    /**
	 * Atributo que indica si aparece el cotizador del tipo de objeto.
	 */
	private String apareceCotizador;
	
	/**
	 * Dato complementario del tipo de objeto.
	 */
	private String datoComplementario;
	
	/**
	 * Obligatoriedad para el dato complementario del tipo de objeto.
	 */
	private String obligatorioComplementario;
	
	/**
	 * Atributo que indica si es modificable el dato complementario del tipo de objeto.
	 */
	private String modificableComplementario;
	
	/**
	 * Atributo que indica si aparece el endoso del tipo de objeto.
	 */
	private String apareceEndoso;
	
	/**
	 * Obligatoriedad para el endoso del tipo de objeto.
	 */
	private String obligatorioEndoso;
	
	
	//GETTERS & SETTERS
	public String getCodigoFormato() {
		return codigoFormato;
	}
	public void setCodigoFormato(String codigoFormato) {
		this.codigoFormato = codigoFormato;
	}
	public String getMinimo() {
		return minimo;
	}
	public void setMinimo(String minimo) {
		this.minimo = minimo;
	}
	public String getMaximo() {
		return maximo;
	}
	public void setMaximo(String maximo) {
		this.maximo = maximo;
	}
	
	public String getEmision() {
		return emision;
	}
	public void setEmision(String emision) {
		this.emision = emision;
	}
	public String getEndoso() {
		return endoso;
	}
	public void setEndoso(String endoso) {
		this.endoso = endoso;
	}
	public String getRetarificacion() {
		return retarificacion;
	}
	public void setRetarificacion(String retarificacion) {
		this.retarificacion = retarificacion;
	}
	public String getCodigoTabla() {
		return codigoTabla;
	}
	public void setCodigoTabla(String codigoTabla) {
		this.codigoTabla = codigoTabla;
	}
	public String getDescripcionTabla() {
		return descripcionTabla;
	}
	public void setDescripcionTabla(String descripcionTabla) {
		this.descripcionTabla = descripcionTabla;
	}
	public String getCodigoObjeto() {
		return codigoObjeto;
	}
	public void setCodigoObjeto(String codigoObjeto) {
		this.codigoObjeto = codigoObjeto;
	}
	public String getCodigoAtributoVariable() {
		return codigoAtributoVariable;
	}
	public void setCodigoAtributoVariable(String codigoAtributoVariable) {
		this.codigoAtributoVariable = codigoAtributoVariable;
	}
	public String getDescripcionAtributoVariable() {
		return descripcionAtributoVariable;
	}
	public void setDescripcionAtributoVariable(String descripcionAtributoVariable) {
		this.descripcionAtributoVariable = descripcionAtributoVariable;
	}
	public String getSwitchObligatorio() {
		return switchObligatorio;
	}
	public void setSwitchObligatorio(String switchObligatorio) {
		this.switchObligatorio = switchObligatorio;
	}
	public String getCodigoPadre() {
		return codigoPadre;
	}
	public void setCodigoPadre(String codigoPadre) {
		this.codigoPadre = codigoPadre;
	}
	public String getAgrupador() {
		return agrupador;
	}
	public void setAgrupador(String agrupador) {
		this.agrupador = agrupador;
	}
	public String getOrden() {
		return orden;
	}
	public void setOrden(String orden) {
		this.orden = orden;
	}
	public String getCodigoCondicion() {
		return codigoCondicion;
	}
	public void setCodigoCondicion(String codigoCondicion) {
		this.codigoCondicion = codigoCondicion;
	}
	public String getDsAtributoPadre() {
		return dsAtributoPadre;
	}
	public void setDsAtributoPadre(String dsAtributoPadre) {
		this.dsAtributoPadre = dsAtributoPadre;
	}
	public String getDsCondicion() {
		return dsCondicion;
	}
	public void setDsCondicion(String dsCondicion) {
		this.dsCondicion = dsCondicion;
	}
	public String getApareceCotizador() {
		return apareceCotizador;
	}
	public void setApareceCotizador(String apareceCotizador) {
		this.apareceCotizador = apareceCotizador;
	}
	public String getDatoComplementario() {
		return datoComplementario;
	}
	public void setDatoComplementario(String datoComplementario) {
		this.datoComplementario = datoComplementario;
	}
	public String getObligatorioComplementario() {
		return obligatorioComplementario;
	}
	public void setObligatorioComplementario(String obligatorioComplementario) {
		this.obligatorioComplementario = obligatorioComplementario;
	}
	public String getModificableComplementario() {
		return modificableComplementario;
	}
	public void setModificableComplementario(String modificableComplementario) {
		this.modificableComplementario = modificableComplementario;
	}
	public String getApareceEndoso() {
		return apareceEndoso;
	}
	public void setApareceEndoso(String apareceEndoso) {
		this.apareceEndoso = apareceEndoso;
	}
	public String getObligatorioEndoso() {
		return obligatorioEndoso;
	}
	public void setObligatorioEndoso(String obligatorioEndoso) {
		this.obligatorioEndoso = obligatorioEndoso;
	}
	
}
