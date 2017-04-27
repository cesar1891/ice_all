package mx.com.gseguros.wizard.configuracion.producto.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.conceptosCobertura.model.ConceptosCoberturaVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.service.ConceptosCoberturaManager;
import mx.com.gseguros.wizard.dao.WizardDAO;

import org.apache.log4j.Logger;

/**
 * Clase concreta que implementa los metodos de la interface
 * {@link ConceptosCoberturaManager} para insertar, modificar y eliminar los
 * conceptos de cobertura.
 * 
 * @since 1.0
 * @author <a href="mailto:edgar.perez@biosnettcs.com">Edgar Perez</a>
 * @version $Id$
 * @see ConceptosCoberturaManager
 */
public class ConceptosCoberturaManagerImpl implements ConceptosCoberturaManager {

	private static Logger logger = Logger
			.getLogger(ReglaValidacionManagerImpl.class);
	
	private WizardDAO wizardDAO;
	
	/**
	 * Asigna al Map de objetos de tipo {link Endpoint} con los vm que se
	 * pueden invocar.
	 * 
	 * @param endpoints
	 */
	//private Map<String, Endpoint> endpoints;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.biosnet.ice.wizard.productos.service.ConceptosCoberturaManager#obtieneConceptosPorCobertura(String
	 *      codigoRamo)
	 */
	public List<ConceptosCoberturaVO> obtieneConceptosPorCobertura(
			String codigoRamo) throws ApplicationException {
		List<ConceptosCoberturaVO> listaConceptosCobertura = null;
		Map<String, String> params = new HashMap<String, String>();
		params.put("PV_CDRAMO", codigoRamo);
		params.put("PV_CDTIPSIT", null);
		params.put("PV_CDGARANT", null);
		try {
			
			listaConceptosCobertura = wizardDAO.obtieneConceptosPorCobertura(params);

			if (listaConceptosCobertura == null) {
				throw new ApplicationException(
						"No exiten conceptos de coberturas");
			}
		} catch (Exception bae) {
			logger.error("Exception in invoke 'OBTIENE_CONCEPTOS_COBERTURA'",
					bae);
			throw new ApplicationException(
					"Error al cargar lista de conceptos de cobertura");
		}
		return listaConceptosCobertura;
	}

	public List<ConceptosCoberturaVO> obtieneConceptosPorCobertura(
			String codigoRamo, String codigoSituacion, String codigoCobertura)
			throws ApplicationException {
		List<ConceptosCoberturaVO> listaConceptosCobertura = null;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("PV_CDRAMO", codigoRamo);
		//params.put("PV_CDTIPSIT", null);
		//params.put("PV_CDGARANT", null);
		params.put("PV_CDTIPSIT",codigoSituacion);
		params.put("PV_CDGARANT",codigoCobertura);
		try {
			
			listaConceptosCobertura = wizardDAO.obtieneConceptosPorCobertura(params);

			if (listaConceptosCobertura == null) {
				throw new ApplicationException(
						"No exiten conceptos de coberturas");
			}
			
		} catch (Exception bae) {
			logger.error("Exception in invoke 'OBTIENE_CONCEPTOS_COBERTURA'",
					bae);
			throw new ApplicationException(
					"Error al cargar lista de conceptos de cobertura");
		}
		return listaConceptosCobertura;
	}

	public List<LlaveValorVO> obtieneListaPeriodos(String codigoRamo)
			throws ApplicationException {
		List<LlaveValorVO> listaPeriodos = null;
		try {
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("PV_CDRAMO_I", codigoRamo);
			
			listaPeriodos = wizardDAO.obtieneListaPeriodos(params);

			if (listaPeriodos == null) {
				throw new ApplicationException("No exiten periodos");
			}
		} catch (Exception bae) {
			logger.error("Exception in invoke 'OBTIENE_LISTA_PERIODOS'", bae);
			throw new ApplicationException("Error al cargar lista de periodos");
		}
		return listaPeriodos;
	}

	public List<LlaveValorVO> obtieneListaCoberturas(String codigoRamo)
			throws ApplicationException {
		List<LlaveValorVO> listaCobertura = null;
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("PV_CDRAMO_I", codigoRamo);
			
			listaCobertura = wizardDAO.obtieneListaCoberturas(params);
			
			if (listaCobertura == null) {
				throw new ApplicationException("No exiten coberturas");
			}
		} catch (Exception bae) {
			logger.error("Exception in invoke 'OBTIENE_LISTA_COBERTURA'", bae);
			throw new ApplicationException(
					"Error al cargar lista de coberturas");
		}
		return listaCobertura;
	}

	public List<LlaveValorVO> obtieneListaConceptos(String parametro)
			throws ApplicationException {
		List<LlaveValorVO> listaConceptos = null;
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("pv_tipcon", parametro);
			
			listaConceptos = wizardDAO.obtieneListaConceptos(params);
			
			if (listaConceptos == null) {
				throw new ApplicationException("No exite ningun concepto");
			}
		} catch (Exception bae) {
			logger.error("Exception in invoke 'OBTIENE_LISTA_CONCEPTOS'", bae);
			throw new ApplicationException(
					"Error al cargar los conceptos de conceptos por coberturas desde el sistema");
		}
		return listaConceptos;
	}

	public List<LlaveValorVO> obtieneListaComportamientos()
			throws ApplicationException {
		List<LlaveValorVO> listaComportamientos = null;
		try {
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("PI_TABLA", "CCOMPORTACONCEP");
			params.put("PI_ATRIB_DESC", null);
			params.put("PI_NOM_CLAVE01", null);
			params.put("PI_NOM_CLAVE02", null);
			params.put("PI_VAL_CLAVE02", null);
			params.put("PI_NOM_CLAVE03", null);
			params.put("PI_VAL_CLAVE03", null);
			params.put("PI_NOM_CLAVE04", null);
			params.put("PI_VAL_CLAVE04", null);
			params.put("PI_NOM_CLAVE05", null);
			params.put("PI_VAL_CLAVE05", null);
			
			listaComportamientos = wizardDAO.obtieneListaComportamientos(params);
			
			if (listaComportamientos == null) {
				throw new ApplicationException("No exite ningun comportamiento");
			}
		} catch (Exception bae) {
			logger.error("Exception in invoke 'OBTIENE_LISTA_COMPORTAMIENTOS'",
					bae);
			throw new ApplicationException(
					"Error al cargar los comportamientos de conceptos por coberturas desde el sistema");
		}
		return listaComportamientos;
	}

	public List<LlaveValorVO> obtieneListaCondiciones()
			throws ApplicationException {
		List<LlaveValorVO> listaCondiciones = null;
		try {
			Map<String, String> params = new HashMap<String, String>();
			
			listaCondiciones = wizardDAO.obtieneListaCondiciones(params);
			
			if (listaCondiciones == null) {
				throw new ApplicationException("No exite ninguna condicion");
			}
		} catch (Exception bae) {
			logger
					.error("Exception in invoke 'OBTIENE_LISTA_CONDICIONES'",
							bae);
			throw new ApplicationException(
					"Error al cargar las condiciones de conceptos por coberturas desde el sistema");
		}
		return listaCondiciones;
	}

	public void agregarConceptoPorCobertura(
			ConceptosCoberturaVO conceptoCobertura, String codigoRamo)
			throws ApplicationException {
		
		Map params = new HashMap<String, String>();
		params.put("PV_CDRAMO_I", codigoRamo);
		params.put("PV_CDPERIOD_I", conceptoCobertura.getCodigoPeriodo());
		params.put("PV_ORDEN_I", conceptoCobertura.getOrden());
		params.put("PV_CDGARANT_I", conceptoCobertura.getCodigoCobertura());
		params.put("PV_CDCONTAR_I", conceptoCobertura.getCodigoConcepto());
		params.put("PV_OTTIPO_I", conceptoCobertura.getCodigoComportamiento());
		params.put("PV_CDCONDIC_I", conceptoCobertura.getCodigoCondicion());
		try {
			wizardDAO.agregarConceptoPorCobertura(params);
		} catch (Exception bae) {
			logger.error("Exception in invoke AGREGAR_CONCEPTO_COBERTURA", bae);
			throw new ApplicationException(
					"Error al agregar concepto por cobertura");
		}

	}

	public void eliminaConceptosPorCobertura(String codigoRamo,
			String codigoPeriodo, String orden) throws ApplicationException {
		
		Map params = new HashMap<String, String>();
		params.put("PV_CDRAMO_I", codigoRamo);
		params.put("PV_CDPERIOD_I", codigoPeriodo);
		params.put("PV_ORDEN_I", orden);
		try {
			wizardDAO.eliminaConceptosPorCobertura(params);
		} catch (Exception bae) {
			logger.error("Exception in invoke ELIMINAR_CONCEPTO_COBERTURA",
							bae);
			throw new ApplicationException(
					"Error al eliminar concepto por cobertura");
		}

	}
	
	
	public void setWizardDAO(WizardDAO wizardDAO) {
		this.wizardDAO = wizardDAO;
	}


}
