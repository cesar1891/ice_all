/*
 *ICE-WIZARD
 * 
 * Creado el 02/04/2008 9:51:13 a.m. (dd/mm/aaaa hh:mm:ss)
 *
 * Copyright (C) Biosnet S.C.    
 * All rights reserved. Todos los derechos reservados.
 *
 * http://www.biosnetmx.com/
 */
package mx.com.gseguros.wizard.configuracion.producto.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.definicion.model.ClausulaVO;
import mx.com.gseguros.wizard.configuracion.producto.definicion.model.PeriodoVO;
import mx.com.gseguros.wizard.configuracion.producto.definicion.model.ProductoVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;

/**
 * ProductManager
 * 
 * <pre>
 *    Interfaz para petici�n de informaci�n de producto
 * &lt;Pre&gt;
 * 
 * &#064;author   &lt;a href=&quot;mailto:alfonso.marquez@biosnetmx.com&quot;&gt;Alfonso M&amp;aacuterquez&lt;/a&gt;
 * &#064;version	 2.0
 * 
 * &#064;since	 1.0
 * 
 */
public interface ProductoManager {

	/**
	 * Se consultan los datos del producto para editarlo apartir de su id.
	 * 
	 * @param idProducto -
	 *            identificador del producto.
	 * @return Bean generado apartir de la consulta.
	 * @throws ApplicationException -
	 *             Excepcion con la informacion y descripci�n del problema en la
	 *             ejecuci�n.
	 */
	public abstract ProductoVO editarProducto(String idProducto)
			throws ApplicationException;

	/**
	 * Se inserta el producto en la base apartir de los datos capturados.
	 * 
	 * @param productoVO -
	 *            Bean con los datos para realizar la insercion del producto.
	 * @return boleano indica si ocurrio algun problema durante la ejecucion el
	 *         metodo.
	 * @throws ApplicationException -
	 *             Excepcion con la informacion y descripci�n del problema en la
	 *             ejecuci�n.
	 */
	public abstract String insertarProducto(ProductoVO productoVO, boolean success)
			throws ApplicationException;

	/**
	 * Se consultan los id's y las descripciones de productos para evitar que se
	 * repitan.
	 * 
	 * @return Lista de Bean's generados a partir de la consulta.
	 * @throws ApplicationException -
	 *             Excepcion con la informacion y descripci�n del problema en la
	 *             ejecuci�n.
	 */
	public abstract List<ProductoVO> validarProductos()
			throws ApplicationException;

	public abstract boolean validacionDePeriodo(String finAnterior,
			Date inicio, Date fin, String inicioPosterior)
			throws ApplicationException, ParseException;

	public abstract boolean validacionDePeriodo(String finAnterior,
			Date inicio, Date fin) throws ApplicationException, ParseException;

	/**
	 * Se agregan los periodos de valides al producto en la base apartir de los
	 * recientemente relacionados.
	 * 
	 * @param periodos -
	 *            Lista de Bean's recientemente relacionados al producto.
	 * @param idProducto -
	 *            Identificador del Producto, sirve para comparar los periodos
	 *            ya relacionados.
	 * @return boleano indica si ocurrio algun problema durante la ejecucion el
	 *         metodo.
	 * @throws ApplicationException -
	 *             Excepcion con la informacion y descripci�n del problema en la
	 *             ejecuci�n.
	 */
	public abstract void agregarPeriodos(List<PeriodoVO> periodos,
			int idProducto, boolean success) throws ApplicationException;

	/**
	 * Se extraen todos los periodos de valides realcionados con un producto.
	 * 
	 * @param productoId -
	 *            Identificador del producto.
	 * @return Lista de Bean's generados a partir de la consulta.
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	public abstract List<PeriodoVO> periodosJson(int productoId)
			throws ApplicationException;

	/**
	 * Se agrega un tipo de clausula al catalogo en la base apartir de los datos
	 * capturados.
	 * 
	 * @param clausula -
	 *            Bean con los datos para realizar la insercion del producto.
	 * @return boleano indica si ocurrio algun problema durante la ejecucion el
	 *         metodo.
	 * @throws ApplicationException -
	 *             Excepcion con la informacion y descripci�n del problema en la
	 *             ejecuci�n.
	 */
	public abstract WrapperResultados agregarClausula(ClausulaVO clausula, boolean success)
			throws ApplicationException;

	/**
	 * Se agregan las clausulas al producto en la base apartir de las
	 * recientemente relacionadas.
	 * 
	 * @param clausulas -
	 *            Lista de Bean's recientemente relacionados al producto.
	 * @param idProducto -
	 *            Identificador del Producto, sirve para comparar las clausulas
	 *            ya relacionados.
	 * @return boleano indica si ocurrio algun problema durante la ejecucion el
	 *         metodo.
	 * @throws ApplicationException -
	 *             Excepcion con la informacion y descripci�n del problema en la
	 *             ejecuci�n.
	 */
	public abstract void asociarClausulas(List<ClausulaVO> clausulas,
			int idProducto, boolean success) throws ApplicationException;

	/**
	 * Se extraen todos las clausulas asociadas a un producto.
	 * 
	 * @param productoId -
	 *            Identificador del producto.
	 * @return Lista con la informacion de todos las clausulas asociadas al
	 *         producto solicitado.
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	public abstract List<ClausulaVO> clausulasJson(int productoId)
			throws ApplicationException;

	/**
	 * Se extraen todos las clausulas asociadas a un producto.
	 * 
	 * @param productoId -
	 *            Identificador del producto.
	 * @return Lista con la informacion de todos las clausulas asociadas al
	 *         producto solicitado.
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	public abstract List<ClausulaVO> catalogoClausulasJson()
			throws ApplicationException;

	public abstract List<LlaveValorVO> catalogoTipoProductoJson()
			throws ApplicationException;

	public abstract List<LlaveValorVO> catalogoTipoRamoJson()
			throws ApplicationException;

	public abstract List<LlaveValorVO> catalogoTipoPolizaJson()
			throws ApplicationException;

	public abstract List<LlaveValorVO> catalogoTipoSeguroJson()
			throws ApplicationException;

	public String generarProducto(String codigoProducto)
			throws ApplicationException;

	public String clonarProducto(String codigoProductoAnterior,
			String descripcionProducto,
			String langCode, String usuario) throws ApplicationException;

	public String generarProducto(String codigoProducto, String server, String usuario, String pass)
			throws ApplicationException;

}