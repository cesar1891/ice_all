package mx.com.gseguros.wizard.configuracion.producto.service;

import java.util.List;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.sumaAsegurada.model.SumaAseguradaIncisoVO;
import mx.com.gseguros.wizard.configuracion.producto.sumaAsegurada.model.SumaAseguradaVO;


/**
 * 
 * @author ricardo.bautista
 *
 */
public interface SumaAseguradaManagerJdbcTemplate {
	
	/**
	 * Metodo que se utiliza para agregar una suma asegurada.
	 * 
	 * @param sumaAseguradaInciso
	 *            Objeto de tipo {@link SumaAseguradaIncisoVO} que contiene los
	 *            valores de la suma asegurada.
	 * @throws ApplicationException
	 * @see {@link SumaAseguradaIncisoVO}
	 */
	void agregaSumaAseguradaInciso(SumaAseguradaIncisoVO sumaAseguradaInciso) throws ApplicationException;
	
	public List<LlaveValorVO> catalogoTipoSumaAsegurada()
			throws ApplicationException;
	
	public List<LlaveValorVO> catalogoSumaAsegurada(String codigoRamo)
			throws ApplicationException;
	
	public List<LlaveValorVO> catalogoMonedaSumaASegurada()
			throws ApplicationException;
	
	public List<SumaAseguradaVO> listaSumaAsegurada(String codigoRamo,
			String codigoCapital) throws ApplicationException;
	
	public List<SumaAseguradaIncisoVO> listaSumaAseguradaInciso(
			String codigoRamo, String codigoCobertura, String codigoCapital, String codigoTipoSituacion)
			throws ApplicationException;
	
	public void eliminaSumaAseguradaProducto(String codigoCapital,
			String codigoRamo) throws ApplicationException ;
	
	public void eliminaSumaAseguradaInciso(String codigoCapital,
			String codigoRamo, String codigoTipoSituacion)
	throws ApplicationException;

}
