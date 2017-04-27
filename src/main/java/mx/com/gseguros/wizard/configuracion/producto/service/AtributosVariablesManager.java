package mx.com.gseguros.wizard.configuracion.producto.service;

import java.util.List;

import mx.com.aon.portal.service.PagedList;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.atributosVariables.model.AtributosVariablesVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.model.MensajesVO;

/**
 * AtributosVariablesManager
 * 
 * <pre>
 *    Interfaz para petici�n de informaci�n de atributos variables
 * </pre>
 * 
 * @author   &lt;a href=&quot;mailto:edgar.perez@biosnetmx.com&quot;&gt;Edgar P&eacute;rez&lt;/a&gt;
 * @version	 2.0
 * @since	 1.0
 * 
 */
public interface AtributosVariablesManager {

	/**
	 * Extrae todos los atributos variables de un producto.
	 * 
	 * @param valor
	 * @return Lista con la informacion de todos los atributos variables de un
	 *         producto solicitado.
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	PagedList listaDeValoresAtributosVariables(String valor, int start, int limit) throws ApplicationException;

	/**
	 * 
	 * Agrega un atributo variable al producto
	 * 
	 * @param atributos
	 * @throws ApplicationException -
	 *  Es lanzada en errores de configuracion de aplicacion error en las consultas a BD.
	 */
	String guardarAtributosVariables(AtributosVariablesVO atributos) throws ApplicationException;

	MensajesVO guardarAtributosVariablesInciso(AtributosVariablesVO atributos) throws ApplicationException;

	MensajesVO guardarAtributosVariablesCobertura(AtributosVariablesVO atributos) throws ApplicationException;
	
	public MensajesVO eliminarAtributosVariables(AtributosVariablesVO atributos, int nivel) throws ApplicationException;

	AtributosVariablesVO obtenerAtributoVariablePorNivel(int nivel, AtributosVariablesVO avvo) throws ApplicationException;

	/**
	 * 
	 * @param cdTipoObjP
	 * @param codigoAtributoP
	 * @return
	 * @throws ApplicationException-
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	List<LlaveValorVO> getPadre(String cdTipSituacion, String codigoAtributoP) throws ApplicationException;

	List<LlaveValorVO> getPadreObjeto(String cdTipSituacion, String codigoAtributoP) throws ApplicationException;
	
	/**
	 * 
	 * @param cdRamo
	 * @param codigoAtributoP
	 * @return
	 * @throws ApplicationException
	 */
	List<LlaveValorVO> getPadrePoliza(String cdRamo,
			String codigoAtributoP) throws ApplicationException;
	
	/**
	 * 
	 * @param codigoRamo
	 * @param codigoGarantia
	 * @param cdTipSituacion
	 * @param codigoAtributoP
	 * @return
	 * @throws ApplicationException
	 */
	List<LlaveValorVO> getPadreGarantia(String codigoRamo, String codigoGarantia, String cdTipSituacion, String codigoAtributoP)
			throws ApplicationException;

	/**
	 * 
	 * @param codigoRamo
	 * @param codigoRol
	 * @param codigoAtributoP
	 * @return
	 * @throws ApplicationException
	 */
	List<LlaveValorVO> getPadreRol(String codigoRamo, String codigoRol, String codigoAtributoP) throws ApplicationException;
	
	/**
	 * Metodo que valida si un atributo variable tiene hijos asociados.
	 * @param endpointName Nombre del endpoint a invocar, dependiendo el nombre es el nivel que se esta validando(p.ej producto, cobertura, etc)
	 * @param atributo Parametros utilizados por el endpoint..
	 * @return true si tiene hijos asociados, false si no los tiene.
	 * @throws ApplicationException
	 */
	public boolean tieneHijosAtributoVariableProducto(AtributosVariablesVO atributo) throws ApplicationException;
	public boolean tieneHijosAtributoVariableIncisoRiesgo(AtributosVariablesVO atributo) throws ApplicationException;
	public boolean tieneHijosAtributoVariableCobertura(AtributosVariablesVO atributo) throws ApplicationException;

}
