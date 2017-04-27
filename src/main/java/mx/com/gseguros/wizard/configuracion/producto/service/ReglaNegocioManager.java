package mx.com.gseguros.wizard.configuracion.producto.service;

import java.util.List;

import mx.com.aon.portal.service.PagedList;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.reglanegocio.model.ReglaNegocioVO;
import mx.com.gseguros.wizard.configuracion.producto.service.impl.ReglaNegocioManagerImpl;
import mx.com.gseguros.wizard.configuracion.producto.util.ReglaNegocio;

/**
 * interface que contien los metodos expuestos por uno de los Manager para la
 * consulta, insercion, modificacion de las reglas de negocio para los
 * productos, tales como:
 * <p>
 * <ul>
 * <li>Validaciones</li>
 * <li>Condiciones</li>
 * <li>Variables Temporales</li>
 * <li>Conceptos de Tarificacion</li>
 * <li>Autorizaciones</li>
 * </ul>
 * 
 * @since 1.0
 * @author <a href="mailto:adolfo.gonzalez@biosnettcs.com">Adolfo Gonzalez</a>
 * @version $Id$
 * @see ReglaNegocioManagerImpl
 */
public interface ReglaNegocioManager {

	/**
	 * Metodo que se utiliza para insertar la regla de negocio en la base de
	 * datos.
	 * 
	 * @param reglaNegocio
	 *            Los valores nombre, descripcion y codigo son <b>obligatorios</b>
	 *            para poder invocar el metodo.
	 * @throws ApplicationException
	 *             Si existe un error en la base de datos al insertar.
	 * @throws DatosInvalidosException
	 *             Si los parametros no son enviados correctamente.
	 */
	void insertarReglaNegocio(ReglaNegocioVO reglaNegocio)
			throws ApplicationException, Exception;

	/**
	 * Metodo que se utiliza para obtener la lista de reglase de negocio segun
	 * su tipo.
	 * 
	 * @param reglaNegocio
	 *            Tipo de regla de negocio que se desea buscar, este valor es
	 *            <b>obligatorio</b>
	 * @return Lista de objetos de tipo {@link ReglaNegocioVO}.
	 * @throws ApplicationException
	 *             Si existe algun error en base de datos.
	 */
	List<ReglaNegocioVO> obtenerReglasNegocio(ReglaNegocio reglaNegocio)
			throws ApplicationException;
	
	public PagedList obtenerReglasNegocioPagedList(ReglaNegocio reglaNegocio, int start, int limit) 
			throws ApplicationException;

	/**
	 * Metodo que se utiliza para obtener la lista de valores del Tipo para los
	 * Conceptos de Tarificacion.
	 * 
	 * @return Lista de objetos de tipo {@link LlaveValorVO}.
	 * @throws ApplicationException
	 *             Si existe algun error en la base de datos.
	 */
	List<LlaveValorVO> obtenerTiposConceptosTarificacion()
			throws ApplicationException;

	/**
	 * Metodo que se utiliza para obtener la lista de valores del 'Tipo' para
	 * las Validaciones.
	 * 
	 * @return Lista de objetos de tipo {@link LlaveValorVO}
	 */
	List<LlaveValorVO> obtenerTiposValidaciones();

	/**
	 * Metodo que se utiliza para obtener las variables temporales asociadas al producto.
	 * 
	 * @return Lista de objetos de tipo {@link ReglaNegocioVO}
	 */
	List<ReglaNegocioVO> obtenerVariablesTemporalesAsociadasAlProducto(
			String cdramo) throws ApplicationException;

	/**
	 * Metodo que se utiliza para asociar las variables temporales al producto.
	 * 
	 * @return Lista de objetos de tipo {@link ReglaNegocioVO}
	 */
	void asociarVariablesDelProducto(
			List<ReglaNegocioVO> listaReglaNegocioVariables, String codigoRamo) throws ApplicationException;

	/**
	 * Metodo que se utiliza para desasociar las variables temporales al producto.
	 * 
	 * @return Lista de objetos de tipo {@link ReglaNegocioVO}
	 */
	void desasociarVariablesDelProducto(List<ReglaNegocioVO> list,
			String codigoRamo)throws ApplicationException;
	
	
	/**
	 * Metodo que borra una variable temporal de las librerias
	 * @param cdVariat
	 * @return
	 * @throws ApplicationException
	 */public String borraVarTmp(String cdVariat) throws ApplicationException;
}
