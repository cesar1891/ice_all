package mx.com.gseguros.wizard.configuracion.producto.reglanegocio.model;

import java.io.Serializable;

import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.ExpresionVO;
import mx.com.gseguros.wizard.configuracion.producto.util.ReglaNegocio;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * Clase VO que contien los datos de las reglas de negocio y su expresion.
 * 
 * @since 1.0
 * @author <a href="mailto:adolfo.gonzalez@biosnettcs.com">Adolfo Gonzalez</a>
 * @version $Id$
 */
public class ReglaNegocioVO implements Serializable{

	
	private static final long serialVersionUID = 3712927998132480856L;
	/**
	 * Booleano que indica si la regla de negocio esta siendo cargada desde 
	 * la base o de forma temporal desde la pantalla.
	 */
	private boolean temporal;
	/**
	 * Nombre de la regla de negocio.
	 */
	private String nombre;

	/**
	 * Descripcion de la regla de negocio.
	 */
	private String descripcion;

	/**
	 * Tipo de la regla de negocio, aplica solo para Conceptos de Tarificacion y
	 * Validacion.
	 */
	private String tipo;

	/**
	 * Descripcion de tipo.
	 */
	private String descripcionTipo;

	/**
	 * Mensaje de la regla de negocio, aplica solo para Validaciones.
	 */
	private String mensaje;

	/**
	 * Nivel de la regla de negocio, aplica solo para Autorizaciones.
	 */
	private String nivel;

	/**
	 * Codigo que identifica que tipo de regla de negocio es.
	 */
	private ReglaNegocio codigo;

	/**
	 * Expresion de la regla de negocio.
	 */
	private ExpresionVO expresion;

	/**
	 * Codigo de la expresion.
	 */
	private String codigoExpresion;

	/**
	 * Retorna Nombre de la regla de negocio.
	 * 
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Asigna Nombre de la regla de negocio.
	 * 
	 * @param nombre
	 *            the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Retorna Descripcion de la regla de negocio.
	 * 
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Asigna Descripcion de la regla de negocio.
	 * 
	 * @param descripcion
	 *            the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Retorna Tipo de la regla de negocio, aplica solo para Conceptos de
	 * Tarificacion y Validacion.
	 * 
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * Asigna Tipo de la regla de negocio, aplica solo para Conceptos de
	 * Tarificacion y Validacion.
	 * 
	 * @param tipo
	 *            the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * Retorna Mensaje de la regla de negocio, aplica solo para Validaciones.
	 * 
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * Asigna Mensaje de la regla de negocio, aplica solo para Validaciones.
	 * 
	 * @param mensaje
	 *            the mensaje to set
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * Retorna Nivel de la regla de negocio, aplica solo para Autorizaciones.
	 * 
	 * @return the nivel
	 */
	public String getNivel() {
		return nivel;
	}

	/**
	 * Asigna Nivel de la regla de negocio, aplica solo para Autorizaciones.
	 * 
	 * @param nivel
	 *            the nivel to set
	 */
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	/**
	 * Retorna Codigo que identifica que tipo de regla de negocio es.
	 * 
	 * @return the codigo
	 */
	public ReglaNegocio getCodigo() {
		return codigo;
	}

	/**
	 * Asigna Codigo que identifica que tipo de regla de negocio es.
	 * 
	 * @param codigo
	 *            the codigo to set.
	 *            <p>
	 *            Se utiliza el enum {@link ReglaNegocio} para insertar el
	 *            codigo.
	 */
	public void setCodigo(ReglaNegocio codigo) {
		this.codigo = codigo;
	}

	/**
	 * Retorna Expresion de la regla de negocio.
	 * 
	 * @return the expresion
	 */
	public ExpresionVO getExpresion() {
		return expresion;
	}

	/**
	 * Asigna Expresion de la regla de negocio.
	 * 
	 * @param expresion
	 *            the expresion to set
	 */
	public void setExpresion(ExpresionVO expresion) {
		this.expresion = expresion;
	}

	/**
	 * Retorna el codigo de la expresion.
	 * 
	 * @return the codigoExpresion
	 */
	public String getCodigoExpresion() {
		return codigoExpresion;
	}

	/**
	 * Asigna el codigo de la expresion.
	 * 
	 * @param codigoExpresion
	 *            the codigoExpresion to set
	 */
	public void setCodigoExpresion(String codigoExpresion) {
		this.codigoExpresion = codigoExpresion;
	}

	/**
	 * Retornar la descripcion de tipo.
	 * 
	 * @return the descripcionTipo
	 */
	public String getDescripcionTipo() {
		return descripcionTipo;
	}

	/**
	 * Asignar la descripcion de tipo
	 * 
	 * @param descripcionTipo
	 *            the descripcionTipo to set
	 */
	public void setDescripcionTipo(String descripcionTipo) {
		this.descripcionTipo = descripcionTipo;
	}

	/**
	 * @return the temporal
	 */
	public boolean isTemporal() {
		return temporal;
	}

	/**
	 * @param temporal the temporal to set
	 */
	public void setTemporal(boolean temporal) {
		this.temporal = temporal;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}

}
