package mx.com.gseguros.wizard.configuracion.producto.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal.service.impl.AbstractManagerJdbcTemplateInvoke;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.coberturas.model.CoberturaVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.service.CoberturaManager;
import mx.com.gseguros.wizard.dao.CatalogosWizardDAO;

import org.apache.log4j.Logger;


/**
 * Clase que implementa los metodos de la interface
 * {@link CoberturaManager} para agregar, asociar y obtener las listas
 * de coberturas asociadas a un producto
 * @author Edgar Perez
 * @version 2.0
 * @since 1.0
 * @see CoberturaManager
 * 
 */

public class CoberturaManagerImpl extends AbstractManagerJdbcTemplateInvoke implements CoberturaManager {

	/**
	 * Logger de la clase.
	 */
	private static Logger logger = Logger.getLogger(CoberturaManagerImpl.class);

	/**
	 * Mapa en el cual se introducen los Manager's para ser extraidos y
	 * utilizados como servicios.
	 */

	//private Map endpoints;

	/**
	 * Implementacion que extrae las coberturas de la BD.
	 * 
	 * 
	 * @return listaCoberturas List<LlaveValorVO> - Lista con la informacion de las
	 *         coberturas asociadas al producto.
	 *         
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	public List<LlaveValorVO> obtieneCoberturas() throws ApplicationException {
		
		List<LlaveValorVO> listaCoberturas = null;
				
		HashMap<String, Object> params = new HashMap<String, Object>();

		WrapperResultados result = this.returnBackBoneInvoke(params,
				CatalogosWizardDAO.OBTIENE_COBERTURAS);
		listaCoberturas = (List<LlaveValorVO>) result.getItemList();
		
		return listaCoberturas;
	}

	/**
	 * Implementacion que extrae las condiciones de las coberturas de la BD.
	 * 
	 * 
	 * @return listaCondiciones List<LlaveValorVO> - Lista con la informacion de las condiciones para las
	 *         coberturas asociadas al producto.
	 *         
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	public List<LlaveValorVO> obtieneCondicionCobertura()
			throws ApplicationException {
		
		List<LlaveValorVO> listaCondiciones = null;
		HashMap<String, Object> params = new HashMap<String, Object>();

		WrapperResultados result = this.returnBackBoneInvoke(params,
				CatalogosWizardDAO.OBTIENE_CONDICION_COBERTURA);
		listaCondiciones = (List<LlaveValorVO>) result.getItemList();
		
		return listaCondiciones;
	}

	/**
	 * Implementacion que extrae los ramos de las coberturas de la BD.
	 * 
	 * 
	 * @return listaRamo List<LlaveValorVO> - Lista con la informacion de los ramos para las
	 *         coberturas asociadas al producto.
	 *         
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	public List<LlaveValorVO> obtieneRamoCobertura()
			throws ApplicationException {

		List<LlaveValorVO> listaRamo = null;
		HashMap<String, Object> params = new HashMap<String, Object>();

		WrapperResultados result = this.returnBackBoneInvoke(params,
				CatalogosWizardDAO.OBTIENE_RAMO_COBERTURA);
		listaRamo = (List<LlaveValorVO>) result.getItemList();
		return listaRamo;
	}

	/**
	 * Implementacion que regresa los tipos de las coberturas.
	 * 
	 * 
	 * @return lista List<LlaveValorVO> - Lista con la informacion de los tipos de las
	 *         coberturas.
	 *         
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	public List<LlaveValorVO> obtieneTipoCobertura()
			throws ApplicationException {
		List<LlaveValorVO> lista = new ArrayList<LlaveValorVO>();

		LlaveValorVO valor = new LlaveValorVO();

		valor.setKey("S");
		valor.setValue("Simple");
		lista.add(valor);

		valor = new LlaveValorVO();
		valor.setKey("C");
		valor.setValue("Compuesta");
		lista.add(valor);

		return lista;
	}

	/**
	 * Metodo que se utiliza para la insercion en la base de datos de la cobertura
	 * 
	 * @param CoberturaVO cobertura
	 *            Parametros que se envian para el query.
	 * @throws ApplicationException
	 *             Si se genera una exception al insertar en la aplicacion.
	 */
	public void insertaCobertura(CoberturaVO cobertura)
			throws ApplicationException {

		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("PV_CDGARANT_I", cobertura.getClaveCobertura());
			params.put("PV_DSGARANT_I", cobertura.getDescripcionCobertura());
			params.put("PV_CDTIPOGA_I", cobertura.getTipoCobertura());
			params.put("PV_CDTIPRAM_I", cobertura.getRamoCobertura());
			params.put("PV_SWREAUTO_I", cobertura.getReinstalacion());
			params.put("PV_SWINFLAC_I", cobertura.getIndiceInflacionario());
			
			returnBackBoneInvoke(params, CatalogosWizardDAO.INSERTA_COBERTURA);

		} catch (Exception e) {
			throw new ApplicationException(
					"Error intentando insertar una nueva cobertura");
		}

	}

	/**
	 * Implementacion que extrae la suma asegurada de las coberturas de la BD.
	 * 
	 * 
	 * @return listaSumaAsegurada List<LlaveValorVO> - Lista con la informacion de la suma asegurada 
	 * para las coberturas asociadas al producto.
	 *         
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	public List<LlaveValorVO> obtieneSumaAseguradaCobertura(String codigoRamo)
			throws ApplicationException {

		List<LlaveValorVO> listaSumaAsegurada = null;
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("PV_CDRAMO_I", codigoRamo);
		
		WrapperResultados result = this.returnBackBoneInvoke(params,
				CatalogosWizardDAO.OBTIENE_SUMA_ASEGURADA_COBERTURA);
		listaSumaAsegurada = (List<LlaveValorVO>) result.getItemList();
		return listaSumaAsegurada;
	}

	/**
	 * Metodo que se utiliza para la asociacion en la base de datos de la cobertura
	 * 
	 * @param CoberturaVO cobertura
	 *            Parametros que se envian para el query.
	 * @throws ApplicationException
	 *             Si se genera una exception al insertar en la aplicacion.
	 */
	public void asociaCobertura(CoberturaVO cobertura)
			throws ApplicationException {

		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("PV_CDRAMO_I", cobertura.getCodigoRamo());
			params.put("PV_CDGARANT_I", cobertura.getClaveCobertura());
			params.put("PV_CDTIPSIT_I", cobertura.getCodigoTipoSituacion());
			params.put("PV_CDCAPITA_I", cobertura.getCodigoCapital());
			params.put("PV_CDCONDIC_I", cobertura.getCodigoCondicion());
			params.put("PV_SWINSERT_I", cobertura.getInserta());
			params.put("PV_SWOBLIGA_I", cobertura.getObligatorio());
			
			returnBackBoneInvoke(params, CatalogosWizardDAO.ASOCIA_COBERTURA);
			
		} catch (Exception e) {
			throw new ApplicationException(
					"Error intentando asociar una cobertura");
		}

	}

	public CoberturaVO obtieneCoberturaAsociada(String codigoRamo,
			String codigoTipoSituacion, String claveCobertura)throws ApplicationException {
		
		Map params = new HashMap<String, Object>();
		params.put("PV_CDRAMO_I", codigoRamo);
		params.put("PV_CDTIPSIT_I", codigoTipoSituacion);
		params.put("PV_CDGARANT_I", claveCobertura);
		CoberturaVO coberturaAsociada = null;
		try {
			WrapperResultados res = returnBackBoneInvoke(params, CatalogosWizardDAO.OBTIENE_COBERTURA_ASOCIADA);
			coberturaAsociada = (CoberturaVO) (res.getItemList().get(0));
        } catch (Exception e) {
            throw new ApplicationException("Error intentando obtener la cobertura asociada");
        }
		return coberturaAsociada;
	}

	
	/**
	 * Asigna al Map de objetos de tipo {@link Endpoint} con los vm que se
	 * pueden invocar.
	 * 
	 * @param endpoints
	 */
//	public void setEndpoints(Map endpoints) {
//		this.endpoints = endpoints;
//	}


}
