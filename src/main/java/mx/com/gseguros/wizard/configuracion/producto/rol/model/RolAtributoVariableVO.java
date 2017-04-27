package mx.com.gseguros.wizard.configuracion.producto.rol.model;

import java.io.Serializable;

/**
 * Clase VO que contien los datos del rol y sus atributos para agregarlos al catalogo.
 * 
 * @since 1.0
 * @author <a href="mailto:edgar.perez@biosnettcs.com">Edgar Perez</a>
 * @version $Id$
 */
public class RolAtributoVariableVO implements Serializable{
	
	private static final long serialVersionUID = -9123450075336556889L;
	
	/**
	 * Codigo de ramo asociado al rol.
	 */
	private int cdRamo;
	
	/**
	 * Codigo de rol asociado al inciso.
	 */
	private String codigoRol;
	
	/**
	 * Codigo del atributo variable asociado al rol.
	 */
	private String codigoAtributoVariable;
	
	/**
	 * Descripcion del atributo variable asociado al rol.
	 */
	private String descripcionAtributoVariable;
	
	/**
	 * Obligatoriedad del rol.
	 */
	private String switchObligatorio;
	
	/**
	 * Codigo del valor de la tabla asociado al rol.
	 */
	private String ottabval;
	
	/**
	 * Codigo del nivel asociado al rol.
	 */
	private String codigoNivel;
	
	/**
	 * Codigo del tipo de situacion asociada al rol.
	 */
	private String codigoTipsit;
	
	/**
	 * Codigo de la lista de valores asociado al rol.
	 */
	private String codigoListaDeValores;
	
	/**
	 * Descripcion de la lista de valores asociada al rol.
	 */
	private String descripcionListaDeValores;
	
	/**
	 * Formato del rol asociado.
	 */
	private String formato;
	
	/**
	 * Numero maximo sociado al rol.
	 */
	private String maximo;
	
	/**
	 * Numero minimo asociado al rol.
	 */
	private String minimo;
	
	/**
	 * Atributo que indica si aparece el cotizador del rol.
	 */
	private String apareceCotizador;
	
	/**
	 * Atributo que indica si el cotizador del rol es modificable.
	 */
	private String modificaCotizador;
	
	/**
	 * Dato complementario del rol.
	 */
	private String datoComplementario;
	
	/**
	 * Obligatoriedad para el dato complementario del rol.
	 */
	private String obligatorioComplementario;
	
	/**
	 * Atributo que indica si es modificable el dato complementario del rol.
	 */
	private String modificableComplementario;
	
	/**
	 * Atributo que indica si aparece el endoso del rol.
	 */
	private String apareceEndoso;
	
	/**
	 * Obligatoriedad para el endoso del rol.
	 */
	private String obligatorioEndoso;
	
	/**
	 * Atributo que indica si es modificable el endoso del rol.
	 */
	private String modificaEndoso;
	
	/**
	 * Codigo de la expresion asociada al atributo del rol.
	 */
	private String codigoExpresion;
	
	private String codigoPadre;
    private String agrupador;
    private String orden;
    private String codigoCondicion;
    private String descripcionPadre;
    private String descripcionCondicion;
    private String retarificacion;
	
	//GETTERS & SETTERS
	
	/**
	 * Retorna codigo de ramo del rol.
	 * @return the cdRamo
	 */
	public int getCdRamo() {
		return cdRamo;
	}
	
	/**
	 * Asigna codigo de ramo del rol
	 * @param cdRamo the cdRamo to set
	 */
	public void setCdRamo(int cdRamo) {
		this.cdRamo = cdRamo;
	}
	
	/**
	 * Retorna codigo de rol.
	 * @return the codigoRol
	 */
	public String getCodigoRol() {
		return codigoRol;
	}
	
	/**
	 * Asigna codigo de rol
	 * @param codigoRol the codigoRol to set
	 */
	public void setCodigoRol(String codigoRol) {
		this.codigoRol = codigoRol;
	}
	
	/**
	 * Retorna codigo de atributo variable del rol
	 * @return the codigoAtributoVariable
	 */
	public String getCodigoAtributoVariable() {
		return codigoAtributoVariable;
	}
	
	/**
	 * Asigna codigo de atributo variable del rol
	 * @param codigoAtributoVariable the codigoAtributoVariable to set
	 */
	public void setCodigoAtributoVariable(String codigoAtributoVariable) {
		this.codigoAtributoVariable = codigoAtributoVariable;
	}
	
	/**
	 * Retorna obligatoriedad del rol
	 * @return the switchObligatorio
	 */
	public String getSwitchObligatorio() {
		return switchObligatorio;
	}
	
	/**
	 * Asigna obligatoriedad del rol
	 * @param switchObligatorio the switchObligatorio to set
	 */
	public void setSwitchObligatorio(String switchObligatorio) {
		this.switchObligatorio = switchObligatorio;
	}
	
	/**
	 * Retorna codigo del valor de la tabla del rol
	 * @return the ottabval
	 */
	public String getOttabval() {
		return ottabval;
	}
	
	/**
	 * Asigna codigo de valor de la tabla del rol
	 * @param ottabval the ottabval to set
	 */
	public void setOttabval(String ottabval) {
		this.ottabval = ottabval;
	}
	
	/**
	 * Retorna codigo de nivel del rol
	 * @return the codigoNivel
	 */
	public String getCodigoNivel() {
		return codigoNivel;
	}
	
	/**
	 * Asigna codigo de nivel del rol
	 * @param codigoNivel the codigoNivel to set
	 */
	public void setCodigoNivel(String codigoNivel) {
		this.codigoNivel = codigoNivel;
	}
	
	/**
	 * Retorna codigo de tipo de situacion del rol
	 * @return the codigoTipsit
	 */
	public String getCodigoTipsit() {
		return codigoTipsit;
	}
	
	/**
	 * Asigna codigo de tipo de situacion del rol
	 * @param codigoTipsit the codigoTipsit to set
	 */
	public void setCodigoTipsit(String codigoTipsit) {
		this.codigoTipsit = codigoTipsit;
	}
	/**
	 * Retorna descripcion del atributo variable del rol
	 * @return the descripcionAtributoVariable
	 */
	public String getDescripcionAtributoVariable() {
		return descripcionAtributoVariable;
	}
	
	/**
	 * Asigna descripcion del atributo variable del rol
	 * @param descripcionAtributoVariable
	 */
	public void setDescripcionAtributoVariable(String descripcionAtributoVariable) {
		this.descripcionAtributoVariable = descripcionAtributoVariable;
	}
	
	/**
	 * Retorna codigo de la lista de valores del rol
	 * @return the codigoListaDeValores
	 */
	public String getCodigoListaDeValores() {
		return codigoListaDeValores;
	}
	
	/**
	 * Asigna codigo de lista de valores del rol
	 * @param codigoListaDeValores
	 */
	public void setCodigoListaDeValores(String codigoListaDeValores) {
		this.codigoListaDeValores = codigoListaDeValores;
	}
	
	/**
	 * Retorna descripcion de lista de valores del rol
	 * @return the descripcionListaDeValores
	 */
	public String getDescripcionListaDeValores() {
		return descripcionListaDeValores;
	}
	
	/**
	 * Asigna descripcion de la lista de valores del rol
	 * @param descripcionListaDeValores
	 */
	public void setDescripcionListaDeValores(String descripcionListaDeValores) {
		this.descripcionListaDeValores = descripcionListaDeValores;
	}
	
	/**
	 * Retorna formato del rol
	 * @return the formato
	 */
	public String getFormato() {
		return formato;
	}
	
	/**
	 * Asigna formato del rol
	 * @param formato
	 */
	public void setFormato(String formato) {
		this.formato = formato;
	}
	/**
	 * Retorna numero maximo del rol
	 * @return the maximo
	 */
	public String getMaximo() {
		return maximo;
	}
	/**
	 * Asigna numero maximo del rol
	 * @param maximo
	 */
	public void setMaximo(String maximo) {
		this.maximo = maximo;
	}
	/**
	 * Retorna numero minimo del rol
	 * @return the minimo
	 */
	public String getMinimo() {
		return minimo;
	}
	/**
	 * Asigna numero minimo del rol
	 * @param minimo
	 */
	public void setMinimo(String minimo) {
		this.minimo = minimo;
	}

	public String getApareceCotizador() {
		return apareceCotizador;
	}

	public void setApareceCotizador(String apareceCotizador) {
		this.apareceCotizador = apareceCotizador;
	}

	public String getModificaCotizador() {
		return modificaCotizador;
	}

	public void setModificaCotizador(String modificaCotizador) {
		this.modificaCotizador = modificaCotizador;
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

	public String getModificaEndoso() {
		return modificaEndoso;
	}

	public void setModificaEndoso(String modificaEndoso) {
		this.modificaEndoso = modificaEndoso;
	}

	public String getCodigoExpresion() {
		return codigoExpresion;
	}

	public void setCodigoExpresion(String codigoExpresion) {
		this.codigoExpresion = codigoExpresion;
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

	public String getDescripcionPadre() {
		return descripcionPadre;
	}

	public void setDescripcionPadre(String descripcionPadre) {
		this.descripcionPadre = descripcionPadre;
	}

	public String getDescripcionCondicion() {
		return descripcionCondicion;
	}

	public void setDescripcionCondicion(String descripcionCondicion) {
		this.descripcionCondicion = descripcionCondicion;
	}

	public String getRetarificacion() {
		return retarificacion;
	}

	public void setRetarificacion(String retarificacion) {
		this.retarificacion = retarificacion;
	}
	
}
