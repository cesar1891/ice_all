package mx.com.gseguros.wizard.configuracion.producto.tipoObjeto.model;

import java.io.Serializable;

/**
 * Clase VO que contien los datos del tipo de objeto y sus atributos para agregarlos al catalogo.
 * 
 * @since 1.0
 * @author <a href="mailto:edgar.perez@biosnettcs.com">Edgar Perez</a>
 * @version $Id$
 */
public class TipoObjetoVO implements Serializable {

	private static final long serialVersionUID = 2054403794079966712L;

	/**
	 * Codigo de tipo de objeto asociado al inciso.
	 */
	private String codigoTipoObjeto;
	
	/**
	 * Descripcion del tipo de objeto asociado al inciso.
	 */
	private String descripcionTipoObjeto;
	
	/**
	 * Codigo del atributo variable para el tipo de objeto asociado al inciso.
	 */
	private String codigoAtributoVariable;
	
	/**
	 * Descripcion del atributo variable para el tipo de objeto asociado al inciso.
	 */
	private String descripcionAtributoVariable;
	
	/**
	 * Codigo valor del atributo variable para el tipo de objeto asociado al inciso.
	 */
	private String codigoValor;
	
	/**
	 * Descripcion valor del atributo variable para el tipo de objeto asociado al inciso.
	 */
	private String descripcionValor;
	
	/**
	 * obligatoriedad del atributo variable para el tipo de objeto asociado al inciso.
	 */
	private String switchObligatorio;

	/**
	 * codigo de tipo de situacion para el tipo de objeto asociado al inciso.
	 */
	private String codigoTipoSituacion;
	
	public String getCodigoTipoObjeto() {
		return codigoTipoObjeto;
	}

	public void setCodigoTipoObjeto(String codigoTipoObjeto) {
		this.codigoTipoObjeto = codigoTipoObjeto;
	}

	public String getDescripcionTipoObjeto() {
		return descripcionTipoObjeto;
	}

	public void setDescripcionTipoObjeto(String descripcionTipoObjeto) {
		this.descripcionTipoObjeto = descripcionTipoObjeto;
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

	public String getCodigoValor() {
		return codigoValor;
	}

	public void setCodigoValor(String codigoValor) {
		this.codigoValor = codigoValor;
	}

	public String getDescripcionValor() {
		return descripcionValor;
	}

	public void setDescripcionValor(String descripcionValor) {
		this.descripcionValor = descripcionValor;
	}

	public String getSwitchObligatorio() {
		return switchObligatorio;
	}

	public void setSwitchObligatorio(String switchObligatorio) {
		this.switchObligatorio = switchObligatorio;
	}

	public String getCodigoTipoSituacion() {
		return codigoTipoSituacion;
	}

	public void setCodigoTipoSituacion(String codigoTipoSituacion) {
		this.codigoTipoSituacion = codigoTipoSituacion;
	}


}
