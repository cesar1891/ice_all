package mx.com.gseguros.wizard.service;

import java.util.List;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.service.impl.CoberturaManagerImpl;
import mx.com.gseguros.wizard.model.CoberturaVO;

/**
 * CoberturaManager
 * 
 * Interface que contien los metodos expuestos por uno de los Manager para la
 * consulta, insercion, modificacion de las coberturas para los
 * productos.
 *
 * @since 1.0
 * @author <a href="mailto:adolfo.gonzalez@biosnettcs.com">Adolfo Gonzalez</a>
 * @version 2.0
 * @see CoberturaManagerImpl
 */
public interface CoberturaManager {

	/**
	 * Metodo que se utiliza para obtener la lista de coberturas 
	 * 
	 * @return Lista de objetos de tipo {@link LlaveValorVO}.
	 * @throws ApplicationException
	 *             Si existe algun error en base de datos.
	 */
	List<LlaveValorVO> obtieneCoberturas()throws ApplicationException;

	/**
	 * Metodo que se utiliza para obtener la lista de condiciones para las coberturas 
	 * 
	 * @return Lista de objetos de tipo {@link LlaveValorVO}.
	 * @throws ApplicationException
	 *             Si existe algun error en base de datos.
	 */
	List<LlaveValorVO> obtieneCondicionCobertura()throws ApplicationException;

	/**
	 * Metodo que se utiliza para obtener la lista de tipos de coberturas 
	 * 
	 * @return Lista de objetos de tipo {@link LlaveValorVO}.
	 * @throws ApplicationException
	 *             Si existe algun error en base de datos.
	 */
	List<LlaveValorVO> obtieneTipoCobertura() throws ApplicationException;

	/**
	 * Metodo que se utiliza para obtener la lista de ramos para las coberturas 
	 * 
	 * @return Lista de objetos de tipo {@link LlaveValorVO}.
	 * @throws ApplicationException
	 *             Si existe algun error en base de datos.
	 */
	List<LlaveValorVO> obtieneRamoCobertura()throws ApplicationException;

	/**
	 * Metodo que se utiliza para obtener la lista de suma asegurada para las coberturas 
	 * 
	 * @return Lista de objetos de tipo {@link LlaveValorVO}.
	 * @throws ApplicationException
	 *             Si existe algun error en base de datos.
	 */
	List<LlaveValorVO> obtieneSumaAseguradaCobertura(String codigoRamo)throws ApplicationException;
	
	/**
	 * Metodo que se utiliza para insertar la cobertura en la base de
	 * datos.
	 * 
	 * @param CoberturaVO
	 *            Los valores claveCobertura, descripcionCobertura, tipoCobertura
	 *            y ramoCobertura son <b>obligatorios</b> para poder invocar el metodo.
	 * @throws ApplicationException
	 *             Si existe un error en la base de datos al insertar.
	 */
	void insertaCobertura(CoberturaVO cobertura)throws ApplicationException;

	/**
	 * Metodo que se utiliza para asociar la cobertura al inciso en la base de
	 * datos.
	 * 
	 * @param CoberturaVO
	 *            Los valores claveCobertura, codigoRamo y codigoTipoSituacion
	 *            son <b>obligatorios</b> para poder invocar el metodo.
	 * @throws ApplicationException
	 *             Si existe un error en la base de datos al insertar.
	 */
	void asociaCobertura(CoberturaVO cobertura)throws ApplicationException;

	CoberturaVO obtieneCoberturaAsociada(String codigoRamo,
			String codigoTipoSituacion, String claveCobertura)throws ApplicationException;

}
